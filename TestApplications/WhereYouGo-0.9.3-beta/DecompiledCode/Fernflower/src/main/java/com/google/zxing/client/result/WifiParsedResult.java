package com.google.zxing.client.result;

public final class WifiParsedResult extends ParsedResult {
   private final boolean hidden;
   private final String networkEncryption;
   private final String password;
   private final String ssid;

   public WifiParsedResult(String var1, String var2, String var3) {
      this(var1, var2, var3, false);
   }

   public WifiParsedResult(String var1, String var2, String var3, boolean var4) {
      super(ParsedResultType.WIFI);
      this.ssid = var2;
      this.networkEncryption = var1;
      this.password = var3;
      this.hidden = var4;
   }

   public String getDisplayResult() {
      StringBuilder var1 = new StringBuilder(80);
      maybeAppend(this.ssid, var1);
      maybeAppend(this.networkEncryption, var1);
      maybeAppend(this.password, var1);
      maybeAppend(Boolean.toString(this.hidden), var1);
      return var1.toString();
   }

   public String getNetworkEncryption() {
      return this.networkEncryption;
   }

   public String getPassword() {
      return this.password;
   }

   public String getSsid() {
      return this.ssid;
   }

   public boolean isHidden() {
      return this.hidden;
   }
}
