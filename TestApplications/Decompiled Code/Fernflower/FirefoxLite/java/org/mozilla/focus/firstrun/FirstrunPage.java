package org.mozilla.focus.firstrun;

class FirstrunPage {
   public final int imageResource;
   public final String lottieString;
   public final CharSequence text;
   public final String title;

   FirstrunPage(String var1, CharSequence var2, int var3) {
      this.title = var1;
      this.text = var2;
      this.lottieString = null;
      this.imageResource = var3;
   }

   FirstrunPage(String var1, CharSequence var2, String var3) {
      this.title = var1;
      this.text = var2;
      this.lottieString = var3;
      this.imageResource = -1;
   }
}
