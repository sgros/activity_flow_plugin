package com.bumptech.glide.manager;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RequestTracker {
   private boolean isPaused;
   private final List pendingRequests = new ArrayList();
   private final Set requests = Collections.newSetFromMap(new WeakHashMap());

   public boolean clearRemoveAndRecycle(Request var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         boolean var3 = this.requests.remove(var1);
         if (this.pendingRequests.remove(var1) || var3) {
            var2 = true;
         }

         if (var2) {
            var1.clear();
            var1.recycle();
         }

         return var2;
      }
   }

   public void clearRequests() {
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         this.clearRemoveAndRecycle((Request)var1.next());
      }

      this.pendingRequests.clear();
   }

   public void pauseRequests() {
      this.isPaused = true;
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if (var2.isRunning()) {
            var2.pause();
            this.pendingRequests.add(var2);
         }
      }

   }

   public void restartRequests() {
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if (!var2.isComplete() && !var2.isCancelled()) {
            var2.pause();
            if (!this.isPaused) {
               var2.begin();
            } else {
               this.pendingRequests.add(var2);
            }
         }
      }

   }

   public void resumeRequests() {
      this.isPaused = false;
      Iterator var1 = Util.getSnapshot(this.requests).iterator();

      while(var1.hasNext()) {
         Request var2 = (Request)var1.next();
         if (!var2.isComplete() && !var2.isCancelled() && !var2.isRunning()) {
            var2.begin();
         }
      }

      this.pendingRequests.clear();
   }

   public void runRequest(Request var1) {
      this.requests.add(var1);
      if (!this.isPaused) {
         var1.begin();
      } else {
         this.pendingRequests.add(var1);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("{numRequests=");
      var1.append(this.requests.size());
      var1.append(", isPaused=");
      var1.append(this.isPaused);
      var1.append("}");
      return var1.toString();
   }
}
