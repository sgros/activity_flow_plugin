package org.mapsforge.android.maps.mapgenerator;

import java.util.PriorityQueue;
import org.mapsforge.android.maps.MapView;

public class JobQueue {
   private static final int INITIAL_CAPACITY = 128;
   private final MapView mapView;
   private PriorityQueue priorityQueue;
   private boolean scheduleNeeded;

   public JobQueue(MapView var1) {
      this.mapView = var1;
      this.priorityQueue = new PriorityQueue(128);
   }

   private void schedule() {
      PriorityQueue var1 = new PriorityQueue(128);

      while(!this.priorityQueue.isEmpty()) {
         MapGeneratorJob var2 = (MapGeneratorJob)this.priorityQueue.poll();
         var2.setPriority(TileScheduler.getPriority(var2.tile, this.mapView));
         var1.offer(var2);
      }

      this.priorityQueue = var1;
   }

   public void addJob(MapGeneratorJob var1) {
      synchronized(this){}

      try {
         if (!this.priorityQueue.contains(var1)) {
            this.priorityQueue.offer(var1);
         }
      } finally {
         ;
      }

   }

   public void clear() {
      synchronized(this){}

      try {
         this.priorityQueue.clear();
      } finally {
         ;
      }

   }

   public boolean isEmpty() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.priorityQueue.isEmpty();
      } finally {
         ;
      }

      return var1;
   }

   public MapGeneratorJob poll() {
      synchronized(this){}

      MapGeneratorJob var1;
      try {
         if (this.scheduleNeeded) {
            this.scheduleNeeded = false;
            this.schedule();
         }

         var1 = (MapGeneratorJob)this.priorityQueue.poll();
      } finally {
         ;
      }

      return var1;
   }

   public void requestSchedule() {
      synchronized(this){}

      try {
         this.scheduleNeeded = true;
      } finally {
         ;
      }

   }
}
