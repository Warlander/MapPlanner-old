package pl.wurmonline.mapplanner.blocks;

import java.util.Arrays;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Block implements XMLSerializable {
    
    private final Blueprint blueprint;
    
    private final StringProperty title;
    
    private final BlockData data;
    private final Argument[] inputs;
    private final Argument[] outputs;
    private final ObservableList<Argument> externalInputs;
    private final ObservableList<Argument> externalInputsReadonly;
    
    private final ProgressProperty progress;
    
    private final IntegerProperty gridX;
    private final IntegerProperty gridY;
    
    private boolean executed;
    private final Object executionLock;
    
    Block(Blueprint blueprint, BlockData data) {
        this.blueprint = blueprint;
        this.title = new SimpleStringProperty(data.getDefaultTitle());
        this.data = data;
        this.inputs = data.createInputs(this);
        this.outputs = data.createOutputs(this);
        this.externalInputs = FXCollections.observableArrayList();
        this.externalInputsReadonly = FXCollections.unmodifiableObservableList(externalInputs);
        
        Arrays.stream(inputs)
                .filter((arg) -> arg.getState() == ArgumentState.EXTERNAL)
                .forEach(externalInputs::add);
        
        this.progress = new ProgressProperty(this);
        
        this.gridX = new SimpleIntegerProperty(0);
        this.gridY = new SimpleIntegerProperty(0);
        
        executionLock = new Object();
    }
    
    public Element serialize(Document doc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void execute() {
        externalInputs.stream()
                .filter((externalInput) -> externalInput.getInput() != null)
                .forEach((externalInput) -> externalInput.getInput().getBlock().waitForExecution());
        
        Object[] in = Arrays.stream(inputs).map((Argument arg) -> arg.getValue()).toArray();
        
        progress.set(0);
        data.execute(in, outputs, progress);
        progress.set(1);
    }
    
    private void waitForExecution() {
        synchronized (executionLock) {
            if (executed) {
                return;
            }
            
            execute();
            executed = true;
        }
    }
    
    public void resetState() {
        executed = false;
    }
    
    public Blueprint getBlueprint() {
        return blueprint;
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
    
    public IntegerProperty gridXProperty() {
        return gridX;
    }
    
    public void setGridX(int value) {
        gridX.set(value);
    }
    
    public int getGridX() {
        return gridX.get();
    }
    
    public IntegerProperty gridYProperty() {
        return gridY;
    }
    
    public void setGridY(int value) {
        gridY.set(value);
    }
    
    public int getGridY() {
        return gridY.get();
    }
    
    public BlockData getData() {
        return data;
    }
    
    public ObservableList<Argument> getExternalInputsReadonly() {
        return externalInputsReadonly;
    }
    
    protected void addExternalInput(Argument value) {
        externalInputs.add(value);
    }
    
    protected void removeExternalInput(Argument value) {
        externalInputs.remove(value);
    }
    
    public Argument[] getInputs() {
        return inputs;
    }
    
    public Argument[] getOutputs() {
        return outputs;
    }
    
    public double getProgress() {
        return progress.get();
    }
    
    public ReadOnlyDoubleProperty progressProperty() {
        return progress;
    } 
    
}
