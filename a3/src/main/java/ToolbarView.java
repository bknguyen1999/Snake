import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javax.tools.Tool;


public class ToolbarView extends GridPane implements IView {
    String bg_color = "-fx-background-color:orange;";
    double default_width;
    double default_height;
    Model model;
    Button select;
    Button erase;
    Button line;
    Button circle;
    Button rectangle;
    Button fill;

    public ToolbarView(Model model, double screen_width, double screen_height){
        super();
        this.model = model;
        this.default_width = screen_width * 0.2;
        this.default_height = screen_height * 0.59;
        setStyle(bg_color);
        select = new Button();
        erase = new Button();
        line = new Button();
        circle = new Button();
        rectangle = new Button();
        fill = new Button();
        Rectangle rec = new Rectangle(60,54);
        rectangle.setGraphic(rec);
        Line l = new Line(0,54,60,0);
        line.setGraphic(l);
        Ellipse c = new Ellipse(0,0,28.5,28.5);
        circle.setGraphic(c);
        fill.setGraphic(new ImageView(new Image("file:src/main/resources/fill2.png")));
        erase.setGraphic(new ImageView(new Image("file:src/main/resources/eraser.png")));
        select.setGraphic(new ImageView(new Image("file:src/main/resources/select.png")));
        add(select, 0, 0);
        add(erase, 1, 0);
        add(line, 0, 1);
        add(circle, 1, 1);
        add(rectangle, 0, 2);
        add(fill, 1, 2);
        setPrefSize(this.default_width, this.default_height);

        setHgap(50);
        setVgap(50);
        this.model.addView(this);
        updateView();
        toolbar_input();

    }


    @Override
    public void updateView() {
        if (model.selected_tool == Model.Tool.RECTANGLE){
            rectangle.setStyle("-fx-background-color:red;");
            erase.setStyle(null);
            line.setStyle(null);
            circle.setStyle(null);
            select.setStyle(null);
            fill.setStyle(null);
        }
        else if (model.selected_tool == Model.Tool.CIRCLE){
            circle.setStyle("-fx-background-color:red;");
            erase.setStyle(null);
            line.setStyle(null);
            select.setStyle(null);
            rectangle.setStyle(null);
            fill.setStyle(null);
        }
        else if (model.selected_tool == Model.Tool.SELECT){
            select.setStyle("-fx-background-color:red;");
            erase.setStyle(null);
            line.setStyle(null);
            circle.setStyle(null);
            rectangle.setStyle(null);
            fill.setStyle(null);
        }
        else if (model.selected_tool == Model.Tool.LINE){
            line.setStyle("-fx-background-color:red;");
            erase.setStyle(null);
            select.setStyle(null);
            circle.setStyle(null);
            rectangle.setStyle(null);
            fill.setStyle(null);
        }
        else if (model.selected_tool == Model.Tool.ERASE){
            erase.setStyle("-fx-background-color:red;");
            select.setStyle(null);
            line.setStyle(null);
            circle.setStyle(null);
            rectangle.setStyle(null);
            fill.setStyle(null);
        }
        else if (model.selected_tool == Model.Tool.FILL){
            fill.setStyle("-fx-background-color:red;");
            erase.setStyle(null);
            line.setStyle(null);
            circle.setStyle(null);
            rectangle.setStyle(null);
            select.setStyle(null);
        }

    }

    private void toolbar_input(){
        this.select.setOnAction(event -> {model.change_tool(Model.Tool.SELECT);});
        this.erase.setOnAction(event -> {model.change_tool(Model.Tool.ERASE);});
        this.line.setOnAction(event -> {model.change_tool(Model.Tool.LINE);});
        this.circle.setOnAction(event -> {model.change_tool(Model.Tool.CIRCLE);});
        this.rectangle.setOnAction(event -> {model.change_tool(Model.Tool.RECTANGLE);});
        this.fill.setOnAction(event -> {model.change_tool(Model.Tool.FILL);});
    }
}
