package org.mozilla.focus.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.settings.SettingsFragment;

public class SettingsActivity extends BaseActivity {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;

   public void applyLocale() {
      this.setTitle(2131755252);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131492897);
      Toolbar var2 = (Toolbar)this.findViewById(2131296700);
      this.setSupportActionBar(var2);
      this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      var2.setNavigationOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            SettingsActivity.this.finish();
         }
      });
      this.getFragmentManager().beginTransaction().replace(2131296374, new SettingsFragment()).commit();
      this.applyLocale();
   }
}
