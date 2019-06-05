package org.mozilla.focus.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.focus.history.model.Site;

public class TopSitesUtils {
   public static void clearTopSiteData(Context var0) {
      PreferenceManager.getDefaultSharedPreferences(var0).edit().remove("topsites_pref").apply();
   }

   public static JSONArray getDefaultSitesJsonArrayFromAssets(Context var0) {
      JSONArray var1;
      JSONArray var10;
      label52: {
         JSONException var5;
         label46: {
            try {
               var1 = new JSONArray(loadDefaultSitesFromAssets(var0, 2131689487));
            } catch (JSONException var9) {
               var5 = var9;
               var10 = null;
               break label46;
            }

            JSONException var10000;
            label47: {
               boolean var10001;
               long var2;
               try {
                  var2 = System.currentTimeMillis();
               } catch (JSONException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label47;
               }

               int var4 = 0;

               while(true) {
                  try {
                     if (var4 >= var1.length()) {
                        break;
                     }

                     ((JSONObject)var1.get(var4)).put("lastViewTimestamp", var2);
                  } catch (JSONException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label47;
                  }

                  ++var4;
               }

               try {
                  saveDefaultSites(var0, var1);
                  break label52;
               } catch (JSONException var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            var5 = var10000;
            var10 = var1;
         }

         var5.printStackTrace();
         return var10;
      }

      var10 = var1;
      return var10;
   }

   public static String loadDefaultSitesFromAssets(Context var0, int var1) {
      try {
         InputStream var2 = var0.getResources().openRawResource(var1);
         byte[] var7 = new byte[var2.available()];
         var2.read(var7);
         var2.close();
         String var8 = new String(var7, "UTF-8");
         return var8;
      } catch (IOException var5) {
         var5.printStackTrace();
      } finally {
         return "[]";
      }

      return "[]";
   }

   public static List paresJsonToList(Context var0, JSONArray var1) {
      ArrayList var17 = new ArrayList();
      if (var1 != null) {
         byte var2 = 0;

         while(true) {
            try {
               if (var2 >= var1.length()) {
                  break;
               }

               JSONObject var3 = (JSONObject)var1.get(var2);
               long var4 = var3.getLong("id");
               String var6 = var3.getString("title");
               String var7 = var3.getString("url");
               long var8 = var3.getLong("viewCount");
               long var10 = var3.getLong("lastViewTimestamp");
               StringBuilder var12 = new StringBuilder();
               var12.append("file:///android_asset/topsites/icon/");
               var12.append(var3.getString("favicon"));
               String var19 = var12.toString();
               Site var18 = new Site(var4, var6, var7, var8, var10, var19);
               var18.setDefault(true);
               var17.add(var18);
            } catch (JSONException var15) {
               var15.printStackTrace();
               break;
            } finally {
               break;
            }

         }
      }

      return var17;
   }

   public static void saveDefaultSites(Context var0, JSONArray var1) {
      if (var0 != null) {
         PreferenceManager.getDefaultSharedPreferences(var0).edit().putString("topsites_pref", var1.toString()).apply();
      }
   }
}
