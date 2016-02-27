package pl.wurmonline.mapplanner.blocks;

import pl.wurmonline.mapplanner.blocks.Block;
import java.util.Stack;

public final class BlocksStack extends Stack<Block> {
    
    public Block push(Block block) {
        remove(block);
        super.push(block);
        return block;
    }
    
    public void execute() {
        while (!empty()) {
            pop().execute();
        }
    }
    
}
