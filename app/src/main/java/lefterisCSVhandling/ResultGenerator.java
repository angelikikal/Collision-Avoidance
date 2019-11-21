package lefterisCSVhandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResultGenerator {

    private static final String CSV_FILE_PATH
            = "C:\\Users\\lefte\\Desktop\\JAVA PROJECTS\\FileViaSocketClient\\src\\Resources";


    static int LINES_OF_DATA;
    static final int NO_OF_COLUMNS = 14;
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

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
        int i;
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

            /*for (double[] row : doubleValues) {
                for (double cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }*/

            double[] entropies = new double[NO_OF_COLUMNS];
            double[] column_buffer = new double[LINES_OF_DATA];


            for (int column = 0; column < NO_OF_COLUMNS; column++) {
                for (int row = 0; row < LINES_OF_DATA; row++) {
                    column_buffer[row] = doubleValues[row][column];
                }
                entropies[column] = Entropy.calculateEntropy(column_buffer);
            }

            /*System.out.println("\n");
            for (i = 0; i < NO_OF_COLUMNS; i++) {
                System.out.println("Entropies are : " + entropies[i]);
            }*/

        /*double feature_vector = calculateEntropy(entropies);
        System.out.println("\nFeature vector is: "+feature_vector);*/


            feature_vector = new Vector(NO_OF_COLUMNS);
            for (int k = 0; k < NO_OF_COLUMNS; k++) {
                feature_vector.addElement(entropies[k]);
            }
            System.out.println(feature_vector);

        }

        for (int l = 0; l < listOfFiles.length; l++) {
            writeNewCSV(listOfFiles,feature_vector);
        }

        // CSV_file_writer(fileToSend, extractFilename(), feature_vector);
    }

    private static void writeNewCSV(File[] listOfFiles,Vector feature_vector) throws FileNotFoundException {
        final String fileToCreate = "C:\\Users\\lefte\\Desktop\\JAVA PROJECTS\\FileViaSocketClient\\FilesGenerated\\Training_Set.csv";
        PrintWriter pw = new PrintWriter(new File(fileToCreate));
        for (int i = 0; i < listOfFiles.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(extractFilename(listOfFiles[i].getAbsolutePath()));
            sb.append(',');
            sb.append(feature_vector);
            sb.append('\n');
            pw.write(sb.toString());
        }
        pw.close();
    }

    private static String extractFilename(String path) {
        Path p = Paths.get(path);
        String filename = p.getFileName().toString();
        final String name_of_experiment = filename.substring(filename.indexOf(".") + 1, filename.indexOf("d") + 1);
        return name_of_experiment;
    }

}
