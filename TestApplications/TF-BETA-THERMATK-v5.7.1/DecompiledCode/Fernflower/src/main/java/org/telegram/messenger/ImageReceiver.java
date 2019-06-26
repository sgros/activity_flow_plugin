package org.telegram.messenger;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.airbnb.lottie.LottieDrawable;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.RecyclableDrawable;

public class ImageReceiver implements NotificationCenter.NotificationCenterDelegate {
   private static final int TYPE_CROSSFDADE = 2;
   public static final int TYPE_IMAGE = 0;
   public static final int TYPE_MEDIA = 3;
   public static final int TYPE_THUMB = 1;
   private static PorterDuffColorFilter selectedColorFilter;
   private static PorterDuffColorFilter selectedGroupColorFilter;
   private boolean allowDecodeSingleFrame;
   private boolean allowStartAnimation;
   private RectF bitmapRect;
   private boolean canceledLoading;
   private boolean centerRotation;
   private ColorFilter colorFilter;
   private byte crossfadeAlpha;
   private Drawable crossfadeImage;
   private String crossfadeKey;
   private BitmapShader crossfadeShader;
   private boolean crossfadeWithOldImage;
   private boolean crossfadeWithThumb;
   private boolean crossfadingWithThumb;
   private int currentAccount;
   private float currentAlpha;
   private int currentCacheType;
   private String currentExt;
   private Drawable currentImageDrawable;
   private String currentImageFilter;
   private String currentImageKey;
   private ImageLocation currentImageLocation;
   private boolean currentKeyQuality;
   private Drawable currentMediaDrawable;
   private String currentMediaFilter;
   private String currentMediaKey;
   private ImageLocation currentMediaLocation;
   private Object currentParentObject;
   private int currentSize;
   private Drawable currentThumbDrawable;
   private String currentThumbFilter;
   private String currentThumbKey;
   private ImageLocation currentThumbLocation;
   private ImageReceiver.ImageReceiverDelegate delegate;
   private RectF drawRegion;
   private boolean forceCrossfade;
   private boolean forceLoding;
   private boolean forcePreview;
   private int imageH;
   private int imageOrientation;
   private BitmapShader imageShader;
   private int imageTag;
   private int imageW;
   private int imageX;
   private int imageY;
   private boolean invalidateAll;
   private boolean isAspectFit;
   private int isPressed;
   private boolean isVisible;
   private long lastUpdateAlphaTime;
   private boolean manualAlphaAnimator;
   private BitmapShader mediaShader;
   private int mediaTag;
   private boolean needsQualityThumb;
   private float overrideAlpha;
   private int param;
   private View parentView;
   private TLRPC.Document qulityThumbDocument;
   private Paint roundPaint;
   private int roundRadius;
   private RectF roundRect;
   private ImageReceiver.SetImageBackup setImageBackup;
   private Matrix shaderMatrix;
   private boolean shouldGenerateQualityThumb;
   private float sideClip;
   private Drawable staticThumbDrawable;
   private ImageLocation strippedLocation;
   private int thumbOrientation;
   private BitmapShader thumbShader;
   private int thumbTag;
   private boolean useSharedAnimationQueue;

   static {
      selectedColorFilter = new PorterDuffColorFilter(-2236963, Mode.MULTIPLY);
      selectedGroupColorFilter = new PorterDuffColorFilter(-4473925, Mode.MULTIPLY);
   }

   public ImageReceiver() {
      this((View)null);
   }

   public ImageReceiver(View var1) {
      this.allowStartAnimation = true;
      this.drawRegion = new RectF();
      this.isVisible = true;
      this.roundRect = new RectF();
      this.bitmapRect = new RectF();
      this.shaderMatrix = new Matrix();
      this.overrideAlpha = 1.0F;
      this.crossfadeAlpha = (byte)1;
      this.parentView = var1;
      this.roundPaint = new Paint(3);
      this.currentAccount = UserConfig.selectedAccount;
   }

   private void checkAlphaAnimation(boolean var1) {
      if (!this.manualAlphaAnimator) {
         if (this.currentAlpha != 1.0F) {
            if (!var1) {
               long var2 = System.currentTimeMillis() - this.lastUpdateAlphaTime;
               long var4 = var2;
               if (var2 > 18L) {
                  var4 = 18L;
               }

               this.currentAlpha += (float)var4 / 150.0F;
               if (this.currentAlpha > 1.0F) {
                  this.currentAlpha = 1.0F;
                  if (this.crossfadeImage != null) {
                     this.recycleBitmap((String)null, 2);
                     this.crossfadeShader = null;
                  }
               }
            }

            this.lastUpdateAlphaTime = System.currentTimeMillis();
            View var6 = this.parentView;
            if (var6 != null) {
               if (this.invalidateAll) {
                  var6.invalidate();
               } else {
                  int var7 = this.imageX;
                  int var8 = this.imageY;
                  var6.invalidate(var7, var8, this.imageW + var7, this.imageH + var8);
               }
            }
         }

      }
   }

   private void drawDrawable(Canvas var1, Drawable var2, int var3, BitmapShader var4, int var5) {
      boolean var6 = var2 instanceof BitmapDrawable;
      float var7 = 1.0F;
      int var11;
      int var12;
      float var13;
      float var14;
      float var15;
      int var33;
      if (var6) {
         BitmapDrawable var8 = (BitmapDrawable)var2;
         Paint var9;
         if (var4 != null) {
            var9 = this.roundPaint;
         } else {
            var9 = var8.getPaint();
         }

         boolean var10;
         if (var9 != null && var9.getColorFilter() != null) {
            var10 = true;
         } else {
            var10 = false;
         }

         if (var10 && this.isPressed == 0) {
            if (var4 != null) {
               this.roundPaint.setColorFilter((ColorFilter)null);
            } else if (this.staticThumbDrawable != var2) {
               var8.setColorFilter((ColorFilter)null);
            }
         } else if (!var10) {
            var33 = this.isPressed;
            if (var33 != 0) {
               if (var33 == 1) {
                  if (var4 != null) {
                     this.roundPaint.setColorFilter(selectedColorFilter);
                  } else {
                     var8.setColorFilter(selectedColorFilter);
                  }
               } else if (var4 != null) {
                  this.roundPaint.setColorFilter(selectedGroupColorFilter);
               } else {
                  var8.setColorFilter(selectedGroupColorFilter);
               }
            }
         }

         ColorFilter var25 = this.colorFilter;
         if (var25 != null) {
            if (var4 != null) {
               this.roundPaint.setColorFilter(var25);
            } else {
               var8.setColorFilter(var25);
            }
         }

         var6 = var8 instanceof AnimatedFileDrawable;
         if (var6) {
            var33 = var5 % 360;
            if (var33 != 90 && var33 != 270) {
               var11 = var8.getIntrinsicWidth();
               var33 = var8.getIntrinsicHeight();
            } else {
               var11 = var8.getIntrinsicHeight();
               var33 = var8.getIntrinsicWidth();
            }
         } else {
            var33 = var5 % 360;
            if (var33 != 90 && var33 != 270) {
               var11 = var8.getBitmap().getWidth();
               var33 = var8.getBitmap().getHeight();
            } else {
               var11 = var8.getBitmap().getHeight();
               var33 = var8.getBitmap().getWidth();
            }
         }

         var12 = this.imageW;
         var13 = (float)var12;
         var7 = this.sideClip;
         var14 = var13 - var7 * 2.0F;
         var15 = (float)this.imageH - var7 * 2.0F;
         if (var12 == 0) {
            var7 = 1.0F;
         } else {
            var7 = (float)var11 / var14;
         }

         if (this.imageH == 0) {
            var13 = 1.0F;
         } else {
            var13 = (float)var33 / var15;
         }

         int var16;
         int var17;
         RectF var26;
         if (var4 != null) {
            Matrix var27;
            if (this.isAspectFit) {
               var13 = Math.max(var7, var13);
               var5 = (int)((float)var11 / var13);
               var12 = (int)((float)var33 / var13);
               var26 = this.drawRegion;
               var16 = this.imageX;
               var11 = this.imageW;
               var7 = (float)((var11 - var5) / 2 + var16);
               var17 = this.imageY;
               var33 = this.imageH;
               var26.set(var7, (float)((var33 - var12) / 2 + var17), (float)(var16 + (var11 + var5) / 2), (float)(var17 + (var33 + var12) / 2));
               if (this.isVisible) {
                  this.roundPaint.setShader(var4);
                  this.shaderMatrix.reset();
                  Matrix var31 = this.shaderMatrix;
                  var26 = this.drawRegion;
                  var31.setTranslate(var26.left, var26.top);
                  var27 = this.shaderMatrix;
                  var7 = 1.0F / var13;
                  var27.preScale(var7, var7);
                  var4.setLocalMatrix(this.shaderMatrix);
                  this.roundPaint.setAlpha(var3);
                  this.roundRect.set(this.drawRegion);
                  var26 = this.roundRect;
                  var3 = this.roundRadius;
                  var1.drawRoundRect(var26, (float)var3, (float)var3, this.roundPaint);
               }
            } else {
               this.roundPaint.setShader(var4);
               float var18 = 1.0F / Math.min(var7, var13);
               var26 = this.roundRect;
               var16 = this.imageX;
               float var19 = (float)var16;
               float var20 = this.sideClip;
               var12 = this.imageY;
               var26.set(var19 + var20, (float)var12 + var20, (float)(var16 + this.imageW) - var20, (float)(var12 + this.imageH) - var20);
               this.shaderMatrix.reset();
               if (Math.abs(var7 - var13) > 5.0E-4F) {
                  var13 = (float)var11 / var13;
                  if (var13 > var14) {
                     var11 = (int)var13;
                     var26 = this.drawRegion;
                     var33 = this.imageX;
                     var7 = (float)var33;
                     var19 = (float)var11;
                     var13 = (var19 - var14) / 2.0F;
                     var11 = this.imageY;
                     var26.set(var7 - var13, (float)var11, (float)var33 + (var19 + var14) / 2.0F, (float)var11 + var15);
                  } else {
                     var11 = (int)((float)var33 / var7);
                     var26 = this.drawRegion;
                     var12 = this.imageX;
                     var7 = (float)var12;
                     var33 = this.imageY;
                     var19 = (float)var33;
                     var13 = (float)var11;
                     var26.set(var7, var19 - (var13 - var15) / 2.0F, (float)var12 + var14, (float)var33 + (var13 + var15) / 2.0F);
                  }
               } else {
                  var26 = this.drawRegion;
                  var11 = this.imageX;
                  var7 = (float)var11;
                  var33 = this.imageY;
                  var26.set(var7, (float)var33, (float)var11 + var14, (float)var33 + var15);
               }

               if (this.isVisible) {
                  this.shaderMatrix.reset();
                  var27 = this.shaderMatrix;
                  RectF var32 = this.drawRegion;
                  var13 = var32.left;
                  var7 = this.sideClip;
                  var27.setTranslate(var13 + var7, var32.top + var7);
                  if (var5 == 90) {
                     this.shaderMatrix.preRotate(90.0F);
                     this.shaderMatrix.preTranslate(0.0F, -this.drawRegion.width());
                  } else if (var5 == 180) {
                     this.shaderMatrix.preRotate(180.0F);
                     this.shaderMatrix.preTranslate(-this.drawRegion.width(), -this.drawRegion.height());
                  } else if (var5 == 270) {
                     this.shaderMatrix.preRotate(270.0F);
                     this.shaderMatrix.preTranslate(-this.drawRegion.height(), 0.0F);
                  }

                  this.shaderMatrix.preScale(var18, var18);
                  var4.setLocalMatrix(this.shaderMatrix);
                  this.roundPaint.setAlpha(var3);
                  var26 = this.roundRect;
                  var3 = this.roundRadius;
                  var1.drawRoundRect(var26, (float)var3, (float)var3, this.roundPaint);
               }
            }
         } else if (this.isAspectFit) {
            var7 = Math.max(var7, var13);
            var1.save();
            var5 = (int)((float)var11 / var7);
            var33 = (int)((float)var33 / var7);
            var26 = this.drawRegion;
            var16 = this.imageX;
            var13 = (float)var16;
            var17 = this.imageW;
            var14 = (float)(var17 - var5) / 2.0F;
            var12 = this.imageY;
            var7 = (float)var12;
            var11 = this.imageH;
            var26.set(var13 + var14, var7 + (float)(var11 - var33) / 2.0F, (float)var16 + (float)(var17 + var5) / 2.0F, (float)var12 + (float)(var11 + var33) / 2.0F);
            var26 = this.drawRegion;
            var8.setBounds((int)var26.left, (int)var26.top, (int)var26.right, (int)var26.bottom);
            if (var6) {
               AnimatedFileDrawable var28 = (AnimatedFileDrawable)var8;
               var26 = this.drawRegion;
               var28.setActualDrawRect(var26.left, var26.top, var26.width(), this.drawRegion.height());
            }

            if (this.isVisible) {
               try {
                  var8.setAlpha(var3);
                  var8.draw(var1);
               } catch (Exception var24) {
                  this.onBitmapException(var8);
                  FileLog.e((Throwable)var24);
               }
            }

            var1.restore();
         } else if (Math.abs(var7 - var13) > 1.0E-5F) {
            var1.save();
            var12 = this.imageX;
            var16 = this.imageY;
            var1.clipRect(var12, var16, this.imageW + var12, this.imageH + var16);
            var12 = var5 % 360;
            if (var12 != 0) {
               if (this.centerRotation) {
                  var1.rotate((float)var5, (float)(this.imageW / 2), (float)(this.imageH / 2));
               } else {
                  var1.rotate((float)var5, 0.0F, 0.0F);
               }
            }

            var13 = (float)var11 / var13;
            var5 = this.imageW;
            if (var13 > (float)var5) {
               var16 = (int)var13;
               var26 = this.drawRegion;
               var33 = this.imageX;
               var13 = (float)var33;
               var7 = (float)(var16 - var5) / 2.0F;
               var11 = this.imageY;
               var26.set(var13 - var7, (float)var11, (float)var33 + (float)(var16 + var5) / 2.0F, (float)(var11 + this.imageH));
            } else {
               var11 = (int)((float)var33 / var7);
               var26 = this.drawRegion;
               var16 = this.imageX;
               var13 = (float)var16;
               var33 = this.imageY;
               var7 = (float)var33;
               var17 = this.imageH;
               var26.set(var13, var7 - (float)(var11 - var17) / 2.0F, (float)(var16 + var5), (float)var33 + (float)(var11 + var17) / 2.0F);
            }

            if (var6) {
               ((AnimatedFileDrawable)var8).setActualDrawRect((float)this.imageX, (float)this.imageY, (float)this.imageW, (float)this.imageH);
            }

            if (var12 != 90 && var12 != 270) {
               var26 = this.drawRegion;
               var8.setBounds((int)var26.left, (int)var26.top, (int)var26.right, (int)var26.bottom);
            } else {
               var15 = this.drawRegion.width() / 2.0F;
               var13 = this.drawRegion.height() / 2.0F;
               var7 = this.drawRegion.centerX();
               var14 = this.drawRegion.centerY();
               var8.setBounds((int)(var7 - var13), (int)(var14 - var15), (int)(var7 + var13), (int)(var14 + var15));
            }

            if (this.isVisible) {
               try {
                  var8.setAlpha(var3);
                  var8.draw(var1);
               } catch (Exception var23) {
                  this.onBitmapException(var8);
                  FileLog.e((Throwable)var23);
               }
            }

            var1.restore();
         } else {
            var1.save();
            var33 = var5 % 360;
            if (var33 != 0) {
               if (this.centerRotation) {
                  var1.rotate((float)var5, (float)(this.imageW / 2), (float)(this.imageH / 2));
               } else {
                  var1.rotate((float)var5, 0.0F, 0.0F);
               }
            }

            var26 = this.drawRegion;
            var5 = this.imageX;
            var7 = (float)var5;
            var11 = this.imageY;
            var26.set(var7, (float)var11, (float)(var5 + this.imageW), (float)(var11 + this.imageH));
            if (var6) {
               ((AnimatedFileDrawable)var8).setActualDrawRect((float)this.imageX, (float)this.imageY, (float)this.imageW, (float)this.imageH);
            }

            if (var33 != 90 && var33 != 270) {
               var26 = this.drawRegion;
               var8.setBounds((int)var26.left, (int)var26.top, (int)var26.right, (int)var26.bottom);
            } else {
               var13 = this.drawRegion.width() / 2.0F;
               var14 = this.drawRegion.height() / 2.0F;
               var15 = this.drawRegion.centerX();
               var7 = this.drawRegion.centerY();
               var8.setBounds((int)(var15 - var14), (int)(var7 - var13), (int)(var15 + var14), (int)(var7 + var13));
            }

            if (this.isVisible) {
               try {
                  var8.setAlpha(var3);
                  var8.draw(var1);
               } catch (Exception var22) {
                  this.onBitmapException(var8);
                  FileLog.e((Throwable)var22);
               }
            }

            var1.restore();
         }
      } else {
         RectF var29 = this.drawRegion;
         var5 = this.imageX;
         var13 = (float)var5;
         var33 = this.imageY;
         var29.set(var13, (float)var33, (float)(var5 + this.imageW), (float)(var33 + this.imageH));
         var29 = this.drawRegion;
         var2.setBounds((int)var29.left, (int)var29.top, (int)var29.right, (int)var29.bottom);
         if (this.isVisible) {
            var6 = var2 instanceof LottieDrawable;
            if (var6) {
               label221: {
                  var1.save();
                  var5 = this.imageX;
                  var5 = this.imageY;
                  var11 = this.getBitmapWidth();
                  var12 = this.getBitmapHeight();
                  if (var11 <= this.imageW) {
                     var33 = var11;
                     var5 = var12;
                     if (var12 <= this.imageH) {
                        break label221;
                     }
                  }

                  var7 = (float)this.imageW;
                  var13 = (float)var11;
                  var15 = var7 / var13;
                  var7 = (float)this.imageH;
                  var14 = (float)var12;
                  var7 = Math.min(var15, var7 / var14);
                  var33 = (int)(var13 * var7);
                  var5 = (int)(var14 * var7);
                  var1.scale(var7, var7);
               }

               var1.translate((float)(this.imageX + (this.imageW - var33) / 2) / var7, (float)(this.imageY + (this.imageH - var5) / 2) / var7);
               View var30 = this.parentView;
               if (var30 != null) {
                  if (this.invalidateAll) {
                     var30.invalidate();
                  } else {
                     var33 = this.imageX;
                     var5 = this.imageY;
                     var30.invalidate(var33, var5, this.imageW + var33, this.imageH + var5);
                  }
               }
            }

            try {
               var2.setAlpha(var3);
               var2.draw(var1);
            } catch (Exception var21) {
               FileLog.e((Throwable)var21);
            }

            if (var6) {
               var1.restore();
            }
         }
      }

   }

   private void onBitmapException(Drawable var1) {
      if (var1 == this.currentMediaDrawable && this.currentMediaKey != null) {
         ImageLoader.getInstance().removeImage(this.currentMediaKey);
         this.currentMediaKey = null;
      } else if (var1 == this.currentImageDrawable && this.currentImageKey != null) {
         ImageLoader.getInstance().removeImage(this.currentImageKey);
         this.currentImageKey = null;
      } else if (var1 == this.currentThumbDrawable && this.currentThumbKey != null) {
         ImageLoader.getInstance().removeImage(this.currentThumbKey);
         this.currentThumbKey = null;
      }

      this.setImage(this.currentMediaLocation, this.currentMediaFilter, this.currentImageLocation, this.currentImageFilter, this.currentThumbLocation, this.currentThumbFilter, this.currentThumbDrawable, this.currentSize, this.currentExt, this.currentParentObject, this.currentCacheType);
   }

   private void recycleBitmap(String var1, int var2) {
      String var3;
      Drawable var4;
      if (var2 == 3) {
         var3 = this.currentMediaKey;
         var4 = this.currentMediaDrawable;
      } else if (var2 == 2) {
         var3 = this.crossfadeKey;
         var4 = this.crossfadeImage;
      } else if (var2 == 1) {
         var3 = this.currentThumbKey;
         var4 = this.currentThumbDrawable;
      } else {
         var3 = this.currentImageKey;
         var4 = this.currentImageDrawable;
      }

      String var5 = var3;
      if (var3 != null) {
         var5 = var3;
         if (var3.startsWith("-")) {
            String var6 = ImageLoader.getInstance().getReplacedKey(var3);
            var5 = var3;
            if (var6 != null) {
               var5 = var6;
            }
         }
      }

      ImageLoader.getInstance().getReplacedKey(var5);
      if (var5 != null && (var1 == null || !var1.equals(var5)) && var4 != null) {
         if (var4 instanceof AnimatedFileDrawable) {
            ((AnimatedFileDrawable)var4).recycle();
         } else if (var4 instanceof BitmapDrawable) {
            Bitmap var8 = ((BitmapDrawable)var4).getBitmap();
            boolean var7 = ImageLoader.getInstance().decrementUseCount(var5);
            if (!ImageLoader.getInstance().isInCache(var5) && var7) {
               var8.recycle();
            }
         }
      }

      if (var2 == 3) {
         this.currentMediaKey = null;
         this.currentMediaDrawable = null;
      } else if (var2 == 2) {
         this.crossfadeKey = null;
         this.crossfadeImage = null;
      } else if (var2 == 1) {
         this.currentThumbDrawable = null;
         this.currentThumbKey = null;
      } else {
         this.currentImageDrawable = null;
         this.currentImageKey = null;
      }

   }

   public boolean canInvertBitmap() {
      boolean var1;
      if (!(this.currentMediaDrawable instanceof ExtendedBitmapDrawable) && !(this.currentImageDrawable instanceof ExtendedBitmapDrawable) && !(this.currentThumbDrawable instanceof ExtendedBitmapDrawable) && !(this.staticThumbDrawable instanceof ExtendedBitmapDrawable)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public void cancelLoadImage() {
      this.forceLoding = false;
      ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
      this.canceledLoading = true;
   }

   public void clearImage() {
      for(int var1 = 0; var1 < 4; ++var1) {
         this.recycleBitmap((String)null, var1);
      }

      ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.didReplacedPhotoInMemCache) {
         String var4 = (String)var3[0];
         String var5 = this.currentMediaKey;
         ImageReceiver.SetImageBackup var7;
         if (var5 != null && var5.equals(var4)) {
            this.currentMediaKey = (String)var3[1];
            this.currentMediaLocation = (ImageLocation)var3[2];
            var7 = this.setImageBackup;
            if (var7 != null) {
               var7.mediaLocation = (ImageLocation)var3[2];
            }
         }

         var5 = this.currentImageKey;
         if (var5 != null && var5.equals(var4)) {
            this.currentImageKey = (String)var3[1];
            this.currentImageLocation = (ImageLocation)var3[2];
            var7 = this.setImageBackup;
            if (var7 != null) {
               var7.imageLocation = (ImageLocation)var3[2];
            }
         }

         var5 = this.currentThumbKey;
         if (var5 != null && var5.equals(var4)) {
            this.currentThumbKey = (String)var3[1];
            this.currentThumbLocation = (ImageLocation)var3[2];
            ImageReceiver.SetImageBackup var6 = this.setImageBackup;
            if (var6 != null) {
               var6.thumbLocation = (ImageLocation)var3[2];
            }
         }
      }

   }

   public boolean draw(Canvas var1) {
      Exception var10000;
      label274: {
         AnimatedFileDrawable var2;
         boolean var10001;
         try {
            var2 = this.getAnimation();
         } catch (Exception var25) {
            var10000 = var25;
            var10001 = false;
            break label274;
         }

         boolean var3;
         label269: {
            label268: {
               if (var2 != null) {
                  try {
                     if (!var2.hasBitmap()) {
                        break label268;
                     }
                  } catch (Exception var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label274;
                  }
               }

               var3 = false;
               break label269;
            }

            var3 = true;
         }

         int var5;
         Drawable var6;
         Object var7;
         Drawable var35;
         label275: {
            label260: {
               BitmapShader var4;
               label276: {
                  label258: {
                     try {
                        if (this.forcePreview || this.currentMediaDrawable == null) {
                           break label258;
                        }
                     } catch (Exception var32) {
                        var10000 = var32;
                        var10001 = false;
                        break label274;
                     }

                     if (!var3) {
                        try {
                           var35 = this.currentMediaDrawable;
                           var4 = this.mediaShader;
                           var5 = this.imageOrientation;
                           break label276;
                        } catch (Exception var24) {
                           var10000 = var24;
                           var10001 = false;
                           break label274;
                        }
                     }
                  }

                  label277: {
                     try {
                        if (this.forcePreview || this.currentImageDrawable == null) {
                           break label277;
                        }
                     } catch (Exception var31) {
                        var10000 = var31;
                        var10001 = false;
                        break label274;
                     }

                     if (!var3) {
                        break label260;
                     }

                     try {
                        if (this.currentMediaDrawable != null) {
                           break label260;
                        }
                     } catch (Exception var27) {
                        var10000 = var27;
                        var10001 = false;
                        break label274;
                     }
                  }

                  try {
                     if (this.crossfadeImage != null && !this.crossfadingWithThumb) {
                        var35 = this.crossfadeImage;
                        var4 = this.crossfadeShader;
                        var5 = this.imageOrientation;
                        break label276;
                     }
                  } catch (Exception var30) {
                     var10000 = var30;
                     var10001 = false;
                     break label274;
                  }

                  try {
                     if (this.staticThumbDrawable instanceof BitmapDrawable) {
                        var35 = this.staticThumbDrawable;
                        var4 = this.thumbShader;
                        var5 = this.thumbOrientation;
                        break label276;
                     }
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break label274;
                  }

                  try {
                     if (this.currentThumbDrawable != null) {
                        var35 = this.currentThumbDrawable;
                        var4 = this.thumbShader;
                        var5 = this.thumbOrientation;
                        break label276;
                     }
                  } catch (Exception var29) {
                     var10000 = var29;
                     var10001 = false;
                     break label274;
                  }

                  var6 = null;
                  var7 = var6;
                  var5 = 0;
                  break label275;
               }

               var6 = var35;
               var7 = var4;
               break label275;
            }

            try {
               var6 = this.currentImageDrawable;
               var7 = this.imageShader;
               var5 = this.imageOrientation;
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
               break label274;
            }

            var3 = false;
         }

         if (var6 != null) {
            label278: {
               label206: {
                  label279: {
                     label204: {
                        try {
                           if (this.crossfadeAlpha == 0) {
                              break label279;
                           }

                           if (!this.crossfadeWithThumb) {
                              break label204;
                           }
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break label278;
                        }

                        if (var3) {
                           try {
                              this.drawDrawable(var1, var6, (int)(this.overrideAlpha * 255.0F), (BitmapShader)var7, var5);
                              break label206;
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                              break label278;
                           }
                        }
                     }

                     label194: {
                        Object var36;
                        label193: {
                           label280: {
                              label284: {
                                 try {
                                    if (!this.crossfadeWithThumb || this.currentAlpha == 1.0F) {
                                       break label194;
                                    }

                                    if (var6 == this.currentImageDrawable || var6 == this.currentMediaDrawable) {
                                       break label284;
                                    }
                                 } catch (Exception var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                    break label278;
                                 }

                                 label285: {
                                    label171:
                                    try {
                                       if (var6 != this.currentThumbDrawable && var6 != this.crossfadeImage) {
                                          break label171;
                                       }
                                       break label285;
                                    } catch (Exception var18) {
                                       var10000 = var18;
                                       var10001 = false;
                                       break label278;
                                    }

                                    try {
                                       if (var6 == this.staticThumbDrawable && this.crossfadeImage != null) {
                                          var35 = this.crossfadeImage;
                                          var36 = this.crossfadeShader;
                                          break label193;
                                       }
                                       break label280;
                                    } catch (Exception var17) {
                                       var10000 = var17;
                                       var10001 = false;
                                       break label278;
                                    }
                                 }

                                 try {
                                    if (this.staticThumbDrawable != null) {
                                       var35 = this.staticThumbDrawable;
                                       var36 = this.thumbShader;
                                       break label193;
                                    }
                                    break label280;
                                 } catch (Exception var16) {
                                    var10000 = var16;
                                    var10001 = false;
                                    break label278;
                                 }
                              }

                              try {
                                 if (this.crossfadeImage != null) {
                                    var35 = this.crossfadeImage;
                                    var36 = this.crossfadeShader;
                                    break label193;
                                 }
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label278;
                              }

                              try {
                                 if (this.currentThumbDrawable != null) {
                                    var35 = this.currentThumbDrawable;
                                    var36 = this.thumbShader;
                                    break label193;
                                 }
                              } catch (Exception var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break label278;
                              }

                              try {
                                 if (this.staticThumbDrawable != null) {
                                    var35 = this.staticThumbDrawable;
                                    var36 = this.thumbShader;
                                    break label193;
                                 }
                              } catch (Exception var14) {
                                 var10000 = var14;
                                 var10001 = false;
                                 break label278;
                              }
                           }

                           var35 = null;
                           var36 = var35;
                        }

                        if (var35 != null) {
                           try {
                              this.drawDrawable(var1, var35, (int)(this.overrideAlpha * 255.0F), (BitmapShader)var36, this.thumbOrientation);
                           } catch (Exception var12) {
                              var10000 = var12;
                              var10001 = false;
                              break label278;
                           }
                        }
                     }

                     try {
                        this.drawDrawable(var1, var6, (int)(this.overrideAlpha * this.currentAlpha * 255.0F), (BitmapShader)var7, var5);
                        break label206;
                     } catch (Exception var11) {
                        var10000 = var11;
                        var10001 = false;
                        break label278;
                     }
                  }

                  try {
                     this.drawDrawable(var1, var6, (int)(this.overrideAlpha * 255.0F), (BitmapShader)var7, var5);
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label278;
                  }
               }

               label143: {
                  label142: {
                     if (var3) {
                        try {
                           if (this.crossfadeWithThumb) {
                              break label142;
                           }
                        } catch (Exception var9) {
                           var10000 = var9;
                           var10001 = false;
                           break label278;
                        }
                     }

                     var3 = false;
                     break label143;
                  }

                  var3 = true;
               }

               try {
                  this.checkAlphaAnimation(var3);
                  return true;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
               }
            }
         } else {
            label283: {
               try {
                  if (this.staticThumbDrawable != null) {
                     this.drawDrawable(var1, this.staticThumbDrawable, (int)(this.overrideAlpha * 255.0F), (BitmapShader)null, this.thumbOrientation);
                     this.checkAlphaAnimation(var3);
                     return true;
                  }
               } catch (Exception var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label283;
               }

               try {
                  this.checkAlphaAnimation(var3);
                  return false;
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
               }
            }
         }
      }

      Exception var34 = var10000;
      FileLog.e((Throwable)var34);
      return false;
   }

   public int getAnimatedOrientation() {
      AnimatedFileDrawable var1 = this.getAnimation();
      int var2;
      if (var1 != null) {
         var2 = var1.getOrientation();
      } else {
         var2 = 0;
      }

      return var2;
   }

   public AnimatedFileDrawable getAnimation() {
      Drawable var1 = this.currentMediaDrawable;
      if (var1 instanceof AnimatedFileDrawable) {
         return (AnimatedFileDrawable)var1;
      } else {
         var1 = this.currentImageDrawable;
         if (var1 instanceof AnimatedFileDrawable) {
            return (AnimatedFileDrawable)var1;
         } else {
            var1 = this.currentThumbDrawable;
            if (var1 instanceof AnimatedFileDrawable) {
               return (AnimatedFileDrawable)var1;
            } else {
               var1 = this.staticThumbDrawable;
               return var1 instanceof AnimatedFileDrawable ? (AnimatedFileDrawable)var1 : null;
            }
         }
      }
   }

   public Bitmap getBitmap() {
      AnimatedFileDrawable var1 = this.getAnimation();
      if (var1 != null && var1.hasBitmap()) {
         return var1.getAnimatedBitmap();
      } else {
         Drawable var2 = this.currentMediaDrawable;
         if (var2 instanceof BitmapDrawable && !(var2 instanceof AnimatedFileDrawable)) {
            return ((BitmapDrawable)var2).getBitmap();
         } else {
            var2 = this.currentImageDrawable;
            if (var2 instanceof BitmapDrawable && !(var2 instanceof AnimatedFileDrawable)) {
               return ((BitmapDrawable)var2).getBitmap();
            } else {
               var2 = this.currentThumbDrawable;
               if (var2 instanceof BitmapDrawable && !(var2 instanceof AnimatedFileDrawable)) {
                  return ((BitmapDrawable)var2).getBitmap();
               } else {
                  var2 = this.staticThumbDrawable;
                  return var2 instanceof BitmapDrawable ? ((BitmapDrawable)var2).getBitmap() : null;
               }
            }
         }
      }
   }

   public int getBitmapHeight() {
      Drawable var1 = this.getDrawable();
      if (var1 instanceof LottieDrawable) {
         return var1.getIntrinsicHeight();
      } else {
         AnimatedFileDrawable var3 = this.getAnimation();
         int var2;
         if (var3 != null) {
            var2 = this.imageOrientation;
            if (var2 % 360 != 0 && var2 % 360 != 180) {
               var2 = var3.getIntrinsicWidth();
            } else {
               var2 = var3.getIntrinsicHeight();
            }

            return var2;
         } else {
            Bitmap var4 = this.getBitmap();
            if (var4 == null) {
               var1 = this.staticThumbDrawable;
               return var1 != null ? var1.getIntrinsicHeight() : 1;
            } else {
               var2 = this.imageOrientation;
               if (var2 % 360 != 0 && var2 % 360 != 180) {
                  var2 = var4.getWidth();
               } else {
                  var2 = var4.getHeight();
               }

               return var2;
            }
         }
      }
   }

   public ImageReceiver.BitmapHolder getBitmapSafe() {
      ImageReceiver.BitmapHolder var2;
      Object var3;
      Bitmap var5;
      label46: {
         AnimatedFileDrawable var1 = this.getAnimation();
         var2 = null;
         if (var1 != null && var1.hasBitmap()) {
            var5 = var1.getAnimatedBitmap();
         } else {
            Drawable var4 = this.currentMediaDrawable;
            if (var4 instanceof BitmapDrawable && !(var4 instanceof AnimatedFileDrawable)) {
               var5 = ((BitmapDrawable)var4).getBitmap();
               var3 = this.currentMediaKey;
               break label46;
            }

            var4 = this.currentImageDrawable;
            if (var4 instanceof BitmapDrawable && !(var4 instanceof AnimatedFileDrawable)) {
               var5 = ((BitmapDrawable)var4).getBitmap();
               var3 = this.currentImageKey;
               break label46;
            }

            var4 = this.currentThumbDrawable;
            if (var4 instanceof BitmapDrawable && !(var4 instanceof AnimatedFileDrawable)) {
               var5 = ((BitmapDrawable)var4).getBitmap();
               var3 = this.currentThumbKey;
               break label46;
            }

            var4 = this.staticThumbDrawable;
            if (!(var4 instanceof BitmapDrawable)) {
               var5 = null;
               var3 = var5;
               break label46;
            }

            var5 = ((BitmapDrawable)var4).getBitmap();
         }

         var3 = null;
      }

      if (var5 != null) {
         var2 = new ImageReceiver.BitmapHolder(var5, (String)var3);
      }

      return var2;
   }

   public int getBitmapWidth() {
      Drawable var1 = this.getDrawable();
      if (var1 instanceof LottieDrawable) {
         return var1.getIntrinsicWidth();
      } else {
         AnimatedFileDrawable var3 = this.getAnimation();
         int var2;
         if (var3 != null) {
            var2 = this.imageOrientation;
            if (var2 % 360 != 0 && var2 % 360 != 180) {
               var2 = var3.getIntrinsicHeight();
            } else {
               var2 = var3.getIntrinsicWidth();
            }

            return var2;
         } else {
            Bitmap var4 = this.getBitmap();
            if (var4 == null) {
               var1 = this.staticThumbDrawable;
               return var1 != null ? var1.getIntrinsicWidth() : 1;
            } else {
               var2 = this.imageOrientation;
               if (var2 % 360 != 0 && var2 % 360 != 180) {
                  var2 = var4.getHeight();
               } else {
                  var2 = var4.getWidth();
               }

               return var2;
            }
         }
      }
   }

   public int getCacheType() {
      return this.currentCacheType;
   }

   public float getCenterX() {
      return (float)this.imageX + (float)this.imageW / 2.0F;
   }

   public float getCenterY() {
      return (float)this.imageY + (float)this.imageH / 2.0F;
   }

   public int getCurrentAccount() {
      return this.currentAccount;
   }

   public float getCurrentAlpha() {
      return this.currentAlpha;
   }

   public RectF getDrawRegion() {
      return this.drawRegion;
   }

   public Drawable getDrawable() {
      Drawable var1 = this.currentMediaDrawable;
      if (var1 != null) {
         return var1;
      } else {
         var1 = this.currentImageDrawable;
         if (var1 != null) {
            return var1;
         } else {
            var1 = this.currentThumbDrawable;
            if (var1 != null) {
               return var1;
            } else {
               var1 = this.staticThumbDrawable;
               return var1 != null ? var1 : null;
            }
         }
      }
   }

   public String getExt() {
      return this.currentExt;
   }

   public float getImageAspectRatio() {
      float var1;
      float var2;
      if (this.imageOrientation % 180 != 0) {
         var1 = this.drawRegion.height();
         var2 = this.drawRegion.width();
      } else {
         var1 = this.drawRegion.width();
         var2 = this.drawRegion.height();
      }

      return var1 / var2;
   }

   public String getImageFilter() {
      return this.currentImageFilter;
   }

   public int getImageHeight() {
      return this.imageH;
   }

   public String getImageKey() {
      return this.currentImageKey;
   }

   public ImageLocation getImageLocation() {
      return this.currentImageLocation;
   }

   public int getImageWidth() {
      return this.imageW;
   }

   public int getImageX() {
      return this.imageX;
   }

   public int getImageX2() {
      return this.imageX + this.imageW;
   }

   public int getImageY() {
      return this.imageY;
   }

   public int getImageY2() {
      return this.imageY + this.imageH;
   }

   public String getMediaFilter() {
      return this.currentMediaFilter;
   }

   public String getMediaKey() {
      return this.currentMediaKey;
   }

   public ImageLocation getMediaLocation() {
      return this.currentMediaLocation;
   }

   public int getOrientation() {
      return this.imageOrientation;
   }

   public int getParam() {
      return this.param;
   }

   public Object getParentObject() {
      return this.currentParentObject;
   }

   public boolean getPressed() {
      boolean var1;
      if (this.isPressed != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public TLRPC.Document getQulityThumbDocument() {
      return this.qulityThumbDocument;
   }

   public int getRoundRadius() {
      return this.roundRadius;
   }

   public int getSize() {
      return this.currentSize;
   }

   public Drawable getStaticThumb() {
      return this.staticThumbDrawable;
   }

   public ImageLocation getStrippedLocation() {
      return this.strippedLocation;
   }

   protected int getTag(int var1) {
      if (var1 == 1) {
         return this.thumbTag;
      } else {
         return var1 == 3 ? this.mediaTag : this.imageTag;
      }
   }

   public Bitmap getThumbBitmap() {
      Drawable var1 = this.currentThumbDrawable;
      if (var1 instanceof BitmapDrawable) {
         return ((BitmapDrawable)var1).getBitmap();
      } else {
         var1 = this.staticThumbDrawable;
         return var1 instanceof BitmapDrawable ? ((BitmapDrawable)var1).getBitmap() : null;
      }
   }

   public ImageReceiver.BitmapHolder getThumbBitmapSafe() {
      Drawable var1 = this.currentThumbDrawable;
      boolean var2 = var1 instanceof BitmapDrawable;
      ImageReceiver.BitmapHolder var3 = null;
      Object var4;
      Bitmap var5;
      if (var2) {
         var5 = ((BitmapDrawable)var1).getBitmap();
         var4 = this.currentThumbKey;
      } else {
         var1 = this.staticThumbDrawable;
         if (var1 instanceof BitmapDrawable) {
            var5 = ((BitmapDrawable)var1).getBitmap();
            var4 = null;
         } else {
            var5 = null;
            var4 = var5;
         }
      }

      if (var5 != null) {
         var3 = new ImageReceiver.BitmapHolder(var5, (String)var4);
      }

      return var3;
   }

   public String getThumbFilter() {
      return this.currentThumbFilter;
   }

   public String getThumbKey() {
      return this.currentThumbKey;
   }

   public ImageLocation getThumbLocation() {
      return this.currentThumbLocation;
   }

   public boolean getVisible() {
      return this.isVisible;
   }

   public boolean hasBitmapImage() {
      boolean var1;
      if (this.currentImageDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean hasImageSet() {
      boolean var1;
      if (this.currentImageDrawable == null && this.currentMediaDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentImageKey == null && this.currentMediaKey == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean hasNotThumb() {
      boolean var1;
      if (this.currentImageDrawable == null && this.currentMediaDrawable == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean hasStaticThumb() {
      boolean var1;
      if (this.staticThumbDrawable != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isAllowStartAnimation() {
      return this.allowStartAnimation;
   }

   public boolean isAnimationRunning() {
      AnimatedFileDrawable var1 = this.getAnimation();
      boolean var2;
      if (var1 != null && var1.isRunning()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isAspectFit() {
      return this.isAspectFit;
   }

   public boolean isCurrentKeyQuality() {
      return this.currentKeyQuality;
   }

   public boolean isForceLoding() {
      return this.forceLoding;
   }

   public boolean isForcePreview() {
      return this.forcePreview;
   }

   public boolean isInsideImage(float var1, float var2) {
      int var3 = this.imageX;
      boolean var4;
      if (var1 >= (float)var3 && var1 <= (float)(var3 + this.imageW)) {
         var3 = this.imageY;
         if (var2 >= (float)var3 && var2 <= (float)(var3 + this.imageH)) {
            var4 = true;
            return var4;
         }
      }

      var4 = false;
      return var4;
   }

   public boolean isNeedsQualityThumb() {
      return this.needsQualityThumb;
   }

   public boolean isShouldGenerateQualityThumb() {
      return this.shouldGenerateQualityThumb;
   }

   public boolean onAttachedToWindow() {
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReplacedPhotoInMemCache);
      ImageReceiver.SetImageBackup var1 = this.setImageBackup;
      if (var1 == null || var1.imageLocation == null && var1.thumbLocation == null && var1.mediaLocation == null && var1.thumb == null) {
         return false;
      } else {
         var1 = this.setImageBackup;
         this.setImage(var1.mediaLocation, var1.mediaFilter, var1.imageLocation, var1.imageFilter, var1.thumbLocation, var1.thumbFilter, var1.thumb, var1.size, var1.ext, var1.parentObject, var1.cacheType);
         return true;
      }
   }

   public void onDetachedFromWindow() {
      if (this.currentImageLocation != null || this.currentMediaLocation != null || this.currentThumbLocation != null || this.staticThumbDrawable != null) {
         if (this.setImageBackup == null) {
            this.setImageBackup = new ImageReceiver.SetImageBackup();
         }

         ImageReceiver.SetImageBackup var1 = this.setImageBackup;
         var1.mediaLocation = this.currentMediaLocation;
         var1.mediaFilter = this.currentMediaFilter;
         var1.imageLocation = this.currentImageLocation;
         var1.imageFilter = this.currentImageFilter;
         var1.thumbLocation = this.currentThumbLocation;
         var1.thumbFilter = this.currentThumbFilter;
         var1.thumb = this.staticThumbDrawable;
         var1.size = this.currentSize;
         var1.ext = this.currentExt;
         var1.cacheType = this.currentCacheType;
         var1.parentObject = this.currentParentObject;
      }

      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReplacedPhotoInMemCache);
      this.clearImage();
   }

   public void setAllowDecodeSingleFrame(boolean var1) {
      this.allowDecodeSingleFrame = var1;
   }

   public void setAllowStartAnimation(boolean var1) {
      this.allowStartAnimation = var1;
   }

   public void setAlpha(float var1) {
      this.overrideAlpha = var1;
   }

   public void setAspectFit(boolean var1) {
      this.isAspectFit = var1;
   }

   public void setColorFilter(ColorFilter var1) {
      this.colorFilter = var1;
   }

   public void setCrossfadeAlpha(byte var1) {
      this.crossfadeAlpha = (byte)var1;
   }

   public void setCrossfadeWithOldImage(boolean var1) {
      this.crossfadeWithOldImage = var1;
   }

   public void setCurrentAccount(int var1) {
      this.currentAccount = var1;
   }

   public void setCurrentAlpha(float var1) {
      this.currentAlpha = var1;
   }

   public void setDelegate(ImageReceiver.ImageReceiverDelegate var1) {
      this.delegate = var1;
   }

   public void setForceCrossfade(boolean var1) {
      this.forceCrossfade = var1;
   }

   public void setForceLoading(boolean var1) {
      this.forceLoding = var1;
   }

   public void setForcePreview(boolean var1) {
      this.forcePreview = var1;
   }

   public void setImage(String var1, String var2, Drawable var3, String var4, int var5) {
      this.setImage(ImageLocation.getForPath(var1), var2, (ImageLocation)null, (String)null, var3, var5, var4, (Object)null, 1);
   }

   public void setImage(ImageLocation var1, String var2, Drawable var3, int var4, String var5, Object var6, int var7) {
      this.setImage(var1, var2, (ImageLocation)null, (String)null, var3, var4, var5, var6, var7);
   }

   public void setImage(ImageLocation var1, String var2, Drawable var3, String var4, Object var5, int var6) {
      this.setImage(var1, var2, (ImageLocation)null, (String)null, var3, 0, var4, var5, var6);
   }

   public void setImage(ImageLocation var1, String var2, ImageLocation var3, String var4, int var5, String var6, Object var7, int var8) {
      this.setImage(var1, var2, var3, var4, (Drawable)null, var5, var6, var7, var8);
   }

   public void setImage(ImageLocation var1, String var2, ImageLocation var3, String var4, Drawable var5, int var6, String var7, Object var8, int var9) {
      this.setImage((ImageLocation)null, (String)null, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public void setImage(ImageLocation var1, String var2, ImageLocation var3, String var4, String var5, Object var6, int var7) {
      this.setImage(var1, var2, var3, var4, (Drawable)null, 0, var5, var6, var7);
   }

   public void setImage(ImageLocation var1, String var2, ImageLocation var3, String var4, ImageLocation var5, String var6, Drawable var7, int var8, String var9, Object var10, int var11) {
      ImageLocation var12 = var1;
      ImageReceiver.SetImageBackup var18 = this.setImageBackup;
      if (var18 != null) {
         var18.imageLocation = null;
         var18.thumbLocation = null;
         var18.mediaLocation = null;
         var18.thumb = null;
      }

      boolean var14 = true;
      boolean var15;
      ImageReceiver.ImageReceiverDelegate var20;
      View var21;
      if (var3 == null && var5 == null && var1 == null) {
         for(var8 = 0; var8 < 4; ++var8) {
            this.recycleBitmap((String)null, var8);
         }

         this.currentImageLocation = null;
         this.currentImageFilter = null;
         this.currentImageKey = null;
         this.currentMediaLocation = null;
         this.currentMediaFilter = null;
         this.currentMediaKey = null;
         this.currentThumbLocation = null;
         this.currentThumbFilter = null;
         this.currentThumbKey = null;
         this.currentMediaDrawable = null;
         this.mediaShader = null;
         this.currentImageDrawable = null;
         this.imageShader = null;
         this.thumbShader = null;
         this.crossfadeShader = null;
         this.currentExt = var9;
         this.currentParentObject = null;
         this.currentCacheType = 0;
         this.staticThumbDrawable = var7;
         this.currentAlpha = 1.0F;
         this.currentSize = 0;
         ImageLoader.getInstance().cancelLoadingForImageReceiver(this, true);
         var21 = this.parentView;
         if (var21 != null) {
            if (this.invalidateAll) {
               var21.invalidate();
            } else {
               var11 = this.imageX;
               var8 = this.imageY;
               var21.invalidate(var11, var8, this.imageW + var11, this.imageH + var8);
            }
         }

         var20 = this.delegate;
         if (var20 != null) {
            if (this.currentImageDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) {
               var15 = false;
            } else {
               var15 = true;
            }

            if (this.currentImageDrawable != null || this.currentMediaDrawable != null) {
               var14 = false;
            }

            var20.didSetImage(this, var15, var14);
         }

      } else {
         String var19;
         if (var3 != null) {
            var19 = var3.getKey(var10, (Object)null);
         } else {
            var19 = null;
         }

         var1 = var3;
         if (var19 == null) {
            var1 = var3;
            if (var3 != null) {
               var1 = null;
            }
         }

         this.currentKeyQuality = false;
         if (var19 == null && this.needsQualityThumb && (var10 instanceof MessageObject || this.qulityThumbDocument != null)) {
            TLRPC.Document var25 = this.qulityThumbDocument;
            if (var25 == null) {
               var25 = ((MessageObject)var10).getDocument();
            }

            if (var25 != null && var25.dc_id != 0 && var25.id != 0L) {
               StringBuilder var22 = new StringBuilder();
               var22.append("q_");
               var22.append(var25.dc_id);
               var22.append("_");
               var22.append(var25.id);
               var19 = var22.toString();
               this.currentKeyQuality = true;
            }
         }

         String var16 = var19;
         if (var19 != null) {
            var16 = var19;
            if (var4 != null) {
               StringBuilder var26 = new StringBuilder();
               var26.append(var19);
               var26.append("@");
               var26.append(var4);
               var16 = var26.toString();
            }
         }

         String var27;
         if (var12 != null) {
            var27 = var12.getKey(var10, (Object)null);
         } else {
            var27 = null;
         }

         var3 = var12;
         if (var27 == null) {
            var3 = var12;
            if (var12 != null) {
               var3 = null;
            }
         }

         String var23 = var27;
         if (var27 != null) {
            var23 = var27;
            if (var2 != null) {
               StringBuilder var24 = new StringBuilder();
               var24.append(var27);
               var24.append("@");
               var24.append(var2);
               var23 = var24.toString();
            }
         }

         label244: {
            label240: {
               if (var23 == null) {
                  var27 = this.currentImageKey;
                  if (var27 != null && var27.equals(var16)) {
                     break label240;
                  }
               }

               var27 = this.currentMediaKey;
               if (var27 == null || !var27.equals(var23)) {
                  break label244;
               }
            }

            ImageReceiver.ImageReceiverDelegate var28 = this.delegate;
            if (var28 != null) {
               if (this.currentImageDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) {
                  var15 = false;
               } else {
                  var15 = true;
               }

               if (this.currentImageDrawable == null && this.currentMediaDrawable == null) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               var28.didSetImage(this, var15, var14);
            }

            if (!this.canceledLoading && !this.forcePreview) {
               return;
            }
         }

         ImageLocation var13 = this.strippedLocation;
         if (var13 == null) {
            if (var3 != null) {
               var13 = var3;
            } else {
               var13 = var1;
            }
         }

         if (var5 != null) {
            var27 = var5.getKey(var10, var13);
         } else {
            var27 = null;
         }

         String var17 = var27;
         if (var27 != null) {
            var17 = var27;
            if (var6 != null) {
               StringBuilder var29 = new StringBuilder();
               var29.append(var27);
               var29.append("@");
               var29.append(var6);
               var17 = var29.toString();
            }
         }

         if (this.crossfadeWithOldImage) {
            if (this.currentImageDrawable != null) {
               this.recycleBitmap(var17, 1);
               this.recycleBitmap((String)null, 2);
               this.recycleBitmap(var23, 3);
               this.crossfadeShader = this.imageShader;
               this.crossfadeImage = this.currentImageDrawable;
               this.crossfadeKey = this.currentImageKey;
               this.crossfadingWithThumb = false;
               this.currentImageDrawable = null;
               this.currentImageKey = null;
            } else if (this.currentThumbDrawable != null) {
               this.recycleBitmap(var16, 0);
               this.recycleBitmap((String)null, 2);
               this.recycleBitmap(var23, 3);
               this.crossfadeShader = this.thumbShader;
               this.crossfadeImage = this.currentThumbDrawable;
               this.crossfadeKey = this.currentThumbKey;
               this.crossfadingWithThumb = false;
               this.currentThumbDrawable = null;
               this.currentThumbKey = null;
            } else if (this.staticThumbDrawable != null) {
               this.recycleBitmap(var16, 0);
               this.recycleBitmap(var17, 1);
               this.recycleBitmap((String)null, 2);
               this.recycleBitmap(var23, 3);
               this.crossfadeShader = this.thumbShader;
               this.crossfadeImage = this.staticThumbDrawable;
               this.crossfadingWithThumb = false;
               this.crossfadeKey = null;
               this.currentThumbDrawable = null;
               this.currentThumbKey = null;
            } else {
               this.recycleBitmap(var16, 0);
               this.recycleBitmap(var17, 1);
               this.recycleBitmap((String)null, 2);
               this.recycleBitmap(var23, 3);
               this.crossfadeShader = null;
            }
         } else {
            this.recycleBitmap(var16, 0);
            this.recycleBitmap(var17, 1);
            this.recycleBitmap((String)null, 2);
            this.recycleBitmap(var23, 3);
            this.crossfadeShader = null;
         }

         var14 = true;
         this.currentImageLocation = var1;
         this.currentImageFilter = var4;
         this.currentImageKey = var16;
         this.currentMediaLocation = var3;
         this.currentMediaFilter = var2;
         this.currentMediaKey = var23;
         this.currentThumbLocation = var5;
         this.currentThumbFilter = var6;
         this.currentThumbKey = var17;
         this.currentParentObject = var10;
         this.currentExt = var9;
         this.currentSize = var8;
         this.currentCacheType = var11;
         this.staticThumbDrawable = var7;
         this.imageShader = null;
         this.thumbShader = null;
         this.mediaShader = null;
         this.currentAlpha = 1.0F;
         var20 = this.delegate;
         if (var20 != null) {
            if (this.currentImageDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) {
               var15 = false;
            } else {
               var15 = true;
            }

            if (this.currentImageDrawable != null || this.currentMediaDrawable != null) {
               var14 = false;
            }

            var20.didSetImage(this, var15, var14);
         }

         ImageLoader.getInstance().loadImageForImageReceiver(this);
         var21 = this.parentView;
         if (var21 != null) {
            if (this.invalidateAll) {
               var21.invalidate();
            } else {
               var8 = this.imageX;
               var11 = this.imageY;
               var21.invalidate(var8, var11, this.imageW + var8, this.imageH + var11);
            }
         }

      }
   }

   public void setImageBitmap(Bitmap var1) {
      BitmapDrawable var2 = null;
      if (var1 != null) {
         var2 = new BitmapDrawable((Resources)null, var1);
      }

      this.setImageBitmap((Drawable)var2);
   }

   public void setImageBitmap(Drawable var1) {
      ImageLoader var2 = ImageLoader.getInstance();
      boolean var3 = true;
      var2.cancelLoadingForImageReceiver(this, true);
      int var4;
      if (this.crossfadeWithOldImage) {
         if (this.currentImageDrawable != null) {
            this.recycleBitmap((String)null, 1);
            this.recycleBitmap((String)null, 2);
            this.recycleBitmap((String)null, 3);
            this.crossfadeShader = this.imageShader;
            this.crossfadeImage = this.currentImageDrawable;
            this.crossfadeKey = this.currentImageKey;
            this.crossfadingWithThumb = true;
         } else if (this.currentThumbDrawable != null) {
            this.recycleBitmap((String)null, 0);
            this.recycleBitmap((String)null, 2);
            this.recycleBitmap((String)null, 3);
            this.crossfadeShader = this.thumbShader;
            this.crossfadeImage = this.currentThumbDrawable;
            this.crossfadeKey = this.currentThumbKey;
            this.crossfadingWithThumb = true;
         } else if (this.staticThumbDrawable != null) {
            this.recycleBitmap((String)null, 0);
            this.recycleBitmap((String)null, 1);
            this.recycleBitmap((String)null, 2);
            this.recycleBitmap((String)null, 3);
            this.crossfadeShader = this.thumbShader;
            this.crossfadeImage = this.staticThumbDrawable;
            this.crossfadingWithThumb = true;
            this.crossfadeKey = null;
         } else {
            for(var4 = 0; var4 < 4; ++var4) {
               this.recycleBitmap((String)null, var4);
            }

            this.crossfadeShader = null;
         }
      } else {
         for(var4 = 0; var4 < 4; ++var4) {
            this.recycleBitmap((String)null, var4);
         }
      }

      Drawable var10 = this.staticThumbDrawable;
      if (var10 instanceof RecyclableDrawable) {
         ((RecyclableDrawable)var10).recycle();
      }

      boolean var5 = var1 instanceof AnimatedFileDrawable;
      if (var5) {
         AnimatedFileDrawable var12 = (AnimatedFileDrawable)var1;
         var12.setParentView(this.parentView);
         var12.setUseSharedQueue(this.useSharedAnimationQueue);
         if (this.allowStartAnimation) {
            var12.start();
         }

         var12.setAllowDecodeSingleFrame(this.allowDecodeSingleFrame);
      }

      this.staticThumbDrawable = var1;
      var4 = this.roundRadius;
      if (var4 != 0 && var1 instanceof BitmapDrawable) {
         if (var5) {
            ((AnimatedFileDrawable)var1).setRoundRadius(var4);
         } else {
            Bitmap var7 = ((BitmapDrawable)var1).getBitmap();
            TileMode var13 = TileMode.CLAMP;
            this.thumbShader = new BitmapShader(var7, var13, var13);
         }
      } else {
         this.thumbShader = null;
      }

      this.currentMediaLocation = null;
      this.currentMediaFilter = null;
      this.currentMediaDrawable = null;
      this.currentMediaKey = null;
      this.mediaShader = null;
      this.currentImageLocation = null;
      this.currentImageFilter = null;
      this.currentImageDrawable = null;
      this.currentImageKey = null;
      this.imageShader = null;
      this.currentThumbLocation = null;
      this.currentThumbFilter = null;
      this.currentThumbKey = null;
      this.currentKeyQuality = false;
      this.currentExt = null;
      this.currentSize = 0;
      this.currentCacheType = 0;
      this.currentAlpha = 1.0F;
      ImageReceiver.SetImageBackup var8 = this.setImageBackup;
      if (var8 != null) {
         var8.imageLocation = null;
         var8.thumbLocation = null;
         var8.mediaLocation = null;
         var8.thumb = null;
      }

      ImageReceiver.ImageReceiverDelegate var9 = this.delegate;
      if (var9 != null) {
         if (this.currentThumbDrawable == null && this.staticThumbDrawable == null) {
            var5 = false;
         } else {
            var5 = true;
         }

         var9.didSetImage(this, var5, true);
      }

      View var11 = this.parentView;
      if (var11 != null) {
         if (this.invalidateAll) {
            var11.invalidate();
         } else {
            var4 = this.imageX;
            int var6 = this.imageY;
            var11.invalidate(var4, var6, this.imageW + var4, this.imageH + var6);
         }
      }

      if (this.forceCrossfade && this.crossfadeWithOldImage && this.crossfadeImage != null) {
         this.currentAlpha = 0.0F;
         this.lastUpdateAlphaTime = System.currentTimeMillis();
         var5 = var3;
         if (this.currentThumbDrawable == null) {
            if (this.staticThumbDrawable != null) {
               var5 = var3;
            } else {
               var5 = false;
            }
         }

         this.crossfadeWithThumb = var5;
      }

   }

   protected boolean setImageBitmapByKey(Drawable var1, String var2, int var3, boolean var4) {
      boolean var5 = false;
      if (var1 != null && var2 != null) {
         boolean var6;
         TileMode var14;
         Bitmap var19;
         if (var3 == 0) {
            if (!var2.equals(this.currentImageKey)) {
               return false;
            }

            var6 = var1 instanceof AnimatedFileDrawable;
            if (!var6 && !(var1 instanceof LottieDrawable)) {
               ImageLoader.getInstance().incrementUseCount(this.currentImageKey);
            }

            this.currentImageDrawable = var1;
            if (var1 instanceof ExtendedBitmapDrawable) {
               this.imageOrientation = ((ExtendedBitmapDrawable)var1).getOrientation();
            }

            var3 = this.roundRadius;
            if (var3 != 0 && var1 instanceof BitmapDrawable) {
               if (var6) {
                  ((AnimatedFileDrawable)var1).setRoundRadius(var3);
               } else {
                  var19 = ((BitmapDrawable)var1).getBitmap();
                  var14 = TileMode.CLAMP;
                  this.imageShader = new BitmapShader(var19, var14, var14);
               }
            } else {
               this.imageShader = null;
            }

            if ((var4 || this.forcePreview) && !this.forceCrossfade) {
               this.currentAlpha = 1.0F;
            } else {
               Drawable var15 = this.currentMediaDrawable;
               boolean var16;
               if (var15 instanceof AnimatedFileDrawable && ((AnimatedFileDrawable)var15).hasBitmap()) {
                  var16 = false;
               } else {
                  var16 = true;
               }

               if (var16 && (this.currentThumbDrawable == null && this.staticThumbDrawable == null || this.currentAlpha == 1.0F || this.forceCrossfade)) {
                  this.currentAlpha = 0.0F;
                  this.lastUpdateAlphaTime = System.currentTimeMillis();
                  if (this.crossfadeImage == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null) {
                     var4 = false;
                  } else {
                     var4 = true;
                  }

                  this.crossfadeWithThumb = var4;
               }
            }
         } else if (var3 == 3) {
            if (!var2.equals(this.currentMediaKey)) {
               return false;
            }

            var6 = var1 instanceof AnimatedFileDrawable;
            if (!var6 && !(var1 instanceof LottieDrawable)) {
               ImageLoader.getInstance().incrementUseCount(this.currentMediaKey);
            }

            this.currentMediaDrawable = var1;
            var3 = this.roundRadius;
            if (var3 != 0 && var1 instanceof BitmapDrawable) {
               if (var6) {
                  ((AnimatedFileDrawable)var1).setRoundRadius(var3);
               } else {
                  var19 = ((BitmapDrawable)var1).getBitmap();
                  var14 = TileMode.CLAMP;
                  this.mediaShader = new BitmapShader(var19, var14, var14);
               }
            } else {
               this.mediaShader = null;
            }

            if (this.currentImageDrawable == null) {
               if ((var4 || this.forcePreview) && !this.forceCrossfade) {
                  this.currentAlpha = 1.0F;
               } else if (this.currentThumbDrawable == null && this.staticThumbDrawable == null || this.currentAlpha == 1.0F || this.forceCrossfade) {
                  this.currentAlpha = 0.0F;
                  this.lastUpdateAlphaTime = System.currentTimeMillis();
                  if (this.crossfadeImage == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null) {
                     var4 = false;
                  } else {
                     var4 = true;
                  }

                  this.crossfadeWithThumb = var4;
               }
            }
         } else if (var3 == 1) {
            if (this.currentThumbDrawable != null) {
               return false;
            }

            if (!this.forcePreview) {
               label296: {
                  AnimatedFileDrawable var7 = this.getAnimation();
                  if (var7 != null && var7.hasBitmap()) {
                     return false;
                  }

                  Drawable var17 = this.currentImageDrawable;
                  if (var17 == null || var17 instanceof AnimatedFileDrawable) {
                     var17 = this.currentMediaDrawable;
                     if (var17 == null || var17 instanceof AnimatedFileDrawable) {
                        break label296;
                     }
                  }

                  return false;
               }
            }

            if (!var2.equals(this.currentThumbKey)) {
               return false;
            }

            ImageLoader.getInstance().incrementUseCount(this.currentThumbKey);
            this.currentThumbDrawable = var1;
            if (var1 instanceof ExtendedBitmapDrawable) {
               this.thumbOrientation = ((ExtendedBitmapDrawable)var1).getOrientation();
            }

            var3 = this.roundRadius;
            if (var3 != 0 && var1 instanceof BitmapDrawable) {
               if (var1 instanceof AnimatedFileDrawable) {
                  ((AnimatedFileDrawable)var1).setRoundRadius(var3);
               } else {
                  Bitmap var12 = ((BitmapDrawable)var1).getBitmap();
                  TileMode var18 = TileMode.CLAMP;
                  this.thumbShader = new BitmapShader(var12, var18, var18);
               }
            } else {
               this.thumbShader = null;
            }

            if (!var4 && this.crossfadeAlpha != 2) {
               Object var13 = this.currentParentObject;
               if (var13 instanceof MessageObject && ((MessageObject)var13).isRoundVideo() && ((MessageObject)this.currentParentObject).isSending()) {
                  this.currentAlpha = 1.0F;
               } else {
                  this.currentAlpha = 0.0F;
                  this.lastUpdateAlphaTime = System.currentTimeMillis();
                  if (this.staticThumbDrawable != null && this.currentImageKey == null && this.currentMediaKey == null) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  this.crossfadeWithThumb = var4;
               }
            } else {
               this.currentAlpha = 1.0F;
            }
         }

         if (var1 instanceof AnimatedFileDrawable) {
            AnimatedFileDrawable var9 = (AnimatedFileDrawable)var1;
            var9.setParentView(this.parentView);
            var9.setUseSharedQueue(this.useSharedAnimationQueue);
            if (this.allowStartAnimation) {
               var9.start();
            }

            var9.setAllowDecodeSingleFrame(this.allowDecodeSingleFrame);
         }

         View var10 = this.parentView;
         if (var10 != null) {
            if (this.invalidateAll) {
               var10.invalidate();
            } else {
               var3 = this.imageX;
               int var8 = this.imageY;
               var10.invalidate(var3, var8, this.imageW + var3, this.imageH + var8);
            }
         }

         ImageReceiver.ImageReceiverDelegate var11 = this.delegate;
         if (var11 != null) {
            if (this.currentImageDrawable == null && this.currentThumbDrawable == null && this.staticThumbDrawable == null && this.currentMediaDrawable == null) {
               var4 = false;
            } else {
               var4 = true;
            }

            var6 = var5;
            if (this.currentImageDrawable == null) {
               var6 = var5;
               if (this.currentMediaDrawable == null) {
                  var6 = true;
               }
            }

            var11.didSetImage(this, var4, var6);
         }

         return true;
      } else {
         return false;
      }
   }

   public void setImageCoords(int var1, int var2, int var3, int var4) {
      this.imageX = var1;
      this.imageY = var2;
      this.imageW = var3;
      this.imageH = var4;
   }

   public void setImageWidth(int var1) {
      this.imageW = var1;
   }

   public void setImageX(int var1) {
      this.imageX = var1;
   }

   public void setImageY(int var1) {
      this.imageY = var1;
   }

   public void setInvalidateAll(boolean var1) {
      this.invalidateAll = var1;
   }

   public void setManualAlphaAnimator(boolean var1) {
      this.manualAlphaAnimator = var1;
   }

   public void setNeedsQualityThumb(boolean var1) {
      this.needsQualityThumb = var1;
   }

   public void setOrientation(int var1, boolean var2) {
      while(true) {
         int var3 = var1;
         if (var1 >= 0) {
            while(var3 > 360) {
               var3 -= 360;
            }

            this.thumbOrientation = var3;
            this.imageOrientation = var3;
            this.centerRotation = var2;
            return;
         }

         var1 += 360;
      }
   }

   public void setParam(int var1) {
      this.param = var1;
   }

   public void setParentView(View var1) {
      this.parentView = var1;
      AnimatedFileDrawable var2 = this.getAnimation();
      if (var2 != null) {
         var2.setParentView(this.parentView);
      }

   }

   public void setPressed(int var1) {
      this.isPressed = var1;
   }

   public void setQualityThumbDocument(TLRPC.Document var1) {
      this.qulityThumbDocument = var1;
   }

   public void setRoundRadius(int var1) {
      this.roundRadius = var1;
   }

   public void setShouldGenerateQualityThumb(boolean var1) {
      this.shouldGenerateQualityThumb = var1;
   }

   public void setSideClip(float var1) {
      this.sideClip = var1;
   }

   public void setStrippedLocation(ImageLocation var1) {
      this.strippedLocation = var1;
   }

   protected void setTag(int var1, int var2) {
      if (var2 == 1) {
         this.thumbTag = var1;
      } else if (var2 == 3) {
         this.mediaTag = var1;
      } else {
         this.imageTag = var1;
      }

   }

   public void setUseSharedAnimationQueue(boolean var1) {
      this.useSharedAnimationQueue = var1;
   }

   public void setVisible(boolean var1, boolean var2) {
      if (this.isVisible != var1) {
         this.isVisible = var1;
         if (var2) {
            View var3 = this.parentView;
            if (var3 != null) {
               if (this.invalidateAll) {
                  var3.invalidate();
               } else {
                  int var4 = this.imageX;
                  int var5 = this.imageY;
                  var3.invalidate(var4, var5, this.imageW + var4, this.imageH + var5);
               }
            }
         }

      }
   }

   public void startAnimation() {
      AnimatedFileDrawable var1 = this.getAnimation();
      if (var1 != null) {
         var1.setUseSharedQueue(this.useSharedAnimationQueue);
         var1.start();
      }

   }

   public void stopAnimation() {
      AnimatedFileDrawable var1 = this.getAnimation();
      if (var1 != null) {
         var1.stop();
      }

   }

   public static class BitmapHolder {
      public Bitmap bitmap;
      private String key;
      private boolean recycleOnRelease;

      public BitmapHolder(Bitmap var1) {
         this.bitmap = var1;
         this.recycleOnRelease = true;
      }

      public BitmapHolder(Bitmap var1, String var2) {
         this.bitmap = var1;
         this.key = var2;
         if (this.key != null) {
            ImageLoader.getInstance().incrementUseCount(this.key);
         }

      }

      public int getHeight() {
         Bitmap var1 = this.bitmap;
         int var2;
         if (var1 != null) {
            var2 = var1.getHeight();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public int getWidth() {
         Bitmap var1 = this.bitmap;
         int var2;
         if (var1 != null) {
            var2 = var1.getWidth();
         } else {
            var2 = 0;
         }

         return var2;
      }

      public boolean isRecycled() {
         Bitmap var1 = this.bitmap;
         boolean var2;
         if (var1 != null && !var1.isRecycled()) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      public void release() {
         if (this.key == null) {
            if (this.recycleOnRelease) {
               Bitmap var1 = this.bitmap;
               if (var1 != null) {
                  var1.recycle();
               }
            }

            this.bitmap = null;
         } else {
            boolean var2 = ImageLoader.getInstance().decrementUseCount(this.key);
            if (!ImageLoader.getInstance().isInCache(this.key) && var2) {
               this.bitmap.recycle();
            }

            this.key = null;
            this.bitmap = null;
         }
      }
   }

   public interface ImageReceiverDelegate {
      void didSetImage(ImageReceiver var1, boolean var2, boolean var3);
   }

   private class SetImageBackup {
      public int cacheType;
      public String ext;
      public String imageFilter;
      public ImageLocation imageLocation;
      public String mediaFilter;
      public ImageLocation mediaLocation;
      public Object parentObject;
      public int size;
      public Drawable thumb;
      public String thumbFilter;
      public ImageLocation thumbLocation;

      private SetImageBackup() {
      }

      // $FF: synthetic method
      SetImageBackup(Object var2) {
         this();
      }
   }
}
