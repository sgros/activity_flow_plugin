package androidx.core.app;

import android.app.Notification;
import android.app.Notification.Builder;
import android.os.Bundle;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class NotificationCompatJellybean {
   private static final Object sActionsLock = new Object();
   private static Field sExtrasField;
   private static boolean sExtrasFieldAccessFailed;
   private static final Object sExtrasLock = new Object();

   public static SparseArray buildActionExtrasMap(List var0) {
      int var1 = var0.size();
      SparseArray var2 = null;

      SparseArray var5;
      for(int var3 = 0; var3 < var1; var2 = var5) {
         Bundle var4 = (Bundle)var0.get(var3);
         var5 = var2;
         if (var4 != null) {
            var5 = var2;
            if (var2 == null) {
               var5 = new SparseArray();
            }

            var5.put(var3, var4);
         }

         ++var3;
      }

      return var2;
   }

   static Bundle getBundleForAction(NotificationCompat.Action var0) {
      Bundle var1 = new Bundle();
      var1.putInt("icon", var0.getIcon());
      var1.putCharSequence("title", var0.getTitle());
      var1.putParcelable("actionIntent", var0.getActionIntent());
      Bundle var2;
      if (var0.getExtras() != null) {
         var2 = new Bundle(var0.getExtras());
      } else {
         var2 = new Bundle();
      }

      var2.putBoolean("android.support.allowGeneratedReplies", var0.getAllowGeneratedReplies());
      var1.putBundle("extras", var2);
      var1.putParcelableArray("remoteInputs", toBundleArray(var0.getRemoteInputs()));
      var1.putBoolean("showsUserInterface", var0.getShowsUserInterface());
      var1.putInt("semanticAction", var0.getSemanticAction());
      return var1;
   }

   public static Bundle getExtras(Notification param0) {
      // $FF: Couldn't be decompiled
   }

   private static Bundle toBundle(RemoteInput var0) {
      Bundle var1 = new Bundle();
      var1.putString("resultKey", var0.getResultKey());
      var1.putCharSequence("label", var0.getLabel());
      var1.putCharSequenceArray("choices", var0.getChoices());
      var1.putBoolean("allowFreeFormInput", var0.getAllowFreeFormInput());
      var1.putBundle("extras", var0.getExtras());
      Set var2 = var0.getAllowedDataTypes();
      if (var2 != null && !var2.isEmpty()) {
         ArrayList var3 = new ArrayList(var2.size());
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.add((String)var4.next());
         }

         var1.putStringArrayList("allowedDataTypes", var3);
      }

      return var1;
   }

   private static Bundle[] toBundleArray(RemoteInput[] var0) {
      if (var0 == null) {
         return null;
      } else {
         Bundle[] var1 = new Bundle[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = toBundle(var0[var2]);
         }

         return var1;
      }
   }

   public static Bundle writeActionAndGetExtras(Builder var0, NotificationCompat.Action var1) {
      var0.addAction(var1.getIcon(), var1.getTitle(), var1.getActionIntent());
      Bundle var2 = new Bundle(var1.getExtras());
      if (var1.getRemoteInputs() != null) {
         var2.putParcelableArray("android.support.remoteInputs", toBundleArray(var1.getRemoteInputs()));
      }

      if (var1.getDataOnlyRemoteInputs() != null) {
         var2.putParcelableArray("android.support.dataRemoteInputs", toBundleArray(var1.getDataOnlyRemoteInputs()));
      }

      var2.putBoolean("android.support.allowGeneratedReplies", var1.getAllowGeneratedReplies());
      return var2;
   }
}
