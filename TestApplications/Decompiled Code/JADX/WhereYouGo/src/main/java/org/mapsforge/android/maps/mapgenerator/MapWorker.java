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
    private Boolean online = Boolean.valueOf(false);
    private final Bitmap tileBitmap;

    public MapWorker(MapView mapView) {
        this.mapView = mapView;
        this.jobQueue = mapView.getJobQueue();
        this.inMemoryTileCache = mapView.getInMemoryTileCache();
        this.fileSystemTileCache = mapView.getFileSystemTileCache();
        this.tileBitmap = Bitmap.createBitmap(256, 256, Config.RGB_565);
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setDatabaseRenderer(DatabaseRenderer databaseRenderer) {
        this.databaseRenderer = databaseRenderer;
    }

    public void setMapGenerator(MapGenerator mapGenerator) {
        this.mapGenerator = mapGenerator;
    }

    /* Access modifiers changed, original: protected */
    public void afterRun() {
        this.tileBitmap.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void doWork() {
        MapGeneratorJob mapGeneratorJob = this.jobQueue.poll();
        if (!this.inMemoryTileCache.containsKey(mapGeneratorJob) && !this.fileSystemTileCache.containsKey(mapGeneratorJob)) {
            boolean success;
            if (this.online.booleanValue()) {
                success = this.mapGenerator.executeJob(mapGeneratorJob, this.tileBitmap);
            } else {
                success = this.databaseRenderer.executeJob(mapGeneratorJob, this.tileBitmap);
            }
            if (!isInterrupted() && success) {
                if (this.mapView.getFrameBuffer().drawBitmap(mapGeneratorJob.tile, this.tileBitmap)) {
                    this.inMemoryTileCache.put(mapGeneratorJob, this.tileBitmap);
                }
                this.mapView.postInvalidate();
                this.fileSystemTileCache.put(mapGeneratorJob, this.tileBitmap);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public String getThreadName() {
        return THREAD_NAME;
    }

    /* Access modifiers changed, original: protected */
    public ThreadPriority getThreadPriority() {
        return ThreadPriority.BELOW_NORMAL;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasWork() {
        return !this.jobQueue.isEmpty();
    }
}
