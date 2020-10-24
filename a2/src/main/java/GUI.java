import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;


public class GUI extends Application {
    Stage stage;
    Scene curScene;
    Scene splashScreen;
    Scene playScreen1;
    Scene playScreen2;
    Scene playScreen3;
    Scene deathScreen;
    Timeline timeline;
    double FPS;
    double block; // blocksize
    int screen_width;
    int screen_height;
    int board_width;
    int board_height;
    int panel_width;
    int panel_height;
    int level;
    boolean isRunning;
    Label paused;
    Label score_label;
    Label high_score_label;
    Label hs1;
    Label hs2;
    Label hs3;


    public GUI(){
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        paused = new Label();
        Image p = new Image("file:src/main/java/img/pause.png");
        ImageView pause_img = new ImageView(p);
        paused.setGraphic(pause_img);
        paused.setTranslateX(340);
        paused.setTranslateY(80);

        score_label = new Label();
        high_score_label = new Label();
        score_label.setFont(new Font("Arial", 50));
        high_score_label.setFont(new Font("Arial", 50));

        block = 40;
        isRunning = false;
        screen_width = 1280;
        screen_height = 800;
        panel_width = 1280;
        panel_height = 40;
        board_width = 1280;
        board_height = screen_height - panel_height;

        splashScreen = createSplashScreen();
        playScreen1 = createPlayScreen1();
        playScreen2 = createPlayScreen2();
        playScreen3 = createPlayScreen3();
        deathScreen = createDeathScreen();
        curScene = splashScreen;
    }


    public void start(Stage stage){
        this.stage = stage;
        stage.setTitle("Snake");

        showSplashScreen();
        stage.show();

    }


    private Scene createSplashScreen(){
        HBox title_hbox = new HBox();
        Label s = new Label();
        s.setGraphic(new ImageView(new Image("file:src/main/java/img/S.png")));
        Label n = new Label();
        n.setGraphic(new ImageView(new Image("file:src/main/java/img/N.png")));
        Label a = new Label();
        a.setGraphic(new ImageView(new Image("file:src/main/java/img/A.png")));
        Label k = new Label();
        k.setGraphic(new ImageView(new Image("file:src/main/java/img/K.png")));
        Label e = new Label();
        e.setGraphic(new ImageView(new Image("file:src/main/java/img/E.png")));
        title_hbox.getChildren().addAll(s,n,a,k,e);
        title_hbox.setAlignment(Pos.TOP_CENTER);
        title_hbox.setSpacing(10);
        title_hbox.setPadding(new Insets(10,0,0,380));

        Label student_desc = new Label();
        student_desc.setGraphic(new ImageView(new Image("file:src/main/java/img/student_desc.png")));
        student_desc.setPadding(new Insets(0,0,0,100));
        HBox description_hbox = new HBox(title_hbox,student_desc);
        description_hbox.setAlignment(Pos.TOP_CENTER);

        Label chooseLevel = new Label("Choose level:");
        chooseLevel.setFont(new Font("Arial", 60));
        chooseLevel.setTextFill(Color.WHITE);
        Label level1 = new Label("Level 1: Press 1");
        level1.setFont(new Font("Arial", 35));
        level1.setTextFill(Color.WHITE);
        Label level2 = new Label("Level 2: Press 2");
        level2.setFont(new Font("Arial", 35));
        level2.setTextFill(Color.WHITE);
        Label level3 = new Label("Level 3: Press 3");
        level3.setFont(new Font("Arial", 35));
        level3.setTextFill(Color.WHITE);
        VBox level_vbox = new VBox(chooseLevel, level1, level2, level3);
        level_vbox.setAlignment(Pos.CENTER_LEFT);
        level_vbox.setSpacing(50);
        level_vbox.setPadding(new Insets(0,0,0,15));

        VBox hs_vbox = new VBox();
        Label hs = new Label("High Scores:");
        hs.setFont(new Font("Arial", 60));
        hs.setTextFill(Color.WHITE);
        hs1 = new Label("Level 1 High Score: 0");
        hs1.setFont(new Font("Arial", 35));
        hs1.setTextFill(Color.WHITE);
        hs2 = new Label("Level 2 High Score: 0");
        hs2.setFont(new Font("Arial", 35));
        hs2.setTextFill(Color.WHITE);
        hs3 = new Label("Level 3 High Score: 0");
        hs3.setFont(new Font("Arial", 35));
        hs3.setTextFill(Color.WHITE);
        hs_vbox.getChildren().addAll(hs, hs1,hs2,hs3);
        hs_vbox.setAlignment(Pos.CENTER_RIGHT);
        hs_vbox.setSpacing(50);
        hs_vbox.setPadding(new Insets(0,15,0,0));

        HBox level_and_hs = new HBox();
        level_and_hs.getChildren().addAll(level_vbox, hs_vbox);
        level_and_hs.setSpacing(500);
        //level_and_hs.setPrefWidth(1280);

        VBox main_vbox = new VBox();

        BackgroundSize backgroundSize = new BackgroundSize(1280,
                800,
                true,
                true,
                true,
                true);
        BackgroundImage bg = new BackgroundImage(new Image("file:src/main/java/img/splash_bg.jpg"),
                BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,backgroundSize);
        main_vbox.setBackground(new Background(bg));



        main_vbox.setSpacing(80);
        main_vbox.getChildren().add(description_hbox);
        main_vbox.getChildren().add(level_and_hs);
        main_vbox.setAlignment(Pos.TOP_CENTER);

        Scene splashScreen = new Scene(main_vbox, screen_width, screen_height);
        return splashScreen;
    }

    private Scene createPlayScreen1() {
        Pane screen = new Pane();
        /*
        screen.setStyle("-fx-background-color: green;");
        screen.setPrefSize(screen_width, screen_height);
        for (int r = 1; r < 20; r++) {
            for (int c = 0; c < 32; c++) {
                Rectangle rec;
                if (r % 2 == 0) {
                    if (c % 2 == 0) {
                        rec = new Rectangle(c * 40, r * 40.0, 40, 40);
                        rec.setFill(Color.DARKGREEN);
                        screen.getChildren().add(rec);
                    }
                } else {
                    if (c % 2 != 0) {
                        rec = new Rectangle(c * 40, r * 40.0, 40, 40);
                        rec.setFill(Color.DARKGREEN);
                        screen.getChildren().add(rec);
                    }
                }

            }
        }*/

        Image bg = new Image("file:src/main/java/img/game_bg1.jpg");
        ImageView game_bg = new ImageView(bg);
        game_bg.setFitWidth(1280);
        game_bg.setFitHeight(800);
        screen.getChildren().add(game_bg);



//        BackgroundSize backgroundSize = new BackgroundSize(1280,
//                800,
//                true,
//                true,
//                true,
//                true);
//        BackgroundImage bg = new BackgroundImage(new Image("file:src/main/java/img/game_bg.jpg"),
//                BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,backgroundSize);
//        Background space = new Background(bg);
//        screen.setBackground(space);
        Scene playScreen = new Scene(screen);
        return playScreen;
    }

    private Scene createPlayScreen2(){
        Pane screen = new Pane();
        /*
        screen.setStyle("-fx-background-color: green;");
        screen.setPrefSize(screen_width, screen_height);
        for (int r = 1; r < 20; r++) {
            for (int c = 0; c < 32; c++) {
                Rectangle rec;
                if (r % 2 == 0) {
                    if (c % 2 == 0) {
                        rec = new Rectangle(c * 40, r * 40.0, 40, 40);
                        rec.setFill(Color.DARKOLIVEGREEN);
                        screen.getChildren().add(rec);
                    }
                } else {
                    if (c % 2 != 0) {
                        rec = new Rectangle(c * 40, r * 40.0, 40, 40);
                        rec.setFill(Color.DARKOLIVEGREEN);
                        screen.getChildren().add(rec);
                    }
                }

            }
        }*/
        Image bg = new Image("file:src/main/java/img/game_bg2.jpg");
        ImageView game_bg = new ImageView(bg);
        game_bg.setFitWidth(1280);
        game_bg.setFitHeight(800);
        screen.getChildren().add(game_bg);
        Scene playScreen = new Scene(screen);
        return playScreen;
    }

    private Scene createPlayScreen3(){
        Pane screen = new Pane();
        /*
        screen.setStyle("-fx-background-color: green;");
        screen.setPrefSize(screen_width, screen_height);
        for (int r = 1; r < 20; r++) {
            for (int c = 0; c < 32; c++) {
                Rectangle rec;
                if (r % 2 == 0) {
                    if (c % 2 == 0) {
                        rec = new Rectangle(c * 40, r * 40.0, 40, 40);
                        rec.setFill(Color.DARKTURQUOISE);
                        screen.getChildren().add(rec);
                    }
                } else {
                    if (c % 2 != 0) {
                        rec = new Rectangle(c * 40, r * 40.0, 40, 40);
                        rec.setFill(Color.DARKTURQUOISE);
                        screen.getChildren().add(rec);
                    }
                }

            }
        }*/
        Image bg = new Image("file:src/main/java/img/game_bg3.jpg");
        ImageView game_bg = new ImageView(bg);
        game_bg.setFitWidth(1280);
        game_bg.setFitHeight(800);
        screen.getChildren().add(game_bg);
        Scene playScreen = new Scene(screen);
        return playScreen;
    }


    private Scene createDeathScreen(){
        VBox vbox = new VBox();
        vbox.setPrefSize(screen_width,screen_height);
//        Label death = new Label("Game Over");
//        death.setTextFill(Color.WHITE);
//        death.setAlignment(Pos.CENTER);
//        death.setFont(new Font("Arial", 100));
//        death.setPadding(new Insets(60, 0, 0, 0));

        BackgroundSize backgroundSize = new BackgroundSize(1280,
                800,
                true,
                true,
                true,
                true);
        BackgroundImage bg = new BackgroundImage(new Image("file:src/main/java/img/death_bg.jpg"),
                BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,backgroundSize);
        vbox.setBackground(new Background(bg));

        HBox gameover = new HBox();
        HBox game = new HBox();
        HBox over = new HBox();
        Label g = new Label();
        Label a = new Label();
        Label m = new Label();
        Label e = new Label();
        Label o = new Label();
        Label v = new Label();
        Label e2 = new Label();
        Label r = new Label();
        g.setGraphic(new ImageView(new Image("file:src/main/java/img/G_ds.png")));
        a.setGraphic(new ImageView(new Image("file:src/main/java/img/A_ds.png")));
        m.setGraphic(new ImageView(new Image("file:src/main/java/img/M_ds.png")));
        e.setGraphic(new ImageView(new Image("file:src/main/java/img/E_ds.png")));
        o.setGraphic(new ImageView(new Image("file:src/main/java/img/O_ds.png")));
        v.setGraphic(new ImageView(new Image("file:src/main/java/img/V_ds.png")));
        e2.setGraphic(new ImageView(new Image("file:src/main/java/img/E_ds.png")));
        r.setGraphic(new ImageView(new Image("file:src/main/java/img/R_ds.png")));
        game.getChildren().addAll(g,a,m,e);
        game.setSpacing(5);
        over.getChildren().addAll(o,v,e2,r);
        over.setSpacing(5);
        gameover.getChildren().addAll(game,over);
        gameover.setSpacing(40);
        gameover.setAlignment(Pos.TOP_CENTER);
        gameover.setPadding(new Insets(50,0,0,0));

        HBox scores = new HBox();
        scores.getChildren().addAll(score_label, high_score_label);
        scores.setAlignment(Pos.CENTER);
        scores.setSpacing(60);
        //scores.setPadding(new Insets(0,0,0,0));
        scores.setStyle("-fx-background-color: black;");
        //scores.setPrefWidth(600);

        VBox options = new VBox();
        Label playAgain = new Label("Play Again: Press P");
        playAgain.setFont(new Font("Arial", 35));
        playAgain.setTextFill(Color.WHITE);
        Label returnSplash = new Label("Return to Splash Screen: Press R");
        returnSplash.setFont(new Font("Arial", 35));
        returnSplash.setTextFill(Color.WHITE);
        Label quit = new Label("Quit: Press Q");
        quit.setFont(new Font("Arial", 35));
        quit.setTextFill(Color.WHITE);
        options.getChildren().addAll(playAgain,returnSplash,quit);
        options.setSpacing(40);
        options.setAlignment(Pos.CENTER);
        options.setPadding(new Insets(60, 0, 0, 0));

        vbox.getChildren().addAll(gameover, scores, options);
        vbox.setSpacing(110);

        Scene deathScreen = new Scene(vbox);
        vbox.setAlignment(Pos.TOP_CENTER);
        return deathScreen;
    }


    public void showSplashScreen(){
        curScene = splashScreen;
        curScene.setOnKeyPressed(ke -> {
            KeyCode keycode = ke.getCode();
            if (keycode.equals(KeyCode.DIGIT1)) {
                FPS = 150;
                level = 1;
                playGame();
            }
            else if (keycode.equals(KeyCode.DIGIT2)){
                FPS = 110;
                level = 2;
                playGame();
            }
            else if (keycode.equals(KeyCode.DIGIT3)){
                FPS = 80;
                level = 3;
                playGame();
            }
        });
        stage.setScene(curScene);

    }

    public void showDeathScreen(int score, int hs1, int hs2, int hs3){
        curScene = deathScreen;
        VBox root = (VBox) curScene.getRoot();
        HBox hbox = new HBox();
        score_label.setText("Score: " + score);
        score_label.setTextFill(Color.LIGHTGOLDENRODYELLOW);
        if (level == 1){
            high_score_label.setText("High Score: " + hs1);
        }
        else if (level == 2){
            high_score_label.setText("High Score: " + hs2);
        }
        else if (level == 3){
            high_score_label.setText("High Score: " + hs3);
        }
        high_score_label.setTextFill(Color.LIGHTGOLDENRODYELLOW);

        curScene.setOnKeyPressed(ke -> {
            KeyCode keycode = ke.getCode();
            if (keycode.equals(KeyCode.P)) {
                playGame();
            }
            else if (keycode.equals(KeyCode.R)){
                showSplashScreen();
            }
            else if (keycode.equals(KeyCode.Q)){
                showQuitScreen(hs1, hs2, hs3);
            }
        });
        stage.setScene(curScene);
    }

    public void showQuitScreen(int hs1, int hs2, int hs3){
        VBox vbox = new VBox();

        BackgroundSize backgroundSize = new BackgroundSize(1280,
                800,
                true,
                true,
                true,
                true);
        BackgroundImage bg = new BackgroundImage(new Image("file:src/main/java/img/death_bg.jpg"),
                BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,backgroundSize);
        vbox.setBackground(new Background(bg));

        vbox.setPrefSize(board_width, board_height);
        //vbox.setStyle("-fx-background-color: yellow;");
        Label thanks = new Label("Thanks for Playing!");
        thanks.setFont(javafx.scene.text.Font.font(100));
        thanks.setTextFill(Color.LIGHTBLUE);
        VBox scores = new VBox();
        Label high1 = new Label("Level 1 High Score: " + hs1);
        high1.setFont(javafx.scene.text.Font.font(70));
        high1.setTextFill(Color.LIGHTBLUE);
        Label high2 = new Label("Level 2 High Score: " + hs2);
        high2.setFont(javafx.scene.text.Font.font(70));
        high2.setTextFill(Color.LIGHTBLUE);
        Label high3 = new Label("Level 3 High Score: " + hs3);
        high3.setFont(javafx.scene.text.Font.font(70));
        high3.setTextFill(Color.LIGHTBLUE);
        scores.getChildren().addAll(high1, high2, high3);
        scores.setAlignment(Pos.CENTER);
        scores.setSpacing(15);
        vbox.getChildren().addAll(thanks,scores);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(50);


        Scene screen = new Scene(vbox);
        curScene = screen;
        stage.setScene(curScene);


    }

    public void showPauseScreen(){
        Pane root = (Pane) curScene.getRoot();
        root.getChildren().add(paused);
    }

    public void unpause(){
        Pane root = (Pane) curScene.getRoot();
        root.getChildren().remove(paused);
    }

    public void playGame(){
        if (level == 1){
            curScene = playScreen1;
        }
        else if (level == 2){
            curScene = playScreen2;
        }
        else{
            curScene = playScreen3;
        }
        stage.setScene(curScene);
        isRunning = true;
        Board board = new Board(this);
        timeline.play();
    }
}
