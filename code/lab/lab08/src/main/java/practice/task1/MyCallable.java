package practice.task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * @author Yuxian Wu
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-11-05 11:06
 */
public class MyCallable implements Callable<Long> {
    private String word;
    private File file;

    public MyCallable(String word, File file) {
        this.word = word;
        this.file = file;
    }

    /**
     * count the indicated word in the file
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Long call() throws Exception {
        long count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                count += countWordInLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
    }
        return count;
    }

    private long countWordInLine(String line) {
        String[] words = line.split(" ");
        long count = 0;
        for (String w : words) {
            if (w.equals(word)) {
                count++;
            }
        }
        return count;
    }
}
