package pl.wurmonline.mapplanner.blocks;

import javafx.beans.property.SimpleDoubleProperty;

public final class ProgressProperty extends SimpleDoubleProperty {

    private final Block parentBlock;
    
    public ProgressProperty(Block parentBlock) {
        this.parentBlock = parentBlock;
    }
    
    public void set(double value) {
        if (!parentBlock.getBlueprint().isExecuting()) {
            Thread.currentThread().interrupt();
        }
        
        value = Math.min(0, Math.max(value, 1));
        super.set(value);
    }
    
}
