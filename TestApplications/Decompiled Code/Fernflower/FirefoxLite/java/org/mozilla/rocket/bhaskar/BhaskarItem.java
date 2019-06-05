package org.mozilla.rocket.bhaskar;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.NewsItem;

public final class BhaskarItem implements NewsItem {
   private final String articleFrom;
   private final String category;
   private final String city;
   private final String description;
   private final String id;
   private final String imageUrl;
   private final String keywords;
   private final String language;
   private final String newsUrl;
   private final String partner;
   private final String province;
   private final String source;
   private final String subcategory;
   private final String summary;
   private final List tags;
   private final long time;
   private final String title;

   public BhaskarItem(String var1, String var2, String var3, String var4, long var5, String var7, String var8, String var9, String var10, String var11, String var12, List var13, String var14, String var15, String var16) {
      Intrinsics.checkParameterIsNotNull(var1, "id");
      Intrinsics.checkParameterIsNotNull(var3, "title");
      Intrinsics.checkParameterIsNotNull(var4, "newsUrl");
      super();
      this.id = var1;
      this.imageUrl = var2;
      this.title = var3;
      this.newsUrl = var4;
      this.time = var5;
      this.summary = var7;
      this.language = var8;
      this.category = var9;
      this.subcategory = var10;
      this.keywords = var11;
      this.description = var12;
      this.tags = var13;
      this.articleFrom = var14;
      this.province = var15;
      this.city = var16;
      this.source = "DainikBhaskar.com";
      this.partner = "DainikBhaskar.com";
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof BhaskarItem)) {
            return false;
         }

         BhaskarItem var3 = (BhaskarItem)var1;
         if (!Intrinsics.areEqual(this.getId(), var3.getId()) || !Intrinsics.areEqual(this.getImageUrl(), var3.getImageUrl()) || !Intrinsics.areEqual(this.getTitle(), var3.getTitle()) || !Intrinsics.areEqual(this.getNewsUrl(), var3.getNewsUrl())) {
            return false;
         }

         boolean var2;
         if (this.getTime() == var3.getTime()) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (!var2 || !Intrinsics.areEqual(this.summary, var3.summary) || !Intrinsics.areEqual(this.language, var3.language) || !Intrinsics.areEqual(this.getCategory(), var3.getCategory()) || !Intrinsics.areEqual(this.getSubcategory(), var3.getSubcategory()) || !Intrinsics.areEqual(this.keywords, var3.keywords) || !Intrinsics.areEqual(this.description, var3.description) || !Intrinsics.areEqual(this.tags, var3.tags) || !Intrinsics.areEqual(this.articleFrom, var3.articleFrom) || !Intrinsics.areEqual(this.province, var3.province) || !Intrinsics.areEqual(this.city, var3.city)) {
            return false;
         }
      }

      return true;
   }

   public String getCategory() {
      return this.category;
   }

   public String getId() {
      return this.id;
   }

   public String getImageUrl() {
      return this.imageUrl;
   }

   public String getNewsUrl() {
      return this.newsUrl;
   }

   public String getPartner() {
      return this.partner;
   }

   public String getSource() {
      return this.source;
   }

   public String getSubcategory() {
      return this.subcategory;
   }

   public long getTime() {
      return this.time;
   }

   public String getTitle() {
      return this.title;
   }

   public int hashCode() {
      String var1 = this.getId();
      int var2 = 0;
      int var3;
      if (var1 != null) {
         var3 = var1.hashCode();
      } else {
         var3 = 0;
      }

      var1 = this.getImageUrl();
      int var4;
      if (var1 != null) {
         var4 = var1.hashCode();
      } else {
         var4 = 0;
      }

      var1 = this.getTitle();
      int var5;
      if (var1 != null) {
         var5 = var1.hashCode();
      } else {
         var5 = 0;
      }

      var1 = this.getNewsUrl();
      int var6;
      if (var1 != null) {
         var6 = var1.hashCode();
      } else {
         var6 = 0;
      }

      long var7 = this.getTime();
      int var9 = (int)(var7 ^ var7 >>> 32);
      var1 = this.summary;
      int var10;
      if (var1 != null) {
         var10 = var1.hashCode();
      } else {
         var10 = 0;
      }

      var1 = this.language;
      int var11;
      if (var1 != null) {
         var11 = var1.hashCode();
      } else {
         var11 = 0;
      }

      var1 = this.getCategory();
      int var12;
      if (var1 != null) {
         var12 = var1.hashCode();
      } else {
         var12 = 0;
      }

      var1 = this.getSubcategory();
      int var13;
      if (var1 != null) {
         var13 = var1.hashCode();
      } else {
         var13 = 0;
      }

      var1 = this.keywords;
      int var14;
      if (var1 != null) {
         var14 = var1.hashCode();
      } else {
         var14 = 0;
      }

      var1 = this.description;
      int var15;
      if (var1 != null) {
         var15 = var1.hashCode();
      } else {
         var15 = 0;
      }

      List var19 = this.tags;
      int var16;
      if (var19 != null) {
         var16 = var19.hashCode();
      } else {
         var16 = 0;
      }

      var1 = this.articleFrom;
      int var17;
      if (var1 != null) {
         var17 = var1.hashCode();
      } else {
         var17 = 0;
      }

      var1 = this.province;
      int var18;
      if (var1 != null) {
         var18 = var1.hashCode();
      } else {
         var18 = 0;
      }

      var1 = this.city;
      if (var1 != null) {
         var2 = var1.hashCode();
      }

      return (((((((((((((var3 * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var9) * 31 + var10) * 31 + var11) * 31 + var12) * 31 + var13) * 31 + var14) * 31 + var15) * 31 + var16) * 31 + var17) * 31 + var18) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("BhaskarItem(id=");
      var1.append(this.getId());
      var1.append(", imageUrl=");
      var1.append(this.getImageUrl());
      var1.append(", title=");
      var1.append(this.getTitle());
      var1.append(", newsUrl=");
      var1.append(this.getNewsUrl());
      var1.append(", time=");
      var1.append(this.getTime());
      var1.append(", summary=");
      var1.append(this.summary);
      var1.append(", language=");
      var1.append(this.language);
      var1.append(", category=");
      var1.append(this.getCategory());
      var1.append(", subcategory=");
      var1.append(this.getSubcategory());
      var1.append(", keywords=");
      var1.append(this.keywords);
      var1.append(", description=");
      var1.append(this.description);
      var1.append(", tags=");
      var1.append(this.tags);
      var1.append(", articleFrom=");
      var1.append(this.articleFrom);
      var1.append(", province=");
      var1.append(this.province);
      var1.append(", city=");
      var1.append(this.city);
      var1.append(")");
      return var1.toString();
   }
}
