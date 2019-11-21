package EntropyCalc;// Java program to illustrate
// for Writing Data in CSV file

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import lefterisCSVhandling.Entropy;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ResultGenerator {

    private static final String CSV_FILE_PATH
            = "C:\\Users\\lefte\\Desktop\\JAVA PROJECTS\\FileViaSocketClient\\src\\Resources";

    private static final String fileToCreate = "C:\\Users\\lefte\\Desktop\\JAVA PROJECTS\\FileViaSocketClient\\FilesGenerated\\Training_Set.csv";

    static int LINES_OF_DATA;
    static final int NO_OF_COLUMNS = 14;
    static final int ROWS_TRAINING_SET = 36;
    private static final String COMMA_DELIMITER = ",";

    public static Vector feature_vector;

    public static File[] listOfFiles;


    private static List<String[]> processInputFile(String inputFilePath) {
        List<String[]> inputList = new ArrayList<String[]>();
        try {
            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputList;
    }


    private static Function<String, String[]> mapToItem = (line) -> {
        String[] p = line.split(COMMA_DELIMITER);
        String[] item = new String[NO_OF_COLUMNS];

        for (int i = 0; i < NO_OF_COLUMNS; i++) {
            if (p[i] != null && p[i].trim().length() > 0) {
                item[i] = p[i];
            }
        }
        return item;
    };


    public static void main(String[] args) throws IOException {

        File folder = new File(CSV_FILE_PATH);
        listOfFiles = folder.listFiles();
        int i;


        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }

        FileWriter fileWriter = new FileWriter(fileToCreate);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (i = 0; i < listOfFiles.length; i++) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(listOfFiles[i].getAbsolutePath()));
                LINES_OF_DATA = (int) Files.lines(Paths.get(listOfFiles[i].getAbsolutePath())).skip(1L).count();
                System.out.println("file has " + LINES_OF_DATA + " lines");
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            List<String[]> arrayCSV = processInputFile(listOfFiles[i].getAbsolutePath());

            double[][] doubleValues = new double[LINES_OF_DATA][NO_OF_COLUMNS];

            for (int k = 0; k < LINES_OF_DATA; k++) {
                for (int j = 0; j < NO_OF_COLUMNS; j++) {
                    doubleValues[k] = Arrays.stream(arrayCSV.get(k))
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                }
            }

            double[] entropies = new double[NO_OF_COLUMNS];
            double[] column_buffer = new double[LINES_OF_DATA];


            for (int column = 0; column < NO_OF_COLUMNS; column++) {
                for (int row = 0; row < LINES_OF_DATA; row++) {
                    column_buffer[row] = doubleValues[row][column];
                }
                entropies[column] = Entropy.calculateEntropy(column_buffer);
            }

            feature_vector = new Vector(NO_OF_COLUMNS);
            for (int k = 0; k < NO_OF_COLUMNS; k++) {
                feature_vector.addElement(entropies[k]);
            }

            System.out.println("\n");

            String sb = extractFilename(listOfFiles[i].getAbsolutePath()) + ',' +
                    extractFVvalue(feature_vector) + '\n';
            printWriter.write(sb);
        }
        printWriter.close();


        try {
            BufferedReader br = new BufferedReader(new FileReader(fileToCreate));
            List<String> lines = Files.readAllLines(Paths.get(fileToCreate), StandardCharsets.UTF_8);
            String[] training_set = lines.toArray(new String[lines.size()]);

            String[][] label_dis = new String[ROWS_TRAINING_SET][2];

            String[] res;
            for (int s = 0; s < ROWS_TRAINING_SET; s++) {
                res = training_set[s].split(",", 2);
                if (res[0].equals("EyesOpened")) {
                    label_dis[s][0] = "1";
                }
                if (res[0].equals("EyesClosed")) {
                    label_dis[s][0] = "0";
                }
                label_dis[s][1] = res[1];
            }

            for (int d = 0; d < ROWS_TRAINING_SET; d++) {
                for (int c = 0; c < 2; c++) {
                    System.out.print(label_dis[d][c] + "\t\t");
                }
                System.out.println();
            }


            System.out.println("----------------------------------");


            for (int d = 0; d < training_set.length; d++) {
                training_set[d] = training_set[d].substring(11);
            }

            for (String row : training_set) {
                System.out.print(row + "\n");
            }


            System.out.println("----------------------------------");

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------");

    }

    private static String extractFilename(String path) {
        Path p = Paths.get(path);
        String filename = p.getFileName().toString();
        return filename.substring(filename.indexOf(".") + 1, filename.indexOf("d") + 1);
    }

    private static String extractFVvalue(Vector feature_vector) {
        String temp = feature_vector.toString();
        return temp.substring(temp.indexOf("[") + 1, temp.indexOf("]"));
    }

    public static double distance(double X, double x) {
        double res = pow((X - x), 2.0);
        return sqrt(res);
    }

}