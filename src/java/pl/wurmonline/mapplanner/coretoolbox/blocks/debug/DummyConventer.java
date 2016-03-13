/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.wurmonline.mapplanner.coretoolbox.blocks.debug;

import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.coretoolbox.arguments.MapArgumentData;

public class DummyConventer extends BlockData {
    
    public DummyConventer() {
        super("Debug/Heightmap>Map",
                new ArgumentData[] { 
                    new HeightmapArgumentData("In") }, 
                new ArgumentData[] { 
                    new MapArgumentData("Out") });
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        
    }
    
}
