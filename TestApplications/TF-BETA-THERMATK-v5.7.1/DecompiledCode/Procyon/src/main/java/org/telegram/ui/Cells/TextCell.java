// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.drawable.Drawable;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.View;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import org.telegram.ui.ActionBar.SimpleTextView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class TextCell extends FrameLayout
{
    private ImageView imageView;
    private boolean needDivider;
    private SimpleTextView textView;
    private ImageView valueImageView;
    private SimpleTextView valueTextView;
    
    public TextCell(final Context context) {
        super(context);
        (this.textView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(16);
        final SimpleTextView textView = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int gravity;
        if (isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        this.addView((View)this.textView);
        (this.valueTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteValueText"));
        this.valueTextView.setTextSize(16);
        final SimpleTextView valueTextView = this.valueTextView;
        int gravity2 = n;
        if (LocaleController.isRTL) {
            gravity2 = 3;
        }
        valueTextView.setGravity(gravity2);
        this.addView((View)this.valueTextView);
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
        this.addView((View)this.imageView);
        (this.valueImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.addView((View)this.valueImageView);
        this.setFocusable(true);
    }
    
    public SimpleTextView getTextView() {
        return this.textView;
    }
    
    public ImageView getValueImageView() {
        return this.valueImageView;
    }
    
    public SimpleTextView getValueTextView() {
        return this.valueTextView;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            final boolean isRTL = LocaleController.isRTL;
            float n = 68.0f;
            float n2;
            if (isRTL) {
                n2 = 0.0f;
            }
            else {
                float n3;
                if (this.imageView.getVisibility() == 0) {
                    n3 = 68.0f;
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
    
    protected void onLayout(final boolean b, int n, int n2, int n3, int dp) {
        n2 = dp - n2;
        n3 -= n;
        dp = (n2 - this.valueTextView.getTextHeight()) / 2;
        if (LocaleController.isRTL) {
            n = AndroidUtilities.dp(23.0f);
        }
        else {
            n = 0;
        }
        final SimpleTextView valueTextView = this.valueTextView;
        valueTextView.layout(n, dp, valueTextView.getMeasuredWidth() + n, this.valueTextView.getMeasuredHeight() + dp);
        dp = (n2 - this.textView.getTextHeight()) / 2;
        final boolean isRTL = LocaleController.isRTL;
        float n4 = 71.0f;
        if (isRTL) {
            n = this.getMeasuredWidth();
            final int measuredWidth = this.textView.getMeasuredWidth();
            if (this.imageView.getVisibility() != 0) {
                n4 = 23.0f;
            }
            n = n - measuredWidth - AndroidUtilities.dp(n4);
        }
        else {
            if (this.imageView.getVisibility() != 0) {
                n4 = 23.0f;
            }
            n = AndroidUtilities.dp(n4);
        }
        final SimpleTextView textView = this.textView;
        textView.layout(n, dp, textView.getMeasuredWidth() + n, this.textView.getMeasuredHeight() + dp);
        if (this.imageView.getVisibility() == 0) {
            dp = AndroidUtilities.dp(5.0f);
            if (!LocaleController.isRTL) {
                n = AndroidUtilities.dp(21.0f);
            }
            else {
                n = n3 - this.imageView.getMeasuredWidth() - AndroidUtilities.dp(21.0f);
            }
            final ImageView imageView = this.imageView;
            imageView.layout(n, dp, imageView.getMeasuredWidth() + n, this.imageView.getMeasuredHeight() + dp);
        }
        if (this.valueImageView.getVisibility() == 0) {
            n2 = (n2 - this.valueImageView.getMeasuredHeight()) / 2;
            if (LocaleController.isRTL) {
                n = AndroidUtilities.dp(23.0f);
            }
            else {
                n = n3 - this.valueImageView.getMeasuredWidth() - AndroidUtilities.dp(23.0f);
            }
            final ImageView valueImageView = this.valueImageView;
            valueImageView.layout(n, n2, valueImageView.getMeasuredWidth() + n, this.valueImageView.getMeasuredHeight() + n2);
        }
    }
    
    protected void onMeasure(int dp, int size) {
        size = View$MeasureSpec.getSize(dp);
        dp = AndroidUtilities.dp(48.0f);
        this.valueTextView.measure(View$MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(23.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(95.0f) - this.valueTextView.getTextWidth(), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
        if (this.imageView.getVisibility() == 0) {
            this.imageView.measure(View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE));
        }
        if (this.valueImageView.getVisibility() == 0) {
            this.valueImageView.measure(View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE));
        }
        this.setMeasuredDimension(size, AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0));
    }
    
    public void setColors(final String tag, final String tag2) {
        this.textView.setTextColor(Theme.getColor(tag2));
        this.textView.setTag((Object)tag2);
        if (tag != null) {
            this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor(tag), PorterDuff$Mode.MULTIPLY));
            this.imageView.setTag((Object)tag);
        }
    }
    
    public void setText(final String text, final boolean needDivider) {
        this.textView.setText(text);
        this.valueTextView.setText(null);
        this.imageView.setVisibility(8);
        this.valueTextView.setVisibility(8);
        this.valueImageView.setVisibility(8);
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
    }
    
    public void setTextAndIcon(final String text, final int imageResource, final boolean needDivider) {
        this.textView.setText(text);
        this.valueTextView.setText(null);
        this.imageView.setImageResource(imageResource);
        this.imageView.setVisibility(0);
        this.valueTextView.setVisibility(8);
        this.valueImageView.setVisibility(8);
        this.imageView.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
    }
    
    public void setTextAndValue(final String text, final String text2, final boolean needDivider) {
        this.textView.setText(text);
        this.valueTextView.setText(text2);
        this.valueTextView.setVisibility(0);
        this.imageView.setVisibility(8);
        this.valueImageView.setVisibility(8);
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
    }
    
    public void setTextAndValueAndIcon(final String text, final String text2, final int imageResource, final boolean needDivider) {
        this.textView.setText(text);
        this.valueTextView.setText(text2);
        this.valueTextView.setVisibility(0);
        this.valueImageView.setVisibility(8);
        this.imageView.setVisibility(0);
        this.imageView.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        this.imageView.setImageResource(imageResource);
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
    }
    
    public void setTextAndValueDrawable(final String text, final Drawable imageDrawable, final boolean needDivider) {
        this.textView.setText(text);
        this.valueTextView.setText(null);
        this.valueImageView.setVisibility(0);
        this.valueImageView.setImageDrawable(imageDrawable);
        this.valueTextView.setVisibility(8);
        this.imageView.setVisibility(8);
        this.imageView.setPadding(0, AndroidUtilities.dp(7.0f), 0, 0);
        this.needDivider = needDivider;
        this.setWillNotDraw(this.needDivider ^ true);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}
