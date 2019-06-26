package com.google.android.exoplayer2.util;

import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

public final class PriorityTaskManager {
   private int highestPriority = Integer.MIN_VALUE;
   private final Object lock = new Object();
   private final PriorityQueue queue = new PriorityQueue(10, Collections.reverseOrder());

   public void add(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void proceed(int var1) throws InterruptedException {
      Object var2 = this.lock;
      synchronized(var2){}

      while(true) {
         Throwable var10000;
         boolean var10001;
         label136: {
            try {
               if (this.highestPriority != var1) {
                  this.lock.wait();
                  continue;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label136;
            }

            label129:
            try {
               return;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label129;
            }
         }

         while(true) {
            Throwable var3 = var10000;

            try {
               throw var3;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public boolean proceedNonBlocking(int var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var3;
         label133: {
            label132: {
               try {
                  if (this.highestPriority == var1) {
                     break label132;
                  }
               } catch (Throwable var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label134;
               }

               var3 = false;
               break label133;
            }

            var3 = true;
         }

         label126:
         try {
            return var3;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            continue;
         }
      }
   }

   public void proceedOrThrow(int var1) throws PriorityTaskManager.PriorityTooLowException {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.highestPriority == var1) {
               return;
            }
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label122;
         }

         label116:
         try {
            PriorityTaskManager.PriorityTooLowException var16 = new PriorityTaskManager.PriorityTooLowException(var1, this.highestPriority);
            throw var16;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label116;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public void remove(int var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label178: {
         label177: {
            label176: {
               try {
                  this.queue.remove(var1);
                  if (this.queue.isEmpty()) {
                     break label176;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label178;
               }

               try {
                  Object var3 = this.queue.peek();
                  Util.castNonNull(var3);
                  var1 = (Integer)var3;
                  break label177;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label178;
               }
            }

            var1 = Integer.MIN_VALUE;
         }

         label169:
         try {
            this.highestPriority = var1;
            this.lock.notifyAll();
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label169;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public static class PriorityTooLowException extends IOException {
      public PriorityTooLowException(int var1, int var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Priority too low [priority=");
         var3.append(var1);
         var3.append(", highest=");
         var3.append(var2);
         var3.append("]");
         super(var3.toString());
      }
   }
}
