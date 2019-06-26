// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.drawable.Drawable;
import org.telegram.tgnet.TLRPC;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.text.TextUtils$TruncateAt;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.Components.BackupImageView;
import android.widget.TextView;
import android.widget.FrameLayout;

public class LocationCell extends FrameLayout
{
    private TextView addressTextView;
    private BackupImageView imageView;
    private TextView nameTextView;
    private boolean needDivider;
    
    public LocationCell(final Context context) {
        super(context);
        (this.imageView = new BackupImageView(context)).setBackgroundResource(2131165803);
        this.imageView.setSize(AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f));
        this.imageView.getImageReceiver().setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), PorterDuff$Mode.MULTIPLY));
        final BackupImageView imageView = this.imageView;
        final boolean isRTL = LocaleController.isRTL;
        final int n = 5;
        int n2;
        if (isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 0.0f;
        }
        else {
            n3 = 17.0f;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 17.0f;
        }
        else {
            n4 = 0.0f;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n2 | 0x30, n3, 8.0f, n4, 0.0f));
        (this.nameTextView = new TextView(context)).setTextSize(1, 16.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        final TextView nameTextView = this.nameTextView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = 5;
        }
        else {
            gravity = 3;
        }
        nameTextView.setGravity(gravity);
        final TextView nameTextView2 = this.nameTextView;
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        final int n6 = 16;
        int n7;
        if (isRTL2) {
            n7 = 16;
        }
        else {
            n7 = 72;
        }
        final float n8 = (float)n7;
        int n9;
        if (LocaleController.isRTL) {
            n9 = 72;
        }
        else {
            n9 = 16;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n5 | 0x30, n8, 5.0f, (float)n9, 0.0f));
        (this.addressTextView = new TextView(context)).setTextSize(1, 14.0f);
        this.addressTextView.setMaxLines(1);
        this.addressTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addressTextView.setSingleLine(true);
        this.addressTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText3"));
        final TextView addressTextView = this.addressTextView;
        int gravity2;
        if (LocaleController.isRTL) {
            gravity2 = 5;
        }
        else {
            gravity2 = 3;
        }
        addressTextView.setGravity(gravity2);
        final TextView addressTextView2 = this.addressTextView;
        int n10;
        if (LocaleController.isRTL) {
            n10 = n;
        }
        else {
            n10 = 3;
        }
        int n11;
        if (LocaleController.isRTL) {
            n11 = 16;
        }
        else {
            n11 = 72;
        }
        final float n12 = (float)n11;
        int n13 = n6;
        if (LocaleController.isRTL) {
            n13 = 72;
        }
        this.addView((View)addressTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n10 | 0x30, n12, 30.0f, (float)n13, 0.0f));
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine((float)AndroidUtilities.dp(72.0f), (float)(this.getHeight() - 1), (float)this.getWidth(), (float)(this.getHeight() - 1), Theme.dividerPaint);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f) + (this.needDivider ? 1 : 0), 1073741824));
    }
    
    public void setLocation(final TLRPC.TL_messageMediaVenue tl_messageMediaVenue, final String s, final boolean needDivider) {
        this.needDivider = needDivider;
        this.nameTextView.setText((CharSequence)tl_messageMediaVenue.title);
        this.addressTextView.setText((CharSequence)tl_messageMediaVenue.address);
        this.imageView.setImage(s, null, null);
        this.setWillNotDraw(needDivider ^ true);
    }
}
