import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



public class test extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane bp = new BorderPane();
        Canvas canvas = new Canvas(800,800);
        bp.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.beginPath();
        //gc.setFill(Color.TRANSPARENT);
        //gc.setStroke(Color.BLUE);
        double x = 400;
        double y = 400;
        double w = 300;
        double h = -200;
        gc.fillRect(x,y + h, w, -h);

        //gc.setFill(Color.RED);
        gc.fill();
        gc.closePath();
//        Rectangle r = new Rectangle(400, 400, -300, -200);
//        r.setFill(Color.TRANSPARENT);
//        r.setStroke(Color.BLACK);
//        bp.getChildren().add(r);

        Scene s = new Scene(bp, 800, 800);
        stage.setScene(s);
        stage.show();

    }
}
