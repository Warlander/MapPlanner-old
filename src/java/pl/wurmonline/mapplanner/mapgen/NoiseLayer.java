package pl.wurmonline.mapplanner.mapgen;

public class NoiseLayer {
    
    private final int mapWidth;
    private final int mapHeight;
    private final int layerWidth;
    private final int layerHeight;
    private final int scale;
    private final double importance;
    
    private final short[][] data;
    
    public NoiseLayer(int mapWidth, int mapHeight, int scale, int importance) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.layerWidth = (mapWidth / scale) + 1;
        this.layerHeight = (mapHeight / scale) + 1;
        this.scale = scale;
        this.importance = importance;
        
        this.data = new short[layerWidth][layerHeight];
    }
    
    public int getLayerWidth() {
        return layerWidth;
    }
    
    public int getLayerHeight() {
        return layerHeight;
    }
    
    public int getMapWidth() {
        return mapWidth;
    }
    
    public int getMapHeight() {
        return mapHeight;
    }
    
    public int getScale() {
        return scale;
    }
    
    public void setLayerHeight(int x, int y, short height) {
        data[x][y] = height;
    }
    
    public short getLayerHeight(int x, int y) {
        return data[x][y];
    }
    
    public short getRealHeight(int x, int y) {
        return bilinearInterpolation(x, y, scale, importance, data);
    }
    
    private short bilinearInterpolation(int x, int y, int scale, double importance, short[][] data) {
        int interX = x % scale;
        int interY = y % scale;
        
        int startX = x / scale;
        int startY = y / scale;
        
        double interXRatio = (double) interX / scale;
        double interYRatio = (double) interY / scale;
        
        double h00 = data[startX][startY];
        double h10 = data[startX + 1][startY];
        double h01 = data[startX][startY + 1];
        double h11 = data[startX + 1][startY + 1];
        
        double h0 = h00 * (1 - interXRatio) + h10 * interXRatio;
        double h1 = h01 * (1 - interXRatio) + h11 * interXRatio;
        double h = h0 * (1 - interYRatio) + h1 * interYRatio;
        
        return (short) (h / importance);
    } 
    
}
