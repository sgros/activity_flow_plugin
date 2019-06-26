// 
// Decompiled by Procyon v0.5.34
// 

package org.mapsforge.graphics.android;

import android.graphics.BitmapFactory;
import java.io.InputStream;
import org.mapsforge.map.graphics.Bitmap;

class AndroidBitmap implements Bitmap
{
    final android.graphics.Bitmap bitmap;
    
    AndroidBitmap(final InputStream inputStream) {
        this.bitmap = BitmapFactory.decodeStream(inputStream);
    }
    
    @Override
    public void destroy() {
        this.bitmap.recycle();
    }
    
    @Override
    public int getHeight() {
        return this.bitmap.getHeight();
    }
    
    @Override
    public int[] getPixels() {
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int[] array = new int[width * height];
        this.bitmap.getPixels(array, 0, width, 0, 0, width, height);
        return array;
    }
    
    @Override
    public int getWidth() {
        return this.bitmap.getWidth();
    }
}
