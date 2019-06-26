package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;

/* renamed from: org.telegram.ui.Components.PickerBottomLayout */
public class PickerBottomLayout extends FrameLayout {
    public TextView cancelButton;
    public LinearLayout doneButton;
    public TextView doneButtonBadgeTextView;
    public TextView doneButtonTextView;
    private boolean isDarkTheme;

    public PickerBottomLayout(Context context) {
        this(context, true);
    }

    public PickerBottomLayout(Context context, boolean z) {
        Drawable createRoundRectDrawable;
        Context context2 = context;
        super(context);
        this.isDarkTheme = z;
        setBackgroundColor(this.isDarkTheme ? -15066598 : Theme.getColor(Theme.key_windowBackgroundWhite));
        this.cancelButton = new TextView(context2);
        this.cancelButton.setTextSize(1, 14.0f);
        TextView textView = this.cancelButton;
        boolean z2 = this.isDarkTheme;
        String str = Theme.key_picker_enabledButton;
        int i = -1;
        textView.setTextColor(z2 ? -1 : Theme.getColor(str));
        this.cancelButton.setGravity(17);
        textView = this.cancelButton;
        boolean z3 = this.isDarkTheme;
        int i2 = Theme.ACTION_BAR_PICKER_SELECTOR_COLOR;
        textView.setBackgroundDrawable(Theme.createSelectorDrawable(z3 ? Theme.ACTION_BAR_PICKER_SELECTOR_COLOR : Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR, 0));
        this.cancelButton.setPadding(AndroidUtilities.m26dp(29.0f), 0, AndroidUtilities.m26dp(29.0f), 0);
        this.cancelButton.setText(LocaleController.getString("Cancel", C1067R.string.Cancel).toUpperCase());
        String str2 = "fonts/rmedium.ttf";
        this.cancelButton.setTypeface(AndroidUtilities.getTypeface(str2));
        addView(this.cancelButton, LayoutHelper.createFrame(-2, -1, 51));
        this.doneButton = new LinearLayout(context2);
        this.doneButton.setOrientation(0);
        LinearLayout linearLayout = this.doneButton;
        if (!this.isDarkTheme) {
            i2 = Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR;
        }
        linearLayout.setBackgroundDrawable(Theme.createSelectorDrawable(i2, 0));
        this.doneButton.setPadding(AndroidUtilities.m26dp(29.0f), 0, AndroidUtilities.m26dp(29.0f), 0);
        addView(this.doneButton, LayoutHelper.createFrame(-2, -1, 53));
        this.doneButtonBadgeTextView = new TextView(context2);
        this.doneButtonBadgeTextView.setTypeface(AndroidUtilities.getTypeface(str2));
        this.doneButtonBadgeTextView.setTextSize(1, 13.0f);
        this.doneButtonBadgeTextView.setTextColor(this.isDarkTheme ? -1 : Theme.getColor(Theme.key_picker_badgeText));
        this.doneButtonBadgeTextView.setGravity(17);
        if (this.isDarkTheme) {
            createRoundRectDrawable = Theme.createRoundRectDrawable(AndroidUtilities.m26dp(11.0f), -10043398);
        } else {
            createRoundRectDrawable = Theme.createRoundRectDrawable(AndroidUtilities.m26dp(11.0f), Theme.getColor(Theme.key_picker_badge));
        }
        this.doneButtonBadgeTextView.setBackgroundDrawable(createRoundRectDrawable);
        this.doneButtonBadgeTextView.setMinWidth(AndroidUtilities.m26dp(23.0f));
        this.doneButtonBadgeTextView.setPadding(AndroidUtilities.m26dp(8.0f), 0, AndroidUtilities.m26dp(8.0f), AndroidUtilities.m26dp(1.0f));
        this.doneButton.addView(this.doneButtonBadgeTextView, LayoutHelper.createLinear(-2, 23, 16, 0, 0, 10, 0));
        this.doneButtonTextView = new TextView(context2);
        this.doneButtonTextView.setTextSize(1, 14.0f);
        TextView textView2 = this.doneButtonTextView;
        if (!this.isDarkTheme) {
            i = Theme.getColor(str);
        }
        textView2.setTextColor(i);
        this.doneButtonTextView.setGravity(17);
        this.doneButtonTextView.setCompoundDrawablePadding(AndroidUtilities.m26dp(8.0f));
        this.doneButtonTextView.setText(LocaleController.getString("Send", C1067R.string.Send).toUpperCase());
        this.doneButtonTextView.setTypeface(AndroidUtilities.getTypeface(str2));
        this.doneButton.addView(this.doneButtonTextView, LayoutHelper.createLinear(-2, -2, 16));
    }

    public void updateSelectedCount(int i, boolean z) {
        int i2 = -1;
        Object obj = null;
        String str = Theme.key_picker_enabledButton;
        TextView textView;
        if (i == 0) {
            this.doneButtonBadgeTextView.setVisibility(8);
            if (z) {
                textView = this.doneButtonTextView;
                z = this.isDarkTheme;
                String str2 = Theme.key_picker_disabledButton;
                if (!z) {
                    obj = str2;
                }
                textView.setTag(obj);
                this.doneButtonTextView.setTextColor(this.isDarkTheme ? -6710887 : Theme.getColor(str2));
                this.doneButton.setEnabled(false);
                return;
            }
            textView = this.doneButtonTextView;
            if (!this.isDarkTheme) {
                obj = str;
            }
            textView.setTag(obj);
            textView = this.doneButtonTextView;
            if (!this.isDarkTheme) {
                i2 = Theme.getColor(str);
            }
            textView.setTextColor(i2);
            return;
        }
        this.doneButtonBadgeTextView.setVisibility(0);
        this.doneButtonBadgeTextView.setText(String.format("%d", new Object[]{Integer.valueOf(i)}));
        textView = this.doneButtonTextView;
        if (!this.isDarkTheme) {
            obj = str;
        }
        textView.setTag(obj);
        textView = this.doneButtonTextView;
        if (!this.isDarkTheme) {
            i2 = Theme.getColor(str);
        }
        textView.setTextColor(i2);
        if (z) {
            this.doneButton.setEnabled(true);
        }
    }
}
