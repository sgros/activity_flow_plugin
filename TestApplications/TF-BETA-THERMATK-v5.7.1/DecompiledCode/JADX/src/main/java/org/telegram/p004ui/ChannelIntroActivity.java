package org.telegram.p004ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;

/* renamed from: org.telegram.ui.ChannelIntroActivity */
public class ChannelIntroActivity extends BaseFragment {
    private TextView createChannelText;
    private TextView descriptionText;
    private ImageView imageView;
    private TextView whatIsChannelText;

    /* renamed from: org.telegram.ui.ChannelIntroActivity$1 */
    class C39601 extends ActionBarMenuOnItemClick {
        C39601() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                ChannelIntroActivity.this.finishFragment();
            }
        }
    }

    public View createView(Context context) {
        C2190ActionBar c2190ActionBar = this.actionBar;
        String str = Theme.key_windowBackgroundWhite;
        c2190ActionBar.setBackgroundColor(Theme.getColor(str));
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setItemsColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2), false);
        this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
        this.actionBar.setCastShadows(false);
        if (!AndroidUtilities.isTablet()) {
            this.actionBar.showActionModeTop();
        }
        this.actionBar.setActionBarMenuOnItemClick(new C39601());
        this.fragmentView = new ViewGroup(context) {
            /* Access modifiers changed, original: protected */
            public void onMeasure(int i, int i2) {
                i = MeasureSpec.getSize(i);
                i2 = MeasureSpec.getSize(i2);
                if (i > i2) {
                    float f = (float) i;
                    ChannelIntroActivity.this.imageView.measure(MeasureSpec.makeMeasureSpec((int) (0.45f * f), 1073741824), MeasureSpec.makeMeasureSpec((int) (((float) i2) * 0.78f), 1073741824));
                    int i3 = (int) (0.6f * f);
                    ChannelIntroActivity.this.whatIsChannelText.measure(MeasureSpec.makeMeasureSpec(i3, 1073741824), MeasureSpec.makeMeasureSpec(i2, 0));
                    ChannelIntroActivity.this.descriptionText.measure(MeasureSpec.makeMeasureSpec((int) (f * 0.5f), 1073741824), MeasureSpec.makeMeasureSpec(i2, 0));
                    ChannelIntroActivity.this.createChannelText.measure(MeasureSpec.makeMeasureSpec(i3, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(24.0f), 1073741824));
                } else {
                    ChannelIntroActivity.this.imageView.measure(MeasureSpec.makeMeasureSpec(i, 1073741824), MeasureSpec.makeMeasureSpec((int) (((float) i2) * 0.44f), 1073741824));
                    ChannelIntroActivity.this.whatIsChannelText.measure(MeasureSpec.makeMeasureSpec(i, 1073741824), MeasureSpec.makeMeasureSpec(i2, 0));
                    ChannelIntroActivity.this.descriptionText.measure(MeasureSpec.makeMeasureSpec((int) (((float) i) * 0.9f), 1073741824), MeasureSpec.makeMeasureSpec(i2, 0));
                    ChannelIntroActivity.this.createChannelText.measure(MeasureSpec.makeMeasureSpec(i, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(24.0f), 1073741824));
                }
                setMeasuredDimension(i, i2);
            }

            /* Access modifiers changed, original: protected */
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                int i5 = i3 - i;
                i = i4 - i2;
                float f;
                if (i3 > i4) {
                    f = (float) i;
                    i2 = (int) (0.05f * f);
                    ChannelIntroActivity.this.imageView.layout(0, i2, ChannelIntroActivity.this.imageView.getMeasuredWidth(), ChannelIntroActivity.this.imageView.getMeasuredHeight() + i2);
                    float f2 = (float) i5;
                    i2 = (int) (0.4f * f2);
                    i3 = (int) (0.14f * f);
                    ChannelIntroActivity.this.whatIsChannelText.layout(i2, i3, ChannelIntroActivity.this.whatIsChannelText.getMeasuredWidth() + i2, ChannelIntroActivity.this.whatIsChannelText.getMeasuredHeight() + i3);
                    i3 = (int) (0.61f * f);
                    ChannelIntroActivity.this.createChannelText.layout(i2, i3, ChannelIntroActivity.this.createChannelText.getMeasuredWidth() + i2, ChannelIntroActivity.this.createChannelText.getMeasuredHeight() + i3);
                    i5 = (int) (f2 * 0.45f);
                    i = (int) (f * 0.31f);
                    ChannelIntroActivity.this.descriptionText.layout(i5, i, ChannelIntroActivity.this.descriptionText.getMeasuredWidth() + i5, ChannelIntroActivity.this.descriptionText.getMeasuredHeight() + i);
                    return;
                }
                f = (float) i;
                i3 = (int) (f * 0.05f);
                ChannelIntroActivity.this.imageView.layout(0, i3, ChannelIntroActivity.this.imageView.getMeasuredWidth(), ChannelIntroActivity.this.imageView.getMeasuredHeight() + i3);
                i3 = (int) (0.59f * f);
                ChannelIntroActivity.this.whatIsChannelText.layout(0, i3, ChannelIntroActivity.this.whatIsChannelText.getMeasuredWidth(), ChannelIntroActivity.this.whatIsChannelText.getMeasuredHeight() + i3);
                i3 = (int) (0.68f * f);
                i5 = (int) (((float) i5) * 0.05f);
                ChannelIntroActivity.this.descriptionText.layout(i5, i3, ChannelIntroActivity.this.descriptionText.getMeasuredWidth() + i5, ChannelIntroActivity.this.descriptionText.getMeasuredHeight() + i3);
                i5 = (int) (f * 0.86f);
                ChannelIntroActivity.this.createChannelText.layout(0, i5, ChannelIntroActivity.this.createChannelText.getMeasuredWidth(), ChannelIntroActivity.this.createChannelText.getMeasuredHeight() + i5);
            }
        };
        this.fragmentView.setBackgroundColor(Theme.getColor(str));
        ViewGroup viewGroup = (ViewGroup) this.fragmentView;
        viewGroup.setOnTouchListener(C1294-$$Lambda$ChannelIntroActivity$0c8d8mysDNu6O6Ps8WT2KcKJBXc.INSTANCE);
        this.imageView = new ImageView(context);
        this.imageView.setImageResource(C1067R.C1065drawable.channelintro);
        this.imageView.setScaleType(ScaleType.FIT_CENTER);
        viewGroup.addView(this.imageView);
        this.whatIsChannelText = new TextView(context);
        this.whatIsChannelText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.whatIsChannelText.setGravity(1);
        this.whatIsChannelText.setTextSize(1, 24.0f);
        this.whatIsChannelText.setText(LocaleController.getString("ChannelAlertTitle", C1067R.string.ChannelAlertTitle));
        viewGroup.addView(this.whatIsChannelText);
        this.descriptionText = new TextView(context);
        this.descriptionText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
        this.descriptionText.setGravity(1);
        this.descriptionText.setTextSize(1, 16.0f);
        this.descriptionText.setText(LocaleController.getString("ChannelAlertText", C1067R.string.ChannelAlertText));
        viewGroup.addView(this.descriptionText);
        this.createChannelText = new TextView(context);
        this.createChannelText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText5));
        this.createChannelText.setGravity(17);
        this.createChannelText.setTextSize(1, 16.0f);
        this.createChannelText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.createChannelText.setText(LocaleController.getString("ChannelAlertCreate", C1067R.string.ChannelAlertCreate));
        viewGroup.addView(this.createChannelText);
        this.createChannelText.setOnClickListener(new C1295-$$Lambda$ChannelIntroActivity$M58NjDpXQDsy4vkbKcDWC5YHj9o(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$1$ChannelIntroActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("step", 0);
        presentFragment(new ChannelCreateActivity(bundle), true);
    }

    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[]{new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText2), new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarWhiteSelector), new ThemeDescription(this.whatIsChannelText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText), new ThemeDescription(this.descriptionText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText6), new ThemeDescription(this.createChannelText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText5)};
    }
}
