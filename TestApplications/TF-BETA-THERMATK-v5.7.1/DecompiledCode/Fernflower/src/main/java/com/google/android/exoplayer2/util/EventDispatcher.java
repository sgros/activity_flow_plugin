package com.google.android.exoplayer2.util;

import android.os.Handler;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventDispatcher {
   private final CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();

   public void addListener(Handler var1, Object var2) {
      boolean var3;
      if (var1 != null && var2 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Assertions.checkArgument(var3);
      this.removeListener(var2);
      this.listeners.add(new EventDispatcher.HandlerAndListener(var1, var2));
   }

   public void dispatch(EventDispatcher.Event var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((EventDispatcher.HandlerAndListener)var2.next()).dispatch(var1);
      }

   }

   public void removeListener(Object var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         EventDispatcher.HandlerAndListener var3 = (EventDispatcher.HandlerAndListener)var2.next();
         if (var3.listener == var1) {
            var3.release();
            this.listeners.remove(var3);
         }
      }

   }

   public interface Event {
      void sendTo(Object var1);
   }

   private static final class HandlerAndListener {
      private final Handler handler;
      private final Object listener;
      private boolean released;

      public HandlerAndListener(Handler var1, Object var2) {
         this.handler = var1;
         this.listener = var2;
      }

      public void dispatch(EventDispatcher.Event var1) {
         this.handler.post(new _$$Lambda$EventDispatcher$HandlerAndListener$uD_JKgYUi0f_RBL7K02WSc4AoE4(this, var1));
      }

      // $FF: synthetic method
      public void lambda$dispatch$0$EventDispatcher$HandlerAndListener(EventDispatcher.Event var1) {
         if (!this.released) {
            var1.sendTo(this.listener);
         }

      }

      public void release() {
         this.released = true;
      }
   }
}
