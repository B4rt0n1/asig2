package metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks performance metrics for algorithm analysis
 */
public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long memoryAllocations;
    private long startTime;
    private long endTime;
    private Map<String, Long> customMetrics;
    
    public PerformanceTracker() {
        reset();
    }
    
    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        memoryAllocations = 0;
        startTime = 0;
        endTime = 0;
        customMetrics = new HashMap<>();
    }
    
    // Increment methods
    public void incrementComparisons(long count) {
        this.comparisons += count;
    }
    
    public void incrementSwaps(long count) {
        this.swaps += count;
    }
    
    public void incrementArrayAccesses(long count) {
        this.arrayAccesses += count;
    }
    
    public void incrementMemoryAllocation(long bytes) {
        this.memoryAllocations += bytes;
    }
    
    public void setCustomMetric(String name, long value) {
        customMetrics.put(name, value);
    }
    
    public void incrementCustomMetric(String name, long value) {
        customMetrics.put(name, customMetrics.getOrDefault(name, 0L) + value);
    }
    
    // Timer methods
    public void startTimer() {
        this.startTime = System.nanoTime();
    }
    
    public void stopTimer() {
        this.endTime = System.nanoTime();
    }
    
    public long getElapsedTime() {
        return endTime - startTime;
    }
    
    // Getters
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getMemoryAllocations() { return memoryAllocations; }
    public Map<String, Long> getCustomMetrics() { return new HashMap<>(customMetrics); }
    
    /**
     * Exports metrics to CSV format
     */
    public String toCSV() {
        return String.format("%d,%d,%d,%d,%d", 
            comparisons, swaps, arrayAccesses, memoryAllocations, getElapsedTime());
    }
    
    public static String getCSVHeader() {
        return "Comparisons,Swaps,ArrayAccesses,MemoryAllocations,TimeNs";
    }
}