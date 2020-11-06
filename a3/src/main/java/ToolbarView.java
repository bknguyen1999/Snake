import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

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
        this.model = model;
        this.default_width = screen_width * 0.2;
        this.default_height = screen_height * 0.59;
        setStyle(bg_color);
        select = new Button("selection tool");
        erase = new Button("erase tool");
        line = new Button("line tool");
        circle = new Button("circle tool");
        rectangle = new Button("rectangle tool");
        fill = new Button("fill tool");
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
