package org.telegram.messenger;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaMetadata.Builder;
import android.media.browse.MediaBrowser.MediaItem;
import android.media.session.MediaSession;
import android.media.session.MediaSession.Callback;
import android.media.session.MediaSession.QueueItem;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.service.media.MediaBrowserService;
import android.service.media.MediaBrowserService.BrowserRoot;
import android.service.media.MediaBrowserService.Result;
import android.text.TextUtils;
import android.util.SparseArray;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;

@TargetApi(21)
public class MusicBrowserService extends MediaBrowserService implements NotificationCenter.NotificationCenterDelegate {
   public static final String ACTION_CMD = "com.example.android.mediabrowserservice.ACTION_CMD";
   public static final String CMD_NAME = "CMD_NAME";
   public static final String CMD_PAUSE = "CMD_PAUSE";
   private static final String MEDIA_ID_ROOT = "__ROOT__";
   private static final String SLOT_RESERVATION_QUEUE = "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_QUEUE";
   private static final String SLOT_RESERVATION_SKIP_TO_NEXT = "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_NEXT";
   private static final String SLOT_RESERVATION_SKIP_TO_PREV = "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_PREVIOUS";
   private static final int STOP_DELAY = 30000;
   private RectF bitmapRect;
   private SparseArray chats;
   private boolean chatsLoaded;
   private int currentAccount;
   private MusicBrowserService.DelayedStopHandler delayedStopHandler;
   private ArrayList dialogs;
   private int lastSelectedDialog;
   private boolean loadingChats;
   private MediaSession mediaSession;
   private SparseArray musicObjects;
   private SparseArray musicQueues;
   private Paint roundPaint;
   private boolean serviceStarted;
   private SparseArray users;

   public MusicBrowserService() {
      this.currentAccount = UserConfig.selectedAccount;
      this.dialogs = new ArrayList();
      this.users = new SparseArray();
      this.chats = new SparseArray();
      this.musicObjects = new SparseArray();
      this.musicQueues = new SparseArray();
      this.delayedStopHandler = new MusicBrowserService.DelayedStopHandler(this);
   }

   private Bitmap createRoundBitmap(File param1) {
      // $FF: Couldn't be decompiled
   }

   private long getAvailableActions() {
      MessageObject var1 = MediaController.getInstance().getPlayingMessageObject();
      long var2 = 3076L;
      long var4 = var2;
      if (var1 != null) {
         if (!MediaController.getInstance().isMessagePaused()) {
            var2 = 3078L;
         }

         var4 = var2 | 16L | 32L;
      }

      return var4;
   }

   private void handlePauseRequest() {
      MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
      this.delayedStopHandler.removeCallbacksAndMessages((Object)null);
      this.delayedStopHandler.sendEmptyMessageDelayed(0, 30000L);
   }

   private void handlePlayRequest() {
      this.delayedStopHandler.removeCallbacksAndMessages((Object)null);
      if (!this.serviceStarted) {
         try {
            Intent var1 = new Intent(this.getApplicationContext(), MusicBrowserService.class);
            this.startService(var1);
         } catch (Throwable var3) {
            FileLog.e(var3);
         }

         this.serviceStarted = true;
      }

      if (!this.mediaSession.isActive()) {
         this.mediaSession.setActive(true);
      }

      MessageObject var2 = MediaController.getInstance().getPlayingMessageObject();
      if (var2 != null) {
         Builder var4 = new Builder();
         var4.putLong("android.media.metadata.DURATION", (long)(var2.getDuration() * 1000));
         var4.putString("android.media.metadata.ARTIST", var2.getMusicAuthor());
         var4.putString("android.media.metadata.TITLE", var2.getMusicTitle());
         AudioInfo var5 = MediaController.getInstance().getAudioInfo();
         if (var5 != null) {
            Bitmap var6 = var5.getCover();
            if (var6 != null) {
               var4.putBitmap("android.media.metadata.ALBUM_ART", var6);
            }
         }

         this.mediaSession.setMetadata(var4.build());
      }
   }

   private void handleStopRequest(String var1) {
      this.delayedStopHandler.removeCallbacksAndMessages((Object)null);
      this.delayedStopHandler.sendEmptyMessageDelayed(0, 30000L);
      this.updatePlaybackState(var1);
      this.stopSelf();
      this.serviceStarted = false;
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
   }

   private void loadChildrenImpl(String var1, Result var2) {
      ArrayList var3 = new ArrayList();
      boolean var4 = "__ROOT__".equals(var1);
      int var5 = 0;
      int var6 = 0;
      android.media.MediaDescription.Builder var8;
      if (!var4) {
         if (var1 != null && var1.startsWith("__CHAT_")) {
            try {
               var6 = Integer.parseInt(var1.replace("__CHAT_", ""));
            } catch (Exception var10) {
               FileLog.e((Throwable)var10);
               var6 = 0;
            }

            ArrayList var19 = (ArrayList)this.musicObjects.get(var6);
            if (var19 != null) {
               while(var5 < var19.size()) {
                  MessageObject var17 = (MessageObject)var19.get(var5);
                  var8 = new android.media.MediaDescription.Builder();
                  StringBuilder var20 = new StringBuilder();
                  var20.append(var6);
                  var20.append("_");
                  var20.append(var5);
                  android.media.MediaDescription.Builder var21 = var8.setMediaId(var20.toString());
                  var21.setTitle(var17.getMusicTitle());
                  var21.setSubtitle(var17.getMusicAuthor());
                  var3.add(new MediaItem(var21.build(), 2));
                  ++var5;
               }
            }
         }
      } else {
         while(var6 < this.dialogs.size()) {
            StringBuilder var11;
            TLRPC.FileLocation var14;
            Bitmap var18;
            label59: {
               var5 = (Integer)this.dialogs.get(var6);
               android.media.MediaDescription.Builder var7 = new android.media.MediaDescription.Builder();
               var11 = new StringBuilder();
               var11.append("__CHAT_");
               var11.append(var5);
               var8 = var7.setMediaId(var11.toString());
               var18 = null;
               if (var5 > 0) {
                  TLRPC.User var12 = (TLRPC.User)this.users.get(var5);
                  if (var12 != null) {
                     var8.setTitle(ContactsController.formatName(var12.first_name, var12.last_name));
                     TLRPC.UserProfilePhoto var13 = var12.photo;
                     if (var13 != null) {
                        var14 = var13.photo_small;
                        if (!(var14 instanceof TLRPC.TL_fileLocationUnavailable)) {
                           break label59;
                        }
                     }
                  } else {
                     var8.setTitle("DELETED USER");
                  }
               } else {
                  TLRPC.Chat var15 = (TLRPC.Chat)this.chats.get(-var5);
                  if (var15 != null) {
                     var8.setTitle(var15.title);
                     TLRPC.ChatPhoto var16 = var15.photo;
                     if (var16 != null) {
                        var14 = var16.photo_small;
                        if (!(var14 instanceof TLRPC.TL_fileLocationUnavailable)) {
                           break label59;
                        }
                     }
                  } else {
                     var8.setTitle("DELETED CHAT");
                  }
               }

               var14 = null;
            }

            if (var14 != null) {
               Bitmap var9 = this.createRoundBitmap(FileLoader.getPathToAttach(var14, true));
               var18 = var9;
               if (var9 != null) {
                  var8.setIconBitmap(var9);
                  var18 = var9;
               }
            }

            if (var14 == null || var18 == null) {
               var11 = new StringBuilder();
               var11.append("android.resource://");
               var11.append(this.getApplicationContext().getPackageName());
               var11.append("/drawable/contact_blue");
               var8.setIconUri(Uri.parse(var11.toString()));
            }

            var3.add(new MediaItem(var8.build(), 1));
            ++var6;
         }
      }

      var2.sendResult(var3);
   }

   private void updatePlaybackState(String var1) {
      MessageObject var2 = MediaController.getInstance().getPlayingMessageObject();
      long var3;
      if (var2 != null) {
         var3 = (long)var2.audioProgressSec * 1000L;
      } else {
         var3 = -1L;
      }

      android.media.session.PlaybackState.Builder var5 = (new android.media.session.PlaybackState.Builder()).setActions(this.getAvailableActions());
      byte var6;
      if (var2 == null) {
         var6 = 1;
      } else if (MediaController.getInstance().isDownloadingCurrentMessage()) {
         var6 = 6;
      } else if (MediaController.getInstance().isMessagePaused()) {
         var6 = 2;
      } else {
         var6 = 3;
      }

      if (var1 != null) {
         var5.setErrorMessage(var1);
         var6 = 7;
      }

      var5.setState(var6, var3, 1.0F, SystemClock.elapsedRealtime());
      if (var2 != null) {
         var5.setActiveQueueItemId((long)MediaController.getInstance().getPlayingMessageObjectNum());
      } else {
         var5.setActiveQueueItemId(0L);
      }

      this.mediaSession.setPlaybackState(var5.build());
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      this.updatePlaybackState((String)null);
      this.handlePlayRequest();
   }

   // $FF: synthetic method
   public void lambda$null$0$MusicBrowserService(String var1, Result var2) {
      this.chatsLoaded = true;
      this.loadingChats = false;
      this.loadChildrenImpl(var1, var2);
      if (this.lastSelectedDialog == 0 && !this.dialogs.isEmpty()) {
         this.lastSelectedDialog = (Integer)this.dialogs.get(0);
      }

      int var3 = this.lastSelectedDialog;
      if (var3 != 0) {
         ArrayList var4 = (ArrayList)this.musicObjects.get(var3);
         ArrayList var6 = (ArrayList)this.musicQueues.get(this.lastSelectedDialog);
         if (var4 != null && !var4.isEmpty()) {
            this.mediaSession.setQueue(var6);
            var3 = this.lastSelectedDialog;
            if (var3 > 0) {
               TLRPC.User var7 = (TLRPC.User)this.users.get(var3);
               if (var7 != null) {
                  this.mediaSession.setQueueTitle(ContactsController.formatName(var7.first_name, var7.last_name));
               } else {
                  this.mediaSession.setQueueTitle("DELETED USER");
               }
            } else {
               TLRPC.Chat var8 = (TLRPC.Chat)this.chats.get(-var3);
               if (var8 != null) {
                  this.mediaSession.setQueueTitle(var8.title);
               } else {
                  this.mediaSession.setQueueTitle("DELETED CHAT");
               }
            }

            MessageObject var9 = (MessageObject)var4.get(0);
            Builder var5 = new Builder();
            var5.putLong("android.media.metadata.DURATION", (long)(var9.getDuration() * 1000));
            var5.putString("android.media.metadata.ARTIST", var9.getMusicAuthor());
            var5.putString("android.media.metadata.TITLE", var9.getMusicTitle());
            this.mediaSession.setMetadata(var5.build());
         }
      }

      this.updatePlaybackState((String)null);
   }

   // $FF: synthetic method
   public void lambda$onLoadChildren$1$MusicBrowserService(MessagesStorage var1, String var2, Result var3) {
      label145: {
         Exception var10000;
         label147: {
            ArrayList var4;
            ArrayList var5;
            SQLiteDatabase var6;
            Locale var7;
            boolean var10001;
            try {
               var4 = new ArrayList();
               var5 = new ArrayList();
               var6 = var1.getDatabase();
               var7 = Locale.US;
            } catch (Exception var28) {
               var10000 = var28;
               var10001 = false;
               break label147;
            }

            byte var8 = 0;

            SQLiteCursor var32;
            try {
               var32 = var6.queryFinalized(String.format(var7, "SELECT DISTINCT uid FROM media_v2 WHERE uid != 0 AND mid > 0 AND type = %d", 4));
            } catch (Exception var26) {
               var10000 = var26;
               var10001 = false;
               break label147;
            }

            int var9;
            while(true) {
               try {
                  if (!var32.next()) {
                     break;
                  }

                  var9 = (int)var32.longValue(0);
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label147;
               }

               if (var9 != 0) {
                  try {
                     this.dialogs.add(var9);
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label147;
                  }

                  if (var9 > 0) {
                     try {
                        var4.add(var9);
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label147;
                     }
                  } else {
                     try {
                        var5.add(-var9);
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label147;
                     }
                  }
               }
            }

            SQLiteCursor var10;
            try {
               var32.dispose();
               if (this.dialogs.isEmpty()) {
                  break label145;
               }

               String var34 = TextUtils.join(",", this.dialogs);
               var10 = var1.getDatabase().queryFinalized(String.format(Locale.US, "SELECT uid, data, mid FROM media_v2 WHERE uid IN (%s) AND mid > 0 AND type = %d ORDER BY date DESC, mid DESC", var34, 4));
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label147;
            }

            ArrayList var37;
            while(true) {
               NativeByteBuffer var36;
               try {
                  if (!var10.next()) {
                     break;
                  }

                  var36 = var10.byteBufferValue(1);
               } catch (Exception var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label147;
               }

               if (var36 != null) {
                  TLRPC.Message var11;
                  ArrayList var12;
                  try {
                     var11 = TLRPC.Message.TLdeserialize(var36, var36.readInt32(false), false);
                     var11.readAttachPath(var36, UserConfig.getInstance(this.currentAccount).clientUserId);
                     var36.reuse();
                     if (!MessageObject.isMusicMessage(var11)) {
                        continue;
                     }

                     var9 = var10.intValue(0);
                     var11.id = var10.intValue(2);
                     var11.dialog_id = (long)var9;
                     var12 = (ArrayList)this.musicObjects.get(var9);
                     var37 = (ArrayList)this.musicQueues.get(var9);
                  } catch (Exception var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label147;
                  }

                  ArrayList var31 = var12;
                  if (var12 == null) {
                     try {
                        var31 = new ArrayList();
                        this.musicObjects.put(var9, var31);
                        var37 = new ArrayList();
                        this.musicQueues.put(var9, var37);
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label147;
                     }
                  }

                  try {
                     MessageObject var39 = new MessageObject(this.currentAccount, var11, false);
                     var31.add(0, var39);
                     android.media.MediaDescription.Builder var13 = new android.media.MediaDescription.Builder();
                     StringBuilder var38 = new StringBuilder();
                     var38.append(var9);
                     var38.append("_");
                     var38.append(var31.size());
                     android.media.MediaDescription.Builder var33 = var13.setMediaId(var38.toString());
                     var33.setTitle(var39.getMusicTitle());
                     var33.setSubtitle(var39.getMusicAuthor());
                     QueueItem var40 = new QueueItem(var33.build(), (long)var37.size());
                     var37.add(0, var40);
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label147;
                  }
               }
            }

            label149: {
               try {
                  var10.dispose();
                  if (var4.isEmpty()) {
                     break label149;
                  }

                  var37 = new ArrayList();
                  var1.getUsersInternal(TextUtils.join(",", var4), var37);
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label147;
               }

               var9 = 0;

               while(true) {
                  try {
                     if (var9 >= var37.size()) {
                        break;
                     }

                     TLRPC.User var35 = (TLRPC.User)var37.get(var9);
                     this.users.put(var35.id, var35);
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label147;
                  }

                  ++var9;
               }
            }

            try {
               if (var5.isEmpty()) {
                  break label145;
               }

               var37 = new ArrayList();
               var1.getChatsInternal(TextUtils.join(",", var5), var37);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label147;
            }

            var9 = var8;

            while(true) {
               try {
                  if (var9 >= var37.size()) {
                     break label145;
                  }

                  TLRPC.Chat var30 = (TLRPC.Chat)var37.get(var9);
                  this.chats.put(var30.id, var30);
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break;
               }

               ++var9;
            }
         }

         Exception var29 = var10000;
         FileLog.e((Throwable)var29);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$MusicBrowserService$kPFLpC10uxOMtsW9zhDqaIzSat8(this, var2, var3));
   }

   public void onCreate() {
      super.onCreate();
      ApplicationLoader.postInitApplication();
      this.lastSelectedDialog = MessagesController.getNotificationsSettings(this.currentAccount).getInt("auto_lastSelectedDialog", 0);
      this.mediaSession = new MediaSession(this, "MusicService");
      this.setSessionToken(this.mediaSession.getSessionToken());
      this.mediaSession.setCallback(new MusicBrowserService.MediaSessionCallback());
      this.mediaSession.setFlags(3);
      Context var1 = this.getApplicationContext();
      PendingIntent var2 = PendingIntent.getActivity(var1, 99, new Intent(var1, LaunchActivity.class), 134217728);
      this.mediaSession.setSessionActivity(var2);
      Bundle var3 = new Bundle();
      var3.putBoolean("com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_QUEUE", true);
      var3.putBoolean("com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_PREVIOUS", true);
      var3.putBoolean("com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_NEXT", true);
      this.mediaSession.setExtras(var3);
      this.updatePlaybackState((String)null);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
   }

   public void onDestroy() {
      this.handleStopRequest((String)null);
      this.delayedStopHandler.removeCallbacksAndMessages((Object)null);
      this.mediaSession.release();
   }

   public BrowserRoot onGetRoot(String var1, int var2, Bundle var3) {
      return var1 == null || 1000 != var2 && Process.myUid() != var2 && !var1.equals("com.google.android.mediasimulator") && !var1.equals("com.google.android.projection.gearhead") ? null : new BrowserRoot("__ROOT__", (Bundle)null);
   }

   public void onLoadChildren(String var1, Result var2) {
      if (!this.chatsLoaded) {
         var2.detach();
         if (this.loadingChats) {
            return;
         }

         this.loadingChats = true;
         MessagesStorage var3 = MessagesStorage.getInstance(this.currentAccount);
         var3.getStorageQueue().postRunnable(new _$$Lambda$MusicBrowserService$iS7bPWX5pXtbCNrWxnzS_j3JBYQ(this, var3, var1, var2));
      } else {
         this.loadChildrenImpl(var1, var2);
      }

   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      return 1;
   }

   private static class DelayedStopHandler extends Handler {
      private final WeakReference mWeakReference;

      private DelayedStopHandler(MusicBrowserService var1) {
         this.mWeakReference = new WeakReference(var1);
      }

      // $FF: synthetic method
      DelayedStopHandler(MusicBrowserService var1, Object var2) {
         this(var1);
      }

      public void handleMessage(Message var1) {
         MusicBrowserService var2 = (MusicBrowserService)this.mWeakReference.get();
         if (var2 != null) {
            if (MediaController.getInstance().getPlayingMessageObject() != null && !MediaController.getInstance().isMessagePaused()) {
               return;
            }

            var2.stopSelf();
            var2.serviceStarted = false;
         }

      }
   }

   private final class MediaSessionCallback extends Callback {
      private MediaSessionCallback() {
      }

      // $FF: synthetic method
      MediaSessionCallback(Object var2) {
         this();
      }

      public void onPause() {
         MusicBrowserService.this.handlePauseRequest();
      }

      public void onPlay() {
         MessageObject var1 = MediaController.getInstance().getPlayingMessageObject();
         if (var1 == null) {
            StringBuilder var2 = new StringBuilder();
            var2.append(MusicBrowserService.this.lastSelectedDialog);
            var2.append("_");
            var2.append(0);
            this.onPlayFromMediaId(var2.toString(), (Bundle)null);
         } else {
            MediaController.getInstance().playMessage(var1);
         }

      }

      public void onPlayFromMediaId(String var1, Bundle var2) {
         String[] var14 = var1.split("_");
         if (var14.length == 2) {
            label81: {
               Exception var10000;
               label87: {
                  int var3;
                  int var4;
                  ArrayList var15;
                  ArrayList var17;
                  boolean var10001;
                  try {
                     var3 = Integer.parseInt(var14[0]);
                     var4 = Integer.parseInt(var14[1]);
                     var15 = (ArrayList)MusicBrowserService.this.musicObjects.get(var3);
                     var17 = (ArrayList)MusicBrowserService.this.musicQueues.get(var3);
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label87;
                  }

                  if (var15 == null || var4 < 0) {
                     return;
                  }

                  try {
                     if (var4 >= var15.size()) {
                        return;
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label87;
                  }

                  try {
                     MusicBrowserService.this.lastSelectedDialog = var3;
                     MessagesController.getNotificationsSettings(MusicBrowserService.this.currentAccount).edit().putInt("auto_lastSelectedDialog", var3).commit();
                     MediaController.getInstance().setPlaylist(var15, (MessageObject)var15.get(var4), false);
                     MusicBrowserService.this.mediaSession.setQueue(var17);
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label87;
                  }

                  if (var3 > 0) {
                     label61: {
                        TLRPC.User var16;
                        try {
                           var16 = (TLRPC.User)MusicBrowserService.this.users.get(var3);
                        } catch (Exception var7) {
                           var10000 = var7;
                           var10001 = false;
                           break label61;
                        }

                        if (var16 != null) {
                           try {
                              MusicBrowserService.this.mediaSession.setQueueTitle(ContactsController.formatName(var16.first_name, var16.last_name));
                              break label81;
                           } catch (Exception var5) {
                              var10000 = var5;
                              var10001 = false;
                           }
                        } else {
                           try {
                              MusicBrowserService.this.mediaSession.setQueueTitle("DELETED USER");
                              break label81;
                           } catch (Exception var6) {
                              var10000 = var6;
                              var10001 = false;
                           }
                        }
                     }
                  } else {
                     label69: {
                        TLRPC.Chat var18;
                        try {
                           var18 = (TLRPC.Chat)MusicBrowserService.this.chats.get(-var3);
                        } catch (Exception var10) {
                           var10000 = var10;
                           var10001 = false;
                           break label69;
                        }

                        if (var18 != null) {
                           try {
                              MusicBrowserService.this.mediaSession.setQueueTitle(var18.title);
                              break label81;
                           } catch (Exception var8) {
                              var10000 = var8;
                              var10001 = false;
                           }
                        } else {
                           try {
                              MusicBrowserService.this.mediaSession.setQueueTitle("DELETED CHAT");
                              break label81;
                           } catch (Exception var9) {
                              var10000 = var9;
                              var10001 = false;
                           }
                        }
                     }
                  }
               }

               Exception var19 = var10000;
               FileLog.e((Throwable)var19);
            }

            MusicBrowserService.this.handlePlayRequest();
         }
      }

      public void onPlayFromSearch(String var1, Bundle var2) {
         if (var1 != null && var1.length() != 0) {
            var1 = var1.toLowerCase();
            int var3 = 0;

            int var4;
            StringBuilder var6;
            while(true) {
               if (var3 >= MusicBrowserService.this.dialogs.size()) {
                  return;
               }

               var4 = (Integer)MusicBrowserService.this.dialogs.get(var3);
               String var8;
               if (var4 > 0) {
                  TLRPC.User var7 = (TLRPC.User)MusicBrowserService.this.users.get(var4);
                  if (var7 != null) {
                     String var5 = var7.first_name;
                     if (var5 != null && var5.startsWith(var1)) {
                        break;
                     }

                     var8 = var7.last_name;
                     if (var8 != null && var8.startsWith(var1)) {
                        break;
                     }
                  }
               } else {
                  TLRPC.Chat var9 = (TLRPC.Chat)MusicBrowserService.this.chats.get(-var4);
                  if (var9 != null) {
                     var8 = var9.title;
                     if (var8 != null && var8.toLowerCase().contains(var1)) {
                        var6 = new StringBuilder();
                        var6.append(var4);
                        var6.append("_");
                        var6.append(0);
                        this.onPlayFromMediaId(var6.toString(), (Bundle)null);
                        return;
                     }
                  }
               }

               ++var3;
            }

            var6 = new StringBuilder();
            var6.append(var4);
            var6.append("_");
            var6.append(0);
            this.onPlayFromMediaId(var6.toString(), (Bundle)null);
         }

      }

      public void onSeekTo(long var1) {
         MessageObject var3 = MediaController.getInstance().getPlayingMessageObject();
         if (var3 != null) {
            MediaController.getInstance().seekToProgress(var3, (float)(var1 / 1000L) / (float)var3.getDuration());
         }

      }

      public void onSkipToNext() {
         MediaController.getInstance().playNextMessage();
      }

      public void onSkipToPrevious() {
         MediaController.getInstance().playPreviousMessage();
      }

      public void onSkipToQueueItem(long var1) {
         MediaController.getInstance().playMessageAtIndex((int)var1);
         MusicBrowserService.this.handlePlayRequest();
      }

      public void onStop() {
         MusicBrowserService.this.handleStopRequest((String)null);
      }
   }
}
