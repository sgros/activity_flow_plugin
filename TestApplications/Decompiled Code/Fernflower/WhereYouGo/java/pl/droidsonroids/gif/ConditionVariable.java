package pl.droidsonroids.gif;

class ConditionVariable {
   private volatile boolean mCondition;

   void block() throws InterruptedException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (this.mCondition) {
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

   void close() {
      synchronized(this){}

      try {
         this.mCondition = false;
      } finally {
         ;
      }

   }

   void open() {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.mCondition;
            this.mCondition = true;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label75;
         }

         if (var1) {
            return;
         }

         label66:
         try {
            this.notify();
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label66;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   void set(boolean var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1) {
         label53:
         try {
            this.open();
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label53;
         }
      } else {
         label55:
         try {
            this.close();
            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label55;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }
}
