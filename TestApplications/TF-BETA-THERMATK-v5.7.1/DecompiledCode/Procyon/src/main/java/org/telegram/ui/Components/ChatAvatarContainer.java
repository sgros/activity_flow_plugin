// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.ChatObject;
import android.text.TextUtils;
import android.view.View$MeasureSpec;
import android.os.Build$VERSION;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.MediaActivity;
import android.os.Bundle;
import android.app.Dialog;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.LocaleController;
import android.view.View$OnClickListener;
import android.graphics.drawable.Drawable;
import android.widget.ImageView$ScaleType;
import org.telegram.ui.ActionBar.Theme;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.widget.ImageView;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ChatActivity;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class ChatAvatarContainer extends FrameLayout implements NotificationCenterDelegate
{
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private int currentAccount;
    private int currentConnectionState;
    private boolean[] isOnline;
    private CharSequence lastSubtitle;
    private boolean occupyStatusBar;
    private int onlineCount;
    private ChatActivity parentFragment;
    private StatusDrawable[] statusDrawables;
    private SimpleTextView subtitleTextView;
    private ImageView timeItem;
    private TimerDrawable timerDrawable;
    private SimpleTextView titleTextView;
    
    public ChatAvatarContainer(final Context context, final ChatActivity parentFragment, final boolean b) {
        super(context);
        this.statusDrawables = new StatusDrawable[5];
        this.avatarDrawable = new AvatarDrawable();
        this.currentAccount = UserConfig.selectedAccount;
        this.occupyStatusBar = true;
        this.isOnline = new boolean[1];
        this.onlineCount = -1;
        this.parentFragment = parentFragment;
        (this.avatarImageView = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(21.0f));
        this.addView((View)this.avatarImageView);
        (this.titleTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("actionBarDefaultTitle"));
        this.titleTextView.setTextSize(18);
        this.titleTextView.setGravity(3);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
        this.addView((View)this.titleTextView);
        (this.subtitleTextView = new SimpleTextView(context)).setTextColor(Theme.getColor("actionBarDefaultSubtitle"));
        this.subtitleTextView.setTag((Object)"actionBarDefaultSubtitle");
        this.subtitleTextView.setTextSize(14);
        this.subtitleTextView.setGravity(3);
        this.addView((View)this.subtitleTextView);
        if (b) {
            (this.timeItem = new ImageView(context)).setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
            this.timeItem.setScaleType(ImageView$ScaleType.CENTER);
            this.timeItem.setImageDrawable((Drawable)(this.timerDrawable = new TimerDrawable(context)));
            this.addView((View)this.timeItem);
            this.timeItem.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatAvatarContainer$AUPtAkLLMEGZaufiE0P7VnJ7Wsk(this));
            this.timeItem.setContentDescription((CharSequence)LocaleController.getString("SetTimer", 2131560737));
        }
        if (this.parentFragment != null) {
            this.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatAvatarContainer$F8krEgvfCBOEDgLZx9CeIOwCevs(this));
            final TLRPC.Chat currentChat = this.parentFragment.getCurrentChat();
            this.statusDrawables[0] = new TypingDotsDrawable();
            this.statusDrawables[1] = new RecordStatusDrawable();
            this.statusDrawables[2] = new SendingFileDrawable();
            this.statusDrawables[3] = new PlayingGameDrawable();
            this.statusDrawables[4] = new RoundStatusDrawable();
            int n = 0;
            while (true) {
                final StatusDrawable[] statusDrawables = this.statusDrawables;
                if (n >= statusDrawables.length) {
                    break;
                }
                statusDrawables[n].setIsChat(currentChat != null);
                ++n;
            }
        }
    }
    
    private void setTypingAnimation(final boolean b) {
        final int n = 0;
        int i = 0;
        if (b) {
            try {
                final Integer n2 = (Integer)MessagesController.getInstance(this.currentAccount).printingStringsTypes.get(this.parentFragment.getDialogId());
                this.subtitleTextView.setLeftDrawable(this.statusDrawables[n2]);
                while (i < this.statusDrawables.length) {
                    if (i == n2) {
                        this.statusDrawables[i].start();
                    }
                    else {
                        this.statusDrawables[i].stop();
                    }
                    ++i;
                }
                return;
            }
            catch (Exception ex) {
                FileLog.e(ex);
                return;
            }
        }
        this.subtitleTextView.setLeftDrawable(null);
        int n3 = n;
        while (true) {
            final StatusDrawable[] statusDrawables = this.statusDrawables;
            if (n3 >= statusDrawables.length) {
                break;
            }
            statusDrawables[n3].stop();
            ++n3;
        }
    }
    
    private void updateCurrentConnectionState() {
        final int currentConnectionState = this.currentConnectionState;
        String text;
        if (currentConnectionState == 2) {
            text = LocaleController.getString("WaitingForNetwork", 2131561102);
        }
        else if (currentConnectionState == 1) {
            text = LocaleController.getString("Connecting", 2131559137);
        }
        else if (currentConnectionState == 5) {
            text = LocaleController.getString("Updating", 2131560962);
        }
        else if (currentConnectionState == 4) {
            text = LocaleController.getString("ConnectingToProxy", 2131559139);
        }
        else {
            text = null;
        }
        if (text == null) {
            final CharSequence lastSubtitle = this.lastSubtitle;
            if (lastSubtitle != null) {
                this.subtitleTextView.setText(lastSubtitle);
                this.lastSubtitle = null;
            }
        }
        else {
            this.lastSubtitle = this.subtitleTextView.getText();
            this.subtitleTextView.setText(text);
        }
    }
    
    public void checkAndUpdateAvatar() {
        final ChatActivity parentFragment = this.parentFragment;
        if (parentFragment == null) {
            return;
        }
        final TLRPC.User currentUser = parentFragment.getCurrentUser();
        final TLRPC.Chat currentChat = this.parentFragment.getCurrentChat();
        if (currentUser != null) {
            this.avatarDrawable.setInfo(currentUser);
            if (UserObject.isUserSelf(currentUser)) {
                this.avatarDrawable.setAvatarType(2);
                final BackupImageView avatarImageView = this.avatarImageView;
                if (avatarImageView != null) {
                    avatarImageView.setImage(null, null, this.avatarDrawable, currentUser);
                }
            }
            else {
                final BackupImageView avatarImageView2 = this.avatarImageView;
                if (avatarImageView2 != null) {
                    avatarImageView2.setImage(ImageLocation.getForUser(currentUser, false), "50_50", this.avatarDrawable, currentUser);
                }
            }
        }
        else if (currentChat != null) {
            this.avatarDrawable.setInfo(currentChat);
            final BackupImageView avatarImageView3 = this.avatarImageView;
            if (avatarImageView3 != null) {
                avatarImageView3.setImage(ImageLocation.getForChat(currentChat, false), "50_50", this.avatarDrawable, currentChat);
            }
        }
    }
    
    public void didReceivedNotification(int connectionState, final int n, final Object... array) {
        if (connectionState == NotificationCenter.didUpdateConnectionState) {
            connectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            if (this.currentConnectionState != connectionState) {
                this.currentConnectionState = connectionState;
                this.updateCurrentConnectionState();
            }
        }
    }
    
    public SimpleTextView getSubtitleTextView() {
        return this.subtitleTextView;
    }
    
    public ImageView getTimeItem() {
        return this.timeItem;
    }
    
    public SimpleTextView getTitleTextView() {
        return this.titleTextView;
    }
    
    public void hideTimeItem() {
        final ImageView timeItem = this.timeItem;
        if (timeItem == null) {
            return;
        }
        timeItem.setVisibility(8);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
            this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            this.updateCurrentConnectionState();
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
        }
    }
    
    protected void onLayout(final boolean b, int statusBarHeight, int n, final int n2, final int n3) {
        n = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(42.0f)) / 2;
        if (Build$VERSION.SDK_INT >= 21 && this.occupyStatusBar) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        statusBarHeight += n;
        this.avatarImageView.layout(AndroidUtilities.dp(8.0f), statusBarHeight, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(42.0f) + statusBarHeight);
        if (this.subtitleTextView.getVisibility() == 0) {
            this.titleTextView.layout(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(1.3f) + statusBarHeight, AndroidUtilities.dp(62.0f) + this.titleTextView.getMeasuredWidth(), this.titleTextView.getTextHeight() + statusBarHeight + AndroidUtilities.dp(1.3f));
        }
        else {
            this.titleTextView.layout(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(11.0f) + statusBarHeight, AndroidUtilities.dp(62.0f) + this.titleTextView.getMeasuredWidth(), this.titleTextView.getTextHeight() + statusBarHeight + AndroidUtilities.dp(11.0f));
        }
        final ImageView timeItem = this.timeItem;
        if (timeItem != null) {
            timeItem.layout(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(15.0f) + statusBarHeight, AndroidUtilities.dp(58.0f), AndroidUtilities.dp(49.0f) + statusBarHeight);
        }
        this.subtitleTextView.layout(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(24.0f) + statusBarHeight, AndroidUtilities.dp(62.0f) + this.subtitleTextView.getMeasuredWidth(), statusBarHeight + this.subtitleTextView.getTextHeight() + AndroidUtilities.dp(24.0f));
    }
    
    protected void onMeasure(int size, final int n) {
        size = View$MeasureSpec.getSize(size);
        final int n2 = size - AndroidUtilities.dp(70.0f);
        this.avatarImageView.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
        this.titleTextView.measure(View$MeasureSpec.makeMeasureSpec(n2, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), Integer.MIN_VALUE));
        this.subtitleTextView.measure(View$MeasureSpec.makeMeasureSpec(n2, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), Integer.MIN_VALUE));
        final ImageView timeItem = this.timeItem;
        if (timeItem != null) {
            timeItem.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0f), 1073741824));
        }
        this.setMeasuredDimension(size, View$MeasureSpec.getSize(n));
    }
    
    public void setChatAvatar(final TLRPC.Chat info) {
        this.avatarDrawable.setInfo(info);
        final BackupImageView avatarImageView = this.avatarImageView;
        if (avatarImageView != null) {
            avatarImageView.setImage(ImageLocation.getForChat(info, false), "50_50", this.avatarDrawable, info);
        }
    }
    
    public void setOccupyStatusBar(final boolean occupyStatusBar) {
        this.occupyStatusBar = occupyStatusBar;
    }
    
    public void setSubtitle(final CharSequence charSequence) {
        if (this.lastSubtitle == null) {
            this.subtitleTextView.setText(charSequence);
        }
        else {
            this.lastSubtitle = charSequence;
        }
    }
    
    public void setTime(final int time) {
        final TimerDrawable timerDrawable = this.timerDrawable;
        if (timerDrawable == null) {
            return;
        }
        timerDrawable.setTime(time);
    }
    
    public void setTitle(final CharSequence charSequence) {
        this.setTitle(charSequence, false);
    }
    
    public void setTitle(final CharSequence text, final boolean b) {
        this.titleTextView.setText(text);
        if (b) {
            if (!(this.titleTextView.getRightDrawable() instanceof ScamDrawable)) {
                final ScamDrawable rightDrawable = new ScamDrawable(11);
                rightDrawable.setColor(Theme.getColor("actionBarDefaultSubtitle"));
                this.titleTextView.setRightDrawable(rightDrawable);
            }
        }
        else if (this.titleTextView.getRightDrawable() instanceof ScamDrawable) {
            this.titleTextView.setRightDrawable(null);
        }
    }
    
    public void setTitleColors(final int textColor, final int n) {
        this.titleTextView.setTextColor(textColor);
        this.subtitleTextView.setTextColor(n);
        this.subtitleTextView.setTag((Object)n);
    }
    
    public void setTitleIcons(final Drawable leftDrawable, final Drawable rightDrawable) {
        this.titleTextView.setLeftDrawable(leftDrawable);
        if (!(this.titleTextView.getRightDrawable() instanceof ScamDrawable)) {
            this.titleTextView.setRightDrawable(rightDrawable);
        }
    }
    
    public void setUserAvatar(final TLRPC.User info) {
        this.avatarDrawable.setInfo(info);
        if (UserObject.isUserSelf(info)) {
            this.avatarDrawable.setAvatarType(2);
            final BackupImageView avatarImageView = this.avatarImageView;
            if (avatarImageView != null) {
                avatarImageView.setImage(null, null, this.avatarDrawable, info);
            }
        }
        else {
            final BackupImageView avatarImageView2 = this.avatarImageView;
            if (avatarImageView2 != null) {
                avatarImageView2.setImage(ImageLocation.getForUser(info, false), "50_50", this.avatarDrawable, info);
            }
        }
    }
    
    public void showTimeItem() {
        final ImageView timeItem = this.timeItem;
        if (timeItem == null) {
            return;
        }
        timeItem.setVisibility(0);
    }
    
    public void updateOnlineCount() {
        final ChatActivity parentFragment = this.parentFragment;
        if (parentFragment == null) {
            return;
        }
        final int n = 0;
        this.onlineCount = 0;
        final TLRPC.ChatFull currentChatInfo = parentFragment.getCurrentChatInfo();
        if (currentChatInfo == null) {
            return;
        }
        final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        int i = n;
        if (!(currentChatInfo instanceof TLRPC.TL_chatFull)) {
            final boolean b = currentChatInfo instanceof TLRPC.TL_channelFull;
            if (b && currentChatInfo.participants_count <= 200 && currentChatInfo.participants != null) {
                i = n;
            }
            else {
                if (b && currentChatInfo.participants_count > 200) {
                    this.onlineCount = currentChatInfo.online_count;
                }
                return;
            }
        }
        while (i < currentChatInfo.participants.participants.size()) {
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(currentChatInfo.participants.participants.get(i).user_id);
            if (user != null) {
                final TLRPC.UserStatus status = user.status;
                if (status != null && (status.expires > currentTime || user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) && user.status.expires > 10000) {
                    ++this.onlineCount;
                }
            }
            ++i;
        }
    }
    
    public void updateSubtitle() {
        final ChatActivity parentFragment = this.parentFragment;
        if (parentFragment == null) {
            return;
        }
        final TLRPC.User currentUser = parentFragment.getCurrentUser();
        if (UserObject.isUserSelf(currentUser)) {
            if (this.subtitleTextView.getVisibility() != 8) {
                this.subtitleTextView.setVisibility(8);
            }
            return;
        }
        final TLRPC.Chat currentChat = this.parentFragment.getCurrentChat();
        final CharSequence charSequence = (CharSequence)MessagesController.getInstance(this.currentAccount).printingStrings.get(this.parentFragment.getDialogId());
        final String s = "";
        boolean b = true;
        CharSequence charSequence2;
        if ((charSequence2 = charSequence) != null) {
            charSequence2 = TextUtils.replace(charSequence, new String[] { "..." }, (CharSequence[])new String[] { "" });
        }
        Label_0747: {
            if (charSequence2 != null && charSequence2.length() != 0 && (!ChatObject.isChannel(currentChat) || currentChat.megagroup)) {
                this.setTypingAnimation(true);
            }
            else {
                this.setTypingAnimation(false);
                Label_0625: {
                    if (currentChat != null) {
                        final TLRPC.ChatFull currentChatInfo = this.parentFragment.getCurrentChatInfo();
                        if (ChatObject.isChannel(currentChat)) {
                            if (currentChatInfo != null) {
                                final int participants_count = currentChatInfo.participants_count;
                                if (participants_count != 0) {
                                    if (currentChat.megagroup) {
                                        if (this.onlineCount > 1) {
                                            charSequence2 = String.format("%s, %s", LocaleController.formatPluralString("Members", participants_count), LocaleController.formatPluralString("OnlineCount", Math.min(this.onlineCount, currentChatInfo.participants_count)));
                                            break Label_0625;
                                        }
                                        charSequence2 = LocaleController.formatPluralString("Members", participants_count);
                                        break Label_0625;
                                    }
                                    else {
                                        final int[] array = { 0 };
                                        final String formatShortNumber = LocaleController.formatShortNumber(participants_count, array);
                                        if (currentChat.megagroup) {
                                            charSequence2 = LocaleController.formatPluralString("Members", array[0]).replace(String.format("%d", array[0]), formatShortNumber);
                                            break Label_0625;
                                        }
                                        charSequence2 = LocaleController.formatPluralString("Subscribers", array[0]).replace(String.format("%d", array[0]), formatShortNumber);
                                        break Label_0625;
                                    }
                                }
                            }
                            if (currentChat.megagroup) {
                                charSequence2 = LocaleController.getString("Loading", 2131559768).toLowerCase();
                            }
                            else if ((currentChat.flags & 0x40) != 0x0) {
                                charSequence2 = LocaleController.getString("ChannelPublic", 2131558991).toLowerCase();
                            }
                            else {
                                charSequence2 = LocaleController.getString("ChannelPrivate", 2131558988).toLowerCase();
                            }
                        }
                        else if (ChatObject.isKickedFromChat(currentChat)) {
                            charSequence2 = LocaleController.getString("YouWereKicked", 2131561141);
                        }
                        else if (ChatObject.isLeftFromChat(currentChat)) {
                            charSequence2 = LocaleController.getString("YouLeft", 2131561140);
                        }
                        else {
                            int n2;
                            final int n = n2 = currentChat.participants_count;
                            if (currentChatInfo != null) {
                                final TLRPC.ChatParticipants participants = currentChatInfo.participants;
                                n2 = n;
                                if (participants != null) {
                                    n2 = participants.participants.size();
                                }
                            }
                            if (this.onlineCount > 1 && n2 != 0) {
                                charSequence2 = String.format("%s, %s", LocaleController.formatPluralString("Members", n2), LocaleController.formatPluralString("OnlineCount", this.onlineCount));
                            }
                            else {
                                charSequence2 = LocaleController.formatPluralString("Members", n2);
                            }
                        }
                    }
                    else {
                        charSequence2 = s;
                        if (currentUser != null) {
                            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(currentUser.id);
                            TLRPC.User user2 = currentUser;
                            if (user != null) {
                                user2 = user;
                            }
                            if (user2.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                charSequence2 = LocaleController.getString("ChatYourSelf", 2131559045);
                            }
                            else {
                                final int id = user2.id;
                                if (id != 333000 && id != 777000 && id != 42777) {
                                    if (MessagesController.isSupportUser(user2)) {
                                        charSequence2 = LocaleController.getString("SupportStatus", 2131560848);
                                    }
                                    else {
                                        if (!user2.bot) {
                                            final boolean[] isOnline = this.isOnline;
                                            isOnline[0] = false;
                                            charSequence2 = LocaleController.formatUserStatus(this.currentAccount, user2, isOnline);
                                            b = this.isOnline[0];
                                            break Label_0747;
                                        }
                                        charSequence2 = LocaleController.getString("Bot", 2131558848);
                                    }
                                }
                                else {
                                    charSequence2 = LocaleController.getString("ServiceNotifications", 2131560724);
                                }
                            }
                        }
                    }
                }
                b = false;
            }
        }
        if (this.lastSubtitle == null) {
            this.subtitleTextView.setText(charSequence2);
            String tag;
            if (b) {
                tag = "chat_status";
            }
            else {
                tag = "actionBarDefaultSubtitle";
            }
            this.subtitleTextView.setTextColor(Theme.getColor(tag));
            this.subtitleTextView.setTag((Object)tag);
        }
        else {
            this.lastSubtitle = charSequence2;
        }
    }
}
