import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class Menu extends HBox implements IView {
    Model model;
    double width;
    double height;

    public Menu(Model model, double screen_width, double screen_height){
        this.model = model;
        this.width = screen_width;
        this.height = screen_height * 0.02;
        setStyle("-fx-background-color:white;");
        setPrefSize(width,height);
        Label file = new Label("file");
        Label edit = new Label("edit");
        getChildren().addAll(file, edit);
        setHgrow(file, Priority.ALWAYS);
        setHgrow(edit, Priority.ALWAYS);
        setSpacing(30);
        this.model.addView(this);

    }

    public void save(){
        return;
    }

    public void load(){
        return;
    }

    public void quit(){
        return;
    }


    @Override
    public void updateView() {
        return;
    }
}
