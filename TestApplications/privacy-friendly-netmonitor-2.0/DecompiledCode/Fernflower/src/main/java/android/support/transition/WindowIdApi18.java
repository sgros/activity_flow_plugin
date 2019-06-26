package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.WindowId;

@RequiresApi(18)
class WindowIdApi18 implements WindowIdImpl {
   private final WindowId mWindowId;

   WindowIdApi18(@NonNull View var1) {
      this.mWindowId = var1.getWindowId();
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof WindowIdApi18 && ((WindowIdApi18)var1).mWindowId.equals(this.mWindowId)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public int hashCode() {
      return this.mWindowId.hashCode();
   }
}
