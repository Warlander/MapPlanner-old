package pl.wurmonline.mapplanner.blocks.blocks.mapinit;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.blocks.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class CreateRandomFromInt extends BlockData {

    public CreateRandomFromInt() {
        super("Map Gen/Random from number", 
                new ArgumentData[] { 
                    new IntArgumentData("Seed", true, 1, Integer.MAX_VALUE) }, 
                new ArgumentData[] {
                    new RandomArgumentData("Random generator")});
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        int seed = (int) inputs[0];
        
        outputs[0].setValue(new XORRandom(seed));
    }
    
}
