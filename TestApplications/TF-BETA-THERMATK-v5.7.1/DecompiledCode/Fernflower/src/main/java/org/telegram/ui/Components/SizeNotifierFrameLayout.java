package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.ActionBar;

public class SizeNotifierFrameLayout extends FrameLayout {
   private Drawable backgroundDrawable;
   private int bottomClip;
   private SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate delegate;
   private int keyboardHeight;
   private boolean occupyStatusBar = true;
   private WallpaperParallaxEffect parallaxEffect;
   private float parallaxScale = 1.0F;
   private boolean paused = true;
   private android.graphics.Rect rect = new android.graphics.Rect();
   private float translationX;
   private float translationY;

   public SizeNotifierFrameLayout(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
   }

   public Drawable getBackgroundImage() {
      return this.backgroundDrawable;
   }

   public int getKeyboardHeight() {
      View var1 = this.getRootView();
      this.getWindowVisibleDisplayFrame(this.rect);
      int var2 = var1.getHeight();
      int var3;
      if (this.rect.top != 0) {
         var3 = AndroidUtilities.statusBarHeight;
      } else {
         var3 = 0;
      }

      int var4 = AndroidUtilities.getViewInset(var1);
      android.graphics.Rect var5 = this.rect;
      return var2 - var3 - var4 - (var5.bottom - var5.top);
   }

   protected boolean isActionBarVisible() {
      return true;
   }

   // $FF: synthetic method
   public void lambda$notifyHeightChanged$1$SizeNotifierFrameLayout(boolean var1) {
      SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate var2 = this.delegate;
      if (var2 != null) {
         var2.onSizeChanged(this.keyboardHeight, var1);
      }

   }

   // $FF: synthetic method
   public void lambda$setBackgroundImage$0$SizeNotifierFrameLayout(int var1, int var2) {
      this.translationX = (float)var1;
      this.translationY = (float)var2;
      this.invalidate();
   }

   public void notifyHeightChanged() {
      if (this.delegate != null) {
         WallpaperParallaxEffect var1 = this.parallaxEffect;
         if (var1 != null) {
            this.parallaxScale = var1.getScale(this.getMeasuredWidth(), this.getMeasuredHeight());
         }

         this.keyboardHeight = this.getKeyboardHeight();
         android.graphics.Point var3 = AndroidUtilities.displaySize;
         boolean var2;
         if (var3.x > var3.y) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.post(new _$$Lambda$SizeNotifierFrameLayout$k4g_DFX6SvnMtnN4qrfdhEQSo18(this, var2));
      }

   }

   protected void onDraw(Canvas var1) {
      Drawable var2 = this.backgroundDrawable;
      if (var2 != null) {
         if (var2 instanceof ColorDrawable) {
            if (this.bottomClip != 0) {
               var1.save();
               var1.clipRect(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight() - this.bottomClip);
            }

            this.backgroundDrawable.setBounds(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight());
            this.backgroundDrawable.draw(var1);
            if (this.bottomClip != 0) {
               var1.restore();
            }
         } else if (var2 instanceof BitmapDrawable) {
            float var3;
            if (((BitmapDrawable)var2).getTileModeX() == TileMode.REPEAT) {
               var1.save();
               var3 = 2.0F / AndroidUtilities.density;
               var1.scale(var3, var3);
               this.backgroundDrawable.setBounds(0, 0, (int)Math.ceil((double)((float)this.getMeasuredWidth() / var3)), (int)Math.ceil((double)((float)this.getMeasuredHeight() / var3)));
               this.backgroundDrawable.draw(var1);
               var1.restore();
            } else {
               int var4;
               if (this.isActionBarVisible()) {
                  var4 = ActionBar.getCurrentActionBarHeight();
               } else {
                  var4 = 0;
               }

               int var5;
               if (VERSION.SDK_INT >= 21 && this.occupyStatusBar) {
                  var5 = AndroidUtilities.statusBarHeight;
               } else {
                  var5 = 0;
               }

               int var6 = var4 + var5;
               int var7 = this.getMeasuredHeight() - var6;
               float var8 = (float)this.getMeasuredWidth() / (float)this.backgroundDrawable.getIntrinsicWidth();
               float var9 = (float)(this.keyboardHeight + var7) / (float)this.backgroundDrawable.getIntrinsicHeight();
               var3 = var8;
               if (var8 < var9) {
                  var3 = var9;
               }

               var5 = (int)Math.ceil((double)((float)this.backgroundDrawable.getIntrinsicWidth() * var3 * this.parallaxScale));
               var4 = (int)Math.ceil((double)((float)this.backgroundDrawable.getIntrinsicHeight() * var3 * this.parallaxScale));
               int var10 = (this.getMeasuredWidth() - var5) / 2 + (int)this.translationX;
               var7 = (var7 - var4 + this.keyboardHeight) / 2 + var6 + (int)this.translationY;
               var1.save();
               var1.clipRect(0, var6, var5, this.getMeasuredHeight() - this.bottomClip);
               this.backgroundDrawable.setAlpha(255);
               this.backgroundDrawable.setBounds(var10, var7, var5 + var10, var4 + var7);
               this.backgroundDrawable.draw(var1);
               var1.restore();
            }
         }
      } else {
         super.onDraw(var1);
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.notifyHeightChanged();
   }

   public void onPause() {
      WallpaperParallaxEffect var1 = this.parallaxEffect;
      if (var1 != null) {
         var1.setEnabled(false);
      }

      this.paused = true;
   }

   public void onResume() {
      WallpaperParallaxEffect var1 = this.parallaxEffect;
      if (var1 != null) {
         var1.setEnabled(true);
      }

      this.paused = false;
   }

   public void setBackgroundImage(Drawable var1, boolean var2) {
      this.backgroundDrawable = var1;
      if (var2) {
         if (this.parallaxEffect == null) {
            this.parallaxEffect = new WallpaperParallaxEffect(this.getContext());
            this.parallaxEffect.setCallback(new _$$Lambda$SizeNotifierFrameLayout$9xVW1u9E8sqKGYKEgYUca0gqvJA(this));
            if (this.getMeasuredWidth() != 0 && this.getMeasuredHeight() != 0) {
               this.parallaxScale = this.parallaxEffect.getScale(this.getMeasuredWidth(), this.getMeasuredHeight());
            }
         }

         if (!this.paused) {
            this.parallaxEffect.setEnabled(true);
         }
      } else {
         WallpaperParallaxEffect var3 = this.parallaxEffect;
         if (var3 != null) {
            var3.setEnabled(false);
            this.parallaxEffect = null;
            this.parallaxScale = 1.0F;
            this.translationX = 0.0F;
            this.translationY = 0.0F;
         }
      }

      this.invalidate();
   }

   public void setBottomClip(int var1) {
      this.bottomClip = var1;
   }

   public void setDelegate(SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate var1) {
      this.delegate = var1;
   }

   public void setOccupyStatusBar(boolean var1) {
      this.occupyStatusBar = var1;
   }

   public interface SizeNotifierFrameLayoutDelegate {
      void onSizeChanged(int var1, boolean var2);
   }
}
