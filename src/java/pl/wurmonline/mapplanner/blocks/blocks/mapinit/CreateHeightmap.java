package pl.wurmonline.mapplanner.blocks.blocks.mapinit;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.blocks.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;

public class CreateHeightmap extends BlockData {
    
    public CreateHeightmap() {
        super("Map Init/Create empty heightmap", 
                new ArgumentData[] { 
                    new IntArgumentData("Width (power of two)", true, 7, 15),
                    new IntArgumentData("Height (power of two)", true, 7, 15)},
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap") });
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int widthLevel = (int) inputs[0];
        int heightLevel = (int) inputs[1];
        
        outputs[0].setValue(new Heightmap(widthLevel, heightLevel));
    }
    
}
