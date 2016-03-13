package pl.wurmonline.mapplanner.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import static pl.wurmonline.mapplanner.GUIConstants.GRID_SIZE;
import pl.wurmonline.mapplanner.coretoolbox.blocks.SaveMap;
import pl.wurmonline.mapplanner.model.Block;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.Blueprint;
import pl.wurmonline.mapplanner.view.nodes.BlockNode;

public class FXUtils {
    
    public static void fillCreationMenu(Blueprint blueprint, ObservableList<MenuItem> items, double posX, double posY) {
        for (BlockData data : blueprint.getAllRegisteredData()) {
            if (data instanceof SaveMap) {
                continue;
            }
            
            ObservableList<MenuItem> currentCategory = items;
            
            for (String category : data.getPath()) {
                Menu menu = null;
                for (MenuItem checkedItem : currentCategory) {
                    if (!(checkedItem instanceof Menu)) {
                        continue;
                    }
                    if (checkedItem.getText().equals(category)) {
                        menu = (Menu) checkedItem;
                        break;
                    }
                }
                
                if (menu == null) {
                    menu = new Menu(category);
                    currentCategory.add(menu);
                }
                
                currentCategory = menu.getItems();
            }
            
            currentCategory.add(new MenuItem(data.getDefaultTitle()) {{
            this.setOnAction((e) -> {
                Block block = blueprint.addBlock(data.getClass());
                int gridX = (int) Math.round(posX / GRID_SIZE);
                int gridY = (int) Math.round(posY / GRID_SIZE);
                block.setGridX(gridX);
                block.setGridY(gridY);
            });
        }});
        }
    }
    
    public static void makeDraggable(final BlockNode block, final Node draggable) {
        final DragContext dragContext = new DragContext();

        draggable.addEventFilter(MouseEvent.MOUSE_PRESSED, (final MouseEvent mouseEvent) -> {
            // remember initial mouse cursor coordinates
            // and node position
            dragContext.mouseAnchorX = mouseEvent.getSceneX();
            dragContext.mouseAnchorY = mouseEvent.getSceneY();
            dragContext.initialTranslateX =
                    block.getTranslateX();
            dragContext.initialTranslateY =
                    block.getTranslateY();
        });

        draggable.addEventFilter(MouseEvent.MOUSE_DRAGGED, (final MouseEvent mouseEvent) -> {
            // shift node from its initial position by delta
            // calculated from mouse cursor movement
            double translateX = dragContext.initialTranslateX + mouseEvent.getSceneX() - dragContext.mouseAnchorX;
            double translateY = dragContext.initialTranslateY + mouseEvent.getSceneY() - dragContext.mouseAnchorY;
            
            block.getBlock().setGridX((int) Math.round(translateX / GRID_SIZE));
            block.getBlock().setGridY((int) Math.round(translateY / GRID_SIZE));
        });
    }
    
    private static final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
        public double initialTranslateX;
        public double initialTranslateY;
    }
    
}
