package org.telegram.ui.Components.Paint;

import android.graphics.Matrix;
import android.view.MotionEvent;
import java.util.Vector;
import org.telegram.messenger.AndroidUtilities;

public class Input {
   private boolean beganDrawing;
   private boolean clearBuffer;
   private boolean hasMoved;
   private Matrix invertMatrix;
   private boolean isFirst;
   private Point lastLocation;
   private double lastRemainder;
   private Point[] points = new Point[3];
   private int pointsCount;
   private RenderView renderView;
   private float[] tempPoint = new float[2];

   public Input(RenderView var1) {
      this.renderView = var1;
   }

   private void paintPath(final Path var1) {
      var1.setup(this.renderView.getCurrentColor(), this.renderView.getCurrentWeight(), this.renderView.getCurrentBrush());
      if (this.clearBuffer) {
         this.lastRemainder = 0.0D;
      }

      var1.remainder = this.lastRemainder;
      this.renderView.getPainting().paintStroke(var1, this.clearBuffer, new Runnable() {
         public void run() {
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  <undefinedtype> var1x = <VAR_NAMELESS_ENCLOSURE>;
                  Input.this.lastRemainder = var1.remainder;
                  Input.this.clearBuffer = false;
               }
            });
         }
      });
   }

   private void reset() {
      this.pointsCount = 0;
   }

   private Point smoothPoint(Point var1, Point var2, Point var3, float var4) {
      float var5 = 1.0F - var4;
      double var6 = Math.pow((double)var5, 2.0D);
      double var8 = (double)(var5 * 2.0F * var4);
      double var10 = (double)(var4 * var4);
      double var12 = var1.x;
      double var14 = var3.x;
      Double.isNaN(var8);
      double var16 = var2.x;
      Double.isNaN(var10);
      double var18 = var1.y;
      double var20 = var3.y;
      Double.isNaN(var8);
      double var22 = var2.y;
      Double.isNaN(var10);
      return new Point(var12 * var6 + var14 * var8 + var16 * var10, var18 * var6 + var20 * var8 + var22 * var10, 1.0D);
   }

   private void smoothenAndPaintPoints(boolean var1) {
      int var2 = this.pointsCount;
      Point[] var11;
      if (var2 > 2) {
         Vector var3 = new Vector();
         Point[] var4 = this.points;
         Point var5 = var4[0];
         Point var6 = var4[1];
         Point var12 = var4[2];
         if (var12 == null || var6 == null || var5 == null) {
            return;
         }

         var5 = var6.multiplySum(var5, 0.5D);
         Point var7 = var12.multiplySum(var6, 0.5D);
         int var8 = (int)Math.min(48.0D, Math.max(Math.floor((double)(var5.getDistanceTo(var7) / (float)1)), 24.0D));
         float var9 = 1.0F / (float)var8;
         var2 = 0;

         for(float var10 = 0.0F; var2 < var8; ++var2) {
            var12 = this.smoothPoint(var5, var7, var6, var10);
            if (this.isFirst) {
               var12.edge = true;
               this.isFirst = false;
            }

            var3.add(var12);
            var10 += var9;
         }

         if (var1) {
            var7.edge = true;
         }

         var3.add(var7);
         Point[] var13 = new Point[var3.size()];
         var3.toArray(var13);
         this.paintPath(new Path(var13));
         var11 = this.points;
         System.arraycopy(var11, 1, var11, 0, 2);
         if (var1) {
            this.pointsCount = 0;
         } else {
            this.pointsCount = 2;
         }
      } else {
         var11 = new Point[var2];
         System.arraycopy(this.points, 0, var11, 0, var2);
         this.paintPath(new Path(var11));
      }

   }

   public void process(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      float var3 = var1.getX();
      float var4 = (float)this.renderView.getHeight();
      float var5 = var1.getY();
      float[] var7 = this.tempPoint;
      var7[0] = var3;
      var7[1] = var4 - var5;
      this.invertMatrix.mapPoints(var7);
      var7 = this.tempPoint;
      Point var6 = new Point((double)var7[0], (double)var7[1], 1.0D);
      if (var2 != 0) {
         if (var2 == 1) {
            if (!this.hasMoved) {
               if (this.renderView.shouldDraw()) {
                  var6.edge = true;
                  this.paintPath(new Path(var6));
               }

               this.reset();
            } else if (this.pointsCount > 0) {
               this.smoothenAndPaintPoints(true);
            }

            this.pointsCount = 0;
            this.renderView.getPainting().commitStroke(this.renderView.getCurrentColor());
            this.beganDrawing = false;
            this.renderView.onFinishedDrawing(this.hasMoved);
            return;
         }

         if (var2 != 2) {
            return;
         }
      }

      if (!this.beganDrawing) {
         this.beganDrawing = true;
         this.hasMoved = false;
         this.isFirst = true;
         this.lastLocation = var6;
         this.points[0] = var6;
         this.pointsCount = 1;
         this.clearBuffer = true;
      } else {
         if (var6.getDistanceTo(this.lastLocation) < (float)AndroidUtilities.dp(5.0F)) {
            return;
         }

         if (!this.hasMoved) {
            this.renderView.onBeganDrawing();
            this.hasMoved = true;
         }

         Point[] var8 = this.points;
         var2 = this.pointsCount;
         var8[var2] = var6;
         this.pointsCount = var2 + 1;
         if (this.pointsCount == 3) {
            this.smoothenAndPaintPoints(false);
         }

         this.lastLocation = var6;
      }

   }

   public void setMatrix(Matrix var1) {
      this.invertMatrix = new Matrix();
      var1.invert(this.invertMatrix);
   }
}
