package pl.droidsonroids.gif;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

final class GifViewUtils {
   static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
   static final List SUPPORTED_RESOURCE_TYPE_NAMES = Arrays.asList("raw", "drawable", "mipmap");

   private GifViewUtils() {
   }

   static float getDensityScale(@NonNull Resources var0, @DrawableRes @RawRes int var1) {
      TypedValue var2 = new TypedValue();
      var0.getValue(var1, var2, true);
      var1 = var2.density;
      if (var1 == 0) {
         var1 = 160;
      } else if (var1 == 65535) {
         var1 = 0;
      }

      int var3 = var0.getDisplayMetrics().densityDpi;
      float var4;
      if (var1 > 0 && var3 > 0) {
         var4 = (float)var3 / (float)var1;
      } else {
         var4 = 1.0F;
      }

      return var4;
   }

   private static int getResourceId(ImageView var0, AttributeSet var1, boolean var2) {
      String var3;
      if (var2) {
         var3 = "src";
      } else {
         var3 = "background";
      }

      int var4 = var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", var3, 0);
      if (var4 > 0) {
         String var5 = var0.getResources().getResourceTypeName(var4);
         if (SUPPORTED_RESOURCE_TYPE_NAMES.contains(var5) && !setResource(var0, var2, var4)) {
            return var4;
         }
      }

      var4 = 0;
      return var4;
   }

   static GifViewUtils.InitResult initImageView(ImageView var0, AttributeSet var1, int var2, int var3) {
      GifViewUtils.InitResult var4;
      if (var1 != null && !var0.isInEditMode()) {
         var4 = new GifViewUtils.InitResult(getResourceId(var0, var1, true), getResourceId(var0, var1, false), isFreezingAnimation(var0, var1, var2, var3));
      } else {
         var4 = new GifViewUtils.InitResult(0, 0, false);
      }

      return var4;
   }

   static boolean isFreezingAnimation(View var0, AttributeSet var1, int var2, int var3) {
      TypedArray var5 = var0.getContext().obtainStyledAttributes(var1, R.styleable.GifView, var2, var3);
      boolean var4 = var5.getBoolean(R.styleable.GifView_freezesAnimation, false);
      var5.recycle();
      return var4;
   }

   static boolean setGifImageUri(ImageView var0, Uri var1) {
      boolean var3;
      if (var1 != null) {
         label23: {
            try {
               GifDrawable var2 = new GifDrawable(var0.getContext().getContentResolver(), var1);
               var0.setImageDrawable(var2);
            } catch (IOException var4) {
               break label23;
            }

            var3 = true;
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   static boolean setResource(ImageView var0, boolean var1, int var2) {
      Resources var3 = var0.getResources();
      if (var3 != null) {
         label68: {
            boolean var10001;
            GifDrawable var4;
            try {
               var4 = new GifDrawable(var3, var2);
            } catch (IOException var9) {
               var10001 = false;
               break label68;
            } catch (NotFoundException var10) {
               var10001 = false;
               break label68;
            }

            if (var1) {
               try {
                  var0.setImageDrawable(var4);
               } catch (IOException var5) {
                  var10001 = false;
                  break label68;
               } catch (NotFoundException var6) {
                  var10001 = false;
                  break label68;
               }
            } else {
               label61: {
                  try {
                     if (VERSION.SDK_INT >= 16) {
                        var0.setBackground(var4);
                        break label61;
                     }
                  } catch (IOException var11) {
                     var10001 = false;
                     break label68;
                  } catch (NotFoundException var12) {
                     var10001 = false;
                     break label68;
                  }

                  try {
                     var0.setBackgroundDrawable(var4);
                  } catch (IOException var7) {
                     var10001 = false;
                     break label68;
                  } catch (NotFoundException var8) {
                     var10001 = false;
                     break label68;
                  }
               }
            }

            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   static class InitResult {
      final int mBackgroundResId;
      final boolean mFreezesAnimation;
      final int mSourceResId;

      InitResult(int var1, int var2, boolean var3) {
         this.mSourceResId = var1;
         this.mBackgroundResId = var2;
         this.mFreezesAnimation = var3;
      }
   }
}
