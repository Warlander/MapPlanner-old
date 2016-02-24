package pl.wurmonline.mapplanner.blocks;

import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import static pl.wurmonline.mapplanner.GUIConstants.GRID_SIZE;

public class Blocks {
    
    public static void fillCreationMenu(Blueprint blueprint, ObservableList<MenuItem> items, double posX, double posY) {
        for (BlockData data : blueprint.getAllRegisteredData()) {
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
    
}
