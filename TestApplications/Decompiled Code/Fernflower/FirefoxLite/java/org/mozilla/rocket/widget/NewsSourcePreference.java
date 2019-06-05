package org.mozilla.rocket.widget;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.content.NewsSourceManager;

public class NewsSourcePreference extends ListPreference {
   public NewsSourcePreference(Context var1) {
      super(var1);
   }

   public NewsSourcePreference(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public NewsSourcePreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   public NewsSourcePreference(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   protected void onAttachedToActivity() {
      super.onAttachedToActivity();
      String[] var1 = new String[]{"DainikBhaskar.com", "Newspoint"};
      this.setEntries(var1);
      this.setEntryValues(var1);
      this.setSummary(this.getValue());
   }

   protected void onDialogClosed(boolean var1) {
      super.onDialogClosed(var1);
      if (var1) {
         this.persistString(this.getValue());
         Settings.getInstance(this.getContext()).setPriority("pref_int_news_priority", 2);
         this.setSummary(this.getValue());
         NewsSourceManager.getInstance().setNewsSource(this.getValue());
         NewsSourceManager.getInstance().setNewsSourceUrl(AppConfigWrapper.getLifeFeedProviderUrl(this.getContext(), this.getValue()));
         StringBuilder var2 = new StringBuilder();
         var2.append("User setup pref:");
         var2.append(this.getValue());
         Log.d("NewsSource", var2.toString());
      }

   }
}
