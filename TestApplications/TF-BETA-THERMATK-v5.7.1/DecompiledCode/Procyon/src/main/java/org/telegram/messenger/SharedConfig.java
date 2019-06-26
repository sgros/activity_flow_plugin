// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.SerializedData;
import android.util.Base64;
import android.text.TextUtils;
import android.os.SystemClock;
import java.util.Iterator;
import org.json.JSONObject;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.telegram.tgnet.ConnectionsManager;
import java.io.File;
import android.os.Environment;
import java.util.ArrayList;
import java.util.HashMap;

public class SharedConfig
{
    public static boolean allowBigEmoji = false;
    public static boolean allowScreenCapture = false;
    public static boolean appLocked = false;
    public static boolean archiveHidden = false;
    public static int autoLockIn = 0;
    public static boolean autoplayGifs = false;
    public static boolean autoplayVideo = false;
    public static int badPasscodeTries = 0;
    private static boolean configLoaded = false;
    public static ProxyInfo currentProxy;
    public static boolean customTabs = false;
    public static boolean directShare = false;
    public static long directShareHash = 0L;
    public static boolean drawDialogIcons = false;
    public static int fontSize = 0;
    public static boolean groupPhotosEnabled = false;
    public static boolean hasCameraCache = false;
    public static boolean inappCamera = false;
    public static boolean isWaitingForPasscodeEnter = false;
    public static long lastAppPauseTime = 0L;
    private static int lastLocalId = 0;
    public static int lastPauseTime = 0;
    public static String lastUpdateVersion;
    public static long lastUptimeMillis = 0L;
    private static final Object localIdSync;
    public static int mapPreviewType = 0;
    public static boolean noSoundHintShowed = false;
    public static String passcodeHash = "";
    public static long passcodeRetryInMs = 0L;
    public static byte[] passcodeSalt;
    public static int passcodeType = 0;
    public static int passportConfigHash = 0;
    private static String passportConfigJson;
    private static HashMap<String, String> passportConfigMap;
    public static boolean playOrderReversed = false;
    public static ArrayList<ProxyInfo> proxyList;
    private static boolean proxyListLoaded = false;
    public static byte[] pushAuthKey;
    public static byte[] pushAuthKeyId;
    public static String pushString = "";
    public static boolean raiseToSpeak;
    public static int repeatMode;
    public static boolean roundCamera16to9;
    public static boolean saveIncomingPhotos;
    public static boolean saveStreamMedia;
    public static boolean saveToGallery;
    public static boolean showAnimatedStickers;
    public static boolean showNotificationsForAllAccounts;
    public static boolean shuffleMusic;
    public static boolean sortContactsByName;
    public static boolean streamAllVideo;
    public static boolean streamMedia;
    public static boolean streamMkv;
    public static int suggestStickers;
    private static final Object sync;
    public static boolean useFingerprint;
    public static boolean useSystemEmoji;
    public static boolean useThreeLinesLayout;
    
    static {
        SharedConfig.passcodeSalt = new byte[0];
        SharedConfig.autoLockIn = 3600;
        SharedConfig.useFingerprint = true;
        SharedConfig.lastLocalId = -210000;
        SharedConfig.passportConfigJson = "";
        sync = new Object();
        localIdSync = new Object();
        SharedConfig.mapPreviewType = 2;
        SharedConfig.autoplayGifs = true;
        SharedConfig.autoplayVideo = true;
        SharedConfig.raiseToSpeak = true;
        SharedConfig.customTabs = true;
        SharedConfig.directShare = true;
        SharedConfig.inappCamera = true;
        SharedConfig.roundCamera16to9 = true;
        SharedConfig.groupPhotosEnabled = true;
        SharedConfig.noSoundHintShowed = false;
        SharedConfig.streamMedia = true;
        SharedConfig.streamAllVideo = false;
        SharedConfig.streamMkv = false;
        SharedConfig.saveStreamMedia = true;
        SharedConfig.showAnimatedStickers = BuildVars.DEBUG_VERSION;
        SharedConfig.showNotificationsForAllAccounts = true;
        SharedConfig.fontSize = AndroidUtilities.dp(16.0f);
        loadConfig();
        SharedConfig.proxyList = new ArrayList<ProxyInfo>();
    }
    
    public static ProxyInfo addProxy(final ProxyInfo e) {
        loadProxyList();
        for (int size = SharedConfig.proxyList.size(), i = 0; i < size; ++i) {
            final ProxyInfo proxyInfo = SharedConfig.proxyList.get(i);
            if (e.address.equals(proxyInfo.address) && e.port == proxyInfo.port && e.username.equals(proxyInfo.username) && e.password.equals(proxyInfo.password) && e.secret.equals(proxyInfo.secret)) {
                return proxyInfo;
            }
        }
        SharedConfig.proxyList.add(e);
        saveProxyList();
        return e;
    }
    
    public static boolean checkPasscode(String bytesToHex) {
        if (SharedConfig.passcodeSalt.length == 0) {
            final boolean equals = Utilities.MD5(bytesToHex).equals(SharedConfig.passcodeHash);
            if (equals) {
                try {
                    SharedConfig.passcodeSalt = new byte[16];
                    Utilities.random.nextBytes(SharedConfig.passcodeSalt);
                    final byte[] bytes = bytesToHex.getBytes("UTF-8");
                    final byte[] array = new byte[bytes.length + 32];
                    System.arraycopy(SharedConfig.passcodeSalt, 0, array, 0, 16);
                    System.arraycopy(bytes, 0, array, 16, bytes.length);
                    System.arraycopy(SharedConfig.passcodeSalt, 0, array, bytes.length + 16, 16);
                    SharedConfig.passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(array, 0, array.length));
                    saveConfig();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            return equals;
        }
        try {
            final byte[] bytes2 = bytesToHex.getBytes("UTF-8");
            final byte[] array2 = new byte[bytes2.length + 32];
            System.arraycopy(SharedConfig.passcodeSalt, 0, array2, 0, 16);
            System.arraycopy(bytes2, 0, array2, 16, bytes2.length);
            System.arraycopy(SharedConfig.passcodeSalt, 0, array2, bytes2.length + 16, 16);
            bytesToHex = Utilities.bytesToHex(Utilities.computeSHA256(array2, 0, array2.length));
            return SharedConfig.passcodeHash.equals(bytesToHex);
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
            return false;
        }
    }
    
    public static void checkSaveToGalleryFiles() {
        try {
            final File file = new File(Environment.getExternalStorageDirectory(), "Telegram");
            final File file2 = new File(file, "Telegram Images");
            file2.mkdir();
            final File file3 = new File(file, "Telegram Video");
            file3.mkdir();
            if (SharedConfig.saveToGallery) {
                if (file2.isDirectory()) {
                    new File(file2, ".nomedia").delete();
                }
                if (file3.isDirectory()) {
                    new File(file3, ".nomedia").delete();
                }
            }
            else {
                if (file2.isDirectory()) {
                    new File(file2, ".nomedia").createNewFile();
                }
                if (file3.isDirectory()) {
                    new File(file3, ".nomedia").createNewFile();
                }
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public static void clearConfig() {
        SharedConfig.saveIncomingPhotos = false;
        SharedConfig.appLocked = false;
        SharedConfig.passcodeType = 0;
        SharedConfig.passcodeRetryInMs = 0L;
        SharedConfig.lastUptimeMillis = 0L;
        SharedConfig.badPasscodeTries = 0;
        SharedConfig.passcodeHash = "";
        SharedConfig.passcodeSalt = new byte[0];
        SharedConfig.autoLockIn = 3600;
        SharedConfig.lastPauseTime = 0;
        SharedConfig.useFingerprint = true;
        SharedConfig.isWaitingForPasscodeEnter = false;
        SharedConfig.allowScreenCapture = false;
        SharedConfig.lastUpdateVersion = BuildVars.BUILD_VERSION_STRING;
        saveConfig();
    }
    
    public static void deleteProxy(final ProxyInfo o) {
        if (SharedConfig.currentProxy == o) {
            SharedConfig.currentProxy = null;
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            final boolean boolean1 = globalMainSettings.getBoolean("proxy_enabled", false);
            final SharedPreferences$Editor edit = globalMainSettings.edit();
            edit.putString("proxy_ip", "");
            edit.putString("proxy_pass", "");
            edit.putString("proxy_user", "");
            edit.putString("proxy_secret", "");
            edit.putInt("proxy_port", 1080);
            edit.putBoolean("proxy_enabled", false);
            edit.putBoolean("proxy_enabled_calls", false);
            edit.commit();
            if (boolean1) {
                ConnectionsManager.setProxySettings(false, "", 0, "", "", "");
            }
        }
        SharedConfig.proxyList.remove(o);
        saveProxyList();
    }
    
    public static HashMap<String, String> getCountryLangs() {
        if (SharedConfig.passportConfigMap == null) {
            SharedConfig.passportConfigMap = new HashMap<String, String>();
            try {
                final JSONObject jsonObject = new JSONObject(SharedConfig.passportConfigJson);
                final Iterator keys = jsonObject.keys();
                while (keys.hasNext()) {
                    final String s = keys.next();
                    SharedConfig.passportConfigMap.put(s.toUpperCase(), jsonObject.getString(s).toUpperCase());
                }
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
        return SharedConfig.passportConfigMap;
    }
    
    public static int getLastLocalId() {
        synchronized (SharedConfig.localIdSync) {
            final int lastLocalId = SharedConfig.lastLocalId;
            SharedConfig.lastLocalId = lastLocalId - 1;
            return lastLocalId;
        }
    }
    
    public static void increaseBadPasscodeTries() {
        ++SharedConfig.badPasscodeTries;
        final int badPasscodeTries = SharedConfig.badPasscodeTries;
        if (badPasscodeTries >= 3) {
            if (badPasscodeTries != 3) {
                if (badPasscodeTries != 4) {
                    if (badPasscodeTries != 5) {
                        if (badPasscodeTries != 6) {
                            if (badPasscodeTries != 7) {
                                SharedConfig.passcodeRetryInMs = 30000L;
                            }
                            else {
                                SharedConfig.passcodeRetryInMs = 25000L;
                            }
                        }
                        else {
                            SharedConfig.passcodeRetryInMs = 20000L;
                        }
                    }
                    else {
                        SharedConfig.passcodeRetryInMs = 15000L;
                    }
                }
                else {
                    SharedConfig.passcodeRetryInMs = 10000L;
                }
            }
            else {
                SharedConfig.passcodeRetryInMs = 5000L;
            }
            SharedConfig.lastUptimeMillis = SystemClock.elapsedRealtime();
        }
        saveConfig();
    }
    
    public static boolean isPassportConfigLoaded() {
        return SharedConfig.passportConfigMap != null;
    }
    
    public static boolean isSecretMapPreviewSet() {
        return MessagesController.getGlobalMainSettings().contains("mapPreviewType");
    }
    
    public static void loadConfig() {
        synchronized (SharedConfig.sync) {
            if (SharedConfig.configLoaded) {
                return;
            }
            final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
            SharedConfig.saveIncomingPhotos = sharedPreferences.getBoolean("saveIncomingPhotos", false);
            SharedConfig.passcodeHash = sharedPreferences.getString("passcodeHash1", "");
            SharedConfig.appLocked = sharedPreferences.getBoolean("appLocked", false);
            SharedConfig.passcodeType = sharedPreferences.getInt("passcodeType", 0);
            SharedConfig.passcodeRetryInMs = sharedPreferences.getLong("passcodeRetryInMs", 0L);
            SharedConfig.lastUptimeMillis = sharedPreferences.getLong("lastUptimeMillis", 0L);
            SharedConfig.badPasscodeTries = sharedPreferences.getInt("badPasscodeTries", 0);
            SharedConfig.autoLockIn = sharedPreferences.getInt("autoLockIn", 3600);
            SharedConfig.lastPauseTime = sharedPreferences.getInt("lastPauseTime", 0);
            SharedConfig.lastAppPauseTime = sharedPreferences.getLong("lastAppPauseTime", 0L);
            SharedConfig.useFingerprint = sharedPreferences.getBoolean("useFingerprint", true);
            SharedConfig.lastUpdateVersion = sharedPreferences.getString("lastUpdateVersion2", "3.5");
            SharedConfig.allowScreenCapture = sharedPreferences.getBoolean("allowScreenCapture", false);
            SharedConfig.lastLocalId = sharedPreferences.getInt("lastLocalId", -210000);
            SharedConfig.pushString = sharedPreferences.getString("pushString2", "");
            SharedConfig.passportConfigJson = sharedPreferences.getString("passportConfigJson", "");
            SharedConfig.passportConfigHash = sharedPreferences.getInt("passportConfigHash", 0);
            final String string = sharedPreferences.getString("pushAuthKey", (String)null);
            if (!TextUtils.isEmpty((CharSequence)string)) {
                SharedConfig.pushAuthKey = Base64.decode(string, 0);
            }
            if (SharedConfig.passcodeHash.length() > 0 && SharedConfig.lastPauseTime == 0) {
                SharedConfig.lastPauseTime = (int)(System.currentTimeMillis() / 1000L - 600L);
            }
            final String string2 = sharedPreferences.getString("passcodeSalt", "");
            if (string2.length() > 0) {
                SharedConfig.passcodeSalt = Base64.decode(string2, 0);
            }
            else {
                SharedConfig.passcodeSalt = new byte[0];
            }
            final SharedPreferences sharedPreferences2 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            SharedConfig.saveToGallery = sharedPreferences2.getBoolean("save_gallery", false);
            SharedConfig.autoplayGifs = sharedPreferences2.getBoolean("autoplay_gif", true);
            SharedConfig.autoplayVideo = sharedPreferences2.getBoolean("autoplay_video", true);
            SharedConfig.mapPreviewType = sharedPreferences2.getInt("mapPreviewType", 2);
            SharedConfig.raiseToSpeak = sharedPreferences2.getBoolean("raise_to_speak", true);
            SharedConfig.customTabs = sharedPreferences2.getBoolean("custom_tabs", true);
            SharedConfig.directShare = sharedPreferences2.getBoolean("direct_share", true);
            SharedConfig.shuffleMusic = sharedPreferences2.getBoolean("shuffleMusic", false);
            SharedConfig.playOrderReversed = sharedPreferences2.getBoolean("playOrderReversed", false);
            SharedConfig.inappCamera = sharedPreferences2.getBoolean("inappCamera", true);
            SharedConfig.hasCameraCache = sharedPreferences2.contains("cameraCache");
            SharedConfig.roundCamera16to9 = true;
            SharedConfig.groupPhotosEnabled = sharedPreferences2.getBoolean("groupPhotosEnabled", true);
            SharedConfig.repeatMode = sharedPreferences2.getInt("repeatMode", 0);
            int n;
            if (AndroidUtilities.isTablet()) {
                n = 18;
            }
            else {
                n = 16;
            }
            SharedConfig.fontSize = sharedPreferences2.getInt("fons_size", n);
            SharedConfig.allowBigEmoji = sharedPreferences2.getBoolean("allowBigEmoji", true);
            SharedConfig.useSystemEmoji = sharedPreferences2.getBoolean("useSystemEmoji", false);
            SharedConfig.streamMedia = sharedPreferences2.getBoolean("streamMedia", true);
            SharedConfig.saveStreamMedia = sharedPreferences2.getBoolean("saveStreamMedia", true);
            SharedConfig.streamAllVideo = sharedPreferences2.getBoolean("streamAllVideo", BuildVars.DEBUG_VERSION);
            SharedConfig.streamMkv = sharedPreferences2.getBoolean("streamMkv", false);
            SharedConfig.suggestStickers = sharedPreferences2.getInt("suggestStickers", 0);
            SharedConfig.sortContactsByName = sharedPreferences2.getBoolean("sortContactsByName", false);
            SharedConfig.noSoundHintShowed = sharedPreferences2.getBoolean("noSoundHintShowed", false);
            SharedConfig.directShareHash = sharedPreferences2.getLong("directShareHash", 0L);
            SharedConfig.useThreeLinesLayout = sharedPreferences2.getBoolean("useThreeLinesLayout", false);
            SharedConfig.archiveHidden = sharedPreferences2.getBoolean("archiveHidden", false);
            SharedConfig.showNotificationsForAllAccounts = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).getBoolean("AllAccounts", true);
            SharedConfig.configLoaded = true;
        }
    }
    
    public static void loadProxyList() {
        if (SharedConfig.proxyListLoaded) {
            return;
        }
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        final String string = sharedPreferences.getString("proxy_ip", "");
        final String string2 = sharedPreferences.getString("proxy_user", "");
        final String string3 = sharedPreferences.getString("proxy_pass", "");
        final String string4 = sharedPreferences.getString("proxy_secret", "");
        final int int1 = sharedPreferences.getInt("proxy_port", 1080);
        SharedConfig.proxyListLoaded = true;
        SharedConfig.proxyList.clear();
        SharedConfig.currentProxy = null;
        final String string5 = sharedPreferences.getString("proxy_list", (String)null);
        if (!TextUtils.isEmpty((CharSequence)string5)) {
            final SerializedData serializedData = new SerializedData(Base64.decode(string5, 0));
            for (int int2 = serializedData.readInt32(false), i = 0; i < int2; ++i) {
                final ProxyInfo proxyInfo = new ProxyInfo(serializedData.readString(false), serializedData.readInt32(false), serializedData.readString(false), serializedData.readString(false), serializedData.readString(false));
                SharedConfig.proxyList.add(proxyInfo);
                if (SharedConfig.currentProxy == null && !TextUtils.isEmpty((CharSequence)string) && string.equals(proxyInfo.address) && int1 == proxyInfo.port && string2.equals(proxyInfo.username) && string3.equals(proxyInfo.password)) {
                    SharedConfig.currentProxy = proxyInfo;
                }
            }
            serializedData.cleanup();
        }
        if (SharedConfig.currentProxy == null && !TextUtils.isEmpty((CharSequence)string)) {
            SharedConfig.proxyList.add(0, SharedConfig.currentProxy = new ProxyInfo(string, int1, string2, string3, string4));
        }
    }
    
    public static void saveConfig() {
        final Object sync = SharedConfig.sync;
        // monitorenter(sync)
        try {
            try {
                final SharedPreferences$Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0).edit();
                edit.putBoolean("saveIncomingPhotos", SharedConfig.saveIncomingPhotos);
                edit.putString("passcodeHash1", SharedConfig.passcodeHash);
                String encodeToString;
                if (SharedConfig.passcodeSalt.length > 0) {
                    encodeToString = Base64.encodeToString(SharedConfig.passcodeSalt, 0);
                }
                else {
                    encodeToString = "";
                }
                edit.putString("passcodeSalt", encodeToString);
                edit.putBoolean("appLocked", SharedConfig.appLocked);
                edit.putInt("passcodeType", SharedConfig.passcodeType);
                edit.putLong("passcodeRetryInMs", SharedConfig.passcodeRetryInMs);
                edit.putLong("lastUptimeMillis", SharedConfig.lastUptimeMillis);
                edit.putInt("badPasscodeTries", SharedConfig.badPasscodeTries);
                edit.putInt("autoLockIn", SharedConfig.autoLockIn);
                edit.putInt("lastPauseTime", SharedConfig.lastPauseTime);
                edit.putLong("lastAppPauseTime", SharedConfig.lastAppPauseTime);
                edit.putString("lastUpdateVersion2", SharedConfig.lastUpdateVersion);
                edit.putBoolean("useFingerprint", SharedConfig.useFingerprint);
                edit.putBoolean("allowScreenCapture", SharedConfig.allowScreenCapture);
                edit.putString("pushString2", SharedConfig.pushString);
                String encodeToString2;
                if (SharedConfig.pushAuthKey != null) {
                    encodeToString2 = Base64.encodeToString(SharedConfig.pushAuthKey, 0);
                }
                else {
                    encodeToString2 = "";
                }
                edit.putString("pushAuthKey", encodeToString2);
                edit.putInt("lastLocalId", SharedConfig.lastLocalId);
                edit.putString("passportConfigJson", SharedConfig.passportConfigJson);
                edit.putInt("passportConfigHash", SharedConfig.passportConfigHash);
                edit.putBoolean("sortContactsByName", SharedConfig.sortContactsByName);
                edit.commit();
            }
            finally {
            }
            // monitorexit(sync)
            // monitorexit(sync)
        }
        catch (Exception ex) {}
    }
    
    public static void saveProxyList() {
        final SerializedData serializedData = new SerializedData();
        final int size = SharedConfig.proxyList.size();
        serializedData.writeInt32(size);
        for (int i = 0; i < size; ++i) {
            final ProxyInfo proxyInfo = SharedConfig.proxyList.get(i);
            String address = proxyInfo.address;
            if (address == null) {
                address = "";
            }
            serializedData.writeString(address);
            serializedData.writeInt32(proxyInfo.port);
            String username = proxyInfo.username;
            if (username == null) {
                username = "";
            }
            serializedData.writeString(username);
            String password = proxyInfo.password;
            if (password == null) {
                password = "";
            }
            serializedData.writeString(password);
            String secret = proxyInfo.secret;
            if (secret == null) {
                secret = "";
            }
            serializedData.writeString(secret);
        }
        ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putString("proxy_list", Base64.encodeToString(serializedData.toByteArray(), 2)).commit();
        serializedData.cleanup();
    }
    
    public static void setNoSoundHintShowed(final boolean noSoundHintShowed) {
        if (SharedConfig.noSoundHintShowed == noSoundHintShowed) {
            return;
        }
        SharedConfig.noSoundHintShowed = noSoundHintShowed;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("noSoundHintShowed", SharedConfig.noSoundHintShowed);
        edit.commit();
    }
    
    public static void setPassportConfig(final String passportConfigJson, final int passportConfigHash) {
        SharedConfig.passportConfigMap = null;
        SharedConfig.passportConfigJson = passportConfigJson;
        SharedConfig.passportConfigHash = passportConfigHash;
        saveConfig();
        getCountryLangs();
    }
    
    public static void setSecretMapPreviewType(final int mapPreviewType) {
        SharedConfig.mapPreviewType = mapPreviewType;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("mapPreviewType", SharedConfig.mapPreviewType);
        edit.commit();
    }
    
    public static void setSuggestStickers(final int suggestStickers) {
        SharedConfig.suggestStickers = suggestStickers;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("suggestStickers", SharedConfig.suggestStickers);
        edit.commit();
    }
    
    public static void setUseThreeLinesLayout(final boolean useThreeLinesLayout) {
        SharedConfig.useThreeLinesLayout = useThreeLinesLayout;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("useThreeLinesLayout", SharedConfig.useThreeLinesLayout);
        edit.commit();
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.dialogsNeedReload, true);
    }
    
    public static void toggleArchiveHidden() {
        SharedConfig.archiveHidden ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("archiveHidden", SharedConfig.archiveHidden);
        edit.commit();
    }
    
    public static void toggleAutoplayGifs() {
        SharedConfig.autoplayGifs ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("autoplay_gif", SharedConfig.autoplayGifs);
        edit.commit();
    }
    
    public static void toggleAutoplayVideo() {
        SharedConfig.autoplayVideo ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("autoplay_video", SharedConfig.autoplayVideo);
        edit.commit();
    }
    
    public static void toggleCustomTabs() {
        SharedConfig.customTabs ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("custom_tabs", SharedConfig.customTabs);
        edit.commit();
    }
    
    public static void toggleDirectShare() {
        SharedConfig.directShare ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("direct_share", SharedConfig.directShare);
        edit.commit();
    }
    
    public static void toggleGroupPhotosEnabled() {
        SharedConfig.groupPhotosEnabled ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("groupPhotosEnabled", SharedConfig.groupPhotosEnabled);
        edit.commit();
    }
    
    public static void toggleInappCamera() {
        SharedConfig.inappCamera ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("inappCamera", SharedConfig.inappCamera);
        edit.commit();
    }
    
    public static void toggleRepeatMode() {
        ++SharedConfig.repeatMode;
        if (SharedConfig.repeatMode > 2) {
            SharedConfig.repeatMode = 0;
        }
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putInt("repeatMode", SharedConfig.repeatMode);
        edit.commit();
    }
    
    public static void toggleRoundCamera16to9() {
        SharedConfig.roundCamera16to9 ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("roundCamera16to9", SharedConfig.roundCamera16to9);
        edit.commit();
    }
    
    public static void toggleSaveStreamMedia() {
        SharedConfig.saveStreamMedia ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("saveStreamMedia", SharedConfig.saveStreamMedia);
        edit.commit();
    }
    
    public static void toggleSaveToGallery() {
        SharedConfig.saveToGallery ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("save_gallery", SharedConfig.saveToGallery);
        edit.commit();
        checkSaveToGalleryFiles();
    }
    
    public static void toggleShuffleMusic(final int n) {
        if (n == 2) {
            SharedConfig.shuffleMusic ^= true;
        }
        else {
            SharedConfig.playOrderReversed ^= true;
        }
        MediaController.getInstance().checkIsNextMediaFileDownloaded();
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("shuffleMusic", SharedConfig.shuffleMusic);
        edit.putBoolean("playOrderReversed", SharedConfig.playOrderReversed);
        edit.commit();
    }
    
    public static void toggleSortContactsByName() {
        SharedConfig.sortContactsByName ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("sortContactsByName", SharedConfig.sortContactsByName);
        edit.commit();
    }
    
    public static void toggleStreamAllVideo() {
        SharedConfig.streamAllVideo ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("streamAllVideo", SharedConfig.streamAllVideo);
        edit.commit();
    }
    
    public static void toggleStreamMedia() {
        SharedConfig.streamMedia ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("streamMedia", SharedConfig.streamMedia);
        edit.commit();
    }
    
    public static void toggleStreamMkv() {
        SharedConfig.streamMkv ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("streamMkv", SharedConfig.streamMkv);
        edit.commit();
    }
    
    public static void toogleRaiseToSpeak() {
        SharedConfig.raiseToSpeak ^= true;
        final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
        edit.putBoolean("raise_to_speak", SharedConfig.raiseToSpeak);
        edit.commit();
    }
    
    public static class ProxyInfo
    {
        public String address;
        public boolean available;
        public long availableCheckTime;
        public boolean checking;
        public String password;
        public long ping;
        public int port;
        public long proxyCheckPingId;
        public String secret;
        public String username;
        
        public ProxyInfo(final String address, final int port, final String username, final String password, final String secret) {
            this.address = address;
            this.port = port;
            this.username = username;
            this.password = password;
            this.secret = secret;
            if (this.address == null) {
                this.address = "";
            }
            if (this.password == null) {
                this.password = "";
            }
            if (this.username == null) {
                this.username = "";
            }
            if (this.secret == null) {
                this.secret = "";
            }
        }
    }
}
