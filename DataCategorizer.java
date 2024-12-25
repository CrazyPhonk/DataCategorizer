import java.io.*;

public class DataCategorizer {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java DataCategorizer <-s> <-f> <-a> <-p prefix> <-o> <input_file1> <input_file2> ...");
            return;
        }

        String[] strParts;
        String inputFilename;
        int argsnum = 0;
        int tmpStrCount = 0;
        String outputDir = ".";
        String prefix = "";
        int numofAllEl, intnum = 0, floatnum = 0, strnum = 0;
        boolean append = true;
        boolean shortStats = false;
        boolean fullStats = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                outputDir = args[++i];
                argsnum += 2;
            } else if (args[i].equals("-p")) {
                prefix = args[++i];
                argsnum+=2;
            } else if (args[i].equals("-a")) {
                append = false;
                argsnum++;
            } else if (args[i].equals("-s")) {
                shortStats = true;
                argsnum++;
            } else if (args[i].equals("-f")) {
                fullStats = true;
                argsnum++;
            }
        }

        String intFilename = outputDir + "/" + prefix + "integers.txt";
        String floatFilename = outputDir + "/" + prefix + "floats.txt";
        String stringFilename = outputDir + "/" + prefix + "strings.txt";
        try{
            File intf = new File(intFilename);
            if (!intf.exists()) {
                intf.createNewFile();
            }
            File floatf = new File(intFilename);
            if (!floatf.exists()) {
                floatf.createNewFile();
            }
            File stringf = new File(intFilename);
            if (!stringf.exists()) {
                stringf.createNewFile();
            }
        }
        catch(IOException e){
            System.out.println("Error:" + e.getMessage());
        }
        try (
                PrintWriter integers = new PrintWriter(new FileWriter(intFilename, !append));
                PrintWriter floats = new PrintWriter(new FileWriter(floatFilename, !append));
                PrintWriter strings = new PrintWriter(new FileWriter(stringFilename, !append));
        ) {

            for (int i = argsnum; i < args.length; i++){
                System.out.println(args[i]);
                inputFilename = args[i];
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        strParts = line.split(" ");
                        for (String word : strParts){
                        if(line.isEmpty()){
                            continue;
                        }
                        if (isInteger(word)) {
                            integers.println(word);
                            intnum += 1;
                        } else if (isFloat(word)) {
                            floats.println(word);
                            floatnum += 1;
                        } else {
                            strings.println(word);
                            tmpStrCount = line.split(" ").length;
                            strnum += tmpStrCount;
                            tmpStrCount = 1;
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error processing file " + inputFilename + ": " + e.getMessage());
                    return;
                }
            }
            integers.close();
            floats.close();
            strings.close();
        } catch (IOException e) {
            System.err.println("Error creating output files: " + e.getMessage());
            return;
        }
        System.out.println("Data categorized successfully.");
        if (stat(shortStats, fullStats, intnum, floatnum, strnum) < 0){
            System.out.println("Error stat func");
            return;
        }
    }

    private static boolean isFloat(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    static int findMax(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    static int findMin(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    private static int stat(boolean shortStats, boolean fullStats, int intnum, int floatnum, int strnum){
        long sum = 0;
        int avg = 0;
        int numofAllEl = intnum + floatnum + strnum;
        if (shortStats == true && fullStats == false){
            System.out.println("Nunber of elements" + numofAllEl);
            return 1;
        }
        else if (shortStats == false && fullStats == true){
            System.out.println("Nunber of int elements " + intnum);
            System.out.println("Nunber of float elements " + floatnum);
            System.out.println("Nunber of String elements " + strnum);
            System.out.println("Max num " + findMax(intnum,floatnum,strnum));
            System.out.println("Min num " + findMin(intnum,floatnum,strnum));
            sum = intnum + floatnum + strnum;
            System.out.println("Sum "+ sum);
            avg = (int)(sum / 3);
            System.out.println("Average " + avg);
            return 0;
        }
        else {
            System.out.println("Error: -s and -f together");
            return -1;
        }
    }
}
