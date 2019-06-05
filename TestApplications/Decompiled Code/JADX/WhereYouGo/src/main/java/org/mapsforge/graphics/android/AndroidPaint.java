package org.mapsforge.graphics.android;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.DashPathEffect;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import org.mapsforge.map.graphics.Align;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.graphics.Cap;
import org.mapsforge.map.graphics.FontFamily;
import org.mapsforge.map.graphics.FontStyle;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Style;

class AndroidPaint implements Paint {
    private Bitmap bitmap;
    final android.graphics.Paint paint = new android.graphics.Paint(1);

    private static int getStyle(FontStyle fontStyle) {
        switch (fontStyle) {
            case BOLD:
                return 1;
            case BOLD_ITALIC:
                return 3;
            case ITALIC:
                return 2;
            case NORMAL:
                return 0;
            default:
                throw new IllegalArgumentException("unknown font style: " + fontStyle);
        }
    }

    private static Typeface getTypeface(FontFamily fontFamily) {
        switch (fontFamily) {
            case DEFAULT:
                return Typeface.DEFAULT;
            case DEFAULT_BOLD:
                return Typeface.DEFAULT_BOLD;
            case MONOSPACE:
                return Typeface.MONOSPACE;
            case SANS_SERIF:
                return Typeface.SANS_SERIF;
            case SERIF:
                return Typeface.SERIF;
            default:
                throw new IllegalArgumentException("unknown font family: " + fontFamily);
        }
    }

    AndroidPaint() {
    }

    public int getColor() {
        return this.paint.getColor();
    }

    public int getTextHeight(String text) {
        Rect rect = new Rect();
        this.paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public int getTextWidth(String text) {
        Rect rect = new Rect();
        this.paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    public void setBitmapShader(Bitmap bitmap) {
        if (bitmap != null) {
            this.bitmap = bitmap;
            this.paint.setShader(new BitmapShader(android.graphics.Bitmap.createBitmap(bitmap.getPixels(), bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888), TileMode.REPEAT, TileMode.REPEAT));
        }
    }

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public void setDashPathEffect(float[] strokeDasharray) {
        this.paint.setPathEffect(new DashPathEffect(strokeDasharray, 0.0f));
    }

    public void setStrokeCap(Cap cap) {
        this.paint.setStrokeCap(android.graphics.Paint.Cap.valueOf(cap.name()));
    }

    public void setStrokeWidth(float width) {
        this.paint.setStrokeWidth(width);
    }

    public void setStyle(Style style) {
        this.paint.setStyle(android.graphics.Paint.Style.valueOf(style.name()));
    }

    public void setTextAlign(Align align) {
        this.paint.setTextAlign(android.graphics.Paint.Align.valueOf(align.name()));
    }

    public void setTextSize(float textSize) {
        this.paint.setTextSize(textSize);
    }

    public void setTypeface(FontFamily fontFamily, FontStyle fontStyle) {
        this.paint.setTypeface(Typeface.create(getTypeface(fontFamily), getStyle(fontStyle)));
    }

    public void destroy() {
        if (this.bitmap != null) {
            this.paint.setShader(null);
            this.bitmap.destroy();
        }
    }
}
