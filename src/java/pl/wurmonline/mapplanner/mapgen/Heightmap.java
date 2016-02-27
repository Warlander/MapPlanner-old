package pl.wurmonline.mapplanner.mapgen;

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
        heightmap[x << widthLevel + y] = height;
    }
    
    public short getHeight(int x, int y) {
        return heightmap[x << widthLevel + y];
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
}
