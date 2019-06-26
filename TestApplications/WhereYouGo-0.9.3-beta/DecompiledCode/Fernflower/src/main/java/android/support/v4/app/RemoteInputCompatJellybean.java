package android.support.v4.app;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@TargetApi(16)
@RequiresApi(16)
class RemoteInputCompatJellybean {
   public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
   private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
   private static final String KEY_CHOICES = "choices";
   private static final String KEY_EXTRAS = "extras";
   private static final String KEY_LABEL = "label";
   private static final String KEY_RESULT_KEY = "resultKey";
   public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";

   static void addResultsToIntent(RemoteInputCompatBase.RemoteInput[] var0, Intent var1, Bundle var2) {
      Bundle var3 = new Bundle();
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         RemoteInputCompatBase.RemoteInput var6 = var0[var5];
         Object var7 = var2.get(var6.getResultKey());
         if (var7 instanceof CharSequence) {
            var3.putCharSequence(var6.getResultKey(), (CharSequence)var7);
         }
      }

      Intent var8 = new Intent();
      var8.putExtra("android.remoteinput.resultsData", var3);
      var1.setClipData(ClipData.newIntent("android.remoteinput.results", var8));
   }

   static RemoteInputCompatBase.RemoteInput fromBundle(Bundle var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
      return var1.build(var0.getString("resultKey"), var0.getCharSequence("label"), var0.getCharSequenceArray("choices"), var0.getBoolean("allowFreeFormInput"), var0.getBundle("extras"));
   }

   static RemoteInputCompatBase.RemoteInput[] fromBundleArray(Bundle[] var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
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

            var3[var4] = fromBundle(var0[var4], var1);
            ++var4;
         }
      }

      return var2;
   }

   static Bundle getResultsFromIntent(Intent var0) {
      Object var1 = null;
      ClipData var2 = var0.getClipData();
      Bundle var4;
      if (var2 == null) {
         var4 = (Bundle)var1;
      } else {
         ClipDescription var3 = var2.getDescription();
         var4 = (Bundle)var1;
         if (var3.hasMimeType("text/vnd.android.intent")) {
            var4 = (Bundle)var1;
            if (var3.getLabel().equals("android.remoteinput.results")) {
               var4 = (Bundle)var2.getItemAt(0).getIntent().getExtras().getParcelable("android.remoteinput.resultsData");
            }
         }
      }

      return var4;
   }

   static Bundle toBundle(RemoteInputCompatBase.RemoteInput var0) {
      Bundle var1 = new Bundle();
      var1.putString("resultKey", var0.getResultKey());
      var1.putCharSequence("label", var0.getLabel());
      var1.putCharSequenceArray("choices", var0.getChoices());
      var1.putBoolean("allowFreeFormInput", var0.getAllowFreeFormInput());
      var1.putBundle("extras", var0.getExtras());
      return var1;
   }

   static Bundle[] toBundleArray(RemoteInputCompatBase.RemoteInput[] var0) {
      Bundle[] var1;
      if (var0 == null) {
         var1 = null;
      } else {
         Bundle[] var2 = new Bundle[var0.length];
         int var3 = 0;

         while(true) {
            var1 = var2;
            if (var3 >= var0.length) {
               break;
            }

            var2[var3] = toBundle(var0[var3]);
            ++var3;
         }
      }

      return var1;
   }
}
