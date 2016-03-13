package pl.wurmonline.mapplanner.coretoolbox.blocks.debug;

import com.sun.glass.ui.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.wurmonline.mapplanner.model.Argument;
import pl.wurmonline.mapplanner.model.ArgumentData;
import pl.wurmonline.mapplanner.model.BlockData;
import pl.wurmonline.mapplanner.model.ProgressProperty;
import pl.wurmonline.mapplanner.coretoolbox.arguments.HeightmapArgumentData;
import pl.wurmonline.mapplanner.mapgen.Heightmap;

public class HeightmapDisplayBlock extends BlockData {
    
    public HeightmapDisplayBlock() {
        super("Debug/Heightmap display",
                new ArgumentData[] { 
                    new HeightmapArgumentData("In") }, 
                new ArgumentData[] { 
                    new HeightmapArgumentData("Out") });
    }
    
    protected void execute(Object[] inputs, Argument[] outputs, ProgressProperty progress) {
        Heightmap arg1 = (Heightmap) inputs[0];
        
        Application.invokeLater(() -> {
            Stage stage = new Stage();
        
            AnchorPane pane = new AnchorPane();
            pane.setPrefSize(512, 512);

            ImageView img = new ImageView(SwingFXUtils.toFXImage(arg1.createDump(9), null));
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
