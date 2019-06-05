package android.support.v7.widget;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnLongClickListener;
import android.view.accessibility.AccessibilityManager;

class TooltipCompatHandler implements OnAttachStateChangeListener, OnHoverListener, OnLongClickListener {
   private static TooltipCompatHandler sActiveHandler;
   private static TooltipCompatHandler sPendingHandler;
   private final View mAnchor;
   private int mAnchorX;
   private int mAnchorY;
   private boolean mFromTouch;
   private final Runnable mHideRunnable = new Runnable() {
      public void run() {
         TooltipCompatHandler.this.hide();
      }
   };
   private final int mHoverSlop;
   private TooltipPopup mPopup;
   private final Runnable mShowRunnable = new Runnable() {
      public void run() {
         TooltipCompatHandler.this.show(false);
      }
   };
   private final CharSequence mTooltipText;

   private TooltipCompatHandler(View var1, CharSequence var2) {
      this.mAnchor = var1;
      this.mTooltipText = var2;
      this.mHoverSlop = ViewConfigurationCompat.getScaledHoverSlop(ViewConfiguration.get(this.mAnchor.getContext()));
      this.clearAnchorPos();
      this.mAnchor.setOnLongClickListener(this);
      this.mAnchor.setOnHoverListener(this);
   }

   private void cancelPendingShow() {
      this.mAnchor.removeCallbacks(this.mShowRunnable);
   }

   private void clearAnchorPos() {
      this.mAnchorX = Integer.MAX_VALUE;
      this.mAnchorY = Integer.MAX_VALUE;
   }

   private void scheduleShow() {
      this.mAnchor.postDelayed(this.mShowRunnable, (long)ViewConfiguration.getLongPressTimeout());
   }

   private static void setPendingHandler(TooltipCompatHandler var0) {
      if (sPendingHandler != null) {
         sPendingHandler.cancelPendingShow();
      }

      sPendingHandler = var0;
      if (sPendingHandler != null) {
         sPendingHandler.scheduleShow();
      }

   }

   public static void setTooltipText(View var0, CharSequence var1) {
      if (sPendingHandler != null && sPendingHandler.mAnchor == var0) {
         setPendingHandler((TooltipCompatHandler)null);
      }

      if (TextUtils.isEmpty(var1)) {
         if (sActiveHandler != null && sActiveHandler.mAnchor == var0) {
            sActiveHandler.hide();
         }

         var0.setOnLongClickListener((OnLongClickListener)null);
         var0.setLongClickable(false);
         var0.setOnHoverListener((OnHoverListener)null);
      } else {
         new TooltipCompatHandler(var0, var1);
      }

   }

   private boolean updateAnchorPos(MotionEvent var1) {
      int var2 = (int)var1.getX();
      int var3 = (int)var1.getY();
      if (Math.abs(var2 - this.mAnchorX) <= this.mHoverSlop && Math.abs(var3 - this.mAnchorY) <= this.mHoverSlop) {
         return false;
      } else {
         this.mAnchorX = var2;
         this.mAnchorY = var3;
         return true;
      }
   }

   void hide() {
      if (sActiveHandler == this) {
         sActiveHandler = null;
         if (this.mPopup != null) {
            this.mPopup.hide();
            this.mPopup = null;
            this.clearAnchorPos();
            this.mAnchor.removeOnAttachStateChangeListener(this);
         } else {
            Log.e("TooltipCompatHandler", "sActiveHandler.mPopup == null");
         }
      }

      if (sPendingHandler == this) {
         setPendingHandler((TooltipCompatHandler)null);
      }

      this.mAnchor.removeCallbacks(this.mHideRunnable);
   }

   public boolean onHover(View var1, MotionEvent var2) {
      if (this.mPopup != null && this.mFromTouch) {
         return false;
      } else {
         AccessibilityManager var4 = (AccessibilityManager)this.mAnchor.getContext().getSystemService("accessibility");
         if (var4.isEnabled() && var4.isTouchExplorationEnabled()) {
            return false;
         } else {
            int var3 = var2.getAction();
            if (var3 != 7) {
               if (var3 == 10) {
                  this.clearAnchorPos();
                  this.hide();
               }
            } else if (this.mAnchor.isEnabled() && this.mPopup == null && this.updateAnchorPos(var2)) {
               setPendingHandler(this);
            }

            return false;
         }
      }
   }

   public boolean onLongClick(View var1) {
      this.mAnchorX = var1.getWidth() / 2;
      this.mAnchorY = var1.getHeight() / 2;
      this.show(true);
      return true;
   }

   public void onViewAttachedToWindow(View var1) {
   }

   public void onViewDetachedFromWindow(View var1) {
      this.hide();
   }

   void show(boolean var1) {
      if (ViewCompat.isAttachedToWindow(this.mAnchor)) {
         setPendingHandler((TooltipCompatHandler)null);
         if (sActiveHandler != null) {
            sActiveHandler.hide();
         }

         sActiveHandler = this;
         this.mFromTouch = var1;
         this.mPopup = new TooltipPopup(this.mAnchor.getContext());
         this.mPopup.show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
         this.mAnchor.addOnAttachStateChangeListener(this);
         long var2;
         if (this.mFromTouch) {
            var2 = 2500L;
         } else if ((ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 1) == 1) {
            var2 = 3000L - (long)ViewConfiguration.getLongPressTimeout();
         } else {
            var2 = 15000L - (long)ViewConfiguration.getLongPressTimeout();
         }

         this.mAnchor.removeCallbacks(this.mHideRunnable);
         this.mAnchor.postDelayed(this.mHideRunnable, var2);
      }
   }
}
