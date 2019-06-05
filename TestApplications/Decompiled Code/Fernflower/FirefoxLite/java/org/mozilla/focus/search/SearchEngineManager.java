package org.mozilla.focus.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.focus.locale.Locales;
import org.mozilla.focus.utils.IOUtils;
import org.mozilla.focus.utils.Settings;

public class SearchEngineManager extends BroadcastReceiver {
   private static final String LOG_TAG = "SearchEngineManager";
   private static SearchEngineManager instance = new SearchEngineManager();
   private boolean loadHasBeenTriggered = false;
   private List searchEngines;

   private SearchEngineManager() {
   }

   public static SearchEngineManager getInstance() {
      return instance;
   }

   private void loadFromDisk(Context param1) {
      // $FF: Couldn't be decompiled
   }

   private JSONArray loadSearchEngineListForLocale(Context var1) throws IOException {
      try {
         Locale var2 = Locale.getDefault();
         JSONObject var5 = IOUtils.readAsset(var1, "search/search_configuration.json");
         String var3 = Locales.getLanguageTag(var2);
         if (var5.has(var3)) {
            return var5.getJSONArray(var3);
         } else {
            String var7 = Locales.getLanguage(var2);
            if (var5.has(var7)) {
               return var5.getJSONArray(var7);
            } else {
               JSONArray var6 = var5.getJSONArray("default");
               return var6;
            }
         }
      } catch (JSONException var4) {
         throw new AssertionError("Reading search configuration failed", var4);
      }
   }

   public void awaitLoadingSearchEnginesLocked() {
      if (!this.loadHasBeenTriggered) {
         throw new IllegalStateException("Attempting to retrieve search engines without a corresponding init()");
      } else {
         while(this.searchEngines == null) {
            try {
               this.wait();
            } catch (InterruptedException var2) {
            }
         }

      }
   }

   public SearchEngine getDefaultSearchEngine(Context var1) {
      synchronized(this){}

      Throwable var10000;
      label210: {
         String var2;
         boolean var10001;
         try {
            this.awaitLoadingSearchEnginesLocked();
            var2 = Settings.getInstance(var1).getDefaultSearchEngineName();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label210;
         }

         if (var2 != null) {
            Iterator var25;
            try {
               var25 = this.searchEngines.iterator();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label210;
            }

            while(true) {
               SearchEngine var3;
               boolean var4;
               try {
                  if (!var25.hasNext()) {
                     break;
                  }

                  var3 = (SearchEngine)var25.next();
                  var4 = var2.equals(var3.getName());
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label210;
               }

               if (var4) {
                  return var3;
               }
            }
         }

         label191:
         try {
            SearchEngine var27 = (SearchEngine)this.searchEngines.get(0);
            return var27;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label191;
         }
      }

      Throwable var26 = var10000;
      throw var26;
   }

   public List getSearchEngines() {
      synchronized(this){}

      List var1;
      try {
         this.awaitLoadingSearchEnginesLocked();
         var1 = this.searchEngines;
      } finally {
         ;
      }

      return var1;
   }

   public void init(Context var1) {
      var1.registerReceiver(this, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
      this.loadSearchEngines(var1);
   }

   public void loadSearchEngines(final Context var1) {
      (new Thread("SearchEngines-Load") {
         public void run() {
            SearchEngineManager.this.loadFromDisk(var1);
         }
      }).start();
   }

   public void onReceive(Context var1, Intent var2) {
      if ("android.intent.action.LOCALE_CHANGED".equals(var2.getAction())) {
         this.loadSearchEngines(var1.getApplicationContext());
      }
   }
}
