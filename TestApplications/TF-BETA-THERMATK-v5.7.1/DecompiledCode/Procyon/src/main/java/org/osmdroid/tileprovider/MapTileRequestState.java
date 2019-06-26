// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import java.util.List;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;

public class MapTileRequestState
{
    private int index;
    private final IMapTileProviderCallback mCallback;
    private MapTileModuleProviderBase mCurrentProvider;
    private final long mMapTileIndex;
    private final List<MapTileModuleProviderBase> mProviderQueue;
    
    public MapTileRequestState(final long mMapTileIndex, final List<MapTileModuleProviderBase> mProviderQueue, final IMapTileProviderCallback mCallback) {
        this.mProviderQueue = mProviderQueue;
        this.mMapTileIndex = mMapTileIndex;
        this.mCallback = mCallback;
    }
    
    public IMapTileProviderCallback getCallback() {
        return this.mCallback;
    }
    
    public long getMapTile() {
        return this.mMapTileIndex;
    }
    
    public MapTileModuleProviderBase getNextProvider() {
        MapTileModuleProviderBase mCurrentProvider;
        if (this.isEmpty()) {
            mCurrentProvider = null;
        }
        else {
            mCurrentProvider = this.mProviderQueue.get(this.index++);
        }
        return this.mCurrentProvider = mCurrentProvider;
    }
    
    public boolean isEmpty() {
        final List<MapTileModuleProviderBase> mProviderQueue = this.mProviderQueue;
        return mProviderQueue == null || this.index >= mProviderQueue.size();
    }
}
