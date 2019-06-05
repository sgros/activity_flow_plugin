package pl.droidsonroids.gif;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GifImageView extends ImageView {
   private boolean mFreezesAnimation;

   public GifImageView(Context var1) {
      super(var1);
   }

   public GifImageView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.postInit(GifViewUtils.initImageView(this, var2, 0, 0));
   }

   public GifImageView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.postInit(GifViewUtils.initImageView(this, var2, var3, 0));
   }

   @RequiresApi(21)
   public GifImageView(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.postInit(GifViewUtils.initImageView(this, var2, var3, var4));
   }

   private void postInit(GifViewUtils.InitResult var1) {
      this.mFreezesAnimation = var1.mFreezesAnimation;
      if (var1.mSourceResId > 0) {
         super.setImageResource(var1.mSourceResId);
      }

      if (var1.mBackgroundResId > 0) {
         super.setBackgroundResource(var1.mBackgroundResId);
      }

   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof GifViewSavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         GifViewSavedState var2 = (GifViewSavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         var2.restoreState(this.getDrawable(), 0);
         var2.restoreState(this.getBackground(), 1);
      }

   }

   public Parcelable onSaveInstanceState() {
      Drawable var1;
      if (this.mFreezesAnimation) {
         var1 = this.getDrawable();
      } else {
         var1 = null;
      }

      Drawable var2;
      if (this.mFreezesAnimation) {
         var2 = this.getBackground();
      } else {
         var2 = null;
      }

      return new GifViewSavedState(super.onSaveInstanceState(), new Drawable[]{var1, var2});
   }

   public void setBackgroundResource(int var1) {
      if (!GifViewUtils.setResource(this, false, var1)) {
         super.setBackgroundResource(var1);
      }

   }

   public void setFreezesAnimation(boolean var1) {
      this.mFreezesAnimation = var1;
   }

   public void setImageResource(int var1) {
      if (!GifViewUtils.setResource(this, true, var1)) {
         super.setImageResource(var1);
      }

   }

   public void setImageURI(Uri var1) {
      if (!GifViewUtils.setGifImageUri(this, var1)) {
         super.setImageURI(var1);
      }

   }
}
