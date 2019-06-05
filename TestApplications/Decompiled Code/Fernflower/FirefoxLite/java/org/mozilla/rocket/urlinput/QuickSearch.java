package org.mozilla.rocket.urlinput;

import android.net.Uri;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Regex;

public final class QuickSearch {
   private String homeUrl;
   private String icon;
   private String name;
   private boolean patternEncode;
   private boolean permitSpace;
   private String searchUrlPattern;
   private String urlPrefix;
   private String urlSuffix;

   public QuickSearch(String var1, String var2, String var3, String var4, String var5, String var6, boolean var7, boolean var8) {
      Intrinsics.checkParameterIsNotNull(var1, "name");
      Intrinsics.checkParameterIsNotNull(var2, "icon");
      Intrinsics.checkParameterIsNotNull(var3, "searchUrlPattern");
      Intrinsics.checkParameterIsNotNull(var4, "homeUrl");
      Intrinsics.checkParameterIsNotNull(var5, "urlPrefix");
      Intrinsics.checkParameterIsNotNull(var6, "urlSuffix");
      super();
      this.name = var1;
      this.icon = var2;
      this.searchUrlPattern = var3;
      this.homeUrl = var4;
      this.urlPrefix = var5;
      this.urlSuffix = var6;
      this.patternEncode = var7;
      this.permitSpace = var8;
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof QuickSearch)) {
            return false;
         }

         QuickSearch var3 = (QuickSearch)var1;
         if (!Intrinsics.areEqual(this.name, var3.name) || !Intrinsics.areEqual(this.icon, var3.icon) || !Intrinsics.areEqual(this.searchUrlPattern, var3.searchUrlPattern) || !Intrinsics.areEqual(this.homeUrl, var3.homeUrl) || !Intrinsics.areEqual(this.urlPrefix, var3.urlPrefix) || !Intrinsics.areEqual(this.urlSuffix, var3.urlSuffix)) {
            return false;
         }

         boolean var2;
         if (this.patternEncode == var3.patternEncode) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (!var2) {
            return false;
         }

         if (this.permitSpace == var3.permitSpace) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (!var2) {
            return false;
         }
      }

      return true;
   }

   public final String generateLink(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "keyword");
      String var2 = var1;
      if (!this.permitSpace) {
         CharSequence var4 = (CharSequence)var1;
         var2 = (new Regex("\\s")).replace(var4, "");
      }

      String var3;
      StringCompanionObject var5;
      Object[] var6;
      if (this.patternEncode) {
         var5 = StringCompanionObject.INSTANCE;
         var3 = this.searchUrlPattern;
         var6 = new Object[]{var2};
         var1 = String.format(var3, Arrays.copyOf(var6, var6.length));
         Intrinsics.checkExpressionValueIsNotNull(var1, "java.lang.String.format(format, *args)");
         var1 = Uri.encode(var1);
      } else {
         var5 = StringCompanionObject.INSTANCE;
         var3 = this.searchUrlPattern;
         var6 = new Object[]{Uri.encode(var2)};
         var1 = String.format(var3, Arrays.copyOf(var6, var6.length));
         Intrinsics.checkExpressionValueIsNotNull(var1, "java.lang.String.format(format, *args)");
      }

      StringBuilder var7 = new StringBuilder();
      var7.append(this.urlPrefix);
      var7.append(var1);
      var7.append(this.urlSuffix);
      return var7.toString();
   }

   public final String getHomeUrl() {
      return this.homeUrl;
   }

   public final String getIcon() {
      return this.icon;
   }

   public final String getName() {
      return this.name;
   }

   public int hashCode() {
      String var1 = this.name;
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.icon;
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.searchUrlPattern;
      int var5;
      if (var1 != null) {
         var5 = var1.hashCode();
      } else {
         var5 = 0;
      }

      var1 = this.homeUrl;
      int var6;
      if (var1 != null) {
         var6 = var1.hashCode();
      } else {
         var6 = 0;
      }

      var1 = this.urlPrefix;
      int var7;
      if (var1 != null) {
         var7 = var1.hashCode();
      } else {
         var7 = 0;
      }

      var1 = this.urlSuffix;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      byte var8 = this.patternEncode;
      byte var9 = var8;
      if (var8 != 0) {
         var9 = 1;
      }

      byte var10 = this.permitSpace;
      var8 = var10;
      if (var10 != 0) {
         var8 = 1;
      }

      return ((((((var3 * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var2) * 31 + var9) * 31 + var8;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("QuickSearch(name=");
      var1.append(this.name);
      var1.append(", icon=");
      var1.append(this.icon);
      var1.append(", searchUrlPattern=");
      var1.append(this.searchUrlPattern);
      var1.append(", homeUrl=");
      var1.append(this.homeUrl);
      var1.append(", urlPrefix=");
      var1.append(this.urlPrefix);
      var1.append(", urlSuffix=");
      var1.append(this.urlSuffix);
      var1.append(", patternEncode=");
      var1.append(this.patternEncode);
      var1.append(", permitSpace=");
      var1.append(this.permitSpace);
      var1.append(")");
      return var1.toString();
   }
}
