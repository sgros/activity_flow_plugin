package org.mozilla.rocket.privately;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import kotlin.jvm.internal.Intrinsics;

public final class SharedViewModel extends ViewModel {
   private MutableLiveData isUrlInputShowing = new MutableLiveData();
   private final MutableLiveData url = new MutableLiveData();

   public final LiveData getUrl() {
      return (LiveData)this.url;
   }

   public final void setUrl(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "newUrl");
      this.url.setValue(var1);
   }

   public final MutableLiveData urlInputState() {
      return this.isUrlInputShowing;
   }
}
