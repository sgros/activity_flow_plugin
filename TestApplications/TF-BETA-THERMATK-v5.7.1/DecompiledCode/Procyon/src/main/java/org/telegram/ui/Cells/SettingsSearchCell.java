// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.Rect;
import android.graphics.Paint$FontMetricsInt;
import android.graphics.Paint;
import android.text.style.ImageSpan;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.widget.FrameLayout$LayoutParams;
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
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class SettingsSearchCell extends FrameLayout
{
    private ImageView imageView;
    private int left;
    private boolean needDivider;
    private TextView textView;
    private TextView valueTextView;
    
    public SettingsSearchCell(final Context context) {
        super(context);
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(1, 16.0f);
        final TextView textView = this.textView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView2 = this.textView;
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        float n2;
        if (LocaleController.isRTL) {
            n2 = 16.0f;
        }
        else {
            n2 = 71.0f;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 71.0f;
        }
        else {
            n3 = 16.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n, n2, 10.0f, n3, 0.0f));
        (this.valueTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.valueTextView.setTextSize(1, 13.0f);
        this.valueTextView.setLines(1);
        this.valueTextView.setMaxLines(1);
        this.valueTextView.setSingleLine(true);
        final TextView valueTextView = this.valueTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        valueTextView.setGravity(gravity2);
        final TextView valueTextView2 = this.valueTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 16.0f;
        }
        else {
            n5 = 71.0f;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 71.0f;
        }
        else {
            n6 = 16.0f;
        }
        this.addView((View)valueTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n4, n5, 33.0f, n6, 0.0f));
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
        final ImageView imageView = this.imageView;
        int n7;
        if (LocaleController.isRTL) {
            n7 = 5;
        }
        else {
            n7 = 3;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, n7, 10.0f, 8.0f, 10.0f, 0.0f));
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            float n;
            if (LocaleController.isRTL) {
                n = 0.0f;
            }
            else {
                n = (float)AndroidUtilities.dp((float)this.left);
            }
            final float n2 = (float)(this.getMeasuredHeight() - 1);
            final int measuredWidth = this.getMeasuredWidth();
            int dp;
            if (LocaleController.isRTL) {
                dp = AndroidUtilities.dp((float)this.left);
            }
            else {
                dp = 0;
            }
            canvas.drawLine(n, n2, (float)(measuredWidth - dp), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setTextAndValue(final CharSequence charSequence, final String[] array, final boolean b, final boolean needDivider) {
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.textView.getLayoutParams();
        if (b) {
            this.valueTextView.setText(charSequence);
            final SpannableStringBuilder text = new SpannableStringBuilder();
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    text.append((CharSequence)" > ");
                    final Drawable mutate = this.getContext().getResources().getDrawable(2131165815).mutate();
                    mutate.setBounds(0, 0, mutate.getIntrinsicWidth(), mutate.getIntrinsicHeight());
                    mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff$Mode.MULTIPLY));
                    text.setSpan((Object)new VerticalImageSpan(mutate), text.length() - 2, text.length() - 1, 33);
                }
                text.append((CharSequence)array[i]);
            }
            this.textView.setText((CharSequence)text);
            this.valueTextView.setVisibility(0);
            frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(10.0f);
        }
        else {
            this.textView.setText(charSequence);
            if (array != null) {
                final SpannableStringBuilder text2 = new SpannableStringBuilder();
                for (int j = 0; j < array.length; ++j) {
                    if (j != 0) {
                        text2.append((CharSequence)" > ");
                        final Drawable mutate2 = this.getContext().getResources().getDrawable(2131165815).mutate();
                        mutate2.setBounds(0, 0, mutate2.getIntrinsicWidth(), mutate2.getIntrinsicHeight());
                        mutate2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText2"), PorterDuff$Mode.MULTIPLY));
                        text2.setSpan((Object)new VerticalImageSpan(mutate2), text2.length() - 2, text2.length() - 1, 33);
                    }
                    text2.append((CharSequence)array[j]);
                }
                this.valueTextView.setText((CharSequence)text2);
                this.valueTextView.setVisibility(0);
                frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(10.0f);
            }
            else {
                frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(21.0f);
                this.valueTextView.setVisibility(8);
            }
        }
        final int dp = AndroidUtilities.dp(16.0f);
        frameLayout$LayoutParams.rightMargin = dp;
        frameLayout$LayoutParams.leftMargin = dp;
        final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.valueTextView.getLayoutParams();
        final int dp2 = AndroidUtilities.dp(16.0f);
        frameLayout$LayoutParams2.rightMargin = dp2;
        frameLayout$LayoutParams2.leftMargin = dp2;
        this.imageView.setVisibility(8);
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
        this.left = 16;
    }
    
    public void setTextAndValueAndIcon(final CharSequence text, final String[] array, final int imageResource, final boolean needDivider) {
        this.textView.setText(text);
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.textView.getLayoutParams();
        final boolean isRTL = LocaleController.isRTL;
        final float n = 16.0f;
        float n2;
        if (isRTL) {
            n2 = 16.0f;
        }
        else {
            n2 = 71.0f;
        }
        frameLayout$LayoutParams.leftMargin = AndroidUtilities.dp(n2);
        float n3;
        if (LocaleController.isRTL) {
            n3 = 71.0f;
        }
        else {
            n3 = 16.0f;
        }
        frameLayout$LayoutParams.rightMargin = AndroidUtilities.dp(n3);
        if (array != null) {
            final SpannableStringBuilder text2 = new SpannableStringBuilder();
            for (int i = 0; i < array.length; ++i) {
                if (i != 0) {
                    text2.append((CharSequence)" > ");
                    final Drawable mutate = this.getContext().getResources().getDrawable(2131165815).mutate();
                    mutate.setBounds(0, 0, mutate.getIntrinsicWidth(), mutate.getIntrinsicHeight());
                    mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText2"), PorterDuff$Mode.MULTIPLY));
                    text2.setSpan((Object)new VerticalImageSpan(mutate), text2.length() - 2, text2.length() - 1, 33);
                }
                text2.append((CharSequence)array[i]);
            }
            this.valueTextView.setText((CharSequence)text2);
            this.valueTextView.setVisibility(0);
            frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(10.0f);
            final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.valueTextView.getLayoutParams();
            float n4;
            if (LocaleController.isRTL) {
                n4 = 16.0f;
            }
            else {
                n4 = 71.0f;
            }
            frameLayout$LayoutParams2.leftMargin = AndroidUtilities.dp(n4);
            float n5 = n;
            if (LocaleController.isRTL) {
                n5 = 71.0f;
            }
            frameLayout$LayoutParams2.rightMargin = AndroidUtilities.dp(n5);
        }
        else {
            frameLayout$LayoutParams.topMargin = AndroidUtilities.dp(21.0f);
            this.valueTextView.setVisibility(8);
        }
        if (imageResource != 0) {
            this.imageView.setImageResource(imageResource);
            this.imageView.setVisibility(0);
        }
        else {
            this.imageView.setVisibility(8);
        }
        this.left = 69;
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
    }
    
    public class VerticalImageSpan extends ImageSpan
    {
        public VerticalImageSpan(final Drawable drawable) {
            super(drawable);
        }
        
        public void draw(final Canvas canvas, final CharSequence charSequence, int descent, final int n, final float n2, final int n3, final int n4, final int n5, final Paint paint) {
            final Drawable drawable = this.getDrawable();
            canvas.save();
            final Paint$FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
            descent = fontMetricsInt.descent;
            canvas.translate(n2, (float)(n4 + descent - (descent - fontMetricsInt.ascent) / 2 - (drawable.getBounds().bottom - drawable.getBounds().top) / 2));
            if (LocaleController.isRTL) {
                canvas.scale(-1.0f, 1.0f, (float)(drawable.getIntrinsicWidth() / 2), (float)(drawable.getIntrinsicHeight() / 2));
            }
            drawable.draw(canvas);
            canvas.restore();
        }
        
        public int getSize(final Paint paint, final CharSequence charSequence, int bottom, int top, final Paint$FontMetricsInt paint$FontMetricsInt) {
            final Rect bounds = this.getDrawable().getBounds();
            if (paint$FontMetricsInt != null) {
                final Paint$FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
                final int descent = fontMetricsInt.descent;
                final int ascent = fontMetricsInt.ascent;
                bottom = bounds.bottom;
                top = bounds.top;
                final int n = ascent + (descent - ascent) / 2;
                bottom = (bottom - top) / 2;
                paint$FontMetricsInt.ascent = n - bottom;
                paint$FontMetricsInt.top = paint$FontMetricsInt.ascent;
                paint$FontMetricsInt.bottom = n + bottom;
                paint$FontMetricsInt.descent = paint$FontMetricsInt.bottom;
            }
            return bounds.right;
        }
    }
}
