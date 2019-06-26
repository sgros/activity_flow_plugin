package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageButton;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.TintableImageSourceView;

public class AppCompatImageButton extends ImageButton implements TintableBackgroundView, TintableImageSourceView {
   private final AppCompatBackgroundHelper mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
   private final AppCompatImageHelper mImageHelper;

   public AppCompatImageButton(Context var1, AttributeSet var2, int var3) {
      super(TintContextWrapper.wrap(var1), var2, var3);
      this.mBackgroundTintHelper.loadFromAttributes(var2, var3);
      this.mImageHelper = new AppCompatImageHelper(this);
      this.mImageHelper.loadFromAttributes(var2, var3);
   }

   protected void drawableStateChanged() {
      super.drawableStateChanged();
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      if (var1 != null) {
         var1.applySupportBackgroundTint();
      }

      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public ColorStateList getSupportBackgroundTintList() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      ColorStateList var2;
      if (var1 != null) {
         var2 = var1.getSupportBackgroundTintList();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Mode getSupportBackgroundTintMode() {
      AppCompatBackgroundHelper var1 = this.mBackgroundTintHelper;
      Mode var2;
      if (var1 != null) {
         var2 = var1.getSupportBackgroundTintMode();
      } else {
         var2 = null;
      }

      return var2;
   }

   public ColorStateList getSupportImageTintList() {
      AppCompatImageHelper var1 = this.mImageHelper;
      ColorStateList var2;
      if (var1 != null) {
         var2 = var1.getSupportImageTintList();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Mode getSupportImageTintMode() {
      AppCompatImageHelper var1 = this.mImageHelper;
      Mode var2;
      if (var1 != null) {
         var2 = var1.getSupportImageTintMode();
      } else {
         var2 = null;
      }

      return var2;
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
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundDrawable(var1);
      }

   }

   public void setBackgroundResource(int var1) {
      super.setBackgroundResource(var1);
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.onSetBackgroundResource(var1);
      }

   }

   public void setImageBitmap(Bitmap var1) {
      super.setImageBitmap(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setImageDrawable(Drawable var1) {
      super.setImageDrawable(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setImageResource(int var1) {
      this.mImageHelper.setImageResource(var1);
   }

   public void setImageURI(Uri var1) {
      super.setImageURI(var1);
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.applySupportImageTint();
      }

   }

   public void setSupportBackgroundTintList(ColorStateList var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintList(var1);
      }

   }

   public void setSupportBackgroundTintMode(Mode var1) {
      AppCompatBackgroundHelper var2 = this.mBackgroundTintHelper;
      if (var2 != null) {
         var2.setSupportBackgroundTintMode(var1);
      }

   }

   public void setSupportImageTintList(ColorStateList var1) {
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.setSupportImageTintList(var1);
      }

   }

   public void setSupportImageTintMode(Mode var1) {
      AppCompatImageHelper var2 = this.mImageHelper;
      if (var2 != null) {
         var2.setSupportImageTintMode(var1);
      }

   }
}
