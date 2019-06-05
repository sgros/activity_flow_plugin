package com.bumptech.glide.request.target;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ViewTarget extends BaseTarget {
   private static boolean isTagUsedAtLeastOnce;
   private static Integer tagId;
   private final ViewTarget.SizeDeterminer sizeDeterminer;
   protected final View view;

   public ViewTarget(View var1) {
      this(var1, false);
   }

   public ViewTarget(View var1, boolean var2) {
      this.view = (View)Preconditions.checkNotNull(var1);
      this.sizeDeterminer = new ViewTarget.SizeDeterminer(var1, var2);
   }

   private Object getTag() {
      return tagId == null ? this.view.getTag() : this.view.getTag(tagId);
   }

   private void setTag(Object var1) {
      if (tagId == null) {
         isTagUsedAtLeastOnce = true;
         this.view.setTag(var1);
      } else {
         this.view.setTag(tagId, var1);
      }

   }

   public Request getRequest() {
      Object var1 = this.getTag();
      Request var2;
      if (var1 != null) {
         if (!(var1 instanceof Request)) {
            throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
         }

         var2 = (Request)var1;
      } else {
         var2 = null;
      }

      return var2;
   }

   public void getSize(SizeReadyCallback var1) {
      this.sizeDeterminer.getSize(var1);
   }

   public void onLoadCleared(Drawable var1) {
      super.onLoadCleared(var1);
      this.sizeDeterminer.clearCallbacksAndListener();
   }

   public void removeCallback(SizeReadyCallback var1) {
      this.sizeDeterminer.removeCallback(var1);
   }

   public void setRequest(Request var1) {
      this.setTag(var1);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Target for: ");
      var1.append(this.view);
      return var1.toString();
   }

   static final class SizeDeterminer {
      static Integer maxDisplayLength;
      private final List cbs = new ArrayList();
      private ViewTarget.SizeDeterminer.SizeDeterminerLayoutListener layoutListener;
      private final View view;
      private final boolean waitForLayout;

      SizeDeterminer(View var1, boolean var2) {
         this.view = var1;
         this.waitForLayout = var2;
      }

      private static int getMaxDisplayLength(Context var0) {
         if (maxDisplayLength == null) {
            Display var2 = ((WindowManager)var0.getSystemService("window")).getDefaultDisplay();
            Point var1 = new Point();
            var2.getSize(var1);
            maxDisplayLength = Math.max(var1.x, var1.y);
         }

         return maxDisplayLength;
      }

      private int getTargetDimen(int var1, int var2, int var3) {
         int var4 = var2 - var3;
         if (var4 > 0) {
            return var4;
         } else if (this.waitForLayout && this.view.isLayoutRequested()) {
            return 0;
         } else {
            var1 -= var3;
            if (var1 > 0) {
               return var1;
            } else if (!this.view.isLayoutRequested() && var2 == -2) {
               if (Log.isLoggable("ViewTarget", 4)) {
                  Log.i("ViewTarget", "Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use .override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions.");
               }

               return getMaxDisplayLength(this.view.getContext());
            } else {
               return 0;
            }
         }
      }

      private int getTargetHeight() {
         int var1 = this.view.getPaddingTop();
         int var2 = this.view.getPaddingBottom();
         LayoutParams var3 = this.view.getLayoutParams();
         int var4;
         if (var3 != null) {
            var4 = var3.height;
         } else {
            var4 = 0;
         }

         return this.getTargetDimen(this.view.getHeight(), var4, var1 + var2);
      }

      private int getTargetWidth() {
         int var1 = this.view.getPaddingLeft();
         int var2 = this.view.getPaddingRight();
         LayoutParams var3 = this.view.getLayoutParams();
         int var4;
         if (var3 != null) {
            var4 = var3.width;
         } else {
            var4 = 0;
         }

         return this.getTargetDimen(this.view.getWidth(), var4, var1 + var2);
      }

      private boolean isDimensionValid(int var1) {
         boolean var2;
         if (var1 <= 0 && var1 != Integer.MIN_VALUE) {
            var2 = false;
         } else {
            var2 = true;
         }

         return var2;
      }

      private boolean isViewStateAndSizeValid(int var1, int var2) {
         boolean var3;
         if (this.isDimensionValid(var1) && this.isDimensionValid(var2)) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      private void notifyCbs(int var1, int var2) {
         Iterator var3 = (new ArrayList(this.cbs)).iterator();

         while(var3.hasNext()) {
            ((SizeReadyCallback)var3.next()).onSizeReady(var1, var2);
         }

      }

      void checkCurrentDimens() {
         if (!this.cbs.isEmpty()) {
            int var1 = this.getTargetWidth();
            int var2 = this.getTargetHeight();
            if (this.isViewStateAndSizeValid(var1, var2)) {
               this.notifyCbs(var1, var2);
               this.clearCallbacksAndListener();
            }
         }
      }

      void clearCallbacksAndListener() {
         ViewTreeObserver var1 = this.view.getViewTreeObserver();
         if (var1.isAlive()) {
            var1.removeOnPreDrawListener(this.layoutListener);
         }

         this.layoutListener = null;
         this.cbs.clear();
      }

      void getSize(SizeReadyCallback var1) {
         int var2 = this.getTargetWidth();
         int var3 = this.getTargetHeight();
         if (this.isViewStateAndSizeValid(var2, var3)) {
            var1.onSizeReady(var2, var3);
         } else {
            if (!this.cbs.contains(var1)) {
               this.cbs.add(var1);
            }

            if (this.layoutListener == null) {
               ViewTreeObserver var4 = this.view.getViewTreeObserver();
               this.layoutListener = new ViewTarget.SizeDeterminer.SizeDeterminerLayoutListener(this);
               var4.addOnPreDrawListener(this.layoutListener);
            }

         }
      }

      void removeCallback(SizeReadyCallback var1) {
         this.cbs.remove(var1);
      }

      private static final class SizeDeterminerLayoutListener implements OnPreDrawListener {
         private final WeakReference sizeDeterminerRef;

         SizeDeterminerLayoutListener(ViewTarget.SizeDeterminer var1) {
            this.sizeDeterminerRef = new WeakReference(var1);
         }

         public boolean onPreDraw() {
            if (Log.isLoggable("ViewTarget", 2)) {
               StringBuilder var1 = new StringBuilder();
               var1.append("OnGlobalLayoutListener called listener=");
               var1.append(this);
               Log.v("ViewTarget", var1.toString());
            }

            ViewTarget.SizeDeterminer var2 = (ViewTarget.SizeDeterminer)this.sizeDeterminerRef.get();
            if (var2 != null) {
               var2.checkCurrentDimens();
            }

            return true;
         }
      }
   }
}
