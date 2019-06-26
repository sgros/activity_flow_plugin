// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout;

public class TextSettingsCell extends FrameLayout
{
    private boolean canDisable;
    private boolean needDivider;
    private TextView textView;
    private ImageView valueImageView;
    private TextView valueTextView;
    
    public TextSettingsCell(final Context context) {
        this(context, 21);
    }
    
    public TextSettingsCell(final Context context, int n) {
        super(context);
        (this.textView = new TextView(context)).setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n2 = 5;
        int n3;
        if (isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        textView.setGravity(n3 | 0x10);
        this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        final TextView textView2 = this.textView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        final float n5 = (float)n;
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n4 | 0x30, n5, 0.0f, n5, 0.0f));
        (this.valueTextView = new TextView(context)).setTextSize(1, 16.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView valueTextView = this.valueTextView;
        if (LocaleController.isRTL) {
            n = 3;
        }
        else {
            n = 5;
        }
        valueTextView.setGravity(n | 0x10);
        this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteValueText"));
        final TextView valueTextView2 = this.valueTextView;
        if (LocaleController.isRTL) {
            n = 3;
        }
        else {
            n = 5;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, n | 0x30, n5, 0.0f, n5, 0.0f));
        (this.valueImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.valueImageView.setVisibility(4);
        this.valueImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
        final ImageView valueImageView = this.valueImageView;
        n = n2;
        if (LocaleController.isRTL) {
            n = 3;
        }
        this.addView((View)valueImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n | 0x10, n5, 0.0f, n5, 0.0f));
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    public TextView getValueTextView() {
        return this.valueTextView;
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
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setEnabled(this.isEnabled());
    }
    
    protected void onMeasure(int n, int n2) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0));
        n2 = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight() - AndroidUtilities.dp(34.0f);
        final int n3 = n2 / 2;
        if (this.valueImageView.getVisibility() == 0) {
            this.valueImageView.measure(View$MeasureSpec.makeMeasureSpec(n3, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
        }
        n = n2;
        if (this.valueTextView.getVisibility() == 0) {
            this.valueTextView.measure(View$MeasureSpec.makeMeasureSpec(n3, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
            n = n2 - this.valueTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0f);
        }
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
    }
    
    public void setCanDisable(final boolean canDisable) {
        this.canDisable = canDisable;
    }
    
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        final TextView textView = this.textView;
        final float n = 0.5f;
        float alpha;
        if (!enabled && this.canDisable) {
            alpha = 0.5f;
        }
        else {
            alpha = 1.0f;
        }
        textView.setAlpha(alpha);
        if (this.valueTextView.getVisibility() == 0) {
            final TextView valueTextView = this.valueTextView;
            float alpha2;
            if (!enabled && this.canDisable) {
                alpha2 = 0.5f;
            }
            else {
                alpha2 = 1.0f;
            }
            valueTextView.setAlpha(alpha2);
        }
        if (this.valueImageView.getVisibility() == 0) {
            final ImageView valueImageView = this.valueImageView;
            float alpha3 = 0.0f;
            Label_0120: {
                if (!enabled) {
                    alpha3 = n;
                    if (this.canDisable) {
                        break Label_0120;
                    }
                }
                alpha3 = 1.0f;
            }
            valueImageView.setAlpha(alpha3);
        }
    }
    
    public void setEnabled(final boolean enabled, final ArrayList<Animator> list) {
        this.setEnabled(enabled);
        float alpha = 1.0f;
        if (list != null) {
            final TextView textView = this.textView;
            float n;
            if (enabled) {
                n = 1.0f;
            }
            else {
                n = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)textView, "alpha", new float[] { n }));
            if (this.valueTextView.getVisibility() == 0) {
                final TextView valueTextView = this.valueTextView;
                float n2;
                if (enabled) {
                    n2 = 1.0f;
                }
                else {
                    n2 = 0.5f;
                }
                list.add((Animator)ObjectAnimator.ofFloat((Object)valueTextView, "alpha", new float[] { n2 }));
            }
            if (this.valueImageView.getVisibility() == 0) {
                final ImageView valueImageView = this.valueImageView;
                if (!enabled) {
                    alpha = 0.5f;
                }
                list.add((Animator)ObjectAnimator.ofFloat((Object)valueImageView, "alpha", new float[] { alpha }));
            }
        }
        else {
            final TextView textView2 = this.textView;
            float alpha2;
            if (enabled) {
                alpha2 = 1.0f;
            }
            else {
                alpha2 = 0.5f;
            }
            textView2.setAlpha(alpha2);
            if (this.valueTextView.getVisibility() == 0) {
                final TextView valueTextView2 = this.valueTextView;
                float alpha3;
                if (enabled) {
                    alpha3 = 1.0f;
                }
                else {
                    alpha3 = 0.5f;
                }
                valueTextView2.setAlpha(alpha3);
            }
            if (this.valueImageView.getVisibility() == 0) {
                final ImageView valueImageView2 = this.valueImageView;
                if (!enabled) {
                    alpha = 0.5f;
                }
                valueImageView2.setAlpha(alpha);
            }
        }
    }
    
    public void setText(final String text, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setVisibility(4);
        this.valueImageView.setVisibility(4);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextAndIcon(final String text, final int imageResource, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setVisibility(4);
        if (imageResource != 0) {
            this.valueImageView.setVisibility(0);
            this.valueImageView.setImageResource(imageResource);
        }
        else {
            this.valueImageView.setVisibility(4);
        }
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
    
    public void setTextAndValue(final String text, final String text2, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueImageView.setVisibility(4);
        if (text2 != null) {
            this.valueTextView.setText((CharSequence)text2);
            this.valueTextView.setVisibility(0);
        }
        else {
            this.valueTextView.setVisibility(4);
        }
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
        this.requestLayout();
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
    
    public void setTextValueColor(final int textColor) {
        this.valueTextView.setTextColor(textColor);
    }
}
