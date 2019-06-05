package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

public final class QuickSearchViewModel extends ViewModel {
   private final LiveData liveGlobal;
   private final LiveData liveLocale;
   private final MediatorLiveData quickSearchObservable;

   public QuickSearchViewModel(QuickSearchRepository var1) {
      Intrinsics.checkParameterIsNotNull(var1, "repository");
      super();
      this.quickSearchObservable = new MediatorLiveData();
      this.liveGlobal = var1.fetchGlobal();
      this.liveLocale = var1.fetchLocale();
      this.quickSearchObservable.addSource(this.liveGlobal, (Observer)(new Observer() {
         public final void onChanged(List var1) {
            QuickSearchViewModel.this.mergeEngines();
         }
      }));
      this.quickSearchObservable.addSource(this.liveLocale, (Observer)(new Observer() {
         public final void onChanged(List var1) {
            QuickSearchViewModel.this.mergeEngines();
         }
      }));
   }

   private final void mergeEngines() {
      ArrayList var1 = new ArrayList();
      List var2 = (List)this.liveGlobal.getValue();
      if (var2 != null) {
         var1.addAll((Collection)var2);
      }

      var2 = (List)this.liveLocale.getValue();
      if (var2 != null) {
         var1.addAll((Collection)var2);
      }

      this.quickSearchObservable.setValue(var1);
   }

   public final MediatorLiveData getQuickSearchObservable() {
      return this.quickSearchObservable;
   }
}
