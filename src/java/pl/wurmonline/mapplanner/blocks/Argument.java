package pl.wurmonline.mapplanner.blocks;

import java.util.UUID;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Argument<T> implements XMLSerializable {
    
    private final ArgumentData<T> data;
    
    private final UUID id;
    private final StringProperty title;
    private final Block block;
    private final ObjectProperty<ArgumentState> state;
    private T value;
    private final ObjectProperty<Argument<T>> input;
    
    private Node editor;
    
    Argument(Block block, ArgumentData<T> data) {
        this.id = UUID.randomUUID();
        this.title = new SimpleStringProperty(data.getDefaultTitle());
        this.state = new SimpleObjectProperty<>(data.getDefaultState());
        this.block = block;
        this.data = data;
        this.value = null;
        this.input = new SimpleObjectProperty<>();
        
        T value = data.getDefaultValue();
        if (value != null) {
            setValue(value);
        }
        
        createStateListener();
    }
    
    Argument(Block block, ArgumentData<T> data, Element root) {
        this.id = UUID.fromString(root.getAttribute("uuid"));
        this.title = new SimpleStringProperty(root.getAttribute("title"));
        this.state = new SimpleObjectProperty<>(ArgumentState.valueOf(root.getAttribute("state")));
        this.block = block;
        this.data = data;
        this.input = new SimpleObjectProperty<>();
        
        if (root.hasAttribute("value")) {
            this.value = data.deserializeValue(root.getAttribute("value"));
        }
        else {
            T value = data.getDefaultValue();
            if (value != null) {
                this.value = value;
            }
        }
        
        if (root.hasAttribute("input")) {
            input.set(block.getBlueprint().lookupExternalInputs(root.getAttribute("input")));
        }
    }
    
    private void createStateListener() {
        this.state.addListener((observable, oldValue, newValue) -> {
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
    }
    
    public Element serialize(Document doc) {
        Element root = doc.createElement("input");
        root.setAttribute("uuid", id.toString());
        root.setAttribute("title", title.get());
        root.setAttribute("state", state.toString());
        root.setAttribute("data", data.getIdentifier());
        if (input == null && data.getDefaultValue() != null) {
            root.setAttribute("value", data.serializeValue(value));
        }
        else if (input != null) {
            root.setAttribute("input", input.get().getId());
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
        if (value != null && (value.block == block || value == input.get())) {
            throw new IllegalArgumentException("Invalid recurrence: argument cannot point to another argument in the same block.");
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
    
}
