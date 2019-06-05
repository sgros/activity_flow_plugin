// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.model;

import android.database.Cursor;
import java.io.FileNotFoundException;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.load.Options;
import android.content.Context;
import java.io.File;
import android.net.Uri;

public final class MediaStoreFileLoader implements ModelLoader<Uri, File>
{
    private final Context context;
    
    MediaStoreFileLoader(final Context context) {
        this.context = context;
    }
    
    public LoadData<File> buildLoadData(final Uri uri, final int n, final int n2, final Options options) {
        return (LoadData<File>)new LoadData(new ObjectKey(uri), (DataFetcher<Object>)new FilePathFetcher(this.context, uri));
    }
    
    @Override
    public boolean handles(final Uri uri) {
        return MediaStoreUtil.isMediaStoreUri(uri);
    }
    
    public static final class Factory implements ModelLoaderFactory<Uri, File>
    {
        private final Context context;
        
        public Factory(final Context context) {
            this.context = context;
        }
        
        @Override
        public ModelLoader<Uri, File> build(final MultiModelLoaderFactory multiModelLoaderFactory) {
            return new MediaStoreFileLoader(this.context);
        }
    }
    
    private static class FilePathFetcher implements DataFetcher<File>
    {
        private static final String[] PROJECTION;
        private final Context context;
        private final Uri uri;
        
        static {
            PROJECTION = new String[] { "_data" };
        }
        
        FilePathFetcher(final Context context, final Uri uri) {
            this.context = context;
            this.uri = uri;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
        }
        
        @Override
        public Class<File> getDataClass() {
            return File.class;
        }
        
        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
        
        @Override
        public void loadData(final Priority priority, final DataCallback<? super File> dataCallback) {
            final Cursor query = this.context.getContentResolver().query(this.uri, FilePathFetcher.PROJECTION, (String)null, (String[])null, (String)null);
            final String pathname = null;
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        query.getString(query.getColumnIndexOrThrow("_data"));
                    }
                }
                finally {
                    query.close();
                }
            }
            if (TextUtils.isEmpty((CharSequence)pathname)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to find file path for: ");
                sb.append(this.uri);
                dataCallback.onLoadFailed(new FileNotFoundException(sb.toString()));
            }
            else {
                dataCallback.onDataReady(new File(pathname));
            }
        }
    }
}
