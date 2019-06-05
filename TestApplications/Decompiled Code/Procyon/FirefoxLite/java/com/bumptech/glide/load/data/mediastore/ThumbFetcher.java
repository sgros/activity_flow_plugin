// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.data.mediastore;

import android.provider.MediaStore$Video$Thumbnails;
import android.provider.MediaStore$Images$Thumbnails;
import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.bumptech.glide.load.data.ExifOrientationStream;
import com.bumptech.glide.Glide;
import android.content.Context;
import android.net.Uri;
import java.io.InputStream;
import com.bumptech.glide.load.data.DataFetcher;

public class ThumbFetcher implements DataFetcher<InputStream>
{
    private InputStream inputStream;
    private final Uri mediaStoreImageUri;
    private final ThumbnailStreamOpener opener;
    
    ThumbFetcher(final Uri mediaStoreImageUri, final ThumbnailStreamOpener opener) {
        this.mediaStoreImageUri = mediaStoreImageUri;
        this.opener = opener;
    }
    
    private static ThumbFetcher build(final Context context, final Uri uri, final ThumbnailQuery thumbnailQuery) {
        return new ThumbFetcher(uri, new ThumbnailStreamOpener(Glide.get(context).getRegistry().getImageHeaderParsers(), thumbnailQuery, Glide.get(context).getArrayPool(), context.getContentResolver()));
    }
    
    public static ThumbFetcher buildImageFetcher(final Context context, final Uri uri) {
        return build(context, uri, new ImageThumbnailQuery(context.getContentResolver()));
    }
    
    public static ThumbFetcher buildVideoFetcher(final Context context, final Uri uri) {
        return build(context, uri, new VideoThumbnailQuery(context.getContentResolver()));
    }
    
    private InputStream openThumbInputStream() throws FileNotFoundException {
        final InputStream open = this.opener.open(this.mediaStoreImageUri);
        int orientation;
        if (open != null) {
            orientation = this.opener.getOrientation(this.mediaStoreImageUri);
        }
        else {
            orientation = -1;
        }
        InputStream inputStream = open;
        if (orientation != -1) {
            inputStream = new ExifOrientationStream(open, orientation);
        }
        return inputStream;
    }
    
    @Override
    public void cancel() {
    }
    
    @Override
    public void cleanup() {
        if (this.inputStream == null) {
            return;
        }
        try {
            this.inputStream.close();
        }
        catch (IOException ex) {}
    }
    
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
    
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
    
    @Override
    public void loadData(final Priority priority, final DataCallback<? super InputStream> dataCallback) {
        try {
            dataCallback.onDataReady(this.inputStream = this.openThumbInputStream());
        }
        catch (FileNotFoundException ex) {
            if (Log.isLoggable("MediaStoreThumbFetcher", 3)) {
                Log.d("MediaStoreThumbFetcher", "Failed to find thumbnail file", (Throwable)ex);
            }
            dataCallback.onLoadFailed(ex);
        }
    }
    
    static class ImageThumbnailQuery implements ThumbnailQuery
    {
        private static final String[] PATH_PROJECTION;
        private final ContentResolver contentResolver;
        
        static {
            PATH_PROJECTION = new String[] { "_data" };
        }
        
        ImageThumbnailQuery(final ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }
        
        @Override
        public Cursor query(final Uri uri) {
            return this.contentResolver.query(MediaStore$Images$Thumbnails.EXTERNAL_CONTENT_URI, ImageThumbnailQuery.PATH_PROJECTION, "kind = 1 AND image_id = ?", new String[] { uri.getLastPathSegment() }, (String)null);
        }
    }
    
    static class VideoThumbnailQuery implements ThumbnailQuery
    {
        private static final String[] PATH_PROJECTION;
        private final ContentResolver contentResolver;
        
        static {
            PATH_PROJECTION = new String[] { "_data" };
        }
        
        VideoThumbnailQuery(final ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }
        
        @Override
        public Cursor query(final Uri uri) {
            return this.contentResolver.query(MediaStore$Video$Thumbnails.EXTERNAL_CONTENT_URI, VideoThumbnailQuery.PATH_PROJECTION, "kind = 1 AND video_id = ?", new String[] { uri.getLastPathSegment() }, (String)null);
        }
    }
}
