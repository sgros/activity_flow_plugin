package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.MapTileArea;
import org.osmdroid.util.MapTileAreaComputer;
import org.osmdroid.util.MapTileAreaList;
import org.osmdroid.util.MapTileContainer;
import org.osmdroid.util.MapTileList;

public class MapTileCache {
    private final MapTileAreaList mAdditionalMapTileList;
    private boolean mAutoEnsureCapacity;
    private final HashMap<Long, Drawable> mCachedTiles;
    private int mCapacity;
    private final List<MapTileAreaComputer> mComputers;
    private final MapTileList mGC;
    private final MapTileArea mMapTileArea;
    private final MapTilePreCache mPreCache;
    private final List<MapTileContainer> mProtectors;
    private boolean mStressedMemory;
    private TileRemovedListener mTileRemovedListener;

    public interface TileRemovedListener {
        void onTileRemoved(long j);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x0032 in {6, 8, 11} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void populateSyncCachedTiles(org.osmdroid.util.MapTileList r5) {
        /*
        r4 = this;
        r0 = r4.mCachedTiles;
        monitor-enter(r0);
        r1 = r4.mCachedTiles;	 Catch:{ all -> 0x002f }
        r1 = r1.size();	 Catch:{ all -> 0x002f }
        r5.ensureCapacity(r1);	 Catch:{ all -> 0x002f }
        r5.clear();	 Catch:{ all -> 0x002f }
        r1 = r4.mCachedTiles;	 Catch:{ all -> 0x002f }
        r1 = r1.keySet();	 Catch:{ all -> 0x002f }
        r1 = r1.iterator();	 Catch:{ all -> 0x002f }
        r2 = r1.hasNext();	 Catch:{ all -> 0x002f }
        if (r2 == 0) goto L_0x002d;	 Catch:{ all -> 0x002f }
        r2 = r1.next();	 Catch:{ all -> 0x002f }
        r2 = (java.lang.Long) r2;	 Catch:{ all -> 0x002f }
        r2 = r2.longValue();	 Catch:{ all -> 0x002f }
        r5.put(r2);	 Catch:{ all -> 0x002f }
        goto L_0x0019;	 Catch:{ all -> 0x002f }
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        return;	 Catch:{ all -> 0x002f }
        r5 = move-exception;	 Catch:{ all -> 0x002f }
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        throw r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.MapTileCache.populateSyncCachedTiles(org.osmdroid.util.MapTileList):void");
    }

    public MapTileCache() {
        this(Configuration.getInstance().getCacheMapTileCount());
    }

    public MapTileCache(int i) {
        this.mCachedTiles = new HashMap();
        this.mMapTileArea = new MapTileArea();
        this.mAdditionalMapTileList = new MapTileAreaList();
        this.mGC = new MapTileList();
        this.mComputers = new ArrayList();
        this.mProtectors = new ArrayList();
        ensureCapacity(i);
        this.mPreCache = new MapTilePreCache(this);
    }

    public List<MapTileAreaComputer> getProtectedTileComputers() {
        return this.mComputers;
    }

    public List<MapTileContainer> getProtectedTileContainers() {
        return this.mProtectors;
    }

    public void setAutoEnsureCapacity(boolean z) {
        this.mAutoEnsureCapacity = z;
    }

    public void setStressedMemory(boolean z) {
        this.mStressedMemory = z;
    }

    public boolean ensureCapacity(int i) {
        if (this.mCapacity >= i) {
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tile cache increased from ");
        stringBuilder.append(this.mCapacity);
        stringBuilder.append(" to ");
        stringBuilder.append(i);
        Log.i("OsmDroid", stringBuilder.toString());
        this.mCapacity = i;
        return true;
    }

    public Drawable getMapTile(long j) {
        Drawable drawable;
        synchronized (this.mCachedTiles) {
            drawable = (Drawable) this.mCachedTiles.get(Long.valueOf(j));
        }
        return drawable;
    }

    public void putTile(long j, Drawable drawable) {
        if (drawable != null) {
            synchronized (this.mCachedTiles) {
                this.mCachedTiles.put(Long.valueOf(j), drawable);
            }
        }
    }

    public void garbageCollection() {
        int i;
        int size = this.mCachedTiles.size();
        if (this.mStressedMemory) {
            i = Integer.MAX_VALUE;
        } else {
            i = size - this.mCapacity;
            if (i <= 0) {
                return;
            }
        }
        refreshAdditionalLists();
        if (this.mAutoEnsureCapacity && ensureCapacity(this.mMapTileArea.size() + this.mAdditionalMapTileList.size()) && !this.mStressedMemory) {
            i = size - this.mCapacity;
            if (i <= 0) {
                return;
            }
        }
        populateSyncCachedTiles(this.mGC);
        for (size = 0; size < this.mGC.getSize(); size++) {
            long j = this.mGC.get(size);
            if (!shouldKeepTile(j)) {
                remove(j);
                i--;
                if (i == 0) {
                    break;
                }
            }
        }
    }

    private void refreshAdditionalLists() {
        int i = 0;
        for (MapTileAreaComputer mapTileAreaComputer : this.mComputers) {
            MapTileArea mapTileArea;
            if (i < this.mAdditionalMapTileList.getList().size()) {
                mapTileArea = (MapTileArea) this.mAdditionalMapTileList.getList().get(i);
            } else {
                mapTileArea = new MapTileArea();
                this.mAdditionalMapTileList.getList().add(mapTileArea);
            }
            mapTileAreaComputer.computeFromSource(this.mMapTileArea, mapTileArea);
            i++;
        }
        while (i < this.mAdditionalMapTileList.getList().size()) {
            this.mAdditionalMapTileList.getList().remove(this.mAdditionalMapTileList.getList().size() - 1);
        }
    }

    private boolean shouldKeepTile(long j) {
        if (this.mMapTileArea.contains(j) || this.mAdditionalMapTileList.contains(j)) {
            return true;
        }
        for (MapTileContainer contains : this.mProtectors) {
            if (contains.contains(j)) {
                return true;
            }
        }
        return false;
    }

    public MapTileArea getMapTileArea() {
        return this.mMapTileArea;
    }

    public MapTileAreaList getAdditionalMapTileList() {
        return this.mAdditionalMapTileList;
    }

    public void clear() {
        MapTileList mapTileList = new MapTileList();
        populateSyncCachedTiles(mapTileList);
        for (int i = 0; i < mapTileList.getSize(); i++) {
            remove(mapTileList.get(i));
        }
        this.mCachedTiles.clear();
    }

    /* Access modifiers changed, original: protected */
    public void remove(long j) {
        Drawable drawable;
        synchronized (this.mCachedTiles) {
            drawable = (Drawable) this.mCachedTiles.remove(Long.valueOf(j));
        }
        if (getTileRemovedListener() != null) {
            getTileRemovedListener().onTileRemoved(j);
        }
        BitmapPool.getInstance().asyncRecycle(drawable);
    }

    public TileRemovedListener getTileRemovedListener() {
        return this.mTileRemovedListener;
    }

    public void maintenance() {
        garbageCollection();
        this.mPreCache.fill();
    }

    public MapTilePreCache getPreCache() {
        return this.mPreCache;
    }
}
