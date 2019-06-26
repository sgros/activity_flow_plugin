// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.text.TextPaint;
import org.telegram.messenger.FileLog;
import android.text.Layout$Alignment;
import android.text.TextUtils$TruncateAt;
import android.text.TextUtils;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.UserObject;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.AndroidUtilities;
import android.view.View;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.text.StaticLayout;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Components.AvatarDrawable;

public class DialogMeUrlCell extends BaseCell
{
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage;
    private int avatarTop;
    private int currentAccount;
    private boolean drawNameBot;
    private boolean drawNameBroadcast;
    private boolean drawNameGroup;
    private boolean drawNameLock;
    private boolean drawVerified;
    private boolean isSelected;
    private StaticLayout messageLayout;
    private int messageLeft;
    private int messageTop;
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
    private TLRPC.RecentMeUrl recentMeUrl;
    public boolean useSeparator;
    
    public DialogMeUrlCell(final Context context) {
        super(context);
        this.avatarImage = new ImageReceiver((View)this);
        this.avatarDrawable = new AvatarDrawable();
        this.messageTop = AndroidUtilities.dp(40.0f);
        this.avatarTop = AndroidUtilities.dp(10.0f);
        this.currentAccount = UserConfig.selectedAccount;
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(26.0f));
    }
    
    public void buildLayout() {
        final TextPaint dialogs_namePaint = Theme.dialogs_namePaint;
        final TextPaint dialogs_messagePaint = Theme.dialogs_messagePaint;
        this.drawNameGroup = false;
        this.drawNameBroadcast = false;
        this.drawNameLock = false;
        this.drawNameBot = false;
        this.drawVerified = false;
        final TLRPC.RecentMeUrl recentMeUrl = this.recentMeUrl;
        String s;
        if (recentMeUrl instanceof TLRPC.TL_recentMeUrlChat) {
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(this.recentMeUrl.chat_id);
            if (chat.id >= 0 && (!ChatObject.isChannel(chat) || chat.megagroup)) {
                this.drawNameGroup = true;
                this.nameLockTop = AndroidUtilities.dp(17.5f);
            }
            else {
                this.drawNameBroadcast = true;
                this.nameLockTop = AndroidUtilities.dp(16.5f);
            }
            this.drawVerified = chat.verified;
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
                final int dp2 = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                Drawable drawable2;
                if (this.drawNameGroup) {
                    drawable2 = Theme.dialogs_groupDrawable;
                }
                else {
                    drawable2 = Theme.dialogs_broadcastDrawable;
                }
                this.nameLockLeft = measuredWidth - dp2 - drawable2.getIntrinsicWidth();
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            s = chat.title;
            this.avatarDrawable.setInfo(chat);
            this.avatarImage.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, null, this.recentMeUrl, 0);
        }
        else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlUser) {
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(this.recentMeUrl.user_id);
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            }
            else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            if (user != null) {
                if (user.bot) {
                    this.drawNameBot = true;
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                        this.nameLeft = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4)) + Theme.dialogs_botDrawable.getIntrinsicWidth();
                    }
                    else {
                        this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(14.0f);
                    }
                }
                this.drawVerified = user.verified;
            }
            s = UserObject.getUserName(user);
            this.avatarDrawable.setInfo(user);
            this.avatarImage.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, null, this.recentMeUrl, 0);
        }
        else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlStickerSet) {
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            }
            else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            s = this.recentMeUrl.set.set.title;
            this.avatarDrawable.setInfo(5, s, null, false);
            this.avatarImage.setImage(ImageLocation.getForDocument(this.recentMeUrl.set.cover), null, this.avatarDrawable, null, this.recentMeUrl, 0);
        }
        else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlChatInvite) {
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            }
            else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            final TLRPC.ChatInvite chat_invite = this.recentMeUrl.chat_invite;
            final TLRPC.Chat chat2 = chat_invite.chat;
            if (chat2 != null) {
                this.avatarDrawable.setInfo(chat2);
                final TLRPC.Chat chat3 = this.recentMeUrl.chat_invite.chat;
                s = chat3.title;
                if (chat3.id >= 0 && (!ChatObject.isChannel(chat3) || this.recentMeUrl.chat_invite.chat.megagroup)) {
                    this.drawNameGroup = true;
                    this.nameLockTop = AndroidUtilities.dp(17.5f);
                }
                else {
                    this.drawNameBroadcast = true;
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                }
                final TLRPC.Chat chat4 = this.recentMeUrl.chat_invite.chat;
                this.drawVerified = chat4.verified;
                this.avatarImage.setImage(ImageLocation.getForChat(chat4, false), "50_50", this.avatarDrawable, null, this.recentMeUrl, 0);
            }
            else {
                s = chat_invite.title;
                this.avatarDrawable.setInfo(5, s, null, false);
                final TLRPC.ChatInvite chat_invite2 = this.recentMeUrl.chat_invite;
                if (!chat_invite2.broadcast && !chat_invite2.channel) {
                    this.drawNameGroup = true;
                    this.nameLockTop = AndroidUtilities.dp(17.5f);
                }
                else {
                    this.drawNameBroadcast = true;
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                }
                this.avatarImage.setImage(ImageLocation.getForPhoto(FileLoader.getClosestPhotoSizeWithSize(this.recentMeUrl.chat_invite.photo.sizes, 50), this.recentMeUrl.chat_invite.photo), "50_50", this.avatarDrawable, null, this.recentMeUrl, 0);
            }
            if (!LocaleController.isRTL) {
                this.nameLockLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                final int dp3 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 4));
                Drawable drawable3;
                if (this.drawNameGroup) {
                    drawable3 = Theme.dialogs_groupDrawable;
                }
                else {
                    drawable3 = Theme.dialogs_broadcastDrawable;
                }
                this.nameLeft = dp3 + drawable3.getIntrinsicWidth();
            }
            else {
                final int measuredWidth2 = this.getMeasuredWidth();
                final int dp4 = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
                Drawable drawable4;
                if (this.drawNameGroup) {
                    drawable4 = Theme.dialogs_groupDrawable;
                }
                else {
                    drawable4 = Theme.dialogs_broadcastDrawable;
                }
                this.nameLockLeft = measuredWidth2 - dp4 - drawable4.getIntrinsicWidth();
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
        }
        else if (recentMeUrl instanceof TLRPC.TL_recentMeUrlUnknown) {
            if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            }
            else {
                this.nameLeft = AndroidUtilities.dp(14.0f);
            }
            this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.recentMeUrl, 0);
            s = "Url";
        }
        else {
            this.avatarImage.setImage(null, null, this.avatarDrawable, null, recentMeUrl, 0);
            s = "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
        sb.append("/");
        sb.append(this.recentMeUrl.url);
        final String string = sb.toString();
        String string2 = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            string2 = LocaleController.getString("HiddenName", 2131559636);
        }
        int n;
        int n2;
        if (!LocaleController.isRTL) {
            n = this.getMeasuredWidth() - this.nameLeft;
            n2 = AndroidUtilities.dp(14.0f);
        }
        else {
            n = this.getMeasuredWidth() - this.nameLeft;
            n2 = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
        }
        final int n3 = n - n2;
        int n6 = 0;
        Label_1377: {
            int n4;
            int n5;
            if (this.drawNameLock) {
                n4 = AndroidUtilities.dp(4.0f);
                n5 = Theme.dialogs_lockDrawable.getIntrinsicWidth();
            }
            else if (this.drawNameGroup) {
                n4 = AndroidUtilities.dp(4.0f);
                n5 = Theme.dialogs_groupDrawable.getIntrinsicWidth();
            }
            else if (this.drawNameBroadcast) {
                n4 = AndroidUtilities.dp(4.0f);
                n5 = Theme.dialogs_broadcastDrawable.getIntrinsicWidth();
            }
            else {
                n6 = n3;
                if (!this.drawNameBot) {
                    break Label_1377;
                }
                n4 = AndroidUtilities.dp(4.0f);
                n5 = Theme.dialogs_botDrawable.getIntrinsicWidth();
            }
            n6 = n3 - (n4 + n5);
        }
        int b = n6;
        if (this.drawVerified) {
            final int n7 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
            b = n6 - n7;
            if (LocaleController.isRTL) {
                this.nameLeft += n7;
                b = b;
            }
        }
        final int max = Math.max(AndroidUtilities.dp(12.0f), b);
        try {
            this.nameLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)string2.replace('\n', ' '), dialogs_namePaint, (float)(max - AndroidUtilities.dp(12.0f)), TextUtils$TruncateAt.END), dialogs_namePaint, max, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        final int measuredWidth3 = this.getMeasuredWidth();
        final int dp5 = AndroidUtilities.dp((float)(AndroidUtilities.leftBaseline + 16));
        int dp6;
        if (!LocaleController.isRTL) {
            this.messageLeft = AndroidUtilities.dp((float)AndroidUtilities.leftBaseline);
            float n8;
            if (AndroidUtilities.isTablet()) {
                n8 = 13.0f;
            }
            else {
                n8 = 9.0f;
            }
            dp6 = AndroidUtilities.dp(n8);
        }
        else {
            this.messageLeft = AndroidUtilities.dp(16.0f);
            final int measuredWidth4 = this.getMeasuredWidth();
            float n9;
            if (AndroidUtilities.isTablet()) {
                n9 = 65.0f;
            }
            else {
                n9 = 61.0f;
            }
            dp6 = measuredWidth4 - AndroidUtilities.dp(n9);
        }
        this.avatarImage.setImageCoords(dp6, this.avatarTop, AndroidUtilities.dp(52.0f), AndroidUtilities.dp(52.0f));
        final int max2 = Math.max(AndroidUtilities.dp(12.0f), measuredWidth3 - dp5);
        final CharSequence ellipsize = TextUtils.ellipsize((CharSequence)string, dialogs_messagePaint, (float)(max2 - AndroidUtilities.dp(12.0f)), TextUtils$TruncateAt.END);
        try {
            this.messageLayout = new StaticLayout(ellipsize, dialogs_messagePaint, max2, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        if (LocaleController.isRTL) {
            final StaticLayout nameLayout = this.nameLayout;
            if (nameLayout != null && nameLayout.getLineCount() > 0) {
                final float lineLeft = this.nameLayout.getLineLeft(0);
                final double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                if (this.drawVerified) {
                    final double v = this.nameLeft;
                    final double v2 = max;
                    Double.isNaN(v2);
                    Double.isNaN(v);
                    final double v3 = AndroidUtilities.dp(6.0f);
                    Double.isNaN(v3);
                    final double v4 = Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                    Double.isNaN(v4);
                    this.nameMuteLeft = (int)(v + (v2 - ceil) - v3 - v4);
                }
                if (lineLeft == 0.0f) {
                    final double v5 = max;
                    if (ceil < v5) {
                        final double v6 = this.nameLeft;
                        Double.isNaN(v5);
                        Double.isNaN(v6);
                        this.nameLeft = (int)(v6 + (v5 - ceil));
                    }
                }
            }
            final StaticLayout messageLayout = this.messageLayout;
            if (messageLayout != null && messageLayout.getLineCount() > 0 && this.messageLayout.getLineLeft(0) == 0.0f) {
                final double ceil2 = Math.ceil(this.messageLayout.getLineWidth(0));
                final double v7 = max2;
                if (ceil2 < v7) {
                    final double v8 = this.messageLeft;
                    Double.isNaN(v7);
                    Double.isNaN(v8);
                    this.messageLeft = (int)(v8 + (v7 - ceil2));
                }
            }
        }
        else {
            final StaticLayout nameLayout2 = this.nameLayout;
            if (nameLayout2 != null && nameLayout2.getLineCount() > 0) {
                final float lineRight = this.nameLayout.getLineRight(0);
                if (lineRight == max) {
                    final double ceil3 = Math.ceil(this.nameLayout.getLineWidth(0));
                    final double v9 = max;
                    if (ceil3 < v9) {
                        final double v10 = this.nameLeft;
                        Double.isNaN(v9);
                        Double.isNaN(v10);
                        this.nameLeft = (int)(v10 - (v9 - ceil3));
                    }
                }
                if (this.drawVerified) {
                    this.nameMuteLeft = (int)(this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                }
            }
            final StaticLayout messageLayout2 = this.messageLayout;
            if (messageLayout2 != null && messageLayout2.getLineCount() > 0 && this.messageLayout.getLineRight(0) == max2) {
                final double ceil4 = Math.ceil(this.messageLayout.getLineWidth(0));
                final double v11 = max2;
                if (ceil4 < v11) {
                    final double v12 = this.messageLeft;
                    Double.isNaN(v11);
                    Double.isNaN(v12);
                    this.messageLeft = (int)(v12 - (v11 - ceil4));
                }
            }
        }
    }
    
    @Override
    public boolean hasOverlappingRendering() {
        return false;
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
        if (this.isSelected) {
            canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
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
            canvas.translate((float)this.nameLeft, (float)AndroidUtilities.dp(13.0f));
            this.nameLayout.draw(canvas);
            canvas.restore();
        }
        if (this.messageLayout != null) {
            canvas.save();
            canvas.translate((float)this.messageLeft, (float)this.messageTop);
            try {
                this.messageLayout.draw(canvas);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            canvas.restore();
        }
        if (this.drawVerified) {
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas);
        }
        if (this.useSeparator) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - AndroidUtilities.dp((float)AndroidUtilities.leftBaseline)), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
            else {
                canvas.drawLine((float)AndroidUtilities.dp((float)AndroidUtilities.leftBaseline), (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
        }
        this.avatarImage.draw(canvas);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (b) {
            this.buildLayout();
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(View$MeasureSpec.getSize(n), AndroidUtilities.dp(72.0f) + (this.useSeparator ? 1 : 0));
    }
    
    public void setDialogSelected(final boolean isSelected) {
        if (this.isSelected != isSelected) {
            this.invalidate();
        }
        this.isSelected = isSelected;
    }
    
    public void setRecentMeUrl(final TLRPC.RecentMeUrl recentMeUrl) {
        this.recentMeUrl = recentMeUrl;
        this.requestLayout();
    }
}
