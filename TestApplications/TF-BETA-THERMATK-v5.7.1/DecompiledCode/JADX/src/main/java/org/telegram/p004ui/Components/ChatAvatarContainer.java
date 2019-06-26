package org.telegram.p004ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.SimpleTextView;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ChatActivity;
import org.telegram.p004ui.MediaActivity;
import org.telegram.p004ui.ProfileActivity;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.ChatParticipants;
import org.telegram.tgnet.TLRPC.TL_channelFull;
import org.telegram.tgnet.TLRPC.TL_chatFull;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.UserStatus;

/* renamed from: org.telegram.ui.Components.ChatAvatarContainer */
public class ChatAvatarContainer extends FrameLayout implements NotificationCenterDelegate {
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private BackupImageView avatarImageView;
    private int currentAccount = UserConfig.selectedAccount;
    private int currentConnectionState;
    private boolean[] isOnline = new boolean[1];
    private CharSequence lastSubtitle;
    private boolean occupyStatusBar = true;
    private int onlineCount = -1;
    private ChatActivity parentFragment;
    private StatusDrawable[] statusDrawables = new StatusDrawable[5];
    private SimpleTextView subtitleTextView;
    private ImageView timeItem;
    private TimerDrawable timerDrawable;
    private SimpleTextView titleTextView;

    public ChatAvatarContainer(Context context, ChatActivity chatActivity, boolean z) {
        super(context);
        this.parentFragment = chatActivity;
        this.avatarImageView = new BackupImageView(context);
        this.avatarImageView.setRoundRadius(AndroidUtilities.m26dp(21.0f));
        addView(this.avatarImageView);
        this.titleTextView = new SimpleTextView(context);
        this.titleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        this.titleTextView.setTextSize(18);
        this.titleTextView.setGravity(3);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setLeftDrawableTopPadding(-AndroidUtilities.m26dp(1.3f));
        addView(this.titleTextView);
        this.subtitleTextView = new SimpleTextView(context);
        SimpleTextView simpleTextView = this.subtitleTextView;
        String str = Theme.key_actionBarDefaultSubtitle;
        simpleTextView.setTextColor(Theme.getColor(str));
        this.subtitleTextView.setTag(str);
        this.subtitleTextView.setTextSize(14);
        this.subtitleTextView.setGravity(3);
        addView(this.subtitleTextView);
        if (z) {
            this.timeItem = new ImageView(context);
            this.timeItem.setPadding(AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(5.0f), AndroidUtilities.m26dp(5.0f));
            this.timeItem.setScaleType(ScaleType.CENTER);
            ImageView imageView = this.timeItem;
            TimerDrawable timerDrawable = new TimerDrawable(context);
            this.timerDrawable = timerDrawable;
            imageView.setImageDrawable(timerDrawable);
            addView(this.timeItem);
            this.timeItem.setOnClickListener(new C2547-$$Lambda$ChatAvatarContainer$AUPtAkLLMEGZaufiE0P7VnJ7Wsk(this));
            this.timeItem.setContentDescription(LocaleController.getString("SetTimer", C1067R.string.SetTimer));
        }
        if (this.parentFragment != null) {
            setOnClickListener(new C2548-$$Lambda$ChatAvatarContainer$F8krEgvfCBOEDgLZx9CeIOwCevs(this));
            Chat currentChat = this.parentFragment.getCurrentChat();
            this.statusDrawables[0] = new TypingDotsDrawable();
            this.statusDrawables[1] = new RecordStatusDrawable();
            this.statusDrawables[2] = new SendingFileDrawable();
            this.statusDrawables[3] = new PlayingGameDrawable();
            this.statusDrawables[4] = new RoundStatusDrawable();
            int i = 0;
            while (true) {
                StatusDrawable[] statusDrawableArr = this.statusDrawables;
                if (i < statusDrawableArr.length) {
                    statusDrawableArr[i].setIsChat(currentChat != null);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public /* synthetic */ void lambda$new$0$ChatAvatarContainer(View view) {
        this.parentFragment.showDialog(AlertsCreator.createTTLAlert(getContext(), this.parentFragment.getCurrentEncryptedChat()).create());
    }

    public /* synthetic */ void lambda$new$1$ChatAvatarContainer(View view) {
        User currentUser = this.parentFragment.getCurrentUser();
        Chat currentChat = this.parentFragment.getCurrentChat();
        if (currentUser != null) {
            Bundle bundle = new Bundle();
            String str = "dialog_id";
            if (UserObject.isUserSelf(currentUser)) {
                bundle.putLong(str, this.parentFragment.getDialogId());
                MediaActivity mediaActivity = new MediaActivity(bundle, new int[]{-1, -1, -1, -1, -1});
                mediaActivity.setChatInfo(this.parentFragment.getCurrentChatInfo());
                this.parentFragment.presentFragment(mediaActivity);
                return;
            }
            bundle.putInt("user_id", currentUser.f534id);
            if (this.timeItem != null) {
                bundle.putLong(str, this.parentFragment.getDialogId());
            }
            ProfileActivity profileActivity = new ProfileActivity(bundle);
            profileActivity.setUserInfo(this.parentFragment.getCurrentUserInfo());
            profileActivity.setPlayProfileAnimation(true);
            this.parentFragment.presentFragment(profileActivity);
        } else if (currentChat != null) {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("chat_id", currentChat.f434id);
            ProfileActivity profileActivity2 = new ProfileActivity(bundle2);
            profileActivity2.setChatInfo(this.parentFragment.getCurrentChatInfo());
            profileActivity2.setPlayProfileAnimation(true);
            this.parentFragment.presentFragment(profileActivity2);
        }
    }

    public void setOccupyStatusBar(boolean z) {
        this.occupyStatusBar = z;
    }

    public void setTitleColors(int i, int i2) {
        this.titleTextView.setTextColor(i);
        this.subtitleTextView.setTextColor(i2);
        this.subtitleTextView.setTag(Integer.valueOf(i2));
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        i = MeasureSpec.getSize(i);
        int dp = i - AndroidUtilities.m26dp(70.0f);
        this.avatarImageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(42.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(42.0f), 1073741824));
        this.titleTextView.measure(MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(24.0f), Integer.MIN_VALUE));
        this.subtitleTextView.measure(MeasureSpec.makeMeasureSpec(dp, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(20.0f), Integer.MIN_VALUE));
        ImageView imageView = this.timeItem;
        if (imageView != null) {
            imageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(34.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(34.0f), 1073741824));
        }
        setMeasuredDimension(i, MeasureSpec.getSize(i2));
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int currentActionBarHeight = (C2190ActionBar.getCurrentActionBarHeight() - AndroidUtilities.m26dp(42.0f)) / 2;
        i2 = (VERSION.SDK_INT < 21 || !this.occupyStatusBar) ? 0 : AndroidUtilities.statusBarHeight;
        currentActionBarHeight += i2;
        this.avatarImageView.layout(AndroidUtilities.m26dp(8.0f), currentActionBarHeight, AndroidUtilities.m26dp(50.0f), AndroidUtilities.m26dp(42.0f) + currentActionBarHeight);
        if (this.subtitleTextView.getVisibility() == 0) {
            this.titleTextView.layout(AndroidUtilities.m26dp(62.0f), AndroidUtilities.m26dp(1.3f) + currentActionBarHeight, AndroidUtilities.m26dp(62.0f) + this.titleTextView.getMeasuredWidth(), (this.titleTextView.getTextHeight() + currentActionBarHeight) + AndroidUtilities.m26dp(1.3f));
        } else {
            this.titleTextView.layout(AndroidUtilities.m26dp(62.0f), AndroidUtilities.m26dp(11.0f) + currentActionBarHeight, AndroidUtilities.m26dp(62.0f) + this.titleTextView.getMeasuredWidth(), (this.titleTextView.getTextHeight() + currentActionBarHeight) + AndroidUtilities.m26dp(11.0f));
        }
        ImageView imageView = this.timeItem;
        if (imageView != null) {
            imageView.layout(AndroidUtilities.m26dp(24.0f), AndroidUtilities.m26dp(15.0f) + currentActionBarHeight, AndroidUtilities.m26dp(58.0f), AndroidUtilities.m26dp(49.0f) + currentActionBarHeight);
        }
        this.subtitleTextView.layout(AndroidUtilities.m26dp(62.0f), AndroidUtilities.m26dp(24.0f) + currentActionBarHeight, AndroidUtilities.m26dp(62.0f) + this.subtitleTextView.getMeasuredWidth(), (currentActionBarHeight + this.subtitleTextView.getTextHeight()) + AndroidUtilities.m26dp(24.0f));
    }

    public void showTimeItem() {
        ImageView imageView = this.timeItem;
        if (imageView != null) {
            imageView.setVisibility(0);
        }
    }

    public void hideTimeItem() {
        ImageView imageView = this.timeItem;
        if (imageView != null) {
            imageView.setVisibility(8);
        }
    }

    public void setTime(int i) {
        TimerDrawable timerDrawable = this.timerDrawable;
        if (timerDrawable != null) {
            timerDrawable.setTime(i);
        }
    }

    public void setTitleIcons(Drawable drawable, Drawable drawable2) {
        this.titleTextView.setLeftDrawable(drawable);
        if (!(this.titleTextView.getRightDrawable() instanceof ScamDrawable)) {
            this.titleTextView.setRightDrawable(drawable2);
        }
    }

    public void setTitle(CharSequence charSequence) {
        setTitle(charSequence, false);
    }

    public void setTitle(CharSequence charSequence, boolean z) {
        this.titleTextView.setText(charSequence);
        if (z) {
            if (!(this.titleTextView.getRightDrawable() instanceof ScamDrawable)) {
                Drawable scamDrawable = new ScamDrawable(11);
                scamDrawable.setColor(Theme.getColor(Theme.key_actionBarDefaultSubtitle));
                this.titleTextView.setRightDrawable(scamDrawable);
            }
        } else if (this.titleTextView.getRightDrawable() instanceof ScamDrawable) {
            this.titleTextView.setRightDrawable(null);
        }
    }

    public void setSubtitle(CharSequence charSequence) {
        if (this.lastSubtitle == null) {
            this.subtitleTextView.setText(charSequence);
        } else {
            this.lastSubtitle = charSequence;
        }
    }

    public ImageView getTimeItem() {
        return this.timeItem;
    }

    public SimpleTextView getTitleTextView() {
        return this.titleTextView;
    }

    public SimpleTextView getSubtitleTextView() {
        return this.subtitleTextView;
    }

    private void setTypingAnimation(boolean z) {
        int i = 0;
        if (z) {
            try {
                Integer num = (Integer) MessagesController.getInstance(this.currentAccount).printingStringsTypes.get(this.parentFragment.getDialogId());
                this.subtitleTextView.setLeftDrawable(this.statusDrawables[num.intValue()]);
                while (i < this.statusDrawables.length) {
                    if (i == num.intValue()) {
                        this.statusDrawables[i].start();
                    } else {
                        this.statusDrawables[i].stop();
                    }
                    i++;
                }
                return;
            } catch (Exception e) {
                FileLog.m30e(e);
                return;
            }
        }
        this.subtitleTextView.setLeftDrawable(null);
        while (true) {
            StatusDrawable[] statusDrawableArr = this.statusDrawables;
            if (i < statusDrawableArr.length) {
                statusDrawableArr[i].stop();
                i++;
            } else {
                return;
            }
        }
    }

    public void updateSubtitle() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            User currentUser = chatActivity.getCurrentUser();
            if (UserObject.isUserSelf(currentUser)) {
                if (this.subtitleTextView.getVisibility() != 8) {
                    this.subtitleTextView.setVisibility(8);
                }
                return;
            }
            String replace;
            Chat currentChat = this.parentFragment.getCurrentChat();
            CharSequence charSequence = (CharSequence) MessagesController.getInstance(this.currentAccount).printingStrings.get(this.parentFragment.getDialogId());
            CharSequence charSequence2 = "";
            int i = 1;
            if (charSequence != null) {
                charSequence = TextUtils.replace(charSequence, new String[]{"..."}, new String[]{charSequence2});
            }
            if (charSequence == null || charSequence.length() == 0 || (ChatObject.isChannel(currentChat) && !currentChat.megagroup)) {
                setTypingAnimation(false);
                int i2;
                if (currentChat != null) {
                    ChatFull currentChatInfo = this.parentFragment.getCurrentChatInfo();
                    String str = "OnlineCount";
                    String str2 = "%s, %s";
                    String str3 = "Members";
                    if (ChatObject.isChannel(currentChat)) {
                        if (currentChatInfo != null) {
                            int i3 = currentChatInfo.participants_count;
                            if (i3 != 0) {
                                if (!currentChat.megagroup) {
                                    int[] iArr = new int[1];
                                    String formatShortNumber = LocaleController.formatShortNumber(i3, iArr);
                                    str = "%d";
                                    if (currentChat.megagroup) {
                                        replace = LocaleController.formatPluralString(str3, iArr[0]).replace(String.format(str, new Object[]{Integer.valueOf(iArr[0])}), formatShortNumber);
                                    } else {
                                        replace = LocaleController.formatPluralString("Subscribers", iArr[0]).replace(String.format(str, new Object[]{Integer.valueOf(iArr[0])}), formatShortNumber);
                                    }
                                } else if (this.onlineCount > 1) {
                                    replace = String.format(str2, new Object[]{LocaleController.formatPluralString(str3, i3), LocaleController.formatPluralString(str, Math.min(this.onlineCount, currentChatInfo.participants_count))});
                                } else {
                                    replace = LocaleController.formatPluralString(str3, i3);
                                }
                            }
                        }
                        if (currentChat.megagroup) {
                            replace = LocaleController.getString("Loading", C1067R.string.Loading).toLowerCase();
                        } else if ((currentChat.flags & 64) != 0) {
                            replace = LocaleController.getString("ChannelPublic", C1067R.string.ChannelPublic).toLowerCase();
                        } else {
                            replace = LocaleController.getString("ChannelPrivate", C1067R.string.ChannelPrivate).toLowerCase();
                        }
                    } else if (ChatObject.isKickedFromChat(currentChat)) {
                        replace = LocaleController.getString("YouWereKicked", C1067R.string.YouWereKicked);
                    } else if (ChatObject.isLeftFromChat(currentChat)) {
                        replace = LocaleController.getString("YouLeft", C1067R.string.YouLeft);
                    } else {
                        i2 = currentChat.participants_count;
                        if (currentChatInfo != null) {
                            ChatParticipants chatParticipants = currentChatInfo.participants;
                            if (chatParticipants != null) {
                                i2 = chatParticipants.participants.size();
                            }
                        }
                        if (this.onlineCount <= 1 || i2 == 0) {
                            replace = LocaleController.formatPluralString(str3, i2);
                        } else {
                            replace = String.format(str2, new Object[]{LocaleController.formatPluralString(str3, i2), LocaleController.formatPluralString(str, this.onlineCount)});
                        }
                    }
                } else {
                    if (currentUser != null) {
                        User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(currentUser.f534id));
                        if (user != null) {
                            currentUser = user;
                        }
                        if (currentUser.f534id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            replace = LocaleController.getString("ChatYourSelf", C1067R.string.ChatYourSelf);
                        } else {
                            i2 = currentUser.f534id;
                            if (i2 == 333000 || i2 == 777000 || i2 == 42777) {
                                replace = LocaleController.getString("ServiceNotifications", C1067R.string.ServiceNotifications);
                            } else if (MessagesController.isSupportUser(currentUser)) {
                                replace = LocaleController.getString("SupportStatus", C1067R.string.SupportStatus);
                            } else if (currentUser.bot) {
                                replace = LocaleController.getString("Bot", C1067R.string.Bot);
                            } else {
                                boolean[] zArr = this.isOnline;
                                zArr[0] = false;
                                charSequence2 = LocaleController.formatUserStatus(this.currentAccount, currentUser, zArr);
                                i = this.isOnline[0];
                            }
                        }
                    }
                    i = 0;
                }
                charSequence2 = replace;
                i = 0;
            } else {
                setTypingAnimation(true);
                charSequence2 = charSequence;
            }
            if (this.lastSubtitle == null) {
                this.subtitleTextView.setText(charSequence2);
                replace = i != 0 ? Theme.key_chat_status : Theme.key_actionBarDefaultSubtitle;
                this.subtitleTextView.setTextColor(Theme.getColor(replace));
                this.subtitleTextView.setTag(replace);
            } else {
                this.lastSubtitle = charSequence2;
            }
        }
    }

    public void setChatAvatar(Chat chat) {
        this.avatarDrawable.setInfo(chat);
        BackupImageView backupImageView = this.avatarImageView;
        if (backupImageView != null) {
            backupImageView.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, (Object) chat);
        }
    }

    public void setUserAvatar(User user) {
        this.avatarDrawable.setInfo(user);
        BackupImageView backupImageView;
        if (UserObject.isUserSelf(user)) {
            this.avatarDrawable.setAvatarType(2);
            backupImageView = this.avatarImageView;
            if (backupImageView != null) {
                backupImageView.setImage(null, null, this.avatarDrawable, (Object) user);
                return;
            }
            return;
        }
        backupImageView = this.avatarImageView;
        if (backupImageView != null) {
            backupImageView.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, (Object) user);
        }
    }

    public void checkAndUpdateAvatar() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            Object currentUser = chatActivity.getCurrentUser();
            Object currentChat = this.parentFragment.getCurrentChat();
            String str = "50_50";
            if (currentUser != null) {
                this.avatarDrawable.setInfo((User) currentUser);
                BackupImageView backupImageView;
                if (UserObject.isUserSelf(currentUser)) {
                    this.avatarDrawable.setAvatarType(2);
                    backupImageView = this.avatarImageView;
                    if (backupImageView != null) {
                        backupImageView.setImage(null, null, this.avatarDrawable, currentUser);
                    }
                } else {
                    backupImageView = this.avatarImageView;
                    if (backupImageView != null) {
                        backupImageView.setImage(ImageLocation.getForUser(currentUser, false), str, this.avatarDrawable, currentUser);
                    }
                }
            } else if (currentChat != null) {
                this.avatarDrawable.setInfo((Chat) currentChat);
                BackupImageView backupImageView2 = this.avatarImageView;
                if (backupImageView2 != null) {
                    backupImageView2.setImage(ImageLocation.getForChat(currentChat, false), str, this.avatarDrawable, currentChat);
                }
            }
        }
    }

    public void updateOnlineCount() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            int i = 0;
            this.onlineCount = 0;
            ChatFull currentChatInfo = chatActivity.getCurrentChatInfo();
            if (currentChatInfo != null) {
                int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                if (!(currentChatInfo instanceof TL_chatFull)) {
                    boolean z = currentChatInfo instanceof TL_channelFull;
                    if (!z || currentChatInfo.participants_count > Callback.DEFAULT_DRAG_ANIMATION_DURATION || currentChatInfo.participants == null) {
                        if (z && currentChatInfo.participants_count > Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                            this.onlineCount = currentChatInfo.online_count;
                        }
                    }
                }
                while (i < currentChatInfo.participants.participants.size()) {
                    User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((ChatParticipant) currentChatInfo.participants.participants.get(i)).user_id));
                    if (user != null) {
                        UserStatus userStatus = user.status;
                        if (userStatus != null && ((userStatus.expires > currentTime || user.f534id == UserConfig.getInstance(this.currentAccount).getClientUserId()) && user.status.expires > 10000)) {
                            this.onlineCount++;
                        }
                    }
                    i++;
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
            this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            updateCurrentConnectionState();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didUpdateConnectionState) {
            i = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            if (this.currentConnectionState != i) {
                this.currentConnectionState = i;
                updateCurrentConnectionState();
            }
        }
    }

    private void updateCurrentConnectionState() {
        int i = this.currentConnectionState;
        CharSequence string = i == 2 ? LocaleController.getString("WaitingForNetwork", C1067R.string.WaitingForNetwork) : i == 1 ? LocaleController.getString("Connecting", C1067R.string.Connecting) : i == 5 ? LocaleController.getString("Updating", C1067R.string.Updating) : i == 4 ? LocaleController.getString("ConnectingToProxy", C1067R.string.ConnectingToProxy) : null;
        if (string == null) {
            string = this.lastSubtitle;
            if (string != null) {
                this.subtitleTextView.setText(string);
                this.lastSubtitle = null;
                return;
            }
            return;
        }
        this.lastSubtitle = this.subtitleTextView.getText();
        this.subtitleTextView.setText(string);
    }
}