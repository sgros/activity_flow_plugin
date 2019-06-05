package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback {
   private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
   private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
   private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
   private static int MAX_IMAGE_SIZE;
   private Matrix mTempMatrix;

   private static Bitmap createDrawableBitmap(Drawable var0) {
      int var1 = var0.getIntrinsicWidth();
      int var2 = var0.getIntrinsicHeight();
      if (var1 > 0 && var2 > 0) {
         float var3 = Math.min(1.0F, (float)MAX_IMAGE_SIZE / (float)(var1 * var2));
         if (var0 instanceof BitmapDrawable && var3 == 1.0F) {
            return ((BitmapDrawable)var0).getBitmap();
         } else {
            var1 = (int)((float)var1 * var3);
            int var4 = (int)((float)var2 * var3);
            Bitmap var5 = Bitmap.createBitmap(var1, var4, Config.ARGB_8888);
            Canvas var6 = new Canvas(var5);
            Rect var7 = var0.getBounds();
            var2 = var7.left;
            int var8 = var7.top;
            int var9 = var7.right;
            int var10 = var7.bottom;
            var0.setBounds(0, 0, var1, var4);
            var0.draw(var6);
            var0.setBounds(var2, var8, var9, var10);
            return var5;
         }
      } else {
         return null;
      }
   }

   public Parcelable onCaptureSharedElementSnapshot(View var1, Matrix var2, RectF var3) {
      Drawable var6;
      if (var1 instanceof ImageView) {
         ImageView var4 = (ImageView)var1;
         Drawable var5 = var4.getDrawable();
         var6 = var4.getBackground();
         if (var5 != null && var6 == null) {
            Bitmap var15 = createDrawableBitmap(var5);
            if (var15 != null) {
               Bundle var10 = new Bundle();
               var10.putParcelable("sharedElement:snapshot:bitmap", var15);
               var10.putString("sharedElement:snapshot:imageScaleType", var4.getScaleType().toString());
               if (var4.getScaleType() == ScaleType.MATRIX) {
                  Matrix var13 = var4.getImageMatrix();
                  float[] var12 = new float[9];
                  var13.getValues(var12);
                  var10.putFloatArray("sharedElement:snapshot:imageMatrix", var12);
               }

               return var10;
            }
         }
      }

      int var7 = Math.round(var3.width());
      int var8 = Math.round(var3.height());
      var6 = null;
      Bitmap var14 = var6;
      if (var7 > 0) {
         var14 = var6;
         if (var8 > 0) {
            float var9 = Math.min(1.0F, (float)MAX_IMAGE_SIZE / (float)(var7 * var8));
            var7 = (int)((float)var7 * var9);
            var8 = (int)((float)var8 * var9);
            if (this.mTempMatrix == null) {
               this.mTempMatrix = new Matrix();
            }

            this.mTempMatrix.set(var2);
            this.mTempMatrix.postTranslate(-var3.left, -var3.top);
            this.mTempMatrix.postScale(var9, var9);
            var14 = Bitmap.createBitmap(var7, var8, Config.ARGB_8888);
            Canvas var11 = new Canvas(var14);
            var11.concat(this.mTempMatrix);
            var1.draw(var11);
         }
      }

      return var14;
   }

   public View onCreateSnapshotView(Context var1, Parcelable var2) {
      boolean var3 = var2 instanceof Bundle;
      ImageView var4 = null;
      if (var3) {
         Bundle var6 = (Bundle)var2;
         Bitmap var9 = (Bitmap)var6.getParcelable("sharedElement:snapshot:bitmap");
         if (var9 == null) {
            return null;
         }

         ImageView var5 = new ImageView(var1);
         var5.setImageBitmap(var9);
         var5.setScaleType(ScaleType.valueOf(var6.getString("sharedElement:snapshot:imageScaleType")));
         var4 = var5;
         if (var5.getScaleType() == ScaleType.MATRIX) {
            float[] var7 = var6.getFloatArray("sharedElement:snapshot:imageMatrix");
            Matrix var10 = new Matrix();
            var10.setValues(var7);
            var5.setImageMatrix(var10);
            var4 = var5;
         }
      } else if (var2 instanceof Bitmap) {
         Bitmap var8 = (Bitmap)var2;
         var4 = new ImageView(var1);
         var4.setImageBitmap(var8);
      }

      return var4;
   }

   public void onMapSharedElements(List var1, Map var2) {
   }

   public void onRejectSharedElements(List var1) {
   }

   public void onSharedElementEnd(List var1, List var2, List var3) {
   }

   public void onSharedElementStart(List var1, List var2, List var3) {
   }

   public void onSharedElementsArrived(List var1, List var2, SharedElementCallback.OnSharedElementsReadyListener var3) {
      var3.onSharedElementsReady();
   }

   public interface OnSharedElementsReadyListener {
      void onSharedElementsReady();
   }
}
