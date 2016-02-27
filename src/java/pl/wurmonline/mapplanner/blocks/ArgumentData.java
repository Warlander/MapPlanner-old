package pl.wurmonline.mapplanner.blocks;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public abstract class ArgumentData<T> {
    
    private final StringProperty defaultTitle;
    private final String identifier;
    private final boolean alwaysExternal;
    private ArgumentState defaultState;
    
    private final T minValue;
    private final T maxValue;
    
    public ArgumentData(String title, boolean alwaysExternal, boolean external) {
        this(title, null, alwaysExternal, external, null, null);
    }
    
    public ArgumentData(String title, String identifier, boolean alwaysExternal, boolean external) {
        this(title, identifier, alwaysExternal, external, null, null);
    }
    
    public ArgumentData(String title, boolean alwaysExternal, boolean external, T minValue, T maxValue) {
        this(title, null, alwaysExternal, external, minValue, maxValue);
    }
    
    public ArgumentData(String title, String identifier, boolean alwaysExternal, boolean external, T minValue, T maxValue) {
        this.defaultTitle = new SimpleStringProperty(title);
        this.identifier = identifier == null ? title : identifier;
        this.alwaysExternal = alwaysExternal;
        this.defaultState = alwaysExternal ? ArgumentState.EXTERNAL : external ? ArgumentState.EXTERNAL : ArgumentState.INTERNAL;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    public final ReadOnlyStringProperty defaultTitleProperty() {
        return defaultTitle;
    }
    
    public final String getDefaultTitle() {
        return defaultTitle.get();
    }
    
    public final String getIdentifier() {
        return identifier;
    }
    
    public final boolean isAlwaysExternal() {
        return alwaysExternal;
    }
    
    public final ArgumentState getDefaultState() {
        return defaultState;
    }
    
    /**
     * @return min possible value of the argument. Can be null.
     */
    public final T getMinValue() {
        return minValue;
    }
    
    /**
     * @return max possible value of the argument. Can be null.
     */
    public final T getMaxValue() {
        return maxValue;
    }
    
    public final Argument<T> createArgument(Block block) {
        Argument<T> arg = new Argument<>(block, this);
        T value = getDefaultValue();
        if (value != null) {
            arg.setValue(value);
        }
        return arg;
    }
    
    public abstract Color getGUIColor();
    protected abstract Node createEditor(Argument<T> arg);
    public abstract T getDefaultValue();
    
    protected abstract boolean checkFit(Object value);
    
}
