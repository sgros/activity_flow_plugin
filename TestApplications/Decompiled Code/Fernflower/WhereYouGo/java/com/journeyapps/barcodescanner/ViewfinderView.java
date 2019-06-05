package com.journeyapps.barcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewfinderView extends View {
   protected static final long ANIMATION_DELAY = 80L;
   protected static final int CURRENT_POINT_OPACITY = 160;
   protected static final int MAX_RESULT_POINTS = 20;
   protected static final int POINT_SIZE = 6;
   protected static final int[] SCANNER_ALPHA = new int[]{0, 64, 128, 192, 255, 192, 128, 64};
   protected static final String TAG = ViewfinderView.class.getSimpleName();
   protected CameraPreview cameraPreview;
   protected Rect framingRect;
   protected final int laserColor;
   protected List lastPossibleResultPoints;
   protected final int maskColor;
   protected final Paint paint = new Paint(1);
   protected List possibleResultPoints;
   protected Rect previewFramingRect;
   protected Bitmap resultBitmap;
   protected final int resultColor;
   protected final int resultPointColor;
   protected int scannerAlpha;

   public ViewfinderView(Context var1, AttributeSet var2) {
      super(var1, var2);
      Resources var3 = this.getResources();
      TypedArray var4 = this.getContext().obtainStyledAttributes(var2, R.styleable.zxing_finder);
      this.maskColor = var4.getColor(R.styleable.zxing_finder_zxing_viewfinder_mask, var3.getColor(R.color.zxing_viewfinder_mask));
      this.resultColor = var4.getColor(R.styleable.zxing_finder_zxing_result_view, var3.getColor(R.color.zxing_result_view));
      this.laserColor = var4.getColor(R.styleable.zxing_finder_zxing_viewfinder_laser, var3.getColor(R.color.zxing_viewfinder_laser));
      this.resultPointColor = var4.getColor(R.styleable.zxing_finder_zxing_possible_result_points, var3.getColor(R.color.zxing_possible_result_points));
      var4.recycle();
      this.scannerAlpha = 0;
      this.possibleResultPoints = new ArrayList(5);
      this.lastPossibleResultPoints = null;
   }

   public void addPossibleResultPoint(ResultPoint var1) {
      List var2 = this.possibleResultPoints;
      var2.add(var1);
      int var3 = var2.size();
      if (var3 > 20) {
         var2.subList(0, var3 - 10).clear();
      }

   }

   public void drawResultBitmap(Bitmap var1) {
      this.resultBitmap = var1;
      this.invalidate();
   }

   public void drawViewfinder() {
      Bitmap var1 = this.resultBitmap;
      this.resultBitmap = null;
      if (var1 != null) {
         var1.recycle();
      }

      this.invalidate();
   }

   @SuppressLint({"DrawAllocation"})
   public void onDraw(Canvas var1) {
      this.refreshSizes();
      if (this.framingRect != null && this.previewFramingRect != null) {
         Rect var2 = this.framingRect;
         Rect var3 = this.previewFramingRect;
         int var4 = var1.getWidth();
         int var5 = var1.getHeight();
         Paint var6 = this.paint;
         int var7;
         if (this.resultBitmap != null) {
            var7 = this.resultColor;
         } else {
            var7 = this.maskColor;
         }

         var6.setColor(var7);
         var1.drawRect(0.0F, 0.0F, (float)var4, (float)var2.top, this.paint);
         var1.drawRect(0.0F, (float)var2.top, (float)var2.left, (float)(var2.bottom + 1), this.paint);
         var1.drawRect((float)(var2.right + 1), (float)var2.top, (float)var4, (float)(var2.bottom + 1), this.paint);
         var1.drawRect(0.0F, (float)(var2.bottom + 1), (float)var4, (float)var5, this.paint);
         if (this.resultBitmap != null) {
            this.paint.setAlpha(160);
            var1.drawBitmap(this.resultBitmap, (Rect)null, var2, this.paint);
         } else {
            this.paint.setColor(this.laserColor);
            this.paint.setAlpha(SCANNER_ALPHA[this.scannerAlpha]);
            this.scannerAlpha = (this.scannerAlpha + 1) % SCANNER_ALPHA.length;
            var7 = var2.height() / 2 + var2.top;
            var1.drawRect((float)(var2.left + 2), (float)(var7 - 1), (float)(var2.right - 1), (float)(var7 + 2), this.paint);
            float var8 = (float)var2.width() / (float)var3.width();
            float var9 = (float)var2.height() / (float)var3.height();
            List var11 = this.possibleResultPoints;
            List var14 = this.lastPossibleResultPoints;
            var4 = var2.left;
            var7 = var2.top;
            if (var11.isEmpty()) {
               this.lastPossibleResultPoints = null;
            } else {
               this.possibleResultPoints = new ArrayList(5);
               this.lastPossibleResultPoints = var11;
               this.paint.setAlpha(160);
               this.paint.setColor(this.resultPointColor);
               Iterator var12 = var11.iterator();

               while(var12.hasNext()) {
                  ResultPoint var10 = (ResultPoint)var12.next();
                  var1.drawCircle((float)((int)(var10.getX() * var8) + var4), (float)((int)(var10.getY() * var9) + var7), 6.0F, this.paint);
               }
            }

            if (var14 != null) {
               this.paint.setAlpha(80);
               this.paint.setColor(this.resultPointColor);
               Iterator var15 = var14.iterator();

               while(var15.hasNext()) {
                  ResultPoint var13 = (ResultPoint)var15.next();
                  var1.drawCircle((float)((int)(var13.getX() * var8) + var4), (float)((int)(var13.getY() * var9) + var7), 3.0F, this.paint);
               }
            }

            this.postInvalidateDelayed(80L, var2.left - 6, var2.top - 6, var2.right + 6, var2.bottom + 6);
         }
      }

   }

   protected void refreshSizes() {
      if (this.cameraPreview != null) {
         Rect var1 = this.cameraPreview.getFramingRect();
         Rect var2 = this.cameraPreview.getPreviewFramingRect();
         if (var1 != null && var2 != null) {
            this.framingRect = var1;
            this.previewFramingRect = var2;
         }
      }

   }

   public void setCameraPreview(CameraPreview var1) {
      this.cameraPreview = var1;
      var1.addStateListener(new CameraPreview.StateListener() {
         public void cameraClosed() {
         }

         public void cameraError(Exception var1) {
         }

         public void previewSized() {
            ViewfinderView.this.refreshSizes();
            ViewfinderView.this.invalidate();
         }

         public void previewStarted() {
         }

         public void previewStopped() {
         }
      });
   }
}
