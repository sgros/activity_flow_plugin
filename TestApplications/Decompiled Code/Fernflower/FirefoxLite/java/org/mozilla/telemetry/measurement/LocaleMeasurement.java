package org.mozilla.telemetry.measurement;

import java.util.Locale;

public class LocaleMeasurement extends TelemetryMeasurement {
   public LocaleMeasurement() {
      super("locale");
   }

   private String getLanguage(Locale var1) {
      String var2 = var1.getLanguage();
      if (var2.equals("iw")) {
         return "he";
      } else if (var2.equals("in")) {
         return "id";
      } else {
         return var2.equals("ji") ? "yi" : var2;
      }
   }

   public Object flush() {
      return this.getLanguageTag(Locale.getDefault());
   }

   public String getLanguageTag(Locale var1) {
      String var2 = this.getLanguage(var1);
      String var3 = var1.getCountry();
      if (var3.equals("")) {
         return var2;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append(var2);
         var4.append("-");
         var4.append(var3);
         return var4.toString();
      }
   }
}
