package com.google.android.exoplayer2.source.dash.manifest;

public final class UtcTimingElement {
   public final String schemeIdUri;
   public final String value;

   public UtcTimingElement(String var1, String var2) {
      this.schemeIdUri = var1;
      this.value = var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.schemeIdUri);
      var1.append(", ");
      var1.append(this.value);
      return var1.toString();
   }
}
