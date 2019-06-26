package org.telegram.p004ui.Cells;

import android.content.Context;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RadioButton;

/* renamed from: org.telegram.ui.Cells.RadioColorCell */
public class RadioColorCell extends FrameLayout {
    private RadioButton radioButton;
    private TextView textView;

    public RadioColorCell(Context context) {
        super(context);
        this.radioButton = new RadioButton(context);
        this.radioButton.setSize(AndroidUtilities.m26dp(20.0f));
        this.radioButton.setColor(Theme.getColor(Theme.key_dialogRadioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
        RadioButton radioButton = this.radioButton;
        int i = 5;
        int i2 = (LocaleController.isRTL ? 5 : 3) | 48;
        int i3 = 0;
        float f = (float) (LocaleController.isRTL ? 0 : 18);
        if (LocaleController.isRTL) {
            i3 = 18;
        }
        addView(radioButton, LayoutHelper.createFrame(22, 22.0f, i2, f, 14.0f, (float) i3, 0.0f));
        this.textView = new TextView(context);
        this.textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        TextView textView = this.textView;
        if (!LocaleController.isRTL) {
            i = 3;
        }
        i2 = i | 48;
        int i4 = 21;
        f = (float) (LocaleController.isRTL ? 21 : 51);
        if (LocaleController.isRTL) {
            i4 = 51;
        }
        addView(textView, LayoutHelper.createFrame(-2, -2.0f, i2, f, 13.0f, (float) i4, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(50.0f), 1073741824));
    }

    public void setCheckColor(int i, int i2) {
        this.radioButton.setColor(i, i2);
    }

    public void setTextAndValue(String str, boolean z) {
        this.textView.setText(str);
        this.radioButton.setChecked(z, false);
    }

    public void setChecked(boolean z, boolean z2) {
        this.radioButton.setChecked(z, z2);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.RadioButton");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.radioButton.isChecked());
    }
}
