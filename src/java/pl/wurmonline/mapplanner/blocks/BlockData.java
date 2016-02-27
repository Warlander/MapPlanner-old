package pl.wurmonline.mapplanner.blocks;

import java.util.Arrays;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class BlockData {
    
    private final StringProperty defaultTitle;
    private final ArgumentData[] dataInputs;
    private final ArgumentData[] dataOutputs;
    
    private final String[] path;
    
    protected BlockData() {
        this("", null, null);
    }
    
    protected BlockData(String title, ArgumentData[] dataInputs, ArgumentData[] dataOutputs) {
        if (title == null || title.length() == 0) {
            throw new IllegalArgumentException("Block data title cannot be null or empty");
        }
        String[] titlePath = title.split("/");
        path = Arrays.copyOf(titlePath, titlePath.length - 1);
        this.defaultTitle = new SimpleStringProperty(titlePath[titlePath.length - 1]);
        this.dataInputs = dataInputs != null ? dataInputs : new ArgumentData[0];
        this.dataOutputs = dataOutputs != null ? dataOutputs : new ArgumentData[0];
    }
    
    public ReadOnlyStringProperty defaultTitleProperty() {
        return defaultTitle;
    }
    
    public final String getDefaultTitle() {
        return defaultTitle.get();
    }
    
    public String[] getPath() {
        return path;
    }
    
    final Argument[] createInputs(Block block) {
        return Arrays.stream(dataInputs).map(data -> data.createArgument(block)).toArray(Argument[]::new);
    }
    
    final Argument[] createOutputs(Block block) {
        return Arrays.stream(dataOutputs).map(data -> data.createArgument(block)).toArray(Argument[]::new);
    }
    
    protected abstract void execute(final Object[] inputs, final Argument[] outputs, ProgressProperty progress);
    
}
