package org.mozilla.focus.screenshot.model;

import java.io.Serializable;

public class Screenshot implements Serializable {
   private String category = "";
   private int categoryVersion = 0;
   private long id;
   private String imageUri;
   private long timestamp;
   private String title;
   private String url;

   public Screenshot() {
   }

   public Screenshot(String var1, String var2, long var3, String var5) {
      this.title = var1;
      this.url = var2;
      this.timestamp = var3;
      this.imageUri = var5;
   }

   public String getCategory() {
      return this.category;
   }

   public int getCategoryVersion() {
      return this.categoryVersion;
   }

   public long getId() {
      return this.id;
   }

   public String getImageUri() {
      return this.imageUri;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public String getTitle() {
      return this.title;
   }

   public String getUrl() {
      return this.url;
   }

   public void setCategory(String var1) {
      this.category = var1;
   }

   public void setCategoryVersion(int var1) {
      this.categoryVersion = var1;
   }

   public void setId(long var1) {
      this.id = var1;
   }

   public void setImageUri(String var1) {
      this.imageUri = var1;
   }

   public void setTimestamp(long var1) {
      this.timestamp = var1;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public void setUrl(String var1) {
      this.url = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Screenshot{id=");
      var1.append(this.id);
      var1.append(", title='");
      var1.append(this.title);
      var1.append('\'');
      var1.append(", url='");
      var1.append(this.url);
      var1.append('\'');
      var1.append(", timestamp=");
      var1.append(this.timestamp);
      var1.append(", imageUri='");
      var1.append(this.imageUri);
      var1.append('\'');
      var1.append('}');
      return var1.toString();
   }
}
