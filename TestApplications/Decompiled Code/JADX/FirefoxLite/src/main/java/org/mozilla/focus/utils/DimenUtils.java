package org.mozilla.focus.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import org.mozilla.icon.FavIconUtils;
import org.mozilla.rocket.C0769R;

public class DimenUtils {
    @Deprecated
    public static int getFavIconType(Resources resources, Bitmap bitmap) {
        if (bitmap == null || bitmap.getWidth() < resources.getDimensionPixelSize(C0769R.dimen.favicon_initial_threshold_size)) {
            return 2;
        }
        return bitmap.getWidth() > resources.getDimensionPixelSize(C0769R.dimen.favicon_downscale_threshold_size) ? 1 : 0;
    }

    @Deprecated
    public static Bitmap getRefinedBitmap(Resources resources, Bitmap bitmap, char c) {
        switch (getFavIconType(resources, bitmap)) {
            case 0:
                return bitmap;
            case 1:
                int dimensionPixelSize = resources.getDimensionPixelSize(C0769R.dimen.favicon_target_size);
                return Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, false);
            case 2:
                return getInitialBitmap(resources, bitmap, c);
            default:
                return getInitialBitmap(resources, bitmap, c);
        }
    }

    public static boolean iconTooBlurry(Resources resources, int i) {
        return i < resources.getDimensionPixelSize(C0769R.dimen.favicon_initial_threshold_size);
    }

    private static float getDefaultFaviconTextSize(Resources resources) {
        return resources.getDimension(C0769R.dimen.favicon_initial_text_size);
    }

    private static int getDefaultFaviconBitmapSize(Resources resources) {
        return resources.getDimensionPixelSize(C0769R.dimen.favicon_target_size);
    }

    public static Bitmap getInitialBitmap(Resources resources, Bitmap bitmap, char c) {
        return FavIconUtils.getInitialBitmap(bitmap, c, getDefaultFaviconTextSize(resources), getDefaultFaviconBitmapSize(resources));
    }

    public static Bitmap getInitialBitmap(Resources resources, char c, int i) {
        return FavIconUtils.getInitialBitmap(c, i, getDefaultFaviconTextSize(resources), getDefaultFaviconBitmapSize(resources));
    }

    static Bitmap getRefinedShortcutIcon(Resources resources, Bitmap bitmap, char c) {
        int dimensionPixelSize = resources.getDimensionPixelSize(C0769R.dimen.shortcut_icon_size);
        if (bitmap == null || bitmap.getWidth() < dimensionPixelSize) {
            return getInitialBitmap(resources, bitmap, c);
        }
        return bitmap.getWidth() > dimensionPixelSize ? Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, false) : bitmap;
    }
}
