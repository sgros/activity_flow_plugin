// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.firstrun;

import android.support.v4.view.ViewPager;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.widget.CompoundButton;
import android.widget.CompoundButton$OnCheckedChangeListener;
import org.mozilla.focus.utils.Settings;
import android.widget.Switch;
import android.view.View;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.NewFeatureNotice;
import android.view.View$OnClickListener;
import android.content.Context;

public class DefaultFirstrunPagerAdapter extends FirstrunPagerAdapter
{
    public DefaultFirstrunPagerAdapter(final Context context, final View$OnClickListener view$OnClickListener) {
        super(context, view$OnClickListener);
        this.pages.add(new FirstrunPage(context.getString(2131755203), context.getString(2131755202), "first_run_img_2.json"));
        this.pages.add(new FirstrunPage(context.getString(2131755207), context.getString(2131755206), "first_run_img_4.json"));
        this.pages.add(new FirstrunPage(context.getString(2131755209), context.getString(2131755208), 2131230899));
        this.pages.add(new FirstrunPage(context.getString(2131755205), context.getString(2131755204), "first_run_img_3.json"));
        final NewFeatureNotice instance = NewFeatureNotice.getInstance(context);
        final boolean shouldShowEcShoppingLinkOnboarding = instance.shouldShowEcShoppingLinkOnboarding();
        if (AppConfigWrapper.hasNewsPortal(context) || shouldShowEcShoppingLinkOnboarding) {
            instance.hasShownEcShoppingLink();
            this.pages.add(FirstRunLibrary.buildLifeFeedFirstrun(context));
        }
    }
    
    private void initForTurboModePage(final Context context, final View view) {
        final Switch switch1 = (Switch)view.findViewById(2131296677);
        final Settings instance = Settings.getInstance(context);
        switch1.setVisibility(0);
        switch1.setText(2131755244);
        switch1.setChecked(instance.shouldUseTurboMode());
        switch1.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)new _$$Lambda$DefaultFirstrunPagerAdapter$gQ8sDKlsdc9_zoSOCIDuMxdjROo(instance));
    }
    
    @Override
    protected View getView(final int n, final ViewPager viewPager) {
        final View view = super.getView(n, viewPager);
        if (n == 0) {
            this.initForTurboModePage(this.context, view);
        }
        return view;
    }
}
