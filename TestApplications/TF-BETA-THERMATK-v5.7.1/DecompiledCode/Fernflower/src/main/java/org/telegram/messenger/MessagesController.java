package org.telegram.messenger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.widget.Toast;
import androidx.core.app.NotificationManagerCompat;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;

public class MessagesController implements NotificationCenter.NotificationCenterDelegate {
   private static volatile MessagesController[] Instance = new MessagesController[3];
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
   protected ArrayList allDialogs;
   public int availableMapProviders;
   public boolean blockedCountry;
   public SparseIntArray blockedUsers;
   public int callConnectTimeout;
   public int callPacketTimeout;
   public int callReceiveTimeout;
   public int callRingTimeout;
   public boolean canRevokePmInbox;
   private SparseArray channelAdmins;
   private SparseArray channelViewsToSend;
   private SparseIntArray channelsPts;
   private ConcurrentHashMap chats;
   private SparseBooleanArray checkingLastMessagesDialogs;
   private boolean checkingProxyInfo;
   private int checkingProxyInfoRequestId;
   private boolean checkingTosUpdate;
   private LongSparseArray clearingHistoryDialogs;
   private ArrayList createdDialogIds;
   private ArrayList createdDialogMainThreadIds;
   private int currentAccount;
   private Runnable currentDeleteTaskRunnable;
   private int currentDeletingTaskChannelId;
   private ArrayList currentDeletingTaskMids;
   private int currentDeletingTaskTime;
   public String dcDomainName;
   public boolean defaultP2pContacts;
   public LongSparseArray deletedHistory;
   private LongSparseArray deletingDialogs;
   private final Comparator dialogComparator;
   public LongSparseArray dialogMessage;
   public SparseArray dialogMessagesByIds;
   public LongSparseArray dialogMessagesByRandomIds;
   private SparseArray dialogsByFolder;
   public ArrayList dialogsCanAddUsers;
   public ArrayList dialogsChannelsOnly;
   private SparseBooleanArray dialogsEndReached;
   public ArrayList dialogsForward;
   public ArrayList dialogsGroupsOnly;
   private boolean dialogsInTransaction;
   public boolean dialogsLoaded;
   public ArrayList dialogsServerOnly;
   public ArrayList dialogsUsersOnly;
   public LongSparseArray dialogs_dict;
   public ConcurrentHashMap dialogs_read_inbox_max;
   public ConcurrentHashMap dialogs_read_outbox_max;
   private SharedPreferences emojiPreferences;
   public boolean enableJoined;
   private ConcurrentHashMap encryptedChats;
   private SparseArray exportedChats;
   public boolean firstGettingTask;
   private SparseArray fullChats;
   private SparseArray fullUsers;
   private boolean getDifferenceFirstSync;
   public boolean gettingDifference;
   private SparseBooleanArray gettingDifferenceChannels;
   private boolean gettingNewDeleteTask;
   private SparseBooleanArray gettingUnknownChannels;
   private LongSparseArray gettingUnknownDialogs;
   public String gifSearchBot;
   public ArrayList hintDialogs;
   public String imageSearchBot;
   private String installReferer;
   private boolean isLeftProxyChannel;
   private ArrayList joiningToChannels;
   private int lastPrintingStringCount;
   private long lastPushRegisterSendTime;
   private long lastStatusUpdateTime;
   private long lastViewsCheckTime;
   public String linkPrefix;
   private ArrayList loadedFullChats;
   private ArrayList loadedFullParticipants;
   private ArrayList loadedFullUsers;
   public boolean loadingBlockedUsers;
   private SparseIntArray loadingChannelAdmins;
   private SparseBooleanArray loadingDialogs;
   private ArrayList loadingFullChats;
   private ArrayList loadingFullParticipants;
   private ArrayList loadingFullUsers;
   private int loadingNotificationSettings;
   private boolean loadingNotificationSignUpSettings;
   private LongSparseArray loadingPeerSettings;
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
   private ConcurrentHashMap objectsByUsernames;
   private boolean offlineSent;
   public ConcurrentHashMap onlinePrivacy;
   private Runnable passwordCheckRunnable;
   private LongSparseArray pollsToCheck;
   private int pollsToCheckSize;
   public boolean preloadFeaturedStickers;
   public LongSparseArray printingStrings;
   public LongSparseArray printingStringsTypes;
   public ConcurrentHashMap printingUsers;
   private TLRPC.Dialog proxyDialog;
   private String proxyDialogAddress;
   private long proxyDialogId;
   public int ratingDecay;
   private ArrayList readTasks;
   private LongSparseArray readTasksMap;
   public boolean registeringForPush;
   private LongSparseArray reloadingMessages;
   private HashMap reloadingWebpages;
   private LongSparseArray reloadingWebpagesPending;
   private TLRPC.messages_Dialogs resetDialogsAll;
   private TLRPC.TL_messages_peerDialogs resetDialogsPinned;
   private boolean resetingDialogs;
   public int revokeTimeLimit;
   public int revokeTimePmLimit;
   public int secretWebpagePreview;
   public SparseArray sendingTypings;
   private SparseBooleanArray serverDialogsEndReached;
   private SparseIntArray shortPollChannels;
   private SparseIntArray shortPollOnlines;
   private int statusRequest;
   private int statusSettingState;
   public boolean suggestContacts;
   public String suggestedLangCode;
   private Runnable themeCheckRunnable;
   public int unreadUnmutedDialogs;
   private final Comparator updatesComparator;
   private SparseArray updatesQueueChannels;
   private ArrayList updatesQueuePts;
   private ArrayList updatesQueueQts;
   private ArrayList updatesQueueSeq;
   private SparseLongArray updatesStartWaitTimeChannels;
   private long updatesStartWaitTimePts;
   private long updatesStartWaitTimeQts;
   private long updatesStartWaitTimeSeq;
   public boolean updatingState;
   private String uploadingAvatar;
   private String uploadingWallpaper;
   private boolean uploadingWallpaperBlurred;
   private boolean uploadingWallpaperMotion;
   private ConcurrentHashMap users;
   public String venueSearchBot;
   private ArrayList visibleDialogMainThreadIds;
   public int webFileDatacenterId;

   public MessagesController(int var1) {
      byte var2 = 2;
      this.chats = new ConcurrentHashMap(100, 1.0F, 2);
      this.encryptedChats = new ConcurrentHashMap(10, 1.0F, 2);
      this.users = new ConcurrentHashMap(100, 1.0F, 2);
      this.objectsByUsernames = new ConcurrentHashMap(100, 1.0F, 2);
      this.joiningToChannels = new ArrayList();
      this.exportedChats = new SparseArray();
      this.hintDialogs = new ArrayList();
      this.dialogsByFolder = new SparseArray();
      this.allDialogs = new ArrayList();
      this.dialogsForward = new ArrayList();
      this.dialogsServerOnly = new ArrayList();
      this.dialogsCanAddUsers = new ArrayList();
      this.dialogsChannelsOnly = new ArrayList();
      this.dialogsUsersOnly = new ArrayList();
      this.dialogsGroupsOnly = new ArrayList();
      this.dialogs_read_inbox_max = new ConcurrentHashMap(100, 1.0F, 2);
      this.dialogs_read_outbox_max = new ConcurrentHashMap(100, 1.0F, 2);
      this.dialogs_dict = new LongSparseArray();
      this.dialogMessage = new LongSparseArray();
      this.dialogMessagesByRandomIds = new LongSparseArray();
      this.deletedHistory = new LongSparseArray();
      this.dialogMessagesByIds = new SparseArray();
      this.printingUsers = new ConcurrentHashMap(20, 1.0F, 2);
      this.printingStrings = new LongSparseArray();
      this.printingStringsTypes = new LongSparseArray();
      this.sendingTypings = new SparseArray();
      this.onlinePrivacy = new ConcurrentHashMap(20, 1.0F, 2);
      this.loadingPeerSettings = new LongSparseArray();
      this.createdDialogIds = new ArrayList();
      this.createdDialogMainThreadIds = new ArrayList();
      this.visibleDialogMainThreadIds = new ArrayList();
      this.shortPollChannels = new SparseIntArray();
      this.needShortPollChannels = new SparseIntArray();
      this.shortPollOnlines = new SparseIntArray();
      this.needShortPollOnlines = new SparseIntArray();
      this.deletingDialogs = new LongSparseArray();
      this.clearingHistoryDialogs = new LongSparseArray();
      this.loadingBlockedUsers = false;
      this.blockedUsers = new SparseIntArray();
      this.channelViewsToSend = new SparseArray();
      this.pollsToCheck = new LongSparseArray();
      this.updatesQueueChannels = new SparseArray();
      this.updatesStartWaitTimeChannels = new SparseLongArray();
      this.channelsPts = new SparseIntArray();
      this.gettingDifferenceChannels = new SparseBooleanArray();
      this.gettingUnknownChannels = new SparseBooleanArray();
      this.gettingUnknownDialogs = new LongSparseArray();
      this.checkingLastMessagesDialogs = new SparseBooleanArray();
      this.updatesQueueSeq = new ArrayList();
      this.updatesQueuePts = new ArrayList();
      this.updatesQueueQts = new ArrayList();
      this.fullUsers = new SparseArray();
      this.fullChats = new SparseArray();
      this.loadingFullUsers = new ArrayList();
      this.loadedFullUsers = new ArrayList();
      this.loadingFullChats = new ArrayList();
      this.loadingFullParticipants = new ArrayList();
      this.loadedFullParticipants = new ArrayList();
      this.loadedFullChats = new ArrayList();
      this.channelAdmins = new SparseArray();
      this.loadingChannelAdmins = new SparseIntArray();
      this.migratedChats = new SparseIntArray();
      this.reloadingWebpages = new HashMap();
      this.reloadingWebpagesPending = new LongSparseArray();
      this.reloadingMessages = new LongSparseArray();
      this.readTasks = new ArrayList();
      this.readTasksMap = new LongSparseArray();
      this.nextDialogsCacheOffset = new SparseIntArray();
      this.loadingDialogs = new SparseBooleanArray();
      this.dialogsEndReached = new SparseBooleanArray();
      this.serverDialogsEndReached = new SparseBooleanArray();
      this.getDifferenceFirstSync = true;
      this.loadingPinnedDialogs = new SparseIntArray();
      this.suggestContacts = true;
      this.themeCheckRunnable = _$$Lambda$RQB0Jwr1FTqp6hrbGUHuOs_9k1I.INSTANCE;
      this.passwordCheckRunnable = new Runnable() {
         public void run() {
            UserConfig.getInstance(MessagesController.this.currentAccount).checkSavedPassword();
         }
      };
      this.maxBroadcastCount = 100;
      this.minGroupConvertSize = 200;
      this.dialogComparator = new Comparator() {
         public int compare(TLRPC.Dialog var1, TLRPC.Dialog var2) {
            boolean var3 = var1 instanceof TLRPC.TL_dialogFolder;
            if (var3 && !(var2 instanceof TLRPC.TL_dialogFolder)) {
               return -1;
            } else if (!var3 && var2 instanceof TLRPC.TL_dialogFolder) {
               return 1;
            } else if (!var1.pinned && var2.pinned) {
               return 1;
            } else if (var1.pinned && !var2.pinned) {
               return -1;
            } else {
               int var4;
               int var5;
               if (var1.pinned && var2.pinned) {
                  var4 = var1.pinnedNum;
                  var5 = var2.pinnedNum;
                  if (var4 < var5) {
                     return 1;
                  } else {
                     return var4 > var5 ? -1 : 0;
                  }
               } else {
                  label60: {
                     TLRPC.DraftMessage var6 = DataQuery.getInstance(MessagesController.this.currentAccount).getDraft(var1.id);
                     if (var6 != null) {
                        var5 = var6.date;
                        if (var5 >= var1.last_message_date) {
                           break label60;
                        }
                     }

                     var5 = var1.last_message_date;
                  }

                  label55: {
                     TLRPC.DraftMessage var7 = DataQuery.getInstance(MessagesController.this.currentAccount).getDraft(var2.id);
                     if (var7 != null) {
                        var4 = var7.date;
                        if (var4 >= var2.last_message_date) {
                           break label55;
                        }
                     }

                     var4 = var2.last_message_date;
                  }

                  if (var5 < var4) {
                     return 1;
                  } else {
                     return var5 > var4 ? -1 : 0;
                  }
               }
            }
         }
      };
      this.updatesComparator = new _$$Lambda$MessagesController$jdY_gZP_eY5WAh_nSc2UhUqpq4M(this);
      this.DIALOGS_LOAD_TYPE_CACHE = 1;
      this.DIALOGS_LOAD_TYPE_CHANNEL = 2;
      this.DIALOGS_LOAD_TYPE_UNKNOWN = 3;
      this.currentAccount = var1;
      ImageLoader.getInstance();
      MessagesStorage.getInstance(this.currentAccount);
      LocationController.getInstance(this.currentAccount);
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$y42O__dKOKyzNi5k5aHwMy0RjOs(this));
      this.addSupportUser();
      if (this.currentAccount == 0) {
         this.notificationsPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
         this.mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
         this.emojiPreferences = ApplicationLoader.applicationContext.getSharedPreferences("emoji", 0);
      } else {
         Context var3 = ApplicationLoader.applicationContext;
         StringBuilder var4 = new StringBuilder();
         var4.append("Notifications");
         var4.append(this.currentAccount);
         this.notificationsPreferences = var3.getSharedPreferences(var4.toString(), 0);
         Context var8 = ApplicationLoader.applicationContext;
         StringBuilder var6 = new StringBuilder();
         var6.append("mainconfig");
         var6.append(this.currentAccount);
         this.mainPreferences = var8.getSharedPreferences(var6.toString(), 0);
         var3 = ApplicationLoader.applicationContext;
         var4 = new StringBuilder();
         var4.append("emoji");
         var4.append(this.currentAccount);
         this.emojiPreferences = var3.getSharedPreferences(var4.toString(), 0);
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
      SharedPreferences var9 = this.mainPreferences;
      String var7;
      if (ConnectionsManager.native_isTestBackend(this.currentAccount) != 0) {
         var7 = "tapv2.stel.com";
      } else {
         var7 = "apv2.stel.com";
      }

      this.dcDomainName = var9.getString("dcDomainName", var7);
      SharedPreferences var10 = this.mainPreferences;
      byte var5;
      if (ConnectionsManager.native_isTestBackend(this.currentAccount) != 0) {
         var5 = var2;
      } else {
         var5 = 4;
      }

      this.webFileDatacenterId = var10.getInt("webFileDatacenterId", var5);
      this.suggestedLangCode = this.mainPreferences.getString("suggestedLangCode", "en");
   }

   private void addDialogToItsFolder(int var1, TLRPC.Dialog var2, boolean var3) {
      int var4;
      if (var2 instanceof TLRPC.TL_dialogFolder) {
         var2.unread_count = 0;
         var2.unread_mentions_count = 0;
         var4 = 0;
      } else {
         var4 = var2.folder_id;
      }

      ArrayList var5 = (ArrayList)this.dialogsByFolder.get(var4);
      ArrayList var6 = var5;
      if (var5 == null) {
         var6 = new ArrayList();
         this.dialogsByFolder.put(var4, var6);
      }

      if (var4 != 0 && var2.unread_count != 0) {
         TLRPC.Dialog var7 = (TLRPC.Dialog)this.dialogs_dict.get(DialogObject.makeFolderDialogId(var4));
         if (var7 != null) {
            if (var3) {
               if (this.isDialogMuted(var2.id)) {
                  var7.unread_count += var2.unread_count;
               } else {
                  var7.unread_mentions_count += var2.unread_count;
               }
            } else if (this.isDialogMuted(var2.id)) {
               ++var7.unread_count;
            } else {
               ++var7.unread_mentions_count;
            }
         }
      }

      if (var1 == -1) {
         var6.add(var2);
      } else if (var1 == -2) {
         if (!var6.isEmpty() && var6.get(0) instanceof TLRPC.TL_dialogFolder) {
            var6.add(1, var2);
         } else {
            var6.add(0, var2);
         }
      } else {
         var6.add(var1, var2);
      }

   }

   private void applyDialogNotificationsSettings(long var1, TLRPC.PeerNotifySettings var3) {
      if (var3 != null) {
         SharedPreferences var4 = this.notificationsPreferences;
         StringBuilder var5 = new StringBuilder();
         var5.append("notify2_");
         var5.append(var1);
         int var6 = var4.getInt(var5.toString(), -1);
         var4 = this.notificationsPreferences;
         var5 = new StringBuilder();
         var5.append("notifyuntil_");
         var5.append(var1);
         int var7 = var4.getInt(var5.toString(), 0);
         Editor var16 = this.notificationsPreferences.edit();
         TLRPC.Dialog var15 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
         if (var15 != null) {
            var15.notify_settings = var3;
         }

         StringBuilder var8;
         if ((var3.flags & 2) != 0) {
            var8 = new StringBuilder();
            var8.append("silent_");
            var8.append(var1);
            var16.putBoolean(var8.toString(), var3.silent);
         } else {
            var8 = new StringBuilder();
            var8.append("silent_");
            var8.append(var1);
            var16.remove(var8.toString());
         }

         int var9 = var3.flags;
         boolean var10 = true;
         boolean var11 = true;
         boolean var12 = true;
         boolean var13 = true;
         StringBuilder var14;
         if ((var9 & 4) != 0) {
            if (var3.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
               int var17;
               if (var3.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                  if (var6 != 2) {
                     var14 = new StringBuilder();
                     var14.append("notify2_");
                     var14.append(var1);
                     var16.putInt(var14.toString(), 2);
                     if (var15 != null) {
                        var15.notify_settings.mute_until = Integer.MAX_VALUE;
                     }

                     var17 = 0;
                  } else {
                     var17 = 0;
                     var10 = false;
                  }
               } else {
                  if (var6 == 3 && var7 == var3.mute_until) {
                     var10 = false;
                  } else {
                     var8 = new StringBuilder();
                     var8.append("notify2_");
                     var8.append(var1);
                     var16.putInt(var8.toString(), 3);
                     var8 = new StringBuilder();
                     var8.append("notifyuntil_");
                     var8.append(var1);
                     var16.putInt(var8.toString(), var3.mute_until);
                     var10 = var13;
                     if (var15 != null) {
                        var15.notify_settings.mute_until = 0;
                        var10 = var13;
                     }
                  }

                  var17 = var3.mute_until;
               }

               MessagesStorage.getInstance(this.currentAccount).setDialogFlags(var1, (long)var17 << 32 | 1L);
               NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(var1);
            } else {
               if (var6 != 0 && var6 != 1) {
                  if (var15 != null) {
                     var15.notify_settings.mute_until = 0;
                  }

                  var14 = new StringBuilder();
                  var14.append("notify2_");
                  var14.append(var1);
                  var16.putInt(var14.toString(), 0);
                  var10 = var11;
               } else {
                  var10 = false;
               }

               MessagesStorage.getInstance(this.currentAccount).setDialogFlags(var1, 0L);
            }
         } else {
            if (var6 != -1) {
               if (var15 != null) {
                  var15.notify_settings.mute_until = 0;
               }

               var14 = new StringBuilder();
               var14.append("notify2_");
               var14.append(var1);
               var16.remove(var14.toString());
               var10 = var12;
            } else {
               var10 = false;
            }

            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(var1, 0L);
         }

         var16.commit();
         if (var10) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated);
         }

      }
   }

   private void applyDialogsNotificationsSettings(ArrayList var1) {
      Editor var2 = null;

      Editor var5;
      for(int var3 = 0; var3 < var1.size(); var2 = var5) {
         TLRPC.Dialog var4 = (TLRPC.Dialog)var1.get(var3);
         var5 = var2;
         if (var4.peer != null) {
            var5 = var2;
            if (var4.notify_settings instanceof TLRPC.TL_peerNotifySettings) {
               var5 = var2;
               if (var2 == null) {
                  var5 = this.notificationsPreferences.edit();
               }

               TLRPC.Peer var7 = var4.peer;
               int var6 = var7.user_id;
               if (var6 == 0) {
                  var6 = var7.chat_id;
                  if (var6 != 0) {
                     var6 = -var6;
                  } else {
                     var6 = -var7.channel_id;
                  }
               }

               StringBuilder var8;
               if ((var4.notify_settings.flags & 2) != 0) {
                  var8 = new StringBuilder();
                  var8.append("silent_");
                  var8.append(var6);
                  var5.putBoolean(var8.toString(), var4.notify_settings.silent);
               } else {
                  var8 = new StringBuilder();
                  var8.append("silent_");
                  var8.append(var6);
                  var5.remove(var8.toString());
               }

               TLRPC.PeerNotifySettings var9 = var4.notify_settings;
               if ((var9.flags & 4) != 0) {
                  if (var9.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                     if (var4.notify_settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                        var8 = new StringBuilder();
                        var8.append("notify2_");
                        var8.append(var6);
                        var5.putInt(var8.toString(), 2);
                        var4.notify_settings.mute_until = Integer.MAX_VALUE;
                     } else {
                        var8 = new StringBuilder();
                        var8.append("notify2_");
                        var8.append(var6);
                        var5.putInt(var8.toString(), 3);
                        var8 = new StringBuilder();
                        var8.append("notifyuntil_");
                        var8.append(var6);
                        var5.putInt(var8.toString(), var4.notify_settings.mute_until);
                     }
                  } else {
                     var8 = new StringBuilder();
                     var8.append("notify2_");
                     var8.append(var6);
                     var5.putInt(var8.toString(), 0);
                  }
               } else {
                  var8 = new StringBuilder();
                  var8.append("notify2_");
                  var8.append(var6);
                  var5.remove(var8.toString());
               }
            }
         }

         ++var3;
      }

      if (var2 != null) {
         var2.commit();
      }

   }

   private void checkChannelError(String var1, int var2) {
      byte var4;
      label34: {
         int var3 = var1.hashCode();
         if (var3 != -1809401834) {
            if (var3 != -795226617) {
               if (var3 == -471086771 && var1.equals("CHANNEL_PUBLIC_GROUP_NA")) {
                  var4 = 1;
                  break label34;
               }
            } else if (var1.equals("CHANNEL_PRIVATE")) {
               var4 = 0;
               break label34;
            }
         } else if (var1.equals("USER_BANNED_IN_CHANNEL")) {
            var4 = 2;
            break label34;
         }

         var4 = -1;
      }

      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 == 2) {
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, var2, 2);
            }
         } else {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, var2, 1);
         }
      } else {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, var2, 0);
      }

   }

   private boolean checkDeletingTask(boolean var1) {
      int var2 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
      if (this.currentDeletingTaskMids != null) {
         if (!var1) {
            int var3 = this.currentDeletingTaskTime;
            if (var3 == 0 || var3 > var2) {
               return false;
            }
         }

         this.currentDeletingTaskTime = 0;
         if (this.currentDeleteTaskRunnable != null && !var1) {
            Utilities.stageQueue.cancelRunnable(this.currentDeleteTaskRunnable);
         }

         this.currentDeleteTaskRunnable = null;
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$znYc9uVm8VzO6BhZCUvwobX7s2A(this, new ArrayList(this.currentDeletingTaskMids)));
         return true;
      } else {
         return false;
      }
   }

   private void checkProxyInfoInternal(boolean var1) {
      if (var1 && this.checkingProxyInfo) {
         this.checkingProxyInfo = false;
      }

      if ((var1 || this.nextProxyInfoCheckTime <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) && !this.checkingProxyInfo) {
         if (this.checkingProxyInfoRequestId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkingProxyInfoRequestId, true);
            this.checkingProxyInfoRequestId = 0;
         }

         String var3;
         byte var6;
         String var7;
         label47: {
            SharedPreferences var2 = getGlobalMainSettings();
            var1 = var2.getBoolean("proxy_enabled", false);
            var3 = var2.getString("proxy_ip", "");
            var7 = var2.getString("proxy_secret", "");
            if (this.proxyDialogId != 0L) {
               String var4 = this.proxyDialogAddress;
               if (var4 != null) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append(var3);
                  var5.append(var7);
                  if (!var4.equals(var5.toString())) {
                     var6 = 1;
                     break label47;
                  }
               }
            }

            var6 = 0;
         }

         if (var1 && !TextUtils.isEmpty(var3) && !TextUtils.isEmpty(var7)) {
            this.checkingProxyInfo = true;
            TLRPC.TL_help_getProxyData var8 = new TLRPC.TL_help_getProxyData();
            this.checkingProxyInfoRequestId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$MessagesController$SaWsznydC5Y7TDBVTp_FLPmMJQE(this, var3, var7));
         } else {
            var6 = 2;
         }

         if (var6 != 0) {
            this.proxyDialogId = 0L;
            this.proxyDialogAddress = null;
            getGlobalMainSettings().edit().putLong("proxy_dialog", this.proxyDialogId).remove("proxyDialogAddress").commit();
            this.nextProxyInfoCheckTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 3600;
            if (var6 == 2) {
               this.checkingProxyInfo = false;
               if (this.checkingProxyInfoRequestId != 0) {
                  ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.checkingProxyInfoRequestId, true);
                  this.checkingProxyInfoRequestId = 0;
               }
            }

            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$FvadbQtD8d2glGqiRUodW_Fj5b8(this));
         }

      }
   }

   private void checkReadTasks() {
      long var1 = SystemClock.elapsedRealtime();
      int var3 = this.readTasks.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         MessagesController.ReadTask var5 = (MessagesController.ReadTask)this.readTasks.get(var4);
         if (var5.sendRequestTime <= var1) {
            this.completeReadTask(var5);
            this.readTasks.remove(var4);
            this.readTasksMap.remove(var5.dialogId);
            --var4;
            --var3;
         }
      }

   }

   private void checkTosUpdate() {
      if (this.nextTosCheckTime <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() && !this.checkingTosUpdate && UserConfig.getInstance(this.currentAccount).isClientActivated()) {
         this.checkingTosUpdate = true;
         TLRPC.TL_help_getTermsOfServiceUpdate var1 = new TLRPC.TL_help_getTermsOfServiceUpdate();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$MessagesController$5GO5q_JFS4BEFNcWBCNsIVVD9gY(this));
      }

   }

   private void completeReadTask(MessagesController.ReadTask var1) {
      long var2 = var1.dialogId;
      int var4 = (int)var2;
      int var5 = (int)(var2 >> 32);
      if (var4 != 0) {
         TLRPC.InputPeer var6 = this.getInputPeer(var4);
         Object var9;
         if (var6 instanceof TLRPC.TL_inputPeerChannel) {
            TLRPC.TL_channels_readHistory var7 = new TLRPC.TL_channels_readHistory();
            var7.channel = this.getInputChannel(-var4);
            var7.max_id = var1.maxId;
            var9 = var7;
         } else {
            TLRPC.TL_messages_readHistory var12 = new TLRPC.TL_messages_readHistory();
            var12.peer = var6;
            var12.max_id = var1.maxId;
            var9 = var12;
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var9, new _$$Lambda$MessagesController$f6Fg5cePsPVR4IQG1UUiIG6Ywco(this));
      } else {
         TLRPC.EncryptedChat var13 = this.getEncryptedChat(var5);
         byte[] var10 = var13.auth_key;
         if (var10 != null && var10.length > 1 && var13 instanceof TLRPC.TL_encryptedChat) {
            TLRPC.TL_messages_readEncryptedHistory var8 = new TLRPC.TL_messages_readEncryptedHistory();
            var8.peer = new TLRPC.TL_inputEncryptedChat();
            TLRPC.TL_inputEncryptedChat var11 = var8.peer;
            var11.chat_id = var13.id;
            var11.access_hash = var13.access_hash;
            var8.max_date = var1.maxDate;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, _$$Lambda$MessagesController$mkj69nAVhIC1gtqhKztic36PgeU.INSTANCE);
         }
      }

   }

   private TLRPC.TL_dialogFolder ensureFolderDialogExists(int var1, boolean[] var2) {
      if (var1 == 0) {
         return null;
      } else {
         long var3 = DialogObject.makeFolderDialogId(var1);
         TLRPC.Dialog var5 = (TLRPC.Dialog)this.dialogs_dict.get(var3);
         if (var5 instanceof TLRPC.TL_dialogFolder) {
            if (var2 != null) {
               var2[0] = false;
            }

            return (TLRPC.TL_dialogFolder)var5;
         } else {
            if (var2 != null) {
               var2[0] = true;
            }

            TLRPC.TL_dialogFolder var7 = new TLRPC.TL_dialogFolder();
            var7.id = var3;
            var7.peer = new TLRPC.TL_peerUser();
            var7.folder = new TLRPC.TL_folder();
            TLRPC.TL_folder var8 = var7.folder;
            var8.id = var1;
            var8.title = LocaleController.getString("ArchivedChats", 2131558653);
            var7.pinned = true;
            var1 = 0;

            int var6;
            for(var6 = 0; var1 < this.allDialogs.size(); ++var1) {
               var5 = (TLRPC.Dialog)this.allDialogs.get(var1);
               if (!var5.pinned) {
                  break;
               }

               var6 = Math.max(var5.pinnedNum, var6);
            }

            var7.pinnedNum = var6 + 1;
            TLRPC.TL_messages_dialogs var9 = new TLRPC.TL_messages_dialogs();
            var9.dialogs.add(var7);
            MessagesStorage.getInstance(this.currentAccount).putDialogs(var9, 1);
            this.dialogs_dict.put(var3, var7);
            this.allDialogs.add(0, var7);
            return var7;
         }
      }
   }

   private void fetchFolderInLoadedPinnedDialogs(TLRPC.TL_messages_peerDialogs var1) {
      int var2 = var1.dialogs.size();
      byte var3 = 0;
      byte var4 = 0;

      for(int var5 = 0; var5 < var2; ++var5) {
         TLRPC.Dialog var6 = (TLRPC.Dialog)var1.dialogs.get(var5);
         if (var6 instanceof TLRPC.TL_dialogFolder) {
            TLRPC.TL_dialogFolder var7 = (TLRPC.TL_dialogFolder)var6;
            long var8 = DialogObject.getPeerDialogId(var6.peer);
            if (var7.top_message != 0 && var8 != 0L) {
               var2 = var1.messages.size();

               for(var5 = 0; var5 < var2; ++var5) {
                  TLRPC.Message var10 = (TLRPC.Message)var1.messages.get(var5);
                  if (var8 == MessageObject.getDialogId(var10) && var6.top_message == var10.id) {
                     TLRPC.TL_dialog var17 = new TLRPC.TL_dialog();
                     var17.peer = var6.peer;
                     var17.top_message = var6.top_message;
                     var17.folder_id = var7.folder.id;
                     var17.flags |= 16;
                     var1.dialogs.add(var17);
                     TLRPC.Peer var13 = var6.peer;
                     Object var14;
                     if (var13 instanceof TLRPC.TL_peerChannel) {
                        TLRPC.TL_inputPeerChannel var19 = new TLRPC.TL_inputPeerChannel();
                        var19.channel_id = var6.peer.channel_id;
                        int var11 = var1.chats.size();
                        var5 = var4;

                        while(true) {
                           var14 = var19;
                           if (var5 >= var11) {
                              break;
                           }

                           TLRPC.Chat var16 = (TLRPC.Chat)var1.chats.get(var5);
                           if (var16.id == var19.channel_id) {
                              var19.access_hash = var16.access_hash;
                              var14 = var19;
                              break;
                           }

                           ++var5;
                        }
                     } else if (var13 instanceof TLRPC.TL_peerChat) {
                        var14 = new TLRPC.TL_inputPeerChat();
                        ((TLRPC.InputPeer)var14).chat_id = var6.peer.chat_id;
                     } else {
                        TLRPC.TL_inputPeerUser var18 = new TLRPC.TL_inputPeerUser();
                        var18.user_id = var6.peer.user_id;
                        int var12 = var1.users.size();
                        var5 = var3;

                        while(true) {
                           var14 = var18;
                           if (var5 >= var12) {
                              break;
                           }

                           TLRPC.User var15 = (TLRPC.User)var1.users.get(var5);
                           if (var15.id == var18.user_id) {
                              var18.access_hash = var15.access_hash;
                              var14 = var18;
                              break;
                           }

                           ++var5;
                        }
                     }

                     this.loadUnknownDialog((TLRPC.InputPeer)var14, 0L);
                     return;
                  }
               }

               return;
            }

            var1.dialogs.remove(var7);
         }
      }

   }

   private void getChannelDifference(int var1) {
      this.getChannelDifference(var1, 0, 0L, (TLRPC.InputChannel)null);
   }

   public static SharedPreferences getEmojiSettings(int var0) {
      return getInstance(var0).emojiPreferences;
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

   public static TLRPC.InputChannel getInputChannel(TLRPC.Chat var0) {
      if (!(var0 instanceof TLRPC.TL_channel) && !(var0 instanceof TLRPC.TL_channelForbidden)) {
         return new TLRPC.TL_inputChannelEmpty();
      } else {
         TLRPC.TL_inputChannel var1 = new TLRPC.TL_inputChannel();
         var1.channel_id = var0.id;
         var1.access_hash = var0.access_hash;
         return var1;
      }
   }

   public static MessagesController getInstance(int var0) {
      MessagesController var1 = Instance[var0];
      MessagesController var2 = var1;
      if (var1 == null) {
         synchronized(MessagesController.class){}

         Throwable var10000;
         boolean var10001;
         label216: {
            try {
               var1 = Instance[var0];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            var2 = var1;
            if (var1 == null) {
               MessagesController[] var23;
               try {
                  var23 = Instance;
                  var2 = new MessagesController(var0);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label216;
               }

               var23[var0] = var2;
            }

            label202:
            try {
               return var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var2;
      }
   }

   public static SharedPreferences getMainSettings(int var0) {
      return getInstance(var0).mainPreferences;
   }

   public static SharedPreferences getNotificationsSettings(int var0) {
      return getInstance(var0).notificationsPreferences;
   }

   private static String getRestrictionReason(String var0) {
      if (var0 != null && var0.length() != 0) {
         int var1 = var0.indexOf(": ");
         if (var1 > 0) {
            String var2 = var0.substring(0, var1);
            if (var2.contains("-all") || var2.contains("-android")) {
               return var0.substring(var1 + 2);
            }
         }
      }

      return null;
   }

   private static int getUpdateChannelId(TLRPC.Update var0) {
      if (var0 instanceof TLRPC.TL_updateNewChannelMessage) {
         return ((TLRPC.TL_updateNewChannelMessage)var0).message.to_id.channel_id;
      } else if (var0 instanceof TLRPC.TL_updateEditChannelMessage) {
         return ((TLRPC.TL_updateEditChannelMessage)var0).message.to_id.channel_id;
      } else if (var0 instanceof TLRPC.TL_updateReadChannelOutbox) {
         return ((TLRPC.TL_updateReadChannelOutbox)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateChannelMessageViews) {
         return ((TLRPC.TL_updateChannelMessageViews)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateChannelTooLong) {
         return ((TLRPC.TL_updateChannelTooLong)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateChannelPinnedMessage) {
         return ((TLRPC.TL_updateChannelPinnedMessage)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateChannelReadMessagesContents) {
         return ((TLRPC.TL_updateChannelReadMessagesContents)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateChannelAvailableMessages) {
         return ((TLRPC.TL_updateChannelAvailableMessages)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateChannel) {
         return ((TLRPC.TL_updateChannel)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateChannelWebPage) {
         return ((TLRPC.TL_updateChannelWebPage)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateDeleteChannelMessages) {
         return ((TLRPC.TL_updateDeleteChannelMessages)var0).channel_id;
      } else if (var0 instanceof TLRPC.TL_updateReadChannelInbox) {
         return ((TLRPC.TL_updateReadChannelInbox)var0).channel_id;
      } else {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var1 = new StringBuilder();
            var1.append("trying to get unknown update channel_id for ");
            var1.append(var0);
            FileLog.e(var1.toString());
         }

         return 0;
      }
   }

   private static int getUpdatePts(TLRPC.Update var0) {
      if (var0 instanceof TLRPC.TL_updateDeleteMessages) {
         return ((TLRPC.TL_updateDeleteMessages)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateNewChannelMessage) {
         return ((TLRPC.TL_updateNewChannelMessage)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateReadHistoryOutbox) {
         return ((TLRPC.TL_updateReadHistoryOutbox)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateNewMessage) {
         return ((TLRPC.TL_updateNewMessage)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateEditMessage) {
         return ((TLRPC.TL_updateEditMessage)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateWebPage) {
         return ((TLRPC.TL_updateWebPage)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateReadHistoryInbox) {
         return ((TLRPC.TL_updateReadHistoryInbox)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateChannelWebPage) {
         return ((TLRPC.TL_updateChannelWebPage)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateDeleteChannelMessages) {
         return ((TLRPC.TL_updateDeleteChannelMessages)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateEditChannelMessage) {
         return ((TLRPC.TL_updateEditChannelMessage)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateReadMessagesContents) {
         return ((TLRPC.TL_updateReadMessagesContents)var0).pts;
      } else if (var0 instanceof TLRPC.TL_updateChannelTooLong) {
         return ((TLRPC.TL_updateChannelTooLong)var0).pts;
      } else {
         return var0 instanceof TLRPC.TL_updateFolderPeers ? ((TLRPC.TL_updateFolderPeers)var0).pts : 0;
      }
   }

   private static int getUpdatePtsCount(TLRPC.Update var0) {
      if (var0 instanceof TLRPC.TL_updateDeleteMessages) {
         return ((TLRPC.TL_updateDeleteMessages)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateNewChannelMessage) {
         return ((TLRPC.TL_updateNewChannelMessage)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateReadHistoryOutbox) {
         return ((TLRPC.TL_updateReadHistoryOutbox)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateNewMessage) {
         return ((TLRPC.TL_updateNewMessage)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateEditMessage) {
         return ((TLRPC.TL_updateEditMessage)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateWebPage) {
         return ((TLRPC.TL_updateWebPage)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateReadHistoryInbox) {
         return ((TLRPC.TL_updateReadHistoryInbox)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateChannelWebPage) {
         return ((TLRPC.TL_updateChannelWebPage)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateDeleteChannelMessages) {
         return ((TLRPC.TL_updateDeleteChannelMessages)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateEditChannelMessage) {
         return ((TLRPC.TL_updateEditChannelMessage)var0).pts_count;
      } else if (var0 instanceof TLRPC.TL_updateReadMessagesContents) {
         return ((TLRPC.TL_updateReadMessagesContents)var0).pts_count;
      } else {
         return var0 instanceof TLRPC.TL_updateFolderPeers ? ((TLRPC.TL_updateFolderPeers)var0).pts_count : 0;
      }
   }

   private static int getUpdateQts(TLRPC.Update var0) {
      return var0 instanceof TLRPC.TL_updateNewEncryptedMessage ? ((TLRPC.TL_updateNewEncryptedMessage)var0).qts : 0;
   }

   private int getUpdateSeq(TLRPC.Updates var1) {
      return var1 instanceof TLRPC.TL_updatesCombined ? var1.seq_start : var1.seq;
   }

   private int getUpdateType(TLRPC.Update var1) {
      if (!(var1 instanceof TLRPC.TL_updateNewMessage) && !(var1 instanceof TLRPC.TL_updateReadMessagesContents) && !(var1 instanceof TLRPC.TL_updateReadHistoryInbox) && !(var1 instanceof TLRPC.TL_updateReadHistoryOutbox) && !(var1 instanceof TLRPC.TL_updateDeleteMessages) && !(var1 instanceof TLRPC.TL_updateWebPage) && !(var1 instanceof TLRPC.TL_updateEditMessage) && !(var1 instanceof TLRPC.TL_updateFolderPeers)) {
         if (var1 instanceof TLRPC.TL_updateNewEncryptedMessage) {
            return 1;
         } else {
            return !(var1 instanceof TLRPC.TL_updateNewChannelMessage) && !(var1 instanceof TLRPC.TL_updateDeleteChannelMessages) && !(var1 instanceof TLRPC.TL_updateEditChannelMessage) && !(var1 instanceof TLRPC.TL_updateChannelWebPage) ? 3 : 2;
         }
      } else {
         return 0;
      }
   }

   private String getUserNameForTyping(TLRPC.User var1) {
      if (var1 == null) {
         return "";
      } else {
         String var2 = var1.first_name;
         if (var2 != null && var2.length() > 0) {
            return var1.first_name;
         } else {
            var2 = var1.last_name;
            return var2 != null && var2.length() > 0 ? var1.last_name : "";
         }
      }
   }

   public static boolean isSupportUser(TLRPC.User var0) {
      boolean var2;
      label108: {
         if (var0 != null) {
            if (var0.support) {
               break label108;
            }

            int var1 = var0.id;
            if (var1 == 777000 || var1 == 333000 || var1 == 4240000 || var1 == 4244000 || var1 == 4245000 || var1 == 4246000 || var1 == 410000 || var1 == 420000 || var1 == 431000 || var1 == 431415000 || var1 == 434000 || var1 == 4243000 || var1 == 439000 || var1 == 449000 || var1 == 450000 || var1 == 452000 || var1 == 454000 || var1 == 4254000 || var1 == 455000 || var1 == 460000 || var1 == 470000 || var1 == 479000 || var1 == 796000 || var1 == 482000 || var1 == 490000 || var1 == 496000 || var1 == 497000 || var1 == 498000 || var1 == 4298000) {
               break label108;
            }
         }

         var2 = false;
         return var2;
      }

      var2 = true;
      return var2;
   }

   private int isValidUpdate(TLRPC.Updates var1, int var2) {
      if (var2 == 0) {
         var2 = this.getUpdateSeq(var1);
         if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() + 1 != var2 && MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() != var2) {
            return MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() < var2 ? 1 : 2;
         } else {
            return 0;
         }
      } else if (var2 == 1) {
         if (var1.pts <= MessagesStorage.getInstance(this.currentAccount).getLastPtsValue()) {
            return 2;
         } else {
            return MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + var1.pts_count == var1.pts ? 0 : 1;
         }
      } else if (var2 == 2) {
         if (var1.pts <= MessagesStorage.getInstance(this.currentAccount).getLastQtsValue()) {
            return 2;
         } else {
            return MessagesStorage.getInstance(this.currentAccount).getLastQtsValue() + var1.updates.size() == var1.pts ? 0 : 1;
         }
      } else {
         return 0;
      }
   }

   // $FF: synthetic method
   static void lambda$completeReadTask$147(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$deleteUserPhoto$58(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$hideReportSpam$23(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$markMentionMessageAsRead$142(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$markMentionsAsRead$149(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$markMessageContentAsRead$140(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$null$161(Context var0, AlertDialog var1) {
      if (!((Activity)var0).isFinishing()) {
         try {
            var1.dismiss();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }
      }

   }

   // $FF: synthetic method
   static void lambda$null$162(MessagesStorage.IntCallback var0, TLRPC.Updates var1) {
      if (var0 != null) {
         for(int var2 = 0; var2 < var1.chats.size(); ++var2) {
            TLRPC.Chat var3 = (TLRPC.Chat)var1.chats.get(var2);
            if (ChatObject.isChannel(var3)) {
               var0.run(var3.id);
               break;
            }
         }
      }

   }

   // $FF: synthetic method
   static void lambda$null$163(Context var0, AlertDialog var1) {
      if (!((Activity)var0).isFinishing()) {
         try {
            var1.dismiss();
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

         AlertDialog.Builder var3 = new AlertDialog.Builder(var0);
         var3.setTitle(LocaleController.getString("AppName", 2131558635));
         var3.setMessage(LocaleController.getString("ErrorOccurred", 2131559375));
         var3.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var3.show().setCanceledOnTouchOutside(true);
      }

   }

   // $FF: synthetic method
   static int lambda$processChannelsUpdatesQueue$191(TLRPC.Updates var0, TLRPC.Updates var1) {
      return AndroidUtilities.compare(var0.pts, var1.pts);
   }

   // $FF: synthetic method
   static void lambda$processUpdates$236(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static int lambda$processUpdatesQueue$193(TLRPC.Updates var0, TLRPC.Updates var1) {
      return AndroidUtilities.compare(var0.pts, var1.pts);
   }

   // $FF: synthetic method
   static int lambda$processUpdatesQueue$194(TLRPC.Updates var0, TLRPC.Updates var1) {
      return AndroidUtilities.compare(var0.pts, var1.pts);
   }

   // $FF: synthetic method
   static void lambda$reportSpam$24(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$reportSpam$25(TLObject var0, TLRPC.TL_error var1) {
   }

   // $FF: synthetic method
   static void lambda$unregistedPush$185(TLObject var0, TLRPC.TL_error var1) {
   }

   private void loadMessagesInternal(long var1, int var3, int var4, int var5, boolean var6, int var7, int var8, int var9, int var10, boolean var11, int var12, int var13, int var14, int var15, boolean var16, int var17, boolean var18) {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var19 = new StringBuilder();
         var19.append("load messages in chat ");
         var19.append(var1);
         var19.append(" count ");
         var19.append(var3);
         var19.append(" max_id ");
         var19.append(var4);
         var19.append(" cache ");
         var19.append(var6);
         var19.append(" mindate = ");
         var19.append(var7);
         var19.append(" guid ");
         var19.append(var8);
         var19.append(" load_type ");
         var19.append(var9);
         var19.append(" last_message_id ");
         var19.append(var10);
         var19.append(" index ");
         var19.append(var12);
         var19.append(" firstUnread ");
         var19.append(var13);
         var19.append(" unread_count ");
         var19.append(var14);
         var19.append(" last_date ");
         var19.append(var15);
         var19.append(" queryFromServer ");
         var19.append(var16);
         FileLog.d(var19.toString());
      }

      int var20 = (int)var1;
      if (!var6 && var20 != 0) {
         if (var18 && (var9 == 3 || var9 == 2) && var10 == 0) {
            TLRPC.TL_messages_getPeerDialogs var21 = new TLRPC.TL_messages_getPeerDialogs();
            TLRPC.InputPeer var24 = this.getInputPeer(var20);
            TLRPC.TL_inputDialogPeer var22 = new TLRPC.TL_inputDialogPeer();
            var22.peer = var24;
            var21.peers.add(var22);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var21, new _$$Lambda$MessagesController$TN49v1ka_VM8bKqcQJdVLHWMje8(this, var1, var3, var4, var5, var7, var8, var9, var11, var12, var13, var15, var16));
            return;
         }

         TLRPC.TL_messages_getHistory var23 = new TLRPC.TL_messages_getHistory();
         var23.peer = this.getInputPeer(var20);
         if (var9 == 4) {
            var23.add_offset = -var3 + 5;
         } else if (var9 == 3) {
            var23.add_offset = -var3 / 2;
         } else if (var9 == 1) {
            var23.add_offset = -var3 - 1;
         } else if (var9 == 2 && var4 != 0) {
            var23.add_offset = -var3 + 6;
         } else if (var20 < 0 && var4 != 0 && ChatObject.isChannel(this.getChat(-var20))) {
            var23.add_offset = -1;
            ++var23.limit;
         }

         var23.limit = var3;
         var23.offset_id = var4;
         var23.offset_date = var5;
         var3 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var23, new _$$Lambda$MessagesController$PBgpLRl3hHYMA8ENpUjkzO9CiK0(this, var1, var3, var4, var5, var8, var13, var10, var14, var15, var9, var11, var12, var16, var17));
         ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var3, var8);
      } else {
         MessagesStorage.getInstance(this.currentAccount).getMessages(var1, var3, var4, var5, var7, var8, var9, var11, var12);
      }

   }

   private void migrateDialogs(int var1, int var2, int var3, int var4, int var5, long var6) {
      if (!this.migratingDialogs && var1 != -1) {
         this.migratingDialogs = true;
         TLRPC.TL_messages_getDialogs var8 = new TLRPC.TL_messages_getDialogs();
         var8.exclude_pinned = true;
         var8.limit = 100;
         var8.offset_id = var1;
         var8.offset_date = var2;
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var9 = new StringBuilder();
            var9.append("start migrate with id ");
            var9.append(var1);
            var9.append(" date ");
            var9.append(LocaleController.getInstance().formatterStats.format((long)var2 * 1000L));
            FileLog.d(var9.toString());
         }

         if (var1 == 0) {
            var8.offset_peer = new TLRPC.TL_inputPeerEmpty();
         } else {
            if (var5 != 0) {
               var8.offset_peer = new TLRPC.TL_inputPeerChannel();
               var8.offset_peer.channel_id = var5;
            } else if (var3 != 0) {
               var8.offset_peer = new TLRPC.TL_inputPeerUser();
               var8.offset_peer.user_id = var3;
            } else {
               var8.offset_peer = new TLRPC.TL_inputPeerChat();
               var8.offset_peer.chat_id = var4;
            }

            var8.offset_peer.access_hash = var6;
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$MessagesController$tmAPAmyX_HLSBIR7TwEc_6LXn_I(this, var1));
      }

   }

   public static void openChatOrProfileWith(TLRPC.User var0, TLRPC.Chat var1, BaseFragment var2, int var3, boolean var4) {
      if ((var0 != null || var1 != null) && var2 != null) {
         String var5 = null;
         int var6;
         boolean var7;
         if (var1 != null) {
            var5 = getRestrictionReason(var1.restriction_reason);
            var6 = var3;
            var7 = var4;
         } else {
            var6 = var3;
            var7 = var4;
            if (var0 != null) {
               String var8 = getRestrictionReason(var0.restriction_reason);
               var5 = var8;
               var6 = var3;
               var7 = var4;
               if (var0.bot) {
                  var6 = 1;
                  var7 = true;
                  var5 = var8;
               }
            }
         }

         if (var5 != null) {
            showCantOpenAlert(var2, var5);
         } else {
            Bundle var9 = new Bundle();
            if (var1 != null) {
               var9.putInt("chat_id", var1.id);
            } else {
               var9.putInt("user_id", var0.id);
            }

            if (var6 == 0) {
               var2.presentFragment(new ProfileActivity(var9));
            } else if (var6 == 2) {
               var2.presentFragment(new ChatActivity(var9), true, true);
            } else {
               var2.presentFragment(new ChatActivity(var9), var7);
            }
         }

      }
   }

   private void processChannelsUpdatesQueue(int var1, int var2) {
      ArrayList var3 = (ArrayList)this.updatesQueueChannels.get(var1);
      if (var3 != null) {
         int var4 = this.channelsPts.get(var1);
         if (!var3.isEmpty() && var4 != 0) {
            Collections.sort(var3, _$$Lambda$MessagesController$JGJclbw8cDS2wcbI_Tj2zlng_g0.INSTANCE);
            if (var2 == 2) {
               this.channelsPts.put(var1, ((TLRPC.Updates)var3.get(0)).pts);
            }

            boolean var5 = false;

            StringBuilder var10;
            while(var3.size() > 0) {
               TLRPC.Updates var6 = (TLRPC.Updates)var3.get(0);
               var2 = var6.pts;
               byte var9;
               if (var2 <= var4) {
                  var9 = 2;
               } else if (var6.pts_count + var4 == var2) {
                  var9 = 0;
               } else {
                  var9 = 1;
               }

               if (var9 == 0) {
                  this.processUpdates(var6, true);
                  var3.remove(0);
                  var5 = true;
               } else {
                  if (var9 == 1) {
                     long var7 = this.updatesStartWaitTimeChannels.get(var1);
                     if (var7 == 0L || !var5 && Math.abs(System.currentTimeMillis() - var7) > 1500L) {
                        if (BuildVars.LOGS_ENABLED) {
                           var10 = new StringBuilder();
                           var10.append("HOLE IN CHANNEL ");
                           var10.append(var1);
                           var10.append(" UPDATES QUEUE - getChannelDifference ");
                           FileLog.d(var10.toString());
                        }

                        this.updatesStartWaitTimeChannels.delete(var1);
                        this.updatesQueueChannels.remove(var1);
                        this.getChannelDifference(var1);
                        return;
                     }

                     if (BuildVars.LOGS_ENABLED) {
                        var10 = new StringBuilder();
                        var10.append("HOLE IN CHANNEL ");
                        var10.append(var1);
                        var10.append(" UPDATES QUEUE - will wait more time");
                        FileLog.d(var10.toString());
                     }

                     if (var5) {
                        this.updatesStartWaitTimeChannels.put(var1, System.currentTimeMillis());
                     }

                     return;
                  }

                  var3.remove(0);
               }
            }

            this.updatesQueueChannels.remove(var1);
            this.updatesStartWaitTimeChannels.delete(var1);
            if (BuildVars.LOGS_ENABLED) {
               var10 = new StringBuilder();
               var10.append("UPDATES CHANNEL ");
               var10.append(var1);
               var10.append(" QUEUE PROCEED - OK");
               FileLog.d(var10.toString());
            }

         } else {
            this.updatesQueueChannels.remove(var1);
         }
      }
   }

   private void processUpdatesQueue(int var1, int var2) {
      ArrayList var3;
      if (var1 == 0) {
         var3 = this.updatesQueueSeq;
         Collections.sort(var3, new _$$Lambda$MessagesController$ZJInZNgn5fe5N3COP4HchbUkZ4o(this));
      } else if (var1 == 1) {
         var3 = this.updatesQueuePts;
         Collections.sort(var3, _$$Lambda$MessagesController$CXfuZJPIy1bgOC7pFoaz_w6qqDM.INSTANCE);
      } else if (var1 == 2) {
         var3 = this.updatesQueueQts;
         Collections.sort(var3, _$$Lambda$MessagesController$CpLCaAXrUWrrraDtT2_grVns0kE.INSTANCE);
      } else {
         var3 = null;
      }

      if (var3 != null && !var3.isEmpty()) {
         TLRPC.Updates var4;
         if (var2 == 2) {
            var4 = (TLRPC.Updates)var3.get(0);
            if (var1 == 0) {
               MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(this.getUpdateSeq(var4));
            } else if (var1 == 1) {
               MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var4.pts);
            } else {
               MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(var4.pts);
            }
         }

         boolean var6 = false;

         while(var3.size() > 0) {
            var4 = (TLRPC.Updates)var3.get(0);
            int var5 = this.isValidUpdate(var4, var1);
            if (var5 == 0) {
               this.processUpdates(var4, true);
               var3.remove(0);
               var6 = true;
            } else {
               if (var5 == 1) {
                  if (this.getUpdatesStartTime(var1) == 0L || !var6 && Math.abs(System.currentTimeMillis() - this.getUpdatesStartTime(var1)) > 1500L) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("HOLE IN UPDATES QUEUE - getDifference");
                     }

                     this.setUpdatesStartTime(var1, 0L);
                     var3.clear();
                     this.getDifference();
                     return;
                  }

                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("HOLE IN UPDATES QUEUE - will wait more time");
                  }

                  if (var6) {
                     this.setUpdatesStartTime(var1, System.currentTimeMillis());
                  }

                  return;
               }

               var3.remove(0);
            }
         }

         var3.clear();
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("UPDATES QUEUE PROCEED - OK");
         }
      }

      this.setUpdatesStartTime(var1, 0L);
   }

   private void reloadDialogsReadValue(ArrayList var1, long var2) {
      if (var2 != 0L || var1 != null && !var1.isEmpty()) {
         TLRPC.TL_messages_getPeerDialogs var4 = new TLRPC.TL_messages_getPeerDialogs();
         if (var1 != null) {
            for(int var5 = 0; var5 < var1.size(); ++var5) {
               TLRPC.InputPeer var6 = this.getInputPeer((int)((TLRPC.Dialog)var1.get(var5)).id);
               if (!(var6 instanceof TLRPC.TL_inputPeerChannel) || var6.access_hash != 0L) {
                  TLRPC.TL_inputDialogPeer var7 = new TLRPC.TL_inputDialogPeer();
                  var7.peer = var6;
                  var4.peers.add(var7);
               }
            }
         } else {
            TLRPC.InputPeer var8 = this.getInputPeer((int)var2);
            if (var8 instanceof TLRPC.TL_inputPeerChannel && var8.access_hash == 0L) {
               return;
            }

            TLRPC.TL_inputDialogPeer var9 = new TLRPC.TL_inputDialogPeer();
            var9.peer = var8;
            var4.peers.add(var9);
         }

         if (!var4.peers.isEmpty()) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$MessagesController$YrH4Mr_BWV_6GLj5qWieGm840u8(this));
         }
      }
   }

   private void reloadMessages(ArrayList var1, long var2) {
      if (!var1.isEmpty()) {
         ArrayList var4 = new ArrayList();
         TLRPC.Chat var5 = ChatObject.getChatByDialog(var2, this.currentAccount);
         Object var6;
         if (ChatObject.isChannel(var5)) {
            var6 = new TLRPC.TL_channels_getMessages();
            ((TLRPC.TL_channels_getMessages)var6).channel = getInputChannel(var5);
            ((TLRPC.TL_channels_getMessages)var6).id = var4;
         } else {
            var6 = new TLRPC.TL_messages_getMessages();
            ((TLRPC.TL_messages_getMessages)var6).id = var4;
         }

         ArrayList var7 = (ArrayList)this.reloadingMessages.get(var2);

         for(int var8 = 0; var8 < var1.size(); ++var8) {
            Integer var9 = (Integer)var1.get(var8);
            if (var7 == null || !var7.contains(var9)) {
               var4.add(var9);
            }
         }

         if (!var4.isEmpty()) {
            var1 = var7;
            if (var7 == null) {
               var1 = new ArrayList();
               this.reloadingMessages.put(var2, var1);
            }

            var1.addAll(var4);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var6, new _$$Lambda$MessagesController$rxB6ZCv_wieivWsHR6TFAEUSbcc(this, var2, var5, var4));
         }
      }
   }

   private void removeDialog(TLRPC.Dialog var1) {
      if (var1 != null) {
         long var2 = var1.id;
         if (this.dialogsServerOnly.remove(var1) && DialogObject.isChannel(var1)) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$vWlNj3xTvw31mmNI0cEnDFi4DZ8(this, var2));
         }

         this.allDialogs.remove(var1);
         this.dialogsCanAddUsers.remove(var1);
         this.dialogsChannelsOnly.remove(var1);
         this.dialogsGroupsOnly.remove(var1);
         this.dialogsUsersOnly.remove(var1);
         this.dialogsForward.remove(var1);
         this.dialogs_dict.remove(var2);
         this.dialogs_read_inbox_max.remove(var2);
         this.dialogs_read_outbox_max.remove(var2);
         ArrayList var4 = (ArrayList)this.dialogsByFolder.get(var1.folder_id);
         if (var4 != null) {
            var4.remove(var1);
         }

      }
   }

   private void removeFolder(int var1) {
      long var2 = DialogObject.makeFolderDialogId(var1);
      TLRPC.Dialog var4 = (TLRPC.Dialog)this.dialogs_dict.get(var2);
      if (var4 != null) {
         this.dialogs_dict.remove(var2);
         this.allDialogs.remove(var4);
         this.sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.folderBecomeEmpty, var1);
      }
   }

   private void resetDialogs(boolean var1, int var2, int var3, int var4, int var5) {
      Integer var6 = 0;
      if (var1) {
         if (this.resetingDialogs) {
            return;
         }

         UserConfig.getInstance(this.currentAccount).setPinnedDialogsLoaded(1, false);
         this.resetingDialogs = true;
         TLRPC.TL_messages_getPinnedDialogs var7 = new TLRPC.TL_messages_getPinnedDialogs();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$MessagesController$gH22liOUbHjBW_HkIeQEsiEIQPo(this, var2, var3, var4, var5));
         TLRPC.TL_messages_getDialogs var23 = new TLRPC.TL_messages_getDialogs();
         var23.limit = 100;
         var23.exclude_pinned = true;
         var23.offset_peer = new TLRPC.TL_inputPeerEmpty();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var23, new _$$Lambda$MessagesController$Y6E_L9N_2KXUoxEcct5cWMRh9hc(this, var2, var3, var4, var5));
      } else if (this.resetDialogsPinned != null) {
         TLRPC.messages_Dialogs var24 = this.resetDialogsAll;
         if (var24 != null) {
            int var8 = var24.messages.size();
            int var9 = this.resetDialogsAll.dialogs.size();
            this.fetchFolderInLoadedPinnedDialogs(this.resetDialogsPinned);
            this.resetDialogsAll.dialogs.addAll(this.resetDialogsPinned.dialogs);
            this.resetDialogsAll.messages.addAll(this.resetDialogsPinned.messages);
            this.resetDialogsAll.users.addAll(this.resetDialogsPinned.users);
            this.resetDialogsAll.chats.addAll(this.resetDialogsPinned.chats);
            LongSparseArray var10 = new LongSparseArray();
            LongSparseArray var11 = new LongSparseArray();
            SparseArray var12 = new SparseArray();
            SparseArray var13 = new SparseArray();

            int var14;
            TLRPC.User var25;
            for(var14 = 0; var14 < this.resetDialogsAll.users.size(); ++var14) {
               var25 = (TLRPC.User)this.resetDialogsAll.users.get(var14);
               var12.put(var25.id, var25);
            }

            for(var14 = 0; var14 < this.resetDialogsAll.chats.size(); ++var14) {
               TLRPC.Chat var26 = (TLRPC.Chat)this.resetDialogsAll.chats.get(var14);
               var13.put(var26.id, var26);
            }

            TLRPC.Message var15 = null;

            TLRPC.Message var27;
            for(var14 = 0; var14 < this.resetDialogsAll.messages.size(); var15 = var27) {
               TLRPC.Message var16 = (TLRPC.Message)this.resetDialogsAll.messages.get(var14);
               var27 = var15;
               if (var14 < var8) {
                  label175: {
                     if (var15 != null) {
                        var27 = var15;
                        if (var16.date >= var15.date) {
                           break label175;
                        }
                     }

                     var27 = var16;
                  }
               }

               label146: {
                  TLRPC.Peer var32 = var16.to_id;
                  int var17 = var32.channel_id;
                  TLRPC.Chat var33;
                  if (var17 != 0) {
                     var33 = (TLRPC.Chat)var13.get(var17);
                     if (var33 != null && var33.left) {
                        break label146;
                     }

                     if (var33 != null && var33.megagroup) {
                        var16.flags |= Integer.MIN_VALUE;
                     }
                  } else {
                     var17 = var32.chat_id;
                     if (var17 != 0) {
                        var33 = (TLRPC.Chat)var13.get(var17);
                        if (var33 != null && var33.migrated_to != null) {
                           break label146;
                        }
                     }
                  }

                  MessageObject var34 = new MessageObject(this.currentAccount, var16, var12, var13, false);
                  var11.put(var34.getDialogId(), var34);
               }

               ++var14;
            }

            var14 = 0;

            Integer var35;
            for(Integer var28 = var6; var14 < this.resetDialogsAll.dialogs.size(); ++var14) {
               TLRPC.Dialog var18 = (TLRPC.Dialog)this.resetDialogsAll.dialogs.get(var14);
               DialogObject.initDialog(var18);
               long var19 = var18.id;
               if (var19 != 0L) {
                  if (var18.last_message_date == 0) {
                     MessageObject var21 = (MessageObject)var11.get(var19);
                     if (var21 != null) {
                        var18.last_message_date = var21.messageOwner.date;
                     }
                  }

                  TLRPC.Chat var22;
                  if (DialogObject.isChannel(var18)) {
                     var22 = (TLRPC.Chat)var13.get(-((int)var18.id));
                     if (var22 != null && var22.left) {
                        continue;
                     }

                     this.channelsPts.put(-((int)var18.id), var18.pts);
                  } else {
                     var19 = var18.id;
                     if ((int)var19 < 0) {
                        var22 = (TLRPC.Chat)var13.get(-((int)var19));
                        if (var22 != null && var22.migrated_to != null) {
                           continue;
                        }
                     }
                  }

                  var10.put(var18.id, var18);
                  var35 = (Integer)this.dialogs_read_inbox_max.get(var18.id);
                  var6 = var35;
                  if (var35 == null) {
                     var6 = var28;
                  }

                  this.dialogs_read_inbox_max.put(var18.id, Math.max(var6, var18.read_inbox_max_id));
                  var35 = (Integer)this.dialogs_read_outbox_max.get(var18.id);
                  var6 = var35;
                  if (var35 == null) {
                     var6 = var28;
                  }

                  this.dialogs_read_outbox_max.put(var18.id, Math.max(var6, var18.read_outbox_max_id));
               }
            }

            ImageLoader.saveMessagesThumbs(this.resetDialogsAll.messages);

            for(var14 = 0; var14 < this.resetDialogsAll.messages.size(); ++var14) {
               TLRPC.Message var31 = (TLRPC.Message)this.resetDialogsAll.messages.get(var14);
               TLRPC.MessageAction var29 = var31.action;
               if (var29 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                  var25 = (TLRPC.User)var12.get(var29.user_id);
                  if (var25 != null && var25.bot) {
                     var31.reply_markup = new TLRPC.TL_replyKeyboardHide();
                     var31.flags |= 64;
                  }
               }

               var29 = var31.action;
               if (!(var29 instanceof TLRPC.TL_messageActionChatMigrateTo) && !(var29 instanceof TLRPC.TL_messageActionChannelCreate)) {
                  ConcurrentHashMap var30;
                  if (var31.out) {
                     var30 = this.dialogs_read_outbox_max;
                  } else {
                     var30 = this.dialogs_read_inbox_max;
                  }

                  var35 = (Integer)var30.get(var31.dialog_id);
                  var6 = var35;
                  if (var35 == null) {
                     var6 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var31.out, var31.dialog_id);
                     var30.put(var31.dialog_id, var6);
                  }

                  if (var6 < var31.id) {
                     var1 = true;
                  } else {
                     var1 = false;
                  }

                  var31.unread = var1;
               } else {
                  var31.unread = false;
                  var31.media_unread = false;
               }
            }

            MessagesStorage.getInstance(this.currentAccount).resetDialogs(this.resetDialogsAll, var8, var2, var3, var4, var5, var10, var11, var15, var9);
            this.resetDialogsPinned = null;
            this.resetDialogsAll = null;
         }
      }

   }

   private void setUpdatesStartTime(int var1, long var2) {
      if (var1 == 0) {
         this.updatesStartWaitTimeSeq = var2;
      } else if (var1 == 1) {
         this.updatesStartWaitTimePts = var2;
      } else if (var1 == 2) {
         this.updatesStartWaitTimeQts = var2;
      }

   }

   private static void showCantOpenAlert(BaseFragment var0, String var1) {
      if (var0 != null && var0.getParentActivity() != null) {
         AlertDialog.Builder var2 = new AlertDialog.Builder(var0.getParentActivity());
         var2.setTitle(LocaleController.getString("AppName", 2131558635));
         var2.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
         var2.setMessage(var1);
         var0.showDialog(var2.create());
      }

   }

   private void updatePrintingStrings() {
      LongSparseArray var1 = new LongSparseArray();
      LongSparseArray var2 = new LongSparseArray();
      Iterator var3 = this.printingUsers.entrySet().iterator();

      while(true) {
         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            long var5 = (Long)var4.getKey();
            ArrayList var7 = (ArrayList)var4.getValue();
            int var8 = (int)var5;
            if (var8 <= 0 && var8 != 0 && var7.size() != 1) {
               StringBuilder var14 = new StringBuilder();
               Iterator var9 = var7.iterator();
               var8 = 0;

               int var10;
               do {
                  var10 = var8;
                  if (!var9.hasNext()) {
                     break;
                  }

                  TLRPC.User var11 = this.getUser(((MessagesController.PrintingUser)var9.next()).userId);
                  var10 = var8;
                  if (var11 != null) {
                     if (var14.length() != 0) {
                        var14.append(", ");
                     }

                     var14.append(this.getUserNameForTyping(var11));
                     var10 = var8 + 1;
                  }

                  var8 = var10;
               } while(var10 != 2);

               if (var14.length() != 0) {
                  if (var10 == 1) {
                     var1.put(var5, LocaleController.formatString("IsTypingGroup", 2131559700, var14.toString()));
                  } else if (var7.size() > 2) {
                     String var17 = LocaleController.getPluralString("AndMoreTypingGroup", var7.size() - 2);

                     try {
                        var1.put(var5, String.format(var17, var14.toString(), var7.size() - 2));
                     } catch (Exception var12) {
                        var1.put(var5, "LOC_ERR: AndMoreTypingGroup");
                     }
                  } else {
                     var1.put(var5, LocaleController.formatString("AreTypingGroup", 2131558665, var14.toString()));
                  }

                  var2.put(var5, 0);
               }
            } else {
               MessagesController.PrintingUser var15 = (MessagesController.PrintingUser)var7.get(0);
               TLRPC.User var13 = this.getUser(var15.userId);
               if (var13 != null) {
                  TLRPC.SendMessageAction var16 = var15.action;
                  if (var16 instanceof TLRPC.TL_sendMessageRecordAudioAction) {
                     if (var8 < 0) {
                        var1.put(var5, LocaleController.formatString("IsRecordingAudio", 2131559692, this.getUserNameForTyping(var13)));
                     } else {
                        var1.put(var5, LocaleController.getString("RecordingAudio", 2131560547));
                     }

                     var2.put(var5, 1);
                  } else if (!(var16 instanceof TLRPC.TL_sendMessageRecordRoundAction) && !(var16 instanceof TLRPC.TL_sendMessageUploadRoundAction)) {
                     if (var16 instanceof TLRPC.TL_sendMessageUploadAudioAction) {
                        if (var8 < 0) {
                           var1.put(var5, LocaleController.formatString("IsSendingAudio", 2131559694, this.getUserNameForTyping(var13)));
                        } else {
                           var1.put(var5, LocaleController.getString("SendingAudio", 2131560709));
                        }

                        var2.put(var5, 2);
                     } else if (!(var16 instanceof TLRPC.TL_sendMessageUploadVideoAction) && !(var16 instanceof TLRPC.TL_sendMessageRecordVideoAction)) {
                        if (var16 instanceof TLRPC.TL_sendMessageUploadDocumentAction) {
                           if (var8 < 0) {
                              var1.put(var5, LocaleController.formatString("IsSendingFile", 2131559695, this.getUserNameForTyping(var13)));
                           } else {
                              var1.put(var5, LocaleController.getString("SendingFile", 2131560710));
                           }

                           var2.put(var5, 2);
                        } else if (var16 instanceof TLRPC.TL_sendMessageUploadPhotoAction) {
                           if (var8 < 0) {
                              var1.put(var5, LocaleController.formatString("IsSendingPhoto", 2131559697, this.getUserNameForTyping(var13)));
                           } else {
                              var1.put(var5, LocaleController.getString("SendingPhoto", 2131560713));
                           }

                           var2.put(var5, 2);
                        } else if (var16 instanceof TLRPC.TL_sendMessageGamePlayAction) {
                           if (var8 < 0) {
                              var1.put(var5, LocaleController.formatString("IsSendingGame", 2131559696, this.getUserNameForTyping(var13)));
                           } else {
                              var1.put(var5, LocaleController.getString("SendingGame", 2131560711));
                           }

                           var2.put(var5, 3);
                        } else {
                           if (var8 < 0) {
                              var1.put(var5, LocaleController.formatString("IsTypingGroup", 2131559700, this.getUserNameForTyping(var13)));
                           } else {
                              var1.put(var5, LocaleController.getString("Typing", 2131560926));
                           }

                           var2.put(var5, 0);
                        }
                     } else {
                        if (var8 < 0) {
                           var1.put(var5, LocaleController.formatString("IsSendingVideo", 2131559698, this.getUserNameForTyping(var13)));
                        } else {
                           var1.put(var5, LocaleController.getString("SendingVideoStatus", 2131560716));
                        }

                        var2.put(var5, 2);
                     }
                  } else {
                     if (var8 < 0) {
                        var1.put(var5, LocaleController.formatString("IsRecordingRound", 2131559693, this.getUserNameForTyping(var13)));
                     } else {
                        var1.put(var5, LocaleController.getString("RecordingRound", 2131560548));
                     }

                     var2.put(var5, 4);
                  }
               }
            }
         }

         this.lastPrintingStringCount = var1.size();
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Gpj0Tfm4ESrlwMoiHamza4rhl1c(this, var1, var2));
         return;
      }
   }

   private boolean updatePrintingUsersWithNewMessages(long var1, ArrayList var3) {
      if (var1 > 0L) {
         if ((ArrayList)this.printingUsers.get(var1) != null) {
            this.printingUsers.remove(var1);
            return true;
         }
      } else if (var1 < 0L) {
         ArrayList var4 = new ArrayList();
         Iterator var9 = var3.iterator();

         while(var9.hasNext()) {
            MessageObject var5 = (MessageObject)var9.next();
            if (!var4.contains(var5.messageOwner.from_id)) {
               var4.add(var5.messageOwner.from_id);
            }
         }

         var3 = (ArrayList)this.printingUsers.get(var1);
         boolean var8;
         if (var3 != null) {
            int var6 = 0;
            boolean var7 = false;

            while(true) {
               var8 = var7;
               if (var6 >= var3.size()) {
                  break;
               }

               int var10 = var6;
               if (var4.contains(((MessagesController.PrintingUser)var3.get(var6)).userId)) {
                  var3.remove(var6);
                  var10 = var6 - 1;
                  if (var3.isEmpty()) {
                     this.printingUsers.remove(var1);
                  }

                  var7 = true;
               }

               var6 = var10 + 1;
            }
         } else {
            var8 = false;
         }

         if (var8) {
            return true;
         }
      }

      return false;
   }

   public void addDialogAction(long var1, boolean var3) {
      TLRPC.Dialog var4 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
      if (var4 != null) {
         if (var3) {
            this.clearingHistoryDialogs.put(var1, var4);
         } else {
            this.deletingDialogs.put(var1, var4);
            this.allDialogs.remove(var4);
            this.sortDialogs((SparseArray)null);
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
      }
   }

   public int addDialogToFolder(long var1, int var3, int var4, long var5) {
      ArrayList var7 = new ArrayList(1);
      var7.add(var1);
      return this.addDialogToFolder(var7, var3, var4, (ArrayList)null, var5);
   }

   public int addDialogToFolder(ArrayList var1, int var2, int var3, ArrayList var4, long var5) {
      TLRPC.TL_folders_editPeerFolders var7 = new TLRPC.TL_folders_editPeerFolders();
      boolean[] var20;
      if (var5 == 0L) {
         int var8 = UserConfig.getInstance(this.currentAccount).getClientUserId();
         int var9 = var1.size();
         boolean var10 = false;
         var20 = null;
         int var11 = 0;

         int var12;
         for(var12 = 0; var11 < var9; ++var11) {
            var5 = (Long)var1.get(var11);
            if ((DialogObject.isPeerDialogId(var5) || DialogObject.isSecretDialogId(var5)) && (var2 != 1 || var5 != (long)var8 && var5 != 777000L && !this.isProxyDialog(var5, false))) {
               TLRPC.Dialog var13 = (TLRPC.Dialog)this.dialogs_dict.get(var5);
               if (var13 != null) {
                  var13.folder_id = var2;
                  if (var3 > 0) {
                     var13.pinned = true;
                     var13.pinnedNum = var3;
                  } else {
                     var13.pinned = false;
                     var13.pinnedNum = 0;
                  }

                  boolean[] var22 = var20;
                  if (var20 == null) {
                     var22 = new boolean[1];
                     this.ensureFolderDialogExists(var2, var22);
                  }

                  if (DialogObject.isSecretDialogId(var5)) {
                     MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId((ArrayList)null, (ArrayList)null, var5, var2);
                  } else {
                     TLRPC.TL_inputFolderPeer var21 = new TLRPC.TL_inputFolderPeer();
                     var21.folder_id = var2;
                     var21.peer = this.getInputPeer((int)var5);
                     var7.folder_peers.add(var21);
                     var12 += var21.getObjectSize();
                  }

                  var10 = true;
                  var20 = var22;
               }
            }
         }

         if (!var10) {
            return 0;
         }

         this.sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         if (var12 != 0) {
            NativeByteBuffer var24;
            label84: {
               NativeByteBuffer var17;
               Exception var23;
               label114: {
                  try {
                     var17 = new NativeByteBuffer(var12 + 12);
                  } catch (Exception var16) {
                     var23 = var16;
                     var17 = null;
                     break label114;
                  }

                  Exception var10000;
                  label115: {
                     boolean var10001;
                     try {
                        var17.writeInt32(17);
                        var17.writeInt32(var2);
                        var17.writeInt32(var7.folder_peers.size());
                        var12 = var7.folder_peers.size();
                     } catch (Exception var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label115;
                     }

                     var3 = 0;

                     while(true) {
                        var24 = var17;
                        if (var3 >= var12) {
                           break label84;
                        }

                        try {
                           ((TLRPC.TL_inputFolderPeer)var7.folder_peers.get(var3)).serializeToStream(var17);
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break;
                        }

                        ++var3;
                     }
                  }

                  var23 = var10000;
               }

               FileLog.e((Throwable)var23);
               var24 = var17;
            }

            var5 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var24);
         } else {
            var5 = 0L;
         }
      } else {
         var7.folder_peers = var4;
         var20 = null;
      }

      byte var19 = 1;
      if (!var7.folder_peers.isEmpty()) {
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$MessagesController$6NDlWW4LaPRt1E9GG1k86kJK_WU(this, var5));
         MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId((ArrayList)null, var7.folder_peers, 0L, var2);
      }

      byte var18;
      if (var20 == null) {
         var18 = 0;
      } else {
         var18 = var19;
         if (var20[0]) {
            var18 = 2;
         }
      }

      return var18;
   }

   public void addSupportUser() {
      TLRPC.TL_userForeign_old2 var1 = new TLRPC.TL_userForeign_old2();
      var1.phone = "333";
      var1.id = 333000;
      var1.first_name = "Telegram";
      var1.last_name = "";
      var1.status = null;
      var1.photo = new TLRPC.TL_userProfilePhotoEmpty();
      this.putUser(var1, true);
      var1 = new TLRPC.TL_userForeign_old2();
      var1.phone = "42777";
      var1.id = 777000;
      var1.verified = true;
      var1.first_name = "Telegram";
      var1.last_name = "Notifications";
      var1.status = null;
      var1.photo = new TLRPC.TL_userProfilePhotoEmpty();
      this.putUser(var1, true);
   }

   public void addToPollsQueue(long var1, ArrayList var3) {
      SparseArray var4 = (SparseArray)this.pollsToCheck.get(var1);
      SparseArray var5 = var4;
      if (var4 == null) {
         var5 = new SparseArray();
         this.pollsToCheck.put(var1, var5);
         ++this.pollsToCheckSize;
      }

      int var6 = var5.size();
      byte var7 = 0;

      int var8;
      for(var8 = 0; var8 < var6; ++var8) {
         ((MessageObject)var5.valueAt(var8)).pollVisibleOnScreen = false;
      }

      var6 = var3.size();

      for(var8 = var7; var8 < var6; ++var8) {
         MessageObject var9 = (MessageObject)var3.get(var8);
         if (var9.type == 17) {
            int var11 = var9.getId();
            MessageObject var10 = (MessageObject)var5.get(var11);
            if (var10 != null) {
               var10.pollVisibleOnScreen = true;
            } else {
               var5.put(var11, var9);
            }
         }
      }

   }

   public void addToViewsQueue(MessageObject var1) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$cWol_41rOBrStFY3FKcjDkmBodE(this, var1));
   }

   public void addUserToChat(int var1, TLRPC.User var2, TLRPC.ChatFull var3, int var4, String var5, BaseFragment var6, Runnable var7) {
      if (var2 != null) {
         if (var1 > 0) {
            boolean var8 = ChatObject.isChannel(var1, this.currentAccount);
            boolean var9;
            if (var8 && this.getChat(var1).megagroup) {
               var9 = true;
            } else {
               var9 = false;
            }

            TLRPC.InputUser var11 = this.getInputUser(var2);
            Object var10;
            if (var5 != null && (!var8 || var9)) {
               var10 = new TLRPC.TL_messages_startBot();
               ((TLRPC.TL_messages_startBot)var10).bot = var11;
               if (var8) {
                  ((TLRPC.TL_messages_startBot)var10).peer = this.getInputPeer(-var1);
               } else {
                  ((TLRPC.TL_messages_startBot)var10).peer = new TLRPC.TL_inputPeerChat();
                  ((TLRPC.TL_messages_startBot)var10).peer.chat_id = var1;
               }

               ((TLRPC.TL_messages_startBot)var10).start_param = var5;
               ((TLRPC.TL_messages_startBot)var10).random_id = Utilities.random.nextLong();
            } else if (var8) {
               if (var11 instanceof TLRPC.TL_inputUserSelf) {
                  if (this.joiningToChannels.contains(var1)) {
                     return;
                  }

                  var10 = new TLRPC.TL_channels_joinChannel();
                  ((TLRPC.TL_channels_joinChannel)var10).channel = this.getInputChannel(var1);
                  this.joiningToChannels.add(var1);
               } else {
                  var10 = new TLRPC.TL_channels_inviteToChannel();
                  ((TLRPC.TL_channels_inviteToChannel)var10).channel = this.getInputChannel(var1);
                  ((TLRPC.TL_channels_inviteToChannel)var10).users.add(var11);
               }
            } else {
               var10 = new TLRPC.TL_messages_addChatUser();
               ((TLRPC.TL_messages_addChatUser)var10).chat_id = var1;
               ((TLRPC.TL_messages_addChatUser)var10).fwd_limit = var4;
               ((TLRPC.TL_messages_addChatUser)var10).user_id = var11;
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var10, new _$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4(this, var8, var11, var1, var6, (TLObject)var10, var9, var7));
         } else if (var3 instanceof TLRPC.TL_chatFull) {
            for(var4 = 0; var4 < var3.participants.participants.size(); ++var4) {
               if (((TLRPC.ChatParticipant)var3.participants.participants.get(var4)).user_id == var2.id) {
                  return;
               }
            }

            TLRPC.Chat var14 = this.getChat(var1);
            ++var14.participants_count;
            ArrayList var12 = new ArrayList();
            var12.add(var14);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats((ArrayList)null, var12, true, true);
            TLRPC.TL_chatParticipant var13 = new TLRPC.TL_chatParticipant();
            var13.user_id = var2.id;
            var13.inviter_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            var13.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            var3.participants.participants.add(0, var13);
            MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var3, true);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var3, 0, false, null);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 32);
         }

      }
   }

   public void addUsersToChannel(int var1, ArrayList var2, BaseFragment var3) {
      if (var2 != null && !var2.isEmpty()) {
         TLRPC.TL_channels_inviteToChannel var4 = new TLRPC.TL_channels_inviteToChannel();
         var4.channel = this.getInputChannel(var1);
         var4.users = var2;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$MessagesController$TfiNuUXCjyH_kDhosYCjp7Cg5Gk(this, var3, var4));
      }

   }

   public void blockUser(int var1) {
      TLRPC.User var2 = this.getUser(var1);
      if (var2 != null && this.blockedUsers.indexOfKey(var1) < 0) {
         this.blockedUsers.put(var1, 1);
         if (var2.bot) {
            DataQuery.getInstance(this.currentAccount).removeInline(var1);
         } else {
            DataQuery.getInstance(this.currentAccount).removePeer(var1);
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad);
         TLRPC.TL_contacts_block var3 = new TLRPC.TL_contacts_block();
         var3.id = this.getInputUser(var2);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$UR0KC8eSIVhCkdT_PUu4ASoxIZg(this, var2));
      }

   }

   public void cancelLoadFullChat(int var1) {
      this.loadingFullChats.remove(var1);
   }

   public void cancelLoadFullUser(int var1) {
      this.loadingFullUsers.remove(var1);
   }

   public void cancelTyping(int var1, long var2) {
      LongSparseArray var4 = (LongSparseArray)this.sendingTypings.get(var1);
      if (var4 != null) {
         var4.remove(var2);
      }

   }

   public void changeChatAvatar(int var1, TLRPC.InputFile var2, TLRPC.FileLocation var3, TLRPC.FileLocation var4) {
      Object var7;
      if (ChatObject.isChannel(var1, this.currentAccount)) {
         TLRPC.TL_channels_editPhoto var5 = new TLRPC.TL_channels_editPhoto();
         var5.channel = this.getInputChannel(var1);
         if (var2 != null) {
            var5.photo = new TLRPC.TL_inputChatUploadedPhoto();
            var5.photo.file = var2;
            var7 = var5;
         } else {
            var5.photo = new TLRPC.TL_inputChatPhotoEmpty();
            var7 = var5;
         }
      } else {
         TLRPC.TL_messages_editChatPhoto var6 = new TLRPC.TL_messages_editChatPhoto();
         var6.chat_id = var1;
         if (var2 != null) {
            var6.photo = new TLRPC.TL_inputChatUploadedPhoto();
            var6.photo.file = var2;
            var7 = var6;
         } else {
            var6.photo = new TLRPC.TL_inputChatPhotoEmpty();
            var7 = var6;
         }
      }

      ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var7, new _$$Lambda$MessagesController$xhFZ44TZl_2_TmtDUCclv1zCK1g(this, var3, var4), 64);
   }

   public void changeChatTitle(int var1, String var2) {
      if (var1 > 0) {
         Object var4;
         if (ChatObject.isChannel(var1, this.currentAccount)) {
            TLRPC.TL_channels_editTitle var3 = new TLRPC.TL_channels_editTitle();
            var3.channel = this.getInputChannel(var1);
            var3.title = var2;
            var4 = var3;
         } else {
            TLRPC.TL_messages_editChatTitle var6 = new TLRPC.TL_messages_editChatTitle();
            var6.chat_id = var1;
            var6.title = var2;
            var4 = var6;
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var4, new _$$Lambda$MessagesController$40Sp7FeVOZVQXMLSFubFHF48OI8(this), 64);
      } else {
         TLRPC.Chat var7 = this.getChat(var1);
         var7.title = var2;
         ArrayList var5 = new ArrayList();
         var5.add(var7);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats((ArrayList)null, var5, true, true);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 16);
      }

   }

   public boolean checkCanOpenChat(Bundle var1, BaseFragment var2) {
      return this.checkCanOpenChat(var1, var2, (MessageObject)null);
   }

   public boolean checkCanOpenChat(Bundle var1, BaseFragment var2, MessageObject var3) {
      if (var1 != null && var2 != null) {
         int var4 = var1.getInt("user_id", 0);
         int var5 = var1.getInt("chat_id", 0);
         int var6 = var1.getInt("message_id", 0);
         String var7 = null;
         TLRPC.User var8;
         Object var9;
         if (var4 != 0) {
            var8 = this.getUser(var4);
            var9 = null;
         } else if (var5 != 0) {
            var9 = this.getChat(var5);
            var8 = null;
         } else {
            var8 = null;
            var9 = var8;
         }

         if (var8 == null && var9 == null) {
            return true;
         }

         if (var9 != null) {
            var7 = getRestrictionReason(((TLRPC.Chat)var9).restriction_reason);
         } else if (var8 != null) {
            var7 = getRestrictionReason(var8.restriction_reason);
         }

         if (var7 != null) {
            showCantOpenAlert(var2, var7);
            return false;
         }

         if (var6 != 0 && var3 != null && var9 != null && ((TLRPC.Chat)var9).access_hash == 0L) {
            var4 = (int)var3.getDialogId();
            if (var4 != 0) {
               AlertDialog var12 = new AlertDialog(var2.getParentActivity(), 3);
               if (var4 < 0) {
                  var9 = this.getChat(-var4);
               }

               Object var10;
               if (var4 <= 0 && ChatObject.isChannel((TLRPC.Chat)var9)) {
                  TLRPC.Chat var11 = this.getChat(-var4);
                  TLRPC.TL_channels_getMessages var14 = new TLRPC.TL_channels_getMessages();
                  var14.channel = getInputChannel(var11);
                  var14.id.add(var3.getId());
                  var10 = var14;
               } else {
                  TLRPC.TL_messages_getMessages var13 = new TLRPC.TL_messages_getMessages();
                  var13.id.add(var3.getId());
                  var10 = var13;
               }

               var12.setOnCancelListener(new _$$Lambda$MessagesController$Uy5HFVAD0pdKo9Ipvs8fE566HbU(this, ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var10, new _$$Lambda$MessagesController$6GNHdOHS4ayBESoK8wFIXPmSlYs(this, var12, var2, var1)), var2));
               var2.setVisibleDialog(var12);
               var12.show();
               return false;
            }
         }
      }

      return true;
   }

   public void checkChannelInviter(int var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$1DIYKuDA2QelETVd6XOA_sCNt2w(this, var1));
   }

   public void checkIfFolderEmpty(int var1) {
      if (var1 != 0) {
         MessagesStorage.getInstance(this.currentAccount).checkIfFolderEmpty(var1);
      }
   }

   protected void checkLastDialogMessage(TLRPC.Dialog var1, TLRPC.InputPeer var2, long var3) {
      int var5 = (int)var1.id;
      if (var5 != 0 && this.checkingLastMessagesDialogs.indexOfKey(var5) < 0) {
         TLRPC.TL_messages_getHistory var6 = new TLRPC.TL_messages_getHistory();
         TLRPC.InputPeer var7;
         if (var2 == null) {
            var7 = this.getInputPeer(var5);
         } else {
            var7 = var2;
         }

         var6.peer = var7;
         if (var6.peer == null) {
            return;
         }

         var6.limit = 1;
         this.checkingLastMessagesDialogs.put(var5, true);
         long var8 = var3;
         if (var3 == 0L) {
            NativeByteBuffer var13;
            label38: {
               NativeByteBuffer var14;
               label37: {
                  Exception var10;
                  label36: {
                     try {
                        var14 = new NativeByteBuffer(var6.peer.getObjectSize() + 60);
                     } catch (Exception var12) {
                        var13 = null;
                        var10 = var12;
                        break label36;
                     }

                     try {
                        var14.writeInt32(14);
                        var14.writeInt64(var1.id);
                        var14.writeInt32(var1.top_message);
                        var14.writeInt32(var1.read_inbox_max_id);
                        var14.writeInt32(var1.read_outbox_max_id);
                        var14.writeInt32(var1.unread_count);
                        var14.writeInt32(var1.last_message_date);
                        var14.writeInt32(var1.pts);
                        var14.writeInt32(var1.flags);
                        var14.writeBool(var1.pinned);
                        var14.writeInt32(var1.pinnedNum);
                        var14.writeInt32(var1.unread_mentions_count);
                        var14.writeBool(var1.unread_mark);
                        var14.writeInt32(var1.folder_id);
                        var2.serializeToStream(var14);
                        break label37;
                     } catch (Exception var11) {
                        var10 = var11;
                        var13 = var14;
                     }
                  }

                  FileLog.e((Throwable)var10);
                  break label38;
               }

               var13 = var14;
            }

            var8 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var13);
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, new _$$Lambda$MessagesController$LzTCLk4PcU0AIVN_3fmrcJA4k9I(this, var5, var1, var8));
      }

   }

   public void checkProxyInfo(boolean var1) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$UmwP8okyEt8khTz852DsKDuGJ70(this, var1));
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
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   protected void clearFullUsers() {
      this.loadedFullUsers.clear();
      this.loadedFullChats.clear();
   }

   protected void completeDialogsReset(TLRPC.messages_Dialogs var1, int var2, int var3, int var4, int var5, int var6, LongSparseArray var7, LongSparseArray var8, TLRPC.Message var9) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$yD6NgtKkjxhJizLM4eoCQwHgn60(this, var4, var5, var6, var1, var7, var8));
   }

   public void convertToMegaGroup(Context var1, int var2, MessagesStorage.IntCallback var3) {
      TLRPC.TL_messages_migrateChat var4 = new TLRPC.TL_messages_migrateChat();
      var4.chat_id = var2;
      AlertDialog var5 = new AlertDialog(var1, 3);
      var5.setOnCancelListener(new _$$Lambda$MessagesController$SIGUP1wZNKHgQo2aKPuAgpDjucg(this, ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$MessagesController$EkRPBwXKrGufP9yBw39xD6g_xjo(this, var1, var5, var3))));

      try {
         var5.show();
      } catch (Exception var6) {
      }

   }

   public int createChat(String var1, ArrayList var2, String var3, int var4, BaseFragment var5) {
      byte var6 = 0;
      if (var4 != 1) {
         if (var4 == 0) {
            TLRPC.TL_messages_createChat var14 = new TLRPC.TL_messages_createChat();
            var14.title = var1;

            for(var4 = var6; var4 < var2.size(); ++var4) {
               TLRPC.User var12 = this.getUser((Integer)var2.get(var4));
               if (var12 != null) {
                  var14.users.add(this.getInputUser(var12));
               }
            }

            return ConnectionsManager.getInstance(this.currentAccount).sendRequest(var14, new _$$Lambda$MessagesController$BwCRJLOHKlBHZCobPU1cBtbarBc(this, var5, var14), 2);
         } else if (var4 != 2 && var4 != 4) {
            return 0;
         } else {
            TLRPC.TL_channels_createChannel var11 = new TLRPC.TL_channels_createChannel();
            var11.title = var1;
            if (var3 == null) {
               var3 = "";
            }

            var11.about = var3;
            if (var4 == 4) {
               var11.megagroup = true;
            } else {
               var11.broadcast = true;
            }

            return ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, new _$$Lambda$MessagesController$0bxY0b0iZuUAKIyUkrQ28a0391w(this, var5, var11), 2);
         }
      } else {
         TLRPC.TL_chat var13 = new TLRPC.TL_chat();
         var13.id = UserConfig.getInstance(this.currentAccount).lastBroadcastId;
         var13.title = var1;
         var13.photo = new TLRPC.TL_chatPhotoEmpty();
         var13.participants_count = var2.size();
         var13.date = (int)(System.currentTimeMillis() / 1000L);
         var13.version = 1;
         UserConfig var7 = UserConfig.getInstance(this.currentAccount);
         --var7.lastBroadcastId;
         this.putChat(var13, false);
         ArrayList var8 = new ArrayList();
         var8.add(var13);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats((ArrayList)null, var8, true, true);
         TLRPC.TL_chatFull var9 = new TLRPC.TL_chatFull();
         var9.id = var13.id;
         var9.chat_photo = new TLRPC.TL_photoEmpty();
         var9.notify_settings = new TLRPC.TL_peerNotifySettingsEmpty_layer77();
         var9.exported_invite = new TLRPC.TL_chatInviteEmpty();
         var9.participants = new TLRPC.TL_chatParticipants();
         TLRPC.ChatParticipants var15 = var9.participants;
         var15.chat_id = var13.id;
         var15.admin_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
         var9.participants.version = 1;

         for(var4 = 0; var4 < var2.size(); ++var4) {
            TLRPC.TL_chatParticipant var16 = new TLRPC.TL_chatParticipant();
            var16.user_id = (Integer)var2.get(var4);
            var16.inviter_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            var16.date = (int)(System.currentTimeMillis() / 1000L);
            var9.participants.participants.add(var16);
         }

         MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var9, false);
         TLRPC.TL_messageService var10 = new TLRPC.TL_messageService();
         var10.action = new TLRPC.TL_messageActionCreatedBroadcastList();
         var4 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
         var10.id = var4;
         var10.local_id = var4;
         var10.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
         var10.dialog_id = AndroidUtilities.makeBroadcastId(var13.id);
         var10.to_id = new TLRPC.TL_peerChat();
         var10.to_id.chat_id = var13.id;
         var10.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         var10.random_id = 0L;
         var10.flags |= 256;
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
         MessageObject var17 = new MessageObject(this.currentAccount, var10, this.users, true);
         var17.messageOwner.send_state = 0;
         var2 = new ArrayList();
         var2.add(var17);
         ArrayList var18 = new ArrayList();
         var18.add(var10);
         MessagesStorage.getInstance(this.currentAccount).putMessages(var18, false, true, false, 0);
         this.updateInterfaceWithMessages(var10.dialog_id, var2);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidCreated, var13.id);
         return 0;
      }
   }

   public void deleteDialog(long var1, int var3) {
      this.deleteDialog(var1, var3, false);
   }

   public void deleteDialog(long var1, int var3, boolean var4) {
      this.deleteDialog(var1, true, var3, 0, var4, (TLRPC.InputPeer)null, 0L);
   }

   protected void deleteDialog(long var1, boolean var3, int var4, int var5, boolean var6, TLRPC.InputPeer var7, long var8) {
      if (var4 == 2) {
         MessagesStorage.getInstance(this.currentAccount).deleteDialog(var1, var4);
      } else {
         if (var4 == 0 || var4 == 3) {
            DataQuery.getInstance(this.currentAccount).uninstallShortcut(var1);
         }

         int var10 = (int)var1;
         int var11 = (int)(var1 >> 32);
         int var13;
         boolean var19;
         if (var3) {
            MessagesStorage.getInstance(this.currentAccount).deleteDialog(var1, var4);
            TLRPC.Dialog var12 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
            if (var4 == 0 || var4 == 3) {
               NotificationsController.getInstance(this.currentAccount).deleteNotificationChannel(var1);
            }

            boolean var14;
            if (var12 != null) {
               if (var5 == 0) {
                  var13 = Math.max(Math.max(Math.max(0, var12.top_message), var12.read_inbox_max_id), var12.read_outbox_max_id);
               } else {
                  var13 = var5;
               }

               TLRPC.Dialog var15;
               long var16;
               if (var4 != 0 && var4 != 3) {
                  var12.unread_count = 0;
                  var14 = false;
                  var15 = var12;
               } else {
                  var15 = this.proxyDialog;
                  boolean var24;
                  if (var15 != null && var15.id == var1) {
                     var24 = true;
                  } else {
                     var24 = false;
                  }

                  if (var24) {
                     this.isLeftProxyChannel = true;
                     var16 = this.proxyDialog.id;
                     if (var16 < 0L) {
                        TLRPC.Chat var31 = this.getChat(-((int)var16));
                        if (var31 != null) {
                           var31.left = true;
                        }
                     }

                     this.sortDialogs((SparseArray)null);
                     var15 = var12;
                     var14 = var24;
                  } else {
                     this.removeDialog(var12);
                     int var18 = this.nextDialogsCacheOffset.get(var12.folder_id, 0);
                     var14 = var24;
                     var15 = var12;
                     if (var18 > 0) {
                        this.nextDialogsCacheOffset.put(var12.folder_id, var18 - 1);
                        var15 = var12;
                        var14 = var24;
                     }
                  }
               }

               var5 = var10;
               if (!var14) {
                  MessageObject var28 = (MessageObject)this.dialogMessage.get(var15.id);
                  this.dialogMessage.remove(var15.id);
                  if (var28 != null) {
                     var10 = var28.getId();
                     this.dialogMessagesByIds.remove(var28.getId());
                  } else {
                     var10 = var15.top_message;
                     var28 = (MessageObject)this.dialogMessagesByIds.get(var10);
                     this.dialogMessagesByIds.remove(var15.top_message);
                  }

                  if (var28 != null) {
                     var16 = var28.messageOwner.random_id;
                     if (var16 != 0L) {
                        this.dialogMessagesByRandomIds.remove(var16);
                     }
                  }

                  if (var4 == 1 && var5 != 0 && var10 > 0) {
                     TLRPC.TL_messageService var29 = new TLRPC.TL_messageService();
                     var29.id = var15.top_message;
                     if ((long)UserConfig.getInstance(this.currentAccount).getClientUserId() == var1) {
                        var19 = true;
                     } else {
                        var19 = false;
                     }

                     var29.out = var19;
                     var29.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                     var29.flags |= 256;
                     var29.action = new TLRPC.TL_messageActionHistoryClear();
                     var29.date = var15.last_message_date;
                     var29.dialog_id = (long)var5;
                     if (var5 > 0) {
                        var29.to_id = new TLRPC.TL_peerUser();
                        var29.to_id.user_id = var5;
                     } else {
                        var10 = -var5;
                        if (ChatObject.isChannel(this.getChat(var10))) {
                           var29.to_id = new TLRPC.TL_peerChannel();
                           var29.to_id.channel_id = var10;
                        } else {
                           var29.to_id = new TLRPC.TL_peerChat();
                           var29.to_id.chat_id = var10;
                        }
                     }

                     MessageObject var20 = new MessageObject(this.currentAccount, var29, this.createdDialogIds.contains(var29.dialog_id));
                     ArrayList var32 = new ArrayList();
                     var32.add(var20);
                     ArrayList var35 = new ArrayList();
                     var35.add(var29);
                     this.updateInterfaceWithMessages(var1, var32);
                     MessagesStorage.getInstance(this.currentAccount).putMessages(var35, false, true, false, 0);
                  } else {
                     var15.top_message = 0;
                  }
               }
            } else {
               var13 = var5;
               var14 = false;
               var5 = var10;
            }

            if (!this.dialogsInTransaction) {
               if (var14) {
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
               } else {
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, var1, false);
               }
            }

            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$fi2heANmHg4iJXLk9Zkj_J0ni7w(this, var1));
         } else {
            var13 = var5;
            var5 = var10;
         }

         if (var11 != 1 && var4 != 3) {
            if (var5 != 0) {
               TLRPC.InputPeer var30;
               if (var7 == null) {
                  var30 = this.getInputPeer(var5);
               } else {
                  var30 = var7;
               }

               if (var30 == null) {
                  return;
               }

               var19 = var30 instanceof TLRPC.TL_inputPeerChannel;
               var5 = Integer.MAX_VALUE;
               if (!var19 || var4 != 0) {
                  if (var13 > 0 && var13 != Integer.MAX_VALUE) {
                     this.deletedHistory.put(var1, var13);
                  }

                  if (var8 == 0L) {
                     NativeByteBuffer var33;
                     label159: {
                        Exception var25;
                        label212: {
                           try {
                              var33 = new NativeByteBuffer(var30.getObjectSize() + 28);
                           } catch (Exception var23) {
                              var25 = var23;
                              var33 = null;
                              break label212;
                           }

                           try {
                              var33.writeInt32(13);
                              var33.writeInt64(var1);
                              var33.writeBool(var3);
                              var33.writeInt32(var4);
                              var33.writeInt32(var13);
                           } catch (Exception var22) {
                              var25 = var22;
                              break label212;
                           }

                           try {
                              var33.writeBool(var6);
                              var30.serializeToStream(var33);
                              break label159;
                           } catch (Exception var21) {
                              var25 = var21;
                           }
                        }

                        FileLog.e((Throwable)var25);
                     }

                     var8 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var33);
                  }
               }

               if (var19) {
                  if (var4 == 0) {
                     if (var8 != 0L) {
                        MessagesStorage.getInstance(this.currentAccount).removePendingTask(var8);
                     }

                     return;
                  }

                  TLRPC.TL_channels_deleteHistory var26 = new TLRPC.TL_channels_deleteHistory();
                  var26.channel = new TLRPC.TL_inputChannel();
                  TLRPC.InputChannel var34 = var26.channel;
                  var34.channel_id = var30.channel_id;
                  var34.access_hash = var30.access_hash;
                  if (var13 <= 0) {
                     var13 = Integer.MAX_VALUE;
                  }

                  var26.max_id = var13;
                  ConnectionsManager.getInstance(this.currentAccount).sendRequest(var26, new _$$Lambda$MessagesController$yR0Wl__tkImGpY86P4U53sAMzc8(this, var8, var1), 64);
               } else {
                  TLRPC.TL_messages_deleteHistory var27 = new TLRPC.TL_messages_deleteHistory();
                  var27.peer = var30;
                  if (var4 != 0) {
                     var5 = var13;
                  }

                  var27.max_id = var5;
                  if (var4 != 0) {
                     var3 = true;
                  } else {
                     var3 = false;
                  }

                  var27.just_clear = var3;
                  var27.revoke = var6;
                  ConnectionsManager.getInstance(this.currentAccount).sendRequest(var27, new _$$Lambda$MessagesController$GgsMvhuFaAXbZ2H0PLOXM_0uOo4(this, var8, var1, var4, var13, var6, var30), 64);
               }
            } else if (var4 == 1) {
               SecretChatHelper.getInstance(this.currentAccount).sendClearHistoryMessage(this.getEncryptedChat(var11), (TLRPC.Message)null);
            } else {
               SecretChatHelper.getInstance(this.currentAccount).declineSecretChat(var11);
            }
         }

      }
   }

   public void deleteMessages(ArrayList var1, ArrayList var2, TLRPC.EncryptedChat var3, int var4, boolean var5) {
      this.deleteMessages(var1, var2, var3, var4, var5, 0L, (TLObject)null);
   }

   public void deleteMessages(ArrayList var1, ArrayList var2, TLRPC.EncryptedChat var3, int var4, boolean var5, long var6, TLObject var8) {
      if (var1 != null && !var1.isEmpty() || var8 != null) {
         if (var6 == 0L) {
            int var9;
            if (var4 == 0) {
               for(var9 = 0; var9 < var1.size(); ++var9) {
                  Integer var10 = (Integer)var1.get(var9);
                  MessageObject var22 = (MessageObject)this.dialogMessagesByIds.get(var10);
                  if (var22 != null) {
                     var22.deleted = true;
                  }
               }
            } else {
               this.markChannelDialogMessageAsDeleted(var1, var4);
            }

            ArrayList var23 = new ArrayList();

            for(var9 = 0; var9 < var1.size(); ++var9) {
               Integer var11 = (Integer)var1.get(var9);
               if (var11 > 0) {
                  var23.add(var11);
               }
            }

            MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeleted(var1, true, var4);
            MessagesStorage.getInstance(this.currentAccount).updateDialogsWithDeletedMessages(var1, (ArrayList)null, true, var4);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, var1, var4);
            var1 = var23;
         } else {
            var1 = null;
         }

         NativeByteBuffer var16;
         Exception var17;
         if (var4 != 0) {
            TLRPC.TL_channels_deleteMessages var18;
            if (var8 != null) {
               var18 = (TLRPC.TL_channels_deleteMessages)var8;
            } else {
               TLRPC.TL_channels_deleteMessages var19 = new TLRPC.TL_channels_deleteMessages();
               var19.id = var1;
               var19.channel = this.getInputChannel(var4);

               label85: {
                  label84: {
                     try {
                        var16 = new NativeByteBuffer(var19.getObjectSize() + 8);
                     } catch (Exception var15) {
                        var17 = var15;
                        var16 = null;
                        break label84;
                     }

                     try {
                        var16.writeInt32(7);
                        var16.writeInt32(var4);
                        var19.serializeToStream(var16);
                        break label85;
                     } catch (Exception var14) {
                        var17 = var14;
                     }
                  }

                  FileLog.e((Throwable)var17);
               }

               var6 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var16);
               var18 = var19;
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var18, new _$$Lambda$MessagesController$9B6lLHcMl9ABhpIDa9aeiv_K8oA(this, var4, var6));
         } else {
            if (var2 != null && var3 != null && !var2.isEmpty()) {
               SecretChatHelper.getInstance(this.currentAccount).sendMessagesDeleteMessage(var3, var2, (TLRPC.Message)null);
            }

            TLRPC.TL_messages_deleteMessages var21;
            if (var8 != null) {
               var21 = (TLRPC.TL_messages_deleteMessages)var8;
            } else {
               TLRPC.TL_messages_deleteMessages var20 = new TLRPC.TL_messages_deleteMessages();
               var20.id = var1;
               var20.revoke = var5;

               label71: {
                  label70: {
                     try {
                        var16 = new NativeByteBuffer(var20.getObjectSize() + 8);
                     } catch (Exception var13) {
                        var17 = var13;
                        var16 = null;
                        break label70;
                     }

                     try {
                        var16.writeInt32(7);
                        var16.writeInt32(var4);
                        var20.serializeToStream(var16);
                        break label71;
                     } catch (Exception var12) {
                        var17 = var12;
                     }
                  }

                  FileLog.e((Throwable)var17);
               }

               var6 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var16);
               var21 = var20;
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var21, new _$$Lambda$MessagesController$py2_IIRHD2uCa7xVMrMM8SVKTaA(this, var6));
         }

      }
   }

   public void deleteUserChannelHistory(TLRPC.Chat var1, TLRPC.User var2, int var3) {
      if (var3 == 0) {
         MessagesStorage.getInstance(this.currentAccount).deleteUserChannelHistory(var1.id, var2.id);
      }

      TLRPC.TL_channels_deleteUserHistory var4 = new TLRPC.TL_channels_deleteUserHistory();
      var4.channel = getInputChannel(var1);
      var4.user_id = this.getInputUser(var2);
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$MessagesController$_lPnTSKn3lxlWS4m8gq50UHxeAw(this, var1, var2));
   }

   public void deleteUserFromChat(int var1, TLRPC.User var2, TLRPC.ChatFull var3) {
      this.deleteUserFromChat(var1, var2, var3, false, false);
   }

   public void deleteUserFromChat(int var1, TLRPC.User var2, TLRPC.ChatFull var3, boolean var4, boolean var5) {
      if (var2 != null) {
         if (var1 > 0) {
            TLRPC.InputUser var6 = this.getInputUser(var2);
            TLRPC.Chat var7 = this.getChat(var1);
            boolean var8 = ChatObject.isChannel(var7);
            Object var10;
            if (var8) {
               if (var6 instanceof TLRPC.TL_inputUserSelf) {
                  if (var7.creator && var4) {
                     var10 = new TLRPC.TL_channels_deleteChannel();
                     ((TLRPC.TL_channels_deleteChannel)var10).channel = getInputChannel(var7);
                  } else {
                     var10 = new TLRPC.TL_channels_leaveChannel();
                     ((TLRPC.TL_channels_leaveChannel)var10).channel = getInputChannel(var7);
                  }
               } else {
                  var10 = new TLRPC.TL_channels_editBanned();
                  ((TLRPC.TL_channels_editBanned)var10).channel = getInputChannel(var7);
                  ((TLRPC.TL_channels_editBanned)var10).user_id = var6;
                  ((TLRPC.TL_channels_editBanned)var10).banned_rights = new TLRPC.TL_chatBannedRights();
                  TLRPC.TL_chatBannedRights var12 = ((TLRPC.TL_channels_editBanned)var10).banned_rights;
                  var12.view_messages = true;
                  var12.send_media = true;
                  var12.send_messages = true;
                  var12.send_stickers = true;
                  var12.send_gifs = true;
                  var12.send_games = true;
                  var12.send_inline = true;
                  var12.embed_links = true;
                  var12.pin_messages = true;
                  var12.send_polls = true;
                  var12.invite_users = true;
                  var12.change_info = true;
               }
            } else {
               var10 = new TLRPC.TL_messages_deleteChatUser();
               ((TLRPC.TL_messages_deleteChatUser)var10).chat_id = var1;
               ((TLRPC.TL_messages_deleteChatUser)var10).user_id = this.getInputUser(var2);
            }

            if (var2.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
               this.deleteDialog((long)(-var1), 0, var5);
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var10, new _$$Lambda$MessagesController$IE4i2TewkP8XTc8wTYcnuzt_GP0(this, var8, var6, var1), 64);
         } else if (var3 instanceof TLRPC.TL_chatFull) {
            TLRPC.Chat var11 = this.getChat(var1);
            --var11.participants_count;
            ArrayList var13 = new ArrayList();
            var13.add(var11);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats((ArrayList)null, var13, true, true);
            var1 = 0;

            boolean var9;
            while(true) {
               if (var1 >= var3.participants.participants.size()) {
                  var9 = false;
                  break;
               }

               if (((TLRPC.ChatParticipant)var3.participants.participants.get(var1)).user_id == var2.id) {
                  var3.participants.participants.remove(var1);
                  var9 = true;
                  break;
               }

               ++var1;
            }

            if (var9) {
               MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var3, true);
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var3, 0, false, null);
            }

            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 32);
         }

      }
   }

   public void deleteUserPhoto(TLRPC.InputPhoto var1) {
      if (var1 == null) {
         TLRPC.TL_photos_updateProfilePhoto var2 = new TLRPC.TL_photos_updateProfilePhoto();
         var2.id = new TLRPC.TL_inputPhotoEmpty();
         UserConfig.getInstance(this.currentAccount).getCurrentUser().photo = new TLRPC.TL_userProfilePhotoEmpty();
         TLRPC.User var3 = this.getUser(UserConfig.getInstance(this.currentAccount).getClientUserId());
         TLRPC.User var4 = var3;
         if (var3 == null) {
            var4 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
         }

         if (var4 == null) {
            return;
         }

         var4.photo = UserConfig.getInstance(this.currentAccount).getCurrentUser().photo;
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1535);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$MessagesController$3uRiNEdYGV34lhCOa9clmeKBj1M(this));
      } else {
         TLRPC.TL_photos_deletePhotos var5 = new TLRPC.TL_photos_deletePhotos();
         var5.id.add(var1);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, _$$Lambda$MessagesController$lnX7i9eGTDuJovXhCLpz1QuqmLE.INSTANCE);
      }

   }

   public void didAddedNewTask(int var1, SparseArray var2) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$LoBRzobgnJFkXmFzseR6vqsooQs(this, var1));
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$bHd9VuzZp__o4GfCjtn5be2_y4I(this, var2));
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      String var4;
      if (var1 == NotificationCenter.FileDidUpload) {
         var4 = (String)var3[0];
         TLRPC.InputFile var7 = (TLRPC.InputFile)var3[1];
         String var5 = this.uploadingAvatar;
         if (var5 != null && var5.equals(var4)) {
            TLRPC.TL_photos_uploadProfilePhoto var12 = new TLRPC.TL_photos_uploadProfilePhoto();
            var12.file = var7;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, new _$$Lambda$MessagesController$avFJzHcfb0E8c4wljuXOjwoiTOA(this));
         } else {
            var5 = this.uploadingWallpaper;
            if (var5 != null && var5.equals(var4)) {
               TLRPC.TL_account_uploadWallPaper var11 = new TLRPC.TL_account_uploadWallPaper();
               var11.file = var7;
               var11.mime_type = "image/jpeg";
               TLRPC.TL_wallPaperSettings var8 = new TLRPC.TL_wallPaperSettings();
               var8.blur = this.uploadingWallpaperBlurred;
               var8.motion = this.uploadingWallpaperMotion;
               var11.settings = var8;
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, new _$$Lambda$MessagesController$G2pKRuboIkVmyKgcxHs4KK8Bkn4(this, var8));
            }
         }
      } else if (var1 == NotificationCenter.FileDidFailUpload) {
         String var9 = (String)var3[0];
         var4 = this.uploadingAvatar;
         if (var4 != null && var4.equals(var9)) {
            this.uploadingAvatar = null;
         } else {
            var4 = this.uploadingWallpaper;
            if (var4 != null && var4.equals(var9)) {
               this.uploadingWallpaper = null;
            }
         }
      } else {
         MessageObject var10;
         if (var1 == NotificationCenter.messageReceivedByServer) {
            Integer var14 = (Integer)var3[0];
            Integer var15 = (Integer)var3[1];
            Long var6 = (Long)var3[3];
            var10 = (MessageObject)this.dialogMessage.get(var6);
            if (var10 != null && (var10.getId() == var14 || var10.messageOwner.local_id == var14)) {
               var10.messageOwner.id = var15;
               var10.messageOwner.send_state = 0;
            }

            TLRPC.Dialog var13 = (TLRPC.Dialog)this.dialogs_dict.get(var6);
            if (var13 != null && var13.top_message == var14) {
               var13.top_message = var15;
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
            }

            var10 = (MessageObject)this.dialogMessagesByIds.get(var14);
            this.dialogMessagesByIds.remove(var14);
            if (var10 != null) {
               this.dialogMessagesByIds.put(var15, var10);
            }
         } else if (var1 == NotificationCenter.updateMessageMedia) {
            TLRPC.Message var16 = (TLRPC.Message)var3[0];
            var10 = (MessageObject)this.dialogMessagesByIds.get(var16.id);
            if (var10 != null) {
               var10.messageOwner.media = var16.media;
               TLRPC.MessageMedia var17 = var16.media;
               if (var17.ttl_seconds != 0 && (var17.photo instanceof TLRPC.TL_photoEmpty || var17.document instanceof TLRPC.TL_documentEmpty)) {
                  var10.setType();
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated);
               }
            }
         }
      }

   }

   public void forceResetDialogs() {
      this.resetDialogs(true, MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
      NotificationsController.getInstance(this.currentAccount).deleteAllNotificationChannels();
   }

   public void generateJoinMessage(int var1, boolean var2) {
      TLRPC.Chat var3 = this.getChat(var1);
      if (var3 != null && ChatObject.isChannel(var1, this.currentAccount) && (!var3.left && !var3.kicked || var2)) {
         TLRPC.TL_messageService var4 = new TLRPC.TL_messageService();
         var4.flags = 256;
         int var5 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
         var4.id = var5;
         var4.local_id = var5;
         var4.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         var4.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
         var4.to_id = new TLRPC.TL_peerChannel();
         var4.to_id.channel_id = var1;
         var4.dialog_id = (long)(-var1);
         var4.post = true;
         var4.action = new TLRPC.TL_messageActionChatAddUser();
         var4.action.users.add(UserConfig.getInstance(this.currentAccount).getClientUserId());
         if (var3.megagroup) {
            var4.flags |= Integer.MIN_VALUE;
         }

         UserConfig.getInstance(this.currentAccount).saveConfig(false);
         ArrayList var7 = new ArrayList();
         ArrayList var6 = new ArrayList();
         var6.add(var4);
         var7.add(new MessageObject(this.currentAccount, var4, true));
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$goN3mCs2oD94EPYohjkaDpROs2E(this, var7));
         MessagesStorage.getInstance(this.currentAccount).putMessages(var6, true, true, false, 0);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$OUR20mf5_d9gsY1zs0m8SWbYyH8(this, var1, var7));
      }

   }

   public void generateUpdateMessage() {
      if (!BuildVars.DEBUG_VERSION) {
         String var1 = SharedConfig.lastUpdateVersion;
         if (var1 != null && !var1.equals(BuildVars.BUILD_VERSION_STRING)) {
            TLRPC.TL_help_getAppChangelog var2 = new TLRPC.TL_help_getAppChangelog();
            var2.prev_app_version = SharedConfig.lastUpdateVersion;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$MessagesController$IayzOkIFtocnVFRbLM_8qmLr_wI(this));
         }
      }

   }

   public ArrayList getAllDialogs() {
      return this.allDialogs;
   }

   public void getBlockedUsers(boolean var1) {
      if (UserConfig.getInstance(this.currentAccount).isClientActivated() && !this.loadingBlockedUsers) {
         this.loadingBlockedUsers = true;
         if (var1) {
            MessagesStorage.getInstance(this.currentAccount).getBlockedUsers();
         } else {
            TLRPC.TL_contacts_getBlocked var2 = new TLRPC.TL_contacts_getBlocked();
            var2.offset = 0;
            var2.limit = 200;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$MessagesController$R5tZYjWVTciy1Jz1GsIoNOmiXf8(this));
         }
      }

   }

   protected void getChannelDifference(int var1, int var2, long var3, TLRPC.InputChannel var5) {
      if (!this.gettingDifferenceChannels.get(var1)) {
         byte var6 = 100;
         boolean var7 = true;
         int var8;
         if (var2 == 1) {
            if (this.channelsPts.get(var1) != 0) {
               return;
            }

            var6 = 1;
            var8 = 1;
         } else {
            var8 = this.channelsPts.get(var1);
            int var9 = var8;
            if (var8 == 0) {
               var8 = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(var1);
               if (var8 != 0) {
                  this.channelsPts.put(var1, var8);
               }

               var9 = var8;
               if (var8 == 0) {
                  label101: {
                     if (var2 != 2) {
                        var9 = var8;
                        if (var2 != 3) {
                           break label101;
                        }
                     }

                     return;
                  }
               }
            }

            var8 = var9;
            if (var9 == 0) {
               return;
            }
         }

         TLRPC.InputChannel var19;
         if (var5 == null) {
            TLRPC.Chat var10 = this.getChat(var1);
            TLRPC.Chat var16 = var10;
            if (var10 == null) {
               var10 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var1);
               var16 = var10;
               if (var10 != null) {
                  this.putChat(var10, true);
                  var16 = var10;
               }
            }

            var19 = getInputChannel(var16);
         } else {
            var19 = var5;
         }

         if (var19 != null && var19.access_hash != 0L) {
            long var11 = var3;
            if (var3 == 0L) {
               NativeByteBuffer var17;
               label69: {
                  Exception var13;
                  label68: {
                     try {
                        var17 = new NativeByteBuffer(var19.getObjectSize() + 12);
                     } catch (Exception var15) {
                        var13 = var15;
                        var17 = null;
                        break label68;
                     }

                     try {
                        var17.writeInt32(6);
                        var17.writeInt32(var1);
                        var17.writeInt32(var2);
                        var19.serializeToStream(var17);
                        break label69;
                     } catch (Exception var14) {
                        var13 = var14;
                     }
                  }

                  FileLog.e((Throwable)var13);
               }

               var11 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var17);
            }

            this.gettingDifferenceChannels.put(var1, true);
            TLRPC.TL_updates_getChannelDifference var18 = new TLRPC.TL_updates_getChannelDifference();
            var18.channel = var19;
            var18.filter = new TLRPC.TL_channelMessagesFilterEmpty();
            var18.pts = var8;
            var18.limit = var6;
            if (var2 == 3) {
               var7 = false;
            }

            var18.force = var7;
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var20 = new StringBuilder();
               var20.append("start getChannelDifference with pts = ");
               var20.append(var8);
               var20.append(" channelId = ");
               var20.append(var1);
               FileLog.d(var20.toString());
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var18, new _$$Lambda$MessagesController$x3OtyFEBR3d2oz5IdcBDlqWOR_4(this, var1, var2, var11));
         } else {
            if (var3 != 0L) {
               MessagesStorage.getInstance(this.currentAccount).removePendingTask(var3);
            }

         }
      }
   }

   public TLRPC.Chat getChat(Integer var1) {
      return (TLRPC.Chat)this.chats.get(var1);
   }

   public TLRPC.ChatFull getChatFull(int var1) {
      return (TLRPC.ChatFull)this.fullChats.get(var1);
   }

   public ArrayList getDialogs(int var1) {
      ArrayList var2 = (ArrayList)this.dialogsByFolder.get(var1);
      ArrayList var3 = var2;
      if (var2 == null) {
         var3 = new ArrayList();
      }

      return var3;
   }

   public void getDifference() {
      this.getDifference(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue(), false);
   }

   public void getDifference(int var1, int var2, int var3, boolean var4) {
      this.registerForPush(SharedConfig.pushString);
      if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() == 0) {
         this.loadCurrentState();
      } else if (var4 || !this.gettingDifference) {
         this.gettingDifference = true;
         TLRPC.TL_updates_getDifference var5 = new TLRPC.TL_updates_getDifference();
         var5.pts = var1;
         var5.date = var2;
         var5.qts = var3;
         if (this.getDifferenceFirstSync) {
            var5.flags |= 1;
            if (ApplicationLoader.isConnectedOrConnectingToWiFi()) {
               var5.pts_total_limit = 5000;
            } else {
               var5.pts_total_limit = 1000;
            }

            this.getDifferenceFirstSync = false;
         }

         if (var5.date == 0) {
            var5.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         }

         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var6 = new StringBuilder();
            var6.append("start getDifference with date = ");
            var6.append(var2);
            var6.append(" pts = ");
            var6.append(var1);
            var6.append(" qts = ");
            var6.append(var3);
            FileLog.d(var6.toString());
         }

         ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(true);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, new _$$Lambda$MessagesController$TiSv3h40ZHze2ul5Mr_eNiZvHcc(this, var2, var3));
      }
   }

   public TLRPC.EncryptedChat getEncryptedChat(Integer var1) {
      return (TLRPC.EncryptedChat)this.encryptedChats.get(var1);
   }

   public TLRPC.EncryptedChat getEncryptedChatDB(int var1, boolean var2) {
      TLRPC.EncryptedChat var3 = (TLRPC.EncryptedChat)this.encryptedChats.get(var1);
      TLRPC.EncryptedChat var4;
      if (var3 != null) {
         var4 = var3;
         if (!var2) {
            return var4;
         }

         if (!(var3 instanceof TLRPC.TL_encryptedChatWaiting)) {
            var4 = var3;
            if (!(var3 instanceof TLRPC.TL_encryptedChatRequested)) {
               return var4;
            }
         }
      }

      CountDownLatch var8 = new CountDownLatch(1);
      ArrayList var5 = new ArrayList();
      MessagesStorage.getInstance(this.currentAccount).getEncryptedChat(var1, var8, var5);

      try {
         var8.await();
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      var4 = var3;
      if (var5.size() == 2) {
         var4 = (TLRPC.EncryptedChat)var5.get(0);
         TLRPC.User var7 = (TLRPC.User)var5.get(1);
         this.putEncryptedChat(var4, false);
         this.putUser(var7, true);
      }

      return var4;
   }

   public TLRPC.ExportedChatInvite getExportedInvite(int var1) {
      return (TLRPC.ExportedChatInvite)this.exportedChats.get(var1);
   }

   public TLRPC.InputChannel getInputChannel(int var1) {
      return getInputChannel(this.getChat(var1));
   }

   public TLRPC.InputPeer getInputPeer(int var1) {
      Object var3;
      if (var1 < 0) {
         var1 = -var1;
         TLRPC.Chat var2 = this.getChat(var1);
         if (ChatObject.isChannel(var2)) {
            var3 = new TLRPC.TL_inputPeerChannel();
            ((TLRPC.InputPeer)var3).channel_id = var1;
            ((TLRPC.InputPeer)var3).access_hash = var2.access_hash;
         } else {
            var3 = new TLRPC.TL_inputPeerChat();
            ((TLRPC.InputPeer)var3).chat_id = var1;
         }
      } else {
         TLRPC.User var4 = this.getUser(var1);
         TLRPC.TL_inputPeerUser var5 = new TLRPC.TL_inputPeerUser();
         var5.user_id = var1;
         var3 = var5;
         if (var4 != null) {
            var5.access_hash = var4.access_hash;
            var3 = var5;
         }
      }

      return (TLRPC.InputPeer)var3;
   }

   public TLRPC.InputUser getInputUser(int var1) {
      return this.getInputUser(getInstance(UserConfig.selectedAccount).getUser(var1));
   }

   public TLRPC.InputUser getInputUser(TLRPC.User var1) {
      if (var1 == null) {
         return new TLRPC.TL_inputUserEmpty();
      } else {
         Object var3;
         if (var1.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            var3 = new TLRPC.TL_inputUserSelf();
         } else {
            TLRPC.TL_inputUser var2 = new TLRPC.TL_inputUser();
            var2.user_id = var1.id;
            var2.access_hash = var1.access_hash;
            var3 = var2;
         }

         return (TLRPC.InputUser)var3;
      }
   }

   public void getNewDeleteTask(ArrayList var1, int var2) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$uJw9E53v9xDGKUuX24rMZpaODgA(this, var1, var2));
   }

   public TLRPC.Peer getPeer(int var1) {
      Object var3;
      if (var1 < 0) {
         var1 = -var1;
         TLRPC.Chat var2 = this.getChat(var1);
         if (!(var2 instanceof TLRPC.TL_channel) && !(var2 instanceof TLRPC.TL_channelForbidden)) {
            var3 = new TLRPC.TL_peerChat();
            ((TLRPC.Peer)var3).chat_id = var1;
         } else {
            var3 = new TLRPC.TL_peerChannel();
            ((TLRPC.Peer)var3).channel_id = var1;
         }
      } else {
         this.getUser(var1);
         var3 = new TLRPC.TL_peerUser();
         ((TLRPC.Peer)var3).user_id = var1;
      }

      return (TLRPC.Peer)var3;
   }

   public long getUpdatesStartTime(int var1) {
      if (var1 == 0) {
         return this.updatesStartWaitTimeSeq;
      } else if (var1 == 1) {
         return this.updatesStartWaitTimePts;
      } else {
         return var1 == 2 ? this.updatesStartWaitTimeQts : 0L;
      }
   }

   public TLRPC.User getUser(Integer var1) {
      return (TLRPC.User)this.users.get(var1);
   }

   public TLRPC.UserFull getUserFull(int var1) {
      return (TLRPC.UserFull)this.fullUsers.get(var1);
   }

   public TLObject getUserOrChat(String var1) {
      return var1 != null && var1.length() != 0 ? (TLObject)this.objectsByUsernames.get(var1.toLowerCase()) : null;
   }

   public ConcurrentHashMap getUsers() {
      return this.users;
   }

   public boolean hasHiddenArchive() {
      boolean var1 = SharedConfig.archiveHidden;
      boolean var2 = true;
      if (!var1 || this.dialogs_dict.get(DialogObject.makeFolderDialogId(1)) == null) {
         var2 = false;
      }

      return var2;
   }

   public void hideReportSpam(long var1, TLRPC.User var3, TLRPC.Chat var4) {
      if (var3 != null || var4 != null) {
         Editor var5 = this.notificationsPreferences.edit();
         StringBuilder var6 = new StringBuilder();
         var6.append("spam3_");
         var6.append(var1);
         var5.putInt(var6.toString(), 1);
         var5.commit();
         if ((int)var1 != 0) {
            TLRPC.TL_messages_hideReportSpam var7 = new TLRPC.TL_messages_hideReportSpam();
            if (var3 != null) {
               var7.peer = this.getInputPeer(var3.id);
            } else if (var4 != null) {
               var7.peer = this.getInputPeer(-var4.id);
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, _$$Lambda$MessagesController$48y_t5lk6J6veA_TARwt8n5wq_Y.INSTANCE);
         }

      }
   }

   public boolean isChannelAdmin(int var1, int var2) {
      ArrayList var3 = (ArrayList)this.channelAdmins.get(var1);
      boolean var4;
      if (var3 != null && var3.indexOf(var2) >= 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }

   public boolean isClearingDialog(long var1) {
      boolean var3;
      if (this.clearingHistoryDialogs.get(var1) != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isDialogCreated(long var1) {
      return this.createdDialogMainThreadIds.contains(var1);
   }

   public boolean isDialogMuted(long var1) {
      SharedPreferences var3 = this.notificationsPreferences;
      StringBuilder var4 = new StringBuilder();
      var4.append("notify2_");
      var4.append(var1);
      int var5 = var3.getInt(var4.toString(), -1);
      if (var5 == -1) {
         return NotificationsController.getInstance(this.currentAccount).isGlobalNotificationsEnabled(var1) ^ true;
      } else if (var5 == 2) {
         return true;
      } else {
         if (var5 == 3) {
            var3 = this.notificationsPreferences;
            var4 = new StringBuilder();
            var4.append("notifyuntil_");
            var4.append(var1);
            if (var3.getInt(var4.toString(), 0) >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isDialogVisible(long var1) {
      return this.visibleDialogMainThreadIds.contains(var1);
   }

   public boolean isDialogsEndReached(int var1) {
      return this.dialogsEndReached.get(var1);
   }

   public boolean isJoiningChannel(int var1) {
      return this.joiningToChannels.contains(var1);
   }

   public boolean isLoadingDialogs(int var1) {
      return this.loadingDialogs.get(var1);
   }

   public boolean isProxyDialog(long var1, boolean var3) {
      TLRPC.Dialog var4 = this.proxyDialog;
      if (var4 == null || var4.id != var1 || var3 && !this.isLeftProxyChannel) {
         var3 = false;
      } else {
         var3 = true;
      }

      return var3;
   }

   public boolean isServerDialogsEndReached(int var1) {
      return this.serverDialogsEndReached.get(var1);
   }

   // $FF: synthetic method
   public void lambda$addDialogToFolder$112$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         this.processUpdates((TLRPC.Updates)var3, false);
      }

      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$addToViewsQueue$139$MessagesController(MessageObject var1) {
      int var2 = (int)var1.getDialogId();
      int var3 = var1.getId();
      ArrayList var4 = (ArrayList)this.channelViewsToSend.get(var2);
      ArrayList var5 = var4;
      if (var4 == null) {
         var5 = new ArrayList();
         this.channelViewsToSend.put(var2, var5);
      }

      if (!var5.contains(var3)) {
         var5.add(var3);
      }

   }

   // $FF: synthetic method
   public void lambda$addUserToChat$180$MessagesController(boolean var1, TLRPC.InputUser var2, int var3, BaseFragment var4, TLObject var5, boolean var6, Runnable var7, TLObject var8, TLRPC.TL_error var9) {
      if (var1 && var2 instanceof TLRPC.TL_inputUserSelf) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$gHAV7k8HUui7jwavTT5ub_YwyHk(this, var3));
      }

      if (var9 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$FzKpDwgMKXFwcQpN6PW_r4fPJ0A(this, var9, var4, var5, var1, var6, var2));
      } else {
         TLRPC.Updates var12 = (TLRPC.Updates)var8;
         int var10 = 0;

         boolean var13;
         while(true) {
            if (var10 >= var12.updates.size()) {
               var13 = false;
               break;
            }

            TLRPC.Update var11 = (TLRPC.Update)var12.updates.get(var10);
            if (var11 instanceof TLRPC.TL_updateNewChannelMessage && ((TLRPC.TL_updateNewChannelMessage)var11).message.action instanceof TLRPC.TL_messageActionChatAddUser) {
               var13 = true;
               break;
            }

            ++var10;
         }

         this.processUpdates(var12, false);
         if (var1) {
            if (!var13 && var2 instanceof TLRPC.TL_inputUserSelf) {
               this.generateJoinMessage(var3, true);
            }

            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$8h8cgPEI6kzZTDRtvYPddJzervw(this, var3), 1000L);
         }

         if (var1 && var2 instanceof TLRPC.TL_inputUserSelf) {
            MessagesStorage.getInstance(this.currentAccount).updateDialogsWithDeletedMessages(new ArrayList(), (ArrayList)null, true, var3);
         }

         if (var7 != null) {
            AndroidUtilities.runOnUIThread(var7);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$addUsersToChannel$167$MessagesController(BaseFragment var1, TLRPC.TL_channels_inviteToChannel var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$JvtxNBYrSDi77SdNL_OBKU4hIZo(this, var4, var1, var2));
      } else {
         this.processUpdates((TLRPC.Updates)var3, false);
      }
   }

   // $FF: synthetic method
   public void lambda$blockUser$39$MessagesController(TLRPC.User var1, TLObject var2, TLRPC.TL_error var3) {
      if (var3 == null) {
         SparseIntArray var4 = new SparseIntArray();
         var4.put(var1.id, 1);
         MessagesStorage.getInstance(this.currentAccount).putBlockedUsers(var4, false);
      }

   }

   // $FF: synthetic method
   public void lambda$changeChatAvatar$184$MessagesController(TLRPC.FileLocation var1, TLRPC.FileLocation var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.Updates var15 = (TLRPC.Updates)var3;
         int var5 = var15.updates.size();
         int var6 = 0;

         TLRPC.Photo var14;
         while(true) {
            if (var6 >= var5) {
               var14 = null;
               break;
            }

            TLRPC.Update var12 = (TLRPC.Update)var15.updates.get(var6);
            TLRPC.MessageAction var13;
            if (var12 instanceof TLRPC.TL_updateNewChannelMessage) {
               var13 = ((TLRPC.TL_updateNewChannelMessage)var12).message.action;
               if (var13 instanceof TLRPC.TL_messageActionChatEditPhoto) {
                  var14 = var13.photo;
                  if (var14 instanceof TLRPC.TL_photo) {
                     break;
                  }
               }
            } else if (var12 instanceof TLRPC.TL_updateNewMessage) {
               var13 = ((TLRPC.TL_updateNewMessage)var12).message.action;
               if (var13 instanceof TLRPC.TL_messageActionChatEditPhoto) {
                  var14 = var13.photo;
                  if (var14 instanceof TLRPC.TL_photo) {
                     break;
                  }
               }
            }

            ++var6;
         }

         if (var14 != null) {
            TLRPC.PhotoSize var7 = FileLoader.getClosestPhotoSizeWithSize(var14.sizes, 150);
            if (var7 != null && var1 != null) {
               File var8 = FileLoader.getPathToAttach(var7, true);
               FileLoader.getPathToAttach(var1, true).renameTo(var8);
               StringBuilder var16 = new StringBuilder();
               var16.append(var1.volume_id);
               var16.append("_");
               var16.append(var1.local_id);
               var16.append("@50_50");
               String var9 = var16.toString();
               var16 = new StringBuilder();
               var16.append(var7.location.volume_id);
               var16.append("_");
               var16.append(var7.location.local_id);
               var16.append("@50_50");
               String var17 = var16.toString();
               ImageLoader.getInstance().replaceImageInCache(var9, var17, ImageLocation.getForPhoto(var7, var14), true);
            }

            TLRPC.PhotoSize var10 = FileLoader.getClosestPhotoSizeWithSize(var14.sizes, 800);
            if (var10 != null && var2 != null) {
               File var11 = FileLoader.getPathToAttach(var10, true);
               FileLoader.getPathToAttach(var2, true).renameTo(var11);
            }
         }

         this.processUpdates(var15, false);
      }
   }

   // $FF: synthetic method
   public void lambda$changeChatTitle$183$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         this.processUpdates((TLRPC.Updates)var1, false);
      }
   }

   // $FF: synthetic method
   public void lambda$checkCanOpenChat$257$MessagesController(AlertDialog var1, BaseFragment var2, Bundle var3, TLObject var4, TLRPC.TL_error var5) {
      if (var4 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$khcet7XZs3bzMGRlSwPxErmYGcs(this, var1, var4, var2, var3));
      }

   }

   // $FF: synthetic method
   public void lambda$checkCanOpenChat$258$MessagesController(int var1, BaseFragment var2, DialogInterface var3) {
      ConnectionsManager.getInstance(this.currentAccount).cancelRequest(var1, true);
      if (var2 != null) {
         var2.setVisibleDialog((Dialog)null);
      }

   }

   // $FF: synthetic method
   public void lambda$checkChannelInviter$231$MessagesController(int var1) {
      TLRPC.Chat var2 = this.getChat(var1);
      if (var2 != null && ChatObject.isChannel(var1, this.currentAccount) && !var2.creator) {
         TLRPC.TL_channels_getParticipant var3 = new TLRPC.TL_channels_getParticipant();
         var3.channel = this.getInputChannel(var1);
         var3.user_id = new TLRPC.TL_inputUserSelf();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$8UBdkj2QNJAC0y4BPGNDaiXhz9c(this, var2, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$checkDeletingTask$34$MessagesController(ArrayList var1) {
      if (!var1.isEmpty() && (Integer)var1.get(0) > 0) {
         MessagesStorage.getInstance(this.currentAccount).emptyMessagesMedia(var1);
      } else {
         this.deleteMessages(var1, (ArrayList)null, (TLRPC.EncryptedChat)null, 0, false);
      }

      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$Ipa0eWaVXBEreH61uO9jqrPCC0g(this, var1));
   }

   // $FF: synthetic method
   public void lambda$checkLastDialogMessage$136$MessagesController(int var1, TLRPC.Dialog var2, long var3, TLObject var5, TLRPC.TL_error var6) {
      if (var5 != null) {
         TLRPC.messages_Messages var12 = (TLRPC.messages_Messages)var5;
         this.removeDeletedMessagesFromArray((long)var1, var12.messages);
         if (!var12.messages.isEmpty()) {
            TLRPC.TL_messages_dialogs var7 = new TLRPC.TL_messages_dialogs();
            TLRPC.Message var11 = (TLRPC.Message)var12.messages.get(0);
            TLRPC.TL_dialog var8 = new TLRPC.TL_dialog();
            var8.flags = var2.flags;
            var8.top_message = var11.id;
            var8.last_message_date = var11.date;
            var8.notify_settings = var2.notify_settings;
            var8.pts = var2.pts;
            var8.unread_count = var2.unread_count;
            var8.unread_mark = var2.unread_mark;
            var8.unread_mentions_count = var2.unread_mentions_count;
            var8.read_inbox_max_id = var2.read_inbox_max_id;
            var8.read_outbox_max_id = var2.read_outbox_max_id;
            var8.pinned = var2.pinned;
            var8.pinnedNum = var2.pinnedNum;
            var8.folder_id = var2.folder_id;
            long var9 = var2.id;
            var8.id = var9;
            var11.dialog_id = var9;
            var7.users.addAll(var12.users);
            var7.chats.addAll(var12.chats);
            var7.dialogs.add(var8);
            var7.messages.addAll(var12.messages);
            var7.count = 1;
            this.processDialogsUpdate(var7, (ArrayList)null);
            MessagesStorage.getInstance(this.currentAccount).putMessages(var12.messages, true, true, false, DownloadController.getInstance(this.currentAccount).getAutodownloadMask(), true);
         } else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$B2hd0oVj2x9ARog9ivfgDWOv_vg(this, var2));
         }
      }

      if (var3 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var3);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Vi0DJPkGrsNPy_GOeNVBnJWDwnE(this, var1));
   }

   // $FF: synthetic method
   public void lambda$checkProxyInfo$89$MessagesController(boolean var1) {
      this.checkProxyInfoInternal(var1);
   }

   // $FF: synthetic method
   public void lambda$checkProxyInfoInternal$95$MessagesController(String var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      if (this.checkingProxyInfoRequestId != 0) {
         boolean var5 = var3 instanceof TLRPC.TL_help_proxyDataEmpty;
         boolean var6 = true;
         boolean var7 = true;
         if (var5) {
            this.nextProxyInfoCheckTime = ((TLRPC.TL_help_proxyDataEmpty)var3).expires;
            var7 = var6;
         } else if (var3 instanceof TLRPC.TL_help_proxyDataPromo) {
            long var8;
            TLRPC.TL_help_proxyDataPromo var12;
            label60: {
               var12 = (TLRPC.TL_help_proxyDataPromo)var3;
               TLRPC.Peer var13 = var12.peer;
               int var16 = var13.user_id;
               if (var16 != 0) {
                  var8 = (long)var16;
               } else {
                  var16 = var13.chat_id;
                  long var10;
                  TLRPC.Chat var14;
                  if (var16 != 0) {
                     var10 = (long)(-var16);
                     var16 = 0;

                     while(true) {
                        var8 = var10;
                        if (var16 >= var12.chats.size()) {
                           break;
                        }

                        var14 = (TLRPC.Chat)var12.chats.get(var16);
                        if (var14.id == var12.peer.chat_id) {
                           var6 = var7;
                           var8 = var10;
                           if (var14.kicked) {
                              break label60;
                           }

                           var8 = var10;
                           if (var14.restricted) {
                              var6 = var7;
                              var8 = var10;
                              break label60;
                           }
                           break;
                        }

                        ++var16;
                     }
                  } else {
                     var10 = (long)(-var13.channel_id);
                     var16 = 0;

                     while(true) {
                        var8 = var10;
                        if (var16 >= var12.chats.size()) {
                           break;
                        }

                        var14 = (TLRPC.Chat)var12.chats.get(var16);
                        if (var14.id == var12.peer.channel_id) {
                           var6 = var7;
                           var8 = var10;
                           if (var14.kicked) {
                              break label60;
                           }

                           var8 = var10;
                           if (var14.restricted) {
                              var6 = var7;
                              var8 = var10;
                              break label60;
                           }
                           break;
                        }

                        ++var16;
                     }
                  }
               }

               var6 = false;
            }

            this.proxyDialogId = var8;
            StringBuilder var15 = new StringBuilder();
            var15.append(var1);
            var15.append(var2);
            this.proxyDialogAddress = var15.toString();
            getGlobalMainSettings().edit().putLong("proxy_dialog", this.proxyDialogId).putString("proxyDialogAddress", this.proxyDialogAddress).commit();
            this.nextProxyInfoCheckTime = var12.expires;
            var7 = var6;
            if (!var6) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$0mxeOwB8KketmB2b8T1pp9iml0o(this, var8, var12));
               var7 = var6;
            }
         } else {
            this.nextProxyInfoCheckTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 3600;
            var7 = var6;
         }

         if (var7) {
            this.proxyDialogId = 0L;
            getGlobalMainSettings().edit().putLong("proxy_dialog", this.proxyDialogId).remove("proxyDialogAddress").commit();
            this.checkingProxyInfoRequestId = 0;
            this.checkingProxyInfo = false;
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Kx39oNX6WpYn1825Y_0aDSwCf7M(this));
         }

      }
   }

   // $FF: synthetic method
   public void lambda$checkProxyInfoInternal$96$MessagesController() {
      TLRPC.Dialog var1 = this.proxyDialog;
      if (var1 != null) {
         int var2 = (int)var1.id;
         if (var2 < 0) {
            TLRPC.Chat var3 = this.getChat(-var2);
            if (ChatObject.isNotInChat(var3) || var3.restricted) {
               this.removeDialog(this.proxyDialog);
            }
         } else {
            this.removeDialog(var1);
         }

         this.proxyDialog = null;
         this.sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      }

   }

   // $FF: synthetic method
   public void lambda$checkTosUpdate$88$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      this.checkingTosUpdate = false;
      if (var1 instanceof TLRPC.TL_help_termsOfServiceUpdateEmpty) {
         this.nextTosCheckTime = ((TLRPC.TL_help_termsOfServiceUpdateEmpty)var1).expires;
      } else if (var1 instanceof TLRPC.TL_help_termsOfServiceUpdate) {
         TLRPC.TL_help_termsOfServiceUpdate var3 = (TLRPC.TL_help_termsOfServiceUpdate)var1;
         this.nextTosCheckTime = var3.expires;
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$fH6gNCG5Tk_fGc0NroG1SVH_0DA(this, var3));
      } else {
         this.nextTosCheckTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 3600;
      }

      this.notificationsPreferences.edit().putInt("nextTosCheckTime", this.nextTosCheckTime).commit();
   }

   // $FF: synthetic method
   public void lambda$cleanup$7$MessagesController() {
      this.readTasks.clear();
      this.readTasksMap.clear();
      this.updatesQueueSeq.clear();
      this.updatesQueuePts.clear();
      this.updatesQueueQts.clear();
      this.gettingUnknownChannels.clear();
      this.gettingUnknownDialogs.clear();
      this.updatesStartWaitTimeSeq = 0L;
      this.updatesStartWaitTimePts = 0L;
      this.updatesStartWaitTimeQts = 0L;
      this.createdDialogIds.clear();
      this.gettingDifference = false;
      this.resetDialogsPinned = null;
      this.resetDialogsAll = null;
   }

   // $FF: synthetic method
   public void lambda$cleanup$8$MessagesController() {
      ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(false);
      this.updatesQueueChannels.clear();
      this.updatesStartWaitTimeChannels.clear();
      this.gettingDifferenceChannels.clear();
      this.channelsPts.clear();
      this.shortPollChannels.clear();
      this.needShortPollChannels.clear();
      this.shortPollOnlines.clear();
      this.needShortPollOnlines.clear();
   }

   // $FF: synthetic method
   public void lambda$completeDialogsReset$122$MessagesController(int var1, int var2, int var3, TLRPC.messages_Dialogs var4, LongSparseArray var5, LongSparseArray var6) {
      this.gettingDifference = false;
      MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var1);
      MessagesStorage.getInstance(this.currentAccount).setLastDateValue(var2);
      MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(var3);
      this.getDifference();
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$_JxxcnOrF9NEl3xKvCXKO_45t_Q(this, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$completeReadTask$146$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null && var1 instanceof TLRPC.TL_messages_affectedMessages) {
         TLRPC.TL_messages_affectedMessages var3 = (TLRPC.TL_messages_affectedMessages)var1;
         this.processNewDifferenceParams(-1, var3.pts, -1, var3.pts_count);
      }

   }

   // $FF: synthetic method
   public void lambda$convertToMegaGroup$164$MessagesController(Context var1, AlertDialog var2, MessagesStorage.IntCallback var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$LMu_cf6mwPFEIl5BHsSN1A4tDck(var1, var2));
         TLRPC.Updates var6 = (TLRPC.Updates)var4;
         this.processUpdates(var6, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$ccOtwUqv0B2ZPbEC4t1iqZYvu2U(var3, var6));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$04Zo7VbxddrCSSpeIC1emxiw6QE(var1, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$convertToMegaGroup$165$MessagesController(int var1, DialogInterface var2) {
      ConnectionsManager.getInstance(this.currentAccount).cancelRequest(var1, true);
   }

   // $FF: synthetic method
   public void lambda$createChat$157$MessagesController(BaseFragment var1, TLRPC.TL_messages_createChat var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$WsumHWD0PnGJZt7IOqYp5KdvEJ0(this, var4, var1, var2));
      } else {
         TLRPC.Updates var5 = (TLRPC.Updates)var3;
         this.processUpdates(var5, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$eAZO9_fJVDn41rVP_qwqU45S3MA(this, var5));
      }
   }

   // $FF: synthetic method
   public void lambda$createChat$160$MessagesController(BaseFragment var1, TLRPC.TL_channels_createChannel var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$1QEtWqNTIuN7FfcY0dyqyX9a0nc(this, var4, var1, var2));
      } else {
         TLRPC.Updates var5 = (TLRPC.Updates)var3;
         this.processUpdates(var5, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$wCKcwOawGryKMm_2e04hmPYVRmA(this, var5));
      }
   }

   // $FF: synthetic method
   public void lambda$deleteDialog$67$MessagesController(long var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$KpbyllFi8c4LuRHme4BUUg14Dek(this, var1));
   }

   // $FF: synthetic method
   public void lambda$deleteDialog$69$MessagesController(long var1, long var3, TLObject var5, TLRPC.TL_error var6) {
      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$DwkK6UVHlAwcTaxAil0Ui6ghGR0(this, var3));
   }

   // $FF: synthetic method
   public void lambda$deleteDialog$70$MessagesController(long var1, long var3, int var5, int var6, boolean var7, TLRPC.InputPeer var8, TLObject var9, TLRPC.TL_error var10) {
      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

      if (var10 == null) {
         TLRPC.TL_messages_affectedHistory var11 = (TLRPC.TL_messages_affectedHistory)var9;
         if (var11.offset > 0) {
            this.deleteDialog(var3, false, var5, var6, var7, var8, 0L);
         }

         this.processNewDifferenceParams(-1, var11.pts, -1, var11.pts_count);
         MessagesStorage.getInstance(this.currentAccount).onDeleteQueryComplete(var3);
      }

   }

   // $FF: synthetic method
   public void lambda$deleteMessages$61$MessagesController(int var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         TLRPC.TL_messages_affectedMessages var6 = (TLRPC.TL_messages_affectedMessages)var4;
         this.processNewChannelDifferenceParams(var6.pts, var6.pts_count, var1);
      }

      if (var2 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var2);
      }

   }

   // $FF: synthetic method
   public void lambda$deleteMessages$62$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.TL_messages_affectedMessages var5 = (TLRPC.TL_messages_affectedMessages)var3;
         this.processNewDifferenceParams(-1, var5.pts, -1, var5.pts_count);
      }

      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$deleteUserChannelHistory$64$MessagesController(TLRPC.Chat var1, TLRPC.User var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.TL_messages_affectedHistory var6 = (TLRPC.TL_messages_affectedHistory)var3;
         int var5 = var6.offset;
         if (var5 > 0) {
            this.deleteUserChannelHistory(var1, var2, var5);
         }

         this.processNewChannelDifferenceParams(var6.pts, var6.pts_count, var1.id);
      }

   }

   // $FF: synthetic method
   public void lambda$deleteUserFromChat$182$MessagesController(boolean var1, TLRPC.InputUser var2, int var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         this.processUpdates((TLRPC.Updates)var4, false);
         if (var1 && !(var2 instanceof TLRPC.TL_inputUserSelf)) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$xTU7cEe4T3HaWaI7TTVa9Ed_zvk(this, var3), 1000L);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$deleteUserPhoto$57$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         TLRPC.User var4 = this.getUser(UserConfig.getInstance(this.currentAccount).getClientUserId());
         if (var4 == null) {
            var4 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
            this.putUser(var4, false);
         } else {
            UserConfig.getInstance(this.currentAccount).setCurrentUser(var4);
         }

         if (var4 == null) {
            return;
         }

         MessagesStorage.getInstance(this.currentAccount).clearUserPhotos(var4.id);
         ArrayList var3 = new ArrayList();
         var3.add(var4);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var3, (ArrayList)null, false, true);
         var4.photo = (TLRPC.UserProfilePhoto)var1;
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$FS6UDAATQ6vB2PUbj_Uk45N_XpU(this));
      }

   }

   // $FF: synthetic method
   public void lambda$didAddedNewTask$30$MessagesController(int var1) {
      if (this.currentDeletingTaskMids != null || this.gettingNewDeleteTask) {
         int var2 = this.currentDeletingTaskTime;
         if (var2 == 0 || var1 >= var2) {
            return;
         }
      }

      this.getNewDeleteTask((ArrayList)null, 0);
   }

   // $FF: synthetic method
   public void lambda$didAddedNewTask$31$MessagesController(SparseArray var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didCreatedNewDeleteTask, var1);
   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$4$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         TLRPC.User var8 = this.getUser(UserConfig.getInstance(this.currentAccount).getClientUserId());
         if (var8 == null) {
            var8 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
            this.putUser(var8, true);
         } else {
            UserConfig.getInstance(this.currentAccount).setCurrentUser(var8);
         }

         if (var8 == null) {
            return;
         }

         TLRPC.TL_photos_photo var3 = (TLRPC.TL_photos_photo)var1;
         ArrayList var4 = var3.photo.sizes;
         TLRPC.PhotoSize var6 = FileLoader.getClosestPhotoSizeWithSize(var4, 100);
         TLRPC.PhotoSize var9 = FileLoader.getClosestPhotoSizeWithSize(var4, 1000);
         var8.photo = new TLRPC.TL_userProfilePhoto();
         TLRPC.UserProfilePhoto var5 = var8.photo;
         var5.photo_id = var3.photo.id;
         if (var6 != null) {
            var5.photo_small = var6.location;
         }

         if (var9 != null) {
            var8.photo.photo_big = var9.location;
         } else if (var6 != null) {
            var8.photo.photo_small = var6.location;
         }

         MessagesStorage.getInstance(this.currentAccount).clearUserPhotos(var8.id);
         ArrayList var7 = new ArrayList();
         var7.add(var8);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var7, (ArrayList)null, false, true);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$VkYWRm7oeBHADU_2qscNSGE0Axs(this));
      }

   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$6$MessagesController(TLRPC.TL_wallPaperSettings var1, TLObject var2, TLRPC.TL_error var3) {
      TLRPC.TL_wallPaper var7 = (TLRPC.TL_wallPaper)var2;
      File var4 = ApplicationLoader.getFilesDirFixed();
      String var6;
      if (this.uploadingWallpaperBlurred) {
         var6 = "wallpaper_original.jpg";
      } else {
         var6 = "wallpaper.jpg";
      }

      File var8 = new File(var4, var6);
      if (var7 != null) {
         try {
            AndroidUtilities.copyFile(var8, FileLoader.getPathToAttach(var7.document, true));
         } catch (Exception var5) {
         }
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$RwdHkk3sx1H7HCQZHIZAoujW4QQ(this, var7, var1, var8));
   }

   // $FF: synthetic method
   public void lambda$generateJoinMessage$224$MessagesController(ArrayList var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$_9gXGASI7A4n8RAC9ZRRZOYw_fA(this, var1));
   }

   // $FF: synthetic method
   public void lambda$generateJoinMessage$225$MessagesController(int var1, ArrayList var2) {
      this.updateInterfaceWithMessages((long)(-var1), var2);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$generateUpdateMessage$187$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         SharedConfig.lastUpdateVersion = BuildVars.BUILD_VERSION_STRING;
         SharedConfig.saveConfig();
      }

      if (var1 instanceof TLRPC.Updates) {
         this.processUpdates((TLRPC.Updates)var1, false);
      }

   }

   // $FF: synthetic method
   public void lambda$getBlockedUsers$54$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      SparseIntArray var3 = new SparseIntArray();
      ArrayList var5;
      if (var2 == null) {
         TLRPC.contacts_Blocked var6 = (TLRPC.contacts_Blocked)var1;
         Iterator var4 = var6.blocked.iterator();

         while(var4.hasNext()) {
            var3.put(((TLRPC.TL_contactBlocked)var4.next()).user_id, 1);
         }

         var5 = var6.users;
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var6.users, (ArrayList)null, true, true);
         MessagesStorage.getInstance(this.currentAccount).putBlockedUsers(var3, true);
      } else {
         var5 = null;
      }

      this.processLoadedBlockedUsers(var3, var5, false);
   }

   // $FF: synthetic method
   public void lambda$getChannelDifference$205$MessagesController(int var1, int var2, long var3, TLObject var5, TLRPC.TL_error var6) {
      if (var5 != null) {
         TLRPC.updates_ChannelDifference var7 = (TLRPC.updates_ChannelDifference)var5;
         SparseArray var14 = new SparseArray();
         byte var8 = 0;

         int var9;
         for(var9 = 0; var9 < var7.users.size(); ++var9) {
            TLRPC.User var12 = (TLRPC.User)var7.users.get(var9);
            var14.put(var12.id, var12);
         }

         var9 = 0;

         TLRPC.Chat var13;
         while(true) {
            if (var9 >= var7.chats.size()) {
               var13 = null;
               break;
            }

            var13 = (TLRPC.Chat)var7.chats.get(var9);
            if (var13.id == var1) {
               break;
            }

            ++var9;
         }

         ArrayList var10 = new ArrayList();
         int var15;
         if (!var7.other_updates.isEmpty()) {
            for(var9 = var8; var9 < var7.other_updates.size(); var9 = var15 + 1) {
               TLRPC.Update var11 = (TLRPC.Update)var7.other_updates.get(var9);
               var15 = var9;
               if (var11 instanceof TLRPC.TL_updateMessageID) {
                  var10.add((TLRPC.TL_updateMessageID)var11);
                  var7.other_updates.remove(var9);
                  var15 = var9 - 1;
               }
            }
         }

         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var7.users, var7.chats, true, true);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$28A9t6uw6zcK_zdsc5pJbUtacP4(this, var7));
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$lCXOkGLPvyvcFRmbZ7785J1HE74(this, var10, var1, var7, var13, var14, var2, var3));
      } else if (var6 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$xuR4Dv4llnFxbNDnZ9xFUOdZuww(this, var6, var1));
         this.gettingDifferenceChannels.delete(var1);
         if (var3 != 0L) {
            MessagesStorage.getInstance(this.currentAccount).removePendingTask(var3);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$getDifference$214$MessagesController(int var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      byte var5 = 0;
      if (var4 == null) {
         TLRPC.updates_Difference var10 = (TLRPC.updates_Difference)var3;
         if (var10 instanceof TLRPC.TL_updates_differenceTooLong) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$T4klBi4vmBoUx4jRc3cN4SM0ygE(this, var10, var1, var2));
         } else {
            if (var10 instanceof TLRPC.TL_updates_differenceSlice) {
               TLRPC.TL_updates_state var11 = var10.intermediate_state;
               this.getDifference(var11.pts, var11.date, var11.qts, true);
            }

            SparseArray var6 = new SparseArray();
            SparseArray var12 = new SparseArray();

            for(var1 = 0; var1 < var10.users.size(); ++var1) {
               TLRPC.User var7 = (TLRPC.User)var10.users.get(var1);
               var6.put(var7.id, var7);
            }

            for(var1 = 0; var1 < var10.chats.size(); ++var1) {
               TLRPC.Chat var14 = (TLRPC.Chat)var10.chats.get(var1);
               var12.put(var14.id, var14);
            }

            ArrayList var15 = new ArrayList();
            int var13;
            if (!var10.other_updates.isEmpty()) {
               for(var1 = var5; var1 < var10.other_updates.size(); var1 = var13 + 1) {
                  TLRPC.Update var8 = (TLRPC.Update)var10.other_updates.get(var1);
                  if (var8 instanceof TLRPC.TL_updateMessageID) {
                     var15.add((TLRPC.TL_updateMessageID)var8);
                     var10.other_updates.remove(var1);
                  } else {
                     var13 = var1;
                     if (this.getUpdateType(var8) != 2) {
                        continue;
                     }

                     int var9 = getUpdateChannelId(var8);
                     var13 = this.channelsPts.get(var9);
                     var2 = var13;
                     if (var13 == 0) {
                        var13 = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(var9);
                        var2 = var13;
                        if (var13 != 0) {
                           this.channelsPts.put(var9, var13);
                           var2 = var13;
                        }
                     }

                     var13 = var1;
                     if (var2 == 0) {
                        continue;
                     }

                     var13 = var1;
                     if (getUpdatePts(var8) > var2) {
                        continue;
                     }

                     var10.other_updates.remove(var1);
                  }

                  var13 = var1 - 1;
               }
            }

            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$uHgsA63Vu83OxWhkgJ5VeBJ4ndU(this, var10));
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$ACN99JV2xbYO8r_lQdJPW0MPsd4(this, var10, var15, var6, var12));
         }
      } else {
         this.gettingDifference = false;
         ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(false);
      }

   }

   // $FF: synthetic method
   public void lambda$getNewDeleteTask$32$MessagesController(ArrayList var1, int var2) {
      this.gettingNewDeleteTask = true;
      MessagesStorage.getInstance(this.currentAccount).getNewTask(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$loadChannelAdmins$13$MessagesController(int var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 instanceof TLRPC.TL_channels_channelParticipants) {
         TLRPC.TL_channels_channelParticipants var6 = (TLRPC.TL_channels_channelParticipants)var2;
         ArrayList var5 = new ArrayList(var6.participants.size());

         for(int var4 = 0; var4 < var6.participants.size(); ++var4) {
            var5.add(((TLRPC.ChannelParticipant)var6.participants.get(var4)).user_id);
         }

         this.processLoadedChannelAdmins(var5, var1, false);
      }

   }

   // $FF: synthetic method
   public void lambda$loadChannelParticipants$74$MessagesController(Integer var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$atBLO_w4l_c1A3YUDnxbyiiuEDs(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$loadCurrentState$190$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      int var3 = 0;
      this.updatingState = false;
      if (var2 == null) {
         TLRPC.TL_updates_state var4 = (TLRPC.TL_updates_state)var1;
         MessagesStorage.getInstance(this.currentAccount).setLastDateValue(var4.date);
         MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var4.pts);
         MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(var4.seq);
         MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(var4.qts);

         while(var3 < 3) {
            this.processUpdatesQueue(var3, 2);
            ++var3;
         }

         MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
      } else if (var2.code != 401) {
         this.loadCurrentState();
      }

   }

   // $FF: synthetic method
   public void lambda$loadDialogPhotos$37$MessagesController(int var1, int var2, long var3, int var5, TLObject var6, TLRPC.TL_error var7) {
      if (var7 == null) {
         this.processLoadedUserPhotos((TLRPC.photos_Photos)var6, var1, var2, var3, false, var5);
      }

   }

   // $FF: synthetic method
   public void lambda$loadDialogPhotos$38$MessagesController(int var1, int var2, long var3, int var5, TLObject var6, TLRPC.TL_error var7) {
      if (var7 == null) {
         TLRPC.messages_Messages var11 = (TLRPC.messages_Messages)var6;
         TLRPC.TL_photos_photos var10 = new TLRPC.TL_photos_photos();
         var10.count = var11.count;
         var10.users.addAll(var11.users);

         for(int var8 = 0; var8 < var11.messages.size(); ++var8) {
            TLRPC.MessageAction var9 = ((TLRPC.Message)var11.messages.get(var8)).action;
            if (var9 != null) {
               TLRPC.Photo var12 = var9.photo;
               if (var12 != null) {
                  var10.photos.add(var12);
               }
            }
         }

         this.processLoadedUserPhotos(var10, var1, var2, var3, false, var5);
      }

   }

   // $FF: synthetic method
   public void lambda$loadDialogs$113$MessagesController(int var1, int var2, Runnable var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         TLRPC.messages_Dialogs var6 = (TLRPC.messages_Dialogs)var4;
         this.processLoadedDialogs(var6, (ArrayList)null, var1, 0, var2, 0, false, false, false);
         if (var3 != null && var6.dialogs.isEmpty()) {
            AndroidUtilities.runOnUIThread(var3);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$loadFullChat$17$MessagesController(TLRPC.Chat var1, long var2, int var4, int var5, TLObject var6, TLRPC.TL_error var7) {
      if (var7 == null) {
         TLRPC.TL_messages_chatFull var13 = (TLRPC.TL_messages_chatFull)var6;
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var13.users, var13.chats, true, true);
         MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var13.full_chat, false);
         if (ChatObject.isChannel(var1)) {
            Integer var11 = (Integer)this.dialogs_read_inbox_max.get(var2);
            Integer var8 = var11;
            if (var11 == null) {
               var8 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, var2);
            }

            this.dialogs_read_inbox_max.put(var2, Math.max(var13.full_chat.read_inbox_max_id, var8));
            if (var8 == 0) {
               ArrayList var12 = new ArrayList();
               TLRPC.TL_updateReadChannelInbox var9 = new TLRPC.TL_updateReadChannelInbox();
               var9.channel_id = var4;
               var9.max_id = var13.full_chat.read_inbox_max_id;
               var12.add(var9);
               this.processUpdateArray(var12, (ArrayList)null, (ArrayList)null, false);
            }

            var11 = (Integer)this.dialogs_read_outbox_max.get(var2);
            var8 = var11;
            if (var11 == null) {
               var8 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var2);
            }

            this.dialogs_read_outbox_max.put(var2, Math.max(var13.full_chat.read_outbox_max_id, var8));
            if (var8 == 0) {
               ArrayList var10 = new ArrayList();
               TLRPC.TL_updateReadChannelOutbox var14 = new TLRPC.TL_updateReadChannelOutbox();
               var14.channel_id = var4;
               var14.max_id = var13.full_chat.read_outbox_max_id;
               var10.add(var14);
               this.processUpdateArray(var10, (ArrayList)null, (ArrayList)null, false);
            }
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$yEPLjf91Lbxq_PAfwQRKxAmq0Lc(this, var4, var13, var5));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$mp8VRdAuIDk5ecDgkI3J_hxLXEM(this, var7, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$loadFullUser$20$MessagesController(TLRPC.User var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.UserFull var5 = (TLRPC.UserFull)var3;
         MessagesStorage.getInstance(this.currentAccount).updateUserInfo(var5, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$11fT0i1GEqsG3tRPr2wtXLuwL70(this, var1, var5, var2));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$HIj5jF1rkzKmEGzAia_8fTi8vbE(this, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$loadGlobalNotificationsSettings$115$MessagesController(int var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$rNUlr7DAfpCKy3vrWYUfF6QWZEw(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$loadHintDialogs$110$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$OHE7_yHc7AWZbJtLN81PZkwCp4o(this, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$loadMessagesInternal$102$MessagesController(long var1, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, int var10, int var11, int var12, boolean var13, TLObject var14, TLRPC.TL_error var15) {
      if (var14 != null) {
         TLRPC.TL_messages_peerDialogs var17 = (TLRPC.TL_messages_peerDialogs)var14;
         if (!var17.dialogs.isEmpty()) {
            TLRPC.Dialog var18 = (TLRPC.Dialog)var17.dialogs.get(0);
            if (var18.top_message != 0) {
               TLRPC.TL_messages_dialogs var16 = new TLRPC.TL_messages_dialogs();
               var16.chats = var17.chats;
               var16.users = var17.users;
               var16.dialogs = var17.dialogs;
               var16.messages = var17.messages;
               MessagesStorage.getInstance(this.currentAccount).putDialogs(var16, 0);
            }

            this.loadMessagesInternal(var1, var3, var4, var5, false, var6, var7, var8, var18.top_message, var9, var10, var11, var18.unread_count, var12, var13, var18.unread_mentions_count, false);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$loadMessagesInternal$103$MessagesController(long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, boolean var12, int var13, boolean var14, int var15, TLObject var16, TLRPC.TL_error var17) {
      if (var16 != null) {
         TLRPC.messages_Messages var20 = (TLRPC.messages_Messages)var16;
         this.removeDeletedMessagesFromArray(var1, var20.messages);
         if (var20.messages.size() > var3) {
            var20.messages.remove(0);
         }

         if (var5 != 0 && !var20.messages.isEmpty()) {
            ArrayList var21 = var20.messages;
            int var18 = ((TLRPC.Message)var21.get(var21.size() - 1)).id;
            int var19 = var20.messages.size() - 1;

            while(true) {
               var4 = var18;
               if (var19 < 0) {
                  break;
               }

               TLRPC.Message var22 = (TLRPC.Message)var20.messages.get(var19);
               if (var22.date > var5) {
                  var4 = var22.id;
                  break;
               }

               --var19;
            }
         }

         this.processLoadedMessages(var20, var1, var3, var4, var5, false, var6, var7, var8, var9, var10, var11, var12, false, var13, var14, var15);
      }

   }

   // $FF: synthetic method
   public void lambda$loadPeerSettings$27$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$T_4uY71RM8CIJ1_vMFx7lbM6TkI(this, var1));
   }

   // $FF: synthetic method
   public void lambda$loadPeerSettings$29$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$vHup36gKZnehVbIgi6EYZgekiCc(this, var1, var3));
   }

   // $FF: synthetic method
   public void lambda$loadPinnedDialogs$222$MessagesController(int var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 != null) {
         TLRPC.TL_messages_peerDialogs var4 = (TLRPC.TL_messages_peerDialogs)var2;
         ArrayList var5 = new ArrayList(var4.dialogs);
         this.fetchFolderInLoadedPinnedDialogs(var4);
         TLRPC.TL_messages_dialogs var6 = new TLRPC.TL_messages_dialogs();
         var6.users.addAll(var4.users);
         var6.chats.addAll(var4.chats);
         var6.dialogs.addAll(var4.dialogs);
         var6.messages.addAll(var4.messages);
         LongSparseArray var7 = new LongSparseArray();
         SparseArray var15 = new SparseArray();
         SparseArray var8 = new SparseArray();

         int var9;
         for(var9 = 0; var9 < var4.users.size(); ++var9) {
            TLRPC.User var17 = (TLRPC.User)var4.users.get(var9);
            var15.put(var17.id, var17);
         }

         for(var9 = 0; var9 < var4.chats.size(); ++var9) {
            TLRPC.Chat var19 = (TLRPC.Chat)var4.chats.get(var9);
            var8.put(var19.id, var19);
         }

         int var11;
         for(var9 = 0; var9 < var4.messages.size(); ++var9) {
            TLRPC.Message var21 = (TLRPC.Message)var4.messages.get(var9);
            TLRPC.Peer var10 = var21.to_id;
            var11 = var10.channel_id;
            TLRPC.Chat var24;
            if (var11 != 0) {
               var24 = (TLRPC.Chat)var8.get(var11);
               if (var24 != null && var24.left) {
                  continue;
               }
            } else {
               var11 = var10.chat_id;
               if (var11 != 0) {
                  var24 = (TLRPC.Chat)var8.get(var11);
                  if (var24 != null && var24.migrated_to != null) {
                     continue;
                  }
               }
            }

            MessageObject var22 = new MessageObject(this.currentAccount, var21, var15, var8, false);
            var7.put(var22.getDialogId(), var22);
         }

         boolean var12;
         if (!var5.isEmpty() && var5.get(0) instanceof TLRPC.TL_dialogFolder) {
            var12 = true;
         } else {
            var12 = false;
         }

         var11 = var5.size();

         for(var9 = 0; var9 < var11; ++var9) {
            TLRPC.Dialog var25 = (TLRPC.Dialog)var5.get(var9);
            var25.pinned = true;
            DialogObject.initDialog(var25);
            TLRPC.Chat var16;
            if (DialogObject.isChannel(var25)) {
               var16 = (TLRPC.Chat)var8.get(-((int)var25.id));
               if (var16 != null && var16.left) {
                  continue;
               }
            } else {
               long var13 = var25.id;
               if ((int)var13 < 0) {
                  var16 = (TLRPC.Chat)var8.get(-((int)var13));
                  if (var16 != null && var16.migrated_to != null) {
                     continue;
                  }
               }
            }

            if (var25.last_message_date == 0) {
               MessageObject var18 = (MessageObject)var7.get(var25.id);
               if (var18 != null) {
                  var25.last_message_date = var18.messageOwner.date;
               }
            }

            Integer var23 = (Integer)this.dialogs_read_inbox_max.get(var25.id);
            Integer var20 = var23;
            if (var23 == null) {
               var20 = 0;
            }

            this.dialogs_read_inbox_max.put(var25.id, Math.max(var20, var25.read_inbox_max_id));
            var23 = (Integer)this.dialogs_read_outbox_max.get(var25.id);
            var20 = var23;
            if (var23 == null) {
               var20 = 0;
            }

            this.dialogs_read_outbox_max.put(var25.id, Math.max(var20, var25.read_outbox_max_id));
         }

         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$2xwawfxy77A7uYEfX2S3k_suXFE(this, var1, var5, var12, var4, var7, var6));
      }

   }

   // $FF: synthetic method
   public void lambda$loadSignUpNotificationsSettings$117$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$mxqgqoLhYjlwTctiq5kQ_FZ7lvE(this, var1));
   }

   // $FF: synthetic method
   public void lambda$loadUnknownChannel$195$MessagesController(long var1, TLRPC.Chat var3, TLObject var4, TLRPC.TL_error var5) {
      if (var4 != null) {
         TLRPC.TL_messages_peerDialogs var7 = (TLRPC.TL_messages_peerDialogs)var4;
         if (!var7.dialogs.isEmpty() && !var7.chats.isEmpty()) {
            TLRPC.TL_dialog var6 = (TLRPC.TL_dialog)var7.dialogs.get(0);
            TLRPC.TL_messages_dialogs var8 = new TLRPC.TL_messages_dialogs();
            var8.dialogs.addAll(var7.dialogs);
            var8.messages.addAll(var7.messages);
            var8.users.addAll(var7.users);
            var8.chats.addAll(var7.chats);
            this.processLoadedDialogs(var8, (ArrayList)null, var6.folder_id, 0, 1, this.DIALOGS_LOAD_TYPE_CHANNEL, false, false, false);
         }
      }

      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

      this.gettingUnknownChannels.delete(var3.id);
   }

   // $FF: synthetic method
   public void lambda$loadUnknownDialog$118$MessagesController(long var1, long var3, TLObject var5, TLRPC.TL_error var6) {
      if (var5 != null) {
         TLRPC.TL_messages_peerDialogs var7 = (TLRPC.TL_messages_peerDialogs)var5;
         if (!var7.dialogs.isEmpty()) {
            TLRPC.TL_dialog var9 = (TLRPC.TL_dialog)var7.dialogs.get(0);
            TLRPC.TL_messages_dialogs var8 = new TLRPC.TL_messages_dialogs();
            var8.dialogs.addAll(var7.dialogs);
            var8.messages.addAll(var7.messages);
            var8.users.addAll(var7.users);
            var8.chats.addAll(var7.chats);
            this.processLoadedDialogs(var8, (ArrayList)null, var9.folder_id, 0, 1, this.DIALOGS_LOAD_TYPE_UNKNOWN, false, false, false);
         }
      }

      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

      this.gettingUnknownDialogs.delete(var3);
   }

   // $FF: synthetic method
   public void lambda$loadUnreadDialogs$217$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$DRgtzkEskOqTf7dqRkfnjux7IBI(this, var1));
   }

   // $FF: synthetic method
   public void lambda$markDialogAsRead$151$MessagesController(long var1, int var3, int var4, boolean var5, boolean var6) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$QJU8iYKsFf9hbJ_ao3ktQ9SZw_c(this, var1, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$markDialogAsRead$153$MessagesController(long var1, int var3, boolean var4, int var5, int var6, boolean var7) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$3aeD82p4JOknKdKlPd_JqRJlTgE(this, var1, var3, var4, var5, var6, var7));
   }

   // $FF: synthetic method
   public void lambda$markDialogAsRead$154$MessagesController(long var1, boolean var3, int var4, int var5) {
      MessagesController.ReadTask var6 = (MessagesController.ReadTask)this.readTasksMap.get(var1);
      MessagesController.ReadTask var7 = var6;
      if (var6 == null) {
         var6 = new MessagesController.ReadTask();
         var6.dialogId = var1;
         var6.sendRequestTime = SystemClock.elapsedRealtime() + 5000L;
         var7 = var6;
         if (!var3) {
            this.readTasksMap.put(var1, var6);
            this.readTasks.add(var6);
            var7 = var6;
         }
      }

      var7.maxDate = var4;
      var7.maxId = var5;
      if (var3) {
         this.completeReadTask(var7);
      }

   }

   // $FF: synthetic method
   public void lambda$markDialogAsReadNow$148$MessagesController(long var1) {
      MessagesController.ReadTask var3 = (MessagesController.ReadTask)this.readTasksMap.get(var1);
      if (var3 != null) {
         this.completeReadTask(var3);
         this.readTasks.remove(var3);
         this.readTasksMap.remove(var1);
      }
   }

   // $FF: synthetic method
   public void lambda$markDialogAsUnread$215$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$markMentionMessageAsRead$143$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         TLRPC.TL_messages_affectedMessages var3 = (TLRPC.TL_messages_affectedMessages)var1;
         this.processNewDifferenceParams(-1, var3.pts, -1, var3.pts_count);
      }

   }

   // $FF: synthetic method
   public void lambda$markMessageAsRead$144$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$markMessageAsRead$145$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.TL_messages_affectedMessages var5 = (TLRPC.TL_messages_affectedMessages)var3;
         this.processNewDifferenceParams(-1, var5.pts, -1, var5.pts_count);
      }

      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$markMessageContentAsRead$141$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         TLRPC.TL_messages_affectedMessages var3 = (TLRPC.TL_messages_affectedMessages)var1;
         this.processNewDifferenceParams(-1, var3.pts, -1, var3.pts_count);
      }

   }

   // $FF: synthetic method
   public void lambda$migrateDialogs$126$MessagesController(int var1, TLObject var2, TLRPC.TL_error var3) {
      if (var3 == null) {
         TLRPC.messages_Dialogs var4 = (TLRPC.messages_Dialogs)var2;
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$N9vCK0oGVwwS8iSsLW2lhMHnKHI(this, var4, var1));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$PNWG6_UzeZ94xJHGN3qJ4RNxOm0(this));
      }

   }

   // $FF: synthetic method
   public int lambda$new$0$MessagesController(TLRPC.Update var1, TLRPC.Update var2) {
      int var3 = this.getUpdateType(var1);
      int var4 = this.getUpdateType(var2);
      if (var3 != var4) {
         return AndroidUtilities.compare(var3, var4);
      } else if (var3 == 0) {
         return AndroidUtilities.compare(getUpdatePts(var1), getUpdatePts(var2));
      } else if (var3 == 1) {
         return AndroidUtilities.compare(getUpdateQts(var1), getUpdateQts(var2));
      } else if (var3 == 2) {
         var3 = getUpdateChannelId(var1);
         var4 = getUpdateChannelId(var2);
         return var3 == var4 ? AndroidUtilities.compare(getUpdatePts(var1), getUpdatePts(var2)) : AndroidUtilities.compare(var3, var4);
      } else {
         return 0;
      }
   }

   // $FF: synthetic method
   public void lambda$new$1$MessagesController() {
      MessagesController var1 = getInstance(this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.FileDidUpload);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.FileDidFailUpload);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.fileDidFailedLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.messageReceivedByServer);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.updateMessageMedia);
   }

   // $FF: synthetic method
   public void lambda$null$100$MessagesController(int var1, long var2) {
      LongSparseArray var4 = (LongSparseArray)this.sendingTypings.get(var1);
      if (var4 != null) {
         var4.remove(var2);
      }

   }

   // $FF: synthetic method
   public void lambda$null$104$MessagesController(String var1, TLObject var2, long var3) {
      ArrayList var5 = (ArrayList)this.reloadingWebpages.remove(var1);
      if (var5 != null) {
         TLRPC.TL_messages_messages var8 = new TLRPC.TL_messages_messages();
         int var6;
         if (!(var2 instanceof TLRPC.TL_messageMediaWebPage)) {
            for(var6 = 0; var6 < var5.size(); ++var6) {
               ((MessageObject)var5.get(var6)).messageOwner.media.webpage = new TLRPC.TL_webPageEmpty();
               var8.messages.add(((MessageObject)var5.get(var6)).messageOwner);
            }
         } else {
            TLRPC.TL_messageMediaWebPage var9 = (TLRPC.TL_messageMediaWebPage)var2;
            TLRPC.WebPage var7 = var9.webpage;
            if (!(var7 instanceof TLRPC.TL_webPage) && !(var7 instanceof TLRPC.TL_webPageEmpty)) {
               this.reloadingWebpagesPending.put(var7.id, var5);
            } else {
               for(var6 = 0; var6 < var5.size(); ++var6) {
                  ((MessageObject)var5.get(var6)).messageOwner.media.webpage = var9.webpage;
                  if (var6 == 0) {
                     ImageLoader.saveMessageThumbs(((MessageObject)var5.get(var6)).messageOwner);
                  }

                  var8.messages.add(((MessageObject)var5.get(var6)).messageOwner);
               }
            }
         }

         if (!var8.messages.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).putMessages(var8, var3, -2, 0, false);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, var3, var5);
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$106$MessagesController(long var1, int var3, int var4, boolean var5, int var6, int var7, int var8, int var9, int var10, boolean var11, int var12, int var13, int var14, int var15) {
      if (var4 == 2 && var5) {
         var7 = var6;
      }

      this.loadMessages(var1, var3, var7, var8, false, 0, var9, var4, var10, var11, var12, var6, var13, var14, var5, var15);
   }

   // $FF: synthetic method
   public void lambda$null$107$MessagesController(TLRPC.messages_Messages var1, boolean var2, boolean var3, int var4, int var5, long var6, int var8, ArrayList var9, int var10, int var11, int var12, boolean var13, int var14, int var15, int var16, int var17, ArrayList var18, HashMap var19) {
      this.putUsers(var1.users, var2);
      this.putChats(var1.chats, var2);
      int var21;
      if (var3 && var4 == 2) {
         int var20 = 0;

         int var24;
         for(var21 = Integer.MAX_VALUE; var20 < var1.messages.size(); var21 = var24) {
            TLRPC.Message var22 = (TLRPC.Message)var1.messages.get(var20);
            if (!var22.out) {
               int var23 = var22.id;
               var24 = var21;
               if (var23 > var5) {
                  var24 = var21;
                  if (var23 < var21) {
                     var24 = var23;
                  }
               }
            } else {
               var24 = var21;
            }

            ++var20;
         }
      } else {
         var21 = Integer.MAX_VALUE;
      }

      if (var21 != Integer.MAX_VALUE) {
         var5 = var21;
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesDidLoad, var6, var8, var9, var2, var5, var10, var11, var12, var4, var13, var14, var15, var16, var17);
      if (!var18.isEmpty()) {
         this.reloadMessages(var18, var6);
      }

      if (!var19.isEmpty()) {
         this.reloadWebPages(var6, var19);
      }

   }

   // $FF: synthetic method
   public void lambda$null$109$MessagesController(TLObject var1) {
      TLRPC.TL_help_recentMeUrls var2 = (TLRPC.TL_help_recentMeUrls)var1;
      this.putUsers(var2.users, false);
      this.putChats(var2.chats, false);
      this.hintDialogs.clear();
      this.hintDialogs.addAll(var2.urls);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$null$114$MessagesController(TLObject var1, int var2) {
      if (var1 != null) {
         --this.loadingNotificationSettings;
         TLRPC.TL_peerNotifySettings var4 = (TLRPC.TL_peerNotifySettings)var1;
         Editor var3 = this.notificationsPreferences.edit();
         if (var2 == 0) {
            if ((var4.flags & 1) != 0) {
               var3.putBoolean("EnablePreviewGroup", var4.show_previews);
            }

            if ((var4.flags & 4) != 0) {
               var3.putInt("EnableGroup2", var4.mute_until);
            }
         } else if (var2 == 1) {
            if ((var4.flags & 1) != 0) {
               var3.putBoolean("EnablePreviewAll", var4.show_previews);
            }

            if ((var4.flags & 4) != 0) {
               var3.putInt("EnableAll2", var4.mute_until);
            }
         } else if (var2 == 2) {
            if ((var4.flags & 1) != 0) {
               var3.putBoolean("EnablePreviewChannel", var4.show_previews);
            }

            if ((var4.flags & 4) != 0) {
               var3.putInt("EnableChannel2", var4.mute_until);
            }
         }

         var3.commit();
         if (this.loadingNotificationSettings == 0) {
            UserConfig.getInstance(this.currentAccount).notificationsSettingsLoaded = true;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$116$MessagesController(TLObject var1) {
      this.loadingNotificationSignUpSettings = false;
      Editor var2 = this.notificationsPreferences.edit();
      this.enableJoined = var1 instanceof TLRPC.TL_boolFalse;
      var2.putBoolean("EnableContactJoined", this.enableJoined);
      var2.commit();
      UserConfig.getInstance(this.currentAccount).notificationsSignUpSettingsLoaded = true;
      UserConfig.getInstance(this.currentAccount).saveConfig(false);
   }

   // $FF: synthetic method
   public void lambda$null$121$MessagesController(TLRPC.messages_Dialogs var1, LongSparseArray var2, LongSparseArray var3) {
      this.resetingDialogs = false;
      this.applyDialogsNotificationsSettings(var1.dialogs);
      if (!UserConfig.getInstance(this.currentAccount).draftsLoaded) {
         DataQuery.getInstance(this.currentAccount).loadDrafts();
      }

      this.putUsers(var1.users, false);
      this.putChats(var1.chats, false);

      int var4;
      long var6;
      MessageObject var9;
      for(var4 = 0; var4 < this.allDialogs.size(); ++var4) {
         TLRPC.Dialog var5 = (TLRPC.Dialog)this.allDialogs.get(var4);
         if (!DialogObject.isSecretDialogId(var5.id)) {
            this.dialogs_dict.remove(var5.id);
            var9 = (MessageObject)this.dialogMessage.get(var5.id);
            this.dialogMessage.remove(var5.id);
            if (var9 != null) {
               this.dialogMessagesByIds.remove(var9.getId());
               var6 = var9.messageOwner.random_id;
               if (var6 != 0L) {
                  this.dialogMessagesByRandomIds.remove(var6);
               }
            }
         }
      }

      for(var4 = 0; var4 < var2.size(); ++var4) {
         var6 = var2.keyAt(var4);
         TLRPC.Dialog var10 = (TLRPC.Dialog)var2.valueAt(var4);
         if (var10.draft instanceof TLRPC.TL_draftMessage) {
            DataQuery.getInstance(this.currentAccount).saveDraft(var10.id, var10.draft, (TLRPC.Message)null, false);
         }

         this.dialogs_dict.put(var6, var10);
         var9 = (MessageObject)var3.get(var10.id);
         this.dialogMessage.put(var6, var9);
         if (var9 != null && var9.messageOwner.to_id.channel_id == 0) {
            this.dialogMessagesByIds.put(var9.getId(), var9);
            var6 = var9.messageOwner.random_id;
            if (var6 != 0L) {
               this.dialogMessagesByRandomIds.put(var6, var9);
            }
         }
      }

      this.allDialogs.clear();
      int var8 = this.dialogs_dict.size();

      for(var4 = 0; var4 < var8; ++var4) {
         this.allDialogs.add(this.dialogs_dict.valueAt(var4));
      }

      this.sortDialogs((SparseArray)null);
      this.dialogsEndReached.put(0, true);
      this.serverDialogsEndReached.put(0, false);
      this.dialogsEndReached.put(1, true);
      this.serverDialogsEndReached.put(1, false);
      var4 = UserConfig.getInstance(this.currentAccount).getTotalDialogsCount(0);
      int[] var11 = UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(0);
      if (var4 < 400 && var11[0] != -1 && var11[0] != Integer.MAX_VALUE) {
         this.loadDialogs(0, 100, 0, false);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$null$123$MessagesController() {
      this.migratingDialogs = false;
   }

   // $FF: synthetic method
   public void lambda$null$124$MessagesController(TLRPC.messages_Dialogs var1, int var2) {
      Exception var10000;
      label459: {
         int var3;
         boolean var10001;
         try {
            var3 = UserConfig.getInstance(this.currentAccount).getTotalDialogsCount(0);
            UserConfig.getInstance(this.currentAccount).setTotalDialogsCount(0, var3 + var1.dialogs.size());
         } catch (Exception var53) {
            var10000 = var53;
            var10001 = false;
            break label459;
         }

         Object var4 = null;
         var3 = 0;

         label429:
         while(true) {
            int var5;
            try {
               var5 = var1.messages.size();
            } catch (Exception var50) {
               var10000 = var50;
               var10001 = false;
               break;
            }

            StringBuilder var7;
            long var10002;
            if (var3 >= var5) {
               try {
                  if (BuildVars.LOGS_ENABLED) {
                     var7 = new StringBuilder();
                     var7.append("migrate step with id ");
                     var7.append(((TLRPC.Message)var4).id);
                     var7.append(" date ");
                     var10002 = (long)((TLRPC.Message)var4).date;
                     var7.append(LocaleController.getInstance().formatterStats.format(var10002 * 1000L));
                     FileLog.d(var7.toString());
                  }
               } catch (Exception var49) {
                  var10000 = var49;
                  var10001 = false;
                  break;
               }

               label439: {
                  try {
                     if (var1.dialogs.size() >= 100) {
                        var3 = ((TLRPC.Message)var4).id;
                        break label439;
                     }
                  } catch (Exception var48) {
                     var10000 = var48;
                     var10001 = false;
                     break;
                  }

                  try {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("migrate stop due to not 100 dialogs");
                     }
                  } catch (Exception var47) {
                     var10000 = var47;
                     var10001 = false;
                     break;
                  }

                  for(var3 = 0; var3 < 2; ++var3) {
                     try {
                        UserConfig.getInstance(this.currentAccount).setDialogsLoadOffset(var3, Integer.MAX_VALUE, UserConfig.getInstance(this.currentAccount).migrateOffsetDate, UserConfig.getInstance(this.currentAccount).migrateOffsetUserId, UserConfig.getInstance(this.currentAccount).migrateOffsetChatId, UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId, UserConfig.getInstance(this.currentAccount).migrateOffsetAccess);
                     } catch (Exception var46) {
                        var10000 = var46;
                        var10001 = false;
                        break label429;
                     }
                  }

                  var3 = -1;
               }

               LongSparseArray var8;
               StringBuilder var55;
               try {
                  var55 = new StringBuilder(var1.dialogs.size() * 12);
                  var8 = new LongSparseArray();
               } catch (Exception var45) {
                  var10000 = var45;
                  var10001 = false;
                  break;
               }

               var5 = 0;

               while(true) {
                  TLRPC.Dialog var62;
                  try {
                     if (var5 >= var1.dialogs.size()) {
                        break;
                     }

                     var62 = (TLRPC.Dialog)var1.dialogs.get(var5);
                     DialogObject.initDialog(var62);
                     if (var55.length() > 0) {
                        var55.append(",");
                     }
                  } catch (Exception var44) {
                     var10000 = var44;
                     var10001 = false;
                     break label429;
                  }

                  try {
                     var55.append(var62.id);
                     var8.put(var62.id, var62);
                  } catch (Exception var43) {
                     var10000 = var43;
                     var10001 = false;
                     break label429;
                  }

                  ++var5;
               }

               SQLiteCursor var63;
               try {
                  var63 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT did, folder_id FROM dialogs WHERE did IN (%s)", var55.toString()));
               } catch (Exception var39) {
                  var10000 = var39;
                  var10001 = false;
                  break;
               }

               long var9;
               TLRPC.Message var11;
               label371:
               while(true) {
                  while(true) {
                     TLRPC.Dialog var56;
                     try {
                        if (!var63.next()) {
                           break label371;
                        }

                        var9 = var63.longValue(0);
                        var5 = var63.intValue(1);
                        var56 = (TLRPC.Dialog)var8.get(var9);
                        if (var56.folder_id != var5) {
                           continue;
                        }
                     } catch (Exception var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label429;
                     }

                     try {
                        var8.remove(var9);
                     } catch (Exception var38) {
                        var10000 = var38;
                        var10001 = false;
                        break label429;
                     }

                     if (var56 != null) {
                        try {
                           var1.dialogs.remove(var56);
                        } catch (Exception var37) {
                           var10000 = var37;
                           var10001 = false;
                           break label429;
                        }

                        var5 = 0;

                        while(true) {
                           label444: {
                              try {
                                 if (var5 >= var1.messages.size()) {
                                    break;
                                 }

                                 var11 = (TLRPC.Message)var1.messages.get(var5);
                                 if (MessageObject.getDialogId(var11) != var9) {
                                    break label444;
                                 }
                              } catch (Exception var41) {
                                 var10000 = var41;
                                 var10001 = false;
                                 break label429;
                              }

                              try {
                                 var1.messages.remove(var5);
                              } catch (Exception var36) {
                                 var10000 = var36;
                                 var10001 = false;
                                 break label429;
                              }

                              --var5;

                              try {
                                 if (var11.id == var56.top_message) {
                                    var56.top_message = 0;
                                    break;
                                 }
                              } catch (Exception var40) {
                                 var10000 = var40;
                                 var10001 = false;
                                 break label429;
                              }
                           }

                           ++var5;
                        }
                     }
                  }
               }

               try {
                  var63.dispose();
                  if (BuildVars.LOGS_ENABLED) {
                     var7 = new StringBuilder();
                     var7.append("migrate found missing dialogs ");
                     var7.append(var1.dialogs.size());
                     FileLog.d(var7.toString());
                  }
               } catch (Exception var35) {
                  var10000 = var35;
                  var10001 = false;
                  break;
               }

               label445: {
                  int var12;
                  label332: {
                     try {
                        var63 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT min(date) FROM dialogs WHERE date != 0 AND did >> 32 IN (0, -1)");
                        if (var63.next()) {
                           var12 = Math.max(1441062000, var63.intValue(0));
                           break label332;
                        }
                     } catch (Exception var34) {
                        var10000 = var34;
                        var10001 = false;
                        break;
                     }

                     var2 = var3;
                     break label445;
                  }

                  var5 = 0;

                  while(true) {
                     int var13;
                     try {
                        var13 = var1.messages.size();
                     } catch (Exception var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label429;
                     }

                     Object var60;
                     if (var5 >= var13) {
                        if (var4 != null) {
                           label450: {
                              var60 = var4;

                              try {
                                 if (((TLRPC.Message)var60).date >= var12) {
                                    break label450;
                                 }
                              } catch (Exception var31) {
                                 var10000 = var31;
                                 var10001 = false;
                                 break label429;
                              }

                              if (var2 != -1) {
                                 for(var2 = 0; var2 < 2; ++var2) {
                                    try {
                                       UserConfig.getInstance(this.currentAccount).setDialogsLoadOffset(var2, UserConfig.getInstance(this.currentAccount).migrateOffsetId, UserConfig.getInstance(this.currentAccount).migrateOffsetDate, UserConfig.getInstance(this.currentAccount).migrateOffsetUserId, UserConfig.getInstance(this.currentAccount).migrateOffsetChatId, UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId, UserConfig.getInstance(this.currentAccount).migrateOffsetAccess);
                                    } catch (Exception var24) {
                                       var10000 = var24;
                                       var10001 = false;
                                       break label429;
                                    }
                                 }

                                 try {
                                    if (BuildVars.LOGS_ENABLED) {
                                       var4 = new StringBuilder();
                                       ((StringBuilder)var4).append("migrate stop due to reached loaded dialogs ");
                                       ((StringBuilder)var4).append(LocaleController.getInstance().formatterStats.format((long)var12 * 1000L));
                                       FileLog.d(((StringBuilder)var4).toString());
                                    }
                                 } catch (Exception var30) {
                                    var10000 = var30;
                                    var10001 = false;
                                    break label429;
                                 }

                                 var2 = -1;
                                 var4 = var4;
                                 break;
                              }
                           }
                        }

                        var2 = var3;
                        break;
                     }

                     SQLiteCursor var64;
                     label320: {
                        label447: {
                           try {
                              var11 = (TLRPC.Message)var1.messages.get(var5);
                              if (var11.date >= var12) {
                                 break label447;
                              }
                           } catch (Exception var33) {
                              var10000 = var33;
                              var10001 = false;
                              break label429;
                           }

                           if (var2 != -1) {
                              for(var3 = 0; var3 < 2; ++var3) {
                                 try {
                                    UserConfig.getInstance(this.currentAccount).setDialogsLoadOffset(var3, UserConfig.getInstance(this.currentAccount).migrateOffsetId, UserConfig.getInstance(this.currentAccount).migrateOffsetDate, UserConfig.getInstance(this.currentAccount).migrateOffsetUserId, UserConfig.getInstance(this.currentAccount).migrateOffsetChatId, UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId, UserConfig.getInstance(this.currentAccount).migrateOffsetAccess);
                                 } catch (Exception var29) {
                                    var10000 = var29;
                                    var10001 = false;
                                    break label429;
                                 }
                              }

                              try {
                                 if (BuildVars.LOGS_ENABLED) {
                                    var55 = new StringBuilder();
                                    var55.append("migrate stop due to reached loaded dialogs ");
                                    var55.append(LocaleController.getInstance().formatterStats.format((long)var12 * 1000L));
                                    FileLog.d(var55.toString());
                                 }
                              } catch (Exception var32) {
                                 var10000 = var32;
                                 var10001 = false;
                                 break label429;
                              }

                              var3 = -1;
                              var60 = var4;
                           } else {
                              var60 = var4;
                           }

                           try {
                              var1.messages.remove(var5);
                           } catch (Exception var28) {
                              var10000 = var28;
                              var10001 = false;
                              break label429;
                           }

                           int var14 = var5 - 1;

                           TLRPC.Dialog var15;
                           try {
                              var9 = MessageObject.getDialogId(var11);
                              var15 = (TLRPC.Dialog)var8.get(var9);
                              var8.remove(var9);
                           } catch (Exception var27) {
                              var10000 = var27;
                              var10001 = false;
                              break label429;
                           }

                           var5 = var14;
                           var13 = var3;
                           var64 = var63;
                           var4 = var60;
                           if (var15 != null) {
                              try {
                                 var1.dialogs.remove(var15);
                              } catch (Exception var26) {
                                 var10000 = var26;
                                 var10001 = false;
                                 break label429;
                              }

                              var5 = var14;
                              var13 = var3;
                              var64 = var63;
                              var4 = var60;
                           }
                           break label320;
                        }

                        var64 = var63;
                        var13 = var3;
                     }

                     ++var5;
                     var63 = var64;
                     var3 = var13;
                  }
               }

               label453: {
                  TLRPC.Chat var57;
                  label454: {
                     try {
                        var63.dispose();
                        UserConfig.getInstance(this.currentAccount).migrateOffsetDate = ((TLRPC.Message)var4).date;
                        if (((TLRPC.Message)var4).to_id.channel_id == 0) {
                           break label454;
                        }

                        UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId = ((TLRPC.Message)var4).to_id.channel_id;
                        UserConfig.getInstance(this.currentAccount).migrateOffsetChatId = 0;
                        UserConfig.getInstance(this.currentAccount).migrateOffsetUserId = 0;
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break;
                     }

                     var3 = 0;

                     while(true) {
                        try {
                           if (var3 >= var1.chats.size()) {
                              break label453;
                           }

                           var57 = (TLRPC.Chat)var1.chats.get(var3);
                           if (var57.id == UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId) {
                              UserConfig.getInstance(this.currentAccount).migrateOffsetAccess = var57.access_hash;
                              break label453;
                           }
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break label429;
                        }

                        ++var3;
                     }
                  }

                  label456: {
                     try {
                        if (((TLRPC.Message)var4).to_id.chat_id == 0) {
                           break label456;
                        }

                        UserConfig.getInstance(this.currentAccount).migrateOffsetChatId = ((TLRPC.Message)var4).to_id.chat_id;
                        UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId = 0;
                        UserConfig.getInstance(this.currentAccount).migrateOffsetUserId = 0;
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break;
                     }

                     var3 = 0;

                     while(true) {
                        try {
                           if (var3 >= var1.chats.size()) {
                              break label453;
                           }

                           var57 = (TLRPC.Chat)var1.chats.get(var3);
                           if (var57.id == UserConfig.getInstance(this.currentAccount).migrateOffsetChatId) {
                              UserConfig.getInstance(this.currentAccount).migrateOffsetAccess = var57.access_hash;
                              break label453;
                           }
                        } catch (Exception var20) {
                           var10000 = var20;
                           var10001 = false;
                           break label429;
                        }

                        ++var3;
                     }
                  }

                  UserConfig var59;
                  try {
                     if (((TLRPC.Message)var4).to_id.user_id == 0) {
                        break label453;
                     }

                     UserConfig.getInstance(this.currentAccount).migrateOffsetUserId = ((TLRPC.Message)var4).to_id.user_id;
                     var59 = UserConfig.getInstance(this.currentAccount);
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break;
                  }

                  var3 = 0;

                  try {
                     var59.migrateOffsetChatId = 0;
                     UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId = 0;
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                     break;
                  }

                  while(true) {
                     try {
                        if (var3 >= var1.users.size()) {
                           break;
                        }

                        TLRPC.User var61 = (TLRPC.User)var1.users.get(var3);
                        if (var61.id == UserConfig.getInstance(this.currentAccount).migrateOffsetUserId) {
                           UserConfig.getInstance(this.currentAccount).migrateOffsetAccess = var61.access_hash;
                           break;
                        }
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label429;
                     }

                     ++var3;
                  }
               }

               try {
                  this.processLoadedDialogs(var1, (ArrayList)null, 0, var2, 0, 0, false, true, false);
                  return;
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break;
               }
            }

            TLRPC.Message var6;
            try {
               var6 = (TLRPC.Message)var1.messages.get(var3);
               if (BuildVars.LOGS_ENABLED) {
                  var7 = new StringBuilder();
                  var7.append("search migrate id ");
                  var7.append(var6.id);
                  var7.append(" date ");
                  var10002 = (long)var6.date;
                  var7.append(LocaleController.getInstance().formatterStats.format(var10002 * 1000L));
                  FileLog.d(var7.toString());
               }
            } catch (Exception var51) {
               var10000 = var51;
               var10001 = false;
               break;
            }

            Object var58;
            label426: {
               if (var4 != null) {
                  var58 = var4;

                  try {
                     if (var6.date >= ((TLRPC.Message)var4).date) {
                        break label426;
                     }
                  } catch (Exception var52) {
                     var10000 = var52;
                     var10001 = false;
                     break;
                  }
               }

               var58 = var6;
            }

            ++var3;
            var4 = var58;
         }
      }

      Exception var54 = var10000;
      FileLog.e((Throwable)var54);
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$n72oPYeD0dDCBaP1ov8Q_eP6egE(this));
   }

   // $FF: synthetic method
   public void lambda$null$125$MessagesController() {
      this.migratingDialogs = false;
   }

   // $FF: synthetic method
   public void lambda$null$127$MessagesController(TLRPC.messages_Dialogs var1, int var2, boolean var3, int[] var4, int var5) {
      this.putUsers(var1.users, true);
      this.loadingDialogs.put(var2, false);
      if (var3) {
         this.dialogsEndReached.put(var2, false);
         this.serverDialogsEndReached.put(var2, false);
      } else if (var4[0] == Integer.MAX_VALUE) {
         this.dialogsEndReached.put(var2, true);
         this.serverDialogsEndReached.put(var2, true);
      } else {
         this.loadDialogs(var2, 0, var5, false);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$null$128$MessagesController(int var1, TLRPC.messages_Dialogs var2, ArrayList var3, boolean var4, int var5, LongSparseArray var6, LongSparseArray var7, SparseArray var8, int var9, boolean var10, int var11, ArrayList var12) {
      if (var1 != this.DIALOGS_LOAD_TYPE_CACHE) {
         this.applyDialogsNotificationsSettings(var2.dialogs);
         if (!UserConfig.getInstance(this.currentAccount).draftsLoaded) {
            DataQuery.getInstance(this.currentAccount).loadDrafts();
         }
      }

      ArrayList var13 = var2.users;
      boolean var14;
      if (var1 == this.DIALOGS_LOAD_TYPE_CACHE) {
         var14 = true;
      } else {
         var14 = false;
      }

      this.putUsers(var13, var14);
      var13 = var2.chats;
      if (var1 == this.DIALOGS_LOAD_TYPE_CACHE) {
         var14 = true;
      } else {
         var14 = false;
      }

      this.putChats(var13, var14);
      int var15;
      if (var3 != null) {
         for(var15 = 0; var15 < var3.size(); ++var15) {
            TLRPC.EncryptedChat var27 = (TLRPC.EncryptedChat)var3.get(var15);
            if (var27 instanceof TLRPC.TL_encryptedChat && AndroidUtilities.getMyLayerVersion(var27.layer) < 73) {
               SecretChatHelper.getInstance(this.currentAccount).sendNotifyLayerMessage(var27, (TLRPC.Message)null);
            }

            this.putEncryptedChat(var27, true);
         }
      }

      if (!var4 && var1 != this.DIALOGS_LOAD_TYPE_UNKNOWN && var1 != this.DIALOGS_LOAD_TYPE_CHANNEL) {
         this.loadingDialogs.put(var5, false);
      }

      this.dialogsLoaded = true;
      if (var4 && !this.allDialogs.isEmpty()) {
         var3 = this.allDialogs;
         var15 = ((TLRPC.Dialog)var3.get(var3.size() - 1)).last_message_date;
      } else {
         var15 = 0;
      }

      int var16 = 0;
      int var17 = 0;
      boolean var18 = false;

      for(int var19 = var15; var16 < var6.size(); var17 = var15) {
         long var20 = var6.keyAt(var16);
         TLRPC.Dialog var28 = (TLRPC.Dialog)var6.valueAt(var16);
         TLRPC.Dialog var24;
         if (var1 != this.DIALOGS_LOAD_TYPE_UNKNOWN) {
            var24 = (TLRPC.Dialog)this.dialogs_dict.get(var20);
         } else {
            var24 = null;
         }

         if (var4 && var24 != null) {
            var24.folder_id = var28.folder_id;
         }

         if (var4 && var19 != 0 && var28.last_message_date < var19) {
            var15 = var17;
         } else {
            if (var1 != this.DIALOGS_LOAD_TYPE_CACHE && var28.draft instanceof TLRPC.TL_draftMessage) {
               DataQuery.getInstance(this.currentAccount).saveDraft(var28.id, var28.draft, (TLRPC.Message)null, false);
            }

            var15 = var17;
            if (var28.folder_id != var5) {
               var15 = var17 + 1;
            }

            MessageObject var25;
            if (var24 == null) {
               this.dialogs_dict.put(var20, var28);
               var25 = (MessageObject)var7.get(var28.id);
               this.dialogMessage.put(var20, var25);
               if (var25 != null && var25.messageOwner.to_id.channel_id == 0) {
                  this.dialogMessagesByIds.put(var25.getId(), var25);
                  var20 = var25.messageOwner.random_id;
                  if (var20 != 0L) {
                     this.dialogMessagesByRandomIds.put(var20, var25);
                  }
               }

               var18 = true;
            } else {
               if (var1 != this.DIALOGS_LOAD_TYPE_CACHE) {
                  var24.notify_settings = var28.notify_settings;
               }

               var24.pinned = var28.pinned;
               var24.pinnedNum = var28.pinnedNum;
               MessageObject var22 = (MessageObject)this.dialogMessage.get(var20);
               if ((var22 == null || !var22.deleted) && var22 != null && var24.top_message <= 0) {
                  var25 = (MessageObject)var7.get(var28.id);
                  if (var22.deleted || var25 == null || var25.messageOwner.date > var22.messageOwner.date) {
                     this.dialogs_dict.put(var20, var28);
                     this.dialogMessage.put(var20, var25);
                     if (var25 != null && var25.messageOwner.to_id.channel_id == 0) {
                        this.dialogMessagesByIds.put(var25.getId(), var25);
                        if (var25 != null) {
                           var20 = var25.messageOwner.random_id;
                           if (var20 != 0L) {
                              this.dialogMessagesByRandomIds.put(var20, var25);
                           }
                        }
                     }

                     this.dialogMessagesByIds.remove(var22.getId());
                     var20 = var22.messageOwner.random_id;
                     if (var20 != 0L) {
                        this.dialogMessagesByRandomIds.remove(var20);
                     }
                  }
               } else if (var28.top_message >= var24.top_message) {
                  this.dialogs_dict.put(var20, var28);
                  var25 = (MessageObject)var7.get(var28.id);
                  this.dialogMessage.put(var20, var25);
                  if (var25 != null && var25.messageOwner.to_id.channel_id == 0) {
                     this.dialogMessagesByIds.put(var25.getId(), var25);
                     if (var25 != null) {
                        var20 = var25.messageOwner.random_id;
                        if (var20 != 0L) {
                           this.dialogMessagesByRandomIds.put(var20, var25);
                        }
                     }
                  }

                  if (var22 != null) {
                     this.dialogMessagesByIds.remove(var22.getId());
                     var20 = var22.messageOwner.random_id;
                     if (var20 != 0L) {
                        this.dialogMessagesByRandomIds.remove(var20);
                     }
                  }
               }
            }
         }

         ++var16;
      }

      this.allDialogs.clear();
      var16 = this.dialogs_dict.size();

      for(var15 = 0; var15 < var16; ++var15) {
         this.allDialogs.add(this.dialogs_dict.valueAt(var15));
      }

      if (!var4) {
         var8 = null;
      }

      this.sortDialogs(var8);
      if (var1 != this.DIALOGS_LOAD_TYPE_CHANNEL && var1 != this.DIALOGS_LOAD_TYPE_UNKNOWN && !var4) {
         SparseBooleanArray var26 = this.dialogsEndReached;
         if ((var2.dialogs.size() == 0 || var2.dialogs.size() != var9) && var1 == 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var26.put(var5, var14);
         if (var17 > 0 && var17 < 20 && var5 == 0) {
            this.dialogsEndReached.put(1, true);
            if (UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(var5)[0] == Integer.MAX_VALUE) {
               this.serverDialogsEndReached.put(1, true);
            }
         }

         var14 = true;
         if (!var10) {
            var26 = this.serverDialogsEndReached;
            if (var2.dialogs.size() != 0 && var2.dialogs.size() == var9 || var1 != 0) {
               var14 = false;
            }

            var26.put(var5, var14);
         }
      }

      var15 = UserConfig.getInstance(this.currentAccount).getTotalDialogsCount(var5);
      int[] var23 = UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(var5);
      if (!var10 && !var4 && var15 < 400 && var23[0] != -1 && var23[0] != Integer.MAX_VALUE) {
         this.loadDialogs(0, 100, var5, false);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      if (var4) {
         UserConfig.getInstance(this.currentAccount).migrateOffsetId = var11;
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
         this.migratingDialogs = false;
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needReloadRecentDialogsSearch);
      } else {
         this.generateUpdateMessage();
         if (!var18 && var1 == this.DIALOGS_LOAD_TYPE_CACHE) {
            this.loadDialogs(var5, 0, var9, false);
         }
      }

      this.migrateDialogs(UserConfig.getInstance(this.currentAccount).migrateOffsetId, UserConfig.getInstance(this.currentAccount).migrateOffsetDate, UserConfig.getInstance(this.currentAccount).migrateOffsetUserId, UserConfig.getInstance(this.currentAccount).migrateOffsetChatId, UserConfig.getInstance(this.currentAccount).migrateOffsetChannelId, UserConfig.getInstance(this.currentAccount).migrateOffsetAccess);
      if (!var12.isEmpty()) {
         this.reloadDialogsReadValue(var12, 0L);
      }

      this.loadUnreadDialogs();
   }

   // $FF: synthetic method
   public void lambda$null$130$MessagesController(TLObject var1, long var2) {
      TLRPC.messages_Messages var5 = (TLRPC.messages_Messages)var1;
      if (var5 != null) {
         int var4 = var5.count;
         if (var4 == 0) {
            var4 = var5.messages.size();
         }

         MessagesStorage.getInstance(this.currentAccount).resetMentionsCount(var2, var4);
      }

   }

   // $FF: synthetic method
   public void lambda$null$131$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$pgmXc5wuX4vuUELeAuqKQqje9q8(this, var3, var1));
   }

   // $FF: synthetic method
   public void lambda$null$134$MessagesController(TLRPC.Dialog var1) {
      TLRPC.Dialog var2 = (TLRPC.Dialog)this.dialogs_dict.get(var1.id);
      if (var2 != null && var2.top_message == 0) {
         this.deleteDialog(var1.id, 3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$135$MessagesController(int var1) {
      this.checkingLastMessagesDialogs.delete(var1);
   }

   // $FF: synthetic method
   public void lambda$null$137$MessagesController(TLRPC.messages_Dialogs var1, LongSparseArray var2, LongSparseArray var3, LongSparseArray var4) {
      this.putUsers(var1.users, true);
      this.putChats(var1.chats, true);

      int var5;
      int var10;
      for(var5 = 0; var5 < var2.size(); ++var5) {
         long var6 = var2.keyAt(var5);
         TLRPC.Dialog var8 = (TLRPC.Dialog)var2.valueAt(var5);
         TLRPC.Dialog var9 = (TLRPC.Dialog)this.dialogs_dict.get(var6);
         MessageObject var12;
         if (var9 == null) {
            var10 = this.nextDialogsCacheOffset.get(var8.folder_id, 0);
            this.nextDialogsCacheOffset.put(var8.folder_id, var10 + 1);
            this.dialogs_dict.put(var6, var8);
            var12 = (MessageObject)var3.get(var8.id);
            this.dialogMessage.put(var6, var12);
            if (var12 != null && var12.messageOwner.to_id.channel_id == 0) {
               this.dialogMessagesByIds.put(var12.getId(), var12);
               var6 = var12.messageOwner.random_id;
               if (var6 != 0L) {
                  this.dialogMessagesByRandomIds.put(var6, var12);
               }
            }
         } else {
            var9.unread_count = var8.unread_count;
            int var11 = var9.unread_mentions_count;
            var10 = var8.unread_mentions_count;
            if (var11 != var10) {
               var9.unread_mentions_count = var10;
               if (this.createdDialogMainThreadIds.contains(var9.id)) {
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateMentionsCount, var9.id, var9.unread_mentions_count);
               }
            }

            var12 = (MessageObject)this.dialogMessage.get(var6);
            MessageObject var13;
            if (var12 != null && var9.top_message <= 0) {
               var13 = (MessageObject)var3.get(var8.id);
               if (var12.deleted || var13 == null || var13.messageOwner.date > var12.messageOwner.date) {
                  this.dialogs_dict.put(var6, var8);
                  this.dialogMessage.put(var6, var13);
                  if (var13 != null && var13.messageOwner.to_id.channel_id == 0) {
                     this.dialogMessagesByIds.put(var13.getId(), var13);
                     var6 = var13.messageOwner.random_id;
                     if (var6 != 0L) {
                        this.dialogMessagesByRandomIds.put(var6, var13);
                     }
                  }

                  this.dialogMessagesByIds.remove(var12.getId());
                  var6 = var12.messageOwner.random_id;
                  if (var6 != 0L) {
                     this.dialogMessagesByRandomIds.remove(var6);
                  }
               }
            } else if (var12 != null && var12.deleted || var8.top_message > var9.top_message) {
               this.dialogs_dict.put(var6, var8);
               var13 = (MessageObject)var3.get(var8.id);
               this.dialogMessage.put(var6, var13);
               if (var13 != null && var13.messageOwner.to_id.channel_id == 0) {
                  this.dialogMessagesByIds.put(var13.getId(), var13);
                  var6 = var13.messageOwner.random_id;
                  if (var6 != 0L) {
                     this.dialogMessagesByRandomIds.put(var6, var13);
                  }
               }

               if (var12 != null) {
                  this.dialogMessagesByIds.remove(var12.getId());
                  var6 = var12.messageOwner.random_id;
                  if (var6 != 0L) {
                     this.dialogMessagesByRandomIds.remove(var6);
                  }
               }

               if (var13 == null) {
                  this.checkLastDialogMessage(var8, (TLRPC.InputPeer)null, 0L);
               }
            }
         }
      }

      this.allDialogs.clear();
      var10 = this.dialogs_dict.size();

      for(var5 = 0; var5 < var10; ++var5) {
         this.allDialogs.add(this.dialogs_dict.valueAt(var5));
      }

      this.sortDialogs((SparseArray)null);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      NotificationsController.getInstance(this.currentAccount).processDialogsUpdateRead(var4);
   }

   // $FF: synthetic method
   public void lambda$null$15$MessagesController(int var1, TLRPC.TL_messages_chatFull var2, int var3) {
      this.fullChats.put(var1, var2.full_chat);
      this.applyDialogNotificationsSettings((long)(-var1), var2.full_chat.notify_settings);

      for(int var4 = 0; var4 < var2.full_chat.bot_info.size(); ++var4) {
         TLRPC.BotInfo var5 = (TLRPC.BotInfo)var2.full_chat.bot_info.get(var4);
         DataQuery.getInstance(this.currentAccount).putBotInfo(var5);
      }

      this.exportedChats.put(var1, var2.full_chat.exported_invite);
      this.loadingFullChats.remove(var1);
      this.loadedFullChats.add(var1);
      this.putUsers(var2.users, false);
      this.putChats(var2.chats, false);
      if (var2.full_chat.stickerset != null) {
         DataQuery.getInstance(this.currentAccount).getGroupStickerSetById(var2.full_chat.stickerset);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var2.full_chat, var3, false, null);
   }

   // $FF: synthetic method
   public void lambda$null$150$MessagesController(long var1, int var3, int var4, boolean var5, boolean var6) {
      TLRPC.Dialog var7 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
      if (var7 != null) {
         int var8 = var7.unread_count;
         if (var3 != 0 && var4 < var7.top_message) {
            var7.unread_count = Math.max(var8 - var3, 0);
            if (var4 != Integer.MIN_VALUE) {
               int var9 = var7.unread_count;
               var3 = var7.top_message;
               if (var9 > var3 - var4) {
                  var7.unread_count = var3 - var4;
               }
            }
         } else {
            var7.unread_count = 0;
         }

         var3 = var7.folder_id;
         if (var3 != 0) {
            TLRPC.Dialog var10 = (TLRPC.Dialog)this.dialogs_dict.get(DialogObject.makeFolderDialogId(var3));
            if (var10 != null) {
               if (var5) {
                  if (this.isDialogMuted(var7.id)) {
                     var10.unread_count -= var8 - var7.unread_count;
                  } else {
                     var10.unread_mentions_count -= var8 - var7.unread_count;
                  }
               } else if (var7.unread_count == 0) {
                  if (this.isDialogMuted(var7.id)) {
                     --var10.unread_count;
                  } else {
                     --var10.unread_mentions_count;
                  }
               }
            }
         }

         if ((var8 != 0 || var7.unread_mark) && var7.unread_count == 0 && !this.isDialogMuted(var1)) {
            --this.unreadUnmutedDialogs;
         }

         if (var7.unread_mark) {
            var7.unread_mark = false;
            MessagesStorage.getInstance(this.currentAccount).setDialogUnread(var7.id, false);
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 256);
      }

      LongSparseArray var11;
      if (!var6) {
         NotificationsController.getInstance(this.currentAccount).processReadMessages((SparseLongArray)null, var1, 0, var4, false);
         var11 = new LongSparseArray(1);
         var11.put(var1, 0);
         NotificationsController.getInstance(this.currentAccount).processDialogsUpdateRead(var11);
      } else {
         NotificationsController.getInstance(this.currentAccount).processReadMessages((SparseLongArray)null, var1, 0, var4, true);
         var11 = new LongSparseArray(1);
         var11.put(var1, -1);
         NotificationsController.getInstance(this.currentAccount).processDialogsUpdateRead(var11);
      }

   }

   // $FF: synthetic method
   public void lambda$null$152$MessagesController(long var1, int var3, boolean var4, int var5, int var6, boolean var7) {
      NotificationsController.getInstance(this.currentAccount).processReadMessages((SparseLongArray)null, var1, var3, 0, var4);
      TLRPC.Dialog var8 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
      if (var8 != null) {
         var3 = var8.unread_count;
         if (var5 != 0 && var6 > var8.top_message) {
            var8.unread_count = Math.max(var3 - var5, 0);
            if (var6 != Integer.MAX_VALUE) {
               int var9 = var8.unread_count;
               var5 = var8.top_message;
               if (var9 > var6 - var5) {
                  var8.unread_count = var6 - var5;
               }
            }
         } else {
            var8.unread_count = 0;
         }

         var5 = var8.folder_id;
         if (var5 != 0) {
            TLRPC.Dialog var10 = (TLRPC.Dialog)this.dialogs_dict.get(DialogObject.makeFolderDialogId(var5));
            if (var10 != null) {
               if (var7) {
                  if (this.isDialogMuted(var8.id)) {
                     var10.unread_count -= var3 - var8.unread_count;
                  } else {
                     var10.unread_mentions_count -= var3 - var8.unread_count;
                  }
               } else if (var8.unread_count == 0) {
                  if (this.isDialogMuted(var8.id)) {
                     --var10.unread_count;
                  } else {
                     --var10.unread_mentions_count;
                  }
               }
            }
         }

         if ((var3 != 0 || var8.unread_mark) && var8.unread_count == 0 && !this.isDialogMuted(var1)) {
            --this.unreadUnmutedDialogs;
         }

         if (var8.unread_mark) {
            var8.unread_mark = false;
            MessagesStorage.getInstance(this.currentAccount).setDialogUnread(var8.id, false);
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 256);
      }

      LongSparseArray var11 = new LongSparseArray(1);
      var11.put(var1, 0);
      NotificationsController.getInstance(this.currentAccount).processDialogsUpdateRead(var11);
   }

   // $FF: synthetic method
   public void lambda$null$155$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_messages_createChat var3) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate);
   }

   // $FF: synthetic method
   public void lambda$null$156$MessagesController(TLRPC.Updates var1) {
      this.putUsers(var1.users, false);
      this.putChats(var1.chats, false);
      ArrayList var2 = var1.chats;
      if (var2 != null && !var2.isEmpty()) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidCreated, ((TLRPC.Chat)var1.chats.get(0)).id);
      } else {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate);
      }

   }

   // $FF: synthetic method
   public void lambda$null$158$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_channels_createChannel var3) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate);
   }

   // $FF: synthetic method
   public void lambda$null$159$MessagesController(TLRPC.Updates var1) {
      this.putUsers(var1.users, false);
      this.putChats(var1.chats, false);
      ArrayList var2 = var1.chats;
      if (var2 != null && !var2.isEmpty()) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidCreated, ((TLRPC.Chat)var1.chats.get(0)).id);
      } else {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate);
      }

   }

   // $FF: synthetic method
   public void lambda$null$16$MessagesController(TLRPC.TL_error var1, int var2) {
      this.checkChannelError(var1.text, var2);
      this.loadingFullChats.remove(var2);
   }

   // $FF: synthetic method
   public void lambda$null$166$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_channels_inviteToChannel var3) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3, true);
   }

   // $FF: synthetic method
   public void lambda$null$168$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 8192);
   }

   // $FF: synthetic method
   public void lambda$null$170$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 8192);
   }

   // $FF: synthetic method
   public void lambda$null$172$MessagesController(TLRPC.ChatFull var1, String var2) {
      var1.about = var2;
      MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var1, false);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var1, 0, false, null);
   }

   // $FF: synthetic method
   public void lambda$null$174$MessagesController(int var1, String var2) {
      TLRPC.Chat var3 = this.getChat(var1);
      if (var2.length() != 0) {
         var3.flags |= 64;
      } else {
         var3.flags &= -65;
      }

      var3.username = var2;
      ArrayList var4 = new ArrayList();
      var4.add(var3);
      MessagesStorage.getInstance(this.currentAccount).putUsersAndChats((ArrayList)null, var4, true, true);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 8192);
   }

   // $FF: synthetic method
   public void lambda$null$177$MessagesController(int var1) {
      this.joiningToChannels.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$null$178$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLObject var3, boolean var4, boolean var5, TLRPC.InputUser var6) {
      int var7 = this.currentAccount;
      if (var4 && !var5) {
         var5 = true;
      } else {
         var5 = false;
      }

      AlertsCreator.processError(var7, var1, var2, var3, var5);
      if (var4 && var6 instanceof TLRPC.TL_inputUserSelf) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 8192);
      }

   }

   // $FF: synthetic method
   public void lambda$null$179$MessagesController(int var1) {
      this.loadFullChat(var1, 0, true);
   }

   // $FF: synthetic method
   public void lambda$null$18$MessagesController(TLRPC.User var1, TLRPC.UserFull var2, int var3) {
      this.applyDialogNotificationsSettings((long)var1.id, var2.notify_settings);
      if (var2.bot_info instanceof TLRPC.TL_botInfo) {
         DataQuery.getInstance(this.currentAccount).putBotInfo(var2.bot_info);
      }

      int var4 = this.blockedUsers.indexOfKey(var1.id);
      if (var2.blocked) {
         if (var4 < 0) {
            SparseIntArray var5 = new SparseIntArray();
            var5.put(var1.id, 1);
            MessagesStorage.getInstance(this.currentAccount).putBlockedUsers(var5, false);
            this.blockedUsers.put(var1.id, 1);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad);
         }
      } else if (var4 >= 0) {
         MessagesStorage.getInstance(this.currentAccount).deleteBlockedUser(var1.id);
         this.blockedUsers.removeAt(var4);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad);
      }

      this.fullUsers.put(var1.id, var2);
      this.loadingFullUsers.remove(var1.id);
      this.loadedFullUsers.add(var1.id);
      StringBuilder var7 = new StringBuilder();
      var7.append(var1.first_name);
      var7.append(var1.last_name);
      var7.append(var1.username);
      String var8 = var7.toString();
      ArrayList var6 = new ArrayList();
      var6.add(var2.user);
      this.putUsers(var6, false);
      MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var6, (ArrayList)null, false, true);
      if (var8 != null) {
         StringBuilder var9 = new StringBuilder();
         var9.append(var2.user.first_name);
         var9.append(var2.user.last_name);
         var9.append(var2.user.username);
         if (!var8.equals(var9.toString())) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1);
         }
      }

      if (var2.bot_info instanceof TLRPC.TL_botInfo) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.botInfoDidLoad, var2.bot_info, var3);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.userInfoDidLoad, var1.id, var2, null);
   }

   // $FF: synthetic method
   public void lambda$null$181$MessagesController(int var1) {
      this.loadFullChat(var1, 0, true);
   }

   // $FF: synthetic method
   public void lambda$null$188$MessagesController() {
      this.registeringForPush = false;
   }

   // $FF: synthetic method
   public void lambda$null$19$MessagesController(TLRPC.User var1) {
      this.loadingFullUsers.remove(var1.id);
   }

   // $FF: synthetic method
   public void lambda$null$197$MessagesController(TLRPC.updates_ChannelDifference var1) {
      this.putUsers(var1.users, false);
      this.putChats(var1.chats, false);
   }

   // $FF: synthetic method
   public void lambda$null$198$MessagesController(SparseArray var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         int var3 = var1.keyAt(var2);
         long[] var4 = (long[])var1.valueAt(var2);
         int var5 = (int)var4[1];
         SendMessagesHelper.getInstance(this.currentAccount).processSentMessage(var5);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, var5, var3, null, var4[0], 0L, -1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$199$MessagesController(LongSparseArray var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.updateInterfaceWithMessages(var1.keyAt(var2), (ArrayList)var1.valueAt(var2));
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$null$200$MessagesController(ArrayList var1) {
      NotificationsController.getInstance(this.currentAccount).processNewMessages(var1, true, false, (CountDownLatch)null);
   }

   // $FF: synthetic method
   public void lambda$null$201$MessagesController(ArrayList var1, TLRPC.updates_ChannelDifference var2) {
      if (!var1.isEmpty()) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$BmJZ4Zdfo6BIVJxBYMn2TEeaaC0(this, var1));
      }

      MessagesStorage.getInstance(this.currentAccount).putMessages(var2.new_messages, true, false, false, DownloadController.getInstance(this.currentAccount).getAutodownloadMask());
   }

   // $FF: synthetic method
   public void lambda$null$202$MessagesController(TLRPC.updates_ChannelDifference var1, int var2, TLRPC.Chat var3, SparseArray var4, int var5, long var6) {
      TLRPC.Chat var8 = var3;
      long var9;
      Integer var11;
      boolean var14;
      if (!(var1 instanceof TLRPC.TL_updates_channelDifference) && !(var1 instanceof TLRPC.TL_updates_channelDifferenceEmpty)) {
         if (var1 instanceof TLRPC.TL_updates_channelDifferenceTooLong) {
            var9 = (long)(-var2);
            Integer var20 = (Integer)this.dialogs_read_inbox_max.get(var9);
            Integer var19 = var20;
            if (var20 == null) {
               var19 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, var9);
               this.dialogs_read_inbox_max.put(var9, var19);
            }

            var11 = (Integer)this.dialogs_read_outbox_max.get(var9);
            var20 = var11;
            if (var11 == null) {
               var20 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var9);
               this.dialogs_read_outbox_max.put(var9, var20);
            }

            for(int var12 = 0; var12 < var1.messages.size(); ++var12) {
               TLRPC.Message var24;
               label125: {
                  var24 = (TLRPC.Message)var1.messages.get(var12);
                  var24.dialog_id = var9;
                  if (!(var24.action instanceof TLRPC.TL_messageActionChannelCreate) && (var8 == null || !var8.left)) {
                     if (var24.out) {
                        var11 = var20;
                     } else {
                        var11 = var19;
                     }

                     if (var11 < var24.id) {
                        var14 = true;
                        break label125;
                     }
                  }

                  var14 = false;
               }

               var24.unread = var14;
               if (var8 != null && var8.megagroup) {
                  var24.flags |= Integer.MIN_VALUE;
               }
            }

            MessagesStorage.getInstance(this.currentAccount).overwriteChannel(var2, (TLRPC.TL_updates_channelDifferenceTooLong)var1, var5);
         }
      } else {
         if (!var1.new_messages.isEmpty()) {
            LongSparseArray var15 = new LongSparseArray();
            ImageLoader.saveMessagesThumbs(var1.new_messages);
            ArrayList var16 = new ArrayList();
            var9 = (long)(-var2);
            var11 = (Integer)this.dialogs_read_inbox_max.get(var9);
            Integer var22 = var11;
            if (var11 == null) {
               var22 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, var9);
               this.dialogs_read_inbox_max.put(var9, var22);
            }

            Integer var13 = (Integer)this.dialogs_read_outbox_max.get(var9);
            var11 = var13;
            if (var13 == null) {
               var11 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var9);
               this.dialogs_read_outbox_max.put(var9, var11);
            }

            var5 = 0;

            while(true) {
               if (var5 >= var1.new_messages.size()) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$unrGLtAr3G8guGu4ob0bX3amrwg(this, var15));
                  MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$ljkOL0yOoU2gtEo0YqV4Xl_c3YY(this, var16, var1));
                  break;
               }

               TLRPC.Message var18;
               label151: {
                  var18 = (TLRPC.Message)var1.new_messages.get(var5);
                  if (var3 == null || !var3.left) {
                     if (var18.out) {
                        var13 = var11;
                     } else {
                        var13 = var22;
                     }

                     if (var13 < var18.id && !(var18.action instanceof TLRPC.TL_messageActionChannelCreate)) {
                        var14 = true;
                        break label151;
                     }
                  }

                  var14 = false;
               }

               var18.unread = var14;
               if (var3 != null && var3.megagroup) {
                  var18.flags |= Integer.MIN_VALUE;
               }

               MessageObject var25 = new MessageObject(this.currentAccount, var18, var4, this.createdDialogIds.contains(var9));
               if (!var25.isOut() && var25.isUnread()) {
                  var16.add(var25);
               }

               ArrayList var17 = (ArrayList)var15.get(var9);
               ArrayList var23 = var17;
               if (var17 == null) {
                  var23 = new ArrayList();
                  var15.put(var9, var23);
               }

               var23.add(var25);
               ++var5;
            }
         }

         if (!var1.other_updates.isEmpty()) {
            this.processUpdateArray(var1.other_updates, var1.users, var1.chats, true);
         }

         this.processChannelsUpdatesQueue(var2, 1);
         MessagesStorage.getInstance(this.currentAccount).saveChannelPts(var2, var1.pts);
      }

      this.gettingDifferenceChannels.delete(var2);
      this.channelsPts.put(var2, var1.pts);
      if ((var1.flags & 2) != 0) {
         this.shortPollChannels.put(var2, (int)(System.currentTimeMillis() / 1000L) + var1.timeout);
      }

      if (!var1.isFinal) {
         this.getChannelDifference(var2);
      }

      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var21 = new StringBuilder();
         var21.append("received channel difference with pts = ");
         var21.append(var1.pts);
         var21.append(" channelId = ");
         var21.append(var2);
         FileLog.d(var21.toString());
         var21 = new StringBuilder();
         var21.append("new_messages = ");
         var21.append(var1.new_messages.size());
         var21.append(" messages = ");
         var21.append(var1.messages.size());
         var21.append(" users = ");
         var21.append(var1.users.size());
         var21.append(" chats = ");
         var21.append(var1.chats.size());
         var21.append(" other updates = ");
         var21.append(var1.other_updates.size());
         FileLog.d(var21.toString());
      }

      if (var6 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var6);
      }

   }

   // $FF: synthetic method
   public void lambda$null$203$MessagesController(ArrayList var1, int var2, TLRPC.updates_ChannelDifference var3, TLRPC.Chat var4, SparseArray var5, int var6, long var7) {
      if (!var1.isEmpty()) {
         SparseArray var9 = new SparseArray();
         Iterator var10 = var1.iterator();

         while(var10.hasNext()) {
            TLRPC.TL_updateMessageID var11 = (TLRPC.TL_updateMessageID)var10.next();
            long[] var12 = MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(var11.random_id, (Integer)null, var11.id, 0, false, var2);
            if (var12 != null) {
               var9.put(var11.id, var12);
            }
         }

         if (var9.size() != 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$knZtwzjIKj6DQF9JLJKyqk4QKIg(this, var9));
         }
      }

      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$vDE_ZP4HWcci4r3xUMBi6xf1Wh0(this, var3, var2, var4, var5, var6, var7));
   }

   // $FF: synthetic method
   public void lambda$null$204$MessagesController(TLRPC.TL_error var1, int var2) {
      this.checkChannelError(var1.text, var2);
   }

   // $FF: synthetic method
   public void lambda$null$206$MessagesController(TLRPC.updates_Difference var1, int var2, int var3) {
      this.loadedFullUsers.clear();
      this.loadedFullChats.clear();
      this.resetDialogs(true, MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), var1.pts, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$207$MessagesController(TLRPC.updates_Difference var1) {
      this.loadedFullUsers.clear();
      this.loadedFullChats.clear();
      this.putUsers(var1.users, false);
      this.putChats(var1.chats, false);
   }

   // $FF: synthetic method
   public void lambda$null$208$MessagesController(SparseArray var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         int var3 = var1.keyAt(var2);
         long[] var4 = (long[])var1.valueAt(var2);
         int var5 = (int)var4[1];
         SendMessagesHelper.getInstance(this.currentAccount).processSentMessage(var5);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, var5, var3, null, var4[0], 0L, -1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$209$MessagesController(LongSparseArray var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.updateInterfaceWithMessages(var1.keyAt(var2), (ArrayList)var1.valueAt(var2));
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$null$21$MessagesController(long var1, ArrayList var3, ArrayList var4) {
      ArrayList var5 = (ArrayList)this.reloadingMessages.get(var1);
      if (var5 != null) {
         var5.removeAll(var3);
         if (var5.isEmpty()) {
            this.reloadingMessages.remove(var1);
         }
      }

      MessageObject var8 = (MessageObject)this.dialogMessage.get(var1);
      if (var8 != null) {
         for(int var6 = 0; var6 < var4.size(); ++var6) {
            MessageObject var7 = (MessageObject)var4.get(var6);
            if (var8 != null && var8.getId() == var7.getId()) {
               this.dialogMessage.put(var1, var7);
               if (var7.messageOwner.to_id.channel_id == 0) {
                  var8 = (MessageObject)this.dialogMessagesByIds.get(var7.getId());
                  this.dialogMessagesByIds.remove(var7.getId());
                  if (var8 != null) {
                     this.dialogMessagesByIds.put(var8.getId(), var8);
                  }
               }

               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
               break;
            }
         }
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, var1, var4);
   }

   // $FF: synthetic method
   public void lambda$null$210$MessagesController(ArrayList var1, TLRPC.updates_Difference var2) {
      NotificationsController var3 = NotificationsController.getInstance(this.currentAccount);
      boolean var4;
      if (!(var2 instanceof TLRPC.TL_updates_differenceSlice)) {
         var4 = true;
      } else {
         var4 = false;
      }

      var3.processNewMessages(var1, var4, false, (CountDownLatch)null);
   }

   // $FF: synthetic method
   public void lambda$null$211$MessagesController(ArrayList var1, TLRPC.updates_Difference var2) {
      if (!var1.isEmpty()) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$ecwYtn1RRyD_7_xCcWPT8y9SkzY(this, var1, var2));
      }

      MessagesStorage.getInstance(this.currentAccount).putMessages(var2.new_messages, true, false, false, DownloadController.getInstance(this.currentAccount).getAutodownloadMask());
   }

   // $FF: synthetic method
   public void lambda$null$212$MessagesController(TLRPC.updates_Difference var1, SparseArray var2, SparseArray var3) {
      boolean var4 = var1.new_messages.isEmpty();
      byte var5 = 0;
      byte var6 = 0;
      int var8;
      if (!var4 || !var1.new_encrypted_messages.isEmpty()) {
         LongSparseArray var7 = new LongSparseArray();

         ArrayList var17;
         for(var8 = 0; var8 < var1.new_encrypted_messages.size(); ++var8) {
            TLRPC.EncryptedMessage var9 = (TLRPC.EncryptedMessage)var1.new_encrypted_messages.get(var8);
            var17 = SecretChatHelper.getInstance(this.currentAccount).decryptMessage(var9);
            if (var17 != null && !var17.isEmpty()) {
               var1.new_messages.addAll(var17);
            }
         }

         ImageLoader.saveMessagesThumbs(var1.new_messages);
         ArrayList var10 = new ArrayList();
         int var11 = UserConfig.getInstance(this.currentAccount).getClientUserId();

         for(var8 = 0; var8 < var1.new_messages.size(); ++var8) {
            TLRPC.Message var12 = (TLRPC.Message)var1.new_messages.get(var8);
            if (var12.dialog_id == 0L) {
               TLRPC.Peer var18 = var12.to_id;
               int var13 = var18.chat_id;
               if (var13 != 0) {
                  var12.dialog_id = (long)(-var13);
               } else {
                  if (var18.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                     var12.to_id.user_id = var12.from_id;
                  }

                  var12.dialog_id = (long)var12.to_id.user_id;
               }
            }

            if ((int)var12.dialog_id != 0) {
               TLRPC.MessageAction var19 = var12.action;
               if (var19 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                  TLRPC.User var20 = (TLRPC.User)var2.get(var19.user_id);
                  if (var20 != null && var20.bot) {
                     var12.reply_markup = new TLRPC.TL_replyKeyboardHide();
                     var12.flags |= 64;
                  }
               }

               var19 = var12.action;
               if (!(var19 instanceof TLRPC.TL_messageActionChatMigrateTo) && !(var19 instanceof TLRPC.TL_messageActionChannelCreate)) {
                  ConcurrentHashMap var21;
                  if (var12.out) {
                     var21 = this.dialogs_read_outbox_max;
                  } else {
                     var21 = this.dialogs_read_inbox_max;
                  }

                  Integer var14 = (Integer)var21.get(var12.dialog_id);
                  Integer var15 = var14;
                  if (var14 == null) {
                     var15 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var12.out, var12.dialog_id);
                     var21.put(var12.dialog_id, var15);
                  }

                  if (var15 < var12.id) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  var12.unread = var4;
               } else {
                  var12.unread = false;
                  var12.media_unread = false;
               }
            }

            if (var12.dialog_id == (long)var11) {
               var12.unread = false;
               var12.media_unread = false;
               var12.out = true;
            }

            MessageObject var22 = new MessageObject(this.currentAccount, var12, var2, var3, this.createdDialogIds.contains(var12.dialog_id));
            if (!var22.isOut() && var22.isUnread()) {
               var10.add(var22);
            }

            ArrayList var23 = (ArrayList)var7.get(var12.dialog_id);
            var17 = var23;
            if (var23 == null) {
               var17 = new ArrayList();
               var7.put(var12.dialog_id, var17);
            }

            var17.add(var22);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$7U_DZAtefOGyLnbtJ_TzAYloz70(this, var7));
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$CTc5p_O22dGYncyM4gxumAZNVws(this, var10, var1));
         SecretChatHelper.getInstance(this.currentAccount).processPendingEncMessages();
      }

      if (!var1.other_updates.isEmpty()) {
         this.processUpdateArray(var1.other_updates, var1.users, var1.chats, true);
      }

      if (var1 instanceof TLRPC.TL_updates_difference) {
         this.gettingDifference = false;
         MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(var1.state.seq);
         MessagesStorage.getInstance(this.currentAccount).setLastDateValue(var1.state.date);
         MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var1.state.pts);
         MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(var1.state.qts);
         ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(false);

         for(var8 = var6; var8 < 3; ++var8) {
            this.processUpdatesQueue(var8, 1);
         }
      } else if (var1 instanceof TLRPC.TL_updates_differenceSlice) {
         MessagesStorage.getInstance(this.currentAccount).setLastDateValue(var1.intermediate_state.date);
         MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var1.intermediate_state.pts);
         MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(var1.intermediate_state.qts);
      } else if (var1 instanceof TLRPC.TL_updates_differenceEmpty) {
         this.gettingDifference = false;
         MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(var1.seq);
         MessagesStorage.getInstance(this.currentAccount).setLastDateValue(var1.date);
         ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(false);

         for(var8 = var5; var8 < 3; ++var8) {
            this.processUpdatesQueue(var8, 1);
         }
      }

      MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var16 = new StringBuilder();
         var16.append("received difference with date = ");
         var16.append(MessagesStorage.getInstance(this.currentAccount).getLastDateValue());
         var16.append(" pts = ");
         var16.append(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue());
         var16.append(" seq = ");
         var16.append(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue());
         var16.append(" messages = ");
         var16.append(var1.new_messages.size());
         var16.append(" users = ");
         var16.append(var1.users.size());
         var16.append(" chats = ");
         var16.append(var1.chats.size());
         var16.append(" other updates = ");
         var16.append(var1.other_updates.size());
         FileLog.d(var16.toString());
      }

   }

   // $FF: synthetic method
   public void lambda$null$213$MessagesController(TLRPC.updates_Difference var1, ArrayList var2, SparseArray var3, SparseArray var4) {
      MessagesStorage var5 = MessagesStorage.getInstance(this.currentAccount);
      ArrayList var6 = var1.users;
      ArrayList var7 = var1.chats;
      int var8 = 0;
      var5.putUsersAndChats(var6, var7, true, false);
      if (!var2.isEmpty()) {
         SparseArray var10;
         for(var10 = new SparseArray(); var8 < var2.size(); ++var8) {
            TLRPC.TL_updateMessageID var11 = (TLRPC.TL_updateMessageID)var2.get(var8);
            long[] var9 = MessagesStorage.getInstance(this.currentAccount).updateMessageStateAndId(var11.random_id, (Integer)null, var11.id, 0, false, 0);
            if (var9 != null) {
               var10.put(var11.id, var9);
            }
         }

         if (var10.size() != 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$ITmJA6kLHBukKSXRGmYt1a3rPug(this, var10));
         }
      }

      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$Ey750lTvkjqFS34n7sagq8Medx8(this, var1, var3, var4));
   }

   // $FF: synthetic method
   public void lambda$null$216$MessagesController(TLObject var1) {
      if (var1 != null) {
         TLRPC.Vector var8 = (TLRPC.Vector)var1;
         int var2 = var8.objects.size();

         for(int var3 = 0; var3 < var2; ++var3) {
            TLRPC.DialogPeer var4 = (TLRPC.DialogPeer)var8.objects.get(var3);
            if (var4 instanceof TLRPC.TL_dialogPeer) {
               TLRPC.Peer var9 = ((TLRPC.TL_dialogPeer)var4).peer;
               int var5 = var9.user_id;
               long var6;
               if (var5 != 0) {
                  if (var5 != 0) {
                     var6 = (long)var5;
                  } else {
                     var5 = var9.chat_id;
                     if (var5 != 0) {
                        var5 = -var5;
                     } else {
                        var5 = -var9.channel_id;
                     }

                     var6 = (long)var5;
                  }
               } else {
                  var6 = 0L;
               }

               MessagesStorage.getInstance(this.currentAccount).setDialogUnread(var6, true);
               TLRPC.Dialog var10 = (TLRPC.Dialog)this.dialogs_dict.get(var6);
               if (var10 != null && !var10.unread_mark) {
                  var10.unread_mark = true;
                  if (var10.unread_count == 0 && !this.isDialogMuted(var6)) {
                     ++this.unreadUnmutedDialogs;
                  }
               }
            }
         }

         UserConfig.getInstance(this.currentAccount).unreadDialogsLoaded = true;
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 256);
         this.loadingUnreadDialogs = false;
      }

   }

   // $FF: synthetic method
   public void lambda$null$220$MessagesController(int var1, ArrayList var2, boolean var3, TLRPC.TL_messages_peerDialogs var4, LongSparseArray var5, TLRPC.TL_messages_dialogs var6) {
      this.loadingPinnedDialogs.delete(var1);
      this.applyDialogsNotificationsSettings(var2);
      ArrayList var7 = this.getDialogs(var1);
      int var8 = var3;
      int var9 = 0;
      int var10 = 0;

      TLRPC.Dialog var11;
      boolean var16;
      for(var16 = false; var9 < var7.size(); ++var9) {
         var11 = (TLRPC.Dialog)var7.get(var9);
         if (!(var11 instanceof TLRPC.TL_dialogFolder)) {
            if ((int)var11.id == 0) {
               if (var8 < var2.size()) {
                  var2.add(var8, var11);
               } else {
                  var2.add(var11);
               }

               ++var8;
            } else {
               if (!var11.pinned) {
                  break;
               }

               var10 = Math.max(var11.pinnedNum, var10);
               var11.pinned = false;
               var11.pinnedNum = 0;
               ++var8;
               var16 = true;
            }
         }
      }

      var7 = new ArrayList();
      boolean var13;
      boolean var19;
      if (!var2.isEmpty()) {
         this.putUsers(var4.users, false);
         this.putChats(var4.chats, false);
         int var12 = var2.size();
         var9 = 0;
         var13 = false;
         var19 = var16;
         var16 = var13;

         while(true) {
            var13 = var16;
            if (var9 >= var12) {
               break;
            }

            TLRPC.Dialog var17 = (TLRPC.Dialog)var2.get(var9);
            var17.pinnedNum = var12 - var9 + var10;
            var7.add(var17.id);
            var11 = (TLRPC.Dialog)this.dialogs_dict.get(var17.id);
            if (var11 != null) {
               var11.pinned = true;
               var11.pinnedNum = var17.pinnedNum;
               MessagesStorage.getInstance(this.currentAccount).setDialogPinned(var17.id, var17.pinnedNum);
            } else {
               this.dialogs_dict.put(var17.id, var17);
               MessageObject var20 = (MessageObject)var5.get(var17.id);
               this.dialogMessage.put(var17.id, var20);
               if (var20 != null && var20.messageOwner.to_id.channel_id == 0) {
                  this.dialogMessagesByIds.put(var20.getId(), var20);
                  long var14 = var20.messageOwner.random_id;
                  if (var14 != 0L) {
                     this.dialogMessagesByRandomIds.put(var14, var20);
                  }
               }

               var16 = true;
            }

            ++var9;
            var19 = true;
         }
      } else {
         var13 = false;
         var19 = var16;
      }

      if (var19) {
         if (var13) {
            this.allDialogs.clear();
            var8 = this.dialogs_dict.size();

            for(int var18 = 0; var18 < var8; ++var18) {
               this.allDialogs.add(this.dialogs_dict.valueAt(var18));
            }
         }

         this.sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      }

      MessagesStorage.getInstance(this.currentAccount).unpinAllDialogsExceptNew(var7, var1);
      MessagesStorage.getInstance(this.currentAccount).putDialogs(var6, 1);
      UserConfig.getInstance(this.currentAccount).setPinnedDialogsLoaded(var1, true);
      UserConfig.getInstance(this.currentAccount).saveConfig(false);
   }

   // $FF: synthetic method
   public void lambda$null$221$MessagesController(int var1, ArrayList var2, boolean var3, TLRPC.TL_messages_peerDialogs var4, LongSparseArray var5, TLRPC.TL_messages_dialogs var6) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$LYiV_HiXwANpNMOPEqVG43Un0Dg(this, var1, var2, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$null$223$MessagesController(ArrayList var1) {
      NotificationsController.getInstance(this.currentAccount).processNewMessages(var1, true, false, (CountDownLatch)null);
   }

   // $FF: synthetic method
   public void lambda$null$226$MessagesController(TLRPC.TL_channels_channelParticipant var1) {
      this.putUsers(var1.users, false);
   }

   // $FF: synthetic method
   public void lambda$null$227$MessagesController(ArrayList var1) {
      NotificationsController.getInstance(this.currentAccount).processNewMessages(var1, true, false, (CountDownLatch)null);
   }

   // $FF: synthetic method
   public void lambda$null$228$MessagesController(ArrayList var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$w_u2qeXpmHAUSajG7KA1I1Awfes(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$229$MessagesController(int var1, ArrayList var2) {
      this.updateInterfaceWithMessages((long)(-var1), var2);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$null$230$MessagesController(TLRPC.Chat var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      TLRPC.TL_channels_channelParticipant var11 = (TLRPC.TL_channels_channelParticipant)var3;
      if (var11 != null) {
         TLRPC.ChannelParticipant var12 = var11.participant;
         if (var12 instanceof TLRPC.TL_channelParticipantSelf && var12.inviter_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            if (var1.megagroup && MessagesStorage.getInstance(this.currentAccount).isMigratedChat(var1.id)) {
               return;
            }

            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Nh66UA3TNkn3k9orw_Ewox9RITw(this, var11));
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var11.users, (ArrayList)null, true, true);
            TLRPC.TL_messageService var13 = new TLRPC.TL_messageService();
            var13.media_unread = true;
            var13.unread = true;
            var13.flags = 256;
            var13.post = true;
            if (var1.megagroup) {
               var13.flags |= Integer.MIN_VALUE;
            }

            int var5 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
            var13.id = var5;
            var13.local_id = var5;
            var13.date = var11.participant.date;
            var13.action = new TLRPC.TL_messageActionChatAddUser();
            var13.from_id = var11.participant.inviter_id;
            var13.action.users.add(UserConfig.getInstance(this.currentAccount).getClientUserId());
            var13.to_id = new TLRPC.TL_peerChannel();
            var13.to_id.channel_id = var2;
            var13.dialog_id = (long)(-var2);
            UserConfig var9 = UserConfig.getInstance(this.currentAccount);
            var5 = 0;
            var9.saveConfig(false);
            ArrayList var6 = new ArrayList();
            ArrayList var7 = new ArrayList();

            ConcurrentHashMap var10;
            for(var10 = new ConcurrentHashMap(); var5 < var11.users.size(); ++var5) {
               TLRPC.User var8 = (TLRPC.User)var11.users.get(var5);
               var10.put(var8.id, var8);
            }

            var7.add(var13);
            var6.add(new MessageObject(this.currentAccount, var13, var10, true));
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$BtccFZ3tf0NlzJfs_5gQlx4EP8M(this, var6));
            MessagesStorage.getInstance(this.currentAccount).putMessages(var7, true, true, false, 0);
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$LcotUNrqwp_WpEp6G2YIbDReBNg(this, var2, var6));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$234$MessagesController(ArrayList var1) {
      NotificationsController.getInstance(this.currentAccount).processNewMessages(var1, true, false, (CountDownLatch)null);
   }

   // $FF: synthetic method
   public void lambda$null$240$MessagesController(TLRPC.TL_updateUserBlocked var1) {
      if (var1.blocked) {
         if (this.blockedUsers.indexOfKey(var1.user_id) < 0) {
            this.blockedUsers.put(var1.user_id, 1);
         }
      } else {
         this.blockedUsers.delete(var1.user_id);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad);
   }

   // $FF: synthetic method
   public void lambda$null$244$MessagesController(ArrayList var1) {
      NotificationsController.getInstance(this.currentAccount).processNewMessages(var1, true, false, (CountDownLatch)null);
   }

   // $FF: synthetic method
   public void lambda$null$246$MessagesController(TLRPC.User var1) {
      ContactsController.getInstance(this.currentAccount).addContactToPhoneBook(var1, true);
   }

   // $FF: synthetic method
   public void lambda$null$247$MessagesController(TLRPC.TL_updateChannel var1) {
      this.getChannelDifference(var1.channel_id, 1, 0L, (TLRPC.InputChannel)null);
   }

   // $FF: synthetic method
   public void lambda$null$248$MessagesController(TLRPC.Chat var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.channelRightsUpdated, var1);
   }

   // $FF: synthetic method
   public void lambda$null$249$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         this.processUpdates((TLRPC.Updates)var1, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$251$MessagesController(SparseLongArray var1, SparseLongArray var2, SparseIntArray var3, ArrayList var4, SparseArray var5, SparseIntArray var6) {
      int var7 = 0;
      int var9;
      int var10;
      int var11;
      int var14;
      MessageObject var19;
      if (var1 != null || var2 != null) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesRead, var1, var2);
         int var12;
         if (var1 == null) {
            var10 = 0;
         } else {
            NotificationsController.getInstance(this.currentAccount).processReadMessages(var1, 0L, 0, 0, false);
            Editor var8 = this.notificationsPreferences.edit();
            var9 = var1.size();
            var10 = 0;

            for(var7 = 0; var10 < var9; var7 = var14) {
               var11 = var1.keyAt(var10);
               var12 = (int)var1.valueAt(var10);
               TLRPC.Dialog var13 = (TLRPC.Dialog)this.dialogs_dict.get((long)var11);
               var14 = var7;
               if (var13 != null) {
                  int var15 = var13.top_message;
                  var14 = var7;
                  if (var15 > 0) {
                     var14 = var7;
                     if (var15 <= var12) {
                        MessageObject var23 = (MessageObject)this.dialogMessage.get(var13.id);
                        var14 = var7;
                        if (var23 != null) {
                           var14 = var7;
                           if (!var23.isOut()) {
                              var23.setIsRead();
                              var14 = var7 | 256;
                           }
                        }
                     }
                  }
               }

               if (var11 != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  StringBuilder var24 = new StringBuilder();
                  var24.append("diditem");
                  var24.append(var11);
                  var8.remove(var24.toString());
                  var24 = new StringBuilder();
                  var24.append("diditemo");
                  var24.append(var11);
                  var8.remove(var24.toString());
               }

               ++var10;
            }

            var8.commit();
            var10 = var7;
         }

         var7 = var10;
         if (var2 != null) {
            var9 = var2.size();
            var14 = 0;

            while(true) {
               var7 = var10;
               if (var14 >= var9) {
                  break;
               }

               var7 = var2.keyAt(var14);
               var11 = (int)var2.valueAt(var14);
               TLRPC.Dialog var18 = (TLRPC.Dialog)this.dialogs_dict.get((long)var7);
               var7 = var10;
               if (var18 != null) {
                  var12 = var18.top_message;
                  var7 = var10;
                  if (var12 > 0) {
                     var7 = var10;
                     if (var12 <= var11) {
                        var19 = (MessageObject)this.dialogMessage.get(var18.id);
                        var7 = var10;
                        if (var19 != null) {
                           var7 = var10;
                           if (var19.isOut()) {
                              var19.setIsRead();
                              var7 = var10 | 256;
                           }
                        }
                     }
                  }
               }

               ++var14;
               var10 = var7;
            }
         }
      }

      var10 = var7;
      long var16;
      if (var3 != null) {
         var9 = var3.size();
         var14 = 0;

         while(true) {
            var10 = var7;
            if (var14 >= var9) {
               break;
            }

            var10 = var3.keyAt(var14);
            var11 = var3.valueAt(var14);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesReadEncrypted, var10, var11);
            var16 = (long)var10 << 32;
            var10 = var7;
            if ((TLRPC.Dialog)this.dialogs_dict.get(var16) != null) {
               var19 = (MessageObject)this.dialogMessage.get(var16);
               var10 = var7;
               if (var19 != null) {
                  var10 = var7;
                  if (var19.messageOwner.date <= var11) {
                     var19.setIsRead();
                     var10 = var7 | 256;
                  }
               }
            }

            ++var14;
            var7 = var10;
         }
      }

      if (var4 != null) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, var4);
      }

      if (var5 != null) {
         var9 = var5.size();

         for(var7 = 0; var7 < var9; ++var7) {
            var14 = var5.keyAt(var7);
            ArrayList var20 = (ArrayList)var5.valueAt(var7);
            if (var20 != null) {
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, var20, var14);
               MessageObject var21;
               if (var14 == 0) {
                  var11 = var20.size();

                  for(var14 = 0; var14 < var11; ++var14) {
                     Integer var22 = (Integer)var20.get(var14);
                     var21 = (MessageObject)this.dialogMessagesByIds.get(var22);
                     if (var21 != null) {
                        var21.deleted = true;
                     }
                  }
               } else {
                  var21 = (MessageObject)this.dialogMessage.get((long)(-var14));
                  if (var21 != null) {
                     var11 = var20.size();

                     for(var14 = 0; var14 < var11; ++var14) {
                        if (var21.getId() == (Integer)var20.get(var14)) {
                           var21.deleted = true;
                           break;
                        }
                     }
                  }
               }
            }
         }

         NotificationsController.getInstance(this.currentAccount).removeDeletedMessagesFromNotifications(var5);
      }

      if (var6 != null) {
         var14 = var6.size();

         for(var7 = 0; var7 < var14; ++var7) {
            var9 = var6.keyAt(var7);
            var11 = var6.valueAt(var7);
            var16 = (long)(-var9);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.historyCleared, var16, var11);
            var19 = (MessageObject)this.dialogMessage.get(var16);
            if (var19 != null && var19.getId() <= var11) {
               var19.deleted = true;
               break;
            }
         }

         NotificationsController.getInstance(this.currentAccount).removeDeletedHisoryFromNotifications(var6);
      }

      if (var10 != 0) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, var10);
      }

   }

   // $FF: synthetic method
   public void lambda$null$256$MessagesController(AlertDialog var1, TLObject var2, BaseFragment var3, Bundle var4) {
      try {
         var1.dismiss();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      TLRPC.messages_Messages var6 = (TLRPC.messages_Messages)var2;
      this.putUsers(var6.users, false);
      this.putChats(var6.chats, false);
      MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var6.users, var6.chats, true, true);
      var3.presentFragment(new ChatActivity(var4), true);
   }

   // $FF: synthetic method
   public void lambda$null$259$MessagesController(AlertDialog[] var1, BaseFragment var2, TLRPC.TL_error var3, TLObject var4, int var5) {
      try {
         var1[0].dismiss();
      } catch (Exception var8) {
      }

      var1[0] = null;
      var2.setVisibleDialog((Dialog)null);
      if (var3 == null) {
         TLRPC.TL_contacts_resolvedPeer var9 = (TLRPC.TL_contacts_resolvedPeer)var4;
         this.putUsers(var9.users, false);
         this.putChats(var9.chats, false);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var9.users, var9.chats, false, true);
         if (!var9.chats.isEmpty()) {
            openChatOrProfileWith((TLRPC.User)null, (TLRPC.Chat)var9.chats.get(0), var2, 1, false);
         } else if (!var9.users.isEmpty()) {
            openChatOrProfileWith((TLRPC.User)var9.users.get(0), (TLRPC.Chat)null, var2, var5, false);
         }
      } else if (var2 != null && var2.getParentActivity() != null) {
         try {
            Toast.makeText(var2.getParentActivity(), LocaleController.getString("NoUsernameFound", 2131559955), 0).show();
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$26$MessagesController(long var1) {
      this.loadingPeerSettings.remove(var1);
      Editor var3 = this.notificationsPreferences.edit();
      StringBuilder var4 = new StringBuilder();
      var4.append("spam_");
      var4.append(var1);
      var3.remove(var4.toString());
      var4 = new StringBuilder();
      var4.append("spam3_");
      var4.append(var1);
      var3.putInt(var4.toString(), 1);
      var3.commit();
   }

   // $FF: synthetic method
   public void lambda$null$261$MessagesController(int var1, DialogInterface var2) {
      ConnectionsManager.getInstance(this.currentAccount).cancelRequest(var1, true);
   }

   // $FF: synthetic method
   public void lambda$null$28$MessagesController(long var1, TLObject var3) {
      this.loadingPeerSettings.remove(var1);
      if (var3 != null) {
         TLRPC.TL_peerSettings var4 = (TLRPC.TL_peerSettings)var3;
         Editor var5 = this.notificationsPreferences.edit();
         StringBuilder var6;
         if (!var4.report_spam) {
            if (BuildVars.LOGS_ENABLED) {
               var6 = new StringBuilder();
               var6.append("don't show spam button for ");
               var6.append(var1);
               FileLog.d(var6.toString());
            }

            var6 = new StringBuilder();
            var6.append("spam3_");
            var6.append(var1);
            var5.putInt(var6.toString(), 1);
            var5.commit();
         } else {
            if (BuildVars.LOGS_ENABLED) {
               var6 = new StringBuilder();
               var6.append("show spam button for ");
               var6.append(var1);
               FileLog.d(var6.toString());
            }

            var6 = new StringBuilder();
            var6.append("spam3_");
            var6.append(var1);
            var5.putInt(var6.toString(), 2);
            var5.commit();
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.peerSettingsDidLoad, var1);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 2);
      UserConfig.getInstance(this.currentAccount).saveConfig(true);
   }

   // $FF: synthetic method
   public void lambda$null$33$MessagesController(ArrayList var1) {
      this.getNewDeleteTask(var1, this.currentDeletingTaskChannelId);
      this.currentDeletingTaskTime = 0;
      this.currentDeletingTaskMids = null;
   }

   // $FF: synthetic method
   public void lambda$null$35$MessagesController() {
      this.checkDeletingTask(true);
   }

   // $FF: synthetic method
   public void lambda$null$40$MessagesController(int var1) {
      this.loadFullChat(var1, 0, true);
   }

   // $FF: synthetic method
   public void lambda$null$41$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_channels_editBanned var3, boolean var4) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void lambda$null$43$MessagesController(int var1) {
      this.loadFullChat(var1, 0, true);
   }

   // $FF: synthetic method
   public void lambda$null$44$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_messages_editChatDefaultBannedRights var3, boolean var4) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void lambda$null$46$MessagesController(int var1) {
      this.loadFullChat(var1, 0, true);
   }

   // $FF: synthetic method
   public void lambda$null$47$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_channels_editAdmin var3, boolean var4) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void lambda$null$49$MessagesController(int var1) {
      this.loadFullChat(var1, 0, true);
   }

   // $FF: synthetic method
   public void lambda$null$5$MessagesController(TLRPC.TL_wallPaper var1, TLRPC.TL_wallPaperSettings var2, File var3) {
      if (this.uploadingWallpaper != null && var1 != null) {
         var1.settings = var2;
         var1.flags |= 4;
         Editor var6 = getGlobalMainSettings().edit();
         var6.putLong("selectedBackground2", var1.id);
         var6.commit();
         ArrayList var7 = new ArrayList();
         var7.add(var1);
         MessagesStorage.getInstance(this.currentAccount).putWallpapers(var7, 2);
         TLRPC.PhotoSize var8 = FileLoader.getClosestPhotoSizeWithSize(var1.document.thumbs, 320);
         if (var8 != null) {
            StringBuilder var4 = new StringBuilder();
            var4.append(var8.location.volume_id);
            var4.append("_");
            var4.append(var8.location.local_id);
            var4.append("@100_100");
            String var10 = var4.toString();
            StringBuilder var5 = new StringBuilder();
            var5.append(Utilities.MD5(var3.getAbsolutePath()));
            var5.append("@100_100");
            String var9 = var5.toString();
            ImageLoader.getInstance().replaceImageInCache(var9, var10, ImageLocation.getForDocument(var8, var1.document), false);
         }

         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.wallpapersNeedReload, var1.id);
      }

   }

   // $FF: synthetic method
   public void lambda$null$50$MessagesController(TLRPC.TL_error var1, BaseFragment var2, TLRPC.TL_messages_editChatAdmin var3) {
      AlertsCreator.processError(this.currentAccount, var1, var2, var3, false);
   }

   // $FF: synthetic method
   public void lambda$null$56$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 1535);
      UserConfig.getInstance(this.currentAccount).saveConfig(true);
   }

   // $FF: synthetic method
   public void lambda$null$66$MessagesController(long var1) {
      NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(var1);
   }

   // $FF: synthetic method
   public void lambda$null$68$MessagesController(long var1) {
      this.deletedHistory.remove(var1);
   }

   // $FF: synthetic method
   public void lambda$null$73$MessagesController(TLRPC.TL_error var1, TLObject var2, Integer var3) {
      if (var1 == null) {
         TLRPC.TL_channels_channelParticipants var4 = (TLRPC.TL_channels_channelParticipants)var2;
         this.putUsers(var4.users, false);
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var4.users, (ArrayList)null, true, true);
         MessagesStorage.getInstance(this.currentAccount).updateChannelUsers(var3, var4.participants);
         this.loadedFullParticipants.add(var3);
      }

      this.loadingFullParticipants.remove(var3);
   }

   // $FF: synthetic method
   public void lambda$null$79$MessagesController(SparseArray var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didUpdatedMessagesViews, var1);
   }

   // $FF: synthetic method
   public void lambda$null$81$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         this.processUpdates((TLRPC.Updates)var1, false);
      }

   }

   // $FF: synthetic method
   public void lambda$null$84$MessagesController(int var1, TLRPC.TL_chatOnlines var2) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatOnlineCountDidLoad, var1, var2.onlines);
   }

   // $FF: synthetic method
   public void lambda$null$87$MessagesController(TLRPC.TL_help_termsOfServiceUpdate var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needShowAlert, 4, var1.terms_of_service);
   }

   // $FF: synthetic method
   public void lambda$null$90$MessagesController(TLRPC.TL_help_proxyDataPromo var1, TLRPC.TL_messages_peerDialogs var2, long var3) {
      ArrayList var5 = var1.users;
      Integer var6 = 0;
      this.putUsers(var5, false);
      this.putChats(var1.chats, false);
      this.putUsers(var2.users, false);
      this.putChats(var2.chats, false);
      this.proxyDialog = (TLRPC.Dialog)var2.dialogs.get(0);
      TLRPC.Dialog var8 = this.proxyDialog;
      var8.id = var3;
      var8.folder_id = 0;
      if (DialogObject.isChannel(var8)) {
         SparseIntArray var13 = this.channelsPts;
         var8 = this.proxyDialog;
         var13.put(-((int)var8.id), var8.pts);
      }

      Integer var14 = (Integer)this.dialogs_read_inbox_max.get(this.proxyDialog.id);
      Integer var9 = var14;
      if (var14 == null) {
         var9 = var6;
      }

      this.dialogs_read_inbox_max.put(this.proxyDialog.id, Math.max(var9, this.proxyDialog.read_inbox_max_id));
      var14 = (Integer)this.dialogs_read_outbox_max.get(this.proxyDialog.id);
      var9 = var14;
      if (var14 == null) {
         var9 = var6;
      }

      this.dialogs_read_outbox_max.put(this.proxyDialog.id, Math.max(var9, this.proxyDialog.read_outbox_max_id));
      this.dialogs_dict.put(var3, this.proxyDialog);
      if (!var2.messages.isEmpty()) {
         SparseArray var15 = new SparseArray();
         SparseArray var11 = new SparseArray();

         int var7;
         for(var7 = 0; var7 < var2.users.size(); ++var7) {
            TLRPC.User var16 = (TLRPC.User)var2.users.get(var7);
            var15.put(var16.id, var16);
         }

         for(var7 = 0; var7 < var2.chats.size(); ++var7) {
            TLRPC.Chat var17 = (TLRPC.Chat)var2.chats.get(var7);
            var11.put(var17.id, var17);
         }

         MessageObject var12 = new MessageObject(this.currentAccount, (TLRPC.Message)var2.messages.get(0), var15, var11, false);
         this.dialogMessage.put(var3, var12);
         TLRPC.Dialog var10 = this.proxyDialog;
         if (var10.last_message_date == 0) {
            var10.last_message_date = var12.messageOwner.date;
         }
      }

      this.sortDialogs((SparseArray)null);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
   }

   // $FF: synthetic method
   public void lambda$null$91$MessagesController() {
      TLRPC.Dialog var1 = this.proxyDialog;
      if (var1 != null) {
         int var2 = (int)var1.id;
         if (var2 < 0) {
            TLRPC.Chat var3 = this.getChat(-var2);
            if (ChatObject.isNotInChat(var3) || var3.restricted) {
               this.removeDialog(this.proxyDialog);
            }
         } else {
            this.removeDialog(var1);
         }

         this.proxyDialog = null;
         this.sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      }

   }

   // $FF: synthetic method
   public void lambda$null$92$MessagesController(TLRPC.TL_help_proxyDataPromo var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      if (this.checkingProxyInfoRequestId != 0) {
         this.checkingProxyInfoRequestId = 0;
         TLRPC.TL_messages_peerDialogs var6 = (TLRPC.TL_messages_peerDialogs)var4;
         if (var6 != null && !var6.dialogs.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var1.users, var1.chats, true, true);
            TLRPC.TL_messages_dialogs var7 = new TLRPC.TL_messages_dialogs();
            var7.chats = var6.chats;
            var7.users = var6.users;
            var7.dialogs = var6.dialogs;
            var7.messages = var6.messages;
            MessagesStorage.getInstance(this.currentAccount).putDialogs(var7, 2);
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$0hzwDCgQaQ7_pO9ojVbt_LU0UiE(this, var1, var6, var2));
         } else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$__dEtAqUXm_ipyTn_k8IK_xJUDw(this));
         }

         this.checkingProxyInfo = false;
      }
   }

   // $FF: synthetic method
   public void lambda$null$93$MessagesController(long var1, TLRPC.TL_help_proxyDataPromo var3) {
      this.proxyDialog = (TLRPC.Dialog)this.dialogs_dict.get(var1);
      TLRPC.Dialog var4 = this.proxyDialog;
      byte var5 = 0;
      if (var4 != null) {
         this.checkingProxyInfo = false;
         this.sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
      } else {
         SparseArray var6 = new SparseArray();
         SparseArray var7 = new SparseArray();
         int var8 = 0;

         while(true) {
            int var9 = var5;
            if (var8 >= var3.users.size()) {
               while(var9 < var3.chats.size()) {
                  TLRPC.Chat var13 = (TLRPC.Chat)var3.chats.get(var9);
                  var7.put(var13.id, var13);
                  ++var9;
               }

               TLRPC.TL_messages_getPeerDialogs var14 = new TLRPC.TL_messages_getPeerDialogs();
               TLRPC.TL_inputDialogPeer var10 = new TLRPC.TL_inputDialogPeer();
               TLRPC.Peer var11 = var3.peer;
               if (var11.user_id != 0) {
                  var10.peer = new TLRPC.TL_inputPeerUser();
                  TLRPC.InputPeer var16 = var10.peer;
                  var8 = var3.peer.user_id;
                  var16.user_id = var8;
                  TLRPC.User var17 = (TLRPC.User)var6.get(var8);
                  if (var17 != null) {
                     var10.peer.access_hash = var17.access_hash;
                  }
               } else {
                  TLRPC.InputPeer var15;
                  TLRPC.Chat var18;
                  if (var11.chat_id != 0) {
                     var10.peer = new TLRPC.TL_inputPeerChat();
                     var15 = var10.peer;
                     var8 = var3.peer.chat_id;
                     var15.chat_id = var8;
                     var18 = (TLRPC.Chat)var7.get(var8);
                     if (var18 != null) {
                        var10.peer.access_hash = var18.access_hash;
                     }
                  } else {
                     var10.peer = new TLRPC.TL_inputPeerChannel();
                     var15 = var10.peer;
                     var8 = var3.peer.channel_id;
                     var15.channel_id = var8;
                     var18 = (TLRPC.Chat)var7.get(var8);
                     if (var18 != null) {
                        var10.peer.access_hash = var18.access_hash;
                     }
                  }
               }

               var14.peers.add(var10);
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var14, new _$$Lambda$MessagesController$jGzHg4Vpn20r2Dvt16XQexkQnM8(this, var3, var1));
               break;
            }

            TLRPC.User var12 = (TLRPC.User)var3.users.get(var8);
            var6.put(var12.id, var12);
            ++var8;
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$94$MessagesController() {
      TLRPC.Dialog var1 = this.proxyDialog;
      if (var1 != null) {
         int var2 = (int)var1.id;
         if (var2 < 0) {
            TLRPC.Chat var3 = this.getChat(-var2);
            if (ChatObject.isNotInChat(var3) || var3.restricted) {
               this.removeDialog(this.proxyDialog);
            }
         } else {
            this.removeDialog(var1);
         }

         this.proxyDialog = null;
         this.sortDialogs((SparseArray)null);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      }

   }

   // $FF: synthetic method
   public void lambda$null$98$MessagesController(int var1, long var2) {
      LongSparseArray var4 = (LongSparseArray)this.sendingTypings.get(var1);
      if (var4 != null) {
         var4.remove(var2);
      }

   }

   // $FF: synthetic method
   public void lambda$onFolderEmpty$111$MessagesController(int var1) {
      this.removeFolder(var1);
   }

   // $FF: synthetic method
   public void lambda$openByUserName$260$MessagesController(AlertDialog[] var1, BaseFragment var2, int var3, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$PtlR0pP29l9GCXkUdVv_AI_Hdug(this, var1, var2, var5, var4, var3));
   }

   // $FF: synthetic method
   public void lambda$openByUserName$262$MessagesController(AlertDialog[] var1, int var2, BaseFragment var3) {
      if (var1[0] != null) {
         var1[0].setOnCancelListener(new _$$Lambda$MessagesController$TMnAL0Zt__AXB8RK3P6tM9ad784(this, var2));
         var3.showDialog(var1[0]);
      }
   }

   // $FF: synthetic method
   public void lambda$performLogout$186$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      ConnectionsManager.getInstance(this.currentAccount).cleanup(false);
   }

   // $FF: synthetic method
   public void lambda$pinDialog$219$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$pinMessage$63$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         this.processUpdates((TLRPC.Updates)var1, false);
      }

   }

   // $FF: synthetic method
   public void lambda$processChatInfo$75$MessagesController(ArrayList var1, boolean var2, TLRPC.ChatFull var3, boolean var4, MessageObject var5) {
      this.putUsers(var1, var2);
      if (var3.stickerset != null) {
         DataQuery.getInstance(this.currentAccount).getGroupStickerSetById(var3.stickerset);
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoad, var3, 0, var4, var5);
   }

   // $FF: synthetic method
   public void lambda$processDialogsUpdate$138$MessagesController(TLRPC.messages_Dialogs var1) {
      LongSparseArray var2 = new LongSparseArray();
      LongSparseArray var3 = new LongSparseArray();
      SparseArray var4 = new SparseArray(var1.users.size());
      SparseArray var5 = new SparseArray(var1.chats.size());
      LongSparseArray var6 = new LongSparseArray();
      byte var7 = 0;
      Integer var8 = 0;

      int var9;
      for(var9 = 0; var9 < var1.users.size(); ++var9) {
         TLRPC.User var10 = (TLRPC.User)var1.users.get(var9);
         var4.put(var10.id, var10);
      }

      for(var9 = 0; var9 < var1.chats.size(); ++var9) {
         TLRPC.Chat var18 = (TLRPC.Chat)var1.chats.get(var9);
         var5.put(var18.id, var18);
      }

      var9 = 0;

      while(true) {
         int var11 = var7;
         long var12;
         if (var9 >= var1.messages.size()) {
            for(; var11 < var1.dialogs.size(); ++var11) {
               TLRPC.Dialog var23 = (TLRPC.Dialog)var1.dialogs.get(var11);
               DialogObject.initDialog(var23);
               var12 = this.proxyDialogId;
               if (var12 == 0L || var12 != var23.id) {
                  TLRPC.Chat var15;
                  if (DialogObject.isChannel(var23)) {
                     var15 = (TLRPC.Chat)var5.get(-((int)var23.id));
                     if (var15 != null && var15.left) {
                        continue;
                     }
                  } else {
                     var12 = var23.id;
                     if ((int)var12 < 0) {
                        var15 = (TLRPC.Chat)var5.get(-((int)var12));
                        if (var15 != null && var15.migrated_to != null) {
                           continue;
                        }
                     }
                  }
               }

               if (var23.last_message_date == 0) {
                  MessageObject var16 = (MessageObject)var3.get(var23.id);
                  if (var16 != null) {
                     var23.last_message_date = var16.messageOwner.date;
                  }
               }

               var2.put(var23.id, var23);
               var6.put(var23.id, var23.unread_count);
               Integer var21 = (Integer)this.dialogs_read_inbox_max.get(var23.id);
               Integer var17 = var21;
               if (var21 == null) {
                  var17 = var8;
               }

               this.dialogs_read_inbox_max.put(var23.id, Math.max(var17, var23.read_inbox_max_id));
               var21 = (Integer)this.dialogs_read_outbox_max.get(var23.id);
               var17 = var21;
               if (var21 == null) {
                  var17 = var8;
               }

               this.dialogs_read_outbox_max.put(var23.id, Math.max(var17, var23.read_outbox_max_id));
            }

            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$oWzxev3PBWOpBqcrFUkL5QjXPQo(this, var1, var2, var3, var6));
            return;
         }

         label102: {
            TLRPC.Message var19 = (TLRPC.Message)var1.messages.get(var9);
            var12 = this.proxyDialogId;
            if (var12 == 0L || var12 != var19.dialog_id) {
               TLRPC.Peer var14 = var19.to_id;
               var11 = var14.channel_id;
               TLRPC.Chat var22;
               if (var11 != 0) {
                  var22 = (TLRPC.Chat)var5.get(var11);
                  if (var22 != null && var22.left) {
                     break label102;
                  }
               } else {
                  var11 = var14.chat_id;
                  if (var11 != 0) {
                     var22 = (TLRPC.Chat)var5.get(var11);
                     if (var22 != null && var22.migrated_to != null) {
                        break label102;
                     }
                  }
               }
            }

            MessageObject var20 = new MessageObject(this.currentAccount, var19, var4, var5, false);
            var3.put(var20.getDialogId(), var20);
         }

         ++var9;
      }
   }

   // $FF: synthetic method
   public void lambda$processDialogsUpdateRead$133$MessagesController(LongSparseArray var1, LongSparseArray var2) {
      int var3;
      long var4;
      TLRPC.Dialog var6;
      if (var1 != null) {
         for(var3 = 0; var3 < var1.size(); ++var3) {
            var4 = var1.keyAt(var3);
            var6 = (TLRPC.Dialog)this.dialogs_dict.get(var4);
            if (var6 != null) {
               int var7 = var6.unread_count;
               var6.unread_count = (Integer)var1.valueAt(var3);
               if (var7 != 0 && var6.unread_count == 0 && !this.isDialogMuted(var4)) {
                  --this.unreadUnmutedDialogs;
               } else if (var7 == 0 && !var6.unread_mark && var6.unread_count != 0 && !this.isDialogMuted(var4)) {
                  ++this.unreadUnmutedDialogs;
               }
            }
         }
      }

      if (var2 != null) {
         for(var3 = 0; var3 < var2.size(); ++var3) {
            var4 = var2.keyAt(var3);
            var6 = (TLRPC.Dialog)this.dialogs_dict.get(var4);
            if (var6 != null) {
               var6.unread_mentions_count = (Integer)var2.valueAt(var3);
               if (this.createdDialogMainThreadIds.contains(var6.id)) {
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateMentionsCount, var6.id, var6.unread_mentions_count);
               }
            }
         }
      }

      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 256);
      if (var1 != null) {
         NotificationsController.getInstance(this.currentAccount).processDialogsUpdateRead(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$processLoadedBlockedUsers$55$MessagesController(ArrayList var1, boolean var2, SparseIntArray var3) {
      if (var1 != null) {
         this.putUsers(var1, var2);
      }

      this.loadingBlockedUsers = false;
      if (var3.size() == 0 && var2 && !UserConfig.getInstance(this.currentAccount).blockedUsersLoaded) {
         this.getBlockedUsers(false);
      } else {
         if (!var2) {
            UserConfig.getInstance(this.currentAccount).blockedUsersLoaded = true;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
         }

         this.blockedUsers = var3;
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad);
      }
   }

   // $FF: synthetic method
   public void lambda$processLoadedChannelAdmins$14$MessagesController(int var1, ArrayList var2, boolean var3) {
      this.loadingChannelAdmins.delete(var1);
      this.channelAdmins.put(var1, var2);
      if (var3) {
         this.loadChannelAdmins(var1, false);
      }

   }

   // $FF: synthetic method
   public void lambda$processLoadedDeleteTask$36$MessagesController(ArrayList var1, int var2) {
      this.gettingNewDeleteTask = false;
      if (var1 != null) {
         this.currentDeletingTaskTime = var2;
         this.currentDeletingTaskMids = var1;
         if (this.currentDeleteTaskRunnable != null) {
            Utilities.stageQueue.cancelRunnable(this.currentDeleteTaskRunnable);
            this.currentDeleteTaskRunnable = null;
         }

         if (!this.checkDeletingTask(false)) {
            this.currentDeleteTaskRunnable = new _$$Lambda$MessagesController$yKVMzLBuY9JjdZe9ImZ_PuELipE(this);
            var2 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            Utilities.stageQueue.postRunnable(this.currentDeleteTaskRunnable, (long)Math.abs(var2 - this.currentDeletingTaskTime) * 1000L);
         }
      } else {
         this.currentDeletingTaskTime = 0;
         this.currentDeletingTaskMids = null;
      }

   }

   // $FF: synthetic method
   public void lambda$processLoadedDialogs$129$MessagesController(int var1, int var2, TLRPC.messages_Dialogs var3, boolean var4, int var5, ArrayList var6, int var7, boolean var8, boolean var9) {
      boolean var10 = this.firstGettingTask;
      Integer var11 = 0;
      if (!var10) {
         this.getNewDeleteTask((ArrayList)null, 0);
         this.firstGettingTask = true;
      }

      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var12 = new StringBuilder();
         var12.append("loaded folderId ");
         var12.append(var1);
         var12.append(" loadType ");
         var12.append(var2);
         var12.append(" count ");
         var12.append(var3.dialogs.size());
         FileLog.d(var12.toString());
      }

      int[] var13 = UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(var1);
      if (var2 == this.DIALOGS_LOAD_TYPE_CACHE && var3.dialogs.size() == 0) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$vf0a3gbyExIJKu8Fv0SfXfbDaBc(this, var3, var1, var4, var13, var5));
      } else {
         LongSparseArray var14 = new LongSparseArray();
         LongSparseArray var15 = new LongSparseArray();
         SparseArray var16 = new SparseArray();
         SparseArray var17 = new SparseArray();

         int var18;
         TLRPC.User var31;
         for(var18 = 0; var18 < var3.users.size(); ++var18) {
            var31 = (TLRPC.User)var3.users.get(var18);
            var16.put(var31.id, var31);
         }

         TLRPC.Chat var33;
         for(var18 = 0; var18 < var3.chats.size(); ++var18) {
            var33 = (TLRPC.Chat)var3.chats.get(var18);
            var17.put(var33.id, var33);
         }

         int var19;
         SparseArray var34;
         if (var6 != null) {
            var34 = new SparseArray();
            var19 = var6.size();

            for(var18 = 0; var18 < var19; ++var18) {
               TLRPC.EncryptedChat var20 = (TLRPC.EncryptedChat)var6.get(var18);
               var34.put(var20.id, var20);
            }
         } else {
            var34 = null;
         }

         if (var2 == this.DIALOGS_LOAD_TYPE_CACHE) {
            this.nextDialogsCacheOffset.put(var1, var7 + var5);
         }

         var18 = 0;

         TLRPC.Message var21;
         long var23;
         TLRPC.Message var39;
         TLRPC.Chat var42;
         MessageObject var45;
         for(var21 = null; var18 < var3.messages.size(); var21 = var39) {
            TLRPC.Message var22;
            label266: {
               var22 = (TLRPC.Message)var3.messages.get(var18);
               if (var21 != null) {
                  var39 = var21;
                  if (var22.date >= var21.date) {
                     break label266;
                  }
               }

               var39 = var22;
            }

            label279: {
               TLRPC.Peer var41 = var22.to_id;
               var19 = var41.channel_id;
               if (var19 != 0) {
                  var42 = (TLRPC.Chat)var17.get(var19);
                  if (var42 != null && var42.left) {
                     var23 = this.proxyDialogId;
                     if (var23 == 0L || var23 != (long)(-var42.id)) {
                        break label279;
                     }
                  }

                  if (var42 != null && var42.megagroup) {
                     var22.flags |= Integer.MIN_VALUE;
                  }
               } else {
                  var19 = var41.chat_id;
                  if (var19 != 0) {
                     var42 = (TLRPC.Chat)var17.get(var19);
                     if (var42 != null && var42.migrated_to != null) {
                        break label279;
                     }
                  }
               }

               var45 = new MessageObject(this.currentAccount, var22, var16, var17, false);
               var15.put(var45.getDialogId(), var45);
            }

            ++var18;
         }

         SparseArray var40 = var34;
         int var27;
         if (!var8 && !var9 && var13[0] != -1 && var2 == 0) {
            int var25 = UserConfig.getInstance(this.currentAccount).getTotalDialogsCount(var1);
            int var26;
            int var28;
            if (var21 != null && var21.id != var13[0]) {
               label252: {
                  var25 += var3.dialogs.size();
                  var18 = var21.id;
                  var26 = var21.date;
                  TLRPC.Peer var36 = var21.to_id;
                  var27 = var36.channel_id;
                  if (var27 != 0) {
                     var19 = 0;

                     while(true) {
                        if (var19 >= var3.chats.size()) {
                           var23 = 0L;
                           break;
                        }

                        var33 = (TLRPC.Chat)var3.chats.get(var19);
                        if (var33.id == var27) {
                           var23 = var33.access_hash;
                           break;
                        }

                        ++var19;
                     }

                     var28 = 0;
                  } else {
                     var28 = var36.chat_id;
                     if (var28 == 0) {
                        var27 = var36.user_id;
                        if (var27 != 0) {
                           var19 = 0;

                           while(true) {
                              if (var19 >= var3.users.size()) {
                                 var23 = 0L;
                                 break;
                              }

                              var31 = (TLRPC.User)var3.users.get(var19);
                              if (var31.id == var27) {
                                 var23 = var31.access_hash;
                                 break;
                              }

                              ++var19;
                           }

                           var19 = var27;
                        } else {
                           var23 = 0L;
                           var19 = 0;
                        }

                        var28 = 0;
                        var27 = 0;
                        break label252;
                     }

                     var19 = 0;

                     while(true) {
                        if (var19 >= var3.chats.size()) {
                           var23 = 0L;
                           break;
                        }

                        var33 = (TLRPC.Chat)var3.chats.get(var19);
                        if (var33.id == var28) {
                           var23 = var33.access_hash;
                           break;
                        }

                        ++var19;
                     }

                     var27 = 0;
                  }

                  var19 = 0;
               }
            } else {
               var23 = 0L;
               var18 = Integer.MAX_VALUE;
               var26 = 0;
               var19 = 0;
               var28 = 0;
               var27 = 0;
            }

            UserConfig.getInstance(this.currentAccount).setDialogsLoadOffset(var1, var18, var26, var19, var28, var27, var23);
            UserConfig.getInstance(this.currentAccount).setTotalDialogsCount(var1, var25);
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
         }

         var34 = var16;
         ArrayList var44 = new ArrayList();

         Integer var46;
         for(var18 = 0; var18 < var3.dialogs.size(); ++var18) {
            TLRPC.Dialog var35 = (TLRPC.Dialog)var3.dialogs.get(var18);
            DialogObject.initDialog(var35);
            var23 = var35.id;
            if (var23 != 0L) {
               var27 = (int)var23;
               var19 = (int)(var23 >> 32);
               if (var27 != 0 || var40 == null || var40.get(var19) != null) {
                  var23 = this.proxyDialogId;
                  if (var23 != 0L && var23 == var35.id) {
                     this.proxyDialog = var35;
                  }

                  if (var35.last_message_date == 0) {
                     var45 = (MessageObject)var15.get(var35.id);
                     if (var45 != null) {
                        var35.last_message_date = var45.messageOwner.date;
                     }
                  }

                  if (DialogObject.isChannel(var35)) {
                     var42 = (TLRPC.Chat)var17.get(-((int)var35.id));
                     if (var42 != null) {
                        var10 = var42.megagroup;
                        var4 = var10;
                        if (var42.left) {
                           var23 = this.proxyDialogId;
                           if (var23 == 0L) {
                              continue;
                           }

                           var4 = var10;
                           if (var23 != var35.id) {
                              continue;
                           }
                        }
                     } else {
                        var4 = true;
                     }

                     this.channelsPts.put(-((int)var35.id), var35.pts);
                  } else {
                     var23 = var35.id;
                     if ((int)var23 < 0) {
                        var42 = (TLRPC.Chat)var17.get(-((int)var23));
                        if (var42 != null && var42.migrated_to != null) {
                           continue;
                        }
                     }

                     var4 = true;
                  }

                  var14.put(var35.id, var35);
                  if (var4 && var2 == this.DIALOGS_LOAD_TYPE_CACHE && (var35.read_outbox_max_id == 0 || var35.read_inbox_max_id == 0) && var35.top_message != 0) {
                     var44.add(var35);
                  }

                  Integer var37 = (Integer)this.dialogs_read_inbox_max.get(var35.id);
                  var46 = var37;
                  if (var37 == null) {
                     var46 = var11;
                  }

                  this.dialogs_read_inbox_max.put(var35.id, Math.max(var46, var35.read_inbox_max_id));
                  var37 = (Integer)this.dialogs_read_outbox_max.get(var35.id);
                  var46 = var37;
                  if (var37 == null) {
                     var46 = var11;
                  }

                  this.dialogs_read_outbox_max.put(var35.id, Math.max(var46, var35.read_outbox_max_id));
               }
            }
         }

         if (var2 != this.DIALOGS_LOAD_TYPE_CACHE) {
            ImageLoader.saveMessagesThumbs(var3.messages);

            for(var18 = 0; var18 < var3.messages.size(); ++var18) {
               TLRPC.Message var38 = (TLRPC.Message)var3.messages.get(var18);
               TLRPC.MessageAction var29 = var38.action;
               if (var29 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                  TLRPC.User var30 = (TLRPC.User)var34.get(var29.user_id);
                  if (var30 != null && var30.bot) {
                     var38.reply_markup = new TLRPC.TL_replyKeyboardHide();
                     var38.flags |= 64;
                  }
               }

               var29 = var38.action;
               if (!(var29 instanceof TLRPC.TL_messageActionChatMigrateTo) && !(var29 instanceof TLRPC.TL_messageActionChannelCreate)) {
                  ConcurrentHashMap var32;
                  if (var38.out) {
                     var32 = this.dialogs_read_outbox_max;
                  } else {
                     var32 = this.dialogs_read_inbox_max;
                  }

                  var46 = (Integer)var32.get(var38.dialog_id);
                  Integer var43 = var46;
                  if (var46 == null) {
                     var43 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var38.out, var38.dialog_id);
                     var32.put(var38.dialog_id, var43);
                  }

                  if (var43 < var38.id) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  var38.unread = var4;
               } else {
                  var38.unread = false;
                  var38.media_unread = false;
               }
            }

            MessagesStorage.getInstance(this.currentAccount).putDialogs(var3, 0);
         }

         if (var2 == this.DIALOGS_LOAD_TYPE_CHANNEL) {
            var33 = (TLRPC.Chat)var3.chats.get(0);
            this.getChannelDifference(var33.id);
            this.checkChannelInviter(var33.id);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$OMZI4TzcbS75iT0wAGlu_LDNynw(this, var2, var3, var6, var9, var1, var14, var15, var17, var5, var8, var7, var44));
      }
   }

   // $FF: synthetic method
   public void lambda$processLoadedMessages$108$MessagesController(TLRPC.messages_Messages var1, long var2, boolean var4, int var5, int var6, boolean var7, int var8, int var9, int var10, int var11, int var12, boolean var13, int var14, int var15, int var16, int var17, boolean var18) {
      TLRPC.messages_Messages var19 = var1;
      int var20;
      boolean var21;
      int var22;
      TLRPC.Chat var23;
      boolean var25;
      if (var1 instanceof TLRPC.TL_messages_channelMessages) {
         var20 = -((int)var2);
         if (this.channelsPts.get(var20) == 0 && MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(var20) == 0) {
            this.channelsPts.put(var20, var1.pts);
            if (this.needShortPollChannels.indexOfKey(var20) >= 0 && this.shortPollChannels.indexOfKey(var20) < 0) {
               this.getChannelDifference(var20, 2, 0L, (TLRPC.InputChannel)null);
            } else {
               this.getChannelDifference(var20);
            }

            var21 = true;
         } else {
            var21 = false;
         }

         var22 = 0;

         while(true) {
            if (var22 >= var19.chats.size()) {
               var25 = var21;
               var21 = false;
               break;
            }

            var23 = (TLRPC.Chat)var19.chats.get(var22);
            if (var23.id == var20) {
               boolean var24 = var23.megagroup;
               var25 = var21;
               var21 = var24;
               break;
            }

            ++var22;
         }
      } else {
         var21 = false;
         var25 = false;
      }

      var22 = (int)var2;
      var20 = (int)(var2 >> 32);
      if (!var4) {
         ImageLoader.saveMessagesThumbs(var19.messages);
      }

      if (var20 != 1 && var22 != 0 && var4 && var19.messages.size() == 0) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$L2__57wpUETyhq6XVWbkuE1LFZE(this, var2, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17));
      } else {
         SparseArray var26 = new SparseArray();
         SparseArray var27 = new SparseArray();

         for(var10 = 0; var10 < var19.users.size(); ++var10) {
            TLRPC.User var35 = (TLRPC.User)var19.users.get(var10);
            var26.put(var35.id, var35);
         }

         for(var10 = 0; var10 < var19.chats.size(); ++var10) {
            var23 = (TLRPC.Chat)var19.chats.get(var10);
            var27.put(var23.id, var23);
         }

         var22 = var19.messages.size();
         if (!var4) {
            Integer var29 = (Integer)this.dialogs_read_inbox_max.get(var2);
            Integer var36 = var29;
            if (var29 == null) {
               var36 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, var2);
               this.dialogs_read_inbox_max.put(var2, var36);
            }

            Integer var30 = (Integer)this.dialogs_read_outbox_max.get(var2);
            var29 = var30;
            if (var30 == null) {
               var29 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var2);
               this.dialogs_read_outbox_max.put(var2, var29);
            }

            for(var10 = 0; var10 < var22; ++var10) {
               TLRPC.Message var31 = (TLRPC.Message)var19.messages.get(var10);
               if (var21) {
                  var31.flags |= Integer.MIN_VALUE;
               }

               TLRPC.MessageAction var42 = var31.action;
               if (var42 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                  TLRPC.User var43 = (TLRPC.User)var26.get(var42.user_id);
                  if (var43 != null && var43.bot) {
                     var31.reply_markup = new TLRPC.TL_replyKeyboardHide();
                     var31.flags |= 64;
                  }
               }

               var42 = var31.action;
               if (!(var42 instanceof TLRPC.TL_messageActionChatMigrateTo) && !(var42 instanceof TLRPC.TL_messageActionChannelCreate)) {
                  if (var31.out) {
                     var30 = var29;
                  } else {
                     var30 = var36;
                  }

                  if (var30 < var31.id) {
                     var13 = true;
                  } else {
                     var13 = false;
                  }

                  var31.unread = var13;
               } else {
                  var31.unread = false;
                  var31.media_unread = false;
               }
            }

            MessagesStorage.getInstance(this.currentAccount).putMessages(var1, var2, var6, var9, var25);
         }

         MessagesController var41 = this;
         ArrayList var34 = new ArrayList();
         ArrayList var28 = new ArrayList();
         HashMap var44 = new HashMap();

         for(var10 = 0; var10 < var22; ++var10) {
            TLRPC.Message var32 = (TLRPC.Message)var1.messages.get(var10);
            var32.dialog_id = var2;
            MessageObject var33 = new MessageObject(var41.currentAccount, var32, var26, var27, true);
            var34.add(var33);
            if (var4) {
               TLRPC.MessageMedia var37;
               if (var32.legacy && var32.layer < 100) {
                  var28.add(var32.id);
               } else {
                  var37 = var32.media;
                  if (var37 instanceof TLRPC.TL_messageMediaUnsupported) {
                     byte[] var38 = var37.bytes;
                     if (var38 != null && (var38.length == 0 || var38.length == 1 && var38[0] < 100)) {
                        var28.add(var32.id);
                     }
                  }
               }

               var37 = var32.media;
               if (var37 instanceof TLRPC.TL_messageMediaWebPage) {
                  TLRPC.WebPage var39 = var37.webpage;
                  if (var39 instanceof TLRPC.TL_webPagePending && var39.date <= ConnectionsManager.getInstance(var41.currentAccount).getCurrentTime()) {
                     var28.add(var32.id);
                  } else {
                     var39 = var32.media.webpage;
                     if (var39 instanceof TLRPC.TL_webPageUrlPending) {
                        ArrayList var45 = (ArrayList)var44.get(var39.url);
                        ArrayList var40 = var45;
                        if (var45 == null) {
                           var40 = new ArrayList();
                           var44.put(var32.media.webpage.url, var40);
                        }

                        var40.add(var33);
                     }
                  }
               }
            }
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Z4Bd5ZrkcBpI903_24hDafvqXGk(this, var1, var4, var7, var6, var8, var2, var5, var34, var12, var15, var16, var18, var11, var14, var9, var17, var28, var44));
      }
   }

   // $FF: synthetic method
   public void lambda$processLoadedUserPhotos$59$MessagesController(TLRPC.photos_Photos var1, boolean var2, int var3, int var4, int var5) {
      this.putUsers(var1.users, var2);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogPhotosLoaded, var3, var4, var2, var5, var1.photos);
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$238$MessagesController(ArrayList var1, ArrayList var2) {
      this.putUsers(var1, false);
      this.putChats(var2, false);
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$239$MessagesController(ArrayList var1, ArrayList var2) {
      this.putUsers(var1, false);
      this.putChats(var2, false);
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$241$MessagesController(TLRPC.TL_updateUserBlocked var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$IE61P_jfCib0uHF9uB4VvmEoPuE(this, var1));
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$242$MessagesController(TLRPC.TL_updateServiceNotification var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needShowAlert, 2, var1.message, var1.type);
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$243$MessagesController(TLRPC.TL_updateLangPack var1) {
      LocaleController.getInstance().saveRemoteLocaleStringsForCurrentLocale(var1.difference, this.currentAccount);
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$245$MessagesController(ArrayList var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$C4rRzL3XbhcTq3487s_diR_40t4(this, var1));
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$250$MessagesController(int var1, ArrayList var2, LongSparseArray var3, LongSparseArray var4, LongSparseArray var5, boolean var6, ArrayList var7, ArrayList var8, SparseArray var9) {
      ArrayList var10;
      int var12;
      int var13;
      boolean var15;
      long var19;
      int var22;
      int var23;
      int var24;
      int var40;
      if (var2 != null) {
         var10 = new ArrayList();
         ArrayList var11 = new ArrayList();
         var12 = var2.size();
         var13 = 0;
         Editor var14 = null;

         for(var15 = false; var13 < var12; var1 = var23) {
            Editor var80;
            boolean var87;
            label564: {
               label563: {
                  TLRPC.Update var16 = (TLRPC.Update)var2.get(var13);
                  if (var16 instanceof TLRPC.TL_updatePrivacy) {
                     TLRPC.TL_updatePrivacy var43 = (TLRPC.TL_updatePrivacy)var16;
                     TLRPC.PrivacyKey var17 = var43.key;
                     if (var17 instanceof TLRPC.TL_privacyKeyStatusTimestamp) {
                        ContactsController.getInstance(this.currentAccount).setPrivacyRules(var43.rules, 0);
                     } else if (var17 instanceof TLRPC.TL_privacyKeyChatInvite) {
                        ContactsController.getInstance(this.currentAccount).setPrivacyRules(var43.rules, 1);
                     } else if (var17 instanceof TLRPC.TL_privacyKeyPhoneCall) {
                        ContactsController.getInstance(this.currentAccount).setPrivacyRules(var43.rules, 2);
                     } else if (var17 instanceof TLRPC.TL_privacyKeyPhoneP2P) {
                        ContactsController.getInstance(this.currentAccount).setPrivacyRules(var43.rules, 3);
                     }
                  } else {
                     TLRPC.User var44;
                     TLRPC.TL_user var45;
                     if (var16 instanceof TLRPC.TL_updateUserStatus) {
                        TLRPC.TL_updateUserStatus var46 = (TLRPC.TL_updateUserStatus)var16;
                        var44 = this.getUser(var46.user_id);
                        TLRPC.UserStatus var18 = var46.status;
                        if (var18 instanceof TLRPC.TL_userStatusRecently) {
                           var18.expires = -100;
                        } else if (var18 instanceof TLRPC.TL_userStatusLastWeek) {
                           var18.expires = -101;
                        } else if (var18 instanceof TLRPC.TL_userStatusLastMonth) {
                           var18.expires = -102;
                        }

                        if (var44 != null) {
                           var44.id = var46.user_id;
                           var44.status = var46.status;
                        }

                        var45 = new TLRPC.TL_user();
                        var45.id = var46.user_id;
                        var45.status = var46.status;
                        var11.add(var45);
                        if (var46.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                           NotificationsController.getInstance(this.currentAccount).setLastOnlineFromOtherDevice(var46.status.expires);
                        }
                     } else if (var16 instanceof TLRPC.TL_updateUserName) {
                        TLRPC.TL_updateUserName var47 = (TLRPC.TL_updateUserName)var16;
                        var44 = this.getUser(var47.user_id);
                        if (var44 != null) {
                           if (!UserObject.isContact(var44)) {
                              var44.first_name = var47.first_name;
                              var44.last_name = var47.last_name;
                           }

                           if (!TextUtils.isEmpty(var44.username)) {
                              this.objectsByUsernames.remove(var44.username);
                           }

                           if (TextUtils.isEmpty(var47.username)) {
                              this.objectsByUsernames.put(var47.username, var44);
                           }

                           var44.username = var47.username;
                        }

                        var45 = new TLRPC.TL_user();
                        var45.id = var47.user_id;
                        var45.first_name = var47.first_name;
                        var45.last_name = var47.last_name;
                        var45.username = var47.username;
                        var10.add(var45);
                     } else {
                        TLRPC.DialogPeer var49;
                        if (var16 instanceof TLRPC.TL_updateDialogPinned) {
                           TLRPC.TL_updateDialogPinned var48 = (TLRPC.TL_updateDialogPinned)var16;
                           var49 = var48.peer;
                           if (var49 instanceof TLRPC.TL_dialogPeer) {
                              var19 = DialogObject.getPeerDialogId(((TLRPC.TL_dialogPeer)var49).peer);
                           } else {
                              var19 = 0L;
                           }

                           if (!this.pinDialog(var19, var48.pinned, (TLRPC.InputPeer)null, -1L)) {
                              UserConfig.getInstance(this.currentAccount).setPinnedDialogsLoaded(var48.folder_id, false);
                              UserConfig.getInstance(this.currentAccount).saveConfig(false);
                              this.loadPinnedDialogs(var48.folder_id, var19, (ArrayList)null);
                           }
                        } else {
                           TLRPC.Peer var59;
                           if (var16 instanceof TLRPC.TL_updatePinnedDialogs) {
                              TLRPC.TL_updatePinnedDialogs var52 = (TLRPC.TL_updatePinnedDialogs)var16;
                              UserConfig.getInstance(this.currentAccount).setPinnedDialogsLoaded(var52.folder_id, false);
                              UserConfig.getInstance(this.currentAccount).saveConfig(false);
                              ArrayList var54;
                              if ((var52.flags & 1) != 0) {
                                 ArrayList var53 = new ArrayList();
                                 ArrayList var21 = var52.order;
                                 var22 = var21.size();
                                 var23 = 0;

                                 while(true) {
                                    var54 = var53;
                                    if (var23 >= var22) {
                                       break;
                                    }

                                    var49 = (TLRPC.DialogPeer)var21.get(var23);
                                    if (var49 instanceof TLRPC.TL_dialogPeer) {
                                       var59 = ((TLRPC.TL_dialogPeer)var49).peer;
                                       var24 = var59.user_id;
                                       if (var24 != 0) {
                                          var19 = (long)var24;
                                       } else {
                                          var24 = var59.chat_id;
                                          if (var24 != 0) {
                                             var24 = -var24;
                                          } else {
                                             var24 = -var59.channel_id;
                                          }

                                          var19 = (long)var24;
                                       }
                                    } else {
                                       var19 = 0L;
                                    }

                                    var53.add(var19);
                                    ++var23;
                                 }
                              } else {
                                 var54 = null;
                              }

                              this.loadPinnedDialogs(var52.folder_id, 0L, var54);
                           } else {
                              TLRPC.Dialog var66;
                              if (var16 instanceof TLRPC.TL_updateFolderPeers) {
                                 TLRPC.TL_updateFolderPeers var88 = (TLRPC.TL_updateFolderPeers)var16;
                                 var24 = var88.folder_peers.size();
                                 var40 = 0;

                                 while(true) {
                                    var23 = var1;
                                    if (var40 >= var24) {
                                       break label563;
                                    }

                                    TLRPC.TL_folderPeer var86 = (TLRPC.TL_folderPeer)var88.folder_peers.get(var40);
                                    var19 = DialogObject.getPeerDialogId(var86.peer);
                                    var66 = (TLRPC.Dialog)this.dialogs_dict.get(var19);
                                    if (var66 != null) {
                                       var23 = var86.folder_id;
                                       var66.folder_id = var23;
                                       this.ensureFolderDialogExists(var23, (boolean[])null);
                                    }

                                    ++var40;
                                 }
                              }

                              TLRPC.User var56;
                              TLRPC.TL_user var58;
                              if (var16 instanceof TLRPC.TL_updateUserPhoto) {
                                 TLRPC.TL_updateUserPhoto var55 = (TLRPC.TL_updateUserPhoto)var16;
                                 var56 = this.getUser(var55.user_id);
                                 if (var56 != null) {
                                    var56.photo = var55.photo;
                                 }

                                 var58 = new TLRPC.TL_user();
                                 var58.id = var55.user_id;
                                 var58.photo = var55.photo;
                                 var10.add(var58);
                              } else if (var16 instanceof TLRPC.TL_updateUserPhone) {
                                 TLRPC.TL_updateUserPhone var57 = (TLRPC.TL_updateUserPhone)var16;
                                 var56 = this.getUser(var57.user_id);
                                 if (var56 != null) {
                                    var56.phone = var57.phone;
                                    Utilities.phoneBookQueue.postRunnable(new _$$Lambda$MessagesController$OhbqgzlD12qA4K124X1JhSsf9bA(this, var56));
                                 }

                                 var58 = new TLRPC.TL_user();
                                 var58.id = var57.user_id;
                                 var58.phone = var57.phone;
                                 var10.add(var58);
                              } else {
                                 StringBuilder var69;
                                 if (var16 instanceof TLRPC.TL_updateNotifySettings) {
                                    TLRPC.TL_updateNotifySettings var85 = (TLRPC.TL_updateNotifySettings)var16;
                                    var80 = var14;
                                    var87 = var15;
                                    var23 = var1;
                                    if (var85.notify_settings instanceof TLRPC.TL_peerNotifySettings) {
                                       var80 = var14;
                                       if (var14 == null) {
                                          var80 = this.notificationsPreferences.edit();
                                       }

                                       var22 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                                       TLRPC.NotifyPeer var37 = var85.peer;
                                       if (var37 instanceof TLRPC.TL_notifyPeer) {
                                          TLRPC.Peer var38 = ((TLRPC.TL_notifyPeer)var37).peer;
                                          var23 = var38.user_id;
                                          if (var23 != 0) {
                                             var19 = (long)var23;
                                          } else {
                                             var23 = var38.chat_id;
                                             if (var23 != 0) {
                                                var23 = -var23;
                                             } else {
                                                var23 = -var38.channel_id;
                                             }

                                             var19 = (long)var23;
                                          }

                                          TLRPC.Dialog var39 = (TLRPC.Dialog)this.dialogs_dict.get(var19);
                                          if (var39 != null) {
                                             var39.notify_settings = var85.notify_settings;
                                          }

                                          if ((var85.notify_settings.flags & 2) != 0) {
                                             var69 = new StringBuilder();
                                             var69.append("silent_");
                                             var69.append(var19);
                                             var80.putBoolean(var69.toString(), var85.notify_settings.silent);
                                          } else {
                                             var69 = new StringBuilder();
                                             var69.append("silent_");
                                             var69.append(var19);
                                             var80.remove(var69.toString());
                                          }

                                          TLRPC.PeerNotifySettings var81 = var85.notify_settings;
                                          StringBuilder var41;
                                          if ((var81.flags & 4) != 0) {
                                             var24 = var81.mute_until;
                                             if (var24 > var22) {
                                                if (var24 > var22 + 31536000) {
                                                   var69 = new StringBuilder();
                                                   var69.append("notify2_");
                                                   var69.append(var19);
                                                   var80.putInt(var69.toString(), 2);
                                                   if (var39 != null) {
                                                      var85.notify_settings.mute_until = Integer.MAX_VALUE;
                                                   }

                                                   var23 = 0;
                                                } else {
                                                   var69 = new StringBuilder();
                                                   var69.append("notify2_");
                                                   var69.append(var19);
                                                   var80.putInt(var69.toString(), 3);
                                                   var69 = new StringBuilder();
                                                   var69.append("notifyuntil_");
                                                   var69.append(var19);
                                                   var80.putInt(var69.toString(), var85.notify_settings.mute_until);
                                                   var23 = var24;
                                                   if (var39 != null) {
                                                      var85.notify_settings.mute_until = var24;
                                                      var23 = var24;
                                                   }
                                                }

                                                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(var19, (long)var23 << 32 | 1L);
                                                NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(var19);
                                             } else {
                                                if (var39 != null) {
                                                   var81.mute_until = 0;
                                                }

                                                var41 = new StringBuilder();
                                                var41.append("notify2_");
                                                var41.append(var19);
                                                var80.putInt(var41.toString(), 0);
                                                MessagesStorage.getInstance(this.currentAccount).setDialogFlags(var19, 0L);
                                             }
                                          } else {
                                             if (var39 != null) {
                                                var81.mute_until = 0;
                                             }

                                             var41 = new StringBuilder();
                                             var41.append("notify2_");
                                             var41.append(var19);
                                             var80.remove(var41.toString());
                                             MessagesStorage.getInstance(this.currentAccount).setDialogFlags(var19, 0L);
                                          }
                                       } else {
                                          TLRPC.PeerNotifySettings var42;
                                          if (var37 instanceof TLRPC.TL_notifyChats) {
                                             var42 = var85.notify_settings;
                                             if ((var42.flags & 1) != 0) {
                                                var80.putBoolean("EnablePreviewGroup", var42.show_previews);
                                             }

                                             var42 = var85.notify_settings;
                                             if ((var42.flags & 4) != 0) {
                                                var80.putInt("EnableGroup2", var42.mute_until);
                                             }
                                          } else if (var37 instanceof TLRPC.TL_notifyUsers) {
                                             var42 = var85.notify_settings;
                                             if ((var42.flags & 1) != 0) {
                                                var80.putBoolean("EnablePreviewAll", var42.show_previews);
                                             }

                                             var42 = var85.notify_settings;
                                             if ((var42.flags & 4) != 0) {
                                                var80.putInt("EnableAll2", var42.mute_until);
                                             }
                                          } else if (var37 instanceof TLRPC.TL_notifyBroadcasts) {
                                             var42 = var85.notify_settings;
                                             if ((var42.flags & 1) != 0) {
                                                var80.putBoolean("EnablePreviewChannel", var42.show_previews);
                                             }

                                             var42 = var85.notify_settings;
                                             if ((var42.flags & 4) != 0) {
                                                var80.putInt("EnableChannel2", var42.mute_until);
                                             }
                                          }
                                       }

                                       var87 = var15;
                                       var23 = var1;
                                    }
                                    break label564;
                                 }

                                 if (var16 instanceof TLRPC.TL_updateChannel) {
                                    TLRPC.TL_updateChannel var60 = (TLRPC.TL_updateChannel)var16;
                                    TLRPC.Dialog var62 = (TLRPC.Dialog)this.dialogs_dict.get(-((long)var60.channel_id));
                                    TLRPC.Chat var63 = this.getChat(var60.channel_id);
                                    if (var63 != null) {
                                       if (var62 == null && var63 instanceof TLRPC.TL_channel && !var63.left) {
                                          Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$gMiG6MCVxnRxisSC8QI4gCOOFM8(this, var60));
                                       } else if (var63.left && var62 != null) {
                                          var66 = this.proxyDialog;
                                          if (var66 == null || var66.id != var62.id) {
                                             this.deleteDialog(var62.id, 0);
                                          }
                                       }
                                    }

                                    var1 |= 8192;
                                    this.loadFullChat(var60.channel_id, 0, true);
                                 } else {
                                    var23 = var1;
                                    TLRPC.Peer var61;
                                    if (var16 instanceof TLRPC.TL_updateChatDefaultBannedRights) {
                                       TLRPC.TL_updateChatDefaultBannedRights var65 = (TLRPC.TL_updateChatDefaultBannedRights)var16;
                                       var61 = var65.peer;
                                       var1 = var61.channel_id;
                                       if (var1 == 0) {
                                          var1 = var61.chat_id;
                                       }

                                       TLRPC.Chat var64 = this.getChat(var1);
                                       var1 = var23;
                                       if (var64 != null) {
                                          var64.default_banned_rights = var65.default_banned_rights;
                                          AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$GABj5DmLp4L1Zi8c9sUorNKjQvk(this, var64));
                                          var1 = var23;
                                       }
                                    } else if (var16 instanceof TLRPC.TL_updateStickerSets) {
                                       TLRPC.TL_updateStickerSets var68 = (TLRPC.TL_updateStickerSets)var16;
                                       DataQuery.getInstance(this.currentAccount).loadStickers(0, false, true);
                                       var1 = var1;
                                    } else if (var16 instanceof TLRPC.TL_updateStickerSetsOrder) {
                                       TLRPC.TL_updateStickerSetsOrder var71 = (TLRPC.TL_updateStickerSetsOrder)var16;
                                       DataQuery.getInstance(this.currentAccount).reorderStickers(var71.masks, var71.order);
                                       var1 = var1;
                                    } else if (var16 instanceof TLRPC.TL_updateFavedStickers) {
                                       DataQuery.getInstance(this.currentAccount).loadRecents(2, false, false, true);
                                       var1 = var1;
                                    } else if (var16 instanceof TLRPC.TL_updateContactsReset) {
                                       ContactsController.getInstance(this.currentAccount).forceImportContacts();
                                       var1 = var1;
                                    } else if (var16 instanceof TLRPC.TL_updateNewStickerSet) {
                                       TLRPC.TL_updateNewStickerSet var73 = (TLRPC.TL_updateNewStickerSet)var16;
                                       DataQuery.getInstance(this.currentAccount).addNewStickerSet(var73.stickerset);
                                       var1 = var1;
                                    } else if (var16 instanceof TLRPC.TL_updateSavedGifs) {
                                       this.emojiPreferences.edit().putLong("lastGifLoadTime", 0L).commit();
                                       var1 = var1;
                                    } else if (var16 instanceof TLRPC.TL_updateRecentStickers) {
                                       this.emojiPreferences.edit().putLong("lastStickersLoadTime", 0L).commit();
                                       var1 = var1;
                                    } else {
                                       if (var16 instanceof TLRPC.TL_updateDraftMessage) {
                                          TLRPC.TL_updateDraftMessage var84 = (TLRPC.TL_updateDraftMessage)var16;
                                          var59 = var84.peer;
                                          var1 = var59.user_id;
                                          if (var1 != 0) {
                                             var19 = (long)var1;
                                          } else {
                                             var1 = var59.channel_id;
                                             if (var1 != 0) {
                                                var1 = -var1;
                                             } else {
                                                var1 = -var59.chat_id;
                                             }

                                             var19 = (long)var1;
                                          }

                                          DataQuery.getInstance(this.currentAccount).saveDraft(var19, var84.draft, (TLRPC.Message)null, true);
                                          break label563;
                                       }

                                       if (var16 instanceof TLRPC.TL_updateReadFeaturedStickers) {
                                          DataQuery.getInstance(this.currentAccount).markFaturedStickersAsRead(false);
                                          var1 = var1;
                                       } else if (var16 instanceof TLRPC.TL_updatePhoneCall) {
                                          TLRPC.PhoneCall var75 = ((TLRPC.TL_updatePhoneCall)var16).phone_call;
                                          VoIPService var67 = VoIPService.getSharedInstance();
                                          if (BuildVars.LOGS_ENABLED) {
                                             var69 = new StringBuilder();
                                             var69.append("Received call in update: ");
                                             var69.append(var75);
                                             FileLog.d(var69.toString());
                                             var69 = new StringBuilder();
                                             var69.append("call id ");
                                             var69.append(var75.id);
                                             FileLog.d(var69.toString());
                                          }

                                          if (var75 instanceof TLRPC.TL_phoneCallRequested) {
                                             if (var75.date + this.callRingTimeout / 1000 < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                                                var1 = var1;
                                                if (BuildVars.LOGS_ENABLED) {
                                                   FileLog.d("ignoring too old call");
                                                   var1 = var23;
                                                }
                                             } else if (VERSION.SDK_INT >= 21 && !NotificationManagerCompat.from(ApplicationLoader.applicationContext).areNotificationsEnabled()) {
                                                var1 = var1;
                                                if (BuildVars.LOGS_ENABLED) {
                                                   FileLog.d("Ignoring incoming call because notifications are disabled in system");
                                                   var1 = var23;
                                                }
                                             } else {
                                                TelephonyManager var74 = (TelephonyManager)ApplicationLoader.applicationContext.getSystemService("phone");
                                                StringBuilder var70;
                                                if (var67 == null && VoIPService.callIShouldHavePutIntoIntent == null && var74.getCallState() == 0) {
                                                   label583: {
                                                      if (BuildVars.LOGS_ENABLED) {
                                                         var70 = new StringBuilder();
                                                         var70.append("Starting service for call ");
                                                         var70.append(var75.id);
                                                         FileLog.d(var70.toString());
                                                      }

                                                      VoIPService.callIShouldHavePutIntoIntent = var75;
                                                      Intent var76 = new Intent(ApplicationLoader.applicationContext, VoIPService.class);
                                                      var76.putExtra("is_outgoing", false);
                                                      if (var75.participant_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                         var1 = var75.admin_id;
                                                      } else {
                                                         var1 = var75.participant_id;
                                                      }

                                                      var76.putExtra("user_id", var1);
                                                      var76.putExtra("account", this.currentAccount);

                                                      label498: {
                                                         try {
                                                            if (VERSION.SDK_INT >= 26) {
                                                               ApplicationLoader.applicationContext.startForegroundService(var76);
                                                               break label498;
                                                            }

                                                            ApplicationLoader.applicationContext.startService(var76);
                                                         } catch (Throwable var29) {
                                                            FileLog.e(var29);
                                                            var1 = var23;
                                                            break label583;
                                                         }

                                                         var1 = var23;
                                                         break label583;
                                                      }

                                                      var1 = var23;
                                                   }
                                                } else {
                                                   if (BuildVars.LOGS_ENABLED) {
                                                      var70 = new StringBuilder();
                                                      var70.append("Auto-declining call ");
                                                      var70.append(var75.id);
                                                      var70.append(" because there's already active one");
                                                      FileLog.d(var70.toString());
                                                   }

                                                   TLRPC.TL_phone_discardCall var72 = new TLRPC.TL_phone_discardCall();
                                                   var72.peer = new TLRPC.TL_inputPhoneCall();
                                                   TLRPC.TL_inputPhoneCall var77 = var72.peer;
                                                   var77.access_hash = var75.access_hash;
                                                   var77.id = var75.id;
                                                   var72.reason = new TLRPC.TL_phoneCallDiscardReasonBusy();
                                                   ConnectionsManager.getInstance(this.currentAccount).sendRequest(var72, new _$$Lambda$MessagesController$dvxVbHYq_pwVIer86eRbP3aeXAg(this));
                                                   var1 = var1;
                                                }
                                             }
                                          } else if (var67 != null && var75 != null) {
                                             var67.onCallUpdated(var75);
                                             var1 = var1;
                                          } else {
                                             var1 = var1;
                                             if (VoIPService.callIShouldHavePutIntoIntent != null) {
                                                if (BuildVars.LOGS_ENABLED) {
                                                   FileLog.d("Updated the call while the service is starting");
                                                }

                                                var1 = var23;
                                                if (var75.id == VoIPService.callIShouldHavePutIntoIntent.id) {
                                                   VoIPService.callIShouldHavePutIntoIntent = var75;
                                                   var1 = var23;
                                                }
                                             }
                                          }
                                       } else {
                                          boolean var26;
                                          if (var16 instanceof TLRPC.TL_updateDialogUnreadMark) {
                                             TLRPC.TL_updateDialogUnreadMark var78 = (TLRPC.TL_updateDialogUnreadMark)var16;
                                             TLRPC.DialogPeer var79 = var78.peer;
                                             if (var79 instanceof TLRPC.TL_dialogPeer) {
                                                var61 = ((TLRPC.TL_dialogPeer)var79).peer;
                                                var1 = var61.user_id;
                                                if (var1 != 0) {
                                                   var19 = (long)var1;
                                                } else {
                                                   var1 = var61.chat_id;
                                                   if (var1 != 0) {
                                                      var1 = -var1;
                                                   } else {
                                                      var1 = -var61.channel_id;
                                                   }

                                                   var19 = (long)var1;
                                                }
                                             } else {
                                                var19 = 0L;
                                             }

                                             MessagesStorage.getInstance(this.currentAccount).setDialogUnread(var19, var78.unread);
                                             TLRPC.Dialog var83 = (TLRPC.Dialog)this.dialogs_dict.get(var19);
                                             var1 = var1;
                                             if (var83 != null) {
                                                boolean var25 = var83.unread_mark;
                                                var26 = var78.unread;
                                                var1 = var23;
                                                if (var25 != var26) {
                                                   var83.unread_mark = var26;
                                                   if (var83.unread_count == 0 && !this.isDialogMuted(var19)) {
                                                      if (var83.unread_mark) {
                                                         ++this.unreadUnmutedDialogs;
                                                      } else {
                                                         --this.unreadUnmutedDialogs;
                                                      }
                                                   }

                                                   var23 |= 256;
                                                   var80 = var14;
                                                   var87 = var15;
                                                   break label564;
                                                }
                                             }
                                          } else if (var16 instanceof TLRPC.TL_updateMessagePoll) {
                                             TLRPC.TL_updateMessagePoll var82 = (TLRPC.TL_updateMessagePoll)var16;
                                             NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didUpdatePollResults, var82.poll_id, var82.poll, var82.results);
                                             var1 = var1;
                                          } else if (var16 instanceof TLRPC.TL_updateGroupCall) {
                                             var1 = var1;
                                          } else {
                                             var26 = var16 instanceof TLRPC.TL_updateGroupCallParticipant;
                                             var1 = var1;
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  var23 = var1;
                  var87 = var15;
                  var80 = var14;
                  break label564;
               }

               var87 = true;
               var80 = var14;
            }

            ++var13;
            var14 = var80;
            var15 = var87;
         }

         if (var14 != null) {
            var14.commit();
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated);
         }

         MessagesStorage.getInstance(this.currentAccount).updateUsers(var11, true, true, true);
         MessagesStorage.getInstance(this.currentAccount).updateUsers(var10, false, true, true);
         var23 = var1;
      } else {
         var15 = false;
         var23 = var1;
      }

      LongSparseArray var30 = var3;
      ArrayList var32;
      if (var3 != null) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didReceivedWebpagesInUpdates, var3);
         var13 = var3.size();
         var1 = 0;

         while(true) {
            var19 = 0L;
            if (var1 >= var13) {
               break;
            }

            long var27 = var30.keyAt(var1);
            var32 = (ArrayList)this.reloadingWebpagesPending.get(var27);
            this.reloadingWebpagesPending.remove(var27);
            if (var32 != null) {
               TLRPC.WebPage var51 = (TLRPC.WebPage)var30.valueAt(var1);
               var10 = new ArrayList();
               if (!(var51 instanceof TLRPC.TL_webPage) && !(var51 instanceof TLRPC.TL_webPageEmpty)) {
                  this.reloadingWebpagesPending.put(var51.id, var32);
               } else {
                  var24 = var32.size();
                  var19 = 0L;

                  for(var12 = 0; var12 < var24; ++var12) {
                     ((MessageObject)var32.get(var12)).messageOwner.media.webpage = var51;
                     if (var12 == 0) {
                        var19 = ((MessageObject)var32.get(var12)).getDialogId();
                        ImageLoader.saveMessageThumbs(((MessageObject)var32.get(var12)).messageOwner);
                     }

                     var10.add(((MessageObject)var32.get(var12)).messageOwner);
                  }
               }

               if (!var10.isEmpty()) {
                  MessagesStorage.getInstance(this.currentAccount).putMessages(var10, true, true, false, DownloadController.getInstance(this.currentAccount).getAutodownloadMask());
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, var19, var32);
               }
            }

            ++var1;
         }
      }

      boolean var50;
      label451: {
         if (var4 != null) {
            var40 = var4.size();

            for(var1 = 0; var1 < var40; ++var1) {
               this.updateInterfaceWithMessages(var4.keyAt(var1), (ArrayList)var4.valueAt(var1));
            }
         } else {
            if (!var15) {
               var50 = false;
               break label451;
            }

            this.sortDialogs((SparseArray)null);
         }

         var50 = true;
      }

      var30 = var5;
      var15 = var50;
      if (var5 != null) {
         var24 = var5.size();

         for(var12 = 0; var12 < var24; ++var12) {
            label434: {
               var19 = var30.keyAt(var12);
               var32 = (ArrayList)var30.valueAt(var12);
               MessageObject var36 = (MessageObject)this.dialogMessage.get(var19);
               var15 = var50;
               if (var36 != null) {
                  var22 = var32.size();
                  var13 = 0;

                  while(true) {
                     var15 = var50;
                     if (var13 >= var22) {
                        break;
                     }

                     MessageObject var33 = (MessageObject)var32.get(var13);
                     if (var36.getId() == var33.getId()) {
                        this.dialogMessage.put(var19, var33);
                        TLRPC.Peer var35 = var33.messageOwner.to_id;
                        if (var35 != null && var35.channel_id == 0) {
                           this.dialogMessagesByIds.put(var33.getId(), var33);
                        }

                        var15 = true;
                        break;
                     }

                     if (var36.getDialogId() == var33.getDialogId() && var36.messageOwner.action instanceof TLRPC.TL_messageActionPinMessage) {
                        MessageObject var34 = var36.replyMessageObject;
                        if (var34 != null && var34.getId() == var33.getId()) {
                           var36.replyMessageObject = var33;
                           var36.generatePinMessageText((TLRPC.User)null, (TLRPC.Chat)null);
                           var50 = true;
                           break label434;
                        }
                     }

                     ++var13;
                  }
               }

               var50 = var15;
            }

            DataQuery.getInstance(this.currentAccount).loadReplyMessagesForMessages(var32, var19);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, var19, var32);
         }

         var15 = var50;
      }

      if (var15) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
      }

      var1 = var23;
      if (var6) {
         var1 = var23 | 64;
      }

      var23 = var1;
      if (var7 != null) {
         var23 = var1 | 1 | 128;
      }

      if (var8 != null) {
         var40 = var8.size();

         for(var1 = 0; var1 < var40; ++var1) {
            TLRPC.ChatParticipants var31 = (TLRPC.ChatParticipants)var8.get(var1);
            MessagesStorage.getInstance(this.currentAccount).updateChatParticipants(var31);
         }
      }

      if (var9 != null) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didUpdatedMessagesViews, var9);
      }

      if (var23 != 0) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, var23);
      }

   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$252$MessagesController(SparseLongArray var1, SparseLongArray var2, SparseIntArray var3, ArrayList var4, SparseArray var5, SparseIntArray var6) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$xLr6cfkOLJahxhWtfNWAwSwDpgA(this, var1, var2, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$253$MessagesController(ArrayList var1, int var2) {
      ArrayList var3 = MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeleted(var1, false, var2);
      MessagesStorage.getInstance(this.currentAccount).updateDialogsWithDeletedMessages(var1, var3, false, var2);
   }

   // $FF: synthetic method
   public void lambda$processUpdateArray$254$MessagesController(int var1, int var2) {
      ArrayList var3 = MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeleted(var1, var2, false);
      MessagesStorage.getInstance(this.currentAccount).updateDialogsWithDeletedMessages(new ArrayList(), var3, false, var1);
   }

   // $FF: synthetic method
   public void lambda$processUpdates$232$MessagesController(boolean var1, int var2, ArrayList var3) {
      if (var1) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 64);
      }

      this.updateInterfaceWithMessages((long)var2, var3);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$processUpdates$233$MessagesController(boolean var1, TLRPC.Updates var2, ArrayList var3) {
      if (var1) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 64);
      }

      this.updateInterfaceWithMessages((long)(-var2.chat_id), var3);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
   }

   // $FF: synthetic method
   public void lambda$processUpdates$235$MessagesController(ArrayList var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$XG76p4T0ljFB2rIL7TEvl_qU3ss(this, var1));
   }

   // $FF: synthetic method
   public void lambda$processUpdates$237$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 4);
   }

   // $FF: synthetic method
   public int lambda$processUpdatesQueue$192$MessagesController(TLRPC.Updates var1, TLRPC.Updates var2) {
      return AndroidUtilities.compare(this.getUpdateSeq(var1), this.getUpdateSeq(var2));
   }

   // $FF: synthetic method
   public void lambda$processUserInfo$76$MessagesController(TLRPC.User var1, TLRPC.UserFull var2, MessageObject var3) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.userInfoDidLoad, var1.id, var2, var3);
   }

   // $FF: synthetic method
   public void lambda$putChat$11$MessagesController(TLRPC.Chat var1) {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.channelRightsUpdated, var1);
   }

   // $FF: synthetic method
   public void lambda$putUsers$10$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 4);
   }

   // $FF: synthetic method
   public void lambda$registerForPush$189$MessagesController(String var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 instanceof TLRPC.TL_boolTrue) {
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var4 = new StringBuilder();
            var4.append("account ");
            var4.append(this.currentAccount);
            var4.append(" registered for push");
            FileLog.d(var4.toString());
         }

         UserConfig.getInstance(this.currentAccount).registeredForPush = true;
         SharedConfig.pushString = var1;
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$SwAlAjR_w1esOUP73MiMXDofIVc(this));
   }

   // $FF: synthetic method
   public void lambda$reloadDialogsReadValue$12$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         TLRPC.TL_messages_peerDialogs var3 = (TLRPC.TL_messages_peerDialogs)var1;
         ArrayList var4 = new ArrayList();

         for(int var5 = 0; var5 < var3.dialogs.size(); ++var5) {
            TLRPC.Dialog var6 = (TLRPC.Dialog)var3.dialogs.get(var5);
            if (var6.read_inbox_max_id == 0) {
               var6.read_inbox_max_id = 1;
            }

            if (var6.read_outbox_max_id == 0) {
               var6.read_outbox_max_id = 1;
            }

            DialogObject.initDialog(var6);
            Integer var8 = (Integer)this.dialogs_read_inbox_max.get(var6.id);
            Integer var7 = var8;
            if (var8 == null) {
               var7 = 0;
            }

            this.dialogs_read_inbox_max.put(var6.id, Math.max(var6.read_inbox_max_id, var7));
            if (var7 == 0) {
               if (var6.peer.channel_id != 0) {
                  TLRPC.TL_updateReadChannelInbox var9 = new TLRPC.TL_updateReadChannelInbox();
                  var9.channel_id = var6.peer.channel_id;
                  var9.max_id = var6.read_inbox_max_id;
                  var4.add(var9);
               } else {
                  TLRPC.TL_updateReadHistoryInbox var10 = new TLRPC.TL_updateReadHistoryInbox();
                  var10.peer = var6.peer;
                  var10.max_id = var6.read_inbox_max_id;
                  var4.add(var10);
               }
            }

            var8 = (Integer)this.dialogs_read_outbox_max.get(var6.id);
            var7 = var8;
            if (var8 == null) {
               var7 = 0;
            }

            this.dialogs_read_outbox_max.put(var6.id, Math.max(var6.read_outbox_max_id, var7));
            if (var7 == 0) {
               if (var6.peer.channel_id != 0) {
                  TLRPC.TL_updateReadChannelOutbox var11 = new TLRPC.TL_updateReadChannelOutbox();
                  var11.channel_id = var6.peer.channel_id;
                  var11.max_id = var6.read_outbox_max_id;
                  var4.add(var11);
               } else {
                  TLRPC.TL_updateReadHistoryOutbox var12 = new TLRPC.TL_updateReadHistoryOutbox();
                  var12.peer = var6.peer;
                  var12.max_id = var6.read_outbox_max_id;
                  var4.add(var12);
               }
            }
         }

         if (!var4.isEmpty()) {
            this.processUpdateArray(var4, (ArrayList)null, (ArrayList)null, false);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$reloadMentionsCountForChannels$132$MessagesController(ArrayList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         long var3 = (long)(-(Integer)var1.get(var2));
         TLRPC.TL_messages_getUnreadMentions var5 = new TLRPC.TL_messages_getUnreadMentions();
         var5.peer = this.getInputPeer((int)var3);
         var5.limit = 1;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, new _$$Lambda$MessagesController$XzymKbdqXufr7jm1iFFTz7X7ruw(this, var3));
      }

   }

   // $FF: synthetic method
   public void lambda$reloadMessages$22$MessagesController(long var1, TLRPC.Chat var3, ArrayList var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         TLRPC.messages_Messages var7 = (TLRPC.messages_Messages)var5;
         SparseArray var8 = new SparseArray();

         int var9;
         for(var9 = 0; var9 < var7.users.size(); ++var9) {
            TLRPC.User var15 = (TLRPC.User)var7.users.get(var9);
            var8.put(var15.id, var15);
         }

         SparseArray var10 = new SparseArray();

         for(var9 = 0; var9 < var7.chats.size(); ++var9) {
            TLRPC.Chat var16 = (TLRPC.Chat)var7.chats.get(var9);
            var10.put(var16.id, var16);
         }

         Integer var18 = (Integer)this.dialogs_read_inbox_max.get(var1);
         Integer var17 = var18;
         if (var18 == null) {
            var17 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, var1);
            this.dialogs_read_inbox_max.put(var1, var17);
         }

         Integer var11 = (Integer)this.dialogs_read_outbox_max.get(var1);
         var18 = var11;
         if (var11 == null) {
            var18 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var1);
            this.dialogs_read_outbox_max.put(var1, var18);
         }

         ArrayList var19 = new ArrayList();

         for(var9 = 0; var9 < var7.messages.size(); ++var9) {
            TLRPC.Message var12 = (TLRPC.Message)var7.messages.get(var9);
            if (var3 != null && var3.megagroup) {
               var12.flags |= Integer.MIN_VALUE;
            }

            var12.dialog_id = var1;
            Integer var13;
            if (var12.out) {
               var13 = var18;
            } else {
               var13 = var17;
            }

            boolean var14;
            if (var13 < var12.id) {
               var14 = true;
            } else {
               var14 = false;
            }

            var12.unread = var14;
            var19.add(new MessageObject(this.currentAccount, var12, var8, var10, true));
         }

         ImageLoader.saveMessagesThumbs(var7.messages);
         MessagesStorage.getInstance(this.currentAccount).putMessages(var7, var1, -1, 0, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$SnNO4Xza4siBfE3j_QycdBx_L6U(this, var1, var4, var19));
      }

   }

   // $FF: synthetic method
   public void lambda$reloadWebPages$105$MessagesController(String var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$CG95xW3CPVaIHZyuw5jMlgk_hAs(this, var1, var4, var2));
   }

   // $FF: synthetic method
   public void lambda$removeDialog$65$MessagesController(long var1) {
      SparseIntArray var3 = this.channelsPts;
      int var4 = -((int)var1);
      var3.delete(var4);
      this.shortPollChannels.delete(var4);
      this.needShortPollChannels.delete(var4);
      this.shortPollOnlines.delete(var4);
      this.needShortPollOnlines.delete(var4);
   }

   // $FF: synthetic method
   public void lambda$reorderPinnedDialogs$218$MessagesController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var1 != 0L) {
         MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      }

   }

   // $FF: synthetic method
   public void lambda$resetDialogs$119$MessagesController(int var1, int var2, int var3, int var4, TLObject var5, TLRPC.TL_error var6) {
      if (var5 != null) {
         this.resetDialogsPinned = (TLRPC.TL_messages_peerDialogs)var5;

         for(int var7 = 0; var7 < this.resetDialogsPinned.dialogs.size(); ++var7) {
            ((TLRPC.Dialog)this.resetDialogsPinned.dialogs.get(var7)).pinned = true;
         }

         this.resetDialogs(false, var1, var2, var3, var4);
      }

   }

   // $FF: synthetic method
   public void lambda$resetDialogs$120$MessagesController(int var1, int var2, int var3, int var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         this.resetDialogsAll = (TLRPC.messages_Dialogs)var5;
         this.resetDialogs(false, var1, var2, var3, var4);
      }

   }

   // $FF: synthetic method
   public void lambda$saveGif$71$MessagesController(Object var1, TLRPC.TL_messages_saveGif var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 != null && FileRefController.isFileRefError(var4.text) && var1 != null) {
         FileRefController.getInstance(this.currentAccount).requestReference(var1, var2);
      }

   }

   // $FF: synthetic method
   public void lambda$saveRecentSticker$72$MessagesController(Object var1, TLRPC.TL_messages_saveRecentSticker var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 != null && FileRefController.isFileRefError(var4.text) && var1 != null) {
         FileRefController.getInstance(this.currentAccount).requestReference(var1, var2);
      }

   }

   // $FF: synthetic method
   public void lambda$saveWallpaperToServer$60$MessagesController(long var1, boolean var3, long var4, TLObject var6, TLRPC.TL_error var7) {
      MessagesStorage.getInstance(this.currentAccount).removePendingTask(var1);
      if (!var3 && this.uploadingWallpaper != null) {
         Editor var8 = getGlobalMainSettings().edit();
         var8.putLong("selectedBackground2", var4);
         var8.commit();
      }

   }

   // $FF: synthetic method
   public void lambda$sendBotStart$176$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         this.processUpdates((TLRPC.Updates)var1, false);
      }
   }

   // $FF: synthetic method
   public void lambda$sendTyping$101$MessagesController(int var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$8jlYAGfWz85ggA903BPszKwwO84(this, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$sendTyping$99$MessagesController(int var1, long var2, TLObject var4, TLRPC.TL_error var5) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$wKGVCgj8ovsTOS0jCqWZ3uZqdEI(this, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$setDefaultBannedRole$45$MessagesController(int var1, BaseFragment var2, TLRPC.TL_messages_editChatDefaultBannedRights var3, boolean var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         this.processUpdates((TLRPC.Updates)var5, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$nf_CcZzYILO5CfS_8KQCWMOSEPI(this, var1), 1000L);
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$jTlC42LjZXj4VFuUMm4RDeppjZs(this, var6, var2, var3, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$setLastCreatedDialogId$9$MessagesController(boolean var1, long var2) {
      if (var1) {
         if (this.createdDialogIds.contains(var2)) {
            return;
         }

         this.createdDialogIds.add(var2);
      } else {
         this.createdDialogIds.remove(var2);
      }

   }

   // $FF: synthetic method
   public void lambda$setUserAdminRole$48$MessagesController(int var1, BaseFragment var2, TLRPC.TL_channels_editAdmin var3, boolean var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         this.processUpdates((TLRPC.Updates)var5, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$1jQ0_MBw6KZOFlWcz0l4GlwUKWE(this, var1), 1000L);
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$xQRkDsNdc7E8A8vOKZnj4SaYK_I(this, var6, var2, var3, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$setUserAdminRole$51$MessagesController(int var1, BaseFragment var2, TLRPC.TL_messages_editChatAdmin var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$G__d657sEgRw5eIUK22CxKPabk8(this, var1), 1000L);
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$lgHhjVycSWp0O6n3iqFJOy3XH1A(this, var5, var2, var3));
      }

   }

   // $FF: synthetic method
   public void lambda$setUserAdminRole$52$MessagesController(TLRPC.TL_messages_editChatAdmin var1, RequestDelegate var2) {
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, var2);
   }

   // $FF: synthetic method
   public void lambda$setUserBannedRole$42$MessagesController(int var1, BaseFragment var2, TLRPC.TL_channels_editBanned var3, boolean var4, TLObject var5, TLRPC.TL_error var6) {
      if (var6 == null) {
         this.processUpdates((TLRPC.Updates)var5, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$1dVEAugRY_eNBjCuCw7fZ_d6SjQ(this, var1), 1000L);
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$uqMzh59_UK2vW6XHpekt8li6T30(this, var6, var2, var3, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$startShortPoll$196$MessagesController(boolean var1, TLRPC.Chat var2) {
      if (var1) {
         this.needShortPollChannels.delete(var2.id);
         if (var2.megagroup) {
            this.needShortPollOnlines.delete(var2.id);
         }
      } else {
         this.needShortPollChannels.put(var2.id, 0);
         if (this.shortPollChannels.indexOfKey(var2.id) < 0) {
            this.getChannelDifference(var2.id, 3, 0L, (TLRPC.InputChannel)null);
         }

         if (var2.megagroup) {
            this.needShortPollOnlines.put(var2.id, 0);
            if (this.shortPollOnlines.indexOfKey(var2.id) < 0) {
               this.shortPollOnlines.put(var2.id, 0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$toogleChannelInvitesHistory$171$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         this.processUpdates((TLRPC.Updates)var1, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$MQt495ngo0PePemjE4vQFdgondE(this));
      }

   }

   // $FF: synthetic method
   public void lambda$toogleChannelSignatures$169$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var1 != null) {
         this.processUpdates((TLRPC.Updates)var1, false);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$3BIrPkpsnU56wDIO_4y6LdEZG1o(this));
      }

   }

   // $FF: synthetic method
   public void lambda$unblockUser$53$MessagesController(TLRPC.User var1, TLObject var2, TLRPC.TL_error var3) {
      MessagesStorage.getInstance(this.currentAccount).deleteBlockedUser(var1.id);
   }

   // $FF: synthetic method
   public void lambda$updateChannelUserName$175$MessagesController(int var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      if (var3 instanceof TLRPC.TL_boolTrue) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$a0Xdl6BGzzSwlj898G_QllCxUyU(this, var1, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$updateChatAbout$173$MessagesController(TLRPC.ChatFull var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      if (var3 instanceof TLRPC.TL_boolTrue) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$HY_Xxx1aYrQjlxZz1_f8hnkFqxY(this, var1, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$updateConfig$2$MessagesController(TLRPC.TL_config var1) {
      DownloadController.getInstance(this.currentAccount).loadAutoDownloadConfig(false);
      this.maxMegagroupCount = var1.megagroup_size_max;
      this.maxGroupCount = var1.chat_size_max;
      this.maxEditTime = var1.edit_time_limit;
      this.ratingDecay = var1.rating_e_decay;
      this.maxRecentGifsCount = var1.saved_gifs_limit;
      this.maxRecentStickersCount = var1.stickers_recent_limit;
      this.maxFaveStickersCount = var1.stickers_faved_limit;
      this.revokeTimeLimit = var1.revoke_time_limit;
      this.revokeTimePmLimit = var1.revoke_pm_time_limit;
      this.canRevokePmInbox = var1.revoke_pm_inbox;
      this.linkPrefix = var1.me_url_prefix;
      String var2;
      if (this.linkPrefix.endsWith("/")) {
         var2 = this.linkPrefix;
         this.linkPrefix = var2.substring(0, var2.length() - 1);
      }

      if (this.linkPrefix.startsWith("https://")) {
         this.linkPrefix = this.linkPrefix.substring(8);
      } else if (this.linkPrefix.startsWith("http://")) {
         this.linkPrefix = this.linkPrefix.substring(7);
      }

      this.callReceiveTimeout = var1.call_receive_timeout_ms;
      this.callRingTimeout = var1.call_ring_timeout_ms;
      this.callConnectTimeout = var1.call_connect_timeout_ms;
      this.callPacketTimeout = var1.call_packet_timeout_ms;
      this.maxPinnedDialogsCount = var1.pinned_dialogs_count_max;
      this.maxFolderPinnedDialogsCount = var1.pinned_infolder_count_max;
      this.maxMessageLength = var1.message_length_max;
      this.maxCaptionLength = var1.caption_length_max;
      this.defaultP2pContacts = var1.default_p2p_contacts;
      this.preloadFeaturedStickers = var1.preload_featured_stickers;
      var2 = var1.venue_search_username;
      if (var2 != null) {
         this.venueSearchBot = var2;
      }

      var2 = var1.gif_search_username;
      if (var2 != null) {
         this.gifSearchBot = var2;
      }

      if (this.imageSearchBot != null) {
         this.imageSearchBot = var1.img_search_username;
      }

      this.blockedCountry = var1.blocked_mode;
      this.dcDomainName = var1.dc_txt_domain_name;
      this.webFileDatacenterId = var1.webfile_dc_id;
      var2 = this.suggestedLangCode;
      if (var2 == null || !var2.equals(var1.suggested_lang_code)) {
         this.suggestedLangCode = var1.suggested_lang_code;
         LocaleController.getInstance().loadRemoteLanguages(this.currentAccount);
      }

      if (var1.static_maps_provider == null) {
         var1.static_maps_provider = "google";
      }

      this.mapKey = null;
      this.mapProvider = 0;
      this.availableMapProviders = 0;
      String[] var6 = var1.static_maps_provider.split(",");

      for(int var3 = 0; var3 < var6.length; ++var3) {
         String[] var4 = var6[var3].split("\\+");
         if (var4.length > 0) {
            String[] var5 = var4[0].split(":");
            if (var5.length > 0) {
               if ("yandex".equals(var5[0])) {
                  if (var3 == 0) {
                     if (var4.length > 1) {
                        this.mapProvider = 3;
                     } else {
                        this.mapProvider = 1;
                     }
                  }

                  this.availableMapProviders |= 4;
               } else if ("google".equals(var5[0])) {
                  if (var3 == 0 && var4.length > 1) {
                     this.mapProvider = 4;
                  }

                  this.availableMapProviders |= 1;
               } else if ("telegram".equals(var5[0])) {
                  if (var3 == 0) {
                     this.mapProvider = 2;
                  }

                  this.availableMapProviders |= 2;
               }

               if (var5.length > 1) {
                  this.mapKey = var5[1];
               }
            }
         }
      }

      Editor var7 = this.mainPreferences.edit();
      var7.putInt("maxGroupCount", this.maxGroupCount);
      var7.putInt("maxMegagroupCount", this.maxMegagroupCount);
      var7.putInt("maxEditTime", this.maxEditTime);
      var7.putInt("ratingDecay", this.ratingDecay);
      var7.putInt("maxRecentGifsCount", this.maxRecentGifsCount);
      var7.putInt("maxRecentStickersCount", this.maxRecentStickersCount);
      var7.putInt("maxFaveStickersCount", this.maxFaveStickersCount);
      var7.putInt("callReceiveTimeout", this.callReceiveTimeout);
      var7.putInt("callRingTimeout", this.callRingTimeout);
      var7.putInt("callConnectTimeout", this.callConnectTimeout);
      var7.putInt("callPacketTimeout", this.callPacketTimeout);
      var7.putString("linkPrefix", this.linkPrefix);
      var7.putInt("maxPinnedDialogsCount", this.maxPinnedDialogsCount);
      var7.putInt("maxFolderPinnedDialogsCount", this.maxFolderPinnedDialogsCount);
      var7.putInt("maxMessageLength", this.maxMessageLength);
      var7.putInt("maxCaptionLength", this.maxCaptionLength);
      var7.putBoolean("defaultP2pContacts", this.defaultP2pContacts);
      var7.putBoolean("preloadFeaturedStickers", this.preloadFeaturedStickers);
      var7.putInt("revokeTimeLimit", this.revokeTimeLimit);
      var7.putInt("revokeTimePmLimit", this.revokeTimePmLimit);
      var7.putInt("mapProvider", this.mapProvider);
      String var8 = this.mapKey;
      if (var8 != null) {
         var7.putString("pk", var8);
      } else {
         var7.remove("pk");
      }

      var7.putBoolean("canRevokePmInbox", this.canRevokePmInbox);
      var7.putBoolean("blockedCountry", this.blockedCountry);
      var7.putString("venueSearchBot", this.venueSearchBot);
      var7.putString("gifSearchBot", this.gifSearchBot);
      var7.putString("imageSearchBot", this.imageSearchBot);
      var7.putString("dcDomainName", this.dcDomainName);
      var7.putInt("webFileDatacenterId", this.webFileDatacenterId);
      var7.putString("suggestedLangCode", this.suggestedLangCode);
      var7.commit();
      LocaleController.getInstance().checkUpdateForCurrentRemoteLocale(this.currentAccount, var1.lang_pack_version, var1.base_lang_pack_version);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.configLoaded);
   }

   // $FF: synthetic method
   public void lambda$updateInterfaceWithMessages$255$MessagesController(TLRPC.Dialog var1, long var2, int var4) {
      if (var4 != -1) {
         if (var4 != 0) {
            var1.folder_id = var4;
            this.sortDialogs((SparseArray)null);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
         }
      } else {
         var4 = (int)var2;
         if (var4 != 0) {
            this.loadUnknownDialog(this.getInputPeer(var4), 0L);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$updatePrintingStrings$97$MessagesController(LongSparseArray var1, LongSparseArray var2) {
      this.printingStrings = var1;
      this.printingStringsTypes = var2;
   }

   // $FF: synthetic method
   public void lambda$updateTimerProc$77$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         this.lastStatusUpdateTime = System.currentTimeMillis();
         this.offlineSent = false;
         this.statusSettingState = 0;
      } else {
         long var3 = this.lastStatusUpdateTime;
         if (var3 != 0L) {
            this.lastStatusUpdateTime = var3 + 5000L;
         }
      }

      this.statusRequest = 0;
   }

   // $FF: synthetic method
   public void lambda$updateTimerProc$78$MessagesController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         this.offlineSent = true;
      } else {
         long var3 = this.lastStatusUpdateTime;
         if (var3 != 0L) {
            this.lastStatusUpdateTime = var3 + 5000L;
         }
      }

      this.statusRequest = 0;
   }

   // $FF: synthetic method
   public void lambda$updateTimerProc$80$MessagesController(int var1, TLRPC.TL_messages_getMessagesViews var2, TLObject var3, TLRPC.TL_error var4) {
      if (var3 != null) {
         TLRPC.Vector var5 = (TLRPC.Vector)var3;
         SparseArray var6 = new SparseArray();
         SparseIntArray var8 = (SparseIntArray)var6.get(var1);
         SparseIntArray var7 = var8;
         if (var8 == null) {
            var7 = new SparseIntArray();
            var6.put(var1, var7);
         }

         for(var1 = 0; var1 < var2.id.size() && var1 < var5.objects.size(); ++var1) {
            var7.put((Integer)var2.id.get(var1), (Integer)var5.objects.get(var1));
         }

         MessagesStorage.getInstance(this.currentAccount).putChannelViews(var6, var2.peer instanceof TLRPC.TL_inputPeerChannel);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$8xLr_ghSkkpTTqlNVF_KzHO2X20(this, var6));
      }

   }

   // $FF: synthetic method
   public void lambda$updateTimerProc$82$MessagesController() {
      long var1 = SystemClock.uptimeMillis();
      int var3 = this.pollsToCheck.size();

      int var7;
      for(int var4 = 0; var4 < var3; var3 = var7) {
         SparseArray var5 = (SparseArray)this.pollsToCheck.valueAt(var4);
         int var6;
         if (var5 == null) {
            var6 = var4;
            var7 = var3;
         } else {
            int var8 = var5.size();

            int var10;
            for(var7 = 0; var7 < var8; var8 = var10) {
               MessageObject var9 = (MessageObject)var5.valueAt(var7);
               if (Math.abs(var1 - var9.pollLastCheckTime) < 30000L) {
                  var6 = var7;
                  var10 = var8;
                  if (!var9.pollVisibleOnScreen) {
                     var5.remove(var9.getId());
                     var10 = var8 - 1;
                     var6 = var7 - 1;
                  }
               } else {
                  var9.pollLastCheckTime = var1;
                  TLRPC.TL_messages_getPollResults var11 = new TLRPC.TL_messages_getPollResults();
                  var11.peer = this.getInputPeer((int)var9.getDialogId());
                  var11.msg_id = var9.getId();
                  ConnectionsManager.getInstance(this.currentAccount).sendRequest(var11, new _$$Lambda$MessagesController$WAhoxMlOe9rKGiImOpgvJIHyQtk(this));
                  var10 = var8;
                  var6 = var7;
               }

               var7 = var6 + 1;
            }

            var6 = var4;
            var7 = var3;
            if (var5.size() == 0) {
               LongSparseArray var12 = this.pollsToCheck;
               var12.remove(var12.keyAt(var4));
               var7 = var3 - 1;
               var6 = var4 - 1;
            }
         }

         var4 = var6 + 1;
      }

      this.pollsToCheckSize = this.pollsToCheck.size();
   }

   // $FF: synthetic method
   public void lambda$updateTimerProc$83$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 4);
   }

   // $FF: synthetic method
   public void lambda$updateTimerProc$85$MessagesController(int var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 != null) {
         TLRPC.TL_chatOnlines var4 = (TLRPC.TL_chatOnlines)var2;
         MessagesStorage.getInstance(this.currentAccount).updateChatOnlineCount(var1, var4.onlines);
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$OmsxlAaybMmJyhzcSO6qE2vdkAg(this, var1, var4));
      }

   }

   // $FF: synthetic method
   public void lambda$updateTimerProc$86$MessagesController() {
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 64);
   }

   public void loadChannelAdmins(int var1, boolean var2) {
      if (this.loadingChannelAdmins.indexOfKey(var1) < 0) {
         SparseIntArray var3 = this.loadingChannelAdmins;
         int var4 = 0;
         var3.put(var1, 0);
         if (var2) {
            MessagesStorage.getInstance(this.currentAccount).loadChannelAdmins(var1);
         } else {
            TLRPC.TL_channels_getParticipants var5 = new TLRPC.TL_channels_getParticipants();
            ArrayList var8 = (ArrayList)this.channelAdmins.get(var1);
            if (var8 != null) {
               long var6;
               for(var6 = 0L; var4 < var8.size(); ++var4) {
                  var6 = (var6 * 20261L + 2147483648L + (long)(Integer)var8.get(var4)) % 2147483648L;
               }

               var5.hash = (int)var6;
            }

            var5.channel = this.getInputChannel(var1);
            var5.limit = 100;
            var5.filter = new TLRPC.TL_channelParticipantsAdmins();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, new _$$Lambda$MessagesController$5NQ9MFRgqgfYWG2HdrbXnmbFM_o(this, var1));
         }

      }
   }

   public void loadChannelParticipants(Integer var1) {
      if (!this.loadingFullParticipants.contains(var1) && !this.loadedFullParticipants.contains(var1)) {
         this.loadingFullParticipants.add(var1);
         TLRPC.TL_channels_getParticipants var2 = new TLRPC.TL_channels_getParticipants();
         var2.channel = this.getInputChannel(var1);
         var2.filter = new TLRPC.TL_channelParticipantsRecent();
         var2.offset = 0;
         var2.limit = 32;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$MessagesController$2gEZaEhNLvSnEOT8iEFZtRspSU0(this, var1));
      }

   }

   public void loadChatInfo(int var1, CountDownLatch var2, boolean var3) {
      MessagesStorage.getInstance(this.currentAccount).loadChatInfo(var1, var2, var3, false);
   }

   public void loadCurrentState() {
      if (!this.updatingState) {
         this.updatingState = true;
         TLRPC.TL_updates_getState var1 = new TLRPC.TL_updates_getState();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$MessagesController$cVyFjAxO3BTy_3AQHVKKSH7bs_0(this));
      }
   }

   public void loadDialogPhotos(int var1, int var2, long var3, boolean var5, int var6) {
      if (var5) {
         MessagesStorage.getInstance(this.currentAccount).getDialogPhotos(var1, var2, var3, var6);
      } else if (var1 > 0) {
         TLRPC.User var7 = this.getUser(var1);
         if (var7 == null) {
            return;
         }

         TLRPC.TL_photos_getUserPhotos var8 = new TLRPC.TL_photos_getUserPhotos();
         var8.limit = var2;
         var8.offset = 0;
         var8.max_id = (long)((int)var3);
         var8.user_id = this.getInputUser(var7);
         var1 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$MessagesController$WZMD1Tmg9CzkfKN6c7nkyjvpJFA(this, var1, var2, var3, var6));
         ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var1, var6);
      } else if (var1 < 0) {
         TLRPC.TL_messages_search var9 = new TLRPC.TL_messages_search();
         var9.filter = new TLRPC.TL_inputMessagesFilterChatPhotos();
         var9.limit = var2;
         var9.offset_id = (int)var3;
         var9.q = "";
         var9.peer = this.getInputPeer(var1);
         var1 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var9, new _$$Lambda$MessagesController$mljRdiyVSAoNeixnSx85I7LZjOE(this, var1, var2, var3, var6));
         ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var1, var6);
      }

   }

   public void loadDialogs(int var1, int var2, int var3, boolean var4) {
      this.loadDialogs(var1, var2, var3, var4, (Runnable)null);
   }

   public void loadDialogs(int var1, int var2, int var3, boolean var4, Runnable var5) {
      if (!this.loadingDialogs.get(var1) && !this.resetingDialogs) {
         SparseBooleanArray var6 = this.loadingDialogs;
         boolean var7 = true;
         var6.put(var1, true);
         NotificationCenter var16 = NotificationCenter.getInstance(this.currentAccount);
         int var8 = NotificationCenter.dialogsNeedReload;
         byte var9 = 0;
         var16.postNotificationName(var8);
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var17 = new StringBuilder();
            var17.append("folderId = ");
            var17.append(var1);
            var17.append(" load cacheOffset = ");
            var17.append(var2);
            var17.append(" count = ");
            var17.append(var3);
            var17.append(" cache = ");
            var17.append(var4);
            FileLog.d(var17.toString());
         }

         if (var4) {
            MessagesStorage var14 = MessagesStorage.getInstance(this.currentAccount);
            if (var2 == 0) {
               var2 = var9;
            } else {
               var2 = this.nextDialogsCacheOffset.get(var1, 0);
            }

            var14.getDialogs(var1, var2, var3);
         } else {
            TLRPC.TL_messages_getDialogs var18 = new TLRPC.TL_messages_getDialogs();
            var18.limit = var3;
            var18.exclude_pinned = true;
            if (var1 != 0) {
               var18.flags |= 2;
               var18.folder_id = var1;
            }

            int[] var10 = UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(var1);
            long var12;
            if (var10[0] != -1) {
               if (var10[0] == Integer.MAX_VALUE) {
                  this.dialogsEndReached.put(var1, true);
                  this.serverDialogsEndReached.put(var1, true);
                  this.loadingDialogs.put(var1, false);
                  NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
                  return;
               }

               var18.offset_id = var10[0];
               var18.offset_date = var10[1];
               if (var18.offset_id == 0) {
                  var18.offset_peer = new TLRPC.TL_inputPeerEmpty();
               } else {
                  if (var10[4] != 0) {
                     var18.offset_peer = new TLRPC.TL_inputPeerChannel();
                     var18.offset_peer.channel_id = var10[4];
                  } else if (var10[2] != 0) {
                     var18.offset_peer = new TLRPC.TL_inputPeerUser();
                     var18.offset_peer.user_id = var10[2];
                  } else {
                     var18.offset_peer = new TLRPC.TL_inputPeerChat();
                     var18.offset_peer.chat_id = var10[3];
                  }

                  TLRPC.InputPeer var24 = var18.offset_peer;
                  var12 = (long)var10[5];
                  var24.access_hash = (long)var10[5] << 32 | var12;
               }
            } else {
               ArrayList var11 = this.getDialogs(var1);
               var2 = var11.size() - 1;

               boolean var15;
               while(true) {
                  if (var2 < 0) {
                     var15 = false;
                     break;
                  }

                  TLRPC.Dialog var20 = (TLRPC.Dialog)var11.get(var2);
                  if (!var20.pinned) {
                     var12 = var20.id;
                     int var19 = (int)var12;
                     var8 = (int)(var12 >> 32);
                     if (var19 != 0 && var8 != 1 && var20.top_message > 0) {
                        MessageObject var21 = (MessageObject)this.dialogMessage.get(var12);
                        if (var21 != null && var21.getId() > 0) {
                           label65: {
                              TLRPC.Message var22 = var21.messageOwner;
                              var18.offset_date = var22.date;
                              var18.offset_id = var22.id;
                              TLRPC.Peer var23 = var22.to_id;
                              var2 = var23.channel_id;
                              if (var2 == 0) {
                                 var2 = var23.chat_id;
                                 if (var2 == 0) {
                                    var2 = var23.user_id;
                                    break label65;
                                 }
                              }

                              var2 = -var2;
                           }

                           var18.offset_peer = this.getInputPeer(var2);
                           var15 = var7;
                           break;
                        }
                     }
                  }

                  --var2;
               }

               if (!var15) {
                  var18.offset_peer = new TLRPC.TL_inputPeerEmpty();
               }
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var18, new _$$Lambda$MessagesController$7uQARORTbE9n3vBAnDQr0_O75Rk(this, var1, var3, var5));
         }
      }

   }

   public void loadFullChat(int var1, int var2, boolean var3) {
      boolean var4 = this.loadedFullChats.contains(var1);
      if (!this.loadingFullChats.contains(var1) && (var3 || !var4)) {
         this.loadingFullChats.add(var1);
         long var5 = (long)(-var1);
         TLRPC.Chat var7 = this.getChat(var1);
         Object var9;
         if (ChatObject.isChannel(var7)) {
            TLRPC.TL_channels_getFullChannel var10 = new TLRPC.TL_channels_getFullChannel();
            var10.channel = getInputChannel(var7);
            var9 = var10;
            if (var7.megagroup) {
               this.loadChannelAdmins(var1, var4 ^ true);
               var9 = var10;
            }
         } else {
            label34: {
               TLRPC.TL_messages_getFullChat var8 = new TLRPC.TL_messages_getFullChat();
               var8.chat_id = var1;
               if (this.dialogs_read_inbox_max.get(var5) != null) {
                  var9 = var8;
                  if (this.dialogs_read_outbox_max.get(var5) != null) {
                     break label34;
                  }
               }

               this.reloadDialogsReadValue((ArrayList)null, var5);
               var9 = var8;
            }
         }

         var1 = ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var9, new _$$Lambda$MessagesController$ebifWTnrFPKK5b_9D1xusLXGK7Q(this, var7, var5, var1, var2));
         if (var2 != 0) {
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var1, var2);
         }
      }

   }

   public void loadFullUser(TLRPC.User var1, int var2, boolean var3) {
      if (var1 != null && !this.loadingFullUsers.contains(var1.id) && (var3 || !this.loadedFullUsers.contains(var1.id))) {
         this.loadingFullUsers.add(var1.id);
         TLRPC.TL_users_getFullUser var4 = new TLRPC.TL_users_getFullUser();
         var4.id = this.getInputUser(var1);
         long var5 = (long)var1.id;
         if (this.dialogs_read_inbox_max.get(var5) == null || this.dialogs_read_outbox_max.get(var5) == null) {
            this.reloadDialogsReadValue((ArrayList)null, var5);
         }

         int var7 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$MessagesController$sHevIZGEf9jGeQ0PmpgZfE_2SOQ(this, var1, var2));
         ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var7, var2);
      }

   }

   public void loadGlobalNotificationsSettings() {
      if (this.loadingNotificationSettings == 0 && !UserConfig.getInstance(this.currentAccount).notificationsSettingsLoaded) {
         SharedPreferences var1 = getNotificationsSettings(this.currentAccount);
         Editor var2 = null;
         boolean var3;
         if (var1.contains("EnableGroup")) {
            var3 = var1.getBoolean("EnableGroup", true);
            var2 = var1.edit();
            if (!var3) {
               var2.putInt("EnableGroup2", Integer.MAX_VALUE);
               var2.putInt("EnableChannel2", Integer.MAX_VALUE);
            }

            var2.remove("EnableGroup").commit();
         }

         Editor var4 = var2;
         if (var1.contains("EnableAll")) {
            var3 = var1.getBoolean("EnableAll", true);
            var4 = var2;
            if (var2 == null) {
               var4 = var1.edit();
            }

            if (!var3) {
               var4.putInt("EnableAll2", Integer.MAX_VALUE);
            }

            var4.remove("EnableAll").commit();
         }

         if (var4 != null) {
            var4.commit();
         }

         this.loadingNotificationSettings = 3;

         for(int var5 = 0; var5 < 3; ++var5) {
            TLRPC.TL_account_getNotifySettings var6 = new TLRPC.TL_account_getNotifySettings();
            if (var5 == 0) {
               var6.peer = new TLRPC.TL_inputNotifyChats();
            } else if (var5 == 1) {
               var6.peer = new TLRPC.TL_inputNotifyUsers();
            } else if (var5 == 2) {
               var6.peer = new TLRPC.TL_inputNotifyBroadcasts();
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, new _$$Lambda$MessagesController$Kw60WHVImMJ5bL_qjfLao1O7Ey8(this, var5));
         }
      }

      if (!UserConfig.getInstance(this.currentAccount).notificationsSignUpSettingsLoaded) {
         this.loadSignUpNotificationsSettings();
      }

   }

   public void loadHintDialogs() {
      if (this.hintDialogs.isEmpty() && !TextUtils.isEmpty(this.installReferer)) {
         TLRPC.TL_help_getRecentMeUrls var1 = new TLRPC.TL_help_getRecentMeUrls();
         var1.referer = this.installReferer;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$MessagesController$S17IW_TdnLou1yG0GrX54gGAuDE(this));
      }

   }

   public void loadMessages(long var1, int var3, int var4, int var5, boolean var6, int var7, int var8, int var9, int var10, boolean var11, int var12) {
      this.loadMessages(var1, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, 0, 0, 0, false, 0);
   }

   public void loadMessages(long var1, int var3, int var4, int var5, boolean var6, int var7, int var8, int var9, int var10, boolean var11, int var12, int var13, int var14, int var15, boolean var16, int var17) {
      this.loadMessagesInternal(var1, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, true);
   }

   public void loadPeerSettings(TLRPC.User var1, TLRPC.Chat var2) {
      if (var1 != null || var2 != null) {
         int var3;
         if (var1 != null) {
            var3 = var1.id;
         } else {
            var3 = -var2.id;
         }

         long var4 = (long)var3;
         if (this.loadingPeerSettings.indexOfKey(var4) < 0) {
            this.loadingPeerSettings.put(var4, true);
            StringBuilder var6;
            if (BuildVars.LOGS_ENABLED) {
               var6 = new StringBuilder();
               var6.append("request spam button for ");
               var6.append(var4);
               FileLog.d(var6.toString());
            }

            SharedPreferences var9 = this.notificationsPreferences;
            StringBuilder var7 = new StringBuilder();
            var7.append("spam3_");
            var7.append(var4);
            if (var9.getInt(var7.toString(), 0) == 1) {
               if (BuildVars.LOGS_ENABLED) {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("spam button already hidden for ");
                  var8.append(var4);
                  FileLog.d(var8.toString());
               }

            } else {
               SharedPreferences var11 = this.notificationsPreferences;
               var6 = new StringBuilder();
               var6.append("spam_");
               var6.append(var4);
               if (var11.getBoolean(var6.toString(), false)) {
                  TLRPC.TL_messages_hideReportSpam var12 = new TLRPC.TL_messages_hideReportSpam();
                  if (var1 != null) {
                     var12.peer = this.getInputPeer(var1.id);
                  } else if (var2 != null) {
                     var12.peer = this.getInputPeer(-var2.id);
                  }

                  ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, new _$$Lambda$MessagesController$earKZXcnSlbNSw2ZGT5Q1z7WPUw(this, var4));
               } else {
                  TLRPC.TL_messages_getPeerSettings var10 = new TLRPC.TL_messages_getPeerSettings();
                  if (var1 != null) {
                     var10.peer = this.getInputPeer(var1.id);
                  } else if (var2 != null) {
                     var10.peer = this.getInputPeer(-var2.id);
                  }

                  ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$MessagesController$Akjl4BU34QO3ZqaL4fYbP0pvGoA(this, var4));
               }
            }
         }
      }
   }

   public void loadPinnedDialogs(int var1, long var2, ArrayList var4) {
      if (this.loadingPinnedDialogs.indexOfKey(var1) < 0 && !UserConfig.getInstance(this.currentAccount).isPinnedDialogsLoaded(var1)) {
         this.loadingPinnedDialogs.put(var1, 1);
         TLRPC.TL_messages_getPinnedDialogs var5 = new TLRPC.TL_messages_getPinnedDialogs();
         var5.folder_id = var1;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, new _$$Lambda$MessagesController$5ms5SUOMLniJAFeiQgpTUwbDtSY(this, var1));
      }

   }

   public void loadSignUpNotificationsSettings() {
      if (!this.loadingNotificationSignUpSettings) {
         this.loadingNotificationSignUpSettings = true;
         TLRPC.TL_account_getContactSignUpNotification var1 = new TLRPC.TL_account_getContactSignUpNotification();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$MessagesController$rOfsCqpWEwvJI16XzaGq8LurqHg(this));
      }

   }

   protected void loadUnknownChannel(TLRPC.Chat var1, long var2) {
      if (var1 instanceof TLRPC.TL_channel && this.gettingUnknownChannels.indexOfKey(var1.id) < 0) {
         if (var1.access_hash == 0L) {
            if (var2 != 0L) {
               MessagesStorage.getInstance(this.currentAccount).removePendingTask(var2);
            }

            return;
         }

         TLRPC.TL_inputPeerChannel var4 = new TLRPC.TL_inputPeerChannel();
         int var5 = var1.id;
         var4.channel_id = var5;
         var4.access_hash = var1.access_hash;
         this.gettingUnknownChannels.put(var5, true);
         TLRPC.TL_messages_getPeerDialogs var6 = new TLRPC.TL_messages_getPeerDialogs();
         TLRPC.TL_inputDialogPeer var7 = new TLRPC.TL_inputDialogPeer();
         var7.peer = var4;
         var6.peers.add(var7);
         long var8 = var2;
         if (var2 == 0L) {
            Object var10 = null;

            NativeByteBuffer var13;
            label37: {
               Exception var14;
               label36: {
                  try {
                     var13 = new NativeByteBuffer(var1.getObjectSize() + 4);
                  } catch (Exception var12) {
                     var14 = var12;
                     var13 = (NativeByteBuffer)var10;
                     break label36;
                  }

                  try {
                     var13.writeInt32(0);
                     var1.serializeToStream(var13);
                     break label37;
                  } catch (Exception var11) {
                     var14 = var11;
                  }
               }

               FileLog.e((Throwable)var14);
            }

            var8 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var13);
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, new _$$Lambda$MessagesController$gfG4ToHa5Z2rjZdIfshLWaUs_Ms(this, var8, var1));
      }

   }

   protected void loadUnknownDialog(TLRPC.InputPeer var1, long var2) {
      if (var1 != null) {
         long var4 = DialogObject.getPeerDialogId(var1);
         if (this.gettingUnknownDialogs.indexOfKey(var4) < 0) {
            this.gettingUnknownDialogs.put(var4, true);
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var6 = new StringBuilder();
               var6.append("load unknown dialog ");
               var6.append(var4);
               FileLog.d(var6.toString());
            }

            TLRPC.TL_messages_getPeerDialogs var7 = new TLRPC.TL_messages_getPeerDialogs();
            TLRPC.TL_inputDialogPeer var14 = new TLRPC.TL_inputDialogPeer();
            var14.peer = var1;
            var7.peers.add(var14);
            long var8 = var2;
            if (var2 == 0L) {
               NativeByteBuffer var13;
               label32: {
                  NativeByteBuffer var15;
                  label31: {
                     Exception var10;
                     label30: {
                        try {
                           var15 = new NativeByteBuffer(var1.getObjectSize() + 4);
                        } catch (Exception var12) {
                           var13 = null;
                           var10 = var12;
                           break label30;
                        }

                        try {
                           var15.writeInt32(15);
                           var1.serializeToStream(var15);
                           break label31;
                        } catch (Exception var11) {
                           var10 = var11;
                           var13 = var15;
                        }
                     }

                     FileLog.e((Throwable)var10);
                     break label32;
                  }

                  var13 = var15;
               }

               var8 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var13);
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$MessagesController$_qOB0YtC_RNY0KjHwuxhfdX_Ckg(this, var8, var4));
         }
      }
   }

   public void loadUnreadDialogs() {
      if (!this.loadingUnreadDialogs && !UserConfig.getInstance(this.currentAccount).unreadDialogsLoaded) {
         this.loadingUnreadDialogs = true;
         TLRPC.TL_messages_getDialogUnreadMarks var1 = new TLRPC.TL_messages_getDialogUnreadMarks();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, new _$$Lambda$MessagesController$A7Jim0c_hwW7n1Xp7g9CGcD85b0(this));
      }

   }

   public void loadUserInfo(TLRPC.User var1, boolean var2, int var3) {
      MessagesStorage.getInstance(this.currentAccount).loadUserInfo(var1, var2, var3);
   }

   public void markChannelDialogMessageAsDeleted(ArrayList var1, int var2) {
      MessageObject var3 = (MessageObject)this.dialogMessage.get((long)(-var2));
      if (var3 != null) {
         for(var2 = 0; var2 < var1.size(); ++var2) {
            Integer var4 = (Integer)var1.get(var2);
            if (var3.getId() == var4) {
               var3.deleted = true;
               break;
            }
         }
      }

   }

   public void markDialogAsRead(long var1, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      boolean var22;
      label48: {
         int var9 = (int)var1;
         int var10 = (int)(var1 >> 32);
         boolean var11 = NotificationsController.getInstance(this.currentAccount).showBadgeMessages;
         if (var9 != 0) {
            if (var3 == 0 || var10 == 1) {
               return;
            }

            long var12;
            long var14;
            boolean var18;
            label44: {
               var12 = (long)var3;
               var14 = (long)var4;
               if (var9 < 0) {
                  var4 = -var9;
                  if (ChatObject.isChannel(this.getChat(var4))) {
                     long var16 = (long)var4 << 32;
                     var14 |= var16;
                     var12 |= var16;
                     var18 = true;
                     break label44;
                  }
               }

               var18 = false;
            }

            Integer var19 = (Integer)this.dialogs_read_inbox_max.get(var1);
            Integer var20 = var19;
            if (var19 == null) {
               var20 = 0;
            }

            this.dialogs_read_inbox_max.put(var1, Math.max(var20, var3));
            MessagesStorage.getInstance(this.currentAccount).processPendingRead(var1, var12, var14, var5, var18);
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$hn_SvAXVeuxCYnxgaaX_Rgf7tNs(this, var1, var7, var3, var11, var6));
            if (var3 == Integer.MAX_VALUE) {
               var22 = false;
               break label48;
            }
         } else {
            if (var5 == 0) {
               return;
            }

            TLRPC.EncryptedChat var21 = this.getEncryptedChat(var10);
            MessagesStorage.getInstance(this.currentAccount).processPendingRead(var1, (long)var3, (long)var4, var5, false);
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$w9XQvAOdl78XSvAXCJDSbdLvKsQ(this, var1, var5, var6, var7, var4, var11));
            if (var21 != null && var21.ttl > 0) {
               var4 = Math.max(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime(), var5);
               MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(var21.id, var4, var4, 0, (ArrayList)null);
            }
         }

         var22 = true;
      }

      if (var22) {
         Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$_VEgWywMVWfxr_9Ssi_lPDOcdtc(this, var1, var8, var5, var3));
      }

   }

   public void markDialogAsReadNow(long var1) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$5qY48mZcpUp58ZVQMwppSQnDlKQ(this, var1));
   }

   public void markDialogAsUnread(long var1, TLRPC.InputPeer var3, long var4) {
      TLRPC.Dialog var6 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
      if (var6 != null) {
         var6.unread_mark = true;
         if (var6.unread_count == 0 && !this.isDialogMuted(var1)) {
            ++this.unreadUnmutedDialogs;
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, 256);
         MessagesStorage.getInstance(this.currentAccount).setDialogUnread(var1, true);
      }

      int var7 = (int)var1;
      if (var7 != 0) {
         TLRPC.TL_messages_markDialogUnread var8 = new TLRPC.TL_messages_markDialogUnread();
         var8.unread = true;
         TLRPC.InputPeer var15 = var3;
         if (var3 == null) {
            var15 = this.getInputPeer(var7);
         }

         if (var15 instanceof TLRPC.TL_inputPeerEmpty) {
            return;
         }

         TLRPC.TL_inputDialogPeer var13 = new TLRPC.TL_inputDialogPeer();
         var13.peer = var15;
         var8.peer = var13;
         long var9 = var4;
         if (var4 == 0L) {
            NativeByteBuffer var14;
            label40: {
               Exception var16;
               label39: {
                  try {
                     var14 = new NativeByteBuffer(var15.getObjectSize() + 12);
                  } catch (Exception var12) {
                     var16 = var12;
                     var14 = null;
                     break label39;
                  }

                  try {
                     var14.writeInt32(9);
                     var14.writeInt64(var1);
                     var15.serializeToStream(var14);
                     break label40;
                  } catch (Exception var11) {
                     var16 = var11;
                  }
               }

               FileLog.e((Throwable)var16);
            }

            var9 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var14);
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$MessagesController$vdUYw660VQTUH5B_NPbzLsGZ09M(this, var9));
      }

   }

   public void markMentionMessageAsRead(int var1, int var2, long var3) {
      MessagesStorage.getInstance(this.currentAccount).markMentionMessageAsRead(var1, var2, var3);
      if (var2 != 0) {
         TLRPC.TL_channels_readMessageContents var5 = new TLRPC.TL_channels_readMessageContents();
         var5.channel = this.getInputChannel(var2);
         if (var5.channel == null) {
            return;
         }

         var5.id.add(var1);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, _$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM.INSTANCE);
      } else {
         TLRPC.TL_messages_readMessageContents var6 = new TLRPC.TL_messages_readMessageContents();
         var6.id.add(var1);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, new _$$Lambda$MessagesController$HGjYxt4hoLR6qJGd10EuSLrAkII(this));
      }

   }

   public void markMentionsAsRead(long var1) {
      int var3 = (int)var1;
      if (var3 != 0) {
         MessagesStorage.getInstance(this.currentAccount).resetMentionsCount(var1, 0);
         TLRPC.TL_messages_readMentions var4 = new TLRPC.TL_messages_readMentions();
         var4.peer = getInstance(this.currentAccount).getInputPeer(var3);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, _$$Lambda$MessagesController$eUPv0EVHJrau5dp5Vk20Bf_j8L4.INSTANCE);
      }
   }

   public void markMessageAsRead(int var1, int var2, TLRPC.InputChannel var3, int var4, long var5) {
      if (var1 != 0 && var4 > 0) {
         TLRPC.InputChannel var7 = var3;
         if (var2 != 0) {
            var7 = var3;
            if (var3 == null) {
               var3 = this.getInputChannel(var2);
               var7 = var3;
               if (var3 == null) {
                  return;
               }
            }
         }

         long var8 = var5;
         int var10;
         if (var5 == 0L) {
            NativeByteBuffer var20;
            label72: {
               NativeByteBuffer var17;
               label71: {
                  Exception var11;
                  label82: {
                     Exception var10000;
                     boolean var10001;
                     label69: {
                        label83: {
                           try {
                              var17 = new NativeByteBuffer;
                           } catch (Exception var16) {
                              var10000 = var16;
                              var10001 = false;
                              break label83;
                           }

                           if (var7 != null) {
                              try {
                                 var10 = var7.getObjectSize();
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label83;
                              }
                           } else {
                              var10 = 0;
                           }

                           try {
                              var17.<init>(16 + var10);
                              break label69;
                           } catch (Exception var14) {
                              var10000 = var14;
                              var10001 = false;
                           }
                        }

                        var11 = var10000;
                        var17 = null;
                        break label82;
                     }

                     label84: {
                        try {
                           var17.writeInt32(11);
                           var17.writeInt32(var1);
                           var17.writeInt32(var2);
                           var17.writeInt32(var4);
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label84;
                        }

                        var20 = var17;
                        if (var2 == 0) {
                           break label72;
                        }

                        try {
                           var7.serializeToStream(var17);
                           break label71;
                        } catch (Exception var12) {
                           var10000 = var12;
                           var10001 = false;
                        }
                     }

                     var11 = var10000;
                  }

                  FileLog.e((Throwable)var11);
                  var20 = var17;
                  break label72;
               }

               var20 = var17;
            }

            var8 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var20);
         }

         var10 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         MessagesStorage.getInstance(this.currentAccount).createTaskForMid(var1, var2, var10, var10, var4, false);
         if (var2 != 0) {
            TLRPC.TL_channels_readMessageContents var18 = new TLRPC.TL_channels_readMessageContents();
            var18.channel = var7;
            var18.id.add(var1);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var18, new _$$Lambda$MessagesController$U0WOqTBPKmEhdWNNNHXKPpg45io(this, var8));
         } else {
            TLRPC.TL_messages_readMessageContents var19 = new TLRPC.TL_messages_readMessageContents();
            var19.id.add(var1);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var19, new _$$Lambda$MessagesController$jXQeCffIHFZ5lMnVQBUWXIuIwZ4(this, var8));
         }
      }

   }

   public void markMessageAsRead(long var1, long var3, int var5) {
      if (var3 != 0L && var1 != 0L && (var5 > 0 || var5 == Integer.MIN_VALUE)) {
         int var6 = (int)var1;
         int var7 = (int)(var1 >> 32);
         if (var6 != 0) {
            return;
         }

         TLRPC.EncryptedChat var8 = this.getEncryptedChat(var7);
         if (var8 == null) {
            return;
         }

         ArrayList var9 = new ArrayList();
         var9.add(var3);
         SecretChatHelper.getInstance(this.currentAccount).sendMessagesReadMessage(var8, var9, (TLRPC.Message)null);
         if (var5 > 0) {
            var5 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(var8.id, var5, var5, 0, var9);
         }
      }

   }

   public void markMessageContentAsRead(MessageObject var1) {
      ArrayList var2 = new ArrayList();
      long var3 = (long)var1.getId();
      int var5 = var1.messageOwner.to_id.channel_id;
      long var6 = var3;
      if (var5 != 0) {
         var6 = var3 | (long)var5 << 32;
      }

      if (var1.messageOwner.mentioned) {
         MessagesStorage.getInstance(this.currentAccount).markMentionMessageAsRead(var1.getId(), var1.messageOwner.to_id.channel_id, var1.getDialogId());
      }

      var2.add(var6);
      MessagesStorage.getInstance(this.currentAccount).markMessagesContentAsRead(var2, 0);
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, var2);
      if (var1.getId() < 0) {
         this.markMessageAsRead(var1.getDialogId(), var1.messageOwner.random_id, Integer.MIN_VALUE);
      } else if (var1.messageOwner.to_id.channel_id != 0) {
         TLRPC.TL_channels_readMessageContents var8 = new TLRPC.TL_channels_readMessageContents();
         var8.channel = this.getInputChannel(var1.messageOwner.to_id.channel_id);
         if (var8.channel == null) {
            return;
         }

         var8.id.add(var1.getId());
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, _$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE.INSTANCE);
      } else {
         TLRPC.TL_messages_readMessageContents var9 = new TLRPC.TL_messages_readMessageContents();
         var9.id.add(var1.getId());
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var9, new _$$Lambda$MessagesController$gAx8uSIsyHdT1Tx6VIiWEu0UYbQ(this));
      }

   }

   protected void onFolderEmpty(int var1) {
      if (UserConfig.getInstance(this.currentAccount).getDialogLoadOffsets(var1)[0] == Integer.MAX_VALUE) {
         this.removeFolder(var1);
      } else {
         this.loadDialogs(var1, 0, 10, false, new _$$Lambda$MessagesController$jEkulIUjpo2aQz9ir2LF9vSzFoc(this, var1));
      }

   }

   public void openByUserName(String var1, BaseFragment var2, int var3) {
      if (var1 != null && var2 != null) {
         Object var5;
         TLRPC.User var6;
         label33: {
            TLObject var4 = this.getUserOrChat(var1);
            if (var4 instanceof TLRPC.User) {
               var6 = (TLRPC.User)var4;
               if (!var6.min) {
                  var5 = null;
                  break label33;
               }
            } else if (var4 instanceof TLRPC.Chat) {
               var5 = (TLRPC.Chat)var4;
               if (!((TLRPC.Chat)var5).min) {
                  var6 = null;
                  break label33;
               }
            }

            var6 = null;
            var5 = var6;
         }

         if (var6 != null) {
            openChatOrProfileWith(var6, (TLRPC.Chat)null, var2, var3, false);
         } else if (var5 != null) {
            openChatOrProfileWith((TLRPC.User)null, (TLRPC.Chat)var5, var2, 1, false);
         } else {
            if (var2.getParentActivity() == null) {
               return;
            }

            AlertDialog[] var8 = new AlertDialog[]{new AlertDialog(var2.getParentActivity(), 3)};
            TLRPC.TL_contacts_resolveUsername var7 = new TLRPC.TL_contacts_resolveUsername();
            var7.username = var1;
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$D332AVh7nCXc7vu_l1allSBL4LE(this, var8, ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$MessagesController$2QxSvtTZ9U28CTPZ6uf2mJLKqt0(this, var8, var2, var3)), var2), 500L);
         }
      }

   }

   public void performLogout(int var1) {
      boolean var2 = true;
      if (var1 == 1) {
         this.unregistedPush();
         TLRPC.TL_auth_logOut var3 = new TLRPC.TL_auth_logOut();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$iSBguIqFt9uYneQ2bb0JRjCTxlQ(this));
      } else {
         ConnectionsManager var4 = ConnectionsManager.getInstance(this.currentAccount);
         if (var1 != 2) {
            var2 = false;
         }

         var4.cleanup(var2);
      }

      UserConfig.getInstance(this.currentAccount).clearConfig();
      NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.appDidLogout);
      MessagesStorage.getInstance(this.currentAccount).cleanup(false);
      this.cleanup();
      ContactsController.getInstance(this.currentAccount).deleteUnknownAppAccounts();
   }

   public boolean pinDialog(long var1, boolean var3, TLRPC.InputPeer var4, long var5) {
      int var7 = (int)var1;
      TLRPC.Dialog var8 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
      boolean var9 = true;
      if (var8 != null && var8.pinned != var3) {
         int var10 = var8.folder_id;
         ArrayList var11 = this.getDialogs(var10);
         var8.pinned = var3;
         if (var3) {
            int var12 = 0;

            int var13;
            for(var13 = 0; var12 < var11.size(); ++var12) {
               TLRPC.Dialog var14 = (TLRPC.Dialog)var11.get(var12);
               if (!(var14 instanceof TLRPC.TL_dialogFolder)) {
                  if (!var14.pinned) {
                     break;
                  }

                  var13 = Math.max(var14.pinnedNum, var13);
               }
            }

            var8.pinnedNum = var13 + 1;
         } else {
            var8.pinnedNum = 0;
         }

         this.sortDialogs((SparseArray)null);
         if (!var3 && var11.get(var11.size() - 1) == var8 && !this.dialogsEndReached.get(var10)) {
            var11.remove(var11.size() - 1);
         }

         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
         if (var7 != 0 && var5 != -1L) {
            TLRPC.TL_messages_toggleDialogPin var21 = new TLRPC.TL_messages_toggleDialogPin();
            var21.pinned = var3;
            TLRPC.InputPeer var19;
            if (var4 == null) {
               var19 = this.getInputPeer(var7);
            } else {
               var19 = var4;
            }

            if (var19 instanceof TLRPC.TL_inputPeerEmpty) {
               return false;
            }

            TLRPC.TL_inputDialogPeer var17 = new TLRPC.TL_inputDialogPeer();
            var17.peer = var19;
            var21.peer = var17;
            if (var5 == 0L) {
               NativeByteBuffer var18;
               label62: {
                  Exception var20;
                  label61: {
                     try {
                        var18 = new NativeByteBuffer(var19.getObjectSize() + 16);
                     } catch (Exception var16) {
                        var20 = var16;
                        var18 = null;
                        break label61;
                     }

                     try {
                        var18.writeInt32(4);
                        var18.writeInt64(var1);
                        var18.writeBool(var3);
                        var19.serializeToStream(var18);
                        break label62;
                     } catch (Exception var15) {
                        var20 = var15;
                     }
                  }

                  FileLog.e((Throwable)var20);
               }

               var5 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var18);
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var21, new _$$Lambda$MessagesController$T_HrG7SvNN3seY_Zr_dkkRoKYHQ(this, var5));
         }

         MessagesStorage.getInstance(this.currentAccount).setDialogPinned(var1, var8.pinnedNum);
         return true;
      } else {
         if (var8 != null) {
            var3 = var9;
         } else {
            var3 = false;
         }

         return var3;
      }
   }

   public void pinMessage(TLRPC.Chat var1, TLRPC.User var2, int var3, boolean var4) {
      if (var1 != null || var2 != null) {
         TLRPC.TL_messages_updatePinnedMessage var5 = new TLRPC.TL_messages_updatePinnedMessage();
         int var6;
         if (var1 != null) {
            var6 = -var1.id;
         } else {
            var6 = var2.id;
         }

         var5.peer = this.getInputPeer(var6);
         var5.id = var3;
         var5.silent = var4 ^ true;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, new _$$Lambda$MessagesController$zi8soJuISiGh3kdE6Q_S1wPr31c(this));
      }
   }

   public void processChatInfo(int var1, TLRPC.ChatFull var2, ArrayList var3, boolean var4, boolean var5, boolean var6, MessageObject var7) {
      if (var4 && var1 > 0 && !var6) {
         this.loadFullChat(var1, 0, var5);
      }

      if (var2 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$aj9gT3RqpNCMmoGiSwlydWLbsP8(this, var3, var4, var2, var6, var7));
      }

   }

   public void processDialogsUpdate(TLRPC.messages_Dialogs var1, ArrayList var2) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$q64E9hWX3Xly3JADViQSa2zMJVo(this, var1));
   }

   public void processDialogsUpdateRead(LongSparseArray var1, LongSparseArray var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$_YsSBCaL_qbcH96s3TWAMj3YbZI(this, var1, var2));
   }

   public void processLoadedBlockedUsers(SparseIntArray var1, ArrayList var2, boolean var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$JPDY_4SRGvZvjZ1pDyebULwuKFA(this, var2, var3, var1));
   }

   public void processLoadedChannelAdmins(ArrayList var1, int var2, boolean var3) {
      Collections.sort(var1);
      if (!var3) {
         MessagesStorage.getInstance(this.currentAccount).putChannelAdmins(var2, var1);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$qaVxMxZZgT13ZZ7VOgNBw4U51uM(this, var2, var1, var3));
   }

   public void processLoadedDeleteTask(int var1, ArrayList var2, int var3) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$5rDlON84Ua92gPbhSWYw9Sv_2ig(this, var2, var1));
   }

   public void processLoadedDialogs(TLRPC.messages_Dialogs var1, ArrayList var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, boolean var9) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$shj4tQAJ8i7NH8eiXBAGt48jDcs(this, var3, var6, var1, var7, var5, var2, var4, var9, var8));
   }

   public void processLoadedMessages(TLRPC.messages_Messages var1, long var2, int var4, int var5, int var6, boolean var7, int var8, int var9, int var10, int var11, int var12, int var13, boolean var14, boolean var15, int var16, boolean var17, int var18) {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var19 = new StringBuilder();
         var19.append("processLoadedMessages size ");
         var19.append(var1.messages.size());
         var19.append(" in chat ");
         var19.append(var2);
         var19.append(" count ");
         var19.append(var4);
         var19.append(" max_id ");
         var19.append(var5);
         var19.append(" cache ");
         var19.append(var7);
         var19.append(" guid ");
         var19.append(var8);
         var19.append(" load_type ");
         var19.append(var13);
         var19.append(" last_message_id ");
         var19.append(var10);
         var19.append(" isChannel ");
         var19.append(var14);
         var19.append(" index ");
         var19.append(var16);
         var19.append(" firstUnread ");
         var19.append(var9);
         var19.append(" unread_count ");
         var19.append(var11);
         var19.append(" last_date ");
         var19.append(var12);
         var19.append(" queryFromServer ");
         var19.append(var17);
         FileLog.d(var19.toString());
      }

      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$B3eeeWzbYDkKoJj9YxFxBPuwloE(this, var1, var2, var7, var4, var13, var17, var9, var5, var6, var8, var10, var14, var16, var11, var12, var18, var15));
   }

   public void processLoadedUserPhotos(TLRPC.photos_Photos var1, int var2, int var3, long var4, boolean var6, int var7) {
      if (!var6) {
         MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var1.users, (ArrayList)null, true, true);
         MessagesStorage.getInstance(this.currentAccount).putDialogPhotos(var2, var1);
      } else if (var1 == null || var1.photos.isEmpty()) {
         this.loadDialogPhotos(var2, var3, var4, false, var7);
         return;
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$NAWdcL37DBZ4gWGx1x8HuDp86gI(this, var1, var6, var2, var3, var7));
   }

   protected void processNewChannelDifferenceParams(int var1, int var2, int var3) {
      StringBuilder var4;
      if (BuildVars.LOGS_ENABLED) {
         var4 = new StringBuilder();
         var4.append("processNewChannelDifferenceParams pts = ");
         var4.append(var1);
         var4.append(" pts_count = ");
         var4.append(var2);
         var4.append(" channeldId = ");
         var4.append(var3);
         FileLog.d(var4.toString());
      }

      int var5 = this.channelsPts.get(var3);
      int var6 = var5;
      if (var5 == 0) {
         var5 = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(var3);
         var6 = var5;
         if (var5 == 0) {
            var6 = 1;
         }

         this.channelsPts.put(var3, var6);
      }

      if (var6 + var2 == var1) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("APPLY CHANNEL PTS");
         }

         this.channelsPts.put(var3, var1);
         MessagesStorage.getInstance(this.currentAccount).saveChannelPts(var3, var1);
      } else if (var6 != var1) {
         long var7 = this.updatesStartWaitTimeChannels.get(var3);
         if (!this.gettingDifferenceChannels.get(var3) && var7 != 0L && Math.abs(System.currentTimeMillis() - var7) > 1500L) {
            this.getChannelDifference(var3);
         } else {
            if (BuildVars.LOGS_ENABLED) {
               var4 = new StringBuilder();
               var4.append("ADD CHANNEL UPDATE TO QUEUE pts = ");
               var4.append(var1);
               var4.append(" pts_count = ");
               var4.append(var2);
               FileLog.d(var4.toString());
            }

            if (var7 == 0L) {
               this.updatesStartWaitTimeChannels.put(var3, System.currentTimeMillis());
            }

            MessagesController.UserActionUpdatesPts var9 = new MessagesController.UserActionUpdatesPts();
            var9.pts = var1;
            var9.pts_count = var2;
            var9.chat_id = var3;
            ArrayList var10 = (ArrayList)this.updatesQueueChannels.get(var3);
            ArrayList var11 = var10;
            if (var10 == null) {
               var11 = new ArrayList();
               this.updatesQueueChannels.put(var3, var11);
            }

            var11.add(var9);
         }
      }

   }

   protected void processNewDifferenceParams(int var1, int var2, int var3, int var4) {
      StringBuilder var5;
      if (BuildVars.LOGS_ENABLED) {
         var5 = new StringBuilder();
         var5.append("processNewDifferenceParams seq = ");
         var5.append(var1);
         var5.append(" pts = ");
         var5.append(var2);
         var5.append(" date = ");
         var5.append(var3);
         var5.append(" pts_count = ");
         var5.append(var4);
         FileLog.d(var5.toString());
      }

      if (var2 != -1) {
         if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + var4 == var2) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("APPLY PTS");
            }

            MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var2);
            MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
         } else if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() != var2) {
            if (!this.gettingDifference && this.updatesStartWaitTimePts != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) > 1500L) {
               this.getDifference();
            } else {
               if (BuildVars.LOGS_ENABLED) {
                  var5 = new StringBuilder();
                  var5.append("ADD UPDATE TO QUEUE pts = ");
                  var5.append(var2);
                  var5.append(" pts_count = ");
                  var5.append(var4);
                  FileLog.d(var5.toString());
               }

               if (this.updatesStartWaitTimePts == 0L) {
                  this.updatesStartWaitTimePts = System.currentTimeMillis();
               }

               MessagesController.UserActionUpdatesPts var6 = new MessagesController.UserActionUpdatesPts();
               var6.pts = var2;
               var6.pts_count = var4;
               this.updatesQueuePts.add(var6);
            }
         }
      }

      if (var1 != -1) {
         if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() + 1 == var1) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("APPLY SEQ");
            }

            MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(var1);
            if (var3 != -1) {
               MessagesStorage.getInstance(this.currentAccount).setLastDateValue(var3);
            }

            MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
         } else if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() != var1) {
            if (!this.gettingDifference && this.updatesStartWaitTimeSeq != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeSeq) > 1500L) {
               this.getDifference();
            } else {
               if (BuildVars.LOGS_ENABLED) {
                  var5 = new StringBuilder();
                  var5.append("ADD UPDATE TO QUEUE seq = ");
                  var5.append(var1);
                  FileLog.d(var5.toString());
               }

               if (this.updatesStartWaitTimeSeq == 0L) {
                  this.updatesStartWaitTimeSeq = System.currentTimeMillis();
               }

               MessagesController.UserActionUpdatesSeq var7 = new MessagesController.UserActionUpdatesSeq();
               var7.seq = var1;
               this.updatesQueueSeq.add(var7);
            }
         }
      }

   }

   public boolean processUpdateArray(ArrayList var1, ArrayList var2, ArrayList var3, boolean var4) {
      if (var1.isEmpty()) {
         if (var2 != null || var3 != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$ez4IR3lOLDGJ8ytrBmZ7ykTpLLU(this, var2, var3));
         }

         return true;
      } else {
         long var5 = System.currentTimeMillis();
         ConcurrentHashMap var7;
         int var8;
         int var9;
         boolean var65;
         if (var2 != null) {
            var7 = new ConcurrentHashMap();
            var8 = var2.size();

            for(var9 = 0; var9 < var8; ++var9) {
               TLRPC.User var10 = (TLRPC.User)var2.get(var9);
               var7.put(var10.id, var10);
            }

            var65 = true;
         } else {
            var7 = this.users;
            var65 = false;
         }

         int var11;
         ConcurrentHashMap var67;
         if (var3 != null) {
            var67 = new ConcurrentHashMap();
            var11 = var3.size();

            for(var8 = 0; var8 < var11; ++var8) {
               TLRPC.Chat var12 = (TLRPC.Chat)var3.get(var8);
               var67.put(var12.id, var12);
            }
         } else {
            var67 = this.chats;
            var65 = false;
         }

         if (var4) {
            var65 = false;
         }

         if (var2 != null || var3 != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$geLuQrjkrfEFbaaNVcTD1Sit87o(this, var2, var3));
         }

         int var13 = var1.size();
         ArrayList var14 = null;
         Object var15 = null;
         var11 = 0;
         ArrayList var71 = null;
         ConcurrentHashMap var16 = null;
         var8 = 0;
         SparseLongArray var17 = null;
         boolean var18 = false;
         ArrayList var19 = null;
         LongSparseArray var20 = null;
         SparseLongArray var21 = null;
         LongSparseArray var22 = null;
         Object var23 = null;
         SparseIntArray var24 = null;
         SparseArray var25 = null;
         SparseIntArray var26 = null;
         ArrayList var27 = null;
         var2 = null;
         ArrayList var28 = null;
         ConcurrentHashMap var53 = var67;
         LongSparseArray var69 = var16;

         ConcurrentHashMap var29;
         int var32;
         int var33;
         LongSparseArray var72;
         for(var29 = var7; var8 < var13; var53 = var67) {
            TLRPC.Update var30 = (TLRPC.Update)var1.get(var8);
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var61 = new StringBuilder();
               var61.append("process update ");
               var61.append(var30);
               FileLog.d(var61.toString());
            }

            ArrayList var64;
            Object var68;
            SparseLongArray var73;
            LongSparseArray var87;
            label1262: {
               label1293: {
                  boolean var31 = var30 instanceof TLRPC.TL_updateNewMessage;
                  int var44;
                  Object var70;
                  ArrayList var106;
                  Integer var111;
                  boolean var153;
                  if (!var31) {
                     Object var62 = var15;
                     if (!(var30 instanceof TLRPC.TL_updateNewChannelMessage)) {
                        ConcurrentHashMap var159;
                        label1212: {
                           if (var30 instanceof TLRPC.TL_updateReadMessagesContents) {
                              TLRPC.TL_updateReadMessagesContents var92 = (TLRPC.TL_updateReadMessagesContents)var30;
                              var64 = var14;
                              if (var14 == null) {
                                 var64 = new ArrayList();
                              }

                              var32 = var92.messages.size();

                              for(var33 = 0; var33 < var32; ++var33) {
                                 var64.add((long)(Integer)var92.messages.get(var33));
                              }

                              var16 = var53;
                              var3 = var64;
                           } else {
                              long var35 = var5;
                              ConcurrentHashMap var76;
                              if (!(var30 instanceof TLRPC.TL_updateChannelReadMessagesContents)) {
                                 Integer var89;
                                 Integer var99;
                                 if (!(var30 instanceof TLRPC.TL_updateReadHistoryInbox)) {
                                    Integer var105;
                                    if (var30 instanceof TLRPC.TL_updateReadHistoryOutbox) {
                                       TLRPC.TL_updateReadHistoryOutbox var179 = (TLRPC.TL_updateReadHistoryOutbox)var30;
                                       SparseLongArray var130 = var21;
                                       var21 = var21;
                                       if (var130 == null) {
                                          var21 = new SparseLongArray();
                                       }

                                       TLRPC.Peer var132 = var179.peer;
                                       var33 = var132.chat_id;
                                       if (var33 != 0) {
                                          var21.put(-var33, (long)var179.max_id);
                                          var33 = -var179.peer.chat_id;
                                       } else {
                                          var21.put(var132.user_id, (long)var179.max_id);
                                          var33 = var179.peer.user_id;
                                       }

                                       var5 = (long)var33;
                                       var99 = (Integer)this.dialogs_read_outbox_max.get(var5);
                                       var105 = var99;
                                       if (var99 == null) {
                                          var105 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var5);
                                       }

                                       this.dialogs_read_outbox_max.put(var5, Math.max(var105, var179.max_id));
                                    } else {
                                       ArrayList var78;
                                       ArrayList var123;
                                       if (var30 instanceof TLRPC.TL_updateDeleteMessages) {
                                          TLRPC.TL_updateDeleteMessages var177 = (TLRPC.TL_updateDeleteMessages)var30;
                                          SparseArray var158 = var25;
                                          var25 = var25;
                                          if (var158 == null) {
                                             var25 = new SparseArray();
                                          }

                                          var78 = (ArrayList)var25.get(0);
                                          var123 = var78;
                                          if (var78 == null) {
                                             var123 = new ArrayList();
                                             var25.put(0, var123);
                                          }

                                          var123.addAll(var177.messages);
                                       } else {
                                          var31 = var30 instanceof TLRPC.TL_updateUserTyping;
                                          var33 = var11;
                                          long var38;
                                          long var40;
                                          ArrayList var134;
                                          ArrayList var162;
                                          if (var31 || var30 instanceof TLRPC.TL_updateChatUserTyping) {
                                             TLRPC.SendMessageAction var122;
                                             if (var31) {
                                                TLRPC.TL_updateUserTyping var119 = (TLRPC.TL_updateUserTyping)var30;
                                                var11 = var119.user_id;
                                                var122 = var119.action;
                                                var44 = 0;
                                             } else {
                                                TLRPC.TL_updateChatUserTyping var126 = (TLRPC.TL_updateChatUserTyping)var30;
                                                var11 = var126.chat_id;
                                                var32 = var126.user_id;
                                                var122 = var126.action;
                                                var44 = var11;
                                                var11 = var32;
                                             }

                                             if (var11 != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                var40 = (long)(-var44);
                                                var38 = var40;
                                                if (var40 == 0L) {
                                                   var38 = (long)var11;
                                                }

                                                var162 = (ArrayList)this.printingUsers.get(var38);
                                                if (var122 instanceof TLRPC.TL_sendMessageCancelAction) {
                                                   boolean var46 = var18;
                                                   if (var162 != null) {
                                                      var44 = var162.size();
                                                      var32 = 0;

                                                      while(true) {
                                                         var31 = var18;
                                                         if (var32 >= var44) {
                                                            break;
                                                         }

                                                         if (((MessagesController.PrintingUser)var162.get(var32)).userId == var11) {
                                                            var162.remove(var32);
                                                            var31 = true;
                                                            break;
                                                         }

                                                         ++var32;
                                                      }

                                                      var46 = var31;
                                                      if (var162.isEmpty()) {
                                                         this.printingUsers.remove(var38);
                                                         var46 = var31;
                                                      }
                                                   }

                                                   var18 = var46;
                                                } else {
                                                   var134 = var162;
                                                   if (var162 == null) {
                                                      var134 = new ArrayList();
                                                      this.printingUsers.put(var38, var134);
                                                   }

                                                   Iterator var175 = var134.iterator();

                                                   while(true) {
                                                      if (!var175.hasNext()) {
                                                         var153 = false;
                                                         break;
                                                      }

                                                      MessagesController.PrintingUser var173 = (MessagesController.PrintingUser)var175.next();
                                                      if (var173.userId == var11) {
                                                         var173.lastTime = var5;
                                                         if (var173.action.getClass() != var122.getClass()) {
                                                            var18 = true;
                                                         }

                                                         var173.action = var122;
                                                         var153 = true;
                                                         break;
                                                      }
                                                   }

                                                   if (!var153) {
                                                      MessagesController.PrintingUser var176 = new MessagesController.PrintingUser();
                                                      var176.userId = var11;
                                                      var176.lastTime = var5;
                                                      var176.action = var122;
                                                      var134.add(var176);
                                                      var18 = true;
                                                   }
                                                }

                                                this.onlinePrivacy.put(var11, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                             }

                                             var87 = var69;
                                             var11 = var33;
                                             var67 = var53;
                                             var64 = var71;
                                             break label1293;
                                          }

                                          if (var30 instanceof TLRPC.TL_updateChatParticipants) {
                                             TLRPC.TL_updateChatParticipants var79 = (TLRPC.TL_updateChatParticipants)var30;
                                             var11 |= 32;
                                             var123 = var27;
                                             if (var27 == null) {
                                                var123 = new ArrayList();
                                             }

                                             var123.add(var79.participants);
                                             var27 = var123;
                                          } else if (var30 instanceof TLRPC.TL_updateUserStatus) {
                                             var11 |= 4;
                                             var123 = var2;
                                             if (var2 == null) {
                                                var123 = new ArrayList();
                                             }

                                             var2 = var123;
                                             var123.add(var30);
                                          } else if (var30 instanceof TLRPC.TL_updateUserName) {
                                             var11 |= 1;
                                             var123 = var2;
                                             if (var2 == null) {
                                                var123 = new ArrayList();
                                             }

                                             var2 = var123;
                                             var123.add(var30);
                                          } else if (var30 instanceof TLRPC.TL_updateUserPhoto) {
                                             TLRPC.TL_updateUserPhoto var141 = (TLRPC.TL_updateUserPhoto)var30;
                                             var11 |= 2;
                                             MessagesStorage.getInstance(this.currentAccount).clearUserPhotos(var141.user_id);
                                             var123 = var2;
                                             if (var2 == null) {
                                                var123 = new ArrayList();
                                             }

                                             var2 = var123;
                                             var123.add(var30);
                                          } else if (var30 instanceof TLRPC.TL_updateUserPhone) {
                                             var11 |= 1024;
                                             var123 = var2;
                                             if (var2 == null) {
                                                var123 = new ArrayList();
                                             }

                                             var2 = var123;
                                             var123.add(var30);
                                          } else {
                                             if (!(var30 instanceof TLRPC.TL_updateContactLink)) {
                                                label1142: {
                                                   label1141: {
                                                      Object var77;
                                                      label1140: {
                                                         MessageObject var167;
                                                         if (var30 instanceof TLRPC.TL_updateNewEncryptedMessage) {
                                                            SecretChatHelper var148 = SecretChatHelper.getInstance(this.currentAccount);
                                                            TLRPC.TL_updateNewEncryptedMessage var85 = (TLRPC.TL_updateNewEncryptedMessage)var30;
                                                            var162 = var148.decryptMessage(var85.message);
                                                            if (var162 != null && !var162.isEmpty()) {
                                                               var5 = (long)var85.message.chat_id << 32;
                                                               LongSparseArray var149 = var69;
                                                               if (var69 == null) {
                                                                  var149 = new LongSparseArray();
                                                               }

                                                               ArrayList var75 = (ArrayList)var149.get(var5);
                                                               var78 = var75;
                                                               if (var75 == null) {
                                                                  var78 = new ArrayList();
                                                                  var149.put(var5, var78);
                                                               }

                                                               var11 = var162.size();

                                                               for(var32 = 0; var32 < var11; var62 = var70) {
                                                                  TLRPC.Message var165 = (TLRPC.Message)var162.get(var32);
                                                                  ImageLoader.saveMessageThumbs(var165);
                                                                  var75 = var19;
                                                                  if (var19 == null) {
                                                                     var75 = new ArrayList();
                                                                  }

                                                                  var75.add(var165);
                                                                  var19 = var75;
                                                                  var167 = new MessageObject(this.currentAccount, var165, var29, var53, this.createdDialogIds.contains(var5));
                                                                  var78.add(var167);
                                                                  if (var62 == null) {
                                                                     var70 = new ArrayList();
                                                                  } else {
                                                                     var70 = var62;
                                                                  }

                                                                  ((ArrayList)var70).add(var167);
                                                                  ++var32;
                                                               }

                                                               var78 = var14;
                                                               var11 = var8;
                                                               var69 = var149;
                                                               var14 = var71;
                                                               var8 = var33;
                                                               var71 = var78;
                                                            } else {
                                                               var123 = var14;
                                                               var14 = var71;
                                                               var11 = var8;
                                                               var71 = var123;
                                                               var8 = var33;
                                                            }

                                                            var21 = var21;
                                                            var162 = var71;
                                                            var33 = var8;
                                                            var5 = var5;
                                                            var71 = var14;
                                                         } else {
                                                            var162 = var14;
                                                            if (!(var30 instanceof TLRPC.TL_updateEncryptedChatTyping)) {
                                                               SparseIntArray var144;
                                                               label1358: {
                                                                  var21 = var21;
                                                                  SparseIntArray var95;
                                                                  ArrayList var135;
                                                                  if (var30 instanceof TLRPC.TL_updateEncryptedMessagesRead) {
                                                                     var15 = (TLRPC.TL_updateEncryptedMessagesRead)var30;
                                                                     var95 = var24;
                                                                     if (var24 == null) {
                                                                        var95 = new SparseIntArray();
                                                                     }

                                                                     var95.put(((TLRPC.TL_updateEncryptedMessagesRead)var15).chat_id, ((TLRPC.TL_updateEncryptedMessagesRead)var15).max_date);
                                                                     var135 = var28;
                                                                     if (var28 == null) {
                                                                        var135 = new ArrayList();
                                                                     }

                                                                     var135.add(var15);
                                                                  } else {
                                                                     label1344: {
                                                                        label1107: {
                                                                           label1106: {
                                                                              Object var163;
                                                                              label1105: {
                                                                                 label1104: {
                                                                                    if (var30 instanceof TLRPC.TL_updateChatParticipantAdd) {
                                                                                       TLRPC.TL_updateChatParticipantAdd var90 = (TLRPC.TL_updateChatParticipantAdd)var30;
                                                                                       MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var90.chat_id, var90.user_id, 0, var90.inviter_id, var90.version);
                                                                                    } else if (var30 instanceof TLRPC.TL_updateChatParticipantDelete) {
                                                                                       TLRPC.TL_updateChatParticipantDelete var94 = (TLRPC.TL_updateChatParticipantDelete)var30;
                                                                                       MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var94.chat_id, var94.user_id, 1, 0, var94.version);
                                                                                    } else {
                                                                                       if (var30 instanceof TLRPC.TL_updateDcOptions || var30 instanceof TLRPC.TL_updateConfig) {
                                                                                          ConnectionsManager.getInstance(this.currentAccount).updateDcSettings();
                                                                                          var76 = var53;
                                                                                          var72 = var20;
                                                                                          break label1142;
                                                                                       }

                                                                                       if (var30 instanceof TLRPC.TL_updateEncryption) {
                                                                                          SecretChatHelper.getInstance(this.currentAccount).processUpdateEncryption((TLRPC.TL_updateEncryption)var30, var29);
                                                                                       } else {
                                                                                          if (!(var30 instanceof TLRPC.TL_updateUserBlocked)) {
                                                                                             if (var30 instanceof TLRPC.TL_updateNotifySettings) {
                                                                                                var14 = var2;
                                                                                                if (var2 == null) {
                                                                                                   var14 = new ArrayList();
                                                                                                }

                                                                                                var14.add(var30);
                                                                                                var2 = var14;
                                                                                                var135 = var28;
                                                                                                var95 = var24;
                                                                                                break label1344;
                                                                                             }

                                                                                             if (var30 instanceof TLRPC.TL_updateServiceNotification) {
                                                                                                TLRPC.TL_updateServiceNotification var156 = (TLRPC.TL_updateServiceNotification)var30;
                                                                                                if (var156.popup) {
                                                                                                   String var110 = var156.message;
                                                                                                   if (var110 != null && var110.length() > 0) {
                                                                                                      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$jWG8NvZquHH3NCs4zu__Fuu7gvw(this, var156));
                                                                                                   }
                                                                                                }

                                                                                                if ((var156.flags & 2) != 0) {
                                                                                                   TLRPC.TL_message var138 = new TLRPC.TL_message();
                                                                                                   var11 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                                                                                                   var138.id = var11;
                                                                                                   var138.local_id = var11;
                                                                                                   UserConfig.getInstance(this.currentAccount).saveConfig(false);
                                                                                                   var138.unread = true;
                                                                                                   var138.flags = 256;
                                                                                                   var11 = var156.inbox_date;
                                                                                                   if (var11 != 0) {
                                                                                                      var138.date = var11;
                                                                                                   } else {
                                                                                                      var138.date = (int)(System.currentTimeMillis() / 1000L);
                                                                                                   }

                                                                                                   var138.from_id = 777000;
                                                                                                   var138.to_id = new TLRPC.TL_peerUser();
                                                                                                   var138.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                                   var138.dialog_id = 777000L;
                                                                                                   TLRPC.MessageMedia var113 = var156.media;
                                                                                                   if (var113 != null) {
                                                                                                      var138.media = var113;
                                                                                                      var138.flags |= 512;
                                                                                                   }

                                                                                                   var138.message = var156.message;
                                                                                                   var14 = var156.entities;
                                                                                                   if (var14 != null) {
                                                                                                      var138.entities = var14;
                                                                                                   }

                                                                                                   var14 = var19;
                                                                                                   if (var19 == null) {
                                                                                                      var14 = new ArrayList();
                                                                                                   }

                                                                                                   var14.add(var138);
                                                                                                   var11 = this.currentAccount;
                                                                                                   var31 = this.createdDialogIds.contains(var138.dialog_id);
                                                                                                   var19 = var14;
                                                                                                   var167 = new MessageObject(var11, var138, var29, var53, var31);
                                                                                                   if (var69 == null) {
                                                                                                      var69 = new LongSparseArray();
                                                                                                   }

                                                                                                   var78 = (ArrayList)var69.get(var138.dialog_id);
                                                                                                   var14 = var78;
                                                                                                   if (var78 == null) {
                                                                                                      var14 = new ArrayList();
                                                                                                      var69.put(var138.dialog_id, var14);
                                                                                                   }

                                                                                                   var14.add(var167);
                                                                                                   Object var166;
                                                                                                   if (var15 == null) {
                                                                                                      var166 = new ArrayList();
                                                                                                   } else {
                                                                                                      var166 = var15;
                                                                                                   }

                                                                                                   ((ArrayList)var166).add(var167);
                                                                                                   var62 = var166;
                                                                                                }
                                                                                                break label1106;
                                                                                             }

                                                                                             if (var30 instanceof TLRPC.TL_updateDialogPinned) {
                                                                                                var14 = var2;
                                                                                                if (var2 == null) {
                                                                                                   var14 = new ArrayList();
                                                                                                }

                                                                                                var2 = var14;
                                                                                                var14.add(var30);
                                                                                                break label1106;
                                                                                             }

                                                                                             if (var30 instanceof TLRPC.TL_updatePinnedDialogs) {
                                                                                                var14 = var2;
                                                                                                if (var2 == null) {
                                                                                                   var14 = new ArrayList();
                                                                                                }

                                                                                                var2 = var14;
                                                                                                var14.add(var30);
                                                                                                break label1106;
                                                                                             }

                                                                                             if (var30 instanceof TLRPC.TL_updateFolderPeers) {
                                                                                                var14 = var2;
                                                                                                if (var2 == null) {
                                                                                                   var14 = new ArrayList();
                                                                                                }

                                                                                                var2 = var14;
                                                                                                var14.add(var30);
                                                                                                TLRPC.TL_updateFolderPeers var152 = (TLRPC.TL_updateFolderPeers)var30;
                                                                                                MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId(var152.folder_peers, (ArrayList)null, 0L, 0);
                                                                                                break label1106;
                                                                                             }

                                                                                             if (var30 instanceof TLRPC.TL_updatePrivacy) {
                                                                                                var14 = var2;
                                                                                                if (var2 == null) {
                                                                                                   var14 = new ArrayList();
                                                                                                }

                                                                                                var2 = var14;
                                                                                                var14.add(var30);
                                                                                                break label1106;
                                                                                             }

                                                                                             LongSparseArray var97;
                                                                                             TLRPC.WebPage var124;
                                                                                             if (var30 instanceof TLRPC.TL_updateWebPage) {
                                                                                                TLRPC.TL_updateWebPage var96 = (TLRPC.TL_updateWebPage)var30;
                                                                                                var97 = var22;
                                                                                                if (var22 == null) {
                                                                                                   var97 = new LongSparseArray();
                                                                                                }

                                                                                                var124 = var96.webpage;
                                                                                                var97.put(var124.id, var124);
                                                                                             } else {
                                                                                                if (!(var30 instanceof TLRPC.TL_updateChannelWebPage)) {
                                                                                                   TLRPC.Chat var86;
                                                                                                   StringBuilder var115;
                                                                                                   TLRPC.Chat var129;
                                                                                                   if (var30 instanceof TLRPC.TL_updateChannelTooLong) {
                                                                                                      TLRPC.TL_updateChannelTooLong var137 = (TLRPC.TL_updateChannelTooLong)var30;
                                                                                                      if (BuildVars.LOGS_ENABLED) {
                                                                                                         var115 = new StringBuilder();
                                                                                                         var115.append(var30);
                                                                                                         var115.append(" channelId = ");
                                                                                                         var115.append(var137.channel_id);
                                                                                                         FileLog.d(var115.toString());
                                                                                                      }

                                                                                                      var11 = this.channelsPts.get(var137.channel_id);
                                                                                                      if (var11 == 0) {
                                                                                                         var32 = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(var137.channel_id);
                                                                                                         if (var32 != 0) {
                                                                                                            this.channelsPts.put(var137.channel_id, var32);
                                                                                                            var11 = var32;
                                                                                                         } else {
                                                                                                            label1012: {
                                                                                                               var86 = (TLRPC.Chat)var53.get(var137.channel_id);
                                                                                                               if (var86 != null) {
                                                                                                                  var129 = var86;
                                                                                                                  if (!var86.min) {
                                                                                                                     break label1012;
                                                                                                                  }
                                                                                                               }

                                                                                                               var129 = this.getChat(var137.channel_id);
                                                                                                            }

                                                                                                            label1007: {
                                                                                                               if (var129 != null) {
                                                                                                                  var86 = var129;
                                                                                                                  if (!var129.min) {
                                                                                                                     break label1007;
                                                                                                                  }
                                                                                                               }

                                                                                                               var86 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var137.channel_id);
                                                                                                               this.putChat(var86, true);
                                                                                                            }

                                                                                                            var11 = var32;
                                                                                                            if (var86 != null) {
                                                                                                               var11 = var32;
                                                                                                               if (!var86.min) {
                                                                                                                  this.loadUnknownChannel(var86, 0L);
                                                                                                                  var11 = var32;
                                                                                                               }
                                                                                                            }
                                                                                                         }
                                                                                                      }

                                                                                                      if (var11 != 0) {
                                                                                                         if ((var137.flags & 1) != 0) {
                                                                                                            if (var137.pts > var11) {
                                                                                                               this.getChannelDifference(var137.channel_id);
                                                                                                            }
                                                                                                         } else {
                                                                                                            this.getChannelDifference(var137.channel_id);
                                                                                                         }
                                                                                                      }

                                                                                                      var76 = var53;
                                                                                                      break label1104;
                                                                                                   }

                                                                                                   SparseLongArray var180;
                                                                                                   if (var30 instanceof TLRPC.TL_updateReadChannelInbox) {
                                                                                                      TLRPC.TL_updateReadChannelInbox var174 = (TLRPC.TL_updateReadChannelInbox)var30;
                                                                                                      var40 = (long)var174.max_id;
                                                                                                      var33 = var174.channel_id;
                                                                                                      var38 = (long)var33;
                                                                                                      var5 = (long)(-var33);
                                                                                                      var180 = var17;
                                                                                                      if (var17 == null) {
                                                                                                         var180 = new SparseLongArray();
                                                                                                      }

                                                                                                      var180.put(-var174.channel_id, var40 | var38 << 32);
                                                                                                      var105 = (Integer)this.dialogs_read_inbox_max.get(var5);
                                                                                                      var89 = var105;
                                                                                                      if (var105 == null) {
                                                                                                         var89 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, var5);
                                                                                                      }

                                                                                                      this.dialogs_read_inbox_max.put(var5, Math.max(var89, var174.max_id));
                                                                                                      var17 = var180;
                                                                                                      break label1141;
                                                                                                   }

                                                                                                   if (var30 instanceof TLRPC.TL_updateReadChannelOutbox) {
                                                                                                      TLRPC.TL_updateReadChannelOutbox var171 = (TLRPC.TL_updateReadChannelOutbox)var30;
                                                                                                      var5 = (long)var171.max_id;
                                                                                                      var33 = var171.channel_id;
                                                                                                      var38 = (long)var33;
                                                                                                      var40 = (long)(-var33);
                                                                                                      if (var21 == null) {
                                                                                                         var180 = new SparseLongArray();
                                                                                                      } else {
                                                                                                         var180 = var21;
                                                                                                      }

                                                                                                      var180.put(-var171.channel_id, var38 << 32 | var5);
                                                                                                      var105 = (Integer)this.dialogs_read_outbox_max.get(var40);
                                                                                                      var111 = var105;
                                                                                                      if (var105 == null) {
                                                                                                         var111 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(true, var40);
                                                                                                      }

                                                                                                      this.dialogs_read_outbox_max.put(var40, Math.max(var111, var171.max_id));
                                                                                                      var21 = var180;
                                                                                                      break label1141;
                                                                                                   }

                                                                                                   StringBuilder var164;
                                                                                                   if (var30 instanceof TLRPC.TL_updateDeleteChannelMessages) {
                                                                                                      TLRPC.TL_updateDeleteChannelMessages var170 = (TLRPC.TL_updateDeleteChannelMessages)var30;
                                                                                                      if (BuildVars.LOGS_ENABLED) {
                                                                                                         var164 = new StringBuilder();
                                                                                                         var164.append(var30);
                                                                                                         var164.append(" channelId = ");
                                                                                                         var164.append(var170.channel_id);
                                                                                                         FileLog.d(var164.toString());
                                                                                                      }

                                                                                                      SparseArray var178;
                                                                                                      if (var25 == null) {
                                                                                                         var178 = new SparseArray();
                                                                                                      } else {
                                                                                                         var178 = var25;
                                                                                                      }

                                                                                                      var78 = (ArrayList)var178.get(var170.channel_id);
                                                                                                      ArrayList var147 = var78;
                                                                                                      if (var78 == null) {
                                                                                                         var147 = new ArrayList();
                                                                                                         var178.put(var170.channel_id, var147);
                                                                                                      }

                                                                                                      var147.addAll(var170.messages);
                                                                                                      var25 = var178;
                                                                                                      break label1141;
                                                                                                   }

                                                                                                   ArrayList var131;
                                                                                                   if (var30 instanceof TLRPC.TL_updateChannel) {
                                                                                                      if (BuildVars.LOGS_ENABLED) {
                                                                                                         TLRPC.TL_updateChannel var172 = (TLRPC.TL_updateChannel)var30;
                                                                                                         StringBuilder var101 = new StringBuilder();
                                                                                                         var101.append(var30);
                                                                                                         var101.append(" channelId = ");
                                                                                                         var101.append(var172.channel_id);
                                                                                                         FileLog.d(var101.toString());
                                                                                                      }

                                                                                                      var131 = var2;
                                                                                                      if (var2 == null) {
                                                                                                         var131 = new ArrayList();
                                                                                                      }

                                                                                                      var131.add(var30);
                                                                                                      var2 = var131;
                                                                                                      break label1141;
                                                                                                   }

                                                                                                   if (var30 instanceof TLRPC.TL_updateChannelMessageViews) {
                                                                                                      TLRPC.TL_updateChannelMessageViews var168 = (TLRPC.TL_updateChannelMessageViews)var30;
                                                                                                      if (BuildVars.LOGS_ENABLED) {
                                                                                                         var164 = new StringBuilder();
                                                                                                         var164.append(var30);
                                                                                                         var164.append(" channelId = ");
                                                                                                         var164.append(var168.channel_id);
                                                                                                         FileLog.d(var164.toString());
                                                                                                      }

                                                                                                      var163 = var23;
                                                                                                      if (var23 == null) {
                                                                                                         var163 = new SparseArray();
                                                                                                      }

                                                                                                      SparseIntArray var98 = (SparseIntArray)((SparseArray)var163).get(var168.channel_id);
                                                                                                      SparseIntArray var139 = var98;
                                                                                                      if (var98 == null) {
                                                                                                         var139 = new SparseIntArray();
                                                                                                         ((SparseArray)var163).put(var168.channel_id, var139);
                                                                                                      }

                                                                                                      var139.put(var168.id, var168.views);
                                                                                                      break label1105;
                                                                                                   }

                                                                                                   label1360: {
                                                                                                      if (var30 instanceof TLRPC.TL_updateChatParticipantAdmin) {
                                                                                                         TLRPC.TL_updateChatParticipantAdmin var146 = (TLRPC.TL_updateChatParticipantAdmin)var30;
                                                                                                         MessagesStorage.getInstance(this.currentAccount).updateChatInfo(var146.chat_id, var146.user_id, 2, var146.is_admin, var146.version);
                                                                                                      } else {
                                                                                                         if (var30 instanceof TLRPC.TL_updateChatDefaultBannedRights) {
                                                                                                            TLRPC.TL_updateChatDefaultBannedRights var160 = (TLRPC.TL_updateChatDefaultBannedRights)var30;
                                                                                                            var23 = var160.peer;
                                                                                                            var33 = ((TLRPC.Peer)var23).channel_id;
                                                                                                            if (var33 == 0) {
                                                                                                               var33 = ((TLRPC.Peer)var23).chat_id;
                                                                                                            }

                                                                                                            MessagesStorage.getInstance(this.currentAccount).updateChatDefaultBannedRights(var33, var160.default_banned_rights, var160.version);
                                                                                                            var131 = var2;
                                                                                                            if (var2 == null) {
                                                                                                               var131 = new ArrayList();
                                                                                                            }

                                                                                                            var2 = var131;
                                                                                                            var131.add(var30);
                                                                                                            break label1360;
                                                                                                         }

                                                                                                         if (var30 instanceof TLRPC.TL_updateStickerSets) {
                                                                                                            var131 = var2;
                                                                                                            if (var2 == null) {
                                                                                                               var131 = new ArrayList();
                                                                                                            }

                                                                                                            var2 = var131;
                                                                                                            var131.add(var30);
                                                                                                            break label1360;
                                                                                                         }

                                                                                                         if (var30 instanceof TLRPC.TL_updateStickerSetsOrder) {
                                                                                                            var131 = var2;
                                                                                                            if (var2 == null) {
                                                                                                               var131 = new ArrayList();
                                                                                                            }

                                                                                                            var2 = var131;
                                                                                                            var131.add(var30);
                                                                                                            break label1360;
                                                                                                         }

                                                                                                         if (var30 instanceof TLRPC.TL_updateNewStickerSet) {
                                                                                                            var131 = var2;
                                                                                                            if (var2 == null) {
                                                                                                               var131 = new ArrayList();
                                                                                                            }

                                                                                                            var2 = var131;
                                                                                                            var131.add(var30);
                                                                                                            break label1360;
                                                                                                         }

                                                                                                         if (var30 instanceof TLRPC.TL_updateDraftMessage) {
                                                                                                            var131 = var2;
                                                                                                            if (var2 == null) {
                                                                                                               var131 = new ArrayList();
                                                                                                            }

                                                                                                            var2 = var131;
                                                                                                            var131.add(var30);
                                                                                                            break label1360;
                                                                                                         }

                                                                                                         if (var30 instanceof TLRPC.TL_updateSavedGifs) {
                                                                                                            var131 = var2;
                                                                                                            if (var2 == null) {
                                                                                                               var131 = new ArrayList();
                                                                                                            }

                                                                                                            var2 = var131;
                                                                                                            var131.add(var30);
                                                                                                            break label1360;
                                                                                                         }

                                                                                                         var31 = var30 instanceof TLRPC.TL_updateEditChannelMessage;
                                                                                                         if (var31 || var30 instanceof TLRPC.TL_updateEditMessage) {
                                                                                                            var32 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                                            TLRPC.Message var91;
                                                                                                            if (var31) {
                                                                                                               TLRPC.Message var114 = ((TLRPC.TL_updateEditChannelMessage)var30).message;
                                                                                                               var86 = (TLRPC.Chat)var53.get(var114.to_id.channel_id);
                                                                                                               var129 = var86;
                                                                                                               if (var86 == null) {
                                                                                                                  var129 = this.getChat(var114.to_id.channel_id);
                                                                                                               }

                                                                                                               var86 = var129;
                                                                                                               if (var129 == null) {
                                                                                                                  var86 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var114.to_id.channel_id);
                                                                                                                  this.putChat(var86, true);
                                                                                                               }

                                                                                                               if (var86 != null && var86.megagroup) {
                                                                                                                  var114.flags |= Integer.MIN_VALUE;
                                                                                                               }

                                                                                                               var91 = var114;
                                                                                                            } else {
                                                                                                               var91 = ((TLRPC.TL_updateEditMessage)var30).message;
                                                                                                               if (var91.dialog_id == (long)var32) {
                                                                                                                  var91.unread = false;
                                                                                                                  var91.media_unread = false;
                                                                                                                  var91.out = true;
                                                                                                               }
                                                                                                            }

                                                                                                            if (!var91.out && var91.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                                                               var91.out = true;
                                                                                                            }

                                                                                                            if (!var4) {
                                                                                                               var44 = var91.entities.size();

                                                                                                               for(var11 = 0; var11 < var44; ++var11) {
                                                                                                                  TLRPC.MessageEntity var133 = (TLRPC.MessageEntity)var91.entities.get(var11);
                                                                                                                  if (var133 instanceof TLRPC.TL_messageEntityMentionName) {
                                                                                                                     TLRPC.User var118;
                                                                                                                     TLRPC.User var136;
                                                                                                                     int var182;
                                                                                                                     label981: {
                                                                                                                        var182 = ((TLRPC.TL_messageEntityMentionName)var133).user_id;
                                                                                                                        var118 = (TLRPC.User)var29.get(var182);
                                                                                                                        if (var118 != null) {
                                                                                                                           var136 = var118;
                                                                                                                           if (!var118.min) {
                                                                                                                              break label981;
                                                                                                                           }
                                                                                                                        }

                                                                                                                        var136 = this.getUser(var182);
                                                                                                                     }

                                                                                                                     label1329: {
                                                                                                                        if (var136 != null) {
                                                                                                                           var118 = var136;
                                                                                                                           if (!var136.min) {
                                                                                                                              break label1329;
                                                                                                                           }
                                                                                                                        }

                                                                                                                        var136 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var182);
                                                                                                                        if (var136 != null && var136.min) {
                                                                                                                           var136 = null;
                                                                                                                        }

                                                                                                                        this.putUser(var136, true);
                                                                                                                        var118 = var136;
                                                                                                                     }

                                                                                                                     if (var118 == null) {
                                                                                                                        return false;
                                                                                                                     }
                                                                                                                  }
                                                                                                               }
                                                                                                            }

                                                                                                            TLRPC.Peer var143 = var91.to_id;
                                                                                                            var11 = var143.chat_id;
                                                                                                            if (var11 != 0) {
                                                                                                               var91.dialog_id = (long)(-var11);
                                                                                                            } else {
                                                                                                               var11 = var143.channel_id;
                                                                                                               if (var11 != 0) {
                                                                                                                  var91.dialog_id = (long)(-var11);
                                                                                                               } else {
                                                                                                                  if (var143.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                                                                     var91.to_id.user_id = var91.from_id;
                                                                                                                  }

                                                                                                                  var91.dialog_id = (long)var91.to_id.user_id;
                                                                                                               }
                                                                                                            }

                                                                                                            if (var91.out) {
                                                                                                               var76 = this.dialogs_read_outbox_max;
                                                                                                            } else {
                                                                                                               var76 = this.dialogs_read_inbox_max;
                                                                                                            }

                                                                                                            Integer var169 = (Integer)var76.get(var91.dialog_id);
                                                                                                            var99 = var169;
                                                                                                            if (var169 == null) {
                                                                                                               var99 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var91.out, var91.dialog_id);
                                                                                                               var76.put(var91.dialog_id, var99);
                                                                                                            }

                                                                                                            if (var99 < var91.id) {
                                                                                                               var31 = true;
                                                                                                            } else {
                                                                                                               var31 = false;
                                                                                                            }

                                                                                                            var91.unread = var31;
                                                                                                            if (var91.dialog_id == (long)var32) {
                                                                                                               var91.out = true;
                                                                                                               var91.unread = false;
                                                                                                               var91.media_unread = false;
                                                                                                            }

                                                                                                            if (var91.out && var91.message == null) {
                                                                                                               var91.message = "";
                                                                                                               var91.attachPath = "";
                                                                                                            }

                                                                                                            ImageLoader.saveMessageThumbs(var91);
                                                                                                            var11 = this.currentAccount;
                                                                                                            var31 = this.createdDialogIds.contains(var91.dialog_id);
                                                                                                            var76 = var53;
                                                                                                            var167 = new MessageObject(var11, var91, var29, var53, var31);
                                                                                                            var72 = var20;
                                                                                                            if (var20 == null) {
                                                                                                               var72 = new LongSparseArray();
                                                                                                            }

                                                                                                            var134 = (ArrayList)var72.get(var91.dialog_id);
                                                                                                            var106 = var134;
                                                                                                            if (var134 == null) {
                                                                                                               var106 = new ArrayList();
                                                                                                               var72.put(var91.dialog_id, var106);
                                                                                                            }

                                                                                                            var106.add(var167);
                                                                                                            break label1142;
                                                                                                         }

                                                                                                         if (var30 instanceof TLRPC.TL_updateChannelPinnedMessage) {
                                                                                                            TLRPC.TL_updateChannelPinnedMessage var84 = (TLRPC.TL_updateChannelPinnedMessage)var30;
                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                               var115 = new StringBuilder();
                                                                                                               var115.append(var30);
                                                                                                               var115.append(" channelId = ");
                                                                                                               var115.append(var84.channel_id);
                                                                                                               FileLog.d(var115.toString());
                                                                                                            }

                                                                                                            MessagesStorage.getInstance(this.currentAccount).updateChatPinnedMessage(var84.channel_id, var84.id);
                                                                                                         } else if (var30 instanceof TLRPC.TL_updateChatPinnedMessage) {
                                                                                                            TLRPC.TL_updateChatPinnedMessage var117 = (TLRPC.TL_updateChatPinnedMessage)var30;
                                                                                                            MessagesStorage.getInstance(this.currentAccount).updateChatPinnedMessage(var117.chat_id, var117.id);
                                                                                                         } else if (var30 instanceof TLRPC.TL_updateUserPinnedMessage) {
                                                                                                            TLRPC.TL_updateUserPinnedMessage var121 = (TLRPC.TL_updateUserPinnedMessage)var30;
                                                                                                            MessagesStorage.getInstance(this.currentAccount).updateUserPinnedMessage(var121.user_id, var121.id);
                                                                                                         } else {
                                                                                                            if (var30 instanceof TLRPC.TL_updateReadFeaturedStickers) {
                                                                                                               var131 = var2;
                                                                                                               if (var2 == null) {
                                                                                                                  var131 = new ArrayList();
                                                                                                               }

                                                                                                               var2 = var131;
                                                                                                               var131.add(var30);
                                                                                                               break label1360;
                                                                                                            }

                                                                                                            if (var30 instanceof TLRPC.TL_updatePhoneCall) {
                                                                                                               var131 = var2;
                                                                                                               if (var2 == null) {
                                                                                                                  var131 = new ArrayList();
                                                                                                               }

                                                                                                               var2 = var131;
                                                                                                               var131.add(var30);
                                                                                                               break label1360;
                                                                                                            }

                                                                                                            if (var30 instanceof TLRPC.TL_updateLangPack) {
                                                                                                               AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$JHJ_M64u5jSxCVF9v5FzZPiyZZ4(this, (TLRPC.TL_updateLangPack)var30));
                                                                                                            } else {
                                                                                                               if (!(var30 instanceof TLRPC.TL_updateLangPackTooLong)) {
                                                                                                                  if (var30 instanceof TLRPC.TL_updateFavedStickers) {
                                                                                                                     var131 = var2;
                                                                                                                     if (var2 == null) {
                                                                                                                        var131 = new ArrayList();
                                                                                                                     }

                                                                                                                     var2 = var131;
                                                                                                                     var131.add(var30);
                                                                                                                  } else {
                                                                                                                     if (!(var30 instanceof TLRPC.TL_updateContactsReset)) {
                                                                                                                        if (var30 instanceof TLRPC.TL_updateChannelAvailableMessages) {
                                                                                                                           var23 = (TLRPC.TL_updateChannelAvailableMessages)var30;
                                                                                                                           var144 = var26;
                                                                                                                           if (var26 == null) {
                                                                                                                              var144 = new SparseIntArray();
                                                                                                                           }

                                                                                                                           var33 = var144.get(((TLRPC.TL_updateChannelAvailableMessages)var23).channel_id);
                                                                                                                           if (var33 == 0 || var33 < ((TLRPC.TL_updateChannelAvailableMessages)var23).available_min_id) {
                                                                                                                              var144.put(((TLRPC.TL_updateChannelAvailableMessages)var23).channel_id, ((TLRPC.TL_updateChannelAvailableMessages)var23).available_min_id);
                                                                                                                           }
                                                                                                                           break label1358;
                                                                                                                        }

                                                                                                                        if (var30 instanceof TLRPC.TL_updateDialogUnreadMark) {
                                                                                                                           var131 = var2;
                                                                                                                           if (var2 == null) {
                                                                                                                              var131 = new ArrayList();
                                                                                                                           }

                                                                                                                           var2 = var131;
                                                                                                                           var131.add(var30);
                                                                                                                           break label1107;
                                                                                                                        }

                                                                                                                        if (var30 instanceof TLRPC.TL_updateMessagePoll) {
                                                                                                                           TLRPC.TL_updateMessagePoll var181 = (TLRPC.TL_updateMessagePoll)var30;
                                                                                                                           var38 = SendMessagesHelper.getInstance(this.currentAccount).getVoteSendTime(var181.poll_id);
                                                                                                                           if (Math.abs(SystemClock.uptimeMillis() - var38) >= 600L) {
                                                                                                                              MessagesStorage.getInstance(this.currentAccount).updateMessagePollResults(var181.poll_id, var181.poll, var181.results);
                                                                                                                              if (var2 == null) {
                                                                                                                                 var2 = new ArrayList();
                                                                                                                              }

                                                                                                                              var2.add(var30);
                                                                                                                              break label1107;
                                                                                                                           }
                                                                                                                        }

                                                                                                                        var76 = var53;
                                                                                                                        break label1104;
                                                                                                                     }

                                                                                                                     var131 = var2;
                                                                                                                     if (var2 == null) {
                                                                                                                        var131 = new ArrayList();
                                                                                                                     }

                                                                                                                     var2 = var131;
                                                                                                                     var131.add(var30);
                                                                                                                  }
                                                                                                                  break label1360;
                                                                                                               }

                                                                                                               TLRPC.TL_updateLangPackTooLong var125 = (TLRPC.TL_updateLangPackTooLong)var30;
                                                                                                               LocaleController.getInstance().reloadCurrentRemoteLocale(this.currentAccount, var125.lang_code);
                                                                                                            }
                                                                                                         }
                                                                                                      }

                                                                                                      var76 = var53;
                                                                                                      var72 = var20;
                                                                                                      break label1142;
                                                                                                   }

                                                                                                   var163 = var23;
                                                                                                   break label1105;
                                                                                                }

                                                                                                TLRPC.TL_updateChannelWebPage var127 = (TLRPC.TL_updateChannelWebPage)var30;
                                                                                                var97 = var22;
                                                                                                if (var22 == null) {
                                                                                                   var97 = new LongSparseArray();
                                                                                                }

                                                                                                var124 = var127.webpage;
                                                                                                var97.put(var124.id, var124);
                                                                                             }

                                                                                             var22 = var97;
                                                                                             break label1106;
                                                                                          }

                                                                                          TLRPC.TL_updateUserBlocked var80 = (TLRPC.TL_updateUserBlocked)var30;
                                                                                          if (var80.blocked) {
                                                                                             var95 = new SparseIntArray();
                                                                                             var95.put(var80.user_id, 1);
                                                                                             MessagesStorage.getInstance(this.currentAccount).putBlockedUsers(var95, false);
                                                                                          } else {
                                                                                             MessagesStorage.getInstance(this.currentAccount).deleteBlockedUser(var80.user_id);
                                                                                          }

                                                                                          MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$0Px26KfS0c0TW2u9ZjYkQVOvQKA(this, var80));
                                                                                       }
                                                                                    }

                                                                                    var76 = var53;
                                                                                 }

                                                                                 var72 = var20;
                                                                                 break label1142;
                                                                              }

                                                                              var23 = var163;
                                                                              break label1141;
                                                                           }

                                                                           var11 = var11;
                                                                           var14 = var14;
                                                                           var77 = var62;
                                                                           var64 = var71;
                                                                           break label1140;
                                                                        }

                                                                        var144 = var26;
                                                                        break label1358;
                                                                     }
                                                                  }

                                                                  var28 = var135;
                                                                  var24 = var95;
                                                                  var5 = var5;
                                                                  var11 = var11;
                                                                  var14 = var14;
                                                                  var64 = var71;
                                                                  var77 = var15;
                                                                  break label1140;
                                                               }

                                                               var23 = var23;
                                                               var26 = var144;
                                                               break label1141;
                                                            }

                                                            TLRPC.TL_updateEncryptedChatTyping var83 = (TLRPC.TL_updateEncryptedChatTyping)var30;
                                                            TLRPC.EncryptedChat var93 = this.getEncryptedChatDB(var83.chat_id, true);
                                                            if (var93 == null) {
                                                               var21 = var21;
                                                            } else {
                                                               var38 = (long)var83.chat_id << 32;
                                                               var123 = (ArrayList)this.printingUsers.get(var38);
                                                               var14 = var123;
                                                               if (var123 == null) {
                                                                  var14 = new ArrayList();
                                                                  this.printingUsers.put(var38, var14);
                                                               }

                                                               var32 = var14.size();
                                                               var11 = 0;

                                                               boolean var112;
                                                               while(true) {
                                                                  if (var11 >= var32) {
                                                                     var112 = false;
                                                                     break;
                                                                  }

                                                                  MessagesController.PrintingUser var151 = (MessagesController.PrintingUser)var14.get(var11);
                                                                  if (var151.userId == var93.user_id) {
                                                                     var151.lastTime = var5;
                                                                     var151.action = new TLRPC.TL_sendMessageTypingAction();
                                                                     var112 = true;
                                                                     break;
                                                                  }

                                                                  ++var11;
                                                               }

                                                               var21 = var21;
                                                               if (!var112) {
                                                                  MessagesController.PrintingUser var107 = new MessagesController.PrintingUser();
                                                                  var107.userId = var93.user_id;
                                                                  var107.lastTime = var5;
                                                                  var107.action = new TLRPC.TL_sendMessageTypingAction();
                                                                  var14.add(var107);
                                                                  var18 = true;
                                                               }

                                                               this.onlinePrivacy.put(var93.user_id, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                                            }

                                                            var11 = var8;
                                                         }

                                                         var14 = var162;
                                                         var8 = var11;
                                                         var64 = var71;
                                                         var11 = var33;
                                                         var77 = var62;
                                                      }

                                                      var68 = var77;
                                                      var87 = var69;
                                                      var67 = var53;
                                                      var73 = var21;
                                                      break label1262;
                                                   }

                                                   var22 = var22;
                                                   var68 = var15;
                                                   var64 = var71;
                                                   var87 = var69;
                                                   var67 = var53;
                                                   var5 = var5;
                                                   var73 = var21;
                                                   break label1262;
                                                }

                                                var64 = var71;
                                                var20 = var72;
                                                var11 = var11;
                                                var14 = var14;
                                                var68 = var15;
                                                var87 = var69;
                                                var67 = var76;
                                                var73 = var21;
                                                break label1262;
                                             }

                                             TLRPC.TL_updateContactLink var82 = (TLRPC.TL_updateContactLink)var30;
                                             var123 = var71;
                                             if (var71 == null) {
                                                var123 = new ArrayList();
                                             }

                                             if (var82.my_link instanceof TLRPC.TL_contactLinkContact) {
                                                var11 = var123.indexOf(-var82.user_id);
                                                if (var11 != -1) {
                                                   var123.remove(var11);
                                                }

                                                if (!var123.contains(var82.user_id)) {
                                                   var123.add(var82.user_id);
                                                }
                                             } else {
                                                var11 = var123.indexOf(var82.user_id);
                                                if (var11 != -1) {
                                                   var123.remove(var11);
                                                }

                                                if (!var123.contains(var82.user_id)) {
                                                   var123.add(-var82.user_id);
                                                }
                                             }

                                             var11 = var33;
                                             var71 = var123;
                                          }
                                       }

                                       var21 = var21;
                                    }

                                    var68 = var15;
                                    var64 = var71;
                                    var87 = var69;
                                    var67 = var53;
                                    var5 = var5;
                                    var73 = var21;
                                    break label1262;
                                 }

                                 TLRPC.TL_updateReadHistoryInbox var161 = (TLRPC.TL_updateReadHistoryInbox)var30;
                                 SparseLongArray var74 = var17;
                                 if (var17 == null) {
                                    var74 = new SparseLongArray();
                                 }

                                 TLRPC.Peer var88 = var161.peer;
                                 var33 = var88.chat_id;
                                 if (var33 != 0) {
                                    var74.put(-var33, (long)var161.max_id);
                                    var33 = -var161.peer.chat_id;
                                 } else {
                                    var74.put(var88.user_id, (long)var161.max_id);
                                    var33 = var161.peer.user_id;
                                 }

                                 var5 = (long)var33;
                                 var99 = (Integer)this.dialogs_read_inbox_max.get(var5);
                                 var89 = var99;
                                 if (var99 == null) {
                                    var89 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(false, var5);
                                 }

                                 this.dialogs_read_inbox_max.put(var5, Math.max(var89, var161.max_id));
                                 var17 = var74;
                                 var159 = var53;
                                 var5 = var35;
                                 break label1212;
                              }

                              TLRPC.TL_updateChannelReadMessagesContents var157 = (TLRPC.TL_updateChannelReadMessagesContents)var30;
                              var64 = var14;
                              if (var14 == null) {
                                 var64 = new ArrayList();
                              }

                              var32 = var157.messages.size();
                              var33 = 0;
                              var76 = var53;

                              while(true) {
                                 var3 = var64;
                                 var16 = var76;
                                 var5 = var35;
                                 if (var33 >= var32) {
                                    break;
                                 }

                                 var64.add((long)(Integer)var157.messages.get(var33) | (long)var157.channel_id << 32);
                                 ++var33;
                              }
                           }

                           var159 = var16;
                           var14 = var3;
                        }

                        var68 = var15;
                        var64 = var71;
                        var87 = var69;
                        var67 = var159;
                        var73 = var21;
                        break label1262;
                     }
                  }

                  var44 = var8;
                  var33 = var11;
                  SparseArray var34 = var25;
                  var70 = var15;
                  TLRPC.Message var150;
                  if (var31) {
                     var150 = ((TLRPC.TL_updateNewMessage)var30).message;
                  } else {
                     TLRPC.Message var54 = ((TLRPC.TL_updateNewChannelMessage)var30).message;
                     if (BuildVars.LOGS_ENABLED) {
                        StringBuilder var140 = new StringBuilder();
                        var140.append(var30);
                        var140.append(" channelId = ");
                        var140.append(var54.to_id.channel_id);
                        FileLog.d(var140.toString());
                     }

                     var150 = var54;
                     if (!var54.out) {
                        var150 = var54;
                        if (var54.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                           var54.out = true;
                           var150 = var54;
                        }
                     }
                  }

                  int var47;
                  label1257: {
                     TLRPC.Peer var55 = var150.to_id;
                     var11 = var55.channel_id;
                     if (var11 == 0) {
                        var11 = var55.chat_id;
                        if (var11 == 0) {
                           var32 = var55.user_id;
                           if (var32 == 0) {
                              var32 = 0;
                           }

                           var47 = 0;
                           break label1257;
                        }
                     }

                     var32 = 0;
                     var47 = var11;
                  }

                  TLRPC.Chat var142;
                  if (var47 != 0) {
                     var142 = (TLRPC.Chat)var53.get(var47);
                     TLRPC.Chat var56 = var142;
                     if (var142 == null) {
                        var56 = this.getChat(var47);
                     }

                     var142 = var56;
                     if (var56 == null) {
                        var142 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var47);
                        this.putChat(var142, true);
                     }
                  } else {
                     var142 = null;
                  }

                  SparseArray var57 = var25;
                  var11 = var33;
                  if (var65) {
                     StringBuilder var50;
                     if (var47 != 0 && var142 == null) {
                        if (BuildVars.LOGS_ENABLED) {
                           var50 = new StringBuilder();
                           var50.append("not found chat ");
                           var50.append(var47);
                           FileLog.d(var50.toString());
                        }

                        return false;
                     }

                     var47 = var150.entities.size() + 3;
                     int var48 = 0;

                     while(true) {
                        var57 = var34;
                        var11 = var33;
                        if (var48 >= var47) {
                           break;
                        }

                        label1228: {
                           var11 = var32;
                           if (var48 != 0) {
                              if (var48 == 1) {
                                 var32 = var150.from_id;
                                 var11 = var32;
                                 if (var150.post) {
                                    boolean var49 = true;
                                    var11 = var32;
                                    var153 = var49;
                                    break label1228;
                                 }
                              } else if (var48 == 2) {
                                 TLRPC.MessageFwdHeader var58 = var150.fwd_from;
                                 if (var58 != null) {
                                    var11 = var58.from_id;
                                 } else {
                                    var11 = 0;
                                 }
                              } else {
                                 TLRPC.MessageEntity var59 = (TLRPC.MessageEntity)var150.entities.get(var48 - 3);
                                 if (var59 instanceof TLRPC.TL_messageEntityMentionName) {
                                    var11 = ((TLRPC.TL_messageEntityMentionName)var59).user_id;
                                 } else {
                                    var11 = 0;
                                 }
                              }
                           }

                           var153 = false;
                        }

                        int var183;
                        if (var11 <= 0) {
                           var183 = var33;
                        } else {
                           TLRPC.User var43;
                           TLRPC.User var60;
                           label1245: {
                              var43 = (TLRPC.User)var29.get(var11);
                              if (var43 != null) {
                                 if (var153) {
                                    var60 = var43;
                                    break label1245;
                                 }

                                 var60 = var43;
                                 if (!var43.min) {
                                    break label1245;
                                 }
                              }

                              var60 = this.getUser(var11);
                           }

                           label1337: {
                              if (var60 != null) {
                                 var43 = var60;
                                 if (var153) {
                                    break label1337;
                                 }

                                 var43 = var60;
                                 if (!var60.min) {
                                    break label1337;
                                 }
                              }

                              var43 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var11);
                              var60 = var43;
                              if (var43 != null) {
                                 var60 = var43;
                                 if (!var153) {
                                    var60 = var43;
                                    if (var43.min) {
                                       var60 = null;
                                    }
                                 }
                              }

                              this.putUser(var60, true);
                              var43 = var60;
                           }

                           if (var43 == null) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var50 = new StringBuilder();
                                 var50.append("not found user ");
                                 var50.append(var11);
                                 FileLog.d(var50.toString());
                              }

                              return false;
                           }

                           var183 = var33;
                           if (var48 == 1) {
                              TLRPC.UserStatus var66 = var43.status;
                              var183 = var33;
                              if (var66 != null) {
                                 var183 = var33;
                                 if (var66.expires <= 0) {
                                    this.onlinePrivacy.put(var11, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                                    var183 = var33 | 4;
                                 }
                              }
                           }
                        }

                        ++var48;
                        var32 = var11;
                        var33 = var183;
                     }
                  }

                  if (var142 != null && var142.megagroup) {
                     var150.flags |= Integer.MIN_VALUE;
                  }

                  TLRPC.MessageAction var154 = var150.action;
                  if (var154 instanceof TLRPC.TL_messageActionChatDeleteUser) {
                     TLRPC.User var155 = (TLRPC.User)var29.get(var154.user_id);
                     if (var155 != null && var155.bot) {
                        var150.reply_markup = new TLRPC.TL_replyKeyboardHide();
                        var150.flags |= 64;
                     } else if (var150.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId() && var150.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        var67 = var53;
                        var87 = var69;
                        var64 = var71;
                        var25 = var57;
                        break label1293;
                     }
                  }

                  var106 = var19;
                  if (var19 == null) {
                     var106 = new ArrayList();
                  }

                  var106.add(var150);
                  ImageLoader.saveMessageThumbs(var150);
                  var9 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                  TLRPC.Peer var100 = var150.to_id;
                  var8 = var100.chat_id;
                  if (var8 != 0) {
                     var150.dialog_id = (long)(-var8);
                  } else {
                     var8 = var100.channel_id;
                     if (var8 != 0) {
                        var150.dialog_id = (long)(-var8);
                     } else {
                        if (var100.user_id == var9) {
                           var100.user_id = var150.from_id;
                        }

                        var150.dialog_id = (long)var150.to_id.user_id;
                     }
                  }

                  ConcurrentHashMap var102;
                  if (var150.out) {
                     var102 = this.dialogs_read_outbox_max;
                  } else {
                     var102 = this.dialogs_read_inbox_max;
                  }

                  var111 = (Integer)var102.get(var150.dialog_id);
                  Integer var108;
                  if (var111 == null) {
                     var111 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var150.out, var150.dialog_id);
                     var102.put(var150.dialog_id, var111);
                     var108 = var111;
                  } else {
                     var108 = var111;
                  }

                  label960: {
                     if (var108 < var150.id && (var142 == null || !ChatObject.isNotInChat(var142))) {
                        TLRPC.MessageAction var116 = var150.action;
                        if (!(var116 instanceof TLRPC.TL_messageActionChatMigrateTo) && !(var116 instanceof TLRPC.TL_messageActionChannelCreate)) {
                           var31 = true;
                           break label960;
                        }
                     }

                     var31 = false;
                  }

                  var150.unread = var31;
                  if (var150.dialog_id == (long)var9) {
                     var150.unread = false;
                     var150.media_unread = false;
                     var150.out = true;
                  }

                  MessageObject var145;
                  label949: {
                     var9 = this.currentAccount;
                     var31 = this.createdDialogIds.contains(var150.dialog_id);
                     var145 = new MessageObject(var9, var150, var29, var53, var31);
                     var9 = var145.type;
                     if (var9 == 11) {
                        var9 = var11 | 8;
                     } else {
                        if (var9 != 10) {
                           break label949;
                        }

                        var9 = var11 | 16;
                     }

                     var11 = var9;
                  }

                  if (var69 == null) {
                     var87 = new LongSparseArray();
                  } else {
                     var87 = var69;
                  }

                  var64 = (ArrayList)var87.get(var150.dialog_id);
                  if (var64 == null) {
                     var64 = new ArrayList();
                     var87.put(var150.dialog_id, var64);
                  }

                  var64.add(var145);
                  if (!var145.isOut() && var145.isUnread()) {
                     if (var15 == null) {
                        var70 = new ArrayList();
                     }

                     ((ArrayList)var70).add(var145);
                  }

                  var64 = var71;
                  var20 = var20;
                  var8 = var44;
                  var73 = var21;
                  var25 = var57;
                  var65 = var65;
                  var67 = var53;
                  var19 = var106;
                  var68 = var70;
                  break label1262;
               }

               var73 = var21;
               var68 = var15;
            }

            ++var8;
            var21 = var73;
            var15 = var68;
            var71 = var64;
            var69 = var87;
         }

         LongSparseArray var51 = var69;
         var4 = var18;
         var72 = var69;
         if (var69 != null) {
            var8 = var69.size();
            var9 = 0;

            while(true) {
               var4 = var18;
               var72 = var51;
               if (var9 >= var8) {
                  break;
               }

               if (this.updatePrintingUsersWithNewMessages(var51.keyAt(var9), (ArrayList)var51.valueAt(var9))) {
                  var18 = true;
               }

               ++var9;
            }
         }

         if (var4) {
            this.updatePrintingStrings();
         }

         if (var71 != null) {
            ContactsController.getInstance(this.currentAccount).processContactsUpdates(var71, var29);
         }

         if (var15 != null) {
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$rmpWNkBwNMY39XhMVE_pMAwyvyA(this, (ArrayList)var15));
         }

         if (var19 != null) {
            StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ApplicationLoader.getCurrentNetworkType(), 1, var19.size());
            MessagesStorage.getInstance(this.currentAccount).putMessages(var19, true, true, false, DownloadController.getInstance(this.currentAccount).getAutodownloadMask());
         }

         if (var20 != null) {
            var33 = var20.size();

            for(var9 = 0; var9 < var33; ++var9) {
               TLRPC.TL_messages_messages var104 = new TLRPC.TL_messages_messages();
               var1 = (ArrayList)var20.valueAt(var9);
               var32 = var1.size();

               for(var8 = 0; var8 < var32; ++var8) {
                  var104.messages.add(((MessageObject)var1.get(var8)).messageOwner);
               }

               MessagesStorage.getInstance(this.currentAccount).putMessages(var104, var20.keyAt(var9), -2, 0, false);
            }
         }

         if (var23 != null) {
            MessagesStorage.getInstance(this.currentAccount).putChannelViews((SparseArray)var23, true);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$aIzxVN1fNX7ISomIdQom833zn_c(this, var11, var2, var22, var72, var20, var4, var71, var27, (SparseArray)var23));
         MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$ZCTDymAyINyEW9FoTNxhmaMSvzk(this, var17, var21, var24, var14, var25, var26));
         if (var22 != null) {
            MessagesStorage.getInstance(this.currentAccount).putWebPages(var22);
         }

         if (var17 != null || var21 != null || var24 != null || var14 != null) {
            if (var17 != null || var14 != null) {
               MessagesStorage.getInstance(this.currentAccount).updateDialogsWithReadMessages(var17, var21, var14, true);
            }

            MessagesStorage.getInstance(this.currentAccount).markMessagesAsRead(var17, var21, var24, true);
         }

         if (var14 != null) {
            MessagesStorage.getInstance(this.currentAccount).markMessagesContentAsRead(var14, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
         }

         if (var25 != null) {
            var8 = var25.size();

            for(var9 = 0; var9 < var8; ++var9) {
               var11 = var25.keyAt(var9);
               var1 = (ArrayList)var25.valueAt(var9);
               MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$eIE8R8MYjUQYKP1gLWpaSncXEb8(this, var1, var11));
            }
         }

         if (var26 != null) {
            var8 = var26.size();

            for(var9 = 0; var9 < var8; ++var9) {
               var11 = var26.keyAt(var9);
               var33 = var26.valueAt(var9);
               MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$H4wHvQqPFocrQ_yPWuW6Ehm2l3E(this, var11, var33));
            }
         }

         if (var28 != null) {
            var8 = var28.size();

            for(var9 = 0; var9 < var8; ++var9) {
               TLRPC.TL_updateEncryptedMessagesRead var52 = (TLRPC.TL_updateEncryptedMessagesRead)var28.get(var9);
               MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(var52.chat_id, var52.max_date, var52.date, 1, (ArrayList)null);
            }
         }

         return true;
      }
   }

   public void processUpdates(TLRPC.Updates var1, boolean var2) {
      boolean var4;
      int var8;
      int var17;
      ArrayList var23;
      boolean var30;
      int var33;
      boolean var34;
      label743: {
         boolean var9;
         label724: {
            boolean var3 = var1 instanceof TLRPC.TL_updateShort;
            var4 = false;
            if (var3) {
               var23 = new ArrayList();
               var23.add(var1.update);
               this.processUpdateArray(var23, (ArrayList)null, (ArrayList)null, false);
            } else {
               var3 = var1 instanceof TLRPC.TL_updateShortChatMessage;
               String var6 = " count = ";
               String var7 = "add to queue";
               int var10;
               TLRPC.Chat var11;
               TLRPC.Chat var13;
               int var21;
               StringBuilder var22;
               ArrayList var49;
               if (var3 || var1 instanceof TLRPC.TL_updateShortMessage) {
                  if (var3) {
                     var21 = var1.from_id;
                  } else {
                     var21 = var1.user_id;
                  }

                  TLRPC.User var25;
                  TLRPC.User var43;
                  TLRPC.User var53;
                  label683: {
                     var25 = this.getUser(var21);
                     if (var25 != null) {
                        var43 = var25;
                        if (!var25.min) {
                           break label683;
                        }
                     }

                     var53 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var21);
                     var25 = var53;
                     if (var53 != null) {
                        var25 = var53;
                        if (var53.min) {
                           var25 = null;
                        }
                     }

                     this.putUser(var25, true);
                     var43 = var25;
                  }

                  TLRPC.Chat var24;
                  label653: {
                     TLRPC.MessageFwdHeader var32 = var1.fwd_from;
                     if (var32 != null) {
                        var8 = var32.from_id;
                        if (var8 != 0) {
                           var53 = this.getUser(var8);
                           var25 = var53;
                           if (var53 == null) {
                              var25 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var1.fwd_from.from_id);
                              this.putUser(var25, true);
                           }

                           var30 = true;
                        } else {
                           var30 = false;
                           var25 = null;
                        }

                        var10 = var1.fwd_from.channel_id;
                        var53 = var25;
                        if (var10 != 0) {
                           var24 = this.getChat(var10);
                           var13 = var24;
                           if (var24 == null) {
                              var13 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var1.fwd_from.channel_id);
                              this.putChat(var13, true);
                           }

                           var24 = var13;
                           var30 = true;
                           var53 = var25;
                           break label653;
                        }
                     } else {
                        var30 = false;
                        var53 = null;
                     }

                     var24 = null;
                  }

                  var10 = var1.via_bot_id;
                  if (var10 != 0) {
                     TLRPC.User var28 = this.getUser(var10);
                     var25 = var28;
                     if (var28 == null) {
                        var25 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var1.via_bot_id);
                        this.putUser(var25, true);
                     }

                     var34 = true;
                  } else {
                     var34 = false;
                     var25 = null;
                  }

                  boolean var19;
                  label646: {
                     label645: {
                        var19 = var1 instanceof TLRPC.TL_updateShortMessage;
                        if (var19) {
                           if (var43 != null && (!var30 || var53 != null || var24 != null) && (!var34 || var25 != null)) {
                              break label645;
                           }
                        } else {
                           var11 = this.getChat(var1.chat_id);
                           TLRPC.Chat var31 = var11;
                           if (var11 == null) {
                              var31 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var1.chat_id);
                              this.putChat(var31, true);
                           }

                           if (var31 != null && var43 != null && (!var30 || var53 != null || var24 != null) && (!var34 || var25 != null)) {
                              break label645;
                           }
                        }

                        var30 = true;
                        break label646;
                     }

                     var30 = false;
                  }

                  var34 = var30;
                  if (!var30) {
                     var34 = var30;
                     if (!var1.entities.isEmpty()) {
                        var33 = 0;

                        while(true) {
                           var34 = var30;
                           if (var33 >= var1.entities.size()) {
                              break;
                           }

                           TLRPC.MessageEntity var38 = (TLRPC.MessageEntity)var1.entities.get(var33);
                           if (var38 instanceof TLRPC.TL_messageEntityMentionName) {
                              var10 = ((TLRPC.TL_messageEntityMentionName)var38).user_id;
                              var25 = this.getUser(var10);
                              if (var25 == null || var25.min) {
                                 var53 = MessagesStorage.getInstance(this.currentAccount).getUserSync(var10);
                                 var25 = var53;
                                 if (var53 != null) {
                                    var25 = var53;
                                    if (var53.min) {
                                       var25 = null;
                                    }
                                 }

                                 if (var25 == null) {
                                    var34 = true;
                                    break;
                                 }

                                 this.putUser(var43, true);
                              }
                           }

                           ++var33;
                        }
                     }
                  }

                  label600: {
                     if (var43 != null) {
                        TLRPC.UserStatus var42 = var43.status;
                        if (var42 != null && var42.expires <= 0) {
                           this.onlinePrivacy.put(var43.id, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
                           var30 = true;
                           break label600;
                        }
                     }

                     var30 = false;
                  }

                  if (!var34) {
                     label714: {
                        if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + var1.pts_count == var1.pts) {
                           TLRPC.TL_message var29 = new TLRPC.TL_message();
                           var29.id = var1.id;
                           var33 = UserConfig.getInstance(this.currentAccount).getClientUserId();
                           if (var19) {
                              if (var1.out) {
                                 var29.from_id = var33;
                              } else {
                                 var29.from_id = var21;
                              }

                              var29.to_id = new TLRPC.TL_peerUser();
                              var29.to_id.user_id = var21;
                              var29.dialog_id = (long)var21;
                           } else {
                              var29.from_id = var21;
                              var29.to_id = new TLRPC.TL_peerChat();
                              TLRPC.Peer var44 = var29.to_id;
                              var10 = var1.chat_id;
                              var44.chat_id = var10;
                              var29.dialog_id = (long)(-var10);
                           }

                           var29.fwd_from = var1.fwd_from;
                           var29.silent = var1.silent;
                           var29.out = var1.out;
                           var29.mentioned = var1.mentioned;
                           var29.media_unread = var1.media_unread;
                           var29.entities = var1.entities;
                           var29.message = var1.message;
                           var29.date = var1.date;
                           var29.via_bot_id = var1.via_bot_id;
                           var29.flags = var1.flags | 256;
                           var29.reply_to_msg_id = var1.reply_to_msg_id;
                           var29.media = new TLRPC.TL_messageMediaEmpty();
                           ConcurrentHashMap var45;
                           if (var29.out) {
                              var45 = this.dialogs_read_outbox_max;
                           } else {
                              var45 = this.dialogs_read_inbox_max;
                           }

                           Integer var50 = (Integer)var45.get(var29.dialog_id);
                           Integer var54 = var50;
                           if (var50 == null) {
                              var54 = MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(var29.out, var29.dialog_id);
                              var45.put(var29.dialog_id, var54);
                           }

                           if (var54 < var29.id) {
                              var3 = true;
                           } else {
                              var3 = false;
                           }

                           var29.unread = var3;
                           if (var29.dialog_id == (long)var33) {
                              var29.unread = false;
                              var29.media_unread = false;
                              var29.out = true;
                           }

                           var3 = true;
                           MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var1.pts);
                           MessageObject var48 = new MessageObject(this.currentAccount, var29, this.createdDialogIds.contains(var29.dialog_id));
                           var49 = new ArrayList();
                           var49.add(var48);
                           ArrayList var52 = new ArrayList();
                           var52.add(var29);
                           if (!var19) {
                              var3 = this.updatePrintingUsersWithNewMessages((long)(-var1.chat_id), var49);
                              if (var3) {
                                 this.updatePrintingStrings();
                              }

                              AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$s0GgcDgh5GZ9Riv31NjWgQrzb_w(this, var3, var1, var49));
                           } else {
                              if (var1.out || !this.updatePrintingUsersWithNewMessages((long)var1.user_id, var49)) {
                                 var3 = false;
                              }

                              if (var3) {
                                 this.updatePrintingStrings();
                              }

                              AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$fPbr3UYq21LGzjTkEfox5kOy2UM(this, var3, var21, var49));
                           }

                           if (!var48.isOut()) {
                              MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$MessagesController$iAn5VSEQcHcofX2azl7_Y4mcgGM(this, var49));
                           }

                           MessagesStorage.getInstance(this.currentAccount).putMessages(var52, false, true, false, 0);
                        } else if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() != var1.pts) {
                           if (BuildVars.LOGS_ENABLED) {
                              var22 = new StringBuilder();
                              var22.append("need get diff short message, pts: ");
                              var22.append(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue());
                              var22.append(" ");
                              var22.append(var1.pts);
                              var22.append(" count = ");
                              var22.append(var1.pts_count);
                              FileLog.d(var22.toString());
                           }

                           if (!this.gettingDifference && this.updatesStartWaitTimePts != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) > 1500L) {
                              break label714;
                           }

                           if (this.updatesStartWaitTimePts == 0L) {
                              this.updatesStartWaitTimePts = System.currentTimeMillis();
                           }

                           if (BuildVars.LOGS_ENABLED) {
                              FileLog.d("add to queue");
                           }

                           this.updatesQueuePts.add(var1);
                        }

                        var4 = false;
                        break label724;
                     }
                  }

                  var4 = true;
                  break label724;
               }

               var3 = var1 instanceof TLRPC.TL_updatesCombined;
               if (var3 || var1 instanceof TLRPC.TL_updates) {
                  SparseArray var5 = null;

                  SparseArray var12;
                  for(var8 = 0; var8 < var1.chats.size(); var5 = var12) {
                     var11 = (TLRPC.Chat)var1.chats.get(var8);
                     var12 = var5;
                     if (var11 instanceof TLRPC.TL_channel) {
                        var12 = var5;
                        if (var11.min) {
                           label717: {
                              label562: {
                                 TLRPC.Chat var39 = this.getChat(var11.id);
                                 if (var39 != null) {
                                    var13 = var39;
                                    if (!var39.min) {
                                       break label562;
                                    }
                                 }

                                 var13 = MessagesStorage.getInstance(this.currentAccount).getChatSync(var1.chat_id);
                                 this.putChat(var13, true);
                              }

                              if (var13 != null) {
                                 var12 = var5;
                                 if (!var13.min) {
                                    break label717;
                                 }
                              }

                              SparseArray var41 = var5;
                              if (var5 == null) {
                                 var41 = new SparseArray();
                              }

                              var41.put(var11.id, var11);
                              var12 = var41;
                           }
                        }
                     }

                     ++var8;
                  }

                  TLRPC.Update var46;
                  label547: {
                     if (var5 != null) {
                        for(var8 = 0; var8 < var1.updates.size(); ++var8) {
                           var46 = (TLRPC.Update)var1.updates.get(var8);
                           if (var46 instanceof TLRPC.TL_updateNewChannelMessage) {
                              var21 = ((TLRPC.TL_updateNewChannelMessage)var46).message.to_id.channel_id;
                              if (var5.indexOfKey(var21) >= 0) {
                                 if (BuildVars.LOGS_ENABLED) {
                                    var22 = new StringBuilder();
                                    var22.append("need get diff because of min channel ");
                                    var22.append(var21);
                                    FileLog.d(var22.toString());
                                 }

                                 var30 = true;
                                 break label547;
                              }
                           }
                        }
                     }

                     var30 = false;
                  }

                  if (var30) {
                     var23 = null;
                     var4 = false;
                  } else {
                     MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var1.users, var1.chats, true, true);
                     Collections.sort(var1.updates, this.updatesComparator);
                     var23 = null;
                     var4 = false;

                     StringBuilder var51;
                     for(String var40 = var7; var1.updates.size() > 0; var1.updates.remove(0)) {
                        label697: {
                           TLRPC.TL_updates var47;
                           label528: {
                              label698: {
                                 label729: {
                                    TLRPC.Update var26 = (TLRPC.Update)var1.updates.get(0);
                                    long var14;
                                    TLRPC.Update var35;
                                    StringBuilder var36;
                                    if (this.getUpdateType(var26) == 0) {
                                       var47 = new TLRPC.TL_updates();
                                       var47.updates.add(var26);
                                       var47.pts = getUpdatePts(var26);
                                       var47.pts_count = getUpdatePtsCount(var26);

                                       while(true) {
                                          if (1 < var1.updates.size()) {
                                             var35 = (TLRPC.Update)var1.updates.get(1);
                                             var10 = getUpdatePts(var35);
                                             var33 = getUpdatePtsCount(var35);
                                             if (this.getUpdateType(var35) == 0 && var47.pts + var33 == var10) {
                                                var47.updates.add(var35);
                                                var47.pts = var10;
                                                var47.pts_count += var33;
                                                var1.updates.remove(1);
                                                continue;
                                             }
                                          }

                                          if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + var47.pts_count == var47.pts) {
                                             if (this.processUpdateArray(var47.updates, var1.users, var1.chats, false)) {
                                                MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var47.pts);
                                                break label698;
                                             }

                                             if (BuildVars.LOGS_ENABLED) {
                                                var51 = new StringBuilder();
                                                var51.append("need get diff inner TL_updates, pts: ");
                                                var51.append(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue());
                                                var51.append(" ");
                                                var51.append(var1.seq);
                                                FileLog.d(var51.toString());
                                             }
                                          } else {
                                             if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() == var47.pts) {
                                                break label698;
                                             }

                                             if (BuildVars.LOGS_ENABLED) {
                                                var36 = new StringBuilder();
                                                var36.append(var26);
                                                var36.append(" need get diff, pts: ");
                                                var36.append(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue());
                                                var36.append(" ");
                                                var36.append(var47.pts);
                                                var36.append(var6);
                                                var36.append(var47.pts_count);
                                                FileLog.d(var36.toString());
                                             }

                                             if (this.gettingDifference) {
                                                break label729;
                                             }

                                             var14 = this.updatesStartWaitTimePts;
                                             if (var14 == 0L || var14 != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) <= 1500L) {
                                                break label729;
                                             }
                                          }
                                          break;
                                       }
                                    } else {
                                       if (this.getUpdateType(var26) != 1) {
                                          if (this.getUpdateType(var26) != 2) {
                                             break;
                                          }

                                          int var16;
                                          label472: {
                                             label471: {
                                                var16 = getUpdateChannelId(var26);
                                                var10 = this.channelsPts.get(var16);
                                                var33 = var10;
                                                if (var10 == 0) {
                                                   var10 = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(var16);
                                                   if (var10 != 0) {
                                                      this.channelsPts.put(var16, var10);
                                                      break label471;
                                                   }

                                                   var17 = 0;

                                                   while(true) {
                                                      var33 = var10;
                                                      if (var17 >= var1.chats.size()) {
                                                         break;
                                                      }

                                                      var13 = (TLRPC.Chat)var1.chats.get(var17);
                                                      if (var13.id == var16) {
                                                         this.loadUnknownChannel(var13, 0L);
                                                         var9 = true;
                                                         break label472;
                                                      }

                                                      ++var17;
                                                   }
                                                }

                                                var10 = var33;
                                             }

                                             var9 = false;
                                          }

                                          TLRPC.TL_updates var37 = new TLRPC.TL_updates();
                                          var37.updates.add(var26);
                                          var37.pts = getUpdatePts(var26);
                                          var37.pts_count = getUpdatePtsCount(var26);

                                          while(1 < var1.updates.size()) {
                                             var46 = (TLRPC.Update)var1.updates.get(1);
                                             int var18 = getUpdatePts(var46);
                                             var17 = getUpdatePtsCount(var46);
                                             if (this.getUpdateType(var46) != 2 || var16 != getUpdateChannelId(var46) || var37.pts + var17 != var18) {
                                                break;
                                             }

                                             var37.updates.add(var46);
                                             var37.pts = var18;
                                             var37.pts_count += var17;
                                             var1.updates.remove(1);
                                          }

                                          if (!var9) {
                                             var17 = var37.pts_count;
                                             var33 = var37.pts;
                                             if (var17 + var10 == var33) {
                                                if (!this.processUpdateArray(var37.updates, var1.users, var1.chats, false)) {
                                                   if (BuildVars.LOGS_ENABLED) {
                                                      var51 = new StringBuilder();
                                                      var51.append("need get channel diff inner TL_updates, channel_id = ");
                                                      var51.append(var16);
                                                      FileLog.d(var51.toString());
                                                   }

                                                   if (var23 == null) {
                                                      var49 = new ArrayList();
                                                   } else {
                                                      var49 = var23;
                                                      if (!var23.contains(var16)) {
                                                         var23.add(var16);
                                                         var49 = var23;
                                                      }
                                                   }
                                                } else {
                                                   this.channelsPts.put(var16, var37.pts);
                                                   MessagesStorage.getInstance(this.currentAccount).saveChannelPts(var16, var37.pts);
                                                   var49 = var23;
                                                }
                                             } else {
                                                var49 = var23;
                                                if (var10 != var33) {
                                                   if (BuildVars.LOGS_ENABLED) {
                                                      var51 = new StringBuilder();
                                                      var51.append(var26);
                                                      var51.append(" need get channel diff, pts: ");
                                                      var51.append(var10);
                                                      var51.append(" ");
                                                      var51.append(var37.pts);
                                                      var51.append(var6);
                                                      var51.append(var37.pts_count);
                                                      var51.append(" channelId = ");
                                                      var51.append(var16);
                                                      FileLog.d(var51.toString());
                                                   }

                                                   var14 = this.updatesStartWaitTimeChannels.get(var16);
                                                   if (!this.gettingDifferenceChannels.get(var16) && var14 != 0L && Math.abs(System.currentTimeMillis() - var14) > 1500L) {
                                                      if (var23 == null) {
                                                         var49 = new ArrayList();
                                                      } else {
                                                         var49 = var23;
                                                         if (!var23.contains(var16)) {
                                                            var23.add(var16);
                                                            var49 = var23;
                                                         }
                                                      }
                                                   } else {
                                                      if (var14 == 0L) {
                                                         this.updatesStartWaitTimeChannels.put(var16, System.currentTimeMillis());
                                                      }

                                                      if (BuildVars.LOGS_ENABLED) {
                                                         FileLog.d(var40);
                                                      }

                                                      ArrayList var27 = (ArrayList)this.updatesQueueChannels.get(var16);
                                                      var49 = var27;
                                                      if (var27 == null) {
                                                         var49 = new ArrayList();
                                                         this.updatesQueueChannels.put(var16, var49);
                                                      }

                                                      var49.add(var37);
                                                      var49 = var23;
                                                   }
                                                }
                                             }
                                          } else {
                                             var49 = var23;
                                             if (BuildVars.LOGS_ENABLED) {
                                                var51 = new StringBuilder();
                                                var51.append("need load unknown channel = ");
                                                var51.append(var16);
                                                FileLog.d(var51.toString());
                                                var49 = var23;
                                             }
                                          }

                                          var23 = var49;
                                          continue;
                                       }

                                       var47 = new TLRPC.TL_updates();
                                       var47.updates.add(var26);
                                       var47.pts = getUpdateQts(var26);

                                       while(1 < var1.updates.size()) {
                                          var35 = (TLRPC.Update)var1.updates.get(1);
                                          var10 = getUpdateQts(var35);
                                          if (this.getUpdateType(var35) != 1 || var47.pts + 1 != var10) {
                                             break;
                                          }

                                          var47.updates.add(var35);
                                          var47.pts = var10;
                                          var1.updates.remove(1);
                                       }

                                       if (MessagesStorage.getInstance(this.currentAccount).getLastQtsValue() == 0 || MessagesStorage.getInstance(this.currentAccount).getLastQtsValue() + var47.updates.size() == var47.pts) {
                                          this.processUpdateArray(var47.updates, var1.users, var1.chats, false);
                                          MessagesStorage.getInstance(this.currentAccount).setLastQtsValue(var47.pts);
                                          var34 = true;
                                          var9 = var30;
                                          break label697;
                                       }

                                       var9 = var30;
                                       var34 = var4;
                                       if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() == var47.pts) {
                                          break label697;
                                       }

                                       if (BuildVars.LOGS_ENABLED) {
                                          var36 = new StringBuilder();
                                          var36.append(var26);
                                          var36.append(" need get diff, qts: ");
                                          var36.append(MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
                                          var36.append(" ");
                                          var36.append(var47.pts);
                                          FileLog.d(var36.toString());
                                       }

                                       if (this.gettingDifference) {
                                          break label528;
                                       }

                                       var14 = this.updatesStartWaitTimeQts;
                                       if (var14 == 0L || var14 != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeQts) <= 1500L) {
                                          break label528;
                                       }
                                    }

                                    var9 = true;
                                    var34 = var4;
                                    break label697;
                                 }

                                 if (this.updatesStartWaitTimePts == 0L) {
                                    this.updatesStartWaitTimePts = System.currentTimeMillis();
                                 }

                                 if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d(var40);
                                 }

                                 this.updatesQueuePts.add(var47);
                                 var9 = var30;
                                 var34 = var4;
                                 break label697;
                              }

                              var34 = var4;
                              var9 = var30;
                              break label697;
                           }

                           if (this.updatesStartWaitTimeQts == 0L) {
                              this.updatesStartWaitTimeQts = System.currentTimeMillis();
                           }

                           if (BuildVars.LOGS_ENABLED) {
                              FileLog.d(var40);
                           }

                           this.updatesQueueQts.add(var47);
                           var9 = var30;
                           var34 = var4;
                        }

                        var30 = var9;
                        var4 = var34;
                     }

                     label429: {
                        label428: {
                           if (var3) {
                              if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() + 1 != var1.seq_start && MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() != var1.seq_start) {
                                 break label428;
                              }
                           } else {
                              var33 = MessagesStorage.getInstance(this.currentAccount).getLastSeqValue();
                              var10 = var1.seq;
                              if (var33 + 1 != var10 && var10 != 0 && var10 != MessagesStorage.getInstance(this.currentAccount).getLastSeqValue()) {
                                 break label428;
                              }
                           }

                           var34 = true;
                           break label429;
                        }

                        var34 = false;
                     }

                     if (var34) {
                        this.processUpdateArray(var1.updates, var1.users, var1.chats, false);
                        if (var1.seq != 0) {
                           if (var1.date != 0) {
                              MessagesStorage.getInstance(this.currentAccount).setLastDateValue(var1.date);
                           }

                           MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(var1.seq);
                        }
                     } else {
                        if (BuildVars.LOGS_ENABLED) {
                           if (var3) {
                              var51 = new StringBuilder();
                              var51.append("need get diff TL_updatesCombined, seq: ");
                              var51.append(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue());
                              var51.append(" ");
                              var51.append(var1.seq_start);
                              FileLog.d(var51.toString());
                           } else {
                              var51 = new StringBuilder();
                              var51.append("need get diff TL_updates, seq: ");
                              var51.append(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue());
                              var51.append(" ");
                              var51.append(var1.seq);
                              FileLog.d(var51.toString());
                           }
                        }

                        if (!this.gettingDifference && this.updatesStartWaitTimeSeq != 0L && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeSeq) > 1500L) {
                           var30 = true;
                        } else {
                           if (this.updatesStartWaitTimeSeq == 0L) {
                              this.updatesStartWaitTimeSeq = System.currentTimeMillis();
                           }

                           if (BuildVars.LOGS_ENABLED) {
                              FileLog.d("add TL_updates/Combined to queue");
                           }

                           this.updatesQueueSeq.add(var1);
                        }
                     }
                  }

                  var34 = false;
                  break label743;
               }

               if (var1 instanceof TLRPC.TL_updatesTooLong) {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("need get diff TL_updatesTooLong");
                  }

                  var30 = false;
                  var4 = true;
                  break label724;
               }

               if (var1 instanceof MessagesController.UserActionUpdatesSeq) {
                  MessagesStorage.getInstance(this.currentAccount).setLastSeqValue(var1.seq);
               } else if (var1 instanceof MessagesController.UserActionUpdatesPts) {
                  var8 = var1.chat_id;
                  if (var8 != 0) {
                     this.channelsPts.put(var8, var1.pts);
                     MessagesStorage.getInstance(this.currentAccount).saveChannelPts(var1.chat_id, var1.pts);
                  } else {
                     MessagesStorage.getInstance(this.currentAccount).setLastPtsValue(var1.pts);
                  }
               }
            }

            var30 = false;
         }

         var23 = null;
         var9 = false;
         var34 = var30;
         var30 = var4;
         var4 = var9;
      }

      SecretChatHelper.getInstance(this.currentAccount).processPendingEncMessages();
      if (!var2) {
         for(var33 = 0; var33 < this.updatesQueueChannels.size(); ++var33) {
            var17 = this.updatesQueueChannels.keyAt(var33);
            if (var23 != null && var23.contains(var17)) {
               this.getChannelDifference(var17);
            } else {
               this.processChannelsUpdatesQueue(var17, 0);
            }
         }

         if (var30) {
            this.getDifference();
         } else {
            for(var8 = 0; var8 < 3; ++var8) {
               this.processUpdatesQueue(var8, 0);
            }
         }
      }

      if (var4) {
         TLRPC.TL_messages_receivedQueue var20 = new TLRPC.TL_messages_receivedQueue();
         var20.max_qts = MessagesStorage.getInstance(this.currentAccount).getLastQtsValue();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var20, _$$Lambda$MessagesController$9PuTSEkyzwdd31UYsaKLfGdsGkM.INSTANCE);
      }

      if (var34) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$U7UNWGF7e0N8D_YCbxsC0l6X5Iw(this));
      }

      MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
   }

   public void processUserInfo(TLRPC.User var1, TLRPC.UserFull var2, boolean var3, boolean var4, MessageObject var5, int var6) {
      if (var3) {
         this.loadFullUser(var1, var6, var4);
      }

      if (var2 != null) {
         if (this.fullUsers.get(var1.id) == null) {
            this.fullUsers.put(var1.id, var2);
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$vF733kQiTAupYlV5PFZpkpSFLz4(this, var1, var2, var5));
      }

   }

   public void putChat(TLRPC.Chat var1, boolean var2) {
      if (var1 != null) {
         TLRPC.Chat var3 = (TLRPC.Chat)this.chats.get(var1.id);
         if (var3 != var1) {
            if (var3 != null && !TextUtils.isEmpty(var3.username)) {
               this.objectsByUsernames.remove(var3.username.toLowerCase());
            }

            if (!TextUtils.isEmpty(var1.username)) {
               this.objectsByUsernames.put(var1.username.toLowerCase(), var1);
            }

            TLRPC.TL_chatBannedRights var4;
            int var5;
            TLRPC.TL_chatAdminRights var9;
            String var10;
            if (var1.min) {
               if (var3 != null) {
                  if (!var2) {
                     var3.title = var1.title;
                     var3.photo = var1.photo;
                     var3.broadcast = var1.broadcast;
                     var3.verified = var1.verified;
                     var3.megagroup = var1.megagroup;
                     var4 = var1.default_banned_rights;
                     if (var4 != null) {
                        var3.default_banned_rights = var4;
                        var3.flags |= 262144;
                     }

                     var9 = var1.admin_rights;
                     if (var9 != null) {
                        var3.admin_rights = var9;
                        var3.flags |= 16384;
                     }

                     var4 = var1.banned_rights;
                     if (var4 != null) {
                        var3.banned_rights = var4;
                        var3.flags |= 32768;
                     }

                     var10 = var1.username;
                     if (var10 != null) {
                        var3.username = var10;
                        var3.flags |= 64;
                     } else {
                        var3.flags &= -65;
                        var3.username = null;
                     }

                     var5 = var1.participants_count;
                     if (var5 != 0) {
                        var3.participants_count = var5;
                     }
                  }
               } else {
                  this.chats.put(var1.id, var1);
               }
            } else {
               int var6 = 0;
               if (!var2) {
                  if (var3 != null) {
                     if (var1.version != var3.version) {
                        this.loadedFullChats.remove(var1.id);
                     }

                     var5 = var3.participants_count;
                     if (var5 != 0 && var1.participants_count == 0) {
                        var1.participants_count = var5;
                        var1.flags |= 131072;
                     }

                     var4 = var3.banned_rights;
                     if (var4 != null) {
                        var5 = var4.flags;
                     } else {
                        var5 = 0;
                     }

                     var4 = var1.banned_rights;
                     int var7;
                     if (var4 != null) {
                        var7 = var4.flags;
                     } else {
                        var7 = 0;
                     }

                     var4 = var3.default_banned_rights;
                     int var8;
                     if (var4 != null) {
                        var8 = var4.flags;
                     } else {
                        var8 = 0;
                     }

                     var4 = var1.default_banned_rights;
                     if (var4 != null) {
                        var6 = var4.flags;
                     }

                     var3.default_banned_rights = var1.default_banned_rights;
                     if (var3.default_banned_rights == null) {
                        var3.flags &= -262145;
                     } else {
                        var3.flags |= 262144;
                     }

                     var3.banned_rights = var1.banned_rights;
                     if (var3.banned_rights == null) {
                        var3.flags &= -32769;
                     } else {
                        var3.flags |= 32768;
                     }

                     var3.admin_rights = var1.admin_rights;
                     if (var3.admin_rights == null) {
                        var3.flags &= -16385;
                     } else {
                        var3.flags |= 16384;
                     }

                     if (var5 != var7 || var8 != var6) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$JQg5AlfE18YOamojW_v4DYkKyIA(this, var1));
                     }
                  }

                  this.chats.put(var1.id, var1);
               } else if (var3 == null) {
                  this.chats.put(var1.id, var1);
               } else if (var3.min) {
                  var1.min = false;
                  var1.title = var3.title;
                  var1.photo = var3.photo;
                  var1.broadcast = var3.broadcast;
                  var1.verified = var3.verified;
                  var1.megagroup = var3.megagroup;
                  var4 = var3.default_banned_rights;
                  if (var4 != null) {
                     var1.default_banned_rights = var4;
                     var1.flags |= 262144;
                  }

                  var9 = var3.admin_rights;
                  if (var9 != null) {
                     var1.admin_rights = var9;
                     var1.flags |= 16384;
                  }

                  var4 = var3.banned_rights;
                  if (var4 != null) {
                     var1.banned_rights = var4;
                     var1.flags |= 32768;
                  }

                  var10 = var3.username;
                  if (var10 != null) {
                     var1.username = var10;
                     var1.flags |= 64;
                  } else {
                     var1.flags &= -65;
                     var1.username = null;
                  }

                  var5 = var3.participants_count;
                  if (var5 != 0 && var1.participants_count == 0) {
                     var1.participants_count = var5;
                     var1.flags |= 131072;
                  }

                  this.chats.put(var1.id, var1);
               }
            }

         }
      }
   }

   public void putChats(ArrayList var1, boolean var2) {
      if (var1 != null && !var1.isEmpty()) {
         int var3 = var1.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            this.putChat((TLRPC.Chat)var1.get(var4), var2);
         }
      }

   }

   public void putEncryptedChat(TLRPC.EncryptedChat var1, boolean var2) {
      if (var1 != null) {
         if (var2) {
            this.encryptedChats.putIfAbsent(var1.id, var1);
         } else {
            this.encryptedChats.put(var1.id, var1);
         }

      }
   }

   public void putEncryptedChats(ArrayList var1, boolean var2) {
      if (var1 != null && !var1.isEmpty()) {
         int var3 = var1.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            this.putEncryptedChat((TLRPC.EncryptedChat)var1.get(var4), var2);
         }
      }

   }

   public boolean putUser(TLRPC.User var1, boolean var2) {
      if (var1 == null) {
         return false;
      } else {
         boolean var8;
         label83: {
            if (var2) {
               int var3 = var1.id;
               if (var3 / 1000 != 333 && var3 != 777000) {
                  var8 = true;
                  break label83;
               }
            }

            var8 = false;
         }

         TLRPC.User var4 = (TLRPC.User)this.users.get(var1.id);
         if (var4 == var1) {
            return false;
         } else {
            if (var4 != null && !TextUtils.isEmpty(var4.username)) {
               this.objectsByUsernames.remove(var4.username.toLowerCase());
            }

            if (!TextUtils.isEmpty(var1.username)) {
               this.objectsByUsernames.put(var1.username.toLowerCase(), var1);
            }

            String var5;
            if (var1.min) {
               if (var4 != null) {
                  if (!var8) {
                     if (var1.bot) {
                        var5 = var1.username;
                        if (var5 != null) {
                           var4.username = var5;
                           var4.flags |= 8;
                        } else {
                           var4.flags &= -9;
                           var4.username = null;
                        }
                     }

                     TLRPC.UserProfilePhoto var6 = var1.photo;
                     if (var6 != null) {
                        var4.photo = var6;
                        var4.flags |= 32;
                     } else {
                        var4.flags &= -33;
                        var4.photo = null;
                     }
                  }
               } else {
                  this.users.put(var1.id, var1);
               }
            } else if (!var8) {
               this.users.put(var1.id, var1);
               if (var1.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                  UserConfig.getInstance(this.currentAccount).setCurrentUser(var1);
                  UserConfig.getInstance(this.currentAccount).saveConfig(true);
               }

               if (var4 != null) {
                  TLRPC.UserStatus var7 = var1.status;
                  if (var7 != null) {
                     TLRPC.UserStatus var9 = var4.status;
                     if (var9 != null && var7.expires != var9.expires) {
                        return true;
                     }
                  }
               }
            } else if (var4 == null) {
               this.users.put(var1.id, var1);
            } else if (var4.min) {
               var1.min = false;
               if (var4.bot) {
                  var5 = var4.username;
                  if (var5 != null) {
                     var1.username = var5;
                     var1.flags |= 8;
                  } else {
                     var1.flags &= -9;
                     var1.username = null;
                  }
               }

               TLRPC.UserProfilePhoto var10 = var4.photo;
               if (var10 != null) {
                  var1.photo = var10;
                  var1.flags |= 32;
               } else {
                  var1.flags &= -33;
                  var1.photo = null;
               }

               this.users.put(var1.id, var1);
            }

            return false;
         }
      }
   }

   public void putUsers(ArrayList var1, boolean var2) {
      if (var1 != null && !var1.isEmpty()) {
         int var3 = var1.size();
         int var4 = 0;

         boolean var5;
         for(var5 = false; var4 < var3; ++var4) {
            if (this.putUser((TLRPC.User)var1.get(var4), var2)) {
               var5 = true;
            }
         }

         if (var5) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$MHasUaMkg9dD_kjYrwSO8zJY7Bs(this));
         }
      }

   }

   public void registerForPush(String var1) {
      if (!TextUtils.isEmpty(var1) && !this.registeringForPush && UserConfig.getInstance(this.currentAccount).getClientUserId() != 0) {
         if (UserConfig.getInstance(this.currentAccount).registeredForPush && var1.equals(SharedConfig.pushString)) {
            return;
         }

         this.registeringForPush = true;
         this.lastPushRegisterSendTime = SystemClock.elapsedRealtime();
         if (SharedConfig.pushAuthKey == null) {
            SharedConfig.pushAuthKey = new byte[256];
            Utilities.random.nextBytes(SharedConfig.pushAuthKey);
            SharedConfig.saveConfig();
         }

         TLRPC.TL_account_registerDevice var2 = new TLRPC.TL_account_registerDevice();
         var2.token_type = 2;
         var2.token = var1;
         var2.secret = SharedConfig.pushAuthKey;

         for(int var3 = 0; var3 < 3; ++var3) {
            UserConfig var4 = UserConfig.getInstance(var3);
            if (var3 != this.currentAccount && var4.isClientActivated()) {
               int var5 = var4.getClientUserId();
               var2.other_uids.add(var5);
               if (BuildVars.LOGS_ENABLED) {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("add other uid = ");
                  var6.append(var5);
                  var6.append(" for account ");
                  var6.append(this.currentAccount);
                  FileLog.d(var6.toString());
               }
            }
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$MessagesController$RQIUdGcrH5D0HSeyADRBCxVETRg(this, var1));
      }

   }

   public void reloadMentionsCountForChannels(ArrayList var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$Wb5lXI806u6uYMpUfHZLtqjdAzg(this, var1));
   }

   public void reloadWebPages(long var1, HashMap var3) {
      Iterator var4 = var3.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var8 = (Entry)var4.next();
         String var5 = (String)var8.getKey();
         ArrayList var6 = (ArrayList)var8.getValue();
         ArrayList var7 = (ArrayList)this.reloadingWebpages.get(var5);
         ArrayList var9 = var7;
         if (var7 == null) {
            var9 = new ArrayList();
            this.reloadingWebpages.put(var5, var9);
         }

         var9.addAll(var6);
         TLRPC.TL_messages_getWebPagePreview var10 = new TLRPC.TL_messages_getWebPagePreview();
         var10.message = var5;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$MessagesController$4nBTlb9eBU7C4QavllU2UZiopks(this, var5, var1));
      }

   }

   protected void removeDeletedMessagesFromArray(long var1, ArrayList var3) {
      LongSparseArray var4 = this.deletedHistory;
      int var5 = 0;
      int var6 = (Integer)var4.get(var1, 0);
      if (var6 != 0) {
         int var9;
         for(int var7 = var3.size(); var5 < var7; var7 = var9) {
            int var8 = var5;
            var9 = var7;
            if (((TLRPC.Message)var3.get(var5)).id <= var6) {
               var3.remove(var5);
               var8 = var5 - 1;
               var9 = var7 - 1;
            }

            var5 = var8 + 1;
         }

      }
   }

   public void removeDialogAction(long var1, boolean var3, boolean var4) {
      TLRPC.Dialog var5 = (TLRPC.Dialog)this.dialogs_dict.get(var1);
      if (var5 != null) {
         if (var3) {
            this.clearingHistoryDialogs.remove(var1);
         } else {
            this.deletingDialogs.remove(var1);
            if (!var4) {
               this.allDialogs.add(var5);
               this.sortDialogs((SparseArray)null);
            }
         }

         if (!var4) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
         }

      }
   }

   public void reorderPinnedDialogs(int var1, ArrayList var2, long var3) {
      TLRPC.TL_messages_reorderPinnedDialogs var5 = new TLRPC.TL_messages_reorderPinnedDialogs();
      var5.folder_id = var1;
      var5.force = true;
      if (var3 == 0L) {
         var2 = this.getDialogs(var1);
         if (var2.isEmpty()) {
            return;
         }

         int var6 = var2.size();
         byte var7 = 0;
         int var8 = 0;

         int var9;
         int var11;
         for(var9 = 0; var8 < var6; var9 = var11) {
            TLRPC.Dialog var10 = (TLRPC.Dialog)var2.get(var8);
            if (var10 instanceof TLRPC.TL_dialogFolder) {
               var11 = var9;
            } else {
               if (!var10.pinned) {
                  break;
               }

               MessagesStorage.getInstance(this.currentAccount).setDialogPinned(var10.id, var10.pinnedNum);
               var11 = var9;
               if ((int)var10.id != 0) {
                  TLRPC.InputPeer var12 = this.getInputPeer((int)((TLRPC.Dialog)var2.get(var8)).id);
                  TLRPC.TL_inputDialogPeer var17 = new TLRPC.TL_inputDialogPeer();
                  var17.peer = var12;
                  var5.order.add(var17);
                  var11 = var9 + var17.getObjectSize();
               }
            }

            ++var8;
         }

         NativeByteBuffer var19;
         label53: {
            NativeByteBuffer var16;
            Exception var18;
            label69: {
               try {
                  var16 = new NativeByteBuffer(var9 + 12);
               } catch (Exception var15) {
                  var18 = var15;
                  var16 = null;
                  break label69;
               }

               Exception var10000;
               label70: {
                  boolean var10001;
                  try {
                     var16.writeInt32(16);
                     var16.writeInt32(var1);
                     var16.writeInt32(var5.order.size());
                     var11 = var5.order.size();
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label70;
                  }

                  var1 = var7;

                  while(true) {
                     var19 = var16;
                     if (var1 >= var11) {
                        break label53;
                     }

                     try {
                        ((TLRPC.InputDialogPeer)var5.order.get(var1)).serializeToStream(var16);
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                        break;
                     }

                     ++var1;
                  }
               }

               var18 = var10000;
            }

            FileLog.e((Throwable)var18);
            var19 = var16;
         }

         var3 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var19);
      } else {
         var5.order = var2;
      }

      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, new _$$Lambda$MessagesController$iDjQfJMpGMx6ueasQejBNbv1luw(this, var3));
   }

   public void reportSpam(long var1, TLRPC.User var3, TLRPC.Chat var4, TLRPC.EncryptedChat var5) {
      if (var3 != null || var4 != null || var5 != null) {
         Editor var6 = this.notificationsPreferences.edit();
         StringBuilder var7 = new StringBuilder();
         var7.append("spam3_");
         var7.append(var1);
         var6.putInt(var7.toString(), 1);
         var6.commit();
         if ((int)var1 == 0) {
            if (var5 == null || var5.access_hash == 0L) {
               return;
            }

            TLRPC.TL_messages_reportEncryptedSpam var9 = new TLRPC.TL_messages_reportEncryptedSpam();
            var9.peer = new TLRPC.TL_inputEncryptedChat();
            TLRPC.TL_inputEncryptedChat var8 = var9.peer;
            var8.chat_id = var5.id;
            var8.access_hash = var5.access_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var9, _$$Lambda$MessagesController$z4dPdFyhgWJPX_RIzHIHAuPr2cc.INSTANCE, 2);
         } else {
            TLRPC.TL_messages_reportSpam var10 = new TLRPC.TL_messages_reportSpam();
            if (var4 != null) {
               var10.peer = this.getInputPeer(-var4.id);
            } else if (var3 != null) {
               var10.peer = this.getInputPeer(var3.id);
            }

            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, _$$Lambda$MessagesController$A_WQ0vAsaYkwbum0RRlzdSDsvGo.INSTANCE, 2);
         }

      }
   }

   public void saveGif(Object var1, TLRPC.Document var2) {
      if (var1 != null && MessageObject.isGifDocument(var2)) {
         TLRPC.TL_messages_saveGif var3 = new TLRPC.TL_messages_saveGif();
         var3.id = new TLRPC.TL_inputDocument();
         TLRPC.InputDocument var4 = var3.id;
         var4.id = var2.id;
         var4.access_hash = var2.access_hash;
         var4.file_reference = var2.file_reference;
         if (var4.file_reference == null) {
            var4.file_reference = new byte[0];
         }

         var3.unsave = false;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$AJRaVVVB2_z__jsu2wGba_x0X0k(this, var1, var3));
      }

   }

   public void saveRecentSticker(Object var1, TLRPC.Document var2, boolean var3) {
      if (var1 != null && var2 != null) {
         TLRPC.TL_messages_saveRecentSticker var4 = new TLRPC.TL_messages_saveRecentSticker();
         var4.id = new TLRPC.TL_inputDocument();
         TLRPC.InputDocument var5 = var4.id;
         var5.id = var2.id;
         var5.access_hash = var2.access_hash;
         var5.file_reference = var2.file_reference;
         if (var5.file_reference == null) {
            var5.file_reference = new byte[0];
         }

         var4.unsave = false;
         var4.attached = var3;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$MessagesController$Ac6iMOZ3v5XiuSoBQXhRvLk2mds(this, var1, var4));
      }

   }

   public void saveWallpaperToServer(File var1, long var2, long var4, boolean var6, boolean var7, int var8, float var9, boolean var10, long var11) {
      if (this.uploadingWallpaper != null) {
         File var13 = ApplicationLoader.getFilesDirFixed();
         String var14;
         if (this.uploadingWallpaperBlurred) {
            var14 = "wallpaper_original.jpg";
         } else {
            var14 = "wallpaper.jpg";
         }

         File var21 = new File(var13, var14);
         if (var1 != null && (var1.getAbsolutePath().equals(this.uploadingWallpaper) || var1.equals(var21))) {
            this.uploadingWallpaperMotion = var7;
            this.uploadingWallpaperBlurred = var6;
            return;
         }

         FileLoader.getInstance(this.currentAccount).cancelUploadFile(this.uploadingWallpaper, false);
         this.uploadingWallpaper = null;
      }

      if (var1 != null) {
         this.uploadingWallpaper = var1.getAbsolutePath();
         this.uploadingWallpaperMotion = var7;
         this.uploadingWallpaperBlurred = var6;
         FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingWallpaper, false, true, 16777216);
      } else if (var4 != 0L) {
         TLRPC.TL_inputWallPaper var19 = new TLRPC.TL_inputWallPaper();
         var19.id = var2;
         var19.access_hash = var4;
         TLRPC.TL_wallPaperSettings var17 = new TLRPC.TL_wallPaperSettings();
         var17.blur = var6;
         var17.motion = var7;
         if (var8 != 0) {
            var17.background_color = var8;
            var17.flags |= 1;
            var17.intensity = (int)(100.0F * var9);
            var17.flags |= 8;
         }

         Object var22;
         if (var10) {
            var22 = new TLRPC.TL_account_installWallPaper();
            ((TLRPC.TL_account_installWallPaper)var22).wallpaper = var19;
            ((TLRPC.TL_account_installWallPaper)var22).settings = var17;
         } else {
            var22 = new TLRPC.TL_account_saveWallPaper();
            ((TLRPC.TL_account_saveWallPaper)var22).wallpaper = var19;
            ((TLRPC.TL_account_saveWallPaper)var22).settings = var17;
         }

         if (var11 == 0L) {
            NativeByteBuffer var18;
            label45: {
               Exception var20;
               label44: {
                  try {
                     var18 = new NativeByteBuffer(44);
                  } catch (Exception var16) {
                     var20 = var16;
                     var18 = null;
                     break label44;
                  }

                  try {
                     var18.writeInt32(12);
                     var18.writeInt64(var2);
                     var18.writeInt64(var4);
                     var18.writeBool(var6);
                     var18.writeBool(var7);
                     var18.writeInt32(var8);
                     var18.writeDouble((double)var9);
                     var18.writeBool(var10);
                     break label45;
                  } catch (Exception var15) {
                     var20 = var15;
                  }
               }

               FileLog.e((Throwable)var20);
            }

            var11 = MessagesStorage.getInstance(this.currentAccount).createPendingTask(var18);
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)var22, new _$$Lambda$MessagesController$YmKMKppARt1F8875D8216H1_tmM(this, var11, var10, var2));
      }

   }

   public void sendBotStart(TLRPC.User var1, String var2) {
      if (var1 != null) {
         TLRPC.TL_messages_startBot var3 = new TLRPC.TL_messages_startBot();
         var3.bot = this.getInputUser(var1);
         var3.peer = this.getInputPeer(var1.id);
         var3.start_param = var2;
         var3.random_id = Utilities.random.nextLong();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$dxMathd1bP5_uSwd4Gt7ZvEOFtw(this));
      }
   }

   public void sendTyping(long var1, int var3, int var4) {
      if (var1 != 0L) {
         LongSparseArray var5 = (LongSparseArray)this.sendingTypings.get(var3);
         if (var5 == null || var5.get(var1) == null) {
            LongSparseArray var6 = var5;
            if (var5 == null) {
               var6 = new LongSparseArray();
               this.sendingTypings.put(var3, var6);
            }

            int var7 = (int)var1;
            int var8 = (int)(var1 >> 32);
            if (var7 != 0) {
               if (var8 == 1) {
                  return;
               }

               TLRPC.TL_messages_setTyping var12 = new TLRPC.TL_messages_setTyping();
               var12.peer = this.getInputPeer(var7);
               TLRPC.InputPeer var14 = var12.peer;
               if (var14 instanceof TLRPC.TL_inputPeerChannel) {
                  TLRPC.Chat var15 = this.getChat(var14.channel_id);
                  if (var15 == null || !var15.megagroup) {
                     return;
                  }
               }

               if (var12.peer == null) {
                  return;
               }

               if (var3 == 0) {
                  var12.action = new TLRPC.TL_sendMessageTypingAction();
               } else if (var3 == 1) {
                  var12.action = new TLRPC.TL_sendMessageRecordAudioAction();
               } else if (var3 == 2) {
                  var12.action = new TLRPC.TL_sendMessageCancelAction();
               } else if (var3 == 3) {
                  var12.action = new TLRPC.TL_sendMessageUploadDocumentAction();
               } else if (var3 == 4) {
                  var12.action = new TLRPC.TL_sendMessageUploadPhotoAction();
               } else if (var3 == 5) {
                  var12.action = new TLRPC.TL_sendMessageUploadVideoAction();
               } else if (var3 == 6) {
                  var12.action = new TLRPC.TL_sendMessageGamePlayAction();
               } else if (var3 == 7) {
                  var12.action = new TLRPC.TL_sendMessageRecordRoundAction();
               } else if (var3 == 8) {
                  var12.action = new TLRPC.TL_sendMessageUploadRoundAction();
               } else if (var3 == 9) {
                  var12.action = new TLRPC.TL_sendMessageUploadAudioAction();
               }

               var6.put(var1, true);
               var3 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var12, new _$$Lambda$MessagesController$0RUkjN1o4ur0BN0_Uwykda9jP9E(this, var3, var1), 2);
               if (var4 != 0) {
                  ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var3, var4);
               }
            } else {
               if (var3 != 0) {
                  return;
               }

               TLRPC.EncryptedChat var11 = this.getEncryptedChat(var8);
               byte[] var9 = var11.auth_key;
               if (var9 != null && var9.length > 1 && var11 instanceof TLRPC.TL_encryptedChat) {
                  TLRPC.TL_messages_setEncryptedTyping var10 = new TLRPC.TL_messages_setEncryptedTyping();
                  var10.peer = new TLRPC.TL_inputEncryptedChat();
                  TLRPC.TL_inputEncryptedChat var13 = var10.peer;
                  var13.chat_id = var11.id;
                  var13.access_hash = var11.access_hash;
                  var10.typing = true;
                  var6.put(var1, true);
                  var3 = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$MessagesController$46d8KXzv97zrAkyG5uwxOcxtLc4(this, var3, var1), 2);
                  if (var4 != 0) {
                     ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(var3, var4);
                  }
               }
            }

         }
      }
   }

   public void setDefaultBannedRole(int var1, TLRPC.TL_chatBannedRights var2, boolean var3, BaseFragment var4) {
      if (var2 != null) {
         TLRPC.TL_messages_editChatDefaultBannedRights var5 = new TLRPC.TL_messages_editChatDefaultBannedRights();
         var5.peer = this.getInputPeer(-var1);
         var5.banned_rights = var2;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var5, new _$$Lambda$MessagesController$1P_GCY_PWvevMznLENn0Y1ly8wA(this, var1, var4, var5, var3));
      }
   }

   public void setDialogsInTransaction(boolean var1) {
      this.dialogsInTransaction = var1;
      if (!var1) {
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, true);
      }

   }

   public void setLastCreatedDialogId(long var1, boolean var3) {
      if (var3) {
         if (this.createdDialogMainThreadIds.contains(var1)) {
            return;
         }

         this.createdDialogMainThreadIds.add(var1);
      } else {
         this.createdDialogMainThreadIds.remove(var1);
         SparseArray var4 = (SparseArray)this.pollsToCheck.get(var1);
         if (var4 != null) {
            int var5 = var4.size();

            for(int var6 = 0; var6 < var5; ++var6) {
               ((MessageObject)var4.valueAt(var6)).pollVisibleOnScreen = false;
            }
         }
      }

      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$GClBurUFA5765ROku5c8uOseY60(this, var3, var1));
   }

   public void setLastVisibleDialogId(long var1, boolean var3) {
      if (var3) {
         if (this.visibleDialogMainThreadIds.contains(var1)) {
            return;
         }

         this.visibleDialogMainThreadIds.add(var1);
      } else {
         this.visibleDialogMainThreadIds.remove(var1);
      }

   }

   public void setReferer(String var1) {
      if (var1 != null) {
         this.installReferer = var1;
         this.mainPreferences.edit().putString("installReferer", var1).commit();
      }
   }

   public void setUserAdminRole(int var1, TLRPC.User var2, TLRPC.TL_chatAdminRights var3, boolean var4, BaseFragment var5, boolean var6) {
      if (var2 != null && var3 != null) {
         TLRPC.Chat var7 = this.getChat(var1);
         if (ChatObject.isChannel(var7)) {
            TLRPC.TL_channels_editAdmin var8 = new TLRPC.TL_channels_editAdmin();
            var8.channel = getInputChannel(var7);
            var8.user_id = this.getInputUser(var2);
            var8.admin_rights = var3;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(var8, new _$$Lambda$MessagesController$ruvO2GOVq6vr5RbeSeoQf9j2_3I(this, var1, var5, var8, var4));
         } else {
            TLRPC.TL_messages_editChatAdmin var10 = new TLRPC.TL_messages_editChatAdmin();
            var10.chat_id = var1;
            var10.user_id = this.getInputUser(var2);
            if (!var3.change_info && !var3.delete_messages && !var3.ban_users && !var3.invite_users && !var3.pin_messages && !var3.add_admins) {
               var4 = false;
            } else {
               var4 = true;
            }

            var10.is_admin = var4;
            _$$Lambda$MessagesController$oInAanuig7UbxR7DhBxjsIzAZu4 var9 = new _$$Lambda$MessagesController$oInAanuig7UbxR7DhBxjsIzAZu4(this, var1, var5, var10);
            if (var10.is_admin && var6) {
               this.addUserToChat(var1, var2, (TLRPC.ChatFull)null, 0, (String)null, var5, new _$$Lambda$MessagesController$re3lVjnmONchziB0xgSqaVhxV5Q(this, var10, var9));
            } else {
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, var9);
            }
         }
      }

   }

   public void setUserBannedRole(int var1, TLRPC.User var2, TLRPC.TL_chatBannedRights var3, boolean var4, BaseFragment var5) {
      if (var2 != null && var3 != null) {
         TLRPC.TL_channels_editBanned var6 = new TLRPC.TL_channels_editBanned();
         var6.channel = this.getInputChannel(var1);
         var6.user_id = this.getInputUser(var2);
         var6.banned_rights = var3;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var6, new _$$Lambda$MessagesController$PTatBcwlbxdtRxQAlB57_yJjqhU(this, var1, var5, var6, var4));
      }

   }

   public void sortDialogs(SparseArray var1) {
      this.dialogsServerOnly.clear();
      this.dialogsCanAddUsers.clear();
      this.dialogsChannelsOnly.clear();
      this.dialogsGroupsOnly.clear();
      this.dialogsUsersOnly.clear();
      this.dialogsForward.clear();
      byte var2 = 0;

      int var3;
      for(var3 = 0; var3 < this.dialogsByFolder.size(); ++var3) {
         ArrayList var4 = (ArrayList)this.dialogsByFolder.get(var3);
         if (var4 != null) {
            var4.clear();
         }
      }

      this.unreadUnmutedDialogs = 0;
      int var5 = UserConfig.getInstance(this.currentAccount).getClientUserId();
      Collections.sort(this.allDialogs, this.dialogComparator);
      this.isLeftProxyChannel = true;
      TLRPC.Dialog var19 = this.proxyDialog;
      long var6;
      if (var19 != null) {
         var6 = var19.id;
         if (var6 < 0L) {
            TLRPC.Chat var20 = this.getChat(-((int)var6));
            if (var20 != null && !var20.left) {
               this.isLeftProxyChannel = false;
            }
         }
      }

      boolean var8 = NotificationsController.getInstance(this.currentAccount).showBadgeMessages;
      int var9 = this.allDialogs.size();
      int var10 = 0;

      boolean var18;
      for(var18 = false; var10 < var9; ++var10) {
         label162: {
            var19 = (TLRPC.Dialog)this.allDialogs.get(var10);
            var6 = var19.id;
            int var11 = (int)(var6 >> 32);
            int var12 = (int)var6;
            boolean var13 = var18;
            if (var19 instanceof TLRPC.TL_dialog) {
               boolean var22;
               label160: {
                  if (var12 != 0 && var11 != 1) {
                     this.dialogsServerOnly.add(var19);
                     TLRPC.Chat var14;
                     if (!DialogObject.isChannel(var19)) {
                        if (var12 < 0) {
                           if (var1 != null) {
                              var14 = (TLRPC.Chat)var1.get(-var12);
                              if (var14 != null && var14.migrated_to != null) {
                                 this.allDialogs.remove(var10);
                                 break label162;
                              }
                           }

                           this.dialogsCanAddUsers.add(var19);
                           this.dialogsGroupsOnly.add(var19);
                        } else if (var12 > 0) {
                           this.dialogsUsersOnly.add(var19);
                        }
                     } else {
                        var14 = this.getChat(-var12);
                        if (var14 != null) {
                           label131: {
                              label130: {
                                 if (var14.megagroup) {
                                    TLRPC.TL_chatAdminRights var15 = var14.admin_rights;
                                    if (var15 != null && (var15.post_messages || var15.add_admins)) {
                                       break label130;
                                    }
                                 }

                                 if (!var14.creator) {
                                    break label131;
                                 }
                              }

                              this.dialogsCanAddUsers.add(var19);
                           }
                        }

                        if (var14 != null && var14.megagroup) {
                           this.dialogsGroupsOnly.add(var19);
                        } else {
                           this.dialogsChannelsOnly.add(var19);
                           if (!ChatObject.hasAdminRights(var14) || !ChatObject.canPost(var14)) {
                              var22 = false;
                              break label160;
                           }
                        }
                     }
                  }

                  var22 = true;
               }

               var13 = var18;
               if (var22) {
                  if (var12 == var5) {
                     this.dialogsForward.add(0, var19);
                     var13 = true;
                  } else {
                     this.dialogsForward.add(var19);
                     var13 = var18;
                  }
               }
            }

            if ((var19.unread_count != 0 || var19.unread_mark) && !this.isDialogMuted(var19.id)) {
               ++this.unreadUnmutedDialogs;
            }

            TLRPC.Dialog var23 = this.proxyDialog;
            if (var23 == null || var19.id != var23.id || !this.isLeftProxyChannel) {
               this.addDialogToItsFolder(-1, var19, var8);
               var18 = var13;
               continue;
            }

            this.allDialogs.remove(var10);
            var18 = var13;
         }

         --var10;
         --var9;
      }

      TLRPC.Dialog var16 = this.proxyDialog;
      if (var16 != null && this.isLeftProxyChannel) {
         this.allDialogs.add(0, var16);
         this.addDialogToItsFolder(-2, this.proxyDialog, var8);
      }

      int var24 = var2;
      if (!var18) {
         TLRPC.User var21 = UserConfig.getInstance(this.currentAccount).getCurrentUser();
         var24 = var2;
         if (var21 != null) {
            TLRPC.TL_dialog var17 = new TLRPC.TL_dialog();
            var17.id = (long)var21.id;
            var17.notify_settings = new TLRPC.TL_peerNotifySettings();
            var17.peer = new TLRPC.TL_peerUser();
            var17.peer.user_id = var21.id;
            this.dialogsForward.add(0, var17);
            var24 = var2;
         }
      }

      for(; var24 < this.dialogsByFolder.size(); ++var24) {
         var3 = this.dialogsByFolder.keyAt(var24);
         if (((ArrayList)this.dialogsByFolder.valueAt(var24)).isEmpty()) {
            this.dialogsByFolder.remove(var3);
         }
      }

   }

   public void startShortPoll(TLRPC.Chat var1, boolean var2) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$MessagesController$ChpY_h3KplD8DdHCKUKN4FRi5P8(this, var2, var1));
   }

   public void toogleChannelInvitesHistory(int var1, boolean var2) {
      TLRPC.TL_channels_togglePreHistoryHidden var3 = new TLRPC.TL_channels_togglePreHistoryHidden();
      var3.channel = this.getInputChannel(var1);
      var3.enabled = var2;
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$YebMv5xtYdxqizkIFhqWdfrEUcI(this), 64);
   }

   public void toogleChannelSignatures(int var1, boolean var2) {
      TLRPC.TL_channels_toggleSignatures var3 = new TLRPC.TL_channels_toggleSignatures();
      var3.channel = this.getInputChannel(var1);
      var3.enabled = var2;
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$aeXtWX5NT1IBK6iBhloNZ1bs_M4(this), 64);
   }

   public void unblockUser(int var1) {
      TLRPC.TL_contacts_unblock var2 = new TLRPC.TL_contacts_unblock();
      TLRPC.User var3 = this.getUser(var1);
      if (var3 != null) {
         this.blockedUsers.delete(var3.id);
         var2.id = this.getInputUser(var3);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoad);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$MessagesController$opwePRyqFhALjQxPzCei2q6RZPU(this, var3));
      }
   }

   public void unregistedPush() {
      if (UserConfig.getInstance(this.currentAccount).registeredForPush && SharedConfig.pushString.length() == 0) {
         TLRPC.TL_account_unregisterDevice var1 = new TLRPC.TL_account_unregisterDevice();
         var1.token = SharedConfig.pushString;
         var1.token_type = 2;

         for(int var2 = 0; var2 < 3; ++var2) {
            UserConfig var3 = UserConfig.getInstance(var2);
            if (var2 != this.currentAccount && var3.isClientActivated()) {
               var1.other_uids.add(var3.getClientUserId());
            }
         }

         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var1, _$$Lambda$MessagesController$8PliyHoMopJhuU4eiI2hN2lPvKs.INSTANCE);
      }

   }

   public void updateChannelUserName(int var1, String var2) {
      TLRPC.TL_channels_updateUsername var3 = new TLRPC.TL_channels_updateUsername();
      var3.channel = this.getInputChannel(var1);
      var3.username = var2;
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$Ov1XVh0U9G1mRr1zQgjy3_Hz5dQ(this, var1, var2), 64);
   }

   public void updateChatAbout(int var1, String var2, TLRPC.ChatFull var3) {
      if (var3 != null) {
         TLRPC.TL_messages_editChatAbout var4 = new TLRPC.TL_messages_editChatAbout();
         var4.peer = this.getInputPeer(-var1);
         var4.about = var2;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$MessagesController$7X_q4bvD4zimEhOUjsZ_PwkItoo(this, var3, var2), 64);
      }
   }

   public void updateConfig(TLRPC.TL_config var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$E_B3T1gXoq4eD739GgHdBDLPpBs(this, var1));
   }

   protected void updateInterfaceWithMessages(long var1, ArrayList var3) {
      this.updateInterfaceWithMessages(var1, var3, false);
   }

   protected void updateInterfaceWithMessages(long var1, ArrayList var3, boolean var4) {
      if (var3 != null && !var3.isEmpty()) {
         int var5 = (int)var1;
         boolean var6 = false;
         boolean var7 = false;
         boolean var22;
         if (var5 == 0) {
            var22 = true;
         } else {
            var22 = false;
         }

         int var8 = 0;
         MessageObject var9 = null;
         boolean var10 = false;
         int var11 = 0;
         boolean var12 = false;

         while(true) {
            MessageObject var14;
            boolean var26;
            if (var8 >= var3.size()) {
               DataQuery.getInstance(this.currentAccount).loadReplyMessagesForMessages(var3, var1);
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didReceiveNewMessages, var1, var3);
               if (var9 == null) {
                  return;
               }

               TLRPC.TL_dialog var20 = (TLRPC.TL_dialog)this.dialogs_dict.get(var1);
               if (var9.messageOwner.action instanceof TLRPC.TL_messageActionChatMigrateTo) {
                  if (var20 != null) {
                     this.allDialogs.remove(var20);
                     this.dialogsServerOnly.remove(var20);
                     this.dialogsCanAddUsers.remove(var20);
                     this.dialogsChannelsOnly.remove(var20);
                     this.dialogsGroupsOnly.remove(var20);
                     this.dialogsUsersOnly.remove(var20);
                     this.dialogsForward.remove(var20);
                     this.dialogs_dict.remove(var20.id);
                     this.dialogs_read_inbox_max.remove(var20.id);
                     this.dialogs_read_outbox_max.remove(var20.id);
                     var5 = this.nextDialogsCacheOffset.get(var20.folder_id, 0);
                     if (var5 > 0) {
                        this.nextDialogsCacheOffset.put(var20.folder_id, var5 - 1);
                     }

                     this.dialogMessage.remove(var20.id);
                     ArrayList var25 = (ArrayList)this.dialogsByFolder.get(var20.folder_id);
                     if (var25 != null) {
                        var25.remove(var20);
                     }

                     var9 = (MessageObject)this.dialogMessagesByIds.get(var20.top_message);
                     this.dialogMessagesByIds.remove(var20.top_message);
                     if (var9 != null) {
                        var1 = var9.messageOwner.random_id;
                        if (var1 != 0L) {
                           this.dialogMessagesByRandomIds.remove(var1);
                        }
                     }

                     var20.top_message = 0;
                     NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(var20.id);
                     NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needReloadRecentDialogsSearch);
                  }

                  return;
               }

               long var18;
               if (var20 == null) {
                  var22 = var6;
                  if (!var4) {
                     TLRPC.Chat var21 = this.getChat(var11);
                     if (var11 != 0 && var21 == null || var21 != null && var21.left) {
                        return;
                     }

                     if (BuildVars.LOGS_ENABLED) {
                        StringBuilder var27 = new StringBuilder();
                        var27.append("not found dialog with id ");
                        var27.append(var1);
                        var27.append(" dictCount = ");
                        var27.append(this.dialogs_dict.size());
                        var27.append(" allCount = ");
                        var27.append(this.allDialogs.size());
                        FileLog.d(var27.toString());
                     }

                     TLRPC.TL_dialog var28 = new TLRPC.TL_dialog();
                     var28.id = var1;
                     var28.unread_count = 0;
                     var28.top_message = var9.getId();
                     var28.last_message_date = var9.messageOwner.date;
                     var28.flags = ChatObject.isChannel(var21);
                     this.dialogs_dict.put(var1, var28);
                     this.allDialogs.add(var28);
                     this.dialogMessage.put(var1, var9);
                     if (var9.messageOwner.to_id.channel_id == 0) {
                        this.dialogMessagesByIds.put(var9.getId(), var9);
                        var18 = var9.messageOwner.random_id;
                        if (var18 != 0L) {
                           this.dialogMessagesByRandomIds.put(var18, var9);
                        }
                     }

                     MessagesStorage.getInstance(this.currentAccount).getDialogFolderId(var1, new _$$Lambda$MessagesController$UGzhyEr_7mVrPzKWG0QmrzUROSg(this, var28, var1));
                     var22 = true;
                  }
               } else {
                  label210: {
                     if (var10) {
                        var26 = var7;
                        if (var20.folder_id == 1) {
                           var26 = var7;
                           if (!this.isDialogMuted(var20.id)) {
                              var20.folder_id = 0;
                              var20.pinned = false;
                              var20.pinnedNum = 0;
                              MessagesStorage.getInstance(this.currentAccount).setDialogsFolderId((ArrayList)null, (ArrayList)null, var20.id, 0);
                              var26 = true;
                           }
                        }
                     } else {
                        var26 = var7;
                     }

                     boolean var23 = true;
                     if ((var20.top_message <= 0 || var9.getId() <= 0 || var9.getId() <= var20.top_message) && (var20.top_message >= 0 || var9.getId() >= 0 || var9.getId() >= var20.top_message) && this.dialogMessage.indexOfKey(var1) >= 0 && var20.top_message >= 0) {
                        var22 = var26;
                        if (var20.last_message_date > var9.messageOwner.date) {
                           break label210;
                        }
                     }

                     var14 = (MessageObject)this.dialogMessagesByIds.get(var20.top_message);
                     this.dialogMessagesByIds.remove(var20.top_message);
                     if (var14 != null) {
                        var18 = var14.messageOwner.random_id;
                        if (var18 != 0L) {
                           this.dialogMessagesByRandomIds.remove(var18);
                        }
                     }

                     var20.top_message = var9.getId();
                     if (!var4) {
                        var20.last_message_date = var9.messageOwner.date;
                        var22 = var23;
                     } else {
                        var22 = var26;
                     }

                     this.dialogMessage.put(var1, var9);
                     if (var9.messageOwner.to_id.channel_id == 0) {
                        this.dialogMessagesByIds.put(var9.getId(), var9);
                        var18 = var9.messageOwner.random_id;
                        if (var18 != 0L) {
                           this.dialogMessagesByRandomIds.put(var18, var9);
                        }
                     }
                  }
               }

               if (var22) {
                  this.sortDialogs((SparseArray)null);
               }

               if (var12) {
                  DataQuery.getInstance(this.currentAccount).increasePeerRaiting(var1);
               }
               break;
            }

            MessageObject var13;
            int var15;
            label203: {
               var13 = (MessageObject)var3.get(var8);
               if (var9 != null && (var22 || var13.getId() <= var9.getId()) && (!var22 && (var13.getId() >= 0 || var9.getId() >= 0) || var13.getId() >= var9.getId())) {
                  var14 = var9;
                  var15 = var11;
                  if (var13.messageOwner.date <= var9.messageOwner.date) {
                     break label203;
                  }
               }

               var15 = var13.messageOwner.to_id.channel_id;
               if (var15 != 0) {
                  var11 = var15;
               }

               var14 = var13;
               var15 = var11;
            }

            var26 = var10;
            if (!var10) {
               var26 = var10;
               if (!var13.isOut()) {
                  var26 = true;
               }
            }

            if (var13.isOut() && !var13.isSending() && !var13.isForwarded()) {
               TLRPC.Message var16;
               DataQuery var24;
               if (var13.isNewGif()) {
                  var24 = DataQuery.getInstance(this.currentAccount);
                  var16 = var13.messageOwner;
                  var24.addRecentGif(var16.media.document, var16.date);
               } else if (var13.isSticker()) {
                  var24 = DataQuery.getInstance(this.currentAccount);
                  var16 = var13.messageOwner;
                  var24.addRecentSticker(0, var13, var16.media.document, var16.date, false);
               }
            }

            boolean var17 = var12;
            if (var13.isOut()) {
               var17 = var12;
               if (var13.isSent()) {
                  var17 = true;
               }
            }

            ++var8;
            var9 = var14;
            var10 = var26;
            var11 = var15;
            var12 = var17;
         }
      }

   }

   public void updateTimerProc() {
      long var1 = System.currentTimeMillis();
      this.checkDeletingTask(false);
      this.checkReadTasks();
      int var4;
      int var5;
      if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
         TLRPC.TL_account_updateStatus var3;
         if (ConnectionsManager.getInstance(this.currentAccount).getPauseTime() == 0L && ApplicationLoader.isScreenOn && !ApplicationLoader.mainInterfacePausedStageQueue) {
            if (ApplicationLoader.mainInterfacePausedStageQueueTime != 0L && Math.abs(ApplicationLoader.mainInterfacePausedStageQueueTime - System.currentTimeMillis()) > 1000L && this.statusSettingState != 1 && (this.lastStatusUpdateTime == 0L || Math.abs(System.currentTimeMillis() - this.lastStatusUpdateTime) >= 55000L || this.offlineSent)) {
               this.statusSettingState = 1;
               if (this.statusRequest != 0) {
                  ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.statusRequest, true);
               }

               var3 = new TLRPC.TL_account_updateStatus();
               var3.offline = false;
               this.statusRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$cfQiiK2gKJLj20bAsG92QXAhxfU(this));
            }
         } else if (this.statusSettingState != 2 && !this.offlineSent && Math.abs(System.currentTimeMillis() - ConnectionsManager.getInstance(this.currentAccount).getPauseTime()) >= 2000L) {
            this.statusSettingState = 2;
            if (this.statusRequest != 0) {
               ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.statusRequest, true);
            }

            var3 = new TLRPC.TL_account_updateStatus();
            var3.offline = true;
            this.statusRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$MessagesController$ueghtSvHFDbkkKRlzH3zhB7vPCY(this));
         }

         StringBuilder var15;
         if (this.updatesQueueChannels.size() != 0) {
            for(var4 = 0; var4 < this.updatesQueueChannels.size(); ++var4) {
               var5 = this.updatesQueueChannels.keyAt(var4);
               if (this.updatesStartWaitTimeChannels.valueAt(var4) + 1500L < var1) {
                  if (BuildVars.LOGS_ENABLED) {
                     var15 = new StringBuilder();
                     var15.append("QUEUE CHANNEL ");
                     var15.append(var5);
                     var15.append(" UPDATES WAIT TIMEOUT - CHECK QUEUE");
                     FileLog.d(var15.toString());
                  }

                  this.processChannelsUpdatesQueue(var5, 0);
               }
            }
         }

         for(var4 = 0; var4 < 3; ++var4) {
            if (this.getUpdatesStartTime(var4) != 0L && this.getUpdatesStartTime(var4) + 1500L < var1) {
               if (BuildVars.LOGS_ENABLED) {
                  var15 = new StringBuilder();
                  var15.append(var4);
                  var15.append(" QUEUE UPDATES WAIT TIMEOUT - CHECK QUEUE");
                  FileLog.d(var15.toString());
               }

               this.processUpdatesQueue(var4, 0);
            }
         }
      }

      if (Math.abs(System.currentTimeMillis() - this.lastViewsCheckTime) >= 5000L) {
         this.lastViewsCheckTime = System.currentTimeMillis();
         if (this.channelViewsToSend.size() != 0) {
            for(var4 = 0; var4 < this.channelViewsToSend.size(); ++var4) {
               var5 = this.channelViewsToSend.keyAt(var4);
               TLRPC.TL_messages_getMessagesViews var16 = new TLRPC.TL_messages_getMessagesViews();
               var16.peer = this.getInputPeer(var5);
               var16.id = (ArrayList)this.channelViewsToSend.valueAt(var4);
               boolean var6;
               if (var4 == 0) {
                  var6 = true;
               } else {
                  var6 = false;
               }

               var16.increment = var6;
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var16, new _$$Lambda$MessagesController$zcPVttRNdPhRMvGP9lvTiB9rsK8(this, var5, var16));
            }

            this.channelViewsToSend.clear();
         }

         if (this.pollsToCheckSize > 0) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$EvGoS6VQaul799anwg65luJP3kM(this));
         }
      }

      ArrayList var7;
      ArrayList var17;
      if (!this.onlinePrivacy.isEmpty()) {
         var7 = null;
         var4 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         Iterator var8 = this.onlinePrivacy.entrySet().iterator();

         while(var8.hasNext()) {
            Entry var9 = (Entry)var8.next();
            if ((Integer)var9.getValue() < var4 - 30) {
               var17 = var7;
               if (var7 == null) {
                  var17 = new ArrayList();
               }

               var17.add(var9.getKey());
               var7 = var17;
            }
         }

         if (var7 != null) {
            Iterator var18 = var7.iterator();

            while(var18.hasNext()) {
               Integer var20 = (Integer)var18.next();
               this.onlinePrivacy.remove(var20);
            }

            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$tRF6Ij6nWDh57hakeYO_dBj2yNg(this));
         }
      }

      int var10;
      if (this.shortPollChannels.size() != 0) {
         for(var4 = 0; var4 < this.shortPollChannels.size(); var4 = var5 + 1) {
            var10 = this.shortPollChannels.keyAt(var4);
            var5 = var4;
            if ((long)this.shortPollChannels.valueAt(var4) < System.currentTimeMillis() / 1000L) {
               this.shortPollChannels.delete(var10);
               --var4;
               var5 = var4;
               if (this.needShortPollChannels.indexOfKey(var10) >= 0) {
                  this.getChannelDifference(var10);
                  var5 = var4;
               }
            }
         }
      }

      long var11;
      if (this.shortPollOnlines.size() != 0) {
         var11 = SystemClock.uptimeMillis() / 1000L;

         for(var4 = 0; var4 < this.shortPollOnlines.size(); var4 = var5 + 1) {
            var10 = this.shortPollOnlines.keyAt(var4);
            var5 = var4;
            if ((long)this.shortPollOnlines.valueAt(var4) < var11) {
               if (this.needShortPollChannels.indexOfKey(var10) >= 0) {
                  this.shortPollOnlines.put(var10, (int)(300L + var11));
               } else {
                  this.shortPollOnlines.delete(var10);
                  --var4;
               }

               TLRPC.TL_messages_getOnlines var19 = new TLRPC.TL_messages_getOnlines();
               var19.peer = this.getInputPeer(-var10);
               ConnectionsManager.getInstance(this.currentAccount).sendRequest(var19, new _$$Lambda$MessagesController$sjofkBeH5gGrYjjT8WNKFtTfESE(this, var10));
               var5 = var4;
            }
         }
      }

      if (!this.printingUsers.isEmpty() || this.lastPrintingStringCount != this.printingUsers.size()) {
         var17 = new ArrayList(this.printingUsers.keySet());
         var4 = 0;

         boolean var21;
         for(var21 = false; var4 < var17.size(); ++var4) {
            var11 = (Long)var17.get(var4);
            var7 = (ArrayList)this.printingUsers.get(var11);
            if (var7 != null) {
               byte var13 = 0;
               boolean var23 = var21;

               int var24;
               for(var5 = var13; var5 < var7.size(); var5 = var24 + 1) {
                  MessagesController.PrintingUser var22 = (MessagesController.PrintingUser)var7.get(var5);
                  short var14;
                  if (var22.action instanceof TLRPC.TL_sendMessageGamePlayAction) {
                     var14 = 30000;
                  } else {
                     var14 = 5900;
                  }

                  var24 = var5;
                  if (var22.lastTime + (long)var14 < var1) {
                     var7.remove(var22);
                     var24 = var5 - 1;
                     var23 = true;
                  }
               }

               var21 = var23;
            }

            if (var7 == null || var7.isEmpty()) {
               this.printingUsers.remove(var11);
               var17.remove(var4);
               --var4;
            }
         }

         this.updatePrintingStrings();
         if (var21) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$MessagesController$NCgFAuLO8OXQTI8IHmqXB1TzYHE(this));
         }
      }

      if (Theme.selectedAutoNightType == 1 && Math.abs(var1 - lastThemeCheckTime) >= 60L) {
         AndroidUtilities.runOnUIThread(this.themeCheckRunnable);
         lastThemeCheckTime = var1;
      }

      if (UserConfig.getInstance(this.currentAccount).savedPasswordHash != null && Math.abs(var1 - lastPasswordCheckTime) >= 60L) {
         AndroidUtilities.runOnUIThread(this.passwordCheckRunnable);
         lastPasswordCheckTime = var1;
      }

      LocationController.getInstance(this.currentAccount).update();
      this.checkProxyInfoInternal(false);
      this.checkTosUpdate();
   }

   public void uploadAndApplyUserAvatar(TLRPC.FileLocation var1) {
      if (var1 != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append(FileLoader.getDirectory(4));
         var2.append("/");
         var2.append(var1.volume_id);
         var2.append("_");
         var2.append(var1.local_id);
         var2.append(".jpg");
         this.uploadingAvatar = var2.toString();
         FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingAvatar, false, true, 16777216);
      }
   }

   public static class PrintingUser {
      public TLRPC.SendMessageAction action;
      public long lastTime;
      public int userId;
   }

   private class ReadTask {
      public long dialogId;
      public int maxDate;
      public int maxId;
      public long sendRequestTime;

      private ReadTask() {
      }

      // $FF: synthetic method
      ReadTask(Object var2) {
         this();
      }
   }

   private class UserActionUpdatesPts extends TLRPC.Updates {
      private UserActionUpdatesPts() {
      }

      // $FF: synthetic method
      UserActionUpdatesPts(Object var2) {
         this();
      }
   }

   private class UserActionUpdatesSeq extends TLRPC.Updates {
      private UserActionUpdatesSeq() {
      }

      // $FF: synthetic method
      UserActionUpdatesSeq(Object var2) {
         this();
      }
   }
}
