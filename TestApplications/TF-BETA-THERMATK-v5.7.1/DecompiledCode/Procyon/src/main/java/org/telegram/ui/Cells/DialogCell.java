// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.view.animation.Interpolator;
import org.telegram.messenger.ImageLocation;
import android.view.accessibility.AccessibilityEvent;
import android.view.View$MeasureSpec;
import android.view.accessibility.AccessibilityNodeInfo;
import android.annotation.SuppressLint;
import org.telegram.ui.Components.ScamDrawable;
import android.graphics.Paint;
import android.graphics.ColorFilter;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import android.os.SystemClock;
import android.graphics.Canvas;
import com.airbnb.lottie.LottieDrawable;
import android.text.TextPaint;
import org.telegram.ui.Components.StaticLayoutEx;
import android.text.TextUtils$TruncateAt;
import android.text.Layout$Alignment;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.FileLog;
import android.text.TextUtils;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.ChatObject;
import android.text.style.ForegroundColorSpan;
import android.os.Build$VERSION;
import org.telegram.messenger.SharedConfig;
import android.graphics.drawable.Drawable$Callback;
import org.telegram.messenger.Emoji;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.DialogObject;
import android.text.SpannableStringBuilder;
import java.util.ArrayList;
import org.telegram.ui.DialogsActivity;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.View;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.RectF;
import org.telegram.messenger.MessageObject;
import android.text.StaticLayout;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ImageReceiver;
import org.telegram.ui.Components.AvatarDrawable;

public class DialogCell extends BaseCell
{
    private boolean animatingArchiveAvatar;
    private float animatingArchiveAvatarProgress;
    private float archiveBackgroundProgress;
    private boolean archiveHidden;
    private boolean attachedToWindow;
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage;
    private int bottomClip;
    private TLRPC.Chat chat;
    private CheckBox2 checkBox;
    private int checkDrawLeft;
    private int checkDrawTop;
    private boolean clearingDialog;
    private float clipProgress;
    private float cornerProgress;
    private StaticLayout countLayout;
    private int countLeft;
    private int countTop;
    private int countWidth;
    private int currentAccount;
    private int currentDialogFolderDialogsCount;
    private int currentDialogFolderId;
    private long currentDialogId;
    private int currentEditDate;
    private float currentRevealBounceProgress;
    private float currentRevealProgress;
    private CustomDialog customDialog;
    private boolean dialogMuted;
    private int dialogsType;
    private TLRPC.DraftMessage draftMessage;
    private boolean drawCheck1;
    private boolean drawCheck2;
    private boolean drawClock;
    private boolean drawCount;
    private boolean drawError;
    private boolean drawMention;
    private boolean drawNameBot;
    private boolean drawNameBroadcast;
    private boolean drawNameGroup;
    private boolean drawNameLock;
    private boolean drawPin;
    private boolean drawPinBackground;
    private boolean drawReorder;
    private boolean drawRevealBackground;
    private boolean drawScam;
    private boolean drawVerified;
    private TLRPC.EncryptedChat encryptedChat;
    private int errorLeft;
    private int errorTop;
    private int folderId;
    public boolean fullSeparator;
    public boolean fullSeparator2;
    private int halfCheckDrawLeft;
    private int index;
    private BounceInterpolator interpolator;
    private boolean isDialogCell;
    private boolean isSelected;
    private boolean isSliding;
    private int lastMessageDate;
    private CharSequence lastMessageString;
    private CharSequence lastPrintString;
    private int lastSendState;
    private boolean lastUnreadState;
    private long lastUpdateTime;
    private boolean markUnread;
    private int mentionCount;
    private StaticLayout mentionLayout;
    private int mentionLeft;
    private int mentionWidth;
    private MessageObject message;
    private int messageId;
    private StaticLayout messageLayout;
    private int messageLeft;
    private StaticLayout messageNameLayout;
    private int messageNameLeft;
    private int messageNameTop;
    private int messageTop;
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
    private float onlineProgress;
    private int pinLeft;
    private int pinTop;
    private RectF rect;
    private float reorderIconProgress;
    private StaticLayout timeLayout;
    private int timeLeft;
    private int timeTop;
    private int topClip;
    private boolean translationAnimationStarted;
    private Drawable translationDrawable;
    private float translationX;
    private int unreadCount;
    public boolean useForceThreeLines;
    public boolean useSeparator;
    private TLRPC.User user;
    
    public DialogCell(final Context context, final boolean b, final boolean useForceThreeLines) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.avatarImage = new ImageReceiver((View)this);
        this.avatarDrawable = new AvatarDrawable();
        this.interpolator = new BounceInterpolator();
        this.rect = new RectF();
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0f));
        this.useForceThreeLines = useForceThreeLines;
        if (b) {
            (this.checkBox = new CheckBox2(context)).setColor(null, "windowBackgroundWhite", "checkboxCheck");
            this.checkBox.setSize(21);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            this.addView((View)this.checkBox);
        }
    }
    
    private void checkOnline() {
        final TLRPC.User user = this.user;
        boolean b = false;
        Label_0075: {
            if (user != null && !user.self) {
                final TLRPC.UserStatus status = user.status;
                if ((status != null && status.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.user.id)) {
                    b = true;
                    break Label_0075;
                }
            }
            b = false;
        }
        float onlineProgress;
        if (b) {
            onlineProgress = 1.0f;
        }
        else {
            onlineProgress = 0.0f;
        }
        this.onlineProgress = onlineProgress;
    }
    
    private MessageObject findFolderTopMessage() {
        final int currentAccount = this.currentAccount;
        final int dialogsType = this.dialogsType;
        final int currentDialogFolderId = this.currentDialogFolderId;
        int index = 0;
        final ArrayList<TLRPC.Dialog> dialogsArray = DialogsActivity.getDialogsArray(currentAccount, dialogsType, currentDialogFolderId, false);
        final boolean empty = dialogsArray.isEmpty();
        MessageObject messageObject = null;
        MessageObject messageObject2 = null;
        if (!empty) {
            final int size = dialogsArray.size();
            while (true) {
                messageObject = messageObject2;
                if (index >= size) {
                    break;
                }
                final TLRPC.Dialog dialog = dialogsArray.get(index);
                final MessageObject messageObject3 = (MessageObject)MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
                messageObject = messageObject2;
                Label_0137: {
                    if (messageObject3 != null) {
                        if (messageObject2 != null) {
                            messageObject = messageObject2;
                            if (messageObject3.messageOwner.date <= messageObject2.messageOwner.date) {
                                break Label_0137;
                            }
                        }
                        messageObject = messageObject3;
                    }
                }
                if (dialog.pinnedNum == 0) {
                    break;
                }
                ++index;
                messageObject2 = messageObject;
            }
        }
        return messageObject;
    }
    
    private CharSequence formatArchivedDialogNames() {
        final ArrayList<TLRPC.Dialog> dialogs = MessagesController.getInstance(this.currentAccount).getDialogs(this.currentDialogFolderId);
        this.currentDialogFolderDialogsCount = dialogs.size();
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (int size = dialogs.size(), i = 0; i < size; ++i) {
            final TLRPC.Dialog dialog = dialogs.get(i);
            final boolean secretDialogId = DialogObject.isSecretDialogId(dialog.id);
            TLRPC.Chat chat = null;
            TLRPC.User user;
            if (secretDialogId) {
                final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat((int)(dialog.id >> 32));
                if (encryptedChat != null) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(encryptedChat.user_id);
                }
                else {
                    user = null;
                }
            }
            else {
                final int j = (int)dialog.id;
                if (j > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(j);
                }
                else {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(-j);
                    user = null;
                }
            }
            String s;
            if (chat != null) {
                s = chat.title.replace('\n', ' ');
            }
            else {
                if (user == null) {
                    continue;
                }
                if (UserObject.isDeleted(user)) {
                    s = LocaleController.getString("HiddenName", 2131559636);
                }
                else {
                    s = ContactsController.formatName(user.first_name, user.last_name).replace('\n', ' ');
                }
            }
            if (spannableStringBuilder.length() > 0) {
                spannableStringBuilder.append((CharSequence)", ");
            }
            final int length = spannableStringBuilder.length();
            final int length2 = s.length();
            spannableStringBuilder.append((CharSequence)s);
            if (dialog.unread_count > 0) {
                spannableStringBuilder.setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor("chats_nameArchived")), length, length2 + length, 33);
            }
            if (spannableStringBuilder.length() > 150) {
                break;
            }
        }
        return Emoji.replaceEmoji((CharSequence)spannableStringBuilder, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
    }
    
    public void animateArchiveAvatar() {
        if (this.avatarDrawable.getAvatarType() != 3) {
            return;
        }
        this.animatingArchiveAvatar = true;
        this.animatingArchiveAvatarProgress = 0.0f;
        Theme.dialogs_archiveAvatarDrawable.setCallback((Drawable$Callback)this);
        Theme.dialogs_archiveAvatarDrawable.setProgress(0.0f);
        Theme.dialogs_archiveAvatarDrawable.start();
        this.invalidate();
    }
    
    public void buildLayout() {
        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            Theme.dialogs_namePaint.setTextSize((float)AndroidUtilities.dp(17.0f));
            Theme.dialogs_nameEncryptedPaint.setTextSize((float)AndroidUtilities.dp(17.0f));
            Theme.dialogs_messagePaint.setTextSize((float)AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePrintingPaint.setTextSize((float)AndroidUtilities.dp(16.0f));
            final TextPaint dialogs_messagePaint = Theme.dialogs_messagePaint;
            dialogs_messagePaint.setColor(dialogs_messagePaint.linkColor = Theme.getColor("chats_message"));
        }
        else {
            Theme.dialogs_namePaint.setTextSize((float)AndroidUtilities.dp(16.0f));
            Theme.dialogs_nameEncryptedPaint.setTextSize((float)AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePaint.setTextSize((float)AndroidUtilities.dp(15.0f));
            Theme.dialogs_messagePrintingPaint.setTextSize((float)AndroidUtilities.dp(15.0f));
            final TextPaint dialogs_messagePaint2 = Theme.dialogs_messagePaint;
            dialogs_messagePaint2.setColor(dialogs_messagePaint2.linkColor = Theme.getColor("chats_message_threeLines"));
        }
        this.currentDialogFolderDialogsCount = 0;
        Object lastPrintString;
        if (this.isDialogCell) {
            lastPrintString = MessagesController.getInstance(this.currentAccount).printingStrings.get(this.currentDialogId);
        }
        else {
            lastPrintString = null;
        }
        Object o = Theme.dialogs_messagePaint;
        this.drawNameGroup = false;
        this.drawNameBroadcast = false;
        this.drawNameLock = false;
        this.drawNameBot = false;
        this.drawVerified = false;
        this.drawScam = false;
        this.drawPinBackground = false;
        final boolean userSelf = UserObject.isUserSelf(this.user);
        int n = 1;
        int n2 = (userSelf ^ true) ? 1 : 0;
        String format = null;
        boolean b = false;
        Label_0342: {
            Label_0339: {
                if (Build$VERSION.SDK_INT >= 18) {
                    if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId == 0) {
                        format = "\u2068%s\u2069";
                        break Label_0339;
                    }
                    format = "%2$s: \u2068%1$s\u2069";
                }
                else {
                    if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId == 0) {
                        format = "%1$s";
                        break Label_0339;
                    }
                    format = "%2$s: %1$s";
                }
                b = true;
                break Label_0342;
            }
            b = false;
        }
        final MessageObject message = this.message;
        CharSequence messageText;
        if (message != null) {
            messageText = message.messageText;
        }
        else {
            messageText = null;
        }
        this.lastMessageString = messageText;
        final CustomDialog customDialog = this.customDialog;
        Object o2 = null;
        Object dialogs_messagePaint3;
        CharSequence charSequence;
        String format2;
        String name;
        String s3 = null;
        String s4;
        int n4;
        if (customDialog != null) {
            final int type = customDialog.type;
            if (type == 2) {
                this.drawNameLock = true;
                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(76.0f);
                        this.nameLeft = AndroidUtilities.dp(80.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                    }
                    else {
                        this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(76.0f) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(18.0f);
                    }
                }
                else {
                    this.nameLockTop = AndroidUtilities.dp(12.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(78.0f);
                        this.nameLeft = AndroidUtilities.dp(82.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                    }
                    else {
                        this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(78.0f) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(22.0f);
                    }
                }
            }
            else {
                this.drawVerified = customDialog.verified;
                if (SharedConfig.drawDialogIcons && type == 1) {
                    this.drawNameGroup = true;
                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        if (!LocaleController.isRTL) {
                            this.nameLockTop = AndroidUtilities.dp(17.5f);
                            this.nameLockLeft = AndroidUtilities.dp(76.0f);
                            final int dp = AndroidUtilities.dp(80.0f);
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
                            final int dp2 = AndroidUtilities.dp(76.0f);
                            Drawable drawable2;
                            if (this.drawNameGroup) {
                                drawable2 = Theme.dialogs_groupDrawable;
                            }
                            else {
                                drawable2 = Theme.dialogs_broadcastDrawable;
                            }
                            this.nameLockLeft = measuredWidth - dp2 - drawable2.getIntrinsicWidth();
                            this.nameLeft = AndroidUtilities.dp(18.0f);
                        }
                    }
                    else {
                        this.nameLockTop = AndroidUtilities.dp(13.5f);
                        if (!LocaleController.isRTL) {
                            this.nameLockLeft = AndroidUtilities.dp(78.0f);
                            final int dp3 = AndroidUtilities.dp(82.0f);
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
                            final int dp4 = AndroidUtilities.dp(78.0f);
                            Drawable drawable4;
                            if (this.drawNameGroup) {
                                drawable4 = Theme.dialogs_groupDrawable;
                            }
                            else {
                                drawable4 = Theme.dialogs_broadcastDrawable;
                            }
                            this.nameLockLeft = measuredWidth2 - dp4 - drawable4.getIntrinsicWidth();
                            this.nameLeft = AndroidUtilities.dp(22.0f);
                        }
                    }
                }
                else if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                    if (!LocaleController.isRTL) {
                        this.nameLeft = AndroidUtilities.dp(76.0f);
                    }
                    else {
                        this.nameLeft = AndroidUtilities.dp(18.0f);
                    }
                }
                else if (!LocaleController.isRTL) {
                    this.nameLeft = AndroidUtilities.dp(78.0f);
                }
                else {
                    this.nameLeft = AndroidUtilities.dp(22.0f);
                }
            }
            final CustomDialog customDialog2 = this.customDialog;
            int n3;
            if (customDialog2.type == 1) {
                final String string = LocaleController.getString("FromYou", 2131559584);
                final CustomDialog customDialog3 = this.customDialog;
                SpannableStringBuilder spannableStringBuilder;
                if (customDialog3.isMedia) {
                    o = Theme.dialogs_messagePrintingPaint;
                    spannableStringBuilder = SpannableStringBuilder.valueOf((CharSequence)String.format(format, this.message.messageText));
                    spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(Theme.getColor("chats_attachMessage")), 0, spannableStringBuilder.length(), 33);
                }
                else {
                    String s2;
                    final String s = s2 = customDialog3.message;
                    if (s.length() > 150) {
                        s2 = s.substring(0, 150);
                    }
                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        spannableStringBuilder = SpannableStringBuilder.valueOf((CharSequence)String.format(format, s2.replace('\n', ' '), string));
                    }
                    else {
                        spannableStringBuilder = SpannableStringBuilder.valueOf((CharSequence)String.format(format, s2, string));
                    }
                }
                o2 = Emoji.replaceEmoji((CharSequence)spannableStringBuilder, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                dialogs_messagePaint3 = o;
                charSequence = string;
                n3 = 0;
            }
            else {
                o2 = customDialog2.message;
                if (customDialog2.isMedia) {
                    o = Theme.dialogs_messagePrintingPaint;
                }
                n3 = 1;
                final CharSequence charSequence2 = null;
                dialogs_messagePaint3 = o;
                charSequence = charSequence2;
            }
            final String stringForMessageListDate = LocaleController.stringForMessageListDate(this.customDialog.date);
            final int unread_count = this.customDialog.unread_count;
            if (unread_count != 0) {
                this.drawCount = true;
                format2 = String.format("%d", unread_count);
            }
            else {
                this.drawCount = false;
                format2 = null;
            }
            if (this.customDialog.sent) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
                this.drawClock = false;
                this.drawError = false;
            }
            else {
                this.drawCheck1 = false;
                this.drawCheck2 = false;
                this.drawClock = false;
                this.drawError = false;
            }
            name = this.customDialog.name;
            s3 = null;
            s4 = stringForMessageListDate;
            n4 = n3;
        }
        else {
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                if (!LocaleController.isRTL) {
                    this.nameLeft = AndroidUtilities.dp(76.0f);
                }
                else {
                    this.nameLeft = AndroidUtilities.dp(18.0f);
                }
            }
            else if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp(78.0f);
            }
            else {
                this.nameLeft = AndroidUtilities.dp(22.0f);
            }
            if (this.encryptedChat != null) {
                if (this.currentDialogFolderId == 0) {
                    this.drawNameLock = true;
                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        this.nameLockTop = AndroidUtilities.dp(16.5f);
                        if (!LocaleController.isRTL) {
                            this.nameLockLeft = AndroidUtilities.dp(76.0f);
                            this.nameLeft = AndroidUtilities.dp(80.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        }
                        else {
                            this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(76.0f) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                            this.nameLeft = AndroidUtilities.dp(18.0f);
                        }
                    }
                    else {
                        this.nameLockTop = AndroidUtilities.dp(12.5f);
                        if (!LocaleController.isRTL) {
                            this.nameLockLeft = AndroidUtilities.dp(78.0f);
                            this.nameLeft = AndroidUtilities.dp(82.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        }
                        else {
                            this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(78.0f) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                            this.nameLeft = AndroidUtilities.dp(22.0f);
                        }
                    }
                }
            }
            else if (this.currentDialogFolderId == 0) {
                final TLRPC.Chat chat = this.chat;
                if (chat != null) {
                    if (chat.scam) {
                        this.drawScam = true;
                        Theme.dialogs_scamDrawable.checkText();
                    }
                    else {
                        this.drawVerified = chat.verified;
                    }
                    if (SharedConfig.drawDialogIcons) {
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            final TLRPC.Chat chat2 = this.chat;
                            if (chat2.id >= 0 && (!ChatObject.isChannel(chat2) || this.chat.megagroup)) {
                                this.drawNameGroup = true;
                                this.nameLockTop = AndroidUtilities.dp(17.5f);
                            }
                            else {
                                this.drawNameBroadcast = true;
                                this.nameLockTop = AndroidUtilities.dp(16.5f);
                            }
                            if (!LocaleController.isRTL) {
                                this.nameLockLeft = AndroidUtilities.dp(76.0f);
                                final int dp5 = AndroidUtilities.dp(80.0f);
                                Drawable drawable5;
                                if (this.drawNameGroup) {
                                    drawable5 = Theme.dialogs_groupDrawable;
                                }
                                else {
                                    drawable5 = Theme.dialogs_broadcastDrawable;
                                }
                                this.nameLeft = dp5 + drawable5.getIntrinsicWidth();
                            }
                            else {
                                final int measuredWidth3 = this.getMeasuredWidth();
                                final int dp6 = AndroidUtilities.dp(76.0f);
                                Drawable drawable6;
                                if (this.drawNameGroup) {
                                    drawable6 = Theme.dialogs_groupDrawable;
                                }
                                else {
                                    drawable6 = Theme.dialogs_broadcastDrawable;
                                }
                                this.nameLockLeft = measuredWidth3 - dp6 - drawable6.getIntrinsicWidth();
                                this.nameLeft = AndroidUtilities.dp(18.0f);
                            }
                        }
                        else {
                            final TLRPC.Chat chat3 = this.chat;
                            if (chat3.id >= 0 && (!ChatObject.isChannel(chat3) || this.chat.megagroup)) {
                                this.drawNameGroup = true;
                                this.nameLockTop = AndroidUtilities.dp(13.5f);
                            }
                            else {
                                this.drawNameBroadcast = true;
                                this.nameLockTop = AndroidUtilities.dp(12.5f);
                            }
                            if (!LocaleController.isRTL) {
                                this.nameLockLeft = AndroidUtilities.dp(78.0f);
                                final int dp7 = AndroidUtilities.dp(82.0f);
                                Drawable drawable7;
                                if (this.drawNameGroup) {
                                    drawable7 = Theme.dialogs_groupDrawable;
                                }
                                else {
                                    drawable7 = Theme.dialogs_broadcastDrawable;
                                }
                                this.nameLeft = dp7 + drawable7.getIntrinsicWidth();
                            }
                            else {
                                final int measuredWidth4 = this.getMeasuredWidth();
                                final int dp8 = AndroidUtilities.dp(78.0f);
                                Drawable drawable8;
                                if (this.drawNameGroup) {
                                    drawable8 = Theme.dialogs_groupDrawable;
                                }
                                else {
                                    drawable8 = Theme.dialogs_broadcastDrawable;
                                }
                                this.nameLockLeft = measuredWidth4 - dp8 - drawable8.getIntrinsicWidth();
                                this.nameLeft = AndroidUtilities.dp(22.0f);
                            }
                        }
                    }
                }
                else {
                    final TLRPC.User user = this.user;
                    if (user != null) {
                        if (user.scam) {
                            this.drawScam = true;
                            Theme.dialogs_scamDrawable.checkText();
                        }
                        else {
                            this.drawVerified = user.verified;
                        }
                        if (SharedConfig.drawDialogIcons && this.user.bot) {
                            this.drawNameBot = true;
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                this.nameLockTop = AndroidUtilities.dp(16.5f);
                                if (!LocaleController.isRTL) {
                                    this.nameLockLeft = AndroidUtilities.dp(76.0f);
                                    this.nameLeft = AndroidUtilities.dp(80.0f) + Theme.dialogs_botDrawable.getIntrinsicWidth();
                                }
                                else {
                                    this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(76.0f) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                                    this.nameLeft = AndroidUtilities.dp(18.0f);
                                }
                            }
                            else {
                                this.nameLockTop = AndroidUtilities.dp(12.5f);
                                if (!LocaleController.isRTL) {
                                    this.nameLockLeft = AndroidUtilities.dp(78.0f);
                                    this.nameLeft = AndroidUtilities.dp(82.0f) + Theme.dialogs_botDrawable.getIntrinsicWidth();
                                }
                                else {
                                    this.nameLockLeft = this.getMeasuredWidth() - AndroidUtilities.dp(78.0f) - Theme.dialogs_botDrawable.getIntrinsicWidth();
                                    this.nameLeft = AndroidUtilities.dp(22.0f);
                                }
                            }
                        }
                    }
                }
            }
            final int lastMessageDate = this.lastMessageDate;
            int date;
            if ((date = lastMessageDate) == 0) {
                final MessageObject message2 = this.message;
                date = lastMessageDate;
                if (message2 != null) {
                    date = message2.messageOwner.date;
                }
            }
            Label_2508: {
                if (this.isDialogCell) {
                    this.draftMessage = DataQuery.getInstance(this.currentAccount).getDraft(this.currentDialogId);
                    final TLRPC.DraftMessage draftMessage = this.draftMessage;
                    Label_2388: {
                        if (draftMessage == null || ((!TextUtils.isEmpty((CharSequence)draftMessage.message) || this.draftMessage.reply_to_msg_id != 0) && (date <= this.draftMessage.date || this.unreadCount == 0))) {
                            if (ChatObject.isChannel(this.chat)) {
                                final TLRPC.Chat chat4 = this.chat;
                                if (!chat4.megagroup && !chat4.creator) {
                                    final TLRPC.TL_chatAdminRights admin_rights = chat4.admin_rights;
                                    if (admin_rights == null || !admin_rights.post_messages) {
                                        break Label_2388;
                                    }
                                }
                            }
                            final TLRPC.Chat chat5 = this.chat;
                            if (chat5 != null) {
                                if (chat5.left) {
                                    break Label_2388;
                                }
                                if (chat5.kicked) {
                                    break Label_2388;
                                }
                            }
                            break Label_2508;
                        }
                    }
                    this.draftMessage = null;
                }
                else {
                    this.draftMessage = null;
                }
            }
            final String format3 = format;
            Object dialogs_messagePrintingPaint = null;
            CharSequence charSequence4 = null;
            int n5 = 0;
            Label_4597: {
                CharSequence charSequence3;
                if (lastPrintString != null) {
                    this.lastPrintString = (CharSequence)lastPrintString;
                    dialogs_messagePrintingPaint = Theme.dialogs_messagePrintingPaint;
                    charSequence3 = null;
                }
                else {
                    this.lastPrintString = null;
                    if (this.draftMessage != null) {
                        charSequence4 = LocaleController.getString("Draft", 2131559300);
                        if (TextUtils.isEmpty((CharSequence)this.draftMessage.message)) {
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                o2 = SpannableStringBuilder.valueOf(charSequence4);
                                ((SpannableStringBuilder)o2).setSpan((Object)new ForegroundColorSpan(Theme.getColor("chats_draft")), 0, charSequence4.length(), 33);
                            }
                            else {
                                o2 = "";
                            }
                        }
                        else {
                            String s6;
                            final String s5 = s6 = this.draftMessage.message;
                            if (s5.length() > 150) {
                                s6 = s5.substring(0, 150);
                            }
                            SpannableStringBuilder spannableStringBuilder2;
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                spannableStringBuilder2 = SpannableStringBuilder.valueOf((CharSequence)String.format(format3, s6.replace('\n', ' '), charSequence4));
                                spannableStringBuilder2.setSpan((Object)new ForegroundColorSpan(Theme.getColor("chats_draft")), 0, charSequence4.length() + 1, 33);
                            }
                            else {
                                spannableStringBuilder2 = SpannableStringBuilder.valueOf((CharSequence)String.format(format3, s6.replace('\n', ' '), charSequence4));
                            }
                            o2 = Emoji.replaceEmoji((CharSequence)spannableStringBuilder2, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                        }
                        n5 = 0;
                        n = 1;
                        dialogs_messagePrintingPaint = o;
                        break Label_4597;
                    }
                    TextPaint textPaint = null;
                    String s7 = null;
                    Label_2834: {
                        if (this.clearingDialog) {
                            textPaint = Theme.dialogs_messagePrintingPaint;
                            s7 = LocaleController.getString("HistoryCleared", 2131559639);
                        }
                        else {
                            final MessageObject message3 = this.message;
                            if (message3 == null) {
                                final TLRPC.EncryptedChat encryptedChat = this.encryptedChat;
                                if (encryptedChat != null) {
                                    textPaint = Theme.dialogs_messagePrintingPaint;
                                    if (encryptedChat instanceof TLRPC.TL_encryptedChatRequested) {
                                        s7 = LocaleController.getString("EncryptionProcessing", 2131559363);
                                        break Label_2834;
                                    }
                                    if (encryptedChat instanceof TLRPC.TL_encryptedChatWaiting) {
                                        final TLRPC.User user2 = this.user;
                                        if (user2 != null) {
                                            final String first_name = user2.first_name;
                                            if (first_name != null) {
                                                s7 = LocaleController.formatString("AwaitingEncryption", 2131558808, first_name);
                                                break Label_2834;
                                            }
                                        }
                                        s7 = LocaleController.formatString("AwaitingEncryption", 2131558808, "");
                                        break Label_2834;
                                    }
                                    if (encryptedChat instanceof TLRPC.TL_encryptedChatDiscarded) {
                                        s7 = LocaleController.getString("EncryptionRejected", 2131559364);
                                        break Label_2834;
                                    }
                                    o = textPaint;
                                    if (encryptedChat instanceof TLRPC.TL_encryptedChat) {
                                        if (encryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                            final TLRPC.User user3 = this.user;
                                            if (user3 != null) {
                                                final String first_name2 = user3.first_name;
                                                if (first_name2 != null) {
                                                    s7 = LocaleController.formatString("EncryptedChatStartedOutgoing", 2131559352, first_name2);
                                                    break Label_2834;
                                                }
                                            }
                                            s7 = LocaleController.formatString("EncryptedChatStartedOutgoing", 2131559352, "");
                                            break Label_2834;
                                        }
                                        s7 = LocaleController.getString("EncryptedChatStartedIncoming", 2131559351);
                                        break Label_2834;
                                    }
                                }
                                o2 = "";
                                n5 = 1;
                                n = 1;
                                charSequence4 = null;
                                dialogs_messagePrintingPaint = o;
                                break Label_4597;
                            }
                            TLRPC.User user4;
                            TLRPC.Chat chat6;
                            if (message3.isFromUser()) {
                                user4 = MessagesController.getInstance(this.currentAccount).getUser(this.message.messageOwner.from_id);
                                chat6 = null;
                            }
                            else {
                                chat6 = MessagesController.getInstance(this.currentAccount).getChat(this.message.messageOwner.to_id.channel_id);
                                user4 = null;
                            }
                            int n6 = 0;
                            int n7 = 0;
                            Label_4570: {
                                if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                    o2 = LocaleController.getString("SavedMessagesInfo", 2131560634);
                                    n6 = 0;
                                    n7 = 1;
                                    n2 = 0;
                                }
                                else if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                    o2 = this.formatArchivedDialogNames();
                                    n6 = 1;
                                    n7 = 0;
                                }
                                else {
                                    final MessageObject message4 = this.message;
                                    Object o3;
                                    int n8;
                                    if (message4.messageOwner instanceof TLRPC.TL_messageService) {
                                        Label_3320: {
                                            if (ChatObject.isChannel(this.chat)) {
                                                final TLRPC.MessageAction action = this.message.messageOwner.action;
                                                if (action instanceof TLRPC.TL_messageActionHistoryClear || action instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                                    o2 = "";
                                                    n2 = 0;
                                                    break Label_3320;
                                                }
                                            }
                                            o2 = this.message.messageText;
                                        }
                                        o3 = Theme.dialogs_messagePrintingPaint;
                                        n8 = n2;
                                    }
                                    else {
                                        final TLRPC.Chat chat7 = this.chat;
                                        if (chat7 != null && chat7.id > 0 && chat6 == null) {
                                            String s8;
                                            if (message4.isOutOwner()) {
                                                s8 = LocaleController.getString("FromYou", 2131559584);
                                            }
                                            else if (user4 != null) {
                                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                                    s8 = UserObject.getFirstName(user4).replace("\n", "");
                                                }
                                                else if (UserObject.isDeleted(user4)) {
                                                    s8 = LocaleController.getString("HiddenName", 2131559636);
                                                }
                                                else {
                                                    s8 = ContactsController.formatName(user4.first_name, user4.last_name).replace("\n", "");
                                                }
                                            }
                                            else if (chat6 != null) {
                                                s8 = chat6.title.replace("\n", "");
                                            }
                                            else {
                                                s8 = "DELETED";
                                            }
                                            final MessageObject message5 = this.message;
                                            final CharSequence caption = message5.caption;
                                            Object o4;
                                            if (caption != null) {
                                                String s10;
                                                final String s9 = s10 = caption.toString();
                                                if (s9.length() > 150) {
                                                    s10 = s9.substring(0, 150);
                                                }
                                                String str;
                                                if (this.message.isVideo()) {
                                                    str = "\ud83d\udcf9 ";
                                                }
                                                else if (this.message.isVoice()) {
                                                    str = "\ud83c\udfa4 ";
                                                }
                                                else if (this.message.isMusic()) {
                                                    str = "\ud83c\udfa7 ";
                                                }
                                                else if (this.message.isPhoto()) {
                                                    str = "\ud83d\uddbc ";
                                                }
                                                else {
                                                    str = "\ud83d\udcce ";
                                                }
                                                final StringBuilder sb = new StringBuilder();
                                                sb.append(str);
                                                sb.append(s10.replace('\n', ' '));
                                                o4 = SpannableStringBuilder.valueOf((CharSequence)String.format(format3, sb.toString(), s8));
                                            }
                                            else if (message5.messageOwner.media != null && !message5.isMediaEmpty()) {
                                                o4 = Theme.dialogs_messagePrintingPaint;
                                                final MessageObject message6 = this.message;
                                                final TLRPC.MessageMedia media = message6.messageOwner.media;
                                                CharSequence charSequence5;
                                                if (media instanceof TLRPC.TL_messageMediaGame) {
                                                    if (Build$VERSION.SDK_INT >= 18) {
                                                        charSequence5 = String.format("\ud83c\udfae \u2068%s\u2069", media.game.title);
                                                    }
                                                    else {
                                                        charSequence5 = String.format("\ud83c\udfae %s", media.game.title);
                                                    }
                                                }
                                                else if (message6.type == 14) {
                                                    if (Build$VERSION.SDK_INT >= 18) {
                                                        charSequence5 = String.format("\ud83c\udfa7 \u2068%s - %s\u2069", message6.getMusicAuthor(), this.message.getMusicTitle());
                                                    }
                                                    else {
                                                        charSequence5 = String.format("\ud83c\udfa7 %s - %s", message6.getMusicAuthor(), this.message.getMusicTitle());
                                                    }
                                                }
                                                else {
                                                    charSequence5 = message6.messageText;
                                                }
                                                final SpannableStringBuilder value = SpannableStringBuilder.valueOf((CharSequence)String.format(format3, charSequence5, s8));
                                                try {
                                                    final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Theme.getColor("chats_attachMessage"));
                                                    int n9;
                                                    if (b) {
                                                        n9 = s8.length() + 2;
                                                    }
                                                    else {
                                                        n9 = 0;
                                                    }
                                                    value.setSpan((Object)foregroundColorSpan, n9, value.length(), 33);
                                                    o = o4;
                                                    o4 = value;
                                                }
                                                catch (Exception ex) {
                                                    FileLog.e(ex);
                                                    o = o4;
                                                    o4 = value;
                                                }
                                            }
                                            else {
                                                final String message7 = this.message.messageOwner.message;
                                                if (message7 != null) {
                                                    String substring = message7;
                                                    if (message7.length() > 150) {
                                                        substring = message7.substring(0, 150);
                                                    }
                                                    o4 = SpannableStringBuilder.valueOf((CharSequence)String.format(format3, substring.replace('\n', ' '), s8));
                                                }
                                                else {
                                                    o4 = SpannableStringBuilder.valueOf((CharSequence)"");
                                                }
                                            }
                                            Label_4118: {
                                                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                                    if (this.currentDialogFolderId == 0 || ((SpannableStringBuilder)o4).length() <= 0) {
                                                        break Label_4118;
                                                    }
                                                }
                                                try {
                                                    ((SpannableStringBuilder)o4).setSpan((Object)new ForegroundColorSpan(Theme.getColor("chats_nameMessage")), 0, s8.length() + 1, 33);
                                                }
                                                catch (Exception ex2) {
                                                    FileLog.e(ex2);
                                                }
                                            }
                                            final CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence)o4, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                            charSequence4 = s8;
                                            n7 = 0;
                                            o2 = replaceEmoji;
                                            n6 = 1;
                                            break Label_4570;
                                        }
                                        final TLRPC.MessageMedia media2 = this.message.messageOwner.media;
                                        if (media2 instanceof TLRPC.TL_messageMediaPhoto && media2.photo instanceof TLRPC.TL_photoEmpty && media2.ttl_seconds != 0) {
                                            o2 = LocaleController.getString("AttachPhotoExpired", 2131558728);
                                            o3 = o;
                                            n8 = n2;
                                        }
                                        else {
                                            final TLRPC.MessageMedia media3 = this.message.messageOwner.media;
                                            if (media3 instanceof TLRPC.TL_messageMediaDocument && media3.document instanceof TLRPC.TL_documentEmpty && media3.ttl_seconds != 0) {
                                                o2 = LocaleController.getString("AttachVideoExpired", 2131558734);
                                                o3 = o;
                                                n8 = n2;
                                            }
                                            else {
                                                final MessageObject message8 = this.message;
                                                if (message8.caption != null) {
                                                    String str2;
                                                    if (message8.isVideo()) {
                                                        str2 = "\ud83d\udcf9 ";
                                                    }
                                                    else if (this.message.isVoice()) {
                                                        str2 = "\ud83c\udfa4 ";
                                                    }
                                                    else if (this.message.isMusic()) {
                                                        str2 = "\ud83c\udfa7 ";
                                                    }
                                                    else if (this.message.isPhoto()) {
                                                        str2 = "\ud83d\uddbc ";
                                                    }
                                                    else {
                                                        str2 = "\ud83d\udcce ";
                                                    }
                                                    final StringBuilder sb2 = new StringBuilder();
                                                    sb2.append(str2);
                                                    sb2.append((Object)this.message.caption);
                                                    o2 = sb2.toString();
                                                    o3 = o;
                                                    n8 = n2;
                                                }
                                                else {
                                                    CharSequence charSequence6;
                                                    if (message8.messageOwner.media instanceof TLRPC.TL_messageMediaGame) {
                                                        final StringBuilder sb3 = new StringBuilder();
                                                        sb3.append("\ud83c\udfae ");
                                                        sb3.append(this.message.messageOwner.media.game.title);
                                                        charSequence6 = sb3.toString();
                                                    }
                                                    else if (message8.type == 14) {
                                                        charSequence6 = String.format("\ud83c\udfa7 %s - %s", message8.getMusicAuthor(), this.message.getMusicTitle());
                                                    }
                                                    else {
                                                        charSequence6 = message8.messageText;
                                                    }
                                                    final MessageObject message9 = this.message;
                                                    o2 = charSequence6;
                                                    o3 = o;
                                                    n8 = n2;
                                                    if (message9.messageOwner.media != null) {
                                                        o2 = charSequence6;
                                                        o3 = o;
                                                        n8 = n2;
                                                        if (!message9.isMediaEmpty()) {
                                                            o3 = Theme.dialogs_messagePrintingPaint;
                                                            o2 = charSequence6;
                                                            n8 = n2;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    o = o3;
                                    n6 = 1;
                                    n7 = 1;
                                    n2 = n8;
                                }
                                charSequence4 = null;
                            }
                            if (this.currentDialogFolderId != 0) {
                                charSequence4 = this.formatArchivedDialogNames();
                            }
                            final int n10 = n6;
                            n5 = n7;
                            dialogs_messagePrintingPaint = o;
                            n = n10;
                            break Label_4597;
                        }
                    }
                    final CharSequence charSequence7 = null;
                    lastPrintString = s7;
                    charSequence3 = charSequence7;
                    dialogs_messagePrintingPaint = textPaint;
                }
                n = 1;
                o2 = lastPrintString;
                n5 = 1;
                charSequence4 = charSequence3;
            }
            final TLRPC.DraftMessage draftMessage2 = this.draftMessage;
            String s11;
            if (draftMessage2 != null) {
                s11 = LocaleController.stringForMessageListDate(draftMessage2.date);
            }
            else {
                final int lastMessageDate2 = this.lastMessageDate;
                if (lastMessageDate2 != 0) {
                    s11 = LocaleController.stringForMessageListDate(lastMessageDate2);
                }
                else {
                    final MessageObject message10 = this.message;
                    if (message10 != null) {
                        s11 = LocaleController.stringForMessageListDate(message10.messageOwner.date);
                    }
                    else {
                        s11 = "";
                    }
                }
            }
            final MessageObject message11 = this.message;
            String s12 = null;
            Label_5244: {
                if (message11 == null) {
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawClock = false;
                    this.drawCount = false;
                    this.drawMention = false;
                    this.drawError = false;
                    s12 = null;
                    s3 = null;
                }
                else {
                    String format4 = null;
                    String s13 = null;
                    Label_4993: {
                        if (this.currentDialogFolderId != 0) {
                            final int unreadCount = this.unreadCount;
                            final int mentionCount = this.mentionCount;
                            if (unreadCount + mentionCount > 0) {
                                if (unreadCount <= mentionCount) {
                                    this.drawCount = false;
                                    this.drawMention = true;
                                    format4 = String.format("%d", unreadCount + mentionCount);
                                    s13 = null;
                                    break Label_4993;
                                }
                                this.drawCount = true;
                                this.drawMention = false;
                                s13 = String.format("%d", unreadCount + mentionCount);
                            }
                            else {
                                this.drawCount = false;
                                this.drawMention = false;
                                s13 = null;
                            }
                        }
                        else {
                            Label_4965: {
                                if (this.clearingDialog) {
                                    this.drawCount = false;
                                    n2 = 0;
                                }
                                else {
                                    final int unreadCount2 = this.unreadCount;
                                    if (unreadCount2 != 0 && (unreadCount2 != 1 || unreadCount2 != this.mentionCount || message11 == null || !message11.messageOwner.mentioned)) {
                                        this.drawCount = true;
                                        s13 = String.format("%d", this.unreadCount);
                                        break Label_4965;
                                    }
                                    if (this.markUnread) {
                                        this.drawCount = true;
                                        s13 = "";
                                        break Label_4965;
                                    }
                                    this.drawCount = false;
                                }
                                s13 = null;
                            }
                            if (this.mentionCount != 0) {
                                this.drawMention = true;
                                format4 = "@";
                                break Label_4993;
                            }
                            this.drawMention = false;
                        }
                        format4 = null;
                    }
                    if (this.message.isOut() && this.draftMessage == null && n2 != 0) {
                        final MessageObject message12 = this.message;
                        if (!(message12.messageOwner.action instanceof TLRPC.TL_messageActionHistoryClear)) {
                            if (message12.isSending()) {
                                this.drawCheck1 = false;
                                this.drawCheck2 = false;
                                this.drawClock = true;
                                this.drawError = false;
                                s12 = s13;
                                s3 = format4;
                                break Label_5244;
                            }
                            if (this.message.isSendError()) {
                                this.drawCheck1 = false;
                                this.drawCheck2 = false;
                                this.drawClock = false;
                                this.drawError = true;
                                this.drawCount = false;
                                this.drawMention = false;
                                s12 = s13;
                                s3 = format4;
                                break Label_5244;
                            }
                            s12 = s13;
                            s3 = format4;
                            if (this.message.isSent()) {
                                this.drawCheck1 = (!this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup));
                                this.drawCheck2 = true;
                                this.drawClock = false;
                                this.drawError = false;
                                s12 = s13;
                                s3 = format4;
                            }
                            break Label_5244;
                        }
                    }
                    this.drawCheck1 = false;
                    this.drawCheck2 = false;
                    this.drawClock = false;
                    this.drawError = false;
                    s3 = format4;
                    s12 = s13;
                }
            }
            String string2;
            if (this.dialogsType == 0 && MessagesController.getInstance(this.currentAccount).isProxyDialog(this.currentDialogId, true)) {
                this.drawPinBackground = true;
                string2 = LocaleController.getString("UseProxySponsor", 2131560980);
            }
            else {
                string2 = s11;
            }
            String s14;
            if (this.currentDialogFolderId != 0) {
                s14 = LocaleController.getString("ArchivedChats", 2131558653);
            }
            else {
                final TLRPC.Chat chat8 = this.chat;
                String s15;
                if (chat8 != null) {
                    s15 = chat8.title;
                }
                else {
                    final TLRPC.User user5 = this.user;
                    if (user5 != null) {
                        if (UserObject.isUserSelf(user5)) {
                            if (this.dialogsType == 3) {
                                this.drawPinBackground = true;
                            }
                            s15 = LocaleController.getString("SavedMessages", 2131560633);
                        }
                        else {
                            final int id = this.user.id;
                            if (id / 1000 != 777 && id / 1000 != 333 && ContactsController.getInstance(this.currentAccount).contactsDict.get(this.user.id) == null) {
                                if (ContactsController.getInstance(this.currentAccount).contactsDict.size() == 0 && (!ContactsController.getInstance(this.currentAccount).contactsLoaded || ContactsController.getInstance(this.currentAccount).isLoadingContacts())) {
                                    s15 = UserObject.getUserName(this.user);
                                }
                                else {
                                    final String phone = this.user.phone;
                                    if (phone != null && phone.length() != 0) {
                                        final PhoneFormat instance = PhoneFormat.getInstance();
                                        final StringBuilder sb4 = new StringBuilder();
                                        sb4.append("+");
                                        sb4.append(this.user.phone);
                                        s15 = instance.format(sb4.toString());
                                    }
                                    else {
                                        s15 = UserObject.getUserName(this.user);
                                    }
                                }
                            }
                            else {
                                s15 = UserObject.getUserName(this.user);
                            }
                        }
                    }
                    else {
                        s15 = "";
                    }
                }
                s14 = s15;
                if (s15.length() == 0) {
                    s14 = LocaleController.getString("HiddenName", 2131559636);
                }
            }
            final Object o5 = dialogs_messagePrintingPaint;
            charSequence = charSequence4;
            n4 = n5;
            format2 = s12;
            s4 = string2;
            name = s14;
            dialogs_messagePaint3 = o5;
        }
        int n11;
        if (n != 0) {
            n11 = (int)Math.ceil(Theme.dialogs_timePaint.measureText(s4));
            this.timeLayout = new StaticLayout((CharSequence)s4, Theme.dialogs_timePaint, n11, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = this.getMeasuredWidth() - AndroidUtilities.dp(15.0f) - n11;
            }
            else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
        }
        else {
            this.timeLayout = null;
            this.timeLeft = 0;
            n11 = 0;
        }
        int n12;
        if (!LocaleController.isRTL) {
            n12 = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp(14.0f) - n11;
        }
        else {
            n12 = this.getMeasuredWidth() - this.nameLeft - AndroidUtilities.dp(77.0f) - n11;
            this.nameLeft += n11;
        }
        int n15 = 0;
        Label_5894: {
            int n13;
            int n14;
            if (this.drawNameLock) {
                n13 = AndroidUtilities.dp(4.0f);
                n14 = Theme.dialogs_lockDrawable.getIntrinsicWidth();
            }
            else if (this.drawNameGroup) {
                n13 = AndroidUtilities.dp(4.0f);
                n14 = Theme.dialogs_groupDrawable.getIntrinsicWidth();
            }
            else if (this.drawNameBroadcast) {
                n13 = AndroidUtilities.dp(4.0f);
                n14 = Theme.dialogs_broadcastDrawable.getIntrinsicWidth();
            }
            else {
                n15 = n12;
                if (!this.drawNameBot) {
                    break Label_5894;
                }
                n13 = AndroidUtilities.dp(4.0f);
                n14 = Theme.dialogs_botDrawable.getIntrinsicWidth();
            }
            n15 = n12 - (n13 + n14);
        }
        int n17;
        if (this.drawClock) {
            final int n16 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            n17 = n15 - n16;
            if (!LocaleController.isRTL) {
                this.checkDrawLeft = this.timeLeft - n16;
            }
            else {
                this.checkDrawLeft = this.timeLeft + n11 + AndroidUtilities.dp(5.0f);
                this.nameLeft += n16;
            }
        }
        else {
            n17 = n15;
            if (this.drawCheck2) {
                final int n18 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
                n17 = n15 - n18;
                if (this.drawCheck1) {
                    n17 -= Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f);
                    if (!LocaleController.isRTL) {
                        this.halfCheckDrawLeft = this.timeLeft - n18;
                        this.checkDrawLeft = this.halfCheckDrawLeft - AndroidUtilities.dp(5.5f);
                    }
                    else {
                        this.checkDrawLeft = this.timeLeft + n11 + AndroidUtilities.dp(5.0f);
                        this.halfCheckDrawLeft = this.checkDrawLeft + AndroidUtilities.dp(5.5f);
                        this.nameLeft += n18 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f);
                    }
                }
                else if (!LocaleController.isRTL) {
                    this.checkDrawLeft = this.timeLeft - n18;
                }
                else {
                    this.checkDrawLeft = this.timeLeft + n11 + AndroidUtilities.dp(5.0f);
                    this.nameLeft += n18;
                }
            }
        }
        int b2;
        if (this.dialogMuted && !this.drawVerified && !this.drawScam) {
            final int n19 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
            b2 = n17 - n19;
            if (LocaleController.isRTL) {
                this.nameLeft += n19;
                b2 = b2;
            }
        }
        else if (this.drawVerified) {
            final int n20 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
            final int n21 = b2 = n17 - n20;
            if (LocaleController.isRTL) {
                this.nameLeft += n20;
                b2 = n21;
            }
        }
        else {
            b2 = n17;
            if (this.drawScam) {
                final int n22 = AndroidUtilities.dp(6.0f) + Theme.dialogs_scamDrawable.getIntrinsicWidth();
                final int n23 = b2 = n17 - n22;
                if (LocaleController.isRTL) {
                    this.nameLeft += n22;
                    b2 = n23;
                }
            }
        }
        final int max = Math.max(AndroidUtilities.dp(12.0f), b2);
        try {
            this.nameLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)name.replace('\n', ' '), Theme.dialogs_namePaint, (float)(max - AndroidUtilities.dp(12.0f)), TextUtils$TruncateAt.END), Theme.dialogs_namePaint, max, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
        }
        int n25;
        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            final int dp9 = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = AndroidUtilities.dp(39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            final int n24 = this.getMeasuredWidth() - AndroidUtilities.dp(95.0f);
            int dp11;
            if (!LocaleController.isRTL) {
                final int dp10 = AndroidUtilities.dp(76.0f);
                this.messageNameLeft = dp10;
                this.messageLeft = dp10;
                dp11 = AndroidUtilities.dp(10.0f);
            }
            else {
                final int dp12 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp12;
                this.messageLeft = dp12;
                dp11 = this.getMeasuredWidth() - AndroidUtilities.dp(64.0f);
            }
            this.avatarImage.setImageCoords(dp11, dp9, AndroidUtilities.dp(54.0f), AndroidUtilities.dp(54.0f));
            n25 = n24;
        }
        else {
            final int dp13 = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            final int n26 = this.getMeasuredWidth() - AndroidUtilities.dp(93.0f);
            int dp15;
            if (!LocaleController.isRTL) {
                final int dp14 = AndroidUtilities.dp(78.0f);
                this.messageNameLeft = dp14;
                this.messageLeft = dp14;
                dp15 = AndroidUtilities.dp(10.0f);
            }
            else {
                final int dp16 = AndroidUtilities.dp(16.0f);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                dp15 = this.getMeasuredWidth() - AndroidUtilities.dp(66.0f);
            }
            this.avatarImage.setImageCoords(dp15, dp13, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            n25 = n26;
        }
        if (this.drawPin) {
            if (!LocaleController.isRTL) {
                this.pinLeft = this.getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth() - AndroidUtilities.dp(14.0f);
            }
            else {
                this.pinLeft = AndroidUtilities.dp(14.0f);
            }
        }
        int b3;
        if (this.drawError) {
            final int dp17 = AndroidUtilities.dp(31.0f);
            b3 = n25 - dp17;
            if (!LocaleController.isRTL) {
                this.errorLeft = this.getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            }
            else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp17;
                this.messageNameLeft += dp17;
            }
        }
        else if (format2 == null && s3 == null) {
            int n27 = n25;
            if (this.drawPin) {
                final int n28 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                n27 = n25 - n28;
                if (LocaleController.isRTL) {
                    this.messageLeft += n28;
                    this.messageNameLeft += n28;
                    n27 = n27;
                }
            }
            this.drawCount = false;
            this.drawMention = false;
            b3 = n27;
        }
        else {
            int n30;
            if (format2 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int)Math.ceil(Theme.dialogs_countTextPaint.measureText(format2)));
                this.countLayout = new StaticLayout((CharSequence)format2, Theme.dialogs_countTextPaint, this.countWidth, Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                final int n29 = this.countWidth + AndroidUtilities.dp(18.0f);
                n30 = n25 - n29;
                if (!LocaleController.isRTL) {
                    this.countLeft = this.getMeasuredWidth() - this.countWidth - AndroidUtilities.dp(20.0f);
                }
                else {
                    this.countLeft = AndroidUtilities.dp(20.0f);
                    this.messageLeft += n29;
                    this.messageNameLeft += n29;
                }
                this.drawCount = true;
            }
            else {
                this.countWidth = 0;
                n30 = n25;
            }
            b3 = n30;
            if (s3 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int)Math.ceil(Theme.dialogs_countTextPaint.measureText(s3)));
                    this.mentionLayout = new StaticLayout((CharSequence)s3, Theme.dialogs_countTextPaint, this.mentionWidth, Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
                else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                final int n31 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                final int n32 = n30 - n31;
                if (!LocaleController.isRTL) {
                    final int measuredWidth5 = this.getMeasuredWidth();
                    final int mentionWidth = this.mentionWidth;
                    final int dp18 = AndroidUtilities.dp(20.0f);
                    final int countWidth = this.countWidth;
                    int n33;
                    if (countWidth != 0) {
                        n33 = countWidth + AndroidUtilities.dp(18.0f);
                    }
                    else {
                        n33 = 0;
                    }
                    this.mentionLeft = measuredWidth5 - mentionWidth - dp18 - n33;
                }
                else {
                    final int dp19 = AndroidUtilities.dp(20.0f);
                    final int countWidth2 = this.countWidth;
                    int n34;
                    if (countWidth2 != 0) {
                        n34 = AndroidUtilities.dp(18.0f) + countWidth2;
                    }
                    else {
                        n34 = 0;
                    }
                    this.mentionLeft = dp19 + n34;
                    this.messageLeft += n31;
                    this.messageNameLeft += n31;
                }
                this.drawMention = true;
                b3 = n32;
            }
        }
        Object replaceEmoji2 = o2;
        if (n4 != 0) {
            Object o6;
            if ((o6 = o2) == null) {
                o6 = "";
            }
            String s17;
            final String s16 = s17 = ((CharSequence)o6).toString();
            if (s16.length() > 150) {
                s17 = s16.substring(0, 150);
            }
            String replace = null;
            Label_7481: {
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    replace = s17;
                    if (charSequence == null) {
                        break Label_7481;
                    }
                }
                replace = s17.replace('\n', ' ');
            }
            replaceEmoji2 = Emoji.replaceEmoji(replace, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
        }
        final int max2 = Math.max(AndroidUtilities.dp(12.0f), b3);
        Label_7637: {
            Label_7593: {
                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && charSequence != null) {
                    if (this.currentDialogFolderId != 0) {
                        if (this.currentDialogFolderDialogsCount != 1) {
                            break Label_7593;
                        }
                    }
                    try {
                        this.messageNameLayout = StaticLayoutEx.createStaticLayout(charSequence, Theme.dialogs_messageNamePaint, max2, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils$TruncateAt.END, max2, 1);
                    }
                    catch (Exception ex4) {
                        FileLog.e(ex4);
                    }
                    this.messageTop = AndroidUtilities.dp(51.0f);
                    break Label_7637;
                }
            }
            this.messageNameLayout = null;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
            else {
                this.messageTop = AndroidUtilities.dp(32.0f);
            }
            try {
                CharSequence ellipsize;
                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                    dialogs_messagePaint3 = Theme.dialogs_messagePaint;
                    final CharSequence charSequence8 = null;
                    ellipsize = charSequence;
                    charSequence = charSequence8;
                }
                else if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || charSequence != null) {
                    ellipsize = TextUtils.ellipsize((CharSequence)replaceEmoji2, (TextPaint)dialogs_messagePaint3, (float)(max2 - AndroidUtilities.dp(12.0f)), TextUtils$TruncateAt.END);
                }
                else {
                    ellipsize = (CharSequence)replaceEmoji2;
                }
                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                    this.messageLayout = new StaticLayout(ellipsize, (TextPaint)dialogs_messagePaint3, max2, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
                else {
                    final Layout$Alignment align_NORMAL = Layout$Alignment.ALIGN_NORMAL;
                    final float n35 = (float)AndroidUtilities.dp(1.0f);
                    final TextUtils$TruncateAt end = TextUtils$TruncateAt.END;
                    int n36;
                    if (charSequence != null) {
                        n36 = 1;
                    }
                    else {
                        n36 = 2;
                    }
                    this.messageLayout = StaticLayoutEx.createStaticLayout(ellipsize, (TextPaint)dialogs_messagePaint3, max2, align_NORMAL, 1.0f, n35, false, end, max2, n36);
                }
            }
            catch (Exception ex5) {
                FileLog.e(ex5);
            }
        }
        if (LocaleController.isRTL) {
            final StaticLayout nameLayout = this.nameLayout;
            if (nameLayout != null && nameLayout.getLineCount() > 0) {
                final float lineLeft = this.nameLayout.getLineLeft(0);
                final double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                if (this.dialogMuted && !this.drawVerified && !this.drawScam) {
                    final double v = this.nameLeft;
                    final double v2 = max;
                    Double.isNaN(v2);
                    Double.isNaN(v);
                    final double v3 = AndroidUtilities.dp(6.0f);
                    Double.isNaN(v3);
                    final double v4 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                    Double.isNaN(v4);
                    this.nameMuteLeft = (int)(v + (v2 - ceil) - v3 - v4);
                }
                else if (this.drawVerified) {
                    final double v5 = this.nameLeft;
                    final double v6 = max;
                    Double.isNaN(v6);
                    Double.isNaN(v5);
                    final double v7 = AndroidUtilities.dp(6.0f);
                    Double.isNaN(v7);
                    final double v8 = Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                    Double.isNaN(v8);
                    this.nameMuteLeft = (int)(v5 + (v6 - ceil) - v7 - v8);
                }
                else if (this.drawScam) {
                    final double v9 = this.nameLeft;
                    final double v10 = max;
                    Double.isNaN(v10);
                    Double.isNaN(v9);
                    final double v11 = AndroidUtilities.dp(6.0f);
                    Double.isNaN(v11);
                    final double v12 = Theme.dialogs_scamDrawable.getIntrinsicWidth();
                    Double.isNaN(v12);
                    this.nameMuteLeft = (int)(v9 + (v10 - ceil) - v11 - v12);
                }
                if (lineLeft == 0.0f) {
                    final double v13 = max;
                    if (ceil < v13) {
                        final double v14 = this.nameLeft;
                        Double.isNaN(v13);
                        Double.isNaN(v14);
                        this.nameLeft = (int)(v14 + (v13 - ceil));
                    }
                }
            }
            final StaticLayout messageLayout = this.messageLayout;
            if (messageLayout != null) {
                final int lineCount = messageLayout.getLineCount();
                if (lineCount > 0) {
                    int n37 = 0;
                    int min = Integer.MAX_VALUE;
                    int n38;
                    while (true) {
                        n38 = min;
                        if (n37 >= lineCount) {
                            break;
                        }
                        if (this.messageLayout.getLineLeft(n37) != 0.0f) {
                            n38 = 0;
                            break;
                        }
                        final double ceil2 = Math.ceil(this.messageLayout.getLineWidth(n37));
                        final double v15 = max2;
                        Double.isNaN(v15);
                        min = Math.min(min, (int)(v15 - ceil2));
                        ++n37;
                    }
                    if (n38 != Integer.MAX_VALUE) {
                        this.messageLeft += n38;
                    }
                }
            }
            final StaticLayout messageNameLayout = this.messageNameLayout;
            if (messageNameLayout != null && messageNameLayout.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                final double ceil3 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                final double v16 = max2;
                if (ceil3 < v16) {
                    final double v17 = this.messageNameLeft;
                    Double.isNaN(v16);
                    Double.isNaN(v17);
                    this.messageNameLeft = (int)(v17 + (v16 - ceil3));
                }
            }
        }
        else {
            final StaticLayout nameLayout2 = this.nameLayout;
            if (nameLayout2 != null && nameLayout2.getLineCount() > 0) {
                final float lineRight = this.nameLayout.getLineRight(0);
                if (lineRight == max) {
                    final double ceil4 = Math.ceil(this.nameLayout.getLineWidth(0));
                    final double v18 = max;
                    if (ceil4 < v18) {
                        final double v19 = this.nameLeft;
                        Double.isNaN(v18);
                        Double.isNaN(v19);
                        this.nameLeft = (int)(v19 - (v18 - ceil4));
                    }
                }
                if (this.dialogMuted || this.drawVerified || this.drawScam) {
                    this.nameMuteLeft = (int)(this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                }
            }
            final StaticLayout messageLayout2 = this.messageLayout;
            if (messageLayout2 != null) {
                final int lineCount2 = messageLayout2.getLineCount();
                if (lineCount2 > 0) {
                    int i = 0;
                    float min2 = 2.14748365E9f;
                    while (i < lineCount2) {
                        min2 = Math.min(min2, this.messageLayout.getLineLeft(i));
                        ++i;
                    }
                    this.messageLeft -= (int)min2;
                }
            }
            final StaticLayout messageNameLayout2 = this.messageNameLayout;
            if (messageNameLayout2 != null && messageNameLayout2.getLineCount() > 0) {
                this.messageNameLeft -= (int)this.messageNameLayout.getLineLeft(0);
            }
        }
    }
    
    public void checkCurrentDialogIndex(final boolean b) {
        final ArrayList<TLRPC.Dialog> dialogsArray = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, b);
        if (this.index < dialogsArray.size()) {
            final TLRPC.Dialog dialog = dialogsArray.get(this.index);
            final int index = this.index;
            final boolean b2 = true;
            TLRPC.Dialog dialog2;
            if (index + 1 < dialogsArray.size()) {
                dialog2 = dialogsArray.get(this.index + 1);
            }
            else {
                dialog2 = null;
            }
            final TLRPC.DraftMessage draft = DataQuery.getInstance(this.currentAccount).getDraft(this.currentDialogId);
            MessageObject folderTopMessage;
            if (this.currentDialogFolderId != 0) {
                folderTopMessage = this.findFolderTopMessage();
            }
            else {
                folderTopMessage = (MessageObject)MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
            }
            if (this.currentDialogId == dialog.id) {
                final MessageObject message = this.message;
                if ((message == null || message.getId() == dialog.top_message) && (folderTopMessage == null || folderTopMessage.messageOwner.edit_date == this.currentEditDate) && this.unreadCount == dialog.unread_count && this.mentionCount == dialog.unread_mentions_count && this.markUnread == dialog.unread_mark) {
                    final MessageObject message2 = this.message;
                    if (message2 == folderTopMessage && (message2 != null || folderTopMessage == null) && draft == this.draftMessage && this.drawPin == dialog.pinned) {
                        return;
                    }
                }
            }
            final boolean b3 = this.currentDialogId != dialog.id;
            this.currentDialogId = dialog.id;
            final boolean b4 = dialog instanceof TLRPC.TL_dialogFolder;
            if (b4) {
                this.currentDialogFolderId = ((TLRPC.TL_dialogFolder)dialog).folder.id;
            }
            else {
                this.currentDialogFolderId = 0;
            }
            this.fullSeparator = (dialog instanceof TLRPC.TL_dialog && dialog.pinned && dialog2 != null && !dialog2.pinned);
            this.fullSeparator2 = (b4 && dialog2 != null && !dialog2.pinned && b2);
            this.update(0);
            if (b3) {
                float reorderIconProgress;
                if (this.drawPin && this.drawReorder) {
                    reorderIconProgress = 1.0f;
                }
                else {
                    reorderIconProgress = 0.0f;
                }
                this.reorderIconProgress = reorderIconProgress;
            }
            this.checkOnline();
        }
    }
    
    public float getClipProgress() {
        return this.clipProgress;
    }
    
    public long getDialogId() {
        return this.currentDialogId;
    }
    
    public int getDialogIndex() {
        return this.index;
    }
    
    public int getMessageId() {
        return this.messageId;
    }
    
    public float getTranslationX() {
        return this.translationX;
    }
    
    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        if (drawable != this.translationDrawable && drawable != Theme.dialogs_archiveAvatarDrawable) {
            super.invalidateDrawable(drawable);
        }
        else {
            this.invalidate(drawable.getBounds());
        }
    }
    
    public boolean isPointInsideAvatar(final float n, final float n2) {
        final boolean isRTL = LocaleController.isRTL;
        final boolean b = true;
        boolean b2 = true;
        if (!isRTL) {
            if (n < 0.0f || n >= AndroidUtilities.dp(60.0f)) {
                b2 = false;
            }
            return b2;
        }
        return n >= this.getMeasuredWidth() - AndroidUtilities.dp(60.0f) && n < this.getMeasuredWidth() && b;
    }
    
    public boolean isUnread() {
        return (this.unreadCount != 0 || this.markUnread) && !this.dialogMuted;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        this.archiveHidden = SharedConfig.archiveHidden;
        final boolean archiveHidden = this.archiveHidden;
        final float n = 1.0f;
        float archiveBackgroundProgress;
        if (archiveHidden) {
            archiveBackgroundProgress = 0.0f;
        }
        else {
            archiveBackgroundProgress = 1.0f;
        }
        this.archiveBackgroundProgress = archiveBackgroundProgress;
        this.avatarDrawable.setArchivedAvatarHiddenProgress(this.archiveBackgroundProgress);
        this.clipProgress = 0.0f;
        this.isSliding = false;
        float reorderIconProgress;
        if (this.drawPin && this.drawReorder) {
            reorderIconProgress = n;
        }
        else {
            reorderIconProgress = 0.0f;
        }
        this.reorderIconProgress = reorderIconProgress;
        this.attachedToWindow = true;
        this.setTranslationX(this.cornerProgress = 0.0f);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isSliding = false;
        this.drawRevealBackground = false;
        this.currentRevealProgress = 0.0f;
        this.attachedToWindow = false;
        float reorderIconProgress;
        if (this.drawPin && this.drawReorder) {
            reorderIconProgress = 1.0f;
        }
        else {
            reorderIconProgress = 0.0f;
        }
        this.reorderIconProgress = reorderIconProgress;
        this.avatarImage.onDetachedFromWindow();
        final Drawable translationDrawable = this.translationDrawable;
        if (translationDrawable != null) {
            if (translationDrawable instanceof LottieDrawable) {
                final LottieDrawable lottieDrawable = (LottieDrawable)translationDrawable;
                lottieDrawable.stop();
                lottieDrawable.setProgress(0.0f);
                lottieDrawable.setCallback((Drawable$Callback)null);
            }
            this.translationDrawable = null;
            this.translationAnimationStarted = false;
        }
    }
    
    @SuppressLint({ "DrawAllocation" })
    protected void onDraw(final Canvas canvas) {
        if (this.currentDialogId == 0L && this.customDialog == null) {
            return;
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        long n;
        if ((n = uptimeMillis - this.lastUpdateTime) > 17L) {
            n = 17L;
        }
        this.lastUpdateTime = uptimeMillis;
        if (this.clipProgress != 0.0f && Build$VERSION.SDK_INT != 24) {
            canvas.save();
            canvas.clipRect(0.0f, this.topClip * this.clipProgress, (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - (int)(this.bottomClip * this.clipProgress)));
        }
        if (this.translationX == 0.0f && this.cornerProgress == 0.0f) {
            final Drawable translationDrawable = this.translationDrawable;
            if (translationDrawable != null) {
                if (translationDrawable instanceof LottieDrawable) {
                    final LottieDrawable lottieDrawable = (LottieDrawable)translationDrawable;
                    lottieDrawable.stop();
                    lottieDrawable.setProgress(0.0f);
                    lottieDrawable.setCallback((Drawable$Callback)null);
                }
                this.translationDrawable = null;
                this.translationAnimationStarted = false;
            }
        }
        else {
            canvas.save();
            int color;
            int color2;
            String s;
            if (this.currentDialogFolderId != 0) {
                if (this.archiveHidden) {
                    color = Theme.getColor("chats_archivePinBackground");
                    color2 = Theme.getColor("chats_archiveBackground");
                    s = LocaleController.getString("UnhideFromTop", 2131560936);
                    this.translationDrawable = Theme.dialogs_unpinArchiveDrawable;
                }
                else {
                    color = Theme.getColor("chats_archiveBackground");
                    color2 = Theme.getColor("chats_archivePinBackground");
                    s = LocaleController.getString("HideOnTop", 2131559637);
                    this.translationDrawable = Theme.dialogs_pinArchiveDrawable;
                }
            }
            else if (this.folderId == 0) {
                color = Theme.getColor("chats_archiveBackground");
                color2 = Theme.getColor("chats_archivePinBackground");
                s = LocaleController.getString("Archive", 2131558642);
                this.translationDrawable = Theme.dialogs_archiveDrawable;
            }
            else {
                color = Theme.getColor("chats_archivePinBackground");
                color2 = Theme.getColor("chats_archiveBackground");
                s = LocaleController.getString("Unarchive", 2131560928);
                this.translationDrawable = Theme.dialogs_unarchiveDrawable;
            }
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > AndroidUtilities.dp(43.0f)) {
                this.translationAnimationStarted = true;
                final Drawable translationDrawable2 = this.translationDrawable;
                if (translationDrawable2 instanceof LottieDrawable) {
                    final LottieDrawable lottieDrawable2 = (LottieDrawable)translationDrawable2;
                    lottieDrawable2.setProgress(0.0f);
                    lottieDrawable2.setCallback((Drawable$Callback)this);
                    lottieDrawable2.start();
                }
            }
            final float n2 = this.getMeasuredWidth() + this.translationX;
            if (this.currentRevealProgress < 1.0f) {
                Theme.dialogs_pinnedPaint.setColor(color);
                canvas.drawRect(n2 - AndroidUtilities.dp(8.0f), 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                if (this.currentRevealProgress == 0.0f && Theme.dialogs_archiveDrawableRecolored) {
                    final Drawable dialogs_archiveDrawable = Theme.dialogs_archiveDrawable;
                    if (dialogs_archiveDrawable instanceof LottieDrawable) {
                        ((LottieDrawable)dialogs_archiveDrawable).addValueCallback(new KeyPath(new String[] { "Arrow", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("chats_archiveBackground"))));
                    }
                    Theme.dialogs_archiveDrawableRecolored = false;
                }
            }
            final int n3 = this.getMeasuredWidth() - AndroidUtilities.dp(43.0f) - this.translationDrawable.getIntrinsicWidth() / 2;
            float n4;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                n4 = 9.0f;
            }
            else {
                n4 = 12.0f;
            }
            int dp;
            final int n5 = dp = AndroidUtilities.dp(n4);
            if (!(this.translationDrawable instanceof LottieDrawable)) {
                dp = n5 + AndroidUtilities.dp(2.0f);
            }
            final int n6 = this.translationDrawable.getIntrinsicWidth() / 2 + n3;
            final int n7 = this.translationDrawable.getIntrinsicHeight() / 2 + dp;
            if (this.currentRevealProgress > 0.0f) {
                canvas.save();
                canvas.clipRect(n2 - AndroidUtilities.dp(8.0f), 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(color2);
                canvas.drawCircle((float)n6, (float)n7, (float)Math.sqrt(n6 * n6 + (n7 - this.getMeasuredHeight()) * (n7 - this.getMeasuredHeight())) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (!Theme.dialogs_archiveDrawableRecolored) {
                    final Drawable dialogs_archiveDrawable2 = Theme.dialogs_archiveDrawable;
                    if (dialogs_archiveDrawable2 instanceof LottieDrawable) {
                        ((LottieDrawable)dialogs_archiveDrawable2).addValueCallback(new KeyPath(new String[] { "Arrow", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("chats_archivePinBackground"))));
                    }
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
            }
            canvas.save();
            canvas.translate((float)n3, (float)dp);
            final float currentRevealBounceProgress = this.currentRevealBounceProgress;
            if (currentRevealBounceProgress != 0.0f && currentRevealBounceProgress != 1.0f) {
                final float n8 = this.interpolator.getInterpolation(currentRevealBounceProgress) + 1.0f;
                canvas.scale(n8, n8, (float)(this.translationDrawable.getIntrinsicWidth() / 2), (float)(this.translationDrawable.getIntrinsicHeight() / 2));
            }
            BaseCell.setDrawableBounds(this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(n2, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
            final float n9 = (float)(this.getMeasuredWidth() - AndroidUtilities.dp(43.0f) - (int)Math.ceil(Theme.dialogs_countTextPaint.measureText(s)) / 2);
            float n10;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                n10 = 59.0f;
            }
            else {
                n10 = 62.0f;
            }
            canvas.drawText(s, n9, (float)AndroidUtilities.dp(n10), (Paint)Theme.dialogs_archiveTextPaint);
            canvas.restore();
        }
        if (this.translationX != 0.0f) {
            canvas.save();
            canvas.translate(this.translationX, 0.0f);
        }
        if (this.isSelected) {
            canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
        }
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
            Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay"), this.archiveBackgroundProgress, 1.0f));
            canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
        }
        else if (this.drawPin || this.drawPinBackground) {
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay"));
            canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
        }
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor("windowBackgroundWhite"));
            this.rect.set((float)(this.getMeasuredWidth() - AndroidUtilities.dp(64.0f)), 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(8.0f) * this.cornerProgress, AndroidUtilities.dp(8.0f) * this.cornerProgress, Theme.dialogs_pinnedPaint);
            if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
                Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor("chats_pinnedOverlay"), this.archiveBackgroundProgress, 1.0f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(8.0f) * this.cornerProgress, AndroidUtilities.dp(8.0f) * this.cornerProgress, Theme.dialogs_pinnedPaint);
            }
            else if (this.drawPin || this.drawPinBackground) {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor("chats_pinnedOverlay"));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(8.0f) * this.cornerProgress, AndroidUtilities.dp(8.0f) * this.cornerProgress, Theme.dialogs_pinnedPaint);
            }
            canvas.restore();
        }
        int n11 = 0;
        Label_1594: {
            Label_1591: {
                if (this.translationX != 0.0f) {
                    final float cornerProgress = this.cornerProgress;
                    if (cornerProgress >= 1.0f) {
                        break Label_1591;
                    }
                    this.cornerProgress = cornerProgress + n / 150.0f;
                    if (this.cornerProgress > 1.0f) {
                        this.cornerProgress = 1.0f;
                    }
                }
                else {
                    final float cornerProgress2 = this.cornerProgress;
                    if (cornerProgress2 <= 0.0f) {
                        break Label_1591;
                    }
                    this.cornerProgress = cornerProgress2 - n / 150.0f;
                    if (this.cornerProgress < 0.0f) {
                        this.cornerProgress = 0.0f;
                    }
                }
                n11 = 1;
                break Label_1594;
            }
            n11 = 0;
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
            Label_1846: {
                if (this.currentDialogFolderId != 0) {
                    final TextPaint dialogs_namePaint = Theme.dialogs_namePaint;
                    dialogs_namePaint.setColor(dialogs_namePaint.linkColor = Theme.getColor("chats_nameArchived"));
                }
                else {
                    if (this.encryptedChat == null) {
                        final CustomDialog customDialog = this.customDialog;
                        if (customDialog == null || customDialog.type != 2) {
                            final TextPaint dialogs_namePaint2 = Theme.dialogs_namePaint;
                            dialogs_namePaint2.setColor(dialogs_namePaint2.linkColor = Theme.getColor("chats_name"));
                            break Label_1846;
                        }
                    }
                    final TextPaint dialogs_namePaint3 = Theme.dialogs_namePaint;
                    dialogs_namePaint3.setColor(dialogs_namePaint3.linkColor = Theme.getColor("chats_secretName"));
                }
            }
            canvas.save();
            final float n12 = (float)this.nameLeft;
            float n13;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                n13 = 13.0f;
            }
            else {
                n13 = 10.0f;
            }
            canvas.translate(n12, (float)AndroidUtilities.dp(n13));
            this.nameLayout.draw(canvas);
            canvas.restore();
        }
        if (this.timeLayout != null && this.currentDialogFolderId == 0) {
            canvas.save();
            canvas.translate((float)this.timeLeft, (float)this.timeTop);
            this.timeLayout.draw(canvas);
            canvas.restore();
        }
        if (this.messageNameLayout != null) {
            if (this.currentDialogFolderId != 0) {
                final TextPaint dialogs_messageNamePaint = Theme.dialogs_messageNamePaint;
                dialogs_messageNamePaint.setColor(dialogs_messageNamePaint.linkColor = Theme.getColor("chats_nameMessageArchived_threeLines"));
            }
            else if (this.draftMessage != null) {
                final TextPaint dialogs_messageNamePaint2 = Theme.dialogs_messageNamePaint;
                dialogs_messageNamePaint2.setColor(dialogs_messageNamePaint2.linkColor = Theme.getColor("chats_draft"));
            }
            else {
                final TextPaint dialogs_messageNamePaint3 = Theme.dialogs_messageNamePaint;
                dialogs_messageNamePaint3.setColor(dialogs_messageNamePaint3.linkColor = Theme.getColor("chats_nameMessage_threeLines"));
            }
            canvas.save();
            canvas.translate((float)this.messageNameLeft, (float)this.messageNameTop);
            try {
                this.messageNameLayout.draw(canvas);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            canvas.restore();
        }
        if (this.messageLayout != null) {
            if (this.currentDialogFolderId != 0) {
                if (this.chat != null) {
                    final TextPaint dialogs_messagePaint = Theme.dialogs_messagePaint;
                    dialogs_messagePaint.setColor(dialogs_messagePaint.linkColor = Theme.getColor("chats_nameMessageArchived"));
                }
                else {
                    final TextPaint dialogs_messagePaint2 = Theme.dialogs_messagePaint;
                    dialogs_messagePaint2.setColor(dialogs_messagePaint2.linkColor = Theme.getColor("chats_messageArchived"));
                }
            }
            else {
                final TextPaint dialogs_messagePaint3 = Theme.dialogs_messagePaint;
                dialogs_messagePaint3.setColor(dialogs_messagePaint3.linkColor = Theme.getColor("chats_message"));
            }
            canvas.save();
            canvas.translate((float)this.messageLeft, (float)this.messageTop);
            try {
                this.messageLayout.draw(canvas);
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
            canvas.restore();
        }
        if (this.currentDialogFolderId == 0) {
            if (this.drawClock) {
                BaseCell.setDrawableBounds(Theme.dialogs_clockDrawable, this.checkDrawLeft, this.checkDrawTop);
                Theme.dialogs_clockDrawable.draw(canvas);
            }
            else if (this.drawCheck2) {
                if (this.drawCheck1) {
                    BaseCell.setDrawableBounds(Theme.dialogs_halfCheckDrawable, this.halfCheckDrawLeft, this.checkDrawTop);
                    Theme.dialogs_halfCheckDrawable.draw(canvas);
                    BaseCell.setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft, this.checkDrawTop);
                    Theme.dialogs_checkDrawable.draw(canvas);
                }
                else {
                    BaseCell.setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft, this.checkDrawTop);
                    Theme.dialogs_checkDrawable.draw(canvas);
                }
            }
        }
        if (this.dialogMuted && !this.drawVerified && !this.drawScam) {
            final Drawable dialogs_muteDrawable = Theme.dialogs_muteDrawable;
            final int nameMuteLeft = this.nameMuteLeft;
            float n14;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                n14 = 1.0f;
            }
            else {
                n14 = 0.0f;
            }
            final int dp2 = AndroidUtilities.dp(n14);
            float n15;
            if (SharedConfig.useThreeLinesLayout) {
                n15 = 13.5f;
            }
            else {
                n15 = 17.5f;
            }
            BaseCell.setDrawableBounds(dialogs_muteDrawable, nameMuteLeft - dp2, AndroidUtilities.dp(n15));
            Theme.dialogs_muteDrawable.draw(canvas);
        }
        else if (this.drawVerified) {
            final Drawable dialogs_verifiedDrawable = Theme.dialogs_verifiedDrawable;
            final int nameMuteLeft2 = this.nameMuteLeft;
            float n16;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                n16 = 16.5f;
            }
            else {
                n16 = 12.5f;
            }
            BaseCell.setDrawableBounds(dialogs_verifiedDrawable, nameMuteLeft2, AndroidUtilities.dp(n16));
            final Drawable dialogs_verifiedCheckDrawable = Theme.dialogs_verifiedCheckDrawable;
            final int nameMuteLeft3 = this.nameMuteLeft;
            float n17;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                n17 = 16.5f;
            }
            else {
                n17 = 12.5f;
            }
            BaseCell.setDrawableBounds(dialogs_verifiedCheckDrawable, nameMuteLeft3, AndroidUtilities.dp(n17));
            Theme.dialogs_verifiedDrawable.draw(canvas);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas);
        }
        else if (this.drawScam) {
            final ScamDrawable dialogs_scamDrawable = Theme.dialogs_scamDrawable;
            final int nameMuteLeft4 = this.nameMuteLeft;
            float n18;
            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                n18 = 15.0f;
            }
            else {
                n18 = 12.0f;
            }
            BaseCell.setDrawableBounds(dialogs_scamDrawable, nameMuteLeft4, AndroidUtilities.dp(n18));
            Theme.dialogs_scamDrawable.draw(canvas);
        }
        if (this.drawReorder || this.reorderIconProgress != 0.0f) {
            Theme.dialogs_reorderDrawable.setAlpha((int)(this.reorderIconProgress * 255.0f));
            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
            Theme.dialogs_reorderDrawable.draw(canvas);
        }
        if (this.drawError) {
            Theme.dialogs_errorDrawable.setAlpha((int)((1.0f - this.reorderIconProgress) * 255.0f));
            final RectF rect = this.rect;
            final int errorLeft = this.errorLeft;
            rect.set((float)errorLeft, (float)this.errorTop, (float)(errorLeft + AndroidUtilities.dp(23.0f)), (float)(this.errorTop + AndroidUtilities.dp(23.0f)));
            final RectF rect2 = this.rect;
            final float density = AndroidUtilities.density;
            canvas.drawRoundRect(rect2, density * 11.5f, density * 11.5f, Theme.dialogs_errorPaint);
            BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
            Theme.dialogs_errorDrawable.draw(canvas);
        }
        else if (!this.drawCount && !this.drawMention) {
            if (this.drawPin) {
                Theme.dialogs_pinnedDrawable.setAlpha((int)((1.0f - this.reorderIconProgress) * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_pinnedDrawable.draw(canvas);
            }
        }
        else {
            if (this.drawCount) {
                Paint paint;
                if (!this.dialogMuted && this.currentDialogFolderId == 0) {
                    paint = Theme.dialogs_countPaint;
                }
                else {
                    paint = Theme.dialogs_countGrayPaint;
                }
                paint.setAlpha((int)((1.0f - this.reorderIconProgress) * 255.0f));
                Theme.dialogs_countTextPaint.setAlpha((int)((1.0f - this.reorderIconProgress) * 255.0f));
                final int n19 = this.countLeft - AndroidUtilities.dp(5.5f);
                this.rect.set((float)n19, (float)this.countTop, (float)(n19 + this.countWidth + AndroidUtilities.dp(11.0f)), (float)(this.countTop + AndroidUtilities.dp(23.0f)));
                final RectF rect3 = this.rect;
                final float density2 = AndroidUtilities.density;
                canvas.drawRoundRect(rect3, density2 * 11.5f, density2 * 11.5f, paint);
                if (this.countLayout != null) {
                    canvas.save();
                    canvas.translate((float)this.countLeft, (float)(this.countTop + AndroidUtilities.dp(4.0f)));
                    this.countLayout.draw(canvas);
                    canvas.restore();
                }
            }
            if (this.drawMention) {
                Theme.dialogs_countPaint.setAlpha((int)((1.0f - this.reorderIconProgress) * 255.0f));
                final int n20 = this.mentionLeft - AndroidUtilities.dp(5.5f);
                this.rect.set((float)n20, (float)this.countTop, (float)(n20 + this.mentionWidth + AndroidUtilities.dp(11.0f)), (float)(this.countTop + AndroidUtilities.dp(23.0f)));
                Paint paint2;
                if (this.dialogMuted && this.folderId != 0) {
                    paint2 = Theme.dialogs_countGrayPaint;
                }
                else {
                    paint2 = Theme.dialogs_countPaint;
                }
                final RectF rect4 = this.rect;
                final float density3 = AndroidUtilities.density;
                canvas.drawRoundRect(rect4, density3 * 11.5f, density3 * 11.5f, paint2);
                if (this.mentionLayout != null) {
                    Theme.dialogs_countTextPaint.setAlpha((int)((1.0f - this.reorderIconProgress) * 255.0f));
                    canvas.save();
                    canvas.translate((float)this.mentionLeft, (float)(this.countTop + AndroidUtilities.dp(4.0f)));
                    this.mentionLayout.draw(canvas);
                    canvas.restore();
                }
                else {
                    Theme.dialogs_mentionDrawable.setAlpha((int)((1.0f - this.reorderIconProgress) * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    Theme.dialogs_mentionDrawable.draw(canvas);
                }
            }
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            final float n21 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + 1.0f;
            canvas.scale(n21, n21, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        }
        this.avatarImage.draw(canvas);
        if (this.animatingArchiveAvatar) {
            canvas.restore();
        }
        final TLRPC.User user = this.user;
        Label_3932: {
            if (user != null && this.isDialogCell && this.currentDialogFolderId == 0 && !MessagesController.isSupportUser(user)) {
                final TLRPC.User user2 = this.user;
                if (!user2.bot) {
                    boolean b = false;
                    Label_3590: {
                        if (!user2.self) {
                            final TLRPC.UserStatus status = user2.status;
                            if ((status != null && status.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(this.user.id)) {
                                b = true;
                                break Label_3590;
                            }
                        }
                        b = false;
                    }
                    if (b || this.onlineProgress != 0.0f) {
                        final int imageY2 = this.avatarImage.getImageY2();
                        float n22;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            n22 = 8.0f;
                        }
                        else {
                            n22 = 6.0f;
                        }
                        final int dp3 = AndroidUtilities.dp(n22);
                        int n24;
                        if (LocaleController.isRTL) {
                            final int imageX = this.avatarImage.getImageX();
                            float n23;
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                n23 = 6.0f;
                            }
                            else {
                                n23 = 10.0f;
                            }
                            n24 = imageX + AndroidUtilities.dp(n23);
                        }
                        else {
                            final int imageX2 = this.avatarImage.getImageX2();
                            float n25;
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                n25 = 6.0f;
                            }
                            else {
                                n25 = 10.0f;
                            }
                            n24 = imageX2 - AndroidUtilities.dp(n25);
                        }
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("windowBackgroundWhite"));
                        final float n26 = (float)n24;
                        final float n27 = (float)(imageY2 - dp3);
                        canvas.drawCircle(n26, n27, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("chats_onlineCircle"));
                        canvas.drawCircle(n26, n27, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                        if (b) {
                            final float onlineProgress = this.onlineProgress;
                            if (onlineProgress >= 1.0f) {
                                break Label_3932;
                            }
                            this.onlineProgress = onlineProgress + n / 150.0f;
                            if (this.onlineProgress > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                        }
                        else {
                            final float onlineProgress2 = this.onlineProgress;
                            if (onlineProgress2 <= 0.0f) {
                                break Label_3932;
                            }
                            this.onlineProgress = onlineProgress2 - n / 150.0f;
                            if (this.onlineProgress < 0.0f) {
                                this.onlineProgress = 0.0f;
                            }
                        }
                        n11 = 1;
                    }
                }
            }
        }
        if (this.translationX != 0.0f) {
            canvas.restore();
        }
        if (this.useSeparator) {
            int dp4;
            if (!this.fullSeparator && (this.currentDialogFolderId == 0 || !this.archiveHidden || this.fullSeparator2) && (!this.fullSeparator2 || this.archiveHidden)) {
                dp4 = AndroidUtilities.dp(72.0f);
            }
            else {
                dp4 = 0;
            }
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, (float)(this.getMeasuredHeight() - 1), (float)(this.getMeasuredWidth() - dp4), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
            else {
                canvas.drawLine((float)dp4, (float)(this.getMeasuredHeight() - 1), (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
            }
        }
        if (this.clipProgress != 0.0f) {
            if (Build$VERSION.SDK_INT != 24) {
                canvas.restore();
            }
            else {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor("windowBackgroundWhite"));
                canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), this.clipProgress * this.topClip, Theme.dialogs_pinnedPaint);
                canvas.drawRect(0.0f, (float)(this.getMeasuredHeight() - (int)(this.bottomClip * this.clipProgress)), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), Theme.dialogs_pinnedPaint);
            }
        }
        Label_4310: {
            int n28 = 0;
            Label_4259: {
                if (!this.drawReorder) {
                    n28 = n11;
                    if (this.reorderIconProgress == 0.0f) {
                        break Label_4259;
                    }
                }
                if (this.drawReorder) {
                    final float reorderIconProgress = this.reorderIconProgress;
                    n28 = n11;
                    if (reorderIconProgress < 1.0f) {
                        this.reorderIconProgress = reorderIconProgress + n / 170.0f;
                        if (this.reorderIconProgress > 1.0f) {
                            this.reorderIconProgress = 1.0f;
                        }
                        n28 = 1;
                    }
                }
                else {
                    final float reorderIconProgress2 = this.reorderIconProgress;
                    if (reorderIconProgress2 > 0.0f) {
                        this.reorderIconProgress = reorderIconProgress2 - n / 170.0f;
                        if (this.reorderIconProgress < 0.0f) {
                            this.reorderIconProgress = 0.0f;
                        }
                        n11 = 1;
                    }
                    break Label_4310;
                }
            }
            n11 = n28;
        }
        Label_4449: {
            if (this.archiveHidden) {
                final float archiveBackgroundProgress = this.archiveBackgroundProgress;
                if (archiveBackgroundProgress <= 0.0f) {
                    break Label_4449;
                }
                this.archiveBackgroundProgress = archiveBackgroundProgress - n / 170.0f;
                if (this.currentRevealBounceProgress < 0.0f) {
                    this.currentRevealBounceProgress = 0.0f;
                }
                if (this.avatarDrawable.getAvatarType() == 3) {
                    this.avatarDrawable.setArchivedAvatarHiddenProgress(this.archiveBackgroundProgress);
                }
            }
            else {
                final float archiveBackgroundProgress2 = this.archiveBackgroundProgress;
                if (archiveBackgroundProgress2 >= 1.0f) {
                    break Label_4449;
                }
                this.archiveBackgroundProgress = archiveBackgroundProgress2 + n / 170.0f;
                if (this.currentRevealBounceProgress > 1.0f) {
                    this.currentRevealBounceProgress = 1.0f;
                }
                if (this.avatarDrawable.getAvatarType() == 3) {
                    this.avatarDrawable.setArchivedAvatarHiddenProgress(this.archiveBackgroundProgress);
                }
            }
            n11 = 1;
        }
        if (this.animatingArchiveAvatar) {
            this.animatingArchiveAvatarProgress += n;
            if (this.animatingArchiveAvatarProgress >= 170.0f) {
                this.animatingArchiveAvatarProgress = 170.0f;
                this.animatingArchiveAvatar = false;
            }
            n11 = 1;
        }
        Label_4656: {
            if (this.drawRevealBackground) {
                final float currentRevealBounceProgress2 = this.currentRevealBounceProgress;
                if (currentRevealBounceProgress2 < 1.0f) {
                    this.currentRevealBounceProgress = currentRevealBounceProgress2 + n / 170.0f;
                    if (this.currentRevealBounceProgress > 1.0f) {
                        this.currentRevealBounceProgress = 1.0f;
                        n11 = 1;
                    }
                }
                final float currentRevealProgress = this.currentRevealProgress;
                if (currentRevealProgress >= 1.0f) {
                    break Label_4656;
                }
                this.currentRevealProgress = currentRevealProgress + n / 300.0f;
                if (this.currentRevealProgress > 1.0f) {
                    this.currentRevealProgress = 1.0f;
                }
            }
            else {
                if (this.currentRevealBounceProgress == 1.0f) {
                    this.currentRevealBounceProgress = 0.0f;
                    n11 = 1;
                }
                final float currentRevealProgress2 = this.currentRevealProgress;
                if (currentRevealProgress2 <= 0.0f) {
                    break Label_4656;
                }
                this.currentRevealProgress = currentRevealProgress2 - n / 300.0f;
                if (this.currentRevealProgress < 0.0f) {
                    this.currentRevealProgress = 0.0f;
                }
            }
            n11 = 1;
        }
        if (n11 != 0) {
            this.invalidate();
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(16);
        accessibilityNodeInfo.addAction(32);
    }
    
    protected void onLayout(final boolean b, int dp, int dp2, final int n, final int n2) {
        if (this.currentDialogId == 0L && this.customDialog == null) {
            return;
        }
        if (this.checkBox != null) {
            if (LocaleController.isRTL) {
                dp = n - dp - AndroidUtilities.dp(45.0f);
            }
            else {
                dp = AndroidUtilities.dp(45.0f);
            }
            dp2 = AndroidUtilities.dp(46.0f);
            final CheckBox2 checkBox = this.checkBox;
            checkBox.layout(dp, dp2, checkBox.getMeasuredWidth() + dp, this.checkBox.getMeasuredHeight() + dp2);
        }
        if (b) {
            try {
                this.buildLayout();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    protected void onMeasure(int size, final int n) {
        final CheckBox2 checkBox = this.checkBox;
        if (checkBox != null) {
            checkBox.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        }
        size = View$MeasureSpec.getSize(size);
        float n2;
        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            n2 = 72.0f;
        }
        else {
            n2 = 78.0f;
        }
        this.setMeasuredDimension(size, AndroidUtilities.dp(n2) + (this.useSeparator ? 1 : 0));
        this.topClip = 0;
        this.bottomClip = this.getMeasuredHeight();
    }
    
    public void onPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        final StringBuilder sb = new StringBuilder();
        if (this.currentDialogFolderId == 1) {
            sb.append(LocaleController.getString("ArchivedChats", 2131558653));
            sb.append(". ");
        }
        else {
            if (this.encryptedChat != null) {
                sb.append(LocaleController.getString("AccDescrSecretChat", 2131558470));
                sb.append(". ");
            }
            final TLRPC.User user = this.user;
            if (user != null) {
                if (user.bot) {
                    sb.append(LocaleController.getString("Bot", 2131558848));
                    sb.append(". ");
                }
                final TLRPC.User user2 = this.user;
                if (user2.self) {
                    sb.append(LocaleController.getString("SavedMessages", 2131560633));
                }
                else {
                    sb.append(ContactsController.formatName(user2.first_name, user2.last_name));
                }
                sb.append(". ");
            }
            else {
                final TLRPC.Chat chat = this.chat;
                if (chat != null) {
                    if (chat.broadcast) {
                        sb.append(LocaleController.getString("AccDescrChannel", 2131558426));
                    }
                    else {
                        sb.append(LocaleController.getString("AccDescrGroup", 2131558437));
                    }
                    sb.append(". ");
                    sb.append(this.chat.title);
                    sb.append(". ");
                }
            }
        }
        final int unreadCount = this.unreadCount;
        if (unreadCount > 0) {
            sb.append(LocaleController.formatPluralString("NewMessages", unreadCount));
            sb.append(". ");
        }
        final MessageObject message = this.message;
        if (message != null && this.currentDialogFolderId == 0) {
            final int lastMessageDate = this.lastMessageDate;
            int date;
            if ((date = lastMessageDate) == 0) {
                date = lastMessageDate;
                if (message != null) {
                    date = message.messageOwner.date;
                }
            }
            final String formatDateAudio = LocaleController.formatDateAudio(date);
            if (this.message.isOut()) {
                sb.append(LocaleController.formatString("AccDescrSentDate", 2131558471, formatDateAudio));
            }
            else {
                sb.append(LocaleController.formatString("AccDescrReceivedDate", 2131558461, formatDateAudio));
            }
            sb.append(". ");
            if (this.chat != null && !this.message.isOut() && this.message.isFromUser() && this.message.messageOwner.action == null) {
                final TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(this.message.messageOwner.from_id);
                if (user3 != null) {
                    sb.append(ContactsController.formatName(user3.first_name, user3.last_name));
                    sb.append(". ");
                }
            }
            if (this.encryptedChat == null) {
                sb.append(this.message.messageText);
                if (!this.message.isMediaEmpty() && !TextUtils.isEmpty(this.message.caption)) {
                    sb.append(". ");
                    sb.append(this.message.caption);
                }
            }
            accessibilityEvent.setContentDescription((CharSequence)sb.toString());
            return;
        }
        accessibilityEvent.setContentDescription((CharSequence)sb.toString());
    }
    
    public void onReorderStateChanged(final boolean drawReorder, final boolean b) {
        if ((!this.drawPin && drawReorder) || this.drawReorder == drawReorder) {
            if (!this.drawPin) {
                this.drawReorder = false;
            }
            return;
        }
        this.drawReorder = drawReorder;
        float n = 1.0f;
        if (b) {
            if (this.drawReorder) {
                n = 0.0f;
            }
            this.reorderIconProgress = n;
        }
        else {
            if (!this.drawReorder) {
                n = 0.0f;
            }
            this.reorderIconProgress = n;
        }
        this.invalidate();
    }
    
    public void setBottomClip(final int bottomClip) {
        this.bottomClip = bottomClip;
    }
    
    public void setChecked(final boolean b, final boolean b2) {
        final CheckBox2 checkBox = this.checkBox;
        if (checkBox == null) {
            return;
        }
        checkBox.setChecked(b, b2);
    }
    
    public void setClipProgress(final float clipProgress) {
        this.clipProgress = clipProgress;
        this.invalidate();
    }
    
    public void setDialog(final long currentDialogId, MessageObject message, int messageId) {
        this.currentDialogId = currentDialogId;
        this.message = message;
        this.isDialogCell = false;
        this.lastMessageDate = messageId;
        if (message != null) {
            messageId = message.messageOwner.edit_date;
        }
        else {
            messageId = 0;
        }
        this.currentEditDate = messageId;
        this.unreadCount = 0;
        this.markUnread = false;
        if (message != null) {
            messageId = message.getId();
        }
        else {
            messageId = 0;
        }
        this.messageId = messageId;
        this.mentionCount = 0;
        this.lastUnreadState = (message != null && message.isUnread());
        message = this.message;
        if (message != null) {
            this.lastSendState = message.messageOwner.send_state;
        }
        this.update(0);
    }
    
    public void setDialog(final TLRPC.Dialog dialog, final int dialogsType, final int folderId) {
        this.currentDialogId = dialog.id;
        this.isDialogCell = true;
        if (dialog instanceof TLRPC.TL_dialogFolder) {
            this.currentDialogFolderId = ((TLRPC.TL_dialogFolder)dialog).folder.id;
        }
        else {
            this.currentDialogFolderId = 0;
        }
        this.dialogsType = dialogsType;
        this.folderId = folderId;
        this.update(this.messageId = 0);
        this.checkOnline();
    }
    
    public void setDialog(final CustomDialog customDialog) {
        this.customDialog = customDialog;
        this.update(this.messageId = 0);
        this.checkOnline();
    }
    
    public void setDialogIndex(final int index) {
        this.index = index;
    }
    
    public void setDialogSelected(final boolean isSelected) {
        if (this.isSelected != isSelected) {
            this.invalidate();
        }
        this.isSelected = isSelected;
    }
    
    public void setSliding(final boolean isSliding) {
        this.isSliding = isSliding;
    }
    
    public void setTopClip(final int topClip) {
        this.topClip = topClip;
    }
    
    public void setTranslationX(final float n) {
        this.translationX = (float)(int)n;
        final Drawable translationDrawable = this.translationDrawable;
        boolean drawRevealBackground = false;
        if (translationDrawable != null && this.translationX == 0.0f) {
            if (translationDrawable instanceof LottieDrawable) {
                ((LottieDrawable)translationDrawable).setProgress(0.0f);
            }
            this.translationAnimationStarted = false;
            this.archiveHidden = SharedConfig.archiveHidden;
            this.currentRevealProgress = 0.0f;
            this.isSliding = false;
        }
        if (this.translationX != 0.0f) {
            this.isSliding = true;
        }
        while (true) {
            if (!this.isSliding) {
                break Label_0144;
            }
            final boolean drawRevealBackground2 = this.drawRevealBackground;
            if (Math.abs(this.translationX) >= this.getMeasuredWidth() * 0.3f) {
                drawRevealBackground = true;
            }
            this.drawRevealBackground = drawRevealBackground;
            if (drawRevealBackground2 == this.drawRevealBackground || this.archiveHidden != SharedConfig.archiveHidden) {
                break Label_0144;
            }
            try {
                this.performHapticFeedback(3, 2);
                this.invalidate();
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public void update(int lastSendState) {
        final CustomDialog customDialog = this.customDialog;
        boolean lastUnreadState = true;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            if (customDialog.unread_count == 0) {
                lastUnreadState = false;
            }
            this.lastUnreadState = lastUnreadState;
            final CustomDialog customDialog2 = this.customDialog;
            this.unreadCount = customDialog2.unread_count;
            this.drawPin = customDialog2.pinned;
            this.dialogMuted = customDialog2.muted;
            this.avatarDrawable.setInfo(customDialog2.id, customDialog2.name, null, false);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0);
        }
        else {
            if (this.isDialogCell) {
                final TLRPC.Dialog dialog = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (dialog != null) {
                    if (lastSendState == 0) {
                        this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(dialog.id);
                        this.message = (MessageObject)MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
                        final MessageObject message = this.message;
                        this.lastUnreadState = (message != null && message.isUnread());
                        this.unreadCount = dialog.unread_count;
                        this.markUnread = dialog.unread_mark;
                        this.mentionCount = dialog.unread_mentions_count;
                        final MessageObject message2 = this.message;
                        int edit_date;
                        if (message2 != null) {
                            edit_date = message2.messageOwner.edit_date;
                        }
                        else {
                            edit_date = 0;
                        }
                        this.currentEditDate = edit_date;
                        this.lastMessageDate = dialog.last_message_date;
                        this.drawPin = (this.currentDialogFolderId == 0 && dialog.pinned);
                        final MessageObject message3 = this.message;
                        if (message3 != null) {
                            this.lastSendState = message3.messageOwner.send_state;
                        }
                    }
                }
                else {
                    this.unreadCount = 0;
                    this.mentionCount = 0;
                    this.currentEditDate = 0;
                    this.lastMessageDate = 0;
                    this.clearingDialog = false;
                }
            }
            else {
                this.drawPin = false;
            }
            if (lastSendState != 0) {
                if (this.user != null && (lastSendState & 0x4) != 0x0) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(this.user.id);
                    this.invalidate();
                }
                int n = 0;
                Label_0495: {
                    Label_0492: {
                        if (this.isDialogCell && (lastSendState & 0x40) != 0x0) {
                            final CharSequence obj = (CharSequence)MessagesController.getInstance(this.currentAccount).printingStrings.get(this.currentDialogId);
                            if ((this.lastPrintString == null || obj != null) && (this.lastPrintString != null || obj == null)) {
                                final CharSequence lastPrintString = this.lastPrintString;
                                if (lastPrintString == null || obj == null || lastPrintString.equals(obj)) {
                                    break Label_0492;
                                }
                            }
                            n = 1;
                            break Label_0495;
                        }
                    }
                    n = 0;
                }
                int n2 = n;
                if (n == 0) {
                    n2 = n;
                    if ((0x8000 & lastSendState) != 0x0) {
                        final MessageObject message4 = this.message;
                        n2 = n;
                        if (message4 != null) {
                            n2 = n;
                            if (message4.messageText != this.lastMessageString) {
                                n2 = 1;
                            }
                        }
                    }
                }
                int n3;
                if ((n3 = n2) == 0) {
                    n3 = n2;
                    if ((lastSendState & 0x2) != 0x0) {
                        n3 = n2;
                        if (this.chat == null) {
                            n3 = 1;
                        }
                    }
                }
                int n4;
                if ((n4 = n3) == 0) {
                    n4 = n3;
                    if ((lastSendState & 0x1) != 0x0) {
                        n4 = n3;
                        if (this.chat == null) {
                            n4 = 1;
                        }
                    }
                }
                int n5;
                if ((n5 = n4) == 0) {
                    n5 = n4;
                    if ((lastSendState & 0x8) != 0x0) {
                        n5 = n4;
                        if (this.user == null) {
                            n5 = 1;
                        }
                    }
                }
                int n6;
                if ((n6 = n5) == 0) {
                    n6 = n5;
                    if ((lastSendState & 0x10) != 0x0) {
                        n6 = n5;
                        if (this.user == null) {
                            n6 = 1;
                        }
                    }
                }
                int n7 = 0;
                Label_0843: {
                    if ((n7 = n6) == 0) {
                        n7 = n6;
                        if ((lastSendState & 0x100) != 0x0) {
                            final MessageObject message5 = this.message;
                            if (message5 != null && this.lastUnreadState != message5.isUnread()) {
                                this.lastUnreadState = this.message.isUnread();
                            }
                            else {
                                n7 = n6;
                                if (!this.isDialogCell) {
                                    break Label_0843;
                                }
                                final TLRPC.Dialog dialog2 = (TLRPC.Dialog)MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                n7 = n6;
                                if (dialog2 == null) {
                                    break Label_0843;
                                }
                                if (this.unreadCount == dialog2.unread_count && this.markUnread == dialog2.unread_mark) {
                                    n7 = n6;
                                    if (this.mentionCount == dialog2.unread_mentions_count) {
                                        break Label_0843;
                                    }
                                }
                                this.unreadCount = dialog2.unread_count;
                                this.mentionCount = dialog2.unread_mentions_count;
                                this.markUnread = dialog2.unread_mark;
                            }
                            n7 = 1;
                        }
                    }
                }
                int n8;
                if ((n8 = n7) == 0) {
                    n8 = n7;
                    if ((lastSendState & 0x1000) != 0x0) {
                        final MessageObject message6 = this.message;
                        n8 = n7;
                        if (message6 != null) {
                            lastSendState = this.lastSendState;
                            final int send_state = message6.messageOwner.send_state;
                            n8 = n7;
                            if (lastSendState != send_state) {
                                this.lastSendState = send_state;
                                n8 = 1;
                            }
                        }
                    }
                }
                if (n8 == 0) {
                    this.invalidate();
                    return;
                }
            }
            this.user = null;
            this.chat = null;
            this.encryptedChat = null;
            long n9;
            if (this.currentDialogFolderId != 0) {
                this.dialogMuted = false;
                this.message = this.findFolderTopMessage();
                final MessageObject message7 = this.message;
                if (message7 != null) {
                    n9 = message7.getDialogId();
                }
                else {
                    n9 = 0L;
                }
            }
            else {
                this.dialogMuted = (this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId));
                n9 = this.currentDialogId;
            }
            if (n9 != 0L) {
                lastSendState = (int)n9;
                final int i = (int)(n9 >> 32);
                if (lastSendState != 0) {
                    if (i == 1) {
                        this.chat = MessagesController.getInstance(this.currentAccount).getChat(lastSendState);
                    }
                    else if (lastSendState < 0) {
                        this.chat = MessagesController.getInstance(this.currentAccount).getChat(-lastSendState);
                        if (!this.isDialogCell) {
                            final TLRPC.Chat chat = this.chat;
                            if (chat != null && chat.migrated_to != null) {
                                final TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(this.chat.migrated_to.channel_id);
                                if (chat2 != null) {
                                    this.chat = chat2;
                                }
                            }
                        }
                    }
                    else {
                        this.user = MessagesController.getInstance(this.currentAccount).getUser(lastSendState);
                    }
                }
                else {
                    this.encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(i);
                    if (this.encryptedChat != null) {
                        this.user = MessagesController.getInstance(this.currentAccount).getUser(this.encryptedChat.user_id);
                    }
                }
            }
            if (this.currentDialogFolderId != 0) {
                this.avatarDrawable.setAvatarType(3);
                this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
            }
            else {
                final TLRPC.User user = this.user;
                if (user != null) {
                    this.avatarDrawable.setInfo(user);
                    if (UserObject.isUserSelf(this.user)) {
                        this.avatarDrawable.setAvatarType(1);
                        this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                    }
                    else {
                        this.avatarImage.setImage(ImageLocation.getForUser(this.user, false), "50_50", this.avatarDrawable, null, this.user, 0);
                    }
                }
                else {
                    final TLRPC.Chat chat3 = this.chat;
                    if (chat3 != null) {
                        this.avatarDrawable.setInfo(chat3);
                        this.avatarImage.setImage(ImageLocation.getForChat(this.chat, false), "50_50", this.avatarDrawable, null, this.chat, 0);
                    }
                }
            }
        }
        if (this.getMeasuredWidth() == 0 && this.getMeasuredHeight() == 0) {
            this.requestLayout();
        }
        else {
            this.buildLayout();
        }
        this.invalidate();
    }
    
    public class BounceInterpolator implements Interpolator
    {
        public float getInterpolation(float n) {
            if (n < 0.33f) {
                return n / 0.33f * 0.1f;
            }
            n -= 0.33f;
            if (n < 0.33f) {
                return 0.1f - n / 0.34f * 0.15f;
            }
            return (n - 0.34f) / 0.33f * 0.05f - 0.05f;
        }
    }
    
    public static class CustomDialog
    {
        public int date;
        public int id;
        public boolean isMedia;
        public String message;
        public boolean muted;
        public String name;
        public boolean pinned;
        public boolean sent;
        public int type;
        public int unread_count;
        public boolean verified;
    }
}
