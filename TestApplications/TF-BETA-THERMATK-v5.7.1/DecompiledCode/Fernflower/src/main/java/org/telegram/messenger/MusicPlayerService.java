package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Notification.MediaStyle;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.media.session.MediaSession;
import android.media.session.MediaSession.Callback;
import android.media.session.PlaybackState.Builder;
import android.net.Uri;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import java.io.File;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.ui.LaunchActivity;

public class MusicPlayerService extends Service implements NotificationCenter.NotificationCenterDelegate {
   private static final int ID_NOTIFICATION = 5;
   public static final String NOTIFY_CLOSE = "org.telegram.android.musicplayer.close";
   public static final String NOTIFY_NEXT = "org.telegram.android.musicplayer.next";
   public static final String NOTIFY_PAUSE = "org.telegram.android.musicplayer.pause";
   public static final String NOTIFY_PLAY = "org.telegram.android.musicplayer.play";
   public static final String NOTIFY_PREVIOUS = "org.telegram.android.musicplayer.previous";
   public static final String NOTIFY_SEEK = "org.telegram.android.musicplayer.seek";
   private static boolean supportBigNotifications;
   private static boolean supportLockScreenControls;
   private Bitmap albumArtPlaceholder;
   private AudioManager audioManager;
   private BroadcastReceiver headsetPlugReceiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         if ("android.media.AUDIO_BECOMING_NOISY".equals(var2.getAction())) {
            MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
         }

      }
   };
   private ImageReceiver imageReceiver;
   private String loadingFilePath;
   private MediaSession mediaSession;
   private int notificationMessageID;
   private Builder playbackState;
   private RemoteControlClient remoteControlClient;

   static {
      int var0 = VERSION.SDK_INT;
      boolean var1 = true;
      boolean var2;
      if (var0 >= 16) {
         var2 = true;
      } else {
         var2 = false;
      }

      supportBigNotifications = var2;
      var2 = var1;
      if (VERSION.SDK_INT >= 21) {
         if (!TextUtils.isEmpty(AndroidUtilities.getSystemProperty("ro.miui.ui.version.code"))) {
            var2 = var1;
         } else {
            var2 = false;
         }
      }

      supportLockScreenControls = var2;
   }

   @SuppressLint({"NewApi"})
   private void createNotification(MessageObject var1, boolean var2) {
      String var3 = var1.getMusicTitle();
      String var4 = var1.getMusicAuthor();
      AudioInfo var5 = MediaController.getInstance().getAudioInfo();
      Intent var6 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
      var6.setAction("com.tmessages.openplayer");
      var6.addCategory("android.intent.category.LAUNCHER");
      PendingIntent var7 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, var6, 0);
      String var8 = var1.getArtworkUrl(true);
      String var9 = var1.getArtworkUrl(false);
      long var10 = (long)(var1.getDuration() * 1000);
      Bitmap var25;
      if (var5 != null) {
         var25 = var5.getSmallCover();
      } else {
         var25 = null;
      }

      Bitmap var12;
      if (var5 != null) {
         var12 = var5.getCover();
      } else {
         var12 = null;
      }

      this.loadingFilePath = null;
      this.imageReceiver.setImageBitmap((Drawable)null);
      Bitmap var22;
      if (var25 == null && !TextUtils.isEmpty(var8)) {
         var22 = this.loadArtworkFromUrl(var9, true, var2 ^ true);
         if (var22 == null) {
            var25 = this.loadArtworkFromUrl(var8, false, var2 ^ true);
            var22 = var25;
         } else {
            var25 = this.loadArtworkFromUrl(var9, false, var2 ^ true);
         }
      } else {
         this.loadingFilePath = FileLoader.getPathToAttach(var1.getDocument()).getAbsolutePath();
         var22 = var12;
      }

      byte var17;
      float var20;
      String var31;
      int var43;
      if (VERSION.SDK_INT >= 21) {
         var2 = MediaController.getInstance().isMessagePaused() ^ true;
         PendingIntent var33 = PendingIntent.getBroadcast(this.getApplicationContext(), 0, (new Intent("org.telegram.android.musicplayer.previous")).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
         Context var27 = this.getApplicationContext();
         Intent var37 = new Intent(this, MusicPlayerService.class);
         StringBuilder var13 = new StringBuilder();
         var13.append(this.getPackageName());
         var13.append(".STOP_PLAYER");
         PendingIntent var38 = PendingIntent.getService(var27, 0, var37.setAction(var13.toString()), 268435456);
         var27 = this.getApplicationContext();
         String var39;
         if (var2) {
            var39 = "org.telegram.android.musicplayer.pause";
         } else {
            var39 = "org.telegram.android.musicplayer.play";
         }

         PendingIntent var14 = PendingIntent.getBroadcast(var27, 0, (new Intent(var39)).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
         PendingIntent var15 = PendingIntent.getBroadcast(this.getApplicationContext(), 0, (new Intent("org.telegram.android.musicplayer.next")).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
         PendingIntent.getBroadcast(this.getApplicationContext(), 0, (new Intent("org.telegram.android.musicplayer.seek")).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
         android.app.Notification.Builder var30 = new android.app.Notification.Builder(this);
         android.app.Notification.Builder var16 = var30.setSmallIcon(2131165773).setOngoing(var2).setContentTitle(var3).setContentText(var4);
         if (var5 != null) {
            var39 = var5.getAlbum();
         } else {
            var39 = null;
         }

         var16.setSubText(var39).setContentIntent(var7).setDeleteIntent(var38).setShowWhen(false).setCategory("transport").setPriority(2).setStyle((new MediaStyle()).setMediaSession(this.mediaSession.getSessionToken()).setShowActionsInCompactView(new int[]{0, 1, 2}));
         if (VERSION.SDK_INT >= 26) {
            NotificationsController.checkOtherNotificationsChannel();
            var30.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
         }

         if (var25 != null) {
            var30.setLargeIcon(var25);
         } else {
            var30.setLargeIcon(this.albumArtPlaceholder);
         }

         if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            this.playbackState.setState(6, 0L, 1.0F).setActions(0L);
            var30.addAction((new android.app.Notification.Action.Builder(2131165424, "", var33)).build()).addAction((new android.app.Notification.Action.Builder(2131165539, "", (PendingIntent)null)).build()).addAction((new android.app.Notification.Action.Builder(2131165421, "", var15)).build());
         } else {
            Builder var28 = this.playbackState;
            if (var2) {
               var17 = 3;
            } else {
               var17 = 2;
            }

            long var18 = (long)MediaController.getInstance().getPlayingMessageObject().audioProgressSec;
            if (var2) {
               var20 = 1.0F;
            } else {
               var20 = 0.0F;
            }

            var28.setState(var17, var18 * 1000L, var20).setActions(822L);
            android.app.Notification.Builder var29 = var30.addAction((new android.app.Notification.Action.Builder(2131165424, "", var33)).build());
            if (var2) {
               var43 = 2131165422;
            } else {
               var43 = 2131165423;
            }

            var29.addAction((new android.app.Notification.Action.Builder(var43, "", var14)).build()).addAction((new android.app.Notification.Action.Builder(2131165421, "", var15)).build());
         }

         this.mediaSession.setPlaybackState(this.playbackState.build());
         android.media.MediaMetadata.Builder var41 = (new android.media.MediaMetadata.Builder()).putBitmap("android.media.metadata.ALBUM_ART", var22).putString("android.media.metadata.ALBUM_ARTIST", var4).putLong("android.media.metadata.DURATION", var10).putString("android.media.metadata.TITLE", var3);
         if (var5 != null) {
            var31 = var5.getAlbum();
         } else {
            var31 = null;
         }

         android.media.MediaMetadata.Builder var34 = var41.putString("android.media.metadata.ALBUM", var31);
         this.mediaSession.setMetadata(var34.build());
         var30.setVisibility(1);
         Notification var35 = var30.build();
         if (var2) {
            this.startForeground(5, var35);
         } else {
            this.stopForeground(false);
            ((NotificationManager)this.getSystemService("notification")).notify(5, var35);
         }
      } else {
         var9 = "";
         RemoteViews var32 = new RemoteViews(this.getApplicationContext().getPackageName(), 2131361831);
         RemoteViews var42;
         if (supportBigNotifications) {
            var42 = new RemoteViews(this.getApplicationContext().getPackageName(), 2131361830);
         } else {
            var42 = null;
         }

         NotificationCompat.Builder var40 = new NotificationCompat.Builder(this.getApplicationContext());
         var40.setSmallIcon(2131165773);
         var40.setContentIntent(var7);
         var40.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
         var40.setContentTitle(var3);
         Notification var26 = var40.build();
         var26.contentView = var32;
         if (supportBigNotifications) {
            var26.bigContentView = var42;
         }

         this.setListeners(var32);
         if (supportBigNotifications) {
            this.setListeners(var42);
         }

         if (var25 != null) {
            var26.contentView.setImageViewBitmap(2131230873, var25);
            if (supportBigNotifications) {
               var26.bigContentView.setImageViewBitmap(2131230873, var25);
            }
         } else {
            var26.contentView.setImageViewResource(2131230873, 2131165696);
            if (supportBigNotifications) {
               var26.bigContentView.setImageViewResource(2131230873, 2131165695);
            }
         }

         if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            var26.contentView.setViewVisibility(2131230878, 8);
            var26.contentView.setViewVisibility(2131230879, 8);
            var26.contentView.setViewVisibility(2131230877, 8);
            var26.contentView.setViewVisibility(2131230880, 8);
            var26.contentView.setViewVisibility(2131230881, 0);
            if (supportBigNotifications) {
               var26.bigContentView.setViewVisibility(2131230878, 8);
               var26.bigContentView.setViewVisibility(2131230879, 8);
               var26.bigContentView.setViewVisibility(2131230877, 8);
               var26.bigContentView.setViewVisibility(2131230880, 8);
               var26.bigContentView.setViewVisibility(2131230881, 0);
            }
         } else {
            var26.contentView.setViewVisibility(2131230881, 8);
            var26.contentView.setViewVisibility(2131230877, 0);
            var26.contentView.setViewVisibility(2131230880, 0);
            if (supportBigNotifications) {
               var26.bigContentView.setViewVisibility(2131230877, 0);
               var26.bigContentView.setViewVisibility(2131230880, 0);
               var26.bigContentView.setViewVisibility(2131230881, 8);
            }

            if (MediaController.getInstance().isMessagePaused()) {
               var26.contentView.setViewVisibility(2131230878, 8);
               var26.contentView.setViewVisibility(2131230879, 0);
               if (supportBigNotifications) {
                  var26.bigContentView.setViewVisibility(2131230878, 8);
                  var26.bigContentView.setViewVisibility(2131230879, 0);
               }
            } else {
               var26.contentView.setViewVisibility(2131230878, 0);
               var26.contentView.setViewVisibility(2131230879, 8);
               if (supportBigNotifications) {
                  var26.bigContentView.setViewVisibility(2131230878, 0);
                  var26.bigContentView.setViewVisibility(2131230879, 8);
               }
            }
         }

         var26.contentView.setTextViewText(2131230882, var3);
         var26.contentView.setTextViewText(2131230875, var4);
         if (supportBigNotifications) {
            var26.bigContentView.setTextViewText(2131230882, var3);
            var26.bigContentView.setTextViewText(2131230875, var4);
            var42 = var26.bigContentView;
            var31 = var9;
            if (var5 != null) {
               var31 = var9;
               if (!TextUtils.isEmpty(var5.getAlbum())) {
                  var31 = var5.getAlbum();
               }
            }

            var42.setTextViewText(2131230874, var31);
         }

         var26.flags |= 2;
         this.startForeground(5, var26);
      }

      if (this.remoteControlClient != null) {
         var43 = MediaController.getInstance().getPlayingMessageObject().getId();
         if (this.notificationMessageID != var43) {
            this.notificationMessageID = var43;
            MetadataEditor var36 = this.remoteControlClient.editMetadata(true);
            var36.putString(2, var4);
            var36.putString(7, var3);
            if (var5 != null && !TextUtils.isEmpty(var5.getAlbum())) {
               var36.putString(1, var5.getAlbum());
            }

            var36.putLong(9, (long)MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000L);
            if (var22 != null) {
               try {
                  var36.putBitmap(100, var22);
               } catch (Throwable var21) {
                  FileLog.e(var21);
               }
            }

            var36.apply();
            AndroidUtilities.runOnUIThread(new Runnable() {
               public void run() {
                  if (MusicPlayerService.this.remoteControlClient != null && MediaController.getInstance().getPlayingMessageObject() != null) {
                     if ((long)MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration == -9223372036854775807L) {
                        AndroidUtilities.runOnUIThread(this, 500L);
                        return;
                     }

                     MetadataEditor var1 = MusicPlayerService.this.remoteControlClient.editMetadata(false);
                     var1.putLong(9, (long)MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000L);
                     var1.apply();
                     int var2 = VERSION.SDK_INT;
                     byte var3 = 2;
                     RemoteControlClient var7;
                     if (var2 >= 18) {
                        var7 = MusicPlayerService.this.remoteControlClient;
                        if (!MediaController.getInstance().isMessagePaused()) {
                           var3 = 3;
                        }

                        long var4 = Math.max((long)MediaController.getInstance().getPlayingMessageObject().audioProgressSec * 1000L, 100L);
                        float var6;
                        if (MediaController.getInstance().isMessagePaused()) {
                           var6 = 0.0F;
                        } else {
                           var6 = 1.0F;
                        }

                        var7.setPlaybackState(var3, var4, var6);
                     } else {
                        var7 = MusicPlayerService.this.remoteControlClient;
                        if (!MediaController.getInstance().isMessagePaused()) {
                           var3 = 3;
                        }

                        var7.setPlaybackState(var3);
                     }
                  }

               }
            }, 1000L);
         }

         var17 = 2;
         if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            this.remoteControlClient.setPlaybackState(8);
         } else {
            MetadataEditor var23 = this.remoteControlClient.editMetadata(false);
            var23.putLong(9, (long)MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000L);
            var23.apply();
            RemoteControlClient var24;
            if (VERSION.SDK_INT >= 18) {
               var24 = this.remoteControlClient;
               if (!MediaController.getInstance().isMessagePaused()) {
                  var17 = 3;
               }

               var10 = Math.max((long)MediaController.getInstance().getPlayingMessageObject().audioProgressSec * 1000L, 100L);
               if (MediaController.getInstance().isMessagePaused()) {
                  var20 = 0.0F;
               } else {
                  var20 = 1.0F;
               }

               var24.setPlaybackState(var17, var10, var20);
            } else {
               var24 = this.remoteControlClient;
               if (!MediaController.getInstance().isMessagePaused()) {
                  var17 = 3;
               }

               var24.setPlaybackState(var17);
            }
         }
      }

   }

   private Bitmap loadArtworkFromUrl(String var1, boolean var2, boolean var3) {
      ImageLoader.getHttpFileName(var1);
      File var4 = ImageLoader.getHttpFilePath(var1, "jpg");
      if (var4.exists()) {
         var1 = var4.getAbsolutePath();
         float var5 = 600.0F;
         float var6;
         if (var2) {
            var6 = 600.0F;
         } else {
            var6 = 100.0F;
         }

         if (!var2) {
            var5 = 100.0F;
         }

         return ImageLoader.loadBitmap(var1, (Uri)null, var6, var5, false);
      } else {
         if (var3) {
            this.loadingFilePath = var4.getAbsolutePath();
            if (!var2) {
               this.imageReceiver.setImage(var1, "48_48", (Drawable)null, (String)null, 0);
            }
         } else {
            this.loadingFilePath = null;
         }

         return null;
      }
   }

   private void updatePlaybackState(long var1) {
      if (VERSION.SDK_INT >= 21) {
         boolean var3 = MediaController.getInstance().isMessagePaused() ^ true;
         boolean var4 = MediaController.getInstance().isDownloadingCurrentMessage();
         float var5 = 1.0F;
         if (var4) {
            this.playbackState.setState(6, 0L, 1.0F).setActions(0L);
         } else {
            Builder var6 = this.playbackState;
            byte var7;
            if (var3) {
               var7 = 3;
            } else {
               var7 = 2;
            }

            if (!var3) {
               var5 = 0.0F;
            }

            var6.setState(var7, var1, var5).setActions(822L);
         }

         this.mediaSession.setPlaybackState(this.playbackState.build());
      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.messagePlayingPlayStateChanged) {
         MessageObject var10 = MediaController.getInstance().getPlayingMessageObject();
         if (var10 != null) {
            this.createNotification(var10, false);
         } else {
            this.stopSelf();
         }
      } else {
         MessageObject var4;
         if (var1 == NotificationCenter.messagePlayingDidSeek) {
            var4 = MediaController.getInstance().getPlayingMessageObject();
            if (this.remoteControlClient != null && VERSION.SDK_INT >= 18) {
               long var5 = (long)Math.round((float)var4.audioPlayerDuration * (Float)var3[1]);
               RemoteControlClient var11 = this.remoteControlClient;
               byte var9;
               if (MediaController.getInstance().isMessagePaused()) {
                  var9 = 2;
               } else {
                  var9 = 3;
               }

               float var7;
               if (MediaController.getInstance().isMessagePaused()) {
                  var7 = 0.0F;
               } else {
                  var7 = 1.0F;
               }

               var11.setPlaybackState(var9, var5 * 1000L, var7);
            }
         } else {
            String var12;
            if (var1 == NotificationCenter.httpFileDidLoad) {
               String var8 = (String)var3[0];
               var4 = MediaController.getInstance().getPlayingMessageObject();
               if (var4 != null) {
                  var12 = this.loadingFilePath;
                  if (var12 != null && var12.equals(var8)) {
                     this.createNotification(var4, false);
                  }
               }
            } else if (var1 == NotificationCenter.fileDidLoad) {
               var12 = (String)var3[0];
               MessageObject var14 = MediaController.getInstance().getPlayingMessageObject();
               if (var14 != null) {
                  String var13 = this.loadingFilePath;
                  if (var13 != null && var13.equals(var12)) {
                     this.createNotification(var14, false);
                  }
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$onCreate$0$MusicPlayerService(ImageReceiver var1, boolean var2, boolean var3) {
      if (var2 && !TextUtils.isEmpty(this.loadingFilePath)) {
         MessageObject var4 = MediaController.getInstance().getPlayingMessageObject();
         if (var4 != null) {
            this.createNotification(var4, true);
         }

         this.loadingFilePath = null;
      }

   }

   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onCreate() {
      this.audioManager = (AudioManager)this.getSystemService("audio");

      for(int var1 = 0; var1 < 3; ++var1) {
         NotificationCenter.getInstance(var1).addObserver(this, NotificationCenter.messagePlayingDidSeek);
         NotificationCenter.getInstance(var1).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
         NotificationCenter.getInstance(var1).addObserver(this, NotificationCenter.httpFileDidLoad);
         NotificationCenter.getInstance(var1).addObserver(this, NotificationCenter.fileDidLoad);
      }

      this.imageReceiver = new ImageReceiver((View)null);
      this.imageReceiver.setDelegate(new _$$Lambda$MusicPlayerService$laWg3UUxrvXdIx91fvPuk_ss_Tg(this));
      if (VERSION.SDK_INT >= 21) {
         this.mediaSession = new MediaSession(this, "telegramAudioPlayer");
         this.playbackState = new Builder();
         this.albumArtPlaceholder = Bitmap.createBitmap(AndroidUtilities.dp(102.0F), AndroidUtilities.dp(102.0F), Config.ARGB_8888);
         Drawable var2 = this.getResources().getDrawable(2131165695);
         var2.setBounds(0, 0, this.albumArtPlaceholder.getWidth(), this.albumArtPlaceholder.getHeight());
         var2.draw(new Canvas(this.albumArtPlaceholder));
         this.mediaSession.setCallback(new Callback() {
            public void onPause() {
               MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
            }

            public void onPlay() {
               MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
            }

            public void onSeekTo(long var1) {
               MessageObject var3 = MediaController.getInstance().getPlayingMessageObject();
               if (var3 != null) {
                  MediaController.getInstance().seekToProgress(var3, (float)(var1 / 1000L) / (float)var3.getDuration());
                  MusicPlayerService.this.updatePlaybackState(var1);
               }

            }

            public void onSkipToNext() {
               MediaController.getInstance().playNextMessage();
            }

            public void onSkipToPrevious() {
               MediaController.getInstance().playPreviousMessage();
            }

            public void onStop() {
            }
         });
         this.mediaSession.setActive(true);
      }

      this.registerReceiver(this.headsetPlugReceiver, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
      super.onCreate();
   }

   @SuppressLint({"NewApi"})
   public void onDestroy() {
      this.unregisterReceiver(this.headsetPlugReceiver);
      super.onDestroy();
      RemoteControlClient var1 = this.remoteControlClient;
      if (var1 != null) {
         MetadataEditor var3 = var1.editMetadata(true);
         var3.clear();
         var3.apply();
         this.audioManager.unregisterRemoteControlClient(this.remoteControlClient);
      }

      if (VERSION.SDK_INT >= 21) {
         this.mediaSession.release();
      }

      for(int var2 = 0; var2 < 3; ++var2) {
         NotificationCenter.getInstance(var2).removeObserver(this, NotificationCenter.messagePlayingDidSeek);
         NotificationCenter.getInstance(var2).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
         NotificationCenter.getInstance(var2).removeObserver(this, NotificationCenter.httpFileDidLoad);
         NotificationCenter.getInstance(var2).removeObserver(this, NotificationCenter.fileDidLoad);
      }

   }

   @SuppressLint({"NewApi"})
   public int onStartCommand(Intent var1, int var2, int var3) {
      Exception var10000;
      label76: {
         boolean var10001;
         if (var1 != null) {
            try {
               StringBuilder var4 = new StringBuilder();
               var4.append(this.getPackageName());
               var4.append(".STOP_PLAYER");
               if (var4.toString().equals(var1.getAction())) {
                  MediaController.getInstance().cleanupPlayer(true, true);
                  return 2;
               }
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label76;
            }
         }

         MessageObject var14;
         try {
            var14 = MediaController.getInstance().getPlayingMessageObject();
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break label76;
         }

         if (var14 == null) {
            try {
               _$$Lambda$C34ajmt2WsPXgqbkhgBzCpu6VDo var15 = new _$$Lambda$C34ajmt2WsPXgqbkhgBzCpu6VDo(this);
               AndroidUtilities.runOnUIThread(var15);
               return 1;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         } else {
            label65: {
               label77: {
                  ComponentName var5;
                  try {
                     if (!supportLockScreenControls) {
                        break label77;
                     }

                     var5 = new ComponentName(this.getApplicationContext(), MusicPlayerReceiver.class.getName());
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label65;
                  }

                  label58: {
                     try {
                        if (this.remoteControlClient == null) {
                           this.audioManager.registerMediaButtonEventReceiver(var5);
                           Intent var17 = new Intent("android.intent.action.MEDIA_BUTTON");
                           var17.setComponent(var5);
                           PendingIntent var18 = PendingIntent.getBroadcast(this, 0, var17, 0);
                           RemoteControlClient var20 = new RemoteControlClient(var18);
                           this.remoteControlClient = var20;
                           this.audioManager.registerRemoteControlClient(this.remoteControlClient);
                        }
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label58;
                     }

                     try {
                        this.remoteControlClient.setTransportControlFlags(189);
                        break label77;
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                     }
                  }

                  Exception var19 = var10000;

                  try {
                     FileLog.e((Throwable)var19);
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label65;
                  }
               }

               try {
                  this.createNotification(var14, false);
                  return 1;
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }
         }
      }

      Exception var16 = var10000;
      var16.printStackTrace();
      return 1;
   }

   public void setListeners(RemoteViews var1) {
      var1.setOnClickPendingIntent(2131230880, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.previous"), 134217728));
      var1.setOnClickPendingIntent(2131230876, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.close"), 134217728));
      var1.setOnClickPendingIntent(2131230878, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.pause"), 134217728));
      var1.setOnClickPendingIntent(2131230877, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.next"), 134217728));
      var1.setOnClickPendingIntent(2131230879, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.play"), 134217728));
   }
}
