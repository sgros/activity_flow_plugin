// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.SharedPreferences$Editor;
import java.io.File;
import android.content.pm.PackageInfo;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.SerializedData;
import android.util.Base64;
import java.io.Serializable;
import android.os.SystemClock;
import android.content.Context;
import android.content.SharedPreferences;
import org.telegram.tgnet.TLRPC;

public class UserConfig
{
    private static volatile UserConfig[] Instance;
    public static final int MAX_ACCOUNT_COUNT = 3;
    public static final int i_dialogsLoadOffsetAccess_1 = 5;
    public static final int i_dialogsLoadOffsetAccess_2 = 6;
    public static final int i_dialogsLoadOffsetChannelId = 4;
    public static final int i_dialogsLoadOffsetChatId = 3;
    public static final int i_dialogsLoadOffsetDate = 1;
    public static final int i_dialogsLoadOffsetId = 0;
    public static final int i_dialogsLoadOffsetUserId = 2;
    public static int selectedAccount;
    public long autoDownloadConfigLoadTime;
    public boolean blockedUsersLoaded;
    public int botRatingLoadTime;
    public int clientUserId;
    private boolean configLoaded;
    public boolean contactsReimported;
    public int contactsSavedCount;
    private int currentAccount;
    private TLRPC.User currentUser;
    public boolean draftsLoaded;
    public boolean hasSecureData;
    public boolean hasValidDialogLoadIds;
    public int lastBroadcastId;
    public int lastContactsSyncTime;
    public int lastHintsSyncTime;
    public int lastSendMessageId;
    public long lastUpdateCheckTime;
    public int loginTime;
    public long migrateOffsetAccess;
    public int migrateOffsetChannelId;
    public int migrateOffsetChatId;
    public int migrateOffsetDate;
    public int migrateOffsetId;
    public int migrateOffsetUserId;
    public boolean notificationsSettingsLoaded;
    public boolean notificationsSignUpSettingsLoaded;
    public TLRPC.TL_help_appUpdate pendingAppUpdate;
    public int pendingAppUpdateBuildVersion;
    public long pendingAppUpdateInstallTime;
    public int ratingLoadTime;
    public boolean registeredForPush;
    public volatile byte[] savedPasswordHash;
    public volatile long savedPasswordTime;
    public volatile byte[] savedSaltedPassword;
    public boolean suggestContacts;
    private final Object sync;
    public boolean syncContacts;
    public TLRPC.TL_account_tmpPassword tmpPassword;
    public TLRPC.TL_help_termsOfService unacceptedTermsOfService;
    public boolean unreadDialogsLoaded;
    
    static {
        UserConfig.Instance = new UserConfig[3];
    }
    
    public UserConfig(final int currentAccount) {
        this.sync = new Object();
        this.lastSendMessageId = -210000;
        this.lastBroadcastId = -1;
        this.unreadDialogsLoaded = true;
        this.migrateOffsetId = -1;
        this.migrateOffsetDate = -1;
        this.migrateOffsetUserId = -1;
        this.migrateOffsetChatId = -1;
        this.migrateOffsetChannelId = -1;
        this.migrateOffsetAccess = -1L;
        this.syncContacts = true;
        this.suggestContacts = true;
        this.currentAccount = currentAccount;
    }
    
    public static int getActivatedAccountsCount() {
        int i = 0;
        int n = 0;
        while (i < 3) {
            int n2 = n;
            if (getInstance(i).isClientActivated()) {
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return n;
    }
    
    public static UserConfig getInstance(final int n) {
        final UserConfig userConfig;
        if ((userConfig = UserConfig.Instance[n]) == null) {
            synchronized (UserConfig.class) {
                if (UserConfig.Instance[n] == null) {
                    UserConfig.Instance[n] = new UserConfig(n);
                }
            }
        }
        return userConfig;
    }
    
    private SharedPreferences getPreferences() {
        if (this.currentAccount == 0) {
            return ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
        }
        final Context applicationContext = ApplicationLoader.applicationContext;
        final StringBuilder sb = new StringBuilder();
        sb.append("userconfig");
        sb.append(this.currentAccount);
        return applicationContext.getSharedPreferences(sb.toString(), 0);
    }
    
    public void checkSavedPassword() {
        if ((this.savedSaltedPassword == null && this.savedPasswordHash == null) || Math.abs(SystemClock.elapsedRealtime() - this.savedPasswordTime) < 1800000L) {
            return;
        }
        this.resetSavedPassword();
    }
    
    public void clearConfig() {
        this.getPreferences().edit().clear().commit();
        this.currentUser = null;
        final int n = 0;
        this.clientUserId = 0;
        this.registeredForPush = false;
        this.contactsSavedCount = 0;
        this.lastSendMessageId = -210000;
        this.lastBroadcastId = -1;
        this.blockedUsersLoaded = false;
        this.notificationsSettingsLoaded = false;
        this.notificationsSignUpSettingsLoaded = false;
        this.migrateOffsetId = -1;
        this.migrateOffsetDate = -1;
        this.migrateOffsetUserId = -1;
        this.migrateOffsetChatId = -1;
        this.migrateOffsetChannelId = -1;
        this.migrateOffsetAccess = -1L;
        this.ratingLoadTime = 0;
        this.botRatingLoadTime = 0;
        this.draftsLoaded = true;
        this.contactsReimported = true;
        this.syncContacts = true;
        this.suggestContacts = true;
        this.unreadDialogsLoaded = true;
        this.hasValidDialogLoadIds = true;
        this.unacceptedTermsOfService = null;
        this.pendingAppUpdate = null;
        this.hasSecureData = false;
        this.loginTime = (int)(System.currentTimeMillis() / 1000L);
        this.lastContactsSyncTime = (int)(System.currentTimeMillis() / 1000L) - 82800;
        this.lastHintsSyncTime = (int)(System.currentTimeMillis() / 1000L) - 90000;
        this.resetSavedPassword();
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= 3) {
                break;
            }
            if (getInstance(n2).isClientActivated()) {
                n3 = 1;
                break;
            }
            ++n2;
        }
        if (n3 == 0) {
            SharedConfig.clearConfig();
        }
        this.saveConfig(true);
    }
    
    public String getClientPhone() {
        synchronized (this.sync) {
            String phone;
            if (this.currentUser != null && this.currentUser.phone != null) {
                phone = this.currentUser.phone;
            }
            else {
                phone = "";
            }
            return phone;
        }
    }
    
    public int getClientUserId() {
        synchronized (this.sync) {
            int id;
            if (this.currentUser != null) {
                id = this.currentUser.id;
            }
            else {
                id = 0;
            }
            return id;
        }
    }
    
    public TLRPC.User getCurrentUser() {
        synchronized (this.sync) {
            return this.currentUser;
        }
    }
    
    public int[] getDialogLoadOffsets(final int n) {
        final SharedPreferences preferences = this.getPreferences();
        final StringBuilder sb = new StringBuilder();
        sb.append("2dialogsLoadOffsetId");
        final String s = "";
        Serializable value;
        if (n == 0) {
            value = "";
        }
        else {
            value = n;
        }
        sb.append(value);
        final String string = sb.toString();
        final boolean hasValidDialogLoadIds = this.hasValidDialogLoadIds;
        final int n2 = -1;
        int n3;
        if (hasValidDialogLoadIds) {
            n3 = 0;
        }
        else {
            n3 = -1;
        }
        final int int1 = preferences.getInt(string, n3);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("2dialogsLoadOffsetDate");
        Serializable value2;
        if (n == 0) {
            value2 = "";
        }
        else {
            value2 = n;
        }
        sb2.append(value2);
        final String string2 = sb2.toString();
        int n4;
        if (this.hasValidDialogLoadIds) {
            n4 = 0;
        }
        else {
            n4 = -1;
        }
        final int int2 = preferences.getInt(string2, n4);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("2dialogsLoadOffsetUserId");
        Serializable value3;
        if (n == 0) {
            value3 = "";
        }
        else {
            value3 = n;
        }
        sb3.append(value3);
        final String string3 = sb3.toString();
        int n5;
        if (this.hasValidDialogLoadIds) {
            n5 = 0;
        }
        else {
            n5 = -1;
        }
        final int int3 = preferences.getInt(string3, n5);
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("2dialogsLoadOffsetChatId");
        Serializable value4;
        if (n == 0) {
            value4 = "";
        }
        else {
            value4 = n;
        }
        sb4.append(value4);
        final String string4 = sb4.toString();
        int n6;
        if (this.hasValidDialogLoadIds) {
            n6 = 0;
        }
        else {
            n6 = -1;
        }
        final int int4 = preferences.getInt(string4, n6);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("2dialogsLoadOffsetChannelId");
        Serializable value5;
        if (n == 0) {
            value5 = "";
        }
        else {
            value5 = n;
        }
        sb5.append(value5);
        final String string5 = sb5.toString();
        int n7 = n2;
        if (this.hasValidDialogLoadIds) {
            n7 = 0;
        }
        final int int5 = preferences.getInt(string5, n7);
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("2dialogsLoadOffsetAccess");
        Serializable value6;
        if (n == 0) {
            value6 = s;
        }
        else {
            value6 = n;
        }
        sb6.append(value6);
        final String string6 = sb6.toString();
        long n8;
        if (this.hasValidDialogLoadIds) {
            n8 = 0L;
        }
        else {
            n8 = -1L;
        }
        final long long1 = preferences.getLong(string6, n8);
        return new int[] { int1, int2, int3, int4, int5, (int)long1, (int)(long1 >> 32) };
    }
    
    public int getNewMessageId() {
        synchronized (this.sync) {
            final int lastSendMessageId = this.lastSendMessageId;
            --this.lastSendMessageId;
            return lastSendMessageId;
        }
    }
    
    public int getTotalDialogsCount(final int i) {
        final SharedPreferences preferences = this.getPreferences();
        final StringBuilder sb = new StringBuilder();
        sb.append("2totalDialogsLoadCount");
        Serializable value;
        if (i == 0) {
            value = "";
        }
        else {
            value = i;
        }
        sb.append(value);
        return preferences.getInt(sb.toString(), 0);
    }
    
    public boolean isClientActivated() {
        synchronized (this.sync) {
            return this.currentUser != null;
        }
    }
    
    public boolean isPinnedDialogsLoaded(final int i) {
        final SharedPreferences preferences = this.getPreferences();
        final StringBuilder sb = new StringBuilder();
        sb.append("2pinnedDialogsLoaded");
        sb.append(i);
        return preferences.getBoolean(sb.toString(), false);
    }
    
    public void loadConfig() {
        synchronized (this.sync) {
            if (this.configLoaded) {
                return;
            }
            final SharedPreferences preferences = this.getPreferences();
            if (this.currentAccount == 0) {
                UserConfig.selectedAccount = preferences.getInt("selectedAccount", 0);
            }
            this.registeredForPush = preferences.getBoolean("registeredForPush", false);
            this.lastSendMessageId = preferences.getInt("lastSendMessageId", -210000);
            this.contactsSavedCount = preferences.getInt("contactsSavedCount", 0);
            this.lastBroadcastId = preferences.getInt("lastBroadcastId", -1);
            this.blockedUsersLoaded = preferences.getBoolean("blockedUsersLoaded", false);
            this.lastContactsSyncTime = preferences.getInt("lastContactsSyncTime", (int)(System.currentTimeMillis() / 1000L) - 82800);
            this.lastHintsSyncTime = preferences.getInt("lastHintsSyncTime", (int)(System.currentTimeMillis() / 1000L) - 90000);
            this.draftsLoaded = preferences.getBoolean("draftsLoaded", false);
            this.unreadDialogsLoaded = preferences.getBoolean("unreadDialogsLoaded", false);
            this.contactsReimported = preferences.getBoolean("contactsReimported", false);
            this.ratingLoadTime = preferences.getInt("ratingLoadTime", 0);
            this.botRatingLoadTime = preferences.getInt("botRatingLoadTime", 0);
            this.loginTime = preferences.getInt("loginTime", this.currentAccount);
            this.syncContacts = preferences.getBoolean("syncContacts", true);
            this.suggestContacts = preferences.getBoolean("suggestContacts", true);
            this.hasSecureData = preferences.getBoolean("hasSecureData", false);
            this.notificationsSettingsLoaded = preferences.getBoolean("notificationsSettingsLoaded3", false);
            this.notificationsSignUpSettingsLoaded = preferences.getBoolean("notificationsSignUpSettingsLoaded", false);
            this.autoDownloadConfigLoadTime = preferences.getLong("autoDownloadConfigLoadTime", 0L);
            this.hasValidDialogLoadIds = (preferences.contains("2dialogsLoadOffsetId") || preferences.getBoolean("hasValidDialogLoadIds", false));
            try {
                final String string = preferences.getString("terms", (String)null);
                if (string != null) {
                    final byte[] decode = Base64.decode(string, 0);
                    if (decode != null) {
                        final SerializedData serializedData = new SerializedData(decode);
                        this.unacceptedTermsOfService = TLRPC.TL_help_termsOfService.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                        serializedData.cleanup();
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (this.currentAccount == 0) {
                this.lastUpdateCheckTime = preferences.getLong("appUpdateCheckTime", System.currentTimeMillis());
                try {
                    final String string2 = preferences.getString("appUpdate", (String)null);
                    if (string2 != null) {
                        this.pendingAppUpdateBuildVersion = preferences.getInt("appUpdateBuild", BuildVars.BUILD_VERSION);
                        this.pendingAppUpdateInstallTime = preferences.getLong("appUpdateTime", System.currentTimeMillis());
                        final byte[] decode2 = Base64.decode(string2, 0);
                        if (decode2 != null) {
                            final SerializedData serializedData2 = new SerializedData(decode2);
                            this.pendingAppUpdate = (TLRPC.TL_help_appUpdate)TLRPC.help_AppUpdate.TLdeserialize(serializedData2, serializedData2.readInt32(false), false);
                            serializedData2.cleanup();
                        }
                    }
                    if (this.pendingAppUpdate != null) {
                        long max;
                        try {
                            final PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                            max = Math.max(packageInfo.lastUpdateTime, packageInfo.firstInstallTime);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                            max = 0L;
                        }
                        if (this.pendingAppUpdateBuildVersion != BuildVars.BUILD_VERSION || this.pendingAppUpdateInstallTime < max) {
                            this.pendingAppUpdate = null;
                            AndroidUtilities.runOnUIThread(new _$$Lambda$UserConfig$HoXioNChxQlw_svExyMbii8fWo0(this));
                        }
                    }
                }
                catch (Exception ex3) {
                    FileLog.e(ex3);
                }
            }
            this.migrateOffsetId = preferences.getInt("6migrateOffsetId", 0);
            if (this.migrateOffsetId != -1) {
                this.migrateOffsetDate = preferences.getInt("6migrateOffsetDate", 0);
                this.migrateOffsetUserId = preferences.getInt("6migrateOffsetUserId", 0);
                this.migrateOffsetChatId = preferences.getInt("6migrateOffsetChatId", 0);
                this.migrateOffsetChannelId = preferences.getInt("6migrateOffsetChannelId", 0);
                this.migrateOffsetAccess = preferences.getLong("6migrateOffsetAccess", 0L);
            }
            final String string3 = preferences.getString("tmpPassword", (String)null);
            if (string3 != null) {
                final byte[] decode3 = Base64.decode(string3, 0);
                if (decode3 != null) {
                    final SerializedData serializedData3 = new SerializedData(decode3);
                    this.tmpPassword = TLRPC.TL_account_tmpPassword.TLdeserialize(serializedData3, serializedData3.readInt32(false), false);
                    serializedData3.cleanup();
                }
            }
            final String string4 = preferences.getString("user", (String)null);
            if (string4 != null) {
                final byte[] decode4 = Base64.decode(string4, 0);
                if (decode4 != null) {
                    final SerializedData serializedData4 = new SerializedData(decode4);
                    this.currentUser = TLRPC.User.TLdeserialize(serializedData4, serializedData4.readInt32(false), false);
                    serializedData4.cleanup();
                }
            }
            if (this.currentUser != null) {
                this.clientUserId = this.currentUser.id;
            }
            this.configLoaded = true;
        }
    }
    
    public void resetSavedPassword() {
        this.savedPasswordTime = 0L;
        if (this.savedPasswordHash != null) {
            for (int i = 0; i < this.savedPasswordHash.length; ++i) {
                this.savedPasswordHash[i] = 0;
            }
            this.savedPasswordHash = null;
        }
        if (this.savedSaltedPassword != null) {
            for (int j = 0; j < this.savedSaltedPassword.length; ++j) {
                this.savedSaltedPassword[j] = 0;
            }
            this.savedSaltedPassword = null;
        }
    }
    
    public void saveConfig(final boolean b) {
        this.saveConfig(b, null);
    }
    
    public void saveConfig(final boolean p0, final File p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/telegram/messenger/UserConfig.sync:Ljava/lang/Object;
        //     4: astore_3       
        //     5: aload_3        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: invokespecial   org/telegram/messenger/UserConfig.getPreferences:()Landroid/content/SharedPreferences;
        //    11: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //    16: astore          4
        //    18: aload_0        
        //    19: getfield        org/telegram/messenger/UserConfig.currentAccount:I
        //    22: ifne            39
        //    25: aload           4
        //    27: ldc_w           "selectedAccount"
        //    30: getstatic       org/telegram/messenger/UserConfig.selectedAccount:I
        //    33: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //    38: pop            
        //    39: aload           4
        //    41: ldc_w           "registeredForPush"
        //    44: aload_0        
        //    45: getfield        org/telegram/messenger/UserConfig.registeredForPush:Z
        //    48: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //    53: pop            
        //    54: aload           4
        //    56: ldc_w           "lastSendMessageId"
        //    59: aload_0        
        //    60: getfield        org/telegram/messenger/UserConfig.lastSendMessageId:I
        //    63: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //    68: pop            
        //    69: aload           4
        //    71: ldc_w           "contactsSavedCount"
        //    74: aload_0        
        //    75: getfield        org/telegram/messenger/UserConfig.contactsSavedCount:I
        //    78: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //    83: pop            
        //    84: aload           4
        //    86: ldc_w           "lastBroadcastId"
        //    89: aload_0        
        //    90: getfield        org/telegram/messenger/UserConfig.lastBroadcastId:I
        //    93: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //    98: pop            
        //    99: aload           4
        //   101: ldc_w           "blockedUsersLoaded"
        //   104: aload_0        
        //   105: getfield        org/telegram/messenger/UserConfig.blockedUsersLoaded:Z
        //   108: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   113: pop            
        //   114: aload           4
        //   116: ldc_w           "lastContactsSyncTime"
        //   119: aload_0        
        //   120: getfield        org/telegram/messenger/UserConfig.lastContactsSyncTime:I
        //   123: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   128: pop            
        //   129: aload           4
        //   131: ldc_w           "lastHintsSyncTime"
        //   134: aload_0        
        //   135: getfield        org/telegram/messenger/UserConfig.lastHintsSyncTime:I
        //   138: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   143: pop            
        //   144: aload           4
        //   146: ldc_w           "draftsLoaded"
        //   149: aload_0        
        //   150: getfield        org/telegram/messenger/UserConfig.draftsLoaded:Z
        //   153: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   158: pop            
        //   159: aload           4
        //   161: ldc_w           "unreadDialogsLoaded"
        //   164: aload_0        
        //   165: getfield        org/telegram/messenger/UserConfig.unreadDialogsLoaded:Z
        //   168: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   173: pop            
        //   174: aload           4
        //   176: ldc_w           "ratingLoadTime"
        //   179: aload_0        
        //   180: getfield        org/telegram/messenger/UserConfig.ratingLoadTime:I
        //   183: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   188: pop            
        //   189: aload           4
        //   191: ldc_w           "botRatingLoadTime"
        //   194: aload_0        
        //   195: getfield        org/telegram/messenger/UserConfig.botRatingLoadTime:I
        //   198: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   203: pop            
        //   204: aload           4
        //   206: ldc_w           "contactsReimported"
        //   209: aload_0        
        //   210: getfield        org/telegram/messenger/UserConfig.contactsReimported:Z
        //   213: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   218: pop            
        //   219: aload           4
        //   221: ldc_w           "loginTime"
        //   224: aload_0        
        //   225: getfield        org/telegram/messenger/UserConfig.loginTime:I
        //   228: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   233: pop            
        //   234: aload           4
        //   236: ldc_w           "syncContacts"
        //   239: aload_0        
        //   240: getfield        org/telegram/messenger/UserConfig.syncContacts:Z
        //   243: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   248: pop            
        //   249: aload           4
        //   251: ldc_w           "suggestContacts"
        //   254: aload_0        
        //   255: getfield        org/telegram/messenger/UserConfig.suggestContacts:Z
        //   258: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   263: pop            
        //   264: aload           4
        //   266: ldc_w           "hasSecureData"
        //   269: aload_0        
        //   270: getfield        org/telegram/messenger/UserConfig.hasSecureData:Z
        //   273: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   278: pop            
        //   279: aload           4
        //   281: ldc_w           "notificationsSettingsLoaded3"
        //   284: aload_0        
        //   285: getfield        org/telegram/messenger/UserConfig.notificationsSettingsLoaded:Z
        //   288: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   293: pop            
        //   294: aload           4
        //   296: ldc_w           "notificationsSignUpSettingsLoaded"
        //   299: aload_0        
        //   300: getfield        org/telegram/messenger/UserConfig.notificationsSignUpSettingsLoaded:Z
        //   303: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   308: pop            
        //   309: aload           4
        //   311: ldc_w           "autoDownloadConfigLoadTime"
        //   314: aload_0        
        //   315: getfield        org/telegram/messenger/UserConfig.autoDownloadConfigLoadTime:J
        //   318: invokeinterface android/content/SharedPreferences$Editor.putLong:(Ljava/lang/String;J)Landroid/content/SharedPreferences$Editor;
        //   323: pop            
        //   324: aload           4
        //   326: ldc_w           "hasValidDialogLoadIds"
        //   329: aload_0        
        //   330: getfield        org/telegram/messenger/UserConfig.hasValidDialogLoadIds:Z
        //   333: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   338: pop            
        //   339: aload           4
        //   341: ldc_w           "6migrateOffsetId"
        //   344: aload_0        
        //   345: getfield        org/telegram/messenger/UserConfig.migrateOffsetId:I
        //   348: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   353: pop            
        //   354: aload_0        
        //   355: getfield        org/telegram/messenger/UserConfig.migrateOffsetId:I
        //   358: iconst_m1      
        //   359: if_icmpeq       437
        //   362: aload           4
        //   364: ldc_w           "6migrateOffsetDate"
        //   367: aload_0        
        //   368: getfield        org/telegram/messenger/UserConfig.migrateOffsetDate:I
        //   371: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   376: pop            
        //   377: aload           4
        //   379: ldc_w           "6migrateOffsetUserId"
        //   382: aload_0        
        //   383: getfield        org/telegram/messenger/UserConfig.migrateOffsetUserId:I
        //   386: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   391: pop            
        //   392: aload           4
        //   394: ldc_w           "6migrateOffsetChatId"
        //   397: aload_0        
        //   398: getfield        org/telegram/messenger/UserConfig.migrateOffsetChatId:I
        //   401: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   406: pop            
        //   407: aload           4
        //   409: ldc_w           "6migrateOffsetChannelId"
        //   412: aload_0        
        //   413: getfield        org/telegram/messenger/UserConfig.migrateOffsetChannelId:I
        //   416: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   421: pop            
        //   422: aload           4
        //   424: ldc_w           "6migrateOffsetAccess"
        //   427: aload_0        
        //   428: getfield        org/telegram/messenger/UserConfig.migrateOffsetAccess:J
        //   431: invokeinterface android/content/SharedPreferences$Editor.putLong:(Ljava/lang/String;J)Landroid/content/SharedPreferences$Editor;
        //   436: pop            
        //   437: aload_0        
        //   438: getfield        org/telegram/messenger/UserConfig.unacceptedTermsOfService:Lorg/telegram/tgnet/TLRPC$TL_help_termsOfService;
        //   441: astore          5
        //   443: aload           5
        //   445: ifnull          502
        //   448: new             Lorg/telegram/tgnet/SerializedData;
        //   451: astore          5
        //   453: aload           5
        //   455: aload_0        
        //   456: getfield        org/telegram/messenger/UserConfig.unacceptedTermsOfService:Lorg/telegram/tgnet/TLRPC$TL_help_termsOfService;
        //   459: invokevirtual   org/telegram/tgnet/TLObject.getObjectSize:()I
        //   462: invokespecial   org/telegram/tgnet/SerializedData.<init>:(I)V
        //   465: aload_0        
        //   466: getfield        org/telegram/messenger/UserConfig.unacceptedTermsOfService:Lorg/telegram/tgnet/TLRPC$TL_help_termsOfService;
        //   469: aload           5
        //   471: invokevirtual   org/telegram/tgnet/TLRPC$TL_help_termsOfService.serializeToStream:(Lorg/telegram/tgnet/AbstractSerializedData;)V
        //   474: aload           4
        //   476: ldc_w           "terms"
        //   479: aload           5
        //   481: invokevirtual   org/telegram/tgnet/SerializedData.toByteArray:()[B
        //   484: iconst_0       
        //   485: invokestatic    android/util/Base64.encodeToString:([BI)Ljava/lang/String;
        //   488: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   493: pop            
        //   494: aload           5
        //   496: invokevirtual   org/telegram/tgnet/SerializedData.cleanup:()V
        //   499: goto            513
        //   502: aload           4
        //   504: ldc_w           "terms"
        //   507: invokeinterface android/content/SharedPreferences$Editor.remove:(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   512: pop            
        //   513: aload_0        
        //   514: getfield        org/telegram/messenger/UserConfig.currentAccount:I
        //   517: ifne            641
        //   520: aload_0        
        //   521: getfield        org/telegram/messenger/UserConfig.pendingAppUpdate:Lorg/telegram/tgnet/TLRPC$TL_help_appUpdate;
        //   524: astore          5
        //   526: aload           5
        //   528: ifnull          630
        //   531: new             Lorg/telegram/tgnet/SerializedData;
        //   534: astore          5
        //   536: aload           5
        //   538: aload_0        
        //   539: getfield        org/telegram/messenger/UserConfig.pendingAppUpdate:Lorg/telegram/tgnet/TLRPC$TL_help_appUpdate;
        //   542: invokevirtual   org/telegram/tgnet/TLObject.getObjectSize:()I
        //   545: invokespecial   org/telegram/tgnet/SerializedData.<init>:(I)V
        //   548: aload_0        
        //   549: getfield        org/telegram/messenger/UserConfig.pendingAppUpdate:Lorg/telegram/tgnet/TLRPC$TL_help_appUpdate;
        //   552: aload           5
        //   554: invokevirtual   org/telegram/tgnet/TLRPC$TL_help_appUpdate.serializeToStream:(Lorg/telegram/tgnet/AbstractSerializedData;)V
        //   557: aload           4
        //   559: ldc_w           "appUpdate"
        //   562: aload           5
        //   564: invokevirtual   org/telegram/tgnet/SerializedData.toByteArray:()[B
        //   567: iconst_0       
        //   568: invokestatic    android/util/Base64.encodeToString:([BI)Ljava/lang/String;
        //   571: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   576: pop            
        //   577: aload           4
        //   579: ldc_w           "appUpdateBuild"
        //   582: aload_0        
        //   583: getfield        org/telegram/messenger/UserConfig.pendingAppUpdateBuildVersion:I
        //   586: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //   591: pop            
        //   592: aload           4
        //   594: ldc_w           "appUpdateTime"
        //   597: aload_0        
        //   598: getfield        org/telegram/messenger/UserConfig.pendingAppUpdateInstallTime:J
        //   601: invokeinterface android/content/SharedPreferences$Editor.putLong:(Ljava/lang/String;J)Landroid/content/SharedPreferences$Editor;
        //   606: pop            
        //   607: aload           4
        //   609: ldc_w           "appUpdateCheckTime"
        //   612: aload_0        
        //   613: getfield        org/telegram/messenger/UserConfig.lastUpdateCheckTime:J
        //   616: invokeinterface android/content/SharedPreferences$Editor.putLong:(Ljava/lang/String;J)Landroid/content/SharedPreferences$Editor;
        //   621: pop            
        //   622: aload           5
        //   624: invokevirtual   org/telegram/tgnet/SerializedData.cleanup:()V
        //   627: goto            641
        //   630: aload           4
        //   632: ldc_w           "appUpdate"
        //   635: invokeinterface android/content/SharedPreferences$Editor.remove:(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   640: pop            
        //   641: invokestatic    org/telegram/messenger/SharedConfig.saveConfig:()V
        //   644: aload_0        
        //   645: getfield        org/telegram/messenger/UserConfig.tmpPassword:Lorg/telegram/tgnet/TLRPC$TL_account_tmpPassword;
        //   648: ifnull          698
        //   651: new             Lorg/telegram/tgnet/SerializedData;
        //   654: astore          5
        //   656: aload           5
        //   658: invokespecial   org/telegram/tgnet/SerializedData.<init>:()V
        //   661: aload_0        
        //   662: getfield        org/telegram/messenger/UserConfig.tmpPassword:Lorg/telegram/tgnet/TLRPC$TL_account_tmpPassword;
        //   665: aload           5
        //   667: invokevirtual   org/telegram/tgnet/TLRPC$TL_account_tmpPassword.serializeToStream:(Lorg/telegram/tgnet/AbstractSerializedData;)V
        //   670: aload           4
        //   672: ldc_w           "tmpPassword"
        //   675: aload           5
        //   677: invokevirtual   org/telegram/tgnet/SerializedData.toByteArray:()[B
        //   680: iconst_0       
        //   681: invokestatic    android/util/Base64.encodeToString:([BI)Ljava/lang/String;
        //   684: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   689: pop            
        //   690: aload           5
        //   692: invokevirtual   org/telegram/tgnet/SerializedData.cleanup:()V
        //   695: goto            709
        //   698: aload           4
        //   700: ldc_w           "tmpPassword"
        //   703: invokeinterface android/content/SharedPreferences$Editor.remove:(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   708: pop            
        //   709: aload_0        
        //   710: getfield        org/telegram/messenger/UserConfig.currentUser:Lorg/telegram/tgnet/TLRPC$User;
        //   713: ifnull          767
        //   716: iload_1        
        //   717: ifeq            778
        //   720: new             Lorg/telegram/tgnet/SerializedData;
        //   723: astore          5
        //   725: aload           5
        //   727: invokespecial   org/telegram/tgnet/SerializedData.<init>:()V
        //   730: aload_0        
        //   731: getfield        org/telegram/messenger/UserConfig.currentUser:Lorg/telegram/tgnet/TLRPC$User;
        //   734: aload           5
        //   736: invokevirtual   org/telegram/tgnet/TLObject.serializeToStream:(Lorg/telegram/tgnet/AbstractSerializedData;)V
        //   739: aload           4
        //   741: ldc_w           "user"
        //   744: aload           5
        //   746: invokevirtual   org/telegram/tgnet/SerializedData.toByteArray:()[B
        //   749: iconst_0       
        //   750: invokestatic    android/util/Base64.encodeToString:([BI)Ljava/lang/String;
        //   753: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   758: pop            
        //   759: aload           5
        //   761: invokevirtual   org/telegram/tgnet/SerializedData.cleanup:()V
        //   764: goto            778
        //   767: aload           4
        //   769: ldc_w           "user"
        //   772: invokeinterface android/content/SharedPreferences$Editor.remove:(Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //   777: pop            
        //   778: aload           4
        //   780: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //   785: pop            
        //   786: aload_2        
        //   787: ifnull          807
        //   790: aload_2        
        //   791: invokevirtual   java/io/File.delete:()Z
        //   794: pop            
        //   795: goto            807
        //   798: astore_2       
        //   799: goto            810
        //   802: astore_2       
        //   803: aload_2        
        //   804: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   807: aload_3        
        //   808: monitorexit    
        //   809: return         
        //   810: aload_3        
        //   811: monitorexit    
        //   812: aload_2        
        //   813: athrow         
        //   814: astore          5
        //   816: goto            513
        //   819: astore          5
        //   821: goto            641
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  7      39     802    807    Ljava/lang/Exception;
        //  7      39     798    814    Any
        //  39     437    802    807    Ljava/lang/Exception;
        //  39     437    798    814    Any
        //  437    443    802    807    Ljava/lang/Exception;
        //  437    443    798    814    Any
        //  448    499    814    819    Ljava/lang/Exception;
        //  448    499    798    814    Any
        //  502    513    802    807    Ljava/lang/Exception;
        //  502    513    798    814    Any
        //  513    526    802    807    Ljava/lang/Exception;
        //  513    526    798    814    Any
        //  531    627    819    824    Ljava/lang/Exception;
        //  531    627    798    814    Any
        //  630    641    802    807    Ljava/lang/Exception;
        //  630    641    798    814    Any
        //  641    695    802    807    Ljava/lang/Exception;
        //  641    695    798    814    Any
        //  698    709    802    807    Ljava/lang/Exception;
        //  698    709    798    814    Any
        //  709    716    802    807    Ljava/lang/Exception;
        //  709    716    798    814    Any
        //  720    764    802    807    Ljava/lang/Exception;
        //  720    764    798    814    Any
        //  767    778    802    807    Ljava/lang/Exception;
        //  767    778    798    814    Any
        //  778    786    802    807    Ljava/lang/Exception;
        //  778    786    798    814    Any
        //  790    795    802    807    Ljava/lang/Exception;
        //  790    795    798    814    Any
        //  803    807    798    814    Any
        //  807    809    798    814    Any
        //  810    812    798    814    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 340 out-of-bounds for length 340
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public void savePassword(final byte[] savedPasswordHash, final byte[] savedSaltedPassword) {
        this.savedPasswordTime = SystemClock.elapsedRealtime();
        this.savedPasswordHash = savedPasswordHash;
        this.savedSaltedPassword = savedSaltedPassword;
    }
    
    public void setCurrentUser(final TLRPC.User currentUser) {
        synchronized (this.sync) {
            this.currentUser = currentUser;
            this.clientUserId = currentUser.id;
        }
    }
    
    public void setDialogsLoadOffset(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final long n7) {
        final SharedPreferences$Editor edit = this.getPreferences().edit();
        final StringBuilder sb = new StringBuilder();
        sb.append("2dialogsLoadOffsetId");
        final String s = "";
        Serializable value;
        if (n == 0) {
            value = "";
        }
        else {
            value = n;
        }
        sb.append(value);
        edit.putInt(sb.toString(), n2);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("2dialogsLoadOffsetDate");
        Serializable value2;
        if (n == 0) {
            value2 = "";
        }
        else {
            value2 = n;
        }
        sb2.append(value2);
        edit.putInt(sb2.toString(), n3);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("2dialogsLoadOffsetUserId");
        Serializable value3;
        if (n == 0) {
            value3 = "";
        }
        else {
            value3 = n;
        }
        sb3.append(value3);
        edit.putInt(sb3.toString(), n4);
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("2dialogsLoadOffsetChatId");
        Serializable value4;
        if (n == 0) {
            value4 = "";
        }
        else {
            value4 = n;
        }
        sb4.append(value4);
        edit.putInt(sb4.toString(), n5);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("2dialogsLoadOffsetChannelId");
        Serializable value5;
        if (n == 0) {
            value5 = "";
        }
        else {
            value5 = n;
        }
        sb5.append(value5);
        edit.putInt(sb5.toString(), n6);
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("2dialogsLoadOffsetAccess");
        Serializable value6;
        if (n == 0) {
            value6 = s;
        }
        else {
            value6 = n;
        }
        sb6.append(value6);
        edit.putLong(sb6.toString(), n7);
        edit.putBoolean("hasValidDialogLoadIds", true);
        edit.commit();
    }
    
    public void setPinnedDialogsLoaded(final int i, final boolean b) {
        final SharedPreferences$Editor edit = this.getPreferences().edit();
        final StringBuilder sb = new StringBuilder();
        sb.append("2pinnedDialogsLoaded");
        sb.append(i);
        edit.putBoolean(sb.toString(), b).commit();
    }
    
    public void setTotalDialogsCount(final int i, final int n) {
        final SharedPreferences$Editor edit = this.getPreferences().edit();
        final StringBuilder sb = new StringBuilder();
        sb.append("2totalDialogsLoadCount");
        Serializable value;
        if (i == 0) {
            value = "";
        }
        else {
            value = i;
        }
        sb.append(value);
        edit.putInt(sb.toString(), n).commit();
    }
}
