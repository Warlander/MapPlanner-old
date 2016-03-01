package pl.wurmonline.mapplanner.blocks.blocks.mapgen;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.blocks.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.blocks.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class NoiseGenerator extends BlockData {
    
    public NoiseGenerator() {
        super("Map Gen/Random noise", 
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap"),
                    new RandomArgumentData("Random") },
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap") });
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        Heightmap heightmap = (Heightmap) inputs[0];
        XORRandom rand = (XORRandom) inputs[1];
        
        for (int i = 0; i < heightmap.getWidth(); i++) {
            for (int i2 = 0; i2 < heightmap.getHeight(); i2++) {
                heightmap.setHeight(i, i2, (short) (rand.nextLong() % Short.MAX_VALUE));
            }
        }
        
        outputs[0].setValue(heightmap);
    }
    
}
