package br.uefs.compiler.util.cache;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Cache to avoid redoing expensive computations.
 */
public class CacheHandler {

    private static Path TMP_DIR = Paths.get(System.getProperty("java.io.tmpdir"));
    private static File VALIDATION_FILE = Paths.get(TMP_DIR.toString(), "cache_validation.data").toFile();
    private static File CONTENT_FILE = Paths.get(TMP_DIR.toString(), "cache_content.data").toFile();

    public static boolean isCacheValid(String validationStr) throws IOException, ClassNotFoundException {
        if (!VALIDATION_FILE.exists()) {
            return false;
        } else {
            String prevValidationStr = String.class.cast(readObject(VALIDATION_FILE));
            return  prevValidationStr.equals(validationStr);
        }
    }

    public static void updateCache(String validationStr, Object content) throws IOException {
        writeObject(VALIDATION_FILE, validationStr);
        writeObject(CONTENT_FILE, content);
    }

    public static Object readCache() throws IOException, ClassNotFoundException {
        return readObject(CONTENT_FILE);
    }

    private static void writeObject(File file, Object obj) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    private static Object readObject(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }
}
