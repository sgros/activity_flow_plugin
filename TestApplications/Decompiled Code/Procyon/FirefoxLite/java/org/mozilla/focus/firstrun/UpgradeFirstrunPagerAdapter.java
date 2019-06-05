// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.firstrun;

import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.NewFeatureNotice;
import android.view.View$OnClickListener;
import android.content.Context;

public class UpgradeFirstrunPagerAdapter extends FirstrunPagerAdapter
{
    public UpgradeFirstrunPagerAdapter(final Context context, final View$OnClickListener view$OnClickListener) {
        super(context, view$OnClickListener);
        final NewFeatureNotice instance = NewFeatureNotice.getInstance(context);
        final boolean from21to40 = instance.from21to40();
        final int n = 0;
        if (from21to40) {
            this.pages.add(new FirstrunPage(context.getString(2131755275), context.getString(2131755274, new Object[] { context.getString(2131755062) }), 2131230897));
        }
        int n2 = n;
        if (instance.from40to114()) {
            n2 = n;
            if (AppConfigWrapper.hasNewsPortal(context)) {
                n2 = 1;
            }
        }
        final boolean shouldShowEcShoppingLinkOnboarding = instance.shouldShowEcShoppingLinkOnboarding();
        if (n2 != 0 || shouldShowEcShoppingLinkOnboarding) {
            instance.hasShownEcShoppingLink();
            this.pages.add(FirstRunLibrary.buildLifeFeedFirstrun(context));
        }
        final PinSiteManager pinSiteManager = PinSiteManagerKt.getPinSiteManager(context);
        if (pinSiteManager.isEnabled() && pinSiteManager.isFirstTimeEnable()) {
            this.pages.add(new FirstrunPage(context.getString(2131755394), context.getString(2131755393), 2131230898));
        }
    }
}
