import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;

import java.awt.*;
import java.util.ArrayList;


public class CanvasView extends BorderPane implements IView{
    Model model;
    static double default_width;
    static double default_height;
    ArrayList<Shape> temps = new ArrayList<Shape>();
    Canvas canvas;
    GraphicsContext gc;

    public CanvasView(Model model, double screen_width, double screen_height){
        this.model = model;
        default_width = screen_width * 0.8;
        default_height = screen_height * 0.98;
        canvas = new Canvas(default_width, default_height);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(canvas);
        setStyle("-fx-background-color:green;");
        setPrefSize(default_width, default_height);
        //setWidth(default_width);
        //setHeight(default_height);
        this.model.addView(this);
    }

    public void updateViewDragged(Shape shape){
        temps.add(shape);
        drawRectangle((Rectangle)shape);
        getChildren().add(shape);
    }

    public void clearTemps(){
        //System.out.println("in clear temps");
        for (Shape s: this.temps){
            getChildren().remove(s);
        }
        temps.clear();
    }


    @Override
    public void updateView() {
        if (model.selected_tool == Model.Tool.CIRCLE || model.selected_tool == Model.Tool.RECTANGLE){
            Shape s = model.shapes.get(model.shapes.size()-1);
            //getChildren().add(s);
            drawRectangle((Rectangle)s);
            getChildren().add(s);
        }
        else if (model.selected_tool == Model.Tool.SELECT){
            Shape ss = null;
            for (Shape s: this.model.shapes){
                if (s == this.model.selected_shape){
                    ss = s;
                }
                else{
                    s.setStroke(Color.BLACK);
                    //s.getStrokeDashArray().addAll();
                }
            }
            if (ss != null){
                ss.setStrokeDashOffset(10);
                ss.setStroke(Color.BLUE);
                //ss.getStrokeDashArray().addAll(80.0, 70.0, 60.0, 50.0, 40.0);
            }


        }

    }

    public void drawRectangle(Rectangle r){
        gc.beginPath();

        double x;
        double y;
        double width;
        double height;
        if (r.getWidth() < 0){
            x = r.getX() + r.getWidth();
            width = -1 * r.getWidth();
        }
        else{
            x = r.getX();
            width = r.getWidth();
        }
        if (r.getHeight() < 0){
            y = r.getY() + r.getHeight();
            height = -1 * r.getHeight();
        }
        else{
            y = r.getY();
            height = r.getHeight();
        }
        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("width: " + width);
        System.out.println("height: " + height);

        gc.rect(x,y,width,height);
        gc.setFill(Color.TRANSPARENT);
        gc.setStroke(Color.BLACK);
        gc.fill();
        gc.closePath();
    }

//    public void showSelectedShape(Shape selected){
//        selected.setFill(Color.YELLOW);
//    }

}



