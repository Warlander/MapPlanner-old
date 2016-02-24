package pl.wurmonline.mapplanner.gui.nodes;

import java.util.Timer;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import pl.wurmonline.mapplanner.blocks.Argument;
import static pl.wurmonline.mapplanner.GUIConstants.*;

public class ArgumentNode extends VBox {    
    
    private final BlueprintNode root;
    private final BlockNode parent;
    
    private final Node editor;
    private final Circle circle;
    
    private final Argument argument;
    private final Type type;
    private ArgumentNode boundArgument;
    private CubicCurve bindCurve;
    
    public ArgumentNode(BlueprintNode root, BlockNode parent, Argument argument, Type type) {
        if (root == null || argument == null || type == null) {
            throw new IllegalArgumentException("Argument or type cannot be null");
        }
        
        this.root = root;
        this.parent = parent;
        this.argument = argument;
        this.type = type;
        
        if (type == Type.INPUT) {
            this.editor = argument.getEditor();
        }
        else {
            this.editor = null;
        }
        
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
            
        Region reg = new Region();

        circle = new Circle(CIRCLE_SIZE);
        circle.setFill(argument.getData().getGUIColor());
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(CIRCLE_BORDER);

        Label argName = new Label();
        argName.textProperty().bind(argument.titleProperty());

        if (type == Type.INPUT) {
            box.getChildren().addAll(reg, circle, argName);
        }
        else {
            box.getChildren().addAll(argName, circle, reg);
        }
        
        this.getChildren().add(box);
        
        if (editor != null) {
            this.getChildren().add(editor);
        }
        
        this.setOnMousePressed((evt) -> {
            root.handleNewBind(this);
        });
    }
    
    public void bind(ArgumentNode arg) {
        if (this.type == arg.type || this.parent == arg.parent || this.argument.getData().getClass() != arg.argument.getData().getClass()) {
            return;
        }
        
        ArgumentNode guiInput = (type == Type.INPUT) ? this : arg;
        ArgumentNode guiOutput = (type == Type.OUTPUT) ? this : arg;
        
        this.boundArgument = arg;
        arg.boundArgument = this;
        
        guiInput.argument.setInput(guiOutput.argument);
        
        guiInput.getChildren().remove(guiInput.editor);
        
        CubicCurve link = new CubicCurve();
        link.setStroke(Color.BLACK);
        link.setStrokeWidth(3);
        link.setFill(null);
        
        link.controlX1Property().bind(link.endXProperty());
        link.controlY1Property().bind(link.startYProperty());
        link.controlX2Property().bind(link.startXProperty());
        link.controlY2Property().bind(link.endYProperty());
        
        root.getChildren().add(link);
        
        guiInput.bindCurve = link;
        guiOutput.bindCurve = link;
        
        final Timer timer = new Timer();
        timer.schedule( 
            new java.util.TimerTask() {
                public void run() {
                    guiInput.parent.updateAllLinks();
                    timer.cancel();
                }
            }
        , 100);
    }
    
    public void unbind() {
        if (boundArgument == null) {
            return;
        }
        
        ArgumentNode guiInput = (type == Type.INPUT) ? this : boundArgument;
        ArgumentNode guiOutput = (type == Type.OUTPUT) ? this : boundArgument;
        
        guiInput.boundArgument = null;
        guiOutput.boundArgument = null;
        
        guiInput.argument.setInput(null);
        
        guiInput.getChildren().add(guiInput.editor);
        
        root.getChildren().remove(bindCurve);
        guiInput.bindCurve = null;
        guiOutput.bindCurve = null;
        
        guiInput.parent.updateAllLinks();
        
        final Timer timer = new Timer();
        timer.schedule( 
            new java.util.TimerTask() {
                public void run() {
                    guiInput.parent.updateAllLinks();
                    timer.cancel();
                }
            }
        , 100);
    }
    
    void updateLinks() {
        if (bindCurve == null) {
            return;
        }
        
        ArgumentNode guiInput = (type == Type.INPUT) ? this : boundArgument;
        ArgumentNode guiOutput = (type == Type.OUTPUT) ? this : boundArgument;
        
        Point2D inputCircle = guiInput.localToScene(new Point2D(guiInput.getCircleX() - parent.getPanel().getLayoutX(), guiInput.getCircleY() - parent.getPanel().getLayoutY()));
        Point2D outputCircle = guiOutput.localToScene(new Point2D(guiOutput.getCircleX() - parent.getPanel().getLayoutX(), guiOutput.getCircleY() - parent.getPanel().getLayoutY()));
        
        bindCurve.setStartX(inputCircle.getX());
        bindCurve.setStartY(inputCircle.getY());
        bindCurve.setEndX(outputCircle.getX());
        bindCurve.setEndY(outputCircle.getY());
    }
    
    public void destroy() {
        unbind();
    }
    
    public Argument getArgument() {
        return argument;
    }
    
    public Type getType() {
        return type;
    }
    
    public double getCircleX() {
        return circle.getLayoutX();
    }
    
    public double getCircleY() {
        return circle.getLayoutY();
    }
    
    public String toString() {
        return argument.getTitle();
    }
    
    public static enum Type {
        INPUT, OUTPUT;
    }
    
}
