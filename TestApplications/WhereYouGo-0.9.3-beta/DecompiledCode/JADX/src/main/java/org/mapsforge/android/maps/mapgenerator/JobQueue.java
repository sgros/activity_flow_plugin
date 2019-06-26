package org.mapsforge.android.maps.mapgenerator;

import java.util.PriorityQueue;
import org.mapsforge.android.maps.MapView;

public class JobQueue {
    private static final int INITIAL_CAPACITY = 128;
    private final MapView mapView;
    private PriorityQueue<MapGeneratorJob> priorityQueue = new PriorityQueue(128);
    private boolean scheduleNeeded;

    public JobQueue(MapView mapView) {
        this.mapView = mapView;
    }

    public synchronized void addJob(MapGeneratorJob mapGeneratorJob) {
        if (!this.priorityQueue.contains(mapGeneratorJob)) {
            this.priorityQueue.offer(mapGeneratorJob);
        }
    }

    public synchronized void clear() {
        this.priorityQueue.clear();
    }

    public synchronized boolean isEmpty() {
        return this.priorityQueue.isEmpty();
    }

    public synchronized MapGeneratorJob poll() {
        if (this.scheduleNeeded) {
            this.scheduleNeeded = false;
            schedule();
        }
        return (MapGeneratorJob) this.priorityQueue.poll();
    }

    public synchronized void requestSchedule() {
        this.scheduleNeeded = true;
    }

    private void schedule() {
        PriorityQueue<MapGeneratorJob> tempJobQueue = new PriorityQueue(128);
        while (!this.priorityQueue.isEmpty()) {
            MapGeneratorJob mapGeneratorJob = (MapGeneratorJob) this.priorityQueue.poll();
            mapGeneratorJob.setPriority(TileScheduler.getPriority(mapGeneratorJob.tile, this.mapView));
            tempJobQueue.offer(mapGeneratorJob);
        }
        this.priorityQueue = tempJobQueue;
    }
}
