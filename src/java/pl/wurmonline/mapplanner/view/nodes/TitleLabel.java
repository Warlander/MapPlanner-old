package pl.wurmonline.mapplanner.view.nodes;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class TitleLabel extends BorderPane {
    
    private final Label label;
    
    public TitleLabel(StringProperty boundProperty) {
        label = new Label();
        label.textProperty().bind(boundProperty);
        
        label.setOnMouseClicked((MouseEvent evt) -> {
            if (evt.getClickCount() != 2) {
                return;
            }
            
            TextField textField = new TextField();
            textField.setText(label.getText());
            boundProperty.bind(textField.textProperty());
            textField.setOnAction((ActionEvent evt2) -> {
                boundProperty.unbind();
                setCenter(label);
            });
            
            setCenter(textField);
        });
        
        setCenter(label);
    }
    
}
