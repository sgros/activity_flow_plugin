package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class BitmapImageViewTarget extends ImageViewTarget {
   public BitmapImageViewTarget(ImageView var1) {
      super(var1);
   }

   protected void setResource(Bitmap var1) {
      ((ImageView)this.view).setImageBitmap(var1);
   }
}
