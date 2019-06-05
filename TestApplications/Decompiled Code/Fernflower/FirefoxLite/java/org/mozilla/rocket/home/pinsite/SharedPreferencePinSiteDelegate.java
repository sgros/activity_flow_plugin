package org.mozilla.rocket.home.pinsite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.focus.history.model.Site;
import org.mozilla.focus.utils.TopSitesUtils;

public final class SharedPreferencePinSiteDelegate implements PinSiteDelegate {
   public static final SharedPreferencePinSiteDelegate.Companion Companion = new SharedPreferencePinSiteDelegate.Companion((DefaultConstructorMarker)null);
   private final Context context;
   private boolean isEnabled;
   private final List partnerList;
   private final SharedPreferences pref;
   private final JSONObject rootNode;
   private final List sites;

   public SharedPreferencePinSiteDelegate(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      super();
      this.context = var1;
      this.pref = this.context.getSharedPreferences("pin_sites", 0);
      this.sites = (List)(new ArrayList());
      this.partnerList = (List)(new ArrayList());
      this.rootNode = new JSONObject(TopSitesUtils.loadDefaultSitesFromAssets(this.context, 2131689481));
      this.isEnabled = this.isEnabled(this.rootNode);
      StringBuilder var2 = new StringBuilder();
      var2.append("isEnable: ");
      var2.append(this.isEnabled);
      this.log(var2.toString());
      var2 = new StringBuilder();
      var2.append("isFirstInit: ");
      var2.append(this.isFirstInit());
      this.log(var2.toString());
      if (this.isEnabled) {
         List var3 = this.getPartnerList(this.rootNode);
         if (this.hasTopSiteRecord()) {
            this.log("init for update user");
            this.initForUpdateUser(this.partnerList, var3);
         } else {
            this.log("init for new user");
            this.initForNewUser(this.partnerList, var3);
         }
      } else {
         this.log("no initialization needed");
      }

   }

   private final String getFaviconUrl(JSONObject var1) {
      String var2 = var1.optString("favicon");
      Intrinsics.checkExpressionValueIsNotNull(var2, "json.optString(TopSitesUtils.KEY_FAVICON)");
      return var2;
   }

   private final List getPartnerList(JSONObject var1) {
      JSONArray var2 = var1.getJSONArray("partner");
      Intrinsics.checkExpressionValueIsNotNull(var2, "rootNode.getJSONArray(JSON_KEY_STRING_PARTNER)");
      return this.jsonToSites(var2, true);
   }

   private final long getViewCountForPinSiteAt(int var1) {
      return Long.MAX_VALUE - (long)var1 * 100L;
   }

   private final boolean hasTopSiteRecord() {
      String var1 = PreferenceManager.getDefaultSharedPreferences(this.context).getString("topsites_pref", "");
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 != null) {
         var3 = var2;
         if (((CharSequence)var1).length() > 0) {
            var3 = true;
         }
      }

      return var3;
   }

   private final void initForNewUser(List var1, List var2) {
      var1.addAll((Collection)var2);
      List var3 = CollectionsKt.toMutableList((Collection)this.jsonToSites(new JSONArray(TopSitesUtils.loadDefaultSitesFromAssets(this.context, 2131689487)), true));

      for(int var4 = 2 - var2.size(); var4 > 0 && ((Collection)var3).isEmpty() ^ true; --var4) {
         var1.add(var3.remove(0));
      }

   }

   private final void initForUpdateUser(List var1, List var2) {
      var1.addAll((Collection)var2);
   }

   private final boolean isEnabled(JSONObject var1) {
      return var1.getBoolean("isEnabled");
   }

   private final boolean isFirstInit() {
      return this.pref.getBoolean("first_init", true);
   }

   private final List jsonToSites(JSONArray var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      String var4;
      if (var2) {
         var4 = "file:///android_asset/topsites/icon/";
      } else {
         var4 = "";
      }

      int var5 = 0;

      int var6;
      boolean var10001;
      try {
         var6 = var1.length();
      } catch (JSONException var17) {
         var10001 = false;
         return (List)var3;
      }

      for(; var5 < var6; ++var5) {
         try {
            JSONObject var7 = var1.getJSONObject(var5);
            long var9 = var7.getLong("id");
            String var11 = var7.getString("title");
            String var12 = var7.getString("url");
            long var13 = var7.getLong("viewCount");
            StringBuilder var15 = new StringBuilder();
            var15.append(var4);
            Intrinsics.checkExpressionValueIsNotNull(var7, "obj");
            var15.append(this.getFaviconUrl(var7));
            Site var8 = new Site(var9, var11, var12, var13, 0L, var15.toString());
            var3.add(var8);
         } catch (JSONException var16) {
            var10001 = false;
            break;
         }
      }

      return (List)var3;
   }

   private final void load(List var1) {
      if (!this.isEnabled) {
         this.log("load - not enabled");
      } else {
         this.log("load - enabled");
         var1.clear();
         boolean var2 = this.isFirstInit();
         if (var2 && ((Collection)this.partnerList).isEmpty() ^ true) {
            var1.addAll(0, (Collection)this.partnerList);
            this.log("load partner list");
            this.save(var1);
         } else {
            this.log("load saved pin site pref");
            this.loadSavedPinnedSite(var1);
         }

         if (var2) {
            this.log("init finished");
            this.onFirstInitComplete();
         }

      }
   }

   private final void loadSavedPinnedSite(List var1) {
      String var2 = this.pref.getString("json", "");

      try {
         JSONArray var3 = new JSONArray(var2);
         var1.addAll((Collection)this.jsonToSites(var3, false));
      } catch (JSONException var4) {
      }

   }

   @SuppressLint({"LogUsage"})
   private final void log(String var1) {
   }

   private final void onFirstInitComplete() {
      this.pref.edit().putBoolean("first_init", false).apply();
   }

   private final void save(List var1) {
      Iterator var2 = ((Iterable)var1).iterator();

      for(int var3 = 0; var2.hasNext(); ++var3) {
         Object var4 = var2.next();
         if (var3 < 0) {
            CollectionsKt.throwIndexOverflow();
         }

         ((Site)var4).setViewCount(this.getViewCountForPinSiteAt(var3));
      }

      JSONArray var5 = this.sitesToJson(var1);
      this.pref.edit().putString("json", var5.toString()).apply();
      this.log("save");
   }

   private final JSONObject siteToJson(Site var1) {
      JSONObject var4;
      try {
         JSONObject var2 = new JSONObject();
         var2.put("id", var1.getId());
         var2.put("url", var1.getUrl());
         var2.put("title", var1.getTitle());
         var2.put("favicon", var1.getFavIconUri());
         var4 = var2.put("viewCount", var1.getViewCount());
      } catch (JSONException var3) {
         var4 = null;
      }

      return var4;
   }

   private final JSONArray sitesToJson(List var1) {
      JSONArray var2 = new JSONArray();
      int var3 = ((Collection)var1).size();

      for(int var4 = 0; var4 < var3; ++var4) {
         JSONObject var5 = this.siteToJson((Site)var1.get(var4));
         if (var5 != null) {
            var2.put(var5);
         }
      }

      return var2;
   }

   public List getPinSites() {
      this.load(this.sites);
      return this.sites;
   }

   public boolean isEnabled() {
      return this.isEnabled;
   }

   public boolean isFirstTimeEnable() {
      return this.isFirstInit();
   }

   public boolean isPinned(Site var1) {
      Intrinsics.checkParameterIsNotNull(var1, "site");
      Iterable var2 = (Iterable)this.sites;
      boolean var3 = var2 instanceof Collection;
      boolean var4 = false;
      if (var3 && ((Collection)var2).isEmpty()) {
         var3 = var4;
      } else {
         Iterator var6 = var2.iterator();

         while(true) {
            var3 = var4;
            if (!var6.hasNext()) {
               break;
            }

            boolean var5;
            if (((Site)var6.next()).getId() == var1.getId()) {
               var5 = true;
            } else {
               var5 = false;
            }

            if (var5) {
               var3 = true;
               break;
            }
         }
      }

      return var3;
   }

   public void pin(Site var1) {
      Intrinsics.checkParameterIsNotNull(var1, "site");
      this.sites.add(0, new Site(var1.getId(), var1.getTitle(), var1.getUrl(), var1.getViewCount(), var1.getLastViewTimestamp(), var1.getFavIconUri()));
      this.save(this.sites);
   }

   public void unpinned(final Site var1) {
      Intrinsics.checkParameterIsNotNull(var1, "site");
      CollectionsKt.removeAll(this.sites, (Function1)(new Function1() {
         public final boolean invoke(Site var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "it");
            boolean var2;
            if (var1x.getId() == var1.getId()) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }
      }));
      this.save(this.sites);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final void resetPinSiteData(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         var1.getSharedPreferences("pin_sites", 0).edit().putBoolean("first_init", true).putString("json", "").apply();
      }
   }
}
