package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.widget.TintableImageSourceView;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AppCompatImageView extends ImageView implements TintableBackgroundView, TintableImageSourceView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper;
   private final AppCompatImageHelper mImageHelper;

   public AppCompatImageView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatImageView(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public AppCompatImageView(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
      this.mBackgroundTintHelper.loadFromAttributes(var2, var3);
      this.mImageHelper = new AppCompatImageHelper(this);
      this.mImageHelper.loadFromAttributes(var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.applySupportBackgroundTint();
      }

      if (this.mImageHelper != null) {
         this.mImageHelper.applySupportImageTint();
      }

   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ColorStateList getSupportBackgroundTintList() {
      ColorStateList var1;
      if (this.mBackgroundTintHelper != null) {
         var1 = this.mBackgroundTintHelper.getSupportBackgroundTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public Mode getSupportBackgroundTintMode() {
      Mode var1;
      if (this.mBackgroundTintHelper != null) {
         var1 = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public ColorStateList getSupportImageTintList() {
      ColorStateList var1;
      if (this.mImageHelper != null) {
         var1 = this.mImageHelper.getSupportImageTintList();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public Mode getSupportImageTintMode() {
      Mode var1;
      if (this.mImageHelper != null) {
         var1 = this.mImageHelper.getSupportImageTintMode();
      } else {
         var1 = null;
      }

      return var1;
   }

   public boolean hasOverlappingRendering() {
      boolean var1;
      if (this.mImageHelper.hasOverlappingRendering() && super.hasOverlappingRendering()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.onSetBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(@DrawableRes int var1) {
      super.setBackgroundResource(var1);
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.onSetBackgroundResource(var1);
      }

   }

   public void setImageBitmap(Bitmap var1) {
      super.setImageBitmap(var1);
      if (this.mImageHelper != null) {
         this.mImageHelper.applySupportImageTint();
      }

   }

   public void setImageDrawable(@Nullable Drawable var1) {
      super.setImageDrawable(var1);
      if (this.mImageHelper != null) {
         this.mImageHelper.applySupportImageTint();
      }

   }

   public void setImageIcon(@Nullable Icon var1) {
      super.setImageIcon(var1);
      if (this.mImageHelper != null) {
         this.mImageHelper.applySupportImageTint();
      }

   }

   public void setImageResource(@DrawableRes int var1) {
      if (this.mImageHelper != null) {
         this.mImageHelper.setImageResource(var1);
      }

   }

   public void setImageURI(@Nullable Uri var1) {
      super.setImageURI(var1);
      if (this.mImageHelper != null) {
         this.mImageHelper.applySupportImageTint();
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportBackgroundTintList(@Nullable ColorStateList var1) {
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.setSupportBackgroundTintList(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportBackgroundTintMode(@Nullable Mode var1) {
      if (this.mBackgroundTintHelper != null) {
         this.mBackgroundTintHelper.setSupportBackgroundTintMode(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportImageTintList(@Nullable ColorStateList var1) {
      if (this.mImageHelper != null) {
         this.mImageHelper.setSupportImageTintList(var1);
      }

   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public void setSupportImageTintMode(@Nullable Mode var1) {
      if (this.mImageHelper != null) {
         this.mImageHelper.setSupportImageTintMode(var1);
      }

   }
}
