package org.telegram.ui.Components;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.SecureDocument;

public class BackupImageView extends View {
   private int height = -1;
   private ImageReceiver imageReceiver;
   private int width = -1;

   public BackupImageView(Context var1) {
      super(var1);
      this.init();
   }

   public BackupImageView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.init();
   }

   public BackupImageView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   private void init() {
      this.imageReceiver = new ImageReceiver(this);
   }

   public ImageReceiver getImageReceiver() {
      return this.imageReceiver;
   }

   public int getRoundRadius() {
      return this.imageReceiver.getRoundRadius();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.imageReceiver.onAttachedToWindow();
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.imageReceiver.onDetachedFromWindow();
   }

   protected void onDraw(Canvas var1) {
      if (this.width != -1 && this.height != -1) {
         ImageReceiver var2 = this.imageReceiver;
         int var3 = (this.getWidth() - this.width) / 2;
         int var4 = this.getHeight();
         int var5 = this.height;
         var2.setImageCoords(var3, (var4 - var5) / 2, this.width, var5);
      } else {
         this.imageReceiver.setImageCoords(0, 0, this.getWidth(), this.getHeight());
      }

      this.imageReceiver.draw(var1);
   }

   public void setAspectFit(boolean var1) {
      this.imageReceiver.setAspectFit(var1);
   }

   public void setImage(String var1, String var2, Drawable var3) {
      this.setImage(ImageLocation.getForPath(var1), var2, (ImageLocation)null, (String)null, var3, (Bitmap)null, (String)null, 0, (Object)null);
   }

   public void setImage(String var1, String var2, String var3, String var4) {
      this.setImage(ImageLocation.getForPath(var1), var2, ImageLocation.getForPath(var3), var4, (Drawable)null, (Bitmap)null, (String)null, 0, (Object)null);
   }

   public void setImage(ImageLocation var1, String var2, Bitmap var3, int var4, Object var5) {
      this.setImage(var1, var2, (ImageLocation)null, (String)null, (Drawable)null, var3, (String)null, var4, var5);
   }

   public void setImage(ImageLocation var1, String var2, Bitmap var3, Object var4) {
      this.setImage(var1, var2, (ImageLocation)null, (String)null, (Drawable)null, var3, (String)null, 0, var4);
   }

   public void setImage(ImageLocation var1, String var2, Drawable var3, int var4, Object var5) {
      this.setImage(var1, var2, (ImageLocation)null, (String)null, var3, (Bitmap)null, (String)null, var4, var5);
   }

   public void setImage(ImageLocation var1, String var2, Drawable var3, Object var4) {
      this.setImage(var1, var2, (ImageLocation)null, (String)null, var3, (Bitmap)null, (String)null, 0, var4);
   }

   public void setImage(ImageLocation var1, String var2, String var3, Drawable var4, Object var5) {
      this.setImage(var1, var2, (ImageLocation)null, (String)null, var4, (Bitmap)null, var3, 0, var5);
   }

   public void setImage(ImageLocation var1, String var2, ImageLocation var3, String var4, int var5, Object var6) {
      this.setImage(var1, var2, var3, var4, (Drawable)null, (Bitmap)null, (String)null, var5, var6);
   }

   public void setImage(ImageLocation var1, String var2, ImageLocation var3, String var4, Drawable var5, Bitmap var6, String var7, int var8, Object var9) {
      if (var6 != null) {
         var5 = new BitmapDrawable((Resources)null, var6);
      }

      this.imageReceiver.setImage(var1, var2, var3, var4, (Drawable)var5, var8, var7, var9, 0);
   }

   public void setImage(ImageLocation var1, String var2, ImageLocation var3, String var4, String var5, int var6, int var7, Object var8) {
      this.imageReceiver.setImage(var1, var2, var3, var4, (Drawable)null, var6, var5, var8, var7);
   }

   public void setImage(SecureDocument var1, String var2) {
      this.setImage(ImageLocation.getForSecureDocument(var1), var2, (ImageLocation)null, (String)null, (Drawable)null, (Bitmap)null, (String)null, 0, (Object)null);
   }

   public void setImageBitmap(Bitmap var1) {
      this.imageReceiver.setImageBitmap(var1);
   }

   public void setImageDrawable(Drawable var1) {
      this.imageReceiver.setImageBitmap(var1);
   }

   public void setImageResource(int var1) {
      Drawable var2 = this.getResources().getDrawable(var1);
      this.imageReceiver.setImageBitmap(var2);
      this.invalidate();
   }

   public void setImageResource(int var1, int var2) {
      Drawable var3 = this.getResources().getDrawable(var1);
      if (var3 != null) {
         var3.setColorFilter(new PorterDuffColorFilter(var2, Mode.MULTIPLY));
      }

      this.imageReceiver.setImageBitmap(var3);
      this.invalidate();
   }

   public void setOrientation(int var1, boolean var2) {
      this.imageReceiver.setOrientation(var1, var2);
   }

   public void setRoundRadius(int var1) {
      this.imageReceiver.setRoundRadius(var1);
      this.invalidate();
   }

   public void setSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }
}
