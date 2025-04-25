import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Filter {
    Network network;
    int[][] outputs;

    public Filter(Network network, int[][] outputs) {
        this.network = network;
        this.outputs = outputs;
    }

    public static int[][] _binaryUpdater(Comparator comparator, int[][] inputBinaries) {
        HashSet<String> seenResults = new HashSet<>();
        List<int[]> uniqueResults = new ArrayList<>();

        for (int[] binarySequence : inputBinaries) {
            int[] resultingSequence = Comparator.apply(comparator,binarySequence);
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

    public static boolean isRedundant(Comparator comparator, Filter filter) {
        return (_binaryUpdater(comparator, filter.outputs) == filter.outputs);
    }

    public static Filter add(Comparator comparator, Filter filter) {
        Network updatedNetwork = Network.append(comparator, filter.network);
        int[][] updatedOutputs = _binaryUpdater(comparator, filter.outputs);
        return new Filter(updatedNetwork,updatedOutputs);
    }

    public boolean isSorting() {
        int networkSize = this.outputs[0].length;
        int outputCount = this.outputs.length;
        return (networkSize + 1 == outputCount);
    }
}
