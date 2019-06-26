// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.ActionBar;

import android.view.View$MeasureSpec;
import android.text.TextUtils$TruncateAt;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class ActionBarMenuSubItem extends FrameLayout
{
    private ImageView imageView;
    private TextView textView;
    
    public ActionBarMenuSubItem(final Context context) {
        super(context);
        this.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
        this.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        (this.imageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("actionBarDefaultSubmenuItemIcon"), PorterDuff$Mode.MULTIPLY));
        final ImageView imageView = this.imageView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, 40, n2 | 0x10));
        (this.textView = new TextView(context)).setLines(1);
        this.textView.setSingleLine(true);
        this.textView.setGravity(1);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        this.textView.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        this.textView.setTextSize(1, 16.0f);
        final TextView textView = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = n;
        }
        else {
            n3 = 3;
        }
        this.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2, n3 | 0x10));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
    }
    
    public void setColors(final int textColor, final int n) {
        this.textView.setTextColor(textColor);
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
    }
    
    public void setIcon(final int imageResource) {
        this.imageView.setImageResource(imageResource);
    }
    
    public void setIconColor(final int n) {
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.MULTIPLY));
    }
    
    public void setText(final String text) {
        this.textView.setText((CharSequence)text);
    }
    
    public void setTextAndIcon(final CharSequence text, int dp) {
        this.textView.setText(text);
        if (dp != 0) {
            this.imageView.setImageResource(dp);
            this.imageView.setVisibility(0);
            final TextView textView = this.textView;
            if (LocaleController.isRTL) {
                dp = 0;
            }
            else {
                dp = AndroidUtilities.dp(43.0f);
            }
            int dp2;
            if (LocaleController.isRTL) {
                dp2 = AndroidUtilities.dp(43.0f);
            }
            else {
                dp2 = 0;
            }
            textView.setPadding(dp, 0, dp2, 0);
        }
        else {
            this.imageView.setVisibility(4);
            this.textView.setPadding(0, 0, 0, 0);
        }
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
}
