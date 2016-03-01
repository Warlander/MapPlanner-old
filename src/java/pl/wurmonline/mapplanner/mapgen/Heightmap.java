package pl.wurmonline.mapplanner.mapgen;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Heightmap {
   
    private final short[] heightmap;
    private final int widthLevel;
    private final int heightLevel;
    private final int width;
    private final int height;
    
    public Heightmap(int widthLevel, int heightLevel) {
        if (widthLevel < 7 || widthLevel > 15 || heightLevel < 7 || heightLevel > 15) {
            throw new IllegalArgumentException("Invalid heightmap size: must be between 2^7 and 2^15");
        }
        
        this.widthLevel = widthLevel;
        this.heightLevel = heightLevel;
        this.width = 1 << widthLevel;
        this.height = 1 << heightLevel;
        this.heightmap = new short[width * height];
    }
    
    public Heightmap(Heightmap other) {
        this.widthLevel = other.widthLevel;
        this.heightLevel = other.heightLevel;
        this.width = other.width;
        this.height = other.height;
        this.heightmap = Arrays.copyOf(other.heightmap, other.heightmap.length);
    }
    
    public void setHeight(int x, int y, short height) {
        heightmap[(x << heightLevel) + y] = height;
    }
    
    public short getHeight(int x, int y) {
        return heightmap[(x << heightLevel) + y];
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public BufferedImage createDump() {
        return createDump(heightLevel);
    }
    
    public BufferedImage createDump(int desiredHeightPowerOfTwo) {
        if (desiredHeightPowerOfTwo < 7) {
            throw new IllegalArgumentException("Desired power of two is smaller than 7.");
        }
        
        final int scale = Math.min(desiredHeightPowerOfTwo, heightLevel);
        final int scaleDiff = heightLevel - scale;
        
        int downscalePower = 1 << scaleDiff;
        final int lWidth = width;
        final int downWidth = width / downscalePower;
        final int lHeight = height;
        final int downHeight = height / downscalePower;
        
        final BufferedImage bi2 = new BufferedImage(downWidth, downHeight, BufferedImage.TYPE_INT_RGB);
        final float[] data = new float[downWidth * downHeight * 3];
        
        for (int x = 0; x < lWidth; x += downscalePower) {
            for (int y = lHeight - 1; y >= 0; y -= downscalePower) {
                int h = getHeight(x, y);
                h -= Short.MIN_VALUE;
                float part = (float) h / (Short.MAX_VALUE - Short.MIN_VALUE);
                
                final Color color = new Color(part, part, part);
                
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                final int coord = (x / downscalePower * downHeight) * 3;
                data[coord + 0] = r;
                data[coord + 1] = g;
                data[coord + 2] = b;
            }
        }

        bi2.getRaster().setPixels(0, 0, downWidth, downWidth, data);
        return bi2;
    }
    
}
