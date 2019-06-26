// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.os.Message;
import java.lang.ref.WeakReference;
import android.os.Handler;
import android.os.Process;
import android.service.media.MediaBrowserService$BrowserRoot;
import android.os.Bundle;
import android.app.PendingIntent;
import org.telegram.ui.LaunchActivity;
import android.media.session.MediaSession$Callback;
import android.content.Context;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.tgnet.AbstractSerializedData;
import android.text.TextUtils;
import java.util.Locale;
import android.os.SystemClock;
import android.media.session.PlaybackState$Builder;
import android.net.Uri;
import org.telegram.tgnet.TLObject;
import android.media.MediaDescription$Builder;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import android.service.media.MediaBrowserService$Result;
import org.telegram.messenger.audioinfo.AudioInfo;
import android.media.MediaMetadata$Builder;
import android.content.Intent;
import android.graphics.Shader;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import android.graphics.Bitmap;
import java.io.File;
import android.graphics.Paint;
import android.media.session.MediaSession$QueueItem;
import android.media.session.MediaSession;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import android.graphics.RectF;
import android.annotation.TargetApi;
import android.service.media.MediaBrowserService;

@TargetApi(21)
public class MusicBrowserService extends MediaBrowserService implements NotificationCenterDelegate
{
    public static final String ACTION_CMD = "com.example.android.mediabrowserservice.ACTION_CMD";
    public static final String CMD_NAME = "CMD_NAME";
    public static final String CMD_PAUSE = "CMD_PAUSE";
    private static final String MEDIA_ID_ROOT = "__ROOT__";
    private static final String SLOT_RESERVATION_QUEUE = "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_QUEUE";
    private static final String SLOT_RESERVATION_SKIP_TO_NEXT = "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_NEXT";
    private static final String SLOT_RESERVATION_SKIP_TO_PREV = "com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_PREVIOUS";
    private static final int STOP_DELAY = 30000;
    private RectF bitmapRect;
    private SparseArray<TLRPC.Chat> chats;
    private boolean chatsLoaded;
    private int currentAccount;
    private DelayedStopHandler delayedStopHandler;
    private ArrayList<Integer> dialogs;
    private int lastSelectedDialog;
    private boolean loadingChats;
    private MediaSession mediaSession;
    private SparseArray<ArrayList<MessageObject>> musicObjects;
    private SparseArray<ArrayList<MediaSession$QueueItem>> musicQueues;
    private Paint roundPaint;
    private boolean serviceStarted;
    private SparseArray<TLRPC.User> users;
    
    public MusicBrowserService() {
        this.currentAccount = UserConfig.selectedAccount;
        this.dialogs = new ArrayList<Integer>();
        this.users = (SparseArray<TLRPC.User>)new SparseArray();
        this.chats = (SparseArray<TLRPC.Chat>)new SparseArray();
        this.musicObjects = (SparseArray<ArrayList<MessageObject>>)new SparseArray();
        this.musicQueues = (SparseArray<ArrayList<MediaSession$QueueItem>>)new SparseArray();
        this.delayedStopHandler = new DelayedStopHandler(this);
    }
    
    private Bitmap createRoundBitmap(final File file) {
        try {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inSampleSize = 2;
            final Bitmap decodeFile = BitmapFactory.decodeFile(file.toString(), bitmapFactory$Options);
            if (decodeFile != null) {
                final Bitmap bitmap = Bitmap.createBitmap(decodeFile.getWidth(), decodeFile.getHeight(), Bitmap$Config.ARGB_8888);
                bitmap.eraseColor(0);
                final Canvas canvas = new Canvas(bitmap);
                final BitmapShader shader = new BitmapShader(decodeFile, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
                if (this.roundPaint == null) {
                    this.roundPaint = new Paint(1);
                    this.bitmapRect = new RectF();
                }
                this.roundPaint.setShader((Shader)shader);
                this.bitmapRect.set(0.0f, 0.0f, (float)decodeFile.getWidth(), (float)decodeFile.getHeight());
                canvas.drawRoundRect(this.bitmapRect, (float)decodeFile.getWidth(), (float)decodeFile.getHeight(), this.roundPaint);
                return bitmap;
            }
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
        return null;
    }
    
    private long getAvailableActions() {
        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        long n2;
        long n = n2 = 3076L;
        if (playingMessageObject != null) {
            if (!MediaController.getInstance().isMessagePaused()) {
                n = 3078L;
            }
            n2 = (n | 0x10L | 0x20L);
        }
        return n2;
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
                this.startService(new Intent(this.getApplicationContext(), (Class)MusicBrowserService.class));
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
            this.serviceStarted = true;
        }
        if (!this.mediaSession.isActive()) {
            this.mediaSession.setActive(true);
        }
        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject == null) {
            return;
        }
        final MediaMetadata$Builder mediaMetadata$Builder = new MediaMetadata$Builder();
        mediaMetadata$Builder.putLong("android.media.metadata.DURATION", (long)(playingMessageObject.getDuration() * 1000));
        mediaMetadata$Builder.putString("android.media.metadata.ARTIST", playingMessageObject.getMusicAuthor());
        mediaMetadata$Builder.putString("android.media.metadata.TITLE", playingMessageObject.getMusicTitle());
        final AudioInfo audioInfo = MediaController.getInstance().getAudioInfo();
        if (audioInfo != null) {
            final Bitmap cover = audioInfo.getCover();
            if (cover != null) {
                mediaMetadata$Builder.putBitmap("android.media.metadata.ALBUM_ART", cover);
            }
        }
        this.mediaSession.setMetadata(mediaMetadata$Builder.build());
    }
    
    private void handleStopRequest(final String s) {
        this.delayedStopHandler.removeCallbacksAndMessages((Object)null);
        this.delayedStopHandler.sendEmptyMessageDelayed(0, 30000L);
        this.updatePlaybackState(s);
        this.stopSelf();
        this.serviceStarted = false;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
    }
    
    private void loadChildrenImpl(final String anObject, final MediaBrowserService$Result<List<MediaBrowser$MediaItem>> mediaBrowserService$Result) {
        final ArrayList<MediaBrowser$MediaItem> list = new ArrayList<MediaBrowser$MediaItem>();
        final boolean equals = "__ROOT__".equals(anObject);
        int i = 0;
        int j = 0;
        if (equals) {
            while (j < this.dialogs.size()) {
                final int intValue = this.dialogs.get(j);
                final MediaDescription$Builder mediaDescription$Builder = new MediaDescription$Builder();
                final StringBuilder sb = new StringBuilder();
                sb.append("__CHAT_");
                sb.append(intValue);
                final MediaDescription$Builder setMediaId = mediaDescription$Builder.setMediaId(sb.toString());
                Bitmap bitmap = null;
                TLRPC.FileLocation fileLocation = null;
                Label_0240: {
                    if (intValue > 0) {
                        final TLRPC.User user = (TLRPC.User)this.users.get(intValue);
                        if (user != null) {
                            setMediaId.setTitle((CharSequence)ContactsController.formatName(user.first_name, user.last_name));
                            final TLRPC.UserProfilePhoto photo = user.photo;
                            if (photo != null) {
                                fileLocation = photo.photo_small;
                                if (!(fileLocation instanceof TLRPC.TL_fileLocationUnavailable)) {
                                    break Label_0240;
                                }
                            }
                        }
                        else {
                            setMediaId.setTitle((CharSequence)"DELETED USER");
                        }
                    }
                    else {
                        final TLRPC.Chat chat = (TLRPC.Chat)this.chats.get(-intValue);
                        if (chat != null) {
                            setMediaId.setTitle((CharSequence)chat.title);
                            final TLRPC.ChatPhoto photo2 = chat.photo;
                            if (photo2 != null) {
                                fileLocation = photo2.photo_small;
                                if (!(fileLocation instanceof TLRPC.TL_fileLocationUnavailable)) {
                                    break Label_0240;
                                }
                            }
                        }
                        else {
                            setMediaId.setTitle((CharSequence)"DELETED CHAT");
                        }
                    }
                    fileLocation = null;
                }
                if (fileLocation != null) {
                    final Bitmap roundBitmap = this.createRoundBitmap(FileLoader.getPathToAttach(fileLocation, true));
                    if ((bitmap = roundBitmap) != null) {
                        setMediaId.setIconBitmap(roundBitmap);
                        bitmap = roundBitmap;
                    }
                }
                if (fileLocation == null || bitmap == null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("android.resource://");
                    sb2.append(this.getApplicationContext().getPackageName());
                    sb2.append("/drawable/contact_blue");
                    setMediaId.setIconUri(Uri.parse(sb2.toString()));
                }
                list.add(new MediaBrowser$MediaItem(setMediaId.build(), 1));
                ++j;
            }
        }
        else if (anObject != null && anObject.startsWith("__CHAT_")) {
            int int1;
            try {
                int1 = Integer.parseInt(anObject.replace("__CHAT_", ""));
            }
            catch (Exception ex) {
                FileLog.e(ex);
                int1 = 0;
            }
            final ArrayList list2 = (ArrayList)this.musicObjects.get(int1);
            if (list2 != null) {
                while (i < list2.size()) {
                    final MessageObject messageObject = list2.get(i);
                    final MediaDescription$Builder mediaDescription$Builder2 = new MediaDescription$Builder();
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(int1);
                    sb3.append("_");
                    sb3.append(i);
                    final MediaDescription$Builder setMediaId2 = mediaDescription$Builder2.setMediaId(sb3.toString());
                    setMediaId2.setTitle((CharSequence)messageObject.getMusicTitle());
                    setMediaId2.setSubtitle((CharSequence)messageObject.getMusicAuthor());
                    list.add(new MediaBrowser$MediaItem(setMediaId2.build(), 2));
                    ++i;
                }
            }
        }
        mediaBrowserService$Result.sendResult((Object)list);
    }
    
    private void updatePlaybackState(final String errorMessage) {
        final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        long n;
        if (playingMessageObject != null) {
            n = playingMessageObject.audioProgressSec * 1000L;
        }
        else {
            n = -1L;
        }
        final PlaybackState$Builder setActions = new PlaybackState$Builder().setActions(this.getAvailableActions());
        int n2;
        if (playingMessageObject == null) {
            n2 = 1;
        }
        else if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            n2 = 6;
        }
        else if (MediaController.getInstance().isMessagePaused()) {
            n2 = 2;
        }
        else {
            n2 = 3;
        }
        if (errorMessage != null) {
            setActions.setErrorMessage((CharSequence)errorMessage);
            n2 = 7;
        }
        setActions.setState(n2, n, 1.0f, SystemClock.elapsedRealtime());
        if (playingMessageObject != null) {
            setActions.setActiveQueueItemId((long)MediaController.getInstance().getPlayingMessageObjectNum());
        }
        else {
            setActions.setActiveQueueItemId(0L);
        }
        this.mediaSession.setPlaybackState(setActions.build());
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        this.updatePlaybackState(null);
        this.handlePlayRequest();
    }
    
    public void onCreate() {
        super.onCreate();
        ApplicationLoader.postInitApplication();
        this.lastSelectedDialog = MessagesController.getNotificationsSettings(this.currentAccount).getInt("auto_lastSelectedDialog", 0);
        this.mediaSession = new MediaSession((Context)this, "MusicService");
        this.setSessionToken(this.mediaSession.getSessionToken());
        this.mediaSession.setCallback((MediaSession$Callback)new MediaSessionCallback());
        this.mediaSession.setFlags(3);
        final Context applicationContext = this.getApplicationContext();
        this.mediaSession.setSessionActivity(PendingIntent.getActivity(applicationContext, 99, new Intent(applicationContext, (Class)LaunchActivity.class), 134217728));
        final Bundle extras = new Bundle();
        extras.putBoolean("com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_QUEUE", true);
        extras.putBoolean("com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_PREVIOUS", true);
        extras.putBoolean("com.google.android.gms.car.media.ALWAYS_RESERVE_SPACE_FOR.ACTION_SKIP_TO_NEXT", true);
        this.mediaSession.setExtras(extras);
        this.updatePlaybackState(null);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
    }
    
    public void onDestroy() {
        this.handleStopRequest(null);
        this.delayedStopHandler.removeCallbacksAndMessages((Object)null);
        this.mediaSession.release();
    }
    
    public MediaBrowserService$BrowserRoot onGetRoot(final String s, final int n, final Bundle bundle) {
        if (s != null && (1000 == n || Process.myUid() == n || s.equals("com.google.android.mediasimulator") || s.equals("com.google.android.projection.gearhead"))) {
            return new MediaBrowserService$BrowserRoot("__ROOT__", (Bundle)null);
        }
        return null;
    }
    
    public void onLoadChildren(final String s, final MediaBrowserService$Result<List<MediaBrowser$MediaItem>> mediaBrowserService$Result) {
        if (!this.chatsLoaded) {
            mediaBrowserService$Result.detach();
            if (this.loadingChats) {
                return;
            }
            this.loadingChats = true;
            final MessagesStorage instance = MessagesStorage.getInstance(this.currentAccount);
            instance.getStorageQueue().postRunnable(new _$$Lambda$MusicBrowserService$iS7bPWX5pXtbCNrWxnzS_j3JBYQ(this, instance, s, mediaBrowserService$Result));
        }
        else {
            this.loadChildrenImpl(s, mediaBrowserService$Result);
        }
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        return 1;
    }
    
    private static class DelayedStopHandler extends Handler
    {
        private final WeakReference<MusicBrowserService> mWeakReference;
        
        private DelayedStopHandler(final MusicBrowserService referent) {
            this.mWeakReference = new WeakReference<MusicBrowserService>(referent);
        }
        
        public void handleMessage(final Message message) {
            final MusicBrowserService musicBrowserService = this.mWeakReference.get();
            if (musicBrowserService != null) {
                if (MediaController.getInstance().getPlayingMessageObject() != null && !MediaController.getInstance().isMessagePaused()) {
                    return;
                }
                musicBrowserService.stopSelf();
                musicBrowserService.serviceStarted = false;
            }
        }
    }
    
    private final class MediaSessionCallback extends MediaSession$Callback
    {
        public void onPause() {
            MusicBrowserService.this.handlePauseRequest();
        }
        
        public void onPlay() {
            final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(MusicBrowserService.this.lastSelectedDialog);
                sb.append("_");
                sb.append(0);
                this.onPlayFromMediaId(sb.toString(), null);
            }
            else {
                MediaController.getInstance().playMessage(playingMessageObject);
            }
        }
        
        public void onPlayFromMediaId(final String s, final Bundle bundle) {
            final String[] split = s.split("_");
            if (split.length != 2) {
                return;
            }
            try {
                final int int1 = Integer.parseInt(split[0]);
                final int int2 = Integer.parseInt(split[1]);
                final ArrayList list = (ArrayList)MusicBrowserService.this.musicObjects.get(int1);
                final ArrayList queue = (ArrayList)MusicBrowserService.this.musicQueues.get(int1);
                if (list == null || int2 < 0 || int2 >= list.size()) {
                    return;
                }
                MusicBrowserService.this.lastSelectedDialog = int1;
                MessagesController.getNotificationsSettings(MusicBrowserService.this.currentAccount).edit().putInt("auto_lastSelectedDialog", int1).commit();
                MediaController.getInstance().setPlaylist(list, list.get(int2), false);
                MusicBrowserService.this.mediaSession.setQueue((List)queue);
                if (int1 > 0) {
                    final TLRPC.User user = (TLRPC.User)MusicBrowserService.this.users.get(int1);
                    if (user != null) {
                        MusicBrowserService.this.mediaSession.setQueueTitle((CharSequence)ContactsController.formatName(user.first_name, user.last_name));
                    }
                    else {
                        MusicBrowserService.this.mediaSession.setQueueTitle((CharSequence)"DELETED USER");
                    }
                }
                else {
                    final TLRPC.Chat chat = (TLRPC.Chat)MusicBrowserService.this.chats.get(-int1);
                    if (chat != null) {
                        MusicBrowserService.this.mediaSession.setQueueTitle((CharSequence)chat.title);
                    }
                    else {
                        MusicBrowserService.this.mediaSession.setQueueTitle((CharSequence)"DELETED CHAT");
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            MusicBrowserService.this.handlePlayRequest();
        }
        
        public void onPlayFromSearch(String lowerCase, final Bundle bundle) {
            if (lowerCase != null) {
                if (lowerCase.length() != 0) {
                    lowerCase = lowerCase.toLowerCase();
                    for (int i = 0; i < MusicBrowserService.this.dialogs.size(); ++i) {
                        final int intValue = MusicBrowserService.this.dialogs.get(i);
                        if (intValue > 0) {
                            final TLRPC.User user = (TLRPC.User)MusicBrowserService.this.users.get(intValue);
                            if (user != null) {
                                final String first_name = user.first_name;
                                if (first_name == null || !first_name.startsWith(lowerCase)) {
                                    final String last_name = user.last_name;
                                    if (last_name == null || !last_name.startsWith(lowerCase)) {
                                        continue;
                                    }
                                }
                                final StringBuilder sb = new StringBuilder();
                                sb.append(intValue);
                                sb.append("_");
                                sb.append(0);
                                this.onPlayFromMediaId(sb.toString(), null);
                                break;
                            }
                        }
                        else {
                            final TLRPC.Chat chat = (TLRPC.Chat)MusicBrowserService.this.chats.get(-intValue);
                            if (chat != null) {
                                final String title = chat.title;
                                if (title != null && title.toLowerCase().contains(lowerCase)) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append(intValue);
                                    sb2.append("_");
                                    sb2.append(0);
                                    this.onPlayFromMediaId(sb2.toString(), null);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        public void onSeekTo(final long n) {
            final MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                MediaController.getInstance().seekToProgress(playingMessageObject, n / 1000L / (float)playingMessageObject.getDuration());
            }
        }
        
        public void onSkipToNext() {
            MediaController.getInstance().playNextMessage();
        }
        
        public void onSkipToPrevious() {
            MediaController.getInstance().playPreviousMessage();
        }
        
        public void onSkipToQueueItem(final long n) {
            MediaController.getInstance().playMessageAtIndex((int)n);
            MusicBrowserService.this.handlePlayRequest();
        }
        
        public void onStop() {
            MusicBrowserService.this.handleStopRequest(null);
        }
    }
}
