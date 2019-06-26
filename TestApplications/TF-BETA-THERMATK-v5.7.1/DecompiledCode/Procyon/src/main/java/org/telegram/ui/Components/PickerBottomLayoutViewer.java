// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.FrameLayout;

public class PickerBottomLayoutViewer extends FrameLayout
{
    public TextView cancelButton;
    public TextView doneButton;
    public TextView doneButtonBadgeTextView;
    private boolean isDarkTheme;
    
    public PickerBottomLayoutViewer(final Context context) {
        this(context, true);
    }
    
    public PickerBottomLayoutViewer(final Context context, final boolean isDarkTheme) {
        super(context);
        this.isDarkTheme = isDarkTheme;
        int backgroundColor;
        if (this.isDarkTheme) {
            backgroundColor = -15066598;
        }
        else {
            backgroundColor = -1;
        }
        this.setBackgroundColor(backgroundColor);
        (this.cancelButton = new TextView(context)).setTextSize(1, 14.0f);
        final TextView cancelButton = this.cancelButton;
        final boolean isDarkTheme2 = this.isDarkTheme;
        final int n = -15095832;
        int textColor;
        if (isDarkTheme2) {
            textColor = -1;
        }
        else {
            textColor = -15095832;
        }
        cancelButton.setTextColor(textColor);
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
        this.cancelButton.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.cancelButton.setText((CharSequence)LocaleController.getString("Cancel", 2131558891).toUpperCase());
        this.cancelButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addView((View)this.cancelButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
        (this.doneButton = new TextView(context)).setTextSize(1, 14.0f);
        final TextView doneButton = this.doneButton;
        int textColor2 = n;
        if (this.isDarkTheme) {
            textColor2 = -1;
        }
        doneButton.setTextColor(textColor2);
        this.doneButton.setGravity(17);
        final TextView doneButton2 = this.doneButton;
        int n4;
        if (this.isDarkTheme) {
            n4 = n2;
        }
        else {
            n4 = 788529152;
        }
        doneButton2.setBackgroundDrawable(Theme.createSelectorDrawable(n4, 0));
        this.doneButton.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        this.doneButton.setText((CharSequence)LocaleController.getString("Send", 2131560685).toUpperCase());
        this.doneButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addView((View)this.doneButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
        (this.doneButtonBadgeTextView = new TextView(context)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.doneButtonBadgeTextView.setTextSize(1, 13.0f);
        this.doneButtonBadgeTextView.setTextColor(-1);
        this.doneButtonBadgeTextView.setGravity(17);
        final TextView doneButtonBadgeTextView = this.doneButtonBadgeTextView;
        int backgroundResource;
        if (this.isDarkTheme) {
            backgroundResource = 2131165754;
        }
        else {
            backgroundResource = 2131165306;
        }
        doneButtonBadgeTextView.setBackgroundResource(backgroundResource);
        this.doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23.0f));
        this.doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(1.0f));
        this.addView((View)this.doneButtonBadgeTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 23.0f, 53, 0.0f, 0.0f, 7.0f, 0.0f));
    }
    
    public void updateSelectedCount(final int i, final boolean b) {
        int n = -1;
        if (i == 0) {
            this.doneButtonBadgeTextView.setVisibility(8);
            if (b) {
                this.doneButton.setTextColor(-6710887);
                this.doneButton.setEnabled(false);
            }
            else {
                final TextView doneButton = this.doneButton;
                if (!this.isDarkTheme) {
                    n = -15095832;
                }
                doneButton.setTextColor(n);
            }
        }
        else {
            this.doneButtonBadgeTextView.setVisibility(0);
            this.doneButtonBadgeTextView.setText((CharSequence)String.format("%d", i));
            final TextView doneButton2 = this.doneButton;
            if (!this.isDarkTheme) {
                n = -15095832;
            }
            doneButton2.setTextColor(n);
            if (b) {
                this.doneButton.setEnabled(true);
            }
        }
    }
}
