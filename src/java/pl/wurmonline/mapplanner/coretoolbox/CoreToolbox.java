package pl.wurmonline.mapplanner.coretoolbox;

import pl.wurmonline.mapplanner.Constants;
import pl.wurmonline.mapplanner.coretoolbox.blocks.SaveMap;
import pl.wurmonline.mapplanner.coretoolbox.blocks.debug.*;
import pl.wurmonline.mapplanner.coretoolbox.blocks.mapgen.*;
import pl.wurmonline.mapplanner.coretoolbox.blocks.mapinit.*;
import pl.wurmonline.mapplanner.coretoolbox.blocks.utilities.*;
import pl.wurmonline.mapplanner.model.Toolbox;

public class CoreToolbox {
    
    private static final Toolbox CORE_TOOLBOX;
    
    static {
        CORE_TOOLBOX = new Toolbox("MapPlanner Core", Constants.VERSION_NUMBER);
        //mapgen
        register(NoiseGenerator.class);
        
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
    
    private CoreToolbox() {} //static class
    
}
