// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import menion.android.whereyougo.preferences.Preferences;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class Images
{
    public static final Bitmap IMAGE_EMPTY_B;
    public static final int SIZE_BIG;
    public static final int SIZE_HUGE;
    public static final int SIZE_MEDIUM;
    public static final int SIZE_SMALL;
    private static final String TAG = "Images";
    
    static {
        SIZE_HUGE = (int)Utils.getDpPixels(48.0f);
        SIZE_BIG = (int)Utils.getDpPixels(32.0f);
        SIZE_MEDIUM = (int)Utils.getDpPixels(24.0f);
        SIZE_SMALL = (int)Utils.getDpPixels(16.0f);
        IMAGE_EMPTY_B = getImageB(2130837578);
    }
    
    public static Bitmap getImageB(final int i) {
        Label_0010: {
            if (i > 0) {
                break Label_0010;
            }
            while (true) {
                Bitmap bitmap;
                try {
                    bitmap = Images.IMAGE_EMPTY_B;
                    return bitmap;
                    bitmap = BitmapFactory.decodeResource(A.getApp().getResources(), i);
                    return bitmap;
                }
                catch (Exception ex) {
                    Logger.w("Images", "getImageB(" + i + "), e:" + ex.toString());
                    bitmap = Images.IMAGE_EMPTY_B;
                    return bitmap;
                }
                return bitmap;
            }
        }
    }
    
    public static Bitmap getImageB(final int n, final int n2) {
        return resizeBitmap(getImageB(n), n2);
    }
    
    public static Drawable getImageD(final int n) {
        try {
            final Drawable drawable = A.getApp().getResources().getDrawable(n);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Drawable getImageD(final int n, final int n2) {
        Drawable sizeOptimizedIcon;
        if (A.getApp() == null) {
            sizeOptimizedIcon = null;
        }
        else {
            sizeOptimizedIcon = getSizeOptimizedIcon(A.getApp().getResources().getDrawable(n), n2);
        }
        return sizeOptimizedIcon;
    }
    
    public static Drawable getImageD(final Bitmap bitmap) {
        final BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        ((Drawable)bitmapDrawable).setBounds(0, 0, ((Drawable)bitmapDrawable).getIntrinsicWidth(), ((Drawable)bitmapDrawable).getIntrinsicHeight());
        return (Drawable)bitmapDrawable;
    }
    
    public static Drawable getSizeOptimizedIcon(Drawable imageD, final int n) {
        if (imageD == null) {
            imageD = getImageD(2130837578);
        }
        else {
            imageD.setBounds(0, 0, n, n);
            imageD.invalidateSelf();
        }
        return imageD;
    }
    
    public static Bitmap overlayBitmapToCenter(final Bitmap bitmap, final Bitmap bitmap2) {
        final int max = Math.max(bitmap.getWidth(), bitmap2.getWidth());
        final int max2 = Math.max(bitmap.getHeight(), bitmap2.getHeight());
        final float n = (float)(max - bitmap.getWidth());
        final float n2 = (float)(max2 - bitmap.getHeight());
        final float n3 = (float)(max - bitmap2.getWidth());
        final float n4 = (float)(max2 - bitmap2.getHeight());
        final Bitmap bitmap3 = Bitmap.createBitmap(max, max2, bitmap.getConfig());
        final Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap, n * 0.5f, n2 * 0.5f, (Paint)null);
        canvas.drawBitmap(bitmap2, n3 * 0.5f, n4 * 0.5f, (Paint)null);
        return bitmap3;
    }
    
    public static Bitmap resizeBitmap(final Bitmap bitmap) {
        Bitmap resizeBitmap = bitmap;
        if (Preferences.APPEARANCE_IMAGE_STRETCH) {
            resizeBitmap = resizeBitmap(bitmap, Const.SCREEN_WIDTH);
        }
        return resizeBitmap;
    }
    
    public static Bitmap resizeBitmap(Bitmap resizeBitmap, final int n) {
        if (resizeBitmap == null) {
            resizeBitmap = null;
        }
        else {
            resizeBitmap = resizeBitmap(resizeBitmap, n, resizeBitmap.getHeight() * n / resizeBitmap.getWidth());
        }
        return resizeBitmap;
    }
    
    public static Bitmap resizeBitmap(final Bitmap bitmap, final int n, final int n2) {
        Bitmap scaledBitmap = bitmap;
        if (bitmap != null) {
            scaledBitmap = bitmap;
            if (n > 0) {
                if (bitmap.getWidth() == n) {
                    scaledBitmap = bitmap;
                }
                else {
                    scaledBitmap = Bitmap.createScaledBitmap(bitmap, n, n2, true);
                }
            }
        }
        return scaledBitmap;
    }
}
