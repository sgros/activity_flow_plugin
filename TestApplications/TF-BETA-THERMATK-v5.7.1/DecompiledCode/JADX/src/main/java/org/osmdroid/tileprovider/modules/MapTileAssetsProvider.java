package org.osmdroid.tileprovider.modules;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase.LowMemoryException;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.TileSystem;

public class MapTileAssetsProvider extends MapTileFileStorageProviderBase {
    private final AssetManager mAssets;
    private final AtomicReference<ITileSource> mTileSource;

    protected class TileLoader extends org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.TileLoader {
        private AssetManager mAssets = null;

        public TileLoader(AssetManager assetManager) {
            super();
            this.mAssets = assetManager;
        }

        public Drawable loadTile(long j) throws CantContinueException {
            ITileSource iTileSource = (ITileSource) MapTileAssetsProvider.this.mTileSource.get();
            if (iTileSource == null) {
                return null;
            }
            try {
                return iTileSource.getDrawable(this.mAssets.open(iTileSource.getTileRelativeFilenameString(j)));
            } catch (IOException unused) {
                return null;
            } catch (LowMemoryException e) {
                throw new CantContinueException(e);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public String getName() {
        return "Assets Cache Provider";
    }

    /* Access modifiers changed, original: protected */
    public String getThreadGroupName() {
        return "assets";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    public MapTileAssetsProvider(IRegisterReceiver iRegisterReceiver, AssetManager assetManager, ITileSource iTileSource) {
        this(iRegisterReceiver, assetManager, iTileSource, Configuration.getInstance().getTileDownloadThreads(), Configuration.getInstance().getTileDownloadMaxQueueSize());
    }

    public MapTileAssetsProvider(IRegisterReceiver iRegisterReceiver, AssetManager assetManager, ITileSource iTileSource, int i, int i2) {
        super(iRegisterReceiver, i, i2);
        this.mTileSource = new AtomicReference();
        setTileSource(iTileSource);
        this.mAssets = assetManager;
    }

    public TileLoader getTileLoader() {
        return new TileLoader(this.mAssets);
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
