package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

class HeapSortTest {
    private HeapSort heapSort;
    private PerformanceTracker tracker;
    
    @BeforeEach
    void setUp() {
        tracker = new PerformanceTracker();
        heapSort = new HeapSort(tracker);
    }
    
    @Test
    void testSortNullArray() {
        assertThrows(IllegalArgumentException.class, () -> heapSort.sort(null));
    }
    
    @Test
    void testSortEmptyArray() {
        int[] arr = {};
        heapSort.sort(arr);
        assertEquals(0, arr.length);
    }
    
    @Test
    void testSortSingleElement() {
        int[] arr = {5};
        int[] expected = {5};
        heapSort.sort(arr);
        assertArrayEquals(expected, arr);
    }
    
    @Test
    void testSortSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        heapSort.sort(arr);
        assertArrayEquals(expected, arr);
    }
    
    @Test
    void testSortReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        heapSort.sort(arr);
        assertArrayEquals(expected, arr);
    }
    
    @Test
    void testSortRandomArray() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        heapSort.sort(arr);
        assertArrayEquals(expected, arr);
    }
    
    @Test
    void testSortWithDuplicates() {
        int[] arr = {5, 2, 5, 1, 2, 3};
        int[] expected = {1, 2, 2, 3, 5, 5};
        heapSort.sort(arr);
        assertArrayEquals(expected, arr);
    }
    
    @Test
    void testSortLargeArray() {
        int size = 1000;
        int[] arr = new int[size];
        Random rand = new Random(42); // Fixed seed for reproducibility
        
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000);
        }
        
        int[] expected = arr.clone();
        Arrays.sort(expected);
        
        heapSort.sort(arr);
        assertArrayEquals(expected, arr);
    }
    
    @Test
    void testPerformanceTracking() {
        int[] arr = {5, 2, 8, 1, 9};
        heapSort.sort(arr);
        
        PerformanceTracker metrics = heapSort.getPerformanceTracker();
        
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getSwaps() > 0);
        assertTrue(metrics.getArrayAccesses() > 0);
        assertTrue(metrics.getElapsedTime() >= 0);
    }
    
    @Test
    void testOptimizedSortWithSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        heapSort.optimizedSort(arr);
        
        // Should detect it's sorted and perform minimal operations
        PerformanceTracker metrics = heapSort.getPerformanceTracker();
        assertTrue(metrics.getComparisons() <= arr.length - 1);
    }
    
    @Test
    void testIterativeVersion() {
        int[] arr = {5, 2, 8, 1, 9};
        int[] expected = {1, 2, 5, 8, 9};
        
        heapSort.sortIterative(arr);
        assertArrayEquals(expected, arr);
    }
}