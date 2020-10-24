import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Random;

public class Fruit {
    GUI gui;
    ArrayList<Rectangle> fruits_on_board;
    Image fruit_img;
    ImagePattern img;

    public Fruit(GUI gui){
        this.gui = gui;
        fruit_img = new Image("file:src/main/java/banana.png") ;
        img = new ImagePattern(fruit_img);

        Scene curScene = gui.curScene;
        fruits_on_board = new ArrayList<Rectangle>();
        Pane pane = (Pane) curScene.getRoot();
        if (gui.level == 1){
            Rectangle f1 = new Rectangle(gui.block, gui.block);
            f1.setTranslateX(22*gui.block);
            f1.setTranslateY(gui.block*10);
            f1.setFill(img);
            fruits_on_board.add(f1);
            Rectangle f2 = new Rectangle(gui.block, gui.block);
            f2.setTranslateX(gui.block*15);
            f2.setTranslateY(gui.block*14);
            f2.setFill(img);
            fruits_on_board.add(f2);
            Rectangle f3 = new Rectangle(gui.block, gui.block);
            f3.setTranslateX(gui.block*20);
            f3.setTranslateY(gui.block*17);
            f3.setFill(img);
            fruits_on_board.add(f3);
            Rectangle f4 = new Rectangle(gui.block, gui.block);
            f4.setTranslateX(gui.block*3);
            f4.setTranslateY(gui.block*2);
            f4.setFill(img);
            fruits_on_board.add(f4);
            Rectangle f5 = new Rectangle(gui.block, gui.block);
            f5.setTranslateX(gui.block*2);
            f5.setTranslateY(gui.block*16);
            f5.setFill(img);
            fruits_on_board.add(f5);
            pane.getChildren().addAll(f1,f2,f3,f4,f5);
        }
        else if (gui.level == 2){
            Rectangle f1 = new Rectangle(gui.block, gui.block);
            f1.setTranslateX(22*gui.block);
            f1.setTranslateY(gui.block*10);
            f1.setFill(img);
            fruits_on_board.add(f1);
            Rectangle f2 = new Rectangle(gui.block, gui.block);
            f2.setTranslateX(gui.block*15);
            f2.setTranslateY(gui.block*14);
            f2.setFill(img);
            fruits_on_board.add(f2);
            Rectangle f3 = new Rectangle(gui.block, gui.block);
            f3.setTranslateX(gui.block*20);
            f3.setTranslateY(gui.block*17);
            f3.setFill(img);
            fruits_on_board.add(f3);
            Rectangle f4 = new Rectangle(gui.block, gui.block);
            f4.setTranslateX(gui.block*3);
            f4.setTranslateY(gui.block*2);
            f4.setFill(img);
            fruits_on_board.add(f4);
            Rectangle f5 = new Rectangle(gui.block, gui.block);
            f5.setTranslateX(gui.block*2);
            f5.setTranslateY(gui.block*16);
            f5.setFill(img);
            fruits_on_board.add(f5);
            Rectangle f6 = new Rectangle(gui.block, gui.block);
            f6.setTranslateX(30*gui.block);
            f6.setTranslateY(gui.block*14);
            f6.setFill(img);
            fruits_on_board.add(f6);
            Rectangle f7 = new Rectangle(gui.block, gui.block);
            f7.setTranslateX(gui.block*27);
            f7.setTranslateY(gui.block*3);
            f7.setFill(img);
            fruits_on_board.add(f7);
            Rectangle f8 = new Rectangle(gui.block, gui.block);
            f8.setTranslateX(gui.block*30);
            f8.setTranslateY(gui.block*19);
            f8.setFill(img);
            fruits_on_board.add(f8);
            Rectangle f9 = new Rectangle(gui.block, gui.block);
            f9.setTranslateX(gui.block*13);
            f9.setTranslateY(gui.block*1);
            f9.setFill(img);
            fruits_on_board.add(f9);
            Rectangle f10 = new Rectangle(gui.block, gui.block);
            f10.setTranslateX(gui.block*4);
            f10.setTranslateY(gui.block*16);
            f10.setFill(img);
            fruits_on_board.add(f10);
            pane.getChildren().addAll(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10);
        }
        else if (gui.level == 3){
            Rectangle f1 = new Rectangle(gui.block, gui.block);
            f1.setTranslateX(22*gui.block);
            f1.setTranslateY(gui.block*10);
            f1.setFill(img);
            fruits_on_board.add(f1);
            Rectangle f2 = new Rectangle(gui.block, gui.block);
            f2.setTranslateX(gui.block*15);
            f2.setTranslateY(gui.block*14);
            f2.setFill(img);
            fruits_on_board.add(f2);
            Rectangle f3 = new Rectangle(gui.block, gui.block);
            f3.setTranslateX(gui.block*20);
            f3.setTranslateY(gui.block*17);
            f3.setFill(img);
            fruits_on_board.add(f3);
            Rectangle f4 = new Rectangle(gui.block, gui.block);
            f4.setTranslateX(gui.block*3);
            f4.setTranslateY(gui.block*2);
            f4.setFill(img);
            fruits_on_board.add(f4);
            Rectangle f5 = new Rectangle(gui.block, gui.block);
            f5.setTranslateX(gui.block*2);
            f5.setTranslateY(gui.block*16);
            f5.setFill(img);
            fruits_on_board.add(f5);
            Rectangle f6 = new Rectangle(gui.block, gui.block);
            f6.setTranslateX(30*gui.block);
            f6.setTranslateY(gui.block*14);
            f6.setFill(img);
            fruits_on_board.add(f6);
            Rectangle f7 = new Rectangle(gui.block, gui.block);
            f7.setTranslateX(gui.block*27);
            f7.setTranslateY(gui.block*3);
            f7.setFill(img);
            fruits_on_board.add(f7);
            Rectangle f8 = new Rectangle(gui.block, gui.block);
            f8.setTranslateX(gui.block*30);
            f8.setTranslateY(gui.block*19);
            f8.setFill(img);
            fruits_on_board.add(f8);
            Rectangle f9 = new Rectangle(gui.block, gui.block);
            f9.setTranslateX(gui.block*13);
            f9.setTranslateY(gui.block*1);
            f9.setFill(img);
            fruits_on_board.add(f9);
            Rectangle f10 = new Rectangle(gui.block, gui.block);
            f10.setTranslateX(gui.block*4);
            f10.setTranslateY(gui.block*16);
            f10.setFill(img);
            fruits_on_board.add(f10);
            Rectangle f11 = new Rectangle(gui.block, gui.block);
            f11.setTranslateX(gui.block*8);
            f11.setTranslateY(gui.block*7);
            f11.setFill(img);
            fruits_on_board.add(f11);
            Rectangle f12 = new Rectangle(gui.block, gui.block);
            f12.setTranslateX(gui.block*18);
            f12.setTranslateY(gui.block*6);
            f12.setFill(img);
            fruits_on_board.add(f12);
            Rectangle f13 = new Rectangle(gui.block, gui.block);
            f13.setTranslateX(gui.block*9);
            f13.setTranslateY(gui.block*12);
            f13.setFill(img);
            fruits_on_board.add(f13);
            Rectangle f14 = new Rectangle(gui.block, gui.block);
            f14.setTranslateX(gui.block*0);
            f14.setTranslateY(gui.block*0 + 40);
            f14.setFill(img);
            fruits_on_board.add(f14);
            Rectangle f15 = new Rectangle(gui.block, gui.block);
            f15.setTranslateX(gui.block*30);
            f15.setTranslateY(gui.block*7);
            f15.setFill(img);
            fruits_on_board.add(f15);

            pane.getChildren().addAll(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13,f14,f15);
        }

    }

    public void moveFruit(Rectangle fruit_eaten){
        Random random = new Random();
        int width_blocks = 32;
        int height_blocks = 19;
        int random_x = random.nextInt(width_blocks);
        int random_y = random.nextInt(height_blocks);
        boolean fruit_already_here = true;
        while(fruit_already_here){
            boolean found = false;
            for (int i = 0; i < fruits_on_board.size();i++){
                Rectangle f = fruits_on_board.get(i);
                if (f.getTranslateX() == random_x * gui.block && f.getTranslateY() == (random_y * gui.block + 40)){
                    found = true;
                    break;
                }
            }
            if(!found){
                fruit_already_here = false;
            }
        }
        fruit_eaten.setTranslateX(random_x * gui.block);
        fruit_eaten.setTranslateY(random_y * gui.block + 40);
    }
}
