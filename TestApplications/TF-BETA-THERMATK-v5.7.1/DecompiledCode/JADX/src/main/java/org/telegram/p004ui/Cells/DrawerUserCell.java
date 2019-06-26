package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.GroupCreateCheckBox;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.Cells.DrawerUserCell */
public class DrawerUserCell extends FrameLayout {
    private int accountNumber;
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private GroupCreateCheckBox checkBox;
    private BackupImageView imageView;
    private RectF rect = new RectF();
    private TextView textView;

    public DrawerUserCell(Context context) {
        super(context);
        this.avatarDrawable.setTextSize(AndroidUtilities.m26dp(12.0f));
        this.imageView = new BackupImageView(context);
        this.imageView.setRoundRadius(AndroidUtilities.m26dp(18.0f));
        addView(this.imageView, LayoutHelper.createFrame(36, 36.0f, 51, 14.0f, 6.0f, 0.0f, 0.0f));
        this.textView = new TextView(context);
        this.textView.setTextColor(Theme.getColor(Theme.key_chats_menuItemText));
        this.textView.setTextSize(1, 15.0f);
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setGravity(19);
        this.textView.setEllipsize(TruncateAt.END);
        addView(this.textView, LayoutHelper.createFrame(-1, -1.0f, 51, 72.0f, 0.0f, 60.0f, 0.0f));
        this.checkBox = new GroupCreateCheckBox(context);
        this.checkBox.setChecked(true, false);
        this.checkBox.setCheckScale(0.9f);
        this.checkBox.setInnerRadDiff(AndroidUtilities.m26dp(1.5f));
        this.checkBox.setColorKeysOverrides(Theme.key_chats_unreadCounterText, Theme.key_chats_unreadCounter, Theme.key_chats_menuBackground);
        addView(this.checkBox, LayoutHelper.createFrame(18, 18.0f, 51, 37.0f, 27.0f, 0.0f, 0.0f));
        setWillNotDraw(false);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(48.0f), 1073741824));
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.textView.setTextColor(Theme.getColor(Theme.key_chats_menuItemText));
    }

    public void setAccount(int i) {
        this.accountNumber = i;
        Object currentUser = UserConfig.getInstance(this.accountNumber).getCurrentUser();
        if (currentUser != null) {
            this.avatarDrawable.setInfo((User) currentUser);
            this.textView.setText(ContactsController.formatName(currentUser.first_name, currentUser.last_name));
            this.imageView.getImageReceiver().setCurrentAccount(i);
            int i2 = 0;
            this.imageView.setImage(ImageLocation.getForUser(currentUser, false), "50_50", this.avatarDrawable, currentUser);
            GroupCreateCheckBox groupCreateCheckBox = this.checkBox;
            if (i != UserConfig.selectedAccount) {
                i2 = 4;
            }
            groupCreateCheckBox.setVisibility(i2);
        }
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (UserConfig.getActivatedAccountsCount() > 1 && NotificationsController.getInstance(this.accountNumber).showBadgeNumber && NotificationsController.getInstance(this.accountNumber).getTotalUnreadCount() > 0) {
            String format = String.format("%d", new Object[]{Integer.valueOf(NotificationsController.getInstance(this.accountNumber).getTotalUnreadCount())});
            int dp = AndroidUtilities.m26dp(12.5f);
            int ceil = (int) Math.ceil((double) Theme.dialogs_countTextPaint.measureText(format));
            int max = Math.max(AndroidUtilities.m26dp(10.0f), ceil);
            int measuredWidth = ((getMeasuredWidth() - max) - AndroidUtilities.m26dp(25.0f)) - AndroidUtilities.m26dp(5.5f);
            this.rect.set((float) measuredWidth, (float) dp, (float) ((measuredWidth + max) + AndroidUtilities.m26dp(14.0f)), (float) (AndroidUtilities.m26dp(23.0f) + dp));
            RectF rectF = this.rect;
            float f = AndroidUtilities.density;
            canvas.drawRoundRect(rectF, f * 11.5f, f * 11.5f, Theme.dialogs_countPaint);
            rectF = this.rect;
            canvas.drawText(format, rectF.left + ((rectF.width() - ((float) ceil)) / 2.0f), (float) (dp + AndroidUtilities.m26dp(16.0f)), Theme.dialogs_countTextPaint);
        }
    }
}
