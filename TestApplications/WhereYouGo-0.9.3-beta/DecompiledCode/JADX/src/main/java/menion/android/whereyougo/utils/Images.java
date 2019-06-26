package menion.android.whereyougo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.preferences.Preferences;

public class Images {
    public static final Bitmap IMAGE_EMPTY_B = getImageB(C0254R.C0252drawable.var_empty);
    public static final int SIZE_BIG = ((int) Utils.getDpPixels(32.0f));
    public static final int SIZE_HUGE = ((int) Utils.getDpPixels(48.0f));
    public static final int SIZE_MEDIUM = ((int) Utils.getDpPixels(24.0f));
    public static final int SIZE_SMALL = ((int) Utils.getDpPixels(16.0f));
    private static final String TAG = "Images";

    public static Bitmap getImageB(int id) {
        if (id > 0) {
            return BitmapFactory.decodeResource(C0322A.getApp().getResources(), id);
        }
        try {
            return IMAGE_EMPTY_B;
        } catch (Exception e) {
            Logger.m26w(TAG, "getImageB(" + id + "), e:" + e.toString());
            return IMAGE_EMPTY_B;
        }
    }

    public static Bitmap getImageB(int id, int width) {
        return resizeBitmap(getImageB(id), width);
    }

    public static Drawable getImageD(Bitmap bitmap) {
        Drawable draw = new BitmapDrawable(bitmap);
        draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
        return draw;
    }

    public static Drawable getImageD(int id) {
        try {
            Drawable draw = C0322A.getApp().getResources().getDrawable(id);
            draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
            return draw;
        } catch (Exception e) {
            return null;
        }
    }

    public static Drawable getImageD(int id, int size) {
        if (C0322A.getApp() == null) {
            return null;
        }
        return getSizeOptimizedIcon(C0322A.getApp().getResources().getDrawable(id), size);
    }

    public static Drawable getSizeOptimizedIcon(Drawable draw, int newSize) {
        if (draw == null) {
            return getImageD((int) C0254R.C0252drawable.var_empty);
        }
        draw.setBounds(0, 0, newSize, newSize);
        draw.invalidateSelf();
        return draw;
    }

    public static Bitmap resizeBitmap(Bitmap bmp) {
        if (Preferences.APPEARANCE_IMAGE_STRETCH) {
            return resizeBitmap(bmp, Const.SCREEN_WIDTH);
        }
        return bmp;
    }

    public static Bitmap resizeBitmap(Bitmap bmp, int newWidth) {
        if (bmp == null) {
            return null;
        }
        return resizeBitmap(bmp, newWidth, (bmp.getHeight() * newWidth) / bmp.getWidth());
    }

    public static Bitmap resizeBitmap(Bitmap bmp, int newWidth, int newHeight) {
        return (bmp == null || newWidth <= 0 || bmp.getWidth() == newWidth) ? bmp : Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);
    }

    public static Bitmap overlayBitmapToCenter(Bitmap bitmap1, Bitmap bitmap2) {
        int bitmapWidth = Math.max(bitmap1.getWidth(), bitmap2.getWidth());
        int bitmapHeight = Math.max(bitmap1.getHeight(), bitmap2.getHeight());
        float margin1Left = ((float) (bitmapWidth - bitmap1.getWidth())) * 0.5f;
        float margin1Top = ((float) (bitmapHeight - bitmap1.getHeight())) * 0.5f;
        float margin2Left = ((float) (bitmapWidth - bitmap2.getWidth())) * 0.5f;
        float margin2Top = ((float) (bitmapHeight - bitmap2.getHeight())) * 0.5f;
        Bitmap overlayBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, bitmap1.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, margin1Left, margin1Top, null);
        canvas.drawBitmap(bitmap2, margin2Left, margin2Top, null);
        return overlayBitmap;
    }
}
