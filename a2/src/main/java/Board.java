import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

public class Board {
    GUI gui;
    Pane pane;
    Snake snake;
    Fruit fruits;
    int level;
    ArrayList<Rectangle> fruit_list;
    Timeline timeline;
    Timer timer;
    int cur_score;
    static int high_score1 = 0;
    static int high_score2 = 0;
    static int high_score3 = 0;
    Label score_label;
    Label high_score_label;
    Label score_img;
    Label high_score_img;
    Label level_img;
    AudioClip eat_fruit_sound;
    AudioClip death_sound;


    public Board(GUI gui){
        try {
            this.gui = gui;
            this.timeline = gui.timeline;
            this.level = gui.level;
            this.cur_score = 0;
            score_label = new Label();
            score_label.setFont(javafx.scene.text.Font.font(25));
            score_label.setPrefSize(40,40);
            high_score_label = new Label();
            high_score_label.setFont(javafx.scene.text.Font.font(25));
            high_score_label.setPrefSize(40,40);
            score_img = new Label();
            Image s = new Image("file:src/main/java/img/banana_score.png");
            ImageView s_img = new ImageView(s);
            score_img.setGraphic(s_img);
            score_img.setPadding(new Insets(2.5,0,0,0));
            high_score_img = new Label();
            Image h = new Image("file:src/main/java/img/highscore.png");
            ImageView hs_img = new ImageView(h);
            high_score_img.setGraphic(hs_img);
            high_score_img.setPadding(new Insets(2.5,0,0,0));
            level_img = new Label();

            eat_fruit_sound = new AudioClip(Paths.get("src/main/java/audio/eat_fruit.mp3").toUri().toString());
            eat_fruit_sound.setVolume(0.3);

            death_sound = new AudioClip(Paths.get("src/main/java/audio/death.mp3").toUri().toString());
            death_sound.setVolume(0.3);

            this.pane = (Pane) gui.curScene.getRoot();
            setup_timer_score();
            fruits = new Fruit(gui);
            snake = new Snake(gui, this);
            fruit_list = fruits.fruits_on_board;
            if (timer != null) {
                timer.start();
            }

            keyboard_input();

            KeyFrame frame = new KeyFrame(Duration.millis(gui.FPS), event -> {
                if (snake.snake_array.size() == 0) {
                    return;
                }
                if (timer != null && timer.time_left == 0 && this.level != 3) {
                    nextLevel();
                    return;
                }
                // hit detection for fruit
                Rectangle fruit_eaten = fruit_hit();
                if (fruit_eaten != null) {
                    eat_fruit(fruit_eaten);
                }
                if (snake_OOB() || !snake.moving) {
                    gameOver();
                }
            });
            if (gui.isRunning) {
                timeline.getKeyFrames().add(frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setup_timer_score(){
        HBox HUD = new HBox();
        score_label.setText(Integer.toString(cur_score));
        HBox score_box = new HBox();
        score_box.getChildren().addAll(score_img, score_label);
        score_box.setSpacing(15);

        HBox hs_box = new HBox();
        hs_box.getChildren().addAll(high_score_img, high_score_label);
        hs_box.setSpacing(15);

        HBox level_hbox = new HBox();
        Label L = new Label();
        Label e = new Label();
        Label v = new Label();
        Label e2 = new Label();
        Label l = new Label();
        Label level_num =  new Label();
        L.setGraphic(new ImageView(new Image("file:src/main/java/img/L_big.png")));
        e.setGraphic(new ImageView(new Image("file:src/main/java/img/E_small.png")));
        v.setGraphic(new ImageView(new Image("file:src/main/java/img/V.png")));
        e2.setGraphic(new ImageView(new Image("file:src/main/java/img/E_small.png")));
        l.setGraphic(new ImageView(new Image("file:src/main/java/img/L_small.png")));

        if (level == 1){
            high_score_label.setText(Integer.toString(high_score1));
//            Image l = new Image("file:src/main/java/img/level1.png");
//            ImageView l_view = new ImageView(l);
//            level_img.setGraphic(l_view);
//            level_img.setPadding(new Insets(0,150,0,0));
            level_num.setGraphic(new ImageView(new Image("file:src/main/java/img/1.png")));
            hs_box.setPadding(new Insets(0, 350, 0,0));
            level_hbox.setPadding(new Insets(2.5,180,0,0));
        }
        else if (level == 2){
            high_score_label.setText(Integer.toString(high_score2));
//            Image l = new Image("file:src/main/java/img/level2.png");
//            ImageView l_view = new ImageView(l);
//            level_img.setGraphic(l_view);
//            level_img.setPadding(new Insets(0,150,0,0));
            level_num.setGraphic(new ImageView(new Image("file:src/main/java/img/2.png")));
            hs_box.setPadding(new Insets(0, 350, 0,0));
            level_hbox.setPadding(new Insets(2.5,180,0,0));
        }
        else{
            high_score_label.setText(Integer.toString(high_score3));
//            Image l = new Image("file:src/main/java/img/level3.png");
//            ImageView l_view = new ImageView(l);
//            level_img.setGraphic(l_view);
//            level_img.setPadding(new Insets(0,300,0,0));
            level_num.setGraphic(new ImageView(new Image("file:src/main/java/img/3.png")));
            hs_box.setPadding(new Insets(0, 300, 0,0));
            level_hbox.setPadding(new Insets(2.5,250,0,0));
        }
        level_hbox.getChildren().addAll(L,e,v,e2,l,level_num);
        level_hbox.setSpacing(3);
        level_num.setPadding(new Insets(0,0,0,20));

        if (level != 3){
            this.timer = new Timer();
            HUD.getChildren().addAll(hs_box, level_hbox, score_box, this.timer);
        }
        else{
            this.timer = null;
            HUD.getChildren().addAll(hs_box, level_hbox,score_box);
        }
        HUD.setPrefHeight(gui.panel_height);
        HUD.setPrefWidth(gui.panel_width);
        HUD.setAlignment(Pos.CENTER);
        HUD.setSpacing(70);
        HUD.setPadding(new Insets(0, 40, 0, 0));
        BackgroundSize backgroundSize = new BackgroundSize(1280,
                40,
                true,
                true,
                true,
                false);
        BackgroundImage bg = new BackgroundImage(new Image("file:src/main/java/img/score_timer_bg.jpg"),
                                                    BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,backgroundSize);
        HUD.setBackground(new Background(bg));
        pane.getChildren().add(HUD);


    }



    private Rectangle fruit_hit(){
        Rectangle snake_head = snake.snake_array.get(0);
        for (int i = 0 ; i < fruit_list.size(); i++){
            Rectangle f = fruit_list.get(i);
            if (snake_head.getTranslateX() == f.getTranslateX() && snake_head.getTranslateY() == f.getTranslateY()){
                return f;
            }
        }
        return null;
    }

    private void eat_fruit(Rectangle fruit_eaten){
        eat_fruit_sound.play();
        cur_score++;
        score_label.setText(Integer.toString(cur_score));
        fruits.moveFruit(fruit_eaten);
        snake.grow();
    }

    private boolean snake_OOB(){
        Rectangle snake_head = snake.snake_array.get(0);
        if (snake_head.getTranslateX() < 0 || snake_head.getTranslateX() >= gui.screen_width
            || snake_head.getTranslateY() < 40 || snake_head.getTranslateY() >= gui.screen_height){
            return true;
        }
        return false;
    }

    public void stopGame(){
        gui.isRunning = false;
        snake.moving = false;
        gui.timeline.stop();
        snake.snake_array.clear();
        snake.snake_container.getChildren().clear();
        for (int i = 0; i < fruits.fruits_on_board.size(); i++){
            pane.getChildren().remove(fruits.fruits_on_board.get(i));
        }
        fruits.fruits_on_board.clear();
        if (timer != null){
            timer.stop();
        }
    }

    public void updateHighScore(){
        if (cur_score > high_score1 && level == 1){
            high_score1 = cur_score;
        }
        else if (cur_score > high_score2 && level == 2){
            high_score2 = cur_score;
        }
        else if (cur_score > high_score3 && level == 3){
            high_score3 = cur_score;
        }
        gui.hs1.setText("Level 1 High Score: " + high_score1);
        gui.hs2.setText("Level 2 High Score: " + high_score2);
        gui.hs3.setText("Level 3 High Score: " + high_score3);
    }

    public void gameOver(){
        stopGame();
        updateHighScore();
        death_sound.play();
        gui.showDeathScreen(cur_score, high_score1, high_score2, high_score3);
    }

    public void nextLevel(){
        stopGame();
        updateHighScore();
        gui.level++;
        gui.playGame();
    }

    public void returnToSplash(){
        stopGame();
        gui.showSplashScreen();
    }

    private void keyboard_input() {
        gui.curScene.setOnKeyPressed(ke -> {
            KeyCode keycode = ke.getCode();
            if (keycode.equals(KeyCode.LEFT)) {
                if (snake.direction == "E") {
                    snake.direction = "N";
                } else if (snake.direction == "N") {
                    snake.direction = "W";
                } else if (snake.direction == "W") {
                    snake.direction = "S";
                } else if (snake.direction == "S") {
                    snake.direction = "E";
                }
            }
            else if (keycode.equals(KeyCode.RIGHT)) {
                if (snake.direction == "E") {
                    snake.direction = "S";
                } else if (snake.direction == "N") {
                    snake.direction = "E";
                } else if (snake.direction == "W") {
                    snake.direction = "N";
                } else if (snake.direction == "S") {
                    snake.direction = "W";
                }
            }
            else if (keycode.equals(KeyCode.R)){
                returnToSplash();
            }
            else if (keycode.equals(KeyCode.P)){
                if(gui.isRunning){
                    gui.isRunning = false;
                    if(level != 3){
                        timer.stop();
                    }
                    timeline.pause();
                    gui.showPauseScreen();
                }
                else{
                    gui.isRunning = true;
                    if (level != 3){
                        timer.resume();
                    }
                    timeline.play();
                    gui.unpause();
                }
            }

        });
    }





}
