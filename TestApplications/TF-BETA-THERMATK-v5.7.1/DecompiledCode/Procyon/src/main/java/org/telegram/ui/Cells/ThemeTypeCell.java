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

public class ThemeTypeCell extends FrameLayout
{
    private ImageView checkImage;
    private boolean needDivider;
    private TextView textView;
    
    public ThemeTypeCell(final Context context) {
        super(context);
        this.setWillNotDraw(false);
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
        float n4;
        if (LocaleController.isRTL) {
            n4 = 71.0f;
        }
        else {
            n4 = 21.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 21.0f;
        }
        else {
            n5 = 23.0f;
        }
        this.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n4, 0.0f, n5, 0.0f));
        (this.checkImage = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
        this.checkImage.setImageResource(2131165858);
        final ImageView checkImage = this.checkImage;
        int n6 = n;
        if (LocaleController.isRTL) {
            n6 = 3;
        }
        this.addView((View)checkImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(19, 14.0f, n6 | 0x10, 23.0f, 0.0f, 23.0f, 0.0f));
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
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setTypeChecked(final boolean b) {
        final ImageView checkImage = this.checkImage;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        checkImage.setVisibility(visibility);
    }
    
    public void setValue(final String text, final boolean b, final boolean needDivider) {
        this.textView.setText((CharSequence)text);
        final ImageView checkImage = this.checkImage;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        checkImage.setVisibility(visibility);
        this.needDivider = needDivider;
    }
}
