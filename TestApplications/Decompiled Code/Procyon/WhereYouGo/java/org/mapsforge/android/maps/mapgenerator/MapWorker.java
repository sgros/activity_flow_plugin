// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.android.maps.mapgenerator;

import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.mapgenerator.databaserenderer.DatabaseRenderer;
import org.mapsforge.android.maps.PausableThread;

public class MapWorker extends PausableThread
{
    private static final String THREAD_NAME = "MapWorker";
    private DatabaseRenderer databaseRenderer;
    private final TileCache fileSystemTileCache;
    private final TileCache inMemoryTileCache;
    private final JobQueue jobQueue;
    private MapGenerator mapGenerator;
    private final MapView mapView;
    private Boolean online;
    private final Bitmap tileBitmap;
    
    public MapWorker(final MapView mapView) {
        this.online = false;
        this.mapView = mapView;
        this.jobQueue = mapView.getJobQueue();
        this.inMemoryTileCache = mapView.getInMemoryTileCache();
        this.fileSystemTileCache = mapView.getFileSystemTileCache();
        this.tileBitmap = Bitmap.createBitmap(256, 256, Bitmap$Config.RGB_565);
    }
    
    @Override
    protected void afterRun() {
        this.tileBitmap.recycle();
    }
    
    @Override
    protected void doWork() {
        final MapGeneratorJob poll = this.jobQueue.poll();
        if (!this.inMemoryTileCache.containsKey(poll) && !this.fileSystemTileCache.containsKey(poll)) {
            boolean b;
            if (this.online) {
                b = this.mapGenerator.executeJob(poll, this.tileBitmap);
            }
            else {
                b = this.databaseRenderer.executeJob(poll, this.tileBitmap);
            }
            if (!this.isInterrupted() && b) {
                if (this.mapView.getFrameBuffer().drawBitmap(poll.tile, this.tileBitmap)) {
                    this.inMemoryTileCache.put(poll, this.tileBitmap);
                }
                this.mapView.postInvalidate();
                this.fileSystemTileCache.put(poll, this.tileBitmap);
            }
        }
    }
    
    @Override
    protected String getThreadName() {
        return "MapWorker";
    }
    
    @Override
    protected ThreadPriority getThreadPriority() {
        return ThreadPriority.BELOW_NORMAL;
    }
    
    @Override
    protected boolean hasWork() {
        return !this.jobQueue.isEmpty();
    }
    
    public void setDatabaseRenderer(final DatabaseRenderer databaseRenderer) {
        this.databaseRenderer = databaseRenderer;
    }
    
    public void setMapGenerator(final MapGenerator mapGenerator) {
        this.mapGenerator = mapGenerator;
    }
    
    public void setOnline(final Boolean online) {
        this.online = online;
    }
}
