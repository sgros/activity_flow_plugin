package org.mozilla.focus.firstrun;

import android.content.Context;
import android.support.p001v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.focus.utils.Settings;
import org.mozilla.rocket.C0769R;

public class DefaultFirstrunPagerAdapter extends FirstrunPagerAdapter {
    public DefaultFirstrunPagerAdapter(Context context, OnClickListener onClickListener) {
        super(context, onClickListener);
        this.pages.add(new FirstrunPage(context.getString(C0769R.string.first_run_page2_title), context.getString(C0769R.string.first_run_page2_text), "first_run_img_2.json"));
        this.pages.add(new FirstrunPage(context.getString(C0769R.string.first_run_page4_title), context.getString(C0769R.string.first_run_page4_text), "first_run_img_4.json"));
        this.pages.add(new FirstrunPage(context.getString(C0769R.string.first_run_page5_title), context.getString(C0769R.string.first_run_page5_text), 2131230899));
        this.pages.add(new FirstrunPage(context.getString(C0769R.string.first_run_page3_title), context.getString(C0769R.string.first_run_page3_text), "first_run_img_3.json"));
        NewFeatureNotice instance = NewFeatureNotice.getInstance(context);
        boolean shouldShowEcShoppingLinkOnboarding = instance.shouldShowEcShoppingLinkOnboarding();
        if (AppConfigWrapper.hasNewsPortal(context) || shouldShowEcShoppingLinkOnboarding) {
            instance.hasShownEcShoppingLink();
            this.pages.add(FirstRunLibrary.buildLifeFeedFirstrun(context));
        }
    }

    /* Access modifiers changed, original: protected */
    public View getView(int i, ViewPager viewPager) {
        View view = super.getView(i, viewPager);
        if (i == 0) {
            initForTurboModePage(this.context, view);
        }
        return view;
    }

    private void initForTurboModePage(Context context, View view) {
        Switch switchR = (Switch) view.findViewById(C0427R.C0426id.switch_widget);
        Settings instance = Settings.getInstance(context);
        switchR.setVisibility(0);
        switchR.setText(C0769R.string.label_menu_turbo_mode);
        switchR.setChecked(instance.shouldUseTurboMode());
        switchR.setOnCheckedChangeListener(new C0456xd6a2eaf5(instance));
    }

    static /* synthetic */ void lambda$initForTurboModePage$0(Settings settings, CompoundButton compoundButton, boolean z) {
        settings.setTurboMode(z);
        TelemetryWrapper.toggleFirstRunPageEvent(z);
    }
}
