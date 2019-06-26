package android.support.v4.app;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewTreeObserver.OnPreDrawListener;

class OneShotPreDrawListener implements OnPreDrawListener, OnAttachStateChangeListener {
   private final Runnable mRunnable;
   private final View mView;
   private ViewTreeObserver mViewTreeObserver;

   private OneShotPreDrawListener(View var1, Runnable var2) {
      this.mView = var1;
      this.mViewTreeObserver = var1.getViewTreeObserver();
      this.mRunnable = var2;
   }

   public static OneShotPreDrawListener add(View var0, Runnable var1) {
      OneShotPreDrawListener var2 = new OneShotPreDrawListener(var0, var1);
      var0.getViewTreeObserver().addOnPreDrawListener(var2);
      var0.addOnAttachStateChangeListener(var2);
      return var2;
   }

   public boolean onPreDraw() {
      this.removeListener();
      this.mRunnable.run();
      return true;
   }

   public void onViewAttachedToWindow(View var1) {
      this.mViewTreeObserver = var1.getViewTreeObserver();
   }

   public void onViewDetachedFromWindow(View var1) {
      this.removeListener();
   }

   public void removeListener() {
      if (this.mViewTreeObserver.isAlive()) {
         this.mViewTreeObserver.removeOnPreDrawListener(this);
      } else {
         this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
      }

      this.mView.removeOnAttachStateChangeListener(this);
   }
}
