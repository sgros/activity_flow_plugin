package org.mozilla.lite.newspoint;

import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.lite.partner.Repository;

public class RepositoryNewsPoint extends Repository {
   static Repository.Parser PARSER;

   static {
      PARSER = _$$Lambda$RepositoryNewsPoint$ZvYynr31y46fQc7qiAJk6VMEhFM.INSTANCE;
   }

   public RepositoryNewsPoint(Context var1, String var2) {
      super(var1, (String)null, 3, (Repository.OnDataChangedListener)null, (Repository.OnCacheInvalidateListener)null, "newspoint", var2, 1, PARSER, true);
   }

   // $FF: synthetic method
   static List lambda$static$0(String var0) throws JSONException {
      ArrayList var1 = new ArrayList();
      JSONArray var28 = (new JSONObject(var0)).getJSONArray("items");

      for(int var2 = 0; var2 < var28.length(); ++var2) {
         JSONObject var3 = var28.getJSONObject(var2);
         String var4 = safeGetString(var3, "id");
         String var5 = safeGetString(var3, "hl");
         String var6 = safeGetString(var3, "imageid");
         JSONArray var7 = safeGetArray(var3, "images");
         String var30;
         if (var7 == null) {
            var30 = null;
         } else {
            var30 = var7.getString(0);
         }

         String var8 = safeGetString(var3, "pn");
         String var9 = safeGetString(var3, "dl");
         String var10 = safeGetString(var3, "dm");
         long var11 = safeGetLong(var3, "pid");
         long var13 = safeGetLong(var3, "lid");
         String var15 = safeGetString(var3, "lang");
         String var16 = safeGetString(var3, "tn");
         String var17 = safeGetString(var3, "wu");
         String var18 = safeGetString(var3, "pnu");
         String var19 = safeGetString(var3, "fu");
         String var20 = safeGetString(var3, "sec");
         String var21 = safeGetString(var3, "mwu");
         String var22 = safeGetString(var3, "m");
         List var29 = Arrays.asList(var3.getJSONArray("tags").join("\u0000").split("\u0000"));
         if (var4 != null && var5 != null && var21 != null && var9 != null) {
            long var24;
            label44: {
               ParseException var31;
               label43: {
                  SimpleDateFormat var23;
                  try {
                     var23 = new SimpleDateFormat;
                  } catch (ParseException var27) {
                     var31 = var27;
                     break label43;
                  }

                  try {
                     var23.<init>("EEE MMM dd HH:mm:ss 'IST' yyyy", Locale.US);
                     var24 = var23.parse(var9).getTime();
                     break label44;
                  } catch (ParseException var26) {
                     var31 = var26;
                  }
               }

               var31.printStackTrace();
               continue;
            }

            var1.add(new NewsPointItem(var4, var30, var5, var21, var24, var6, var8, var10, var11, var13, var15, var16, var17, var18, var19, var20, var22, var29));
         }
      }

      return var1;
   }

   private static JSONArray safeGetArray(JSONObject var0, String var1) {
      try {
         JSONArray var3 = var0.getJSONArray(var1);
         return var3;
      } catch (JSONException var2) {
         return null;
      }
   }

   private static long safeGetLong(JSONObject var0, String var1) {
      try {
         long var2 = var0.getLong(var1);
         return var2;
      } catch (JSONException var4) {
         return -1L;
      }
   }

   private static String safeGetString(JSONObject var0, String var1) {
      try {
         String var3 = var0.getString(var1);
         return var3;
      } catch (JSONException var2) {
         return null;
      }
   }

   protected String getSubscriptionUrl(int var1) {
      return String.format(Locale.US, this.subscriptionUrl, var1, 30);
   }
}
