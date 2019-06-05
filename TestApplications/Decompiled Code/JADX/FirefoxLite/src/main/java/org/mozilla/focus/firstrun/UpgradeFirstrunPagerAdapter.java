package org.mozilla.focus.firstrun;

import android.content.Context;
import android.view.View.OnClickListener;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.home.pinsite.PinSiteManager;
import org.mozilla.rocket.home.pinsite.PinSiteManagerKt;

public class UpgradeFirstrunPagerAdapter extends FirstrunPagerAdapter {
    public UpgradeFirstrunPagerAdapter(Context context, OnClickListener onClickListener) {
        super(context, onClickListener);
        NewFeatureNotice instance = NewFeatureNotice.getInstance(context);
        int i = 0;
        if (instance.from21to40()) {
            this.pages.add(new FirstrunPage(context.getString(C0769R.string.new_name_upgrade_page_title), context.getString(C0769R.string.new_name_upgrade_page_text, new Object[]{context.getString(C0769R.string.app_name)}), 2131230897));
        }
        if (instance.from40to114() && AppConfigWrapper.hasNewsPortal(context)) {
            i = 1;
        }
        boolean shouldShowEcShoppingLinkOnboarding = instance.shouldShowEcShoppingLinkOnboarding();
        if (i != 0 || shouldShowEcShoppingLinkOnboarding) {
            instance.hasShownEcShoppingLink();
            this.pages.add(FirstRunLibrary.buildLifeFeedFirstrun(context));
        }
        PinSiteManager pinSiteManager = PinSiteManagerKt.getPinSiteManager(context);
        if (pinSiteManager.isEnabled() && pinSiteManager.isFirstTimeEnable()) {
            this.pages.add(new FirstrunPage(context.getString(C0769R.string.second_run_upgrade_page_title), context.getString(C0769R.string.second_run_upgrade_page_text), 2131230898));
        }
    }
}
