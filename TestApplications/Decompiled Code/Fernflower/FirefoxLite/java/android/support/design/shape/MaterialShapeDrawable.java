package android.support.design.shape;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.TintAwareDrawable;

public class MaterialShapeDrawable extends Drawable implements TintAwareDrawable {
   private int alpha;
   private final ShapePath[] cornerPaths;
   private final Matrix[] cornerTransforms;
   private final Matrix[] edgeTransforms;
   private float interpolation;
   private final Matrix matrix;
   private final Paint paint;
   private Style paintStyle;
   private final Path path;
   private final PointF pointF;
   private float scale;
   private final float[] scratch;
   private final float[] scratch2;
   private final Region scratchRegion;
   private int shadowColor;
   private int shadowElevation;
   private boolean shadowEnabled;
   private int shadowRadius;
   private final ShapePath shapePath;
   private ShapePathModel shapedViewModel;
   private float strokeWidth;
   private PorterDuffColorFilter tintFilter;
   private ColorStateList tintList;
   private Mode tintMode;
   private final Region transparentRegion;
   private boolean useTintColorForShadow;

   private float angleOfCorner(int var1, int var2, int var3) {
      this.getCoordinatesOfCorner((var1 - 1 + 4) % 4, var2, var3, this.pointF);
      float var4 = this.pointF.x;
      float var5 = this.pointF.y;
      this.getCoordinatesOfCorner((var1 + 1) % 4, var2, var3, this.pointF);
      float var6 = this.pointF.x;
      float var7 = this.pointF.y;
      this.getCoordinatesOfCorner(var1, var2, var3, this.pointF);
      float var8 = this.pointF.x;
      float var9 = this.pointF.y;
      var7 = (float)Math.atan2((double)(var5 - var9), (double)(var4 - var8)) - (float)Math.atan2((double)(var7 - var9), (double)(var6 - var8));
      var9 = var7;
      if (var7 < 0.0F) {
         var9 = (float)((double)var7 + 6.283185307179586D);
      }

      return var9;
   }

   private float angleOfEdge(int var1, int var2, int var3) {
      this.getCoordinatesOfCorner(var1, var2, var3, this.pointF);
      float var4 = this.pointF.x;
      float var5 = this.pointF.y;
      this.getCoordinatesOfCorner((var1 + 1) % 4, var2, var3, this.pointF);
      float var6 = this.pointF.x;
      return (float)Math.atan2((double)(this.pointF.y - var5), (double)(var6 - var4));
   }

   private void appendCornerPath(int var1, Path var2) {
      this.scratch[0] = this.cornerPaths[var1].startX;
      this.scratch[1] = this.cornerPaths[var1].startY;
      this.cornerTransforms[var1].mapPoints(this.scratch);
      if (var1 == 0) {
         var2.moveTo(this.scratch[0], this.scratch[1]);
      } else {
         var2.lineTo(this.scratch[0], this.scratch[1]);
      }

      this.cornerPaths[var1].applyToPath(this.cornerTransforms[var1], var2);
   }

   private void appendEdgePath(int var1, Path var2) {
      int var3 = (var1 + 1) % 4;
      this.scratch[0] = this.cornerPaths[var1].endX;
      this.scratch[1] = this.cornerPaths[var1].endY;
      this.cornerTransforms[var1].mapPoints(this.scratch);
      this.scratch2[0] = this.cornerPaths[var3].startX;
      this.scratch2[1] = this.cornerPaths[var3].startY;
      this.cornerTransforms[var3].mapPoints(this.scratch2);
      float var4 = (float)Math.hypot((double)(this.scratch[0] - this.scratch2[0]), (double)(this.scratch[1] - this.scratch2[1]));
      this.shapePath.reset(0.0F, 0.0F);
      this.getEdgeTreatmentForIndex(var1).getEdgePath(var4, this.interpolation, this.shapePath);
      this.shapePath.applyToPath(this.edgeTransforms[var1], var2);
   }

   private void getCoordinatesOfCorner(int var1, int var2, int var3, PointF var4) {
      switch(var1) {
      case 1:
         var4.set((float)var2, 0.0F);
         break;
      case 2:
         var4.set((float)var2, (float)var3);
         break;
      case 3:
         var4.set(0.0F, (float)var3);
         break;
      default:
         var4.set(0.0F, 0.0F);
      }

   }

   private CornerTreatment getCornerTreatmentForIndex(int var1) {
      switch(var1) {
      case 1:
         return this.shapedViewModel.getTopRightCorner();
      case 2:
         return this.shapedViewModel.getBottomRightCorner();
      case 3:
         return this.shapedViewModel.getBottomLeftCorner();
      default:
         return this.shapedViewModel.getTopLeftCorner();
      }
   }

   private EdgeTreatment getEdgeTreatmentForIndex(int var1) {
      switch(var1) {
      case 1:
         return this.shapedViewModel.getRightEdge();
      case 2:
         return this.shapedViewModel.getBottomEdge();
      case 3:
         return this.shapedViewModel.getLeftEdge();
      default:
         return this.shapedViewModel.getTopEdge();
      }
   }

   private void getPath(int var1, int var2, Path var3) {
      this.getPathForSize(var1, var2, var3);
      if (this.scale != 1.0F) {
         this.matrix.reset();
         this.matrix.setScale(this.scale, this.scale, (float)(var1 / 2), (float)(var2 / 2));
         var3.transform(this.matrix);
      }
   }

   private static int modulateAlpha(int var0, int var1) {
      return var0 * (var1 + (var1 >>> 7)) >>> 8;
   }

   private void setCornerPathAndTransform(int var1, int var2, int var3) {
      this.getCoordinatesOfCorner(var1, var2, var3, this.pointF);
      float var4 = this.angleOfCorner(var1, var2, var3);
      this.getCornerTreatmentForIndex(var1).getCornerPath(var4, this.interpolation, this.cornerPaths[var1]);
      var4 = this.angleOfEdge((var1 - 1 + 4) % 4, var2, var3);
      this.cornerTransforms[var1].reset();
      this.cornerTransforms[var1].setTranslate(this.pointF.x, this.pointF.y);
      this.cornerTransforms[var1].preRotate((float)Math.toDegrees((double)(var4 + 1.5707964F)));
   }

   private void setEdgeTransform(int var1, int var2, int var3) {
      this.scratch[0] = this.cornerPaths[var1].endX;
      this.scratch[1] = this.cornerPaths[var1].endY;
      this.cornerTransforms[var1].mapPoints(this.scratch);
      float var4 = this.angleOfEdge(var1, var2, var3);
      this.edgeTransforms[var1].reset();
      this.edgeTransforms[var1].setTranslate(this.scratch[0], this.scratch[1]);
      this.edgeTransforms[var1].preRotate((float)Math.toDegrees((double)var4));
   }

   private void updateTintFilter() {
      if (this.tintList != null && this.tintMode != null) {
         int var1 = this.tintList.getColorForState(this.getState(), 0);
         this.tintFilter = new PorterDuffColorFilter(var1, this.tintMode);
         if (this.useTintColorForShadow) {
            this.shadowColor = var1;
         }

      } else {
         this.tintFilter = null;
      }
   }

   public void draw(Canvas var1) {
      this.paint.setColorFilter(this.tintFilter);
      int var2 = this.paint.getAlpha();
      this.paint.setAlpha(modulateAlpha(var2, this.alpha));
      this.paint.setStrokeWidth(this.strokeWidth);
      this.paint.setStyle(this.paintStyle);
      if (this.shadowElevation > 0 && this.shadowEnabled) {
         this.paint.setShadowLayer((float)this.shadowRadius, 0.0F, (float)this.shadowElevation, this.shadowColor);
      }

      if (this.shapedViewModel != null) {
         this.getPath(var1.getWidth(), var1.getHeight(), this.path);
         var1.drawPath(this.path, this.paint);
      } else {
         var1.drawRect(0.0F, 0.0F, (float)var1.getWidth(), (float)var1.getHeight(), this.paint);
      }

      this.paint.setAlpha(var2);
   }

   public int getOpacity() {
      return -3;
   }

   public void getPathForSize(int var1, int var2, Path var3) {
      var3.rewind();
      if (this.shapedViewModel != null) {
         byte var4 = 0;
         int var5 = 0;

         while(true) {
            int var6 = var4;
            if (var5 >= 4) {
               while(var6 < 4) {
                  this.appendCornerPath(var6, var3);
                  this.appendEdgePath(var6, var3);
                  ++var6;
               }

               var3.close();
               return;
            }

            this.setCornerPathAndTransform(var5, var1, var2);
            this.setEdgeTransform(var5, var1, var2);
            ++var5;
         }
      }
   }

   public ColorStateList getTintList() {
      return this.tintList;
   }

   public Region getTransparentRegion() {
      Rect var1 = this.getBounds();
      this.transparentRegion.set(var1);
      this.getPath(var1.width(), var1.height(), this.path);
      this.scratchRegion.setPath(this.path, this.transparentRegion);
      this.transparentRegion.op(this.scratchRegion, Op.DIFFERENCE);
      return this.transparentRegion;
   }

   public void setAlpha(int var1) {
      this.alpha = var1;
      this.invalidateSelf();
   }

   public void setColorFilter(ColorFilter var1) {
      this.paint.setColorFilter(var1);
      this.invalidateSelf();
   }

   public void setInterpolation(float var1) {
      this.interpolation = var1;
      this.invalidateSelf();
   }

   public void setTint(int var1) {
      this.setTintList(ColorStateList.valueOf(var1));
   }

   public void setTintList(ColorStateList var1) {
      this.tintList = var1;
      this.updateTintFilter();
      this.invalidateSelf();
   }

   public void setTintMode(Mode var1) {
      this.tintMode = var1;
      this.updateTintFilter();
      this.invalidateSelf();
   }
}
