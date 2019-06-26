package org.osmdroid.tileprovider.modules;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.ExpirableBitmapDrawable;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.util.TileSystem;

public class MapTileApproximater extends MapTileModuleProviderBase {
    private final List<MapTileModuleProviderBase> mProviders;
    private int minZoomLevel;

    protected class TileLoader extends org.osmdroid.tileprovider.modules.MapTileModuleProviderBase.TileLoader {
        protected TileLoader() {
            super();
        }

        public Drawable loadTile(long j) {
            Bitmap approximateTileFromLowerZoom = MapTileApproximater.this.approximateTileFromLowerZoom(j);
            if (approximateTileFromLowerZoom == null) {
                return null;
            }
            BitmapDrawable bitmapDrawable = new BitmapDrawable(approximateTileFromLowerZoom);
            ExpirableBitmapDrawable.setState(bitmapDrawable, -3);
            return bitmapDrawable;
        }
    }

    /* Access modifiers changed, original: protected */
    public String getName() {
        return "Offline Tile Approximation Provider";
    }

    /* Access modifiers changed, original: protected */
    public String getThreadGroupName() {
        return "approximater";
    }

    public boolean getUsesDataConnection() {
        return false;
    }

    @Deprecated
    public void setTileSource(ITileSource iTileSource) {
    }

    public MapTileApproximater() {
        this(Configuration.getInstance().getTileFileSystemThreads(), Configuration.getInstance().getTileFileSystemMaxQueueSize());
    }

    public MapTileApproximater(int i, int i2) {
        super(i, i2);
        this.mProviders = new CopyOnWriteArrayList();
    }

    public void addProvider(MapTileModuleProviderBase mapTileModuleProviderBase) {
        this.mProviders.add(mapTileModuleProviderBase);
        computeZoomLevels();
    }

    private void computeZoomLevels() {
        this.minZoomLevel = 0;
        Object obj = 1;
        for (MapTileModuleProviderBase minimumZoomLevel : this.mProviders) {
            int minimumZoomLevel2 = minimumZoomLevel.getMinimumZoomLevel();
            if (obj != null) {
                this.minZoomLevel = minimumZoomLevel2;
                obj = null;
            } else {
                this.minZoomLevel = Math.min(this.minZoomLevel, minimumZoomLevel2);
            }
        }
    }

    public TileLoader getTileLoader() {
        return new TileLoader();
    }

    public int getMinimumZoomLevel() {
        return this.minZoomLevel;
    }

    public int getMaximumZoomLevel() {
        return TileSystem.getMaximumZoomLevel();
    }

    public Bitmap approximateTileFromLowerZoom(long j) {
        for (int i = 1; MapTileIndex.getZoom(j) - i >= 0; i++) {
            Bitmap approximateTileFromLowerZoom = approximateTileFromLowerZoom(j, i);
            if (approximateTileFromLowerZoom != null) {
                return approximateTileFromLowerZoom;
            }
        }
        return null;
    }

    public Bitmap approximateTileFromLowerZoom(long j, int i) {
        for (MapTileModuleProviderBase approximateTileFromLowerZoom : this.mProviders) {
            Bitmap approximateTileFromLowerZoom2 = approximateTileFromLowerZoom(approximateTileFromLowerZoom, j, i);
            if (approximateTileFromLowerZoom2 != null) {
                return approximateTileFromLowerZoom2;
            }
        }
        return null;
    }

    public static Bitmap approximateTileFromLowerZoom(MapTileModuleProviderBase mapTileModuleProviderBase, long j, int i) {
        if (i <= 0) {
            return null;
        }
        int zoom = MapTileIndex.getZoom(j) - i;
        if (zoom < mapTileModuleProviderBase.getMinimumZoomLevel() || zoom > mapTileModuleProviderBase.getMaximumZoomLevel()) {
            return null;
        }
        try {
            Drawable loadTile = mapTileModuleProviderBase.getTileLoader().loadTile(MapTileIndex.getTileIndex(zoom, MapTileIndex.getX(j) >> i, MapTileIndex.getY(j) >> i));
            if (loadTile instanceof BitmapDrawable) {
                return approximateTileFromLowerZoom((BitmapDrawable) loadTile, j, i);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0062 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0061 A:{RETURN} */
    public static android.graphics.Bitmap approximateTileFromLowerZoom(android.graphics.drawable.BitmapDrawable r10, long r11, int r13) {
        /*
        r0 = 0;
        if (r13 > 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r10.getBitmap();
        r1 = r1.getWidth();
        r2 = getTileBitmap(r1);
        r3 = new android.graphics.Canvas;
        r3.<init>(r2);
        r4 = r10 instanceof org.osmdroid.tileprovider.ReusableBitmapDrawable;
        if (r4 == 0) goto L_0x001d;
    L_0x0019:
        r5 = r10;
        r5 = (org.osmdroid.tileprovider.ReusableBitmapDrawable) r5;
        goto L_0x001e;
    L_0x001d:
        r5 = r0;
    L_0x001e:
        if (r4 == 0) goto L_0x0023;
    L_0x0020:
        r5.beginUsingDrawable();
    L_0x0023:
        r6 = 1;
        r7 = 0;
        if (r4 == 0) goto L_0x0030;
    L_0x0027:
        r8 = r5.isBitmapValid();	 Catch:{ all -> 0x002e }
        if (r8 == 0) goto L_0x005a;
    L_0x002d:
        goto L_0x0030;
    L_0x002e:
        r10 = move-exception;
        goto L_0x0063;
    L_0x0030:
        r8 = r1 >> r13;
        if (r8 != 0) goto L_0x0035;
    L_0x0034:
        goto L_0x005a;
    L_0x0035:
        r9 = org.osmdroid.util.MapTileIndex.getX(r11);	 Catch:{ all -> 0x002e }
        r13 = r6 << r13;
        r9 = r9 % r13;
        r9 = r9 * r8;
        r11 = org.osmdroid.util.MapTileIndex.getY(r11);	 Catch:{ all -> 0x002e }
        r11 = r11 % r13;
        r11 = r11 * r8;
        r12 = new android.graphics.Rect;	 Catch:{ all -> 0x002e }
        r13 = r9 + r8;
        r8 = r8 + r11;
        r12.<init>(r9, r11, r13, r8);	 Catch:{ all -> 0x002e }
        r11 = new android.graphics.Rect;	 Catch:{ all -> 0x002e }
        r11.<init>(r7, r7, r1, r1);	 Catch:{ all -> 0x002e }
        r10 = r10.getBitmap();	 Catch:{ all -> 0x002e }
        r3.drawBitmap(r10, r12, r11, r0);	 Catch:{ all -> 0x002e }
        r7 = 1;
    L_0x005a:
        if (r4 == 0) goto L_0x005f;
    L_0x005c:
        r5.finishUsingDrawable();
    L_0x005f:
        if (r7 != 0) goto L_0x0062;
    L_0x0061:
        return r0;
    L_0x0062:
        return r2;
    L_0x0063:
        if (r4 == 0) goto L_0x0068;
    L_0x0065:
        r5.finishUsingDrawable();
    L_0x0068:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.tileprovider.modules.MapTileApproximater.approximateTileFromLowerZoom(android.graphics.drawable.BitmapDrawable, long, int):android.graphics.Bitmap");
    }

    public static Bitmap getTileBitmap(int i) {
        Bitmap obtainSizedBitmapFromPool = BitmapPool.getInstance().obtainSizedBitmapFromPool(i, i);
        if (obtainSizedBitmapFromPool != null) {
            return obtainSizedBitmapFromPool;
        }
        return Bitmap.createBitmap(i, i, Config.ARGB_8888);
    }

    public void detach() {
        super.detach();
        this.mProviders.clear();
    }
}
