// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.tgnet.TLRPC;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ContactsController;
import android.view.View$MeasureSpec;
import android.graphics.Paint;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.UserConfig;
import android.graphics.Canvas;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import android.widget.TextView;
import android.graphics.RectF;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.GroupCreateCheckBox;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class DrawerUserCell extends FrameLayout
{
    private int accountNumber;
    private AvatarDrawable avatarDrawable;
    private GroupCreateCheckBox checkBox;
    private BackupImageView imageView;
    private RectF rect;
    private TextView textView;
    
    public DrawerUserCell(final Context context) {
        super(context);
        this.rect = new RectF();
        (this.avatarDrawable = new AvatarDrawable()).setTextSize(AndroidUtilities.dp(12.0f));
        (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(18.0f));
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(36, 36.0f, 51, 14.0f, 6.0f, 0.0f, 0.0f));
        (this.textView = new TextView(context)).setTextColor(Theme.getColor("chats_menuItemText"));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setGravity(19);
        this.textView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 72.0f, 0.0f, 60.0f, 0.0f));
        (this.checkBox = new GroupCreateCheckBox(context)).setChecked(true, false);
        this.checkBox.setCheckScale(0.9f);
        this.checkBox.setInnerRadDiff(AndroidUtilities.dp(1.5f));
        this.checkBox.setColorKeysOverrides("chats_unreadCounterText", "chats_unreadCounter", "chats_menuBackground");
        this.addView((View)this.checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(18, 18.0f, 51, 37.0f, 27.0f, 0.0f, 0.0f));
        this.setWillNotDraw(false);
    }
    
    public int getAccountNumber() {
        return this.accountNumber;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.textView.setTextColor(Theme.getColor("chats_menuItemText"));
    }
    
    protected void onDraw(final Canvas canvas) {
        if (UserConfig.getActivatedAccountsCount() > 1) {
            if (NotificationsController.getInstance(this.accountNumber).showBadgeNumber) {
                final int totalUnreadCount = NotificationsController.getInstance(this.accountNumber).getTotalUnreadCount();
                if (totalUnreadCount <= 0) {
                    return;
                }
                final String format = String.format("%d", totalUnreadCount);
                final int dp = AndroidUtilities.dp(12.5f);
                final int b = (int)Math.ceil(Theme.dialogs_countTextPaint.measureText(format));
                final int max = Math.max(AndroidUtilities.dp(10.0f), b);
                final int n = this.getMeasuredWidth() - max - AndroidUtilities.dp(25.0f) - AndroidUtilities.dp(5.5f);
                this.rect.set((float)n, (float)dp, (float)(n + max + AndroidUtilities.dp(14.0f)), (float)(AndroidUtilities.dp(23.0f) + dp));
                final RectF rect = this.rect;
                final float density = AndroidUtilities.density;
                canvas.drawRoundRect(rect, density * 11.5f, density * 11.5f, Theme.dialogs_countPaint);
                final RectF rect2 = this.rect;
                canvas.drawText(format, rect2.left + (rect2.width() - b) / 2.0f, (float)(dp + AndroidUtilities.dp(16.0f)), (Paint)Theme.dialogs_countTextPaint);
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
    }
    
    public void setAccount(int visibility) {
        this.accountNumber = visibility;
        final TLRPC.User currentUser = UserConfig.getInstance(this.accountNumber).getCurrentUser();
        if (currentUser == null) {
            return;
        }
        this.avatarDrawable.setInfo(currentUser);
        this.textView.setText((CharSequence)ContactsController.formatName(currentUser.first_name, currentUser.last_name));
        this.imageView.getImageReceiver().setCurrentAccount(visibility);
        final BackupImageView imageView = this.imageView;
        final int n = 0;
        imageView.setImage(ImageLocation.getForUser(currentUser, false), "50_50", this.avatarDrawable, currentUser);
        final GroupCreateCheckBox checkBox = this.checkBox;
        if (visibility == UserConfig.selectedAccount) {
            visibility = n;
        }
        else {
            visibility = 4;
        }
        checkBox.setVisibility(visibility);
    }
}
