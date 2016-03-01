package pl.wurmonline.mapplanner.blocks.blocks.mapinit;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.blocks.arguments.StringArgumentData;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class CreateRandomFromString extends BlockData {

    public CreateRandomFromString() {
        super("Map Gen/Random from string", 
                new ArgumentData[] { 
                    new StringArgumentData("Seed", true) }, 
                new ArgumentData[] {
                    new RandomArgumentData("Random generator")});
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        String seed = (String) inputs[0];
        
        outputs[0].setValue(new XORRandom(seed.hashCode()));
    }
    
}
