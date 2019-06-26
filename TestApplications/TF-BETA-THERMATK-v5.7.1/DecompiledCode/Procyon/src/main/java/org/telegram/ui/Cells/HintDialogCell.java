// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.text.Layout$Alignment;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserObject;
import android.view.View$MeasureSpec;
import android.graphics.Paint;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import android.graphics.Canvas;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.graphics.RectF;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.tgnet.TLRPC;
import android.text.StaticLayout;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class HintDialogCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private StaticLayout countLayout;
    private int countWidth;
    private int currentAccount;
    private TLRPC.User currentUser;
    private long dialog_id;
    private BackupImageView imageView;
    private int lastUnreadCount;
    private TextView nameTextView;
    private RectF rect;
    
    public HintDialogCell(final Context context) {
        super(context);
        this.avatarDrawable = new AvatarDrawable();
        this.rect = new RectF();
        this.currentAccount = UserConfig.selectedAccount;
        (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(27.0f));
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(54, 54.0f, 49, 0.0f, 7.0f, 0.0f, 0.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(1);
        this.nameTextView.setGravity(49);
        this.nameTextView.setLines(1);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 6.0f, 64.0f, 6.0f, 0.0f));
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, final long n) {
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (view == this.imageView) {
            if (this.countLayout != null) {
                final int dp = AndroidUtilities.dp(6.0f);
                final int dp2 = AndroidUtilities.dp(54.0f);
                final int n2 = dp2 - AndroidUtilities.dp(5.5f);
                this.rect.set((float)n2, (float)dp, (float)(n2 + this.countWidth + AndroidUtilities.dp(11.0f)), (float)(AndroidUtilities.dp(23.0f) + dp));
                final RectF rect = this.rect;
                final float density = AndroidUtilities.density;
                Paint paint;
                if (MessagesController.getInstance(this.currentAccount).isDialogMuted(this.dialog_id)) {
                    paint = Theme.dialogs_countGrayPaint;
                }
                else {
                    paint = Theme.dialogs_countPaint;
                }
                canvas.drawRoundRect(rect, density * 11.5f, density * 11.5f, paint);
                canvas.save();
                canvas.translate((float)dp2, (float)(dp + AndroidUtilities.dp(4.0f)));
                this.countLayout.draw(canvas);
                canvas.restore();
            }
            final TLRPC.User currentUser = this.currentUser;
            if (currentUser != null && !currentUser.bot) {
                final TLRPC.UserStatus status = currentUser.status;
                if ((status != null && status.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.currentUser.id)) {
                    final int dp3 = AndroidUtilities.dp(53.0f);
                    final int dp4 = AndroidUtilities.dp(59.0f);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite"));
                    final float n3 = (float)dp4;
                    final float n4 = (float)dp3;
                    canvas.drawCircle(n3, n4, (float)AndroidUtilities.dp(7.0f), Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle"));
                    canvas.drawCircle(n3, n4, (float)AndroidUtilities.dp(5.0f), Theme.dialogs_onlineCirclePaint);
                }
            }
        }
        return drawChild;
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(86.0f), 1073741824));
    }
    
    public void setDialog(final int i, final boolean b, final CharSequence charSequence) {
        this.dialog_id = i;
        if (i > 0) {
            this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(i);
            if (charSequence != null) {
                this.nameTextView.setText(charSequence);
            }
            else {
                final TLRPC.User currentUser = this.currentUser;
                if (currentUser != null) {
                    this.nameTextView.setText((CharSequence)UserObject.getFirstName(currentUser));
                }
                else {
                    this.nameTextView.setText((CharSequence)"");
                }
            }
            this.avatarDrawable.setInfo(this.currentUser);
            this.imageView.setImage(ImageLocation.getForUser(this.currentUser, false), "50_50", this.avatarDrawable, this.currentUser);
        }
        else {
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
            if (charSequence != null) {
                this.nameTextView.setText(charSequence);
            }
            else if (chat != null) {
                this.nameTextView.setText((CharSequence)chat.title);
            }
            else {
                this.nameTextView.setText((CharSequence)"");
            }
            this.avatarDrawable.setInfo(chat);
            this.currentUser = null;
            this.imageView.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, chat);
        }
        if (b) {
            this.update(0);
        }
        else {
            this.countLayout = null;
        }
    }
    
    public void update() {
        final int i = (int)this.dialog_id;
        if (i > 0) {
            this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(i);
            this.avatarDrawable.setInfo(this.currentUser);
        }
        else {
            this.avatarDrawable.setInfo(MessagesController.getInstance(this.currentAccount).getChat(-i));
            this.currentUser = null;
        }
    }
    
    public void update(final int n) {
        if ((n & 0x4) != 0x0 && this.currentUser != null) {
            this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(this.currentUser.id);
            this.imageView.invalidate();
            this.invalidate();
        }
        if (n != 0 && (n & 0x100) == 0x0 && (n & 0x800) == 0x0) {
            return;
        }
        final TLRPC.Dialog dialog = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
        if (dialog != null) {
            final int unread_count = dialog.unread_count;
            if (unread_count != 0) {
                if (this.lastUnreadCount == unread_count) {
                    return;
                }
                this.lastUnreadCount = unread_count;
                final String format = String.format("%d", unread_count);
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int)Math.ceil(Theme.dialogs_countTextPaint.measureText(format)));
                this.countLayout = new StaticLayout((CharSequence)format, Theme.dialogs_countTextPaint, this.countWidth, Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                if (n != 0) {
                    this.invalidate();
                }
                return;
            }
        }
        if (this.countLayout != null) {
            if (n != 0) {
                this.invalidate();
            }
            this.lastUnreadCount = 0;
            this.countLayout = null;
        }
    }
}
