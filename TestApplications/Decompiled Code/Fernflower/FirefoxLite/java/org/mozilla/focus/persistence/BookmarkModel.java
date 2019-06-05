package org.mozilla.focus.persistence;

public class BookmarkModel {
   private String id;
   private String title;
   private String url;

   public BookmarkModel(String var1, String var2, String var3) {
      this.id = var1;
      this.title = var2;
      this.url = var3;
   }

   public String getId() {
      return this.id;
   }

   public String getTitle() {
      return this.title;
   }

   public String getUrl() {
      return this.url;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("BookmarkModel{id='");
      var1.append(this.id);
      var1.append('\'');
      var1.append(", title='");
      var1.append(this.title);
      var1.append('\'');
      var1.append(", url='");
      var1.append(this.url);
      var1.append('\'');
      var1.append('}');
      return var1.toString();
   }
}
