package ca.uwaterloo.cs349.pdfreader;
import android.graphics.Paint;
import android.graphics.Path;



public class MyPath extends Path {
    public int color;
    public float strokeWidth;
    //public Path path;
    public int page;


    public MyPath(int color, float strokeWidth, int page) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        //this.path = path;
        this.page = page;
    }
}
