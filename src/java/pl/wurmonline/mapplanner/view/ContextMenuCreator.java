package pl.wurmonline.mapplanner.view;

import javafx.scene.control.ContextMenu;
import javafx.scene.input.ContextMenuEvent;

public interface ContextMenuCreator {
    
    public ContextMenu createContextMenu(ContextMenuEvent evt);
    
}
