package pl.droidsonroids.gif;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.Iterator;

class InvalidationHandler extends Handler {
   static final int MSG_TYPE_INVALIDATION = -1;
   private final WeakReference mDrawableRef;

   public InvalidationHandler(GifDrawable var1) {
      super(Looper.getMainLooper());
      this.mDrawableRef = new WeakReference(var1);
   }

   public void handleMessage(Message var1) {
      GifDrawable var2 = (GifDrawable)this.mDrawableRef.get();
      if (var2 != null) {
         if (var1.what == -1) {
            var2.invalidateSelf();
         } else {
            Iterator var3 = var2.mListeners.iterator();

            while(var3.hasNext()) {
               ((AnimationListener)var3.next()).onAnimationCompleted(var1.what);
            }
         }
      }

   }
}
