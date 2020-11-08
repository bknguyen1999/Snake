import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.stage.Screen;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;

public class Model {
    enum Tool{
        SELECT,
        ERASE,
        LINE,
        CIRCLE,
        RECTANGLE,
        FILL
    }


    double initX;
    double initY;

    ArrayList<IView> views = new ArrayList<IView>();
    Shape selected_shape = null; // current selected shape on the canvas (must be using the select tool)
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    Tool selected_tool = Tool.SELECT; // current selected tool on the toolbar
    Color line_color;
    Color fill_color;
    double line_thickness;
    double line_style = -1;
    boolean fill_shape = false;
    DropShadow ds = new DropShadow();




    public Model(){
        System.out.println("created model");
    }
    


    public void addView(IView view){
        this.views.add(view);
    }

    private void updateAllViews(){
        for (IView view: this.views){
            view.updateView();
        }
    }

    private void updateCanvasView(){
        IView canvas = views.get(2);
        canvas.updateView();
    }
    
    private void updatePropertiesView(){
        PropertiesView properties = (PropertiesView) views.get(1);
        properties.updateView();
    }

    private void updateToolbarView(){
        IView toolbar = views.get(0);
        toolbar.updateView();
    }
    

    public void change_tool(Tool tool){
        deselectShape();
        this.selected_tool = tool;
        updateToolbarView();
        updatePropertiesView();
    }


    public void configureShape(Shape s){
        shapes.add(s);
        // select shape
        s.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selected_tool == Tool.SELECT){
                    if (s != selected_shape){
                        deselectShape();
                    }
                    selectShape(s);
                }
                else if (selected_tool == Tool.ERASE){
                    eraseShape(s);
                }
                else if (selected_tool == Tool.FILL){
                    s.setFill(fill_color);
                }
            }
        });
        // move shape
        s.setOnMousePressed(event -> {
            if(selected_tool == Tool.SELECT && selected_shape == s){
                initX = event.getX();
                initY = event.getY();
            }
        });
        s.setOnMouseDragged(event -> {
            if (selected_tool == Tool.SELECT && selected_shape == s){
                double newX = s.getLayoutX() + event.getX() - initX;
                double newY = s.getLayoutY() + event.getY() - initY;
                s.setLayoutX(newX);
                s.setLayoutY(newY);
                // make sure you dont drag it out of bounds
                if (s.getBoundsInParent().getMinX() < 0){
                    s.setLayoutX(s.getLayoutX() - s.getBoundsInParent().getMinX());
                }
                if (s.getBoundsInParent().getMinY() < 0){
                    s.setLayoutY(s.getLayoutY() - s.getBoundsInParent().getMinY());
                }
            }
        });
//        s.setOnKeyPressed(ke -> {
//            if (selected_tool == Tool.SELECT){
//                KeyCode keyCode = ke.getCode();
//                if (keyCode.equals(KeyCode.DELETE)){
//                    System.out.println("pressed delete on selected shape");
//                    deleteSelectedShape();
//                    return;
//                }
//            }
//        });
    }
    
    public void selectShape(Shape s){
        selected_shape = s;
        fill_color = (Color)s.getFill();
        //line_color = (Color)s.getStroke();
        if (s.getStrokeDashArray().size() > 0){
            line_style = s.getStrokeDashArray().get(0);
        }
        else{
            line_style = -1;
        }
        if(s.getStroke() == null){
            line_thickness = -1;
            line_color = null;
        }
        else{
            line_thickness = s.getStrokeWidth();
            line_color = (Color)s.getStroke();
        }
        //updatePropertiesView();
        ds.setOffsetX(3);
        ds.setOffsetY(3);
        ds.setColor(Color.RED);
        selected_shape.setEffect(ds);
        updatePropertiesView();
    }

    public void deselectShape(){
        if (selected_shape != null){
            selected_shape.setEffect(null);
            this.selected_shape = null;
        }
    }


    public void eraseShape(Shape s){
        CanvasView canvas = (CanvasView)views.get(2);
        shapes.remove(s);
        canvas.getChildren().remove(s);
    }

    public void deleteSelectedShape(){
        CanvasView canvas = (CanvasView)views.get(2);
        shapes.remove(selected_shape);
        canvas.getChildren().remove(selected_shape);
        selected_shape = null;
    }

}
