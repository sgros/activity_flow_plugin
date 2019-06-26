// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.IntentFilter;
import android.media.session.MediaSession$Callback;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.view.View;
import android.os.IBinder;
import java.io.File;
import android.net.Uri;
import android.annotation.SuppressLint;
import android.app.Notification;
import org.telegram.messenger.audioinfo.AudioInfo;
import android.media.RemoteControlClient$MetadataEditor;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;
import android.app.NotificationManager;
import android.media.MediaMetadata$Builder;
import android.app.Notification$Action$Builder;
import android.app.Notification$Style;
import android.app.Notification$MediaStyle;
import android.app.Notification$Builder;
import android.content.ComponentName;
import org.telegram.tgnet.TLObject;
import android.graphics.drawable.Drawable;
import android.app.PendingIntent;
import org.telegram.ui.LaunchActivity;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.os.Build$VERSION;
import android.media.RemoteControlClient;
import android.media.session.PlaybackState$Builder;
import android.media.session.MediaSession;
import android.content.BroadcastReceiver;
import android.media.AudioManager;
import android.graphics.Bitmap;
import android.app.Service;

public class MusicPlayerService extends Service implements NotificationCenterDelegate
{
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
    private BroadcastReceiver headsetPlugReceiver;
    private ImageReceiver imageReceiver;
    private String loadingFilePath;
    private MediaSession mediaSession;
    private int notificationMessageID;
    private PlaybackState$Builder playbackState;
    private RemoteControlClient remoteControlClient;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = true;
        MusicPlayerService.supportBigNotifications = (sdk_INT >= 16);
        boolean supportLockScreenControls = b;
        if (Build$VERSION.SDK_INT >= 21) {
            supportLockScreenControls = (!TextUtils.isEmpty((CharSequence)AndroidUtilities.getSystemProperty("ro.miui.ui.version.code")) && b);
        }
        MusicPlayerService.supportLockScreenControls = supportLockScreenControls;
    }
    
    public MusicPlayerService() {
        this.headsetPlugReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if ("android.media.AUDIO_BECOMING_NOISY".equals(intent.getAction())) {
                    MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
                }
            }
        };
    }
    
    @SuppressLint({ "NewApi" })
    private void createNotification(final MessageObject messageObject, final boolean b) {
        final String musicTitle = messageObject.getMusicTitle();
        final String musicAuthor = messageObject.getMusicAuthor();
        final AudioInfo audioInfo = MediaController.getInstance().getAudioInfo();
        final Intent intent = new Intent(ApplicationLoader.applicationContext, (Class)LaunchActivity.class);
        intent.setAction("com.tmessages.openplayer");
        intent.addCategory("android.intent.category.LAUNCHER");
        final PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 0);
        final String artworkUrl = messageObject.getArtworkUrl(true);
        final String artworkUrl2 = messageObject.getArtworkUrl(false);
        final long n = messageObject.getDuration() * 1000;
        Bitmap largeIcon;
        if (audioInfo != null) {
            largeIcon = audioInfo.getSmallCover();
        }
        else {
            largeIcon = null;
        }
        Bitmap cover;
        if (audioInfo != null) {
            cover = audioInfo.getCover();
        }
        else {
            cover = null;
        }
        this.loadingFilePath = null;
        this.imageReceiver.setImageBitmap((Drawable)null);
        Bitmap bitmap;
        if (largeIcon == null && !TextUtils.isEmpty((CharSequence)artworkUrl)) {
            bitmap = this.loadArtworkFromUrl(artworkUrl2, true, b ^ true);
            if (bitmap == null) {
                largeIcon = (bitmap = this.loadArtworkFromUrl(artworkUrl, false, b ^ true));
            }
            else {
                largeIcon = this.loadArtworkFromUrl(artworkUrl2, false, b ^ true);
            }
        }
        else {
            this.loadingFilePath = FileLoader.getPathToAttach(messageObject.getDocument()).getAbsolutePath();
            bitmap = cover;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            final boolean ongoing = MediaController.getInstance().isMessagePaused() ^ true;
            final PendingIntent broadcast = PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.previous").setComponent(new ComponentName((Context)this, (Class)MusicPlayerReceiver.class)), 268435456);
            final Context applicationContext = this.getApplicationContext();
            final Intent intent2 = new Intent((Context)this, (Class)MusicPlayerService.class);
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getPackageName());
            sb.append(".STOP_PLAYER");
            final PendingIntent service = PendingIntent.getService(applicationContext, 0, intent2.setAction(sb.toString()), 268435456);
            final Context applicationContext2 = this.getApplicationContext();
            String s;
            if (ongoing) {
                s = "org.telegram.android.musicplayer.pause";
            }
            else {
                s = "org.telegram.android.musicplayer.play";
            }
            final PendingIntent broadcast2 = PendingIntent.getBroadcast(applicationContext2, 0, new Intent(s).setComponent(new ComponentName((Context)this, (Class)MusicPlayerReceiver.class)), 268435456);
            final PendingIntent broadcast3 = PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.next").setComponent(new ComponentName((Context)this, (Class)MusicPlayerReceiver.class)), 268435456);
            PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.seek").setComponent(new ComponentName((Context)this, (Class)MusicPlayerReceiver.class)), 268435456);
            final Notification$Builder notification$Builder = new Notification$Builder((Context)this);
            final Notification$Builder setContentText = notification$Builder.setSmallIcon(2131165773).setOngoing(ongoing).setContentTitle((CharSequence)musicTitle).setContentText((CharSequence)musicAuthor);
            String album;
            if (audioInfo != null) {
                album = audioInfo.getAlbum();
            }
            else {
                album = null;
            }
            setContentText.setSubText((CharSequence)album).setContentIntent(activity).setDeleteIntent(service).setShowWhen(false).setCategory("transport").setPriority(2).setStyle((Notification$Style)new Notification$MediaStyle().setMediaSession(this.mediaSession.getSessionToken()).setShowActionsInCompactView(new int[] { 0, 1, 2 }));
            if (Build$VERSION.SDK_INT >= 26) {
                NotificationsController.checkOtherNotificationsChannel();
                notification$Builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            }
            if (largeIcon != null) {
                notification$Builder.setLargeIcon(largeIcon);
            }
            else {
                notification$Builder.setLargeIcon(this.albumArtPlaceholder);
            }
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                this.playbackState.setState(6, 0L, 1.0f).setActions(0L);
                notification$Builder.addAction(new Notification$Action$Builder(2131165424, (CharSequence)"", broadcast).build()).addAction(new Notification$Action$Builder(2131165539, (CharSequence)"", (PendingIntent)null).build()).addAction(new Notification$Action$Builder(2131165421, (CharSequence)"", broadcast3).build());
            }
            else {
                final PlaybackState$Builder playbackState = this.playbackState;
                int n2;
                if (ongoing) {
                    n2 = 3;
                }
                else {
                    n2 = 2;
                }
                final long n3 = MediaController.getInstance().getPlayingMessageObject().audioProgressSec;
                float n4;
                if (ongoing) {
                    n4 = 1.0f;
                }
                else {
                    n4 = 0.0f;
                }
                playbackState.setState(n2, n3 * 1000L, n4).setActions(822L);
                final Notification$Builder addAction = notification$Builder.addAction(new Notification$Action$Builder(2131165424, (CharSequence)"", broadcast).build());
                int n5;
                if (ongoing) {
                    n5 = 2131165422;
                }
                else {
                    n5 = 2131165423;
                }
                addAction.addAction(new Notification$Action$Builder(n5, (CharSequence)"", broadcast2).build()).addAction(new Notification$Action$Builder(2131165421, (CharSequence)"", broadcast3).build());
            }
            this.mediaSession.setPlaybackState(this.playbackState.build());
            final MediaMetadata$Builder putString = new MediaMetadata$Builder().putBitmap("android.media.metadata.ALBUM_ART", bitmap).putString("android.media.metadata.ALBUM_ARTIST", musicAuthor).putLong("android.media.metadata.DURATION", n).putString("android.media.metadata.TITLE", musicTitle);
            String album2;
            if (audioInfo != null) {
                album2 = audioInfo.getAlbum();
            }
            else {
                album2 = null;
            }
            this.mediaSession.setMetadata(putString.putString("android.media.metadata.ALBUM", album2).build());
            notification$Builder.setVisibility(1);
            final Notification build = notification$Builder.build();
            if (ongoing) {
                this.startForeground(5, build);
            }
            else {
                this.stopForeground(false);
                ((NotificationManager)this.getSystemService("notification")).notify(5, build);
            }
        }
        else {
            final String s2 = "";
            final RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), 2131361831);
            RemoteViews remoteViews2;
            if (MusicPlayerService.supportBigNotifications) {
                remoteViews2 = new RemoteViews(this.getApplicationContext().getPackageName(), 2131361830);
            }
            else {
                remoteViews2 = null;
            }
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
            builder.setSmallIcon(2131165773);
            builder.setContentIntent(activity);
            builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            builder.setContentTitle(musicTitle);
            final Notification build2 = builder.build();
            build2.contentView = remoteViews;
            if (MusicPlayerService.supportBigNotifications) {
                build2.bigContentView = remoteViews2;
            }
            this.setListeners(remoteViews);
            if (MusicPlayerService.supportBigNotifications) {
                this.setListeners(remoteViews2);
            }
            if (largeIcon != null) {
                build2.contentView.setImageViewBitmap(2131230873, largeIcon);
                if (MusicPlayerService.supportBigNotifications) {
                    build2.bigContentView.setImageViewBitmap(2131230873, largeIcon);
                }
            }
            else {
                build2.contentView.setImageViewResource(2131230873, 2131165696);
                if (MusicPlayerService.supportBigNotifications) {
                    build2.bigContentView.setImageViewResource(2131230873, 2131165695);
                }
            }
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                build2.contentView.setViewVisibility(2131230878, 8);
                build2.contentView.setViewVisibility(2131230879, 8);
                build2.contentView.setViewVisibility(2131230877, 8);
                build2.contentView.setViewVisibility(2131230880, 8);
                build2.contentView.setViewVisibility(2131230881, 0);
                if (MusicPlayerService.supportBigNotifications) {
                    build2.bigContentView.setViewVisibility(2131230878, 8);
                    build2.bigContentView.setViewVisibility(2131230879, 8);
                    build2.bigContentView.setViewVisibility(2131230877, 8);
                    build2.bigContentView.setViewVisibility(2131230880, 8);
                    build2.bigContentView.setViewVisibility(2131230881, 0);
                }
            }
            else {
                build2.contentView.setViewVisibility(2131230881, 8);
                build2.contentView.setViewVisibility(2131230877, 0);
                build2.contentView.setViewVisibility(2131230880, 0);
                if (MusicPlayerService.supportBigNotifications) {
                    build2.bigContentView.setViewVisibility(2131230877, 0);
                    build2.bigContentView.setViewVisibility(2131230880, 0);
                    build2.bigContentView.setViewVisibility(2131230881, 8);
                }
                if (MediaController.getInstance().isMessagePaused()) {
                    build2.contentView.setViewVisibility(2131230878, 8);
                    build2.contentView.setViewVisibility(2131230879, 0);
                    if (MusicPlayerService.supportBigNotifications) {
                        build2.bigContentView.setViewVisibility(2131230878, 8);
                        build2.bigContentView.setViewVisibility(2131230879, 0);
                    }
                }
                else {
                    build2.contentView.setViewVisibility(2131230878, 0);
                    build2.contentView.setViewVisibility(2131230879, 8);
                    if (MusicPlayerService.supportBigNotifications) {
                        build2.bigContentView.setViewVisibility(2131230878, 0);
                        build2.bigContentView.setViewVisibility(2131230879, 8);
                    }
                }
            }
            build2.contentView.setTextViewText(2131230882, (CharSequence)musicTitle);
            build2.contentView.setTextViewText(2131230875, (CharSequence)musicAuthor);
            if (MusicPlayerService.supportBigNotifications) {
                build2.bigContentView.setTextViewText(2131230882, (CharSequence)musicTitle);
                build2.bigContentView.setTextViewText(2131230875, (CharSequence)musicAuthor);
                final RemoteViews bigContentView = build2.bigContentView;
                String album3 = s2;
                if (audioInfo != null) {
                    album3 = s2;
                    if (!TextUtils.isEmpty((CharSequence)audioInfo.getAlbum())) {
                        album3 = audioInfo.getAlbum();
                    }
                }
                bigContentView.setTextViewText(2131230874, (CharSequence)album3);
            }
            build2.flags |= 0x2;
            this.startForeground(5, build2);
        }
        if (this.remoteControlClient != null) {
            final int id = MediaController.getInstance().getPlayingMessageObject().getId();
            if (this.notificationMessageID != id) {
                this.notificationMessageID = id;
                final RemoteControlClient$MetadataEditor editMetadata = this.remoteControlClient.editMetadata(true);
                editMetadata.putString(2, musicAuthor);
                editMetadata.putString(7, musicTitle);
                if (audioInfo != null && !TextUtils.isEmpty((CharSequence)audioInfo.getAlbum())) {
                    editMetadata.putString(1, audioInfo.getAlbum());
                }
                editMetadata.putLong(9, MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000L);
                if (bitmap != null) {
                    try {
                        editMetadata.putBitmap(100, bitmap);
                    }
                    catch (Throwable t) {
                        FileLog.e(t);
                    }
                }
                editMetadata.apply();
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MusicPlayerService.this.remoteControlClient != null) {
                            if (MediaController.getInstance().getPlayingMessageObject() != null) {
                                if (MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration == -9223372036854775807L) {
                                    AndroidUtilities.runOnUIThread(this, 500L);
                                    return;
                                }
                                final RemoteControlClient$MetadataEditor editMetadata = MusicPlayerService.this.remoteControlClient.editMetadata(false);
                                editMetadata.putLong(9, MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000L);
                                editMetadata.apply();
                                final int sdk_INT = Build$VERSION.SDK_INT;
                                int playbackState = 2;
                                if (sdk_INT >= 18) {
                                    final RemoteControlClient access$100 = MusicPlayerService.this.remoteControlClient;
                                    if (!MediaController.getInstance().isMessagePaused()) {
                                        playbackState = 3;
                                    }
                                    final long max = Math.max(MediaController.getInstance().getPlayingMessageObject().audioProgressSec * 1000L, 100L);
                                    float n;
                                    if (MediaController.getInstance().isMessagePaused()) {
                                        n = 0.0f;
                                    }
                                    else {
                                        n = 1.0f;
                                    }
                                    access$100.setPlaybackState(playbackState, max, n);
                                }
                                else {
                                    final RemoteControlClient access$101 = MusicPlayerService.this.remoteControlClient;
                                    if (!MediaController.getInstance().isMessagePaused()) {
                                        playbackState = 3;
                                    }
                                    access$101.setPlaybackState(playbackState);
                                }
                            }
                        }
                    }
                }, 1000L);
            }
            int playbackState2 = 2;
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                this.remoteControlClient.setPlaybackState(8);
            }
            else {
                final RemoteControlClient$MetadataEditor editMetadata2 = this.remoteControlClient.editMetadata(false);
                editMetadata2.putLong(9, MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration * 1000L);
                editMetadata2.apply();
                if (Build$VERSION.SDK_INT >= 18) {
                    final RemoteControlClient remoteControlClient = this.remoteControlClient;
                    if (!MediaController.getInstance().isMessagePaused()) {
                        playbackState2 = 3;
                    }
                    final long max = Math.max(MediaController.getInstance().getPlayingMessageObject().audioProgressSec * 1000L, 100L);
                    float n6;
                    if (MediaController.getInstance().isMessagePaused()) {
                        n6 = 0.0f;
                    }
                    else {
                        n6 = 1.0f;
                    }
                    remoteControlClient.setPlaybackState(playbackState2, max, n6);
                }
                else {
                    final RemoteControlClient remoteControlClient2 = this.remoteControlClient;
                    if (!MediaController.getInstance().isMessagePaused()) {
                        playbackState2 = 3;
                    }
                    remoteControlClient2.setPlaybackState(playbackState2);
                }
            }
        }
    }
    
    private Bitmap loadArtworkFromUrl(String absolutePath, final boolean b, final boolean b2) {
        ImageLoader.getHttpFileName(absolutePath);
        final File httpFilePath = ImageLoader.getHttpFilePath(absolutePath, "jpg");
        if (httpFilePath.exists()) {
            absolutePath = httpFilePath.getAbsolutePath();
            float n = 600.0f;
            float n2;
            if (b) {
                n2 = 600.0f;
            }
            else {
                n2 = 100.0f;
            }
            if (!b) {
                n = 100.0f;
            }
            return ImageLoader.loadBitmap(absolutePath, null, n2, n, false);
        }
        if (b2) {
            this.loadingFilePath = httpFilePath.getAbsolutePath();
            if (!b) {
                this.imageReceiver.setImage(absolutePath, "48_48", null, null, 0);
            }
        }
        else {
            this.loadingFilePath = null;
        }
        return null;
    }
    
    private void updatePlaybackState(final long n) {
        if (Build$VERSION.SDK_INT < 21) {
            return;
        }
        final boolean b = MediaController.getInstance().isMessagePaused() ^ true;
        final boolean downloadingCurrentMessage = MediaController.getInstance().isDownloadingCurrentMessage();
        float n2 = 1.0f;
        if (downloadingCurrentMessage) {
            this.playbackState.setState(6, 0L, 1.0f).setActions(0L);
        }
        else {
            final PlaybackState$Builder playbackState = this.playbackState;
            int n3;
            if (b) {
                n3 = 3;
            }
            else {
                n3 = 2;
            }
            if (!b) {
                n2 = 0.0f;
            }
            playbackState.setState(n3, n, n2).setActions(822L);
        }
        this.mediaSession.setPlaybackState(this.playbackState.build());
    }
    
    public void didReceivedNotification(int n, final int n2, final Object... array) {
        if (n == NotificationCenter.messagePlayingPlayStateChanged) {
            final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                this.createNotification(playingMessageObject, false);
            }
            else {
                this.stopSelf();
            }
        }
        else if (n == NotificationCenter.messagePlayingDidSeek) {
            final MessageObject playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
            if (this.remoteControlClient != null && Build$VERSION.SDK_INT >= 18) {
                final long n3 = Math.round(playingMessageObject2.audioPlayerDuration * (float)array[1]);
                final RemoteControlClient remoteControlClient = this.remoteControlClient;
                if (MediaController.getInstance().isMessagePaused()) {
                    n = 2;
                }
                else {
                    n = 3;
                }
                float n4;
                if (MediaController.getInstance().isMessagePaused()) {
                    n4 = 0.0f;
                }
                else {
                    n4 = 1.0f;
                }
                remoteControlClient.setPlaybackState(n, n3 * 1000L, n4);
            }
        }
        else if (n == NotificationCenter.httpFileDidLoad) {
            final String anObject = (String)array[0];
            final MessageObject playingMessageObject3 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject3 != null) {
                final String loadingFilePath = this.loadingFilePath;
                if (loadingFilePath != null && loadingFilePath.equals(anObject)) {
                    this.createNotification(playingMessageObject3, false);
                }
            }
        }
        else if (n == NotificationCenter.fileDidLoad) {
            final String anObject2 = (String)array[0];
            final MessageObject playingMessageObject4 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject4 != null) {
                final String loadingFilePath2 = this.loadingFilePath;
                if (loadingFilePath2 != null && loadingFilePath2.equals(anObject2)) {
                    this.createNotification(playingMessageObject4, false);
                }
            }
        }
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onCreate() {
        this.audioManager = (AudioManager)this.getSystemService("audio");
        for (int i = 0; i < 3; ++i) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileDidLoad);
        }
        (this.imageReceiver = new ImageReceiver(null)).setDelegate((ImageReceiver.ImageReceiverDelegate)new _$$Lambda$MusicPlayerService$laWg3UUxrvXdIx91fvPuk_ss_Tg(this));
        if (Build$VERSION.SDK_INT >= 21) {
            this.mediaSession = new MediaSession((Context)this, "telegramAudioPlayer");
            this.playbackState = new PlaybackState$Builder();
            this.albumArtPlaceholder = Bitmap.createBitmap(AndroidUtilities.dp(102.0f), AndroidUtilities.dp(102.0f), Bitmap$Config.ARGB_8888);
            final Drawable drawable = this.getResources().getDrawable(2131165695);
            drawable.setBounds(0, 0, this.albumArtPlaceholder.getWidth(), this.albumArtPlaceholder.getHeight());
            drawable.draw(new Canvas(this.albumArtPlaceholder));
            this.mediaSession.setCallback((MediaSession$Callback)new MediaSession$Callback() {
                public void onPause() {
                    MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
                }
                
                public void onPlay() {
                    MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
                }
                
                public void onSeekTo(final long n) {
                    final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject != null) {
                        MediaController.getInstance().seekToProgress(playingMessageObject, n / 1000L / (float)playingMessageObject.getDuration());
                        MusicPlayerService.this.updatePlaybackState(n);
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
    
    @SuppressLint({ "NewApi" })
    public void onDestroy() {
        this.unregisterReceiver(this.headsetPlugReceiver);
        super.onDestroy();
        final RemoteControlClient remoteControlClient = this.remoteControlClient;
        if (remoteControlClient != null) {
            final RemoteControlClient$MetadataEditor editMetadata = remoteControlClient.editMetadata(true);
            editMetadata.clear();
            editMetadata.apply();
            this.audioManager.unregisterRemoteControlClient(this.remoteControlClient);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mediaSession.release();
        }
        for (int i = 0; i < 3; ++i) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileDidLoad);
        }
    }
    
    @SuppressLint({ "NewApi" })
    public int onStartCommand(Intent playingMessageObject, final int n, final int n2) {
        while (true) {
            if (playingMessageObject != null) {
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.getPackageName());
                    sb.append(".STOP_PLAYER");
                    if (sb.toString().equals(playingMessageObject.getAction())) {
                        MediaController.getInstance().cleanupPlayer(true, true);
                        return 2;
                    }
                    playingMessageObject = (Intent)MediaController.getInstance().getPlayingMessageObject();
                    if (playingMessageObject == null) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$C34ajmt2WsPXgqbkhgBzCpu6VDo(this));
                        return 1;
                    }
                    if (MusicPlayerService.supportLockScreenControls) {
                        final ComponentName component = new ComponentName(this.getApplicationContext(), MusicPlayerReceiver.class.getName());
                        try {
                            if (this.remoteControlClient == null) {
                                this.audioManager.registerMediaButtonEventReceiver(component);
                                final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                                intent.setComponent(component);
                                this.remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast((Context)this, 0, intent, 0));
                                this.audioManager.registerRemoteControlClient(this.remoteControlClient);
                            }
                            this.remoteControlClient.setTransportControlFlags(189);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    this.createNotification((MessageObject)playingMessageObject, false);
                }
                catch (Exception ex2) {
                    ex2.printStackTrace();
                }
                return 1;
            }
            continue;
        }
    }
    
    public void setListeners(final RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(2131230880, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.previous"), 134217728));
        remoteViews.setOnClickPendingIntent(2131230876, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.close"), 134217728));
        remoteViews.setOnClickPendingIntent(2131230878, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.pause"), 134217728));
        remoteViews.setOnClickPendingIntent(2131230877, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.next"), 134217728));
        remoteViews.setOnClickPendingIntent(2131230879, PendingIntent.getBroadcast(this.getApplicationContext(), 0, new Intent("org.telegram.android.musicplayer.play"), 134217728));
    }
}
