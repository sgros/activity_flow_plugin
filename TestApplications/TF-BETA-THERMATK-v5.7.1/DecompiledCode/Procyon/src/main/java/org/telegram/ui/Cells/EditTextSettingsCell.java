// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.text.TextWatcher;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.widget.FrameLayout;

public class EditTextSettingsCell extends FrameLayout
{
    private boolean needDivider;
    private EditTextBoldCursor textView;
    
    public EditTextSettingsCell(final Context context) {
        super(context);
        (this.textView = new EditTextBoldCursor(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final EditTextBoldCursor textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView.setGravity(n2 | 0x10);
        this.textView.setBackgroundDrawable((Drawable)null);
        this.textView.setPadding(0, 0, 0, 0);
        final EditTextBoldCursor textView2 = this.textView;
        textView2.setInputType(textView2.getInputType() | 0x4000);
        final EditTextBoldCursor textView3 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = n;
        }
        else {
            n3 = 3;
        }
        this.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, 21.0f, 0.0f, 21.0f, 0.0f));
    }
    
    public void addTextWatcher(final TextWatcher textWatcher) {
        this.textView.addTextChangedListener(textWatcher);
    }
    
    public String getText() {
        return this.textView.getText().toString();
    }
    
    public EditTextBoldCursor getTextView() {
        return this.textView;
    }
    
    public int length() {
        return this.textView.length();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp(20.0f);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp(20.0f);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(int measuredWidth, int dp) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(measuredWidth), AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0));
        measuredWidth = this.getMeasuredWidth();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        dp = AndroidUtilities.dp(42.0f);
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(measuredWidth - paddingLeft - paddingRight - dp, 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
    }
    
    public void setEnabled(final boolean enabled, final ArrayList<Animator> list) {
        this.setEnabled(enabled);
    }
    
    public void setText(final String text, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextAndHint(final String text, final String hint, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.textView.setHint((CharSequence)hint);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}
