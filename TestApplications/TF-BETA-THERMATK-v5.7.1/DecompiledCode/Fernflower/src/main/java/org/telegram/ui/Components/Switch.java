package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.util.StateSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.annotation.Keep;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class Switch extends View {
   private boolean attachedToWindow;
   private boolean bitmapsCreated;
   private ObjectAnimator checkAnimator;
   private int colorSet;
   private int drawIconType;
   private boolean drawRipple;
   private ObjectAnimator iconAnimator;
   private Drawable iconDrawable;
   private float iconProgress = 1.0F;
   private boolean isChecked;
   private int lastIconColor;
   private Switch.OnCheckedChangeListener onCheckedChangeListener;
   private Bitmap[] overlayBitmap;
   private Canvas[] overlayCanvas;
   private float overlayCx;
   private float overlayCy;
   private Paint overlayEraserPaint;
   private Bitmap overlayMaskBitmap;
   private Canvas overlayMaskCanvas;
   private Paint overlayMaskPaint;
   private float overlayRad;
   private int overrideColorProgress;
   private Paint paint = new Paint(1);
   private Paint paint2 = new Paint(1);
   private int[] pressedState = new int[]{16842910, 16842919};
   private float progress;
   private RectF rectF = new RectF();
   private RippleDrawable rippleDrawable;
   private Paint ripplePaint;
   private String thumbCheckedColorKey = "windowBackgroundWhite";
   private String thumbColorKey = "windowBackgroundWhite";
   private String trackCheckedColorKey = "switch2TrackChecked";
   private String trackColorKey = "switch2Track";

   public Switch(Context var1) {
      super(var1);
      this.paint2.setStyle(Style.STROKE);
      this.paint2.setStrokeCap(Cap.ROUND);
      this.paint2.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
   }

   private void animateIcon(boolean var1) {
      float var2;
      if (var1) {
         var2 = 1.0F;
      } else {
         var2 = 0.0F;
      }

      this.iconAnimator = ObjectAnimator.ofFloat(this, "iconProgress", new float[]{var2});
      this.iconAnimator.setDuration(250L);
      this.iconAnimator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            Switch.this.iconAnimator = null;
         }
      });
      this.iconAnimator.start();
   }

   private void animateToCheckedState(boolean var1) {
      float var2;
      if (var1) {
         var2 = 1.0F;
      } else {
         var2 = 0.0F;
      }

      this.checkAnimator = ObjectAnimator.ofFloat(this, "progress", new float[]{var2});
      this.checkAnimator.setDuration(250L);
      this.checkAnimator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            Switch.this.checkAnimator = null;
         }
      });
      this.checkAnimator.start();
   }

   private void cancelCheckAnimator() {
      ObjectAnimator var1 = this.checkAnimator;
      if (var1 != null) {
         var1.cancel();
         this.checkAnimator = null;
      }

   }

   private void cancelIconAnimator() {
      ObjectAnimator var1 = this.iconAnimator;
      if (var1 != null) {
         var1.cancel();
         this.iconAnimator = null;
      }

   }

   @Keep
   public float getIconProgress() {
      return this.iconProgress;
   }

   @Keep
   public float getProgress() {
      return this.progress;
   }

   public boolean hasIcon() {
      boolean var1;
      if (this.iconDrawable != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isChecked() {
      return this.isChecked;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.attachedToWindow = true;
   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      this.attachedToWindow = false;
   }

   protected void onDraw(Canvas var1) {
      if (this.getVisibility() == 0) {
         int var2 = AndroidUtilities.dp(31.0F);
         AndroidUtilities.dp(20.0F);
         int var3 = (this.getMeasuredWidth() - var2) / 2;
         float var4 = ((float)this.getMeasuredHeight() - AndroidUtilities.dpf2(14.0F)) / 2.0F;
         int var5 = AndroidUtilities.dp(7.0F) + var3 + (int)((float)AndroidUtilities.dp(17.0F) * this.progress);
         int var6 = this.getMeasuredHeight() / 2;

         int var7;
         Canvas var8;
         int var9;
         float var10;
         int var11;
         int var12;
         int var13;
         Drawable var14;
         int var17;
         int var18;
         for(var7 = 0; var7 < 2; ++var7) {
            if (var7 != 1 || this.overrideColorProgress != 0) {
               if (var7 == 0) {
                  var8 = var1;
               } else {
                  var8 = this.overlayCanvas[0];
               }

               if (var7 == 1) {
                  this.overlayBitmap[0].eraseColor(0);
                  this.paint.setColor(-16777216);
                  this.overlayMaskCanvas.drawRect(0.0F, 0.0F, (float)this.overlayMaskBitmap.getWidth(), (float)this.overlayMaskBitmap.getHeight(), this.paint);
                  this.overlayMaskCanvas.drawCircle(this.overlayCx - this.getX(), this.overlayCy - this.getY(), this.overlayRad, this.overlayEraserPaint);
               }

               label138: {
                  label137: {
                     var9 = this.overrideColorProgress;
                     if (var9 == 1) {
                        if (var7 != 0) {
                           break label137;
                        }
                     } else {
                        if (var9 != 2) {
                           var10 = this.progress;
                           break label138;
                        }

                        if (var7 == 0) {
                           break label137;
                        }
                     }

                     var10 = 0.0F;
                     break label138;
                  }

                  var10 = 1.0F;
               }

               var11 = Theme.getColor(this.trackColorKey);
               var9 = Theme.getColor(this.trackCheckedColorKey);
               if (var7 == 0 && this.iconDrawable != null) {
                  var12 = this.lastIconColor;
                  if (this.isChecked) {
                     var13 = var9;
                  } else {
                     var13 = var11;
                  }

                  if (var12 != var13) {
                     var14 = this.iconDrawable;
                     if (this.isChecked) {
                        var13 = var9;
                     } else {
                        var13 = var11;
                     }

                     this.lastIconColor = var13;
                     var14.setColorFilter(new PorterDuffColorFilter(var13, Mode.MULTIPLY));
                  }
               }

               int var15 = Color.red(var11);
               int var16 = Color.red(var9);
               var17 = Color.green(var11);
               var18 = Color.green(var9);
               var12 = Color.blue(var11);
               var13 = Color.blue(var9);
               var11 = Color.alpha(var11);
               var9 = Color.alpha(var9);
               var16 = (int)((float)var15 + (float)(var16 - var15) * var10);
               var17 = (int)((float)var17 + (float)(var18 - var17) * var10);
               var13 = (int)((float)var12 + (float)(var13 - var12) * var10);
               var9 = (var16 & 255) << 16 | ((int)((float)var11 + (float)(var9 - var11) * var10) & 255) << 24 | (var17 & 255) << 8 | var13 & 255;
               this.paint.setColor(var9);
               this.paint2.setColor(var9);
               this.rectF.set((float)var3, var4, (float)(var3 + var2), AndroidUtilities.dpf2(14.0F) + var4);
               var8.drawRoundRect(this.rectF, AndroidUtilities.dpf2(7.0F), AndroidUtilities.dpf2(7.0F), this.paint);
               var8.drawCircle((float)var5, (float)var6, AndroidUtilities.dpf2(10.0F), this.paint);
               if (var7 == 0) {
                  RippleDrawable var20 = this.rippleDrawable;
                  if (var20 != null) {
                     var20.setBounds(var5 - AndroidUtilities.dp(18.0F), var6 - AndroidUtilities.dp(18.0F), AndroidUtilities.dp(18.0F) + var5, AndroidUtilities.dp(18.0F) + var6);
                     this.rippleDrawable.draw(var8);
                     continue;
                  }
               }

               if (var7 == 1) {
                  var8.drawBitmap(this.overlayMaskBitmap, 0.0F, 0.0F, this.overlayMaskPaint);
               }
            }
         }

         if (this.overrideColorProgress != 0) {
            var1.drawBitmap(this.overlayBitmap[0], 0.0F, 0.0F, (Paint)null);
         }

         var11 = 0;
         var7 = var6;

         for(var9 = var5; var11 < 2; ++var11) {
            if (var11 != 1 || this.overrideColorProgress != 0) {
               if (var11 == 0) {
                  var8 = var1;
               } else {
                  var8 = this.overlayCanvas[1];
               }

               if (var11 == 1) {
                  this.overlayBitmap[1].eraseColor(0);
               }

               label114: {
                  label113: {
                     var13 = this.overrideColorProgress;
                     if (var13 == 1) {
                        if (var11 == 0) {
                           break label113;
                        }
                     } else {
                        if (var13 != 2) {
                           var10 = this.progress;
                           break label114;
                        }

                        if (var11 != 0) {
                           break label113;
                        }
                     }

                     var10 = 1.0F;
                     break label114;
                  }

                  var10 = 0.0F;
               }

               var6 = Theme.getColor(this.thumbColorKey);
               var2 = Theme.getColor(this.thumbCheckedColorKey);
               var17 = Color.red(var6);
               var18 = Color.red(var2);
               var3 = Color.green(var6);
               var12 = Color.green(var2);
               var13 = Color.blue(var6);
               var5 = Color.blue(var2);
               var6 = Color.alpha(var6);
               var2 = Color.alpha(var2);
               var17 = (int)((float)var17 + (float)(var18 - var17) * var10);
               var3 = (int)((float)var3 + (float)(var12 - var3) * var10);
               var13 = (int)((float)var13 + (float)(var5 - var13) * var10);
               var5 = (int)((float)var6 + (float)(var2 - var6) * var10);
               this.paint.setColor((var5 & 255) << 24 | (var17 & 255) << 16 | (var3 & 255) << 8 | var13 & 255);
               var10 = (float)var9;
               var4 = (float)var7;
               var8.drawCircle(var10, var4, (float)AndroidUtilities.dp(8.0F), this.paint);
               if (var11 == 0) {
                  var14 = this.iconDrawable;
                  if (var14 != null) {
                     var14.setBounds(var9 - var14.getIntrinsicWidth() / 2, var7 - this.iconDrawable.getIntrinsicHeight() / 2, this.iconDrawable.getIntrinsicWidth() / 2 + var9, this.iconDrawable.getIntrinsicHeight() / 2 + var7);
                     this.iconDrawable.draw(var8);
                  } else {
                     var13 = this.drawIconType;
                     if (var13 == 1) {
                        var9 = (int)(var10 - ((float)AndroidUtilities.dp(10.8F) - (float)AndroidUtilities.dp(1.3F) * this.progress));
                        var7 = (int)(var4 - ((float)AndroidUtilities.dp(8.5F) - (float)AndroidUtilities.dp(0.5F) * this.progress));
                        var17 = (int)AndroidUtilities.dpf2(4.6F) + var9;
                        var6 = (int)(AndroidUtilities.dpf2(9.5F) + (float)var7);
                        var12 = AndroidUtilities.dp(2.0F);
                        var13 = AndroidUtilities.dp(2.0F);
                        var5 = (int)AndroidUtilities.dpf2(7.5F) + var9;
                        var18 = (int)AndroidUtilities.dpf2(5.4F) + var7;
                        var2 = var5 + AndroidUtilities.dp(7.0F);
                        var3 = var18 + AndroidUtilities.dp(7.0F);
                        var4 = (float)var5;
                        float var19 = (float)(var17 - var5);
                        var10 = this.progress;
                        var5 = (int)(var4 + var19 * var10);
                        var18 = (int)((float)var18 + (float)(var6 - var18) * var10);
                        var2 = (int)((float)var2 + (float)(var12 + var17 - var2) * var10);
                        var13 = (int)((float)var3 + (float)(var13 + var6 - var3) * var10);
                        var8.drawLine((float)var5, (float)var18, (float)var2, (float)var13, this.paint2);
                        var13 = (int)AndroidUtilities.dpf2(7.5F) + var9;
                        var5 = (int)AndroidUtilities.dpf2(12.5F) + var7;
                        var6 = AndroidUtilities.dp(7.0F);
                        var3 = AndroidUtilities.dp(7.0F);
                        var8.drawLine((float)var13, (float)var5, (float)(var6 + var13), (float)(var5 - var3), this.paint2);
                     } else if (var13 == 2 || this.iconAnimator != null) {
                        this.paint2.setAlpha((int)((1.0F - this.iconProgress) * 255.0F));
                        var8.drawLine(var10, var4, var10, (float)(var7 - AndroidUtilities.dp(5.0F)), this.paint2);
                        var8.save();
                        var8.rotate(this.iconProgress * -90.0F, var10, var4);
                        var8.drawLine(var10, var4, (float)(AndroidUtilities.dp(4.0F) + var9), var4, this.paint2);
                        var8.restore();
                     }
                  }
               }

               if (var11 == 1) {
                  var8.drawBitmap(this.overlayMaskBitmap, 0.0F, 0.0F, this.overlayMaskPaint);
               }
            }
         }

         if (this.overrideColorProgress != 0) {
            var1.drawBitmap(this.overlayBitmap[1], 0.0F, 0.0F, (Paint)null);
         }

      }
   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      var1.setClassName("android.widget.Switch");
      var1.setCheckable(true);
      var1.setChecked(this.isChecked);
   }

   public void setChecked(boolean var1, int var2, boolean var3) {
      boolean var4 = this.isChecked;
      float var5 = 1.0F;
      float var6;
      if (var1 != var4) {
         this.isChecked = var1;
         if (this.attachedToWindow && var3) {
            this.animateToCheckedState(var1);
         } else {
            this.cancelCheckAnimator();
            if (var1) {
               var6 = 1.0F;
            } else {
               var6 = 0.0F;
            }

            this.setProgress(var6);
         }

         Switch.OnCheckedChangeListener var7 = this.onCheckedChangeListener;
         if (var7 != null) {
            var7.onCheckedChanged(this, var1);
         }
      }

      if (this.drawIconType != var2) {
         this.drawIconType = var2;
         if (this.attachedToWindow && var3) {
            if (var2 == 0) {
               var1 = true;
            } else {
               var1 = false;
            }

            this.animateIcon(var1);
         } else {
            this.cancelIconAnimator();
            if (var2 == 0) {
               var6 = var5;
            } else {
               var6 = 0.0F;
            }

            this.setIconProgress(var6);
         }
      }

   }

   public void setChecked(boolean var1, boolean var2) {
      this.setChecked(var1, this.drawIconType, var2);
   }

   public void setColors(String var1, String var2, String var3, String var4) {
      this.trackColorKey = var1;
      this.trackCheckedColorKey = var2;
      this.thumbColorKey = var3;
      this.thumbCheckedColorKey = var4;
   }

   public void setDrawIconType(int var1) {
      this.drawIconType = var1;
   }

   public void setDrawRipple(boolean var1) {
      if (VERSION.SDK_INT >= 21 && var1 != this.drawRipple) {
         this.drawRipple = var1;
         RippleDrawable var2 = this.rippleDrawable;
         byte var3 = 1;
         if (var2 == null) {
            this.ripplePaint = new Paint(1);
            this.ripplePaint.setColor(-1);
            Drawable var7;
            if (VERSION.SDK_INT >= 23) {
               var7 = null;
            } else {
               var7 = new Drawable() {
                  public void draw(Canvas var1) {
                     android.graphics.Rect var2 = this.getBounds();
                     var1.drawCircle((float)var2.centerX(), (float)var2.centerY(), (float)AndroidUtilities.dp(18.0F), Switch.this.ripplePaint);
                  }

                  public int getOpacity() {
                     return 0;
                  }

                  public void setAlpha(int var1) {
                  }

                  public void setColorFilter(ColorFilter var1) {
                  }
               };
            }

            this.rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{0}), (Drawable)null, var7);
            if (VERSION.SDK_INT >= 23) {
               this.rippleDrawable.setRadius(AndroidUtilities.dp(18.0F));
            }

            this.rippleDrawable.setCallback(this);
         }

         if (this.isChecked && this.colorSet != 2 || !this.isChecked && this.colorSet != 1) {
            String var8;
            if (this.isChecked) {
               var8 = "switchTrackBlueSelectorChecked";
            } else {
               var8 = "switchTrackBlueSelector";
            }

            int var4 = Theme.getColor(var8);
            ColorStateList var9 = new ColorStateList(new int[][]{StateSet.WILD_CARD}, new int[]{var4});
            this.rippleDrawable.setColor(var9);
            if (this.isChecked) {
               var3 = 2;
            }

            this.colorSet = var3;
         }

         if (VERSION.SDK_INT >= 28 && var1) {
            var2 = this.rippleDrawable;
            float var5;
            if (this.isChecked) {
               var5 = 0.0F;
            } else {
               var5 = (float)AndroidUtilities.dp(100.0F);
            }

            var2.setHotspot(var5, (float)AndroidUtilities.dp(18.0F));
         }

         RippleDrawable var6 = this.rippleDrawable;
         int[] var10;
         if (var1) {
            var10 = this.pressedState;
         } else {
            var10 = StateSet.NOTHING;
         }

         var6.setState(var10);
         this.invalidate();
      }

   }

   public void setIcon(int var1) {
      if (var1 != 0) {
         this.iconDrawable = this.getResources().getDrawable(var1).mutate();
         Drawable var2 = this.iconDrawable;
         if (var2 != null) {
            String var3;
            if (this.isChecked) {
               var3 = this.trackCheckedColorKey;
            } else {
               var3 = this.trackColorKey;
            }

            var1 = Theme.getColor(var3);
            this.lastIconColor = var1;
            var2.setColorFilter(new PorterDuffColorFilter(var1, Mode.MULTIPLY));
         }
      } else {
         this.iconDrawable = null;
      }

   }

   @Keep
   public void setIconProgress(float var1) {
      if (this.iconProgress != var1) {
         this.iconProgress = var1;
         this.invalidate();
      }
   }

   public void setOnCheckedChangeListener(Switch.OnCheckedChangeListener var1) {
      this.onCheckedChangeListener = var1;
   }

   public void setOverrideColor(int var1) {
      if (this.overrideColorProgress != var1) {
         if (this.overlayBitmap == null) {
            boolean var10001;
            try {
               this.overlayBitmap = new Bitmap[2];
               this.overlayCanvas = new Canvas[2];
            } catch (Throwable var7) {
               var10001 = false;
               return;
            }

            for(int var2 = 0; var2 < 2; ++var2) {
               try {
                  this.overlayBitmap[var2] = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Config.ARGB_8888);
                  this.overlayCanvas[var2] = new Canvas(this.overlayBitmap[var2]);
               } catch (Throwable var6) {
                  var10001 = false;
                  return;
               }
            }

            try {
               this.overlayMaskBitmap = Bitmap.createBitmap(this.getMeasuredWidth(), this.getMeasuredHeight(), Config.ARGB_8888);
               Canvas var3 = new Canvas(this.overlayMaskBitmap);
               this.overlayMaskCanvas = var3;
               Paint var8 = new Paint(1);
               this.overlayEraserPaint = var8;
               var8 = this.overlayEraserPaint;
               PorterDuffXfermode var4 = new PorterDuffXfermode(Mode.CLEAR);
               var8.setXfermode(var4);
               var8 = new Paint(1);
               this.overlayMaskPaint = var8;
               var8 = this.overlayMaskPaint;
               var4 = new PorterDuffXfermode(Mode.DST_OUT);
               var8.setXfermode(var4);
               this.bitmapsCreated = true;
            } catch (Throwable var5) {
               var10001 = false;
               return;
            }
         }

         if (this.bitmapsCreated) {
            this.overrideColorProgress = var1;
            this.overlayCx = 0.0F;
            this.overlayCy = 0.0F;
            this.overlayRad = 0.0F;
            this.invalidate();
         }
      }
   }

   public void setOverrideColorProgress(float var1, float var2, float var3) {
      this.overlayCx = var1;
      this.overlayCy = var2;
      this.overlayRad = var3;
      this.invalidate();
   }

   @Keep
   public void setProgress(float var1) {
      if (this.progress != var1) {
         this.progress = var1;
         this.invalidate();
      }
   }

   protected boolean verifyDrawable(Drawable var1) {
      boolean var3;
      if (!super.verifyDrawable(var1)) {
         RippleDrawable var2 = this.rippleDrawable;
         if (var2 == null || var1 != var2) {
            var3 = false;
            return var3;
         }
      }

      var3 = true;
      return var3;
   }

   public interface OnCheckedChangeListener {
      void onCheckedChanged(Switch var1, boolean var2);
   }
}
