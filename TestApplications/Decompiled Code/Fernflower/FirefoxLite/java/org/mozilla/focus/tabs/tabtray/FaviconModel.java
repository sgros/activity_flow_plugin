package org.mozilla.focus.tabs.tabtray;

import android.graphics.Bitmap;

public class FaviconModel {
   Bitmap originalIcon;
   int type;
   String url;

   FaviconModel(String var1, int var2, Bitmap var3) {
      this.url = var1;
      this.type = var2;
      this.originalIcon = var3;
   }
}
