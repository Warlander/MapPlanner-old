package pl.wurmonline.mapplanner.model;

import java.util.Arrays;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
    
    final Argument[] createInputs(Block block, NodeList serializationData) {
        return parseArguments(block, serializationData, dataInputs);
    }
    
    final Argument[] createOutputs(Block block, NodeList serializationData) {
        return parseArguments(block, serializationData, dataOutputs);
    }
    
    private Argument[] parseArguments(Block block, NodeList serializationData, ArgumentData[] datas) {
        Argument[] arguments = new Argument[datas.length];
        
        outer:
        for (int i = 0; i < datas.length; i++) {
            ArgumentData data = datas[i];
            String identifier = data.getIdentifier();
            for (int i2 = 0; i2 < serializationData.getLength(); i2++) {
                Element node = (Element) serializationData.item(i2);
                String xmlIdentifier = node.getAttribute("data");
                if (xmlIdentifier.equals(identifier)) {
                    arguments[i] = new Argument(block, data, node);
                    continue outer;
                }
            }
            
            if (arguments[i] == null) {
                arguments[i] = new Argument(block, data);
            }
        }
        
        return arguments;
    }
    
    final Argument[] createInputs(Block block) {
        return Arrays.stream(dataInputs).map(data -> new Argument(block, data)).toArray(Argument[]::new);
    }
    
    final Argument[] createOutputs(Block block) {
        return Arrays.stream(dataOutputs).map(data -> new Argument(block, data)).toArray(Argument[]::new);
    }
    
    protected abstract void execute(final Object[] inputs, final Argument[] outputs, ProgressProperty progress);
    
}
