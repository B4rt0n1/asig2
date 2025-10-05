package cli;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import algorithms.HeapSort;
import metrics.PerformanceTracker;
/**
 * CLI tool for benchmarking Heap Sort performance
 */
public class BenchmarkRunner {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        switch (args[0]) {
            case "benchmark":
                runBenchmarks();
                break;
            case "test":
                testWithInputSize(Integer.parseInt(args[1]));
                break;
            case "csv":
                exportCSVBenchmarks();
                break;
            default:
                printUsage();
        }
    }
    
    private static void printUsage() {
        System.out.println("Heap Sort Benchmark CLI");
        System.out.println("Usage:");
        System.out.println("  benchmark    - Run comprehensive benchmarks");
        System.out.println("  test <size>  - Test with specific input size");
        System.out.println("  csv          - Export benchmarks to CSV");
    }
    
    private static void runBenchmarks() {
        int[] sizes = {100, 1000, 10000, 50000, 100000};
        String[] distributions = {"random", "sorted", "reverse_sorted", "nearly_sorted"};
        
        System.out.println("Heap Sort Performance Benchmark");
        System.out.println("=================================");
        System.out.printf("%-10s %-15s %-12s %-10s %-10s %-15s%n", 
            "Size", "Distribution", "Time(ms)", "Comparisons", "Swaps", "Array Accesses");
        
        for (int size : sizes) {
            for (String dist : distributions) {
                PerformanceTracker metrics = benchmarkSort(size, dist);
                System.out.printf("%-10d %-15s %-12.3f %-10d %-10d %-15d%n",
                    size, dist, metrics.getElapsedTime() / 1e6, 
                    metrics.getComparisons(), metrics.getSwaps(), 
                    metrics.getArrayAccesses());
            }
            System.out.println();
        }
    }
    
    private static void testWithInputSize(int size) {
        System.out.println("Testing Heap Sort with size: " + size);
        
        int[] randomArray = generateRandomArray(size);
        int[] sortedArray = generateSortedArray(size);
        int[] reverseArray = generateReverseSortedArray(size);
        
        testArray("Random", randomArray);
        testArray("Sorted", sortedArray);
        testArray("Reverse Sorted", reverseArray);
    }
    
    private static void testArray(String type, int[] arr) {
        HeapSort sorter = new HeapSort();
        int[] testArray = arr.clone();
        
        sorter.sort(testArray);
        PerformanceTracker metrics = sorter.getPerformanceTracker();
        
        System.out.printf("%-15s: Time=%.3fms, Comparisons=%d, Swaps=%d%n",
            type, metrics.getElapsedTime() / 1e6, 
            metrics.getComparisons(), metrics.getSwaps());
        
        // Verify sort correctness
        assert isSorted(testArray) : "Array is not sorted correctly!";
    }
    
    private static void exportCSVBenchmarks() {
        try (FileWriter writer = new FileWriter("heap_sort_benchmark.csv")) {
            writer.write("Size,Distribution," + PerformanceTracker.getCSVHeader() + "\n");
            
            int[] sizes = {100, 1000, 10000, 50000, 100000};
            String[] distributions = {"random", "sorted", "reverse_sorted", "nearly_sorted"};
            
            // Run 5 trials for each configuration
            for (int trial = 0; trial < 5; trial++) {
                for (int size : sizes) {
                    for (String dist : distributions) {
                        PerformanceTracker metrics = benchmarkSort(size, dist);
                        writer.write(String.format("%d,%s,%s%n", 
                            size, dist, metrics.toCSV()));
                    }
                }
            }
            
            System.out.println("Benchmark data exported to heap_sort_benchmark.csv");
            
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }
    
    private static PerformanceTracker benchmarkSort(int size, String distribution) {
        int[] array;
        
        switch (distribution) {
            case "sorted":
                array = generateSortedArray(size);
                break;
            case "reverse_sorted":
                array = generateReverseSortedArray(size);
                break;
            case "nearly_sorted":
                array = generateNearlySortedArray(size);
                break;
            default:
                array = generateRandomArray(size);
        }
        
        HeapSort sorter = new HeapSort();
        sorter.sort(array);
        
        return sorter.getPerformanceTracker();
    }
    
    // Array generation methods
    private static int[] generateRandomArray(int size) {
        Random rand = new Random(42); // Fixed seed for reproducibility
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10);
        }
        return arr;
    }
    
    private static int[] generateSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }
    
    private static int[] generateReverseSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }
    
    private static int[] generateNearlySortedArray(int size) {
        int[] arr = generateSortedArray(size);
        Random rand = new Random(42);
        
        // Swap 10% of elements randomly
        int swaps = size / 10;
        for (int i = 0; i < swaps; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }
        return arr;
    }
    
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
}