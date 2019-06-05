package org.mozilla.focus.firstrun;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.focus.utils.Settings;

public class DefaultFirstrunPagerAdapter extends FirstrunPagerAdapter {
   public DefaultFirstrunPagerAdapter(Context var1, OnClickListener var2) {
      super(var1, var2);
      this.pages.add(new FirstrunPage(var1.getString(2131755203), var1.getString(2131755202), "first_run_img_2.json"));
      this.pages.add(new FirstrunPage(var1.getString(2131755207), var1.getString(2131755206), "first_run_img_4.json"));
      this.pages.add(new FirstrunPage(var1.getString(2131755209), var1.getString(2131755208), 2131230899));
      this.pages.add(new FirstrunPage(var1.getString(2131755205), var1.getString(2131755204), "first_run_img_3.json"));
      NewFeatureNotice var4 = NewFeatureNotice.getInstance(var1);
      boolean var3 = var4.shouldShowEcShoppingLinkOnboarding();
      if (AppConfigWrapper.hasNewsPortal(var1) || var3) {
         var4.hasShownEcShoppingLink();
         this.pages.add(FirstRunLibrary.buildLifeFeedFirstrun(var1));
      }

   }

   private void initForTurboModePage(Context var1, View var2) {
      Switch var4 = (Switch)var2.findViewById(2131296677);
      Settings var3 = Settings.getInstance(var1);
      var4.setVisibility(0);
      var4.setText(2131755244);
      var4.setChecked(var3.shouldUseTurboMode());
      var4.setOnCheckedChangeListener(new _$$Lambda$DefaultFirstrunPagerAdapter$gQ8sDKlsdc9_zoSOCIDuMxdjROo(var3));
   }

   // $FF: synthetic method
   static void lambda$initForTurboModePage$0(Settings var0, CompoundButton var1, boolean var2) {
      var0.setTurboMode(var2);
      TelemetryWrapper.toggleFirstRunPageEvent(var2);
   }

   protected View getView(int var1, ViewPager var2) {
      View var3 = super.getView(var1, var2);
      if (var1 == 0) {
         this.initForTurboModePage(this.context, var3);
      }

      return var3;
   }
}
