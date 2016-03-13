package pl.wurmonline.mapplanner.coretoolbox.blocks.mapgen;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class NoiseGenerator extends BlockData {
    
    public NoiseGenerator() {
        super("Map Gen/Random noise", 
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap"),
                    new RandomArgumentData("Random gen") },
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap") });
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        Heightmap heightmap = (Heightmap) inputs[0];
        XORRandom rand = (XORRandom) inputs[1];
        
        for (int i = 0; i < heightmap.getWidth(); i++) {
            for (int i2 = 0; i2 < heightmap.getHeight(); i2++) {
                heightmap.setHeight(i, i2, rand.nextShort());
            }
        }
        
        outputs[0].setValue(heightmap);
    }
    
}
