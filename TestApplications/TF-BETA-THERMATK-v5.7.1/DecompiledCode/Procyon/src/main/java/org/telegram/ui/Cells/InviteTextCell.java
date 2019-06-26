// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
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

public class InviteTextCell extends FrameLayout
{
    private ImageView imageView;
    private SimpleTextView textView;
    
    public InviteTextCell(final Context context) {
        super(context);
        (this.textView = new SimpleTextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.textView.setTextSize(17);
        final SimpleTextView textView = this.textView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        textView.setGravity(gravity);
        this.addView((View)this.textView);
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
        this.addView((View)this.imageView);
    }
    
    public SimpleTextView getTextView() {
        return this.textView;
    }
    
    protected void onLayout(final boolean b, int dp, int dp2, final int n, int n2) {
        final int n3 = n2 - dp2;
        n2 = (n3 - this.textView.getTextHeight()) / 2;
        float n4;
        if (!LocaleController.isRTL) {
            n4 = 71.0f;
        }
        else {
            n4 = 24.0f;
        }
        dp2 = AndroidUtilities.dp(n4);
        final SimpleTextView textView = this.textView;
        textView.layout(dp2, n2, textView.getMeasuredWidth() + dp2, this.textView.getMeasuredHeight() + n2);
        dp2 = (n3 - this.imageView.getMeasuredHeight()) / 2;
        if (!LocaleController.isRTL) {
            dp = AndroidUtilities.dp(20.0f);
        }
        else {
            dp = n - dp - this.imageView.getMeasuredWidth() - AndroidUtilities.dp(20.0f);
        }
        final ImageView imageView = this.imageView;
        imageView.layout(dp, dp2, imageView.getMeasuredWidth() + dp, this.imageView.getMeasuredHeight() + dp2);
    }
    
    protected void onMeasure(int size, int dp) {
        size = View$MeasureSpec.getSize(size);
        dp = AndroidUtilities.dp(72.0f);
        this.textView.measure(View$MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(95.0f), Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), 1073741824));
        this.imageView.measure(View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE));
        this.setMeasuredDimension(size, AndroidUtilities.dp(72.0f));
    }
    
    public void setTextAndIcon(final String text, final int imageResource) {
        this.textView.setText(text);
        this.imageView.setImageResource(imageResource);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}
