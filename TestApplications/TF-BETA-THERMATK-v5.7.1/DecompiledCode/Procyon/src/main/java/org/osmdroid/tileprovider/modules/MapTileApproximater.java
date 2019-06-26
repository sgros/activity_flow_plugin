// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.TileSystem;
import android.graphics.Bitmap$Config;
import org.osmdroid.tileprovider.BitmapPool;
import java.util.Iterator;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.graphics.Rect;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osmdroid.config.Configuration;
import java.util.List;

public class MapTileApproximater extends MapTileModuleProviderBase
{
    private final List<MapTileModuleProviderBase> mProviders;
    private int minZoomLevel;
    
    public MapTileApproximater() {
        this(Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
    }
    
    public MapTileApproximater(final int n, final int n2) {
        super(n, n2);
        this.mProviders = new CopyOnWriteArrayList<MapTileModuleProviderBase>();
    }
    
    public static Bitmap approximateTileFromLowerZoom(final BitmapDrawable bitmapDrawable, final long n, int n2) {
        if (n2 <= 0) {
            return null;
        }
        final int width = bitmapDrawable.getBitmap().getWidth();
        final Bitmap tileBitmap = getTileBitmap(width);
        final Canvas canvas = new Canvas(tileBitmap);
        final boolean b = bitmapDrawable instanceof ReusableBitmapDrawable;
        ReusableBitmapDrawable reusableBitmapDrawable;
        if (b) {
            reusableBitmapDrawable = (ReusableBitmapDrawable)bitmapDrawable;
        }
        else {
            reusableBitmapDrawable = null;
        }
        if (b) {
            reusableBitmapDrawable.beginUsingDrawable();
        }
        final int n3 = 0;
        Label_0093: {
            if (!b) {
                break Label_0093;
            }
            int n4 = n3;
            Label_0198: {
                try {
                    if (reusableBitmapDrawable.isBitmapValid()) {
                        break Label_0093;
                    }
                    break Label_0198;
                }
                finally {
                    if (b) {
                        reusableBitmapDrawable.finishUsingDrawable();
                    }
                    final int x;
                    Label_0111: {
                        x = MapTileIndex.getX(n);
                    }
                    final int n5 = 1 << n2;
                    final int n6;
                    n2 = x % n5 * n6;
                    final int n7 = MapTileIndex.getY(n) % n5 * n6;
                    canvas.drawBitmap(bitmapDrawable.getBitmap(), new Rect(n2, n7, n2 + n6, n6 + n7), new Rect(0, 0, width, width), (Paint)null);
                    n4 = 1;
                    break Label_0198;
                    n6 = width >> n2;
                    // iftrue(Label_0111:, n6 != 0)
                    // iftrue(Label_0215:, n4 != 0)
                    while (true) {
                        Block_9: {
                            break Block_9;
                            reusableBitmapDrawable.finishUsingDrawable();
                            Label_0208:
                            return null;
                            Label_0215:
                            return tileBitmap;
                        }
                        n4 = n3;
                        continue;
                    }
                }
                // iftrue(Label_0208:, !b)
            }
        }
    }
    
    public static Bitmap approximateTileFromLowerZoom(final MapTileModuleProviderBase mapTileModuleProviderBase, final long n, final int n2) {
        if (n2 <= 0) {
            return null;
        }
        final int n3 = MapTileIndex.getZoom(n) - n2;
        if (n3 < mapTileModuleProviderBase.getMinimumZoomLevel()) {
            return null;
        }
        if (n3 > mapTileModuleProviderBase.getMaximumZoomLevel()) {
            return null;
        }
        final long tileIndex = MapTileIndex.getTileIndex(n3, MapTileIndex.getX(n) >> n2, MapTileIndex.getY(n) >> n2);
        try {
            final Drawable loadTile = mapTileModuleProviderBase.getTileLoader().loadTile(tileIndex);
            if (!(loadTile instanceof BitmapDrawable)) {
                return null;
            }
            return approximateTileFromLowerZoom((BitmapDrawable)loadTile, n, n2);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private void computeZoomLevels() {
        this.minZoomLevel = 0;
        final Iterator<MapTileModuleProviderBase> iterator = this.mProviders.iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final int minimumZoomLevel = iterator.next().getMinimumZoomLevel();
            if (n != 0) {
                this.minZoomLevel = minimumZoomLevel;
                n = 0;
            }
            else {
                this.minZoomLevel = Math.min(this.minZoomLevel, minimumZoomLevel);
            }
        }
    }
    
    public static Bitmap getTileBitmap(final int n) {
        final Bitmap obtainSizedBitmapFromPool = BitmapPool.getInstance().obtainSizedBitmapFromPool(n, n);
        if (obtainSizedBitmapFromPool != null) {
            return obtainSizedBitmapFromPool;
        }
        return Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
    }
    
    public void addProvider(final MapTileModuleProviderBase mapTileModuleProviderBase) {
        this.mProviders.add(mapTileModuleProviderBase);
        this.computeZoomLevels();
    }
    
    public Bitmap approximateTileFromLowerZoom(final long n) {
        for (int n2 = 1; MapTileIndex.getZoom(n) - n2 >= 0; ++n2) {
            final Bitmap approximateTileFromLowerZoom = this.approximateTileFromLowerZoom(n, n2);
            if (approximateTileFromLowerZoom != null) {
                return approximateTileFromLowerZoom;
            }
        }
        return null;
    }
    
    public Bitmap approximateTileFromLowerZoom(final long n, final int n2) {
        final Iterator<MapTileModuleProviderBase> iterator = this.mProviders.iterator();
        while (iterator.hasNext()) {
            final Bitmap approximateTileFromLowerZoom = approximateTileFromLowerZoom(iterator.next(), n, n2);
            if (approximateTileFromLowerZoom != null) {
                return approximateTileFromLowerZoom;
            }
        }
        return null;
    }
    
    @Override
    public void detach() {
        super.detach();
        this.mProviders.clear();
    }
    
    @Override
    public int getMaximumZoomLevel() {
        return TileSystem.getMaximumZoomLevel();
    }
    
    @Override
    public int getMinimumZoomLevel() {
        return this.minZoomLevel;
    }
    
    @Override
    protected String getName() {
        return "Offline Tile Approximation Provider";
    }
    
    @Override
    protected String getThreadGroupName() {
        return "approximater";
    }
    
    public TileLoader getTileLoader() {
        return new TileLoader();
    }
    
    @Override
    public boolean getUsesDataConnection() {
        return false;
    }
    
    @Deprecated
    @Override
    public void setTileSource(final ITileSource tileSource) {
    }
    
    protected class TileLoader extends MapTileModuleProviderBase.TileLoader
    {
        @Override
        public Drawable loadTile(final long n) {
            final Bitmap approximateTileFromLowerZoom = MapTileApproximater.this.approximateTileFromLowerZoom(n);
            if (approximateTileFromLowerZoom != null) {
                final BitmapDrawable bitmapDrawable = new BitmapDrawable(approximateTileFromLowerZoom);
                ExpirableBitmapDrawable.setState((Drawable)bitmapDrawable, -3);
                return (Drawable)bitmapDrawable;
            }
            return null;
        }
    }
}
