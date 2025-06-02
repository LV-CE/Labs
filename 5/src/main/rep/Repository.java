package main.rep;
import java.io.File;

public interface Repository<T> {
    void outputArray(T[] array, File file);
    void outputArray(T[] array, String fileName);
    T[] readArray(File file);
    T[] readArray(String fileName);
}