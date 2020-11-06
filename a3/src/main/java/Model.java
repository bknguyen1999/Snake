import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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

    SimpleDoubleProperty initX = new SimpleDoubleProperty();
    SimpleDoubleProperty initY = new SimpleDoubleProperty();
    SimpleDoubleProperty rectX = new SimpleDoubleProperty();
    SimpleDoubleProperty rectY = new SimpleDoubleProperty();
    SimpleDoubleProperty rect_w = new SimpleDoubleProperty();
    SimpleDoubleProperty rect_h = new SimpleDoubleProperty();

    ArrayList<IView> views = new ArrayList<IView>();
    Shape selected_shape; // current selected shape on the canvas (must be using the select tool)
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    Tool selected_tool; // current selected tool on the toolbar
    Scene scene;

    public Model(){
        System.out.println("created model");
    }

    public void setScene(Scene scene){
        this.scene = scene;
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

    private void updateToolbarView(){
        IView toolbar = views.get(0);
        toolbar.updateView();
    }

    public void change_tool(Tool tool){
        this.selected_tool = tool;
        updateToolbarView();
        use_tool();
        //updateAllViews();
    }

    public void use_tool(){
        CanvasView canvas = (CanvasView) views.get(2);
        ToolbarView toolbar = (ToolbarView) views.get(0);
        Menu menu = (Menu) views.get(3);

        if (selected_tool == Tool.RECTANGLE){
            scene.setOnMousePressed(null);
            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //System.out.println("in mouse pressed");
                    double toolbar_w = toolbar.getWidth();
                    double menu_h = menu.getHeight();
                    if (event.getX() < toolbar_w){
                        initX.set(-1);
                        return;
                    }
                    if (event.getY() < menu_h) {
                        initY.set(-1);
                        return;
                    }

                    initX.set(event.getX() - toolbar_w);
                    initY.set(event.getY() - menu_h);
                }
            });

            scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (initX.getValue() == -1 || initY.getValue() == -1 ){
                        return;
                    }
                    //System.out.println("in mouse dragged");
                    double toolbar_w = toolbar.getWidth();
                    double menu_h = menu.getHeight();
                    Rectangle r = new Rectangle();
                    r.setFill(Color.TRANSPARENT);
                    r.setStroke(Color.BLACK);
                    r.setStrokeWidth(5);
                    r.setX(initX.get());
                    r.setY(initY.get());
                    //double dx = (event.getX() - toolbar_w) - initX.getValue();
                    //double dy = (event.getY() - menu_h) - initY.getValue();

                    rectX.set(event.getX() - toolbar_w);
                    r.widthProperty().bind(rectX.subtract(initX));
                    rect_w.set(r.getWidth());

                    rectY.set(event.getY() - menu_h);
                    r.heightProperty().bind(rectY.subtract(initY));
                    rect_h.set(r.getHeight());

                    canvas.updateViewDragged(r);
                }
            });

            scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (initX.getValue() == -1 || initY.getValue() == -1 ){
                        return;
                    }
                    canvas.clearTemps();

                    Rectangle rect = new Rectangle();
                    rect.setFill(Color.TRANSPARENT);
                    rect.setStroke(Color.BLACK);
                    rect.setStrokeWidth(5);

                    rect.setX(initX.getValue());
                    rect.setY(initY.getValue());
                    rect.setWidth(rect_w.getValue());
                    rect.setHeight(rect_h.getValue());
                    shapes.add(rect);
                    updateCanvasView();


                    // Hide the rectangle
                    //rectX.set(0);
                    //rectY.set(0);
                }
            });

        }
        else if (selected_tool == Tool.SELECT){
            System.out.println("in select tool");
            scene.setOnMousePressed(null);
            scene.setOnMouseDragged(null);
            scene.setOnMouseReleased(null);

            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    double press_x = event.getX();
                    double press_y = event.getY();
                    for (int i = shapes.size()-1; i >= 0; i--){
                        Shape s = shapes.get(i);
                        s.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                selected_shape = s;
                                //System.out.println(selected_shape.getId());
                                //selected_shape.setFill(Color.YELLOW);
                                updateCanvasView();
                            }
                        });
                    }
                }
            });

//            selected_shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    double toolbar_w = toolbar.getWidth();
//                    double menu_h = menu.getHeight();
//                    Rectangle r = (Rectangle) selected_shape;
//                    double dx = event.getX() - r.getX();
//                    double dy = event.getY() - r.getY();
//                    r.setX(dx);
//                    r.setY(dy);
//
//
//                }
//            });
        }
        else if (selected_tool == Tool.FILL){

        }
    }
}
