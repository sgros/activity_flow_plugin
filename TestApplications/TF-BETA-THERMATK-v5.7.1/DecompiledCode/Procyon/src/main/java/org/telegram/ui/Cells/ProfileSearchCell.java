// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import org.telegram.messenger.ImageLocation;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.tgnet.TLObject;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.graphics.drawable.Drawable;
import org.telegram.tgnet.ConnectionsManager;
import android.text.TextUtils;
import android.text.TextUtils$TruncateAt;
import android.text.Layout$Alignment;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.graphics.RectF;
import android.text.StaticLayout;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Components.AvatarDrawable;

public class ProfileSearchCell extends BaseCell
{
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage;
    private TLRPC.Chat chat;
    private StaticLayout countLayout;
    private int countLeft;
    private int countTop;
    private int countWidth;
    private int currentAccount;
    private CharSequence currentName;
    private long dialog_id;
    private boolean drawCheck;
    private boolean drawCount;
    private boolean drawNameBot;
    private boolean drawNameBroadcast;
    private boolean drawNameGroup;
    private boolean drawNameLock;
    private TLRPC.EncryptedChat encryptedChat;
    private TLRPC.FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private int lastUnreadCount;
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameTop;
    private int nameWidth;
    private RectF rect;
    private boolean savedMessages;
    private StaticLayout statusLayout;
    private int statusLeft;
    private CharSequence subLabel;
    private int sublabelOffsetX;
    private int sublabelOffsetY;
    public boolean useSeparator;
    private TLRPC.User user;
    
    public ProfileSearchCell(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.countTop = AndroidUtilities.dp(19.0f);
        this.rect = new RectF();
        (this.avatarImage = new ImageReceiver((View)this)).setRoundRadius(AndroidUtilities.dp(23.0f));
        this.avatarDrawable = new AvatarDrawable();
    }
    
    public void buildLayout() {
        this.drawNameBroadcast = false;
        this.drawNameLock = false;
        this.drawNameGroup = false;
        this.drawCheck = false;
        this.drawNameBot = false;
        final TLRPC.EncryptedChat encryptedChat = this.encryptedChat;
        if (encryptedChat != null) {
            this.drawNameLock = true;
            this.dialog_id = (long)encryptedChat.id << 32;
            if (!LocaleController.isRTL) {
                this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                this.nameLeft = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4)) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
            }
            else {
                this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 2)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                this.nameLeft = AndroidUtilities.dp(11.0f);
            }
            this.nameLockTop = AndroidUtilities.dp(20.5f);
        }
        else {
            final TLRPC.Chat chat = this.chat;
            if (chat != null) {
                final int id = chat.id;
                if (id < 0) {
                    this.dialog_id = AndroidUtilities.makeBroadcastId(id);
                    if (SharedConfig.drawDialogIcons) {
                        this.drawNameBroadcast = true;
                    }
                    this.nameLockTop = AndroidUtilities.dp(22.5f);
                }
                else {
                    this.dialog_id = -id;
                    if (SharedConfig.drawDialogIcons) {
                        if (ChatObject.isChannel(chat) && !this.chat.megagroup) {
                            this.drawNameBroadcast = true;
                            this.nameLockTop = AndroidUtilities.dp(22.5f);
                        }
                        else {
                            this.drawNameGroup = true;
                            this.nameLockTop = AndroidUtilities.dp(24.0f);
                        }
                    }
                }
                this.drawCheck = this.chat.verified;
                if (SharedConfig.drawDialogIcons) {
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                        final int dp = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4));
                        Drawable drawable;
                        if (this.drawNameGroup) {
                            drawable = Theme.dialogs_groupDrawable;
                        }
                        else {
                            drawable = Theme.dialogs_broadcastDrawable;
                        }
                        this.nameLeft = dp + drawable.getIntrinsicWidth();
                    }
                    else {
                        final int measuredWidth = this.getMeasuredWidth();
                        final int dp2 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 2));
                        Drawable drawable2;
                        if (this.drawNameGroup) {
                            drawable2 = Theme.dialogs_groupDrawable;
                        }
                        else {
                            drawable2 = Theme.dialogs_broadcastDrawable;
                        }
                        this.nameLockLeft = measuredWidth - dp2 - drawable2.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(11.0f);
                    }
                }
                else if (!LocaleController.isRTL) {
                    this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                }
                else {
                    this.nameLeft = AndroidUtilities.dp(11.0f);
                }
            }
            else {
                final TLRPC.User user = this.user;
                if (user != null) {
                    this.dialog_id = user.id;
                    if (!LocaleController.isRTL) {
                        this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                    }
                    else {
                        this.nameLeft = AndroidUtilities.dp(11.0f);
                    }
                    Label_0583: {
                        if (SharedConfig.drawDialogIcons) {
                            final TLRPC.User user2 = this.user;
                            if (user2.bot && !MessagesController.isSupportUser(user2)) {
                                this.drawNameBot = true;
                                if (!LocaleController.isRTL) {
                                    this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                                    this.nameLeft = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4)) + Theme.dialogs_botDrawable.getIntrinsicWidth();
                                }
                                else {
                                    this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 2)) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                                    this.nameLeft = AndroidUtilities.dp(11.0f);
                                }
                                this.nameLockTop = AndroidUtilities.dp(20.5f);
                                break Label_0583;
                            }
                        }
                        this.nameLockTop = AndroidUtilities.dp(21.0f);
                    }
                    this.drawCheck = this.user.verified;
                }
            }
        }
        CharSequence charSequence = this.currentName;
        if (charSequence == null) {
            final TLRPC.Chat chat2 = this.chat;
            String s;
            if (chat2 != null) {
                s = chat2.title;
            }
            else {
                final TLRPC.User user3 = this.user;
                if (user3 != null) {
                    s = UserObject.getUserName(user3);
                }
                else {
                    s = "";
                }
            }
            charSequence = s.replace('\n', ' ');
        }
        CharSequence charSequence2 = charSequence;
        Label_0750: {
            if (charSequence.length() == 0) {
                final TLRPC.User user4 = this.user;
                if (user4 != null) {
                    final String phone = user4.phone;
                    if (phone != null && phone.length() != 0) {
                        final PhoneFormat instance = PhoneFormat.getInstance();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("+");
                        sb.append(this.user.phone);
                        charSequence2 = instance.format(sb.toString());
                        break Label_0750;
                    }
                }
                charSequence2 = LocaleController.getString("HiddenName", 2131559636);
            }
        }
        TextPaint textPaint;
        if (this.encryptedChat != null) {
            textPaint = Theme.dialogs_searchNameEncryptedPaint;
        }
        else {
            textPaint = Theme.dialogs_searchNamePaint;
        }
        int n;
        if (!LocaleController.isRTL) {
            n = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp(14.0f);
            this.nameWidth = n;
        }
        else {
            n = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            this.nameWidth = n;
        }
        if (this.drawNameLock) {
            this.nameWidth -= AndroidUtilities.dp(6.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        else if (this.drawNameBroadcast) {
            this.nameWidth -= AndroidUtilities.dp(6.0f) + Theme.dialogs_broadcastDrawable.getIntrinsicWidth();
        }
        else if (this.drawNameGroup) {
            this.nameWidth -= AndroidUtilities.dp(6.0f) + Theme.dialogs_groupDrawable.getIntrinsicWidth();
        }
        else if (this.drawNameBot) {
            this.nameWidth -= AndroidUtilities.dp(6.0f) + Theme.dialogs_botDrawable.getIntrinsicWidth();
        }
        this.nameWidth -= this.getPaddingLeft() + this.getPaddingRight();
        final int n2 = n - (this.getPaddingLeft() + this.getPaddingRight());
        Label_1194: {
            if (this.drawCount) {
                final TLRPC.Dialog dialog = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
                if (dialog != null) {
                    final int unread_count = dialog.unread_count;
                    if (unread_count != 0) {
                        this.lastUnreadCount = unread_count;
                        final String format = String.format("%d", unread_count);
                        this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int)Math.ceil(Theme.dialogs_countTextPaint.measureText(format)));
                        this.countLayout = new StaticLayout((CharSequence)format, Theme.dialogs_countTextPaint, this.countWidth, Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        final int n3 = this.countWidth + AndroidUtilities.dp(18.0f);
                        this.nameWidth -= n3;
                        if (!LocaleController.isRTL) {
                            this.countLeft = this.getMeasuredWidth() - this.countWidth - AndroidUtilities.dp(19.0f);
                            break Label_1194;
                        }
                        this.countLeft = AndroidUtilities.dp(19.0f);
                        this.nameLeft += n3;
                        break Label_1194;
                    }
                }
                this.lastUnreadCount = 0;
                this.countLayout = null;
            }
            else {
                this.lastUnreadCount = 0;
                this.countLayout = null;
            }
        }
        this.nameLayout = new StaticLayout(TextUtils.ellipsize(charSequence2, textPaint, (float)(this.nameWidth - AndroidUtilities.dp(12.0f)), TextUtils$TruncateAt.END), textPaint, this.nameWidth, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        TextPaint dialogs_offlinePaint = Theme.dialogs_offlinePaint;
        if (!LocaleController.isRTL) {
            this.statusLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
        }
        else {
            this.statusLeft = AndroidUtilities.dp(11.0f);
        }
        final TLRPC.Chat chat3 = this.chat;
        CharSequence charSequence3 = null;
        if (chat3 != null && this.subLabel == null) {
            Label_1403: {
                if (chat3 != null) {
                    if (ChatObject.isChannel(chat3)) {
                        final TLRPC.Chat chat4 = this.chat;
                        if (!chat4.megagroup) {
                            if (TextUtils.isEmpty((CharSequence)chat4.username)) {
                                charSequence3 = LocaleController.getString("ChannelPrivate", 2131558988).toLowerCase();
                                break Label_1403;
                            }
                            charSequence3 = LocaleController.getString("ChannelPublic", 2131558991).toLowerCase();
                            break Label_1403;
                        }
                    }
                    if (TextUtils.isEmpty((CharSequence)this.chat.username)) {
                        charSequence3 = LocaleController.getString("MegaPrivate", 2131559831).toLowerCase();
                    }
                    else {
                        charSequence3 = LocaleController.getString("MegaPublic", 2131559834).toLowerCase();
                    }
                }
                else {
                    charSequence3 = null;
                }
            }
            this.nameTop = AndroidUtilities.dp(19.0f);
        }
        else {
            charSequence3 = this.subLabel;
            TextPaint dialogs_onlinePaint = null;
            Label_1648: {
                if (charSequence3 != null) {
                    dialogs_onlinePaint = dialogs_offlinePaint;
                }
                else {
                    final TLRPC.User user5 = this.user;
                    if (user5 != null) {
                        if (MessagesController.isSupportUser(user5)) {
                            charSequence3 = LocaleController.getString("SupportStatus", 2131560848);
                            dialogs_onlinePaint = dialogs_offlinePaint;
                        }
                        else {
                            final TLRPC.User user6 = this.user;
                            if (user6.bot) {
                                charSequence3 = LocaleController.getString("Bot", 2131558848);
                                dialogs_onlinePaint = dialogs_offlinePaint;
                            }
                            else {
                                final int id2 = user6.id;
                                if (id2 != 333000 && id2 != 777000) {
                                    final String formatUserStatus = LocaleController.formatUserStatus(this.currentAccount, user6);
                                    final TLRPC.User user7 = this.user;
                                    dialogs_onlinePaint = dialogs_offlinePaint;
                                    charSequence3 = formatUserStatus;
                                    if (user7 != null) {
                                        if (user7.id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                            final TLRPC.UserStatus status = this.user.status;
                                            dialogs_onlinePaint = dialogs_offlinePaint;
                                            charSequence3 = formatUserStatus;
                                            if (status == null) {
                                                break Label_1648;
                                            }
                                            dialogs_onlinePaint = dialogs_offlinePaint;
                                            charSequence3 = formatUserStatus;
                                            if (status.expires <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                                                break Label_1648;
                                            }
                                        }
                                        dialogs_onlinePaint = Theme.dialogs_onlinePaint;
                                        charSequence3 = LocaleController.getString("Online", 2131560100);
                                    }
                                }
                                else {
                                    charSequence3 = LocaleController.getString("ServiceNotifications", 2131560724);
                                    dialogs_onlinePaint = dialogs_offlinePaint;
                                }
                            }
                        }
                    }
                    else {
                        charSequence3 = null;
                        dialogs_onlinePaint = dialogs_offlinePaint;
                    }
                }
            }
            dialogs_offlinePaint = dialogs_onlinePaint;
            if (this.savedMessages) {
                this.nameTop = AndroidUtilities.dp(19.0f);
                charSequence3 = null;
                dialogs_offlinePaint = dialogs_onlinePaint;
            }
        }
        if (!TextUtils.isEmpty(charSequence3)) {
            this.statusLayout = new StaticLayout(TextUtils.ellipsize(charSequence3, dialogs_offlinePaint, (float)(n2 - AndroidUtilities.dp(12.0f)), TextUtils$TruncateAt.END), dialogs_offlinePaint, n2, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            this.nameTop = AndroidUtilities.dp(9.0f);
            this.nameLockTop -= AndroidUtilities.dp(10.0f);
        }
        else {
            this.statusLayout = null;
        }
        int n4;
        if (LocaleController.isRTL) {
            n4 = this.getMeasuredWidth() - AndroidUtilities.dp(57.0f) - this.getPaddingRight();
        }
        else {
            n4 = AndroidUtilities.dp(11.0f) + this.getPaddingLeft();
        }
        this.avatarImage.setImageCoords(n4, AndroidUtilities.dp(7.0f), AndroidUtilities.dp(46.0f), AndroidUtilities.dp(46.0f));
        if (LocaleController.isRTL) {
            if (this.nameLayout.getLineCount() > 0 && this.nameLayout.getLineLeft(0) == 0.0f) {
                final double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                final int nameWidth = this.nameWidth;
                if (ceil < nameWidth) {
                    final double v = this.nameLeft;
                    final double v2 = nameWidth;
                    Double.isNaN(v2);
                    Double.isNaN(v);
                    this.nameLeft = (int)(v + (v2 - ceil));
                }
            }
            final StaticLayout statusLayout = this.statusLayout;
            if (statusLayout != null && statusLayout.getLineCount() > 0 && this.statusLayout.getLineLeft(0) == 0.0f) {
                final double ceil2 = Math.ceil(this.statusLayout.getLineWidth(0));
                final double v3 = n2;
                if (ceil2 < v3) {
                    final double v4 = this.statusLeft;
                    Double.isNaN(v3);
                    Double.isNaN(v4);
                    this.statusLeft = (int)(v4 + (v3 - ceil2));
                }
            }
        }
        else {
            if (this.nameLayout.getLineCount() > 0 && this.nameLayout.getLineRight(0) == this.nameWidth) {
                final double ceil3 = Math.ceil(this.nameLayout.getLineWidth(0));
                final int nameWidth2 = this.nameWidth;
                if (ceil3 < nameWidth2) {
                    final double v5 = this.nameLeft;
                    final double v6 = nameWidth2;
                    Double.isNaN(v6);
                    Double.isNaN(v5);
                    this.nameLeft = (int)(v5 - (v6 - ceil3));
                }
            }
            final StaticLayout statusLayout2 = this.statusLayout;
            if (statusLayout2 != null && statusLayout2.getLineCount() > 0 && this.statusLayout.getLineRight(0) == n2) {
                final double ceil4 = Math.ceil(this.statusLayout.getLineWidth(0));
                final double v7 = n2;
                if (ceil4 < v7) {
                    final double v8 = this.statusLeft;
                    Double.isNaN(v7);
                    Double.isNaN(v8);
                    this.statusLeft = (int)(v8 - (v7 - ceil4));
                }
            }
        }
        this.nameLeft += this.getPaddingLeft();
        this.statusLeft += this.getPaddingLeft();
        this.nameLockLeft += this.getPaddingLeft();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.avatarImage.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.user == null && this.chat == null && this.encryptedChat == null) {
            return;
        }
        if (this.useSeparator) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
            else {
                canvas.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
        }
        if (this.drawNameLock) {
            BaseCell.setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_lockDrawable.draw(canvas);
        }
        else if (this.drawNameGroup) {
            BaseCell.setDrawableBounds(Theme.dialogs_groupDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_groupDrawable.draw(canvas);
        }
        else if (this.drawNameBroadcast) {
            BaseCell.setDrawableBounds(Theme.dialogs_broadcastDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_broadcastDrawable.draw(canvas);
        }
        else if (this.drawNameBot) {
            BaseCell.setDrawableBounds(Theme.dialogs_botDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_botDrawable.draw(canvas);
        }
        if (this.nameLayout != null) {
            canvas.save();
            canvas.translate((float)this.nameLeft, (float)this.nameTop);
            this.nameLayout.draw(canvas);
            canvas.restore();
            if (this.drawCheck) {
                int n;
                if (LocaleController.isRTL) {
                    if (this.nameLayout.getLineLeft(0) == 0.0f) {
                        n = this.nameLeft - AndroidUtilities.dp(6.0f) - Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                    }
                    else {
                        final float lineWidth = this.nameLayout.getLineWidth(0);
                        final double v = this.nameLeft + this.nameWidth;
                        final double ceil = Math.ceil(lineWidth);
                        Double.isNaN(v);
                        final double v2 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(v2);
                        final double v3 = Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                        Double.isNaN(v3);
                        n = (int)(v - ceil - v2 - v3);
                    }
                }
                else {
                    n = (int)(this.nameLeft + this.nameLayout.getLineRight(0) + AndroidUtilities.dp(6.0f));
                }
                BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, n, this.nameTop + AndroidUtilities.dp(3.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, n, this.nameTop + AndroidUtilities.dp(3.0f));
                Theme.dialogs_verifiedDrawable.draw(canvas);
                Theme.dialogs_verifiedCheckDrawable.draw(canvas);
            }
        }
        if (this.statusLayout != null) {
            canvas.save();
            canvas.translate((float)(this.statusLeft + this.sublabelOffsetX), (float)(AndroidUtilities.dp(33.0f) + this.sublabelOffsetY));
            this.statusLayout.draw(canvas);
            canvas.restore();
        }
        if (this.countLayout != null) {
            final int n2 = this.countLeft - AndroidUtilities.dp(5.5f);
            this.rect.set((float)n2, (float)this.countTop, (float)(n2 + this.countWidth + AndroidUtilities.dp(11.0f)), (float)(this.countTop + AndroidUtilities.dp(23.0f)));
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
            canvas.translate((float)this.countLeft, (float)(this.countTop + AndroidUtilities.dp(4.0f)));
            this.countLayout.draw(canvas);
            canvas.restore();
        }
        this.avatarImage.draw(canvas);
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        final StringBuilder sb = new StringBuilder();
        final StaticLayout nameLayout = this.nameLayout;
        if (nameLayout != null) {
            sb.append(nameLayout.getText());
        }
        if (this.statusLayout != null) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(this.statusLayout.getText());
        }
        accessibilityNodeInfo.setText((CharSequence)sb.toString());
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.user == null && this.chat == null && this.encryptedChat == null) {
            return;
        }
        if (b) {
            this.buildLayout();
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(60.0f) + (this.useSeparator ? 1 : 0));
    }
    
    public void setData(final TLObject tlObject, final TLRPC.EncryptedChat encryptedChat, final CharSequence currentName, final CharSequence subLabel, final boolean drawCount, final boolean savedMessages) {
        this.currentName = currentName;
        if (tlObject instanceof TLRPC.User) {
            this.user = (TLRPC.User)tlObject;
            this.chat = null;
        }
        else if (tlObject instanceof TLRPC.Chat) {
            this.chat = (TLRPC.Chat)tlObject;
            this.user = null;
        }
        this.encryptedChat = encryptedChat;
        this.subLabel = subLabel;
        this.drawCount = drawCount;
        this.savedMessages = savedMessages;
        this.update(0);
    }
    
    public void setException(final NotificationsSettingsActivity.NotificationException ex, final CharSequence charSequence) {
        final boolean hasCustom = ex.hasCustom;
        final int notify = ex.notify;
        final int muteUntil = ex.muteUntil;
        boolean b = false;
        String s;
        if (notify == 3 && muteUntil != Integer.MAX_VALUE) {
            final int n = muteUntil - ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            if (n <= 0) {
                if (hasCustom) {
                    s = LocaleController.getString("NotificationsCustom", 2131560059);
                }
                else {
                    s = LocaleController.getString("NotificationsUnmuted", 2131560094);
                }
            }
            else if (n < 3600) {
                s = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Minutes", n / 60));
            }
            else if (n < 86400) {
                s = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Hours", (int)Math.ceil(n / 60.0f / 60.0f)));
            }
            else if (n < 31536000) {
                s = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Days", (int)Math.ceil(n / 60.0f / 60.0f / 24.0f)));
            }
            else {
                s = null;
            }
        }
        else {
            if (notify == 0 || notify == 1) {
                b = true;
            }
            if (b && hasCustom) {
                s = LocaleController.getString("NotificationsCustom", 2131560059);
            }
            else if (b) {
                s = LocaleController.getString("NotificationsUnmuted", 2131560094);
            }
            else {
                s = LocaleController.getString("NotificationsMuted", 2131560076);
            }
        }
        String string = s;
        if (s == null) {
            string = LocaleController.getString("NotificationsOff", 2131560078);
        }
        final long did = ex.did;
        final int i = (int)did;
        final int j = (int)(did >> 32);
        if (i != 0) {
            if (i > 0) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(i);
                if (user != null) {
                    this.setData(user, null, charSequence, string, false, false);
                }
            }
            else {
                final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-i);
                if (chat != null) {
                    this.setData(chat, null, charSequence, string, false, false);
                }
            }
        }
        else {
            final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(j);
            if (encryptedChat != null) {
                final TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(encryptedChat.user_id);
                if (user2 != null) {
                    this.setData(user2, encryptedChat, charSequence, string, false, false);
                }
            }
        }
    }
    
    public void setSublabelOffset(final int sublabelOffsetX, final int sublabelOffsetY) {
        this.sublabelOffsetX = sublabelOffsetX;
        this.sublabelOffsetY = sublabelOffsetY;
    }
    
    public void update(final int n) {
        final TLRPC.User user = this.user;
        TLRPC.FileLocation lastAvatar = null;
        final TLRPC.FileLocation fileLocation = null;
        final TLRPC.FileLocation fileLocation2 = null;
        if (user != null) {
            this.avatarDrawable.setInfo(user);
            if (this.savedMessages) {
                this.avatarDrawable.setAvatarType(1);
                this.avatarImage.setImage(null, null, this.avatarDrawable, null, null, 0);
                lastAvatar = fileLocation;
            }
            else {
                final TLRPC.UserProfilePhoto photo = this.user.photo;
                lastAvatar = fileLocation2;
                if (photo != null) {
                    lastAvatar = photo.photo_small;
                }
                this.avatarImage.setImage(ImageLocation.getForUser(this.user, false), "50_50", this.avatarDrawable, null, this.user, 0);
            }
        }
        else {
            final TLRPC.Chat chat = this.chat;
            if (chat != null) {
                final TLRPC.ChatPhoto photo2 = chat.photo;
                if (photo2 != null) {
                    lastAvatar = photo2.photo_small;
                }
                this.avatarDrawable.setInfo(this.chat);
                this.avatarImage.setImage(ImageLocation.getForChat(this.chat, false), "50_50", this.avatarDrawable, null, this.chat, 0);
            }
            else {
                this.avatarDrawable.setInfo(0, null, null, false);
                this.avatarImage.setImage(null, null, this.avatarDrawable, null, null, 0);
                lastAvatar = fileLocation;
            }
        }
        if (n != 0) {
            int n2 = 0;
            Label_0319: {
                Label_0316: {
                    if (((n & 0x2) != 0x0 && this.user != null) || ((n & 0x8) != 0x0 && this.chat != null)) {
                        if ((this.lastAvatar == null || lastAvatar != null) && (this.lastAvatar != null || lastAvatar == null)) {
                            final TLRPC.FileLocation lastAvatar2 = this.lastAvatar;
                            if (lastAvatar2 == null || lastAvatar == null || (lastAvatar2.volume_id == lastAvatar.volume_id && lastAvatar2.local_id == lastAvatar.local_id)) {
                                break Label_0316;
                            }
                        }
                        n2 = 1;
                        break Label_0319;
                    }
                }
                n2 = 0;
            }
            int n3 = n2;
            if (n2 == 0) {
                n3 = n2;
                if ((n & 0x4) != 0x0) {
                    final TLRPC.User user2 = this.user;
                    n3 = n2;
                    if (user2 != null) {
                        final TLRPC.UserStatus status = user2.status;
                        int expires;
                        if (status != null) {
                            expires = status.expires;
                        }
                        else {
                            expires = 0;
                        }
                        n3 = n2;
                        if (expires != this.lastStatus) {
                            n3 = 1;
                        }
                    }
                }
            }
            int n4 = 0;
            Label_0514: {
                if (n3 || (n & 0x1) == 0x0 || this.user == null) {
                    n4 = n3;
                    if ((n & 0x10) == 0x0) {
                        break Label_0514;
                    }
                    n4 = n3;
                    if (this.chat == null) {
                        break Label_0514;
                    }
                }
                String s;
                if (this.user != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.user.first_name);
                    sb.append(this.user.last_name);
                    s = sb.toString();
                }
                else {
                    s = this.chat.title;
                }
                n4 = n3;
                if (!s.equals(this.lastName)) {
                    n4 = 1;
                }
            }
            int n5;
            if ((n5 = n4) == 0) {
                n5 = n4;
                if (this.drawCount) {
                    n5 = n4;
                    if ((n & 0x100) != 0x0) {
                        final TLRPC.Dialog dialog = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
                        n5 = n4;
                        if (dialog != null) {
                            n5 = n4;
                            if (dialog.unread_count != this.lastUnreadCount) {
                                n5 = 1;
                            }
                        }
                    }
                }
            }
            if (n5 == 0) {
                return;
            }
        }
        final TLRPC.User user3 = this.user;
        if (user3 != null) {
            final TLRPC.UserStatus status2 = user3.status;
            if (status2 != null) {
                this.lastStatus = status2.expires;
            }
            else {
                this.lastStatus = 0;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.user.first_name);
            sb2.append(this.user.last_name);
            this.lastName = sb2.toString();
        }
        else {
            final TLRPC.Chat chat2 = this.chat;
            if (chat2 != null) {
                this.lastName = chat2.title;
            }
        }
        this.lastAvatar = lastAvatar;
        if (this.getMeasuredWidth() == 0 && this.getMeasuredHeight() == 0) {
            this.requestLayout();
        }
        else {
            this.buildLayout();
        }
        this.postInvalidate();
    }
}
