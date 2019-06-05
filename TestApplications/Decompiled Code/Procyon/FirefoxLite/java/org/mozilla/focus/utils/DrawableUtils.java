// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.content.Context;

public class DrawableUtils
{
    public static Drawable getAndroidDrawable(final Context context, final String s) {
        final int identifier = context.getResources().getIdentifier(s, "drawable", context.getPackageName());
        if (identifier == 0) {
            return null;
        }
        return context.getDrawable(identifier);
    }
    
    public static Bitmap getBitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    
    public static Drawable loadAndTintDrawable(final Context context, final int n, final int n2) {
        final Drawable wrap = DrawableCompat.wrap(context.getResources().getDrawable(n, context.getTheme()).mutate());
        DrawableCompat.setTint(wrap, n2);
        return wrap;
    }
}
