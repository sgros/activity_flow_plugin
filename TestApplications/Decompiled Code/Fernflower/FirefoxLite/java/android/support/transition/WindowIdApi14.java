package android.support.transition;

import android.os.IBinder;

class WindowIdApi14 implements WindowIdImpl {
   private final IBinder mToken;

   WindowIdApi14(IBinder var1) {
      this.mToken = var1;
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof WindowIdApi14 && ((WindowIdApi14)var1).mToken.equals(this.mToken)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return this.mToken.hashCode();
   }
}
