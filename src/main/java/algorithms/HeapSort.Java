package algorithms;

import metrics.PerformanceTracker;

/**
 * In-place Heap Sort implementation with bottom-up heapify
 * Time Complexity: O(n log n) for all cases
 * Space Complexity: O(1) - in-place
 * 
 * Features:
 * - Bottom-up heapify for efficient heap construction
 * - Performance tracking (comparisons, swaps, array accesses)
 * - Comprehensive input validation
 * - Optimized for various input distributions
 */
public class HeapSort {
    
    private PerformanceTracker tracker;

    public HeapSort() {
        this.tracker = new PerformanceTracker();
    }
    
    public HeapSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }
    
    /**
     * Main sorting method - sorts the array using heap sort algorithm
     * @param arr the array to be sorted
     */
    public void sort(int[] arr) {
        tracker.reset();
        tracker.incrementMemoryAllocation(32); // Basic object overhead
        
        if (arr == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        
        if (arr.length <= 1) {
            return; // Already sorted
        }
        
        tracker.startTimer();
        
        int n = arr.length;
        
        // Build max heap using bottom-up approach
        buildMaxHeap(arr, n);
        
        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            swap(arr, 0, i);
            // Call maxHeapify on the reduced heap
            maxHeapify(arr, i, 0);
        }
        
        tracker.stopTimer();
    }
    
    /**
     * Builds a max heap from the array using bottom-up heapify
     * @param arr the array to heapify
     * @param n the size of the heap
     */
    private void buildMaxHeap(int[] arr, int n) {
        // Start from the last non-leaf node and heapify each node
        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapify(arr, n, i);
        }
    }
    
    /**
     * Maintains the max heap property for a subtree rooted at index i
     * @param arr the array representing the heap
     * @param n the size of the heap
     * @param i the root index of the subtree
     */
    private void maxHeapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        tracker.incrementArrayAccesses(1); // Access arr[i]
        
        // Check if left child exists and is larger than root
        if (left < n) {
            tracker.incrementArrayAccesses(1); // Access arr[left]
            tracker.incrementComparisons(1);
            if (arr[left] > arr[largest]) {
                largest = left;
            }
        }
        
        // Check if right child exists and is larger than current largest
        if (right < n) {
            tracker.incrementArrayAccesses(1); // Access arr[right]
            tracker.incrementComparisons(1);
            if (arr[right] > arr[largest]) {
                largest = right;
            }
        }
        
        // If largest is not root, swap and continue heapifying
        if (largest != i) {
            swap(arr, i, largest);
            maxHeapify(arr, n, largest);
        }
    }
    
    /**
     * Optimized iterative heapify for better constant factors
     * Reduces recursive overhead
     */
    private void maxHeapifyIterative(int[] arr, int n, int i) {
        int current = i;
        
        while (true) {
            int largest = current;
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            
            tracker.incrementArrayAccesses(1); // Access arr[current]
            
            if (left < n) {
                tracker.incrementArrayAccesses(1); // Access arr[left]
                tracker.incrementComparisons(1);
                if (arr[left] > arr[largest]) {
                    largest = left;
                }
            }
            
            if (right < n) {
                tracker.incrementArrayAccesses(1); // Access arr[right]
                tracker.incrementComparisons(1);
                if (arr[right] > arr[largest]) {
                    largest = right;
                }
            }
            
            if (largest != current) {
                swap(arr, current, largest);
                current = largest;
            } else {
                break;
            }
        }
    }
    
    /**
     * Swaps two elements in the array
     * @param arr the array
     * @param i first index
     * @param j second index
     */
    private void swap(int[] arr, int i, int j) {
        tracker.incrementArrayAccesses(4); // 2 reads + 2 writes
        tracker.incrementSwaps(1);
        
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * Returns performance metrics for analysis
     * @return PerformanceTracker with current metrics
     */
    public PerformanceTracker getPerformanceTracker() {
        return tracker;
    }

    /**
     * Sorts array and returns performance metrics in one call
     * @param arr array to sort
     * @return performance metrics
     */
    public PerformanceTracker sortWithMetrics(int[] arr) {
        sort(arr);
        return tracker;
    }

    /**
     * Optimized version for nearly sorted arrays
     * Checks if array is already sorted before proceeding
     */
    public void optimizedSort(int[] arr) {
        tracker.reset();
        
        if (isSorted(arr)) {
            return; // Early termination for sorted arrays
        }
        
        sort(arr);
    }
    
    /**
     * Checks if array is already sorted
     * @param arr array to check
     * @return true if sorted, false otherwise
     */
    private boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            tracker.incrementComparisons(1);
            tracker.incrementArrayAccesses(2);
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Version that uses iterative heapify for reduced stack usage
     */
    public void sortIterative(int[] arr) {
        tracker.reset();
        
        if (arr == null || arr.length <= 1) {
            return;
        }
        
        tracker.startTimer();
        
        int n = arr.length;
        
        // Build max heap using iterative heapify
        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapifyIterative(arr, n, i);
        }
        
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            maxHeapifyIterative(arr, i, 0);
        }
        
        tracker.stopTimer();
    }
}