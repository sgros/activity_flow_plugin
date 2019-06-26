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
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.messenger.LocaleController;
import android.widget.ImageView;
import android.widget.FrameLayout;

public class LanguageCell extends FrameLayout
{
    private ImageView checkImage;
    private LocaleController.LocaleInfo currentLocale;
    private boolean isDialog;
    private boolean needDivider;
    private TextView textView;
    private TextView textView2;
    
    public LanguageCell(final Context context, final boolean isDialog) {
        super(context);
        this.setWillNotDraw(false);
        this.isDialog = isDialog;
        this.textView = new TextView(context);
        final TextView textView = this.textView;
        String s;
        if (isDialog) {
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
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView2 = this.textView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView2.setGravity(n2 | 0x30);
        final TextView textView3 = this.textView;
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
            n4 = 23.0f;
        }
        int n5;
        if (this.isDialog) {
            n5 = 4;
        }
        else {
            n5 = 7;
        }
        final float n6 = (float)n5;
        float n7;
        if (LocaleController.isRTL) {
            n7 = 23.0f;
        }
        else {
            n7 = 71.0f;
        }
        this.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n4, n6, n7, 0.0f));
        this.textView2 = new TextView(context);
        final TextView textView4 = this.textView2;
        String s2;
        if (isDialog) {
            s2 = "dialogTextGray3";
        }
        else {
            s2 = "windowBackgroundWhiteGrayText3";
        }
        textView4.setTextColor(Theme.getColor(s2));
        this.textView2.setTextSize(1, 13.0f);
        this.textView2.setLines(1);
        this.textView2.setMaxLines(1);
        this.textView2.setSingleLine(true);
        this.textView2.setEllipsize(TextUtils$TruncateAt.END);
        final TextView textView5 = this.textView2;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        textView5.setGravity(n8 | 0x30);
        final TextView textView6 = this.textView2;
        int n9;
        if (LocaleController.isRTL) {
            n9 = 5;
        }
        else {
            n9 = 3;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 71.0f;
        }
        else {
            n10 = 23.0f;
        }
        int n11;
        if (this.isDialog) {
            n11 = 25;
        }
        else {
            n11 = 29;
        }
        final float n12 = (float)n11;
        float n13;
        if (LocaleController.isRTL) {
            n13 = 23.0f;
        }
        else {
            n13 = 71.0f;
        }
        this.addView((View)textView6, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n9 | 0x30, n10, n12, n13, 0.0f));
        (this.checkImage = new ImageView(context)).setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("featuredStickers_addedIcon"), PorterDuff$Mode.MULTIPLY));
        this.checkImage.setImageResource(2131165858);
        final ImageView checkImage = this.checkImage;
        int n14 = n;
        if (LocaleController.isRTL) {
            n14 = 3;
        }
        this.addView((View)checkImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(19, 14.0f, n14 | 0x10, 23.0f, 0.0f, 23.0f, 0.0f));
    }
    
    public LocaleController.LocaleInfo getCurrentLocale() {
        return this.currentLocale;
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
    
    protected void onMeasure(int measureSpec, final int n) {
        measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(measureSpec), 1073741824);
        float n2;
        if (this.isDialog) {
            n2 = 50.0f;
        }
        else {
            n2 = 54.0f;
        }
        super.onMeasure(measureSpec, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(n2) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setLanguage(final LocaleController.LocaleInfo currentLocale, String name, final boolean needDivider) {
        final TextView textView = this.textView;
        if (name == null) {
            name = currentLocale.name;
        }
        textView.setText((CharSequence)name);
        this.textView2.setText((CharSequence)currentLocale.nameEnglish);
        this.currentLocale = currentLocale;
        this.needDivider = needDivider;
    }
    
    public void setLanguageSelected(final boolean b) {
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
    
    public void setValue(final String text, final String text2) {
        this.textView.setText((CharSequence)text);
        this.textView2.setText((CharSequence)text2);
        this.checkImage.setVisibility(4);
        this.currentLocale = null;
        this.needDivider = false;
    }
}
