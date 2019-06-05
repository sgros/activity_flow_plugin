package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONObject;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class SearchesMeasurement extends TelemetryMeasurement {
   private final TelemetryConfiguration configuration;

   public SearchesMeasurement(TelemetryConfiguration var1) {
      super("searches");
      this.configuration = var1;
   }

   private static String getEngineSearchCountKey(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("measurements-search-count-engine-");
      var1.append(var0);
      return var1.toString();
   }

   private JSONObject getSearchCountMapAndReset() {
      // $FF: Couldn't be decompiled
   }

   private void storeCount(String var1) {
      SharedPreferences var2 = this.configuration.getSharedPreferences();
      var1 = getEngineSearchCountKey(var1);
      int var3 = var2.getInt(var1, 0);
      var2.edit().putInt(var1, var3 + 1).apply();
   }

   private void storeKey(String var1) {
      SharedPreferences var2 = this.configuration.getSharedPreferences();
      Set var3 = var2.getStringSet("measurements-search-count-keyset", Collections.emptySet());
      if (!var3.contains(var1)) {
         HashSet var4 = new HashSet(var3);
         var4.add(var1);
         var2.edit().putStringSet("measurements-search-count-keyset", var4).apply();
      }
   }

   public Object flush() {
      return this.getSearchCountMapAndReset();
   }

   public void recordSearch(String var1, String var2) {
      synchronized(this){}

      try {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(".");
         var3.append(var2);
         var1 = var3.toString();
         this.storeCount(var1);
         this.storeKey(var1);
      } finally {
         ;
      }

   }
}
