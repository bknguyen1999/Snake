package ca.uwaterloo.cs349.pdfreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import javax.security.auth.DestroyFailedException;
import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class PDFimage extends ImageView {

    final String LOGNAME = "pdf_image";
    MainActivity main_activity;
    Model model;

    // drawing path
    Path path = null;
    MyPath fp = null;
    Action action = null;
    private static final float TOUCH_TOLERANCE = 4;
    private float prev_x;
    private float prev_y;


    // image to display
    Bitmap bitmap;
    Paint paint;

    // scale stuff
    ScaleGestureDetector sgd;
    Matrix matrix;
    float scale = 1f;
    float startx;
    float starty;
    float dx;
    float dy;
    float posx;
    float posy;
    float lastGestureX;
    float lastGestureY;
    float[] values;
    Drawable drawable;
    Rect imageBounds;

    // constructor
    public PDFimage(Context context, Model model) {
        super(context);
        this.main_activity = (MainActivity) context;
        this.model = model;
        paint = new Paint();
        paint.setColor(model.current_color);
        paint.setStrokeWidth(model.current_stroke);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);


        sgd = new ScaleGestureDetector(context, new ScaleListener());
        //drawable = this.getDrawable();
        //matrix.setValues(values);
        matrix = new Matrix();
        //this.setScaleType(ScaleType.MATRIX);
        //this.setAdjustViewBounds(true);
        //pdfimage = this;
    }




    // capture touch events (down/move/up) to create a path
    // and use that to create a stroke that we can draw
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        Log.d(LOGNAME, "POINTER COUNT: " + event.getPointerCount());
        if(model.cur_tool != Model.Tool.PAN && !sgd.isInProgress() && event.getPointerCount() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(LOGNAME, "Action down");
                    //path = new Path();
                    Log.d(LOGNAME, "" + event.getX() + ", " + event.getY());
                    fp = new MyPath(model.current_color, model.current_stroke, model.cur_page);
//                    matrix = new Matrix();
//                    values = new float[9];
//                    matrix.getValues(values);
//                    float x = (event.getX() - posx- values[2]) / values[0];
//                    float y = (event.getY() - posy - values[5]) / values[4];

                    final float x = (event.getX() / scale) - (posx * scale);
                    final float y = (event.getY() / scale) - (posy * scale);


                    //path.moveTo(x, y);
                    fp.moveTo(x,y);
                    prev_x = x;
                    prev_y = y;
                    //MyPath fp = new MyPath(current_color, current_stroke, path, cur_page);
                    if (model.cur_tool == Model.Tool.PENCIL) {
                        model.paths.add(fp);
                        action = new Action("DRAW", fp, model.paths.size() - 1, model.cur_page);
                        model.pushUndoStack(action);
                        model.clearRedoStack();

                    } else if (model.cur_tool == Model.Tool.HIGHLIGHTER) {
                        //paths.add(0, fp);
                        model.paths.add(0, fp);
                        action = new Action("DRAW", fp, 0, model.cur_page);
                        model.pushUndoStack(action);
                        model.clearRedoStack();
                    } else {
                        erase(fp);
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(LOGNAME, "Action move");
//                    matrix = new Matrix();
//                    values = new float[9];
//                    matrix.getValues(values);
//                    float x2 = (event.getX() - posx - values[2]) / values[0];
//                    float y2 = (event.getY() - posy - values[5]) / values[4];

                    final float x2 = (event.getX() / scale) - (posx * scale);
                    final float y2 = (event.getY() / scale) - (posy * scale);


                    float dx = Math.abs(x2 - prev_x);
                    float dy = Math.abs(y2 - prev_y);

                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        //path.quadTo(prev_x, prev_y, (x2 + prev_x) / 2, (y2 + prev_y) / 2);
                        fp.quadTo(prev_x, prev_y, (x2 + prev_x) / 2, (y2 + prev_y) / 2);
                        prev_x = x2;
                        prev_y = y2;
                    }
                    if (model.cur_tool == Model.Tool.ERASER) {
                        erase(fp);
                    }

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(LOGNAME, "Action up");
//                    matrix = new Matrix();
//                    values = new float[9];
//                    matrix.getValues(values);
//                    float x3 = (event.getX() -posx- values[2]) / values[0];
//                    float y3 = (event.getY() -posy- values[5]) / values[4];

                    final float x3 = (event.getX() / scale) - (posx * scale);
                    final float y3 = (event.getY() / scale) - (posy * scale);


                    //path.lineTo(x3, y3);
                    fp.lineTo(x3, y3);
                    if (model.cur_tool == Model.Tool.ERASER) {
                        erase(fp);
                    }
                    invalidate();
                    break;
            }
        }
        else{
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(LOGNAME, "Action down");
                    if(!sgd.isInProgress()) {
                        startx = event.getX();
                        starty = event.getY();
                    }
                    else{
                        lastGestureX = sgd.getFocusX();
                        lastGestureY = sgd.getFocusY();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!sgd.isInProgress()) {
                        Log.d(LOGNAME, "Action move");
                        dx = event.getX() - startx;
                        dy = event.getY() - starty;
                        startx = event.getX();
                        starty = event.getY();
                        posx += dx;
                        posy += dy;
                        //matrix.postTranslate(posx, posy);
                        invalidate();
                    }
                    else{
                        final float gx = sgd.getFocusX();
                        final float gy = sgd.getFocusY();

                        final float gdx = gx - lastGestureX;
                        final float gdy = gy - lastGestureY;

                        posx += gdx;
                        posy += gdy;
                        //matrix.postTranslate(posx, posy);

                        invalidate();

                        lastGestureX = gx;
                        lastGestureY = gy;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(LOGNAME, "Action up");
                    break;
            }
        }
        return true;
    }

    // set image as background
    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // set brush characteristics
    // e.g. color, thickness, alpha
    public void setBrush(Paint paint) {
        this.paint = paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.save();
        // draw background
        if (bitmap != null) {
            this.setImageBitmap(bitmap);
        }

        if (sgd.isInProgress()) {
            canvas.scale(scale, scale, sgd.getFocusX(), sgd.getFocusY());
        }
        else{
            canvas.scale(scale, scale, lastGestureX, lastGestureY);
        }
        //canvas.scale(scale, scale, sgd.getFocusX(), sgd.getFocusY());
        //canvas.setMatrix(matrix);
        if(posx > this.getWidth() / 2){
            posx = this.getWidth() / 2;
        }
        else if(posx < -(this.getWidth()/2)){
            posx = -(this.getWidth() / 2);
        }
        if(posy > this.getHeight() / 2){
            posy = this.getHeight() / 2;
        }
        else if(posy < -(this.getHeight()/2)){
            posy = -(this.getHeight()/2);
        }
        //canvas.setMatrix(matrix);
        canvas.translate(posx, posy);

        // draw lines over it
        for (MyPath mp : model.paths) {
            if(mp.page == model.cur_page){
                paint.setColor(mp.color);
                paint.setStrokeWidth(mp.strokeWidth);
                canvas.drawPath(mp, paint);
            }
        }
        super.onDraw(canvas);
        //canvas.restore();


    }

    private void erase(Path eraser_path){
        Region clip = new Region(0,0, getWidth(), getHeight());
        Region eraser_region = new Region();
        eraser_region.setPath(eraser_path, clip);
        for(int i = model.paths.size() - 1; i >= 0; i--){
            MyPath mp = model.paths.get(i);
            //Path cur = mp.path;
            Region cur_reg = new Region();
            cur_reg.setPath(mp, clip);
            boolean intersect = !eraser_region.quickReject(cur_reg) && eraser_region.op(cur_reg, Region.Op.INTERSECT);
            if(intersect){
                Log.d(LOGNAME, "found an intersection");
                action = new Action("ERASE", mp, i, model.cur_page);
                model.pushUndoStack(action);
                model.clearRedoStack();
                model.paths.remove(mp);
                //Log.d(LOGNAME, "" + paths.size());
                return;
            }
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(1f, Math.min(7f, scale));

            Log.d("Scale", String.valueOf(scale));
            //matrix.postScale(scale,scale);

            //invalidate();

            //return true;
            return super.onScale(detector);
        }

    }


}
