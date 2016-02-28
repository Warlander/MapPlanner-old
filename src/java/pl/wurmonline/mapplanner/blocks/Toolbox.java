package pl.wurmonline.mapplanner.blocks;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Toolbox implements XMLSerializable {
    
    private final String title;
    private final int version;
    private final Map<String, BlockData> blocksData;
    
    public Toolbox(String title, int version) {
        this.title = title;
        this.version = version;
        blocksData = new HashMap<>();
    }
    
    public Element serialize(Document doc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean isUsedIn(Blueprint blueprint) {
        return blueprint.getChildrenReadonly().stream()
                .map((Block block) -> block.getData())
                .anyMatch((BlockData data) -> blocksData.values().contains(data));
    }
    
    public void registerBlockData(Class<? extends BlockData> clazz) {
        try {
            BlockData data = clazz.newInstance();
            blocksData.put(getIdentifier(clazz), data);
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
        return getBlockData(getIdentifier(clazz));
    }
    
    BlockData getBlockData(String identifier) {
        return blocksData.get(identifier);
    }
    
    private String getIdentifier(Class<? extends BlockData> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof Identifier) {
                return ((Identifier) annotation).value();
            }
        }
        return clazz.getSimpleName();
    }
    
    public String title() {
        return title;
    }
    
    public int getVersion() {
        return version;
    }
    
}
