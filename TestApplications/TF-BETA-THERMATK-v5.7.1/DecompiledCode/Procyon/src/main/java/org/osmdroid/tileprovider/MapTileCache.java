// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.util.Log;
import java.util.Iterator;
import java.util.ArrayList;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.MapTileContainer;
import org.osmdroid.util.MapTileArea;
import org.osmdroid.util.MapTileList;
import org.osmdroid.util.MapTileAreaComputer;
import java.util.List;
import android.graphics.drawable.Drawable;
import java.util.HashMap;
import org.osmdroid.util.MapTileAreaList;

public class MapTileCache
{
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
    
    public MapTileCache() {
        this(Configuration.getInstance().getCacheMapTileCount());
    }
    
    public MapTileCache(final int n) {
        this.mCachedTiles = new HashMap<Long, Drawable>();
        this.mMapTileArea = new MapTileArea();
        this.mAdditionalMapTileList = new MapTileAreaList();
        this.mGC = new MapTileList();
        this.mComputers = new ArrayList<MapTileAreaComputer>();
        this.mProtectors = new ArrayList<MapTileContainer>();
        this.ensureCapacity(n);
        this.mPreCache = new MapTilePreCache(this);
    }
    
    private void populateSyncCachedTiles(final MapTileList list) {
        synchronized (this.mCachedTiles) {
            list.ensureCapacity(this.mCachedTiles.size());
            list.clear();
            final Iterator<Long> iterator = this.mCachedTiles.keySet().iterator();
            while (iterator.hasNext()) {
                list.put(iterator.next());
            }
        }
    }
    
    private void refreshAdditionalLists() {
        final Iterator<MapTileAreaComputer> iterator = this.mComputers.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            final MapTileAreaComputer mapTileAreaComputer = iterator.next();
            MapTileArea mapTileArea;
            if (i < this.mAdditionalMapTileList.getList().size()) {
                mapTileArea = this.mAdditionalMapTileList.getList().get(i);
            }
            else {
                mapTileArea = new MapTileArea();
                this.mAdditionalMapTileList.getList().add(mapTileArea);
            }
            mapTileAreaComputer.computeFromSource(this.mMapTileArea, mapTileArea);
            ++i;
        }
        while (i < this.mAdditionalMapTileList.getList().size()) {
            this.mAdditionalMapTileList.getList().remove(this.mAdditionalMapTileList.getList().size() - 1);
        }
    }
    
    private boolean shouldKeepTile(final long n) {
        if (this.mMapTileArea.contains(n)) {
            return true;
        }
        if (this.mAdditionalMapTileList.contains(n)) {
            return true;
        }
        final Iterator<MapTileContainer> iterator = this.mProtectors.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().contains(n)) {
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        final MapTileList list = new MapTileList();
        this.populateSyncCachedTiles(list);
        for (int i = 0; i < list.getSize(); ++i) {
            this.remove(list.get(i));
        }
        this.mCachedTiles.clear();
    }
    
    public boolean ensureCapacity(final int n) {
        if (this.mCapacity < n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Tile cache increased from ");
            sb.append(this.mCapacity);
            sb.append(" to ");
            sb.append(n);
            Log.i("OsmDroid", sb.toString());
            this.mCapacity = n;
            return true;
        }
        return false;
    }
    
    public void garbageCollection() {
        final int size = this.mCachedTiles.size();
        int n;
        if (!this.mStressedMemory) {
            if ((n = size - this.mCapacity) <= 0) {
                return;
            }
        }
        else {
            n = Integer.MAX_VALUE;
        }
        this.refreshAdditionalLists();
        int n2 = n;
        if (this.mAutoEnsureCapacity) {
            n2 = n;
            if (this.ensureCapacity(this.mMapTileArea.size() + this.mAdditionalMapTileList.size())) {
                n2 = n;
                if (!this.mStressedMemory && (n2 = size - this.mCapacity) <= 0) {
                    return;
                }
            }
        }
        this.populateSyncCachedTiles(this.mGC);
        final int n3 = 0;
        int n4 = n2;
        for (int i = n3; i < this.mGC.getSize(); ++i) {
            final long value = this.mGC.get(i);
            if (!this.shouldKeepTile(value)) {
                this.remove(value);
                if (--n4 == 0) {
                    break;
                }
            }
        }
    }
    
    public MapTileAreaList getAdditionalMapTileList() {
        return this.mAdditionalMapTileList;
    }
    
    public Drawable getMapTile(final long l) {
        synchronized (this.mCachedTiles) {
            return this.mCachedTiles.get(l);
        }
    }
    
    public MapTileArea getMapTileArea() {
        return this.mMapTileArea;
    }
    
    public MapTilePreCache getPreCache() {
        return this.mPreCache;
    }
    
    public List<MapTileAreaComputer> getProtectedTileComputers() {
        return this.mComputers;
    }
    
    public List<MapTileContainer> getProtectedTileContainers() {
        return this.mProtectors;
    }
    
    public TileRemovedListener getTileRemovedListener() {
        return this.mTileRemovedListener;
    }
    
    public void maintenance() {
        this.garbageCollection();
        this.mPreCache.fill();
    }
    
    public void putTile(final long l, final Drawable value) {
        if (value != null) {
            synchronized (this.mCachedTiles) {
                this.mCachedTiles.put(l, value);
            }
        }
    }
    
    protected void remove(final long l) {
        synchronized (this.mCachedTiles) {
            final Drawable drawable = this.mCachedTiles.remove(l);
            // monitorexit(this.mCachedTiles)
            if (this.getTileRemovedListener() != null) {
                this.getTileRemovedListener().onTileRemoved(l);
            }
            BitmapPool.getInstance().asyncRecycle(drawable);
        }
    }
    
    public void setAutoEnsureCapacity(final boolean mAutoEnsureCapacity) {
        this.mAutoEnsureCapacity = mAutoEnsureCapacity;
    }
    
    public void setStressedMemory(final boolean mStressedMemory) {
        this.mStressedMemory = mStressedMemory;
    }
    
    public interface TileRemovedListener
    {
        void onTileRemoved(final long p0);
    }
}
