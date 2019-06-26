package com.google.android.exoplayer2;

import android.os.Handler;
import com.google.android.exoplayer2.util.Assertions;

public final class PlayerMessage {
   private boolean deleteAfterDelivery;
   private Handler handler;
   private boolean isCanceled;
   private boolean isDelivered;
   private boolean isProcessed;
   private boolean isSent;
   private Object payload;
   private long positionMs;
   private final PlayerMessage.Sender sender;
   private final PlayerMessage.Target target;
   private final Timeline timeline;
   private int type;
   private int windowIndex;

   public PlayerMessage(PlayerMessage.Sender var1, PlayerMessage.Target var2, Timeline var3, int var4, Handler var5) {
      this.sender = var1;
      this.target = var2;
      this.timeline = var3;
      this.handler = var5;
      this.windowIndex = var4;
      this.positionMs = -9223372036854775807L;
      this.deleteAfterDelivery = true;
   }

   public boolean blockUntilDelivered() throws InterruptedException {
      synchronized(this){}

      Throwable var10000;
      label225: {
         boolean var10001;
         boolean var1;
         label219: {
            label218: {
               try {
                  Assertions.checkState(this.isSent);
                  if (this.handler.getLooper().getThread() != Thread.currentThread()) {
                     break label218;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label225;
               }

               var1 = false;
               break label219;
            }

            var1 = true;
         }

         try {
            Assertions.checkState(var1);
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label225;
         }

         while(true) {
            try {
               if (this.isProcessed) {
                  break;
               }

               this.wait();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label225;
            }
         }

         label202:
         try {
            var1 = this.isDelivered;
            return var1;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label202;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public boolean getDeleteAfterDelivery() {
      return this.deleteAfterDelivery;
   }

   public Handler getHandler() {
      return this.handler;
   }

   public Object getPayload() {
      return this.payload;
   }

   public long getPositionMs() {
      return this.positionMs;
   }

   public PlayerMessage.Target getTarget() {
      return this.target;
   }

   public Timeline getTimeline() {
      return this.timeline;
   }

   public int getType() {
      return this.type;
   }

   public int getWindowIndex() {
      return this.windowIndex;
   }

   public boolean isCanceled() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.isCanceled;
      } finally {
         ;
      }

      return var1;
   }

   public void markAsProcessed(boolean var1) {
      synchronized(this){}

      try {
         this.isDelivered |= var1;
         this.isProcessed = true;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public PlayerMessage send() {
      Assertions.checkState(this.isSent ^ true);
      if (this.positionMs == -9223372036854775807L) {
         Assertions.checkArgument(this.deleteAfterDelivery);
      }

      this.isSent = true;
      this.sender.sendMessage(this);
      return this;
   }

   public PlayerMessage setPayload(Object var1) {
      Assertions.checkState(this.isSent ^ true);
      this.payload = var1;
      return this;
   }

   public PlayerMessage setType(int var1) {
      Assertions.checkState(this.isSent ^ true);
      this.type = var1;
      return this;
   }

   public interface Sender {
      void sendMessage(PlayerMessage var1);
   }

   public interface Target {
      void handleMessage(int var1, Object var2) throws ExoPlaybackException;
   }
}
