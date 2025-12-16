package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to load data files into string arrays.
 * Supports different file formats with whitespace or tab separation.
 */
public class FileLoader {

    /**
     * Load a file and return list of parsed lines (skip header and empty lines).
     *
     * @param filePath path to the data file
     * @return list of string arrays, each representing one data row
     */
    public static List<String[]> loadFile(String filePath) {
        List<String[]> parsedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) {
                    continue;
                }

                // Skip header line (first non-comment line)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Split by whitespace (handles multiple spaces/tabs)
                String[] parts = line.split("\\s+");
                parsedLines.add(parts);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            System.err.println("Please ensure data files are in the correct location.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return parsedLines;
    }

    /**
     * Load a file with specific expected column count (for validation).
     *
     * @param filePath path to the data file
     * @param expectedColumns expected number of columns
     * @return list of string arrays
     */
    public static List<String[]> loadFile(String filePath, int expectedColumns) {
        List<String[]> data = loadFile(filePath);

        // Validate column count
        for (String[] row : data) {
            if (row.length != expectedColumns) {
                System.err.println("Warning: Expected " + expectedColumns +
                        " columns but found " + row.length +
                        " in file: " + filePath);
            }
        }

        return data;
    }

    /**
     * Alternative method name for compatibility.
     */
    public static List<String[]> load(String filePath, int expectedColumns) {
        return loadFile(filePath, expectedColumns);
    }
}
