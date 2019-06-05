package android.support.design.circularreveal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.Path.Direction;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.design.widget.MathUtils;
import android.view.View;

public class CircularRevealHelper {
   public static final int STRATEGY;
   private boolean buildingCircularRevealCache;
   private final CircularRevealHelper.Delegate delegate;
   private boolean hasCircularRevealCache;
   private Drawable overlayDrawable;
   private CircularRevealWidget.RevealInfo revealInfo;
   private final Paint revealPaint;
   private final Path revealPath;
   private final Paint scrimPaint;
   private final View view;

   static {
      if (VERSION.SDK_INT >= 21) {
         STRATEGY = 2;
      } else if (VERSION.SDK_INT >= 18) {
         STRATEGY = 1;
      } else {
         STRATEGY = 0;
      }

   }

   private void drawOverlayDrawable(Canvas var1) {
      if (this.shouldDrawOverlayDrawable()) {
         Rect var2 = this.overlayDrawable.getBounds();
         float var3 = this.revealInfo.centerX - (float)var2.width() / 2.0F;
         float var4 = this.revealInfo.centerY - (float)var2.height() / 2.0F;
         var1.translate(var3, var4);
         this.overlayDrawable.draw(var1);
         var1.translate(-var3, -var4);
      }

   }

   private float getDistanceToFurthestCorner(CircularRevealWidget.RevealInfo var1) {
      return MathUtils.distanceToFurthestCorner(var1.centerX, var1.centerY, 0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight());
   }

   private void invalidateRevealInfo() {
      if (STRATEGY == 1) {
         this.revealPath.rewind();
         if (this.revealInfo != null) {
            this.revealPath.addCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, Direction.CW);
         }
      }

      this.view.invalidate();
   }

   private boolean shouldDrawCircularReveal() {
      CircularRevealWidget.RevealInfo var1 = this.revealInfo;
      boolean var2 = false;
      boolean var3;
      if (var1 != null && !this.revealInfo.isInvalid()) {
         var3 = false;
      } else {
         var3 = true;
      }

      if (STRATEGY == 0) {
         boolean var4 = var2;
         if (!var3) {
            var4 = var2;
            if (this.hasCircularRevealCache) {
               var4 = true;
            }
         }

         return var4;
      } else {
         return var3 ^ true;
      }
   }

   private boolean shouldDrawOverlayDrawable() {
      boolean var1;
      if (!this.buildingCircularRevealCache && this.overlayDrawable != null && this.revealInfo != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean shouldDrawScrim() {
      boolean var1;
      if (!this.buildingCircularRevealCache && Color.alpha(this.scrimPaint.getColor()) != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void buildCircularRevealCache() {
      if (STRATEGY == 0) {
         this.buildingCircularRevealCache = true;
         this.hasCircularRevealCache = false;
         this.view.buildDrawingCache();
         Bitmap var1 = this.view.getDrawingCache();
         Bitmap var2 = var1;
         if (var1 == null) {
            var2 = var1;
            if (this.view.getWidth() != 0) {
               var2 = var1;
               if (this.view.getHeight() != 0) {
                  var2 = Bitmap.createBitmap(this.view.getWidth(), this.view.getHeight(), Config.ARGB_8888);
                  Canvas var3 = new Canvas(var2);
                  this.view.draw(var3);
               }
            }
         }

         if (var2 != null) {
            this.revealPaint.setShader(new BitmapShader(var2, TileMode.CLAMP, TileMode.CLAMP));
         }

         this.buildingCircularRevealCache = false;
         this.hasCircularRevealCache = true;
      }

   }

   public void destroyCircularRevealCache() {
      if (STRATEGY == 0) {
         this.hasCircularRevealCache = false;
         this.view.destroyDrawingCache();
         this.revealPaint.setShader((Shader)null);
         this.view.invalidate();
      }

   }

   public void draw(Canvas var1) {
      if (this.shouldDrawCircularReveal()) {
         switch(STRATEGY) {
         case 0:
            var1.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.revealPaint);
            if (this.shouldDrawScrim()) {
               var1.drawCircle(this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.radius, this.scrimPaint);
            }
            break;
         case 1:
            int var2 = var1.save();
            var1.clipPath(this.revealPath);
            this.delegate.actualDraw(var1);
            if (this.shouldDrawScrim()) {
               var1.drawRect(0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
            }

            var1.restoreToCount(var2);
            break;
         case 2:
            this.delegate.actualDraw(var1);
            if (this.shouldDrawScrim()) {
               var1.drawRect(0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
            }
            break;
         default:
            StringBuilder var3 = new StringBuilder();
            var3.append("Unsupported strategy ");
            var3.append(STRATEGY);
            throw new IllegalStateException(var3.toString());
         }
      } else {
         this.delegate.actualDraw(var1);
         if (this.shouldDrawScrim()) {
            var1.drawRect(0.0F, 0.0F, (float)this.view.getWidth(), (float)this.view.getHeight(), this.scrimPaint);
         }
      }

      this.drawOverlayDrawable(var1);
   }

   public Drawable getCircularRevealOverlayDrawable() {
      return this.overlayDrawable;
   }

   public int getCircularRevealScrimColor() {
      return this.scrimPaint.getColor();
   }

   public CircularRevealWidget.RevealInfo getRevealInfo() {
      if (this.revealInfo == null) {
         return null;
      } else {
         CircularRevealWidget.RevealInfo var1 = new CircularRevealWidget.RevealInfo(this.revealInfo);
         if (var1.isInvalid()) {
            var1.radius = this.getDistanceToFurthestCorner(var1);
         }

         return var1;
      }
   }

   public boolean isOpaque() {
      boolean var1;
      if (this.delegate.actualIsOpaque() && !this.shouldDrawCircularReveal()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void setCircularRevealOverlayDrawable(Drawable var1) {
      this.overlayDrawable = var1;
      this.view.invalidate();
   }

   public void setCircularRevealScrimColor(int var1) {
      this.scrimPaint.setColor(var1);
      this.view.invalidate();
   }

   public void setRevealInfo(CircularRevealWidget.RevealInfo var1) {
      if (var1 == null) {
         this.revealInfo = null;
      } else {
         if (this.revealInfo == null) {
            this.revealInfo = new CircularRevealWidget.RevealInfo(var1);
         } else {
            this.revealInfo.set(var1);
         }

         if (MathUtils.geq(var1.radius, this.getDistanceToFurthestCorner(var1), 1.0E-4F)) {
            this.revealInfo.radius = Float.MAX_VALUE;
         }
      }

      this.invalidateRevealInfo();
   }

   interface Delegate {
      void actualDraw(Canvas var1);

      boolean actualIsOpaque();
   }
}
