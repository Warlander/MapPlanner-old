package pl.wurmonline.mapplanner.mapgen;

public final class XORRandom {
    
    private final long originalSeed;
    private long seed;
    
    public XORRandom(long seed) {
        if (seed == 0) {
            throw new IllegalArgumentException("Random generator seed cannot be equal 0");
        }
        this.originalSeed = seed;
        this.seed = seed;
    }
    
    public long nextLong() {
        seed ^= (seed << 21);
        seed ^= (seed >>> 35);
        seed ^= (seed << 4);
        return seed;
    }
    
    public long getOriginalSeed() {
        return originalSeed;
    }
    
}
