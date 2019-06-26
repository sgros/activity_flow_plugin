package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class RadialProgress2 {
   private int backgroundStroke;
   private int circleColor;
   private String circleColorKey;
   private Paint circleMiniPaint = new Paint(1);
   private Paint circlePaint = new Paint(1);
   private int circlePressedColor;
   private String circlePressedColorKey;
   private int circleRadius;
   private boolean drawBackground = true;
   private boolean drawMiniIcon;
   private int iconColor;
   private String iconColorKey;
   private int iconPressedColor;
   private String iconPressedColorKey;
   private boolean isPressed;
   private boolean isPressedMini;
   private MediaActionDrawable mediaActionDrawable;
   private Bitmap miniDrawBitmap;
   private Canvas miniDrawCanvas;
   private MediaActionDrawable miniMediaActionDrawable;
   private Paint miniProgressBackgroundPaint = new Paint(1);
   private ImageReceiver overlayImageView;
   private Paint overlayPaint = new Paint(1);
   private float overrideAlpha = 1.0F;
   private View parent;
   private boolean previousCheckDrawable;
   private int progressColor = -1;
   private RectF progressRect = new RectF();

   public RadialProgress2(View var1) {
      this.parent = var1;
      this.overlayImageView = new ImageReceiver(var1);
      this.overlayImageView.setInvalidateAll(true);
      this.mediaActionDrawable = new MediaActionDrawable();
      MediaActionDrawable var2 = this.mediaActionDrawable;
      var1.getClass();
      var2.setDelegate(new _$$Lambda$F8rg4UBMmP_S27QL_K3VXBnPS_E(var1));
      this.miniMediaActionDrawable = new MediaActionDrawable();
      var2 = this.miniMediaActionDrawable;
      var1.getClass();
      var2.setDelegate(new _$$Lambda$F8rg4UBMmP_S27QL_K3VXBnPS_E(var1));
      this.miniMediaActionDrawable.setMini(true);
      this.miniMediaActionDrawable.setIcon(4, false);
      this.circleRadius = AndroidUtilities.dp(22.0F);
      this.overlayImageView.setRoundRadius(this.circleRadius);
      this.overlayPaint.setColor(1677721600);
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

   public void draw(Canvas var1) {
      if (this.mediaActionDrawable.getCurrentIcon() != 4 || this.mediaActionDrawable.getTransitionProgress() < 1.0F) {
         int var2;
         int var3;
         float var4;
         label179: {
            label178: {
               var2 = this.mediaActionDrawable.getCurrentIcon();
               var3 = this.mediaActionDrawable.getPreviousIcon();
               if (this.backgroundStroke != 0) {
                  if (var2 == 3) {
                     var4 = this.mediaActionDrawable.getTransitionProgress();
                     break label178;
                  }

                  if (var3 == 3) {
                     var4 = this.mediaActionDrawable.getTransitionProgress();
                     break label179;
                  }
               } else {
                  if ((var2 == 3 || var2 == 6 || var2 == 10 || var2 == 8 || var2 == 0) && var3 == 4) {
                     var4 = this.mediaActionDrawable.getTransitionProgress();
                     break label179;
                  }

                  if (var2 == 4) {
                     var4 = this.mediaActionDrawable.getTransitionProgress();
                     break label178;
                  }
               }

               var4 = 1.0F;
               break label179;
            }

            var4 = 1.0F - var4;
         }

         String var5;
         if (this.isPressedMini) {
            var5 = this.iconPressedColorKey;
            if (var5 != null) {
               this.miniMediaActionDrawable.setColor(Theme.getColor(var5));
            } else {
               this.miniMediaActionDrawable.setColor(this.iconPressedColor);
            }

            var5 = this.circlePressedColorKey;
            if (var5 != null) {
               this.circleMiniPaint.setColor(Theme.getColor(var5));
            } else {
               this.circleMiniPaint.setColor(this.circlePressedColor);
            }
         } else {
            var5 = this.iconColorKey;
            if (var5 != null) {
               this.miniMediaActionDrawable.setColor(Theme.getColor(var5));
            } else {
               this.miniMediaActionDrawable.setColor(this.iconColor);
            }

            var5 = this.circleColorKey;
            if (var5 != null) {
               this.circleMiniPaint.setColor(Theme.getColor(var5));
            } else {
               this.circleMiniPaint.setColor(this.circleColor);
            }
         }

         String var6;
         MediaActionDrawable var21;
         if (this.isPressed) {
            var6 = this.iconPressedColorKey;
            if (var6 != null) {
               var21 = this.mediaActionDrawable;
               var3 = Theme.getColor(var6);
               var21.setColor(var3);
            } else {
               var21 = this.mediaActionDrawable;
               var3 = this.iconPressedColor;
               var21.setColor(var3);
            }

            var5 = this.circlePressedColorKey;
            if (var5 != null) {
               this.circlePaint.setColor(Theme.getColor(var5));
            } else {
               this.circlePaint.setColor(this.circlePressedColor);
            }
         } else {
            var6 = this.iconColorKey;
            if (var6 != null) {
               var21 = this.mediaActionDrawable;
               var3 = Theme.getColor(var6);
               var21.setColor(var3);
            } else {
               var21 = this.mediaActionDrawable;
               var3 = this.iconColor;
               var21.setColor(var3);
            }

            var5 = this.circleColorKey;
            if (var5 != null) {
               this.circlePaint.setColor(Theme.getColor(var5));
            } else {
               this.circlePaint.setColor(this.circleColor);
            }
         }

         if (this.drawMiniIcon && this.miniDrawCanvas != null) {
            this.miniDrawBitmap.eraseColor(0);
         }

         int var7 = this.circlePaint.getAlpha();
         this.circlePaint.setAlpha((int)((float)var7 * var4 * this.overrideAlpha));
         var7 = this.circleMiniPaint.getAlpha();
         this.circleMiniPaint.setAlpha((int)((float)var7 * var4 * this.overrideAlpha));
         int var8;
         if (this.drawMiniIcon && this.miniDrawCanvas != null) {
            var7 = (int)(this.progressRect.width() / 2.0F);
            var8 = (int)(this.progressRect.height() / 2.0F);
         } else {
            var7 = (int)this.progressRect.centerX();
            var8 = (int)this.progressRect.centerY();
         }

         boolean var9 = this.overlayImageView.hasBitmapImage();
         byte var10 = 2;
         float var11;
         boolean var20;
         if (var9) {
            var11 = this.overlayImageView.getCurrentAlpha();
            this.overlayPaint.setAlpha((int)(100.0F * var11 * var4 * this.overrideAlpha));
            int var12;
            if (var11 >= 1.0F) {
               var12 = -1;
               var20 = false;
            } else {
               int var13 = Color.red(var3);
               var12 = Color.green(var3);
               int var14 = Color.blue(var3);
               int var15 = Color.alpha(var3);
               int var16 = (int)((float)(255 - var13) * var11);
               int var17 = (int)((float)(255 - var12) * var11);
               var3 = (int)((float)(255 - var14) * var11);
               var12 = Color.argb(var15 + (int)((float)(255 - var15) * var11), var13 + var16, var12 + var17, var14 + var3);
               var20 = true;
            }

            this.mediaActionDrawable.setColor(var12);
            ImageReceiver var24 = this.overlayImageView;
            var12 = this.circleRadius;
            var24.setImageCoords(var7 - var12, var8 - var12, var12 * 2, var12 * 2);
         } else {
            var20 = true;
         }

         Canvas var25;
         if (var20 && this.drawBackground) {
            label183: {
               if (this.drawMiniIcon) {
                  var25 = this.miniDrawCanvas;
                  if (var25 != null) {
                     var25.drawCircle((float)var7, (float)var8, (float)this.circleRadius, this.circlePaint);
                     break label183;
                  }
               }

               if (var2 != 4 || var4 != 0.0F) {
                  if (this.backgroundStroke != 0) {
                     var1.drawCircle((float)var7, (float)var8, (float)(this.circleRadius - AndroidUtilities.dp(3.5F)), this.circlePaint);
                  } else {
                     var1.drawCircle((float)var7, (float)var8, (float)this.circleRadius * var4, this.circlePaint);
                  }
               }
            }
         }

         if (this.overlayImageView.hasBitmapImage()) {
            label141: {
               this.overlayImageView.setAlpha(var4 * this.overrideAlpha);
               if (this.drawMiniIcon) {
                  var25 = this.miniDrawCanvas;
                  if (var25 != null) {
                     this.overlayImageView.draw(var25);
                     this.miniDrawCanvas.drawCircle((float)var7, (float)var8, (float)this.circleRadius, this.overlayPaint);
                     break label141;
                  }
               }

               this.overlayImageView.draw(var1);
               var1.drawCircle((float)var7, (float)var8, (float)this.circleRadius, this.overlayPaint);
            }
         }

         var21 = this.mediaActionDrawable;
         var3 = this.circleRadius;
         var21.setBounds(var7 - var3, var8 - var3, var7 + var3, var8 + var3);
         if (this.drawMiniIcon) {
            var25 = this.miniDrawCanvas;
            if (var25 != null) {
               this.mediaActionDrawable.draw(var25);
            } else {
               this.mediaActionDrawable.draw(var1);
            }
         } else {
            this.mediaActionDrawable.setOverrideAlpha(this.overrideAlpha);
            this.mediaActionDrawable.draw(var1);
         }

         if (this.drawMiniIcon) {
            byte var23;
            byte var27;
            if (Math.abs(this.progressRect.width() - (float)AndroidUtilities.dp(44.0F)) < AndroidUtilities.density) {
               var23 = 20;
               var4 = this.progressRect.centerX();
               var11 = (float)16;
               var4 += (float)AndroidUtilities.dp(var11);
               var11 = this.progressRect.centerY() + (float)AndroidUtilities.dp(var11);
               var27 = 0;
            } else {
               var23 = 22;
               var4 = this.progressRect.centerX() + (float)AndroidUtilities.dp(18.0F);
               var11 = this.progressRect.centerY() + (float)AndroidUtilities.dp(18.0F);
               var27 = var10;
            }

            var8 = var23 / 2;
            float var18;
            if (this.miniMediaActionDrawable.getCurrentIcon() != 4) {
               var18 = 1.0F;
            } else {
               var18 = 1.0F - this.miniMediaActionDrawable.getTransitionProgress();
            }

            if (var18 == 0.0F) {
               this.drawMiniIcon = false;
            }

            var25 = this.miniDrawCanvas;
            float var19;
            if (var25 != null) {
               var19 = (float)(var23 + 18 + var27);
               var25.drawCircle((float)AndroidUtilities.dp(var19), (float)AndroidUtilities.dp(var19), (float)AndroidUtilities.dp((float)(var8 + 1)) * var18, Theme.checkboxSquare_eraserPaint);
            } else {
               this.miniProgressBackgroundPaint.setColor(this.progressColor);
               var1.drawCircle(var4, var11, (float)AndroidUtilities.dp(12.0F), this.miniProgressBackgroundPaint);
            }

            if (this.miniDrawCanvas != null) {
               Bitmap var22 = this.miniDrawBitmap;
               RectF var26 = this.progressRect;
               var1.drawBitmap(var22, (float)((int)var26.left), (float)((int)var26.top), (Paint)null);
            }

            var19 = (float)var8;
            var1.drawCircle(var4, var11, (float)AndroidUtilities.dp(var19) * var18, this.circleMiniPaint);
            this.miniMediaActionDrawable.setBounds((int)(var4 - (float)AndroidUtilities.dp(var19) * var18), (int)(var11 - (float)AndroidUtilities.dp(var19) * var18), (int)(var4 + (float)AndroidUtilities.dp(var19) * var18), (int)(var11 + (float)AndroidUtilities.dp(var19) * var18));
            this.miniMediaActionDrawable.draw(var1);
         }

      }
   }

   public int getIcon() {
      return this.mediaActionDrawable.getCurrentIcon();
   }

   public int getMiniIcon() {
      return this.miniMediaActionDrawable.getCurrentIcon();
   }

   public RectF getProgressRect() {
      return this.progressRect;
   }

   public void initMiniIcons() {
      if (this.miniDrawBitmap == null) {
         try {
            this.miniDrawBitmap = Bitmap.createBitmap(AndroidUtilities.dp(48.0F), AndroidUtilities.dp(48.0F), Config.ARGB_8888);
            Canvas var1 = new Canvas(this.miniDrawBitmap);
            this.miniDrawCanvas = var1;
         } catch (Throwable var2) {
         }
      }

   }

   public void onAttachedToWindow() {
      this.overlayImageView.onAttachedToWindow();
   }

   public void onDetachedFromWindow() {
      this.overlayImageView.onDetachedFromWindow();
   }

   public void setBackgroundStroke(int var1) {
      this.backgroundStroke = var1;
      this.circlePaint.setStrokeWidth((float)var1);
      this.circlePaint.setStyle(Style.STROKE);
      this.invalidateParent();
   }

   public void setCircleRadius(int var1) {
      this.circleRadius = var1;
      this.overlayImageView.setRoundRadius(this.circleRadius);
   }

   public void setColors(int var1, int var2, int var3, int var4) {
      this.circleColor = var1;
      this.circlePressedColor = var2;
      this.iconColor = var3;
      this.iconPressedColor = var4;
      this.circleColorKey = null;
      this.circlePressedColorKey = null;
      this.iconColorKey = null;
      this.iconPressedColorKey = null;
   }

   public void setColors(String var1, String var2, String var3, String var4) {
      this.circleColorKey = var1;
      this.circlePressedColorKey = var2;
      this.iconColorKey = var3;
      this.iconPressedColorKey = var4;
   }

   public void setDrawBackground(boolean var1) {
      this.drawBackground = var1;
   }

   public void setIcon(int var1, boolean var2, boolean var3) {
      if (!var2 || var1 != this.mediaActionDrawable.getCurrentIcon()) {
         this.mediaActionDrawable.setIcon(var1, var3);
         if (!var3) {
            this.parent.invalidate();
         } else {
            this.invalidateParent();
         }

      }
   }

   public void setImageOverlay(String var1) {
      ImageReceiver var2 = this.overlayImageView;
      String var3;
      if (var1 != null) {
         var3 = String.format(Locale.US, "%d_%d", this.circleRadius * 2, this.circleRadius * 2);
      } else {
         var3 = null;
      }

      var2.setImage(var1, var3, (Drawable)null, (String)null, -1);
   }

   public void setImageOverlay(TLRPC.PhotoSize var1, TLRPC.Document var2, Object var3) {
      this.overlayImageView.setImage(ImageLocation.getForDocument(var1, var2), String.format(Locale.US, "%d_%d", this.circleRadius * 2, this.circleRadius * 2), (Drawable)null, (String)null, var3, 1);
   }

   public void setMiniIcon(int var1, boolean var2, boolean var3) {
      if (var1 == 2 || var1 == 3 || var1 == 4) {
         if (!var2 || var1 != this.miniMediaActionDrawable.getCurrentIcon()) {
            this.miniMediaActionDrawable.setIcon(var1, var3);
            if (var1 == 4 && this.miniMediaActionDrawable.getTransitionProgress() >= 1.0F) {
               var2 = false;
            } else {
               var2 = true;
            }

            this.drawMiniIcon = var2;
            if (this.drawMiniIcon) {
               this.initMiniIcons();
            }

            if (!var3) {
               this.parent.invalidate();
            } else {
               this.invalidateParent();
            }

         }
      }
   }

   public void setMiniProgressBackgroundColor(int var1) {
      this.miniProgressBackgroundPaint.setColor(var1);
   }

   public void setOverrideAlpha(float var1) {
      this.overrideAlpha = var1;
   }

   public void setPressed(boolean var1, boolean var2) {
      if (var2) {
         this.isPressedMini = var1;
      } else {
         this.isPressed = var1;
      }

      this.invalidateParent();
   }

   public void setProgress(float var1, boolean var2) {
      if (this.drawMiniIcon) {
         this.miniMediaActionDrawable.setProgress(var1, var2);
      } else {
         this.mediaActionDrawable.setProgress(var1, var2);
      }

   }

   public void setProgressColor(int var1) {
      this.progressColor = var1;
   }

   public void setProgressRect(int var1, int var2, int var3, int var4) {
      this.progressRect.set((float)var1, (float)var2, (float)var3, (float)var4);
   }

   public boolean swapIcon(int var1) {
      return this.mediaActionDrawable.setIcon(var1, false);
   }
}
