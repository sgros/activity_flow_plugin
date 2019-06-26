// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import java.util.Iterator;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.tileprovider.modules.MapTileSqlCacheProvider;
import org.osmdroid.tileprovider.modules.MapTileFilesystemProvider;
import org.osmdroid.tileprovider.modules.MapTileFileStorageProviderBase;
import org.osmdroid.util.MapTileAreaBorderComputer;
import org.osmdroid.util.MapTileAreaZoomComputer;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileAssetsProvider;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.modules.TileWriter;
import android.os.Build$VERSION;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import android.content.Context;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileApproximater;

public class MapTileProviderBasic extends MapTileProviderArray implements IMapTileProviderCallback
{
    private final MapTileApproximater mApproximationProvider;
    private final MapTileDownloader mDownloaderProvider;
    private final INetworkAvailablityCheck mNetworkAvailabilityCheck;
    protected IFilesystemCache tileWriter;
    
    public MapTileProviderBasic(final Context context, final ITileSource tileSource) {
        this(context, tileSource, null);
    }
    
    public MapTileProviderBasic(final Context context, final ITileSource tileSource, final IFilesystemCache filesystemCache) {
        this(new SimpleRegisterReceiver(context), new NetworkAvailabliltyCheck(context), tileSource, context, filesystemCache);
    }
    
    public MapTileProviderBasic(final IRegisterReceiver registerReceiver, final INetworkAvailablityCheck mNetworkAvailabilityCheck, final ITileSource tileSource, final Context context, final IFilesystemCache tileWriter) {
        super(tileSource, registerReceiver);
        this.mNetworkAvailabilityCheck = mNetworkAvailabilityCheck;
        if (tileWriter != null) {
            this.tileWriter = tileWriter;
        }
        else if (Build$VERSION.SDK_INT < 10) {
            this.tileWriter = new TileWriter();
        }
        else {
            this.tileWriter = new SqlTileWriter();
        }
        final MapTileAssetsProvider mapTileAssetsProvider = new MapTileAssetsProvider(registerReceiver, context.getAssets(), tileSource);
        super.mTileProviderList.add(mapTileAssetsProvider);
        final MapTileFileStorageProviderBase mapTileFileStorageProviderBase = getMapTileFileStorageProviderBase(registerReceiver, tileSource, this.tileWriter);
        super.mTileProviderList.add(mapTileFileStorageProviderBase);
        final MapTileFileArchiveProvider mapTileFileArchiveProvider = new MapTileFileArchiveProvider(registerReceiver, tileSource);
        super.mTileProviderList.add(mapTileFileArchiveProvider);
        this.mApproximationProvider = new MapTileApproximater();
        super.mTileProviderList.add(this.mApproximationProvider);
        this.mApproximationProvider.addProvider(mapTileAssetsProvider);
        this.mApproximationProvider.addProvider(mapTileFileStorageProviderBase);
        this.mApproximationProvider.addProvider(mapTileFileArchiveProvider);
        this.mDownloaderProvider = new MapTileDownloader(tileSource, this.tileWriter, mNetworkAvailabilityCheck);
        super.mTileProviderList.add(this.mDownloaderProvider);
        this.getTileCache().getProtectedTileComputers().add(new MapTileAreaZoomComputer(-1));
        this.getTileCache().getProtectedTileComputers().add(new MapTileAreaBorderComputer(1));
        this.getTileCache().setAutoEnsureCapacity(false);
        this.getTileCache().setStressedMemory(false);
        this.getTileCache().getPreCache().addProvider(mapTileAssetsProvider);
        this.getTileCache().getPreCache().addProvider(mapTileFileStorageProviderBase);
        this.getTileCache().getPreCache().addProvider(mapTileFileArchiveProvider);
        this.getTileCache().getPreCache().addProvider(this.mDownloaderProvider);
        this.getTileCache().getProtectedTileContainers().add(this);
        this.setOfflineFirst(true);
    }
    
    public static MapTileFileStorageProviderBase getMapTileFileStorageProviderBase(final IRegisterReceiver registerReceiver, final ITileSource tileSource, final IFilesystemCache filesystemCache) {
        if (filesystemCache instanceof TileWriter) {
            return new MapTileFilesystemProvider(registerReceiver, tileSource);
        }
        return new MapTileSqlCacheProvider(registerReceiver, tileSource);
    }
    
    @Override
    public void detach() {
        final IFilesystemCache tileWriter = this.tileWriter;
        if (tileWriter != null) {
            tileWriter.onDetach();
        }
        this.tileWriter = null;
        super.detach();
    }
    
    @Override
    protected boolean isDowngradedMode(final long n) {
        final INetworkAvailablityCheck mNetworkAvailabilityCheck = this.mNetworkAvailabilityCheck;
        final boolean b = true;
        if ((mNetworkAvailabilityCheck != null && !mNetworkAvailabilityCheck.getNetworkAvailable()) || !this.useDataConnection()) {
            return true;
        }
        final Iterator<MapTileModuleProviderBase> iterator = super.mTileProviderList.iterator();
        int n2 = -1;
        int n3 = -1;
        while (iterator.hasNext()) {
            final MapTileModuleProviderBase mapTileModuleProviderBase = iterator.next();
            if (mapTileModuleProviderBase.getUsesDataConnection()) {
                final int minimumZoomLevel = mapTileModuleProviderBase.getMinimumZoomLevel();
                int n4;
                if (n2 == -1 || (n4 = n2) > minimumZoomLevel) {
                    n4 = minimumZoomLevel;
                }
                final int maximumZoomLevel = mapTileModuleProviderBase.getMaximumZoomLevel();
                if (n3 != -1) {
                    n2 = n4;
                    if (n3 >= maximumZoomLevel) {
                        continue;
                    }
                }
                n3 = maximumZoomLevel;
                n2 = n4;
            }
        }
        boolean b2 = b;
        if (n2 != -1) {
            if (n3 == -1) {
                b2 = b;
            }
            else {
                final int zoom = MapTileIndex.getZoom(n);
                b2 = b;
                if (zoom >= n2) {
                    b2 = (zoom > n3 && b);
                }
            }
        }
        return b2;
    }
    
    public boolean setOfflineFirst(final boolean b) {
        final Iterator<MapTileModuleProviderBase> iterator = super.mTileProviderList.iterator();
        int n = -1;
        int n2 = -1;
        int n3 = 0;
        while (iterator.hasNext()) {
            final MapTileModuleProviderBase mapTileModuleProviderBase = iterator.next();
            int n4;
            if ((n4 = n) == -1) {
                n4 = n;
                if (mapTileModuleProviderBase == this.mDownloaderProvider) {
                    n4 = n3;
                }
            }
            int n5;
            if ((n5 = n2) == -1) {
                n5 = n2;
                if (mapTileModuleProviderBase == this.mApproximationProvider) {
                    n5 = n3;
                }
            }
            ++n3;
            n = n4;
            n2 = n5;
        }
        if (n == -1 || n2 == -1) {
            return false;
        }
        if (n2 < n && b) {
            return true;
        }
        if (n2 > n && !b) {
            return true;
        }
        super.mTileProviderList.set(n, this.mApproximationProvider);
        super.mTileProviderList.set(n2, this.mDownloaderProvider);
        return true;
    }
}
