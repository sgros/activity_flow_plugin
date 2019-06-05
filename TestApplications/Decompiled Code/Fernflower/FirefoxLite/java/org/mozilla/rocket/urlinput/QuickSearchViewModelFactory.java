package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import kotlin.jvm.internal.Intrinsics;

public final class QuickSearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {
   private final QuickSearchRepository repository;

   public QuickSearchViewModelFactory(QuickSearchRepository var1) {
      Intrinsics.checkParameterIsNotNull(var1, "repository");
      super();
      this.repository = var1;
   }

   public ViewModel create(Class var1) {
      Intrinsics.checkParameterIsNotNull(var1, "modelClass");
      return (ViewModel)(new QuickSearchViewModel(this.repository));
   }
}
