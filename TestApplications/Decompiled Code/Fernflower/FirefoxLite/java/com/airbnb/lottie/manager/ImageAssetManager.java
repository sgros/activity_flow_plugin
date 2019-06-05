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
import android.util.Log;
import android.view.View;
import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieImageAsset;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ImageAssetManager {
   private static final Object bitmapHashLock = new Object();
   private final Context context;
   private ImageAssetDelegate delegate;
   private final Map imageAssets;
   private String imagesFolder;

   public ImageAssetManager(Callback var1, String var2, ImageAssetDelegate var3, Map var4) {
      this.imagesFolder = var2;
      if (!TextUtils.isEmpty(var2) && this.imagesFolder.charAt(this.imagesFolder.length() - 1) != '/') {
         StringBuilder var5 = new StringBuilder();
         var5.append(this.imagesFolder);
         var5.append('/');
         this.imagesFolder = var5.toString();
      }

      if (!(var1 instanceof View)) {
         Log.w("LOTTIE", "LottieDrawable must be inside of a view for images to work.");
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
         } else if (this.delegate != null) {
            var3 = this.delegate.fetchBitmap(var2);
            if (var3 != null) {
               this.putBitmap(var1, var3);
            }

            return var3;
         } else {
            String var9 = var2.getFileName();
            Options var12 = new Options();
            var12.inScaled = true;
            var12.inDensity = 160;
            if (var9.startsWith("data:") && var9.indexOf("base64,") > 0) {
               byte[] var11;
               try {
                  var11 = Base64.decode(var9.substring(var9.indexOf(44) + 1), 0);
               } catch (IllegalArgumentException var6) {
                  Log.w("LOTTIE", "data URL did not have correct base64 format.", var6);
                  return null;
               }

               return this.putBitmap(var1, BitmapFactory.decodeByteArray(var11, 0, var11.length, var12));
            } else {
               InputStream var10;
               try {
                  if (TextUtils.isEmpty(this.imagesFolder)) {
                     IllegalStateException var8 = new IllegalStateException("You must set an images folder before loading an image. Set it with LottieComposition#setImagesFolder or LottieDrawable#setImagesFolder");
                     throw var8;
                  }

                  AssetManager var4 = this.context.getAssets();
                  StringBuilder var5 = new StringBuilder();
                  var5.append(this.imagesFolder);
                  var5.append(var9);
                  var10 = var4.open(var5.toString());
               } catch (IOException var7) {
                  Log.w("LOTTIE", "Unable to open asset.", var7);
                  return null;
               }

               return this.putBitmap(var1, BitmapFactory.decodeStream(var10, (Rect)null, var12));
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

   public void recycleBitmaps() {
      Object var1 = bitmapHashLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label277: {
         Iterator var2;
         try {
            var2 = this.imageAssets.entrySet().iterator();
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label277;
         }

         while(true) {
            LottieImageAsset var3;
            Bitmap var4;
            try {
               if (!var2.hasNext()) {
                  break;
               }

               var3 = (LottieImageAsset)((Entry)var2.next()).getValue();
               var4 = var3.getBitmap();
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label277;
            }

            if (var4 != null) {
               try {
                  var4.recycle();
                  var3.setBitmap((Bitmap)null);
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label277;
               }
            }
         }

         label259:
         try {
            return;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label259;
         }
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            continue;
         }
      }
   }

   public void setDelegate(ImageAssetDelegate var1) {
      this.delegate = var1;
   }
}
