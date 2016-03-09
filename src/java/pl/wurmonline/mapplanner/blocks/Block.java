package pl.wurmonline.mapplanner.blocks;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.wurmonline.mapplanner.util.Log;
import pl.wurmonline.mapplanner.util.SerializationUtils;

public final class Block implements XMLSerializable {
    
    private static final Semaphore SEMAPHORE = new Semaphore(4);
    
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
    
    public Block(Blueprint blueprint, Element root) {
        this.blueprint = blueprint;
        this.title = new SimpleStringProperty(root.getAttribute("title"));
        this.data = blueprint.getRegisteredData(root.getAttribute("data"));
        this.inputs = data.createInputs(this, root.getElementsByTagName("input"));
        this.outputs = data.createOutputs(this);
        this.externalInputs = FXCollections.observableArrayList();
        this.externalInputsReadonly = FXCollections.unmodifiableObservableList(externalInputs);
        
        Arrays.stream(inputs)
                .filter((arg) -> arg.getState() == ArgumentState.EXTERNAL)
                .forEach(externalInputs::add);
        
        this.progress = new ProgressProperty(this);
        
        this.gridX = new SimpleIntegerProperty(Integer.parseInt(root.getAttribute("gridX")));
        this.gridY = new SimpleIntegerProperty(Integer.parseInt(root.getAttribute("gridY")));
        
        executionLock = new Object();
    }
    
    public void recreateLinks() {
        for (Argument input : inputs) {
            input.recreateLinks();
        }
    }
    
    public Element serialize(Document doc) {
        Element root = doc.createElement("block");
        root.setAttribute("title", title.get());
        root.setAttribute("data", SerializationUtils.getIdentifier(data.getClass()));
        root.setAttribute("gridX", Integer.toString(gridX.get()));
        root.setAttribute("gridY", Integer.toString(gridY.get()));
        
        for (Argument argument : inputs) {
            Element node = argument.serialize(doc);
            root.appendChild(node);
        }
        
        return root;
    }
    
    void execute() {
        Log.info(this, "Waiting for external inputs");
        
        externalInputs.stream()
                .filter((externalInput) -> externalInput.getInput() != null)
                .forEach((externalInput) -> externalInput.getInput().getBlock().waitForExecution());
        
        Log.info(this, "Inputs ready, preparing for execution");
        
        Object[] in = Arrays.stream(inputs).map((Argument arg) -> arg.getValue()).toArray();
        
        try {
            SEMAPHORE.acquire();
            Log.info(this, "Semaphore aquired");
            progress.set(0);
            Log.info(this, "Executing");
            data.execute(in, outputs, progress);
            Log.info(this, "Finished execution");
            progress.set(1);
            SEMAPHORE.release();
            Log.info(this, "Semaphore released");
        } catch (InterruptedException ex) {
            Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        progress.set(0);
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
    
    public String toString() {
        return title.get();
    }
    
}
