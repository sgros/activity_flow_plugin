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

public class MapTileFilesystemProvider extends MapTileFileStorageProviderBase {
    private final AtomicReference<ITileSource> mTileSource;
    private final TileWriter mWriter;

    protected class TileLoader extends org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        public Drawable loadTile(long j) throws CantContinueException {
            String str = "OsmDroid";
            ITileSource iTileSource = (ITileSource) MapTileFilesystemProvider.this.mTileSource.get();
            if (iTileSource == null) {
                return null;
            }
            try {
                Drawable loadTile = MapTileFilesystemProvider.this.mWriter.loadTile(iTileSource, j);
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
    }

    /* Access modifiers changed, original: protected */
    public String getName() {
        return "File System Cache Provider";
    }

    /* Access modifiers changed, original: protected */
    public String getThreadGroupName() {
        return "filesystem";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    public MapTileFilesystemProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource) {
        this(iRegisterReceiver, iTileSource, Configuration.getInstance().getExpirationExtendedDuration() + 604800000);
    }

    public MapTileFilesystemProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, long j) {
        this(iRegisterReceiver, iTileSource, j, Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
    }

    public MapTileFilesystemProvider(IRegisterReceiver iRegisterReceiver, ITileSource iTileSource, long j, int i, int i2) {
        super(iRegisterReceiver, i, i2);
        this.mWriter = new TileWriter();
        this.mTileSource = new AtomicReference();
        setTileSource(iTileSource);
        this.mWriter.setMaximumCachedFileAge(j);
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

    public void setTileSource(ITileSource iTileSource) {
        this.mTileSource.set(iTileSource);
    }
}
