package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class MediaActionDrawable extends Drawable {
   private static final float CANCEL_TO_CHECK_STAGE1 = 0.5F;
   private static final float CANCEL_TO_CHECK_STAGE2 = 0.5F;
   private static final float DOWNLOAD_TO_CANCEL_STAGE1 = 0.5F;
   private static final float DOWNLOAD_TO_CANCEL_STAGE2 = 0.2F;
   private static final float DOWNLOAD_TO_CANCEL_STAGE3 = 0.3F;
   private static final float EPS = 0.001F;
   public static final int ICON_CANCEL = 3;
   public static final int ICON_CANCEL_FILL = 14;
   public static final int ICON_CANCEL_NOPROFRESS = 12;
   public static final int ICON_CANCEL_PERCENT = 13;
   public static final int ICON_CHECK = 6;
   public static final int ICON_DOWNLOAD = 2;
   public static final int ICON_EMPTY = 10;
   public static final int ICON_EMPTY_NOPROGRESS = 11;
   public static final int ICON_FILE = 5;
   public static final int ICON_FIRE = 7;
   public static final int ICON_GIF = 8;
   public static final int ICON_NONE = 4;
   public static final int ICON_PAUSE = 1;
   public static final int ICON_PLAY = 0;
   public static final int ICON_SECRETCHECK = 9;
   private static final float[] pausePath1 = new float[]{16.0F, 17.0F, 32.0F, 17.0F, 32.0F, 22.0F, 16.0F, 22.0F, 16.0F, 19.5F};
   private static final float[] pausePath2 = new float[]{16.0F, 31.0F, 32.0F, 31.0F, 32.0F, 26.0F, 16.0F, 26.0F, 16.0F, 28.5F};
   private static final int pauseRotation = 90;
   private static final float[] playFinalPath = new float[]{18.0F, 15.0F, 34.0F, 24.0F, 18.0F, 33.0F};
   private static final float[] playPath1 = new float[]{18.0F, 15.0F, 34.0F, 24.0F, 34.0F, 24.0F, 18.0F, 24.0F, 18.0F, 24.0F};
   private static final float[] playPath2 = new float[]{18.0F, 33.0F, 34.0F, 24.0F, 34.0F, 24.0F, 18.0F, 24.0F, 18.0F, 24.0F};
   private static final int playRotation = 0;
   private float animatedDownloadProgress;
   private boolean animatingTransition;
   private ColorFilter colorFilter;
   private int currentIcon;
   private MediaActionDrawable.MediaActionDrawableDelegate delegate;
   private float downloadProgress;
   private float downloadProgressAnimationStart;
   private float downloadProgressTime;
   private float downloadRadOffset;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator();
   private boolean isMini;
   private long lastAnimationTime;
   private int lastPercent = -1;
   private int nextIcon;
   private float overrideAlpha = 1.0F;
   private Paint paint = new Paint(1);
   private Paint paint2 = new Paint(1);
   private Paint paint3 = new Paint(1);
   private Path path1 = new Path();
   private Path path2 = new Path();
   private String percentString;
   private int percentStringWidth;
   private RectF rect = new RectF();
   private float savedTransitionProgress;
   private float scale = 1.0F;
   private TextPaint textPaint = new TextPaint(1);
   private float transitionAnimationTime = 400.0F;
   private float transitionProgress = 1.0F;

   public MediaActionDrawable() {
      this.paint.setColor(-1);
      this.paint.setStrokeCap(Cap.ROUND);
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
      this.paint.setStyle(Style.STROKE);
      this.paint3.setColor(-1);
      this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
      this.textPaint.setColor(-1);
      this.paint2.setColor(-1);
      this.paint2.setPathEffect(new CornerPathEffect((float)AndroidUtilities.dp(2.0F)));
   }

   private float getCircleValue(float var1) {
      while(var1 > 360.0F) {
         var1 -= 360.0F;
      }

      return var1;
   }

   public void draw(Canvas var1) {
      android.graphics.Rect var2 = this.getBounds();
      int var3 = var2.centerX();
      int var4 = var3;
      int var5 = var2.centerY();
      int var6 = this.nextIcon;
      float var7;
      if (var6 == 4) {
         var7 = 1.0F - this.transitionProgress;
         var1.save();
         var1.scale(var7, var7, (float)var3, (float)var5);
      } else if ((var6 == 6 || var6 == 10) && this.currentIcon == 4) {
         var1.save();
         var7 = this.transitionProgress;
         var1.scale(var7, var7, (float)var3, (float)var5);
      }

      AndroidUtilities.dp(3.0F);
      float var8;
      float var9;
      float var10;
      float var11;
      float var12;
      float var14;
      RectF var15;
      int var17;
      int var18;
      if (this.currentIcon == 2 || this.nextIcon == 2) {
         var8 = (float)var5;
         var9 = var8 - (float)AndroidUtilities.dp(9.0F) * this.scale;
         var10 = (float)AndroidUtilities.dp(9.0F) * this.scale + var8;
         var11 = (float)AndroidUtilities.dp(12.0F) * this.scale + var8;
         var6 = this.currentIcon;
         if ((var6 == 3 || var6 == 14) && this.nextIcon == 2) {
            this.paint.setAlpha((int)(Math.min(1.0F, this.transitionProgress / 0.5F) * 255.0F));
            var7 = this.transitionProgress;
            var12 = (float)AndroidUtilities.dp(12.0F) * this.scale + var8;
         } else {
            var6 = this.nextIcon;
            if (var6 != 3 && var6 != 14 && var6 != 2) {
               this.paint.setAlpha((int)(Math.min(1.0F, this.savedTransitionProgress / 0.5F) * 255.0F * (1.0F - this.transitionProgress)));
               var7 = this.savedTransitionProgress;
            } else {
               this.paint.setAlpha(255);
               var7 = this.transitionProgress;
            }

            var12 = (float)AndroidUtilities.dp(1.0F) * this.scale + var8;
         }

         float var13;
         float var16;
         if (!this.animatingTransition) {
            var13 = (float)var3;
            var12 = (float)AndroidUtilities.dp(8.0F);
            var11 = this.scale;
            var8 = (float)AndroidUtilities.dp(8.0F);
            var16 = this.scale;
            var14 = (float)AndroidUtilities.dp(8.0F);
            var7 = this.scale;
            var8 = var13 + var8 * var16;
            var12 = var13 - var12 * var11;
            var14 = var10 - var14 * var7;
            var11 = var9;
            var7 = var10;
         } else {
            if (this.nextIcon != 2 && var7 > 0.5F) {
               var9 = (float)AndroidUtilities.dp(13.0F) * this.scale;
               var7 -= 0.5F;
               var13 = var7 / 0.5F;
               if (var7 > 0.2F) {
                  var14 = (var7 - 0.2F) / 0.3F;
                  var7 = 1.0F;
               } else {
                  var7 /= 0.2F;
                  var14 = 0.0F;
               }

               var15 = this.rect;
               var10 = (float)var3;
               var16 = var9 / 2.0F;
               var15.set(var10 - var9, var11 - var16, var10, var16 + var11);
               var9 = var14 * 100.0F;
               var1.drawArc(this.rect, var9, var13 * 104.0F - var9, false, this.paint);
               var9 = var12 + (var11 - var12) * var7;
               if (var14 > 0.0F) {
                  if (this.nextIcon == 14) {
                     var7 = 0.0F;
                  } else {
                     var7 = (1.0F - var14) * -45.0F;
                  }

                  var12 = (float)AndroidUtilities.dp(7.0F) * var14 * this.scale;
                  var17 = (int)(var14 * 255.0F);
                  var18 = this.nextIcon;
                  var6 = var17;
                  if (var18 != 3) {
                     var6 = var17;
                     if (var18 != 14) {
                        var6 = var17;
                        if (var18 != 2) {
                           var14 = Math.min(1.0F, this.transitionProgress / 0.5F);
                           var6 = (int)((float)var17 * (1.0F - var14));
                        }
                     }
                  }

                  if (var7 != 0.0F) {
                     var1.save();
                     var1.rotate(var7, var10, var8);
                  }

                  if (var6 != 0) {
                     this.paint.setAlpha(var6);
                     if (this.nextIcon == 14) {
                        this.paint3.setAlpha(var6);
                        this.rect.set((float)(var3 - AndroidUtilities.dp(3.5F)), (float)(var5 - AndroidUtilities.dp(3.5F)), (float)(var3 + AndroidUtilities.dp(3.5F)), (float)(var5 + AndroidUtilities.dp(3.5F)));
                        var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), this.paint3);
                        this.paint.setAlpha((int)((float)var6 * 0.15F));
                        if (this.isMini) {
                           var12 = 2.0F;
                        } else {
                           var12 = 4.0F;
                        }

                        var17 = AndroidUtilities.dp(var12);
                        this.rect.set((float)(var2.left + var17), (float)(var2.top + var17), (float)(var2.right - var17), (float)(var2.bottom - var17));
                        var1.drawArc(this.rect, 0.0F, 360.0F, false, this.paint);
                        this.paint.setAlpha(var6);
                     } else {
                        var13 = var10 - var12;
                        var16 = var8 - var12;
                        var14 = var10 + var12;
                        var12 += var8;
                        var1.drawLine(var13, var16, var14, var12, this.paint);
                        var1.drawLine(var14, var16, var13, var12, this.paint);
                     }
                  }

                  if (var7 != 0.0F) {
                     var1.restore();
                  }
               }

               var12 = var11;
               var8 = var10;
               var14 = var10;
               var7 = var9;
            } else {
               if (this.nextIcon == 2) {
                  var14 = 1.0F - var7;
               } else {
                  var14 = var7 / 0.5F;
                  var7 = 1.0F - var14;
               }

               var12 = (var12 - var9) * var14 + var9;
               var11 = (var11 - var10) * var14 + var10;
               var8 = (float)var3;
               var14 = var8 - (float)AndroidUtilities.dp(8.0F) * var7 * this.scale;
               var8 += (float)AndroidUtilities.dp(8.0F) * var7 * this.scale;
               var10 = var11 - (float)AndroidUtilities.dp(8.0F) * var7 * this.scale;
               var7 = var12;
               var12 = var10;
            }

            var10 = var11;
            var9 = var12;
            var11 = var7;
            var7 = var10;
            var12 = var14;
            var14 = var9;
         }

         if (var11 != var7) {
            var10 = (float)var3;
            var1.drawLine(var10, var11, var10, var7, this.paint);
         }

         var11 = (float)var3;
         if (var12 != var11) {
            var1.drawLine(var12, var14, var11, var7, this.paint);
            var1.drawLine(var8, var14, var11, var7, this.paint);
         }
      }

      Paint var35;
      label682: {
         var6 = this.currentIcon;
         if (var6 != 3 && var6 != 14) {
            label695: {
               if (var6 == 4) {
                  var6 = this.nextIcon;
                  if (var6 == 14 || var6 == 3) {
                     break label695;
                  }
               }

               var6 = this.currentIcon;
               if (var6 != 10 && this.nextIcon != 10 && var6 != 13) {
                  break label682;
               }

               var6 = this.nextIcon;
               if (var6 != 4 && var6 != 6) {
                  var6 = 255;
               } else {
                  var6 = (int)((1.0F - this.transitionProgress) * 255.0F);
               }

               if (var6 != 0) {
                  this.paint.setAlpha((int)((float)var6 * this.overrideAlpha));
                  var12 = Math.max(4.0F, this.animatedDownloadProgress * 360.0F);
                  if (this.isMini) {
                     var7 = 2.0F;
                  } else {
                     var7 = 4.0F;
                  }

                  var6 = AndroidUtilities.dp(var7);
                  this.rect.set((float)(var2.left + var6), (float)(var2.top + var6), (float)(var2.right - var6), (float)(var2.bottom - var6));
                  var1.drawArc(this.rect, this.downloadRadOffset, var12, false, this.paint);
               }
               break label682;
            }
         }

         label707: {
            label595: {
               var6 = this.nextIcon;
               if (var6 == 2) {
                  var7 = this.transitionProgress;
                  if (var7 <= 0.5F) {
                     var12 = 1.0F - var7 / 0.5F;
                     var7 = (float)AndroidUtilities.dp(7.0F) * var12 * this.scale;
                     var6 = (int)(var12 * 255.0F);
                  } else {
                     var7 = 0.0F;
                     var6 = 0;
                  }

                  var14 = var7;
               } else {
                  if (var6 == 0 || var6 == 1 || var6 == 5 || var6 == 8 || var6 == 9 || var6 == 7 || var6 == 6) {
                     if (this.nextIcon == 6) {
                        var7 = Math.min(1.0F, this.transitionProgress / 0.5F);
                     } else {
                        var7 = this.transitionProgress;
                     }

                     var12 = 1.0F - var7;
                     var11 = (float)AndroidUtilities.dp(7.0F);
                     var14 = this.scale;
                     var6 = (int)(Math.min(1.0F, var12 * 2.0F) * 255.0F);
                     var14 = var11 * var12 * var14;
                     var8 = 0.0F;
                     var10 = 1.0F;
                     var12 = var7 * 45.0F;
                     var11 = 0.0F;
                     var7 = var10;
                     break label707;
                  }

                  if (var6 == 4) {
                     var7 = this.transitionProgress;
                     var12 = 1.0F - var7;
                     var14 = (float)AndroidUtilities.dp(7.0F);
                     var11 = this.scale;
                     var6 = (int)(var12 * 255.0F);
                     if (this.currentIcon == 14) {
                        var7 = 0.0F;
                     } else {
                        var7 *= 45.0F;
                        var12 = 1.0F;
                     }

                     var8 = var7;
                     var14 *= var11;
                     var11 = 0.0F;
                     var7 = 0.0F;
                     break label595;
                  }

                  if (var6 == 14 || var6 == 3) {
                     var14 = this.transitionProgress;
                     if (this.currentIcon == 4) {
                        var7 = var14;
                        var12 = 0.0F;
                     } else {
                        var12 = (1.0F - var14) * 45.0F;
                        var7 = 1.0F;
                     }

                     var10 = (float)AndroidUtilities.dp(7.0F);
                     var8 = this.scale;
                     var17 = (int)(var14 * 255.0F);
                     if (this.nextIcon == 14) {
                        var11 = (float)var2.left;
                        var6 = var2.top;
                     } else {
                        var11 = (float)var2.centerX();
                        var6 = var2.centerY();
                     }

                     var14 = (float)var6;
                     var10 *= var8;
                     var8 = var12;
                     var12 = var7;
                     var7 = var14;
                     var6 = var17;
                     var14 = var10;
                     break label595;
                  }

                  var7 = (float)AndroidUtilities.dp(7.0F);
                  var14 = this.scale * var7;
                  var6 = 255;
               }

               var11 = 0.0F;
               var12 = 0.0F;
               var8 = 0.0F;
               var7 = 1.0F;
               break label707;
            }

            var10 = var12;
            var12 = var8;
            var8 = var7;
            var7 = var10;
         }

         if (var7 != 1.0F) {
            var1.save();
            var1.scale(var7, var7, var11, var8);
         }

         if (var12 != 0.0F) {
            var1.save();
            var1.rotate(var12, (float)var3, (float)var5);
         }

         if (var6 != 0) {
            var35 = this.paint;
            var11 = (float)var6;
            var35.setAlpha((int)(this.overrideAlpha * var11));
            if (this.currentIcon != 14 && this.nextIcon != 14) {
               var9 = (float)var3;
               var11 = var9 - var14;
               var10 = (float)var5;
               var8 = var10 - var14;
               var9 += var14;
               var14 += var10;
               var1.drawLine(var11, var8, var9, var14, this.paint);
               var1.drawLine(var9, var8, var11, var14, this.paint);
            } else {
               this.paint3.setAlpha((int)(var11 * this.overrideAlpha));
               this.rect.set((float)(var3 - AndroidUtilities.dp(3.5F)), (float)(var5 - AndroidUtilities.dp(3.5F)), (float)(AndroidUtilities.dp(3.5F) + var3), (float)(AndroidUtilities.dp(3.5F) + var5));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), this.paint3);
            }
         }

         if (var12 != 0.0F) {
            var1.restore();
         }

         label663: {
            var17 = this.currentIcon;
            if (var17 != 3 && var17 != 14) {
               if (var17 != 4) {
                  break label663;
               }

               var17 = this.nextIcon;
               if (var17 != 14 && var17 != 3) {
                  break label663;
               }
            }

            if (var6 != 0) {
               var14 = Math.max(4.0F, this.animatedDownloadProgress * 360.0F);
               if (this.isMini) {
                  var12 = 2.0F;
               } else {
                  var12 = 4.0F;
               }

               label554: {
                  var17 = AndroidUtilities.dp(var12);
                  this.rect.set((float)(var2.left + var17), (float)(var2.top + var17), (float)(var2.right - var17), (float)(var2.bottom - var17));
                  var17 = this.currentIcon;
                  if (var17 != 14) {
                     if (var17 != 4) {
                        break label554;
                     }

                     var17 = this.nextIcon;
                     if (var17 != 14 && var17 != 3) {
                        break label554;
                     }
                  }

                  this.paint.setAlpha((int)((float)var6 * 0.15F * this.overrideAlpha));
                  var1.drawArc(this.rect, 0.0F, 360.0F, false, this.paint);
                  this.paint.setAlpha(var6);
               }

               var1.drawArc(this.rect, this.downloadRadOffset, var14, false, this.paint);
            }
         }

         if (var7 != 1.0F) {
            var1.restore();
         }
      }

      var15 = null;
      Drawable var19 = null;
      var6 = this.currentIcon;
      if (var6 == this.nextIcon) {
         var7 = 1.0F;
         var14 = 1.0F;
      } else {
         if (var6 == 4) {
            var12 = this.transitionProgress;
            var7 = 1.0F - var12;
         } else {
            var12 = Math.min(1.0F, this.transitionProgress / 0.5F);
            var7 = Math.max(0.0F, 1.0F - this.transitionProgress / 0.5F);
         }

         var14 = var7;
         var7 = var12;
      }

      Drawable var20;
      if (this.nextIcon == 5) {
         var20 = Theme.chat_fileIcon;
      } else {
         var20 = var15;
         if (this.currentIcon == 5) {
            var19 = Theme.chat_fileIcon;
            var20 = var15;
         }
      }

      Drawable var36;
      if (this.nextIcon == 7) {
         var36 = Theme.chat_flameIcon;
      } else {
         var36 = var20;
         if (this.currentIcon == 7) {
            var19 = Theme.chat_flameIcon;
            var36 = var20;
         }
      }

      Object var21;
      Object var42;
      if (this.nextIcon == 8) {
         var21 = Theme.chat_gifIcon;
         var42 = var19;
      } else {
         var21 = var36;
         var42 = var19;
         if (this.currentIcon == 8) {
            var42 = Theme.chat_gifIcon;
            var21 = var36;
         }
      }

      if (this.currentIcon == 9 || this.nextIcon == 9) {
         var35 = this.paint;
         if (this.currentIcon == this.nextIcon) {
            var6 = 255;
         } else {
            var6 = (int)(this.transitionProgress * 255.0F);
         }

         var35.setAlpha(var6);
         var17 = var5 + AndroidUtilities.dp(7.0F);
         var6 = var3 - AndroidUtilities.dp(3.0F);
         if (this.currentIcon != this.nextIcon) {
            var1.save();
            var12 = this.transitionProgress;
            var1.scale(var12, var12, (float)var3, (float)var5);
         }

         var10 = (float)(var6 - AndroidUtilities.dp(6.0F));
         var12 = (float)(var17 - AndroidUtilities.dp(6.0F));
         var8 = (float)var6;
         var11 = (float)var17;
         var1.drawLine(var10, var12, var8, var11, this.paint);
         var1.drawLine(var8, var11, (float)(var6 + AndroidUtilities.dp(12.0F)), (float)(var17 - AndroidUtilities.dp(12.0F)), this.paint);
         if (this.currentIcon != this.nextIcon) {
            var1.restore();
         }
      }

      if (this.currentIcon == 12 || this.nextIcon == 12) {
         var17 = this.currentIcon;
         var6 = this.nextIcon;
         if (var17 == var6) {
            var12 = 1.0F;
         } else if (var6 == 13) {
            var12 = this.transitionProgress;
         } else {
            var12 = 1.0F - this.transitionProgress;
         }

         var35 = this.paint;
         if (this.currentIcon == this.nextIcon) {
            var6 = 255;
         } else {
            var6 = (int)(var12 * 255.0F);
         }

         var35.setAlpha(var6);
         AndroidUtilities.dp(7.0F);
         AndroidUtilities.dp(3.0F);
         if (this.currentIcon != this.nextIcon) {
            var1.save();
            var1.scale(var12, var12, (float)var3, (float)var5);
         }

         var8 = (float)AndroidUtilities.dp(7.0F) * this.scale;
         var9 = (float)var3;
         var11 = var9 - var8;
         var10 = (float)var5;
         var12 = var10 - var8;
         var9 += var8;
         var8 += var10;
         var1.drawLine(var11, var12, var9, var8, this.paint);
         var1.drawLine(var9, var12, var11, var8, this.paint);
         if (this.currentIcon != this.nextIcon) {
            var1.restore();
         }
      }

      if (this.currentIcon == 13 || this.nextIcon == 13) {
         var6 = this.currentIcon;
         var17 = this.nextIcon;
         if (var6 == var17) {
            var12 = 1.0F;
         } else if (var17 == 13) {
            var12 = this.transitionProgress;
         } else {
            var12 = 1.0F - this.transitionProgress;
         }

         this.textPaint.setAlpha((int)(var12 * 255.0F));
         var17 = AndroidUtilities.dp(5.0F);
         var18 = this.percentStringWidth / 2;
         if (this.currentIcon != this.nextIcon) {
            var1.save();
            var1.scale(var12, var12, (float)var3, (float)var5);
         }

         var6 = (int)(this.animatedDownloadProgress * 100.0F);
         if (this.percentString == null || var6 != this.lastPercent) {
            this.lastPercent = var6;
            this.percentString = String.format("%d%%", this.lastPercent);
            this.percentStringWidth = (int)Math.ceil((double)this.textPaint.measureText(this.percentString));
         }

         var1.drawText(this.percentString, (float)(var3 - var18), (float)(var17 + var5), this.textPaint);
         if (this.currentIcon != this.nextIcon) {
            var1.restore();
         }
      }

      label705: {
         var6 = this.currentIcon;
         if (var6 != 0 && var6 != 1) {
            var6 = this.nextIcon;
            if (var6 != 0 && var6 != 1) {
               var6 = var3;
               var11 = var7;
               break label705;
            }
         }

         if ((this.currentIcon == 0 && this.nextIcon == 1 || this.currentIcon == 1 && this.nextIcon == 0) && this.animatingTransition) {
            var12 = this.interpolator.getInterpolation(this.transitionProgress);
         } else {
            var12 = 0.0F;
         }

         this.path1.reset();
         this.path2.reset();
         float[] var24 = null;
         var6 = this.currentIcon;
         Object var37;
         byte var40;
         float[] var43;
         if (var6 != 0) {
            if (var6 != 1) {
               var43 = null;
               var37 = null;
               var40 = 0;
            } else {
               var43 = pausePath1;
               var37 = pausePath2;
               var40 = 90;
            }
         } else {
            var43 = playPath1;
            var37 = playPath2;
            var24 = playFinalPath;
            var40 = 0;
         }

         byte var38;
         float[] var39;
         label490: {
            var6 = this.nextIcon;
            if (var6 != 0) {
               if (var6 == 1) {
                  var39 = pausePath1;
                  var21 = pausePath2;
                  var38 = 90;
                  break label490;
               }

               var39 = null;
               var21 = null;
            } else {
               var39 = playPath1;
               var21 = playPath2;
            }

            var38 = 0;
         }

         if (var43 == null) {
            var42 = null;
            Object var25 = null;
            var37 = var21;
            var21 = var25;
         } else {
            var42 = var39;
            var39 = var43;
         }

         byte var34;
         label698: {
            int var26;
            if (!this.animatingTransition && var24 != null) {
               for(var4 = 0; var4 < var24.length / 2; ++var4) {
                  Path var41;
                  if (var4 == 0) {
                     var41 = this.path1;
                     var18 = var4 * 2;
                     var11 = (float)AndroidUtilities.dp(var24[var18]);
                     var8 = this.scale;
                     var26 = var18 + 1;
                     var41.moveTo(var11 * var8, (float)AndroidUtilities.dp(var24[var26]) * this.scale);
                     this.path2.moveTo((float)AndroidUtilities.dp(var24[var18]) * this.scale, (float)AndroidUtilities.dp(var24[var26]) * this.scale);
                  } else {
                     var41 = this.path1;
                     var18 = var4 * 2;
                     var11 = (float)AndroidUtilities.dp(var24[var18]);
                     var8 = this.scale;
                     var26 = var18 + 1;
                     var41.lineTo(var11 * var8, (float)AndroidUtilities.dp(var24[var26]) * this.scale);
                     this.path2.lineTo((float)AndroidUtilities.dp(var24[var18]) * this.scale, (float)AndroidUtilities.dp(var24[var26]) * this.scale);
                  }
               }
            } else {
               if (var42 != null) {
                  for(var3 = 0; var3 < 5; ++var3) {
                     Path var44;
                     if (var3 == 0) {
                        var44 = this.path1;
                        var18 = var3 * 2;
                        var11 = (float)AndroidUtilities.dp(var39[var18] + (((Object[])var42)[var18] - var39[var18]) * var12);
                        var8 = this.scale;
                        var26 = var18 + 1;
                        var44.moveTo(var11 * var8, (float)AndroidUtilities.dp(var39[var26] + (((Object[])var42)[var26] - var39[var26]) * var12) * this.scale);
                        this.path2.moveTo((float)AndroidUtilities.dp(((Object[])var37)[var18] + (((Object[])var21)[var18] - ((Object[])var37)[var18]) * var12) * this.scale, (float)AndroidUtilities.dp(((Object[])var37)[var26] + (((Object[])var21)[var26] - ((Object[])var37)[var26]) * var12) * this.scale);
                     } else {
                        var44 = this.path1;
                        var18 = var3 * 2;
                        var8 = (float)AndroidUtilities.dp(var39[var18] + (((Object[])var42)[var18] - var39[var18]) * var12);
                        var11 = this.scale;
                        var26 = var18 + 1;
                        var44.lineTo(var8 * var11, (float)AndroidUtilities.dp(var39[var26] + (((Object[])var42)[var26] - var39[var26]) * var12) * this.scale);
                        this.path2.lineTo((float)AndroidUtilities.dp(((Object[])var37)[var18] + (((Object[])var21)[var18] - ((Object[])var37)[var18]) * var12) * this.scale, (float)AndroidUtilities.dp(((Object[])var37)[var26] + (((Object[])var21)[var26] - ((Object[])var37)[var26]) * var12) * this.scale);
                     }
                  }

                  this.paint2.setAlpha(255);
                  var6 = var4;
                  var34 = var38;
                  break label698;
               }

               for(var4 = 0; var4 < 5; ++var4) {
                  if (var4 == 0) {
                     var42 = this.path1;
                     var26 = var4 * 2;
                     var8 = (float)AndroidUtilities.dp(var39[var26]);
                     var11 = this.scale;
                     var18 = var26 + 1;
                     ((Path)var42).moveTo(var8 * var11, (float)AndroidUtilities.dp(var39[var18]) * this.scale);
                     this.path2.moveTo((float)AndroidUtilities.dp((float)((Object[])var37)[var26]) * this.scale, (float)AndroidUtilities.dp((float)((Object[])var37)[var18]) * this.scale);
                  } else {
                     var42 = this.path1;
                     var26 = var4 * 2;
                     var8 = (float)AndroidUtilities.dp(var39[var26]);
                     var11 = this.scale;
                     var18 = var26 + 1;
                     ((Path)var42).lineTo(var8 * var11, (float)AndroidUtilities.dp(var39[var18]) * this.scale);
                     this.path2.lineTo((float)AndroidUtilities.dp((float)((Object[])var37)[var26]) * this.scale, (float)AndroidUtilities.dp((float)((Object[])var37)[var18]) * this.scale);
                  }
               }

               var4 = this.nextIcon;
               if (var4 == 4) {
                  this.paint2.setAlpha((int)((1.0F - this.transitionProgress) * 255.0F));
               } else {
                  var35 = this.paint2;
                  if (this.currentIcon == var4) {
                     var4 = 255;
                  } else {
                     var4 = (int)(this.transitionProgress * 255.0F);
                  }

                  var35.setAlpha(var4);
               }
            }

            var34 = var38;
            var6 = var3;
         }

         this.path1.close();
         this.path2.close();
         var1.save();
         var1.translate((float)var2.left, (float)var2.top);
         var1.rotate((float)var40 + (float)(var34 - var40) * var12, (float)(var6 - var2.left), (float)(var5 - var2.top));
         var4 = this.currentIcon;
         if (var4 != 0 && var4 != 1 || this.currentIcon == 4) {
            var8 = (float)(var6 - var2.left);
            var11 = (float)(var5 - var2.top);
            var1.scale(var7, var7, var8, var11);
         }

         var1.drawPath(this.path1, this.paint2);
         var1.drawPath(this.path2, this.paint2);
         var1.restore();
         var11 = var7;
      }

      if (this.currentIcon == 6 || this.nextIcon == 6) {
         if (this.currentIcon != 6) {
            var7 = this.transitionProgress;
            if (var7 > 0.5F) {
               var7 = (var7 - 0.5F) / 0.5F;
               var12 = 1.0F - Math.min(1.0F, var7 / 0.5F);
               if (var7 > 0.5F) {
                  var7 = (var7 - 0.5F) / 0.5F;
               } else {
                  var7 = 0.0F;
               }
            } else {
               var7 = 0.0F;
               var12 = 1.0F;
            }
         } else {
            var7 = 1.0F;
            var12 = 0.0F;
         }

         var17 = var5 + AndroidUtilities.dp(7.0F);
         var4 = var6 - AndroidUtilities.dp(3.0F);
         this.paint.setAlpha(255);
         if (var12 < 1.0F) {
            var1.drawLine((float)(var4 - AndroidUtilities.dp(6.0F)), (float)(var17 - AndroidUtilities.dp(6.0F)), (float)var4 - (float)AndroidUtilities.dp(6.0F) * var12, (float)var17 - (float)AndroidUtilities.dp(6.0F) * var12, this.paint);
         }

         if (var7 > 0.0F) {
            var12 = (float)var4;
            var8 = (float)var17;
            var1.drawLine(var12, var8, var12 + (float)AndroidUtilities.dp(12.0F) * var7, var8 - (float)AndroidUtilities.dp(12.0F) * var7, this.paint);
         }
      }

      if (var42 != null && var42 != var21) {
         var3 = (int)((float)((Drawable)var42).getIntrinsicWidth() * var14);
         var17 = (int)((float)((Drawable)var42).getIntrinsicHeight() * var14);
         ((Drawable)var42).setColorFilter(this.colorFilter);
         if (this.currentIcon == this.nextIcon) {
            var4 = 255;
         } else {
            var4 = (int)((1.0F - this.transitionProgress) * 255.0F);
         }

         ((Drawable)var42).setAlpha(var4);
         var4 = var3 / 2;
         var17 /= 2;
         ((Drawable)var42).setBounds(var6 - var4, var5 - var17, var6 + var4, var5 + var17);
         ((Drawable)var42).draw(var1);
      }

      if (var21 != null) {
         var3 = (int)((float)((Drawable)var21).getIntrinsicWidth() * var11);
         var17 = (int)((float)((Drawable)var21).getIntrinsicHeight() * var11);
         ((Drawable)var21).setColorFilter(this.colorFilter);
         if (this.currentIcon == this.nextIcon) {
            var4 = 255;
         } else {
            var4 = (int)(this.transitionProgress * 255.0F);
         }

         ((Drawable)var21).setAlpha(var4);
         var4 = var3 / 2;
         var17 /= 2;
         ((Drawable)var21).setBounds(var6 - var4, var5 - var17, var6 + var4, var5 + var17);
         ((Drawable)var21).draw(var1);
      }

      long var27 = System.currentTimeMillis();
      long var29 = var27 - this.lastAnimationTime;
      long var31 = var29;
      if (var29 > 17L) {
         var31 = 17L;
      }

      label675: {
         this.lastAnimationTime = var27;
         var5 = this.currentIcon;
         if (var5 != 3 && var5 != 14 && (var5 != 4 || this.nextIcon != 14)) {
            var5 = this.currentIcon;
            if (var5 != 10 && var5 != 13) {
               break label675;
            }
         }

         this.downloadRadOffset += (float)(360L * var31) / 2500.0F;
         this.downloadRadOffset = this.getCircleValue(this.downloadRadOffset);
         if (this.nextIcon != 2) {
            var12 = this.downloadProgress;
            var7 = this.downloadProgressAnimationStart;
            var11 = var12 - var7;
            if (var11 > 0.0F) {
               this.downloadProgressTime += (float)var31;
               var14 = this.downloadProgressTime;
               if (var14 >= 200.0F) {
                  this.animatedDownloadProgress = var12;
                  this.downloadProgressAnimationStart = var12;
                  this.downloadProgressTime = 0.0F;
               } else {
                  this.animatedDownloadProgress = var7 + var11 * this.interpolator.getInterpolation(var14 / 200.0F);
               }
            }
         }

         this.invalidateSelf();
      }

      if (this.animatingTransition) {
         var7 = this.transitionProgress;
         if (var7 < 1.0F) {
            this.transitionProgress = var7 + (float)var31 / this.transitionAnimationTime;
            if (this.transitionProgress >= 1.0F) {
               this.currentIcon = this.nextIcon;
               this.transitionProgress = 1.0F;
               this.animatingTransition = false;
            }

            this.invalidateSelf();
         }
      }

      var5 = this.nextIcon;
      if (var5 == 4 || (var5 == 6 || var5 == 10) && this.currentIcon == 4) {
         var1.restore();
      }

   }

   public int getColor() {
      return this.paint.getColor();
   }

   public int getCurrentIcon() {
      return this.nextIcon;
   }

   public int getIntrinsicHeight() {
      return AndroidUtilities.dp(48.0F);
   }

   public int getIntrinsicWidth() {
      return AndroidUtilities.dp(48.0F);
   }

   public int getMinimumHeight() {
      return AndroidUtilities.dp(48.0F);
   }

   public int getMinimumWidth() {
      return AndroidUtilities.dp(48.0F);
   }

   public int getOpacity() {
      return -2;
   }

   public int getPreviousIcon() {
      return this.currentIcon;
   }

   public float getProgressAlpha() {
      return 1.0F - this.transitionProgress;
   }

   public float getTransitionProgress() {
      float var1;
      if (this.animatingTransition) {
         var1 = this.transitionProgress;
      } else {
         var1 = 1.0F;
      }

      return var1;
   }

   public void invalidateSelf() {
      super.invalidateSelf();
      MediaActionDrawable.MediaActionDrawableDelegate var1 = this.delegate;
      if (var1 != null) {
         var1.invalidate();
      }

   }

   public void setAlpha(int var1) {
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
      this.scale = (float)(var3 - var1) / (float)this.getIntrinsicWidth();
      if (this.scale < 0.7F) {
         this.paint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      }

   }

   public void setColor(int var1) {
      Paint var2 = this.paint;
      int var3 = -16777216 | var1;
      var2.setColor(var3);
      this.paint2.setColor(var3);
      this.paint3.setColor(var3);
      this.textPaint.setColor(var3);
      this.colorFilter = new PorterDuffColorFilter(var1, Mode.MULTIPLY);
   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
      this.paint2.setColorFilter(var1);
      this.paint3.setColorFilter(var1);
      this.textPaint.setColorFilter(var1);
   }

   public void setDelegate(MediaActionDrawable.MediaActionDrawableDelegate var1) {
      this.delegate = var1;
   }

   public boolean setIcon(int var1, boolean var2) {
      int var3;
      if (this.currentIcon == var1) {
         var3 = this.nextIcon;
         if (var3 != var1) {
            this.currentIcon = var3;
            this.transitionProgress = 1.0F;
         }
      }

      if (var2) {
         var3 = this.currentIcon;
         if (var3 == var1 || this.nextIcon == var1) {
            return false;
         }

         if (var3 == 2 && (var1 == 3 || var1 == 14)) {
            this.transitionAnimationTime = 400.0F;
         } else if (this.currentIcon != 4 && var1 == 6) {
            this.transitionAnimationTime = 360.0F;
         } else if ((this.currentIcon != 4 || var1 != 14) && (this.currentIcon != 14 || var1 != 4)) {
            this.transitionAnimationTime = 220.0F;
         } else {
            this.transitionAnimationTime = 160.0F;
         }

         this.animatingTransition = true;
         this.nextIcon = var1;
         this.savedTransitionProgress = this.transitionProgress;
         this.transitionProgress = 0.0F;
      } else {
         if (this.currentIcon == var1) {
            return false;
         }

         this.animatingTransition = false;
         this.nextIcon = var1;
         this.currentIcon = var1;
         this.savedTransitionProgress = this.transitionProgress;
         this.transitionProgress = 1.0F;
      }

      if (var1 == 3 || var1 == 14) {
         this.downloadRadOffset = 112.0F;
         this.animatedDownloadProgress = 0.0F;
         this.downloadProgressAnimationStart = 0.0F;
         this.downloadProgressTime = 0.0F;
      }

      this.invalidateSelf();
      return true;
   }

   public void setMini(boolean var1) {
      this.isMini = var1;
      Paint var2 = this.paint;
      float var3;
      if (this.isMini) {
         var3 = 2.0F;
      } else {
         var3 = 3.0F;
      }

      var2.setStrokeWidth((float)AndroidUtilities.dp(var3));
   }

   public void setOverrideAlpha(float var1) {
      this.overrideAlpha = var1;
   }

   public void setProgress(float var1, boolean var2) {
      if (!var2) {
         this.animatedDownloadProgress = var1;
         this.downloadProgressAnimationStart = var1;
      } else {
         if (this.animatedDownloadProgress > var1) {
            this.animatedDownloadProgress = var1;
         }

         this.downloadProgressAnimationStart = this.animatedDownloadProgress;
      }

      this.downloadProgress = var1;
      this.downloadProgressTime = 0.0F;
      this.invalidateSelf();
   }

   public interface MediaActionDrawableDelegate {
      void invalidate();
   }
}
