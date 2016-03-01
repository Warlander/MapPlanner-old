package pl.wurmonline.mapplanner.gui.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
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
import pl.wurmonline.mapplanner.blocks.blocks.SaveMap;
import pl.wurmonline.mapplanner.gui.ContextMenuCreator;
import pl.wurmonline.mapplanner.gui.FXUtils;
import pl.wurmonline.mapplanner.gui.InputsPane;

public class BlockNode extends BorderPane implements ContextMenuCreator {
    
    private Stage inputsStage;
    private final BlueprintPane root;
    
    private final Block block;
    private final ArrayList<ArgumentNode> inputsList;
    private final ArrayList<ArgumentNode> outputsList;
    
    private final VBox inputsBox;
    
    public BlockNode(BlueprintPane root, Block block) {
        this.root = root;
        this.block = block;
        this.inputsList = new ArrayList<>();
        this.outputsList = new ArrayList<>();
        
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
        block.getExternalInputsReadonly().stream()
                .map(arg -> new ArgumentNode(root, this, arg, ArgumentNode.Type.INPUT))
                .forEach(arg -> {
                    inputsBox.getChildren().add(arg);
                    inputsList.add(arg);
                });
        block.getExternalInputsReadonly().addListener((ListChangeListener.Change<? extends Argument> c) -> {
            while (c.next()) {
                c.getAddedSubList().forEach(this::addInput);
                c.getRemoved().forEach(this::removeInput);
            }
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
                    outputsList.add(arg);
                });
        
        setRight(outputsBox);
    }
    
    final void updateAllLinks() {
        inputsList.forEach((ArgumentNode arg) -> arg.updateLinks());
        outputsList.forEach((ArgumentNode arg) -> arg.updateLinks());
    }

    public ContextMenu createContextMenu(ContextMenuEvent evt) {
        ContextMenu menu = new ContextMenu();
        
        if (!(block.getData() instanceof SaveMap)) {
            menu.getItems().add(new MenuItem("Delete") {{
                this.setOnAction((e) -> {
                    destroy();
                });
            }});
        }
        
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
    
    public BlueprintPane getPanel() {
        return root;
    }
    
    public void destroy() {
        inputsList.stream().forEach((arg) -> {
            arg.destroy();
        });
        outputsList.stream().forEach((arg) -> {
            arg.destroy();
        });
    }
    
    private void addInput(Argument arg) {
        if (arg.getBlock() != block) {
            throw new IllegalArgumentException("Added input must belong to the same block");
        }
        
        ArgumentNode guiArg = new ArgumentNode(root, this, arg, ArgumentNode.Type.INPUT);
        
        inputsBox.getChildren().add(guiArg);
        inputsList.add(guiArg);
    }
    
    private void removeInput(Argument arg) {
        if (arg.getBlock() != block) {
            return;
        }
        
        for (int i = 0; i < inputsList.size(); i++) {
            ArgumentNode guiArg = inputsList.get(i);
            guiArg.unbind();
            
            if (guiArg.getArgument() == arg && guiArg.getType() == ArgumentNode.Type.INPUT) {
                inputsBox.getChildren().remove(guiArg);
                inputsList.remove(i);
                return;
            }
        }
    }
    
    public Block getBlock() {
        return block;
    }
    
}
