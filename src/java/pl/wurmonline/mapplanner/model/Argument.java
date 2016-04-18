package pl.wurmonline.mapplanner.model;

import java.util.UUID;
import java.util.stream.Stream;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.wurmonline.mapplanner.util.Log;

public final class Argument<T> implements XMLSerializable {
    
    private final ArgumentData<T> data;
    
    private final UUID id;
    private final StringProperty title;
    private final Block block;
    private final ObjectProperty<ArgumentState> state;
    private T value;
    private final ObjectProperty<Argument<T>> input;
    private final String initialInputUUID;
    
    private Node editor;
    
    Argument(Block block, ArgumentData<T> data) {
        this.id = UUID.randomUUID();
        this.title = new SimpleStringProperty(data.getDefaultTitle());
        this.state = new SimpleObjectProperty<>(data.getDefaultState());
        this.block = block;
        this.data = data;
        this.value = null;
        this.input = new SimpleObjectProperty<>();
        this.initialInputUUID = "";
        
        T value = data.getDefaultValue();
        if (value != null) {
            setValue(value);
        }
        
        createListeners();
    }
    
    Argument(Block block, ArgumentData<T> data, Element root) {
        boolean isOutput = root.getTagName().equals("output");
        
        this.id = UUID.fromString(root.getAttribute("uuid"));
        this.title = new SimpleStringProperty(root.getAttribute("title"));
        this.block = block;
        this.data = data;
        this.input = new SimpleObjectProperty<>();
        
        if (isOutput) {
            this.state = new SimpleObjectProperty<>(ArgumentState.EXTERNAL);
        }
        else {
            this.state = new SimpleObjectProperty<>(ArgumentState.valueOf(root.getAttribute("state")));
            
            if (root.hasAttribute("value")) {
                this.value = data.deserializeValue(root.getAttribute("value"));
            }
            else {
                T value = data.getDefaultValue();
                if (value != null) {
                    this.value = value;
                }
            }
            
            if (state.get() == ArgumentState.PARAMETER) {
                block.getBlueprint().addProperty(this);
            }
        }
        
        this.initialInputUUID = root.getAttribute("input");
        
        createListeners();
    }
    
    public void recreateLinks() {
        if (initialInputUUID.equals("")) {
            return;
        }
        input.set(block.getBlueprint().lookupExternalInputs(initialInputUUID));
    }
    
    private void createListeners() {
        state.addListener((observable, oldValue, newValue) -> {
            switch (oldValue) {
                case EXTERNAL:
                    if (input.get() != null) {
                        input.set(null);
                    }
                    block.removeExternalInput(this);
                    break;
                case PARAMETER:
                    block.getBlueprint().removeProperty(this);
                    break;
            }

            switch (newValue) {
                case EXTERNAL:
                    block.addExternalInput(this);
                    break;
                case PARAMETER:
                    block.getBlueprint().addProperty(this);
                    break;
            }
        });
        
        input.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Log.info(this, "Linked with argument " + newValue.toString() + ".");
            }
            else {
                Log.info(this, "Argument is no longer linked with anything.");
            }
        });
    }
    
    public Element serialize(Document doc) {
        boolean isOutput = Stream.of(block.getOutputs())
                .anyMatch((arg) -> arg == this);
        
        Element root = doc.createElement(isOutput ? "output" : "input");
        root.setAttribute("uuid", id.toString());
        root.setAttribute("title", title.get());
        root.setAttribute("data", data.getIdentifier());
        
        if (!isOutput) {
            root.setAttribute("state", state.get().toString());
            if (input.get() == null && data.getDefaultValue() != null) {
                root.setAttribute("value", data.serializeValue(value));
            }
            else if (input.get() != null) {
                root.setAttribute("input", input.get().getId());
            }
        }
        
        return root;
    }
    
    public ArgumentData<T> getData() {
        return data;
    }
    
    public StringProperty titleProperty() {
        return title;
    }
    
    public void setTitle(String value) {
        title.set(value);
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public ObjectProperty<ArgumentState> stateProperty() {
        return state;
    }
    
    public void setState(ArgumentState value) {
        if (data.isAlwaysExternal()) {
            return;
        }
    }
    
    public ArgumentState getState() {
        return state.get();
    }
    
    public Block getBlock() {
        return block;
    }
    
    public T getValue() {
        if (input.get() != null) {
            return input.get().getValue();
        }
        return value;
    }
    
    public void setValue(T value) {
        if (!data.checkFit(value)) {
            throw new IllegalArgumentException("Wrong argument type or argument is null");
        }
        this.value = value;
    }
    
    public ObjectProperty<Argument<T>> inputProperty() {
        return input;
    }
    
    public Argument<T> getInput() {
        return input.get();
    }
    
    public void setInput(Argument<T> value) {
        if (value != null && (value.block == block)) {
            Log.info(this, "Attempt to make circular dependence with " + value.toString());
            return;
        }
        input.set(value);
    }
    
    public String getId() {
        return id.toString();
    }
    
    public Node getEditor() {
        if (editor == null) {
            editor = data.createEditor(this);
        }
        
        return editor;
    }
    
    public String toString() {
        return title.get() + ", " + id.toString();
    }
    
}
