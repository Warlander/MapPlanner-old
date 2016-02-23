package pl.wurmonline.mapplanner;

import pl.wurmonline.mapplanner.gui.MainPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MapPlanner PRE-ALPHA 5");
        
        MainPane root = new MainPane();
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Main.class.getResource("gui/stylesheet.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}
