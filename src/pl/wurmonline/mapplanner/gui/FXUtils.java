package pl.wurmonline.mapplanner.gui;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import static pl.wurmonline.mapplanner.GUIConstants.*;
import pl.wurmonline.mapplanner.gui.nodes.GUIBlock;

public class FXUtils {
    
    public static void makeDraggable(final GUIBlock block, final Node draggable) {
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
