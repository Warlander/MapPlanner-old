package pl.wurmonline.mapplanner.gui.nodes;

import javafx.collections.FXCollections;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableMap;
import pl.wurmonline.mapplanner.gui.nodes.BlockNode;
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
import pl.wurmonline.mapplanner.blocks.Block;
import pl.wurmonline.mapplanner.blocks.Blueprint;
import pl.wurmonline.mapplanner.blocks.Toolbox;
import pl.wurmonline.mapplanner.blocks.Blocks;
import pl.wurmonline.mapplanner.blocks.blocks.mapinit.CreateMap;
import pl.wurmonline.mapplanner.gui.ContextMenuCreator;
import pl.wurmonline.mapplanner.gui.MainPane;
import pl.wurmonline.mapplanner.gui.nodes.ArgumentNode;

public final class BlueprintNode extends AnchorPane implements ContextMenuCreator {
    
    private final MainPane mainPane;
    
    private final Group group;
    
    private final Blueprint blueprint;
    private final ObservableMap<Block, BlockNode> blocksMap;
    
    public BlueprintNode(MainPane mainPane) {
        this.mainPane = mainPane;
        
        blueprint = new Blueprint();
        Toolbox toolbox = new Toolbox("MapPlanner Core", 1);
        toolbox.registerBlockData(CreateMap.class);
        blueprint.addToolbox(toolbox);
        blocksMap = FXCollections.observableHashMap();
        
        BackgroundImage background = new BackgroundImage(new Image(BlueprintNode.class.getResourceAsStream("background.png")),
            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        setBackground(new Background(background));
        
        group = new Group();
        getChildren().add(group);
        
        EventHandler<ContextMenuEvent> contextMenuHandler = (evt) -> {
            requestContextMenu(evt);
        };
        
        setOnContextMenuRequested(contextMenuHandler);
        
        blueprint.getChildrenUnmodifiable().addListener((Change<? extends Block> change) -> {
            while (change.next()) {
                change.getAddedSubList().stream().forEach((block) -> {
                    addBlock(block);
                });
                change.getRemoved().stream().forEach((block) -> {
                    removeBlock(block);
                });
            }
        });
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
        Blocks.fillCreationMenu(blueprint, menu.getItems(), evt.getX(), evt.getY());
        
        return menu;
    }
    
    public void handleNewBind(ArgumentNode start) {
        start.unbind();
        
        CubicCurve newBindCurve = new CubicCurve();
        newBindCurve.setStroke(Color.BLACK);
        newBindCurve.setStrokeWidth(3);
        newBindCurve.setFill(null);
        newBindCurve.setStartX(start.localToScene(new Point2D(0, 0)).getX() + start.getCircleX() - getLayoutX());
        newBindCurve.setStartY(start.localToScene(new Point2D(0, 0)).getY() + start.getCircleY() - getLayoutY());
        newBindCurve.setEndX(start.localToScene(new Point2D(0, 0)).getX() + start.getCircleX() - getLayoutX());
        newBindCurve.setEndY(start.localToScene(new Point2D(0, 0)).getY() + start.getCircleY() - getLayoutY());
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
                arg.bind(start);
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
    
    public MainPane getMainPane() {
        return mainPane;
    }
    
}
