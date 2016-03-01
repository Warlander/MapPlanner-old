package pl.wurmonline.mapplanner.blocks;

import pl.wurmonline.mapplanner.blocks.blocks.debug.AddBlock;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import pl.wurmonline.mapplanner.Constants;
import static pl.wurmonline.mapplanner.GUIConstants.GRID_SIZE;
import pl.wurmonline.mapplanner.blocks.blocks.*;
import pl.wurmonline.mapplanner.blocks.blocks.debug.DummyConventer;
import pl.wurmonline.mapplanner.blocks.blocks.debug.HeightmapDisplayBlock;
import pl.wurmonline.mapplanner.blocks.blocks.mapinit.*;
import pl.wurmonline.mapplanner.blocks.blocks.utilities.SingleRandomValue;

public class Blocks {
    
    private static final Toolbox CORE_TOOLBOX;
    
    static {
        CORE_TOOLBOX = new Toolbox("MapPlanner Core", Constants.VERSION_NUMBER);
        //mapgen
        
        //mapinit
        register(CreateHeightmap.class);
        register(CreateMap.class);
        register(CreateRandomFromInt.class);
        register(CreateRandomFromString.class);
        
        //utilities
        register(SingleRandomValue.class);
        
        //other
        register(SaveMap.class);
        
        //testing/debug
        registerDebug(AddBlock.class);
        registerDebug(DummyConventer.class);
        registerDebug(HeightmapDisplayBlock.class);
    }
    
    private static void register(Class clazz) {
        CORE_TOOLBOX.registerBlockData(clazz);
    }
    
    private static void registerDebug(Class clazz) {
        if (Constants.DEBUG) {
            register(clazz);
        }
    }
    
    public static Toolbox getCoreToolbox() {
        return CORE_TOOLBOX;
    }
    
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
    
}
