package ch.fetm.backuptools.common;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Admin on 28.03.14.
 */
public class FileSystemTools {
    public static void eraseDirectory(String directory){
        SimpleFileVisitor<Path> test = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {

                System.out.println("Deleting file: " + file);
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

                System.out.println("Deleting dir: " + dir);
                if (exc == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    throw exc;
                }
            }
        };

        if (Files.exists(Paths.get(directory)))
            try {
                Files.walkFileTree(Paths.get(directory), test);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
