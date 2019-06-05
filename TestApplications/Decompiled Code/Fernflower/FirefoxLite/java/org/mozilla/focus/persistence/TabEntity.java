package org.mozilla.focus.persistence;

public class TabEntity {
   private String id;
   private String parentId;
   private String title;
   private String url;

   public TabEntity(String var1, String var2) {
      this(var1, var2, "", "");
   }

   public TabEntity(String var1, String var2, String var3, String var4) {
      this.id = var1;
      this.parentId = var2;
      this.title = var3;
      this.url = var4;
   }

   public String getId() {
      return this.id;
   }

   public String getParentId() {
      return this.parentId;
   }

   public String getTitle() {
      return this.title;
   }

   public String getUrl() {
      return this.url;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public void setUrl(String var1) {
      this.url = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("TabEntity{id='");
      var1.append(this.id);
      var1.append('\'');
      var1.append(", parentId='");
      var1.append(this.parentId);
      var1.append('\'');
      var1.append(", title='");
      var1.append(this.title);
      var1.append('\'');
      var1.append(", url='");
      var1.append(this.url);
      var1.append('}');
      return var1.toString();
   }
}
