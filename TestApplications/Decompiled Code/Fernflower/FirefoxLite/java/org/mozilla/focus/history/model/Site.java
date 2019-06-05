package org.mozilla.focus.history.model;

import org.mozilla.focus.utils.AppConstants;

public class Site {
   private String favIconUri;
   private long id;
   private boolean isDefault = false;
   private long lastViewTimestamp;
   private String title;
   private String url;
   private long viewCount;

   public Site(long var1, String var3, String var4, long var5, long var7, String var9) {
      this.id = var1;
      this.title = var3;
      this.url = var4;
      this.viewCount = var5;
      this.lastViewTimestamp = var7;
      this.favIconUri = var9;
   }

   private String toStringNormal() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Site{id='");
      var1.append(this.id);
      var1.append('\'');
      var1.append(", title='");
      var1.append(this.title);
      var1.append('\'');
      var1.append(", url='");
      var1.append(this.url);
      var1.append('\'');
      var1.append(", viewCount='");
      var1.append(this.viewCount);
      var1.append('\'');
      var1.append(", lastViewTimestamp='");
      var1.append(this.lastViewTimestamp);
      var1.append('\'');
      var1.append(", favIconUri='");
      var1.append(this.favIconUri);
      var1.append('\'');
      var1.append('}');
      return var1.toString();
   }

   private String toStringRelease() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Site{id='");
      var1.append(this.id);
      var1.append('\'');
      var1.append(", viewCount='");
      var1.append(this.viewCount);
      var1.append('\'');
      var1.append(", lastViewTimestamp='");
      var1.append(this.lastViewTimestamp);
      var1.append('\'');
      var1.append('}');
      return var1.toString();
   }

   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof Site && ((Site)var1).getId() == this.getId()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public String getFavIconUri() {
      return this.favIconUri;
   }

   public long getId() {
      return this.id;
   }

   public long getLastViewTimestamp() {
      return this.lastViewTimestamp;
   }

   public String getTitle() {
      return this.title;
   }

   public String getUrl() {
      return this.url;
   }

   public long getViewCount() {
      return this.viewCount;
   }

   public int hashCode() {
      return super.hashCode();
   }

   public boolean isDefault() {
      return this.isDefault;
   }

   public void setDefault(boolean var1) {
      this.isDefault = var1;
   }

   public void setViewCount(long var1) {
      this.viewCount = var1;
   }

   public String toString() {
      String var1;
      if (AppConstants.isReleaseBuild()) {
         var1 = this.toStringRelease();
      } else {
         var1 = this.toStringNormal();
      }

      return var1;
   }
}
