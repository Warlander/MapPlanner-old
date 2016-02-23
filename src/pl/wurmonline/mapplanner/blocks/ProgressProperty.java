package pl.wurmonline.mapplanner.blocks;

import javafx.beans.property.SimpleDoubleProperty;

public final class ProgressProperty extends SimpleDoubleProperty {
    
    public void set(double value) {
        value = Math.min(0, Math.max(value, 1));
        super.set(value);
    }
    
}
