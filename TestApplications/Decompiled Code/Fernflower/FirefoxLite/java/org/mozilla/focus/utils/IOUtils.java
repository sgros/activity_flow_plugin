package org.mozilla.focus.utils;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IOUtils {
   public static JSONObject readAsset(Context param0, String param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static JSONArray readRawJsonArray(Context var0, int var1) throws IOException {
      try {
         InputStream var2 = var0.getResources().openRawResource(var1);
         byte[] var3 = new byte[var2.available()];
         var2.read(var3);
         String var5 = new String(var3, "UTF-8");
         var2.close();
         JSONArray var6 = new JSONArray(var5);
         return var6;
      } catch (JSONException var4) {
         throw new AssertionError("Corrupt JSON in readRawJsonArray", var4);
      }
   }
}
