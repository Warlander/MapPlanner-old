package pl.wurmonline.mapplanner.coretoolbox.blocks;

import java.util.logging.Level;
import java.util.logging.Logger;
import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.MapArgumentData;

public class SaveMap extends BlockData {
    
    public SaveMap() {
        super("Save Map",
                new ArgumentData[] { 
                    new MapArgumentData("Map") }, 
                new ArgumentData[] {});
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        progress.set(0.5);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SaveMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
