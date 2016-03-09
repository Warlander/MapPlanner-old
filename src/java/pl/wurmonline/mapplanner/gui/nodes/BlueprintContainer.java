package pl.wurmonline.mapplanner.gui.nodes;

import javafx.scene.layout.BorderPane;
import pl.wurmonline.mapplanner.blocks.Blueprint;
import pl.wurmonline.mapplanner.gui.MainPane;

public class BlueprintContainer extends BorderPane {
    
        private final MainPane mainPane;
        private Blueprint blueprint;
    
        private BlueprintPane diagramPane;
        private ParametersBox parametersBox;
        
        public BlueprintContainer(MainPane mainPane) {
            this(mainPane, new Blueprint());
        }
        
        public BlueprintContainer(MainPane mainPane, Blueprint blueprint) {
            this.mainPane = mainPane;
            this.blueprint = blueprint;
            
            diagramPane = new BlueprintPane(this);
            setCenter(diagramPane);

            parametersBox = new ParametersBox(diagramPane.getBlueprint());
            setLeft(parametersBox);
        }
        
        public MainPane getMainPane() {
            return mainPane;
        }
        
        public void setBlueprint(Blueprint blueprint) {
            if (this.blueprint == blueprint) {
                return;
            }
            this.blueprint = blueprint;
            
            diagramPane = new BlueprintPane(this);
            setCenter(diagramPane);

            parametersBox = new ParametersBox(diagramPane.getBlueprint());
            setLeft(parametersBox);
        }
        
        public Blueprint getBlueprint() {
            return blueprint;
        }
    
}
