// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.tgnet.TLRPC;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserConfig;
import android.view.View$MeasureSpec;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import android.widget.ImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class AccountSelectCell extends FrameLayout
{
    private int accountNumber;
    private AvatarDrawable avatarDrawable;
    private ImageView checkImageView;
    private BackupImageView imageView;
    private TextView textView;
    
    public AccountSelectCell(final Context context) {
        super(context);
        (this.avatarDrawable = new AvatarDrawable()).setTextSize(AndroidUtilities.dp(12.0f));
        (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(18.0f));
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 51, 10.0f, 10.0f, 0.0f, 0.0f));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setGravity(19);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 61.0f, 0.0f, 56.0f, 0.0f));
        (this.checkImageView = new ImageView(context)).setImageResource(2131165270);
        this.checkImageView.setScaleType(ImageView$ScaleType.CENTER);
        this.checkImageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_menuItemCheck"), PorterDuff$Mode.MULTIPLY));
        this.addView((View)this.checkImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, -1.0f, 53, 0.0f, 0.0f, 6.0f, 0.0f));
    }
    
    public int getAccountNumber() {
        return this.accountNumber;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(56.0f), 1073741824));
    }
    
    public void setAccount(int visibility, final boolean b) {
        this.accountNumber = visibility;
        final TLRPC.User currentUser = UserConfig.getInstance(this.accountNumber).getCurrentUser();
        this.avatarDrawable.setInfo(currentUser);
        this.textView.setText((CharSequence)ContactsController.formatName(currentUser.first_name, currentUser.last_name));
        this.imageView.getImageReceiver().setCurrentAccount(visibility);
        final BackupImageView imageView = this.imageView;
        final int n = 0;
        imageView.setImage(ImageLocation.getForUser(currentUser, false), "50_50", this.avatarDrawable, currentUser);
        final ImageView checkImageView = this.checkImageView;
        if (b && visibility == UserConfig.selectedAccount) {
            visibility = n;
        }
        else {
            visibility = 4;
        }
        checkImageView.setVisibility(visibility);
    }
}
