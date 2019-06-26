package org.osmdroid.tileprovider.tilesource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.util.Random;
import org.osmdroid.tileprovider.BitmapPool;
import org.osmdroid.tileprovider.ReusableBitmapDrawable;
import org.osmdroid.tileprovider.util.Counters;
import org.osmdroid.util.MapTileIndex;

public abstract class BitmapTileSourceBase implements ITileSource {
    private static int globalOrdinal;
    protected String mCopyright;
    protected final String mImageFilenameEnding;
    private final int mMaximumZoomLevel;
    private final int mMinimumZoomLevel;
    protected String mName;
    private final int mOrdinal;
    private final int mTileSizePixels;
    protected final Random random = new Random();

    public static final class LowMemoryException extends Exception {
        public LowMemoryException(Throwable th) {
            super(th);
        }
    }

    public BitmapTileSourceBase(String str, int i, int i2, int i3, String str2, String str3) {
        int i4 = globalOrdinal;
        globalOrdinal = i4 + 1;
        this.mOrdinal = i4;
        this.mName = str;
        this.mMinimumZoomLevel = i;
        this.mMaximumZoomLevel = i2;
        this.mTileSizePixels = i3;
        this.mImageFilenameEnding = str2;
        this.mCopyright = str3;
    }

    public String name() {
        return this.mName;
    }

    public String pathBase() {
        return this.mName;
    }

    public String imageFilenameEnding() {
        return this.mImageFilenameEnding;
    }

    public int getMinimumZoomLevel() {
        return this.mMinimumZoomLevel;
    }

    public int getMaximumZoomLevel() {
        return this.mMaximumZoomLevel;
    }

    public int getTileSizePixels() {
        return this.mTileSizePixels;
    }

    public String toString() {
        return name();
    }

    public Drawable getDrawable(String str) throws LowMemoryException {
        StringBuilder stringBuilder;
        String str2 = "OsmDroid";
        try {
            Bitmap decodeFile;
            Options options = new Options();
            BitmapPool.getInstance().applyReusableOptions(options, this.mTileSizePixels, this.mTileSizePixels);
            if (VERSION.SDK_INT == 15) {
                decodeFile = BitmapFactory.decodeFile(str);
            } else {
                decodeFile = BitmapFactory.decodeFile(str, options);
            }
            if (decodeFile != null) {
                return new ReusableBitmapDrawable(decodeFile);
            }
            StringBuilder stringBuilder2;
            if (new File(str).exists()) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(" is an invalid image file, deleting...");
                Log.d(str2, stringBuilder2.toString());
                try {
                    new File(str).delete();
                } catch (Throwable th) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Error deleting invalid file: ");
                    stringBuilder.append(str);
                    Log.e(str2, stringBuilder.toString(), th);
                }
            } else {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Request tile: ");
                stringBuilder2.append(str);
                stringBuilder2.append(" does not exist");
                Log.d(str2, stringBuilder2.toString());
            }
            return null;
        } catch (OutOfMemoryError e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("OutOfMemoryError loading bitmap: ");
            stringBuilder.append(str);
            Log.e(str2, stringBuilder.toString());
            System.gc();
            throw new LowMemoryException(e);
        } catch (Exception e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected error loading bitmap: ");
            stringBuilder.append(str);
            Log.e(str2, stringBuilder.toString(), e2);
            Counters.tileDownloadErrors++;
            System.gc();
        }
    }

    public String getTileRelativeFilenameString(long j) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pathBase());
        stringBuilder.append('/');
        stringBuilder.append(MapTileIndex.getZoom(j));
        stringBuilder.append('/');
        stringBuilder.append(MapTileIndex.getX(j));
        stringBuilder.append('/');
        stringBuilder.append(MapTileIndex.getY(j));
        stringBuilder.append(imageFilenameEnding());
        return stringBuilder.toString();
    }

    public Drawable getDrawable(InputStream inputStream) throws LowMemoryException {
        String str = "OsmDroid";
        try {
            Options options = new Options();
            BitmapPool.getInstance().applyReusableOptions(options, this.mTileSizePixels, this.mTileSizePixels);
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            if (decodeStream != null) {
                return new ReusableBitmapDrawable(decodeStream);
            }
        } catch (OutOfMemoryError e) {
            Log.e(str, "OutOfMemoryError loading bitmap");
            System.gc();
            throw new LowMemoryException(e);
        } catch (Exception e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("#547 Error loading bitmap");
            stringBuilder.append(pathBase());
            Log.w(str, stringBuilder.toString(), e2);
        }
        return null;
    }
}
