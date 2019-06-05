package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class BitmapPoolAdapter implements BitmapPool {
    public void clearMemory() {
    }

    public void trimMemory(int i) {
    }

    public void put(Bitmap bitmap) {
        bitmap.recycle();
    }

    public Bitmap get(int i, int i2, Config config) {
        return Bitmap.createBitmap(i, i2, config);
    }

    public Bitmap getDirty(int i, int i2, Config config) {
        return get(i, i2, config);
    }
}
