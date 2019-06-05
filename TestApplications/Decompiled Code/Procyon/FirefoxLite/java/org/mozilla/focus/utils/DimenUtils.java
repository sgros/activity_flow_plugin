// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.mozilla.icon.FavIconUtils;
import android.graphics.Bitmap;
import android.content.res.Resources;

public class DimenUtils
{
    private static int getDefaultFaviconBitmapSize(final Resources resources) {
        return resources.getDimensionPixelSize(2131165339);
    }
    
    private static float getDefaultFaviconTextSize(final Resources resources) {
        return resources.getDimension(2131165337);
    }
    
    @Deprecated
    public static int getFavIconType(final Resources resources, final Bitmap bitmap) {
        if (bitmap == null || bitmap.getWidth() < resources.getDimensionPixelSize(2131165338)) {
            return 2;
        }
        if (bitmap.getWidth() > resources.getDimensionPixelSize(2131165336)) {
            return 1;
        }
        return 0;
    }
    
    public static Bitmap getInitialBitmap(final Resources resources, final char c, final int n) {
        return FavIconUtils.getInitialBitmap(c, n, getDefaultFaviconTextSize(resources), getDefaultFaviconBitmapSize(resources));
    }
    
    public static Bitmap getInitialBitmap(final Resources resources, final Bitmap bitmap, final char c) {
        return FavIconUtils.getInitialBitmap(bitmap, c, getDefaultFaviconTextSize(resources), getDefaultFaviconBitmapSize(resources));
    }
    
    @Deprecated
    public static Bitmap getRefinedBitmap(final Resources resources, final Bitmap bitmap, final char c) {
        switch (getFavIconType(resources, bitmap)) {
            default: {
                return getInitialBitmap(resources, bitmap, c);
            }
            case 2: {
                return getInitialBitmap(resources, bitmap, c);
            }
            case 1: {
                final int dimensionPixelSize = resources.getDimensionPixelSize(2131165339);
                return Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, false);
            }
            case 0: {
                return bitmap;
            }
        }
    }
    
    static Bitmap getRefinedShortcutIcon(final Resources resources, final Bitmap bitmap, final char c) {
        final int dimensionPixelSize = resources.getDimensionPixelSize(2131165439);
        if (bitmap == null || bitmap.getWidth() < dimensionPixelSize) {
            return getInitialBitmap(resources, bitmap, c);
        }
        if (bitmap.getWidth() > dimensionPixelSize) {
            return Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, false);
        }
        return bitmap;
    }
    
    public static boolean iconTooBlurry(final Resources resources, final int n) {
        return n < resources.getDimensionPixelSize(2131165338);
    }
}
