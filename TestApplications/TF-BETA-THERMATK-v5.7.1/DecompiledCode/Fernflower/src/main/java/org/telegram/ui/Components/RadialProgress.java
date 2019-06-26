package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class RadialProgress {
   private static DecelerateInterpolator decelerateInterpolator;
   private boolean alphaForMiniPrevious = true;
   private boolean alphaForPrevious = true;
   private float animatedAlphaValue = 1.0F;
   private float animatedProgressValue = 0.0F;
   private float animationProgressStart = 0.0F;
   private Drawable checkBackgroundDrawable;
   private RadialProgress.CheckDrawable checkDrawable;
   private RectF cicleRect = new RectF();
   private Drawable currentDrawable;
   private Drawable currentMiniDrawable;
   private boolean currentMiniWithRound;
   private float currentProgress = 0.0F;
   private long currentProgressTime = 0L;
   private boolean currentWithRound;
   private int diff = AndroidUtilities.dp(4.0F);
   private boolean drawMiniProgress;
   private boolean hideCurrentDrawable;
   private long lastUpdateTime = 0L;
   private Bitmap miniDrawBitmap;
   private Canvas miniDrawCanvas;
   private Paint miniProgressBackgroundPaint;
   private Paint miniProgressPaint;
   private float overrideAlpha = 1.0F;
   private View parent;
   private boolean previousCheckDrawable;
   private Drawable previousDrawable;
   private Drawable previousMiniDrawable;
   private boolean previousMiniWithRound;
   private boolean previousWithRound;
   private int progressColor = -1;
   private Paint progressPaint;
   private RectF progressRect = new RectF();
   private float radOffset = 0.0F;

   public RadialProgress(View var1) {
      if (decelerateInterpolator == null) {
         decelerateInterpolator = new DecelerateInterpolator();
      }

      this.progressPaint = new Paint(1);
      this.progressPaint.setStyle(Style.STROKE);
      this.progressPaint.setStrokeCap(Cap.ROUND);
      this.progressPaint.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
      this.miniProgressPaint = new Paint(1);
      this.miniProgressPaint.setStyle(Style.STROKE);
      this.miniProgressPaint.setStrokeCap(Cap.ROUND);
      this.miniProgressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.miniProgressBackgroundPaint = new Paint(1);
      this.parent = var1;
   }

   private void invalidateParent() {
      int var1 = AndroidUtilities.dp(2.0F);
      View var2 = this.parent;
      RectF var3 = this.progressRect;
      int var4 = (int)var3.left;
      int var5 = (int)var3.top;
      int var6 = (int)var3.right;
      int var7 = var1 * 2;
      var2.invalidate(var4 - var1, var5 - var1, var6 + var7, (int)var3.bottom + var7);
   }

   private void updateAnimation(boolean var1) {
      long var2 = System.currentTimeMillis();
      long var4 = var2 - this.lastUpdateTime;
      this.lastUpdateTime = var2;
      Drawable var6 = this.checkBackgroundDrawable;
      if (var6 != null && (this.currentDrawable == var6 || this.previousDrawable == var6) && this.checkDrawable.updateAnimation(var4)) {
         this.invalidateParent();
      }

      boolean var7 = true;
      boolean var8 = true;
      if (var1) {
         if (this.animatedProgressValue != 1.0F) {
            this.radOffset += (float)(360L * var4) / 3000.0F;
            float var9 = this.currentProgress;
            float var10 = this.animationProgressStart;
            float var11 = var9 - var10;
            if (var11 > 0.0F) {
               this.currentProgressTime += var4;
               var2 = this.currentProgressTime;
               if (var2 >= 300L) {
                  this.animatedProgressValue = var9;
                  this.animationProgressStart = var9;
                  this.currentProgressTime = 0L;
               } else {
                  this.animatedProgressValue = var10 + var11 * decelerateInterpolator.getInterpolation((float)var2 / 300.0F);
               }
            }

            this.invalidateParent();
         }

         if (this.drawMiniProgress) {
            if (this.animatedProgressValue >= 1.0F && this.previousMiniDrawable != null) {
               this.animatedAlphaValue -= (float)var4 / 200.0F;
               if (this.animatedAlphaValue <= 0.0F) {
                  this.animatedAlphaValue = 0.0F;
                  this.previousMiniDrawable = null;
                  if (this.currentMiniDrawable != null) {
                     var1 = var8;
                  } else {
                     var1 = false;
                  }

                  this.drawMiniProgress = var1;
               }

               this.invalidateParent();
            }
         } else if (this.animatedProgressValue >= 1.0F && this.previousDrawable != null) {
            this.animatedAlphaValue -= (float)var4 / 200.0F;
            if (this.animatedAlphaValue <= 0.0F) {
               this.animatedAlphaValue = 0.0F;
               this.previousDrawable = null;
            }

            this.invalidateParent();
         }
      } else if (this.drawMiniProgress) {
         if (this.previousMiniDrawable != null) {
            this.animatedAlphaValue -= (float)var4 / 200.0F;
            if (this.animatedAlphaValue <= 0.0F) {
               this.animatedAlphaValue = 0.0F;
               this.previousMiniDrawable = null;
               if (this.currentMiniDrawable != null) {
                  var1 = var7;
               } else {
                  var1 = false;
               }

               this.drawMiniProgress = var1;
            }

            this.invalidateParent();
         }
      } else if (this.previousDrawable != null) {
         this.animatedAlphaValue -= (float)var4 / 200.0F;
         if (this.animatedAlphaValue <= 0.0F) {
            this.animatedAlphaValue = 0.0F;
            this.previousDrawable = null;
         }

         this.invalidateParent();
      }

   }

   public void draw(Canvas var1) {
      Drawable var2;
      Drawable var3;
      float var5;
      RectF var11;
      RectF var12;
      if (this.drawMiniProgress && this.currentDrawable != null) {
         if (this.miniDrawCanvas != null) {
            this.miniDrawBitmap.eraseColor(0);
         }

         this.currentDrawable.setAlpha((int)(this.overrideAlpha * 255.0F));
         if (this.miniDrawCanvas != null) {
            this.currentDrawable.setBounds(0, 0, (int)this.progressRect.width(), (int)this.progressRect.height());
            this.currentDrawable.draw(this.miniDrawCanvas);
         } else {
            var2 = this.currentDrawable;
            var12 = this.progressRect;
            var2.setBounds((int)var12.left, (int)var12.top, (int)var12.right, (int)var12.bottom);
            this.currentDrawable.draw(var1);
         }

         float var6;
         float var7;
         byte var8;
         byte var13;
         if (Math.abs(this.progressRect.width() - (float)AndroidUtilities.dp(44.0F)) < AndroidUtilities.density) {
            var13 = 20;
            var5 = this.progressRect.centerX();
            var6 = (float)16;
            var7 = (float)AndroidUtilities.dp(var6);
            var6 = this.progressRect.centerY() + (float)AndroidUtilities.dp(var6);
            var5 += var7;
            var8 = 0;
         } else {
            var13 = 22;
            var5 = this.progressRect.centerX();
            var7 = (float)AndroidUtilities.dp(18.0F);
            var6 = this.progressRect.centerY() + (float)AndroidUtilities.dp(18.0F);
            var5 += var7;
            var8 = 2;
         }

         int var9 = var13 / 2;
         if (this.previousMiniDrawable != null && this.alphaForMiniPrevious) {
            var7 = this.animatedAlphaValue;
            var7 = this.overrideAlpha * var7;
         } else {
            var7 = 1.0F;
         }

         Canvas var14 = this.miniDrawCanvas;
         float var10;
         if (var14 != null) {
            var10 = (float)(var13 + 18 + var8);
            var14.drawCircle((float)AndroidUtilities.dp(var10), (float)AndroidUtilities.dp(var10), (float)AndroidUtilities.dp((float)(var9 + 1)) * var7, Theme.checkboxSquare_eraserPaint);
         } else {
            this.miniProgressBackgroundPaint.setColor(this.progressColor);
            if (this.previousMiniDrawable != null && this.currentMiniDrawable == null) {
               this.miniProgressBackgroundPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.overrideAlpha));
            } else {
               this.miniProgressBackgroundPaint.setAlpha(255);
            }

            var1.drawCircle(var5, var6, (float)AndroidUtilities.dp(12.0F), this.miniProgressBackgroundPaint);
         }

         if (this.miniDrawCanvas != null) {
            Bitmap var15 = this.miniDrawBitmap;
            var11 = this.progressRect;
            var1.drawBitmap(var15, (float)((int)var11.left), (float)((int)var11.top), (Paint)null);
         }

         var3 = this.previousMiniDrawable;
         if (var3 != null) {
            if (this.alphaForMiniPrevious) {
               var3.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.overrideAlpha));
            } else {
               var3.setAlpha((int)(this.overrideAlpha * 255.0F));
            }

            var3 = this.previousMiniDrawable;
            var10 = (float)var9;
            var3.setBounds((int)(var5 - (float)AndroidUtilities.dp(var10) * var7), (int)(var6 - (float)AndroidUtilities.dp(var10) * var7), (int)((float)AndroidUtilities.dp(var10) * var7 + var5), (int)((float)AndroidUtilities.dp(var10) * var7 + var6));
            this.previousMiniDrawable.draw(var1);
         }

         if (!this.hideCurrentDrawable) {
            var3 = this.currentMiniDrawable;
            if (var3 != null) {
               if (this.previousMiniDrawable != null) {
                  var3.setAlpha((int)((1.0F - this.animatedAlphaValue) * 255.0F * this.overrideAlpha));
               } else {
                  var3.setAlpha((int)(this.overrideAlpha * 255.0F));
               }

               var3 = this.currentMiniDrawable;
               var10 = (float)var9;
               var3.setBounds((int)(var5 - (float)AndroidUtilities.dp(var10)), (int)(var6 - (float)AndroidUtilities.dp(var10)), (int)((float)AndroidUtilities.dp(var10) + var5), (int)((float)AndroidUtilities.dp(var10) + var6));
               this.currentMiniDrawable.draw(var1);
            }
         }

         if (!this.currentMiniWithRound && !this.previousMiniWithRound) {
            this.updateAnimation(false);
         } else {
            this.miniProgressPaint.setColor(this.progressColor);
            if (this.previousMiniWithRound) {
               this.miniProgressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.overrideAlpha));
            } else {
               this.miniProgressPaint.setAlpha((int)(this.overrideAlpha * 255.0F));
            }

            var12 = this.cicleRect;
            var10 = (float)(var9 - 2);
            var12.set(var5 - (float)AndroidUtilities.dp(var10) * var7, var6 - (float)AndroidUtilities.dp(var10) * var7, var5 + (float)AndroidUtilities.dp(var10) * var7, var6 + (float)AndroidUtilities.dp(var10) * var7);
            var1.drawArc(this.cicleRect, this.radOffset - 90.0F, Math.max(4.0F, this.animatedProgressValue * 360.0F), false, this.miniProgressPaint);
            this.updateAnimation(true);
         }
      } else {
         var3 = this.previousDrawable;
         if (var3 != null) {
            if (this.alphaForPrevious) {
               var3.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.overrideAlpha));
            } else {
               var3.setAlpha((int)(this.overrideAlpha * 255.0F));
            }

            var2 = this.previousDrawable;
            var12 = this.progressRect;
            var2.setBounds((int)var12.left, (int)var12.top, (int)var12.right, (int)var12.bottom);
            this.previousDrawable.draw(var1);
         }

         if (!this.hideCurrentDrawable) {
            var3 = this.currentDrawable;
            if (var3 != null) {
               if (this.previousDrawable != null) {
                  var3.setAlpha((int)((1.0F - this.animatedAlphaValue) * 255.0F * this.overrideAlpha));
               } else {
                  var3.setAlpha((int)(this.overrideAlpha * 255.0F));
               }

               var2 = this.currentDrawable;
               var12 = this.progressRect;
               var2.setBounds((int)var12.left, (int)var12.top, (int)var12.right, (int)var12.bottom);
               this.currentDrawable.draw(var1);
            }
         }

         if (!this.currentWithRound && !this.previousWithRound) {
            this.updateAnimation(false);
         } else {
            this.progressPaint.setColor(this.progressColor);
            if (this.previousWithRound) {
               this.progressPaint.setAlpha((int)(this.animatedAlphaValue * 255.0F * this.overrideAlpha));
            } else {
               this.progressPaint.setAlpha((int)(this.overrideAlpha * 255.0F));
            }

            var12 = this.cicleRect;
            var11 = this.progressRect;
            var5 = var11.left;
            int var4 = this.diff;
            var12.set(var5 + (float)var4, var11.top + (float)var4, var11.right - (float)var4, var11.bottom - (float)var4);
            var1.drawArc(this.cicleRect, this.radOffset - 90.0F, Math.max(4.0F, this.animatedProgressValue * 360.0F), false, this.progressPaint);
            this.updateAnimation(true);
         }
      }

   }

   public float getAlpha() {
      float var1;
      if (this.previousDrawable == null && this.currentDrawable == null) {
         var1 = 0.0F;
      } else {
         var1 = this.animatedAlphaValue;
      }

      return var1;
   }

   public RectF getProgressRect() {
      return this.progressRect;
   }

   public boolean isDrawCheckDrawable() {
      boolean var1;
      if (this.currentDrawable == this.checkBackgroundDrawable) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setAlphaForMiniPrevious(boolean var1) {
      this.alphaForMiniPrevious = var1;
   }

   public void setAlphaForPrevious(boolean var1) {
      this.alphaForPrevious = var1;
   }

   public void setBackground(Drawable var1, boolean var2, boolean var3) {
      label17: {
         this.lastUpdateTime = System.currentTimeMillis();
         if (var3) {
            Drawable var4 = this.currentDrawable;
            if (var4 != var1) {
               this.previousDrawable = var4;
               this.previousWithRound = this.currentWithRound;
               this.animatedAlphaValue = 1.0F;
               this.setProgress(1.0F, var3);
               break label17;
            }
         }

         this.previousDrawable = null;
         this.previousWithRound = false;
      }

      this.currentWithRound = var2;
      this.currentDrawable = var1;
      if (!var3) {
         this.parent.invalidate();
      } else {
         this.invalidateParent();
      }

   }

   public void setCheckBackground(boolean var1, boolean var2) {
      if (this.checkDrawable == null) {
         this.checkDrawable = new RadialProgress.CheckDrawable();
         this.checkBackgroundDrawable = Theme.createCircleDrawableWithIcon(AndroidUtilities.dp(48.0F), this.checkDrawable, 0);
      }

      Theme.setCombinedDrawableColor(this.checkBackgroundDrawable, Theme.getColor("chat_mediaLoaderPhoto"), false);
      Theme.setCombinedDrawableColor(this.checkBackgroundDrawable, Theme.getColor("chat_mediaLoaderPhotoIcon"), true);
      Drawable var3 = this.currentDrawable;
      Drawable var4 = this.checkBackgroundDrawable;
      if (var3 != var4) {
         this.setBackground(var4, var1, var2);
         this.checkDrawable.resetProgress(var2);
      }

   }

   public void setDiff(int var1) {
      this.diff = var1;
   }

   public void setHideCurrentDrawable(boolean var1) {
      this.hideCurrentDrawable = var1;
   }

   public void setMiniBackground(Drawable var1, boolean var2, boolean var3) {
      boolean var4;
      label36: {
         this.lastUpdateTime = System.currentTimeMillis();
         var4 = false;
         if (var3) {
            Drawable var5 = this.currentMiniDrawable;
            if (var5 != var1) {
               this.previousMiniDrawable = var5;
               this.previousMiniWithRound = this.currentMiniWithRound;
               this.animatedAlphaValue = 1.0F;
               this.setProgress(1.0F, var3);
               break label36;
            }
         }

         this.previousMiniDrawable = null;
         this.previousMiniWithRound = false;
      }

      label31: {
         this.currentMiniWithRound = var2;
         this.currentMiniDrawable = var1;
         if (this.previousMiniDrawable == null) {
            var2 = var4;
            if (this.currentMiniDrawable == null) {
               break label31;
            }
         }

         var2 = true;
      }

      this.drawMiniProgress = var2;
      if (this.drawMiniProgress && this.miniDrawBitmap == null) {
         try {
            this.miniDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(48.0F), AndroidUtilities.dp(48.0F), Config.ARGB_8888);
            Canvas var7 = new Canvas(this.miniDrawBitmap);
            this.miniDrawCanvas = var7;
         } catch (Throwable var6) {
         }
      }

      if (!var3) {
         this.parent.invalidate();
      } else {
         this.invalidateParent();
      }

   }

   public void setMiniProgressBackgroundColor(int var1) {
      this.miniProgressBackgroundPaint.setColor(var1);
   }

   public void setOverrideAlpha(float var1) {
      this.overrideAlpha = var1;
   }

   public void setProgress(float var1, boolean var2) {
      if (this.drawMiniProgress) {
         if (var1 != 1.0F && this.animatedAlphaValue != 0.0F && this.previousMiniDrawable != null) {
            this.animatedAlphaValue = 0.0F;
            this.previousMiniDrawable = null;
            boolean var3;
            if (this.currentMiniDrawable != null) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.drawMiniProgress = var3;
         }
      } else if (var1 != 1.0F && this.animatedAlphaValue != 0.0F && this.previousDrawable != null) {
         this.animatedAlphaValue = 0.0F;
         this.previousDrawable = null;
      }

      if (!var2) {
         this.animatedProgressValue = var1;
         this.animationProgressStart = var1;
      } else {
         if (this.animatedProgressValue > var1) {
            this.animatedProgressValue = var1;
         }

         this.animationProgressStart = this.animatedProgressValue;
      }

      this.currentProgress = var1;
      this.currentProgressTime = 0L;
      this.invalidateParent();
   }

   public void setProgressColor(int var1) {
      this.progressColor = var1;
   }

   public void setProgressRect(int var1, int var2, int var3, int var4) {
      this.progressRect.set((float)var1, (float)var2, (float)var3, (float)var4);
   }

   public void setStrokeWidth(int var1) {
      this.progressPaint.setStrokeWidth((float)var1);
   }

   public boolean swapBackground(Drawable var1) {
      if (this.currentDrawable != var1) {
         this.currentDrawable = var1;
         return true;
      } else {
         return false;
      }
   }

   public boolean swapMiniBackground(Drawable var1) {
      Drawable var2 = this.currentMiniDrawable;
      boolean var3 = false;
      if (var2 == var1) {
         return false;
      } else {
         this.currentMiniDrawable = var1;
         if (this.previousMiniDrawable != null || this.currentMiniDrawable != null) {
            var3 = true;
         }

         this.drawMiniProgress = var3;
         return true;
      }
   }

   private class CheckDrawable extends Drawable {
      private Paint paint = new Paint(1);
      private float progress;

      public CheckDrawable() {
         this.paint.setStyle(Style.STROKE);
         this.paint.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
         this.paint.setStrokeCap(Cap.ROUND);
         this.paint.setColor(-1);
      }

      public void draw(Canvas var1) {
         int var2 = this.getBounds().centerX() - AndroidUtilities.dp(12.0F);
         int var3 = this.getBounds().centerY() - AndroidUtilities.dp(6.0F);
         float var4 = this.progress;
         float var5 = 1.0F;
         if (var4 != 1.0F) {
            var5 = RadialProgress.decelerateInterpolator.getInterpolation(this.progress);
         }

         int var6 = (int)((float)AndroidUtilities.dp(7.0F) - (float)AndroidUtilities.dp(6.0F) * var5);
         int var7 = (int)(AndroidUtilities.dpf2(13.0F) - (float)AndroidUtilities.dp(6.0F) * var5);
         var1.drawLine((float)(AndroidUtilities.dp(7.0F) + var2), (float)((int)AndroidUtilities.dpf2(13.0F) + var3), (float)(var6 + var2), (float)(var7 + var3), this.paint);
         var7 = (int)(AndroidUtilities.dpf2(7.0F) + (float)AndroidUtilities.dp(13.0F) * var5);
         var6 = (int)(AndroidUtilities.dpf2(13.0F) - (float)AndroidUtilities.dp(13.0F) * var5);
         var1.drawLine((float)((int)AndroidUtilities.dpf2(7.0F) + var2), (float)((int)AndroidUtilities.dpf2(13.0F) + var3), (float)(var2 + var7), (float)(var3 + var6), this.paint);
      }

      public int getIntrinsicHeight() {
         return AndroidUtilities.dp(48.0F);
      }

      public int getIntrinsicWidth() {
         return AndroidUtilities.dp(48.0F);
      }

      public int getOpacity() {
         return -2;
      }

      public void resetProgress(boolean var1) {
         float var2;
         if (var1) {
            var2 = 0.0F;
         } else {
            var2 = 1.0F;
         }

         this.progress = var2;
      }

      public void setAlpha(int var1) {
         this.paint.setAlpha(var1);
      }

      public void setColorFilter(ColorFilter var1) {
         this.paint.setColorFilter(var1);
      }

      public boolean updateAnimation(long var1) {
         float var3 = this.progress;
         if (var3 < 1.0F) {
            this.progress = var3 + (float)var1 / 700.0F;
            if (this.progress > 1.0F) {
               this.progress = 1.0F;
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
