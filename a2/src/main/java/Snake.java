import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;



public class Snake {
    ArrayList<SnakeBlock> snake_array; // snake[0] will be the head of the snake
    Group snake_container;
    String direction = "E";
    Scene curScene;
    GUI gui;
    Timeline timeline;
    boolean moving = true;
    ImagePattern snakehead_E;
    ImagePattern snakehead_N;
    ImagePattern snakehead_W;
    ImagePattern snakehead_S;
    ImagePattern snakebody;
    ImagePattern snaketail_N;
    ImagePattern snaketail_E;
    ImagePattern snaketail_S;
    ImagePattern snaketail_W;
    ImagePattern snaketurn_NE;
    ImagePattern snaketurn_NW;
    ImagePattern snaketurn_EN;
    ImagePattern snaketurn_ES;
    ImagePattern snaketurn_WN;
    ImagePattern snaketurn_WS;
    ImagePattern snaketurn_SE;
    ImagePattern snaketurn_SW;




    public Snake(GUI gui, Board board) {
        this.gui = gui;
        this.curScene = gui.curScene;
        this.timeline = gui.timeline;
        snake_container = new Group();
        double init_head_x = 80;
        double init_head_y = 40 * 10;
        Image snakehead_img_E = new Image("file:src/main/java/img/snakehead_E.png");
        Image snakehead_img_N = new Image("file:src/main/java/img/snakehead_N.png");
        Image snakehead_img_W = new Image("file:src/main/java/img/snakehead_W.png");
        Image snakehead_img_S = new Image("file:src/main/java/img/snakehead_S.png");
        Image snakebody_img = new Image("file:src/main/java/img/snakebody.png");
        Image snaketail_img_N = new Image("file:src/main/java/img/snaketail_N.png");
        Image snaketail_img_E = new Image("file:src/main/java/img/snaketail_E.png");
        Image snaketail_img_S = new Image("file:src/main/java/img/snaketail_S.png");
        Image snaketail_img_W = new Image("file:src/main/java/img/snaketail_W.png");


        snakehead_E = new ImagePattern(snakehead_img_E);
        snakehead_N = new ImagePattern(snakehead_img_N);
        snakehead_W = new ImagePattern(snakehead_img_W);
        snakehead_S = new ImagePattern(snakehead_img_S);
        snakebody = new ImagePattern(snakebody_img);
        snaketail_N = new ImagePattern(snaketail_img_N);
        snaketail_E = new ImagePattern(snaketail_img_E);
        snaketail_S = new ImagePattern(snaketail_img_S);
        snaketail_W = new ImagePattern(snaketail_img_W);

        snaketurn_EN = new ImagePattern(new Image("file:src/main/java/img/turn_EN.png"));
        snaketurn_ES = new ImagePattern(new Image("file:src/main/java/img/turn_ES.png"));
        snaketurn_NE = new ImagePattern(new Image("file:src/main/java/img/turn_NE.png"));
        snaketurn_NW = new ImagePattern(new Image("file:src/main/java/img/turn_NW.png"));
        snaketurn_SE = new ImagePattern(new Image("file:src/main/java/img/turn_SE.png"));
        snaketurn_SW = new ImagePattern(new Image("file:src/main/java/img/turn_SW.png"));
        snaketurn_WN = new ImagePattern(new Image("file:src/main/java/img/turn_WN.png"));
        snaketurn_WS = new ImagePattern(new Image("file:src/main/java/img/turn_WS.png"));

        //System.out.println("GOT ALL IMAGES");

        snake_array = new ArrayList<SnakeBlock>();
        for (int i = 0; i < 2; i++) {
            SnakeBlock r;
            if (i == 0){
                r = new SnakeBlock("head", "E");
            }
            else{
                r = new SnakeBlock("tail", "E");
            }
            r.setTranslateX(init_head_x - (i*40));
            r.setTranslateY(init_head_y);

            snake_array.add(r);
        }
        draw();
        handle_animation();

    }

    private void draw() {
        Pane pane = (Pane) curScene.getRoot();
        for (int i = 0; i < snake_array.size(); i++) {
            snake_container.getChildren().add(snake_array.get(i));
        }
        pane.getChildren().add(snake_container);
    }


    private void handle_animation() {
        //gui.isRunning = false;
        if(!gui.isRunning){
            return;
        }

        KeyFrame frame = new KeyFrame(Duration.millis(gui.FPS), event -> {
            SnakeBlock tail;
            if (snake_array.size() > 0){
                rotateTail();
                tail = snake_array.remove(snake_array.size() - 1);
            }
            else{
                return;
            };
            //rotateTail();
            SnakeBlock new_tail = snake_array.get(snake_array.size()-1);
            new_tail.setTail();
            tail.setHead(this.direction);
            checkTurns();
            if (direction == "N") {
                tail.setTranslateX(snake_array.get(0).getTranslateX());
                tail.setTranslateY(snake_array.get(0).getTranslateY() - gui.block);
                snake_array.add(0, tail);

            } else if (direction == "E") {
                tail.setTranslateX(snake_array.get(0).getTranslateX() + gui.block);
                tail.setTranslateY(snake_array.get(0).getTranslateY());
                snake_array.add(0, tail);

            } else if (direction == "S") {
                tail.setTranslateX(snake_array.get(0).getTranslateX());
                tail.setTranslateY(snake_array.get(0).getTranslateY() + gui.block);
                snake_array.add(0, tail);

            } else if (direction == "W") {
                tail.setTranslateX(snake_array.get(0).getTranslateX() - gui.block);
                tail.setTranslateY(snake_array.get(0).getTranslateY());
                snake_array.add(0, tail);
            }
            rotateTail();

            SnakeBlock old_head = snake_array.get(1);
            old_head.setBody(old_head.getDirection());

            checkTurns();

            if (self_hit()) {
                moving = false;
            }
        });
        timeline.getKeyFrames().add(frame);



    }


    public void grow(){
        SnakeBlock grow = new SnakeBlock("head", direction);
        grow.setTranslateX(snake_array.get(0).getTranslateX());
        grow.setTranslateY(snake_array.get(0).getTranslateY());
        snake_container.getChildren().add(grow);
        snake_array.add(grow);
        rotateHead();
        checkTurns();
    }

    private boolean self_hit(){
        Rectangle snake_head = snake_array.get(0);
        for (int i = 1; i < snake_array.size(); i++){
            Rectangle body = snake_array.get(i);
            if (snake_head.getTranslateX() == body.getTranslateX() && snake_head.getTranslateY() == body.getTranslateY()){
                return true;
            }
        }
        return false;
    }

    private void rotateHead(){
        Rectangle head = snake_array.get(0);

        if (direction == "N"){
            head.setFill(snakehead_N);
        }
       else if (direction == "E"){
            head.setFill(snakehead_E);
        }
        else if (direction == "S"){
            head.setFill(snakehead_S);
        }
        else if (direction == "W"){
            head.setFill(snakehead_W);
        }
    }

    public void rotateTail(){
        SnakeBlock tail = snake_array.get(snake_array.size()-1);
        String block_intfront_dir = snake_array.get(snake_array.size()-2).getDirection();
        if (block_intfront_dir == "N"){
            tail.setFill(snaketail_N);
        }
        else if (block_intfront_dir == "E"){
            tail.setFill(snaketail_E);
        }
        else if (block_intfront_dir == "S"){
            tail.setFill(snaketail_S);
        }
        else if (block_intfront_dir == "W"){
            tail.setFill(snaketail_W);
        }
    }

    public void checkTurns(){
        for (int i = 1; i < snake_array.size() - 1; i++){
            SnakeBlock bb = snake_array.get(i); // body block
            String bb_dir = bb.getDirection();
            SnakeBlock infront = snake_array.get(i-1);
            String front_dir = infront.getDirection();
            if (bb_dir == front_dir){
                continue;
            }
            else if (bb_dir == "N" && front_dir == "E"){
                bb.setFill(snaketurn_NE);
            }
            else if (bb_dir == "N" && front_dir == "W"){
                bb.setFill(snaketurn_NW);
            }
            else if (bb_dir == "E" && front_dir == "N"){
                bb.setFill(snaketurn_EN);
            }
            else if (bb_dir == "E" && front_dir == "S"){
                bb.setFill(snaketurn_ES);
            }
            else if (bb_dir == "S" && front_dir == "E"){
                bb.setFill(snaketurn_SE);
            }
            else if (bb_dir == "S" && front_dir == "W"){
                bb.setFill(snaketurn_SW);
            }
            else if (bb_dir == "W" && front_dir == "N"){
                bb.setFill(snaketurn_WN);
            }
            else if (bb_dir == "W" && front_dir == "S"){
                bb.setFill(snaketurn_WS);
            }
        }
    }


    public class SnakeBlock extends Rectangle{
        String type;
        String cur_dir;

        public SnakeBlock(String type, String direction){
            this.type = type;
            this.cur_dir = direction;
            setWidth(gui.block);
            setHeight(gui.block);
            if(type == "head"){
                setHead(direction);
            }
            else if (type == "body"){
                setBody(this.cur_dir);
            }
            else{
                setTail();
            }
        }
        public void setHead(String dir){
            this.type = "head";
            this.cur_dir = dir;
            if(dir == "N"){
                setFill(snakehead_N);
            }
            else if (dir == "E"){
                setFill(snakehead_E);
            }
            else if (dir == "W"){
                setFill(snakehead_W);
            }
            else if (dir == "S"){
                setFill(snakehead_S);
            }
        }
        public void setBody(String dir){
            this.type = "body";
            this.cur_dir = dir;
            setFill(snakebody);
        }

        public void setTail(){
            this.type = "tail";
            this.cur_dir = snake_array.get(snake_array.size()-1).getDirection();
            if (cur_dir == "N"){
                setFill(snaketail_N);
            }
            else if (cur_dir == "E"){
                setFill(snaketail_E);
            }
            else if (cur_dir == "S"){
                setFill(snaketail_S);
            }
            else if (cur_dir == "W"){
                setFill(snaketail_W);
            }
        }

        public String getDirection(){
            return this.cur_dir;
        }

    }



}