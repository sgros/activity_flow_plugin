package org.mozilla.cachedrequestloader;

import android.arch.lifecycle.MutableLiveData;
import android.support.v4.util.Pair;

public class ResponseData extends MutableLiveData {
   private boolean networkReturned = false;

   ResponseData() {
   }

   private void setNetworkReturned(Pair var1) {
      if (var1 != null && var1.first != null && (Integer)var1.first == 0) {
         this.networkReturned = true;
      }

   }

   private boolean shouldIgnoreCache(Pair var1) {
      boolean var2 = this.networkReturned;
      boolean var3 = true;
      if (!var2 || var1 == null || var1.first == null || 1 != (Integer)var1.first) {
         var3 = false;
      }

      return var3;
   }

   public void postValue(Pair var1) {
      this.setNetworkReturned(var1);
      if (!this.shouldIgnoreCache(var1)) {
         super.postValue(var1);
      }
   }

   public void setValue(Pair var1) {
      this.setNetworkReturned(var1);
      if (!this.shouldIgnoreCache(var1)) {
         super.setValue(var1);
      }
   }
}
