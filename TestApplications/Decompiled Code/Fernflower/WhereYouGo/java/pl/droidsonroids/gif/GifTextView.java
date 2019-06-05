package pl.droidsonroids.gif;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;
import java.io.IOException;

public class GifTextView extends TextView {
   private boolean mFreezesAnimation;

   public GifTextView(Context var1) {
      super(var1);
   }

   public GifTextView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init(var2, 0, 0);
   }

   public GifTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init(var2, var3, 0);
   }

   @RequiresApi(21)
   public GifTextView(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.init(var2, var3, var4);
   }

   private Drawable getGifOrDefaultDrawable(int var1) {
      Object var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         Resources var3 = this.getResources();
         if (!this.isInEditMode() && "drawable".equals(var3.getResourceTypeName(var1))) {
            try {
               var2 = new GifDrawable(var3, var1);
               return (Drawable)var2;
            } catch (IOException var4) {
            } catch (NotFoundException var5) {
            }
         }

         if (VERSION.SDK_INT >= 21) {
            var2 = var3.getDrawable(var1, this.getContext().getTheme());
         } else {
            var2 = var3.getDrawable(var1);
         }
      }

      return (Drawable)var2;
   }

   private void hideCompoundDrawables(Drawable[] var1) {
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Drawable var4 = var1[var3];
         if (var4 != null) {
            var4.setVisible(false, false);
         }
      }

   }

   private void init(AttributeSet var1, int var2, int var3) {
      if (var1 != null) {
         Drawable var4 = this.getGifOrDefaultDrawable(var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableLeft", 0));
         Drawable var5 = this.getGifOrDefaultDrawable(var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableTop", 0));
         Drawable var6 = this.getGifOrDefaultDrawable(var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableRight", 0));
         Drawable var7 = this.getGifOrDefaultDrawable(var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableBottom", 0));
         Drawable var8 = this.getGifOrDefaultDrawable(var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableStart", 0));
         Drawable var9 = this.getGifOrDefaultDrawable(var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "drawableEnd", 0));
         if (VERSION.SDK_INT >= 17) {
            Drawable var10;
            Drawable var11;
            if (this.getLayoutDirection() == 0) {
               var10 = var8;
               if (var8 == null) {
                  var10 = var4;
               }

               var8 = var9;
               var11 = var10;
               if (var9 == null) {
                  var8 = var6;
                  var11 = var10;
               }
            } else {
               var10 = var8;
               if (var8 == null) {
                  var10 = var6;
               }

               var8 = var9;
               var11 = var10;
               if (var9 == null) {
                  var8 = var4;
                  var11 = var10;
               }
            }

            this.setCompoundDrawablesRelativeWithIntrinsicBounds(var11, var5, var8, var7);
            this.setCompoundDrawablesWithIntrinsicBounds(var4, var5, var6, var7);
         } else {
            this.setCompoundDrawablesWithIntrinsicBounds(var4, var5, var6, var7);
         }

         this.setBackgroundInternal(this.getGifOrDefaultDrawable(var1.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", 0)));
      }

      this.mFreezesAnimation = GifViewUtils.isFreezingAnimation(this, var1, var2, var3);
   }

   private void setBackgroundInternal(Drawable var1) {
      if (VERSION.SDK_INT >= 16) {
         this.setBackground(var1);
      } else {
         this.setBackgroundDrawable(var1);
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.hideCompoundDrawables(this.getCompoundDrawables());
      if (VERSION.SDK_INT >= 17) {
         this.hideCompoundDrawables(this.getCompoundDrawablesRelative());
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof GifViewSavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         GifViewSavedState var3 = (GifViewSavedState)var1;
         super.onRestoreInstanceState(var3.getSuperState());
         Drawable[] var2 = this.getCompoundDrawables();
         var3.restoreState(var2[0], 0);
         var3.restoreState(var2[1], 1);
         var3.restoreState(var2[2], 2);
         var3.restoreState(var2[3], 3);
         if (VERSION.SDK_INT >= 17) {
            var2 = this.getCompoundDrawablesRelative();
            var3.restoreState(var2[0], 4);
            var3.restoreState(var2[2], 5);
         }

         var3.restoreState(this.getBackground(), 6);
      }

   }

   public Parcelable onSaveInstanceState() {
      Drawable[] var1 = new Drawable[7];
      if (this.mFreezesAnimation) {
         Drawable[] var2 = this.getCompoundDrawables();
         System.arraycopy(var2, 0, var1, 0, var2.length);
         if (VERSION.SDK_INT >= 17) {
            var2 = this.getCompoundDrawablesRelative();
            var1[4] = var2[0];
            var1[5] = var2[2];
         }

         var1[6] = this.getBackground();
      }

      return new GifViewSavedState(super.onSaveInstanceState(), var1);
   }

   public void setBackgroundResource(int var1) {
      this.setBackgroundInternal(this.getGifOrDefaultDrawable(var1));
   }

   @RequiresApi(17)
   public void setCompoundDrawablesRelativeWithIntrinsicBounds(int var1, int var2, int var3, int var4) {
      this.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getGifOrDefaultDrawable(var1), this.getGifOrDefaultDrawable(var2), this.getGifOrDefaultDrawable(var3), this.getGifOrDefaultDrawable(var4));
   }

   public void setCompoundDrawablesWithIntrinsicBounds(int var1, int var2, int var3, int var4) {
      this.setCompoundDrawablesWithIntrinsicBounds(this.getGifOrDefaultDrawable(var1), this.getGifOrDefaultDrawable(var2), this.getGifOrDefaultDrawable(var3), this.getGifOrDefaultDrawable(var4));
   }

   public void setFreezesAnimation(boolean var1) {
      this.mFreezesAnimation = var1;
   }
}
