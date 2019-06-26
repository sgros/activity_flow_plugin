package android.support.v4.os;

import android.content.Context;

public class UserManagerCompat {
   private UserManagerCompat() {
   }

   public static boolean isUserUnlocked(Context var0) {
      boolean var1;
      if (BuildCompat.isAtLeastN()) {
         var1 = UserManagerCompatApi24.isUserUnlocked(var0);
      } else {
         var1 = true;
      }

      return var1;
   }
}
