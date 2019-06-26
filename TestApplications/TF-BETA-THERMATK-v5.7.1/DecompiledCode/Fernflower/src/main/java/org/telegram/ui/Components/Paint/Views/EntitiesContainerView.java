package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.FrameLayout;

public class EntitiesContainerView extends FrameLayout implements OnScaleGestureListener, RotationGestureDetector.OnRotationGestureListener {
   private EntitiesContainerView.EntitiesContainerViewDelegate delegate;
   private ScaleGestureDetector gestureDetector;
   private boolean hasTransformed;
   private float previousAngle;
   private float previousScale = 1.0F;
   private RotationGestureDetector rotationGestureDetector;

   public EntitiesContainerView(Context var1, EntitiesContainerView.EntitiesContainerViewDelegate var2) {
      super(var1);
      this.gestureDetector = new ScaleGestureDetector(var1, this);
      this.rotationGestureDetector = new RotationGestureDetector(this);
      this.delegate = var2;
   }

   public void bringViewToFront(EntityView var1) {
      if (this.indexOfChild(var1) != this.getChildCount() - 1) {
         this.removeView(var1);
         this.addView(var1, this.getChildCount());
      }

   }

   public int entitiesCount() {
      int var1 = 0;

      int var2;
      for(var2 = 0; var1 < this.getChildCount(); ++var1) {
         if (this.getChildAt(var1) instanceof EntityView) {
            ++var2;
         }
      }

      return var2;
   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      boolean var2;
      if (var1.getPointerCount() == 2 && this.delegate.shouldReceiveTouches()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void onRotation(RotationGestureDetector var1) {
      EntityView var2 = this.delegate.onSelectedEntityRequest();
      float var3 = var1.getAngle();
      float var4 = this.previousAngle;
      var2.rotate(var2.getRotation() + (var4 - var3));
      this.previousAngle = var3;
   }

   public void onRotationBegin(RotationGestureDetector var1) {
      this.previousAngle = var1.getStartAngle();
      this.hasTransformed = true;
   }

   public void onRotationEnd(RotationGestureDetector var1) {
   }

   public boolean onScale(ScaleGestureDetector var1) {
      float var2 = var1.getScaleFactor();
      float var3 = var2 / this.previousScale;
      this.delegate.onSelectedEntityRequest().scale(var3);
      this.previousScale = var2;
      return false;
   }

   public boolean onScaleBegin(ScaleGestureDetector var1) {
      this.previousScale = 1.0F;
      this.hasTransformed = true;
      return true;
   }

   public void onScaleEnd(ScaleGestureDetector var1) {
   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (this.delegate.onSelectedEntityRequest() == null) {
         return false;
      } else {
         if (var1.getPointerCount() == 1) {
            int var2 = var1.getActionMasked();
            if (var2 == 0) {
               this.hasTransformed = false;
            } else if (var2 == 1 || var2 == 2) {
               if (!this.hasTransformed) {
                  EntitiesContainerView.EntitiesContainerViewDelegate var3 = this.delegate;
                  if (var3 != null) {
                     var3.onEntityDeselect();
                  }
               }

               return false;
            }
         }

         this.gestureDetector.onTouchEvent(var1);
         this.rotationGestureDetector.onTouchEvent(var1);
         return true;
      }
   }

   public interface EntitiesContainerViewDelegate {
      void onEntityDeselect();

      EntityView onSelectedEntityRequest();

      boolean shouldReceiveTouches();
   }
}
