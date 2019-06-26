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

public class MapTileSqlCacheProvider extends MapTileFileStorageProviderBase
{
    private static final String[] columns;
    private final AtomicReference<ITileSource> mTileSource;
    private SqlTileWriter mWriter;
    
    static {
        columns = new String[] { "tile", "expires" };
    }
    
    public MapTileSqlCacheProvider(final IRegisterReceiver registerReceiver, final ITileSource tileSource) {
        super(registerReceiver, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        this.mTileSource = new AtomicReference<ITileSource>();
        this.setTileSource(tileSource);
        this.mWriter = new SqlTileWriter();
    }
    
    @Override
    public void detach() {
        final SqlTileWriter mWriter = this.mWriter;
        if (mWriter != null) {
            mWriter.onDetach();
        }
        this.mWriter = null;
        super.detach();
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
        return "SQL Cache Archive Provider";
    }
    
    @Override
    protected String getThreadGroupName() {
        return "sqlcache";
    }
    
    public TileLoader getTileLoader() {
        return new TileLoader();
    }
    
    @Override
    public boolean getUsesDataConnection() {
        return false;
    }
    
    @Override
    protected void onMediaMounted() {
    }
    
    @Override
    protected void onMediaUnmounted() {
        final SqlTileWriter mWriter = this.mWriter;
        if (mWriter != null) {
            mWriter.onDetach();
        }
        this.mWriter = new SqlTileWriter();
    }
    
    @Override
    public void setTileSource(final ITileSource newValue) {
        this.mTileSource.set(newValue);
    }
    
    protected class TileLoader extends MapTileModuleProviderBase.TileLoader
    {
        @Override
        public Drawable loadTile(final long n) throws CantContinueException {
            final ITileSource tileSource = MapTileSqlCacheProvider.this.mTileSource.get();
            if (tileSource == null) {
                return null;
            }
            if (MapTileSqlCacheProvider.this.mWriter != null) {
                try {
                    final Drawable loadTile = MapTileSqlCacheProvider.this.mWriter.loadTile(tileSource, n);
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
            Log.d("OsmDroid", "TileLoader failed to load tile due to mWriter being null (map shutdown?)");
            return null;
        }
    }
}
