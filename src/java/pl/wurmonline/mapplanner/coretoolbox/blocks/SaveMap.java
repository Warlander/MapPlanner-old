package pl.wurmonline.mapplanner.coretoolbox.blocks;

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
        
    }
    
}
