package pl.wurmonline.mapplanner.blocks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public final class Argument<T> {
    
    private final ArgumentData<T> data;
    
    private final StringProperty title;
    private final Block block;
    private final ObjectProperty<ArgumentState> state;
    private T value;
    private final ObjectProperty<Argument<T>> input;
    
    private Node editor;
    
    Argument(Block block, ArgumentData<T> data) {
        this.title = new SimpleStringProperty(data.getDefaultTitle());
        this.state = new SimpleObjectProperty<>(data.getDefaultState());
        this.block = block;
        this.data = data;
        this.value = null;
        this.input = new SimpleObjectProperty<>();
        
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
    
    public Node getEditor() {
        if (editor == null) {
            editor = data.createEditor(this);
        }
        
        return editor;
    }
    
}
