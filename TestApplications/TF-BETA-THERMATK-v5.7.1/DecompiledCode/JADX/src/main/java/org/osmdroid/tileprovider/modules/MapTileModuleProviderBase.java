package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

public abstract class MapTileModuleProviderBase {
    private final ExecutorService mExecutor;
    protected final LinkedHashMap<Long, MapTileRequestState> mPending;
    protected final Object mQueueLockObject = new Object();
    protected final HashMap<Long, MapTileRequestState> mWorking;

    public abstract class TileLoader implements Runnable {
        public abstract Drawable loadTile(long j) throws CantContinueException;

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:24:0x00b2 in {10, 11, 15, 16, 18, 20, 23} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        protected org.osmdroid.tileprovider.MapTileRequestState nextTile() {
            /*
            r8 = this;
            r0 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;
            r0 = r0.mQueueLockObject;
            monitor-enter(r0);
            r1 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;	 Catch:{ all -> 0x00af }
            r1 = r1.mPending;	 Catch:{ all -> 0x00af }
            r1 = r1.keySet();	 Catch:{ all -> 0x00af }
            r1 = r1.iterator();	 Catch:{ all -> 0x00af }
            r2 = 0;	 Catch:{ all -> 0x00af }
            r3 = r2;	 Catch:{ all -> 0x00af }
            r4 = r1.hasNext();	 Catch:{ all -> 0x00af }
            if (r4 == 0) goto L_0x0061;	 Catch:{ all -> 0x00af }
            r4 = r1.next();	 Catch:{ all -> 0x00af }
            r4 = (java.lang.Long) r4;	 Catch:{ all -> 0x00af }
            r5 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;	 Catch:{ all -> 0x00af }
            r5 = r5.mWorking;	 Catch:{ all -> 0x00af }
            r5 = r5.containsKey(r4);	 Catch:{ all -> 0x00af }
            if (r5 != 0) goto L_0x0013;	 Catch:{ all -> 0x00af }
            r3 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00af }
            r3 = r3.isDebugTileProviders();	 Catch:{ all -> 0x00af }
            if (r3 == 0) goto L_0x005f;	 Catch:{ all -> 0x00af }
            r3 = "OsmDroid";	 Catch:{ all -> 0x00af }
            r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00af }
            r5.<init>();	 Catch:{ all -> 0x00af }
            r6 = "TileLoader.nextTile() on provider: ";	 Catch:{ all -> 0x00af }
            r5.append(r6);	 Catch:{ all -> 0x00af }
            r6 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;	 Catch:{ all -> 0x00af }
            r6 = r6.getName();	 Catch:{ all -> 0x00af }
            r5.append(r6);	 Catch:{ all -> 0x00af }
            r6 = " found tile in working queue: ";	 Catch:{ all -> 0x00af }
            r5.append(r6);	 Catch:{ all -> 0x00af }
            r6 = r4.longValue();	 Catch:{ all -> 0x00af }
            r6 = org.osmdroid.util.MapTileIndex.toString(r6);	 Catch:{ all -> 0x00af }
            r5.append(r6);	 Catch:{ all -> 0x00af }
            r5 = r5.toString();	 Catch:{ all -> 0x00af }
            android.util.Log.d(r3, r5);	 Catch:{ all -> 0x00af }
            r3 = r4;	 Catch:{ all -> 0x00af }
            goto L_0x0013;	 Catch:{ all -> 0x00af }
            if (r3 == 0) goto L_0x00a0;	 Catch:{ all -> 0x00af }
            r1 = org.osmdroid.config.Configuration.getInstance();	 Catch:{ all -> 0x00af }
            r1 = r1.isDebugTileProviders();	 Catch:{ all -> 0x00af }
            if (r1 == 0) goto L_0x0091;	 Catch:{ all -> 0x00af }
            r1 = "OsmDroid";	 Catch:{ all -> 0x00af }
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00af }
            r4.<init>();	 Catch:{ all -> 0x00af }
            r5 = "TileLoader.nextTile() on provider: ";	 Catch:{ all -> 0x00af }
            r4.append(r5);	 Catch:{ all -> 0x00af }
            r5 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;	 Catch:{ all -> 0x00af }
            r5 = r5.getName();	 Catch:{ all -> 0x00af }
            r4.append(r5);	 Catch:{ all -> 0x00af }
            r5 = " adding tile to working queue: ";	 Catch:{ all -> 0x00af }
            r4.append(r5);	 Catch:{ all -> 0x00af }
            r4.append(r3);	 Catch:{ all -> 0x00af }
            r4 = r4.toString();	 Catch:{ all -> 0x00af }
            android.util.Log.d(r1, r4);	 Catch:{ all -> 0x00af }
            r1 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;	 Catch:{ all -> 0x00af }
            r1 = r1.mWorking;	 Catch:{ all -> 0x00af }
            r4 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;	 Catch:{ all -> 0x00af }
            r4 = r4.mPending;	 Catch:{ all -> 0x00af }
            r4 = r4.get(r3);	 Catch:{ all -> 0x00af }
            r1.put(r3, r4);	 Catch:{ all -> 0x00af }
            if (r3 == 0) goto L_0x00ad;	 Catch:{ all -> 0x00af }
            r1 = org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.this;	 Catch:{ all -> 0x00af }
            r1 = r1.mPending;	 Catch:{ all -> 0x00af }
            r1 = r1.get(r3);	 Catch:{ all -> 0x00af }
            r2 = r1;	 Catch:{ all -> 0x00af }
            r2 = (org.osmdroid.tileprovider.MapTileRequestState) r2;	 Catch:{ all -> 0x00af }
            monitor-exit(r0);	 Catch:{ all -> 0x00af }
            return r2;	 Catch:{ all -> 0x00af }
            r1 = move-exception;	 Catch:{ all -> 0x00af }
            monitor-exit(r0);	 Catch:{ all -> 0x00af }
            throw r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.MapTileModuleProviderBase$TileLoader.nextTile():org.osmdroid.tileprovider.MapTileRequestState");
        }

        /* Access modifiers changed, original: protected */
        public void onTileLoaderInit() {
        }

        /* Access modifiers changed, original: protected */
        public void onTileLoaderShutdown() {
        }

        /* Access modifiers changed, original: protected */
        public void tileLoaded(MapTileRequestState mapTileRequestState, Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TileLoader.tileLoaded() on provider: ");
                stringBuilder.append(MapTileModuleProviderBase.this.getName());
                stringBuilder.append(" with tile: ");
                stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", stringBuilder.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -1);
            mapTileRequestState.getCallback().mapTileRequestCompleted(mapTileRequestState, drawable);
        }

        /* Access modifiers changed, original: protected */
        public void tileLoadedExpired(MapTileRequestState mapTileRequestState, Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TileLoader.tileLoadedExpired() on provider: ");
                stringBuilder.append(MapTileModuleProviderBase.this.getName());
                stringBuilder.append(" with tile: ");
                stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", stringBuilder.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -2);
            mapTileRequestState.getCallback().mapTileRequestExpiredTile(mapTileRequestState, drawable);
        }

        /* Access modifiers changed, original: protected */
        public void tileLoadedScaled(MapTileRequestState mapTileRequestState, Drawable drawable) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TileLoader.tileLoadedScaled() on provider: ");
                stringBuilder.append(MapTileModuleProviderBase.this.getName());
                stringBuilder.append(" with tile: ");
                stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", stringBuilder.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            ExpirableBitmapDrawable.setState(drawable, -3);
            mapTileRequestState.getCallback().mapTileRequestExpiredTile(mapTileRequestState, drawable);
        }

        /* Access modifiers changed, original: protected */
        public void tileLoadedFailed(MapTileRequestState mapTileRequestState) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("TileLoader.tileLoadedFailed() on provider: ");
                stringBuilder.append(MapTileModuleProviderBase.this.getName());
                stringBuilder.append(" with tile: ");
                stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                Log.d("OsmDroid", stringBuilder.toString());
            }
            MapTileModuleProviderBase.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            mapTileRequestState.getCallback().mapTileRequestFailed(mapTileRequestState);
        }

        public final void run() {
            StringBuilder stringBuilder;
            onTileLoaderInit();
            while (true) {
                MapTileRequestState nextTile = nextTile();
                if (nextTile != null) {
                    String str = "OsmDroid";
                    if (Configuration.getInstance().isDebugTileProviders()) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("TileLoader.run() processing next tile: ");
                        stringBuilder2.append(MapTileIndex.toString(nextTile.getMapTile()));
                        stringBuilder2.append(", pending:");
                        stringBuilder2.append(MapTileModuleProviderBase.this.mPending.size());
                        stringBuilder2.append(", working:");
                        stringBuilder2.append(MapTileModuleProviderBase.this.mWorking.size());
                        Log.d(str, stringBuilder2.toString());
                    }
                    Drawable drawable = null;
                    try {
                        drawable = loadTile(nextTile.getMapTile());
                    } catch (CantContinueException e) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Tile loader can't continue: ");
                        stringBuilder.append(MapTileIndex.toString(nextTile.getMapTile()));
                        Log.i(str, stringBuilder.toString(), e);
                        MapTileModuleProviderBase.this.clearQueue();
                    } catch (Throwable th) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Error downloading tile: ");
                        stringBuilder.append(MapTileIndex.toString(nextTile.getMapTile()));
                        Log.i(str, stringBuilder.toString(), th);
                    }
                    if (drawable == null) {
                        tileLoadedFailed(nextTile);
                    } else if (ExpirableBitmapDrawable.getState(drawable) == -2) {
                        tileLoadedExpired(nextTile, drawable);
                    } else if (ExpirableBitmapDrawable.getState(drawable) == -3) {
                        tileLoadedScaled(nextTile, drawable);
                    } else {
                        tileLoaded(nextTile, drawable);
                    }
                } else {
                    onTileLoaderShutdown();
                    return;
                }
            }
        }
    }

    public abstract int getMaximumZoomLevel();

    public abstract int getMinimumZoomLevel();

    public abstract String getName();

    public abstract String getThreadGroupName();

    public abstract TileLoader getTileLoader();

    public abstract boolean getUsesDataConnection();

    public abstract void setTileSource(ITileSource iTileSource);

    public MapTileModuleProviderBase(int i, int i2) {
        if (i2 < i) {
            Log.w("OsmDroid", "The pending queue size is smaller than the thread pool size. Automatically reducing the thread pool size.");
            i = i2;
        }
        this.mExecutor = Executors.newFixedThreadPool(i, new ConfigurablePriorityThreadFactory(5, getThreadGroupName()));
        this.mWorking = new HashMap();
        final int i3 = i2;
        this.mPending = new LinkedHashMap<Long, MapTileRequestState>(i2 + 2, 0.1f, true) {
            /* Access modifiers changed, original: protected */
            public boolean removeEldestEntry(Entry<Long, MapTileRequestState> entry) {
                if (size() <= i3) {
                    return false;
                }
                for (Long longValue : MapTileModuleProviderBase.this.mPending.keySet()) {
                    long longValue2 = longValue.longValue();
                    if (!MapTileModuleProviderBase.this.mWorking.containsKey(Long.valueOf(longValue2))) {
                        MapTileRequestState mapTileRequestState = (MapTileRequestState) MapTileModuleProviderBase.this.mPending.get(Long.valueOf(longValue2));
                        if (mapTileRequestState != null) {
                            MapTileModuleProviderBase.this.removeTileFromQueues(longValue2);
                            mapTileRequestState.getCallback().mapTileRequestFailedExceedsMaxQueueSize(mapTileRequestState);
                            break;
                        }
                    }
                }
                return false;
            }
        };
    }

    public void loadMapTileAsync(MapTileRequestState mapTileRequestState) {
        if (!this.mExecutor.isShutdown()) {
            synchronized (this.mQueueLockObject) {
                if (Configuration.getInstance().isDebugTileProviders()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MapTileModuleProviderBase.loadMaptileAsync() on provider: ");
                    stringBuilder.append(getName());
                    stringBuilder.append(" for tile: ");
                    stringBuilder.append(MapTileIndex.toString(mapTileRequestState.getMapTile()));
                    Log.d("OsmDroid", stringBuilder.toString());
                    if (this.mPending.containsKey(Long.valueOf(mapTileRequestState.getMapTile()))) {
                        Log.d("OsmDroid", "MapTileModuleProviderBase.loadMaptileAsync() tile already exists in request queue for modular provider. Moving to front of queue.");
                    } else {
                        Log.d("OsmDroid", "MapTileModuleProviderBase.loadMaptileAsync() adding tile to request queue for modular provider.");
                    }
                }
                this.mPending.put(Long.valueOf(mapTileRequestState.getMapTile()), mapTileRequestState);
            }
            try {
                this.mExecutor.execute(getTileLoader());
            } catch (RejectedExecutionException e) {
                Log.w("OsmDroid", "RejectedExecutionException", e);
            }
        }
    }

    private void clearQueue() {
        synchronized (this.mQueueLockObject) {
            this.mPending.clear();
            this.mWorking.clear();
        }
    }

    public void detach() {
        clearQueue();
        this.mExecutor.shutdown();
    }

    /* Access modifiers changed, original: protected */
    public void removeTileFromQueues(long j) {
        synchronized (this.mQueueLockObject) {
            if (Configuration.getInstance().isDebugTileProviders()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("MapTileModuleProviderBase.removeTileFromQueues() on provider: ");
                stringBuilder.append(getName());
                stringBuilder.append(" for tile: ");
                stringBuilder.append(MapTileIndex.toString(j));
                Log.d("OsmDroid", stringBuilder.toString());
            }
            this.mPending.remove(Long.valueOf(j));
            this.mWorking.remove(Long.valueOf(j));
        }
    }
}
