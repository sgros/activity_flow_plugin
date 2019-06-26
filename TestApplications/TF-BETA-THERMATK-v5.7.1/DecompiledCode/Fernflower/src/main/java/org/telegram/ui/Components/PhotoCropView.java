package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Crop.CropRotationWheel;
import org.telegram.ui.Components.Crop.CropView;

public class PhotoCropView extends FrameLayout {
   private CropView cropView = new CropView(this.getContext());
   private PhotoCropView.PhotoCropViewDelegate delegate;
   private boolean showOnSetBitmap;
   private CropRotationWheel wheelView;

   public PhotoCropView(Context var1) {
      super(var1);
      this.cropView.setListener(new CropView.CropViewListener() {
         public void onAspectLock(boolean var1) {
            PhotoCropView.this.wheelView.setAspectLock(var1);
         }

         public void onChange(boolean var1) {
            if (PhotoCropView.this.delegate != null) {
               PhotoCropView.this.delegate.onChange(var1);
            }

         }
      });
      this.cropView.setBottomPadding((float)AndroidUtilities.dp(64.0F));
      this.addView(this.cropView);
      this.wheelView = new CropRotationWheel(this.getContext());
      this.wheelView.setListener(new CropRotationWheel.RotationWheelListener() {
         public void aspectRatioPressed() {
            PhotoCropView.this.cropView.showAspectRatioDialog();
         }

         public void onChange(float var1) {
            PhotoCropView.this.cropView.setRotation(var1);
            if (PhotoCropView.this.delegate != null) {
               PhotoCropView.this.delegate.onChange(false);
            }

         }

         public void onEnd(float var1) {
            PhotoCropView.this.cropView.onRotationEnded();
         }

         public void onStart() {
            PhotoCropView.this.cropView.onRotationBegan();
         }

         public void rotate90Pressed() {
            PhotoCropView.this.rotate();
         }
      });
      this.addView(this.wheelView, LayoutHelper.createFrame(-1, -2.0F, 81, 0.0F, 0.0F, 0.0F, 0.0F));
   }

   public Bitmap getBitmap() {
      CropView var1 = this.cropView;
      return var1 != null ? var1.getResult() : null;
   }

   public float getRectSizeX() {
      return this.cropView.getCropWidth();
   }

   public float getRectSizeY() {
      return this.cropView.getCropHeight();
   }

   public float getRectX() {
      return this.cropView.getCropLeft() - (float)AndroidUtilities.dp(14.0F);
   }

   public float getRectY() {
      float var1 = this.cropView.getCropTop();
      float var2 = (float)AndroidUtilities.dp(14.0F);
      int var3;
      if (VERSION.SDK_INT >= 21) {
         var3 = AndroidUtilities.statusBarHeight;
      } else {
         var3 = 0;
      }

      return var1 - var2 - (float)var3;
   }

   public void hideBackView() {
      this.cropView.hideBackView();
   }

   public boolean isReady() {
      return this.cropView.isReady();
   }

   public void onAppear() {
      this.cropView.willShow();
   }

   public void onAppeared() {
      CropView var1 = this.cropView;
      if (var1 != null) {
         var1.show();
      } else {
         this.showOnSetBitmap = true;
      }

   }

   public void onDisappear() {
      CropView var1 = this.cropView;
      if (var1 != null) {
         var1.hide();
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      CropView var6 = this.cropView;
      if (var6 != null) {
         var6.updateLayout();
      }

   }

   public void reset() {
      this.wheelView.reset();
      this.cropView.reset();
   }

   public void rotate() {
      CropRotationWheel var1 = this.wheelView;
      if (var1 != null) {
         var1.reset();
      }

      this.cropView.rotate90Degrees();
   }

   public void setAspectRatio(float var1) {
      this.cropView.setAspectRatio(var1);
   }

   public void setBitmap(Bitmap var1, int var2, boolean var3, boolean var4) {
      this.requestLayout();
      this.cropView.setBitmap(var1, var2, var3, var4);
      var4 = this.showOnSetBitmap;
      byte var6 = 0;
      if (var4) {
         this.showOnSetBitmap = false;
         this.cropView.show();
      }

      this.wheelView.setFreeform(var3);
      this.wheelView.reset();
      CropRotationWheel var5 = this.wheelView;
      if (!var3) {
         var6 = 4;
      }

      var5.setVisibility(var6);
   }

   public void setDelegate(PhotoCropView.PhotoCropViewDelegate var1) {
      this.delegate = var1;
   }

   public void setFreeform(boolean var1) {
      this.cropView.setFreeform(var1);
   }

   public void showBackView() {
      this.cropView.showBackView();
   }

   public interface PhotoCropViewDelegate {
      void onChange(boolean var1);
   }
}
