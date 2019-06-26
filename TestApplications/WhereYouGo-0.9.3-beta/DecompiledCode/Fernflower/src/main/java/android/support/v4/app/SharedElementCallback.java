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
   private static int MAX_IMAGE_SIZE = 1048576;
   private Matrix mTempMatrix;

   private static Bitmap createDrawableBitmap(Drawable var0) {
      int var1 = var0.getIntrinsicWidth();
      int var2 = var0.getIntrinsicHeight();
      Bitmap var11;
      if (var1 > 0 && var2 > 0) {
         float var3 = Math.min(1.0F, (float)MAX_IMAGE_SIZE / (float)(var1 * var2));
         if (var0 instanceof BitmapDrawable && var3 == 1.0F) {
            var11 = ((BitmapDrawable)var0).getBitmap();
         } else {
            var1 = (int)((float)var1 * var3);
            int var4 = (int)((float)var2 * var3);
            Bitmap var5 = Bitmap.createBitmap(var1, var4, Config.ARGB_8888);
            Canvas var6 = new Canvas(var5);
            Rect var7 = var0.getBounds();
            int var8 = var7.left;
            int var9 = var7.top;
            int var10 = var7.right;
            var2 = var7.bottom;
            var0.setBounds(0, 0, var1, var4);
            var0.draw(var6);
            var0.setBounds(var8, var9, var10, var2);
            var11 = var5;
         }
      } else {
         var11 = null;
      }

      return var11;
   }

   public Parcelable onCaptureSharedElementSnapshot(View var1, Matrix var2, RectF var3) {
      Drawable var5;
      Object var10;
      if (var1 instanceof ImageView) {
         ImageView var4 = (ImageView)var1;
         var5 = var4.getDrawable();
         Drawable var6 = var4.getBackground();
         if (var5 != null && var6 == null) {
            Bitmap var16 = createDrawableBitmap(var5);
            if (var16 != null) {
               Bundle var13 = new Bundle();
               var13.putParcelable("sharedElement:snapshot:bitmap", var16);
               var13.putString("sharedElement:snapshot:imageScaleType", var4.getScaleType().toString());
               var10 = var13;
               if (var4.getScaleType() == ScaleType.MATRIX) {
                  Matrix var14 = var4.getImageMatrix();
                  float[] var11 = new float[9];
                  var14.getValues(var11);
                  var13.putFloatArray("sharedElement:snapshot:imageMatrix", var11);
                  var10 = var13;
               }

               return (Parcelable)var10;
            }
         }
      }

      int var7 = Math.round(var3.width());
      int var8 = Math.round(var3.height());
      var5 = null;
      Bitmap var15 = var5;
      if (var7 > 0) {
         var15 = var5;
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
            var15 = Bitmap.createBitmap(var7, var8, Config.ARGB_8888);
            Canvas var12 = new Canvas(var15);
            var12.concat(this.mTempMatrix);
            var1.draw(var12);
         }
      }

      var10 = var15;
      return (Parcelable)var10;
   }

   public View onCreateSnapshotView(Context var1, Parcelable var2) {
      ImageView var3 = null;
      ImageView var5;
      if (var2 instanceof Bundle) {
         Bundle var4 = (Bundle)var2;
         Bitmap var8 = (Bitmap)var4.getParcelable("sharedElement:snapshot:bitmap");
         if (var8 == null) {
            var5 = null;
            return var5;
         }

         ImageView var6 = new ImageView(var1);
         var6.setImageBitmap(var8);
         var6.setScaleType(ScaleType.valueOf(var4.getString("sharedElement:snapshot:imageScaleType")));
         var3 = var6;
         if (var6.getScaleType() == ScaleType.MATRIX) {
            float[] var9 = var4.getFloatArray("sharedElement:snapshot:imageMatrix");
            Matrix var10 = new Matrix();
            var10.setValues(var9);
            var6.setImageMatrix(var10);
            var3 = var6;
         }
      } else if (var2 instanceof Bitmap) {
         Bitmap var7 = (Bitmap)var2;
         var3 = new ImageView(var1);
         var3.setImageBitmap(var7);
      }

      var5 = var3;
      return var5;
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
