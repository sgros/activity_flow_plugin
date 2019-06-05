package org.mozilla.focus.firstrun;

import android.content.Context;
import android.view.View.OnClickListener;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;

public class UpgradeFirstrunPagerAdapter extends FirstrunPagerAdapter {
   public UpgradeFirstrunPagerAdapter(Context var1, OnClickListener var2) {
      super(var1, var2);
      NewFeatureNotice var6 = NewFeatureNotice.getInstance(var1);
      boolean var3 = var6.from21to40();
      boolean var4 = false;
      if (var3) {
         this.pages.add(new FirstrunPage(var1.getString(2131755275), var1.getString(2131755274, new Object[]{var1.getString(2131755062)}), 2131230897));
      }

      boolean var5 = var4;
      if (var6.from40to114()) {
         var5 = var4;
         if (AppConfigWrapper.hasNewsPortal(var1)) {
            var5 = true;
         }
      }

      var3 = var6.shouldShowEcShoppingLinkOnboarding();
      if (var5 || var3) {
         var6.hasShownEcShoppingLink();
         this.pages.add(FirstRunLibrary.buildLifeFeedFirstrun(var1));
      }

      PinSiteManager var7 = PinSiteManagerKt.getPinSiteManager(var1);
      if (var7.isEnabled() && var7.isFirstTimeEnable()) {
         this.pages.add(new FirstrunPage(var1.getString(2131755394), var1.getString(2131755393), 2131230898));
      }

   }
}
