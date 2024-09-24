package task2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Will
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-09-24 11:48
 */
public class WordFrequency {
    public void wordFrequency(File file) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase().replaceAll("[^a-zA-Z]", "");
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }

            List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordFrequency.entrySet());
            sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

            System.out.println("Word : Count");
            for (int i = 0; i < 5 && i < sortedList.size(); i++) {
                Map.Entry<String, Integer> entry = sortedList.get(i);
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
    }
}
