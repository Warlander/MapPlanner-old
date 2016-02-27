package pl.wurmonline.mapplanner.blocks.blocks;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.IntArgumentData;

public class AddBlock extends BlockData {
    
    public AddBlock() {
        super("Value + Value",
                new ArgumentData[] { 
                    new IntArgumentData("Value 1", true), 
                    new IntArgumentData("Value 1", true) }, 
                new ArgumentData[] { 
                    new IntArgumentData("Value 1 + Value 2", true) });
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int arg1 = (int) inputs[0];
        int arg2 = (int) inputs[1];
        
        outputs[0].setValue(arg1 + arg2);
    }
    
}
