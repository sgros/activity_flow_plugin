package de.bjoernr.ssllabs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;

public class Api {
   private static final String API_URL = "https://api.ssllabs.com/api/v2";
   private static final String VERSION = "0.0.1-SNAPSHOT";

   private String booleanToOnOffString(boolean var1) {
      String var2;
      if (var1) {
         var2 = "on";
      } else {
         var2 = "off";
      }

      return var2;
   }

   private String buildGetParameterString(Map var1) {
      String var2 = "";
      Iterator var3 = var1.entrySet().iterator();
      String var5 = var2;

      while(var3.hasNext()) {
         Entry var6 = (Entry)var3.next();
         if (var6.getValue() != null) {
            StringBuilder var4 = new StringBuilder();
            var4.append(var5);
            if (var5.length() < 1) {
               var5 = "?";
            } else {
               var5 = "&";
            }

            var4.append(var5);
            String var8 = var4.toString();
            StringBuilder var7 = new StringBuilder();
            var7.append(var8);
            var7.append((String)var6.getKey());
            var7.append("=");
            var7.append((String)var6.getValue());
            var5 = var7.toString();
         }
      }

      return var5;
   }

   public static String getApiUrl() {
      return "https://api.ssllabs.com/api/v2";
   }

   public static String getVersion() {
      return "0.0.1-SNAPSHOT";
   }

   private String sendApiRequest(String var1, Map var2) throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append("https://api.ssllabs.com/api/v2/");
      var3.append(var1);
      URL var5 = new URL(var3.toString());
      if (var2 != null) {
         var3 = new StringBuilder();
         var3.append(var5.toString());
         var3.append(this.buildGetParameterString(var2));
         var5 = new URL(var3.toString());
      }

      InputStream var6 = var5.openStream();
      StringBuffer var7 = new StringBuffer();

      while(true) {
         int var4 = var6.read();
         if (var4 == -1) {
            var6.close();
            return var7.toString();
         }

         var7.append((char)var4);
      }
   }

   public JSONObject fetchApiInfo() {
      JSONObject var1 = new JSONObject();

      JSONObject var3;
      try {
         String var2 = this.sendApiRequest("info", (Map)null);
         var3 = new JSONObject(var2);
      } catch (Exception var4) {
         return var1;
      }

      var1 = var3;
      return var1;
   }

   public JSONObject fetchEndpointData(String var1, String var2, boolean var3) {
      JSONObject var4 = new JSONObject();

      JSONObject var7;
      try {
         HashMap var5 = new HashMap();
         var5.put("host", var1);
         var5.put("s", var2);
         var5.put("fromCache", this.booleanToOnOffString(var3));
         var2 = this.sendApiRequest("getEndpointData", var5);
         var7 = new JSONObject(var2);
      } catch (Exception var6) {
         var7 = var4;
      }

      return var7;
   }

   public JSONObject fetchHostInformation(String var1, boolean var2, boolean var3, boolean var4, String var5, String var6, boolean var7) {
      JSONObject var8 = new JSONObject();

      JSONObject var11;
      try {
         HashMap var9 = new HashMap();
         var9.put("host", var1);
         var9.put("publish", this.booleanToOnOffString(var2));
         var9.put("startNew", this.booleanToOnOffString(var3));
         var9.put("fromCache", this.booleanToOnOffString(var4));
         var9.put("maxAge", var5);
         var9.put("all", var6);
         var9.put("ignoreMismatch", this.booleanToOnOffString(var7));
         var5 = this.sendApiRequest("analyze", var9);
         var11 = new JSONObject(var5);
      } catch (Exception var10) {
         var11 = var8;
      }

      return var11;
   }

   public JSONObject fetchHostInformationCached(String var1, String var2, boolean var3, boolean var4) {
      return this.fetchHostInformation(var1, var3, false, true, var2, "done", var4);
   }

   public JSONObject fetchStatusCodes() {
      JSONObject var1 = new JSONObject();

      JSONObject var3;
      try {
         String var2 = this.sendApiRequest("getStatusCodes", (Map)null);
         var3 = new JSONObject(var2);
      } catch (Exception var4) {
         return var1;
      }

      var1 = var3;
      return var1;
   }

   public String sendCustomApiRequest(String var1, Map var2) {
      try {
         var1 = this.sendApiRequest(var1, var2);
      } catch (Exception var3) {
         var1 = "";
      }

      return var1;
   }
}
