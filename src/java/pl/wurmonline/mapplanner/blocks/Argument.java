package pl.wurmonline.mapplanner.blocks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public final class Argument<T> {
    
    private final ArgumentData<T> data;
    
    private final StringProperty title;
    private final Block block;
    private ArgumentState state;
    private T value;
    private Argument<T> input;
    
    private Node editor;
    
    Argument(Block block, ArgumentData<T> data) {
        this.title = new SimpleStringProperty(data.getDefaultTitle());
        this.state = data.getDefaultState();
        this.block = block;
        this.data = data;
        this.value = null;
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
    
    public final void setState(ArgumentState state) {
        if (data.isAlwaysExternal()) {
            return;
        }
        
        this.state = state;
    }
    
    public ArgumentState getState() {
        return state;
    }
    
    public Block getBlock() {
        return block;
    }
    
    public T getValue() {
        if (input != null) {
            return input.getValue();
        }
        return value;
    }
    
    public void setValue(T value) {
        if (!data.checkFit(value)) {
            throw new IllegalArgumentException("Wrong argument type or argument is null");
        }
        this.value = value;
    }
    
    public Argument<T> getInput() {
        return input;
    }
    
    public void setInput(Argument<T> input) {
        if (input != null && (input.block == block || this == input)) {
            throw new IllegalArgumentException("Invalid recurrence: argument cannot point to another argument in the same block.");
        }
        this.input = input;
    }
    
    public Node getEditor() {
        if (editor == null) {
            editor = data.createEditor(this);
        }
        
        return editor;
    }
    
}
