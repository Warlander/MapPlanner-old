package pl.wurmonline.mapplanner.coretoolbox.blocks.debug;

import com.sun.glass.ui.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.wurmonline.mapplanner.coretoolbox.arguments.MapArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;
import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.mapgen.Map;

public class HeightmapDisplayBlock extends BlockData {
    
    public HeightmapDisplayBlock() {
        super("Debug/Heightmap display",
                new ArgumentData[] { 
                    new MapArgumentData("In") }, 
                new ArgumentData[] { 
                    new MapArgumentData("Out") });
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        Map arg1 = (Map) inputs[0];
        Heightmap heightmap = arg1.getRockHeightmap();
        
        Application.invokeLater(() -> {
            Stage stage = new Stage();
        
            AnchorPane pane = new AnchorPane();
            pane.setPrefSize(512, 512);

            ImageView img = new ImageView(SwingFXUtils.toFXImage(heightmap.createDump(9), null));
            img.setFitWidth(512);
            img.setFitHeight(512);
            
            pane.getChildren().add(img);

            Scene scene = new Scene(pane, 512, 512);
            stage.setScene(scene);
            stage.show();
        });
        
        outputs[0].setValue(arg1);
    }
    
}
