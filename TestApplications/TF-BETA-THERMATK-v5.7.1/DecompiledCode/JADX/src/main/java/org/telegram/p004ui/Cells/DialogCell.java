package org.telegram.p004ui.Cells;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieDrawable;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.CheckBox2;
import org.telegram.p004ui.Components.TypefaceSpan;
import org.telegram.p004ui.DialogsActivity;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Dialog;
import org.telegram.tgnet.TLRPC.DraftMessage;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_dialogFolder;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.Cells.DialogCell */
public class DialogCell extends BaseCell {
    private boolean animatingArchiveAvatar;
    private float animatingArchiveAvatarProgress;
    private float archiveBackgroundProgress;
    private boolean archiveHidden;
    private boolean attachedToWindow;
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private ImageReceiver avatarImage = new ImageReceiver(this);
    private int bottomClip;
    private Chat chat;
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
    private int currentAccount = UserConfig.selectedAccount;
    private int currentDialogFolderDialogsCount;
    private int currentDialogFolderId;
    private long currentDialogId;
    private int currentEditDate;
    private float currentRevealBounceProgress;
    private float currentRevealProgress;
    private CustomDialog customDialog;
    private boolean dialogMuted;
    private int dialogsType;
    private DraftMessage draftMessage;
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
    private EncryptedChat encryptedChat;
    private int errorLeft;
    private int errorTop;
    private int folderId;
    public boolean fullSeparator;
    public boolean fullSeparator2;
    private int halfCheckDrawLeft;
    private int index;
    private BounceInterpolator interpolator = new BounceInterpolator();
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
    private RectF rect = new RectF();
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
    private User user;

    /* renamed from: org.telegram.ui.Cells.DialogCell$BounceInterpolator */
    public class BounceInterpolator implements Interpolator {
        public float getInterpolation(float f) {
            if (f < 0.33f) {
                return (f / 0.33f) * 0.1f;
            }
            f -= 0.33f;
            return f < 0.33f ? 0.1f - ((f / 0.34f) * 0.15f) : (((f - 0.34f) / 0.33f) * 0.05f) - 89.6f;
        }
    }

    /* renamed from: org.telegram.ui.Cells.DialogCell$CustomDialog */
    public static class CustomDialog {
        public int date;
        /* renamed from: id */
        public int f579id;
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

    public boolean hasOverlappingRendering() {
        return false;
    }

    public DialogCell(Context context, boolean z, boolean z2) {
        super(context);
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.m26dp(28.0f));
        this.useForceThreeLines = z2;
        if (z) {
            this.checkBox = new CheckBox2(context);
            this.checkBox.setColor(null, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
            this.checkBox.setSize(21);
            this.checkBox.setDrawUnchecked(false);
            this.checkBox.setDrawBackgroundAsArc(3);
            addView(this.checkBox);
        }
    }

    public void setDialog(Dialog dialog, int i, int i2) {
        this.currentDialogId = dialog.f440id;
        this.isDialogCell = true;
        if (dialog instanceof TL_dialogFolder) {
            this.currentDialogFolderId = ((TL_dialogFolder) dialog).folder.f485id;
        } else {
            this.currentDialogFolderId = 0;
        }
        this.dialogsType = i;
        this.folderId = i2;
        this.messageId = 0;
        update(0);
        checkOnline();
    }

    public void setDialogIndex(int i) {
        this.index = i;
    }

    public void setDialog(CustomDialog customDialog) {
        this.customDialog = customDialog;
        this.messageId = 0;
        update(0);
        checkOnline();
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0035  */
    private void checkOnline() {
        /*
        r2 = this;
        r0 = r2.user;
        if (r0 == 0) goto L_0x0032;
    L_0x0004:
        r1 = r0.self;
        if (r1 != 0) goto L_0x0032;
    L_0x0008:
        r0 = r0.status;
        if (r0 == 0) goto L_0x001a;
    L_0x000c:
        r0 = r0.expires;
        r1 = r2.currentAccount;
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);
        r1 = r1.getCurrentTime();
        if (r0 > r1) goto L_0x0030;
    L_0x001a:
        r0 = r2.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r0 = r0.onlinePrivacy;
        r1 = r2.user;
        r1 = r1.f534id;
        r1 = java.lang.Integer.valueOf(r1);
        r0 = r0.containsKey(r1);
        if (r0 == 0) goto L_0x0032;
    L_0x0030:
        r0 = 1;
        goto L_0x0033;
    L_0x0032:
        r0 = 0;
    L_0x0033:
        if (r0 == 0) goto L_0x0038;
    L_0x0035:
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x0039;
    L_0x0038:
        r0 = 0;
    L_0x0039:
        r2.onlineProgress = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.DialogCell.checkOnline():void");
    }

    public void setDialog(long j, MessageObject messageObject, int i) {
        this.currentDialogId = j;
        this.message = messageObject;
        this.isDialogCell = false;
        this.lastMessageDate = i;
        this.currentEditDate = messageObject != null ? messageObject.messageOwner.edit_date : 0;
        this.unreadCount = 0;
        this.markUnread = false;
        this.messageId = messageObject != null ? messageObject.getId() : 0;
        this.mentionCount = 0;
        boolean z = messageObject != null && messageObject.isUnread();
        this.lastUnreadState = z;
        MessageObject messageObject2 = this.message;
        if (messageObject2 != null) {
            this.lastSendState = messageObject2.messageOwner.send_state;
        }
        update(0);
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

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isSliding = false;
        this.drawRevealBackground = false;
        this.currentRevealProgress = 0.0f;
        this.attachedToWindow = false;
        float f = (this.drawPin && this.drawReorder) ? 1.0f : 0.0f;
        this.reorderIconProgress = f;
        this.avatarImage.onDetachedFromWindow();
        Drawable drawable = this.translationDrawable;
        if (drawable != null) {
            if (drawable instanceof LottieDrawable) {
                LottieDrawable lottieDrawable = (LottieDrawable) drawable;
                lottieDrawable.stop();
                lottieDrawable.setProgress(0.0f);
                lottieDrawable.setCallback(null);
            }
            this.translationDrawable = null;
            this.translationAnimationStarted = false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        this.archiveHidden = SharedConfig.archiveHidden;
        float f = 1.0f;
        this.archiveBackgroundProgress = this.archiveHidden ? 0.0f : 1.0f;
        this.avatarDrawable.setArchivedAvatarHiddenProgress(this.archiveBackgroundProgress);
        this.clipProgress = 0.0f;
        this.isSliding = false;
        if (!(this.drawPin && this.drawReorder)) {
            f = 0.0f;
        }
        this.reorderIconProgress = f;
        this.attachedToWindow = true;
        this.cornerProgress = 0.0f;
        setTranslationX(0.0f);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            checkBox2.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(24.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(24.0f), 1073741824));
        }
        i = MeasureSpec.getSize(i);
        float f = (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 78.0f : 72.0f;
        setMeasuredDimension(i, AndroidUtilities.m26dp(f) + this.useSeparator);
        this.topClip = 0;
        this.bottomClip = getMeasuredHeight();
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.currentDialogId != 0 || this.customDialog != null) {
            if (this.checkBox != null) {
                i3 = LocaleController.isRTL ? (i3 - i) - AndroidUtilities.m26dp(45.0f) : AndroidUtilities.m26dp(45.0f);
                i = AndroidUtilities.m26dp(46.0f);
                CheckBox2 checkBox2 = this.checkBox;
                checkBox2.layout(i3, i, checkBox2.getMeasuredWidth() + i3, this.checkBox.getMeasuredHeight() + i);
            }
            if (z) {
                try {
                    buildLayout();
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
            }
        }
    }

    public boolean isUnread() {
        return (this.unreadCount != 0 || this.markUnread) && !this.dialogMuted;
    }

    private CharSequence formatArchivedDialogNames() {
        ArrayList dialogs = MessagesController.getInstance(this.currentAccount).getDialogs(this.currentDialogFolderId);
        this.currentDialogFolderDialogsCount = dialogs.size();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int size = dialogs.size();
        for (int i = 0; i < size; i++) {
            User user;
            String replace;
            Dialog dialog = (Dialog) dialogs.get(i);
            Chat chat = null;
            if (DialogObject.isSecretDialogId(dialog.f440id)) {
                EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf((int) (dialog.f440id >> 32)));
                user = encryptedChat != null ? MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(encryptedChat.user_id)) : null;
            } else {
                int i2 = (int) dialog.f440id;
                if (i2 > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(i2));
                } else {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-i2));
                    user = null;
                }
            }
            if (chat != null) {
                replace = chat.title.replace(10, ' ');
            } else if (user == null) {
                continue;
            } else if (UserObject.isDeleted(user)) {
                replace = LocaleController.getString("HiddenName", C1067R.string.HiddenName);
            } else {
                replace = ContactsController.formatName(user.first_name, user.last_name).replace(10, ' ');
            }
            if (spannableStringBuilder.length() > 0) {
                spannableStringBuilder.append(", ");
            }
            int length = spannableStringBuilder.length();
            int length2 = replace.length() + length;
            spannableStringBuilder.append(replace);
            if (dialog.unread_count > 0) {
                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, Theme.getColor(Theme.key_chats_nameArchived)), length, length2, 33);
            }
            if (spannableStringBuilder.length() > 150) {
                break;
            }
        }
        return Emoji.replaceEmoji(spannableStringBuilder, Theme.dialogs_messagePaint.getFontMetricsInt(), AndroidUtilities.m26dp(17.0f), false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:483:0x0ac1  */
    /* JADX WARNING: Removed duplicated region for block: B:483:0x0ac1  */
    /* JADX WARNING: Removed duplicated region for block: B:536:0x0bac  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x0b90  */
    /* JADX WARNING: Removed duplicated region for block: B:527:0x0b86  */
    /* JADX WARNING: Removed duplicated region for block: B:527:0x0b86  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x0b90  */
    /* JADX WARNING: Removed duplicated region for block: B:536:0x0bac  */
    /* JADX WARNING: Removed duplicated region for block: B:488:0x0ad7  */
    /* JADX WARNING: Removed duplicated region for block: B:487:0x0acf  */
    /* JADX WARNING: Removed duplicated region for block: B:498:0x0b04  */
    /* JADX WARNING: Removed duplicated region for block: B:497:0x0af4  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x0c38  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0c2a  */
    /* JADX WARNING: Removed duplicated region for block: B:833:0x13b0  */
    /* JADX WARNING: Removed duplicated region for block: B:789:0x1282  */
    /* JADX WARNING: Removed duplicated region for block: B:776:0x1236 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:783:0x1267 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:782:0x1264 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:789:0x1282  */
    /* JADX WARNING: Removed duplicated region for block: B:833:0x13b0  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0ddd  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0db1  */
    /* JADX WARNING: Removed duplicated region for block: B:654:0x0e9a  */
    /* JADX WARNING: Removed duplicated region for block: B:651:0x0e84  */
    /* JADX WARNING: Removed duplicated region for block: B:676:0x0fc4  */
    /* JADX WARNING: Removed duplicated region for block: B:675:0x0fb5  */
    /* JADX WARNING: Removed duplicated region for block: B:680:0x0fee  */
    /* JADX WARNING: Removed duplicated region for block: B:690:0x1041  */
    /* JADX WARNING: Removed duplicated region for block: B:686:0x1013  */
    /* JADX WARNING: Removed duplicated region for block: B:724:0x1168  */
    /* JADX WARNING: Removed duplicated region for block: B:765:0x1211 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:776:0x1236 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:782:0x1264 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:783:0x1267 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:833:0x13b0  */
    /* JADX WARNING: Removed duplicated region for block: B:789:0x1282  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0338  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0101  */
    /* JADX WARNING: Removed duplicated region for block: B:604:0x0d39  */
    /* JADX WARNING: Removed duplicated region for block: B:600:0x0cfa  */
    /* JADX WARNING: Removed duplicated region for block: B:608:0x0d53  */
    /* JADX WARNING: Removed duplicated region for block: B:607:0x0d43  */
    /* JADX WARNING: Removed duplicated region for block: B:613:0x0d7a  */
    /* JADX WARNING: Removed duplicated region for block: B:611:0x0d6b  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0db1  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0ddd  */
    /* JADX WARNING: Removed duplicated region for block: B:642:0x0e62  */
    /* JADX WARNING: Removed duplicated region for block: B:651:0x0e84  */
    /* JADX WARNING: Removed duplicated region for block: B:654:0x0e9a  */
    /* JADX WARNING: Removed duplicated region for block: B:666:0x0ef0  */
    /* JADX WARNING: Removed duplicated region for block: B:675:0x0fb5  */
    /* JADX WARNING: Removed duplicated region for block: B:676:0x0fc4  */
    /* JADX WARNING: Removed duplicated region for block: B:680:0x0fee  */
    /* JADX WARNING: Removed duplicated region for block: B:686:0x1013  */
    /* JADX WARNING: Removed duplicated region for block: B:690:0x1041  */
    /* JADX WARNING: Removed duplicated region for block: B:724:0x1168  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x11ab  */
    /* JADX WARNING: Removed duplicated region for block: B:759:0x1204 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:765:0x1211 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:769:0x121b A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:776:0x1236 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:783:0x1267 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:782:0x1264 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:789:0x1282  */
    /* JADX WARNING: Removed duplicated region for block: B:833:0x13b0  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0101  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0338  */
    /* JADX WARNING: Removed duplicated region for block: B:600:0x0cfa  */
    /* JADX WARNING: Removed duplicated region for block: B:604:0x0d39  */
    /* JADX WARNING: Removed duplicated region for block: B:607:0x0d43  */
    /* JADX WARNING: Removed duplicated region for block: B:608:0x0d53  */
    /* JADX WARNING: Removed duplicated region for block: B:611:0x0d6b  */
    /* JADX WARNING: Removed duplicated region for block: B:613:0x0d7a  */
    /* JADX WARNING: Removed duplicated region for block: B:628:0x0ddd  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0db1  */
    /* JADX WARNING: Removed duplicated region for block: B:642:0x0e62  */
    /* JADX WARNING: Removed duplicated region for block: B:654:0x0e9a  */
    /* JADX WARNING: Removed duplicated region for block: B:651:0x0e84  */
    /* JADX WARNING: Removed duplicated region for block: B:666:0x0ef0  */
    /* JADX WARNING: Removed duplicated region for block: B:676:0x0fc4  */
    /* JADX WARNING: Removed duplicated region for block: B:675:0x0fb5  */
    /* JADX WARNING: Removed duplicated region for block: B:680:0x0fee  */
    /* JADX WARNING: Removed duplicated region for block: B:690:0x1041  */
    /* JADX WARNING: Removed duplicated region for block: B:686:0x1013  */
    /* JADX WARNING: Removed duplicated region for block: B:724:0x1168  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x11ab  */
    /* JADX WARNING: Removed duplicated region for block: B:759:0x1204 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:765:0x1211 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:769:0x121b A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:776:0x1236 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:782:0x1264 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:783:0x1267 A:{Catch:{ Exception -> 0x127a }} */
    /* JADX WARNING: Removed duplicated region for block: B:833:0x13b0  */
    /* JADX WARNING: Removed duplicated region for block: B:789:0x1282  */
    /* JADX WARNING: Missing block: B:253:0x05e1, code skipped:
            if (r6.post_messages != false) goto L_0x05e3;
     */
    public void buildLayout() {
        /*
        r33 = this;
        r1 = r33;
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x0049;
    L_0x0006:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x000b;
    L_0x000a:
        goto L_0x0049;
    L_0x000b:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_namePaint;
        r2 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_nameEncryptedPaint;
        r2 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r2 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r2 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r2 = "chats_message";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.linkColor = r2;
        r0.setColor(r2);
        goto L_0x0086;
    L_0x0049:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_namePaint;
        r2 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_nameEncryptedPaint;
        r2 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r2 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r2 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r2;
        r0.setTextSize(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r2 = "chats_message_threeLines";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.linkColor = r2;
        r0.setColor(r2);
    L_0x0086:
        r2 = 0;
        r1.currentDialogFolderDialogsCount = r2;
        r0 = r1.isDialogCell;
        if (r0 == 0) goto L_0x009e;
    L_0x008d:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r0 = r0.printingStrings;
        r4 = r1.currentDialogId;
        r0 = r0.get(r4);
        r0 = (java.lang.CharSequence) r0;
        goto L_0x009f;
    L_0x009e:
        r0 = 0;
    L_0x009f:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r1.drawNameGroup = r2;
        r1.drawNameBroadcast = r2;
        r1.drawNameLock = r2;
        r1.drawNameBot = r2;
        r1.drawVerified = r2;
        r1.drawScam = r2;
        r1.drawPinBackground = r2;
        r5 = r1.user;
        r5 = org.telegram.messenger.UserObject.isUserSelf(r5);
        r6 = 1;
        r5 = r5 ^ r6;
        r7 = android.os.Build.VERSION.SDK_INT;
        r8 = 18;
        if (r7 < r8) goto L_0x00cf;
    L_0x00bd:
        r7 = r1.useForceThreeLines;
        if (r7 != 0) goto L_0x00c5;
    L_0x00c1:
        r7 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r7 == 0) goto L_0x00c9;
    L_0x00c5:
        r7 = r1.currentDialogFolderId;
        if (r7 == 0) goto L_0x00cc;
    L_0x00c9:
        r7 = "%2$s: ⁨%1$s⁩";
        goto L_0x00dd;
    L_0x00cc:
        r7 = "⁨%s⁩";
        goto L_0x00e1;
    L_0x00cf:
        r7 = r1.useForceThreeLines;
        if (r7 != 0) goto L_0x00d7;
    L_0x00d3:
        r7 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r7 == 0) goto L_0x00db;
    L_0x00d7:
        r7 = r1.currentDialogFolderId;
        if (r7 == 0) goto L_0x00df;
    L_0x00db:
        r7 = "%2$s: %1$s";
    L_0x00dd:
        r8 = 1;
        goto L_0x00e2;
    L_0x00df:
        r7 = "%1$s";
    L_0x00e1:
        r8 = 0;
    L_0x00e2:
        r9 = r1.message;
        if (r9 == 0) goto L_0x00e9;
    L_0x00e6:
        r9 = r9.messageText;
        goto L_0x00ea;
    L_0x00e9:
        r9 = 0;
    L_0x00ea:
        r1.lastMessageString = r9;
        r9 = r1.customDialog;
        r10 = 32;
        r11 = 10;
        r13 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r14 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        r15 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r16 = 1117257728; // 0x42980000 float:76.0 double:5.51998661E-315;
        r17 = 1117519872; // 0x429c0000 float:78.0 double:5.521281773E-315;
        r3 = 2;
        r12 = "";
        if (r9 == 0) goto L_0x0338;
    L_0x0101:
        r0 = r9.type;
        if (r0 != r3) goto L_0x018a;
    L_0x0105:
        r1.drawNameLock = r6;
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x014d;
    L_0x010b:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x0110;
    L_0x010f:
        goto L_0x014d;
    L_0x0110:
        r0 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.nameLockTop = r0;
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 != 0) goto L_0x0133;
    L_0x011c:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.nameLockLeft = r0;
        r0 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r5 = r5.getIntrinsicWidth();
        r0 = r0 + r5;
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x0133:
        r0 = r33.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r0 = r0 - r5;
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r5 = r5.getIntrinsicWidth();
        r0 = r0 - r5;
        r1.nameLockLeft = r0;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x014d:
        r0 = 1095237632; // 0x41480000 float:12.5 double:5.41119288E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.nameLockTop = r0;
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 != 0) goto L_0x0170;
    L_0x0159:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.nameLockLeft = r0;
        r0 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r5 = r5.getIntrinsicWidth();
        r0 = r0 + r5;
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x0170:
        r0 = r33.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r0 = r0 - r5;
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r5 = r5.getIntrinsicWidth();
        r0 = r0 - r5;
        r1.nameLockLeft = r0;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x018a:
        r5 = r9.verified;
        r1.drawVerified = r5;
        r5 = org.telegram.messenger.SharedConfig.drawDialogIcons;
        if (r5 == 0) goto L_0x0233;
    L_0x0192:
        if (r0 != r6) goto L_0x0233;
    L_0x0194:
        r1.drawNameGroup = r6;
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x01ea;
    L_0x019a:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x019f;
    L_0x019e:
        goto L_0x01ea;
    L_0x019f:
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 != 0) goto L_0x01c9;
    L_0x01a3:
        r0 = 1099694080; // 0x418c0000 float:17.5 double:5.43321066E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.nameLockTop = r0;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.nameLockLeft = r0;
        r0 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r5 = r1.drawNameGroup;
        if (r5 == 0) goto L_0x01be;
    L_0x01bb:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x01c0;
    L_0x01be:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x01c0:
        r5 = r5.getIntrinsicWidth();
        r0 = r0 + r5;
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x01c9:
        r0 = r33.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r0 = r0 - r5;
        r5 = r1.drawNameGroup;
        if (r5 == 0) goto L_0x01d9;
    L_0x01d6:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x01db;
    L_0x01d9:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x01db:
        r5 = r5.getIntrinsicWidth();
        r0 = r0 - r5;
        r1.nameLockLeft = r0;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x01ea:
        r0 = 1096286208; // 0x41580000 float:13.5 double:5.416373534E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.nameLockTop = r0;
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 != 0) goto L_0x0213;
    L_0x01f6:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.nameLockLeft = r0;
        r0 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r5 = r1.drawNameGroup;
        if (r5 == 0) goto L_0x0209;
    L_0x0206:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x020b;
    L_0x0209:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x020b:
        r5 = r5.getIntrinsicWidth();
        r0 = r0 + r5;
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x0213:
        r0 = r33.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r0 = r0 - r5;
        r5 = r1.drawNameGroup;
        if (r5 == 0) goto L_0x0223;
    L_0x0220:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x0225;
    L_0x0223:
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x0225:
        r5 = r5.getIntrinsicWidth();
        r0 = r0 - r5;
        r1.nameLockLeft = r0;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x0233:
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x024e;
    L_0x0237:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x023c;
    L_0x023b:
        goto L_0x024e;
    L_0x023c:
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 != 0) goto L_0x0247;
    L_0x0240:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x0247:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x024e:
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 != 0) goto L_0x0259;
    L_0x0252:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.nameLeft = r0;
        goto L_0x025f;
    L_0x0259:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r1.nameLeft = r0;
    L_0x025f:
        r0 = r1.customDialog;
        r5 = r0.type;
        if (r5 != r6) goto L_0x02e5;
    L_0x0265:
        r0 = 2131559584; // 0x7f0d04a0 float:1.8744516E38 double:1.0531303625E-314;
        r5 = "FromYou";
        r0 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r5 = r1.customDialog;
        r8 = r5.isMedia;
        if (r8 == 0) goto L_0x029b;
    L_0x0274:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r5 = new java.lang.Object[r6];
        r8 = r1.message;
        r8 = r8.messageText;
        r5[r2] = r8;
        r5 = java.lang.String.format(r7, r5);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
        r7 = new android.text.style.ForegroundColorSpan;
        r8 = "chats_attachMessage";
        r8 = org.telegram.p004ui.ActionBar.Theme.getColor(r8);
        r7.<init>(r8);
        r8 = r5.length();
        r9 = 33;
        r5.setSpan(r7, r2, r8, r9);
        goto L_0x02d1;
    L_0x029b:
        r5 = r5.message;
        r8 = r5.length();
        if (r8 <= r14) goto L_0x02a7;
    L_0x02a3:
        r5 = r5.substring(r2, r14);
    L_0x02a7:
        r8 = r1.useForceThreeLines;
        if (r8 != 0) goto L_0x02c3;
    L_0x02ab:
        r8 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r8 == 0) goto L_0x02b0;
    L_0x02af:
        goto L_0x02c3;
    L_0x02b0:
        r8 = new java.lang.Object[r3];
        r5 = r5.replace(r11, r10);
        r8[r2] = r5;
        r8[r6] = r0;
        r5 = java.lang.String.format(r7, r8);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
        goto L_0x02d1;
    L_0x02c3:
        r8 = new java.lang.Object[r3];
        r8[r2] = r5;
        r8[r6] = r0;
        r5 = java.lang.String.format(r7, r8);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
    L_0x02d1:
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r7 = r7.getFontMetricsInt();
        r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r5 = org.telegram.messenger.Emoji.replaceEmoji(r5, r7, r9, r2);
        r7 = r4;
        r4 = r0;
        r0 = 0;
        goto L_0x02f0;
    L_0x02e5:
        r5 = r0.message;
        r0 = r0.isMedia;
        if (r0 == 0) goto L_0x02ed;
    L_0x02eb:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
    L_0x02ed:
        r7 = r4;
        r0 = 1;
        r4 = 0;
    L_0x02f0:
        r8 = r1.customDialog;
        r8 = r8.date;
        r8 = (long) r8;
        r8 = org.telegram.messenger.LocaleController.stringForMessageListDate(r8);
        r9 = r1.customDialog;
        r9 = r9.unread_count;
        if (r9 == 0) goto L_0x0310;
    L_0x02ff:
        r1.drawCount = r6;
        r10 = new java.lang.Object[r6];
        r9 = java.lang.Integer.valueOf(r9);
        r10[r2] = r9;
        r9 = "%d";
        r9 = java.lang.String.format(r9, r10);
        goto L_0x0313;
    L_0x0310:
        r1.drawCount = r2;
        r9 = 0;
    L_0x0313:
        r10 = r1.customDialog;
        r10 = r10.sent;
        if (r10 == 0) goto L_0x0322;
    L_0x0319:
        r1.drawCheck1 = r6;
        r1.drawCheck2 = r6;
        r1.drawClock = r2;
        r1.drawError = r2;
        goto L_0x032a;
    L_0x0322:
        r1.drawCheck1 = r2;
        r1.drawCheck2 = r2;
        r1.drawClock = r2;
        r1.drawError = r2;
    L_0x032a:
        r10 = r1.customDialog;
        r10 = r10.name;
        r13 = r7;
        r3 = r9;
        r9 = r10;
        r10 = 0;
        r7 = r5;
        r5 = r8;
        r8 = r4;
    L_0x0335:
        r4 = r0;
        goto L_0x0cf8;
    L_0x0338:
        r9 = r1.useForceThreeLines;
        if (r9 != 0) goto L_0x0353;
    L_0x033c:
        r9 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r9 == 0) goto L_0x0341;
    L_0x0340:
        goto L_0x0353;
    L_0x0341:
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x034c;
    L_0x0345:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.nameLeft = r9;
        goto L_0x0364;
    L_0x034c:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r1.nameLeft = r9;
        goto L_0x0364;
    L_0x0353:
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x035e;
    L_0x0357:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.nameLeft = r9;
        goto L_0x0364;
    L_0x035e:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r1.nameLeft = r9;
    L_0x0364:
        r9 = r1.encryptedChat;
        if (r9 == 0) goto L_0x03f1;
    L_0x0368:
        r9 = r1.currentDialogFolderId;
        if (r9 != 0) goto L_0x058a;
    L_0x036c:
        r1.drawNameLock = r6;
        r9 = r1.useForceThreeLines;
        if (r9 != 0) goto L_0x03b4;
    L_0x0372:
        r9 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r9 == 0) goto L_0x0377;
    L_0x0376:
        goto L_0x03b4;
    L_0x0377:
        r9 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x039a;
    L_0x0383:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.nameLockLeft = r9;
        r9 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 + r10;
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x039a:
        r9 = r33.getMeasuredWidth();
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r9 = r9 - r10;
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 - r10;
        r1.nameLockLeft = r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x03b4:
        r9 = 1095237632; // 0x41480000 float:12.5 double:5.41119288E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x03d7;
    L_0x03c0:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.nameLockLeft = r9;
        r9 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 + r10;
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x03d7:
        r9 = r33.getMeasuredWidth();
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r9 = r9 - r10;
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 - r10;
        r1.nameLockLeft = r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x03f1:
        r9 = r1.currentDialogFolderId;
        if (r9 != 0) goto L_0x058a;
    L_0x03f5:
        r9 = r1.chat;
        if (r9 == 0) goto L_0x04ec;
    L_0x03f9:
        r10 = r9.scam;
        if (r10 == 0) goto L_0x0405;
    L_0x03fd:
        r1.drawScam = r6;
        r9 = org.telegram.p004ui.ActionBar.Theme.dialogs_scamDrawable;
        r9.checkText();
        goto L_0x0409;
    L_0x0405:
        r9 = r9.verified;
        r1.drawVerified = r9;
    L_0x0409:
        r9 = org.telegram.messenger.SharedConfig.drawDialogIcons;
        if (r9 == 0) goto L_0x058a;
    L_0x040d:
        r9 = r1.useForceThreeLines;
        if (r9 != 0) goto L_0x0481;
    L_0x0411:
        r9 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r9 == 0) goto L_0x0416;
    L_0x0415:
        goto L_0x0481;
    L_0x0416:
        r9 = r1.chat;
        r10 = r9.f434id;
        if (r10 < 0) goto L_0x0434;
    L_0x041c:
        r9 = org.telegram.messenger.ChatObject.isChannel(r9);
        if (r9 == 0) goto L_0x0429;
    L_0x0422:
        r9 = r1.chat;
        r9 = r9.megagroup;
        if (r9 != 0) goto L_0x0429;
    L_0x0428:
        goto L_0x0434;
    L_0x0429:
        r1.drawNameGroup = r6;
        r9 = 1099694080; // 0x418c0000 float:17.5 double:5.43321066E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
        goto L_0x043e;
    L_0x0434:
        r1.drawNameBroadcast = r6;
        r9 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
    L_0x043e:
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x0460;
    L_0x0442:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.nameLockLeft = r9;
        r9 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = r1.drawNameGroup;
        if (r10 == 0) goto L_0x0455;
    L_0x0452:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x0457;
    L_0x0455:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x0457:
        r10 = r10.getIntrinsicWidth();
        r9 = r9 + r10;
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x0460:
        r9 = r33.getMeasuredWidth();
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r9 = r9 - r10;
        r10 = r1.drawNameGroup;
        if (r10 == 0) goto L_0x0470;
    L_0x046d:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x0472;
    L_0x0470:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x0472:
        r10 = r10.getIntrinsicWidth();
        r9 = r9 - r10;
        r1.nameLockLeft = r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x0481:
        r9 = r1.chat;
        r10 = r9.f434id;
        if (r10 < 0) goto L_0x049f;
    L_0x0487:
        r9 = org.telegram.messenger.ChatObject.isChannel(r9);
        if (r9 == 0) goto L_0x0494;
    L_0x048d:
        r9 = r1.chat;
        r9 = r9.megagroup;
        if (r9 != 0) goto L_0x0494;
    L_0x0493:
        goto L_0x049f;
    L_0x0494:
        r1.drawNameGroup = r6;
        r9 = 1096286208; // 0x41580000 float:13.5 double:5.416373534E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
        goto L_0x04a9;
    L_0x049f:
        r1.drawNameBroadcast = r6;
        r9 = 1095237632; // 0x41480000 float:12.5 double:5.41119288E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
    L_0x04a9:
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x04cb;
    L_0x04ad:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.nameLockLeft = r9;
        r9 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = r1.drawNameGroup;
        if (r10 == 0) goto L_0x04c0;
    L_0x04bd:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x04c2;
    L_0x04c0:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x04c2:
        r10 = r10.getIntrinsicWidth();
        r9 = r9 + r10;
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x04cb:
        r9 = r33.getMeasuredWidth();
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r9 = r9 - r10;
        r10 = r1.drawNameGroup;
        if (r10 == 0) goto L_0x04db;
    L_0x04d8:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        goto L_0x04dd;
    L_0x04db:
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
    L_0x04dd:
        r10 = r10.getIntrinsicWidth();
        r9 = r9 - r10;
        r1.nameLockLeft = r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x04ec:
        r9 = r1.user;
        if (r9 == 0) goto L_0x058a;
    L_0x04f0:
        r10 = r9.scam;
        if (r10 == 0) goto L_0x04fc;
    L_0x04f4:
        r1.drawScam = r6;
        r9 = org.telegram.p004ui.ActionBar.Theme.dialogs_scamDrawable;
        r9.checkText();
        goto L_0x0500;
    L_0x04fc:
        r9 = r9.verified;
        r1.drawVerified = r9;
    L_0x0500:
        r9 = org.telegram.messenger.SharedConfig.drawDialogIcons;
        if (r9 == 0) goto L_0x058a;
    L_0x0504:
        r9 = r1.user;
        r9 = r9.bot;
        if (r9 == 0) goto L_0x058a;
    L_0x050a:
        r1.drawNameBot = r6;
        r9 = r1.useForceThreeLines;
        if (r9 != 0) goto L_0x0550;
    L_0x0510:
        r9 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r9 == 0) goto L_0x0515;
    L_0x0514:
        goto L_0x0550;
    L_0x0515:
        r9 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x0537;
    L_0x0521:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.nameLockLeft = r9;
        r9 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_botDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 + r10;
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x0537:
        r9 = r33.getMeasuredWidth();
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r9 = r9 - r10;
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_botDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 - r10;
        r1.nameLockLeft = r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x0550:
        r9 = 1095237632; // 0x41480000 float:12.5 double:5.41119288E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.nameLockTop = r9;
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x0572;
    L_0x055c:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.nameLockLeft = r9;
        r9 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_botDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 + r10;
        r1.nameLeft = r9;
        goto L_0x058a;
    L_0x0572:
        r9 = r33.getMeasuredWidth();
        r10 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r9 = r9 - r10;
        r10 = org.telegram.p004ui.ActionBar.Theme.dialogs_botDrawable;
        r10 = r10.getIntrinsicWidth();
        r9 = r9 - r10;
        r1.nameLockLeft = r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r1.nameLeft = r9;
    L_0x058a:
        r9 = r1.lastMessageDate;
        if (r9 != 0) goto L_0x0596;
    L_0x058e:
        r10 = r1.message;
        if (r10 == 0) goto L_0x0596;
    L_0x0592:
        r9 = r10.messageOwner;
        r9 = r9.date;
    L_0x0596:
        r10 = r1.isDialogCell;
        if (r10 == 0) goto L_0x05f5;
    L_0x059a:
        r10 = r1.currentAccount;
        r10 = org.telegram.messenger.DataQuery.getInstance(r10);
        r18 = r7;
        r6 = r1.currentDialogId;
        r6 = r10.getDraft(r6);
        r1.draftMessage = r6;
        r6 = r1.draftMessage;
        if (r6 == 0) goto L_0x05c9;
    L_0x05ae:
        r6 = r6.message;
        r6 = android.text.TextUtils.isEmpty(r6);
        if (r6 == 0) goto L_0x05bf;
    L_0x05b6:
        r6 = r1.draftMessage;
        r6 = r6.reply_to_msg_id;
        if (r6 == 0) goto L_0x05bd;
    L_0x05bc:
        goto L_0x05bf;
    L_0x05bd:
        r6 = 0;
        goto L_0x05f0;
    L_0x05bf:
        r6 = r1.draftMessage;
        r6 = r6.date;
        if (r9 <= r6) goto L_0x05c9;
    L_0x05c5:
        r6 = r1.unreadCount;
        if (r6 != 0) goto L_0x05bd;
    L_0x05c9:
        r6 = r1.chat;
        r6 = org.telegram.messenger.ChatObject.isChannel(r6);
        if (r6 == 0) goto L_0x05e3;
    L_0x05d1:
        r6 = r1.chat;
        r7 = r6.megagroup;
        if (r7 != 0) goto L_0x05e3;
    L_0x05d7:
        r7 = r6.creator;
        if (r7 != 0) goto L_0x05e3;
    L_0x05db:
        r6 = r6.admin_rights;
        if (r6 == 0) goto L_0x05bd;
    L_0x05df:
        r6 = r6.post_messages;
        if (r6 == 0) goto L_0x05bd;
    L_0x05e3:
        r6 = r1.chat;
        if (r6 == 0) goto L_0x05f3;
    L_0x05e7:
        r7 = r6.left;
        if (r7 != 0) goto L_0x05bd;
    L_0x05eb:
        r6 = r6.kicked;
        if (r6 == 0) goto L_0x05f3;
    L_0x05ef:
        goto L_0x05bd;
    L_0x05f0:
        r1.draftMessage = r6;
        goto L_0x05fa;
    L_0x05f3:
        r6 = 0;
        goto L_0x05fa;
    L_0x05f5:
        r18 = r7;
        r6 = 0;
        r1.draftMessage = r6;
    L_0x05fa:
        if (r0 == 0) goto L_0x0607;
    L_0x05fc:
        r1.lastPrintString = r0;
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r8 = r4;
        r7 = r6;
        r6 = 1;
    L_0x0603:
        r4 = r0;
        r0 = 1;
        goto L_0x0acb;
    L_0x0607:
        r1.lastPrintString = r6;
        r0 = r1.draftMessage;
        if (r0 == 0) goto L_0x06b8;
    L_0x060d:
        r0 = 2131559300; // 0x7f0d0384 float:1.874394E38 double:1.053130222E-314;
        r6 = "Draft";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
        r6 = r1.draftMessage;
        r6 = r6.message;
        r6 = android.text.TextUtils.isEmpty(r6);
        if (r6 == 0) goto L_0x064c;
    L_0x0620:
        r6 = r1.useForceThreeLines;
        if (r6 != 0) goto L_0x0645;
    L_0x0624:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x0629;
    L_0x0628:
        goto L_0x0645;
    L_0x0629:
        r6 = android.text.SpannableStringBuilder.valueOf(r0);
        r7 = new android.text.style.ForegroundColorSpan;
        r8 = "chats_draft";
        r8 = org.telegram.p004ui.ActionBar.Theme.getColor(r8);
        r7.<init>(r8);
        r8 = r0.length();
        r9 = 33;
        r6.setSpan(r7, r2, r8, r9);
    L_0x0641:
        r7 = r0;
        r8 = r4;
        r4 = r6;
        goto L_0x0648;
    L_0x0645:
        r7 = r0;
        r8 = r4;
        r4 = r12;
    L_0x0648:
        r0 = 0;
        r6 = 1;
        goto L_0x0acb;
    L_0x064c:
        r6 = r1.draftMessage;
        r6 = r6.message;
        r7 = r6.length();
        if (r7 <= r14) goto L_0x065a;
    L_0x0656:
        r6 = r6.substring(r2, r14);
    L_0x065a:
        r7 = r1.useForceThreeLines;
        if (r7 != 0) goto L_0x0690;
    L_0x065e:
        r7 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r7 == 0) goto L_0x0663;
    L_0x0662:
        goto L_0x0690;
    L_0x0663:
        r7 = new java.lang.Object[r3];
        r8 = 32;
        r6 = r6.replace(r11, r8);
        r7[r2] = r6;
        r8 = 1;
        r7[r8] = r0;
        r9 = r18;
        r6 = java.lang.String.format(r9, r7);
        r6 = android.text.SpannableStringBuilder.valueOf(r6);
        r7 = new android.text.style.ForegroundColorSpan;
        r9 = "chats_draft";
        r9 = org.telegram.p004ui.ActionBar.Theme.getColor(r9);
        r7.<init>(r9);
        r9 = r0.length();
        r9 = r9 + r8;
        r10 = 33;
        r6.setSpan(r7, r2, r9, r10);
        goto L_0x06a7;
    L_0x0690:
        r9 = r18;
        r8 = 1;
        r7 = new java.lang.Object[r3];
        r10 = 32;
        r6 = r6.replace(r11, r10);
        r7[r2] = r6;
        r7[r8] = r0;
        r6 = java.lang.String.format(r9, r7);
        r6 = android.text.SpannableStringBuilder.valueOf(r6);
    L_0x06a7:
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r7 = r7.getFontMetricsInt();
        r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r6 = org.telegram.messenger.Emoji.replaceEmoji(r6, r7, r9, r2);
        goto L_0x0641;
    L_0x06b8:
        r9 = r18;
        r0 = r1.clearingDialog;
        if (r0 == 0) goto L_0x06ce;
    L_0x06be:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r0 = 2131559639; // 0x7f0d04d7 float:1.8744628E38 double:1.0531303897E-314;
        r6 = "HistoryCleared";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
    L_0x06c9:
        r8 = r4;
        r6 = 1;
        r7 = 0;
        goto L_0x0603;
    L_0x06ce:
        r0 = r1.message;
        if (r0 != 0) goto L_0x0769;
    L_0x06d2:
        r0 = r1.encryptedChat;
        if (r0 == 0) goto L_0x0762;
    L_0x06d6:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r6 = r0 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChatRequested;
        if (r6 == 0) goto L_0x06e6;
    L_0x06dc:
        r0 = 2131559363; // 0x7f0d03c3 float:1.8744068E38 double:1.0531302533E-314;
        r6 = "EncryptionProcessing";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
        goto L_0x06c9;
    L_0x06e6:
        r6 = r0 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChatWaiting;
        if (r6 == 0) goto L_0x0710;
    L_0x06ea:
        r0 = r1.user;
        if (r0 == 0) goto L_0x0701;
    L_0x06ee:
        r0 = r0.first_name;
        if (r0 == 0) goto L_0x0701;
    L_0x06f2:
        r6 = 2131558808; // 0x7f0d0198 float:1.8742942E38 double:1.053129979E-314;
        r7 = 1;
        r8 = new java.lang.Object[r7];
        r8[r2] = r0;
        r0 = "AwaitingEncryption";
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r6, r8);
        goto L_0x06c9;
    L_0x0701:
        r7 = 1;
        r0 = 2131558808; // 0x7f0d0198 float:1.8742942E38 double:1.053129979E-314;
        r6 = new java.lang.Object[r7];
        r6[r2] = r12;
        r7 = "AwaitingEncryption";
        r0 = org.telegram.messenger.LocaleController.formatString(r7, r0, r6);
        goto L_0x06c9;
    L_0x0710:
        r6 = r0 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChatDiscarded;
        if (r6 == 0) goto L_0x071e;
    L_0x0714:
        r0 = 2131559364; // 0x7f0d03c4 float:1.874407E38 double:1.053130254E-314;
        r6 = "EncryptionRejected";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
        goto L_0x06c9;
    L_0x071e:
        r6 = r0 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChat;
        if (r6 == 0) goto L_0x0762;
    L_0x0722:
        r0 = r0.admin_id;
        r6 = r1.currentAccount;
        r6 = org.telegram.messenger.UserConfig.getInstance(r6);
        r6 = r6.getClientUserId();
        if (r0 != r6) goto L_0x0757;
    L_0x0730:
        r0 = r1.user;
        if (r0 == 0) goto L_0x0747;
    L_0x0734:
        r0 = r0.first_name;
        if (r0 == 0) goto L_0x0747;
    L_0x0738:
        r6 = 2131559352; // 0x7f0d03b8 float:1.8744046E38 double:1.053130248E-314;
        r7 = 1;
        r8 = new java.lang.Object[r7];
        r8[r2] = r0;
        r0 = "EncryptedChatStartedOutgoing";
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r6, r8);
        goto L_0x06c9;
    L_0x0747:
        r7 = 1;
        r0 = 2131559352; // 0x7f0d03b8 float:1.8744046E38 double:1.053130248E-314;
        r6 = new java.lang.Object[r7];
        r6[r2] = r12;
        r7 = "EncryptedChatStartedOutgoing";
        r0 = org.telegram.messenger.LocaleController.formatString(r7, r0, r6);
        goto L_0x06c9;
    L_0x0757:
        r0 = 2131559351; // 0x7f0d03b7 float:1.8744044E38 double:1.0531302474E-314;
        r6 = "EncryptedChatStartedIncoming";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
        goto L_0x06c9;
    L_0x0762:
        r8 = r4;
        r4 = r12;
        r0 = 1;
        r6 = 1;
        r7 = 0;
        goto L_0x0acb;
    L_0x0769:
        r0 = r0.isFromUser();
        if (r0 == 0) goto L_0x0786;
    L_0x076f:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r6 = r1.message;
        r6 = r6.messageOwner;
        r6 = r6.from_id;
        r6 = java.lang.Integer.valueOf(r6);
        r0 = r0.getUser(r6);
        r6 = r0;
        r0 = 0;
        goto L_0x079d;
    L_0x0786:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r6 = r1.message;
        r6 = r6.messageOwner;
        r6 = r6.to_id;
        r6 = r6.channel_id;
        r6 = java.lang.Integer.valueOf(r6);
        r0 = r0.getChat(r6);
        r6 = 0;
    L_0x079d:
        r7 = r1.dialogsType;
        r10 = 3;
        if (r7 != r10) goto L_0x07bb;
    L_0x07a2:
        r7 = r1.user;
        r7 = org.telegram.messenger.UserObject.isUserSelf(r7);
        if (r7 == 0) goto L_0x07bb;
    L_0x07aa:
        r0 = 2131560634; // 0x7f0d08ba float:1.8746646E38 double:1.0531308813E-314;
        r5 = "SavedMessagesInfo";
        r0 = org.telegram.messenger.LocaleController.getString(r5, r0);
        r6 = r0;
        r8 = r4;
        r0 = 0;
        r4 = 1;
        r5 = 0;
    L_0x07b8:
        r7 = 0;
        goto L_0x0abd;
    L_0x07bb:
        r7 = r1.useForceThreeLines;
        if (r7 != 0) goto L_0x07d0;
    L_0x07bf:
        r7 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r7 != 0) goto L_0x07d0;
    L_0x07c3:
        r7 = r1.currentDialogFolderId;
        if (r7 == 0) goto L_0x07d0;
    L_0x07c7:
        r0 = r33.formatArchivedDialogNames();
        r6 = r0;
        r8 = r4;
        r0 = 1;
        r4 = 0;
        goto L_0x07b8;
    L_0x07d0:
        r7 = r1.message;
        r10 = r7.messageOwner;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageService;
        if (r10 == 0) goto L_0x07fc;
    L_0x07d8:
        r0 = r1.chat;
        r0 = org.telegram.messenger.ChatObject.isChannel(r0);
        if (r0 == 0) goto L_0x07f1;
    L_0x07e0:
        r0 = r1.message;
        r0 = r0.messageOwner;
        r0 = r0.action;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
        if (r4 != 0) goto L_0x07ee;
    L_0x07ea:
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom;
        if (r0 == 0) goto L_0x07f1;
    L_0x07ee:
        r0 = r12;
        r5 = 0;
        goto L_0x07f5;
    L_0x07f1:
        r0 = r1.message;
        r0 = r0.messageText;
    L_0x07f5:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
    L_0x07f7:
        r6 = r0;
        r8 = r4;
        r0 = 1;
        r4 = 1;
        goto L_0x07b8;
    L_0x07fc:
        r10 = r1.chat;
        if (r10 == 0) goto L_0x09e2;
    L_0x0800:
        r10 = r10.f434id;
        if (r10 <= 0) goto L_0x09e2;
    L_0x0804:
        if (r0 != 0) goto L_0x09e2;
    L_0x0806:
        r7 = r7.isOutOwner();
        if (r7 == 0) goto L_0x0817;
    L_0x080c:
        r0 = 2131559584; // 0x7f0d04a0 float:1.8744516E38 double:1.0531303625E-314;
        r6 = "FromYou";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
    L_0x0815:
        r6 = r0;
        goto L_0x085a;
    L_0x0817:
        if (r6 == 0) goto L_0x084c;
    L_0x0819:
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x082d;
    L_0x081d:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x0822;
    L_0x0821:
        goto L_0x082d;
    L_0x0822:
        r0 = org.telegram.messenger.UserObject.getFirstName(r6);
        r6 = "\n";
        r0 = r0.replace(r6, r12);
        goto L_0x0815;
    L_0x082d:
        r0 = org.telegram.messenger.UserObject.isDeleted(r6);
        if (r0 == 0) goto L_0x083d;
    L_0x0833:
        r0 = 2131559636; // 0x7f0d04d4 float:1.8744622E38 double:1.053130388E-314;
        r6 = "HiddenName";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
        goto L_0x0815;
    L_0x083d:
        r0 = r6.first_name;
        r6 = r6.last_name;
        r0 = org.telegram.messenger.ContactsController.formatName(r0, r6);
        r6 = "\n";
        r0 = r0.replace(r6, r12);
        goto L_0x0815;
    L_0x084c:
        if (r0 == 0) goto L_0x0857;
    L_0x084e:
        r0 = r0.title;
        r6 = "\n";
        r0 = r0.replace(r6, r12);
        goto L_0x0815;
    L_0x0857:
        r0 = "DELETED";
        goto L_0x0815;
    L_0x085a:
        r0 = r1.message;
        r7 = r0.caption;
        if (r7 == 0) goto L_0x08c2;
    L_0x0860:
        r0 = r7.toString();
        r7 = r0.length();
        if (r7 <= r14) goto L_0x086e;
    L_0x086a:
        r0 = r0.substring(r2, r14);
    L_0x086e:
        r7 = r1.message;
        r7 = r7.isVideo();
        if (r7 == 0) goto L_0x0879;
    L_0x0876:
        r7 = "📹 ";
        goto L_0x089c;
    L_0x0879:
        r7 = r1.message;
        r7 = r7.isVoice();
        if (r7 == 0) goto L_0x0884;
    L_0x0881:
        r7 = "🎤 ";
        goto L_0x089c;
    L_0x0884:
        r7 = r1.message;
        r7 = r7.isMusic();
        if (r7 == 0) goto L_0x088f;
    L_0x088c:
        r7 = "🎧 ";
        goto L_0x089c;
    L_0x088f:
        r7 = r1.message;
        r7 = r7.isPhoto();
        if (r7 == 0) goto L_0x089a;
    L_0x0897:
        r7 = "🖼 ";
        goto L_0x089c;
    L_0x089a:
        r7 = "📎 ";
    L_0x089c:
        r8 = new java.lang.Object[r3];
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r10.append(r7);
        r7 = 32;
        r0 = r0.replace(r11, r7);
        r10.append(r0);
        r0 = r10.toString();
        r8[r2] = r0;
        r7 = 1;
        r8[r7] = r6;
        r0 = java.lang.String.format(r9, r8);
        r0 = android.text.SpannableStringBuilder.valueOf(r0);
        goto L_0x099d;
    L_0x08c2:
        r7 = r0.messageOwner;
        r7 = r7.media;
        if (r7 == 0) goto L_0x0971;
    L_0x08c8:
        r0 = r0.isMediaEmpty();
        if (r0 != 0) goto L_0x0971;
    L_0x08ce:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r0 = r1.message;
        r7 = r0.messageOwner;
        r7 = r7.media;
        r10 = r7 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r10 == 0) goto L_0x0901;
    L_0x08da:
        r0 = android.os.Build.VERSION.SDK_INT;
        r10 = 18;
        if (r0 < r10) goto L_0x08f0;
    L_0x08e0:
        r10 = 1;
        r0 = new java.lang.Object[r10];
        r7 = r7.game;
        r7 = r7.title;
        r0[r2] = r7;
        r7 = "🎮 ⁨%s⁩";
        r0 = java.lang.String.format(r7, r0);
        goto L_0x0940;
    L_0x08f0:
        r10 = 1;
        r0 = new java.lang.Object[r10];
        r7 = r7.game;
        r7 = r7.title;
        r0[r2] = r7;
        r7 = "🎮 %s";
        r0 = java.lang.String.format(r7, r0);
        r10 = 1;
        goto L_0x0940;
    L_0x0901:
        r7 = r0.type;
        r10 = 14;
        if (r7 != r10) goto L_0x093d;
    L_0x0907:
        r7 = android.os.Build.VERSION.SDK_INT;
        r10 = 18;
        if (r7 < r10) goto L_0x0925;
    L_0x090d:
        r7 = new java.lang.Object[r3];
        r0 = r0.getMusicAuthor();
        r7[r2] = r0;
        r0 = r1.message;
        r0 = r0.getMusicTitle();
        r10 = 1;
        r7[r10] = r0;
        r0 = "🎧 ⁨%s - %s⁩";
        r0 = java.lang.String.format(r0, r7);
        goto L_0x0940;
    L_0x0925:
        r10 = 1;
        r7 = new java.lang.Object[r3];
        r0 = r0.getMusicAuthor();
        r7[r2] = r0;
        r0 = r1.message;
        r0 = r0.getMusicTitle();
        r7[r10] = r0;
        r0 = "🎧 %s - %s";
        r0 = java.lang.String.format(r0, r7);
        goto L_0x0940;
    L_0x093d:
        r10 = 1;
        r0 = r0.messageText;
    L_0x0940:
        r7 = new java.lang.Object[r3];
        r7[r2] = r0;
        r7[r10] = r6;
        r0 = java.lang.String.format(r9, r7);
        r7 = android.text.SpannableStringBuilder.valueOf(r0);
        r0 = new android.text.style.ForegroundColorSpan;	 Catch:{ Exception -> 0x096c }
        r9 = "chats_attachMessage";
        r9 = org.telegram.p004ui.ActionBar.Theme.getColor(r9);	 Catch:{ Exception -> 0x096c }
        r0.<init>(r9);	 Catch:{ Exception -> 0x096c }
        if (r8 == 0) goto L_0x0961;
    L_0x095b:
        r8 = r6.length();	 Catch:{ Exception -> 0x096c }
        r8 = r8 + r3;
        goto L_0x0962;
    L_0x0961:
        r8 = 0;
    L_0x0962:
        r9 = r7.length();	 Catch:{ Exception -> 0x096c }
        r10 = 33;
        r7.setSpan(r0, r8, r9, r10);	 Catch:{ Exception -> 0x096c }
        goto L_0x099e;
    L_0x096c:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
        goto L_0x099e;
    L_0x0971:
        r0 = r1.message;
        r0 = r0.messageOwner;
        r0 = r0.message;
        if (r0 == 0) goto L_0x0999;
    L_0x0979:
        r7 = r0.length();
        if (r7 <= r14) goto L_0x0983;
    L_0x097f:
        r0 = r0.substring(r2, r14);
    L_0x0983:
        r7 = new java.lang.Object[r3];
        r8 = 32;
        r0 = r0.replace(r11, r8);
        r7[r2] = r0;
        r8 = 1;
        r7[r8] = r6;
        r0 = java.lang.String.format(r9, r7);
        r0 = android.text.SpannableStringBuilder.valueOf(r0);
        goto L_0x099d;
    L_0x0999:
        r0 = android.text.SpannableStringBuilder.valueOf(r12);
    L_0x099d:
        r7 = r0;
    L_0x099e:
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x09a6;
    L_0x09a2:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x09b0;
    L_0x09a6:
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x09cb;
    L_0x09aa:
        r0 = r7.length();
        if (r0 <= 0) goto L_0x09cb;
    L_0x09b0:
        r0 = new android.text.style.ForegroundColorSpan;	 Catch:{ Exception -> 0x09c7 }
        r8 = "chats_nameMessage";
        r8 = org.telegram.p004ui.ActionBar.Theme.getColor(r8);	 Catch:{ Exception -> 0x09c7 }
        r0.<init>(r8);	 Catch:{ Exception -> 0x09c7 }
        r8 = r6.length();	 Catch:{ Exception -> 0x09c7 }
        r9 = 1;
        r8 = r8 + r9;
        r9 = 33;
        r7.setSpan(r0, r2, r8, r9);	 Catch:{ Exception -> 0x09c7 }
        goto L_0x09cb;
    L_0x09c7:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x09cb:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r0 = r0.getFontMetricsInt();
        r8 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r0 = org.telegram.messenger.Emoji.replaceEmoji(r7, r0, r9, r2);
        r8 = r4;
        r7 = r6;
        r4 = 0;
        r6 = r0;
        r0 = 1;
        goto L_0x0abd;
    L_0x09e2:
        r0 = r1.message;
        r0 = r0.messageOwner;
        r0 = r0.media;
        r6 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r6 == 0) goto L_0x0a01;
    L_0x09ec:
        r6 = r0.photo;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_photoEmpty;
        if (r6 == 0) goto L_0x0a01;
    L_0x09f2:
        r0 = r0.ttl_seconds;
        if (r0 == 0) goto L_0x0a01;
    L_0x09f6:
        r0 = 2131558728; // 0x7f0d0148 float:1.874278E38 double:1.0531299396E-314;
        r6 = "AttachPhotoExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
        goto L_0x07f7;
    L_0x0a01:
        r0 = r1.message;
        r0 = r0.messageOwner;
        r0 = r0.media;
        r6 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r6 == 0) goto L_0x0a20;
    L_0x0a0b:
        r6 = r0.document;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentEmpty;
        if (r6 == 0) goto L_0x0a20;
    L_0x0a11:
        r0 = r0.ttl_seconds;
        if (r0 == 0) goto L_0x0a20;
    L_0x0a15:
        r0 = 2131558734; // 0x7f0d014e float:1.8742792E38 double:1.0531299426E-314;
        r6 = "AttachVideoExpired";
        r0 = org.telegram.messenger.LocaleController.getString(r6, r0);
        goto L_0x07f7;
    L_0x0a20:
        r0 = r1.message;
        r6 = r0.caption;
        if (r6 == 0) goto L_0x0a67;
    L_0x0a26:
        r0 = r0.isVideo();
        if (r0 == 0) goto L_0x0a2f;
    L_0x0a2c:
        r0 = "📹 ";
        goto L_0x0a52;
    L_0x0a2f:
        r0 = r1.message;
        r0 = r0.isVoice();
        if (r0 == 0) goto L_0x0a3a;
    L_0x0a37:
        r0 = "🎤 ";
        goto L_0x0a52;
    L_0x0a3a:
        r0 = r1.message;
        r0 = r0.isMusic();
        if (r0 == 0) goto L_0x0a45;
    L_0x0a42:
        r0 = "🎧 ";
        goto L_0x0a52;
    L_0x0a45:
        r0 = r1.message;
        r0 = r0.isPhoto();
        if (r0 == 0) goto L_0x0a50;
    L_0x0a4d:
        r0 = "🖼 ";
        goto L_0x0a52;
    L_0x0a50:
        r0 = "📎 ";
    L_0x0a52:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6.append(r0);
        r0 = r1.message;
        r0 = r0.caption;
        r6.append(r0);
        r0 = r6.toString();
        goto L_0x07f7;
    L_0x0a67:
        r6 = r0.messageOwner;
        r6 = r6.media;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r6 == 0) goto L_0x0a8b;
    L_0x0a6f:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r6 = "🎮 ";
        r0.append(r6);
        r6 = r1.message;
        r6 = r6.messageOwner;
        r6 = r6.media;
        r6 = r6.game;
        r6 = r6.title;
        r0.append(r6);
        r0 = r0.toString();
        goto L_0x0aab;
    L_0x0a8b:
        r6 = r0.type;
        r7 = 14;
        if (r6 != r7) goto L_0x0aa9;
    L_0x0a91:
        r6 = new java.lang.Object[r3];
        r0 = r0.getMusicAuthor();
        r6[r2] = r0;
        r0 = r1.message;
        r0 = r0.getMusicTitle();
        r7 = 1;
        r6[r7] = r0;
        r0 = "🎧 %s - %s";
        r0 = java.lang.String.format(r0, r6);
        goto L_0x0aab;
    L_0x0aa9:
        r0 = r0.messageText;
    L_0x0aab:
        r6 = r1.message;
        r7 = r6.messageOwner;
        r7 = r7.media;
        if (r7 == 0) goto L_0x07f7;
    L_0x0ab3:
        r6 = r6.isMediaEmpty();
        if (r6 != 0) goto L_0x07f7;
    L_0x0ab9:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        goto L_0x07f7;
    L_0x0abd:
        r9 = r1.currentDialogFolderId;
        if (r9 == 0) goto L_0x0ac5;
    L_0x0ac1:
        r7 = r33.formatArchivedDialogNames();
    L_0x0ac5:
        r32 = r6;
        r6 = r0;
        r0 = r4;
        r4 = r32;
    L_0x0acb:
        r9 = r1.draftMessage;
        if (r9 == 0) goto L_0x0ad7;
    L_0x0acf:
        r9 = r9.date;
        r9 = (long) r9;
        r9 = org.telegram.messenger.LocaleController.stringForMessageListDate(r9);
        goto L_0x0af0;
    L_0x0ad7:
        r9 = r1.lastMessageDate;
        if (r9 == 0) goto L_0x0ae1;
    L_0x0adb:
        r9 = (long) r9;
        r9 = org.telegram.messenger.LocaleController.stringForMessageListDate(r9);
        goto L_0x0af0;
    L_0x0ae1:
        r9 = r1.message;
        if (r9 == 0) goto L_0x0aef;
    L_0x0ae5:
        r9 = r9.messageOwner;
        r9 = r9.date;
        r9 = (long) r9;
        r9 = org.telegram.messenger.LocaleController.stringForMessageListDate(r9);
        goto L_0x0af0;
    L_0x0aef:
        r9 = r12;
    L_0x0af0:
        r10 = r1.message;
        if (r10 != 0) goto L_0x0b04;
    L_0x0af4:
        r1.drawCheck1 = r2;
        r1.drawCheck2 = r2;
        r1.drawClock = r2;
        r1.drawCount = r2;
        r1.drawMention = r2;
        r1.drawError = r2;
        r3 = 0;
        r10 = 0;
        goto L_0x0c06;
    L_0x0b04:
        r3 = r1.currentDialogFolderId;
        if (r3 == 0) goto L_0x0b45;
    L_0x0b08:
        r3 = r1.unreadCount;
        r10 = r1.mentionCount;
        r19 = r3 + r10;
        if (r19 <= 0) goto L_0x0b3e;
    L_0x0b10:
        if (r3 <= r10) goto L_0x0b27;
    L_0x0b12:
        r14 = 1;
        r1.drawCount = r14;
        r1.drawMention = r2;
        r15 = new java.lang.Object[r14];
        r3 = r3 + r10;
        r3 = java.lang.Integer.valueOf(r3);
        r15[r2] = r3;
        r3 = "%d";
        r3 = java.lang.String.format(r3, r15);
        goto L_0x0b43;
    L_0x0b27:
        r14 = 1;
        r1.drawCount = r2;
        r1.drawMention = r14;
        r15 = new java.lang.Object[r14];
        r3 = r3 + r10;
        r3 = java.lang.Integer.valueOf(r3);
        r15[r2] = r3;
        r3 = "%d";
        r3 = java.lang.String.format(r3, r15);
        r10 = r3;
        r3 = 0;
        goto L_0x0b94;
    L_0x0b3e:
        r1.drawCount = r2;
        r1.drawMention = r2;
        r3 = 0;
    L_0x0b43:
        r10 = 0;
        goto L_0x0b94;
    L_0x0b45:
        r3 = r1.clearingDialog;
        if (r3 == 0) goto L_0x0b4f;
    L_0x0b49:
        r1.drawCount = r2;
        r3 = 1;
        r5 = 0;
    L_0x0b4d:
        r10 = 0;
        goto L_0x0b82;
    L_0x0b4f:
        r3 = r1.unreadCount;
        if (r3 == 0) goto L_0x0b76;
    L_0x0b53:
        r14 = 1;
        if (r3 != r14) goto L_0x0b62;
    L_0x0b56:
        r14 = r1.mentionCount;
        if (r3 != r14) goto L_0x0b62;
    L_0x0b5a:
        if (r10 == 0) goto L_0x0b62;
    L_0x0b5c:
        r3 = r10.messageOwner;
        r3 = r3.mentioned;
        if (r3 != 0) goto L_0x0b76;
    L_0x0b62:
        r3 = 1;
        r1.drawCount = r3;
        r10 = new java.lang.Object[r3];
        r14 = r1.unreadCount;
        r14 = java.lang.Integer.valueOf(r14);
        r10[r2] = r14;
        r14 = "%d";
        r10 = java.lang.String.format(r14, r10);
        goto L_0x0b82;
    L_0x0b76:
        r3 = 1;
        r10 = r1.markUnread;
        if (r10 == 0) goto L_0x0b7f;
    L_0x0b7b:
        r1.drawCount = r3;
        r10 = r12;
        goto L_0x0b82;
    L_0x0b7f:
        r1.drawCount = r2;
        goto L_0x0b4d;
    L_0x0b82:
        r14 = r1.mentionCount;
        if (r14 == 0) goto L_0x0b90;
    L_0x0b86:
        r1.drawMention = r3;
        r3 = "@";
        r32 = r10;
        r10 = r3;
        r3 = r32;
        goto L_0x0b94;
    L_0x0b90:
        r1.drawMention = r2;
        r3 = r10;
        goto L_0x0b43;
    L_0x0b94:
        r14 = r1.message;
        r14 = r14.isOut();
        if (r14 == 0) goto L_0x0bfe;
    L_0x0b9c:
        r14 = r1.draftMessage;
        if (r14 != 0) goto L_0x0bfe;
    L_0x0ba0:
        if (r5 == 0) goto L_0x0bfe;
    L_0x0ba2:
        r5 = r1.message;
        r14 = r5.messageOwner;
        r14 = r14.action;
        r14 = r14 instanceof org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
        if (r14 != 0) goto L_0x0bfe;
    L_0x0bac:
        r5 = r5.isSending();
        if (r5 == 0) goto L_0x0bbc;
    L_0x0bb2:
        r1.drawCheck1 = r2;
        r1.drawCheck2 = r2;
        r5 = 1;
        r1.drawClock = r5;
        r1.drawError = r2;
        goto L_0x0c06;
    L_0x0bbc:
        r5 = 1;
        r14 = r1.message;
        r14 = r14.isSendError();
        if (r14 == 0) goto L_0x0bd2;
    L_0x0bc5:
        r1.drawCheck1 = r2;
        r1.drawCheck2 = r2;
        r1.drawClock = r2;
        r1.drawError = r5;
        r1.drawCount = r2;
        r1.drawMention = r2;
        goto L_0x0c06;
    L_0x0bd2:
        r5 = r1.message;
        r5 = r5.isSent();
        if (r5 == 0) goto L_0x0c06;
    L_0x0bda:
        r5 = r1.message;
        r5 = r5.isUnread();
        if (r5 == 0) goto L_0x0bf3;
    L_0x0be2:
        r5 = r1.chat;
        r5 = org.telegram.messenger.ChatObject.isChannel(r5);
        if (r5 == 0) goto L_0x0bf1;
    L_0x0bea:
        r5 = r1.chat;
        r5 = r5.megagroup;
        if (r5 != 0) goto L_0x0bf1;
    L_0x0bf0:
        goto L_0x0bf3;
    L_0x0bf1:
        r5 = 0;
        goto L_0x0bf4;
    L_0x0bf3:
        r5 = 1;
    L_0x0bf4:
        r1.drawCheck1 = r5;
        r5 = 1;
        r1.drawCheck2 = r5;
        r1.drawClock = r2;
        r1.drawError = r2;
        goto L_0x0c06;
    L_0x0bfe:
        r1.drawCheck1 = r2;
        r1.drawCheck2 = r2;
        r1.drawClock = r2;
        r1.drawError = r2;
    L_0x0c06:
        r5 = r1.dialogsType;
        if (r5 != 0) goto L_0x0c25;
    L_0x0c0a:
        r5 = r1.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);
        r14 = r1.currentDialogId;
        r13 = 1;
        r5 = r5.isProxyDialog(r14, r13);
        if (r5 == 0) goto L_0x0c25;
    L_0x0c19:
        r1.drawPinBackground = r13;
        r5 = 2131560980; // 0x7f0d0a14 float:1.8747348E38 double:1.053131052E-314;
        r9 = "UseProxySponsor";
        r5 = org.telegram.messenger.LocaleController.getString(r9, r5);
        goto L_0x0c26;
    L_0x0c25:
        r5 = r9;
    L_0x0c26:
        r9 = r1.currentDialogFolderId;
        if (r9 == 0) goto L_0x0c38;
    L_0x0c2a:
        r9 = 2131558653; // 0x7f0d00fd float:1.8742628E38 double:1.0531299025E-314;
        r13 = "ArchivedChats";
        r9 = org.telegram.messenger.LocaleController.getString(r13, r9);
    L_0x0c33:
        r13 = r8;
        r8 = r7;
        r7 = r4;
        goto L_0x0335;
    L_0x0c38:
        r9 = r1.chat;
        if (r9 == 0) goto L_0x0c40;
    L_0x0c3c:
        r9 = r9.title;
        goto L_0x0ce7;
    L_0x0c40:
        r9 = r1.user;
        if (r9 == 0) goto L_0x0ce6;
    L_0x0c44:
        r9 = org.telegram.messenger.UserObject.isUserSelf(r9);
        if (r9 == 0) goto L_0x0c5d;
    L_0x0c4a:
        r9 = r1.dialogsType;
        r13 = 3;
        if (r9 != r13) goto L_0x0c52;
    L_0x0c4f:
        r9 = 1;
        r1.drawPinBackground = r9;
    L_0x0c52:
        r9 = 2131560633; // 0x7f0d08b9 float:1.8746644E38 double:1.053130881E-314;
        r13 = "SavedMessages";
        r9 = org.telegram.messenger.LocaleController.getString(r13, r9);
        goto L_0x0ce7;
    L_0x0c5d:
        r9 = r1.user;
        r9 = r9.f534id;
        r13 = r9 / 1000;
        r14 = 777; // 0x309 float:1.089E-42 double:3.84E-321;
        if (r13 == r14) goto L_0x0cdf;
    L_0x0c67:
        r9 = r9 / 1000;
        r13 = 333; // 0x14d float:4.67E-43 double:1.645E-321;
        if (r9 == r13) goto L_0x0cdf;
    L_0x0c6d:
        r9 = r1.currentAccount;
        r9 = org.telegram.messenger.ContactsController.getInstance(r9);
        r9 = r9.contactsDict;
        r13 = r1.user;
        r13 = r13.f534id;
        r13 = java.lang.Integer.valueOf(r13);
        r9 = r9.get(r13);
        if (r9 != 0) goto L_0x0cdf;
    L_0x0c83:
        r9 = r1.currentAccount;
        r9 = org.telegram.messenger.ContactsController.getInstance(r9);
        r9 = r9.contactsDict;
        r9 = r9.size();
        if (r9 != 0) goto L_0x0cae;
    L_0x0c91:
        r9 = r1.currentAccount;
        r9 = org.telegram.messenger.ContactsController.getInstance(r9);
        r9 = r9.contactsLoaded;
        if (r9 == 0) goto L_0x0ca7;
    L_0x0c9b:
        r9 = r1.currentAccount;
        r9 = org.telegram.messenger.ContactsController.getInstance(r9);
        r9 = r9.isLoadingContacts();
        if (r9 == 0) goto L_0x0cae;
    L_0x0ca7:
        r9 = r1.user;
        r9 = org.telegram.messenger.UserObject.getUserName(r9);
        goto L_0x0ce7;
    L_0x0cae:
        r9 = r1.user;
        r9 = r9.phone;
        if (r9 == 0) goto L_0x0cd8;
    L_0x0cb4:
        r9 = r9.length();
        if (r9 == 0) goto L_0x0cd8;
    L_0x0cba:
        r9 = org.telegram.PhoneFormat.C0278PhoneFormat.getInstance();
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r14 = "+";
        r13.append(r14);
        r14 = r1.user;
        r14 = r14.phone;
        r13.append(r14);
        r13 = r13.toString();
        r9 = r9.format(r13);
        goto L_0x0ce7;
    L_0x0cd8:
        r9 = r1.user;
        r9 = org.telegram.messenger.UserObject.getUserName(r9);
        goto L_0x0ce7;
    L_0x0cdf:
        r9 = r1.user;
        r9 = org.telegram.messenger.UserObject.getUserName(r9);
        goto L_0x0ce7;
    L_0x0ce6:
        r9 = r12;
    L_0x0ce7:
        r13 = r9.length();
        if (r13 != 0) goto L_0x0c33;
    L_0x0ced:
        r9 = 2131559636; // 0x7f0d04d4 float:1.8744622E38 double:1.053130388E-314;
        r13 = "HiddenName";
        r9 = org.telegram.messenger.LocaleController.getString(r13, r9);
        goto L_0x0c33;
    L_0x0cf8:
        if (r6 == 0) goto L_0x0d39;
    L_0x0cfa:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_timePaint;
        r0 = r0.measureText(r5);
        r14 = (double) r0;
        r14 = java.lang.Math.ceil(r14);
        r0 = (int) r14;
        r6 = new android.text.StaticLayout;
        r24 = org.telegram.p004ui.ActionBar.Theme.dialogs_timePaint;
        r26 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r27 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r28 = 0;
        r29 = 0;
        r22 = r6;
        r23 = r5;
        r25 = r0;
        r22.<init>(r23, r24, r25, r26, r27, r28, r29);
        r1.timeLayout = r6;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 != 0) goto L_0x0d30;
    L_0x0d21:
        r5 = r33.getMeasuredWidth();
        r6 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
        r5 = r5 - r0;
        r1.timeLeft = r5;
        goto L_0x0d3f;
    L_0x0d30:
        r5 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r1.timeLeft = r5;
        goto L_0x0d3f;
    L_0x0d39:
        r5 = 0;
        r1.timeLayout = r5;
        r1.timeLeft = r2;
        r0 = 0;
    L_0x0d3f:
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 != 0) goto L_0x0d53;
    L_0x0d43:
        r5 = r33.getMeasuredWidth();
        r6 = r1.nameLeft;
        r5 = r5 - r6;
        r6 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
        r5 = r5 - r0;
        goto L_0x0d67;
    L_0x0d53:
        r5 = r33.getMeasuredWidth();
        r6 = r1.nameLeft;
        r5 = r5 - r6;
        r6 = 1117388800; // 0x429a0000 float:77.0 double:5.52063419E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
        r5 = r5 - r0;
        r6 = r1.nameLeft;
        r6 = r6 + r0;
        r1.nameLeft = r6;
    L_0x0d67:
        r6 = r1.drawNameLock;
        if (r6 == 0) goto L_0x0d7a;
    L_0x0d6b:
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r14 = r14.getIntrinsicWidth();
    L_0x0d77:
        r6 = r6 + r14;
        r5 = r5 - r6;
        goto L_0x0dad;
    L_0x0d7a:
        r6 = r1.drawNameGroup;
        if (r6 == 0) goto L_0x0d8b;
    L_0x0d7e:
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        r14 = r14.getIntrinsicWidth();
        goto L_0x0d77;
    L_0x0d8b:
        r6 = r1.drawNameBroadcast;
        if (r6 == 0) goto L_0x0d9c;
    L_0x0d8f:
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
        r14 = r14.getIntrinsicWidth();
        goto L_0x0d77;
    L_0x0d9c:
        r6 = r1.drawNameBot;
        if (r6 == 0) goto L_0x0dad;
    L_0x0da0:
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_botDrawable;
        r14 = r14.getIntrinsicWidth();
        goto L_0x0d77;
    L_0x0dad:
        r6 = r1.drawClock;
        if (r6 == 0) goto L_0x0ddd;
    L_0x0db1:
        r6 = org.telegram.p004ui.ActionBar.Theme.dialogs_clockDrawable;
        r6 = r6.getIntrinsicWidth();
        r14 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r6 = r6 + r14;
        r5 = r5 - r6;
        r14 = org.telegram.messenger.LocaleController.isRTL;
        if (r14 != 0) goto L_0x0dca;
    L_0x0dc3:
        r0 = r1.timeLeft;
        r0 = r0 - r6;
        r1.checkDrawLeft = r0;
        goto L_0x0e5c;
    L_0x0dca:
        r14 = r1.timeLeft;
        r14 = r14 + r0;
        r0 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r14 = r14 + r0;
        r1.checkDrawLeft = r14;
        r0 = r1.nameLeft;
        r0 = r0 + r6;
        r1.nameLeft = r0;
        goto L_0x0e5c;
    L_0x0ddd:
        r6 = r1.drawCheck2;
        if (r6 == 0) goto L_0x0e5c;
    L_0x0de1:
        r6 = org.telegram.p004ui.ActionBar.Theme.dialogs_checkDrawable;
        r6 = r6.getIntrinsicWidth();
        r14 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r6 = r6 + r14;
        r5 = r5 - r6;
        r14 = r1.drawCheck1;
        if (r14 == 0) goto L_0x0e41;
    L_0x0df3:
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_halfCheckDrawable;
        r14 = r14.getIntrinsicWidth();
        r15 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r14 = r14 - r15;
        r5 = r5 - r14;
        r14 = org.telegram.messenger.LocaleController.isRTL;
        if (r14 != 0) goto L_0x0e16;
    L_0x0e05:
        r0 = r1.timeLeft;
        r0 = r0 - r6;
        r1.halfCheckDrawLeft = r0;
        r0 = r1.halfCheckDrawLeft;
        r6 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r0 = r0 - r6;
        r1.checkDrawLeft = r0;
        goto L_0x0e5c;
    L_0x0e16:
        r14 = r1.timeLeft;
        r14 = r14 + r0;
        r0 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r14 = r14 + r0;
        r1.checkDrawLeft = r14;
        r0 = r1.checkDrawLeft;
        r14 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r0 = r0 + r14;
        r1.halfCheckDrawLeft = r0;
        r0 = r1.nameLeft;
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_halfCheckDrawable;
        r14 = r14.getIntrinsicWidth();
        r6 = r6 + r14;
        r14 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r6 = r6 - r14;
        r0 = r0 + r6;
        r1.nameLeft = r0;
        goto L_0x0e5c;
    L_0x0e41:
        r14 = org.telegram.messenger.LocaleController.isRTL;
        if (r14 != 0) goto L_0x0e4b;
    L_0x0e45:
        r0 = r1.timeLeft;
        r0 = r0 - r6;
        r1.checkDrawLeft = r0;
        goto L_0x0e5c;
    L_0x0e4b:
        r14 = r1.timeLeft;
        r14 = r14 + r0;
        r0 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r14 = r14 + r0;
        r1.checkDrawLeft = r14;
        r0 = r1.nameLeft;
        r0 = r0 + r6;
        r1.nameLeft = r0;
    L_0x0e5c:
        r0 = r1.dialogMuted;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        if (r0 == 0) goto L_0x0e80;
    L_0x0e62:
        r0 = r1.drawVerified;
        if (r0 != 0) goto L_0x0e80;
    L_0x0e66:
        r0 = r1.drawScam;
        if (r0 != 0) goto L_0x0e80;
    L_0x0e6a:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_muteDrawable;
        r14 = r14.getIntrinsicWidth();
        r0 = r0 + r14;
        r5 = r5 - r0;
        r14 = org.telegram.messenger.LocaleController.isRTL;
        if (r14 == 0) goto L_0x0eb3;
    L_0x0e7a:
        r14 = r1.nameLeft;
        r14 = r14 + r0;
        r1.nameLeft = r14;
        goto L_0x0eb3;
    L_0x0e80:
        r0 = r1.drawVerified;
        if (r0 == 0) goto L_0x0e9a;
    L_0x0e84:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r14 = r14.getIntrinsicWidth();
        r0 = r0 + r14;
        r5 = r5 - r0;
        r14 = org.telegram.messenger.LocaleController.isRTL;
        if (r14 == 0) goto L_0x0eb3;
    L_0x0e94:
        r14 = r1.nameLeft;
        r14 = r14 + r0;
        r1.nameLeft = r14;
        goto L_0x0eb3;
    L_0x0e9a:
        r0 = r1.drawScam;
        if (r0 == 0) goto L_0x0eb3;
    L_0x0e9e:
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = org.telegram.p004ui.ActionBar.Theme.dialogs_scamDrawable;
        r14 = r14.getIntrinsicWidth();
        r0 = r0 + r14;
        r5 = r5 - r0;
        r14 = org.telegram.messenger.LocaleController.isRTL;
        if (r14 == 0) goto L_0x0eb3;
    L_0x0eae:
        r14 = r1.nameLeft;
        r14 = r14 + r0;
        r1.nameLeft = r14;
    L_0x0eb3:
        r14 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r5 = java.lang.Math.max(r0, r5);
        r15 = 32;
        r0 = r9.replace(r11, r15);	 Catch:{ Exception -> 0x0ee8 }
        r9 = org.telegram.p004ui.ActionBar.Theme.dialogs_namePaint;	 Catch:{ Exception -> 0x0ee8 }
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r14);	 Catch:{ Exception -> 0x0ee8 }
        r15 = r5 - r15;
        r15 = (float) r15;	 Catch:{ Exception -> 0x0ee8 }
        r6 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x0ee8 }
        r23 = android.text.TextUtils.ellipsize(r0, r9, r15, r6);	 Catch:{ Exception -> 0x0ee8 }
        r0 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0ee8 }
        r24 = org.telegram.p004ui.ActionBar.Theme.dialogs_namePaint;	 Catch:{ Exception -> 0x0ee8 }
        r26 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0ee8 }
        r27 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r28 = 0;
        r29 = 0;
        r22 = r0;
        r25 = r5;
        r22.<init>(r23, r24, r25, r26, r27, r28, r29);	 Catch:{ Exception -> 0x0ee8 }
        r1.nameLayout = r0;	 Catch:{ Exception -> 0x0ee8 }
        goto L_0x0eec;
    L_0x0ee8:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0eec:
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x0f70;
    L_0x0ef0:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x0ef6;
    L_0x0ef4:
        goto L_0x0f70;
    L_0x0ef6:
        r0 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.messageNameTop = r6;
        r6 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.timeTop = r6;
        r6 = 1109131264; // 0x421c0000 float:39.0 double:5.479836543E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.errorTop = r6;
        r6 = 1109131264; // 0x421c0000 float:39.0 double:5.479836543E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.pinTop = r6;
        r6 = 1109131264; // 0x421c0000 float:39.0 double:5.479836543E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.countTop = r6;
        r6 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.checkDrawTop = r6;
        r6 = r33.getMeasuredWidth();
        r9 = 1119748096; // 0x42be0000 float:95.0 double:5.53229066E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = r6 - r9;
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x0f4a;
    L_0x0f3b:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r1.messageNameLeft = r9;
        r1.messageLeft = r9;
        r9 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        goto L_0x0f5f;
    L_0x0f4a:
        r9 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.messageNameLeft = r9;
        r1.messageLeft = r9;
        r9 = r33.getMeasuredWidth();
        r15 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r9 = r9 - r15;
    L_0x0f5f:
        r15 = r1.avatarImage;
        r16 = 1113063424; // 0x42580000 float:54.0 double:5.499263994E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r15.setImageCoords(r9, r0, r11, r14);
        goto L_0x0fea;
    L_0x0f70:
        r0 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r6 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.messageNameTop = r6;
        r6 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.timeTop = r6;
        r6 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.errorTop = r6;
        r6 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.pinTop = r6;
        r6 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.countTop = r6;
        r6 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r1.checkDrawTop = r6;
        r6 = r33.getMeasuredWidth();
        r9 = 1119485952; // 0x42ba0000 float:93.0 double:5.5309955E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r6 = r6 - r9;
        r9 = org.telegram.messenger.LocaleController.isRTL;
        if (r9 != 0) goto L_0x0fc4;
    L_0x0fb5:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r17);
        r1.messageNameLeft = r9;
        r1.messageLeft = r9;
        r9 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        goto L_0x0fd9;
    L_0x0fc4:
        r9 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r1.messageNameLeft = r9;
        r1.messageLeft = r9;
        r9 = r33.getMeasuredWidth();
        r11 = 1115947008; // 0x42840000 float:66.0 double:5.51351079E-315;
        r11 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r9 = r9 - r11;
    L_0x0fd9:
        r11 = r1.avatarImage;
        r14 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r15 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r11.setImageCoords(r9, r0, r14, r15);
    L_0x0fea:
        r0 = r1.drawPin;
        if (r0 == 0) goto L_0x100f;
    L_0x0fee:
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 != 0) goto L_0x1007;
    L_0x0ff2:
        r0 = r33.getMeasuredWidth();
        r9 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedDrawable;
        r9 = r9.getIntrinsicWidth();
        r0 = r0 - r9;
        r9 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r0 = r0 - r9;
        r1.pinLeft = r0;
        goto L_0x100f;
    L_0x1007:
        r0 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.pinLeft = r0;
    L_0x100f:
        r0 = r1.drawError;
        if (r0 == 0) goto L_0x1041;
    L_0x1013:
        r0 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r6 = r6 - r0;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x102d;
    L_0x101e:
        r0 = r33.getMeasuredWidth();
        r3 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r1.errorLeft = r0;
        goto L_0x1166;
    L_0x102d:
        r3 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r1.errorLeft = r3;
        r3 = r1.messageLeft;
        r3 = r3 + r0;
        r1.messageLeft = r3;
        r3 = r1.messageNameLeft;
        r3 = r3 + r0;
        r1.messageNameLeft = r3;
        goto L_0x1166;
    L_0x1041:
        if (r3 != 0) goto L_0x106c;
    L_0x1043:
        if (r10 == 0) goto L_0x1046;
    L_0x1045:
        goto L_0x106c;
    L_0x1046:
        r0 = r1.drawPin;
        if (r0 == 0) goto L_0x1066;
    L_0x104a:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedDrawable;
        r0 = r0.getIntrinsicWidth();
        r3 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r3;
        r6 = r6 - r0;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 == 0) goto L_0x1066;
    L_0x105c:
        r3 = r1.messageLeft;
        r3 = r3 + r0;
        r1.messageLeft = r3;
        r3 = r1.messageNameLeft;
        r3 = r3 + r0;
        r1.messageNameLeft = r3;
    L_0x1066:
        r1.drawCount = r2;
        r1.drawMention = r2;
        goto L_0x1166;
    L_0x106c:
        if (r3 == 0) goto L_0x10d4;
    L_0x106e:
        r9 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r9 = org.telegram.p004ui.ActionBar.Theme.dialogs_countTextPaint;
        r9 = r9.measureText(r3);
        r14 = (double) r9;
        r14 = java.lang.Math.ceil(r14);
        r9 = (int) r14;
        r0 = java.lang.Math.max(r0, r9);
        r1.countWidth = r0;
        r0 = new android.text.StaticLayout;
        r24 = org.telegram.p004ui.ActionBar.Theme.dialogs_countTextPaint;
        r9 = r1.countWidth;
        r26 = android.text.Layout.Alignment.ALIGN_CENTER;
        r27 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r28 = 0;
        r29 = 0;
        r22 = r0;
        r23 = r3;
        r25 = r9;
        r22.<init>(r23, r24, r25, r26, r27, r28, r29);
        r1.countLayout = r0;
        r0 = r1.countWidth;
        r3 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r9;
        r6 = r6 - r0;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x10be;
    L_0x10ad:
        r0 = r33.getMeasuredWidth();
        r3 = r1.countWidth;
        r0 = r0 - r3;
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r9;
        r1.countLeft = r0;
        goto L_0x10d0;
    L_0x10be:
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r1.countLeft = r9;
        r3 = r1.messageLeft;
        r3 = r3 + r0;
        r1.messageLeft = r3;
        r3 = r1.messageNameLeft;
        r3 = r3 + r0;
        r1.messageNameLeft = r3;
    L_0x10d0:
        r3 = 1;
        r1.drawCount = r3;
        goto L_0x10d6;
    L_0x10d4:
        r1.countWidth = r2;
    L_0x10d6:
        if (r10 == 0) goto L_0x1166;
    L_0x10d8:
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x110e;
    L_0x10dc:
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = org.telegram.p004ui.ActionBar.Theme.dialogs_countTextPaint;
        r3 = r3.measureText(r10);
        r14 = (double) r3;
        r14 = java.lang.Math.ceil(r14);
        r3 = (int) r14;
        r0 = java.lang.Math.max(r0, r3);
        r1.mentionWidth = r0;
        r0 = new android.text.StaticLayout;
        r24 = org.telegram.p004ui.ActionBar.Theme.dialogs_countTextPaint;
        r3 = r1.mentionWidth;
        r26 = android.text.Layout.Alignment.ALIGN_CENTER;
        r27 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r28 = 0;
        r29 = 0;
        r22 = r0;
        r23 = r10;
        r25 = r3;
        r22.<init>(r23, r24, r25, r26, r27, r28, r29);
        r1.mentionLayout = r0;
        goto L_0x1116;
    L_0x110e:
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r1.mentionWidth = r0;
    L_0x1116:
        r0 = r1.mentionWidth;
        r3 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 + r9;
        r6 = r6 - r0;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x1143;
    L_0x1124:
        r0 = r33.getMeasuredWidth();
        r3 = r1.mentionWidth;
        r0 = r0 - r3;
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r0 = r0 - r3;
        r3 = r1.countWidth;
        if (r3 == 0) goto L_0x113e;
    L_0x1136:
        r9 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r3 = r3 + r9;
        goto L_0x113f;
    L_0x113e:
        r3 = 0;
    L_0x113f:
        r0 = r0 - r3;
        r1.mentionLeft = r0;
        goto L_0x1163;
    L_0x1143:
        r3 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r10 = r1.countWidth;
        if (r10 == 0) goto L_0x1155;
    L_0x114f:
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r9);
        r9 = r9 + r10;
        goto L_0x1156;
    L_0x1155:
        r9 = 0;
    L_0x1156:
        r3 = r3 + r9;
        r1.mentionLeft = r3;
        r3 = r1.messageLeft;
        r3 = r3 + r0;
        r1.messageLeft = r3;
        r3 = r1.messageNameLeft;
        r3 = r3 + r0;
        r1.messageNameLeft = r3;
    L_0x1163:
        r3 = 1;
        r1.drawMention = r3;
    L_0x1166:
        if (r4 == 0) goto L_0x119d;
    L_0x1168:
        if (r7 != 0) goto L_0x116b;
    L_0x116a:
        r7 = r12;
    L_0x116b:
        r0 = r7.toString();
        r3 = r0.length();
        r4 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r3 <= r4) goto L_0x117b;
    L_0x1177:
        r0 = r0.substring(r2, r4);
    L_0x117b:
        r3 = r1.useForceThreeLines;
        if (r3 != 0) goto L_0x1183;
    L_0x117f:
        r3 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r3 == 0) goto L_0x1185;
    L_0x1183:
        if (r8 == 0) goto L_0x118d;
    L_0x1185:
        r3 = 32;
        r4 = 10;
        r0 = r0.replace(r4, r3);
    L_0x118d:
        r3 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r3 = r3.getFontMetricsInt();
        r4 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r7 = org.telegram.messenger.Emoji.replaceEmoji(r0, r3, r4, r2);
    L_0x119d:
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = java.lang.Math.max(r0, r6);
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x11af;
    L_0x11ab:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x11e3;
    L_0x11af:
        if (r8 == 0) goto L_0x11e3;
    L_0x11b1:
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x11ba;
    L_0x11b5:
        r0 = r1.currentDialogFolderDialogsCount;
        r4 = 1;
        if (r0 != r4) goto L_0x11e3;
    L_0x11ba:
        r23 = org.telegram.p004ui.ActionBar.Theme.dialogs_messageNamePaint;	 Catch:{ Exception -> 0x11d5 }
        r25 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x11d5 }
        r26 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r27 = 0;
        r28 = 0;
        r29 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x11d5 }
        r31 = 1;
        r22 = r8;
        r24 = r3;
        r30 = r3;
        r0 = org.telegram.p004ui.Components.StaticLayoutEx.createStaticLayout(r22, r23, r24, r25, r26, r27, r28, r29, r30, r31);	 Catch:{ Exception -> 0x11d5 }
        r1.messageNameLayout = r0;	 Catch:{ Exception -> 0x11d5 }
        goto L_0x11d9;
    L_0x11d5:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x11d9:
        r0 = 1112276992; // 0x424c0000 float:51.0 double:5.495378504E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.messageTop = r0;
        r6 = 0;
        goto L_0x1200;
    L_0x11e3:
        r6 = 0;
        r1.messageNameLayout = r6;
        r0 = r1.useForceThreeLines;
        if (r0 != 0) goto L_0x11f8;
    L_0x11ea:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r0 == 0) goto L_0x11ef;
    L_0x11ee:
        goto L_0x11f8;
    L_0x11ef:
        r0 = 1109131264; // 0x421c0000 float:39.0 double:5.479836543E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.messageTop = r0;
        goto L_0x1200;
    L_0x11f8:
        r0 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r1.messageTop = r0;
    L_0x1200:
        r0 = r1.useForceThreeLines;	 Catch:{ Exception -> 0x127a }
        if (r0 != 0) goto L_0x1208;
    L_0x1204:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;	 Catch:{ Exception -> 0x127a }
        if (r0 == 0) goto L_0x1216;
    L_0x1208:
        r0 = r1.currentDialogFolderId;	 Catch:{ Exception -> 0x127a }
        if (r0 == 0) goto L_0x1216;
    L_0x120c:
        r0 = r1.currentDialogFolderDialogsCount;	 Catch:{ Exception -> 0x127a }
        r4 = 1;
        if (r0 <= r4) goto L_0x1217;
    L_0x1211:
        r13 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;	 Catch:{ Exception -> 0x127a }
        r0 = r8;
        r8 = r6;
        goto L_0x1232;
    L_0x1216:
        r4 = 1;
    L_0x1217:
        r0 = r1.useForceThreeLines;	 Catch:{ Exception -> 0x127a }
        if (r0 != 0) goto L_0x121f;
    L_0x121b:
        r0 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;	 Catch:{ Exception -> 0x127a }
        if (r0 == 0) goto L_0x1221;
    L_0x121f:
        if (r8 == 0) goto L_0x1231;
    L_0x1221:
        r6 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r6);	 Catch:{ Exception -> 0x127a }
        r0 = r3 - r0;
        r0 = (float) r0;	 Catch:{ Exception -> 0x127a }
        r6 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x127a }
        r0 = android.text.TextUtils.ellipsize(r7, r13, r0, r6);	 Catch:{ Exception -> 0x127a }
        goto L_0x1232;
    L_0x1231:
        r0 = r7;
    L_0x1232:
        r6 = r1.useForceThreeLines;	 Catch:{ Exception -> 0x127a }
        if (r6 != 0) goto L_0x1253;
    L_0x1236:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;	 Catch:{ Exception -> 0x127a }
        if (r6 == 0) goto L_0x123b;
    L_0x123a:
        goto L_0x1253;
    L_0x123b:
        r4 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x127a }
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x127a }
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r25 = 0;
        r26 = 0;
        r19 = r4;
        r20 = r0;
        r21 = r13;
        r22 = r3;
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x127a }
        r1.messageLayout = r4;	 Catch:{ Exception -> 0x127a }
        goto L_0x127e;
    L_0x1253:
        r22 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x127a }
        r23 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);	 Catch:{ Exception -> 0x127a }
        r6 = (float) r6;	 Catch:{ Exception -> 0x127a }
        r25 = 0;
        r26 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x127a }
        if (r8 == 0) goto L_0x1267;
    L_0x1264:
        r28 = 1;
        goto L_0x1269;
    L_0x1267:
        r28 = 2;
    L_0x1269:
        r19 = r0;
        r20 = r13;
        r21 = r3;
        r24 = r6;
        r27 = r3;
        r0 = org.telegram.p004ui.Components.StaticLayoutEx.createStaticLayout(r19, r20, r21, r22, r23, r24, r25, r26, r27, r28);	 Catch:{ Exception -> 0x127a }
        r1.messageLayout = r0;	 Catch:{ Exception -> 0x127a }
        goto L_0x127e;
    L_0x127a:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x127e:
        r0 = org.telegram.messenger.LocaleController.isRTL;
        if (r0 == 0) goto L_0x13b0;
    L_0x1282:
        r0 = r1.nameLayout;
        if (r0 == 0) goto L_0x133a;
    L_0x1286:
        r0 = r0.getLineCount();
        if (r0 <= 0) goto L_0x133a;
    L_0x128c:
        r0 = r1.nameLayout;
        r0 = r0.getLineLeft(r2);
        r4 = r1.nameLayout;
        r4 = r4.getLineWidth(r2);
        r6 = (double) r4;
        r6 = java.lang.Math.ceil(r6);
        r4 = r1.dialogMuted;
        if (r4 == 0) goto L_0x12cf;
    L_0x12a1:
        r4 = r1.drawVerified;
        if (r4 != 0) goto L_0x12cf;
    L_0x12a5:
        r4 = r1.drawScam;
        if (r4 != 0) goto L_0x12cf;
    L_0x12a9:
        r4 = r1.nameLeft;
        r8 = (double) r4;
        r10 = (double) r5;
        java.lang.Double.isNaN(r10);
        r10 = r10 - r6;
        java.lang.Double.isNaN(r8);
        r8 = r8 + r10;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = (double) r4;
        java.lang.Double.isNaN(r10);
        r8 = r8 - r10;
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_muteDrawable;
        r4 = r4.getIntrinsicWidth();
        r10 = (double) r4;
        java.lang.Double.isNaN(r10);
        r8 = r8 - r10;
        r4 = (int) r8;
        r1.nameMuteLeft = r4;
        goto L_0x1322;
    L_0x12cf:
        r4 = r1.drawVerified;
        if (r4 == 0) goto L_0x12f9;
    L_0x12d3:
        r4 = r1.nameLeft;
        r8 = (double) r4;
        r10 = (double) r5;
        java.lang.Double.isNaN(r10);
        r10 = r10 - r6;
        java.lang.Double.isNaN(r8);
        r8 = r8 + r10;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = (double) r4;
        java.lang.Double.isNaN(r10);
        r8 = r8 - r10;
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r4 = r4.getIntrinsicWidth();
        r10 = (double) r4;
        java.lang.Double.isNaN(r10);
        r8 = r8 - r10;
        r4 = (int) r8;
        r1.nameMuteLeft = r4;
        goto L_0x1322;
    L_0x12f9:
        r4 = r1.drawScam;
        if (r4 == 0) goto L_0x1322;
    L_0x12fd:
        r4 = r1.nameLeft;
        r8 = (double) r4;
        r10 = (double) r5;
        java.lang.Double.isNaN(r10);
        r10 = r10 - r6;
        java.lang.Double.isNaN(r8);
        r8 = r8 + r10;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r10 = (double) r4;
        java.lang.Double.isNaN(r10);
        r8 = r8 - r10;
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_scamDrawable;
        r4 = r4.getIntrinsicWidth();
        r10 = (double) r4;
        java.lang.Double.isNaN(r10);
        r8 = r8 - r10;
        r4 = (int) r8;
        r1.nameMuteLeft = r4;
    L_0x1322:
        r4 = 0;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 != 0) goto L_0x133a;
    L_0x1327:
        r4 = (double) r5;
        r0 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r0 >= 0) goto L_0x133a;
    L_0x132c:
        r0 = r1.nameLeft;
        r8 = (double) r0;
        java.lang.Double.isNaN(r4);
        r4 = r4 - r6;
        java.lang.Double.isNaN(r8);
        r8 = r8 + r4;
        r0 = (int) r8;
        r1.nameLeft = r0;
    L_0x133a:
        r0 = r1.messageLayout;
        if (r0 == 0) goto L_0x137b;
    L_0x133e:
        r0 = r0.getLineCount();
        if (r0 <= 0) goto L_0x137b;
    L_0x1344:
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r4 = 0;
        r5 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
    L_0x134b:
        if (r4 >= r0) goto L_0x1371;
    L_0x134d:
        r6 = r1.messageLayout;
        r6 = r6.getLineLeft(r4);
        r7 = 0;
        r6 = (r6 > r7 ? 1 : (r6 == r7 ? 0 : -1));
        if (r6 != 0) goto L_0x1370;
    L_0x1358:
        r6 = r1.messageLayout;
        r6 = r6.getLineWidth(r4);
        r6 = (double) r6;
        r6 = java.lang.Math.ceil(r6);
        r8 = (double) r3;
        java.lang.Double.isNaN(r8);
        r8 = r8 - r6;
        r6 = (int) r8;
        r5 = java.lang.Math.min(r5, r6);
        r4 = r4 + 1;
        goto L_0x134b;
    L_0x1370:
        r5 = 0;
    L_0x1371:
        r0 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r5 == r0) goto L_0x137b;
    L_0x1376:
        r0 = r1.messageLeft;
        r0 = r0 + r5;
        r1.messageLeft = r0;
    L_0x137b:
        r0 = r1.messageNameLayout;
        if (r0 == 0) goto L_0x143a;
    L_0x137f:
        r0 = r0.getLineCount();
        if (r0 <= 0) goto L_0x143a;
    L_0x1385:
        r0 = r1.messageNameLayout;
        r0 = r0.getLineLeft(r2);
        r4 = 0;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 != 0) goto L_0x143a;
    L_0x1390:
        r0 = r1.messageNameLayout;
        r0 = r0.getLineWidth(r2);
        r4 = (double) r0;
        r4 = java.lang.Math.ceil(r4);
        r2 = (double) r3;
        r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r0 >= 0) goto L_0x143a;
    L_0x13a0:
        r0 = r1.messageNameLeft;
        r6 = (double) r0;
        java.lang.Double.isNaN(r2);
        r2 = r2 - r4;
        java.lang.Double.isNaN(r6);
        r6 = r6 + r2;
        r0 = (int) r6;
        r1.messageNameLeft = r0;
        goto L_0x143a;
    L_0x13b0:
        r0 = r1.nameLayout;
        if (r0 == 0) goto L_0x13fe;
    L_0x13b4:
        r0 = r0.getLineCount();
        if (r0 <= 0) goto L_0x13fe;
    L_0x13ba:
        r0 = r1.nameLayout;
        r0 = r0.getLineRight(r2);
        r3 = (float) r5;
        r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r3 != 0) goto L_0x13e3;
    L_0x13c5:
        r3 = r1.nameLayout;
        r3 = r3.getLineWidth(r2);
        r3 = (double) r3;
        r3 = java.lang.Math.ceil(r3);
        r5 = (double) r5;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 >= 0) goto L_0x13e3;
    L_0x13d5:
        r7 = r1.nameLeft;
        r7 = (double) r7;
        java.lang.Double.isNaN(r5);
        r5 = r5 - r3;
        java.lang.Double.isNaN(r7);
        r7 = r7 - r5;
        r3 = (int) r7;
        r1.nameLeft = r3;
    L_0x13e3:
        r3 = r1.dialogMuted;
        if (r3 != 0) goto L_0x13ef;
    L_0x13e7:
        r3 = r1.drawVerified;
        if (r3 != 0) goto L_0x13ef;
    L_0x13eb:
        r3 = r1.drawScam;
        if (r3 == 0) goto L_0x13fe;
    L_0x13ef:
        r3 = r1.nameLeft;
        r3 = (float) r3;
        r3 = r3 + r0;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r0 = (float) r0;
        r3 = r3 + r0;
        r0 = (int) r3;
        r1.nameMuteLeft = r0;
    L_0x13fe:
        r0 = r1.messageLayout;
        if (r0 == 0) goto L_0x1423;
    L_0x1402:
        r0 = r0.getLineCount();
        if (r0 <= 0) goto L_0x1423;
    L_0x1408:
        r3 = 1325400064; // 0x4f000000 float:2.14748365E9 double:6.548346386E-315;
        r3 = 0;
        r4 = 1325400064; // 0x4f000000 float:2.14748365E9 double:6.548346386E-315;
    L_0x140d:
        if (r3 >= r0) goto L_0x141c;
    L_0x140f:
        r5 = r1.messageLayout;
        r5 = r5.getLineLeft(r3);
        r4 = java.lang.Math.min(r4, r5);
        r3 = r3 + 1;
        goto L_0x140d;
    L_0x141c:
        r0 = r1.messageLeft;
        r0 = (float) r0;
        r0 = r0 - r4;
        r0 = (int) r0;
        r1.messageLeft = r0;
    L_0x1423:
        r0 = r1.messageNameLayout;
        if (r0 == 0) goto L_0x143a;
    L_0x1427:
        r0 = r0.getLineCount();
        if (r0 <= 0) goto L_0x143a;
    L_0x142d:
        r0 = r1.messageNameLeft;
        r0 = (float) r0;
        r3 = r1.messageNameLayout;
        r2 = r3.getLineLeft(r2);
        r0 = r0 - r2;
        r0 = (int) r0;
        r1.messageNameLeft = r0;
    L_0x143a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.DialogCell.buildLayout():void");
    }

    public boolean isPointInsideAvatar(float f, float f2) {
        boolean z = true;
        if (LocaleController.isRTL) {
            if (f < ((float) (getMeasuredWidth() - AndroidUtilities.m26dp(60.0f))) || f >= ((float) getMeasuredWidth())) {
                z = false;
            }
            return z;
        }
        if (f < 0.0f || f >= ((float) AndroidUtilities.m26dp(60.0f))) {
            z = false;
        }
        return z;
    }

    public void setDialogSelected(boolean z) {
        if (this.isSelected != z) {
            invalidate();
        }
        this.isSelected = z;
    }

    public void checkCurrentDialogIndex(boolean z) {
        ArrayList dialogsArray = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, z);
        if (this.index < dialogsArray.size()) {
            MessageObject findFolderTopMessage;
            Dialog dialog = (Dialog) dialogsArray.get(this.index);
            boolean z2 = true;
            Dialog dialog2 = this.index + 1 < dialogsArray.size() ? (Dialog) dialogsArray.get(this.index + 1) : null;
            DraftMessage draft = DataQuery.getInstance(this.currentAccount).getDraft(this.currentDialogId);
            if (this.currentDialogFolderId != 0) {
                findFolderTopMessage = findFolderTopMessage();
            } else {
                findFolderTopMessage = (MessageObject) MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.f440id);
            }
            if (this.currentDialogId == dialog.f440id) {
                MessageObject messageObject = this.message;
                if ((messageObject == null || messageObject.getId() == dialog.top_message) && ((findFolderTopMessage == null || findFolderTopMessage.messageOwner.edit_date == this.currentEditDate) && this.unreadCount == dialog.unread_count && this.mentionCount == dialog.unread_mentions_count && this.markUnread == dialog.unread_mark)) {
                    messageObject = this.message;
                    if (messageObject == findFolderTopMessage && ((messageObject != null || findFolderTopMessage == null) && draft == this.draftMessage && this.drawPin == dialog.pinned)) {
                        return;
                    }
                }
            }
            Object obj = this.currentDialogId != dialog.f440id ? 1 : null;
            this.currentDialogId = dialog.f440id;
            boolean z3 = dialog instanceof TL_dialogFolder;
            if (z3) {
                this.currentDialogFolderId = ((TL_dialogFolder) dialog).folder.f485id;
            } else {
                this.currentDialogFolderId = 0;
            }
            boolean z4 = (dialog instanceof TL_dialog) && dialog.pinned && dialog2 != null && !dialog2.pinned;
            this.fullSeparator = z4;
            if (!z3 || dialog2 == null || dialog2.pinned) {
                z2 = false;
            }
            this.fullSeparator2 = z2;
            update(0);
            if (obj != null) {
                float f = (this.drawPin && this.drawReorder) ? 1.0f : 0.0f;
                this.reorderIconProgress = f;
            }
            checkOnline();
        }
    }

    public void animateArchiveAvatar() {
        if (this.avatarDrawable.getAvatarType() == 3) {
            this.animatingArchiveAvatar = true;
            this.animatingArchiveAvatarProgress = 0.0f;
            Theme.dialogs_archiveAvatarDrawable.setCallback(this);
            Theme.dialogs_archiveAvatarDrawable.setProgress(0.0f);
            Theme.dialogs_archiveAvatarDrawable.start();
            invalidate();
        }
    }

    public void setChecked(boolean z, boolean z2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            checkBox2.setChecked(z, z2);
        }
    }

    private MessageObject findFolderTopMessage() {
        int i = 0;
        ArrayList dialogsArray = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.currentDialogFolderId, false);
        MessageObject messageObject = null;
        if (!dialogsArray.isEmpty()) {
            int size = dialogsArray.size();
            while (i < size) {
                Dialog dialog = (Dialog) dialogsArray.get(i);
                MessageObject messageObject2 = (MessageObject) MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.f440id);
                if (messageObject2 != null && (messageObject == null || messageObject2.messageOwner.date > messageObject.messageOwner.date)) {
                    messageObject = messageObject2;
                }
                if (dialog.pinnedNum == 0) {
                    break;
                }
                i++;
            }
        }
        return messageObject;
    }

    /* JADX WARNING: Removed duplicated region for block: B:98:0x0177  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x01b2  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x01bf  */
    /* JADX WARNING: Missing block: B:52:0x0113, code skipped:
            if (r6.equals(r2) == false) goto L_0x0115;
     */
    public void update(int r20) {
        /*
        r19 = this;
        r0 = r19;
        r1 = r20;
        r2 = r0.customDialog;
        r3 = 0;
        r4 = 1;
        r5 = 0;
        if (r2 == 0) goto L_0x003c;
    L_0x000b:
        r1 = r2.date;
        r0.lastMessageDate = r1;
        r1 = r2.unread_count;
        if (r1 == 0) goto L_0x0014;
    L_0x0013:
        goto L_0x0015;
    L_0x0014:
        r4 = 0;
    L_0x0015:
        r0.lastUnreadState = r4;
        r1 = r0.customDialog;
        r2 = r1.unread_count;
        r0.unreadCount = r2;
        r2 = r1.pinned;
        r0.drawPin = r2;
        r2 = r1.muted;
        r0.dialogMuted = r2;
        r2 = r0.avatarDrawable;
        r4 = r1.f579id;
        r1 = r1.name;
        r2.setInfo(r4, r1, r3, r5);
        r6 = r0.avatarImage;
        r7 = 0;
        r9 = r0.avatarDrawable;
        r10 = 0;
        r11 = 0;
        r8 = "50_50";
        r6.setImage(r7, r8, r9, r10, r11);
        goto L_0x02fb;
    L_0x003c:
        r2 = r0.isDialogCell;
        if (r2 == 0) goto L_0x00c2;
    L_0x0040:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r2 = r2.dialogs_dict;
        r6 = r0.currentDialogId;
        r2 = r2.get(r6);
        r2 = (org.telegram.tgnet.TLRPC.Dialog) r2;
        if (r2 == 0) goto L_0x00b7;
    L_0x0052:
        if (r1 != 0) goto L_0x00c4;
    L_0x0054:
        r6 = r0.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r7 = r2.f440id;
        r6 = r6.isClearingDialog(r7);
        r0.clearingDialog = r6;
        r6 = r0.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r6 = r6.dialogMessage;
        r7 = r2.f440id;
        r6 = r6.get(r7);
        r6 = (org.telegram.messenger.MessageObject) r6;
        r0.message = r6;
        r6 = r0.message;
        if (r6 == 0) goto L_0x0080;
    L_0x0078:
        r6 = r6.isUnread();
        if (r6 == 0) goto L_0x0080;
    L_0x007e:
        r6 = 1;
        goto L_0x0081;
    L_0x0080:
        r6 = 0;
    L_0x0081:
        r0.lastUnreadState = r6;
        r6 = r2.unread_count;
        r0.unreadCount = r6;
        r6 = r2.unread_mark;
        r0.markUnread = r6;
        r6 = r2.unread_mentions_count;
        r0.mentionCount = r6;
        r6 = r0.message;
        if (r6 == 0) goto L_0x0098;
    L_0x0093:
        r6 = r6.messageOwner;
        r6 = r6.edit_date;
        goto L_0x0099;
    L_0x0098:
        r6 = 0;
    L_0x0099:
        r0.currentEditDate = r6;
        r6 = r2.last_message_date;
        r0.lastMessageDate = r6;
        r6 = r0.currentDialogFolderId;
        if (r6 != 0) goto L_0x00a9;
    L_0x00a3:
        r2 = r2.pinned;
        if (r2 == 0) goto L_0x00a9;
    L_0x00a7:
        r2 = 1;
        goto L_0x00aa;
    L_0x00a9:
        r2 = 0;
    L_0x00aa:
        r0.drawPin = r2;
        r2 = r0.message;
        if (r2 == 0) goto L_0x00c4;
    L_0x00b0:
        r2 = r2.messageOwner;
        r2 = r2.send_state;
        r0.lastSendState = r2;
        goto L_0x00c4;
    L_0x00b7:
        r0.unreadCount = r5;
        r0.mentionCount = r5;
        r0.currentEditDate = r5;
        r0.lastMessageDate = r5;
        r0.clearingDialog = r5;
        goto L_0x00c4;
    L_0x00c2:
        r0.drawPin = r5;
    L_0x00c4:
        if (r1 == 0) goto L_0x01c3;
    L_0x00c6:
        r2 = r0.user;
        if (r2 == 0) goto L_0x00e5;
    L_0x00ca:
        r2 = r1 & 4;
        if (r2 == 0) goto L_0x00e5;
    L_0x00ce:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r6 = r0.user;
        r6 = r6.f534id;
        r6 = java.lang.Integer.valueOf(r6);
        r2 = r2.getUser(r6);
        r0.user = r2;
        r19.invalidate();
    L_0x00e5:
        r2 = r0.isDialogCell;
        if (r2 == 0) goto L_0x0117;
    L_0x00e9:
        r2 = r1 & 64;
        if (r2 == 0) goto L_0x0117;
    L_0x00ed:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r2 = r2.printingStrings;
        r6 = r0.currentDialogId;
        r2 = r2.get(r6);
        r2 = (java.lang.CharSequence) r2;
        r6 = r0.lastPrintString;
        if (r6 == 0) goto L_0x0103;
    L_0x0101:
        if (r2 == 0) goto L_0x0115;
    L_0x0103:
        r6 = r0.lastPrintString;
        if (r6 != 0) goto L_0x0109;
    L_0x0107:
        if (r2 != 0) goto L_0x0115;
    L_0x0109:
        r6 = r0.lastPrintString;
        if (r6 == 0) goto L_0x0117;
    L_0x010d:
        if (r2 == 0) goto L_0x0117;
    L_0x010f:
        r2 = r6.equals(r2);
        if (r2 != 0) goto L_0x0117;
    L_0x0115:
        r2 = 1;
        goto L_0x0118;
    L_0x0117:
        r2 = 0;
    L_0x0118:
        if (r2 != 0) goto L_0x012b;
    L_0x011a:
        r6 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r6 = r6 & r1;
        if (r6 == 0) goto L_0x012b;
    L_0x0120:
        r6 = r0.message;
        if (r6 == 0) goto L_0x012b;
    L_0x0124:
        r6 = r6.messageText;
        r7 = r0.lastMessageString;
        if (r6 == r7) goto L_0x012b;
    L_0x012a:
        r2 = 1;
    L_0x012b:
        if (r2 != 0) goto L_0x0136;
    L_0x012d:
        r6 = r1 & 2;
        if (r6 == 0) goto L_0x0136;
    L_0x0131:
        r6 = r0.chat;
        if (r6 != 0) goto L_0x0136;
    L_0x0135:
        r2 = 1;
    L_0x0136:
        if (r2 != 0) goto L_0x0141;
    L_0x0138:
        r6 = r1 & 1;
        if (r6 == 0) goto L_0x0141;
    L_0x013c:
        r6 = r0.chat;
        if (r6 != 0) goto L_0x0141;
    L_0x0140:
        r2 = 1;
    L_0x0141:
        if (r2 != 0) goto L_0x014c;
    L_0x0143:
        r6 = r1 & 8;
        if (r6 == 0) goto L_0x014c;
    L_0x0147:
        r6 = r0.user;
        if (r6 != 0) goto L_0x014c;
    L_0x014b:
        r2 = 1;
    L_0x014c:
        if (r2 != 0) goto L_0x0157;
    L_0x014e:
        r6 = r1 & 16;
        if (r6 == 0) goto L_0x0157;
    L_0x0152:
        r6 = r0.user;
        if (r6 != 0) goto L_0x0157;
    L_0x0156:
        r2 = 1;
    L_0x0157:
        if (r2 != 0) goto L_0x01a8;
    L_0x0159:
        r6 = r1 & 256;
        if (r6 == 0) goto L_0x01a8;
    L_0x015d:
        r6 = r0.message;
        if (r6 == 0) goto L_0x0173;
    L_0x0161:
        r7 = r0.lastUnreadState;
        r6 = r6.isUnread();
        if (r7 == r6) goto L_0x0173;
    L_0x0169:
        r2 = r0.message;
        r2 = r2.isUnread();
        r0.lastUnreadState = r2;
    L_0x0171:
        r2 = 1;
        goto L_0x01a8;
    L_0x0173:
        r6 = r0.isDialogCell;
        if (r6 == 0) goto L_0x01a8;
    L_0x0177:
        r6 = r0.currentAccount;
        r6 = org.telegram.messenger.MessagesController.getInstance(r6);
        r6 = r6.dialogs_dict;
        r7 = r0.currentDialogId;
        r6 = r6.get(r7);
        r6 = (org.telegram.tgnet.TLRPC.Dialog) r6;
        if (r6 == 0) goto L_0x01a8;
    L_0x0189:
        r7 = r0.unreadCount;
        r8 = r6.unread_count;
        if (r7 != r8) goto L_0x019b;
    L_0x018f:
        r7 = r0.markUnread;
        r8 = r6.unread_mark;
        if (r7 != r8) goto L_0x019b;
    L_0x0195:
        r7 = r0.mentionCount;
        r8 = r6.unread_mentions_count;
        if (r7 == r8) goto L_0x01a8;
    L_0x019b:
        r2 = r6.unread_count;
        r0.unreadCount = r2;
        r2 = r6.unread_mentions_count;
        r0.mentionCount = r2;
        r2 = r6.unread_mark;
        r0.markUnread = r2;
        goto L_0x0171;
    L_0x01a8:
        if (r2 != 0) goto L_0x01bd;
    L_0x01aa:
        r1 = r1 & 4096;
        if (r1 == 0) goto L_0x01bd;
    L_0x01ae:
        r1 = r0.message;
        if (r1 == 0) goto L_0x01bd;
    L_0x01b2:
        r6 = r0.lastSendState;
        r1 = r1.messageOwner;
        r1 = r1.send_state;
        if (r6 == r1) goto L_0x01bd;
    L_0x01ba:
        r0.lastSendState = r1;
        r2 = 1;
    L_0x01bd:
        if (r2 != 0) goto L_0x01c3;
    L_0x01bf:
        r19.invalidate();
        return;
    L_0x01c3:
        r0.user = r3;
        r0.chat = r3;
        r0.encryptedChat = r3;
        r1 = r0.currentDialogFolderId;
        r2 = 0;
        if (r1 == 0) goto L_0x01e2;
    L_0x01cf:
        r0.dialogMuted = r5;
        r1 = r19.findFolderTopMessage();
        r0.message = r1;
        r1 = r0.message;
        if (r1 == 0) goto L_0x01e0;
    L_0x01db:
        r6 = r1.getDialogId();
        goto L_0x01fb;
    L_0x01e0:
        r6 = r2;
        goto L_0x01fb;
    L_0x01e2:
        r1 = r0.isDialogCell;
        if (r1 == 0) goto L_0x01f6;
    L_0x01e6:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r6 = r0.currentDialogId;
        r1 = r1.isDialogMuted(r6);
        if (r1 == 0) goto L_0x01f6;
    L_0x01f4:
        r1 = 1;
        goto L_0x01f7;
    L_0x01f6:
        r1 = 0;
    L_0x01f7:
        r0.dialogMuted = r1;
        r6 = r0.currentDialogId;
    L_0x01fb:
        r1 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r1 == 0) goto L_0x028b;
    L_0x01ff:
        r1 = (int) r6;
        r2 = 32;
        r2 = r6 >> r2;
        r3 = (int) r2;
        if (r1 == 0) goto L_0x0263;
    L_0x0207:
        if (r3 != r4) goto L_0x021a;
    L_0x0209:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r1 = java.lang.Integer.valueOf(r1);
        r1 = r2.getChat(r1);
        r0.chat = r1;
        goto L_0x028b;
    L_0x021a:
        if (r1 >= 0) goto L_0x0252;
    L_0x021c:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r1 = -r1;
        r1 = java.lang.Integer.valueOf(r1);
        r1 = r2.getChat(r1);
        r0.chat = r1;
        r1 = r0.isDialogCell;
        if (r1 != 0) goto L_0x028b;
    L_0x0231:
        r1 = r0.chat;
        if (r1 == 0) goto L_0x028b;
    L_0x0235:
        r1 = r1.migrated_to;
        if (r1 == 0) goto L_0x028b;
    L_0x0239:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = r0.chat;
        r2 = r2.migrated_to;
        r2 = r2.channel_id;
        r2 = java.lang.Integer.valueOf(r2);
        r1 = r1.getChat(r2);
        if (r1 == 0) goto L_0x028b;
    L_0x024f:
        r0.chat = r1;
        goto L_0x028b;
    L_0x0252:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r1 = java.lang.Integer.valueOf(r1);
        r1 = r2.getUser(r1);
        r0.user = r1;
        goto L_0x028b;
    L_0x0263:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = java.lang.Integer.valueOf(r3);
        r1 = r1.getEncryptedChat(r2);
        r0.encryptedChat = r1;
        r1 = r0.encryptedChat;
        if (r1 == 0) goto L_0x028b;
    L_0x0277:
        r1 = r0.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = r0.encryptedChat;
        r2 = r2.user_id;
        r2 = java.lang.Integer.valueOf(r2);
        r1 = r1.getUser(r2);
        r0.user = r1;
    L_0x028b:
        r1 = r0.currentDialogFolderId;
        if (r1 == 0) goto L_0x02a3;
    L_0x028f:
        r1 = r0.avatarDrawable;
        r2 = 3;
        r1.setAvatarType(r2);
        r3 = r0.avatarImage;
        r4 = 0;
        r5 = 0;
        r6 = r0.avatarDrawable;
        r7 = 0;
        r8 = r0.user;
        r9 = 0;
        r3.setImage(r4, r5, r6, r7, r8, r9);
        goto L_0x02fb;
    L_0x02a3:
        r1 = r0.user;
        if (r1 == 0) goto L_0x02df;
    L_0x02a7:
        r2 = r0.avatarDrawable;
        r2.setInfo(r1);
        r1 = r0.user;
        r1 = org.telegram.messenger.UserObject.isUserSelf(r1);
        if (r1 == 0) goto L_0x02c7;
    L_0x02b4:
        r1 = r0.avatarDrawable;
        r1.setAvatarType(r4);
        r5 = r0.avatarImage;
        r6 = 0;
        r7 = 0;
        r8 = r0.avatarDrawable;
        r9 = 0;
        r10 = r0.user;
        r11 = 0;
        r5.setImage(r6, r7, r8, r9, r10, r11);
        goto L_0x02fb;
    L_0x02c7:
        r12 = r0.avatarImage;
        r1 = r0.user;
        r13 = org.telegram.messenger.ImageLocation.getForUser(r1, r5);
        r15 = r0.avatarDrawable;
        r16 = 0;
        r1 = r0.user;
        r18 = 0;
        r14 = "50_50";
        r17 = r1;
        r12.setImage(r13, r14, r15, r16, r17, r18);
        goto L_0x02fb;
    L_0x02df:
        r1 = r0.chat;
        if (r1 == 0) goto L_0x02fb;
    L_0x02e3:
        r2 = r0.avatarDrawable;
        r2.setInfo(r1);
        r6 = r0.avatarImage;
        r1 = r0.chat;
        r7 = org.telegram.messenger.ImageLocation.getForChat(r1, r5);
        r9 = r0.avatarDrawable;
        r10 = 0;
        r11 = r0.chat;
        r12 = 0;
        r8 = "50_50";
        r6.setImage(r7, r8, r9, r10, r11, r12);
    L_0x02fb:
        r1 = r19.getMeasuredWidth();
        if (r1 != 0) goto L_0x030c;
    L_0x0301:
        r1 = r19.getMeasuredHeight();
        if (r1 == 0) goto L_0x0308;
    L_0x0307:
        goto L_0x030c;
    L_0x0308:
        r19.requestLayout();
        goto L_0x030f;
    L_0x030c:
        r19.buildLayout();
    L_0x030f:
        r19.invalidate();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.DialogCell.update(int):void");
    }

    public float getTranslationX() {
        return this.translationX;
    }

    public void setTranslationX(float f) {
        this.translationX = (float) ((int) f);
        Drawable drawable = this.translationDrawable;
        boolean z = false;
        if (drawable != null && this.translationX == 0.0f) {
            if (drawable instanceof LottieDrawable) {
                ((LottieDrawable) drawable).setProgress(0.0f);
            }
            this.translationAnimationStarted = false;
            this.archiveHidden = SharedConfig.archiveHidden;
            this.currentRevealProgress = 0.0f;
            this.isSliding = false;
        }
        if (this.translationX != 0.0f) {
            this.isSliding = true;
        }
        if (this.isSliding) {
            boolean z2 = this.drawRevealBackground;
            if (Math.abs(this.translationX) >= ((float) getMeasuredWidth()) * 0.3f) {
                z = true;
            }
            this.drawRevealBackground = z;
            if (z2 != this.drawRevealBackground && this.archiveHidden == SharedConfig.archiveHidden) {
                try {
                    performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
            }
        }
        invalidate();
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:441:0x0b12  */
    /* JADX WARNING: Removed duplicated region for block: B:455:0x0b47  */
    /* JADX WARNING: Removed duplicated region for block: B:455:0x0b47  */
    /* JADX WARNING: Removed duplicated region for block: B:455:0x0b47  */
    /* JADX WARNING: Removed duplicated region for block: B:427:0x0ae0  */
    /* JADX WARNING: Removed duplicated region for block: B:444:0x0b22  */
    /* JADX WARNING: Removed duplicated region for block: B:433:0x0af6  */
    /* JADX WARNING: Removed duplicated region for block: B:455:0x0b47  */
    /* JADX WARNING: Removed duplicated region for block: B:427:0x0ae0  */
    /* JADX WARNING: Removed duplicated region for block: B:433:0x0af6  */
    /* JADX WARNING: Removed duplicated region for block: B:444:0x0b22  */
    /* JADX WARNING: Removed duplicated region for block: B:455:0x0b47  */
    /* JADX WARNING: Removed duplicated region for block: B:416:0x0ab8  */
    /* JADX WARNING: Removed duplicated region for block: B:408:0x0a94  */
    /* JADX WARNING: Removed duplicated region for block: B:427:0x0ae0  */
    /* JADX WARNING: Removed duplicated region for block: B:444:0x0b22  */
    /* JADX WARNING: Removed duplicated region for block: B:433:0x0af6  */
    /* JADX WARNING: Removed duplicated region for block: B:455:0x0b47  */
    /* JADX WARNING: Removed duplicated region for block: B:383:0x0a0d  */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0a7b  */
    /* JADX WARNING: Removed duplicated region for block: B:393:0x0a65  */
    /* JADX WARNING: Removed duplicated region for block: B:336:0x0919  */
    /* JADX WARNING: Removed duplicated region for block: B:329:0x08ff  */
    /* JADX WARNING: Removed duplicated region for block: B:350:0x0980  */
    /* JADX WARNING: Removed duplicated region for block: B:345:0x096c  */
    /* JADX WARNING: Removed duplicated region for block: B:359:0x099e  */
    /* JADX WARNING: Removed duplicated region for block: B:362:0x09a5  */
    /* JADX WARNING: Removed duplicated region for block: B:383:0x0a0d  */
    /* JADX WARNING: Removed duplicated region for block: B:393:0x0a65  */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0a7b  */
    /* JADX WARNING: Removed duplicated region for block: B:359:0x099e  */
    /* JADX WARNING: Removed duplicated region for block: B:362:0x09a5  */
    /* JADX WARNING: Removed duplicated region for block: B:383:0x0a0d  */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0a7b  */
    /* JADX WARNING: Removed duplicated region for block: B:393:0x0a65  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x045f  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x0450  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x049b  */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x0519  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x0567  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x05b5  */
    /* JADX WARNING: Removed duplicated region for block: B:245:0x0674  */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x0637  */
    /* JADX WARNING: Removed duplicated region for block: B:262:0x0713  */
    /* JADX WARNING: Removed duplicated region for block: B:261:0x06c3  */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x0868  */
    /* JADX WARNING: Removed duplicated region for block: B:297:0x088d  */
    /* JADX WARNING: Removed duplicated region for block: B:308:0x08a8  */
    /* JADX WARNING: Removed duplicated region for block: B:359:0x099e  */
    /* JADX WARNING: Removed duplicated region for block: B:362:0x09a5  */
    /* JADX WARNING: Removed duplicated region for block: B:383:0x0a0d  */
    /* JADX WARNING: Removed duplicated region for block: B:393:0x0a65  */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0a7b  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x0450  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x045f  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x049b  */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x0519  */
    /* JADX WARNING: Removed duplicated region for block: B:189:0x0567  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x05b5  */
    /* JADX WARNING: Removed duplicated region for block: B:215:0x05ff  */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x0637  */
    /* JADX WARNING: Removed duplicated region for block: B:245:0x0674  */
    /* JADX WARNING: Removed duplicated region for block: B:261:0x06c3  */
    /* JADX WARNING: Removed duplicated region for block: B:262:0x0713  */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x0868  */
    /* JADX WARNING: Removed duplicated region for block: B:297:0x088d  */
    /* JADX WARNING: Removed duplicated region for block: B:308:0x08a8  */
    /* JADX WARNING: Removed duplicated region for block: B:359:0x099e  */
    /* JADX WARNING: Removed duplicated region for block: B:362:0x09a5  */
    /* JADX WARNING: Removed duplicated region for block: B:383:0x0a0d  */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0a7b  */
    /* JADX WARNING: Removed duplicated region for block: B:393:0x0a65  */
    @android.annotation.SuppressLint({"DrawAllocation"})
    public void onDraw(android.graphics.Canvas r24) {
        /*
        r23 = this;
        r1 = r23;
        r8 = r24;
        r2 = r1.currentDialogId;
        r4 = 0;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 != 0) goto L_0x0011;
    L_0x000c:
        r0 = r1.customDialog;
        if (r0 != 0) goto L_0x0011;
    L_0x0010:
        return;
    L_0x0011:
        r2 = android.os.SystemClock.uptimeMillis();
        r4 = r1.lastUpdateTime;
        r4 = r2 - r4;
        r6 = 17;
        r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r0 <= 0) goto L_0x0021;
    L_0x001f:
        r4 = 17;
    L_0x0021:
        r9 = r4;
        r1.lastUpdateTime = r2;
        r0 = r1.clipProgress;
        r11 = 0;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 == 0) goto L_0x0051;
    L_0x002b:
        r0 = android.os.Build.VERSION.SDK_INT;
        r2 = 24;
        if (r0 == r2) goto L_0x0051;
    L_0x0031:
        r24.save();
        r0 = r1.topClip;
        r0 = (float) r0;
        r2 = r1.clipProgress;
        r0 = r0 * r2;
        r2 = r23.getMeasuredWidth();
        r2 = (float) r2;
        r3 = r23.getMeasuredHeight();
        r4 = r1.bottomClip;
        r4 = (float) r4;
        r5 = r1.clipProgress;
        r4 = r4 * r5;
        r4 = (int) r4;
        r3 = r3 - r4;
        r3 = (float) r3;
        r8.clipRect(r11, r0, r2, r3);
    L_0x0051:
        r0 = r1.translationX;
        r12 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r13 = 2;
        r14 = 0;
        r15 = 1;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 != 0) goto L_0x0082;
    L_0x005e:
        r0 = r1.cornerProgress;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 == 0) goto L_0x0065;
    L_0x0064:
        goto L_0x0082;
    L_0x0065:
        r0 = r1.translationDrawable;
        if (r0 == 0) goto L_0x007e;
    L_0x0069:
        r2 = r0 instanceof com.airbnb.lottie.LottieDrawable;
        if (r2 == 0) goto L_0x0079;
    L_0x006d:
        r0 = (com.airbnb.lottie.LottieDrawable) r0;
        r0.stop();
        r0.setProgress(r11);
        r2 = 0;
        r0.setCallback(r2);
    L_0x0079:
        r0 = 0;
        r1.translationDrawable = r0;
        r1.translationAnimationStarted = r14;
    L_0x007e:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x02d4;
    L_0x0082:
        r24.save();
        r0 = r1.currentDialogFolderId;
        r16 = "chats_archiveBackground";
        r17 = "chats_archivePinBackground";
        if (r0 == 0) goto L_0x00bd;
    L_0x008d:
        r0 = r1.archiveHidden;
        if (r0 == 0) goto L_0x00a7;
    L_0x0091:
        r0 = org.telegram.p004ui.ActionBar.Theme.getColor(r17);
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r16);
        r3 = 2131560936; // 0x7f0d09e8 float:1.8747258E38 double:1.0531310305E-314;
        r4 = "UnhideFromTop";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_unpinArchiveDrawable;
        r1.translationDrawable = r4;
        goto L_0x00ec;
    L_0x00a7:
        r0 = org.telegram.p004ui.ActionBar.Theme.getColor(r16);
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r17);
        r3 = 2131559637; // 0x7f0d04d5 float:1.8744624E38 double:1.0531303887E-314;
        r4 = "HideOnTop";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinArchiveDrawable;
        r1.translationDrawable = r4;
        goto L_0x00ec;
    L_0x00bd:
        r0 = r1.folderId;
        if (r0 != 0) goto L_0x00d7;
    L_0x00c1:
        r0 = org.telegram.p004ui.ActionBar.Theme.getColor(r16);
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r17);
        r3 = 2131558642; // 0x7f0d00f2 float:1.8742606E38 double:1.053129897E-314;
        r4 = "Archive";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_archiveDrawable;
        r1.translationDrawable = r4;
        goto L_0x00ec;
    L_0x00d7:
        r0 = org.telegram.p004ui.ActionBar.Theme.getColor(r17);
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r16);
        r3 = 2131560928; // 0x7f0d09e0 float:1.8747242E38 double:1.0531310265E-314;
        r4 = "Unarchive";
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_unarchiveDrawable;
        r1.translationDrawable = r4;
    L_0x00ec:
        r6 = r2;
        r5 = r3;
        r2 = r1.translationAnimationStarted;
        r18 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        if (r2 != 0) goto L_0x0116;
    L_0x00f4:
        r2 = r1.translationX;
        r2 = java.lang.Math.abs(r2);
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r3 = (float) r3;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x0116;
    L_0x0103:
        r1.translationAnimationStarted = r15;
        r2 = r1.translationDrawable;
        r3 = r2 instanceof com.airbnb.lottie.LottieDrawable;
        if (r3 == 0) goto L_0x0116;
    L_0x010b:
        r2 = (com.airbnb.lottie.LottieDrawable) r2;
        r2.setProgress(r11);
        r2.setCallback(r1);
        r2.start();
    L_0x0116:
        r2 = r23.getMeasuredWidth();
        r2 = (float) r2;
        r3 = r1.translationX;
        r4 = r2 + r3;
        r2 = r1.currentRevealProgress;
        r2 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1));
        if (r2 >= 0) goto L_0x0187;
    L_0x0125:
        r2 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2.setColor(r0);
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r0 = (float) r0;
        r3 = r4 - r0;
        r0 = 0;
        r2 = r23.getMeasuredWidth();
        r2 = (float) r2;
        r7 = r23.getMeasuredHeight();
        r7 = (float) r7;
        r19 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r20 = r2;
        r2 = r24;
        r21 = r4;
        r4 = r0;
        r0 = r5;
        r5 = r20;
        r22 = r6;
        r6 = r7;
        r7 = r19;
        r2.drawRect(r3, r4, r5, r6, r7);
        r2 = r1.currentRevealProgress;
        r2 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1));
        if (r2 != 0) goto L_0x018c;
    L_0x0156:
        r2 = org.telegram.p004ui.ActionBar.Theme.dialogs_archiveDrawableRecolored;
        if (r2 == 0) goto L_0x018c;
    L_0x015a:
        r2 = org.telegram.p004ui.ActionBar.Theme.dialogs_archiveDrawable;
        r3 = r2 instanceof com.airbnb.lottie.LottieDrawable;
        if (r3 == 0) goto L_0x0184;
    L_0x0160:
        r2 = (com.airbnb.lottie.LottieDrawable) r2;
        r3 = new com.airbnb.lottie.model.KeyPath;
        r4 = new java.lang.String[r13];
        r5 = "Arrow";
        r4[r14] = r5;
        r5 = "**";
        r4[r15] = r5;
        r3.<init>(r4);
        r4 = com.airbnb.lottie.LottieProperty.COLOR_FILTER;
        r5 = new com.airbnb.lottie.value.LottieValueCallback;
        r6 = new com.airbnb.lottie.SimpleColorFilter;
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r16);
        r6.<init>(r7);
        r5.<init>(r6);
        r2.addValueCallback(r3, r4, r5);
    L_0x0184:
        org.telegram.p004ui.ActionBar.Theme.dialogs_archiveDrawableRecolored = r14;
        goto L_0x018c;
    L_0x0187:
        r21 = r4;
        r0 = r5;
        r22 = r6;
    L_0x018c:
        r2 = r23.getMeasuredWidth();
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r2 = r2 - r3;
        r3 = r1.translationDrawable;
        r3 = r3.getIntrinsicWidth();
        r3 = r3 / r13;
        r2 = r2 - r3;
        r3 = r1.useForceThreeLines;
        if (r3 != 0) goto L_0x01a9;
    L_0x01a1:
        r3 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r3 == 0) goto L_0x01a6;
    L_0x01a5:
        goto L_0x01a9;
    L_0x01a6:
        r3 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x01ab;
    L_0x01a9:
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
    L_0x01ab:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r4 = r1.translationDrawable;
        r4 = r4 instanceof com.airbnb.lottie.LottieDrawable;
        if (r4 != 0) goto L_0x01bc;
    L_0x01b5:
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r4);
        r3 = r3 + r4;
    L_0x01bc:
        r4 = r1.translationDrawable;
        r4 = r4.getIntrinsicWidth();
        r4 = r4 / r13;
        r4 = r4 + r2;
        r5 = r1.translationDrawable;
        r5 = r5.getIntrinsicHeight();
        r5 = r5 / r13;
        r5 = r5 + r3;
        r6 = r1.currentRevealProgress;
        r6 = (r6 > r11 ? 1 : (r6 == r11 ? 0 : -1));
        if (r6 <= 0) goto L_0x0251;
    L_0x01d2:
        r24.save();
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r6 = (float) r6;
        r7 = r21;
        r6 = r7 - r6;
        r12 = r23.getMeasuredWidth();
        r12 = (float) r12;
        r15 = r23.getMeasuredHeight();
        r15 = (float) r15;
        r8.clipRect(r6, r11, r12, r15);
        r6 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r12 = r22;
        r6.setColor(r12);
        r6 = r4 * r4;
        r12 = r23.getMeasuredHeight();
        r12 = r5 - r12;
        r15 = r23.getMeasuredHeight();
        r15 = r5 - r15;
        r12 = r12 * r15;
        r6 = r6 + r12;
        r11 = (double) r6;
        r11 = java.lang.Math.sqrt(r11);
        r6 = (float) r11;
        r4 = (float) r4;
        r5 = (float) r5;
        r11 = org.telegram.messenger.AndroidUtilities.accelerateInterpolator;
        r12 = r1.currentRevealProgress;
        r11 = r11.getInterpolation(r12);
        r6 = r6 * r11;
        r11 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r8.drawCircle(r4, r5, r6, r11);
        r24.restore();
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_archiveDrawableRecolored;
        if (r4 != 0) goto L_0x0253;
    L_0x0221:
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_archiveDrawable;
        r5 = r4 instanceof com.airbnb.lottie.LottieDrawable;
        if (r5 == 0) goto L_0x024d;
    L_0x0227:
        r4 = (com.airbnb.lottie.LottieDrawable) r4;
        r5 = new com.airbnb.lottie.model.KeyPath;
        r6 = new java.lang.String[r13];
        r11 = "Arrow";
        r6[r14] = r11;
        r11 = "**";
        r12 = 1;
        r6[r12] = r11;
        r5.<init>(r6);
        r6 = com.airbnb.lottie.LottieProperty.COLOR_FILTER;
        r11 = new com.airbnb.lottie.value.LottieValueCallback;
        r15 = new com.airbnb.lottie.SimpleColorFilter;
        r14 = org.telegram.p004ui.ActionBar.Theme.getColor(r17);
        r15.<init>(r14);
        r11.<init>(r15);
        r4.addValueCallback(r5, r6, r11);
        goto L_0x024e;
    L_0x024d:
        r12 = 1;
    L_0x024e:
        org.telegram.p004ui.ActionBar.Theme.dialogs_archiveDrawableRecolored = r12;
        goto L_0x0253;
    L_0x0251:
        r7 = r21;
    L_0x0253:
        r24.save();
        r2 = (float) r2;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.currentRevealBounceProgress;
        r3 = 0;
        r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r4 == 0) goto L_0x0283;
    L_0x0262:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1));
        if (r3 == 0) goto L_0x0285;
    L_0x0268:
        r3 = r1.interpolator;
        r2 = r3.getInterpolation(r2);
        r2 = r2 + r11;
        r3 = r1.translationDrawable;
        r3 = r3.getIntrinsicWidth();
        r3 = r3 / r13;
        r3 = (float) r3;
        r4 = r1.translationDrawable;
        r4 = r4.getIntrinsicHeight();
        r4 = r4 / r13;
        r4 = (float) r4;
        r8.scale(r2, r2, r3, r4);
        goto L_0x0285;
    L_0x0283:
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0285:
        r2 = r1.translationDrawable;
        r3 = 0;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r2, r3, r3);
        r2 = r1.translationDrawable;
        r2.draw(r8);
        r24.restore();
        r2 = r23.getMeasuredWidth();
        r2 = (float) r2;
        r3 = r23.getMeasuredHeight();
        r3 = (float) r3;
        r4 = 0;
        r8.clipRect(r7, r4, r2, r3);
        r2 = org.telegram.p004ui.ActionBar.Theme.dialogs_countTextPaint;
        r2 = r2.measureText(r0);
        r2 = (double) r2;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r3 = r23.getMeasuredWidth();
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r18);
        r3 = r3 - r4;
        r2 = r2 / r13;
        r3 = r3 - r2;
        r2 = (float) r3;
        r3 = r1.useForceThreeLines;
        if (r3 != 0) goto L_0x02c5;
    L_0x02bd:
        r3 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r3 == 0) goto L_0x02c2;
    L_0x02c1:
        goto L_0x02c5;
    L_0x02c2:
        r3 = 1114374144; // 0x426c0000 float:59.0 double:5.50573981E-315;
        goto L_0x02c7;
    L_0x02c5:
        r3 = 1115160576; // 0x42780000 float:62.0 double:5.5096253E-315;
    L_0x02c7:
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r3 = (float) r3;
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_archiveTextPaint;
        r8.drawText(r0, r2, r3, r4);
        r24.restore();
    L_0x02d4:
        r0 = r1.translationX;
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x02e3;
    L_0x02db:
        r24.save();
        r0 = r1.translationX;
        r8.translate(r0, r2);
    L_0x02e3:
        r0 = r1.isSelected;
        if (r0 == 0) goto L_0x02fa;
    L_0x02e7:
        r3 = 0;
        r4 = 0;
        r0 = r23.getMeasuredWidth();
        r5 = (float) r0;
        r0 = r23.getMeasuredHeight();
        r6 = (float) r0;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_tabletSeletedPaint;
        r2 = r24;
        r2.drawRect(r3, r4, r5, r6, r7);
    L_0x02fa:
        r0 = r1.currentDialogFolderId;
        r12 = "chats_pinnedOverlay";
        if (r0 == 0) goto L_0x032f;
    L_0x0300:
        r0 = org.telegram.messenger.SharedConfig.archiveHidden;
        if (r0 == 0) goto L_0x030b;
    L_0x0304:
        r0 = r1.archiveBackgroundProgress;
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x032f;
    L_0x030b:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r12);
        r3 = r1.archiveBackgroundProgress;
        r4 = 0;
        r2 = org.telegram.messenger.AndroidUtilities.getOffsetColor(r4, r2, r3, r11);
        r0.setColor(r2);
        r3 = 0;
        r4 = 0;
        r0 = r23.getMeasuredWidth();
        r5 = (float) r0;
        r0 = r23.getMeasuredHeight();
        r6 = (float) r0;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = r24;
        r2.drawRect(r3, r4, r5, r6, r7);
        goto L_0x0353;
    L_0x032f:
        r0 = r1.drawPin;
        if (r0 != 0) goto L_0x0337;
    L_0x0333:
        r0 = r1.drawPinBackground;
        if (r0 == 0) goto L_0x0353;
    L_0x0337:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r12);
        r0.setColor(r2);
        r3 = 0;
        r4 = 0;
        r0 = r23.getMeasuredWidth();
        r5 = (float) r0;
        r0 = r23.getMeasuredHeight();
        r6 = (float) r0;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = r24;
        r2.drawRect(r3, r4, r5, r6, r7);
    L_0x0353:
        r0 = r1.translationX;
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 != 0) goto L_0x0365;
    L_0x035a:
        r0 = r1.cornerProgress;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x0361;
    L_0x0360:
        goto L_0x0365;
    L_0x0361:
        r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        goto L_0x0418;
    L_0x0365:
        r24.save();
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = "windowBackgroundWhite";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r0.setColor(r2);
        r0 = r1.rect;
        r2 = r23.getMeasuredWidth();
        r3 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r3);
        r2 = r2 - r3;
        r2 = (float) r2;
        r3 = r23.getMeasuredWidth();
        r3 = (float) r3;
        r4 = r23.getMeasuredHeight();
        r4 = (float) r4;
        r5 = 0;
        r0.set(r2, r5, r3, r4);
        r0 = r1.rect;
        r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = (float) r3;
        r4 = r1.cornerProgress;
        r3 = r3 * r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r4;
        r4 = r1.cornerProgress;
        r2 = r2 * r4;
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r8.drawRoundRect(r0, r3, r2, r4);
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x03e5;
    L_0x03ae:
        r0 = org.telegram.messenger.SharedConfig.archiveHidden;
        if (r0 == 0) goto L_0x03b9;
    L_0x03b2:
        r0 = r1.archiveBackgroundProgress;
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x03e5;
    L_0x03b9:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r12);
        r3 = r1.archiveBackgroundProgress;
        r4 = 0;
        r2 = org.telegram.messenger.AndroidUtilities.getOffsetColor(r4, r2, r3, r11);
        r0.setColor(r2);
        r0 = r1.rect;
        r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = (float) r3;
        r4 = r1.cornerProgress;
        r3 = r3 * r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r2 = (float) r4;
        r4 = r1.cornerProgress;
        r2 = r2 * r4;
        r4 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r8.drawRoundRect(r0, r3, r2, r4);
        goto L_0x03ee;
    L_0x03e5:
        r0 = r1.drawPin;
        if (r0 != 0) goto L_0x03f1;
    L_0x03e9:
        r0 = r1.drawPinBackground;
        if (r0 == 0) goto L_0x03ee;
    L_0x03ed:
        goto L_0x03f1;
    L_0x03ee:
        r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        goto L_0x0415;
    L_0x03f1:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r12);
        r0.setColor(r2);
        r0 = r1.rect;
        r2 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r3 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r3 = (float) r3;
        r4 = r1.cornerProgress;
        r3 = r3 * r4;
        r4 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r4 = (float) r4;
        r5 = r1.cornerProgress;
        r4 = r4 * r5;
        r5 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r8.drawRoundRect(r0, r3, r4, r5);
    L_0x0415:
        r24.restore();
    L_0x0418:
        r0 = r1.translationX;
        r3 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r4 = 0;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x0435;
    L_0x0421:
        r0 = r1.cornerProgress;
        r4 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r4 >= 0) goto L_0x044b;
    L_0x0427:
        r4 = (float) r9;
        r4 = r4 / r3;
        r0 = r0 + r4;
        r1.cornerProgress = r0;
        r0 = r1.cornerProgress;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 <= 0) goto L_0x0449;
    L_0x0432:
        r1.cornerProgress = r11;
        goto L_0x0449;
    L_0x0435:
        r0 = r1.cornerProgress;
        r4 = 0;
        r5 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r5 <= 0) goto L_0x044b;
    L_0x043c:
        r5 = (float) r9;
        r5 = r5 / r3;
        r0 = r0 - r5;
        r1.cornerProgress = r0;
        r0 = r1.cornerProgress;
        r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r0 >= 0) goto L_0x0449;
    L_0x0447:
        r1.cornerProgress = r4;
    L_0x0449:
        r4 = 1;
        goto L_0x044c;
    L_0x044b:
        r4 = 0;
    L_0x044c:
        r0 = r1.drawNameLock;
        if (r0 == 0) goto L_0x045f;
    L_0x0450:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r5 = r1.nameLockLeft;
        r6 = r1.nameLockTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_lockDrawable;
        r0.draw(r8);
        goto L_0x0497;
    L_0x045f:
        r0 = r1.drawNameGroup;
        if (r0 == 0) goto L_0x0472;
    L_0x0463:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        r5 = r1.nameLockLeft;
        r6 = r1.nameLockTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_groupDrawable;
        r0.draw(r8);
        goto L_0x0497;
    L_0x0472:
        r0 = r1.drawNameBroadcast;
        if (r0 == 0) goto L_0x0485;
    L_0x0476:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
        r5 = r1.nameLockLeft;
        r6 = r1.nameLockTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_broadcastDrawable;
        r0.draw(r8);
        goto L_0x0497;
    L_0x0485:
        r0 = r1.drawNameBot;
        if (r0 == 0) goto L_0x0497;
    L_0x0489:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_botDrawable;
        r5 = r1.nameLockLeft;
        r6 = r1.nameLockTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_botDrawable;
        r0.draw(r8);
    L_0x0497:
        r0 = r1.nameLayout;
        if (r0 == 0) goto L_0x04f9;
    L_0x049b:
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x04ad;
    L_0x049f:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_namePaint;
        r5 = "chats_nameArchived";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
        goto L_0x04d5;
    L_0x04ad:
        r0 = r1.encryptedChat;
        if (r0 != 0) goto L_0x04c8;
    L_0x04b1:
        r0 = r1.customDialog;
        if (r0 == 0) goto L_0x04ba;
    L_0x04b5:
        r0 = r0.type;
        if (r0 != r13) goto L_0x04ba;
    L_0x04b9:
        goto L_0x04c8;
    L_0x04ba:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_namePaint;
        r5 = "chats_name";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
        goto L_0x04d5;
    L_0x04c8:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_namePaint;
        r5 = "chats_secretName";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
    L_0x04d5:
        r24.save();
        r0 = r1.nameLeft;
        r0 = (float) r0;
        r5 = r1.useForceThreeLines;
        if (r5 != 0) goto L_0x04e7;
    L_0x04df:
        r5 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r5 == 0) goto L_0x04e4;
    L_0x04e3:
        goto L_0x04e7;
    L_0x04e4:
        r5 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        goto L_0x04e9;
    L_0x04e7:
        r5 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
    L_0x04e9:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r5 = (float) r5;
        r8.translate(r0, r5);
        r0 = r1.nameLayout;
        r0.draw(r8);
        r24.restore();
    L_0x04f9:
        r0 = r1.timeLayout;
        if (r0 == 0) goto L_0x0515;
    L_0x04fd:
        r0 = r1.currentDialogFolderId;
        if (r0 != 0) goto L_0x0515;
    L_0x0501:
        r24.save();
        r0 = r1.timeLeft;
        r0 = (float) r0;
        r5 = r1.timeTop;
        r5 = (float) r5;
        r8.translate(r0, r5);
        r0 = r1.timeLayout;
        r0.draw(r8);
        r24.restore();
    L_0x0515:
        r0 = r1.messageNameLayout;
        if (r0 == 0) goto L_0x0563;
    L_0x0519:
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x052b;
    L_0x051d:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messageNamePaint;
        r5 = "chats_nameMessageArchived_threeLines";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
        goto L_0x054a;
    L_0x052b:
        r0 = r1.draftMessage;
        if (r0 == 0) goto L_0x053d;
    L_0x052f:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messageNamePaint;
        r5 = "chats_draft";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
        goto L_0x054a;
    L_0x053d:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messageNamePaint;
        r5 = "chats_nameMessage_threeLines";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
    L_0x054a:
        r24.save();
        r0 = r1.messageNameLeft;
        r0 = (float) r0;
        r5 = r1.messageNameTop;
        r5 = (float) r5;
        r8.translate(r0, r5);
        r0 = r1.messageNameLayout;	 Catch:{ Exception -> 0x055c }
        r0.draw(r8);	 Catch:{ Exception -> 0x055c }
        goto L_0x0560;
    L_0x055c:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0560:
        r24.restore();
    L_0x0563:
        r0 = r1.messageLayout;
        if (r0 == 0) goto L_0x05b1;
    L_0x0567:
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x058b;
    L_0x056b:
        r0 = r1.chat;
        if (r0 == 0) goto L_0x057d;
    L_0x056f:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r5 = "chats_nameMessageArchived";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
        goto L_0x0598;
    L_0x057d:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r5 = "chats_messageArchived";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
        goto L_0x0598;
    L_0x058b:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_messagePaint;
        r5 = "chats_message";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);
        r0.linkColor = r5;
        r0.setColor(r5);
    L_0x0598:
        r24.save();
        r0 = r1.messageLeft;
        r0 = (float) r0;
        r5 = r1.messageTop;
        r5 = (float) r5;
        r8.translate(r0, r5);
        r0 = r1.messageLayout;	 Catch:{ Exception -> 0x05aa }
        r0.draw(r8);	 Catch:{ Exception -> 0x05aa }
        goto L_0x05ae;
    L_0x05aa:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x05ae:
        r24.restore();
    L_0x05b1:
        r0 = r1.currentDialogFolderId;
        if (r0 != 0) goto L_0x05fb;
    L_0x05b5:
        r0 = r1.drawClock;
        if (r0 == 0) goto L_0x05c8;
    L_0x05b9:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_clockDrawable;
        r5 = r1.checkDrawLeft;
        r6 = r1.checkDrawTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_clockDrawable;
        r0.draw(r8);
        goto L_0x05fb;
    L_0x05c8:
        r0 = r1.drawCheck2;
        if (r0 == 0) goto L_0x05fb;
    L_0x05cc:
        r0 = r1.drawCheck1;
        if (r0 == 0) goto L_0x05ed;
    L_0x05d0:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_halfCheckDrawable;
        r5 = r1.halfCheckDrawLeft;
        r6 = r1.checkDrawTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_halfCheckDrawable;
        r0.draw(r8);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_checkDrawable;
        r5 = r1.checkDrawLeft;
        r6 = r1.checkDrawTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_checkDrawable;
        r0.draw(r8);
        goto L_0x05fb;
    L_0x05ed:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_checkDrawable;
        r5 = r1.checkDrawLeft;
        r6 = r1.checkDrawTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_checkDrawable;
        r0.draw(r8);
    L_0x05fb:
        r0 = r1.dialogMuted;
        if (r0 == 0) goto L_0x0633;
    L_0x05ff:
        r0 = r1.drawVerified;
        if (r0 != 0) goto L_0x0633;
    L_0x0603:
        r0 = r1.drawScam;
        if (r0 != 0) goto L_0x0633;
    L_0x0607:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_muteDrawable;
        r5 = r1.nameMuteLeft;
        r6 = r1.useForceThreeLines;
        if (r6 != 0) goto L_0x0617;
    L_0x060f:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x0614;
    L_0x0613:
        goto L_0x0617;
    L_0x0614:
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x0618;
    L_0x0617:
        r6 = 0;
    L_0x0618:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x0624;
    L_0x0621:
        r6 = 1096286208; // 0x41580000 float:13.5 double:5.416373534E-315;
        goto L_0x0626;
    L_0x0624:
        r6 = 1099694080; // 0x418c0000 float:17.5 double:5.43321066E-315;
    L_0x0626:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_muteDrawable;
        r0.draw(r8);
        goto L_0x0696;
    L_0x0633:
        r0 = r1.drawVerified;
        if (r0 == 0) goto L_0x0674;
    L_0x0637:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r5 = r1.nameMuteLeft;
        r6 = r1.useForceThreeLines;
        if (r6 != 0) goto L_0x0647;
    L_0x063f:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x0644;
    L_0x0643:
        goto L_0x0647;
    L_0x0644:
        r6 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        goto L_0x0649;
    L_0x0647:
        r6 = 1095237632; // 0x41480000 float:12.5 double:5.41119288E-315;
    L_0x0649:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_verifiedCheckDrawable;
        r5 = r1.nameMuteLeft;
        r6 = r1.useForceThreeLines;
        if (r6 != 0) goto L_0x0660;
    L_0x0658:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x065d;
    L_0x065c:
        goto L_0x0660;
    L_0x065d:
        r6 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        goto L_0x0662;
    L_0x0660:
        r6 = 1095237632; // 0x41480000 float:12.5 double:5.41119288E-315;
    L_0x0662:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r0.draw(r8);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_verifiedCheckDrawable;
        r0.draw(r8);
        goto L_0x0696;
    L_0x0674:
        r0 = r1.drawScam;
        if (r0 == 0) goto L_0x0696;
    L_0x0678:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_scamDrawable;
        r5 = r1.nameMuteLeft;
        r6 = r1.useForceThreeLines;
        if (r6 != 0) goto L_0x0688;
    L_0x0680:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x0685;
    L_0x0684:
        goto L_0x0688;
    L_0x0685:
        r6 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        goto L_0x068a;
    L_0x0688:
        r6 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
    L_0x068a:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_scamDrawable;
        r0.draw(r8);
    L_0x0696:
        r0 = r1.drawReorder;
        r5 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        if (r0 != 0) goto L_0x06a3;
    L_0x069c:
        r0 = r1.reorderIconProgress;
        r6 = 0;
        r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r0 == 0) goto L_0x06bb;
    L_0x06a3:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_reorderDrawable;
        r6 = r1.reorderIconProgress;
        r6 = r6 * r5;
        r6 = (int) r6;
        r0.setAlpha(r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_reorderDrawable;
        r6 = r1.pinLeft;
        r7 = r1.pinTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r6, r7);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_reorderDrawable;
        r0.draw(r8);
    L_0x06bb:
        r0 = r1.drawError;
        r6 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
        r7 = 1094189056; // 0x41380000 float:11.5 double:5.406012226E-315;
        if (r0 == 0) goto L_0x0713;
    L_0x06c3:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_errorDrawable;
        r12 = r1.reorderIconProgress;
        r12 = r11 - r12;
        r12 = r12 * r5;
        r5 = (int) r12;
        r0.setAlpha(r5);
        r0 = r1.rect;
        r5 = r1.errorLeft;
        r12 = (float) r5;
        r13 = r1.errorTop;
        r13 = (float) r13;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 + r14;
        r5 = (float) r5;
        r14 = r1.errorTop;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = r14 + r6;
        r6 = (float) r14;
        r0.set(r12, r13, r5, r6);
        r0 = r1.rect;
        r5 = org.telegram.messenger.AndroidUtilities.density;
        r6 = r5 * r7;
        r5 = r5 * r7;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_errorPaint;
        r8.drawRoundRect(r0, r6, r5, r7);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_errorDrawable;
        r5 = r1.errorLeft;
        r6 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 + r6;
        r6 = r1.errorTop;
        r7 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        r6 = r6 + r7;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_errorDrawable;
        r0.draw(r8);
        goto L_0x0862;
    L_0x0713:
        r0 = r1.drawCount;
        if (r0 != 0) goto L_0x073c;
    L_0x0717:
        r0 = r1.drawMention;
        if (r0 == 0) goto L_0x071c;
    L_0x071b:
        goto L_0x073c;
    L_0x071c:
        r0 = r1.drawPin;
        if (r0 == 0) goto L_0x0862;
    L_0x0720:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedDrawable;
        r6 = r1.reorderIconProgress;
        r7 = r11 - r6;
        r7 = r7 * r5;
        r5 = (int) r7;
        r0.setAlpha(r5);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedDrawable;
        r5 = r1.pinLeft;
        r6 = r1.pinTop;
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r5, r6);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedDrawable;
        r0.draw(r8);
        goto L_0x0862;
    L_0x073c:
        r0 = r1.drawCount;
        if (r0 == 0) goto L_0x07b4;
    L_0x0740:
        r0 = r1.dialogMuted;
        if (r0 != 0) goto L_0x074c;
    L_0x0744:
        r0 = r1.currentDialogFolderId;
        if (r0 == 0) goto L_0x0749;
    L_0x0748:
        goto L_0x074c;
    L_0x0749:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_countPaint;
        goto L_0x074e;
    L_0x074c:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_countGrayPaint;
    L_0x074e:
        r12 = r1.reorderIconProgress;
        r12 = r11 - r12;
        r12 = r12 * r5;
        r12 = (int) r12;
        r0.setAlpha(r12);
        r12 = org.telegram.p004ui.ActionBar.Theme.dialogs_countTextPaint;
        r13 = r1.reorderIconProgress;
        r13 = r11 - r13;
        r13 = r13 * r5;
        r13 = (int) r13;
        r12.setAlpha(r13);
        r12 = r1.countLeft;
        r13 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r13);
        r12 = r12 - r13;
        r13 = r1.rect;
        r14 = (float) r12;
        r2 = r1.countTop;
        r2 = (float) r2;
        r15 = r1.countWidth;
        r12 = r12 + r15;
        r15 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r15 = org.telegram.messenger.AndroidUtilities.m26dp(r15);
        r12 = r12 + r15;
        r12 = (float) r12;
        r15 = r1.countTop;
        r17 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r15 = r15 + r17;
        r15 = (float) r15;
        r13.set(r14, r2, r12, r15);
        r2 = r1.rect;
        r12 = org.telegram.messenger.AndroidUtilities.density;
        r13 = r12 * r7;
        r12 = r12 * r7;
        r8.drawRoundRect(r2, r13, r12, r0);
        r0 = r1.countLayout;
        if (r0 == 0) goto L_0x07b4;
    L_0x0799:
        r24.save();
        r0 = r1.countLeft;
        r0 = (float) r0;
        r2 = r1.countTop;
        r12 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r12 = org.telegram.messenger.AndroidUtilities.m26dp(r12);
        r2 = r2 + r12;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.countLayout;
        r0.draw(r8);
        r24.restore();
    L_0x07b4:
        r0 = r1.drawMention;
        if (r0 == 0) goto L_0x0862;
    L_0x07b8:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_countPaint;
        r2 = r1.reorderIconProgress;
        r2 = r11 - r2;
        r2 = r2 * r5;
        r2 = (int) r2;
        r0.setAlpha(r2);
        r0 = r1.mentionLeft;
        r2 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r2 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        r0 = r0 - r2;
        r2 = r1.rect;
        r12 = (float) r0;
        r13 = r1.countTop;
        r13 = (float) r13;
        r14 = r1.mentionWidth;
        r0 = r0 + r14;
        r14 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r14);
        r0 = r0 + r14;
        r0 = (float) r0;
        r14 = r1.countTop;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r14 = r14 + r6;
        r6 = (float) r14;
        r2.set(r12, r13, r0, r6);
        r0 = r1.dialogMuted;
        if (r0 == 0) goto L_0x07f4;
    L_0x07ed:
        r0 = r1.folderId;
        if (r0 == 0) goto L_0x07f4;
    L_0x07f1:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_countGrayPaint;
        goto L_0x07f6;
    L_0x07f4:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_countPaint;
    L_0x07f6:
        r2 = r1.rect;
        r6 = org.telegram.messenger.AndroidUtilities.density;
        r12 = r6 * r7;
        r6 = r6 * r7;
        r8.drawRoundRect(r2, r12, r6, r0);
        r0 = r1.mentionLayout;
        if (r0 == 0) goto L_0x082d;
    L_0x0805:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_countTextPaint;
        r2 = r1.reorderIconProgress;
        r7 = r11 - r2;
        r7 = r7 * r5;
        r2 = (int) r7;
        r0.setAlpha(r2);
        r24.save();
        r0 = r1.mentionLeft;
        r0 = (float) r0;
        r2 = r1.countTop;
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r2 = r2 + r5;
        r2 = (float) r2;
        r8.translate(r0, r2);
        r0 = r1.mentionLayout;
        r0.draw(r8);
        r24.restore();
        goto L_0x0862;
    L_0x082d:
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_mentionDrawable;
        r2 = r1.reorderIconProgress;
        r7 = r11 - r2;
        r7 = r7 * r5;
        r2 = (int) r7;
        r0.setAlpha(r2);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_mentionDrawable;
        r2 = r1.mentionLeft;
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r5);
        r2 = r2 - r5;
        r5 = r1.countTop;
        r6 = 1078774989; // 0x404ccccd float:3.2 double:5.329856617E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 + r6;
        r6 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r7 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r7);
        org.telegram.p004ui.Cells.BaseCell.setDrawableBounds(r0, r2, r5, r6, r7);
        r0 = org.telegram.p004ui.ActionBar.Theme.dialogs_mentionDrawable;
        r0.draw(r8);
    L_0x0862:
        r0 = r1.animatingArchiveAvatar;
        r12 = 1126825984; // 0x432a0000 float:170.0 double:5.567260075E-315;
        if (r0 == 0) goto L_0x0884;
    L_0x0868:
        r24.save();
        r0 = r1.interpolator;
        r2 = r1.animatingArchiveAvatarProgress;
        r2 = r2 / r12;
        r0 = r0.getInterpolation(r2);
        r0 = r0 + r11;
        r2 = r1.avatarImage;
        r2 = r2.getCenterX();
        r5 = r1.avatarImage;
        r5 = r5.getCenterY();
        r8.scale(r0, r0, r2, r5);
    L_0x0884:
        r0 = r1.avatarImage;
        r0.draw(r8);
        r0 = r1.animatingArchiveAvatar;
        if (r0 == 0) goto L_0x0890;
    L_0x088d:
        r24.restore();
    L_0x0890:
        r0 = r1.user;
        if (r0 == 0) goto L_0x0996;
    L_0x0894:
        r2 = r1.isDialogCell;
        if (r2 == 0) goto L_0x0996;
    L_0x0898:
        r2 = r1.currentDialogFolderId;
        if (r2 != 0) goto L_0x0996;
    L_0x089c:
        r0 = org.telegram.messenger.MessagesController.isSupportUser(r0);
        if (r0 != 0) goto L_0x0996;
    L_0x08a2:
        r0 = r1.user;
        r2 = r0.bot;
        if (r2 != 0) goto L_0x0996;
    L_0x08a8:
        r2 = r0.self;
        if (r2 != 0) goto L_0x08d6;
    L_0x08ac:
        r0 = r0.status;
        if (r0 == 0) goto L_0x08be;
    L_0x08b0:
        r0 = r0.expires;
        r2 = r1.currentAccount;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);
        r2 = r2.getCurrentTime();
        if (r0 > r2) goto L_0x08d4;
    L_0x08be:
        r0 = r1.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r0 = r0.onlinePrivacy;
        r2 = r1.user;
        r2 = r2.f534id;
        r2 = java.lang.Integer.valueOf(r2);
        r0 = r0.containsKey(r2);
        if (r0 == 0) goto L_0x08d6;
    L_0x08d4:
        r0 = 1;
        goto L_0x08d7;
    L_0x08d6:
        r0 = 0;
    L_0x08d7:
        if (r0 != 0) goto L_0x08e0;
    L_0x08d9:
        r2 = r1.onlineProgress;
        r5 = 0;
        r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1));
        if (r2 == 0) goto L_0x0996;
    L_0x08e0:
        r2 = r1.avatarImage;
        r2 = r2.getImageY2();
        r5 = r1.useForceThreeLines;
        if (r5 != 0) goto L_0x08f2;
    L_0x08ea:
        r5 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r5 == 0) goto L_0x08ef;
    L_0x08ee:
        goto L_0x08f2;
    L_0x08ef:
        r16 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        goto L_0x08f6;
    L_0x08f2:
        r5 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r16 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
    L_0x08f6:
        r5 = org.telegram.messenger.AndroidUtilities.m26dp(r16);
        r2 = r2 - r5;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 == 0) goto L_0x0919;
    L_0x08ff:
        r5 = r1.avatarImage;
        r5 = r5.getImageX();
        r6 = r1.useForceThreeLines;
        if (r6 != 0) goto L_0x0911;
    L_0x0909:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x090e;
    L_0x090d:
        goto L_0x0911;
    L_0x090e:
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        goto L_0x0913;
    L_0x0911:
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
    L_0x0913:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 + r6;
        goto L_0x0932;
    L_0x0919:
        r5 = r1.avatarImage;
        r5 = r5.getImageX2();
        r6 = r1.useForceThreeLines;
        if (r6 != 0) goto L_0x092b;
    L_0x0923:
        r6 = org.telegram.messenger.SharedConfig.useThreeLinesLayout;
        if (r6 == 0) goto L_0x0928;
    L_0x0927:
        goto L_0x092b;
    L_0x0928:
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        goto L_0x092d;
    L_0x092b:
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
    L_0x092d:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r5 = r5 - r6;
    L_0x0932:
        r6 = org.telegram.p004ui.ActionBar.Theme.dialogs_onlineCirclePaint;
        r7 = "windowBackgroundWhite";
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r7);
        r6.setColor(r7);
        r5 = (float) r5;
        r2 = (float) r2;
        r6 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r6 = (float) r6;
        r7 = r1.onlineProgress;
        r6 = r6 * r7;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_onlineCirclePaint;
        r8.drawCircle(r5, r2, r6, r7);
        r6 = org.telegram.p004ui.ActionBar.Theme.dialogs_onlineCirclePaint;
        r7 = "chats_onlineCircle";
        r7 = org.telegram.p004ui.ActionBar.Theme.getColor(r7);
        r6.setColor(r7);
        r6 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r6);
        r6 = (float) r6;
        r7 = r1.onlineProgress;
        r6 = r6 * r7;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_onlineCirclePaint;
        r8.drawCircle(r5, r2, r6, r7);
        if (r0 == 0) goto L_0x0980;
    L_0x096c:
        r0 = r1.onlineProgress;
        r2 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r2 >= 0) goto L_0x0996;
    L_0x0972:
        r2 = (float) r9;
        r2 = r2 / r3;
        r0 = r0 + r2;
        r1.onlineProgress = r0;
        r0 = r1.onlineProgress;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 <= 0) goto L_0x0994;
    L_0x097d:
        r1.onlineProgress = r11;
        goto L_0x0994;
    L_0x0980:
        r0 = r1.onlineProgress;
        r2 = 0;
        r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r5 <= 0) goto L_0x0996;
    L_0x0987:
        r4 = (float) r9;
        r4 = r4 / r3;
        r0 = r0 - r4;
        r1.onlineProgress = r0;
        r0 = r1.onlineProgress;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 >= 0) goto L_0x0994;
    L_0x0992:
        r1.onlineProgress = r2;
    L_0x0994:
        r0 = 1;
        goto L_0x0997;
    L_0x0996:
        r0 = r4;
    L_0x0997:
        r2 = r1.translationX;
        r3 = 0;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 == 0) goto L_0x09a1;
    L_0x099e:
        r24.restore();
    L_0x09a1:
        r2 = r1.useSeparator;
        if (r2 == 0) goto L_0x0a05;
    L_0x09a5:
        r2 = r1.fullSeparator;
        if (r2 != 0) goto L_0x09c5;
    L_0x09a9:
        r2 = r1.currentDialogFolderId;
        if (r2 == 0) goto L_0x09b5;
    L_0x09ad:
        r2 = r1.archiveHidden;
        if (r2 == 0) goto L_0x09b5;
    L_0x09b1:
        r2 = r1.fullSeparator2;
        if (r2 == 0) goto L_0x09c5;
    L_0x09b5:
        r2 = r1.fullSeparator2;
        if (r2 == 0) goto L_0x09be;
    L_0x09b9:
        r2 = r1.archiveHidden;
        if (r2 != 0) goto L_0x09be;
    L_0x09bd:
        goto L_0x09c5;
    L_0x09be:
        r2 = 1116733440; // 0x42900000 float:72.0 double:5.517396283E-315;
        r14 = org.telegram.messenger.AndroidUtilities.m26dp(r2);
        goto L_0x09c6;
    L_0x09c5:
        r14 = 0;
    L_0x09c6:
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 == 0) goto L_0x09ea;
    L_0x09ca:
        r3 = 0;
        r2 = r23.getMeasuredHeight();
        r4 = 1;
        r2 = r2 - r4;
        r5 = (float) r2;
        r2 = r23.getMeasuredWidth();
        r2 = r2 - r14;
        r6 = (float) r2;
        r2 = r23.getMeasuredHeight();
        r2 = r2 - r4;
        r7 = (float) r2;
        r13 = org.telegram.p004ui.ActionBar.Theme.dividerPaint;
        r2 = r24;
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r13;
        r2.drawLine(r3, r4, r5, r6, r7);
        goto L_0x0a05;
    L_0x09ea:
        r3 = (float) r14;
        r2 = r23.getMeasuredHeight();
        r13 = 1;
        r2 = r2 - r13;
        r4 = (float) r2;
        r2 = r23.getMeasuredWidth();
        r5 = (float) r2;
        r2 = r23.getMeasuredHeight();
        r2 = r2 - r13;
        r6 = (float) r2;
        r7 = org.telegram.p004ui.ActionBar.Theme.dividerPaint;
        r2 = r24;
        r2.drawLine(r3, r4, r5, r6, r7);
        goto L_0x0a06;
    L_0x0a05:
        r13 = 1;
    L_0x0a06:
        r2 = r1.clipProgress;
        r3 = 0;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 == 0) goto L_0x0a56;
    L_0x0a0d:
        r2 = android.os.Build.VERSION.SDK_INT;
        r3 = 24;
        if (r2 == r3) goto L_0x0a17;
    L_0x0a13:
        r24.restore();
        goto L_0x0a56;
    L_0x0a17:
        r2 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r3 = "windowBackgroundWhite";
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r3 = 0;
        r4 = 0;
        r2 = r23.getMeasuredWidth();
        r5 = (float) r2;
        r2 = r1.topClip;
        r2 = (float) r2;
        r6 = r1.clipProgress;
        r6 = r6 * r2;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = r24;
        r2.drawRect(r3, r4, r5, r6, r7);
        r2 = r23.getMeasuredHeight();
        r4 = r1.bottomClip;
        r4 = (float) r4;
        r5 = r1.clipProgress;
        r4 = r4 * r5;
        r4 = (int) r4;
        r2 = r2 - r4;
        r4 = (float) r2;
        r2 = r23.getMeasuredWidth();
        r5 = (float) r2;
        r2 = r23.getMeasuredHeight();
        r6 = (float) r2;
        r7 = org.telegram.p004ui.ActionBar.Theme.dialogs_pinnedPaint;
        r2 = r24;
        r2.drawRect(r3, r4, r5, r6, r7);
    L_0x0a56:
        r2 = r1.drawReorder;
        if (r2 != 0) goto L_0x0a61;
    L_0x0a5a:
        r2 = r1.reorderIconProgress;
        r3 = 0;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 == 0) goto L_0x0a79;
    L_0x0a61:
        r2 = r1.drawReorder;
        if (r2 == 0) goto L_0x0a7b;
    L_0x0a65:
        r2 = r1.reorderIconProgress;
        r3 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1));
        if (r3 >= 0) goto L_0x0a79;
    L_0x0a6b:
        r0 = (float) r9;
        r0 = r0 / r12;
        r2 = r2 + r0;
        r1.reorderIconProgress = r2;
        r0 = r1.reorderIconProgress;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 <= 0) goto L_0x0a78;
    L_0x0a76:
        r1.reorderIconProgress = r11;
    L_0x0a78:
        r0 = 1;
    L_0x0a79:
        r3 = 0;
        goto L_0x0a90;
    L_0x0a7b:
        r2 = r1.reorderIconProgress;
        r3 = 0;
        r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r4 <= 0) goto L_0x0a90;
    L_0x0a82:
        r0 = (float) r9;
        r0 = r0 / r12;
        r2 = r2 - r0;
        r1.reorderIconProgress = r2;
        r0 = r1.reorderIconProgress;
        r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r0 >= 0) goto L_0x0a8f;
    L_0x0a8d:
        r1.reorderIconProgress = r3;
    L_0x0a8f:
        r0 = 1;
    L_0x0a90:
        r2 = r1.archiveHidden;
        if (r2 == 0) goto L_0x0ab8;
    L_0x0a94:
        r2 = r1.archiveBackgroundProgress;
        r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r4 <= 0) goto L_0x0adc;
    L_0x0a9a:
        r0 = (float) r9;
        r0 = r0 / r12;
        r2 = r2 - r0;
        r1.archiveBackgroundProgress = r2;
        r0 = r1.currentRevealBounceProgress;
        r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        if (r0 >= 0) goto L_0x0aa7;
    L_0x0aa5:
        r1.currentRevealBounceProgress = r3;
    L_0x0aa7:
        r0 = r1.avatarDrawable;
        r0 = r0.getAvatarType();
        r2 = 3;
        if (r0 != r2) goto L_0x0adb;
    L_0x0ab0:
        r0 = r1.avatarDrawable;
        r2 = r1.archiveBackgroundProgress;
        r0.setArchivedAvatarHiddenProgress(r2);
        goto L_0x0adb;
    L_0x0ab8:
        r2 = r1.archiveBackgroundProgress;
        r3 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1));
        if (r3 >= 0) goto L_0x0adc;
    L_0x0abe:
        r0 = (float) r9;
        r0 = r0 / r12;
        r2 = r2 + r0;
        r1.archiveBackgroundProgress = r2;
        r0 = r1.currentRevealBounceProgress;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 <= 0) goto L_0x0acb;
    L_0x0ac9:
        r1.currentRevealBounceProgress = r11;
    L_0x0acb:
        r0 = r1.avatarDrawable;
        r0 = r0.getAvatarType();
        r2 = 3;
        if (r0 != r2) goto L_0x0adb;
    L_0x0ad4:
        r0 = r1.avatarDrawable;
        r2 = r1.archiveBackgroundProgress;
        r0.setArchivedAvatarHiddenProgress(r2);
    L_0x0adb:
        r0 = 1;
    L_0x0adc:
        r2 = r1.animatingArchiveAvatar;
        if (r2 == 0) goto L_0x0af2;
    L_0x0ae0:
        r0 = r1.animatingArchiveAvatarProgress;
        r2 = (float) r9;
        r0 = r0 + r2;
        r1.animatingArchiveAvatarProgress = r0;
        r0 = r1.animatingArchiveAvatarProgress;
        r0 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1));
        if (r0 < 0) goto L_0x0af1;
    L_0x0aec:
        r1.animatingArchiveAvatarProgress = r12;
        r2 = 0;
        r1.animatingArchiveAvatar = r2;
    L_0x0af1:
        r0 = 1;
    L_0x0af2:
        r2 = r1.drawRevealBackground;
        if (r2 == 0) goto L_0x0b22;
    L_0x0af6:
        r2 = r1.currentRevealBounceProgress;
        r3 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1));
        if (r3 >= 0) goto L_0x0b0b;
    L_0x0afc:
        r3 = (float) r9;
        r3 = r3 / r12;
        r2 = r2 + r3;
        r1.currentRevealBounceProgress = r2;
        r2 = r1.currentRevealBounceProgress;
        r2 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1));
        if (r2 <= 0) goto L_0x0b0b;
    L_0x0b07:
        r1.currentRevealBounceProgress = r11;
        r15 = 1;
        goto L_0x0b0c;
    L_0x0b0b:
        r15 = r0;
    L_0x0b0c:
        r0 = r1.currentRevealProgress;
        r2 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r2 >= 0) goto L_0x0b45;
    L_0x0b12:
        r2 = (float) r9;
        r3 = 1133903872; // 0x43960000 float:300.0 double:5.60222949E-315;
        r2 = r2 / r3;
        r0 = r0 + r2;
        r1.currentRevealProgress = r0;
        r0 = r1.currentRevealProgress;
        r0 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1));
        if (r0 <= 0) goto L_0x0b44;
    L_0x0b1f:
        r1.currentRevealProgress = r11;
        goto L_0x0b44;
    L_0x0b22:
        r2 = r1.currentRevealBounceProgress;
        r2 = (r2 > r11 ? 1 : (r2 == r11 ? 0 : -1));
        if (r2 != 0) goto L_0x0b2d;
    L_0x0b28:
        r2 = 0;
        r1.currentRevealBounceProgress = r2;
        r15 = 1;
        goto L_0x0b2f;
    L_0x0b2d:
        r2 = 0;
        r15 = r0;
    L_0x0b2f:
        r0 = r1.currentRevealProgress;
        r3 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r3 <= 0) goto L_0x0b45;
    L_0x0b35:
        r3 = (float) r9;
        r4 = 1133903872; // 0x43960000 float:300.0 double:5.60222949E-315;
        r3 = r3 / r4;
        r0 = r0 - r3;
        r1.currentRevealProgress = r0;
        r0 = r1.currentRevealProgress;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 >= 0) goto L_0x0b44;
    L_0x0b42:
        r1.currentRevealProgress = r2;
    L_0x0b44:
        r15 = 1;
    L_0x0b45:
        if (r15 == 0) goto L_0x0b4a;
    L_0x0b47:
        r23.invalidate();
    L_0x0b4a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Cells.DialogCell.onDraw(android.graphics.Canvas):void");
    }

    public void onReorderStateChanged(boolean z, boolean z2) {
        if ((this.drawPin || !z) && this.drawReorder != z) {
            this.drawReorder = z;
            float f = 1.0f;
            if (z2) {
                if (this.drawReorder) {
                    f = 0.0f;
                }
                this.reorderIconProgress = f;
            } else {
                if (!this.drawReorder) {
                    f = 0.0f;
                }
                this.reorderIconProgress = f;
            }
            invalidate();
            return;
        }
        if (!this.drawPin) {
            this.drawReorder = false;
        }
    }

    public void setSliding(boolean z) {
        this.isSliding = z;
    }

    public void invalidateDrawable(Drawable drawable) {
        if (drawable == this.translationDrawable || drawable == Theme.dialogs_archiveAvatarDrawable) {
            invalidate(drawable.getBounds());
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(16);
        accessibilityNodeInfo.addAction(32);
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        User user;
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        StringBuilder stringBuilder = new StringBuilder();
        String str = ". ";
        if (this.currentDialogFolderId == 1) {
            stringBuilder.append(LocaleController.getString("ArchivedChats", C1067R.string.ArchivedChats));
            stringBuilder.append(str);
        } else {
            if (this.encryptedChat != null) {
                stringBuilder.append(LocaleController.getString("AccDescrSecretChat", C1067R.string.AccDescrSecretChat));
                stringBuilder.append(str);
            }
            user = this.user;
            if (user != null) {
                if (user.bot) {
                    stringBuilder.append(LocaleController.getString("Bot", C1067R.string.Bot));
                    stringBuilder.append(str);
                }
                user = this.user;
                if (user.self) {
                    stringBuilder.append(LocaleController.getString("SavedMessages", C1067R.string.SavedMessages));
                } else {
                    stringBuilder.append(ContactsController.formatName(user.first_name, user.last_name));
                }
                stringBuilder.append(str);
            } else {
                Chat chat = this.chat;
                if (chat != null) {
                    if (chat.broadcast) {
                        stringBuilder.append(LocaleController.getString("AccDescrChannel", C1067R.string.AccDescrChannel));
                    } else {
                        stringBuilder.append(LocaleController.getString("AccDescrGroup", C1067R.string.AccDescrGroup));
                    }
                    stringBuilder.append(str);
                    stringBuilder.append(this.chat.title);
                    stringBuilder.append(str);
                }
            }
        }
        int i = this.unreadCount;
        if (i > 0) {
            stringBuilder.append(LocaleController.formatPluralString("NewMessages", i));
            stringBuilder.append(str);
        }
        MessageObject messageObject = this.message;
        if (messageObject == null || this.currentDialogFolderId != 0) {
            accessibilityEvent.setContentDescription(stringBuilder.toString());
            return;
        }
        int i2 = this.lastMessageDate;
        if (i2 == 0 && messageObject != null) {
            i2 = messageObject.messageOwner.date;
        }
        String formatDateAudio = LocaleController.formatDateAudio((long) i2);
        if (this.message.isOut()) {
            stringBuilder.append(LocaleController.formatString("AccDescrSentDate", C1067R.string.AccDescrSentDate, formatDateAudio));
        } else {
            stringBuilder.append(LocaleController.formatString("AccDescrReceivedDate", C1067R.string.AccDescrReceivedDate, formatDateAudio));
        }
        stringBuilder.append(str);
        if (this.chat != null && !this.message.isOut() && this.message.isFromUser() && this.message.messageOwner.action == null) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.message.messageOwner.from_id));
            if (user != null) {
                stringBuilder.append(ContactsController.formatName(user.first_name, user.last_name));
                stringBuilder.append(str);
            }
        }
        if (this.encryptedChat == null) {
            stringBuilder.append(this.message.messageText);
            if (!(this.message.isMediaEmpty() || TextUtils.isEmpty(this.message.caption))) {
                stringBuilder.append(str);
                stringBuilder.append(this.message.caption);
            }
        }
        accessibilityEvent.setContentDescription(stringBuilder.toString());
    }

    public void setClipProgress(float f) {
        this.clipProgress = f;
        invalidate();
    }

    public float getClipProgress() {
        return this.clipProgress;
    }

    public void setTopClip(int i) {
        this.topClip = i;
    }

    public void setBottomClip(int i) {
        this.bottomClip = i;
    }
}
