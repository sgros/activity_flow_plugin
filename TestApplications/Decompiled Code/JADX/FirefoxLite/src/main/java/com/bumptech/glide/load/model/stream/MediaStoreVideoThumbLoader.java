package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.signature.ObjectKey;
import java.io.InputStream;

public class MediaStoreVideoThumbLoader implements ModelLoader<Uri, InputStream> {
    private final Context context;

    public static class Factory implements ModelLoaderFactory<Uri, InputStream> {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreVideoThumbLoader(this.context);
        }
    }

    MediaStoreVideoThumbLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    public LoadData<InputStream> buildLoadData(Uri uri, int i, int i2, Options options) {
        return (MediaStoreUtil.isThumbnailSize(i, i2) && isRequestingDefaultFrame(options)) ? new LoadData(new ObjectKey(uri), ThumbFetcher.buildVideoFetcher(this.context, uri)) : null;
    }

    private boolean isRequestingDefaultFrame(Options options) {
        Long l = (Long) options.get(VideoBitmapDecoder.TARGET_FRAME);
        return l != null && l.longValue() == -1;
    }

    public boolean handles(Uri uri) {
        return MediaStoreUtil.isMediaStoreVideoUri(uri);
    }
}
