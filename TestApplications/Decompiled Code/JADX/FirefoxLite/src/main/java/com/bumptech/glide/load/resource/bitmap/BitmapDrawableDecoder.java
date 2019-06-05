package com.bumptech.glide.load.resource.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;

public class BitmapDrawableDecoder<DataType> implements ResourceDecoder<DataType, BitmapDrawable> {
    private final BitmapPool bitmapPool;
    private final ResourceDecoder<DataType, Bitmap> decoder;
    private final Resources resources;

    public BitmapDrawableDecoder(Resources resources, BitmapPool bitmapPool, ResourceDecoder<DataType, Bitmap> resourceDecoder) {
        this.resources = (Resources) Preconditions.checkNotNull(resources);
        this.bitmapPool = (BitmapPool) Preconditions.checkNotNull(bitmapPool);
        this.decoder = (ResourceDecoder) Preconditions.checkNotNull(resourceDecoder);
    }

    public boolean handles(DataType dataType, Options options) throws IOException {
        return this.decoder.handles(dataType, options);
    }

    public Resource<BitmapDrawable> decode(DataType dataType, int i, int i2, Options options) throws IOException {
        Resource decode = this.decoder.decode(dataType, i, i2, options);
        if (decode == null) {
            return null;
        }
        return LazyBitmapDrawableResource.obtain(this.resources, this.bitmapPool, (Bitmap) decode.get());
    }
}
