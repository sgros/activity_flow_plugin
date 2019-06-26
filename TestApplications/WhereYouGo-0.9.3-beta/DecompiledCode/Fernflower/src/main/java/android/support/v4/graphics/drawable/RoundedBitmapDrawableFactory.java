package android.support.v4.graphics.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import java.io.InputStream;

public final class RoundedBitmapDrawableFactory {
   private static final String TAG = "RoundedBitmapDrawableFactory";

   private RoundedBitmapDrawableFactory() {
   }

   public static RoundedBitmapDrawable create(Resources var0, Bitmap var1) {
      Object var2;
      if (VERSION.SDK_INT >= 21) {
         var2 = new RoundedBitmapDrawable21(var0, var1);
      } else {
         var2 = new RoundedBitmapDrawableFactory.DefaultRoundedBitmapDrawable(var0, var1);
      }

      return (RoundedBitmapDrawable)var2;
   }

   public static RoundedBitmapDrawable create(Resources var0, InputStream var1) {
      RoundedBitmapDrawable var2 = create(var0, BitmapFactory.decodeStream(var1));
      if (var2.getBitmap() == null) {
         Log.w("RoundedBitmapDrawableFactory", "RoundedBitmapDrawable cannot decode " + var1);
      }

      return var2;
   }

   public static RoundedBitmapDrawable create(Resources var0, String var1) {
      RoundedBitmapDrawable var2 = create(var0, BitmapFactory.decodeFile(var1));
      if (var2.getBitmap() == null) {
         Log.w("RoundedBitmapDrawableFactory", "RoundedBitmapDrawable cannot decode " + var1);
      }

      return var2;
   }

   private static class DefaultRoundedBitmapDrawable extends RoundedBitmapDrawable {
      DefaultRoundedBitmapDrawable(Resources var1, Bitmap var2) {
         super(var1, var2);
      }

      void gravityCompatApply(int var1, int var2, int var3, Rect var4, Rect var5) {
         GravityCompat.apply(var1, var2, var3, var4, var5, 0);
      }

      public boolean hasMipMap() {
         boolean var1;
         if (this.mBitmap != null && BitmapCompat.hasMipMap(this.mBitmap)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void setMipMap(boolean var1) {
         if (this.mBitmap != null) {
            BitmapCompat.setHasMipMap(this.mBitmap, var1);
            this.invalidateSelf();
         }

      }
   }
}
