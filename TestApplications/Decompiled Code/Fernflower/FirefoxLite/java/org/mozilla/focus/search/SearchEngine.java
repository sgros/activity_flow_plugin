package org.mozilla.focus.search;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchEngine {
   Bitmap icon;
   private final String identifier;
   String name;
   List resultsUris;
   Uri suggestUri;

   SearchEngine(String var1) {
      this.identifier = var1;
      this.resultsUris = new ArrayList();
   }

   private String paramSubstitution(String var1, String var2) {
      String var5;
      if (VERSION.SDK_INT >= 24) {
         String var3 = Locale.getDefault().getCountry();
         StringBuilder var4 = new StringBuilder();
         var4.append(Locale.getDefault().getLanguage());
         if (TextUtils.isEmpty(var3)) {
            var5 = "";
         } else {
            StringBuilder var6 = new StringBuilder();
            var6.append("_");
            var6.append(var3);
            var5 = var6.toString();
         }

         var4.append(var5);
         var5 = var4.toString();
      } else {
         var5 = Locale.getDefault().toString();
      }

      return var1.replaceAll("\\{moz:locale\\}", var5).replaceAll("\\{moz:distributionID\\}", "").replaceAll("\\{moz:official\\}", "unofficial").replaceAll("\\{searchTerms\\??\\}", var2).replaceAll("\\{inputEncoding\\??\\}", "UTF-8").replaceAll("\\{language\\??\\}", var5).replaceAll("\\{outputEncoding\\??\\}", "UTF-8").replaceAll("\\{(?:\\w+:)?\\w+\\?\\}", "");
   }

   public String buildSearchSuggestionUrl(String var1) {
      return this.suggestUri == null ? null : this.paramSubstitution(Uri.decode(this.suggestUri.toString()), Uri.encode(var1));
   }

   public String buildSearchUrl(String var1) {
      return this.resultsUris.isEmpty() ? var1 : this.paramSubstitution(Uri.decode(((Uri)this.resultsUris.get(0)).toString()), Uri.encode(var1));
   }

   public Bitmap getIcon() {
      return this.icon;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public String getName() {
      return this.name;
   }
}
