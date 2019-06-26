package org.mapsforge.graphics.android;

import android.graphics.BitmapFactory;
import java.io.InputStream;
import org.mapsforge.map.graphics.Bitmap;

class AndroidBitmap implements Bitmap {
    final android.graphics.Bitmap bitmap;

    AndroidBitmap(InputStream inputStream) {
        this.bitmap = BitmapFactory.decodeStream(inputStream);
    }

    public void destroy() {
        this.bitmap.recycle();
    }

    public int getHeight() {
        return this.bitmap.getHeight();
    }

    public int[] getPixels() {
        int width = getWidth();
        int height = getHeight();
        int[] colors = new int[(width * height)];
        this.bitmap.getPixels(colors, 0, width, 0, 0, width, height);
        return colors;
    }

    public int getWidth() {
        return this.bitmap.getWidth();
    }
}
