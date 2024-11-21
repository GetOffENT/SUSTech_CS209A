package practice.task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Yuxian Wu
 * @version 1.0
 * @Description:
 * @Create: 2024-11-05 11:08
 */
public class CountOccurrence {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String filePath = "E:\\courses\\CS209A-ComputerSystemDesignAndApplicationsA\\code\\lab\\lab08\\src\\main\\resources\\src";
        System.out.println("Enter keyword (e.g. volatile): ");
        String wordToCount = scanner.next();
        Path directory = Paths.get(filePath);

        if (!Files.isDirectory(directory)) {
            System.out.println("Provided path is not a directory.");
            return;
        }

        measurePerformanceWithFixedThreadPool(wordToCount, directory);
        measurePerformanceWithCachedThreadPool(wordToCount, directory);
        measurePerformanceWithSingleThreadExecutor(wordToCount, directory);
    }

    private static void measurePerformanceWithFixedThreadPool(String wordToCount, Path directory) {
        System.out.println("Using FixedThreadPool:");
        measurePerformance(Executors.newFixedThreadPool(2000), wordToCount, directory);
    }

    private static void measurePerformanceWithCachedThreadPool(String wordToCount, Path directory) {
        System.out.println("Using CachedThreadPool:");
        measurePerformance(Executors.newCachedThreadPool(), wordToCount, directory);
    }

    private static void measurePerformanceWithSingleThreadExecutor(String wordToCount, Path directory) {
        System.out.println("Using SingleThreadExecutor:");
        measurePerformance(Executors.newSingleThreadExecutor(), wordToCount, directory);
    }

    private static void measurePerformance(ExecutorService executor, String wordToCount, Path directory) {
        Instant startTime = Instant.now();

        List<Future<Long>> futures = new ArrayList<>();

        // Use Files.walk to get all files in the directory and subdirectories
        try {
            List<Path> files = Files.walk(directory)
                    .filter(Files::isRegularFile) // Filter to get only regular files
                    .collect(Collectors.toList());

            for (Path file : files) {
                MyCallable callable = new MyCallable(wordToCount, file.toFile());
                Future<Long> future = executor.submit(callable);
                futures.add(future);
            }

            long totalOccurrences = 0;
            for (Future<Long> future : futures) {
                try {
                    totalOccurrences += future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();

            Instant endTime = Instant.now();
            System.out.println("Occurrences of '" + wordToCount + "': " + totalOccurrences);
            System.out.println("Time elapsed: " + Duration.between(startTime, endTime).toMillis() + " ms");
            if (executor instanceof ThreadPoolExecutor) {
                System.out.println("Using CachedThreadPool");
                System.out.println("Largest pool size: "
                        + ((ThreadPoolExecutor) executor).getLargestPoolSize());
            }
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
    }
}