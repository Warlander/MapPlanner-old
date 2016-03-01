package pl.wurmonline.mapplanner.gui;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentState;
import pl.wurmonline.mapplanner.blocks.Block;
import pl.wurmonline.mapplanner.gui.nodes.TitleLabel;

public class InputsPane extends GridPane {
    
    private static final int LABEL_SPACE_PERCENT = 35;
    private static final int ARGUMENT_TYPE_SPACE_PERCENT = 20;
    private static final int EDITOR_SPACE_PERCENT = 45;
    
    private final MainPane mainPane;
    
    public InputsPane(MainPane mainPane, Block block) {
        this.mainPane = mainPane;
        
        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setPercentWidth(LABEL_SPACE_PERCENT);
        ColumnConstraints argCol = new ColumnConstraints();
        argCol.setPercentWidth(ARGUMENT_TYPE_SPACE_PERCENT);
        ColumnConstraints controlCol = new ColumnConstraints();
        controlCol.setPercentWidth(EDITOR_SPACE_PERCENT);
        getColumnConstraints().addAll(labelCol, argCol, controlCol);
        
        int argsCount = 0;
        for (Argument arg : block.getInputs()) {
            final int currentArg = argsCount;
            
            TitleLabel titleLabel = new TitleLabel(arg.titleProperty());
            Node editor = arg.getEditor();
            
            ChoiceBox<ArgumentState> argumentTypeBox = new ChoiceBox<>();
            argumentTypeBox.setItems(FXCollections.observableArrayList(ArgumentState.values()));
            argumentTypeBox.getSelectionModel().select(arg.getState());
            if (arg.getData().isAlwaysExternal()) {
                argumentTypeBox.setDisable(true);
            }
            arg.stateProperty().bind(argumentTypeBox.getSelectionModel().selectedItemProperty());
            arg.stateProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue == ArgumentState.INTERNAL) {
                    getChildren().remove(editor);
                }
                if (newValue == ArgumentState.INTERNAL) {
                    add(editor, 2, currentArg);
                }
            });
            
            add(titleLabel, 0, currentArg);
            add(argumentTypeBox, 1, currentArg);
            if (arg.getState() == ArgumentState.INTERNAL) {
                add(editor, 2, currentArg);
            }
            
            argsCount++;
        }
    }
    
}
