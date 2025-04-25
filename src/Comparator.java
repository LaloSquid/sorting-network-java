import java.util.ArrayList;

public class Comparator {
    int min;
    int max;

    public Comparator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int minChannel() {
        return this.min;
    }

    public int maxChannel() {
        return this.max;
    }

    public boolean isStandard() {
        return (this.minChannel() < this.maxChannel());
    }

    public static int[] apply(Comparator comparator, int[] inputArray) {
        int[] outputArray = inputArray.clone();
        int minIndex = comparator.minChannel();
        int maxIndex = comparator.maxChannel();
        if (inputArray[minIndex] > inputArray[maxIndex]) {
            outputArray[minIndex] = inputArray[maxIndex];
            outputArray[maxIndex] = inputArray[minIndex];
        }
        return outputArray;
    }

    public static ArrayList<Comparator> allComparators(int n) {
        ArrayList<Comparator> comparators = new ArrayList<>();
        for (int min = 0; min < n; min++) {
            for (int max = 0; max < n; max ++) {
                comparators.add(new Comparator(min, max));
            }
        }
        return comparators;
    }

    public static ArrayList<Comparator> óldStdComparators(int n) {
        ArrayList<Comparator> stdComparators = new ArrayList<>();
        for (Comparator comparator : allComparators(n)) {
            if (comparator.isStandard()) {
                stdComparators.add(comparator);
            }
        }
        return stdComparators;
    }

    public static Comparator[] stdComparators(int n) {
        int possibleComparators = (n * (n - 1)) / 2;
        Comparator[] stdComparators = new Comparator[possibleComparators];
        int i = 0;
        for (int min = 0; min < n; min++) {
            for (int max = 0; max < n; max ++) {
                if (min < max) {
                    stdComparators[i] = new Comparator(min, max);
                    i = i + 1;
                }
            }
        }
        return stdComparators;
    }

    public static String toProgram(Comparator comparator, String var) {
        int min = comparator.minChannel();
        int max = comparator.maxChannel();
        return new String(
            var + "[" + min + "], " + var + "[" + max + "] = ("
            + var + "[" + max + "], " + var + "[" + min + "]) if "
            + var + "[" + min + "] > " + var + "[" + max + "] else ("
            + var + "[" + min + "], " + var + "[" + max + "])"
            );
    }
}
