package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.ImageView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AppCompatImageHelper {
   private TintInfo mImageTint;
   private TintInfo mInternalImageTint;
   private TintInfo mTmpInfo;
   private final ImageView mView;

   public AppCompatImageHelper(ImageView var1) {
      this.mView = var1;
   }

   private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable var1) {
      if (this.mTmpInfo == null) {
         this.mTmpInfo = new TintInfo();
      }

      TintInfo var2 = this.mTmpInfo;
      var2.clear();
      ColorStateList var3 = ImageViewCompat.getImageTintList(this.mView);
      if (var3 != null) {
         var2.mHasTintList = true;
         var2.mTintList = var3;
      }

      Mode var4 = ImageViewCompat.getImageTintMode(this.mView);
      if (var4 != null) {
         var2.mHasTintMode = true;
         var2.mTintMode = var4;
      }

      if (!var2.mHasTintList && !var2.mHasTintMode) {
         return false;
      } else {
         AppCompatDrawableManager.tintDrawable(var1, var2, this.mView.getDrawableState());
         return true;
      }
   }

   private boolean shouldApplyFrameworkTintUsingColorFilter() {
      int var1 = VERSION.SDK_INT;
      boolean var2 = false;
      if (var1 > 21) {
         if (this.mInternalImageTint != null) {
            var2 = true;
         }

         return var2;
      } else {
         return var1 == 21;
      }
   }

   void applySupportImageTint() {
      Drawable var1 = this.mView.getDrawable();
      if (var1 != null) {
         DrawableUtils.fixDrawable(var1);
      }

      if (var1 != null) {
         if (this.shouldApplyFrameworkTintUsingColorFilter() && this.applyFrameworkTintUsingColorFilter(var1)) {
            return;
         }

         if (this.mImageTint != null) {
            AppCompatDrawableManager.tintDrawable(var1, this.mImageTint, this.mView.getDrawableState());
         } else if (this.mInternalImageTint != null) {
            AppCompatDrawableManager.tintDrawable(var1, this.mInternalImageTint, this.mView.getDrawableState());
         }
      }

   }

   ColorStateList getSupportImageTintList() {
      ColorStateList var1;
      if (this.mImageTint != null) {
         var1 = this.mImageTint.mTintList;
      } else {
         var1 = null;
      }

      return var1;
   }

   Mode getSupportImageTintMode() {
      Mode var1;
      if (this.mImageTint != null) {
         var1 = this.mImageTint.mTintMode;
      } else {
         var1 = null;
      }

      return var1;
   }

   boolean hasOverlappingRendering() {
      Drawable var1 = this.mView.getBackground();
      return VERSION.SDK_INT < 21 || !(var1 instanceof RippleDrawable);
   }

   public void loadFromAttributes(AttributeSet var1, int var2) {
      TintTypedArray var3 = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), var1, R.styleable.AppCompatImageView, var2, 0);

      label484: {
         Throwable var10000;
         label488: {
            Drawable var4;
            boolean var10001;
            try {
               var4 = this.mView.getDrawable();
            } catch (Throwable var60) {
               var10000 = var60;
               var10001 = false;
               break label488;
            }

            Drawable var61 = var4;
            if (var4 == null) {
               try {
                  var2 = var3.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label488;
               }

               var61 = var4;
               if (var2 != -1) {
                  try {
                     var4 = AppCompatResources.getDrawable(this.mView.getContext(), var2);
                  } catch (Throwable var58) {
                     var10000 = var58;
                     var10001 = false;
                     break label488;
                  }

                  var61 = var4;
                  if (var4 != null) {
                     try {
                        this.mView.setImageDrawable(var4);
                     } catch (Throwable var57) {
                        var10000 = var57;
                        var10001 = false;
                        break label488;
                     }

                     var61 = var4;
                  }
               }
            }

            if (var61 != null) {
               try {
                  DrawableUtils.fixDrawable(var61);
               } catch (Throwable var56) {
                  var10000 = var56;
                  var10001 = false;
                  break label488;
               }
            }

            try {
               if (var3.hasValue(R.styleable.AppCompatImageView_tint)) {
                  ImageViewCompat.setImageTintList(this.mView, var3.getColorStateList(R.styleable.AppCompatImageView_tint));
               }
            } catch (Throwable var55) {
               var10000 = var55;
               var10001 = false;
               break label488;
            }

            label462:
            try {
               if (var3.hasValue(R.styleable.AppCompatImageView_tintMode)) {
                  ImageViewCompat.setImageTintMode(this.mView, DrawableUtils.parseTintMode(var3.getInt(R.styleable.AppCompatImageView_tintMode, -1), (Mode)null));
               }
               break label484;
            } catch (Throwable var54) {
               var10000 = var54;
               var10001 = false;
               break label462;
            }
         }

         Throwable var62 = var10000;
         var3.recycle();
         throw var62;
      }

      var3.recycle();
   }

   public void setImageResource(int var1) {
      if (var1 != 0) {
         Drawable var2 = AppCompatResources.getDrawable(this.mView.getContext(), var1);
         if (var2 != null) {
            DrawableUtils.fixDrawable(var2);
         }

         this.mView.setImageDrawable(var2);
      } else {
         this.mView.setImageDrawable((Drawable)null);
      }

      this.applySupportImageTint();
   }

   void setInternalImageTint(ColorStateList var1) {
      if (var1 != null) {
         if (this.mInternalImageTint == null) {
            this.mInternalImageTint = new TintInfo();
         }

         this.mInternalImageTint.mTintList = var1;
         this.mInternalImageTint.mHasTintList = true;
      } else {
         this.mInternalImageTint = null;
      }

      this.applySupportImageTint();
   }

   void setSupportImageTintList(ColorStateList var1) {
      if (this.mImageTint == null) {
         this.mImageTint = new TintInfo();
      }

      this.mImageTint.mTintList = var1;
      this.mImageTint.mHasTintList = true;
      this.applySupportImageTint();
   }

   void setSupportImageTintMode(Mode var1) {
      if (this.mImageTint == null) {
         this.mImageTint = new TintInfo();
      }

      this.mImageTint.mTintMode = var1;
      this.mImageTint.mHasTintMode = true;
      this.applySupportImageTint();
   }
}
