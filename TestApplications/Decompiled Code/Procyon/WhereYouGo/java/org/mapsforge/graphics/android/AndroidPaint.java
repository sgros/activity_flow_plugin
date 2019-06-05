// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.graphics.android;

import android.graphics.Paint$Align;
import org.mapsforge.map.graphics.Align;
import android.graphics.Paint$Style;
import org.mapsforge.map.graphics.Style;
import android.graphics.Paint$Cap;
import org.mapsforge.map.graphics.Cap;
import android.graphics.PathEffect;
import android.graphics.DashPathEffect;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Bitmap$Config;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import org.mapsforge.map.graphics.FontFamily;
import org.mapsforge.map.graphics.FontStyle;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Paint;

class AndroidPaint implements Paint
{
    private Bitmap bitmap;
    final android.graphics.Paint paint;
    
    AndroidPaint() {
        this.paint = new android.graphics.Paint(1);
    }
    
    private static int getStyle(final FontStyle obj) {
        int n = 0;
        switch (obj) {
            default: {
                throw new IllegalArgumentException("unknown font style: " + obj);
            }
            case BOLD: {
                n = 1;
                break;
            }
            case BOLD_ITALIC: {
                n = 3;
                break;
            }
            case ITALIC: {
                n = 2;
                break;
            }
            case NORMAL: {
                n = 0;
                break;
            }
        }
        return n;
    }
    
    private static Typeface getTypeface(final FontFamily obj) {
        Typeface typeface = null;
        switch (obj) {
            default: {
                throw new IllegalArgumentException("unknown font family: " + obj);
            }
            case DEFAULT: {
                typeface = Typeface.DEFAULT;
                break;
            }
            case DEFAULT_BOLD: {
                typeface = Typeface.DEFAULT_BOLD;
                break;
            }
            case MONOSPACE: {
                typeface = Typeface.MONOSPACE;
                break;
            }
            case SANS_SERIF: {
                typeface = Typeface.SANS_SERIF;
                break;
            }
            case SERIF: {
                typeface = Typeface.SERIF;
                break;
            }
        }
        return typeface;
    }
    
    @Override
    public void destroy() {
        if (this.bitmap != null) {
            this.paint.setShader((Shader)null);
            this.bitmap.destroy();
        }
    }
    
    @Override
    public int getColor() {
        return this.paint.getColor();
    }
    
    @Override
    public int getTextHeight(final String s) {
        final Rect rect = new Rect();
        this.paint.getTextBounds(s, 0, s.length(), rect);
        return rect.height();
    }
    
    @Override
    public int getTextWidth(final String s) {
        final Rect rect = new Rect();
        this.paint.getTextBounds(s, 0, s.length(), rect);
        return rect.width();
    }
    
    @Override
    public void setBitmapShader(final Bitmap bitmap) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            this.paint.setShader((Shader)new BitmapShader(android.graphics.Bitmap.createBitmap(bitmap.getPixels(), bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888), Shader$TileMode.REPEAT, Shader$TileMode.REPEAT));
        }
    }
    
    @Override
    public void setColor(final int color) {
        this.paint.setColor(color);
    }
    
    @Override
    public void setDashPathEffect(final float[] array) {
        this.paint.setPathEffect((PathEffect)new DashPathEffect(array, 0.0f));
    }
    
    @Override
    public void setStrokeCap(final Cap cap) {
        this.paint.setStrokeCap(Paint$Cap.valueOf(cap.name()));
    }
    
    @Override
    public void setStrokeWidth(final float strokeWidth) {
        this.paint.setStrokeWidth(strokeWidth);
    }
    
    @Override
    public void setStyle(final Style style) {
        this.paint.setStyle(Paint$Style.valueOf(style.name()));
    }
    
    @Override
    public void setTextAlign(final Align align) {
        this.paint.setTextAlign(Paint$Align.valueOf(align.name()));
    }
    
    @Override
    public void setTextSize(final float textSize) {
        this.paint.setTextSize(textSize);
    }
    
    @Override
    public void setTypeface(final FontFamily fontFamily, final FontStyle fontStyle) {
        this.paint.setTypeface(Typeface.create(getTypeface(fontFamily), getStyle(fontStyle)));
    }
}
