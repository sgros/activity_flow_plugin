package org.osmdroid.tileprovider;

import android.content.Context;
import android.os.Build.VERSION;
import org.osmdroid.tileprovider.modules.IFilesystemCache;
import org.osmdroid.tileprovider.modules.INetworkAvailablityCheck;
import org.osmdroid.tileprovider.modules.MapTileApproximater;
import org.osmdroid.tileprovider.modules.MapTileAssetsProvider;
import org.osmdroid.tileprovider.modules.MapTileDownloader;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileFileStorageProviderBase;
import org.osmdroid.tileprovider.modules.MapTileFilesystemProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.modules.MapTileSqlCacheProvider;
import org.osmdroid.tileprovider.modules.NetworkAvailabliltyCheck;
import org.osmdroid.tileprovider.modules.SqlTileWriter;
import org.osmdroid.tileprovider.modules.TileWriter;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.MapTileAreaBorderComputer;
import org.osmdroid.util.MapTileAreaZoomComputer;
import org.osmdroid.util.MapTileIndex;

public class MapTileProviderBasic extends MapTileProviderArray implements IMapTileProviderCallback {
    private final MapTileApproximater mApproximationProvider;
    private final MapTileDownloader mDownloaderProvider;
    private final INetworkAvailablityCheck mNetworkAvailabilityCheck;
    protected IFilesystemCache tileWriter;

    public MapTileProviderBasic(Context context, ITileSource iTileSource) {
        this(context, iTileSource, null);
    }

    public MapTileProviderBasic(Context context, ITileSource iTileSource, IFilesystemCache iFilesystemCache) {
        this(new SimpleRegisterReceiver(context), new NetworkAvailabliltyCheck(context), iTileSource, context, iFilesystemCache);
    }

    public MapTileProviderBasic(IRegisterReceiver iRegisterReceiver, INetworkAvailablityCheck iNetworkAvailablityCheck, ITileSource iTileSource, Context context, IFilesystemCache iFilesystemCache) {
        super(iTileSource, iRegisterReceiver);
        this.mNetworkAvailabilityCheck = iNetworkAvailablityCheck;
        if (iFilesystemCache != null) {
            this.tileWriter = iFilesystemCache;
        } else if (VERSION.SDK_INT < 10) {
            this.tileWriter = new TileWriter();
        } else {
            this.tileWriter = new SqlTileWriter();
        }
        MapTileAssetsProvider mapTileAssetsProvider = new MapTileAssetsProvider(iRegisterReceiver, context.getAssets(), iTileSource);
        this.mTileProviderList.add(mapTileAssetsProvider);
        MapTileFileStorageProviderBase mapTileFileStorageProviderBase = getMapTileFileStorageProviderBase(iRegisterReceiver, iTileSource, this.tileWriter);
        this.mTileProviderList.add(mapTileFileStorageProviderBase);
        MapTileFileArchiveProvider mapTileFileArchiveProvider = new MapTileFileArchiveProvider(iRegisterReceiver, iTileSource);
        this.mTileProviderList.add(mapTileFileArchiveProvider);
        this.mApproximationProvider = new MapTileApproximater();
        this.mTileProviderList.add(this.mApproximationProvider);
        this.mApproximationProvider.addProvider(mapTileAssetsProvider);
        this.mApproximationProvider.addProvider(mapTileFileStorageProviderBase);
        this.mApproximationProvider.addProvider(mapTileFileArchiveProvider);
        this.mDownloaderProvider = new MapTileDownloader(iTileSource, this.tileWriter, iNetworkAvailablityCheck);
        this.mTileProviderList.add(this.mDownloaderProvider);
        getTileCache().getProtectedTileComputers().add(new MapTileAreaZoomComputer(-1));
        getTileCache().getProtectedTileComputers().add(new MapTileAreaBorderComputer(1));
        getTileCache().setAutoEnsureCapacity(false);
        getTileCache().setStressedMemory(false);
        getTileCache().getPreCache().addProvider(mapTileAssetsProvider);
        getTileCache().getPreCache().addProvider(mapTileFileStorageProviderBase);
        getTileCache().getPreCache().addProvider(mapTileFileArchiveProvider);
        getTileCache().getPreCache().addProvider(this.mDownloaderProvider);
        getTileCache().getProtectedTileContainers().add(this);
        setOfflineFirst(true);
    }

    public void detach() {
        IFilesystemCache iFilesystemCache = this.tileWriter;
        if (iFilesystemCache != null) {
            iFilesystemCache.onDetach();
        }
        this.tileWriter = null;
        super.detach();
    }

    /* Access modifiers changed, original: protected */
    public boolean isDowngradedMode(long j) {
        INetworkAvailablityCheck iNetworkAvailablityCheck = this.mNetworkAvailabilityCheck;
        boolean z = true;
        if ((iNetworkAvailablityCheck != null && !iNetworkAvailablityCheck.getNetworkAvailable()) || !useDataConnection()) {
            return true;
        }
        int i = -1;
        int i2 = -1;
        for (MapTileModuleProviderBase mapTileModuleProviderBase : this.mTileProviderList) {
            if (mapTileModuleProviderBase.getUsesDataConnection()) {
                int minimumZoomLevel = mapTileModuleProviderBase.getMinimumZoomLevel();
                if (i == -1 || i > minimumZoomLevel) {
                    i = minimumZoomLevel;
                }
                int maximumZoomLevel = mapTileModuleProviderBase.getMaximumZoomLevel();
                if (i2 == -1 || i2 < maximumZoomLevel) {
                    i2 = maximumZoomLevel;
                }
            }
        }
        if (!(i == -1 || i2 == -1)) {
            int zoom = MapTileIndex.getZoom(j);
            if (zoom >= i && zoom <= i2) {
                z = false;
            }
        }
        return z;
    }

    public static MapTileFileStorageProviderBase getMapTileFileStorageProviderBase(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, IFilesystemCache iFilesystemCache) {
        if (iFilesystemCache instanceof TileWriter) {
            return new MapTileFilesystemProvider(iRegisterReceiver, iTileSource);
        }
        return new MapTileSqlCacheProvider(iRegisterReceiver, iTileSource);
    }

    public boolean setOfflineFirst(boolean z) {
        int i = -1;
        int i2 = -1;
        int i3 = 0;
        for (MapTileModuleProviderBase mapTileModuleProviderBase : this.mTileProviderList) {
            if (i == -1 && mapTileModuleProviderBase == this.mDownloaderProvider) {
                i = i3;
            }
            if (i2 == -1 && mapTileModuleProviderBase == this.mApproximationProvider) {
                i2 = i3;
            }
            i3++;
        }
        if (i == -1 || i2 == -1) {
            return false;
        }
        if (i2 < i && z) {
            return true;
        }
        if (i2 > i && !z) {
            return true;
        }
        this.mTileProviderList.set(i, this.mApproximationProvider);
        this.mTileProviderList.set(i2, this.mDownloaderProvider);
        return true;
    }
}
