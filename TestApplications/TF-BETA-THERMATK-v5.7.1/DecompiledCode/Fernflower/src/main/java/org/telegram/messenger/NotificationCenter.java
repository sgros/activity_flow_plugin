package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;

public class NotificationCenter {
   public static final int FileDidFailUpload;
   public static final int FileDidUpload;
   public static final int FileLoadProgressChanged;
   public static final int FileUploadProgressChanged;
   private static volatile NotificationCenter[] Instance;
   public static final int albumsDidLoad;
   public static final int appDidLogout;
   public static final int archivedStickersCountDidLoad;
   public static final int audioDidSent;
   public static final int audioRecordTooShort;
   public static final int audioRouteChanged;
   public static final int blockedUsersDidLoad;
   public static final int botInfoDidLoad;
   public static final int botKeyboardDidLoad;
   public static final int cameraInitied;
   public static final int channelRightsUpdated;
   public static final int chatDidCreated;
   public static final int chatDidFailCreate;
   public static final int chatInfoCantLoad;
   public static final int chatInfoDidLoad;
   public static final int chatOnlineCountDidLoad;
   public static final int chatSearchResultsAvailable;
   public static final int chatSearchResultsLoading;
   public static final int closeChats;
   public static final int closeInCallActivity;
   public static final int closeOtherAppActivities;
   public static final int closeSearchByActiveAction;
   public static final int configLoaded;
   public static final int contactsDidLoad;
   public static final int contactsImported;
   public static final int dialogPhotosLoaded;
   public static final int dialogsNeedReload;
   public static final int dialogsUnreadCounterChanged;
   public static final int didCreatedNewDeleteTask;
   public static final int didEndedCall;
   public static final int didReceiveCall;
   public static final int didReceiveNewMessages;
   public static final int didReceiveSmsCode;
   public static final int didReceivedWebpages;
   public static final int didReceivedWebpagesInUpdates;
   public static final int didRemoveTwoStepPassword;
   public static final int didReplacedPhotoInMemCache;
   public static final int didSetNewTheme;
   public static final int didSetNewWallpapper;
   public static final int didSetPasscode;
   public static final int didSetTwoStepPassword;
   public static final int didStartedCall;
   public static final int didUpdateConnectionState;
   public static final int didUpdatePollResults;
   public static final int didUpdatedMessagesViews;
   public static final int emojiDidLoad;
   public static final int encryptedChatCreated;
   public static final int encryptedChatUpdated;
   public static final int featuredStickersDidLoad;
   public static final int fileDidFailedLoad;
   public static final int fileDidLoad;
   public static final int fileNewChunkAvailable;
   public static final int filePreparingFailed;
   public static final int filePreparingStarted;
   public static final int folderBecomeEmpty;
   private static volatile NotificationCenter globalInstance;
   public static final int groupStickersDidLoad;
   public static final int hasNewContactsToImport;
   public static final int historyCleared;
   public static final int httpFileDidFailedLoad;
   public static final int httpFileDidLoad;
   public static final int liveLocationsCacheChanged;
   public static final int liveLocationsChanged;
   public static final int locationPermissionGranted;
   public static final int mainUserInfoChanged;
   public static final int mediaCountDidLoad;
   public static final int mediaCountsDidLoad;
   public static final int mediaDidLoad;
   public static final int messagePlayingDidReset;
   public static final int messagePlayingDidSeek;
   public static final int messagePlayingDidStart;
   public static final int messagePlayingGoingToStop;
   public static final int messagePlayingPlayStateChanged;
   public static final int messagePlayingProgressDidChanged;
   public static final int messageReceivedByAck;
   public static final int messageReceivedByServer;
   public static final int messageSendError;
   public static final int messagesDeleted;
   public static final int messagesDidLoad;
   public static final int messagesRead;
   public static final int messagesReadContent;
   public static final int messagesReadEncrypted;
   public static final int musicDidLoad;
   public static final int needDeleteDialog;
   public static final int needReloadArchivedStickers;
   public static final int needReloadRecentDialogsSearch;
   public static final int needSetDayNightTheme;
   public static final int needShowAlert;
   public static final int newDraftReceived;
   public static final int newEmojiSuggestionsAvailable;
   public static final int newSessionReceived;
   public static final int notificationsCountUpdated;
   public static final int notificationsSettingsUpdated;
   public static final int openArticle;
   public static final int openedChatChanged;
   public static final int paymentFinished;
   public static final int peerSettingsDidLoad;
   public static final int pinnedMessageDidLoad;
   public static final int playerDidStartPlaying;
   public static final int privacyRulesUpdated;
   public static final int proxyCheckDone;
   public static final int proxySettingsChanged;
   public static final int pushMessagesUpdated;
   public static final int recentDocumentsDidLoad;
   public static final int recentImagesDidLoad;
   public static final int recordProgressChanged;
   public static final int recordStartError;
   public static final int recordStarted;
   public static final int recordStopped;
   public static final int reloadHints;
   public static final int reloadInlineHints;
   public static final int reloadInterface;
   public static final int removeAllMessagesFromDialog;
   public static final int replaceMessagesObjects;
   public static final int replyMessagesDidLoad;
   public static final int screenshotTook;
   public static final int stickersDidLoad;
   public static final int stopEncodingService;
   public static final int suggestedLangpack;
   public static final int themeListUpdated;
   private static int totalEvents;
   public static final int updateInterfaces;
   public static final int updateMentionsCount;
   public static final int updateMessageMedia;
   public static final int userInfoDidLoad;
   public static final int videoLoadingStateChanged;
   public static final int wallpapersDidLoad;
   public static final int wallpapersNeedReload;
   public static final int wasUnableToFindCurrentLocation;
   private SparseArray addAfterBroadcast = new SparseArray();
   private int[] allowedNotifications;
   private boolean animationInProgress;
   private int broadcasting = 0;
   private int currentAccount;
   private ArrayList delayedPosts = new ArrayList(10);
   private SparseArray observers = new SparseArray();
   private SparseArray removeAfterBroadcast = new SparseArray();

   static {
      int var0 = totalEvents++;
      didReceiveNewMessages = var0;
      var0 = totalEvents++;
      updateInterfaces = var0;
      var0 = totalEvents++;
      dialogsNeedReload = var0;
      var0 = totalEvents++;
      closeChats = var0;
      var0 = totalEvents++;
      messagesDeleted = var0;
      var0 = totalEvents++;
      historyCleared = var0;
      var0 = totalEvents++;
      messagesRead = var0;
      var0 = totalEvents++;
      messagesDidLoad = var0;
      var0 = totalEvents++;
      messageReceivedByAck = var0;
      var0 = totalEvents++;
      messageReceivedByServer = var0;
      var0 = totalEvents++;
      messageSendError = var0;
      var0 = totalEvents++;
      contactsDidLoad = var0;
      var0 = totalEvents++;
      contactsImported = var0;
      var0 = totalEvents++;
      hasNewContactsToImport = var0;
      var0 = totalEvents++;
      chatDidCreated = var0;
      var0 = totalEvents++;
      chatDidFailCreate = var0;
      var0 = totalEvents++;
      chatInfoDidLoad = var0;
      var0 = totalEvents++;
      chatInfoCantLoad = var0;
      var0 = totalEvents++;
      mediaDidLoad = var0;
      var0 = totalEvents++;
      mediaCountDidLoad = var0;
      var0 = totalEvents++;
      mediaCountsDidLoad = var0;
      var0 = totalEvents++;
      encryptedChatUpdated = var0;
      var0 = totalEvents++;
      messagesReadEncrypted = var0;
      var0 = totalEvents++;
      encryptedChatCreated = var0;
      var0 = totalEvents++;
      dialogPhotosLoaded = var0;
      var0 = totalEvents++;
      folderBecomeEmpty = var0;
      var0 = totalEvents++;
      removeAllMessagesFromDialog = var0;
      var0 = totalEvents++;
      notificationsSettingsUpdated = var0;
      var0 = totalEvents++;
      blockedUsersDidLoad = var0;
      var0 = totalEvents++;
      openedChatChanged = var0;
      var0 = totalEvents++;
      didCreatedNewDeleteTask = var0;
      var0 = totalEvents++;
      mainUserInfoChanged = var0;
      var0 = totalEvents++;
      privacyRulesUpdated = var0;
      var0 = totalEvents++;
      updateMessageMedia = var0;
      var0 = totalEvents++;
      recentImagesDidLoad = var0;
      var0 = totalEvents++;
      replaceMessagesObjects = var0;
      var0 = totalEvents++;
      didSetPasscode = var0;
      var0 = totalEvents++;
      didSetTwoStepPassword = var0;
      var0 = totalEvents++;
      didRemoveTwoStepPassword = var0;
      var0 = totalEvents++;
      replyMessagesDidLoad = var0;
      var0 = totalEvents++;
      pinnedMessageDidLoad = var0;
      var0 = totalEvents++;
      newSessionReceived = var0;
      var0 = totalEvents++;
      didReceivedWebpages = var0;
      var0 = totalEvents++;
      didReceivedWebpagesInUpdates = var0;
      var0 = totalEvents++;
      stickersDidLoad = var0;
      var0 = totalEvents++;
      featuredStickersDidLoad = var0;
      var0 = totalEvents++;
      groupStickersDidLoad = var0;
      var0 = totalEvents++;
      messagesReadContent = var0;
      var0 = totalEvents++;
      botInfoDidLoad = var0;
      var0 = totalEvents++;
      userInfoDidLoad = var0;
      var0 = totalEvents++;
      botKeyboardDidLoad = var0;
      var0 = totalEvents++;
      chatSearchResultsAvailable = var0;
      var0 = totalEvents++;
      chatSearchResultsLoading = var0;
      var0 = totalEvents++;
      musicDidLoad = var0;
      var0 = totalEvents++;
      needShowAlert = var0;
      var0 = totalEvents++;
      didUpdatedMessagesViews = var0;
      var0 = totalEvents++;
      needReloadRecentDialogsSearch = var0;
      var0 = totalEvents++;
      peerSettingsDidLoad = var0;
      var0 = totalEvents++;
      wasUnableToFindCurrentLocation = var0;
      var0 = totalEvents++;
      reloadHints = var0;
      var0 = totalEvents++;
      reloadInlineHints = var0;
      var0 = totalEvents++;
      newDraftReceived = var0;
      var0 = totalEvents++;
      recentDocumentsDidLoad = var0;
      var0 = totalEvents++;
      needReloadArchivedStickers = var0;
      var0 = totalEvents++;
      archivedStickersCountDidLoad = var0;
      var0 = totalEvents++;
      paymentFinished = var0;
      var0 = totalEvents++;
      channelRightsUpdated = var0;
      var0 = totalEvents++;
      openArticle = var0;
      var0 = totalEvents++;
      updateMentionsCount = var0;
      var0 = totalEvents++;
      didUpdatePollResults = var0;
      var0 = totalEvents++;
      chatOnlineCountDidLoad = var0;
      var0 = totalEvents++;
      videoLoadingStateChanged = var0;
      var0 = totalEvents++;
      httpFileDidLoad = var0;
      var0 = totalEvents++;
      httpFileDidFailedLoad = var0;
      var0 = totalEvents++;
      didUpdateConnectionState = var0;
      var0 = totalEvents++;
      FileDidUpload = var0;
      var0 = totalEvents++;
      FileDidFailUpload = var0;
      var0 = totalEvents++;
      FileUploadProgressChanged = var0;
      var0 = totalEvents++;
      FileLoadProgressChanged = var0;
      var0 = totalEvents++;
      fileDidLoad = var0;
      var0 = totalEvents++;
      fileDidFailedLoad = var0;
      var0 = totalEvents++;
      filePreparingStarted = var0;
      var0 = totalEvents++;
      fileNewChunkAvailable = var0;
      var0 = totalEvents++;
      filePreparingFailed = var0;
      var0 = totalEvents++;
      dialogsUnreadCounterChanged = var0;
      var0 = totalEvents++;
      messagePlayingProgressDidChanged = var0;
      var0 = totalEvents++;
      messagePlayingDidReset = var0;
      var0 = totalEvents++;
      messagePlayingPlayStateChanged = var0;
      var0 = totalEvents++;
      messagePlayingDidStart = var0;
      var0 = totalEvents++;
      messagePlayingDidSeek = var0;
      var0 = totalEvents++;
      messagePlayingGoingToStop = var0;
      var0 = totalEvents++;
      recordProgressChanged = var0;
      var0 = totalEvents++;
      recordStarted = var0;
      var0 = totalEvents++;
      recordStartError = var0;
      var0 = totalEvents++;
      recordStopped = var0;
      var0 = totalEvents++;
      screenshotTook = var0;
      var0 = totalEvents++;
      albumsDidLoad = var0;
      var0 = totalEvents++;
      audioDidSent = var0;
      var0 = totalEvents++;
      audioRecordTooShort = var0;
      var0 = totalEvents++;
      audioRouteChanged = var0;
      var0 = totalEvents++;
      didStartedCall = var0;
      var0 = totalEvents++;
      didEndedCall = var0;
      var0 = totalEvents++;
      closeInCallActivity = var0;
      var0 = totalEvents++;
      appDidLogout = var0;
      var0 = totalEvents++;
      configLoaded = var0;
      var0 = totalEvents++;
      needDeleteDialog = var0;
      var0 = totalEvents++;
      newEmojiSuggestionsAvailable = var0;
      var0 = totalEvents++;
      pushMessagesUpdated = var0;
      var0 = totalEvents++;
      stopEncodingService = var0;
      var0 = totalEvents++;
      wallpapersDidLoad = var0;
      var0 = totalEvents++;
      wallpapersNeedReload = var0;
      var0 = totalEvents++;
      didReceiveSmsCode = var0;
      var0 = totalEvents++;
      didReceiveCall = var0;
      var0 = totalEvents++;
      emojiDidLoad = var0;
      var0 = totalEvents++;
      closeOtherAppActivities = var0;
      var0 = totalEvents++;
      cameraInitied = var0;
      var0 = totalEvents++;
      didReplacedPhotoInMemCache = var0;
      var0 = totalEvents++;
      didSetNewTheme = var0;
      var0 = totalEvents++;
      themeListUpdated = var0;
      var0 = totalEvents++;
      needSetDayNightTheme = var0;
      var0 = totalEvents++;
      locationPermissionGranted = var0;
      var0 = totalEvents++;
      reloadInterface = var0;
      var0 = totalEvents++;
      suggestedLangpack = var0;
      var0 = totalEvents++;
      didSetNewWallpapper = var0;
      var0 = totalEvents++;
      proxySettingsChanged = var0;
      var0 = totalEvents++;
      proxyCheckDone = var0;
      var0 = totalEvents++;
      liveLocationsChanged = var0;
      var0 = totalEvents++;
      liveLocationsCacheChanged = var0;
      var0 = totalEvents++;
      notificationsCountUpdated = var0;
      var0 = totalEvents++;
      playerDidStartPlaying = var0;
      var0 = totalEvents++;
      closeSearchByActiveAction = var0;
      Instance = new NotificationCenter[3];
   }

   public NotificationCenter(int var1) {
      this.currentAccount = var1;
   }

   public static NotificationCenter getGlobalInstance() {
      NotificationCenter var0 = globalInstance;
      NotificationCenter var1 = var0;
      if (var0 == null) {
         synchronized(NotificationCenter.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = globalInstance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new NotificationCenter(-1);
                  globalInstance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public static NotificationCenter getInstance(int var0) {
      NotificationCenter var1 = Instance[var0];
      NotificationCenter var2 = var1;
      if (var1 == null) {
         synchronized(NotificationCenter.class){}

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
               NotificationCenter[] var23;
               try {
                  var23 = Instance;
                  var2 = new NotificationCenter(var0);
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

   public void addObserver(Object var1, int var2) {
      if (BuildVars.DEBUG_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
         throw new RuntimeException("addObserver allowed only from MAIN thread");
      } else {
         ArrayList var3;
         ArrayList var4;
         if (this.broadcasting != 0) {
            var3 = (ArrayList)this.addAfterBroadcast.get(var2);
            var4 = var3;
            if (var3 == null) {
               var4 = new ArrayList();
               this.addAfterBroadcast.put(var2, var4);
            }

            var4.add(var1);
         } else {
            var3 = (ArrayList)this.observers.get(var2);
            var4 = var3;
            if (var3 == null) {
               SparseArray var5 = this.observers;
               var4 = new ArrayList();
               var5.put(var2, var4);
            }

            if (!var4.contains(var1)) {
               var4.add(var1);
            }
         }
      }
   }

   public boolean hasObservers(int var1) {
      boolean var2;
      if (this.observers.indexOfKey(var1) >= 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isAnimationInProgress() {
      return this.animationInProgress;
   }

   public void postNotificationName(int var1, Object... var2) {
      int[] var3 = this.allowedNotifications;
      boolean var4 = false;
      boolean var5 = var4;
      if (var3 != null) {
         int var6 = 0;

         while(true) {
            var3 = this.allowedNotifications;
            var5 = var4;
            if (var6 >= var3.length) {
               break;
            }

            if (var3[var6] == var1) {
               var5 = true;
               break;
            }

            ++var6;
         }
      }

      this.postNotificationNameInternal(var1, var5, var2);
   }

   public void postNotificationNameInternal(int var1, boolean var2, Object... var3) {
      if (BuildVars.DEBUG_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
         throw new RuntimeException("postNotificationName allowed only from MAIN thread");
      } else if (!var2 && this.animationInProgress) {
         NotificationCenter.DelayedPost var8 = new NotificationCenter.DelayedPost(var1, var3);
         this.delayedPosts.add(var8);
         if (BuildVars.LOGS_ENABLED) {
            StringBuilder var9 = new StringBuilder();
            var9.append("delay post notification ");
            var9.append(var1);
            var9.append(" with args count = ");
            var9.append(var3.length);
            FileLog.e(var9.toString());
         }

      } else {
         ++this.broadcasting;
         ArrayList var4 = (ArrayList)this.observers.get(var1);
         int var5;
         if (var4 != null && !var4.isEmpty()) {
            for(var5 = 0; var5 < var4.size(); ++var5) {
               ((NotificationCenter.NotificationCenterDelegate)var4.get(var5)).didReceivedNotification(var1, this.currentAccount, var3);
            }
         }

         --this.broadcasting;
         if (this.broadcasting == 0) {
            int var6;
            ArrayList var7;
            if (this.removeAfterBroadcast.size() != 0) {
               var1 = 0;

               while(true) {
                  if (var1 >= this.removeAfterBroadcast.size()) {
                     this.removeAfterBroadcast.clear();
                     break;
                  }

                  var6 = this.removeAfterBroadcast.keyAt(var1);
                  var7 = (ArrayList)this.removeAfterBroadcast.get(var6);

                  for(var5 = 0; var5 < var7.size(); ++var5) {
                     this.removeObserver(var7.get(var5), var6);
                  }

                  ++var1;
               }
            }

            if (this.addAfterBroadcast.size() != 0) {
               for(var1 = 0; var1 < this.addAfterBroadcast.size(); ++var1) {
                  var6 = this.addAfterBroadcast.keyAt(var1);
                  var7 = (ArrayList)this.addAfterBroadcast.get(var6);

                  for(var5 = 0; var5 < var7.size(); ++var5) {
                     this.addObserver(var7.get(var5), var6);
                  }
               }

               this.addAfterBroadcast.clear();
            }
         }

      }
   }

   public void removeObserver(Object var1, int var2) {
      if (BuildVars.DEBUG_VERSION && Thread.currentThread() != ApplicationLoader.applicationHandler.getLooper().getThread()) {
         throw new RuntimeException("removeObserver allowed only from MAIN thread");
      } else {
         ArrayList var4;
         if (this.broadcasting != 0) {
            ArrayList var3 = (ArrayList)this.removeAfterBroadcast.get(var2);
            var4 = var3;
            if (var3 == null) {
               var4 = new ArrayList();
               this.removeAfterBroadcast.put(var2, var4);
            }

            var4.add(var1);
         } else {
            var4 = (ArrayList)this.observers.get(var2);
            if (var4 != null) {
               var4.remove(var1);
            }

         }
      }
   }

   public void setAllowedNotificationsDutingAnimation(int[] var1) {
      this.allowedNotifications = var1;
   }

   public void setAnimationInProgress(boolean var1) {
      this.animationInProgress = var1;
      if (!this.animationInProgress && !this.delayedPosts.isEmpty()) {
         for(int var2 = 0; var2 < this.delayedPosts.size(); ++var2) {
            NotificationCenter.DelayedPost var3 = (NotificationCenter.DelayedPost)this.delayedPosts.get(var2);
            this.postNotificationNameInternal(var3.id, true, var3.args);
         }

         this.delayedPosts.clear();
      }

   }

   private class DelayedPost {
      private Object[] args;
      private int id;

      private DelayedPost(int var2, Object[] var3) {
         this.id = var2;
         this.args = var3;
      }

      // $FF: synthetic method
      DelayedPost(int var2, Object[] var3, Object var4) {
         this(var2, var3);
      }
   }

   public interface NotificationCenterDelegate {
      void didReceivedNotification(int var1, int var2, Object... var3);
   }
}
