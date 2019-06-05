package org.mozilla.lite.newspoint;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.lite.partner.NewsItem;

public final class NewsPointItem implements NewsItem {
   private final String category;
   private final String dm;
   private final String fu;
   private final String id;
   private final String imageUrl;
   private final String imageid;
   private final String lang;
   private final Long lid;
   private final String m;
   private final String newsUrl;
   private final String partner;
   private final Long pid;
   private final String pnu;
   private final String source;
   private final String subcategory;
   private final List tags;
   private final long time;
   private final String title;
   private final String wu;

   public NewsPointItem(String var1, String var2, String var3, String var4, long var5, String var7, String var8, String var9, Long var10, Long var11, String var12, String var13, String var14, String var15, String var16, String var17, String var18, List var19) {
      Intrinsics.checkParameterIsNotNull(var1, "id");
      Intrinsics.checkParameterIsNotNull(var3, "title");
      Intrinsics.checkParameterIsNotNull(var4, "newsUrl");
      super();
      this.id = var1;
      this.imageUrl = var2;
      this.title = var3;
      this.newsUrl = var4;
      this.time = var5;
      this.imageid = var7;
      this.partner = var8;
      this.dm = var9;
      this.pid = var10;
      this.lid = var11;
      this.lang = var12;
      this.category = var13;
      this.wu = var14;
      this.pnu = var15;
      this.fu = var16;
      this.subcategory = var17;
      this.m = var18;
      this.tags = var19;
      this.source = "Newspoint";
   }

   public boolean equals(Object var1) {
      if (this != var1) {
         if (!(var1 instanceof NewsPointItem)) {
            return false;
         }

         NewsPointItem var3 = (NewsPointItem)var1;
         if (!Intrinsics.areEqual(this.getId(), var3.getId()) || !Intrinsics.areEqual(this.getImageUrl(), var3.getImageUrl()) || !Intrinsics.areEqual(this.getTitle(), var3.getTitle()) || !Intrinsics.areEqual(this.getNewsUrl(), var3.getNewsUrl())) {
            return false;
         }

         boolean var2;
         if (this.getTime() == var3.getTime()) {
            var2 = true;
         } else {
            var2 = false;
         }

         if (!var2 || !Intrinsics.areEqual(this.imageid, var3.imageid) || !Intrinsics.areEqual(this.getPartner(), var3.getPartner()) || !Intrinsics.areEqual(this.dm, var3.dm) || !Intrinsics.areEqual(this.pid, var3.pid) || !Intrinsics.areEqual(this.lid, var3.lid) || !Intrinsics.areEqual(this.lang, var3.lang) || !Intrinsics.areEqual(this.getCategory(), var3.getCategory()) || !Intrinsics.areEqual(this.wu, var3.wu) || !Intrinsics.areEqual(this.pnu, var3.pnu) || !Intrinsics.areEqual(this.fu, var3.fu) || !Intrinsics.areEqual(this.getSubcategory(), var3.getSubcategory()) || !Intrinsics.areEqual(this.m, var3.m) || !Intrinsics.areEqual(this.tags, var3.tags)) {
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
      var1 = this.imageid;
      int var10;
      if (var1 != null) {
         var10 = var1.hashCode();
      } else {
         var10 = 0;
      }

      var1 = this.getPartner();
      int var11;
      if (var1 != null) {
         var11 = var1.hashCode();
      } else {
         var11 = 0;
      }

      var1 = this.dm;
      int var12;
      if (var1 != null) {
         var12 = var1.hashCode();
      } else {
         var12 = 0;
      }

      Long var22 = this.pid;
      int var13;
      if (var22 != null) {
         var13 = var22.hashCode();
      } else {
         var13 = 0;
      }

      var22 = this.lid;
      int var14;
      if (var22 != null) {
         var14 = var22.hashCode();
      } else {
         var14 = 0;
      }

      var1 = this.lang;
      int var15;
      if (var1 != null) {
         var15 = var1.hashCode();
      } else {
         var15 = 0;
      }

      var1 = this.getCategory();
      int var16;
      if (var1 != null) {
         var16 = var1.hashCode();
      } else {
         var16 = 0;
      }

      var1 = this.wu;
      int var17;
      if (var1 != null) {
         var17 = var1.hashCode();
      } else {
         var17 = 0;
      }

      var1 = this.pnu;
      int var18;
      if (var1 != null) {
         var18 = var1.hashCode();
      } else {
         var18 = 0;
      }

      var1 = this.fu;
      int var19;
      if (var1 != null) {
         var19 = var1.hashCode();
      } else {
         var19 = 0;
      }

      var1 = this.getSubcategory();
      int var20;
      if (var1 != null) {
         var20 = var1.hashCode();
      } else {
         var20 = 0;
      }

      var1 = this.m;
      int var21;
      if (var1 != null) {
         var21 = var1.hashCode();
      } else {
         var21 = 0;
      }

      List var23 = this.tags;
      if (var23 != null) {
         var2 = var23.hashCode();
      }

      return ((((((((((((((((var3 * 31 + var4) * 31 + var5) * 31 + var6) * 31 + var9) * 31 + var10) * 31 + var11) * 31 + var12) * 31 + var13) * 31 + var14) * 31 + var15) * 31 + var16) * 31 + var17) * 31 + var18) * 31 + var19) * 31 + var20) * 31 + var21) * 31 + var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("NewsPointItem(id=");
      var1.append(this.getId());
      var1.append(", imageUrl=");
      var1.append(this.getImageUrl());
      var1.append(", title=");
      var1.append(this.getTitle());
      var1.append(", newsUrl=");
      var1.append(this.getNewsUrl());
      var1.append(", time=");
      var1.append(this.getTime());
      var1.append(", imageid=");
      var1.append(this.imageid);
      var1.append(", partner=");
      var1.append(this.getPartner());
      var1.append(", dm=");
      var1.append(this.dm);
      var1.append(", pid=");
      var1.append(this.pid);
      var1.append(", lid=");
      var1.append(this.lid);
      var1.append(", lang=");
      var1.append(this.lang);
      var1.append(", category=");
      var1.append(this.getCategory());
      var1.append(", wu=");
      var1.append(this.wu);
      var1.append(", pnu=");
      var1.append(this.pnu);
      var1.append(", fu=");
      var1.append(this.fu);
      var1.append(", subcategory=");
      var1.append(this.getSubcategory());
      var1.append(", m=");
      var1.append(this.m);
      var1.append(", tags=");
      var1.append(this.tags);
      var1.append(")");
      return var1.toString();
   }
}
