// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.ui.ActionBar.Theme;
import android.content.Intent;
import android.telephony.TelephonyManager;
import androidx.core.app.NotificationManagerCompat;
import android.os.Build$VERSION;
import org.telegram.messenger.voip.VoIPService;
import android.widget.Toast;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.SQLite.SQLiteCursor;
import java.util.Locale;
import java.io.File;
import android.content.DialogInterface;
import java.util.concurrent.CountDownLatch;
import java.util.AbstractMap;
import org.telegram.ui.DialogsActivity;
import android.content.DialogInterface$OnCancelListener;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.NativeByteBuffer;
import java.util.Iterator;
import java.util.Map;
import android.app.Dialog;
import java.util.List;
import java.util.Collections;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ProfileActivity;
import android.os.Bundle;
import org.telegram.ui.ActionBar.BaseFragment;
import android.content.DialogInterface$OnClickListener;
import android.app.Activity;
import org.telegram.ui.ActionBar.AlertDialog;
import android.os.SystemClock;
import org.telegram.tgnet.RequestDelegate;
import android.text.TextUtils;
import java.util.Collection;
import android.content.SharedPreferences$Editor;
import android.content.Context;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.support.SparseLongArray;
import java.util.HashMap;
import org.telegram.tgnet.TLObject;
import android.content.SharedPreferences;
import java.util.Comparator;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import java.util.concurrent.ConcurrentHashMap;
import android.util.SparseArray;
import android.util.SparseIntArray;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;

public class MessagesController implements NotificationCenterDelegate
{
    private static volatile MessagesController[] Instance;
    public static final int UPDATE_MASK_ALL = 1535;
    public static final int UPDATE_MASK_AVATAR = 2;
    public static final int UPDATE_MASK_CHAT = 8192;
    public static final int UPDATE_MASK_CHAT_AVATAR = 8;
    public static final int UPDATE_MASK_CHAT_MEMBERS = 32;
    public static final int UPDATE_MASK_CHAT_NAME = 16;
    public static final int UPDATE_MASK_CHECK = 65536;
    public static final int UPDATE_MASK_MESSAGE_TEXT = 32768;
    public static final int UPDATE_MASK_NAME = 1;
    public static final int UPDATE_MASK_NEW_MESSAGE = 2048;
    public static final int UPDATE_MASK_PHONE = 1024;
    public static final int UPDATE_MASK_READ_DIALOG_MESSAGE = 256;
    public static final int UPDATE_MASK_REORDER = 131072;
    public static final int UPDATE_MASK_SELECT_DIALOG = 512;
    public static final int UPDATE_MASK_SEND_STATE = 4096;
    public static final int UPDATE_MASK_STATUS = 4;
    public static final int UPDATE_MASK_USER_PHONE = 128;
    public static final int UPDATE_MASK_USER_PRINT = 64;
    private static volatile long lastPasswordCheckTime;
    private static volatile long lastThemeCheckTime;
    private int DIALOGS_LOAD_TYPE_CACHE;
    private int DIALOGS_LOAD_TYPE_CHANNEL;
    private int DIALOGS_LOAD_TYPE_UNKNOWN;
    protected ArrayList<TLRPC.Dialog> allDialogs;
    public int availableMapProviders;
    public boolean blockedCountry;
    public SparseIntArray blockedUsers;
    public int callConnectTimeout;
    public int callPacketTimeout;
    public int callReceiveTimeout;
    public int callRingTimeout;
    public boolean canRevokePmInbox;
    private SparseArray<ArrayList<Integer>> channelAdmins;
    private SparseArray<ArrayList<Integer>> channelViewsToSend;
    private SparseIntArray channelsPts;
    private ConcurrentHashMap<Integer, TLRPC.Chat> chats;
    private SparseBooleanArray checkingLastMessagesDialogs;
    private boolean checkingProxyInfo;
    private int checkingProxyInfoRequestId;
    private boolean checkingTosUpdate;
    private LongSparseArray<TLRPC.Dialog> clearingHistoryDialogs;
    private ArrayList<Long> createdDialogIds;
    private ArrayList<Long> createdDialogMainThreadIds;
    private int currentAccount;
    private Runnable currentDeleteTaskRunnable;
    private int currentDeletingTaskChannelId;
    private ArrayList<Integer> currentDeletingTaskMids;
    private int currentDeletingTaskTime;
    public String dcDomainName;
    public boolean defaultP2pContacts;
    public LongSparseArray<Integer> deletedHistory;
    private LongSparseArray<TLRPC.Dialog> deletingDialogs;
    private final Comparator<TLRPC.Dialog> dialogComparator;
    public LongSparseArray<MessageObject> dialogMessage;
    public SparseArray<MessageObject> dialogMessagesByIds;
    public LongSparseArray<MessageObject> dialogMessagesByRandomIds;
    private SparseArray<ArrayList<TLRPC.Dialog>> dialogsByFolder;
    public ArrayList<TLRPC.Dialog> dialogsCanAddUsers;
    public ArrayList<TLRPC.Dialog> dialogsChannelsOnly;
    private SparseBooleanArray dialogsEndReached;
    public ArrayList<TLRPC.Dialog> dialogsForward;
    public ArrayList<TLRPC.Dialog> dialogsGroupsOnly;
    private boolean dialogsInTransaction;
    public boolean dialogsLoaded;
    public ArrayList<TLRPC.Dialog> dialogsServerOnly;
    public ArrayList<TLRPC.Dialog> dialogsUsersOnly;
    public LongSparseArray<TLRPC.Dialog> dialogs_dict;
    public ConcurrentHashMap<Long, Integer> dialogs_read_inbox_max;
    public ConcurrentHashMap<Long, Integer> dialogs_read_outbox_max;
    private SharedPreferences emojiPreferences;
    public boolean enableJoined;
    private ConcurrentHashMap<Integer, TLRPC.EncryptedChat> encryptedChats;
    private SparseArray<TLRPC.ExportedChatInvite> exportedChats;
    public boolean firstGettingTask;
    private SparseArray<TLRPC.ChatFull> fullChats;
    private SparseArray<TLRPC.UserFull> fullUsers;
    private boolean getDifferenceFirstSync;
    public boolean gettingDifference;
    private SparseBooleanArray gettingDifferenceChannels;
    private boolean gettingNewDeleteTask;
    private SparseBooleanArray gettingUnknownChannels;
    private LongSparseArray<Boolean> gettingUnknownDialogs;
    public String gifSearchBot;
    public ArrayList<TLRPC.RecentMeUrl> hintDialogs;
    public String imageSearchBot;
    private String installReferer;
    private boolean isLeftProxyChannel;
    private ArrayList<Integer> joiningToChannels;
    private int lastPrintingStringCount;
    private long lastPushRegisterSendTime;
    private long lastStatusUpdateTime;
    private long lastViewsCheckTime;
    public String linkPrefix;
    private ArrayList<Integer> loadedFullChats;
    private ArrayList<Integer> loadedFullParticipants;
    private ArrayList<Integer> loadedFullUsers;
    public boolean loadingBlockedUsers;
    private SparseIntArray loadingChannelAdmins;
    private SparseBooleanArray loadingDialogs;
    private ArrayList<Integer> loadingFullChats;
    private ArrayList<Integer> loadingFullParticipants;
    private ArrayList<Integer> loadingFullUsers;
    private int loadingNotificationSettings;
    private boolean loadingNotificationSignUpSettings;
    private LongSparseArray<Boolean> loadingPeerSettings;
    private SparseIntArray loadingPinnedDialogs;
    private boolean loadingUnreadDialogs;
    private SharedPreferences mainPreferences;
    public String mapKey;
    public int mapProvider;
    public int maxBroadcastCount;
    public int maxCaptionLength;
    public int maxEditTime;
    public int maxFaveStickersCount;
    public int maxFolderPinnedDialogsCount;
    public int maxGroupCount;
    public int maxMegagroupCount;
    public int maxMessageLength;
    public int maxPinnedDialogsCount;
    public int maxRecentGifsCount;
    public int maxRecentStickersCount;
    private SparseIntArray migratedChats;
    private boolean migratingDialogs;
    public int minGroupConvertSize;
    private SparseIntArray needShortPollChannels;
    private SparseIntArray needShortPollOnlines;
    private SparseIntArray nextDialogsCacheOffset;
    private int nextProxyInfoCheckTime;
    private int nextTosCheckTime;
    private SharedPreferences notificationsPreferences;
    private ConcurrentHashMap<String, TLObject> objectsByUsernames;
    private boolean offlineSent;
    public ConcurrentHashMap<Integer, Integer> onlinePrivacy;
    private Runnable passwordCheckRunnable;
    private LongSparseArray<SparseArray<MessageObject>> pollsToCheck;
    private int pollsToCheckSize;
    public boolean preloadFeaturedStickers;
    public LongSparseArray<CharSequence> printingStrings;
    public LongSparseArray<Integer> printingStringsTypes;
    public ConcurrentHashMap<Long, ArrayList<PrintingUser>> printingUsers;
    private TLRPC.Dialog proxyDialog;
    private String proxyDialogAddress;
    private long proxyDialogId;
    public int ratingDecay;
    private ArrayList<ReadTask> readTasks;
    private LongSparseArray<ReadTask> readTasksMap;
    public boolean registeringForPush;
    private LongSparseArray<ArrayList<Integer>> reloadingMessages;
    private HashMap<String, ArrayList<MessageObject>> reloadingWebpages;
    private LongSparseArray<ArrayList<MessageObject>> reloadingWebpagesPending;
    private TLRPC.messages_Dialogs resetDialogsAll;
    private TLRPC.TL_messages_peerDialogs resetDialogsPinned;
    private boolean resetingDialogs;
    public int revokeTimeLimit;
    public int revokeTimePmLimit;
    public int secretWebpagePreview;
    public SparseArray<LongSparseArray<Boolean>> sendingTypings;
    private SparseBooleanArray serverDialogsEndReached;
    private SparseIntArray shortPollChannels;
    private SparseIntArray shortPollOnlines;
    private int statusRequest;
    private int statusSettingState;
    public boolean suggestContacts;
    public String suggestedLangCode;
    private Runnable themeCheckRunnable;
    public int unreadUnmutedDialogs;
    private final Comparator<TLRPC.Update> updatesComparator;
    private SparseArray<ArrayList<TLRPC.Updates>> updatesQueueChannels;
    private ArrayList<TLRPC.Updates> updatesQueuePts;
    private ArrayList<TLRPC.Updates> updatesQueueQts;
    private ArrayList<TLRPC.Updates> updatesQueueSeq;
    private SparseLongArray updatesStartWaitTimeChannels;
    private long updatesStartWaitTimePts;
    private long updatesStartWaitTimeQts;
    private long updatesStartWaitTimeSeq;
    public boolean updatingState;
    private String uploadingAvatar;
    private String uploadingWallpaper;
    private boolean uploadingWallpaperBlurred;
    private boolean uploadingWallpaperMotion;
    private ConcurrentHashMap<Integer, TLRPC.User> users;
    public String venueSearchBot;
    private ArrayList<Long> visibleDialogMainThreadIds;
    public int webFileDatacenterId;
    
    static {
        MessagesController.Instance = new MessagesController[3];
    }
    
    public MessagesController(int currentAccount) {
        final int n = 2;
        this.chats = new ConcurrentHashMap<Integer, TLRPC.Chat>(100, 1.0f, 2);
        this.encryptedChats = new ConcurrentHashMap<Integer, TLRPC.EncryptedChat>(10, 1.0f, 2);
        this.users = new ConcurrentHashMap<Integer, TLRPC.User>(100, 1.0f, 2);
        this.objectsByUsernames = new ConcurrentHashMap<String, TLObject>(100, 1.0f, 2);
        this.joiningToChannels = new ArrayList<Integer>();
        this.exportedChats = (SparseArray<TLRPC.ExportedChatInvite>)new SparseArray();
        this.hintDialogs = new ArrayList<TLRPC.RecentMeUrl>();
        this.dialogsByFolder = (SparseArray<ArrayList<TLRPC.Dialog>>)new SparseArray();
        this.allDialogs = new ArrayList<TLRPC.Dialog>();
        this.dialogsForward = new ArrayList<TLRPC.Dialog>();
        this.dialogsServerOnly = new ArrayList<TLRPC.Dialog>();
        this.dialogsCanAddUsers = new ArrayList<TLRPC.Dialog>();
        this.dialogsChannelsOnly = new ArrayList<TLRPC.Dialog>();
        this.dialogsUsersOnly = new ArrayList<TLRPC.Dialog>();
        this.dialogsGroupsOnly = new ArrayList<TLRPC.Dialog>();
        this.dialogs_read_inbox_max = new ConcurrentHashMap<Long, Integer>(100, 1.0f, 2);
        this.dialogs_read_outbox_max = new ConcurrentHashMap<Long, Integer>(100, 1.0f, 2);
        this.dialogs_dict = (LongSparseArray<TLRPC.Dialog>)new LongSparseArray();
        this.dialogMessage = (LongSparseArray<MessageObject>)new LongSparseArray();
        this.dialogMessagesByRandomIds = (LongSparseArray<MessageObject>)new LongSparseArray();
        this.deletedHistory = (LongSparseArray<Integer>)new LongSparseArray();
        this.dialogMessagesByIds = (SparseArray<MessageObject>)new SparseArray();
        this.printingUsers = new ConcurrentHashMap<Long, ArrayList<PrintingUser>>(20, 1.0f, 2);
        this.printingStrings = (LongSparseArray<CharSequence>)new LongSparseArray();
        this.printingStringsTypes = (LongSparseArray<Integer>)new LongSparseArray();
        this.sendingTypings = (SparseArray<LongSparseArray<Boolean>>)new SparseArray();
        this.onlinePrivacy = new ConcurrentHashMap<Integer, Integer>(20, 1.0f, 2);
        this.loadingPeerSettings = (LongSparseArray<Boolean>)new LongSparseArray();
        this.createdDialogIds = new ArrayList<Long>();
        this.createdDialogMainThreadIds = new ArrayList<Long>();
        this.visibleDialogMainThreadIds = new ArrayList<Long>();
        this.shortPollChannels = new SparseIntArray();
        this.needShortPollChannels = new SparseIntArray();
        this.shortPollOnlines = new SparseIntArray();
        this.needShortPollOnlines = new SparseIntArray();
        this.deletingDialogs = (LongSparseArray<TLRPC.Dialog>)new LongSparseArray();
        this.clearingHistoryDialogs = (LongSparseArray<TLRPC.Dialog>)new LongSparseArray();
        this.loadingBlockedUsers = false;
        this.blockedUsers = new SparseIntArray();
        this.channelViewsToSend = (SparseArray<ArrayList<Integer>>)new SparseArray();
        this.pollsToCheck = (LongSparseArray<SparseArray<MessageObject>>)new LongSparseArray();
        this.updatesQueueChannels = (SparseArray<ArrayList<TLRPC.Updates>>)new SparseArray();
        this.updatesStartWaitTimeChannels = new SparseLongArray();
        this.channelsPts = new SparseIntArray();
        this.gettingDifferenceChannels = new SparseBooleanArray();
        this.gettingUnknownChannels = new SparseBooleanArray();
        this.gettingUnknownDialogs = (LongSparseArray<Boolean>)new LongSparseArray();
        this.checkingLastMessagesDialogs = new SparseBooleanArray();
        this.updatesQueueSeq = new ArrayList<TLRPC.Updates>();
        this.updatesQueuePts = new ArrayList<TLRPC.Updates>();
        this.updatesQueueQts = new ArrayList<TLRPC.Updates>();
        this.fullUsers = (SparseArray<TLRPC.UserFull>)new SparseArray();
        this.fullChats = (SparseArray<TLRPC.ChatFull>)new SparseArray();
        this.loadingFullUsers = new ArrayList<Integer>();
        this.loadedFullUsers = new ArrayList<Integer>();
        this.loadingFullChats = new ArrayList<Integer>();
        this.loadingFullParticipants = new ArrayList<Integer>();
        this.loadedFullParticipants = new ArrayList<Integer>();
        this.loadedFullChats = new ArrayList<Integer>();
        this.channelAdmins = (SparseArray<ArrayList<Integer>>)new SparseArray();
        this.loadingChannelAdmins = new SparseIntArray();
        this.migratedChats = new SparseIntArray();
        this.reloadingWebpages = new HashMap<String, ArrayList<MessageObject>>();
        this.reloadingWebpagesPending = (LongSparseArray<ArrayList<MessageObject>>)new LongSparseArray();
        this.reloadingMessages = (LongSparseArray<ArrayList<Integer>>)new LongSparseArray();
        this.readTasks = new ArrayList<ReadTask>();
        this.readTasksMap = (LongSparseArray<ReadTask>)new LongSparseArray();
        this.nextDialogsCacheOffset = new SparseIntArray();
        this.loadingDialogs = new SparseBooleanArray();
        this.dialogsEndReached = new SparseBooleanArray();
        this.serverDialogsEndReached = new SparseBooleanArray();
        this.getDifferenceFirstSync = true;
        this.loadingPinnedDialogs = new SparseIntArray();
        this.suggestContacts = true;
        this.themeCheckRunnable = (Runnable)_$$Lambda$RQB0Jwr1FTqp6hrbGUHuOs_9k1I.INSTANCE;
        this.passwordCheckRunnable = new Runnable() {
            @Override
            public void run() {
                UserConfig.getInstance(MessagesController.this.currentAccount).checkSavedPassword();
            }
        };
        this.maxBroadcastCount = 100;
        this.minGroupConvertSize = 200;
        this.dialogComparator = new Comparator<TLRPC.Dialog>() {
            @Override
            public int compare(final TLRPC.Dialog dialog, final TLRPC.Dialog dialog2) {
                final boolean b = dialog instanceof TLRPC.TL_dialogFolder;
                if (b && !(dialog2 instanceof TLRPC.TL_dialogFolder)) {
                    return -1;
                }
                if (!b && dialog2 instanceof TLRPC.TL_dialogFolder) {
                    return 1;
                }
                if (!dialog.pinned && dialog2.pinned) {
                    return 1;
                }
                if (dialog.pinned && !dialog2.pinned) {
                    return -1;
                }
                if (dialog.pinned && dialog2.pinned) {
                    final int pinnedNum = dialog.pinnedNum;
                    final int pinnedNum2 = dialog2.pinnedNum;
                    if (pinnedNum < pinnedNum2) {
                        return 1;
                    }
                    if (pinnedNum > pinnedNum2) {
                        return -1;
                    }
                    return 0;
                }
                else {
                    final TLRPC.DraftMessage draft = DataQuery.getInstance(MessagesController.this.currentAccount).getDraft(dialog.id);
                    int n = 0;
                    Label_0158: {
                        if (draft != null) {
                            n = draft.date;
                            if (n >= dialog.last_message_date) {
                                break Label_0158;
                            }
                        }
                        n = dialog.last_message_date;
                    }
                    final TLRPC.DraftMessage draft2 = DataQuery.getInstance(MessagesController.this.currentAccount).getDraft(dialog2.id);
                    int n2 = 0;
                    Label_0204: {
                        if (draft2 != null) {
                            n2 = draft2.date;
                            if (n2 >= dialog2.last_message_date) {
                                break Label_0204;
                            }
                        }
                        n2 = dialog2.last_message_date;
                    }
                    if (n < n2) {
                        return 1;
                    }
                    if (n > n2) {
                        return -1;
                    }
                    return 0;
                }
            }
        };
        this.updatesComparator = (Comparator<TLRPC.Update>)new _$$Lambda$MessagesController$jdY_gZP_eY5WAh_nSc2UhUqpq4M(this);
        this.DIALOGS_LOAD_TYPE_CACHE = 1;
        this.DIALOGS_LOAD_TYPE_CHANNEL = 2;
        this.DIALOGS_LOAD_TYPE_UNKNOWN = 3;
        this.currentAccount = currentAccount;
        ImageLoader.getInstance();
        MessagesStorage.getInstance(this.currentAccount);
        LocationController.getInstance(this.currentAccount);
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$y42O__dKOKyzNi5k5aHwMy0RjOs(this));
        this.addSupportUser();
        if (this.currentAccount == 0) {
            this.notificationsPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            this.mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            this.emojiPreferences = ApplicationLoader.applicationContext.getSharedPreferences("emoji", 0);
        }
        else {
            final Context applicationContext = ApplicationLoader.applicationContext;
            final StringBuilder sb = new StringBuilder();
            sb.append("Notifications");
            sb.append(this.currentAccount);
            this.notificationsPreferences = applicationContext.getSharedPreferences(sb.toString(), 0);
            final Context applicationContext2 = ApplicationLoader.applicationContext;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("mainconfig");
            sb2.append(this.currentAccount);
            this.mainPreferences = applicationContext2.getSharedPreferences(sb2.toString(), 0);
            final Context applicationContext3 = ApplicationLoader.applicationContext;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("emoji");
            sb3.append(this.currentAccount);
            this.emojiPreferences = applicationContext3.getSharedPreferences(sb3.toString(), 0);
        }
        this.enableJoined = this.notificationsPreferences.getBoolean("EnableContactJoined", true);
        this.secretWebpagePreview = this.mainPreferences.getInt("secretWebpage2", 2);
        this.maxGroupCount = this.mainPreferences.getInt("maxGroupCount", 200);
        this.maxMegagroupCount = this.mainPreferences.getInt("maxMegagroupCount", 10000);
        this.maxRecentGifsCount = this.mainPreferences.getInt("maxRecentGifsCount", 200);
        this.maxRecentStickersCount = this.mainPreferences.getInt("maxRecentStickersCount", 30);
        this.maxFaveStickersCount = this.mainPreferences.getInt("maxFaveStickersCount", 5);
        this.maxEditTime = this.mainPreferences.getInt("maxEditTime", 3600);
        this.ratingDecay = this.mainPreferences.getInt("ratingDecay", 2419200);
        this.linkPrefix = this.mainPreferences.getString("linkPrefix", "t.me");
        this.callReceiveTimeout = this.mainPreferences.getInt("callReceiveTimeout", 20000);
        this.callRingTimeout = this.mainPreferences.getInt("callRingTimeout", 90000);
        this.callConnectTimeout = this.mainPreferences.getInt("callConnectTimeout", 30000);
        this.callPacketTimeout = this.mainPreferences.getInt("callPacketTimeout", 10000);
        this.maxPinnedDialogsCount = this.mainPreferences.getInt("maxPinnedDialogsCount", 5);
        this.maxFolderPinnedDialogsCount = this.mainPreferences.getInt("maxFolderPinnedDialogsCount", 100);
        this.maxMessageLength = this.mainPreferences.getInt("maxMessageLength", 4096);
        this.maxCaptionLength = this.mainPreferences.getInt("maxCaptionLength", 1024);
        this.mapProvider = this.mainPreferences.getInt("mapProvider", 0);
        this.availableMapProviders = this.mainPreferences.getInt("availableMapProviders", 3);
        this.mapKey = this.mainPreferences.getString("pk", (String)null);
        this.installReferer = this.mainPreferences.getString("installReferer", (String)null);
        this.defaultP2pContacts = this.mainPreferences.getBoolean("defaultP2pContacts", false);
        this.revokeTimeLimit = this.mainPreferences.getInt("revokeTimeLimit", this.revokeTimeLimit);
        this.revokeTimePmLimit = this.mainPreferences.getInt("revokeTimePmLimit", this.revokeTimePmLimit);
        this.canRevokePmInbox = this.mainPreferences.getBoolean("canRevokePmInbox", this.canRevokePmInbox);
        this.preloadFeaturedStickers = this.mainPreferences.getBoolean("preloadFeaturedStickers", false);
        this.proxyDialogId = this.mainPreferences.getLong("proxy_dialog", 0L);
        this.proxyDialogAddress = this.mainPreferences.getString("proxyDialogAddress", (String)null);
        this.nextTosCheckTime = this.notificationsPreferences.getInt("nextTosCheckTime", 0);
        this.venueSearchBot = this.mainPreferences.getString("venueSearchBot", "foursquare");
        this.gifSearchBot = this.mainPreferences.getString("gifSearchBot", "gif");
        this.imageSearchBot = this.mainPreferences.getString("imageSearchBot", "pic");
        this.blockedCountry = this.mainPreferences.getBoolean("blockedCountry", false);
        final SharedPreferences mainPreferences = this.mainPreferences;
        String s;
        if (ConnectionsManager.native_isTestBackend(this.currentAccount) != 0) {
            s = "tapv2.stel.com";
        }
        else {
            s = "apv2.stel.com";
        }
        this.dcDomainName = mainPreferences.getString("dcDomainName", s);
        final SharedPreferences mainPreferences2 = this.mainPreferences;
        if (ConnectionsManager.native_isTestBackend(this.currentAccount) != 0) {
            currentAccount = n;
        }
        else {
            currentAccount = 4;
        }
        this.webFileDatacenterId = mainPreferences2.getInt("webFileDatacenterId", currentAccount);
        this.suggestedLangCode = this.mainPreferences.getString("suggestedLangCode", "en");
    }
    
    private void addDialogToItsFolder(final int index, final TLRPC.Dialog dialog, final boolean b) {
        int folder_id;
        if (dialog instanceof TLRPC.TL_dialogFolder) {
            dialog.unread_count = 0;
            dialog.unread_mentions_count = 0;
            folder_id = 0;
        }
        else {
            folder_id = dialog.folder_id;
        }
        ArrayList<TLRPC.Dialog> list;
        if ((list = (ArrayList<TLRPC.Dialog>)this.dialogsByFolder.get(folder_id)) == null) {
            list = new ArrayList<TLRPC.Dialog>();
            this.dialogsByFolder.put(folder_id, (Object)list);
        }
        if (folder_id != 0 && dialog.unread_count != 0) {
            final TLRPC.Dialog dialog2 = (TLRPC.Dialog)this.dialogs_dict.get(DialogObject.makeFolderDialogId(folder_id));
            if (dialog2 != null) {
                if (b) {
                    if (this.isDialogMuted(dialog.id)) {
                        dialog2.unread_count += dialog.unread_count;
                    }
                    else {
                        dialog2.unread_mentions_count += dialog.unread_count;
                    }
                }
                else if (this.isDialogMuted(dialog.id)) {
                    ++dialog2.unread_count;
                }
                else {
                    ++dialog2.unread_mentions_count;
                }
            }
        }
        if (index == -1) {
            list.add(dialog);
        }
        else if (index == -2) {
            if (!list.isEmpty() && list.get(0) instanceof TLRPC.TL_dialogFolder) {
                list.add(1, dialog);
            }
            else {
                list.add(0, dialog);
            }
        }
        else {
            list.add(index, dialog);
        }
    }
    
    private void applyDialogNotificationsSettings(final long lng, final TLRPC.PeerNotifySettings notify_settings) {
        if (notify_settings == null) {
            return;
        }
        final SharedPreferences notificationsPreferences = this.notificationsPreferences;
        final StringBuilder sb = new StringBuilder();
        sb.append("notify2_");
        sb.append(lng);
        final int int1 = notificationsPreferences.getInt(sb.toString(), -1);
        final SharedPreferences notificationsPreferences2 = this.notificationsPreferences;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("notifyuntil_");
        sb2.append(lng);
        final int int2 = notificationsPreferences2.getInt(sb2.toString(), 0);
        final SharedPreferences$Editor edit = this.notificationsPreferences.edit();
        final TLRPC.Dialog dialog = (TLRPC.Dialog)this.dialogs_dict.get(lng);
        if (dialog != null) {
            dialog.notify_settings = notify_settings;
        }
        if ((notify_settings.flags & 0x2) != 0x0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("silent_");
            sb3.append(lng);
            edit.putBoolean(sb3.toString(), notify_settings.silent);
        }
        else {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("silent_");
            sb4.append(lng);
            edit.remove(sb4.toString());
        }
        final int flags = notify_settings.flags;
        int n = 1;
        final int n2 = 1;
        final int n3 = 1;
        final boolean b = true;
        if ((flags & 0x4) != 0x0) {
            if (notify_settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                int mute_until;
                if (notify_settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                    if (int1 != 2) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("notify2_");
                        sb5.append(lng);
                        edit.putInt(sb5.toString(), 2);
                        if (dialog != null) {
                            dialog.notify_settings.mute_until = Integer.MAX_VALUE;
                        }
                        mute_until = 0;
                    }
                    else {
                        mute_until = 0;
                        n = 0;
                    }
                }
                else {
                    if (int1 == 3 && int2 == notify_settings.mute_until) {
                        n = 0;
                    }
                    else {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("notify2_");
                        sb6.append(lng);
                        edit.putInt(sb6.toString(), 3);
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("notifyuntil_");
                        sb7.append(lng);
                        edit.putInt(sb7.toString(), notify_settings.mute_until);
                        n = (b ? 1 : 0);
                        if (dialog != null) {
                            dialog.notify_settings.mute_until = 0;
                            n = (b ? 1 : 0);
                        }
                    }
                    mute_until = notify_settings.mute_until;
                }
                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(lng, (long)mute_until << 32 | 0x1L);
                NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(lng);
            }
            else {
                if (int1 != 0 && int1 != 1) {
                    if (dialog != null) {
                        dialog.notify_settings.mute_until = 0;
                    }
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append("notify2_");
                    sb8.append(lng);
                    edit.putInt(sb8.toString(), 0);
                    n = n2;
                }
                else {
                    n = 0;
                }
                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(lng, 0L);
            }
        }
        else {
            if (int1 != -1) {
                if (dialog != null) {
                    dialog.notify_settings.mute_until = 0;
                }
                final StringBuilder sb9 = new StringBuilder();
                sb9.append("notify2_");
                sb9.append(lng);
                edit.remove(sb9.toString());
                n = n3;
            }
            else {
                n = 0;
            }
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(lng, 0L);
        }
        edit.commit();
        if (n != 0) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
    }
    
    private void applyDialogsNotificationsSettings(final ArrayList<TLRPC.Dialog> list) {
        SharedPreferences$Editor sharedPreferences$Editor = null;
        SharedPreferences$Editor edit;
        for (int i = 0; i < list.size(); ++i, sharedPreferences$Editor = edit) {
            final TLRPC.Dialog dialog = list.get(i);
            edit = sharedPreferences$Editor;
            if (dialog.peer != null) {
                edit = sharedPreferences$Editor;
                if (dialog.notify_settings instanceof TLRPC.TL_peerNotifySettings) {
                    if ((edit = sharedPreferences$Editor) == null) {
                        edit = this.notificationsPreferences.edit();
                    }
                    final TLRPC.Peer peer = dialog.peer;
                    int user_id = peer.user_id;
                    if (user_id == 0) {
                        final int chat_id = peer.chat_id;
                        if (chat_id != 0) {
                            user_id = -chat_id;
                        }
                        else {
                            user_id = -peer.channel_id;
                        }
                    }
                    if ((dialog.notify_settings.flags & 0x2) != 0x0) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("silent_");
                        sb.append(user_id);
                        edit.putBoolean(sb.toString(), dialog.notify_settings.silent);
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("silent_");
                        sb2.append(user_id);
                        edit.remove(sb2.toString());
                    }
                    final TLRPC.PeerNotifySettings notify_settings = dialog.notify_settings;
                    if ((notify_settings.flags & 0x4) != 0x0) {
                        if (notify_settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                            if (dialog.notify_settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("notify2_");
                                sb3.append(user_id);
                                edit.putInt(sb3.toString(), 2);
                                dialog.notify_settings.mute_until = Integer.MAX_VALUE;
                            }
                            else {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("notify2_");
                                sb4.append(user_id);
                                edit.putInt(sb4.toString(), 3);
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("notifyuntil_");
                                sb5.append(user_id);
                                edit.putInt(sb5.toString(), dialog.notify_settings.mute_until);
                            }
                        }
                        else {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("notify2_");
                            sb6.append(user_id);
                            edit.putInt(sb6.toString(), 0);
                        }
                    }
                    else {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("notify2_");
                        sb7.append(user_id);
                        edit.remove(sb7.toString());
                    }
                }
            }
        }
        if (sharedPreferences$Editor != null) {
            sharedPreferences$Editor.commit();
        }
    }
    
    private void checkChannelError(final String s, final int i) {
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0076: {
            if (hashCode != -1809401834) {
                if (hashCode != -795226617) {
                    if (hashCode == -471086771) {
                        if (s.equals("CHANNEL_PUBLIC_GROUP_NA")) {
                            n = 1;
                            break Label_0076;
                        }
                    }
                }
                else if (s.equals("CHANNEL_PRIVATE")) {
                    n = 0;
                    break Label_0076;
                }
            }
            else if (s.equals("USER_BANNED_IN_CHANNEL")) {
                n = 2;
                break Label_0076;
            }
            n = -1;
        }
        if (n != 0) {
            if (n != 1) {
                if (n == 2) {
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, i, 2);
                }
            }
            else {
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, i, 1);
            }
        }
        else {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, i, 0);
        }
    }
    
    private boolean checkDeletingTask(final boolean b) {
        final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        if (this.currentDeletingTaskMids != null) {
            if (!b) {
                final int currentDeletingTaskTime = this.currentDeletingTaskTime;
                if (currentDeletingTaskTime == 0 || currentDeletingTaskTime > currentTime) {
                    return false;
                }
            }
            this.currentDeletingTaskTime = 0;
            if (this.currentDeleteTaskRunnable != null && !b) {
                Utilities.stageQueue.cancelRunnable(this.currentDeleteTaskRunnable);
            }
            this.currentDeleteTaskRunnable = null;
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$znYc9uVm8VzO6BhZCUvwobX7s2A(this, new ArrayList((Collection<? extends E>)this.currentDeletingTaskMids)));
            return true;
        }
        return false;
    }
    
    private void checkProxyInfoInternal(final boolean b) {
        if (b && this.checkingProxyInfo) {
            this.checkingProxyInfo = false;
        }
        if ((!b && this.nextProxyInfoCheckTime > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) || this.checkingProxyInfo) {
            return;
        }
        if (this.checkingProxyInfoRequestId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkingProxyInfoRequestId, true);
            this.checkingProxyInfoRequestId = 0;
        }
        final SharedPreferences globalMainSettings = getGlobalMainSettings();
        final boolean boolean1 = globalMainSettings.getBoolean("proxy_enabled", false);
        final String string = globalMainSettings.getString("proxy_ip", "");
        final String string2 = globalMainSettings.getString("proxy_secret", "");
        int n = 0;
        Label_0178: {
            if (this.proxyDialogId != 0L) {
                final String proxyDialogAddress = this.proxyDialogAddress;
                if (proxyDialogAddress != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(string);
                    sb.append(string2);
                    if (!proxyDialogAddress.equals(sb.toString())) {
                        n = 1;
                        break Label_0178;
                    }
                }
            }
            n = 0;
        }
        if (boolean1 && !TextUtils.isEmpty((CharSequence)string) && !TextUtils.isEmpty((CharSequence)string2)) {
            this.checkingProxyInfo = true;
            this.checkingProxyInfoRequestId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_help_getProxyData(), new _$$Lambda$MessagesController$SaWsznydC5Y7TDBVTp_FLPmMJQE(this, string, string2));
        }
        else {
            n = 2;
        }
        if (n != 0) {
            this.proxyDialogId = 0L;
            this.proxyDialogAddress = null;
            getGlobalMainSettings().edit().putLong("proxy_dialog", this.proxyDialogId).remove("proxyDialogAddress").commit();
            this.nextProxyInfoCheckTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 3600;
            if (n == 2) {
                this.checkingProxyInfo = false;
                if (this.checkingProxyInfoRequestId != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkingProxyInfoRequestId, true);
                    this.checkingProxyInfoRequestId = 0;
                }
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$FvadbQtD8d2glGqiRUodW_Fj5b8(this));
        }
    }
    
    private void checkReadTasks() {
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int size = this.readTasks.size(), i = 0; i < size; ++i) {
            final ReadTask readTask = this.readTasks.get(i);
            if (readTask.sendRequestTime <= elapsedRealtime) {
                this.completeReadTask(readTask);
                this.readTasks.remove(i);
                this.readTasksMap.remove(readTask.dialogId);
                --i;
                --size;
            }
        }
    }
    
    private void checkTosUpdate() {
        if (this.nextTosCheckTime <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() && !this.checkingTosUpdate) {
            if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
                this.checkingTosUpdate = true;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_help_getTermsOfServiceUpdate(), new _$$Lambda$MessagesController$5GO5q_JFS4BEFNcWBCNsIVVD9gY(this));
            }
        }
    }
    
    private void completeReadTask(final ReadTask readTask) {
        final long dialogId = readTask.dialogId;
        final int n = (int)dialogId;
        final int i = (int)(dialogId >> 32);
        if (n != 0) {
            final TLRPC.InputPeer inputPeer = this.getInputPeer(n);
            TLRPC.TL_channels_readHistory tl_channels_readHistory2;
            if (inputPeer instanceof TLRPC.TL_inputPeerChannel) {
                final TLRPC.TL_channels_readHistory tl_channels_readHistory = new TLRPC.TL_channels_readHistory();
                tl_channels_readHistory.channel = this.getInputChannel(-n);
                tl_channels_readHistory.max_id = readTask.maxId;
                tl_channels_readHistory2 = tl_channels_readHistory;
            }
            else {
                final TLRPC.TL_messages_readHistory tl_messages_readHistory = new TLRPC.TL_messages_readHistory();
                tl_messages_readHistory.peer = inputPeer;
                tl_messages_readHistory.max_id = readTask.maxId;
                tl_channels_readHistory2 = (TLRPC.TL_channels_readHistory)tl_messages_readHistory;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_readHistory2, new _$$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco(this));
        }
        else {
            final TLRPC.EncryptedChat encryptedChat = this.getEncryptedChat(i);
            final byte[] auth_key = encryptedChat.auth_key;
            if (auth_key != null && auth_key.length > 1 && encryptedChat instanceof TLRPC.TL_encryptedChat) {
                final TLRPC.TL_messages_readEncryptedHistory tl_messages_readEncryptedHistory = new TLRPC.TL_messages_readEncryptedHistory();
                tl_messages_readEncryptedHistory.peer = new TLRPC.TL_inputEncryptedChat();
                final TLRPC.TL_inputEncryptedChat peer = tl_messages_readEncryptedHistory.peer;
                peer.chat_id = encryptedChat.id;
                peer.access_hash = encryptedChat.access_hash;
                tl_messages_readEncryptedHistory.max_date = readTask.maxDate;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_readEncryptedHistory, (RequestDelegate)_$$Lambda$MessagesController$mkj69nAVhIC1gtqhKztic36PgeU.INSTANCE);
            }
        }
    }
    
    private TLRPC.TL_dialogFolder ensureFolderDialogExists(int i, final boolean[] array) {
        if (i == 0) {
            return null;
        }
        final long folderDialogId = DialogObject.makeFolderDialogId(i);
        final TLRPC.Dialog dialog = (TLRPC.Dialog)this.dialogs_dict.get(folderDialogId);
        if (dialog instanceof TLRPC.TL_dialogFolder) {
            if (array != null) {
                array[0] = false;
            }
            return (TLRPC.TL_dialogFolder)dialog;
        }
        if (array != null) {
            array[0] = true;
        }
        final TLRPC.TL_dialogFolder tl_dialogFolder = new TLRPC.TL_dialogFolder();
        tl_dialogFolder.id = folderDialogId;
        tl_dialogFolder.peer = new TLRPC.TL_peerUser();
        tl_dialogFolder.folder = new TLRPC.TL_folder();
        final TLRPC.TL_folder folder = tl_dialogFolder.folder;
        folder.id = i;
        folder.title = LocaleController.getString("ArchivedChats", 2131558653);
        tl_dialogFolder.pinned = true;
        i = 0;
        int max = 0;
        while (i < this.allDialogs.size()) {
            final TLRPC.Dialog dialog2 = this.allDialogs.get(i);
            if (!dialog2.pinned) {
                break;
            }
            max = Math.max(dialog2.pinnedNum, max);
            ++i;
        }
        tl_dialogFolder.pinnedNum = max + 1;
        final TLRPC.TL_messages_dialogs tl_messages_dialogs = new TLRPC.TL_messages_dialogs();
        tl_messages_dialogs.dialogs.add(tl_dialogFolder);
        MessagesStorage.getInstance(this.currentAccount).putDialogs(tl_messages_dialogs, 1);
        this.dialogs_dict.put(folderDialogId, (Object)tl_dialogFolder);
        this.allDialogs.add(0, tl_dialogFolder);
        return tl_dialogFolder;
    }
    
    private void fetchFolderInLoadedPinnedDialogs(final TLRPC.TL_messages_peerDialogs tl_messages_peerDialogs) {
        final int size = tl_messages_peerDialogs.dialogs.size();
        final int n = 0;
        final int n2 = 0;
        for (int i = 0; i < size; ++i) {
            final TLRPC.Dialog dialog = tl_messages_peerDialogs.dialogs.get(i);
            if (dialog instanceof TLRPC.TL_dialogFolder) {
                final TLRPC.TL_dialogFolder o = (TLRPC.TL_dialogFolder)dialog;
                final long peerDialogId = DialogObject.getPeerDialogId(dialog.peer);
                if (o.top_message != 0 && peerDialogId != 0L) {
                    for (int size2 = tl_messages_peerDialogs.messages.size(), j = 0; j < size2; ++j) {
                        final TLRPC.Message message = tl_messages_peerDialogs.messages.get(j);
                        if (peerDialogId == MessageObject.getDialogId(message) && dialog.top_message == message.id) {
                            final TLRPC.TL_dialog e = new TLRPC.TL_dialog();
                            e.peer = dialog.peer;
                            e.top_message = dialog.top_message;
                            e.folder_id = o.folder.id;
                            e.flags |= 0x10;
                            tl_messages_peerDialogs.dialogs.add(e);
                            final TLRPC.Peer peer = dialog.peer;
                            TLRPC.InputPeer inputPeer;
                            if (peer instanceof TLRPC.TL_peerChannel) {
                                final TLRPC.TL_inputPeerChannel tl_inputPeerChannel = new TLRPC.TL_inputPeerChannel();
                                tl_inputPeerChannel.channel_id = dialog.peer.channel_id;
                                final int size3 = tl_messages_peerDialogs.chats.size();
                                int index = n2;
                                while (true) {
                                    inputPeer = tl_inputPeerChannel;
                                    if (index >= size3) {
                                        break;
                                    }
                                    final TLRPC.Chat chat = tl_messages_peerDialogs.chats.get(index);
                                    if (chat.id == tl_inputPeerChannel.channel_id) {
                                        tl_inputPeerChannel.access_hash = chat.access_hash;
                                        inputPeer = tl_inputPeerChannel;
                                        break;
                                    }
                                    ++index;
                                }
                            }
                            else if (peer instanceof TLRPC.TL_peerChat) {
                                inputPeer = new TLRPC.TL_inputPeerChat();
                                inputPeer.chat_id = dialog.peer.chat_id;
                            }
                            else {
                                final TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
                                tl_inputPeerUser.user_id = dialog.peer.user_id;
                                final int size4 = tl_messages_peerDialogs.users.size();
                                int index2 = n;
                                while (true) {
                                    inputPeer = tl_inputPeerUser;
                                    if (index2 >= size4) {
                                        break;
                                    }
                                    final TLRPC.User user = tl_messages_peerDialogs.users.get(index2);
                                    if (user.id == tl_inputPeerUser.user_id) {
                                        tl_inputPeerUser.access_hash = user.access_hash;
                                        inputPeer = tl_inputPeerUser;
                                        break;
                                    }
                                    ++index2;
                                }
                            }
                            this.loadUnknownDialog(inputPeer, 0L);
                            break;
                        }
                    }
                    break;
                }
                tl_messages_peerDialogs.dialogs.remove(o);
            }
        }
    }
    
    private void getChannelDifference(final int n) {
        this.getChannelDifference(n, 0, 0L, null);
    }
    
    public static SharedPreferences getEmojiSettings(final int n) {
        return getInstance(n).emojiPreferences;
    }
    
    public static SharedPreferences getGlobalEmojiSettings() {
        return getInstance(0).emojiPreferences;
    }
    
    public static SharedPreferences getGlobalMainSettings() {
        return getInstance(0).mainPreferences;
    }
    
    public static SharedPreferences getGlobalNotificationsSettings() {
        return getInstance(0).notificationsPreferences;
    }
    
    public static TLRPC.InputChannel getInputChannel(final TLRPC.Chat chat) {
        if (!(chat instanceof TLRPC.TL_channel) && !(chat instanceof TLRPC.TL_channelForbidden)) {
            return new TLRPC.TL_inputChannelEmpty();
        }
        final TLRPC.TL_inputChannel tl_inputChannel = new TLRPC.TL_inputChannel();
        tl_inputChannel.channel_id = chat.id;
        tl_inputChannel.access_hash = chat.access_hash;
        return tl_inputChannel;
    }
    
    public static MessagesController getInstance(final int n) {
        final MessagesController messagesController;
        if ((messagesController = MessagesController.Instance[n]) == null) {
            synchronized (MessagesController.class) {
                if (MessagesController.Instance[n] == null) {
                    MessagesController.Instance[n] = new MessagesController(n);
                }
            }
        }
        return messagesController;
    }
    
    public static SharedPreferences getMainSettings(final int n) {
        return getInstance(n).mainPreferences;
    }
    
    public static SharedPreferences getNotificationsSettings(final int n) {
        return getInstance(n).notificationsPreferences;
    }
    
    private static String getRestrictionReason(final String s) {
        if (s != null) {
            if (s.length() != 0) {
                final int index = s.indexOf(": ");
                if (index > 0) {
                    final String substring = s.substring(0, index);
                    if (substring.contains("-all") || substring.contains("-android")) {
                        return s.substring(index + 2);
                    }
                }
            }
        }
        return null;
    }
    
    private static int getUpdateChannelId(final TLRPC.Update obj) {
        if (obj instanceof TLRPC.TL_updateNewChannelMessage) {
            return ((TLRPC.TL_updateNewChannelMessage)obj).message.to_id.channel_id;
        }
        if (obj instanceof TLRPC.TL_updateEditChannelMessage) {
            return ((TLRPC.TL_updateEditChannelMessage)obj).message.to_id.channel_id;
        }
        if (obj instanceof TLRPC.TL_updateReadChannelOutbox) {
            return ((TLRPC.TL_updateReadChannelOutbox)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateChannelMessageViews) {
            return ((TLRPC.TL_updateChannelMessageViews)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateChannelTooLong) {
            return ((TLRPC.TL_updateChannelTooLong)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateChannelPinnedMessage) {
            return ((TLRPC.TL_updateChannelPinnedMessage)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateChannelReadMessagesContents) {
            return ((TLRPC.TL_updateChannelReadMessagesContents)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateChannelAvailableMessages) {
            return ((TLRPC.TL_updateChannelAvailableMessages)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateChannel) {
            return ((TLRPC.TL_updateChannel)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateChannelWebPage) {
            return ((TLRPC.TL_updateChannelWebPage)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateDeleteChannelMessages) {
            return ((TLRPC.TL_updateDeleteChannelMessages)obj).channel_id;
        }
        if (obj instanceof TLRPC.TL_updateReadChannelInbox) {
            return ((TLRPC.TL_updateReadChannelInbox)obj).channel_id;
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("trying to get unknown update channel_id for ");
            sb.append(obj);
            FileLog.e(sb.toString());
        }
        return 0;
    }
    
    private static int getUpdatePts(final TLRPC.Update update) {
        if (update instanceof TLRPC.TL_updateDeleteMessages) {
            return ((TLRPC.TL_updateDeleteMessages)update).pts;
        }
        if (update instanceof TLRPC.TL_updateNewChannelMessage) {
            return ((TLRPC.TL_updateNewChannelMessage)update).pts;
        }
        if (update instanceof TLRPC.TL_updateReadHistoryOutbox) {
            return ((TLRPC.TL_updateReadHistoryOutbox)update).pts;
        }
        if (update instanceof TLRPC.TL_updateNewMessage) {
            return ((TLRPC.TL_updateNewMessage)update).pts;
        }
        if (update instanceof TLRPC.TL_updateEditMessage) {
            return ((TLRPC.TL_updateEditMessage)update).pts;
        }
        if (update instanceof TLRPC.TL_updateWebPage) {
            return ((TLRPC.TL_updateWebPage)update).pts;
        }
        if (update instanceof TLRPC.TL_updateReadHistoryInbox) {
            return ((TLRPC.TL_updateReadHistoryInbox)update).pts;
        }
        if (update instanceof TLRPC.TL_updateChannelWebPage) {
            return ((TLRPC.TL_updateChannelWebPage)update).pts;
        }
        if (update instanceof TLRPC.TL_updateDeleteChannelMessages) {
            return ((TLRPC.TL_updateDeleteChannelMessages)update).pts;
        }
        if (update instanceof TLRPC.TL_updateEditChannelMessage) {
            return ((TLRPC.TL_updateEditChannelMessage)update).pts;
        }
        if (update instanceof TLRPC.TL_updateReadMessagesContents) {
            return ((TLRPC.TL_updateReadMessagesContents)update).pts;
        }
        if (update instanceof TLRPC.TL_updateChannelTooLong) {
            return ((TLRPC.TL_updateChannelTooLong)update).pts;
        }
        if (update instanceof TLRPC.TL_updateFolderPeers) {
            return ((TLRPC.TL_updateFolderPeers)update).pts;
        }
        return 0;
    }
    
    private static int getUpdatePtsCount(final TLRPC.Update update) {
        if (update instanceof TLRPC.TL_updateDeleteMessages) {
            return ((TLRPC.TL_updateDeleteMessages)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateNewChannelMessage) {
            return ((TLRPC.TL_updateNewChannelMessage)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateReadHistoryOutbox) {
            return ((TLRPC.TL_updateReadHistoryOutbox)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateNewMessage) {
            return ((TLRPC.TL_updateNewMessage)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateEditMessage) {
            return ((TLRPC.TL_updateEditMessage)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateWebPage) {
            return ((TLRPC.TL_updateWebPage)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateReadHistoryInbox) {
            return ((TLRPC.TL_updateReadHistoryInbox)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateChannelWebPage) {
            return ((TLRPC.TL_updateChannelWebPage)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateDeleteChannelMessages) {
            return ((TLRPC.TL_updateDeleteChannelMessages)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateEditChannelMessage) {
            return ((TLRPC.TL_updateEditChannelMessage)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateReadMessagesContents) {
            return ((TLRPC.TL_updateReadMessagesContents)update).pts_count;
        }
        if (update instanceof TLRPC.TL_updateFolderPeers) {
            return ((TLRPC.TL_updateFolderPeers)update).pts_count;
        }
        return 0;
    }
    
    private static int getUpdateQts(final TLRPC.Update update) {
        if (update instanceof TLRPC.TL_updateNewEncryptedMessage) {
            return ((TLRPC.TL_updateNewEncryptedMessage)update).qts;
        }
        return 0;
    }
    
    private int getUpdateSeq(final TLRPC.Updates updates) {
        if (updates instanceof TLRPC.TL_updatesCombined) {
            return updates.seq_start;
        }
        return updates.seq;
    }
    
    private int getUpdateType(final TLRPC.Update update) {
        if (update instanceof TLRPC.TL_updateNewMessage || update instanceof TLRPC.TL_updateReadMessagesContents || update instanceof TLRPC.TL_updateReadHistoryInbox || update instanceof TLRPC.TL_updateReadHistoryOutbox || update instanceof TLRPC.TL_updateDeleteMessages || update instanceof TLRPC.TL_updateWebPage || update instanceof TLRPC.TL_updateEditMessage || update instanceof TLRPC.TL_updateFolderPeers) {
            return 0;
        }
        if (update instanceof TLRPC.TL_updateNewEncryptedMessage) {
            return 1;
        }
        if (!(update instanceof TLRPC.TL_updateNewChannelMessage) && !(update instanceof TLRPC.TL_updateDeleteChannelMessages) && !(update instanceof TLRPC.TL_updateEditChannelMessage) && !(update instanceof TLRPC.TL_updateChannelWebPage)) {
            return 3;
        }
        return 2;
    }
    
    private String getUserNameForTyping(final TLRPC.User user) {
        if (user == null) {
            return "";
        }
        final String first_name = user.first_name;
        if (first_name != null && first_name.length() > 0) {
            return user.first_name;
        }
        final String last_name = user.last_name;
        if (last_name != null && last_name.length() > 0) {
            return user.last_name;
        }
        return "";
    }
    
    public static boolean isSupportUser(final TLRPC.User user) {
        if (user != null) {
            if (!user.support) {
                final int id = user.id;
                if (id != 777000 && id != 333000 && id != 4240000 && id != 4244000 && id != 4245000 && id != 4246000 && id != 410000 && id != 420000 && id != 431000 && id != 431415000 && id != 434000 && id != 4243000 && id != 439000 && id != 449000 && id != 450000 && id != 452000 && id != 454000 && id != 4254000 && id != 455000 && id != 460000 && id != 470000 && id != 479000 && id != 796000 && id != 482000 && id != 490000 && id != 496000 && id != 497000 && id != 498000 && id != 4298000) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private int isValidUpdate(final TLRPC.Updates updates, int updateSeq) {
        if (updateSeq == 0) {
            updateSeq = this.getUpdateSeq(updates);
            if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() + 1 == updateSeq || MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() == updateSeq) {
                return 0;
            }
            if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() < updateSeq) {
                return 1;
            }
            return 2;
        }
        else if (updateSeq == 1) {
            if (updates.pts <= MessagesStorage.getInstance(this.currentAccount).getLastPtsValue()) {
                return 2;
            }
            if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + updates.pts_count == updates.pts) {
                return 0;
            }
            return 1;
        }
        else {
            if (updateSeq != 2) {
                return 0;
            }
            if (updates.pts <= MessagesStorage.getInstance(this.currentAccount).getLastQtsValue()) {
                return 2;
            }
            if (MessagesStorage.getInstance(this.currentAccount).getLastQtsValue() + updates.updates.size() == updates.pts) {
                return 0;
            }
            return 1;
        }
    }
    
    private void loadMessagesInternal(final long lng, int sendRequest, final int n, final int offset_date, final boolean b, final int i, final int j, final int k, final int l, final boolean b2, final int m, final int i2, final int i3, final int i4, final boolean b3, final int n2, final boolean b4) {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("load messages in chat ");
            sb.append(lng);
            sb.append(" count ");
            sb.append(sendRequest);
            sb.append(" max_id ");
            sb.append(n);
            sb.append(" cache ");
            sb.append(b);
            sb.append(" mindate = ");
            sb.append(i);
            sb.append(" guid ");
            sb.append(j);
            sb.append(" load_type ");
            sb.append(k);
            sb.append(" last_message_id ");
            sb.append(l);
            sb.append(" index ");
            sb.append(m);
            sb.append(" firstUnread ");
            sb.append(i2);
            sb.append(" unread_count ");
            sb.append(i3);
            sb.append(" last_date ");
            sb.append(i4);
            sb.append(" queryFromServer ");
            sb.append(b3);
            FileLog.d(sb.toString());
        }
        final int n3 = (int)lng;
        if (!b && n3 != 0) {
            if (b4 && (k == 3 || k == 2) && l == 0) {
                final TLRPC.TL_messages_getPeerDialogs tl_messages_getPeerDialogs = new TLRPC.TL_messages_getPeerDialogs();
                final TLRPC.InputPeer inputPeer = this.getInputPeer(n3);
                final TLRPC.TL_inputDialogPeer e = new TLRPC.TL_inputDialogPeer();
                e.peer = inputPeer;
                tl_messages_getPeerDialogs.peers.add(e);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getPeerDialogs, new _$$Lambda$MessagesController$TN49v1ka_VM8bKqcQJdVLHWMje8(this, lng, sendRequest, n, offset_date, i, j, k, b2, m, i2, i4, b3));
                return;
            }
            final TLRPC.TL_messages_getHistory tl_messages_getHistory = new TLRPC.TL_messages_getHistory();
            tl_messages_getHistory.peer = this.getInputPeer(n3);
            if (k == 4) {
                tl_messages_getHistory.add_offset = -sendRequest + 5;
            }
            else if (k == 3) {
                tl_messages_getHistory.add_offset = -sendRequest / 2;
            }
            else if (k == 1) {
                tl_messages_getHistory.add_offset = -sendRequest - 1;
            }
            else if (k == 2 && n != 0) {
                tl_messages_getHistory.add_offset = -sendRequest + 6;
            }
            else if (n3 < 0 && n != 0 && ChatObject.isChannel(this.getChat(-n3))) {
                tl_messages_getHistory.add_offset = -1;
                ++tl_messages_getHistory.limit;
            }
            tl_messages_getHistory.limit = sendRequest;
            tl_messages_getHistory.offset_id = n;
            tl_messages_getHistory.offset_date = offset_date;
            sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getHistory, new _$$Lambda$MessagesController$PBgpLRl3hHYMA8ENpUjkzO9CiK0(this, lng, sendRequest, n, offset_date, j, i2, l, i3, i4, k, b2, m, b3, n2));
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(sendRequest, j);
        }
        else {
            MessagesStorage.getInstance(this.currentAccount).getMessages(lng, sendRequest, n, offset_date, i, j, k, b2, m);
        }
    }
    
    private void migrateDialogs(final int n, final int offset_date, final int user_id, final int chat_id, final int channel_id, final long access_hash) {
        if (!this.migratingDialogs) {
            if (n != -1) {
                this.migratingDialogs = true;
                final TLRPC.TL_messages_getDialogs tl_messages_getDialogs = new TLRPC.TL_messages_getDialogs();
                tl_messages_getDialogs.exclude_pinned = true;
                tl_messages_getDialogs.limit = 100;
                tl_messages_getDialogs.offset_id = n;
                tl_messages_getDialogs.offset_date = offset_date;
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("start migrate with id ");
                    sb.append(n);
                    sb.append(" date ");
                    sb.append(LocaleController.getInstance().formatterStats.format(offset_date * 1000L));
                    FileLog.d(sb.toString());
                }
                if (n == 0) {
                    tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerEmpty();
                }
                else {
                    if (channel_id != 0) {
                        tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerChannel();
                        tl_messages_getDialogs.offset_peer.channel_id = channel_id;
                    }
                    else if (user_id != 0) {
                        tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerUser();
                        tl_messages_getDialogs.offset_peer.user_id = user_id;
                    }
                    else {
                        tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerChat();
                        tl_messages_getDialogs.offset_peer.chat_id = chat_id;
                    }
                    tl_messages_getDialogs.offset_peer.access_hash = access_hash;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getDialogs, new _$$Lambda$MessagesController$tmAPAmyX_HLSBIR7TwEc_6LXn_I(this, n));
            }
        }
    }
    
    public static void openChatOrProfileWith(final TLRPC.User user, final TLRPC.Chat chat, final BaseFragment baseFragment, final int n, final boolean b) {
        if ((user == null && chat == null) || baseFragment == null) {
            return;
        }
        String s = null;
        int n2;
        boolean b2;
        if (chat != null) {
            s = getRestrictionReason(chat.restriction_reason);
            n2 = n;
            b2 = b;
        }
        else {
            n2 = n;
            b2 = b;
            if (user != null) {
                final String s2 = s = getRestrictionReason(user.restriction_reason);
                n2 = n;
                b2 = b;
                if (user.bot) {
                    n2 = 1;
                    b2 = true;
                    s = s2;
                }
            }
        }
        if (s != null) {
            showCantOpenAlert(baseFragment, s);
        }
        else {
            final Bundle bundle = new Bundle();
            if (chat != null) {
                bundle.putInt("chat_id", chat.id);
            }
            else {
                bundle.putInt("user_id", user.id);
            }
            if (n2 == 0) {
                baseFragment.presentFragment(new ProfileActivity(bundle));
            }
            else if (n2 == 2) {
                baseFragment.presentFragment(new ChatActivity(bundle), true, true);
            }
            else {
                baseFragment.presentFragment(new ChatActivity(bundle), b2);
            }
        }
    }
    
    private void processChannelsUpdatesQueue(final int i, int pts) {
        final ArrayList list = (ArrayList)this.updatesQueueChannels.get(i);
        if (list == null) {
            return;
        }
        final int value = this.channelsPts.get(i);
        if (!list.isEmpty() && value != 0) {
            Collections.sort((List<Object>)list, (Comparator<? super Object>)_$$Lambda$MessagesController$JGJclbw8cDS2wcbI_Tj2zlng_g0.INSTANCE);
            if (pts == 2) {
                this.channelsPts.put(i, list.get(0).pts);
            }
            boolean b = false;
            while (list.size() > 0) {
                final TLRPC.Updates updates = list.get(0);
                pts = updates.pts;
                if (pts <= value) {
                    pts = 2;
                }
                else if (updates.pts_count + value == pts) {
                    pts = 0;
                }
                else {
                    pts = 1;
                }
                if (pts == 0) {
                    this.processUpdates(updates, true);
                    list.remove(0);
                    b = true;
                }
                else if (pts == 1) {
                    final long value2 = this.updatesStartWaitTimeChannels.get(i);
                    if (value2 != 0L && (b || Math.abs(System.currentTimeMillis() - value2) <= 1500L)) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("HOLE IN CHANNEL ");
                            sb.append(i);
                            sb.append(" UPDATES QUEUE - will wait more time");
                            FileLog.d(sb.toString());
                        }
                        if (b) {
                            this.updatesStartWaitTimeChannels.put(i, System.currentTimeMillis());
                        }
                        return;
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("HOLE IN CHANNEL ");
                        sb2.append(i);
                        sb2.append(" UPDATES QUEUE - getChannelDifference ");
                        FileLog.d(sb2.toString());
                    }
                    this.updatesStartWaitTimeChannels.delete(i);
                    this.updatesQueueChannels.remove(i);
                    this.getChannelDifference(i);
                    return;
                }
                else {
                    list.remove(0);
                }
            }
            this.updatesQueueChannels.remove(i);
            this.updatesStartWaitTimeChannels.delete(i);
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("UPDATES CHANNEL ");
                sb3.append(i);
                sb3.append(" QUEUE PROCEED - OK");
                FileLog.d(sb3.toString());
            }
            return;
        }
        this.updatesQueueChannels.remove(i);
    }
    
    private void processUpdatesQueue(final int n, int n2) {
        Object list;
        if (n == 0) {
            list = this.updatesQueueSeq;
            Collections.sort((List<Object>)list, new _$$Lambda$MessagesController$ZJInZNgn5fe5N3COP4HchbUkZ4o(this));
        }
        else if (n == 1) {
            list = this.updatesQueuePts;
            Collections.sort((List<E>)list, (Comparator<? super E>)_$$Lambda$MessagesController$CXfuZJPIy1bgOC7pFoaz_w6qqDM.INSTANCE);
        }
        else if (n == 2) {
            list = this.updatesQueueQts;
            Collections.sort((List<E>)list, (Comparator<? super E>)_$$Lambda$MessagesController$CpLCaAXrUWrrraDtT2_grVns0kE.INSTANCE);
        }
        else {
            list = null;
        }
        if (list != null && !((ArrayList)list).isEmpty()) {
            if (n2 == 2) {
                final TLRPC.Updates updates = ((ArrayList<TLRPC.Updates>)list).get(0);
                if (n == 0) {
                    MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(this.getUpdateSeq(updates));
                }
                else if (n == 1) {
                    MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(updates.pts);
                }
                else {
                    MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(updates.pts);
                }
            }
            n2 = 0;
            while (((ArrayList)list).size() > 0) {
                final TLRPC.Updates updates2 = ((ArrayList<TLRPC.Updates>)list).get(0);
                final int validUpdate = this.isValidUpdate(updates2, n);
                if (validUpdate == 0) {
                    this.processUpdates(updates2, true);
                    ((ArrayList<Object>)list).remove(0);
                    n2 = 1;
                }
                else if (validUpdate == 1) {
                    if (this.getUpdatesStartTime(n) != 0L && (n2 != 0 || Math.abs(System.currentTimeMillis() - this.getUpdatesStartTime(n)) <= 1500L)) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("HOLE IN UPDATES QUEUE - will wait more time");
                        }
                        if (n2 != 0) {
                            this.setUpdatesStartTime(n, System.currentTimeMillis());
                        }
                        return;
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("HOLE IN UPDATES QUEUE - getDifference");
                    }
                    this.setUpdatesStartTime(n, 0L);
                    ((ArrayList)list).clear();
                    this.getDifference();
                    return;
                }
                else {
                    ((ArrayList<Object>)list).remove(0);
                }
            }
            ((ArrayList)list).clear();
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("UPDATES QUEUE PROCEED - OK");
            }
        }
        this.setUpdatesStartTime(n, 0L);
    }
    
    private void reloadDialogsReadValue(final ArrayList<TLRPC.Dialog> list, final long n) {
        if (n == 0L && (list == null || list.isEmpty())) {
            return;
        }
        final TLRPC.TL_messages_getPeerDialogs tl_messages_getPeerDialogs = new TLRPC.TL_messages_getPeerDialogs();
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                final TLRPC.InputPeer inputPeer = this.getInputPeer((int)list.get(i).id);
                if (!(inputPeer instanceof TLRPC.TL_inputPeerChannel) || inputPeer.access_hash != 0L) {
                    final TLRPC.TL_inputDialogPeer e = new TLRPC.TL_inputDialogPeer();
                    e.peer = inputPeer;
                    tl_messages_getPeerDialogs.peers.add(e);
                }
            }
        }
        else {
            final TLRPC.InputPeer inputPeer2 = this.getInputPeer((int)n);
            if (inputPeer2 instanceof TLRPC.TL_inputPeerChannel && inputPeer2.access_hash == 0L) {
                return;
            }
            final TLRPC.TL_inputDialogPeer e2 = new TLRPC.TL_inputDialogPeer();
            e2.peer = inputPeer2;
            tl_messages_getPeerDialogs.peers.add(e2);
        }
        if (tl_messages_getPeerDialogs.peers.isEmpty()) {
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getPeerDialogs, new _$$Lambda$MessagesController$YrH4Mr_BWV_6GLj5qWieGm840u8(this));
    }
    
    private void reloadMessages(final ArrayList<Integer> list, final long n) {
        if (list.isEmpty()) {
            return;
        }
        final ArrayList<Integer> c = new ArrayList<Integer>();
        final TLRPC.Chat chatByDialog = ChatObject.getChatByDialog(n, this.currentAccount);
        TLObject tlObject;
        if (ChatObject.isChannel(chatByDialog)) {
            tlObject = new TLRPC.TL_channels_getMessages();
            ((TLRPC.TL_channels_getMessages)tlObject).channel = getInputChannel(chatByDialog);
            ((TLRPC.TL_channels_getMessages)tlObject).id = c;
        }
        else {
            tlObject = new TLRPC.TL_messages_getMessages();
            ((TLRPC.TL_messages_getMessages)tlObject).id = c;
        }
        final ArrayList list2 = (ArrayList)this.reloadingMessages.get(n);
        for (int i = 0; i < list.size(); ++i) {
            final Integer n2 = list.get(i);
            if (list2 == null || !list2.contains(n2)) {
                c.add(n2);
            }
        }
        if (c.isEmpty()) {
            return;
        }
        ArrayList list3;
        if ((list3 = list2) == null) {
            list3 = new ArrayList();
            this.reloadingMessages.put(n, (Object)list3);
        }
        list3.addAll(c);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$MessagesController$rxB6ZCv_wieivWsHR6TFAEUSbcc(this, n, chatByDialog, c));
    }
    
    private void removeDialog(final TLRPC.Dialog dialog) {
        if (dialog == null) {
            return;
        }
        final long id = dialog.id;
        if (this.dialogsServerOnly.remove(dialog) && DialogObject.isChannel(dialog)) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$vWlNj3xTvw31mmNI0cEnDFi4DZ8(this, id));
        }
        this.allDialogs.remove(dialog);
        this.dialogsCanAddUsers.remove(dialog);
        this.dialogsChannelsOnly.remove(dialog);
        this.dialogsGroupsOnly.remove(dialog);
        this.dialogsUsersOnly.remove(dialog);
        this.dialogsForward.remove(dialog);
        this.dialogs_dict.remove(id);
        this.dialogs_read_inbox_max.remove(id);
        this.dialogs_read_outbox_max.remove(id);
        final ArrayList list = (ArrayList)this.dialogsByFolder.get(dialog.folder_id);
        if (list != null) {
            list.remove(dialog);
        }
    }
    
    private void removeFolder(final int i) {
        final long folderDialogId = DialogObject.makeFolderDialogId(i);
        final TLRPC.Dialog o = (TLRPC.Dialog)this.dialogs_dict.get(folderDialogId);
        if (o == null) {
            return;
        }
        this.dialogs_dict.remove(folderDialogId);
        this.allDialogs.remove(o);
        this.sortDialogs(null);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.folderBecomeEmpty, i);
    }
    
    private void resetDialogs(final boolean b, final int n, final int n2, final int n3, final int n4) {
        final Integer value = 0;
        if (b) {
            if (this.resetingDialogs) {
                return;
            }
            UserConfig.getInstance(this.currentAccount).setPinnedDialogsLoaded(1, false);
            this.resetingDialogs = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_getPinnedDialogs(), new _$$Lambda$MessagesController$gH22liOUbHjBW_HkIeQEsiEIQPo(this, n, n2, n3, n4));
            final TLRPC.TL_messages_getDialogs tl_messages_getDialogs = new TLRPC.TL_messages_getDialogs();
            tl_messages_getDialogs.limit = 100;
            tl_messages_getDialogs.exclude_pinned = true;
            tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerEmpty();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getDialogs, new _$$Lambda$MessagesController$Y6E_L9N_2KXUoxEcct5cWMRh9hc(this, n, n2, n3, n4));
        }
        else if (this.resetDialogsPinned != null) {
            final TLRPC.messages_Dialogs resetDialogsAll = this.resetDialogsAll;
            if (resetDialogsAll != null) {
                final int size = resetDialogsAll.messages.size();
                final int size2 = this.resetDialogsAll.dialogs.size();
                this.fetchFolderInLoadedPinnedDialogs(this.resetDialogsPinned);
                this.resetDialogsAll.dialogs.addAll(this.resetDialogsPinned.dialogs);
                this.resetDialogsAll.messages.addAll(this.resetDialogsPinned.messages);
                this.resetDialogsAll.users.addAll(this.resetDialogsPinned.users);
                this.resetDialogsAll.chats.addAll(this.resetDialogsPinned.chats);
                final LongSparseArray longSparseArray = new LongSparseArray();
                final LongSparseArray longSparseArray2 = new LongSparseArray();
                final SparseArray sparseArray = new SparseArray();
                final SparseArray sparseArray2 = new SparseArray();
                for (int i = 0; i < this.resetDialogsAll.users.size(); ++i) {
                    final TLRPC.User user = this.resetDialogsAll.users.get(i);
                    sparseArray.put(user.id, (Object)user);
                }
                for (int j = 0; j < this.resetDialogsAll.chats.size(); ++j) {
                    final TLRPC.Chat chat = this.resetDialogsAll.chats.get(j);
                    sparseArray2.put(chat.id, (Object)chat);
                }
                TLRPC.Message message = null;
                TLRPC.Message message3;
                for (int k = 0; k < this.resetDialogsAll.messages.size(); ++k, message = message3) {
                    final TLRPC.Message message2 = this.resetDialogsAll.messages.get(k);
                    message3 = message;
                    Label_0472: {
                        if (k < size) {
                            if (message != null) {
                                message3 = message;
                                if (message2.date >= message.date) {
                                    break Label_0472;
                                }
                            }
                            message3 = message2;
                        }
                    }
                    final TLRPC.Peer to_id = message2.to_id;
                    final int channel_id = to_id.channel_id;
                    if (channel_id != 0) {
                        final TLRPC.Chat chat2 = (TLRPC.Chat)sparseArray2.get(channel_id);
                        if (chat2 != null && chat2.left) {
                            continue;
                        }
                        if (chat2 != null && chat2.megagroup) {
                            message2.flags |= Integer.MIN_VALUE;
                        }
                    }
                    else {
                        final int chat_id = to_id.chat_id;
                        if (chat_id != 0) {
                            final TLRPC.Chat chat3 = (TLRPC.Chat)sparseArray2.get(chat_id);
                            if (chat3 != null && chat3.migrated_to != null) {
                                continue;
                            }
                        }
                    }
                    final MessageObject messageObject = new MessageObject(this.currentAccount, message2, (SparseArray<TLRPC.User>)sparseArray, (SparseArray<TLRPC.Chat>)sparseArray2, false);
                    longSparseArray2.put(messageObject.getDialogId(), (Object)messageObject);
                }
                int l = 0;
                final Integer n5 = value;
                while (l < this.resetDialogsAll.dialogs.size()) {
                    final TLRPC.Dialog dialog = this.resetDialogsAll.dialogs.get(l);
                    DialogObject.initDialog(dialog);
                    final long id = dialog.id;
                    Label_0689: {
                        if (id != 0L) {
                            if (dialog.last_message_date == 0) {
                                final MessageObject messageObject2 = (MessageObject)longSparseArray2.get(id);
                                if (messageObject2 != null) {
                                    dialog.last_message_date = messageObject2.messageOwner.date;
                                }
                            }
                            if (DialogObject.isChannel(dialog)) {
                                final TLRPC.Chat chat4 = (TLRPC.Chat)sparseArray2.get(-(int)dialog.id);
                                if (chat4 != null && chat4.left) {
                                    break Label_0689;
                                }
                                this.channelsPts.put(-(int)dialog.id, dialog.pts);
                            }
                            else {
                                final long id2 = dialog.id;
                                if ((int)id2 < 0) {
                                    final TLRPC.Chat chat5 = (TLRPC.Chat)sparseArray2.get(-(int)id2);
                                    if (chat5 != null && chat5.migrated_to != null) {
                                        break Label_0689;
                                    }
                                }
                            }
                            longSparseArray.put(dialog.id, (Object)dialog);
                            Integer n6;
                            if ((n6 = this.dialogs_read_inbox_max.get(dialog.id)) == null) {
                                n6 = n5;
                            }
                            this.dialogs_read_inbox_max.put(dialog.id, Math.max(n6, dialog.read_inbox_max_id));
                            Integer n7;
                            if ((n7 = this.dialogs_read_outbox_max.get(dialog.id)) == null) {
                                n7 = n5;
                            }
                            this.dialogs_read_outbox_max.put(dialog.id, Math.max(n7, dialog.read_outbox_max_id));
                        }
                    }
                    ++l;
                }
                ImageLoader.saveMessagesThumbs(this.resetDialogsAll.messages);
                for (int index = 0; index < this.resetDialogsAll.messages.size(); ++index) {
                    final TLRPC.Message message4 = this.resetDialogsAll.messages.get(index);
                    final TLRPC.MessageAction action = message4.action;
                    if (action instanceof TLRPC.TL_messageActionChatDeleteUser) {
                        final TLRPC.User user2 = (TLRPC.User)sparseArray.get(action.user_id);
                        if (user2 != null && user2.bot) {
                            message4.reply_markup = new TLRPC.TL_replyKeyboardHide();
                            message4.flags |= 0x40;
                        }
                    }
                    final TLRPC.MessageAction action2 = message4.action;
                    if (!(action2 instanceof TLRPC.TL_messageActionChatMigrateTo) && !(action2 instanceof TLRPC.TL_messageActionChannelCreate)) {
                        ConcurrentHashMap<Long, Integer> concurrentHashMap;
                        if (message4.out) {
                            concurrentHashMap = this.dialogs_read_outbox_max;
                        }
                        else {
                            concurrentHashMap = this.dialogs_read_inbox_max;
                        }
                        Integer value2;
                        if ((value2 = concurrentHashMap.get(message4.dialog_id)) == null) {
                            value2 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message4.out, message4.dialog_id);
                            concurrentHashMap.put(message4.dialog_id, value2);
                        }
                        message4.unread = (value2 < message4.id);
                    }
                    else {
                        message4.unread = false;
                        message4.media_unread = false;
                    }
                }
                MessagesStorage.getInstance(this.currentAccount).resetDialogs(this.resetDialogsAll, size, n, n2, n3, n4, (LongSparseArray<TLRPC.Dialog>)longSparseArray, (LongSparseArray<MessageObject>)longSparseArray2, message, size2);
                this.resetDialogsPinned = null;
                this.resetDialogsAll = null;
            }
        }
    }
    
    private void setUpdatesStartTime(final int n, final long updatesStartWaitTimeQts) {
        if (n == 0) {
            this.updatesStartWaitTimeSeq = updatesStartWaitTimeQts;
        }
        else if (n == 1) {
            this.updatesStartWaitTimePts = updatesStartWaitTimeQts;
        }
        else if (n == 2) {
            this.updatesStartWaitTimeQts = updatesStartWaitTimeQts;
        }
    }
    
    private static void showCantOpenAlert(final BaseFragment baseFragment, final String message) {
        if (baseFragment != null) {
            if (baseFragment.getParentActivity() != null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)baseFragment.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                builder.setMessage(message);
                baseFragment.showDialog(builder.create());
            }
        }
    }
    
    private void updatePrintingStrings() {
        final LongSparseArray longSparseArray = new LongSparseArray();
        final LongSparseArray longSparseArray2 = new LongSparseArray();
        for (final Map.Entry<Long, ArrayList<PrintingUser>> entry : this.printingUsers.entrySet()) {
            final long longValue = entry.getKey();
            final ArrayList<PrintingUser> list = entry.getValue();
            final int n = (int)longValue;
            if (n <= 0 && n != 0 && list.size() != 1) {
                final StringBuilder sb = new StringBuilder();
                final Iterator<PrintingUser> iterator2 = list.iterator();
                int n2 = 0;
                int n3;
                do {
                    n3 = n2;
                    if (!iterator2.hasNext()) {
                        break;
                    }
                    final TLRPC.User user = this.getUser(iterator2.next().userId);
                    n3 = n2;
                    if (user == null) {
                        continue;
                    }
                    if (sb.length() != 0) {
                        sb.append(", ");
                    }
                    sb.append(this.getUserNameForTyping(user));
                    n3 = n2 + 1;
                } while ((n2 = n3) != 2);
                if (sb.length() == 0) {
                    continue;
                }
                if (n3 == 1) {
                    longSparseArray.put(longValue, (Object)LocaleController.formatString("IsTypingGroup", 2131559700, sb.toString()));
                }
                else if (list.size() > 2) {
                    final String pluralString = LocaleController.getPluralString("AndMoreTypingGroup", list.size() - 2);
                    try {
                        longSparseArray.put(longValue, (Object)String.format(pluralString, sb.toString(), list.size() - 2));
                    }
                    catch (Exception ex) {
                        longSparseArray.put(longValue, (Object)"LOC_ERR: AndMoreTypingGroup");
                    }
                }
                else {
                    longSparseArray.put(longValue, (Object)LocaleController.formatString("AreTypingGroup", 2131558665, sb.toString()));
                }
                longSparseArray2.put(longValue, (Object)0);
            }
            else {
                final PrintingUser printingUser = list.get(0);
                final TLRPC.User user2 = this.getUser(printingUser.userId);
                if (user2 == null) {
                    continue;
                }
                final TLRPC.SendMessageAction action = printingUser.action;
                if (action instanceof TLRPC.TL_sendMessageRecordAudioAction) {
                    if (n < 0) {
                        longSparseArray.put(longValue, (Object)LocaleController.formatString("IsRecordingAudio", 2131559692, this.getUserNameForTyping(user2)));
                    }
                    else {
                        longSparseArray.put(longValue, (Object)LocaleController.getString("RecordingAudio", 2131560547));
                    }
                    longSparseArray2.put(longValue, (Object)1);
                }
                else if (!(action instanceof TLRPC.TL_sendMessageRecordRoundAction) && !(action instanceof TLRPC.TL_sendMessageUploadRoundAction)) {
                    if (action instanceof TLRPC.TL_sendMessageUploadAudioAction) {
                        if (n < 0) {
                            longSparseArray.put(longValue, (Object)LocaleController.formatString("IsSendingAudio", 2131559694, this.getUserNameForTyping(user2)));
                        }
                        else {
                            longSparseArray.put(longValue, (Object)LocaleController.getString("SendingAudio", 2131560709));
                        }
                        longSparseArray2.put(longValue, (Object)2);
                    }
                    else if (!(action instanceof TLRPC.TL_sendMessageUploadVideoAction) && !(action instanceof TLRPC.TL_sendMessageRecordVideoAction)) {
                        if (action instanceof TLRPC.TL_sendMessageUploadDocumentAction) {
                            if (n < 0) {
                                longSparseArray.put(longValue, (Object)LocaleController.formatString("IsSendingFile", 2131559695, this.getUserNameForTyping(user2)));
                            }
                            else {
                                longSparseArray.put(longValue, (Object)LocaleController.getString("SendingFile", 2131560710));
                            }
                            longSparseArray2.put(longValue, (Object)2);
                        }
                        else if (action instanceof TLRPC.TL_sendMessageUploadPhotoAction) {
                            if (n < 0) {
                                longSparseArray.put(longValue, (Object)LocaleController.formatString("IsSendingPhoto", 2131559697, this.getUserNameForTyping(user2)));
                            }
                            else {
                                longSparseArray.put(longValue, (Object)LocaleController.getString("SendingPhoto", 2131560713));
                            }
                            longSparseArray2.put(longValue, (Object)2);
                        }
                        else if (action instanceof TLRPC.TL_sendMessageGamePlayAction) {
                            if (n < 0) {
                                longSparseArray.put(longValue, (Object)LocaleController.formatString("IsSendingGame", 2131559696, this.getUserNameForTyping(user2)));
                            }
                            else {
                                longSparseArray.put(longValue, (Object)LocaleController.getString("SendingGame", 2131560711));
                            }
                            longSparseArray2.put(longValue, (Object)3);
                        }
                        else {
                            if (n < 0) {
                                longSparseArray.put(longValue, (Object)LocaleController.formatString("IsTypingGroup", 2131559700, this.getUserNameForTyping(user2)));
                            }
                            else {
                                longSparseArray.put(longValue, (Object)LocaleController.getString("Typing", 2131560926));
                            }
                            longSparseArray2.put(longValue, (Object)0);
                        }
                    }
                    else {
                        if (n < 0) {
                            longSparseArray.put(longValue, (Object)LocaleController.formatString("IsSendingVideo", 2131559698, this.getUserNameForTyping(user2)));
                        }
                        else {
                            longSparseArray.put(longValue, (Object)LocaleController.getString("SendingVideoStatus", 2131560716));
                        }
                        longSparseArray2.put(longValue, (Object)2);
                    }
                }
                else {
                    if (n < 0) {
                        longSparseArray.put(longValue, (Object)LocaleController.formatString("IsRecordingRound", 2131559693, this.getUserNameForTyping(user2)));
                    }
                    else {
                        longSparseArray.put(longValue, (Object)LocaleController.getString("RecordingRound", 2131560548));
                    }
                    longSparseArray2.put(longValue, (Object)4);
                }
            }
        }
        this.lastPrintingStringCount = longSparseArray.size();
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Gpj0Tfm4ESrlwMoiHamza4rhl1c(this, longSparseArray, longSparseArray2));
    }
    
    private boolean updatePrintingUsersWithNewMessages(final long n, final ArrayList<MessageObject> list) {
        if (n > 0L) {
            if (this.printingUsers.get(n) != null) {
                this.printingUsers.remove(n);
                return true;
            }
        }
        else if (n < 0L) {
            final ArrayList<Integer> list2 = new ArrayList<Integer>();
            for (final MessageObject messageObject : list) {
                if (!list2.contains(messageObject.messageOwner.from_id)) {
                    list2.add(messageObject.messageOwner.from_id);
                }
            }
            final ArrayList<PrintingUser> list3 = this.printingUsers.get(n);
            int n4;
            if (list3 != null) {
                int n2 = 0;
                int n3 = 0;
                while (true) {
                    n4 = n3;
                    if (n2 >= list3.size()) {
                        break;
                    }
                    int n5 = n2;
                    if (list2.contains(list3.get(n2).userId)) {
                        list3.remove(n2);
                        n5 = n2 - 1;
                        if (list3.isEmpty()) {
                            this.printingUsers.remove(n);
                        }
                        n3 = 1;
                    }
                    n2 = n5 + 1;
                }
            }
            else {
                n4 = 0;
            }
            if (n4 != 0) {
                return true;
            }
        }
        return false;
    }
    
    public void addDialogAction(final long n, final boolean b) {
        final TLRPC.Dialog o = (TLRPC.Dialog)this.dialogs_dict.get(n);
        if (o == null) {
            return;
        }
        if (b) {
            this.clearingHistoryDialogs.put(n, (Object)o);
        }
        else {
            this.deletingDialogs.put(n, (Object)o);
            this.allDialogs.remove(o);
            this.sortDialogs(null);
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
    }
    
    public int addDialogToFolder(final long l, final int n, final int n2, final long n3) {
        final ArrayList<Long> list = new ArrayList<Long>(1);
        list.add(l);
        return this.addDialogToFolder(list, n, n2, null, n3);
    }
    
    public int addDialogToFolder(ArrayList<Long> nativeByteBuffer, int n, int n2, ArrayList<TLRPC.TL_inputFolderPeer> folder_peers, long n3) {
        final TLRPC.TL_folders_editPeerFolders tl_folders_editPeerFolders = new TLRPC.TL_folders_editPeerFolders();
        if (n3 == 0L) {
            final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            final int size = ((ArrayList)nativeByteBuffer).size();
            boolean b = false;
            folder_peers = null;
            int i = 0;
            int n4 = 0;
            while (i < size) {
                n3 = ((ArrayList<Long>)nativeByteBuffer).get(i);
                Label_0141: {
                    if (DialogObject.isPeerDialogId(n3) || DialogObject.isSecretDialogId(n3)) {
                        if (n == 1) {
                            if (n3 == clientUserId || n3 == 777000L) {
                                break Label_0141;
                            }
                            if (this.isProxyDialog(n3, false)) {
                                break Label_0141;
                            }
                        }
                        final TLRPC.Dialog dialog = (TLRPC.Dialog)this.dialogs_dict.get(n3);
                        if (dialog != null) {
                            dialog.folder_id = n;
                            if (n2 > 0) {
                                dialog.pinned = true;
                                dialog.pinnedNum = n2;
                            }
                            else {
                                dialog.pinned = false;
                                dialog.pinnedNum = 0;
                            }
                            Object o = folder_peers;
                            if (folder_peers == null) {
                                o = new boolean[] { false };
                                this.ensureFolderDialogExists(n, (boolean[])o);
                            }
                            if (DialogObject.isSecretDialogId(n3)) {
                                MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId(null, null, n3, n);
                            }
                            else {
                                final TLRPC.TL_inputFolderPeer e = new TLRPC.TL_inputFolderPeer();
                                e.folder_id = n;
                                e.peer = this.getInputPeer((int)n3);
                                tl_folders_editPeerFolders.folder_peers.add(e);
                                n4 += e.getObjectSize();
                            }
                            b = true;
                            folder_peers = o;
                        }
                    }
                }
                ++i;
            }
            if (!b) {
                return 0;
            }
            this.sortDialogs(null);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            if (n4 != 0) {
                NativeByteBuffer nativeByteBuffer2 = null;
                Label_0420: {
                    try {
                        nativeByteBuffer = new NativeByteBuffer(n4 + 12);
                        try {
                            nativeByteBuffer.writeInt32(17);
                            nativeByteBuffer.writeInt32(n);
                            nativeByteBuffer.writeInt32(tl_folders_editPeerFolders.folder_peers.size());
                            final int size2 = tl_folders_editPeerFolders.folder_peers.size();
                            n2 = 0;
                            while (true) {
                                nativeByteBuffer2 = nativeByteBuffer;
                                if (n2 >= size2) {
                                    break Label_0420;
                                }
                                tl_folders_editPeerFolders.folder_peers.get(n2).serializeToStream(nativeByteBuffer);
                                ++n2;
                            }
                        }
                        catch (Exception ex) {}
                    }
                    catch (Exception ex) {
                        nativeByteBuffer = null;
                    }
                    final Exception ex;
                    FileLog.e(ex);
                    nativeByteBuffer2 = nativeByteBuffer;
                }
                n3 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer2);
            }
            else {
                n3 = 0L;
            }
        }
        else {
            tl_folders_editPeerFolders.folder_peers = (ArrayList<TLRPC.TL_inputFolderPeer>)folder_peers;
            folder_peers = null;
        }
        n2 = 1;
        if (!tl_folders_editPeerFolders.folder_peers.isEmpty()) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_folders_editPeerFolders, new _$$Lambda$MessagesController$6NDlWW4LaPRt1E9GG1k86kJK_WU(this, n3));
            MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId(null, tl_folders_editPeerFolders.folder_peers, 0L, n);
        }
        if (folder_peers == null) {
            n = 0;
        }
        else {
            n = n2;
            if (folder_peers[0]) {
                n = 2;
            }
        }
        return n;
    }
    
    public void addSupportUser() {
        final TLRPC.TL_userForeign_old2 tl_userForeign_old2 = new TLRPC.TL_userForeign_old2();
        tl_userForeign_old2.phone = "333";
        tl_userForeign_old2.id = 333000;
        tl_userForeign_old2.first_name = "Telegram";
        tl_userForeign_old2.last_name = "";
        tl_userForeign_old2.status = null;
        tl_userForeign_old2.photo = new TLRPC.TL_userProfilePhotoEmpty();
        this.putUser(tl_userForeign_old2, true);
        final TLRPC.TL_userForeign_old2 tl_userForeign_old3 = new TLRPC.TL_userForeign_old2();
        tl_userForeign_old3.phone = "42777";
        tl_userForeign_old3.id = 777000;
        tl_userForeign_old3.verified = true;
        tl_userForeign_old3.first_name = "Telegram";
        tl_userForeign_old3.last_name = "Notifications";
        tl_userForeign_old3.status = null;
        tl_userForeign_old3.photo = new TLRPC.TL_userProfilePhotoEmpty();
        this.putUser(tl_userForeign_old3, true);
    }
    
    public void addToPollsQueue(final long n, final ArrayList<MessageObject> list) {
        SparseArray sparseArray;
        if ((sparseArray = (SparseArray)this.pollsToCheck.get(n)) == null) {
            sparseArray = new SparseArray();
            this.pollsToCheck.put(n, (Object)sparseArray);
            ++this.pollsToCheckSize;
        }
        final int size = sparseArray.size();
        final int n2 = 0;
        for (int i = 0; i < size; ++i) {
            ((MessageObject)sparseArray.valueAt(i)).pollVisibleOnScreen = false;
        }
        for (int size2 = list.size(), j = n2; j < size2; ++j) {
            final MessageObject messageObject = list.get(j);
            if (messageObject.type == 17) {
                final int id = messageObject.getId();
                final MessageObject messageObject2 = (MessageObject)sparseArray.get(id);
                if (messageObject2 != null) {
                    messageObject2.pollVisibleOnScreen = true;
                }
                else {
                    sparseArray.put(id, (Object)messageObject);
                }
            }
        }
    }
    
    public void addToViewsQueue(final MessageObject messageObject) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$cWol_41rOBrStFY3FKcjDkmBodE(this, messageObject));
    }
    
    public void addUserToChat(final int n, final TLRPC.User user, final TLRPC.ChatFull chatFull, int i, final String start_param, final BaseFragment baseFragment, final Runnable runnable) {
        if (user == null) {
            return;
        }
        if (n > 0) {
            final boolean channel = ChatObject.isChannel(n, this.currentAccount);
            final boolean b = channel && this.getChat(n).megagroup;
            final TLRPC.InputUser inputUser = this.getInputUser(user);
            TLObject tlObject;
            if (start_param != null && (!channel || b)) {
                tlObject = new TLRPC.TL_messages_startBot();
                ((TLRPC.TL_messages_startBot)tlObject).bot = inputUser;
                if (channel) {
                    ((TLRPC.TL_messages_startBot)tlObject).peer = this.getInputPeer(-n);
                }
                else {
                    ((TLRPC.TL_messages_startBot)tlObject).peer = new TLRPC.TL_inputPeerChat();
                    ((TLRPC.TL_messages_startBot)tlObject).peer.chat_id = n;
                }
                ((TLRPC.TL_messages_startBot)tlObject).start_param = start_param;
                ((TLRPC.TL_messages_startBot)tlObject).random_id = Utilities.random.nextLong();
            }
            else if (channel) {
                if (inputUser instanceof TLRPC.TL_inputUserSelf) {
                    if (this.joiningToChannels.contains(n)) {
                        return;
                    }
                    tlObject = new TLRPC.TL_channels_joinChannel();
                    ((TLRPC.TL_channels_joinChannel)tlObject).channel = this.getInputChannel(n);
                    this.joiningToChannels.add(n);
                }
                else {
                    tlObject = new TLRPC.TL_channels_inviteToChannel();
                    ((TLRPC.TL_channels_inviteToChannel)tlObject).channel = this.getInputChannel(n);
                    ((TLRPC.TL_channels_inviteToChannel)tlObject).users.add(inputUser);
                }
            }
            else {
                tlObject = new TLRPC.TL_messages_addChatUser();
                ((TLRPC.TL_messages_addChatUser)tlObject).chat_id = n;
                ((TLRPC.TL_messages_addChatUser)tlObject).fwd_limit = i;
                ((TLRPC.TL_messages_addChatUser)tlObject).user_id = inputUser;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4(this, channel, inputUser, n, baseFragment, tlObject, b, runnable));
        }
        else if (chatFull instanceof TLRPC.TL_chatFull) {
            for (i = 0; i < chatFull.participants.participants.size(); ++i) {
                if (chatFull.participants.participants.get(i).user_id == user.id) {
                    return;
                }
            }
            final TLRPC.Chat chat = this.getChat(n);
            ++chat.participants_count;
            final ArrayList<TLRPC.Chat> list = new ArrayList<TLRPC.Chat>();
            list.add(chat);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, list, true, true);
            final TLRPC.TL_chatParticipant element = new TLRPC.TL_chatParticipant();
            element.user_id = user.id;
            element.inviter_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            element.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            chatFull.participants.participants.add(0, element);
            MessagesStorage.getInstance(this.currentAccount).updateChatInfo(chatFull, true);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, chatFull, 0, false, null);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 32);
        }
    }
    
    public void addUsersToChannel(final int n, final ArrayList<TLRPC.InputUser> users, final BaseFragment baseFragment) {
        if (users != null) {
            if (!users.isEmpty()) {
                final TLRPC.TL_channels_inviteToChannel tl_channels_inviteToChannel = new TLRPC.TL_channels_inviteToChannel();
                tl_channels_inviteToChannel.channel = this.getInputChannel(n);
                tl_channels_inviteToChannel.users = users;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_inviteToChannel, new _$$Lambda$MessagesController$TfiNuUXCjyH_kDhosYCjp7Cg5Gk(this, baseFragment, tl_channels_inviteToChannel));
            }
        }
    }
    
    public void blockUser(final int i) {
        final TLRPC.User user = this.getUser(i);
        if (user != null) {
            if (this.blockedUsers.indexOfKey(i) < 0) {
                this.blockedUsers.put(i, 1);
                if (user.bot) {
                    DataQuery.getInstance(this.currentAccount).removeInline(i);
                }
                else {
                    DataQuery.getInstance(this.currentAccount).removePeer(i);
                }
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad, new Object[0]);
                final TLRPC.TL_contacts_block tl_contacts_block = new TLRPC.TL_contacts_block();
                tl_contacts_block.id = this.getInputUser(user);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_block, new _$$Lambda$MessagesController$UR0KC8eSIVhCkdT_PUu4ASoxIZg(this, user));
            }
        }
    }
    
    public void cancelLoadFullChat(final int i) {
        this.loadingFullChats.remove((Object)i);
    }
    
    public void cancelLoadFullUser(final int i) {
        this.loadingFullUsers.remove((Object)i);
    }
    
    public void cancelTyping(final int n, final long n2) {
        final LongSparseArray longSparseArray = (LongSparseArray)this.sendingTypings.get(n);
        if (longSparseArray != null) {
            longSparseArray.remove(n2);
        }
    }
    
    public void changeChatAvatar(final int chat_id, final TLRPC.InputFile inputFile, final TLRPC.FileLocation fileLocation, final TLRPC.FileLocation fileLocation2) {
        TLObject tlObject;
        if (ChatObject.isChannel(chat_id, this.currentAccount)) {
            final TLRPC.TL_channels_editPhoto tl_channels_editPhoto = new TLRPC.TL_channels_editPhoto();
            tl_channels_editPhoto.channel = this.getInputChannel(chat_id);
            if (inputFile != null) {
                tl_channels_editPhoto.photo = new TLRPC.TL_inputChatUploadedPhoto();
                tl_channels_editPhoto.photo.file = inputFile;
                tlObject = tl_channels_editPhoto;
            }
            else {
                tl_channels_editPhoto.photo = new TLRPC.TL_inputChatPhotoEmpty();
                tlObject = tl_channels_editPhoto;
            }
        }
        else {
            final TLRPC.TL_messages_editChatPhoto tl_messages_editChatPhoto = new TLRPC.TL_messages_editChatPhoto();
            tl_messages_editChatPhoto.chat_id = chat_id;
            if (inputFile != null) {
                tl_messages_editChatPhoto.photo = new TLRPC.TL_inputChatUploadedPhoto();
                tl_messages_editChatPhoto.photo.file = inputFile;
                tlObject = tl_messages_editChatPhoto;
            }
            else {
                tl_messages_editChatPhoto.photo = new TLRPC.TL_inputChatPhotoEmpty();
                tlObject = tl_messages_editChatPhoto;
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$MessagesController$xhFZ44TZl_2_TmtDUCclv1zCK1g(this, fileLocation, fileLocation2), 64);
    }
    
    public void changeChatTitle(final int n, final String title) {
        if (n > 0) {
            TLRPC.TL_channels_editTitle tl_channels_editTitle2;
            if (ChatObject.isChannel(n, this.currentAccount)) {
                final TLRPC.TL_channels_editTitle tl_channels_editTitle = new TLRPC.TL_channels_editTitle();
                tl_channels_editTitle.channel = this.getInputChannel(n);
                tl_channels_editTitle.title = title;
                tl_channels_editTitle2 = tl_channels_editTitle;
            }
            else {
                final TLRPC.TL_messages_editChatTitle tl_messages_editChatTitle = new TLRPC.TL_messages_editChatTitle();
                tl_messages_editChatTitle.chat_id = n;
                tl_messages_editChatTitle.title = title;
                tl_channels_editTitle2 = (TLRPC.TL_channels_editTitle)tl_messages_editChatTitle;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_editTitle2, new _$$Lambda$MessagesController$40Sp7FeVOZVQXMLSFubFHF48OI8(this), 64);
        }
        else {
            final TLRPC.Chat chat = this.getChat(n);
            chat.title = title;
            final ArrayList<TLRPC.Chat> list = new ArrayList<TLRPC.Chat>();
            list.add(chat);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, list, true, true);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 16);
        }
    }
    
    public boolean checkCanOpenChat(final Bundle bundle, final BaseFragment baseFragment) {
        return this.checkCanOpenChat(bundle, baseFragment, null);
    }
    
    public boolean checkCanOpenChat(final Bundle bundle, final BaseFragment baseFragment, final MessageObject messageObject) {
        if (bundle != null) {
            if (baseFragment != null) {
                final int int1 = bundle.getInt("user_id", 0);
                final int int2 = bundle.getInt("chat_id", 0);
                final int int3 = bundle.getInt("message_id", 0);
                String s = null;
                TLObject user;
                TLRPC.Chat chat;
                if (int1 != 0) {
                    user = this.getUser(int1);
                    chat = null;
                }
                else if (int2 != 0) {
                    chat = this.getChat(int2);
                    user = null;
                }
                else {
                    user = (chat = null);
                }
                if (user == null && chat == null) {
                    return true;
                }
                if (chat != null) {
                    s = getRestrictionReason(chat.restriction_reason);
                }
                else if (user != null) {
                    s = getRestrictionReason(((TLRPC.User)user).restriction_reason);
                }
                if (s != null) {
                    showCantOpenAlert(baseFragment, s);
                    return false;
                }
                if (int3 != 0 && messageObject != null && chat != null && chat.access_hash == 0L) {
                    final int n = (int)messageObject.getDialogId();
                    if (n != 0) {
                        final AlertDialog visibleDialog = new AlertDialog((Context)baseFragment.getParentActivity(), 3);
                        if (n < 0) {
                            chat = this.getChat(-n);
                        }
                        TLObject tlObject;
                        if (n <= 0 && ChatObject.isChannel(chat)) {
                            final TLRPC.Chat chat2 = this.getChat(-n);
                            final TLRPC.TL_channels_getMessages tl_channels_getMessages = new TLRPC.TL_channels_getMessages();
                            tl_channels_getMessages.channel = getInputChannel(chat2);
                            tl_channels_getMessages.id.add(messageObject.getId());
                            tlObject = tl_channels_getMessages;
                        }
                        else {
                            final TLRPC.TL_messages_getMessages tl_messages_getMessages = new TLRPC.TL_messages_getMessages();
                            tl_messages_getMessages.id.add(messageObject.getId());
                            tlObject = tl_messages_getMessages;
                        }
                        visibleDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$MessagesController$Uy5HFVAD0pdKo9Ipvs8fE566HbU(this, ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$MessagesController$6GNHdOHS4ayBESoK8wFIXPmSlYs(this, visibleDialog, baseFragment, bundle)), baseFragment));
                        baseFragment.setVisibleDialog(visibleDialog);
                        visibleDialog.show();
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void checkChannelInviter(final int n) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$1DIYKuDA2QelETVd6XOA_sCNt2w(this, n));
    }
    
    public void checkIfFolderEmpty(final int n) {
        if (n == 0) {
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).checkIfFolderEmpty(n);
    }
    
    protected void checkLastDialogMessage(final TLRPC.Dialog dialog, final TLRPC.InputPeer inputPeer, final long n) {
        final int n2 = (int)dialog.id;
        if (n2 != 0) {
            if (this.checkingLastMessagesDialogs.indexOfKey(n2) < 0) {
                final TLRPC.TL_messages_getHistory tl_messages_getHistory = new TLRPC.TL_messages_getHistory();
                TLRPC.InputPeer inputPeer2;
                if (inputPeer == null) {
                    inputPeer2 = this.getInputPeer(n2);
                }
                else {
                    inputPeer2 = inputPeer;
                }
                tl_messages_getHistory.peer = inputPeer2;
                if (tl_messages_getHistory.peer == null) {
                    return;
                }
                tl_messages_getHistory.limit = 1;
                this.checkingLastMessagesDialogs.put(n2, true);
                long pendingTask = n;
                if (n == 0L) {
                    NativeByteBuffer nativeByteBuffer2;
                    Exception ex = null;
                    try {
                        final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tl_messages_getHistory.peer.getObjectSize() + 60);
                        try {
                            nativeByteBuffer.writeInt32(14);
                            nativeByteBuffer.writeInt64(dialog.id);
                            nativeByteBuffer.writeInt32(dialog.top_message);
                            nativeByteBuffer.writeInt32(dialog.read_inbox_max_id);
                            nativeByteBuffer.writeInt32(dialog.read_outbox_max_id);
                            nativeByteBuffer.writeInt32(dialog.unread_count);
                            nativeByteBuffer.writeInt32(dialog.last_message_date);
                            nativeByteBuffer.writeInt32(dialog.pts);
                            nativeByteBuffer.writeInt32(dialog.flags);
                            nativeByteBuffer.writeBool(dialog.pinned);
                            nativeByteBuffer.writeInt32(dialog.pinnedNum);
                            nativeByteBuffer.writeInt32(dialog.unread_mentions_count);
                            nativeByteBuffer.writeBool(dialog.unread_mark);
                            nativeByteBuffer.writeInt32(dialog.folder_id);
                            inputPeer.serializeToStream(nativeByteBuffer);
                            nativeByteBuffer2 = nativeByteBuffer;
                        }
                        catch (Exception ex) {
                            nativeByteBuffer2 = nativeByteBuffer;
                        }
                    }
                    catch (Exception ex2) {
                        nativeByteBuffer2 = null;
                        ex = ex2;
                    }
                    FileLog.e(ex);
                    pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer2);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getHistory, new _$$Lambda$MessagesController$LzTCLk4PcU0AIVN_3fmrcJA4k9I(this, n2, dialog, pendingTask));
            }
        }
    }
    
    public void checkProxyInfo(final boolean b) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$UmwP8okyEt8khTz852DsKDuGJ70(this, b));
    }
    
    public void cleanup() {
        ContactsController.getInstance(this.currentAccount).cleanup();
        MediaController.getInstance().cleanup();
        NotificationsController.getInstance(this.currentAccount).cleanup();
        SendMessagesHelper.getInstance(this.currentAccount).cleanup();
        SecretChatHelper.getInstance(this.currentAccount).cleanup();
        LocationController.getInstance(this.currentAccount).cleanup();
        DataQuery.getInstance(this.currentAccount).cleanup();
        DialogsActivity.dialogsLoaded[this.currentAccount] = false;
        this.notificationsPreferences.edit().clear().commit();
        this.emojiPreferences.edit().putLong("lastGifLoadTime", 0L).putLong("lastStickersLoadTime", 0L).putLong("lastStickersLoadTimeMask", 0L).putLong("lastStickersLoadTimeFavs", 0L).commit();
        this.mainPreferences.edit().remove("archivehint").remove("archivehint_l").remove("gifhint").remove("soundHint").remove("dcDomainName").remove("webFileDatacenterId").commit();
        this.reloadingWebpages.clear();
        this.reloadingWebpagesPending.clear();
        this.dialogs_dict.clear();
        this.dialogs_read_inbox_max.clear();
        this.loadingPinnedDialogs.clear();
        this.dialogs_read_outbox_max.clear();
        this.exportedChats.clear();
        this.fullUsers.clear();
        this.fullChats.clear();
        this.dialogsByFolder.clear();
        this.unreadUnmutedDialogs = 0;
        this.joiningToChannels.clear();
        this.migratedChats.clear();
        this.channelViewsToSend.clear();
        this.pollsToCheck.clear();
        this.pollsToCheckSize = 0;
        this.dialogsServerOnly.clear();
        this.dialogsForward.clear();
        this.allDialogs.clear();
        this.dialogsCanAddUsers.clear();
        this.dialogsChannelsOnly.clear();
        this.dialogsGroupsOnly.clear();
        this.dialogsUsersOnly.clear();
        this.dialogMessagesByIds.clear();
        this.dialogMessagesByRandomIds.clear();
        this.channelAdmins.clear();
        this.loadingChannelAdmins.clear();
        this.users.clear();
        this.objectsByUsernames.clear();
        this.chats.clear();
        this.dialogMessage.clear();
        this.deletedHistory.clear();
        this.printingUsers.clear();
        this.printingStrings.clear();
        this.printingStringsTypes.clear();
        this.onlinePrivacy.clear();
        this.loadingPeerSettings.clear();
        this.deletingDialogs.clear();
        this.clearingHistoryDialogs.clear();
        this.lastPrintingStringCount = 0;
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$cyTTwmVBJXbVI4kVZKxLSy4FE3s(this));
        this.createdDialogMainThreadIds.clear();
        this.visibleDialogMainThreadIds.clear();
        this.blockedUsers.clear();
        this.sendingTypings.clear();
        this.loadingFullUsers.clear();
        this.loadedFullUsers.clear();
        this.reloadingMessages.clear();
        this.loadingFullChats.clear();
        this.loadingFullParticipants.clear();
        this.loadedFullParticipants.clear();
        this.loadedFullChats.clear();
        this.dialogsLoaded = false;
        this.nextDialogsCacheOffset.clear();
        this.loadingDialogs.clear();
        this.dialogsEndReached.clear();
        this.serverDialogsEndReached.clear();
        this.checkingTosUpdate = false;
        this.nextTosCheckTime = 0;
        this.nextProxyInfoCheckTime = 0;
        this.checkingProxyInfo = false;
        this.loadingUnreadDialogs = false;
        this.currentDeletingTaskTime = 0;
        this.currentDeletingTaskMids = null;
        this.currentDeletingTaskChannelId = 0;
        this.gettingNewDeleteTask = false;
        this.loadingBlockedUsers = false;
        this.firstGettingTask = false;
        this.updatingState = false;
        this.resetingDialogs = false;
        this.lastStatusUpdateTime = 0L;
        this.offlineSent = false;
        this.registeringForPush = false;
        this.getDifferenceFirstSync = true;
        this.uploadingAvatar = null;
        this.uploadingWallpaper = null;
        this.statusRequest = 0;
        this.statusSettingState = 0;
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$6czMCwAPM6zONbsB9qHk4xGgZKk(this));
        if (this.currentDeleteTaskRunnable != null) {
            Utilities.stageQueue.cancelRunnable(this.currentDeleteTaskRunnable);
            this.currentDeleteTaskRunnable = null;
        }
        this.addSupportUser();
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
    }
    
    protected void clearFullUsers() {
        this.loadedFullUsers.clear();
        this.loadedFullChats.clear();
    }
    
    protected void completeDialogsReset(final TLRPC.messages_Dialogs messages_Dialogs, final int n, final int n2, final int n3, final int n4, final int n5, final LongSparseArray<TLRPC.Dialog> longSparseArray, final LongSparseArray<MessageObject> longSparseArray2, final TLRPC.Message message) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$yD6NgtKkjxhJizLM4eoCQwHgn60(this, n3, n4, n5, messages_Dialogs, longSparseArray, longSparseArray2));
    }
    
    public void convertToMegaGroup(final Context context, final int chat_id, final MessagesStorage.IntCallback intCallback) {
        final TLRPC.TL_messages_migrateChat tl_messages_migrateChat = new TLRPC.TL_messages_migrateChat();
        tl_messages_migrateChat.chat_id = chat_id;
        final AlertDialog alertDialog = new AlertDialog(context, 3);
        alertDialog.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$MessagesController$SIGUP1wZNKHgQo2aKPuAgpDjucg(this, ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_migrateChat, new _$$Lambda$MessagesController$EkRPBwXKrGufP9yBw39xD6g_xjo(this, context, alertDialog, intCallback))));
        try {
            alertDialog.show();
        }
        catch (Exception ex) {}
    }
    
    public int createChat(final String title, final ArrayList<Integer> list, String about, int i, final BaseFragment baseFragment) {
        final int n = 0;
        if (i == 1) {
            final TLRPC.TL_chat e = new TLRPC.TL_chat();
            e.id = UserConfig.getInstance(this.currentAccount).lastBroadcastId;
            e.title = title;
            e.photo = new TLRPC.TL_chatPhotoEmpty();
            e.participants_count = list.size();
            e.date = (int)(System.currentTimeMillis() / 1000L);
            e.version = 1;
            final UserConfig instance = UserConfig.getInstance(this.currentAccount);
            --instance.lastBroadcastId;
            this.putChat(e, false);
            final ArrayList<TLRPC.Chat> list2 = new ArrayList<TLRPC.Chat>();
            list2.add(e);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, list2, true, true);
            final TLRPC.TL_chatFull tl_chatFull = new TLRPC.TL_chatFull();
            tl_chatFull.id = e.id;
            tl_chatFull.chat_photo = new TLRPC.TL_photoEmpty();
            tl_chatFull.notify_settings = new TLRPC.TL_peerNotifySettingsEmpty_layer77();
            tl_chatFull.exported_invite = new TLRPC.TL_chatInviteEmpty();
            tl_chatFull.participants = new TLRPC.TL_chatParticipants();
            final TLRPC.ChatParticipants participants = tl_chatFull.participants;
            participants.chat_id = e.id;
            participants.admin_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            tl_chatFull.participants.version = 1;
            TLRPC.TL_chatParticipant e2;
            for (i = 0; i < list.size(); ++i) {
                e2 = new TLRPC.TL_chatParticipant();
                e2.user_id = list.get(i);
                e2.inviter_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                e2.date = (int)(System.currentTimeMillis() / 1000L);
                tl_chatFull.participants.participants.add(e2);
            }
            MessagesStorage.getInstance(this.currentAccount).updateChatInfo(tl_chatFull, false);
            final TLRPC.TL_messageService e3 = new TLRPC.TL_messageService();
            e3.action = new TLRPC.TL_messageActionCreatedBroadcastList();
            i = UserConfig.getInstance(this.currentAccount).getNewMessageId();
            e3.id = i;
            e3.local_id = i;
            e3.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            e3.dialog_id = AndroidUtilities.makeBroadcastId(e.id);
            e3.to_id = new TLRPC.TL_peerChat();
            e3.to_id.chat_id = e.id;
            e3.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            e3.random_id = 0L;
            e3.flags |= 0x100;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            final MessageObject e4 = new MessageObject(this.currentAccount, e3, this.users, true);
            e4.messageOwner.send_state = 0;
            final ArrayList<MessageObject> list3 = new ArrayList<MessageObject>();
            list3.add(e4);
            final ArrayList<TLRPC.Message> list4 = new ArrayList<TLRPC.Message>();
            list4.add(e3);
            MessagesStorage.getInstance(this.currentAccount).putMessages(list4, false, true, false, 0);
            this.updateInterfaceWithMessages(e3.dialog_id, list3);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidCreated, e.id);
            return 0;
        }
        if (i == 0) {
            final TLRPC.TL_messages_createChat tl_messages_createChat = new TLRPC.TL_messages_createChat();
            tl_messages_createChat.title = title;
            TLRPC.User user;
            for (i = n; i < list.size(); ++i) {
                user = this.getUser(list.get(i));
                if (user != null) {
                    tl_messages_createChat.users.add(this.getInputUser(user));
                }
            }
            return ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_createChat, new _$$Lambda$MessagesController$BwCRJLOHKlBHZCobPU1cBtbarBc(this, baseFragment, tl_messages_createChat), 2);
        }
        if (i != 2 && i != 4) {
            return 0;
        }
        final TLRPC.TL_channels_createChannel tl_channels_createChannel = new TLRPC.TL_channels_createChannel();
        tl_channels_createChannel.title = title;
        if (about == null) {
            about = "";
        }
        tl_channels_createChannel.about = about;
        if (i == 4) {
            tl_channels_createChannel.megagroup = true;
        }
        else {
            tl_channels_createChannel.broadcast = true;
        }
        return ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_createChannel, new _$$Lambda$MessagesController$0bxY0b0iZuUAKIyUkrQ28a0391w(this, baseFragment, tl_channels_createChannel), 2);
    }
    
    public void deleteDialog(final long n, final int n2) {
        this.deleteDialog(n, n2, false);
    }
    
    public void deleteDialog(final long n, final int n2, final boolean b) {
        this.deleteDialog(n, true, n2, 0, b, null, 0L);
    }
    
    protected void deleteDialog(final long l, final boolean b, final int n, int n2, final boolean revoke, final TLRPC.InputPeer inputPeer, long pendingTask) {
        if (n == 2) {
            MessagesStorage.getInstance(this.currentAccount).deleteDialog(l, n);
            return;
        }
        if (n == 0 || n == 3) {
            DataQuery.getInstance(this.currentAccount).uninstallShortcut(l);
        }
        final int n3 = (int)l;
        final int i = (int)(l >> 32);
        int max;
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).deleteDialog(l, n);
            final TLRPC.Dialog dialog = (TLRPC.Dialog)this.dialogs_dict.get(l);
            if (n == 0 || n == 3) {
                NotificationsController.getInstance(this.currentAccount).deleteNotificationChannel(l);
            }
            int n4;
            if (dialog != null) {
                if (n2 == 0) {
                    max = Math.max(Math.max(Math.max(0, dialog.top_message), dialog.read_inbox_max_id), dialog.read_outbox_max_id);
                }
                else {
                    max = n2;
                }
                TLRPC.Dialog dialog2;
                if (n != 0 && n != 3) {
                    dialog.unread_count = 0;
                    n4 = 0;
                    dialog2 = dialog;
                }
                else {
                    final TLRPC.Dialog proxyDialog = this.proxyDialog;
                    if (proxyDialog != null && proxyDialog.id == l) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                    if (n2 != 0) {
                        this.isLeftProxyChannel = true;
                        final long id = this.proxyDialog.id;
                        if (id < 0L) {
                            final TLRPC.Chat chat = this.getChat(-(int)id);
                            if (chat != null) {
                                chat.left = true;
                            }
                        }
                        this.sortDialogs(null);
                        dialog2 = dialog;
                        n4 = n2;
                    }
                    else {
                        this.removeDialog(dialog);
                        final int value = this.nextDialogsCacheOffset.get(dialog.folder_id, 0);
                        n4 = n2;
                        dialog2 = dialog;
                        if (value > 0) {
                            this.nextDialogsCacheOffset.put(dialog.folder_id, value - 1);
                            dialog2 = dialog;
                            n4 = n2;
                        }
                    }
                }
                n2 = n3;
                if (n4 == 0) {
                    MessageObject messageObject = (MessageObject)this.dialogMessage.get(dialog2.id);
                    this.dialogMessage.remove(dialog2.id);
                    int n5;
                    if (messageObject != null) {
                        n5 = messageObject.getId();
                        this.dialogMessagesByIds.remove(messageObject.getId());
                    }
                    else {
                        n5 = dialog2.top_message;
                        messageObject = (MessageObject)this.dialogMessagesByIds.get(n5);
                        this.dialogMessagesByIds.remove(dialog2.top_message);
                    }
                    if (messageObject != null) {
                        final long random_id = messageObject.messageOwner.random_id;
                        if (random_id != 0L) {
                            this.dialogMessagesByRandomIds.remove(random_id);
                        }
                    }
                    if (n == 1 && n2 != 0 && n5 > 0) {
                        final TLRPC.TL_messageService e = new TLRPC.TL_messageService();
                        e.id = dialog2.top_message;
                        e.out = (UserConfig.getInstance(this.currentAccount).getClientUserId() == l);
                        e.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                        e.flags |= 0x100;
                        e.action = new TLRPC.TL_messageActionHistoryClear();
                        e.date = dialog2.last_message_date;
                        e.dialog_id = n2;
                        if (n2 > 0) {
                            e.to_id = new TLRPC.TL_peerUser();
                            e.to_id.user_id = n2;
                        }
                        else {
                            final int chat_id = -n2;
                            if (ChatObject.isChannel(this.getChat(chat_id))) {
                                e.to_id = new TLRPC.TL_peerChannel();
                                e.to_id.channel_id = chat_id;
                            }
                            else {
                                e.to_id = new TLRPC.TL_peerChat();
                                e.to_id.chat_id = chat_id;
                            }
                        }
                        final MessageObject e2 = new MessageObject(this.currentAccount, e, this.createdDialogIds.contains(e.dialog_id));
                        final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
                        list.add(e2);
                        final ArrayList<TLRPC.Message> list2 = new ArrayList<TLRPC.Message>();
                        list2.add(e);
                        this.updateInterfaceWithMessages(l, list);
                        MessagesStorage.getInstance(this.currentAccount).putMessages(list2, false, true, false, 0);
                    }
                    else {
                        dialog2.top_message = 0;
                    }
                }
            }
            else {
                max = n2;
                n4 = 0;
                n2 = n3;
            }
            if (!this.dialogsInTransaction) {
                if (n4 != 0) {
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
                }
                else {
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, l, false);
                }
            }
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$fi2heANmHg4iJXLk9Zkj_J0ni7w(this, l));
        }
        else {
            max = n2;
            n2 = n3;
        }
        if (i != 1) {
            if (n != 3) {
                if (n2 != 0) {
                    TLRPC.InputPeer inputPeer2;
                    if (inputPeer == null) {
                        inputPeer2 = this.getInputPeer(n2);
                    }
                    else {
                        inputPeer2 = inputPeer;
                    }
                    if (inputPeer2 == null) {
                        return;
                    }
                    final boolean b2 = inputPeer2 instanceof TLRPC.TL_inputPeerChannel;
                    n2 = Integer.MAX_VALUE;
                    if (!b2 || n != 0) {
                        if (max > 0 && max != Integer.MAX_VALUE) {
                            this.deletedHistory.put(l, (Object)max);
                        }
                        if (pendingTask == 0L) {
                            NativeByteBuffer nativeByteBuffer;
                            try {
                                nativeByteBuffer = new NativeByteBuffer(inputPeer2.getObjectSize() + 28);
                                try {
                                    nativeByteBuffer.writeInt32(13);
                                    nativeByteBuffer.writeInt64(l);
                                    nativeByteBuffer.writeBool(b);
                                    nativeByteBuffer.writeInt32(n);
                                    nativeByteBuffer.writeInt32(max);
                                    try {
                                        nativeByteBuffer.writeBool(revoke);
                                        inputPeer2.serializeToStream(nativeByteBuffer);
                                    }
                                    catch (Exception ex) {}
                                }
                                catch (Exception ex) {}
                            }
                            catch (Exception ex) {
                                nativeByteBuffer = null;
                            }
                            final Exception ex;
                            FileLog.e(ex);
                            pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer);
                        }
                    }
                    if (b2) {
                        if (n == 0) {
                            if (pendingTask != 0L) {
                                MessagesStorage.getInstance(this.currentAccount).removePendingTask(pendingTask);
                            }
                            return;
                        }
                        final TLRPC.TL_channels_deleteHistory tl_channels_deleteHistory = new TLRPC.TL_channels_deleteHistory();
                        tl_channels_deleteHistory.channel = new TLRPC.TL_inputChannel();
                        final TLRPC.InputChannel channel = tl_channels_deleteHistory.channel;
                        channel.channel_id = inputPeer2.channel_id;
                        channel.access_hash = inputPeer2.access_hash;
                        if (max <= 0) {
                            max = Integer.MAX_VALUE;
                        }
                        tl_channels_deleteHistory.max_id = max;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_deleteHistory, new _$$Lambda$MessagesController$yR0Wl__tkImGpY86P4U53sAMzc8(this, pendingTask, l), 64);
                    }
                    else {
                        final TLRPC.TL_messages_deleteHistory tl_messages_deleteHistory = new TLRPC.TL_messages_deleteHistory();
                        tl_messages_deleteHistory.peer = inputPeer2;
                        if (n != 0) {
                            n2 = max;
                        }
                        tl_messages_deleteHistory.max_id = n2;
                        tl_messages_deleteHistory.just_clear = (n != 0);
                        tl_messages_deleteHistory.revoke = revoke;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_deleteHistory, new _$$Lambda$MessagesController$GgsMvhuFaAXbZ2H0PLOXM_0uOo4(this, pendingTask, l, n, max, revoke, inputPeer2), 64);
                    }
                }
                else if (n == 1) {
                    SecretChatHelper.getInstance(this.currentAccount).sendClearHistoryMessage(this.getEncryptedChat(i), null);
                }
                else {
                    SecretChatHelper.getInstance(this.currentAccount).declineSecretChat(i);
                }
            }
        }
    }
    
    public void deleteMessages(final ArrayList<Integer> list, final ArrayList<Long> list2, final TLRPC.EncryptedChat encryptedChat, final int n, final boolean b) {
        this.deleteMessages(list, list2, encryptedChat, n, b, 0L, null);
    }
    
    public void deleteMessages(ArrayList<Integer> nativeByteBuffer, final ArrayList<Long> list, TLRPC.EncryptedChat encryptedChat, final int i, final boolean revoke, long n, final TLObject tlObject) {
        if ((nativeByteBuffer == null || ((ArrayList)nativeByteBuffer).isEmpty()) && tlObject == null) {
            return;
        }
        ArrayList<Integer> list3;
        if (n == 0L) {
            if (i == 0) {
                for (int j = 0; j < ((ArrayList)nativeByteBuffer).size(); ++j) {
                    final MessageObject messageObject = (MessageObject)this.dialogMessagesByIds.get((int)((ArrayList<Integer>)nativeByteBuffer).get(j));
                    if (messageObject != null) {
                        messageObject.deleted = true;
                    }
                }
            }
            else {
                this.markChannelDialogMessageAsDeleted((ArrayList<Integer>)nativeByteBuffer, i);
            }
            final ArrayList<Integer> list2 = new ArrayList<Integer>();
            for (int k = 0; k < ((ArrayList)nativeByteBuffer).size(); ++k) {
                final Integer e = ((ArrayList<Integer>)nativeByteBuffer).get(k);
                if (e > 0) {
                    list2.add(e);
                }
            }
            MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeleted((ArrayList<Integer>)nativeByteBuffer, true, i);
            MessagesStorage.getInstance(this.currentAccount).updateDialogsWithDeletedMessages((ArrayList<Integer>)nativeByteBuffer, null, true, i);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, nativeByteBuffer, i);
            list3 = list2;
        }
        else {
            list3 = null;
        }
        if (i != 0) {
            TLObject tlObject2;
            if (tlObject != null) {
                tlObject2 = tlObject;
            }
            else {
                encryptedChat = (TLRPC.EncryptedChat)new TLRPC.TL_channels_deleteMessages();
                ((TLRPC.TL_channels_deleteMessages)encryptedChat).id = list3;
                ((TLRPC.TL_channels_deleteMessages)encryptedChat).channel = this.getInputChannel(i);
                try {
                    nativeByteBuffer = new NativeByteBuffer(encryptedChat.getObjectSize() + 8);
                    try {
                        nativeByteBuffer.writeInt32(7);
                        nativeByteBuffer.writeInt32(i);
                        ((TLRPC.TL_channels_deleteMessages)encryptedChat).serializeToStream(nativeByteBuffer);
                    }
                    catch (Exception ex) {}
                }
                catch (Exception ex) {
                    nativeByteBuffer = null;
                }
                final Exception ex;
                FileLog.e(ex);
                n = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer);
                tlObject2 = encryptedChat;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject2, new _$$Lambda$MessagesController$9B6lLHcMl9ABhpIDa9aeiv_K8oA(this, i, n));
        }
        else {
            if (list != null && encryptedChat != null && !list.isEmpty()) {
                SecretChatHelper.getInstance(this.currentAccount).sendMessagesDeleteMessage(encryptedChat, list, null);
            }
            TLObject tlObject3;
            if (tlObject != null) {
                tlObject3 = tlObject;
            }
            else {
                encryptedChat = (TLRPC.EncryptedChat)new TLRPC.TL_messages_deleteMessages();
                ((TLRPC.TL_messages_deleteMessages)encryptedChat).id = list3;
                ((TLRPC.TL_messages_deleteMessages)encryptedChat).revoke = revoke;
                try {
                    nativeByteBuffer = new NativeByteBuffer(encryptedChat.getObjectSize() + 8);
                    try {
                        nativeByteBuffer.writeInt32(7);
                        nativeByteBuffer.writeInt32(i);
                        ((TLRPC.TL_messages_deleteMessages)encryptedChat).serializeToStream(nativeByteBuffer);
                    }
                    catch (Exception ex2) {}
                }
                catch (Exception ex2) {
                    nativeByteBuffer = null;
                }
                final Exception ex2;
                FileLog.e(ex2);
                n = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer);
                tlObject3 = encryptedChat;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject3, new _$$Lambda$MessagesController$py2_IIRHD2uCa7xVMrMM8SVKTaA(this, n));
        }
    }
    
    public void deleteUserChannelHistory(final TLRPC.Chat chat, final TLRPC.User user, final int n) {
        if (n == 0) {
            MessagesStorage.getInstance(this.currentAccount).deleteUserChannelHistory(chat.id, user.id);
        }
        final TLRPC.TL_channels_deleteUserHistory tl_channels_deleteUserHistory = new TLRPC.TL_channels_deleteUserHistory();
        tl_channels_deleteUserHistory.channel = getInputChannel(chat);
        tl_channels_deleteUserHistory.user_id = this.getInputUser(user);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_deleteUserHistory, new _$$Lambda$MessagesController$_lPnTSKn3lxlWS4m8gq50UHxeAw(this, chat, user));
    }
    
    public void deleteUserFromChat(final int n, final TLRPC.User user, final TLRPC.ChatFull chatFull) {
        this.deleteUserFromChat(n, user, chatFull, false, false);
    }
    
    public void deleteUserFromChat(int i, final TLRPC.User user, final TLRPC.ChatFull chatFull, final boolean b, final boolean b2) {
        if (user == null) {
            return;
        }
        if (i > 0) {
            final TLRPC.InputUser inputUser = this.getInputUser(user);
            final TLRPC.Chat chat = this.getChat(i);
            final boolean channel = ChatObject.isChannel(chat);
            TLObject tlObject;
            if (channel) {
                if (inputUser instanceof TLRPC.TL_inputUserSelf) {
                    if (chat.creator && b) {
                        tlObject = new TLRPC.TL_channels_deleteChannel();
                        ((TLRPC.TL_channels_deleteChannel)tlObject).channel = getInputChannel(chat);
                    }
                    else {
                        tlObject = new TLRPC.TL_channels_leaveChannel();
                        ((TLRPC.TL_channels_leaveChannel)tlObject).channel = getInputChannel(chat);
                    }
                }
                else {
                    tlObject = new TLRPC.TL_channels_editBanned();
                    ((TLRPC.TL_channels_editBanned)tlObject).channel = getInputChannel(chat);
                    ((TLRPC.TL_channels_editBanned)tlObject).user_id = inputUser;
                    ((TLRPC.TL_channels_editBanned)tlObject).banned_rights = new TLRPC.TL_chatBannedRights();
                    final TLRPC.TL_chatBannedRights banned_rights = ((TLRPC.TL_channels_editBanned)tlObject).banned_rights;
                    banned_rights.view_messages = true;
                    banned_rights.send_media = true;
                    banned_rights.send_messages = true;
                    banned_rights.send_stickers = true;
                    banned_rights.send_gifs = true;
                    banned_rights.send_games = true;
                    banned_rights.send_inline = true;
                    banned_rights.embed_links = true;
                    banned_rights.pin_messages = true;
                    banned_rights.send_polls = true;
                    banned_rights.invite_users = true;
                    banned_rights.change_info = true;
                }
            }
            else {
                tlObject = new TLRPC.TL_messages_deleteChatUser();
                ((TLRPC.TL_messages_deleteChatUser)tlObject).chat_id = i;
                ((TLRPC.TL_messages_deleteChatUser)tlObject).user_id = this.getInputUser(user);
            }
            if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.deleteDialog(-i, 0, b2);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$MessagesController$IE4i2TewkP8XTc8wTYcnuzt_GP0(this, channel, inputUser, i), 64);
        }
        else if (chatFull instanceof TLRPC.TL_chatFull) {
            final TLRPC.Chat chat2 = this.getChat(i);
            --chat2.participants_count;
            final ArrayList<TLRPC.Chat> list = new ArrayList<TLRPC.Chat>();
            list.add(chat2);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, list, true, true);
            while (true) {
                for (i = 0; i < chatFull.participants.participants.size(); ++i) {
                    if (chatFull.participants.participants.get(i).user_id == user.id) {
                        chatFull.participants.participants.remove(i);
                        i = 1;
                        if (i != 0) {
                            MessagesStorage.getInstance(this.currentAccount).updateChatInfo(chatFull, true);
                            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, chatFull, 0, false, null);
                        }
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 32);
                        return;
                    }
                }
                i = 0;
                continue;
            }
        }
    }
    
    public void deleteUserPhoto(final TLRPC.InputPhoto e) {
        if (e == null) {
            final TLRPC.TL_photos_updateProfilePhoto tl_photos_updateProfilePhoto = new TLRPC.TL_photos_updateProfilePhoto();
            tl_photos_updateProfilePhoto.id = new TLRPC.TL_inputPhotoEmpty();
            UserConfig.getInstance(this.currentAccount).getCurrentUser().photo = new TLRPC.TL_userProfilePhotoEmpty();
            TLRPC.User user;
            if ((user = this.getUser(UserConfig.getInstance(this.currentAccount).getClientUserId())) == null) {
                user = UserConfig.getInstance(this.currentAccount).getCurrentUser();
            }
            if (user == null) {
                return;
            }
            user.photo = UserConfig.getInstance(this.currentAccount).getCurrentUser().photo;
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1535);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_photos_updateProfilePhoto, new _$$Lambda$MessagesController$3uRiNEdYGV34lhCOa9clmeKBj1M(this));
        }
        else {
            final TLRPC.TL_photos_deletePhotos tl_photos_deletePhotos = new TLRPC.TL_photos_deletePhotos();
            tl_photos_deletePhotos.id.add(e);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_photos_deletePhotos, (RequestDelegate)_$$Lambda$MessagesController$lnX7i9eGTDuJovXhCLpz1QuqmLE.INSTANCE);
        }
    }
    
    public void didAddedNewTask(final int n, final SparseArray<ArrayList<Long>> sparseArray) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$LoBRzobgnJFkXmFzseR6vqsooQs(this, n));
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$bHd9VuzZp__o4GfCjtn5be2_y4I(this, sparseArray));
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.FileDidUpload) {
            final String s = (String)array[0];
            final TLRPC.InputFile inputFile = (TLRPC.InputFile)array[1];
            final String uploadingAvatar = this.uploadingAvatar;
            if (uploadingAvatar != null && uploadingAvatar.equals(s)) {
                final TLRPC.TL_photos_uploadProfilePhoto tl_photos_uploadProfilePhoto = new TLRPC.TL_photos_uploadProfilePhoto();
                tl_photos_uploadProfilePhoto.file = inputFile;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_photos_uploadProfilePhoto, new _$$Lambda$MessagesController$avFJzHcfb0E8c4wljuXOjwoiTOA(this));
            }
            else {
                final String uploadingWallpaper = this.uploadingWallpaper;
                if (uploadingWallpaper != null && uploadingWallpaper.equals(s)) {
                    final TLRPC.TL_account_uploadWallPaper tl_account_uploadWallPaper = new TLRPC.TL_account_uploadWallPaper();
                    tl_account_uploadWallPaper.file = inputFile;
                    tl_account_uploadWallPaper.mime_type = "image/jpeg";
                    final TLRPC.TL_wallPaperSettings settings = new TLRPC.TL_wallPaperSettings();
                    settings.blur = this.uploadingWallpaperBlurred;
                    settings.motion = this.uploadingWallpaperMotion;
                    tl_account_uploadWallPaper.settings = settings;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_uploadWallPaper, new _$$Lambda$MessagesController$G2pKRuboIkVmyKgcxHs4KK8Bkn4(this, settings));
                }
            }
        }
        else if (n == NotificationCenter.FileDidFailUpload) {
            final String s2 = (String)array[0];
            final String uploadingAvatar2 = this.uploadingAvatar;
            if (uploadingAvatar2 != null && uploadingAvatar2.equals(s2)) {
                this.uploadingAvatar = null;
            }
            else {
                final String uploadingWallpaper2 = this.uploadingWallpaper;
                if (uploadingWallpaper2 != null && uploadingWallpaper2.equals(s2)) {
                    this.uploadingWallpaper = null;
                }
            }
        }
        else if (n == NotificationCenter.messageReceivedByServer) {
            final Integer n3 = (Integer)array[0];
            final Integer n4 = (Integer)array[1];
            final Long n5 = (Long)array[3];
            final MessageObject messageObject = (MessageObject)this.dialogMessage.get((long)n5);
            if (messageObject != null && (messageObject.getId() == n3 || messageObject.messageOwner.local_id == n3)) {
                messageObject.messageOwner.id = n4;
                messageObject.messageOwner.send_state = 0;
            }
            final TLRPC.Dialog dialog = (TLRPC.Dialog)this.dialogs_dict.get((long)n5);
            if (dialog != null && dialog.top_message == n3) {
                dialog.top_message = n4;
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            final MessageObject messageObject2 = (MessageObject)this.dialogMessagesByIds.get((int)n3);
            this.dialogMessagesByIds.remove((int)n3);
            if (messageObject2 != null) {
                this.dialogMessagesByIds.put((int)n4, (Object)messageObject2);
            }
        }
        else if (n == NotificationCenter.updateMessageMedia) {
            final TLRPC.Message message = (TLRPC.Message)array[0];
            final MessageObject messageObject3 = (MessageObject)this.dialogMessagesByIds.get(message.id);
            if (messageObject3 != null) {
                messageObject3.messageOwner.media = message.media;
                final TLRPC.MessageMedia media = message.media;
                if (media.ttl_seconds != 0 && (media.photo instanceof TLRPC.TL_photoEmpty || media.document instanceof TLRPC.TL_documentEmpty)) {
                    messageObject3.setType();
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
                }
            }
        }
    }
    
    public void forceResetDialogs() {
        this.resetDialogs(true, MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
        NotificationsController.getInstance(this.currentAccount).deleteAllNotificationChannels();
    }
    
    public void generateJoinMessage(final int n, final boolean b) {
        final TLRPC.Chat chat = this.getChat(n);
        if (chat != null && ChatObject.isChannel(n, this.currentAccount)) {
            if ((!chat.left && !chat.kicked) || b) {
                final TLRPC.TL_messageService e = new TLRPC.TL_messageService();
                e.flags = 256;
                final int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                e.id = newMessageId;
                e.local_id = newMessageId;
                e.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                e.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                e.to_id = new TLRPC.TL_peerChannel();
                e.to_id.channel_id = n;
                e.dialog_id = -n;
                e.post = true;
                e.action = new TLRPC.TL_messageActionChatAddUser();
                e.action.users.add(UserConfig.getInstance(this.currentAccount).getClientUserId());
                if (chat.megagroup) {
                    e.flags |= Integer.MIN_VALUE;
                }
                UserConfig.getInstance(this.currentAccount).saveConfig(false);
                final ArrayList<MessageObject> list = new ArrayList<MessageObject>();
                final ArrayList<TLRPC.Message> list2 = new ArrayList<TLRPC.Message>();
                list2.add(e);
                list.add(new MessageObject(this.currentAccount, e, true));
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$goN3mCs2oD94EPYohjkaDpROs2E(this, list));
                MessagesStorage.getInstance(this.currentAccount).putMessages(list2, true, true, false, 0);
                AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$OUR20mf5_d9gsY1zs0m8SWbYyH8(this, n, list));
            }
        }
    }
    
    public void generateUpdateMessage() {
        if (!BuildVars.DEBUG_VERSION) {
            final String lastUpdateVersion = SharedConfig.lastUpdateVersion;
            if (lastUpdateVersion != null) {
                if (!lastUpdateVersion.equals(BuildVars.BUILD_VERSION_STRING)) {
                    final TLRPC.TL_help_getAppChangelog tl_help_getAppChangelog = new TLRPC.TL_help_getAppChangelog();
                    tl_help_getAppChangelog.prev_app_version = SharedConfig.lastUpdateVersion;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_help_getAppChangelog, new _$$Lambda$MessagesController$IayzOkIFtocnVFRbLM_8qmLr_wI(this));
                }
            }
        }
    }
    
    public ArrayList<TLRPC.Dialog> getAllDialogs() {
        return this.allDialogs;
    }
    
    public void getBlockedUsers(final boolean b) {
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            if (!this.loadingBlockedUsers) {
                this.loadingBlockedUsers = true;
                if (b) {
                    MessagesStorage.getInstance(this.currentAccount).getBlockedUsers();
                }
                else {
                    final TLRPC.TL_contacts_getBlocked tl_contacts_getBlocked = new TLRPC.TL_contacts_getBlocked();
                    tl_contacts_getBlocked.offset = 0;
                    tl_contacts_getBlocked.limit = 200;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_getBlocked, new _$$Lambda$MessagesController$R5tZYjWVTciy1Jz1GsIoNOmiXf8(this));
                }
            }
        }
    }
    
    protected void getChannelDifference(final int n, final int n2, final long n3, TLRPC.InputChannel inputChannel) {
        if (this.gettingDifferenceChannels.get(n)) {
            return;
        }
        int limit = 100;
        boolean force = true;
        int n4;
        if (n2 == 1) {
            if (this.channelsPts.get(n) != 0) {
                return;
            }
            limit = 1;
            n4 = 1;
        }
        else {
            int value = 0;
            Label_0116: {
                if ((value = this.channelsPts.get(n)) == 0) {
                    final int channelPtsSync = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(n);
                    if (channelPtsSync != 0) {
                        this.channelsPts.put(n, channelPtsSync);
                    }
                    if ((value = channelPtsSync) == 0) {
                        if (n2 != 2) {
                            value = channelPtsSync;
                            if (n2 != 3) {
                                break Label_0116;
                            }
                        }
                        return;
                    }
                }
            }
            if ((n4 = value) == 0) {
                return;
            }
        }
        TLRPC.InputChannel inputChannel2;
        if (inputChannel == null) {
            TLRPC.Chat chat;
            if ((chat = this.getChat(n)) == null) {
                final TLRPC.Chat chatSync = MessagesStorage.getInstance(this.currentAccount).getChatSync(n);
                if ((chat = chatSync) != null) {
                    this.putChat(chatSync, true);
                    chat = chatSync;
                }
            }
            inputChannel2 = getInputChannel(chat);
        }
        else {
            inputChannel2 = inputChannel;
        }
        if (inputChannel2 != null && inputChannel2.access_hash != 0L) {
            long pendingTask = n3;
            if (n3 == 0L) {
                try {
                    inputChannel = (TLRPC.InputChannel)new NativeByteBuffer(inputChannel2.getObjectSize() + 12);
                    try {
                        ((NativeByteBuffer)inputChannel).writeInt32(6);
                        ((NativeByteBuffer)inputChannel).writeInt32(n);
                        ((NativeByteBuffer)inputChannel).writeInt32(n2);
                        inputChannel2.serializeToStream((AbstractSerializedData)inputChannel);
                    }
                    catch (Exception ex) {}
                }
                catch (Exception ex) {
                    inputChannel = null;
                }
                final Exception ex;
                FileLog.e(ex);
                pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask((NativeByteBuffer)inputChannel);
            }
            this.gettingDifferenceChannels.put(n, true);
            final TLRPC.TL_updates_getChannelDifference tl_updates_getChannelDifference = new TLRPC.TL_updates_getChannelDifference();
            tl_updates_getChannelDifference.channel = inputChannel2;
            tl_updates_getChannelDifference.filter = new TLRPC.TL_channelMessagesFilterEmpty();
            tl_updates_getChannelDifference.pts = n4;
            tl_updates_getChannelDifference.limit = limit;
            if (n2 == 3) {
                force = false;
            }
            tl_updates_getChannelDifference.force = force;
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("start getChannelDifference with pts = ");
                sb.append(n4);
                sb.append(" channelId = ");
                sb.append(n);
                FileLog.d(sb.toString());
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_updates_getChannelDifference, new _$$Lambda$MessagesController$x3OtyFEBR3d2oz5IdcBDlqWOR_4(this, n, n2, pendingTask));
            return;
        }
        if (n3 != 0L) {
            MessagesStorage.getInstance(this.currentAccount).removePendingTask(n3);
        }
    }
    
    public TLRPC.Chat getChat(final Integer key) {
        return this.chats.get(key);
    }
    
    public TLRPC.ChatFull getChatFull(final int n) {
        return (TLRPC.ChatFull)this.fullChats.get(n);
    }
    
    public ArrayList<TLRPC.Dialog> getDialogs(final int n) {
        ArrayList<TLRPC.Dialog> list;
        if ((list = (ArrayList<TLRPC.Dialog>)this.dialogsByFolder.get(n)) == null) {
            list = new ArrayList<TLRPC.Dialog>();
        }
        return list;
    }
    
    public void getDifference() {
        this.getDifference(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue(), false);
    }
    
    public void getDifference(final int n, final int n2, final int n3, final boolean b) {
        this.registerForPush(SharedConfig.pushString);
        if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() == 0) {
            this.loadCurrentState();
            return;
        }
        if (!b && this.gettingDifference) {
            return;
        }
        this.gettingDifference = true;
        final TLRPC.TL_updates_getDifference tl_updates_getDifference = new TLRPC.TL_updates_getDifference();
        tl_updates_getDifference.pts = n;
        tl_updates_getDifference.date = n2;
        tl_updates_getDifference.qts = n3;
        if (this.getDifferenceFirstSync) {
            tl_updates_getDifference.flags |= 0x1;
            if (ApplicationLoader.isConnectedOrConnectingToWiFi()) {
                tl_updates_getDifference.pts_total_limit = 5000;
            }
            else {
                tl_updates_getDifference.pts_total_limit = 1000;
            }
            this.getDifferenceFirstSync = false;
        }
        if (tl_updates_getDifference.date == 0) {
            tl_updates_getDifference.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("start getDifference with date = ");
            sb.append(n2);
            sb.append(" pts = ");
            sb.append(n);
            sb.append(" qts = ");
            sb.append(n3);
            FileLog.d(sb.toString());
        }
        ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(true);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_updates_getDifference, new _$$Lambda$MessagesController$TiSv3h40ZHze2ul5Mr_eNiZvHcc(this, n2, n3));
    }
    
    public TLRPC.EncryptedChat getEncryptedChat(final Integer key) {
        return this.encryptedChats.get(key);
    }
    
    public TLRPC.EncryptedChat getEncryptedChatDB(final int i, final boolean b) {
        final TLRPC.EncryptedChat encryptedChat = this.encryptedChats.get(i);
        if (encryptedChat != null) {
            TLRPC.EncryptedChat encryptedChat2 = encryptedChat;
            if (!b) {
                return encryptedChat2;
            }
            if (!(encryptedChat instanceof TLRPC.TL_encryptedChatWaiting)) {
                encryptedChat2 = encryptedChat;
                if (!(encryptedChat instanceof TLRPC.TL_encryptedChatRequested)) {
                    return encryptedChat2;
                }
            }
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final ArrayList<TLRPC.EncryptedChat> list = new ArrayList<TLRPC.EncryptedChat>();
        MessagesStorage.getInstance(this.currentAccount).getEncryptedChat(i, countDownLatch, (ArrayList<TLObject>)list);
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        TLRPC.EncryptedChat encryptedChat2 = encryptedChat;
        if (list.size() == 2) {
            encryptedChat2 = list.get(0);
            final TLRPC.User user = (TLRPC.User)list.get(1);
            this.putEncryptedChat(encryptedChat2, false);
            this.putUser(user, true);
        }
        return encryptedChat2;
    }
    
    public TLRPC.ExportedChatInvite getExportedInvite(final int n) {
        return (TLRPC.ExportedChatInvite)this.exportedChats.get(n);
    }
    
    public TLRPC.InputChannel getInputChannel(final int i) {
        return getInputChannel(this.getChat(i));
    }
    
    public TLRPC.InputPeer getInputPeer(int user_id) {
        TLRPC.InputPeer inputPeer;
        if (user_id < 0) {
            user_id = -user_id;
            final TLRPC.Chat chat = this.getChat(user_id);
            if (ChatObject.isChannel(chat)) {
                inputPeer = new TLRPC.TL_inputPeerChannel();
                inputPeer.channel_id = user_id;
                inputPeer.access_hash = chat.access_hash;
            }
            else {
                inputPeer = new TLRPC.TL_inputPeerChat();
                inputPeer.chat_id = user_id;
            }
        }
        else {
            final TLRPC.User user = this.getUser(user_id);
            final TLRPC.TL_inputPeerUser tl_inputPeerUser = new TLRPC.TL_inputPeerUser();
            tl_inputPeerUser.user_id = user_id;
            inputPeer = tl_inputPeerUser;
            if (user != null) {
                tl_inputPeerUser.access_hash = user.access_hash;
                inputPeer = tl_inputPeerUser;
            }
        }
        return inputPeer;
    }
    
    public TLRPC.InputUser getInputUser(final int i) {
        return this.getInputUser(getInstance(UserConfig.selectedAccount).getUser(i));
    }
    
    public TLRPC.InputUser getInputUser(final TLRPC.User user) {
        if (user == null) {
            return new TLRPC.TL_inputUserEmpty();
        }
        TLRPC.TL_inputUserSelf tl_inputUserSelf;
        if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            tl_inputUserSelf = new TLRPC.TL_inputUserSelf();
        }
        else {
            final TLRPC.TL_inputUser tl_inputUser = new TLRPC.TL_inputUser();
            tl_inputUser.user_id = user.id;
            tl_inputUser.access_hash = user.access_hash;
            tl_inputUserSelf = (TLRPC.TL_inputUserSelf)tl_inputUser;
        }
        return tl_inputUserSelf;
    }
    
    public void getNewDeleteTask(final ArrayList<Integer> list, final int n) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$uJw9E53v9xDGKUuX24rMZpaODgA(this, list, n));
    }
    
    public TLRPC.Peer getPeer(int user_id) {
        TLRPC.Peer peer;
        if (user_id < 0) {
            user_id = -user_id;
            final TLRPC.Chat chat = this.getChat(user_id);
            if (!(chat instanceof TLRPC.TL_channel) && !(chat instanceof TLRPC.TL_channelForbidden)) {
                peer = new TLRPC.TL_peerChat();
                peer.chat_id = user_id;
            }
            else {
                peer = new TLRPC.TL_peerChannel();
                peer.channel_id = user_id;
            }
        }
        else {
            this.getUser(user_id);
            peer = new TLRPC.TL_peerUser();
            peer.user_id = user_id;
        }
        return peer;
    }
    
    public long getUpdatesStartTime(final int n) {
        if (n == 0) {
            return this.updatesStartWaitTimeSeq;
        }
        if (n == 1) {
            return this.updatesStartWaitTimePts;
        }
        if (n == 2) {
            return this.updatesStartWaitTimeQts;
        }
        return 0L;
    }
    
    public TLRPC.User getUser(final Integer key) {
        return this.users.get(key);
    }
    
    public TLRPC.UserFull getUserFull(final int n) {
        return (TLRPC.UserFull)this.fullUsers.get(n);
    }
    
    public TLObject getUserOrChat(final String s) {
        if (s != null && s.length() != 0) {
            return this.objectsByUsernames.get(s.toLowerCase());
        }
        return null;
    }
    
    public ConcurrentHashMap<Integer, TLRPC.User> getUsers() {
        return this.users;
    }
    
    public boolean hasHiddenArchive() {
        final boolean archiveHidden = SharedConfig.archiveHidden;
        boolean b = true;
        if (!archiveHidden || this.dialogs_dict.get(DialogObject.makeFolderDialogId(1)) == null) {
            b = false;
        }
        return b;
    }
    
    public void hideReportSpam(final long lng, final TLRPC.User user, final TLRPC.Chat chat) {
        if (user == null && chat == null) {
            return;
        }
        final SharedPreferences$Editor edit = this.notificationsPreferences.edit();
        final StringBuilder sb = new StringBuilder();
        sb.append("spam3_");
        sb.append(lng);
        edit.putInt(sb.toString(), 1);
        edit.commit();
        if ((int)lng != 0) {
            final TLRPC.TL_messages_hideReportSpam tl_messages_hideReportSpam = new TLRPC.TL_messages_hideReportSpam();
            if (user != null) {
                tl_messages_hideReportSpam.peer = this.getInputPeer(user.id);
            }
            else if (chat != null) {
                tl_messages_hideReportSpam.peer = this.getInputPeer(-chat.id);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_hideReportSpam, (RequestDelegate)_$$Lambda$MessagesController$48y_t5lk6J6veA_TARwt8n5wq_Y.INSTANCE);
        }
    }
    
    public boolean isChannelAdmin(final int n, final int i) {
        final ArrayList list = (ArrayList)this.channelAdmins.get(n);
        return list != null && list.indexOf(i) >= 0;
    }
    
    public boolean isClearingDialog(final long n) {
        return this.clearingHistoryDialogs.get(n) != null;
    }
    
    public boolean isDialogCreated(final long l) {
        return this.createdDialogMainThreadIds.contains(l);
    }
    
    public boolean isDialogMuted(final long n) {
        final SharedPreferences notificationsPreferences = this.notificationsPreferences;
        final StringBuilder sb = new StringBuilder();
        sb.append("notify2_");
        sb.append(n);
        final int int1 = notificationsPreferences.getInt(sb.toString(), -1);
        if (int1 == -1) {
            return NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(n) ^ true;
        }
        if (int1 == 2) {
            return true;
        }
        if (int1 == 3) {
            final SharedPreferences notificationsPreferences2 = this.notificationsPreferences;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("notifyuntil_");
            sb2.append(n);
            if (notificationsPreferences2.getInt(sb2.toString(), 0) >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isDialogVisible(final long l) {
        return this.visibleDialogMainThreadIds.contains(l);
    }
    
    public boolean isDialogsEndReached(final int n) {
        return this.dialogsEndReached.get(n);
    }
    
    public boolean isJoiningChannel(final int i) {
        return this.joiningToChannels.contains(i);
    }
    
    public boolean isLoadingDialogs(final int n) {
        return this.loadingDialogs.get(n);
    }
    
    public boolean isProxyDialog(final long n, final boolean b) {
        final TLRPC.Dialog proxyDialog = this.proxyDialog;
        return proxyDialog != null && proxyDialog.id == n && (!b || this.isLeftProxyChannel);
    }
    
    public boolean isServerDialogsEndReached(final int n) {
        return this.serverDialogsEndReached.get(n);
    }
    
    public void loadChannelAdmins(final int n, final boolean b) {
        if (this.loadingChannelAdmins.indexOfKey(n) >= 0) {
            return;
        }
        final SparseIntArray loadingChannelAdmins = this.loadingChannelAdmins;
        int i = 0;
        loadingChannelAdmins.put(n, 0);
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).loadChannelAdmins(n);
        }
        else {
            final TLRPC.TL_channels_getParticipants tl_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
            final ArrayList list = (ArrayList)this.channelAdmins.get(n);
            if (list != null) {
                long n2 = 0L;
                while (i < list.size()) {
                    n2 = (n2 * 20261L + 2147483648L + list.get(i)) % 2147483648L;
                    ++i;
                }
                tl_channels_getParticipants.hash = (int)n2;
            }
            tl_channels_getParticipants.channel = this.getInputChannel(n);
            tl_channels_getParticipants.limit = 100;
            tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsAdmins();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getParticipants, new _$$Lambda$MessagesController$5NQ9MFRgqgfYWG2HdrbXnmbFM_o(this, n));
        }
    }
    
    public void loadChannelParticipants(final Integer e) {
        if (!this.loadingFullParticipants.contains(e)) {
            if (!this.loadedFullParticipants.contains(e)) {
                this.loadingFullParticipants.add(e);
                final TLRPC.TL_channels_getParticipants tl_channels_getParticipants = new TLRPC.TL_channels_getParticipants();
                tl_channels_getParticipants.channel = this.getInputChannel(e);
                tl_channels_getParticipants.filter = new TLRPC.TL_channelParticipantsRecent();
                tl_channels_getParticipants.offset = 0;
                tl_channels_getParticipants.limit = 32;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getParticipants, new _$$Lambda$MessagesController$2gEZaEhNLvSnEOT8iEFZtRspSU0(this, e));
            }
        }
    }
    
    public void loadChatInfo(final int n, final CountDownLatch countDownLatch, final boolean b) {
        MessagesStorage.getInstance(this.currentAccount).loadChatInfo(n, countDownLatch, b, false);
    }
    
    public void loadCurrentState() {
        if (this.updatingState) {
            return;
        }
        this.updatingState = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_updates_getState(), new _$$Lambda$MessagesController$cVyFjAxO3BTy_3AQHVKKSH7bs_0(this));
    }
    
    public void loadDialogPhotos(int i, final int n, final long n2, final boolean b, final int n3) {
        if (b) {
            MessagesStorage.getInstance(this.currentAccount).getDialogPhotos(i, n, n2, n3);
        }
        else if (i > 0) {
            final TLRPC.User user = this.getUser(i);
            if (user == null) {
                return;
            }
            final TLRPC.TL_photos_getUserPhotos tl_photos_getUserPhotos = new TLRPC.TL_photos_getUserPhotos();
            tl_photos_getUserPhotos.limit = n;
            tl_photos_getUserPhotos.offset = 0;
            tl_photos_getUserPhotos.max_id = (int)n2;
            tl_photos_getUserPhotos.user_id = this.getInputUser(user);
            i = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_photos_getUserPhotos, new _$$Lambda$MessagesController$WZMD1Tmg9CzkfKN6c7nkyjvpJFA(this, i, n, n2, n3));
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(i, n3);
        }
        else if (i < 0) {
            final TLRPC.TL_messages_search tl_messages_search = new TLRPC.TL_messages_search();
            tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterChatPhotos();
            tl_messages_search.limit = n;
            tl_messages_search.offset_id = (int)n2;
            tl_messages_search.q = "";
            tl_messages_search.peer = this.getInputPeer(i);
            i = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_search, new _$$Lambda$MessagesController$mljRdiyVSAoNeixnSx85I7LZjOE(this, i, n, n2, n3));
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(i, n3);
        }
    }
    
    public void loadDialogs(final int n, final int n2, final int n3, final boolean b) {
        this.loadDialogs(n, n2, n3, b, null);
    }
    
    public void loadDialogs(final int n, int i, final int n2, final boolean b, final Runnable runnable) {
        if (!this.loadingDialogs.get(n)) {
            if (!this.resetingDialogs) {
                final SparseBooleanArray loadingDialogs = this.loadingDialogs;
                final int n3 = 1;
                loadingDialogs.put(n, true);
                final NotificationCenter instance = NotificationCenter.getInstance(this.currentAccount);
                final int dialogsNeedReload = NotificationCenter.dialogsNeedReload;
                final int n4 = 0;
                instance.postNotificationName(dialogsNeedReload, new Object[0]);
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("folderId = ");
                    sb.append(n);
                    sb.append(" load cacheOffset = ");
                    sb.append(i);
                    sb.append(" count = ");
                    sb.append(n2);
                    sb.append(" cache = ");
                    sb.append(b);
                    FileLog.d(sb.toString());
                }
                if (b) {
                    final MessagesStorage instance2 = MessagesStorage.getInstance(this.currentAccount);
                    if (i == 0) {
                        i = n4;
                    }
                    else {
                        i = this.nextDialogsCacheOffset.get(n, 0);
                    }
                    instance2.getDialogs(n, i, n2);
                }
                else {
                    final TLRPC.TL_messages_getDialogs tl_messages_getDialogs = new TLRPC.TL_messages_getDialogs();
                    tl_messages_getDialogs.limit = n2;
                    tl_messages_getDialogs.exclude_pinned = true;
                    if (n != 0) {
                        tl_messages_getDialogs.flags |= 0x2;
                        tl_messages_getDialogs.folder_id = n;
                    }
                    final int[] dialogLoadOffsets = UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(n);
                    Label_0699: {
                        if (dialogLoadOffsets[0] == -1) {
                            final ArrayList<TLRPC.Dialog> dialogs = this.getDialogs(n);
                            while (true) {
                                TLRPC.Dialog dialog;
                                long id;
                                int n5;
                                int n6;
                                MessageObject messageObject;
                                TLRPC.Message messageOwner;
                                TLRPC.Peer to_id;
                                for (i = dialogs.size() - 1; i >= 0; --i) {
                                    dialog = dialogs.get(i);
                                    if (!dialog.pinned) {
                                        id = dialog.id;
                                        n5 = (int)id;
                                        n6 = (int)(id >> 32);
                                        if (n5 != 0 && n6 != 1 && dialog.top_message > 0) {
                                            messageObject = (MessageObject)this.dialogMessage.get(id);
                                            if (messageObject != null && messageObject.getId() > 0) {
                                                messageOwner = messageObject.messageOwner;
                                                tl_messages_getDialogs.offset_date = messageOwner.date;
                                                tl_messages_getDialogs.offset_id = messageOwner.id;
                                                to_id = messageOwner.to_id;
                                                i = to_id.channel_id;
                                                Label_0659: {
                                                    if (i == 0) {
                                                        i = to_id.chat_id;
                                                        if (i == 0) {
                                                            i = to_id.user_id;
                                                            break Label_0659;
                                                        }
                                                    }
                                                    i = -i;
                                                }
                                                tl_messages_getDialogs.offset_peer = this.getInputPeer(i);
                                                i = n3;
                                                if (i == 0) {
                                                    tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerEmpty();
                                                }
                                                break Label_0699;
                                            }
                                        }
                                    }
                                }
                                i = 0;
                                continue;
                            }
                        }
                        if (dialogLoadOffsets[0] == Integer.MAX_VALUE) {
                            this.dialogsEndReached.put(n, true);
                            this.serverDialogsEndReached.put(n, true);
                            this.loadingDialogs.put(n, false);
                            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                            return;
                        }
                        tl_messages_getDialogs.offset_id = dialogLoadOffsets[0];
                        tl_messages_getDialogs.offset_date = dialogLoadOffsets[1];
                        if (tl_messages_getDialogs.offset_id == 0) {
                            tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerEmpty();
                        }
                        else {
                            if (dialogLoadOffsets[4] != 0) {
                                tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerChannel();
                                tl_messages_getDialogs.offset_peer.channel_id = dialogLoadOffsets[4];
                            }
                            else if (dialogLoadOffsets[2] != 0) {
                                tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerUser();
                                tl_messages_getDialogs.offset_peer.user_id = dialogLoadOffsets[2];
                            }
                            else {
                                tl_messages_getDialogs.offset_peer = new TLRPC.TL_inputPeerChat();
                                tl_messages_getDialogs.offset_peer.chat_id = dialogLoadOffsets[3];
                            }
                            tl_messages_getDialogs.offset_peer.access_hash = ((long)dialogLoadOffsets[5] << 32 | (long)dialogLoadOffsets[5]);
                        }
                    }
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getDialogs, new _$$Lambda$MessagesController$7uQARORTbE9n3vBAnDQr0_O75Rk(this, n, n2, runnable));
                }
            }
        }
    }
    
    public void loadFullChat(int sendRequest, final int n, final boolean b) {
        final boolean contains = this.loadedFullChats.contains(sendRequest);
        if (!this.loadingFullChats.contains(sendRequest)) {
            if (b || !contains) {
                this.loadingFullChats.add(sendRequest);
                final long n2 = -sendRequest;
                final TLRPC.Chat chat = this.getChat(sendRequest);
                TLRPC.TL_channels_getFullChannel tl_channels_getFullChannel2 = null;
                Label_0181: {
                    if (ChatObject.isChannel(chat)) {
                        final TLRPC.TL_channels_getFullChannel tl_channels_getFullChannel = new TLRPC.TL_channels_getFullChannel();
                        tl_channels_getFullChannel.channel = getInputChannel(chat);
                        tl_channels_getFullChannel2 = tl_channels_getFullChannel;
                        if (chat.megagroup) {
                            this.loadChannelAdmins(sendRequest, contains ^ true);
                            tl_channels_getFullChannel2 = tl_channels_getFullChannel;
                        }
                    }
                    else {
                        final TLRPC.TL_messages_getFullChat tl_messages_getFullChat = new TLRPC.TL_messages_getFullChat();
                        tl_messages_getFullChat.chat_id = sendRequest;
                        if (this.dialogs_read_inbox_max.get(n2) != null) {
                            tl_channels_getFullChannel2 = (TLRPC.TL_channels_getFullChannel)tl_messages_getFullChat;
                            if (this.dialogs_read_outbox_max.get(n2) != null) {
                                break Label_0181;
                            }
                        }
                        this.reloadDialogsReadValue(null, n2);
                        tl_channels_getFullChannel2 = (TLRPC.TL_channels_getFullChannel)tl_messages_getFullChat;
                    }
                }
                sendRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_getFullChannel2, new _$$Lambda$MessagesController$ebifWTnrFPKK5b_9D1xusLXGK7Q(this, chat, n2, sendRequest, n));
                if (n != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(sendRequest, n);
                }
            }
        }
    }
    
    public void loadFullUser(final TLRPC.User user, final int n, final boolean b) {
        if (user != null && !this.loadingFullUsers.contains(user.id)) {
            if (b || !this.loadedFullUsers.contains(user.id)) {
                this.loadingFullUsers.add(user.id);
                final TLRPC.TL_users_getFullUser tl_users_getFullUser = new TLRPC.TL_users_getFullUser();
                tl_users_getFullUser.id = this.getInputUser(user);
                final long n2 = user.id;
                if (this.dialogs_read_inbox_max.get(n2) == null || this.dialogs_read_outbox_max.get(n2) == null) {
                    this.reloadDialogsReadValue(null, n2);
                }
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_users_getFullUser, new _$$Lambda$MessagesController$sHevIZGEf9jGeQ0PmpgZfE_2SOQ(this, user, n)), n);
            }
        }
    }
    
    public void loadGlobalNotificationsSettings() {
        if (this.loadingNotificationSettings == 0 && !UserConfig.getInstance(this.currentAccount).notificationsSettingsLoaded) {
            final SharedPreferences notificationsSettings = getNotificationsSettings(this.currentAccount);
            SharedPreferences$Editor edit = null;
            if (notificationsSettings.contains("EnableGroup")) {
                final boolean boolean1 = notificationsSettings.getBoolean("EnableGroup", true);
                edit = notificationsSettings.edit();
                if (!boolean1) {
                    edit.putInt("EnableGroup2", Integer.MAX_VALUE);
                    edit.putInt("EnableChannel2", Integer.MAX_VALUE);
                }
                edit.remove("EnableGroup").commit();
            }
            SharedPreferences$Editor edit2 = edit;
            if (notificationsSettings.contains("EnableAll")) {
                final boolean boolean2 = notificationsSettings.getBoolean("EnableAll", true);
                if ((edit2 = edit) == null) {
                    edit2 = notificationsSettings.edit();
                }
                if (!boolean2) {
                    edit2.putInt("EnableAll2", Integer.MAX_VALUE);
                }
                edit2.remove("EnableAll").commit();
            }
            if (edit2 != null) {
                edit2.commit();
            }
            this.loadingNotificationSettings = 3;
            for (int i = 0; i < 3; ++i) {
                final TLRPC.TL_account_getNotifySettings tl_account_getNotifySettings = new TLRPC.TL_account_getNotifySettings();
                if (i == 0) {
                    tl_account_getNotifySettings.peer = new TLRPC.TL_inputNotifyChats();
                }
                else if (i == 1) {
                    tl_account_getNotifySettings.peer = new TLRPC.TL_inputNotifyUsers();
                }
                else if (i == 2) {
                    tl_account_getNotifySettings.peer = new TLRPC.TL_inputNotifyBroadcasts();
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_getNotifySettings, new _$$Lambda$MessagesController$Kw60WHVImMJ5bL_qjfLao1O7Ey8(this, i));
            }
        }
        if (!UserConfig.getInstance(this.currentAccount).notificationsSignUpSettingsLoaded) {
            this.loadSignUpNotificationsSettings();
        }
    }
    
    public void loadHintDialogs() {
        if (this.hintDialogs.isEmpty()) {
            if (!TextUtils.isEmpty((CharSequence)this.installReferer)) {
                final TLRPC.TL_help_getRecentMeUrls tl_help_getRecentMeUrls = new TLRPC.TL_help_getRecentMeUrls();
                tl_help_getRecentMeUrls.referer = this.installReferer;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_help_getRecentMeUrls, new _$$Lambda$MessagesController$S17IW_TdnLou1yG0GrX54gGAuDE(this));
            }
        }
    }
    
    public void loadMessages(final long n, final int n2, final int n3, final int n4, final boolean b, final int n5, final int n6, final int n7, final int n8, final boolean b2, final int n9) {
        this.loadMessages(n, n2, n3, n4, b, n5, n6, n7, n8, b2, n9, 0, 0, 0, false, 0);
    }
    
    public void loadMessages(final long n, final int n2, final int n3, final int n4, final boolean b, final int n5, final int n6, final int n7, final int n8, final boolean b2, final int n9, final int n10, final int n11, final int n12, final boolean b3, final int n13) {
        this.loadMessagesInternal(n, n2, n3, n4, b, n5, n6, n7, n8, b2, n9, n10, n11, n12, b3, n13, true);
    }
    
    public void loadPeerSettings(final TLRPC.User user, final TLRPC.Chat chat) {
        if (user == null && chat == null) {
            return;
        }
        int id;
        if (user != null) {
            id = user.id;
        }
        else {
            id = -chat.id;
        }
        final long n = id;
        if (this.loadingPeerSettings.indexOfKey(n) >= 0) {
            return;
        }
        this.loadingPeerSettings.put(n, (Object)true);
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("request spam button for ");
            sb.append(n);
            FileLog.d(sb.toString());
        }
        final SharedPreferences notificationsPreferences = this.notificationsPreferences;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("spam3_");
        sb2.append(n);
        if (notificationsPreferences.getInt(sb2.toString(), 0) == 1) {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("spam button already hidden for ");
                sb3.append(n);
                FileLog.d(sb3.toString());
            }
            return;
        }
        final SharedPreferences notificationsPreferences2 = this.notificationsPreferences;
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("spam_");
        sb4.append(n);
        if (notificationsPreferences2.getBoolean(sb4.toString(), false)) {
            final TLRPC.TL_messages_hideReportSpam tl_messages_hideReportSpam = new TLRPC.TL_messages_hideReportSpam();
            if (user != null) {
                tl_messages_hideReportSpam.peer = this.getInputPeer(user.id);
            }
            else if (chat != null) {
                tl_messages_hideReportSpam.peer = this.getInputPeer(-chat.id);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_hideReportSpam, new _$$Lambda$MessagesController$earKZXcnSlbNSw2ZGT5Q1z7WPUw(this, n));
            return;
        }
        final TLRPC.TL_messages_getPeerSettings tl_messages_getPeerSettings = new TLRPC.TL_messages_getPeerSettings();
        if (user != null) {
            tl_messages_getPeerSettings.peer = this.getInputPeer(user.id);
        }
        else if (chat != null) {
            tl_messages_getPeerSettings.peer = this.getInputPeer(-chat.id);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getPeerSettings, new _$$Lambda$MessagesController$Akjl4BU34QO3ZqaL4fYbP0pvGoA(this, n));
    }
    
    public void loadPinnedDialogs(final int folder_id, final long n, final ArrayList<Long> list) {
        if (this.loadingPinnedDialogs.indexOfKey(folder_id) < 0) {
            if (!UserConfig.getInstance(this.currentAccount).isPinnedDialogsLoaded(folder_id)) {
                this.loadingPinnedDialogs.put(folder_id, 1);
                final TLRPC.TL_messages_getPinnedDialogs tl_messages_getPinnedDialogs = new TLRPC.TL_messages_getPinnedDialogs();
                tl_messages_getPinnedDialogs.folder_id = folder_id;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getPinnedDialogs, new _$$Lambda$MessagesController$5ms5SUOMLniJAFeiQgpTUwbDtSY(this, folder_id));
            }
        }
    }
    
    public void loadSignUpNotificationsSettings() {
        if (!this.loadingNotificationSignUpSettings) {
            this.loadingNotificationSignUpSettings = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_account_getContactSignUpNotification(), new _$$Lambda$MessagesController$rOfsCqpWEwvJI16XzaGq8LurqHg(this));
        }
    }
    
    protected void loadUnknownChannel(final TLRPC.Chat chat, final long n) {
        if (chat instanceof TLRPC.TL_channel) {
            if (this.gettingUnknownChannels.indexOfKey(chat.id) < 0) {
                if (chat.access_hash == 0L) {
                    if (n != 0L) {
                        MessagesStorage.getInstance(this.currentAccount).removePendingTask(n);
                    }
                    return;
                }
                final TLRPC.TL_inputPeerChannel peer = new TLRPC.TL_inputPeerChannel();
                final int id = chat.id;
                peer.channel_id = id;
                peer.access_hash = chat.access_hash;
                this.gettingUnknownChannels.put(id, true);
                final TLRPC.TL_messages_getPeerDialogs tl_messages_getPeerDialogs = new TLRPC.TL_messages_getPeerDialogs();
                final TLRPC.TL_inputDialogPeer e = new TLRPC.TL_inputDialogPeer();
                e.peer = peer;
                tl_messages_getPeerDialogs.peers.add(e);
                long pendingTask = n;
                if (n == 0L) {
                    final NativeByteBuffer nativeByteBuffer = null;
                    NativeByteBuffer nativeByteBuffer2;
                    try {
                        nativeByteBuffer2 = new NativeByteBuffer(chat.getObjectSize() + 4);
                        try {
                            nativeByteBuffer2.writeInt32(0);
                            chat.serializeToStream(nativeByteBuffer2);
                        }
                        catch (Exception ex) {}
                    }
                    catch (Exception ex) {
                        nativeByteBuffer2 = nativeByteBuffer;
                    }
                    final Exception ex;
                    FileLog.e(ex);
                    pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer2);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getPeerDialogs, new _$$Lambda$MessagesController$gfG4ToHa5Z2rjZdIfshLWaUs_Ms(this, pendingTask, chat));
            }
        }
    }
    
    protected void loadUnknownDialog(final TLRPC.InputPeer peer, final long n) {
        if (peer == null) {
            return;
        }
        final long peerDialogId = DialogObject.getPeerDialogId(peer);
        if (this.gettingUnknownDialogs.indexOfKey(peerDialogId) >= 0) {
            return;
        }
        this.gettingUnknownDialogs.put(peerDialogId, (Object)true);
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("load unknown dialog ");
            sb.append(peerDialogId);
            FileLog.d(sb.toString());
        }
        final TLRPC.TL_messages_getPeerDialogs tl_messages_getPeerDialogs = new TLRPC.TL_messages_getPeerDialogs();
        final TLRPC.TL_inputDialogPeer e = new TLRPC.TL_inputDialogPeer();
        e.peer = peer;
        tl_messages_getPeerDialogs.peers.add(e);
        long pendingTask = n;
        if (n == 0L) {
            NativeByteBuffer nativeByteBuffer2;
            Exception ex = null;
            try {
                final NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(peer.getObjectSize() + 4);
                try {
                    nativeByteBuffer.writeInt32(15);
                    peer.serializeToStream(nativeByteBuffer);
                    nativeByteBuffer2 = nativeByteBuffer;
                }
                catch (Exception ex) {
                    nativeByteBuffer2 = nativeByteBuffer;
                }
            }
            catch (Exception ex2) {
                nativeByteBuffer2 = null;
                ex = ex2;
            }
            FileLog.e(ex);
            pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer2);
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getPeerDialogs, new _$$Lambda$MessagesController$_qOB0YtC_RNY0KjHwuxhfdX_Ckg(this, pendingTask, peerDialogId));
    }
    
    public void loadUnreadDialogs() {
        if (!this.loadingUnreadDialogs) {
            if (!UserConfig.getInstance(this.currentAccount).unreadDialogsLoaded) {
                this.loadingUnreadDialogs = true;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_messages_getDialogUnreadMarks(), new _$$Lambda$MessagesController$A7Jim0c_hwW7n1Xp7g9CGcD85b0(this));
            }
        }
    }
    
    public void loadUserInfo(final TLRPC.User user, final boolean b, final int n) {
        MessagesStorage.getInstance(this.currentAccount).loadUserInfo(user, b, n);
    }
    
    public void markChannelDialogMessageAsDeleted(final ArrayList<Integer> list, int i) {
        final MessageObject messageObject = (MessageObject)this.dialogMessage.get((long)(-i));
        if (messageObject != null) {
            for (i = 0; i < list.size(); ++i) {
                if (messageObject.getId() == list.get(i)) {
                    messageObject.deleted = true;
                    break;
                }
            }
        }
    }
    
    public void markDialogAsRead(final long n, final int b, int max, final int b2, final boolean b3, final int n2, final boolean b4) {
        final int n3 = (int)n;
        final int i = (int)(n >> 32);
        final boolean showBadgeMessages = NotificationsController.getInstance(this.currentAccount).showBadgeMessages;
        Label_0348: {
            if (n3 != 0) {
                if (b == 0 || i == 1) {
                    return;
                }
                long n4 = b;
                long n5 = max;
                boolean b5 = false;
                Label_0106: {
                    if (n3 < 0) {
                        max = -n3;
                        if (ChatObject.isChannel(this.getChat(max))) {
                            final long n6 = (long)max << 32;
                            n5 |= n6;
                            n4 |= n6;
                            b5 = true;
                            break Label_0106;
                        }
                    }
                    b5 = false;
                }
                Integer value;
                if ((value = this.dialogs_read_inbox_max.get(n)) == null) {
                    value = 0;
                }
                this.dialogs_read_inbox_max.put(n, Math.max(value, b));
                MessagesStorage.getInstance(this.currentAccount).processPendingRead(n, n4, n5, b2, b5);
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$hn_SvAXVeuxCYnxgaaX_Rgf7tNs(this, n, n2, b, showBadgeMessages, b3));
                if (b == Integer.MAX_VALUE) {
                    max = 0;
                    break Label_0348;
                }
            }
            else {
                if (b2 == 0) {
                    return;
                }
                final TLRPC.EncryptedChat encryptedChat = this.getEncryptedChat(i);
                MessagesStorage.getInstance(this.currentAccount).processPendingRead(n, b, max, b2, false);
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$w9XQvAOdl78XSvAXCJDSbdLvKsQ(this, n, b2, b3, n2, max, showBadgeMessages));
                if (encryptedChat != null && encryptedChat.ttl > 0) {
                    max = Math.max(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime(), b2);
                    MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(encryptedChat.id, max, max, 0, null);
                }
            }
            max = 1;
        }
        if (max != 0) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$_VEgWywMVWfxr_9Ssi_lPDOcdtc(this, n, b4, b2, b));
        }
    }
    
    public void markDialogAsReadNow(final long n) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$5qY48mZcpUp58ZVQMwppSQnDlKQ(this, n));
    }
    
    public void markDialogAsUnread(final long n, TLRPC.InputPeer inputPeer, final long n2) {
        final TLRPC.Dialog dialog = (TLRPC.Dialog)this.dialogs_dict.get(n);
        if (dialog != null) {
            dialog.unread_mark = true;
            if (dialog.unread_count == 0 && !this.isDialogMuted(n)) {
                ++this.unreadUnmutedDialogs;
            }
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 256);
            MessagesStorage.getInstance(this.currentAccount).setDialogUnread(n, true);
        }
        final int n3 = (int)n;
        if (n3 != 0) {
            final TLRPC.TL_messages_markDialogUnread tl_messages_markDialogUnread = new TLRPC.TL_messages_markDialogUnread();
            tl_messages_markDialogUnread.unread = true;
            TLRPC.InputPeer inputPeer2;
            if ((inputPeer2 = inputPeer) == null) {
                inputPeer2 = this.getInputPeer(n3);
            }
            if (inputPeer2 instanceof TLRPC.TL_inputPeerEmpty) {
                return;
            }
            final TLRPC.TL_inputDialogPeer peer = new TLRPC.TL_inputDialogPeer();
            peer.peer = inputPeer2;
            tl_messages_markDialogUnread.peer = peer;
            long pendingTask = n2;
            if (n2 == 0L) {
                try {
                    inputPeer = (TLRPC.InputPeer)new NativeByteBuffer(inputPeer2.getObjectSize() + 12);
                    try {
                        ((NativeByteBuffer)inputPeer).writeInt32(9);
                        ((NativeByteBuffer)inputPeer).writeInt64(n);
                        inputPeer2.serializeToStream((AbstractSerializedData)inputPeer);
                    }
                    catch (Exception ex) {}
                }
                catch (Exception ex) {
                    inputPeer = null;
                }
                final Exception ex;
                FileLog.e(ex);
                pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask((NativeByteBuffer)inputPeer);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_markDialogUnread, new _$$Lambda$MessagesController$vdUYw660VQTUH5B_NPbzLsGZ09M(this, pendingTask));
        }
    }
    
    public void markMentionMessageAsRead(final int n, final int n2, final long n3) {
        MessagesStorage.getInstance(this.currentAccount).markMentionMessageAsRead(n, n2, n3);
        if (n2 != 0) {
            final TLRPC.TL_channels_readMessageContents tl_channels_readMessageContents = new TLRPC.TL_channels_readMessageContents();
            tl_channels_readMessageContents.channel = this.getInputChannel(n2);
            if (tl_channels_readMessageContents.channel == null) {
                return;
            }
            tl_channels_readMessageContents.id.add(n);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_readMessageContents, (RequestDelegate)_$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM.INSTANCE);
        }
        else {
            final TLRPC.TL_messages_readMessageContents tl_messages_readMessageContents = new TLRPC.TL_messages_readMessageContents();
            tl_messages_readMessageContents.id.add(n);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_readMessageContents, new _$$Lambda$MessagesController$HGjYxt4hoLR6qJGd10EuSLrAkII(this));
        }
    }
    
    public void markMentionsAsRead(final long n) {
        final int n2 = (int)n;
        if (n2 == 0) {
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).resetMentionsCount(n, 0);
        final TLRPC.TL_messages_readMentions tl_messages_readMentions = new TLRPC.TL_messages_readMentions();
        tl_messages_readMentions.peer = getInstance(this.currentAccount).getInputPeer(n2);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_readMentions, (RequestDelegate)_$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4.INSTANCE);
    }
    
    public void markMessageAsRead(final int n, final int n2, TLRPC.InputChannel inputChannel, final int n3, final long n4) {
        if (n != 0) {
            if (n3 > 0) {
                Object channel = inputChannel;
                if (n2 != 0 && (channel = inputChannel) == null) {
                    inputChannel = this.getInputChannel(n2);
                    if ((channel = inputChannel) == null) {
                        return;
                    }
                }
                long pendingTask = n4;
                if (n4 == 0L) {
                    Object o = null;
                    Label_0140: {
                        try {
                            int objectSize;
                            if (channel != null) {
                                objectSize = ((TLObject)channel).getObjectSize();
                            }
                            else {
                                objectSize = 0;
                            }
                            inputChannel = new NativeByteBuffer(16 + objectSize);
                            try {
                                ((NativeByteBuffer)inputChannel).writeInt32(11);
                                ((NativeByteBuffer)inputChannel).writeInt32(n);
                                ((NativeByteBuffer)inputChannel).writeInt32(n2);
                                ((NativeByteBuffer)inputChannel).writeInt32(n3);
                                o = inputChannel;
                                if (n2 != 0) {
                                    ((TLObject)channel).serializeToStream((AbstractSerializedData)inputChannel);
                                    o = inputChannel;
                                }
                                break Label_0140;
                            }
                            catch (Exception ex) {}
                        }
                        catch (Exception ex) {
                            inputChannel = null;
                        }
                        final Exception ex;
                        FileLog.e(ex);
                        o = inputChannel;
                    }
                    pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask((NativeByteBuffer)o);
                }
                final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                MessagesStorage.getInstance(this.currentAccount).createTaskForMid(n, n2, currentTime, currentTime, n3, false);
                if (n2 != 0) {
                    final TLRPC.TL_channels_readMessageContents tl_channels_readMessageContents = new TLRPC.TL_channels_readMessageContents();
                    tl_channels_readMessageContents.channel = (TLRPC.InputChannel)channel;
                    tl_channels_readMessageContents.id.add(n);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_readMessageContents, new _$$Lambda$MessagesController$U0WOqTBPKmEhdWNNNHXKPpg45io(this, pendingTask));
                }
                else {
                    final TLRPC.TL_messages_readMessageContents tl_messages_readMessageContents = new TLRPC.TL_messages_readMessageContents();
                    tl_messages_readMessageContents.id.add(n);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_readMessageContents, new _$$Lambda$MessagesController$jXQeCffIHFZ5lMnVQBUWXIuIwZ4(this, pendingTask));
                }
            }
        }
    }
    
    public void markMessageAsRead(final long n, final long l, int currentTime) {
        if (l != 0L && n != 0L) {
            if (currentTime > 0 || currentTime == Integer.MIN_VALUE) {
                final int n2 = (int)n;
                final int i = (int)(n >> 32);
                if (n2 != 0) {
                    return;
                }
                final TLRPC.EncryptedChat encryptedChat = this.getEncryptedChat(i);
                if (encryptedChat == null) {
                    return;
                }
                final ArrayList<Long> list = new ArrayList<Long>();
                list.add(l);
                SecretChatHelper.getInstance(this.currentAccount).sendMessagesReadMessage(encryptedChat, list, null);
                if (currentTime > 0) {
                    currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                    MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(encryptedChat.id, currentTime, currentTime, 0, list);
                }
            }
        }
    }
    
    public void markMessageContentAsRead(final MessageObject messageObject) {
        final ArrayList<Long> list = new ArrayList<Long>();
        final long n = messageObject.getId();
        final int channel_id = messageObject.messageOwner.to_id.channel_id;
        long l = n;
        if (channel_id != 0) {
            l = (n | (long)channel_id << 32);
        }
        if (messageObject.messageOwner.mentioned) {
            MessagesStorage.getInstance(this.currentAccount).markMentionMessageAsRead(messageObject.getId(), messageObject.messageOwner.to_id.channel_id, messageObject.getDialogId());
        }
        list.add(l);
        MessagesStorage.getInstance(this.currentAccount).markMessagesContentAsRead(list, 0);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, list);
        if (messageObject.getId() < 0) {
            this.markMessageAsRead(messageObject.getDialogId(), messageObject.messageOwner.random_id, Integer.MIN_VALUE);
        }
        else if (messageObject.messageOwner.to_id.channel_id != 0) {
            final TLRPC.TL_channels_readMessageContents tl_channels_readMessageContents = new TLRPC.TL_channels_readMessageContents();
            tl_channels_readMessageContents.channel = this.getInputChannel(messageObject.messageOwner.to_id.channel_id);
            if (tl_channels_readMessageContents.channel == null) {
                return;
            }
            tl_channels_readMessageContents.id.add(messageObject.getId());
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_readMessageContents, (RequestDelegate)_$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE.INSTANCE);
        }
        else {
            final TLRPC.TL_messages_readMessageContents tl_messages_readMessageContents = new TLRPC.TL_messages_readMessageContents();
            tl_messages_readMessageContents.id.add(messageObject.getId());
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_readMessageContents, new _$$Lambda$MessagesController$gAx8uSIsyHdT1Tx6VIiWEu0UYbQ(this));
        }
    }
    
    protected void onFolderEmpty(final int n) {
        if (UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(n)[0] == Integer.MAX_VALUE) {
            this.removeFolder(n);
        }
        else {
            this.loadDialogs(n, 0, 10, false, new _$$Lambda$MessagesController$jEkulIUjpo2aQz9ir2LF9vSzFoc(this, n));
        }
    }
    
    public void openByUserName(final String username, final BaseFragment baseFragment, final int n) {
        if (username != null) {
            if (baseFragment != null) {
                final TLObject userOrChat = this.getUserOrChat(username);
                TLObject tlObject = null;
                TLRPC.Chat chat = null;
                Label_0089: {
                    if (userOrChat instanceof TLRPC.User) {
                        tlObject = userOrChat;
                        if (!((TLRPC.User)tlObject).min) {
                            chat = null;
                            break Label_0089;
                        }
                    }
                    else if (userOrChat instanceof TLRPC.Chat) {
                        chat = (TLRPC.Chat)userOrChat;
                        if (!chat.min) {
                            tlObject = null;
                            break Label_0089;
                        }
                    }
                    tlObject = (chat = null);
                }
                if (tlObject != null) {
                    openChatOrProfileWith((TLRPC.User)tlObject, null, baseFragment, n, false);
                }
                else if (chat != null) {
                    openChatOrProfileWith(null, chat, baseFragment, 1, false);
                }
                else {
                    if (baseFragment.getParentActivity() == null) {
                        return;
                    }
                    final AlertDialog[] array = { new AlertDialog((Context)baseFragment.getParentActivity(), 3) };
                    final TLRPC.TL_contacts_resolveUsername tl_contacts_resolveUsername = new TLRPC.TL_contacts_resolveUsername();
                    tl_contacts_resolveUsername.username = username;
                    AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$D332AVh7nCXc7vu_l1allSBL4LE(this, array, ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_resolveUsername, new _$$Lambda$MessagesController$2QxSvtTZ9U28CTPZ6uf2mJLKqt0(this, array, baseFragment, n)), baseFragment), 500L);
                }
            }
        }
    }
    
    public void performLogout(final int n) {
        boolean b = true;
        if (n == 1) {
            this.unregistedPush();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_auth_logOut(), new _$$Lambda$MessagesController$iSBguIqFt9uYneQ2bb0JRjCTxlQ(this));
        }
        else {
            final ConnectionsManager instance = ConnectionsManager.getInstance(this.currentAccount);
            if (n != 2) {
                b = false;
            }
            instance.cleanup(b);
        }
        UserConfig.getInstance(this.currentAccount).clearConfig();
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.appDidLogout, new Object[0]);
        MessagesStorage.getInstance(this.currentAccount).cleanup(false);
        this.cleanup();
        ContactsController.getInstance(this.currentAccount).deleteUnknownAppAccounts();
    }
    
    public boolean pinDialog(final long n, final boolean b, TLRPC.InputPeer inputPeer, long pendingTask) {
        final int n2 = (int)n;
        final TLRPC.Dialog dialog = (TLRPC.Dialog)this.dialogs_dict.get(n);
        final boolean b2 = true;
        if (dialog != null && dialog.pinned != b) {
            final int folder_id = dialog.folder_id;
            final ArrayList<TLRPC.Dialog> dialogs = this.getDialogs(folder_id);
            dialog.pinned = b;
            if (b) {
                int i = 0;
                int max = 0;
                while (i < dialogs.size()) {
                    final TLRPC.Dialog dialog2 = dialogs.get(i);
                    if (!(dialog2 instanceof TLRPC.TL_dialogFolder)) {
                        if (!dialog2.pinned) {
                            break;
                        }
                        max = Math.max(dialog2.pinnedNum, max);
                    }
                    ++i;
                }
                dialog.pinnedNum = max + 1;
            }
            else {
                dialog.pinnedNum = 0;
            }
            this.sortDialogs(null);
            if (!b && dialogs.get(dialogs.size() - 1) == dialog && !this.dialogsEndReached.get(folder_id)) {
                dialogs.remove(dialogs.size() - 1);
            }
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            if (n2 != 0 && pendingTask != -1L) {
                final TLRPC.TL_messages_toggleDialogPin tl_messages_toggleDialogPin = new TLRPC.TL_messages_toggleDialogPin();
                tl_messages_toggleDialogPin.pinned = b;
                TLRPC.InputPeer inputPeer2;
                if (inputPeer == null) {
                    inputPeer2 = this.getInputPeer(n2);
                }
                else {
                    inputPeer2 = inputPeer;
                }
                if (inputPeer2 instanceof TLRPC.TL_inputPeerEmpty) {
                    return false;
                }
                final TLRPC.TL_inputDialogPeer peer = new TLRPC.TL_inputDialogPeer();
                peer.peer = inputPeer2;
                tl_messages_toggleDialogPin.peer = peer;
                if (pendingTask == 0L) {
                    try {
                        inputPeer = (TLRPC.InputPeer)new NativeByteBuffer(inputPeer2.getObjectSize() + 16);
                        try {
                            ((NativeByteBuffer)inputPeer).writeInt32(4);
                            ((NativeByteBuffer)inputPeer).writeInt64(n);
                            ((NativeByteBuffer)inputPeer).writeBool(b);
                            inputPeer2.serializeToStream((AbstractSerializedData)inputPeer);
                        }
                        catch (Exception ex) {}
                    }
                    catch (Exception ex) {
                        inputPeer = null;
                    }
                    final Exception ex;
                    FileLog.e(ex);
                    pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask((NativeByteBuffer)inputPeer);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_toggleDialogPin, new _$$Lambda$MessagesController$T_HrG7SvNN3seY_Zr_dkkRoKYHQ(this, pendingTask));
            }
            MessagesStorage.getInstance(this.currentAccount).setDialogPinned(n, dialog.pinnedNum);
            return true;
        }
        return dialog != null && b2;
    }
    
    public void pinMessage(final TLRPC.Chat chat, final TLRPC.User user, final int id, final boolean b) {
        if (chat == null && user == null) {
            return;
        }
        final TLRPC.TL_messages_updatePinnedMessage tl_messages_updatePinnedMessage = new TLRPC.TL_messages_updatePinnedMessage();
        int id2;
        if (chat != null) {
            id2 = -chat.id;
        }
        else {
            id2 = user.id;
        }
        tl_messages_updatePinnedMessage.peer = this.getInputPeer(id2);
        tl_messages_updatePinnedMessage.id = id;
        tl_messages_updatePinnedMessage.silent = (b ^ true);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_updatePinnedMessage, new _$$Lambda$MessagesController$zi8soJuISiGh3kdE6Q_S1wPr31c(this));
    }
    
    public void processChatInfo(final int n, final TLRPC.ChatFull chatFull, final ArrayList<TLRPC.User> list, final boolean b, final boolean b2, final boolean b3, final MessageObject messageObject) {
        if (b && n > 0 && !b3) {
            this.loadFullChat(n, 0, b2);
        }
        if (chatFull != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$aj9gT3RqpNCMmoGiSwlydWLbsP8(this, list, b, chatFull, b3, messageObject));
        }
    }
    
    public void processDialogsUpdate(final TLRPC.messages_Dialogs messages_Dialogs, final ArrayList<TLRPC.EncryptedChat> list) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$q64E9hWX3Xly3JADViQSa2zMJVo(this, messages_Dialogs));
    }
    
    public void processDialogsUpdateRead(final LongSparseArray<Integer> longSparseArray, final LongSparseArray<Integer> longSparseArray2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$_YsSBCaL_qbcH96s3TWAMj3YbZI(this, longSparseArray, longSparseArray2));
    }
    
    public void processLoadedBlockedUsers(final SparseIntArray sparseIntArray, final ArrayList<TLRPC.User> list, final boolean b) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$JPDY_4SRGvZvjZ1pDyebULwuKFA(this, list, b, sparseIntArray));
    }
    
    public void processLoadedChannelAdmins(final ArrayList<Integer> list, final int n, final boolean b) {
        Collections.sort((List<Comparable>)list);
        if (!b) {
            MessagesStorage.getInstance(this.currentAccount).putChannelAdmins(n, list);
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$qaVxMxZZgT13ZZ7VOgNBw4U51uM(this, n, list, b));
    }
    
    public void processLoadedDeleteTask(final int n, final ArrayList<Integer> list, final int n2) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$5rDlON84Ua92gPbhSWYw9Sv_2ig(this, list, n));
    }
    
    public void processLoadedDialogs(final TLRPC.messages_Dialogs messages_Dialogs, final ArrayList<TLRPC.EncryptedChat> list, final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2, final boolean b3) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$shj4tQAJ8i7NH8eiXBAGt48jDcs(this, n, n4, messages_Dialogs, b, n3, list, n2, b3, b2));
    }
    
    public void processLoadedMessages(final TLRPC.messages_Messages messages_Messages, final long lng, final int i, final int j, final int n, final boolean b, final int k, final int l, final int m, final int i2, final int i3, final int i4, final boolean b2, final boolean b3, final int i5, final boolean b4, final int n2) {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("processLoadedMessages size ");
            sb.append(messages_Messages.messages.size());
            sb.append(" in chat ");
            sb.append(lng);
            sb.append(" count ");
            sb.append(i);
            sb.append(" max_id ");
            sb.append(j);
            sb.append(" cache ");
            sb.append(b);
            sb.append(" guid ");
            sb.append(k);
            sb.append(" load_type ");
            sb.append(i4);
            sb.append(" last_message_id ");
            sb.append(m);
            sb.append(" isChannel ");
            sb.append(b2);
            sb.append(" index ");
            sb.append(i5);
            sb.append(" firstUnread ");
            sb.append(l);
            sb.append(" unread_count ");
            sb.append(i2);
            sb.append(" last_date ");
            sb.append(i3);
            sb.append(" queryFromServer ");
            sb.append(b4);
            FileLog.d(sb.toString());
        }
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$B3eeeWzbYDkKoJj9YxFxBPuwloE(this, messages_Messages, lng, b, i, i4, b4, l, j, n, k, m, b2, i5, i2, i3, n2, b3));
    }
    
    public void processLoadedUserPhotos(final TLRPC.photos_Photos photos_Photos, final int n, final int n2, final long n3, final boolean b, final int n4) {
        if (!b) {
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(photos_Photos.users, null, true, true);
            MessagesStorage.getInstance(this.currentAccount).putDialogPhotos(n, photos_Photos);
        }
        else if (photos_Photos == null || photos_Photos.photos.isEmpty()) {
            this.loadDialogPhotos(n, n2, n3, false, n4);
            return;
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$NAWdcL37DBZ4gWGx1x8HuDp86gI(this, photos_Photos, b, n, n2, n4));
    }
    
    protected void processNewChannelDifferenceParams(final int pts, final int pts_count, final int n) {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("processNewChannelDifferenceParams pts = ");
            sb.append(pts);
            sb.append(" pts_count = ");
            sb.append(pts_count);
            sb.append(" channeldId = ");
            sb.append(n);
            FileLog.d(sb.toString());
        }
        int n2;
        if ((n2 = this.channelsPts.get(n)) == 0) {
            if ((n2 = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(n)) == 0) {
                n2 = 1;
            }
            this.channelsPts.put(n, n2);
        }
        if (n2 + pts_count == pts) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("APPLY CHANNEL PTS");
            }
            this.channelsPts.put(n, pts);
            MessagesStorage.getInstance(this.currentAccount).saveChannelPts(n, pts);
        }
        else if (n2 != pts) {
            final long value = this.updatesStartWaitTimeChannels.get(n);
            if (!this.gettingDifferenceChannels.get(n) && value != 0L && Math.abs(System.currentTimeMillis() - value) > 1500L) {
                this.getChannelDifference(n);
            }
            else {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("ADD CHANNEL UPDATE TO QUEUE pts = ");
                    sb2.append(pts);
                    sb2.append(" pts_count = ");
                    sb2.append(pts_count);
                    FileLog.d(sb2.toString());
                }
                if (value == 0L) {
                    this.updatesStartWaitTimeChannels.put(n, System.currentTimeMillis());
                }
                final UserActionUpdatesPts e = new UserActionUpdatesPts();
                e.pts = pts;
                e.pts_count = pts_count;
                e.chat_id = n;
                ArrayList<UserActionUpdatesPts> list;
                if ((list = (ArrayList<UserActionUpdatesPts>)this.updatesQueueChannels.get(n)) == null) {
                    list = new ArrayList<UserActionUpdatesPts>();
                    this.updatesQueueChannels.put(n, (Object)list);
                }
                list.add(e);
            }
        }
    }
    
    protected void processNewDifferenceParams(final int n, final int n2, final int n3, final int pts_count) {
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("processNewDifferenceParams seq = ");
            sb.append(n);
            sb.append(" pts = ");
            sb.append(n2);
            sb.append(" date = ");
            sb.append(n3);
            sb.append(" pts_count = ");
            sb.append(pts_count);
            FileLog.d(sb.toString());
        }
        if (n2 != -1) {
            if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + pts_count == n2) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("APPLY PTS");
                }
                MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(n2);
                MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
            }
            else if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() != n2) {
                if (!this.gettingDifference && this.updatesStartWaitTimePts != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) > 1500L) {
                    this.getDifference();
                }
                else {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("ADD UPDATE TO QUEUE pts = ");
                        sb2.append(n2);
                        sb2.append(" pts_count = ");
                        sb2.append(pts_count);
                        FileLog.d(sb2.toString());
                    }
                    if (this.updatesStartWaitTimePts == 0L) {
                        this.updatesStartWaitTimePts = System.currentTimeMillis();
                    }
                    final UserActionUpdatesPts e = new UserActionUpdatesPts();
                    e.pts = n2;
                    e.pts_count = pts_count;
                    this.updatesQueuePts.add(e);
                }
            }
        }
        if (n != -1) {
            if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() + 1 == n) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("APPLY SEQ");
                }
                MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(n);
                if (n3 != -1) {
                    MessagesStorage.getInstance(this.currentAccount).setLastDateValue(n3);
                }
                MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
            }
            else if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() != n) {
                if (!this.gettingDifference && this.updatesStartWaitTimeSeq != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeSeq) > 1500L) {
                    this.getDifference();
                }
                else {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("ADD UPDATE TO QUEUE seq = ");
                        sb3.append(n);
                        FileLog.d(sb3.toString());
                    }
                    if (this.updatesStartWaitTimeSeq == 0L) {
                        this.updatesStartWaitTimeSeq = System.currentTimeMillis();
                    }
                    final UserActionUpdatesSeq e2 = new UserActionUpdatesSeq();
                    e2.seq = n;
                    this.updatesQueueSeq.add(e2);
                }
            }
        }
    }
    
    public boolean processUpdateArray(final ArrayList<TLRPC.Update> list, final ArrayList<TLRPC.User> list2, final ArrayList<TLRPC.Chat> list3, final boolean b) {
        if (list.isEmpty()) {
            if (list2 != null || list3 != null) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$ez4IR3lOLDGJ8ytrBmZ7ykTpLLU(this, list2, list3));
            }
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        ConcurrentHashMap<Integer, TLRPC.User> users;
        if (list2 != null) {
            users = new ConcurrentHashMap<Integer, TLRPC.User>();
            for (int size = list2.size(), i = 0; i < size; ++i) {
                final TLRPC.User value = list2.get(i);
                users.put(value.id, value);
            }
            final int n = 1;
        }
        else {
            users = this.users;
            final int n = 0;
        }
        int n;
        ConcurrentHashMap<Integer, TLRPC.Chat> chats;
        if (list3 != null) {
            chats = new ConcurrentHashMap<Integer, TLRPC.Chat>();
            for (int size2 = list3.size(), j = 0; j < size2; ++j) {
                final TLRPC.Chat value2 = list3.get(j);
                chats.put(value2.id, value2);
            }
        }
        else {
            chats = this.chats;
            n = 0;
        }
        if (b) {
            n = 0;
        }
        if (list2 != null || list3 != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$geLuQrjkrfEFbaaNVcTD1Sit87o(this, list2, list3));
        }
        final int size3 = list.size();
        ArrayList<Long> list4 = null;
        ArrayList<MessageObject> list5 = null;
        int n2 = 0;
        ArrayList<Integer> list6 = null;
        final LongSparseArray longSparseArray = null;
        int k = 0;
        SparseLongArray sparseLongArray = null;
        int n3 = 0;
        ArrayList<TLRPC.TL_message> list7 = null;
        LongSparseArray longSparseArray2 = null;
        SparseLongArray sparseLongArray2 = null;
        LongSparseArray longSparseArray3 = null;
        SparseArray sparseArray = null;
        SparseIntArray sparseIntArray = null;
        SparseArray sparseArray2 = null;
        SparseIntArray sparseIntArray2 = null;
        ArrayList<TLRPC.ChatParticipants> list8 = null;
        ArrayList<TLRPC.TL_updateChatParticipants> list9 = null;
        ArrayList<TLRPC.TL_updateEncryptedMessagesRead> list10 = null;
        ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap = chats;
        LongSparseArray longSparseArray4 = longSparseArray;
        final ConcurrentHashMap<Integer, TLRPC.User> concurrentHashMap2 = users;
    Label_6721_Outer:
        while (k < size3) {
            final TLRPC.Update obj = list.get(k);
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("process update ");
                sb.append(obj);
                FileLog.d(sb.toString());
            }
            final boolean b2 = obj instanceof TLRPC.TL_updateNewMessage;
            LongSparseArray longSparseArray5 = null;
            ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap6 = null;
            ArrayList<Integer> list17 = null;
            ArrayList<MessageObject> list56 = null;
            SparseLongArray sparseLongArray9 = null;
            Label_8228: {
                int n22;
                SparseLongArray sparseLongArray10;
                LongSparseArray longSparseArray10;
                int n24;
                ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap13;
                LongSparseArray longSparseArray11;
                ArrayList<MessageObject> list62;
                TLRPC.Message message4;
                TLRPC.Chat chatSync3;
                SparseArray sparseArray7;
                while (true) {
                    Label_6731: {
                        if (b2) {
                            break Label_6731;
                        }
                        ArrayList<MessageObject> list11 = list5;
                        if (obj instanceof TLRPC.TL_updateNewChannelMessage) {
                            break Label_6731;
                        }
                        ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap5 = null;
                        Label_0477: {
                            ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap3;
                            ArrayList<Long> list13;
                            if (obj instanceof TLRPC.TL_updateReadMessagesContents) {
                                final TLRPC.TL_updateReadMessagesContents tl_updateReadMessagesContents = (TLRPC.TL_updateReadMessagesContents)obj;
                                ArrayList<Long> list12;
                                if ((list12 = list4) == null) {
                                    list12 = new ArrayList<Long>();
                                }
                                for (int size4 = tl_updateReadMessagesContents.messages.size(), l = 0; l < size4; ++l) {
                                    list12.add((long)tl_updateReadMessagesContents.messages.get(l));
                                }
                                concurrentHashMap3 = concurrentHashMap;
                                list13 = list12;
                            }
                            else {
                                final long n4 = currentTimeMillis;
                                if (obj instanceof TLRPC.TL_updateChannelReadMessagesContents) {
                                    final TLRPC.TL_updateChannelReadMessagesContents tl_updateChannelReadMessagesContents = (TLRPC.TL_updateChannelReadMessagesContents)obj;
                                    ArrayList<Long> list14;
                                    if ((list14 = list4) == null) {
                                        list14 = new ArrayList<Long>();
                                    }
                                    final int size5 = tl_updateChannelReadMessagesContents.messages.size();
                                    int index = 0;
                                    final ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap4 = concurrentHashMap;
                                    while (true) {
                                        list13 = list14;
                                        concurrentHashMap3 = concurrentHashMap4;
                                        currentTimeMillis = n4;
                                        if (index >= size5) {
                                            break;
                                        }
                                        list14.add((long)tl_updateChannelReadMessagesContents.messages.get(index) | (long)tl_updateChannelReadMessagesContents.channel_id << 32);
                                        ++index;
                                    }
                                }
                                else {
                                    if (obj instanceof TLRPC.TL_updateReadHistoryInbox) {
                                        final TLRPC.TL_updateReadHistoryInbox tl_updateReadHistoryInbox = (TLRPC.TL_updateReadHistoryInbox)obj;
                                        SparseLongArray sparseLongArray3;
                                        if ((sparseLongArray3 = sparseLongArray) == null) {
                                            sparseLongArray3 = new SparseLongArray();
                                        }
                                        final TLRPC.Peer peer = tl_updateReadHistoryInbox.peer;
                                        final int chat_id = peer.chat_id;
                                        int user_id;
                                        if (chat_id != 0) {
                                            sparseLongArray3.put(-chat_id, tl_updateReadHistoryInbox.max_id);
                                            user_id = -tl_updateReadHistoryInbox.peer.chat_id;
                                        }
                                        else {
                                            sparseLongArray3.put(peer.user_id, tl_updateReadHistoryInbox.max_id);
                                            user_id = tl_updateReadHistoryInbox.peer.user_id;
                                        }
                                        final long n5 = user_id;
                                        Integer value3;
                                        if ((value3 = this.dialogs_read_inbox_max.get(n5)) == null) {
                                            value3 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, n5);
                                        }
                                        this.dialogs_read_inbox_max.put(n5, Math.max(value3, tl_updateReadHistoryInbox.max_id));
                                        sparseLongArray = sparseLongArray3;
                                        concurrentHashMap5 = concurrentHashMap;
                                        currentTimeMillis = n4;
                                        break Label_0477;
                                    }
                                    SparseLongArray sparseLongArray4;
                                    if (obj instanceof TLRPC.TL_updateReadHistoryOutbox) {
                                        final TLRPC.TL_updateReadHistoryOutbox tl_updateReadHistoryOutbox = (TLRPC.TL_updateReadHistoryOutbox)obj;
                                        if ((sparseLongArray4 = sparseLongArray2) == null) {
                                            sparseLongArray4 = new SparseLongArray();
                                        }
                                        final TLRPC.Peer peer2 = tl_updateReadHistoryOutbox.peer;
                                        final int chat_id2 = peer2.chat_id;
                                        int user_id2;
                                        if (chat_id2 != 0) {
                                            sparseLongArray4.put(-chat_id2, tl_updateReadHistoryOutbox.max_id);
                                            user_id2 = -tl_updateReadHistoryOutbox.peer.chat_id;
                                        }
                                        else {
                                            sparseLongArray4.put(peer2.user_id, tl_updateReadHistoryOutbox.max_id);
                                            user_id2 = tl_updateReadHistoryOutbox.peer.user_id;
                                        }
                                        final long n6 = user_id2;
                                        Integer value4;
                                        if ((value4 = this.dialogs_read_outbox_max.get(n6)) == null) {
                                            value4 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, n6);
                                        }
                                        this.dialogs_read_outbox_max.put(n6, Math.max(value4, tl_updateReadHistoryOutbox.max_id));
                                    }
                                    else {
                                        final SparseLongArray sparseLongArray5 = sparseLongArray2;
                                        if (obj instanceof TLRPC.TL_updateDeleteMessages) {
                                            final TLRPC.TL_updateDeleteMessages tl_updateDeleteMessages = (TLRPC.TL_updateDeleteMessages)obj;
                                            if ((sparseArray2 = sparseArray2) == null) {
                                                sparseArray2 = new SparseArray();
                                            }
                                            ArrayList list15;
                                            if ((list15 = (ArrayList)sparseArray2.get(0)) == null) {
                                                list15 = new ArrayList();
                                                sparseArray2.put(0, (Object)list15);
                                            }
                                            list15.addAll(tl_updateDeleteMessages.messages);
                                        }
                                        else {
                                            final boolean b3 = obj instanceof TLRPC.TL_updateUserTyping;
                                            int n7 = n2;
                                            if (b3 || obj instanceof TLRPC.TL_updateChatUserTyping) {
                                                int user_id3;
                                                TLRPC.SendMessageAction sendMessageAction;
                                                int n8;
                                                if (b3) {
                                                    final TLRPC.TL_updateUserTyping tl_updateUserTyping = (TLRPC.TL_updateUserTyping)obj;
                                                    user_id3 = tl_updateUserTyping.user_id;
                                                    sendMessageAction = tl_updateUserTyping.action;
                                                    n8 = 0;
                                                }
                                                else {
                                                    final TLRPC.TL_updateChatUserTyping tl_updateChatUserTyping = (TLRPC.TL_updateChatUserTyping)obj;
                                                    final int chat_id3 = tl_updateChatUserTyping.chat_id;
                                                    final int user_id4 = tl_updateChatUserTyping.user_id;
                                                    sendMessageAction = tl_updateChatUserTyping.action;
                                                    n8 = chat_id3;
                                                    user_id3 = user_id4;
                                                }
                                                if (user_id3 != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                    long m;
                                                    if ((m = -n8) == 0L) {
                                                        m = user_id3;
                                                    }
                                                    final ArrayList<PrintingUser> list16 = this.printingUsers.get(m);
                                                    Label_6677: {
                                                        if (!(sendMessageAction instanceof TLRPC.TL_sendMessageCancelAction)) {
                                                            ArrayList<PrintingUser> value5;
                                                            if ((value5 = list16) == null) {
                                                                value5 = new ArrayList<PrintingUser>();
                                                                this.printingUsers.put(m, value5);
                                                            }
                                                            while (true) {
                                                                for (final PrintingUser printingUser : value5) {
                                                                    if (printingUser.userId == user_id3) {
                                                                        printingUser.lastTime = n4;
                                                                        if (printingUser.action.getClass() != sendMessageAction.getClass()) {
                                                                            n3 = 1;
                                                                        }
                                                                        printingUser.action = sendMessageAction;
                                                                        final boolean b4 = true;
                                                                        if (!b4) {
                                                                            final PrintingUser e = new PrintingUser();
                                                                            e.userId = user_id3;
                                                                            e.lastTime = currentTimeMillis;
                                                                            e.action = sendMessageAction;
                                                                            value5.add(e);
                                                                            n3 = 1;
                                                                        }
                                                                        break Label_6677;
                                                                    }
                                                                }
                                                                final boolean b4 = false;
                                                                continue Label_6721_Outer;
                                                            }
                                                        }
                                                        int n9 = n3;
                                                        if (list16 != null) {
                                                            final int size6 = list16.size();
                                                            int n10 = 0;
                                                            int n11;
                                                            while (true) {
                                                                n11 = n3;
                                                                if (n10 >= size6) {
                                                                    break;
                                                                }
                                                                if (list16.get(n10).userId == user_id3) {
                                                                    list16.remove(n10);
                                                                    n11 = 1;
                                                                    break;
                                                                }
                                                                ++n10;
                                                            }
                                                            n9 = n11;
                                                            if (list16.isEmpty()) {
                                                                this.printingUsers.remove(m);
                                                                n9 = n11;
                                                            }
                                                        }
                                                        n3 = n9;
                                                    }
                                                    this.onlinePrivacy.put(user_id3, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                                }
                                                longSparseArray5 = longSparseArray4;
                                                n2 = n7;
                                                concurrentHashMap6 = concurrentHashMap;
                                                list17 = list6;
                                                break Label_6721;
                                            }
                                            if (obj instanceof TLRPC.TL_updateChatParticipants) {
                                                final TLRPC.TL_updateChatParticipants tl_updateChatParticipants = (TLRPC.TL_updateChatParticipants)obj;
                                                n2 = (n7 | 0x20);
                                                ArrayList<TLRPC.ChatParticipants> list18;
                                                if ((list18 = list8) == null) {
                                                    list18 = new ArrayList<TLRPC.ChatParticipants>();
                                                }
                                                list18.add(tl_updateChatParticipants.participants);
                                                list8 = list18;
                                            }
                                            else if (obj instanceof TLRPC.TL_updateUserStatus) {
                                                n2 = (n7 | 0x4);
                                                ArrayList<TLRPC.TL_updateChatParticipants> list19;
                                                if ((list19 = list9) == null) {
                                                    list19 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                }
                                                list9 = list19;
                                                list9.add((TLRPC.TL_updateChatParticipants)obj);
                                            }
                                            else if (obj instanceof TLRPC.TL_updateUserName) {
                                                n2 = (n7 | 0x1);
                                                ArrayList<TLRPC.TL_updateChatParticipants> list20;
                                                if ((list20 = list9) == null) {
                                                    list20 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                }
                                                list9 = list20;
                                                list9.add((TLRPC.TL_updateChatParticipants)obj);
                                            }
                                            else if (obj instanceof TLRPC.TL_updateUserPhoto) {
                                                final TLRPC.TL_updateUserPhoto tl_updateUserPhoto = (TLRPC.TL_updateUserPhoto)obj;
                                                n2 = (n7 | 0x2);
                                                MessagesStorage.getInstance(this.currentAccount).clearUserPhotos(tl_updateUserPhoto.user_id);
                                                ArrayList<TLRPC.TL_updateChatParticipants> list21;
                                                if ((list21 = list9) == null) {
                                                    list21 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                }
                                                list9 = list21;
                                                list9.add((TLRPC.TL_updateChatParticipants)obj);
                                            }
                                            else if (obj instanceof TLRPC.TL_updateUserPhone) {
                                                n2 = (n7 | 0x400);
                                                ArrayList<TLRPC.TL_updateChatParticipants> list22;
                                                if ((list22 = list9) == null) {
                                                    list22 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                }
                                                list9 = list22;
                                                list9.add((TLRPC.TL_updateChatParticipants)obj);
                                            }
                                            else {
                                                if (!(obj instanceof TLRPC.TL_updateContactLink)) {
                                                    SparseLongArray sparseLongArray6 = null;
                                                    ArrayList<MessageObject> list58 = null;
                                                    Label_1934: {
                                                        int n12;
                                                        ArrayList<Long> list30;
                                                        if (obj instanceof TLRPC.TL_updateNewEncryptedMessage) {
                                                            final SecretChatHelper instance = SecretChatHelper.getInstance(this.currentAccount);
                                                            final TLRPC.TL_updateNewEncryptedMessage tl_updateNewEncryptedMessage = (TLRPC.TL_updateNewEncryptedMessage)obj;
                                                            final ArrayList<TLRPC.Message> decryptMessage = instance.decryptMessage(tl_updateNewEncryptedMessage.message);
                                                            ArrayList<Integer> list27;
                                                            int n13;
                                                            ArrayList<Long> list28;
                                                            if (decryptMessage != null && !decryptMessage.isEmpty()) {
                                                                final long l2 = (long)tl_updateNewEncryptedMessage.message.chat_id << 32;
                                                                LongSparseArray longSparseArray6;
                                                                if ((longSparseArray6 = longSparseArray4) == null) {
                                                                    longSparseArray6 = new LongSparseArray();
                                                                }
                                                                ArrayList<MessageObject> list23;
                                                                if ((list23 = (ArrayList<MessageObject>)longSparseArray6.get(l2)) == null) {
                                                                    list23 = new ArrayList<MessageObject>();
                                                                    longSparseArray6.put(l2, (Object)list23);
                                                                }
                                                                ArrayList<MessageObject> list25;
                                                                for (int size7 = decryptMessage.size(), index2 = 0; index2 < size7; ++index2, list11 = list25) {
                                                                    final TLRPC.Message e2 = decryptMessage.get(index2);
                                                                    ImageLoader.saveMessageThumbs(e2);
                                                                    ArrayList<TLRPC.TL_message> list24;
                                                                    if ((list24 = list7) == null) {
                                                                        list24 = new ArrayList<TLRPC.TL_message>();
                                                                    }
                                                                    list24.add((TLRPC.TL_message)e2);
                                                                    list7 = list24;
                                                                    final MessageObject messageObject = new MessageObject(this.currentAccount, e2, concurrentHashMap2, concurrentHashMap, this.createdDialogIds.contains(l2));
                                                                    list23.add(messageObject);
                                                                    if (list11 == null) {
                                                                        list25 = new ArrayList<MessageObject>();
                                                                    }
                                                                    else {
                                                                        list25 = list11;
                                                                    }
                                                                    list25.add(messageObject);
                                                                }
                                                                final ArrayList<Long> list26 = list4;
                                                                n12 = k;
                                                                longSparseArray4 = longSparseArray6;
                                                                list27 = list6;
                                                                n13 = n7;
                                                                list28 = list26;
                                                            }
                                                            else {
                                                                final ArrayList<Long> list29 = list4;
                                                                list27 = list6;
                                                                n12 = k;
                                                                list28 = list29;
                                                                n13 = n7;
                                                            }
                                                            sparseLongArray6 = sparseLongArray5;
                                                            list30 = list28;
                                                            n7 = n13;
                                                            currentTimeMillis = n4;
                                                            list6 = list27;
                                                        }
                                                        else {
                                                            list30 = list4;
                                                            if (!(obj instanceof TLRPC.TL_updateEncryptedChatTyping)) {
                                                                sparseLongArray6 = sparseLongArray5;
                                                                ArrayList<TLRPC.TL_updateEncryptedMessagesRead> list33 = null;
                                                                SparseIntArray sparseIntArray5 = null;
                                                                Label_2307: {
                                                                    if (!(obj instanceof TLRPC.TL_updateEncryptedMessagesRead)) {
                                                                        final SparseIntArray sparseIntArray3 = sparseIntArray;
                                                                        final ArrayList<TLRPC.TL_updateEncryptedMessagesRead> list31 = list10;
                                                                        ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap7 = null;
                                                                        LongSparseArray longSparseArray7 = null;
                                                                        Label_6246: {
                                                                            Label_2399: {
                                                                                if (obj instanceof TLRPC.TL_updateChatParticipantAdd) {
                                                                                    final TLRPC.TL_updateChatParticipantAdd tl_updateChatParticipantAdd = (TLRPC.TL_updateChatParticipantAdd)obj;
                                                                                    MessagesStorage.getInstance(this.currentAccount).updateChatInfo(tl_updateChatParticipantAdd.chat_id, tl_updateChatParticipantAdd.user_id, 0, tl_updateChatParticipantAdd.inviter_id, tl_updateChatParticipantAdd.version);
                                                                                }
                                                                                else if (obj instanceof TLRPC.TL_updateChatParticipantDelete) {
                                                                                    final TLRPC.TL_updateChatParticipantDelete tl_updateChatParticipantDelete = (TLRPC.TL_updateChatParticipantDelete)obj;
                                                                                    MessagesStorage.getInstance(this.currentAccount).updateChatInfo(tl_updateChatParticipantDelete.chat_id, tl_updateChatParticipantDelete.user_id, 1, 0, tl_updateChatParticipantDelete.version);
                                                                                }
                                                                                else {
                                                                                    if (obj instanceof TLRPC.TL_updateDcOptions || obj instanceof TLRPC.TL_updateConfig) {
                                                                                        ConnectionsManager.getInstance(this.currentAccount).updateDcSettings();
                                                                                        concurrentHashMap7 = concurrentHashMap;
                                                                                        longSparseArray7 = longSparseArray2;
                                                                                        break Label_6246;
                                                                                    }
                                                                                    if (obj instanceof TLRPC.TL_updateEncryption) {
                                                                                        SecretChatHelper.getInstance(this.currentAccount).processUpdateEncryption((TLRPC.TL_updateEncryption)obj, concurrentHashMap2);
                                                                                    }
                                                                                    else if (obj instanceof TLRPC.TL_updateUserBlocked) {
                                                                                        final TLRPC.TL_updateUserBlocked tl_updateUserBlocked = (TLRPC.TL_updateUserBlocked)obj;
                                                                                        if (tl_updateUserBlocked.blocked) {
                                                                                            final SparseIntArray sparseIntArray4 = new SparseIntArray();
                                                                                            sparseIntArray4.put(tl_updateUserBlocked.user_id, 1);
                                                                                            MessagesStorage.getInstance(this.currentAccount).putBlockedUsers(sparseIntArray4, false);
                                                                                        }
                                                                                        else {
                                                                                            MessagesStorage.getInstance(this.currentAccount).deleteBlockedUser(tl_updateUserBlocked.user_id);
                                                                                        }
                                                                                        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$0Px26KfS0c0TW2u9ZjYkQVOvQKA(this, tl_updateUserBlocked));
                                                                                    }
                                                                                    else {
                                                                                        if (obj instanceof TLRPC.TL_updateNotifySettings) {
                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list32;
                                                                                            if ((list32 = list9) == null) {
                                                                                                list32 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                            }
                                                                                            list32.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                            list9 = list32;
                                                                                            list33 = list31;
                                                                                            sparseIntArray5 = sparseIntArray3;
                                                                                            break Label_2307;
                                                                                        }
                                                                                        if (obj instanceof TLRPC.TL_updateServiceNotification) {
                                                                                            final TLRPC.TL_updateServiceNotification tl_updateServiceNotification = (TLRPC.TL_updateServiceNotification)obj;
                                                                                            if (tl_updateServiceNotification.popup) {
                                                                                                final String message = tl_updateServiceNotification.message;
                                                                                                if (message != null && message.length() > 0) {
                                                                                                    AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$jWG8NvZquHH3NCs4zu__Fuu7gvw(this, tl_updateServiceNotification));
                                                                                                }
                                                                                            }
                                                                                            if ((tl_updateServiceNotification.flags & 0x2) != 0x0) {
                                                                                                final TLRPC.TL_message e3 = new TLRPC.TL_message();
                                                                                                final int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                                                                                                e3.id = newMessageId;
                                                                                                e3.local_id = newMessageId;
                                                                                                UserConfig.getInstance(this.currentAccount).saveConfig(false);
                                                                                                e3.unread = true;
                                                                                                e3.flags = 256;
                                                                                                final int inbox_date = tl_updateServiceNotification.inbox_date;
                                                                                                if (inbox_date != 0) {
                                                                                                    e3.date = inbox_date;
                                                                                                }
                                                                                                else {
                                                                                                    e3.date = (int)(System.currentTimeMillis() / 1000L);
                                                                                                }
                                                                                                e3.from_id = 777000;
                                                                                                e3.to_id = new TLRPC.TL_peerUser();
                                                                                                e3.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                                e3.dialog_id = 777000L;
                                                                                                final TLRPC.MessageMedia media = tl_updateServiceNotification.media;
                                                                                                if (media != null) {
                                                                                                    e3.media = media;
                                                                                                    e3.flags |= 0x200;
                                                                                                }
                                                                                                e3.message = tl_updateServiceNotification.message;
                                                                                                final ArrayList<TLRPC.MessageEntity> entities = tl_updateServiceNotification.entities;
                                                                                                if (entities != null) {
                                                                                                    e3.entities = entities;
                                                                                                }
                                                                                                ArrayList<TLRPC.TL_message> list34;
                                                                                                if ((list34 = list7) == null) {
                                                                                                    list34 = new ArrayList<TLRPC.TL_message>();
                                                                                                }
                                                                                                list34.add(e3);
                                                                                                final int currentAccount = this.currentAccount;
                                                                                                final boolean contains = this.createdDialogIds.contains(e3.dialog_id);
                                                                                                list7 = list34;
                                                                                                final MessageObject messageObject2 = new MessageObject(currentAccount, e3, concurrentHashMap2, concurrentHashMap, contains);
                                                                                                if (longSparseArray4 == null) {
                                                                                                    longSparseArray4 = new LongSparseArray();
                                                                                                }
                                                                                                ArrayList<MessageObject> list35;
                                                                                                if ((list35 = (ArrayList<MessageObject>)longSparseArray4.get(e3.dialog_id)) == null) {
                                                                                                    list35 = new ArrayList<MessageObject>();
                                                                                                    longSparseArray4.put(e3.dialog_id, (Object)list35);
                                                                                                }
                                                                                                list35.add(messageObject2);
                                                                                                ArrayList<MessageObject> list36;
                                                                                                if (list11 == null) {
                                                                                                    list36 = new ArrayList<MessageObject>();
                                                                                                }
                                                                                                else {
                                                                                                    list36 = list11;
                                                                                                }
                                                                                                list36.add(messageObject2);
                                                                                                list11 = list36;
                                                                                            }
                                                                                        }
                                                                                        else if (obj instanceof TLRPC.TL_updateDialogPinned) {
                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list37;
                                                                                            if ((list37 = list9) == null) {
                                                                                                list37 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                            }
                                                                                            list9 = list37;
                                                                                            list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                        }
                                                                                        else if (obj instanceof TLRPC.TL_updatePinnedDialogs) {
                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list38;
                                                                                            if ((list38 = list9) == null) {
                                                                                                list38 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                            }
                                                                                            list9 = list38;
                                                                                            list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                        }
                                                                                        else if (obj instanceof TLRPC.TL_updateFolderPeers) {
                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list39;
                                                                                            if ((list39 = list9) == null) {
                                                                                                list39 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                            }
                                                                                            list9 = list39;
                                                                                            list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                            MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId(((TLRPC.TL_updateFolderPeers)obj).folder_peers, null, 0L, 0);
                                                                                        }
                                                                                        else if (obj instanceof TLRPC.TL_updatePrivacy) {
                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list40;
                                                                                            if ((list40 = list9) == null) {
                                                                                                list40 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                            }
                                                                                            list9 = list40;
                                                                                            list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                        }
                                                                                        else {
                                                                                            LongSparseArray longSparseArray8;
                                                                                            if (obj instanceof TLRPC.TL_updateWebPage) {
                                                                                                final TLRPC.TL_updateWebPage tl_updateWebPage = (TLRPC.TL_updateWebPage)obj;
                                                                                                if ((longSparseArray8 = longSparseArray3) == null) {
                                                                                                    longSparseArray8 = new LongSparseArray();
                                                                                                }
                                                                                                final TLRPC.WebPage webpage = tl_updateWebPage.webpage;
                                                                                                longSparseArray8.put(webpage.id, (Object)webpage);
                                                                                            }
                                                                                            else {
                                                                                                final LongSparseArray longSparseArray9 = longSparseArray3;
                                                                                                if (!(obj instanceof TLRPC.TL_updateChannelWebPage)) {
                                                                                                    Label_3704: {
                                                                                                        if (!(obj instanceof TLRPC.TL_updateChannelTooLong)) {
                                                                                                            Label_3862: {
                                                                                                                if (obj instanceof TLRPC.TL_updateReadChannelInbox) {
                                                                                                                    final TLRPC.TL_updateReadChannelInbox tl_updateReadChannelInbox = (TLRPC.TL_updateReadChannelInbox)obj;
                                                                                                                    final long n14 = tl_updateReadChannelInbox.max_id;
                                                                                                                    final int channel_id = tl_updateReadChannelInbox.channel_id;
                                                                                                                    final long n15 = channel_id;
                                                                                                                    final long n16 = -channel_id;
                                                                                                                    SparseLongArray sparseLongArray7;
                                                                                                                    if ((sparseLongArray7 = sparseLongArray) == null) {
                                                                                                                        sparseLongArray7 = new SparseLongArray();
                                                                                                                    }
                                                                                                                    sparseLongArray7.put(-tl_updateReadChannelInbox.channel_id, n14 | n15 << 32);
                                                                                                                    Integer value6;
                                                                                                                    if ((value6 = this.dialogs_read_inbox_max.get(n16)) == null) {
                                                                                                                        value6 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, n16);
                                                                                                                    }
                                                                                                                    this.dialogs_read_inbox_max.put(n16, Math.max(value6, tl_updateReadChannelInbox.max_id));
                                                                                                                    sparseLongArray = sparseLongArray7;
                                                                                                                }
                                                                                                                else if (obj instanceof TLRPC.TL_updateReadChannelOutbox) {
                                                                                                                    final TLRPC.TL_updateReadChannelOutbox tl_updateReadChannelOutbox = (TLRPC.TL_updateReadChannelOutbox)obj;
                                                                                                                    final long n17 = tl_updateReadChannelOutbox.max_id;
                                                                                                                    final int channel_id2 = tl_updateReadChannelOutbox.channel_id;
                                                                                                                    final long n18 = channel_id2;
                                                                                                                    final long n19 = -channel_id2;
                                                                                                                    SparseLongArray sparseLongArray8;
                                                                                                                    if (sparseLongArray6 == null) {
                                                                                                                        sparseLongArray8 = new SparseLongArray();
                                                                                                                    }
                                                                                                                    else {
                                                                                                                        sparseLongArray8 = sparseLongArray6;
                                                                                                                    }
                                                                                                                    sparseLongArray8.put(-tl_updateReadChannelOutbox.channel_id, n18 << 32 | n17);
                                                                                                                    Integer value7;
                                                                                                                    if ((value7 = this.dialogs_read_outbox_max.get(n19)) == null) {
                                                                                                                        value7 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, n19);
                                                                                                                    }
                                                                                                                    this.dialogs_read_outbox_max.put(n19, Math.max(value7, tl_updateReadChannelOutbox.max_id));
                                                                                                                    sparseLongArray6 = sparseLongArray8;
                                                                                                                }
                                                                                                                else if (obj instanceof TLRPC.TL_updateDeleteChannelMessages) {
                                                                                                                    final TLRPC.TL_updateDeleteChannelMessages tl_updateDeleteChannelMessages = (TLRPC.TL_updateDeleteChannelMessages)obj;
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                        final StringBuilder sb2 = new StringBuilder();
                                                                                                                        sb2.append(obj);
                                                                                                                        sb2.append(" channelId = ");
                                                                                                                        sb2.append(tl_updateDeleteChannelMessages.channel_id);
                                                                                                                        FileLog.d(sb2.toString());
                                                                                                                    }
                                                                                                                    SparseArray sparseArray3;
                                                                                                                    if (sparseArray2 == null) {
                                                                                                                        sparseArray3 = new SparseArray();
                                                                                                                    }
                                                                                                                    else {
                                                                                                                        sparseArray3 = sparseArray2;
                                                                                                                    }
                                                                                                                    ArrayList list41;
                                                                                                                    if ((list41 = (ArrayList)sparseArray3.get(tl_updateDeleteChannelMessages.channel_id)) == null) {
                                                                                                                        list41 = new ArrayList();
                                                                                                                        sparseArray3.put(tl_updateDeleteChannelMessages.channel_id, (Object)list41);
                                                                                                                    }
                                                                                                                    list41.addAll(tl_updateDeleteChannelMessages.messages);
                                                                                                                    sparseArray2 = sparseArray3;
                                                                                                                }
                                                                                                                else if (obj instanceof TLRPC.TL_updateChannel) {
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                        final TLRPC.TL_updateChannel tl_updateChannel = (TLRPC.TL_updateChannel)obj;
                                                                                                                        final StringBuilder sb3 = new StringBuilder();
                                                                                                                        sb3.append(obj);
                                                                                                                        sb3.append(" channelId = ");
                                                                                                                        sb3.append(tl_updateChannel.channel_id);
                                                                                                                        FileLog.d(sb3.toString());
                                                                                                                    }
                                                                                                                    ArrayList<TLRPC.TL_updateChatParticipants> list42;
                                                                                                                    if ((list42 = list9) == null) {
                                                                                                                        list42 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                    }
                                                                                                                    list42.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                    list9 = list42;
                                                                                                                }
                                                                                                                else {
                                                                                                                    SparseArray sparseArray5 = null;
                                                                                                                    Label_4450: {
                                                                                                                        if (!(obj instanceof TLRPC.TL_updateChannelMessageViews)) {
                                                                                                                            final SparseArray sparseArray4 = sparseArray;
                                                                                                                            Label_4507: {
                                                                                                                                if (!(obj instanceof TLRPC.TL_updateChatParticipantAdmin)) {
                                                                                                                                    if (obj instanceof TLRPC.TL_updateChatDefaultBannedRights) {
                                                                                                                                        final TLRPC.TL_updateChatDefaultBannedRights tl_updateChatDefaultBannedRights = (TLRPC.TL_updateChatDefaultBannedRights)obj;
                                                                                                                                        final TLRPC.Peer peer3 = tl_updateChatDefaultBannedRights.peer;
                                                                                                                                        int n20 = peer3.channel_id;
                                                                                                                                        if (n20 == 0) {
                                                                                                                                            n20 = peer3.chat_id;
                                                                                                                                        }
                                                                                                                                        MessagesStorage.getInstance(this.currentAccount).updateChatDefaultBannedRights(n20, tl_updateChatDefaultBannedRights.default_banned_rights, tl_updateChatDefaultBannedRights.version);
                                                                                                                                        ArrayList<TLRPC.TL_updateChatParticipants> list43 = list9;
                                                                                                                                        if (list9 == null) {
                                                                                                                                            list43 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                        }
                                                                                                                                        list9 = list43;
                                                                                                                                        list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                    }
                                                                                                                                    else if (obj instanceof TLRPC.TL_updateStickerSets) {
                                                                                                                                        ArrayList<TLRPC.TL_updateChatParticipants> list44;
                                                                                                                                        if ((list44 = list9) == null) {
                                                                                                                                            list44 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                        }
                                                                                                                                        list9 = list44;
                                                                                                                                        list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                    }
                                                                                                                                    else if (obj instanceof TLRPC.TL_updateStickerSetsOrder) {
                                                                                                                                        ArrayList<TLRPC.TL_updateChatParticipants> list45;
                                                                                                                                        if ((list45 = list9) == null) {
                                                                                                                                            list45 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                        }
                                                                                                                                        list9 = list45;
                                                                                                                                        list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                    }
                                                                                                                                    else if (obj instanceof TLRPC.TL_updateNewStickerSet) {
                                                                                                                                        ArrayList<TLRPC.TL_updateChatParticipants> list46;
                                                                                                                                        if ((list46 = list9) == null) {
                                                                                                                                            list46 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                        }
                                                                                                                                        list9 = list46;
                                                                                                                                        list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                    }
                                                                                                                                    else if (obj instanceof TLRPC.TL_updateDraftMessage) {
                                                                                                                                        ArrayList<TLRPC.TL_updateChatParticipants> list47;
                                                                                                                                        if ((list47 = list9) == null) {
                                                                                                                                            list47 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                        }
                                                                                                                                        list9 = list47;
                                                                                                                                        list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                    }
                                                                                                                                    else if (obj instanceof TLRPC.TL_updateSavedGifs) {
                                                                                                                                        ArrayList<TLRPC.TL_updateChatParticipants> list48;
                                                                                                                                        if ((list48 = list9) == null) {
                                                                                                                                            list48 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                        }
                                                                                                                                        list9 = list48;
                                                                                                                                        list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                    }
                                                                                                                                    else {
                                                                                                                                        final boolean b5 = obj instanceof TLRPC.TL_updateEditChannelMessage;
                                                                                                                                        if (b5 || obj instanceof TLRPC.TL_updateEditMessage) {
                                                                                                                                            final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                                                                            TLRPC.Message message3;
                                                                                                                                            if (b5) {
                                                                                                                                                final TLRPC.Message message2 = ((TLRPC.TL_updateEditChannelMessage)obj).message;
                                                                                                                                                TLRPC.Chat chat;
                                                                                                                                                if ((chat = concurrentHashMap.get(message2.to_id.channel_id)) == null) {
                                                                                                                                                    chat = this.getChat(message2.to_id.channel_id);
                                                                                                                                                }
                                                                                                                                                TLRPC.Chat chatSync;
                                                                                                                                                if ((chatSync = chat) == null) {
                                                                                                                                                    chatSync = MessagesStorage.getInstance(this.currentAccount).getChatSync(message2.to_id.channel_id);
                                                                                                                                                    this.putChat(chatSync, true);
                                                                                                                                                }
                                                                                                                                                if (chatSync != null && chatSync.megagroup) {
                                                                                                                                                    message2.flags |= Integer.MIN_VALUE;
                                                                                                                                                }
                                                                                                                                                message3 = message2;
                                                                                                                                            }
                                                                                                                                            else {
                                                                                                                                                message3 = ((TLRPC.TL_updateEditMessage)obj).message;
                                                                                                                                                if (message3.dialog_id == clientUserId) {
                                                                                                                                                    message3.unread = false;
                                                                                                                                                    message3.media_unread = false;
                                                                                                                                                    message3.out = true;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            if (!message3.out && message3.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                                                                                                message3.out = true;
                                                                                                                                            }
                                                                                                                                            if (!b) {
                                                                                                                                                for (int size8 = message3.entities.size(), index3 = 0; index3 < size8; ++index3) {
                                                                                                                                                    final TLRPC.MessageEntity messageEntity = message3.entities.get(index3);
                                                                                                                                                    if (messageEntity instanceof TLRPC.TL_messageEntityMentionName) {
                                                                                                                                                        final int user_id5 = ((TLRPC.TL_messageEntityMentionName)messageEntity).user_id;
                                                                                                                                                        final TLRPC.User user = concurrentHashMap2.get(user_id5);
                                                                                                                                                        TLRPC.User user2 = null;
                                                                                                                                                        Label_5753: {
                                                                                                                                                            if (user != null) {
                                                                                                                                                                user2 = user;
                                                                                                                                                                if (!user.min) {
                                                                                                                                                                    break Label_5753;
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            user2 = this.getUser(user_id5);
                                                                                                                                                        }
                                                                                                                                                        TLRPC.User user3 = null;
                                                                                                                                                        Label_5815: {
                                                                                                                                                            if (user2 != null) {
                                                                                                                                                                user3 = user2;
                                                                                                                                                                if (!user2.min) {
                                                                                                                                                                    break Label_5815;
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            TLRPC.User userSync = MessagesStorage.getInstance(this.currentAccount).getUserSync(user_id5);
                                                                                                                                                            if (userSync != null && userSync.min) {
                                                                                                                                                                userSync = null;
                                                                                                                                                            }
                                                                                                                                                            this.putUser(userSync, true);
                                                                                                                                                            user3 = userSync;
                                                                                                                                                        }
                                                                                                                                                        if (user3 == null) {
                                                                                                                                                            return false;
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            final TLRPC.Peer to_id = message3.to_id;
                                                                                                                                            final int chat_id4 = to_id.chat_id;
                                                                                                                                            if (chat_id4 != 0) {
                                                                                                                                                message3.dialog_id = -chat_id4;
                                                                                                                                            }
                                                                                                                                            else {
                                                                                                                                                final int channel_id3 = to_id.channel_id;
                                                                                                                                                if (channel_id3 != 0) {
                                                                                                                                                    message3.dialog_id = -channel_id3;
                                                                                                                                                }
                                                                                                                                                else {
                                                                                                                                                    if (to_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                                                                                                        message3.to_id.user_id = message3.from_id;
                                                                                                                                                    }
                                                                                                                                                    message3.dialog_id = message3.to_id.user_id;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            ConcurrentHashMap<Long, Integer> concurrentHashMap8;
                                                                                                                                            if (message3.out) {
                                                                                                                                                concurrentHashMap8 = this.dialogs_read_outbox_max;
                                                                                                                                            }
                                                                                                                                            else {
                                                                                                                                                concurrentHashMap8 = this.dialogs_read_inbox_max;
                                                                                                                                            }
                                                                                                                                            Integer value8;
                                                                                                                                            if ((value8 = concurrentHashMap8.get(message3.dialog_id)) == null) {
                                                                                                                                                value8 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message3.out, message3.dialog_id);
                                                                                                                                                concurrentHashMap8.put(message3.dialog_id, value8);
                                                                                                                                            }
                                                                                                                                            message3.unread = (value8 < message3.id);
                                                                                                                                            if (message3.dialog_id == clientUserId) {
                                                                                                                                                message3.out = true;
                                                                                                                                                message3.unread = false;
                                                                                                                                                message3.media_unread = false;
                                                                                                                                            }
                                                                                                                                            if (message3.out && message3.message == null) {
                                                                                                                                                message3.message = "";
                                                                                                                                                message3.attachPath = "";
                                                                                                                                            }
                                                                                                                                            ImageLoader.saveMessageThumbs(message3);
                                                                                                                                            final int currentAccount2 = this.currentAccount;
                                                                                                                                            final boolean contains2 = this.createdDialogIds.contains(message3.dialog_id);
                                                                                                                                            concurrentHashMap7 = concurrentHashMap;
                                                                                                                                            final MessageObject e4 = new MessageObject(currentAccount2, message3, concurrentHashMap2, concurrentHashMap7, contains2);
                                                                                                                                            if ((longSparseArray7 = longSparseArray2) == null) {
                                                                                                                                                longSparseArray7 = new LongSparseArray();
                                                                                                                                            }
                                                                                                                                            ArrayList<MessageObject> list49;
                                                                                                                                            if ((list49 = (ArrayList<MessageObject>)longSparseArray7.get(message3.dialog_id)) == null) {
                                                                                                                                                list49 = new ArrayList<MessageObject>();
                                                                                                                                                longSparseArray7.put(message3.dialog_id, (Object)list49);
                                                                                                                                            }
                                                                                                                                            list49.add(e4);
                                                                                                                                            break Label_6246;
                                                                                                                                        }
                                                                                                                                        if (obj instanceof TLRPC.TL_updateChannelPinnedMessage) {
                                                                                                                                            final TLRPC.TL_updateChannelPinnedMessage tl_updateChannelPinnedMessage = (TLRPC.TL_updateChannelPinnedMessage)obj;
                                                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                final StringBuilder sb4 = new StringBuilder();
                                                                                                                                                sb4.append(obj);
                                                                                                                                                sb4.append(" channelId = ");
                                                                                                                                                sb4.append(tl_updateChannelPinnedMessage.channel_id);
                                                                                                                                                FileLog.d(sb4.toString());
                                                                                                                                            }
                                                                                                                                            MessagesStorage.getInstance(this.currentAccount).updateChatPinnedMessage(tl_updateChannelPinnedMessage.channel_id, tl_updateChannelPinnedMessage.id);
                                                                                                                                            break Label_4507;
                                                                                                                                        }
                                                                                                                                        if (obj instanceof TLRPC.TL_updateChatPinnedMessage) {
                                                                                                                                            final TLRPC.TL_updateChatPinnedMessage tl_updateChatPinnedMessage = (TLRPC.TL_updateChatPinnedMessage)obj;
                                                                                                                                            MessagesStorage.getInstance(this.currentAccount).updateChatPinnedMessage(tl_updateChatPinnedMessage.chat_id, tl_updateChatPinnedMessage.id);
                                                                                                                                            break Label_4507;
                                                                                                                                        }
                                                                                                                                        if (obj instanceof TLRPC.TL_updateUserPinnedMessage) {
                                                                                                                                            final TLRPC.TL_updateUserPinnedMessage tl_updateUserPinnedMessage = (TLRPC.TL_updateUserPinnedMessage)obj;
                                                                                                                                            MessagesStorage.getInstance(this.currentAccount).updateUserPinnedMessage(tl_updateUserPinnedMessage.user_id, tl_updateUserPinnedMessage.id);
                                                                                                                                            break Label_4507;
                                                                                                                                        }
                                                                                                                                        if (obj instanceof TLRPC.TL_updateReadFeaturedStickers) {
                                                                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list50;
                                                                                                                                            if ((list50 = list9) == null) {
                                                                                                                                                list50 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                            }
                                                                                                                                            list9 = list50;
                                                                                                                                            list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                        }
                                                                                                                                        else if (obj instanceof TLRPC.TL_updatePhoneCall) {
                                                                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list51;
                                                                                                                                            if ((list51 = list9) == null) {
                                                                                                                                                list51 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                            }
                                                                                                                                            list9 = list51;
                                                                                                                                            list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                        }
                                                                                                                                        else {
                                                                                                                                            if (obj instanceof TLRPC.TL_updateLangPack) {
                                                                                                                                                AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$JHJ_M64u5jSxCVF9v5FzZPiyZZ4(this, (TLRPC.TL_updateLangPack)obj));
                                                                                                                                                break Label_4507;
                                                                                                                                            }
                                                                                                                                            if (obj instanceof TLRPC.TL_updateLangPackTooLong) {
                                                                                                                                                LocaleController.getInstance().reloadCurrentRemoteLocale(this.currentAccount, ((TLRPC.TL_updateLangPackTooLong)obj).lang_code);
                                                                                                                                                break Label_4507;
                                                                                                                                            }
                                                                                                                                            if (obj instanceof TLRPC.TL_updateFavedStickers) {
                                                                                                                                                ArrayList<TLRPC.TL_updateChatParticipants> list52;
                                                                                                                                                if ((list52 = list9) == null) {
                                                                                                                                                    list52 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                                }
                                                                                                                                                list9 = list52;
                                                                                                                                                list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                            }
                                                                                                                                            else {
                                                                                                                                                if (!(obj instanceof TLRPC.TL_updateContactsReset)) {
                                                                                                                                                    SparseIntArray sparseIntArray6;
                                                                                                                                                    if (obj instanceof TLRPC.TL_updateChannelAvailableMessages) {
                                                                                                                                                        final TLRPC.TL_updateChannelAvailableMessages tl_updateChannelAvailableMessages = (TLRPC.TL_updateChannelAvailableMessages)obj;
                                                                                                                                                        if ((sparseIntArray6 = sparseIntArray2) == null) {
                                                                                                                                                            sparseIntArray6 = new SparseIntArray();
                                                                                                                                                        }
                                                                                                                                                        final int value9 = sparseIntArray6.get(tl_updateChannelAvailableMessages.channel_id);
                                                                                                                                                        if (value9 == 0 || value9 < tl_updateChannelAvailableMessages.available_min_id) {
                                                                                                                                                            sparseIntArray6.put(tl_updateChannelAvailableMessages.channel_id, tl_updateChannelAvailableMessages.available_min_id);
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    else {
                                                                                                                                                        final SparseIntArray sparseIntArray7 = sparseIntArray2;
                                                                                                                                                        Label_5312: {
                                                                                                                                                            if (!(obj instanceof TLRPC.TL_updateDialogUnreadMark)) {
                                                                                                                                                                if (obj instanceof TLRPC.TL_updateMessagePoll) {
                                                                                                                                                                    final TLRPC.TL_updateMessagePoll tl_updateMessagePoll = (TLRPC.TL_updateMessagePoll)obj;
                                                                                                                                                                    if (Math.abs(SystemClock.uptimeMillis() - SendMessagesHelper.getInstance(this.currentAccount).getVoteSendTime(tl_updateMessagePoll.poll_id)) >= 600L) {
                                                                                                                                                                        MessagesStorage.getInstance(this.currentAccount).updateMessagePollResults(tl_updateMessagePoll.poll_id, tl_updateMessagePoll.poll, tl_updateMessagePoll.results);
                                                                                                                                                                        if (list9 == null) {
                                                                                                                                                                            list9 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                                                        }
                                                                                                                                                                        list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                                                        break Label_5312;
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                                concurrentHashMap7 = concurrentHashMap;
                                                                                                                                                                break Label_3704;
                                                                                                                                                            }
                                                                                                                                                            ArrayList<TLRPC.TL_updateChatParticipants> list53;
                                                                                                                                                            if ((list53 = list9) == null) {
                                                                                                                                                                list53 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                                            }
                                                                                                                                                            list9 = list53;
                                                                                                                                                            list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                                        }
                                                                                                                                                        sparseIntArray6 = sparseIntArray7;
                                                                                                                                                    }
                                                                                                                                                    sparseArray = sparseArray4;
                                                                                                                                                    sparseIntArray2 = sparseIntArray6;
                                                                                                                                                    break Label_3862;
                                                                                                                                                }
                                                                                                                                                ArrayList<TLRPC.TL_updateChatParticipants> list54;
                                                                                                                                                if ((list54 = list9) == null) {
                                                                                                                                                    list54 = new ArrayList<TLRPC.TL_updateChatParticipants>();
                                                                                                                                                }
                                                                                                                                                list9 = list54;
                                                                                                                                                list9.add((TLRPC.TL_updateChatParticipants)obj);
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    sparseArray5 = sparseArray4;
                                                                                                                                    break Label_4450;
                                                                                                                                }
                                                                                                                                final TLRPC.TL_updateChatParticipantAdmin tl_updateChatParticipantAdmin = (TLRPC.TL_updateChatParticipantAdmin)obj;
                                                                                                                                MessagesStorage.getInstance(this.currentAccount).updateChatInfo(tl_updateChatParticipantAdmin.chat_id, tl_updateChatParticipantAdmin.user_id, 2, tl_updateChatParticipantAdmin.is_admin ? 1 : 0, tl_updateChatParticipantAdmin.version);
                                                                                                                            }
                                                                                                                            concurrentHashMap7 = concurrentHashMap;
                                                                                                                            longSparseArray7 = longSparseArray2;
                                                                                                                            break Label_6246;
                                                                                                                        }
                                                                                                                        final TLRPC.TL_updateChannelMessageViews tl_updateChannelMessageViews = (TLRPC.TL_updateChannelMessageViews)obj;
                                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                                            final StringBuilder sb5 = new StringBuilder();
                                                                                                                            sb5.append(obj);
                                                                                                                            sb5.append(" channelId = ");
                                                                                                                            sb5.append(tl_updateChannelMessageViews.channel_id);
                                                                                                                            FileLog.d(sb5.toString());
                                                                                                                        }
                                                                                                                        if ((sparseArray5 = sparseArray) == null) {
                                                                                                                            sparseArray5 = new SparseArray();
                                                                                                                        }
                                                                                                                        SparseIntArray sparseIntArray8;
                                                                                                                        if ((sparseIntArray8 = (SparseIntArray)sparseArray5.get(tl_updateChannelMessageViews.channel_id)) == null) {
                                                                                                                            sparseIntArray8 = new SparseIntArray();
                                                                                                                            sparseArray5.put(tl_updateChannelMessageViews.channel_id, (Object)sparseIntArray8);
                                                                                                                        }
                                                                                                                        sparseIntArray8.put(tl_updateChannelMessageViews.id, tl_updateChannelMessageViews.views);
                                                                                                                    }
                                                                                                                    sparseArray = sparseArray5;
                                                                                                                }
                                                                                                            }
                                                                                                            final ArrayList<Integer> list55 = list6;
                                                                                                            final ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap9 = concurrentHashMap;
                                                                                                            longSparseArray3 = longSparseArray9;
                                                                                                            list56 = list11;
                                                                                                            list17 = list55;
                                                                                                            longSparseArray5 = longSparseArray4;
                                                                                                            concurrentHashMap6 = concurrentHashMap9;
                                                                                                            currentTimeMillis = n4;
                                                                                                            sparseLongArray9 = sparseLongArray6;
                                                                                                            break Label_8228;
                                                                                                        }
                                                                                                        final TLRPC.TL_updateChannelTooLong tl_updateChannelTooLong = (TLRPC.TL_updateChannelTooLong)obj;
                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                            final StringBuilder sb6 = new StringBuilder();
                                                                                                            sb6.append(obj);
                                                                                                            sb6.append(" channelId = ");
                                                                                                            sb6.append(tl_updateChannelTooLong.channel_id);
                                                                                                            FileLog.d(sb6.toString());
                                                                                                        }
                                                                                                        int value10 = this.channelsPts.get(tl_updateChannelTooLong.channel_id);
                                                                                                        if (value10 == 0) {
                                                                                                            final int channelPtsSync = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(tl_updateChannelTooLong.channel_id);
                                                                                                            if (channelPtsSync == 0) {
                                                                                                                final TLRPC.Chat chat2 = concurrentHashMap.get(tl_updateChannelTooLong.channel_id);
                                                                                                                TLRPC.Chat chat3 = null;
                                                                                                                Label_3558: {
                                                                                                                    if (chat2 != null) {
                                                                                                                        chat3 = chat2;
                                                                                                                        if (!chat2.min) {
                                                                                                                            break Label_3558;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    chat3 = this.getChat(tl_updateChannelTooLong.channel_id);
                                                                                                                }
                                                                                                                TLRPC.Chat chatSync2 = null;
                                                                                                                Label_3599: {
                                                                                                                    if (chat3 != null) {
                                                                                                                        chatSync2 = chat3;
                                                                                                                        if (!chat3.min) {
                                                                                                                            break Label_3599;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    chatSync2 = MessagesStorage.getInstance(this.currentAccount).getChatSync(tl_updateChannelTooLong.channel_id);
                                                                                                                    this.putChat(chatSync2, true);
                                                                                                                }
                                                                                                                value10 = channelPtsSync;
                                                                                                                if (chatSync2 != null) {
                                                                                                                    value10 = channelPtsSync;
                                                                                                                    if (!chatSync2.min) {
                                                                                                                        this.loadUnknownChannel(chatSync2, 0L);
                                                                                                                        value10 = channelPtsSync;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            else {
                                                                                                                this.channelsPts.put(tl_updateChannelTooLong.channel_id, channelPtsSync);
                                                                                                                value10 = channelPtsSync;
                                                                                                            }
                                                                                                        }
                                                                                                        if (value10 != 0) {
                                                                                                            if ((tl_updateChannelTooLong.flags & 0x1) != 0x0) {
                                                                                                                if (tl_updateChannelTooLong.pts > value10) {
                                                                                                                    this.getChannelDifference(tl_updateChannelTooLong.channel_id);
                                                                                                                }
                                                                                                            }
                                                                                                            else {
                                                                                                                this.getChannelDifference(tl_updateChannelTooLong.channel_id);
                                                                                                            }
                                                                                                        }
                                                                                                        concurrentHashMap7 = concurrentHashMap;
                                                                                                    }
                                                                                                    break Label_2399;
                                                                                                }
                                                                                                final TLRPC.TL_updateChannelWebPage tl_updateChannelWebPage = (TLRPC.TL_updateChannelWebPage)obj;
                                                                                                if ((longSparseArray8 = longSparseArray9) == null) {
                                                                                                    longSparseArray8 = new LongSparseArray();
                                                                                                }
                                                                                                final TLRPC.WebPage webpage2 = tl_updateChannelWebPage.webpage;
                                                                                                longSparseArray8.put(webpage2.id, (Object)webpage2);
                                                                                            }
                                                                                            longSparseArray3 = longSparseArray8;
                                                                                        }
                                                                                        final ArrayList<Integer> list57 = list6;
                                                                                        n2 = n7;
                                                                                        list4 = list30;
                                                                                        list58 = list11;
                                                                                        list17 = list57;
                                                                                        break Label_1934;
                                                                                    }
                                                                                }
                                                                                concurrentHashMap7 = concurrentHashMap;
                                                                            }
                                                                            longSparseArray7 = longSparseArray2;
                                                                        }
                                                                        list17 = list6;
                                                                        longSparseArray2 = longSparseArray7;
                                                                        final ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap10 = concurrentHashMap7;
                                                                        n2 = n7;
                                                                        list4 = list30;
                                                                        list56 = list5;
                                                                        longSparseArray5 = longSparseArray4;
                                                                        concurrentHashMap6 = concurrentHashMap10;
                                                                        sparseLongArray9 = sparseLongArray6;
                                                                        break Label_8228;
                                                                    }
                                                                    final TLRPC.TL_updateEncryptedMessagesRead e5 = (TLRPC.TL_updateEncryptedMessagesRead)obj;
                                                                    if ((sparseIntArray5 = sparseIntArray) == null) {
                                                                        sparseIntArray5 = new SparseIntArray();
                                                                    }
                                                                    sparseIntArray5.put(e5.chat_id, e5.max_date);
                                                                    if ((list33 = list10) == null) {
                                                                        list33 = new ArrayList<TLRPC.TL_updateEncryptedMessagesRead>();
                                                                    }
                                                                    list33.add(e5);
                                                                }
                                                                list10 = list33;
                                                                sparseIntArray = sparseIntArray5;
                                                                currentTimeMillis = n4;
                                                                final ArrayList<MessageObject> list59 = list11;
                                                                n2 = n7;
                                                                list4 = list30;
                                                                list17 = list6;
                                                                list58 = list59;
                                                                break Label_1934;
                                                            }
                                                            final TLRPC.TL_updateEncryptedChatTyping tl_updateEncryptedChatTyping = (TLRPC.TL_updateEncryptedChatTyping)obj;
                                                            final TLRPC.EncryptedChat encryptedChatDB = this.getEncryptedChatDB(tl_updateEncryptedChatTyping.chat_id, true);
                                                            Label_2222: {
                                                                if (encryptedChatDB != null) {
                                                                    final long n21 = (long)tl_updateEncryptedChatTyping.chat_id << 32;
                                                                    ArrayList<PrintingUser> value11;
                                                                    if ((value11 = this.printingUsers.get(n21)) == null) {
                                                                        value11 = new ArrayList<PrintingUser>();
                                                                        this.printingUsers.put(n21, value11);
                                                                    }
                                                                    while (true) {
                                                                        for (int size9 = value11.size(), index4 = 0; index4 < size9; ++index4) {
                                                                            final PrintingUser printingUser2 = value11.get(index4);
                                                                            if (printingUser2.userId == encryptedChatDB.user_id) {
                                                                                printingUser2.lastTime = n4;
                                                                                printingUser2.action = new TLRPC.TL_sendMessageTypingAction();
                                                                                final boolean b6 = true;
                                                                                sparseLongArray6 = sparseLongArray5;
                                                                                if (!b6) {
                                                                                    final PrintingUser e6 = new PrintingUser();
                                                                                    e6.userId = encryptedChatDB.user_id;
                                                                                    e6.lastTime = currentTimeMillis;
                                                                                    e6.action = new TLRPC.TL_sendMessageTypingAction();
                                                                                    value11.add(e6);
                                                                                    n3 = 1;
                                                                                }
                                                                                this.onlinePrivacy.put(encryptedChatDB.user_id, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                                                                break Label_2222;
                                                                            }
                                                                        }
                                                                        final boolean b6 = false;
                                                                        continue;
                                                                    }
                                                                }
                                                                sparseLongArray6 = sparseLongArray5;
                                                            }
                                                            n12 = k;
                                                        }
                                                        final ArrayList<MessageObject> list60 = list11;
                                                        list4 = list30;
                                                        k = n12;
                                                        list17 = list6;
                                                        n2 = n7;
                                                        list58 = list60;
                                                    }
                                                    final ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap11 = concurrentHashMap;
                                                    list56 = list58;
                                                    longSparseArray5 = longSparseArray4;
                                                    concurrentHashMap6 = concurrentHashMap11;
                                                    sparseLongArray9 = sparseLongArray6;
                                                    break Label_8228;
                                                }
                                                final TLRPC.TL_updateContactLink tl_updateContactLink = (TLRPC.TL_updateContactLink)obj;
                                                ArrayList<Integer> list61;
                                                if ((list61 = list6) == null) {
                                                    list61 = new ArrayList<Integer>();
                                                }
                                                if (tl_updateContactLink.my_link instanceof TLRPC.TL_contactLinkContact) {
                                                    final int index5 = list61.indexOf(-tl_updateContactLink.user_id);
                                                    if (index5 != -1) {
                                                        list61.remove(index5);
                                                    }
                                                    if (!list61.contains(tl_updateContactLink.user_id)) {
                                                        list61.add(tl_updateContactLink.user_id);
                                                    }
                                                }
                                                else {
                                                    final int index6 = list61.indexOf(tl_updateContactLink.user_id);
                                                    if (index6 != -1) {
                                                        list61.remove(index6);
                                                    }
                                                    if (!list61.contains(tl_updateContactLink.user_id)) {
                                                        list61.add(-tl_updateContactLink.user_id);
                                                    }
                                                }
                                                n2 = n7;
                                                list6 = list61;
                                            }
                                        }
                                        sparseLongArray4 = sparseLongArray5;
                                    }
                                    final ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap12 = concurrentHashMap;
                                    list56 = list11;
                                    list17 = list6;
                                    longSparseArray5 = longSparseArray4;
                                    concurrentHashMap6 = concurrentHashMap12;
                                    currentTimeMillis = n4;
                                    sparseLongArray9 = sparseLongArray4;
                                    break Label_8228;
                                }
                            }
                            concurrentHashMap5 = concurrentHashMap3;
                            list4 = list13;
                        }
                        list56 = list5;
                        list17 = list6;
                        longSparseArray5 = longSparseArray4;
                        concurrentHashMap6 = concurrentHashMap5;
                        sparseLongArray9 = sparseLongArray2;
                        break Label_8228;
                        sparseLongArray9 = sparseLongArray2;
                        list56 = list5;
                        break Label_8228;
                    }
                    n22 = k;
                    sparseLongArray10 = sparseLongArray2;
                    int n23 = n2;
                    longSparseArray10 = longSparseArray4;
                    n24 = n;
                    concurrentHashMap13 = concurrentHashMap;
                    longSparseArray11 = longSparseArray2;
                    final SparseArray sparseArray6 = sparseArray2;
                    list62 = list5;
                    if (b2) {
                        message4 = ((TLRPC.TL_updateNewMessage)obj).message;
                    }
                    else {
                        final TLRPC.Message message5 = ((TLRPC.TL_updateNewChannelMessage)obj).message;
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb7 = new StringBuilder();
                            sb7.append(obj);
                            sb7.append(" channelId = ");
                            sb7.append(message5.to_id.channel_id);
                            FileLog.d(sb7.toString());
                        }
                        message4 = message5;
                        if (!message5.out) {
                            message4 = message5;
                            if (message5.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                message5.out = true;
                                message4 = message5;
                            }
                        }
                    }
                    final TLRPC.Peer to_id2 = message4.to_id;
                    int n25 = to_id2.channel_id;
                    int user_id6 = 0;
                    int i2 = 0;
                    Label_6945: {
                        if (n25 == 0) {
                            n25 = to_id2.chat_id;
                            if (n25 == 0) {
                                user_id6 = to_id2.user_id;
                                if (user_id6 == 0) {
                                    user_id6 = 0;
                                }
                                i2 = 0;
                                break Label_6945;
                            }
                        }
                        user_id6 = 0;
                        i2 = n25;
                    }
                    if (i2 != 0) {
                        TLRPC.Chat chat4;
                        if ((chat4 = concurrentHashMap13.get(i2)) == null) {
                            chat4 = this.getChat(i2);
                        }
                        if ((chatSync3 = chat4) == null) {
                            chatSync3 = MessagesStorage.getInstance(this.currentAccount).getChatSync(i2);
                            this.putChat(chatSync3, true);
                        }
                    }
                    else {
                        chatSync3 = null;
                    }
                    sparseArray7 = sparseArray6;
                    n2 = n23;
                    if (n24 != 0) {
                        if (i2 != 0 && chatSync3 == null) {
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb8 = new StringBuilder();
                                sb8.append("not found chat ");
                                sb8.append(i2);
                                FileLog.d(sb8.toString());
                            }
                            return false;
                        }
                        final int n26 = message4.entities.size() + 3;
                        int n27 = 0;
                        while (true) {
                            sparseArray7 = sparseArray6;
                            n2 = n23;
                            if (n27 >= n26) {
                                break;
                            }
                            int n28 = user_id6;
                            int n31 = 0;
                            Label_7226: {
                                if (n27 != 0) {
                                    if (n27 == 1) {
                                        final int n29 = n28 = message4.from_id;
                                        if (message4.post) {
                                            final int n30 = 1;
                                            n28 = n29;
                                            n31 = n30;
                                            break Label_7226;
                                        }
                                    }
                                    else if (n27 == 2) {
                                        final TLRPC.MessageFwdHeader fwd_from = message4.fwd_from;
                                        if (fwd_from != null) {
                                            n28 = fwd_from.from_id;
                                        }
                                        else {
                                            n28 = 0;
                                        }
                                    }
                                    else {
                                        final TLRPC.MessageEntity messageEntity2 = message4.entities.get(n27 - 3);
                                        if (messageEntity2 instanceof TLRPC.TL_messageEntityMentionName) {
                                            n28 = ((TLRPC.TL_messageEntityMentionName)messageEntity2).user_id;
                                        }
                                        else {
                                            n28 = 0;
                                        }
                                    }
                                }
                                n31 = 0;
                            }
                            int n32;
                            if (n28 > 0) {
                                final TLRPC.User user4 = concurrentHashMap2.get(n28);
                                TLRPC.User user5 = null;
                                Label_7286: {
                                    if (user4 != null) {
                                        if (n31 != 0) {
                                            user5 = user4;
                                            break Label_7286;
                                        }
                                        user5 = user4;
                                        if (!user4.min) {
                                            break Label_7286;
                                        }
                                    }
                                    user5 = this.getUser(n28);
                                }
                                TLRPC.User user6 = null;
                                Label_7361: {
                                    if (user5 != null) {
                                        user6 = user5;
                                        if (n31 != 0) {
                                            break Label_7361;
                                        }
                                        user6 = user5;
                                        if (!user5.min) {
                                            break Label_7361;
                                        }
                                    }
                                    final TLRPC.User userSync2 = MessagesStorage.getInstance(this.currentAccount).getUserSync(n28);
                                    TLRPC.User user7;
                                    if ((user7 = userSync2) != null) {
                                        user7 = userSync2;
                                        if (n31 == 0) {
                                            user7 = userSync2;
                                            if (userSync2.min) {
                                                user7 = null;
                                            }
                                        }
                                    }
                                    this.putUser(user7, true);
                                    user6 = user7;
                                }
                                if (user6 == null) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        final StringBuilder sb9 = new StringBuilder();
                                        sb9.append("not found user ");
                                        sb9.append(n28);
                                        FileLog.d(sb9.toString());
                                    }
                                    return false;
                                }
                                n32 = n23;
                                if (n27 == 1) {
                                    final TLRPC.UserStatus status = user6.status;
                                    n32 = n23;
                                    if (status != null) {
                                        n32 = n23;
                                        if (status.expires <= 0) {
                                            this.onlinePrivacy.put(n28, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                            n32 = (n23 | 0x4);
                                        }
                                    }
                                }
                            }
                            else {
                                n32 = n23;
                            }
                            ++n27;
                            user_id6 = n28;
                            n23 = n32;
                        }
                    }
                    if (chatSync3 != null && chatSync3.megagroup) {
                        message4.flags |= Integer.MIN_VALUE;
                    }
                    final TLRPC.MessageAction action = message4.action;
                    if (action instanceof TLRPC.TL_messageActionChatDeleteUser) {
                        final TLRPC.User user8 = concurrentHashMap2.get(action.user_id);
                        if (user8 != null && user8.bot) {
                            message4.reply_markup = new TLRPC.TL_replyKeyboardHide();
                            message4.flags |= 0x40;
                        }
                        else if (message4.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId() && message4.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            concurrentHashMap6 = concurrentHashMap13;
                            longSparseArray5 = longSparseArray10;
                            list17 = list6;
                            sparseArray2 = sparseArray7;
                            continue;
                        }
                    }
                    break;
                }
                ArrayList<TLRPC.TL_message> list63;
                if ((list63 = list7) == null) {
                    list63 = new ArrayList<TLRPC.TL_message>();
                }
                list63.add((TLRPC.TL_message)message4);
                ImageLoader.saveMessageThumbs(message4);
                final int clientUserId2 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                final TLRPC.Peer to_id3 = message4.to_id;
                final int chat_id5 = to_id3.chat_id;
                if (chat_id5 != 0) {
                    message4.dialog_id = -chat_id5;
                }
                else {
                    final int channel_id4 = to_id3.channel_id;
                    if (channel_id4 != 0) {
                        message4.dialog_id = -channel_id4;
                    }
                    else {
                        if (to_id3.user_id == clientUserId2) {
                            to_id3.user_id = message4.from_id;
                        }
                        message4.dialog_id = message4.to_id.user_id;
                    }
                }
                ConcurrentHashMap<Long, Integer> concurrentHashMap14;
                if (message4.out) {
                    concurrentHashMap14 = this.dialogs_read_outbox_max;
                }
                else {
                    concurrentHashMap14 = this.dialogs_read_inbox_max;
                }
                final Integer n33 = concurrentHashMap14.get(message4.dialog_id);
                Integer n34;
                if (n33 == null) {
                    final Integer value12 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message4.out, message4.dialog_id);
                    concurrentHashMap14.put(message4.dialog_id, value12);
                    n34 = value12;
                }
                else {
                    n34 = n33;
                }
                boolean unread = false;
                Label_7938: {
                    if (n34 < message4.id && (chatSync3 == null || !ChatObject.isNotInChat(chatSync3))) {
                        final TLRPC.MessageAction action2 = message4.action;
                        if (!(action2 instanceof TLRPC.TL_messageActionChatMigrateTo) && !(action2 instanceof TLRPC.TL_messageActionChannelCreate)) {
                            unread = true;
                            break Label_7938;
                        }
                    }
                    unread = false;
                }
                message4.unread = unread;
                if (message4.dialog_id == clientUserId2) {
                    message4.unread = false;
                    message4.media_unread = false;
                    message4.out = true;
                }
                final int currentAccount3 = this.currentAccount;
                final boolean contains3 = this.createdDialogIds.contains(message4.dialog_id);
                final ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap15 = concurrentHashMap13;
                final MessageObject messageObject3 = new MessageObject(currentAccount3, message4, concurrentHashMap2, concurrentHashMap15, contains3);
                final int type = messageObject3.type;
                Label_8069: {
                    int n35;
                    if (type == 11) {
                        n35 = (n2 | 0x8);
                    }
                    else {
                        if (type != 10) {
                            break Label_8069;
                        }
                        n35 = (n2 | 0x10);
                    }
                    n2 = n35;
                }
                if (longSparseArray10 == null) {
                    longSparseArray5 = new LongSparseArray();
                }
                else {
                    longSparseArray5 = longSparseArray10;
                }
                ArrayList<MessageObject> list64 = (ArrayList<MessageObject>)longSparseArray5.get(message4.dialog_id);
                if (list64 == null) {
                    list64 = new ArrayList<MessageObject>();
                    longSparseArray5.put(message4.dialog_id, (Object)list64);
                }
                list64.add(messageObject3);
                if (!messageObject3.isOut() && messageObject3.isUnread()) {
                    if (list62 == null) {
                        list62 = new ArrayList<MessageObject>();
                    }
                    list62.add(messageObject3);
                }
                final ArrayList<MessageObject> list65 = list62;
                final ArrayList<TLRPC.TL_message> list66 = list63;
                list17 = list6;
                longSparseArray2 = longSparseArray11;
                k = n22;
                sparseLongArray9 = sparseLongArray10;
                sparseArray2 = sparseArray7;
                n = n24;
                concurrentHashMap6 = concurrentHashMap15;
                list7 = list66;
                list56 = list65;
            }
            ++k;
            final ConcurrentHashMap<Integer, TLRPC.Chat> concurrentHashMap16 = concurrentHashMap6;
            sparseLongArray2 = sparseLongArray9;
            list5 = list56;
            list6 = list17;
            longSparseArray4 = longSparseArray5;
            concurrentHashMap = concurrentHashMap16;
        }
        final LongSparseArray longSparseArray12 = longSparseArray4;
        boolean b7 = n3 != 0;
        LongSparseArray longSparseArray13;
        if ((longSparseArray13 = longSparseArray12) != null) {
            final int size10 = longSparseArray12.size();
            int n36 = 0;
            while (true) {
                b7 = (n3 != 0);
                longSparseArray13 = longSparseArray12;
                if (n36 >= size10) {
                    break;
                }
                if (this.updatePrintingUsersWithNewMessages(longSparseArray12.keyAt(n36), (ArrayList<MessageObject>)longSparseArray12.valueAt(n36))) {
                    n3 = (true ? 1 : 0);
                }
                ++n36;
            }
        }
        if (b7) {
            this.updatePrintingStrings();
        }
        if (list6 != null) {
            ContactsController.getInstance(this.currentAccount).processContactsUpdates(list6, concurrentHashMap2);
        }
        if (list5 != null) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$rmpWNkBwNMY39XhMVE_pMAwyvyA(this, list5));
        }
        if (list7 != null) {
            StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, list7.size());
            MessagesStorage.getInstance(this.currentAccount).putMessages((ArrayList<TLRPC.Message>)list7, true, true, false, DownloadController.getInstance(this.currentAccount).getAutodownloadMask());
        }
        if (longSparseArray2 != null) {
            for (int size11 = longSparseArray2.size(), n37 = 0; n37 < size11; ++n37) {
                final TLRPC.TL_messages_messages tl_messages_messages = new TLRPC.TL_messages_messages();
                final ArrayList list67 = (ArrayList)longSparseArray2.valueAt(n37);
                for (int size12 = list67.size(), index7 = 0; index7 < size12; ++index7) {
                    tl_messages_messages.messages.add(list67.get(index7).messageOwner);
                }
                MessagesStorage.getInstance(this.currentAccount).putMessages(tl_messages_messages, longSparseArray2.keyAt(n37), -2, 0, false);
            }
        }
        if (sparseArray != null) {
            MessagesStorage.getInstance(this.currentAccount).putChannelViews((SparseArray<SparseIntArray>)sparseArray, true);
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$aIzxVN1fNX7ISomIdQom833zn_c(this, n2, list9, longSparseArray3, longSparseArray13, longSparseArray2, b7, list6, list8, sparseArray));
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$ZCTDymAyINyEW9FoTNxhmaMSvzk(this, sparseLongArray, sparseLongArray2, sparseIntArray, list4, sparseArray2, sparseIntArray2));
        if (longSparseArray3 != null) {
            MessagesStorage.getInstance(this.currentAccount).putWebPages((LongSparseArray<TLRPC.WebPage>)longSparseArray3);
        }
        if (sparseLongArray != null || sparseLongArray2 != null || sparseIntArray != null || list4 != null) {
            final ArrayList<Long> list68 = list4;
            if (sparseLongArray != null || list68 != null) {
                MessagesStorage.getInstance(this.currentAccount).updateDialogsWithReadMessages(sparseLongArray, sparseLongArray2, list68, true);
            }
            MessagesStorage.getInstance(this.currentAccount).markMessagesAsRead(sparseLongArray, sparseLongArray2, sparseIntArray, true);
        }
        if (list4 != null) {
            MessagesStorage.getInstance(this.currentAccount).markMessagesContentAsRead(list4, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
        }
        if (sparseArray2 != null) {
            for (int size13 = sparseArray2.size(), n38 = 0; n38 < size13; ++n38) {
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$eIE8R8MYjUQYKP1gLWpaSncXEb8(this, (ArrayList)sparseArray2.valueAt(n38), sparseArray2.keyAt(n38)));
            }
        }
        if (sparseIntArray2 != null) {
            for (int size14 = sparseIntArray2.size(), n39 = 0; n39 < size14; ++n39) {
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$H4wHvQqPFocrQ_yPWuW6Ehm2l3E(this, sparseIntArray2.keyAt(n39), sparseIntArray2.valueAt(n39)));
            }
        }
        if (list10 != null) {
            for (int size15 = list10.size(), index8 = 0; index8 < size15; ++index8) {
                final TLRPC.TL_updateEncryptedMessagesRead tl_updateEncryptedMessagesRead = list10.get(index8);
                MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(tl_updateEncryptedMessagesRead.chat_id, tl_updateEncryptedMessagesRead.max_date, tl_updateEncryptedMessagesRead.date, 1, null);
            }
        }
        return true;
    }
    
    public void processUpdates(final TLRPC.Updates updates, final boolean b) {
        final boolean b2 = updates instanceof TLRPC.TL_updateShort;
        int n = 0;
        int n5 = 0;
        ArrayList<Integer> list4 = null;
        int n6 = 0;
        int n9 = 0;
        Label_4304: {
            int n4 = 0;
            Label_0213: {
                if (b2) {
                    final ArrayList<TLRPC.Update> list = new ArrayList<TLRPC.Update>();
                    list.add(updates.update);
                    this.processUpdateArray(list, null, null, false);
                }
                else {
                    final boolean b3 = updates instanceof TLRPC.TL_updateShortChatMessage;
                    final String s = " count = ";
                    final String s2 = "add to queue";
                    if (b3 || updates instanceof TLRPC.TL_updateShortMessage) {
                        int n2;
                        if (b3) {
                            n2 = updates.from_id;
                        }
                        else {
                            n2 = updates.user_id;
                        }
                        final TLRPC.User user = this.getUser(n2);
                        TLRPC.User user2 = null;
                        Label_2863: {
                            if (user != null) {
                                user2 = user;
                                if (!user.min) {
                                    break Label_2863;
                                }
                            }
                            final TLRPC.User userSync = MessagesStorage.getInstance(this.currentAccount).getUserSync(n2);
                            TLRPC.User user3;
                            if ((user3 = userSync) != null) {
                                user3 = userSync;
                                if (userSync.min) {
                                    user3 = null;
                                }
                            }
                            this.putUser(user3, true);
                            user2 = user3;
                        }
                        final TLRPC.MessageFwdHeader fwd_from = updates.fwd_from;
                        boolean b4 = false;
                        TLRPC.User user5 = null;
                        TLRPC.Chat chat2 = null;
                        Label_3032: {
                            if (fwd_from != null) {
                                final int from_id = fwd_from.from_id;
                                TLRPC.User user4;
                                if (from_id != 0) {
                                    if ((user4 = this.getUser(from_id)) == null) {
                                        user4 = MessagesStorage.getInstance(this.currentAccount).getUserSync(updates.fwd_from.from_id);
                                        this.putUser(user4, true);
                                    }
                                    b4 = true;
                                }
                                else {
                                    b4 = false;
                                    user4 = null;
                                }
                                final int channel_id = updates.fwd_from.channel_id;
                                user5 = user4;
                                if (channel_id != 0) {
                                    TLRPC.Chat chat;
                                    if ((chat = this.getChat(channel_id)) == null) {
                                        chat = MessagesStorage.getInstance(this.currentAccount).getChatSync(updates.fwd_from.channel_id);
                                        this.putChat(chat, true);
                                    }
                                    chat2 = chat;
                                    b4 = true;
                                    user5 = user4;
                                    break Label_3032;
                                }
                            }
                            else {
                                b4 = false;
                                user5 = null;
                            }
                            chat2 = null;
                        }
                        final int via_bot_id = updates.via_bot_id;
                        TLRPC.User user6;
                        boolean b5;
                        if (via_bot_id != 0) {
                            if ((user6 = this.getUser(via_bot_id)) == null) {
                                user6 = MessagesStorage.getInstance(this.currentAccount).getUserSync(updates.via_bot_id);
                                this.putUser(user6, true);
                            }
                            b5 = true;
                        }
                        else {
                            b5 = false;
                            user6 = null;
                        }
                        final boolean b6 = updates instanceof TLRPC.TL_updateShortMessage;
                        int n3 = 0;
                        Label_3238: {
                            Label_3149: {
                                if (b6) {
                                    if (user2 == null || (b4 && user5 == null && chat2 == null)) {
                                        break Label_3149;
                                    }
                                    if (b5 && user6 == null) {
                                        break Label_3149;
                                    }
                                }
                                else {
                                    TLRPC.Chat chat3;
                                    if ((chat3 = this.getChat(updates.chat_id)) == null) {
                                        chat3 = MessagesStorage.getInstance(this.currentAccount).getChatSync(updates.chat_id);
                                        this.putChat(chat3, true);
                                    }
                                    if (chat3 == null || user2 == null || (b4 && user5 == null && chat2 == null)) {
                                        break Label_3149;
                                    }
                                    if (b5 && user6 == null) {
                                        break Label_3149;
                                    }
                                }
                                n3 = 0;
                                break Label_3238;
                            }
                            n3 = 1;
                        }
                        boolean b7 = n3 != 0;
                        if (n3 == 0) {
                            b7 = (n3 != 0);
                            if (!updates.entities.isEmpty()) {
                                int index = 0;
                                while (true) {
                                    b7 = (n3 != 0);
                                    if (index >= updates.entities.size()) {
                                        break;
                                    }
                                    final TLRPC.MessageEntity messageEntity = updates.entities.get(index);
                                    if (messageEntity instanceof TLRPC.TL_messageEntityMentionName) {
                                        final int user_id = ((TLRPC.TL_messageEntityMentionName)messageEntity).user_id;
                                        final TLRPC.User user7 = this.getUser(user_id);
                                        if (user7 == null || user7.min) {
                                            final TLRPC.User userSync2 = MessagesStorage.getInstance(this.currentAccount).getUserSync(user_id);
                                            TLRPC.User user8;
                                            if ((user8 = userSync2) != null) {
                                                user8 = userSync2;
                                                if (userSync2.min) {
                                                    user8 = null;
                                                }
                                            }
                                            if (user8 == null) {
                                                b7 = true;
                                                break;
                                            }
                                            this.putUser(user2, true);
                                        }
                                    }
                                    ++index;
                                }
                            }
                        }
                        Label_3462: {
                            if (user2 != null) {
                                final TLRPC.UserStatus status = user2.status;
                                if (status != null && status.expires <= 0) {
                                    this.onlinePrivacy.put(user2.id, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                    n4 = 1;
                                    break Label_3462;
                                }
                            }
                            n4 = 0;
                        }
                        Label_4301: {
                            Label_3467: {
                                if (!b7) {
                                    if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + updates.pts_count == updates.pts) {
                                        final TLRPC.TL_message e = new TLRPC.TL_message();
                                        e.id = updates.id;
                                        final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                        if (b6) {
                                            if (updates.out) {
                                                e.from_id = clientUserId;
                                            }
                                            else {
                                                e.from_id = n2;
                                            }
                                            e.to_id = new TLRPC.TL_peerUser();
                                            e.to_id.user_id = n2;
                                            e.dialog_id = n2;
                                        }
                                        else {
                                            e.from_id = n2;
                                            e.to_id = new TLRPC.TL_peerChat();
                                            final TLRPC.Peer to_id = e.to_id;
                                            final int chat_id = updates.chat_id;
                                            to_id.chat_id = chat_id;
                                            e.dialog_id = -chat_id;
                                        }
                                        e.fwd_from = updates.fwd_from;
                                        e.silent = updates.silent;
                                        e.out = updates.out;
                                        e.mentioned = updates.mentioned;
                                        e.media_unread = updates.media_unread;
                                        e.entities = updates.entities;
                                        e.message = updates.message;
                                        e.date = updates.date;
                                        e.via_bot_id = updates.via_bot_id;
                                        e.flags = (updates.flags | 0x100);
                                        e.reply_to_msg_id = updates.reply_to_msg_id;
                                        e.media = new TLRPC.TL_messageMediaEmpty();
                                        ConcurrentHashMap<Long, Integer> concurrentHashMap;
                                        if (e.out) {
                                            concurrentHashMap = this.dialogs_read_outbox_max;
                                        }
                                        else {
                                            concurrentHashMap = this.dialogs_read_inbox_max;
                                        }
                                        Integer value;
                                        if ((value = concurrentHashMap.get(e.dialog_id)) == null) {
                                            value = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(e.out, e.dialog_id);
                                            concurrentHashMap.put(e.dialog_id, value);
                                        }
                                        e.unread = (value < e.id);
                                        if (e.dialog_id == clientUserId) {
                                            e.unread = false;
                                            e.media_unread = false;
                                            e.out = true;
                                        }
                                        boolean b8 = true;
                                        MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(updates.pts);
                                        final MessageObject e2 = new MessageObject(this.currentAccount, e, this.createdDialogIds.contains(e.dialog_id));
                                        final ArrayList<MessageObject> list2 = new ArrayList<MessageObject>();
                                        list2.add(e2);
                                        final ArrayList<TLRPC.Message> list3 = new ArrayList<TLRPC.Message>();
                                        list3.add(e);
                                        if (b6) {
                                            if (updates.out || !this.updatePrintingUsersWithNewMessages(updates.user_id, list2)) {
                                                b8 = false;
                                            }
                                            if (b8) {
                                                this.updatePrintingStrings();
                                            }
                                            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$fPbr3UYq21LGzjTkEfox5kOy2UM(this, b8, n2, list2));
                                        }
                                        else {
                                            final boolean updatePrintingUsersWithNewMessages = this.updatePrintingUsersWithNewMessages(-updates.chat_id, list2);
                                            if (updatePrintingUsersWithNewMessages) {
                                                this.updatePrintingStrings();
                                            }
                                            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$s0GgcDgh5GZ9Riv31NjWgQrzb_w(this, updatePrintingUsersWithNewMessages, updates, list2));
                                        }
                                        if (!e2.isOut()) {
                                            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$iAn5VSEQcHcofX2azl7_Y4mcgGM(this, list2));
                                        }
                                        MessagesStorage.getInstance(this.currentAccount).putMessages(list3, false, true, false, 0);
                                    }
                                    else if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() != updates.pts) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("need get diff short message, pts: ");
                                            sb.append(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue());
                                            sb.append(" ");
                                            sb.append(updates.pts);
                                            sb.append(" count = ");
                                            sb.append(updates.pts_count);
                                            FileLog.d(sb.toString());
                                        }
                                        if (!this.gettingDifference && this.updatesStartWaitTimePts != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) > 1500L) {
                                            break Label_3467;
                                        }
                                        if (this.updatesStartWaitTimePts == 0L) {
                                            this.updatesStartWaitTimePts = System.currentTimeMillis();
                                        }
                                        if (BuildVars.LOGS_ENABLED) {
                                            FileLog.d("add to queue");
                                        }
                                        this.updatesQueuePts.add(updates);
                                    }
                                    n = 0;
                                    break Label_4301;
                                }
                            }
                            n = 1;
                        }
                        break Label_0213;
                    }
                    final boolean b9 = updates instanceof TLRPC.TL_updatesCombined;
                    if (b9 || updates instanceof TLRPC.TL_updates) {
                        SparseArray sparseArray = null;
                        SparseArray sparseArray2;
                        for (int i = 0; i < updates.chats.size(); ++i, sparseArray = sparseArray2) {
                            final TLRPC.Chat chat4 = updates.chats.get(i);
                            sparseArray2 = sparseArray;
                            if (chat4 instanceof TLRPC.TL_channel) {
                                sparseArray2 = sparseArray;
                                if (chat4.min) {
                                    final TLRPC.Chat chat5 = this.getChat(chat4.id);
                                    TLRPC.Chat chatSync = null;
                                    Label_0344: {
                                        if (chat5 != null) {
                                            chatSync = chat5;
                                            if (!chat5.min) {
                                                break Label_0344;
                                            }
                                        }
                                        chatSync = MessagesStorage.getInstance(this.currentAccount).getChatSync(updates.chat_id);
                                        this.putChat(chatSync, true);
                                    }
                                    if (chatSync != null) {
                                        sparseArray2 = sparseArray;
                                        if (!chatSync.min) {
                                            continue;
                                        }
                                    }
                                    SparseArray sparseArray3;
                                    if ((sparseArray3 = sparseArray) == null) {
                                        sparseArray3 = new SparseArray();
                                    }
                                    sparseArray3.put(chat4.id, (Object)chat4);
                                    sparseArray2 = sparseArray3;
                                }
                            }
                        }
                        Label_0528: {
                            if (sparseArray != null) {
                                for (int j = 0; j < updates.updates.size(); ++j) {
                                    final TLRPC.Update update = updates.updates.get(j);
                                    if (update instanceof TLRPC.TL_updateNewChannelMessage) {
                                        final int channel_id2 = ((TLRPC.TL_updateNewChannelMessage)update).message.to_id.channel_id;
                                        if (sparseArray.indexOfKey(channel_id2) >= 0) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                final StringBuilder sb2 = new StringBuilder();
                                                sb2.append("need get diff because of min channel ");
                                                sb2.append(channel_id2);
                                                FileLog.d(sb2.toString());
                                            }
                                            n5 = 1;
                                            break Label_0528;
                                        }
                                    }
                                }
                            }
                            n5 = 0;
                        }
                        if (n5 == 0) {
                            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(updates.users, updates.chats, true, true);
                            Collections.sort(updates.updates, this.updatesComparator);
                            list4 = null;
                            n6 = 0;
                            final String s3 = s2;
                            while (updates.updates.size() > 0) {
                                final TLRPC.Update update2 = updates.updates.get(0);
                                Label_2351: {
                                    int n7 = 0;
                                    int n8 = 0;
                                    Label_1106: {
                                        Label_0861: {
                                            if (this.getUpdateType(update2) == 0) {
                                                final TLRPC.TL_updates e3 = new TLRPC.TL_updates();
                                                e3.updates.add(update2);
                                                e3.pts = getUpdatePts(update2);
                                                e3.pts_count = getUpdatePtsCount(update2);
                                                while (1 < updates.updates.size()) {
                                                    final TLRPC.Update e4 = updates.updates.get(1);
                                                    final int updatePts = getUpdatePts(e4);
                                                    final int updatePtsCount = getUpdatePtsCount(e4);
                                                    if (this.getUpdateType(e4) != 0 || e3.pts + updatePtsCount != updatePts) {
                                                        break;
                                                    }
                                                    e3.updates.add(e4);
                                                    e3.pts = updatePts;
                                                    e3.pts_count += updatePtsCount;
                                                    updates.updates.remove(1);
                                                }
                                                if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + e3.pts_count == e3.pts) {
                                                    if (!this.processUpdateArray(e3.updates, updates.users, updates.chats, false)) {
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            final StringBuilder sb3 = new StringBuilder();
                                                            sb3.append("need get diff inner TL_updates, pts: ");
                                                            sb3.append(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue());
                                                            sb3.append(" ");
                                                            sb3.append(updates.seq);
                                                            FileLog.d(sb3.toString());
                                                        }
                                                        break Label_0861;
                                                    }
                                                    else {
                                                        MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(e3.pts);
                                                    }
                                                }
                                                else if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() != e3.pts) {
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        final StringBuilder sb4 = new StringBuilder();
                                                        sb4.append(update2);
                                                        sb4.append(" need get diff, pts: ");
                                                        sb4.append(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue());
                                                        sb4.append(" ");
                                                        sb4.append(e3.pts);
                                                        sb4.append(s);
                                                        sb4.append(e3.pts_count);
                                                        FileLog.d(sb4.toString());
                                                    }
                                                    if (!this.gettingDifference) {
                                                        final long updatesStartWaitTimePts = this.updatesStartWaitTimePts;
                                                        if (updatesStartWaitTimePts != 0L && (updatesStartWaitTimePts == 0L || Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) > 1500L)) {
                                                            break Label_0861;
                                                        }
                                                    }
                                                    if (this.updatesStartWaitTimePts == 0L) {
                                                        this.updatesStartWaitTimePts = System.currentTimeMillis();
                                                    }
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.d(s3);
                                                    }
                                                    this.updatesQueuePts.add(e3);
                                                    n7 = n5;
                                                    n8 = n6;
                                                    break Label_1106;
                                                }
                                                n8 = n6;
                                                n7 = n5;
                                                break Label_1106;
                                            }
                                            if (this.getUpdateType(update2) == 1) {
                                                final TLRPC.TL_updates e5 = new TLRPC.TL_updates();
                                                e5.updates.add(update2);
                                                e5.pts = getUpdateQts(update2);
                                                while (1 < updates.updates.size()) {
                                                    final TLRPC.Update e6 = updates.updates.get(1);
                                                    final int updateQts = getUpdateQts(e6);
                                                    if (this.getUpdateType(e6) != 1 || e5.pts + 1 != updateQts) {
                                                        break;
                                                    }
                                                    e5.updates.add(e6);
                                                    e5.pts = updateQts;
                                                    updates.updates.remove(1);
                                                }
                                                if (MessagesStorage.getInstance(this.currentAccount).getLastQtsValue() == 0 || MessagesStorage.getInstance(this.currentAccount).getLastQtsValue() + e5.updates.size() == e5.pts) {
                                                    this.processUpdateArray(e5.updates, updates.users, updates.chats, false);
                                                    MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(e5.pts);
                                                    n8 = 1;
                                                    n7 = n5;
                                                    break Label_1106;
                                                }
                                                n7 = n5;
                                                n8 = n6;
                                                if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() != e5.pts) {
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        final StringBuilder sb5 = new StringBuilder();
                                                        sb5.append(update2);
                                                        sb5.append(" need get diff, qts: ");
                                                        sb5.append(MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
                                                        sb5.append(" ");
                                                        sb5.append(e5.pts);
                                                        FileLog.d(sb5.toString());
                                                    }
                                                    if (!this.gettingDifference) {
                                                        final long updatesStartWaitTimeQts = this.updatesStartWaitTimeQts;
                                                        if (updatesStartWaitTimeQts != 0L && (updatesStartWaitTimeQts == 0L || Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeQts) > 1500L)) {
                                                            break Label_0861;
                                                        }
                                                    }
                                                    if (this.updatesStartWaitTimeQts == 0L) {
                                                        this.updatesStartWaitTimeQts = System.currentTimeMillis();
                                                    }
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        FileLog.d(s3);
                                                    }
                                                    this.updatesQueueQts.add(e5);
                                                    n7 = n5;
                                                    n8 = n6;
                                                }
                                                break Label_1106;
                                            }
                                            else {
                                                if (this.getUpdateType(update2) == 2) {
                                                    final int updateChannelId = getUpdateChannelId(update2);
                                                    int channelPtsSync = 0;
                                                    boolean b10 = false;
                                                    Label_1661: {
                                                        Label_1658: {
                                                            int value2;
                                                            if ((value2 = this.channelsPts.get(updateChannelId)) == 0) {
                                                                channelPtsSync = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(updateChannelId);
                                                                if (channelPtsSync != 0) {
                                                                    this.channelsPts.put(updateChannelId, channelPtsSync);
                                                                    break Label_1658;
                                                                }
                                                                int index2 = 0;
                                                                while (true) {
                                                                    value2 = channelPtsSync;
                                                                    if (index2 >= updates.chats.size()) {
                                                                        break;
                                                                    }
                                                                    final TLRPC.Chat chat6 = updates.chats.get(index2);
                                                                    if (chat6.id == updateChannelId) {
                                                                        this.loadUnknownChannel(chat6, 0L);
                                                                        b10 = true;
                                                                        break Label_1661;
                                                                    }
                                                                    ++index2;
                                                                }
                                                            }
                                                            channelPtsSync = value2;
                                                        }
                                                        b10 = false;
                                                    }
                                                    final TLRPC.TL_updates e7 = new TLRPC.TL_updates();
                                                    e7.updates.add(update2);
                                                    e7.pts = getUpdatePts(update2);
                                                    e7.pts_count = getUpdatePtsCount(update2);
                                                    while (1 < updates.updates.size()) {
                                                        final TLRPC.Update e8 = updates.updates.get(1);
                                                        final int updatePts2 = getUpdatePts(e8);
                                                        final int updatePtsCount2 = getUpdatePtsCount(e8);
                                                        if (this.getUpdateType(e8) != 2 || updateChannelId != getUpdateChannelId(e8) || e7.pts + updatePtsCount2 != updatePts2) {
                                                            break;
                                                        }
                                                        e7.updates.add(e8);
                                                        e7.pts = updatePts2;
                                                        e7.pts_count += updatePtsCount2;
                                                        updates.updates.remove(1);
                                                    }
                                                    ArrayList<Integer> list5;
                                                    if (!b10) {
                                                        final int pts_count = e7.pts_count;
                                                        final int pts = e7.pts;
                                                        if (pts_count + channelPtsSync == pts) {
                                                            if (!this.processUpdateArray(e7.updates, updates.users, updates.chats, false)) {
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                    final StringBuilder sb6 = new StringBuilder();
                                                                    sb6.append("need get channel diff inner TL_updates, channel_id = ");
                                                                    sb6.append(updateChannelId);
                                                                    FileLog.d(sb6.toString());
                                                                }
                                                                if (list4 == null) {
                                                                    list5 = new ArrayList<Integer>();
                                                                }
                                                                else {
                                                                    list5 = list4;
                                                                    if (!list4.contains(updateChannelId)) {
                                                                        list4.add(updateChannelId);
                                                                        list5 = list4;
                                                                    }
                                                                }
                                                            }
                                                            else {
                                                                this.channelsPts.put(updateChannelId, e7.pts);
                                                                MessagesStorage.getInstance(this.currentAccount).saveChannelPts(updateChannelId, e7.pts);
                                                                list5 = list4;
                                                            }
                                                        }
                                                        else {
                                                            list5 = list4;
                                                            if (channelPtsSync != pts) {
                                                                if (BuildVars.LOGS_ENABLED) {
                                                                    final StringBuilder sb7 = new StringBuilder();
                                                                    sb7.append(update2);
                                                                    sb7.append(" need get channel diff, pts: ");
                                                                    sb7.append(channelPtsSync);
                                                                    sb7.append(" ");
                                                                    sb7.append(e7.pts);
                                                                    sb7.append(s);
                                                                    sb7.append(e7.pts_count);
                                                                    sb7.append(" channelId = ");
                                                                    sb7.append(updateChannelId);
                                                                    FileLog.d(sb7.toString());
                                                                }
                                                                final long value3 = this.updatesStartWaitTimeChannels.get(updateChannelId);
                                                                if (!this.gettingDifferenceChannels.get(updateChannelId) && value3 != 0L && Math.abs(System.currentTimeMillis() - value3) > 1500L) {
                                                                    if (list4 == null) {
                                                                        list5 = new ArrayList<Integer>();
                                                                    }
                                                                    else {
                                                                        list5 = list4;
                                                                        if (!list4.contains(updateChannelId)) {
                                                                            list4.add(updateChannelId);
                                                                            list5 = list4;
                                                                        }
                                                                    }
                                                                }
                                                                else {
                                                                    if (value3 == 0L) {
                                                                        this.updatesStartWaitTimeChannels.put(updateChannelId, System.currentTimeMillis());
                                                                    }
                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                        FileLog.d(s3);
                                                                    }
                                                                    ArrayList<TLRPC.TL_updates> list6;
                                                                    if ((list6 = (ArrayList<TLRPC.TL_updates>)this.updatesQueueChannels.get(updateChannelId)) == null) {
                                                                        list6 = new ArrayList<TLRPC.TL_updates>();
                                                                        this.updatesQueueChannels.put(updateChannelId, (Object)list6);
                                                                    }
                                                                    list6.add(e7);
                                                                    list5 = list4;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        list5 = list4;
                                                        if (BuildVars.LOGS_ENABLED) {
                                                            final StringBuilder sb8 = new StringBuilder();
                                                            sb8.append("need load unknown channel = ");
                                                            sb8.append(updateChannelId);
                                                            FileLog.d(sb8.toString());
                                                            list5 = list4;
                                                        }
                                                    }
                                                    list4 = list5;
                                                    break Label_2351;
                                                }
                                                break;
                                            }
                                        }
                                        n7 = 1;
                                        n8 = n6;
                                    }
                                    n5 = n7;
                                    n6 = n8;
                                }
                                updates.updates.remove(0);
                            }
                            boolean b11 = false;
                            Label_2468: {
                                Label_2412: {
                                    if (b9) {
                                        if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() + 1 == updates.seq_start) {
                                            break Label_2412;
                                        }
                                        if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() == updates.seq_start) {
                                            break Label_2412;
                                        }
                                    }
                                    else {
                                        final int lastSeqValue = MessagesStorage.getInstance(this.currentAccount).getLastSeqValue();
                                        final int seq = updates.seq;
                                        if (lastSeqValue + 1 == seq || seq == 0) {
                                            break Label_2412;
                                        }
                                        if (seq == MessagesStorage.getInstance(this.currentAccount).getLastSeqValue()) {
                                            break Label_2412;
                                        }
                                    }
                                    b11 = false;
                                    break Label_2468;
                                }
                                b11 = true;
                            }
                            if (b11) {
                                this.processUpdateArray(updates.updates, updates.users, updates.chats, false);
                                if (updates.seq != 0) {
                                    if (updates.date != 0) {
                                        MessagesStorage.getInstance(this.currentAccount).setLastDateValue(updates.date);
                                    }
                                    MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(updates.seq);
                                }
                            }
                            else {
                                if (BuildVars.LOGS_ENABLED) {
                                    if (b9) {
                                        final StringBuilder sb9 = new StringBuilder();
                                        sb9.append("need get diff TL_updatesCombined, seq: ");
                                        sb9.append(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue());
                                        sb9.append(" ");
                                        sb9.append(updates.seq_start);
                                        FileLog.d(sb9.toString());
                                    }
                                    else {
                                        final StringBuilder sb10 = new StringBuilder();
                                        sb10.append("need get diff TL_updates, seq: ");
                                        sb10.append(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue());
                                        sb10.append(" ");
                                        sb10.append(updates.seq);
                                        FileLog.d(sb10.toString());
                                    }
                                }
                                if (!this.gettingDifference && this.updatesStartWaitTimeSeq != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeSeq) > 1500L) {
                                    n5 = 1;
                                }
                                else {
                                    if (this.updatesStartWaitTimeSeq == 0L) {
                                        this.updatesStartWaitTimeSeq = System.currentTimeMillis();
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("add TL_updates/Combined to queue");
                                    }
                                    this.updatesQueueSeq.add(updates);
                                }
                            }
                        }
                        else {
                            list4 = null;
                            n6 = 0;
                        }
                        n9 = 0;
                        break Label_4304;
                    }
                    if (updates instanceof TLRPC.TL_updatesTooLong) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("need get diff TL_updatesTooLong");
                        }
                        n4 = 0;
                        n = 1;
                        break Label_0213;
                    }
                    if (updates instanceof UserActionUpdatesSeq) {
                        MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(updates.seq);
                    }
                    else if (updates instanceof UserActionUpdatesPts) {
                        final int chat_id2 = updates.chat_id;
                        if (chat_id2 != 0) {
                            this.channelsPts.put(chat_id2, updates.pts);
                            MessagesStorage.getInstance(this.currentAccount).saveChannelPts(updates.chat_id, updates.pts);
                        }
                        else {
                            MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(updates.pts);
                        }
                    }
                }
                n4 = 0;
            }
            list4 = null;
            final int n10 = 0;
            n9 = n4;
            n5 = n;
            n6 = n10;
        }
        SecretChatHelper.getInstance(this.currentAccount).processPendingEncMessages();
        if (!b) {
            for (int k = 0; k < this.updatesQueueChannels.size(); ++k) {
                final int key = this.updatesQueueChannels.keyAt(k);
                if (list4 != null && list4.contains(key)) {
                    this.getChannelDifference(key);
                }
                else {
                    this.processChannelsUpdatesQueue(key, 0);
                }
            }
            if (n5 != 0) {
                this.getDifference();
            }
            else {
                for (int l = 0; l < 3; ++l) {
                    this.processUpdatesQueue(l, 0);
                }
            }
        }
        if (n6 != 0) {
            final TLRPC.TL_messages_receivedQueue tl_messages_receivedQueue = new TLRPC.TL_messages_receivedQueue();
            tl_messages_receivedQueue.max_qts = MessagesStorage.getInstance(this.currentAccount).getLastQtsValue();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_receivedQueue, (RequestDelegate)_$$Lambda$MessagesController$9PuTSEkyzwdd31UYsaKLfGdsGkM.INSTANCE);
        }
        if (n9 != 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$U7UNWGF7e0N8D_YCbxsC0l6X5Iw(this));
        }
        MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
    }
    
    public void processUserInfo(final TLRPC.User user, final TLRPC.UserFull userFull, final boolean b, final boolean b2, final MessageObject messageObject, final int n) {
        if (b) {
            this.loadFullUser(user, n, b2);
        }
        if (userFull != null) {
            if (this.fullUsers.get(user.id) == null) {
                this.fullUsers.put(user.id, (Object)userFull);
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$vF733kQiTAupYlV5PFZpkpSFLz4(this, user, userFull, messageObject));
        }
    }
    
    public void putChat(final TLRPC.Chat value, final boolean b) {
        if (value == null) {
            return;
        }
        final TLRPC.Chat chat = this.chats.get(value.id);
        if (chat == value) {
            return;
        }
        if (chat != null && !TextUtils.isEmpty((CharSequence)chat.username)) {
            this.objectsByUsernames.remove(chat.username.toLowerCase());
        }
        if (!TextUtils.isEmpty((CharSequence)value.username)) {
            this.objectsByUsernames.put(value.username.toLowerCase(), value);
        }
        if (value.min) {
            if (chat != null) {
                if (!b) {
                    chat.title = value.title;
                    chat.photo = value.photo;
                    chat.broadcast = value.broadcast;
                    chat.verified = value.verified;
                    chat.megagroup = value.megagroup;
                    final TLRPC.TL_chatBannedRights default_banned_rights = value.default_banned_rights;
                    if (default_banned_rights != null) {
                        chat.default_banned_rights = default_banned_rights;
                        chat.flags |= 0x40000;
                    }
                    final TLRPC.TL_chatAdminRights admin_rights = value.admin_rights;
                    if (admin_rights != null) {
                        chat.admin_rights = admin_rights;
                        chat.flags |= 0x4000;
                    }
                    final TLRPC.TL_chatBannedRights banned_rights = value.banned_rights;
                    if (banned_rights != null) {
                        chat.banned_rights = banned_rights;
                        chat.flags |= 0x8000;
                    }
                    final String username = value.username;
                    if (username != null) {
                        chat.username = username;
                        chat.flags |= 0x40;
                    }
                    else {
                        chat.flags &= 0xFFFFFFBF;
                        chat.username = null;
                    }
                    final int participants_count = value.participants_count;
                    if (participants_count != 0) {
                        chat.participants_count = participants_count;
                    }
                }
            }
            else {
                this.chats.put(value.id, value);
            }
        }
        else {
            int flags = 0;
            if (!b) {
                if (chat != null) {
                    if (value.version != chat.version) {
                        this.loadedFullChats.remove((Object)value.id);
                    }
                    final int participants_count2 = chat.participants_count;
                    if (participants_count2 != 0 && value.participants_count == 0) {
                        value.participants_count = participants_count2;
                        value.flags |= 0x20000;
                    }
                    final TLRPC.TL_chatBannedRights banned_rights2 = chat.banned_rights;
                    int flags2;
                    if (banned_rights2 != null) {
                        flags2 = banned_rights2.flags;
                    }
                    else {
                        flags2 = 0;
                    }
                    final TLRPC.TL_chatBannedRights banned_rights3 = value.banned_rights;
                    int flags3;
                    if (banned_rights3 != null) {
                        flags3 = banned_rights3.flags;
                    }
                    else {
                        flags3 = 0;
                    }
                    final TLRPC.TL_chatBannedRights default_banned_rights2 = chat.default_banned_rights;
                    int flags4;
                    if (default_banned_rights2 != null) {
                        flags4 = default_banned_rights2.flags;
                    }
                    else {
                        flags4 = 0;
                    }
                    final TLRPC.TL_chatBannedRights default_banned_rights3 = value.default_banned_rights;
                    if (default_banned_rights3 != null) {
                        flags = default_banned_rights3.flags;
                    }
                    chat.default_banned_rights = value.default_banned_rights;
                    if (chat.default_banned_rights == null) {
                        chat.flags &= 0xFFFBFFFF;
                    }
                    else {
                        chat.flags |= 0x40000;
                    }
                    chat.banned_rights = value.banned_rights;
                    if (chat.banned_rights == null) {
                        chat.flags &= 0xFFFF7FFF;
                    }
                    else {
                        chat.flags |= 0x8000;
                    }
                    chat.admin_rights = value.admin_rights;
                    if (chat.admin_rights == null) {
                        chat.flags &= 0xFFFFBFFF;
                    }
                    else {
                        chat.flags |= 0x4000;
                    }
                    if (flags2 != flags3 || flags4 != flags) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$JQg5AlfE18YOamojW_v4DYkKyIA(this, value));
                    }
                }
                this.chats.put(value.id, value);
            }
            else if (chat == null) {
                this.chats.put(value.id, value);
            }
            else if (chat.min) {
                value.min = false;
                value.title = chat.title;
                value.photo = chat.photo;
                value.broadcast = chat.broadcast;
                value.verified = chat.verified;
                value.megagroup = chat.megagroup;
                final TLRPC.TL_chatBannedRights default_banned_rights4 = chat.default_banned_rights;
                if (default_banned_rights4 != null) {
                    value.default_banned_rights = default_banned_rights4;
                    value.flags |= 0x40000;
                }
                final TLRPC.TL_chatAdminRights admin_rights2 = chat.admin_rights;
                if (admin_rights2 != null) {
                    value.admin_rights = admin_rights2;
                    value.flags |= 0x4000;
                }
                final TLRPC.TL_chatBannedRights banned_rights4 = chat.banned_rights;
                if (banned_rights4 != null) {
                    value.banned_rights = banned_rights4;
                    value.flags |= 0x8000;
                }
                final String username2 = chat.username;
                if (username2 != null) {
                    value.username = username2;
                    value.flags |= 0x40;
                }
                else {
                    value.flags &= 0xFFFFFFBF;
                    value.username = null;
                }
                final int participants_count3 = chat.participants_count;
                if (participants_count3 != 0 && value.participants_count == 0) {
                    value.participants_count = participants_count3;
                    value.flags |= 0x20000;
                }
                this.chats.put(value.id, value);
            }
        }
    }
    
    public void putChats(final ArrayList<TLRPC.Chat> list, final boolean b) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (int size = list.size(), i = 0; i < size; ++i) {
                    this.putChat(list.get(i), b);
                }
            }
        }
    }
    
    public void putEncryptedChat(final TLRPC.EncryptedChat encryptedChat, final boolean b) {
        if (encryptedChat == null) {
            return;
        }
        if (b) {
            this.encryptedChats.putIfAbsent(encryptedChat.id, encryptedChat);
        }
        else {
            this.encryptedChats.put(encryptedChat.id, encryptedChat);
        }
    }
    
    public void putEncryptedChats(final ArrayList<TLRPC.EncryptedChat> list, final boolean b) {
        if (list != null) {
            if (!list.isEmpty()) {
                for (int size = list.size(), i = 0; i < size; ++i) {
                    this.putEncryptedChat(list.get(i), b);
                }
            }
        }
    }
    
    public boolean putUser(final TLRPC.User user, final boolean b) {
        if (user == null) {
            return false;
        }
        boolean b2 = false;
        Label_0040: {
            if (b) {
                final int id = user.id;
                if (id / 1000 != 333 && id != 777000) {
                    b2 = true;
                    break Label_0040;
                }
            }
            b2 = false;
        }
        final TLRPC.User user2 = this.users.get(user.id);
        if (user2 == user) {
            return false;
        }
        if (user2 != null && !TextUtils.isEmpty((CharSequence)user2.username)) {
            this.objectsByUsernames.remove(user2.username.toLowerCase());
        }
        if (!TextUtils.isEmpty((CharSequence)user.username)) {
            this.objectsByUsernames.put(user.username.toLowerCase(), user);
        }
        if (user.min) {
            if (user2 != null) {
                if (!b2) {
                    if (user.bot) {
                        final String username = user.username;
                        if (username != null) {
                            user2.username = username;
                            user2.flags |= 0x8;
                        }
                        else {
                            user2.flags &= 0xFFFFFFF7;
                            user2.username = null;
                        }
                    }
                    final TLRPC.UserProfilePhoto photo = user.photo;
                    if (photo != null) {
                        user2.photo = photo;
                        user2.flags |= 0x20;
                    }
                    else {
                        user2.flags &= 0xFFFFFFDF;
                        user2.photo = null;
                    }
                }
            }
            else {
                this.users.put(user.id, user);
            }
        }
        else if (!b2) {
            this.users.put(user.id, user);
            if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                UserConfig.getInstance(this.currentAccount).setCurrentUser(user);
                UserConfig.getInstance(this.currentAccount).saveConfig(true);
            }
            if (user2 != null) {
                final TLRPC.UserStatus status = user.status;
                if (status != null) {
                    final TLRPC.UserStatus status2 = user2.status;
                    if (status2 != null && status.expires != status2.expires) {
                        return true;
                    }
                }
            }
        }
        else if (user2 == null) {
            this.users.put(user.id, user);
        }
        else if (user2.min) {
            user.min = false;
            if (user2.bot) {
                final String username2 = user2.username;
                if (username2 != null) {
                    user.username = username2;
                    user.flags |= 0x8;
                }
                else {
                    user.flags &= 0xFFFFFFF7;
                    user.username = null;
                }
            }
            final TLRPC.UserProfilePhoto photo2 = user2.photo;
            if (photo2 != null) {
                user.photo = photo2;
                user.flags |= 0x20;
            }
            else {
                user.flags &= 0xFFFFFFDF;
                user.photo = null;
            }
            this.users.put(user.id, user);
        }
        return false;
    }
    
    public void putUsers(final ArrayList<TLRPC.User> list, final boolean b) {
        if (list != null) {
            if (!list.isEmpty()) {
                final int size = list.size();
                int i = 0;
                boolean b2 = false;
                while (i < size) {
                    if (this.putUser(list.get(i), b)) {
                        b2 = true;
                    }
                    ++i;
                }
                if (b2) {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$MHasUaMkg9dD_kjYrwSO8zJY7Bs(this));
                }
            }
        }
    }
    
    public void registerForPush(final String token) {
        if (!TextUtils.isEmpty((CharSequence)token) && !this.registeringForPush) {
            if (UserConfig.getInstance(this.currentAccount).getClientUserId() != 0) {
                if (UserConfig.getInstance(this.currentAccount).registeredForPush && token.equals(SharedConfig.pushString)) {
                    return;
                }
                this.registeringForPush = true;
                this.lastPushRegisterSendTime = SystemClock.elapsedRealtime();
                if (SharedConfig.pushAuthKey == null) {
                    SharedConfig.pushAuthKey = new byte[256];
                    Utilities.random.nextBytes(SharedConfig.pushAuthKey);
                    SharedConfig.saveConfig();
                }
                final TLRPC.TL_account_registerDevice tl_account_registerDevice = new TLRPC.TL_account_registerDevice();
                tl_account_registerDevice.token_type = 2;
                tl_account_registerDevice.token = token;
                tl_account_registerDevice.secret = SharedConfig.pushAuthKey;
                for (int i = 0; i < 3; ++i) {
                    final UserConfig instance = UserConfig.getInstance(i);
                    if (i != this.currentAccount && instance.isClientActivated()) {
                        final int clientUserId = instance.getClientUserId();
                        tl_account_registerDevice.other_uids.add(clientUserId);
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("add other uid = ");
                            sb.append(clientUserId);
                            sb.append(" for account ");
                            sb.append(this.currentAccount);
                            FileLog.d(sb.toString());
                        }
                    }
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_registerDevice, new _$$Lambda$MessagesController$RQIUdGcrH5D0HSeyADRBCxVETRg(this, token));
            }
        }
    }
    
    public void reloadMentionsCountForChannels(final ArrayList<Integer> list) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Wb5lXI806u6uYMpUfHZLtqjdAzg(this, list));
    }
    
    public void reloadWebPages(final long n, final HashMap<String, ArrayList<MessageObject>> hashMap) {
        for (final Map.Entry<String, ArrayList<MessageObject>> entry : hashMap.entrySet()) {
            final String message = entry.getKey();
            final ArrayList<MessageObject> c = entry.getValue();
            ArrayList<MessageObject> value;
            if ((value = this.reloadingWebpages.get(message)) == null) {
                value = new ArrayList<MessageObject>();
                this.reloadingWebpages.put(message, value);
            }
            value.addAll(c);
            final TLRPC.TL_messages_getWebPagePreview tl_messages_getWebPagePreview = new TLRPC.TL_messages_getWebPagePreview();
            tl_messages_getWebPagePreview.message = message;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getWebPagePreview, new _$$Lambda$MessagesController$4nBTlb9eBU7C4QavllU2UZiopks(this, message, n));
        }
    }
    
    protected void removeDeletedMessagesFromArray(final long n, final ArrayList<TLRPC.Message> list) {
        final LongSparseArray<Integer> deletedHistory = this.deletedHistory;
        int i = 0;
        final int intValue = (int)deletedHistory.get(n, (Object)0);
        if (intValue == 0) {
            return;
        }
        int n2;
        int n3;
        for (int size = list.size(); i < size; i = n2 + 1, size = n3) {
            n2 = i;
            n3 = size;
            if (list.get(i).id <= intValue) {
                list.remove(i);
                n2 = i - 1;
                n3 = size - 1;
            }
        }
    }
    
    public void removeDialogAction(final long n, final boolean b, final boolean b2) {
        final TLRPC.Dialog e = (TLRPC.Dialog)this.dialogs_dict.get(n);
        if (e == null) {
            return;
        }
        if (b) {
            this.clearingHistoryDialogs.remove(n);
        }
        else {
            this.deletingDialogs.remove(n);
            if (!b2) {
                this.allDialogs.add(e);
                this.sortDialogs(null);
            }
        }
        if (!b2) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
        }
    }
    
    public void reorderPinnedDialogs(int n, ArrayList<TLRPC.InputDialogPeer> order, long pendingTask) {
        final TLRPC.TL_messages_reorderPinnedDialogs tl_messages_reorderPinnedDialogs = new TLRPC.TL_messages_reorderPinnedDialogs();
        tl_messages_reorderPinnedDialogs.folder_id = n;
        tl_messages_reorderPinnedDialogs.force = true;
        if (pendingTask == 0L) {
            final ArrayList<TLRPC.Dialog> dialogs = this.getDialogs(n);
            if (dialogs.isEmpty()) {
                return;
            }
            final int size = dialogs.size();
            final int n2 = 0;
            int i = 0;
            int n3 = 0;
            while (i < size) {
                final TLRPC.Dialog dialog = dialogs.get(i);
                int n4;
                if (dialog instanceof TLRPC.TL_dialogFolder) {
                    n4 = n3;
                }
                else {
                    if (!dialog.pinned) {
                        break;
                    }
                    MessagesStorage.getInstance(this.currentAccount).setDialogPinned(dialog.id, dialog.pinnedNum);
                    n4 = n3;
                    if ((int)dialog.id != 0) {
                        final TLRPC.InputPeer inputPeer = this.getInputPeer((int)dialogs.get(i).id);
                        final TLRPC.TL_inputDialogPeer e = new TLRPC.TL_inputDialogPeer();
                        e.peer = inputPeer;
                        tl_messages_reorderPinnedDialogs.order.add(e);
                        n4 = n3 + e.getObjectSize();
                    }
                }
                ++i;
                n3 = n4;
            }
            NativeByteBuffer nativeByteBuffer = null;
            Label_0296: {
                try {
                    order = new NativeByteBuffer(n3 + 12);
                    try {
                        order.writeInt32(16);
                        order.writeInt32(n);
                        order.writeInt32(tl_messages_reorderPinnedDialogs.order.size());
                        final int size2 = tl_messages_reorderPinnedDialogs.order.size();
                        n = n2;
                        while (true) {
                            nativeByteBuffer = order;
                            if (n >= size2) {
                                break Label_0296;
                            }
                            tl_messages_reorderPinnedDialogs.order.get(n).serializeToStream(order);
                            ++n;
                        }
                    }
                    catch (Exception ex) {}
                }
                catch (Exception ex) {
                    order = null;
                }
                final Exception ex;
                FileLog.e(ex);
                nativeByteBuffer = order;
            }
            pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask(nativeByteBuffer);
        }
        else {
            tl_messages_reorderPinnedDialogs.order = (ArrayList<TLRPC.InputDialogPeer>)order;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_reorderPinnedDialogs, new _$$Lambda$MessagesController$iDjQfJMpGMx6ueasQejBNbv1luw(this, pendingTask));
    }
    
    public void reportSpam(final long lng, final TLRPC.User user, final TLRPC.Chat chat, final TLRPC.EncryptedChat encryptedChat) {
        if (user == null && chat == null && encryptedChat == null) {
            return;
        }
        final SharedPreferences$Editor edit = this.notificationsPreferences.edit();
        final StringBuilder sb = new StringBuilder();
        sb.append("spam3_");
        sb.append(lng);
        edit.putInt(sb.toString(), 1);
        edit.commit();
        if ((int)lng == 0) {
            if (encryptedChat == null || encryptedChat.access_hash == 0L) {
                return;
            }
            final TLRPC.TL_messages_reportEncryptedSpam tl_messages_reportEncryptedSpam = new TLRPC.TL_messages_reportEncryptedSpam();
            tl_messages_reportEncryptedSpam.peer = new TLRPC.TL_inputEncryptedChat();
            final TLRPC.TL_inputEncryptedChat peer = tl_messages_reportEncryptedSpam.peer;
            peer.chat_id = encryptedChat.id;
            peer.access_hash = encryptedChat.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_reportEncryptedSpam, (RequestDelegate)_$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc.INSTANCE, 2);
        }
        else {
            final TLRPC.TL_messages_reportSpam tl_messages_reportSpam = new TLRPC.TL_messages_reportSpam();
            if (chat != null) {
                tl_messages_reportSpam.peer = this.getInputPeer(-chat.id);
            }
            else if (user != null) {
                tl_messages_reportSpam.peer = this.getInputPeer(user.id);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_reportSpam, (RequestDelegate)_$$Lambda$MessagesController$A_WQ0vAsaYkwbum0RRlzdSDsvGo.INSTANCE, 2);
        }
    }
    
    public void saveGif(final Object o, final TLRPC.Document document) {
        if (o != null) {
            if (MessageObject.isGifDocument(document)) {
                final TLRPC.TL_messages_saveGif tl_messages_saveGif = new TLRPC.TL_messages_saveGif();
                tl_messages_saveGif.id = new TLRPC.TL_inputDocument();
                final TLRPC.InputDocument id = tl_messages_saveGif.id;
                id.id = document.id;
                id.access_hash = document.access_hash;
                id.file_reference = document.file_reference;
                if (id.file_reference == null) {
                    id.file_reference = new byte[0];
                }
                tl_messages_saveGif.unsave = false;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_saveGif, new _$$Lambda$MessagesController$AJRaVVVB2_z__jsu2wGba_x0X0k(this, o, tl_messages_saveGif));
            }
        }
    }
    
    public void saveRecentSticker(final Object o, final TLRPC.Document document, final boolean attached) {
        if (o != null) {
            if (document != null) {
                final TLRPC.TL_messages_saveRecentSticker tl_messages_saveRecentSticker = new TLRPC.TL_messages_saveRecentSticker();
                tl_messages_saveRecentSticker.id = new TLRPC.TL_inputDocument();
                final TLRPC.InputDocument id = tl_messages_saveRecentSticker.id;
                id.id = document.id;
                id.access_hash = document.access_hash;
                id.file_reference = document.file_reference;
                if (id.file_reference == null) {
                    id.file_reference = new byte[0];
                }
                tl_messages_saveRecentSticker.unsave = false;
                tl_messages_saveRecentSticker.attached = attached;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_saveRecentSticker, new _$$Lambda$MessagesController$Ac6iMOZ3v5XiuSoBQXhRvLk2mds(this, o, tl_messages_saveRecentSticker));
            }
        }
    }
    
    public void saveWallpaperToServer(File file, final long id, final long access_hash, final boolean blur, final boolean motion, final int background_color, final float n, final boolean b, long pendingTask) {
        if (this.uploadingWallpaper != null) {
            final File filesDirFixed = ApplicationLoader.getFilesDirFixed();
            String child;
            if (this.uploadingWallpaperBlurred) {
                child = "wallpaper_original.jpg";
            }
            else {
                child = "wallpaper.jpg";
            }
            final File obj = new File(filesDirFixed, child);
            if (file != null && (file.getAbsolutePath().equals(this.uploadingWallpaper) || file.equals(obj))) {
                this.uploadingWallpaperMotion = motion;
                this.uploadingWallpaperBlurred = blur;
                return;
            }
            FileLoader.getInstance(this.currentAccount).cancelUploadFile(this.uploadingWallpaper, false);
            this.uploadingWallpaper = null;
        }
        if (file != null) {
            this.uploadingWallpaper = file.getAbsolutePath();
            this.uploadingWallpaperMotion = motion;
            this.uploadingWallpaperBlurred = blur;
            FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingWallpaper, false, true, 16777216);
        }
        else if (access_hash != 0L) {
            final TLRPC.TL_inputWallPaper tl_inputWallPaper = new TLRPC.TL_inputWallPaper();
            tl_inputWallPaper.id = id;
            tl_inputWallPaper.access_hash = access_hash;
            final TLRPC.TL_wallPaperSettings tl_wallPaperSettings = new TLRPC.TL_wallPaperSettings();
            tl_wallPaperSettings.blur = blur;
            tl_wallPaperSettings.motion = motion;
            if (background_color != 0) {
                tl_wallPaperSettings.background_color = background_color;
                tl_wallPaperSettings.flags |= 0x1;
                tl_wallPaperSettings.intensity = (int)(100.0f * n);
                tl_wallPaperSettings.flags |= 0x8;
            }
            TLObject tlObject;
            if (b) {
                tlObject = new TLRPC.TL_account_installWallPaper();
                ((TLRPC.TL_account_installWallPaper)tlObject).wallpaper = tl_inputWallPaper;
                ((TLRPC.TL_account_installWallPaper)tlObject).settings = tl_wallPaperSettings;
            }
            else {
                tlObject = new TLRPC.TL_account_saveWallPaper();
                ((TLRPC.TL_account_saveWallPaper)tlObject).wallpaper = tl_inputWallPaper;
                ((TLRPC.TL_account_saveWallPaper)tlObject).settings = tl_wallPaperSettings;
            }
            if (pendingTask == 0L) {
                try {
                    file = (File)new NativeByteBuffer(44);
                    try {
                        ((NativeByteBuffer)file).writeInt32(12);
                        ((NativeByteBuffer)file).writeInt64(id);
                        ((NativeByteBuffer)file).writeInt64(access_hash);
                        ((NativeByteBuffer)file).writeBool(blur);
                        ((NativeByteBuffer)file).writeBool(motion);
                        ((NativeByteBuffer)file).writeInt32(background_color);
                        ((NativeByteBuffer)file).writeDouble(n);
                        ((NativeByteBuffer)file).writeBool(b);
                    }
                    catch (Exception ex) {}
                }
                catch (Exception ex) {
                    file = null;
                }
                final Exception ex;
                FileLog.e(ex);
                pendingTask = MessagesStorage.getInstance(this.currentAccount).createPendingTask((NativeByteBuffer)file);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tlObject, new _$$Lambda$MessagesController$YmKMKppARt1F8875D8216H1_tmM(this, pendingTask, b, id));
        }
    }
    
    public void sendBotStart(final TLRPC.User user, final String start_param) {
        if (user == null) {
            return;
        }
        final TLRPC.TL_messages_startBot tl_messages_startBot = new TLRPC.TL_messages_startBot();
        tl_messages_startBot.bot = this.getInputUser(user);
        tl_messages_startBot.peer = this.getInputPeer(user.id);
        tl_messages_startBot.start_param = start_param;
        tl_messages_startBot.random_id = Utilities.random.nextLong();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_startBot, new _$$Lambda$MessagesController$dxMathd1bP5_uSwd4Gt7ZvEOFtw(this));
    }
    
    public void sendTyping(final long n, int n2, final int n3) {
        if (n == 0L) {
            return;
        }
        final LongSparseArray longSparseArray = (LongSparseArray)this.sendingTypings.get(n2);
        if (longSparseArray != null && longSparseArray.get(n) != null) {
            return;
        }
        LongSparseArray longSparseArray2;
        if ((longSparseArray2 = longSparseArray) == null) {
            longSparseArray2 = new LongSparseArray();
            this.sendingTypings.put(n2, (Object)longSparseArray2);
        }
        final int n4 = (int)n;
        final int i = (int)(n >> 32);
        if (n4 != 0) {
            if (i == 1) {
                return;
            }
            final TLRPC.TL_messages_setTyping tl_messages_setTyping = new TLRPC.TL_messages_setTyping();
            tl_messages_setTyping.peer = this.getInputPeer(n4);
            final TLRPC.InputPeer peer = tl_messages_setTyping.peer;
            if (peer instanceof TLRPC.TL_inputPeerChannel) {
                final TLRPC.Chat chat = this.getChat(peer.channel_id);
                if (chat == null || !chat.megagroup) {
                    return;
                }
            }
            if (tl_messages_setTyping.peer == null) {
                return;
            }
            if (n2 == 0) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageTypingAction();
            }
            else if (n2 == 1) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageRecordAudioAction();
            }
            else if (n2 == 2) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageCancelAction();
            }
            else if (n2 == 3) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageUploadDocumentAction();
            }
            else if (n2 == 4) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageUploadPhotoAction();
            }
            else if (n2 == 5) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageUploadVideoAction();
            }
            else if (n2 == 6) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageGamePlayAction();
            }
            else if (n2 == 7) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageRecordRoundAction();
            }
            else if (n2 == 8) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageUploadRoundAction();
            }
            else if (n2 == 9) {
                tl_messages_setTyping.action = new TLRPC.TL_sendMessageUploadAudioAction();
            }
            longSparseArray2.put(n, (Object)true);
            n2 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_setTyping, new _$$Lambda$MessagesController$0RUkjN1o4ur0BN0_Uwykda9jP9E(this, n2, n), 2);
            if (n3 != 0) {
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(n2, n3);
            }
        }
        else {
            if (n2 != 0) {
                return;
            }
            final TLRPC.EncryptedChat encryptedChat = this.getEncryptedChat(i);
            final byte[] auth_key = encryptedChat.auth_key;
            if (auth_key != null && auth_key.length > 1 && encryptedChat instanceof TLRPC.TL_encryptedChat) {
                final TLRPC.TL_messages_setEncryptedTyping tl_messages_setEncryptedTyping = new TLRPC.TL_messages_setEncryptedTyping();
                tl_messages_setEncryptedTyping.peer = new TLRPC.TL_inputEncryptedChat();
                final TLRPC.TL_inputEncryptedChat peer2 = tl_messages_setEncryptedTyping.peer;
                peer2.chat_id = encryptedChat.id;
                peer2.access_hash = encryptedChat.access_hash;
                tl_messages_setEncryptedTyping.typing = true;
                longSparseArray2.put(n, (Object)true);
                n2 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_setEncryptedTyping, new _$$Lambda$MessagesController$46d8KXzv97zrAkyG5uwxOcxtLc4(this, n2, n), 2);
                if (n3 != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(n2, n3);
                }
            }
        }
    }
    
    public void setDefaultBannedRole(final int n, final TLRPC.TL_chatBannedRights banned_rights, final boolean b, final BaseFragment baseFragment) {
        if (banned_rights == null) {
            return;
        }
        final TLRPC.TL_messages_editChatDefaultBannedRights tl_messages_editChatDefaultBannedRights = new TLRPC.TL_messages_editChatDefaultBannedRights();
        tl_messages_editChatDefaultBannedRights.peer = this.getInputPeer(-n);
        tl_messages_editChatDefaultBannedRights.banned_rights = banned_rights;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_editChatDefaultBannedRights, new _$$Lambda$MessagesController$1P_GCY_PWvevMznLENn0Y1ly8wA(this, n, baseFragment, tl_messages_editChatDefaultBannedRights, b));
    }
    
    public void setDialogsInTransaction(final boolean dialogsInTransaction) {
        if (!(this.dialogsInTransaction = dialogsInTransaction)) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
        }
    }
    
    public void setLastCreatedDialogId(final long l, final boolean b) {
        if (b) {
            if (this.createdDialogMainThreadIds.contains(l)) {
                return;
            }
            this.createdDialogMainThreadIds.add(l);
        }
        else {
            this.createdDialogMainThreadIds.remove(l);
            final SparseArray sparseArray = (SparseArray)this.pollsToCheck.get(l);
            if (sparseArray != null) {
                for (int size = sparseArray.size(), i = 0; i < size; ++i) {
                    ((MessageObject)sparseArray.valueAt(i)).pollVisibleOnScreen = false;
                }
            }
        }
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$GClBurUFA5765ROku5c8uOseY60(this, b, l));
    }
    
    public void setLastVisibleDialogId(final long l, final boolean b) {
        if (b) {
            if (this.visibleDialogMainThreadIds.contains(l)) {
                return;
            }
            this.visibleDialogMainThreadIds.add(l);
        }
        else {
            this.visibleDialogMainThreadIds.remove(l);
        }
    }
    
    public void setReferer(final String installReferer) {
        if (installReferer == null) {
            return;
        }
        this.installReferer = installReferer;
        this.mainPreferences.edit().putString("installReferer", installReferer).commit();
    }
    
    public void setUserAdminRole(final int n, final TLRPC.User user, final TLRPC.TL_chatAdminRights admin_rights, final boolean b, final BaseFragment baseFragment, final boolean b2) {
        if (user != null) {
            if (admin_rights != null) {
                final TLRPC.Chat chat = this.getChat(n);
                if (ChatObject.isChannel(chat)) {
                    final TLRPC.TL_channels_editAdmin tl_channels_editAdmin = new TLRPC.TL_channels_editAdmin();
                    tl_channels_editAdmin.channel = getInputChannel(chat);
                    tl_channels_editAdmin.user_id = this.getInputUser(user);
                    tl_channels_editAdmin.admin_rights = admin_rights;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_editAdmin, new _$$Lambda$MessagesController$ruvO2GOVq6vr5RbeSeoQf9j2_3I(this, n, baseFragment, tl_channels_editAdmin, b));
                }
                else {
                    final TLRPC.TL_messages_editChatAdmin tl_messages_editChatAdmin = new TLRPC.TL_messages_editChatAdmin();
                    tl_messages_editChatAdmin.chat_id = n;
                    tl_messages_editChatAdmin.user_id = this.getInputUser(user);
                    tl_messages_editChatAdmin.is_admin = (admin_rights.change_info || admin_rights.delete_messages || admin_rights.ban_users || admin_rights.invite_users || admin_rights.pin_messages || admin_rights.add_admins);
                    final _$$Lambda$MessagesController$oInAanuig7UbxR7DhBxjsIzAZu4 $$Lambda$MessagesController$oInAanuig7UbxR7DhBxjsIzAZu4 = new _$$Lambda$MessagesController$oInAanuig7UbxR7DhBxjsIzAZu4(this, n, baseFragment, tl_messages_editChatAdmin);
                    if (tl_messages_editChatAdmin.is_admin && b2) {
                        this.addUserToChat(n, user, null, 0, null, baseFragment, new _$$Lambda$MessagesController$re3lVjnmONchziB0xgSqaVhxV5Q(this, tl_messages_editChatAdmin, $$Lambda$MessagesController$oInAanuig7UbxR7DhBxjsIzAZu4));
                    }
                    else {
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_editChatAdmin, $$Lambda$MessagesController$oInAanuig7UbxR7DhBxjsIzAZu4);
                    }
                }
            }
        }
    }
    
    public void setUserBannedRole(final int n, final TLRPC.User user, final TLRPC.TL_chatBannedRights banned_rights, final boolean b, final BaseFragment baseFragment) {
        if (user != null) {
            if (banned_rights != null) {
                final TLRPC.TL_channels_editBanned tl_channels_editBanned = new TLRPC.TL_channels_editBanned();
                tl_channels_editBanned.channel = this.getInputChannel(n);
                tl_channels_editBanned.user_id = this.getInputUser(user);
                tl_channels_editBanned.banned_rights = banned_rights;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_editBanned, new _$$Lambda$MessagesController$PTatBcwlbxdtRxQAlB57_yJjqhU(this, n, baseFragment, tl_channels_editBanned, b));
            }
        }
    }
    
    public void sortDialogs(final SparseArray<TLRPC.Chat> sparseArray) {
        this.dialogsServerOnly.clear();
        this.dialogsCanAddUsers.clear();
        this.dialogsChannelsOnly.clear();
        this.dialogsGroupsOnly.clear();
        this.dialogsUsersOnly.clear();
        this.dialogsForward.clear();
        final int n = 0;
        for (int i = 0; i < this.dialogsByFolder.size(); ++i) {
            final ArrayList list = (ArrayList)this.dialogsByFolder.get(i);
            if (list != null) {
                list.clear();
            }
        }
        this.unreadUnmutedDialogs = 0;
        final int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        Collections.sort(this.allDialogs, this.dialogComparator);
        this.isLeftProxyChannel = true;
        final TLRPC.Dialog proxyDialog = this.proxyDialog;
        if (proxyDialog != null) {
            final long id = proxyDialog.id;
            if (id < 0L) {
                final TLRPC.Chat chat = this.getChat(-(int)id);
                if (chat != null && !chat.left) {
                    this.isLeftProxyChannel = false;
                }
            }
        }
        final boolean showBadgeMessages = NotificationsController.getInstance(this.currentAccount).showBadgeMessages;
        int size = this.allDialogs.size();
        int j = 0;
        int n2 = 0;
        while (j < size) {
            final TLRPC.Dialog e = this.allDialogs.get(j);
            final long id2 = e.id;
            final int n3 = (int)(id2 >> 32);
            final int n4 = (int)id2;
            int n5 = n2;
            Label_0650: {
                Label_0629: {
                    if (e instanceof TLRPC.TL_dialog) {
                        boolean b = false;
                        Label_0502: {
                            if (n4 != 0 && n3 != 1) {
                                this.dialogsServerOnly.add(e);
                                if (DialogObject.isChannel(e)) {
                                    final TLRPC.Chat chat2 = this.getChat(-n4);
                                    Label_0353: {
                                        if (chat2 != null) {
                                            Label_0343: {
                                                if (chat2.megagroup) {
                                                    final TLRPC.TL_chatAdminRights admin_rights = chat2.admin_rights;
                                                    if (admin_rights != null && (admin_rights.post_messages || admin_rights.add_admins)) {
                                                        break Label_0343;
                                                    }
                                                }
                                                if (!chat2.creator) {
                                                    break Label_0353;
                                                }
                                            }
                                            this.dialogsCanAddUsers.add(e);
                                        }
                                    }
                                    if (chat2 != null && chat2.megagroup) {
                                        this.dialogsGroupsOnly.add(e);
                                    }
                                    else {
                                        this.dialogsChannelsOnly.add(e);
                                        if (!ChatObject.hasAdminRights(chat2) || !ChatObject.canPost(chat2)) {
                                            b = false;
                                            break Label_0502;
                                        }
                                    }
                                }
                                else if (n4 < 0) {
                                    if (sparseArray != null) {
                                        final TLRPC.Chat chat3 = (TLRPC.Chat)sparseArray.get(-n4);
                                        if (chat3 != null && chat3.migrated_to != null) {
                                            this.allDialogs.remove(j);
                                            break Label_0629;
                                        }
                                    }
                                    this.dialogsCanAddUsers.add(e);
                                    this.dialogsGroupsOnly.add(e);
                                }
                                else if (n4 > 0) {
                                    this.dialogsUsersOnly.add(e);
                                }
                            }
                            b = true;
                        }
                        n5 = n2;
                        if (b) {
                            if (n4 == clientUserId) {
                                this.dialogsForward.add(0, e);
                                n5 = 1;
                            }
                            else {
                                this.dialogsForward.add(e);
                                n5 = n2;
                            }
                        }
                    }
                    if ((e.unread_count != 0 || e.unread_mark) && !this.isDialogMuted(e.id)) {
                        ++this.unreadUnmutedDialogs;
                    }
                    final TLRPC.Dialog proxyDialog2 = this.proxyDialog;
                    if (proxyDialog2 == null || e.id != proxyDialog2.id || !this.isLeftProxyChannel) {
                        this.addDialogToItsFolder(-1, e, showBadgeMessages);
                        n2 = n5;
                        break Label_0650;
                    }
                    this.allDialogs.remove(j);
                    n2 = n5;
                }
                --j;
                --size;
            }
            ++j;
        }
        final TLRPC.Dialog proxyDialog3 = this.proxyDialog;
        if (proxyDialog3 != null && this.isLeftProxyChannel) {
            this.allDialogs.add(0, proxyDialog3);
            this.addDialogToItsFolder(-2, this.proxyDialog, showBadgeMessages);
        }
        int k = n;
        if (n2 == 0) {
            final TLRPC.User currentUser = UserConfig.getInstance(this.currentAccount).getCurrentUser();
            k = n;
            if (currentUser != null) {
                final TLRPC.TL_dialog element = new TLRPC.TL_dialog();
                element.id = currentUser.id;
                element.notify_settings = new TLRPC.TL_peerNotifySettings();
                element.peer = new TLRPC.TL_peerUser();
                element.peer.user_id = currentUser.id;
                this.dialogsForward.add(0, element);
                k = n;
            }
        }
        while (k < this.dialogsByFolder.size()) {
            final int key = this.dialogsByFolder.keyAt(k);
            if (((ArrayList)this.dialogsByFolder.valueAt(k)).isEmpty()) {
                this.dialogsByFolder.remove(key);
            }
            ++k;
        }
    }
    
    public void startShortPoll(final TLRPC.Chat chat, final boolean b) {
        Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$ChpY_h3KplD8DdHCKUKN4FRi5P8(this, b, chat));
    }
    
    public void toogleChannelInvitesHistory(final int n, final boolean enabled) {
        final TLRPC.TL_channels_togglePreHistoryHidden tl_channels_togglePreHistoryHidden = new TLRPC.TL_channels_togglePreHistoryHidden();
        tl_channels_togglePreHistoryHidden.channel = this.getInputChannel(n);
        tl_channels_togglePreHistoryHidden.enabled = enabled;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_togglePreHistoryHidden, new _$$Lambda$MessagesController$YebMv5xtYdxqizkIFhqWdfrEUcI(this), 64);
    }
    
    public void toogleChannelSignatures(final int n, final boolean enabled) {
        final TLRPC.TL_channels_toggleSignatures tl_channels_toggleSignatures = new TLRPC.TL_channels_toggleSignatures();
        tl_channels_toggleSignatures.channel = this.getInputChannel(n);
        tl_channels_toggleSignatures.enabled = enabled;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_toggleSignatures, new _$$Lambda$MessagesController$aeXtWX5NT1IBK6iBhloNZ1bs_M4(this), 64);
    }
    
    public void unblockUser(final int i) {
        final TLRPC.TL_contacts_unblock tl_contacts_unblock = new TLRPC.TL_contacts_unblock();
        final TLRPC.User user = this.getUser(i);
        if (user == null) {
            return;
        }
        this.blockedUsers.delete(user.id);
        tl_contacts_unblock.id = this.getInputUser(user);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad, new Object[0]);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_unblock, new _$$Lambda$MessagesController$opwePRyqFhALjQxPzCei2q6RZPU(this, user));
    }
    
    public void unregistedPush() {
        if (UserConfig.getInstance(this.currentAccount).registeredForPush && SharedConfig.pushString.length() == 0) {
            final TLRPC.TL_account_unregisterDevice tl_account_unregisterDevice = new TLRPC.TL_account_unregisterDevice();
            tl_account_unregisterDevice.token = SharedConfig.pushString;
            tl_account_unregisterDevice.token_type = 2;
            for (int i = 0; i < 3; ++i) {
                final UserConfig instance = UserConfig.getInstance(i);
                if (i != this.currentAccount && instance.isClientActivated()) {
                    tl_account_unregisterDevice.other_uids.add(instance.getClientUserId());
                }
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_unregisterDevice, (RequestDelegate)_$$Lambda$MessagesController$8PliyHoMopJhuU4eiI2hN2lPvKs.INSTANCE);
        }
    }
    
    public void updateChannelUserName(final int n, final String username) {
        final TLRPC.TL_channels_updateUsername tl_channels_updateUsername = new TLRPC.TL_channels_updateUsername();
        tl_channels_updateUsername.channel = this.getInputChannel(n);
        tl_channels_updateUsername.username = username;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_channels_updateUsername, new _$$Lambda$MessagesController$Ov1XVh0U9G1mRr1zQgjy3_Hz5dQ(this, n, username), 64);
    }
    
    public void updateChatAbout(final int n, final String about, final TLRPC.ChatFull chatFull) {
        if (chatFull == null) {
            return;
        }
        final TLRPC.TL_messages_editChatAbout tl_messages_editChatAbout = new TLRPC.TL_messages_editChatAbout();
        tl_messages_editChatAbout.peer = this.getInputPeer(-n);
        tl_messages_editChatAbout.about = about;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_editChatAbout, new _$$Lambda$MessagesController$7X_q4bvD4zimEhOUjsZ_PwkItoo(this, chatFull, about), 64);
    }
    
    public void updateConfig(final TLRPC.TL_config tl_config) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$E_B3T1gXoq4eD739GgHdBDLPpBs(this, tl_config));
    }
    
    protected void updateInterfaceWithMessages(final long n, final ArrayList<MessageObject> list) {
        this.updateInterfaceWithMessages(n, list, false);
    }
    
    protected void updateInterfaceWithMessages(long random_id, final ArrayList<MessageObject> list, final boolean b) {
        if (list != null) {
            if (!list.isEmpty()) {
                final int n = (int)random_id;
                final int n2 = 0;
                final int n3 = 0;
                final boolean b2 = n == 0;
                int i = 0;
                MessageObject messageObject = null;
                int n4 = 0;
                int j = 0;
                int n5 = 0;
                while (i < list.size()) {
                    final MessageObject messageObject2 = list.get(i);
                    MessageObject messageObject3 = null;
                    int n6 = 0;
                    Label_0187: {
                        if (messageObject != null && (b2 || messageObject2.getId() <= messageObject.getId()) && ((!b2 && (messageObject2.getId() >= 0 || messageObject.getId() >= 0)) || messageObject2.getId() >= messageObject.getId())) {
                            messageObject3 = messageObject;
                            n6 = j;
                            if (messageObject2.messageOwner.date <= messageObject.messageOwner.date) {
                                break Label_0187;
                            }
                        }
                        final int channel_id = messageObject2.messageOwner.to_id.channel_id;
                        if (channel_id != 0) {
                            j = channel_id;
                        }
                        messageObject3 = messageObject2;
                        n6 = j;
                    }
                    int n7;
                    if ((n7 = n4) == 0) {
                        n7 = n4;
                        if (!messageObject2.isOut()) {
                            n7 = 1;
                        }
                    }
                    if (messageObject2.isOut() && !messageObject2.isSending() && !messageObject2.isForwarded()) {
                        if (messageObject2.isNewGif()) {
                            final DataQuery instance = DataQuery.getInstance(this.currentAccount);
                            final TLRPC.Message messageOwner = messageObject2.messageOwner;
                            instance.addRecentGif(messageOwner.media.document, messageOwner.date);
                        }
                        else if (messageObject2.isSticker()) {
                            final DataQuery instance2 = DataQuery.getInstance(this.currentAccount);
                            final TLRPC.Message messageOwner2 = messageObject2.messageOwner;
                            instance2.addRecentSticker(0, messageObject2, messageOwner2.media.document, messageOwner2.date, false);
                        }
                    }
                    int n8 = n5;
                    if (messageObject2.isOut()) {
                        n8 = n5;
                        if (messageObject2.isSent()) {
                            n8 = 1;
                        }
                    }
                    ++i;
                    messageObject = messageObject3;
                    n4 = n7;
                    j = n6;
                    n5 = n8;
                }
                DataQuery.getInstance(this.currentAccount).loadReplyMessagesForMessages(list, random_id);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didReceiveNewMessages, random_id, list);
                if (messageObject == null) {
                    return;
                }
                final TLRPC.TL_dialog tl_dialog = (TLRPC.TL_dialog)this.dialogs_dict.get(random_id);
                if (messageObject.messageOwner.action instanceof TLRPC.TL_messageActionChatMigrateTo) {
                    if (tl_dialog != null) {
                        this.allDialogs.remove(tl_dialog);
                        this.dialogsServerOnly.remove(tl_dialog);
                        this.dialogsCanAddUsers.remove(tl_dialog);
                        this.dialogsChannelsOnly.remove(tl_dialog);
                        this.dialogsGroupsOnly.remove(tl_dialog);
                        this.dialogsUsersOnly.remove(tl_dialog);
                        this.dialogsForward.remove(tl_dialog);
                        this.dialogs_dict.remove(tl_dialog.id);
                        this.dialogs_read_inbox_max.remove(tl_dialog.id);
                        this.dialogs_read_outbox_max.remove(tl_dialog.id);
                        final int value = this.nextDialogsCacheOffset.get(tl_dialog.folder_id, 0);
                        if (value > 0) {
                            this.nextDialogsCacheOffset.put(tl_dialog.folder_id, value - 1);
                        }
                        this.dialogMessage.remove(tl_dialog.id);
                        final ArrayList list2 = (ArrayList)this.dialogsByFolder.get(tl_dialog.folder_id);
                        if (list2 != null) {
                            list2.remove(tl_dialog);
                        }
                        final MessageObject messageObject4 = (MessageObject)this.dialogMessagesByIds.get(tl_dialog.top_message);
                        this.dialogMessagesByIds.remove(tl_dialog.top_message);
                        if (messageObject4 != null) {
                            random_id = messageObject4.messageOwner.random_id;
                            if (random_id != 0L) {
                                this.dialogMessagesByRandomIds.remove(random_id);
                            }
                        }
                        tl_dialog.top_message = 0;
                        NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(tl_dialog.id);
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needReloadRecentDialogsSearch, new Object[0]);
                    }
                    return;
                }
                int n9 = 0;
                Label_1343: {
                    if (tl_dialog == null) {
                        n9 = n2;
                        if (!b) {
                            final TLRPC.Chat chat = this.getChat(j);
                            if ((j != 0 && chat == null) || (chat != null && chat.left)) {
                                return;
                            }
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("not found dialog with id ");
                                sb.append(random_id);
                                sb.append(" dictCount = ");
                                sb.append(this.dialogs_dict.size());
                                sb.append(" allCount = ");
                                sb.append(this.allDialogs.size());
                                FileLog.d(sb.toString());
                            }
                            final TLRPC.TL_dialog e = new TLRPC.TL_dialog();
                            e.id = random_id;
                            e.unread_count = 0;
                            e.top_message = messageObject.getId();
                            e.last_message_date = messageObject.messageOwner.date;
                            e.flags = (ChatObject.isChannel(chat) ? 1 : 0);
                            this.dialogs_dict.put(random_id, (Object)e);
                            this.allDialogs.add(e);
                            this.dialogMessage.put(random_id, (Object)messageObject);
                            if (messageObject.messageOwner.to_id.channel_id == 0) {
                                this.dialogMessagesByIds.put(messageObject.getId(), (Object)messageObject);
                                final long random_id2 = messageObject.messageOwner.random_id;
                                if (random_id2 != 0L) {
                                    this.dialogMessagesByRandomIds.put(random_id2, (Object)messageObject);
                                }
                            }
                            MessagesStorage.getInstance(this.currentAccount).getDialogFolderId(random_id, (MessagesStorage.IntCallback)new _$$Lambda$MessagesController$UGzhyEr_7mVrPzKWG0QmrzUROSg(this, e, random_id));
                            n9 = 1;
                        }
                    }
                    else {
                        int n10;
                        if (n4 != 0) {
                            n10 = n3;
                            if (tl_dialog.folder_id == 1) {
                                n10 = n3;
                                if (!this.isDialogMuted(tl_dialog.id)) {
                                    tl_dialog.folder_id = 0;
                                    tl_dialog.pinned = false;
                                    tl_dialog.pinnedNum = 0;
                                    MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId(null, null, tl_dialog.id, 0);
                                    n10 = 1;
                                }
                            }
                        }
                        else {
                            n10 = n3;
                        }
                        final int n11 = 1;
                        if ((tl_dialog.top_message <= 0 || messageObject.getId() <= 0 || messageObject.getId() <= tl_dialog.top_message) && (tl_dialog.top_message >= 0 || messageObject.getId() >= 0 || messageObject.getId() >= tl_dialog.top_message) && this.dialogMessage.indexOfKey(random_id) >= 0 && tl_dialog.top_message >= 0) {
                            n9 = n10;
                            if (tl_dialog.last_message_date > messageObject.messageOwner.date) {
                                break Label_1343;
                            }
                        }
                        final MessageObject messageObject5 = (MessageObject)this.dialogMessagesByIds.get(tl_dialog.top_message);
                        this.dialogMessagesByIds.remove(tl_dialog.top_message);
                        if (messageObject5 != null) {
                            final long random_id3 = messageObject5.messageOwner.random_id;
                            if (random_id3 != 0L) {
                                this.dialogMessagesByRandomIds.remove(random_id3);
                            }
                        }
                        tl_dialog.top_message = messageObject.getId();
                        if (!b) {
                            tl_dialog.last_message_date = messageObject.messageOwner.date;
                            n9 = n11;
                        }
                        else {
                            n9 = n10;
                        }
                        this.dialogMessage.put(random_id, (Object)messageObject);
                        if (messageObject.messageOwner.to_id.channel_id == 0) {
                            this.dialogMessagesByIds.put(messageObject.getId(), (Object)messageObject);
                            final long random_id4 = messageObject.messageOwner.random_id;
                            if (random_id4 != 0L) {
                                this.dialogMessagesByRandomIds.put(random_id4, (Object)messageObject);
                            }
                        }
                    }
                }
                if (n9 != 0) {
                    this.sortDialogs(null);
                }
                if (n5 != 0) {
                    DataQuery.getInstance(this.currentAccount).increasePeerRaiting(random_id);
                }
            }
        }
    }
    
    public void updateTimerProc() {
        final long currentTimeMillis = System.currentTimeMillis();
        this.checkDeletingTask(false);
        this.checkReadTasks();
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            if (ConnectionsManager.getInstance(this.currentAccount).getPauseTime() == 0L && ApplicationLoader.isScreenOn && !ApplicationLoader.mainInterfacePausedStageQueue) {
                if (ApplicationLoader.mainInterfacePausedStageQueueTime != 0L && Math.abs(ApplicationLoader.mainInterfacePausedStageQueueTime - System.currentTimeMillis()) > 1000L && this.statusSettingState != 1 && (this.lastStatusUpdateTime == 0L || Math.abs(System.currentTimeMillis() - this.lastStatusUpdateTime) >= 55000L || this.offlineSent)) {
                    this.statusSettingState = 1;
                    if (this.statusRequest != 0) {
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.statusRequest, true);
                    }
                    final TLRPC.TL_account_updateStatus tl_account_updateStatus = new TLRPC.TL_account_updateStatus();
                    tl_account_updateStatus.offline = false;
                    this.statusRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_updateStatus, new _$$Lambda$MessagesController$cfQiiK2gKJLj20bAsG92QXAhxfU(this));
                }
            }
            else if (this.statusSettingState != 2 && !this.offlineSent && Math.abs(System.currentTimeMillis() - ConnectionsManager.getInstance(this.currentAccount).getPauseTime()) >= 2000L) {
                this.statusSettingState = 2;
                if (this.statusRequest != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.statusRequest, true);
                }
                final TLRPC.TL_account_updateStatus tl_account_updateStatus2 = new TLRPC.TL_account_updateStatus();
                tl_account_updateStatus2.offline = true;
                this.statusRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_updateStatus2, new _$$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY(this));
            }
            if (this.updatesQueueChannels.size() != 0) {
                for (int i = 0; i < this.updatesQueueChannels.size(); ++i) {
                    final int key = this.updatesQueueChannels.keyAt(i);
                    if (this.updatesStartWaitTimeChannels.valueAt(i) + 1500L < currentTimeMillis) {
                        if (BuildVars.LOGS_ENABLED) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("QUEUE CHANNEL ");
                            sb.append(key);
                            sb.append(" UPDATES WAIT TIMEOUT - CHECK QUEUE");
                            FileLog.d(sb.toString());
                        }
                        this.processChannelsUpdatesQueue(key, 0);
                    }
                }
            }
            for (int j = 0; j < 3; ++j) {
                if (this.getUpdatesStartTime(j) != 0L && this.getUpdatesStartTime(j) + 1500L < currentTimeMillis) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(j);
                        sb2.append(" QUEUE UPDATES WAIT TIMEOUT - CHECK QUEUE");
                        FileLog.d(sb2.toString());
                    }
                    this.processUpdatesQueue(j, 0);
                }
            }
        }
        if (Math.abs(System.currentTimeMillis() - this.lastViewsCheckTime) >= 5000L) {
            this.lastViewsCheckTime = System.currentTimeMillis();
            if (this.channelViewsToSend.size() != 0) {
                for (int k = 0; k < this.channelViewsToSend.size(); ++k) {
                    final int key2 = this.channelViewsToSend.keyAt(k);
                    final TLRPC.TL_messages_getMessagesViews tl_messages_getMessagesViews = new TLRPC.TL_messages_getMessagesViews();
                    tl_messages_getMessagesViews.peer = this.getInputPeer(key2);
                    tl_messages_getMessagesViews.id = (ArrayList<Integer>)this.channelViewsToSend.valueAt(k);
                    tl_messages_getMessagesViews.increment = (k == 0);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getMessagesViews, new _$$Lambda$MessagesController$zcPVttRNdPhRMvGP9lvTiB9rsK8(this, key2, tl_messages_getMessagesViews));
                }
                this.channelViewsToSend.clear();
            }
            if (this.pollsToCheckSize > 0) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$EvGoS6VQaul799anwg65luJP3kM(this));
            }
        }
        if (!this.onlinePrivacy.isEmpty()) {
            ArrayList<Integer> list = null;
            final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            for (final Map.Entry<Integer, Integer> entry : this.onlinePrivacy.entrySet()) {
                if (entry.getValue() < currentTime - 30) {
                    ArrayList<Integer> list2;
                    if ((list2 = list) == null) {
                        list2 = new ArrayList<Integer>();
                    }
                    list2.add(entry.getKey());
                    list = list2;
                }
            }
            if (list != null) {
                final Iterator<Integer> iterator2 = list.iterator();
                while (iterator2.hasNext()) {
                    this.onlinePrivacy.remove(iterator2.next());
                }
                AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$tRF6Ij6nWDh57hakeYO_dBj2yNg(this));
            }
        }
        if (this.shortPollChannels.size() != 0) {
            int n;
            for (int l = 0; l < this.shortPollChannels.size(); l = n + 1) {
                final int key3 = this.shortPollChannels.keyAt(l);
                n = l;
                if (this.shortPollChannels.valueAt(l) < System.currentTimeMillis() / 1000L) {
                    this.shortPollChannels.delete(key3);
                    n = --l;
                    if (this.needShortPollChannels.indexOfKey(key3) >= 0) {
                        this.getChannelDifference(key3);
                        n = l;
                    }
                }
            }
        }
        if (this.shortPollOnlines.size() != 0) {
            final long n2 = SystemClock.uptimeMillis() / 1000L;
            int n4;
            for (int n3 = 0; n3 < this.shortPollOnlines.size(); n3 = n4 + 1) {
                final int key4 = this.shortPollOnlines.keyAt(n3);
                n4 = n3;
                if (this.shortPollOnlines.valueAt(n3) < n2) {
                    if (this.needShortPollChannels.indexOfKey(key4) >= 0) {
                        this.shortPollOnlines.put(key4, (int)(300L + n2));
                    }
                    else {
                        this.shortPollOnlines.delete(key4);
                        --n3;
                    }
                    final TLRPC.TL_messages_getOnlines tl_messages_getOnlines = new TLRPC.TL_messages_getOnlines();
                    tl_messages_getOnlines.peer = this.getInputPeer(-key4);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_messages_getOnlines, new _$$Lambda$MessagesController$sjofkBeH5gGrYjjT8WNKFtTfESE(this, key4));
                    n4 = n3;
                }
            }
        }
        if (!this.printingUsers.isEmpty() || this.lastPrintingStringCount != this.printingUsers.size()) {
            final ArrayList<Long> list3 = new ArrayList<Long>(this.printingUsers.keySet());
            int n5 = 0;
            int n6 = 0;
            while (n5 < list3.size()) {
                final long longValue = list3.get(n5);
                final ArrayList<PrintingUser> list4 = this.printingUsers.get(longValue);
                if (list4 != null) {
                    final int n7 = 0;
                    int n8 = n6;
                    int n10;
                    for (int index = n7; index < list4.size(); index = n10 + 1) {
                        final PrintingUser o = list4.get(index);
                        int n9;
                        if (o.action instanceof TLRPC.TL_sendMessageGamePlayAction) {
                            n9 = 30000;
                        }
                        else {
                            n9 = 5900;
                        }
                        n10 = index;
                        if (o.lastTime + n9 < currentTimeMillis) {
                            list4.remove(o);
                            n10 = index - 1;
                            n8 = 1;
                        }
                    }
                    n6 = n8;
                }
                if (list4 == null || list4.isEmpty()) {
                    this.printingUsers.remove(longValue);
                    list3.remove(n5);
                    --n5;
                }
                ++n5;
            }
            this.updatePrintingStrings();
            if (n6 != 0) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$NCgFAuLO8OXQTI8IHmqXB1TzYHE(this));
            }
        }
        if (Theme.selectedAutoNightType == 1 && Math.abs(currentTimeMillis - MessagesController.lastThemeCheckTime) >= 60L) {
            AndroidUtilities.runOnUIThread(this.themeCheckRunnable);
            MessagesController.lastThemeCheckTime = currentTimeMillis;
        }
        if (UserConfig.getInstance(this.currentAccount).savedPasswordHash != null && Math.abs(currentTimeMillis - MessagesController.lastPasswordCheckTime) >= 60L) {
            AndroidUtilities.runOnUIThread(this.passwordCheckRunnable);
            MessagesController.lastPasswordCheckTime = currentTimeMillis;
        }
        LocationController.getInstance(this.currentAccount).update();
        this.checkProxyInfoInternal(false);
        this.checkTosUpdate();
    }
    
    public void uploadAndApplyUserAvatar(final TLRPC.FileLocation fileLocation) {
        if (fileLocation == null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(FileLoader.getDirectory(4));
        sb.append("/");
        sb.append(fileLocation.volume_id);
        sb.append("_");
        sb.append(fileLocation.local_id);
        sb.append(".jpg");
        this.uploadingAvatar = sb.toString();
        FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingAvatar, false, true, 16777216);
    }
    
    public static class PrintingUser
    {
        public TLRPC.SendMessageAction action;
        public long lastTime;
        public int userId;
    }
    
    private class ReadTask
    {
        public long dialogId;
        public int maxDate;
        public int maxId;
        public long sendRequestTime;
    }
    
    private class UserActionUpdatesPts extends Updates
    {
    }
    
    private class UserActionUpdatesSeq extends Updates
    {
    }
}
