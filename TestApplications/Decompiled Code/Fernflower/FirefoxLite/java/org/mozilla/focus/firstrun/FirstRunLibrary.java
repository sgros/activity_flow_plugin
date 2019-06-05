package org.mozilla.focus.firstrun;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.utils.SupportUtils;

public class FirstRunLibrary {
   public static FirstrunPage buildLifeFeedFirstrun(Context var0) {
      String var1 = SupportUtils.getSumoURLForTopic(var0, "firefox-lite-feed");
      String var2 = var0.getString(2131755245);
      String var3 = var0.getString(2131755049);
      Spannable var4 = linkTextSpan(var0, var0.getString(2131755210, new Object[]{var2, "%s"}), var3, var1, var2);
      return new FirstrunPage(var0.getString(2131755211), var4, 2131230952);
   }

   private static Spannable linkTextSpan(final Context var0, String var1, String var2, final String var3, final String var4) {
      var1 = String.format(var1, var2);
      int var5 = var1.indexOf(var2);
      int var6 = var2.length();
      ClickableSpan var7 = new ClickableSpan() {
         public void onClick(View var1) {
            var0.startActivity(InfoActivity.getIntentFor(var0, var3, var4));
         }
      };
      SpannableStringBuilder var8 = new SpannableStringBuilder(var1);
      var8.setSpan(var7, var5, var6 + var5, 18);
      return var8;
   }
}
