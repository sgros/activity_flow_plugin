package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.Notification.MediaStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.MetadataEditor;
import android.media.session.MediaSession;
import android.media.session.MediaSession.Callback;
import android.media.session.PlaybackState.Builder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.File;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.p004ui.LaunchActivity;

public class MusicPlayerService extends Service implements NotificationCenterDelegate {
    private static final int ID_NOTIFICATION = 5;
    public static final String NOTIFY_CLOSE = "org.telegram.android.musicplayer.close";
    public static final String NOTIFY_NEXT = "org.telegram.android.musicplayer.next";
    public static final String NOTIFY_PAUSE = "org.telegram.android.musicplayer.pause";
    public static final String NOTIFY_PLAY = "org.telegram.android.musicplayer.play";
    public static final String NOTIFY_PREVIOUS = "org.telegram.android.musicplayer.previous";
    public static final String NOTIFY_SEEK = "org.telegram.android.musicplayer.seek";
    private static boolean supportBigNotifications = (VERSION.SDK_INT >= 16);
    private static boolean supportLockScreenControls;
    private Bitmap albumArtPlaceholder;
    private AudioManager audioManager;
    private BroadcastReceiver headsetPlugReceiver = new C10491();
    private ImageReceiver imageReceiver;
    private String loadingFilePath;
    private MediaSession mediaSession;
    private int notificationMessageID;
    private Builder playbackState;
    private RemoteControlClient remoteControlClient;

    /* renamed from: org.telegram.messenger.MusicPlayerService$1 */
    class C10491 extends BroadcastReceiver {
        C10491() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.media.AUDIO_BECOMING_NOISY".equals(intent.getAction())) {
                MediaController.getInstance().lambda$startAudioAgain$5$MediaController(MediaController.getInstance().getPlayingMessageObject());
            }
        }
    }

    /* renamed from: org.telegram.messenger.MusicPlayerService$2 */
    class C10502 extends Callback {
        public void onStop() {
        }

        C10502() {
        }

        public void onPlay() {
            MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
        }

        public void onPause() {
            MediaController.getInstance().lambda$startAudioAgain$5$MediaController(MediaController.getInstance().getPlayingMessageObject());
        }

        public void onSkipToNext() {
            MediaController.getInstance().playNextMessage();
        }

        public void onSkipToPrevious() {
            MediaController.getInstance().playPreviousMessage();
        }

        public void onSeekTo(long j) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                MediaController.getInstance().seekToProgress(playingMessageObject, ((float) (j / 1000)) / ((float) playingMessageObject.getDuration()));
                MusicPlayerService.this.updatePlaybackState(j);
            }
        }
    }

    /* renamed from: org.telegram.messenger.MusicPlayerService$3 */
    class C10513 implements Runnable {
        C10513() {
        }

        public void run() {
            if (!(MusicPlayerService.this.remoteControlClient == null || MediaController.getInstance().getPlayingMessageObject() == null)) {
                if (((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) == -9223372036854775807L) {
                    AndroidUtilities.runOnUIThread(this, 500);
                    return;
                }
                MetadataEditor editMetadata = MusicPlayerService.this.remoteControlClient.editMetadata(false);
                editMetadata.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * 1000);
                editMetadata.apply();
                int i = 2;
                RemoteControlClient access$100;
                if (VERSION.SDK_INT >= 18) {
                    access$100 = MusicPlayerService.this.remoteControlClient;
                    if (!MediaController.getInstance().isMessagePaused()) {
                        i = 3;
                    }
                    access$100.setPlaybackState(i, Math.max(((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, 100), MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
                } else {
                    access$100 = MusicPlayerService.this.remoteControlClient;
                    if (!MediaController.getInstance().isMessagePaused()) {
                        i = 3;
                    }
                    access$100.setPlaybackState(i);
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    static {
        boolean z = true;
        if (VERSION.SDK_INT >= 21 && TextUtils.isEmpty(AndroidUtilities.getSystemProperty("ro.miui.ui.version.code"))) {
            z = false;
        }
        supportLockScreenControls = z;
    }

    public void onCreate() {
        this.audioManager = (AudioManager) getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        for (int i = 0; i < 3; i++) {
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.fileDidLoad);
        }
        this.imageReceiver = new ImageReceiver(null);
        this.imageReceiver.setDelegate(new C3530-$$Lambda$MusicPlayerService$laWg3UUxrvXdIx91fvPuk-ss-Tg(this));
        if (VERSION.SDK_INT >= 21) {
            this.mediaSession = new MediaSession(this, "telegramAudioPlayer");
            this.playbackState = new Builder();
            this.albumArtPlaceholder = Bitmap.createBitmap(AndroidUtilities.m26dp(102.0f), AndroidUtilities.m26dp(102.0f), Config.ARGB_8888);
            Drawable drawable = getResources().getDrawable(C1067R.C1065drawable.nocover_big);
            drawable.setBounds(0, 0, this.albumArtPlaceholder.getWidth(), this.albumArtPlaceholder.getHeight());
            drawable.draw(new Canvas(this.albumArtPlaceholder));
            this.mediaSession.setCallback(new C10502());
            this.mediaSession.setActive(true);
        }
        registerReceiver(this.headsetPlugReceiver, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
        super.onCreate();
    }

    public /* synthetic */ void lambda$onCreate$0$MusicPlayerService(ImageReceiver imageReceiver, boolean z, boolean z2) {
        if (z && !TextUtils.isEmpty(this.loadingFilePath)) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                createNotification(playingMessageObject, true);
            }
            this.loadingFilePath = null;
        }
    }

    @SuppressLint({"NewApi"})
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(getPackageName());
                stringBuilder.append(".STOP_PLAYER");
                if (stringBuilder.toString().equals(intent.getAction())) {
                    MediaController.getInstance().cleanupPlayer(true, true);
                    return 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject == null) {
            AndroidUtilities.runOnUIThread(new C0282-$$Lambda$C34ajmt2WsPXgqbkhgBzCpu6VDo(this));
            return 1;
        }
        if (supportLockScreenControls) {
            ComponentName componentName = new ComponentName(getApplicationContext(), MusicPlayerReceiver.class.getName());
            try {
                if (this.remoteControlClient == null) {
                    this.audioManager.registerMediaButtonEventReceiver(componentName);
                    Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON");
                    intent2.setComponent(componentName);
                    this.remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(this, 0, intent2, 0));
                    this.audioManager.registerRemoteControlClient(this.remoteControlClient);
                }
                this.remoteControlClient.setTransportControlFlags(189);
            } catch (Exception e2) {
                FileLog.m30e(e2);
            }
        }
        createNotification(playingMessageObject, false);
        return 1;
    }

    private Bitmap loadArtworkFromUrl(String str, boolean z, boolean z2) {
        ImageLoader.getHttpFileName(str);
        File httpFilePath = ImageLoader.getHttpFilePath(str, "jpg");
        if (httpFilePath.exists()) {
            str = httpFilePath.getAbsolutePath();
            float f = 600.0f;
            float f2 = z ? 600.0f : 100.0f;
            if (!z) {
                f = 100.0f;
            }
            return ImageLoader.loadBitmap(str, null, f2, f, false);
        }
        if (z2) {
            this.loadingFilePath = httpFilePath.getAbsolutePath();
            if (!z) {
                this.imageReceiver.setImage(str, "48_48", null, null, 0);
            }
        } else {
            this.loadingFilePath = null;
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    private void createNotification(MessageObject messageObject, boolean z) {
        int isMessagePaused;
        MessageObject messageObject2 = messageObject;
        String musicTitle = messageObject.getMusicTitle();
        String musicAuthor = messageObject.getMusicAuthor();
        AudioInfo audioInfo = MediaController.getInstance().getAudioInfo();
        Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
        intent.setAction("com.tmessages.openplayer");
        intent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 0);
        String artworkUrl = messageObject2.getArtworkUrl(true);
        String artworkUrl2 = messageObject2.getArtworkUrl(false);
        long duration = (long) (messageObject.getDuration() * 1000);
        Bitmap smallCover = audioInfo != null ? audioInfo.getSmallCover() : null;
        Bitmap cover = audioInfo != null ? audioInfo.getCover() : null;
        this.loadingFilePath = null;
        this.imageReceiver.setImageBitmap(null);
        if (smallCover != null || TextUtils.isEmpty(artworkUrl)) {
            this.loadingFilePath = FileLoader.getPathToAttach(messageObject.getDocument()).getAbsolutePath();
        } else {
            cover = loadArtworkFromUrl(artworkUrl2, true, z ^ 1);
            if (cover == null) {
                smallCover = loadArtworkFromUrl(artworkUrl, false, z ^ 1);
                cover = smallCover;
            } else {
                smallCover = loadArtworkFromUrl(artworkUrl2, false, z ^ 1);
            }
        }
        Bitmap bitmap = smallCover;
        String str = "";
        Notification build;
        if (VERSION.SDK_INT >= 21) {
            isMessagePaused = MediaController.getInstance().isMessagePaused() ^ 1;
            PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
            Context applicationContext = getApplicationContext();
            Intent intent2 = new Intent(this, MusicPlayerService.class);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getPackageName());
            stringBuilder.append(".STOP_PLAYER");
            PendingIntent service = PendingIntent.getService(applicationContext, 0, intent2.setAction(stringBuilder.toString()), 268435456);
            PendingIntent broadcast2 = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(isMessagePaused != 0 ? NOTIFY_PAUSE : NOTIFY_PLAY).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
            long j = duration;
            PendingIntent broadcast3 = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
            PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_SEEK).setComponent(new ComponentName(this, MusicPlayerReceiver.class)), 268435456);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(C1067R.C1065drawable.player).setOngoing(isMessagePaused).setContentTitle(musicTitle).setContentText(musicAuthor).setSubText(audioInfo != null ? audioInfo.getAlbum() : null).setContentIntent(activity).setDeleteIntent(service).setShowWhen(false).setCategory("transport").setPriority(2).setStyle(new MediaStyle().setMediaSession(this.mediaSession.getSessionToken()).setShowActionsInCompactView(new int[]{0, 1, 2}));
            if (VERSION.SDK_INT >= 26) {
                NotificationsController.checkOtherNotificationsChannel();
                builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            }
            if (bitmap != null) {
                builder.setLargeIcon(bitmap);
            } else {
                builder.setLargeIcon(this.albumArtPlaceholder);
            }
            String str2;
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                String str3 = str;
                this.playbackState.setState(6, 0, 1.0f).setActions(0);
                str2 = str3;
                builder.addAction(new Action.Builder(C1067R.C1065drawable.ic_action_previous, str2, broadcast).build()).addAction(new Action.Builder(C1067R.C1065drawable.loading_animation2, str2, null).build()).addAction(new Action.Builder(C1067R.C1065drawable.ic_action_next, str2, broadcast3).build());
            } else {
                str2 = str;
                this.playbackState.setState(isMessagePaused != 0 ? 3 : 2, ((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, isMessagePaused != 0 ? 1.0f : 0.0f).setActions(822);
                builder.addAction(new Action.Builder(C1067R.C1065drawable.ic_action_previous, str2, broadcast).build()).addAction(new Action.Builder(isMessagePaused != 0 ? C1067R.C1065drawable.ic_action_pause : C1067R.C1065drawable.ic_action_play, str2, broadcast2).build()).addAction(new Action.Builder(C1067R.C1065drawable.ic_action_next, str2, broadcast3).build());
            }
            this.mediaSession.setPlaybackState(this.playbackState.build());
            this.mediaSession.setMetadata(new MediaMetadata.Builder().putBitmap("android.media.metadata.ALBUM_ART", cover).putString("android.media.metadata.ALBUM_ARTIST", musicAuthor).putLong("android.media.metadata.DURATION", j).putString("android.media.metadata.TITLE", musicTitle).putString("android.media.metadata.ALBUM", audioInfo != null ? audioInfo.getAlbum() : null).build());
            builder.setVisibility(1);
            build = builder.build();
            if (isMessagePaused != 0) {
                startForeground(5, build);
            } else {
                stopForeground(false);
                ((NotificationManager) getSystemService("notification")).notify(5, build);
            }
        } else {
            smallCover = bitmap;
            CharSequence charSequence = str;
            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), C1067R.layout.player_small_notification);
            RemoteViews remoteViews2 = supportBigNotifications ? new RemoteViews(getApplicationContext().getPackageName(), C1067R.layout.player_big_notification) : null;
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(getApplicationContext());
            builder2.setSmallIcon(C1067R.C1065drawable.player);
            builder2.setContentIntent(activity);
            builder2.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            builder2.setContentTitle(musicTitle);
            build = builder2.build();
            build.contentView = remoteViews;
            if (supportBigNotifications) {
                build.bigContentView = remoteViews2;
            }
            setListeners(remoteViews);
            if (supportBigNotifications) {
                setListeners(remoteViews2);
            }
            if (smallCover != null) {
                build.contentView.setImageViewBitmap(C1067R.C1066id.player_album_art, smallCover);
                if (supportBigNotifications) {
                    build.bigContentView.setImageViewBitmap(C1067R.C1066id.player_album_art, smallCover);
                }
            } else {
                build.contentView.setImageViewResource(C1067R.C1066id.player_album_art, C1067R.C1065drawable.nocover_small);
                if (supportBigNotifications) {
                    build.bigContentView.setImageViewResource(C1067R.C1066id.player_album_art, C1067R.C1065drawable.nocover_big);
                }
            }
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                build.contentView.setViewVisibility(C1067R.C1066id.player_pause, 8);
                build.contentView.setViewVisibility(C1067R.C1066id.player_play, 8);
                build.contentView.setViewVisibility(C1067R.C1066id.player_next, 8);
                build.contentView.setViewVisibility(C1067R.C1066id.player_previous, 8);
                build.contentView.setViewVisibility(C1067R.C1066id.player_progress_bar, 0);
                if (supportBigNotifications) {
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_pause, 8);
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_play, 8);
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_next, 8);
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_previous, 8);
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_progress_bar, 0);
                }
            } else {
                int i;
                build.contentView.setViewVisibility(C1067R.C1066id.player_progress_bar, 8);
                build.contentView.setViewVisibility(C1067R.C1066id.player_next, 0);
                build.contentView.setViewVisibility(C1067R.C1066id.player_previous, 0);
                if (supportBigNotifications) {
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_next, 0);
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_previous, 0);
                    i = 8;
                    build.bigContentView.setViewVisibility(C1067R.C1066id.player_progress_bar, 8);
                } else {
                    i = 8;
                }
                if (MediaController.getInstance().isMessagePaused()) {
                    build.contentView.setViewVisibility(C1067R.C1066id.player_pause, i);
                    build.contentView.setViewVisibility(C1067R.C1066id.player_play, 0);
                    if (supportBigNotifications) {
                        build.bigContentView.setViewVisibility(C1067R.C1066id.player_pause, i);
                        build.bigContentView.setViewVisibility(C1067R.C1066id.player_play, 0);
                    }
                } else {
                    build.contentView.setViewVisibility(C1067R.C1066id.player_pause, 0);
                    build.contentView.setViewVisibility(C1067R.C1066id.player_play, i);
                    if (supportBigNotifications) {
                        build.bigContentView.setViewVisibility(C1067R.C1066id.player_pause, 0);
                        build.bigContentView.setViewVisibility(C1067R.C1066id.player_play, i);
                    }
                }
            }
            build.contentView.setTextViewText(C1067R.C1066id.player_song_name, musicTitle);
            build.contentView.setTextViewText(C1067R.C1066id.player_author_name, musicAuthor);
            if (supportBigNotifications) {
                build.bigContentView.setTextViewText(C1067R.C1066id.player_song_name, musicTitle);
                build.bigContentView.setTextViewText(C1067R.C1066id.player_author_name, musicAuthor);
                remoteViews = build.bigContentView;
                if (!(audioInfo == null || TextUtils.isEmpty(audioInfo.getAlbum()))) {
                    charSequence = audioInfo.getAlbum();
                }
                remoteViews.setTextViewText(C1067R.C1066id.player_album_title, charSequence);
            }
            build.flags |= 2;
            startForeground(5, build);
        }
        if (this.remoteControlClient != null) {
            int i2;
            isMessagePaused = MediaController.getInstance().getPlayingMessageObject().getId();
            if (this.notificationMessageID != isMessagePaused) {
                this.notificationMessageID = isMessagePaused;
                MetadataEditor editMetadata = this.remoteControlClient.editMetadata(true);
                i2 = 2;
                editMetadata.putString(2, musicAuthor);
                editMetadata.putString(7, musicTitle);
                if (!(audioInfo == null || TextUtils.isEmpty(audioInfo.getAlbum()))) {
                    editMetadata.putString(1, audioInfo.getAlbum());
                }
                editMetadata.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * 1000);
                if (cover != null) {
                    try {
                        editMetadata.putBitmap(100, cover);
                    } catch (Throwable th) {
                        FileLog.m30e(th);
                    }
                }
                editMetadata.apply();
                AndroidUtilities.runOnUIThread(new C10513(), 1000);
            } else {
                i2 = 2;
            }
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                this.remoteControlClient.setPlaybackState(8);
                return;
            }
            MetadataEditor editMetadata2 = this.remoteControlClient.editMetadata(false);
            editMetadata2.putLong(9, ((long) MediaController.getInstance().getPlayingMessageObject().audioPlayerDuration) * 1000);
            editMetadata2.apply();
            RemoteControlClient remoteControlClient;
            if (VERSION.SDK_INT >= 18) {
                remoteControlClient = this.remoteControlClient;
                if (!MediaController.getInstance().isMessagePaused()) {
                    i2 = 3;
                }
                remoteControlClient.setPlaybackState(i2, Math.max(((long) MediaController.getInstance().getPlayingMessageObject().audioProgressSec) * 1000, 100), MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
                return;
            }
            remoteControlClient = this.remoteControlClient;
            if (!MediaController.getInstance().isMessagePaused()) {
                i2 = 3;
            }
            remoteControlClient.setPlaybackState(i2);
        }
    }

    private void updatePlaybackState(long j) {
        if (VERSION.SDK_INT >= 21) {
            int isMessagePaused = MediaController.getInstance().isMessagePaused() ^ 1;
            float f = 1.0f;
            if (MediaController.getInstance().isDownloadingCurrentMessage()) {
                this.playbackState.setState(6, 0, 1.0f).setActions(0);
            } else {
                Builder builder = this.playbackState;
                int i = isMessagePaused != 0 ? 3 : 2;
                if (isMessagePaused == 0) {
                    f = 0.0f;
                }
                builder.setState(i, j, f).setActions(822);
            }
            this.mediaSession.setPlaybackState(this.playbackState.build());
        }
    }

    public void setListeners(RemoteViews remoteViews) {
        remoteViews.setOnClickPendingIntent(C1067R.C1066id.player_previous, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PREVIOUS), 134217728));
        remoteViews.setOnClickPendingIntent(C1067R.C1066id.player_close, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_CLOSE), 134217728));
        remoteViews.setOnClickPendingIntent(C1067R.C1066id.player_pause, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PAUSE), 134217728));
        remoteViews.setOnClickPendingIntent(C1067R.C1066id.player_next, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_NEXT), 134217728));
        remoteViews.setOnClickPendingIntent(C1067R.C1066id.player_play, PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(NOTIFY_PLAY), 134217728));
    }

    @SuppressLint({"NewApi"})
    public void onDestroy() {
        unregisterReceiver(this.headsetPlugReceiver);
        super.onDestroy();
        RemoteControlClient remoteControlClient = this.remoteControlClient;
        if (remoteControlClient != null) {
            MetadataEditor editMetadata = remoteControlClient.editMetadata(true);
            editMetadata.clear();
            editMetadata.apply();
            this.audioManager.unregisterRemoteControlClient(this.remoteControlClient);
        }
        if (VERSION.SDK_INT >= 21) {
            this.mediaSession.release();
        }
        for (int i = 0; i < 3; i++) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingDidSeek);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.httpFileDidLoad);
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.fileDidLoad);
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        MessageObject playingMessageObject;
        String str;
        MessageObject playingMessageObject2;
        String str2;
        if (i == NotificationCenter.messagePlayingPlayStateChanged) {
            playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                createNotification(playingMessageObject, false);
            } else {
                stopSelf();
            }
        } else if (i == NotificationCenter.messagePlayingDidSeek) {
            playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (this.remoteControlClient != null && VERSION.SDK_INT >= 18) {
                this.remoteControlClient.setPlaybackState(MediaController.getInstance().isMessagePaused() ? 2 : 3, ((long) Math.round(((float) playingMessageObject.audioPlayerDuration) * ((Float) objArr[1]).floatValue())) * 1000, MediaController.getInstance().isMessagePaused() ? 0.0f : 1.0f);
            }
        } else if (i == NotificationCenter.httpFileDidLoad) {
            str = (String) objArr[0];
            playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject2 != null) {
                str2 = this.loadingFilePath;
                if (str2 != null && str2.equals(str)) {
                    createNotification(playingMessageObject2, false);
                }
            }
        } else if (i == NotificationCenter.fileDidLoad) {
            str = (String) objArr[0];
            playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject2 != null) {
                str2 = this.loadingFilePath;
                if (str2 != null && str2.equals(str)) {
                    createNotification(playingMessageObject2, false);
                }
            }
        }
    }
}
