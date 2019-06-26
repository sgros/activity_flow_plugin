package com.google.android.exoplayer2.util;

public final class ConditionVariable {
   private boolean isOpen;

   public void block() throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (this.isOpen) {
               var3 = false;
               return;
            }

            this.wait();
            var3 = false;
         } finally {
            if (var3) {
               ;
            }
         }
      }
   }

   public boolean block(long var1) throws InterruptedException {
      synchronized(this){}

      Throwable var10000;
      label209: {
         boolean var10001;
         long var3;
         try {
            var3 = android.os.SystemClock.elapsedRealtime();
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            break label209;
         }

         long var5 = var1 + var3;
         var1 = var3;

         while(true) {
            try {
               if (this.isOpen) {
                  break;
               }
            } catch (Throwable var27) {
               var10000 = var27;
               var10001 = false;
               break label209;
            }

            if (var1 >= var5) {
               break;
            }

            try {
               this.wait(var5 - var1);
               var1 = android.os.SystemClock.elapsedRealtime();
            } catch (Throwable var26) {
               var10000 = var26;
               var10001 = false;
               break label209;
            }
         }

         label189:
         try {
            boolean var7 = this.isOpen;
            return var7;
         } catch (Throwable var25) {
            var10000 = var25;
            var10001 = false;
            break label189;
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public boolean close() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.isOpen;
         this.isOpen = false;
      } finally {
         ;
      }

      return var1;
   }

   public boolean open() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.isOpen;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (var1) {
            return false;
         }

         try {
            this.isOpen = true;
            this.notifyAll();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return true;
      }

      Throwable var2 = var10000;
      throw var2;
   }
}
