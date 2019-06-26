// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.graphics.drawable.Drawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.modules.CantContinueException;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.util.MapTileArea;
import java.util.ArrayList;
import java.util.Iterator;
import org.osmdroid.util.MapTileAreaList;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import java.util.List;
import org.osmdroid.util.GarbageCollector;

public class MapTilePreCache
{
    private final MapTileCache mCache;
    private final GarbageCollector mGC;
    private final List<MapTileModuleProviderBase> mProviders;
    private final MapTileAreaList mTileAreas;
    private Iterator<Long> mTileIndices;
    
    public MapTilePreCache(final MapTileCache mCache) {
        this.mProviders = new ArrayList<MapTileModuleProviderBase>();
        this.mTileAreas = new MapTileAreaList();
        this.mGC = new GarbageCollector(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final long access$000 = MapTilePreCache.this.next();
                    if (access$000 == -1L) {
                        break;
                    }
                    MapTilePreCache.this.search(access$000);
                }
            }
        });
        this.mCache = mCache;
    }
    
    private long next() {
        while (true) {
            synchronized (this.mTileAreas) {
                if (!this.mTileIndices.hasNext()) {
                    return -1L;
                }
                final long longValue = this.mTileIndices.next();
                // monitorexit(this.mTileAreas)
                if (this.mCache.getMapTile(longValue) == null) {
                    return longValue;
                }
                continue;
            }
        }
    }
    
    private void refresh() {
        final MapTileAreaList mTileAreas = this.mTileAreas;
        // monitorenter(mTileAreas)
        int i = 0;
        try {
            for (final MapTileArea mapTileArea : this.mCache.getAdditionalMapTileList().getList()) {
                MapTileArea mapTileArea2;
                if (i < this.mTileAreas.getList().size()) {
                    mapTileArea2 = this.mTileAreas.getList().get(i);
                }
                else {
                    mapTileArea2 = new MapTileArea();
                    this.mTileAreas.getList().add(mapTileArea2);
                }
                mapTileArea2.set(mapTileArea);
                ++i;
            }
            while (i < this.mTileAreas.getList().size()) {
                this.mTileAreas.getList().remove(this.mTileAreas.getList().size() - 1);
            }
            this.mTileIndices = this.mTileAreas.iterator();
        }
        finally {
        }
        // monitorexit(mTileAreas)
    }
    
    private void search(final long n) {
        for (final MapTileModuleProviderBase mapTileModuleProviderBase : this.mProviders) {
            try {
                if (mapTileModuleProviderBase instanceof MapTileDownloader) {
                    final ITileSource tileSource = ((MapTileDownloader)mapTileModuleProviderBase).getTileSource();
                    if (tileSource instanceof OnlineTileSourceBase && !((OnlineTileSourceBase)tileSource).getTileSourcePolicy().acceptsPreventive()) {
                        continue;
                    }
                }
                final Drawable loadTile = mapTileModuleProviderBase.getTileLoader().loadTile(n);
                if (loadTile == null) {
                    continue;
                }
                this.mCache.putTile(n, loadTile);
                return;
            }
            catch (CantContinueException ex) {
                continue;
            }
            break;
        }
    }
    
    public void addProvider(final MapTileModuleProviderBase mapTileModuleProviderBase) {
        this.mProviders.add(mapTileModuleProviderBase);
    }
    
    public void fill() {
        if (this.mGC.isRunning()) {
            return;
        }
        this.refresh();
        this.mGC.gc();
    }
}
