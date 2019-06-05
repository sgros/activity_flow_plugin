package org.mozilla.focus.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.p001v4.graphics.drawable.DrawableCompat;

public class DrawableUtils {
    public static Drawable loadAndTintDrawable(Context context, int i, int i2) {
        Drawable wrap = DrawableCompat.wrap(context.getResources().getDrawable(i, context.getTheme()).mutate());
        DrawableCompat.setTint(wrap, i2);
        return wrap;
    }

    public static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Drawable getAndroidDrawable(Context context, String str) {
        int identifier = context.getResources().getIdentifier(str, "drawable", context.getPackageName());
        if (identifier == 0) {
            return null;
        }
        return context.getDrawable(identifier);
    }
}
