package pl.wurmonline.mapplanner;

import pl.wurmonline.mapplanner.view.MainPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.wurmonline.mapplanner.mapgen.Heightmap;
import pl.wurmonline.mapplanner.util.Log;

public class Main extends Application {

    public static void main(String[] args) {
        Log.info(Main.class, "Initializing main application window");
        
        launch(args);
    }
    
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(Constants.VERSION_STRING);
        
        MainPane root = new MainPane();
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Main.class.getResource("gui/stylesheet.css").toExternalForm());
        
        primaryStage.setScene(scene);
        
        Log.info(Main.class, "Main window initialized, launching");
        
        primaryStage.show();
    }
    
}
