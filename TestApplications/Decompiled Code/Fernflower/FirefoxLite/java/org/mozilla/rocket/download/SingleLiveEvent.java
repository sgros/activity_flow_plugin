package org.mozilla.rocket.download;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.util.Log;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent extends MutableLiveData {
   private final AtomicBoolean mPending = new AtomicBoolean(false);

   public void observe(LifecycleOwner var1, final Observer var2) {
      if (this.hasActiveObservers()) {
         Log.w("SingleLiveEvent", "Multiple observers registered but only one will be notified of changes.");
      }

      super.observe(var1, new Observer() {
         public void onChanged(Object var1) {
            if (SingleLiveEvent.this.mPending.compareAndSet(true, false)) {
               var2.onChanged(var1);
            }

         }
      });
   }

   public void setValue(Object var1) {
      this.mPending.set(true);
      super.setValue(var1);
   }
}
