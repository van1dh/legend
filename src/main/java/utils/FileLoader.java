package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to load data files into string arrays.
 */
public class FileLoader {

    public static List<String[]> loadFile(String filePath) {
        List<String[]> parsedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;
                if (isFirstLine) { isFirstLine = false; continue; } // skip header
                parsedLines.add(line.split("\\s+"));
            }

        } catch (IOException e) {
            System.err.println("Error loading file: " + filePath);
            e.printStackTrace();
        }

        return parsedLines;
    }
}
