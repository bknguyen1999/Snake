import java.util.HashMap;
import java.io.*;
import java.util.Vector;
import java.util.Arrays;
import java.util.stream.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;





public class FindFiles {


    public static void help(){
        System.out.println("Usage: java FindFiles filetofind [-option arg]");
        System.out.println("-help                     :: print out a help page and exit the program.");
        System.out.println("-r                        :: execute the command recursively in subfiles.");
        System.out.println("-reg                      :: treat `filetofind` as a regular expression when searching.");
        System.out.println("-dir [directory]          :: find files starting in the specified directory.");
        System.out.println("-ext [ext1,ext2,...]      :: find files matching [filetofind] with extensions [ext1, ext2,...].");
    }

    public static boolean verify_input(String args[]){
        if(args.length == 0){
            System.out.println("ERROR: FindFiles requires arguments. See -help menu for more details.");
            help();
            return false;
        }
        for(int i=1; i < args.length; i++){ // first check to see if there's a help flag
            if (args[i].equals("-help")){
                help();
                return false;
            }
        }
        if (args[0].equals("-r") || args[0].equals("-ext") || args[0].equals("-reg") || args[0].equals("-dir")){
            System.out.println("ERROR: First argument must be the file name to look for");
            help();
            return false;
        }
        if (args.length == 1){
            return true;
        }
        else{
            boolean is_option = true;
            boolean is_dir = false; // we need this because we need to check the arg after the -dir flag
            boolean is_ext = false; // we need this because we need to check the arg after the -ext flag
            for (int i = 1; i < args.length; i++){
                if (is_option){
                    if (!(args[i].equals("-r") || args[i].equals("-ext") || args[i].equals("-reg") || args[i].equals("-dir"))){
                        System.out.println("ERROR: " + args[i] + " is not a valid option. Please refer to the -help menu for valid options:");
                        help();
                        return false;
                    }
                    if (args[i].equals("-ext") || args[i].equals("-dir")){
                        is_option = false; // turn it off because next arg should be the specified extensions
                        if (args[i].equals("-ext")){
                            is_ext = true;
                        }
                        else{
                            is_dir = true;
                        }
                        if (i == args.length - 1){
                            System.out.println("ERROR: " + args[i] + " requires arguments to be specified but none were found");
                            help();
                            return false;
                        }
                    }
                    else if (args[i].equals("-reg")){
                        try{
                            String pattern = args[0];
                            Pattern.compile(pattern);
                        }
                        catch(PatternSyntaxException e){
                            System.out.println("ERROR: Invalid regex expression");
                            help();
                            return false;
                        }
                    }
                }
                else{ // expecting parameters for the options
                    if (args[i].equals("-r") || args[i].equals("-ext") || args[i].equals("-reg") || args[i].equals("-dir")){
                        System.out.println("ERROR: " + args[i-1] + " requires arguments to be specified but " + args[i] + " was found");
                        help();
                        return false;
                    }
                    if (is_dir){
                        String startingdir = new File("").getAbsolutePath();
                        File d = new File(startingdir +"/" + args[i]);
                        if (!d.isDirectory()){
                            System.out.println("ERROR: " + args[i] + " is not a valid directory");
                            help();
                            return false;
                        }
                        is_dir = false;
                    }
                    is_ext = false;
                    is_option = true;
                }
            }
            return true;
        }
    }

    public static boolean check_extensions_reg(String file, String[] extensions){
        File f = new File(file);
        return (Arrays.stream(extensions).anyMatch(entry -> file.endsWith(entry))) && f.isFile();
    }


    public static boolean is_directory(String s){
        File f = new File(s);
        return f.isDirectory();
    }

    public static void find_file(String filetofind, String directory, HashMap<String, String> options, String[] extensions){
        // looks for file in current directory and does a bfs search in the subdirectories if file isnt found
        File dir = new File(directory);
        System.out.println("=============== Looking for file(s) in directory: " + dir.getAbsolutePath() + " ===============");
        String[] list_files_dir = dir.list();

        Vector<String> copy = new Vector<String>(Arrays.asList(list_files_dir));
        if (!options.containsKey("-ext") && !options.containsKey("-reg")){
            copy.removeIf(f -> !(f.equals(filetofind)));
        }

        if (options.containsKey("-ext")){
            if (options.containsKey("-reg")){
                copy.removeIf(f -> !check_extensions_reg(directory + "/" + f, extensions));
            }
            else{
                copy.removeAllElements();
                for (int i = 0; i < extensions.length; i++){
                    File f = new File(directory + "/" + filetofind + extensions[i]);
                    if (f.isFile()){
                        copy.add(filetofind + extensions[i]);
                    }
                }
            }
        }
        if (options.containsKey(("-reg"))){
            copy.removeIf(f -> !f.matches(filetofind));
        }

        if (copy.size() > 0){
            for (int i = 0; i < copy.size(); i++) {
                File f = new File(directory + "/" + copy.get(i));
                System.out.println(f.getAbsolutePath());
            }
        }
        else{
            System.out.println("No files found in this directory");
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
        if (verify_input(args) == false){ // verify that the command line inputs are valid
            return;
        }
        String filetofind = args[0];
        HashMap<String, String> options = new HashMap<String, String>();
        String startingDir = new File("").getAbsolutePath();
        String[] extensions = {};
        for(int i = 1; i < args.length; i++) {
            if (args[i].equals("-r")) {
                options.put("-r", "");
            } else if (args[i].equals("-reg")) {
                options.put("-reg", filetofind);
            } else if (args[i].equals("-dir")) {
                String chosen_dir = args[i+1];
                try{
                    startingDir = new File(startingDir + "/" + chosen_dir).getCanonicalPath();
                    options.put("-dir", startingDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
