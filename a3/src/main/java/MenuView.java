import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MenuView extends HBox implements IView {
    Model model;
    double width;
    double height;
    Menu file;
    Menu about;
    MenuItem save;
    MenuItem new_file;
    MenuItem load_file;
    MenuItem quit;
    MenuItem desc;
    FileChooser filechooser;
    Stage stage;
    Pane canvas;


    public MenuView(Model model, double screen_width, double screen_height, Stage stage, Pane canvas){
        this.model = model;
        this.width = screen_width;
        this.height = screen_height * 0.02;
        this.stage = stage;
        this.canvas = canvas;
        setStyle("-fx-background-color:beige;");
        setPrefSize(width,height);

        file = new Menu("File");
        new_file = new MenuItem("New File");
        save = new MenuItem("Save");
        load_file = new MenuItem("Load file");
        quit = new MenuItem("Quit");
        file.getItems().addAll(new_file,  load_file, save, quit);

        about = new Menu("About");
        desc = new MenuItem("Name: Brandon Nguyen\nStudent ID: 2073309");
        about.getItems().add(desc);

        filechooser = new FileChooser();
        filechooser.setTitle("Save");
        //filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

        MenuBar menubar = new MenuBar(file, about);
        
        getChildren().addAll(menubar);

        //setHgrow(file, Priority.ALWAYS);
        //setHgrow(edit, Priority.ALWAYS);
        setSpacing(30);
        this.model.addView(this);
        registerController();

    }

    public void save(){

    }

    public void load(){
        return;
    }

    public void quit(){
        return;
    }

    private void registerController(){
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //filechooser.showSaveDialog(stage);
                WritableImage img = canvas.snapshot(new SnapshotParameters(), null);
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                // File fileToSave = chooser.getSelectedFile();//Remove this line.
                //BufferedImage img2 = SwingFXUtils.fromFXImage(img, null);
                int result = chooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fileToSave = chooser.getSelectedFile();
                        ImageIO.write((RenderedImage) img,"png", fileToSave);
                    } catch (IOException ex) {
                        //Logger.getLogger(GuiClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }


    @Override
    public void updateView() {
        return;
    }
}
