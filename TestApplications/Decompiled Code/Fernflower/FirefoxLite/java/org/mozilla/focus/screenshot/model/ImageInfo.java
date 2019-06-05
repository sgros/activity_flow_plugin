package org.mozilla.focus.screenshot.model;

public class ImageInfo {
   public String title;

   public ImageInfo(String var1) {
      this.title = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ImageInfo{title='");
      var1.append(this.title);
      var1.append('\'');
      var1.append('}');
      return var1.toString();
   }
}
