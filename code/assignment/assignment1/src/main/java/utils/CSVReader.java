package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

    public static <T> List<T> mapCsvToClass(String filePath, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<String> headers = Arrays.asList(br.readLine().split(","));
            boolean inQuotes = false;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                List<String> row = new ArrayList<>();

                do{
                    if (inQuotes) {
                        line = br.readLine();
                    }
                    for (char c : line.toCharArray()) {
                        if (c == '"') {
                            inQuotes = !inQuotes;
                        } else if (c == ',' && !inQuotes) {
                            row.add(sb.toString().trim());
                            sb.setLength(0);
                        } else {
                            sb.append(c);
                        }
                    }
                    if (sb.length() > 0) {
                        row.add(sb.toString().trim());
                        sb.setLength(0);
                    }
                }while (inQuotes);

                T obj = clazz.getDeclaredConstructor().newInstance();
                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    String value = j < row.size() ? row.get(j) : null;
                    Field field = clazz.getDeclaredField(header);
                    field.setAccessible(true);

                    field.set(obj, convertValue(value, field.getType()));
                }
                resultList.add(obj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    private static Object convertValue(String value, Class<?> type) {
        if (type == String.class) return value;
        if (type == int.class || type == Integer.class) return (value != null && !value.isEmpty()) ? Integer.valueOf(value) : null;
        if (type == float.class || type == Float.class) return (value != null && !value.isEmpty()) ? Float.valueOf(value) : null;
        if (type == boolean.class || type == Boolean.class) return (value != null && !value.isEmpty()) ? Boolean.valueOf(value) : null;

        return null;
    }
}