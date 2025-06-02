package main.service;

import main.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

  private Service service;

  @BeforeEach
  public void setup() {
    service = new Service();

    service.addProduct(new Product(1, "IPHONE", 25.0, LocalDate.of(2024, 1, 1), "Телефон", "APPLE", 14, "1412317"), true);
    service.addProduct(new Product(2, "IPHONE 2", 112.0, LocalDate.of(2024, 12, 1), "Телефон", "APPLE", 18, "5940245"), true);
    service.addProduct(new Product(3, "SAMSUNGE", 12.5, LocalDate.of(2025, 5, 1), "Планшет", "SAMSUNGE", 133, "5838928"), true);
    service.addProduct(new Product(4, "PLAYSTATIONE 12", 30.0, LocalDate.of(2025, 4, 1), "Ігрова Приставка", "SONYA", 15, "9274812"), true);
  }

  @AfterEach
  void cleanup() throws Exception
  {
    Files.deleteIfExists(Path.of("test.txt"));
    Files.deleteIfExists(Path.of("test.bin"));
  }

  @Test
  public void testAddProductAndIDGeneration() {
    List<Product> all = service.getAllProducts(true);
    assertEquals(4, all.size());
    assertEquals(1, all.get(0).getId());
    assertEquals("IPHONE", all.get(0).getName());
  }

  @Test
  public void testRemoveProduct() {
    boolean removed = service.removeProductById(3, true);
    assertTrue(removed);
    assertEquals(3, service.getAllProducts(true).size());
  }

  @Test
  public void testFindByName() {
    service.findByName("IPHONE");
    List<Product> result = service.getAllProducts(true);
    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(p -> p.getName().equals("IPHONE 2")));
  }

  @Test
  public void testFindByPrice() {
    service.findByPrice(30.0);
    List<Product> result = service.getAllProducts(true);
    assertEquals(3, result.size()); // IPHONE, SAMSUNGE, PLAYSTATIONE 12
  }

  @Test
  public void testFindByReceiptDate() {
    service.findByReceiptDate(LocalDate.of(2025, 1, 1));
    List<Product> result = service.getAllProducts(true);
    assertEquals(2, result.size()); // IPHONE, IPHONE 2
  }

  @Test
  public void testFindByType() {
    service.findByType("телефон");
    List<Product> result = service.getAllProducts(true);
    assertEquals(2, result.size()); // IPHONE, IPHONE 2
  }

  @Test
  public void testFindByManufacturer() {
    service.findByManufacturer("apple");
    List<Product> result = service.getAllProducts(true);
    assertEquals(2, result.size());
  }

  @Test
  public void testFindBySerialNumber() {
    service.findBySerialNumber("9274812");
    List<Product> result = service.getAllProducts(true);
    assertEquals(1, result.size());
    assertEquals("PLAYSTATIONE 12", result.get(0).getName());
  }

  @Test
  public void testSortByReceiptDate() {
    List<Product> sorted = service.sortByReceiptDate();
    assertEquals("IPHONE", sorted.get(0).getName());
    assertEquals("SAMSUNGE", sorted.get(3).getName());
  }

  @Test
  public void testSortByManufacturer() {
    List<Product> sorted = service.sortByManufacturer();
    assertEquals("APPLE", sorted.get(0).getManufacturer());
    assertEquals("SONYA", sorted.get(3).getManufacturer());
  }

  @Test
  public void testSortByNameSortedByReceipt() {
    List<Product> sorted = service.sortByNameSortedByReceipt();
    assertEquals("IPHONE", sorted.get(0).getName());
    assertEquals("IPHONE 2", sorted.get(1).getName());
  }

  @Test
  public void testMostExpensiveByName() {
    List<Product> expensive = service.mostExpensiveByName();
    assertEquals(4, expensive.size());
    assertTrue(expensive.stream().anyMatch(p -> p.getName().equals("IPHONE 2") && p.getPrice() == 112.0));
  }

  @Test
  public void testTextSaveLoad() {
    assertTrue(service.saveToTextFile("test.txt", true));
    assertTrue(service.loadFromTextFile("test.txt", true));
  }

  @Test
  public void testBinarySaveLoad() {
    assertTrue(service.saveToBinFile("test.dat", true));
    assertTrue(service.loadFromBinFile("test.dat", true));
  }
}