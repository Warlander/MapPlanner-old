package pl.wurmonline.mapplanner.blocks.blocks.utilities;

import pl.wurmonline.mapplanner.blocks.Argument;
import pl.wurmonline.mapplanner.blocks.ArgumentData;
import pl.wurmonline.mapplanner.blocks.BlockData;
import pl.wurmonline.mapplanner.blocks.ProgressProperty;
import pl.wurmonline.mapplanner.blocks.arguments.IntArgumentData;
import pl.wurmonline.mapplanner.blocks.arguments.RandomArgumentData;
import pl.wurmonline.mapplanner.mapgen.XORRandom;

public class SingleRandomValue extends BlockData {

    public SingleRandomValue() {
        super("Utilities/Generate single random value", 
                new ArgumentData[] { 
                    new RandomArgumentData("Random generator") }, 
                new ArgumentData[] {
                    new RandomArgumentData("The same generator"),
                    new IntArgumentData("Single random integer", true)});
    }

    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        XORRandom generator = (XORRandom) inputs[0];
        
        outputs[0].setValue(generator);
        outputs[1].setValue((int) generator.nextLong());
    }
    
}
