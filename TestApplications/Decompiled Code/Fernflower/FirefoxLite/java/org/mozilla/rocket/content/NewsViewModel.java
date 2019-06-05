package org.mozilla.rocket.content;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.Repository;

public final class NewsViewModel extends ViewModel implements Repository.OnDataChangedListener {
   private final MutableLiveData items = new MutableLiveData();
   private Repository repository;

   public final MutableLiveData getItems() {
      return this.items;
   }

   public final void loadMore() {
      Repository var1 = this.repository;
      if (var1 != null) {
         var1.loadMore();
      }

   }

   public void onDataChanged(List var1) {
      this.items.setValue(var1);
   }

   public final void setRepository(Repository var1) {
      if (Intrinsics.areEqual(this.repository, var1) ^ true) {
         this.items.setValue((Object)null);
      }

      this.repository = var1;
   }
}
