package pl.wurmonline.mapplanner.blocks;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

public final class Blueprint {
    
    private final StringProperty title;
    
    private final List<Toolbox> toolboxes;
    private final ObservableList<Block> blocks;
    private final ObservableList<Block> blocksImmutable;
    
    private final DoubleProperty cameraX;
    private final DoubleProperty cameraY;
    
    public Blueprint() {
        this.title = new SimpleStringProperty("Blueprint");
        
        this.toolboxes = new ArrayList<>();
        this.blocks = FXCollections.observableArrayList();
        this.blocksImmutable = FXCollections.unmodifiableObservableList(blocks);
        
        this.cameraX = new SimpleDoubleProperty();
        this.cameraY = new SimpleDoubleProperty();
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
    
    public ObservableList<Block> getChildrenUnmodifiable() {
        return blocksImmutable;
    }
    
    public Block addBlock(Class<? extends BlockData> dataClass) {
        Optional<BlockData> optionalData = toolboxes
                .stream()
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
