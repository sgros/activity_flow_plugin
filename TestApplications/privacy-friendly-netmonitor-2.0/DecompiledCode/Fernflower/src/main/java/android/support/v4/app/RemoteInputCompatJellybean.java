package android.support.v4.app;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@RequiresApi(16)
class RemoteInputCompatJellybean {
   private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
   private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
   private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
   private static final String KEY_CHOICES = "choices";
   private static final String KEY_EXTRAS = "extras";
   private static final String KEY_LABEL = "label";
   private static final String KEY_RESULT_KEY = "resultKey";

   public static void addDataResultToIntent(RemoteInput var0, Intent var1, Map var2) {
      Intent var3 = getClipDataIntentFromIntent(var1);
      Intent var4 = var3;
      if (var3 == null) {
         var4 = new Intent();
      }

      Iterator var5 = var2.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var8 = (Entry)var5.next();
         String var6 = (String)var8.getKey();
         Uri var7 = (Uri)var8.getValue();
         if (var6 != null) {
            Bundle var10 = var4.getBundleExtra(getExtraResultsKeyForData(var6));
            Bundle var9 = var10;
            if (var10 == null) {
               var9 = new Bundle();
            }

            var9.putString(var0.getResultKey(), var7.toString());
            var4.putExtra(getExtraResultsKeyForData(var6), var9);
         }
      }

      var1.setClipData(ClipData.newIntent("android.remoteinput.results", var4));
   }

   static void addResultsToIntent(RemoteInputCompatBase.RemoteInput[] var0, Intent var1, Bundle var2) {
      Intent var3 = getClipDataIntentFromIntent(var1);
      Intent var4 = var3;
      if (var3 == null) {
         var4 = new Intent();
      }

      Bundle var5 = var4.getBundleExtra("android.remoteinput.resultsData");
      Bundle var9 = var5;
      if (var5 == null) {
         var9 = new Bundle();
      }

      int var6 = var0.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         RemoteInputCompatBase.RemoteInput var8 = var0[var7];
         Object var10 = var2.get(var8.getResultKey());
         if (var10 instanceof CharSequence) {
            var9.putCharSequence(var8.getResultKey(), (CharSequence)var10);
         }
      }

      var4.putExtra("android.remoteinput.resultsData", var9);
      var1.setClipData(ClipData.newIntent("android.remoteinput.results", var4));
   }

   static RemoteInputCompatBase.RemoteInput fromBundle(Bundle var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
      ArrayList var2 = var0.getStringArrayList("allowedDataTypes");
      HashSet var3 = new HashSet();
      if (var2 != null) {
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var3.add((String)var4.next());
         }
      }

      return var1.build(var0.getString("resultKey"), var0.getCharSequence("label"), var0.getCharSequenceArray("choices"), var0.getBoolean("allowFreeFormInput"), var0.getBundle("extras"), var3);
   }

   static RemoteInputCompatBase.RemoteInput[] fromBundleArray(Bundle[] var0, RemoteInputCompatBase.RemoteInput.Factory var1) {
      if (var0 == null) {
         return null;
      } else {
         RemoteInputCompatBase.RemoteInput[] var2 = var1.newArray(var0.length);

         for(int var3 = 0; var3 < var0.length; ++var3) {
            var2[var3] = fromBundle(var0[var3], var1);
         }

         return var2;
      }
   }

   private static Intent getClipDataIntentFromIntent(Intent var0) {
      ClipData var2 = var0.getClipData();
      if (var2 == null) {
         return null;
      } else {
         ClipDescription var1 = var2.getDescription();
         if (!var1.hasMimeType("text/vnd.android.intent")) {
            return null;
         } else {
            return !var1.getLabel().equals("android.remoteinput.results") ? null : var2.getItemAt(0).getIntent();
         }
      }
   }

   static Map getDataResultsFromIntent(Intent var0, String var1) {
      Intent var2 = getClipDataIntentFromIntent(var0);
      Object var3 = null;
      if (var2 == null) {
         return null;
      } else {
         HashMap var7 = new HashMap();
         Iterator var4 = var2.getExtras().keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (var5.startsWith("android.remoteinput.dataTypeResultsData")) {
               String var6 = var5.substring("android.remoteinput.dataTypeResultsData".length());
               if (var6 != null && !var6.isEmpty()) {
                  var5 = var2.getBundleExtra(var5).getString(var1);
                  if (var5 != null && !var5.isEmpty()) {
                     var7.put(var6, Uri.parse(var5));
                  }
               }
            }
         }

         if (var7.isEmpty()) {
            var7 = (HashMap)var3;
         }

         return var7;
      }
   }

   private static String getExtraResultsKeyForData(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("android.remoteinput.dataTypeResultsData");
      var1.append(var0);
      return var1.toString();
   }

   static Bundle getResultsFromIntent(Intent var0) {
      var0 = getClipDataIntentFromIntent(var0);
      return var0 == null ? null : (Bundle)var0.getExtras().getParcelable("android.remoteinput.resultsData");
   }

   static Bundle toBundle(RemoteInputCompatBase.RemoteInput var0) {
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

   static Bundle[] toBundleArray(RemoteInputCompatBase.RemoteInput[] var0) {
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
}
