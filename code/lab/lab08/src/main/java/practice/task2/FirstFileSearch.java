package practice.task2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * @author Yuxian Wu
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-11-05 11:48
 */
public class FirstFileSearch {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String filePath = "E:\\courses\\CS209A-ComputerSystemDesignAndApplicationsA\\code\\lab\\lab08\\src\\main\\resources\\src";
        System.out.println("Enter keyword (e.g. volatile): ");
        String wordToFind = scanner.next();
        Path directory = Paths.get(filePath);

        if (!Files.isDirectory(directory)) {
            System.out.println("Provided path is not a directory.");
            return;
        }

        try {
            String resultFilePath = searchForFileWithWord(wordToFind, directory);
            System.out.println("Found in file: " + resultFilePath);
        } catch (NoSuchElementException e) {
            System.out.println("No file contains the word '" + wordToFind + "'.");
        } catch (InterruptedException e) {
            System.out.println("Search was interrupted.");
        }
    }

    private static String searchForFileWithWord(String wordToFind, Path directory) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<String>> tasks = new ArrayList<>();

        try {
            Files.walk(directory)
                    .forEach(file -> tasks.add(() -> searchInFile(file, wordToFind)));
            String path = executor.invokeAny(tasks);
            if (executor instanceof ThreadPoolExecutor) {
                System.out.println("Using CachedThreadPool");
                System.out.println("Largest pool size: "
                        + ((ThreadPoolExecutor) executor).getLargestPoolSize());
            }
            return path;
        } catch (IOException | ExecutionException e) {
            System.out.println("Error reading files: " + e.getMessage());
            return null;
        } finally {
            executor.shutdown();
        }
    }

    private static String searchInFile(Path file, String word) {
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("Search in " + file + " canceled.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(word)) {
                    return file.toString();
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Search in " + file + " canceled.");
                    return null;
                }
            }
        } catch (IOException e) {
            throw new NoSuchElementException();
        }

        throw new NoSuchElementException();
    }
}