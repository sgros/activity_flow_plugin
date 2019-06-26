// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.tilesource;

import org.osmdroid.util.MapTileIndex;
import org.osmdroid.tileprovider.util.Counters;
import java.io.File;
import android.os.Build$VERSION;
import android.graphics.Bitmap;
import android.util.Log;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import org.osmdroid.tileprovider.BitmapPool;
import android.graphics.BitmapFactory$Options;
import android.graphics.drawable.Drawable;
import java.io.InputStream;
import java.util.Random;

public abstract class BitmapTileSourceBase implements ITileSource
{
    private static int globalOrdinal;
    protected String mCopyright;
    protected final String mImageFilenameEnding;
    private final int mMaximumZoomLevel;
    private final int mMinimumZoomLevel;
    protected String mName;
    private final int mOrdinal;
    private final int mTileSizePixels;
    protected final Random random;
    
    public BitmapTileSourceBase(final String mName, final int mMinimumZoomLevel, final int mMaximumZoomLevel, final int mTileSizePixels, final String mImageFilenameEnding, final String mCopyright) {
        this.random = new Random();
        final int globalOrdinal = BitmapTileSourceBase.globalOrdinal;
        BitmapTileSourceBase.globalOrdinal = globalOrdinal + 1;
        this.mOrdinal = globalOrdinal;
        this.mName = mName;
        this.mMinimumZoomLevel = mMinimumZoomLevel;
        this.mMaximumZoomLevel = mMaximumZoomLevel;
        this.mTileSizePixels = mTileSizePixels;
        this.mImageFilenameEnding = mImageFilenameEnding;
        this.mCopyright = mCopyright;
    }
    
    @Override
    public Drawable getDrawable(final InputStream inputStream) throws LowMemoryException {
        try {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            BitmapPool.getInstance().applyReusableOptions(bitmapFactory$Options, this.mTileSizePixels, this.mTileSizePixels);
            final Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, (Rect)null, bitmapFactory$Options);
            if (decodeStream != null) {
                return (Drawable)new ReusableBitmapDrawable(decodeStream);
            }
            goto Label_0081;
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("#547 Error loading bitmap");
            sb.append(this.pathBase());
            Log.w("OsmDroid", sb.toString(), (Throwable)ex);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            Log.e("OsmDroid", "OutOfMemoryError loading bitmap");
            System.gc();
            throw new LowMemoryException(outOfMemoryError);
        }
    }
    
    @Override
    public Drawable getDrawable(final String str) throws LowMemoryException {
        try {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            BitmapPool.getInstance().applyReusableOptions(bitmapFactory$Options, this.mTileSizePixels, this.mTileSizePixels);
            Bitmap bitmap;
            if (Build$VERSION.SDK_INT == 15) {
                bitmap = BitmapFactory.decodeFile(str);
            }
            else {
                bitmap = BitmapFactory.decodeFile(str, bitmapFactory$Options);
            }
            if (bitmap != null) {
                return (Drawable)new ReusableBitmapDrawable(bitmap);
            }
            if (new File(str).exists()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" is an invalid image file, deleting...");
                Log.d("OsmDroid", sb.toString());
                try {
                    new File(str).delete();
                    goto Label_0243;
                }
                catch (Throwable t) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Error deleting invalid file: ");
                    sb2.append(str);
                    Log.e("OsmDroid", sb2.toString(), t);
                    goto Label_0243;
                }
                goto Label_0243;
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Request tile: ");
            sb3.append(str);
            sb3.append(" does not exist");
            Log.d("OsmDroid", sb3.toString());
            goto Label_0243;
        }
        catch (Exception ex) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Unexpected error loading bitmap: ");
            sb4.append(str);
            Log.e("OsmDroid", sb4.toString(), (Throwable)ex);
            ++Counters.tileDownloadErrors;
            System.gc();
        }
        catch (OutOfMemoryError outOfMemoryError) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("OutOfMemoryError loading bitmap: ");
            sb5.append(str);
            Log.e("OsmDroid", sb5.toString());
            System.gc();
            throw new LowMemoryException(outOfMemoryError);
        }
    }
    
    @Override
    public int getMaximumZoomLevel() {
        return this.mMaximumZoomLevel;
    }
    
    @Override
    public int getMinimumZoomLevel() {
        return this.mMinimumZoomLevel;
    }
    
    @Override
    public String getTileRelativeFilenameString(final long n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.pathBase());
        sb.append('/');
        sb.append(MapTileIndex.getZoom(n));
        sb.append('/');
        sb.append(MapTileIndex.getX(n));
        sb.append('/');
        sb.append(MapTileIndex.getY(n));
        sb.append(this.imageFilenameEnding());
        return sb.toString();
    }
    
    @Override
    public int getTileSizePixels() {
        return this.mTileSizePixels;
    }
    
    public String imageFilenameEnding() {
        return this.mImageFilenameEnding;
    }
    
    @Override
    public String name() {
        return this.mName;
    }
    
    public String pathBase() {
        return this.mName;
    }
    
    @Override
    public String toString() {
        return this.name();
    }
    
    public static final class LowMemoryException extends Exception
    {
        public LowMemoryException(final Throwable cause) {
            super(cause);
        }
    }
}
