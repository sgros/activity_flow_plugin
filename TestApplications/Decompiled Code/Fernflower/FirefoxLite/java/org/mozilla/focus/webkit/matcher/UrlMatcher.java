package org.mozilla.focus.webkit.matcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.util.ArrayMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.mozilla.focus.webkit.matcher.util.FocusString;

public class UrlMatcher implements OnSharedPreferenceChangeListener {
   private static final String[] WEBFONT_EXTENSIONS = new String[]{".woff2", ".woff", ".eot", ".ttf", ".otf"};
   private boolean blockWebfonts = true;
   private final Map categories;
   private final Map categoryPrefMap;
   private final Set enabledCategories = new HashSet();
   private final EntityList entityList;
   private final HashSet previouslyMatched = new HashSet();
   private final HashSet previouslyUnmatched = new HashSet();

   UrlMatcher(Context var1, Map var2, Map var3, EntityList var4) {
      this.categoryPrefMap = var2;
      this.entityList = var4;
      this.categories = var3;
      Iterator var5 = var3.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         if (!var2.values().contains(var6.getKey())) {
            throw new IllegalArgumentException("categoryMap contains undeclared category");
         }

         this.enabledCategories.add(var6.getKey());
      }

      this.loadPrefs(var1);
      PreferenceManager.getDefaultSharedPreferences(var1).registerOnSharedPreferenceChangeListener(this);
   }

   private static Map loadDefaultPrefMap(Context var0) {
      ArrayMap var1 = new ArrayMap();
      var1.put(var0.getString(2131755319), "Advertising");
      var1.put(var0.getString(2131755320), "Analytics");
      var1.put(var0.getString(2131755322), "Social");
      var1.put(var0.getString(2131755321), "Content");
      var1.put(var0.getString(2131755318), "ABPIndo");
      var1.put(var0.getString(2131755317), "Webfonts");
      return Collections.unmodifiableMap(var1);
   }

   public static UrlMatcher loadMatcher(Context param0, int param1, int[] param2, int param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   private void loadPrefs(Context var1) {
      SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(var1);
      Iterator var3 = this.categoryPrefMap.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         boolean var5 = var2.getBoolean((String)var4.getKey(), this.shouldDefaultBlock(var1, (String)var4.getKey()));
         this.setCategoryEnabled((String)var4.getValue(), var5);
      }

   }

   private boolean shouldDefaultBlock(Context var1, String var2) {
      String var3 = var1.getString(2131755319);
      String var4 = var1.getString(2131755320);
      String var5 = var1.getString(2131755322);
      String var7 = var1.getString(2131755318);
      boolean var6;
      if (!var3.equals(var2) && !var4.equals(var2) && !var5.equals(var2) && !var7.equals(var2)) {
         var6 = false;
      } else {
         var6 = true;
      }

      return var6;
   }

   public Set getCategories() {
      return this.categories.keySet();
   }

   public boolean matches(Uri var1, Uri var2) {
      String var3 = var1.getPath();
      if (var3 == null) {
         return false;
      } else {
         if (this.blockWebfonts) {
            String[] var4 = WEBFONT_EXTENSIONS;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               if (var3.endsWith(var4[var6])) {
                  return true;
               }
            }
         }

         String var12 = var1.toString();
         if (this.previouslyUnmatched.contains(var12)) {
            return false;
         } else if (this.entityList != null && this.entityList.isWhiteListed(var2, var1)) {
            return false;
         } else {
            String var7 = var1.getHost();
            String var9 = var2.getHost();
            if (var9 != null && var9.equals(var7)) {
               return false;
            } else if (this.previouslyMatched.contains(var12)) {
               return true;
            } else {
               FocusString var11 = FocusString.create(var7).reverse();
               Iterator var8 = this.categories.entrySet().iterator();

               Entry var10;
               do {
                  if (!var8.hasNext()) {
                     this.previouslyUnmatched.add(var12);
                     return false;
                  }

                  var10 = (Entry)var8.next();
               } while(!this.enabledCategories.contains(var10.getKey()) || ((Trie)var10.getValue()).findNode(var11) == null);

               this.previouslyMatched.add(var12);
               return true;
            }
         }
      }
   }

   public void onSharedPreferenceChanged(SharedPreferences var1, String var2) {
      String var3 = (String)this.categoryPrefMap.get(var2);
      if (var3 != null) {
         this.setCategoryEnabled(var3, var1.getBoolean(var2, false));
      }

   }

   public void setCategoryEnabled(String var1, boolean var2) {
      if ("Webfonts".equals(var1)) {
         this.blockWebfonts = var2;
      } else if (this.getCategories().contains(var1)) {
         if (var2) {
            if (this.enabledCategories.contains(var1)) {
               return;
            }

            this.enabledCategories.add(var1);
            this.previouslyUnmatched.clear();
         } else {
            if (!this.enabledCategories.contains(var1)) {
               return;
            }

            this.enabledCategories.remove(var1);
            this.previouslyMatched.clear();
         }

      } else {
         throw new IllegalArgumentException("Can't enable/disable inexistant category");
      }
   }
}
