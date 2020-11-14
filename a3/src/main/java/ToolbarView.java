import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class ToolbarView extends GridPane implements IView {
    //String bg_color = "-fx-background-color:orange;";
    double default_width;
    double default_height;
    Model model;
    Button select;
    Button erase;
    Button line;
    Button circle;
    Button rectangle;
    Button fill;
    InnerShadow innerShadow;

    public ToolbarView(Model model, double screen_width, double screen_height){
        super();
        this.model = model;
        this.default_width = screen_width * 0.18;
        this.default_height = screen_height * 0.44;

        setLayout();
        this.model.addView(this);
        updateView();
        registerController();

    }

    private void setLayout(){
        //setStyle(bg_color);
        setBackground(new Background(new BackgroundFill(Paint.valueOf("#66b2b2"), null, null)));
        setMinWidth(default_width);
        innerShadow = new InnerShadow();
        innerShadow.setColor(Color.RED);
        innerShadow.setHeight(25);
        innerShadow.setRadius(12);
        innerShadow.setWidth(20);
        innerShadow.setChoke(0.5);
        Label tools_label = new Label("Tools");
        tools_label.setTextFill(Color.WHITE);
        tools_label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        tools_label.setUnderline(true);
        //tools_label.setPadding(new Insets(5,0,0,0));
        select = new Button();
        erase = new Button();
        line = new Button();
        circle = new Button();
        rectangle = new Button();
        fill = new Button();
        Rectangle rec = new Rectangle(60,54);
        rec.setStroke(Color.BLACK);
        rec.setStrokeWidth(3);
        rec.setFill(Color.TRANSPARENT);
        rectangle.setGraphic(rec);
        Line l = new Line(0,54,58,0);
        l.setStrokeWidth(3);
        line.setGraphic(l);
        Ellipse c = new Ellipse(0,0,28.5,28.5);
        c.setStroke(Color.BLACK);
        c.setStrokeWidth(3);
        c.setFill(Color.TRANSPARENT);
        circle.setGraphic(c);
        fill.setGraphic(new ImageView(new Image("file:src/main/resources/fill2.png")));
        erase.setGraphic(new ImageView(new Image("file:src/main/resources/eraser.png")));
        select.setGraphic(new ImageView(new Image("file:src/main/resources/select.png")));
        add(tools_label, 0, 0, 2, 1);
        add(select, 0, 1);
        add(erase, 1, 1);
        add(line, 0, 2);
        add(circle, 1, 2);
        add(rectangle, 0, 3);
        add(fill, 1, 3);
        setPrefSize(this.default_width, this.default_height);
        //tools_label.setAlignment(Pos.CENTER);
        setHalignment(tools_label, HPos.CENTER);
        setAlignment(Pos.TOP_CENTER);


        setHgap(40);
        setVgap(20);
        //setPadding(new Insets(0,0,0,5));
    }


    @Override
    public void updateView() {
        if (model.selected_tool == Model.Tool.RECTANGLE){
            rectangle.setEffect(innerShadow);
            erase.setEffect(null);
            line.setEffect(null);
            circle.setEffect(null);
            select.setEffect(null);
            fill.setEffect(null);
        }
        else if (model.selected_tool == Model.Tool.CIRCLE){
            circle.setEffect(innerShadow);
            erase.setEffect(null);
            line.setEffect(null);
            select.setEffect(null);
            rectangle.setEffect(null);
            fill.setEffect(null);
        }
        else if (model.selected_tool == Model.Tool.SELECT){
            select.setEffect(innerShadow);
            erase.setEffect(null);
            line.setEffect(null);
            circle.setEffect(null);
            rectangle.setEffect(null);
            fill.setEffect(null);
        }
        else if (model.selected_tool == Model.Tool.LINE){
            line.setEffect(innerShadow);
            erase.setEffect(null);
            select.setEffect(null);
            circle.setEffect(null);
            rectangle.setEffect(null);
            fill.setEffect(null);
        }
        else if (model.selected_tool == Model.Tool.ERASE){
            erase.setEffect(innerShadow);
            select.setEffect(null);
            line.setEffect(null);
            circle.setEffect(null);
            rectangle.setEffect(null);
            fill.setEffect(null);
        }
        else if (model.selected_tool == Model.Tool.FILL){
            fill.setEffect(innerShadow);
            erase.setEffect(null);
            line.setEffect(null);
            circle.setEffect(null);
            rectangle.setEffect(null);
            select.setEffect(null);
        }

    }

    private void registerController(){
        this.select.setOnAction(event -> {model.change_tool(Model.Tool.SELECT);});
        this.erase.setOnAction(event -> {model.change_tool(Model.Tool.ERASE);});
        this.line.setOnAction(event -> {model.change_tool(Model.Tool.LINE);});
        this.circle.setOnAction(event -> {model.change_tool(Model.Tool.CIRCLE);});
        this.rectangle.setOnAction(event -> {model.change_tool(Model.Tool.RECTANGLE);});
        this.fill.setOnAction(event -> {model.change_tool(Model.Tool.FILL);});
    }
}
