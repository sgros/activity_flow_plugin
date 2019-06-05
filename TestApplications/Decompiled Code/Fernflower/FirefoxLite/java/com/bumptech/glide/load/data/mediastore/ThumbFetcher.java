package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.ExifOrientationStream;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ThumbFetcher implements DataFetcher {
   private InputStream inputStream;
   private final Uri mediaStoreImageUri;
   private final ThumbnailStreamOpener opener;

   ThumbFetcher(Uri var1, ThumbnailStreamOpener var2) {
      this.mediaStoreImageUri = var1;
      this.opener = var2;
   }

   private static ThumbFetcher build(Context var0, Uri var1, ThumbnailQuery var2) {
      ArrayPool var3 = Glide.get(var0).getArrayPool();
      return new ThumbFetcher(var1, new ThumbnailStreamOpener(Glide.get(var0).getRegistry().getImageHeaderParsers(), var2, var3, var0.getContentResolver()));
   }

   public static ThumbFetcher buildImageFetcher(Context var0, Uri var1) {
      return build(var0, var1, new ThumbFetcher.ImageThumbnailQuery(var0.getContentResolver()));
   }

   public static ThumbFetcher buildVideoFetcher(Context var0, Uri var1) {
      return build(var0, var1, new ThumbFetcher.VideoThumbnailQuery(var0.getContentResolver()));
   }

   private InputStream openThumbInputStream() throws FileNotFoundException {
      InputStream var1 = this.opener.open(this.mediaStoreImageUri);
      int var2;
      if (var1 != null) {
         var2 = this.opener.getOrientation(this.mediaStoreImageUri);
      } else {
         var2 = -1;
      }

      Object var3 = var1;
      if (var2 != -1) {
         var3 = new ExifOrientationStream(var1, var2);
      }

      return (InputStream)var3;
   }

   public void cancel() {
   }

   public void cleanup() {
      if (this.inputStream != null) {
         try {
            this.inputStream.close();
         } catch (IOException var2) {
         }
      }

   }

   public Class getDataClass() {
      return InputStream.class;
   }

   public DataSource getDataSource() {
      return DataSource.LOCAL;
   }

   public void loadData(Priority var1, DataFetcher.DataCallback var2) {
      try {
         this.inputStream = this.openThumbInputStream();
      } catch (FileNotFoundException var3) {
         if (Log.isLoggable("MediaStoreThumbFetcher", 3)) {
            Log.d("MediaStoreThumbFetcher", "Failed to find thumbnail file", var3);
         }

         var2.onLoadFailed(var3);
         return;
      }

      var2.onDataReady(this.inputStream);
   }

   static class ImageThumbnailQuery implements ThumbnailQuery {
      private static final String[] PATH_PROJECTION = new String[]{"_data"};
      private final ContentResolver contentResolver;

      ImageThumbnailQuery(ContentResolver var1) {
         this.contentResolver = var1;
      }

      public Cursor query(Uri var1) {
         String var2 = var1.getLastPathSegment();
         return this.contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, "kind = 1 AND image_id = ?", new String[]{var2}, (String)null);
      }
   }

   static class VideoThumbnailQuery implements ThumbnailQuery {
      private static final String[] PATH_PROJECTION = new String[]{"_data"};
      private final ContentResolver contentResolver;

      VideoThumbnailQuery(ContentResolver var1) {
         this.contentResolver = var1;
      }

      public Cursor query(Uri var1) {
         String var2 = var1.getLastPathSegment();
         return this.contentResolver.query(android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, "kind = 1 AND video_id = ?", new String[]{var2}, (String)null);
      }
   }
}
