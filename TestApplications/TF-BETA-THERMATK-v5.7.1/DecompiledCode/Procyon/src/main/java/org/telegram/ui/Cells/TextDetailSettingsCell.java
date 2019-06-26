// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.Emoji;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class TextDetailSettingsCell extends FrameLayout
{
    private ImageView imageView;
    private boolean multiline;
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;
    
    public TextDetailSettingsCell(final Context context) {
        super(context);
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView = this.textView;
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
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n3 | 0x30, 21.0f, 10.0f, 21.0f, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        final TextView valueTextView = this.valueTextView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        valueTextView.setGravity(gravity);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setPadding(0, 0, 0, 0);
        final TextView valueTextView2 = this.valueTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n4 | 0x30, 21.0f, 35.0f, 21.0f, 0.0f));
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
        this.imageView.setVisibility(8);
        final ImageView imageView = this.imageView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = n;
        }
        else {
            n5 = 3;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(52, 52.0f, n5 | 0x30, 8.0f, 6.0f, 8.0f, 0.0f));
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    public TextView getValueTextView() {
        return this.valueTextView;
    }
    
    public void invalidate() {
        super.invalidate();
        this.textView.invalidate();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider && Theme.dividerPaint != null) {
            final boolean isRTL = LocaleController.isRTL;
            float n = 71.0f;
            float n2;
            if (isRTL) {
                n2 = 0.0f;
            }
            else {
                float n3;
                if (this.imageView.getVisibility() == 0) {
                    n3 = 71.0f;
                }
                else {
                    n3 = 20.0f;
                }
                n2 = (float)AndroidUtilities.dp(n3);
            }
            final float n4 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                if (this.imageView.getVisibility() != 0) {
                    n = 20.0f;
                }
                dp = AndroidUtilities.dp(n);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n2, n4, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (!this.multiline) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
        }
        else {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        }
    }
    
    public void setMultilineDetail(final boolean multiline) {
        this.multiline = multiline;
        if (multiline) {
            this.valueTextView.setLines(0);
            this.valueTextView.setMaxLines(0);
            this.valueTextView.setSingleLine(false);
            this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(12.0f));
        }
        else {
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setPadding(0, 0, 0, 0);
        }
    }
    
    public void setTextAndValue(final String text, final CharSequence text2, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setText(text2);
        this.needDivider = needDivider;
        this.imageView.setVisibility(8);
        this.setWillNotDraw(needDivider ^ true);
    }
    
    public void setTextAndValueAndIcon(final String text, final CharSequence text2, int imageResource, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setText(text2);
        this.imageView.setImageResource(imageResource);
        this.imageView.setVisibility(0);
        final TextView textView = this.textView;
        if (LocaleController.isRTL) {
            imageResource = 0;
        }
        else {
            imageResource = AndroidUtilities.dp(50.0f);
        }
        int dp;
        if (LocaleController.isRTL) {
            dp = AndroidUtilities.dp(50.0f);
        }
        else {
            dp = 0;
        }
        textView.setPadding(imageResource, 0, dp, 0);
        final TextView valueTextView = this.valueTextView;
        if (LocaleController.isRTL) {
            imageResource = 0;
        }
        else {
            imageResource = AndroidUtilities.dp(50.0f);
        }
        int dp2;
        if (LocaleController.isRTL) {
            dp2 = AndroidUtilities.dp(50.0f);
        }
        else {
            dp2 = 0;
        }
        int dp3;
        if (this.multiline) {
            dp3 = AndroidUtilities.dp(12.0f);
        }
        else {
            dp3 = 0;
        }
        valueTextView.setPadding(imageResource, 0, dp2, dp3);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextWithEmojiAnd21Value(final String s, final CharSequence text, final boolean needDivider) {
        final TextView textView = this.textView;
        textView.setText(Emoji.replaceEmoji(s, textView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
        this.valueTextView.setText(text);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setValue(final CharSequence text) {
        this.valueTextView.setText(text);
    }
}
