package com.davemorrissey.labs.subscaleview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class ImageSource {
   private final Bitmap bitmap;
   private boolean cached;
   private final Integer resource;
   private int sHeight;
   private Rect sRegion;
   private int sWidth;
   private boolean tile;
   private final Uri uri;

   private ImageSource(int var1) {
      this.bitmap = null;
      this.uri = null;
      this.resource = var1;
      this.tile = true;
   }

   private ImageSource(Uri var1) {
      String var2 = var1.toString();
      Uri var3 = var1;
      if (var2.startsWith("file:///")) {
         var3 = var1;
         if (!(new File(var2.substring("file:///".length() - 1))).exists()) {
            try {
               var3 = Uri.parse(URLDecoder.decode(var2, "UTF-8"));
            } catch (UnsupportedEncodingException var4) {
               var3 = var1;
            }
         }
      }

      this.bitmap = null;
      this.uri = var3;
      this.resource = null;
      this.tile = true;
   }

   public static ImageSource asset(String var0) {
      if (var0 != null) {
         StringBuilder var1 = new StringBuilder();
         var1.append("file:///android_asset/");
         var1.append(var0);
         return uri(var1.toString());
      } else {
         throw new NullPointerException("Asset name must not be null");
      }
   }

   public static ImageSource resource(int var0) {
      return new ImageSource(var0);
   }

   public static ImageSource uri(Uri var0) {
      if (var0 != null) {
         return new ImageSource(var0);
      } else {
         throw new NullPointerException("Uri must not be null");
      }
   }

   public static ImageSource uri(String var0) {
      if (var0 != null) {
         String var1 = var0;
         if (!var0.contains("://")) {
            var1 = var0;
            if (var0.startsWith("/")) {
               var1 = var0.substring(1);
            }

            StringBuilder var2 = new StringBuilder();
            var2.append("file:///");
            var2.append(var1);
            var1 = var2.toString();
         }

         return new ImageSource(Uri.parse(var1));
      } else {
         throw new NullPointerException("Uri must not be null");
      }
   }

   protected final Bitmap getBitmap() {
      return this.bitmap;
   }

   protected final Integer getResource() {
      return this.resource;
   }

   protected final int getSHeight() {
      return this.sHeight;
   }

   protected final Rect getSRegion() {
      return this.sRegion;
   }

   protected final int getSWidth() {
      return this.sWidth;
   }

   protected final boolean getTile() {
      return this.tile;
   }

   protected final Uri getUri() {
      return this.uri;
   }

   protected final boolean isCached() {
      return this.cached;
   }

   public ImageSource tiling(boolean var1) {
      this.tile = var1;
      return this;
   }

   public ImageSource tilingEnabled() {
      return this.tiling(true);
   }
}
