package org.osmdroid.tileprovider;

import java.util.List;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;

public class MapTileRequestState {
    private int index;
    private final IMapTileProviderCallback mCallback;
    private MapTileModuleProviderBase mCurrentProvider;
    private final long mMapTileIndex;
    private final List<MapTileModuleProviderBase> mProviderQueue;

    public MapTileRequestState(long j, List<MapTileModuleProviderBase> list, IMapTileProviderCallback iMapTileProviderCallback) {
        this.mProviderQueue = list;
        this.mMapTileIndex = j;
        this.mCallback = iMapTileProviderCallback;
    }

    public long getMapTile() {
        return this.mMapTileIndex;
    }

    public IMapTileProviderCallback getCallback() {
        return this.mCallback;
    }

    public boolean isEmpty() {
        List list = this.mProviderQueue;
        return list == null || this.index >= list.size();
    }

    public MapTileModuleProviderBase getNextProvider() {
        MapTileModuleProviderBase mapTileModuleProviderBase;
        if (isEmpty()) {
            mapTileModuleProviderBase = null;
        } else {
            List list = this.mProviderQueue;
            int i = this.index;
            this.index = i + 1;
            mapTileModuleProviderBase = (MapTileModuleProviderBase) list.get(i);
        }
        this.mCurrentProvider = mapTileModuleProviderBase;
        return this.mCurrentProvider;
    }
}
