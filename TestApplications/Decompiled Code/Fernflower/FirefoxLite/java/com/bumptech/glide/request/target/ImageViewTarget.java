package com.bumptech.glide.request.target;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.transition.Transition;

public abstract class ImageViewTarget extends ViewTarget implements Transition.ViewAdapter {
   private Animatable animatable;

   public ImageViewTarget(ImageView var1) {
      super(var1);
   }

   private void maybeUpdateAnimatable(Object var1) {
      if (var1 instanceof Animatable) {
         this.animatable = (Animatable)var1;
         this.animatable.start();
      } else {
         this.animatable = null;
      }

   }

   private void setResourceInternal(Object var1) {
      this.maybeUpdateAnimatable(var1);
      this.setResource(var1);
   }

   public void onLoadCleared(Drawable var1) {
      super.onLoadCleared(var1);
      this.setResourceInternal((Object)null);
      this.setDrawable(var1);
   }

   public void onLoadFailed(Drawable var1) {
      super.onLoadFailed(var1);
      this.setResourceInternal((Object)null);
      this.setDrawable(var1);
   }

   public void onLoadStarted(Drawable var1) {
      super.onLoadStarted(var1);
      this.setResourceInternal((Object)null);
      this.setDrawable(var1);
   }

   public void onResourceReady(Object var1, Transition var2) {
      if (var2 != null && var2.transition(var1, this)) {
         this.maybeUpdateAnimatable(var1);
      } else {
         this.setResourceInternal(var1);
      }

   }

   public void onStart() {
      if (this.animatable != null) {
         this.animatable.start();
      }

   }

   public void onStop() {
      if (this.animatable != null) {
         this.animatable.stop();
      }

   }

   public void setDrawable(Drawable var1) {
      ((ImageView)this.view).setImageDrawable(var1);
   }

   protected abstract void setResource(Object var1);
}
