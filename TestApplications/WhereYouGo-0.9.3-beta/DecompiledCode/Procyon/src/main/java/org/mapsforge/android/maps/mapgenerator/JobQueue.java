// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import java.util.PriorityQueue;
import org.mapsforge.android.maps.MapView;

public class JobQueue
{
    private static final int INITIAL_CAPACITY = 128;
    private final MapView mapView;
    private PriorityQueue<MapGeneratorJob> priorityQueue;
    private boolean scheduleNeeded;
    
    public JobQueue(final MapView mapView) {
        this.mapView = mapView;
        this.priorityQueue = new PriorityQueue<MapGeneratorJob>(128);
    }
    
    private void schedule() {
        final PriorityQueue<MapGeneratorJob> priorityQueue = new PriorityQueue<MapGeneratorJob>(128);
        while (!this.priorityQueue.isEmpty()) {
            final MapGeneratorJob e = this.priorityQueue.poll();
            e.setPriority(TileScheduler.getPriority(e.tile, this.mapView));
            priorityQueue.offer(e);
        }
        this.priorityQueue = priorityQueue;
    }
    
    public void addJob(final MapGeneratorJob mapGeneratorJob) {
        synchronized (this) {
            if (!this.priorityQueue.contains(mapGeneratorJob)) {
                this.priorityQueue.offer(mapGeneratorJob);
            }
        }
    }
    
    public void clear() {
        synchronized (this) {
            this.priorityQueue.clear();
        }
    }
    
    public boolean isEmpty() {
        synchronized (this) {
            return this.priorityQueue.isEmpty();
        }
    }
    
    public MapGeneratorJob poll() {
        synchronized (this) {
            if (this.scheduleNeeded) {
                this.scheduleNeeded = false;
                this.schedule();
            }
            return this.priorityQueue.poll();
        }
    }
    
    public void requestSchedule() {
        synchronized (this) {
            this.scheduleNeeded = true;
        }
    }
}
