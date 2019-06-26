// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.View$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.widget.ImageView$ScaleType;
import android.view.View$OnTouchListener;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChannelIntroActivity extends BaseFragment
{
    private TextView createChannelText;
    private TextView descriptionText;
    private ImageView imageView;
    private TextView whatIsChannelText;
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteGrayText2"), false);
        super.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarWhiteSelector"), false);
        super.actionBar.setCastShadows(false);
        if (!AndroidUtilities.isTablet()) {
            super.actionBar.showActionModeTop();
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChannelIntroActivity.this.finishFragment();
                }
            }
        });
        (super.fragmentView = (View)new ViewGroup(context) {
            protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
                n = n3 - n;
                n2 = n4 - n2;
                if (n3 > n4) {
                    final float n5 = (float)n2;
                    n2 = (int)(0.05f * n5);
                    ChannelIntroActivity.this.imageView.layout(0, n2, ChannelIntroActivity.this.imageView.getMeasuredWidth(), ChannelIntroActivity.this.imageView.getMeasuredHeight() + n2);
                    final float n6 = (float)n;
                    n = (int)(0.4f * n6);
                    n2 = (int)(0.14f * n5);
                    ChannelIntroActivity.this.whatIsChannelText.layout(n, n2, ChannelIntroActivity.this.whatIsChannelText.getMeasuredWidth() + n, ChannelIntroActivity.this.whatIsChannelText.getMeasuredHeight() + n2);
                    n2 = (int)(0.61f * n5);
                    ChannelIntroActivity.this.createChannelText.layout(n, n2, ChannelIntroActivity.this.createChannelText.getMeasuredWidth() + n, ChannelIntroActivity.this.createChannelText.getMeasuredHeight() + n2);
                    n2 = (int)(n6 * 0.45f);
                    n = (int)(n5 * 0.31f);
                    ChannelIntroActivity.this.descriptionText.layout(n2, n, ChannelIntroActivity.this.descriptionText.getMeasuredWidth() + n2, ChannelIntroActivity.this.descriptionText.getMeasuredHeight() + n);
                }
                else {
                    final float n7 = (float)n2;
                    n2 = (int)(n7 * 0.05f);
                    ChannelIntroActivity.this.imageView.layout(0, n2, ChannelIntroActivity.this.imageView.getMeasuredWidth(), ChannelIntroActivity.this.imageView.getMeasuredHeight() + n2);
                    n2 = (int)(0.59f * n7);
                    ChannelIntroActivity.this.whatIsChannelText.layout(0, n2, ChannelIntroActivity.this.whatIsChannelText.getMeasuredWidth(), ChannelIntroActivity.this.whatIsChannelText.getMeasuredHeight() + n2);
                    n2 = (int)(0.68f * n7);
                    n *= (int)0.05f;
                    ChannelIntroActivity.this.descriptionText.layout(n, n2, ChannelIntroActivity.this.descriptionText.getMeasuredWidth() + n, ChannelIntroActivity.this.descriptionText.getMeasuredHeight() + n2);
                    n = (int)(n7 * 0.86f);
                    ChannelIntroActivity.this.createChannelText.layout(0, n, ChannelIntroActivity.this.createChannelText.getMeasuredWidth(), ChannelIntroActivity.this.createChannelText.getMeasuredHeight() + n);
                }
            }
            
            protected void onMeasure(int size, int n) {
                size = View$MeasureSpec.getSize(size);
                final int size2 = View$MeasureSpec.getSize(n);
                if (size > size2) {
                    final ImageView access$000 = ChannelIntroActivity.this.imageView;
                    final float n2 = (float)size;
                    access$000.measure(View$MeasureSpec.makeMeasureSpec((int)(0.45f * n2), 1073741824), View$MeasureSpec.makeMeasureSpec((int)(size2 * 0.78f), 1073741824));
                    final TextView access$2 = ChannelIntroActivity.this.whatIsChannelText;
                    n = (int)(0.6f * n2);
                    access$2.measure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 0));
                    ChannelIntroActivity.this.descriptionText.measure(View$MeasureSpec.makeMeasureSpec((int)(n2 * 0.5f), 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 0));
                    ChannelIntroActivity.this.createChannelText.measure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
                }
                else {
                    ChannelIntroActivity.this.imageView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec((int)(size2 * 0.44f), 1073741824));
                    ChannelIntroActivity.this.whatIsChannelText.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 0));
                    ChannelIntroActivity.this.descriptionText.measure(View$MeasureSpec.makeMeasureSpec((int)(size * 0.9f), 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 0));
                    ChannelIntroActivity.this.createChannelText.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
                }
                this.setMeasuredDimension(size, size2);
            }
        }).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        final ViewGroup viewGroup = (ViewGroup)super.fragmentView;
        viewGroup.setOnTouchListener((View$OnTouchListener)_$$Lambda$ChannelIntroActivity$0c8d8mysDNu6O6Ps8WT2KcKJBXc.INSTANCE);
        (this.imageView = new ImageView(context)).setImageResource(2131165340);
        this.imageView.setScaleType(ImageView$ScaleType.FIT_CENTER);
        viewGroup.addView((View)this.imageView);
        (this.whatIsChannelText = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.whatIsChannelText.setGravity(1);
        this.whatIsChannelText.setTextSize(1, 24.0f);
        this.whatIsChannelText.setText((CharSequence)LocaleController.getString("ChannelAlertTitle", 2131558931));
        viewGroup.addView((View)this.whatIsChannelText);
        (this.descriptionText = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.descriptionText.setGravity(1);
        this.descriptionText.setTextSize(1, 16.0f);
        this.descriptionText.setText((CharSequence)LocaleController.getString("ChannelAlertText", 2131558930));
        viewGroup.addView((View)this.descriptionText);
        (this.createChannelText = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlueText5"));
        this.createChannelText.setGravity(17);
        this.createChannelText.setTextSize(1, 16.0f);
        this.createChannelText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.createChannelText.setText((CharSequence)LocaleController.getString("ChannelAlertCreate", 2131558929));
        viewGroup.addView((View)this.createChannelText);
        this.createChannelText.setOnClickListener((View$OnClickListener)new _$$Lambda$ChannelIntroActivity$M58NjDpXQDsy4vkbKcDWC5YHj9o(this));
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarWhiteSelector"), new ThemeDescription((View)this.whatIsChannelText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.descriptionText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"), new ThemeDescription((View)this.createChannelText, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText5") };
    }
}
