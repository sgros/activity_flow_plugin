// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View$MeasureSpec;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.DividerCell;
import android.view.ViewGroup;
import org.telegram.ui.Components.IdenticonDrawable;
import android.graphics.drawable.ColorDrawable;
import android.content.SharedPreferences;
import androidx.annotation.Keep;
import android.graphics.Color;
import android.util.Property;
import java.util.Collection;
import android.content.res.Configuration;
import android.net.Uri;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.SecretChatHelper;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.NotificationsController;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.AboutLinkCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.UserCell;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.StateListAnimator;
import android.widget.ImageView$ScaleType;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.View$OnClickListener;
import org.telegram.ui.Components.LayoutHelper;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.FrameLayout;
import android.content.DialogInterface$OnCancelListener;
import org.telegram.ui.Components.voip.VoIPHelper;
import android.content.Intent;
import android.widget.TextView;
import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper;
import android.content.DialogInterface;
import org.telegram.ui.ActionBar.BackDrawable;
import android.view.MotionEvent;
import android.text.TextUtils;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLog;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.view.animation.AccelerateInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.Components.AlertsCreator;
import android.graphics.drawable.Drawable;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.view.ViewTreeObserver$OnPreDrawListener;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.view.View;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessageObject;
import android.os.Bundle;
import android.animation.AnimatorSet;
import android.widget.ImageView;
import java.util.ArrayList;
import org.telegram.ui.Components.ScamDrawable;
import android.util.SparseArray;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ProfileActivity extends BaseFragment implements NotificationCenterDelegate, DialogsActivityDelegate
{
    private static final int add_contact = 1;
    private static final int add_member = 18;
    private static final int add_shortcut = 14;
    private static final int block_contact = 2;
    private static final int call_item = 15;
    private static final int delete_contact = 5;
    private static final int edit_channel = 12;
    private static final int edit_contact = 4;
    private static final int invite_to_group = 9;
    private static final int leave_group = 7;
    private static final int search_members = 17;
    private static final int share = 10;
    private static final int share_contact = 3;
    private static final int statistics = 19;
    private int addMemberRow;
    private int administratorsRow;
    private boolean allowProfileAnimation;
    private ActionBarMenuItem animatingItem;
    private float animationProgress;
    private int audioRow;
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImage;
    private int banFromGroup;
    private int blockedUsersRow;
    private TLRPC.BotInfo botInfo;
    private ActionBarMenuItem callItem;
    private int channelInfoRow;
    private TLRPC.ChatFull chatInfo;
    private int chat_id;
    private boolean creatingChat;
    private TLRPC.ChannelParticipant currentChannelParticipant;
    private TLRPC.Chat currentChat;
    private TLRPC.EncryptedChat currentEncryptedChat;
    private long dialog_id;
    private ActionBarMenuItem editItem;
    private int emptyRow;
    private int extraHeight;
    private int filesRow;
    private int groupsInCommonRow;
    private int infoHeaderRow;
    private int infoSectionRow;
    private int initialAnimationExtraHeight;
    private boolean isBot;
    private boolean[] isOnline;
    private int joinRow;
    private int[] lastMediaCount;
    private int lastSectionRow;
    private LinearLayoutManager layoutManager;
    private int leaveChannelRow;
    private int linksRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loadingUsers;
    private MediaActivity mediaActivity;
    private int[] mediaCount;
    private int[] mediaMergeCount;
    private int membersEndRow;
    private int membersHeaderRow;
    private int membersSectionRow;
    private int membersStartRow;
    private long mergeDialogId;
    private SimpleTextView[] nameTextView;
    private int notificationsDividerRow;
    private int notificationsRow;
    private int onlineCount;
    private SimpleTextView[] onlineTextView;
    private boolean openAnimationInProgress;
    private SparseArray<TLRPC.ChatParticipant> participantsMap;
    private int phoneRow;
    private int photosRow;
    private boolean playProfileAnimation;
    private int[] prevMediaCount;
    private PhotoViewer.PhotoViewerProvider provider;
    private boolean recreateMenuAfterAnimation;
    private int rowCount;
    private ScamDrawable scamDrawable;
    private int selectedUser;
    private int settingsKeyRow;
    private int settingsSectionRow;
    private int settingsTimerRow;
    private int sharedHeaderRow;
    private MediaActivity.SharedMediaData[] sharedMediaData;
    private int sharedSectionRow;
    private ArrayList<Integer> sortedUsers;
    private int startSecretChatRow;
    private int subscribersRow;
    private TopView topView;
    private int unblockRow;
    private boolean userBlocked;
    private TLRPC.UserFull userInfo;
    private int userInfoRow;
    private int user_id;
    private int usernameRow;
    private boolean usersEndReached;
    private int voiceRow;
    private ImageView writeButton;
    private AnimatorSet writeButtonAnimation;
    
    public ProfileActivity(final Bundle bundle) {
        super(bundle);
        this.nameTextView = new SimpleTextView[2];
        this.onlineTextView = new SimpleTextView[2];
        this.isOnline = new boolean[1];
        this.mediaCount = new int[] { -1, -1, -1, -1, -1 };
        this.mediaMergeCount = new int[] { -1, -1, -1, -1, -1 };
        this.lastMediaCount = new int[] { -1, -1, -1, -1, -1 };
        this.prevMediaCount = new int[] { -1, -1, -1, -1, -1 };
        this.participantsMap = (SparseArray<TLRPC.ChatParticipant>)new SparseArray();
        this.allowProfileAnimation = true;
        this.onlineCount = -1;
        this.provider = new PhotoViewer.EmptyPhotoViewerProvider() {
            @Override
            public PlaceProviderObject getPlaceForPhoto(final MessageObject messageObject, final TLRPC.FileLocation fileLocation, int statusBarHeight, final boolean b) {
                if (fileLocation == null) {
                    return null;
                }
                TLRPC.FileLocation fileLocation2 = null;
                Label_0126: {
                    if (ProfileActivity.this.user_id != 0) {
                        final TLRPC.User user = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                        if (user != null) {
                            final TLRPC.UserProfilePhoto photo = user.photo;
                            if (photo != null) {
                                fileLocation2 = photo.photo_big;
                                if (fileLocation2 != null) {
                                    break Label_0126;
                                }
                            }
                        }
                    }
                    else if (ProfileActivity.this.chat_id != 0) {
                        final TLRPC.Chat chat = MessagesController.getInstance(ProfileActivity.this.currentAccount).getChat(ProfileActivity.this.chat_id);
                        if (chat != null) {
                            final TLRPC.ChatPhoto photo2 = chat.photo;
                            if (photo2 != null) {
                                fileLocation2 = photo2.photo_big;
                                if (fileLocation2 != null) {
                                    break Label_0126;
                                }
                            }
                        }
                    }
                    fileLocation2 = null;
                }
                if (fileLocation2 != null && fileLocation2.local_id == fileLocation.local_id && fileLocation2.volume_id == fileLocation.volume_id && fileLocation2.dc_id == fileLocation.dc_id) {
                    final int[] array = new int[2];
                    ProfileActivity.this.avatarImage.getLocationInWindow(array);
                    final PlaceProviderObject placeProviderObject = new PhotoViewer.PlaceProviderObject();
                    statusBarHeight = 0;
                    placeProviderObject.viewX = array[0];
                    final int n = array[1];
                    if (Build$VERSION.SDK_INT < 21) {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    placeProviderObject.viewY = n - statusBarHeight;
                    placeProviderObject.parentView = ProfileActivity.this.avatarImage;
                    placeProviderObject.imageReceiver = ProfileActivity.this.avatarImage.getImageReceiver();
                    if (ProfileActivity.this.user_id != 0) {
                        placeProviderObject.dialogId = ProfileActivity.this.user_id;
                    }
                    else if (ProfileActivity.this.chat_id != 0) {
                        placeProviderObject.dialogId = -ProfileActivity.this.chat_id;
                    }
                    placeProviderObject.thumb = placeProviderObject.imageReceiver.getBitmapSafe();
                    placeProviderObject.size = -1;
                    placeProviderObject.radius = ProfileActivity.this.avatarImage.getImageReceiver().getRoundRadius();
                    placeProviderObject.scale = ProfileActivity.this.avatarImage.getScaleX();
                    return placeProviderObject;
                }
                return null;
            }
            
            @Override
            public void willHidePhotoViewer() {
                ProfileActivity.this.avatarImage.getImageReceiver().setVisible(true, true);
            }
        };
    }
    
    private void checkListViewScroll() {
        if (this.listView.getChildCount() > 0) {
            if (!this.openAnimationInProgress) {
                final RecyclerListView listView = this.listView;
                boolean allowProfileAnimation = false;
                final View child = listView.getChildAt(0);
                final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.findContainingViewHolder(child);
                int top = child.getTop();
                if (top < 0 || holder == null || ((RecyclerView.ViewHolder)holder).getAdapterPosition() != 0) {
                    top = 0;
                }
                if (this.extraHeight != top) {
                    this.extraHeight = top;
                    this.topView.invalidate();
                    if (this.playProfileAnimation) {
                        if (this.extraHeight != 0) {
                            allowProfileAnimation = true;
                        }
                        this.allowProfileAnimation = allowProfileAnimation;
                    }
                    this.needLayout();
                }
            }
        }
    }
    
    private void createActionBarMenu() {
        final ActionBarMenu menu = super.actionBar.createMenu();
        menu.clearItems();
        final ActionBarMenuItem actionBarMenuItem = null;
        final ActionBarMenuItem actionBarMenuItem2 = null;
        this.animatingItem = null;
        ActionBarMenuItem actionBarMenuItem3;
        if (this.user_id != 0) {
            if (UserConfig.getInstance(super.currentAccount).getClientUserId() != this.user_id) {
                final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
                if (user == null) {
                    return;
                }
                final TLRPC.UserFull userInfo = this.userInfo;
                if (userInfo != null && userInfo.phone_calls_available) {
                    this.callItem = menu.addItem(15, 2131165429);
                }
                if (!this.isBot && ContactsController.getInstance(super.currentAccount).contactsDict.get(this.user_id) != null) {
                    final ActionBarMenuItem addItem = menu.addItem(10, 2131165416);
                    addItem.addSubItem(3, 2131165670, LocaleController.getString("ShareContact", 2131560747));
                    final boolean userBlocked = this.userBlocked;
                    String s;
                    if (!this.userBlocked) {
                        s = LocaleController.getString("BlockContact", 2131558833);
                    }
                    else {
                        s = LocaleController.getString("Unblock", 2131560932);
                    }
                    addItem.addSubItem(2, 2131165614, s);
                    addItem.addSubItem(4, 2131165625, LocaleController.getString("EditContact", 2131559322));
                    addItem.addSubItem(5, 2131165623, LocaleController.getString("DeleteContact", 2131559241));
                    actionBarMenuItem3 = addItem;
                }
                else {
                    final ActionBarMenuItem addItem2 = menu.addItem(10, 2131165416);
                    if (MessagesController.isSupportUser(user)) {
                        actionBarMenuItem3 = addItem2;
                        if (this.userBlocked) {
                            addItem2.addSubItem(2, 2131165614, LocaleController.getString("Unblock", 2131560932));
                            actionBarMenuItem3 = addItem2;
                        }
                    }
                    else {
                        if (this.isBot) {
                            if (!user.bot_nochats) {
                                addItem2.addSubItem(9, 2131165611, LocaleController.getString("BotInvite", 2131558852));
                            }
                            addItem2.addSubItem(10, 2131165670, LocaleController.getString("BotShare", 2131558856));
                        }
                        final String phone = user.phone;
                        if (phone != null && phone.length() != 0) {
                            addItem2.addSubItem(1, 2131165612, LocaleController.getString("AddContact", 2131558567));
                            addItem2.addSubItem(3, 2131165670, LocaleController.getString("ShareContact", 2131560747));
                            final boolean userBlocked2 = this.userBlocked;
                            String s2;
                            if (!this.userBlocked) {
                                s2 = LocaleController.getString("BlockContact", 2131558833);
                            }
                            else {
                                s2 = LocaleController.getString("Unblock", 2131560932);
                            }
                            addItem2.addSubItem(2, 2131165614, s2);
                            actionBarMenuItem3 = addItem2;
                        }
                        else if (this.isBot) {
                            int n;
                            if (!this.userBlocked) {
                                n = 2131165614;
                            }
                            else {
                                n = 2131165661;
                            }
                            int n2;
                            String s3;
                            if (!this.userBlocked) {
                                n2 = 2131558860;
                                s3 = "BotStop";
                            }
                            else {
                                n2 = 2131558854;
                                s3 = "BotRestart";
                            }
                            addItem2.addSubItem(2, n, LocaleController.getString(s3, n2));
                            actionBarMenuItem3 = addItem2;
                        }
                        else {
                            final boolean userBlocked3 = this.userBlocked;
                            String s4;
                            if (!this.userBlocked) {
                                s4 = LocaleController.getString("BlockContact", 2131558833);
                            }
                            else {
                                s4 = LocaleController.getString("Unblock", 2131560932);
                            }
                            addItem2.addSubItem(2, 2131165614, s4);
                            actionBarMenuItem3 = addItem2;
                        }
                    }
                }
            }
            else {
                actionBarMenuItem3 = menu.addItem(10, 2131165416);
                actionBarMenuItem3.addSubItem(3, 2131165670, LocaleController.getString("ShareContact", 2131560747));
            }
        }
        else {
            final int chat_id = this.chat_id;
            actionBarMenuItem3 = actionBarMenuItem;
            if (chat_id != 0) {
                actionBarMenuItem3 = actionBarMenuItem;
                if (chat_id > 0) {
                    final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
                    if (ChatObject.isChannel(chat)) {
                        if (ChatObject.hasAdminRights(chat) || (chat.megagroup && ChatObject.canChangeChatInfo(chat))) {
                            this.editItem = menu.addItem(12, 2131165404);
                        }
                        ActionBarMenuItem addItem3 = actionBarMenuItem2;
                        if (!chat.megagroup) {
                            final TLRPC.ChatFull chatInfo = this.chatInfo;
                            addItem3 = actionBarMenuItem2;
                            if (chatInfo != null) {
                                addItem3 = actionBarMenuItem2;
                                if (chatInfo.can_view_stats) {
                                    addItem3 = menu.addItem(10, 2131165416);
                                    addItem3.addSubItem(19, 2131165672, LocaleController.getString("Statistics", 2131560806));
                                }
                            }
                        }
                        actionBarMenuItem3 = addItem3;
                        if (chat.megagroup) {
                            ActionBarMenuItem addItem4;
                            if ((addItem4 = addItem3) == null) {
                                addItem4 = menu.addItem(10, 2131165416);
                            }
                            addItem4.addSubItem(17, 2131165669, LocaleController.getString("SearchMembers", 2131560654));
                            actionBarMenuItem3 = addItem4;
                            if (!chat.creator) {
                                actionBarMenuItem3 = addItem4;
                                if (!chat.left) {
                                    actionBarMenuItem3 = addItem4;
                                    if (!chat.kicked) {
                                        addItem4.addSubItem(7, 2131165639, LocaleController.getString("LeaveMegaMenu", 2131559746));
                                        actionBarMenuItem3 = addItem4;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (ChatObject.canChangeChatInfo(chat)) {
                            this.editItem = menu.addItem(12, 2131165404);
                        }
                        actionBarMenuItem3 = menu.addItem(10, 2131165416);
                        actionBarMenuItem3.addSubItem(17, 2131165669, LocaleController.getString("SearchMembers", 2131560654));
                        actionBarMenuItem3.addSubItem(7, 2131165639, LocaleController.getString("DeleteAndExit", 2131559234));
                    }
                }
            }
        }
        ActionBarMenuItem addItem5;
        if ((addItem5 = actionBarMenuItem3) == null) {
            addItem5 = menu.addItem(10, 2131165416);
        }
        addItem5.addSubItem(14, 2131165633, LocaleController.getString("AddShortcut", 2131558583));
        addItem5.setContentDescription((CharSequence)LocaleController.getString("AccDescrMoreOptions", 2131558443));
        final ActionBarMenuItem editItem = this.editItem;
        if (editItem != null) {
            editItem.setContentDescription((CharSequence)LocaleController.getString("Edit", 2131559301));
        }
        final ActionBarMenuItem callItem = this.callItem;
        if (callItem != null) {
            callItem.setContentDescription((CharSequence)LocaleController.getString("Call", 2131558869));
        }
    }
    
    private void fetchUsersFromChannelInfo() {
        final TLRPC.Chat currentChat = this.currentChat;
        if (currentChat != null) {
            if (currentChat.megagroup) {
                final TLRPC.ChatFull chatInfo = this.chatInfo;
                if (chatInfo instanceof TLRPC.TL_channelFull && chatInfo.participants != null) {
                    for (int i = 0; i < this.chatInfo.participants.participants.size(); ++i) {
                        final TLRPC.ChatParticipant chatParticipant = this.chatInfo.participants.participants.get(i);
                        this.participantsMap.put(chatParticipant.user_id, (Object)chatParticipant);
                    }
                }
            }
        }
    }
    
    private void fixLayout() {
        final View fragmentView = super.fragmentView;
        if (fragmentView == null) {
            return;
        }
        fragmentView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
            public boolean onPreDraw() {
                if (ProfileActivity.this.fragmentView != null) {
                    ProfileActivity.this.checkListViewScroll();
                    ProfileActivity.this.needLayout();
                    ProfileActivity.this.fragmentView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                }
                return true;
            }
        });
    }
    
    private void getChannelParticipants(final boolean b) {
        if (!this.loadingUsers) {
            final SparseArray<TLRPC.ChatParticipant> participantsMap = this.participantsMap;
            if (participantsMap != null) {
                if (this.chatInfo != null) {
                    this.loadingUsers = true;
                    final int size = participantsMap.size();
                    int size2 = 0;
                    int n;
                    if (size != 0 && b) {
                        n = 300;
                    }
                    else {
                        n = 0;
                    }
                    final TLRPC.TL_channels_getParticipants tl_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
                    tl_channels_getParticipants.channel = MessagesController.getInstance(super.currentAccount).getInputChannel(this.chat_id);
                    tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsRecent();
                    if (!b) {
                        size2 = this.participantsMap.size();
                    }
                    tl_channels_getParticipants.offset = size2;
                    tl_channels_getParticipants.limit = 200;
                    ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_getParticipants, new _$$Lambda$ProfileActivity$0ctq_HMwIlt8ZVK21U6qfPQkrgk(this, tl_channels_getParticipants, n)), super.classGuid);
                }
            }
        }
    }
    
    private Drawable getScamDrawable() {
        if (this.scamDrawable == null) {
            this.scamDrawable = new ScamDrawable(11);
            final ScamDrawable scamDrawable = this.scamDrawable;
            int chat_id;
            if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
                chat_id = this.chat_id;
            }
            else {
                chat_id = 5;
            }
            scamDrawable.setColor(AvatarDrawable.getProfileTextColorForId(chat_id));
        }
        return this.scamDrawable;
    }
    
    private void kickUser(final int i) {
        if (i != 0) {
            MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chat_id, MessagesController.getInstance(super.currentAccount).getUser(i), this.chatInfo);
        }
        else {
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
            if (AndroidUtilities.isTablet()) {
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, Long.valueOf(-this.chat_id));
            }
            else {
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            }
            MessagesController.getInstance(super.currentAccount).deleteUserFromChat(this.chat_id, MessagesController.getInstance(super.currentAccount).getUser(UserConfig.getInstance(super.currentAccount).getClientUserId()), this.chatInfo);
            this.playProfileAnimation = false;
            this.finishFragment();
        }
    }
    
    private void leaveChatPressed() {
        AlertsCreator.createClearOrDeleteDialogAlert(this, false, this.currentChat, null, false, new _$$Lambda$ProfileActivity$_PR1FK7y_zwmejCbxKdZpGR_CRE(this));
    }
    
    private void loadMediaCounts() {
        if (this.dialog_id != 0L) {
            DataQuery.getInstance(super.currentAccount).getMediaCounts(this.dialog_id, super.classGuid);
        }
        else if (this.user_id != 0) {
            DataQuery.getInstance(super.currentAccount).getMediaCounts(this.user_id, super.classGuid);
        }
        else if (this.chat_id > 0) {
            DataQuery.getInstance(super.currentAccount).getMediaCounts(-this.chat_id, super.classGuid);
            if (this.mergeDialogId != 0L) {
                DataQuery.getInstance(super.currentAccount).getMediaCounts(this.mergeDialogId, super.classGuid);
            }
        }
    }
    
    private void needLayout() {
        int statusBarHeight;
        if (super.actionBar.getOccupyStatusBar()) {
            statusBarHeight = AndroidUtilities.statusBarHeight;
        }
        else {
            statusBarHeight = 0;
        }
        final int topMargin = statusBarHeight + ActionBar.getCurrentActionBarHeight();
        final RecyclerListView listView = this.listView;
        if (listView != null && !this.openAnimationInProgress) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)listView.getLayoutParams();
            if (layoutParams.topMargin != topMargin) {
                layoutParams.topMargin = topMargin;
                this.listView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            }
        }
        if (this.avatarImage != null) {
            final float n = this.extraHeight / (float)AndroidUtilities.dp(88.0f);
            this.listView.setTopGlowOffset(this.extraHeight);
            final ImageView writeButton = this.writeButton;
            if (writeButton != null) {
                int statusBarHeight2;
                if (super.actionBar.getOccupyStatusBar()) {
                    statusBarHeight2 = AndroidUtilities.statusBarHeight;
                }
                else {
                    statusBarHeight2 = 0;
                }
                writeButton.setTranslationY((float)(statusBarHeight2 + ActionBar.getCurrentActionBarHeight() + this.extraHeight - AndroidUtilities.dp(29.5f)));
                if (!this.openAnimationInProgress) {
                    final boolean b = n > 0.2f;
                    if (b != (this.writeButton.getTag() == null)) {
                        if (b) {
                            this.writeButton.setTag((Object)null);
                        }
                        else {
                            this.writeButton.setTag((Object)0);
                        }
                        final AnimatorSet writeButtonAnimation = this.writeButtonAnimation;
                        if (writeButtonAnimation != null) {
                            this.writeButtonAnimation = null;
                            writeButtonAnimation.cancel();
                        }
                        this.writeButtonAnimation = new AnimatorSet();
                        if (b) {
                            this.writeButtonAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                            this.writeButtonAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.SCALE_X, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.SCALE_Y, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.ALPHA, new float[] { 1.0f }) });
                        }
                        else {
                            this.writeButtonAnimation.setInterpolator((TimeInterpolator)new AccelerateInterpolator());
                            this.writeButtonAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.SCALE_X, new float[] { 0.2f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.SCALE_Y, new float[] { 0.2f }), (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.ALPHA, new float[] { 0.0f }) });
                        }
                        this.writeButtonAnimation.setDuration(150L);
                        this.writeButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator obj) {
                                if (ProfileActivity.this.writeButtonAnimation != null && ProfileActivity.this.writeButtonAnimation.equals(obj)) {
                                    ProfileActivity.this.writeButtonAnimation = null;
                                }
                            }
                        });
                        this.writeButtonAnimation.start();
                    }
                }
            }
            int statusBarHeight3;
            if (super.actionBar.getOccupyStatusBar()) {
                statusBarHeight3 = AndroidUtilities.statusBarHeight;
            }
            else {
                statusBarHeight3 = 0;
            }
            final float n2 = (float)statusBarHeight3;
            final float n3 = ActionBar.getCurrentActionBarHeight() / 2.0f;
            final float density = AndroidUtilities.density;
            final BackupImageView avatarImage = this.avatarImage;
            final float n4 = (18.0f * n + 42.0f) / 42.0f;
            avatarImage.setScaleX(n4);
            this.avatarImage.setScaleY(n4);
            this.avatarImage.setTranslationX(-AndroidUtilities.dp(47.0f) * n);
            final BackupImageView avatarImage2 = this.avatarImage;
            final double a = n2 + n3 * (n + 1.0f) - 21.0f * density + density * 27.0f * n;
            avatarImage2.setTranslationY((float)Math.ceil(a));
            for (int i = 0; i < 2; ++i) {
                final SimpleTextView[] nameTextView = this.nameTextView;
                if (nameTextView[i] != null) {
                    nameTextView[i].setTranslationX(AndroidUtilities.density * -21.0f * n);
                    this.nameTextView[i].setTranslationY((float)Math.floor(a) + AndroidUtilities.dp(1.3f) + AndroidUtilities.dp(7.0f) * n);
                    this.onlineTextView[i].setTranslationX(AndroidUtilities.density * -21.0f * n);
                    this.onlineTextView[i].setTranslationY((float)Math.floor(a) + AndroidUtilities.dp(24.0f) + (float)Math.floor(AndroidUtilities.density * 11.0f) * n);
                    final float n5 = 0.12f * n + 1.0f;
                    this.nameTextView[i].setScaleX(n5);
                    this.nameTextView[i].setScaleY(n5);
                    if (i == 1 && !this.openAnimationInProgress) {
                        int n6;
                        if (AndroidUtilities.isTablet()) {
                            n6 = AndroidUtilities.dp(490.0f);
                        }
                        else {
                            n6 = AndroidUtilities.displaySize.x;
                        }
                        int n7;
                        if (this.callItem == null && this.editItem == null) {
                            n7 = 0;
                        }
                        else {
                            n7 = 48;
                        }
                        final int dp = AndroidUtilities.dp((float)(126 + (40 + n7)));
                        final float n8 = (float)n6;
                        final float n9 = (float)dp;
                        float n10;
                        if (n != 1.0f) {
                            n10 = 0.15f * n / (1.0f - n);
                        }
                        else {
                            n10 = 1.0f;
                        }
                        final int n11 = (int)(n8 - n9 * Math.max(0.0f, 1.0f - n10) - this.nameTextView[i].getTranslationX());
                        final float n12 = this.nameTextView[i].getPaint().measureText(this.nameTextView[i].getText().toString()) * n5 + this.nameTextView[i].getSideDrawablesSize();
                        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.nameTextView[i].getLayoutParams();
                        final float n13 = (float)n11;
                        if (n13 < n12) {
                            layoutParams2.width = Math.max(n6 - dp, (int)Math.ceil((n11 - AndroidUtilities.dp(24.0f)) / ((1.12f - n5) * 7.0f + n5)));
                        }
                        else {
                            layoutParams2.width = (int)Math.ceil(n12);
                        }
                        layoutParams2.width = (int)Math.min((n8 - this.nameTextView[i].getX()) / n5 - AndroidUtilities.dp(8.0f), (float)layoutParams2.width);
                        this.nameTextView[i].setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                        final float measureText = this.onlineTextView[i].getPaint().measureText(this.onlineTextView[i].getText().toString());
                        final FrameLayout$LayoutParams layoutParams3 = (FrameLayout$LayoutParams)this.onlineTextView[i].getLayoutParams();
                        layoutParams3.rightMargin = (int)Math.ceil(this.onlineTextView[i].getTranslationX() + AndroidUtilities.dp(8.0f) + AndroidUtilities.dp(40.0f) * (1.0f - n));
                        if (n13 < measureText) {
                            layoutParams3.width = (int)Math.ceil(n11);
                        }
                        else {
                            layoutParams3.width = -2;
                        }
                        this.onlineTextView[i].setLayoutParams((ViewGroup$LayoutParams)layoutParams3);
                    }
                }
            }
        }
    }
    
    private void openAddMember() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean("addToGroup", true);
        bundle.putInt("chatId", this.currentChat.id);
        final GroupCreateActivity groupCreateActivity = new GroupCreateActivity(bundle);
        final TLRPC.ChatFull chatInfo = this.chatInfo;
        if (chatInfo != null && chatInfo.participants != null) {
            final SparseArray ignoreUsers = new SparseArray();
            for (int i = 0; i < this.chatInfo.participants.participants.size(); ++i) {
                ignoreUsers.put(this.chatInfo.participants.participants.get(i).user_id, (Object)null);
            }
            groupCreateActivity.setIgnoreUsers((SparseArray<TLObject>)ignoreUsers);
        }
        groupCreateActivity.setDelegate((GroupCreateActivity.ContactsAddActivityDelegate)new _$$Lambda$ProfileActivity$00s_Q5Ms7AV0v3XsAsD6561decA(this));
        this.presentFragment(groupCreateActivity);
    }
    
    private void openRightsEdit(final int n, final int n2, final TLRPC.ChatParticipant chatParticipant, final TLRPC.TL_chatAdminRights tl_chatAdminRights, final TLRPC.TL_chatBannedRights tl_chatBannedRights) {
        final ChatRightsEditActivity chatRightsEditActivity = new ChatRightsEditActivity(n2, this.chat_id, tl_chatAdminRights, this.currentChat.default_banned_rights, tl_chatBannedRights, n, true, false);
        chatRightsEditActivity.setDelegate((ChatRightsEditActivity.ChatRightsEditActivityDelegate)new _$$Lambda$ProfileActivity$QONFZUnsCsoEQ085dztQiYr_zvw(this, n, chatParticipant));
        this.presentFragment(chatRightsEditActivity);
    }
    
    private boolean processOnClickOrPress(final int n) {
        if (n == this.usernameRow) {
            String s = null;
            Label_0093: {
                if (this.user_id == 0) {
                    if (this.chat_id != 0) {
                        final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
                        if (chat != null) {
                            if ((s = chat.username) != null) {
                                break Label_0093;
                            }
                        }
                    }
                    return false;
                }
                final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
                if (user == null || (s = user.username) == null) {
                    return false;
                }
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            builder.setItems(new CharSequence[] { LocaleController.getString("Copy", 2131559163) }, (DialogInterface$OnClickListener)new _$$Lambda$ProfileActivity$Bn6iwTJ0OB3g1V6Tv9pADNC222E(this, s));
            this.showDialog(builder.create());
            return true;
        }
        if (n == this.phoneRow) {
            final TLRPC.User user2 = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            if (user2 != null) {
                final String phone = user2.phone;
                if (phone != null && phone.length() != 0) {
                    if (this.getParentActivity() != null) {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)this.getParentActivity());
                        final ArrayList<String> list = new ArrayList<String>();
                        final ArrayList<Integer> list2 = new ArrayList<Integer>();
                        final TLRPC.UserFull userInfo = this.userInfo;
                        if (userInfo != null && userInfo.phone_calls_available) {
                            list.add(LocaleController.getString("CallViaTelegram", 2131558886));
                            list2.add(2);
                        }
                        list.add(LocaleController.getString("Call", 2131558869));
                        list2.add(0);
                        list.add(LocaleController.getString("Copy", 2131559163));
                        list2.add(1);
                        builder2.setItems(list.toArray(new CharSequence[0]), (DialogInterface$OnClickListener)new _$$Lambda$ProfileActivity$5IM4wVI_k9G6IDitB8PxuFm_zIw(this, list2, user2));
                        this.showDialog(builder2.create());
                        return true;
                    }
                }
            }
            return false;
        }
        if (n != this.channelInfoRow && n != this.userInfoRow) {
            return false;
        }
        final AlertDialog.Builder builder3 = new AlertDialog.Builder((Context)this.getParentActivity());
        builder3.setItems(new CharSequence[] { LocaleController.getString("Copy", 2131559163) }, (DialogInterface$OnClickListener)new _$$Lambda$ProfileActivity$_RDBSC_Boyp_tFgUU9wFSccPLw8(this, n));
        this.showDialog(builder3.create());
        return true;
    }
    
    private void updateOnlineCount() {
        final int n = 0;
        this.onlineCount = 0;
        final int currentTime = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime();
        this.sortedUsers.clear();
        final TLRPC.ChatFull chatInfo = this.chatInfo;
        int i = n;
        if (!(chatInfo instanceof TLRPC.TL_chatFull)) {
            if (chatInfo instanceof TLRPC.TL_channelFull && chatInfo.participants_count <= 200 && chatInfo.participants != null) {
                i = n;
            }
            else {
                final TLRPC.ChatFull chatInfo2 = this.chatInfo;
                if (chatInfo2 instanceof TLRPC.TL_channelFull && chatInfo2.participants_count > 200) {
                    this.onlineCount = chatInfo2.online_count;
                }
                return;
            }
        }
        while (i < this.chatInfo.participants.participants.size()) {
            final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.chatInfo.participants.participants.get(i).user_id);
            if (user != null) {
                final TLRPC.UserStatus status = user.status;
                if (status != null && (status.expires > currentTime || user.id == UserConfig.getInstance(super.currentAccount).getClientUserId()) && user.status.expires > 10000) {
                    ++this.onlineCount;
                }
            }
            this.sortedUsers.add(i);
            ++i;
        }
        try {
            Collections.sort(this.sortedUsers, new _$$Lambda$ProfileActivity$teGXnC_jUHvrMNZVWwzA3YIyd9o(this, currentTime));
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            final int membersStartRow = this.membersStartRow;
            if (membersStartRow > 0) {
                ((RecyclerView.Adapter)listAdapter).notifyItemRangeChanged(membersStartRow, this.sortedUsers.size());
            }
        }
    }
    
    private void updateProfileData() {
        if (this.avatarImage != null) {
            if (this.nameTextView != null) {
                final int connectionState = ConnectionsManager.getInstance(super.currentAccount).getConnectionState();
                String s;
                if (connectionState == 2) {
                    s = LocaleController.getString("WaitingForNetwork", 2131561102);
                }
                else if (connectionState == 1) {
                    s = LocaleController.getString("Connecting", 2131559137);
                }
                else if (connectionState == 5) {
                    s = LocaleController.getString("Updating", 2131560962);
                }
                else if (connectionState == 4) {
                    s = LocaleController.getString("ConnectingToProxy", 2131559139);
                }
                else {
                    s = null;
                }
                if (this.user_id != 0) {
                    final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
                    final TLRPC.UserProfilePhoto photo = user.photo;
                    TLRPC.FileLocation photo_big;
                    if (photo != null) {
                        photo_big = photo.photo_big;
                    }
                    else {
                        photo_big = null;
                    }
                    this.avatarDrawable.setInfo(user);
                    this.avatarImage.setImage(ImageLocation.getForUser(user, false), "50_50", this.avatarDrawable, user);
                    FileLoader.getInstance(super.currentAccount).loadFile(ImageLocation.getForUser(user, true), user, null, 0, 1);
                    final String userName = UserObject.getUserName(user);
                    String s2;
                    String string;
                    if (user.id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                        s2 = LocaleController.getString("ChatYourSelf", 2131559045);
                        string = LocaleController.getString("ChatYourSelfName", 2131559050);
                    }
                    else {
                        final int id = user.id;
                        if (id != 333000 && id != 777000 && id != 42777) {
                            if (MessagesController.isSupportUser(user)) {
                                s2 = LocaleController.getString("SupportStatus", 2131560848);
                                string = userName;
                            }
                            else if (this.isBot) {
                                s2 = LocaleController.getString("Bot", 2131558848);
                                string = userName;
                            }
                            else {
                                final boolean[] isOnline = this.isOnline;
                                isOnline[0] = false;
                                final String formatUserStatus = LocaleController.formatUserStatus(super.currentAccount, user, isOnline);
                                string = userName;
                                s2 = formatUserStatus;
                                if (this.onlineTextView[1] != null) {
                                    String tag;
                                    if (this.isOnline[0]) {
                                        tag = "profile_status";
                                    }
                                    else {
                                        tag = "avatar_subtitleInProfileBlue";
                                    }
                                    this.onlineTextView[1].setTag((Object)tag);
                                    this.onlineTextView[1].setTextColor(Theme.getColor(tag));
                                    string = userName;
                                    s2 = formatUserStatus;
                                }
                            }
                        }
                        else {
                            s2 = LocaleController.getString("ServiceNotifications", 2131560724);
                            string = userName;
                        }
                    }
                    for (int i = 0; i < 2; ++i) {
                        if (this.nameTextView[i] != null) {
                            Label_0685: {
                                if (i == 0 && user.id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                                    final int id2 = user.id;
                                    if (id2 / 1000 != 777 && id2 / 1000 != 333) {
                                        final String phone = user.phone;
                                        if (phone != null && phone.length() != 0 && ContactsController.getInstance(super.currentAccount).contactsDict.get(user.id) == null && (ContactsController.getInstance(super.currentAccount).contactsDict.size() != 0 || !ContactsController.getInstance(super.currentAccount).isLoadingContacts())) {
                                            final PhoneFormat instance = PhoneFormat.getInstance();
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("+");
                                            sb.append(user.phone);
                                            final String format = instance.format(sb.toString());
                                            if (!this.nameTextView[i].getText().equals(format)) {
                                                this.nameTextView[i].setText(format);
                                            }
                                            break Label_0685;
                                        }
                                    }
                                }
                                if (!this.nameTextView[i].getText().equals(string)) {
                                    this.nameTextView[i].setText(string);
                                }
                            }
                            if (i == 0 && s != null) {
                                this.onlineTextView[i].setText(s);
                            }
                            else if (!this.onlineTextView[i].getText().equals(s2)) {
                                this.onlineTextView[i].setText(s2);
                            }
                            Drawable chat_lockIconDrawable;
                            if (this.currentEncryptedChat != null) {
                                chat_lockIconDrawable = Theme.chat_lockIconDrawable;
                            }
                            else {
                                chat_lockIconDrawable = null;
                            }
                            Drawable rightDrawable = null;
                            Label_0868: {
                                if (i == 0) {
                                    if (user.scam) {
                                        rightDrawable = this.getScamDrawable();
                                        break Label_0868;
                                    }
                                    final MessagesController instance2 = MessagesController.getInstance(super.currentAccount);
                                    long dialog_id = this.dialog_id;
                                    if (dialog_id == 0L) {
                                        dialog_id = this.user_id;
                                    }
                                    if (instance2.isDialogMuted(dialog_id)) {
                                        rightDrawable = Theme.chat_muteIconDrawable;
                                        break Label_0868;
                                    }
                                }
                                else {
                                    if (user.scam) {
                                        rightDrawable = this.getScamDrawable();
                                        break Label_0868;
                                    }
                                    if (user.verified) {
                                        rightDrawable = new CombinedDrawable(Theme.profile_verifiedDrawable, Theme.profile_verifiedCheckDrawable);
                                        break Label_0868;
                                    }
                                }
                                rightDrawable = null;
                            }
                            this.nameTextView[i].setLeftDrawable(chat_lockIconDrawable);
                            this.nameTextView[i].setRightDrawable(rightDrawable);
                        }
                    }
                    this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(photo_big) ^ true, false);
                }
                else if (this.chat_id != 0) {
                    TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
                    if (chat != null) {
                        this.currentChat = chat;
                    }
                    else {
                        chat = this.currentChat;
                    }
                    String s3 = null;
                    Label_1345: {
                        if (ChatObject.isChannel(chat)) {
                            final TLRPC.ChatFull chatInfo = this.chatInfo;
                            Label_1185: {
                                if (chatInfo != null) {
                                    final TLRPC.Chat currentChat = this.currentChat;
                                    if (!currentChat.megagroup) {
                                        if (chatInfo.participants_count == 0 || ChatObject.hasAdminRights(currentChat)) {
                                            break Label_1185;
                                        }
                                        if (this.chatInfo.can_view_participants) {
                                            break Label_1185;
                                        }
                                    }
                                    if (this.currentChat.megagroup) {
                                        if (this.onlineCount > 1) {
                                            final int participants_count = this.chatInfo.participants_count;
                                            if (participants_count != 0) {
                                                s3 = String.format("%s, %s", LocaleController.formatPluralString("Members", participants_count), LocaleController.formatPluralString("OnlineCount", Math.min(this.onlineCount, this.chatInfo.participants_count)));
                                                break Label_1345;
                                            }
                                        }
                                        s3 = LocaleController.formatPluralString("Members", this.chatInfo.participants_count);
                                        break Label_1345;
                                    }
                                    LocaleController.formatShortNumber(this.chatInfo.participants_count, new int[1]);
                                    if (this.currentChat.megagroup) {
                                        s3 = LocaleController.formatPluralString("Members", this.chatInfo.participants_count);
                                        break Label_1345;
                                    }
                                    s3 = LocaleController.formatPluralString("Subscribers", this.chatInfo.participants_count);
                                    break Label_1345;
                                }
                            }
                            if (this.currentChat.megagroup) {
                                s3 = LocaleController.getString("Loading", 2131559768).toLowerCase();
                            }
                            else if ((chat.flags & 0x40) != 0x0) {
                                s3 = LocaleController.getString("ChannelPublic", 2131558991).toLowerCase();
                            }
                            else {
                                s3 = LocaleController.getString("ChannelPrivate", 2131558988).toLowerCase();
                            }
                        }
                        else {
                            int n = chat.participants_count;
                            final TLRPC.ChatFull chatInfo2 = this.chatInfo;
                            if (chatInfo2 != null) {
                                n = chatInfo2.participants.participants.size();
                            }
                            if (n != 0 && this.onlineCount > 1) {
                                s3 = String.format("%s, %s", LocaleController.formatPluralString("Members", n), LocaleController.formatPluralString("OnlineCount", this.onlineCount));
                            }
                            else {
                                s3 = LocaleController.formatPluralString("Members", n);
                            }
                        }
                    }
                    int j = 0;
                    int n2 = 0;
                    while (j < 2) {
                        final SimpleTextView[] nameTextView = this.nameTextView;
                        Label_1907: {
                            if (nameTextView[j] != null) {
                                int n3 = n2;
                                if (chat.title != null) {
                                    n3 = n2;
                                    if (!nameTextView[j].getText().equals(chat.title)) {
                                        n3 = n2;
                                        if (this.nameTextView[j].setText(chat.title)) {
                                            n3 = 1;
                                        }
                                    }
                                }
                                this.nameTextView[j].setLeftDrawable(null);
                                if (j != 0) {
                                    if (chat.scam) {
                                        this.nameTextView[j].setRightDrawable(this.getScamDrawable());
                                    }
                                    else if (chat.verified) {
                                        this.nameTextView[j].setRightDrawable(new CombinedDrawable(Theme.profile_verifiedDrawable, Theme.profile_verifiedCheckDrawable));
                                    }
                                    else {
                                        this.nameTextView[j].setRightDrawable(null);
                                    }
                                }
                                else if (chat.scam) {
                                    this.nameTextView[j].setRightDrawable(this.getScamDrawable());
                                }
                                else {
                                    final SimpleTextView simpleTextView = this.nameTextView[j];
                                    Drawable chat_muteIconDrawable;
                                    if (MessagesController.getInstance(super.currentAccount).isDialogMuted(-this.chat_id)) {
                                        chat_muteIconDrawable = Theme.chat_muteIconDrawable;
                                    }
                                    else {
                                        chat_muteIconDrawable = null;
                                    }
                                    simpleTextView.setRightDrawable(chat_muteIconDrawable);
                                }
                                if (j == 0 && s != null) {
                                    this.onlineTextView[j].setText(s);
                                    n2 = n3;
                                }
                                else if (this.currentChat.megagroup && this.chatInfo != null && this.onlineCount > 0) {
                                    n2 = n3;
                                    if (!this.onlineTextView[j].getText().equals(s3)) {
                                        this.onlineTextView[j].setText(s3);
                                        n2 = n3;
                                    }
                                }
                                else {
                                    if (j == 0 && ChatObject.isChannel(this.currentChat)) {
                                        final TLRPC.ChatFull chatInfo3 = this.chatInfo;
                                        if (chatInfo3 != null && chatInfo3.participants_count != 0) {
                                            final TLRPC.Chat currentChat2 = this.currentChat;
                                            if (currentChat2.megagroup || currentChat2.broadcast) {
                                                final int[] array = { 0 };
                                                final String formatShortNumber = LocaleController.formatShortNumber(this.chatInfo.participants_count, array);
                                                if (this.currentChat.megagroup) {
                                                    this.onlineTextView[j].setText(LocaleController.formatPluralString("Members", array[0]).replace(String.format("%d", array[0]), formatShortNumber));
                                                    n2 = n3;
                                                    break Label_1907;
                                                }
                                                this.onlineTextView[j].setText(LocaleController.formatPluralString("Subscribers", array[0]).replace(String.format("%d", array[0]), formatShortNumber));
                                                n2 = n3;
                                                break Label_1907;
                                            }
                                        }
                                    }
                                    n2 = n3;
                                    if (!this.onlineTextView[j].getText().equals(s3)) {
                                        this.onlineTextView[j].setText(s3);
                                        n2 = n3;
                                    }
                                }
                            }
                        }
                        ++j;
                    }
                    if (n2 != 0) {
                        this.needLayout();
                    }
                    final TLRPC.ChatPhoto photo2 = chat.photo;
                    TLRPC.FileLocation photo_big2;
                    if (photo2 != null) {
                        photo_big2 = photo2.photo_big;
                    }
                    else {
                        photo_big2 = null;
                    }
                    this.avatarDrawable.setInfo(chat);
                    this.avatarImage.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, chat);
                    FileLoader.getInstance(super.currentAccount).loadFile(ImageLocation.getForChat(chat, true), chat, null, 0, 1);
                    this.avatarImage.getImageReceiver().setVisible(PhotoViewer.isShowingImage(photo_big2) ^ true, false);
                }
            }
        }
    }
    
    private void updateRowsIds() {
        this.rowCount = 0;
        this.emptyRow = -1;
        this.infoHeaderRow = -1;
        this.phoneRow = -1;
        this.userInfoRow = -1;
        this.channelInfoRow = -1;
        this.usernameRow = -1;
        this.settingsTimerRow = -1;
        this.settingsKeyRow = -1;
        this.notificationsDividerRow = -1;
        this.notificationsRow = -1;
        this.infoSectionRow = -1;
        this.settingsSectionRow = -1;
        this.membersHeaderRow = -1;
        this.membersStartRow = -1;
        this.membersEndRow = -1;
        this.addMemberRow = -1;
        this.subscribersRow = -1;
        this.administratorsRow = -1;
        this.blockedUsersRow = -1;
        this.membersSectionRow = -1;
        this.sharedHeaderRow = -1;
        this.photosRow = -1;
        this.filesRow = -1;
        this.linksRow = -1;
        this.audioRow = -1;
        this.voiceRow = -1;
        this.groupsInCommonRow = -1;
        this.sharedSectionRow = -1;
        this.unblockRow = -1;
        this.startSecretChatRow = -1;
        this.leaveChannelRow = -1;
        this.joinRow = -1;
        this.lastSectionRow = -1;
        int n = 0;
        boolean b;
        while (true) {
            final int[] lastMediaCount = this.lastMediaCount;
            if (n >= lastMediaCount.length) {
                b = false;
                break;
            }
            if (lastMediaCount[n] > 0) {
                b = true;
                break;
            }
            ++n;
        }
        if (this.user_id != 0 && LocaleController.isRTL) {
            this.emptyRow = this.rowCount++;
        }
        if (this.user_id != 0) {
            final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            final TLRPC.UserFull userInfo = this.userInfo;
            final boolean b2 = (userInfo != null && !TextUtils.isEmpty((CharSequence)userInfo.about)) || (user != null && !TextUtils.isEmpty((CharSequence)user.username));
            final boolean b3 = user != null && !TextUtils.isEmpty((CharSequence)user.phone);
            this.infoHeaderRow = this.rowCount++;
            if (!this.isBot && (b3 || (!b3 && !b2))) {
                this.phoneRow = this.rowCount++;
            }
            final TLRPC.UserFull userInfo2 = this.userInfo;
            if (userInfo2 != null && !TextUtils.isEmpty((CharSequence)userInfo2.about)) {
                this.userInfoRow = this.rowCount++;
            }
            if (user != null && !TextUtils.isEmpty((CharSequence)user.username)) {
                this.usernameRow = this.rowCount++;
            }
            if (this.phoneRow != -1 || this.userInfoRow != -1 || this.usernameRow != -1) {
                this.notificationsDividerRow = this.rowCount++;
            }
            if (this.user_id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                this.notificationsRow = this.rowCount++;
            }
            this.infoSectionRow = this.rowCount++;
            if (this.currentEncryptedChat instanceof TLRPC.TL_encryptedChat) {
                this.settingsTimerRow = this.rowCount++;
                this.settingsKeyRow = this.rowCount++;
                this.settingsSectionRow = this.rowCount++;
            }
            Label_0867: {
                if (!b) {
                    final TLRPC.UserFull userInfo3 = this.userInfo;
                    if (userInfo3 == null || userInfo3.common_chats_count == 0) {
                        break Label_0867;
                    }
                }
                this.sharedHeaderRow = this.rowCount++;
                if (this.lastMediaCount[0] > 0) {
                    this.photosRow = this.rowCount++;
                }
                else {
                    this.photosRow = -1;
                }
                if (this.lastMediaCount[1] > 0) {
                    this.filesRow = this.rowCount++;
                }
                else {
                    this.filesRow = -1;
                }
                if (this.lastMediaCount[3] > 0) {
                    this.linksRow = this.rowCount++;
                }
                else {
                    this.linksRow = -1;
                }
                if (this.lastMediaCount[4] > 0) {
                    this.audioRow = this.rowCount++;
                }
                else {
                    this.audioRow = -1;
                }
                if (this.lastMediaCount[2] > 0) {
                    this.voiceRow = this.rowCount++;
                }
                else {
                    this.voiceRow = -1;
                }
                final TLRPC.UserFull userInfo4 = this.userInfo;
                if (userInfo4 != null && userInfo4.common_chats_count != 0) {
                    this.groupsInCommonRow = this.rowCount++;
                }
                this.sharedSectionRow = this.rowCount++;
            }
            if (user != null && !this.isBot && this.currentEncryptedChat == null && user.id != UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                if (this.userBlocked) {
                    this.unblockRow = this.rowCount++;
                }
                else {
                    this.startSecretChatRow = this.rowCount++;
                }
                this.lastSectionRow = this.rowCount++;
            }
        }
        else {
            final int chat_id = this.chat_id;
            if (chat_id != 0) {
                if (chat_id > 0) {
                    final TLRPC.ChatFull chatInfo = this.chatInfo;
                    if ((chatInfo != null && !TextUtils.isEmpty((CharSequence)chatInfo.about)) || !TextUtils.isEmpty((CharSequence)this.currentChat.username)) {
                        this.infoHeaderRow = this.rowCount++;
                        final TLRPC.ChatFull chatInfo2 = this.chatInfo;
                        if (chatInfo2 != null && !TextUtils.isEmpty((CharSequence)chatInfo2.about)) {
                            this.channelInfoRow = this.rowCount++;
                        }
                        if (!TextUtils.isEmpty((CharSequence)this.currentChat.username)) {
                            this.usernameRow = this.rowCount++;
                        }
                    }
                    if (this.channelInfoRow != -1 || this.usernameRow != -1) {
                        this.notificationsDividerRow = this.rowCount++;
                    }
                    this.notificationsRow = this.rowCount++;
                    this.infoSectionRow = this.rowCount++;
                    if (ChatObject.isChannel(this.currentChat)) {
                        final TLRPC.Chat currentChat = this.currentChat;
                        if (!currentChat.megagroup) {
                            final TLRPC.ChatFull chatInfo3 = this.chatInfo;
                            if (chatInfo3 != null && (currentChat.creator || chatInfo3.can_view_participants)) {
                                this.membersHeaderRow = this.rowCount++;
                                this.subscribersRow = this.rowCount++;
                                this.administratorsRow = this.rowCount++;
                                final TLRPC.ChatFull chatInfo4 = this.chatInfo;
                                if (chatInfo4.banned_count != 0 || chatInfo4.kicked_count != 0) {
                                    this.blockedUsersRow = this.rowCount++;
                                }
                                this.membersSectionRow = this.rowCount++;
                            }
                        }
                    }
                    if (b) {
                        this.sharedHeaderRow = this.rowCount++;
                        if (this.lastMediaCount[0] > 0) {
                            this.photosRow = this.rowCount++;
                        }
                        else {
                            this.photosRow = -1;
                        }
                        if (this.lastMediaCount[1] > 0) {
                            this.filesRow = this.rowCount++;
                        }
                        else {
                            this.filesRow = -1;
                        }
                        if (this.lastMediaCount[3] > 0) {
                            this.linksRow = this.rowCount++;
                        }
                        else {
                            this.linksRow = -1;
                        }
                        if (this.lastMediaCount[4] > 0) {
                            this.audioRow = this.rowCount++;
                        }
                        else {
                            this.audioRow = -1;
                        }
                        if (this.lastMediaCount[2] > 0) {
                            this.voiceRow = this.rowCount++;
                        }
                        else {
                            this.voiceRow = -1;
                        }
                        this.sharedSectionRow = this.rowCount++;
                    }
                    if (ChatObject.isChannel(this.currentChat)) {
                        final TLRPC.Chat currentChat2 = this.currentChat;
                        if (!currentChat2.creator && !currentChat2.left && !currentChat2.kicked && !currentChat2.megagroup) {
                            this.leaveChannelRow = this.rowCount++;
                            this.lastSectionRow = this.rowCount++;
                        }
                        final TLRPC.ChatFull chatInfo5 = this.chatInfo;
                        if (chatInfo5 != null && this.currentChat.megagroup) {
                            final TLRPC.ChatParticipants participants = chatInfo5.participants;
                            if (participants != null && !participants.participants.isEmpty()) {
                                Label_1728: {
                                    if (!ChatObject.isNotInChat(this.currentChat)) {
                                        final TLRPC.Chat currentChat3 = this.currentChat;
                                        if (currentChat3.megagroup && ChatObject.canAddUsers(currentChat3)) {
                                            final TLRPC.ChatFull chatInfo6 = this.chatInfo;
                                            if (chatInfo6 == null || chatInfo6.participants_count < MessagesController.getInstance(super.currentAccount).maxMegagroupCount) {
                                                this.addMemberRow = this.rowCount++;
                                                break Label_1728;
                                            }
                                        }
                                    }
                                    this.membersHeaderRow = this.rowCount++;
                                }
                                final int rowCount = this.rowCount;
                                this.membersStartRow = rowCount;
                                this.rowCount = rowCount + this.chatInfo.participants.participants.size();
                                final int rowCount2 = this.rowCount;
                                this.membersEndRow = rowCount2;
                                this.rowCount = rowCount2 + 1;
                                this.membersSectionRow = rowCount2;
                            }
                        }
                        if (this.lastSectionRow == -1) {
                            final TLRPC.Chat currentChat4 = this.currentChat;
                            if (currentChat4.left && !currentChat4.kicked) {
                                this.joinRow = this.rowCount++;
                                this.lastSectionRow = this.rowCount++;
                            }
                        }
                    }
                    else {
                        final TLRPC.ChatFull chatInfo7 = this.chatInfo;
                        if (chatInfo7 != null && !(chatInfo7.participants instanceof TLRPC.TL_chatParticipantsForbidden)) {
                            Label_1931: {
                                if (!ChatObject.canAddUsers(this.currentChat)) {
                                    final TLRPC.TL_chatBannedRights default_banned_rights = this.currentChat.default_banned_rights;
                                    if (default_banned_rights != null) {
                                        if (default_banned_rights.invite_users) {
                                            this.membersHeaderRow = this.rowCount++;
                                            break Label_1931;
                                        }
                                    }
                                }
                                this.addMemberRow = this.rowCount++;
                            }
                            final int rowCount3 = this.rowCount;
                            this.membersStartRow = rowCount3;
                            this.rowCount = rowCount3 + this.chatInfo.participants.participants.size();
                            final int rowCount4 = this.rowCount;
                            this.membersEndRow = rowCount4;
                            this.rowCount = rowCount4 + 1;
                            this.membersSectionRow = rowCount4;
                        }
                    }
                }
                else if (!ChatObject.isChannel(this.currentChat)) {
                    final TLRPC.ChatFull chatInfo8 = this.chatInfo;
                    if (chatInfo8 != null) {
                        final TLRPC.ChatParticipants participants2 = chatInfo8.participants;
                        if (!(participants2 instanceof TLRPC.TL_chatParticipantsForbidden)) {
                            this.membersHeaderRow = this.rowCount++;
                            final int rowCount5 = this.rowCount;
                            this.membersStartRow = rowCount5;
                            this.rowCount = rowCount5 + participants2.participants.size();
                            final int rowCount6 = this.rowCount;
                            this.membersEndRow = rowCount6;
                            this.rowCount = rowCount6 + 1;
                            this.membersSectionRow = rowCount6;
                            this.addMemberRow = this.rowCount++;
                        }
                    }
                }
            }
        }
    }
    
    private void updateSharedMediaRows() {
        if (this.listAdapter == null) {
            return;
        }
        final int sharedHeaderRow = this.sharedHeaderRow;
        final int photosRow = this.photosRow;
        final int filesRow = this.filesRow;
        final int linksRow = this.linksRow;
        final int audioRow = this.audioRow;
        final int voiceRow = this.voiceRow;
        final int groupsInCommonRow = this.groupsInCommonRow;
        final int sharedSectionRow = this.sharedSectionRow;
        this.updateRowsIds();
        int n = 3;
        if (sharedHeaderRow == -1 && this.sharedHeaderRow != -1) {
            if (this.photosRow == -1) {
                n = 2;
            }
            int n2 = n;
            if (this.filesRow != -1) {
                n2 = n + 1;
            }
            int n3 = n2;
            if (this.linksRow != -1) {
                n3 = n2 + 1;
            }
            int n4 = n3;
            if (this.audioRow != -1) {
                n4 = n3 + 1;
            }
            int n5 = n4;
            if (this.voiceRow != -1) {
                n5 = n4 + 1;
            }
            int n6 = n5;
            if (this.groupsInCommonRow != -1) {
                n6 = n5 + 1;
            }
            this.listAdapter.notifyItemRangeInserted(this.sharedHeaderRow, n6);
        }
        else if (sharedHeaderRow != -1 && this.sharedHeaderRow != -1) {
            if (photosRow != -1) {
                final int photosRow2 = this.photosRow;
                if (photosRow2 != -1 && this.prevMediaCount[0] != this.lastMediaCount[0]) {
                    this.listAdapter.notifyItemChanged(photosRow2);
                }
            }
            if (filesRow != -1) {
                final int filesRow2 = this.filesRow;
                if (filesRow2 != -1 && this.prevMediaCount[1] != this.lastMediaCount[1]) {
                    this.listAdapter.notifyItemChanged(filesRow2);
                }
            }
            if (linksRow != -1) {
                final int linksRow2 = this.linksRow;
                if (linksRow2 != -1 && this.prevMediaCount[3] != this.lastMediaCount[3]) {
                    this.listAdapter.notifyItemChanged(linksRow2);
                }
            }
            if (audioRow != -1) {
                final int audioRow2 = this.audioRow;
                if (audioRow2 != -1 && this.prevMediaCount[4] != this.lastMediaCount[4]) {
                    this.listAdapter.notifyItemChanged(audioRow2);
                }
            }
            if (voiceRow != -1) {
                final int voiceRow2 = this.voiceRow;
                if (voiceRow2 != -1 && this.prevMediaCount[2] != this.lastMediaCount[2]) {
                    this.listAdapter.notifyItemChanged(voiceRow2);
                }
            }
            Label_0464: {
                if (photosRow == -1) {
                    final int photosRow3 = this.photosRow;
                    if (photosRow3 != -1) {
                        this.listAdapter.notifyItemInserted(photosRow3);
                        break Label_0464;
                    }
                }
                if (photosRow != -1 && this.photosRow == -1) {
                    this.listAdapter.notifyItemRemoved(photosRow);
                }
            }
            Label_0514: {
                if (filesRow == -1) {
                    final int filesRow3 = this.filesRow;
                    if (filesRow3 != -1) {
                        this.listAdapter.notifyItemInserted(filesRow3);
                        break Label_0514;
                    }
                }
                if (filesRow != -1 && this.filesRow == -1) {
                    this.listAdapter.notifyItemRemoved(filesRow);
                }
            }
            Label_0567: {
                if (linksRow == -1) {
                    final int linksRow3 = this.linksRow;
                    if (linksRow3 != -1) {
                        this.listAdapter.notifyItemInserted(linksRow3);
                        break Label_0567;
                    }
                }
                if (linksRow != -1 && this.linksRow == -1) {
                    this.listAdapter.notifyItemRemoved(linksRow);
                }
            }
            Label_0620: {
                if (audioRow == -1) {
                    final int audioRow3 = this.audioRow;
                    if (audioRow3 != -1) {
                        this.listAdapter.notifyItemInserted(audioRow3);
                        break Label_0620;
                    }
                }
                if (audioRow != -1 && this.audioRow == -1) {
                    this.listAdapter.notifyItemRemoved(audioRow);
                }
            }
            Label_0673: {
                if (voiceRow == -1) {
                    final int voiceRow3 = this.voiceRow;
                    if (voiceRow3 != -1) {
                        this.listAdapter.notifyItemInserted(voiceRow3);
                        break Label_0673;
                    }
                }
                if (voiceRow != -1 && this.voiceRow == -1) {
                    this.listAdapter.notifyItemRemoved(voiceRow);
                }
            }
            if (groupsInCommonRow == -1) {
                final int groupsInCommonRow2 = this.groupsInCommonRow;
                if (groupsInCommonRow2 != -1) {
                    this.listAdapter.notifyItemInserted(groupsInCommonRow2);
                    return;
                }
            }
            if (groupsInCommonRow != -1 && this.groupsInCommonRow == -1) {
                this.listAdapter.notifyItemRemoved(groupsInCommonRow);
            }
        }
    }
    
    @Override
    protected ActionBar createActionBar(final Context context) {
        final ActionBar actionBar = new ActionBar(context) {
            @Override
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                return super.onTouchEvent(motionEvent);
            }
        };
        int chat_id;
        if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
            chat_id = this.chat_id;
        }
        else {
            chat_id = 5;
        }
        final int buttonColorForId = AvatarDrawable.getButtonColorForId(chat_id);
        final boolean b = false;
        actionBar.setItemsBackgroundColor(buttonColorForId, false);
        actionBar.setItemsColor(Theme.getColor("actionBarDefaultIcon"), false);
        actionBar.setItemsColor(Theme.getColor("actionBarActionModeDefaultIcon"), true);
        actionBar.setBackButtonDrawable(new BackDrawable(false));
        actionBar.setCastShadows(false);
        actionBar.setAddToContainer(false);
        boolean occupyStatusBar = b;
        if (Build$VERSION.SDK_INT >= 21) {
            occupyStatusBar = b;
            if (!AndroidUtilities.isTablet()) {
                occupyStatusBar = true;
            }
        }
        actionBar.setOccupyStatusBar(occupyStatusBar);
        return actionBar;
    }
    
    @Override
    public View createView(final Context context) {
        Theme.createProfileResources(context);
        super.hasOwnBackground = true;
        this.extraHeight = AndroidUtilities.dp(88.0f);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int n) {
                if (ProfileActivity.this.getParentActivity() == null) {
                    return;
                }
                if (n == -1) {
                    ProfileActivity.this.finishFragment();
                }
                else if (n == 2) {
                    final TLRPC.User user = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                    if (user == null) {
                        return;
                    }
                    if (ProfileActivity.this.isBot && !MessagesController.isSupportUser(user)) {
                        if (!ProfileActivity.this.userBlocked) {
                            MessagesController.getInstance(ProfileActivity.this.currentAccount).blockUser(ProfileActivity.this.user_id);
                        }
                        else {
                            MessagesController.getInstance(ProfileActivity.this.currentAccount).unblockUser(ProfileActivity.this.user_id);
                            SendMessagesHelper.getInstance(ProfileActivity.this.currentAccount).sendMessage("/start", ProfileActivity.this.user_id, null, null, false, null, null, null);
                            ProfileActivity.this.finishFragment();
                        }
                    }
                    else if (ProfileActivity.this.userBlocked) {
                        MessagesController.getInstance(ProfileActivity.this.currentAccount).unblockUser(ProfileActivity.this.user_id);
                        AlertsCreator.showSimpleToast(ProfileActivity.this, LocaleController.getString("UserUnblocked", 2131561020));
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ProfileActivity.this.getParentActivity());
                        builder.setTitle(LocaleController.getString("BlockUser", 2131558834));
                        builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureBlockContact2", 2131558667, ContactsController.formatName(user.first_name, user.last_name))));
                        builder.setPositiveButton(LocaleController.getString("BlockContact", 2131558833), (DialogInterface$OnClickListener)new _$$Lambda$ProfileActivity$3$RRKDv6pwZUkbtuOuDHn_j5rBSMY(this));
                        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                        final AlertDialog create = builder.create();
                        ProfileActivity.this.showDialog(create);
                        final TextView textView = (TextView)create.getButton(-1);
                        if (textView != null) {
                            textView.setTextColor(Theme.getColor("dialogTextRed2"));
                        }
                    }
                }
                else if (n == 1) {
                    final TLRPC.User user2 = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                    final Bundle bundle = new Bundle();
                    bundle.putInt("user_id", user2.id);
                    bundle.putBoolean("addContact", true);
                    ProfileActivity.this.presentFragment(new ContactAddActivity(bundle));
                }
                else if (n == 3) {
                    final Bundle bundle2 = new Bundle();
                    bundle2.putBoolean("onlySelect", true);
                    bundle2.putString("selectAlertString", LocaleController.getString("SendContactTo", 2131560689));
                    bundle2.putString("selectAlertStringGroup", LocaleController.getString("SendContactToGroup", 2131560690));
                    final DialogsActivity dialogsActivity = new DialogsActivity(bundle2);
                    dialogsActivity.setDelegate((DialogsActivity.DialogsActivityDelegate)ProfileActivity.this);
                    ProfileActivity.this.presentFragment(dialogsActivity);
                }
                else if (n == 4) {
                    final Bundle bundle3 = new Bundle();
                    bundle3.putInt("user_id", ProfileActivity.this.user_id);
                    ProfileActivity.this.presentFragment(new ContactAddActivity(bundle3));
                }
                else if (n == 5) {
                    final TLRPC.User user3 = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                    if (user3 == null || ProfileActivity.this.getParentActivity() == null) {
                        return;
                    }
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)ProfileActivity.this.getParentActivity());
                    builder2.setTitle(LocaleController.getString("DeleteContact", 2131559241));
                    builder2.setMessage(LocaleController.getString("AreYouSureDeleteContact", 2131558680));
                    builder2.setPositiveButton(LocaleController.getString("Delete", 2131559227), (DialogInterface$OnClickListener)new _$$Lambda$ProfileActivity$3$_wA0DEMpYVaVxb1SmJsBe0hOY_I(this, user3));
                    builder2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    final AlertDialog create2 = builder2.create();
                    ProfileActivity.this.showDialog(create2);
                    final TextView textView2 = (TextView)create2.getButton(-1);
                    if (textView2 != null) {
                        textView2.setTextColor(Theme.getColor("dialogTextRed2"));
                    }
                }
                else if (n == 7) {
                    ProfileActivity.this.leaveChatPressed();
                }
                else if (n == 12) {
                    final Bundle bundle4 = new Bundle();
                    bundle4.putInt("chat_id", ProfileActivity.this.chat_id);
                    final ChatEditActivity chatEditActivity = new ChatEditActivity(bundle4);
                    chatEditActivity.setInfo(ProfileActivity.this.chatInfo);
                    ProfileActivity.this.presentFragment(chatEditActivity);
                }
                else if (n == 9) {
                    final TLRPC.User user4 = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                    if (user4 == null) {
                        return;
                    }
                    final Bundle bundle5 = new Bundle();
                    bundle5.putBoolean("onlySelect", true);
                    bundle5.putInt("dialogsType", 2);
                    bundle5.putString("addToGroupAlertString", LocaleController.formatString("AddToTheGroupTitle", 2131558596, UserObject.getUserName(user4), "%1$s"));
                    final DialogsActivity dialogsActivity2 = new DialogsActivity(bundle5);
                    dialogsActivity2.setDelegate((DialogsActivity.DialogsActivityDelegate)new _$$Lambda$ProfileActivity$3$P_pKjyT3akZRyLfSf9io1zoClmw(this, user4));
                    ProfileActivity.this.presentFragment(dialogsActivity2);
                }
                else if (n == 10) {
                    try {
                        final TLRPC.User user5 = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                        if (user5 == null) {
                            return;
                        }
                        final Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        if (ProfileActivity.this.botInfo != null && ProfileActivity.this.userInfo != null && !TextUtils.isEmpty((CharSequence)ProfileActivity.this.userInfo.about)) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("%s https://");
                            sb.append(MessagesController.getInstance(ProfileActivity.this.currentAccount).linkPrefix);
                            sb.append("/%s");
                            intent.putExtra("android.intent.extra.TEXT", String.format(sb.toString(), ProfileActivity.this.userInfo.about, user5.username));
                        }
                        else {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("https://");
                            sb2.append(MessagesController.getInstance(ProfileActivity.this.currentAccount).linkPrefix);
                            sb2.append("/%s");
                            intent.putExtra("android.intent.extra.TEXT", String.format(sb2.toString(), user5.username));
                        }
                        ProfileActivity.this.startActivityForResult(Intent.createChooser(intent, (CharSequence)LocaleController.getString("BotShare", 2131558856)), 500);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                else {
                    if (n == 14) {
                        try {
                            long n2;
                            if (ProfileActivity.this.currentEncryptedChat != null) {
                                n2 = (long)ProfileActivity.this.currentEncryptedChat.id << 32;
                            }
                            else {
                                if (ProfileActivity.this.user_id != 0) {
                                    n = ProfileActivity.this.user_id;
                                }
                                else {
                                    if (ProfileActivity.this.chat_id == 0) {
                                        return;
                                    }
                                    n = -ProfileActivity.this.chat_id;
                                }
                                n2 = n;
                            }
                            DataQuery.getInstance(ProfileActivity.this.currentAccount).installShortcut(n2);
                            return;
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                            return;
                        }
                    }
                    if (n == 15) {
                        final TLRPC.User user6 = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                        if (user6 != null) {
                            VoIPHelper.startCall(user6, ProfileActivity.this.getParentActivity(), ProfileActivity.this.userInfo);
                        }
                    }
                    else if (n == 17) {
                        final Bundle bundle6 = new Bundle();
                        bundle6.putInt("chat_id", ProfileActivity.this.chat_id);
                        bundle6.putInt("type", 2);
                        bundle6.putBoolean("open_search", true);
                        final ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle6);
                        chatUsersActivity.setInfo(ProfileActivity.this.chatInfo);
                        ProfileActivity.this.presentFragment(chatUsersActivity);
                    }
                    else if (n == 18) {
                        ProfileActivity.this.openAddMember();
                    }
                    else if (n == 19) {
                        if (ProfileActivity.this.user_id != 0) {
                            n = ProfileActivity.this.user_id;
                        }
                        else {
                            n = -ProfileActivity.this.chat_id;
                        }
                        final AlertDialog[] array = { new AlertDialog((Context)ProfileActivity.this.getParentActivity(), 3) };
                        final TLRPC.TL_messages_getStatsURL tl_messages_getStatsURL = new TLRPC.TL_messages_getStatsURL();
                        tl_messages_getStatsURL.peer = MessagesController.getInstance(ProfileActivity.this.currentAccount).getInputPeer(n);
                        tl_messages_getStatsURL.dark = Theme.getCurrentTheme().isDark();
                        tl_messages_getStatsURL.params = "";
                        n = ConnectionsManager.getInstance(ProfileActivity.this.currentAccount).sendRequest(tl_messages_getStatsURL, new _$$Lambda$ProfileActivity$3$vS1Cpjl_0IaDsZcydzGAsiNHZyw(this, array));
                        if (array[0] == null) {
                            return;
                        }
                        array[0].setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$ProfileActivity$3$Y2V1RZ8yX7Q0L7hu0zkMaoHuQs4(this, n));
                        ProfileActivity.this.showDialog(array[0]);
                    }
                }
            }
        });
        this.createActionBarMenu();
        this.listAdapter = new ListAdapter(context);
        (this.avatarDrawable = new AvatarDrawable()).setProfile(true);
        super.fragmentView = (View)new FrameLayout(context) {
            public boolean hasOverlappingRendering() {
                return false;
            }
            
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                ProfileActivity.this.checkListViewScroll();
            }
        };
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.listView = new RecyclerListView(context) {
            private Paint paint = new Paint();
            
            @Override
            public boolean hasOverlappingRendering() {
                return false;
            }
            
            @Override
            public void onDraw(final Canvas canvas) {
                Object o;
                if (ProfileActivity.this.lastSectionRow != -1) {
                    o = this.findViewHolderForAdapterPosition(ProfileActivity.this.lastSectionRow);
                }
                else if (ProfileActivity.this.sharedSectionRow != -1 && (ProfileActivity.this.membersSectionRow == -1 || ProfileActivity.this.membersSectionRow < ProfileActivity.this.sharedSectionRow)) {
                    o = this.findViewHolderForAdapterPosition(ProfileActivity.this.sharedSectionRow);
                }
                else if (ProfileActivity.this.membersSectionRow != -1 && (ProfileActivity.this.sharedSectionRow == -1 || ProfileActivity.this.membersSectionRow > ProfileActivity.this.sharedSectionRow)) {
                    o = this.findViewHolderForAdapterPosition(ProfileActivity.this.membersSectionRow);
                }
                else if (ProfileActivity.this.infoSectionRow != -1) {
                    o = this.findViewHolderForAdapterPosition(ProfileActivity.this.infoSectionRow);
                }
                else {
                    o = null;
                }
                final int measuredHeight = this.getMeasuredHeight();
                int bottom = 0;
                Label_0194: {
                    if (o != null) {
                        bottom = ((ViewHolder)o).itemView.getBottom();
                        if (((ViewHolder)o).itemView.getBottom() < measuredHeight) {
                            break Label_0194;
                        }
                    }
                    bottom = measuredHeight;
                }
                this.paint.setColor(Theme.getColor("windowBackgroundWhite"));
                final float n = (float)this.getMeasuredWidth();
                final float n2 = (float)bottom;
                canvas.drawRect(0.0f, 0.0f, n, n2, this.paint);
                if (bottom != measuredHeight) {
                    this.paint.setColor(Theme.getColor("windowBackgroundGray"));
                    canvas.drawRect(0.0f, n2, (float)this.getMeasuredWidth(), (float)measuredHeight, this.paint);
                }
            }
        }).setVerticalScrollBarEnabled(false);
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setClipToPadding(false);
        (this.layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }).setOrientation(1);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)this.layoutManager);
        final RecyclerListView listView = this.listView;
        int chat_id;
        if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
            chat_id = this.chat_id;
        }
        else {
            chat_id = 5;
        }
        listView.setGlowColor(AvatarDrawable.getProfileBackColorForId(chat_id));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)new _$$Lambda$ProfileActivity$H08izesUB4mAkQt5BMshtXqt2Qs(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$ProfileActivity$Y_qIPa4kzyhg9puFuR2Vq4GocZU(this));
        if (this.banFromGroup != 0) {
            final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.banFromGroup);
            if (this.currentChannelParticipant == null) {
                final TLRPC.TL_channels_getParticipant tl_channels_getParticipant = new TLRPC.TL_channels_getParticipant();
                tl_channels_getParticipant.channel = MessagesController.getInputChannel(chat);
                tl_channels_getParticipant.user_id = MessagesController.getInstance(super.currentAccount).getInputUser(this.user_id);
                ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_channels_getParticipant, new _$$Lambda$ProfileActivity$UPmZCQ76huGf0mwqhk7A6RKfTkw(this));
            }
            final FrameLayout frameLayout2 = new FrameLayout(context) {
                protected void onDraw(final Canvas canvas) {
                    final int intrinsicHeight = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                    Theme.chat_composeShadowDrawable.setBounds(0, 0, this.getMeasuredWidth(), intrinsicHeight);
                    Theme.chat_composeShadowDrawable.draw(canvas);
                    canvas.drawRect(0.0f, (float)intrinsicHeight, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
                }
            };
            frameLayout2.setWillNotDraw(false);
            frameLayout.addView((View)frameLayout2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 51, 83));
            frameLayout2.setOnClickListener((View$OnClickListener)new _$$Lambda$ProfileActivity$ZhX3Wvmi7If4l7aW31Ti2T1UbGA(this, chat));
            final TextView textView = new TextView(context);
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteRedText"));
            textView.setTextSize(1, 15.0f);
            textView.setGravity(17);
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView.setText((CharSequence)LocaleController.getString("BanFromTheGroup", 2131558830));
            frameLayout2.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 1.0f, 0.0f, 0.0f));
            this.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, AndroidUtilities.dp(48.0f));
            this.listView.setBottomGlowOffset(AndroidUtilities.dp(48.0f));
        }
        else {
            this.listView.setPadding(0, AndroidUtilities.dp(88.0f), 0, 0);
        }
        this.topView = new TopView(context);
        final TopView topView = this.topView;
        int chat_id2;
        if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
            chat_id2 = this.chat_id;
        }
        else {
            chat_id2 = 5;
        }
        topView.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(chat_id2));
        frameLayout.addView((View)this.topView);
        frameLayout.addView((View)super.actionBar);
        (this.avatarImage = new BackupImageView(context)).setRoundRadius(AndroidUtilities.dp(21.0f));
        this.avatarImage.setPivotX(0.0f);
        this.avatarImage.setPivotY(0.0f);
        frameLayout.addView((View)this.avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(42, 42.0f, 51, 64.0f, 0.0f, 0.0f, 0.0f));
        this.avatarImage.setOnClickListener((View$OnClickListener)new _$$Lambda$ProfileActivity$MnD_12oukmBdwxWh9wauwLpeZDg(this));
        this.avatarImage.setContentDescription((CharSequence)LocaleController.getString("AccDescrProfilePicture", 2131558460));
        for (int i = 0; i < 2; ++i) {
            if (this.playProfileAnimation || i != 0) {
                this.nameTextView[i] = new SimpleTextView(context);
                if (i == 1) {
                    this.nameTextView[i].setTextColor(Theme.getColor("profile_title"));
                }
                else {
                    this.nameTextView[i].setTextColor(Theme.getColor("actionBarDefaultTitle"));
                }
                this.nameTextView[i].setTextSize(18);
                this.nameTextView[i].setGravity(3);
                this.nameTextView[i].setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                this.nameTextView[i].setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
                this.nameTextView[i].setPivotX(0.0f);
                this.nameTextView[i].setPivotY(0.0f);
                final SimpleTextView simpleTextView = this.nameTextView[i];
                float alpha;
                if (i == 0) {
                    alpha = 0.0f;
                }
                else {
                    alpha = 1.0f;
                }
                simpleTextView.setAlpha(alpha);
                if (i == 1) {
                    this.nameTextView[i].setScrollNonFitText(true);
                    final SimpleTextView simpleTextView2 = this.nameTextView[i];
                    int chat_id3;
                    if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
                        chat_id3 = this.chat_id;
                    }
                    else {
                        chat_id3 = 5;
                    }
                    simpleTextView2.setBackgroundColor(AvatarDrawable.getProfileBackColorForId(chat_id3));
                }
                final SimpleTextView simpleTextView3 = this.nameTextView[i];
                float n;
                if (i == 0) {
                    n = 48.0f;
                }
                else {
                    n = 0.0f;
                }
                frameLayout.addView((View)simpleTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, n, 0.0f));
                this.onlineTextView[i] = new SimpleTextView(context);
                final SimpleTextView simpleTextView4 = this.onlineTextView[i];
                int chat_id4;
                if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
                    chat_id4 = this.chat_id;
                }
                else {
                    chat_id4 = 5;
                }
                simpleTextView4.setTextColor(AvatarDrawable.getProfileTextColorForId(chat_id4));
                this.onlineTextView[i].setTextSize(14);
                this.onlineTextView[i].setGravity(3);
                final SimpleTextView simpleTextView5 = this.onlineTextView[i];
                float alpha2;
                if (i == 0) {
                    alpha2 = 0.0f;
                }
                else {
                    alpha2 = 1.0f;
                }
                simpleTextView5.setAlpha(alpha2);
                final SimpleTextView simpleTextView6 = this.onlineTextView[i];
                float n2;
                if (i == 0) {
                    n2 = 48.0f;
                }
                else {
                    n2 = 8.0f;
                }
                frameLayout.addView((View)simpleTextView6, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 51, 118.0f, 0.0f, n2, 0.0f));
            }
        }
        if (this.user_id != 0) {
            this.writeButton = new ImageView(context);
            CombinedDrawable simpleSelectorCircleDrawable;
            final Drawable drawable = simpleSelectorCircleDrawable = (CombinedDrawable)Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("profile_actionBackground"), Theme.getColor("profile_actionPressedBackground"));
            if (Build$VERSION.SDK_INT < 21) {
                final Drawable mutate = context.getResources().getDrawable(2131165388).mutate();
                mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
                simpleSelectorCircleDrawable = new CombinedDrawable(mutate, drawable, 0, 0);
                simpleSelectorCircleDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            }
            this.writeButton.setBackgroundDrawable((Drawable)simpleSelectorCircleDrawable);
            this.writeButton.setImageResource(2131165790);
            this.writeButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrOpenChat", 2131558450));
            this.writeButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("profile_actionIcon"), PorterDuff$Mode.MULTIPLY));
            this.writeButton.setScaleType(ImageView$ScaleType.CENTER);
            if (Build$VERSION.SDK_INT >= 21) {
                final StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
                stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.writeButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
                this.writeButton.setStateListAnimator(stateListAnimator);
                this.writeButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                    @SuppressLint({ "NewApi" })
                    public void getOutline(final View view, final Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    }
                });
            }
            final ImageView writeButton = this.writeButton;
            int n3;
            if (Build$VERSION.SDK_INT >= 21) {
                n3 = 56;
            }
            else {
                n3 = 60;
            }
            float n4;
            if (Build$VERSION.SDK_INT >= 21) {
                n4 = 56.0f;
            }
            else {
                n4 = 60.0f;
            }
            frameLayout.addView((View)writeButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n3, n4, 53, 0.0f, 0.0f, 16.0f, 0.0f));
            this.writeButton.setOnClickListener((View$OnClickListener)new _$$Lambda$ProfileActivity$UMpMcjByin2Fxco4lDz2l5oi4sc(this));
        }
        this.needLayout();
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                ProfileActivity.this.checkListViewScroll();
                if (ProfileActivity.this.participantsMap != null && !ProfileActivity.this.usersEndReached && ProfileActivity.this.layoutManager.findLastVisibleItemPosition() > ProfileActivity.this.membersEndRow - 8) {
                    ProfileActivity.this.getChannelParticipants(false);
                }
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int i, int j, final Object... array) {
        final int updateInterfaces = NotificationCenter.updateInterfaces;
        boolean userBlocked = false;
        final int n = 0;
        j = 0;
        if (i == updateInterfaces) {
            final int intValue = (int)array[0];
            if (this.user_id != 0) {
                if ((intValue & 0x2) != 0x0 || (intValue & 0x1) != 0x0 || (intValue & 0x4) != 0x0) {
                    this.updateProfileData();
                }
                if ((intValue & 0x400) != 0x0) {
                    final RecyclerListView listView = this.listView;
                    if (listView != null) {
                        final RecyclerListView.Holder holder = (RecyclerListView.Holder)listView.findViewHolderForPosition(this.phoneRow);
                        if (holder != null) {
                            this.listAdapter.onBindViewHolder(holder, this.phoneRow);
                        }
                    }
                }
            }
            else if (this.chat_id != 0) {
                i = (intValue & 0x2000);
                if (i != 0 || (intValue & 0x8) != 0x0 || (intValue & 0x10) != 0x0 || (intValue & 0x20) != 0x0 || (intValue & 0x4) != 0x0) {
                    this.updateOnlineCount();
                    this.updateProfileData();
                }
                if (i != 0) {
                    this.updateRowsIds();
                    final ListAdapter listAdapter = this.listAdapter;
                    if (listAdapter != null) {
                        ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
                    }
                }
                if ((intValue & 0x2) != 0x0 || (intValue & 0x1) != 0x0 || (intValue & 0x4) != 0x0) {
                    final RecyclerListView listView2 = this.listView;
                    if (listView2 != null) {
                        int childCount;
                        View child;
                        for (childCount = listView2.getChildCount(), i = j; i < childCount; ++i) {
                            child = this.listView.getChildAt(i);
                            if (child instanceof UserCell) {
                                ((UserCell)child).update(intValue);
                            }
                        }
                    }
                }
            }
        }
        else {
            final int chatOnlineCountDidLoad = NotificationCenter.chatOnlineCountDidLoad;
            j = 1;
            if (i == chatOnlineCountDidLoad) {
                final Integer n2 = (Integer)array[0];
                if (this.chatInfo != null) {
                    final TLRPC.Chat currentChat = this.currentChat;
                    if (currentChat != null) {
                        if (currentChat.id == n2) {
                            this.chatInfo.online_count = (int)array[1];
                            this.updateOnlineCount();
                            this.updateProfileData();
                        }
                    }
                }
                return;
            }
            if (i == NotificationCenter.contactsDidLoad) {
                this.createActionBarMenu();
            }
            else if (i == NotificationCenter.mediaDidLoad) {
                final long longValue = (long)array[0];
                if ((int)array[3] == super.classGuid) {
                    long dialog_id;
                    final long n3 = dialog_id = this.dialog_id;
                    Label_0447: {
                        if (n3 == 0L) {
                            i = this.user_id;
                            if (i == 0) {
                                i = this.chat_id;
                                dialog_id = n3;
                                if (i == 0) {
                                    break Label_0447;
                                }
                                i = -i;
                            }
                            dialog_id = i;
                        }
                    }
                    final int intValue2 = (int)array[4];
                    this.sharedMediaData[intValue2].setTotalCount((int)array[1]);
                    final ArrayList list = (ArrayList)array[2];
                    final boolean b = (int)dialog_id == 0;
                    i = j;
                    if (longValue == dialog_id) {
                        i = 0;
                    }
                    if (!list.isEmpty()) {
                        this.sharedMediaData[intValue2].setEndReached(i, (boolean)array[5]);
                    }
                    for (j = 0; j < list.size(); ++j) {
                        this.sharedMediaData[intValue2].addMessage(list.get(j), i, false, b);
                    }
                }
            }
            else if (i == NotificationCenter.mediaCountsDidLoad) {
                final long longValue2 = (long)array[0];
                long dialog_id2;
                final long n4 = dialog_id2 = this.dialog_id;
                if (n4 == 0L) {
                    i = this.user_id;
                    if (i != 0) {
                        dialog_id2 = i;
                    }
                    else {
                        i = this.chat_id;
                        dialog_id2 = n4;
                        if (i != 0) {
                            dialog_id2 = -i;
                        }
                    }
                }
                if (longValue2 == dialog_id2 || longValue2 == this.mergeDialogId) {
                    final int[] array2 = (int[])array[1];
                    if (longValue2 == dialog_id2) {
                        this.mediaCount = array2;
                    }
                    else {
                        this.mediaMergeCount = array2;
                    }
                    final int[] lastMediaCount = this.lastMediaCount;
                    final int[] prevMediaCount = this.prevMediaCount;
                    System.arraycopy(lastMediaCount, 0, prevMediaCount, 0, prevMediaCount.length);
                    i = 0;
                    while (true) {
                        final int[] lastMediaCount2 = this.lastMediaCount;
                        if (i >= lastMediaCount2.length) {
                            break;
                        }
                        final int[] mediaCount = this.mediaCount;
                        Label_0827: {
                            if (mediaCount[i] >= 0) {
                                final int[] mediaMergeCount = this.mediaMergeCount;
                                if (mediaMergeCount[i] >= 0) {
                                    lastMediaCount2[i] = mediaCount[i] + mediaMergeCount[i];
                                    break Label_0827;
                                }
                            }
                            final int[] mediaCount2 = this.mediaCount;
                            if (mediaCount2[i] >= 0) {
                                this.lastMediaCount[i] = mediaCount2[i];
                            }
                            else {
                                final int[] mediaMergeCount2 = this.mediaMergeCount;
                                if (mediaMergeCount2[i] >= 0) {
                                    this.lastMediaCount[i] = mediaMergeCount2[i];
                                }
                                else {
                                    this.lastMediaCount[i] = 0;
                                }
                            }
                        }
                        if (longValue2 == dialog_id2 && this.lastMediaCount[i] != 0) {
                            DataQuery.getInstance(super.currentAccount).loadMedia(dialog_id2, 50, 0, i, 2, super.classGuid);
                        }
                        ++i;
                    }
                    this.updateSharedMediaRows();
                }
            }
            else if (i == NotificationCenter.mediaCountDidLoad) {
                final long longValue3 = (long)array[0];
                long dialog_id3;
                final long n5 = dialog_id3 = this.dialog_id;
                Label_0948: {
                    if (n5 == 0L) {
                        i = this.user_id;
                        if (i == 0) {
                            i = this.chat_id;
                            dialog_id3 = n5;
                            if (i == 0) {
                                break Label_0948;
                            }
                            i = -i;
                        }
                        dialog_id3 = i;
                    }
                }
                if (longValue3 == dialog_id3 || longValue3 == this.mergeDialogId) {
                    j = (int)array[3];
                    i = (int)array[1];
                    if (longValue3 == dialog_id3) {
                        this.mediaCount[j] = i;
                    }
                    else {
                        this.mediaMergeCount[j] = i;
                    }
                    final int[] prevMediaCount2 = this.prevMediaCount;
                    final int[] lastMediaCount3 = this.lastMediaCount;
                    prevMediaCount2[j] = lastMediaCount3[j];
                    final int[] mediaCount3 = this.mediaCount;
                    Label_1123: {
                        if (mediaCount3[j] >= 0) {
                            final int[] mediaMergeCount3 = this.mediaMergeCount;
                            if (mediaMergeCount3[j] >= 0) {
                                lastMediaCount3[j] = mediaCount3[j] + mediaMergeCount3[j];
                                break Label_1123;
                            }
                        }
                        final int[] mediaCount4 = this.mediaCount;
                        if (mediaCount4[j] >= 0) {
                            this.lastMediaCount[j] = mediaCount4[j];
                        }
                        else {
                            final int[] mediaMergeCount4 = this.mediaMergeCount;
                            if (mediaMergeCount4[j] >= 0) {
                                this.lastMediaCount[j] = mediaMergeCount4[j];
                            }
                            else {
                                this.lastMediaCount[j] = 0;
                            }
                        }
                    }
                    this.updateSharedMediaRows();
                }
            }
            else if (i == NotificationCenter.encryptedChatCreated) {
                if (this.creatingChat) {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$ProfileActivity$90QXGDxq7odO0Woawf3z2MTXklQ(this, array));
                }
            }
            else if (i == NotificationCenter.encryptedChatUpdated) {
                final TLRPC.EncryptedChat currentEncryptedChat = (TLRPC.EncryptedChat)array[0];
                final TLRPC.EncryptedChat currentEncryptedChat2 = this.currentEncryptedChat;
                if (currentEncryptedChat2 != null && currentEncryptedChat.id == currentEncryptedChat2.id) {
                    this.currentEncryptedChat = currentEncryptedChat;
                    this.updateRowsIds();
                    final ListAdapter listAdapter2 = this.listAdapter;
                    if (listAdapter2 != null) {
                        ((RecyclerView.Adapter)listAdapter2).notifyDataSetChanged();
                    }
                }
            }
            else if (i == NotificationCenter.blockedUsersDidLoad) {
                final boolean userBlocked2 = this.userBlocked;
                if (MessagesController.getInstance(super.currentAccount).blockedUsers.indexOfKey(this.user_id) >= 0) {
                    userBlocked = true;
                }
                this.userBlocked = userBlocked;
                if (userBlocked2 != this.userBlocked) {
                    this.createActionBarMenu();
                    this.updateRowsIds();
                    this.listAdapter.notifyDataSetChanged();
                }
            }
            else if (i == NotificationCenter.chatInfoDidLoad) {
                final TLRPC.ChatFull chatInfo = (TLRPC.ChatFull)array[0];
                if (chatInfo.id == this.chat_id) {
                    final boolean booleanValue = (boolean)array[2];
                    final TLRPC.ChatFull chatInfo2 = this.chatInfo;
                    if (chatInfo2 instanceof TLRPC.TL_channelFull && chatInfo.participants == null && chatInfo2 != null) {
                        chatInfo.participants = chatInfo2.participants;
                    }
                    i = n;
                    if (this.chatInfo == null) {
                        i = n;
                        if (chatInfo instanceof TLRPC.TL_channelFull) {
                            i = 1;
                        }
                    }
                    this.chatInfo = chatInfo;
                    if (this.mergeDialogId == 0L) {
                        j = this.chatInfo.migrated_from_chat_id;
                        if (j != 0) {
                            this.mergeDialogId = -j;
                            DataQuery.getInstance(super.currentAccount).getMediaCount(this.mergeDialogId, 0, super.classGuid, true);
                        }
                    }
                    this.fetchUsersFromChannelInfo();
                    this.updateOnlineCount();
                    this.updateRowsIds();
                    final ListAdapter listAdapter3 = this.listAdapter;
                    if (listAdapter3 != null) {
                        ((RecyclerView.Adapter)listAdapter3).notifyDataSetChanged();
                    }
                    final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
                    if (chat != null) {
                        this.currentChat = chat;
                        this.createActionBarMenu();
                    }
                    if (this.currentChat.megagroup && (i != 0 || !booleanValue)) {
                        this.getChannelParticipants(true);
                    }
                }
            }
            else if (i == NotificationCenter.closeChats) {
                this.removeSelfFromStack();
            }
            else if (i == NotificationCenter.botInfoDidLoad) {
                final TLRPC.BotInfo botInfo = (TLRPC.BotInfo)array[0];
                if (botInfo.user_id == this.user_id) {
                    this.botInfo = botInfo;
                    this.updateRowsIds();
                    final ListAdapter listAdapter4 = this.listAdapter;
                    if (listAdapter4 != null) {
                        ((RecyclerView.Adapter)listAdapter4).notifyDataSetChanged();
                    }
                }
            }
            else if (i == NotificationCenter.userInfoDidLoad) {
                if ((int)array[0] == this.user_id) {
                    this.userInfo = (TLRPC.UserFull)array[1];
                    if (!this.openAnimationInProgress && this.callItem == null) {
                        this.createActionBarMenu();
                    }
                    else {
                        this.recreateMenuAfterAnimation = true;
                    }
                    this.updateRowsIds();
                    final ListAdapter listAdapter5 = this.listAdapter;
                    if (listAdapter5 != null) {
                        ((RecyclerView.Adapter)listAdapter5).notifyDataSetChanged();
                    }
                }
            }
            else if (i == NotificationCenter.didReceiveNewMessages) {
                long dialog_id4 = this.dialog_id;
                if (dialog_id4 == 0L) {
                    i = this.user_id;
                    if (i == 0) {
                        i = -this.chat_id;
                    }
                    dialog_id4 = i;
                }
                if (dialog_id4 == (long)array[0]) {
                    final boolean b2 = (int)dialog_id4 == 0;
                    ArrayList list2;
                    MessageObject messageObject;
                    TLRPC.MessageAction action;
                    TLRPC.DecryptedMessageAction encryptedAction;
                    TLRPC.TL_decryptedMessageActionSetMessageTTL tl_decryptedMessageActionSetMessageTTL;
                    ListAdapter listAdapter6;
                    for (list2 = (ArrayList)array[1], i = 0; i < list2.size(); ++i) {
                        messageObject = list2.get(i);
                        if (this.currentEncryptedChat != null) {
                            action = messageObject.messageOwner.action;
                            if (action instanceof TLRPC.TL_messageEncryptedAction) {
                                encryptedAction = action.encryptedAction;
                                if (encryptedAction instanceof TLRPC.TL_decryptedMessageActionSetMessageTTL) {
                                    tl_decryptedMessageActionSetMessageTTL = (TLRPC.TL_decryptedMessageActionSetMessageTTL)encryptedAction;
                                    listAdapter6 = this.listAdapter;
                                    if (listAdapter6 != null) {
                                        ((RecyclerView.Adapter)listAdapter6).notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                        j = DataQuery.getMediaType(messageObject.messageOwner);
                        if (j == -1) {
                            return;
                        }
                        this.sharedMediaData[j].addMessage(messageObject, 0, true, b2);
                    }
                    this.loadMediaCounts();
                }
            }
            else if (i == NotificationCenter.messagesDeleted) {
                i = (int)array[1];
                if (ChatObject.isChannel(this.currentChat)) {
                    if ((i != 0 || this.mergeDialogId == 0L) && i != this.currentChat.id) {
                        return;
                    }
                }
                else if (i != 0) {
                    return;
                }
                final ArrayList list3 = (ArrayList)array[0];
                final int size = list3.size();
                j = 0;
                i = 0;
                while (j < size) {
                    int n6 = 0;
                    while (true) {
                        final MediaActivity.SharedMediaData[] sharedMediaData = this.sharedMediaData;
                        if (n6 >= sharedMediaData.length) {
                            break;
                        }
                        if (sharedMediaData[n6].deleteMessage(list3.get(j), 0)) {
                            i = 1;
                        }
                        ++n6;
                    }
                    ++j;
                }
                if (i != 0) {
                    final MediaActivity mediaActivity = this.mediaActivity;
                    if (mediaActivity != null) {
                        mediaActivity.updateAdapters();
                    }
                }
                this.loadMediaCounts();
            }
        }
    }
    
    @Override
    public void didSelectDialogs(final DialogsActivity dialogsActivity, final ArrayList<Long> list, final CharSequence charSequence, final boolean b) {
        final long longValue = list.get(0);
        final Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        final int n = (int)longValue;
        if (n != 0) {
            if (n > 0) {
                bundle.putInt("user_id", n);
            }
            else if (n < 0) {
                bundle.putInt("chat_id", -n);
            }
        }
        else {
            bundle.putInt("enc_id", (int)(longValue >> 32));
        }
        if (!MessagesController.getInstance(super.currentAccount).checkCanOpenChat(bundle, dialogsActivity)) {
            return;
        }
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        this.presentFragment(new ChatActivity(bundle), true);
        this.removeSelfFromStack();
        SendMessagesHelper.getInstance(super.currentAccount).sendMessage(MessagesController.getInstance(super.currentAccount).getUser(this.user_id), longValue, null, null, null);
    }
    
    public float getAnimationProgress() {
        return this.animationProgress;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI $$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI = new _$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, 0, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.listView, 0, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "avatar_backgroundActionBarBlue"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "avatar_backgroundActionBarBlue"), new ThemeDescription(this.topView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "avatar_backgroundActionBarBlue"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "avatar_actionBarSelectorBlue"), new ThemeDescription(this.nameTextView[1], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "profile_title"), new ThemeDescription(this.nameTextView[1], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "avatar_backgroundActionBarBlue"), new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "profile_status"), new ThemeDescription(this.onlineTextView[1], ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "avatar_subtitleInProfileBlue"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(this.avatarImage, 0, null, null, new Drawable[] { this.avatarDrawable }, null, "avatar_backgroundInProfileBlue"), new ThemeDescription((View)this.writeButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "profile_actionIcon"), new ThemeDescription((View)this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "profile_actionBackground"), new ThemeDescription((View)this.writeButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "profile_actionPressedBackground"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGreenText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText5"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueButton"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteBlueIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { UserCell.class }, new String[] { "adminTextView" }, null, null, null, "profile_creatorIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileActivity$3NoKUcQ9UrSRLPtNP6ehpdoM5qI, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { AboutLinkCell.class }, (Paint)Theme.profile_aboutTextPaint, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[] { AboutLinkCell.class }, (Paint)Theme.profile_aboutTextPaint, null, null, "windowBackgroundWhiteLinkText"), new ThemeDescription((View)this.listView, 0, new Class[] { AboutLinkCell.class }, Theme.linkSelectionPaint, null, null, "windowBackgroundWhiteLinkSelection"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[] { Theme.profile_verifiedCheckDrawable }, null, "profile_verifiedCheck"), new ThemeDescription(this.nameTextView[1], 0, null, null, new Drawable[] { Theme.profile_verifiedDrawable }, null, "profile_verifiedBackground") };
    }
    
    public boolean isChat() {
        return this.chat_id != 0;
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.fixLayout();
    }
    
    @Override
    protected AnimatorSet onCustomTransitionAnimation(final boolean b, final Runnable runnable) {
        if (this.playProfileAnimation && this.allowProfileAnimation) {
            final AnimatorSet set = new AnimatorSet();
            set.setDuration(180L);
            this.listView.setLayerType(2, (Paint)null);
            final ActionBarMenu menu = super.actionBar.createMenu();
            if (menu.getItem(10) == null && this.animatingItem == null) {
                this.animatingItem = menu.addItem(10, 2131165416);
            }
            if (b) {
                final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.onlineTextView[1].getLayoutParams();
                layoutParams.rightMargin = (int)(AndroidUtilities.density * -21.0f + AndroidUtilities.dp(8.0f));
                this.onlineTextView[1].setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                final int n = (int)Math.ceil(AndroidUtilities.displaySize.x - AndroidUtilities.dp(126.0f) + AndroidUtilities.density * 21.0f);
                final float measureText = this.nameTextView[1].getPaint().measureText(this.nameTextView[1].getText().toString());
                final float n2 = (float)this.nameTextView[1].getSideDrawablesSize();
                final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.nameTextView[1].getLayoutParams();
                final float n3 = (float)n;
                if (n3 < measureText * 1.12f + n2) {
                    layoutParams2.width = (int)Math.ceil(n3 / 1.12f);
                }
                else {
                    layoutParams2.width = -2;
                }
                this.nameTextView[1].setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                this.initialAnimationExtraHeight = AndroidUtilities.dp(88.0f);
                super.fragmentView.setBackgroundColor(0);
                this.setAnimationProgress(0.0f);
                final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
                list.add(ObjectAnimator.ofFloat((Object)this, "animationProgress", new float[] { 0.0f, 1.0f }));
                final ImageView writeButton = this.writeButton;
                if (writeButton != null) {
                    writeButton.setScaleX(0.2f);
                    this.writeButton.setScaleY(0.2f);
                    this.writeButton.setAlpha(0.0f);
                    list.add(ObjectAnimator.ofFloat((Object)this.writeButton, View.SCALE_X, new float[] { 1.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)this.writeButton, View.SCALE_Y, new float[] { 1.0f }));
                    list.add(ObjectAnimator.ofFloat((Object)this.writeButton, View.ALPHA, new float[] { 1.0f }));
                }
                for (int i = 0; i < 2; ++i) {
                    final SimpleTextView simpleTextView = this.onlineTextView[i];
                    float alpha;
                    if (i == 0) {
                        alpha = 1.0f;
                    }
                    else {
                        alpha = 0.0f;
                    }
                    simpleTextView.setAlpha(alpha);
                    final SimpleTextView simpleTextView2 = this.nameTextView[i];
                    float alpha2;
                    if (i == 0) {
                        alpha2 = 1.0f;
                    }
                    else {
                        alpha2 = 0.0f;
                    }
                    simpleTextView2.setAlpha(alpha2);
                    final SimpleTextView simpleTextView3 = this.onlineTextView[i];
                    final Property alpha3 = View.ALPHA;
                    float n4;
                    if (i == 0) {
                        n4 = 0.0f;
                    }
                    else {
                        n4 = 1.0f;
                    }
                    list.add(ObjectAnimator.ofFloat((Object)simpleTextView3, alpha3, new float[] { n4 }));
                    final SimpleTextView simpleTextView4 = this.nameTextView[i];
                    final Property alpha4 = View.ALPHA;
                    float n5;
                    if (i == 0) {
                        n5 = 0.0f;
                    }
                    else {
                        n5 = 1.0f;
                    }
                    list.add(ObjectAnimator.ofFloat((Object)simpleTextView4, alpha4, new float[] { n5 }));
                }
                final ActionBarMenuItem animatingItem = this.animatingItem;
                if (animatingItem != null) {
                    animatingItem.setAlpha(1.0f);
                    list.add(ObjectAnimator.ofFloat((Object)this.animatingItem, View.ALPHA, new float[] { 0.0f }));
                }
                final ActionBarMenuItem callItem = this.callItem;
                if (callItem != null) {
                    callItem.setAlpha(0.0f);
                    list.add(ObjectAnimator.ofFloat((Object)this.callItem, View.ALPHA, new float[] { 1.0f }));
                }
                final ActionBarMenuItem editItem = this.editItem;
                if (editItem != null) {
                    editItem.setAlpha(0.0f);
                    list.add(ObjectAnimator.ofFloat((Object)this.editItem, View.ALPHA, new float[] { 1.0f }));
                }
                set.playTogether((Collection)list);
            }
            else {
                this.initialAnimationExtraHeight = this.extraHeight;
                final ArrayList<ObjectAnimator> list2 = new ArrayList<ObjectAnimator>();
                list2.add(ObjectAnimator.ofFloat((Object)this, "animationProgress", new float[] { 1.0f, 0.0f }));
                final ImageView writeButton2 = this.writeButton;
                if (writeButton2 != null) {
                    list2.add(ObjectAnimator.ofFloat((Object)writeButton2, View.SCALE_X, new float[] { 0.2f }));
                    list2.add(ObjectAnimator.ofFloat((Object)this.writeButton, View.SCALE_Y, new float[] { 0.2f }));
                    list2.add(ObjectAnimator.ofFloat((Object)this.writeButton, View.ALPHA, new float[] { 0.0f }));
                }
                for (int j = 0; j < 2; ++j) {
                    final SimpleTextView simpleTextView5 = this.onlineTextView[j];
                    final Property alpha5 = View.ALPHA;
                    float n6;
                    if (j == 0) {
                        n6 = 1.0f;
                    }
                    else {
                        n6 = 0.0f;
                    }
                    list2.add(ObjectAnimator.ofFloat((Object)simpleTextView5, alpha5, new float[] { n6 }));
                    final SimpleTextView simpleTextView6 = this.nameTextView[j];
                    final Property alpha6 = View.ALPHA;
                    float n7;
                    if (j == 0) {
                        n7 = 1.0f;
                    }
                    else {
                        n7 = 0.0f;
                    }
                    list2.add(ObjectAnimator.ofFloat((Object)simpleTextView6, alpha6, new float[] { n7 }));
                }
                final ActionBarMenuItem animatingItem2 = this.animatingItem;
                if (animatingItem2 != null) {
                    animatingItem2.setAlpha(0.0f);
                    list2.add(ObjectAnimator.ofFloat((Object)this.animatingItem, View.ALPHA, new float[] { 1.0f }));
                }
                final ActionBarMenuItem callItem2 = this.callItem;
                if (callItem2 != null) {
                    callItem2.setAlpha(1.0f);
                    list2.add(ObjectAnimator.ofFloat((Object)this.callItem, View.ALPHA, new float[] { 0.0f }));
                }
                final ActionBarMenuItem editItem2 = this.editItem;
                if (editItem2 != null) {
                    editItem2.setAlpha(1.0f);
                    list2.add(ObjectAnimator.ofFloat((Object)this.editItem, View.ALPHA, new float[] { 0.0f }));
                }
                set.playTogether((Collection)list2);
            }
            set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    ProfileActivity.this.listView.setLayerType(0, (Paint)null);
                    if (ProfileActivity.this.animatingItem != null) {
                        ProfileActivity.this.actionBar.createMenu().clearItems();
                        ProfileActivity.this.animatingItem = null;
                    }
                    runnable.run();
                }
            });
            set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
            set.getClass();
            AndroidUtilities.runOnUIThread(new _$$Lambda$V1ckApGFHcFeYW_JU7RAm0ElIv8(set), 50L);
            return set;
        }
        return null;
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            listView.invalidateViews();
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.user_id = super.arguments.getInt("user_id", 0);
        this.chat_id = super.arguments.getInt("chat_id", 0);
        this.banFromGroup = super.arguments.getInt("ban_chat_id", 0);
        if (this.user_id != 0) {
            this.dialog_id = super.arguments.getLong("dialog_id", 0L);
            if (this.dialog_id != 0L) {
                this.currentEncryptedChat = MessagesController.getInstance(super.currentAccount).getEncryptedChat((int)(this.dialog_id >> 32));
            }
            final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            if (user == null) {
                return false;
            }
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.encryptedChatCreated);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.encryptedChatUpdated);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.blockedUsersDidLoad);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.botInfoDidLoad);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.userInfoDidLoad);
            this.userBlocked = (MessagesController.getInstance(super.currentAccount).blockedUsers.indexOfKey(this.user_id) >= 0);
            if (user.bot) {
                this.isBot = true;
                DataQuery.getInstance(super.currentAccount).loadBotInfo(user.id, true, super.classGuid);
            }
            this.userInfo = MessagesController.getInstance(super.currentAccount).getUserFull(this.user_id);
            MessagesController.getInstance(super.currentAccount).loadFullUser(MessagesController.getInstance(super.currentAccount).getUser(this.user_id), super.classGuid, true);
            this.participantsMap = null;
        }
        else {
            if (this.chat_id == 0) {
                return false;
            }
            this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chat_id);
            if (this.currentChat == null) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ProfileActivity$ELtZVLkMORk4xUSfHIm0LgPewE8(this, countDownLatch));
                try {
                    countDownLatch.await();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                if (this.currentChat == null) {
                    return false;
                }
                MessagesController.getInstance(super.currentAccount).putChat(this.currentChat, true);
            }
            if (this.currentChat.megagroup) {
                this.getChannelParticipants(true);
            }
            else {
                this.participantsMap = null;
            }
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatOnlineCountDidLoad);
            this.sortedUsers = new ArrayList<Integer>();
            this.updateOnlineCount();
            if (ChatObject.isChannel(this.currentChat)) {
                MessagesController.getInstance(super.currentAccount).loadFullChat(this.chat_id, super.classGuid, true);
            }
            else if (this.chatInfo == null) {
                MessagesController.getInstance(super.currentAccount).loadChatInfo(this.chat_id, null, false);
            }
            if (this.chatInfo == null) {
                this.chatInfo = this.getMessagesController().getChatFull(this.chat_id);
            }
        }
        this.sharedMediaData = new MediaActivity.SharedMediaData[5];
        int n = 0;
        while (true) {
            final MediaActivity.SharedMediaData[] sharedMediaData = this.sharedMediaData;
            if (n >= sharedMediaData.length) {
                break;
            }
            sharedMediaData[n] = new MediaActivity.SharedMediaData();
            final MediaActivity.SharedMediaData sharedMediaData2 = this.sharedMediaData[n];
            int n2;
            if (this.dialog_id != 0L) {
                n2 = Integer.MIN_VALUE;
            }
            else {
                n2 = Integer.MAX_VALUE;
            }
            sharedMediaData2.setMaxId(0, n2);
            ++n;
        }
        this.loadMediaCounts();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.mediaCountDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.mediaCountsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.mediaDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
        this.updateRowsIds();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.mediaCountDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.mediaCountsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.mediaDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        if (this.user_id != 0) {
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.encryptedChatCreated);
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.encryptedChatUpdated);
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.blockedUsersDidLoad);
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.botInfoDidLoad);
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.userInfoDidLoad);
            MessagesController.getInstance(super.currentAccount).cancelLoadFullUser(this.user_id);
        }
        else if (this.chat_id != 0) {
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatOnlineCountDidLoad);
        }
    }
    
    @Override
    public void onRequestPermissionsResultFragment(final int n, final String[] array, final int[] array2) {
        if (n == 101) {
            final TLRPC.User user = MessagesController.getInstance(super.currentAccount).getUser(this.user_id);
            if (user == null) {
                return;
            }
            if (array2.length > 0 && array2[0] == 0) {
                VoIPHelper.startCall(user, this.getParentActivity(), this.userInfo);
            }
            else {
                VoIPHelper.permissionDenied(this.getParentActivity(), null);
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        this.updateProfileData();
        this.fixLayout();
        final SimpleTextView[] nameTextView = this.nameTextView;
        if (nameTextView[1] != null) {
            this.setParentActivityTitle(nameTextView[1].getText());
        }
    }
    
    @Override
    protected void onTransitionAnimationEnd(final boolean b, final boolean b2) {
        if (b && !b2 && this.playProfileAnimation && this.allowProfileAnimation) {
            this.openAnimationInProgress = false;
            if (this.recreateMenuAfterAnimation) {
                this.createActionBarMenu();
            }
        }
        NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(false);
    }
    
    @Override
    protected void onTransitionAnimationStart(final boolean b, final boolean b2) {
        if (!b2 && this.playProfileAnimation && this.allowProfileAnimation) {
            this.openAnimationInProgress = true;
        }
        NotificationCenter.getInstance(super.currentAccount).setAllowedNotificationsDutingAnimation(new int[] { NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.mediaCountDidLoad, NotificationCenter.mediaCountsDidLoad });
        NotificationCenter.getInstance(super.currentAccount).setAnimationInProgress(true);
    }
    
    @Keep
    public void setAnimationProgress(final float n) {
        this.animationProgress = n;
        this.listView.setAlpha(n);
        this.listView.setTranslationX(AndroidUtilities.dp(48.0f) - AndroidUtilities.dp(48.0f) * n);
        int chat_id;
        if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
            chat_id = this.chat_id;
        }
        else {
            chat_id = 5;
        }
        final int profileBackColorForId = AvatarDrawable.getProfileBackColorForId(chat_id);
        final int color = Theme.getColor("actionBarDefault");
        final int red = Color.red(color);
        final int green = Color.green(color);
        final int blue = Color.blue(color);
        this.topView.setBackgroundColor(Color.rgb(red + (int)((Color.red(profileBackColorForId) - red) * n), green + (int)((Color.green(profileBackColorForId) - green) * n), blue + (int)((Color.blue(profileBackColorForId) - blue) * n)));
        int chat_id2;
        if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
            chat_id2 = this.chat_id;
        }
        else {
            chat_id2 = 5;
        }
        final int iconColorForId = AvatarDrawable.getIconColorForId(chat_id2);
        final int color2 = Theme.getColor("actionBarDefaultIcon");
        final int red2 = Color.red(color2);
        final int green2 = Color.green(color2);
        final int blue2 = Color.blue(color2);
        super.actionBar.setItemsColor(Color.rgb(red2 + (int)((Color.red(iconColorForId) - red2) * n), green2 + (int)((Color.green(iconColorForId) - green2) * n), blue2 + (int)((Color.blue(iconColorForId) - blue2) * n)), false);
        final int color3 = Theme.getColor("profile_title");
        final int color4 = Theme.getColor("actionBarDefaultTitle");
        final int red3 = Color.red(color4);
        final int green3 = Color.green(color4);
        final int blue3 = Color.blue(color4);
        final int alpha = Color.alpha(color4);
        final int n2 = (int)((Color.red(color3) - red3) * n);
        final int n3 = (int)((Color.green(color3) - green3) * n);
        final int n4 = (int)((Color.blue(color3) - blue3) * n);
        final int n5 = (int)((Color.alpha(color3) - alpha) * n);
        for (int i = 0; i < 2; ++i) {
            final SimpleTextView[] nameTextView = this.nameTextView;
            if (nameTextView[i] != null) {
                nameTextView[i].setTextColor(Color.argb(alpha + n5, red3 + n2, green3 + n3, blue3 + n4));
            }
        }
        int n6;
        if (this.isOnline[0]) {
            n6 = Theme.getColor("profile_status");
        }
        else {
            int chat_id3;
            if (this.user_id == 0 && (!ChatObject.isChannel(this.chat_id, super.currentAccount) || this.currentChat.megagroup)) {
                chat_id3 = this.chat_id;
            }
            else {
                chat_id3 = 5;
            }
            n6 = AvatarDrawable.getProfileTextColorForId(chat_id3);
        }
        final boolean[] isOnline = this.isOnline;
        final int n7 = 0;
        String s;
        if (isOnline[0]) {
            s = "chat_status";
        }
        else {
            s = "actionBarDefaultSubtitle";
        }
        final int color5 = Theme.getColor(s);
        final int red4 = Color.red(color5);
        final int green4 = Color.green(color5);
        final int blue4 = Color.blue(color5);
        final int alpha2 = Color.alpha(color5);
        final int n8 = (int)((Color.red(n6) - red4) * n);
        final int n9 = (int)((Color.green(n6) - green4) * n);
        final int n10 = (int)((Color.blue(n6) - blue4) * n);
        final int n11 = (int)((Color.alpha(n6) - alpha2) * n);
        for (int j = n7; j < 2; ++j) {
            final SimpleTextView[] onlineTextView = this.onlineTextView;
            if (onlineTextView[j] != null) {
                onlineTextView[j].setTextColor(Color.argb(alpha2 + n11, red4 + n8, green4 + n9, blue4 + n10));
            }
        }
        this.extraHeight = (int)(this.initialAnimationExtraHeight * n);
        int n12 = this.user_id;
        if (n12 == 0) {
            n12 = this.chat_id;
        }
        final int profileColorForId = AvatarDrawable.getProfileColorForId(n12);
        int n13 = this.user_id;
        if (n13 == 0) {
            n13 = this.chat_id;
        }
        final int colorForId = AvatarDrawable.getColorForId(n13);
        if (profileColorForId != colorForId) {
            this.avatarDrawable.setColor(Color.rgb(Color.red(colorForId) + (int)((Color.red(profileColorForId) - Color.red(colorForId)) * n), Color.green(colorForId) + (int)((Color.green(profileColorForId) - Color.green(colorForId)) * n), Color.blue(colorForId) + (int)((Color.blue(profileColorForId) - Color.blue(colorForId)) * n)));
            this.avatarImage.invalidate();
        }
        this.needLayout();
    }
    
    public void setChatInfo(TLRPC.ChatFull chatInfo) {
        this.chatInfo = chatInfo;
        chatInfo = this.chatInfo;
        if (chatInfo != null) {
            final int migrated_from_chat_id = chatInfo.migrated_from_chat_id;
            if (migrated_from_chat_id != 0 && this.mergeDialogId == 0L) {
                this.mergeDialogId = -migrated_from_chat_id;
                DataQuery.getInstance(super.currentAccount).getMediaCounts(this.mergeDialogId, super.classGuid);
            }
        }
        this.fetchUsersFromChannelInfo();
    }
    
    public void setPlayProfileAnimation(final boolean playProfileAnimation) {
        final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        if (!AndroidUtilities.isTablet() && globalMainSettings.getBoolean("view_animations", true)) {
            this.playProfileAnimation = playProfileAnimation;
        }
    }
    
    public void setUserInfo(final TLRPC.UserFull userInfo) {
        this.userInfo = userInfo;
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return ProfileActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == ProfileActivity.this.infoHeaderRow || n == ProfileActivity.this.sharedHeaderRow || n == ProfileActivity.this.membersHeaderRow) {
                return 1;
            }
            if (n == ProfileActivity.this.phoneRow || n == ProfileActivity.this.usernameRow) {
                return 2;
            }
            if (n == ProfileActivity.this.userInfoRow || n == ProfileActivity.this.channelInfoRow) {
                return 3;
            }
            if (n == ProfileActivity.this.settingsTimerRow || n == ProfileActivity.this.settingsKeyRow || n == ProfileActivity.this.photosRow || n == ProfileActivity.this.filesRow || n == ProfileActivity.this.linksRow || n == ProfileActivity.this.audioRow || n == ProfileActivity.this.voiceRow || n == ProfileActivity.this.groupsInCommonRow || n == ProfileActivity.this.startSecretChatRow || n == ProfileActivity.this.subscribersRow || n == ProfileActivity.this.administratorsRow || n == ProfileActivity.this.blockedUsersRow || n == ProfileActivity.this.leaveChannelRow || n == ProfileActivity.this.addMemberRow || n == ProfileActivity.this.joinRow || n == ProfileActivity.this.unblockRow) {
                return 4;
            }
            if (n == ProfileActivity.this.notificationsDividerRow) {
                return 5;
            }
            if (n == ProfileActivity.this.notificationsRow) {
                return 6;
            }
            if (n == ProfileActivity.this.infoSectionRow || n == ProfileActivity.this.sharedSectionRow || n == ProfileActivity.this.lastSectionRow || n == ProfileActivity.this.membersSectionRow || n == ProfileActivity.this.settingsSectionRow) {
                return 7;
            }
            if (n >= ProfileActivity.this.membersStartRow && n < ProfileActivity.this.membersEndRow) {
                return 8;
            }
            if (n == ProfileActivity.this.emptyRow) {
                return 11;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = true;
            if (itemViewType == 1 || itemViewType == 5 || itemViewType == 7 || itemViewType == 9 || itemViewType == 10 || itemViewType == 11) {
                b = false;
            }
            return b;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int itemViewType = viewHolder.getItemViewType();
            final String s = null;
            final boolean b = false;
            final boolean b2 = false;
            final boolean b3 = false;
            final boolean b4 = false;
            final boolean b5 = false;
            final boolean b6 = false;
            final boolean b7 = false;
            final boolean b8 = false;
            final boolean b9 = false;
            final boolean b10 = false;
            final boolean b11 = false;
            final boolean b12 = false;
            final boolean b13 = false;
            final boolean b14 = false;
            final boolean b15 = true;
            boolean globalNotificationsEnabled = true;
            switch (itemViewType) {
                case 8: {
                    final UserCell userCell = (UserCell)viewHolder.itemView;
                    TLRPC.ChatParticipant chatParticipant;
                    if (!ProfileActivity.this.sortedUsers.isEmpty()) {
                        chatParticipant = ProfileActivity.this.chatInfo.participants.participants.get(ProfileActivity.this.sortedUsers.get(i - ProfileActivity.this.membersStartRow));
                    }
                    else {
                        chatParticipant = ProfileActivity.this.chatInfo.participants.participants.get(i - ProfileActivity.this.membersStartRow);
                    }
                    if (chatParticipant != null) {
                        if (chatParticipant instanceof TLRPC.TL_chatChannelParticipant) {
                            final TLRPC.ChannelParticipant channelParticipant = ((TLRPC.TL_chatChannelParticipant)chatParticipant).channelParticipant;
                            if (channelParticipant instanceof TLRPC.TL_channelParticipantCreator) {
                                userCell.setIsAdmin(1);
                            }
                            else if (channelParticipant instanceof TLRPC.TL_channelParticipantAdmin) {
                                userCell.setIsAdmin(2);
                            }
                            else {
                                userCell.setIsAdmin(0);
                            }
                        }
                        else if (chatParticipant instanceof TLRPC.TL_chatParticipantCreator) {
                            userCell.setIsAdmin(1);
                        }
                        else if (chatParticipant instanceof TLRPC.TL_chatParticipantAdmin) {
                            userCell.setIsAdmin(2);
                        }
                        else {
                            userCell.setIsAdmin(0);
                        }
                        userCell.setData(MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(chatParticipant.user_id), null, null, 0, i != ProfileActivity.this.membersEndRow - 1);
                        break;
                    }
                    break;
                }
                case 7: {
                    final View itemView = viewHolder.itemView;
                    itemView.setTag((Object)i);
                    Drawable drawable = null;
                    Label_0536: {
                        Label_0524: {
                            if ((i != ProfileActivity.this.infoSectionRow || ProfileActivity.this.sharedSectionRow != -1 || ProfileActivity.this.lastSectionRow != -1 || ProfileActivity.this.settingsSectionRow != -1) && (i != ProfileActivity.this.sharedSectionRow || ProfileActivity.this.lastSectionRow != -1) && i != ProfileActivity.this.lastSectionRow) {
                                if (i == ProfileActivity.this.membersSectionRow && ProfileActivity.this.lastSectionRow == -1) {
                                    if (ProfileActivity.this.sharedSectionRow == -1) {
                                        break Label_0524;
                                    }
                                    if (ProfileActivity.this.membersSectionRow > ProfileActivity.this.sharedSectionRow) {
                                        break Label_0524;
                                    }
                                }
                                drawable = Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow");
                                break Label_0536;
                            }
                        }
                        drawable = Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow");
                    }
                    final CombinedDrawable backgroundDrawable = new CombinedDrawable((Drawable)new ColorDrawable(Theme.getColor("windowBackgroundGray")), drawable);
                    backgroundDrawable.setFullsize(true);
                    itemView.setBackgroundDrawable((Drawable)backgroundDrawable);
                    break;
                }
                case 6: {
                    final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell)viewHolder.itemView;
                    if (i == ProfileActivity.this.notificationsRow) {
                        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(ProfileActivity.this.currentAccount);
                        long access$8100;
                        if (ProfileActivity.this.dialog_id != 0L) {
                            access$8100 = ProfileActivity.this.dialog_id;
                        }
                        else {
                            if (ProfileActivity.this.user_id != 0) {
                                i = ProfileActivity.this.user_id;
                            }
                            else {
                                i = -ProfileActivity.this.chat_id;
                            }
                            access$8100 = i;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("custom_");
                        sb.append(access$8100);
                        final boolean boolean1 = notificationsSettings.getBoolean(sb.toString(), false);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("notify2_");
                        sb2.append(access$8100);
                        final boolean contains = notificationsSettings.contains(sb2.toString());
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("notify2_");
                        sb3.append(access$8100);
                        final int int1 = notificationsSettings.getInt(sb3.toString(), 0);
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("notifyuntil_");
                        sb4.append(access$8100);
                        i = notificationsSettings.getInt(sb4.toString(), 0);
                        String s2;
                        if (int1 == 3 && i != Integer.MAX_VALUE) {
                            i -= ConnectionsManager.getInstance(ProfileActivity.this.currentAccount).getCurrentTime();
                            if (i <= 0) {
                                if (boolean1) {
                                    s2 = LocaleController.getString("NotificationsCustom", 2131560059);
                                }
                                else {
                                    s2 = LocaleController.getString("NotificationsOn", 2131560080);
                                }
                            }
                            else {
                                if (i < 3600) {
                                    s2 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Minutes", i / 60));
                                }
                                else if (i < 86400) {
                                    s2 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Hours", (int)Math.ceil(i / 60.0f / 60.0f)));
                                }
                                else {
                                    s2 = s;
                                    if (i < 31536000) {
                                        s2 = LocaleController.formatString("WillUnmuteIn", 2131561122, LocaleController.formatPluralString("Days", (int)Math.ceil(i / 60.0f / 60.0f / 24.0f)));
                                    }
                                }
                                globalNotificationsEnabled = false;
                            }
                        }
                        else {
                            if (int1 == 0) {
                                if (contains) {
                                    globalNotificationsEnabled = b15;
                                }
                                else {
                                    globalNotificationsEnabled = NotificationsController.getInstance(ProfileActivity.this.currentAccount).isGlobalNotificationsEnabled(access$8100);
                                }
                            }
                            else {
                                globalNotificationsEnabled = (int1 == 1 && b15);
                            }
                            if (globalNotificationsEnabled && boolean1) {
                                s2 = LocaleController.getString("NotificationsCustom", 2131560059);
                            }
                            else {
                                String s3;
                                if (globalNotificationsEnabled) {
                                    i = 2131560080;
                                    s3 = "NotificationsOn";
                                }
                                else {
                                    i = 2131560078;
                                    s3 = "NotificationsOff";
                                }
                                s2 = LocaleController.getString(s3, i);
                            }
                        }
                        String string = s2;
                        if (s2 == null) {
                            string = LocaleController.getString("NotificationsOff", 2131560078);
                        }
                        notificationsCheckCell.setTextAndValueAndCheck(LocaleController.getString("Notifications", 2131560055), string, globalNotificationsEnabled, false);
                        break;
                    }
                    break;
                }
                case 4: {
                    final TextCell textCell = (TextCell)viewHolder.itemView;
                    textCell.setColors("windowBackgroundWhiteGrayIcon", "windowBackgroundWhiteBlackText");
                    textCell.setTag((Object)"windowBackgroundWhiteBlackText");
                    if (i == ProfileActivity.this.photosRow) {
                        final String string2 = LocaleController.getString("SharedPhotosAndVideos", 2131560769);
                        final String format = String.format("%d", ProfileActivity.this.lastMediaCount[0]);
                        boolean b16 = b14;
                        if (i != ProfileActivity.this.sharedSectionRow - 1) {
                            b16 = true;
                        }
                        textCell.setTextAndValueAndIcon(string2, format, 2131165792, b16);
                        break;
                    }
                    if (i == ProfileActivity.this.filesRow) {
                        final String string3 = LocaleController.getString("FilesDataUsage", 2131559483);
                        final String format2 = String.format("%d", ProfileActivity.this.lastMediaCount[1]);
                        boolean b17 = b;
                        if (i != ProfileActivity.this.sharedSectionRow - 1) {
                            b17 = true;
                        }
                        textCell.setTextAndValueAndIcon(string3, format2, 2131165785, b17);
                        break;
                    }
                    if (i == ProfileActivity.this.linksRow) {
                        final String string4 = LocaleController.getString("SharedLinks", 2131560764);
                        final String format3 = String.format("%d", ProfileActivity.this.lastMediaCount[3]);
                        boolean b18 = b2;
                        if (i != ProfileActivity.this.sharedSectionRow - 1) {
                            b18 = true;
                        }
                        textCell.setTextAndValueAndIcon(string4, format3, 2131165787, b18);
                        break;
                    }
                    if (i == ProfileActivity.this.audioRow) {
                        final String string5 = LocaleController.getString("SharedAudioFiles", 2131560760);
                        final String format4 = String.format("%d", ProfileActivity.this.lastMediaCount[4]);
                        boolean b19 = b3;
                        if (i != ProfileActivity.this.sharedSectionRow - 1) {
                            b19 = true;
                        }
                        textCell.setTextAndValueAndIcon(string5, format4, 2131165783, b19);
                        break;
                    }
                    if (i == ProfileActivity.this.voiceRow) {
                        final String string6 = LocaleController.getString("AudioAutodownload", 2131558735);
                        final String format5 = String.format("%d", ProfileActivity.this.lastMediaCount[2]);
                        boolean b20 = b4;
                        if (i != ProfileActivity.this.sharedSectionRow - 1) {
                            b20 = true;
                        }
                        textCell.setTextAndValueAndIcon(string6, format5, 2131165793, b20);
                        break;
                    }
                    if (i == ProfileActivity.this.groupsInCommonRow) {
                        final String string7 = LocaleController.getString("GroupsInCommonTitle", 2131559627);
                        final String format6 = String.format("%d", ProfileActivity.this.userInfo.common_chats_count);
                        boolean b21 = b5;
                        if (i != ProfileActivity.this.sharedSectionRow - 1) {
                            b21 = true;
                        }
                        textCell.setTextAndValueAndIcon(string7, format6, 2131165277, b21);
                        break;
                    }
                    if (i == ProfileActivity.this.settingsTimerRow) {
                        i = MessagesController.getInstance(ProfileActivity.this.currentAccount).getEncryptedChat((int)(ProfileActivity.this.dialog_id >> 32)).ttl;
                        String s4;
                        if (i == 0) {
                            s4 = LocaleController.getString("ShortMessageLifetimeForever", 2131560776);
                        }
                        else {
                            s4 = LocaleController.formatTTLString(i);
                        }
                        textCell.setTextAndValue(LocaleController.getString("MessageLifetime", 2131559846), s4, false);
                        break;
                    }
                    if (i == ProfileActivity.this.unblockRow) {
                        textCell.setText(LocaleController.getString("Unblock", 2131560932), false);
                        textCell.setColors(null, "windowBackgroundWhiteRedText5");
                        break;
                    }
                    if (i == ProfileActivity.this.startSecretChatRow) {
                        textCell.setText(LocaleController.getString("StartEncryptedChat", 2131560803), false);
                        textCell.setColors(null, "windowBackgroundWhiteGreenText2");
                        break;
                    }
                    if (i == ProfileActivity.this.settingsKeyRow) {
                        final IdenticonDrawable identiconDrawable = new IdenticonDrawable();
                        identiconDrawable.setEncryptedChat(MessagesController.getInstance(ProfileActivity.this.currentAccount).getEncryptedChat((int)(ProfileActivity.this.dialog_id >> 32)));
                        textCell.setTextAndValueDrawable(LocaleController.getString("EncryptionKey", 2131559360), identiconDrawable, false);
                        break;
                    }
                    if (i == ProfileActivity.this.leaveChannelRow) {
                        textCell.setColors(null, "windowBackgroundWhiteRedText5");
                        textCell.setText(LocaleController.getString("LeaveChannel", 2131559744), false);
                        break;
                    }
                    if (i == ProfileActivity.this.joinRow) {
                        textCell.setColors(null, "windowBackgroundWhiteBlueText2");
                        textCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText2"));
                        if (ProfileActivity.this.currentChat.megagroup) {
                            textCell.setText(LocaleController.getString("ProfileJoinGroup", 2131560513), false);
                            break;
                        }
                        textCell.setText(LocaleController.getString("ProfileJoinChannel", 2131560512), false);
                        break;
                    }
                    else if (i == ProfileActivity.this.subscribersRow) {
                        if (ProfileActivity.this.chatInfo != null) {
                            if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup) {
                                final String string8 = LocaleController.getString("ChannelSubscribers", 2131559004);
                                final String format7 = String.format("%d", ProfileActivity.this.chatInfo.participants_count);
                                boolean b22 = b6;
                                if (i != ProfileActivity.this.membersSectionRow - 1) {
                                    b22 = true;
                                }
                                textCell.setTextAndValueAndIcon(string8, format7, 2131165277, b22);
                                break;
                            }
                            final String string9 = LocaleController.getString("ChannelMembers", 2131558962);
                            final String format8 = String.format("%d", ProfileActivity.this.chatInfo.participants_count);
                            boolean b23 = b7;
                            if (i != ProfileActivity.this.membersSectionRow - 1) {
                                b23 = true;
                            }
                            textCell.setTextAndValueAndIcon(string9, format8, 2131165277, b23);
                            break;
                        }
                        else {
                            if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup) {
                                final String string10 = LocaleController.getString("ChannelSubscribers", 2131559004);
                                boolean b24 = b8;
                                if (i != ProfileActivity.this.membersSectionRow - 1) {
                                    b24 = true;
                                }
                                textCell.setTextAndIcon(string10, 2131165277, b24);
                                break;
                            }
                            final String string11 = LocaleController.getString("ChannelMembers", 2131558962);
                            boolean b25 = b9;
                            if (i != ProfileActivity.this.membersSectionRow - 1) {
                                b25 = true;
                            }
                            textCell.setTextAndIcon(string11, 2131165277, b25);
                            break;
                        }
                    }
                    else if (i == ProfileActivity.this.administratorsRow) {
                        if (ProfileActivity.this.chatInfo != null) {
                            final String string12 = LocaleController.getString("ChannelAdministrators", 2131558927);
                            final String format9 = String.format("%d", ProfileActivity.this.chatInfo.admins_count);
                            boolean b26 = b10;
                            if (i != ProfileActivity.this.membersSectionRow - 1) {
                                b26 = true;
                            }
                            textCell.setTextAndValueAndIcon(string12, format9, 2131165271, b26);
                            break;
                        }
                        final String string13 = LocaleController.getString("ChannelAdministrators", 2131558927);
                        boolean b27 = b11;
                        if (i != ProfileActivity.this.membersSectionRow - 1) {
                            b27 = true;
                        }
                        textCell.setTextAndIcon(string13, 2131165271, b27);
                        break;
                    }
                    else if (i == ProfileActivity.this.blockedUsersRow) {
                        if (ProfileActivity.this.chatInfo != null) {
                            final String string14 = LocaleController.getString("ChannelBlacklist", 2131558932);
                            final String format10 = String.format("%d", Math.max(ProfileActivity.this.chatInfo.banned_count, ProfileActivity.this.chatInfo.kicked_count));
                            boolean b28 = b12;
                            if (i != ProfileActivity.this.membersSectionRow - 1) {
                                b28 = true;
                            }
                            textCell.setTextAndValueAndIcon(string14, format10, 2131165275, b28);
                            break;
                        }
                        final String string15 = LocaleController.getString("ChannelBlacklist", 2131558932);
                        boolean b29 = b13;
                        if (i != ProfileActivity.this.membersSectionRow - 1) {
                            b29 = true;
                        }
                        textCell.setTextAndIcon(string15, 2131165275, b29);
                        break;
                    }
                    else {
                        if (i != ProfileActivity.this.addMemberRow) {
                            break;
                        }
                        textCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                        if (ProfileActivity.this.chat_id > 0) {
                            textCell.setTextAndIcon(LocaleController.getString("AddMember", 2131558573), 2131165272, true);
                            break;
                        }
                        textCell.setTextAndIcon(LocaleController.getString("AddRecipient", 2131558582), 2131165272, true);
                        break;
                    }
                    break;
                }
                case 3: {
                    final AboutLinkCell aboutLinkCell = (AboutLinkCell)viewHolder.itemView;
                    if (i == ProfileActivity.this.userInfoRow) {
                        aboutLinkCell.setTextAndValue(ProfileActivity.this.userInfo.about, LocaleController.getString("UserBio", 2131560987), ProfileActivity.this.isBot);
                        break;
                    }
                    if (i == ProfileActivity.this.channelInfoRow) {
                        String s5;
                        for (s5 = ProfileActivity.this.chatInfo.about; s5.contains("\n\n\n"); s5 = s5.replace("\n\n\n", "\n\n")) {}
                        aboutLinkCell.setText(s5, true);
                        break;
                    }
                    break;
                }
                case 2: {
                    final TextDetailCell textDetailCell = (TextDetailCell)viewHolder.itemView;
                    if (i == ProfileActivity.this.phoneRow) {
                        final TLRPC.User user = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                        String s6;
                        if (!TextUtils.isEmpty((CharSequence)user.phone)) {
                            final PhoneFormat instance = PhoneFormat.getInstance();
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("+");
                            sb5.append(user.phone);
                            s6 = instance.format(sb5.toString());
                        }
                        else {
                            s6 = LocaleController.getString("PhoneHidden", 2131560424);
                        }
                        textDetailCell.setTextAndValue(s6, LocaleController.getString("PhoneMobile", 2131560427), false);
                        break;
                    }
                    if (i != ProfileActivity.this.usernameRow) {
                        break;
                    }
                    if (ProfileActivity.this.user_id != 0) {
                        final TLRPC.User user2 = MessagesController.getInstance(ProfileActivity.this.currentAccount).getUser(ProfileActivity.this.user_id);
                        String string16;
                        if (user2 != null && !TextUtils.isEmpty((CharSequence)user2.username)) {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("@");
                            sb6.append(user2.username);
                            string16 = sb6.toString();
                        }
                        else {
                            string16 = "-";
                        }
                        textDetailCell.setTextAndValue(string16, LocaleController.getString("Username", 2131561021), false);
                        break;
                    }
                    if (ProfileActivity.this.currentChat != null) {
                        final TLRPC.Chat chat = MessagesController.getInstance(ProfileActivity.this.currentAccount).getChat(ProfileActivity.this.chat_id);
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append(MessagesController.getInstance(ProfileActivity.this.currentAccount).linkPrefix);
                        sb7.append("/");
                        sb7.append(chat.username);
                        textDetailCell.setTextAndValue(sb7.toString(), LocaleController.getString("InviteLink", 2131559679), false);
                        break;
                    }
                    break;
                }
                case 1: {
                    final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                    if (i == ProfileActivity.this.infoHeaderRow) {
                        if (ChatObject.isChannel(ProfileActivity.this.currentChat) && !ProfileActivity.this.currentChat.megagroup && ProfileActivity.this.channelInfoRow != -1) {
                            headerCell.setText(LocaleController.getString("ReportChatDescription", 2131560570));
                            break;
                        }
                        headerCell.setText(LocaleController.getString("Info", 2131559665));
                        break;
                    }
                    else {
                        if (i == ProfileActivity.this.sharedHeaderRow) {
                            headerCell.setText(LocaleController.getString("SharedContent", 2131560761));
                            break;
                        }
                        if (i == ProfileActivity.this.membersHeaderRow) {
                            headerCell.setText(LocaleController.getString("ChannelMembers", 2131558962));
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int n) {
            Object o = null;
            if (n != 11) {
                switch (n) {
                    default: {
                        o = null;
                        break;
                    }
                    case 8: {
                        final Context mContext = this.mContext;
                        if (ProfileActivity.this.addMemberRow == -1) {
                            n = 9;
                        }
                        else {
                            n = 6;
                        }
                        o = new UserCell(mContext, n, 0, true);
                        break;
                    }
                    case 7: {
                        o = new ShadowSectionCell(this.mContext);
                        break;
                    }
                    case 6: {
                        o = new NotificationsCheckCell(this.mContext, 23, 70);
                        break;
                    }
                    case 5: {
                        o = new DividerCell(this.mContext);
                        ((View)o).setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(4.0f), 0, 0);
                        break;
                    }
                    case 4: {
                        o = new TextCell(this.mContext);
                        break;
                    }
                    case 3: {
                        o = new AboutLinkCell(this.mContext) {
                            @Override
                            protected void didPressUrl(final String searchString) {
                                if (searchString.startsWith("@")) {
                                    MessagesController.getInstance(ProfileActivity.this.currentAccount).openByUserName(searchString.substring(1), ProfileActivity.this, 0);
                                }
                                else if (searchString.startsWith("#")) {
                                    final DialogsActivity dialogsActivity = new DialogsActivity(null);
                                    dialogsActivity.setSearchString(searchString);
                                    ProfileActivity.this.presentFragment(dialogsActivity);
                                }
                                else if (searchString.startsWith("/") && ProfileActivity.this.parentLayout.fragmentsStack.size() > 1) {
                                    final BaseFragment baseFragment = ProfileActivity.this.parentLayout.fragmentsStack.get(ProfileActivity.this.parentLayout.fragmentsStack.size() - 2);
                                    if (baseFragment instanceof ChatActivity) {
                                        ProfileActivity.this.finishFragment();
                                        ((ChatActivity)baseFragment).chatActivityEnterView.setCommand(null, searchString, false, false);
                                    }
                                }
                            }
                        };
                        break;
                    }
                    case 2: {
                        o = new TextDetailCell(this.mContext);
                        break;
                    }
                    case 1: {
                        o = new HeaderCell(this.mContext, 23);
                        break;
                    }
                }
            }
            else {
                o = new EmptyCell(this.mContext, 36);
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    private class TopView extends View
    {
        private int currentColor;
        private Paint paint;
        
        public TopView(final Context context) {
            super(context);
            this.paint = new Paint();
        }
        
        protected void onDraw(final Canvas canvas) {
            final int n = this.getMeasuredHeight() - AndroidUtilities.dp(91.0f);
            canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)(ProfileActivity.this.extraHeight + n), this.paint);
            if (ProfileActivity.this.parentLayout != null) {
                ProfileActivity.this.parentLayout.drawHeaderShadow(canvas, n + ProfileActivity.this.extraHeight);
            }
        }
        
        protected void onMeasure(int statusBarHeight, int size) {
            size = View$MeasureSpec.getSize(statusBarHeight);
            final int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
            if (ProfileActivity.this.actionBar.getOccupyStatusBar()) {
                statusBarHeight = AndroidUtilities.statusBarHeight;
            }
            else {
                statusBarHeight = 0;
            }
            this.setMeasuredDimension(size, currentActionBarHeight + statusBarHeight + AndroidUtilities.dp(91.0f));
        }
        
        public void setBackgroundColor(final int color) {
            if (color != this.currentColor) {
                this.paint.setColor(color);
                this.invalidate();
            }
        }
    }
}
