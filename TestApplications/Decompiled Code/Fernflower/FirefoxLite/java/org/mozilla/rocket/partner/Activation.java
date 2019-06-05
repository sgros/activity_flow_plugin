package org.mozilla.rocket.partner;

import android.util.MalformedJsonException;
import org.json.JSONException;
import org.json.JSONObject;

class Activation {
   final long duration;
   final String id;
   final String owner;
   final String url;
   final long version;

   private Activation(JSONObject var1) throws JSONException {
      this.owner = var1.getString("owner");
      this.version = (long)var1.getInt("version");
      this.id = var1.getString("id");
      this.duration = var1.getLong("duration");
      this.url = var1.getString("url");
   }

   static Activation from(JSONObject var0) throws MalformedJsonException {
      try {
         Activation var2 = new Activation(var0);
         return var2;
      } catch (JSONException var1) {
         throw new MalformedJsonException("Activation information invalid");
      }
   }

   boolean matchKeys(String[] var1) {
      boolean var2 = false;
      if (var1 != null && var1.length == 3) {
         boolean var3;
         if (var1[0] != null && var1[0].equals(this.owner)) {
            var3 = true;
         } else {
            var3 = false;
         }

         boolean var4;
         if (var1[1] != null && var1[1].equals(String.valueOf(this.version))) {
            var4 = true;
         } else {
            var4 = false;
         }

         boolean var5 = var2;
         if (var1[2] != null) {
            var5 = var2;
            if (var1[2].equals(this.id)) {
               var5 = true;
            }
         }

         return var3 & var4 & var5;
      } else {
         return false;
      }
   }
}
