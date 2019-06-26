// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.MapTileRequestState;
import android.text.TextUtils;
import android.util.Log;
import android.graphics.drawable.Drawable;
import org.osmdroid.util.TileSystem;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.UrlBackoff;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import java.util.concurrent.atomic.AtomicReference;

public class MapTileDownloader extends MapTileModuleProviderBase
{
    private final IFilesystemCache mFilesystemCache;
    private final INetworkAvailablityCheck mNetworkAvailablityCheck;
    private TileDownloader mTileDownloader;
    private final TileLoader mTileLoader;
    private final AtomicReference<OnlineTileSourceBase> mTileSource;
    private final UrlBackoff mUrlBackoff;
    
    public MapTileDownloader(final ITileSource tileSource, final IFilesystemCache filesystemCache, final INetworkAvailablityCheck networkAvailablityCheck) {
        this(tileSource, filesystemCache, networkAvailablityCheck, Configuration.getInstance().getTileDownloadThreads(), Configuration.getInstance().getTileDownloadMaxQueueSize());
    }
    
    public MapTileDownloader(final ITileSource tileSource, final IFilesystemCache mFilesystemCache, final INetworkAvailablityCheck mNetworkAvailablityCheck, final int n, final int n2) {
        super(n, n2);
        this.mTileSource = new AtomicReference<OnlineTileSourceBase>();
        this.mTileLoader = new TileLoader();
        this.mUrlBackoff = new UrlBackoff();
        this.mTileDownloader = new TileDownloader();
        this.mFilesystemCache = mFilesystemCache;
        this.mNetworkAvailablityCheck = mNetworkAvailablityCheck;
        this.setTileSource(tileSource);
    }
    
    @Override
    public void detach() {
        super.detach();
        final IFilesystemCache mFilesystemCache = this.mFilesystemCache;
        if (mFilesystemCache != null) {
            mFilesystemCache.onDetach();
        }
    }
    
    @Override
    public int getMaximumZoomLevel() {
        final OnlineTileSourceBase onlineTileSourceBase = this.mTileSource.get();
        int n;
        if (onlineTileSourceBase != null) {
            n = onlineTileSourceBase.getMaximumZoomLevel();
        }
        else {
            n = TileSystem.getMaximumZoomLevel();
        }
        return n;
    }
    
    @Override
    public int getMinimumZoomLevel() {
        final OnlineTileSourceBase onlineTileSourceBase = this.mTileSource.get();
        int minimumZoomLevel;
        if (onlineTileSourceBase != null) {
            minimumZoomLevel = onlineTileSourceBase.getMinimumZoomLevel();
        }
        else {
            minimumZoomLevel = 0;
        }
        return minimumZoomLevel;
    }
    
    @Override
    protected String getName() {
        return "Online Tile Download Provider";
    }
    
    @Override
    protected String getThreadGroupName() {
        return "downloader";
    }
    
    public TileLoader getTileLoader() {
        return this.mTileLoader;
    }
    
    public ITileSource getTileSource() {
        return this.mTileSource.get();
    }
    
    @Override
    public boolean getUsesDataConnection() {
        return true;
    }
    
    @Override
    public void setTileSource(final ITileSource tileSource) {
        if (tileSource instanceof OnlineTileSourceBase) {
            this.mTileSource.set((OnlineTileSourceBase)tileSource);
        }
        else {
            this.mTileSource.set(null);
        }
    }
    
    protected class TileLoader extends MapTileModuleProviderBase.TileLoader
    {
        protected Drawable downloadTile(final long n, final int n2, final String s) throws CantContinueException {
            final OnlineTileSourceBase onlineTileSourceBase = MapTileDownloader.this.mTileSource.get();
            if (onlineTileSourceBase == null) {
                return null;
            }
            try {
                onlineTileSourceBase.acquire();
                try {
                    return MapTileDownloader.this.mTileDownloader.downloadTile(n, n2, s, MapTileDownloader.this.mFilesystemCache, onlineTileSourceBase);
                }
                finally {
                    onlineTileSourceBase.release();
                }
            }
            catch (InterruptedException ex) {
                return null;
            }
        }
        
        @Override
        public Drawable loadTile(final long n) throws CantContinueException {
            final OnlineTileSourceBase onlineTileSourceBase = MapTileDownloader.this.mTileSource.get();
            if (onlineTileSourceBase == null) {
                return null;
            }
            if (MapTileDownloader.this.mNetworkAvailablityCheck != null && !MapTileDownloader.this.mNetworkAvailablityCheck.getNetworkAvailable()) {
                if (Configuration.getInstance().isDebugMode()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Skipping ");
                    sb.append(MapTileDownloader.this.getName());
                    sb.append(" due to NetworkAvailabliltyCheck.");
                    Log.d("OsmDroid", sb.toString());
                }
                return null;
            }
            final String tileURLString = onlineTileSourceBase.getTileURLString(n);
            if (TextUtils.isEmpty((CharSequence)tileURLString)) {
                return null;
            }
            if (MapTileDownloader.this.mUrlBackoff.shouldWait(tileURLString)) {
                return null;
            }
            final Drawable downloadTile = this.downloadTile(n, 0, tileURLString);
            if (downloadTile == null) {
                MapTileDownloader.this.mUrlBackoff.next(tileURLString);
            }
            else {
                MapTileDownloader.this.mUrlBackoff.remove(tileURLString);
            }
            return downloadTile;
        }
        
        @Override
        protected void tileLoaded(final MapTileRequestState mapTileRequestState, final Drawable drawable) {
            MapTileDownloader.this.removeTileFromQueues(mapTileRequestState.getMapTile());
            mapTileRequestState.getCallback().mapTileRequestCompleted(mapTileRequestState, null);
            BitmapPool.getInstance().asyncRecycle(drawable);
        }
    }
}
