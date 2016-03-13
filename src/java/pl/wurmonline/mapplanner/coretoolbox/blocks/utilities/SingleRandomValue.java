package pl.wurmonline.mapplanner.coretoolbox.blocks.utilities;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class SingleRandomValue extends BlockData {

    public SingleRandomValue() {
        super("Utilities/Generate single random value", 
                new ArgumentData[] { 
                    new RandomArgumentData("Random gen") }, 
                new ArgumentData[] {
                    new RandomArgumentData("The same gen"),
                    new IntArgumentData("Random int", true)});
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        XORRandom generator = (XORRandom) inputs[0];
        
        outputs[0].setValue(generator);
        outputs[1].setValue((int) generator.nextLong());
    }
    
}
