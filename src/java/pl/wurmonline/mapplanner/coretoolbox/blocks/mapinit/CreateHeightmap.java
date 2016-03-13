package pl.wurmonline.mapplanner.coretoolbox.blocks.mapinit;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;

public class CreateHeightmap extends BlockData {
    
    public CreateHeightmap() {
        super("Map Init/Create empty heightmap", 
                new ArgumentData[] { 
                    new IntArgumentData("Width (2^x)", true, 7, 15),
                    new IntArgumentData("Height (2^x)", true, 7, 15)},
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap") });
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int widthLevel = (int) inputs[0];
        int heightLevel = (int) inputs[1];
        
        outputs[0].setValue(new Heightmap(widthLevel, heightLevel));
    }
    
}
