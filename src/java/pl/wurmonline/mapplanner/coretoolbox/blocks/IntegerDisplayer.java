package pl.wurmonline.mapplanner.coretoolbox.blocks;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;

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
