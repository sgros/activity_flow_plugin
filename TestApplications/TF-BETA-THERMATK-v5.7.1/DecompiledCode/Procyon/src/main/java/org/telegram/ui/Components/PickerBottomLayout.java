// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout;

public class PickerBottomLayout extends FrameLayout
{
    public TextView cancelButton;
    public LinearLayout doneButton;
    public TextView doneButtonBadgeTextView;
    public TextView doneButtonTextView;
    private boolean isDarkTheme;
    
    public PickerBottomLayout(final Context context) {
        this(context, true);
    }
    
    public PickerBottomLayout(final Context context, final boolean isDarkTheme) {
        super(context);
        this.isDarkTheme = isDarkTheme;
        int color;
        if (this.isDarkTheme) {
            color = -15066598;
        }
        else {
            color = Theme.getColor("windowBackgroundWhite");
        }
        this.setBackgroundColor(color);
        (this.cancelButton = new TextView(context)).setTextSize(1, 14.0f);
        final TextView cancelButton = this.cancelButton;
        final boolean isDarkTheme2 = this.isDarkTheme;
        final int n = -1;
        int color2;
        if (isDarkTheme2) {
            color2 = -1;
        }
        else {
            color2 = Theme.getColor("picker_enabledButton");
        }
        cancelButton.setTextColor(color2);
        this.cancelButton.setGravity(17);
        final TextView cancelButton2 = this.cancelButton;
        final boolean isDarkTheme3 = this.isDarkTheme;
        final int n2 = -12763843;
        int n3;
        if (isDarkTheme3) {
            n3 = -12763843;
        }
        else {
            n3 = 788529152;
        }
        cancelButton2.setBackgroundDrawable(Theme.createSelectorDrawable(n3, 0));
        this.cancelButton.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
        this.cancelButton.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        this.cancelButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addView((View)this.cancelButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
        (this.doneButton = new LinearLayout(context)).setOrientation(0);
        final LinearLayout doneButton = this.doneButton;
        int n4;
        if (this.isDarkTheme) {
            n4 = n2;
        }
        else {
            n4 = 788529152;
        }
        doneButton.setBackgroundDrawable(Theme.createSelectorDrawable(n4, 0));
        this.doneButton.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
        this.addView((View)this.doneButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
        (this.doneButtonBadgeTextView = new TextView(context)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.doneButtonBadgeTextView.setTextSize(1, 13.0f);
        final TextView doneButtonBadgeTextView = this.doneButtonBadgeTextView;
        int color3;
        if (this.isDarkTheme) {
            color3 = -1;
        }
        else {
            color3 = Theme.getColor("picker_badgeText");
        }
        doneButtonBadgeTextView.setTextColor(color3);
        this.doneButtonBadgeTextView.setGravity(17);
        Drawable backgroundDrawable;
        if (this.isDarkTheme) {
            backgroundDrawable = Theme.createRoundRectDrawable(AndroidUtilities.dp(11.0f), -10043398);
        }
        else {
            backgroundDrawable = Theme.createRoundRectDrawable(AndroidUtilities.dp(11.0f), Theme.getColor("picker_badge"));
        }
        this.doneButtonBadgeTextView.setBackgroundDrawable(backgroundDrawable);
        this.doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23.0f));
        this.doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(1.0f));
        this.doneButton.addView((View)this.doneButtonBadgeTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 23, 16, 0, 0, 10, 0));
        (this.doneButtonTextView = new TextView(context)).setTextSize(1, 14.0f);
        final TextView doneButtonTextView = this.doneButtonTextView;
        int color4;
        if (this.isDarkTheme) {
            color4 = n;
        }
        else {
            color4 = Theme.getColor("picker_enabledButton");
        }
        doneButtonTextView.setTextColor(color4);
        this.doneButtonTextView.setGravity(17);
        this.doneButtonTextView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        this.doneButtonTextView.setText((CharSequence)LocaleController.getString("Send", 2131560685).toUpperCase());
        this.doneButtonTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.doneButton.addView((View)this.doneButtonTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 16));
    }
    
    public void updateSelectedCount(int color, final boolean b) {
        int n = -1;
        final Object o = null;
        final Object o2 = null;
        Object tag = null;
        if (color == 0) {
            this.doneButtonBadgeTextView.setVisibility(8);
            if (b) {
                final TextView doneButtonTextView = this.doneButtonTextView;
                if (!this.isDarkTheme) {
                    tag = "picker_disabledButton";
                }
                doneButtonTextView.setTag(tag);
                final TextView doneButtonTextView2 = this.doneButtonTextView;
                if (this.isDarkTheme) {
                    color = -6710887;
                }
                else {
                    color = Theme.getColor("picker_disabledButton");
                }
                doneButtonTextView2.setTextColor(color);
                this.doneButton.setEnabled(false);
            }
            else {
                final TextView doneButtonTextView3 = this.doneButtonTextView;
                Object tag2;
                if (this.isDarkTheme) {
                    tag2 = o;
                }
                else {
                    tag2 = "picker_enabledButton";
                }
                doneButtonTextView3.setTag(tag2);
                final TextView doneButtonTextView4 = this.doneButtonTextView;
                if (!this.isDarkTheme) {
                    n = Theme.getColor("picker_enabledButton");
                }
                doneButtonTextView4.setTextColor(n);
            }
        }
        else {
            this.doneButtonBadgeTextView.setVisibility(0);
            this.doneButtonBadgeTextView.setText((CharSequence)String.format("%d", color));
            final TextView doneButtonTextView5 = this.doneButtonTextView;
            Object tag3;
            if (this.isDarkTheme) {
                tag3 = o2;
            }
            else {
                tag3 = "picker_enabledButton";
            }
            doneButtonTextView5.setTag(tag3);
            final TextView doneButtonTextView6 = this.doneButtonTextView;
            if (!this.isDarkTheme) {
                n = Theme.getColor("picker_enabledButton");
            }
            doneButtonTextView6.setTextColor(n);
            if (b) {
                this.doneButton.setEnabled(true);
            }
        }
    }
}
