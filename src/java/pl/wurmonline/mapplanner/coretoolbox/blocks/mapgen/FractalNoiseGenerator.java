package pl.wurmonline.mapplanner.coretoolbox.blocks.mapgen;

import java.util.ArrayList;
import pl.wurmonline.mapplanner.coretoolbox.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;
import pl.wurmonline.mapplanner.mapgen.NoiseLayer;
import pl.wurmonline.mapplanner.mapgen.XORRandom;
import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;

public class FractalNoiseGenerator extends BlockData {
    
    public FractalNoiseGenerator() {
        super("Map Gen/Fractal noise", 
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap"),
                    new RandomArgumentData("Random gen"),
                    new IntArgumentData("Fractal levels", false)},
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap") });
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        Heightmap heightmap = (Heightmap) inputs[0];
        XORRandom rand = (XORRandom) inputs[1];
        int fractalLevels = (int) inputs[2];
        int importance = fractalLevels;
        ArrayList<NoiseLayer> noises = new ArrayList<>();
        for (int i = heightmap.getWidthLevel() - fractalLevels + 1; i <= heightmap.getWidthLevel(); i++) {
            int scale = (1 << (heightmap.getWidthLevel() - i));
            
            NoiseLayer noise = new NoiseLayer(heightmap.getWidth(), heightmap.getHeight(), scale, importance);
            
            for (int x = 0; x < noise.getLayerWidth(); x++) {
                for (int y = 0; y < noise.getLayerHeight(); y++) {
                    noise.setLayerHeight(x, y, rand.nextShort());
                }
            }
            
            noises.add(noise);
        }
        
        for (int x = 0; x < heightmap.getWidth(); x++) {
            for (int y = 0; y < heightmap.getHeight(); y++) {
                short resultHeight = 0;
                for (int i = 0; i < noises.size(); i++) {
                    resultHeight += (short) noises.get(i).getRealHeight(x, y);
                }
                heightmap.setHeight(x, y, resultHeight);
            }
        }
        
        outputs[0].setValue(heightmap);
    }
    
    
    
}