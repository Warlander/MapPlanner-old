package pl.wurmonline.mapplanner.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.wurmonline.mapplanner.coretoolbox.CoreToolbox;
import pl.wurmonline.mapplanner.util.SerializationUtils;

public final class Toolbox implements XMLSerializable {
    
    private final String title;
    private final int version;
    private final Map<String, BlockData> blocksData;
    
    public static Toolbox getToolbox(Element root) {
        String title = root.getAttribute("title");
        int version = Integer.parseInt(root.getAttribute("version"));
        return getToolbox(title, version);
    }
    
    public static Toolbox getToolbox(String title, int version) {
        if (title.equals("MapPlanner Core")) {
            return CoreToolbox.getCoreToolbox();
        }
        else {
            return new Toolbox(title, version);
        }
    }
    
    public Toolbox(String title, int version) {
        this.title = title;
        this.version = version;
        blocksData = new HashMap<>();
        lookupData();
    }
    
    public Toolbox(Element root) {
        this.title = root.getAttribute("title");
        this.version = Integer.parseInt(root.getAttribute("version"));
        blocksData = new HashMap<>();
        lookupData();
    }
    
    private void lookupData() {
        
    }
    
    public Element serialize(Document doc) {
        Element root = doc.createElement("toolbox");
        root.setAttribute("title", title);
        root.setAttribute("version", Integer.toString(version));
        
        return root;
    }
    
    public boolean isUsedIn(Blueprint blueprint) {
        return blueprint.getChildrenReadonly().stream()
                .map((Block block) -> block.getData())
                .anyMatch((BlockData data) -> blocksData.values().contains(data));
    }
    
    public void registerBlockData(Class<? extends BlockData> clazz) {
        try {
            BlockData data = clazz.newInstance();
            blocksData.put(SerializationUtils.getIdentifier(clazz), data);
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException("Block data must contain empty constructor");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Toolbox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    Collection<BlockData> getAllRegisteredData() {
        return blocksData.values();
    }
    
    BlockData getBlockData(Class<? extends BlockData> clazz) {
        return getBlockData(SerializationUtils.getIdentifier(clazz));
    }
    
    BlockData getBlockData(String identifier) {
        return blocksData.get(identifier);
    }
    
    public String title() {
        return title;
    }
    
    public int getVersion() {
        return version;
    }
    
}
