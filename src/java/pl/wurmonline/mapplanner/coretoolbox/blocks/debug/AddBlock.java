package pl.wurmonline.mapplanner.coretoolbox.blocks.debug;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;

public class AddBlock extends BlockData {
    
    public AddBlock() {
        super("Debug/Value + Value",
                new ArgumentData[] { 
                    new IntArgumentData("Value 1", true), 
                    new IntArgumentData("Value 2", true) }, 
                new ArgumentData[] { 
                    new IntArgumentData("Value 1 + Value 2", true) });
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int arg1 = (int) inputs[0];
        int arg2 = (int) inputs[1];
        
        outputs[0].setValue(arg1 + arg2);
    }
    
}
