package org.osmdroid.tileprovider.modules;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase.LowMemoryException;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileSqlCacheProvider extends MapTileFileStorageProviderBase {
    private static final String[] columns = new String[]{"tile", "expires"};
    private final AtomicReference<ITileSource> mTileSource = new AtomicReference();
    private SqlTileWriter mWriter;

    protected class TileLoader extends org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        public Drawable loadTile(long j) throws CantContinueException {
            ITileSource iTileSource = (ITileSource) MapTileSqlCacheProvider.this.mTileSource.get();
            if (iTileSource == null) {
                return null;
            }
            String str = "OsmDroid";
            if (MapTileSqlCacheProvider.this.mWriter != null) {
                try {
                    Drawable loadTile = MapTileSqlCacheProvider.this.mWriter.loadTile(iTileSource, j);
                    if (loadTile == null) {
                        Counters.fileCacheMiss++;
                    } else {
                        Counters.fileCacheHit++;
                    }
                    return loadTile;
                } catch (LowMemoryException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("LowMemoryException downloading MapTile: ");
                    stringBuilder.append(MapTileIndex.toString(j));
                    stringBuilder.append(" : ");
                    stringBuilder.append(e);
                    Log.w(str, stringBuilder.toString());
                    Counters.fileCacheOOM++;
                    throw new CantContinueException(e);
                } catch (Throwable th) {
                    Log.e(str, "Error loading tile", th);
                    return null;
                }
            }
            Log.d(str, "TileLoader failed to load tile due to mWriter being null (map shutdown?)");
            return null;
        }
    }

    /* Access modifiers changed, original: protected */
    public String getName() {
        return "SQL Cache Archive Provider";
    }

    /* Access modifiers changed, original: protected */
    public String getThreadGroupName() {
        return "sqlcache";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onMediaMounted() {
    }

    public MapTileSqlCacheProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource) {
        super(iRegisterReceiver, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
        setTileSource(iTileSource);
        this.mWriter = new SqlTileWriter();
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        ITileSource iTileSource = (ITileSource) this.mTileSource.get();
        return iTileSource != null ? iTileSource.getMinimumZoomLevel() : 0;
    }

    public int getMaximumZoomLevel() {
        ITileSource iTileSource = (ITileSource) this.mTileSource.get();
        if (iTileSource != null) {
            return iTileSource.getMaximumZoomLevel();
        }
        return TileSystem.getMaximumZoomLevel();
    }

    /* Access modifiers changed, original: protected */
    public void onMediaUnmounted() {
        SqlTileWriter sqlTileWriter = this.mWriter;
        if (sqlTileWriter != null) {
            sqlTileWriter.onDetach();
        }
        this.mWriter = new SqlTileWriter();
    }

    public void setTileSource(ITileSource iTileSource) {
        this.mTileSource.set(iTileSource);
    }

    public void detach() {
        SqlTileWriter sqlTileWriter = this.mWriter;
        if (sqlTileWriter != null) {
            sqlTileWriter.onDetach();
        }
        this.mWriter = null;
        super.detach();
    }
}
