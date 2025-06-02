package tabulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class TabulationTest {
    private final double EPS = 1e-9;
    private Tabulation tab;
    @BeforeEach
    void setUp() {
        tab = new Tabulation();
    }
    @Test
    void testGenerateXArrayBounds() {
        double[] x = tab.generateXArray(0.5, 2.0, 0.005);
        assertEquals(0.5, x[0], EPS, "x[0] має бути 0.5");
        assertEquals(1.2, x[140], EPS, "x[140] має бути 1.2");
        assertEquals(2.0, x[300], EPS, "x[300] має бути 2.0");
    }
    @Test
    void testGenerateYArrayValues() {
        double[] x = tab.generateXArray(0.5, 2.0, 0.005);
        double[] y = tab.generateYArray(x);
        assertEquals(tab.fun(x[0]), y[0], EPS, "y[0] має відповідати fun(x[0])");
        assertEquals(tab.fun(x[140]), y[140], EPS, "y[140] має відповідати fun(x[140])");
        assertEquals(tab.fun(x[300]), y[300], EPS, "y[300] має відповідати fun(x[300])");
    }
    @Test
    void testCountSteps() {
        int steps = tab.countStep(0.5, 2.0, 0.005);
        assertEquals(301, steps, "Кількість кроків має бути 301");
    }
    @ParameterizedTest
    @CsvSource({
            "0, 0.5",
            "140, 1.2",
            "300, 2.0"
    })
    void testGenerateXArrayAtIndex(int index, double expected) {
        double[] x = tab.generateXArray(0.5, 2.0, 0.005);
        assertEquals(expected, x[index], EPS, "Значення x[" + index + "] не співпадає");
    }
    @ParameterizedTest
    @CsvSource({
            "0",
            "140",
            "300"
    })
    void testFunConsistencyAtIndices(int index) {
        double[] x = tab.generateXArray(0.5, 2.0, 0.005);
        double[] y = tab.generateYArray(x);
        assertEquals(tab.fun(x[index]), y[index], EPS, "y[" + index + "] не співпадає з fun(x[" + index + "])");
    }
    @Test
    void testMinElement() {
        double[] x = tab.generateXArray(0.5, 2.0, 0.005);
        double[] y = tab.generateYArray(x);
        int minIndex = tab.minElement(y);
        assertTrue(minIndex >= 0 && minIndex < y.length, "Індекс мінімального елемента в межах масиву");
    }
    @Test
    void testMaxElement() {
        double[] x = tab.generateXArray(0.5, 2.0, 0.005);
        double[] y = tab.generateYArray(x);
        int maxIndex = tab.maxElement(y);
        assertTrue(maxIndex >= 0 && maxIndex < y.length, "Індекс максимального елемента в межах масиву");
    }
    @Test
    void testSumAndAverage() {
        double[] x = tab.generateXArray(0.5, 2.0, 0.005);
        double[] y = tab.generateYArray(x);
        double sum = tab.sum(y);
        double avg = tab.average(y);
        assertTrue(sum > 0, "Сума має бути більше 0");
        assertTrue(avg > 0, "Середнє має бути більше 0");
        assertEquals(sum / y.length, avg, EPS, "Середнє має дорівнювати сумі поділеній на довжину");
    }
}
