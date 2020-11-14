import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class MenuView extends HBox implements IView {
    Model model;
    double width;
    double height;
    double screen_height;
    double screen_width;
    Menu file;
    Menu edit;
    Menu about;
    MenuItem save;
    MenuItem new_file;
    MenuItem load_file;
    MenuItem quit;
    MenuItem copy;
    MenuItem paste;
    MenuItem cut;
    MenuItem desc;
    FileChooser filechooser;
    Stage stage;
    CanvasView canvas;
    String ENDL = "\n";
    String DELIMITER = ",";
    File cur_file = null;
    Dialog save_prompt;
    Dialog about_screen;


    public MenuView(Model model, double screen_width, double screen_height, Stage stage, CanvasView canvas){
        this.model = model;
        this.screen_height = screen_height;
        this.screen_width = screen_width;
        this.width = screen_width;
        this.height = screen_height * 0.02;
        this.stage = stage;
        this.canvas = canvas;

        setLayout();
        this.model.addView(this);
        registerController();

    }


    private void setLayout(){
        //setStyle("-fx-background-color:beige;");
        setBackground(new Background(new BackgroundFill(Paint.valueOf("b2d8d8"), null, null)));
        setPrefSize(width,height);
        //setBackground(new Background(new BackgroundFill(Paint.valueOf("E5FCC2"), null, null)));

        file = new Menu("File");
        new_file = new MenuItem("New File");
        save = new MenuItem("Save");
        load_file = new MenuItem("Load file");
        quit = new MenuItem("Quit");
        file.getItems().addAll(new_file,  load_file, save, quit);

        edit = new Menu("Edit");
        copy = new MenuItem("Copy");
        paste = new MenuItem("Paste");
        cut = new MenuItem("Cut");
        edit.getItems().addAll(copy, paste, cut);

        about = new Menu("About");
        desc = new MenuItem("See Student Info");
        about.getItems().add(desc);

        filechooser = new FileChooser();
        filechooser.setTitle("Save");

        MenuBar menubar = new MenuBar(file, edit, about);
        menubar.setBackground(new Background(new BackgroundFill(Paint.valueOf("b2d8d8"), null, null)));



        getChildren().addAll(menubar);

    }

    private int save(){
        FileWriter file = null;
        BufferedWriter writer = null;
        try {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("SketchIt files (*.sketchit)", "*.sketchit")));
            chooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")));
            chooser.setTitle("Save File");
            if(cur_file != null){
                chooser.setInitialFileName(cur_file.getName());
            }
            File selectedFile = chooser.showSaveDialog(stage);

            if (selectedFile != null){
                cur_file = selectedFile;
                file = new FileWriter(selectedFile);
                writer = new BufferedWriter(file);

                // first line of the file will always be the background colour of the canvas
                List<BackgroundFill> canvas_bg_fills = canvas.canvas.getBackground().getFills();
                BackgroundFill fill = canvas_bg_fills.get(0);
                Paint bg_color = fill.getFill();
                writer.write(bg_color + ENDL);

                for (int i = 0; i < model.shapes.size(); i++){
                    Shape s = model.shapes.get(i);
                    String s_info = s.toString();
                    String s_type = s_info.split("\\[")[0];
                    if (s_type.equals("Rectangle")){
                        Rectangle s_rect = (Rectangle) s;
                        double x = s_rect.getX();
                        double y = s_rect.getY();
                        double rect_width = s_rect.getWidth();
                        double rect_height = s_rect.getHeight();
                        Color line_colour = (Color) s_rect.getStroke();
                        Color fill_colour = (Color) s_rect.getFill();
                        double line_thickness = s_rect.getStrokeWidth();
                        ObservableList line_style = s_rect.getStrokeDashArray();
                        double layoutX = s_rect.getLayoutX();
                        double layoutY = s_rect.getLayoutY();
                        writer.write(s_type + DELIMITER + x +DELIMITER + y + DELIMITER + rect_width + DELIMITER + rect_height + DELIMITER +
                                line_colour.toString() + DELIMITER + fill_colour.toString() + DELIMITER + line_thickness + DELIMITER + line_style + DELIMITER +
                                layoutX + DELIMITER + layoutY + ENDL);
                    }
                    else if (s_type.equals("Ellipse")){
                        Ellipse s_circle = (Ellipse) s;
                        double centre_x = s_circle.getCenterX();
                        double centre_y = s_circle.getCenterY();
                        double radius_x = s_circle.getRadiusX();
                        double radius_y = s_circle.getRadiusY();
                        Color line_colour = (Color) s_circle.getStroke();
                        Color fill_colour = (Color) s_circle.getFill();
                        double line_thickness = s_circle.getStrokeWidth();
                        ObservableList line_style = s_circle.getStrokeDashArray();
                        double layoutX = s_circle.getLayoutX();
                        double layoutY = s_circle.getLayoutY();
                        writer.write(s_type + DELIMITER + centre_x +DELIMITER + centre_y + DELIMITER + radius_x + DELIMITER + radius_y + DELIMITER +
                                line_colour.toString() + DELIMITER + fill_colour.toString() + DELIMITER + line_thickness + DELIMITER + line_style + DELIMITER +
                                layoutX + DELIMITER + layoutY + ENDL);

                    }
                    else if (s_type.equals("Line")){
                        Line s_line = (Line) s;
                        double start_x = s_line.getStartX();
                        double start_y = s_line.getStartY();
                        double end_x = s_line.getEndX();
                        double end_y = s_line.getEndY();
                        Color line_colour = (Color) s_line.getStroke();
                        double line_thickness = s_line.getStrokeWidth();
                        ObservableList line_style = s_line.getStrokeDashArray();
                        double layoutX = s_line.getLayoutX();
                        double layoutY = s_line.getLayoutY();
                        writer.write(s_type + DELIMITER + start_x +DELIMITER + start_y + DELIMITER + end_x + DELIMITER + end_y + DELIMITER +
                                line_colour.toString() + DELIMITER + line_thickness + DELIMITER + line_style + DELIMITER + layoutX +
                                DELIMITER + layoutY + ENDL);
                    }
                }
                writer.close();
                file.close();
                model.saved = true;
                return 0; // file saved
            }
            else{
                return 1; // user canceled the save
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    private void load(){
        FileReader file = null;
        BufferedReader reader = null;
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Load File");
            File chosen_file = chooser.showOpenDialog(stage);
            if (chosen_file != null){
                file = new FileReader(chosen_file);
                reader = new BufferedReader(file);

                new_file();
                cur_file = chosen_file;
                String line;
                boolean first_line = true;
                while((line = reader.readLine()) != null){
                    if(first_line){
                        Paint bg_color = Paint.valueOf(line);
                        canvas.canvas.setBackground(new Background(new BackgroundFill(bg_color, null, null)));
                        first_line = false;
                    }
                    else {
                        String[] values = line.split(DELIMITER);
                        if (values[0].equals("Rectangle")) {
                            Rectangle r = new Rectangle();
                            r.setX(Double.parseDouble(values[1]));
                            r.setY(Double.parseDouble(values[2]));
                            r.setWidth(Double.parseDouble(values[3]));
                            r.setHeight(Double.parseDouble(values[4]));
                            r.setStroke(Paint.valueOf(values[5]));
                            r.setFill(Paint.valueOf(values[6]));
                            r.setStrokeWidth(Double.parseDouble(values[7]));
                            if (!values[8].equals("[]")) {
                                int len = values[8].length();
                                r.getStrokeDashArray().add(Double.parseDouble(values[8].substring(1, len-1)));
                            }
                            r.setLayoutX(Double.parseDouble(values[9]));
                            r.setLayoutY(Double.parseDouble(values[10]));
                            model.configureShape(r);
                            canvas.getChildren().add(r);
                        } else if (values[0].equals("Ellipse")) {
                            Ellipse c = new Ellipse();
                            c.setCenterX(Double.parseDouble(values[1]));
                            c.setCenterY(Double.parseDouble(values[2]));
                            c.setRadiusX(Double.parseDouble(values[3]));
                            c.setRadiusY(Double.parseDouble(values[4]));
                            c.setStroke(Paint.valueOf(values[5]));
                            c.setFill(Paint.valueOf(values[6]));
                            c.setStrokeWidth(Double.parseDouble(values[7]));
                            if (!values[8].equals("[]")) {
                                int len = values[8].length();
                                c.getStrokeDashArray().add(Double.parseDouble(values[8].substring(1, len-1)));
                            }
                            c.setLayoutX(Double.parseDouble(values[9]));
                            c.setLayoutY(Double.parseDouble(values[10]));
                            model.configureShape(c);
                            canvas.getChildren().add(c);
                        }
                        else if (values[0].equals("Line")) {
                            Line l = new Line();
                            l.setStartX(Double.parseDouble(values[1]));
                            l.setStartY(Double.parseDouble(values[2]));
                            l.setEndX(Double.parseDouble(values[3]));
                            l.setEndY(Double.parseDouble(values[4]));
                            l.setStroke(Paint.valueOf(values[5]));
                            l.setStrokeWidth(Double.parseDouble(values[6]));
                            if (!values[7].equals("[]")) {
                                int len = values[7].length();
                                l.getStrokeDashArray().add(Double.parseDouble(values[7].substring(1, len - 1)));
                            }
                            l.setLayoutX(Double.parseDouble(values[8]));
                            l.setLayoutY(Double.parseDouble(values[9]));
                            model.configureShape(l);
                            canvas.getChildren().add(l);
                        }
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    private void new_file(){
        cur_file = null;
        model.newFile();
        canvas.newCanvas();
    }

    private void quit(){
        stage.close();
    }

    private void registerController(){
        new_file.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int result = -1;
                if(model.saved == false){
                    result = promptSave();
                }
                if(result == -1 ){ // no changes were made
                    new_file();
                }
                else if (result == 0){ // user needs to save the file
                    int saved = save();
                    if(saved == 0){ // user successfully saved the file so we can make a new file
                        new_file();
                    }
                    else{
                        return; // user didnt successfully save the file so don't make new file
                    }
                }
                else if (result == 1){ // user didn't save the current file so make new file
                    new_file();
                }
                else if (result == 2){ // user clicked cancel so dont create new file
                    return;
                }

            }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                save();
            }
        });

        load_file.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int result = -1;
                if(model.saved == false){
                    result = promptSave();
                }
                if(result == -1 ){ // no changes were made
                    load();
                }
                else if (result == 0){ // user needs to save the file
                    int saved = save();
                    if(saved == 0){ // user successfully saved the file so we can make a new file
                        load();
                    }
                    else{
                        return; // user didnt successfully save the file so don't make new file
                    }
                }
                else if (result == 1){ // user didn't save the current file so make new file
                    load();
                }
                else if (result == 2){ // user clicked cancel so dont create new file
                    return;
                }
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int result = -1;
                if(model.saved == false){
                    result = promptSave();
                }
                if(result == -1 ){ // no changes were made
                    quit();
                }
                else if (result == 0){ // user needs to save the file
                    int saved = save();
                    if(saved == 0){ // user successfully saved the file so we can make a new file
                        quit();
                    }
                    else{
                        return; // user didnt successfully save the file so don't make new file
                    }
                }
                else if (result == 1){ // user didn't save the current file so make new file
                    quit();
                }
                else if (result == 2){ // user clicked cancel so dont create new file
                    return;
                }
            }
        });

        copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(model.selected_shape != null){
                    model.copied_shape = model.copyShape(model.selected_shape);
                }

            }
        });

        paste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                model.pasteShape();
            }
        });

        cut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (model.selected_shape != null){
                    model.cutShape(model.selected_shape);
                }

            }
        });

        desc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showAboutScreen();
            }
        });
    }

    private int promptSave(){
        // if promptSave returns 0 then user clicked save
        // if promptSave returns 1 then user clicked don't save
        // if promptSave returns 2 then user clicked cancel

        save_prompt = new Dialog();
        if(cur_file != null){
            save_prompt.setContentText("Do you want to save the changes you made to " + cur_file.getName() + "?");
        }
        else{
            save_prompt.setContentText("Do you want to save this file?");
        }
        save_prompt.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        ButtonType save = new ButtonType("Save", ButtonData.OK_DONE);
        ButtonType no_save = new ButtonType("Don't Save", ButtonData.NO);
        save_prompt.getDialogPane().getButtonTypes().addAll(no_save);
        save_prompt.getDialogPane().getButtonTypes().addAll(save);
        Optional<ButtonType> result = save_prompt.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonData.OK_DONE){
            return 0;
        }
        else if (result.isPresent() && result.get().getButtonData() == ButtonData.NO){
            return 1;
        }else if (result.isPresent() && result.get() == ButtonType.CANCEL){
            return 2;
        }
        else{
            return -1; // SHOULD NEVER GET HERE
        }
    }

    private void showAboutScreen(){
        about_screen = new Dialog();
        about_screen.setContentText("SketchIt!\nName: Brandon Nguyen\nStudent ID: 2073309");
        about_screen.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        about_screen.showAndWait();

    }

    @Override
    public void updateView() {
        return;
    }
}
