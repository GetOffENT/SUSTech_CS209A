package lab.practice1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameMapProcessor {

    public static void main(String[] args) {
        String inputFileName = "input.txt";  // 资源文件名
        String outputFileName = "output.txt"; // 输出文件名

        try {
            // 读取输入地图
            StringBuilder mapBuilder = new StringBuilder();
            try (InputStream inputStream = GameMapProcessor.class.getClassLoader().getResourceAsStream(inputFileName);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    mapBuilder.append(line).append(System.lineSeparator());
                }
            }

            // 处理符号
            String processedMap = processMap(mapBuilder.toString());

            // 写入处理后的地图到输出文件
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("lab05/src/main/resources/" + outputFileName), java.nio.charset.StandardCharsets.UTF_16)) {
                writer.write(processedMap);
            }

            System.out.println("Map processed and written to src/main/resources/" + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processMap(String map) {
        StringBuilder processedMap = new StringBuilder();

        // 使用 codePoints 方法处理每个字符
        map.codePoints().forEach(codePoint -> {
            switch (codePoint) {
                case 0x007E: //水 ~
                    processedMap.appendCodePoint(0x2744);
                    break;
                case 0x1F332:
                    processedMap.appendCodePoint(0x2B1C);
                    break;
                case 0x26F0:
                    processedMap.appendCodePoint(0x26F0);
                    break;
                default:
                    processedMap.appendCodePoint(codePoint);
            }
        });

        return processedMap.toString();
    }
}