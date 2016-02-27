package pl.wurmonline.mapplanner.blocks.blocks;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.MapArgumentData;

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
