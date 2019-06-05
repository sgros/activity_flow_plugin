// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.load.Options;
import android.content.Context;
import java.io.InputStream;
import android.net.Uri;
import com.bumptech.glide.load.model.ModelLoader;

public class MediaStoreVideoThumbLoader implements ModelLoader<Uri, InputStream>
{
    private final Context context;
    
    MediaStoreVideoThumbLoader(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    private boolean isRequestingDefaultFrame(final Options options) {
        final Long n = options.get(VideoBitmapDecoder.TARGET_FRAME);
        return n != null && n == -1L;
    }
    
    public LoadData<InputStream> buildLoadData(final Uri uri, final int n, final int n2, final Options options) {
        if (MediaStoreUtil.isThumbnailSize(n, n2) && this.isRequestingDefaultFrame(options)) {
            return (LoadData<InputStream>)new LoadData(new ObjectKey(uri), (DataFetcher<Object>)ThumbFetcher.buildVideoFetcher(this.context, uri));
        }
        return null;
    }
    
    @Override
    public boolean handles(final Uri uri) {
        return MediaStoreUtil.isMediaStoreVideoUri(uri);
    }
    
    public static class Factory implements ModelLoaderFactory<Uri, InputStream>
    {
        private final Context context;
        
        public Factory(final Context context) {
            this.context = context;
        }
        
        @Override
        public ModelLoader<Uri, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreVideoThumbLoader(this.context);
        }
    }
}
