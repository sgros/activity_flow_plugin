package android.support.transition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@RequiresApi(14)
class ViewOverlayApi14 implements ViewOverlayImpl {
   protected ViewOverlayApi14.OverlayViewGroup mOverlayViewGroup;

   ViewOverlayApi14(Context var1, ViewGroup var2, View var3) {
      this.mOverlayViewGroup = new ViewOverlayApi14.OverlayViewGroup(var1, var2, var3, this);
   }

   static ViewOverlayApi14 createFrom(View var0) {
      ViewGroup var1 = getContentView(var0);
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = var1.getChildAt(var3);
            if (var4 instanceof ViewOverlayApi14.OverlayViewGroup) {
               return ((ViewOverlayApi14.OverlayViewGroup)var4).mViewOverlay;
            }
         }

         return new ViewGroupOverlayApi14(var1.getContext(), var1, var0);
      } else {
         return null;
      }
   }

   static ViewGroup getContentView(View var0) {
      while(var0 != null) {
         if (((View)var0).getId() == 16908290 && var0 instanceof ViewGroup) {
            return (ViewGroup)var0;
         }

         if (((View)var0).getParent() instanceof ViewGroup) {
            var0 = (ViewGroup)((View)var0).getParent();
         }
      }

      return null;
   }

   public void add(@NonNull Drawable var1) {
      this.mOverlayViewGroup.add(var1);
   }

   public void clear() {
      this.mOverlayViewGroup.clear();
   }

   ViewGroup getOverlayView() {
      return this.mOverlayViewGroup;
   }

   boolean isEmpty() {
      return this.mOverlayViewGroup.isEmpty();
   }

   public void remove(@NonNull Drawable var1) {
      this.mOverlayViewGroup.remove(var1);
   }

   static class OverlayViewGroup extends ViewGroup {
      static Method sInvalidateChildInParentFastMethod;
      ArrayList mDrawables = null;
      ViewGroup mHostView;
      View mRequestingView;
      ViewOverlayApi14 mViewOverlay;

      static {
         try {
            sInvalidateChildInParentFastMethod = ViewGroup.class.getDeclaredMethod("invalidateChildInParentFast", Integer.TYPE, Integer.TYPE, Rect.class);
         } catch (NoSuchMethodException var1) {
         }

      }

      OverlayViewGroup(Context var1, ViewGroup var2, View var3, ViewOverlayApi14 var4) {
         super(var1);
         this.mHostView = var2;
         this.mRequestingView = var3;
         this.setRight(var2.getWidth());
         this.setBottom(var2.getHeight());
         var2.addView(this);
         this.mViewOverlay = var4;
      }

      private void getOffset(int[] var1) {
         int[] var2 = new int[2];
         int[] var3 = new int[2];
         this.mHostView.getLocationOnScreen(var2);
         this.mRequestingView.getLocationOnScreen(var3);
         var1[0] = var3[0] - var2[0];
         var1[1] = var3[1] - var2[1];
      }

      public void add(Drawable var1) {
         if (this.mDrawables == null) {
            this.mDrawables = new ArrayList();
         }

         if (!this.mDrawables.contains(var1)) {
            this.mDrawables.add(var1);
            this.invalidate(var1.getBounds());
            var1.setCallback(this);
         }

      }

      public void add(View var1) {
         if (var1.getParent() instanceof ViewGroup) {
            ViewGroup var2 = (ViewGroup)var1.getParent();
            if (var2 != this.mHostView && var2.getParent() != null && ViewCompat.isAttachedToWindow(var2)) {
               int[] var3 = new int[2];
               int[] var4 = new int[2];
               var2.getLocationOnScreen(var3);
               this.mHostView.getLocationOnScreen(var4);
               ViewCompat.offsetLeftAndRight(var1, var3[0] - var4[0]);
               ViewCompat.offsetTopAndBottom(var1, var3[1] - var4[1]);
            }

            var2.removeView(var1);
            if (var1.getParent() != null) {
               var2.removeView(var1);
            }
         }

         super.addView(var1, this.getChildCount() - 1);
      }

      public void clear() {
         this.removeAllViews();
         if (this.mDrawables != null) {
            this.mDrawables.clear();
         }

      }

      protected void dispatchDraw(Canvas var1) {
         int[] var2 = new int[2];
         int[] var3 = new int[2];
         this.mHostView.getLocationOnScreen(var2);
         this.mRequestingView.getLocationOnScreen(var3);
         int var4 = 0;
         var1.translate((float)(var3[0] - var2[0]), (float)(var3[1] - var2[1]));
         var1.clipRect(new Rect(0, 0, this.mRequestingView.getWidth(), this.mRequestingView.getHeight()));
         super.dispatchDraw(var1);
         int var5;
         if (this.mDrawables == null) {
            var5 = 0;
         } else {
            var5 = this.mDrawables.size();
         }

         while(var4 < var5) {
            ((Drawable)this.mDrawables.get(var4)).draw(var1);
            ++var4;
         }

      }

      public boolean dispatchTouchEvent(MotionEvent var1) {
         return false;
      }

      public void invalidateChildFast(View var1, Rect var2) {
         if (this.mHostView != null) {
            int var3 = var1.getLeft();
            int var4 = var1.getTop();
            int[] var5 = new int[2];
            this.getOffset(var5);
            var2.offset(var3 + var5[0], var4 + var5[1]);
            this.mHostView.invalidate(var2);
         }

      }

      public ViewParent invalidateChildInParent(int[] var1, Rect var2) {
         if (this.mHostView != null) {
            var2.offset(var1[0], var1[1]);
            if (this.mHostView instanceof ViewGroup) {
               var1[0] = 0;
               var1[1] = 0;
               int[] var3 = new int[2];
               this.getOffset(var3);
               var2.offset(var3[0], var3[1]);
               return super.invalidateChildInParent(var1, var2);
            }

            this.invalidate(var2);
         }

         return null;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      protected ViewParent invalidateChildInParentFast(int var1, int var2, Rect var3) {
         if (this.mHostView instanceof ViewGroup && sInvalidateChildInParentFastMethod != null) {
            try {
               this.getOffset(new int[2]);
               sInvalidateChildInParentFastMethod.invoke(this.mHostView, var1, var2, var3);
            } catch (IllegalAccessException var4) {
               var4.printStackTrace();
            } catch (InvocationTargetException var5) {
               var5.printStackTrace();
            }
         }

         return null;
      }

      public void invalidateDrawable(@NonNull Drawable var1) {
         this.invalidate(var1.getBounds());
      }

      boolean isEmpty() {
         boolean var1;
         if (this.getChildCount() != 0 || this.mDrawables != null && this.mDrawables.size() != 0) {
            var1 = false;
         } else {
            var1 = true;
         }

         return var1;
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      }

      public void remove(Drawable var1) {
         if (this.mDrawables != null) {
            this.mDrawables.remove(var1);
            this.invalidate(var1.getBounds());
            var1.setCallback((Callback)null);
         }

      }

      public void remove(View var1) {
         super.removeView(var1);
         if (this.isEmpty()) {
            this.mHostView.removeView(this);
         }

      }

      protected boolean verifyDrawable(@NonNull Drawable var1) {
         boolean var2;
         if (super.verifyDrawable(var1) || this.mDrawables != null && this.mDrawables.contains(var1)) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      static class TouchInterceptor extends View {
         TouchInterceptor(Context var1) {
            super(var1);
         }
      }
   }
}
