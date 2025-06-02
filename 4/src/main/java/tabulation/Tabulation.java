package tabulation;

public class Tabulation {
    private final double a = 20.3;
    public double fun(double x) {
        if (x > 1.2) {
            return Math.log10(x + 1);
        } else {
            return Math.pow(Math.sin(Math.sqrt(a * x)), 2);
        }
    }
    public double[] generateXArray(double start, double end, double step) {
        int count = countStep(start, end, step);
        double[] x = new double[count];
        for (int i = 0; i < count; i++) {
            x[i] = start + i * step;
        }
        return x;
    }
    public double[] generateYArray(double[] x) {
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            y[i] = fun(x[i]);
        }
        return y;
    }
    public int countStep(double start, double end, double step) {
        return (int) Math.round((end - start) / step) + 1;
    }
    public int indexOfMin(double[] array) {
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[index]) index = i;
        }
        return index;
    }
    public int indexOfMax(double[] array) {
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[index]) index = i;
        }
        return index;
    }
    public int minElement(double[] array) {
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }
    public int maxElement(double[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    public double sum(double[] array) {
        double sum = 0;
        for (double v : array) sum += v;
        return sum;
    }
    public double average(double[] array) {
        return sum(array) / array.length;
    }
}
