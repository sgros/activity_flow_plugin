// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ContactsController;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserObject;
import android.view.View$MeasureSpec;
import org.telegram.tgnet.ConnectionsManager;
import android.os.SystemClock;
import org.telegram.messenger.MessagesController;
import android.graphics.Canvas;
import org.telegram.ui.Components.CheckBoxBase;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.widget.TextView;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;

public class ShareDialogCell extends FrameLayout
{
    private AvatarDrawable avatarDrawable;
    private CheckBox2 checkBox;
    private int currentAccount;
    private BackupImageView imageView;
    private long lastUpdateTime;
    private TextView nameTextView;
    private float onlineProgress;
    private TLRPC.User user;
    
    public ShareDialogCell(final Context context) {
        super(context);
        this.avatarDrawable = new AvatarDrawable();
        this.currentAccount = UserConfig.selectedAccount;
        this.setWillNotDraw(false);
        (this.imageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(28.0f));
        this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 56.0f, 49, 0.0f, 7.0f, 0.0f, 0.0f));
        (this.nameTextView = new TextView(context)).setTextColor(Theme.getColor("dialogTextBlack"));
        this.nameTextView.setTextSize(1, 12.0f);
        this.nameTextView.setMaxLines(2);
        this.nameTextView.setGravity(49);
        this.nameTextView.setLines(2);
        this.nameTextView.setEllipsize(TextUtils$TruncateAt.END);
        this.addView((View)this.nameTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 6.0f, 66.0f, 6.0f, 0.0f));
        (this.checkBox = new CheckBox2(context)).setSize(21);
        this.checkBox.setColor("dialogRoundCheckBox", "dialogBackground", "dialogRoundCheckBoxCheck");
        this.checkBox.setDrawUnchecked(false);
        this.checkBox.setDrawBackgroundAsArc(4);
        this.checkBox.setProgressDelegate(new _$$Lambda$ShareDialogCell$Ua6Rykc_bDn7xM5VKieCIkl7edo(this));
        this.addView((View)this.checkBox, (ViewGroup$LayoutParams)LayoutHelper.createFrame(24, 24.0f, 49, 19.0f, 42.0f, 0.0f, 0.0f));
    }
    
    protected boolean drawChild(final Canvas canvas, final View view, long n) {
        final boolean drawChild = super.drawChild(canvas, view, n);
        if (view == this.imageView) {
            final TLRPC.User user = this.user;
            if (user != null && !MessagesController.isSupportUser(user)) {
                final long uptimeMillis = SystemClock.uptimeMillis();
                if ((n = uptimeMillis - this.lastUpdateTime) > 17L) {
                    n = 17L;
                }
                this.lastUpdateTime = uptimeMillis;
                final TLRPC.User user2 = this.user;
                boolean b = false;
                Label_0149: {
                    if (!user2.self && !user2.bot) {
                        final TLRPC.UserStatus status = user2.status;
                        if ((status != null && status.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.user.id)) {
                            b = true;
                            break Label_0149;
                        }
                    }
                    b = false;
                }
                if (b || this.onlineProgress != 0.0f) {
                    final int bottom = this.imageView.getBottom();
                    final int dp = AndroidUtilities.dp(6.0f);
                    final int right = this.imageView.getRight();
                    final int dp2 = AndroidUtilities.dp(10.0f);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite"));
                    final float n2 = (float)(right - dp2);
                    final float n3 = (float)(bottom - dp);
                    canvas.drawCircle(n2, n3, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle"));
                    canvas.drawCircle(n2, n3, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (b) {
                        final float onlineProgress = this.onlineProgress;
                        if (onlineProgress < 1.0f) {
                            this.onlineProgress = onlineProgress + n / 150.0f;
                            if (this.onlineProgress > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                            this.imageView.invalidate();
                            this.invalidate();
                        }
                    }
                    else {
                        final float onlineProgress2 = this.onlineProgress;
                        if (onlineProgress2 > 0.0f) {
                            this.onlineProgress = onlineProgress2 - n / 150.0f;
                            if (this.onlineProgress < 0.0f) {
                                this.onlineProgress = 0.0f;
                            }
                            this.imageView.invalidate();
                            this.invalidate();
                        }
                    }
                }
            }
        }
        return drawChild;
    }
    
    protected void onDraw(final Canvas canvas) {
        final int left = this.imageView.getLeft();
        final int n = this.imageView.getMeasuredWidth() / 2;
        final int top = this.imageView.getTop();
        final int n2 = this.imageView.getMeasuredHeight() / 2;
        Theme.checkboxSquare_checkPaint.setColor(Theme.getColor("dialogRoundCheckBox"));
        Theme.checkboxSquare_checkPaint.setAlpha((int)(this.checkBox.getProgress() * 255.0f));
        canvas.drawCircle((float)(left + n), (float)(top + n2), (float)AndroidUtilities.dp(28.0f), Theme.checkboxSquare_checkPaint);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(103.0f), 1073741824));
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        this.checkBox.setChecked(b, b2);
    }
    
    public void setDialog(final int i, final boolean b, final CharSequence charSequence) {
        if (i > 0) {
            this.user = MessagesController.getInstance(this.currentAccount).getUser(i);
            this.avatarDrawable.setInfo(this.user);
            if (UserObject.isUserSelf(this.user)) {
                this.nameTextView.setText((CharSequence)LocaleController.getString("SavedMessages", 2131560633));
                this.avatarDrawable.setAvatarType(1);
                this.imageView.setImage(null, null, this.avatarDrawable, this.user);
            }
            else {
                if (charSequence != null) {
                    this.nameTextView.setText(charSequence);
                }
                else {
                    final TLRPC.User user = this.user;
                    if (user != null) {
                        this.nameTextView.setText((CharSequence)ContactsController.formatName(user.first_name, user.last_name));
                    }
                    else {
                        this.nameTextView.setText((CharSequence)"");
                    }
                }
                this.imageView.setImage(ImageLocation.getForUser(this.user, false), "50_50", this.avatarDrawable, this.user);
            }
        }
        else {
            this.user = null;
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
            this.imageView.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, chat);
        }
        this.checkBox.setChecked(b, false);
    }
}
