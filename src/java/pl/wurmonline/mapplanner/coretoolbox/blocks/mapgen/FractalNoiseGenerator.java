package pl.wurmonline.mapplanner.coretoolbox.blocks.mapgen;

import java.util.ArrayList;
import pl.wurmonline.mapplanner.coretoolbox.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;
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
                    new IntArgumentData("Fractal level", false)},
                new ArgumentData[] { 
                    new HeightmapArgumentData("Heightmap") });
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        Heightmap heightmap = (Heightmap) inputs[0];
        XORRandom rand = (XORRandom) inputs[1];
        int fractalLevel = (int) inputs[2];
        
        int heightRatio = heightmap.getHeightLevel() - heightmap.getWidthLevel();
        
        ArrayList<int[][]> noises = new ArrayList<>();
        for (int i = fractalLevel; i < heightmap.getWidthLevel(); i++) {
            int width = 1 << i;
            int height = i << (i + heightRatio);
            int[][] noise = new int[width][height];
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    noise[x][y] = rand.nextShort();
                }
            }
            
            noises.add(noise);
        }
        
        for (int i = 0; i < heightmap.getWidth(); i++) {
            for (int i2 = 0; i2 < heightmap.getHeight(); i2++) {
                heightmap.setHeight(i, i2, rand.nextShort());
            }
        }
        
        outputs[0].setValue(heightmap);
    }
    
    private short bilinearInterpolation(int x, int y, int scale, short[][] data) {
        int interX = x % scale;
        int interY = y % scale;
        
        int startX = x - interX;
        int startY = y - interY;
        
        
    }
    
}