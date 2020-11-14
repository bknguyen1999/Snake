import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;


public class CanvasView extends Pane implements IView{
    Model model;
    static double default_width;
    static double default_height;
    Pane canvas;
    Rectangle rect;
    Ellipse circle;
    Line line;
    double initX;
    double initY;


    public CanvasView(Model model, double screen_width, double screen_height){
        super();
        this.model = model;
        default_width = screen_width * 0.82;
        default_height = screen_height * 0.98;

        setLayout();
        this.model.addView(this);
        registerController();

    }

    private void setLayout(){
        canvas = new Pane();
        canvas.setPrefSize(default_width, default_height);
        canvas.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        getChildren().add(canvas);
        setPrefSize(default_width, default_height);
    }

    private void registerController(){
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (model.selected_tool == Model.Tool.RECTANGLE){
                    initX = event.getX();
                    initY = event.getY();
                    rect = new Rectangle(initX, initY, 0, 0);
                    if(model.fill_shape){
                        rect.setFill(model.fill_color);
                    }
                    else{
                        rect.setFill(Color.TRANSPARENT);
                    }
                    if(model.line_thickness != -1){
                        rect.setStroke(model.line_color);
                        rect.setStrokeWidth(model.line_thickness);
                    }
                    else{
                        rect.setFill(model.fill_color);
                    }
                    
                    if (model.line_style != -1){
                        rect.getStrokeDashArray().add(model.line_style);
                    }
                    model.configureShape(rect);
                    getChildren().add(rect);
                    model.saved = false;
                }
                else if (model.selected_tool == Model.Tool.CIRCLE){
                    initX = event.getX();
                    initY = event.getY();
                    circle = new Ellipse(initX, initY, 0, 0);

                    if (model.fill_shape){
                        circle.setFill(model.fill_color);
                    }
                    else{
                        circle.setFill(Color.TRANSPARENT);
                    }
                    if(model.line_thickness != -1){
                        circle.setStroke(model.line_color);
                        circle.setStrokeWidth(model.line_thickness);
                    }
                    else{
                        circle.setFill(model.fill_color);
                    }
                    if (model.line_style != -1){
                        circle.getStrokeDashArray().add(model.line_style);
                    }
                    model.configureShape(circle);
                    getChildren().add(circle);
                    model.saved = false;
                }
                else if (model.selected_tool == Model.Tool.LINE){
                    initX = event.getX();
                    initY = event.getY();
                    line = new Line(initX, initY, initX, initY);
                    line.setStroke(model.line_color);
                    line.setStrokeWidth(model.line_thickness);
                    if (model.line_style != -1){
                        line.getStrokeDashArray().add(model.line_style);
                    }
                    model.configureShape(line);
                    getChildren().add(line);
                    model.saved = false;
                }
                else{
                    return;
                }
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (model.selected_tool == Model.Tool.RECTANGLE){
                    double dx = event.getX() - initX;
                    double dy = event.getY() - initY;
                    if(dx < 0){
                        rect.setX(event.getX());
                        rect.setWidth(-dx);
                    }
                    else{
                        rect.setWidth(event.getX() - initX);
                    }
                    if (dy < 0){
                        rect.setY(event.getY());
                        rect.setHeight(-dy);
                    }
                    else{
                        rect.setHeight(event.getY() - initY);
                    }
                    // so you cant drag the rectangle out of bounds
                    if(rect.getX() < 0){
                        rect.setX(0);
                    }
                    if(rect.getY() < 0){
                        rect.setY(0);
                    }
                }
                else if (model.selected_tool == Model.Tool.CIRCLE){
                    circle.setCenterX((event.getX() + initX) / 2);
                    circle.setCenterY((event.getY() + initY) / 2);
                    circle.setRadiusX(Math.abs((event.getX() - initX) / 2));
                    circle.setRadiusY(Math.abs((event.getY() - initY) / 2));

                    // make sure you can't drag circle out of bounds
                    if (circle.getBoundsInParent().getMinX() < 0){
                        circle.setLayoutX(circle.getLayoutX() - circle.getBoundsInParent().getMinX());
                    }
                    if (circle.getBoundsInParent().getMinY() < 0){
                        circle.setLayoutY(circle.getLayoutY() - circle.getBoundsInParent().getMinY());
                    }
                }
                else if (model.selected_tool == Model.Tool.LINE){
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());
                    if (event.getX() < 0){
                        line.setEndX(0);
                    }
                    if (event.getY() < 0){
                        line.setEndY(0);
                    }
                }
            }
        });

        // CLICK ON THE CANVAS (NOT A SHAPE)
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (model.selected_tool == Model.Tool.SELECT){
                    //System.out.println("clicked on canvas");
                    model.deselectShape();
                }
                if (model.selected_tool == Model.Tool.FILL){
                    model.saved = false;
                    canvas.setBackground(new Background(new BackgroundFill(model.fill_color, null, null)));

                }
            }
        });

    }


    public void newCanvas(){
        getChildren().clear();
        canvas = new Pane();
        canvas.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        canvas.setPrefSize(default_width, default_height);
        getChildren().add(canvas);
        registerController();
    }


    @Override
    public void updateView() {
        return;
    }


}




