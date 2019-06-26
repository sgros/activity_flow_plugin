// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.SeekBarView;
import android.widget.FrameLayout;

public class MaxFileSizeCell extends FrameLayout
{
    private long currentSize;
    private SeekBarView seekBarView;
    private TextView sizeTextView;
    private TextView textView;
    
    public MaxFileSizeCell(final Context context) {
        super(context);
        this.setWillNotDraw(false);
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("dialogTextBlack"));
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
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
        textView.setGravity(n2 | 0x30);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView2 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, 21.0f, 13.0f, 21.0f, 0.0f));
        (this.sizeTextView = new TextView(context)).setTextColor(Theme.getColor("dialogTextBlue2"));
        this.sizeTextView.setTextSize(1, 16.0f);
        this.sizeTextView.setLines(1);
        this.sizeTextView.setMaxLines(1);
        this.sizeTextView.setSingleLine(true);
        final TextView sizeTextView = this.sizeTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 3;
        }
        else {
            n4 = 5;
        }
        sizeTextView.setGravity(n4 | 0x30);
        final TextView sizeTextView2 = this.sizeTextView;
        int n5 = n;
        if (LocaleController.isRTL) {
            n5 = 3;
        }
        this.addView((View)sizeTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1.0f, n5 | 0x30, 21.0f, 13.0f, 21.0f, 0.0f));
        (this.seekBarView = new SeekBarView(context) {
            @Override
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        }).setReportChanges(true);
        this.seekBarView.setDelegate((SeekBarView.SeekBarViewDelegate)new _$$Lambda$MaxFileSizeCell$cPUnEl5DY5tp_A3IQaD5cETcr3k(this));
        this.addView((View)this.seekBarView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 30.0f, 51, 10.0f, 40.0f, 10.0f, 0.0f));
    }
    
    protected void didChangedSizeValue(final int n) {
    }
    
    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return !this.isEnabled() || super.dispatchTouchEvent(motionEvent);
    }
    
    public long getSize() {
        return this.currentSize;
    }
    
    protected void onDraw(final Canvas canvas) {
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
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return !this.isEnabled() || super.onInterceptTouchEvent(motionEvent);
    }
    
    protected void onMeasure(int max, final int n) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(max), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
        this.setMeasuredDimension(View$MeasureSpec.getSize(max), AndroidUtilities.dp(80.0f));
        max = this.getMeasuredWidth() - AndroidUtilities.dp(42.0f);
        this.sizeTextView.measure(View$MeasureSpec.makeMeasureSpec(max, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824));
        max = Math.max(AndroidUtilities.dp(10.0f), max - this.sizeTextView.getMeasuredWidth() - AndroidUtilities.dp(8.0f));
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(max, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824));
        this.seekBarView.measure(View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth() - AndroidUtilities.dp(20.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824));
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return !this.isEnabled() || super.onTouchEvent(motionEvent);
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
            final SeekBarView seekBarView = this.seekBarView;
            float n2;
            if (enabled) {
                n2 = 1.0f;
            }
            else {
                n2 = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)seekBarView, "alpha", new float[] { n2 }));
            final TextView sizeTextView = this.sizeTextView;
            if (!enabled) {
                alpha = 0.5f;
            }
            list.add((Animator)ObjectAnimator.ofFloat((Object)sizeTextView, "alpha", new float[] { alpha }));
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
            final SeekBarView seekBarView2 = this.seekBarView;
            float alpha3;
            if (enabled) {
                alpha3 = 1.0f;
            }
            else {
                alpha3 = 0.5f;
            }
            seekBarView2.setAlpha(alpha3);
            final TextView sizeTextView2 = this.sizeTextView;
            if (!enabled) {
                alpha = 0.5f;
            }
            sizeTextView2.setAlpha(alpha);
        }
    }
    
    public void setSize(long currentSize) {
        this.currentSize = currentSize;
        this.sizeTextView.setText((CharSequence)LocaleController.formatString("AutodownloadSizeLimitUpTo", 2131558801, AndroidUtilities.formatFileSize(currentSize)));
        currentSize -= 512000L;
        float progress;
        if (currentSize < 536576L) {
            progress = Math.max(0.0f, currentSize / 536576.0f) * 0.25f;
        }
        else {
            currentSize -= 536576L;
            if (currentSize < 9437184L) {
                progress = Math.max(0.0f, currentSize / 9437184.0f) * 0.25f + 0.25f;
            }
            else {
                float n = 0.5f;
                currentSize -= 9437184L;
                float n2;
                if (currentSize < 94371840L) {
                    n2 = Math.max(0.0f, currentSize / 9.437184E7f);
                }
                else {
                    n = 0.75f;
                    n2 = Math.max(0.0f, (currentSize - 94371840L) / 1.50575514E9f);
                }
                progress = n2 * 0.25f + n;
            }
        }
        this.seekBarView.setProgress(progress);
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
}
