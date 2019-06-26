// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.Point;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.WebFile;
import java.util.Locale;
import org.telegram.tgnet.TLRPC;
import android.view.View$MeasureSpec;
import android.text.TextUtils$TruncateAt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.Components.BackupImageView;
import android.widget.TextView;
import android.widget.FrameLayout;

public class PaymentInfoCell extends FrameLayout
{
    private TextView detailExTextView;
    private TextView detailTextView;
    private BackupImageView imageView;
    private TextView nameTextView;
    
    public PaymentInfoCell(final Context context) {
        super(context);
        this.imageView = new BackupImageView(context);
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
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(100, 100.0f, n2, 10.0f, 10.0f, 10.0f, 0.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(1, 16.0f);
        this.nameTextView.setLines(1);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView nameTextView = this.nameTextView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        nameTextView.setGravity(n3 | 0x30);
        final TextView nameTextView2 = this.nameTextView;
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 10.0f;
        }
        else {
            n5 = 123.0f;
        }
        float n6;
        if (LocaleController.isRTL) {
            n6 = 123.0f;
        }
        else {
            n6 = 10.0f;
        }
        this.addView((View)nameTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n4 | 0x30, n5, 9.0f, n6, 0.0f));
        (this.detailTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.detailTextView.setTextSize(1, 14.0f);
        this.detailTextView.setMaxLines(3);
        this.detailTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView detailTextView = this.detailTextView;
        int n7;
        if (LocaleController.isRTL) {
            n7 = 5;
        }
        else {
            n7 = 3;
        }
        detailTextView.setGravity(n7 | 0x30);
        final TextView detailTextView2 = this.detailTextView;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        float n9;
        if (LocaleController.isRTL) {
            n9 = 10.0f;
        }
        else {
            n9 = 123.0f;
        }
        float n10;
        if (LocaleController.isRTL) {
            n10 = 123.0f;
        }
        else {
            n10 = 10.0f;
        }
        this.addView((View)detailTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n8 | 0x30, n9, 33.0f, n10, 0.0f));
        (this.detailExTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.detailExTextView.setTextSize(1, 13.0f);
        this.detailExTextView.setLines(1);
        this.detailExTextView.setMaxLines(1);
        this.detailExTextView.setSingleLine(true);
        this.detailExTextView.setEllipsize(TextUtils$TruncateAt.END);
        final TextView detailExTextView = this.detailExTextView;
        int n11;
        if (LocaleController.isRTL) {
            n11 = 5;
        }
        else {
            n11 = 3;
        }
        detailExTextView.setGravity(n11 | 0x30);
        final TextView detailExTextView2 = this.detailExTextView;
        int n12;
        if (LocaleController.isRTL) {
            n12 = n;
        }
        else {
            n12 = 3;
        }
        float n13;
        if (LocaleController.isRTL) {
            n13 = 10.0f;
        }
        else {
            n13 = 123.0f;
        }
        float n14;
        if (LocaleController.isRTL) {
            n14 = 123.0f;
        }
        else {
            n14 = 10.0f;
        }
        this.addView((View)detailExTextView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n12 | 0x30, n13, 90.0f, n14, 0.0f));
    }
    
    protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        n = this.detailTextView.getBottom() + AndroidUtilities.dp(3.0f);
        final TextView detailExTextView = this.detailExTextView;
        detailExTextView.layout(detailExTextView.getLeft(), n, this.detailExTextView.getRight(), this.detailExTextView.getMeasuredHeight() + n);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(120.0f), 1073741824));
    }
    
    public void setInvoice(final TLRPC.TL_messageMediaInvoice tl_messageMediaInvoice, String format) {
        this.nameTextView.setText((CharSequence)tl_messageMediaInvoice.title);
        this.detailTextView.setText((CharSequence)tl_messageMediaInvoice.description);
        this.detailExTextView.setText((CharSequence)format);
        int n;
        if (AndroidUtilities.isTablet()) {
            n = AndroidUtilities.getMinTabletSide();
        }
        else {
            final Point displaySize = AndroidUtilities.displaySize;
            n = Math.min(displaySize.x, displaySize.y);
        }
        final int n2 = (int)(n * 0.7f);
        final float n3 = 640;
        final float n4 = n3 / (n2 - AndroidUtilities.dp(2.0f));
        final int i = (int)(n3 / n4);
        final int j = (int)(360 / n4);
        final TLRPC.WebDocument photo = tl_messageMediaInvoice.photo;
        int n5 = 5;
        if (photo != null && photo.mime_type.startsWith("image/")) {
            final TextView nameTextView = this.nameTextView;
            int n6;
            if (LocaleController.isRTL) {
                n6 = 5;
            }
            else {
                n6 = 3;
            }
            float n7;
            if (LocaleController.isRTL) {
                n7 = 10.0f;
            }
            else {
                n7 = 123.0f;
            }
            float n8;
            if (LocaleController.isRTL) {
                n8 = 123.0f;
            }
            else {
                n8 = 10.0f;
            }
            nameTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n6 | 0x30, n7, 9.0f, n8, 0.0f));
            final TextView detailTextView = this.detailTextView;
            int n9;
            if (LocaleController.isRTL) {
                n9 = 5;
            }
            else {
                n9 = 3;
            }
            float n10;
            if (LocaleController.isRTL) {
                n10 = 10.0f;
            }
            else {
                n10 = 123.0f;
            }
            float n11;
            if (LocaleController.isRTL) {
                n11 = 123.0f;
            }
            else {
                n11 = 10.0f;
            }
            detailTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n9 | 0x30, n10, 33.0f, n11, 0.0f));
            final TextView detailExTextView = this.detailExTextView;
            if (!LocaleController.isRTL) {
                n5 = 3;
            }
            float n12;
            if (LocaleController.isRTL) {
                n12 = 10.0f;
            }
            else {
                n12 = 123.0f;
            }
            float n13;
            if (LocaleController.isRTL) {
                n13 = 123.0f;
            }
            else {
                n13 = 10.0f;
            }
            detailExTextView.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n5 | 0x30, n12, 90.0f, n13, 0.0f));
            this.imageView.setVisibility(0);
            format = String.format(Locale.US, "%d_%d", i, j);
            this.imageView.getImageReceiver().setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(tl_messageMediaInvoice.photo)), format, null, null, -1, null, tl_messageMediaInvoice, 1);
        }
        else {
            final TextView nameTextView2 = this.nameTextView;
            int n14;
            if (LocaleController.isRTL) {
                n14 = 5;
            }
            else {
                n14 = 3;
            }
            nameTextView2.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n14 | 0x30, 17.0f, 9.0f, 17.0f, 0.0f));
            final TextView detailTextView2 = this.detailTextView;
            int n15;
            if (LocaleController.isRTL) {
                n15 = 5;
            }
            else {
                n15 = 3;
            }
            detailTextView2.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n15 | 0x30, 17.0f, 33.0f, 17.0f, 0.0f));
            final TextView detailExTextView2 = this.detailExTextView;
            if (!LocaleController.isRTL) {
                n5 = 3;
            }
            detailExTextView2.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n5 | 0x30, 17.0f, 90.0f, 17.0f, 0.0f));
            this.imageView.setVisibility(8);
        }
    }
}
