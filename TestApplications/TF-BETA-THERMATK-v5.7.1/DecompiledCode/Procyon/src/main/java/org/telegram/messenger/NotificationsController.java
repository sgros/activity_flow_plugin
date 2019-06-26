// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import java.util.Calendar;
import android.util.SparseArray;
import android.util.SparseIntArray;
import org.telegram.messenger.support.SparseLongArray;
import java.util.concurrent.CountDownLatch;
import android.media.SoundPool$OnLoadCompleteListener;
import org.telegram.ui.PopupNotificationActivity;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import android.content.SharedPreferences$Editor;
import android.media.AudioAttributes$Builder;
import android.annotation.SuppressLint;
import java.util.List;
import android.graphics.drawable.BitmapDrawable;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import org.telegram.ui.LaunchActivity;
import android.net.Uri$Builder;
import androidx.core.content.FileProvider;
import android.app.ActivityManager;
import androidx.core.app.RemoteInput;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import androidx.core.app.NotificationCompat;
import android.os.SystemClock;
import android.app.PendingIntent;
import android.content.Intent;
import android.annotation.TargetApi;
import androidx.core.graphics.drawable.IconCompat;
import android.graphics.ImageDecoder$OnHeaderDecodedListener;
import androidx.core.app.Person;
import java.io.File;
import org.telegram.tgnet.TLObject;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;
import android.graphics.Path$Direction;
import android.graphics.Path$FillType;
import android.graphics.Path;
import android.graphics.Canvas;
import android.graphics.PostProcessor;
import android.graphics.ImageDecoder$Source;
import android.graphics.ImageDecoder$ImageInfo;
import android.graphics.ImageDecoder;
import android.text.TextUtils;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.ConnectionsManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.app.NotificationChannel;
import android.content.SharedPreferences;
import java.io.Serializable;
import android.os.PowerManager;
import android.os.Build$VERSION;
import android.media.SoundPool;
import android.graphics.Point;
import android.os.PowerManager$WakeLock;
import android.util.LongSparseArray;
import java.util.ArrayList;
import android.app.AlarmManager;
import android.app.NotificationManager;
import androidx.core.app.NotificationManagerCompat;
import android.media.AudioManager;

public class NotificationsController
{
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static volatile NotificationsController[] Instance;
    public static String OTHER_NOTIFICATIONS_CHANNEL;
    public static final int SETTING_MUTE_2_DAYS = 2;
    public static final int SETTING_MUTE_8_HOURS = 1;
    public static final int SETTING_MUTE_FOREVER = 3;
    public static final int SETTING_MUTE_HOUR = 0;
    public static final int SETTING_MUTE_UNMUTE = 4;
    public static final int TYPE_CHANNEL = 2;
    public static final int TYPE_GROUP = 0;
    public static final int TYPE_PRIVATE = 1;
    protected static AudioManager audioManager;
    public static long globalSecretChatId;
    private static NotificationManagerCompat notificationManager;
    private static DispatchQueue notificationsQueue;
    private static NotificationManager systemNotificationManager;
    private AlarmManager alarmManager;
    private int currentAccount;
    private ArrayList<MessageObject> delayedPushMessages;
    private LongSparseArray<MessageObject> fcmRandomMessagesDict;
    private boolean inChatSoundEnabled;
    private int lastBadgeCount;
    private int lastButtonId;
    private int lastOnlineFromOtherDevice;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
    private LongSparseArray<Integer> lastWearNotifiedMessageId;
    private String launcherClassName;
    private Runnable notificationDelayRunnable;
    private PowerManager$WakeLock notificationDelayWakelock;
    private String notificationGroup;
    private int notificationId;
    private boolean notifyCheck;
    private long opened_dialog_id;
    private int personal_count;
    public ArrayList<MessageObject> popupMessages;
    public ArrayList<MessageObject> popupReplyMessages;
    private LongSparseArray<Integer> pushDialogs;
    private LongSparseArray<Integer> pushDialogsOverrideMention;
    private ArrayList<MessageObject> pushMessages;
    private LongSparseArray<MessageObject> pushMessagesDict;
    public boolean showBadgeMessages;
    public boolean showBadgeMuted;
    public boolean showBadgeNumber;
    private LongSparseArray<Point> smartNotificationsDialogs;
    private int soundIn;
    private boolean soundInLoaded;
    private int soundOut;
    private boolean soundOutLoaded;
    private SoundPool soundPool;
    private int soundRecord;
    private boolean soundRecordLoaded;
    private int total_unread_count;
    private LongSparseArray<Integer> wearNotificationsIds;
    
    static {
        NotificationsController.notificationsQueue = new DispatchQueue("notificationsQueue");
        NotificationsController.notificationManager = null;
        NotificationsController.systemNotificationManager = null;
        NotificationsController.globalSecretChatId = -4294967296L;
        if (Build$VERSION.SDK_INT >= 26 && ApplicationLoader.applicationContext != null) {
            NotificationsController.notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
            NotificationsController.systemNotificationManager = (NotificationManager)ApplicationLoader.applicationContext.getSystemService("notification");
            checkOtherNotificationsChannel();
        }
        NotificationsController.audioManager = (AudioManager)ApplicationLoader.applicationContext.getSystemService("audio");
        NotificationsController.Instance = new NotificationsController[3];
    }
    
    public NotificationsController(int currentAccount) {
        this.pushMessages = new ArrayList<MessageObject>();
        this.delayedPushMessages = new ArrayList<MessageObject>();
        this.pushMessagesDict = (LongSparseArray<MessageObject>)new LongSparseArray();
        this.fcmRandomMessagesDict = (LongSparseArray<MessageObject>)new LongSparseArray();
        this.smartNotificationsDialogs = (LongSparseArray<Point>)new LongSparseArray();
        this.pushDialogs = (LongSparseArray<Integer>)new LongSparseArray();
        this.wearNotificationsIds = (LongSparseArray<Integer>)new LongSparseArray();
        this.lastWearNotifiedMessageId = (LongSparseArray<Integer>)new LongSparseArray();
        this.pushDialogsOverrideMention = (LongSparseArray<Integer>)new LongSparseArray();
        this.popupMessages = new ArrayList<MessageObject>();
        this.popupReplyMessages = new ArrayList<MessageObject>();
        this.opened_dialog_id = 0L;
        this.lastButtonId = 5000;
        this.total_unread_count = 0;
        this.personal_count = 0;
        this.notifyCheck = false;
        this.lastOnlineFromOtherDevice = 0;
        this.lastBadgeCount = -1;
        this.currentAccount = currentAccount;
        this.notificationId = this.currentAccount + 1;
        final StringBuilder sb = new StringBuilder();
        sb.append("messages");
        currentAccount = this.currentAccount;
        Serializable value;
        if (currentAccount == 0) {
            value = "";
        }
        else {
            value = currentAccount;
        }
        sb.append(value);
        this.notificationGroup = sb.toString();
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        this.inChatSoundEnabled = notificationsSettings.getBoolean("EnableInChatSound", true);
        this.showBadgeNumber = notificationsSettings.getBoolean("badgeNumber", true);
        this.showBadgeMuted = notificationsSettings.getBoolean("badgeNumberMuted", false);
        this.showBadgeMessages = notificationsSettings.getBoolean("badgeNumberMessages", true);
        NotificationsController.notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
        NotificationsController.systemNotificationManager = (NotificationManager)ApplicationLoader.applicationContext.getSystemService("notification");
        try {
            NotificationsController.audioManager = (AudioManager)ApplicationLoader.applicationContext.getSystemService("audio");
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            this.alarmManager = (AlarmManager)ApplicationLoader.applicationContext.getSystemService("alarm");
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        try {
            (this.notificationDelayWakelock = ((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(1, "lock")).setReferenceCounted(false);
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
        }
        this.notificationDelayRunnable = new _$$Lambda$NotificationsController$u_XWL43v4eUkt0lAcsDPJJv0mZM(this);
    }
    
    private int addToPopupMessages(final ArrayList<MessageObject> list, final MessageObject element, int n, final long n2, final boolean b, final SharedPreferences sharedPreferences) {
        Label_0173: {
            if (n != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("custom_");
                sb.append(n2);
                int int1;
                if (sharedPreferences.getBoolean(sb.toString(), false)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("popup_");
                    sb2.append(n2);
                    int1 = sharedPreferences.getInt(sb2.toString(), 0);
                }
                else {
                    int1 = 0;
                }
                if (int1 == 0) {
                    if (b) {
                        n = sharedPreferences.getInt("popupChannel", 0);
                        break Label_0173;
                    }
                    String s;
                    if ((int)n2 < 0) {
                        s = "popupGroup";
                    }
                    else {
                        s = "popupAll";
                    }
                    n = sharedPreferences.getInt(s, 0);
                    break Label_0173;
                }
                else {
                    if (int1 == 1) {
                        n = 3;
                        break Label_0173;
                    }
                    if ((n = int1) != 2) {
                        break Label_0173;
                    }
                }
            }
            n = 0;
        }
        int n3;
        if ((n3 = n) != 0) {
            n3 = n;
            if (element.messageOwner.to_id.channel_id != 0) {
                n3 = n;
                if (!element.isMegagroup()) {
                    n3 = 0;
                }
            }
        }
        if (n3 != 0) {
            list.add(0, element);
        }
        return n3;
    }
    
    public static void checkOtherNotificationsChannel() {
        if (Build$VERSION.SDK_INT < 26) {
            return;
        }
        SharedPreferences sharedPreferences;
        if (NotificationsController.OTHER_NOTIFICATIONS_CHANNEL == null) {
            sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            NotificationsController.OTHER_NOTIFICATIONS_CHANNEL = sharedPreferences.getString("OtherKey", "Other3");
        }
        else {
            sharedPreferences = null;
        }
        NotificationChannel notificationChannel2;
        final NotificationChannel notificationChannel = notificationChannel2 = NotificationsController.systemNotificationManager.getNotificationChannel(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
        if (notificationChannel != null) {
            notificationChannel2 = notificationChannel;
            if (notificationChannel.getImportance() == 0) {
                NotificationsController.systemNotificationManager.deleteNotificationChannel(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
                NotificationsController.OTHER_NOTIFICATIONS_CHANNEL = null;
                notificationChannel2 = null;
            }
        }
        if (NotificationsController.OTHER_NOTIFICATIONS_CHANNEL == null) {
            SharedPreferences sharedPreferences2;
            if ((sharedPreferences2 = sharedPreferences) == null) {
                sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Other");
            sb.append(Utilities.random.nextLong());
            NotificationsController.OTHER_NOTIFICATIONS_CHANNEL = sb.toString();
            sharedPreferences2.edit().putString("OtherKey", NotificationsController.OTHER_NOTIFICATIONS_CHANNEL).commit();
        }
        if (notificationChannel2 == null) {
            final NotificationChannel notificationChannel3 = new NotificationChannel(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL, (CharSequence)"Other", 3);
            notificationChannel3.enableLights(false);
            notificationChannel3.enableVibration(false);
            notificationChannel3.setSound((Uri)null, (AudioAttributes)null);
            NotificationsController.systemNotificationManager.createNotificationChannel(notificationChannel3);
        }
    }
    
    private void dismissNotification() {
        try {
            NotificationsController.notificationManager.cancel(this.notificationId);
            this.pushMessages.clear();
            this.pushMessagesDict.clear();
            this.lastWearNotifiedMessageId.clear();
            for (int i = 0; i < this.wearNotificationsIds.size(); ++i) {
                NotificationsController.notificationManager.cancel((int)this.wearNotificationsIds.valueAt(i));
            }
            this.wearNotificationsIds.clear();
            AndroidUtilities.runOnUIThread((Runnable)_$$Lambda$NotificationsController$2v2nyML5dTCxIdrQrE6xmPJzze8.INSTANCE);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static NotificationsController getInstance(final int n) {
        final NotificationsController notificationsController;
        if ((notificationsController = NotificationsController.Instance[n]) == null) {
            synchronized (NotificationsController.class) {
                if (NotificationsController.Instance[n] == null) {
                    NotificationsController.Instance[n] = new NotificationsController(n);
                }
            }
        }
        return notificationsController;
    }
    
    private int getNotifyOverride(final SharedPreferences sharedPreferences, final long n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("notify2_");
        sb.append(n);
        int int1;
        final int n2 = int1 = sharedPreferences.getInt(sb.toString(), -1);
        if (n2 == 3) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("notifyuntil_");
            sb2.append(n);
            int1 = n2;
            if (sharedPreferences.getInt(sb2.toString(), 0) >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                int1 = 2;
            }
        }
        return int1;
    }
    
    private String getShortStringForMessage(MessageObject messageObject, final String[] array, final boolean[] array2) {
        if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", 2131561139);
        }
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        final long dialog_id = messageOwner.dialog_id;
        final TLRPC.Peer to_id = messageOwner.to_id;
        int i = to_id.chat_id;
        if (i == 0) {
            i = to_id.channel_id;
        }
        final int user_id = messageObject.messageOwner.to_id.user_id;
        if (array2 != null) {
            array2[0] = true;
        }
        if (messageObject.isFcmMessage()) {
            if (i == 0 && user_id != 0) {
                if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                    if (array2 != null) {
                        array2[0] = false;
                    }
                    return LocaleController.formatString("NotificationMessageNoText", 2131560043, messageObject.localName);
                }
                if (Build$VERSION.SDK_INT > 27) {
                    array[0] = messageObject.localName;
                }
            }
            else if (i != 0) {
                final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                if ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true))) {
                    if (array2 != null) {
                        array2[0] = false;
                    }
                    if (!messageObject.isMegagroup() && messageObject.messageOwner.to_id.channel_id != 0) {
                        return LocaleController.formatString("ChannelMessageNoText", 2131558973, messageObject.localName);
                    }
                    return LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, messageObject.localUserName, messageObject.localName);
                }
                else if (messageObject.messageOwner.to_id.channel_id != 0 && !messageObject.isMegagroup()) {
                    if (Build$VERSION.SDK_INT > 27) {
                        array[0] = messageObject.localName;
                    }
                }
                else {
                    array[0] = messageObject.localUserName;
                }
            }
            return messageObject.messageOwner.message;
        }
        int j;
        if (user_id == 0) {
            if (!messageObject.isFromUser() && messageObject.getId() >= 0) {
                j = -i;
            }
            else {
                j = messageObject.messageOwner.from_id;
            }
        }
        else if ((j = user_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            j = messageObject.messageOwner.from_id;
        }
        long n = dialog_id;
        if (dialog_id == 0L) {
            if (i != 0) {
                n = -i;
            }
            else {
                n = dialog_id;
                if (j != 0) {
                    n = j;
                }
            }
        }
        String s = null;
        Label_0570: {
            if (j > 0) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(j);
                if (user != null) {
                    s = UserObject.getUserName(user);
                    if (i != 0) {
                        array[0] = s;
                        break Label_0570;
                    }
                    if (Build$VERSION.SDK_INT > 27) {
                        array[0] = s;
                        break Label_0570;
                    }
                    array[0] = null;
                    break Label_0570;
                }
            }
            else {
                final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-j);
                if (chat != null) {
                    s = chat.title;
                    array[0] = s;
                    break Label_0570;
                }
            }
            s = null;
        }
        if (s == null) {
            return null;
        }
        TLRPC.Chat chat3;
        if (i != 0) {
            final TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(i);
            if (chat2 == null) {
                return null;
            }
            chat3 = chat2;
            if (ChatObject.isChannel(chat2)) {
                chat3 = chat2;
                if (!chat2.megagroup) {
                    chat3 = chat2;
                    if (Build$VERSION.SDK_INT <= 27) {
                        array[0] = null;
                        chat3 = chat2;
                    }
                }
            }
        }
        else {
            chat3 = null;
        }
        if ((int)n == 0) {
            array[0] = null;
            return LocaleController.getString("YouHaveNewMessage", 2131561139);
        }
        final SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
        final boolean b = ChatObject.isChannel(chat3) && !chat3.megagroup;
        if ((i == 0 && j != 0 && notificationsSettings2.getBoolean("EnablePreviewAll", true)) || (i != 0 && ((!b && notificationsSettings2.getBoolean("EnablePreviewGroup", true)) || (b && notificationsSettings2.getBoolean("EnablePreviewChannel", true))))) {
            final TLRPC.Message messageOwner2 = messageObject.messageOwner;
            if (messageOwner2 instanceof TLRPC.TL_messageService) {
                array[0] = null;
                final TLRPC.MessageAction action = messageOwner2.action;
                if (action instanceof TLRPC.TL_messageActionUserJoined || action instanceof TLRPC.TL_messageActionContactSignUp) {
                    return LocaleController.formatString("NotificationContactJoined", 2131559997, s);
                }
                if (action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                    return LocaleController.formatString("NotificationContactNewPhoto", 2131559998, s);
                }
                if (action instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                    final String formatString = LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(messageObject.messageOwner.date * 1000L), LocaleController.getInstance().formatterDay.format(messageObject.messageOwner.date * 1000L));
                    final String first_name = UserConfig.getInstance(this.currentAccount).getCurrentUser().first_name;
                    final TLRPC.MessageAction action2 = messageObject.messageOwner.action;
                    return LocaleController.formatString("NotificationUnrecognizedDevice", 2131560054, first_name, formatString, action2.title, action2.address);
                }
                if (action instanceof TLRPC.TL_messageActionGameScore || action instanceof TLRPC.TL_messageActionPaymentSent) {
                    return messageObject.messageText.toString();
                }
                if (action instanceof TLRPC.TL_messageActionPhoneCall) {
                    final TLRPC.PhoneCallDiscardReason reason = action.reason;
                    if (!messageObject.isOut() && reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed) {
                        return LocaleController.getString("CallMessageIncomingMissed", 2131558875);
                    }
                }
                else if (action instanceof TLRPC.TL_messageActionChatAddUser) {
                    final int user_id2 = action.user_id;
                    int intValue;
                    if ((intValue = user_id2) == 0) {
                        intValue = user_id2;
                        if (action.users.size() == 1) {
                            intValue = messageObject.messageOwner.action.users.get(0);
                        }
                    }
                    if (intValue == 0) {
                        final StringBuilder sb = new StringBuilder();
                        for (int k = 0; k < messageObject.messageOwner.action.users.size(); ++k) {
                            final TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(messageObject.messageOwner.action.users.get(k));
                            if (user2 != null) {
                                final String userName = UserObject.getUserName(user2);
                                if (sb.length() != 0) {
                                    sb.append(", ");
                                }
                                sb.append(userName);
                            }
                        }
                        return LocaleController.formatString("NotificationGroupAddMember", 2131560001, s, chat3.title, sb.toString());
                    }
                    if (messageObject.messageOwner.to_id.channel_id != 0 && !chat3.megagroup) {
                        return LocaleController.formatString("ChannelAddedByNotification", 2131558925, s, chat3.title);
                    }
                    if (intValue == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        return LocaleController.formatString("NotificationInvitedToGroup", 2131560010, s, chat3.title);
                    }
                    final TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(intValue);
                    if (user3 == null) {
                        return null;
                    }
                    if (j != user3.id) {
                        return LocaleController.formatString("NotificationGroupAddMember", 2131560001, s, chat3.title, UserObject.getUserName(user3));
                    }
                    if (chat3.megagroup) {
                        return LocaleController.formatString("NotificationGroupAddSelfMega", 2131560003, s, chat3.title);
                    }
                    return LocaleController.formatString("NotificationGroupAddSelf", 2131560002, s, chat3.title);
                }
                else {
                    if (action instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                        return LocaleController.formatString("NotificationInvitedToGroupByLink", 2131560011, s, chat3.title);
                    }
                    if (action instanceof TLRPC.TL_messageActionChatEditTitle) {
                        return LocaleController.formatString("NotificationEditedGroupName", 2131559999, s, action.title);
                    }
                    if (!(action instanceof TLRPC.TL_messageActionChatEditPhoto) && !(action instanceof TLRPC.TL_messageActionChatDeletePhoto)) {
                        if (action instanceof TLRPC.TL_messageActionChatDeleteUser) {
                            if (action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                return LocaleController.formatString("NotificationGroupKickYou", 2131560008, s, chat3.title);
                            }
                            if (messageObject.messageOwner.action.user_id == j) {
                                return LocaleController.formatString("NotificationGroupLeftMember", 2131560009, s, chat3.title);
                            }
                            final TLRPC.User user4 = MessagesController.getInstance(this.currentAccount).getUser(messageObject.messageOwner.action.user_id);
                            if (user4 == null) {
                                return null;
                            }
                            return LocaleController.formatString("NotificationGroupKickMember", 2131560007, s, chat3.title, UserObject.getUserName(user4));
                        }
                        else {
                            if (action instanceof TLRPC.TL_messageActionChatCreate) {
                                return messageObject.messageText.toString();
                            }
                            if (action instanceof TLRPC.TL_messageActionChannelCreate) {
                                return messageObject.messageText.toString();
                            }
                            if (action instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                return LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, chat3.title);
                            }
                            if (action instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                return LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, action.title);
                            }
                            if (action instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                return messageObject.messageText.toString();
                            }
                            if (action instanceof TLRPC.TL_messageActionPinMessage) {
                                if (chat3 != null && (!ChatObject.isChannel(chat3) || chat3.megagroup)) {
                                    messageObject = messageObject.replyMessageObject;
                                    if (messageObject == null) {
                                        return LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, s, chat3.title);
                                    }
                                    if (messageObject.isMusic()) {
                                        return LocaleController.formatString("NotificationActionPinnedMusic", 2131559977, s, chat3.title);
                                    }
                                    if (messageObject.isVideo()) {
                                        if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append("\ud83d\udcf9 ");
                                            sb2.append(messageObject.messageOwner.message);
                                            return LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb2.toString(), chat3.title);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedVideo", 2131559993, s, chat3.title);
                                    }
                                    else if (messageObject.isGif()) {
                                        if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final StringBuilder sb3 = new StringBuilder();
                                            sb3.append("\ud83c\udfac ");
                                            sb3.append(messageObject.messageOwner.message);
                                            return LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb3.toString(), chat3.title);
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedGif", 2131559973, s, chat3.title);
                                    }
                                    else {
                                        if (messageObject.isVoice()) {
                                            return LocaleController.formatString("NotificationActionPinnedVoice", 2131559995, s, chat3.title);
                                        }
                                        if (messageObject.isRoundVideo()) {
                                            return LocaleController.formatString("NotificationActionPinnedRound", 2131559985, s, chat3.title);
                                        }
                                        if (messageObject.isSticker()) {
                                            final String stickerEmoji = messageObject.getStickerEmoji();
                                            if (stickerEmoji != null) {
                                                return LocaleController.formatString("NotificationActionPinnedStickerEmoji", 2131559989, s, chat3.title, stickerEmoji);
                                            }
                                            return LocaleController.formatString("NotificationActionPinnedSticker", 2131559987, s, chat3.title);
                                        }
                                        else {
                                            final TLRPC.Message messageOwner3 = messageObject.messageOwner;
                                            final TLRPC.MessageMedia media = messageOwner3.media;
                                            if (media instanceof TLRPC.TL_messageMediaDocument) {
                                                if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner3.message)) {
                                                    final StringBuilder sb4 = new StringBuilder();
                                                    sb4.append("\ud83d\udcce ");
                                                    sb4.append(messageObject.messageOwner.message);
                                                    return LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb4.toString(), chat3.title);
                                                }
                                                return LocaleController.formatString("NotificationActionPinnedFile", 2131559963, s, chat3.title);
                                            }
                                            else {
                                                if (media instanceof TLRPC.TL_messageMediaGeo || media instanceof TLRPC.TL_messageMediaVenue) {
                                                    return LocaleController.formatString("NotificationActionPinnedGeo", 2131559969, s, chat3.title);
                                                }
                                                if (media instanceof TLRPC.TL_messageMediaGeoLive) {
                                                    return LocaleController.formatString("NotificationActionPinnedGeoLive", 2131559971, s, chat3.title);
                                                }
                                                if (media instanceof TLRPC.TL_messageMediaContact) {
                                                    final TLRPC.TL_messageMediaContact tl_messageMediaContact = (TLRPC.TL_messageMediaContact)media;
                                                    return LocaleController.formatString("NotificationActionPinnedContact2", 2131559961, s, chat3.title, ContactsController.formatName(tl_messageMediaContact.first_name, tl_messageMediaContact.last_name));
                                                }
                                                if (media instanceof TLRPC.TL_messageMediaPoll) {
                                                    return LocaleController.formatString("NotificationActionPinnedPoll2", 2131559983, s, chat3.title, ((TLRPC.TL_messageMediaPoll)media).poll.question);
                                                }
                                                if (media instanceof TLRPC.TL_messageMediaPhoto) {
                                                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner3.message)) {
                                                        final StringBuilder sb5 = new StringBuilder();
                                                        sb5.append("\ud83d\uddbc ");
                                                        sb5.append(messageObject.messageOwner.message);
                                                        return LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb5.toString(), chat3.title);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedPhoto", 2131559981, s, chat3.title);
                                                }
                                                else {
                                                    if (media instanceof TLRPC.TL_messageMediaGame) {
                                                        return LocaleController.formatString("NotificationActionPinnedGame", 2131559965, s, chat3.title);
                                                    }
                                                    final CharSequence messageText = messageObject.messageText;
                                                    if (messageText != null && messageText.length() > 0) {
                                                        String s2;
                                                        final CharSequence charSequence = s2 = (String)messageObject.messageText;
                                                        if (charSequence.length() > 20) {
                                                            final StringBuilder sb6 = new StringBuilder();
                                                            sb6.append((Object)charSequence.subSequence(0, 20));
                                                            sb6.append("...");
                                                            s2 = sb6.toString();
                                                        }
                                                        return LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, s2, chat3.title);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, s, chat3.title);
                                                }
                                            }
                                        }
                                    }
                                }
                                else {
                                    messageObject = messageObject.replyMessageObject;
                                    if (messageObject == null) {
                                        return LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, chat3.title);
                                    }
                                    if (messageObject.isMusic()) {
                                        return LocaleController.formatString("NotificationActionPinnedMusicChannel", 2131559978, chat3.title);
                                    }
                                    if (messageObject.isVideo()) {
                                        if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final StringBuilder sb7 = new StringBuilder();
                                            sb7.append("\ud83d\udcf9 ");
                                            sb7.append(messageObject.messageOwner.message);
                                            return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat3.title, sb7.toString());
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedVideoChannel", 2131559994, chat3.title);
                                    }
                                    else if (messageObject.isGif()) {
                                        if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final StringBuilder sb8 = new StringBuilder();
                                            sb8.append("\ud83c\udfac ");
                                            sb8.append(messageObject.messageOwner.message);
                                            return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat3.title, sb8.toString());
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedGifChannel", 2131559974, chat3.title);
                                    }
                                    else {
                                        if (messageObject.isVoice()) {
                                            return LocaleController.formatString("NotificationActionPinnedVoiceChannel", 2131559996, chat3.title);
                                        }
                                        if (messageObject.isRoundVideo()) {
                                            return LocaleController.formatString("NotificationActionPinnedRoundChannel", 2131559986, chat3.title);
                                        }
                                        if (messageObject.isSticker()) {
                                            final String stickerEmoji2 = messageObject.getStickerEmoji();
                                            if (stickerEmoji2 != null) {
                                                return LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", 2131559990, chat3.title, stickerEmoji2);
                                            }
                                            return LocaleController.formatString("NotificationActionPinnedStickerChannel", 2131559988, chat3.title);
                                        }
                                        else {
                                            final TLRPC.Message messageOwner4 = messageObject.messageOwner;
                                            final TLRPC.MessageMedia media2 = messageOwner4.media;
                                            if (media2 instanceof TLRPC.TL_messageMediaDocument) {
                                                if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner4.message)) {
                                                    final StringBuilder sb9 = new StringBuilder();
                                                    sb9.append("\ud83d\udcce ");
                                                    sb9.append(messageObject.messageOwner.message);
                                                    return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat3.title, sb9.toString());
                                                }
                                                return LocaleController.formatString("NotificationActionPinnedFileChannel", 2131559964, chat3.title);
                                            }
                                            else {
                                                if (media2 instanceof TLRPC.TL_messageMediaGeo || media2 instanceof TLRPC.TL_messageMediaVenue) {
                                                    return LocaleController.formatString("NotificationActionPinnedGeoChannel", 2131559970, chat3.title);
                                                }
                                                if (media2 instanceof TLRPC.TL_messageMediaGeoLive) {
                                                    return LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", 2131559972, chat3.title);
                                                }
                                                if (media2 instanceof TLRPC.TL_messageMediaContact) {
                                                    final TLRPC.TL_messageMediaContact tl_messageMediaContact2 = (TLRPC.TL_messageMediaContact)media2;
                                                    return LocaleController.formatString("NotificationActionPinnedContactChannel2", 2131559962, chat3.title, ContactsController.formatName(tl_messageMediaContact2.first_name, tl_messageMediaContact2.last_name));
                                                }
                                                if (media2 instanceof TLRPC.TL_messageMediaPoll) {
                                                    return LocaleController.formatString("NotificationActionPinnedPollChannel2", 2131559984, chat3.title, ((TLRPC.TL_messageMediaPoll)media2).poll.question);
                                                }
                                                if (media2 instanceof TLRPC.TL_messageMediaPhoto) {
                                                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner4.message)) {
                                                        final StringBuilder sb10 = new StringBuilder();
                                                        sb10.append("\ud83d\uddbc ");
                                                        sb10.append(messageObject.messageOwner.message);
                                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat3.title, sb10.toString());
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedPhotoChannel", 2131559982, chat3.title);
                                                }
                                                else {
                                                    if (media2 instanceof TLRPC.TL_messageMediaGame) {
                                                        return LocaleController.formatString("NotificationActionPinnedGameChannel", 2131559966, chat3.title);
                                                    }
                                                    final CharSequence messageText2 = messageObject.messageText;
                                                    if (messageText2 != null && messageText2.length() > 0) {
                                                        String s3;
                                                        final CharSequence charSequence2 = s3 = (String)messageObject.messageText;
                                                        if (charSequence2.length() > 20) {
                                                            final StringBuilder sb11 = new StringBuilder();
                                                            sb11.append((Object)charSequence2.subSequence(0, 20));
                                                            sb11.append("...");
                                                            s3 = sb11.toString();
                                                        }
                                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat3.title, s3);
                                                    }
                                                    return LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, chat3.title);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (messageObject.messageOwner.to_id.channel_id != 0 && !chat3.megagroup) {
                            return LocaleController.formatString("ChannelPhotoEditNotification", 2131558987, chat3.title);
                        }
                        return LocaleController.formatString("NotificationEditedGroupPhoto", 2131560000, s, chat3.title);
                    }
                }
            }
            else if (messageObject.isMediaEmpty()) {
                if (!TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                    return messageObject.messageOwner.message;
                }
                return LocaleController.getString("Message", 2131559845);
            }
            else {
                final TLRPC.Message messageOwner5 = messageObject.messageOwner;
                if (messageOwner5.media instanceof TLRPC.TL_messageMediaPhoto) {
                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner5.message)) {
                        final StringBuilder sb12 = new StringBuilder();
                        sb12.append("\ud83d\uddbc ");
                        sb12.append(messageObject.messageOwner.message);
                        return sb12.toString();
                    }
                    if (messageObject.messageOwner.media.ttl_seconds != 0) {
                        return LocaleController.getString("AttachDestructingPhoto", 2131558712);
                    }
                    return LocaleController.getString("AttachPhoto", 2131558727);
                }
                else if (messageObject.isVideo()) {
                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                        final StringBuilder sb13 = new StringBuilder();
                        sb13.append("\ud83d\udcf9 ");
                        sb13.append(messageObject.messageOwner.message);
                        return sb13.toString();
                    }
                    if (messageObject.messageOwner.media.ttl_seconds != 0) {
                        return LocaleController.getString("AttachDestructingVideo", 2131558713);
                    }
                    return LocaleController.getString("AttachVideo", 2131558733);
                }
                else {
                    if (messageObject.isGame()) {
                        return LocaleController.getString("AttachGame", 2131558715);
                    }
                    if (messageObject.isVoice()) {
                        return LocaleController.getString("AttachAudio", 2131558709);
                    }
                    if (messageObject.isRoundVideo()) {
                        return LocaleController.getString("AttachRound", 2131558729);
                    }
                    if (messageObject.isMusic()) {
                        return LocaleController.getString("AttachMusic", 2131558726);
                    }
                    final TLRPC.MessageMedia media3 = messageObject.messageOwner.media;
                    if (media3 instanceof TLRPC.TL_messageMediaContact) {
                        return LocaleController.getString("AttachContact", 2131558711);
                    }
                    if (media3 instanceof TLRPC.TL_messageMediaPoll) {
                        return LocaleController.getString("Poll", 2131560467);
                    }
                    if (media3 instanceof TLRPC.TL_messageMediaGeo || media3 instanceof TLRPC.TL_messageMediaVenue) {
                        return LocaleController.getString("AttachLocation", 2131558723);
                    }
                    if (media3 instanceof TLRPC.TL_messageMediaGeoLive) {
                        return LocaleController.getString("AttachLiveLocation", 2131558721);
                    }
                    if (media3 instanceof TLRPC.TL_messageMediaDocument) {
                        if (messageObject.isSticker()) {
                            final String stickerEmoji3 = messageObject.getStickerEmoji();
                            if (stickerEmoji3 != null) {
                                final StringBuilder sb14 = new StringBuilder();
                                sb14.append(stickerEmoji3);
                                sb14.append(" ");
                                sb14.append(LocaleController.getString("AttachSticker", 2131558730));
                                return sb14.toString();
                            }
                            return LocaleController.getString("AttachSticker", 2131558730);
                        }
                        else if (messageObject.isGif()) {
                            if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                final StringBuilder sb15 = new StringBuilder();
                                sb15.append("\ud83c\udfac ");
                                sb15.append(messageObject.messageOwner.message);
                                return sb15.toString();
                            }
                            return LocaleController.getString("AttachGif", 2131558716);
                        }
                        else {
                            if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                final StringBuilder sb16 = new StringBuilder();
                                sb16.append("\ud83d\udcce ");
                                sb16.append(messageObject.messageOwner.message);
                                return sb16.toString();
                            }
                            return LocaleController.getString("AttachDocument", 2131558714);
                        }
                    }
                }
            }
            return null;
        }
        if (array2 != null) {
            array2[0] = false;
        }
        return LocaleController.getString("Message", 2131559845);
    }
    
    private String getStringForMessage(final MessageObject messageObject, final boolean b, final boolean[] array, final boolean[] array2) {
        if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", 2131561139);
        }
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        final long dialog_id = messageOwner.dialog_id;
        final TLRPC.Peer to_id = messageOwner.to_id;
        int i = to_id.chat_id;
        if (i == 0) {
            i = to_id.channel_id;
        }
        final int user_id = messageObject.messageOwner.to_id.user_id;
        if (array2 != null) {
            array2[0] = true;
        }
        if (messageObject.isFcmMessage()) {
            if (i == 0 && user_id != 0) {
                if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                    if (array2 != null) {
                        array2[0] = false;
                    }
                    return LocaleController.formatString("NotificationMessageNoText", 2131560043, messageObject.localName);
                }
            }
            else if (i != 0) {
                final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
                if ((!messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewGroup", true)) || (messageObject.localChannel && !notificationsSettings.getBoolean("EnablePreviewChannel", true))) {
                    if (array2 != null) {
                        array2[0] = false;
                    }
                    if (!messageObject.isMegagroup() && messageObject.messageOwner.to_id.channel_id != 0) {
                        return LocaleController.formatString("ChannelMessageNoText", 2131558973, messageObject.localName);
                    }
                    return LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, messageObject.localUserName, messageObject.localName);
                }
            }
            array[0] = true;
            return (String)messageObject.messageText;
        }
        int j;
        if (user_id == 0) {
            if (!messageObject.isFromUser() && messageObject.getId() >= 0) {
                j = -i;
            }
            else {
                j = messageObject.messageOwner.from_id;
            }
        }
        else if ((j = user_id) == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            j = messageObject.messageOwner.from_id;
        }
        long n = dialog_id;
        if (dialog_id == 0L) {
            if (i != 0) {
                n = -i;
            }
            else {
                n = dialog_id;
                if (j != 0) {
                    n = j;
                }
            }
        }
        String s = null;
        Label_0476: {
            if (j > 0) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(j);
                if (user != null) {
                    s = UserObject.getUserName(user);
                    break Label_0476;
                }
            }
            else {
                final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-j);
                if (chat != null) {
                    s = chat.title;
                    break Label_0476;
                }
            }
            s = null;
        }
        if (s == null) {
            return null;
        }
        TLRPC.Chat chat2;
        if (i != 0) {
            if ((chat2 = MessagesController.getInstance(this.currentAccount).getChat(i)) == null) {
                return null;
            }
        }
        else {
            chat2 = null;
        }
        String s2;
        if ((int)n == 0) {
            s2 = LocaleController.getString("YouHaveNewMessage", 2131561139);
        }
        else {
            String s3 = null;
            Label_1657: {
                if (i == 0 && j != 0) {
                    if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                        if (array2 != null) {
                            array2[0] = false;
                        }
                        s2 = LocaleController.formatString("NotificationMessageNoText", 2131560043, s);
                        return s2;
                    }
                    final TLRPC.Message messageOwner2 = messageObject.messageOwner;
                    if (messageOwner2 instanceof TLRPC.TL_messageService) {
                        final TLRPC.MessageAction action = messageOwner2.action;
                        if (action instanceof TLRPC.TL_messageActionUserJoined || action instanceof TLRPC.TL_messageActionContactSignUp) {
                            s2 = LocaleController.formatString("NotificationContactJoined", 2131559997, s);
                            return s2;
                        }
                        if (action instanceof TLRPC.TL_messageActionUserUpdatedPhoto) {
                            s2 = LocaleController.formatString("NotificationContactNewPhoto", 2131559998, s);
                            return s2;
                        }
                        if (action instanceof TLRPC.TL_messageActionLoginUnknownLocation) {
                            final String formatString = LocaleController.formatString("formatDateAtTime", 2131561210, LocaleController.getInstance().formatterYear.format(messageObject.messageOwner.date * 1000L), LocaleController.getInstance().formatterDay.format(messageObject.messageOwner.date * 1000L));
                            final String first_name = UserConfig.getInstance(this.currentAccount).getCurrentUser().first_name;
                            final TLRPC.MessageAction action2 = messageObject.messageOwner.action;
                            s2 = LocaleController.formatString("NotificationUnrecognizedDevice", 2131560054, first_name, formatString, action2.title, action2.address);
                            return s2;
                        }
                        if (action instanceof TLRPC.TL_messageActionGameScore || action instanceof TLRPC.TL_messageActionPaymentSent) {
                            s2 = messageObject.messageText.toString();
                            return s2;
                        }
                        if (action instanceof TLRPC.TL_messageActionPhoneCall) {
                            final TLRPC.PhoneCallDiscardReason reason = action.reason;
                            if (!messageObject.isOut() && reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed) {
                                s2 = LocaleController.getString("CallMessageIncomingMissed", 2131558875);
                                return s2;
                            }
                        }
                    }
                    else if (messageObject.isMediaEmpty()) {
                        if (b) {
                            s2 = LocaleController.formatString("NotificationMessageNoText", 2131560043, s);
                            return s2;
                        }
                        if (!TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                            s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, messageObject.messageOwner.message);
                            array[0] = true;
                            return s2;
                        }
                        s2 = LocaleController.formatString("NotificationMessageNoText", 2131560043, s);
                        return s2;
                    }
                    else {
                        final TLRPC.Message messageOwner3 = messageObject.messageOwner;
                        if (messageOwner3.media instanceof TLRPC.TL_messageMediaPhoto) {
                            if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner3.message)) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("\ud83d\uddbc ");
                                sb.append(messageObject.messageOwner.message);
                                s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb.toString());
                                array[0] = true;
                                return s2;
                            }
                            if (messageObject.messageOwner.media.ttl_seconds != 0) {
                                s2 = LocaleController.formatString("NotificationMessageSDPhoto", 2131560047, s);
                                return s2;
                            }
                            s2 = LocaleController.formatString("NotificationMessagePhoto", 2131560044, s);
                            return s2;
                        }
                        else if (messageObject.isVideo()) {
                            if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("\ud83d\udcf9 ");
                                sb2.append(messageObject.messageOwner.message);
                                s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb2.toString());
                                array[0] = true;
                                return s2;
                            }
                            if (messageObject.messageOwner.media.ttl_seconds != 0) {
                                s2 = LocaleController.formatString("NotificationMessageSDVideo", 2131560048, s);
                                return s2;
                            }
                            s2 = LocaleController.formatString("NotificationMessageVideo", 2131560052, s);
                            return s2;
                        }
                        else {
                            if (messageObject.isGame()) {
                                s2 = LocaleController.formatString("NotificationMessageGame", 2131560018, s, messageObject.messageOwner.media.game.title);
                                return s2;
                            }
                            if (messageObject.isVoice()) {
                                s2 = LocaleController.formatString("NotificationMessageAudio", 2131560013, s);
                                return s2;
                            }
                            if (messageObject.isRoundVideo()) {
                                s2 = LocaleController.formatString("NotificationMessageRound", 2131560046, s);
                                return s2;
                            }
                            if (messageObject.isMusic()) {
                                s2 = LocaleController.formatString("NotificationMessageMusic", 2131560042, s);
                                return s2;
                            }
                            final TLRPC.MessageMedia media = messageObject.messageOwner.media;
                            if (media instanceof TLRPC.TL_messageMediaContact) {
                                final TLRPC.TL_messageMediaContact tl_messageMediaContact = (TLRPC.TL_messageMediaContact)media;
                                s2 = LocaleController.formatString("NotificationMessageContact2", 2131560014, s, ContactsController.formatName(tl_messageMediaContact.first_name, tl_messageMediaContact.last_name));
                                return s2;
                            }
                            if (media instanceof TLRPC.TL_messageMediaPoll) {
                                s2 = LocaleController.formatString("NotificationMessagePoll2", 2131560045, s, ((TLRPC.TL_messageMediaPoll)media).poll.question);
                                return s2;
                            }
                            if (media instanceof TLRPC.TL_messageMediaGeo || media instanceof TLRPC.TL_messageMediaVenue) {
                                s2 = LocaleController.formatString("NotificationMessageMap", 2131560041, s);
                                return s2;
                            }
                            if (media instanceof TLRPC.TL_messageMediaGeoLive) {
                                s2 = LocaleController.formatString("NotificationMessageLiveLocation", 2131560040, s);
                                return s2;
                            }
                            if (media instanceof TLRPC.TL_messageMediaDocument) {
                                if (messageObject.isSticker()) {
                                    final String stickerEmoji = messageObject.getStickerEmoji();
                                    if (stickerEmoji != null) {
                                        s3 = LocaleController.formatString("NotificationMessageStickerEmoji", 2131560050, s, stickerEmoji);
                                        break Label_1657;
                                    }
                                    s3 = LocaleController.formatString("NotificationMessageSticker", 2131560049, s);
                                    break Label_1657;
                                }
                                else if (messageObject.isGif()) {
                                    if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append("\ud83c\udfac ");
                                        sb3.append(messageObject.messageOwner.message);
                                        s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb3.toString());
                                        array[0] = true;
                                        return s2;
                                    }
                                    s2 = LocaleController.formatString("NotificationMessageGif", 2131560020, s);
                                    return s2;
                                }
                                else {
                                    if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                        final StringBuilder sb4 = new StringBuilder();
                                        sb4.append("\ud83d\udcce ");
                                        sb4.append(messageObject.messageOwner.message);
                                        s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb4.toString());
                                        array[0] = true;
                                        return s2;
                                    }
                                    s2 = LocaleController.formatString("NotificationMessageDocument", 2131560015, s);
                                    return s2;
                                }
                            }
                        }
                    }
                }
                else if (i != 0) {
                    final SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(this.currentAccount);
                    final boolean b2 = ChatObject.isChannel(chat2) && !chat2.megagroup;
                    if ((!b2 && notificationsSettings2.getBoolean("EnablePreviewGroup", true)) || (b2 && notificationsSettings2.getBoolean("EnablePreviewChannel", true))) {
                        final TLRPC.Message messageOwner4 = messageObject.messageOwner;
                        if (messageOwner4 instanceof TLRPC.TL_messageService) {
                            final TLRPC.MessageAction action3 = messageOwner4.action;
                            if (action3 instanceof TLRPC.TL_messageActionChatAddUser) {
                                final int user_id2 = action3.user_id;
                                int intValue;
                                if ((intValue = user_id2) == 0) {
                                    intValue = user_id2;
                                    if (action3.users.size() == 1) {
                                        intValue = messageObject.messageOwner.action.users.get(0);
                                    }
                                }
                                if (intValue == 0) {
                                    final StringBuilder sb5 = new StringBuilder();
                                    for (int k = 0; k < messageObject.messageOwner.action.users.size(); ++k) {
                                        final TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(messageObject.messageOwner.action.users.get(k));
                                        if (user2 != null) {
                                            final String userName = UserObject.getUserName(user2);
                                            if (sb5.length() != 0) {
                                                sb5.append(", ");
                                            }
                                            sb5.append(userName);
                                        }
                                    }
                                    s3 = LocaleController.formatString("NotificationGroupAddMember", 2131560001, s, chat2.title, sb5.toString());
                                    break Label_1657;
                                }
                                if (messageObject.messageOwner.to_id.channel_id != 0 && !chat2.megagroup) {
                                    s3 = LocaleController.formatString("ChannelAddedByNotification", 2131558925, s, chat2.title);
                                    break Label_1657;
                                }
                                if (intValue == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                    s3 = LocaleController.formatString("NotificationInvitedToGroup", 2131560010, s, chat2.title);
                                    break Label_1657;
                                }
                                final TLRPC.User user3 = MessagesController.getInstance(this.currentAccount).getUser(intValue);
                                if (user3 == null) {
                                    return null;
                                }
                                if (j != user3.id) {
                                    s3 = LocaleController.formatString("NotificationGroupAddMember", 2131560001, s, chat2.title, UserObject.getUserName(user3));
                                    break Label_1657;
                                }
                                if (chat2.megagroup) {
                                    s3 = LocaleController.formatString("NotificationGroupAddSelfMega", 2131560003, s, chat2.title);
                                    break Label_1657;
                                }
                                s3 = LocaleController.formatString("NotificationGroupAddSelf", 2131560002, s, chat2.title);
                                break Label_1657;
                            }
                            else {
                                if (action3 instanceof TLRPC.TL_messageActionChatJoinedByLink) {
                                    s2 = LocaleController.formatString("NotificationInvitedToGroupByLink", 2131560011, s, chat2.title);
                                    return s2;
                                }
                                if (action3 instanceof TLRPC.TL_messageActionChatEditTitle) {
                                    s2 = LocaleController.formatString("NotificationEditedGroupName", 2131559999, s, action3.title);
                                    return s2;
                                }
                                if (!(action3 instanceof TLRPC.TL_messageActionChatEditPhoto) && !(action3 instanceof TLRPC.TL_messageActionChatDeletePhoto)) {
                                    if (action3 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                                        if (action3.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                            s2 = LocaleController.formatString("NotificationGroupKickYou", 2131560008, s, chat2.title);
                                            return s2;
                                        }
                                        if (messageObject.messageOwner.action.user_id == j) {
                                            s2 = LocaleController.formatString("NotificationGroupLeftMember", 2131560009, s, chat2.title);
                                            return s2;
                                        }
                                        final TLRPC.User user4 = MessagesController.getInstance(this.currentAccount).getUser(messageObject.messageOwner.action.user_id);
                                        if (user4 == null) {
                                            return null;
                                        }
                                        s2 = LocaleController.formatString("NotificationGroupKickMember", 2131560007, s, chat2.title, UserObject.getUserName(user4));
                                        return s2;
                                    }
                                    else {
                                        s2 = null;
                                        if (action3 instanceof TLRPC.TL_messageActionChatCreate) {
                                            s2 = messageObject.messageText.toString();
                                            return s2;
                                        }
                                        if (action3 instanceof TLRPC.TL_messageActionChannelCreate) {
                                            s2 = messageObject.messageText.toString();
                                            return s2;
                                        }
                                        if (action3 instanceof TLRPC.TL_messageActionChatMigrateTo) {
                                            s2 = LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, chat2.title);
                                            return s2;
                                        }
                                        if (action3 instanceof TLRPC.TL_messageActionChannelMigrateFrom) {
                                            s2 = LocaleController.formatString("ActionMigrateFromGroupNotify", 2131558525, action3.title);
                                            return s2;
                                        }
                                        if (action3 instanceof TLRPC.TL_messageActionScreenshotTaken) {
                                            s2 = messageObject.messageText.toString();
                                            return s2;
                                        }
                                        if (action3 instanceof TLRPC.TL_messageActionPinMessage) {
                                            if (chat2 != null && (!ChatObject.isChannel(chat2) || chat2.megagroup)) {
                                                final MessageObject replyMessageObject = messageObject.replyMessageObject;
                                                if (replyMessageObject == null) {
                                                    s2 = LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, s, chat2.title);
                                                    return s2;
                                                }
                                                if (replyMessageObject.isMusic()) {
                                                    s3 = LocaleController.formatString("NotificationActionPinnedMusic", 2131559977, s, chat2.title);
                                                    break Label_1657;
                                                }
                                                if (replyMessageObject.isVideo()) {
                                                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)replyMessageObject.messageOwner.message)) {
                                                        final StringBuilder sb6 = new StringBuilder();
                                                        sb6.append("\ud83d\udcf9 ");
                                                        sb6.append(replyMessageObject.messageOwner.message);
                                                        s3 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb6.toString(), chat2.title);
                                                        break Label_1657;
                                                    }
                                                    s3 = LocaleController.formatString("NotificationActionPinnedVideo", 2131559993, s, chat2.title);
                                                    break Label_1657;
                                                }
                                                else if (replyMessageObject.isGif()) {
                                                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)replyMessageObject.messageOwner.message)) {
                                                        final StringBuilder sb7 = new StringBuilder();
                                                        sb7.append("\ud83c\udfac ");
                                                        sb7.append(replyMessageObject.messageOwner.message);
                                                        s3 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb7.toString(), chat2.title);
                                                        break Label_1657;
                                                    }
                                                    s3 = LocaleController.formatString("NotificationActionPinnedGif", 2131559973, s, chat2.title);
                                                    break Label_1657;
                                                }
                                                else {
                                                    if (replyMessageObject.isVoice()) {
                                                        s3 = LocaleController.formatString("NotificationActionPinnedVoice", 2131559995, s, chat2.title);
                                                        break Label_1657;
                                                    }
                                                    if (replyMessageObject.isRoundVideo()) {
                                                        s3 = LocaleController.formatString("NotificationActionPinnedRound", 2131559985, s, chat2.title);
                                                        break Label_1657;
                                                    }
                                                    if (replyMessageObject.isSticker()) {
                                                        final String stickerEmoji2 = replyMessageObject.getStickerEmoji();
                                                        if (stickerEmoji2 != null) {
                                                            s3 = LocaleController.formatString("NotificationActionPinnedStickerEmoji", 2131559989, s, chat2.title, stickerEmoji2);
                                                            break Label_1657;
                                                        }
                                                        s3 = LocaleController.formatString("NotificationActionPinnedSticker", 2131559987, s, chat2.title);
                                                        break Label_1657;
                                                    }
                                                    else {
                                                        final TLRPC.Message messageOwner5 = replyMessageObject.messageOwner;
                                                        final TLRPC.MessageMedia media2 = messageOwner5.media;
                                                        if (media2 instanceof TLRPC.TL_messageMediaDocument) {
                                                            if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner5.message)) {
                                                                final StringBuilder sb8 = new StringBuilder();
                                                                sb8.append("\ud83d\udcce ");
                                                                sb8.append(replyMessageObject.messageOwner.message);
                                                                s3 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb8.toString(), chat2.title);
                                                                break Label_1657;
                                                            }
                                                            s3 = LocaleController.formatString("NotificationActionPinnedFile", 2131559963, s, chat2.title);
                                                            break Label_1657;
                                                        }
                                                        else {
                                                            if (media2 instanceof TLRPC.TL_messageMediaGeo || media2 instanceof TLRPC.TL_messageMediaVenue) {
                                                                s3 = LocaleController.formatString("NotificationActionPinnedGeo", 2131559969, s, chat2.title);
                                                                break Label_1657;
                                                            }
                                                            if (media2 instanceof TLRPC.TL_messageMediaGeoLive) {
                                                                s3 = LocaleController.formatString("NotificationActionPinnedGeoLive", 2131559971, s, chat2.title);
                                                                break Label_1657;
                                                            }
                                                            if (media2 instanceof TLRPC.TL_messageMediaContact) {
                                                                final TLRPC.TL_messageMediaContact tl_messageMediaContact2 = (TLRPC.TL_messageMediaContact)messageObject.messageOwner.media;
                                                                s3 = LocaleController.formatString("NotificationActionPinnedContact2", 2131559961, s, chat2.title, ContactsController.formatName(tl_messageMediaContact2.first_name, tl_messageMediaContact2.last_name));
                                                                break Label_1657;
                                                            }
                                                            if (media2 instanceof TLRPC.TL_messageMediaPoll) {
                                                                s3 = LocaleController.formatString("NotificationActionPinnedPoll2", 2131559983, s, chat2.title, ((TLRPC.TL_messageMediaPoll)media2).poll.question);
                                                                break Label_1657;
                                                            }
                                                            if (media2 instanceof TLRPC.TL_messageMediaPhoto) {
                                                                if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner5.message)) {
                                                                    final StringBuilder sb9 = new StringBuilder();
                                                                    sb9.append("\ud83d\uddbc ");
                                                                    sb9.append(replyMessageObject.messageOwner.message);
                                                                    s3 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, sb9.toString(), chat2.title);
                                                                    break Label_1657;
                                                                }
                                                                s3 = LocaleController.formatString("NotificationActionPinnedPhoto", 2131559981, s, chat2.title);
                                                                break Label_1657;
                                                            }
                                                            else {
                                                                if (media2 instanceof TLRPC.TL_messageMediaGame) {
                                                                    s3 = LocaleController.formatString("NotificationActionPinnedGame", 2131559965, s, chat2.title);
                                                                    break Label_1657;
                                                                }
                                                                final CharSequence messageText = replyMessageObject.messageText;
                                                                if (messageText != null && messageText.length() > 0) {
                                                                    CharSequence charSequence2;
                                                                    final CharSequence charSequence = charSequence2 = replyMessageObject.messageText;
                                                                    if (charSequence.length() > 20) {
                                                                        final StringBuilder sb10 = new StringBuilder();
                                                                        sb10.append((Object)charSequence.subSequence(0, 20));
                                                                        sb10.append("...");
                                                                        charSequence2 = sb10.toString();
                                                                    }
                                                                    s3 = LocaleController.formatString("NotificationActionPinnedText", 2131559991, s, charSequence2, chat2.title);
                                                                    break Label_1657;
                                                                }
                                                                s3 = LocaleController.formatString("NotificationActionPinnedNoText", 2131559979, s, chat2.title);
                                                                break Label_1657;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                final MessageObject replyMessageObject2 = messageObject.replyMessageObject;
                                                if (replyMessageObject2 == null) {
                                                    s2 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, chat2.title);
                                                    return s2;
                                                }
                                                if (replyMessageObject2.isMusic()) {
                                                    s3 = LocaleController.formatString("NotificationActionPinnedMusicChannel", 2131559978, chat2.title);
                                                    break Label_1657;
                                                }
                                                if (replyMessageObject2.isVideo()) {
                                                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)replyMessageObject2.messageOwner.message)) {
                                                        final StringBuilder sb11 = new StringBuilder();
                                                        sb11.append("\ud83d\udcf9 ");
                                                        sb11.append(replyMessageObject2.messageOwner.message);
                                                        s3 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat2.title, sb11.toString());
                                                        break Label_1657;
                                                    }
                                                    s3 = LocaleController.formatString("NotificationActionPinnedVideoChannel", 2131559994, chat2.title);
                                                    break Label_1657;
                                                }
                                                else if (replyMessageObject2.isGif()) {
                                                    if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)replyMessageObject2.messageOwner.message)) {
                                                        final StringBuilder sb12 = new StringBuilder();
                                                        sb12.append("\ud83c\udfac ");
                                                        sb12.append(replyMessageObject2.messageOwner.message);
                                                        s3 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat2.title, sb12.toString());
                                                        break Label_1657;
                                                    }
                                                    s3 = LocaleController.formatString("NotificationActionPinnedGifChannel", 2131559974, chat2.title);
                                                    break Label_1657;
                                                }
                                                else {
                                                    if (replyMessageObject2.isVoice()) {
                                                        s3 = LocaleController.formatString("NotificationActionPinnedVoiceChannel", 2131559996, chat2.title);
                                                        break Label_1657;
                                                    }
                                                    if (replyMessageObject2.isRoundVideo()) {
                                                        s3 = LocaleController.formatString("NotificationActionPinnedRoundChannel", 2131559986, chat2.title);
                                                        break Label_1657;
                                                    }
                                                    if (replyMessageObject2.isSticker()) {
                                                        final String stickerEmoji3 = replyMessageObject2.getStickerEmoji();
                                                        if (stickerEmoji3 != null) {
                                                            s3 = LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", 2131559990, chat2.title, stickerEmoji3);
                                                            break Label_1657;
                                                        }
                                                        s3 = LocaleController.formatString("NotificationActionPinnedStickerChannel", 2131559988, chat2.title);
                                                        break Label_1657;
                                                    }
                                                    else {
                                                        final TLRPC.Message messageOwner6 = replyMessageObject2.messageOwner;
                                                        final TLRPC.MessageMedia media3 = messageOwner6.media;
                                                        if (media3 instanceof TLRPC.TL_messageMediaDocument) {
                                                            if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner6.message)) {
                                                                final StringBuilder sb13 = new StringBuilder();
                                                                sb13.append("\ud83d\udcce ");
                                                                sb13.append(replyMessageObject2.messageOwner.message);
                                                                s3 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat2.title, sb13.toString());
                                                                break Label_1657;
                                                            }
                                                            s3 = LocaleController.formatString("NotificationActionPinnedFileChannel", 2131559964, chat2.title);
                                                            break Label_1657;
                                                        }
                                                        else {
                                                            if (media3 instanceof TLRPC.TL_messageMediaGeo || media3 instanceof TLRPC.TL_messageMediaVenue) {
                                                                s3 = LocaleController.formatString("NotificationActionPinnedGeoChannel", 2131559970, chat2.title);
                                                                break Label_1657;
                                                            }
                                                            if (media3 instanceof TLRPC.TL_messageMediaGeoLive) {
                                                                s3 = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", 2131559972, chat2.title);
                                                                break Label_1657;
                                                            }
                                                            if (media3 instanceof TLRPC.TL_messageMediaContact) {
                                                                final TLRPC.TL_messageMediaContact tl_messageMediaContact3 = (TLRPC.TL_messageMediaContact)messageObject.messageOwner.media;
                                                                s3 = LocaleController.formatString("NotificationActionPinnedContactChannel2", 2131559962, chat2.title, ContactsController.formatName(tl_messageMediaContact3.first_name, tl_messageMediaContact3.last_name));
                                                                break Label_1657;
                                                            }
                                                            if (media3 instanceof TLRPC.TL_messageMediaPoll) {
                                                                s3 = LocaleController.formatString("NotificationActionPinnedPollChannel2", 2131559984, chat2.title, ((TLRPC.TL_messageMediaPoll)media3).poll.question);
                                                                break Label_1657;
                                                            }
                                                            if (media3 instanceof TLRPC.TL_messageMediaPhoto) {
                                                                if (Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner6.message)) {
                                                                    final StringBuilder sb14 = new StringBuilder();
                                                                    sb14.append("\ud83d\uddbc ");
                                                                    sb14.append(replyMessageObject2.messageOwner.message);
                                                                    s3 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat2.title, sb14.toString());
                                                                    break Label_1657;
                                                                }
                                                                s3 = LocaleController.formatString("NotificationActionPinnedPhotoChannel", 2131559982, chat2.title);
                                                                break Label_1657;
                                                            }
                                                            else {
                                                                if (media3 instanceof TLRPC.TL_messageMediaGame) {
                                                                    s3 = LocaleController.formatString("NotificationActionPinnedGameChannel", 2131559966, chat2.title);
                                                                    break Label_1657;
                                                                }
                                                                final CharSequence messageText2 = replyMessageObject2.messageText;
                                                                if (messageText2 != null && messageText2.length() > 0) {
                                                                    CharSequence charSequence4;
                                                                    final CharSequence charSequence3 = charSequence4 = replyMessageObject2.messageText;
                                                                    if (charSequence3.length() > 20) {
                                                                        final StringBuilder sb15 = new StringBuilder();
                                                                        sb15.append((Object)charSequence3.subSequence(0, 20));
                                                                        sb15.append("...");
                                                                        charSequence4 = sb15.toString();
                                                                    }
                                                                    s3 = LocaleController.formatString("NotificationActionPinnedTextChannel", 2131559992, chat2.title, charSequence4);
                                                                    break Label_1657;
                                                                }
                                                                s3 = LocaleController.formatString("NotificationActionPinnedNoTextChannel", 2131559980, chat2.title);
                                                                break Label_1657;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            if (action3 instanceof TLRPC.TL_messageActionGameScore) {
                                                s2 = messageObject.messageText.toString();
                                                return s2;
                                            }
                                            return s2;
                                        }
                                    }
                                }
                                else {
                                    if (messageObject.messageOwner.to_id.channel_id != 0 && !chat2.megagroup) {
                                        s2 = LocaleController.formatString("ChannelPhotoEditNotification", 2131558987, chat2.title);
                                        return s2;
                                    }
                                    s2 = LocaleController.formatString("NotificationEditedGroupPhoto", 2131560000, s, chat2.title);
                                    return s2;
                                }
                            }
                        }
                        else {
                            s2 = null;
                            if (ChatObject.isChannel(chat2) && !chat2.megagroup) {
                                if (messageObject.isMediaEmpty()) {
                                    if (!b) {
                                        final String message = messageObject.messageOwner.message;
                                        if (message != null && message.length() != 0) {
                                            s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, messageObject.messageOwner.message);
                                            array[0] = true;
                                            return s2;
                                        }
                                    }
                                    s2 = LocaleController.formatString("ChannelMessageNoText", 2131558973, s);
                                    return s2;
                                }
                                final TLRPC.Message messageOwner7 = messageObject.messageOwner;
                                if (messageOwner7.media instanceof TLRPC.TL_messageMediaPhoto) {
                                    if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner7.message)) {
                                        final StringBuilder sb16 = new StringBuilder();
                                        sb16.append("\ud83d\uddbc ");
                                        sb16.append(messageObject.messageOwner.message);
                                        s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb16.toString());
                                        array[0] = true;
                                        return s2;
                                    }
                                    s2 = LocaleController.formatString("ChannelMessagePhoto", 2131558974, s);
                                    return s2;
                                }
                                else if (messageObject.isVideo()) {
                                    if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                        final StringBuilder sb17 = new StringBuilder();
                                        sb17.append("\ud83d\udcf9 ");
                                        sb17.append(messageObject.messageOwner.message);
                                        s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb17.toString());
                                        array[0] = true;
                                        return s2;
                                    }
                                    s2 = LocaleController.formatString("ChannelMessageVideo", 2131558979, s);
                                    return s2;
                                }
                                else {
                                    if (messageObject.isVoice()) {
                                        s2 = LocaleController.formatString("ChannelMessageAudio", 2131558965, s);
                                        return s2;
                                    }
                                    if (messageObject.isRoundVideo()) {
                                        s2 = LocaleController.formatString("ChannelMessageRound", 2131558976, s);
                                        return s2;
                                    }
                                    if (messageObject.isMusic()) {
                                        s2 = LocaleController.formatString("ChannelMessageMusic", 2131558972, s);
                                        return s2;
                                    }
                                    final TLRPC.MessageMedia media4 = messageObject.messageOwner.media;
                                    if (media4 instanceof TLRPC.TL_messageMediaContact) {
                                        final TLRPC.TL_messageMediaContact tl_messageMediaContact4 = (TLRPC.TL_messageMediaContact)media4;
                                        s2 = LocaleController.formatString("ChannelMessageContact2", 2131558966, s, ContactsController.formatName(tl_messageMediaContact4.first_name, tl_messageMediaContact4.last_name));
                                        return s2;
                                    }
                                    if (media4 instanceof TLRPC.TL_messageMediaPoll) {
                                        s2 = LocaleController.formatString("ChannelMessagePoll2", 2131558975, s, ((TLRPC.TL_messageMediaPoll)media4).poll.question);
                                        return s2;
                                    }
                                    if (media4 instanceof TLRPC.TL_messageMediaGeo || media4 instanceof TLRPC.TL_messageMediaVenue) {
                                        s2 = LocaleController.formatString("ChannelMessageMap", 2131558971, s);
                                        return s2;
                                    }
                                    if (media4 instanceof TLRPC.TL_messageMediaGeoLive) {
                                        s2 = LocaleController.formatString("ChannelMessageLiveLocation", 2131558970, s);
                                        return s2;
                                    }
                                    if (!(media4 instanceof TLRPC.TL_messageMediaDocument)) {
                                        return s2;
                                    }
                                    if (messageObject.isSticker()) {
                                        final String stickerEmoji4 = messageObject.getStickerEmoji();
                                        if (stickerEmoji4 != null) {
                                            s3 = LocaleController.formatString("ChannelMessageStickerEmoji", 2131558978, s, stickerEmoji4);
                                            break Label_1657;
                                        }
                                        s3 = LocaleController.formatString("ChannelMessageSticker", 2131558977, s);
                                        break Label_1657;
                                    }
                                    else if (messageObject.isGif()) {
                                        if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final StringBuilder sb18 = new StringBuilder();
                                            sb18.append("\ud83c\udfac ");
                                            sb18.append(messageObject.messageOwner.message);
                                            s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb18.toString());
                                            array[0] = true;
                                            return s2;
                                        }
                                        s2 = LocaleController.formatString("ChannelMessageGIF", 2131558969, s);
                                        return s2;
                                    }
                                    else {
                                        if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final StringBuilder sb19 = new StringBuilder();
                                            sb19.append("\ud83d\udcce ");
                                            sb19.append(messageObject.messageOwner.message);
                                            s2 = LocaleController.formatString("NotificationMessageText", 2131560051, s, sb19.toString());
                                            array[0] = true;
                                            return s2;
                                        }
                                        s2 = LocaleController.formatString("ChannelMessageDocument", 2131558967, s);
                                        return s2;
                                    }
                                }
                            }
                            else {
                                if (messageObject.isMediaEmpty()) {
                                    if (!b) {
                                        final String message2 = messageObject.messageOwner.message;
                                        if (message2 != null && message2.length() != 0) {
                                            s2 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, s, chat2.title, messageObject.messageOwner.message);
                                            return s2;
                                        }
                                    }
                                    s2 = LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, s, chat2.title);
                                    return s2;
                                }
                                final TLRPC.Message messageOwner8 = messageObject.messageOwner;
                                if (messageOwner8.media instanceof TLRPC.TL_messageMediaPhoto) {
                                    if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageOwner8.message)) {
                                        final String title = chat2.title;
                                        final StringBuilder sb20 = new StringBuilder();
                                        sb20.append("\ud83d\uddbc ");
                                        sb20.append(messageObject.messageOwner.message);
                                        s2 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, s, title, sb20.toString());
                                        return s2;
                                    }
                                    s2 = LocaleController.formatString("NotificationMessageGroupPhoto", 2131560032, s, chat2.title);
                                    return s2;
                                }
                                else if (messageObject.isVideo()) {
                                    if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                        final String title2 = chat2.title;
                                        final StringBuilder sb21 = new StringBuilder();
                                        sb21.append("\ud83d\udcf9 ");
                                        sb21.append(messageObject.messageOwner.message);
                                        s2 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, s, title2, sb21.toString());
                                        return s2;
                                    }
                                    s2 = LocaleController.formatString(" ", 2131560038, s, chat2.title);
                                    return s2;
                                }
                                else {
                                    if (messageObject.isVoice()) {
                                        s2 = LocaleController.formatString("NotificationMessageGroupAudio", 2131560021, s, chat2.title);
                                        return s2;
                                    }
                                    if (messageObject.isRoundVideo()) {
                                        s2 = LocaleController.formatString("NotificationMessageGroupRound", 2131560034, s, chat2.title);
                                        return s2;
                                    }
                                    if (messageObject.isMusic()) {
                                        s2 = LocaleController.formatString("NotificationMessageGroupMusic", 2131560030, s, chat2.title);
                                        return s2;
                                    }
                                    final TLRPC.MessageMedia media5 = messageObject.messageOwner.media;
                                    if (media5 instanceof TLRPC.TL_messageMediaContact) {
                                        final TLRPC.TL_messageMediaContact tl_messageMediaContact5 = (TLRPC.TL_messageMediaContact)media5;
                                        s2 = LocaleController.formatString("NotificationMessageGroupContact2", 2131560022, s, chat2.title, ContactsController.formatName(tl_messageMediaContact5.first_name, tl_messageMediaContact5.last_name));
                                        return s2;
                                    }
                                    if (media5 instanceof TLRPC.TL_messageMediaPoll) {
                                        s2 = LocaleController.formatString("NotificationMessageGroupPoll2", 2131560033, s, chat2.title, ((TLRPC.TL_messageMediaPoll)media5).poll.question);
                                        return s2;
                                    }
                                    if (media5 instanceof TLRPC.TL_messageMediaGame) {
                                        s2 = LocaleController.formatString("NotificationMessageGroupGame", 2131560024, s, chat2.title, media5.game.title);
                                        return s2;
                                    }
                                    if (media5 instanceof TLRPC.TL_messageMediaGeo || media5 instanceof TLRPC.TL_messageMediaVenue) {
                                        s2 = LocaleController.formatString("NotificationMessageGroupMap", 2131560029, s, chat2.title);
                                        return s2;
                                    }
                                    if (media5 instanceof TLRPC.TL_messageMediaGeoLive) {
                                        s2 = LocaleController.formatString("NotificationMessageGroupLiveLocation", 2131560028, s, chat2.title);
                                        return s2;
                                    }
                                    if (!(media5 instanceof TLRPC.TL_messageMediaDocument)) {
                                        return s2;
                                    }
                                    if (messageObject.isSticker()) {
                                        final String stickerEmoji5 = messageObject.getStickerEmoji();
                                        if (stickerEmoji5 != null) {
                                            s3 = LocaleController.formatString("NotificationMessageGroupStickerEmoji", 2131560036, s, chat2.title, stickerEmoji5);
                                            break Label_1657;
                                        }
                                        s3 = LocaleController.formatString("NotificationMessageGroupSticker", 2131560035, s, chat2.title);
                                        break Label_1657;
                                    }
                                    else if (messageObject.isGif()) {
                                        if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final String title3 = chat2.title;
                                            final StringBuilder sb22 = new StringBuilder();
                                            sb22.append("\ud83c\udfac ");
                                            sb22.append(messageObject.messageOwner.message);
                                            s2 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, s, title3, sb22.toString());
                                            return s2;
                                        }
                                        s2 = LocaleController.formatString("NotificationMessageGroupGif", 2131560026, s, chat2.title);
                                        return s2;
                                    }
                                    else {
                                        if (!b && Build$VERSION.SDK_INT >= 19 && !TextUtils.isEmpty((CharSequence)messageObject.messageOwner.message)) {
                                            final String title4 = chat2.title;
                                            final StringBuilder sb23 = new StringBuilder();
                                            sb23.append("\ud83d\udcce ");
                                            sb23.append(messageObject.messageOwner.message);
                                            s2 = LocaleController.formatString("NotificationMessageGroupText", 2131560037, s, title4, sb23.toString());
                                            return s2;
                                        }
                                        s2 = LocaleController.formatString("NotificationMessageGroupDocument", 2131560023, s, chat2.title);
                                        return s2;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (array2 != null) {
                            array2[0] = false;
                        }
                        if (ChatObject.isChannel(chat2) && !chat2.megagroup) {
                            s2 = LocaleController.formatString("ChannelMessageNoText", 2131558973, s);
                            return s2;
                        }
                        s2 = LocaleController.formatString("NotificationMessageGroupNoText", 2131560031, s, chat2.title);
                        return s2;
                    }
                }
                s2 = null;
                return s2;
            }
            s2 = s3;
        }
        return s2;
    }
    
    private int getTotalAllUnreadCount() {
        int i = 0;
        int unread_count = 0;
        while (i < 3) {
            int n = unread_count;
            Label_0255: {
                if (UserConfig.getInstance(i).isClientActivated()) {
                    Object instance = getInstance(i);
                    n = unread_count;
                    if (((NotificationsController)instance).showBadgeNumber) {
                        int n2 = 0;
                        Label_0251: {
                            Label_0237: {
                                if (((NotificationsController)instance).showBadgeMessages) {
                                    if (!((NotificationsController)instance).showBadgeMuted) {
                                        n2 = ((NotificationsController)instance).total_unread_count;
                                        break Label_0251;
                                    }
                                    try {
                                        final int size = MessagesController.getInstance(i).allDialogs.size();
                                        int index = 0;
                                        int n3 = unread_count;
                                        while (true) {
                                            unread_count = n3;
                                            if (index >= size) {
                                                break Label_0237;
                                            }
                                            try {
                                                instance = MessagesController.getInstance(i).allDialogs.get(index);
                                                unread_count = n3;
                                                if (((TLRPC.Dialog)instance).unread_count != 0) {
                                                    unread_count = ((TLRPC.Dialog)instance).unread_count;
                                                    unread_count += n3;
                                                }
                                                ++index;
                                                n3 = unread_count;
                                            }
                                            catch (Exception instance) {
                                                unread_count = n3;
                                            }
                                        }
                                    }
                                    catch (Exception ex) {}
                                    FileLog.e((Throwable)instance);
                                }
                                else {
                                    if (!((NotificationsController)instance).showBadgeMuted) {
                                        n2 = ((NotificationsController)instance).pushDialogs.size();
                                        break Label_0251;
                                    }
                                    try {
                                        final int size2 = MessagesController.getInstance(i).allDialogs.size();
                                        int index2 = 0;
                                        int n4 = unread_count;
                                        while (true) {
                                            unread_count = n4;
                                            if (index2 >= size2) {
                                                break Label_0237;
                                            }
                                            try {
                                                final int unread_count2 = MessagesController.getInstance(i).allDialogs.get(index2).unread_count;
                                                unread_count = n4;
                                                if (unread_count2 != 0) {
                                                    unread_count = n4 + 1;
                                                }
                                                ++index2;
                                                n4 = unread_count;
                                            }
                                            catch (Exception instance) {
                                                unread_count = n4;
                                            }
                                        }
                                    }
                                    catch (Exception ex2) {}
                                    FileLog.e((Throwable)instance);
                                }
                            }
                            n = unread_count;
                            break Label_0255;
                        }
                        n = unread_count + n2;
                    }
                }
            }
            ++i;
            unread_count = n;
        }
        return unread_count;
    }
    
    private boolean isEmptyVibration(final long[] array) {
        if (array != null && array.length != 0) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i] != 0L) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean isPersonalMessage(final MessageObject messageObject) {
        final TLRPC.Message messageOwner = messageObject.messageOwner;
        final TLRPC.Peer to_id = messageOwner.to_id;
        if (to_id != null && to_id.chat_id == 0 && to_id.channel_id == 0) {
            final TLRPC.MessageAction action = messageOwner.action;
            if (action == null || action instanceof TLRPC.TL_messageActionEmpty) {
                return true;
            }
        }
        return false;
    }
    
    @TargetApi(28)
    private void loadRoundAvatar(final File file, final Person.Builder builder) {
        if (file == null) {
            return;
        }
        try {
            builder.setIcon(IconCompat.createWithBitmap(ImageDecoder.decodeBitmap(ImageDecoder.createSource(file), (ImageDecoder$OnHeaderDecodedListener)_$$Lambda$NotificationsController$N5IA2yCFiGMc2IXHr3hVgVbBFF8.INSTANCE)));
        }
        catch (Throwable t) {}
    }
    
    private void playInChatSound() {
        if (this.inChatSoundEnabled) {
            if (!MediaController.getInstance().isRecordingAudio()) {
                try {
                    if (NotificationsController.audioManager.getRingerMode() == 0) {
                        return;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                try {
                    if (this.getNotifyOverride(MessagesController.getNotificationsSettings(this.currentAccount), this.opened_dialog_id) == 2) {
                        return;
                    }
                    NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$51wmHPlGOlC0_zQ9GY7w7j4BjsE(this));
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
        }
    }
    
    private void scheduleNotificationDelay(final boolean b) {
        try {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("delay notification start, onlineReason = ");
                sb.append(b);
                FileLog.d(sb.toString());
            }
            this.notificationDelayWakelock.acquire(10000L);
            NotificationsController.notificationsQueue.cancelRunnable(this.notificationDelayRunnable);
            final DispatchQueue notificationsQueue = NotificationsController.notificationsQueue;
            final Runnable notificationDelayRunnable = this.notificationDelayRunnable;
            int n;
            if (b) {
                n = 3000;
            }
            else {
                n = 1000;
            }
            notificationsQueue.postRunnable(notificationDelayRunnable, n);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.showOrUpdateNotification(this.notifyCheck);
        }
    }
    
    private void scheduleNotificationRepeat() {
        try {
            final Intent intent = new Intent(ApplicationLoader.applicationContext, (Class)NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            final PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, 0);
            final int int1 = MessagesController.getNotificationsSettings(this.currentAccount).getInt("repeat_messages", 60);
            if (int1 > 0 && this.personal_count > 0) {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + int1 * 60 * 1000, service);
            }
            else {
                this.alarmManager.cancel(service);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void setBadge(final int lastBadgeCount) {
        if (this.lastBadgeCount == lastBadgeCount) {
            return;
        }
        NotificationBadge.applyCount(this.lastBadgeCount = lastBadgeCount);
    }
    
    @SuppressLint({ "InlinedApi" })
    private void showExtraNotifications(NotificationCompat.Builder builder, boolean megagroup, String s) {
        final Notification build = builder.build();
        if (Build$VERSION.SDK_INT < 18) {
            NotificationsController.notificationManager.notify(this.notificationId, build);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("show summary notification by SDK check");
            }
            return;
        }
        final ArrayList<Long> list = new ArrayList<Long>();
        final LongSparseArray longSparseArray = new LongSparseArray();
        for (int i = 0; i < this.pushMessages.size(); ++i) {
            final Object e = this.pushMessages.get(i);
            final long n = ((MessageObject)e).getDialogId();
            ArrayList<MessageObject> list2;
            if ((list2 = (ArrayList<MessageObject>)longSparseArray.get(n)) == null) {
                list2 = new ArrayList<MessageObject>();
                longSparseArray.put(n, (Object)list2);
                list.add(0, n);
            }
            list2.add((MessageObject)e);
        }
        final LongSparseArray clone = this.wearNotificationsIds.clone();
        this.wearNotificationsIds.clear();
        ArrayList<TLRPC.TL_keyboardButtonRow> list3 = new ArrayList<TLRPC.TL_keyboardButtonRow>();
        int j = Build$VERSION.SDK_INT;
        int n2;
        if (j > 27 && (j <= 27 || list.size() <= 1)) {
            n2 = 0;
        }
        else {
            n2 = 1;
        }
        if (n2 != 0 && Build$VERSION.SDK_INT >= 26) {
            checkOtherNotificationsChannel();
        }
        int size = list.size();
        int size2 = 0;
    Label_1215_Outer:
        while (true) {
            Label_4121: {
                if (size2 >= size) {
                    break Label_4121;
                }
                final long n = list.get(size2);
                final ArrayList list4 = (ArrayList)longSparseArray.get(n);
                int k = list4.get(0).getId();
                int l = (int)n;
                int size3 = (int)(n >> 32);
                Object e = clone.get(n);
                if (e == null) {
                    if (l != 0) {
                        e = l;
                    }
                    else {
                        e = size3;
                    }
                }
                else {
                    clone.remove(n);
                }
                final MessageObject messageObject = list4.get(0);
                j = messageObject.messageOwner.date;
                Object activity = new LongSparseArray();
                Label_4111: {
                    Object contentTitle = null;
                    Object o = null;
                    TLRPC.Chat chat = null;
                    int n5 = 0;
                    TLRPC.FileLocation fileLocation3 = null;
                    Label_1039: {
                        TLRPC.FileLocation fileLocation = null;
                        Object o2 = null;
                        Label_1023: {
                            Label_0913: {
                                if (l == 0) {
                                    NotificationCompat.Action.Builder builder2;
                                    if (n != NotificationsController.globalSecretChatId) {
                                        final TLRPC.EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(size3);
                                        if (encryptedChat == null) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                final StringBuilder sb = new StringBuilder();
                                                sb.append("not found secret chat to show dialog notification ");
                                                sb.append(size3);
                                                FileLog.w(sb.toString());
                                            }
                                            break Label_0913;
                                        }
                                        else {
                                            contentTitle = MessagesController.getInstance(this.currentAccount).getUser(encryptedChat.user_id);
                                            if ((builder2 = (NotificationCompat.Action.Builder)contentTitle) == null) {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    final StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("not found secret chat user to show dialog notification ");
                                                    sb2.append(encryptedChat.user_id);
                                                    FileLog.w(sb2.toString());
                                                }
                                                break Label_0913;
                                            }
                                        }
                                    }
                                    else {
                                        builder2 = null;
                                    }
                                    final String string = LocaleController.getString("SecretChatName", 2131560671);
                                    fileLocation = null;
                                    j = 0;
                                    o = builder2;
                                    o2 = string;
                                    break Label_1023;
                                }
                                if (l != 777000) {
                                    j = 1;
                                }
                                else {
                                    j = 0;
                                }
                                if (l > 0) {
                                    final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(l);
                                    Label_0581: {
                                        if (user == null) {
                                            if (!messageObject.isFcmMessage()) {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    final StringBuilder sb3 = new StringBuilder();
                                                    sb3.append("not found user to show dialog notification ");
                                                    sb3.append(l);
                                                    FileLog.w(sb3.toString());
                                                }
                                                break Label_0913;
                                            }
                                            final String localName = messageObject.localName;
                                            o = user;
                                            o2 = localName;
                                        }
                                        else {
                                            final String userName = UserObject.getUserName(user);
                                            final TLRPC.UserProfilePhoto photo = user.photo;
                                            if (photo != null) {
                                                final TLRPC.FileLocation photo_small = photo.photo_small;
                                                if (photo_small != null && photo_small.volume_id != 0L && photo_small.local_id != 0) {
                                                    o = user;
                                                    o2 = userName;
                                                    fileLocation = photo_small;
                                                    break Label_0581;
                                                }
                                            }
                                            o = user;
                                            o2 = userName;
                                        }
                                        fileLocation = null;
                                    }
                                    break Label_1023;
                                }
                                chat = MessagesController.getInstance(this.currentAccount).getChat(-l);
                                TLRPC.FileLocation fileLocation2 = null;
                                Label_0646: {
                                    boolean megagroup2 = false;
                                    Label_0640: {
                                        int localChannel;
                                        if (chat == null) {
                                            if (messageObject.isFcmMessage()) {
                                                megagroup2 = messageObject.isMegagroup();
                                                contentTitle = messageObject.localName;
                                                localChannel = (messageObject.localChannel ? 1 : 0);
                                            }
                                            else {
                                                if (BuildVars.LOGS_ENABLED) {
                                                    final StringBuilder sb4 = new StringBuilder();
                                                    sb4.append("not found chat to show dialog notification ");
                                                    sb4.append(l);
                                                    FileLog.w(sb4.toString());
                                                }
                                                break Label_0913;
                                            }
                                        }
                                        else {
                                            megagroup = chat.megagroup;
                                            int n3;
                                            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                                                n3 = 1;
                                            }
                                            else {
                                                n3 = 0;
                                            }
                                            final String title = chat.title;
                                            final TLRPC.ChatPhoto photo2 = chat.photo;
                                            localChannel = n3;
                                            contentTitle = title;
                                            megagroup2 = megagroup;
                                            if (photo2 != null) {
                                                final TLRPC.FileLocation photo_small2 = photo2.photo_small;
                                                localChannel = n3;
                                                contentTitle = title;
                                                megagroup2 = megagroup;
                                                if (photo_small2 != null) {
                                                    final int n4 = n3;
                                                    contentTitle = title;
                                                    megagroup2 = megagroup;
                                                    n5 = n4;
                                                    if (photo_small2.volume_id == 0L) {
                                                        break Label_0640;
                                                    }
                                                    contentTitle = title;
                                                    megagroup2 = megagroup;
                                                    n5 = n4;
                                                    if (photo_small2.local_id != 0) {
                                                        contentTitle = title;
                                                        fileLocation2 = photo_small2;
                                                        n5 = n4;
                                                        break Label_0646;
                                                    }
                                                    break Label_0640;
                                                }
                                            }
                                        }
                                        n5 = localChannel;
                                    }
                                    fileLocation2 = null;
                                    megagroup = megagroup2;
                                }
                                o = null;
                                fileLocation3 = fileLocation2;
                                break Label_1039;
                            }
                            final ArrayList<TLRPC.TL_keyboardButtonRow> list5 = list3;
                            break Label_4111;
                        }
                        megagroup = false;
                        chat = null;
                        n5 = 0;
                        fileLocation3 = fileLocation;
                        contentTitle = o2;
                    }
                    if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
                        contentTitle = LocaleController.getString("AppName", 2131558635);
                        fileLocation3 = null;
                        j = 0;
                    }
                    Label_1208: {
                        if (fileLocation3 == null) {
                            break Label_1208;
                        }
                        Object o3 = FileLoader.getPathToAttach(fileLocation3, true);
                        final BitmapDrawable imageFromMemory = ImageLoader.getInstance().getImageFromMemory(fileLocation3, null, "50_50");
                    Label_1198_Outer:
                        while (true) {
                            if (imageFromMemory != null) {
                                final Object o4 = imageFromMemory.getBitmap();
                                final Object o5 = o3;
                                break Label_1215;
                            }
                            ArrayList<TLRPC.TL_keyboardButtonRow> list5;
                            Object o4;
                            Object o5;
                            float n6;
                            BitmapFactory$Options bitmapFactory$Options;
                            int inSampleSize;
                            String s2;
                            PendingIntent broadcast;
                            RemoteInput build2;
                            Integer n7;
                            int n8;
                            Object largeIcon;
                            Object o6;
                            Integer value;
                            Object format;
                            NotificationCompat.MessagingStyle style;
                            String[] array;
                            boolean[] array2;
                            int index;
                            NotificationCompat.Action.Builder builder3;
                            MessageObject messageObject2;
                            String shortStringForMessage;
                            int id;
                            long n9;
                            long n10 = 0L;
                            int fromId;
                            Person build3;
                            Person.Builder builder4;
                            int fromId2;
                            TLRPC.User userSync;
                            String s3;
                            NotificationCompat.MessagingStyle.Message message;
                            String s4;
                            Uri$Builder appendPath;
                            StringBuilder sb5;
                            List<NotificationCompat.MessagingStyle.Message> messages;
                            TLRPC.ReplyMarkup reply_markup;
                            Intent intent;
                            NotificationCompat.WearableExtender wearableExtender;
                            Intent intent2;
                            PendingIntent broadcast2;
                            String s5;
                            NotificationCompat.Action.Builder builder5;
                            NotificationCompat.Action build4;
                            StringBuilder sb6;
                            String s6;
                            StringBuilder sb7;
                            StringBuilder sb8;
                            NotificationCompat.WearableExtender wearableExtender2;
                            StringBuilder sb9;
                            StringBuilder sb10;
                            long when;
                            NotificationCompat.Builder builder6;
                            StringBuilder sb11;
                            StringBuilder sb12;
                            int n11;
                            int n12;
                            String s7;
                            TLRPC.KeyboardButton keyboardButton;
                            String text;
                            String phone;
                            StringBuilder sb13;
                            StringBuilder sb14;
                            Block_124_Outer:Label_4202_Outer:
                            while (true) {
                                if (Build$VERSION.SDK_INT >= 28) {
                                    break Label_1198;
                                }
                                try {
                                    if (((File)o3).exists()) {
                                        n6 = 160.0f / AndroidUtilities.dp(50.0f);
                                        bitmapFactory$Options = new BitmapFactory$Options();
                                        if (n6 < 1.0f) {
                                            inSampleSize = 1;
                                        }
                                        else {
                                            inSampleSize = (int)n6;
                                        }
                                        bitmapFactory$Options.inSampleSize = inSampleSize;
                                        o4 = BitmapFactory.decodeFile(((File)o3).getAbsolutePath(), bitmapFactory$Options);
                                        o5 = o3;
                                    }
                                    else {
                                        o4 = null;
                                        o5 = o3;
                                    }
                                    s2 = "dialog_id";
                                    if ((n5 == 0 || megagroup) && j != 0 && !SharedConfig.isWaitingForPasscodeEnter) {
                                        o3 = new Intent(ApplicationLoader.applicationContext, (Class)WearReplyReceiver.class);
                                        ((Intent)o3).putExtra("dialog_id", n);
                                        ((Intent)o3).putExtra("max_id", k);
                                        ((Intent)o3).putExtra("currentAccount", this.currentAccount);
                                        broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, (int)e, (Intent)o3, 134217728);
                                        o3 = new RemoteInput.Builder("extra_voice_reply");
                                        ((RemoteInput.Builder)o3).setLabel(LocaleController.getString("Reply", 2131560565));
                                        build2 = ((RemoteInput.Builder)o3).build();
                                        if (l < 0) {
                                            o3 = LocaleController.formatString("ReplyToGroup", 2131560566, contentTitle);
                                        }
                                        else {
                                            o3 = LocaleController.formatString("ReplyToUser", 2131560567, contentTitle);
                                        }
                                        o3 = new NotificationCompat.Action.Builder(2131165467, (CharSequence)o3, broadcast);
                                        ((NotificationCompat.Action.Builder)o3).setAllowGeneratedReplies(true);
                                        ((NotificationCompat.Action.Builder)o3).setSemanticAction(1);
                                        ((NotificationCompat.Action.Builder)o3).addRemoteInput(build2);
                                        ((NotificationCompat.Action.Builder)o3).setShowsUserInterface(false);
                                        o3 = ((NotificationCompat.Action.Builder)o3).build();
                                    }
                                    else {
                                        o3 = null;
                                    }
                                    n7 = (Integer)e;
                                    n8 = n2;
                                    largeIcon = o4;
                                    o6 = o;
                                    e = (value = (Integer)this.pushDialogs.get(n));
                                    if (e == null) {
                                        value = 0;
                                    }
                                    j = Math.max(value, list4.size());
                                    if (j > 1 && Build$VERSION.SDK_INT < 28) {
                                        format = String.format("%1$s (%2$d)", contentTitle, j);
                                    }
                                    else {
                                        format = contentTitle;
                                    }
                                    style = new NotificationCompat.MessagingStyle("");
                                    if (Build$VERSION.SDK_INT < 28 || (l < 0 && n5 == 0)) {
                                        style.setConversationTitle((CharSequence)format);
                                    }
                                    if (Build$VERSION.SDK_INT >= 28 && (n5 != 0 || l >= 0)) {
                                        megagroup = false;
                                    }
                                    else {
                                        megagroup = true;
                                    }
                                    style.setGroupConversation(megagroup);
                                    e = new StringBuilder();
                                    array = new String[] { null };
                                    array2 = new boolean[] { false };
                                    index = list4.size() - 1;
                                    o = null;
                                    j = 0;
                                    builder3 = (NotificationCompat.Action.Builder)o3;
                                    o3 = e;
                                    while (index >= 0) {
                                        messageObject2 = list4.get(index);
                                        shortStringForMessage = this.getShortStringForMessage(messageObject2, array, array2);
                                        if (shortStringForMessage == null) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                e = new StringBuilder();
                                                ((StringBuilder)e).append("message text is null for ");
                                                ((StringBuilder)e).append(messageObject2.getId());
                                                ((StringBuilder)e).append(" did = ");
                                                ((StringBuilder)e).append(messageObject2.getDialogId());
                                                FileLog.w(((StringBuilder)e).toString());
                                            }
                                            e = o;
                                            id = j;
                                        }
                                        else {
                                            n9 = n;
                                            if (((StringBuilder)o3).length() > 0) {
                                                ((StringBuilder)o3).append("\n\n");
                                            }
                                            if (array[0] != null) {
                                                ((StringBuilder)o3).append(String.format("%1$s: %2$s", array[0], shortStringForMessage));
                                            }
                                            else {
                                                ((StringBuilder)o3).append(shortStringForMessage);
                                            }
                                            Label_1899: {
                                                if (l > 0) {
                                                    n10 = l;
                                                }
                                                else {
                                                    if (n5 != 0) {
                                                        fromId = -l;
                                                    }
                                                    else {
                                                        if (l >= 0) {
                                                            n10 = n9;
                                                            break Label_1899;
                                                        }
                                                        fromId = messageObject2.getFromId();
                                                    }
                                                    n10 = fromId;
                                                }
                                            }
                                            build3 = (Person)((LongSparseArray)activity).get(n10);
                                            if (build3 == null) {
                                                builder4 = new Person.Builder();
                                                if (array[0] == null) {
                                                    e = "";
                                                }
                                                else {
                                                    e = array[0];
                                                }
                                                builder4.setName((CharSequence)e);
                                                if (array2[0] && l != 0) {
                                                    if (Build$VERSION.SDK_INT >= 28) {
                                                        Label_2133: {
                                                            if (l <= 0 && n5 == 0) {
                                                                if (l < 0) {
                                                                    fromId2 = messageObject2.getFromId();
                                                                    if ((e = MessagesController.getInstance(this.currentAccount).getUser(fromId2)) == null) {
                                                                        userSync = MessagesStorage.getInstance(this.currentAccount).getUserSync(fromId2);
                                                                        if ((e = userSync) != null) {
                                                                            MessagesController.getInstance(this.currentAccount).putUser(userSync, true);
                                                                            e = userSync;
                                                                        }
                                                                    }
                                                                    if (e != null) {
                                                                        e = ((TLRPC.User)e).photo;
                                                                        if (e != null) {
                                                                            e = ((TLRPC.UserProfilePhoto)e).photo_small;
                                                                            if (e != null && ((TLRPC.FileLocation)e).volume_id != 0L && ((TLRPC.FileLocation)e).local_id != 0) {
                                                                                e = FileLoader.getPathToAttach((TLObject)e, true);
                                                                                break Label_2133;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                e = null;
                                                            }
                                                            else {
                                                                e = o5;
                                                            }
                                                        }
                                                        this.loadRoundAvatar((File)e, builder4);
                                                    }
                                                }
                                                build3 = builder4.build();
                                                ((LongSparseArray)activity).put(n10, (Object)build3);
                                            }
                                            s3 = shortStringForMessage;
                                            if (l != 0) {
                                                if (Build$VERSION.SDK_INT >= 28 && !((ActivityManager)ApplicationLoader.applicationContext.getSystemService("activity")).isLowRamDevice()) {
                                                    if (messageObject2.type != 1 && !messageObject2.isSticker()) {
                                                        style.addMessage(s3, messageObject2.messageOwner.date * 1000L, build3);
                                                    }
                                                    else {
                                                        e = FileLoader.getPathToMessage(messageObject2.messageOwner);
                                                        message = new NotificationCompat.MessagingStyle.Message(s3, messageObject2.messageOwner.date * 1000L, build3);
                                                        if (messageObject2.isSticker()) {
                                                            s4 = "image/webp";
                                                        }
                                                        else {
                                                            s4 = "image/jpeg";
                                                        }
                                                        if (((File)e).exists()) {
                                                            e = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.provider", (File)e);
                                                        }
                                                        else if (FileLoader.getInstance(this.currentAccount).isLoadingFile(((File)e).getName())) {
                                                            appendPath = new Uri$Builder().scheme("content").authority("org.telegram.messenger.notification_image_provider").appendPath("msg_media_raw");
                                                            sb5 = new StringBuilder();
                                                            sb5.append(this.currentAccount);
                                                            sb5.append("");
                                                            e = appendPath.appendPath(sb5.toString()).appendPath(((File)e).getName()).appendQueryParameter("final_path", ((File)e).getAbsolutePath()).build();
                                                        }
                                                        else {
                                                            e = null;
                                                        }
                                                        if (e != null) {
                                                            message.setData(s4, (Uri)e);
                                                            style.addMessage(message);
                                                            ApplicationLoader.applicationContext.grantUriPermission("com.android.systemui", (Uri)e, 1);
                                                            AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsController$hROO1aIM4eduzMv5uJ3U4yL97Bo((Uri)e), 20000L);
                                                            if (!TextUtils.isEmpty(messageObject2.caption)) {
                                                                style.addMessage(messageObject2.caption, messageObject2.messageOwner.date * 1000L, build3);
                                                            }
                                                        }
                                                        else {
                                                            style.addMessage(s3, messageObject2.messageOwner.date * 1000L, build3);
                                                        }
                                                    }
                                                }
                                                else {
                                                    style.addMessage(s3, messageObject2.messageOwner.date * 1000L, build3);
                                                }
                                                if (messageObject2.isVoice()) {
                                                    messages = style.getMessages();
                                                    if (!messages.isEmpty()) {
                                                        e = FileLoader.getPathToMessage(messageObject2.messageOwner);
                                                        if (Build$VERSION.SDK_INT >= 24) {
                                                            try {
                                                                e = FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.provider", (File)e);
                                                            }
                                                            catch (Exception e) {
                                                                e = null;
                                                            }
                                                        }
                                                        else {
                                                            e = Uri.fromFile((File)e);
                                                        }
                                                        if (e != null) {
                                                            ((NotificationCompat.MessagingStyle.Message)messages.get(messages.size() - 1)).setData("audio/ogg", (Uri)e);
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                style.addMessage(s3, messageObject2.messageOwner.date * 1000L, build3);
                                            }
                                            e = o;
                                            id = j;
                                            if (n9 == 777000L) {
                                                reply_markup = messageObject2.messageOwner.reply_markup;
                                                e = o;
                                                id = j;
                                                if (reply_markup != null) {
                                                    e = reply_markup.rows;
                                                    id = messageObject2.getId();
                                                }
                                            }
                                        }
                                        --index;
                                        o = e;
                                        j = id;
                                    }
                                    intent = new Intent(ApplicationLoader.applicationContext, (Class)LaunchActivity.class);
                                    e = new StringBuilder();
                                    ((StringBuilder)e).append("com.tmessages.openchat");
                                    ((StringBuilder)e).append(Math.random());
                                    ((StringBuilder)e).append(Integer.MAX_VALUE);
                                    intent.setAction(((StringBuilder)e).toString());
                                    intent.setFlags(32768);
                                    intent.addCategory("android.intent.category.LAUNCHER");
                                    if (l != 0) {
                                        if (l > 0) {
                                            intent.putExtra("userId", l);
                                        }
                                        else {
                                            intent.putExtra("chatId", -l);
                                        }
                                    }
                                    else {
                                        intent.putExtra("encId", size3);
                                    }
                                    intent.putExtra("currentAccount", this.currentAccount);
                                    activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824);
                                    wearableExtender = new NotificationCompat.WearableExtender();
                                    if (builder3 != null) {
                                        wearableExtender.addAction((NotificationCompat.Action)builder3);
                                    }
                                    e = builder3;
                                    intent2 = new Intent(ApplicationLoader.applicationContext, (Class)AutoMessageHeardReceiver.class);
                                    intent2.addFlags(32);
                                    intent2.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                                    intent2.putExtra(s2, n);
                                    intent2.putExtra("max_id", k);
                                    intent2.putExtra("currentAccount", this.currentAccount);
                                    broadcast2 = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, (int)n7, intent2, 134217728);
                                    s5 = "currentAccount";
                                    builder5 = new NotificationCompat.Action.Builder(2131165591, LocaleController.getString("MarkAsRead", 2131559807), broadcast2);
                                    builder5.setSemanticAction(2);
                                    builder5.setShowsUserInterface(false);
                                    build4 = builder5.build();
                                    if (l != 0) {
                                        if (l > 0) {
                                            sb6 = new StringBuilder();
                                            sb6.append("tguser");
                                            sb6.append(l);
                                            sb6.append("_");
                                            sb6.append(k);
                                            s6 = sb6.toString();
                                        }
                                        else {
                                            sb7 = new StringBuilder();
                                            sb7.append("tgchat");
                                            sb7.append(-l);
                                            sb7.append("_");
                                            sb7.append(k);
                                            s6 = sb7.toString();
                                        }
                                    }
                                    else if (n != NotificationsController.globalSecretChatId) {
                                        sb8 = new StringBuilder();
                                        sb8.append("tgenc");
                                        sb8.append(size3);
                                        sb8.append("_");
                                        sb8.append(k);
                                        s6 = sb8.toString();
                                    }
                                    else {
                                        s6 = null;
                                    }
                                    if (s6 != null) {
                                        wearableExtender.setDismissalId(s6);
                                        wearableExtender2 = new NotificationCompat.WearableExtender();
                                        sb9 = new StringBuilder();
                                        sb9.append("summary_");
                                        sb9.append(s6);
                                        wearableExtender2.setDismissalId(sb9.toString());
                                        builder.extend(wearableExtender2);
                                    }
                                    sb10 = new StringBuilder();
                                    sb10.append("tgaccount");
                                    sb10.append(UserConfig.getInstance(this.currentAccount).getClientUserId());
                                    wearableExtender.setBridgeTag(sb10.toString());
                                    when = list4.get(0).messageOwner.date * 1000L;
                                    builder6 = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
                                    builder6.setContentTitle((CharSequence)contentTitle);
                                    builder6.setSmallIcon(2131165698);
                                    builder6.setContentText(((StringBuilder)o3).toString());
                                    builder6.setAutoCancel(true);
                                    builder6.setNumber(list4.size());
                                    builder6.setColor(-15618822);
                                    builder6.setGroupSummary(false);
                                    builder6.setWhen(when);
                                    builder6.setShowWhen(true);
                                    sb11 = new StringBuilder();
                                    sb11.append("sdid_");
                                    sb11.append(n);
                                    builder6.setShortcutId(sb11.toString());
                                    builder6.setStyle(style);
                                    builder6.setContentIntent((PendingIntent)activity);
                                    builder6.extend(wearableExtender);
                                    sb12 = new StringBuilder();
                                    sb12.append("");
                                    sb12.append(Long.MAX_VALUE - when);
                                    builder6.setSortKey(sb12.toString());
                                    builder6.setCategory("msg");
                                    if (n8 != 0) {
                                        builder6.setGroup(this.notificationGroup);
                                        builder6.setGroupAlertBehavior(1);
                                    }
                                    if (e != null) {
                                        builder6.addAction((NotificationCompat.Action)e);
                                    }
                                    builder6.addAction(build4);
                                    if (this.pushDialogs.size() == 1 && !TextUtils.isEmpty((CharSequence)s)) {
                                        builder6.setSubText(s);
                                    }
                                    if (l == 0) {
                                        builder6.setLocalOnly(true);
                                    }
                                    if (largeIcon != null && Build$VERSION.SDK_INT < 28) {
                                        builder6.setLargeIcon((Bitmap)largeIcon);
                                    }
                                    if (!AndroidUtilities.needShowPasscode(false) && !SharedConfig.isWaitingForPasscodeEnter && o != null) {
                                        k = ((ArrayList)o).size();
                                        n11 = 0;
                                        n12 = j;
                                        s7 = s5;
                                        for (j = n11; j < k; ++j) {
                                            e = ((ArrayList<TLRPC.TL_keyboardButtonRow>)o).get(j);
                                            size3 = ((TLRPC.TL_keyboardButtonRow)e).buttons.size();
                                            for (int index2 = 0; index2 < size3; ++index2) {
                                                keyboardButton = ((TLRPC.TL_keyboardButtonRow)e).buttons.get(index2);
                                                if (keyboardButton instanceof TLRPC.TL_keyboardButtonCallback) {
                                                    contentTitle = new Intent(ApplicationLoader.applicationContext, (Class)NotificationCallbackReceiver.class);
                                                    ((Intent)contentTitle).putExtra(s7, this.currentAccount);
                                                    ((Intent)contentTitle).putExtra("did", n);
                                                    o3 = keyboardButton.data;
                                                    if (o3 != null) {
                                                        ((Intent)contentTitle).putExtra("data", (byte[])o3);
                                                    }
                                                    ((Intent)contentTitle).putExtra("mid", n12);
                                                    text = keyboardButton.text;
                                                    o3 = ApplicationLoader.applicationContext;
                                                    l = this.lastButtonId++;
                                                    builder6.addAction(0, text, PendingIntent.getBroadcast((Context)o3, l, (Intent)contentTitle, 134217728));
                                                }
                                            }
                                        }
                                    }
                                    if (chat == null && o6 != null) {
                                        phone = ((TLRPC.User)o6).phone;
                                        if (phone != null && phone.length() > 0) {
                                            sb13 = new StringBuilder();
                                            sb13.append("tel:+");
                                            sb13.append(((TLRPC.User)o6).phone);
                                            builder6.addPerson(sb13.toString());
                                        }
                                    }
                                    if (Build$VERSION.SDK_INT >= 26) {
                                        if (n8 != 0) {
                                            builder6.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
                                        }
                                        else {
                                            builder6.setChannelId(build.getChannelId());
                                        }
                                    }
                                    e = new NotificationHolder(n7, builder6.build());
                                    list5 = list3;
                                    list5.add((TLRPC.TL_keyboardButtonRow)e);
                                    this.wearNotificationsIds.put(n, (Object)n7);
                                    ++size2;
                                    list3 = list5;
                                    continue Label_1215_Outer;
                                    Label_4309: {
                                        return;
                                    }
                                    // iftrue(Label_4167:, !BuildVars.LOGS_ENABLED)
                                    // iftrue(Label_4182:, n2 == 0)
                                    // iftrue(Label_4309:, n2 >= builder.size())
                                    // iftrue(Label_4235:, j >= size2)
                                    // iftrue(Label_4293:, !BuildVars.LOGS_ENABLED)
                                    Label_4202:Label_4293_Outer:
                                    while (true) {
                                        Label_4192: {
                                            Label_4167: {
                                                while (true) {
                                                Block_125:
                                                    while (true) {
                                                        while (true) {
                                                            while (true) {
                                                                builder = (NotificationCompat.Builder)new StringBuilder();
                                                                ((StringBuilder)builder).append("show summary with id ");
                                                                ((StringBuilder)builder).append(this.notificationId);
                                                                FileLog.d(((StringBuilder)builder).toString());
                                                                break Label_4167;
                                                                ((NotificationHolder)list3.get(j)).call();
                                                                ++j;
                                                                break Label_4202;
                                                                o4 = (o5 = null);
                                                                continue Label_1198_Outer;
                                                                continue Block_124_Outer;
                                                            }
                                                            size = 0;
                                                            continue Label_4293_Outer;
                                                        }
                                                        NotificationsController.notificationManager.cancel((int)s);
                                                        ++n2;
                                                        Label_4235:
                                                        break Block_125;
                                                        Label_4182:
                                                        NotificationsController.notificationManager.cancel(this.notificationId);
                                                        break Label_4192;
                                                        n2 = size;
                                                        builder = (NotificationCompat.Builder)clone;
                                                        continue Label_4202_Outer;
                                                    }
                                                    s = (String)((LongSparseArray)builder).valueAt(n2);
                                                    sb14 = new StringBuilder();
                                                    sb14.append("cancel notification id ");
                                                    sb14.append((Object)s);
                                                    FileLog.w(sb14.toString());
                                                    continue;
                                                }
                                            }
                                            NotificationsController.notificationManager.notify(this.notificationId, build);
                                        }
                                        size2 = list3.size();
                                        j = 0;
                                        continue Label_4202;
                                    }
                                }
                                catch (Throwable t) {
                                    continue;
                                }
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private void showOrUpdateNotification(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //     4: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //     7: invokevirtual   org/telegram/messenger/UserConfig.isClientActivated:()Z
        //    10: ifeq            4718
        //    13: aload_0        
        //    14: getfield        org/telegram/messenger/NotificationsController.pushMessages:Ljava/util/ArrayList;
        //    17: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //    20: ifne            4718
        //    23: getstatic       org/telegram/messenger/SharedConfig.showNotificationsForAllAccounts:Z
        //    26: ifne            42
        //    29: aload_0        
        //    30: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //    33: getstatic       org/telegram/messenger/UserConfig.selectedAccount:I
        //    36: if_icmpeq       42
        //    39: goto            4718
        //    42: aload_0        
        //    43: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //    46: invokestatic    org/telegram/tgnet/ConnectionsManager.getInstance:(I)Lorg/telegram/tgnet/ConnectionsManager;
        //    49: invokevirtual   org/telegram/tgnet/ConnectionsManager.resumeNetworkMaybe:()V
        //    52: aload_0        
        //    53: getfield        org/telegram/messenger/NotificationsController.pushMessages:Ljava/util/ArrayList;
        //    56: iconst_0       
        //    57: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    60: checkcast       Lorg/telegram/messenger/MessageObject;
        //    63: astore_2       
        //    64: aload_0        
        //    65: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //    68: invokestatic    org/telegram/messenger/MessagesController.getNotificationsSettings:(I)Landroid/content/SharedPreferences;
        //    71: astore_3       
        //    72: aload_3        
        //    73: ldc_w           "dismissDate"
        //    76: iconst_0       
        //    77: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //    82: istore          4
        //    84: aload_2        
        //    85: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //    88: getfield        org/telegram/tgnet/TLRPC$Message.date:I
        //    91: iload           4
        //    93: if_icmpgt       101
        //    96: aload_0        
        //    97: invokespecial   org/telegram/messenger/NotificationsController.dismissNotification:()V
        //   100: return         
        //   101: aload_2        
        //   102: invokevirtual   org/telegram/messenger/MessageObject.getDialogId:()J
        //   105: lstore          5
        //   107: aload_2        
        //   108: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   111: getfield        org/telegram/tgnet/TLRPC$Message.mentioned:Z
        //   114: ifeq            130
        //   117: aload_2        
        //   118: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   121: getfield        org/telegram/tgnet/TLRPC$Message.from_id:I
        //   124: i2l            
        //   125: lstore          7
        //   127: goto            134
        //   130: lload           5
        //   132: lstore          7
        //   134: aload_2        
        //   135: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        //   138: pop            
        //   139: aload_2        
        //   140: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   143: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //   146: getfield        org/telegram/tgnet/TLRPC$Peer.chat_id:I
        //   149: ifeq            167
        //   152: aload_2        
        //   153: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   156: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //   159: getfield        org/telegram/tgnet/TLRPC$Peer.chat_id:I
        //   162: istore          9
        //   164: goto            179
        //   167: aload_2        
        //   168: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   171: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //   174: getfield        org/telegram/tgnet/TLRPC$Peer.channel_id:I
        //   177: istore          9
        //   179: aload_2        
        //   180: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   183: getfield        org/telegram/tgnet/TLRPC$Message.to_id:Lorg/telegram/tgnet/TLRPC$Peer;
        //   186: getfield        org/telegram/tgnet/TLRPC$Peer.user_id:I
        //   189: istore          10
        //   191: iload           10
        //   193: ifne            208
        //   196: aload_2        
        //   197: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   200: getfield        org/telegram/tgnet/TLRPC$Message.from_id:I
        //   203: istore          11
        //   205: goto            236
        //   208: iload           10
        //   210: istore          11
        //   212: iload           10
        //   214: aload_0        
        //   215: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //   218: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //   221: invokevirtual   org/telegram/messenger/UserConfig.getClientUserId:()I
        //   224: if_icmpne       236
        //   227: aload_2        
        //   228: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   231: getfield        org/telegram/tgnet/TLRPC$Message.from_id:I
        //   234: istore          11
        //   236: aload_0        
        //   237: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //   240: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //   243: iload           11
        //   245: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   248: invokevirtual   org/telegram/messenger/MessagesController.getUser:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$User;
        //   251: astore          12
        //   253: iload           9
        //   255: ifeq            307
        //   258: aload_0        
        //   259: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //   262: invokestatic    org/telegram/messenger/MessagesController.getInstance:(I)Lorg/telegram/messenger/MessagesController;
        //   265: iload           9
        //   267: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   270: invokevirtual   org/telegram/messenger/MessagesController.getChat:(Ljava/lang/Integer;)Lorg/telegram/tgnet/TLRPC$Chat;
        //   273: astore          13
        //   275: aload           13
        //   277: invokestatic    org/telegram/messenger/ChatObject.isChannel:(Lorg/telegram/tgnet/TLRPC$Chat;)Z
        //   280: ifeq            297
        //   283: aload           13
        //   285: getfield        org/telegram/tgnet/TLRPC$Chat.megagroup:Z
        //   288: ifne            297
        //   291: iconst_1       
        //   292: istore          10
        //   294: goto            300
        //   297: iconst_0       
        //   298: istore          10
        //   300: iload           10
        //   302: istore          14
        //   304: goto            313
        //   307: iconst_0       
        //   308: istore          14
        //   310: aconst_null    
        //   311: astore          13
        //   313: aload_0        
        //   314: aload_3        
        //   315: lload           7
        //   317: invokespecial   org/telegram/messenger/NotificationsController.getNotifyOverride:(Landroid/content/SharedPreferences;J)I
        //   320: istore          10
        //   322: iload           10
        //   324: iconst_m1      
        //   325: if_icmpne       339
        //   328: aload_0        
        //   329: lload           5
        //   331: invokevirtual   org/telegram/messenger/NotificationsController.isGlobalNotificationsEnabled:(J)Z
        //   334: istore          15
        //   336: goto            354
        //   339: iload           10
        //   341: iconst_2       
        //   342: if_icmpeq       351
        //   345: iconst_1       
        //   346: istore          15
        //   348: goto            354
        //   351: iconst_0       
        //   352: istore          15
        //   354: iload_1        
        //   355: ifeq            372
        //   358: iload           15
        //   360: ifne            366
        //   363: goto            372
        //   366: iconst_0       
        //   367: istore          10
        //   369: goto            375
        //   372: iconst_1       
        //   373: istore          10
        //   375: iload           10
        //   377: ifne            676
        //   380: lload           5
        //   382: lload           7
        //   384: lcmp           
        //   385: ifne            676
        //   388: aload           13
        //   390: ifnull          676
        //   393: new             Ljava/lang/StringBuilder;
        //   396: astore          16
        //   398: aload           16
        //   400: invokespecial   java/lang/StringBuilder.<init>:()V
        //   403: aload           16
        //   405: ldc_w           "custom_"
        //   408: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   411: pop            
        //   412: aload           16
        //   414: lload           5
        //   416: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   419: pop            
        //   420: aload_3        
        //   421: aload           16
        //   423: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   426: iconst_0       
        //   427: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //   432: ifeq            522
        //   435: new             Ljava/lang/StringBuilder;
        //   438: astore          16
        //   440: aload           16
        //   442: invokespecial   java/lang/StringBuilder.<init>:()V
        //   445: aload           16
        //   447: ldc_w           "smart_max_count_"
        //   450: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   453: pop            
        //   454: aload           16
        //   456: lload           5
        //   458: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   461: pop            
        //   462: aload_3        
        //   463: aload           16
        //   465: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   468: iconst_2       
        //   469: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   474: istore          17
        //   476: new             Ljava/lang/StringBuilder;
        //   479: astore          16
        //   481: aload           16
        //   483: invokespecial   java/lang/StringBuilder.<init>:()V
        //   486: aload           16
        //   488: ldc_w           "smart_delay_"
        //   491: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   494: pop            
        //   495: aload           16
        //   497: lload           5
        //   499: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   502: pop            
        //   503: aload_3        
        //   504: aload           16
        //   506: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   509: sipush          180
        //   512: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   517: istore          18
        //   519: goto            530
        //   522: sipush          180
        //   525: istore          18
        //   527: iconst_2       
        //   528: istore          17
        //   530: iload           17
        //   532: ifeq            676
        //   535: aload_0        
        //   536: getfield        org/telegram/messenger/NotificationsController.smartNotificationsDialogs:Landroid/util/LongSparseArray;
        //   539: lload           5
        //   541: invokevirtual   android/util/LongSparseArray.get:(J)Ljava/lang/Object;
        //   544: checkcast       Landroid/graphics/Point;
        //   547: astore          16
        //   549: aload           16
        //   551: ifnonnull       587
        //   554: new             Landroid/graphics/Point;
        //   557: astore          16
        //   559: aload           16
        //   561: iconst_1       
        //   562: invokestatic    java/lang/System.currentTimeMillis:()J
        //   565: ldc2_w          1000
        //   568: ldiv           
        //   569: l2i            
        //   570: invokespecial   android/graphics/Point.<init>:(II)V
        //   573: aload_0        
        //   574: getfield        org/telegram/messenger/NotificationsController.smartNotificationsDialogs:Landroid/util/LongSparseArray;
        //   577: lload           5
        //   579: aload           16
        //   581: invokevirtual   android/util/LongSparseArray.put:(JLjava/lang/Object;)V
        //   584: goto            676
        //   587: aload           16
        //   589: getfield        android/graphics/Point.y:I
        //   592: istore          19
        //   594: iload           10
        //   596: istore          20
        //   598: iload           19
        //   600: iload           18
        //   602: iadd           
        //   603: i2l            
        //   604: invokestatic    java/lang/System.currentTimeMillis:()J
        //   607: ldc2_w          1000
        //   610: ldiv           
        //   611: lcmp           
        //   612: ifge            632
        //   615: aload           16
        //   617: iconst_1       
        //   618: invokestatic    java/lang/System.currentTimeMillis:()J
        //   621: ldc2_w          1000
        //   624: ldiv           
        //   625: l2i            
        //   626: invokevirtual   android/graphics/Point.set:(II)V
        //   629: goto            676
        //   632: aload           16
        //   634: getfield        android/graphics/Point.x:I
        //   637: istore          10
        //   639: iload           10
        //   641: iload           17
        //   643: if_icmpge       670
        //   646: aload           16
        //   648: iload           10
        //   650: iconst_1       
        //   651: iadd           
        //   652: invokestatic    java/lang/System.currentTimeMillis:()J
        //   655: ldc2_w          1000
        //   658: ldiv           
        //   659: l2i            
        //   660: invokevirtual   android/graphics/Point.set:(II)V
        //   663: iload           20
        //   665: istore          10
        //   667: goto            676
        //   670: iconst_1       
        //   671: istore          10
        //   673: goto            676
        //   676: getstatic       android/provider/Settings$System.DEFAULT_NOTIFICATION_URI:Landroid/net/Uri;
        //   679: invokevirtual   android/net/Uri.getPath:()Ljava/lang/String;
        //   682: astore          21
        //   684: aload_3        
        //   685: ldc_w           "EnableInAppSounds"
        //   688: iconst_1       
        //   689: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //   694: istore          22
        //   696: aload_3        
        //   697: ldc_w           "EnableInAppVibrate"
        //   700: iconst_1       
        //   701: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //   706: istore          23
        //   708: aload_3        
        //   709: ldc_w           "EnableInAppPreview"
        //   712: iconst_1       
        //   713: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //   718: istore          15
        //   720: aload_3        
        //   721: ldc_w           "EnableInAppPriority"
        //   724: iconst_0       
        //   725: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //   730: istore          24
        //   732: new             Ljava/lang/StringBuilder;
        //   735: astore          16
        //   737: aload           16
        //   739: invokespecial   java/lang/StringBuilder.<init>:()V
        //   742: aload           16
        //   744: ldc_w           "custom_"
        //   747: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   750: pop            
        //   751: aload           16
        //   753: lload           5
        //   755: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   758: pop            
        //   759: aload_3        
        //   760: aload           16
        //   762: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   765: iconst_0       
        //   766: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //   771: istore          25
        //   773: iload           25
        //   775: ifeq            904
        //   778: new             Ljava/lang/StringBuilder;
        //   781: astore          16
        //   783: aload           16
        //   785: invokespecial   java/lang/StringBuilder.<init>:()V
        //   788: aload           16
        //   790: ldc_w           "vibrate_"
        //   793: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   796: pop            
        //   797: aload           16
        //   799: lload           5
        //   801: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   804: pop            
        //   805: aload_3        
        //   806: aload           16
        //   808: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   811: iconst_0       
        //   812: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   817: istore          20
        //   819: new             Ljava/lang/StringBuilder;
        //   822: astore          16
        //   824: aload           16
        //   826: invokespecial   java/lang/StringBuilder.<init>:()V
        //   829: aload           16
        //   831: ldc_w           "priority_"
        //   834: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   837: pop            
        //   838: aload           16
        //   840: lload           5
        //   842: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   845: pop            
        //   846: aload_3        
        //   847: aload           16
        //   849: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   852: iconst_3       
        //   853: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   858: istore          18
        //   860: new             Ljava/lang/StringBuilder;
        //   863: astore          16
        //   865: aload           16
        //   867: invokespecial   java/lang/StringBuilder.<init>:()V
        //   870: aload           16
        //   872: ldc_w           "sound_path_"
        //   875: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   878: pop            
        //   879: aload           16
        //   881: lload           5
        //   883: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   886: pop            
        //   887: aload_3        
        //   888: aload           16
        //   890: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   893: aconst_null    
        //   894: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   899: astore          16
        //   901: goto            913
        //   904: iconst_3       
        //   905: istore          18
        //   907: iconst_0       
        //   908: istore          20
        //   910: aconst_null    
        //   911: astore          16
        //   913: iload           10
        //   915: istore          26
        //   917: iload           9
        //   919: ifeq            1103
        //   922: iload           14
        //   924: ifeq            1015
        //   927: aload           16
        //   929: ifnull          948
        //   932: aload           16
        //   934: aload           21
        //   936: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   939: ifeq            948
        //   942: aconst_null    
        //   943: astore          27
        //   945: goto            970
        //   948: aload           16
        //   950: astore          27
        //   952: aload           16
        //   954: ifnonnull       970
        //   957: aload_3        
        //   958: ldc_w           "ChannelSoundPath"
        //   961: aload           21
        //   963: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   968: astore          27
        //   970: aload_3        
        //   971: ldc_w           "vibrate_channel"
        //   974: iconst_0       
        //   975: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   980: istore          17
        //   982: aload_3        
        //   983: ldc_w           "priority_channel"
        //   986: iconst_1       
        //   987: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //   992: istore          14
        //   994: aload_3        
        //   995: ldc_w           "ChannelLed"
        //   998: ldc_w           -16776961
        //  1001: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1006: istore          10
        //  1008: aload           27
        //  1010: astore          16
        //  1012: goto            1193
        //  1015: aload           16
        //  1017: ifnull          1036
        //  1020: aload           16
        //  1022: aload           21
        //  1024: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1027: ifeq            1036
        //  1030: aconst_null    
        //  1031: astore          27
        //  1033: goto            1058
        //  1036: aload           16
        //  1038: astore          27
        //  1040: aload           16
        //  1042: ifnonnull       1058
        //  1045: aload_3        
        //  1046: ldc_w           "GroupSoundPath"
        //  1049: aload           21
        //  1051: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  1056: astore          27
        //  1058: aload_3        
        //  1059: ldc_w           "vibrate_group"
        //  1062: iconst_0       
        //  1063: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1068: istore          17
        //  1070: aload_3        
        //  1071: ldc_w           "priority_group"
        //  1074: iconst_1       
        //  1075: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1080: istore          14
        //  1082: aload_3        
        //  1083: ldc_w           "GroupLed"
        //  1086: ldc_w           -16776961
        //  1089: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1094: istore          10
        //  1096: aload           27
        //  1098: astore          16
        //  1100: goto            1193
        //  1103: iload           11
        //  1105: ifeq            1196
        //  1108: aload           16
        //  1110: ifnull          1129
        //  1113: aload           16
        //  1115: aload           21
        //  1117: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1120: ifeq            1129
        //  1123: aconst_null    
        //  1124: astore          27
        //  1126: goto            1151
        //  1129: aload           16
        //  1131: astore          27
        //  1133: aload           16
        //  1135: ifnonnull       1151
        //  1138: aload_3        
        //  1139: ldc_w           "GlobalSoundPath"
        //  1142: aload           21
        //  1144: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  1149: astore          27
        //  1151: aload_3        
        //  1152: ldc_w           "vibrate_messages"
        //  1155: iconst_0       
        //  1156: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1161: istore          17
        //  1163: aload_3        
        //  1164: ldc_w           "priority_messages"
        //  1167: iconst_1       
        //  1168: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1173: istore          14
        //  1175: aload_3        
        //  1176: ldc_w           "MessagesLed"
        //  1179: ldc_w           -16776961
        //  1182: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1187: istore          10
        //  1189: aload           27
        //  1191: astore          16
        //  1193: goto            1207
        //  1196: ldc_w           -16776961
        //  1199: istore          10
        //  1201: iconst_0       
        //  1202: istore          17
        //  1204: iconst_0       
        //  1205: istore          14
        //  1207: iload           25
        //  1209: ifeq            1302
        //  1212: new             Ljava/lang/StringBuilder;
        //  1215: astore          27
        //  1217: aload           27
        //  1219: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1222: aload           27
        //  1224: ldc_w           "color_"
        //  1227: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1230: pop            
        //  1231: aload           27
        //  1233: lload           5
        //  1235: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  1238: pop            
        //  1239: aload_3        
        //  1240: aload           27
        //  1242: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1245: invokeinterface android/content/SharedPreferences.contains:(Ljava/lang/String;)Z
        //  1250: ifeq            1302
        //  1253: new             Ljava/lang/StringBuilder;
        //  1256: astore          27
        //  1258: aload           27
        //  1260: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1263: aload           27
        //  1265: ldc_w           "color_"
        //  1268: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1271: pop            
        //  1272: aload           27
        //  1274: lload           5
        //  1276: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  1279: pop            
        //  1280: aload_3        
        //  1281: aload           27
        //  1283: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1286: iconst_0       
        //  1287: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  1292: istore          10
        //  1294: goto            1302
        //  1297: astore          16
        //  1299: goto            4712
        //  1302: iload           18
        //  1304: iconst_3       
        //  1305: if_icmpeq       1311
        //  1308: goto            1315
        //  1311: iload           14
        //  1313: istore          18
        //  1315: iload           17
        //  1317: iconst_4       
        //  1318: if_icmpne       1330
        //  1321: iconst_0       
        //  1322: istore          17
        //  1324: iconst_1       
        //  1325: istore          19
        //  1327: goto            1333
        //  1330: iconst_0       
        //  1331: istore          19
        //  1333: iload           17
        //  1335: iconst_2       
        //  1336: if_icmpne       1359
        //  1339: iload           20
        //  1341: istore          14
        //  1343: iload           20
        //  1345: iconst_1       
        //  1346: if_icmpeq       1397
        //  1349: iload           20
        //  1351: istore          14
        //  1353: iload           20
        //  1355: iconst_3       
        //  1356: if_icmpeq       1397
        //  1359: iload           17
        //  1361: iconst_2       
        //  1362: if_icmpeq       1375
        //  1365: iload           20
        //  1367: istore          14
        //  1369: iload           20
        //  1371: iconst_2       
        //  1372: if_icmpeq       1397
        //  1375: iload           20
        //  1377: ifeq            1393
        //  1380: iload           20
        //  1382: iconst_4       
        //  1383: if_icmpeq       1393
        //  1386: iload           20
        //  1388: istore          14
        //  1390: goto            1397
        //  1393: iload           17
        //  1395: istore          14
        //  1397: getstatic       org/telegram/messenger/ApplicationLoader.mainInterfacePaused:Z
        //  1400: istore          25
        //  1402: iload           25
        //  1404: ifne            1462
        //  1407: iload           22
        //  1409: ifne            1415
        //  1412: aconst_null    
        //  1413: astore          16
        //  1415: iload           23
        //  1417: ifne            1423
        //  1420: iconst_2       
        //  1421: istore          14
        //  1423: iload           24
        //  1425: ifne            1438
        //  1428: iconst_0       
        //  1429: istore          17
        //  1431: aload           16
        //  1433: astore          28
        //  1435: goto            1478
        //  1438: iload           14
        //  1440: istore          17
        //  1442: aload           16
        //  1444: astore          28
        //  1446: iload           18
        //  1448: iconst_2       
        //  1449: if_icmpne       1470
        //  1452: iconst_1       
        //  1453: istore          17
        //  1455: aload           16
        //  1457: astore          28
        //  1459: goto            1478
        //  1462: aload           16
        //  1464: astore          28
        //  1466: iload           14
        //  1468: istore          17
        //  1470: iload           17
        //  1472: istore          14
        //  1474: iload           18
        //  1476: istore          17
        //  1478: iload           14
        //  1480: istore          20
        //  1482: iload           19
        //  1484: ifeq            1541
        //  1487: iload           14
        //  1489: istore          20
        //  1491: iload           14
        //  1493: iconst_2       
        //  1494: if_icmpeq       1541
        //  1497: getstatic       org/telegram/messenger/NotificationsController.audioManager:Landroid/media/AudioManager;
        //  1500: invokevirtual   android/media/AudioManager.getRingerMode:()I
        //  1503: istore          18
        //  1505: iload           14
        //  1507: istore          20
        //  1509: iload           18
        //  1511: ifeq            1541
        //  1514: iload           14
        //  1516: istore          20
        //  1518: iload           18
        //  1520: iconst_1       
        //  1521: if_icmpeq       1541
        //  1524: iconst_2       
        //  1525: istore          20
        //  1527: goto            1541
        //  1530: astore          16
        //  1532: aload           16
        //  1534: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1537: iload           14
        //  1539: istore          20
        //  1541: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1544: bipush          26
        //  1546: if_icmplt       1766
        //  1549: iload           20
        //  1551: iconst_2       
        //  1552: if_icmpne       1571
        //  1555: iconst_2       
        //  1556: newarray        J
        //  1558: dup            
        //  1559: iconst_0       
        //  1560: lconst_0       
        //  1561: lastore        
        //  1562: dup            
        //  1563: iconst_1       
        //  1564: lconst_0       
        //  1565: lastore        
        //  1566: astore          16
        //  1568: goto            1660
        //  1571: iload           20
        //  1573: iconst_1       
        //  1574: if_icmpne       1609
        //  1577: iconst_4       
        //  1578: newarray        J
        //  1580: astore          16
        //  1582: aload           16
        //  1584: iconst_0       
        //  1585: lconst_0       
        //  1586: lastore        
        //  1587: aload           16
        //  1589: iconst_1       
        //  1590: ldc2_w          100
        //  1593: lastore        
        //  1594: aload           16
        //  1596: iconst_2       
        //  1597: lconst_0       
        //  1598: lastore        
        //  1599: aload           16
        //  1601: iconst_3       
        //  1602: ldc2_w          100
        //  1605: lastore        
        //  1606: goto            1660
        //  1609: iload           20
        //  1611: ifeq            1655
        //  1614: iload           20
        //  1616: iconst_4       
        //  1617: if_icmpne       1623
        //  1620: goto            1655
        //  1623: iload           20
        //  1625: iconst_3       
        //  1626: if_icmpne       1649
        //  1629: iconst_2       
        //  1630: newarray        J
        //  1632: astore          16
        //  1634: aload           16
        //  1636: iconst_0       
        //  1637: lconst_0       
        //  1638: lastore        
        //  1639: aload           16
        //  1641: iconst_1       
        //  1642: ldc2_w          1000
        //  1645: lastore        
        //  1646: goto            1660
        //  1649: aconst_null    
        //  1650: astore          16
        //  1652: goto            1660
        //  1655: iconst_0       
        //  1656: newarray        J
        //  1658: astore          16
        //  1660: aload           28
        //  1662: ifnull          1704
        //  1665: aload           28
        //  1667: ldc_w           "NoSound"
        //  1670: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1673: ifne            1704
        //  1676: aload           28
        //  1678: aload           21
        //  1680: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1683: ifeq            1694
        //  1686: getstatic       android/provider/Settings$System.DEFAULT_NOTIFICATION_URI:Landroid/net/Uri;
        //  1689: astore          27
        //  1691: goto            1707
        //  1694: aload           28
        //  1696: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  1699: astore          27
        //  1701: goto            1707
        //  1704: aconst_null    
        //  1705: astore          27
        //  1707: iload           17
        //  1709: ifne            1718
        //  1712: iconst_3       
        //  1713: istore          14
        //  1715: goto            1775
        //  1718: iload           17
        //  1720: iconst_1       
        //  1721: if_icmpeq       1760
        //  1724: iload           17
        //  1726: iconst_2       
        //  1727: if_icmpne       1733
        //  1730: goto            1760
        //  1733: iload           17
        //  1735: iconst_4       
        //  1736: if_icmpne       1745
        //  1739: iconst_1       
        //  1740: istore          14
        //  1742: goto            1775
        //  1745: iload           17
        //  1747: iconst_5       
        //  1748: if_icmpne       1757
        //  1751: iconst_2       
        //  1752: istore          14
        //  1754: goto            1775
        //  1757: goto            1772
        //  1760: iconst_4       
        //  1761: istore          14
        //  1763: goto            1775
        //  1766: aconst_null    
        //  1767: astore          16
        //  1769: aconst_null    
        //  1770: astore          27
        //  1772: iconst_0       
        //  1773: istore          14
        //  1775: iload           26
        //  1777: ifeq            1795
        //  1780: iconst_0       
        //  1781: istore          17
        //  1783: iconst_0       
        //  1784: istore          10
        //  1786: iconst_0       
        //  1787: istore          20
        //  1789: aconst_null    
        //  1790: astore          28
        //  1792: goto            1795
        //  1795: new             Landroid/content/Intent;
        //  1798: astore          29
        //  1800: aload           29
        //  1802: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  1805: ldc_w           Lorg/telegram/ui/LaunchActivity;.class
        //  1808: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //  1811: new             Ljava/lang/StringBuilder;
        //  1814: astore_3       
        //  1815: aload_3        
        //  1816: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1819: aload_3        
        //  1820: ldc_w           "com.tmessages.openchat"
        //  1823: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1826: pop            
        //  1827: aload_3        
        //  1828: invokestatic    java/lang/Math.random:()D
        //  1831: invokevirtual   java/lang/StringBuilder.append:(D)Ljava/lang/StringBuilder;
        //  1834: pop            
        //  1835: aload_3        
        //  1836: ldc_w           2147483647
        //  1839: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  1842: pop            
        //  1843: aload           29
        //  1845: aload_3        
        //  1846: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1849: invokevirtual   android/content/Intent.setAction:(Ljava/lang/String;)Landroid/content/Intent;
        //  1852: pop            
        //  1853: aload           29
        //  1855: ldc_w           32768
        //  1858: invokevirtual   android/content/Intent.setFlags:(I)Landroid/content/Intent;
        //  1861: pop            
        //  1862: lload           5
        //  1864: l2i            
        //  1865: istore          18
        //  1867: iload           18
        //  1869: ifeq            2095
        //  1872: aload_0        
        //  1873: astore_3       
        //  1874: aload_3        
        //  1875: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  1878: invokevirtual   android/util/LongSparseArray.size:()I
        //  1881: iconst_1       
        //  1882: if_icmpne       1920
        //  1885: iload           9
        //  1887: ifeq            1904
        //  1890: aload           29
        //  1892: ldc_w           "chatId"
        //  1895: iload           9
        //  1897: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  1900: pop            
        //  1901: goto            1920
        //  1904: iload           11
        //  1906: ifeq            1920
        //  1909: aload           29
        //  1911: ldc_w           "userId"
        //  1914: iload           11
        //  1916: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  1919: pop            
        //  1920: iconst_0       
        //  1921: invokestatic    org/telegram/messenger/AndroidUtilities.needShowPasscode:(Z)Z
        //  1924: ifne            2092
        //  1927: getstatic       org/telegram/messenger/SharedConfig.isWaitingForPasscodeEnter:Z
        //  1930: ifeq            1936
        //  1933: goto            2092
        //  1936: aload_3        
        //  1937: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  1940: invokevirtual   android/util/LongSparseArray.size:()I
        //  1943: iconst_1       
        //  1944: if_icmpne       2089
        //  1947: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1950: bipush          28
        //  1952: if_icmpge       2089
        //  1955: aload           13
        //  1957: ifnull          2022
        //  1960: aload           13
        //  1962: astore_3       
        //  1963: aload_3        
        //  1964: getfield        org/telegram/tgnet/TLRPC$Chat.photo:Lorg/telegram/tgnet/TLRPC$ChatPhoto;
        //  1967: ifnull          2019
        //  1970: aload_3        
        //  1971: getfield        org/telegram/tgnet/TLRPC$Chat.photo:Lorg/telegram/tgnet/TLRPC$ChatPhoto;
        //  1974: getfield        org/telegram/tgnet/TLRPC$ChatPhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  1977: ifnull          2019
        //  1980: aload_3        
        //  1981: getfield        org/telegram/tgnet/TLRPC$Chat.photo:Lorg/telegram/tgnet/TLRPC$ChatPhoto;
        //  1984: getfield        org/telegram/tgnet/TLRPC$ChatPhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  1987: getfield        org/telegram/tgnet/TLRPC$FileLocation.volume_id:J
        //  1990: lconst_0       
        //  1991: lcmp           
        //  1992: ifeq            2019
        //  1995: aload_3        
        //  1996: getfield        org/telegram/tgnet/TLRPC$Chat.photo:Lorg/telegram/tgnet/TLRPC$ChatPhoto;
        //  1999: getfield        org/telegram/tgnet/TLRPC$ChatPhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  2002: getfield        org/telegram/tgnet/TLRPC$FileLocation.local_id:I
        //  2005: ifeq            2019
        //  2008: aload_3        
        //  2009: getfield        org/telegram/tgnet/TLRPC$Chat.photo:Lorg/telegram/tgnet/TLRPC$ChatPhoto;
        //  2012: getfield        org/telegram/tgnet/TLRPC$ChatPhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  2015: astore_3       
        //  2016: goto            2138
        //  2019: goto            2092
        //  2022: aload           12
        //  2024: ifnull          2086
        //  2027: aload           12
        //  2029: astore_3       
        //  2030: aload_3        
        //  2031: getfield        org/telegram/tgnet/TLRPC$User.photo:Lorg/telegram/tgnet/TLRPC$UserProfilePhoto;
        //  2034: ifnull          2133
        //  2037: aload_3        
        //  2038: getfield        org/telegram/tgnet/TLRPC$User.photo:Lorg/telegram/tgnet/TLRPC$UserProfilePhoto;
        //  2041: getfield        org/telegram/tgnet/TLRPC$UserProfilePhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  2044: ifnull          2133
        //  2047: aload_3        
        //  2048: getfield        org/telegram/tgnet/TLRPC$User.photo:Lorg/telegram/tgnet/TLRPC$UserProfilePhoto;
        //  2051: getfield        org/telegram/tgnet/TLRPC$UserProfilePhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  2054: getfield        org/telegram/tgnet/TLRPC$FileLocation.volume_id:J
        //  2057: lconst_0       
        //  2058: lcmp           
        //  2059: ifeq            2133
        //  2062: aload_3        
        //  2063: getfield        org/telegram/tgnet/TLRPC$User.photo:Lorg/telegram/tgnet/TLRPC$UserProfilePhoto;
        //  2066: getfield        org/telegram/tgnet/TLRPC$UserProfilePhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  2069: getfield        org/telegram/tgnet/TLRPC$FileLocation.local_id:I
        //  2072: ifeq            2133
        //  2075: aload_3        
        //  2076: getfield        org/telegram/tgnet/TLRPC$User.photo:Lorg/telegram/tgnet/TLRPC$UserProfilePhoto;
        //  2079: getfield        org/telegram/tgnet/TLRPC$UserProfilePhoto.photo_small:Lorg/telegram/tgnet/TLRPC$FileLocation;
        //  2082: astore_3       
        //  2083: goto            2016
        //  2086: goto            2133
        //  2089: goto            2133
        //  2092: goto            2136
        //  2095: aload_0        
        //  2096: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  2099: invokevirtual   android/util/LongSparseArray.size:()I
        //  2102: iconst_1       
        //  2103: if_icmpne       2133
        //  2106: lload           5
        //  2108: getstatic       org/telegram/messenger/NotificationsController.globalSecretChatId:J
        //  2111: lcmp           
        //  2112: ifeq            2133
        //  2115: aload           29
        //  2117: ldc_w           "encId"
        //  2120: lload           5
        //  2122: bipush          32
        //  2124: lshr           
        //  2125: l2i            
        //  2126: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  2129: pop            
        //  2130: goto            2136
        //  2133: goto            2092
        //  2136: aconst_null    
        //  2137: astore_3       
        //  2138: iload           20
        //  2140: istore          19
        //  2142: aload_0        
        //  2143: astore          30
        //  2145: aload           12
        //  2147: astore          31
        //  2149: aload           13
        //  2151: astore          32
        //  2153: iload           10
        //  2155: istore          11
        //  2157: aload           30
        //  2159: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //  2162: istore          10
        //  2164: ldc_w           "currentAccount"
        //  2167: astore          33
        //  2169: aload           29
        //  2171: aload           33
        //  2173: iload           10
        //  2175: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  2178: pop            
        //  2179: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  2182: astore          12
        //  2184: aload           12
        //  2186: iconst_0       
        //  2187: aload           29
        //  2189: ldc_w           1073741824
        //  2192: invokestatic    android/app/PendingIntent.getActivity:(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
        //  2195: astore          34
        //  2197: iload           9
        //  2199: ifeq            2207
        //  2202: aload           32
        //  2204: ifnull          2212
        //  2207: aload           31
        //  2209: ifnonnull       2228
        //  2212: aload_2        
        //  2213: invokevirtual   org/telegram/messenger/MessageObject.isFcmMessage:()Z
        //  2216: ifeq            2228
        //  2219: aload_2        
        //  2220: getfield        org/telegram/messenger/MessageObject.localName:Ljava/lang/String;
        //  2223: astore          12
        //  2225: goto            2250
        //  2228: aload           32
        //  2230: ifnull          2243
        //  2233: aload           32
        //  2235: getfield        org/telegram/tgnet/TLRPC$Chat.title:Ljava/lang/String;
        //  2238: astore          12
        //  2240: goto            2250
        //  2243: aload           31
        //  2245: invokestatic    org/telegram/messenger/UserObject.getUserName:(Lorg/telegram/tgnet/TLRPC$User;)Ljava/lang/String;
        //  2248: astore          12
        //  2250: iload           18
        //  2252: ifeq            2293
        //  2255: aload           30
        //  2257: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  2260: invokevirtual   android/util/LongSparseArray.size:()I
        //  2263: iconst_1       
        //  2264: if_icmpgt       2293
        //  2267: iconst_0       
        //  2268: invokestatic    org/telegram/messenger/AndroidUtilities.needShowPasscode:(Z)Z
        //  2271: ifne            2293
        //  2274: getstatic       org/telegram/messenger/SharedConfig.isWaitingForPasscodeEnter:Z
        //  2277: ifeq            2283
        //  2280: goto            2293
        //  2283: aload           12
        //  2285: astore          35
        //  2287: iconst_1       
        //  2288: istore          20
        //  2290: goto            2307
        //  2293: ldc_w           "AppName"
        //  2296: ldc_w           2131558635
        //  2299: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  2302: astore          35
        //  2304: iconst_0       
        //  2305: istore          20
        //  2307: invokestatic    org/telegram/messenger/UserConfig.getActivatedAccountsCount:()I
        //  2310: istore          10
        //  2312: iload           10
        //  2314: iconst_1       
        //  2315: if_icmple       2398
        //  2318: aload           30
        //  2320: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  2323: invokevirtual   android/util/LongSparseArray.size:()I
        //  2326: iconst_1       
        //  2327: if_icmpne       2349
        //  2330: aload           30
        //  2332: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //  2335: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  2338: invokevirtual   org/telegram/messenger/UserConfig.getCurrentUser:()Lorg/telegram/tgnet/TLRPC$User;
        //  2341: invokestatic    org/telegram/messenger/UserObject.getFirstName:(Lorg/telegram/tgnet/TLRPC$User;)Ljava/lang/String;
        //  2344: astore          13
        //  2346: goto            2402
        //  2349: new             Ljava/lang/StringBuilder;
        //  2352: astore          13
        //  2354: aload           13
        //  2356: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2359: aload           13
        //  2361: aload           30
        //  2363: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //  2366: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //  2369: invokevirtual   org/telegram/messenger/UserConfig.getCurrentUser:()Lorg/telegram/tgnet/TLRPC$User;
        //  2372: invokestatic    org/telegram/messenger/UserObject.getFirstName:(Lorg/telegram/tgnet/TLRPC$User;)Ljava/lang/String;
        //  2375: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2378: pop            
        //  2379: aload           13
        //  2381: ldc_w           "\u30fb"
        //  2384: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2387: pop            
        //  2388: aload           13
        //  2390: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2393: astore          13
        //  2395: goto            2402
        //  2398: ldc             ""
        //  2400: astore          13
        //  2402: aload           30
        //  2404: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  2407: invokevirtual   android/util/LongSparseArray.size:()I
        //  2410: iconst_1       
        //  2411: if_icmpne       2448
        //  2414: aload           13
        //  2416: astore          29
        //  2418: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  2421: bipush          23
        //  2423: if_icmpge       2429
        //  2426: goto            2448
        //  2429: aload           29
        //  2431: astore          13
        //  2433: iload           17
        //  2435: istore          36
        //  2437: aload           28
        //  2439: astore          37
        //  2441: aload           13
        //  2443: astore          28
        //  2445: goto            2583
        //  2448: aload           30
        //  2450: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  2453: invokevirtual   android/util/LongSparseArray.size:()I
        //  2456: iconst_1       
        //  2457: if_icmpne       2505
        //  2460: new             Ljava/lang/StringBuilder;
        //  2463: astore          29
        //  2465: aload           29
        //  2467: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2470: aload           29
        //  2472: aload           13
        //  2474: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2477: pop            
        //  2478: aload           29
        //  2480: ldc_w           "NewMessages"
        //  2483: aload           30
        //  2485: getfield        org/telegram/messenger/NotificationsController.total_unread_count:I
        //  2488: invokestatic    org/telegram/messenger/LocaleController.formatPluralString:(Ljava/lang/String;I)Ljava/lang/String;
        //  2491: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2494: pop            
        //  2495: aload           29
        //  2497: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2500: astore          29
        //  2502: goto            2429
        //  2505: new             Ljava/lang/StringBuilder;
        //  2508: astore          29
        //  2510: aload           29
        //  2512: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2515: aload           29
        //  2517: aload           13
        //  2519: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2522: pop            
        //  2523: aload           29
        //  2525: ldc_w           "NotificationMessagesPeopleDisplayOrder"
        //  2528: ldc_w           2131560053
        //  2531: iconst_2       
        //  2532: anewarray       Ljava/lang/Object;
        //  2535: dup            
        //  2536: iconst_0       
        //  2537: ldc_w           "NewMessages"
        //  2540: aload           30
        //  2542: getfield        org/telegram/messenger/NotificationsController.total_unread_count:I
        //  2545: invokestatic    org/telegram/messenger/LocaleController.formatPluralString:(Ljava/lang/String;I)Ljava/lang/String;
        //  2548: aastore        
        //  2549: dup            
        //  2550: iconst_1       
        //  2551: ldc_w           "FromChats"
        //  2554: aload           30
        //  2556: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  2559: invokevirtual   android/util/LongSparseArray.size:()I
        //  2562: invokestatic    org/telegram/messenger/LocaleController.formatPluralString:(Ljava/lang/String;I)Ljava/lang/String;
        //  2565: aastore        
        //  2566: invokestatic    org/telegram/messenger/LocaleController.formatString:(Ljava/lang/String;I[Ljava/lang/Object;)Ljava/lang/String;
        //  2569: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2572: pop            
        //  2573: aload           29
        //  2575: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2578: astore          13
        //  2580: goto            2433
        //  2583: new             Landroidx/core/app/NotificationCompat$Builder;
        //  2586: astore          38
        //  2588: aload           38
        //  2590: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  2593: invokespecial   androidx/core/app/NotificationCompat$Builder.<init>:(Landroid/content/Context;)V
        //  2596: aload           38
        //  2598: aload           35
        //  2600: invokevirtual   androidx/core/app/NotificationCompat$Builder.setContentTitle:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$Builder;
        //  2603: pop            
        //  2604: aload           38
        //  2606: ldc_w           2131165698
        //  2609: invokevirtual   androidx/core/app/NotificationCompat$Builder.setSmallIcon:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  2612: pop            
        //  2613: aload           38
        //  2615: iconst_1       
        //  2616: invokevirtual   androidx/core/app/NotificationCompat$Builder.setAutoCancel:(Z)Landroidx/core/app/NotificationCompat$Builder;
        //  2619: pop            
        //  2620: aload           38
        //  2622: aload           30
        //  2624: getfield        org/telegram/messenger/NotificationsController.total_unread_count:I
        //  2627: invokevirtual   androidx/core/app/NotificationCompat$Builder.setNumber:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  2630: pop            
        //  2631: aload           38
        //  2633: aload           34
        //  2635: invokevirtual   androidx/core/app/NotificationCompat$Builder.setContentIntent:(Landroid/app/PendingIntent;)Landroidx/core/app/NotificationCompat$Builder;
        //  2638: pop            
        //  2639: aload           38
        //  2641: aload           30
        //  2643: getfield        org/telegram/messenger/NotificationsController.notificationGroup:Ljava/lang/String;
        //  2646: invokevirtual   androidx/core/app/NotificationCompat$Builder.setGroup:(Ljava/lang/String;)Landroidx/core/app/NotificationCompat$Builder;
        //  2649: pop            
        //  2650: aload           38
        //  2652: iconst_1       
        //  2653: invokevirtual   androidx/core/app/NotificationCompat$Builder.setGroupSummary:(Z)Landroidx/core/app/NotificationCompat$Builder;
        //  2656: pop            
        //  2657: aload           38
        //  2659: iconst_1       
        //  2660: invokevirtual   androidx/core/app/NotificationCompat$Builder.setShowWhen:(Z)Landroidx/core/app/NotificationCompat$Builder;
        //  2663: pop            
        //  2664: aload           38
        //  2666: aload_2        
        //  2667: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  2670: getfield        org/telegram/tgnet/TLRPC$Message.date:I
        //  2673: i2l            
        //  2674: ldc2_w          1000
        //  2677: lmul           
        //  2678: invokevirtual   androidx/core/app/NotificationCompat$Builder.setWhen:(J)Landroidx/core/app/NotificationCompat$Builder;
        //  2681: pop            
        //  2682: aload           38
        //  2684: ldc_w           -15618822
        //  2687: invokevirtual   androidx/core/app/NotificationCompat$Builder.setColor:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  2690: pop            
        //  2691: aload           38
        //  2693: ldc_w           "msg"
        //  2696: invokevirtual   androidx/core/app/NotificationCompat$Builder.setCategory:(Ljava/lang/String;)Landroidx/core/app/NotificationCompat$Builder;
        //  2699: pop            
        //  2700: aload           32
        //  2702: ifnonnull       2770
        //  2705: aload           31
        //  2707: ifnull          2770
        //  2710: aload           31
        //  2712: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //  2715: ifnull          2770
        //  2718: aload           31
        //  2720: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //  2723: invokevirtual   java/lang/String.length:()I
        //  2726: ifle            2770
        //  2729: new             Ljava/lang/StringBuilder;
        //  2732: astore          13
        //  2734: aload           13
        //  2736: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2739: aload           13
        //  2741: ldc_w           "tel:+"
        //  2744: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2747: pop            
        //  2748: aload           13
        //  2750: aload           31
        //  2752: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //  2755: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2758: pop            
        //  2759: aload           38
        //  2761: aload           13
        //  2763: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2766: invokevirtual   androidx/core/app/NotificationCompat$Builder.addPerson:(Ljava/lang/String;)Landroidx/core/app/NotificationCompat$Builder;
        //  2769: pop            
        //  2770: aload           30
        //  2772: getfield        org/telegram/messenger/NotificationsController.pushMessages:Ljava/util/ArrayList;
        //  2775: invokevirtual   java/util/ArrayList.size:()I
        //  2778: iconst_1       
        //  2779: if_icmpne       3032
        //  2782: aload           30
        //  2784: getfield        org/telegram/messenger/NotificationsController.pushMessages:Ljava/util/ArrayList;
        //  2787: iconst_0       
        //  2788: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  2791: checkcast       Lorg/telegram/messenger/MessageObject;
        //  2794: astore          13
        //  2796: iconst_1       
        //  2797: newarray        Z
        //  2799: astore          31
        //  2801: aload           30
        //  2803: aload           13
        //  2805: iconst_0       
        //  2806: aload           31
        //  2808: aconst_null    
        //  2809: invokespecial   org/telegram/messenger/NotificationsController.getStringForMessage:(Lorg/telegram/messenger/MessageObject;Z[Z[Z)Ljava/lang/String;
        //  2812: astore          29
        //  2814: aload           13
        //  2816: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  2819: getfield        org/telegram/tgnet/TLRPC$Message.silent:Z
        //  2822: istore          17
        //  2824: aload           29
        //  2826: ifnonnull       2830
        //  2829: return         
        //  2830: iload           20
        //  2832: ifeq            2979
        //  2835: aload           32
        //  2837: ifnull          2884
        //  2840: new             Ljava/lang/StringBuilder;
        //  2843: astore          13
        //  2845: aload           13
        //  2847: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2850: aload           13
        //  2852: ldc_w           " @ "
        //  2855: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2858: pop            
        //  2859: aload           13
        //  2861: aload           35
        //  2863: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2866: pop            
        //  2867: aload           29
        //  2869: aload           13
        //  2871: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2874: ldc             ""
        //  2876: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2879: astore          13
        //  2881: goto            2983
        //  2884: aload           31
        //  2886: iconst_0       
        //  2887: baload         
        //  2888: ifeq            2935
        //  2891: new             Ljava/lang/StringBuilder;
        //  2894: astore          13
        //  2896: aload           13
        //  2898: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2901: aload           13
        //  2903: aload           35
        //  2905: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2908: pop            
        //  2909: aload           13
        //  2911: ldc_w           ": "
        //  2914: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2917: pop            
        //  2918: aload           29
        //  2920: aload           13
        //  2922: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2925: ldc             ""
        //  2927: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2930: astore          13
        //  2932: goto            2983
        //  2935: new             Ljava/lang/StringBuilder;
        //  2938: astore          13
        //  2940: aload           13
        //  2942: invokespecial   java/lang/StringBuilder.<init>:()V
        //  2945: aload           13
        //  2947: aload           35
        //  2949: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2952: pop            
        //  2953: aload           13
        //  2955: ldc_w           " "
        //  2958: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2961: pop            
        //  2962: aload           29
        //  2964: aload           13
        //  2966: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  2969: ldc             ""
        //  2971: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  2974: astore          13
        //  2976: goto            2983
        //  2979: aload           29
        //  2981: astore          13
        //  2983: aload           38
        //  2985: aload           13
        //  2987: invokevirtual   androidx/core/app/NotificationCompat$Builder.setContentText:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$Builder;
        //  2990: pop            
        //  2991: new             Landroidx/core/app/NotificationCompat$BigTextStyle;
        //  2994: astore          35
        //  2996: aload           35
        //  2998: invokespecial   androidx/core/app/NotificationCompat$BigTextStyle.<init>:()V
        //  3001: aload           35
        //  3003: aload           13
        //  3005: invokevirtual   androidx/core/app/NotificationCompat$BigTextStyle.bigText:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$BigTextStyle;
        //  3008: pop            
        //  3009: aload           38
        //  3011: aload           35
        //  3013: invokevirtual   androidx/core/app/NotificationCompat$Builder.setStyle:(Landroidx/core/app/NotificationCompat$Style;)Landroidx/core/app/NotificationCompat$Builder;
        //  3016: pop            
        //  3017: aload_2        
        //  3018: astore          13
        //  3020: aload           33
        //  3022: astore_2       
        //  3023: aload_3        
        //  3024: astore          33
        //  3026: aload           29
        //  3028: astore_3       
        //  3029: goto            3371
        //  3032: aload           38
        //  3034: aload           28
        //  3036: invokevirtual   androidx/core/app/NotificationCompat$Builder.setContentText:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$Builder;
        //  3039: pop            
        //  3040: new             Landroidx/core/app/NotificationCompat$InboxStyle;
        //  3043: astore          34
        //  3045: aload           34
        //  3047: invokespecial   androidx/core/app/NotificationCompat$InboxStyle.<init>:()V
        //  3050: aload           34
        //  3052: aload           35
        //  3054: invokevirtual   androidx/core/app/NotificationCompat$InboxStyle.setBigContentTitle:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$InboxStyle;
        //  3057: pop            
        //  3058: bipush          10
        //  3060: aload           30
        //  3062: getfield        org/telegram/messenger/NotificationsController.pushMessages:Ljava/util/ArrayList;
        //  3065: invokevirtual   java/util/ArrayList.size:()I
        //  3068: invokestatic    java/lang/Math.min:(II)I
        //  3071: istore          9
        //  3073: iconst_1       
        //  3074: newarray        Z
        //  3076: astore          39
        //  3078: iconst_0       
        //  3079: istore          18
        //  3081: iconst_2       
        //  3082: istore          10
        //  3084: aconst_null    
        //  3085: astore          13
        //  3087: aload_3        
        //  3088: astore          29
        //  3090: iload           18
        //  3092: iload           9
        //  3094: if_icmpge       3332
        //  3097: aload           30
        //  3099: getfield        org/telegram/messenger/NotificationsController.pushMessages:Ljava/util/ArrayList;
        //  3102: iload           18
        //  3104: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  3107: checkcast       Lorg/telegram/messenger/MessageObject;
        //  3110: astore_3       
        //  3111: aload           30
        //  3113: aload_3        
        //  3114: iconst_0       
        //  3115: aload           39
        //  3117: aconst_null    
        //  3118: invokespecial   org/telegram/messenger/NotificationsController.getStringForMessage:(Lorg/telegram/messenger/MessageObject;Z[Z[Z)Ljava/lang/String;
        //  3121: astore          31
        //  3123: aload           31
        //  3125: ifnull          3326
        //  3128: aload_3        
        //  3129: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  3132: getfield        org/telegram/tgnet/TLRPC$Message.date:I
        //  3135: iload           4
        //  3137: if_icmpgt       3143
        //  3140: goto            3326
        //  3143: iload           10
        //  3145: istore          17
        //  3147: iload           10
        //  3149: iconst_2       
        //  3150: if_icmpne       3166
        //  3153: aload_3        
        //  3154: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  3157: getfield        org/telegram/tgnet/TLRPC$Message.silent:Z
        //  3160: istore          17
        //  3162: aload           31
        //  3164: astore          13
        //  3166: aload           31
        //  3168: astore_3       
        //  3169: aload           30
        //  3171: getfield        org/telegram/messenger/NotificationsController.pushDialogs:Landroid/util/LongSparseArray;
        //  3174: invokevirtual   android/util/LongSparseArray.size:()I
        //  3177: iconst_1       
        //  3178: if_icmpne       3312
        //  3181: aload           31
        //  3183: astore_3       
        //  3184: iload           20
        //  3186: ifeq            3312
        //  3189: aload           32
        //  3191: ifnull          3232
        //  3194: new             Ljava/lang/StringBuilder;
        //  3197: astore_3       
        //  3198: aload_3        
        //  3199: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3202: aload_3        
        //  3203: ldc_w           " @ "
        //  3206: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3209: pop            
        //  3210: aload_3        
        //  3211: aload           35
        //  3213: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3216: pop            
        //  3217: aload           31
        //  3219: aload_3        
        //  3220: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3223: ldc             ""
        //  3225: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3228: astore_3       
        //  3229: goto            3312
        //  3232: aload           39
        //  3234: iconst_0       
        //  3235: baload         
        //  3236: ifeq            3277
        //  3239: new             Ljava/lang/StringBuilder;
        //  3242: astore_3       
        //  3243: aload_3        
        //  3244: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3247: aload_3        
        //  3248: aload           35
        //  3250: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3253: pop            
        //  3254: aload_3        
        //  3255: ldc_w           ": "
        //  3258: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3261: pop            
        //  3262: aload           31
        //  3264: aload_3        
        //  3265: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3268: ldc             ""
        //  3270: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3273: astore_3       
        //  3274: goto            3312
        //  3277: new             Ljava/lang/StringBuilder;
        //  3280: astore_3       
        //  3281: aload_3        
        //  3282: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3285: aload_3        
        //  3286: aload           35
        //  3288: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3291: pop            
        //  3292: aload_3        
        //  3293: ldc_w           " "
        //  3296: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3299: pop            
        //  3300: aload           31
        //  3302: aload_3        
        //  3303: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3306: ldc             ""
        //  3308: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3311: astore_3       
        //  3312: aload           34
        //  3314: aload_3        
        //  3315: invokevirtual   androidx/core/app/NotificationCompat$InboxStyle.addLine:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$InboxStyle;
        //  3318: pop            
        //  3319: iload           17
        //  3321: istore          10
        //  3323: goto            3326
        //  3326: iinc            18, 1
        //  3329: goto            3090
        //  3332: aload_2        
        //  3333: astore_3       
        //  3334: aload           33
        //  3336: astore_2       
        //  3337: aload           34
        //  3339: aload           28
        //  3341: invokevirtual   androidx/core/app/NotificationCompat$InboxStyle.setSummaryText:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$InboxStyle;
        //  3344: pop            
        //  3345: aload           38
        //  3347: aload           34
        //  3349: invokevirtual   androidx/core/app/NotificationCompat$Builder.setStyle:(Landroidx/core/app/NotificationCompat$Style;)Landroidx/core/app/NotificationCompat$Builder;
        //  3352: pop            
        //  3353: aload           13
        //  3355: astore          35
        //  3357: aload_3        
        //  3358: astore          13
        //  3360: aload           29
        //  3362: astore          33
        //  3364: aload           35
        //  3366: astore_3       
        //  3367: iload           10
        //  3369: istore          17
        //  3371: new             Landroid/content/Intent;
        //  3374: astore          35
        //  3376: aload           35
        //  3378: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  3381: ldc_w           Lorg/telegram/messenger/NotificationDismissReceiver;.class
        //  3384: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //  3387: aload           13
        //  3389: astore          29
        //  3391: aload           35
        //  3393: ldc_w           "messageDate"
        //  3396: aload           29
        //  3398: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  3401: getfield        org/telegram/tgnet/TLRPC$Message.date:I
        //  3404: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  3407: pop            
        //  3408: aload           35
        //  3410: aload_2        
        //  3411: aload           30
        //  3413: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //  3416: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  3419: pop            
        //  3420: aload           38
        //  3422: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  3425: iconst_1       
        //  3426: aload           35
        //  3428: ldc_w           134217728
        //  3431: invokestatic    android/app/PendingIntent.getBroadcast:(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
        //  3434: invokevirtual   androidx/core/app/NotificationCompat$Builder.setDeleteIntent:(Landroid/app/PendingIntent;)Landroidx/core/app/NotificationCompat$Builder;
        //  3437: pop            
        //  3438: aload           33
        //  3440: ifnull          3568
        //  3443: invokestatic    org/telegram/messenger/ImageLoader.getInstance:()Lorg/telegram/messenger/ImageLoader;
        //  3446: aload           33
        //  3448: aconst_null    
        //  3449: ldc_w           "50_50"
        //  3452: invokevirtual   org/telegram/messenger/ImageLoader.getImageFromMemory:(Lorg/telegram/tgnet/TLObject;Ljava/lang/String;Ljava/lang/String;)Landroid/graphics/drawable/BitmapDrawable;
        //  3455: astore          13
        //  3457: aload           13
        //  3459: ifnull          3476
        //  3462: aload           38
        //  3464: aload           13
        //  3466: invokevirtual   android/graphics/drawable/BitmapDrawable.getBitmap:()Landroid/graphics/Bitmap;
        //  3469: invokevirtual   androidx/core/app/NotificationCompat$Builder.setLargeIcon:(Landroid/graphics/Bitmap;)Landroidx/core/app/NotificationCompat$Builder;
        //  3472: pop            
        //  3473: goto            3568
        //  3476: aload           33
        //  3478: iconst_1       
        //  3479: invokestatic    org/telegram/messenger/FileLoader.getPathToAttach:(Lorg/telegram/tgnet/TLObject;Z)Ljava/io/File;
        //  3482: astore          13
        //  3484: aload           13
        //  3486: invokevirtual   java/io/File.exists:()Z
        //  3489: ifeq            3568
        //  3492: ldc_w           160.0
        //  3495: ldc_w           50.0
        //  3498: invokestatic    org/telegram/messenger/AndroidUtilities.dp:(F)I
        //  3501: i2f            
        //  3502: fdiv           
        //  3503: fstore          40
        //  3505: new             Landroid/graphics/BitmapFactory$Options;
        //  3508: astore          33
        //  3510: aload           33
        //  3512: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
        //  3515: fload           40
        //  3517: fconst_1       
        //  3518: fcmpg          
        //  3519: ifge            3528
        //  3522: iconst_1       
        //  3523: istore          10
        //  3525: goto            3533
        //  3528: fload           40
        //  3530: f2i            
        //  3531: istore          10
        //  3533: aload           33
        //  3535: iload           10
        //  3537: putfield        android/graphics/BitmapFactory$Options.inSampleSize:I
        //  3540: aload           13
        //  3542: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //  3545: aload           33
        //  3547: invokestatic    android/graphics/BitmapFactory.decodeFile:(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //  3550: astore          13
        //  3552: aload           13
        //  3554: ifnull          3568
        //  3557: aload           38
        //  3559: aload           13
        //  3561: invokevirtual   androidx/core/app/NotificationCompat$Builder.setLargeIcon:(Landroid/graphics/Bitmap;)Landroidx/core/app/NotificationCompat$Builder;
        //  3564: pop            
        //  3565: goto            3568
        //  3568: aconst_null    
        //  3569: astore          33
        //  3571: iload_1        
        //  3572: ifeq            3698
        //  3575: iload           17
        //  3577: iconst_1       
        //  3578: if_icmpne       3584
        //  3581: goto            3698
        //  3584: iload           36
        //  3586: ifne            3610
        //  3589: aload           38
        //  3591: iconst_0       
        //  3592: invokevirtual   androidx/core/app/NotificationCompat$Builder.setPriority:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  3595: pop            
        //  3596: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3599: bipush          26
        //  3601: if_icmplt       3719
        //  3604: iconst_3       
        //  3605: istore          10
        //  3607: goto            3722
        //  3610: iload           36
        //  3612: iconst_1       
        //  3613: if_icmpeq       3677
        //  3616: iload           36
        //  3618: iconst_2       
        //  3619: if_icmpne       3625
        //  3622: goto            3677
        //  3625: iload           36
        //  3627: iconst_4       
        //  3628: if_icmpne       3653
        //  3631: aload           38
        //  3633: bipush          -2
        //  3635: invokevirtual   androidx/core/app/NotificationCompat$Builder.setPriority:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  3638: pop            
        //  3639: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3642: bipush          26
        //  3644: if_icmplt       3719
        //  3647: iconst_1       
        //  3648: istore          10
        //  3650: goto            3722
        //  3653: iload           36
        //  3655: iconst_5       
        //  3656: if_icmpne       3719
        //  3659: aload           38
        //  3661: iconst_m1      
        //  3662: invokevirtual   androidx/core/app/NotificationCompat$Builder.setPriority:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  3665: pop            
        //  3666: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3669: bipush          26
        //  3671: if_icmplt       3719
        //  3674: goto            3713
        //  3677: aload           38
        //  3679: iconst_1       
        //  3680: invokevirtual   androidx/core/app/NotificationCompat$Builder.setPriority:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  3683: pop            
        //  3684: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3687: bipush          26
        //  3689: if_icmplt       3719
        //  3692: iconst_4       
        //  3693: istore          10
        //  3695: goto            3722
        //  3698: aload           38
        //  3700: iconst_m1      
        //  3701: invokevirtual   androidx/core/app/NotificationCompat$Builder.setPriority:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  3704: pop            
        //  3705: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3708: bipush          26
        //  3710: if_icmplt       3719
        //  3713: iconst_2       
        //  3714: istore          10
        //  3716: goto            3722
        //  3719: iconst_0       
        //  3720: istore          10
        //  3722: iload           17
        //  3724: iconst_1       
        //  3725: if_icmpeq       4190
        //  3728: iload           26
        //  3730: ifne            4190
        //  3733: getstatic       org/telegram/messenger/ApplicationLoader.mainInterfacePaused:Z
        //  3736: ifne            3744
        //  3739: iload           15
        //  3741: ifeq            3813
        //  3744: aload_3        
        //  3745: astore          13
        //  3747: aload_3        
        //  3748: invokevirtual   java/lang/String.length:()I
        //  3751: bipush          100
        //  3753: if_icmple       3805
        //  3756: new             Ljava/lang/StringBuilder;
        //  3759: astore          13
        //  3761: aload           13
        //  3763: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3766: aload           13
        //  3768: aload_3        
        //  3769: iconst_0       
        //  3770: bipush          100
        //  3772: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  3775: bipush          10
        //  3777: bipush          32
        //  3779: invokevirtual   java/lang/String.replace:(CC)Ljava/lang/String;
        //  3782: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //  3785: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3788: pop            
        //  3789: aload           13
        //  3791: ldc_w           "..."
        //  3794: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3797: pop            
        //  3798: aload           13
        //  3800: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3803: astore          13
        //  3805: aload           38
        //  3807: aload           13
        //  3809: invokevirtual   androidx/core/app/NotificationCompat$Builder.setTicker:(Ljava/lang/CharSequence;)Landroidx/core/app/NotificationCompat$Builder;
        //  3812: pop            
        //  3813: invokestatic    org/telegram/messenger/MediaController.getInstance:()Lorg/telegram/messenger/MediaController;
        //  3816: invokevirtual   org/telegram/messenger/MediaController.isRecordingAudio:()Z
        //  3819: ifne            4014
        //  3822: aload           37
        //  3824: ifnull          4014
        //  3827: aload           37
        //  3829: ldc_w           "NoSound"
        //  3832: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3835: ifne            4014
        //  3838: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3841: bipush          26
        //  3843: if_icmplt       3874
        //  3846: aload           37
        //  3848: aload           21
        //  3850: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3853: ifeq            3864
        //  3856: getstatic       android/provider/Settings$System.DEFAULT_NOTIFICATION_URI:Landroid/net/Uri;
        //  3859: astore          13
        //  3861: goto            4017
        //  3864: aload           37
        //  3866: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  3869: astore          13
        //  3871: goto            4017
        //  3874: aload           37
        //  3876: aload           21
        //  3878: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  3881: ifeq            3897
        //  3884: aload           38
        //  3886: getstatic       android/provider/Settings$System.DEFAULT_NOTIFICATION_URI:Landroid/net/Uri;
        //  3889: iconst_5       
        //  3890: invokevirtual   androidx/core/app/NotificationCompat$Builder.setSound:(Landroid/net/Uri;I)Landroidx/core/app/NotificationCompat$Builder;
        //  3893: pop            
        //  3894: goto            4014
        //  3897: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3900: bipush          24
        //  3902: if_icmplt       4002
        //  3905: aload           37
        //  3907: ldc_w           "file://"
        //  3910: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //  3913: ifeq            4002
        //  3916: aload           37
        //  3918: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  3921: invokestatic    org/telegram/messenger/AndroidUtilities.isInternalUri:(Landroid/net/Uri;)Z
        //  3924: istore          15
        //  3926: iload           15
        //  3928: ifne            4002
        //  3931: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  3934: astore          13
        //  3936: new             Ljava/io/File;
        //  3939: astore_3       
        //  3940: aload_3        
        //  3941: aload           37
        //  3943: ldc_w           "file://"
        //  3946: ldc             ""
        //  3948: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //  3951: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //  3954: aload           13
        //  3956: ldc_w           "org.telegram.messenger.provider"
        //  3959: aload_3        
        //  3960: invokestatic    androidx/core/content/FileProvider.getUriForFile:(Landroid/content/Context;Ljava/lang/String;Ljava/io/File;)Landroid/net/Uri;
        //  3963: astore_3       
        //  3964: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  3967: ldc_w           "com.android.systemui"
        //  3970: aload_3        
        //  3971: iconst_1       
        //  3972: invokevirtual   android/content/Context.grantUriPermission:(Ljava/lang/String;Landroid/net/Uri;I)V
        //  3975: aload           38
        //  3977: aload_3        
        //  3978: iconst_5       
        //  3979: invokevirtual   androidx/core/app/NotificationCompat$Builder.setSound:(Landroid/net/Uri;I)Landroidx/core/app/NotificationCompat$Builder;
        //  3982: pop            
        //  3983: goto            4014
        //  3986: astore_3       
        //  3987: aload           38
        //  3989: aload           37
        //  3991: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  3994: iconst_5       
        //  3995: invokevirtual   androidx/core/app/NotificationCompat$Builder.setSound:(Landroid/net/Uri;I)Landroidx/core/app/NotificationCompat$Builder;
        //  3998: pop            
        //  3999: goto            4014
        //  4002: aload           38
        //  4004: aload           37
        //  4006: invokestatic    android/net/Uri.parse:(Ljava/lang/String;)Landroid/net/Uri;
        //  4009: iconst_5       
        //  4010: invokevirtual   androidx/core/app/NotificationCompat$Builder.setSound:(Landroid/net/Uri;I)Landroidx/core/app/NotificationCompat$Builder;
        //  4013: pop            
        //  4014: aconst_null    
        //  4015: astore          13
        //  4017: iload           11
        //  4019: ifeq            4039
        //  4022: aload           38
        //  4024: iload           11
        //  4026: sipush          1000
        //  4029: sipush          1000
        //  4032: invokevirtual   androidx/core/app/NotificationCompat$Builder.setLights:(III)Landroidx/core/app/NotificationCompat$Builder;
        //  4035: pop            
        //  4036: goto            4039
        //  4039: iload           19
        //  4041: iconst_2       
        //  4042: if_icmpeq       4161
        //  4045: invokestatic    org/telegram/messenger/MediaController.getInstance:()Lorg/telegram/messenger/MediaController;
        //  4048: invokevirtual   org/telegram/messenger/MediaController.isRecordingAudio:()Z
        //  4051: ifeq            4057
        //  4054: goto            4161
        //  4057: iload           19
        //  4059: iconst_1       
        //  4060: if_icmpne       4097
        //  4063: iconst_4       
        //  4064: newarray        J
        //  4066: astore_3       
        //  4067: aload_3        
        //  4068: iconst_0       
        //  4069: lconst_0       
        //  4070: lastore        
        //  4071: aload_3        
        //  4072: iconst_1       
        //  4073: ldc2_w          100
        //  4076: lastore        
        //  4077: aload_3        
        //  4078: iconst_2       
        //  4079: lconst_0       
        //  4080: lastore        
        //  4081: aload_3        
        //  4082: iconst_3       
        //  4083: ldc2_w          100
        //  4086: lastore        
        //  4087: aload           38
        //  4089: aload_3        
        //  4090: invokevirtual   androidx/core/app/NotificationCompat$Builder.setVibrate:([J)Landroidx/core/app/NotificationCompat$Builder;
        //  4093: pop            
        //  4094: goto            4180
        //  4097: iload           19
        //  4099: ifeq            4147
        //  4102: iload           19
        //  4104: iconst_4       
        //  4105: if_icmpne       4111
        //  4108: goto            4147
        //  4111: iload           19
        //  4113: iconst_3       
        //  4114: if_icmpne       4141
        //  4117: iconst_2       
        //  4118: newarray        J
        //  4120: astore_3       
        //  4121: aload_3        
        //  4122: iconst_0       
        //  4123: lconst_0       
        //  4124: lastore        
        //  4125: aload_3        
        //  4126: iconst_1       
        //  4127: ldc2_w          1000
        //  4130: lastore        
        //  4131: aload           38
        //  4133: aload_3        
        //  4134: invokevirtual   androidx/core/app/NotificationCompat$Builder.setVibrate:([J)Landroidx/core/app/NotificationCompat$Builder;
        //  4137: pop            
        //  4138: goto            4180
        //  4141: aload           33
        //  4143: astore_3       
        //  4144: goto            4180
        //  4147: aload           38
        //  4149: iconst_2       
        //  4150: invokevirtual   androidx/core/app/NotificationCompat$Builder.setDefaults:(I)Landroidx/core/app/NotificationCompat$Builder;
        //  4153: pop            
        //  4154: iconst_0       
        //  4155: newarray        J
        //  4157: astore_3       
        //  4158: goto            4180
        //  4161: iconst_2       
        //  4162: newarray        J
        //  4164: astore_3       
        //  4165: aload_3        
        //  4166: iconst_0       
        //  4167: lconst_0       
        //  4168: lastore        
        //  4169: aload_3        
        //  4170: iconst_1       
        //  4171: lconst_0       
        //  4172: lastore        
        //  4173: aload           38
        //  4175: aload_3        
        //  4176: invokevirtual   androidx/core/app/NotificationCompat$Builder.setVibrate:([J)Landroidx/core/app/NotificationCompat$Builder;
        //  4179: pop            
        //  4180: aload           13
        //  4182: astore          33
        //  4184: aload_3        
        //  4185: astore          13
        //  4187: goto            4216
        //  4190: iconst_2       
        //  4191: newarray        J
        //  4193: astore          13
        //  4195: aload           13
        //  4197: iconst_0       
        //  4198: lconst_0       
        //  4199: lastore        
        //  4200: aload           13
        //  4202: iconst_1       
        //  4203: lconst_0       
        //  4204: lastore        
        //  4205: aload           38
        //  4207: aload           13
        //  4209: invokevirtual   androidx/core/app/NotificationCompat$Builder.setVibrate:([J)Landroidx/core/app/NotificationCompat$Builder;
        //  4212: pop            
        //  4213: aconst_null    
        //  4214: astore          33
        //  4216: iload           11
        //  4218: istore          17
        //  4220: iconst_0       
        //  4221: invokestatic    org/telegram/messenger/AndroidUtilities.needShowPasscode:(Z)Z
        //  4224: ifne            4521
        //  4227: getstatic       org/telegram/messenger/SharedConfig.isWaitingForPasscodeEnter:Z
        //  4230: ifne            4521
        //  4233: aload           29
        //  4235: invokevirtual   org/telegram/messenger/MessageObject.getDialogId:()J
        //  4238: ldc2_w          777000
        //  4241: lcmp           
        //  4242: ifne            4521
        //  4245: aload           29
        //  4247: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  4250: getfield        org/telegram/tgnet/TLRPC$Message.reply_markup:Lorg/telegram/tgnet/TLRPC$ReplyMarkup;
        //  4253: ifnull          4521
        //  4256: aload           29
        //  4258: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //  4261: getfield        org/telegram/tgnet/TLRPC$Message.reply_markup:Lorg/telegram/tgnet/TLRPC$ReplyMarkup;
        //  4264: getfield        org/telegram/tgnet/TLRPC$ReplyMarkup.rows:Ljava/util/ArrayList;
        //  4267: astore          35
        //  4269: aload           35
        //  4271: invokevirtual   java/util/ArrayList.size:()I
        //  4274: istore          4
        //  4276: iconst_0       
        //  4277: istore          20
        //  4279: iconst_0       
        //  4280: istore          11
        //  4282: aload           29
        //  4284: astore_3       
        //  4285: aload           35
        //  4287: astore          29
        //  4289: iload           20
        //  4291: iload           4
        //  4293: if_icmpge       4515
        //  4296: aload           29
        //  4298: iload           20
        //  4300: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  4303: checkcast       Lorg/telegram/tgnet/TLRPC$TL_keyboardButtonRow;
        //  4306: astore          35
        //  4308: aload           35
        //  4310: getfield        org/telegram/tgnet/TLRPC$TL_keyboardButtonRow.buttons:Ljava/util/ArrayList;
        //  4313: invokevirtual   java/util/ArrayList.size:()I
        //  4316: istore          18
        //  4318: iconst_0       
        //  4319: istore          19
        //  4321: iload           11
        //  4323: istore          9
        //  4325: iload           4
        //  4327: istore          11
        //  4329: iload           19
        //  4331: istore          4
        //  4333: iload           4
        //  4335: iload           18
        //  4337: if_icmpge       4501
        //  4340: aload           35
        //  4342: getfield        org/telegram/tgnet/TLRPC$TL_keyboardButtonRow.buttons:Ljava/util/ArrayList;
        //  4345: iload           4
        //  4347: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  4350: checkcast       Lorg/telegram/tgnet/TLRPC$KeyboardButton;
        //  4353: astore          37
        //  4355: aload           37
        //  4357: instanceof      Lorg/telegram/tgnet/TLRPC$TL_keyboardButtonCallback;
        //  4360: ifeq            4495
        //  4363: new             Landroid/content/Intent;
        //  4366: astore          31
        //  4368: aload           31
        //  4370: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  4373: ldc_w           Lorg/telegram/messenger/NotificationCallbackReceiver;.class
        //  4376: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //  4379: aload           31
        //  4381: aload_2        
        //  4382: aload           30
        //  4384: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //  4387: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  4390: pop            
        //  4391: aload           31
        //  4393: ldc_w           "did"
        //  4396: lload           5
        //  4398: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;J)Landroid/content/Intent;
        //  4401: pop            
        //  4402: aload           37
        //  4404: getfield        org/telegram/tgnet/TLRPC$KeyboardButton.data:[B
        //  4407: ifnull          4427
        //  4410: aload           31
        //  4412: ldc_w           "data"
        //  4415: aload           37
        //  4417: getfield        org/telegram/tgnet/TLRPC$KeyboardButton.data:[B
        //  4420: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;[B)Landroid/content/Intent;
        //  4423: pop            
        //  4424: goto            4427
        //  4427: aload           31
        //  4429: ldc_w           "mid"
        //  4432: aload_3        
        //  4433: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        //  4436: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  4439: pop            
        //  4440: aload           37
        //  4442: getfield        org/telegram/tgnet/TLRPC$KeyboardButton.text:Ljava/lang/String;
        //  4445: astore          37
        //  4447: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  4450: astore          32
        //  4452: aload           30
        //  4454: getfield        org/telegram/messenger/NotificationsController.lastButtonId:I
        //  4457: istore          9
        //  4459: aload           30
        //  4461: iload           9
        //  4463: iconst_1       
        //  4464: iadd           
        //  4465: putfield        org/telegram/messenger/NotificationsController.lastButtonId:I
        //  4468: aload           38
        //  4470: iconst_0       
        //  4471: aload           37
        //  4473: aload           32
        //  4475: iload           9
        //  4477: aload           31
        //  4479: ldc_w           134217728
        //  4482: invokestatic    android/app/PendingIntent.getBroadcast:(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
        //  4485: invokevirtual   androidx/core/app/NotificationCompat$Builder.addAction:(ILjava/lang/CharSequence;Landroid/app/PendingIntent;)Landroidx/core/app/NotificationCompat$Builder;
        //  4488: pop            
        //  4489: iconst_1       
        //  4490: istore          9
        //  4492: goto            4495
        //  4495: iinc            4, 1
        //  4498: goto            4333
        //  4501: iinc            20, 1
        //  4504: iload           11
        //  4506: istore          4
        //  4508: iload           9
        //  4510: istore          11
        //  4512: goto            4289
        //  4515: aload           28
        //  4517: astore_3       
        //  4518: goto            4527
        //  4521: aload           28
        //  4523: astore_3       
        //  4524: iconst_0       
        //  4525: istore          11
        //  4527: iload           11
        //  4529: ifne            4655
        //  4532: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  4535: bipush          24
        //  4537: if_icmpge       4655
        //  4540: getstatic       org/telegram/messenger/SharedConfig.passcodeHash:Ljava/lang/String;
        //  4543: invokevirtual   java/lang/String.length:()I
        //  4546: ifne            4655
        //  4549: aload_0        
        //  4550: invokevirtual   org/telegram/messenger/NotificationsController.hasMessagesToReply:()Z
        //  4553: ifeq            4655
        //  4556: new             Landroid/content/Intent;
        //  4559: astore          28
        //  4561: aload           28
        //  4563: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  4566: ldc_w           Lorg/telegram/messenger/PopupReplyReceiver;.class
        //  4569: invokespecial   android/content/Intent.<init>:(Landroid/content/Context;Ljava/lang/Class;)V
        //  4572: aload           28
        //  4574: aload_2        
        //  4575: aload           30
        //  4577: getfield        org/telegram/messenger/NotificationsController.currentAccount:I
        //  4580: invokevirtual   android/content/Intent.putExtra:(Ljava/lang/String;I)Landroid/content/Intent;
        //  4583: pop            
        //  4584: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  4587: bipush          19
        //  4589: if_icmpgt       4625
        //  4592: aload           38
        //  4594: ldc_w           2131165418
        //  4597: ldc_w           "Reply"
        //  4600: ldc_w           2131560565
        //  4603: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  4606: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  4609: iconst_2       
        //  4610: aload           28
        //  4612: ldc_w           134217728
        //  4615: invokestatic    android/app/PendingIntent.getBroadcast:(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
        //  4618: invokevirtual   androidx/core/app/NotificationCompat$Builder.addAction:(ILjava/lang/CharSequence;Landroid/app/PendingIntent;)Landroidx/core/app/NotificationCompat$Builder;
        //  4621: pop            
        //  4622: goto            4655
        //  4625: aload           38
        //  4627: ldc_w           2131165417
        //  4630: ldc_w           "Reply"
        //  4633: ldc_w           2131560565
        //  4636: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  4639: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //  4642: iconst_2       
        //  4643: aload           28
        //  4645: ldc_w           134217728
        //  4648: invokestatic    android/app/PendingIntent.getBroadcast:(Landroid/content/Context;ILandroid/content/Intent;I)Landroid/app/PendingIntent;
        //  4651: invokevirtual   androidx/core/app/NotificationCompat$Builder.addAction:(ILjava/lang/CharSequence;Landroid/app/PendingIntent;)Landroidx/core/app/NotificationCompat$Builder;
        //  4654: pop            
        //  4655: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  4658: bipush          26
        //  4660: if_icmplt       4694
        //  4663: aload           38
        //  4665: aload_0        
        //  4666: lload           5
        //  4668: aload           12
        //  4670: aload           13
        //  4672: iload           17
        //  4674: aload           33
        //  4676: iload           10
        //  4678: aload           16
        //  4680: aload           27
        //  4682: iload           14
        //  4684: invokespecial   org/telegram/messenger/NotificationsController.validateChannelId:(JLjava/lang/String;[JILandroid/net/Uri;I[JLandroid/net/Uri;I)Ljava/lang/String;
        //  4687: invokevirtual   androidx/core/app/NotificationCompat$Builder.setChannelId:(Ljava/lang/String;)Landroidx/core/app/NotificationCompat$Builder;
        //  4690: pop            
        //  4691: goto            4694
        //  4694: aload           30
        //  4696: aload           38
        //  4698: iload_1        
        //  4699: aload_3        
        //  4700: invokespecial   org/telegram/messenger/NotificationsController.showExtraNotifications:(Landroidx/core/app/NotificationCompat$Builder;ZLjava/lang/String;)V
        //  4703: aload_0        
        //  4704: invokespecial   org/telegram/messenger/NotificationsController.scheduleNotificationRepeat:()V
        //  4707: goto            4717
        //  4710: astore          16
        //  4712: aload           16
        //  4714: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  4717: return         
        //  4718: aload_0        
        //  4719: invokespecial   org/telegram/messenger/NotificationsController.dismissNotification:()V
        //  4722: return         
        //  4723: astore          13
        //  4725: goto            3568
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  42     100    4710   4712   Ljava/lang/Exception;
        //  101    127    4710   4712   Ljava/lang/Exception;
        //  134    164    4710   4712   Ljava/lang/Exception;
        //  167    179    4710   4712   Ljava/lang/Exception;
        //  179    191    4710   4712   Ljava/lang/Exception;
        //  196    205    4710   4712   Ljava/lang/Exception;
        //  212    236    4710   4712   Ljava/lang/Exception;
        //  236    253    4710   4712   Ljava/lang/Exception;
        //  258    291    4710   4712   Ljava/lang/Exception;
        //  313    322    4710   4712   Ljava/lang/Exception;
        //  328    336    4710   4712   Ljava/lang/Exception;
        //  393    519    4710   4712   Ljava/lang/Exception;
        //  535    549    4710   4712   Ljava/lang/Exception;
        //  554    584    4710   4712   Ljava/lang/Exception;
        //  587    594    4710   4712   Ljava/lang/Exception;
        //  598    629    4710   4712   Ljava/lang/Exception;
        //  632    639    4710   4712   Ljava/lang/Exception;
        //  646    663    4710   4712   Ljava/lang/Exception;
        //  676    773    4710   4712   Ljava/lang/Exception;
        //  778    901    4710   4712   Ljava/lang/Exception;
        //  932    942    1297   1302   Ljava/lang/Exception;
        //  957    970    1297   1302   Ljava/lang/Exception;
        //  970    1008   1297   1302   Ljava/lang/Exception;
        //  1020   1030   1297   1302   Ljava/lang/Exception;
        //  1045   1058   1297   1302   Ljava/lang/Exception;
        //  1058   1096   1297   1302   Ljava/lang/Exception;
        //  1113   1123   1297   1302   Ljava/lang/Exception;
        //  1138   1151   1297   1302   Ljava/lang/Exception;
        //  1151   1189   1297   1302   Ljava/lang/Exception;
        //  1212   1222   1297   1302   Ljava/lang/Exception;
        //  1222   1239   1297   1302   Ljava/lang/Exception;
        //  1239   1294   1297   1302   Ljava/lang/Exception;
        //  1397   1402   1297   1302   Ljava/lang/Exception;
        //  1497   1505   1530   1541   Ljava/lang/Exception;
        //  1532   1537   1297   1302   Ljava/lang/Exception;
        //  1541   1549   1297   1302   Ljava/lang/Exception;
        //  1555   1568   1297   1302   Ljava/lang/Exception;
        //  1577   1582   1297   1302   Ljava/lang/Exception;
        //  1629   1634   1297   1302   Ljava/lang/Exception;
        //  1655   1660   1297   1302   Ljava/lang/Exception;
        //  1665   1691   1297   1302   Ljava/lang/Exception;
        //  1694   1701   1297   1302   Ljava/lang/Exception;
        //  1795   1862   1297   1302   Ljava/lang/Exception;
        //  1874   1885   4710   4712   Ljava/lang/Exception;
        //  1890   1901   4710   4712   Ljava/lang/Exception;
        //  1909   1920   4710   4712   Ljava/lang/Exception;
        //  1920   1933   4710   4712   Ljava/lang/Exception;
        //  1936   1955   4710   4712   Ljava/lang/Exception;
        //  1963   2016   4710   4712   Ljava/lang/Exception;
        //  2030   2083   4710   4712   Ljava/lang/Exception;
        //  2095   2130   4710   4712   Ljava/lang/Exception;
        //  2157   2164   4710   4712   Ljava/lang/Exception;
        //  2169   2184   4710   4712   Ljava/lang/Exception;
        //  2184   2197   4710   4712   Ljava/lang/Exception;
        //  2212   2225   4710   4712   Ljava/lang/Exception;
        //  2233   2240   4710   4712   Ljava/lang/Exception;
        //  2243   2250   4710   4712   Ljava/lang/Exception;
        //  2255   2280   4710   4712   Ljava/lang/Exception;
        //  2293   2304   4710   4712   Ljava/lang/Exception;
        //  2307   2312   4710   4712   Ljava/lang/Exception;
        //  2318   2346   4710   4712   Ljava/lang/Exception;
        //  2349   2395   4710   4712   Ljava/lang/Exception;
        //  2402   2414   4710   4712   Ljava/lang/Exception;
        //  2418   2426   4710   4712   Ljava/lang/Exception;
        //  2448   2502   4710   4712   Ljava/lang/Exception;
        //  2505   2580   4710   4712   Ljava/lang/Exception;
        //  2583   2700   4710   4712   Ljava/lang/Exception;
        //  2710   2770   4710   4712   Ljava/lang/Exception;
        //  2770   2824   4710   4712   Ljava/lang/Exception;
        //  2840   2881   4710   4712   Ljava/lang/Exception;
        //  2891   2932   4710   4712   Ljava/lang/Exception;
        //  2935   2976   4710   4712   Ljava/lang/Exception;
        //  2983   3017   4710   4712   Ljava/lang/Exception;
        //  3032   3078   4710   4712   Ljava/lang/Exception;
        //  3097   3123   4710   4712   Ljava/lang/Exception;
        //  3128   3140   4710   4712   Ljava/lang/Exception;
        //  3153   3162   4710   4712   Ljava/lang/Exception;
        //  3169   3181   4710   4712   Ljava/lang/Exception;
        //  3194   3229   4710   4712   Ljava/lang/Exception;
        //  3239   3274   4710   4712   Ljava/lang/Exception;
        //  3277   3312   4710   4712   Ljava/lang/Exception;
        //  3312   3319   4710   4712   Ljava/lang/Exception;
        //  3337   3353   4710   4712   Ljava/lang/Exception;
        //  3371   3387   4710   4712   Ljava/lang/Exception;
        //  3391   3438   4710   4712   Ljava/lang/Exception;
        //  3443   3457   4710   4712   Ljava/lang/Exception;
        //  3462   3473   4710   4712   Ljava/lang/Exception;
        //  3476   3515   4723   4728   Ljava/lang/Throwable;
        //  3533   3552   4723   4728   Ljava/lang/Throwable;
        //  3557   3565   4723   4728   Ljava/lang/Throwable;
        //  3589   3604   4710   4712   Ljava/lang/Exception;
        //  3631   3647   4710   4712   Ljava/lang/Exception;
        //  3659   3674   4710   4712   Ljava/lang/Exception;
        //  3677   3692   4710   4712   Ljava/lang/Exception;
        //  3698   3713   4710   4712   Ljava/lang/Exception;
        //  3733   3739   4710   4712   Ljava/lang/Exception;
        //  3747   3805   4710   4712   Ljava/lang/Exception;
        //  3805   3813   4710   4712   Ljava/lang/Exception;
        //  3813   3822   4710   4712   Ljava/lang/Exception;
        //  3827   3861   4710   4712   Ljava/lang/Exception;
        //  3864   3871   4710   4712   Ljava/lang/Exception;
        //  3874   3894   4710   4712   Ljava/lang/Exception;
        //  3897   3926   4710   4712   Ljava/lang/Exception;
        //  3931   3983   3986   4002   Ljava/lang/Exception;
        //  3987   3999   4710   4712   Ljava/lang/Exception;
        //  4002   4014   4710   4712   Ljava/lang/Exception;
        //  4022   4036   4710   4712   Ljava/lang/Exception;
        //  4045   4054   4710   4712   Ljava/lang/Exception;
        //  4063   4067   4710   4712   Ljava/lang/Exception;
        //  4087   4094   4710   4712   Ljava/lang/Exception;
        //  4117   4121   4710   4712   Ljava/lang/Exception;
        //  4131   4138   4710   4712   Ljava/lang/Exception;
        //  4147   4158   4710   4712   Ljava/lang/Exception;
        //  4161   4165   4710   4712   Ljava/lang/Exception;
        //  4173   4180   4710   4712   Ljava/lang/Exception;
        //  4190   4195   4710   4712   Ljava/lang/Exception;
        //  4205   4213   4710   4712   Ljava/lang/Exception;
        //  4220   4276   4710   4712   Ljava/lang/Exception;
        //  4296   4318   4710   4712   Ljava/lang/Exception;
        //  4340   4424   4710   4712   Ljava/lang/Exception;
        //  4427   4489   4710   4712   Ljava/lang/Exception;
        //  4532   4622   4710   4712   Ljava/lang/Exception;
        //  4625   4655   4710   4712   Ljava/lang/Exception;
        //  4655   4691   4710   4712   Ljava/lang/Exception;
        //  4694   4707   4710   4712   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_3476:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @TargetApi(26)
    private String validateChannelId(final long n, final String s, final long[] vibrationPattern, final int n2, final Uri uri, final int i, final long[] array, final Uri uri2, int j) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        final StringBuilder sb = new StringBuilder();
        sb.append("org.telegram.key");
        sb.append(n);
        final String string = sb.toString();
        final String string2 = notificationsSettings.getString(string, (String)null);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append("_s");
        final String string3 = notificationsSettings.getString(sb2.toString(), (String)null);
        final StringBuilder sb3 = new StringBuilder();
        for (j = 0; j < vibrationPattern.length; ++j) {
            sb3.append(vibrationPattern[j]);
        }
        sb3.append(n2);
        if (uri != null) {
            sb3.append(uri.toString());
        }
        sb3.append(i);
        final String md5 = Utilities.MD5(sb3.toString());
        String s2;
        if ((s2 = string2) != null) {
            s2 = string2;
            if (!string3.equals(md5)) {
                NotificationsController.systemNotificationManager.deleteNotificationChannel(string2);
                s2 = null;
            }
        }
        String string4;
        if ((string4 = s2) == null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(this.currentAccount);
            sb4.append("channel");
            sb4.append(n);
            sb4.append("_");
            sb4.append(Utilities.random.nextLong());
            string4 = sb4.toString();
            final NotificationChannel notificationChannel = new NotificationChannel(string4, (CharSequence)s, i);
            if (n2 != 0) {
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(n2);
            }
            if (!this.isEmptyVibration(vibrationPattern)) {
                notificationChannel.enableVibration(true);
                if (vibrationPattern != null && vibrationPattern.length > 0) {
                    notificationChannel.setVibrationPattern(vibrationPattern);
                }
            }
            else {
                notificationChannel.enableVibration(false);
            }
            final AudioAttributes$Builder audioAttributes$Builder = new AudioAttributes$Builder();
            audioAttributes$Builder.setContentType(4);
            audioAttributes$Builder.setUsage(5);
            if (uri != null) {
                notificationChannel.setSound(uri, audioAttributes$Builder.build());
            }
            else {
                notificationChannel.setSound((Uri)null, audioAttributes$Builder.build());
            }
            NotificationsController.systemNotificationManager.createNotificationChannel(notificationChannel);
            final SharedPreferences$Editor putString = notificationsSettings.edit().putString(string, string4);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(string);
            sb5.append("_s");
            putString.putString(sb5.toString(), md5).commit();
        }
        return string4;
    }
    
    public void cleanup() {
        this.popupMessages.clear();
        this.popupReplyMessages.clear();
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$A9SCTrujp78_YxIRivW7UAoIEBo(this));
    }
    
    @TargetApi(26)
    public void deleteAllNotificationChannels() {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$iv6fUe9w_2c54mbdiQOLFrNptrg(this));
    }
    
    @TargetApi(26)
    public void deleteNotificationChannel(final long n) {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$eYqBa_GxEYzKlHSSB2VWl64XX2Q(this, n));
    }
    
    protected void forceShowPopupForReply() {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$eQV_fs8YB0lhGMYS2TKm4CX_EZk(this));
    }
    
    public String getGlobalNotificationsKey(final int n) {
        if (n == 0) {
            return "EnableGroup2";
        }
        if (n == 1) {
            return "EnableAll2";
        }
        return "EnableChannel2";
    }
    
    public int getTotalUnreadCount() {
        return this.total_unread_count;
    }
    
    public boolean hasMessagesToReply() {
        for (int i = 0; i < this.pushMessages.size(); ++i) {
            final MessageObject messageObject = this.pushMessages.get(i);
            final long dialogId = messageObject.getDialogId();
            final TLRPC.Message messageOwner = messageObject.messageOwner;
            if ((!messageOwner.mentioned || !(messageOwner.action instanceof TLRPC.TL_messageActionPinMessage)) && (int)dialogId != 0 && (messageObject.messageOwner.to_id.channel_id == 0 || messageObject.isMegagroup())) {
                return true;
            }
        }
        return false;
    }
    
    public void hideNotifications() {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$_1VL5AJa2XU8eBaEZNLOYhMw8bE(this));
    }
    
    public boolean isGlobalNotificationsEnabled(final int n) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        final String globalNotificationsKey = this.getGlobalNotificationsKey(n);
        boolean b = false;
        if (notificationsSettings.getInt(globalNotificationsKey, 0) < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
            b = true;
        }
        return b;
    }
    
    public boolean isGlobalNotificationsEnabled(final long n) {
        final int n2 = (int)n;
        int n3;
        if (n2 < 0) {
            final TLRPC.Chat chat = MessagesController.getInstance(this.currentAccount).getChat(-n2);
            if (ChatObject.isChannel(chat) && !chat.megagroup) {
                n3 = 2;
            }
            else {
                n3 = 0;
            }
        }
        else {
            n3 = 1;
        }
        return this.isGlobalNotificationsEnabled(n3);
    }
    
    public void playOutChatSound() {
        if (this.inChatSoundEnabled) {
            if (!MediaController.getInstance().isRecordingAudio()) {
                try {
                    if (NotificationsController.audioManager.getRingerMode() == 0) {
                        return;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$9BWFjQml5zrAo3EV8FWEAyCpJLQ(this));
            }
        }
    }
    
    public void processDialogsUpdateRead(final LongSparseArray<Integer> longSparseArray) {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$bRv8AkmkiAwGyZ1dPg2TuCyHYS0(this, longSparseArray, new ArrayList()));
    }
    
    public void processLoadedUnreadMessages(final LongSparseArray<Integer> longSparseArray, final ArrayList<TLRPC.Message> list, final ArrayList<MessageObject> list2, final ArrayList<TLRPC.User> list3, final ArrayList<TLRPC.Chat> list4, final ArrayList<TLRPC.EncryptedChat> list5) {
        MessagesController.getInstance(this.currentAccount).putUsers(list3, true);
        MessagesController.getInstance(this.currentAccount).putChats(list4, true);
        MessagesController.getInstance(this.currentAccount).putEncryptedChats(list5, true);
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$XEAogHRWLk5KuijEFvgR3DVl_Oc(this, list, longSparseArray, list2));
    }
    
    public void processNewMessages(final ArrayList<MessageObject> list, final boolean b, final boolean b2, final CountDownLatch countDownLatch) {
        if (list.isEmpty()) {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
            return;
        }
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$blpPMIxTaKEgWkp2zDr1_y8eGUY(this, list, new ArrayList(0), b2, b, countDownLatch));
    }
    
    public void processReadMessages(final SparseLongArray sparseLongArray, final long n, final int n2, final int n3, final boolean b) {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$bn_qy54k0GHNymLhNYsBBa6g2mw(this, sparseLongArray, new ArrayList(0), n, n3, n2, b));
    }
    
    public void removeDeletedHisoryFromNotifications(final SparseIntArray sparseIntArray) {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$4ZPSiSXCXkKfxVPcPpmsFy8foEU(this, sparseIntArray, new ArrayList(0)));
    }
    
    public void removeDeletedMessagesFromNotifications(final SparseArray<ArrayList<Integer>> sparseArray) {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$8lQbr5XMNBt__wC6arYqfGdfeMk(this, sparseArray, new ArrayList(0)));
    }
    
    public void removeNotificationsForDialog(final long n) {
        getInstance(this.currentAccount).processReadMessages(null, n, 0, Integer.MAX_VALUE, false);
        final LongSparseArray longSparseArray = new LongSparseArray();
        longSparseArray.put(n, (Object)0);
        getInstance(this.currentAccount).processDialogsUpdateRead((LongSparseArray<Integer>)longSparseArray);
    }
    
    protected void repeatNotificationMaybe() {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$kDrFFl__TRrIJW3mtxiKJeeK1vw(this));
    }
    
    public void setDialogNotificationsSettings(final long lng, final int n) {
        final SharedPreferences$Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
        final TLRPC.Dialog dialog = (TLRPC.Dialog)MessagesController.getInstance(UserConfig.selectedAccount).dialogs_dict.get(lng);
        if (n == 4) {
            if (getInstance(this.currentAccount).isGlobalNotificationsEnabled(lng)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("notify2_");
                sb.append(lng);
                edit.remove(sb.toString());
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("notify2_");
                sb2.append(lng);
                edit.putInt(sb2.toString(), 0);
            }
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(lng, 0L);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
            }
        }
        else {
            int currentTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
            if (n == 0) {
                currentTime += 3600;
            }
            else if (n == 1) {
                currentTime += 28800;
            }
            else if (n == 2) {
                currentTime += 172800;
            }
            else if (n == 3) {
                currentTime = Integer.MAX_VALUE;
            }
            long n2 = 1L;
            if (n == 3) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("notify2_");
                sb3.append(lng);
                edit.putInt(sb3.toString(), 2);
            }
            else {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("notify2_");
                sb4.append(lng);
                edit.putInt(sb4.toString(), 3);
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("notifyuntil_");
                sb5.append(lng);
                edit.putInt(sb5.toString(), currentTime);
                n2 = (0x1L | (long)currentTime << 32);
            }
            getInstance(UserConfig.selectedAccount).removeNotificationsForDialog(lng);
            MessagesStorage.getInstance(UserConfig.selectedAccount).setDialogFlags(lng, n2);
            if (dialog != null) {
                dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
                dialog.notify_settings.mute_until = currentTime;
            }
        }
        edit.commit();
        this.updateServerNotificationsSettings(lng);
    }
    
    public void setGlobalNotificationsEnabled(final int n, final int n2) {
        MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt(this.getGlobalNotificationsKey(n), n2).commit();
        getInstance(this.currentAccount).updateServerNotificationsSettings(n);
    }
    
    public void setInChatSoundEnabled(final boolean inChatSoundEnabled) {
        this.inChatSoundEnabled = inChatSoundEnabled;
    }
    
    public void setLastOnlineFromOtherDevice(final int n) {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$aMKmdt9uT4z6_2MONOs1umiLD6k(this, n));
    }
    
    public void setOpenedDialogId(final long n) {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$XWu9HxcgJh0WGxxES9w4G4Lj_cA(this, n));
    }
    
    public void showNotifications() {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$tVtEcXBSUtzhqixsWunEmHPHAAI(this));
    }
    
    public void updateBadge() {
        NotificationsController.notificationsQueue.postRunnable(new _$$Lambda$NotificationsController$z9M3KFS8OpgW1aPw2rnfQYb2xt0(this));
    }
    
    public void updateServerNotificationsSettings(final int n) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        final TLRPC.TL_account_updateNotifySettings tl_account_updateNotifySettings = new TLRPC.TL_account_updateNotifySettings();
        tl_account_updateNotifySettings.settings = new TLRPC.TL_inputPeerNotifySettings();
        tl_account_updateNotifySettings.settings.flags = 5;
        if (n == 0) {
            tl_account_updateNotifySettings.peer = new TLRPC.TL_inputNotifyChats();
            tl_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableGroup2", 0);
            tl_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewGroup", true);
        }
        else if (n == 1) {
            tl_account_updateNotifySettings.peer = new TLRPC.TL_inputNotifyUsers();
            tl_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableAll2", 0);
            tl_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewAll", true);
        }
        else {
            tl_account_updateNotifySettings.peer = new TLRPC.TL_inputNotifyBroadcasts();
            tl_account_updateNotifySettings.settings.mute_until = notificationsSettings.getInt("EnableChannel2", 0);
            tl_account_updateNotifySettings.settings.show_previews = notificationsSettings.getBoolean("EnablePreviewChannel", true);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_updateNotifySettings, (RequestDelegate)_$$Lambda$NotificationsController$WV8JpQrNXdfWVJfPV9wKTUTuLBk.INSTANCE);
    }
    
    public void updateServerNotificationsSettings(final long n) {
        final NotificationCenter instance = NotificationCenter.getInstance(this.currentAccount);
        final int notificationsSettingsUpdated = NotificationCenter.notificationsSettingsUpdated;
        int mute_until = 0;
        instance.postNotificationName(notificationsSettingsUpdated, new Object[0]);
        final int n2 = (int)n;
        if (n2 == 0) {
            return;
        }
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
        final TLRPC.TL_account_updateNotifySettings tl_account_updateNotifySettings = new TLRPC.TL_account_updateNotifySettings();
        tl_account_updateNotifySettings.settings = new TLRPC.TL_inputPeerNotifySettings();
        final TLRPC.TL_inputPeerNotifySettings settings = tl_account_updateNotifySettings.settings;
        settings.flags |= 0x1;
        final StringBuilder sb = new StringBuilder();
        sb.append("preview_");
        sb.append(n);
        settings.show_previews = notificationsSettings.getBoolean(sb.toString(), true);
        final TLRPC.TL_inputPeerNotifySettings settings2 = tl_account_updateNotifySettings.settings;
        settings2.flags |= 0x2;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("silent_");
        sb2.append(n);
        settings2.silent = notificationsSettings.getBoolean(sb2.toString(), false);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("notify2_");
        sb3.append(n);
        final int int1 = notificationsSettings.getInt(sb3.toString(), -1);
        if (int1 != -1) {
            final TLRPC.TL_inputPeerNotifySettings settings3 = tl_account_updateNotifySettings.settings;
            settings3.flags |= 0x4;
            if (int1 == 3) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("notifyuntil_");
                sb4.append(n);
                settings3.mute_until = notificationsSettings.getInt(sb4.toString(), 0);
            }
            else {
                if (int1 == 2) {
                    mute_until = Integer.MAX_VALUE;
                }
                settings3.mute_until = mute_until;
            }
        }
        tl_account_updateNotifySettings.peer = new TLRPC.TL_inputNotifyPeer();
        ((TLRPC.TL_inputNotifyPeer)tl_account_updateNotifySettings.peer).peer = MessagesController.getInstance(this.currentAccount).getInputPeer(n2);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_updateNotifySettings, (RequestDelegate)_$$Lambda$NotificationsController$KyQqllEdy_fdmMCr6frsin2S3Cs.INSTANCE);
    }
    
    class NotificationHolder
    {
        int id;
        Notification notification;
        
        NotificationHolder(final int id, final Notification notification) {
            this.id = id;
            this.notification = notification;
        }
        
        void call() {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("show dialog notification with id ");
                sb.append(this.id);
                FileLog.w(sb.toString());
            }
            NotificationsController.notificationManager.notify(this.id, this.notification);
        }
    }
}
