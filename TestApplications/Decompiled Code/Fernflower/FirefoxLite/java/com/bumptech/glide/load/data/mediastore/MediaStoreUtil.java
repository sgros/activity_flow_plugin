package com.bumptech.glide.load.data.mediastore;

import android.net.Uri;

public final class MediaStoreUtil {
   public static boolean isMediaStoreImageUri(Uri var0) {
      boolean var1;
      if (isMediaStoreUri(var0) && !isVideoUri(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isMediaStoreUri(Uri var0) {
      boolean var1;
      if (var0 != null && "content".equals(var0.getScheme()) && "media".equals(var0.getAuthority())) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isMediaStoreVideoUri(Uri var0) {
      boolean var1;
      if (isMediaStoreUri(var0) && isVideoUri(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isThumbnailSize(int var0, int var1) {
      boolean var2;
      if (var0 <= 512 && var1 <= 384) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean isVideoUri(Uri var0) {
      return var0.getPathSegments().contains("video");
   }
}
