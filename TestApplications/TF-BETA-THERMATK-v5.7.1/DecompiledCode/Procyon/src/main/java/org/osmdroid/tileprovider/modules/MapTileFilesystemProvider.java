// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import org.osmdroid.util.MapTileIndex;
import android.util.Log;
import org.osmdroid.tileprovider.util.Counters;
import android.graphics.drawable.Drawable;
import org.osmdroid.util.TileSystem;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.util.concurrent.atomic.AtomicReference;

public class MapTileFilesystemProvider extends MapTileFileStorageProviderBase
{
    private final AtomicReference<ITileSource> mTileSource;
    private final TileWriter mWriter;
    
    public MapTileFilesystemProvider(final IRegisterReceiver registerReceiver, final ITileSource tileSource) {
        this(registerReceiver, tileSource, Configuration.getInstance().getExpirationExtendedDuration() + 604800000L);
    }
    
    public MapTileFilesystemProvider(final IRegisterReceiver registerReceiver, final ITileSource tileSource, final long n) {
        this(registerReceiver, tileSource, n, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
    }
    
    public MapTileFilesystemProvider(final IRegisterReceiver registerReceiver, final ITileSource tileSource, final long maximumCachedFileAge, final int n, final int n2) {
        super(registerReceiver, n, n2);
        this.mWriter = new TileWriter();
        this.mTileSource = new AtomicReference<ITileSource>();
        this.setTileSource(tileSource);
        this.mWriter.setMaximumCachedFileAge(maximumCachedFileAge);
    }
    
    @Override
    public int getMaximumZoomLevel() {
        final ITileSource tileSource = this.mTileSource.get();
        int n;
        if (tileSource != null) {
            n = tileSource.getMaximumZoomLevel();
        }
        else {
            n = TileSystem.getMaximumZoomLevel();
        }
        return n;
    }
    
    @Override
    public int getMinimumZoomLevel() {
        final ITileSource tileSource = this.mTileSource.get();
        int minimumZoomLevel;
        if (tileSource != null) {
            minimumZoomLevel = tileSource.getMinimumZoomLevel();
        }
        else {
            minimumZoomLevel = 0;
        }
        return minimumZoomLevel;
    }
    
    @Override
    protected String getName() {
        return "File System Cache Provider";
    }
    
    @Override
    protected String getThreadGroupName() {
        return "filesystem";
    }
    
    public TileLoader getTileLoader() {
        return new TileLoader();
    }
    
    @Override
    public boolean getUsesDataConnection() {
        return false;
    }
    
    @Override
    public void setTileSource(final ITileSource newValue) {
        this.mTileSource.set(newValue);
    }
    
    protected class TileLoader extends MapTileModuleProviderBase.TileLoader
    {
        @Override
        public Drawable loadTile(final long n) throws CantContinueException {
            final ITileSource tileSource = MapTileFilesystemProvider.this.mTileSource.get();
            if (tileSource == null) {
                return null;
            }
            try {
                final Drawable loadTile = MapTileFilesystemProvider.this.mWriter.loadTile(tileSource, n);
                if (loadTile == null) {
                    ++Counters.fileCacheMiss;
                }
                else {
                    ++Counters.fileCacheHit;
                }
                return loadTile;
            }
            catch (Throwable t) {
                Log.e("OsmDroid", "Error loading tile", t);
                return null;
            }
            catch (BitmapTileSourceBase.LowMemoryException obj) {
                final StringBuilder sb = new StringBuilder();
                sb.append("LowMemoryException downloading MapTile: ");
                sb.append(MapTileIndex.toString(n));
                sb.append(" : ");
                sb.append(obj);
                Log.w("OsmDroid", sb.toString());
                ++Counters.fileCacheOOM;
                throw new CantContinueException(obj);
            }
        }
    }
}
