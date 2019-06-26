package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.RemoteInput.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@TargetApi(20)
@RequiresApi(20)
class RemoteInputCompatApi20 {
   static void addResultsToIntent(RemoteInputCompatBase.RemoteInput[] var0, Intent var1, Bundle var2) {
      android.app.RemoteInput.addResultsToIntent(fromCompat(var0), var1, var2);
   }

   static android.app.RemoteInput[] fromCompat(RemoteInputCompatBase.RemoteInput[] var0) {
      android.app.RemoteInput[] var1;
      if (var0 == null) {
         var1 = null;
      } else {
         android.app.RemoteInput[] var2 = new android.app.RemoteInput[var0.length];
         int var3 = 0;

         while(true) {
            var1 = var2;
            if (var3 >= var0.length) {
               break;
            }

            RemoteInputCompatBase.RemoteInput var4 = var0[var3];
            var2[var3] = (new Builder(var4.getResultKey())).setLabel(var4.getLabel()).setChoices(var4.getChoices()).setAllowFreeFormInput(var4.getAllowFreeFormInput()).addExtras(var4.getExtras()).build();
            ++var3;
         }
      }

      return var1;
   }

   static Bundle getResultsFromIntent(Intent var0) {
      return android.app.RemoteInput.getResultsFromIntent(var0);
   }

   static RemoteInputCompatBase.RemoteInput[] toCompat(android.app.RemoteInput[] var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
      RemoteInputCompatBase.RemoteInput[] var2;
      if (var0 == null) {
         var2 = null;
      } else {
         RemoteInputCompatBase.RemoteInput[] var3 = var1.newArray(var0.length);
         int var4 = 0;

         while(true) {
            var2 = var3;
            if (var4 >= var0.length) {
               break;
            }

            android.app.RemoteInput var5 = var0[var4];
            var3[var4] = var1.build(var5.getResultKey(), var5.getLabel(), var5.getChoices(), var5.getAllowFreeFormInput(), var5.getExtras());
            ++var4;
         }
      }

      return var2;
   }
}
