package org.telegram.p004ui.Components;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.PickerBottomLayoutViewer */
public class PickerBottomLayoutViewer extends FrameLayout {
    public TextView cancelButton;
    public TextView doneButton;
    public TextView doneButtonBadgeTextView;
    private boolean isDarkTheme;

    public PickerBottomLayoutViewer(Context context) {
        this(context, true);
    }

    public PickerBottomLayoutViewer(Context context, boolean z) {
        super(context);
        this.isDarkTheme = z;
        setBackgroundColor(this.isDarkTheme ? -15066598 : -1);
        this.cancelButton = new TextView(context);
        this.cancelButton.setTextSize(1, 14.0f);
        int i = -15095832;
        this.cancelButton.setTextColor(this.isDarkTheme ? -1 : -15095832);
        this.cancelButton.setGravity(17);
        TextView textView = this.cancelButton;
        boolean z2 = this.isDarkTheme;
        int i2 = Theme.ACTION_BAR_PICKER_SELECTOR_COLOR;
        textView.setBackgroundDrawable(Theme.createSelectorDrawable(z2 ? Theme.ACTION_BAR_PICKER_SELECTOR_COLOR : Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR, 0));
        this.cancelButton.setPadding(AndroidUtilities.m26dp(20.0f), 0, AndroidUtilities.m26dp(20.0f), 0);
        this.cancelButton.setText(LocaleController.getString("Cancel", C1067R.string.Cancel).toUpperCase());
        String str = "fonts/rmedium.ttf";
        this.cancelButton.setTypeface(AndroidUtilities.getTypeface(str));
        addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
        this.doneButton = new TextView(context);
        this.doneButton.setTextSize(1, 14.0f);
        textView = this.doneButton;
        if (this.isDarkTheme) {
            i = -1;
        }
        textView.setTextColor(i);
        this.doneButton.setGravity(17);
        textView = this.doneButton;
        if (!this.isDarkTheme) {
            i2 = Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR;
        }
        textView.setBackgroundDrawable(Theme.createSelectorDrawable(i2, 0));
        this.doneButton.setPadding(AndroidUtilities.m26dp(20.0f), 0, AndroidUtilities.m26dp(20.0f), 0);
        this.doneButton.setText(LocaleController.getString("Send", C1067R.string.Send).toUpperCase());
        this.doneButton.setTypeface(AndroidUtilities.getTypeface(str));
        addView(this.doneButton, LayoutHelper.createFrame(-2, -1, 53));
        this.doneButtonBadgeTextView = new TextView(context);
        this.doneButtonBadgeTextView.setTypeface(AndroidUtilities.getTypeface(str));
        this.doneButtonBadgeTextView.setTextSize(1, 13.0f);
        this.doneButtonBadgeTextView.setTextColor(-1);
        this.doneButtonBadgeTextView.setGravity(17);
        this.doneButtonBadgeTextView.setBackgroundResource(this.isDarkTheme ? C1067R.C1065drawable.photobadge : C1067R.C1065drawable.bluecounter);
        this.doneButtonBadgeTextView.setMinWidth(AndroidUtilities.m26dp(23.0f));
        this.doneButtonBadgeTextView.setPadding(AndroidUtilities.m26dp(8.0f), 0, AndroidUtilities.m26dp(8.0f), AndroidUtilities.m26dp(1.0f));
        addView(this.doneButtonBadgeTextView, LayoutHelper.createFrame(-2, 23.0f, 53, 0.0f, 0.0f, 7.0f, 0.0f));
    }

    public void updateSelectedCount(int i, boolean z) {
        int i2 = -1;
        TextView textView;
        if (i == 0) {
            this.doneButtonBadgeTextView.setVisibility(8);
            if (z) {
                this.doneButton.setTextColor(-6710887);
                this.doneButton.setEnabled(false);
                return;
            }
            textView = this.doneButton;
            if (!this.isDarkTheme) {
                i2 = -15095832;
            }
            textView.setTextColor(i2);
            return;
        }
        this.doneButtonBadgeTextView.setVisibility(0);
        this.doneButtonBadgeTextView.setText(String.format("%d", new Object[]{Integer.valueOf(i)}));
        textView = this.doneButton;
        if (!this.isDarkTheme) {
            i2 = -15095832;
        }
        textView.setTextColor(i2);
        if (z) {
            this.doneButton.setEnabled(true);
        }
    }
}
