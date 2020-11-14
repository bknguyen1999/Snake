import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SketchIt extends Application {

    double screen_width;
    double screen_height;

    @Override
    public void start(Stage stage) throws Exception{
        Rectangle2D screen_bounds = Screen.getPrimary().getBounds();
        screen_width = screen_bounds.getWidth();
        screen_height = screen_bounds.getHeight();
        Model model = new Model();
        GridPane gp = new GridPane();
        ToolbarView tools = new ToolbarView(model, screen_width, screen_height);
        PropertiesView properties = new PropertiesView(model, screen_width, screen_height);
        CanvasView canvas = new CanvasView(model, screen_width, screen_height);
        MenuView menu = new MenuView(model, screen_width, screen_height, stage, canvas);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(tools, properties);

        gp.add(vbox, 0, 0);
        gp.add(canvas, 1, 0);

        VBox main = new VBox();
        main.getChildren().addAll(menu, gp);

        gp.setHgrow(vbox, Priority.ALWAYS);
        gp.setVgrow(vbox, Priority.ALWAYS);
        gp.setHgrow(canvas, Priority.ALWAYS);
        gp.setVgrow(canvas, Priority.ALWAYS);

        gp.setHgrow(menu, Priority.ALWAYS);
        gp.setVgrow(menu, Priority.ALWAYS);

        stage.setMinWidth(640);
        stage.setMinHeight(700);


        Scene scene = new Scene(main, screen_width, screen_height);
        model.setScene(scene);

        stage.setScene(scene);
        stage.show();









    }
}
