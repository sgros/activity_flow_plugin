package org.mozilla.telemetry.event;

import android.os.SystemClock;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.telemetry.util.StringUtils;

public class TelemetryEvent {
   private static final long startTime = SystemClock.elapsedRealtime();
   private final String category;
   private final Map extras;
   private final String method;
   private final String object;
   private final long timestamp;
   private String value;

   private TelemetryEvent(String var1, String var2, String var3, String var4) {
      this.timestamp = SystemClock.elapsedRealtime() - startTime;
      this.category = StringUtils.safeSubstring(var1, 0, 30);
      this.method = StringUtils.safeSubstring(var2, 0, 20);
      var2 = null;
      if (var3 == null) {
         var1 = null;
      } else {
         var1 = StringUtils.safeSubstring(var3, 0, 20);
      }

      this.object = var1;
      if (var4 == null) {
         var1 = var2;
      } else {
         var1 = StringUtils.safeSubstring(var4, 0, 80);
      }

      this.value = var1;
      this.extras = new HashMap();
   }

   public static TelemetryEvent create(String var0, String var1, String var2, String var3) {
      return new TelemetryEvent(var0, var1, var2, var3);
   }

   public TelemetryEvent extra(String var1, String var2) {
      if (this.extras.size() <= 200) {
         this.extras.put(StringUtils.safeSubstring(var1, 0, 15), StringUtils.safeSubstring(var2, 0, 80));
         return this;
      } else {
         throw new IllegalArgumentException("Exceeding limit of 200 extra keys");
      }
   }

   public void queue() {
      TelemetryHolder.get().queueEvent(this);
   }

   public String toJSON() {
      JSONArray var1 = new JSONArray();
      var1.put(this.timestamp);
      var1.put(this.category);
      var1.put(this.method);
      var1.put(this.object);
      if (this.value != null) {
         var1.put(this.value);
      }

      if (this.extras != null && !this.extras.isEmpty()) {
         if (this.value == null) {
            var1.put((Object)null);
         }

         var1.put(new JSONObject(this.extras));
      }

      return var1.toString();
   }
}
