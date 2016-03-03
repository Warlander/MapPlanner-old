package pl.wurmonline.mapplanner.blocks.arguments;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.paint.Color;
import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;

public class IntArgumentData extends ArgumentData<Integer> {

    public IntArgumentData(String title, boolean external) {
        this(title, external, -2147483648, 2147483647);
    }
    
    public IntArgumentData(String title, String identifier, boolean external) {
        this(title, identifier, external, -2147483648, 2147483647);
    }
    
    public IntArgumentData(String title, boolean external, int minValue, int maxValue) {
        super(title, false, external, minValue, maxValue);
    }
    
    public IntArgumentData(String title, String identifier, boolean external, int minValue, int maxValue) {
        super(title, identifier, false, external, minValue, maxValue);
    }
    
    public Color getGUIColor() {
        return Color.CYAN;
    }

    protected Node createEditor(Argument<Integer> arg) {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory((SpinnerValueFactory<Integer>) new IntegerSpinnerValueFactory(arg.getData().getMinValue(), arg.getData().getMaxValue(), arg.getValue()));
        spinner.setPrefWidth(80);
        spinner.setScaleX(0.75);
        spinner.setScaleY(0.75);
        
        spinner.valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            arg.setValue(newValue);
        });
        
        return spinner;
    }

    public Integer getDefaultValue() {
        return Math.max(getMinValue(), 0);
    }

    protected boolean checkFit(Object value) {
        return value instanceof Integer;
    }

    public String serializeValue(Integer value) {
        return value.toString();
    }

    public Integer deserializeValue(String str) {
        return Integer.parseInt(str);
    }
    
}
