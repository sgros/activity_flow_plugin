// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.screenshot;

import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.BitmapFactory$Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.Options;
import java.io.IOException;
import android.graphics.BitmapRegionDecoder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import android.graphics.Bitmap;
import java.io.InputStream;
import com.bumptech.glide.load.ResourceDecoder;

public class RegionFileDecoder implements ResourceDecoder<InputStream, Bitmap>
{
    private final BitmapPool bitmapPool;
    private final int defaultWidth;
    
    public RegionFileDecoder(final Glide glide, final int n) {
        this(glide.getBitmapPool(), n);
    }
    
    public RegionFileDecoder(final BitmapPool bitmapPool, final int defaultWidth) {
        this.bitmapPool = bitmapPool;
        this.defaultWidth = defaultWidth;
    }
    
    private BitmapRegionDecoder createDecoder(final InputStream inputStream, final int n, final int n2) throws IOException {
        return BitmapRegionDecoder.newInstance(inputStream, false);
    }
    
    @Override
    public Resource<Bitmap> decode(final InputStream inputStream, int highestOneBit, final int n, final Options options) throws IOException {
        int n2 = this.defaultWidth;
        if (inputStream.markSupported()) {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            inputStream.mark(inputStream.available());
            BitmapFactory.decodeStream(inputStream, new Rect(), bitmapFactory$Options);
            inputStream.reset();
            n2 = bitmapFactory$Options.outWidth;
        }
        final BitmapRegionDecoder decoder = this.createDecoder(inputStream, highestOneBit, n);
        final BitmapFactory$Options bitmapFactory$Options2 = new BitmapFactory$Options();
        highestOneBit = (int)Math.ceil(n2 / (double)highestOneBit);
        if (highestOneBit == 0) {
            highestOneBit = 0;
        }
        else {
            highestOneBit = Integer.highestOneBit(highestOneBit);
        }
        bitmapFactory$Options2.inSampleSize = Math.max(1, highestOneBit);
        return BitmapResource.obtain(decoder.decodeRegion(new Rect(0, 0, n2, n2), bitmapFactory$Options2), this.bitmapPool);
    }
    
    @Override
    public boolean handles(final InputStream inputStream, final Options options) {
        return true;
    }
}
