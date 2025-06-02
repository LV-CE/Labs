package main;
import tabulation.Tabulation;
import task.Task;

public class Main {
    public static void main(String[] args) {
        Tabulation tab = new Tabulation();
        double[] xArray = tab.generateXArray(0.5, 2.0, 0.005);
        double[] yArray = tab.generateYArray(xArray);
        int minIndex = tab.indexOfMin(yArray);
        int maxIndex = tab.indexOfMax(yArray);
        System.out.printf("Min єлементи: %d, x: %.4f,  y: %.4f\n", minIndex, xArray[minIndex], yArray[minIndex]);
        System.out.printf("Max єлементи: %d, x: %.4f,  y: %.4f\n", maxIndex, xArray[maxIndex], yArray[maxIndex]);
        System.out.printf("Сумма = %.5f, Середнє = %.5f%n", tab.sum(yArray), tab.average(yArray));
        Task.processText();
    }
}
