package pl.wurmonline.mapplanner.blocks.blocks;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.IntArgumentData;

public class IntegerDisplayer extends BlockData {
    
    public IntegerDisplayer(ProgressProperty progress) {
        super("Display Integer",
                new ArgumentData[] { 
                    new IntArgumentData("Value", true) }, 
                new ArgumentData[] {});
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int arg1 = (int) inputs[0];
        
        System.out.println(arg1);
    }
    
}
