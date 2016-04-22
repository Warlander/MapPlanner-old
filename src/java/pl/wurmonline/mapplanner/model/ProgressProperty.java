package pl.wurmonline.mapplanner.model;

import javafx.beans.property.SimpleDoubleProperty;

public final class ProgressProperty extends SimpleDoubleProperty {

    private final Block parentBlock;
    
    public ProgressProperty(Block parentBlock) {
        this.parentBlock = parentBlock;
    }
    
    public void set(double value) {
        if (!shouldContinue() && value != 0) {
            throw new IllegalStateException("Blueprint no longer running");
        }
        value = Math.max(0, Math.min(value, 1));
        super.set(value);
    }
    
    public boolean shouldContinue() {
        return parentBlock.getBlueprint().isExecuting();
    }
    
}
