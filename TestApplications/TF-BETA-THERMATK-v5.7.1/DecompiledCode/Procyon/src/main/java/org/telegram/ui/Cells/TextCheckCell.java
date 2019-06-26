// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.Typeface;
import android.widget.FrameLayout$LayoutParams;
import java.util.ArrayList;
import android.animation.TimeInterpolator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import org.telegram.ui.Components.AnimationProperties;
import android.widget.TextView;
import org.telegram.ui.Components.Switch;
import android.animation.ObjectAnimator;
import android.graphics.Paint;
import android.util.Property;
import android.widget.FrameLayout;

public class TextCheckCell extends FrameLayout
{
    public static final Property<TextCheckCell, Float> ANIMATION_PROGRESS;
    private int animatedColorBackground;
    private Paint animationPaint;
    private float animationProgress;
    private ObjectAnimator animator;
    private Switch checkBox;
    private boolean drawCheckRipple;
    private int height;
    private boolean isMultiline;
    private float lastTouchX;
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;
    
    static {
        ANIMATION_PROGRESS = new AnimationProperties.FloatProperty<TextCheckCell>("animationProgress") {
            public Float get(final TextCheckCell textCheckCell) {
                return textCheckCell.animationProgress;
            }
            
            public void setValue(final TextCheckCell textCheckCell, final float n) {
                textCheckCell.setAnimationProgress(n);
                textCheckCell.invalidate();
            }
        };
    }
    
    public TextCheckCell(final Context context) {
        this(context, 21);
    }
    
    public TextCheckCell(final Context context, final int n) {
        this(context, n, false);
    }
    
    public TextCheckCell(final Context context, int n, final boolean b) {
        super(context);
        this.height = 50;
        this.textView = new TextView(context);
        final TextView textView = this.textView;
        String s;
        if (b) {
            s = "dialogTextBlack";
        }
        else {
            s = "windowBackgroundWhiteBlackText";
        }
        textView.setTextColor(Theme.getColor(s));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        final TextView textView2 = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n2 = 5;
        int n3;
        if (isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        textView2.setGravity(n3 | 0x10);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView3 = this.textView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 70.0f;
        }
        else {
            n5 = (float)n;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = (float)n;
        }
        else {
            n6 = 70.0f;
        }
        this.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n4 | 0x30, n5, 0.0f, n6, 0.0f));
        this.valueTextView = new TextView(context);
        final TextView valueTextView = this.valueTextView;
        String s2;
        if (b) {
            s2 = "dialogIcon";
        }
        else {
            s2 = "windowBackgroundWhiteGrayText2";
        }
        valueTextView.setTextColor(Theme.getColor(s2));
        this.valueTextView.setTextSize(1, 13.0f);
        final TextView valueTextView2 = this.valueTextView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        valueTextView2.setGravity(gravity);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        this.valueTextView.setPadding(0, 0, 0, 0);
        this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView valueTextView3 = this.valueTextView;
        int n7;
        if (LocaleController.isRTL) {
            n7 = 5;
        }
        else {
            n7 = 3;
        }
        float n8;
        if (LocaleController.isRTL) {
            n8 = 64.0f;
        }
        else {
            n8 = (float)n;
        }
        float n9;
        if (LocaleController.isRTL) {
            n9 = (float)n;
        }
        else {
            n9 = 64.0f;
        }
        this.addView((View)valueTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n7 | 0x30, n8, 36.0f, n9, 0.0f));
        (this.checkBox = new Switch(context)).setColors("switchTrack", "switchTrackChecked", "windowBackgroundWhite", "windowBackgroundWhite");
        final Switch checkBox = this.checkBox;
        n = n2;
        if (LocaleController.isRTL) {
            n = 3;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(37, 20.0f, n | 0x10, 22.0f, 0.0f, 22.0f, 0.0f));
        this.setClipChildren(false);
    }
    
    private void setAnimationProgress(float max) {
        this.animationProgress = max;
        max = Math.max(this.lastTouchX, this.getMeasuredWidth() - this.lastTouchX);
        this.checkBox.setOverrideColorProgress(this.lastTouchX, (float)(this.getMeasuredHeight() / 2), (max + AndroidUtilities.dp(40.0f)) * this.animationProgress);
    }
    
    public boolean isChecked() {
        return this.checkBox.isChecked();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.animatedColorBackground != 0) {
            canvas.drawCircle(this.lastTouchX, (float)(this.getMeasuredHeight() / 2), (Math.max(this.lastTouchX, this.getMeasuredWidth() - this.lastTouchX) + AndroidUtilities.dp(40.0f)) * this.animationProgress, this.animationPaint);
        }
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
        accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Switch");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(this.checkBox.isChecked());
        int n;
        String s;
        if (this.checkBox.isChecked()) {
            n = 2131560080;
            s = "NotificationsOn";
        }
        else {
            n = 2131560078;
            s = "NotificationsOff";
        }
        accessibilityNodeInfo.setContentDescription((CharSequence)LocaleController.getString(s, n));
    }
    
    protected void onMeasure(int measureSpec, final int n) {
        if (this.isMultiline) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824), View$MeasureSpec.makeMeasureSpec(0, 0));
        }
        else {
            measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824);
            float n2;
            if (this.valueTextView.getVisibility() == 0) {
                n2 = 64.0f;
            }
            else {
                n2 = (float)this.height;
            }
            super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(n2) + (this.needDivider ? 1 : 0), 1073741824));
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.lastTouchX = motionEvent.getX();
        return super.onTouchEvent(motionEvent);
    }
    
    public void setBackgroundColor(final int backgroundColor) {
        this.clearAnimation();
        this.animatedColorBackground = 0;
        super.setBackgroundColor(backgroundColor);
    }
    
    public void setBackgroundColorAnimated(final boolean b, final int animatedColorBackground) {
        final ObjectAnimator animator = this.animator;
        if (animator != null) {
            animator.cancel();
            this.animator = null;
        }
        final int animatedColorBackground2 = this.animatedColorBackground;
        if (animatedColorBackground2 != 0) {
            this.setBackgroundColor(animatedColorBackground2);
        }
        final Paint animationPaint = this.animationPaint;
        int overrideColor = 1;
        if (animationPaint == null) {
            this.animationPaint = new Paint(1);
        }
        final Switch checkBox = this.checkBox;
        if (!b) {
            overrideColor = 2;
        }
        checkBox.setOverrideColor(overrideColor);
        this.animatedColorBackground = animatedColorBackground;
        this.animationPaint.setColor(this.animatedColorBackground);
        this.animationProgress = 0.0f;
        (this.animator = ObjectAnimator.ofFloat((Object)this, (Property)TextCheckCell.ANIMATION_PROGRESS, new float[] { 0.0f, 1.0f })).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                final TextCheckCell this$0 = TextCheckCell.this;
                this$0.setBackgroundColor(this$0.animatedColorBackground);
                TextCheckCell.this.animatedColorBackground = 0;
                TextCheckCell.this.invalidate();
            }
        });
        this.animator.setInterpolator((TimeInterpolator)CubicBezierInterpolator.EASE_OUT);
        this.animator.setDuration(240L).start();
    }
    
    public void setChecked(final boolean b) {
        this.checkBox.setChecked(b, true);
    }
    
    public void setColors(final String tag, final String s, final String s2, final String s3, final String s4) {
        this.textView.setTextColor(Theme.getColor(tag));
        this.checkBox.setColors(s, s2, s3, s4);
        this.textView.setTag((Object)tag);
    }
    
    public void setDrawCheckRipple(final boolean drawCheckRipple) {
        this.drawCheckRipple = drawCheckRipple;
    }
    
    public void setEnabled(final boolean enabled, final ArrayList<Animator> list) {
        super.setEnabled(enabled);
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
            final Switch checkBox = this.checkBox;
            float n2;
            if (enabled) {
                n2 = 1.0f;
            }
            else {
                n2 = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)checkBox, "alpha", new float[] { n2 }));
            if (this.valueTextView.getVisibility() == 0) {
                final TextView valueTextView = this.valueTextView;
                if (!enabled) {
                    alpha = 0.5f;
                }
                list.add((Animator)ObjectAnimator.ofFloat((Object)valueTextView, "alpha", new float[] { alpha }));
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
            final Switch checkBox2 = this.checkBox;
            float alpha3;
            if (enabled) {
                alpha3 = 1.0f;
            }
            else {
                alpha3 = 0.5f;
            }
            checkBox2.setAlpha(alpha3);
            if (this.valueTextView.getVisibility() == 0) {
                final TextView valueTextView2 = this.valueTextView;
                if (!enabled) {
                    alpha = 0.5f;
                }
                valueTextView2.setAlpha(alpha);
            }
        }
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setPressed(final boolean b) {
        if (this.drawCheckRipple) {
            this.checkBox.setDrawRipple(b);
        }
        super.setPressed(b);
    }
    
    public void setTextAndCheck(final String text, final boolean b, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.isMultiline = false;
        this.checkBox.setChecked(b, false);
        this.needDivider = needDivider;
        this.valueTextView.setVisibility(8);
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.textView.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.topMargin = 0;
        this.textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.setWillNotDraw(needDivider ^ true);
    }
    
    public void setTextAndValueAndCheck(final String text, final String text2, final boolean b, final boolean isMultiline, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        this.valueTextView.setText((CharSequence)text2);
        this.checkBox.setChecked(b, false);
        this.needDivider = needDivider;
        this.valueTextView.setVisibility(0);
        this.isMultiline = isMultiline;
        if (isMultiline) {
            this.valueTextView.setLines(0);
            this.valueTextView.setMaxLines(0);
            this.valueTextView.setSingleLine(false);
            this.valueTextView.setEllipsize((TextUtils$TruncateAt)null);
            this.valueTextView.setPadding(0, 0, 0, AndroidUtilities.dp(11.0f));
        }
        else {
            this.valueTextView.setLines(1);
            this.valueTextView.setMaxLines(1);
            this.valueTextView.setSingleLine(true);
            this.valueTextView.setEllipsize(TextUtils$TruncateAt.END);
            this.valueTextView.setPadding(0, 0, 0, 0);
        }
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.textView.getLayoutParams();
        layoutParams.height = -2;
        layoutParams.topMargin = AndroidUtilities.dp(10.0f);
        this.textView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.setWillNotDraw(true ^ needDivider);
    }
    
    public void setTypeface(final Typeface typeface) {
        this.textView.setTypeface(typeface);
    }
}
