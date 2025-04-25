import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("How many channels should the sorting network account for?");
        int networkSize = getUserNetworkSize();
        Filter emptyFilter = createStartFilter(networkSize);
        System.out.println("The starting filter has " + emptyFilter.outputs.length + " unique binary sequences.");
        Network sortingNetwork = findSortingNetwork(emptyFilter, networkSize);
        String[] pythonInstructions = Network.toProgram(sortingNetwork, "v");
        System.out.println("The Python implementation for sorting a list v of size " + networkSize + " is:");
        for (String instruction : pythonInstructions) {
            System.out.println(instruction);
        }
    }

    private static Network findSortingNetwork(Filter emptyFilter, int networkSize) {
        Comparator[] stdComparators = Comparator.stdComparators(networkSize);
        int iteration = 1;
        ArrayList<Filter> filterList = new ArrayList<>();
        filterList.add(emptyFilter);
        Network sortingNetwork = null;

        while (sortingNetwork == null) {
            System.out.println("-------------------------------------");
            System.out.println("Iteration: " + iteration);
            filterList = extend(filterList, networkSize, stdComparators); //Extend filters
            System.out.println("New filters after extension: " + filterList.size());
            filterList = basicPrune(filterList); //Prune filters - We don't do this yet.
            System.out.println("Remaining filters after prune: " + filterList.size());
            for (Filter filter : filterList) {
                if (filter.isSorting()) {
                    sortingNetwork = filter.network;
                }
            }
            iteration = iteration + 1;
        }
        return sortingNetwork;
    }

    private static ArrayList<Filter> basicPrune(ArrayList<Filter> filters) {
        ArrayList<Filter> keptFilters = new ArrayList<>();
        HashSet<String> seenOutputs = new HashSet<>();
        for (Filter filter : filters) {
            String key = Arrays.deepToString(filter.outputs);
            if (!seenOutputs.contains(key)) {
                seenOutputs.add(key);
                keptFilters.add(filter);
            }
        }
        return keptFilters;
    }

    private static Filter createStartFilter(int networkSize) {
        Network emptyNetwork = new Network();
        int[][] outputs = Network.generateBinarySequences(networkSize);
        Filter emptyFilter = new Filter(emptyNetwork, outputs);
        return emptyFilter; 
    }

    private static int getUserNetworkSize() {
        int networkSize = 0;
        String retryMessage = "Only whole numbers from 2 to 127 are supported. Please try again!";
        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                networkSize = scanner.nextInt();
                if (networkSize < 2) {
                    System.out.print("The number given was too low! " + retryMessage + "\n");
                } else if (networkSize > 127) {
                    System.out.print("The number given was too high! " + retryMessage + "\n");
                } else {
                    break;
                }
            } catch(Exception e) {
                System.out.println("You entered something that wasn't a whole number. " + retryMessage + "\n");
            }
            scanner.nextLine();
        }
        scanner.close();
        return networkSize;
    }

    private static ArrayList<Filter> extend(ArrayList<Filter> filters, int networkSize, Comparator[] stdComparators) {
        ArrayList<Filter> validExtensions = new ArrayList<>();

        for (Filter filter : filters) {
            int oldOutputsLength = filter.outputs.length;
            for (Comparator comparator : stdComparators) {
                Filter extendedFilter = Filter.add(comparator, filter);
                int newOutputsLength = extendedFilter.outputs.length;
                if (newOutputsLength < oldOutputsLength) {
                    validExtensions.add(extendedFilter);
                }
            }
        }
        return validExtensions;
    }
}
