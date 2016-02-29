package pl.wurmonline.mapplanner.blocks;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.wurmonline.mapplanner.blocks.blocks.SaveMap;

public final class Blueprint {
    
    private final StringProperty title;
    
    private final List<Toolbox> toolboxes;
    private final ObservableList<Block> blocks;
    private final ObservableList<Block> blocksReadonly;
    
    private final ObservableList<Argument> properties;
    private final ObservableList<Argument> propertiesReadonly;
    
    private final DoubleProperty cameraX;
    private final DoubleProperty cameraY;
    
    private boolean executing;
    private final Object executionLock;
    
    public Blueprint() {
        this.title = new SimpleStringProperty("Blueprint");
        
        this.toolboxes = new ArrayList<>();
        this.blocks = FXCollections.observableArrayList();
        this.blocksReadonly = FXCollections.unmodifiableObservableList(blocks);
        
        this.properties = FXCollections.observableArrayList();
        this.propertiesReadonly = FXCollections.unmodifiableObservableList(properties);
        
        this.cameraX = new SimpleDoubleProperty();
        this.cameraY = new SimpleDoubleProperty();
        
        addToolbox(Blocks.getCoreToolbox());
        
        this.executing = false;
        this.executionLock = new Object();
    }
    
    public String serialize() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        
        Element main = doc.createElement("blueprint");
        main.setAttribute("cameraX", Double.toString(cameraX.get()));
        main.setAttribute("cameraY", Double.toString(cameraY.get()));
        doc.appendChild(main);
        
        Element requireElement = doc.createElement("require");
        main.appendChild(requireElement);
        
        for (Toolbox toolbox : toolboxes) {
            if (!toolbox.isUsedIn(this)) {
                continue;
            }
            
            Element toolboxElement = toolbox.serialize(doc);
            requireElement.appendChild(toolboxElement);
        }
        
        Element blocksElement = doc.createElement("blocks");
        main.appendChild(blocksElement);
        
        for (Block block : blocks) {
            Element blockElement = block.serialize(doc);
            blocksElement.appendChild(blockElement);
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        
        return writer.getBuffer().toString();
    }
    
    public boolean isExecuting() {
        return executing;
    }
    
    public void cancelExecution() {
        executing = false;
    }
    
    public void execute() {
        synchronized(executionLock) {
            if (executing) {
                return;
            }
            
            executing = true;
        }
        
        Thread executionThread = new Thread() {
            public void run() {
                blocks.stream()
                .filter((Block block) -> block.getData() instanceof SaveMap)
                .findAny()
                .ifPresent((Block block) -> block.execute());
                executing = false;
            }
        };
        
        executionThread.start();
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
    
    public DoubleProperty cameraXProperty() {
        return cameraX;
    }
    
    public void setCameraX(double value) {
        cameraX.set(value);
    }
    
    public double getCameraX() {
        return cameraX.get();
    }
    
    public DoubleProperty cameraYProperty() {
        return cameraY;
    }
    
    public void setCameraY(double value) {
        cameraY.set(value);
    }
    
    public double getCameraY() {
        return cameraY.get();
    }
    
    public void addToolbox(Toolbox toolbox) {
        toolboxes.add(toolbox);
    }
    
    public ObservableList<Argument> getPropertiesReadonly() {
        return propertiesReadonly;
    }
    
    protected void addProperty(Argument value) {
        properties.add(value);
    }
    
    protected void removeProperty(Argument value) {
        properties.remove(value);
    }
    
    public ObservableList<Block> getChildrenReadonly() {
        return blocksReadonly;
    }
    
    public Block addBlock(Class<? extends BlockData> dataClass) {
        if (dataClass == SaveMap.class) {
            Optional<Block> saveBlock = blocks.stream()
                .filter((Block block) -> block.getData() instanceof SaveMap)
                .findAny();
            
            if (saveBlock.isPresent()) {
                throw new IllegalArgumentException("You cannot have more than one map save block on single blueprint.");
            }
        }
        
        Optional<BlockData> optionalData = toolboxes.stream()
                .map((Toolbox toolbox) -> toolbox.getBlockData(dataClass))
                .filter((BlockData data) -> data != null)
                .findFirst();
        
        if (!optionalData.isPresent()) {
            throw new IllegalArgumentException("Block not registered - please register proper BlockData classes in toolbox first.");
        }
        Block block = new Block(this, optionalData.get());
        blocks.add(block);
        return block;
    }
    
    public BlockData[] getAllRegisteredData() {
        return toolboxes.stream()
                .flatMap((Toolbox toolbox) -> toolbox.getAllRegisteredData().stream())
                .toArray(BlockData[]::new);
    }
    
    public boolean removeBlock(Block block) {
        return blocks.remove(block);
    }
    
}
