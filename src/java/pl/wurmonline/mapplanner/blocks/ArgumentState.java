package pl.wurmonline.mapplanner.blocks;

import pl.wurmonline.mapplanner.blocks.ArgumentData;

public enum ArgumentState {
    INTERNAL, EXTERNAL, PARAMETER;
    
    private static final ArgumentState[] EXTERNAL_ONLY = { EXTERNAL };
    private static final ArgumentState[] ALL_ALLOWED = ArgumentState.values();
    
    public ArgumentState[] getAcceptableValuesFor(ArgumentData data) {
        if (data.isAlwaysExternal()) {
            return EXTERNAL_ONLY;
        }
        return ALL_ALLOWED;
    }
    
}
