// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.Intent;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.FileLog;
import org.telegram.ui.Cells.RadioButtonCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.ActionBar.BottomSheet;
import android.view.KeyEvent;
import android.widget.TextView;
import org.telegram.ui.Components.AlertsCreator;
import android.content.DialogInterface;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.messenger.ImageLocation;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView$OnEditorActionListener;
import android.graphics.drawable.Drawable;
import android.text.InputFilter$LengthFilter;
import android.text.InputFilter;
import android.widget.ImageView$ScaleType;
import android.view.View$OnClickListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.ScrollView;
import org.telegram.ui.ActionBar.Theme;
import android.view.View$OnTouchListener;
import android.view.View$MeasureSpec;
import android.widget.FrameLayout$LayoutParams;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import android.text.TextUtils;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.content.DialogInterface$OnCancelListener;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.AndroidUtilities;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.messenger.ChatObject;
import org.telegram.ui.ActionBar.ActionBar;
import android.os.Bundle;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.EditTextEmoji;
import org.telegram.ui.Cells.TextDetailCell;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.widget.FrameLayout;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.RadialProgressView;
import android.view.View;
import org.telegram.ui.Components.BackupImageView;
import android.widget.ImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.LinearLayout;
import android.animation.AnimatorSet;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.TextCell;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChatEditActivity extends BaseFragment implements ImageUpdaterDelegate, NotificationCenterDelegate
{
    private static final int done_button = 1;
    private TextCell adminCell;
    private TLRPC.FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private TLRPC.FileLocation avatarBig;
    private LinearLayout avatarContainer;
    private AvatarDrawable avatarDrawable;
    private ImageView avatarEditor;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private TextCell blockCell;
    private int chatId;
    private boolean createAfterUpload;
    private TLRPC.Chat currentChat;
    private TextSettingsCell deleteCell;
    private FrameLayout deleteContainer;
    private ShadowSectionCell deleteInfoCell;
    private EditTextBoldCursor descriptionTextView;
    private View doneButton;
    private boolean donePressed;
    private TextDetailCell historyCell;
    private boolean historyHidden;
    private ImageUpdater imageUpdater;
    private TLRPC.ChatFull info;
    private LinearLayout infoContainer;
    private ShadowSectionCell infoSectionCell;
    private boolean isChannel;
    private TextDetailCell linkedCell;
    private TextCell logCell;
    private TextCell membersCell;
    private EditTextEmoji nameTextView;
    private AlertDialog progressDialog;
    private LinearLayout settingsContainer;
    private ShadowSectionCell settingsSectionCell;
    private ShadowSectionCell settingsTopSectionCell;
    private TextCheckCell signCell;
    private boolean signMessages;
    private TextSettingsCell stickersCell;
    private FrameLayout stickersContainer;
    private TextInfoPrivacyCell stickersInfoCell3;
    private TextDetailCell typeCell;
    private LinearLayout typeEditContainer;
    private TLRPC.InputFile uploadedAvatar;
    
    public ChatEditActivity(final Bundle bundle) {
        super(bundle);
        this.avatarDrawable = new AvatarDrawable();
        this.imageUpdater = new ImageUpdater();
        this.chatId = bundle.getInt("chat_id", 0);
    }
    
    private boolean checkDiscard() {
        final TLRPC.ChatFull info = this.info;
        String about = null;
        Label_0024: {
            if (info != null) {
                about = info.about;
                if (about != null) {
                    break Label_0024;
                }
            }
            about = "";
        }
        if ((this.info == null || !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory == this.historyHidden) && this.imageUpdater.uploadingImage == null) {
            final EditTextEmoji nameTextView = this.nameTextView;
            if (nameTextView == null || this.currentChat.title.equals(nameTextView.getText().toString())) {
                final EditTextBoldCursor descriptionTextView = this.descriptionTextView;
                if (descriptionTextView == null || about.equals(descriptionTextView.getText().toString())) {
                    final boolean signMessages = this.signMessages;
                    final TLRPC.Chat currentChat = this.currentChat;
                    if (signMessages == currentChat.signatures && this.uploadedAvatar == null) {
                        if (this.avatar != null || !(currentChat.photo instanceof TLRPC.TL_chatPhoto)) {
                            return true;
                        }
                    }
                }
            }
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", 2131560995));
        if (this.isChannel) {
            builder.setMessage(LocaleController.getString("ChannelSettingsChangedAlert", 2131558999));
        }
        else {
            builder.setMessage(LocaleController.getString("GroupSettingsChangedAlert", 2131559613));
        }
        builder.setPositiveButton(LocaleController.getString("ApplyTheme", 2131558639), (DialogInterface$OnClickListener)new _$$Lambda$ChatEditActivity$42WGB1bZqU27h5UDp3vuD_usGEg(this));
        builder.setNegativeButton(LocaleController.getString("PassportDiscard", 2131560212), (DialogInterface$OnClickListener)new _$$Lambda$ChatEditActivity$NBEr6CX4NZ1r3XbdnOXbearPc6k(this));
        this.showDialog(builder.create());
        return false;
    }
    
    private int getAdminCount() {
        final TLRPC.ChatFull info = this.info;
        if (info == null) {
            return 1;
        }
        final int size = info.participants.participants.size();
        int i = 0;
        int n = 0;
        while (i < size) {
            final TLRPC.ChatParticipant chatParticipant = this.info.participants.participants.get(i);
            int n2 = 0;
            Label_0074: {
                if (!(chatParticipant instanceof TLRPC.TL_chatParticipantAdmin)) {
                    n2 = n;
                    if (!(chatParticipant instanceof TLRPC.TL_chatParticipantCreator)) {
                        break Label_0074;
                    }
                }
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    private void processDone() {
        if (!this.donePressed) {
            final EditTextEmoji nameTextView = this.nameTextView;
            if (nameTextView != null) {
                if (nameTextView.length() == 0) {
                    final Vibrator vibrator = (Vibrator)this.getParentActivity().getSystemService("vibrator");
                    if (vibrator != null) {
                        vibrator.vibrate(200L);
                    }
                    AndroidUtilities.shakeView((View)this.nameTextView, 2.0f, 0);
                    return;
                }
                this.donePressed = true;
                if (!ChatObject.isChannel(this.currentChat) && !this.historyHidden) {
                    MessagesController.getInstance(super.currentAccount).convertToMegaGroup((Context)this.getParentActivity(), this.chatId, new _$$Lambda$ChatEditActivity$FM5TsTTFL8A_rr_SPaVMYlNXC2Q(this));
                    return;
                }
                if (this.info != null && ChatObject.isChannel(this.currentChat)) {
                    final TLRPC.ChatFull info = this.info;
                    final boolean hidden_prehistory = info.hidden_prehistory;
                    final boolean historyHidden = this.historyHidden;
                    if (hidden_prehistory != historyHidden) {
                        info.hidden_prehistory = historyHidden;
                        MessagesController.getInstance(super.currentAccount).toogleChannelInvitesHistory(this.chatId, this.historyHidden);
                    }
                }
                if (this.imageUpdater.uploadingImage != null) {
                    this.createAfterUpload = true;
                    (this.progressDialog = new AlertDialog((Context)this.getParentActivity(), 3)).setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$ChatEditActivity$G_WbIT_ViCCFZMn6b9uYoBS_uJ0(this));
                    this.progressDialog.show();
                    return;
                }
                if (!this.currentChat.title.equals(this.nameTextView.getText().toString())) {
                    MessagesController.getInstance(super.currentAccount).changeChatTitle(this.chatId, this.nameTextView.getText().toString());
                }
                final TLRPC.ChatFull info2 = this.info;
                String about = null;
                Label_0295: {
                    if (info2 != null) {
                        about = info2.about;
                        if (about != null) {
                            break Label_0295;
                        }
                    }
                    about = "";
                }
                final EditTextBoldCursor descriptionTextView = this.descriptionTextView;
                if (descriptionTextView != null && !about.equals(descriptionTextView.getText().toString())) {
                    MessagesController.getInstance(super.currentAccount).updateChatAbout(this.chatId, this.descriptionTextView.getText().toString(), this.info);
                }
                final boolean signMessages = this.signMessages;
                final TLRPC.Chat currentChat = this.currentChat;
                if (signMessages != currentChat.signatures) {
                    currentChat.signatures = true;
                    MessagesController.getInstance(super.currentAccount).toogleChannelSignatures(this.chatId, this.signMessages);
                }
                if (this.uploadedAvatar != null) {
                    MessagesController.getInstance(super.currentAccount).changeChatAvatar(this.chatId, this.uploadedAvatar, this.avatar, this.avatarBig);
                }
                else if (this.avatar == null && this.currentChat.photo instanceof TLRPC.TL_chatPhoto) {
                    MessagesController.getInstance(super.currentAccount).changeChatAvatar(this.chatId, null, null, null);
                }
                this.finishFragment();
            }
        }
    }
    
    private void showAvatarProgress(final boolean b, final boolean b2) {
        if (this.avatarEditor == null) {
            return;
        }
        final AnimatorSet avatarAnimation = this.avatarAnimation;
        if (avatarAnimation != null) {
            avatarAnimation.cancel();
            this.avatarAnimation = null;
        }
        if (b2) {
            this.avatarAnimation = new AnimatorSet();
            if (b) {
                this.avatarProgressView.setVisibility(0);
                this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarEditor, View.ALPHA, new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 1.0f }) });
            }
            else {
                this.avatarEditor.setVisibility(0);
                this.avatarAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.avatarEditor, View.ALPHA, new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.avatarProgressView, View.ALPHA, new float[] { 0.0f }) });
            }
            this.avatarAnimation.setDuration(180L);
            this.avatarAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationCancel(final Animator animator) {
                    ChatEditActivity.this.avatarAnimation = null;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    if (ChatEditActivity.this.avatarAnimation != null) {
                        if (ChatEditActivity.this.avatarEditor != null) {
                            if (b) {
                                ChatEditActivity.this.avatarEditor.setVisibility(4);
                            }
                            else {
                                ChatEditActivity.this.avatarProgressView.setVisibility(4);
                            }
                            ChatEditActivity.this.avatarAnimation = null;
                        }
                    }
                }
            });
            this.avatarAnimation.start();
        }
        else if (b) {
            this.avatarEditor.setAlpha(1.0f);
            this.avatarEditor.setVisibility(4);
            this.avatarProgressView.setAlpha(1.0f);
            this.avatarProgressView.setVisibility(0);
        }
        else {
            this.avatarEditor.setAlpha(1.0f);
            this.avatarEditor.setVisibility(0);
            this.avatarProgressView.setAlpha(0.0f);
            this.avatarProgressView.setVisibility(4);
        }
    }
    
    private void updateFields(final boolean b) {
        if (b) {
            final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
            if (chat != null) {
                this.currentChat = chat;
            }
        }
        final boolean empty = TextUtils.isEmpty((CharSequence)this.currentChat.username);
        final TextDetailCell historyCell = this.historyCell;
        if (historyCell != null) {
            int visibility = 0;
            Label_0081: {
                if (empty) {
                    final TLRPC.ChatFull info = this.info;
                    if (info == null || info.linked_chat_id == 0) {
                        visibility = 0;
                        break Label_0081;
                    }
                }
                visibility = 8;
            }
            historyCell.setVisibility(visibility);
        }
        final ShadowSectionCell settingsSectionCell = this.settingsSectionCell;
        if (settingsSectionCell != null) {
            int visibility2 = 0;
            Label_0143: {
                if (this.signCell == null && this.typeCell == null && this.linkedCell == null) {
                    final TextDetailCell historyCell2 = this.historyCell;
                    if (historyCell2 == null || historyCell2.getVisibility() != 0) {
                        visibility2 = 8;
                        break Label_0143;
                    }
                }
                visibility2 = 0;
            }
            settingsSectionCell.setVisibility(visibility2);
        }
        final TextCell logCell = this.logCell;
        if (logCell != null) {
            int visibility3 = 0;
            Label_0200: {
                if (this.currentChat.megagroup) {
                    final TLRPC.ChatFull info2 = this.info;
                    if (info2 == null || info2.participants_count <= 200) {
                        visibility3 = 8;
                        break Label_0200;
                    }
                }
                visibility3 = 0;
            }
            logCell.setVisibility(visibility3);
        }
        if (this.typeCell != null) {
            String s2;
            if (this.isChannel) {
                int n;
                String s;
                if (empty) {
                    n = 2131560922;
                    s = "TypePrivate";
                }
                else {
                    n = 2131560924;
                    s = "TypePublic";
                }
                s2 = LocaleController.getString(s, n);
            }
            else {
                int n2;
                String s3;
                if (empty) {
                    n2 = 2131560923;
                    s3 = "TypePrivateGroup";
                }
                else {
                    n2 = 2131560925;
                    s3 = "TypePublicGroup";
                }
                s2 = LocaleController.getString(s3, n2);
            }
            if (this.isChannel) {
                this.typeCell.setTextAndValue(LocaleController.getString("ChannelType", 2131559005), s2, true);
            }
            else {
                this.typeCell.setTextAndValue(LocaleController.getString("GroupType", 2131559617), s2, true);
            }
        }
        if (this.linkedCell != null) {
            final TLRPC.ChatFull info3 = this.info;
            if (info3 != null && (this.isChannel || info3.linked_chat_id != 0)) {
                this.linkedCell.setVisibility(0);
                if (this.info.linked_chat_id == 0) {
                    this.linkedCell.setTextAndValue(LocaleController.getString("Discussion", 2131559280), LocaleController.getString("DiscussionInfo", 2131559287), true);
                }
                else {
                    final TLRPC.Chat chat2 = this.getMessagesController().getChat(this.info.linked_chat_id);
                    if (chat2 == null) {
                        this.linkedCell.setVisibility(8);
                    }
                    else if (this.isChannel) {
                        if (TextUtils.isEmpty((CharSequence)chat2.username)) {
                            this.linkedCell.setTextAndValue(LocaleController.getString("Discussion", 2131559280), chat2.title, true);
                        }
                        else {
                            final TextDetailCell linkedCell = this.linkedCell;
                            final String string = LocaleController.getString("Discussion", 2131559280);
                            final StringBuilder sb = new StringBuilder();
                            sb.append("@");
                            sb.append(chat2.username);
                            linkedCell.setTextAndValue(string, sb.toString(), true);
                        }
                    }
                    else if (TextUtils.isEmpty((CharSequence)chat2.username)) {
                        this.linkedCell.setTextAndValue(LocaleController.getString("LinkedChannel", 2131559763), chat2.title, false);
                    }
                    else {
                        final TextDetailCell linkedCell2 = this.linkedCell;
                        final String string2 = LocaleController.getString("LinkedChannel", 2131559763);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("@");
                        sb2.append(chat2.username);
                        linkedCell2.setTextAndValue(string2, sb2.toString(), false);
                    }
                }
            }
            else {
                this.linkedCell.setVisibility(8);
            }
        }
        if (this.info != null && this.historyCell != null) {
            int n3;
            String s4;
            if (this.historyHidden) {
                n3 = 2131559034;
                s4 = "ChatHistoryHidden";
            }
            else {
                n3 = 2131559037;
                s4 = "ChatHistoryVisible";
            }
            this.historyCell.setTextAndValue(LocaleController.getString("ChatHistory", 2131559033), LocaleController.getString(s4, n3), false);
        }
        final TextSettingsCell stickersCell = this.stickersCell;
        if (stickersCell != null) {
            if (this.info.stickerset != null) {
                stickersCell.setTextAndValue(LocaleController.getString("GroupStickers", 2131559615), this.info.stickerset.title, false);
            }
            else {
                stickersCell.setText(LocaleController.getString("GroupStickers", 2131559615), false);
            }
        }
        final TextCell membersCell = this.membersCell;
        if (membersCell != null) {
            if (this.info != null) {
                if (this.isChannel) {
                    membersCell.setTextAndValueAndIcon(LocaleController.getString("ChannelSubscribers", 2131559004), String.format("%d", this.info.participants_count), 2131165277, true);
                    final TextCell blockCell = this.blockCell;
                    final String string3 = LocaleController.getString("ChannelBlacklist", 2131558932);
                    final TLRPC.ChatFull info4 = this.info;
                    final String format = String.format("%d", Math.max(info4.banned_count, info4.kicked_count));
                    final TextCell logCell2 = this.logCell;
                    blockCell.setTextAndValueAndIcon(string3, format, 2131165275, logCell2 != null && logCell2.getVisibility() == 0);
                }
                else {
                    if (ChatObject.isChannel(this.currentChat)) {
                        final TextCell membersCell2 = this.membersCell;
                        final String string4 = LocaleController.getString("ChannelMembers", 2131558962);
                        final String format2 = String.format("%d", this.info.participants_count);
                        final TextCell logCell3 = this.logCell;
                        membersCell2.setTextAndValueAndIcon(string4, format2, 2131165277, logCell3 != null && logCell3.getVisibility() == 0);
                    }
                    else {
                        final TextCell membersCell3 = this.membersCell;
                        final String string5 = LocaleController.getString("ChannelMembers", 2131558962);
                        final String format3 = String.format("%d", this.info.participants.participants.size());
                        final TextCell logCell4 = this.logCell;
                        membersCell3.setTextAndValueAndIcon(string5, format3, 2131165277, logCell4 != null && logCell4.getVisibility() == 0);
                    }
                    final TLRPC.TL_chatBannedRights default_banned_rights = this.currentChat.default_banned_rights;
                    int i;
                    if (default_banned_rights != null) {
                        int n4;
                        if (!default_banned_rights.send_stickers) {
                            n4 = 1;
                        }
                        else {
                            n4 = 0;
                        }
                        int n5 = n4;
                        if (!this.currentChat.default_banned_rights.send_media) {
                            n5 = n4 + 1;
                        }
                        int n6 = n5;
                        if (!this.currentChat.default_banned_rights.embed_links) {
                            n6 = n5 + 1;
                        }
                        int n7 = n6;
                        if (!this.currentChat.default_banned_rights.send_messages) {
                            n7 = n6 + 1;
                        }
                        int n8 = n7;
                        if (!this.currentChat.default_banned_rights.pin_messages) {
                            n8 = n7 + 1;
                        }
                        int n9 = n8;
                        if (!this.currentChat.default_banned_rights.send_polls) {
                            n9 = n8 + 1;
                        }
                        int n10 = n9;
                        if (!this.currentChat.default_banned_rights.invite_users) {
                            n10 = n9 + 1;
                        }
                        i = n10;
                        if (!this.currentChat.default_banned_rights.change_info) {
                            i = n10 + 1;
                        }
                    }
                    else {
                        i = 8;
                    }
                    this.blockCell.setTextAndValueAndIcon(LocaleController.getString("ChannelPermissions", 2131558985), String.format("%d/%d", i, 8), 2131165273, true);
                }
                final TextCell adminCell = this.adminCell;
                final String string6 = LocaleController.getString("ChannelAdministrators", 2131558927);
                int j;
                if (ChatObject.isChannel(this.currentChat)) {
                    j = this.info.admins_count;
                }
                else {
                    j = this.getAdminCount();
                }
                adminCell.setTextAndValueAndIcon(string6, String.format("%d", j), 2131165271, true);
            }
            else {
                if (this.isChannel) {
                    membersCell.setTextAndIcon(LocaleController.getString("ChannelSubscribers", 2131559004), 2131165277, true);
                    final TextCell blockCell2 = this.blockCell;
                    final String string7 = LocaleController.getString("ChannelBlacklist", 2131558932);
                    final TextCell logCell5 = this.logCell;
                    blockCell2.setTextAndIcon(string7, 2131165275, logCell5 != null && logCell5.getVisibility() == 0);
                }
                else {
                    final String string8 = LocaleController.getString("ChannelMembers", 2131558962);
                    final TextCell logCell6 = this.logCell;
                    membersCell.setTextAndIcon(string8, 2131165277, logCell6 != null && logCell6.getVisibility() == 0);
                    this.blockCell.setTextAndIcon(LocaleController.getString("ChannelPermissions", 2131558985), 2131165273, true);
                }
                this.adminCell.setTextAndIcon(LocaleController.getString("ChannelAdministrators", 2131558927), 2131165271, true);
            }
        }
        final TextSettingsCell stickersCell2 = this.stickersCell;
        if (stickersCell2 != null) {
            final TLRPC.ChatFull info5 = this.info;
            if (info5 != null) {
                if (info5.stickerset != null) {
                    stickersCell2.setTextAndValue(LocaleController.getString("GroupStickers", 2131559615), this.info.stickerset.title, false);
                }
                else {
                    stickersCell2.setText(LocaleController.getString("GroupStickers", 2131559615), false);
                }
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onDestroy();
        }
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    if (ChatEditActivity.this.checkDiscard()) {
                        ChatEditActivity.this.finishFragment();
                    }
                }
                else if (n == 1) {
                    ChatEditActivity.this.processDone();
                }
            }
        });
        final SizeNotifierFrameLayout fragmentView = new SizeNotifierFrameLayout(context) {
            private boolean ignoreLayout;
            
            @Override
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                final int childCount = this.getChildCount();
                final int keyboardHeight = this.getKeyboardHeight();
                final int dp = AndroidUtilities.dp(20.0f);
                int i = 0;
                int emojiPadding;
                if (keyboardHeight <= dp && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                    emojiPadding = ChatEditActivity.this.nameTextView.getEmojiPadding();
                }
                else {
                    emojiPadding = 0;
                }
                this.setBottomClip(emojiPadding);
                while (i < childCount) {
                    final View child = this.getChildAt(i);
                    if (child.getVisibility() != 8) {
                        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
                        final int measuredWidth = child.getMeasuredWidth();
                        final int measuredHeight = child.getMeasuredHeight();
                        int gravity;
                        if ((gravity = frameLayout$LayoutParams.gravity) == -1) {
                            gravity = 51;
                        }
                        final int n5 = gravity & 0x70;
                        final int n6 = gravity & 0x7 & 0x7;
                        int leftMargin = 0;
                        Label_0225: {
                            int n7;
                            int n8;
                            if (n6 != 1) {
                                if (n6 != 5) {
                                    leftMargin = frameLayout$LayoutParams.leftMargin;
                                    break Label_0225;
                                }
                                n7 = n3 - measuredWidth;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            else {
                                n7 = (n3 - n - measuredWidth) / 2 + frameLayout$LayoutParams.leftMargin;
                                n8 = frameLayout$LayoutParams.rightMargin;
                            }
                            leftMargin = n7 - n8;
                        }
                        int topMargin = 0;
                        Label_0327: {
                            int n9;
                            int n10;
                            if (n5 != 16) {
                                if (n5 == 48) {
                                    topMargin = frameLayout$LayoutParams.topMargin + this.getPaddingTop();
                                    break Label_0327;
                                }
                                if (n5 != 80) {
                                    topMargin = frameLayout$LayoutParams.topMargin;
                                    break Label_0327;
                                }
                                n9 = n4 - emojiPadding - n2 - measuredHeight;
                                n10 = frameLayout$LayoutParams.bottomMargin;
                            }
                            else {
                                n9 = (n4 - emojiPadding - n2 - measuredHeight) / 2 + frameLayout$LayoutParams.topMargin;
                                n10 = frameLayout$LayoutParams.bottomMargin;
                            }
                            topMargin = n9 - n10;
                        }
                        int n11 = topMargin;
                        if (ChatEditActivity.this.nameTextView != null) {
                            n11 = topMargin;
                            if (ChatEditActivity.this.nameTextView.isPopupView(child)) {
                                int measuredHeight2;
                                int n12;
                                if (AndroidUtilities.isTablet()) {
                                    measuredHeight2 = this.getMeasuredHeight();
                                    n12 = child.getMeasuredHeight();
                                }
                                else {
                                    measuredHeight2 = this.getMeasuredHeight() + this.getKeyboardHeight();
                                    n12 = child.getMeasuredHeight();
                                }
                                n11 = measuredHeight2 - n12;
                            }
                        }
                        child.layout(leftMargin, n11, measuredWidth + leftMargin, measuredHeight + n11);
                    }
                    ++i;
                }
                this.notifyHeightChanged();
            }
            
            protected void onMeasure(final int n, final int n2) {
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(n2);
                this.setMeasuredDimension(size, size2);
                final int n3 = size2 - this.getPaddingTop();
                this.measureChildWithMargins((View)ChatEditActivity.this.actionBar, n, 0, n2, 0);
                final int keyboardHeight = this.getKeyboardHeight();
                final int dp = AndroidUtilities.dp(20.0f);
                int i = 0;
                if (keyboardHeight > dp) {
                    this.ignoreLayout = true;
                    ChatEditActivity.this.nameTextView.hideEmojiView();
                    this.ignoreLayout = false;
                }
                while (i < this.getChildCount()) {
                    final View child = this.getChildAt(i);
                    if (child != null && child.getVisibility() != 8) {
                        if (child != ChatEditActivity.this.actionBar) {
                            if (ChatEditActivity.this.nameTextView != null && ChatEditActivity.this.nameTextView.isPopupView(child)) {
                                if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                                }
                                else if (AndroidUtilities.isTablet()) {
                                    final int measureSpec = View$MeasureSpec.makeMeasureSpec(size, 1073741824);
                                    float n4;
                                    if (AndroidUtilities.isTablet()) {
                                        n4 = 200.0f;
                                    }
                                    else {
                                        n4 = 320.0f;
                                    }
                                    child.measure(measureSpec, View$MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(n4), n3 - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                                }
                                else {
                                    child.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n3 - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                                }
                            }
                            else {
                                this.measureChildWithMargins(child, n, 0, n2, 0);
                            }
                        }
                    }
                    ++i;
                }
            }
            
            public void requestLayout() {
                if (this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        fragmentView.setOnTouchListener((View$OnTouchListener)_$$Lambda$ChatEditActivity$VwiI9D4ZnAE2nkj3zFy5AkednDE.INSTANCE);
        (super.fragmentView = (View)fragmentView).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final ScrollView scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        fragmentView.addView((View)scrollView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        final LinearLayout linearLayout = new LinearLayout(context);
        scrollView.addView((View)linearLayout, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        super.actionBar.setTitle(LocaleController.getString("ChannelEdit", 2131558950));
        (this.avatarContainer = new LinearLayout(context)).setOrientation(1);
        this.avatarContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView((View)this.avatarContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        final FrameLayout frameLayout = new FrameLayout(context);
        this.avatarContainer.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.avatarImage = new BackupImageView(context) {
            public void invalidate() {
                if (ChatEditActivity.this.avatarOverlay != null) {
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate();
            }
            
            public void invalidate(final int n, final int n2, final int n3, final int n4) {
                if (ChatEditActivity.this.avatarOverlay != null) {
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate(n, n2, n3, n4);
            }
        }).setRoundRadius(AndroidUtilities.dp(32.0f));
        final BackupImageView avatarImage = this.avatarImage;
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        float n2;
        if (LocaleController.isRTL) {
            n2 = 0.0f;
        }
        else {
            n2 = 16.0f;
        }
        float n3;
        if (LocaleController.isRTL) {
            n3 = 16.0f;
        }
        else {
            n3 = 0.0f;
        }
        final int n4 = 5;
        frameLayout.addView((View)avatarImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n | 0x30, n2, 12.0f, n3, 12.0f));
        if (ChatObject.canChangeChatInfo(this.currentChat)) {
            this.avatarDrawable.setInfo(5, null, null, false);
            final Paint paint = new Paint(1);
            paint.setColor(1426063360);
            this.avatarOverlay = new View(context) {
                protected void onDraw(final Canvas canvas) {
                    if (ChatEditActivity.this.avatarImage != null && ChatEditActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                        paint.setAlpha((int)(ChatEditActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
                        canvas.drawCircle((float)(this.getMeasuredWidth() / 2), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(32.0f), paint);
                    }
                }
            };
            final View avatarOverlay = this.avatarOverlay;
            int n5;
            if (LocaleController.isRTL) {
                n5 = 5;
            }
            else {
                n5 = 3;
            }
            float n6;
            if (LocaleController.isRTL) {
                n6 = 0.0f;
            }
            else {
                n6 = 16.0f;
            }
            float n7;
            if (LocaleController.isRTL) {
                n7 = 16.0f;
            }
            else {
                n7 = 0.0f;
            }
            frameLayout.addView(avatarOverlay, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n5 | 0x30, n6, 12.0f, n7, 12.0f));
            this.avatarOverlay.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$Hxmf_lPSvYp0l6WoXkJtGNwopNs(this));
            this.avatarOverlay.setContentDescription((CharSequence)LocaleController.getString("ChoosePhoto", 2131559091));
            (this.avatarEditor = new ImageView(context) {
                public void invalidate() {
                    super.invalidate();
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
                
                public void invalidate(final int n, final int n2, final int n3, final int n4) {
                    super.invalidate(n, n2, n3, n4);
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
            }).setScaleType(ImageView$ScaleType.CENTER);
            this.avatarEditor.setImageResource(2131165572);
            this.avatarEditor.setEnabled(false);
            this.avatarEditor.setClickable(false);
            final ImageView avatarEditor = this.avatarEditor;
            int n8;
            if (LocaleController.isRTL) {
                n8 = 5;
            }
            else {
                n8 = 3;
            }
            float n9;
            if (LocaleController.isRTL) {
                n9 = 0.0f;
            }
            else {
                n9 = 16.0f;
            }
            float n10;
            if (LocaleController.isRTL) {
                n10 = 16.0f;
            }
            else {
                n10 = 0.0f;
            }
            frameLayout.addView((View)avatarEditor, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n8 | 0x30, n9, 12.0f, n10, 12.0f));
            (this.avatarProgressView = new RadialProgressView(context)).setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            final RadialProgressView avatarProgressView = this.avatarProgressView;
            int n11;
            if (LocaleController.isRTL) {
                n11 = 5;
            }
            else {
                n11 = 3;
            }
            float n12;
            if (LocaleController.isRTL) {
                n12 = 0.0f;
            }
            else {
                n12 = 16.0f;
            }
            float n13;
            if (LocaleController.isRTL) {
                n13 = 16.0f;
            }
            else {
                n13 = 0.0f;
            }
            frameLayout.addView((View)avatarProgressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(64, 64.0f, n11 | 0x30, n12, 12.0f, n13, 12.0f));
            this.showAvatarProgress(false, false);
        }
        else {
            this.avatarDrawable.setInfo(5, this.currentChat.title, null, false);
        }
        this.nameTextView = new EditTextEmoji(context, fragmentView, this, 0);
        if (this.isChannel) {
            this.nameTextView.setHint(LocaleController.getString("EnterChannelName", 2131559367));
        }
        else {
            this.nameTextView.setHint(LocaleController.getString("GroupName", 2131559610));
        }
        this.nameTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
        final EditTextEmoji nameTextView2 = this.nameTextView;
        nameTextView2.setFocusable(nameTextView2.isEnabled());
        this.nameTextView.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(100) });
        final EditTextEmoji nameTextView3 = this.nameTextView;
        float n14;
        if (LocaleController.isRTL) {
            n14 = 5.0f;
        }
        else {
            n14 = 96.0f;
        }
        float n15;
        if (LocaleController.isRTL) {
            n15 = 96.0f;
        }
        else {
            n15 = 5.0f;
        }
        frameLayout.addView((View)nameTextView3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 16, n14, 0.0f, n15, 0.0f));
        (this.settingsContainer = new LinearLayout(context)).setOrientation(1);
        this.settingsContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView((View)this.settingsContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.descriptionTextView = new EditTextBoldCursor(context)).setTextSize(1, 16.0f);
        this.descriptionTextView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
        this.descriptionTextView.setBackgroundDrawable((Drawable)null);
        final EditTextBoldCursor descriptionTextView = this.descriptionTextView;
        int gravity;
        if (LocaleController.isRTL) {
            gravity = n4;
        }
        else {
            gravity = 3;
        }
        descriptionTextView.setGravity(gravity);
        this.descriptionTextView.setInputType(180225);
        this.descriptionTextView.setImeOptions(6);
        this.descriptionTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
        final EditTextBoldCursor descriptionTextView2 = this.descriptionTextView;
        descriptionTextView2.setFocusable(descriptionTextView2.isEnabled());
        this.descriptionTextView.setFilters(new InputFilter[] { (InputFilter)new InputFilter$LengthFilter(255) });
        this.descriptionTextView.setHint((CharSequence)LocaleController.getString("DescriptionOptionalPlaceholder", 2131559264));
        this.descriptionTextView.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.descriptionTextView.setCursorSize(AndroidUtilities.dp(20.0f));
        this.descriptionTextView.setCursorWidth(1.5f);
        this.settingsContainer.addView((View)this.descriptionTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 23.0f, 12.0f, 23.0f, 6.0f));
        this.descriptionTextView.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$ChatEditActivity$p1fZRHy8NDyNuO213khwXU229Jc(this));
        this.descriptionTextView.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        linearLayout.addView((View)(this.settingsTopSectionCell = new ShadowSectionCell(context)), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.typeEditContainer = new LinearLayout(context)).setOrientation(1);
        this.typeEditContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView((View)this.typeEditContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        if (this.currentChat.creator) {
            final TLRPC.ChatFull info = this.info;
            if (info == null || info.can_set_username) {
                (this.typeCell = new TextDetailCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
                this.typeEditContainer.addView((View)this.typeCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                this.typeCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$takgJ7d_dj5vza0E_4qO74BhrTA(this));
            }
        }
        if (ChatObject.isChannel(this.currentChat) && ((this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 1)) || (!this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 0)))) {
            (this.linkedCell = new TextDetailCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView((View)this.linkedCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.linkedCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$vs7xjVOaqM3gt8vxvzKAx_LFF8w(this));
        }
        if (!this.isChannel && ChatObject.canBlockUsers(this.currentChat) && (ChatObject.isChannel(this.currentChat) || this.currentChat.creator)) {
            (this.historyCell = new TextDetailCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView((View)this.historyCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            this.historyCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$ZaLTd9UrDPsZkM9f0GkspKG3v50(this, context));
        }
        if (this.isChannel) {
            (this.signCell = new TextCheckCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.signCell.setTextAndValueAndCheck(LocaleController.getString("ChannelSignMessages", 2131559001), LocaleController.getString("ChannelSignMessagesInfo", 2131559002), this.signMessages, true, false);
            this.typeEditContainer.addView((View)this.signCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
            this.signCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$m_aFUQRAeXShPmT_g_6jI0sXdes(this));
        }
        final ActionBarMenu menu = super.actionBar.createMenu();
        if (ChatObject.canChangeChatInfo(this.currentChat) || this.signCell != null || this.historyCell != null) {
            (this.doneButton = (View)menu.addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f))).setContentDescription((CharSequence)LocaleController.getString("Done", 2131559299));
        }
        if (this.signCell != null || this.historyCell != null || this.typeCell != null || this.linkedCell != null) {
            linearLayout.addView((View)(this.settingsSectionCell = new ShadowSectionCell(context)), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        (this.infoContainer = new LinearLayout(context)).setOrientation(1);
        this.infoContainer.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        linearLayout.addView((View)this.infoContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        (this.blockCell = new TextCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        final TextCell blockCell = this.blockCell;
        int visibility;
        if (!ChatObject.isChannel(this.currentChat) && !this.currentChat.creator) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        blockCell.setVisibility(visibility);
        this.blockCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$Z_VSyPell_FXQ74xw_1QaAWQHLA(this));
        (this.adminCell = new TextCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.adminCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$BW8nfxB2gbGLRBoiaMPR3BQCdjM(this));
        (this.membersCell = new TextCell(context)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.membersCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$R8nmnQgVpQwtOgAwCTLlWo2fY0k(this));
        if (ChatObject.isChannel(this.currentChat)) {
            (this.logCell = new TextCell(context)).setTextAndIcon(LocaleController.getString("EventLog", 2131559382), 2131165405, false);
            this.logCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.logCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$__qhLtKqz7XJ52ia47Bo4pX1C7s(this));
        }
        if (!this.isChannel) {
            this.infoContainer.addView((View)this.blockCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        this.infoContainer.addView((View)this.adminCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        this.infoContainer.addView((View)this.membersCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        if (this.isChannel) {
            this.infoContainer.addView((View)this.blockCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        final TextCell logCell = this.logCell;
        if (logCell != null) {
            this.infoContainer.addView((View)logCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        linearLayout.addView((View)(this.infoSectionCell = new ShadowSectionCell(context)), (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        if (!ChatObject.hasAdminRights(this.currentChat)) {
            this.infoContainer.setVisibility(8);
            this.settingsTopSectionCell.setVisibility(8);
        }
        if (!this.isChannel) {
            final TLRPC.ChatFull info2 = this.info;
            if (info2 != null && info2.can_set_stickers) {
                (this.stickersContainer = new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                linearLayout.addView((View)this.stickersContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                (this.stickersCell = new TextSettingsCell(context)).setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.stickersCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                this.stickersContainer.addView((View)this.stickersCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
                this.stickersCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$51Cw78hSbx5h61mWEeKV8Wy14wg(this));
                (this.stickersInfoCell3 = new TextInfoPrivacyCell(context)).setText(LocaleController.getString("GroupStickersInfo", 2131559616));
                linearLayout.addView((View)this.stickersInfoCell3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            }
        }
        if (this.currentChat.creator) {
            (this.deleteContainer = new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            linearLayout.addView((View)this.deleteContainer, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
            (this.deleteCell = new TextSettingsCell(context)).setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
            this.deleteCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (this.isChannel) {
                this.deleteCell.setText(LocaleController.getString("ChannelDelete", 2131558943), false);
            }
            else if (this.currentChat.megagroup) {
                this.deleteCell.setText(LocaleController.getString("DeleteMega", 2131559248), false);
            }
            else {
                this.deleteCell.setText(LocaleController.getString("DeleteAndExitButton", 2131559235), false);
            }
            this.deleteContainer.addView((View)this.deleteCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
            this.deleteCell.setOnClickListener((View$OnClickListener)new _$$Lambda$ChatEditActivity$qBqi8ghp1hDeyx8_eISTzlbn7qQ(this));
            (this.deleteInfoCell = new ShadowSectionCell(context)).setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            linearLayout.addView((View)this.deleteInfoCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
        }
        else if (!this.isChannel && this.stickersInfoCell3 == null) {
            this.infoSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        }
        final TextInfoPrivacyCell stickersInfoCell3 = this.stickersInfoCell3;
        if (stickersInfoCell3 != null) {
            if (this.deleteInfoCell == null) {
                stickersInfoCell3.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
            }
            else {
                stickersInfoCell3.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165394, "windowBackgroundGrayShadow"));
            }
        }
        this.nameTextView.setText(this.currentChat.title);
        final EditTextEmoji nameTextView4 = this.nameTextView;
        nameTextView4.setSelection(nameTextView4.length());
        final TLRPC.ChatFull info3 = this.info;
        if (info3 != null) {
            this.descriptionTextView.setText((CharSequence)info3.about);
        }
        final TLRPC.Chat currentChat = this.currentChat;
        final TLRPC.ChatPhoto photo = currentChat.photo;
        if (photo != null) {
            this.avatar = photo.photo_small;
            this.avatarBig = photo.photo_big;
            this.avatarImage.setImage(ImageLocation.getForChat(currentChat, false), "50_50", this.avatarDrawable, this.currentChat);
        }
        else {
            this.avatarImage.setImageDrawable(this.avatarDrawable);
        }
        this.updateFields(true);
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.chatInfoDidLoad) {
            final TLRPC.ChatFull info = (TLRPC.ChatFull)array[0];
            if (info.id == this.chatId) {
                if (this.info == null) {
                    final EditTextBoldCursor descriptionTextView = this.descriptionTextView;
                    if (descriptionTextView != null) {
                        descriptionTextView.setText((CharSequence)info.about);
                    }
                }
                this.info = info;
                this.historyHidden = (!ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory);
                this.updateFields(false);
            }
        }
    }
    
    @Override
    public void didUploadPhoto(final TLRPC.InputFile inputFile, final TLRPC.PhotoSize photoSize, final TLRPC.PhotoSize photoSize2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$ChatEditActivity$_yvKcw5WJa5Pk6zwjtXTtTJaTb0(this, inputFile, photoSize2, photoSize));
    }
    
    @Override
    public String getInitialSearchString() {
        return this.nameTextView.getText().toString();
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs $$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs = new _$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.membersCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.membersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.membersCell, 0, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.adminCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.adminCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.adminCell, 0, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.blockCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.blockCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.blockCell, 0, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.logCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.logCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.logCell, 0, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.typeCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.typeCell, 0, new Class[] { TextDetailCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.typeCell, 0, new Class[] { TextDetailCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.historyCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.historyCell, 0, new Class[] { TextDetailCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.historyCell, 0, new Class[] { TextDetailCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"), new ThemeDescription((View)this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"), new ThemeDescription((View)this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"), new ThemeDescription((View)this.avatarContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.settingsContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.typeEditContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.deleteContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.stickersContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.infoContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription(this.settingsTopSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription(this.settingsSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription(this.deleteInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription(this.infoSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.signCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.signCell, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.signCell, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.signCell, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.deleteCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.deleteCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText5"), new ThemeDescription((View)this.stickersCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.stickersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.stickersInfoCell3, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.stickersInfoCell3, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(null, 0, null, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs, "avatar_backgroundPink") };
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        this.imageUpdater.onActivityResult(n, n2, intent);
    }
    
    @Override
    public boolean onBackPressed() {
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null && nameTextView.isPopupShowing()) {
            this.nameTextView.hidePopup(true);
            return false;
        }
        return this.checkDiscard();
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
        final TLRPC.Chat currentChat = this.currentChat;
        boolean isChannel = true;
        Label_0142: {
            if (currentChat == null) {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ChatEditActivity$j_VWblaHSOc0ptEwu8DVX6LNsH0(this, countDownLatch));
                try {
                    countDownLatch.await();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                if (this.currentChat != null) {
                    MessagesController.getInstance(super.currentAccount).putChat(this.currentChat, true);
                    if (this.info != null) {
                        break Label_0142;
                    }
                    MessagesStorage.getInstance(super.currentAccount).loadChatInfo(this.chatId, countDownLatch, false, false);
                    try {
                        countDownLatch.await();
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                    if (this.info != null) {
                        break Label_0142;
                    }
                }
                return false;
            }
        }
        if (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup) {
            isChannel = false;
        }
        this.isChannel = isChannel;
        final ImageUpdater imageUpdater = this.imageUpdater;
        imageUpdater.parentFragment = this;
        imageUpdater.delegate = (ImageUpdater.ImageUpdaterDelegate)this;
        this.signMessages = this.currentChat.signatures;
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.clear();
        }
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onDestroy();
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onPause();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            nameTextView.onResume();
            this.nameTextView.getEditText().requestFocus();
        }
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
        this.updateFields(true);
    }
    
    @Override
    public void restoreSelfArgs(final Bundle bundle) {
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.currentPicturePath = bundle.getString("path");
        }
    }
    
    @Override
    public void saveSelfArgs(final Bundle bundle) {
        final ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            final String currentPicturePath = imageUpdater.currentPicturePath;
            if (currentPicturePath != null) {
                bundle.putString("path", currentPicturePath);
            }
        }
        final EditTextEmoji nameTextView = this.nameTextView;
        if (nameTextView != null) {
            final String string = nameTextView.getText().toString();
            if (string != null && string.length() != 0) {
                bundle.putString("nameTextView", string);
            }
        }
    }
    
    public void setInfo(final TLRPC.ChatFull info) {
        this.info = info;
        if (info != null) {
            if (this.currentChat == null) {
                this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.chatId);
            }
            this.historyHidden = (!ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory);
        }
    }
}
