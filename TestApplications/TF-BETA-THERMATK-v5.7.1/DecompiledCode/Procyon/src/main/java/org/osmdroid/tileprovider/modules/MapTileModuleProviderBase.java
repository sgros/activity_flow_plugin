// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import android.graphics.drawable.Drawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.util.concurrent.RejectedExecutionException;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.config.Configuration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import android.util.Log;
import java.util.HashMap;
import org.osmdroid.tileprovider.MapTileRequestState;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;

public abstract class MapTileModuleProviderBase
{
    private final ExecutorService mExecutor;
    protected final LinkedHashMap<Long, MapTileRequestState> mPending;
    protected final Object mQueueLockObject;
    protected final HashMap<Long, MapTileRequestState> mWorking;
    
    public MapTileModuleProviderBase(final int n, final int n2) {
        this.mQueueLockObject = new Object();
        int nThreads = n;
        if (n2 < n) {
            Log.w("OsmDroid", "The pending queue size is smaller than the thread pool size. Automatically reducing the thread pool size.");
            nThreads = n2;
        }
        this.mExecutor = Executors.newFixedThreadPool(nThreads, new ConfigurablePriorityThreadFactory(5, this.getThreadGroupName()));
        this.mWorking = new HashMap<Long, MapTileRequestState>();
        this.mPending = new LinkedHashMap<Long, MapTileRequestState>(n2 + 2, 0.1f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<Long, MapTileRequestState> entry) {
                if (this.size() <= n2) {
                    return false;
                }
                for (final long longValue : MapTileModuleProviderBase.this.mPending.keySet()) {
                    if (!MapTileModuleProviderBase.this.mWorking.containsKey(longValue)) {
                        final MapTileRequestState mapTileRequestState = MapTileModuleProviderBase.this.mPending.get(longValue);
                        if (mapTileRequestState != null) {
                            MapTileModuleProviderBase.this.removeTileFromQueues(longValue);
                            mapTileRequestState.getCallback().mapTileRequestFailedExceedsMaxQueueSize(mapTileRequestState);
                            break;
                        }
                        continue;
                    }
                }
                return false;
            }
        };
    }
    
    private void clearQueue() {
        synchronized (this.mQueueLockObject) {
            this.mPending.clear();
            this.mWorking.clear();
        }
    }
    
    public void detach() {
        this.clearQueue();
        this.mExecutor.shutdown();
    }
    
    public abstract int getMaximumZoomLevel();
    
    public abstract int getMinimumZoomLevel();
    
    protected abstract String getName();
    
    protected abstract String getThreadGroupName();
    
    public abstract TileLoader getTileLoader();
    
    public abstract boolean getUsesDataConnection();
    
    public void loadMapTileAsync(final MapTileRequestState value) {
        if (this.mExecutor.isShutdown()) {
            return;
        }
        synchronized (this.mQueueLockObject) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("MapTileModuleProviderBase.loadMaptileAsync() on provider: ");
                sb.append(this.getName());
                sb.append(" for tile: ");
                sb.append(MapTileIndex.toString(value.getMapTile()));
                Log.d("OsmDroid", sb.toString());
                if (this.mPending.containsKey(value.getMapTile())) {
                    Log.d("OsmDroid", "MapTileModuleProviderBase.loadMaptileAsync() tile already exists in request queue for modular provider. Moving to front of queue.");
                }
                else {
                    Log.d("OsmDroid", "MapTileModuleProviderBase.loadMaptileAsync() adding tile to request queue for modular provider.");
                }
            }
            this.mPending.put(value.getMapTile(), value);
            // monitorexit(this.mQueueLockObject)
            try {
                this.mExecutor.execute(this.getTileLoader());
            }
            catch (RejectedExecutionException ex) {
                Log.w("OsmDroid", "RejectedExecutionException", (Throwable)ex);
            }
        }
    }
    
    protected void removeTileFromQueues(final long n) {
        synchronized (this.mQueueLockObject) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("MapTileModuleProviderBase.removeTileFromQueues() on provider: ");
                sb.append(this.getName());
                sb.append(" for tile: ");
                sb.append(MapTileIndex.toString(n));
                Log.d("OsmDroid", sb.toString());
            }
            this.mPending.remove(n);
            this.mWorking.remove(n);
        }
    }
    
    public abstract void setTileSource(final ITileSource p0);
    
    public abstract class TileLoader implements Runnable
    {
        public abstract Drawable loadTile(final long p0) throws CantContinueException;
        
        protected MapTileRequestState nextTile() {
            synchronized (MapTileModuleProviderBase.this.mQueueLockObject) {
                final Iterator<Long> iterator = MapTileModuleProviderBase.this.mPending.keySet().iterator();
                MapTileRequestState mapTileRequestState = null;
                Long n = null;
                while (iterator.hasNext()) {
                    final Long key = iterator.next();
                    if (!MapTileModuleProviderBase.this.mWorking.containsKey(key)) {
                        if (Configuration.getInstance().isDebugTileProviders()) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("TileLoader.nextTile() on provider: ");
                            sb.append(MapTileModuleProviderBase.this.getName());
                            sb.append(" found tile in working queue: ");
                            sb.append(MapTileIndex.toString(key));
                            Log.d("OsmDroid", sb.toString());
                        }
                        n = key;
                    }
                }
                if (n != null) {
                    if (Configuration.getInstance().isDebugTileProviders()) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("TileLoader.nextTile() on provider: ");
                        sb2.append(MapTileModuleProviderBase.this.getName());
                        sb2.append(" adding tile to working queue: ");
                        sb2.append(n);
                        Log.d("OsmDroid", sb2.toString());
                    }
                    MapTileModuleProviderBase.this.mWorking.put(n, MapTileModuleProviderBase.this.mPending.get(n));
                }
                if (n != null) {
                    mapTileRequestState = MapTileModuleProviderBase.this.mPending.get(n);
                }
                return mapTileRequestState;
            }
        }
        
        protected void onTileLoaderInit() {
        }
        
        protected void onTileLoaderShutdown() {
        }
        
        @Override
        public final void run() {
            this.onTileLoaderInit();
            while (true) {
                final MapTileRequestState nextTile = this.nextTile();
                if (nextTile == null) {
                    break;
                }
                if (Configuration.getInstance().isDebugTileProviders()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("TileLoader.run() processing next tile: ");
                    sb.append(MapTileIndex.toString(nextTile.getMapTile()));
                    sb.append(", pending:");
                    sb.append(MapTileModuleProviderBase.this.mPending.size());
                    sb.append(", working:");
                    sb.append(MapTileModuleProviderBase.this.mWorking.size());
                    Log.d("OsmDroid", sb.toString());
                }
                Drawable loadTile = null;
                try {
                    loadTile = this.loadTile(nextTile.getMapTile());
                }
                catch (Throwable t) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Error downloading tile: ");
                    sb2.append(MapTileIndex.toString(nextTile.getMapTile()));
                    Log.i("OsmDroid", sb2.toString(), t);
                }
                catch (CantContinueException ex) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Tile loader can't continue: ");
                    sb3.append(MapTileIndex.toString(nextTile.getMapTile()));
                    Log.i("OsmDroid", sb3.toString(), (Throwable)ex);
                    MapTileModuleProviderBase.this.clearQueue();
                }
                if (loadTile == null) {
                    this.tileLoadedFailed(nextTile);
                }
                else if (ExpirableBitmapDrawable.getState(loadTile) == -2) {
                    this.tileLoadedExpired(nextTile, loadTile);
                }
                else if (ExpirableBitmapDrawable.getState(loadTile) == -3) {
                    this.tileLoadedScaled(nextTile, loadTile);
                }
                else {
                    this.tileLoaded(nextTile, loadTile);
                }
            }
            this.onTileLoaderShutdown();
        }
        
        protected void tileLoaded(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TileLoader.tileLoaded() on provider: ");
                sb.append(MapTileModuleProviderBase.this.getName());
                sb.append(" with tile: ");
                sb.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", sb.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -1);
            mapTileRequestState.getCallback().mapTileRequestCompleted(mapTileRequestState, drawable);
        }
        
        protected void tileLoadedExpired(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TileLoader.tileLoadedExpired() on provider: ");
                sb.append(MapTileModuleProviderBase.this.getName());
                sb.append(" with tile: ");
                sb.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", sb.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -2);
            mapTileRequestState.getCallback().mapTileRequestExpiredTile(mapTileRequestState, drawable);
        }
        
        protected void tileLoadedFailed(final MapTileRequestState mapTileRequestState) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TileLoader.tileLoadedFailed() on provider: ");
                sb.append(MapTileModuleProviderBase.this.getName());
                sb.append(" with tile: ");
                sb.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", sb.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            mapTileRequestState.getCallback().mapTileRequestFailed(mapTileRequestState);
        }
        
        protected void tileLoadedScaled(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("TileLoader.tileLoadedScaled() on provider: ");
                sb.append(MapTileModuleProviderBase.this.getName());
                sb.append(" with tile: ");
                sb.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", sb.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -3);
            mapTileRequestState.getCallback().mapTileRequestExpiredTile(mapTileRequestState, drawable);
        }
    }
}
