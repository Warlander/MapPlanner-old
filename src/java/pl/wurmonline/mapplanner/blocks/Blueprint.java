package pl.wurmonline.mapplanner.blocks;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import org.w3c.dom.NodeList;
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
    
    private final BooleanProperty executing;
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
        
        this.executing = new SimpleBooleanProperty(false);
        this.executionLock = new Object();
        
        addBlock(SaveMap.class);
    }
    
    public Blueprint(Document doc) {
        Element root = (Element) doc.getElementsByTagName("blueprint").item(0);
        
        this.title = new SimpleStringProperty(root.getAttribute("title"));
        this.cameraX = new SimpleDoubleProperty(Double.parseDouble(root.getAttribute("cameraX")));
        this.cameraY = new SimpleDoubleProperty(Double.parseDouble(root.getAttribute("cameraY")));
        
        this.toolboxes = new ArrayList<>();
        this.blocks = FXCollections.observableArrayList();
        this.blocksReadonly = FXCollections.unmodifiableObservableList(blocks);
        
        this.properties = FXCollections.observableArrayList();
        this.propertiesReadonly = FXCollections.unmodifiableObservableList(properties);
        
        this.executing = new SimpleBooleanProperty(false);
        this.executionLock = new Object();
        
        addToolbox(Blocks.getCoreToolbox());
        
        Element requires = (Element) root.getElementsByTagName("require").item(0);
        NodeList toolboxList = requires.getElementsByTagName("toolbox");
        for (int i = 0; i < toolboxList.getLength(); i++) {
            Element item = (Element) toolboxList.item(i);
            addToolbox(new Toolbox(item));
        }
        
        Element blocksElement = (Element) root.getElementsByTagName("blocks").item(0);
        NodeList blocksList = blocksElement.getElementsByTagName("block");
        for (int i = 0; i < blocksList.getLength(); i++) {
            Element item = (Element) blocksList.item(i);
            blocks.add(new Block(this, item));
        }
        
        for (Block block : blocks) {
            block.recreateLinks();
        }
    }
    
    public String serialize() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        
        Element main = doc.createElement("blueprint");
        main.setAttribute("title", title.get());
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
    
    public ReadOnlyBooleanProperty executingProperty() {
        return ReadOnlyBooleanProperty.readOnlyBooleanProperty(executing);
    }
    
    private void resetState() {
        synchronized (executionLock) {
            if (!executing.get()) {
                return;
            }
            
            executing.set(false);
            blocks.stream().forEach((block) -> block.resetState());
        }
    }
    
    public boolean isExecuting() {
        return executing.get();
    }
    
    public void cancelExecution() {
        resetState();
    }
    
    public void execute() {
        synchronized(executionLock) {
            if (executing.get()) {
                return;
            }
            
            executing.set(true);
            executeBlueprint();
        }
    }
    
    private void executeBlueprint() {
        Thread executionThread = new Thread() {
            public void run() {
                blocks.stream()
                .filter((Block block) -> block.getData() instanceof SaveMap)
                .findAny()
                .ifPresent((Block block) -> block.execute());
                resetState();
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
    
    public BlockData getRegisteredData(String identifier) {
        return toolboxes.stream()
                .filter((toolbox) -> toolbox.getBlockData(identifier) != null)
                .map((toolbox) -> toolbox.getBlockData(identifier))
                .findAny()
                .orElse(null);
    }
    
    public boolean removeBlock(Block block) {
        return blocks.remove(block);
    }
    
    public Argument lookupExternalInputs(String uuid) {
        return blocks.stream()
                .flatMap((block) -> Stream.of(block.getOutputs()))
                .peek((argument) -> System.out.println(argument.getId()))
                .filter((argument) -> argument.getId().equals(uuid))
                .findAny()
                .orElse(null);
    }
    
}
