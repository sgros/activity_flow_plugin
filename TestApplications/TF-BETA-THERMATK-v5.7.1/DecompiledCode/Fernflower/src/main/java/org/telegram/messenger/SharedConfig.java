package org.telegram.messenger;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;

public class SharedConfig {
   public static boolean allowBigEmoji;
   public static boolean allowScreenCapture;
   public static boolean appLocked;
   public static boolean archiveHidden;
   public static int autoLockIn = 3600;
   public static boolean autoplayGifs = true;
   public static boolean autoplayVideo = true;
   public static int badPasscodeTries;
   private static boolean configLoaded;
   public static SharedConfig.ProxyInfo currentProxy;
   public static boolean customTabs = true;
   public static boolean directShare = true;
   public static long directShareHash;
   public static boolean drawDialogIcons;
   public static int fontSize;
   public static boolean groupPhotosEnabled = true;
   public static boolean hasCameraCache;
   public static boolean inappCamera = true;
   public static boolean isWaitingForPasscodeEnter;
   public static long lastAppPauseTime;
   private static int lastLocalId = -210000;
   public static int lastPauseTime;
   public static String lastUpdateVersion;
   public static long lastUptimeMillis;
   private static final Object localIdSync = new Object();
   public static int mapPreviewType = 2;
   public static boolean noSoundHintShowed = false;
   public static String passcodeHash;
   public static long passcodeRetryInMs;
   public static byte[] passcodeSalt = new byte[0];
   public static int passcodeType;
   public static int passportConfigHash;
   private static String passportConfigJson = "";
   private static HashMap passportConfigMap;
   public static boolean playOrderReversed;
   public static ArrayList proxyList;
   private static boolean proxyListLoaded;
   public static byte[] pushAuthKey;
   public static byte[] pushAuthKeyId;
   public static String pushString;
   public static boolean raiseToSpeak = true;
   public static int repeatMode;
   public static boolean roundCamera16to9 = true;
   public static boolean saveIncomingPhotos;
   public static boolean saveStreamMedia = true;
   public static boolean saveToGallery;
   public static boolean showAnimatedStickers;
   public static boolean showNotificationsForAllAccounts;
   public static boolean shuffleMusic;
   public static boolean sortContactsByName;
   public static boolean streamAllVideo = false;
   public static boolean streamMedia = true;
   public static boolean streamMkv = false;
   public static int suggestStickers;
   private static final Object sync = new Object();
   public static boolean useFingerprint = true;
   public static boolean useSystemEmoji;
   public static boolean useThreeLinesLayout;

   static {
      showAnimatedStickers = BuildVars.DEBUG_VERSION;
      showNotificationsForAllAccounts = true;
      fontSize = AndroidUtilities.dp(16.0F);
      loadConfig();
      proxyList = new ArrayList();
   }

   public static SharedConfig.ProxyInfo addProxy(SharedConfig.ProxyInfo var0) {
      loadProxyList();
      int var1 = proxyList.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         SharedConfig.ProxyInfo var3 = (SharedConfig.ProxyInfo)proxyList.get(var2);
         if (var0.address.equals(var3.address) && var0.port == var3.port && var0.username.equals(var3.username) && var0.password.equals(var3.password) && var0.secret.equals(var3.secret)) {
            return var3;
         }
      }

      proxyList.add(var0);
      saveProxyList();
      return var0;
   }

   public static boolean checkPasscode(String var0) {
      boolean var1;
      byte[] var2;
      byte[] var5;
      if (passcodeSalt.length == 0) {
         var1 = Utilities.MD5(var0).equals(passcodeHash);
         if (var1) {
            try {
               passcodeSalt = new byte[16];
               Utilities.random.nextBytes(passcodeSalt);
               var2 = var0.getBytes("UTF-8");
               var5 = new byte[var2.length + 32];
               System.arraycopy(passcodeSalt, 0, var5, 0, 16);
               System.arraycopy(var2, 0, var5, 16, var2.length);
               System.arraycopy(passcodeSalt, 0, var5, var2.length + 16, 16);
               passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(var5, 0, var5.length));
               saveConfig();
            } catch (Exception var3) {
               FileLog.e((Throwable)var3);
            }
         }

         return var1;
      } else {
         try {
            var2 = var0.getBytes("UTF-8");
            var5 = new byte[var2.length + 32];
            System.arraycopy(passcodeSalt, 0, var5, 0, 16);
            System.arraycopy(var2, 0, var5, 16, var2.length);
            System.arraycopy(passcodeSalt, 0, var5, var2.length + 16, 16);
            var0 = Utilities.bytesToHex(Utilities.computeSHA256(var5, 0, var5.length));
            var1 = passcodeHash.equals(var0);
            return var1;
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
            return false;
         }
      }
   }

   public static void checkSaveToGalleryFiles() {
      Exception var10000;
      label51: {
         File var0;
         File var1;
         boolean var10001;
         File var2;
         boolean var3;
         try {
            var0 = new File(Environment.getExternalStorageDirectory(), "Telegram");
            var1 = new File(var0, "Telegram Images");
            var1.mkdir();
            var2 = new File(var0, "Telegram Video");
            var2.mkdir();
            var3 = saveToGallery;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label51;
         }

         if (var3) {
            label42: {
               try {
                  if (var1.isDirectory()) {
                     var0 = new File(var1, ".nomedia");
                     var0.delete();
                  }
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label42;
               }

               try {
                  if (var2.isDirectory()) {
                     var1 = new File(var2, ".nomedia");
                     var1.delete();
                  }

                  return;
               } catch (Exception var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            }
         } else {
            label47: {
               try {
                  if (var1.isDirectory()) {
                     var0 = new File(var1, ".nomedia");
                     var0.createNewFile();
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label47;
               }

               try {
                  if (var2.isDirectory()) {
                     var1 = new File(var2, ".nomedia");
                     var1.createNewFile();
                  }

                  return;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }
         }
      }

      Exception var9 = var10000;
      FileLog.e((Throwable)var9);
   }

   public static void clearConfig() {
      saveIncomingPhotos = false;
      appLocked = false;
      passcodeType = 0;
      passcodeRetryInMs = 0L;
      lastUptimeMillis = 0L;
      badPasscodeTries = 0;
      passcodeHash = "";
      passcodeSalt = new byte[0];
      autoLockIn = 3600;
      lastPauseTime = 0;
      useFingerprint = true;
      isWaitingForPasscodeEnter = false;
      allowScreenCapture = false;
      lastUpdateVersion = BuildVars.BUILD_VERSION_STRING;
      saveConfig();
   }

   public static void deleteProxy(SharedConfig.ProxyInfo var0) {
      if (currentProxy == var0) {
         currentProxy = null;
         SharedPreferences var1 = MessagesController.getGlobalMainSettings();
         boolean var2 = var1.getBoolean("proxy_enabled", false);
         Editor var3 = var1.edit();
         var3.putString("proxy_ip", "");
         var3.putString("proxy_pass", "");
         var3.putString("proxy_user", "");
         var3.putString("proxy_secret", "");
         var3.putInt("proxy_port", 1080);
         var3.putBoolean("proxy_enabled", false);
         var3.putBoolean("proxy_enabled_calls", false);
         var3.commit();
         if (var2) {
            ConnectionsManager.setProxySettings(false, "", 0, "", "", "");
         }
      }

      proxyList.remove(var0);
      saveProxyList();
   }

   public static HashMap getCountryLangs() {
      if (passportConfigMap == null) {
         passportConfigMap = new HashMap();

         Throwable var10000;
         label26: {
            JSONObject var0;
            Iterator var1;
            boolean var10001;
            try {
               var0 = new JSONObject(passportConfigJson);
               var1 = var0.keys();
            } catch (Throwable var4) {
               var10000 = var4;
               var10001 = false;
               break label26;
            }

            while(true) {
               try {
                  if (!var1.hasNext()) {
                     return passportConfigMap;
                  }

                  String var5 = (String)var1.next();
                  passportConfigMap.put(var5.toUpperCase(), var0.getString(var5).toUpperCase());
               } catch (Throwable var3) {
                  var10000 = var3;
                  var10001 = false;
                  break;
               }
            }
         }

         Throwable var2 = var10000;
         FileLog.e(var2);
      }

      return passportConfigMap;
   }

   public static int getLastLocalId() {
      // $FF: Couldn't be decompiled
   }

   public static void increaseBadPasscodeTries() {
      ++badPasscodeTries;
      int var0 = badPasscodeTries;
      if (var0 >= 3) {
         if (var0 != 3) {
            if (var0 != 4) {
               if (var0 != 5) {
                  if (var0 != 6) {
                     if (var0 != 7) {
                        passcodeRetryInMs = 30000L;
                     } else {
                        passcodeRetryInMs = 25000L;
                     }
                  } else {
                     passcodeRetryInMs = 20000L;
                  }
               } else {
                  passcodeRetryInMs = 15000L;
               }
            } else {
               passcodeRetryInMs = 10000L;
            }
         } else {
            passcodeRetryInMs = 5000L;
         }

         lastUptimeMillis = SystemClock.elapsedRealtime();
      }

      saveConfig();
   }

   public static boolean isPassportConfigLoaded() {
      boolean var0;
      if (passportConfigMap != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isSecretMapPreviewSet() {
      return MessagesController.getGlobalMainSettings().contains("mapPreviewType");
   }

   public static void loadConfig() {
      Object var0 = sync;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label688: {
         try {
            if (configLoaded) {
               return;
            }
         } catch (Throwable var75) {
            var10000 = var75;
            var10001 = false;
            break label688;
         }

         SharedPreferences var1;
         String var2;
         try {
            var1 = ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
            saveIncomingPhotos = var1.getBoolean("saveIncomingPhotos", false);
            passcodeHash = var1.getString("passcodeHash1", "");
            appLocked = var1.getBoolean("appLocked", false);
            passcodeType = var1.getInt("passcodeType", 0);
            passcodeRetryInMs = var1.getLong("passcodeRetryInMs", 0L);
            lastUptimeMillis = var1.getLong("lastUptimeMillis", 0L);
            badPasscodeTries = var1.getInt("badPasscodeTries", 0);
            autoLockIn = var1.getInt("autoLockIn", 3600);
            lastPauseTime = var1.getInt("lastPauseTime", 0);
            lastAppPauseTime = var1.getLong("lastAppPauseTime", 0L);
            useFingerprint = var1.getBoolean("useFingerprint", true);
            lastUpdateVersion = var1.getString("lastUpdateVersion2", "3.5");
            allowScreenCapture = var1.getBoolean("allowScreenCapture", false);
            lastLocalId = var1.getInt("lastLocalId", -210000);
            pushString = var1.getString("pushString2", "");
            passportConfigJson = var1.getString("passportConfigJson", "");
            passportConfigHash = var1.getInt("passportConfigHash", 0);
            var2 = var1.getString("pushAuthKey", (String)null);
            if (!TextUtils.isEmpty(var2)) {
               pushAuthKey = Base64.decode(var2, 0);
            }
         } catch (Throwable var74) {
            var10000 = var74;
            var10001 = false;
            break label688;
         }

         try {
            if (passcodeHash.length() > 0 && lastPauseTime == 0) {
               lastPauseTime = (int)(System.currentTimeMillis() / 1000L - 600L);
            }
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label688;
         }

         label689: {
            try {
               var2 = var1.getString("passcodeSalt", "");
               if (var2.length() > 0) {
                  passcodeSalt = Base64.decode(var2, 0);
                  break label689;
               }
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label688;
            }

            try {
               passcodeSalt = new byte[0];
            } catch (Throwable var71) {
               var10000 = var71;
               var10001 = false;
               break label688;
            }
         }

         byte var3;
         SharedPreferences var76;
         label664: {
            label663: {
               try {
                  var76 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                  saveToGallery = var76.getBoolean("save_gallery", false);
                  autoplayGifs = var76.getBoolean("autoplay_gif", true);
                  autoplayVideo = var76.getBoolean("autoplay_video", true);
                  mapPreviewType = var76.getInt("mapPreviewType", 2);
                  raiseToSpeak = var76.getBoolean("raise_to_speak", true);
                  customTabs = var76.getBoolean("custom_tabs", true);
                  directShare = var76.getBoolean("direct_share", true);
                  shuffleMusic = var76.getBoolean("shuffleMusic", false);
                  playOrderReversed = var76.getBoolean("playOrderReversed", false);
                  inappCamera = var76.getBoolean("inappCamera", true);
                  hasCameraCache = var76.contains("cameraCache");
                  roundCamera16to9 = true;
                  groupPhotosEnabled = var76.getBoolean("groupPhotosEnabled", true);
                  repeatMode = var76.getInt("repeatMode", 0);
                  if (!AndroidUtilities.isTablet()) {
                     break label663;
                  }
               } catch (Throwable var70) {
                  var10000 = var70;
                  var10001 = false;
                  break label688;
               }

               var3 = 18;
               break label664;
            }

            var3 = 16;
         }

         label657:
         try {
            fontSize = var76.getInt("fons_size", var3);
            allowBigEmoji = var76.getBoolean("allowBigEmoji", true);
            useSystemEmoji = var76.getBoolean("useSystemEmoji", false);
            streamMedia = var76.getBoolean("streamMedia", true);
            saveStreamMedia = var76.getBoolean("saveStreamMedia", true);
            streamAllVideo = var76.getBoolean("streamAllVideo", BuildVars.DEBUG_VERSION);
            streamMkv = var76.getBoolean("streamMkv", false);
            suggestStickers = var76.getInt("suggestStickers", 0);
            sortContactsByName = var76.getBoolean("sortContactsByName", false);
            noSoundHintShowed = var76.getBoolean("noSoundHintShowed", false);
            directShareHash = var76.getLong("directShareHash", 0L);
            useThreeLinesLayout = var76.getBoolean("useThreeLinesLayout", false);
            archiveHidden = var76.getBoolean("archiveHidden", false);
            showNotificationsForAllAccounts = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0).getBoolean("AllAccounts", true);
            configLoaded = true;
            return;
         } catch (Throwable var69) {
            var10000 = var69;
            var10001 = false;
            break label657;
         }
      }

      while(true) {
         Throwable var77 = var10000;

         try {
            throw var77;
         } catch (Throwable var68) {
            var10000 = var68;
            var10001 = false;
            continue;
         }
      }
   }

   public static void loadProxyList() {
      if (!proxyListLoaded) {
         SharedPreferences var0 = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
         String var1 = var0.getString("proxy_ip", "");
         String var2 = var0.getString("proxy_user", "");
         String var3 = var0.getString("proxy_pass", "");
         String var4 = var0.getString("proxy_secret", "");
         int var5 = var0.getInt("proxy_port", 1080);
         proxyListLoaded = true;
         proxyList.clear();
         currentProxy = null;
         String var9 = var0.getString("proxy_list", (String)null);
         if (!TextUtils.isEmpty(var9)) {
            SerializedData var10 = new SerializedData(Base64.decode(var9, 0));
            int var6 = var10.readInt32(false);

            for(int var7 = 0; var7 < var6; ++var7) {
               SharedConfig.ProxyInfo var8 = new SharedConfig.ProxyInfo(var10.readString(false), var10.readInt32(false), var10.readString(false), var10.readString(false), var10.readString(false));
               proxyList.add(var8);
               if (currentProxy == null && !TextUtils.isEmpty(var1) && var1.equals(var8.address) && var5 == var8.port && var2.equals(var8.username) && var3.equals(var8.password)) {
                  currentProxy = var8;
               }
            }

            var10.cleanup();
         }

         if (currentProxy == null && !TextUtils.isEmpty(var1)) {
            SharedConfig.ProxyInfo var11 = new SharedConfig.ProxyInfo(var1, var5, var2, var3, var4);
            currentProxy = var11;
            proxyList.add(0, var11);
         }

      }
   }

   public static void saveConfig() {
      // $FF: Couldn't be decompiled
   }

   public static void saveProxyList() {
      SerializedData var0 = new SerializedData();
      int var1 = proxyList.size();
      var0.writeInt32(var1);

      for(int var2 = 0; var2 < var1; ++var2) {
         SharedConfig.ProxyInfo var3 = (SharedConfig.ProxyInfo)proxyList.get(var2);
         String var4 = var3.address;
         if (var4 == null) {
            var4 = "";
         }

         var0.writeString(var4);
         var0.writeInt32(var3.port);
         var4 = var3.username;
         if (var4 == null) {
            var4 = "";
         }

         var0.writeString(var4);
         var4 = var3.password;
         if (var4 == null) {
            var4 = "";
         }

         var0.writeString(var4);
         var4 = var3.secret;
         if (var4 == null) {
            var4 = "";
         }

         var0.writeString(var4);
      }

      ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit().putString("proxy_list", Base64.encodeToString(var0.toByteArray(), 2)).commit();
      var0.cleanup();
   }

   public static void setNoSoundHintShowed(boolean var0) {
      if (noSoundHintShowed != var0) {
         noSoundHintShowed = var0;
         Editor var1 = MessagesController.getGlobalMainSettings().edit();
         var1.putBoolean("noSoundHintShowed", noSoundHintShowed);
         var1.commit();
      }
   }

   public static void setPassportConfig(String var0, int var1) {
      passportConfigMap = null;
      passportConfigJson = var0;
      passportConfigHash = var1;
      saveConfig();
      getCountryLangs();
   }

   public static void setSecretMapPreviewType(int var0) {
      mapPreviewType = var0;
      Editor var1 = MessagesController.getGlobalMainSettings().edit();
      var1.putInt("mapPreviewType", mapPreviewType);
      var1.commit();
   }

   public static void setSuggestStickers(int var0) {
      suggestStickers = var0;
      Editor var1 = MessagesController.getGlobalMainSettings().edit();
      var1.putInt("suggestStickers", suggestStickers);
      var1.commit();
   }

   public static void setUseThreeLinesLayout(boolean var0) {
      useThreeLinesLayout = var0;
      Editor var1 = MessagesController.getGlobalMainSettings().edit();
      var1.putBoolean("useThreeLinesLayout", useThreeLinesLayout);
      var1.commit();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.dialogsNeedReload, true);
   }

   public static void toggleArchiveHidden() {
      archiveHidden ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("archiveHidden", archiveHidden);
      var0.commit();
   }

   public static void toggleAutoplayGifs() {
      autoplayGifs ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("autoplay_gif", autoplayGifs);
      var0.commit();
   }

   public static void toggleAutoplayVideo() {
      autoplayVideo ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("autoplay_video", autoplayVideo);
      var0.commit();
   }

   public static void toggleCustomTabs() {
      customTabs ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("custom_tabs", customTabs);
      var0.commit();
   }

   public static void toggleDirectShare() {
      directShare ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("direct_share", directShare);
      var0.commit();
   }

   public static void toggleGroupPhotosEnabled() {
      groupPhotosEnabled ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("groupPhotosEnabled", groupPhotosEnabled);
      var0.commit();
   }

   public static void toggleInappCamera() {
      inappCamera ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("inappCamera", inappCamera);
      var0.commit();
   }

   public static void toggleRepeatMode() {
      ++repeatMode;
      if (repeatMode > 2) {
         repeatMode = 0;
      }

      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putInt("repeatMode", repeatMode);
      var0.commit();
   }

   public static void toggleRoundCamera16to9() {
      roundCamera16to9 ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("roundCamera16to9", roundCamera16to9);
      var0.commit();
   }

   public static void toggleSaveStreamMedia() {
      saveStreamMedia ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("saveStreamMedia", saveStreamMedia);
      var0.commit();
   }

   public static void toggleSaveToGallery() {
      saveToGallery ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("save_gallery", saveToGallery);
      var0.commit();
      checkSaveToGalleryFiles();
   }

   public static void toggleShuffleMusic(int var0) {
      if (var0 == 2) {
         shuffleMusic ^= true;
      } else {
         playOrderReversed ^= true;
      }

      MediaController.getInstance().checkIsNextMediaFileDownloaded();
      Editor var1 = MessagesController.getGlobalMainSettings().edit();
      var1.putBoolean("shuffleMusic", shuffleMusic);
      var1.putBoolean("playOrderReversed", playOrderReversed);
      var1.commit();
   }

   public static void toggleSortContactsByName() {
      sortContactsByName ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("sortContactsByName", sortContactsByName);
      var0.commit();
   }

   public static void toggleStreamAllVideo() {
      streamAllVideo ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("streamAllVideo", streamAllVideo);
      var0.commit();
   }

   public static void toggleStreamMedia() {
      streamMedia ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("streamMedia", streamMedia);
      var0.commit();
   }

   public static void toggleStreamMkv() {
      streamMkv ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("streamMkv", streamMkv);
      var0.commit();
   }

   public static void toogleRaiseToSpeak() {
      raiseToSpeak ^= true;
      Editor var0 = MessagesController.getGlobalMainSettings().edit();
      var0.putBoolean("raise_to_speak", raiseToSpeak);
      var0.commit();
   }

   public static class ProxyInfo {
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

      public ProxyInfo(String var1, int var2, String var3, String var4, String var5) {
         this.address = var1;
         this.port = var2;
         this.username = var3;
         this.password = var4;
         this.secret = var5;
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
