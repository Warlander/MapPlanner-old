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
            int alt = downHeight - 1;
            for (int y = lHeight - 1; y >= 0; y -= downscalePower) {
                float node = (float) (getHeight(x, y) / (Short.MAX_VALUE / 3.3f));
                float node2 = x == lWidth - 1 || y == lHeight - 1 ? node : (float) (getHeight(x + downscalePower, y + downscalePower) / (Short.MAX_VALUE / 3.3f));

                final float hh = node;

                float h = ((node2 - node) * 1500) / 256.0f * downHeight / 128 + hh / 2 + 1.0f;
                h *= 0.4f;

                float r = h;
                float g = h;
                float b = h;

                final Color color = Color.gray;
                r *= (color.getRed() / 255.0f) * 2;
                g *= (color.getGreen() / 255.0f) * 2;
                b *= (color.getBlue() / 255.0f) * 2;

                if (r < 0)
                    r = 0;
                if (r > 1)
                    r = 1;
                if (g < 0)
                    g = 0;
                if (g > 1)
                    g = 1;
                if (b < 0)
                    b = 0;
                if (b > 1)
                    b = 1;

                if (node < 0) {
                    r = r * 0.2f + 0.4f * 0.4f;
                    g = g * 0.2f + 0.5f * 0.4f;
                    b = b * 0.2f + 1.0f * 0.4f;
                }

                final int altTarget = y / downscalePower - (int) (getHeight(x, y) * 1000 / 4  / (Short.MAX_VALUE / 3.3f)) / downscalePower;
                while (alt > altTarget && alt >= 0) {
                    final int coord = (x / downscalePower + alt * downHeight) * 3;
                    data[coord + 0] = r * 255;
                    data[coord + 1] = g * 255;
                    data[coord + 2] = b * 255;
                    alt--;
                }
            }
        }

        bi2.getRaster().setPixels(0, 0, downWidth, downWidth, data);
        return bi2;
    }
    
}
