package pl.wurmonline.mapplanner.mapgen;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.mesh.Tiles.Tile;

public class Map {
    
    private final int widthLevel;
    private final int heightLevel;
    private final int width;
    private final int height;
    
    private final Heightmap caveMap;
    private final Heightmap rockMap;
    private final Heightmap surfaceMap;
    
    private final byte[] surfaceType;
    private final byte[] surfaceData;
    private final byte[] caveType;
    private final byte[] caveCeiling;
    
    public Map(int widthLevel, int heightLevel) {
        this.widthLevel = widthLevel;
        this.heightLevel = heightLevel;
        this.width = 1 << widthLevel;
        this.height = 1 << heightLevel;
        
        this.caveMap = new Heightmap(widthLevel, heightLevel);
        this.rockMap = new Heightmap(widthLevel, heightLevel);
        this.surfaceMap = new Heightmap(widthLevel, heightLevel);
        
        final int arraySize = width * height;
        this.surfaceType = new byte[arraySize];
        this.surfaceData = new byte[arraySize];
        this.caveType = new byte[arraySize];
        this.caveCeiling = new byte[arraySize];
    }
    
    public short getDirtHeight(int x, int y) {
        return (short) (getSurfaceHeight(x, y) - getRockHeight(x, y));
    }
    
    public void setSurfaceType(int x, int y, Tile tileType) {
        surfaceType[x << widthLevel + y] = tileType.id;
        surfaceData[x << widthLevel + y] = 0;
    }
    
    public Tile getSurfaceType(int x, int y) {
        return Tiles.getTile(surfaceType[x << widthLevel + y]);
    }
    
    public void setSurfaceHeight(int x, int y, short height) {
        surfaceMap.setHeight(x, y, height);
    }
    
    public short getSurfaceHeight(int x, int y) {
        return surfaceMap.getHeight(x, y);
    }
    
    public void setRockHeight(int x, int y, short height) {
        rockMap.setHeight(x, y, height);
    }
    
    public short getRockHeight(int x, int y) {
        return rockMap.getHeight(x, y);
    }
    
    public void setCaveType(int x, int y, Tile tileType) {
        caveType[x << widthLevel + y] = tileType.id;
    }
    
    public Tile getCaveType(int x, int y) {
        return Tiles.getTile(caveType[x << widthLevel + y]);
    }
    
    public void setCaveCeiling(int x, int y, Tile tileType) {
        caveCeiling[x << widthLevel + y] = tileType.id;
    }
    
    public Tile getCaveCeiling(int x, int y) {
        return Tiles.getTile(caveCeiling[x << widthLevel + y]);
    }
    
    public void setCaveHeight(int x, int y, short height) {
        caveMap.setHeight(x, y, height);
    }
    
    public short getCaveHeight(int x, int y) {
        return caveMap.getHeight(x, y);
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
}
