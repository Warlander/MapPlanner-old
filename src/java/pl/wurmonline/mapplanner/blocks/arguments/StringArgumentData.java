package pl.wurmonline.mapplanner.blocks.arguments;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;

public class StringArgumentData extends ArgumentData<String> {

    public StringArgumentData(String title, boolean external) {
        super(title, false, external);
    }
    
    public StringArgumentData(String title, String identifier, boolean external) {
        super(title, identifier, false, external);
    }
    
    public Color getGUIColor() {
        return Color.PURPLE;
    }

    protected Node createEditor(Argument<String> arg) {
        TextField field = new TextField();
        field.setPrefWidth(80);
        field.setScaleX(0.75);
        field.setScaleY(0.75);
        
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            arg.setValue(newValue);
        });
        
        return field;
    }

    public String getDefaultValue() {
        return "";
    }

    protected boolean checkFit(Object value) {
        return value instanceof String;
    }
    
}
