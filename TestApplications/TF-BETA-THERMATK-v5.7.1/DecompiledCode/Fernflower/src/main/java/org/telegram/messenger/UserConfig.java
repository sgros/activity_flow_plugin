package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import java.io.File;
import org.telegram.tgnet.TLRPC;

public class UserConfig {
   private static volatile UserConfig[] Instance = new UserConfig[3];
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
   public int lastBroadcastId = -1;
   public int lastContactsSyncTime;
   public int lastHintsSyncTime;
   public int lastSendMessageId = -210000;
   public long lastUpdateCheckTime;
   public int loginTime;
   public long migrateOffsetAccess = -1L;
   public int migrateOffsetChannelId = -1;
   public int migrateOffsetChatId = -1;
   public int migrateOffsetDate = -1;
   public int migrateOffsetId = -1;
   public int migrateOffsetUserId = -1;
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
   public boolean suggestContacts = true;
   private final Object sync = new Object();
   public boolean syncContacts = true;
   public TLRPC.TL_account_tmpPassword tmpPassword;
   public TLRPC.TL_help_termsOfService unacceptedTermsOfService;
   public boolean unreadDialogsLoaded = true;

   public UserConfig(int var1) {
      this.currentAccount = var1;
   }

   public static int getActivatedAccountsCount() {
      int var0 = 0;

      int var1;
      int var2;
      for(var1 = 0; var0 < 3; var1 = var2) {
         var2 = var1;
         if (getInstance(var0).isClientActivated()) {
            var2 = var1 + 1;
         }

         ++var0;
      }

      return var1;
   }

   public static UserConfig getInstance(int var0) {
      UserConfig var1 = Instance[var0];
      UserConfig var2 = var1;
      if (var1 == null) {
         synchronized(UserConfig.class){}

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
               UserConfig[] var23;
               try {
                  var23 = Instance;
                  var2 = new UserConfig(var0);
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

   private SharedPreferences getPreferences() {
      if (this.currentAccount == 0) {
         return ApplicationLoader.applicationContext.getSharedPreferences("userconfing", 0);
      } else {
         Context var1 = ApplicationLoader.applicationContext;
         StringBuilder var2 = new StringBuilder();
         var2.append("userconfig");
         var2.append(this.currentAccount);
         return var1.getSharedPreferences(var2.toString(), 0);
      }
   }

   public void checkSavedPassword() {
      if ((this.savedSaltedPassword != null || this.savedPasswordHash != null) && Math.abs(SystemClock.elapsedRealtime() - this.savedPasswordTime) >= 1800000L) {
         this.resetSavedPassword();
      }
   }

   public void clearConfig() {
      this.getPreferences().edit().clear().commit();
      this.currentUser = null;
      boolean var1 = false;
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
      int var2 = 0;

      boolean var3;
      while(true) {
         var3 = var1;
         if (var2 >= 3) {
            break;
         }

         if (getInstance(var2).isClientActivated()) {
            var3 = true;
            break;
         }

         ++var2;
      }

      if (!var3) {
         SharedConfig.clearConfig();
      }

      this.saveConfig(true);
   }

   public String getClientPhone() {
      Object var1 = this.sync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label158: {
         String var2;
         label152: {
            try {
               if (this.currentUser != null && this.currentUser.phone != null) {
                  var2 = this.currentUser.phone;
                  break label152;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label158;
            }

            var2 = "";
         }

         label143:
         try {
            return var2;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label143;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public int getClientUserId() {
      Object var1 = this.sync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label150: {
         int var2;
         label144: {
            try {
               if (this.currentUser != null) {
                  var2 = this.currentUser.id;
                  break label144;
               }
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label150;
            }

            var2 = 0;
         }

         label136:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label136;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public TLRPC.User getCurrentUser() {
      // $FF: Couldn't be decompiled
   }

   public int[] getDialogLoadOffsets(int var1) {
      SharedPreferences var2 = this.getPreferences();
      StringBuilder var3 = new StringBuilder();
      var3.append("2dialogsLoadOffsetId");
      String var4 = "";
      Object var5;
      if (var1 == 0) {
         var5 = "";
      } else {
         var5 = var1;
      }

      var3.append(var5);
      String var15 = var3.toString();
      boolean var6 = this.hasValidDialogLoadIds;
      byte var7 = -1;
      byte var8;
      if (var6) {
         var8 = 0;
      } else {
         var8 = -1;
      }

      int var9 = var2.getInt(var15, var8);
      var3 = new StringBuilder();
      var3.append("2dialogsLoadOffsetDate");
      if (var1 == 0) {
         var5 = "";
      } else {
         var5 = var1;
      }

      var3.append(var5);
      var15 = var3.toString();
      if (this.hasValidDialogLoadIds) {
         var8 = 0;
      } else {
         var8 = -1;
      }

      int var10 = var2.getInt(var15, var8);
      var3 = new StringBuilder();
      var3.append("2dialogsLoadOffsetUserId");
      if (var1 == 0) {
         var5 = "";
      } else {
         var5 = var1;
      }

      var3.append(var5);
      var15 = var3.toString();
      if (this.hasValidDialogLoadIds) {
         var8 = 0;
      } else {
         var8 = -1;
      }

      int var11 = var2.getInt(var15, var8);
      var3 = new StringBuilder();
      var3.append("2dialogsLoadOffsetChatId");
      if (var1 == 0) {
         var5 = "";
      } else {
         var5 = var1;
      }

      var3.append(var5);
      var15 = var3.toString();
      if (this.hasValidDialogLoadIds) {
         var8 = 0;
      } else {
         var8 = -1;
      }

      int var12 = var2.getInt(var15, var8);
      var3 = new StringBuilder();
      var3.append("2dialogsLoadOffsetChannelId");
      if (var1 == 0) {
         var5 = "";
      } else {
         var5 = var1;
      }

      var3.append(var5);
      var15 = var3.toString();
      var8 = var7;
      if (this.hasValidDialogLoadIds) {
         var8 = 0;
      }

      int var16 = var2.getInt(var15, var8);
      var3 = new StringBuilder();
      var3.append("2dialogsLoadOffsetAccess");
      if (var1 == 0) {
         var5 = var4;
      } else {
         var5 = var1;
      }

      var3.append(var5);
      var15 = var3.toString();
      long var13;
      if (this.hasValidDialogLoadIds) {
         var13 = 0L;
      } else {
         var13 = -1L;
      }

      var13 = var2.getLong(var15, var13);
      return new int[]{var9, var10, var11, var12, var16, (int)var13, (int)(var13 >> 32)};
   }

   public int getNewMessageId() {
      // $FF: Couldn't be decompiled
   }

   public int getTotalDialogsCount(int var1) {
      SharedPreferences var2 = this.getPreferences();
      StringBuilder var3 = new StringBuilder();
      var3.append("2totalDialogsLoadCount");
      Object var4;
      if (var1 == 0) {
         var4 = "";
      } else {
         var4 = var1;
      }

      var3.append(var4);
      return var2.getInt(var3.toString(), 0);
   }

   public boolean isClientActivated() {
      Object var1 = this.sync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label134: {
         boolean var2;
         label133: {
            label132: {
               try {
                  if (this.currentUser != null) {
                     break label132;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label134;
               }

               var2 = false;
               break label133;
            }

            var2 = true;
         }

         label126:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label126;
         }
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isPinnedDialogsLoaded(int var1) {
      SharedPreferences var2 = this.getPreferences();
      StringBuilder var3 = new StringBuilder();
      var3.append("2pinnedDialogsLoaded");
      var3.append(var1);
      return var2.getBoolean(var3.toString(), false);
   }

   // $FF: synthetic method
   public void lambda$loadConfig$0$UserConfig() {
      this.saveConfig(false);
   }

   public void loadConfig() {
      // $FF: Couldn't be decompiled
   }

   public void resetSavedPassword() {
      this.savedPasswordTime = 0L;
      int var1;
      if (this.savedPasswordHash != null) {
         for(var1 = 0; var1 < this.savedPasswordHash.length; ++var1) {
            this.savedPasswordHash[var1] = (byte)0;
         }

         this.savedPasswordHash = null;
      }

      if (this.savedSaltedPassword != null) {
         for(var1 = 0; var1 < this.savedSaltedPassword.length; ++var1) {
            this.savedSaltedPassword[var1] = (byte)0;
         }

         this.savedSaltedPassword = null;
      }

   }

   public void saveConfig(boolean var1) {
      this.saveConfig(var1, (File)null);
   }

   public void saveConfig(boolean param1, File param2) {
      // $FF: Couldn't be decompiled
   }

   public void savePassword(byte[] var1, byte[] var2) {
      this.savedPasswordTime = SystemClock.elapsedRealtime();
      this.savedPasswordHash = var1;
      this.savedSaltedPassword = var2;
   }

   public void setCurrentUser(TLRPC.User param1) {
      // $FF: Couldn't be decompiled
   }

   public void setDialogsLoadOffset(int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      Editor var9 = this.getPreferences().edit();
      StringBuilder var10 = new StringBuilder();
      var10.append("2dialogsLoadOffsetId");
      String var11 = "";
      Object var12;
      if (var1 == 0) {
         var12 = "";
      } else {
         var12 = var1;
      }

      var10.append(var12);
      var9.putInt(var10.toString(), var2);
      var10 = new StringBuilder();
      var10.append("2dialogsLoadOffsetDate");
      if (var1 == 0) {
         var12 = "";
      } else {
         var12 = var1;
      }

      var10.append(var12);
      var9.putInt(var10.toString(), var3);
      var10 = new StringBuilder();
      var10.append("2dialogsLoadOffsetUserId");
      if (var1 == 0) {
         var12 = "";
      } else {
         var12 = var1;
      }

      var10.append(var12);
      var9.putInt(var10.toString(), var4);
      var10 = new StringBuilder();
      var10.append("2dialogsLoadOffsetChatId");
      if (var1 == 0) {
         var12 = "";
      } else {
         var12 = var1;
      }

      var10.append(var12);
      var9.putInt(var10.toString(), var5);
      var10 = new StringBuilder();
      var10.append("2dialogsLoadOffsetChannelId");
      if (var1 == 0) {
         var12 = "";
      } else {
         var12 = var1;
      }

      var10.append(var12);
      var9.putInt(var10.toString(), var6);
      var10 = new StringBuilder();
      var10.append("2dialogsLoadOffsetAccess");
      if (var1 == 0) {
         var12 = var11;
      } else {
         var12 = var1;
      }

      var10.append(var12);
      var9.putLong(var10.toString(), var7);
      var9.putBoolean("hasValidDialogLoadIds", true);
      var9.commit();
   }

   public void setPinnedDialogsLoaded(int var1, boolean var2) {
      Editor var3 = this.getPreferences().edit();
      StringBuilder var4 = new StringBuilder();
      var4.append("2pinnedDialogsLoaded");
      var4.append(var1);
      var3.putBoolean(var4.toString(), var2).commit();
   }

   public void setTotalDialogsCount(int var1, int var2) {
      Editor var3 = this.getPreferences().edit();
      StringBuilder var4 = new StringBuilder();
      var4.append("2totalDialogsLoadCount");
      Object var5;
      if (var1 == 0) {
         var5 = "";
      } else {
         var5 = var1;
      }

      var4.append(var5);
      var3.putInt(var4.toString(), var2).commit();
   }
}
