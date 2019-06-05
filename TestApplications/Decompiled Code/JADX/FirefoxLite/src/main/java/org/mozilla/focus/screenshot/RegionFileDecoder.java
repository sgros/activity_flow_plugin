package org.mozilla.focus.screenshot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import java.io.IOException;
import java.io.InputStream;

public class RegionFileDecoder implements ResourceDecoder<InputStream, Bitmap> {
    private final BitmapPool bitmapPool;
    private final int defaultWidth;

    public boolean handles(InputStream inputStream, Options options) {
        return true;
    }

    public RegionFileDecoder(Glide glide, int i) {
        this(glide.getBitmapPool(), i);
    }

    public RegionFileDecoder(BitmapPool bitmapPool, int i) {
        this.bitmapPool = bitmapPool;
        this.defaultWidth = i;
    }

    public Resource<Bitmap> decode(InputStream inputStream, int i, int i2, Options options) throws IOException {
        int i3 = this.defaultWidth;
        if (inputStream.markSupported()) {
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inJustDecodeBounds = true;
            inputStream.mark(inputStream.available());
            BitmapFactory.decodeStream(inputStream, new Rect(), options2);
            inputStream.reset();
            i3 = options2.outWidth;
        }
        BitmapRegionDecoder createDecoder = createDecoder(inputStream, i, i2);
        BitmapFactory.Options options3 = new BitmapFactory.Options();
        i = (int) Math.ceil(((double) i3) / ((double) i));
        if (i == 0) {
            i = 0;
        } else {
            i = Integer.highestOneBit(i);
        }
        options3.inSampleSize = Math.max(1, i);
        return BitmapResource.obtain(createDecoder.decodeRegion(new Rect(0, 0, i3, i3), options3), this.bitmapPool);
    }

    private BitmapRegionDecoder createDecoder(InputStream inputStream, int i, int i2) throws IOException {
        return BitmapRegionDecoder.newInstance(inputStream, false);
    }
}
