package org.mozilla.rocket.bhaskar;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.lite.partner.Repository;

public class RepositoryBhaskar extends Repository {
   static Repository.Parser PARSER;

   static {
      PARSER = _$$Lambda$RepositoryBhaskar$6W7SyoJU81_IckXVButtHQnV0FA.INSTANCE;
   }

   public RepositoryBhaskar(Context var1, String var2) {
      super(var1, (String)null, 3, (Repository.OnDataChangedListener)null, (Repository.OnCacheInvalidateListener)null, "bhaskar", var2, 1, PARSER, true);
   }

   // $FF: synthetic method
   static List lambda$static$0(String var0) throws JSONException {
      ArrayList var1 = new ArrayList();
      JSONArray var2 = (new JSONObject(var0)).getJSONObject("data").getJSONArray("rows");

      for(int var3 = 0; var3 < var2.length(); ++var3) {
         JSONObject var4;
         String var5;
         String var6;
         String var7;
         String var8;
         label27: {
            var4 = var2.getJSONObject(var3);
            var5 = var4.optString("id", (String)null);
            var6 = var4.optString("articleFrom", (String)null);
            var7 = var4.optString("category", (String)null);
            var8 = var4.optString("city", (String)null);
            var0 = var4.optString("coverPic", (String)null);
            if (var0 != null) {
               JSONArray var19 = new JSONArray(var0);
               if (var19.length() > 0) {
                  var0 = var19.getString(0);
                  break label27;
               }
            }

            var0 = null;
         }

         String var9 = var4.optString("description", (String)null);
         String var10 = var4.optString("detailUrl", (String)null);
         String var11 = var4.optString("keywords", (String)null);
         String var12 = var4.optString("language", (String)null);
         String var13 = var4.optString("province", (String)null);
         long var14 = var4.optLong("publishTime", -1L);
         String var16 = var4.optString("subcategory", (String)null);
         String var17 = var4.optString("summary", (String)null);
         List var18 = Arrays.asList(var4.getJSONArray("tags").join("\u0000").split("\u0000"));
         String var20 = var4.getString("title");
         if (var5 != null && var20 != null && var10 != null && var14 != -1L) {
            var1.add(new BhaskarItem(var5, var0, var20, var10, var14, var17, var12, var7, var16, var11, var9, var18, var6, var13, var8));
         }
      }

      return var1;
   }

   protected String getSubscriptionUrl(int var1) {
      return String.format(Locale.US, this.subscriptionUrl, 30, 521, var1);
   }
}
