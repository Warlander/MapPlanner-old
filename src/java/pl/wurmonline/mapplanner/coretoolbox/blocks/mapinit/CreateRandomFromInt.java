package pl.wurmonline.mapplanner.coretoolbox.blocks.mapinit;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class CreateRandomFromInt extends BlockData {

    public CreateRandomFromInt() {
        super("Map Gen/Random from number", 
                new ArgumentData[] { 
                    new IntArgumentData("Seed", true, 1, Integer.MAX_VALUE) }, 
                new ArgumentData[] {
                    new RandomArgumentData("Random gen")});
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int seed = (int) inputs[0];
        
        outputs[0].setValue(new XORRandom(seed));
    }
    
}
