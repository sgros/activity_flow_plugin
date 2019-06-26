package com.airbnb.lottie.manager;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable.Callback;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageAssetManager {
   private static final Object bitmapHashLock = new Object();
   private final Context context;
   private ImageAssetDelegate delegate;
   private final Map imageAssets;
   private String imagesFolder;

   public ImageAssetManager(Callback var1, String var2, ImageAssetDelegate var3, Map var4) {
      this.imagesFolder = var2;
      if (!TextUtils.isEmpty(var2)) {
         var2 = this.imagesFolder;
         if (var2.charAt(var2.length() - 1) != '/') {
            StringBuilder var5 = new StringBuilder();
            var5.append(this.imagesFolder);
            var5.append('/');
            this.imagesFolder = var5.toString();
         }
      }

      if (!(var1 instanceof View)) {
         Logger.warning("LottieDrawable must be inside of a view for images to work.");
         this.imageAssets = new HashMap();
         this.context = null;
      } else {
         this.context = ((View)var1).getContext();
         this.imageAssets = var4;
         this.setDelegate(var3);
      }
   }

   private Bitmap putBitmap(String param1, Bitmap param2) {
      // $FF: Couldn't be decompiled
   }

   public Bitmap bitmapForId(String var1) {
      LottieImageAsset var2 = (LottieImageAsset)this.imageAssets.get(var1);
      if (var2 == null) {
         return null;
      } else {
         Bitmap var3 = var2.getBitmap();
         if (var3 != null) {
            return var3;
         } else {
            ImageAssetDelegate var12 = this.delegate;
            Bitmap var10;
            if (var12 != null) {
               var10 = var12.fetchBitmap(var2);
               if (var10 != null) {
                  this.putBitmap(var1, var10);
               }

               return var10;
            } else {
               String var4 = var2.getFileName();
               Options var13 = new Options();
               var13.inScaled = true;
               var13.inDensity = 160;
               if (var4.startsWith("data:") && var4.indexOf("base64,") > 0) {
                  byte[] var11;
                  try {
                     var11 = Base64.decode(var4.substring(var4.indexOf(44) + 1), 0);
                  } catch (IllegalArgumentException var7) {
                     Logger.warning("data URL did not have correct base64 format.", var7);
                     return null;
                  }

                  var10 = BitmapFactory.decodeByteArray(var11, 0, var11.length, var13);
                  this.putBitmap(var1, var10);
                  return var10;
               } else {
                  InputStream var14;
                  try {
                     if (TextUtils.isEmpty(this.imagesFolder)) {
                        IllegalStateException var9 = new IllegalStateException("You must set an images folder before loading an image. Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
                        throw var9;
                     }

                     AssetManager var5 = this.context.getAssets();
                     StringBuilder var6 = new StringBuilder();
                     var6.append(this.imagesFolder);
                     var6.append(var4);
                     var14 = var5.open(var6.toString());
                  } catch (IOException var8) {
                     Logger.warning("Unable to open asset.", var8);
                     return null;
                  }

                  var10 = Utils.resizeBitmapIfNeeded(BitmapFactory.decodeStream(var14, (Rect)null, var13), var2.getWidth(), var2.getHeight());
                  this.putBitmap(var1, var10);
                  return var10;
               }
            }
         }
      }
   }

   public boolean hasSameContext(Context var1) {
      boolean var2;
      if ((var1 != null || this.context != null) && !this.context.equals(var1)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void setDelegate(ImageAssetDelegate var1) {
      this.delegate = var1;
   }
}
