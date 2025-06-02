package main.rep.impl;
import main.resource.Product;
import main.rep.ProductRep;
import java.io.*;

public class ProductBinaryRepository implements ProductRep {
    @Override
    public void saveToFile(Product[] array, File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(array);
        } catch (IOException e) {
            System.out.println("Error writing to binary file: " + e.getMessage());
        }
    }
    @Override
    public Product[] loadFromFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Product[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from binary file: " + e.getMessage());
        }
        return new Product[0];
    }
    @Override
    public void outputArray(Product[] array, File file) {
        saveToFile(array, file);
    }
    @Override
    public void outputArray(Product[] array, String fileName) {
        outputArray(array, new File(fileName));
    }
    @Override
    public Product[] readArray(File file) {
        return loadFromFile(file);
    }
    @Override
    public Product[] readArray(String fileName) {
        return readArray(new File(fileName));
    }
}