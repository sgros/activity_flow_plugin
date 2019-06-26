// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ContactsController;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.LocaleController;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class CheckBoxUserCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private CheckBoxSquare checkBox;
    private TLRPC.User currentUser;
    private BackupImageView imageView;
    private boolean needDivider;
    private TextView textView;
    
    public CheckBoxUserCell(final Context context, final boolean b) {
        super(context);
        this.textView = new TextView(context);
        final TextView textView = this.textView;
        String s;
        if (b) {
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
        textView2.setGravity(n2 | 0x10);
        final TextView textView3 = this.textView;
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        final boolean isRTL2 = LocaleController.isRTL;
        final int n4 = 94;
        int n5;
        if (isRTL2) {
            n5 = 21;
        }
        else {
            n5 = 94;
        }
        final float n6 = (float)n5;
        int n7;
        if (LocaleController.isRTL) {
            n7 = n4;
        }
        else {
            n7 = 21;
        }
        this.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n3 | 0x30, n6, 0.0f, (float)n7, 0.0f));
        this.avatarDrawable = new AvatarDrawable();
        (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(36.0f));
        final BackupImageView imageView = this.imageView;
        int n8;
        if (LocaleController.isRTL) {
            n8 = 5;
        }
        else {
            n8 = 3;
        }
        this.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, n8 | 0x30, 48.0f, 7.0f, 48.0f, 0.0f));
        this.checkBox = new CheckBoxSquare(context, b);
        final CheckBoxSquare checkBox = this.checkBox;
        int n9;
        if (LocaleController.isRTL) {
            n9 = n;
        }
        else {
            n9 = 3;
        }
        final boolean isRTL3 = LocaleController.isRTL;
        final int n10 = 0;
        int n11;
        if (isRTL3) {
            n11 = 0;
        }
        else {
            n11 = 21;
        }
        final float n12 = (float)n11;
        int n13 = n10;
        if (LocaleController.isRTL) {
            n13 = 21;
        }
        this.addView((View)checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, n9 | 0x30, n12, 16.0f, (float)n13, 0.0f));
    }
    
    public CheckBoxSquare getCheckBox() {
        return this.checkBox;
    }
    
    public TLRPC.User getCurrentUser() {
        return this.currentUser;
    }
    
    public TextView getTextView() {
        return this.textView;
    }
    
    public boolean isChecked() {
        return this.checkBox.isChecked();
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
    
    public void setChecked(final boolean b, final boolean b2) {
        this.checkBox.setChecked(b, b2);
    }
    
    public void setTextColor(final int textColor) {
        this.textView.setTextColor(textColor);
    }
    
    public void setUser(final TLRPC.User user, final boolean b, final boolean needDivider) {
        this.currentUser = user;
        this.textView.setText((CharSequence)ContactsController.formatName(user.first_name, user.last_name));
        this.checkBox.setChecked(b, false);
        this.avatarDrawable.setInfo(user);
        this.imageView.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, user);
        this.setWillNotDraw((this.needDivider = needDivider) ^ true);
    }
}
