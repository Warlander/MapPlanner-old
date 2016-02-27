package pl.wurmonline.mapplanner.gui;

import javafx.scene.control.ContextMenu;
import javafx.scene.input.ContextMenuEvent;

public interface ContextMenuCreator {
    
    public ContextMenu createContextMenu(ContextMenuEvent evt);
    
}
