// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

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

public class ManageChatTextCell extends FrameLayout
{
    private boolean divider;
    private ImageView imageView;
    private SimpleTextView textView;
    private SimpleTextView valueTextView;
    
    public ManageChatTextCell(final Context context) {
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
    }
    
    public SimpleTextView getTextView() {
        return this.textView;
    }
    
    public SimpleTextView getValueTextView() {
        return this.valueTextView;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.divider) {
            canvas.drawLine((float)AndroidUtilities.dp(71.0f), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onLayout(final boolean b, int dp, int n, final int n2, int n3) {
        n3 -= n;
        final int n4 = (n3 - this.valueTextView.getTextHeight()) / 2;
        if (LocaleController.isRTL) {
            n = AndroidUtilities.dp(24.0f);
        }
        else {
            n = 0;
        }
        final SimpleTextView valueTextView = this.valueTextView;
        valueTextView.layout(n, n4, valueTextView.getMeasuredWidth() + n, this.valueTextView.getMeasuredHeight() + n4);
        n3 = (n3 - this.textView.getTextHeight()) / 2;
        if (!LocaleController.isRTL) {
            n = AndroidUtilities.dp(71.0f);
        }
        else {
            n = AndroidUtilities.dp(24.0f);
        }
        final SimpleTextView textView = this.textView;
        textView.layout(n, n3, textView.getMeasuredWidth() + n, this.textView.getMeasuredHeight() + n3);
        n = AndroidUtilities.dp(9.0f);
        if (!LocaleController.isRTL) {
            dp = AndroidUtilities.dp(21.0f);
        }
        else {
            dp = n2 - dp - this.imageView.getMeasuredWidth() - AndroidUtilities.dp(21.0f);
        }
        final ImageView imageView = this.imageView;
        imageView.layout(dp, n, imageView.getMeasuredWidth() + dp, this.imageView.getMeasuredHeight() + n);
    }
    
    protected void onMeasure(int dp, int size) {
        size = View$MeasureSpec.getSize(dp);
        dp = AndroidUtilities.dp(48.0f);
        this.valueTextView.measure(View$MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(24.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(95.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
        this.imageView.measure(View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE));
        this.setMeasuredDimension(size, AndroidUtilities.dp(56.0f) + (this.divider ? 1 : 0));
    }
    
    public void setColors(final String tag, final String tag2) {
        this.textView.setTextColor(Theme.getColor(tag2));
        this.textView.setTag((Object)tag2);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor(tag), PorterDuff$Mode.MULTIPLY));
        this.imageView.setTag((Object)tag);
    }
    
    public void setText(final String text, final String text2, final int imageResource, final boolean divider) {
        this.textView.setText(text);
        if (text2 != null) {
            this.valueTextView.setText(text2);
            this.valueTextView.setVisibility(0);
        }
        else {
            this.valueTextView.setVisibility(4);
        }
        this.imageView.setPadding(0, AndroidUtilities.dp(5.0f), 0, 0);
        this.imageView.setImageResource(imageResource);
        this.divider = divider;
        this.setWillNotDraw(this.divider ^ true);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}
