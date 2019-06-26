package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.CheckBoxSquare;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.CheckBoxCell */
public class CheckBoxCell extends FrameLayout {
    private CheckBoxSquare checkBox;
    private boolean isMultiline;
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;

    public CheckBoxCell(Context context, int i) {
        this(context, i, 17);
    }

    public CheckBoxCell(Context context, int i, int i2) {
        Context context2 = context;
        int i3 = i;
        int i4 = i2;
        super(context);
        this.textView = new TextView(context2);
        boolean z = true;
        this.textView.setTextColor(Theme.getColor(i3 == 1 ? Theme.key_dialogTextBlack : Theme.key_windowBackgroundWhiteBlackText));
        this.textView.setLinkTextColor(Theme.getColor(i3 == 1 ? Theme.key_dialogTextLink : Theme.key_windowBackgroundWhiteLinkText));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TruncateAt.END);
        int i5 = 5;
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        if (i3 == 2) {
            TextView textView = this.textView;
            int i6 = (LocaleController.isRTL ? 5 : 3) | 48;
            int i7 = 29;
            float f = (float) (LocaleController.isRTL ? 0 : 29);
            if (!LocaleController.isRTL) {
                i7 = 0;
            }
            addView(textView, LayoutHelper.createFrame(-1, -1.0f, i6, f, 0.0f, (float) i7, 0.0f));
        } else {
            addView(this.textView, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 5 : 3) | 48, (float) (LocaleController.isRTL ? i4 : (i4 - 17) + 46), 0.0f, (float) (LocaleController.isRTL ? (i4 - 17) + 46 : i4), 0.0f));
        }
        this.valueTextView = new TextView(context2);
        this.valueTextView.setTextColor(Theme.getColor(i3 == 1 ? Theme.key_dialogTextBlue : Theme.key_windowBackgroundWhiteValueText));
        this.valueTextView.setTextSize(1, 16.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setEllipsize(TruncateAt.END);
        this.valueTextView.setGravity((LocaleController.isRTL ? 3 : 5) | 16);
        float f2 = (float) i4;
        addView(this.valueTextView, LayoutHelper.createFrame(-2, -1.0f, (LocaleController.isRTL ? 3 : 5) | 48, f2, 0.0f, f2, 0.0f));
        if (i3 != 1) {
            z = false;
        }
        this.checkBox = new CheckBoxSquare(context2, z);
        CheckBoxSquare checkBoxSquare;
        if (i3 == 2) {
            checkBoxSquare = this.checkBox;
            if (!LocaleController.isRTL) {
                i5 = 3;
            }
            addView(checkBoxSquare, LayoutHelper.createFrame(18, 18.0f, i5 | 48, 0.0f, 15.0f, 0.0f, 0.0f));
            return;
        }
        checkBoxSquare = this.checkBox;
        if (!LocaleController.isRTL) {
            i5 = 3;
        }
        int i8 = i5 | 48;
        float f3 = (float) (LocaleController.isRTL ? 0 : i4);
        if (!LocaleController.isRTL) {
            i4 = 0;
        }
        addView(checkBoxSquare, LayoutHelper.createFrame(18, 18.0f, i8, f3, 16.0f, (float) i4, 0.0f));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        if (this.isMultiline) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
            return;
        }
        setMeasuredDimension(MeasureSpec.getSize(i), AndroidUtilities.m26dp(50.0f) + this.needDivider);
        i = ((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight()) - AndroidUtilities.m26dp(34.0f);
        this.valueTextView.measure(MeasureSpec.makeMeasureSpec(i / 2, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
        this.textView.measure(MeasureSpec.makeMeasureSpec((i - this.valueTextView.getMeasuredWidth()) - AndroidUtilities.m26dp(8.0f), 1073741824), MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
        this.checkBox.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(18.0f), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(18.0f), 1073741824));
    }

    public void setTextColor(int i) {
        this.textView.setTextColor(i);
    }

    public void setText(CharSequence charSequence, String str, boolean z, boolean z2) {
        this.textView.setText(charSequence);
        this.checkBox.setChecked(z, false);
        this.valueTextView.setText(str);
        this.needDivider = z2;
        setWillNotDraw(z2 ^ 1);
    }

    public void setMultiline(boolean z) {
        this.isMultiline = z;
        LayoutParams layoutParams = (LayoutParams) this.textView.getLayoutParams();
        LayoutParams layoutParams2 = (LayoutParams) this.checkBox.getLayoutParams();
        if (this.isMultiline) {
            this.textView.setLines(0);
            this.textView.setMaxLines(0);
            this.textView.setSingleLine(false);
            this.textView.setEllipsize(null);
            this.textView.setPadding(0, 0, 0, AndroidUtilities.m26dp(5.0f));
            layoutParams.height = -2;
            layoutParams.topMargin = AndroidUtilities.m26dp(10.0f);
            layoutParams2.topMargin = AndroidUtilities.m26dp(12.0f);
        } else {
            this.textView.setLines(1);
            this.textView.setMaxLines(1);
            this.textView.setSingleLine(true);
            this.textView.setEllipsize(TruncateAt.END);
            this.textView.setPadding(0, 0, 0, 0);
            layoutParams.height = -1;
            layoutParams.topMargin = 0;
            layoutParams2.topMargin = AndroidUtilities.m26dp(15.0f);
        }
        this.textView.setLayoutParams(layoutParams);
        this.checkBox.setLayoutParams(layoutParams2);
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        float f = 1.0f;
        this.textView.setAlpha(z ? 1.0f : 0.5f);
        this.valueTextView.setAlpha(z ? 1.0f : 0.5f);
        CheckBoxSquare checkBoxSquare = this.checkBox;
        if (!z) {
            f = 0.5f;
        }
        checkBoxSquare.setAlpha(f);
    }

    public void setChecked(boolean z, boolean z2) {
        this.checkBox.setChecked(z, z2);
    }

    public boolean isChecked() {
        return this.checkBox.isChecked();
    }

    public TextView getTextView() {
        return this.textView;
    }

    public TextView getValueTextView() {
        return this.valueTextView;
    }

    public CheckBoxSquare getCheckBox() {
        return this.checkBox;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine(LocaleController.isRTL ? 0.0f : (float) AndroidUtilities.m26dp(20.0f), (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.m26dp(20.0f) : 0)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.CheckBox");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(isChecked());
    }
}