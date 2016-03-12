package pl.wurmonline.mapplanner.mapgen;

public final class XORRandom {
    
    private static final double NORM_DOUBLE = 1.0 / ( 1L << 53 );
    private static final double NORM_FLOAT = 1.0 / ( 1L << 24 );
    
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
    
    public int nextInt() {
        return (int) (nextLong() % Integer.MAX_VALUE);
    }
    
    public short nextShort() {
        return (short) (nextLong() % Short.MAX_VALUE);
    }
    
    public byte nextByte() {
        return (byte) (nextLong() % Byte.MAX_VALUE);
    }
    
    public double nextDouble() {
        return (nextLong() >>> 11) * NORM_DOUBLE;
    }
    
    public float nextFloat() {
        return (float) ((nextLong() >>> 40) * NORM_FLOAT);
    }
    
    public long getOriginalSeed() {
        return originalSeed;
    }
    
}
