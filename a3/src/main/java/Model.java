import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    Tool selected_tool; // current selected tool on the toolbar
    Color line_color;
    Color fill_color;
    double line_thickness;
    double line_style;
    boolean fill_shape;
    DropShadow ds = new DropShadow();
    Scene scene;
    boolean saved;
    Shape copied_shape = null;




    public Model(){
        setDefaultValues();
    }

    public void setDefaultValues(){
        selected_tool = Tool.SELECT;
        line_color = Color.BLACK;
        fill_color = Color.YELLOW;
        fill_shape = false;
        line_thickness = 3;
        line_style = -1;
        saved = true;
        selected_shape = null;
        // line thickness and line style are set in PropertiesView
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
        updateCanvasView();
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
                    fillShape(s, fill_color);
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
                saved = false;
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

        scene.setOnKeyPressed(ke -> {
            KeyCode keyCode = ke.getCode();
            if (selected_tool == Model.Tool.SELECT){
                if (keyCode.equals(KeyCode.BACK_SPACE) && selected_shape != null){
                    deleteSelectedShape();
                }
                else if (keyCode.equals(KeyCode.ESCAPE)){
                    deselectShape();
                }
            }
        });
    }
    
    public void selectShape(Shape s){
        selected_shape = s;
        fill_color = (Color)s.getFill();
        if(fill_color == null || fill_color.equals(Color.TRANSPARENT)){
            fill_shape = false;
        }
        else{
            fill_shape = true;
        }
        if (s.getStrokeDashArray().size() > 0){
            line_style = s.getStrokeDashArray().get(0);
        }
        else{
            line_style = -1;
        }
        if(s.getStroke() == null){
            line_thickness = -1;
        }
        else{
            line_thickness = s.getStrokeWidth();
            line_color = (Color)s.getStroke();
        }
        ds.setOffsetX(5);
        ds.setOffsetY(5);
        ds.setWidth(14);
        ds.setHeight(14);
        ds.setColor(Color.RED);
        selected_shape.setEffect(ds);
        updatePropertiesView();
    }

    public void deselectShape(){
        if (selected_shape != null){
            selected_shape.setEffect(null);
            this.selected_shape = null;
            updatePropertiesView();
        }
    }

    public void fillShape(Shape s, Color color){
        saved = false;
        s.setFill(color);
    }

    public void setStrokeColor(Shape s){
        saved = false;
        s.setStroke(line_color);
    }

    public void removeShapeBorder(Shape s){
        saved = false;
        s.setStroke(null);
    }

    public void setShapeStrokeWidth(Shape s){
        saved = false;
        s.setStroke(line_color);
        s.setStrokeWidth(line_thickness);
    }

    public void setShapeStrokeStyle(Shape s){
        saved = false;
        s.getStrokeDashArray().clear();
        if(line_style != -1){
            s.getStrokeDashArray().add(line_style);
        }
    }


    public void eraseShape(Shape s){
        saved = false;
        CanvasView canvas = (CanvasView)views.get(2);
        shapes.remove(s);
        canvas.getChildren().remove(s);
    }

    public void deleteSelectedShape(){
        saved = false;
        CanvasView canvas = (CanvasView)views.get(2);
        shapes.remove(selected_shape);
        canvas.getChildren().remove(selected_shape);
        selected_shape = null;
        updatePropertiesView();
    }

    public void newFile(){
        shapes.clear();
        setDefaultValues();
        updateCanvasView();
        updateToolbarView();
        updatePropertiesView();
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public Shape copyShape(Shape s){
        String s_info = s.toString();
        String s_type = s_info.split("\\[")[0];
        if (s_type.equals("Rectangle")){
            Rectangle s_rect = (Rectangle) s;
            Rectangle s_copy = new Rectangle();
            s_copy.setX(s_rect.getX());
            s_copy.setY(s_rect.getY());
            s_copy.setWidth(s_rect.getWidth());
            s_copy.setHeight(s_rect.getHeight());
            s_copy.setStroke(s_rect.getStroke());
            s_copy.setFill(s_rect.getFill());
            s_copy.setStrokeWidth(s_rect.getStrokeWidth());
            if(s_rect.getStrokeDashArray().size() > 0){
                s_copy.getStrokeDashArray().add(s_rect.getStrokeDashArray().get(0));
            }
            s_copy.setLayoutX(s_rect.getLayoutX());
            s_copy.setLayoutY(s_rect.getLayoutY());
            return s_copy;
        }
        else if(s_type.equals("Ellipse")){
            Ellipse s_circle = (Ellipse) s;
            Ellipse s_copy = new Ellipse();
            s_copy.setCenterX(s_circle.getCenterX());
            s_copy.setCenterY(s_circle.getCenterY());
            s_copy.setRadiusX(s_circle.getRadiusX());
            s_copy.setRadiusY(s_circle.getRadiusY());
            s_copy.setStroke(s_circle.getStroke());
            s_copy.setFill(s_circle.getFill());
            s_copy.setStrokeWidth(s_circle.getStrokeWidth());
            if(s_circle.getStrokeDashArray().size() > 0){
                s_copy.getStrokeDashArray().add(s_circle.getStrokeDashArray().get(0));
            }
            s_copy.setLayoutX(s_circle.getLayoutX());
            s_copy.setLayoutY(s_circle.getLayoutY());
            return s_copy;
        }
        else if (s_type.equals("Line")){
            Line s_line = (Line) s;
            Line s_copy = new Line();
            s_copy.setStartX(s_line.getStartX());
            s_copy.setStartY(s_line.getStartY());
            s_copy.setEndX(s_line.getEndX());
            s_copy.setEndY(s_line.getEndY());
            s_copy.setStroke(s_line.getStroke());
            s_copy.setStrokeWidth(s_line.getStrokeWidth());
            if(s_line.getStrokeDashArray().size() > 0){
                s_copy.getStrokeDashArray().add(s_line.getStrokeDashArray().get(0));
            }
            s_copy.setLayoutX(s_line.getLayoutX());
            s_copy.setLayoutY(s_line.getLayoutY());
            return s_copy;
        }
        else{
            return null;
        }


    }

    public void pasteShape(){
        if (copied_shape != null){
            saved = false;
            Shape temp = copyShape(copied_shape);
            temp.setLayoutX(temp.getLayoutX()+10);
            temp.setLayoutY(temp.getLayoutY()+10);
            configureShape(temp);
            CanvasView cv = (CanvasView) views.get(2);
            cv.getChildren().add(temp);
        }
    }

    public void cutShape(Shape s){
        saved = false;
        Shape temp = copyShape(s);
        temp.setLayoutX(temp.getLayoutX());
        temp.setLayoutY(temp.getLayoutY());
        copied_shape = temp;
        deselectShape();
        shapes.remove(s);
        CanvasView cv = (CanvasView) views.get(2);
        cv.getChildren().remove(s);
    }



}
