package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.MapTileRequestState;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.TileSystem;
import org.osmdroid.util.UrlBackoff;

public class MapTileDownloader extends MapTileModuleProviderBase {
    private final IFilesystemCache mFilesystemCache;
    private final INetworkAvailablityCheck mNetworkAvailablityCheck;
    private TileDownloader mTileDownloader;
    private final TileLoader mTileLoader;
    private final AtomicReference<OnlineTileSourceBase> mTileSource;
    private final UrlBackoff mUrlBackoff;

    protected class TileLoader extends org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        /* Access modifiers changed, original: protected */
        public Drawable downloadTile(long j, int i, String str) throws CantContinueException {
            OnlineTileSourceBase onlineTileSourceBase = (OnlineTileSourceBase) MapTileDownloader.this.mTileSource.get();
            if (onlineTileSourceBase == null) {
                return null;
            }
            try {
                onlineTileSourceBase.acquire();
                try {
                    Drawable downloadTile = MapTileDownloader.this.mTileDownloader.downloadTile(j, i, str, MapTileDownloader.this.mFilesystemCache, onlineTileSourceBase);
                    return downloadTile;
                } finally {
                    onlineTileSourceBase.release();
                }
            } catch (InterruptedException unused) {
                return null;
            }
        }

        public Drawable loadTile(long j) throws CantContinueException {
            OnlineTileSourceBase onlineTileSourceBase = (OnlineTileSourceBase) MapTileDownloader.this.mTileSource.get();
            if (onlineTileSourceBase == null) {
                return null;
            }
            if (MapTileDownloader.this.mNetworkAvailablityCheck == null || MapTileDownloader.this.mNetworkAvailablityCheck.getNetworkAvailable()) {
                String tileURLString = onlineTileSourceBase.getTileURLString(j);
                if (TextUtils.isEmpty(tileURLString) || MapTileDownloader.this.mUrlBackoff.shouldWait(tileURLString)) {
                    return null;
                }
                Drawable downloadTile = downloadTile(j, 0, tileURLString);
                if (downloadTile == null) {
                    MapTileDownloader.this.mUrlBackoff.next(tileURLString);
                } else {
                    MapTileDownloader.this.mUrlBackoff.remove(tileURLString);
                }
                return downloadTile;
            }
            if (Configuration.getInstance().isDebugMode()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Skipping ");
                stringBuilder.append(MapTileDownloader.this.getName());
                stringBuilder.append(" due to NetworkAvailabliltyCheck.");
                Log.d("OsmDroid", stringBuilder.toString());
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void tileLoaded(MapTileRequestState mapTileRequestState, Drawable drawable) {
            MapTileDownloader.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            mapTileRequestState.getCallback().mapTileRequestCompleted(mapTileRequestState, null);
            BitmapPool.getInstance().asyncRecycle(drawable);
        }
    }

    /* Access modifiers changed, original: protected */
    public String getName() {
        return "Online Tile Download Provider";
    }

    /* Access modifiers changed, original: protected */
    public String getThreadGroupName() {
        return "downloader";
    }

    public boolean getUsesDataConnection() {
        return true;
    }

    public MapTileDownloader(ITileSource iTileSource, IFilesystemCache iFilesystemCache, INetworkAvailablityCheck iNetworkAvailablityCheck) {
        this(iTileSource, iFilesystemCache, iNetworkAvailablityCheck, Configuration.getInstance().getTileDownloadThreads(), Configuration.getInstance().getTileDownloadMaxQueueSize());
    }

    public MapTileDownloader(ITileSource iTileSource, IFilesystemCache iFilesystemCache, INetworkAvailablityCheck iNetworkAvailablityCheck, int i, int i2) {
        super(i, i2);
        this.mTileSource = new AtomicReference();
        this.mTileLoader = new TileLoader();
        this.mUrlBackoff = new UrlBackoff();
        this.mTileDownloader = new TileDownloader();
        this.mFilesystemCache = iFilesystemCache;
        this.mNetworkAvailablityCheck = iNetworkAvailablityCheck;
        setTileSource(iTileSource);
    }

    public ITileSource getTileSource() {
        return (ITileSource) this.mTileSource.get();
    }

    public TileLoader getTileLoader() {
        return this.mTileLoader;
    }

    public void detach() {
        super.detach();
        IFilesystemCache iFilesystemCache = this.mFilesystemCache;
        if (iFilesystemCache != null) {
            iFilesystemCache.onDetach();
        }
    }

    public int getMinimumZoomLevel() {
        OnlineTileSourceBase onlineTileSourceBase = (OnlineTileSourceBase) this.mTileSource.get();
        return onlineTileSourceBase != null ? onlineTileSourceBase.getMinimumZoomLevel() : 0;
    }

    public int getMaximumZoomLevel() {
        OnlineTileSourceBase onlineTileSourceBase = (OnlineTileSourceBase) this.mTileSource.get();
        if (onlineTileSourceBase != null) {
            return onlineTileSourceBase.getMaximumZoomLevel();
        }
        return TileSystem.getMaximumZoomLevel();
    }

    public void setTileSource(ITileSource iTileSource) {
        if (iTileSource instanceof OnlineTileSourceBase) {
            this.mTileSource.set((OnlineTileSourceBase) iTileSource);
        } else {
            this.mTileSource.set(null);
        }
    }
}
