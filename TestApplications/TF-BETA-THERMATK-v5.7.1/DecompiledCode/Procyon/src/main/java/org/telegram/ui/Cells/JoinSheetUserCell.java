// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ContactsController;
import org.telegram.tgnet.TLRPC;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import android.view.View$MeasureSpec;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class JoinSheetUserCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView imageView;
    private TextView nameTextView;
    private int[] result;
    
    public JoinSheetUserCell(final Context context) {
        super(context);
        this.avatarDrawable = new AvatarDrawable();
        this.result = new int[1];
        (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(27.0f));
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, 54.0f, 49, 0.0f, 7.0f, 0.0f, 0.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("dialogTextBlack"));
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setGravity(49);
        this.nameTextView.setLines(1);
        this.nameTextView.setSingleLine(true);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 6.0f, 64.0f, 6.0f, 0.0f));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(90.0f), 1073741824));
    }
    
    public void setCount(final int n) {
        this.nameTextView.setText((CharSequence)"");
        final AvatarDrawable avatarDrawable = this.avatarDrawable;
        final StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(LocaleController.formatShortNumber(n, this.result));
        avatarDrawable.setInfo(0, null, null, false, sb.toString());
        this.imageView.setImage(null, "50_50", this.avatarDrawable, null);
    }
    
    public void setUser(final TLRPC.User info) {
        this.nameTextView.setText((CharSequence)ContactsController.formatName(info.first_name, info.last_name));
        this.avatarDrawable.setInfo(info);
        this.imageView.setImage(ImageLocation.getForUser(info, false), "50_50", this.avatarDrawable, info);
    }
}
