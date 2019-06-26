package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class DownloadController implements NotificationCenter.NotificationCenterDelegate {
   public static final int AUTODOWNLOAD_TYPE_AUDIO = 2;
   public static final int AUTODOWNLOAD_TYPE_DOCUMENT = 8;
   public static final int AUTODOWNLOAD_TYPE_PHOTO = 1;
   public static final int AUTODOWNLOAD_TYPE_VIDEO = 4;
   private static volatile DownloadController[] Instance = new DownloadController[3];
   public static final int PRESET_NUM_CHANNEL = 3;
   public static final int PRESET_NUM_CONTACT = 0;
   public static final int PRESET_NUM_GROUP = 2;
   public static final int PRESET_NUM_PM = 1;
   public static final int PRESET_SIZE_NUM_AUDIO = 3;
   public static final int PRESET_SIZE_NUM_DOCUMENT = 2;
   public static final int PRESET_SIZE_NUM_PHOTO = 0;
   public static final int PRESET_SIZE_NUM_VIDEO = 1;
   private HashMap addLaterArray = new HashMap();
   private ArrayList audioDownloadQueue = new ArrayList();
   private int currentAccount;
   public int currentMobilePreset;
   public int currentRoamingPreset;
   public int currentWifiPreset;
   private ArrayList deleteLaterArray = new ArrayList();
   private ArrayList documentDownloadQueue = new ArrayList();
   private HashMap downloadQueueKeys = new HashMap();
   public DownloadController.Preset highPreset;
   private int lastCheckMask = 0;
   private int lastTag = 0;
   private boolean listenerInProgress = false;
   private boolean loadingAutoDownloadConfig;
   private HashMap loadingFileMessagesObservers = new HashMap();
   private HashMap loadingFileObservers = new HashMap();
   public DownloadController.Preset lowPreset;
   public DownloadController.Preset mediumPreset;
   public DownloadController.Preset mobilePreset;
   private SparseArray observersByTag = new SparseArray();
   private ArrayList photoDownloadQueue = new ArrayList();
   public DownloadController.Preset roamingPreset;
   private LongSparseArray typingTimes = new LongSparseArray();
   private ArrayList videoDownloadQueue = new ArrayList();
   public DownloadController.Preset wifiPreset;

   public DownloadController(int var1) {
      this.currentAccount = var1;
      SharedPreferences var2 = MessagesController.getMainSettings(this.currentAccount);
      this.lowPreset = new DownloadController.Preset(var2.getString("preset0", "1_1_1_1_1048576_512000_512000_524288_0_0_1_1"));
      this.mediumPreset = new DownloadController.Preset(var2.getString("preset1", "13_13_13_13_1048576_10485760_1048576_524288_1_1_1_0"));
      this.highPreset = new DownloadController.Preset(var2.getString("preset2", "13_13_13_13_1048576_15728640_3145728_524288_1_1_1_0"));
      boolean var13;
      if (!var2.contains("newConfig") && UserConfig.getInstance(this.currentAccount).isClientActivated()) {
         var13 = false;
      } else {
         var13 = true;
      }

      String var3 = "currentWifiPreset";
      if (var13) {
         this.mobilePreset = new DownloadController.Preset(var2.getString("mobilePreset", this.mediumPreset.toString()));
         this.wifiPreset = new DownloadController.Preset(var2.getString("wifiPreset", this.highPreset.toString()));
         this.roamingPreset = new DownloadController.Preset(var2.getString("roamingPreset", this.lowPreset.toString()));
         this.currentMobilePreset = var2.getInt("currentMobilePreset", 3);
         this.currentWifiPreset = var2.getInt("currentWifiPreset", 3);
         this.currentRoamingPreset = var2.getInt("currentRoamingPreset", 3);
         if (!var13) {
            var2.edit().putBoolean("newConfig", true).commit();
         }
      } else {
         int[] var4 = new int[4];
         int[] var5 = new int[4];
         int[] var6 = new int[4];
         int[] var7 = new int[7];
         int[] var8 = new int[7];
         int[] var9 = new int[7];

         for(var1 = 0; var1 < 4; ++var1) {
            StringBuilder var10 = new StringBuilder();
            var10.append("mobileDataDownloadMask");
            Object var11 = "";
            if (var1 != 0) {
               var11 = var1;
            }

            var10.append(var11);
            String var15 = var10.toString();
            if (var1 != 0 && !var2.contains(var15)) {
               var4[var1] = var4[0];
               var5[var1] = var5[0];
               var6[var1] = var6[0];
            } else {
               var4[var1] = var2.getInt(var15, 13);
               var10 = new StringBuilder();
               var10.append("wifiDownloadMask");
               if (var1 == 0) {
                  var11 = "";
               } else {
                  var11 = var1;
               }

               var10.append(var11);
               var5[var1] = var2.getInt(var10.toString(), 13);
               var10 = new StringBuilder();
               var10.append("roamingDownloadMask");
               if (var1 == 0) {
                  var11 = "";
               } else {
                  var11 = var1;
               }

               var10.append(var11);
               var6[var1] = var2.getInt(var10.toString(), 1);
            }
         }

         var7[2] = var2.getInt("mobileMaxDownloadSize2", this.mediumPreset.sizes[1]);
         var7[3] = var2.getInt("mobileMaxDownloadSize3", this.mediumPreset.sizes[2]);
         var8[2] = var2.getInt("wifiMaxDownloadSize2", this.highPreset.sizes[1]);
         var8[3] = var2.getInt("wifiMaxDownloadSize3", this.highPreset.sizes[2]);
         var9[2] = var2.getInt("roamingMaxDownloadSize2", this.lowPreset.sizes[1]);
         var9[3] = var2.getInt("roamingMaxDownloadSize3", this.lowPreset.sizes[2]);
         boolean var12 = var2.getBoolean("globalAutodownloadEnabled", true);
         this.mobilePreset = new DownloadController.Preset(var4, this.mediumPreset.sizes[0], var7[2], var7[3], true, true, var12, false);
         this.wifiPreset = new DownloadController.Preset(var5, this.highPreset.sizes[0], var8[2], var8[3], true, true, var12, false);
         this.roamingPreset = new DownloadController.Preset(var6, this.lowPreset.sizes[0], var9[2], var9[3], false, false, var12, true);
         Editor var16 = var2.edit();
         var16.putBoolean("newConfig", true);
         var16.putString("mobilePreset", this.mobilePreset.toString());
         var16.putString("wifiPreset", this.wifiPreset.toString());
         var16.putString("roamingPreset", this.roamingPreset.toString());
         this.currentMobilePreset = 3;
         var16.putInt("currentMobilePreset", 3);
         this.currentWifiPreset = 3;
         var16.putInt(var3, 3);
         this.currentRoamingPreset = 3;
         var16.putInt("currentRoamingPreset", 3);
         var16.commit();
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$DownloadController$TvQOK4BckOSg64NROgC4NLSY7xY(this));
      BroadcastReceiver var17 = new BroadcastReceiver() {
         public void onReceive(Context var1, Intent var2) {
            DownloadController.this.checkAutodownloadSettings();
         }
      };
      IntentFilter var14 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
      ApplicationLoader.applicationContext.registerReceiver(var17, var14);
      if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
         this.checkAutodownloadSettings();
      }

   }

   private void checkDownloadFinished(String var1, int var2) {
      DownloadObject var3 = (DownloadObject)this.downloadQueueKeys.get(var1);
      if (var3 != null) {
         this.downloadQueueKeys.remove(var1);
         if (var2 == 0 || var2 == 2) {
            MessagesStorage.getInstance(this.currentAccount).removeFromDownloadQueue(var3.id, var3.type, false);
         }

         var2 = var3.type;
         if (var2 == 1) {
            this.photoDownloadQueue.remove(var3);
            if (this.photoDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(1);
            }
         } else if (var2 == 2) {
            this.audioDownloadQueue.remove(var3);
            if (this.audioDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(2);
            }
         } else if (var2 == 4) {
            this.videoDownloadQueue.remove(var3);
            if (this.videoDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(4);
            }
         } else if (var2 == 8) {
            this.documentDownloadQueue.remove(var3);
            if (this.documentDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(8);
            }
         }
      }

   }

   public static DownloadController getInstance(int var0) {
      DownloadController var1 = Instance[var0];
      DownloadController var2 = var1;
      if (var1 == null) {
         synchronized(DownloadController.class){}

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
               DownloadController[] var23;
               try {
                  var23 = Instance;
                  var2 = new DownloadController(var0);
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

   // $FF: synthetic method
   static void lambda$savePresetToServer$3(TLObject var0, TLRPC.TL_error var1) {
   }

   private void processLaterArrays() {
      Iterator var1 = this.addLaterArray.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         this.addLoadingFileObserver((String)var2.getKey(), (DownloadController.FileDownloadProgressListener)var2.getValue());
      }

      this.addLaterArray.clear();
      Iterator var3 = this.deleteLaterArray.iterator();

      while(var3.hasNext()) {
         this.removeLoadingFileObserver((DownloadController.FileDownloadProgressListener)var3.next());
      }

      this.deleteLaterArray.clear();
   }

   public static int typeToIndex(int var0) {
      if (var0 == 1) {
         return 0;
      } else if (var0 == 2) {
         return 3;
      } else if (var0 == 4) {
         return 1;
      } else {
         return var0 == 8 ? 2 : 0;
      }
   }

   public void addLoadingFileObserver(String var1, DownloadController.FileDownloadProgressListener var2) {
      this.addLoadingFileObserver(var1, (MessageObject)null, var2);
   }

   public void addLoadingFileObserver(String var1, MessageObject var2, DownloadController.FileDownloadProgressListener var3) {
      if (this.listenerInProgress) {
         this.addLaterArray.put(var1, var3);
      } else {
         this.removeLoadingFileObserver(var3);
         ArrayList var4 = (ArrayList)this.loadingFileObservers.get(var1);
         ArrayList var5 = var4;
         if (var4 == null) {
            var5 = new ArrayList();
            this.loadingFileObservers.put(var1, var5);
         }

         var5.add(new WeakReference(var3));
         if (var2 != null) {
            var4 = (ArrayList)this.loadingFileMessagesObservers.get(var1);
            var5 = var4;
            if (var4 == null) {
               var5 = new ArrayList();
               this.loadingFileMessagesObservers.put(var1, var5);
            }

            var5.add(var2);
         }

         this.observersByTag.put(var3.getObserverTag(), var1);
      }
   }

   public int canDownloadMedia(TLRPC.Message var1) {
      byte var2 = 0;
      byte var3 = 0;
      if (var1 == null) {
         return 0;
      } else {
         boolean var4 = MessageObject.isVideoMessage(var1);
         byte var5;
         if (!var4 && !MessageObject.isGifMessage(var1) && !MessageObject.isRoundVideoMessage(var1) && !MessageObject.isGameMessage(var1)) {
            if (MessageObject.isVoiceMessage(var1)) {
               var5 = 2;
            } else if (!MessageObject.isPhoto(var1) && !MessageObject.isStickerMessage(var1)) {
               if (MessageObject.getDocument(var1) == null) {
                  return 0;
               }

               var5 = 8;
            } else {
               var5 = 1;
            }
         } else {
            var5 = 4;
         }

         byte var7;
         label131: {
            TLRPC.Peer var6 = var1.to_id;
            if (var6 != null) {
               label132: {
                  if (var6.user_id != 0) {
                     if (!ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(var6.user_id)) {
                        break label132;
                     }
                  } else {
                     label130: {
                        if (var6.chat_id != 0) {
                           if (var1.from_id != 0 && ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(var1.from_id)) {
                              break label130;
                           }
                        } else {
                           if (!MessageObject.isMegagroup(var1)) {
                              var7 = 3;
                              break label131;
                           }

                           if (var1.from_id != 0 && ContactsController.getInstance(this.currentAccount).contactsDict.containsKey(var1.from_id)) {
                              break label130;
                           }
                        }

                        var7 = 2;
                        break label131;
                     }
                  }

                  var7 = 0;
                  break label131;
               }
            }

            var7 = 1;
         }

         DownloadController.Preset var11;
         if (ApplicationLoader.isConnectedToWiFi()) {
            if (!this.wifiPreset.enabled) {
               return 0;
            }

            var11 = this.getCurrentWiFiPreset();
         } else if (ApplicationLoader.isRoaming()) {
            if (!this.roamingPreset.enabled) {
               return 0;
            }

            var11 = this.getCurrentRoamingPreset();
         } else {
            if (!this.mobilePreset.enabled) {
               return 0;
            }

            var11 = this.getCurrentMobilePreset();
         }

         int var8 = var11.mask[var7];
         int var9 = var11.sizes[typeToIndex(var5)];
         int var10 = MessageObject.getMessageSize(var1);
         if (var4 && var11.preloadVideo && var10 > var9 && var9 > 2097152) {
            var7 = var3;
            if ((var8 & var5) != 0) {
               var7 = 2;
            }

            return var7;
         } else {
            if (var5 != 1) {
               var7 = var2;
               if (var10 == 0) {
                  return var7;
               }

               var7 = var2;
               if (var10 > var9) {
                  return var7;
               }
            }

            if (var5 != 2) {
               var7 = var2;
               if ((var8 & var5) == 0) {
                  return var7;
               }
            }

            var7 = 1;
            return var7;
         }
      }
   }

   public boolean canDownloadMedia(int var1, int var2) {
      boolean var3 = ApplicationLoader.isConnectedToWiFi();
      boolean var4 = false;
      DownloadController.Preset var5;
      if (var3) {
         if (!this.wifiPreset.enabled) {
            return false;
         }

         var5 = this.getCurrentWiFiPreset();
      } else if (ApplicationLoader.isRoaming()) {
         if (!this.roamingPreset.enabled) {
            return false;
         }

         var5 = this.getCurrentRoamingPreset();
      } else {
         if (!this.mobilePreset.enabled) {
            return false;
         }

         var5 = this.getCurrentMobilePreset();
      }

      int var6 = var5.mask[1];
      int var7 = var5.sizes[typeToIndex(var1)];
      if (var1 != 1) {
         var3 = var4;
         if (var2 == 0) {
            return var3;
         }

         var3 = var4;
         if (var2 > var7) {
            return var3;
         }
      }

      if (var1 != 2) {
         var3 = var4;
         if ((var1 & var6) == 0) {
            return var3;
         }
      }

      var3 = true;
      return var3;
   }

   public boolean canDownloadMedia(MessageObject var1) {
      int var2 = this.canDownloadMedia(var1.messageOwner);
      boolean var3 = true;
      if (var2 != 1) {
         var3 = false;
      }

      return var3;
   }

   protected boolean canDownloadNextTrack() {
      boolean var1 = ApplicationLoader.isConnectedToWiFi();
      boolean var2 = true;
      boolean var3 = true;
      boolean var4 = true;
      if (var1) {
         if (!this.wifiPreset.enabled || !this.getCurrentWiFiPreset().preloadMusic) {
            var4 = false;
         }

         return var4;
      } else if (ApplicationLoader.isRoaming()) {
         if (this.roamingPreset.enabled && this.getCurrentRoamingPreset().preloadMusic) {
            var4 = var2;
         } else {
            var4 = false;
         }

         return var4;
      } else {
         if (this.mobilePreset.enabled && this.getCurrentMobilePreset().preloadMusic) {
            var4 = var3;
         } else {
            var4 = false;
         }

         return var4;
      }
   }

   public void checkAutodownloadSettings() {
      int var1 = this.getCurrentDownloadMask();
      if (var1 != this.lastCheckMask) {
         this.lastCheckMask = var1;
         int var2;
         DownloadObject var3;
         if ((var1 & 1) != 0) {
            if (this.photoDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(1);
            }
         } else {
            for(var2 = 0; var2 < this.photoDownloadQueue.size(); ++var2) {
               var3 = (DownloadObject)this.photoDownloadQueue.get(var2);
               TLObject var4 = var3.object;
               if (var4 instanceof TLRPC.Photo) {
                  TLRPC.PhotoSize var5 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)var4).sizes, AndroidUtilities.getPhotoSize());
                  FileLoader.getInstance(this.currentAccount).cancelLoadFile(var5);
               } else if (var4 instanceof TLRPC.Document) {
                  FileLoader.getInstance(this.currentAccount).cancelLoadFile((TLRPC.Document)var3.object);
               }
            }

            this.photoDownloadQueue.clear();
         }

         if ((var1 & 2) != 0) {
            if (this.audioDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(2);
            }
         } else {
            for(var2 = 0; var2 < this.audioDownloadQueue.size(); ++var2) {
               var3 = (DownloadObject)this.audioDownloadQueue.get(var2);
               FileLoader.getInstance(this.currentAccount).cancelLoadFile((TLRPC.Document)var3.object);
            }

            this.audioDownloadQueue.clear();
         }

         if ((var1 & 8) != 0) {
            if (this.documentDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(8);
            }
         } else {
            for(var2 = 0; var2 < this.documentDownloadQueue.size(); ++var2) {
               TLRPC.Document var6 = (TLRPC.Document)((DownloadObject)this.documentDownloadQueue.get(var2)).object;
               FileLoader.getInstance(this.currentAccount).cancelLoadFile(var6);
            }

            this.documentDownloadQueue.clear();
         }

         if ((var1 & 4) != 0) {
            if (this.videoDownloadQueue.isEmpty()) {
               this.newDownloadObjectsAvailable(4);
            }
         } else {
            for(var2 = 0; var2 < this.videoDownloadQueue.size(); ++var2) {
               var3 = (DownloadObject)this.videoDownloadQueue.get(var2);
               FileLoader.getInstance(this.currentAccount).cancelLoadFile((TLRPC.Document)var3.object);
            }

            this.videoDownloadQueue.clear();
         }

         var2 = this.getAutodownloadMaskAll();
         if (var2 == 0) {
            MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(0);
         } else {
            if ((var2 & 1) == 0) {
               MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(1);
            }

            if ((var2 & 2) == 0) {
               MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(2);
            }

            if ((var2 & 4) == 0) {
               MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(4);
            }

            if ((var2 & 8) == 0) {
               MessagesStorage.getInstance(this.currentAccount).clearDownloadQueue(8);
            }
         }

      }
   }

   public void cleanup() {
      this.photoDownloadQueue.clear();
      this.audioDownloadQueue.clear();
      this.documentDownloadQueue.clear();
      this.videoDownloadQueue.clear();
      this.downloadQueueKeys.clear();
      this.typingTimes.clear();
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      String var4;
      ArrayList var5;
      if (var1 != NotificationCenter.fileDidFailedLoad && var1 != NotificationCenter.httpFileDidFailedLoad) {
         if (var1 != NotificationCenter.fileDidLoad && var1 != NotificationCenter.httpFileDidLoad) {
            if (var1 == NotificationCenter.FileLoadProgressChanged) {
               this.listenerInProgress = true;
               var4 = (String)var3[0];
               var5 = (ArrayList)this.loadingFileObservers.get(var4);
               if (var5 != null) {
                  Float var28 = (Float)var3[1];
                  var2 = var5.size();

                  for(var1 = 0; var1 < var2; ++var1) {
                     WeakReference var35 = (WeakReference)var5.get(var1);
                     if (var35.get() != null) {
                        ((DownloadController.FileDownloadProgressListener)var35.get()).onProgressDownload(var4, var28);
                     }
                  }
               }

               this.listenerInProgress = false;
               this.processLaterArrays();
            } else if (var1 == NotificationCenter.FileUploadProgressChanged) {
               this.listenerInProgress = true;
               var4 = (String)var3[0];
               ArrayList var37 = (ArrayList)this.loadingFileObservers.get(var4);
               if (var37 != null) {
                  Float var34 = (Float)var3[1];
                  Boolean var41 = (Boolean)var3[2];
                  var2 = var37.size();

                  for(var1 = 0; var1 < var2; ++var1) {
                     WeakReference var30 = (WeakReference)var37.get(var1);
                     if (var30.get() != null) {
                        ((DownloadController.FileDownloadProgressListener)var30.get()).onProgressUpload(var4, var34, var41);
                     }
                  }
               }

               this.listenerInProgress = false;
               this.processLaterArrays();

               Exception var10000;
               label280: {
                  boolean var10001;
                  ArrayList var31;
                  try {
                     var31 = SendMessagesHelper.getInstance(this.currentAccount).getDelayedMessages(var4);
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label280;
                  }

                  if (var31 == null) {
                     return;
                  }

                  var1 = 0;

                  while(true) {
                     label291: {
                        long var8;
                        SendMessagesHelper.DelayedMessage var36;
                        Long var39;
                        label292: {
                           try {
                              if (var1 >= var31.size()) {
                                 return;
                              }

                              var36 = (SendMessagesHelper.DelayedMessage)var31.get(var1);
                              if (var36.encryptedChat != null) {
                                 break label291;
                              }

                              var8 = var36.peer;
                              if (var36.type == 4) {
                                 var39 = (Long)this.typingTimes.get(var8);
                                 break label292;
                              }
                           } catch (Exception var24) {
                              var10000 = var24;
                              var10001 = false;
                              break;
                           }

                           try {
                              var39 = (Long)this.typingTimes.get(var8);
                              var36.obj.getDocument();
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                              break;
                           }

                           if (var39 != null) {
                              try {
                                 if (var39 + 4000L >= System.currentTimeMillis()) {
                                    break label291;
                                 }
                              } catch (Exception var22) {
                                 var10000 = var22;
                                 var10001 = false;
                                 break;
                              }
                           }

                           label306: {
                              try {
                                 if (var36.obj.isRoundVideo()) {
                                    MessagesController.getInstance(this.currentAccount).sendTyping(var8, 8, 0);
                                    break label306;
                                 }
                              } catch (Exception var21) {
                                 var10000 = var21;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 if (var36.obj.isVideo()) {
                                    MessagesController.getInstance(this.currentAccount).sendTyping(var8, 5, 0);
                                    break label306;
                                 }
                              } catch (Exception var20) {
                                 var10000 = var20;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 if (var36.obj.isVoice()) {
                                    MessagesController.getInstance(this.currentAccount).sendTyping(var8, 9, 0);
                                    break label306;
                                 }
                              } catch (Exception var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 if (var36.obj.getDocument() != null) {
                                    MessagesController.getInstance(this.currentAccount).sendTyping(var8, 3, 0);
                                    break label306;
                                 }
                              } catch (Exception var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break;
                              }

                              try {
                                 if (var36.photoSize != null) {
                                    MessagesController.getInstance(this.currentAccount).sendTyping(var8, 4, 0);
                                 }
                              } catch (Exception var12) {
                                 var10000 = var12;
                                 var10001 = false;
                                 break;
                              }
                           }

                           try {
                              this.typingTimes.put(var8, System.currentTimeMillis());
                              break label291;
                           } catch (Exception var11) {
                              var10000 = var11;
                              var10001 = false;
                              break;
                           }
                        }

                        if (var39 != null) {
                           try {
                              if (var39 + 4000L >= System.currentTimeMillis()) {
                                 break label291;
                              }
                           } catch (Exception var23) {
                              var10000 = var23;
                              var10001 = false;
                              break;
                           }
                        }

                        MessageObject var40;
                        try {
                           HashMap var38 = var36.extraHashMap;
                           StringBuilder var42 = new StringBuilder();
                           var42.append(var4);
                           var42.append("_i");
                           var40 = (MessageObject)var38.get(var42.toString());
                        } catch (Exception var16) {
                           var10000 = var16;
                           var10001 = false;
                           break;
                        }

                        label180: {
                           if (var40 != null) {
                              try {
                                 if (var40.isVideo()) {
                                    MessagesController.getInstance(this.currentAccount).sendTyping(var8, 5, 0);
                                    break label180;
                                 }
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break;
                              }
                           }

                           try {
                              MessagesController.getInstance(this.currentAccount).sendTyping(var8, 4, 0);
                           } catch (Exception var15) {
                              var10000 = var15;
                              var10001 = false;
                              break;
                           }
                        }

                        try {
                           this.typingTimes.put(var8, System.currentTimeMillis());
                        } catch (Exception var14) {
                           var10000 = var14;
                           var10001 = false;
                           break;
                        }
                     }

                     ++var1;
                  }
               }

               Exception var32 = var10000;
               FileLog.e((Throwable)var32);
            }
         } else {
            this.listenerInProgress = true;
            String var27 = (String)var3[0];
            ArrayList var29 = (ArrayList)this.loadingFileMessagesObservers.get(var27);
            if (var29 != null) {
               var2 = var29.size();

               for(var1 = 0; var1 < var2; ++var1) {
                  ((MessageObject)var29.get(var1)).mediaExists = true;
               }

               this.loadingFileMessagesObservers.remove(var27);
            }

            var29 = (ArrayList)this.loadingFileObservers.get(var27);
            if (var29 != null) {
               var2 = var29.size();

               for(var1 = 0; var1 < var2; ++var1) {
                  WeakReference var33 = (WeakReference)var29.get(var1);
                  if (var33.get() != null) {
                     ((DownloadController.FileDownloadProgressListener)var33.get()).onSuccessDownload(var27);
                     this.observersByTag.remove(((DownloadController.FileDownloadProgressListener)var33.get()).getObserverTag());
                  }
               }

               this.loadingFileObservers.remove(var27);
            }

            this.listenerInProgress = false;
            this.processLaterArrays();
            this.checkDownloadFinished(var27, 0);
         }
      } else {
         var4 = (String)var3[0];
         Integer var6 = (Integer)var3[1];
         this.listenerInProgress = true;
         var5 = (ArrayList)this.loadingFileObservers.get(var4);
         if (var5 != null) {
            var2 = var5.size();

            for(var1 = 0; var1 < var2; ++var1) {
               WeakReference var7 = (WeakReference)var5.get(var1);
               if (var7.get() != null) {
                  DownloadController.FileDownloadProgressListener var26 = (DownloadController.FileDownloadProgressListener)var7.get();
                  boolean var10;
                  if (var6 == 1) {
                     var10 = true;
                  } else {
                     var10 = false;
                  }

                  var26.onFailedDownload(var4, var10);
                  if (var6 != 1) {
                     this.observersByTag.remove(((DownloadController.FileDownloadProgressListener)var7.get()).getObserverTag());
                  }
               }
            }

            if (var6 != 1) {
               this.loadingFileObservers.remove(var4);
            }
         }

         this.listenerInProgress = false;
         this.processLaterArrays();
         this.checkDownloadFinished(var4, var6);
      }

   }

   public int generateObserverTag() {
      int var1 = this.lastTag++;
      return var1;
   }

   public int getAutodownloadMask() {
      int[] var1;
      if (ApplicationLoader.isConnectedToWiFi()) {
         if (!this.wifiPreset.enabled) {
            return 0;
         }

         var1 = this.getCurrentWiFiPreset().mask;
      } else if (ApplicationLoader.isRoaming()) {
         if (!this.roamingPreset.enabled) {
            return 0;
         }

         var1 = this.getCurrentRoamingPreset().mask;
      } else {
         if (!this.mobilePreset.enabled) {
            return 0;
         }

         var1 = this.getCurrentMobilePreset().mask;
      }

      int var2 = 0;

      int var3;
      for(var3 = 0; var2 < var1.length; ++var2) {
         int var4 = var1[var2];
         byte var5 = 1;
         if ((var4 & 1) == 0) {
            var5 = 0;
         }

         var4 = var5;
         if ((var1[var2] & 2) != 0) {
            var4 = var5 | 2;
         }

         int var6 = var4;
         if ((var1[var2] & 4) != 0) {
            var6 = var4 | 4;
         }

         var4 = var6;
         if ((var1[var2] & 8) != 0) {
            var4 = var6 | 8;
         }

         var3 |= var4 << var2 * 8;
      }

      return var3;
   }

   protected int getAutodownloadMaskAll() {
      boolean var1 = this.mobilePreset.enabled;
      int var2 = 0;
      if (!var1 && !this.roamingPreset.enabled && !this.wifiPreset.enabled) {
         return 0;
      } else {
         int var3;
         for(var3 = 0; var2 < 4; ++var2) {
            int var4;
            label37: {
               if ((this.getCurrentMobilePreset().mask[var2] & 1) == 0 && (this.getCurrentWiFiPreset().mask[var2] & 1) == 0) {
                  var4 = var3;
                  if ((this.getCurrentRoamingPreset().mask[var2] & 1) == 0) {
                     break label37;
                  }
               }

               var4 = var3 | 1;
            }

            label43: {
               if ((this.getCurrentMobilePreset().mask[var2] & 2) == 0 && (this.getCurrentWiFiPreset().mask[var2] & 2) == 0) {
                  var3 = var4;
                  if ((this.getCurrentRoamingPreset().mask[var2] & 2) == 0) {
                     break label43;
                  }
               }

               var3 = var4 | 2;
            }

            label49: {
               if ((this.getCurrentMobilePreset().mask[var2] & 4) == 0 && (this.getCurrentWiFiPreset().mask[var2] & 4) == 0) {
                  var4 = var3;
                  if ((4 & this.getCurrentRoamingPreset().mask[var2]) == 0) {
                     break label49;
                  }
               }

               var4 = var3 | 4;
            }

            if ((this.getCurrentMobilePreset().mask[var2] & 8) == 0 && (this.getCurrentWiFiPreset().mask[var2] & 8) == 0) {
               var3 = var4;
               if ((this.getCurrentRoamingPreset().mask[var2] & 8) == 0) {
                  continue;
               }
            }

            var3 = var4 | 8;
         }

         return var3;
      }
   }

   public int getCurrentDownloadMask() {
      boolean var1 = ApplicationLoader.isConnectedToWiFi();
      byte var2 = 0;
      byte var3 = 0;
      int var4 = 0;
      int var5;
      if (var1) {
         if (!this.wifiPreset.enabled) {
            return 0;
         } else {
            for(var5 = 0; var4 < 4; ++var4) {
               var5 |= this.getCurrentWiFiPreset().mask[var4];
            }

            return var5;
         }
      } else if (ApplicationLoader.isRoaming()) {
         if (!this.roamingPreset.enabled) {
            return 0;
         } else {
            var3 = 0;
            var4 = var2;

            for(var5 = var3; var4 < 4; ++var4) {
               var5 |= this.getCurrentRoamingPreset().mask[var4];
            }

            return var5;
         }
      } else if (!this.mobilePreset.enabled) {
         return 0;
      } else {
         var5 = 0;

         for(var4 = var3; var4 < 4; ++var4) {
            var5 |= this.getCurrentMobilePreset().mask[var4];
         }

         return var5;
      }
   }

   public DownloadController.Preset getCurrentMobilePreset() {
      int var1 = this.currentMobilePreset;
      if (var1 == 0) {
         return this.lowPreset;
      } else if (var1 == 1) {
         return this.mediumPreset;
      } else {
         return var1 == 2 ? this.highPreset : this.mobilePreset;
      }
   }

   public DownloadController.Preset getCurrentRoamingPreset() {
      int var1 = this.currentRoamingPreset;
      if (var1 == 0) {
         return this.lowPreset;
      } else if (var1 == 1) {
         return this.mediumPreset;
      } else {
         return var1 == 2 ? this.highPreset : this.roamingPreset;
      }
   }

   public DownloadController.Preset getCurrentWiFiPreset() {
      int var1 = this.currentWifiPreset;
      if (var1 == 0) {
         return this.lowPreset;
      } else if (var1 == 1) {
         return this.mediumPreset;
      } else {
         return var1 == 2 ? this.highPreset : this.wifiPreset;
      }
   }

   // $FF: synthetic method
   public void lambda$loadAutoDownloadConfig$2$DownloadController(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$DownloadController$Sppih_WlM90EoDE67qxYSN9jq_E(this, var1));
   }

   // $FF: synthetic method
   public void lambda$new$0$DownloadController() {
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidFailedLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileLoadProgressChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileUploadProgressChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.httpFileDidFailedLoad);
      this.loadAutoDownloadConfig(false);
   }

   // $FF: synthetic method
   public void lambda$null$1$DownloadController(TLObject var1) {
      int var2 = 0;
      this.loadingAutoDownloadConfig = false;
      UserConfig.getInstance(this.currentAccount).autoDownloadConfigLoadTime = System.currentTimeMillis();
      UserConfig.getInstance(this.currentAccount).saveConfig(false);
      if (var1 != null) {
         TLRPC.TL_account_autoDownloadSettings var3 = (TLRPC.TL_account_autoDownloadSettings)var1;
         this.lowPreset.set(var3.low);
         this.mediumPreset.set(var3.medium);
         this.highPreset.set(var3.high);

         for(; var2 < 3; ++var2) {
            DownloadController.Preset var4;
            if (var2 == 0) {
               var4 = this.mobilePreset;
            } else if (var2 == 1) {
               var4 = this.wifiPreset;
            } else {
               var4 = this.roamingPreset;
            }

            if (var4.equals(this.lowPreset)) {
               var4.set(var3.low);
            } else if (var4.equals(this.mediumPreset)) {
               var4.set(var3.medium);
            } else if (var4.equals(this.highPreset)) {
               var4.set(var3.high);
            }
         }

         Editor var5 = MessagesController.getMainSettings(this.currentAccount).edit();
         var5.putString("mobilePreset", this.mobilePreset.toString());
         var5.putString("wifiPreset", this.wifiPreset.toString());
         var5.putString("roamingPreset", this.roamingPreset.toString());
         var5.putString("preset0", this.lowPreset.toString());
         var5.putString("preset1", this.mediumPreset.toString());
         var5.putString("preset2", this.highPreset.toString());
         var5.commit();
         this.lowPreset.toString();
         this.mediumPreset.toString();
         this.highPreset.toString();
         this.checkAutodownloadSettings();
      }

   }

   public void loadAutoDownloadConfig(boolean var1) {
      if (!this.loadingAutoDownloadConfig && (var1 || Math.abs(System.currentTimeMillis() - UserConfig.getInstance(this.currentAccount).autoDownloadConfigLoadTime) >= 86400000L)) {
         this.loadingAutoDownloadConfig = true;
         TLRPC.TL_account_getAutoDownloadSettings var2 = new TLRPC.TL_account_getAutoDownloadSettings();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, new _$$Lambda$DownloadController$Vy_RFVunDaT6j2u2tHT0TGLKrLk(this));
      }

   }

   protected void newDownloadObjectsAvailable(int var1) {
      int var2 = this.getCurrentDownloadMask();
      if ((var2 & 1) != 0 && (var1 & 1) != 0 && this.photoDownloadQueue.isEmpty()) {
         MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(1);
      }

      if ((var2 & 2) != 0 && (var1 & 2) != 0 && this.audioDownloadQueue.isEmpty()) {
         MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(2);
      }

      if ((var2 & 4) != 0 && (var1 & 4) != 0 && this.videoDownloadQueue.isEmpty()) {
         MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(4);
      }

      if ((var2 & 8) != 0 && (var1 & 8) != 0 && this.documentDownloadQueue.isEmpty()) {
         MessagesStorage.getInstance(this.currentAccount).getDownloadQueue(8);
      }

   }

   protected void processDownloadObjects(int var1, ArrayList var2) {
      if (!var2.isEmpty()) {
         ArrayList var3;
         if (var1 == 1) {
            var3 = this.photoDownloadQueue;
         } else if (var1 == 2) {
            var3 = this.audioDownloadQueue;
         } else if (var1 == 4) {
            var3 = this.videoDownloadQueue;
         } else if (var1 == 8) {
            var3 = this.documentDownloadQueue;
         } else {
            var3 = null;
         }

         for(int var4 = 0; var4 < var2.size(); ++var4) {
            DownloadObject var5 = (DownloadObject)var2.get(var4);
            TLObject var6 = var5.object;
            Object var7;
            String var12;
            if (var6 instanceof TLRPC.Document) {
               var12 = FileLoader.getAttachFileName((TLRPC.Document)var6);
               var7 = null;
            } else if (var6 instanceof TLRPC.Photo) {
               var12 = FileLoader.getAttachFileName(var6);
               var7 = FileLoader.getClosestPhotoSizeWithSize(((TLRPC.Photo)var5.object).sizes, AndroidUtilities.getPhotoSize());
            } else {
               var12 = null;
               var7 = var12;
            }

            if (var12 != null && !this.downloadQueueKeys.containsKey(var12)) {
               boolean var11;
               label69: {
                  byte var10;
                  if (var7 != null) {
                     TLRPC.Photo var8 = (TLRPC.Photo)var5.object;
                     if (var5.secret) {
                        var10 = 2;
                     } else if (var5.forceCache) {
                        var10 = 1;
                     } else {
                        var10 = 0;
                     }

                     FileLoader.getInstance(this.currentAccount).loadFile(ImageLocation.getForPhoto((TLRPC.PhotoSize)var7, var8), var5.parent, (String)null, 0, var10);
                  } else {
                     TLObject var13 = var5.object;
                     if (!(var13 instanceof TLRPC.Document)) {
                        var11 = false;
                        break label69;
                     }

                     TLRPC.Document var15 = (TLRPC.Document)var13;
                     FileLoader var9 = FileLoader.getInstance(this.currentAccount);
                     String var14 = var5.parent;
                     if (var5.secret) {
                        var10 = 2;
                     } else {
                        var10 = 0;
                     }

                     var9.loadFile(var15, var14, 0, var10);
                  }

                  var11 = true;
               }

               if (var11) {
                  var3.add(var5);
                  this.downloadQueueKeys.put(var12, var5);
               }
            }
         }

      }
   }

   public void removeLoadingFileObserver(DownloadController.FileDownloadProgressListener var1) {
      if (this.listenerInProgress) {
         this.deleteLaterArray.add(var1);
      } else {
         String var2 = (String)this.observersByTag.get(var1.getObserverTag());
         if (var2 != null) {
            ArrayList var3 = (ArrayList)this.loadingFileObservers.get(var2);
            if (var3 != null) {
               int var4 = 0;

               while(true) {
                  if (var4 >= var3.size()) {
                     if (var3.isEmpty()) {
                        this.loadingFileObservers.remove(var2);
                     }
                     break;
                  }

                  int var6;
                  label27: {
                     WeakReference var5 = (WeakReference)var3.get(var4);
                     if (var5.get() != null) {
                        var6 = var4;
                        if (var5.get() != var1) {
                           break label27;
                        }
                     }

                     var3.remove(var4);
                     var6 = var4 - 1;
                  }

                  var4 = var6 + 1;
               }
            }

            this.observersByTag.remove(var1.getObserverTag());
         }

      }
   }

   public void savePresetToServer(int var1) {
      TLRPC.TL_account_saveAutoDownloadSettings var2 = new TLRPC.TL_account_saveAutoDownloadSettings();
      DownloadController.Preset var3;
      boolean var4;
      if (var1 == 0) {
         var3 = this.getCurrentMobilePreset();
         var4 = this.mobilePreset.enabled;
      } else if (var1 == 1) {
         var3 = this.getCurrentWiFiPreset();
         var4 = this.wifiPreset.enabled;
      } else {
         var3 = this.getCurrentRoamingPreset();
         var4 = this.roamingPreset.enabled;
      }

      var2.settings = new TLRPC.TL_autoDownloadSettings();
      TLRPC.TL_autoDownloadSettings var5 = var2.settings;
      var5.audio_preload_next = var3.preloadMusic;
      var5.video_preload_large = var3.preloadVideo;
      var5.phonecalls_less_data = var3.lessCallData;
      byte var6 = 0;
      if (!var4) {
         var4 = true;
      } else {
         var4 = false;
      }

      var5.disabled = var4;
      int var7 = 0;
      boolean var8 = false;
      boolean var9 = false;
      boolean var13 = false;

      boolean var10;
      boolean var11;
      boolean var12;
      while(true) {
         int[] var14 = var3.mask;
         var10 = var8;
         var11 = var9;
         var12 = var13;
         if (var7 >= var14.length) {
            break;
         }

         if ((var14[var7] & 1) != 0) {
            var8 = true;
         }

         if ((var3.mask[var7] & 4) != 0) {
            var9 = true;
         }

         if ((var3.mask[var7] & 8) != 0) {
            var13 = true;
         }

         if (var8 && var9 && var13) {
            var10 = var8;
            var11 = var9;
            var12 = var13;
            break;
         }

         ++var7;
      }

      var5 = var2.settings;
      if (var10) {
         var1 = var3.sizes[0];
      } else {
         var1 = 0;
      }

      var5.photo_size_max = var1;
      var5 = var2.settings;
      if (var11) {
         var1 = var3.sizes[1];
      } else {
         var1 = 0;
      }

      var5.video_size_max = var1;
      var5 = var2.settings;
      var1 = var6;
      if (var12) {
         var1 = var3.sizes[2];
      }

      var5.file_size_max = var1;
      ConnectionsManager.getInstance(this.currentAccount).sendRequest(var2, _$$Lambda$DownloadController$0LtKveHOl8NLZKx_EDiX80oSJa0.INSTANCE);
   }

   public interface FileDownloadProgressListener {
      int getObserverTag();

      void onFailedDownload(String var1, boolean var2);

      void onProgressDownload(String var1, float var2);

      void onProgressUpload(String var1, float var2, boolean var3);

      void onSuccessDownload(String var1);
   }

   public static class Preset {
      public boolean enabled;
      public boolean lessCallData;
      public int[] mask = new int[4];
      public boolean preloadMusic;
      public boolean preloadVideo;
      public int[] sizes = new int[4];

      public Preset(String var1) {
         String[] var5 = var1.split("_");
         if (var5.length >= 11) {
            int[] var2 = this.mask;
            boolean var3 = false;
            var2[0] = Utilities.parseInt(var5[0]);
            this.mask[1] = Utilities.parseInt(var5[1]);
            this.mask[2] = Utilities.parseInt(var5[2]);
            this.mask[3] = Utilities.parseInt(var5[3]);
            this.sizes[0] = Utilities.parseInt(var5[4]);
            this.sizes[1] = Utilities.parseInt(var5[5]);
            this.sizes[2] = Utilities.parseInt(var5[6]);
            this.sizes[3] = Utilities.parseInt(var5[7]);
            boolean var4;
            if (Utilities.parseInt(var5[8]) == 1) {
               var4 = true;
            } else {
               var4 = false;
            }

            this.preloadVideo = var4;
            if (Utilities.parseInt(var5[9]) == 1) {
               var4 = true;
            } else {
               var4 = false;
            }

            this.preloadMusic = var4;
            if (Utilities.parseInt(var5[10]) == 1) {
               var4 = true;
            } else {
               var4 = false;
            }

            this.enabled = var4;
            if (var5.length >= 12) {
               var4 = var3;
               if (Utilities.parseInt(var5[11]) == 1) {
                  var4 = true;
               }

               this.lessCallData = var4;
            }
         }

      }

      public Preset(int[] var1, int var2, int var3, int var4, boolean var5, boolean var6, boolean var7, boolean var8) {
         int[] var9 = this.mask;
         System.arraycopy(var1, 0, var9, 0, var9.length);
         var1 = this.sizes;
         var1[0] = var2;
         var1[1] = var3;
         var1[2] = var4;
         var1[3] = 524288;
         this.preloadVideo = var5;
         this.preloadMusic = var6;
         this.lessCallData = var8;
         this.enabled = var7;
      }

      public boolean equals(DownloadController.Preset var1) {
         int[] var2 = this.mask;
         boolean var3 = false;
         int var4 = var2[0];
         int[] var5 = var1.mask;
         boolean var6 = var3;
         if (var4 == var5[0]) {
            var6 = var3;
            if (var2[1] == var5[1]) {
               var6 = var3;
               if (var2[2] == var5[2]) {
                  var6 = var3;
                  if (var2[3] == var5[3]) {
                     var2 = this.sizes;
                     var4 = var2[0];
                     var5 = var1.sizes;
                     var6 = var3;
                     if (var4 == var5[0]) {
                        var6 = var3;
                        if (var2[1] == var5[1]) {
                           var6 = var3;
                           if (var2[2] == var5[2]) {
                              var6 = var3;
                              if (var2[3] == var5[3]) {
                                 var6 = var3;
                                 if (this.preloadVideo == var1.preloadVideo) {
                                    var6 = var3;
                                    if (this.preloadMusic == var1.preloadMusic) {
                                       var6 = true;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return var6;
      }

      public boolean isEnabled() {
         int var1 = 0;

         while(true) {
            int[] var2 = this.mask;
            if (var1 >= var2.length) {
               return false;
            }

            if (var2[var1] != 0) {
               return true;
            }

            ++var1;
         }
      }

      public void set(DownloadController.Preset var1) {
         int[] var2 = var1.mask;
         int[] var3 = this.mask;
         System.arraycopy(var2, 0, var3, 0, var3.length);
         var2 = var1.sizes;
         var3 = this.sizes;
         System.arraycopy(var2, 0, var3, 0, var3.length);
         this.preloadVideo = var1.preloadVideo;
         this.preloadMusic = var1.preloadMusic;
         this.lessCallData = var1.lessCallData;
      }

      public void set(TLRPC.TL_autoDownloadSettings var1) {
         this.preloadMusic = var1.audio_preload_next;
         this.preloadVideo = var1.video_preload_large;
         this.lessCallData = var1.phonecalls_less_data;
         int[] var2 = this.sizes;
         int var3 = Math.max(512000, var1.photo_size_max);
         int var4 = 0;
         var2[0] = var3;
         this.sizes[1] = Math.max(512000, var1.video_size_max);
         this.sizes[2] = Math.max(512000, var1.file_size_max);

         while(true) {
            var2 = this.mask;
            if (var4 >= var2.length) {
               return;
            }

            if (var1.photo_size_max != 0 && !var1.disabled) {
               var2[var4] |= 1;
            } else {
               var2 = this.mask;
               var2[var4] &= -2;
            }

            if (var1.video_size_max != 0 && !var1.disabled) {
               var2 = this.mask;
               var2[var4] |= 4;
            } else {
               var2 = this.mask;
               var2[var4] &= -5;
            }

            if (var1.file_size_max != 0 && !var1.disabled) {
               var2 = this.mask;
               var2[var4] |= 8;
            } else {
               var2 = this.mask;
               var2[var4] &= -9;
            }

            ++var4;
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.mask[0]);
         var1.append("_");
         var1.append(this.mask[1]);
         var1.append("_");
         var1.append(this.mask[2]);
         var1.append("_");
         var1.append(this.mask[3]);
         var1.append("_");
         var1.append(this.sizes[0]);
         var1.append("_");
         var1.append(this.sizes[1]);
         var1.append("_");
         var1.append(this.sizes[2]);
         var1.append("_");
         var1.append(this.sizes[3]);
         var1.append("_");
         var1.append(this.preloadVideo);
         var1.append("_");
         var1.append(this.preloadMusic);
         var1.append("_");
         var1.append(this.enabled);
         var1.append("_");
         var1.append(this.lessCallData);
         return var1.toString();
      }
   }
}
