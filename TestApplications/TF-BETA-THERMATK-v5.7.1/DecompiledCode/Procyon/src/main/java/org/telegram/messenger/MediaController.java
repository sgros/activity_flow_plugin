// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.app.Dialog;
import java.io.InputStream;
import org.telegram.ui.Components.PhotoFilterView;
import org.telegram.ui.Components.Point;
import android.os.Handler;
import java.net.URLEncoder;
import org.telegram.messenger.voip.VoIPService;
import android.hardware.SensorEvent;
import org.telegram.tgnet.ConnectionsManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.os.PowerManager;
import android.graphics.SurfaceTexture;
import android.content.Intent;
import java.util.TimerTask;
import android.media.MediaCodecInfo$CodecCapabilities;
import android.annotation.SuppressLint;
import android.media.MediaCodecList;
import android.media.MediaCodecInfo;
import android.content.DialogInterface$OnCancelListener;
import android.content.Context;
import android.media.MediaFormat;
import android.media.MediaCodec$BufferInfo;
import org.telegram.messenger.video.MP4Builder;
import android.content.DialogInterface;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.PhotoViewer;
import android.media.MediaExtractor;
import java.io.FileOutputStream;
import java.util.Locale;
import android.text.TextUtils;
import android.os.Build$VERSION;
import android.media.AudioManager;
import java.util.Collection;
import android.net.Uri;
import android.content.ContentResolver;
import android.provider.MediaStore$Video$Media;
import android.database.ContentObserver;
import android.provider.MediaStore$Images$Media;
import java.nio.ByteOrder;
import android.util.SparseArray;
import android.hardware.SensorManager;
import java.io.File;
import org.telegram.ui.ChatActivity;
import android.os.PowerManager$WakeLock;
import java.util.Timer;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.ui.ActionBar.BaseFragment;
import java.nio.ByteBuffer;
import android.view.View;
import android.widget.FrameLayout;
import android.view.TextureView;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import android.app.Activity;
import android.media.AudioRecord;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.messenger.audioinfo.AudioInfo;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.media.AudioManager$OnAudioFocusChangeListener;

public class MediaController implements AudioManager$OnAudioFocusChangeListener, NotificationCenterDelegate, SensorEventListener
{
    private static final int AUDIO_FOCUSED = 2;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static volatile MediaController Instance;
    public static final String MIME_TYPE = "video/avc";
    private static final int PROCESSOR_TYPE_INTEL = 2;
    private static final int PROCESSOR_TYPE_MTK = 3;
    private static final int PROCESSOR_TYPE_OTHER = 0;
    private static final int PROCESSOR_TYPE_QCOM = 1;
    private static final int PROCESSOR_TYPE_SEC = 4;
    private static final int PROCESSOR_TYPE_TI = 5;
    private static final float VOLUME_DUCK = 0.2f;
    private static final float VOLUME_NORMAL = 1.0f;
    public static AlbumEntry allMediaAlbumEntry;
    public static AlbumEntry allPhotosAlbumEntry;
    public static AlbumEntry allVideosAlbumEntry;
    private static Runnable broadcastPhotosRunnable;
    private static final String[] projectionPhotos;
    private static final String[] projectionVideo;
    private static Runnable refreshGalleryRunnable;
    private Sensor accelerometerSensor;
    private boolean accelerometerVertical;
    private boolean allowStartRecord;
    private int audioFocus;
    private AudioInfo audioInfo;
    private VideoPlayer audioPlayer;
    private AudioRecord audioRecorder;
    private Activity baseActivity;
    private boolean callInProgress;
    private boolean cancelCurrentVideoConversion;
    private int countLess;
    private AspectRatioFrameLayout currentAspectRatioFrameLayout;
    private float currentAspectRatioFrameLayoutRatio;
    private boolean currentAspectRatioFrameLayoutReady;
    private int currentAspectRatioFrameLayoutRotation;
    private float currentPlaybackSpeed;
    private int currentPlaylistNum;
    private TextureView currentTextureView;
    private FrameLayout currentTextureViewContainer;
    private boolean downloadingCurrentMessage;
    private ExternalObserver externalObserver;
    private View feedbackView;
    private ByteBuffer fileBuffer;
    private DispatchQueue fileEncodingQueue;
    private BaseFragment flagSecureFragment;
    private boolean forceLoopCurrentPlaylist;
    private HashMap<String, MessageObject> generatingWaveform;
    private MessageObject goingToShowMessageObject;
    private float[] gravity;
    private float[] gravityFast;
    private Sensor gravitySensor;
    private int hasAudioFocus;
    private boolean ignoreOnPause;
    private boolean ignoreProximity;
    private boolean inputFieldHasText;
    private InternalObserver internalObserver;
    private boolean isDrawingWasReady;
    private boolean isPaused;
    private int lastChatAccount;
    private long lastChatEnterTime;
    private long lastChatLeaveTime;
    private ArrayList<Long> lastChatVisibleMessages;
    private long lastMediaCheckTime;
    private int lastMessageId;
    private long lastProgress;
    private float lastProximityValue;
    private TLRPC.EncryptedChat lastSecretChat;
    private long lastTimestamp;
    private TLRPC.User lastUser;
    private float[] linearAcceleration;
    private Sensor linearSensor;
    private String[] mediaProjections;
    private PipRoundVideoView pipRoundVideoView;
    private int pipSwitchingState;
    private boolean playMusicAgain;
    private boolean playerWasReady;
    private MessageObject playingMessageObject;
    private ArrayList<MessageObject> playlist;
    private float previousAccValue;
    private Timer progressTimer;
    private final Object progressTimerSync;
    private boolean proximityHasDifferentValues;
    private Sensor proximitySensor;
    private boolean proximityTouched;
    private PowerManager$WakeLock proximityWakeLock;
    private ChatActivity raiseChat;
    private boolean raiseToEarRecord;
    private int raisedToBack;
    private int raisedToTop;
    private int raisedToTopSign;
    private int recordBufferSize;
    private ArrayList<ByteBuffer> recordBuffers;
    private long recordDialogId;
    private DispatchQueue recordQueue;
    private MessageObject recordReplyingMessageObject;
    private Runnable recordRunnable;
    private short[] recordSamples;
    private Runnable recordStartRunnable;
    private long recordStartTime;
    private long recordTimeCount;
    private TLRPC.TL_document recordingAudio;
    private File recordingAudioFile;
    private int recordingCurrentAccount;
    private boolean resumeAudioOnFocusGain;
    private long samplesCount;
    private float seekToProgressPending;
    private int sendAfterDone;
    private SensorManager sensorManager;
    private boolean sensorsStarted;
    private Runnable setLoadingRunnable;
    private ArrayList<MessageObject> shuffledPlaylist;
    private int startObserverToken;
    private StopMediaObserverRunnable stopMediaObserverRunnable;
    private final Object sync;
    private long timeSinceRaise;
    private boolean useFrontSpeaker;
    private boolean videoConvertFirstWrite;
    private ArrayList<MessageObject> videoConvertQueue;
    private final Object videoConvertSync;
    private VideoPlayer videoPlayer;
    private final Object videoQueueSync;
    private ArrayList<MessageObject> voiceMessagesPlaylist;
    private SparseArray<MessageObject> voiceMessagesPlaylistMap;
    private boolean voiceMessagesPlaylistUnread;
    
    static {
        projectionPhotos = new String[] { "_id", "bucket_id", "bucket_display_name", "_data", "datetaken", "orientation" };
        projectionVideo = new String[] { "_id", "bucket_id", "bucket_display_name", "_data", "datetaken", "duration" };
    }
    
    public MediaController() {
        this.videoConvertSync = new Object();
        this.lastTimestamp = 0L;
        this.lastProximityValue = -100.0f;
        this.gravity = new float[3];
        this.gravityFast = new float[3];
        this.linearAcceleration = new float[3];
        this.audioFocus = 0;
        this.videoConvertQueue = new ArrayList<MessageObject>();
        this.videoQueueSync = new Object();
        this.cancelCurrentVideoConversion = false;
        this.videoConvertFirstWrite = true;
        this.generatingWaveform = new HashMap<String, MessageObject>();
        this.isPaused = false;
        this.audioPlayer = null;
        this.currentPlaybackSpeed = 1.0f;
        this.lastProgress = 0L;
        this.progressTimer = null;
        this.progressTimerSync = new Object();
        this.playlist = new ArrayList<MessageObject>();
        this.shuffledPlaylist = new ArrayList<MessageObject>();
        this.setLoadingRunnable = new Runnable() {
            @Override
            public void run() {
                if (MediaController.this.playingMessageObject == null) {
                    return;
                }
                FileLoader.getInstance(MediaController.this.playingMessageObject.currentAccount).setLoadingVideo(MediaController.this.playingMessageObject.getDocument(), true, false);
            }
        };
        this.recordSamples = new short[1024];
        this.sync = new Object();
        this.recordBuffers = new ArrayList<ByteBuffer>();
        this.recordBufferSize = 1280;
        this.recordRunnable = new Runnable() {
            @Override
            public void run() {
                if (MediaController.this.audioRecorder != null) {
                    final boolean empty = MediaController.this.recordBuffers.isEmpty();
                    boolean b = false;
                    ByteBuffer allocateDirect;
                    if (!empty) {
                        allocateDirect = MediaController.this.recordBuffers.get(0);
                        MediaController.this.recordBuffers.remove(0);
                    }
                    else {
                        allocateDirect = ByteBuffer.allocateDirect(MediaController.this.recordBufferSize);
                        allocateDirect.order(ByteOrder.nativeOrder());
                    }
                    allocateDirect.rewind();
                    final int read = MediaController.this.audioRecorder.read(allocateDirect, allocateDirect.capacity());
                    if (read > 0) {
                        allocateDirect.limit(read);
                        double n10 = 0.0;
                        Label_0482: {
                            double v4;
                            try {
                                final long n = MediaController.this.samplesCount + read / 2;
                                final double v = (double)MediaController.this.samplesCount;
                                final double v2 = (double)n;
                                Double.isNaN(v);
                                Double.isNaN(v2);
                                final double n2 = v / v2;
                                final double v3 = MediaController.this.recordSamples.length;
                                Double.isNaN(v3);
                                final int n3 = (int)(n2 * v3);
                                final int length = MediaController.this.recordSamples.length;
                                final float n4 = 0.0f;
                                if (n3 != 0) {
                                    final float n5 = MediaController.this.recordSamples.length / (float)n3;
                                    int i = 0;
                                    float n6 = 0.0f;
                                    while (i < n3) {
                                        MediaController.this.recordSamples[i] = MediaController.this.recordSamples[(int)n6];
                                        n6 += n5;
                                        ++i;
                                    }
                                }
                                final float n7 = read / 2.0f / (length - n3);
                                int n8 = n3;
                                int n9 = 0;
                                n10 = 0.0;
                                float n11 = n4;
                                while (true) {
                                    v4 = n10;
                                    try {
                                        if (n9 < read / 2) {
                                            v4 = n10;
                                            final short short1 = allocateDirect.getShort();
                                            double n12 = n10;
                                            if (short1 > 2500) {
                                                v4 = short1 * short1;
                                                Double.isNaN(v4);
                                                n12 = n10 + v4;
                                            }
                                            int n13 = n8;
                                            float n14 = n11;
                                            if (n9 == (int)n11) {
                                                n13 = n8;
                                                n14 = n11;
                                                v4 = n12;
                                                if (n8 < MediaController.this.recordSamples.length) {
                                                    v4 = n12;
                                                    MediaController.this.recordSamples[n8] = short1;
                                                    n14 = n11 + n7;
                                                    n13 = n8 + 1;
                                                }
                                            }
                                            ++n9;
                                            n8 = n13;
                                            n11 = n14;
                                            n10 = n12;
                                            continue;
                                        }
                                        v4 = n10;
                                        MediaController.this.samplesCount = n;
                                        break Label_0482;
                                    }
                                    catch (Exception ex) {}
                                }
                            }
                            catch (Exception ex) {
                                v4 = 0.0;
                            }
                            final Exception ex;
                            FileLog.e(ex);
                            n10 = v4;
                        }
                        allocateDirect.position(0);
                        final double v5 = read;
                        Double.isNaN(v5);
                        final double sqrt = Math.sqrt(n10 / v5 / 2.0);
                        if (read != allocateDirect.capacity()) {
                            b = true;
                        }
                        if (read != 0) {
                            MediaController.this.fileEncodingQueue.postRunnable(new _$$Lambda$MediaController$2$FRC3BsVbfkarri1fq2pbdFf8in8(this, allocateDirect, b));
                        }
                        MediaController.this.recordQueue.postRunnable(MediaController.this.recordRunnable);
                        AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$2$c_rZm06Vbt5svAWMsWH6lgBDhHI(this, sqrt));
                    }
                    else {
                        MediaController.this.recordBuffers.add(allocateDirect);
                        final MediaController this$0 = MediaController.this;
                        this$0.stopRecordingInternal(this$0.sendAfterDone);
                    }
                }
            }
        };
        (this.recordQueue = new DispatchQueue("recordQueue")).setPriority(10);
        (this.fileEncodingQueue = new DispatchQueue("fileEncodingQueue")).setPriority(10);
        this.recordQueue.postRunnable(new _$$Lambda$MediaController$qSDZPxerqEszkXBWWApUdhxsISQ(this));
        Utilities.globalQueue.postRunnable(new _$$Lambda$MediaController$FaCBRlQgZxu6gGs04ETW1GD6ZDk(this));
        this.fileBuffer = ByteBuffer.allocateDirect(1920);
        AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$Jq_ZASoLiwPvRrenXbD34k0cp8A(this));
        this.mediaProjections = new String[] { "_data", "_display_name", "bucket_display_name", "datetaken", "title", "width", "height" };
        final ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
        try {
            contentResolver.registerContentObserver(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, true, (ContentObserver)new GalleryObserverExternal());
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            contentResolver.registerContentObserver(MediaStore$Images$Media.INTERNAL_CONTENT_URI, true, (ContentObserver)new GalleryObserverInternal());
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        try {
            contentResolver.registerContentObserver(MediaStore$Video$Media.EXTERNAL_CONTENT_URI, true, (ContentObserver)new GalleryObserverExternal());
        }
        catch (Exception ex3) {
            FileLog.e(ex3);
        }
        try {
            contentResolver.registerContentObserver(MediaStore$Video$Media.INTERNAL_CONTENT_URI, true, (ContentObserver)new GalleryObserverInternal());
        }
        catch (Exception ex4) {
            FileLog.e(ex4);
        }
    }
    
    private static void broadcastNewPhotos(final int n, final ArrayList<AlbumEntry> list, final ArrayList<AlbumEntry> list2, final Integer n2, final AlbumEntry albumEntry, final AlbumEntry albumEntry2, final AlbumEntry albumEntry3, final int n3) {
        final Runnable broadcastPhotosRunnable = MediaController.broadcastPhotosRunnable;
        if (broadcastPhotosRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(broadcastPhotosRunnable);
        }
        AndroidUtilities.runOnUIThread(MediaController.broadcastPhotosRunnable = new _$$Lambda$MediaController$FEhqTC_6dgiRDF8VPuSngG1CY9Y(n, list, list2, n2, albumEntry, albumEntry2, albumEntry3), n3);
    }
    
    private void buildShuffledPlayList() {
        if (this.playlist.isEmpty()) {
            return;
        }
        final ArrayList<MessageObject> list = new ArrayList<MessageObject>(this.playlist);
        this.shuffledPlaylist.clear();
        final MessageObject e = this.playlist.get(this.currentPlaylistNum);
        list.remove(this.currentPlaylistNum);
        this.shuffledPlaylist.add(e);
        for (int size = list.size(), i = 0; i < size; ++i) {
            final int nextInt = Utilities.random.nextInt(list.size());
            this.shuffledPlaylist.add(list.get(nextInt));
            list.remove(nextInt);
        }
    }
    
    private void checkAudioFocus(final MessageObject messageObject) {
        int hasAudioFocus;
        if (!messageObject.isVoice() && !messageObject.isRoundVideo()) {
            hasAudioFocus = 1;
        }
        else if (this.useFrontSpeaker) {
            hasAudioFocus = 3;
        }
        else {
            hasAudioFocus = 2;
        }
        if (this.hasAudioFocus != hasAudioFocus) {
            int n;
            if ((this.hasAudioFocus = hasAudioFocus) == 3) {
                n = NotificationsController.audioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)this, 0, 1);
            }
            else {
                final AudioManager audioManager = NotificationsController.audioManager;
                int n2;
                if (hasAudioFocus == 2) {
                    n2 = 3;
                }
                else {
                    n2 = 1;
                }
                n = audioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)this, 3, n2);
            }
            if (n == 1) {
                this.audioFocus = 2;
            }
        }
    }
    
    private void checkConversionCanceled() {
        synchronized (this.videoConvertSync) {
            final boolean cancelCurrentVideoConversion = this.cancelCurrentVideoConversion;
            // monitorexit(this.videoConvertSync)
            if (!cancelCurrentVideoConversion) {
                return;
            }
            throw new RuntimeException("canceled conversion");
        }
    }
    
    public static void checkGallery() {
        if (Build$VERSION.SDK_INT >= 24) {
            final AlbumEntry allPhotosAlbumEntry = MediaController.allPhotosAlbumEntry;
            if (allPhotosAlbumEntry != null) {
                Utilities.globalQueue.postRunnable(new _$$Lambda$MediaController$_5Ec_d3Vawho174Y_PrhWoRflEw(allPhotosAlbumEntry.photos.size()), 2000L);
            }
        }
    }
    
    private void checkIsNextMusicFileDownloaded(final int n) {
        if (!DownloadController.getInstance(n).canDownloadNextTrack()) {
            return;
        }
        ArrayList<MessageObject> list;
        if (SharedConfig.shuffleMusic) {
            list = this.shuffledPlaylist;
        }
        else {
            list = this.playlist;
        }
        if (list != null) {
            if (list.size() >= 2) {
                int index;
                if (SharedConfig.playOrderReversed) {
                    if ((index = this.currentPlaylistNum + 1) >= list.size()) {
                        index = 0;
                    }
                }
                else if ((index = this.currentPlaylistNum - 1) < 0) {
                    index = list.size() - 1;
                }
                if (index >= 0) {
                    if (index < list.size()) {
                        final MessageObject messageObject = list.get(index);
                        final boolean empty = TextUtils.isEmpty((CharSequence)messageObject.messageOwner.attachPath);
                        File file2;
                        final File file = file2 = null;
                        if (!empty) {
                            file2 = new File(messageObject.messageOwner.attachPath);
                            if (!file2.exists()) {
                                file2 = file;
                            }
                        }
                        File pathToMessage;
                        if (file2 != null) {
                            pathToMessage = file2;
                        }
                        else {
                            pathToMessage = FileLoader.getPathToMessage(messageObject.messageOwner);
                        }
                        if (pathToMessage != null) {
                            pathToMessage.exists();
                        }
                        if (pathToMessage != null && pathToMessage != file2 && !pathToMessage.exists() && messageObject.isMusic()) {
                            FileLoader.getInstance(n).loadFile(messageObject.getDocument(), messageObject, 0, 0);
                        }
                    }
                }
            }
        }
    }
    
    private void checkIsNextVoiceFileDownloaded(final int n) {
        final ArrayList<MessageObject> voiceMessagesPlaylist = this.voiceMessagesPlaylist;
        if (voiceMessagesPlaylist != null) {
            if (voiceMessagesPlaylist.size() >= 2) {
                final MessageObject messageObject = this.voiceMessagesPlaylist.get(1);
                final String attachPath = messageObject.messageOwner.attachPath;
                File file2;
                final File file = file2 = null;
                if (attachPath != null) {
                    file2 = file;
                    if (attachPath.length() > 0) {
                        file2 = new File(messageObject.messageOwner.attachPath);
                        if (!file2.exists()) {
                            file2 = file;
                        }
                    }
                }
                File pathToMessage;
                if (file2 != null) {
                    pathToMessage = file2;
                }
                else {
                    pathToMessage = FileLoader.getPathToMessage(messageObject.messageOwner);
                }
                if (pathToMessage != null) {
                    pathToMessage.exists();
                }
                if (pathToMessage != null && pathToMessage != file2 && !pathToMessage.exists()) {
                    FileLoader.getInstance(n).loadFile(messageObject.getDocument(), messageObject, 0, 0);
                }
            }
        }
    }
    
    private void checkScreenshots(final ArrayList<Long> list) {
        if (list != null && !list.isEmpty() && this.lastChatEnterTime != 0L) {
            if (this.lastUser != null || this.lastSecretChat instanceof TLRPC.TL_encryptedChat) {
                int i = 0;
                int n = 0;
                while (i < list.size()) {
                    final Long n2 = list.get(i);
                    int n3 = 0;
                    Label_0154: {
                        if (this.lastMediaCheckTime != 0L && n2 <= this.lastMediaCheckTime) {
                            n3 = n;
                        }
                        else {
                            n3 = n;
                            if (n2 >= this.lastChatEnterTime) {
                                if (this.lastChatLeaveTime != 0L) {
                                    n3 = n;
                                    if (n2 > this.lastChatLeaveTime + 2000L) {
                                        break Label_0154;
                                    }
                                }
                                this.lastMediaCheckTime = Math.max(this.lastMediaCheckTime, n2);
                                n3 = 1;
                            }
                        }
                    }
                    ++i;
                    n = n3;
                }
                if (n != 0) {
                    if (this.lastSecretChat != null) {
                        SecretChatHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastSecretChat, this.lastChatVisibleMessages, null);
                    }
                    else {
                        SendMessagesHelper.getInstance(this.lastChatAccount).sendScreenshotMessage(this.lastUser, this.lastMessageId, null);
                    }
                }
            }
        }
    }
    
    private boolean convertVideo(final MessageObject p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          4648
        //     4: aload_1        
        //     5: getfield        org/telegram/messenger/MessageObject.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //     8: astore_2       
        //     9: aload_2        
        //    10: ifnonnull       16
        //    13: goto            4648
        //    16: aload_2        
        //    17: getfield        org/telegram/messenger/VideoEditedInfo.originalPath:Ljava/lang/String;
        //    20: astore_3       
        //    21: aload_2        
        //    22: getfield        org/telegram/messenger/VideoEditedInfo.startTime:J
        //    25: lstore          4
        //    27: aload_2        
        //    28: getfield        org/telegram/messenger/VideoEditedInfo.endTime:J
        //    31: lstore          6
        //    33: aload_2        
        //    34: getfield        org/telegram/messenger/VideoEditedInfo.resultWidth:I
        //    37: istore          8
        //    39: aload_2        
        //    40: getfield        org/telegram/messenger/VideoEditedInfo.resultHeight:I
        //    43: istore          9
        //    45: aload_2        
        //    46: getfield        org/telegram/messenger/VideoEditedInfo.rotationValue:I
        //    49: istore          10
        //    51: aload_2        
        //    52: getfield        org/telegram/messenger/VideoEditedInfo.originalWidth:I
        //    55: istore          11
        //    57: aload_2        
        //    58: getfield        org/telegram/messenger/VideoEditedInfo.originalHeight:I
        //    61: istore          12
        //    63: aload_2        
        //    64: getfield        org/telegram/messenger/VideoEditedInfo.framerate:I
        //    67: istore          13
        //    69: aload_2        
        //    70: getfield        org/telegram/messenger/VideoEditedInfo.bitrate:I
        //    73: istore          14
        //    75: aload_1        
        //    76: invokevirtual   org/telegram/messenger/MessageObject.getDialogId:()J
        //    79: l2i            
        //    80: ifne            89
        //    83: iconst_1       
        //    84: istore          15
        //    86: goto            92
        //    89: iconst_0       
        //    90: istore          15
        //    92: new             Ljava/io/File;
        //    95: dup            
        //    96: aload_1        
        //    97: getfield        org/telegram/messenger/MessageObject.messageOwner:Lorg/telegram/tgnet/TLRPC$Message;
        //   100: getfield        org/telegram/tgnet/TLRPC$Message.attachPath:Ljava/lang/String;
        //   103: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   106: astore          16
        //   108: aload_3        
        //   109: astore_2       
        //   110: aload_3        
        //   111: ifnonnull       118
        //   114: ldc_w           ""
        //   117: astore_2       
        //   118: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   121: bipush          18
        //   123: if_icmpge       167
        //   126: iload           9
        //   128: iload           8
        //   130: if_icmple       167
        //   133: iload           8
        //   135: iload           11
        //   137: if_icmpeq       167
        //   140: iload           9
        //   142: iload           12
        //   144: if_icmpeq       167
        //   147: bipush          90
        //   149: istore          10
        //   151: sipush          270
        //   154: istore          17
        //   156: iload           9
        //   158: istore          18
        //   160: iload           8
        //   162: istore          19
        //   164: goto            263
        //   167: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   170: bipush          20
        //   172: if_icmple       252
        //   175: iload           10
        //   177: bipush          90
        //   179: if_icmpne       209
        //   182: sipush          270
        //   185: istore          10
        //   187: iconst_0       
        //   188: istore          20
        //   190: iload           10
        //   192: istore          17
        //   194: iload           20
        //   196: istore          10
        //   198: iload           9
        //   200: istore          18
        //   202: iload           8
        //   204: istore          19
        //   206: goto            263
        //   209: iload           10
        //   211: sipush          180
        //   214: if_icmpne       237
        //   217: iload           9
        //   219: istore          17
        //   221: sipush          180
        //   224: istore          10
        //   226: iload           8
        //   228: istore          9
        //   230: iload           17
        //   232: istore          8
        //   234: goto            187
        //   237: iload           10
        //   239: sipush          270
        //   242: if_icmpne       252
        //   245: bipush          90
        //   247: istore          10
        //   249: goto            187
        //   252: iconst_0       
        //   253: istore          17
        //   255: iload           9
        //   257: istore          19
        //   259: iload           8
        //   261: istore          18
        //   263: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //   266: ldc_w           "videoconvert"
        //   269: iconst_0       
        //   270: invokevirtual   android/content/Context.getSharedPreferences:(Ljava/lang/String;I)Landroid/content/SharedPreferences;
        //   273: astore          21
        //   275: new             Ljava/io/File;
        //   278: dup            
        //   279: aload_2        
        //   280: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   283: astore_3       
        //   284: aload_1        
        //   285: invokevirtual   org/telegram/messenger/MessageObject.getId:()I
        //   288: ifeq            372
        //   291: aload           21
        //   293: ldc_w           "isPreviousOk"
        //   296: iconst_1       
        //   297: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //   302: istore          22
        //   304: aload           21
        //   306: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //   311: ldc_w           "isPreviousOk"
        //   314: iconst_0       
        //   315: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   320: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //   325: pop            
        //   326: aload_3        
        //   327: invokevirtual   java/io/File.canRead:()Z
        //   330: ifeq            338
        //   333: iload           22
        //   335: ifne            372
        //   338: aload_0        
        //   339: aload_1        
        //   340: aload           16
        //   342: iconst_1       
        //   343: lconst_0       
        //   344: iconst_1       
        //   345: invokespecial   org/telegram/messenger/MediaController.didWriteData:(Lorg/telegram/messenger/MessageObject;Ljava/io/File;ZJZ)V
        //   348: aload           21
        //   350: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //   355: ldc_w           "isPreviousOk"
        //   358: iconst_1       
        //   359: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //   364: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //   369: pop            
        //   370: iconst_0       
        //   371: ireturn        
        //   372: aload_0        
        //   373: iconst_1       
        //   374: putfield        org/telegram/messenger/MediaController.videoConvertFirstWrite:Z
        //   377: invokestatic    java/lang/System.currentTimeMillis:()J
        //   380: lstore          23
        //   382: iload           18
        //   384: ifeq            4613
        //   387: iload           19
        //   389: ifeq            4613
        //   392: new             Landroid/media/MediaCodec$BufferInfo;
        //   395: astore          25
        //   397: aload           25
        //   399: invokespecial   android/media/MediaCodec$BufferInfo.<init>:()V
        //   402: new             Lorg/telegram/messenger/video/Mp4Movie;
        //   405: astore_3       
        //   406: aload_3        
        //   407: invokespecial   org/telegram/messenger/video/Mp4Movie.<init>:()V
        //   410: aload_3        
        //   411: aload           16
        //   413: invokevirtual   org/telegram/messenger/video/Mp4Movie.setCacheFile:(Ljava/io/File;)V
        //   416: aload_3        
        //   417: iload           10
        //   419: invokevirtual   org/telegram/messenger/video/Mp4Movie.setRotation:(I)V
        //   422: aload_3        
        //   423: iload           18
        //   425: iload           19
        //   427: invokevirtual   org/telegram/messenger/video/Mp4Movie.setSize:(II)V
        //   430: new             Lorg/telegram/messenger/video/MP4Builder;
        //   433: astore          26
        //   435: aload           26
        //   437: invokespecial   org/telegram/messenger/video/MP4Builder.<init>:()V
        //   440: aload           26
        //   442: aload_3        
        //   443: iload           15
        //   445: invokevirtual   org/telegram/messenger/video/MP4Builder.createMovie:(Lorg/telegram/messenger/video/Mp4Movie;Z)Lorg/telegram/messenger/video/MP4Builder;
        //   448: astore          27
        //   450: new             Landroid/media/MediaExtractor;
        //   453: astore          28
        //   455: aload           28
        //   457: invokespecial   android/media/MediaExtractor.<init>:()V
        //   460: aload           28
        //   462: aload_2        
        //   463: invokevirtual   android/media/MediaExtractor.setDataSource:(Ljava/lang/String;)V
        //   466: aload_0        
        //   467: invokespecial   org/telegram/messenger/MediaController.checkConversionCanceled:()V
        //   470: iload           18
        //   472: iload           11
        //   474: if_icmpne       548
        //   477: iload           19
        //   479: iload           12
        //   481: if_icmpne       548
        //   484: iload           17
        //   486: ifne            548
        //   489: aload_1        
        //   490: getfield        org/telegram/messenger/MessageObject.videoEditedInfo:Lorg/telegram/messenger/VideoEditedInfo;
        //   493: getfield        org/telegram/messenger/VideoEditedInfo.roundVideo:Z
        //   496: istore          15
        //   498: iload           15
        //   500: ifeq            506
        //   503: goto            548
        //   506: iload           14
        //   508: iconst_m1      
        //   509: if_icmpeq       518
        //   512: iconst_1       
        //   513: istore          15
        //   515: goto            521
        //   518: iconst_0       
        //   519: istore          15
        //   521: aload_0        
        //   522: aload_1        
        //   523: aload           28
        //   525: aload           27
        //   527: aload           25
        //   529: lload           4
        //   531: lload           6
        //   533: aload           16
        //   535: iload           15
        //   537: invokespecial   org/telegram/messenger/MediaController.readAndWriteTracks:(Lorg/telegram/messenger/MessageObject;Landroid/media/MediaExtractor;Lorg/telegram/messenger/video/MP4Builder;Landroid/media/MediaCodec$BufferInfo;JJLjava/io/File;Z)J
        //   540: pop2           
        //   541: goto            4300
        //   544: astore_2       
        //   545: goto            584
        //   548: aload           28
        //   550: astore          29
        //   552: aload_0        
        //   553: aload           29
        //   555: iconst_0       
        //   556: invokespecial   org/telegram/messenger/MediaController.findTrack:(Landroid/media/MediaExtractor;Z)I
        //   559: istore          10
        //   561: iload           10
        //   563: istore          9
        //   565: iload           14
        //   567: iconst_m1      
        //   568: if_icmpeq       596
        //   571: aload_0        
        //   572: aload           29
        //   574: iconst_1       
        //   575: invokespecial   org/telegram/messenger/MediaController.findTrack:(Landroid/media/MediaExtractor;Z)I
        //   578: istore          20
        //   580: goto            599
        //   583: astore_2       
        //   584: aload_2        
        //   585: astore_3       
        //   586: aload           28
        //   588: astore_2       
        //   589: aload           27
        //   591: astore          26
        //   593: goto            4436
        //   596: iconst_m1      
        //   597: istore          20
        //   599: iload           9
        //   601: iflt            4300
        //   604: getstatic       android/os/Build.MANUFACTURER:Ljava/lang/String;
        //   607: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   610: astore_2       
        //   611: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   614: istore          8
        //   616: ldc             "video/avc"
        //   618: astore          30
        //   620: iload           8
        //   622: bipush          18
        //   624: if_icmpge       902
        //   627: ldc             "video/avc"
        //   629: invokestatic    org/telegram/messenger/MediaController.selectCodec:(Ljava/lang/String;)Landroid/media/MediaCodecInfo;
        //   632: astore_3       
        //   633: aload_3        
        //   634: ldc             "video/avc"
        //   636: invokestatic    org/telegram/messenger/MediaController.selectColorFormat:(Landroid/media/MediaCodecInfo;Ljava/lang/String;)I
        //   639: istore          31
        //   641: iload           31
        //   643: ifeq            870
        //   646: aload_3        
        //   647: invokevirtual   android/media/MediaCodecInfo.getName:()Ljava/lang/String;
        //   650: astore          26
        //   652: aload           26
        //   654: ldc_w           "OMX.qcom."
        //   657: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   660: ifeq            709
        //   663: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   666: bipush          16
        //   668: if_icmpne       700
        //   671: aload_2        
        //   672: ldc_w           "lge"
        //   675: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   678: ifne            691
        //   681: aload_2        
        //   682: ldc_w           "nokia"
        //   685: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   688: ifeq            700
        //   691: iconst_1       
        //   692: istore          8
        //   694: iconst_1       
        //   695: istore          11
        //   697: goto            783
        //   700: iconst_1       
        //   701: istore          8
        //   703: iconst_0       
        //   704: istore          11
        //   706: goto            783
        //   709: aload           26
        //   711: ldc_w           "OMX.Intel."
        //   714: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   717: ifeq            726
        //   720: iconst_2       
        //   721: istore          8
        //   723: goto            703
        //   726: aload           26
        //   728: ldc_w           "OMX.MTK.VIDEO.ENCODER.AVC"
        //   731: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   734: ifeq            743
        //   737: iconst_3       
        //   738: istore          8
        //   740: goto            703
        //   743: aload           26
        //   745: ldc_w           "OMX.SEC.AVC.Encoder"
        //   748: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   751: ifeq            760
        //   754: iconst_4       
        //   755: istore          8
        //   757: goto            694
        //   760: aload           26
        //   762: ldc_w           "OMX.TI.DUCATI1.VIDEO.H264E"
        //   765: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   768: ifeq            777
        //   771: iconst_5       
        //   772: istore          8
        //   774: goto            703
        //   777: iconst_0       
        //   778: istore          8
        //   780: goto            703
        //   783: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   786: ifeq            863
        //   789: new             Ljava/lang/StringBuilder;
        //   792: astore          26
        //   794: aload           26
        //   796: invokespecial   java/lang/StringBuilder.<init>:()V
        //   799: aload           26
        //   801: ldc_w           "codec = "
        //   804: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   807: pop            
        //   808: aload           26
        //   810: aload_3        
        //   811: invokevirtual   android/media/MediaCodecInfo.getName:()Ljava/lang/String;
        //   814: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   817: pop            
        //   818: aload           26
        //   820: ldc_w           " manufacturer = "
        //   823: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   826: pop            
        //   827: aload           26
        //   829: aload_2        
        //   830: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   833: pop            
        //   834: aload           26
        //   836: ldc_w           "device = "
        //   839: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   842: pop            
        //   843: aload           26
        //   845: getstatic       android/os/Build.MODEL:Ljava/lang/String;
        //   848: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   851: pop            
        //   852: aload           26
        //   854: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   857: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   860: goto            863
        //   863: iload           11
        //   865: istore          32
        //   867: goto            913
        //   870: new             Ljava/lang/RuntimeException;
        //   873: astore_2       
        //   874: aload_2        
        //   875: ldc_w           "no supported color format"
        //   878: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   881: aload_2        
        //   882: athrow         
        //   883: astore          33
        //   885: aconst_null    
        //   886: astore_2       
        //   887: aconst_null    
        //   888: astore_3       
        //   889: aconst_null    
        //   890: astore          34
        //   892: iload           10
        //   894: istore          9
        //   896: aconst_null    
        //   897: astore          26
        //   899: goto            4221
        //   902: iconst_0       
        //   903: istore          8
        //   905: ldc_w           2130708361
        //   908: istore          31
        //   910: iconst_0       
        //   911: istore          32
        //   913: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //   916: istore          15
        //   918: iload           15
        //   920: ifeq            963
        //   923: new             Ljava/lang/StringBuilder;
        //   926: astore_3       
        //   927: aload_3        
        //   928: invokespecial   java/lang/StringBuilder.<init>:()V
        //   931: aload_3        
        //   932: ldc_w           "colorFormat = "
        //   935: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   938: pop            
        //   939: aload_3        
        //   940: iload           31
        //   942: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   945: pop            
        //   946: aload_3        
        //   947: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   950: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //   953: goto            963
        //   956: astore_2       
        //   957: aload_2        
        //   958: astore          33
        //   960: goto            885
        //   963: aload           16
        //   965: astore          35
        //   967: iload           18
        //   969: iload           19
        //   971: imul           
        //   972: istore          11
        //   974: iload           11
        //   976: iconst_3       
        //   977: imul           
        //   978: iconst_2       
        //   979: idiv           
        //   980: istore          36
        //   982: iload           8
        //   984: ifne            1043
        //   987: iload           19
        //   989: bipush          16
        //   991: irem           
        //   992: ifeq            1036
        //   995: bipush          16
        //   997: iload           19
        //   999: bipush          16
        //  1001: irem           
        //  1002: isub           
        //  1003: iload           19
        //  1005: iadd           
        //  1006: iload           19
        //  1008: isub           
        //  1009: iload           18
        //  1011: imul           
        //  1012: istore          8
        //  1014: iload           36
        //  1016: iload           8
        //  1018: iconst_5       
        //  1019: imul           
        //  1020: iconst_4       
        //  1021: idiv           
        //  1022: iadd           
        //  1023: istore          11
        //  1025: iload           8
        //  1027: istore          37
        //  1029: iload           11
        //  1031: istore          36
        //  1033: goto            1152
        //  1036: goto            1149
        //  1039: astore_2       
        //  1040: goto            957
        //  1043: iload           8
        //  1045: iconst_1       
        //  1046: if_icmpne       1087
        //  1049: aload_2        
        //  1050: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  1053: ldc_w           "lge"
        //  1056: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1059: ifne            1036
        //  1062: iload           11
        //  1064: sipush          2047
        //  1067: iadd           
        //  1068: sipush          -2048
        //  1071: iand           
        //  1072: iload           11
        //  1074: isub           
        //  1075: istore          8
        //  1077: iload           36
        //  1079: iload           8
        //  1081: iadd           
        //  1082: istore          11
        //  1084: goto            1025
        //  1087: iload           8
        //  1089: iconst_5       
        //  1090: if_icmpne       1096
        //  1093: goto            1036
        //  1096: iload           8
        //  1098: iconst_3       
        //  1099: if_icmpne       1149
        //  1102: aload_2        
        //  1103: ldc_w           "baidu"
        //  1106: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1109: ifeq            1149
        //  1112: bipush          16
        //  1114: iload           19
        //  1116: bipush          16
        //  1118: irem           
        //  1119: isub           
        //  1120: iload           19
        //  1122: iadd           
        //  1123: iload           19
        //  1125: isub           
        //  1126: iload           18
        //  1128: imul           
        //  1129: istore          8
        //  1131: iload           8
        //  1133: iconst_5       
        //  1134: imul           
        //  1135: iconst_4       
        //  1136: idiv           
        //  1137: istore          11
        //  1139: iload           36
        //  1141: iload           11
        //  1143: iadd           
        //  1144: istore          11
        //  1146: goto            1025
        //  1149: iconst_0       
        //  1150: istore          37
        //  1152: aload           29
        //  1154: iload           9
        //  1156: invokevirtual   android/media/MediaExtractor.selectTrack:(I)V
        //  1159: aload           29
        //  1161: iload           9
        //  1163: invokevirtual   android/media/MediaExtractor.getTrackFormat:(I)Landroid/media/MediaFormat;
        //  1166: astore          34
        //  1168: iload           20
        //  1170: iflt            1212
        //  1173: aload           29
        //  1175: iload           20
        //  1177: invokevirtual   android/media/MediaExtractor.selectTrack:(I)V
        //  1180: aload           29
        //  1182: iload           20
        //  1184: invokevirtual   android/media/MediaExtractor.getTrackFormat:(I)Landroid/media/MediaFormat;
        //  1187: astore_2       
        //  1188: aload_2        
        //  1189: ldc_w           "max-input-size"
        //  1192: invokevirtual   android/media/MediaFormat.getInteger:(Ljava/lang/String;)I
        //  1195: invokestatic    java/nio/ByteBuffer.allocateDirect:(I)Ljava/nio/ByteBuffer;
        //  1198: astore          33
        //  1200: aload           27
        //  1202: aload_2        
        //  1203: iconst_1       
        //  1204: invokevirtual   org/telegram/messenger/video/MP4Builder.addTrack:(Landroid/media/MediaFormat;Z)I
        //  1207: istore          8
        //  1209: goto            1219
        //  1212: bipush          -5
        //  1214: istore          8
        //  1216: aconst_null    
        //  1217: astore          33
        //  1219: lload           4
        //  1221: lconst_0       
        //  1222: lcmp           
        //  1223: ifle            1237
        //  1226: aload           29
        //  1228: lload           4
        //  1230: iconst_0       
        //  1231: invokevirtual   android/media/MediaExtractor.seekTo:(JI)V
        //  1234: goto            1244
        //  1237: aload           29
        //  1239: lconst_0       
        //  1240: iconst_0       
        //  1241: invokevirtual   android/media/MediaExtractor.seekTo:(JI)V
        //  1244: ldc             "video/avc"
        //  1246: iload           18
        //  1248: iload           19
        //  1250: invokestatic    android/media/MediaFormat.createVideoFormat:(Ljava/lang/String;II)Landroid/media/MediaFormat;
        //  1253: astore_2       
        //  1254: aload_2        
        //  1255: ldc_w           "color-format"
        //  1258: iload           31
        //  1260: invokevirtual   android/media/MediaFormat.setInteger:(Ljava/lang/String;I)V
        //  1263: iload           14
        //  1265: ifle            1271
        //  1268: goto            1276
        //  1271: ldc_w           921600
        //  1274: istore          14
        //  1276: aload_2        
        //  1277: ldc_w           "bitrate"
        //  1280: iload           14
        //  1282: invokevirtual   android/media/MediaFormat.setInteger:(Ljava/lang/String;I)V
        //  1285: iload           13
        //  1287: ifeq            1297
        //  1290: iload           13
        //  1292: istore          11
        //  1294: goto            1301
        //  1297: bipush          25
        //  1299: istore          11
        //  1301: aload_2        
        //  1302: ldc_w           "frame-rate"
        //  1305: iload           11
        //  1307: invokevirtual   android/media/MediaFormat.setInteger:(Ljava/lang/String;I)V
        //  1310: aload_2        
        //  1311: ldc_w           "i-frame-interval"
        //  1314: iconst_2       
        //  1315: invokevirtual   android/media/MediaFormat.setInteger:(Ljava/lang/String;I)V
        //  1318: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1321: istore          11
        //  1323: iload           11
        //  1325: bipush          18
        //  1327: if_icmpge       1351
        //  1330: aload_2        
        //  1331: ldc_w           "stride"
        //  1334: iload           18
        //  1336: bipush          32
        //  1338: iadd           
        //  1339: invokevirtual   android/media/MediaFormat.setInteger:(Ljava/lang/String;I)V
        //  1342: aload_2        
        //  1343: ldc_w           "slice-height"
        //  1346: iload           19
        //  1348: invokevirtual   android/media/MediaFormat.setInteger:(Ljava/lang/String;I)V
        //  1351: ldc             "video/avc"
        //  1353: invokestatic    android/media/MediaCodec.createEncoderByType:(Ljava/lang/String;)Landroid/media/MediaCodec;
        //  1356: astore          38
        //  1358: aload           38
        //  1360: aload_2        
        //  1361: aconst_null    
        //  1362: aconst_null    
        //  1363: iconst_1       
        //  1364: invokevirtual   android/media/MediaCodec.configure:(Landroid/media/MediaFormat;Landroid/view/Surface;Landroid/media/MediaCrypto;I)V
        //  1367: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1370: istore          11
        //  1372: iload           11
        //  1374: bipush          18
        //  1376: if_icmplt       1431
        //  1379: new             Lorg/telegram/messenger/video/InputSurface;
        //  1382: astore_2       
        //  1383: aload_2        
        //  1384: aload           38
        //  1386: invokevirtual   android/media/MediaCodec.createInputSurface:()Landroid/view/Surface;
        //  1389: invokespecial   org/telegram/messenger/video/InputSurface.<init>:(Landroid/view/Surface;)V
        //  1392: aload_2        
        //  1393: invokevirtual   org/telegram/messenger/video/InputSurface.makeCurrent:()V
        //  1396: aload_2        
        //  1397: astore          26
        //  1399: goto            1434
        //  1402: astore          33
        //  1404: aload_2        
        //  1405: astore_3       
        //  1406: aconst_null    
        //  1407: astore_2       
        //  1408: aconst_null    
        //  1409: astore          26
        //  1411: aload           38
        //  1413: astore          34
        //  1415: goto            4221
        //  1418: astore          33
        //  1420: aconst_null    
        //  1421: astore_2       
        //  1422: aconst_null    
        //  1423: astore_3       
        //  1424: aload           38
        //  1426: astore          34
        //  1428: goto            892
        //  1431: aconst_null    
        //  1432: astore          26
        //  1434: aload           38
        //  1436: invokevirtual   android/media/MediaCodec.start:()V
        //  1439: aload           34
        //  1441: ldc_w           "mime"
        //  1444: invokevirtual   android/media/MediaFormat.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1447: invokestatic    android/media/MediaCodec.createDecoderByType:(Ljava/lang/String;)Landroid/media/MediaCodec;
        //  1450: astore_3       
        //  1451: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1454: istore          11
        //  1456: aload           26
        //  1458: astore_2       
        //  1459: iload           11
        //  1461: bipush          18
        //  1463: if_icmplt       1496
        //  1466: new             Lorg/telegram/messenger/video/OutputSurface;
        //  1469: astore          26
        //  1471: aload           26
        //  1473: invokespecial   org/telegram/messenger/video/OutputSurface.<init>:()V
        //  1476: goto            1511
        //  1479: astore          33
        //  1481: aload_2        
        //  1482: astore          26
        //  1484: aload_3        
        //  1485: astore_2       
        //  1486: aload           26
        //  1488: astore_3       
        //  1489: aload           38
        //  1491: astore          34
        //  1493: goto            892
        //  1496: new             Lorg/telegram/messenger/video/OutputSurface;
        //  1499: dup            
        //  1500: iload           18
        //  1502: iload           19
        //  1504: iload           17
        //  1506: invokespecial   org/telegram/messenger/video/OutputSurface.<init>:(III)V
        //  1509: astore          26
        //  1511: aload           26
        //  1513: invokevirtual   org/telegram/messenger/video/OutputSurface.getSurface:()Landroid/view/Surface;
        //  1516: astore          39
        //  1518: aload_3        
        //  1519: astore          40
        //  1521: iload           9
        //  1523: istore          11
        //  1525: aload_2        
        //  1526: astore          41
        //  1528: aload           26
        //  1530: astore          42
        //  1532: aload_3        
        //  1533: aload           34
        //  1535: aload           39
        //  1537: aconst_null    
        //  1538: iconst_0       
        //  1539: invokevirtual   android/media/MediaCodec.configure:(Landroid/media/MediaFormat;Landroid/view/Surface;Landroid/media/MediaCrypto;I)V
        //  1542: aload_3        
        //  1543: astore          40
        //  1545: iload           9
        //  1547: istore          11
        //  1549: aload_2        
        //  1550: astore          41
        //  1552: aload           26
        //  1554: astore          42
        //  1556: aload_3        
        //  1557: invokevirtual   android/media/MediaCodec.start:()V
        //  1560: aload_3        
        //  1561: astore          40
        //  1563: iload           9
        //  1565: istore          11
        //  1567: aload_2        
        //  1568: astore          41
        //  1570: aload           26
        //  1572: astore          42
        //  1574: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1577: istore          10
        //  1579: iload           10
        //  1581: bipush          21
        //  1583: if_icmpge       1707
        //  1586: aload_3        
        //  1587: astore          40
        //  1589: iload           9
        //  1591: istore          11
        //  1593: aload_2        
        //  1594: astore          41
        //  1596: aload           26
        //  1598: astore          42
        //  1600: aload_3        
        //  1601: invokevirtual   android/media/MediaCodec.getInputBuffers:()[Ljava/nio/ByteBuffer;
        //  1604: astore          39
        //  1606: aload_3        
        //  1607: astore          40
        //  1609: iload           9
        //  1611: istore          11
        //  1613: aload_2        
        //  1614: astore          41
        //  1616: aload           26
        //  1618: astore          42
        //  1620: aload           38
        //  1622: invokevirtual   android/media/MediaCodec.getOutputBuffers:()[Ljava/nio/ByteBuffer;
        //  1625: astore          34
        //  1627: aload_3        
        //  1628: astore          40
        //  1630: iload           9
        //  1632: istore          11
        //  1634: aload_2        
        //  1635: astore          41
        //  1637: aload           26
        //  1639: astore          42
        //  1641: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1644: bipush          18
        //  1646: if_icmpge       1677
        //  1649: aload_3        
        //  1650: astore          40
        //  1652: iload           9
        //  1654: istore          11
        //  1656: aload_2        
        //  1657: astore          41
        //  1659: aload           26
        //  1661: astore          42
        //  1663: aload           38
        //  1665: invokevirtual   android/media/MediaCodec.getInputBuffers:()[Ljava/nio/ByteBuffer;
        //  1668: astore          43
        //  1670: aload           39
        //  1672: astore          44
        //  1674: goto            1720
        //  1677: aload           39
        //  1679: astore          42
        //  1681: goto            1713
        //  1684: astore          33
        //  1686: iload           11
        //  1688: istore          9
        //  1690: aload           41
        //  1692: astore_3       
        //  1693: aload           40
        //  1695: astore_2       
        //  1696: aload           38
        //  1698: astore          34
        //  1700: aload           42
        //  1702: astore          26
        //  1704: goto            4221
        //  1707: aconst_null    
        //  1708: astore          34
        //  1710: aconst_null    
        //  1711: astore          42
        //  1713: aconst_null    
        //  1714: astore          43
        //  1716: aload           42
        //  1718: astore          44
        //  1720: aload_3        
        //  1721: astore          40
        //  1723: iload           9
        //  1725: istore          11
        //  1727: aload_2        
        //  1728: astore          41
        //  1730: aload           26
        //  1732: astore          42
        //  1734: aload_0        
        //  1735: invokespecial   org/telegram/messenger/MediaController.checkConversionCanceled:()V
        //  1738: iconst_0       
        //  1739: istore          11
        //  1741: iconst_0       
        //  1742: istore          13
        //  1744: iconst_0       
        //  1745: istore          17
        //  1747: bipush          -5
        //  1749: istore          10
        //  1751: ldc2_w          -1
        //  1754: lstore          45
        //  1756: aload           34
        //  1758: astore          47
        //  1760: aload           33
        //  1762: astore          48
        //  1764: aload           25
        //  1766: astore          33
        //  1768: iload           20
        //  1770: istore          14
        //  1772: aload           30
        //  1774: astore          34
        //  1776: iload           8
        //  1778: istore          49
        //  1780: iload           11
        //  1782: istore          8
        //  1784: iload           13
        //  1786: ifne            4053
        //  1789: aload_3        
        //  1790: astore          40
        //  1792: iload           9
        //  1794: istore          11
        //  1796: aload_2        
        //  1797: astore          41
        //  1799: aload           26
        //  1801: astore          42
        //  1803: aload_0        
        //  1804: invokespecial   org/telegram/messenger/MediaController.checkConversionCanceled:()V
        //  1807: iload           8
        //  1809: ifne            2406
        //  1812: aload           29
        //  1814: invokevirtual   android/media/MediaExtractor.getSampleTrackIndex:()I
        //  1817: istore          11
        //  1819: iload           11
        //  1821: iload           9
        //  1823: if_icmpne       2027
        //  1826: aload_3        
        //  1827: astore          40
        //  1829: iload           9
        //  1831: istore          11
        //  1833: aload_2        
        //  1834: astore          41
        //  1836: aload           26
        //  1838: astore          42
        //  1840: aload_3        
        //  1841: ldc2_w          2500
        //  1844: invokevirtual   android/media/MediaCodec.dequeueInputBuffer:(J)I
        //  1847: istore          12
        //  1849: iload           8
        //  1851: istore          20
        //  1853: iload           12
        //  1855: iflt            2024
        //  1858: aload_3        
        //  1859: astore          40
        //  1861: iload           9
        //  1863: istore          11
        //  1865: aload_2        
        //  1866: astore          41
        //  1868: aload           26
        //  1870: astore          42
        //  1872: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  1875: bipush          21
        //  1877: if_icmpge       1890
        //  1880: aload           44
        //  1882: iload           12
        //  1884: aaload         
        //  1885: astore          25
        //  1887: goto            1912
        //  1890: aload_3        
        //  1891: astore          40
        //  1893: iload           9
        //  1895: istore          11
        //  1897: aload_2        
        //  1898: astore          41
        //  1900: aload           26
        //  1902: astore          42
        //  1904: aload_3        
        //  1905: iload           12
        //  1907: invokevirtual   android/media/MediaCodec.getInputBuffer:(I)Ljava/nio/ByteBuffer;
        //  1910: astore          25
        //  1912: aload_3        
        //  1913: astore          40
        //  1915: iload           9
        //  1917: istore          11
        //  1919: aload_2        
        //  1920: astore          41
        //  1922: aload           26
        //  1924: astore          42
        //  1926: aload           29
        //  1928: aload           25
        //  1930: iconst_0       
        //  1931: invokevirtual   android/media/MediaExtractor.readSampleData:(Ljava/nio/ByteBuffer;I)I
        //  1934: istore          20
        //  1936: iload           20
        //  1938: ifge            1971
        //  1941: aload_3        
        //  1942: astore          40
        //  1944: iload           9
        //  1946: istore          11
        //  1948: aload_2        
        //  1949: astore          41
        //  1951: aload           26
        //  1953: astore          42
        //  1955: aload_3        
        //  1956: iload           12
        //  1958: iconst_0       
        //  1959: iconst_0       
        //  1960: lconst_0       
        //  1961: iconst_4       
        //  1962: invokevirtual   android/media/MediaCodec.queueInputBuffer:(IIIJI)V
        //  1965: iconst_1       
        //  1966: istore          20
        //  1968: goto            2024
        //  1971: aload_3        
        //  1972: astore          40
        //  1974: iload           9
        //  1976: istore          11
        //  1978: aload_2        
        //  1979: astore          41
        //  1981: aload           26
        //  1983: astore          42
        //  1985: aload_3        
        //  1986: iload           12
        //  1988: iconst_0       
        //  1989: iload           20
        //  1991: aload           29
        //  1993: invokevirtual   android/media/MediaExtractor.getSampleTime:()J
        //  1996: iconst_0       
        //  1997: invokevirtual   android/media/MediaCodec.queueInputBuffer:(IIIJI)V
        //  2000: aload_3        
        //  2001: astore          40
        //  2003: iload           9
        //  2005: istore          11
        //  2007: aload_2        
        //  2008: astore          41
        //  2010: aload           26
        //  2012: astore          42
        //  2014: aload           29
        //  2016: invokevirtual   android/media/MediaExtractor.advance:()Z
        //  2019: pop            
        //  2020: iload           8
        //  2022: istore          20
        //  2024: goto            2287
        //  2027: iload           14
        //  2029: iconst_m1      
        //  2030: if_icmpeq       2271
        //  2033: iload           11
        //  2035: iload           14
        //  2037: if_icmpne       2271
        //  2040: aload           29
        //  2042: aload           48
        //  2044: iconst_0       
        //  2045: invokevirtual   android/media/MediaExtractor.readSampleData:(Ljava/nio/ByteBuffer;I)I
        //  2048: istore          20
        //  2050: aload           33
        //  2052: iload           20
        //  2054: putfield        android/media/MediaCodec$BufferInfo.size:I
        //  2057: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  2060: istore          20
        //  2062: iload           20
        //  2064: bipush          21
        //  2066: if_icmpge       2107
        //  2069: aload           48
        //  2071: iconst_0       
        //  2072: invokevirtual   java/nio/ByteBuffer.position:(I)Ljava/nio/Buffer;
        //  2075: pop            
        //  2076: aload           48
        //  2078: aload           33
        //  2080: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2083: invokevirtual   java/nio/ByteBuffer.limit:(I)Ljava/nio/Buffer;
        //  2086: pop            
        //  2087: goto            2107
        //  2090: astore          33
        //  2092: aload_2        
        //  2093: astore          34
        //  2095: aload_3        
        //  2096: astore_2       
        //  2097: aload           34
        //  2099: astore_3       
        //  2100: aload           38
        //  2102: astore          34
        //  2104: goto            4221
        //  2107: aload           33
        //  2109: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2112: istore          20
        //  2114: iload           20
        //  2116: iflt            2143
        //  2119: aload           33
        //  2121: aload           29
        //  2123: invokevirtual   android/media/MediaExtractor.getSampleTime:()J
        //  2126: putfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  2129: aload           29
        //  2131: invokevirtual   android/media/MediaExtractor.advance:()Z
        //  2134: pop            
        //  2135: goto            2152
        //  2138: astore          33
        //  2140: goto            2092
        //  2143: aload           33
        //  2145: iconst_0       
        //  2146: putfield        android/media/MediaCodec$BufferInfo.size:I
        //  2149: iconst_1       
        //  2150: istore          8
        //  2152: aload           33
        //  2154: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2157: istore          20
        //  2159: iload           20
        //  2161: ifle            2245
        //  2164: lload           6
        //  2166: lconst_0       
        //  2167: lcmp           
        //  2168: iflt            2186
        //  2171: aload           33
        //  2173: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  2176: lstore          50
        //  2178: lload           50
        //  2180: lload           6
        //  2182: lcmp           
        //  2183: ifge            2245
        //  2186: aload           33
        //  2188: iconst_0       
        //  2189: putfield        android/media/MediaCodec$BufferInfo.offset:I
        //  2192: aload           33
        //  2194: aload           29
        //  2196: invokevirtual   android/media/MediaExtractor.getSampleFlags:()I
        //  2199: putfield        android/media/MediaCodec$BufferInfo.flags:I
        //  2202: aload           27
        //  2204: iload           49
        //  2206: aload           48
        //  2208: aload           33
        //  2210: iconst_0       
        //  2211: invokevirtual   org/telegram/messenger/video/MP4Builder.writeSampleData:(ILjava/nio/ByteBuffer;Landroid/media/MediaCodec$BufferInfo;Z)J
        //  2214: lstore          50
        //  2216: lload           50
        //  2218: lconst_0       
        //  2219: lcmp           
        //  2220: ifeq            2242
        //  2223: aload_0        
        //  2224: aload_1        
        //  2225: aload           35
        //  2227: iconst_0       
        //  2228: lload           50
        //  2230: iconst_0       
        //  2231: invokespecial   org/telegram/messenger/MediaController.didWriteData:(Lorg/telegram/messenger/MessageObject;Ljava/io/File;ZJZ)V
        //  2234: goto            2245
        //  2237: astore          33
        //  2239: goto            2258
        //  2242: goto            2245
        //  2245: iconst_0       
        //  2246: istore          20
        //  2248: goto            2294
        //  2251: astore          33
        //  2253: goto            2258
        //  2256: astore          33
        //  2258: aload_2        
        //  2259: astore          34
        //  2261: aload_3        
        //  2262: astore_2       
        //  2263: goto            2396
        //  2266: astore          33
        //  2268: goto            2391
        //  2271: iload           8
        //  2273: istore          20
        //  2275: iload           11
        //  2277: iconst_m1      
        //  2278: if_icmpne       2287
        //  2281: iconst_1       
        //  2282: istore          20
        //  2284: goto            2294
        //  2287: iload           20
        //  2289: istore          8
        //  2291: iconst_0       
        //  2292: istore          20
        //  2294: aload_2        
        //  2295: astore          42
        //  2297: aload           26
        //  2299: astore          30
        //  2301: iload           20
        //  2303: ifeq            2383
        //  2306: aload_3        
        //  2307: astore          40
        //  2309: aload           40
        //  2311: astore          39
        //  2313: aload           30
        //  2315: astore          41
        //  2317: aload           42
        //  2319: astore          25
        //  2321: aload           40
        //  2323: ldc2_w          2500
        //  2326: invokevirtual   android/media/MediaCodec.dequeueInputBuffer:(J)I
        //  2329: istore          20
        //  2331: iload           20
        //  2333: iflt            2383
        //  2336: aload           40
        //  2338: astore          39
        //  2340: aload           30
        //  2342: astore          41
        //  2344: aload           42
        //  2346: astore          25
        //  2348: aload           40
        //  2350: iload           20
        //  2352: iconst_0       
        //  2353: iconst_0       
        //  2354: lconst_0       
        //  2355: iconst_4       
        //  2356: invokevirtual   android/media/MediaCodec.queueInputBuffer:(IIIJI)V
        //  2359: iconst_1       
        //  2360: istore          8
        //  2362: goto            2409
        //  2365: astore          33
        //  2367: aload           39
        //  2369: astore_2       
        //  2370: aload           25
        //  2372: astore_3       
        //  2373: aload           41
        //  2375: astore          26
        //  2377: aload_3        
        //  2378: astore          34
        //  2380: goto            2396
        //  2383: aload_3        
        //  2384: astore          40
        //  2386: goto            2409
        //  2389: astore          33
        //  2391: aload_2        
        //  2392: astore          34
        //  2394: aload_3        
        //  2395: astore_2       
        //  2396: aload           34
        //  2398: astore_3       
        //  2399: aload           38
        //  2401: astore          34
        //  2403: goto            4221
        //  2406: aload_3        
        //  2407: astore          40
        //  2409: iload           9
        //  2411: istore          11
        //  2413: iload           17
        //  2415: iconst_1       
        //  2416: ixor           
        //  2417: istore          52
        //  2419: iload           10
        //  2421: istore          12
        //  2423: iconst_1       
        //  2424: istore          20
        //  2426: aload_2        
        //  2427: astore_3       
        //  2428: aload           47
        //  2430: astore          42
        //  2432: iload           17
        //  2434: istore          10
        //  2436: iload           8
        //  2438: istore          17
        //  2440: iload           12
        //  2442: istore          8
        //  2444: aload           40
        //  2446: astore_2       
        //  2447: iload           52
        //  2449: ifne            2495
        //  2452: iload           20
        //  2454: ifeq            2460
        //  2457: goto            2495
        //  2460: iload           8
        //  2462: istore          20
        //  2464: iload           17
        //  2466: istore          8
        //  2468: iload           11
        //  2470: istore          9
        //  2472: aload_3        
        //  2473: astore          40
        //  2475: aload_2        
        //  2476: astore_3       
        //  2477: aload           40
        //  2479: astore_2       
        //  2480: iload           10
        //  2482: istore          17
        //  2484: iload           20
        //  2486: istore          10
        //  2488: aload           42
        //  2490: astore          47
        //  2492: goto            1784
        //  2495: aload_0        
        //  2496: invokespecial   org/telegram/messenger/MediaController.checkConversionCanceled:()V
        //  2499: aload           33
        //  2501: astore          40
        //  2503: aload           38
        //  2505: aload           40
        //  2507: ldc2_w          2500
        //  2510: invokevirtual   android/media/MediaCodec.dequeueOutputBuffer:(Landroid/media/MediaCodec$BufferInfo;J)I
        //  2513: istore          53
        //  2515: iload           53
        //  2517: iconst_m1      
        //  2518: if_icmpne       2547
        //  2521: iconst_0       
        //  2522: istore          20
        //  2524: aload           42
        //  2526: astore          33
        //  2528: iload           8
        //  2530: istore          12
        //  2532: iload           13
        //  2534: istore          8
        //  2536: iload           12
        //  2538: istore          13
        //  2540: aload           33
        //  2542: astore          41
        //  2544: goto            3090
        //  2547: iload           53
        //  2549: bipush          -3
        //  2551: if_icmpne       2604
        //  2554: aload_2        
        //  2555: astore          39
        //  2557: aload           26
        //  2559: astore          41
        //  2561: aload_3        
        //  2562: astore          25
        //  2564: iload           8
        //  2566: istore          12
        //  2568: aload           42
        //  2570: astore          33
        //  2572: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  2575: bipush          21
        //  2577: if_icmpge       2601
        //  2580: aload_2        
        //  2581: astore          39
        //  2583: aload           26
        //  2585: astore          41
        //  2587: aload_3        
        //  2588: astore          25
        //  2590: aload           38
        //  2592: invokevirtual   android/media/MediaCodec.getOutputBuffers:()[Ljava/nio/ByteBuffer;
        //  2595: astore          33
        //  2597: iload           8
        //  2599: istore          12
        //  2601: goto            2532
        //  2604: iload           53
        //  2606: bipush          -2
        //  2608: if_icmpne       2670
        //  2611: aload_2        
        //  2612: astore          39
        //  2614: aload           26
        //  2616: astore          41
        //  2618: aload_3        
        //  2619: astore          25
        //  2621: aload           38
        //  2623: invokevirtual   android/media/MediaCodec.getOutputFormat:()Landroid/media/MediaFormat;
        //  2626: astore          47
        //  2628: iload           8
        //  2630: istore          12
        //  2632: aload           42
        //  2634: astore          33
        //  2636: iload           8
        //  2638: bipush          -5
        //  2640: if_icmpne       2601
        //  2643: aload_2        
        //  2644: astore          39
        //  2646: aload           26
        //  2648: astore          41
        //  2650: aload_3        
        //  2651: astore          25
        //  2653: aload           27
        //  2655: aload           47
        //  2657: iconst_0       
        //  2658: invokevirtual   org/telegram/messenger/video/MP4Builder.addTrack:(Landroid/media/MediaFormat;Z)I
        //  2661: istore          12
        //  2663: aload           42
        //  2665: astore          33
        //  2667: goto            2601
        //  2670: iload           53
        //  2672: iflt            3960
        //  2675: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  2678: istore          13
        //  2680: iload           13
        //  2682: bipush          21
        //  2684: if_icmpge       2697
        //  2687: aload           42
        //  2689: iload           53
        //  2691: aaload         
        //  2692: astore          41
        //  2694: goto            2706
        //  2697: aload           38
        //  2699: iload           53
        //  2701: invokevirtual   android/media/MediaCodec.getOutputBuffer:(I)Ljava/nio/ByteBuffer;
        //  2704: astore          41
        //  2706: aload           41
        //  2708: ifnull          3882
        //  2711: aload           40
        //  2713: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2716: istore          13
        //  2718: iload           13
        //  2720: iconst_1       
        //  2721: if_icmple       3039
        //  2724: aload           40
        //  2726: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //  2729: iconst_2       
        //  2730: iand           
        //  2731: ifne            2772
        //  2734: aload           27
        //  2736: iload           8
        //  2738: aload           41
        //  2740: aload           40
        //  2742: iconst_1       
        //  2743: invokevirtual   org/telegram/messenger/video/MP4Builder.writeSampleData:(ILjava/nio/ByteBuffer;Landroid/media/MediaCodec$BufferInfo;Z)J
        //  2746: lstore          50
        //  2748: lload           50
        //  2750: lconst_0       
        //  2751: lcmp           
        //  2752: ifeq            2769
        //  2755: aload_0        
        //  2756: aload_1        
        //  2757: aload           35
        //  2759: iconst_0       
        //  2760: lload           50
        //  2762: iconst_0       
        //  2763: invokespecial   org/telegram/messenger/MediaController.didWriteData:(Lorg/telegram/messenger/MessageObject;Ljava/io/File;ZJZ)V
        //  2766: goto            3019
        //  2769: goto            3019
        //  2772: iload           8
        //  2774: bipush          -5
        //  2776: if_icmpne       3019
        //  2779: aload           40
        //  2781: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2784: newarray        B
        //  2786: astore          25
        //  2788: aload           41
        //  2790: aload           40
        //  2792: getfield        android/media/MediaCodec$BufferInfo.offset:I
        //  2795: aload           40
        //  2797: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2800: iadd           
        //  2801: invokevirtual   java/nio/ByteBuffer.limit:(I)Ljava/nio/Buffer;
        //  2804: pop            
        //  2805: aload           41
        //  2807: aload           40
        //  2809: getfield        android/media/MediaCodec$BufferInfo.offset:I
        //  2812: invokevirtual   java/nio/ByteBuffer.position:(I)Ljava/nio/Buffer;
        //  2815: pop            
        //  2816: aload           41
        //  2818: aload           25
        //  2820: invokevirtual   java/nio/ByteBuffer.get:([B)Ljava/nio/ByteBuffer;
        //  2823: pop            
        //  2824: aload           40
        //  2826: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2829: iconst_1       
        //  2830: isub           
        //  2831: istore          8
        //  2833: iload           8
        //  2835: iflt            2953
        //  2838: iload           8
        //  2840: iconst_3       
        //  2841: if_icmple       2953
        //  2844: aload           25
        //  2846: iload           8
        //  2848: baload         
        //  2849: iconst_1       
        //  2850: if_icmpne       2947
        //  2853: aload           25
        //  2855: iload           8
        //  2857: iconst_1       
        //  2858: isub           
        //  2859: baload         
        //  2860: ifne            2947
        //  2863: aload           25
        //  2865: iload           8
        //  2867: iconst_2       
        //  2868: isub           
        //  2869: baload         
        //  2870: ifne            2947
        //  2873: iload           8
        //  2875: iconst_3       
        //  2876: isub           
        //  2877: istore          13
        //  2879: aload           25
        //  2881: iload           13
        //  2883: baload         
        //  2884: ifne            2947
        //  2887: iload           13
        //  2889: invokestatic    java/nio/ByteBuffer.allocate:(I)Ljava/nio/ByteBuffer;
        //  2892: astore          41
        //  2894: aload           40
        //  2896: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2899: iload           13
        //  2901: isub           
        //  2902: invokestatic    java/nio/ByteBuffer.allocate:(I)Ljava/nio/ByteBuffer;
        //  2905: astore          33
        //  2907: aload           41
        //  2909: aload           25
        //  2911: iconst_0       
        //  2912: iload           13
        //  2914: invokevirtual   java/nio/ByteBuffer.put:([BII)Ljava/nio/ByteBuffer;
        //  2917: iconst_0       
        //  2918: invokevirtual   java/nio/ByteBuffer.position:(I)Ljava/nio/Buffer;
        //  2921: pop            
        //  2922: aload           33
        //  2924: aload           25
        //  2926: iload           13
        //  2928: aload           40
        //  2930: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  2933: iload           13
        //  2935: isub           
        //  2936: invokevirtual   java/nio/ByteBuffer.put:([BII)Ljava/nio/ByteBuffer;
        //  2939: iconst_0       
        //  2940: invokevirtual   java/nio/ByteBuffer.position:(I)Ljava/nio/Buffer;
        //  2943: pop            
        //  2944: goto            2960
        //  2947: iinc            8, -1
        //  2950: goto            2833
        //  2953: aconst_null    
        //  2954: astore          33
        //  2956: aload           33
        //  2958: astore          41
        //  2960: aload           34
        //  2962: iload           18
        //  2964: iload           19
        //  2966: invokestatic    android/media/MediaFormat.createVideoFormat:(Ljava/lang/String;II)Landroid/media/MediaFormat;
        //  2969: astore          25
        //  2971: aload           41
        //  2973: ifnull          3001
        //  2976: aload           33
        //  2978: ifnull          3001
        //  2981: aload           25
        //  2983: ldc_w           "csd-0"
        //  2986: aload           41
        //  2988: invokevirtual   android/media/MediaFormat.setByteBuffer:(Ljava/lang/String;Ljava/nio/ByteBuffer;)V
        //  2991: aload           25
        //  2993: ldc_w           "csd-1"
        //  2996: aload           33
        //  2998: invokevirtual   android/media/MediaFormat.setByteBuffer:(Ljava/lang/String;Ljava/nio/ByteBuffer;)V
        //  3001: aload           27
        //  3003: aload           25
        //  3005: iconst_0       
        //  3006: invokevirtual   org/telegram/messenger/video/MP4Builder.addTrack:(Landroid/media/MediaFormat;Z)I
        //  3009: istore          8
        //  3011: goto            3039
        //  3014: astore          33
        //  3016: goto            3028
        //  3019: aload           33
        //  3021: astore          40
        //  3023: goto            3039
        //  3026: astore          33
        //  3028: aload           38
        //  3030: astore          34
        //  3032: iload           11
        //  3034: istore          9
        //  3036: goto            4221
        //  3039: iload           53
        //  3041: istore          12
        //  3043: aload           40
        //  3045: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //  3048: iconst_4       
        //  3049: iand           
        //  3050: ifeq            3059
        //  3053: iconst_1       
        //  3054: istore          13
        //  3056: goto            3062
        //  3059: iconst_0       
        //  3060: istore          13
        //  3062: aload           38
        //  3064: iload           12
        //  3066: iconst_0       
        //  3067: invokevirtual   android/media/MediaCodec.releaseOutputBuffer:(IZ)V
        //  3070: iload           13
        //  3072: istore          53
        //  3074: aload           42
        //  3076: astore          41
        //  3078: iload           8
        //  3080: istore          13
        //  3082: iload           53
        //  3084: istore          8
        //  3086: iload           12
        //  3088: istore          53
        //  3090: aload_2        
        //  3091: astore          42
        //  3093: iload           53
        //  3095: iconst_m1      
        //  3096: if_icmpeq       3125
        //  3099: aload           40
        //  3101: astore          33
        //  3103: iload           8
        //  3105: istore          12
        //  3107: iload           13
        //  3109: istore          8
        //  3111: aload           42
        //  3113: astore_2       
        //  3114: iload           12
        //  3116: istore          13
        //  3118: aload           41
        //  3120: astore          42
        //  3122: goto            2447
        //  3125: iload           10
        //  3127: ifne            3831
        //  3130: aload           42
        //  3132: astore_2       
        //  3133: aload_2        
        //  3134: aload           40
        //  3136: ldc2_w          2500
        //  3139: invokevirtual   android/media/MediaCodec.dequeueOutputBuffer:(Landroid/media/MediaCodec$BufferInfo;J)I
        //  3142: istore          12
        //  3144: iload           12
        //  3146: iconst_m1      
        //  3147: if_icmpne       3156
        //  3150: iconst_0       
        //  3151: istore          52
        //  3153: goto            3831
        //  3156: iload           12
        //  3158: bipush          -3
        //  3160: if_icmpne       3166
        //  3163: goto            3831
        //  3166: iload           12
        //  3168: bipush          -2
        //  3170: if_icmpne       3228
        //  3173: aload_2        
        //  3174: invokevirtual   android/media/MediaCodec.getOutputFormat:()Landroid/media/MediaFormat;
        //  3177: astore          25
        //  3179: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  3182: ifeq            3163
        //  3185: new             Ljava/lang/StringBuilder;
        //  3188: astore          33
        //  3190: aload           33
        //  3192: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3195: aload           33
        //  3197: ldc_w           "newFormat = "
        //  3200: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3203: pop            
        //  3204: aload           33
        //  3206: aload           25
        //  3208: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  3211: pop            
        //  3212: aload           33
        //  3214: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3217: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  3220: goto            3163
        //  3223: astore          33
        //  3225: goto            2377
        //  3228: iload           12
        //  3230: iflt            3760
        //  3233: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3236: istore          53
        //  3238: iload           53
        //  3240: bipush          18
        //  3242: if_icmplt       3269
        //  3245: aload           40
        //  3247: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  3250: istore          53
        //  3252: iload           53
        //  3254: ifeq            3263
        //  3257: iconst_1       
        //  3258: istore          15
        //  3260: goto            3294
        //  3263: iconst_0       
        //  3264: istore          15
        //  3266: goto            3294
        //  3269: aload           40
        //  3271: getfield        android/media/MediaCodec$BufferInfo.size:I
        //  3274: istore          53
        //  3276: iload           53
        //  3278: ifne            3257
        //  3281: aload           40
        //  3283: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3286: lconst_0       
        //  3287: lcmp           
        //  3288: ifeq            3263
        //  3291: goto            3257
        //  3294: lload           6
        //  3296: lconst_0       
        //  3297: lcmp           
        //  3298: ifle            3336
        //  3301: aload           40
        //  3303: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3306: lload           6
        //  3308: lcmp           
        //  3309: iflt            3336
        //  3312: aload           40
        //  3314: aload           40
        //  3316: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //  3319: iconst_4       
        //  3320: ior            
        //  3321: putfield        android/media/MediaCodec$BufferInfo.flags:I
        //  3324: iconst_0       
        //  3325: istore          15
        //  3327: iconst_1       
        //  3328: istore          17
        //  3330: iconst_1       
        //  3331: istore          10
        //  3333: goto            3336
        //  3336: lload           4
        //  3338: lconst_0       
        //  3339: lcmp           
        //  3340: ifle            3443
        //  3343: lload           45
        //  3345: ldc2_w          -1
        //  3348: lcmp           
        //  3349: ifne            3443
        //  3352: aload           40
        //  3354: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3357: lload           4
        //  3359: lcmp           
        //  3360: ifge            3433
        //  3363: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  3366: ifeq            3427
        //  3369: new             Ljava/lang/StringBuilder;
        //  3372: astore          33
        //  3374: aload           33
        //  3376: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3379: aload           33
        //  3381: ldc_w           "drop frame startTime = "
        //  3384: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3387: pop            
        //  3388: aload           33
        //  3390: lload           4
        //  3392: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  3395: pop            
        //  3396: aload           33
        //  3398: ldc_w           " present time = "
        //  3401: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3404: pop            
        //  3405: aload           33
        //  3407: aload           40
        //  3409: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3412: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  3415: pop            
        //  3416: aload           33
        //  3418: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3421: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  3424: goto            3427
        //  3427: iconst_0       
        //  3428: istore          15
        //  3430: goto            3443
        //  3433: aload           40
        //  3435: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3438: lstore          45
        //  3440: goto            3443
        //  3443: aload_2        
        //  3444: iload           12
        //  3446: iload           15
        //  3448: invokevirtual   android/media/MediaCodec.releaseOutputBuffer:(IZ)V
        //  3451: iload           15
        //  3453: ifeq            3665
        //  3456: aload           26
        //  3458: invokevirtual   org/telegram/messenger/video/OutputSurface.awaitNewImage:()V
        //  3461: iconst_0       
        //  3462: istore          12
        //  3464: goto            3477
        //  3467: astore          33
        //  3469: aload           33
        //  3471: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  3474: iconst_1       
        //  3475: istore          12
        //  3477: iload           12
        //  3479: ifne            3665
        //  3482: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3485: istore          12
        //  3487: iload           12
        //  3489: bipush          18
        //  3491: if_icmplt       3544
        //  3494: aload           26
        //  3496: iconst_0       
        //  3497: invokevirtual   org/telegram/messenger/video/OutputSurface.drawImage:(Z)V
        //  3500: aload           40
        //  3502: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3505: lstore          50
        //  3507: aload_3        
        //  3508: astore          25
        //  3510: aload_2        
        //  3511: astore          33
        //  3513: aload           25
        //  3515: lload           50
        //  3517: ldc2_w          1000
        //  3520: lmul           
        //  3521: invokevirtual   org/telegram/messenger/video/InputSurface.setPresentationTime:(J)V
        //  3524: aload_2        
        //  3525: astore          33
        //  3527: aload           25
        //  3529: invokevirtual   org/telegram/messenger/video/InputSurface.swapBuffers:()Z
        //  3532: pop            
        //  3533: goto            3665
        //  3536: astore          34
        //  3538: aload_2        
        //  3539: astore          33
        //  3541: goto            4041
        //  3544: aload           26
        //  3546: astore          25
        //  3548: aload_2        
        //  3549: astore          33
        //  3551: aload           38
        //  3553: ldc2_w          2500
        //  3556: invokevirtual   android/media/MediaCodec.dequeueInputBuffer:(J)I
        //  3559: istore          12
        //  3561: iload           12
        //  3563: iflt            3644
        //  3566: aload_2        
        //  3567: astore          33
        //  3569: aload           25
        //  3571: iconst_1       
        //  3572: invokevirtual   org/telegram/messenger/video/OutputSurface.drawImage:(Z)V
        //  3575: aload_2        
        //  3576: astore          33
        //  3578: aload           25
        //  3580: invokevirtual   org/telegram/messenger/video/OutputSurface.getFrame:()Ljava/nio/ByteBuffer;
        //  3583: astore          39
        //  3585: aload           43
        //  3587: iload           12
        //  3589: aaload         
        //  3590: astore          25
        //  3592: aload_2        
        //  3593: astore          33
        //  3595: aload           25
        //  3597: invokevirtual   java/nio/ByteBuffer.clear:()Ljava/nio/Buffer;
        //  3600: pop            
        //  3601: aload_2        
        //  3602: astore          33
        //  3604: aload           39
        //  3606: aload           25
        //  3608: iload           31
        //  3610: iload           18
        //  3612: iload           19
        //  3614: iload           37
        //  3616: iload           32
        //  3618: invokestatic    org/telegram/messenger/Utilities.convertVideoFrame:(Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;IIIII)I
        //  3621: pop            
        //  3622: aload_2        
        //  3623: astore          33
        //  3625: aload           38
        //  3627: iload           12
        //  3629: iconst_0       
        //  3630: iload           36
        //  3632: aload           40
        //  3634: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3637: iconst_0       
        //  3638: invokevirtual   android/media/MediaCodec.queueInputBuffer:(IIIJI)V
        //  3641: goto            3665
        //  3644: aload_2        
        //  3645: astore          33
        //  3647: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  3650: ifeq            3665
        //  3653: aload_2        
        //  3654: astore          33
        //  3656: ldc_w           "input buffer not available"
        //  3659: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  3662: goto            3665
        //  3665: aload_2        
        //  3666: astore          33
        //  3668: aload           40
        //  3670: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //  3673: iconst_4       
        //  3674: iand           
        //  3675: ifeq            3757
        //  3678: aload_2        
        //  3679: astore          33
        //  3681: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  3684: ifeq            3696
        //  3687: aload_2        
        //  3688: astore          33
        //  3690: ldc_w           "decoder stream end"
        //  3693: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  3696: aload_2        
        //  3697: astore          33
        //  3699: getstatic       android/os/Build$VERSION.SDK_INT:I
        //  3702: bipush          18
        //  3704: if_icmplt       3718
        //  3707: aload_2        
        //  3708: astore          33
        //  3710: aload           38
        //  3712: invokevirtual   android/media/MediaCodec.signalEndOfInputStream:()V
        //  3715: goto            3754
        //  3718: aload_2        
        //  3719: astore          33
        //  3721: aload           38
        //  3723: ldc2_w          2500
        //  3726: invokevirtual   android/media/MediaCodec.dequeueInputBuffer:(J)I
        //  3729: istore          12
        //  3731: iload           12
        //  3733: iflt            3754
        //  3736: aload_2        
        //  3737: astore          33
        //  3739: aload           38
        //  3741: iload           12
        //  3743: iconst_0       
        //  3744: iconst_1       
        //  3745: aload           40
        //  3747: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //  3750: iconst_4       
        //  3751: invokevirtual   android/media/MediaCodec.queueInputBuffer:(IIIJI)V
        //  3754: goto            3150
        //  3757: goto            3831
        //  3760: aload_2        
        //  3761: astore          33
        //  3763: new             Ljava/lang/RuntimeException;
        //  3766: astore          34
        //  3768: aload_2        
        //  3769: astore          33
        //  3771: new             Ljava/lang/StringBuilder;
        //  3774: astore          42
        //  3776: aload_2        
        //  3777: astore          33
        //  3779: aload           42
        //  3781: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3784: aload_2        
        //  3785: astore          33
        //  3787: aload           42
        //  3789: ldc_w           "unexpected result from decoder.dequeueOutputBuffer: "
        //  3792: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3795: pop            
        //  3796: aload_2        
        //  3797: astore          33
        //  3799: aload           42
        //  3801: iload           12
        //  3803: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  3806: pop            
        //  3807: aload_2        
        //  3808: astore          33
        //  3810: aload           34
        //  3812: aload           42
        //  3814: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3817: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //  3820: aload_2        
        //  3821: astore          33
        //  3823: aload           34
        //  3825: athrow         
        //  3826: astore          34
        //  3828: goto            4038
        //  3831: aload           40
        //  3833: astore          33
        //  3835: aload           42
        //  3837: astore_2       
        //  3838: iload           8
        //  3840: istore          12
        //  3842: iload           13
        //  3844: istore          8
        //  3846: iload           12
        //  3848: istore          13
        //  3850: aload           41
        //  3852: astore          42
        //  3854: goto            2447
        //  3857: astore          33
        //  3859: aload_3        
        //  3860: astore          34
        //  3862: aload_2        
        //  3863: astore          42
        //  3865: aload           33
        //  3867: astore_3       
        //  3868: aload           34
        //  3870: astore_2       
        //  3871: aload           42
        //  3873: astore          33
        //  3875: iload           11
        //  3877: istore          9
        //  3879: goto            4093
        //  3882: aload_2        
        //  3883: astore          33
        //  3885: new             Ljava/lang/RuntimeException;
        //  3888: astore          42
        //  3890: aload_2        
        //  3891: astore          33
        //  3893: new             Ljava/lang/StringBuilder;
        //  3896: astore          34
        //  3898: aload_2        
        //  3899: astore          33
        //  3901: aload           34
        //  3903: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3906: aload_2        
        //  3907: astore          33
        //  3909: aload           34
        //  3911: ldc_w           "encoderOutputBuffer "
        //  3914: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3917: pop            
        //  3918: aload_2        
        //  3919: astore          33
        //  3921: aload           34
        //  3923: iload           53
        //  3925: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  3928: pop            
        //  3929: aload_2        
        //  3930: astore          33
        //  3932: aload           34
        //  3934: ldc_w           " was null"
        //  3937: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3940: pop            
        //  3941: aload_2        
        //  3942: astore          33
        //  3944: aload           42
        //  3946: aload           34
        //  3948: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  3951: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //  3954: aload_2        
        //  3955: astore          33
        //  3957: aload           42
        //  3959: athrow         
        //  3960: aload_2        
        //  3961: astore          33
        //  3963: new             Ljava/lang/RuntimeException;
        //  3966: astore          34
        //  3968: aload_2        
        //  3969: astore          33
        //  3971: new             Ljava/lang/StringBuilder;
        //  3974: astore          42
        //  3976: aload_2        
        //  3977: astore          33
        //  3979: aload           42
        //  3981: invokespecial   java/lang/StringBuilder.<init>:()V
        //  3984: aload_2        
        //  3985: astore          33
        //  3987: aload           42
        //  3989: ldc_w           "unexpected result from encoder.dequeueOutputBuffer: "
        //  3992: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3995: pop            
        //  3996: aload_2        
        //  3997: astore          33
        //  3999: aload           42
        //  4001: iload           53
        //  4003: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //  4006: pop            
        //  4007: aload_2        
        //  4008: astore          33
        //  4010: aload           34
        //  4012: aload           42
        //  4014: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4017: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //  4020: aload_2        
        //  4021: astore          33
        //  4023: aload           34
        //  4025: athrow         
        //  4026: aload_3        
        //  4027: astore_2       
        //  4028: astore_3       
        //  4029: iload           11
        //  4031: istore          9
        //  4033: goto            4093
        //  4036: astore          34
        //  4038: aload_2        
        //  4039: astore          33
        //  4041: aload_3        
        //  4042: astore_2       
        //  4043: aload           34
        //  4045: astore_3       
        //  4046: iload           11
        //  4048: istore          9
        //  4050: goto            4093
        //  4053: iconst_0       
        //  4054: istore          15
        //  4056: aload_3        
        //  4057: astore          33
        //  4059: aload           38
        //  4061: astore          34
        //  4063: goto            4234
        //  4066: astore          33
        //  4068: iload           11
        //  4070: istore          9
        //  4072: aload           41
        //  4074: astore_2       
        //  4075: aload           40
        //  4077: astore_3       
        //  4078: aload           42
        //  4080: astore          26
        //  4082: goto            4103
        //  4085: astore          34
        //  4087: aload_3        
        //  4088: astore          33
        //  4090: aload           34
        //  4092: astore_3       
        //  4093: aload_3        
        //  4094: astore          34
        //  4096: aload           33
        //  4098: astore_3       
        //  4099: aload           34
        //  4101: astore          33
        //  4103: aload_2        
        //  4104: astore          34
        //  4106: aload_3        
        //  4107: astore_2       
        //  4108: aload           34
        //  4110: astore_3       
        //  4111: aload           38
        //  4113: astore          34
        //  4115: goto            4221
        //  4118: astore          33
        //  4120: aload_2        
        //  4121: astore          26
        //  4123: goto            4128
        //  4126: astore          33
        //  4128: iload           10
        //  4130: istore          9
        //  4132: aconst_null    
        //  4133: astore          42
        //  4135: aload_3        
        //  4136: astore_2       
        //  4137: aload           26
        //  4139: astore_3       
        //  4140: aload           38
        //  4142: astore          34
        //  4144: aload           42
        //  4146: astore          26
        //  4148: goto            4221
        //  4151: astore          33
        //  4153: aconst_null    
        //  4154: astore_2       
        //  4155: aload_2        
        //  4156: astore          42
        //  4158: aload           26
        //  4160: astore_3       
        //  4161: aload           38
        //  4163: astore          34
        //  4165: aload           42
        //  4167: astore          26
        //  4169: goto            4221
        //  4172: astore_2       
        //  4173: goto            4177
        //  4176: astore_2       
        //  4177: iload           10
        //  4179: istore          9
        //  4181: aload_2        
        //  4182: astore          33
        //  4184: aconst_null    
        //  4185: astore_2       
        //  4186: aload_2        
        //  4187: astore_3       
        //  4188: aload_3        
        //  4189: astore          26
        //  4191: aload           38
        //  4193: astore          34
        //  4195: goto            4221
        //  4198: astore_2       
        //  4199: goto            4203
        //  4202: astore_2       
        //  4203: iload           10
        //  4205: istore          9
        //  4207: aload_2        
        //  4208: astore          33
        //  4210: aconst_null    
        //  4211: astore_2       
        //  4212: aload_2        
        //  4213: astore_3       
        //  4214: aload_3        
        //  4215: astore          34
        //  4217: aload           34
        //  4219: astore          26
        //  4221: aload           33
        //  4223: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  4226: iconst_1       
        //  4227: istore          15
        //  4229: aload_2        
        //  4230: astore          33
        //  4232: aload_3        
        //  4233: astore_2       
        //  4234: aload           29
        //  4236: iload           9
        //  4238: invokevirtual   android/media/MediaExtractor.unselectTrack:(I)V
        //  4241: aload           26
        //  4243: ifnull          4251
        //  4246: aload           26
        //  4248: invokevirtual   org/telegram/messenger/video/OutputSurface.release:()V
        //  4251: aload_2        
        //  4252: ifnull          4259
        //  4255: aload_2        
        //  4256: invokevirtual   org/telegram/messenger/video/InputSurface.release:()V
        //  4259: aload           33
        //  4261: ifnull          4274
        //  4264: aload           33
        //  4266: invokevirtual   android/media/MediaCodec.stop:()V
        //  4269: aload           33
        //  4271: invokevirtual   android/media/MediaCodec.release:()V
        //  4274: aload           34
        //  4276: ifnull          4289
        //  4279: aload           34
        //  4281: invokevirtual   android/media/MediaCodec.stop:()V
        //  4284: aload           34
        //  4286: invokevirtual   android/media/MediaCodec.release:()V
        //  4289: aload_0        
        //  4290: invokespecial   org/telegram/messenger/MediaController.checkConversionCanceled:()V
        //  4293: goto            4303
        //  4296: astore_2       
        //  4297: goto            4389
        //  4300: iconst_0       
        //  4301: istore          15
        //  4303: aload           28
        //  4305: invokevirtual   android/media/MediaExtractor.release:()V
        //  4308: aload           27
        //  4310: ifnull          4326
        //  4313: aload           27
        //  4315: invokevirtual   org/telegram/messenger/video/MP4Builder.finishMovie:()V
        //  4318: goto            4326
        //  4321: astore_2       
        //  4322: aload_2        
        //  4323: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  4326: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  4329: ifeq            4366
        //  4332: new             Ljava/lang/StringBuilder;
        //  4335: dup            
        //  4336: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4339: astore_2       
        //  4340: aload_2        
        //  4341: ldc_w           "time = "
        //  4344: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4347: pop            
        //  4348: aload_2        
        //  4349: invokestatic    java/lang/System.currentTimeMillis:()J
        //  4352: lload           23
        //  4354: lsub           
        //  4355: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  4358: pop            
        //  4359: aload_2        
        //  4360: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4363: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  4366: goto            4509
        //  4369: astore_1       
        //  4370: goto            4378
        //  4373: astore_2       
        //  4374: goto            4389
        //  4377: astore_1       
        //  4378: aload           28
        //  4380: astore_2       
        //  4381: aload           27
        //  4383: astore          26
        //  4385: goto            4545
        //  4388: astore_2       
        //  4389: aload_2        
        //  4390: astore_3       
        //  4391: aload           28
        //  4393: astore_2       
        //  4394: aload           27
        //  4396: astore          26
        //  4398: goto            4436
        //  4401: astore_1       
        //  4402: aconst_null    
        //  4403: astore_2       
        //  4404: aload           27
        //  4406: astore          26
        //  4408: goto            4545
        //  4411: astore_3       
        //  4412: aconst_null    
        //  4413: astore_2       
        //  4414: aload           27
        //  4416: astore          26
        //  4418: goto            4436
        //  4421: astore_1       
        //  4422: aconst_null    
        //  4423: astore_2       
        //  4424: aload_2        
        //  4425: astore          26
        //  4427: goto            4545
        //  4430: astore_3       
        //  4431: aconst_null    
        //  4432: astore_2       
        //  4433: aload_2        
        //  4434: astore          26
        //  4436: aload_3        
        //  4437: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  4440: aload_2        
        //  4441: ifnull          4448
        //  4444: aload_2        
        //  4445: invokevirtual   android/media/MediaExtractor.release:()V
        //  4448: aload           26
        //  4450: ifnull          4466
        //  4453: aload           26
        //  4455: invokevirtual   org/telegram/messenger/video/MP4Builder.finishMovie:()V
        //  4458: goto            4466
        //  4461: astore_2       
        //  4462: aload_2        
        //  4463: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  4466: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  4469: ifeq            4506
        //  4472: new             Ljava/lang/StringBuilder;
        //  4475: dup            
        //  4476: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4479: astore_2       
        //  4480: aload_2        
        //  4481: ldc_w           "time = "
        //  4484: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4487: pop            
        //  4488: aload_2        
        //  4489: invokestatic    java/lang/System.currentTimeMillis:()J
        //  4492: lload           23
        //  4494: lsub           
        //  4495: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  4498: pop            
        //  4499: aload_2        
        //  4500: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4503: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  4506: iconst_1       
        //  4507: istore          15
        //  4509: aload           21
        //  4511: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //  4516: ldc_w           "isPreviousOk"
        //  4519: iconst_1       
        //  4520: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //  4525: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //  4530: pop            
        //  4531: aload_0        
        //  4532: aload_1        
        //  4533: aload           16
        //  4535: iconst_1       
        //  4536: lconst_0       
        //  4537: iload           15
        //  4539: invokespecial   org/telegram/messenger/MediaController.didWriteData:(Lorg/telegram/messenger/MessageObject;Ljava/io/File;ZJZ)V
        //  4542: iconst_1       
        //  4543: ireturn        
        //  4544: astore_1       
        //  4545: aload_2        
        //  4546: ifnull          4553
        //  4549: aload_2        
        //  4550: invokevirtual   android/media/MediaExtractor.release:()V
        //  4553: aload           26
        //  4555: ifnull          4571
        //  4558: aload           26
        //  4560: invokevirtual   org/telegram/messenger/video/MP4Builder.finishMovie:()V
        //  4563: goto            4571
        //  4566: astore_2       
        //  4567: aload_2        
        //  4568: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  4571: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //  4574: ifeq            4611
        //  4577: new             Ljava/lang/StringBuilder;
        //  4580: dup            
        //  4581: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4584: astore_2       
        //  4585: aload_2        
        //  4586: ldc_w           "time = "
        //  4589: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4592: pop            
        //  4593: aload_2        
        //  4594: invokestatic    java/lang/System.currentTimeMillis:()J
        //  4597: lload           23
        //  4599: lsub           
        //  4600: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //  4603: pop            
        //  4604: aload_2        
        //  4605: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  4608: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //  4611: aload_1        
        //  4612: athrow         
        //  4613: aload           21
        //  4615: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //  4620: ldc_w           "isPreviousOk"
        //  4623: iconst_1       
        //  4624: invokeinterface android/content/SharedPreferences$Editor.putBoolean:(Ljava/lang/String;Z)Landroid/content/SharedPreferences$Editor;
        //  4629: invokeinterface android/content/SharedPreferences$Editor.commit:()Z
        //  4634: pop            
        //  4635: aload_0        
        //  4636: aload_1        
        //  4637: aload           16
        //  4639: iconst_1       
        //  4640: lconst_0       
        //  4641: iconst_1       
        //  4642: invokespecial   org/telegram/messenger/MediaController.didWriteData:(Lorg/telegram/messenger/MessageObject;Ljava/io/File;ZJZ)V
        //  4645: goto            370
        //  4648: iconst_0       
        //  4649: ireturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  392    450    4430   4436   Ljava/lang/Exception;
        //  392    450    4421   4430   Any
        //  450    460    4411   4421   Ljava/lang/Exception;
        //  450    460    4401   4411   Any
        //  460    470    4388   4389   Ljava/lang/Exception;
        //  460    470    4377   4378   Any
        //  489    498    544    548    Ljava/lang/Exception;
        //  489    498    4377   4378   Any
        //  521    541    583    584    Ljava/lang/Exception;
        //  521    541    4369   4373   Any
        //  552    561    4373   4377   Ljava/lang/Exception;
        //  552    561    4369   4373   Any
        //  571    580    583    584    Ljava/lang/Exception;
        //  571    580    4369   4373   Any
        //  604    616    4202   4203   Ljava/lang/Exception;
        //  604    616    4369   4373   Any
        //  627    641    883    885    Ljava/lang/Exception;
        //  627    641    4369   4373   Any
        //  646    691    883    885    Ljava/lang/Exception;
        //  646    691    4369   4373   Any
        //  709    720    883    885    Ljava/lang/Exception;
        //  709    720    4369   4373   Any
        //  726    737    883    885    Ljava/lang/Exception;
        //  726    737    4369   4373   Any
        //  743    754    883    885    Ljava/lang/Exception;
        //  743    754    4369   4373   Any
        //  760    771    883    885    Ljava/lang/Exception;
        //  760    771    4369   4373   Any
        //  783    860    883    885    Ljava/lang/Exception;
        //  783    860    4369   4373   Any
        //  870    883    883    885    Ljava/lang/Exception;
        //  870    883    4369   4373   Any
        //  913    918    4202   4203   Ljava/lang/Exception;
        //  913    918    4369   4373   Any
        //  923    931    956    957    Ljava/lang/Exception;
        //  923    931    4369   4373   Any
        //  931    953    1039   1043   Ljava/lang/Exception;
        //  931    953    4369   4373   Any
        //  974    982    4198   4202   Ljava/lang/Exception;
        //  974    982    4369   4373   Any
        //  1014   1025   1039   1043   Ljava/lang/Exception;
        //  1014   1025   4369   4373   Any
        //  1049   1062   1039   1043   Ljava/lang/Exception;
        //  1049   1062   4369   4373   Any
        //  1102   1112   1039   1043   Ljava/lang/Exception;
        //  1102   1112   4369   4373   Any
        //  1131   1139   1039   1043   Ljava/lang/Exception;
        //  1131   1139   4369   4373   Any
        //  1152   1168   4198   4202   Ljava/lang/Exception;
        //  1152   1168   4369   4373   Any
        //  1173   1209   1039   1043   Ljava/lang/Exception;
        //  1173   1209   4369   4373   Any
        //  1226   1234   1039   1043   Ljava/lang/Exception;
        //  1226   1234   4369   4373   Any
        //  1237   1244   4198   4202   Ljava/lang/Exception;
        //  1237   1244   4369   4373   Any
        //  1244   1263   4198   4202   Ljava/lang/Exception;
        //  1244   1263   4369   4373   Any
        //  1276   1285   4198   4202   Ljava/lang/Exception;
        //  1276   1285   4369   4373   Any
        //  1301   1323   4198   4202   Ljava/lang/Exception;
        //  1301   1323   4369   4373   Any
        //  1330   1351   1039   1043   Ljava/lang/Exception;
        //  1330   1351   4369   4373   Any
        //  1351   1358   4198   4202   Ljava/lang/Exception;
        //  1351   1358   4369   4373   Any
        //  1358   1367   4176   4177   Ljava/lang/Exception;
        //  1358   1367   4369   4373   Any
        //  1367   1372   4172   4176   Ljava/lang/Exception;
        //  1367   1372   4369   4373   Any
        //  1379   1392   1418   1431   Ljava/lang/Exception;
        //  1379   1392   4369   4373   Any
        //  1392   1396   1402   1418   Ljava/lang/Exception;
        //  1392   1396   4369   4373   Any
        //  1434   1451   4151   4172   Ljava/lang/Exception;
        //  1434   1451   4369   4373   Any
        //  1451   1456   4126   4128   Ljava/lang/Exception;
        //  1451   1456   4369   4373   Any
        //  1466   1476   1479   1496   Ljava/lang/Exception;
        //  1466   1476   4369   4373   Any
        //  1496   1511   4118   4126   Ljava/lang/Exception;
        //  1496   1511   4369   4373   Any
        //  1511   1518   4085   4093   Ljava/lang/Exception;
        //  1511   1518   4369   4373   Any
        //  1532   1542   4066   4085   Ljava/lang/Exception;
        //  1532   1542   4369   4373   Any
        //  1556   1560   4066   4085   Ljava/lang/Exception;
        //  1556   1560   4369   4373   Any
        //  1574   1579   4066   4085   Ljava/lang/Exception;
        //  1574   1579   4369   4373   Any
        //  1600   1606   1684   1707   Ljava/lang/Exception;
        //  1600   1606   4369   4373   Any
        //  1620   1627   1684   1707   Ljava/lang/Exception;
        //  1620   1627   4369   4373   Any
        //  1641   1649   1684   1707   Ljava/lang/Exception;
        //  1641   1649   4369   4373   Any
        //  1663   1670   1684   1707   Ljava/lang/Exception;
        //  1663   1670   4369   4373   Any
        //  1734   1738   4066   4085   Ljava/lang/Exception;
        //  1734   1738   4369   4373   Any
        //  1803   1807   4066   4085   Ljava/lang/Exception;
        //  1803   1807   4369   4373   Any
        //  1812   1819   2389   2391   Ljava/lang/Exception;
        //  1812   1819   4369   4373   Any
        //  1840   1849   1684   1707   Ljava/lang/Exception;
        //  1840   1849   4369   4373   Any
        //  1872   1880   1684   1707   Ljava/lang/Exception;
        //  1872   1880   4369   4373   Any
        //  1904   1912   1684   1707   Ljava/lang/Exception;
        //  1904   1912   4369   4373   Any
        //  1926   1936   1684   1707   Ljava/lang/Exception;
        //  1926   1936   4369   4373   Any
        //  1955   1965   1684   1707   Ljava/lang/Exception;
        //  1955   1965   4369   4373   Any
        //  1985   2000   1684   1707   Ljava/lang/Exception;
        //  1985   2000   4369   4373   Any
        //  2014   2020   1684   1707   Ljava/lang/Exception;
        //  2014   2020   4369   4373   Any
        //  2040   2050   2266   2271   Ljava/lang/Exception;
        //  2040   2050   4369   4373   Any
        //  2050   2062   2256   2258   Ljava/lang/Exception;
        //  2050   2062   4369   4373   Any
        //  2069   2087   2090   2092   Ljava/lang/Exception;
        //  2069   2087   4369   4373   Any
        //  2107   2114   2256   2258   Ljava/lang/Exception;
        //  2107   2114   4369   4373   Any
        //  2119   2135   2138   2143   Ljava/lang/Exception;
        //  2119   2135   4369   4373   Any
        //  2143   2149   2251   2256   Ljava/lang/Exception;
        //  2143   2149   4369   4373   Any
        //  2152   2159   2251   2256   Ljava/lang/Exception;
        //  2152   2159   4369   4373   Any
        //  2171   2178   2138   2143   Ljava/lang/Exception;
        //  2171   2178   4369   4373   Any
        //  2186   2216   2251   2256   Ljava/lang/Exception;
        //  2186   2216   4369   4373   Any
        //  2223   2234   2237   2242   Ljava/lang/Exception;
        //  2223   2234   4369   4373   Any
        //  2321   2331   2365   2377   Ljava/lang/Exception;
        //  2321   2331   4369   4373   Any
        //  2348   2359   2365   2377   Ljava/lang/Exception;
        //  2348   2359   4369   4373   Any
        //  2495   2499   4036   4038   Ljava/lang/Exception;
        //  2495   2499   4369   4373   Any
        //  2503   2515   4036   4038   Ljava/lang/Exception;
        //  2503   2515   4369   4373   Any
        //  2572   2580   2365   2377   Ljava/lang/Exception;
        //  2572   2580   4369   4373   Any
        //  2590   2597   2365   2377   Ljava/lang/Exception;
        //  2590   2597   4369   4373   Any
        //  2621   2628   2365   2377   Ljava/lang/Exception;
        //  2621   2628   4369   4373   Any
        //  2653   2663   2365   2377   Ljava/lang/Exception;
        //  2653   2663   4369   4373   Any
        //  2675   2680   4036   4038   Ljava/lang/Exception;
        //  2675   2680   4369   4373   Any
        //  2697   2706   4036   4038   Ljava/lang/Exception;
        //  2697   2706   4369   4373   Any
        //  2711   2718   4036   4038   Ljava/lang/Exception;
        //  2711   2718   4369   4373   Any
        //  2724   2748   3026   3028   Ljava/lang/Exception;
        //  2724   2748   4369   4373   Any
        //  2755   2766   3014   3019   Ljava/lang/Exception;
        //  2755   2766   4369   4373   Any
        //  2779   2833   3014   3019   Ljava/lang/Exception;
        //  2779   2833   4369   4373   Any
        //  2887   2944   3014   3019   Ljava/lang/Exception;
        //  2887   2944   4369   4373   Any
        //  2960   2971   3014   3019   Ljava/lang/Exception;
        //  2960   2971   4369   4373   Any
        //  2981   3001   3014   3019   Ljava/lang/Exception;
        //  2981   3001   4369   4373   Any
        //  3001   3011   3014   3019   Ljava/lang/Exception;
        //  3001   3011   4369   4373   Any
        //  3043   3053   3857   3882   Ljava/lang/Exception;
        //  3043   3053   4369   4373   Any
        //  3062   3070   3857   3882   Ljava/lang/Exception;
        //  3062   3070   4369   4373   Any
        //  3133   3144   3826   3831   Ljava/lang/Exception;
        //  3133   3144   4369   4373   Any
        //  3173   3220   3223   3228   Ljava/lang/Exception;
        //  3173   3220   4369   4373   Any
        //  3233   3238   3826   3831   Ljava/lang/Exception;
        //  3233   3238   4369   4373   Any
        //  3245   3252   3223   3228   Ljava/lang/Exception;
        //  3245   3252   4369   4373   Any
        //  3269   3276   3826   3831   Ljava/lang/Exception;
        //  3269   3276   4369   4373   Any
        //  3281   3291   3223   3228   Ljava/lang/Exception;
        //  3281   3291   4369   4373   Any
        //  3301   3324   3223   3228   Ljava/lang/Exception;
        //  3301   3324   4369   4373   Any
        //  3352   3424   3223   3228   Ljava/lang/Exception;
        //  3352   3424   4369   4373   Any
        //  3433   3440   3223   3228   Ljava/lang/Exception;
        //  3433   3440   4369   4373   Any
        //  3443   3451   3826   3831   Ljava/lang/Exception;
        //  3443   3451   4369   4373   Any
        //  3456   3461   3467   3477   Ljava/lang/Exception;
        //  3456   3461   4369   4373   Any
        //  3469   3474   3826   3831   Ljava/lang/Exception;
        //  3469   3474   4369   4373   Any
        //  3482   3487   3826   3831   Ljava/lang/Exception;
        //  3482   3487   4369   4373   Any
        //  3494   3507   3536   3544   Ljava/lang/Exception;
        //  3494   3507   4369   4373   Any
        //  3513   3524   4026   4036   Ljava/lang/Exception;
        //  3513   3524   4369   4373   Any
        //  3527   3533   4026   4036   Ljava/lang/Exception;
        //  3527   3533   4369   4373   Any
        //  3551   3561   4026   4036   Ljava/lang/Exception;
        //  3551   3561   4369   4373   Any
        //  3569   3575   4026   4036   Ljava/lang/Exception;
        //  3569   3575   4369   4373   Any
        //  3578   3585   4026   4036   Ljava/lang/Exception;
        //  3578   3585   4369   4373   Any
        //  3595   3601   4026   4036   Ljava/lang/Exception;
        //  3595   3601   4369   4373   Any
        //  3604   3622   4026   4036   Ljava/lang/Exception;
        //  3604   3622   4369   4373   Any
        //  3625   3641   4026   4036   Ljava/lang/Exception;
        //  3625   3641   4369   4373   Any
        //  3647   3653   4026   4036   Ljava/lang/Exception;
        //  3647   3653   4369   4373   Any
        //  3656   3662   4026   4036   Ljava/lang/Exception;
        //  3656   3662   4369   4373   Any
        //  3668   3678   4026   4036   Ljava/lang/Exception;
        //  3668   3678   4369   4373   Any
        //  3681   3687   4026   4036   Ljava/lang/Exception;
        //  3681   3687   4369   4373   Any
        //  3690   3696   4026   4036   Ljava/lang/Exception;
        //  3690   3696   4369   4373   Any
        //  3699   3707   4026   4036   Ljava/lang/Exception;
        //  3699   3707   4369   4373   Any
        //  3710   3715   4026   4036   Ljava/lang/Exception;
        //  3710   3715   4369   4373   Any
        //  3721   3731   4026   4036   Ljava/lang/Exception;
        //  3721   3731   4369   4373   Any
        //  3739   3754   4026   4036   Ljava/lang/Exception;
        //  3739   3754   4369   4373   Any
        //  3763   3768   4026   4036   Ljava/lang/Exception;
        //  3763   3768   4369   4373   Any
        //  3771   3776   4026   4036   Ljava/lang/Exception;
        //  3771   3776   4369   4373   Any
        //  3779   3784   4026   4036   Ljava/lang/Exception;
        //  3779   3784   4369   4373   Any
        //  3787   3796   4026   4036   Ljava/lang/Exception;
        //  3787   3796   4369   4373   Any
        //  3799   3807   4026   4036   Ljava/lang/Exception;
        //  3799   3807   4369   4373   Any
        //  3810   3820   4026   4036   Ljava/lang/Exception;
        //  3810   3820   4369   4373   Any
        //  3823   3826   4026   4036   Ljava/lang/Exception;
        //  3823   3826   4369   4373   Any
        //  3885   3890   4026   4036   Ljava/lang/Exception;
        //  3885   3890   4369   4373   Any
        //  3893   3898   4026   4036   Ljava/lang/Exception;
        //  3893   3898   4369   4373   Any
        //  3901   3906   4026   4036   Ljava/lang/Exception;
        //  3901   3906   4369   4373   Any
        //  3909   3918   4026   4036   Ljava/lang/Exception;
        //  3909   3918   4369   4373   Any
        //  3921   3929   4026   4036   Ljava/lang/Exception;
        //  3921   3929   4369   4373   Any
        //  3932   3941   4026   4036   Ljava/lang/Exception;
        //  3932   3941   4369   4373   Any
        //  3944   3954   4026   4036   Ljava/lang/Exception;
        //  3944   3954   4369   4373   Any
        //  3957   3960   4026   4036   Ljava/lang/Exception;
        //  3957   3960   4369   4373   Any
        //  3963   3968   4026   4036   Ljava/lang/Exception;
        //  3963   3968   4369   4373   Any
        //  3971   3976   4026   4036   Ljava/lang/Exception;
        //  3971   3976   4369   4373   Any
        //  3979   3984   4026   4036   Ljava/lang/Exception;
        //  3979   3984   4369   4373   Any
        //  3987   3996   4026   4036   Ljava/lang/Exception;
        //  3987   3996   4369   4373   Any
        //  3999   4007   4026   4036   Ljava/lang/Exception;
        //  3999   4007   4369   4373   Any
        //  4010   4020   4026   4036   Ljava/lang/Exception;
        //  4010   4020   4369   4373   Any
        //  4023   4026   4026   4036   Ljava/lang/Exception;
        //  4023   4026   4369   4373   Any
        //  4221   4226   4296   4300   Ljava/lang/Exception;
        //  4221   4226   4369   4373   Any
        //  4234   4241   4296   4300   Ljava/lang/Exception;
        //  4234   4241   4369   4373   Any
        //  4246   4251   4296   4300   Ljava/lang/Exception;
        //  4246   4251   4369   4373   Any
        //  4255   4259   4296   4300   Ljava/lang/Exception;
        //  4255   4259   4369   4373   Any
        //  4264   4274   4296   4300   Ljava/lang/Exception;
        //  4264   4274   4369   4373   Any
        //  4279   4289   4296   4300   Ljava/lang/Exception;
        //  4279   4289   4369   4373   Any
        //  4289   4293   4296   4300   Ljava/lang/Exception;
        //  4289   4293   4369   4373   Any
        //  4313   4318   4321   4326   Ljava/lang/Exception;
        //  4436   4440   4544   4545   Any
        //  4453   4458   4461   4466   Ljava/lang/Exception;
        //  4558   4563   4566   4571   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0521:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static String copyFileToCache(Uri openInputStream, String absolutePath) {
        Object o = null;
        Object o3 = null;
        Label_0282: {
            try {
                String child;
                if ((child = FileLoader.fixFileName(getFileName((Uri)openInputStream))) == null) {
                    final int lastLocalId = SharedConfig.getLastLocalId();
                    SharedConfig.saveConfig();
                    child = String.format(Locale.US, "%d.%s", lastLocalId, absolutePath);
                }
                final File parent = new File(FileLoader.getDirectory(4), "sharing/");
                parent.mkdirs();
                final File file = new File(parent, child);
                if (AndroidUtilities.isInternalUri(Uri.fromFile(file))) {
                    return null;
                }
                openInputStream = (Exception)ApplicationLoader.applicationContext.getContentResolver().openInputStream((Uri)openInputStream);
                try {
                    final Object o2 = new FileOutputStream(file);
                    try {
                        o = new byte[20480];
                        while (true) {
                            final int read = ((InputStream)openInputStream).read((byte[])o);
                            if (read == -1) {
                                break;
                            }
                            ((FileOutputStream)o2).write((byte[])o, 0, read);
                        }
                        absolutePath = (Exception)file.getAbsolutePath();
                        if (openInputStream != null) {
                            try {
                                ((InputStream)openInputStream).close();
                            }
                            catch (Exception openInputStream) {
                                FileLog.e(openInputStream);
                            }
                        }
                        try {
                            ((FileOutputStream)o2).close();
                        }
                        catch (Exception openInputStream) {
                            FileLog.e(openInputStream);
                        }
                        return (String)absolutePath;
                    }
                    catch (Exception o) {
                        openInputStream = (Exception)o2;
                    }
                }
                catch (Exception ex5) {
                    o = null;
                }
            }
            catch (Exception o3) {
                absolutePath = (openInputStream = null);
            }
            finally {
                o3 = null;
                openInputStream = (Exception)o;
                break Label_0282;
            }
            try {
                FileLog.e((Throwable)o3);
                if (absolutePath != null) {
                    try {
                        ((InputStream)absolutePath).close();
                    }
                    catch (Exception absolutePath) {
                        FileLog.e(absolutePath);
                    }
                }
                if (openInputStream != null) {
                    try {
                        ((FileOutputStream)openInputStream).close();
                    }
                    catch (Exception openInputStream) {
                        FileLog.e(openInputStream);
                    }
                }
                return null;
            }
            finally {
                final Exception ex = absolutePath;
                o3 = openInputStream;
                final Exception ex2;
                absolutePath = ex2;
                openInputStream = ex;
            }
        }
        if (openInputStream != null) {
            try {
                ((InputStream)openInputStream).close();
            }
            catch (Exception ex3) {
                FileLog.e(ex3);
            }
        }
        if (o3 != null) {
            try {
                ((FileOutputStream)o3).close();
            }
            catch (Exception ex4) {
                FileLog.e(ex4);
            }
        }
        throw absolutePath;
    }
    
    private void didWriteData(final MessageObject messageObject, final File file, final boolean b, final long n, final boolean b2) {
        final boolean videoConvertFirstWrite = this.videoConvertFirstWrite;
        if (videoConvertFirstWrite) {
            this.videoConvertFirstWrite = false;
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$fwtpfrjmuwNTgxHqhKcXwShkFp0(this, b2, b, messageObject, file, videoConvertFirstWrite, n));
    }
    
    private int findTrack(final MediaExtractor mediaExtractor, final boolean b) {
        for (int trackCount = mediaExtractor.getTrackCount(), i = 0; i < trackCount; ++i) {
            final String string = mediaExtractor.getTrackFormat(i).getString("mime");
            if (b) {
                if (string.startsWith("audio/")) {
                    return i;
                }
            }
            else if (string.startsWith("video/")) {
                return i;
            }
        }
        return -5;
    }
    
    public static String getFileName(final Uri p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //     4: ldc_w           "content"
        //     7: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    10: istore_1       
        //    11: aconst_null    
        //    12: astore_2       
        //    13: aconst_null    
        //    14: astore_3       
        //    15: aconst_null    
        //    16: astore          4
        //    18: aconst_null    
        //    19: astore          5
        //    21: aload           4
        //    23: astore          6
        //    25: iload_1        
        //    26: ifeq            178
        //    29: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    32: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    35: aload_0        
        //    36: iconst_1       
        //    37: anewarray       Ljava/lang/String;
        //    40: dup            
        //    41: iconst_0       
        //    42: ldc_w           "_display_name"
        //    45: aastore        
        //    46: aconst_null    
        //    47: aconst_null    
        //    48: aconst_null    
        //    49: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    52: astore          7
        //    54: aload           5
        //    56: astore_3       
        //    57: aload           7
        //    59: astore          6
        //    61: aload           7
        //    63: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    68: ifeq            93
        //    71: aload           7
        //    73: astore          6
        //    75: aload           7
        //    77: aload           7
        //    79: ldc_w           "_display_name"
        //    82: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //    87: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //    92: astore_3       
        //    93: aload_3        
        //    94: astore          6
        //    96: aload           7
        //    98: ifnull          178
        //   101: aload_3        
        //   102: astore          6
        //   104: aload           7
        //   106: astore_3       
        //   107: aload_3        
        //   108: invokeinterface android/database/Cursor.close:()V
        //   113: goto            178
        //   116: astore          5
        //   118: aload           7
        //   120: astore_3       
        //   121: goto            138
        //   124: astore          6
        //   126: aload_3        
        //   127: astore_0       
        //   128: aload           6
        //   130: astore_3       
        //   131: goto            166
        //   134: astore          5
        //   136: aconst_null    
        //   137: astore_3       
        //   138: aload_3        
        //   139: astore          6
        //   141: aload           5
        //   143: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   146: aload           4
        //   148: astore          6
        //   150: aload_3        
        //   151: ifnull          178
        //   154: aload_2        
        //   155: astore          6
        //   157: goto            107
        //   160: astore_0       
        //   161: aload_0        
        //   162: astore_3       
        //   163: aload           6
        //   165: astore_0       
        //   166: aload_0        
        //   167: ifnull          176
        //   170: aload_0        
        //   171: invokeinterface android/database/Cursor.close:()V
        //   176: aload_3        
        //   177: athrow         
        //   178: aload           6
        //   180: astore_3       
        //   181: aload           6
        //   183: ifnonnull       216
        //   186: aload_0        
        //   187: invokevirtual   android/net/Uri.getPath:()Ljava/lang/String;
        //   190: astore_0       
        //   191: aload_0        
        //   192: bipush          47
        //   194: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //   197: istore          8
        //   199: aload_0        
        //   200: astore_3       
        //   201: iload           8
        //   203: iconst_m1      
        //   204: if_icmpeq       216
        //   207: aload_0        
        //   208: iload           8
        //   210: iconst_1       
        //   211: iadd           
        //   212: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   215: astore_3       
        //   216: aload_3        
        //   217: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  29     54     134    138    Ljava/lang/Exception;
        //  29     54     124    134    Any
        //  61     71     116    124    Ljava/lang/Exception;
        //  61     71     160    166    Any
        //  75     93     116    124    Ljava/lang/Exception;
        //  75     93     160    166    Any
        //  141    146    160    166    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0093:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static MediaController getInstance() {
        final MediaController instance;
        if ((instance = MediaController.Instance) == null) {
            synchronized (MediaController.class) {
                if (MediaController.Instance == null) {
                    MediaController.Instance = new MediaController();
                }
            }
        }
        return instance;
    }
    
    public static boolean isGif(final Uri p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aconst_null    
        //     3: astore_2       
        //     4: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //     7: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    10: aload_0        
        //    11: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //    14: astore_0       
        //    15: aload_0        
        //    16: astore_2       
        //    17: aload_0        
        //    18: astore_1       
        //    19: iconst_3       
        //    20: newarray        B
        //    22: astore_3       
        //    23: aload_0        
        //    24: astore_2       
        //    25: aload_0        
        //    26: astore_1       
        //    27: aload_0        
        //    28: aload_3        
        //    29: iconst_0       
        //    30: iconst_3       
        //    31: invokevirtual   java/io/InputStream.read:([BII)I
        //    34: iconst_3       
        //    35: if_icmpne       94
        //    38: aload_0        
        //    39: astore_2       
        //    40: aload_0        
        //    41: astore_1       
        //    42: new             Ljava/lang/String;
        //    45: astore          4
        //    47: aload_0        
        //    48: astore_2       
        //    49: aload_0        
        //    50: astore_1       
        //    51: aload           4
        //    53: aload_3        
        //    54: invokespecial   java/lang/String.<init>:([B)V
        //    57: aload_0        
        //    58: astore_2       
        //    59: aload_0        
        //    60: astore_1       
        //    61: aload           4
        //    63: ldc_w           "gif"
        //    66: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //    69: istore          5
        //    71: iload           5
        //    73: ifeq            94
        //    76: aload_0        
        //    77: ifnull          92
        //    80: aload_0        
        //    81: invokevirtual   java/io/InputStream.close:()V
        //    84: goto            92
        //    87: astore_0       
        //    88: aload_0        
        //    89: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //    92: iconst_1       
        //    93: ireturn        
        //    94: aload_0        
        //    95: ifnull          132
        //    98: aload_0        
        //    99: invokevirtual   java/io/InputStream.close:()V
        //   102: goto            132
        //   105: astore_0       
        //   106: goto            134
        //   109: astore_0       
        //   110: aload_1        
        //   111: astore_2       
        //   112: aload_0        
        //   113: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   116: aload_1        
        //   117: ifnull          132
        //   120: aload_1        
        //   121: invokevirtual   java/io/InputStream.close:()V
        //   124: goto            132
        //   127: astore_0       
        //   128: aload_0        
        //   129: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   132: iconst_0       
        //   133: ireturn        
        //   134: aload_2        
        //   135: ifnull          150
        //   138: aload_2        
        //   139: invokevirtual   java/io/InputStream.close:()V
        //   142: goto            150
        //   145: astore_2       
        //   146: aload_2        
        //   147: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   150: aload_0        
        //   151: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      15     109    127    Ljava/lang/Exception;
        //  4      15     105    152    Any
        //  19     23     109    127    Ljava/lang/Exception;
        //  19     23     105    152    Any
        //  27     38     109    127    Ljava/lang/Exception;
        //  27     38     105    152    Any
        //  42     47     109    127    Ljava/lang/Exception;
        //  42     47     105    152    Any
        //  51     57     109    127    Ljava/lang/Exception;
        //  51     57     105    152    Any
        //  61     71     109    127    Ljava/lang/Exception;
        //  61     71     105    152    Any
        //  80     84     87     92     Ljava/lang/Exception;
        //  98     102    127    132    Ljava/lang/Exception;
        //  112    116    105    152    Any
        //  120    124    127    132    Ljava/lang/Exception;
        //  138    142    145    150    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 92 out-of-bounds for length 92
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private boolean isNearToSensor(final float n) {
        return n < 5.0f && n != this.proximitySensor.getMaximumRange();
    }
    
    public static native int isOpusFile(final String p0);
    
    private static boolean isRecognizedFormat(final int n) {
        if (n != 39 && n != 2130706688) {
            switch (n) {
                default: {
                    return false;
                }
                case 19:
                case 20:
                case 21: {
                    break;
                }
            }
        }
        return true;
    }
    
    private boolean isSamePlayingMessage(final MessageObject messageObject) {
        final MessageObject playingMessageObject = this.playingMessageObject;
        boolean b = true;
        if (playingMessageObject == null || playingMessageObject.getDialogId() != messageObject.getDialogId() || this.playingMessageObject.getId() != messageObject.getId() || this.playingMessageObject.eventId == 0L != (messageObject.eventId == 0L)) {
            b = false;
        }
        return b;
    }
    
    public static boolean isWebp(final Uri p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aconst_null    
        //     3: astore_2       
        //     4: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //     7: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    10: aload_0        
        //    11: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //    14: astore_0       
        //    15: aload_0        
        //    16: astore_2       
        //    17: aload_0        
        //    18: astore_1       
        //    19: bipush          12
        //    21: newarray        B
        //    23: astore_3       
        //    24: aload_0        
        //    25: astore_2       
        //    26: aload_0        
        //    27: astore_1       
        //    28: aload_0        
        //    29: aload_3        
        //    30: iconst_0       
        //    31: bipush          12
        //    33: invokevirtual   java/io/InputStream.read:([BII)I
        //    36: bipush          12
        //    38: if_icmpne       120
        //    41: aload_0        
        //    42: astore_2       
        //    43: aload_0        
        //    44: astore_1       
        //    45: new             Ljava/lang/String;
        //    48: astore          4
        //    50: aload_0        
        //    51: astore_2       
        //    52: aload_0        
        //    53: astore_1       
        //    54: aload           4
        //    56: aload_3        
        //    57: invokespecial   java/lang/String.<init>:([B)V
        //    60: aload_0        
        //    61: astore_2       
        //    62: aload_0        
        //    63: astore_1       
        //    64: aload           4
        //    66: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //    69: astore_3       
        //    70: aload_0        
        //    71: astore_2       
        //    72: aload_0        
        //    73: astore_1       
        //    74: aload_3        
        //    75: ldc_w           "riff"
        //    78: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    81: ifeq            120
        //    84: aload_0        
        //    85: astore_2       
        //    86: aload_0        
        //    87: astore_1       
        //    88: aload_3        
        //    89: ldc_w           "webp"
        //    92: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    95: istore          5
        //    97: iload           5
        //    99: ifeq            120
        //   102: aload_0        
        //   103: ifnull          118
        //   106: aload_0        
        //   107: invokevirtual   java/io/InputStream.close:()V
        //   110: goto            118
        //   113: astore_0       
        //   114: aload_0        
        //   115: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   118: iconst_1       
        //   119: ireturn        
        //   120: aload_0        
        //   121: ifnull          158
        //   124: aload_0        
        //   125: invokevirtual   java/io/InputStream.close:()V
        //   128: goto            158
        //   131: astore_0       
        //   132: goto            160
        //   135: astore_0       
        //   136: aload_1        
        //   137: astore_2       
        //   138: aload_0        
        //   139: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   142: aload_1        
        //   143: ifnull          158
        //   146: aload_1        
        //   147: invokevirtual   java/io/InputStream.close:()V
        //   150: goto            158
        //   153: astore_0       
        //   154: aload_0        
        //   155: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   158: iconst_0       
        //   159: ireturn        
        //   160: aload_2        
        //   161: ifnull          176
        //   164: aload_2        
        //   165: invokevirtual   java/io/InputStream.close:()V
        //   168: goto            176
        //   171: astore_2       
        //   172: aload_2        
        //   173: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   176: aload_0        
        //   177: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      15     135    153    Ljava/lang/Exception;
        //  4      15     131    178    Any
        //  19     24     135    153    Ljava/lang/Exception;
        //  19     24     131    178    Any
        //  28     41     135    153    Ljava/lang/Exception;
        //  28     41     131    178    Any
        //  45     50     135    153    Ljava/lang/Exception;
        //  45     50     131    178    Any
        //  54     60     135    153    Ljava/lang/Exception;
        //  54     60     131    178    Any
        //  64     70     135    153    Ljava/lang/Exception;
        //  64     70     131    178    Any
        //  74     84     135    153    Ljava/lang/Exception;
        //  74     84     131    178    Any
        //  88     97     135    153    Ljava/lang/Exception;
        //  88     97     131    178    Any
        //  106    110    113    118    Ljava/lang/Exception;
        //  124    128    153    158    Ljava/lang/Exception;
        //  138    142    131    178    Any
        //  146    150    153    158    Ljava/lang/Exception;
        //  164    168    171    176    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 107 out-of-bounds for length 107
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static void loadGalleryPhotosAlbums(final int n) {
        final Thread thread = new Thread(new _$$Lambda$MediaController$SBcnC_DI67Ol01XNU9e0YE4yw3c(n));
        thread.setPriority(1);
        thread.start();
    }
    
    private void playNextMessageWithoutOrder(final boolean b) {
        ArrayList<MessageObject> list;
        if (SharedConfig.shuffleMusic) {
            list = this.shuffledPlaylist;
        }
        else {
            list = this.playlist;
        }
        if (b && SharedConfig.repeatMode == 2 && !this.forceLoopCurrentPlaylist) {
            this.cleanupPlayer(false, false);
            final MessageObject messageObject = list.get(this.currentPlaylistNum);
            messageObject.audioProgress = 0.0f;
            messageObject.audioProgressSec = 0;
            this.playMessage(messageObject);
            return;
        }
        boolean b2 = false;
        Label_0141: {
            Label_0139: {
                if (SharedConfig.playOrderReversed) {
                    ++this.currentPlaylistNum;
                    if (this.currentPlaylistNum < list.size()) {
                        break Label_0139;
                    }
                    this.currentPlaylistNum = 0;
                }
                else {
                    --this.currentPlaylistNum;
                    if (this.currentPlaylistNum >= 0) {
                        break Label_0139;
                    }
                    this.currentPlaylistNum = list.size() - 1;
                }
                b2 = true;
                break Label_0141;
            }
            b2 = false;
        }
        if (b2 && b && SharedConfig.repeatMode == 0 && !this.forceLoopCurrentPlaylist) {
            if (this.audioPlayer != null || this.videoPlayer != null) {
                final VideoPlayer audioPlayer = this.audioPlayer;
                if (audioPlayer != null) {
                    try {
                        audioPlayer.releasePlayer(true);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    this.audioPlayer = null;
                }
                else {
                    final VideoPlayer videoPlayer = this.videoPlayer;
                    if (videoPlayer != null) {
                        this.currentAspectRatioFrameLayout = null;
                        this.currentTextureViewContainer = null;
                        this.currentAspectRatioFrameLayoutReady = false;
                        this.currentTextureView = null;
                        videoPlayer.releasePlayer(true);
                        this.videoPlayer = null;
                        try {
                            this.baseActivity.getWindow().clearFlags(128);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                        AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                        FileLoader.getInstance(this.playingMessageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
                    }
                }
                this.stopProgressTimer();
                this.lastProgress = 0L;
                this.isPaused = true;
                final MessageObject playingMessageObject = this.playingMessageObject;
                playingMessageObject.audioProgress = 0.0f;
                playingMessageObject.audioProgressSec = 0;
                NotificationCenter.getInstance(playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, this.playingMessageObject.getId(), 0);
                NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, this.playingMessageObject.getId());
            }
            return;
        }
        final int currentPlaylistNum = this.currentPlaylistNum;
        if (currentPlaylistNum >= 0) {
            if (currentPlaylistNum < list.size()) {
                final MessageObject playingMessageObject2 = this.playingMessageObject;
                if (playingMessageObject2 != null) {
                    playingMessageObject2.resetPlayingProgress();
                }
                this.playMusicAgain = true;
                this.playMessage(list.get(this.currentPlaylistNum));
            }
        }
    }
    
    private void processMediaObserver(final Uri p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_2       
        //     2: aconst_null    
        //     3: astore_3       
        //     4: aload_3        
        //     5: astore          4
        //     7: aload_2        
        //     8: astore          5
        //    10: invokestatic    org/telegram/messenger/AndroidUtilities.getRealScreenSize:()Landroid/graphics/Point;
        //    13: astore          6
        //    15: aload_3        
        //    16: astore          4
        //    18: aload_2        
        //    19: astore          5
        //    21: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    24: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    27: aload_1        
        //    28: aload_0        
        //    29: getfield        org/telegram/messenger/MediaController.mediaProjections:[Ljava/lang/String;
        //    32: aconst_null    
        //    33: aconst_null    
        //    34: ldc_w           "date_added DESC LIMIT 1"
        //    37: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    40: astore_1       
        //    41: aload_1        
        //    42: astore          4
        //    44: aload_1        
        //    45: astore          5
        //    47: new             Ljava/util/ArrayList;
        //    50: astore_3       
        //    51: aload_1        
        //    52: astore          4
        //    54: aload_1        
        //    55: astore          5
        //    57: aload_3        
        //    58: invokespecial   java/util/ArrayList.<init>:()V
        //    61: aload_1        
        //    62: ifnull          467
        //    65: aload_1        
        //    66: astore          4
        //    68: aload_1        
        //    69: astore          5
        //    71: aload_1        
        //    72: invokeinterface android/database/Cursor.moveToNext:()Z
        //    77: ifeq            455
        //    80: aload_1        
        //    81: astore          4
        //    83: aload_1        
        //    84: astore          5
        //    86: aload_1        
        //    87: iconst_0       
        //    88: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //    93: astore_2       
        //    94: aload_1        
        //    95: astore          4
        //    97: aload_1        
        //    98: astore          5
        //   100: aload_1        
        //   101: iconst_1       
        //   102: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   107: astore          7
        //   109: aload_1        
        //   110: astore          4
        //   112: aload_1        
        //   113: astore          5
        //   115: aload_1        
        //   116: iconst_2       
        //   117: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   122: astore          8
        //   124: aload_1        
        //   125: astore          4
        //   127: aload_1        
        //   128: astore          5
        //   130: aload_1        
        //   131: iconst_3       
        //   132: invokeinterface android/database/Cursor.getLong:(I)J
        //   137: lstore          9
        //   139: aload_1        
        //   140: astore          4
        //   142: aload_1        
        //   143: astore          5
        //   145: aload_1        
        //   146: iconst_4       
        //   147: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   152: astore          11
        //   154: aload_1        
        //   155: astore          4
        //   157: aload_1        
        //   158: astore          5
        //   160: aload_1        
        //   161: iconst_5       
        //   162: invokeinterface android/database/Cursor.getInt:(I)I
        //   167: istore          12
        //   169: aload_1        
        //   170: astore          4
        //   172: aload_1        
        //   173: astore          5
        //   175: aload_1        
        //   176: bipush          6
        //   178: invokeinterface android/database/Cursor.getInt:(I)I
        //   183: istore          13
        //   185: aload_2        
        //   186: ifnull          208
        //   189: aload_1        
        //   190: astore          4
        //   192: aload_1        
        //   193: astore          5
        //   195: aload_2        
        //   196: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   199: ldc_w           "screenshot"
        //   202: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   205: ifne            287
        //   208: aload           7
        //   210: ifnull          233
        //   213: aload_1        
        //   214: astore          4
        //   216: aload_1        
        //   217: astore          5
        //   219: aload           7
        //   221: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   224: ldc_w           "screenshot"
        //   227: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   230: ifne            287
        //   233: aload           8
        //   235: ifnull          258
        //   238: aload_1        
        //   239: astore          4
        //   241: aload_1        
        //   242: astore          5
        //   244: aload           8
        //   246: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   249: ldc_w           "screenshot"
        //   252: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   255: ifne            287
        //   258: aload           11
        //   260: ifnull          65
        //   263: aload_1        
        //   264: astore          4
        //   266: aload_1        
        //   267: astore          5
        //   269: aload           11
        //   271: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //   274: ldc_w           "screenshot"
        //   277: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   280: istore          14
        //   282: iload           14
        //   284: ifeq            65
        //   287: iload           12
        //   289: ifeq            301
        //   292: iload           13
        //   294: istore          15
        //   296: iload           13
        //   298: ifne            356
        //   301: aload_1        
        //   302: astore          4
        //   304: new             Landroid/graphics/BitmapFactory$Options;
        //   307: astore          5
        //   309: aload_1        
        //   310: astore          4
        //   312: aload           5
        //   314: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
        //   317: aload_1        
        //   318: astore          4
        //   320: aload           5
        //   322: iconst_1       
        //   323: putfield        android/graphics/BitmapFactory$Options.inJustDecodeBounds:Z
        //   326: aload_1        
        //   327: astore          4
        //   329: aload_2        
        //   330: aload           5
        //   332: invokestatic    android/graphics/BitmapFactory.decodeFile:(Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
        //   335: pop            
        //   336: aload_1        
        //   337: astore          4
        //   339: aload           5
        //   341: getfield        android/graphics/BitmapFactory$Options.outWidth:I
        //   344: istore          12
        //   346: aload_1        
        //   347: astore          4
        //   349: aload           5
        //   351: getfield        android/graphics/BitmapFactory$Options.outHeight:I
        //   354: istore          15
        //   356: iload           12
        //   358: ifle            418
        //   361: iload           15
        //   363: ifle            418
        //   366: aload_1        
        //   367: astore          4
        //   369: iload           12
        //   371: aload           6
        //   373: getfield        android/graphics/Point.x:I
        //   376: if_icmpne       392
        //   379: aload_1        
        //   380: astore          4
        //   382: iload           15
        //   384: aload           6
        //   386: getfield        android/graphics/Point.y:I
        //   389: if_icmpeq       418
        //   392: aload_1        
        //   393: astore          4
        //   395: iload           15
        //   397: aload           6
        //   399: getfield        android/graphics/Point.x:I
        //   402: if_icmpne       65
        //   405: aload_1        
        //   406: astore          4
        //   408: iload           12
        //   410: aload           6
        //   412: getfield        android/graphics/Point.y:I
        //   415: if_icmpne       65
        //   418: aload_1        
        //   419: astore          4
        //   421: aload_3        
        //   422: lload           9
        //   424: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   427: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   430: pop            
        //   431: goto            65
        //   434: astore          5
        //   436: aload_1        
        //   437: astore          4
        //   439: aload_1        
        //   440: astore          5
        //   442: aload_3        
        //   443: lload           9
        //   445: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   448: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   451: pop            
        //   452: goto            65
        //   455: aload_1        
        //   456: astore          4
        //   458: aload_1        
        //   459: astore          5
        //   461: aload_1        
        //   462: invokeinterface android/database/Cursor.close:()V
        //   467: aload_1        
        //   468: astore          4
        //   470: aload_1        
        //   471: astore          5
        //   473: aload_3        
        //   474: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //   477: ifne            519
        //   480: aload_1        
        //   481: astore          4
        //   483: aload_1        
        //   484: astore          5
        //   486: new             Lorg/telegram/messenger/_$$Lambda$MediaController$M715dCmB5sndyTLyXH8F6AQFBc4;
        //   489: astore_2       
        //   490: aload_1        
        //   491: astore          4
        //   493: aload_1        
        //   494: astore          5
        //   496: aload_2        
        //   497: aload_0        
        //   498: aload_3        
        //   499: invokespecial   org/telegram/messenger/_$$Lambda$MediaController$M715dCmB5sndyTLyXH8F6AQFBc4.<init>:(Lorg/telegram/messenger/MediaController;Ljava/util/ArrayList;)V
        //   502: aload_1        
        //   503: astore          4
        //   505: aload_1        
        //   506: astore          5
        //   508: aload_2        
        //   509: invokestatic    org/telegram/messenger/AndroidUtilities.runOnUIThread:(Ljava/lang/Runnable;)V
        //   512: goto            519
        //   515: astore_1       
        //   516: goto            553
        //   519: aload_1        
        //   520: ifnull          552
        //   523: aload_1        
        //   524: invokeinterface android/database/Cursor.close:()V
        //   529: goto            552
        //   532: astore_1       
        //   533: aload           5
        //   535: astore          4
        //   537: aload_1        
        //   538: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   541: aload           5
        //   543: ifnull          552
        //   546: aload           5
        //   548: astore_1       
        //   549: goto            523
        //   552: return         
        //   553: aload           4
        //   555: ifnull          565
        //   558: aload           4
        //   560: invokeinterface android/database/Cursor.close:()V
        //   565: aload_1        
        //   566: athrow         
        //   567: astore_1       
        //   568: goto            552
        //   571: astore          5
        //   573: goto            565
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     15     532    552    Ljava/lang/Exception;
        //  10     15     515    567    Any
        //  21     41     532    552    Ljava/lang/Exception;
        //  21     41     515    567    Any
        //  47     51     532    552    Ljava/lang/Exception;
        //  47     51     515    567    Any
        //  57     61     532    552    Ljava/lang/Exception;
        //  57     61     515    567    Any
        //  71     80     532    552    Ljava/lang/Exception;
        //  71     80     515    567    Any
        //  86     94     532    552    Ljava/lang/Exception;
        //  86     94     515    567    Any
        //  100    109    532    552    Ljava/lang/Exception;
        //  100    109    515    567    Any
        //  115    124    532    552    Ljava/lang/Exception;
        //  115    124    515    567    Any
        //  130    139    532    552    Ljava/lang/Exception;
        //  130    139    515    567    Any
        //  145    154    532    552    Ljava/lang/Exception;
        //  145    154    515    567    Any
        //  160    169    532    552    Ljava/lang/Exception;
        //  160    169    515    567    Any
        //  175    185    532    552    Ljava/lang/Exception;
        //  175    185    515    567    Any
        //  195    208    532    552    Ljava/lang/Exception;
        //  195    208    515    567    Any
        //  219    233    532    552    Ljava/lang/Exception;
        //  219    233    515    567    Any
        //  244    258    532    552    Ljava/lang/Exception;
        //  244    258    515    567    Any
        //  269    282    532    552    Ljava/lang/Exception;
        //  269    282    515    567    Any
        //  304    309    434    455    Ljava/lang/Exception;
        //  304    309    515    567    Any
        //  312    317    434    455    Ljava/lang/Exception;
        //  312    317    515    567    Any
        //  320    326    434    455    Ljava/lang/Exception;
        //  320    326    515    567    Any
        //  329    336    434    455    Ljava/lang/Exception;
        //  329    336    515    567    Any
        //  339    346    434    455    Ljava/lang/Exception;
        //  339    346    515    567    Any
        //  349    356    434    455    Ljava/lang/Exception;
        //  349    356    515    567    Any
        //  369    379    434    455    Ljava/lang/Exception;
        //  369    379    515    567    Any
        //  382    392    434    455    Ljava/lang/Exception;
        //  382    392    515    567    Any
        //  395    405    434    455    Ljava/lang/Exception;
        //  395    405    515    567    Any
        //  408    418    434    455    Ljava/lang/Exception;
        //  408    418    515    567    Any
        //  421    431    434    455    Ljava/lang/Exception;
        //  421    431    515    567    Any
        //  442    452    532    552    Ljava/lang/Exception;
        //  442    452    515    567    Any
        //  461    467    532    552    Ljava/lang/Exception;
        //  461    467    515    567    Any
        //  473    480    532    552    Ljava/lang/Exception;
        //  473    480    515    567    Any
        //  486    490    532    552    Ljava/lang/Exception;
        //  486    490    515    567    Any
        //  496    502    532    552    Ljava/lang/Exception;
        //  496    502    515    567    Any
        //  508    512    532    552    Ljava/lang/Exception;
        //  508    512    515    567    Any
        //  523    529    567    571    Ljava/lang/Exception;
        //  537    541    515    567    Any
        //  558    565    571    576    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 291 out-of-bounds for length 291
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private long readAndWriteTracks(final MessageObject messageObject, final MediaExtractor mediaExtractor, final MP4Builder mp4Builder, final MediaCodec$BufferInfo mediaCodec$BufferInfo, final long n, final long n2, final File file, final boolean b) throws Exception {
        final int track = this.findTrack(mediaExtractor, false);
        int track2;
        if (b) {
            track2 = this.findTrack(mediaExtractor, true);
        }
        else {
            track2 = -1;
        }
        int addTrack;
        int n3;
        if (track >= 0) {
            mediaExtractor.selectTrack(track);
            final MediaFormat trackFormat = mediaExtractor.getTrackFormat(track);
            addTrack = mp4Builder.addTrack(trackFormat, false);
            n3 = trackFormat.getInteger("max-input-size");
            if (n > 0L) {
                mediaExtractor.seekTo(n, 0);
            }
            else {
                mediaExtractor.seekTo(0L, 0);
            }
        }
        else {
            n3 = 0;
            addTrack = -1;
        }
        int addTrack2;
        if (track2 >= 0) {
            mediaExtractor.selectTrack(track2);
            final MediaFormat trackFormat2 = mediaExtractor.getTrackFormat(track2);
            addTrack2 = mp4Builder.addTrack(trackFormat2, true);
            n3 = Math.max(trackFormat2.getInteger("max-input-size"), n3);
            if (n > 0L) {
                mediaExtractor.seekTo(n, 0);
            }
            else {
                mediaExtractor.seekTo(0L, 0);
            }
        }
        else {
            addTrack2 = -1;
        }
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(n3);
        if (track2 < 0 && track < 0) {
            return -1L;
        }
        this.checkConversionCanceled();
        long n4 = -1L;
        for (int i = 0; i == 0; i = 1) {
            this.checkConversionCanceled();
            mediaCodec$BufferInfo.size = mediaExtractor.readSampleData(allocateDirect, 0);
            final int sampleTrackIndex = mediaExtractor.getSampleTrackIndex();
            int n5;
            if (sampleTrackIndex == track) {
                n5 = addTrack;
            }
            else if (sampleTrackIndex == track2) {
                n5 = addTrack2;
            }
            else {
                n5 = -1;
            }
            int n16;
            if (n5 != -1) {
                if (Build$VERSION.SDK_INT < 21) {
                    allocateDirect.position(0);
                    allocateDirect.limit(mediaCodec$BufferInfo.size);
                }
                int n11 = 0;
                Label_0534: {
                    if (sampleTrackIndex != track2) {
                        final byte[] array = allocateDirect.array();
                        if (array != null) {
                            final int arrayOffset = allocateDirect.arrayOffset();
                            final int limit = allocateDirect.limit();
                            int n6 = -1;
                            int n7 = arrayOffset;
                            while (true) {
                                int n8 = n7;
                                final int n9 = 4;
                                final int n10 = arrayOffset + limit - 4;
                                n11 = track2;
                                if (n8 > n10) {
                                    break Label_0534;
                                }
                                int n12 = 0;
                                Label_0516: {
                                    if (array[n8] != 0 || array[n8 + 1] != 0 || array[n8 + 2] != 0 || array[n8 + 3] != 1) {
                                        n12 = n6;
                                        if (n8 != n10) {
                                            break Label_0516;
                                        }
                                    }
                                    if (n6 != -1) {
                                        int n13;
                                        if (n8 != n10) {
                                            n13 = n9;
                                        }
                                        else {
                                            n13 = 0;
                                        }
                                        final int n14 = n8 - n6 - n13;
                                        array[n6] = (byte)(n14 >> 24);
                                        array[n6 + 1] = (byte)(n14 >> 16);
                                        array[n6 + 2] = (byte)(n14 >> 8);
                                        array[n6 + 3] = (byte)n14;
                                    }
                                    n12 = n8;
                                }
                                ++n8;
                                n6 = n12;
                                n7 = n8;
                            }
                        }
                    }
                    n11 = track2;
                }
                int n15;
                if (mediaCodec$BufferInfo.size >= 0) {
                    mediaCodec$BufferInfo.presentationTimeUs = mediaExtractor.getSampleTime();
                    n15 = 0;
                }
                else {
                    mediaCodec$BufferInfo.size = 0;
                    n15 = 1;
                }
                if (mediaCodec$BufferInfo.size > 0 && n15 == 0) {
                    long presentationTimeUs;
                    if (sampleTrackIndex == track) {
                        presentationTimeUs = n4;
                        if (n > 0L) {
                            presentationTimeUs = n4;
                            if (n4 == -1L) {
                                presentationTimeUs = mediaCodec$BufferInfo.presentationTimeUs;
                            }
                        }
                    }
                    else {
                        presentationTimeUs = n4;
                    }
                    n4 = presentationTimeUs;
                    if (n2 >= 0L && mediaCodec$BufferInfo.presentationTimeUs >= n2) {
                        n15 = 1;
                    }
                    else {
                        mediaCodec$BufferInfo.offset = 0;
                        mediaCodec$BufferInfo.flags = mediaExtractor.getSampleFlags();
                        final long writeSampleData = mp4Builder.writeSampleData(n5, allocateDirect, mediaCodec$BufferInfo, false);
                        if (writeSampleData != 0L) {
                            this.didWriteData(messageObject, file, false, writeSampleData, false);
                        }
                    }
                }
                if (n15 == 0) {
                    mediaExtractor.advance();
                }
                n16 = n15;
                track2 = n11;
            }
            else if (sampleTrackIndex == -1) {
                n16 = 1;
            }
            else {
                mediaExtractor.advance();
                n16 = 0;
            }
            if (n16 != 0) {}
        }
        if (track >= 0) {
            mediaExtractor.unselectTrack(track);
        }
        if (track2 >= 0) {
            mediaExtractor.unselectTrack(track2);
        }
        return n4;
    }
    
    public static void saveFile(String pathname, final Context context, final int n, final String s, final String s2) {
        if (pathname == null) {
            return;
        }
        final String s3 = null;
        final String s4 = null;
        File file = null;
        Label_0060: {
            if (pathname != null && pathname.length() != 0) {
                file = new File(pathname);
                if (file.exists()) {
                    if (!AndroidUtilities.isInternalUri(Uri.fromFile(file))) {
                        break Label_0060;
                    }
                }
            }
            file = null;
        }
        if (file == null) {
            return;
        }
        final boolean[] array = { false };
        if (file.exists()) {
            pathname = s3;
            if (context != null) {
                pathname = s3;
                if (n != 0) {
                    try {
                        pathname = (String)new AlertDialog(context, 2);
                        try {
                            ((AlertDialog)pathname).setMessage(LocaleController.getString("Loading", 2131559768));
                            ((AlertDialog)pathname).setCanceledOnTouchOutside(false);
                            ((Dialog)pathname).setCancelable(true);
                            ((AlertDialog)pathname).setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$MediaController$hrz_cghaZ1kTzzeIoiWSaviEy_E(array));
                            ((Dialog)pathname).show();
                        }
                        catch (Exception ex) {}
                    }
                    catch (Exception ex) {
                        pathname = s4;
                    }
                    final Exception ex;
                    FileLog.e(ex);
                }
            }
            new Thread(new _$$Lambda$MediaController$nx3Q4nKr4qmGQfXNSzGCKTPGTMo(n, s, file, array, (AlertDialog)pathname, s2)).start();
        }
    }
    
    @SuppressLint({ "NewApi" })
    public static MediaCodecInfo selectCodec(final String anotherString) {
        final int codecCount = MediaCodecList.getCodecCount();
        MediaCodecInfo mediaCodecInfo = null;
        for (int i = 0; i < codecCount; ++i) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (codecInfo.isEncoder()) {
                final String[] supportedTypes = codecInfo.getSupportedTypes();
                for (int length = supportedTypes.length, j = 0; j < length; ++j) {
                    if (supportedTypes[j].equalsIgnoreCase(anotherString)) {
                        final String name = codecInfo.getName();
                        if (name != null) {
                            if (!name.equals("OMX.SEC.avc.enc")) {
                                return codecInfo;
                            }
                            if (name.equals("OMX.SEC.AVC.Encoder")) {
                                return codecInfo;
                            }
                        }
                        mediaCodecInfo = codecInfo;
                    }
                }
            }
        }
        return mediaCodecInfo;
    }
    
    @SuppressLint({ "NewApi" })
    public static int selectColorFormat(final MediaCodecInfo mediaCodecInfo, final String s) {
        final MediaCodecInfo$CodecCapabilities capabilitiesForType = mediaCodecInfo.getCapabilitiesForType(s);
        int n = 0;
        int n2 = 0;
        while (true) {
            final int[] colorFormats = capabilitiesForType.colorFormats;
            if (n >= colorFormats.length) {
                return n2;
            }
            final int n3 = colorFormats[n];
            if (isRecognizedFormat(n3)) {
                if (!mediaCodecInfo.getName().equals("OMX.SEC.AVC.Encoder") || n3 != 19) {
                    return n3;
                }
                n2 = n3;
            }
            ++n;
        }
    }
    
    private void setPlayerVolume() {
        try {
            float n;
            if (this.audioFocus != 1) {
                n = 1.0f;
            }
            else {
                n = 0.2f;
            }
            if (this.audioPlayer != null) {
                this.audioPlayer.setVolume(n);
            }
            else if (this.videoPlayer != null) {
                this.videoPlayer.setVolume(n);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void setUseFrontSpeaker(final boolean useFrontSpeaker) {
        this.useFrontSpeaker = useFrontSpeaker;
        final AudioManager audioManager = NotificationsController.audioManager;
        if (this.useFrontSpeaker) {
            audioManager.setBluetoothScoOn(false);
            audioManager.setSpeakerphoneOn(false);
        }
        else {
            audioManager.setSpeakerphoneOn(true);
        }
    }
    
    private void startAudioAgain(final boolean b) {
        final MessageObject playingMessageObject = this.playingMessageObject;
        if (playingMessageObject == null) {
            return;
        }
        final NotificationCenter instance = NotificationCenter.getInstance(playingMessageObject.currentAccount);
        final int audioRouteChanged = NotificationCenter.audioRouteChanged;
        final boolean useFrontSpeaker = this.useFrontSpeaker;
        int streamType = 0;
        instance.postNotificationName(audioRouteChanged, useFrontSpeaker);
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            if (!this.useFrontSpeaker) {
                streamType = 3;
            }
            videoPlayer.setStreamType(streamType);
            if (!b) {
                this.videoPlayer.play();
            }
            else {
                this.pauseMessage(this.playingMessageObject);
            }
        }
        else {
            final boolean b2 = this.audioPlayer != null;
            final MessageObject playingMessageObject2 = this.playingMessageObject;
            final float audioProgress = playingMessageObject2.audioProgress;
            this.cleanupPlayer(false, true);
            playingMessageObject2.audioProgress = audioProgress;
            this.playMessage(playingMessageObject2);
            if (b) {
                if (b2) {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$lSLgqwMUP5qD77OV_XW0fHoQAiY(this, playingMessageObject2), 100L);
                }
                else {
                    this.pauseMessage(playingMessageObject2);
                }
            }
        }
    }
    
    private void startProgressTimer(final MessageObject messageObject) {
        synchronized (this.progressTimerSync) {
            if (this.progressTimer != null) {
                try {
                    this.progressTimer.cancel();
                    this.progressTimer = null;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            messageObject.getFileName();
            (this.progressTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (MediaController.this.sync) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$4$r0MGx90x6zCLeWW_3FGDHLnfRxc(this, messageObject));
                    }
                }
            }, 0L, 17L);
        }
    }
    
    private native int startRecord(final String p0);
    
    private boolean startVideoConvertFromQueue() {
        final boolean empty = this.videoConvertQueue.isEmpty();
        int i = 0;
        if (!empty) {
            Object videoConvertSync = this.videoConvertSync;
            synchronized (videoConvertSync) {
                this.cancelCurrentVideoConversion = false;
                // monitorexit(videoConvertSync)
                final MessageObject messageObject = this.videoConvertQueue.get(0);
                videoConvertSync = new Intent(ApplicationLoader.applicationContext, (Class)VideoEncodingService.class);
                ((Intent)videoConvertSync).putExtra("path", messageObject.messageOwner.attachPath);
                ((Intent)videoConvertSync).putExtra("currentAccount", messageObject.currentAccount);
                if (messageObject.messageOwner.media.document != null) {
                    while (i < messageObject.messageOwner.media.document.attributes.size()) {
                        if (messageObject.messageOwner.media.document.attributes.get(i) instanceof TLRPC.TL_documentAttributeAnimated) {
                            ((Intent)videoConvertSync).putExtra("gif", true);
                            break;
                        }
                        ++i;
                    }
                }
                if (messageObject.getId() != 0) {
                    try {
                        ApplicationLoader.applicationContext.startService((Intent)videoConvertSync);
                    }
                    catch (Throwable videoConvertSync) {
                        FileLog.e((Throwable)videoConvertSync);
                    }
                }
                VideoConvertRunnable.runConversion(messageObject);
                return true;
            }
        }
        return false;
    }
    
    private void stopProgressTimer() {
        synchronized (this.progressTimerSync) {
            if (this.progressTimer != null) {
                try {
                    this.progressTimer.cancel();
                    this.progressTimer = null;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
    }
    
    private native void stopRecord();
    
    private void stopRecordingInternal(final int n) {
        if (n != 0) {
            this.fileEncodingQueue.postRunnable(new _$$Lambda$MediaController$xOPvRRaZp12WyiiCi3uolis74HI(this, this.recordingAudio, this.recordingAudioFile, n));
        }
        try {
            if (this.audioRecorder != null) {
                this.audioRecorder.release();
                this.audioRecorder = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        this.recordingAudio = null;
        this.recordingAudioFile = null;
    }
    
    private void updateVideoState(MessageObject playingMessageObject, final int[] array, final boolean b, final boolean b2, final int n) {
        if (this.videoPlayer == null) {
            return;
        }
        if (n != 4 && n != 1) {
            try {
                this.baseActivity.getWindow().addFlags(128);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        else {
            try {
                this.baseActivity.getWindow().clearFlags(128);
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
        }
        if (n == 3) {
            this.playerWasReady = true;
            final MessageObject playingMessageObject2 = this.playingMessageObject;
            if (playingMessageObject2 != null && (playingMessageObject2.isVideo() || this.playingMessageObject.isRoundVideo())) {
                AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                FileLoader.getInstance(playingMessageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
            }
            this.currentAspectRatioFrameLayoutReady = true;
        }
        else if (n == 2) {
            if (b2) {
                playingMessageObject = this.playingMessageObject;
                if (playingMessageObject != null && (playingMessageObject.isVideo() || this.playingMessageObject.isRoundVideo())) {
                    if (this.playerWasReady) {
                        this.setLoadingRunnable.run();
                    }
                    else {
                        AndroidUtilities.runOnUIThread(this.setLoadingRunnable, 1000L);
                    }
                }
            }
        }
        else if (this.videoPlayer.isPlaying() && n == 4) {
            if (this.playingMessageObject.isVideo() && !b && (array == null || array[0] < 4)) {
                this.videoPlayer.seekTo(0L);
                if (array != null) {
                    ++array[0];
                }
            }
            else {
                this.cleanupPlayer(true, true, true, false);
            }
        }
    }
    
    private native int writeFrame(final ByteBuffer p0, final int p1);
    
    public void cancelVideoConvert(final MessageObject messageObject) {
        if (messageObject == null) {
            synchronized (this.videoConvertSync) {
                this.cancelCurrentVideoConversion = true;
                return;
            }
        }
        if (!this.videoConvertQueue.isEmpty()) {
            for (int i = 0; i < this.videoConvertQueue.size(); ++i) {
                final MessageObject messageObject2 = this.videoConvertQueue.get(i);
                if (messageObject2.getId() == messageObject.getId() && messageObject2.currentAccount == messageObject.currentAccount) {
                    if (i == 0) {
                        synchronized (this.videoConvertSync) {
                            this.cancelCurrentVideoConversion = true;
                            break;
                        }
                    }
                    this.videoConvertQueue.remove(i);
                    break;
                }
            }
        }
    }
    
    protected void checkIsNextMediaFileDownloaded() {
        final MessageObject playingMessageObject = this.playingMessageObject;
        if (playingMessageObject != null) {
            if (playingMessageObject.isMusic()) {
                this.checkIsNextMusicFileDownloaded(this.playingMessageObject.currentAccount);
            }
        }
    }
    
    public void cleanup() {
        int i = 0;
        this.cleanupPlayer(false, true);
        this.audioInfo = null;
        this.playMusicAgain = false;
        while (i < 3) {
            DownloadController.getInstance(i).cleanup();
            ++i;
        }
        this.videoConvertQueue.clear();
        this.playlist.clear();
        this.shuffledPlaylist.clear();
        this.generatingWaveform.clear();
        this.voiceMessagesPlaylist = null;
        this.voiceMessagesPlaylistMap = null;
        this.cancelVideoConvert(null);
    }
    
    public void cleanupPlayer(final boolean b, final boolean b2) {
        this.cleanupPlayer(b, b2, false, false);
    }
    
    public void cleanupPlayer(final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        final VideoPlayer audioPlayer = this.audioPlayer;
        if (audioPlayer != null) {
            try {
                audioPlayer.releasePlayer(true);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            this.audioPlayer = null;
        }
        else {
            final VideoPlayer videoPlayer = this.videoPlayer;
            if (videoPlayer != null) {
                this.currentAspectRatioFrameLayout = null;
                this.currentTextureViewContainer = null;
                this.currentAspectRatioFrameLayoutReady = false;
                this.isDrawingWasReady = false;
                this.currentTextureView = null;
                this.goingToShowMessageObject = null;
                if (b4) {
                    PhotoViewer.getInstance().injectVideoPlayer(this.videoPlayer);
                    final MessageObject playingMessageObject = this.playingMessageObject;
                    this.goingToShowMessageObject = playingMessageObject;
                    NotificationCenter.getInstance(playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingGoingToStop, this.playingMessageObject, true);
                }
                else {
                    final long currentPosition = videoPlayer.getCurrentPosition();
                    final MessageObject playingMessageObject2 = this.playingMessageObject;
                    if (playingMessageObject2 != null && playingMessageObject2.isVideo() && currentPosition > 0L && currentPosition != -9223372036854775807L) {
                        final MessageObject playingMessageObject3 = this.playingMessageObject;
                        playingMessageObject3.audioProgressMs = (int)currentPosition;
                        NotificationCenter.getInstance(playingMessageObject3.currentAccount).postNotificationName(NotificationCenter.messagePlayingGoingToStop, this.playingMessageObject, false);
                    }
                    this.videoPlayer.releasePlayer(true);
                    this.videoPlayer = null;
                }
                try {
                    this.baseActivity.getWindow().clearFlags(128);
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
                if (this.playingMessageObject != null && !b4) {
                    AndroidUtilities.cancelRunOnUIThread(this.setLoadingRunnable);
                    FileLoader.getInstance(this.playingMessageObject.currentAccount).removeLoadingVideo(this.playingMessageObject.getDocument(), true, false);
                }
            }
        }
        this.stopProgressTimer();
        this.lastProgress = 0L;
        this.isPaused = false;
        if (!this.useFrontSpeaker && !SharedConfig.raiseToSpeak) {
            final ChatActivity raiseChat = this.raiseChat;
            this.stopRaiseToEarSensors(raiseChat, false);
            this.raiseChat = raiseChat;
        }
        final MessageObject playingMessageObject4 = this.playingMessageObject;
        if (playingMessageObject4 != null) {
            if (this.downloadingCurrentMessage) {
                FileLoader.getInstance(playingMessageObject4.currentAccount).cancelLoadFile(this.playingMessageObject.getDocument());
            }
            final MessageObject playingMessageObject5 = this.playingMessageObject;
            if (b) {
                playingMessageObject5.resetPlayingProgress();
                NotificationCenter.getInstance(playingMessageObject5.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, this.playingMessageObject.getId(), 0);
            }
            this.playingMessageObject = null;
            this.downloadingCurrentMessage = false;
            if (b) {
                NotificationsController.audioManager.abandonAudioFocus((AudioManager$OnAudioFocusChangeListener)this);
                this.hasAudioFocus = 0;
                final int n = -1;
                final ArrayList<MessageObject> voiceMessagesPlaylist = this.voiceMessagesPlaylist;
                int index = n;
                Label_0572: {
                    if (voiceMessagesPlaylist != null) {
                        index = n;
                        if (b3) {
                            final int index2 = voiceMessagesPlaylist.indexOf(playingMessageObject5);
                            if ((index = index2) >= 0) {
                                this.voiceMessagesPlaylist.remove(index2);
                                this.voiceMessagesPlaylistMap.remove(playingMessageObject5.getId());
                                index = index2;
                                if (this.voiceMessagesPlaylist.isEmpty()) {
                                    this.voiceMessagesPlaylist = null;
                                    this.voiceMessagesPlaylistMap = null;
                                    index = index2;
                                }
                                break Label_0572;
                            }
                        }
                        this.voiceMessagesPlaylist = null;
                        this.voiceMessagesPlaylistMap = null;
                    }
                }
                final ArrayList<MessageObject> voiceMessagesPlaylist2 = this.voiceMessagesPlaylist;
                if (voiceMessagesPlaylist2 != null && index < voiceMessagesPlaylist2.size()) {
                    final MessageObject messageObject = this.voiceMessagesPlaylist.get(index);
                    this.playMessage(messageObject);
                    if (!messageObject.isRoundVideo()) {
                        final PipRoundVideoView pipRoundVideoView = this.pipRoundVideoView;
                        if (pipRoundVideoView != null) {
                            pipRoundVideoView.close(true);
                            this.pipRoundVideoView = null;
                        }
                    }
                }
                else {
                    if ((playingMessageObject5.isVoice() || playingMessageObject5.isRoundVideo()) && playingMessageObject5.getId() != 0) {
                        this.startRecordingIfFromSpeaker();
                    }
                    NotificationCenter.getInstance(playingMessageObject5.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidReset, playingMessageObject5.getId(), b2);
                    this.pipSwitchingState = 0;
                    final PipRoundVideoView pipRoundVideoView2 = this.pipRoundVideoView;
                    if (pipRoundVideoView2 != null) {
                        pipRoundVideoView2.close(true);
                        this.pipRoundVideoView = null;
                    }
                }
            }
            if (b2) {
                ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class)MusicPlayerService.class));
            }
        }
    }
    
    public void didReceivedNotification(int i, final int n, final Object... array) {
        final int fileDidLoad = NotificationCenter.fileDidLoad;
        final int n2 = 0;
        final int n3 = 0;
        if (i != fileDidLoad && i != NotificationCenter.httpFileDidLoad) {
            if (i == NotificationCenter.messagesDeleted) {
                i = (int)array[1];
                final ArrayList list = (ArrayList)array[0];
                final MessageObject playingMessageObject = this.playingMessageObject;
                if (playingMessageObject != null && i == playingMessageObject.messageOwner.to_id.channel_id && list.contains(playingMessageObject.getId())) {
                    this.cleanupPlayer(true, true);
                }
                final ArrayList<MessageObject> voiceMessagesPlaylist = this.voiceMessagesPlaylist;
                if (voiceMessagesPlaylist != null && !voiceMessagesPlaylist.isEmpty() && i == this.voiceMessagesPlaylist.get(0).messageOwner.to_id.channel_id) {
                    Integer n4;
                    MessageObject o;
                    for (i = n3; i < list.size(); ++i) {
                        n4 = list.get(i);
                        o = (MessageObject)this.voiceMessagesPlaylistMap.get((int)n4);
                        this.voiceMessagesPlaylistMap.remove((int)n4);
                        if (o != null) {
                            this.voiceMessagesPlaylist.remove(o);
                        }
                    }
                }
            }
            else if (i == NotificationCenter.removeAllMessagesFromDialog) {
                final long longValue = (long)array[0];
                final MessageObject playingMessageObject2 = this.playingMessageObject;
                if (playingMessageObject2 != null && playingMessageObject2.getDialogId() == longValue) {
                    this.cleanupPlayer(false, true);
                }
            }
            else if (i == NotificationCenter.musicDidLoad) {
                final long longValue2 = (long)array[0];
                final MessageObject playingMessageObject3 = this.playingMessageObject;
                if (playingMessageObject3 != null && playingMessageObject3.isMusic() && this.playingMessageObject.getDialogId() == longValue2) {
                    final ArrayList c = (ArrayList)array[1];
                    this.playlist.addAll(0, c);
                    if (SharedConfig.shuffleMusic) {
                        this.buildShuffledPlayList();
                        this.currentPlaylistNum = 0;
                    }
                    else {
                        this.currentPlaylistNum += c.size();
                    }
                }
            }
            else if (i == NotificationCenter.didReceiveNewMessages) {
                final ArrayList<MessageObject> voiceMessagesPlaylist2 = this.voiceMessagesPlaylist;
                if (voiceMessagesPlaylist2 != null && !voiceMessagesPlaylist2.isEmpty() && (long)array[0] == this.voiceMessagesPlaylist.get(0).getDialogId()) {
                    ArrayList list2;
                    MessageObject e;
                    for (list2 = (ArrayList)array[1], i = n2; i < list2.size(); ++i) {
                        e = list2.get(i);
                        if ((e.isVoice() || e.isRoundVideo()) && (!this.voiceMessagesPlaylistUnread || (e.isContentUnread() && !e.isOut()))) {
                            this.voiceMessagesPlaylist.add(e);
                            this.voiceMessagesPlaylistMap.put(e.getId(), (Object)e);
                        }
                    }
                }
            }
            else if (i == NotificationCenter.playerDidStartPlaying && !getInstance().isCurrentPlayer((VideoPlayer)array[0])) {
                getInstance().pauseMessage(getInstance().getPlayingMessageObject());
            }
        }
        else {
            final String anObject = (String)array[0];
            if (this.downloadingCurrentMessage) {
                final MessageObject playingMessageObject4 = this.playingMessageObject;
                if (playingMessageObject4 != null && playingMessageObject4.currentAccount == n && FileLoader.getAttachFileName(playingMessageObject4.getDocument()).equals(anObject)) {
                    this.playMusicAgain = true;
                    this.playMessage(this.playingMessageObject);
                }
            }
        }
    }
    
    public boolean findMessageInPlaylistAndPlay(final MessageObject o) {
        final int index = this.playlist.indexOf(o);
        if (index == -1) {
            return this.playMessage(o);
        }
        this.playMessageAtIndex(index);
        return true;
    }
    
    public void generateWaveform(final MessageObject value) {
        final StringBuilder sb = new StringBuilder();
        sb.append(value.getId());
        sb.append("_");
        sb.append(value.getDialogId());
        final String string = sb.toString();
        final String absolutePath = FileLoader.getPathToMessage(value.messageOwner).getAbsolutePath();
        if (this.generatingWaveform.containsKey(string)) {
            return;
        }
        this.generatingWaveform.put(string, value);
        Utilities.globalQueue.postRunnable(new _$$Lambda$MediaController$8PZv0oHSpRt_Io2cMkMt61RbJcc(this, absolutePath, string));
    }
    
    public AudioInfo getAudioInfo() {
        return this.audioInfo;
    }
    
    public float getPlaybackSpeed() {
        return this.currentPlaybackSpeed;
    }
    
    public MessageObject getPlayingMessageObject() {
        return this.playingMessageObject;
    }
    
    public int getPlayingMessageObjectNum() {
        return this.currentPlaylistNum;
    }
    
    public ArrayList<MessageObject> getPlaylist() {
        return this.playlist;
    }
    
    public native byte[] getWaveform(final String p0);
    
    public native byte[] getWaveform2(final short[] p0, final int p1);
    
    public boolean hasFlagSecureFragment() {
        return this.flagSecureFragment != null;
    }
    
    public void injectVideoPlayer(final VideoPlayer videoPlayer, final MessageObject messageObject) {
        if (videoPlayer != null) {
            if (messageObject != null) {
                FileLoader.getInstance(messageObject.currentAccount).setLoadingVideoForPlayer(messageObject.getDocument(), true);
                this.playerWasReady = false;
                this.playlist.clear();
                this.shuffledPlaylist.clear();
                this.videoPlayer = videoPlayer;
                this.playingMessageObject = messageObject;
                this.videoPlayer.setDelegate((VideoPlayer.VideoPlayerDelegate)new VideoPlayer.VideoPlayerDelegate() {
                    @Override
                    public void onError(final Exception ex) {
                        FileLog.e(ex);
                    }
                    
                    @Override
                    public void onRenderedFirstFrame() {
                        if (MediaController.this.currentAspectRatioFrameLayout != null && !MediaController.this.currentAspectRatioFrameLayout.isDrawingReady()) {
                            MediaController.this.isDrawingWasReady = true;
                            MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                            MediaController.this.currentTextureViewContainer.setTag((Object)1);
                        }
                    }
                    
                    @Override
                    public void onStateChanged(final boolean b, final int n) {
                        MediaController.this.updateVideoState(messageObject, null, true, b, n);
                    }
                    
                    @Override
                    public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
                        if (MediaController.this.videoPlayer == null) {
                            return false;
                        }
                        if (MediaController.this.pipSwitchingState == 2) {
                            if (MediaController.this.currentAspectRatioFrameLayout != null) {
                                if (MediaController.this.isDrawingWasReady) {
                                    MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                                }
                                if (MediaController.this.currentAspectRatioFrameLayout.getParent() == null) {
                                    MediaController.this.currentTextureViewContainer.addView((View)MediaController.this.currentAspectRatioFrameLayout);
                                }
                                if (MediaController.this.currentTextureView.getSurfaceTexture() != surfaceTexture) {
                                    MediaController.this.currentTextureView.setSurfaceTexture(surfaceTexture);
                                }
                                MediaController.this.videoPlayer.setTextureView(MediaController.this.currentTextureView);
                            }
                            MediaController.this.pipSwitchingState = 0;
                            return true;
                        }
                        if (MediaController.this.pipSwitchingState == 1) {
                            if (MediaController.this.baseActivity != null) {
                                if (MediaController.this.pipRoundVideoView == null) {
                                    try {
                                        MediaController.this.pipRoundVideoView = new PipRoundVideoView();
                                        MediaController.this.pipRoundVideoView.show(MediaController.this.baseActivity, new _$$Lambda$MediaController$5$ROZf_OsRqepDnAAg1NpMCnDNXO8(this));
                                    }
                                    catch (Exception ex) {
                                        MediaController.this.pipRoundVideoView = null;
                                    }
                                }
                                if (MediaController.this.pipRoundVideoView != null) {
                                    if (MediaController.this.pipRoundVideoView.getTextureView().getSurfaceTexture() != surfaceTexture) {
                                        MediaController.this.pipRoundVideoView.getTextureView().setSurfaceTexture(surfaceTexture);
                                    }
                                    MediaController.this.videoPlayer.setTextureView(MediaController.this.pipRoundVideoView.getTextureView());
                                }
                            }
                            MediaController.this.pipSwitchingState = 0;
                            return true;
                        }
                        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isInjectingVideoPlayer()) {
                            PhotoViewer.getInstance().injectVideoPlayerSurface(surfaceTexture);
                            return true;
                        }
                        return false;
                    }
                    
                    @Override
                    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
                    }
                    
                    @Override
                    public void onVideoSizeChanged(final int n, final int n2, final int n3, float n4) {
                        MediaController.this.currentAspectRatioFrameLayoutRotation = n3;
                        int n5 = n;
                        int n6 = n2;
                        if (n3 != 90) {
                            if (n3 == 270) {
                                n5 = n;
                                n6 = n2;
                            }
                            else {
                                n6 = n;
                                n5 = n2;
                            }
                        }
                        final MediaController this$0 = MediaController.this;
                        if (n5 == 0) {
                            n4 = 1.0f;
                        }
                        else {
                            n4 = n6 * n4 / n5;
                        }
                        this$0.currentAspectRatioFrameLayoutRatio = n4;
                        if (MediaController.this.currentAspectRatioFrameLayout != null) {
                            MediaController.this.currentAspectRatioFrameLayout.setAspectRatio(MediaController.this.currentAspectRatioFrameLayoutRatio, MediaController.this.currentAspectRatioFrameLayoutRotation);
                        }
                    }
                });
                this.currentAspectRatioFrameLayoutReady = false;
                final TextureView currentTextureView = this.currentTextureView;
                if (currentTextureView != null) {
                    this.videoPlayer.setTextureView(currentTextureView);
                }
                this.checkAudioFocus(messageObject);
                this.setPlayerVolume();
                this.isPaused = false;
                this.lastProgress = 0L;
                this.playingMessageObject = messageObject;
                if (!SharedConfig.raiseToSpeak) {
                    this.startRaiseToEarSensors(this.raiseChat);
                }
                this.startProgressTimer(this.playingMessageObject);
                NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidStart, messageObject);
            }
        }
    }
    
    public boolean isCurrentPlayer(final VideoPlayer videoPlayer) {
        return this.videoPlayer == videoPlayer || this.audioPlayer == videoPlayer;
    }
    
    public boolean isDownloadingCurrentMessage() {
        return this.downloadingCurrentMessage;
    }
    
    public boolean isGoingToShowMessageObject(final MessageObject messageObject) {
        return this.goingToShowMessageObject == messageObject;
    }
    
    public boolean isMessagePaused() {
        return this.isPaused || this.downloadingCurrentMessage;
    }
    
    public boolean isPlayingMessage(final MessageObject messageObject) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null) {
            final MessageObject playingMessageObject = this.playingMessageObject;
            if (playingMessageObject != null) {
                final long eventId = playingMessageObject.eventId;
                if (eventId != 0L && eventId == messageObject.eventId) {
                    return this.downloadingCurrentMessage ^ true;
                }
                if (this.isSamePlayingMessage(messageObject)) {
                    return this.downloadingCurrentMessage ^ true;
                }
            }
        }
        return false;
    }
    
    public boolean isPlayingMessageAndReadyToDraw(final MessageObject messageObject) {
        return this.isDrawingWasReady && this.isPlayingMessage(messageObject);
    }
    
    protected boolean isRecordingAudio() {
        return this.recordStartRunnable != null || this.recordingAudio != null;
    }
    
    public boolean isRecordingOrListeningByProximity() {
        if (this.proximityTouched) {
            if (!this.isRecordingAudio()) {
                final MessageObject playingMessageObject = this.playingMessageObject;
                if (playingMessageObject == null || (!playingMessageObject.isVoice() && !this.playingMessageObject.isRoundVideo())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean isVideoDrawingReady() {
        final AspectRatioFrameLayout currentAspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        return currentAspectRatioFrameLayout != null && currentAspectRatioFrameLayout.isDrawingReady();
    }
    
    public void onAccuracyChanged(final Sensor sensor, final int n) {
    }
    
    public void onAudioFocusChange(final int n) {
        if (n == -1) {
            if (this.isPlayingMessage(this.getPlayingMessageObject()) && !this.isMessagePaused()) {
                this.pauseMessage(this.playingMessageObject);
            }
            this.hasAudioFocus = 0;
            this.audioFocus = 0;
        }
        else if (n == 1) {
            this.audioFocus = 2;
            if (this.resumeAudioOnFocusGain) {
                this.resumeAudioOnFocusGain = false;
                if (this.isPlayingMessage(this.getPlayingMessageObject()) && this.isMessagePaused()) {
                    this.playMessage(this.getPlayingMessageObject());
                }
            }
        }
        else if (n == -3) {
            this.audioFocus = 1;
        }
        else if (n == -2) {
            this.audioFocus = 0;
            if (this.isPlayingMessage(this.getPlayingMessageObject()) && !this.isMessagePaused()) {
                this.pauseMessage(this.playingMessageObject);
                this.resumeAudioOnFocusGain = true;
            }
        }
        this.setPlayerVolume();
    }
    
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (this.sensorsStarted) {
            if (VoIPService.getSharedInstance() == null) {
                final Sensor sensor = sensorEvent.sensor;
                if (sensor == this.proximitySensor) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("proximity changed to ");
                        sb.append(sensorEvent.values[0]);
                        sb.append(" max value = ");
                        sb.append(this.proximitySensor.getMaximumRange());
                        FileLog.d(sb.toString());
                    }
                    final float lastProximityValue = this.lastProximityValue;
                    if (lastProximityValue == -100.0f) {
                        this.lastProximityValue = sensorEvent.values[0];
                    }
                    else if (lastProximityValue != sensorEvent.values[0]) {
                        this.proximityHasDifferentValues = true;
                    }
                    if (this.proximityHasDifferentValues) {
                        this.proximityTouched = this.isNearToSensor(sensorEvent.values[0]);
                    }
                }
                else if (sensor == this.accelerometerSensor) {
                    final long lastTimestamp = this.lastTimestamp;
                    double n;
                    if (lastTimestamp == 0L) {
                        n = 0.9800000190734863;
                    }
                    else {
                        final double v = (double)(sensorEvent.timestamp - lastTimestamp);
                        Double.isNaN(v);
                        n = 1.0 / (v / 1.0E9 + 1.0);
                    }
                    this.lastTimestamp = sensorEvent.timestamp;
                    final float[] gravity = this.gravity;
                    final double v2 = gravity[0];
                    Double.isNaN(v2);
                    final double n2 = 1.0 - n;
                    final float[] values = sensorEvent.values;
                    final double v3 = values[0];
                    Double.isNaN(v3);
                    gravity[0] = (float)(v2 * n + v3 * n2);
                    final double v4 = gravity[1];
                    Double.isNaN(v4);
                    final double v5 = values[1];
                    Double.isNaN(v5);
                    gravity[1] = (float)(v4 * n + v5 * n2);
                    final double v6 = gravity[2];
                    Double.isNaN(v6);
                    final double v7 = values[2];
                    Double.isNaN(v7);
                    gravity[2] = (float)(n * v6 + n2 * v7);
                    final float[] gravityFast = this.gravityFast;
                    gravityFast[0] = gravity[0] * 0.8f + values[0] * 0.19999999f;
                    gravityFast[1] = gravity[1] * 0.8f + values[1] * 0.19999999f;
                    gravityFast[2] = gravity[2] * 0.8f + values[2] * 0.19999999f;
                    final float[] linearAcceleration = this.linearAcceleration;
                    linearAcceleration[0] = values[0] - gravity[0];
                    linearAcceleration[1] = values[1] - gravity[1];
                    linearAcceleration[2] = values[2] - gravity[2];
                }
                else if (sensor == this.linearSensor) {
                    final float[] linearAcceleration2 = this.linearAcceleration;
                    final float[] values2 = sensorEvent.values;
                    linearAcceleration2[0] = values2[0];
                    linearAcceleration2[1] = values2[1];
                    linearAcceleration2[2] = values2[2];
                }
                else if (sensor == this.gravitySensor) {
                    final float[] gravityFast2 = this.gravityFast;
                    final float[] gravity2 = this.gravity;
                    final float[] values3 = sensorEvent.values;
                    gravityFast2[0] = (gravity2[0] = values3[0]);
                    gravityFast2[1] = (gravity2[1] = values3[1]);
                    gravityFast2[2] = (gravity2[2] = values3[2]);
                }
                final Sensor sensor2 = sensorEvent.sensor;
                if (sensor2 == this.linearSensor || sensor2 == this.gravitySensor || sensor2 == this.accelerometerSensor) {
                    final float[] gravity3 = this.gravity;
                    final float n3 = gravity3[0];
                    final float[] linearAcceleration3 = this.linearAcceleration;
                    final float previousAccValue = n3 * linearAcceleration3[0] + gravity3[1] * linearAcceleration3[1] + gravity3[2] * linearAcceleration3[2];
                    Label_1064: {
                        if (this.raisedToBack != 6 && ((previousAccValue > 0.0f && this.previousAccValue > 0.0f) || (previousAccValue < 0.0f && this.previousAccValue < 0.0f))) {
                            int n4;
                            int raisedToTopSign;
                            if (previousAccValue > 0.0f) {
                                n4 = ((previousAccValue > 15.0f) ? 1 : 0);
                                raisedToTopSign = 1;
                            }
                            else {
                                final boolean b = previousAccValue < -15.0f;
                                final int n5 = 2;
                                n4 = (b ? 1 : 0);
                                raisedToTopSign = n5;
                            }
                            final int raisedToTopSign2 = this.raisedToTopSign;
                            if (raisedToTopSign2 != 0 && raisedToTopSign2 != raisedToTopSign) {
                                if (this.raisedToTop == 6 && n4 != 0) {
                                    final int raisedToBack = this.raisedToBack;
                                    if (raisedToBack < 6) {
                                        this.raisedToBack = raisedToBack + 1;
                                        if (this.raisedToBack == 6) {
                                            this.raisedToTop = 0;
                                            this.raisedToTopSign = 0;
                                            this.countLess = 0;
                                            this.timeSinceRaise = System.currentTimeMillis();
                                            if (BuildVars.LOGS_ENABLED && BuildVars.DEBUG_PRIVATE_VERSION) {
                                                FileLog.d("motion detected");
                                            }
                                        }
                                    }
                                }
                                else {
                                    if (n4 == 0) {
                                        ++this.countLess;
                                    }
                                    if (this.countLess == 10 || this.raisedToTop != 6 || this.raisedToBack != 0) {
                                        this.raisedToTop = 0;
                                        this.raisedToTopSign = 0;
                                        this.raisedToBack = 0;
                                        this.countLess = 0;
                                    }
                                }
                            }
                            else {
                                if (n4 != 0 && this.raisedToBack == 0) {
                                    final int raisedToTopSign3 = this.raisedToTopSign;
                                    if (raisedToTopSign3 == 0 || raisedToTopSign3 == raisedToTopSign) {
                                        final int raisedToTop = this.raisedToTop;
                                        if (raisedToTop >= 6 || this.proximityTouched) {
                                            break Label_1064;
                                        }
                                        this.raisedToTopSign = raisedToTopSign;
                                        this.raisedToTop = raisedToTop + 1;
                                        if (this.raisedToTop == 6) {
                                            this.countLess = 0;
                                        }
                                        break Label_1064;
                                    }
                                }
                                if (n4 == 0) {
                                    ++this.countLess;
                                }
                                if (this.raisedToTopSign != raisedToTopSign || this.countLess == 10 || this.raisedToTop != 6 || this.raisedToBack != 0) {
                                    this.raisedToBack = 0;
                                    this.raisedToTop = 0;
                                    this.raisedToTopSign = 0;
                                    this.countLess = 0;
                                }
                            }
                        }
                    }
                    this.previousAccValue = previousAccValue;
                    final float[] gravityFast3 = this.gravityFast;
                    this.accelerometerVertical = (gravityFast3[1] > 2.5f && Math.abs(gravityFast3[2]) < 4.0f && Math.abs(this.gravityFast[0]) > 1.5f);
                }
                if (this.raisedToBack == 6 && this.accelerometerVertical && this.proximityTouched && !NotificationsController.audioManager.isWiredHeadsetOn()) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("sensor values reached");
                    }
                    if (this.playingMessageObject == null && this.recordStartRunnable == null && this.recordingAudio == null && !PhotoViewer.getInstance().isVisible() && ApplicationLoader.isScreenOn && !this.inputFieldHasText && this.allowStartRecord && this.raiseChat != null && !this.callInProgress) {
                        if (!this.raiseToEarRecord) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start record");
                            }
                            this.useFrontSpeaker = true;
                            if (!this.raiseChat.playFirstUnreadVoiceMessage()) {
                                this.raiseToEarRecord = true;
                                this.useFrontSpeaker = false;
                                this.startRecording(this.raiseChat.getCurrentAccount(), this.raiseChat.getDialogId(), null);
                            }
                            if (this.useFrontSpeaker) {
                                this.setUseFrontSpeaker(true);
                            }
                            this.ignoreOnPause = true;
                            if (this.proximityHasDifferentValues) {
                                final PowerManager$WakeLock proximityWakeLock = this.proximityWakeLock;
                                if (proximityWakeLock != null && !proximityWakeLock.isHeld()) {
                                    this.proximityWakeLock.acquire();
                                }
                            }
                        }
                    }
                    else {
                        final MessageObject playingMessageObject = this.playingMessageObject;
                        if (playingMessageObject != null && (playingMessageObject.isVoice() || this.playingMessageObject.isRoundVideo()) && !this.useFrontSpeaker) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start listen");
                            }
                            if (this.proximityHasDifferentValues) {
                                final PowerManager$WakeLock proximityWakeLock2 = this.proximityWakeLock;
                                if (proximityWakeLock2 != null && !proximityWakeLock2.isHeld()) {
                                    this.proximityWakeLock.acquire();
                                }
                            }
                            this.setUseFrontSpeaker(true);
                            this.startAudioAgain(false);
                            this.ignoreOnPause = true;
                        }
                    }
                    this.raisedToBack = 0;
                    this.raisedToTop = 0;
                    this.raisedToTopSign = 0;
                    this.countLess = 0;
                }
                else {
                    final boolean proximityTouched = this.proximityTouched;
                    if (proximityTouched) {
                        if (this.playingMessageObject != null && !ApplicationLoader.mainInterfacePaused && (this.playingMessageObject.isVoice() || this.playingMessageObject.isRoundVideo()) && !this.useFrontSpeaker) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("start listen by proximity only");
                            }
                            if (this.proximityHasDifferentValues) {
                                final PowerManager$WakeLock proximityWakeLock3 = this.proximityWakeLock;
                                if (proximityWakeLock3 != null && !proximityWakeLock3.isHeld()) {
                                    this.proximityWakeLock.acquire();
                                }
                            }
                            this.setUseFrontSpeaker(true);
                            this.startAudioAgain(false);
                            this.ignoreOnPause = true;
                        }
                    }
                    else if (!proximityTouched) {
                        if (this.raiseToEarRecord) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("stop record");
                            }
                            this.stopRecording(2);
                            this.raiseToEarRecord = false;
                            this.ignoreOnPause = false;
                            if (this.proximityHasDifferentValues) {
                                final PowerManager$WakeLock proximityWakeLock4 = this.proximityWakeLock;
                                if (proximityWakeLock4 != null && proximityWakeLock4.isHeld()) {
                                    this.proximityWakeLock.release();
                                }
                            }
                        }
                        else if (this.useFrontSpeaker) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("stop listen");
                            }
                            this.useFrontSpeaker = false;
                            this.startAudioAgain(true);
                            this.ignoreOnPause = false;
                            if (this.proximityHasDifferentValues) {
                                final PowerManager$WakeLock proximityWakeLock5 = this.proximityWakeLock;
                                if (proximityWakeLock5 != null && proximityWakeLock5.isHeld()) {
                                    this.proximityWakeLock.release();
                                }
                            }
                        }
                    }
                }
                if (this.timeSinceRaise != 0L && this.raisedToBack == 6 && Math.abs(System.currentTimeMillis() - this.timeSinceRaise) > 1000L) {
                    this.raisedToBack = 0;
                    this.raisedToTop = 0;
                    this.raisedToTopSign = 0;
                    this.countLess = 0;
                    this.timeSinceRaise = 0L;
                }
            }
        }
    }
    
    public boolean pauseMessage(final MessageObject messageObject) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && this.playingMessageObject != null) {
            if (this.isSamePlayingMessage(messageObject)) {
                this.stopProgressTimer();
                try {
                    if (this.audioPlayer != null) {
                        this.audioPlayer.pause();
                    }
                    else if (this.videoPlayer != null) {
                        this.videoPlayer.pause();
                    }
                    this.isPaused = true;
                    NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, this.playingMessageObject.getId());
                    return true;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    this.isPaused = false;
                }
            }
        }
        return false;
    }
    
    public boolean playMessage(MessageObject playingMessageObject) {
        final Integer value = 0;
        if (playingMessageObject == null) {
            return false;
        }
        if ((this.audioPlayer != null || this.videoPlayer != null) && this.isSamePlayingMessage(playingMessageObject)) {
            if (this.isPaused) {
                this.resumeAudio(playingMessageObject);
            }
            if (!SharedConfig.raiseToSpeak) {
                this.startRaiseToEarSensors(this.raiseChat);
            }
            return true;
        }
        if (!playingMessageObject.isOut() && playingMessageObject.isContentUnread()) {
            MessagesController.getInstance(playingMessageObject.currentAccount).markMessageContentAsRead(playingMessageObject);
        }
        final boolean playMusicAgain = this.playMusicAgain;
        boolean b = playMusicAgain ^ true;
        final MessageObject playingMessageObject2 = this.playingMessageObject;
        if (playingMessageObject2 != null) {
            if (!playMusicAgain) {
                playingMessageObject2.resetPlayingProgress();
            }
            b = false;
        }
        this.cleanupPlayer(b, false);
        this.playMusicAgain = false;
        this.seekToProgressPending = 0.0f;
        final String attachPath = playingMessageObject.messageOwner.attachPath;
        File file;
        int exists;
        if (attachPath != null && attachPath.length() > 0) {
            file = new File(playingMessageObject.messageOwner.attachPath);
            final boolean b2 = (exists = (file.exists() ? 1 : 0)) != 0;
            if (!b2) {
                file = null;
                exists = (b2 ? 1 : 0);
            }
        }
        else {
            file = null;
            exists = 0;
        }
        File pathToMessage;
        if (file != null) {
            pathToMessage = file;
        }
        else {
            pathToMessage = FileLoader.getPathToMessage(playingMessageObject.messageOwner);
        }
        final boolean b3 = SharedConfig.streamMedia && (playingMessageObject.isMusic() || playingMessageObject.isRoundVideo() || (playingMessageObject.isVideo() && playingMessageObject.canStreamVideo())) && (int)playingMessageObject.getDialogId() != 0;
        int n = exists;
        if (pathToMessage != null) {
            n = exists;
            if (pathToMessage != file) {
                final boolean exists2 = pathToMessage.exists();
                if ((n = (exists2 ? 1 : 0)) == 0) {
                    n = (exists2 ? 1 : 0);
                    if (!b3) {
                        FileLoader.getInstance(playingMessageObject.currentAccount).loadFile(playingMessageObject.getDocument(), playingMessageObject, 0, 0);
                        this.downloadingCurrentMessage = true;
                        this.isPaused = false;
                        this.lastProgress = 0L;
                        this.audioInfo = null;
                        this.playingMessageObject = playingMessageObject;
                        if (this.playingMessageObject.isMusic()) {
                            final Intent intent = new Intent(ApplicationLoader.applicationContext, (Class)MusicPlayerService.class);
                            try {
                                ApplicationLoader.applicationContext.startService(intent);
                            }
                            catch (Throwable t) {
                                FileLog.e(t);
                            }
                        }
                        else {
                            ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class)MusicPlayerService.class));
                        }
                        NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, this.playingMessageObject.getId());
                        return true;
                    }
                }
            }
        }
        this.downloadingCurrentMessage = false;
        if (playingMessageObject.isMusic()) {
            this.checkIsNextMusicFileDownloaded(playingMessageObject.currentAccount);
        }
        else {
            this.checkIsNextVoiceFileDownloaded(playingMessageObject.currentAccount);
        }
        final AspectRatioFrameLayout currentAspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        if (currentAspectRatioFrameLayout != null) {
            currentAspectRatioFrameLayout.setDrawingReady(this.isDrawingWasReady = false);
        }
        final boolean video = playingMessageObject.isVideo();
        Label_1869: {
            if (!playingMessageObject.isRoundVideo()) {
                if (!video) {
                    final PipRoundVideoView pipRoundVideoView = this.pipRoundVideoView;
                    if (pipRoundVideoView != null) {
                        pipRoundVideoView.close(true);
                        this.pipRoundVideoView = null;
                    }
                    try {
                        (this.audioPlayer = new VideoPlayer()).setDelegate((VideoPlayer.VideoPlayerDelegate)new VideoPlayer.VideoPlayerDelegate() {
                            @Override
                            public void onError(final Exception ex) {
                            }
                            
                            @Override
                            public void onRenderedFirstFrame() {
                            }
                            
                            @Override
                            public void onStateChanged(final boolean b, int n) {
                                if (n != 4 && ((n != 1 && n != 2) || !b || playingMessageObject.audioProgress < 0.999f)) {
                                    if (MediaController.this.seekToProgressPending != 0.0f && (n == 3 || n == 1)) {
                                        n = (int)(MediaController.this.audioPlayer.getDuration() * MediaController.this.seekToProgressPending);
                                        final VideoPlayer access$2200 = MediaController.this.audioPlayer;
                                        final long n2 = n;
                                        access$2200.seekTo(n2);
                                        MediaController.this.lastProgress = n2;
                                        MediaController.this.seekToProgressPending = 0.0f;
                                    }
                                }
                                else if (!MediaController.this.playlist.isEmpty() && MediaController.this.playlist.size() > 1) {
                                    MediaController.this.playNextMessageWithoutOrder(true);
                                }
                                else {
                                    final MediaController this$0 = MediaController.this;
                                    final MessageObject val$messageObject = playingMessageObject;
                                    this$0.cleanupPlayer(true, true, val$messageObject != null && val$messageObject.isVoice(), false);
                                }
                            }
                            
                            @Override
                            public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
                                return false;
                            }
                            
                            @Override
                            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
                            }
                            
                            @Override
                            public void onVideoSizeChanged(final int n, final int n2, final int n3, final float n4) {
                            }
                        });
                        if (n != 0) {
                            if (!playingMessageObject.mediaExists && pathToMessage != file) {
                                AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$auT6qepqgY7CkLB80AAQ5iO7pfI(playingMessageObject));
                            }
                            this.audioPlayer.preparePlayer(Uri.fromFile(pathToMessage), "other");
                        }
                        else {
                            final int fileReference = FileLoader.getInstance(playingMessageObject.currentAccount).getFileReference(playingMessageObject);
                            final TLRPC.Document document = playingMessageObject.getDocument();
                            final StringBuilder sb = new StringBuilder();
                            sb.append("?account=");
                            sb.append(playingMessageObject.currentAccount);
                            sb.append("&id=");
                            sb.append(document.id);
                            sb.append("&hash=");
                            sb.append(document.access_hash);
                            sb.append("&dc=");
                            sb.append(document.dc_id);
                            sb.append("&size=");
                            sb.append(document.size);
                            sb.append("&mime=");
                            sb.append(URLEncoder.encode(document.mime_type, "UTF-8"));
                            sb.append("&rid=");
                            sb.append(fileReference);
                            sb.append("&name=");
                            sb.append(URLEncoder.encode(FileLoader.getDocumentFileName(document), "UTF-8"));
                            sb.append("&reference=");
                            byte[] file_reference;
                            if (document.file_reference != null) {
                                file_reference = document.file_reference;
                            }
                            else {
                                file_reference = new byte[0];
                            }
                            sb.append(Utilities.bytesToHex(file_reference));
                            final String string = sb.toString();
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("tg://");
                            sb2.append(playingMessageObject.getFileName());
                            sb2.append(string);
                            this.audioPlayer.preparePlayer(Uri.parse(sb2.toString()), "other");
                        }
                        if (playingMessageObject.isVoice()) {
                            if (this.currentPlaybackSpeed > 1.0f) {
                                this.audioPlayer.setPlaybackSpeed(this.currentPlaybackSpeed);
                            }
                            this.audioInfo = null;
                            this.playlist.clear();
                            this.shuffledPlaylist.clear();
                        }
                        else {
                            try {
                                this.audioInfo = AudioInfo.getAudioInfo(pathToMessage);
                            }
                            catch (Exception ex) {
                                FileLog.e(ex);
                            }
                        }
                        final VideoPlayer audioPlayer = this.audioPlayer;
                        int streamType;
                        if (this.useFrontSpeaker) {
                            streamType = 0;
                        }
                        else {
                            streamType = 3;
                        }
                        audioPlayer.setStreamType(streamType);
                        this.audioPlayer.play();
                        break Label_1869;
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                        final NotificationCenter instance = NotificationCenter.getInstance(playingMessageObject.currentAccount);
                        final int messagePlayingPlayStateChanged = NotificationCenter.messagePlayingPlayStateChanged;
                        final MessageObject playingMessageObject3 = this.playingMessageObject;
                        int id;
                        if (playingMessageObject3 != null) {
                            id = playingMessageObject3.getId();
                        }
                        else {
                            id = 0;
                        }
                        instance.postNotificationName(messagePlayingPlayStateChanged, id);
                        final VideoPlayer audioPlayer2 = this.audioPlayer;
                        if (audioPlayer2 != null) {
                            audioPlayer2.releasePlayer(true);
                            this.audioPlayer = null;
                            this.isPaused = false;
                            this.playingMessageObject = null;
                            this.downloadingCurrentMessage = false;
                        }
                        return false;
                    }
                }
            }
            FileLoader.getInstance(playingMessageObject.currentAccount).setLoadingVideoForPlayer(playingMessageObject.getDocument(), true);
            this.playerWasReady = false;
            final boolean b4 = !video || (playingMessageObject.messageOwner.to_id.channel_id == 0 && playingMessageObject.audioProgress <= 0.1f);
            int[] array;
            if (video && playingMessageObject.getDuration() <= 30) {
                array = new int[] { 1 };
            }
            else {
                array = null;
            }
            this.playlist.clear();
            this.shuffledPlaylist.clear();
            (this.videoPlayer = new VideoPlayer()).setDelegate((VideoPlayer.VideoPlayerDelegate)new VideoPlayer.VideoPlayerDelegate() {
                @Override
                public void onError(final Exception ex) {
                    FileLog.e(ex);
                }
                
                @Override
                public void onRenderedFirstFrame() {
                    if (MediaController.this.currentAspectRatioFrameLayout != null && !MediaController.this.currentAspectRatioFrameLayout.isDrawingReady()) {
                        MediaController.this.isDrawingWasReady = true;
                        MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                        MediaController.this.currentTextureViewContainer.setTag((Object)1);
                    }
                }
                
                @Override
                public void onStateChanged(final boolean b, final int n) {
                    MediaController.this.updateVideoState(playingMessageObject, array, b4, b, n);
                }
                
                @Override
                public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
                    if (MediaController.this.videoPlayer == null) {
                        return false;
                    }
                    if (MediaController.this.pipSwitchingState == 2) {
                        if (MediaController.this.currentAspectRatioFrameLayout != null) {
                            if (MediaController.this.isDrawingWasReady) {
                                MediaController.this.currentAspectRatioFrameLayout.setDrawingReady(true);
                            }
                            if (MediaController.this.currentAspectRatioFrameLayout.getParent() == null) {
                                MediaController.this.currentTextureViewContainer.addView((View)MediaController.this.currentAspectRatioFrameLayout);
                            }
                            if (MediaController.this.currentTextureView.getSurfaceTexture() != surfaceTexture) {
                                MediaController.this.currentTextureView.setSurfaceTexture(surfaceTexture);
                            }
                            MediaController.this.videoPlayer.setTextureView(MediaController.this.currentTextureView);
                        }
                        MediaController.this.pipSwitchingState = 0;
                        return true;
                    }
                    if (MediaController.this.pipSwitchingState == 1) {
                        if (MediaController.this.baseActivity != null) {
                            if (MediaController.this.pipRoundVideoView == null) {
                                try {
                                    MediaController.this.pipRoundVideoView = new PipRoundVideoView();
                                    MediaController.this.pipRoundVideoView.show(MediaController.this.baseActivity, new _$$Lambda$MediaController$6$8PAmwbvWSqlIkfrUwfiuydCYWZ0(this));
                                }
                                catch (Exception ex) {
                                    MediaController.this.pipRoundVideoView = null;
                                }
                            }
                            if (MediaController.this.pipRoundVideoView != null) {
                                if (MediaController.this.pipRoundVideoView.getTextureView().getSurfaceTexture() != surfaceTexture) {
                                    MediaController.this.pipRoundVideoView.getTextureView().setSurfaceTexture(surfaceTexture);
                                }
                                MediaController.this.videoPlayer.setTextureView(MediaController.this.pipRoundVideoView.getTextureView());
                            }
                        }
                        MediaController.this.pipSwitchingState = 0;
                        return true;
                    }
                    if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isInjectingVideoPlayer()) {
                        PhotoViewer.getInstance().injectVideoPlayerSurface(surfaceTexture);
                        return true;
                    }
                    return false;
                }
                
                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
                }
                
                @Override
                public void onVideoSizeChanged(final int n, final int n2, final int n3, float n4) {
                    MediaController.this.currentAspectRatioFrameLayoutRotation = n3;
                    int n5 = n;
                    int n6 = n2;
                    if (n3 != 90) {
                        if (n3 == 270) {
                            n5 = n;
                            n6 = n2;
                        }
                        else {
                            n6 = n;
                            n5 = n2;
                        }
                    }
                    final MediaController this$0 = MediaController.this;
                    if (n5 == 0) {
                        n4 = 1.0f;
                    }
                    else {
                        n4 = n6 * n4 / n5;
                    }
                    this$0.currentAspectRatioFrameLayoutRatio = n4;
                    if (MediaController.this.currentAspectRatioFrameLayout != null) {
                        MediaController.this.currentAspectRatioFrameLayout.setAspectRatio(MediaController.this.currentAspectRatioFrameLayoutRatio, MediaController.this.currentAspectRatioFrameLayoutRotation);
                    }
                }
            });
            this.currentAspectRatioFrameLayoutReady = false;
            if (this.pipRoundVideoView == null && MessagesController.getInstance(playingMessageObject.currentAccount).isDialogVisible(playingMessageObject.getDialogId())) {
                final TextureView currentTextureView = this.currentTextureView;
                if (currentTextureView != null) {
                    this.videoPlayer.setTextureView(currentTextureView);
                }
            }
            else {
                if (this.pipRoundVideoView == null) {
                    try {
                        (this.pipRoundVideoView = new PipRoundVideoView()).show(this.baseActivity, new _$$Lambda$MediaController$u8rACRf9hl_QJDf7Qe2JZbJv__Q(this));
                    }
                    catch (Exception ex6) {
                        this.pipRoundVideoView = null;
                    }
                }
                final PipRoundVideoView pipRoundVideoView2 = this.pipRoundVideoView;
                if (pipRoundVideoView2 != null) {
                    this.videoPlayer.setTextureView(pipRoundVideoView2.getTextureView());
                }
            }
            if (n != 0) {
                if (!playingMessageObject.mediaExists && pathToMessage != file) {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$UNhwNBqeTy1Z6WZHWcGGmYV_bRw(playingMessageObject));
                }
                this.videoPlayer.preparePlayer(Uri.fromFile(pathToMessage), "other");
            }
            else {
                try {
                    final int fileReference2 = FileLoader.getInstance(playingMessageObject.currentAccount).getFileReference(playingMessageObject);
                    final TLRPC.Document document2 = playingMessageObject.getDocument();
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("?account=");
                    sb3.append(playingMessageObject.currentAccount);
                    sb3.append("&id=");
                    sb3.append(document2.id);
                    sb3.append("&hash=");
                    sb3.append(document2.access_hash);
                    sb3.append("&dc=");
                    sb3.append(document2.dc_id);
                    sb3.append("&size=");
                    sb3.append(document2.size);
                    sb3.append("&mime=");
                    sb3.append(URLEncoder.encode(document2.mime_type, "UTF-8"));
                    sb3.append("&rid=");
                    sb3.append(fileReference2);
                    sb3.append("&name=");
                    sb3.append(URLEncoder.encode(FileLoader.getDocumentFileName(document2), "UTF-8"));
                    sb3.append("&reference=");
                    byte[] file_reference2;
                    if (document2.file_reference != null) {
                        file_reference2 = document2.file_reference;
                    }
                    else {
                        file_reference2 = new byte[0];
                    }
                    sb3.append(Utilities.bytesToHex(file_reference2));
                    final String string2 = sb3.toString();
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("tg://");
                    sb4.append(playingMessageObject.getFileName());
                    sb4.append(string2);
                    this.videoPlayer.preparePlayer(Uri.parse(sb4.toString()), "other");
                }
                catch (Exception ex3) {
                    FileLog.e(ex3);
                }
            }
            if (playingMessageObject.isRoundVideo()) {
                final VideoPlayer videoPlayer = this.videoPlayer;
                int streamType2;
                if (this.useFrontSpeaker) {
                    streamType2 = 0;
                }
                else {
                    streamType2 = 3;
                }
                videoPlayer.setStreamType(streamType2);
                final float currentPlaybackSpeed = this.currentPlaybackSpeed;
                if (currentPlaybackSpeed > 1.0f) {
                    this.videoPlayer.setPlaybackSpeed(currentPlaybackSpeed);
                }
            }
            else {
                this.videoPlayer.setStreamType(3);
            }
        }
        this.checkAudioFocus(playingMessageObject);
        this.setPlayerVolume();
        this.isPaused = false;
        this.lastProgress = 0L;
        this.playingMessageObject = playingMessageObject;
        if (!SharedConfig.raiseToSpeak) {
            this.startRaiseToEarSensors(this.raiseChat);
        }
        this.startProgressTimer(this.playingMessageObject);
        NotificationCenter.getInstance(playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidStart, playingMessageObject);
        final VideoPlayer videoPlayer2 = this.videoPlayer;
        if (videoPlayer2 != null) {
            try {
                if (this.playingMessageObject.audioProgress != 0.0f) {
                    long duration;
                    if ((duration = videoPlayer2.getDuration()) == -9223372036854775807L) {
                        duration = this.playingMessageObject.getDuration() * 1000L;
                    }
                    int audioProgressMs = (int)(duration * this.playingMessageObject.audioProgress);
                    if (this.playingMessageObject.audioProgressMs != 0) {
                        audioProgressMs = this.playingMessageObject.audioProgressMs;
                        this.playingMessageObject.audioProgressMs = 0;
                    }
                    this.videoPlayer.seekTo(audioProgressMs);
                }
            }
            catch (Exception ex4) {
                final MessageObject playingMessageObject4 = this.playingMessageObject;
                playingMessageObject4.audioProgress = 0.0f;
                playingMessageObject4.audioProgressSec = 0;
                NotificationCenter.getInstance(playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, this.playingMessageObject.getId(), value);
                FileLog.e(ex4);
            }
            this.videoPlayer.play();
        }
        else {
            final VideoPlayer audioPlayer3 = this.audioPlayer;
            if (audioPlayer3 != null) {
                try {
                    if (this.playingMessageObject.audioProgress != 0.0f) {
                        long duration2;
                        if ((duration2 = audioPlayer3.getDuration()) == -9223372036854775807L) {
                            duration2 = this.playingMessageObject.getDuration() * 1000L;
                        }
                        this.audioPlayer.seekTo((int)(duration2 * this.playingMessageObject.audioProgress));
                    }
                }
                catch (Exception ex5) {
                    this.playingMessageObject.resetPlayingProgress();
                    NotificationCenter.getInstance(playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingProgressDidChanged, this.playingMessageObject.getId(), value);
                    FileLog.e(ex5);
                }
            }
        }
        playingMessageObject = this.playingMessageObject;
        if (playingMessageObject != null && playingMessageObject.isMusic()) {
            final Intent intent2 = new Intent(ApplicationLoader.applicationContext, (Class)MusicPlayerService.class);
            try {
                ApplicationLoader.applicationContext.startService(intent2);
            }
            catch (Throwable t2) {
                FileLog.e(t2);
            }
        }
        else {
            ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class)MusicPlayerService.class));
        }
        return true;
    }
    
    public void playMessageAtIndex(final int currentPlaylistNum) {
        final int currentPlaylistNum2 = this.currentPlaylistNum;
        if (currentPlaylistNum2 >= 0) {
            if (currentPlaylistNum2 < this.playlist.size()) {
                this.currentPlaylistNum = currentPlaylistNum;
                this.playMusicAgain = true;
                final MessageObject playingMessageObject = this.playingMessageObject;
                if (playingMessageObject != null) {
                    playingMessageObject.resetPlayingProgress();
                }
                this.playMessage(this.playlist.get(this.currentPlaylistNum));
            }
        }
    }
    
    public void playNextMessage() {
        this.playNextMessageWithoutOrder(false);
    }
    
    public void playPreviousMessage() {
        ArrayList<MessageObject> list;
        if (SharedConfig.shuffleMusic) {
            list = this.shuffledPlaylist;
        }
        else {
            list = this.playlist;
        }
        if (!list.isEmpty()) {
            final int currentPlaylistNum = this.currentPlaylistNum;
            if (currentPlaylistNum >= 0) {
                if (currentPlaylistNum < list.size()) {
                    final MessageObject messageObject = list.get(this.currentPlaylistNum);
                    if (messageObject.audioProgressSec > 10) {
                        this.seekToProgress(messageObject, 0.0f);
                        return;
                    }
                    if (SharedConfig.playOrderReversed) {
                        --this.currentPlaylistNum;
                        if (this.currentPlaylistNum < 0) {
                            this.currentPlaylistNum = list.size() - 1;
                        }
                    }
                    else {
                        ++this.currentPlaylistNum;
                        if (this.currentPlaylistNum >= list.size()) {
                            this.currentPlaylistNum = 0;
                        }
                    }
                    final int currentPlaylistNum2 = this.currentPlaylistNum;
                    if (currentPlaylistNum2 >= 0) {
                        if (currentPlaylistNum2 < list.size()) {
                            this.playMusicAgain = true;
                            this.playMessage(list.get(this.currentPlaylistNum));
                        }
                    }
                }
            }
        }
    }
    
    public void resetGoingToShowMessageObject() {
        this.goingToShowMessageObject = null;
    }
    
    public boolean resumeAudio(final MessageObject messageObject) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && this.playingMessageObject != null) {
            if (this.isSamePlayingMessage(messageObject)) {
                try {
                    this.startProgressTimer(this.playingMessageObject);
                    if (this.audioPlayer != null) {
                        this.audioPlayer.play();
                    }
                    else if (this.videoPlayer != null) {
                        this.videoPlayer.play();
                    }
                    this.checkAudioFocus(messageObject);
                    this.isPaused = false;
                    NotificationCenter.getInstance(this.playingMessageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingPlayStateChanged, this.playingMessageObject.getId());
                    return true;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        return false;
    }
    
    public void scheduleVideoConvert(final MessageObject messageObject) {
        this.scheduleVideoConvert(messageObject, false);
    }
    
    public boolean scheduleVideoConvert(final MessageObject e, final boolean b) {
        if (e == null || e.videoEditedInfo == null) {
            return false;
        }
        if (b && !this.videoConvertQueue.isEmpty()) {
            return false;
        }
        if (b) {
            new File(e.messageOwner.attachPath).delete();
        }
        this.videoConvertQueue.add(e);
        if (this.videoConvertQueue.size() == 1) {
            this.startVideoConvertFromQueue();
        }
        return true;
    }
    
    public boolean seekToProgress(final MessageObject messageObject, final float n) {
        if ((this.audioPlayer != null || this.videoPlayer != null) && messageObject != null && this.playingMessageObject != null) {
            if (this.isSamePlayingMessage(messageObject)) {
                try {
                    if (this.audioPlayer != null) {
                        final long duration = this.audioPlayer.getDuration();
                        if (duration == -9223372036854775807L) {
                            this.seekToProgressPending = n;
                        }
                        else {
                            final int n2 = (int)(duration * n);
                            final VideoPlayer audioPlayer = this.audioPlayer;
                            final long lastProgress = n2;
                            audioPlayer.seekTo(lastProgress);
                            this.lastProgress = lastProgress;
                        }
                    }
                    else if (this.videoPlayer != null) {
                        this.videoPlayer.seekTo((long)(this.videoPlayer.getDuration() * n));
                    }
                    NotificationCenter.getInstance(messageObject.currentAccount).postNotificationName(NotificationCenter.messagePlayingDidSeek, this.playingMessageObject.getId(), n);
                    return true;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        return false;
    }
    
    public void setAllowStartRecord(final boolean allowStartRecord) {
        this.allowStartRecord = allowStartRecord;
    }
    
    public void setBaseActivity(final Activity baseActivity, final boolean b) {
        if (b) {
            this.baseActivity = baseActivity;
        }
        else if (this.baseActivity == baseActivity) {
            this.baseActivity = null;
        }
    }
    
    public void setCurrentVideoVisible(final boolean b) {
        final AspectRatioFrameLayout currentAspectRatioFrameLayout = this.currentAspectRatioFrameLayout;
        if (currentAspectRatioFrameLayout == null) {
            return;
        }
        if (b) {
            final PipRoundVideoView pipRoundVideoView = this.pipRoundVideoView;
            if (pipRoundVideoView != null) {
                this.pipSwitchingState = 2;
                pipRoundVideoView.close(true);
                this.pipRoundVideoView = null;
            }
            else if (currentAspectRatioFrameLayout != null) {
                if (currentAspectRatioFrameLayout.getParent() == null) {
                    this.currentTextureViewContainer.addView((View)this.currentAspectRatioFrameLayout);
                }
                this.videoPlayer.setTextureView(this.currentTextureView);
            }
        }
        else if (currentAspectRatioFrameLayout.getParent() != null) {
            this.pipSwitchingState = 1;
            this.currentTextureViewContainer.removeView((View)this.currentAspectRatioFrameLayout);
        }
        else {
            if (this.pipRoundVideoView == null) {
                try {
                    (this.pipRoundVideoView = new PipRoundVideoView()).show(this.baseActivity, new _$$Lambda$MediaController$GKrg4OGTs8RBmP_jQs_T_HQHgXA(this));
                }
                catch (Exception ex) {
                    this.pipRoundVideoView = null;
                }
            }
            final PipRoundVideoView pipRoundVideoView2 = this.pipRoundVideoView;
            if (pipRoundVideoView2 != null) {
                this.videoPlayer.setTextureView(pipRoundVideoView2.getTextureView());
            }
        }
    }
    
    public void setFeedbackView(final View feedbackView, final boolean b) {
        if (b) {
            this.feedbackView = feedbackView;
        }
        else if (this.feedbackView == feedbackView) {
            this.feedbackView = null;
        }
    }
    
    public void setFlagSecure(final BaseFragment p0, final boolean p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifeq            28
        //     4: aload_1        
        //     5: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.getParentActivity:()Landroid/app/Activity;
        //     8: invokevirtual   android/app/Activity.getWindow:()Landroid/view/Window;
        //    11: sipush          8192
        //    14: sipush          8192
        //    17: invokevirtual   android/view/Window.setFlags:(II)V
        //    20: aload_0        
        //    21: aload_1        
        //    22: putfield        org/telegram/messenger/MediaController.flagSecureFragment:Lorg/telegram/ui/ActionBar/BaseFragment;
        //    25: goto            54
        //    28: aload_0        
        //    29: getfield        org/telegram/messenger/MediaController.flagSecureFragment:Lorg/telegram/ui/ActionBar/BaseFragment;
        //    32: aload_1        
        //    33: if_acmpne       54
        //    36: aload_1        
        //    37: invokevirtual   org/telegram/ui/ActionBar/BaseFragment.getParentActivity:()Landroid/app/Activity;
        //    40: invokevirtual   android/app/Activity.getWindow:()Landroid/view/Window;
        //    43: sipush          8192
        //    46: invokevirtual   android/view/Window.clearFlags:(I)V
        //    49: aload_0        
        //    50: aconst_null    
        //    51: putfield        org/telegram/messenger/MediaController.flagSecureFragment:Lorg/telegram/ui/ActionBar/BaseFragment;
        //    54: return         
        //    55: astore_3       
        //    56: goto            20
        //    59: astore_1       
        //    60: goto            49
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      20     55     59     Ljava/lang/Exception;
        //  36     49     59     63     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0049:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void setInputFieldHasText(final boolean inputFieldHasText) {
        this.inputFieldHasText = inputFieldHasText;
    }
    
    public void setLastVisibleMessageIds(final int lastChatAccount, final long lastChatEnterTime, final long lastChatLeaveTime, final TLRPC.User lastUser, final TLRPC.EncryptedChat lastSecretChat, final ArrayList<Long> lastChatVisibleMessages, final int lastMessageId) {
        this.lastChatEnterTime = lastChatEnterTime;
        this.lastChatLeaveTime = lastChatLeaveTime;
        this.lastChatAccount = lastChatAccount;
        this.lastSecretChat = lastSecretChat;
        this.lastUser = lastUser;
        this.lastMessageId = lastMessageId;
        this.lastChatVisibleMessages = lastChatVisibleMessages;
    }
    
    public void setPlaybackSpeed(final float currentPlaybackSpeed) {
        this.currentPlaybackSpeed = currentPlaybackSpeed;
        final VideoPlayer audioPlayer = this.audioPlayer;
        if (audioPlayer != null) {
            audioPlayer.setPlaybackSpeed(this.currentPlaybackSpeed);
        }
        else {
            final VideoPlayer videoPlayer = this.videoPlayer;
            if (videoPlayer != null) {
                videoPlayer.setPlaybackSpeed(this.currentPlaybackSpeed);
            }
        }
        MessagesController.getGlobalMainSettings().edit().putFloat("playbackSpeed", currentPlaybackSpeed).commit();
    }
    
    public boolean setPlaylist(final ArrayList<MessageObject> list, final MessageObject messageObject) {
        return this.setPlaylist(list, messageObject, true);
    }
    
    public boolean setPlaylist(final ArrayList<MessageObject> list, final MessageObject messageObject, final boolean b) {
        if (this.playingMessageObject == messageObject) {
            return this.playMessage(messageObject);
        }
        this.forceLoopCurrentPlaylist = (b ^ true);
        this.playMusicAgain = (this.playlist.isEmpty() ^ true);
        this.playlist.clear();
        for (int i = list.size() - 1; i >= 0; --i) {
            final MessageObject e = list.get(i);
            if (e.isMusic()) {
                this.playlist.add(e);
            }
        }
        this.currentPlaylistNum = this.playlist.indexOf(messageObject);
        if (this.currentPlaylistNum == -1) {
            this.playlist.clear();
            this.shuffledPlaylist.clear();
            this.currentPlaylistNum = this.playlist.size();
            this.playlist.add(messageObject);
        }
        if (messageObject.isMusic()) {
            if (SharedConfig.shuffleMusic) {
                this.buildShuffledPlayList();
                this.currentPlaylistNum = 0;
            }
            if (b) {
                DataQuery.getInstance(messageObject.currentAccount).loadMusic(messageObject.getDialogId(), this.playlist.get(0).getIdWithChannel());
            }
        }
        return this.playMessage(messageObject);
    }
    
    public void setReplyingMessage(final MessageObject recordReplyingMessageObject) {
        this.recordReplyingMessageObject = recordReplyingMessageObject;
    }
    
    public void setTextureView(final TextureView currentTextureView, final AspectRatioFrameLayout currentAspectRatioFrameLayout, final FrameLayout currentTextureViewContainer, final boolean b) {
        if (currentTextureView == null) {
            return;
        }
        final boolean b2 = true;
        if (!b && this.currentTextureView == currentTextureView) {
            this.pipSwitchingState = 1;
            this.currentTextureView = null;
            this.currentAspectRatioFrameLayout = null;
            this.currentTextureViewContainer = null;
            return;
        }
        if (this.videoPlayer != null) {
            if (currentTextureView != this.currentTextureView) {
                this.isDrawingWasReady = (currentAspectRatioFrameLayout != null && currentAspectRatioFrameLayout.isDrawingReady() && b2);
                this.currentTextureView = currentTextureView;
                final PipRoundVideoView pipRoundVideoView = this.pipRoundVideoView;
                if (pipRoundVideoView != null) {
                    this.videoPlayer.setTextureView(pipRoundVideoView.getTextureView());
                }
                else {
                    this.videoPlayer.setTextureView(this.currentTextureView);
                }
                this.currentAspectRatioFrameLayout = currentAspectRatioFrameLayout;
                this.currentTextureViewContainer = currentTextureViewContainer;
                if (this.currentAspectRatioFrameLayoutReady) {
                    final AspectRatioFrameLayout currentAspectRatioFrameLayout2 = this.currentAspectRatioFrameLayout;
                    if (currentAspectRatioFrameLayout2 != null && currentAspectRatioFrameLayout2 != null) {
                        currentAspectRatioFrameLayout2.setAspectRatio(this.currentAspectRatioFrameLayoutRatio, this.currentAspectRatioFrameLayoutRotation);
                    }
                }
            }
        }
    }
    
    public void setVoiceMessagesPlaylist(final ArrayList<MessageObject> voiceMessagesPlaylist, final boolean voiceMessagesPlaylistUnread) {
        this.voiceMessagesPlaylist = voiceMessagesPlaylist;
        if (this.voiceMessagesPlaylist != null) {
            this.voiceMessagesPlaylistUnread = voiceMessagesPlaylistUnread;
            this.voiceMessagesPlaylistMap = (SparseArray<MessageObject>)new SparseArray();
            for (int i = 0; i < this.voiceMessagesPlaylist.size(); ++i) {
                final MessageObject messageObject = this.voiceMessagesPlaylist.get(i);
                this.voiceMessagesPlaylistMap.put(messageObject.getId(), (Object)messageObject);
            }
        }
    }
    
    public void startMediaObserver() {
        ApplicationLoader.applicationHandler.removeCallbacks((Runnable)this.stopMediaObserverRunnable);
        ++this.startObserverToken;
        try {
            if (this.internalObserver == null) {
                ApplicationLoader.applicationContext.getContentResolver().registerContentObserver(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, false, (ContentObserver)(this.externalObserver = new ExternalObserver()));
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            if (this.externalObserver == null) {
                ApplicationLoader.applicationContext.getContentResolver().registerContentObserver(MediaStore$Images$Media.INTERNAL_CONTENT_URI, false, (ContentObserver)(this.internalObserver = new InternalObserver()));
            }
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
    }
    
    public void startRaiseToEarSensors(final ChatActivity raiseChat) {
        if (raiseChat != null && (this.accelerometerSensor != null || (this.gravitySensor != null && this.linearAcceleration != null))) {
            if (this.proximitySensor != null) {
                this.raiseChat = raiseChat;
                if (!SharedConfig.raiseToSpeak) {
                    final MessageObject playingMessageObject = this.playingMessageObject;
                    if (playingMessageObject == null || (!playingMessageObject.isVoice() && !this.playingMessageObject.isRoundVideo())) {
                        return;
                    }
                }
                if (!this.sensorsStarted) {
                    final float[] gravity = this.gravity;
                    gravity[2] = 0.0f;
                    gravity[0] = (gravity[1] = 0.0f);
                    final float[] linearAcceleration = this.linearAcceleration;
                    linearAcceleration[2] = 0.0f;
                    linearAcceleration[0] = (linearAcceleration[1] = 0.0f);
                    final float[] gravityFast = this.gravityFast;
                    gravityFast[2] = 0.0f;
                    gravityFast[0] = (gravityFast[1] = 0.0f);
                    this.lastTimestamp = 0L;
                    this.previousAccValue = 0.0f;
                    this.raisedToTop = 0;
                    this.raisedToTopSign = 0;
                    this.countLess = 0;
                    this.raisedToBack = 0;
                    Utilities.globalQueue.postRunnable(new _$$Lambda$MediaController$XP8XA61VZfZeH2YNAAQQ28iI33I(this));
                    this.sensorsStarted = true;
                }
            }
        }
    }
    
    public void startRecording(final int n, long n2, MessageObject recordStartRunnable) {
        final MessageObject playingMessageObject = this.playingMessageObject;
        boolean b;
        if (playingMessageObject != null && this.isPlayingMessage(playingMessageObject) && !this.isMessagePaused()) {
            b = true;
            this.pauseMessage(this.playingMessageObject);
        }
        else {
            b = false;
        }
        while (true) {
            try {
                this.feedbackView.performHapticFeedback(3, 2);
                final DispatchQueue recordQueue = this.recordQueue;
                recordStartRunnable = (MessageObject)new _$$Lambda$MediaController$aNYrkuu1k_Q9tWrqTPM_3Nml9Zg(this, n, n2, recordStartRunnable);
                this.recordStartRunnable = (Runnable)recordStartRunnable;
                if (b) {
                    n2 = 500L;
                }
                else {
                    n2 = 50L;
                }
                recordQueue.postRunnable((Runnable)recordStartRunnable, n2);
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public void startRecordingIfFromSpeaker() {
        if (this.useFrontSpeaker) {
            final ChatActivity raiseChat = this.raiseChat;
            if (raiseChat != null) {
                if (this.allowStartRecord) {
                    this.raiseToEarRecord = true;
                    this.startRecording(raiseChat.getCurrentAccount(), this.raiseChat.getDialogId(), null);
                    this.ignoreOnPause = true;
                }
            }
        }
    }
    
    public void stopMediaObserver() {
        if (this.stopMediaObserverRunnable == null) {
            this.stopMediaObserverRunnable = new StopMediaObserverRunnable();
        }
        this.stopMediaObserverRunnable.currentObserverToken = this.startObserverToken;
        ApplicationLoader.applicationHandler.postDelayed((Runnable)this.stopMediaObserverRunnable, 5000L);
    }
    
    public void stopRaiseToEarSensors(final ChatActivity chatActivity, final boolean b) {
        if (this.ignoreOnPause) {
            this.ignoreOnPause = false;
            return;
        }
        int n;
        if (b) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.stopRecording(n);
        if (this.sensorsStarted && !this.ignoreOnPause && (this.accelerometerSensor != null || (this.gravitySensor != null && this.linearAcceleration != null)) && this.proximitySensor != null) {
            if (this.raiseChat == chatActivity) {
                this.raiseChat = null;
                this.sensorsStarted = false;
                this.accelerometerVertical = false;
                this.proximityTouched = false;
                this.raiseToEarRecord = false;
                this.useFrontSpeaker = false;
                Utilities.globalQueue.postRunnable(new _$$Lambda$MediaController$5zcBDCHMQng3baqUjT_bO0RgccA(this));
                if (this.proximityHasDifferentValues) {
                    final PowerManager$WakeLock proximityWakeLock = this.proximityWakeLock;
                    if (proximityWakeLock != null && proximityWakeLock.isHeld()) {
                        this.proximityWakeLock.release();
                    }
                }
            }
        }
    }
    
    public void stopRecording(final int n) {
        final Runnable recordStartRunnable = this.recordStartRunnable;
        if (recordStartRunnable != null) {
            this.recordQueue.cancelRunnable(recordStartRunnable);
            this.recordStartRunnable = null;
        }
        this.recordQueue.postRunnable(new _$$Lambda$MediaController$9Mt4rRJGCphHi4pab0_bDovIi84(this, n));
    }
    
    public void toggleShuffleMusic(final int n) {
        final boolean shuffleMusic = SharedConfig.shuffleMusic;
        SharedConfig.toggleShuffleMusic(n);
        final boolean shuffleMusic2 = SharedConfig.shuffleMusic;
        if (shuffleMusic != shuffleMusic2) {
            if (shuffleMusic2) {
                this.buildShuffledPlayList();
                this.currentPlaylistNum = 0;
            }
            else {
                final MessageObject playingMessageObject = this.playingMessageObject;
                if (playingMessageObject != null) {
                    this.currentPlaylistNum = this.playlist.indexOf(playingMessageObject);
                    if (this.currentPlaylistNum == -1) {
                        this.playlist.clear();
                        this.shuffledPlaylist.clear();
                        this.cleanupPlayer(true, true);
                    }
                }
            }
        }
    }
    
    public static class AlbumEntry
    {
        public int bucketId;
        public String bucketName;
        public PhotoEntry coverPhoto;
        public ArrayList<PhotoEntry> photos;
        public SparseArray<PhotoEntry> photosByIds;
        
        public AlbumEntry(final int bucketId, final String bucketName, final PhotoEntry coverPhoto) {
            this.photos = new ArrayList<PhotoEntry>();
            this.photosByIds = (SparseArray<PhotoEntry>)new SparseArray();
            this.bucketId = bucketId;
            this.bucketName = bucketName;
            this.coverPhoto = coverPhoto;
        }
        
        public void addPhoto(final PhotoEntry e) {
            this.photos.add(e);
            this.photosByIds.put(e.imageId, (Object)e);
        }
    }
    
    private class AudioBuffer
    {
        ByteBuffer buffer;
        byte[] bufferBytes;
        int finished;
        long pcmOffset;
        int size;
        
        public AudioBuffer(final int capacity) {
            this.buffer = ByteBuffer.allocateDirect(capacity);
            this.bufferBytes = new byte[capacity];
        }
    }
    
    public static class AudioEntry
    {
        public String author;
        public int duration;
        public String genre;
        public long id;
        public MessageObject messageObject;
        public String path;
        public String title;
    }
    
    private class ExternalObserver extends ContentObserver
    {
        public ExternalObserver() {
            super((Handler)null);
        }
        
        public void onChange(final boolean b) {
            super.onChange(b);
            MediaController.this.processMediaObserver(MediaStore$Images$Media.EXTERNAL_CONTENT_URI);
        }
    }
    
    private class GalleryObserverExternal extends ContentObserver
    {
        public GalleryObserverExternal() {
            super((Handler)null);
        }
        
        public void onChange(final boolean b) {
            super.onChange(b);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            final -$$Lambda$MediaController$GalleryObserverExternal$dwi1SqFQz-7StR-lsnN0S1Sqd6M instance = _$$Lambda$MediaController$GalleryObserverExternal$dwi1SqFQz_7StR_lsnN0S1Sqd6M.INSTANCE;
            MediaController.refreshGalleryRunnable = (Runnable)instance;
            AndroidUtilities.runOnUIThread((Runnable)instance, 2000L);
        }
    }
    
    private class GalleryObserverInternal extends ContentObserver
    {
        public GalleryObserverInternal() {
            super((Handler)null);
        }
        
        private void scheduleReloadRunnable() {
            final _$$Lambda$MediaController$GalleryObserverInternal$1zsHrCjjtoIwj0Amhv_aO54Om6g $$Lambda$MediaController$GalleryObserverInternal$1zsHrCjjtoIwj0Amhv_aO54Om6g = new _$$Lambda$MediaController$GalleryObserverInternal$1zsHrCjjtoIwj0Amhv_aO54Om6g(this);
            MediaController.refreshGalleryRunnable = $$Lambda$MediaController$GalleryObserverInternal$1zsHrCjjtoIwj0Amhv_aO54Om6g;
            AndroidUtilities.runOnUIThread($$Lambda$MediaController$GalleryObserverInternal$1zsHrCjjtoIwj0Amhv_aO54Om6g, 2000L);
        }
        
        public void onChange(final boolean b) {
            super.onChange(b);
            if (MediaController.refreshGalleryRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(MediaController.refreshGalleryRunnable);
            }
            this.scheduleReloadRunnable();
        }
    }
    
    private class InternalObserver extends ContentObserver
    {
        public InternalObserver() {
            super((Handler)null);
        }
        
        public void onChange(final boolean b) {
            super.onChange(b);
            MediaController.this.processMediaObserver(MediaStore$Images$Media.INTERNAL_CONTENT_URI);
        }
    }
    
    public static class PhotoEntry
    {
        public int bucketId;
        public boolean canDeleteAfter;
        public CharSequence caption;
        public long dateTaken;
        public int duration;
        public VideoEditedInfo editedInfo;
        public ArrayList<TLRPC.MessageEntity> entities;
        public int imageId;
        public String imagePath;
        public boolean isCropped;
        public boolean isFiltered;
        public boolean isMuted;
        public boolean isPainted;
        public boolean isVideo;
        public int orientation;
        public String path;
        public SavedFilterState savedFilterState;
        public ArrayList<TLRPC.InputDocument> stickers;
        public String thumbPath;
        public int ttl;
        
        public PhotoEntry(final int bucketId, final int imageId, final long dateTaken, final String path, final int n, final boolean isVideo) {
            this.stickers = new ArrayList<TLRPC.InputDocument>();
            this.bucketId = bucketId;
            this.imageId = imageId;
            this.dateTaken = dateTaken;
            this.path = path;
            if (isVideo) {
                this.duration = n;
            }
            else {
                this.orientation = n;
            }
            this.isVideo = isVideo;
        }
        
        public void reset() {
            this.isFiltered = false;
            this.isPainted = false;
            this.isCropped = false;
            this.ttl = 0;
            this.imagePath = null;
            if (!this.isVideo) {
                this.thumbPath = null;
            }
            this.editedInfo = null;
            this.caption = null;
            this.entities = null;
            this.savedFilterState = null;
            this.stickers.clear();
        }
    }
    
    public static class SavedFilterState
    {
        public float blurAngle;
        public float blurExcludeBlurSize;
        public Point blurExcludePoint;
        public float blurExcludeSize;
        public int blurType;
        public float contrastValue;
        public PhotoFilterView.CurvesToolValue curvesToolValue;
        public float enhanceValue;
        public float exposureValue;
        public float fadeValue;
        public float grainValue;
        public float highlightsValue;
        public float saturationValue;
        public float shadowsValue;
        public float sharpenValue;
        public int tintHighlightsColor;
        public int tintShadowsColor;
        public float vignetteValue;
        public float warmthValue;
        
        public SavedFilterState() {
            this.curvesToolValue = new PhotoFilterView.CurvesToolValue();
        }
    }
    
    public static class SearchImage
    {
        public CharSequence caption;
        public int date;
        public TLRPC.Document document;
        public ArrayList<TLRPC.MessageEntity> entities;
        public int height;
        public String id;
        public String imagePath;
        public String imageUrl;
        public boolean isCropped;
        public boolean isFiltered;
        public boolean isPainted;
        public String localUrl;
        public TLRPC.Photo photo;
        public TLRPC.PhotoSize photoSize;
        public SavedFilterState savedFilterState;
        public int size;
        public ArrayList<TLRPC.InputDocument> stickers;
        public String thumbPath;
        public TLRPC.PhotoSize thumbPhotoSize;
        public String thumbUrl;
        public int ttl;
        public int type;
        public int width;
        
        public SearchImage() {
            this.stickers = new ArrayList<TLRPC.InputDocument>();
        }
        
        public String getAttachName() {
            final TLRPC.PhotoSize photoSize = this.photoSize;
            if (photoSize != null) {
                return FileLoader.getAttachFileName(photoSize);
            }
            final TLRPC.Document document = this.document;
            if (document != null) {
                return FileLoader.getAttachFileName(document);
            }
            if (this.type != 1) {
                final String localUrl = this.localUrl;
                if (localUrl != null && localUrl.length() > 0) {
                    final File file = new File(this.localUrl);
                    if (file.exists()) {
                        return file.getName();
                    }
                    this.localUrl = "";
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(Utilities.MD5(this.imageUrl));
            sb.append(".");
            sb.append(ImageLoader.getHttpUrlExtension(this.imageUrl, "jpg"));
            return sb.toString();
        }
        
        public String getPathToAttach() {
            final TLRPC.PhotoSize photoSize = this.photoSize;
            if (photoSize != null) {
                return FileLoader.getPathToAttach(photoSize, true).getAbsolutePath();
            }
            final TLRPC.Document document = this.document;
            if (document != null) {
                return FileLoader.getPathToAttach(document, true).getAbsolutePath();
            }
            return this.imageUrl;
        }
        
        public void reset() {
            this.isFiltered = false;
            this.isPainted = false;
            this.isCropped = false;
            this.ttl = 0;
            this.imagePath = null;
            this.thumbPath = null;
            this.caption = null;
            this.entities = null;
            this.savedFilterState = null;
            this.stickers.clear();
        }
    }
    
    private final class StopMediaObserverRunnable implements Runnable
    {
        public int currentObserverToken;
        
        private StopMediaObserverRunnable() {
            this.currentObserverToken = 0;
        }
        
        @Override
        public void run() {
            if (this.currentObserverToken == MediaController.this.startObserverToken) {
                try {
                    if (MediaController.this.internalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver((ContentObserver)MediaController.this.internalObserver);
                        MediaController.this.internalObserver = null;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                try {
                    if (MediaController.this.externalObserver != null) {
                        ApplicationLoader.applicationContext.getContentResolver().unregisterContentObserver((ContentObserver)MediaController.this.externalObserver);
                        MediaController.this.externalObserver = null;
                    }
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
        }
    }
    
    private static class VideoConvertRunnable implements Runnable
    {
        private MessageObject messageObject;
        
        private VideoConvertRunnable(final MessageObject messageObject) {
            this.messageObject = messageObject;
        }
        
        public static void runConversion(final MessageObject messageObject) {
            new Thread(new _$$Lambda$MediaController$VideoConvertRunnable$HHYJEBFJxn1b5hYjDjNcGHVKJ_Y(messageObject)).start();
        }
        
        @Override
        public void run() {
            MediaController.getInstance().convertVideo(this.messageObject);
        }
    }
}
