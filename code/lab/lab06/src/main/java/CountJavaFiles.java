import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Yuxian Wu
 * @version 1.0
 * @Description: TODO
 * @Create: 2024-10-22 10:53
 */
public class CountJavaFiles {
    public static void main(String[] args) {
        String srcZipPath = "E:\\courses\\CS209A-ComputerSystemDesignAndApplicationsA\\code\\lab\\lab06\\src\\main\\resources\\src.zip"; // 更新为 src.zip 的实际路径
        String jarFilePath = "E:\\courses\\CS209A-ComputerSystemDesignAndApplicationsA\\code\\lab\\lab06\\src\\main\\resources\\rt.jar"; // 更新为 rt.jar 的实际路径

        Set<String> javaFiles = new HashSet<>();
        Set<String> classFiles = new HashSet<>();
        Set<String> innerClassFiles = new HashSet<>();

        countJavaFiles(srcZipPath, javaFiles);

        countClassFiles(jarFilePath, classFiles, innerClassFiles);

        System.out.println("In .zip: # of .java files in java.io/java.nio packages: " + javaFiles.size());
        System.out.println("In .jar: # of .class files in java.io/java.nio packages: " + classFiles.size());

        System.out.println();
        System.out.println("# of .class files for inner classes: " + innerClassFiles.size());

        classFiles.removeAll(innerClassFiles);


        findMatchingFiles(javaFiles, classFiles);
    }

    private static void countJavaFiles(String srcZipPath, Set<String> javaFiles) {
        try (ZipFile zipFile = new ZipFile(srcZipPath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if ((entryName.startsWith("java/io/") || entryName.startsWith("java/nio/")) && entryName.endsWith(".java")) {
                    String className = entryName.replace('/', '.').replace(".java", "");
                    System.out.println(className);
                    javaFiles.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void countClassFiles(String jarFilePath, Set<String> classFiles, Set<String> innerClassFiles) {
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();

                if ((entryName.startsWith("java/io/") || entryName.startsWith("java/nio/")) && entryName.endsWith(".class")) {
                    String className = entryName.replace('/', '.').replace(".class", "");
                    System.out.println(className);
                    if (className.contains("$")) {
                        innerClassFiles.add(className);
                    }
                    classFiles.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void findMatchingFiles(Set<String> javaFiles, Set<String> classFiles) {
        int matchingCount = 0;


        for (String classFile : classFiles) {
            if (javaFiles.contains(classFile)) {
                matchingCount++;
            }
        }
        System.out.println("# of .java files with corresponding .class: " + matchingCount);
        System.out.println();


        List<String> onlyClass = new ArrayList<>();

        for (String classFile : classFiles) {
            if (!javaFiles.contains(classFile)) {
                onlyClass.add(classFile);
            }
        }

        List<String> onlyJava = new ArrayList<>();
        for (String javaFile : javaFiles) {
            if (!classFiles.contains(javaFile)) {
                onlyJava.add(javaFile);
            }
        }

        System.out.println("# of .java without its .class: " + onlyJava.size());
        onlyJava.forEach(System.out::println);
        System.out.println();
        System.out.println("# of .class without its .java: " + onlyClass.size());
        onlyClass.forEach(System.out::println);
    }
}