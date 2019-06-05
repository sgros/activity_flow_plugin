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
import com.bumptech.glide.load.Options;
import android.content.Context;
import java.io.InputStream;
import android.net.Uri;
import com.bumptech.glide.load.model.ModelLoader;

public class MediaStoreImageThumbLoader implements ModelLoader<Uri, InputStream>
{
    public final Context context;
    
    public MediaStoreImageThumbLoader(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    public LoadData<InputStream> buildLoadData(final Uri uri, final int n, final int n2, final Options options) {
        if (MediaStoreUtil.isThumbnailSize(n, n2)) {
            return (LoadData<InputStream>)new LoadData(new ObjectKey(uri), (DataFetcher<Object>)ThumbFetcher.buildImageFetcher(this.context, uri));
        }
        return null;
    }
    
    @Override
    public boolean handles(final Uri uri) {
        return MediaStoreUtil.isMediaStoreImageUri(uri);
    }
    
    public static class Factory implements ModelLoaderFactory<Uri, InputStream>
    {
        private final Context context;
        
        public Factory(final Context context) {
            this.context = context;
        }
        
        @Override
        public ModelLoader<Uri, InputStream> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreImageThumbLoader(this.context);
        }
    }
}
