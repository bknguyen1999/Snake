import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.text.StyledEditorKit;

public class PropertiesView extends GridPane implements IView {
    String bg_color = "-fx-background-color:gray;";
    double default_width;
    double default_height;
    Model model;
    ColorPicker line_color;
    ColorPicker fill_color;
    Button no_border = new Button("No\nBorder");
    Button thickness1_button = new Button();
    Button thickness2_button = new Button();
    Button thickness3_button = new Button();
    Button thickness4_button = new Button();
    Button thickness5_button = new Button();
    Button style1 = new Button();
    Button style2 = new Button();
    Button style3 = new Button();
    Button style4 = new Button();
    Button style5 = new Button();
    CheckBox fill_shape = new CheckBox("Fill Shape When Drawn");
    Button thickness_selected = thickness2_button;
    Button style_selected = style1;
    HBox thickness_hbox;
    double thickness1 = 1;
    double thickness2 = 3;
    double thickness3 = 5;
    double thickness4 = 7;
    double thickness5 = 10;
    double dash1 = -1;
    double dash2 = 10d;
    double dash3 = 6d;
    double dash4 = 4d;
    double dash5 = 3d;
    InnerShadow innerShadow;


    public PropertiesView(Model model, double screen_width, double screen_height) {
        super();
        this.model = model;
        this.default_width = screen_width * 0.15;
        this.default_height = screen_height * 0.49;

        this.model.addView(this);
        model.line_thickness = thickness2;
        model.line_style = dash1;
        setLayout();
        registerController();
        updateView();

    }

    private void setLayout() {
        setStyle(bg_color);
        setPrefSize(this.default_width, this.default_height);
        innerShadow = new InnerShadow();
        innerShadow.setColor(Color.RED);
        innerShadow.setHeight(25);
        innerShadow.setRadius(12);
        innerShadow.setWidth(20);
        innerShadow.setChoke(0.5);
        Color default_line = Color.BLACK;
        Color default_fill = Color.YELLOW;
        Label line_color_label = new Label("Line Color");
        line_color_label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        Label fill_color_label = new Label("Fill Color");
        fill_color_label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        line_color = new ColorPicker(default_line);
        fill_color = new ColorPicker(default_fill);
        model.line_color = default_line;
        model.fill_color = default_fill;
        Label thickness_label = new Label("Line Thickness");
        thickness_label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        thickness_label.setAlignment(Pos.CENTER);


        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.HORIZONTAL);

        Label style_label = new Label("Line Style");
        style_label.setFont(Font.font("Verdana", FontWeight.BOLD, 13));


        Line t1 = new Line(0, 20, 20, 0);
        t1.setStrokeWidth(thickness1);
        thickness1_button.setGraphic(t1);
        Line t2 = new Line(0, 20, 20, 0);
        t2.setStrokeWidth(thickness2);
        thickness2_button.setGraphic(t2);
        Line t3 = new Line(0, 20, 20, 0);
        t3.setStrokeWidth(thickness3);
        thickness3_button.setGraphic(t3);
        Line t4 = new Line(0, 20, 20, 0);
        t4.setStrokeWidth(thickness4);
        thickness4_button.setGraphic(t4);
        Line t5 = new Line(0, 20, 20, 0);
        t5.setStrokeWidth(thickness5);
        thickness5_button.setGraphic(t5);

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.HORIZONTAL);

        Line s1 = new Line(0, 20, 20, 0);
        style1.setGraphic(s1);
        Line s2 = new Line(0, 20, 20, 0);
        s2.getStrokeDashArray().add(dash2);
        style2.setGraphic(s2);
        Line s3 = new Line(0, 20, 20, 0);
        s3.getStrokeDashArray().add(dash3);
        style3.setGraphic(s3);
        Line s4 = new Line(0, 20, 20, 0);
        s4.getStrokeDashArray().add(dash4);
        style4.setGraphic(s4);
        Line s5 = new Line(0, 20, 20, 0);
        s5.getStrokeDashArray().add(dash5);
        style5.setGraphic(s5);

        HBox color_label_hbox = new HBox();
        color_label_hbox.getChildren().addAll(line_color_label, fill_color_label);
        color_label_hbox.setSpacing(55);


        HBox color_picker_hbox = new HBox();
        color_picker_hbox.getChildren().addAll(line_color, fill_color);

        thickness_hbox = new HBox();
        no_border.setFont(Font.font("Verdana", 10));
        thickness_hbox.getChildren().addAll(no_border, thickness1_button, thickness2_button, thickness3_button, thickness4_button, thickness5_button);

        HBox style_hbox = new HBox();
        style_hbox.getChildren().addAll(style1, style2, style3, style4, style5);
        add(color_label_hbox, 0, 0);

        add(color_picker_hbox, 0, 1, 2, 1);
        add(fill_shape, 0, 2);

        add(separator1, 0, 3);

        add(thickness_label, 0, 4, 4, 1);
        add(thickness_hbox, 0, 5);

        add(separator2, 0, 6);

        add(style_label, 0, 7);
        add(style_hbox, 0, 8);

        setVgap(15);
        setPadding(new Insets(5, 0, 0,5));

    }

    private void registerController() {
        line_color.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                model.line_color = line_color.getValue();
                if (model.selected_shape != null) {
                    model.setStrokeColor(model.selected_shape);
                }
                //model.line_color = line_color.getValue();
                updateView();
            }
        });

        fill_color.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                model.fill_color = fill_color.getValue();
                if (model.selected_shape != null) {
                    model.fillShape(model.selected_shape);
                }
                //model.fill_color = fill_color.getValue();
                updateView();
            }
        });
        no_border.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_thickness = -1;
                thickness_selected = no_border;
                if (model.selected_shape != null && model.selected_shape.getFill() != Color.TRANSPARENT) {
                    model.removeShapeBorder(model.selected_shape);
                }
                updateView();
            }
        });
        thickness1_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_thickness = thickness1;
                thickness_selected = thickness1_button;
                if (model.selected_shape != null) {
                    model.setShapeStrokeWidth(model.selected_shape);
                }
                updateView();
            }
        });
        thickness2_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_thickness = thickness2;
                thickness_selected = thickness2_button;
                if (model.selected_shape != null) {
                    model.setShapeStrokeWidth(model.selected_shape);
                }
                updateView();
            }
        });
        thickness3_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_thickness = thickness3;
                thickness_selected = thickness3_button;
                if (model.selected_shape != null) {
                    model.setShapeStrokeWidth(model.selected_shape);
                }
                updateView();
            }
        });
        thickness4_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_thickness = thickness4;
                thickness_selected = thickness4_button;
                if (model.selected_shape != null) {
                    model.setShapeStrokeWidth(model.selected_shape);
                }
                updateView();
            }
        });
        thickness5_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_thickness = thickness5;
                thickness_selected = thickness5_button;
                if (model.selected_shape != null) {
                    model.setShapeStrokeWidth(model.selected_shape);
                }
                updateView();
            }
        });
        style1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_style = dash1;
                style_selected = style1;
                if (model.selected_shape != null) {
                    model.setShapeStrokeStyle(model.selected_shape);
                }
                updateView();
            }
        });
        style2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_style = dash2;
                style_selected = style2;
                if (model.selected_shape != null) {
                    model.setShapeStrokeStyle(model.selected_shape);
                }
                updateView();
            }
        });
        style3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_style = dash3;
                style_selected = style3;
                if (model.selected_shape != null) {
                    model.setShapeStrokeStyle(model.selected_shape);
                }
                updateView();
            }
        });
        style4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_style = dash4;
                style_selected = style4;
                if (model.selected_shape != null) {
                    model.setShapeStrokeStyle(model.selected_shape);
                }
                updateView();
            }
        });
        style5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.line_style = dash5;
                style_selected = style5;
                if (model.selected_shape != null) {
                    model.setShapeStrokeStyle(model.selected_shape);
                }
                updateView();
            }
        });
        fill_shape.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.fill_shape = fill_shape.isSelected();
            }
        });
    }


    @Override
    public void updateView() {

        // check if the selected shape has a fill colour or not

        if (model.line_thickness == -1) {
            thickness_selected = no_border;
        } else if (model.line_thickness == thickness1) {
            thickness_selected = thickness1_button;
        } else if (model.line_thickness == thickness2) {
            thickness_selected = thickness2_button;
        } else if (model.line_thickness == thickness3) {
            thickness_selected = thickness3_button;
        } else if (model.line_thickness == thickness4) {
            thickness_selected = thickness4_button;
        } else if (model.line_thickness == thickness5) {
            thickness_selected = thickness5_button;
        }


        if (thickness_selected == no_border) {
            style1.setDisable(true);
            style2.setDisable(true);
            style3.setDisable(true);
            style4.setDisable(true);
            style5.setDisable(true);
            line_color.setDisable(true);
            fill_shape.setDisable(true);
        } else {
            style1.setDisable(false);
            style2.setDisable(false);
            style3.setDisable(false);
            style4.setDisable(false);
            style5.setDisable(false);
            line_color.setDisable(false);
            fill_shape.setDisable(false);
        }

        no_border.setEffect(null);
        thickness1_button.setEffect(null);
        thickness2_button.setEffect(null);
        thickness3_button.setEffect(null);
        thickness4_button.setEffect(null);
        thickness5_button.setEffect(null);
        thickness_selected.setEffect(innerShadow);
        //thickness_selected.setEffect(ds);


        if (model.line_style == dash1) {
            style_selected = style1;
        } else if (model.line_style == dash2) {
            style_selected = style2;
        } else if (model.line_style == dash3) {
            style_selected = style3;
        } else if (model.line_style == dash4) {
            style_selected = style4;
        } else if (model.line_style == dash5) {
            style_selected = style5;
        }
        style1.setEffect(null);
        style2.setEffect(null);
        style3.setEffect(null);
        style4.setEffect(null);
        style5.setEffect(null);
        style_selected.setEffect(innerShadow);
        //style_selected.setEffect(ds);

        fill_color.setValue(model.fill_color);
        line_color.setValue(model.line_color);



        if (model.selected_tool == Model.Tool.SELECT) {
            activateAllButtons();
            if (model.selected_shape != null) {
                fill_shape.setDisable(true);
                if (model.selected_shape.getFill() == Color.TRANSPARENT || model.selected_shape.getFill() == null) {
                    // when the shape has no fill color or the selected shape is a line
                    no_border.setDisable(true);
                } else {
                    no_border.setDisable(false);
                }
                if(thickness_selected == no_border){
                    style1.setDisable(true);
                    style2.setDisable(true);
                    style3.setDisable(true);
                    style4.setDisable(true);
                    style5.setDisable(true);
                }
                else{
                    style1.setDisable(false);
                    style2.setDisable(false);
                    style3.setDisable(false);
                    style4.setDisable(false);
                    style5.setDisable(false);
                }
            } else {
                activateAllButtons();
            }
        }
        else if (model.selected_tool == Model.Tool.ERASE){
            deactivateAllButtons();
        }
        else if (model.selected_tool == Model.Tool.LINE) {
            activateAllButtons();
            fill_color.setDisable(true);
            fill_shape.setDisable(true);
            no_border.setDisable(true);
            if (thickness_selected == no_border) {
                model.line_thickness = thickness1;
                thickness_selected = thickness1_button;
            }
            if (line_color.getValue() == null) {
                line_color.setValue(Color.BLACK);
                model.line_color = Color.BLACK;
            }
        }
        else if (model.selected_tool == Model.Tool.FILL){
            deactivateAllButtons();
            fill_color.setDisable(false);
        }
        else { // shape (circle or rectangle)
            activateAllButtons();
        }


    }


    private void activateAllButtons(){
        line_color.setDisable(false);
        fill_color.setDisable(false);
        fill_shape.setDisable(false);
        no_border.setDisable(false);
        thickness1_button.setDisable(false);
        thickness2_button.setDisable(false);
        thickness3_button.setDisable(false);
        thickness4_button.setDisable(false);
        thickness5_button.setDisable(false);
        style1.setDisable(false);
        style2.setDisable(false);
        style3.setDisable(false);
        style4.setDisable(false);
        style5.setDisable(false);
    }

    private void deactivateAllButtons(){
        line_color.setDisable(true);
        fill_color.setDisable(true);
        fill_shape.setDisable(true);
        no_border.setDisable(true);
        thickness1_button.setDisable(true);
        thickness2_button.setDisable(true);
        thickness3_button.setDisable(true);
        thickness4_button.setDisable(true);
        thickness5_button.setDisable(true);
        style1.setDisable(true);
        style2.setDisable(true);
        style3.setDisable(true);
        style4.setDisable(true);
        style5.setDisable(true);
    }
}
