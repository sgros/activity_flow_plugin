package org.mozilla.focus.widget;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

public class AboutPreference extends Preference {
   public AboutPreference(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public AboutPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.setTitle(this.getContext().getResources().getString(2131755341));
   }
}
