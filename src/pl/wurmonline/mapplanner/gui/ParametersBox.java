package pl.wurmonline.mapplanner.gui;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.gui.nodes.TitleLabel;

public class ParametersBox extends VBox {
    
    private final Map<Argument, Node> parameterMappings;
    
    private final Label emptyLabel;
    
    ParametersBox() {
        setMaxWidth(200);
        setPrefWidth(200);
        
        parameterMappings = new HashMap<>();
        
        Label titleLabel = new Label("Generation parameters");
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.prefWidthProperty().bind(this.widthProperty());
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setFont(Font.font("Arial", 16));
        getChildren().add(titleLabel);
        
        emptyLabel = new Label("Empty.\nYou can add quick access\nmap generation parameters\nby right-clicking desired block,\nchoosing \"inputs\" option\nand setting inputs as parameters.");
        emptyLabel.prefWidthProperty().bind(this.widthProperty());
        emptyLabel.setTextAlignment(TextAlignment.CENTER);
        emptyLabel.setAlignment(Pos.CENTER);
        emptyLabel.setFont(Font.font("Arial", 10));
        getChildren().add(emptyLabel);
    }
    
    public void addParameter(Argument arg) {
        if (parameterMappings.containsKey(arg)) {
            return;
        }
        
        if (parameterMappings.isEmpty()) {
            getChildren().remove(emptyLabel);
        }
        
        TitleLabel titleLabel = new TitleLabel(arg.titleProperty());
        Node editor = arg.getEditor();
        
        BorderPane argPane = new BorderPane();
        argPane.setLeft(titleLabel);
        argPane.setRight(editor);
        
        getChildren().add(argPane);
        parameterMappings.put(arg, argPane);
    }
    
    public void removeParameter(Argument arg) {
        getChildren().remove(parameterMappings.get(arg));
        parameterMappings.remove(arg);
        
        if (parameterMappings.isEmpty()) {
            getChildren().add(emptyLabel);
        }
    }
    
}
