import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class Timer extends HBox {
    int count;
    int time_left;
    String time_str;
    Label clock;
    Label img;
    Timeline animation;



    public Timer(){
        count = 30;
        time_left = count;
        time_str = Integer.toString(count);
        clock = new Label(time_str);
        clock.setFont(javafx.scene.text.Font.font(25));
        clock.setPrefSize(40,40);
        img = new Label();
        Image h = new Image("file:src/main/java/img/timer.png");
        ImageView hourglass = new ImageView(h);
        img.setGraphic(hourglass);
        img.setPadding(new Insets(2.5, 0,0,0));
        getChildren().addAll(img, clock);
        setSpacing(15);

    }

    public void start(){
        animation = new Timeline(new KeyFrame(Duration.seconds(1), e-> countdown()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }


    public void stop(){
        animation.stop();
    }

    public void resume(){
        animation.play();
    }


    public void countdown(){
        if (time_left > 0){
            time_left--;
        }
        //System.out.println("TIME LEFT: " + time_left);
        time_str = Integer.toString(time_left);
        clock.setText(time_str);

    }

}
