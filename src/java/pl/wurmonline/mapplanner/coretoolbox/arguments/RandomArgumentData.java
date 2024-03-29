package pl.wurmonline.mapplanner.coretoolbox.arguments;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class RandomArgumentData extends ArgumentData<XORRandom> {

    public RandomArgumentData(String title) {
        super(title, true, true);
    }
    
    public RandomArgumentData(String title, String identifier) {
        super(title, identifier, true, true);
    }

    public Color getGUIColor() {
        return Color.PURPLE;
    }

    protected Node createEditor(Argument<XORRandom> arg) {
        TextField field = new TextField();
        field.setPrefWidth(80);
        field.setScaleX(0.75);
        field.setScaleY(0.75);
        field.setText("seed");
        
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            arg.setValue(new XORRandom(newValue.hashCode()));
        });
        
        return field;
    }

    public XORRandom getDefaultValue() {
        return new XORRandom("seed".hashCode());
    }

    protected boolean checkFit(Object value) {
        return value instanceof XORRandom;
    }

    public String serializeValue(XORRandom value) {
        return Long.toString(value.getOriginalSeed());
    }

    public XORRandom deserializeValue(String str) {
        try {
            return new XORRandom(Long.parseLong(str));
        } catch (NumberFormatException ex) {
            return new XORRandom(str.hashCode());
        }
    }
    
    
    
}
