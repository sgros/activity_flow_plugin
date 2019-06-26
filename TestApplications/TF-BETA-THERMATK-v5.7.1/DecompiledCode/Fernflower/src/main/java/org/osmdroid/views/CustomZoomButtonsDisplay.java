package org.osmdroid.views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import org.osmdroid.library.R$drawable;

public class CustomZoomButtonsDisplay {
   private Paint mAlphaPaint;
   private int mBitmapSize;
   private boolean mHorizontalOrVertical;
   private CustomZoomButtonsDisplay.HorizontalPosition mHorizontalPosition;
   private final MapView mMapView;
   private float mMargin;
   private float mPadding;
   private final Point mUnrotatedPoint = new Point();
   private CustomZoomButtonsDisplay.VerticalPosition mVerticalPosition;
   private Bitmap mZoomInBitmapDisabled;
   private Bitmap mZoomInBitmapEnabled;
   private Bitmap mZoomOutBitmapDisabled;
   private Bitmap mZoomOutBitmapEnabled;

   public CustomZoomButtonsDisplay(MapView var1) {
      this.mMapView = var1;
      this.setPositions(true, CustomZoomButtonsDisplay.HorizontalPosition.CENTER, CustomZoomButtonsDisplay.VerticalPosition.BOTTOM);
      this.setMarginPadding(0.5F, 0.5F);
   }

   private Bitmap getBitmap(boolean var1, boolean var2) {
      if (this.mZoomInBitmapEnabled == null) {
         this.setBitmaps(this.getZoomBitmap(true, true), this.getZoomBitmap(true, false), this.getZoomBitmap(false, true), this.getZoomBitmap(false, false));
      }

      Bitmap var3;
      if (var1) {
         if (var2) {
            var3 = this.mZoomInBitmapEnabled;
         } else {
            var3 = this.mZoomInBitmapDisabled;
         }

         return var3;
      } else {
         if (var2) {
            var3 = this.mZoomOutBitmapEnabled;
         } else {
            var3 = this.mZoomOutBitmapDisabled;
         }

         return var3;
      }
   }

   private float getFirstLeft(int var1) {
      int var2 = null.$SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$HorizontalPosition[this.mHorizontalPosition.ordinal()];
      if (var2 != 1) {
         float var3;
         float var4;
         if (var2 != 2) {
            if (var2 == 3) {
               var3 = (float)(var1 / 2);
               if (this.mHorizontalOrVertical) {
                  var4 = this.mPadding;
                  var1 = this.mBitmapSize;
                  var4 = var4 * (float)var1 / 2.0F + (float)var1;
               } else {
                  var4 = (float)(this.mBitmapSize / 2);
               }

               return var3 - var4;
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            float var5 = (float)var1;
            float var6 = this.mMargin;
            var1 = this.mBitmapSize;
            var3 = (float)var1;
            float var7 = (float)var1;
            if (this.mHorizontalOrVertical) {
               var4 = this.mPadding * (float)var1 + (float)var1;
            } else {
               var4 = 0.0F;
            }

            return var5 - var6 * var3 - var7 - var4;
         }
      } else {
         return this.mMargin * (float)this.mBitmapSize;
      }
   }

   private float getFirstTop(int var1) {
      int var2 = null.$SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$VerticalPosition[this.mVerticalPosition.ordinal()];
      if (var2 != 1) {
         float var3;
         float var4;
         if (var2 != 2) {
            if (var2 == 3) {
               var3 = (float)(var1 / 2);
               if (this.mHorizontalOrVertical) {
                  var4 = (float)(this.mBitmapSize / 2);
               } else {
                  var4 = this.mPadding;
                  var1 = this.mBitmapSize;
                  var4 = var4 * (float)var1 / 2.0F + (float)var1;
               }

               return var3 - var4;
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            var3 = (float)var1;
            float var5 = this.mMargin;
            var1 = this.mBitmapSize;
            float var6 = (float)var1;
            float var7 = (float)var1;
            if (this.mHorizontalOrVertical) {
               var4 = 0.0F;
            } else {
               var4 = this.mPadding * (float)var1 + (float)var1;
            }

            return var3 - var5 * var6 - var7 - var4;
         }
      } else {
         return this.mMargin * (float)this.mBitmapSize;
      }
   }

   private float getTopLeft(boolean var1, boolean var2) {
      float var3;
      int var4;
      float var5;
      if (var2) {
         var3 = this.getFirstLeft(this.mMapView.getWidth());
         if (!this.mHorizontalOrVertical) {
            return var3;
         }

         if (!var1) {
            return var3;
         }

         var4 = this.mBitmapSize;
         var5 = var3 + (float)var4;
         var3 = this.mPadding;
      } else {
         var3 = this.getFirstTop(this.mMapView.getHeight());
         if (this.mHorizontalOrVertical) {
            return var3;
         }

         if (var1) {
            return var3;
         }

         var4 = this.mBitmapSize;
         var5 = var3 + (float)var4;
         var3 = this.mPadding;
      }

      return var5 + var3 * (float)var4;
   }

   private boolean isTouched(int var1, int var2, boolean var3) {
      float var4 = (float)var1;
      boolean var5 = true;
      if (this.isTouched(var3, true, var4) && this.isTouched(var3, false, (float)var2)) {
         var3 = var5;
      } else {
         var3 = false;
      }

      return var3;
   }

   private boolean isTouched(boolean var1, boolean var2, float var3) {
      float var4 = this.getTopLeft(var1, var2);
      if (var3 >= var4 && var3 <= var4 + (float)this.mBitmapSize) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void draw(Canvas var1, float var2, boolean var3, boolean var4) {
      if (var2 != 0.0F) {
         Paint var5;
         if (var2 == 1.0F) {
            var5 = null;
         } else {
            if (this.mAlphaPaint == null) {
               this.mAlphaPaint = new Paint();
            }

            this.mAlphaPaint.setAlpha((int)(var2 * 255.0F));
            var5 = this.mAlphaPaint;
         }

         var1.drawBitmap(this.getBitmap(true, var3), this.getTopLeft(true, true), this.getTopLeft(true, false), var5);
         var1.drawBitmap(this.getBitmap(false, var4), this.getTopLeft(false, true), this.getTopLeft(false, false), var5);
      }
   }

   protected Bitmap getIcon(boolean var1) {
      int var2;
      if (var1) {
         var2 = R$drawable.sharp_add_black_36;
      } else {
         var2 = R$drawable.sharp_remove_black_36;
      }

      return ((BitmapDrawable)this.mMapView.getResources().getDrawable(var2)).getBitmap();
   }

   protected Bitmap getZoomBitmap(boolean var1, boolean var2) {
      Bitmap var3 = this.getIcon(var1);
      this.mBitmapSize = var3.getWidth();
      int var4 = this.mBitmapSize;
      Bitmap var5 = Bitmap.createBitmap(var4, var4, Config.ARGB_8888);
      Canvas var6 = new Canvas(var5);
      Paint var7 = new Paint();
      if (var2) {
         var4 = -1;
      } else {
         var4 = -3355444;
      }

      var7.setColor(var4);
      var7.setStyle(Style.FILL);
      var4 = this.mBitmapSize;
      var6.drawRect(0.0F, 0.0F, (float)(var4 - 1), (float)(var4 - 1), var7);
      var6.drawBitmap(var3, 0.0F, 0.0F, (Paint)null);
      return var5;
   }

   public boolean isTouchedRotated(MotionEvent var1, boolean var2) {
      if (this.mMapView.getMapOrientation() == 0.0F) {
         this.mUnrotatedPoint.set((int)var1.getX(), (int)var1.getY());
      } else {
         this.mMapView.getProjection().rotateAndScalePoint((int)var1.getX(), (int)var1.getY(), this.mUnrotatedPoint);
      }

      Point var3 = this.mUnrotatedPoint;
      return this.isTouched(var3.x, var3.y, var2);
   }

   public void setBitmaps(Bitmap var1, Bitmap var2, Bitmap var3, Bitmap var4) {
      this.mZoomInBitmapEnabled = var1;
      this.mZoomInBitmapDisabled = var2;
      this.mZoomOutBitmapEnabled = var3;
      this.mZoomOutBitmapDisabled = var4;
      this.mBitmapSize = this.mZoomInBitmapEnabled.getWidth();
   }

   public void setMarginPadding(float var1, float var2) {
      this.mMargin = var1;
      this.mPadding = var2;
   }

   public void setPositions(boolean var1, CustomZoomButtonsDisplay.HorizontalPosition var2, CustomZoomButtonsDisplay.VerticalPosition var3) {
      this.mHorizontalOrVertical = var1;
      this.mHorizontalPosition = var2;
      this.mVerticalPosition = var3;
   }

   public static enum HorizontalPosition {
      CENTER,
      LEFT,
      RIGHT;
   }

   public static enum VerticalPosition {
      BOTTOM,
      CENTER,
      TOP;
   }
}
