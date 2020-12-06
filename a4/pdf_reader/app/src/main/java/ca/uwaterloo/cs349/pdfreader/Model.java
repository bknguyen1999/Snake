package ca.uwaterloo.cs349.pdfreader;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Model {
    enum Tool{
        PENCIL,
        HIGHLIGHTER,
        ERASER,
        PAN
    }

    static Model model = new Model();


    String LOGNAME = "MODEL";

    ArrayList<MyPath> paths = new ArrayList<MyPath>();
    int cur_page = 0;
    MainActivity mainActivity;
    Tool cur_tool = Tool.PAN;


    // drawing values
    private int pencil_color = Color.BLUE;
    private float pencil_stroke = 5f;
    private int highlighter_color = Color.YELLOW;
    private float highlighter_stroke = 25f;
    int current_color = pencil_color;
    float current_stroke = pencil_stroke;


    // undo redo stacks
    Stack<Action> undo_stack = new Stack<Action>();;
    Stack<Action> redo_stack = new Stack<Action>();;


    public static Model getModel(){
        return model;
    }

    public void setTool(Tool tool){
        cur_tool = tool;
        if(tool == Tool.PENCIL){
            current_color = pencil_color;
            current_stroke = pencil_stroke;
        }
        else if(tool == Tool.HIGHLIGHTER){
            current_color = highlighter_color;
            current_stroke = highlighter_stroke;
        }
        else if(tool == Tool.ERASER){
            current_color = Color.TRANSPARENT;
            current_stroke = pencil_stroke;
        }
        else if(tool == Tool.PAN){
            current_color = Color.TRANSPARENT;
            current_stroke = pencil_stroke;
        }
    }


    public void pushUndoStack(Action action){
        Log.d(LOGNAME,"PUSHED TO UNDO STACK");
        undo_stack.push(action);
        //clearRedoStack();
        MainActivity.validateUndoRedoButtons();
    }

    private void pushRedoStack(Action action){
        redo_stack.push(action);
    }

    private Action popUndoStack(){
        return undo_stack.pop();
    }

    private Action popRedoStack(){
        return redo_stack.pop();
    }

    public void clearRedoStack(){
        while(!redo_stack.empty()){
            redo_stack.pop();
        }
        //MainActivity.validateUndoRedoButtons();
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Action undo(){
        Log.d(LOGNAME,"UNDO");
        if(!undo_stack.empty()) {
            Action action = popUndoStack();
            //MainActivity.showPage(action.page);
            pushRedoStack(action);
            //ArrayList<MyPath> paths = pageImage.getPaths();
            if (action.type == "DRAW") {
                Log.d(LOGNAME, "UNDO DRAWING");
                paths.remove(action.path);
                Log.d(LOGNAME, "paths size: " + paths.size());
            } else {
                Log.d(LOGNAME, "UNDO ERASE");
                paths.add(action.path_index, action.path);
            }
            MainActivity.validateUndoRedoButtons();
            return action;
        }
        return null;
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Action redo(){
        if(!redo_stack.empty()){
            Action action = popRedoStack();
            //MainActivity.showPage(action.page);
            pushUndoStack(action);
            //ArrayList<MyPath> paths = pageImage.getPaths();
            if(action.type == "DRAW"){
                paths.add(action.path_index, action.path);
            }
            else{
                paths.remove(action.path);
            }
            MainActivity.validateUndoRedoButtons();
            return action;
        }
        return null;

    }


    public void save(Context context) throws IOException {
        Log.d(LOGNAME, "Saved Called");
        FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), "data.txt"));
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(this);
        os.close();
        fos.close();
    }

    public void load(Context context) throws IOException, ClassNotFoundException {
        Log.d(LOGNAME, "Load Called");
        FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), "data.txt"));
        ObjectInputStream is = new ObjectInputStream(fis);
        model = (Model) is.readObject();
        is.close();
        fis.close();
    }



}
