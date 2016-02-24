package pl.wurmonline.mapplanner.gui.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.wurmonline.mapplanner.blocks.ArgumentState;
import pl.wurmonline.mapplanner.blocks.Block;
import static pl.wurmonline.mapplanner.GUIConstants.*;
import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.gui.ContextMenuCreator;
import pl.wurmonline.mapplanner.gui.FXUtils;
import pl.wurmonline.mapplanner.gui.InputsPane;

public class BlockNode extends BorderPane implements ContextMenuCreator {
    
    private Stage inputsStage;
    private final BlueprintNode root;
    
    private final Block block;
    private final ArrayList<ArgumentNode> argumentsList;
    
    private final VBox inputsBox;
    
    public BlockNode(BlueprintNode root, Block block) {
        this.root = root;
        this.block = block;
        this.argumentsList = new ArrayList<>();
        
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE), new Insets(-BORDER_SIZE))));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY.deriveColor(1, 1, 1, 0.7), CornerRadii.EMPTY, Insets.EMPTY)));
        
        translateXProperty().bind(block.gridXProperty().multiply(GRID_SIZE));
        translateYProperty().bind(block.gridYProperty().multiply(GRID_SIZE));
        
        TitleLabel title = new TitleLabel(block.titleProperty());
        title.setMaxWidth(Double.MAX_VALUE);
        title.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE), new Insets(-BORDER_SIZE))));
        setTop(title);
        FXUtils.makeDraggable(this, title);
        title.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent evt) -> {
            updateAllLinks();
        });
        
        inputsBox = new VBox(2);
        Arrays.stream(block.getInputs())
                .filter(arg -> arg.getState() == ArgumentState.EXTERNAL)
                .map(arg -> new ArgumentNode(root, this, arg, ArgumentNode.Type.INPUT))
                .forEach(arg -> {
                    inputsBox.getChildren().add(arg);
                    argumentsList.add(arg);
                });
        
        setLeft(inputsBox);
        
        HBox middle = new HBox();
        Region region0 = new Region();
        region0.setPrefWidth(10);
        Region region1 = new Region();
        region1.setPrefWidth(10);
        middle.getChildren().addAll(region0, region1);
        setCenter(middle);
        
        VBox outputsBox = new VBox(2);
        Arrays.stream(block.getOutputs())
                .map(arg -> new ArgumentNode(root, this, arg, ArgumentNode.Type.OUTPUT))
                .forEach(arg -> {
                    outputsBox.getChildren().add(arg);
                    argumentsList.add(arg);
                });
        
        setRight(outputsBox);
    }
    
    final void updateAllLinks() {
        argumentsList.forEach((ArgumentNode arg) -> arg.updateLinks());
    }

    public ContextMenu createContextMenu(ContextMenuEvent evt) {
        ContextMenu menu = new ContextMenu();
        
        menu.getItems().add(new MenuItem("Execute Block") {{
            this.setOnAction((e) -> {
                block.run();
            });
        }});
        
        menu.getItems().add(new MenuItem("Delete") {{
            this.setOnAction((e) -> {
                destroy();
            });
        }});
        
        menu.getItems().add(new MenuItem("Inputs") {{
            this.setOnAction((e) -> {
                if (inputsStage != null) {
                    inputsStage.show();
                    return;
                }
                
                inputsStage = new Stage();
                inputsStage.titleProperty().bind(Bindings.concat("Inputs of ", block.titleProperty()));
                inputsStage.setResizable(false);
                inputsStage.setAlwaysOnTop(true);
                
                InputsPane inputs = new InputsPane(root.getMainPane(), block);
                
                Scene scene = new Scene(inputs, 400, 600);
                inputsStage.setScene(scene);
                inputsStage.show();
            });
        }});
        
        return menu;
    }
    
    public BlueprintNode getPanel() {
        return root;
    }
    
    public void destroy() {
        argumentsList.stream().forEach((arg) -> {
            arg.destroy();
        });
    }
    
    public void addInput(Argument arg) {
        if (arg.getBlock() != block) {
            throw new IllegalArgumentException("Added input must belong to the same block");
        }
        
        ArgumentNode guiArg = new ArgumentNode(root, this, arg, ArgumentNode.Type.INPUT);
        
        inputsBox.getChildren().add(guiArg);
        argumentsList.add(guiArg);
    }
    
    public void removeInput(Argument arg) {
        if (arg.getBlock() != block) {
            return;
        }
        
        for (int i = 0; i < argumentsList.size(); i++) {
            ArgumentNode guiArg = argumentsList.get(i);
            guiArg.unbind();
            
            if (guiArg.getArgument() == arg && guiArg.getType() == ArgumentNode.Type.INPUT) {
                inputsBox.getChildren().remove(guiArg);
                argumentsList.remove(i);
                return;
            }
        }
    }
    
    public Block getBlock() {
        return block;
    }
    
}
