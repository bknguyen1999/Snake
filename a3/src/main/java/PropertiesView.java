import javafx.scene.layout.GridPane;

public class PropertiesView extends GridPane implements IView {
    String bg_color = "-fx-background-color:blue;";
    double default_width;
    double default_height;
    Model model;


    public PropertiesView(Model model, double screen_width, double screen_height){
        this.model = model;
        this.default_width = screen_width * 0.2;
        this.default_height = screen_height * 0.39;
        setStyle(bg_color);
        setPrefSize(this.default_width, this.default_height);
        this.model.addView(this);

    }

    @Override
    public void updateView() {
        //System.out.println("HI");
    }
}
