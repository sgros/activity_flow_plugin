package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.PausableThread;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;

public class MapWorker extends PausableThread {
   private static final String THREAD_NAME = "MapWorker";
   private DatabaseRenderer databaseRenderer;
   private final TileCache fileSystemTileCache;
   private final TileCache inMemoryTileCache;
   private final JobQueue jobQueue;
   private MapGenerator mapGenerator;
   private final MapView mapView;
   private Boolean online = false;
   private final Bitmap tileBitmap;

   public MapWorker(MapView var1) {
      this.mapView = var1;
      this.jobQueue = var1.getJobQueue();
      this.inMemoryTileCache = var1.getInMemoryTileCache();
      this.fileSystemTileCache = var1.getFileSystemTileCache();
      this.tileBitmap = Bitmap.createBitmap(256, 256, Config.RGB_565);
   }

   protected void afterRun() {
      this.tileBitmap.recycle();
   }

   protected void doWork() {
      MapGeneratorJob var1 = this.jobQueue.poll();
      if (!this.inMemoryTileCache.containsKey(var1) && !this.fileSystemTileCache.containsKey(var1)) {
         boolean var2;
         if (this.online) {
            var2 = this.mapGenerator.executeJob(var1, this.tileBitmap);
         } else {
            var2 = this.databaseRenderer.executeJob(var1, this.tileBitmap);
         }

         if (!this.isInterrupted() && var2) {
            if (this.mapView.getFrameBuffer().drawBitmap(var1.tile, this.tileBitmap)) {
               this.inMemoryTileCache.put(var1, this.tileBitmap);
            }

            this.mapView.postInvalidate();
            this.fileSystemTileCache.put(var1, this.tileBitmap);
         }
      }

   }

   protected String getThreadName() {
      return "MapWorker";
   }

   protected PausableThread.ThreadPriority getThreadPriority() {
      return PausableThread.ThreadPriority.BELOW_NORMAL;
   }

   protected boolean hasWork() {
      boolean var1;
      if (!this.jobQueue.isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setDatabaseRenderer(DatabaseRenderer var1) {
      this.databaseRenderer = var1;
   }

   public void setMapGenerator(MapGenerator var1) {
      this.mapGenerator = var1;
   }

   public void setOnline(Boolean var1) {
      this.online = var1;
   }
}
