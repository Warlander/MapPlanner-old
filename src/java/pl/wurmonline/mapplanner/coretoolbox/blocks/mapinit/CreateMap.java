package pl.wurmonline.mapplanner.coretoolbox.blocks.mapinit;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.MapArgumentData;
import pl.wurmonline.mapplanner.mapgen.Map;

public class CreateMap extends BlockData {

    public CreateMap() {
        super("Map Init/Create empty map", 
                new ArgumentData[] { 
                    new IntArgumentData("Width (2^x)", true, 7, 15),
                    new IntArgumentData("Height (2^x)", true, 7, 15)},
                new ArgumentData[] { 
                    new MapArgumentData("Map") });
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int widthLevel = (int) inputs[0];
        int heightLevel = (int) inputs[1];
        
        outputs[0].setValue(new Map(widthLevel, heightLevel));
    }
    
}
