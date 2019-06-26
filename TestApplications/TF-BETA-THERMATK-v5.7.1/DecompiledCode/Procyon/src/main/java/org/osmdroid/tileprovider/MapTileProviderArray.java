// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import org.osmdroid.util.TileSystem;
import android.graphics.drawable.Drawable;
import org.osmdroid.util.MapTileIndex;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.util.Map;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import java.util.List;
import org.osmdroid.util.MapTileContainer;

public class MapTileProviderArray extends MapTileProviderBase implements MapTileContainer
{
    private IRegisterReceiver mRegisterReceiver;
    protected final List<MapTileModuleProviderBase> mTileProviderList;
    private final Map<Long, Integer> mWorking;
    
    protected MapTileProviderArray(final ITileSource tileSource, final IRegisterReceiver registerReceiver) {
        this(tileSource, registerReceiver, new MapTileModuleProviderBase[0]);
    }
    
    public MapTileProviderArray(final ITileSource tileSource, final IRegisterReceiver mRegisterReceiver, final MapTileModuleProviderBase[] elements) {
        super(tileSource);
        this.mWorking = new HashMap<Long, Integer>();
        this.mRegisterReceiver = null;
        this.mRegisterReceiver = mRegisterReceiver;
        Collections.addAll(this.mTileProviderList = new ArrayList<MapTileModuleProviderBase>(), elements);
    }
    
    private void remove(final long l) {
        synchronized (this.mWorking) {
            this.mWorking.remove(l);
        }
    }
    
    private void runAsyncNextProvider(final MapTileRequestState mapTileRequestState) {
        final MapTileModuleProviderBase nextAppropriateProvider = this.findNextAppropriateProvider(mapTileRequestState);
        if (nextAppropriateProvider != null) {
            nextAppropriateProvider.loadMapTileAsync(mapTileRequestState);
            return;
        }
        synchronized (this.mWorking) {
            final Integer n = this.mWorking.get(mapTileRequestState.getMapTile());
            // monitorexit(this.mWorking)
            if (n != null && n == 0) {
                super.mapTileRequestFailed(mapTileRequestState);
            }
            this.remove(mapTileRequestState.getMapTile());
        }
    }
    
    @Override
    public boolean contains(final long l) {
        synchronized (this.mWorking) {
            return this.mWorking.containsKey(l);
        }
    }
    
    @Override
    public void detach() {
        synchronized (this.mTileProviderList) {
            final Iterator<MapTileModuleProviderBase> iterator = this.mTileProviderList.iterator();
            while (iterator.hasNext()) {
                iterator.next().detach();
            }
            // monitorexit(this.mTileProviderList)
            synchronized (this.mWorking) {
                this.mWorking.clear();
                // monitorexit(this.mWorking)
                final IRegisterReceiver mRegisterReceiver = this.mRegisterReceiver;
                if (mRegisterReceiver != null) {
                    mRegisterReceiver.destroy();
                    this.mRegisterReceiver = null;
                }
                super.detach();
            }
        }
    }
    
    protected MapTileModuleProviderBase findNextAppropriateProvider(final MapTileRequestState mapTileRequestState) {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        MapTileModuleProviderBase nextProvider;
        while (true) {
            nextProvider = mapTileRequestState.getNextProvider();
            int n4 = n;
            int n5 = n2;
            int n6 = n3;
            if (nextProvider != null) {
                final boolean providerExists = this.getProviderExists(nextProvider);
                final boolean b = true;
                n4 = ((providerExists ^ true) ? 1 : 0);
                final boolean b2 = !this.useDataConnection() && nextProvider.getUsesDataConnection();
                final int zoom = MapTileIndex.getZoom(mapTileRequestState.getMapTile());
                int n7 = b ? 1 : 0;
                if (zoom <= nextProvider.getMaximumZoomLevel()) {
                    if (zoom < nextProvider.getMinimumZoomLevel()) {
                        n7 = (b ? 1 : 0);
                    }
                    else {
                        n7 = 0;
                    }
                }
                n5 = (b2 ? 1 : 0);
                n6 = n7;
            }
            if (nextProvider == null) {
                break;
            }
            n = n4;
            n2 = n5;
            n3 = n6;
            if (n4 != 0) {
                continue;
            }
            n = n4;
            n2 = n5;
            n3 = n6;
            if (n5 != 0) {
                continue;
            }
            n = n4;
            n2 = n5;
            if ((n3 = n6) == 0) {
                break;
            }
        }
        return nextProvider;
    }
    
    @Override
    public Drawable getMapTile(final long n) {
        final Drawable mapTile = super.mTileCache.getMapTile(n);
        if (mapTile != null) {
            if (ExpirableBitmapDrawable.getState(mapTile) == -1) {
                return mapTile;
            }
            if (this.isDowngradedMode(n)) {
                return mapTile;
            }
        }
        synchronized (this.mWorking) {
            if (this.mWorking.containsKey(n)) {
                return mapTile;
            }
            this.mWorking.put(n, 0);
            // monitorexit(this.mWorking)
            this.runAsyncNextProvider(new MapTileRequestState(n, this.mTileProviderList, this));
            return mapTile;
        }
    }
    
    @Override
    public int getMaximumZoomLevel() {
        synchronized (this.mTileProviderList) {
            final Iterator<MapTileModuleProviderBase> iterator = this.mTileProviderList.iterator();
            int maximumZoomLevel = 0;
            while (iterator.hasNext()) {
                final MapTileModuleProviderBase mapTileModuleProviderBase = iterator.next();
                if (mapTileModuleProviderBase.getMaximumZoomLevel() > maximumZoomLevel) {
                    maximumZoomLevel = mapTileModuleProviderBase.getMaximumZoomLevel();
                }
            }
            return maximumZoomLevel;
        }
    }
    
    @Override
    public int getMinimumZoomLevel() {
        int n = TileSystem.getMaximumZoomLevel();
        synchronized (this.mTileProviderList) {
            for (final MapTileModuleProviderBase mapTileModuleProviderBase : this.mTileProviderList) {
                if (mapTileModuleProviderBase.getMinimumZoomLevel() < n) {
                    n = mapTileModuleProviderBase.getMinimumZoomLevel();
                }
            }
            return n;
        }
    }
    
    public boolean getProviderExists(final MapTileModuleProviderBase mapTileModuleProviderBase) {
        return this.mTileProviderList.contains(mapTileModuleProviderBase);
    }
    
    protected boolean isDowngradedMode(final long n) {
        return false;
    }
    
    @Override
    public void mapTileRequestCompleted(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
        super.mapTileRequestCompleted(mapTileRequestState, drawable);
        this.remove(mapTileRequestState.getMapTile());
    }
    
    @Override
    public void mapTileRequestExpiredTile(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
        super.mapTileRequestExpiredTile(mapTileRequestState, drawable);
        synchronized (this.mWorking) {
            this.mWorking.put(mapTileRequestState.getMapTile(), 1);
            // monitorexit(this.mWorking)
            this.runAsyncNextProvider(mapTileRequestState);
        }
    }
    
    @Override
    public void mapTileRequestFailed(final MapTileRequestState mapTileRequestState) {
        this.runAsyncNextProvider(mapTileRequestState);
    }
    
    @Override
    public void mapTileRequestFailedExceedsMaxQueueSize(final MapTileRequestState mapTileRequestState) {
        super.mapTileRequestFailed(mapTileRequestState);
        this.remove(mapTileRequestState.getMapTile());
    }
    
    @Override
    public void setTileSource(final ITileSource tileSource) {
        super.setTileSource(tileSource);
        synchronized (this.mTileProviderList) {
            final Iterator<MapTileModuleProviderBase> iterator = this.mTileProviderList.iterator();
            while (iterator.hasNext()) {
                iterator.next().setTileSource(tileSource);
                this.clearTileCache();
            }
        }
    }
}
