package lab.practice2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class FileTypeParser {
    private static final Map<String, String> FILE_TYPE_MAP = Map.of("89504e47", "png", "504b0304", "zip or jar", "cafebabe", "class");

    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }

        String filename = args[0];
        String fileType = getFileType(filename);
        System.out.println("Filename: " + filename);
        System.out.printf("File Header(Hex): [%s]%n", fileType);
        System.out.println("File Type: " + FILE_TYPE_MAP.getOrDefault(fileType.replace(" ", "").toLowerCase(), "Unknown"));
    }

    private static String getFileType(String filename) {
        try (InputStream inputStream = FileTypeParser.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                return "Unknown";
            }

            byte[] header = new byte[4];
            int read = inputStream.read(header);
            if (read != 4) {
                return "Unknown";
            }

            StringBuilder hexHeader = new StringBuilder();
            for (byte b : header) {
                hexHeader.append(String.format("%02x", b));
            }
            return hexHeader.toString();
        } catch (IOException e) {
            return "Unknown";
        }
    }
}