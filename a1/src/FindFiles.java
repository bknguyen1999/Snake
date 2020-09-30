import java.util.HashMap;
import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import java.nio.file.*;
import java.util.stream.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FindFiles {


    public static void help(){
        System.out.println("HELP");
    }

    public static boolean verify_input(String args[]){
        return true;
    }

    public static boolean check_extensions(String file, String[] extensions){
        File f = new File(file);
        return (Arrays.stream(extensions).anyMatch(entry -> file.endsWith(entry))) && f.isFile();
    }

    public static boolean is_directory(String s){
        File f = new File(s);
        return f.isDirectory();
    }

    public static void find_file(String filetofind, String directory, HashMap<String, String> options, String[] extensions){
        // looks for file in current directory and does a dfs search in the subdirectories if file isnt found
        System.out.println("=============== Looking for file in directory: " + directory + " ===============");
        File dir = new File(directory);
        String[] list_files_dir = dir.list();

        Vector<String> copy = new Vector<String>(Arrays.asList(list_files_dir));
        if (!options.containsKey("-ext") && !options.containsKey("-reg")){
            copy.removeIf(f -> !(f.equals(filetofind)));
        }

        if (options.containsKey("-ext")){
            copy.removeIf(f -> !check_extensions(f, extensions));
            if (!options.containsKey("-reg")){
                copy.removeIf(f -> !(f.equals(filetofind)));
            }
        }
        if (options.containsKey(("-reg"))){
            copy.removeIf(f -> !f.matches(filetofind));
        }

        for (int i = 0; i < copy.size(); i++) {
            File f = new File(copy.get(i));
            System.out.println(f.getAbsolutePath());
        }
        // by now, you should have already gotten all files in the current directory so just do a dfs on the subdirectories
        if (options.containsKey("-r")) {

            Vector<String> subdir = new Vector<String>(Arrays.asList(list_files_dir));
            subdir.removeIf(f -> !is_directory(directory + "/" + f));
            for (int i = 0; i < subdir.size(); i++) {
                find_file(filetofind, directory + "/" + subdir.get(i), options, extensions);
            }
        }
    }


    public static void main(String args[]){
        String filetofind = args[0];
        System.out.println("filetofind:" + filetofind);
        for(int i=1; i < args.length; i++){ // first check to see if there's a help flag
            if (args[i].equals("-help")){
                help();
                //return 0;
                //System.out.println("help");
            }
        }
        if (verify_input(args) == false){ // verify that the command line inputs are valid
            help();
            //return 0;
            //System.out.println("invalid input");
        }
        HashMap<String, String> options = new HashMap<String, String>();
        String startingDir = "."; // current directory
        String[] extensions = {};
        for(int i = 1; i < args.length; i++) {
            if (args[i].equals("-r")) {
                options.put("-r", "");
            } else if (args[i].equals("-reg")) {
                options.put("-reg", filetofind);
            } else if (args[i].equals("-dir")) {
                options.put("-dir", args[i + 1]);
                startingDir = args[i + 1];
            } else if (args[i].equals("-ext")) {
                options.put("-ext", args[i + 1]);
                String val = options.get("-ext");
                extensions = val.split(",");
                for (int j = 0; j < extensions.length; j++){
                    extensions[j] = "." + extensions[j];
                }
            }
        }
        find_file(filetofind, startingDir, options, extensions);

    }
}
