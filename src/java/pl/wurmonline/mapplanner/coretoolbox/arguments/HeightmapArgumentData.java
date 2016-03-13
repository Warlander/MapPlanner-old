package pl.wurmonline.mapplanner.coretoolbox.arguments;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;

public class HeightmapArgumentData extends ArgumentData<Heightmap> {
    
    public HeightmapArgumentData(String title) {
        super(title, true, true);
    }
    
    public HeightmapArgumentData(String title, String identifier) {
        super(title, identifier, true, true);
    }
    
    public Color getGUIColor() {
        return Color.ORANGE;
    }

    protected Node createEditor(Argument<Heightmap> arg) {
        return null;
    }

    public Heightmap getDefaultValue() {
        return null;
    }

    protected boolean checkFit(Object value) {
        return value instanceof Heightmap;
    }

    public String serializeValue(Heightmap value) {
        return null;
    }

    public Heightmap deserializeValue(String str) {
        return null;
    }
    
}
