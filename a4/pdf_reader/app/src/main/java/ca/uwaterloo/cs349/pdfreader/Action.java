package ca.uwaterloo.cs349.pdfreader;

public class Action {
    String type;
    MyPath path;
    int path_index;
    int page;

    // if type == erase, then path will be the path that was erased
    // if type == draw, then path will be the path that was drawn (with pencil or highlighter)
    // path_index is the index of which the path was in the paths list

    public Action(String type, MyPath path, int path_index, int page){
        this.type = type;
        this.path = path;
        this.path_index = path_index;
        this.page = page;
    }

}
