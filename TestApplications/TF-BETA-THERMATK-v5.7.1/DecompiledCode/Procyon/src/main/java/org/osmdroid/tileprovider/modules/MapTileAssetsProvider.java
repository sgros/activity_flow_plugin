// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import java.io.IOException;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;
import android.graphics.drawable.Drawable;
import org.osmdroid.util.TileSystem;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import java.util.concurrent.atomic.AtomicReference;
import android.content.res.AssetManager;

public class MapTileAssetsProvider extends MapTileFileStorageProviderBase
{
    private final AssetManager mAssets;
    private final AtomicReference<ITileSource> mTileSource;
    
    public MapTileAssetsProvider(final IRegisterReceiver registerReceiver, final AssetManager assetManager, final ITileSource tileSource) {
        this(registerReceiver, assetManager, tileSource, Configuration.getInstance().getTileDownloadThreads(), Configuration.getInstance().getTileDownloadMaxQueueSize());
    }
    
    public MapTileAssetsProvider(final IRegisterReceiver registerReceiver, final AssetManager mAssets, final ITileSource tileSource, final int n, final int n2) {
        super(registerReceiver, n, n2);
        this.mTileSource = new AtomicReference<ITileSource>();
        this.setTileSource(tileSource);
        this.mAssets = mAssets;
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
        return "Assets Cache Provider";
    }
    
    @Override
    protected String getThreadGroupName() {
        return "assets";
    }
    
    public TileLoader getTileLoader() {
        return new TileLoader(this.mAssets);
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
        private AssetManager mAssets;
        
        public TileLoader(final AssetManager mAssets) {
            this.mAssets = null;
            this.mAssets = mAssets;
        }
        
        @Override
        public Drawable loadTile(final long n) throws CantContinueException {
            final ITileSource tileSource = MapTileAssetsProvider.this.mTileSource.get();
            if (tileSource == null) {
                return null;
            }
            try {
                return tileSource.getDrawable(this.mAssets.open(tileSource.getTileRelativeFilenameString(n)));
            }
            catch (BitmapTileSourceBase.LowMemoryException ex) {
                throw new CantContinueException(ex);
            }
            catch (IOException ex2) {
                return null;
            }
        }
    }
}
