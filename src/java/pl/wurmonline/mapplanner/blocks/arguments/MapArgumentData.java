package pl.wurmonline.mapplanner.blocks.arguments;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.mapgen.Map;

public class MapArgumentData extends ArgumentData<Map> {
    
    public MapArgumentData(String title) {
        super(title, true, true);
    }
    
    public MapArgumentData(String title, String identifier) {
        super(title, identifier, true, true);
    }
    
    public Color getGUIColor() {
        return Color.RED;
    }

    protected Node createEditor(Argument<Map> arg) {
        return null;
    }

    public Map getDefaultValue() {
        return null;
    }

    protected boolean checkFit(Object value) {
        return value instanceof Map;
    }
    
}
