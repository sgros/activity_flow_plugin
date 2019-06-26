// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.graphics.android;

import android.graphics.Color;
import java.io.InputStream;
import org.mapsforge.map.graphics.Paint;
import org.mapsforge.map.graphics.Bitmap;
import org.mapsforge.map.rendertheme.GraphicAdapter;

public final class AndroidGraphics implements GraphicAdapter
{
    public static final AndroidGraphics INSTANCE;
    
    static {
        INSTANCE = new AndroidGraphics();
    }
    
    private AndroidGraphics() {
    }
    
    public static android.graphics.Bitmap getAndroidBitmap(final Bitmap bitmap) {
        return ((AndroidBitmap)bitmap).bitmap;
    }
    
    public static android.graphics.Paint getAndroidPaint(final Paint paint) {
        return ((AndroidPaint)paint).paint;
    }
    
    @Override
    public Bitmap decodeStream(final InputStream inputStream) {
        return new AndroidBitmap(inputStream);
    }
    
    @Override
    public int getColor(final Color obj) {
        int n = 0;
        switch (obj) {
            default: {
                throw new IllegalArgumentException("unknown color value: " + obj);
            }
            case BLACK: {
                n = -16777216;
                break;
            }
            case CYAN: {
                n = -16711681;
                break;
            }
            case TRANSPARENT: {
                n = 0;
                break;
            }
            case WHITE: {
                n = -1;
                break;
            }
        }
        return n;
    }
    
    @Override
    public Paint getPaint() {
        return new AndroidPaint();
    }
    
    @Override
    public int parseColor(final String s) {
        return android.graphics.Color.parseColor(s);
    }
}
