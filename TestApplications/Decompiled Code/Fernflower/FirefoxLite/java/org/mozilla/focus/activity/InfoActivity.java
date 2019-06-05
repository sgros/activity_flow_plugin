package org.mozilla.focus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.fragment.InfoFragment;
import org.mozilla.focus.locale.Locales;

public class InfoActivity extends BaseActivity {
   private InfoFragment infoFragment;

   public static final Intent getAboutIntent(Context var0) {
      return getIntentFor(var0, "focusabout:", Locales.getLocalizedResources(var0).getString(2131755247));
   }

   public static final Intent getIntentFor(Context var0, String var1, String var2) {
      Intent var3 = new Intent(var0, InfoActivity.class);
      var3.putExtra("extra_url", var1);
      var3.putExtra("extra_title", var2);
      return var3;
   }

   public void applyLocale() {
   }

   public void onBackPressed() {
      if (this.infoFragment != null && this.infoFragment.canGoBack()) {
         this.infoFragment.goBack();
      } else {
         super.onBackPressed();
      }
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131492893);
      String var2 = this.getIntent().getStringExtra("extra_url");
      String var3 = this.getIntent().getStringExtra("extra_title");
      this.infoFragment = InfoFragment.create(var2);
      this.getSupportFragmentManager().beginTransaction().replace(2131296483, this.infoFragment).commit();
      Toolbar var4 = (Toolbar)this.findViewById(2131296700);
      var4.setTitle(var3);
      this.setSupportActionBar(var4);
      this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      this.getSupportActionBar().setDisplayShowHomeEnabled(true);
      var4.setNavigationOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            InfoActivity.this.finish();
         }
      });
   }
}
