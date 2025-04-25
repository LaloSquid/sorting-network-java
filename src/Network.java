import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Network implements Cloneable {
    ArrayList<Comparator> comparators;

    public Network() {
        this.comparators = new ArrayList<>();
    }

    public static Network append(Comparator comparator, Network network) {
        // I would like to implement a better clone method,
        // but it was late and it looked confusing, so I'm cloning
        // like this instead.
        Network newNetwork = new Network();
        for (Comparator OGComparator : network.comparators) {
            newNetwork.comparators.add(OGComparator);
        }
        newNetwork.comparators.add(comparator);
        return newNetwork;
    }

    public int size() {
        return this.comparators.size();
    }

    public int maxChannel() {
        int maxValue = Integer.MIN_VALUE;
        for (Comparator comparator : this.comparators) {
            int currentMax = comparator.maxChannel();
            if (currentMax > maxValue) {
                maxValue = currentMax;
            }
        }
        return maxValue;
    }

    public boolean isStandard() {
        boolean allStandard = true;
        for (Comparator comparator : this.comparators) {
            if (!comparator.isStandard()) {
                allStandard = false;
            }
        }
        return allStandard;
    }

    public static int[] apply(Network network, int[] inputArray) {
        int[] outputArray = inputArray.clone();
        for (Comparator comparator : network.comparators) {
            outputArray = Comparator.apply(comparator, outputArray);
        }
        return outputArray;
    }

    public static int[][] outputs(Network network, int[][] inputArraySequence) {
        HashSet<String> seenResults = new HashSet<>();
        List<int[]> uniqueResults = new ArrayList<>();

        for (int[] sequence : inputArraySequence) {
            int[] resultingSequence = apply(network, sequence);
            String key = Arrays.toString(resultingSequence);
            if (!seenResults.contains(key)) {
                seenResults.add(key);
                uniqueResults.add(resultingSequence);
            }
        }
        int[][] results = new int[uniqueResults.size()][];
        int i = 0;
        for (int[] result : uniqueResults) {
            results[i++] = result;
        }        
        return results;
    }

    private static int[] _base10ToBinaryConvert(int value, int digits) {
        // Benchmarked to be around 1.9x faster than the original method.
        int[] output = new int[digits];
        for (int i = digits - 1; i >= 0; i--) {
            output[i] = value & 1;
            value = value >> 1;
        }
        return output;
    }

    public static int[][] generateBinarySequences(int length) {
        int totalSequences = (int)Math.pow(2, length);
        int[][] binarySequences = new int[totalSequences][length];
        for (int value = 0; value < totalSequences; value++) {
            binarySequences[value] = _base10ToBinaryConvert(value, length);
        }
        return binarySequences;
    }

    public static int[][] allOutputs(Network network, int size) {
        return outputs(network, generateBinarySequences(size));
    }

    public static boolean isSorting(Network network, int size) {
        int[][] allOutputs = allOutputs(network, size);
        return (allOutputs.length == size + 1);
    }

    public static String[] toProgram(Network network, String var) {
        String[] outputArray = new String[network.size()];
        int i = 0;
        for (Comparator comparator : network.comparators) {
            outputArray[i] = Comparator.toProgram(comparator, var);
            i = i + 1;
        }
        return outputArray;
    }
}
