package pl.wurmonline.mapplanner.gui.nodes;

import java.util.Timer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import pl.wurmonline.mapplanner.util.Log;

public final class ArgumentNode extends VBox {    
    
    private final BlueprintPane root;
    private final BlockNode parent;
    
    private final Node editor;
    private final Circle circle;
    
    private final Argument argument;
    private final Type type;
    private ArgumentNode boundArgument;
    private CubicCurve bindCurve;
    
    public ArgumentNode(BlueprintPane root, BlockNode parent, Argument argument, Type type) {
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
        
        argument.inputProperty().addListener(new ChangeListener<Argument>() {
            public void changed(ObservableValue<? extends Argument> observable, Argument oldValue, Argument newValue) {
                bind(root.lookupArgumentNode(newValue));
            }
        });
    }
    
    void deserializeLinks() {
        if (argument.getInput() != null) {
            bind(root.lookupArgumentNode(argument.getInput()));
        }
    }
    
    public void bind(ArgumentNode arg) {
        if (arg == null || this.type == arg.type || this.parent == arg.parent || this.argument.getData().getClass() != arg.argument.getData().getClass()) {
            return;
        }
        
        ArgumentNode guiInput = (type == Type.INPUT) ? this : arg;
        ArgumentNode guiOutput = (type == Type.OUTPUT) ? this : arg;
        
        this.boundArgument = arg;
        arg.boundArgument = this;
        
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
        
        Log.info(this, "Linked with argument " + arg);
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
        
        double panelX = parent.getPanel().getLayoutX();
        double panelY = parent.getPanel().getLayoutY();
        double parentX = parent.getPanel().getParent().getLayoutX();
        double parentY = parent.getPanel().getParent().getLayoutY();
        double layoutX = panelX + parentX;
        double layoutY = panelY + parentY;
        
        Point2D inputCircle = guiInput.localToScene(new Point2D(guiInput.getCircleX() - layoutX, guiInput.getCircleY() - layoutY));
        Point2D outputCircle = guiOutput.localToScene(new Point2D(guiOutput.getCircleX() - layoutX, guiOutput.getCircleY() - layoutY));
        
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
        return "GUI " + argument.toString();
    }
    
    public static enum Type {
        INPUT, OUTPUT;
    }
    
}
