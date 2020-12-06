package ca.uwaterloo.cs349.pdfreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

// PDF sample code from
// https://medium.com/@chahat.jain0/rendering-a-pdf-document-in-android-activity-fragment-using-pdfrenderer-442462cb8f9a
// Issues about cache etc. are not at all obvious from documentation, so read this carefully.

public class MainActivity extends AppCompatActivity implements Serializable{

    final static String LOGNAME = "pdf_viewer";
    final String FILENAME = "shannon1948.pdf";
    final int FILERESID = R.raw.shannon1948;

    // manage the pages of the PDF, see below
    static PdfRenderer pdfRenderer;
    private ParcelFileDescriptor parcelFileDescriptor;
    static private PdfRenderer.Page currentPage;


    // custom ImageView class that captures strokes and draws them over the image
    static PDFimage pageImage;

    // toolbar
    private ImageButton[] btn = new ImageButton[4];
    private ImageButton btn_unfocus;
    private int[] btn_id = {R.id.pan, R.id.pencil, R.id.highlighter, R.id.eraser};

    // buttons
    static ImageButton next_page;
    static ImageButton prev_page;
    static ImageButton undo_button;
    static ImageButton redo_button;


    static Model model;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = Model.getModel();

        setupToolbar();

        next_page = findViewById(R.id.next_page);
        prev_page = findViewById(R.id.prev_page);
        undo_button = findViewById(R.id.undo);
        redo_button = findViewById(R.id.redo);
        validateUndoRedoButtons();


        LinearLayout layout = findViewById(R.id.pdfLayout);

        pageImage = new PDFimage(this, model);
        layout.addView(pageImage);

        layout.setEnabled(true);
        pageImage.setMinimumWidth(1000);
        pageImage.setMinimumHeight(2000);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart(){
        // open page 0 of the PDF
        // it will be displayed as an image in the pageImage (above)
        super.onStart();
        try {
            openRenderer(this);
        } catch (IOException exception) {
            Log.d(LOGNAME, "Error opening PDF");
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume(){
        // open page 0 of the PDF
        // it will be displayed as an image in the pageImage (above)
        super.onResume();
        showPage(model.cur_page);
    }



//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//            closeRenderer();
//        } catch (IOException ex) {
//            Log.d(LOGNAME, "Unable to close PDF renderer");
//        }
//    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            closeRenderer();
        } catch (IOException ex) {
            Log.d(LOGNAME, "Unable to close PDF renderer");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void  onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        try {
            model.save(this);
        } catch (IOException ex) {
            Log.d(LOGNAME, "Unable to save");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void  onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        try {
            model.load(this);
            model = Model.getModel();
        } catch (IOException | ClassNotFoundException ex) {
            Log.d(LOGNAME, "Unable to restore");
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.
        TextView filename_label = (TextView) findViewById(R.id.filename);
        filename_label.setText(FILENAME);
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {
            // pdfRenderer cannot handle the resource directly,
            // so extract it into the local cache directory.
            InputStream asset = this.getResources().openRawResource(FILERESID);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        // capture PDF data
        // all this just to get a handle to the actual PDF representation
        if (parcelFileDescriptor != null) {
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        }
    }

    // do this before you quit!
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
        }
        pdfRenderer.close();
        parcelFileDescriptor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showPage(int index) {
        Log.d(LOGNAME, "SHOWING PAGE "+ index);
        if (pdfRenderer.getPageCount() <= index) {
            model.cur_page = pdfRenderer.getPageCount() - 1;
            return;
        }
        if(index < 0){
            model.cur_page = 0;
            return;
        }
        // Close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
        }
        //turn on and off appropriate page buttons
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(), Bitmap.Config.ARGB_8888);

        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Display the page
        pageImage.setImage(bitmap);
        //pageImage.scale = 1f;
        //pageImage.setAdjustViewBounds(true);
        validatePageButtons();
        model.cur_page = index;

        //Log.d(LOGNAME, "finished showing page");
    }


    protected void setupToolbar() {
        //Log.d(logname, "toolbar on create");
        //super.onCreate(savedInstanceState);
        //toolbar = new Toolbar(this);
        setContentView(R.layout.activity_main);


        for(int i = 0; i < btn.length; i++){
            btn[i] = (ImageButton) findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            //btn[i].setOnClickListener(this);
        }

        btn_unfocus = btn[0];
        //btn_unfocus.setTextColor(Color.rgb(255, 255, 255));
        btn_unfocus.setBackgroundColor(Color.rgb(3, 106, 150));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        //setForcus(btn_unfocus, (Button) findViewById(v.getId()));
        //Or use switch
        Log.d(LOGNAME, "toolbar on click");
        switch (v.getId()){
            case R.id.pan:
                setFocus(btn_unfocus, btn[0]);
                model.setTool(Model.Tool.PAN);
                break;

            case R.id.pencil:
                setFocus(btn_unfocus, btn[1]);
                model.setTool(Model.Tool.PENCIL);
                break;

            case R.id.highlighter:
                setFocus(btn_unfocus, btn[2]);
                model.setTool(Model.Tool.HIGHLIGHTER);
                break;

            case R.id.eraser:
                setFocus(btn_unfocus, btn[3]);
                model.setTool(Model.Tool.ERASER);
                break;

            case R.id.next_page:
                try{
                    //model.cur_page++;
                    showPage(model.cur_page + 1);
                    //pageImage.cur_page = cur_page_num;
                    pageImage.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.prev_page:
                try{
                    //model.cur_page--;
                    showPage(model.cur_page - 1);
                    //pageImage.cur_page = cur_page_num;
                    pageImage.invalidate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.undo:
                Action action = model.undo();
                if(action != null){
                    showPage(action.page);
                }
                validateUndoRedoButtons();
                //model.cur_page = action.page;
                break;

            case R.id.redo:
                Action action2 = model.redo();
                if(action2 != null){
                    showPage(action2.page);
                }
                validateUndoRedoButtons();
                //model.cur_page = action2.page;
                break;
        }
    }

    private void setFocus(ImageButton btn_unfocus, ImageButton btn_focus){
        Log.d(LOGNAME, "setFocus");
        //btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        //btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void validatePageButtons(){
        if(model.cur_page == 0){
            prev_page.setClickable(false);
            prev_page.setEnabled(false);
        }
        else{
            prev_page.setClickable(true);
            prev_page.setEnabled(true);
        }
        if(model.cur_page == pdfRenderer.getPageCount()-1){
            next_page.setClickable(false);
            next_page.setEnabled(false);
        }
        else{
            next_page.setClickable(true);
            next_page.setEnabled(true);
        }
    }

    public static void validateUndoRedoButtons(){
        if(model.undo_stack.empty()){
            undo_button.setEnabled(false);
            undo_button.setClickable(false);
        }
        else{
            undo_button.setEnabled(true);
            undo_button.setClickable(true);
        }
        if(model.redo_stack.empty()){
            redo_button.setEnabled(false);
            redo_button.setClickable(false);
        }
        else{
            redo_button.setEnabled(true);
            redo_button.setClickable(true);
        }
    }

}
