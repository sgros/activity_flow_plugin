package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.signature.ObjectKey;

public class MediaStoreVideoThumbLoader implements ModelLoader {
   private final Context context;

   MediaStoreVideoThumbLoader(Context var1) {
      this.context = var1.getApplicationContext();
   }

   private boolean isRequestingDefaultFrame(Options var1) {
      Long var3 = (Long)var1.get(VideoBitmapDecoder.TARGET_FRAME);
      boolean var2;
      if (var3 != null && var3 == -1L) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      return MediaStoreUtil.isThumbnailSize(var2, var3) && this.isRequestingDefaultFrame(var4) ? new ModelLoader.LoadData(new ObjectKey(var1), ThumbFetcher.buildVideoFetcher(this.context, var1)) : null;
   }

   public boolean handles(Uri var1) {
      return MediaStoreUtil.isMediaStoreVideoUri(var1);
   }

   public static class Factory implements ModelLoaderFactory {
      private final Context context;

      public Factory(Context var1) {
         this.context = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new MediaStoreVideoThumbLoader(this.context);
      }
   }
}
