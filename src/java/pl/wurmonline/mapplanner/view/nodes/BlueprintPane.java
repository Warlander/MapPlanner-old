package pl.wurmonline.mapplanner.view.nodes;

import java.util.stream.Stream;
import javafx.collections.FXCollections;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.Block;
import pl.wurmonline.mapplanner.model.Blueprint;
import pl.wurmonline.mapplanner.util.FXUtils;
import pl.wurmonline.mapplanner.view.ContextMenuCreator;

public final class BlueprintPane extends AnchorPane implements ContextMenuCreator {
    
    private final BlueprintContainer blueprintContainer;
    
    private final Group group;
    
    private final Blueprint blueprint;
    private final ObservableMap<Block, BlockNode> blocksMap;
    
    public BlueprintPane(BlueprintContainer blueprintContainer) {
        this.blueprintContainer = blueprintContainer;
        
        blueprint = blueprintContainer.getBlueprint();
        blocksMap = FXCollections.observableHashMap();
        
        BackgroundImage background = new BackgroundImage(new Image(BlueprintPane.class.getResourceAsStream("/pl/wurmonline/mapplanner/gui/background.png")),
            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        setBackground(new Background(background));
        
        group = new Group();
        getChildren().add(group);
        
        EventHandler<ContextMenuEvent> contextMenuHandler = (evt) -> {
            requestContextMenu(evt);
        };
        
        setOnContextMenuRequested(contextMenuHandler);
        
        blueprint.getChildrenReadonly().forEach(this::addBlock);
        
        blueprint.getChildrenReadonly().addListener((Change<? extends Block> change) -> {
            while (change.next()) {
                change.getAddedSubList().forEach(this::addBlock);
                change.getRemoved().forEach(this::removeBlock);
            }
        });
        
        blocksMap.values().forEach(block -> block.deserializeLinks());
    }
    
    private void requestContextMenu(ContextMenuEvent evt) {
        Node node = evt.getPickResult().getIntersectedNode();
        while (node != null) {
            if (node instanceof ContextMenuCreator) {
                break;
            }
            node = node.getParent();
        }
        
        if (node == null) {
            return;
        }

        ContextMenuCreator contextMenuCreator = (ContextMenuCreator) node;
        ContextMenu menu = contextMenuCreator.createContextMenu(evt);

        addEventHandler(MouseEvent.MOUSE_MOVED, (MouseEvent evt1) -> {
            double relativeX = evt1.getScreenX() - menu.getX();
            double relativeY = evt1.getScreenY() - menu.getY();
            if (relativeX < 0 || relativeX > menu.getWidth() || relativeY < 0 || relativeY > menu.getHeight()) {
                menu.hide();
            }
        });

        menu.show(this, evt.getScreenX() - 5, evt.getScreenY() - 5);
    }
    
    public final ContextMenu createContextMenu(ContextMenuEvent evt) {
        ContextMenu menu = new ContextMenu();
        FXUtils.fillCreationMenu(blueprint, menu.getItems(), evt.getX(), evt.getY());
        
        return menu;
    }
    
    public void handleNewBind(ArgumentNode start) {
        start.unbind();
        
        CubicCurve newBindCurve = new CubicCurve();
        newBindCurve.setStroke(Color.BLACK);
        newBindCurve.setStrokeWidth(3);
        newBindCurve.setFill(null);
        newBindCurve.setStartX(start.localToScene(new Point2D(0, 0)).getX() + start.getCircleX() - getLayoutX() - getParent().getLayoutX());
        newBindCurve.setStartY(start.localToScene(new Point2D(0, 0)).getY() + start.getCircleY() - getLayoutY() - getParent().getLayoutY());
        newBindCurve.setEndX(start.localToScene(new Point2D(0, 0)).getX() + start.getCircleX() - getLayoutX() - getParent().getLayoutX());
        newBindCurve.setEndY(start.localToScene(new Point2D(0, 0)).getY() + start.getCircleY() - getLayoutY() - getParent().getLayoutY());
        newBindCurve.setControlX1(newBindCurve.getEndX());
        newBindCurve.setControlY1(newBindCurve.getStartY());

        newBindCurve.setControlX2(newBindCurve.getStartX());
        newBindCurve.setControlY2(newBindCurve.getEndY());
        
        EventHandler<MouseEvent> moveEvent = (evt) -> {
            newBindCurve.setEndX(evt.getX());
            newBindCurve.setEndY(evt.getY());
            
            newBindCurve.setControlX1(newBindCurve.getEndX());
            newBindCurve.setControlY1(newBindCurve.getStartY());
            
            newBindCurve.setControlX2(newBindCurve.getStartX());
            newBindCurve.setControlY2(newBindCurve.getEndY());
        };
        
        EventHandler<MouseEvent> releaseEvent = (evt) -> {
            getChildren().remove(newBindCurve);
            setOnMouseDragged(null);
            setOnMouseReleased(null);
            
            Parent parent;
            if (evt.getPickResult().getIntersectedNode() instanceof Parent) {
                parent = (Parent) evt.getPickResult().getIntersectedNode();
            }
            else {
                parent = evt.getPickResult().getIntersectedNode().getParent();
            }
            
            if (parent != null && !(parent instanceof ArgumentNode)) {
                while (parent.getParent() != null) {
                    parent = parent.getParent();
                    if (parent instanceof ArgumentNode) {
                        break;
                    }
                }
            }

            if (parent instanceof ArgumentNode) {
                ArgumentNode arg = (ArgumentNode) parent;
                ArgumentNode guiInput = (start.getType() == ArgumentNode.Type.INPUT) ? start : arg;
                ArgumentNode guiOutput = (start.getType() == ArgumentNode.Type.OUTPUT) ? start : arg;
                guiInput.getArgument().setInput(guiOutput.getArgument());
            }
        };
        
        setOnMouseDragged(moveEvent);
        setOnMouseReleased(releaseEvent);
        
        getChildren().add(newBindCurve);
        newBindCurve.toBack();
    }
    
    private void addBlock(Block block) {
        BlockNode newGuiBlock = new BlockNode(this, block);
        
        group.getChildren().add(newGuiBlock);
        blocksMap.put(block, newGuiBlock);
    }
    
    private void removeBlock(Block block) {
        BlockNode guiBlock = blocksMap.get(block);
        
        group.getChildren().remove(guiBlock);
        blocksMap.remove(block);
    }
    
    public Blueprint getBlueprint() {
        return blueprint;
    }
    
    public BlockNode getBlock(Block block) {
        return blocksMap.get(block);
    }
    
    public BlueprintContainer getBlueprintContainer() {
        return blueprintContainer;
    }
    
    public ArgumentNode lookupArgumentNode(Argument arg) {
        return blocksMap.values().stream()
                .flatMap(block -> Stream.concat(block.getInputsReadonly().stream(), block.getOutputsReadonly().stream()))
                .filter(node -> node.getArgument() == arg)
                .findAny()
                .orElse(null);
    }
    
}
