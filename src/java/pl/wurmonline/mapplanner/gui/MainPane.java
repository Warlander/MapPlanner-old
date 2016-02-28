package pl.wurmonline.mapplanner.gui;

import pl.wurmonline.mapplanner.gui.nodes.BlueprintPane;
import pl.wurmonline.mapplanner.gui.nodes.ParametersBox;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.wurmonline.mapplanner.blocks.Blocks;

public class MainPane extends BorderPane {
    
    private final BlueprintPane diagramPane;
    private final ParametersBox parametersBox;
    
    public MainPane() {
        diagramPane = new BlueprintPane(this);
        setCenter(diagramPane);
        
        parametersBox = new ParametersBox(diagramPane.getBlueprint());
        setLeft(parametersBox);
        
        VBox top = createTopBar();
        setTop(top);
    }
    
    private VBox createTopBar() {
        VBox box = new VBox();
        
        MenuBar menu = createMenuBar();
        HBox tools = createToolsBar();
        
        box.getChildren().addAll(menu, tools);
        
        return box;
    }
    
    private MenuBar createMenuBar() {
        MenuBar menu = new MenuBar();
        
        Menu file = createFileMenu();
        Menu edit = createEditMenu();
        Menu blocks = createBlocksMenu();
        Menu examples = createExamplesMenu();
        Menu run = createRunMenu();
        Menu tools = createToolsMenu();
        Menu help = createHelpMenu();
        
        menu.getMenus().addAll(file, edit, blocks, examples, run, tools, help);
        
        return menu;
    }
    
    private Menu createFileMenu() {
        Menu file = new Menu("File");
        
        MenuItem newProject = new MenuItem("New Project");
        newProject.setOnAction((ActionEvent evt) -> handleNewProject());
        
        SeparatorMenuItem separator0 = new SeparatorMenuItem();
        
        MenuItem open = new MenuItem("Open Project");
        open.setOnAction((ActionEvent evt) -> handleOpen());
        
        MenuItem save = new MenuItem("Save Project");
        save.setOnAction((ActionEvent evt) -> handleSave());
        
        SeparatorMenuItem separator1 = new SeparatorMenuItem();
        
        MenuItem importProject = new MenuItem("Import Project");
        importProject.setOnAction((ActionEvent evt) -> handleImport());
        
        MenuItem exportProject = new MenuItem("Export Project");
        exportProject.setOnAction((ActionEvent evt) -> handleExport());
        
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction((ActionEvent evt) -> handleClose());
        
        file.getItems().addAll(newProject, separator0, open, save, separator1, importProject, exportProject, separator2, exit);
        
        return file;
    }
    
    private Menu createEditMenu() {
        Menu edit = new Menu("Edit");
        
        MenuItem undo = new MenuItem("Undo");
        undo.setOnAction((ActionEvent evt) -> handleUndo());
        
        MenuItem redo = new MenuItem("Redo");
        redo.setOnAction((ActionEvent evt) -> handleRedo());
        
        edit.getItems().addAll(undo, redo);
        
        return edit;
    }
    
    private Menu createBlocksMenu() {
        Menu blocks = new Menu("Blocks");
        Blocks.fillCreationMenu(diagramPane.getBlueprint(), blocks.getItems(), 0, 0);
        
        return blocks;
    }
    
    public Menu createExamplesMenu() {
        Menu examples = new Menu("Examples");
        
        return examples;
    }
    
    private Menu createRunMenu() {
        Menu run = new Menu("Run");
        
        MenuItem executeBlock = new MenuItem("Execute Block");
        executeBlock.setOnAction((ActionEvent evt) -> handleExecuteBlock());
        
        SeparatorMenuItem separator = new SeparatorMenuItem();
        
        MenuItem execute = new MenuItem("Execute");
        execute.setOnAction((ActionEvent evt) -> handleExecute());
        
        MenuItem stop = new MenuItem("Stop");
        stop.setOnAction((ActionEvent evt) -> handleStop());
        
        run.getItems().addAll(executeBlock, separator, execute, stop);
        
        return run;
    }
    
    private Menu createToolsMenu() {
        Menu tools = new Menu("Tools");
        
        MenuItem options = new MenuItem("Options");
        options.setOnAction((ActionEvent evt) -> handleOpenOptions());
        
        tools.getItems().addAll(options);
        
        return tools;
    }
    
    private Menu createHelpMenu() {
        Menu help = new Menu("Help");
        
        MenuItem credits = new MenuItem("Credits");
        credits.setOnAction((ActionEvent evt) -> handleOpenCredits());
        
        help.getItems().addAll(credits);
        
        return help;
    }
    
    private HBox createToolsBar() {
        HBox tools = new HBox();
        tools.setPrefHeight(32);
        
        Node newProject = createImageView("icons/document348.png", this::handleNewProject);
        Node openProject = createImageView("icons/folder265.png", this::handleOpen);
        Node saveProject = createImageView("icons/save31.png", this::handleSave);
        Node importProject = createImageView("icons/direction385.png", this::handleImport);
        Node exportProject = createImageView("icons/uploading43.png", this::handleExport);
        
        Node separator0 = createSeparator();
        
        Node undo = createImageView("icons/left207.png", this::handleUndo);
        Node redo = createImageView("icons/arrow621.png", this::handleRedo);
        
        Node separator1 = createSeparator();
        
        Node executeBlock = createImageView("icons/gear47.png", this::handleExecuteBlock);
        Node execute = createImageView("icons/play-button4.png", this::handleExecute);
        Node stop = createImageView("icons/musicplayer145.png", this::handleStop);
        
        tools.getChildren().addAll(newProject, openProject, saveProject, importProject, exportProject, separator0, undo, redo, separator1, executeBlock, execute, stop);
        
        return tools;
    }
    
    private HBox createImageView(String iconDir, Runnable action) {
        Image image = new Image(getClass().getResourceAsStream(iconDir));
        ImageView imageView = new ImageView(image);
        imageView.setOnMouseClicked((MouseEvent evt) -> action.run());
        
        HBox wrapper = new HBox();
        wrapper.getStyleClass().add("gui-button");
        wrapper.getChildren().add(imageView);
        
        return wrapper;
    }
    
    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        
        return separator;
    }
    
    private void handleNewProject() {
        
    }
    
    private void handleOpen() {
        
    }
    
    private void handleSave() {
        
    }
    
    private void handleImport() {
        
    }
    
    private void handleExport() {
        
    }
    
    private void handleUndo() {
        
    }
    
    private void handleRedo() {
        
    }
    
    private void handleExecuteBlock() {
        
    }
    
    private void handleExecute() {
        
    }
    
    private void handleStop() {
        
    }
    
    private void handleOpenOptions() {
        
    }
    
    private void handleOpenCredits() {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Credits");
        Credits creditsPane = new Credits();

        Scene scene = new Scene(creditsPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private void handleClose() {
        Stage stage = (Stage) getScene().getWindow();
        
        stage.close();
    }
    
    public BlueprintPane getDiagramPane() {
        return diagramPane;
    }
    
    public ParametersBox getParametersBox() {
        return parametersBox;
    }
    
}
