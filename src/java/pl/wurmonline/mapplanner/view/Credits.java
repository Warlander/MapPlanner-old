package pl.wurmonline.mapplanner.view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import org.apache.commons.io.IOUtils;

public class Credits extends BorderPane {
    
    private static final String CREDITS;
    
    static {
        CREDITS = loadCredits();
    }
    
    private static String loadCredits() {
        try {
            return IOUtils.toString(Credits.class.getResourceAsStream("Credits.txt"));
        } catch (IOException ex) {
            Logger.getLogger(Credits.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Credits() {
        Label creditsLabel = new Label(CREDITS);
        creditsLabel.setWrapText(true);
        creditsLabel.setTextAlignment(TextAlignment.CENTER);
        setCenter(creditsLabel);
    }
    
}
