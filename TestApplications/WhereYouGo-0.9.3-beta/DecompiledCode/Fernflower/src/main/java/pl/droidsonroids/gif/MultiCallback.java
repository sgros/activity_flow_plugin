package pl.droidsonroids.gif;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiCallback implements Callback {
   private final CopyOnWriteArrayList mCallbacks;
   private final boolean mUseViewInvalidate;

   public MultiCallback() {
      this(false);
   }

   public MultiCallback(boolean var1) {
      this.mCallbacks = new CopyOnWriteArrayList();
      this.mUseViewInvalidate = var1;
   }

   public void addView(Callback var1) {
      for(int var2 = 0; var2 < this.mCallbacks.size(); ++var2) {
         MultiCallback.CallbackWeakReference var3 = (MultiCallback.CallbackWeakReference)this.mCallbacks.get(var2);
         if ((Callback)var3.get() == null) {
            this.mCallbacks.remove(var3);
         }
      }

      this.mCallbacks.addIfAbsent(new MultiCallback.CallbackWeakReference(var1));
   }

   public void invalidateDrawable(@NonNull Drawable var1) {
      for(int var2 = 0; var2 < this.mCallbacks.size(); ++var2) {
         MultiCallback.CallbackWeakReference var3 = (MultiCallback.CallbackWeakReference)this.mCallbacks.get(var2);
         Callback var4 = (Callback)var3.get();
         if (var4 != null) {
            if (this.mUseViewInvalidate && var4 instanceof View) {
               ((View)var4).invalidate();
            } else {
               var4.invalidateDrawable(var1);
            }
         } else {
            this.mCallbacks.remove(var3);
         }
      }

   }

   public void removeView(Callback var1) {
      for(int var2 = 0; var2 < this.mCallbacks.size(); ++var2) {
         MultiCallback.CallbackWeakReference var3 = (MultiCallback.CallbackWeakReference)this.mCallbacks.get(var2);
         Callback var4 = (Callback)var3.get();
         if (var4 == null || var4 == var1) {
            this.mCallbacks.remove(var3);
         }
      }

   }

   public void scheduleDrawable(@NonNull Drawable var1, @NonNull Runnable var2, long var3) {
      for(int var5 = 0; var5 < this.mCallbacks.size(); ++var5) {
         MultiCallback.CallbackWeakReference var6 = (MultiCallback.CallbackWeakReference)this.mCallbacks.get(var5);
         Callback var7 = (Callback)var6.get();
         if (var7 != null) {
            var7.scheduleDrawable(var1, var2, var3);
         } else {
            this.mCallbacks.remove(var6);
         }
      }

   }

   public void unscheduleDrawable(@NonNull Drawable var1, @NonNull Runnable var2) {
      for(int var3 = 0; var3 < this.mCallbacks.size(); ++var3) {
         MultiCallback.CallbackWeakReference var4 = (MultiCallback.CallbackWeakReference)this.mCallbacks.get(var3);
         Callback var5 = (Callback)var4.get();
         if (var5 != null) {
            var5.unscheduleDrawable(var1, var2);
         } else {
            this.mCallbacks.remove(var4);
         }
      }

   }

   static final class CallbackWeakReference extends WeakReference {
      CallbackWeakReference(Callback var1) {
         super(var1);
      }

      public boolean equals(Object var1) {
         boolean var2 = true;
         if (this != var1) {
            if (var1 != null && this.getClass() == var1.getClass()) {
               if (this.get() != ((MultiCallback.CallbackWeakReference)var1).get()) {
                  var2 = false;
               }
            } else {
               var2 = false;
            }
         }

         return var2;
      }

      public int hashCode() {
         Callback var1 = (Callback)this.get();
         int var2;
         if (var1 != null) {
            var2 = var1.hashCode();
         } else {
            var2 = 0;
         }

         return var2;
      }
   }
}
