package pl.wurmonline.mapplanner.coretoolbox.blocks.mapinit;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.StringArgumentData;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class CreateRandomFromString extends BlockData {

    public CreateRandomFromString() {
        super("Map Gen/Random from string", 
                new ArgumentData[] { 
                    new StringArgumentData("Seed", true) }, 
                new ArgumentData[] {
                    new RandomArgumentData("Random gen")});
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        String seed = (String) inputs[0];
        
        outputs[0].setValue(new XORRandom(seed.hashCode()));
    }
    
}
