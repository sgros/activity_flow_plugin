package org.mapsforge.graphics.android;

import android.graphics.Bitmap;
import android.graphics.Paint;
import java.io.InputStream;
import org.mapsforge.map.rendertheme.GraphicAdapter;
import org.mapsforge.map.rendertheme.GraphicAdapter.Color;

public final class AndroidGraphics implements GraphicAdapter {
    public static final AndroidGraphics INSTANCE = new AndroidGraphics();

    public static Bitmap getAndroidBitmap(org.mapsforge.map.graphics.Bitmap bitmap) {
        return ((AndroidBitmap) bitmap).bitmap;
    }

    public static Paint getAndroidPaint(org.mapsforge.map.graphics.Paint paint) {
        return ((AndroidPaint) paint).paint;
    }

    private AndroidGraphics() {
    }

    public org.mapsforge.map.graphics.Bitmap decodeStream(InputStream inputStream) {
        return new AndroidBitmap(inputStream);
    }

    public int getColor(Color color) {
        switch (color) {
            case BLACK:
                return -16777216;
            case CYAN:
                return -16711681;
            case TRANSPARENT:
                return 0;
            case WHITE:
                return -1;
            default:
                throw new IllegalArgumentException("unknown color value: " + color);
        }
    }

    public org.mapsforge.map.graphics.Paint getPaint() {
        return new AndroidPaint();
    }

    public int parseColor(String colorString) {
        return android.graphics.Color.parseColor(colorString);
    }
}
