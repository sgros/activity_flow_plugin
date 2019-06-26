package org.telegram.p004ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaController.PhotoEntry;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.camera.CameraInfo;
import org.telegram.messenger.camera.CameraSession;
import org.telegram.messenger.camera.Size;
import org.telegram.messenger.video.MP4Builder;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ChatActivity;
import org.telegram.p004ui.Components.VideoPlayer.VideoPlayerDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.InputEncryptedFile;
import org.telegram.tgnet.TLRPC.InputFile;

@TargetApi(18)
/* renamed from: org.telegram.ui.Components.InstantCameraView */
public class InstantCameraView extends FrameLayout implements NotificationCenterDelegate {
    private static final String FRAGMENT_SCREEN_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision lowp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float scaleX;\nuniform float scaleY;\nuniform float alpha;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec2 coord = vec2((vTextureCoord.x - 0.5) * scaleX, (vTextureCoord.y - 0.5) * scaleY);\n   float coef = ceil(clamp(0.2601 - dot(coord, coord), 0.0, 1.0));\n   vec3 color = texture2D(sTexture, vTextureCoord).rgb * coef + (1.0 - step(0.001, coef));\n   gl_FragColor = vec4(color * alpha, alpha);\n}\n";
    private static final int MSG_AUDIOFRAME_AVAILABLE = 3;
    private static final int MSG_START_RECORDING = 0;
    private static final int MSG_STOP_RECORDING = 1;
    private static final int MSG_VIDEOFRAME_AVAILABLE = 2;
    private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private AnimatorSet animatorSet;
    private Size aspectRatio;
    private ChatActivity baseFragment;
    private FrameLayout cameraContainer;
    private File cameraFile;
    private volatile boolean cameraReady;
    private CameraSession cameraSession;
    private int[] cameraTexture = new int[1];
    private float cameraTextureAlpha = 1.0f;
    private CameraGLThread cameraThread;
    private boolean cancelled;
    private int currentAccount = UserConfig.selectedAccount;
    private boolean deviceHasGoodCamera;
    private long duration;
    private InputEncryptedFile encryptedFile;
    private InputFile file;
    private boolean isFrontface = true;
    private boolean isSecretChat;
    /* renamed from: iv */
    private byte[] f633iv;
    private byte[] key;
    private Bitmap lastBitmap;
    private float[] mMVPMatrix;
    private float[] mSTMatrix;
    private float[] moldSTMatrix;
    private AnimatorSet muteAnimation;
    private ImageView muteImageView;
    private int[] oldCameraTexture = new int[1];
    private Paint paint;
    private Size pictureSize;
    private int[] position = new int[2];
    private Size previewSize;
    private float progress;
    private Timer progressTimer;
    private long recordStartTime;
    private long recordedTime;
    private boolean recording;
    private RectF rect;
    private boolean requestingPermissions;
    private float scaleX;
    private float scaleY;
    private CameraInfo selectedCamera;
    private long size;
    private ImageView switchCameraButton;
    private FloatBuffer textureBuffer;
    private BackupImageView textureOverlayView;
    private TextureView textureView;
    private Runnable timerRunnable = new C28581();
    private FloatBuffer vertexBuffer;
    private VideoEditedInfo videoEditedInfo;
    private VideoPlayer videoPlayer;

    /* renamed from: org.telegram.ui.Components.InstantCameraView$10 */
    class C285710 extends TimerTask {
        C285710() {
        }

        public void run() {
            AndroidUtilities.runOnUIThread(new C2592-$$Lambda$InstantCameraView$10$Q79AnmgYDpgQ0WR-lA8rbDjA_6E(this));
        }

        public /* synthetic */ void lambda$run$0$InstantCameraView$10() {
            try {
                if (InstantCameraView.this.videoPlayer != null && InstantCameraView.this.videoEditedInfo != null) {
                    long j = 0;
                    if (InstantCameraView.this.videoEditedInfo.endTime > 0 && InstantCameraView.this.videoPlayer.getCurrentPosition() >= InstantCameraView.this.videoEditedInfo.endTime) {
                        VideoPlayer access$900 = InstantCameraView.this.videoPlayer;
                        if (InstantCameraView.this.videoEditedInfo.startTime > 0) {
                            j = InstantCameraView.this.videoEditedInfo.startTime;
                        }
                        access$900.seekTo(j);
                    }
                }
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$1 */
    class C28581 implements Runnable {
        C28581() {
        }

        public void run() {
            if (InstantCameraView.this.recording) {
                NotificationCenter instance = NotificationCenter.getInstance(InstantCameraView.this.currentAccount);
                int i = NotificationCenter.recordProgressChanged;
                Object[] objArr = new Object[2];
                long currentTimeMillis = System.currentTimeMillis() - InstantCameraView.this.recordStartTime;
                InstantCameraView.this.duration = currentTimeMillis;
                objArr[0] = Long.valueOf(currentTimeMillis);
                objArr[1] = Double.valueOf(0.0d);
                instance.postNotificationName(i, objArr);
                AndroidUtilities.runOnUIThread(InstantCameraView.this.timerRunnable, 50);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$2 */
    class C28592 extends AnimatorListenerAdapter {
        C28592() {
        }

        public void onAnimationEnd(Animator animator) {
            if (animator.equals(InstantCameraView.this.muteAnimation)) {
                InstantCameraView.this.muteAnimation = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$5 */
    class C28625 extends ViewOutlineProvider {
        C28625() {
        }

        @TargetApi(21)
        public void getOutline(View view, Outline outline) {
            int i = AndroidUtilities.roundMessageSize;
            outline.setOval(0, 0, i, i);
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$7 */
    class C28647 extends AnimatorListenerAdapter {
        C28647() {
        }

        public void onAnimationEnd(Animator animator) {
            InstantCameraView.this.switchCameraButton.setImageResource(InstantCameraView.this.isFrontface ? C1067R.C1065drawable.camera_revert1 : C1067R.C1065drawable.camera_revert2);
            ObjectAnimator.ofFloat(InstantCameraView.this.switchCameraButton, "scaleX", new float[]{1.0f}).setDuration(100).start();
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$8 */
    class C28658 implements SurfaceTextureListener {
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        C28658() {
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("camera surface available");
            }
            if (InstantCameraView.this.cameraThread == null && surfaceTexture != null && !InstantCameraView.this.cancelled) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.m27d("start create thread");
                }
                InstantCameraView instantCameraView = InstantCameraView.this;
                instantCameraView.cameraThread = new CameraGLThread(surfaceTexture, i, i2);
            }
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            if (InstantCameraView.this.cameraThread != null) {
                InstantCameraView.this.cameraThread.shutdown(0);
                InstantCameraView.this.cameraThread = null;
            }
            if (InstantCameraView.this.cameraSession != null) {
                CameraController.getInstance().close(InstantCameraView.this.cameraSession, null, null);
            }
            return true;
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$9 */
    class C28669 extends AnimatorListenerAdapter {
        C28669() {
        }

        public void onAnimationEnd(Animator animator) {
            if (animator.equals(InstantCameraView.this.animatorSet)) {
                InstantCameraView.this.hideCamera(true);
                InstantCameraView.this.setVisibility(4);
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$AudioBufferInfo */
    private class AudioBufferInfo {
        byte[] buffer;
        boolean last;
        int lastWroteBuffer;
        long[] offset;
        int[] read;
        int results;

        private AudioBufferInfo() {
            this.buffer = new byte[20480];
            this.offset = new long[10];
            this.read = new int[10];
        }

        /* synthetic */ AudioBufferInfo(InstantCameraView instantCameraView, C28581 c28581) {
            this();
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$EncoderHandler */
    private static class EncoderHandler extends Handler {
        private WeakReference<VideoRecorder> mWeakEncoder;

        public EncoderHandler(VideoRecorder videoRecorder) {
            this.mWeakEncoder = new WeakReference(videoRecorder);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            Object obj = message.obj;
            VideoRecorder videoRecorder = (VideoRecorder) this.mWeakEncoder.get();
            if (videoRecorder != null) {
                if (i == 0) {
                    try {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.m28e("start encoder");
                        }
                        videoRecorder.prepareEncoder();
                    } catch (Exception e) {
                        FileLog.m30e(e);
                        videoRecorder.handleStopRecording(0);
                        Looper.myLooper().quit();
                    }
                } else if (i == 1) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m28e("stop encoder");
                    }
                    videoRecorder.handleStopRecording(message.arg1);
                } else if (i == 2) {
                    videoRecorder.handleVideoFrameAvailable((((long) message.arg1) << 32) | (((long) message.arg2) & 4294967295L), (Integer) message.obj);
                } else if (i == 3) {
                    videoRecorder.handleAudioFrameAvailable((AudioBufferInfo) message.obj);
                }
            }
        }

        public void exit() {
            Looper.myLooper().quit();
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$VideoRecorder */
    private class VideoRecorder implements Runnable {
        private static final String AUDIO_MIME_TYPE = "audio/mp4a-latm";
        private static final int FRAME_RATE = 30;
        private static final int IFRAME_INTERVAL = 1;
        private static final String VIDEO_MIME_TYPE = "video/avc";
        private int alphaHandle;
        private BufferInfo audioBufferInfo;
        private MediaCodec audioEncoder;
        private long audioFirst;
        private AudioRecord audioRecorder;
        private long audioStartTime;
        private boolean audioStopedByTime;
        private int audioTrackIndex;
        private boolean blendEnabled;
        private ArrayBlockingQueue<AudioBufferInfo> buffers;
        private ArrayList<AudioBufferInfo> buffersToWrite;
        private long currentTimestamp;
        private long desyncTime;
        private int drawProgram;
        private EGLConfig eglConfig;
        private EGLContext eglContext;
        private EGLDisplay eglDisplay;
        private EGLSurface eglSurface;
        private volatile EncoderHandler handler;
        private Integer lastCameraId;
        private long lastCommitedFrameTime;
        private long lastTimestamp;
        private MP4Builder mediaMuxer;
        private int positionHandle;
        private boolean ready;
        private Runnable recorderRunnable;
        private volatile boolean running;
        private int scaleXHandle;
        private int scaleYHandle;
        private volatile int sendWhenDone;
        private EGLContext sharedEglContext;
        private boolean skippedFirst;
        private long skippedTime;
        private Surface surface;
        private final Object sync;
        private int textureHandle;
        private int textureMatrixHandle;
        private int vertexMatrixHandle;
        private int videoBitrate;
        private BufferInfo videoBufferInfo;
        private boolean videoConvertFirstWrite;
        private MediaCodec videoEncoder;
        private File videoFile;
        private long videoFirst;
        private int videoHeight;
        private long videoLast;
        private int videoTrackIndex;
        private int videoWidth;
        private int zeroTimeStamps;

        /* renamed from: org.telegram.ui.Components.InstantCameraView$VideoRecorder$1 */
        class C28671 implements Runnable {
            C28671() {
            }

            /* JADX WARNING: Missing block: B:13:0x002d, code skipped:
            if (org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.access$3300(r14.this$1) == 0) goto L_0x00e3;
     */
            public void run() {
                /*
                r14 = this;
                r0 = -1;
                r2 = 0;
                r4 = r0;
                r3 = 0;
            L_0x0005:
                r6 = 1;
                if (r3 != 0) goto L_0x00e3;
            L_0x0008:
                r7 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r7 = r7.running;
                if (r7 != 0) goto L_0x0031;
            L_0x0010:
                r7 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r7 = r7.audioRecorder;
                r7 = r7.getRecordingState();
                if (r7 == r6) goto L_0x0031;
            L_0x001c:
                r7 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;	 Catch:{ Exception -> 0x0026 }
                r7 = r7.audioRecorder;	 Catch:{ Exception -> 0x0026 }
                r7.stop();	 Catch:{ Exception -> 0x0026 }
                goto L_0x0027;
            L_0x0026:
                r3 = 1;
            L_0x0027:
                r7 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r7 = r7.sendWhenDone;
                if (r7 != 0) goto L_0x0031;
            L_0x002f:
                goto L_0x00e3;
            L_0x0031:
                r7 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r7 = r7.buffers;
                r7 = r7.isEmpty();
                if (r7 == 0) goto L_0x0048;
            L_0x003d:
                r7 = new org.telegram.ui.Components.InstantCameraView$AudioBufferInfo;
                r8 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r8 = org.telegram.p004ui.Components.InstantCameraView.this;
                r9 = 0;
                r7.<init>(r8, r9);
                goto L_0x0054;
            L_0x0048:
                r7 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r7 = r7.buffers;
                r7 = r7.poll();
                r7 = (org.telegram.p004ui.Components.InstantCameraView.AudioBufferInfo) r7;
            L_0x0054:
                r7.lastWroteBuffer = r2;
                r8 = 10;
                r7.results = r8;
                r9 = r4;
                r4 = 0;
            L_0x005c:
                if (r4 >= r8) goto L_0x00a0;
            L_0x005e:
                r5 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
                if (r5 != 0) goto L_0x0069;
            L_0x0062:
                r9 = java.lang.System.nanoTime();
                r11 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
                r9 = r9 / r11;
            L_0x0069:
                r5 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r5 = r5.audioRecorder;
                r11 = r7.buffer;
                r12 = r4 * 2048;
                r13 = 2048; // 0x800 float:2.87E-42 double:1.0118E-320;
                r5 = r5.read(r11, r12, r13);
                if (r5 > 0) goto L_0x0088;
            L_0x007b:
                r7.results = r4;
                r4 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r4 = r4.running;
                if (r4 != 0) goto L_0x00a0;
            L_0x0085:
                r7.last = r6;
                goto L_0x00a0;
            L_0x0088:
                r11 = r7.offset;
                r11[r4] = r9;
                r11 = r7.read;
                r11[r4] = r5;
                r11 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
                r5 = r5 * r11;
                r11 = 44100; // 0xac44 float:6.1797E-41 double:2.17883E-319;
                r5 = r5 / r11;
                r5 = r5 / 2;
                r11 = (long) r5;
                r9 = r9 + r11;
                r4 = r4 + 1;
                goto L_0x005c;
            L_0x00a0:
                r4 = r9;
                r9 = r7.results;
                if (r9 >= 0) goto L_0x00c0;
            L_0x00a5:
                r9 = r7.last;
                if (r9 == 0) goto L_0x00aa;
            L_0x00a9:
                goto L_0x00c0;
            L_0x00aa:
                r8 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r8 = r8.running;
                if (r8 != 0) goto L_0x00b5;
            L_0x00b2:
                r3 = 1;
                goto L_0x0005;
            L_0x00b5:
                r6 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;	 Catch:{ Exception -> 0x0005 }
                r6 = r6.buffers;	 Catch:{ Exception -> 0x0005 }
                r6.put(r7);	 Catch:{ Exception -> 0x0005 }
                goto L_0x0005;
            L_0x00c0:
                r9 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r9 = r9.running;
                if (r9 != 0) goto L_0x00cd;
            L_0x00c8:
                r9 = r7.results;
                if (r9 >= r8) goto L_0x00cd;
            L_0x00cc:
                r3 = 1;
            L_0x00cd:
                r6 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r6 = r6.handler;
                r8 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r8 = r8.handler;
                r9 = 3;
                r7 = r8.obtainMessage(r9, r7);
                r6.sendMessage(r7);
                goto L_0x0005;
            L_0x00e3:
                r0 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;	 Catch:{ Exception -> 0x00ed }
                r0 = r0.audioRecorder;	 Catch:{ Exception -> 0x00ed }
                r0.release();	 Catch:{ Exception -> 0x00ed }
                goto L_0x00f1;
            L_0x00ed:
                r0 = move-exception;
                org.telegram.messenger.FileLog.m30e(r0);
            L_0x00f1:
                r0 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r0 = r0.handler;
                r1 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r1 = r1.handler;
                r3 = org.telegram.p004ui.Components.InstantCameraView.VideoRecorder.this;
                r3 = r3.sendWhenDone;
                r1 = r1.obtainMessage(r6, r3, r2);
                r0.sendMessage(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.InstantCameraView$VideoRecorder$C28671.run():void");
            }
        }

        /* renamed from: org.telegram.ui.Components.InstantCameraView$VideoRecorder$2 */
        class C28682 implements VideoPlayerDelegate {
            public void onRenderedFirstFrame() {
            }

            public boolean onSurfaceDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            public void onVideoSizeChanged(int i, int i2, int i3, float f) {
            }

            C28682() {
            }

            public void onStateChanged(boolean z, int i) {
                if (InstantCameraView.this.videoPlayer != null && InstantCameraView.this.videoPlayer.isPlaying() && i == 4) {
                    VideoPlayer access$900 = InstantCameraView.this.videoPlayer;
                    long j = 0;
                    if (InstantCameraView.this.videoEditedInfo.startTime > 0) {
                        j = InstantCameraView.this.videoEditedInfo.startTime;
                    }
                    access$900.seekTo(j);
                }
            }

            public void onError(Exception exception) {
                FileLog.m30e((Throwable) exception);
            }
        }

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:61:0x02c2 in {4, 7, 11, 14, 26, 28, 37, 39, 45, 46, 47, 49, 51, 53, 55, 57, 60} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        private void prepareEncoder() {
            /*
            r25 = this;
            r1 = r25;
            r0 = "video/avc";
            r2 = "bitrate";
            r3 = "audio/mp4a-latm";
            r4 = 16;
            r5 = 44100; // 0xac44 float:6.1797E-41 double:2.17883E-319;
            r6 = 2;
            r4 = android.media.AudioRecord.getMinBufferSize(r5, r4, r6);	 Catch:{ Exception -> 0x02bb }
            if (r4 > 0) goto L_0x0016;	 Catch:{ Exception -> 0x02bb }
            r4 = 3584; // 0xe00 float:5.022E-42 double:1.7707E-320;	 Catch:{ Exception -> 0x02bb }
            r7 = 49152; // 0xc000 float:6.8877E-41 double:2.42843E-319;	 Catch:{ Exception -> 0x02bb }
            r8 = 1;	 Catch:{ Exception -> 0x02bb }
            if (r7 >= r4) goto L_0x0023;	 Catch:{ Exception -> 0x02bb }
            r4 = r4 / 2048;	 Catch:{ Exception -> 0x02bb }
            r4 = r4 + r8;	 Catch:{ Exception -> 0x02bb }
            r4 = r4 * 2048;	 Catch:{ Exception -> 0x02bb }
            r7 = r4 * 2;	 Catch:{ Exception -> 0x02bb }
            r9 = 0;	 Catch:{ Exception -> 0x02bb }
            r15 = 3;	 Catch:{ Exception -> 0x02bb }
            r14 = 0;	 Catch:{ Exception -> 0x02bb }
            if (r9 >= r15) goto L_0x0037;	 Catch:{ Exception -> 0x02bb }
            r10 = r1.buffers;	 Catch:{ Exception -> 0x02bb }
            r11 = new org.telegram.ui.Components.InstantCameraView$AudioBufferInfo;	 Catch:{ Exception -> 0x02bb }
            r12 = org.telegram.p004ui.Components.InstantCameraView.this;	 Catch:{ Exception -> 0x02bb }
            r11.<init>(r12, r14);	 Catch:{ Exception -> 0x02bb }
            r10.add(r11);	 Catch:{ Exception -> 0x02bb }
            r9 = r9 + 1;	 Catch:{ Exception -> 0x02bb }
            goto L_0x0024;	 Catch:{ Exception -> 0x02bb }
            r13 = new android.media.AudioRecord;	 Catch:{ Exception -> 0x02bb }
            r10 = 0;	 Catch:{ Exception -> 0x02bb }
            r11 = 44100; // 0xac44 float:6.1797E-41 double:2.17883E-319;	 Catch:{ Exception -> 0x02bb }
            r12 = 16;	 Catch:{ Exception -> 0x02bb }
            r16 = 2;	 Catch:{ Exception -> 0x02bb }
            r9 = r13;	 Catch:{ Exception -> 0x02bb }
            r15 = r13;	 Catch:{ Exception -> 0x02bb }
            r13 = r16;	 Catch:{ Exception -> 0x02bb }
            r4 = r14;	 Catch:{ Exception -> 0x02bb }
            r14 = r7;	 Catch:{ Exception -> 0x02bb }
            r9.<init>(r10, r11, r12, r13, r14);	 Catch:{ Exception -> 0x02bb }
            r1.audioRecorder = r15;	 Catch:{ Exception -> 0x02bb }
            r9 = r1.audioRecorder;	 Catch:{ Exception -> 0x02bb }
            r9.startRecording();	 Catch:{ Exception -> 0x02bb }
            r9 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x02bb }
            if (r9 == 0) goto L_0x0085;	 Catch:{ Exception -> 0x02bb }
            r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02bb }
            r9.<init>();	 Catch:{ Exception -> 0x02bb }
            r10 = "initied audio record with channels ";	 Catch:{ Exception -> 0x02bb }
            r9.append(r10);	 Catch:{ Exception -> 0x02bb }
            r10 = r1.audioRecorder;	 Catch:{ Exception -> 0x02bb }
            r10 = r10.getChannelCount();	 Catch:{ Exception -> 0x02bb }
            r9.append(r10);	 Catch:{ Exception -> 0x02bb }
            r10 = " sample rate = ";	 Catch:{ Exception -> 0x02bb }
            r9.append(r10);	 Catch:{ Exception -> 0x02bb }
            r10 = r1.audioRecorder;	 Catch:{ Exception -> 0x02bb }
            r10 = r10.getSampleRate();	 Catch:{ Exception -> 0x02bb }
            r9.append(r10);	 Catch:{ Exception -> 0x02bb }
            r10 = " bufferSize = ";	 Catch:{ Exception -> 0x02bb }
            r9.append(r10);	 Catch:{ Exception -> 0x02bb }
            r9.append(r7);	 Catch:{ Exception -> 0x02bb }
            r7 = r9.toString();	 Catch:{ Exception -> 0x02bb }
            org.telegram.messenger.FileLog.m27d(r7);	 Catch:{ Exception -> 0x02bb }
            r7 = new java.lang.Thread;	 Catch:{ Exception -> 0x02bb }
            r9 = r1.recorderRunnable;	 Catch:{ Exception -> 0x02bb }
            r7.<init>(r9);	 Catch:{ Exception -> 0x02bb }
            r9 = 10;	 Catch:{ Exception -> 0x02bb }
            r7.setPriority(r9);	 Catch:{ Exception -> 0x02bb }
            r7.start();	 Catch:{ Exception -> 0x02bb }
            r7 = new android.media.MediaCodec$BufferInfo;	 Catch:{ Exception -> 0x02bb }
            r7.<init>();	 Catch:{ Exception -> 0x02bb }
            r1.audioBufferInfo = r7;	 Catch:{ Exception -> 0x02bb }
            r7 = new android.media.MediaCodec$BufferInfo;	 Catch:{ Exception -> 0x02bb }
            r7.<init>();	 Catch:{ Exception -> 0x02bb }
            r1.videoBufferInfo = r7;	 Catch:{ Exception -> 0x02bb }
            r7 = new android.media.MediaFormat;	 Catch:{ Exception -> 0x02bb }
            r7.<init>();	 Catch:{ Exception -> 0x02bb }
            r9 = "mime";	 Catch:{ Exception -> 0x02bb }
            r7.setString(r9, r3);	 Catch:{ Exception -> 0x02bb }
            r9 = "aac-profile";	 Catch:{ Exception -> 0x02bb }
            r7.setInteger(r9, r6);	 Catch:{ Exception -> 0x02bb }
            r9 = "sample-rate";	 Catch:{ Exception -> 0x02bb }
            r7.setInteger(r9, r5);	 Catch:{ Exception -> 0x02bb }
            r5 = "channel-count";	 Catch:{ Exception -> 0x02bb }
            r7.setInteger(r5, r8);	 Catch:{ Exception -> 0x02bb }
            r5 = 32000; // 0x7d00 float:4.4842E-41 double:1.581E-319;	 Catch:{ Exception -> 0x02bb }
            r7.setInteger(r2, r5);	 Catch:{ Exception -> 0x02bb }
            r5 = "max-input-size";	 Catch:{ Exception -> 0x02bb }
            r9 = 20480; // 0x5000 float:2.8699E-41 double:1.01185E-319;	 Catch:{ Exception -> 0x02bb }
            r7.setInteger(r5, r9);	 Catch:{ Exception -> 0x02bb }
            r3 = android.media.MediaCodec.createEncoderByType(r3);	 Catch:{ Exception -> 0x02bb }
            r1.audioEncoder = r3;	 Catch:{ Exception -> 0x02bb }
            r3 = r1.audioEncoder;	 Catch:{ Exception -> 0x02bb }
            r3.configure(r7, r4, r4, r8);	 Catch:{ Exception -> 0x02bb }
            r3 = r1.audioEncoder;	 Catch:{ Exception -> 0x02bb }
            r3.start();	 Catch:{ Exception -> 0x02bb }
            r3 = android.media.MediaCodec.createEncoderByType(r0);	 Catch:{ Exception -> 0x02bb }
            r1.videoEncoder = r3;	 Catch:{ Exception -> 0x02bb }
            r3 = r1.videoWidth;	 Catch:{ Exception -> 0x02bb }
            r5 = r1.videoHeight;	 Catch:{ Exception -> 0x02bb }
            r0 = android.media.MediaFormat.createVideoFormat(r0, r3, r5);	 Catch:{ Exception -> 0x02bb }
            r3 = "color-format";	 Catch:{ Exception -> 0x02bb }
            r5 = 2130708361; // 0x7f000789 float:1.701803E38 double:1.0527098025E-314;	 Catch:{ Exception -> 0x02bb }
            r0.setInteger(r3, r5);	 Catch:{ Exception -> 0x02bb }
            r3 = r1.videoBitrate;	 Catch:{ Exception -> 0x02bb }
            r0.setInteger(r2, r3);	 Catch:{ Exception -> 0x02bb }
            r2 = "frame-rate";	 Catch:{ Exception -> 0x02bb }
            r3 = 30;	 Catch:{ Exception -> 0x02bb }
            r0.setInteger(r2, r3);	 Catch:{ Exception -> 0x02bb }
            r2 = "i-frame-interval";	 Catch:{ Exception -> 0x02bb }
            r0.setInteger(r2, r8);	 Catch:{ Exception -> 0x02bb }
            r2 = r1.videoEncoder;	 Catch:{ Exception -> 0x02bb }
            r2.configure(r0, r4, r4, r8);	 Catch:{ Exception -> 0x02bb }
            r0 = r1.videoEncoder;	 Catch:{ Exception -> 0x02bb }
            r0 = r0.createInputSurface();	 Catch:{ Exception -> 0x02bb }
            r1.surface = r0;	 Catch:{ Exception -> 0x02bb }
            r0 = r1.videoEncoder;	 Catch:{ Exception -> 0x02bb }
            r0.start();	 Catch:{ Exception -> 0x02bb }
            r0 = new org.telegram.messenger.video.Mp4Movie;	 Catch:{ Exception -> 0x02bb }
            r0.<init>();	 Catch:{ Exception -> 0x02bb }
            r2 = r1.videoFile;	 Catch:{ Exception -> 0x02bb }
            r0.setCacheFile(r2);	 Catch:{ Exception -> 0x02bb }
            r2 = 0;	 Catch:{ Exception -> 0x02bb }
            r0.setRotation(r2);	 Catch:{ Exception -> 0x02bb }
            r2 = r1.videoWidth;	 Catch:{ Exception -> 0x02bb }
            r3 = r1.videoHeight;	 Catch:{ Exception -> 0x02bb }
            r0.setSize(r2, r3);	 Catch:{ Exception -> 0x02bb }
            r2 = new org.telegram.messenger.video.MP4Builder;	 Catch:{ Exception -> 0x02bb }
            r2.<init>();	 Catch:{ Exception -> 0x02bb }
            r3 = org.telegram.p004ui.Components.InstantCameraView.this;	 Catch:{ Exception -> 0x02bb }
            r3 = r3.isSecretChat;	 Catch:{ Exception -> 0x02bb }
            r0 = r2.createMovie(r0, r3);	 Catch:{ Exception -> 0x02bb }
            r1.mediaMuxer = r0;	 Catch:{ Exception -> 0x02bb }
            r0 = new org.telegram.ui.Components.-$$Lambda$InstantCameraView$VideoRecorder$8ZN8xq5EwIyFVJLsetA_qjZZbSQ;	 Catch:{ Exception -> 0x02bb }
            r0.<init>(r1);	 Catch:{ Exception -> 0x02bb }
            org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);	 Catch:{ Exception -> 0x02bb }
            r0 = r1.eglDisplay;
            r2 = android.opengl.EGL14.EGL_NO_DISPLAY;
            if (r0 != r2) goto L_0x02b3;
            r0 = 0;
            r2 = android.opengl.EGL14.eglGetDisplay(r0);
            r1.eglDisplay = r2;
            r2 = r1.eglDisplay;
            r3 = android.opengl.EGL14.EGL_NO_DISPLAY;
            if (r2 == r3) goto L_0x02ab;
            r3 = new int[r6];
            r2 = android.opengl.EGL14.eglInitialize(r2, r3, r0, r3, r8);
            if (r2 == 0) goto L_0x02a1;
            r0 = r1.eglContext;
            r2 = android.opengl.EGL14.EGL_NO_CONTEXT;
            if (r0 != r2) goto L_0x01a3;
            r0 = 13;
            r0 = new int[r0];
            r0 = {12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12610, 1, 12344};
            r2 = new android.opengl.EGLConfig[r8];
            r3 = new int[r8];
            r4 = r1.eglDisplay;
            r19 = 0;
            r21 = 0;
            r5 = r2.length;
            r24 = 0;
            r17 = r4;
            r18 = r0;
            r20 = r2;
            r22 = r5;
            r23 = r3;
            r0 = android.opengl.EGL14.eglChooseConfig(r17, r18, r19, r20, r21, r22, r23, r24);
            if (r0 == 0) goto L_0x019b;
            r0 = 3;
            r0 = new int[r0];
            r0 = {12440, 2, 12344};
            r3 = r1.eglDisplay;
            r4 = 0;
            r5 = r2[r4];
            r6 = r1.sharedEglContext;
            r0 = android.opengl.EGL14.eglCreateContext(r3, r5, r6, r0, r4);
            r1.eglContext = r0;
            r0 = r2[r4];
            r1.eglConfig = r0;
            goto L_0x01a3;
            r0 = new java.lang.RuntimeException;
            r2 = "Unable to find a suitable EGLConfig";
            r0.<init>(r2);
            throw r0;
            r0 = new int[r8];
            r2 = r1.eglDisplay;
            r3 = r1.eglContext;
            r4 = 12440; // 0x3098 float:1.7432E-41 double:6.146E-320;
            r5 = 0;
            android.opengl.EGL14.eglQueryContext(r2, r3, r4, r0, r5);
            r0 = r1.eglSurface;
            r2 = android.opengl.EGL14.EGL_NO_SURFACE;
            if (r0 != r2) goto L_0x0299;
            r0 = new int[r8];
            r2 = 12344; // 0x3038 float:1.7298E-41 double:6.0987E-320;
            r0[r5] = r2;
            r2 = r1.eglDisplay;
            r3 = r1.eglConfig;
            r4 = r1.surface;
            r0 = android.opengl.EGL14.eglCreateWindowSurface(r2, r3, r4, r0, r5);
            r1.eglSurface = r0;
            r0 = r1.eglSurface;
            if (r0 == 0) goto L_0x0291;
            r2 = r1.eglDisplay;
            r3 = r1.eglContext;
            r0 = android.opengl.EGL14.eglMakeCurrent(r2, r0, r0, r3);
            if (r0 != 0) goto L_0x01fd;
            r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r0 == 0) goto L_0x01f5;
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r2 = "eglMakeCurrent failed ";
            r0.append(r2);
            r2 = android.opengl.EGL14.eglGetError();
            r2 = android.opengl.GLUtils.getEGLErrorString(r2);
            r0.append(r2);
            r0 = r0.toString();
            org.telegram.messenger.FileLog.m28e(r0);
            r0 = new java.lang.RuntimeException;
            r2 = "eglMakeCurrent failed";
            r0.<init>(r2);
            throw r0;
            r0 = 770; // 0x302 float:1.079E-42 double:3.804E-321;
            r2 = 771; // 0x303 float:1.08E-42 double:3.81E-321;
            android.opengl.GLES20.glBlendFunc(r0, r2);
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r2 = 35633; // 0x8b31 float:4.9932E-41 double:1.7605E-319;
            r3 = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
            r0 = r0.loadShader(r2, r3);
            r2 = org.telegram.p004ui.Components.InstantCameraView.this;
            r3 = 35632; // 0x8b30 float:4.9931E-41 double:1.76045E-319;
            r4 = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float scaleX;\nuniform float scaleY;\nuniform float alpha;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec2 coord = vec2((vTextureCoord.x - 0.5) * scaleX, (vTextureCoord.y - 0.5) * scaleY);\n   float coef = ceil(clamp(0.2601 - dot(coord, coord), 0.0, 1.0));\n   vec3 color = texture2D(sTexture, vTextureCoord).rgb * coef + (1.0 - step(0.001, coef));\n   gl_FragColor = vec4(color * alpha, alpha);\n}\n";
            r2 = r2.loadShader(r3, r4);
            if (r0 == 0) goto L_0x0290;
            if (r2 == 0) goto L_0x0290;
            r3 = android.opengl.GLES20.glCreateProgram();
            r1.drawProgram = r3;
            r3 = r1.drawProgram;
            android.opengl.GLES20.glAttachShader(r3, r0);
            r0 = r1.drawProgram;
            android.opengl.GLES20.glAttachShader(r0, r2);
            r0 = r1.drawProgram;
            android.opengl.GLES20.glLinkProgram(r0);
            r0 = new int[r8];
            r2 = r1.drawProgram;
            r3 = 35714; // 0x8b82 float:5.0046E-41 double:1.7645E-319;
            r4 = 0;
            android.opengl.GLES20.glGetProgramiv(r2, r3, r0, r4);
            r0 = r0[r4];
            if (r0 != 0) goto L_0x024a;
            r0 = r1.drawProgram;
            android.opengl.GLES20.glDeleteProgram(r0);
            r1.drawProgram = r4;
            goto L_0x0290;
            r0 = r1.drawProgram;
            r2 = "aPosition";
            r0 = android.opengl.GLES20.glGetAttribLocation(r0, r2);
            r1.positionHandle = r0;
            r0 = r1.drawProgram;
            r2 = "aTextureCoord";
            r0 = android.opengl.GLES20.glGetAttribLocation(r0, r2);
            r1.textureHandle = r0;
            r0 = r1.drawProgram;
            r2 = "scaleX";
            r0 = android.opengl.GLES20.glGetUniformLocation(r0, r2);
            r1.scaleXHandle = r0;
            r0 = r1.drawProgram;
            r2 = "scaleY";
            r0 = android.opengl.GLES20.glGetUniformLocation(r0, r2);
            r1.scaleYHandle = r0;
            r0 = r1.drawProgram;
            r2 = "alpha";
            r0 = android.opengl.GLES20.glGetUniformLocation(r0, r2);
            r1.alphaHandle = r0;
            r0 = r1.drawProgram;
            r2 = "uMVPMatrix";
            r0 = android.opengl.GLES20.glGetUniformLocation(r0, r2);
            r1.vertexMatrixHandle = r0;
            r0 = r1.drawProgram;
            r2 = "uSTMatrix";
            r0 = android.opengl.GLES20.glGetUniformLocation(r0, r2);
            r1.textureMatrixHandle = r0;
            return;
            r0 = new java.lang.RuntimeException;
            r2 = "surface was null";
            r0.<init>(r2);
            throw r0;
            r0 = new java.lang.IllegalStateException;
            r2 = "surface already created";
            r0.<init>(r2);
            throw r0;
            r1.eglDisplay = r4;
            r0 = new java.lang.RuntimeException;
            r2 = "unable to initialize EGL14";
            r0.<init>(r2);
            throw r0;
            r0 = new java.lang.RuntimeException;
            r2 = "unable to get EGL14 display";
            r0.<init>(r2);
            throw r0;
            r0 = new java.lang.RuntimeException;
            r2 = "EGL already set up";
            r0.<init>(r2);
            throw r0;
            r0 = move-exception;
            r2 = new java.lang.RuntimeException;
            r2.<init>(r0);
            throw r2;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.InstantCameraView$VideoRecorder.prepareEncoder():void");
        }

        /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:32:0x0062 in {2, 7, 8, 9, 16, 19, 23, 27, 31} preds:[]
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
            	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
            	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
            	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
            	at jadx.core.ProcessClass.process(ProcessClass.java:32)
            	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
            	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
            	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
            	at jadx.core.ProcessClass.process(ProcessClass.java:37)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
            */
        public void startRecording(java.io.File r3, android.opengl.EGLContext r4) {
            /*
            r2 = this;
            r0 = android.os.Build.DEVICE;
            if (r0 != 0) goto L_0x0006;
            r0 = "";
            r1 = "zeroflte";
            r1 = r0.startsWith(r1);
            if (r1 != 0) goto L_0x001d;
            r1 = "zenlte";
            r0 = r0.startsWith(r1);
            if (r0 == 0) goto L_0x0017;
            goto L_0x001d;
            r0 = 240; // 0xf0 float:3.36E-43 double:1.186E-321;
            r1 = 400000; // 0x61a80 float:5.6052E-40 double:1.976263E-318;
            goto L_0x0022;
            r0 = 320; // 0x140 float:4.48E-43 double:1.58E-321;
            r1 = 600000; // 0x927c0 float:8.40779E-40 double:2.964394E-318;
            r2.videoFile = r3;
            r2.videoWidth = r0;
            r2.videoHeight = r0;
            r2.videoBitrate = r1;
            r2.sharedEglContext = r4;
            r3 = r2.sync;
            monitor-enter(r3);
            r4 = r2.running;	 Catch:{ all -> 0x005f }
            if (r4 == 0) goto L_0x0035;	 Catch:{ all -> 0x005f }
            monitor-exit(r3);	 Catch:{ all -> 0x005f }
            return;	 Catch:{ all -> 0x005f }
            r4 = 1;	 Catch:{ all -> 0x005f }
            r2.running = r4;	 Catch:{ all -> 0x005f }
            r4 = new java.lang.Thread;	 Catch:{ all -> 0x005f }
            r0 = "TextureMovieEncoder";	 Catch:{ all -> 0x005f }
            r4.<init>(r2, r0);	 Catch:{ all -> 0x005f }
            r0 = 10;	 Catch:{ all -> 0x005f }
            r4.setPriority(r0);	 Catch:{ all -> 0x005f }
            r4.start();	 Catch:{ all -> 0x005f }
            r4 = r2.ready;	 Catch:{ all -> 0x005f }
            if (r4 != 0) goto L_0x0051;
            r4 = r2.sync;	 Catch:{ InterruptedException -> 0x0047 }
            r4.wait();	 Catch:{ InterruptedException -> 0x0047 }
            goto L_0x0047;
            monitor-exit(r3);	 Catch:{ all -> 0x005f }
            r3 = r2.handler;
            r4 = r2.handler;
            r0 = 0;
            r4 = r4.obtainMessage(r0);
            r3.sendMessage(r4);
            return;
            r4 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x005f }
            throw r4;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.InstantCameraView$VideoRecorder.startRecording(java.io.File, android.opengl.EGLContext):void");
        }

        private VideoRecorder() {
            this.videoConvertFirstWrite = true;
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            this.buffersToWrite = new ArrayList();
            this.videoTrackIndex = -5;
            this.audioTrackIndex = -5;
            this.audioStartTime = -1;
            this.currentTimestamp = 0;
            this.lastTimestamp = -1;
            this.sync = new Object();
            this.videoFirst = -1;
            this.audioFirst = -1;
            this.lastCameraId = Integer.valueOf(0);
            this.buffers = new ArrayBlockingQueue(10);
            this.recorderRunnable = new C28671();
        }

        /* synthetic */ VideoRecorder(InstantCameraView instantCameraView, C28581 c28581) {
            this();
        }

        public void stopRecording(int i) {
            this.handler.sendMessage(this.handler.obtainMessage(1, i, 0));
        }

        /* JADX WARNING: Missing block: B:8:0x000a, code skipped:
            r0 = r5.getTimestamp();
     */
        /* JADX WARNING: Missing block: B:9:0x0012, code skipped:
            if (r0 != 0) goto L_0x0029;
     */
        /* JADX WARNING: Missing block: B:10:0x0014, code skipped:
            r4.zeroTimeStamps++;
     */
        /* JADX WARNING: Missing block: B:11:0x001c, code skipped:
            if (r4.zeroTimeStamps <= 1) goto L_0x0028;
     */
        /* JADX WARNING: Missing block: B:13:0x0020, code skipped:
            if (org.telegram.messenger.BuildVars.LOGS_ENABLED == false) goto L_0x002d;
     */
        /* JADX WARNING: Missing block: B:14:0x0022, code skipped:
            org.telegram.messenger.FileLog.m27d("fix timestamp enabled");
     */
        /* JADX WARNING: Missing block: B:15:0x0028, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:16:0x0029, code skipped:
            r4.zeroTimeStamps = 0;
            r7 = r0;
     */
        /* JADX WARNING: Missing block: B:17:0x002d, code skipped:
            r4.handler.sendMessage(r4.handler.obtainMessage(2, (int) (r7 >> 32), (int) r7, r6));
     */
        /* JADX WARNING: Missing block: B:18:0x003f, code skipped:
            return;
     */
        public void frameAvailable(android.graphics.SurfaceTexture r5, java.lang.Integer r6, long r7) {
            /*
            r4 = this;
            r0 = r4.sync;
            monitor-enter(r0);
            r1 = r4.ready;	 Catch:{ all -> 0x0040 }
            if (r1 != 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r0);	 Catch:{ all -> 0x0040 }
            return;
        L_0x0009:
            monitor-exit(r0);	 Catch:{ all -> 0x0040 }
            r0 = r5.getTimestamp();
            r2 = 0;
            r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
            if (r5 != 0) goto L_0x0029;
        L_0x0014:
            r5 = r4.zeroTimeStamps;
            r0 = 1;
            r5 = r5 + r0;
            r4.zeroTimeStamps = r5;
            r5 = r4.zeroTimeStamps;
            if (r5 <= r0) goto L_0x0028;
        L_0x001e:
            r5 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r5 == 0) goto L_0x002d;
        L_0x0022:
            r5 = "fix timestamp enabled";
            org.telegram.messenger.FileLog.m27d(r5);
            goto L_0x002d;
        L_0x0028:
            return;
        L_0x0029:
            r5 = 0;
            r4.zeroTimeStamps = r5;
            r7 = r0;
        L_0x002d:
            r5 = r4.handler;
            r0 = r4.handler;
            r1 = 2;
            r2 = 32;
            r2 = r7 >> r2;
            r3 = (int) r2;
            r8 = (int) r7;
            r6 = r0.obtainMessage(r1, r3, r8, r6);
            r5.sendMessage(r6);
            return;
        L_0x0040:
            r5 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0040 }
            throw r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.InstantCameraView$VideoRecorder.frameAvailable(android.graphics.SurfaceTexture, java.lang.Integer, long):void");
        }

        public void run() {
            Looper.prepare();
            synchronized (this.sync) {
                this.handler = new EncoderHandler(this);
                this.ready = true;
                this.sync.notify();
            }
            Looper.loop();
            synchronized (this.sync) {
                this.ready = false;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:100:0x00fb A:{SYNTHETIC, EDGE_INSN: B:100:0x00fb->B:39:0x00fb ?: BREAK  } */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x00c8  */
        private void handleAudioFrameAvailable(org.telegram.p004ui.Components.InstantCameraView.AudioBufferInfo r17) {
            /*
            r16 = this;
            r1 = r16;
            r0 = r1.audioStopedByTime;
            if (r0 == 0) goto L_0x0007;
        L_0x0006:
            return;
        L_0x0007:
            r0 = r1.buffersToWrite;
            r2 = r17;
            r0.add(r2);
            r3 = r1.audioFirst;
            r5 = -1;
            r7 = 0;
            r8 = 1;
            r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r0 != 0) goto L_0x00fb;
        L_0x0018:
            r3 = r1.videoFirst;
            r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r0 != 0) goto L_0x0028;
        L_0x001e:
            r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r0 == 0) goto L_0x0027;
        L_0x0022:
            r0 = "video record not yet started";
            org.telegram.messenger.FileLog.m27d(r0);
        L_0x0027:
            return;
        L_0x0028:
            r0 = 0;
        L_0x0029:
            r3 = r2.results;
            if (r0 >= r3) goto L_0x00c5;
        L_0x002d:
            if (r0 != 0) goto L_0x0069;
        L_0x002f:
            r3 = r1.videoFirst;
            r9 = r2.offset;
            r10 = r9[r0];
            r3 = r3 - r10;
            r3 = java.lang.Math.abs(r3);
            r9 = 10000000; // 0x989680 float:1.4012985E-38 double:4.9406565E-317;
            r11 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1));
            if (r11 <= 0) goto L_0x0069;
        L_0x0041:
            r3 = r1.videoFirst;
            r9 = r2.offset;
            r10 = r9[r0];
            r3 = r3 - r10;
            r1.desyncTime = r3;
            r3 = r9[r0];
            r1.audioFirst = r3;
            r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r0 == 0) goto L_0x009d;
        L_0x0052:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r3 = "detected desync between audio and video ";
            r0.append(r3);
            r3 = r1.desyncTime;
            r0.append(r3);
            r0 = r0.toString();
            org.telegram.messenger.FileLog.m27d(r0);
            goto L_0x009d;
        L_0x0069:
            r3 = r2.offset;
            r9 = r3[r0];
            r11 = r1.videoFirst;
            r4 = " timestamp = ";
            r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
            if (r13 < 0) goto L_0x009f;
        L_0x0075:
            r2.lastWroteBuffer = r0;
            r9 = r3[r0];
            r1.audioFirst = r9;
            r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r3 == 0) goto L_0x009d;
        L_0x007f:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r9 = "found first audio frame at ";
            r3.append(r9);
            r3.append(r0);
            r3.append(r4);
            r4 = r2.offset;
            r9 = r4[r0];
            r3.append(r9);
            r0 = r3.toString();
            org.telegram.messenger.FileLog.m27d(r0);
        L_0x009d:
            r0 = 1;
            goto L_0x00c6;
        L_0x009f:
            r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r3 == 0) goto L_0x00c1;
        L_0x00a3:
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r9 = "ignore first audio frame at ";
            r3.append(r9);
            r3.append(r0);
            r3.append(r4);
            r4 = r2.offset;
            r9 = r4[r0];
            r3.append(r9);
            r3 = r3.toString();
            org.telegram.messenger.FileLog.m27d(r3);
        L_0x00c1:
            r0 = r0 + 1;
            goto L_0x0029;
        L_0x00c5:
            r0 = 0;
        L_0x00c6:
            if (r0 != 0) goto L_0x00fb;
        L_0x00c8:
            r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r0 == 0) goto L_0x00e2;
        L_0x00cc:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r3 = "first audio frame not found, removing buffers ";
            r0.append(r3);
            r3 = r2.results;
            r0.append(r3);
            r0 = r0.toString();
            org.telegram.messenger.FileLog.m27d(r0);
        L_0x00e2:
            r0 = r1.buffersToWrite;
            r0.remove(r2);
            r0 = r1.buffersToWrite;
            r0 = r0.isEmpty();
            if (r0 != 0) goto L_0x00fa;
        L_0x00ef:
            r0 = r1.buffersToWrite;
            r0 = r0.get(r7);
            r2 = r0;
            r2 = (org.telegram.p004ui.Components.InstantCameraView.AudioBufferInfo) r2;
            goto L_0x0028;
        L_0x00fa:
            return;
        L_0x00fb:
            r3 = r1.audioStartTime;
            r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r0 != 0) goto L_0x0109;
        L_0x0101:
            r0 = r2.offset;
            r3 = r2.lastWroteBuffer;
            r3 = r0[r3];
            r1.audioStartTime = r3;
        L_0x0109:
            r0 = r1.buffersToWrite;
            r0 = r0.size();
            if (r0 <= r8) goto L_0x011a;
        L_0x0111:
            r0 = r1.buffersToWrite;
            r0 = r0.get(r7);
            r2 = r0;
            r2 = (org.telegram.p004ui.Components.InstantCameraView.AudioBufferInfo) r2;
        L_0x011a:
            r1.drainEncoder(r7);	 Catch:{ Exception -> 0x011e }
            goto L_0x0123;
        L_0x011e:
            r0 = move-exception;
            r3 = r0;
            org.telegram.messenger.FileLog.m30e(r3);
        L_0x0123:
            r0 = 0;
        L_0x0124:
            if (r2 == 0) goto L_0x0215;
        L_0x0126:
            r3 = r1.audioEncoder;	 Catch:{ Throwable -> 0x0211 }
            r4 = 0;
            r10 = r3.dequeueInputBuffer(r4);	 Catch:{ Throwable -> 0x0211 }
            if (r10 < 0) goto L_0x020b;
        L_0x0130:
            r3 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0211 }
            r6 = 21;
            if (r3 < r6) goto L_0x013d;
        L_0x0136:
            r3 = r1.audioEncoder;	 Catch:{ Throwable -> 0x0211 }
            r3 = r3.getInputBuffer(r10);	 Catch:{ Throwable -> 0x0211 }
            goto L_0x0148;
        L_0x013d:
            r3 = r1.audioEncoder;	 Catch:{ Throwable -> 0x0211 }
            r3 = r3.getInputBuffers();	 Catch:{ Throwable -> 0x0211 }
            r3 = r3[r10];	 Catch:{ Throwable -> 0x0211 }
            r3.clear();	 Catch:{ Throwable -> 0x0211 }
        L_0x0148:
            r6 = r2.offset;	 Catch:{ Throwable -> 0x0211 }
            r9 = r2.lastWroteBuffer;	 Catch:{ Throwable -> 0x0211 }
            r11 = r6[r9];	 Catch:{ Throwable -> 0x0211 }
            r6 = r2.lastWroteBuffer;	 Catch:{ Throwable -> 0x0211 }
        L_0x0150:
            r9 = r2.results;	 Catch:{ Throwable -> 0x0211 }
            r13 = 0;
            if (r6 > r9) goto L_0x01e9;
        L_0x0155:
            r9 = r2.results;	 Catch:{ Throwable -> 0x0211 }
            if (r6 >= r9) goto L_0x01b4;
        L_0x0159:
            r9 = r1.running;	 Catch:{ Throwable -> 0x0211 }
            if (r9 != 0) goto L_0x019b;
        L_0x015d:
            r9 = r2.offset;	 Catch:{ Throwable -> 0x0211 }
            r14 = r9[r6];	 Catch:{ Throwable -> 0x0211 }
            r4 = r1.videoLast;	 Catch:{ Throwable -> 0x0211 }
            r7 = r1.desyncTime;	 Catch:{ Throwable -> 0x0211 }
            r4 = r4 - r7;
            r7 = (r14 > r4 ? 1 : (r14 == r4 ? 0 : -1));
            if (r7 < 0) goto L_0x019b;
        L_0x016a:
            r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x0211 }
            if (r0 == 0) goto L_0x0190;
        L_0x016e:
            r0 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0211 }
            r0.<init>();	 Catch:{ Throwable -> 0x0211 }
            r4 = "stop audio encoding because of stoped video recording at ";
            r0.append(r4);	 Catch:{ Throwable -> 0x0211 }
            r2 = r2.offset;	 Catch:{ Throwable -> 0x0211 }
            r4 = r2[r6];	 Catch:{ Throwable -> 0x0211 }
            r0.append(r4);	 Catch:{ Throwable -> 0x0211 }
            r2 = " last video ";
            r0.append(r2);	 Catch:{ Throwable -> 0x0211 }
            r4 = r1.videoLast;	 Catch:{ Throwable -> 0x0211 }
            r0.append(r4);	 Catch:{ Throwable -> 0x0211 }
            r0 = r0.toString();	 Catch:{ Throwable -> 0x0211 }
            org.telegram.messenger.FileLog.m27d(r0);	 Catch:{ Throwable -> 0x0211 }
        L_0x0190:
            r2 = 1;
            r1.audioStopedByTime = r2;	 Catch:{ Throwable -> 0x0211 }
            r0 = r1.buffersToWrite;	 Catch:{ Throwable -> 0x0211 }
            r0.clear();	 Catch:{ Throwable -> 0x0211 }
            r2 = r13;
            r0 = 1;
            goto L_0x01e9;
        L_0x019b:
            r4 = r3.remaining();	 Catch:{ Throwable -> 0x0211 }
            r5 = r2.read;	 Catch:{ Throwable -> 0x0211 }
            r5 = r5[r6];	 Catch:{ Throwable -> 0x0211 }
            if (r4 >= r5) goto L_0x01a9;
        L_0x01a5:
            r2.lastWroteBuffer = r6;	 Catch:{ Throwable -> 0x0211 }
            r2 = r13;
            goto L_0x01e9;
        L_0x01a9:
            r4 = r2.buffer;	 Catch:{ Throwable -> 0x0211 }
            r5 = r6 * 2048;
            r7 = r2.read;	 Catch:{ Throwable -> 0x0211 }
            r7 = r7[r6];	 Catch:{ Throwable -> 0x0211 }
            r3.put(r4, r5, r7);	 Catch:{ Throwable -> 0x0211 }
        L_0x01b4:
            r4 = r2.results;	 Catch:{ Throwable -> 0x0211 }
            r5 = 1;
            r4 = r4 - r5;
            if (r6 < r4) goto L_0x01e0;
        L_0x01ba:
            r4 = r1.buffersToWrite;	 Catch:{ Throwable -> 0x0211 }
            r4.remove(r2);	 Catch:{ Throwable -> 0x0211 }
            r4 = r1.running;	 Catch:{ Throwable -> 0x0211 }
            if (r4 == 0) goto L_0x01c8;
        L_0x01c3:
            r4 = r1.buffers;	 Catch:{ Throwable -> 0x0211 }
            r4.put(r2);	 Catch:{ Throwable -> 0x0211 }
        L_0x01c8:
            r4 = r1.buffersToWrite;	 Catch:{ Throwable -> 0x0211 }
            r4 = r4.isEmpty();	 Catch:{ Throwable -> 0x0211 }
            if (r4 != 0) goto L_0x01da;
        L_0x01d0:
            r2 = r1.buffersToWrite;	 Catch:{ Throwable -> 0x0211 }
            r4 = 0;
            r2 = r2.get(r4);	 Catch:{ Throwable -> 0x0211 }
            r2 = (org.telegram.p004ui.Components.InstantCameraView.AudioBufferInfo) r2;	 Catch:{ Throwable -> 0x0211 }
            goto L_0x01e1;
        L_0x01da:
            r4 = 0;
            r8 = r2.last;	 Catch:{ Throwable -> 0x0211 }
            r0 = r8;
            r2 = r13;
            goto L_0x01eb;
        L_0x01e0:
            r4 = 0;
        L_0x01e1:
            r6 = r6 + 1;
            r4 = 0;
            r7 = 0;
            r8 = 1;
            goto L_0x0150;
        L_0x01e9:
            r4 = 0;
            r5 = 1;
        L_0x01eb:
            r9 = r1.audioEncoder;	 Catch:{ Throwable -> 0x0211 }
            r6 = 0;
            r3 = r3.position();	 Catch:{ Throwable -> 0x0211 }
            r7 = 0;
            r13 = (r11 > r7 ? 1 : (r11 == r7 ? 0 : -1));
            if (r13 != 0) goto L_0x01fa;
        L_0x01f8:
            r13 = r7;
            goto L_0x01ff;
        L_0x01fa:
            r7 = r1.audioStartTime;	 Catch:{ Throwable -> 0x0211 }
            r7 = r11 - r7;
            goto L_0x01f8;
        L_0x01ff:
            if (r0 == 0) goto L_0x0204;
        L_0x0201:
            r7 = 4;
            r15 = 4;
            goto L_0x0205;
        L_0x0204:
            r15 = 0;
        L_0x0205:
            r11 = r6;
            r12 = r3;
            r9.queueInputBuffer(r10, r11, r12, r13, r15);	 Catch:{ Throwable -> 0x0211 }
            goto L_0x020d;
        L_0x020b:
            r4 = 0;
            r5 = 1;
        L_0x020d:
            r7 = 0;
            r8 = 1;
            goto L_0x0124;
        L_0x0211:
            r0 = move-exception;
            org.telegram.messenger.FileLog.m30e(r0);
        L_0x0215:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.InstantCameraView$VideoRecorder.handleAudioFrameAvailable(org.telegram.ui.Components.InstantCameraView$AudioBufferInfo):void");
        }

        /* JADX WARNING: Removed duplicated region for block: B:17:0x0050  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x006c  */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x00f6  */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x01c7  */
        private void handleVideoFrameAvailable(long r19, java.lang.Integer r21) {
            /*
            r18 = this;
            r1 = r18;
            r2 = r19;
            r4 = r21;
            r5 = 0;
            r1.drainEncoder(r5);	 Catch:{ Exception -> 0x000b }
            goto L_0x0010;
        L_0x000b:
            r0 = move-exception;
            r6 = r0;
            org.telegram.messenger.FileLog.m30e(r6);
        L_0x0010:
            r0 = r1.lastCameraId;
            r0 = r0.equals(r4);
            r6 = -1;
            if (r0 != 0) goto L_0x001e;
        L_0x001a:
            r1.lastTimestamp = r6;
            r1.lastCameraId = r4;
        L_0x001e:
            r8 = r1.lastTimestamp;
            r10 = 0;
            r0 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
            if (r0 != 0) goto L_0x0040;
        L_0x0026:
            r1.lastTimestamp = r2;
            r8 = r1.currentTimestamp;
            r0 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
            if (r0 == 0) goto L_0x0044;
        L_0x002e:
            r8 = java.lang.System.currentTimeMillis();
            r12 = r1.lastCommitedFrameTime;
            r8 = r8 - r12;
            r12 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
            r8 = r8 * r12;
            r16 = r8;
            r8 = r10;
            r10 = r16;
            goto L_0x0045;
        L_0x0040:
            r10 = r2 - r8;
            r1.lastTimestamp = r2;
        L_0x0044:
            r8 = r10;
        L_0x0045:
            r12 = java.lang.System.currentTimeMillis();
            r1.lastCommitedFrameTime = r12;
            r0 = r1.skippedFirst;
            r4 = 1;
            if (r0 != 0) goto L_0x0061;
        L_0x0050:
            r12 = r1.skippedTime;
            r12 = r12 + r10;
            r1.skippedTime = r12;
            r12 = r1.skippedTime;
            r14 = 200000000; // 0xbebc200 float:9.0810606E-32 double:9.8813129E-316;
            r0 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
            if (r0 >= 0) goto L_0x005f;
        L_0x005e:
            return;
        L_0x005f:
            r1.skippedFirst = r4;
        L_0x0061:
            r12 = r1.currentTimestamp;
            r12 = r12 + r10;
            r1.currentTimestamp = r12;
            r10 = r1.videoFirst;
            r0 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1));
            if (r0 != 0) goto L_0x008c;
        L_0x006c:
            r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r6 = r2 / r6;
            r1.videoFirst = r6;
            r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r0 == 0) goto L_0x008c;
        L_0x0076:
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r6 = "first video frame was at ";
            r0.append(r6);
            r6 = r1.videoFirst;
            r0.append(r6);
            r0 = r0.toString();
            org.telegram.messenger.FileLog.m27d(r0);
        L_0x008c:
            r1.videoLast = r2;
            r0 = r1.drawProgram;
            android.opengl.GLES20.glUseProgram(r0);
            r10 = r1.positionHandle;
            r11 = 3;
            r12 = 5126; // 0x1406 float:7.183E-42 double:2.5326E-320;
            r13 = 0;
            r14 = 12;
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r15 = r0.vertexBuffer;
            android.opengl.GLES20.glVertexAttribPointer(r10, r11, r12, r13, r14, r15);
            r0 = r1.positionHandle;
            android.opengl.GLES20.glEnableVertexAttribArray(r0);
            r10 = r1.textureHandle;
            r11 = 2;
            r14 = 8;
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r15 = r0.textureBuffer;
            android.opengl.GLES20.glVertexAttribPointer(r10, r11, r12, r13, r14, r15);
            r0 = r1.textureHandle;
            android.opengl.GLES20.glEnableVertexAttribArray(r0);
            r0 = r1.scaleXHandle;
            r2 = org.telegram.p004ui.Components.InstantCameraView.this;
            r2 = r2.scaleX;
            android.opengl.GLES20.glUniform1f(r0, r2);
            r0 = r1.scaleYHandle;
            r2 = org.telegram.p004ui.Components.InstantCameraView.this;
            r2 = r2.scaleY;
            android.opengl.GLES20.glUniform1f(r0, r2);
            r0 = r1.vertexMatrixHandle;
            r2 = org.telegram.p004ui.Components.InstantCameraView.this;
            r2 = r2.mMVPMatrix;
            android.opengl.GLES20.glUniformMatrix4fv(r0, r4, r5, r2, r5);
            r0 = 33984; // 0x84c0 float:4.7622E-41 double:1.67903E-319;
            android.opengl.GLES20.glActiveTexture(r0);
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.oldCameraTexture;
            r0 = r0[r5];
            r2 = 3042; // 0xbe2 float:4.263E-42 double:1.503E-320;
            r3 = 4;
            r6 = 5;
            r7 = 36197; // 0x8d65 float:5.0723E-41 double:1.78837E-319;
            r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
            if (r0 == 0) goto L_0x011d;
        L_0x00f6:
            r0 = r1.blendEnabled;
            if (r0 != 0) goto L_0x00ff;
        L_0x00fa:
            android.opengl.GLES20.glEnable(r2);
            r1.blendEnabled = r4;
        L_0x00ff:
            r0 = r1.textureMatrixHandle;
            r11 = org.telegram.p004ui.Components.InstantCameraView.this;
            r11 = r11.moldSTMatrix;
            android.opengl.GLES20.glUniformMatrix4fv(r0, r4, r5, r11, r5);
            r0 = r1.alphaHandle;
            android.opengl.GLES20.glUniform1f(r0, r10);
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.oldCameraTexture;
            r0 = r0[r5];
            android.opengl.GLES20.glBindTexture(r7, r0);
            android.opengl.GLES20.glDrawArrays(r6, r5, r3);
        L_0x011d:
            r0 = r1.textureMatrixHandle;
            r11 = org.telegram.p004ui.Components.InstantCameraView.this;
            r11 = r11.mSTMatrix;
            android.opengl.GLES20.glUniformMatrix4fv(r0, r4, r5, r11, r5);
            r0 = r1.alphaHandle;
            r11 = org.telegram.p004ui.Components.InstantCameraView.this;
            r11 = r11.cameraTextureAlpha;
            android.opengl.GLES20.glUniform1f(r0, r11);
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.cameraTexture;
            r0 = r0[r5];
            android.opengl.GLES20.glBindTexture(r7, r0);
            android.opengl.GLES20.glDrawArrays(r6, r5, r3);
            r0 = r1.positionHandle;
            android.opengl.GLES20.glDisableVertexAttribArray(r0);
            r0 = r1.textureHandle;
            android.opengl.GLES20.glDisableVertexAttribArray(r0);
            android.opengl.GLES20.glBindTexture(r7, r5);
            android.opengl.GLES20.glUseProgram(r5);
            r0 = r1.eglDisplay;
            r3 = r1.eglSurface;
            r6 = r1.currentTimestamp;
            android.opengl.EGLExt.eglPresentationTimeANDROID(r0, r3, r6);
            r0 = r1.eglDisplay;
            r3 = r1.eglSurface;
            android.opengl.EGL14.eglSwapBuffers(r0, r3);
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.oldCameraTexture;
            r0 = r0[r5];
            if (r0 == 0) goto L_0x01bf;
        L_0x016b:
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.cameraTextureAlpha;
            r0 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1));
            if (r0 >= 0) goto L_0x01bf;
        L_0x0175:
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r3 = r0.cameraTextureAlpha;
            r6 = (float) r8;
            r7 = 1295957024; // 0x4d3ebc20 float:2.0E8 double:6.40287844E-315;
            r6 = r6 / r7;
            r3 = r3 + r6;
            r0.cameraTextureAlpha = r3;
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.cameraTextureAlpha;
            r0 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1));
            if (r0 <= 0) goto L_0x01d4;
        L_0x018e:
            android.opengl.GLES20.glDisable(r2);
            r1.blendEnabled = r5;
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0.cameraTextureAlpha = r10;
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.oldCameraTexture;
            android.opengl.GLES20.glDeleteTextures(r4, r0, r5);
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.oldCameraTexture;
            r0[r5] = r5;
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.cameraReady;
            if (r0 != 0) goto L_0x01d4;
        L_0x01b1:
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0.cameraReady = r4;
            r0 = new org.telegram.ui.Components.-$$Lambda$InstantCameraView$VideoRecorder$ONc2-IZzZjKNnbhigUCIh3I7vt4;
            r0.<init>(r1);
            org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
            goto L_0x01d4;
        L_0x01bf:
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0 = r0.cameraReady;
            if (r0 != 0) goto L_0x01d4;
        L_0x01c7:
            r0 = org.telegram.p004ui.Components.InstantCameraView.this;
            r0.cameraReady = r4;
            r0 = new org.telegram.ui.Components.-$$Lambda$InstantCameraView$VideoRecorder$cj_4IZt_HllVK7x5iDyMvDcfDnc;
            r0.<init>(r1);
            org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
        L_0x01d4:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Components.InstantCameraView$VideoRecorder.handleVideoFrameAvailable(long, java.lang.Integer):void");
        }

        /* renamed from: lambda$handleVideoFrameAvailable$0$InstantCameraView$VideoRecorder */
        public /* synthetic */ void mo10885x44721c1a() {
            InstantCameraView.this.textureOverlayView.animate().setDuration(120).alpha(0.0f).setInterpolator(new DecelerateInterpolator()).start();
        }

        /* renamed from: lambda$handleVideoFrameAvailable$1$InstantCameraView$VideoRecorder */
        public /* synthetic */ void mo10886xc249d81b() {
            InstantCameraView.this.textureOverlayView.animate().setDuration(120).alpha(0.0f).setInterpolator(new DecelerateInterpolator()).start();
        }

        private void handleStopRecording(int i) {
            if (this.running) {
                this.sendWhenDone = i;
                this.running = false;
                return;
            }
            try {
                drainEncoder(true);
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            MediaCodec mediaCodec = this.videoEncoder;
            if (mediaCodec != null) {
                try {
                    mediaCodec.stop();
                    this.videoEncoder.release();
                    this.videoEncoder = null;
                } catch (Exception e2) {
                    FileLog.m30e(e2);
                }
            }
            mediaCodec = this.audioEncoder;
            if (mediaCodec != null) {
                try {
                    mediaCodec.stop();
                    this.audioEncoder.release();
                    this.audioEncoder = null;
                } catch (Exception e22) {
                    FileLog.m30e(e22);
                }
            }
            MP4Builder mP4Builder = this.mediaMuxer;
            if (mP4Builder != null) {
                try {
                    mP4Builder.finishMovie();
                } catch (Exception e222) {
                    FileLog.m30e(e222);
                }
            }
            if (i != 0) {
                AndroidUtilities.runOnUIThread(new C2599x41a2118d(this, i));
            } else {
                FileLoader.getInstance(InstantCameraView.this.currentAccount).cancelUploadFile(this.videoFile.getAbsolutePath(), false);
                this.videoFile.delete();
            }
            EGL14.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            Surface surface = this.surface;
            if (surface != null) {
                surface.release();
                this.surface = null;
            }
            EGLDisplay eGLDisplay = this.eglDisplay;
            if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
                EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
                EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
                EGL14.eglReleaseThread();
                EGL14.eglTerminate(this.eglDisplay);
            }
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglConfig = null;
            this.handler.exit();
        }

        public /* synthetic */ void lambda$handleStopRecording$2$InstantCameraView$VideoRecorder(int i) {
            InstantCameraView.this.videoEditedInfo = new VideoEditedInfo();
            InstantCameraView.this.videoEditedInfo.roundVideo = true;
            InstantCameraView.this.videoEditedInfo.startTime = -1;
            InstantCameraView.this.videoEditedInfo.endTime = -1;
            InstantCameraView.this.videoEditedInfo.file = InstantCameraView.this.file;
            InstantCameraView.this.videoEditedInfo.encryptedFile = InstantCameraView.this.encryptedFile;
            InstantCameraView.this.videoEditedInfo.key = InstantCameraView.this.key;
            InstantCameraView.this.videoEditedInfo.f423iv = InstantCameraView.this.f633iv;
            InstantCameraView.this.videoEditedInfo.estimatedSize = Math.max(1, InstantCameraView.this.size);
            InstantCameraView.this.videoEditedInfo.framerate = 25;
            VideoEditedInfo access$1000 = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalWidth = 240;
            access$1000.resultWidth = 240;
            access$1000 = InstantCameraView.this.videoEditedInfo;
            InstantCameraView.this.videoEditedInfo.originalHeight = 240;
            access$1000.resultHeight = 240;
            InstantCameraView.this.videoEditedInfo.originalPath = this.videoFile.getAbsolutePath();
            if (i == 1) {
                InstantCameraView.this.baseFragment.sendMedia(new PhotoEntry(0, 0, 0, this.videoFile.getAbsolutePath(), 0, true), InstantCameraView.this.videoEditedInfo);
            } else {
                InstantCameraView.this.videoPlayer = new VideoPlayer();
                InstantCameraView.this.videoPlayer.setDelegate(new C28682());
                InstantCameraView.this.videoPlayer.setTextureView(InstantCameraView.this.textureView);
                InstantCameraView.this.videoPlayer.preparePlayer(Uri.fromFile(this.videoFile), "other");
                InstantCameraView.this.videoPlayer.play();
                InstantCameraView.this.videoPlayer.setMute(true);
                InstantCameraView.this.startProgressTimer();
                AnimatorSet animatorSet = new AnimatorSet();
                r0 = new Animator[3];
                String str = "alpha";
                r0[0] = ObjectAnimator.ofFloat(InstantCameraView.this.switchCameraButton, str, new float[]{0.0f});
                r0[1] = ObjectAnimator.ofInt(InstantCameraView.this.paint, str, new int[]{0});
                r0[2] = ObjectAnimator.ofFloat(InstantCameraView.this.muteImageView, str, new float[]{1.0f});
                animatorSet.playTogether(r0);
                animatorSet.setDuration(180);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.start();
                InstantCameraView.this.videoEditedInfo.estimatedDuration = InstantCameraView.this.duration;
                NotificationCenter.getInstance(InstantCameraView.this.currentAccount).postNotificationName(NotificationCenter.audioDidSent, InstantCameraView.this.videoEditedInfo, this.videoFile.getAbsolutePath());
            }
            didWriteData(this.videoFile, 0, true);
        }

        public /* synthetic */ void lambda$prepareEncoder$3$InstantCameraView$VideoRecorder() {
            if (!InstantCameraView.this.cancelled) {
                try {
                    InstantCameraView.this.performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
                AndroidUtilities.lockOrientation(InstantCameraView.this.baseFragment.getParentActivity());
                InstantCameraView.this.recording = true;
                InstantCameraView.this.recordStartTime = System.currentTimeMillis();
                AndroidUtilities.runOnUIThread(InstantCameraView.this.timerRunnable);
                NotificationCenter.getInstance(InstantCameraView.this.currentAccount).postNotificationName(NotificationCenter.recordStarted, new Object[0]);
            }
        }

        public Surface getInputSurface() {
            return this.surface;
        }

        private void didWriteData(File file, long j, boolean z) {
            long j2 = 0;
            FileLoader instance;
            String file2;
            boolean access$3700;
            if (this.videoConvertFirstWrite) {
                FileLoader.getInstance(InstantCameraView.this.currentAccount).uploadFile(file.toString(), InstantCameraView.this.isSecretChat, false, 1, ConnectionsManager.FileTypeVideo);
                this.videoConvertFirstWrite = false;
                if (z) {
                    instance = FileLoader.getInstance(InstantCameraView.this.currentAccount);
                    file2 = file.toString();
                    access$3700 = InstantCameraView.this.isSecretChat;
                    if (z) {
                        j2 = file.length();
                    }
                    instance.checkUploadNewDataAvailable(file2, access$3700, j, j2);
                    return;
                }
                return;
            }
            instance = FileLoader.getInstance(InstantCameraView.this.currentAccount);
            file2 = file.toString();
            access$3700 = InstantCameraView.this.isSecretChat;
            if (z) {
                j2 = file.length();
            }
            instance.checkUploadNewDataAvailable(file2, access$3700, j, j2);
        }

        public void drainEncoder(boolean z) throws Exception {
            String str;
            String str2;
            int i;
            if (z) {
                this.videoEncoder.signalEndOfInputStream();
            }
            int i2 = 21;
            ByteBuffer[] outputBuffers = VERSION.SDK_INT < 21 ? this.videoEncoder.getOutputBuffers() : null;
            while (true) {
                int dequeueOutputBuffer = this.videoEncoder.dequeueOutputBuffer(this.videoBufferInfo, 10000);
                str = " was null";
                str2 = "encoderOutputBuffer ";
                boolean z2 = true;
                if (dequeueOutputBuffer != -1) {
                    if (dequeueOutputBuffer == -3) {
                        if (VERSION.SDK_INT < i2) {
                            outputBuffers = this.videoEncoder.getOutputBuffers();
                        }
                    } else if (dequeueOutputBuffer == -2) {
                        MediaFormat outputFormat = this.videoEncoder.getOutputFormat();
                        if (this.videoTrackIndex == -5) {
                            this.videoTrackIndex = this.mediaMuxer.addTrack(outputFormat, false);
                        }
                    } else if (dequeueOutputBuffer < 0) {
                        continue;
                    } else {
                        ByteBuffer byteBuffer;
                        if (VERSION.SDK_INT < i2) {
                            byteBuffer = outputBuffers[dequeueOutputBuffer];
                        } else {
                            byteBuffer = this.videoEncoder.getOutputBuffer(dequeueOutputBuffer);
                        }
                        if (byteBuffer != null) {
                            BufferInfo bufferInfo = this.videoBufferInfo;
                            int i3 = bufferInfo.size;
                            if (i3 > 1) {
                                if ((bufferInfo.flags & 2) == 0) {
                                    long writeSampleData = this.mediaMuxer.writeSampleData(this.videoTrackIndex, byteBuffer, bufferInfo, true);
                                    if (writeSampleData != 0) {
                                        didWriteData(this.videoFile, writeSampleData, false);
                                    }
                                } else if (this.videoTrackIndex == -5) {
                                    ByteBuffer allocate;
                                    byte[] bArr = new byte[i3];
                                    byteBuffer.limit(bufferInfo.offset + i3);
                                    byteBuffer.position(this.videoBufferInfo.offset);
                                    byteBuffer.get(bArr);
                                    i = this.videoBufferInfo.size - 1;
                                    while (i >= 0 && i > 3) {
                                        if (bArr[i] == z2 && bArr[i - 1] == (byte) 0 && bArr[i - 2] == (byte) 0) {
                                            i3 = i - 3;
                                            if (bArr[i3] == (byte) 0) {
                                                allocate = ByteBuffer.allocate(i3);
                                                byteBuffer = ByteBuffer.allocate(this.videoBufferInfo.size - i3);
                                                allocate.put(bArr, 0, i3).position(0);
                                                byteBuffer.put(bArr, i3, this.videoBufferInfo.size - i3).position(0);
                                                break;
                                            }
                                        }
                                        i--;
                                        z2 = true;
                                    }
                                    allocate = null;
                                    byteBuffer = null;
                                    MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", this.videoWidth, this.videoHeight);
                                    if (!(allocate == null || byteBuffer == null)) {
                                        createVideoFormat.setByteBuffer("csd-0", allocate);
                                        createVideoFormat.setByteBuffer("csd-1", byteBuffer);
                                    }
                                    this.videoTrackIndex = this.mediaMuxer.addTrack(createVideoFormat, false);
                                }
                            }
                            this.videoEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                            if ((this.videoBufferInfo.flags & 4) != 0) {
                                break;
                            }
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(dequeueOutputBuffer);
                            stringBuilder.append(str);
                            throw new RuntimeException(stringBuilder.toString());
                        }
                    }
                    i2 = 21;
                } else if (!z) {
                    break;
                } else {
                    i2 = 21;
                }
            }
            if (VERSION.SDK_INT < i2) {
                outputBuffers = this.audioEncoder.getOutputBuffers();
            }
            while (true) {
                i = this.audioEncoder.dequeueOutputBuffer(this.audioBufferInfo, 0);
                if (i == -1) {
                    if (!z) {
                        return;
                    }
                    if (!this.running && this.sendWhenDone == 0) {
                        return;
                    }
                } else if (i != -3) {
                    if (i == -2) {
                        MediaFormat outputFormat2 = this.audioEncoder.getOutputFormat();
                        if (this.audioTrackIndex == -5) {
                            this.audioTrackIndex = this.mediaMuxer.addTrack(outputFormat2, true);
                        }
                    } else if (i < 0) {
                        continue;
                    } else {
                        ByteBuffer byteBuffer2;
                        if (VERSION.SDK_INT < i2) {
                            byteBuffer2 = outputBuffers[i];
                        } else {
                            byteBuffer2 = this.audioEncoder.getOutputBuffer(i);
                        }
                        if (byteBuffer2 != null) {
                            BufferInfo bufferInfo2 = this.audioBufferInfo;
                            if ((bufferInfo2.flags & 2) != 0) {
                                bufferInfo2.size = 0;
                            }
                            BufferInfo bufferInfo3 = this.audioBufferInfo;
                            if (bufferInfo3.size != 0) {
                                long writeSampleData2 = this.mediaMuxer.writeSampleData(this.audioTrackIndex, byteBuffer2, bufferInfo3, false);
                                if (writeSampleData2 != 0) {
                                    didWriteData(this.videoFile, writeSampleData2, false);
                                }
                            }
                            this.audioEncoder.releaseOutputBuffer(i, false);
                            if ((this.audioBufferInfo.flags & 4) != 0) {
                                return;
                            }
                        } else {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(str2);
                            stringBuilder2.append(i);
                            stringBuilder2.append(str);
                            throw new RuntimeException(stringBuilder2.toString());
                        }
                    }
                    i2 = 21;
                } else if (VERSION.SDK_INT < i2) {
                    outputBuffers = this.audioEncoder.getOutputBuffers();
                }
                i2 = 21;
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            try {
                if (this.eglDisplay != EGL14.EGL_NO_DISPLAY) {
                    EGL14.eglMakeCurrent(this.eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
                    EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
                    EGL14.eglReleaseThread();
                    EGL14.eglTerminate(this.eglDisplay);
                    this.eglDisplay = EGL14.EGL_NO_DISPLAY;
                    this.eglContext = EGL14.EGL_NO_CONTEXT;
                    this.eglConfig = null;
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.InstantCameraView$CameraGLThread */
    public class CameraGLThread extends DispatchQueue {
        private final int DO_REINIT_MESSAGE = 2;
        private final int DO_RENDER_MESSAGE = 0;
        private final int DO_SETSESSION_MESSAGE = 3;
        private final int DO_SHUTDOWN_MESSAGE = 1;
        private final int EGL_CONTEXT_CLIENT_VERSION = 12440;
        private final int EGL_OPENGL_ES2_BIT = 4;
        private Integer cameraId = Integer.valueOf(0);
        private SurfaceTexture cameraSurface;
        private CameraSession currentSession;
        private int drawProgram;
        private EGL10 egl10;
        private javax.microedition.khronos.egl.EGLConfig eglConfig;
        private javax.microedition.khronos.egl.EGLContext eglContext;
        private javax.microedition.khronos.egl.EGLDisplay eglDisplay;
        private javax.microedition.khronos.egl.EGLSurface eglSurface;
        /* renamed from: gl */
        private GL f632gl;
        private boolean initied;
        private int positionHandle;
        private boolean recording;
        private int rotationAngle;
        private SurfaceTexture surfaceTexture;
        private int textureHandle;
        private int textureMatrixHandle;
        private int vertexMatrixHandle;
        private VideoRecorder videoEncoder;

        public CameraGLThread(SurfaceTexture surfaceTexture, int i, int i2) {
            super("CameraGLThread");
            this.surfaceTexture = surfaceTexture;
            int width = InstantCameraView.this.previewSize.getWidth();
            int height = InstantCameraView.this.previewSize.getHeight();
            float f = (float) i;
            float min = f / ((float) Math.min(width, height));
            width = (int) (((float) width) * min);
            height = (int) (((float) height) * min);
            if (width > height) {
                InstantCameraView.this.scaleX = 1.0f;
                InstantCameraView.this.scaleY = ((float) width) / ((float) i2);
                return;
            }
            InstantCameraView.this.scaleX = ((float) height) / f;
            InstantCameraView.this.scaleY = 1.0f;
        }

        private boolean initGL() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("start init gl");
            }
            this.egl10 = (EGL10) javax.microedition.khronos.egl.EGLContext.getEGL();
            this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            javax.microedition.khronos.egl.EGLDisplay eGLDisplay = this.eglDisplay;
            StringBuilder stringBuilder;
            if (eGLDisplay == EGL10.EGL_NO_DISPLAY) {
                if (BuildVars.LOGS_ENABLED) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("eglGetDisplay failed ");
                    stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.m28e(stringBuilder.toString());
                }
                finish();
                return false;
            }
            if (this.egl10.eglInitialize(eGLDisplay, new int[2])) {
                int[] iArr = new int[1];
                javax.microedition.khronos.egl.EGLConfig[] eGLConfigArr = new javax.microedition.khronos.egl.EGLConfig[1];
                if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12325, 0, 12326, 0, 12344}, eGLConfigArr, 1, iArr)) {
                    if (BuildVars.LOGS_ENABLED) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("eglChooseConfig failed ");
                        stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.m28e(stringBuilder.toString());
                    }
                    finish();
                    return false;
                } else if (iArr[0] > 0) {
                    this.eglConfig = eGLConfigArr[0];
                    this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                    if (this.eglContext == null) {
                        if (BuildVars.LOGS_ENABLED) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("eglCreateContext failed ");
                            stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                            FileLog.m28e(stringBuilder.toString());
                        }
                        finish();
                        return false;
                    }
                    SurfaceTexture surfaceTexture = this.surfaceTexture;
                    if (surfaceTexture instanceof SurfaceTexture) {
                        this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, surfaceTexture, null);
                        javax.microedition.khronos.egl.EGLSurface eGLSurface = this.eglSurface;
                        if (eGLSurface == null || eGLSurface == EGL10.EGL_NO_SURFACE) {
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("createWindowSurface failed ");
                                stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                FileLog.m28e(stringBuilder.toString());
                            }
                            finish();
                            return false;
                        } else if (this.egl10.eglMakeCurrent(this.eglDisplay, eGLSurface, eGLSurface, this.eglContext)) {
                            this.f632gl = this.eglContext.getGL();
                            float access$1200 = (1.0f / InstantCameraView.this.scaleX) / 2.0f;
                            float access$1300 = (1.0f / InstantCameraView.this.scaleY) / 2.0f;
                            float[] fArr = new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f};
                            r7 = new float[8];
                            float f = 0.5f - access$1200;
                            r7[0] = f;
                            float f2 = 0.5f - access$1300;
                            r7[1] = f2;
                            access$1200 += 0.5f;
                            r7[2] = access$1200;
                            r7[3] = f2;
                            r7[4] = f;
                            access$1300 += 0.5f;
                            r7[5] = access$1300;
                            r7[6] = access$1200;
                            r7[7] = access$1300;
                            this.videoEncoder = new VideoRecorder(InstantCameraView.this, null);
                            InstantCameraView.this.vertexBuffer = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                            InstantCameraView.this.vertexBuffer.put(fArr).position(0);
                            InstantCameraView.this.textureBuffer = ByteBuffer.allocateDirect(r7.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                            InstantCameraView.this.textureBuffer.put(r7).position(0);
                            Matrix.setIdentityM(InstantCameraView.this.mSTMatrix, 0);
                            int access$1800 = InstantCameraView.this.loadShader(35633, InstantCameraView.VERTEX_SHADER);
                            int access$18002 = InstantCameraView.this.loadShader(35632, InstantCameraView.FRAGMENT_SCREEN_SHADER);
                            if (access$1800 == 0 || access$18002 == 0) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.m28e("failed creating shader");
                                }
                                finish();
                                return false;
                            }
                            this.drawProgram = GLES20.glCreateProgram();
                            GLES20.glAttachShader(this.drawProgram, access$1800);
                            GLES20.glAttachShader(this.drawProgram, access$18002);
                            GLES20.glLinkProgram(this.drawProgram);
                            int[] iArr2 = new int[1];
                            GLES20.glGetProgramiv(this.drawProgram, 35714, iArr2, 0);
                            if (iArr2[0] == 0) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.m28e("failed link shader");
                                }
                                GLES20.glDeleteProgram(this.drawProgram);
                                this.drawProgram = 0;
                            } else {
                                this.positionHandle = GLES20.glGetAttribLocation(this.drawProgram, "aPosition");
                                this.textureHandle = GLES20.glGetAttribLocation(this.drawProgram, "aTextureCoord");
                                this.vertexMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uMVPMatrix");
                                this.textureMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uSTMatrix");
                            }
                            GLES20.glGenTextures(1, InstantCameraView.this.cameraTexture, 0);
                            GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
                            GLES20.glTexParameteri(36197, 10241, 9729);
                            GLES20.glTexParameteri(36197, 10240, 9729);
                            GLES20.glTexParameteri(36197, 10242, 33071);
                            GLES20.glTexParameteri(36197, 10243, 33071);
                            Matrix.setIdentityM(InstantCameraView.this.mMVPMatrix, 0);
                            this.cameraSurface = new SurfaceTexture(InstantCameraView.this.cameraTexture[0]);
                            this.cameraSurface.setOnFrameAvailableListener(new C2594x8cccee34(this));
                            InstantCameraView.this.createCamera(this.cameraSurface);
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.m28e("gl initied");
                            }
                            return true;
                        } else {
                            if (BuildVars.LOGS_ENABLED) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("eglMakeCurrent failed ");
                                stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                FileLog.m28e(stringBuilder.toString());
                            }
                            finish();
                            return false;
                        }
                    }
                    finish();
                    return false;
                } else {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m28e("eglConfig not initialized");
                    }
                    finish();
                    return false;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("eglInitialize failed ");
                stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                FileLog.m28e(stringBuilder.toString());
            }
            finish();
            return false;
        }

        public /* synthetic */ void lambda$initGL$0$InstantCameraView$CameraGLThread(SurfaceTexture surfaceTexture) {
            requestRender();
        }

        public void reinitForNewCamera() {
            Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(2), 0);
            }
        }

        public void finish() {
            if (this.eglSurface != null) {
                EGL10 egl10 = this.egl10;
                javax.microedition.khronos.egl.EGLDisplay eGLDisplay = this.eglDisplay;
                javax.microedition.khronos.egl.EGLSurface eGLSurface = EGL10.EGL_NO_SURFACE;
                egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL10.EGL_NO_CONTEXT);
                this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
                this.eglSurface = null;
            }
            javax.microedition.khronos.egl.EGLContext eGLContext = this.eglContext;
            if (eGLContext != null) {
                this.egl10.eglDestroyContext(this.eglDisplay, eGLContext);
                this.eglContext = null;
            }
            javax.microedition.khronos.egl.EGLDisplay eGLDisplay2 = this.eglDisplay;
            if (eGLDisplay2 != null) {
                this.egl10.eglTerminate(eGLDisplay2);
                this.eglDisplay = null;
            }
        }

        public void setCurrentSession(CameraSession cameraSession) {
            Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(3, cameraSession), 0);
            }
        }

        private void onDraw(Integer num) {
            if (this.initied) {
                if (!(this.eglContext.equals(this.egl10.eglGetCurrentContext()) && this.eglSurface.equals(this.egl10.eglGetCurrentSurface(12377)))) {
                    EGL10 egl10 = this.egl10;
                    javax.microedition.khronos.egl.EGLDisplay eGLDisplay = this.eglDisplay;
                    javax.microedition.khronos.egl.EGLSurface eGLSurface = this.eglSurface;
                    if (!egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext)) {
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("eglMakeCurrent failed ");
                            stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                            FileLog.m28e(stringBuilder.toString());
                        }
                        return;
                    }
                }
                this.cameraSurface.updateTexImage();
                if (!this.recording) {
                    this.videoEncoder.startRecording(InstantCameraView.this.cameraFile, EGL14.eglGetCurrentContext());
                    this.recording = true;
                    int currentOrientation = this.currentSession.getCurrentOrientation();
                    if (currentOrientation == 90 || currentOrientation == 270) {
                        float access$1200 = InstantCameraView.this.scaleX;
                        InstantCameraView instantCameraView = InstantCameraView.this;
                        instantCameraView.scaleX = instantCameraView.scaleY;
                        InstantCameraView.this.scaleY = access$1200;
                    }
                }
                this.videoEncoder.frameAvailable(this.cameraSurface, num, System.nanoTime());
                this.cameraSurface.getTransformMatrix(InstantCameraView.this.mSTMatrix);
                GLES20.glUseProgram(this.drawProgram);
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
                GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 12, InstantCameraView.this.vertexBuffer);
                GLES20.glEnableVertexAttribArray(this.positionHandle);
                GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 8, InstantCameraView.this.textureBuffer);
                GLES20.glEnableVertexAttribArray(this.textureHandle);
                GLES20.glUniformMatrix4fv(this.textureMatrixHandle, 1, false, InstantCameraView.this.mSTMatrix, 0);
                GLES20.glUniformMatrix4fv(this.vertexMatrixHandle, 1, false, InstantCameraView.this.mMVPMatrix, 0);
                GLES20.glDrawArrays(5, 0, 4);
                GLES20.glDisableVertexAttribArray(this.positionHandle);
                GLES20.glDisableVertexAttribArray(this.textureHandle);
                GLES20.glBindTexture(36197, 0);
                GLES20.glUseProgram(0);
                this.egl10.eglSwapBuffers(this.eglDisplay, this.eglSurface);
            }
        }

        public void run() {
            this.initied = initGL();
            super.run();
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                onDraw((Integer) message.obj);
            } else if (i == 1) {
                finish();
                if (this.recording) {
                    this.videoEncoder.stopRecording(message.arg1);
                }
                Looper myLooper = Looper.myLooper();
                if (myLooper != null) {
                    myLooper.quit();
                }
            } else if (i == 2) {
                EGL10 egl10 = this.egl10;
                javax.microedition.khronos.egl.EGLDisplay eGLDisplay = this.eglDisplay;
                javax.microedition.khronos.egl.EGLSurface eGLSurface = this.eglSurface;
                if (egl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.eglContext)) {
                    SurfaceTexture surfaceTexture = this.cameraSurface;
                    if (surfaceTexture != null) {
                        surfaceTexture.getTransformMatrix(InstantCameraView.this.moldSTMatrix);
                        this.cameraSurface.setOnFrameAvailableListener(null);
                        this.cameraSurface.release();
                        InstantCameraView.this.oldCameraTexture[0] = InstantCameraView.this.cameraTexture[0];
                        InstantCameraView.this.cameraTextureAlpha = 0.0f;
                        InstantCameraView.this.cameraTexture[0] = 0;
                    }
                    this.cameraId = Integer.valueOf(this.cameraId.intValue() + 1);
                    InstantCameraView.this.cameraReady = false;
                    GLES20.glGenTextures(1, InstantCameraView.this.cameraTexture, 0);
                    GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
                    GLES20.glTexParameteri(36197, 10241, 9729);
                    GLES20.glTexParameteri(36197, 10240, 9729);
                    GLES20.glTexParameteri(36197, 10242, 33071);
                    GLES20.glTexParameteri(36197, 10243, 33071);
                    this.cameraSurface = new SurfaceTexture(InstantCameraView.this.cameraTexture[0]);
                    this.cameraSurface.setOnFrameAvailableListener(new C2593x60a77276(this));
                    InstantCameraView.this.createCamera(this.cameraSurface);
                } else {
                    if (BuildVars.LOGS_ENABLED) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("eglMakeCurrent failed ");
                        stringBuilder.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.m27d(stringBuilder.toString());
                    }
                }
            } else if (i == 3) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.m27d("set gl rednderer session");
                }
                CameraSession cameraSession = (CameraSession) message.obj;
                CameraSession cameraSession2 = this.currentSession;
                if (cameraSession2 == cameraSession) {
                    this.rotationAngle = cameraSession2.getWorldAngle();
                    Matrix.setIdentityM(InstantCameraView.this.mMVPMatrix, 0);
                    if (this.rotationAngle != 0) {
                        Matrix.rotateM(InstantCameraView.this.mMVPMatrix, 0, (float) this.rotationAngle, 0.0f, 0.0f, 1.0f);
                    }
                } else {
                    this.currentSession = cameraSession;
                }
            }
        }

        public /* synthetic */ void lambda$handleMessage$1$InstantCameraView$CameraGLThread(SurfaceTexture surfaceTexture) {
            requestRender();
        }

        public void shutdown(int i) {
            Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(1, i, 0), 0);
            }
        }

        public void requestRender() {
            Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                sendMessage(handler.obtainMessage(0, this.cameraId), 0);
            }
        }
    }

    public InstantCameraView(Context context, ChatActivity chatActivity) {
        super(context);
        this.aspectRatio = SharedConfig.roundCamera16to9 ? new Size(16, 9) : new Size(4, 3);
        this.mMVPMatrix = new float[16];
        this.mSTMatrix = new float[16];
        this.moldSTMatrix = new float[16];
        setOnTouchListener(new C2602-$$Lambda$InstantCameraView$qN9GPrIe8LvogWTbvas-MPqE9F8(this));
        setWillNotDraw(false);
        setBackgroundColor(-1073741824);
        this.baseFragment = chatActivity;
        this.isSecretChat = this.baseFragment.getCurrentEncryptedChat() != null;
        this.paint = new Paint(1) {
            public void setAlpha(int i) {
                super.setAlpha(i);
                InstantCameraView.this.invalidate();
            }
        };
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setStrokeWidth((float) AndroidUtilities.m26dp(3.0f));
        this.paint.setColor(-1);
        this.rect = new RectF();
        if (VERSION.SDK_INT >= 21) {
            this.cameraContainer = new FrameLayout(context) {
                public void setScaleX(float f) {
                    super.setScaleX(f);
                    InstantCameraView.this.invalidate();
                }

                public void setAlpha(float f) {
                    super.setAlpha(f);
                    InstantCameraView.this.invalidate();
                }
            };
            this.cameraContainer.setOutlineProvider(new C28625());
            this.cameraContainer.setClipToOutline(true);
            this.cameraContainer.setWillNotDraw(false);
        } else {
            final Path path = new Path();
            final Paint paint = new Paint(1);
            paint.setColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
            paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            this.cameraContainer = new FrameLayout(context) {
                public void setScaleX(float f) {
                    super.setScaleX(f);
                    InstantCameraView.this.invalidate();
                }

                /* Access modifiers changed, original: protected */
                public void onSizeChanged(int i, int i2, int i3, int i4) {
                    super.onSizeChanged(i, i2, i3, i4);
                    path.reset();
                    float f = (float) (i / 2);
                    path.addCircle(f, (float) (i2 / 2), f, Direction.CW);
                    path.toggleInverseFillType();
                }

                /* Access modifiers changed, original: protected */
                public void dispatchDraw(Canvas canvas) {
                    try {
                        super.dispatchDraw(canvas);
                        canvas.drawPath(path, paint);
                    } catch (Exception unused) {
                    }
                }
            };
            this.cameraContainer.setWillNotDraw(false);
            this.cameraContainer.setLayerType(2, null);
        }
        FrameLayout frameLayout = this.cameraContainer;
        int i = AndroidUtilities.roundMessageSize;
        addView(frameLayout, new LayoutParams(i, i, 17));
        this.switchCameraButton = new ImageView(context);
        this.switchCameraButton.setScaleType(ScaleType.CENTER);
        this.switchCameraButton.setContentDescription(LocaleController.getString("AccDescrSwitchCamera", C1067R.string.AccDescrSwitchCamera));
        addView(this.switchCameraButton, LayoutHelper.createFrame(48, 48.0f, 83, 20.0f, 0.0f, 0.0f, 14.0f));
        this.switchCameraButton.setOnClickListener(new C2597-$$Lambda$InstantCameraView$S2zQmSsGveVOM2kGAgGGE27kbGY(this));
        this.muteImageView = new ImageView(context);
        this.muteImageView.setScaleType(ScaleType.CENTER);
        this.muteImageView.setImageResource(C1067R.C1065drawable.video_mute);
        this.muteImageView.setAlpha(0.0f);
        addView(this.muteImageView, LayoutHelper.createFrame(48, 48, 17));
        ((LayoutParams) this.muteImageView.getLayoutParams()).topMargin = (AndroidUtilities.roundMessageSize / 2) - AndroidUtilities.m26dp(24.0f);
        this.textureOverlayView = new BackupImageView(getContext());
        this.textureOverlayView.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
        BackupImageView backupImageView = this.textureOverlayView;
        int i2 = AndroidUtilities.roundMessageSize;
        addView(backupImageView, new LayoutParams(i2, i2, 17));
        setVisibility(4);
    }

    public /* synthetic */ boolean lambda$new$0$InstantCameraView(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            ChatActivity chatActivity = this.baseFragment;
            if (chatActivity != null) {
                VideoPlayer videoPlayer = this.videoPlayer;
                if (videoPlayer != null) {
                    int isMuted = videoPlayer.isMuted() ^ 1;
                    this.videoPlayer.setMute(isMuted);
                    AnimatorSet animatorSet = this.muteAnimation;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                    }
                    this.muteAnimation = new AnimatorSet();
                    animatorSet = this.muteAnimation;
                    Animator[] animatorArr = new Animator[3];
                    ImageView imageView = this.muteImageView;
                    float[] fArr = new float[1];
                    float f = 1.0f;
                    fArr[0] = isMuted != 0 ? 1.0f : 0.0f;
                    animatorArr[0] = ObjectAnimator.ofFloat(imageView, "alpha", fArr);
                    imageView = this.muteImageView;
                    fArr = new float[1];
                    fArr[0] = isMuted != 0 ? 1.0f : 0.5f;
                    animatorArr[1] = ObjectAnimator.ofFloat(imageView, "scaleX", fArr);
                    ImageView imageView2 = this.muteImageView;
                    float[] fArr2 = new float[1];
                    if (isMuted == 0) {
                        f = 0.5f;
                    }
                    fArr2[0] = f;
                    animatorArr[2] = ObjectAnimator.ofFloat(imageView2, "scaleY", fArr2);
                    animatorSet.playTogether(animatorArr);
                    this.muteAnimation.addListener(new C28592());
                    this.muteAnimation.setDuration(180);
                    this.muteAnimation.setInterpolator(new DecelerateInterpolator());
                    this.muteAnimation.start();
                } else {
                    chatActivity.checkRecordLocked();
                }
            }
        }
        return true;
    }

    public /* synthetic */ void lambda$new$1$InstantCameraView(View view) {
        if (this.cameraReady) {
            CameraSession cameraSession = this.cameraSession;
            if (cameraSession != null && cameraSession.isInitied() && this.cameraThread != null) {
                switchCamera();
                ObjectAnimator duration = ObjectAnimator.ofFloat(this.switchCameraButton, "scaleX", new float[]{0.0f}).setDuration(100);
                duration.addListener(new C28647());
                duration.start();
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (getVisibility() != 0) {
            this.cameraContainer.setTranslationY((float) (getMeasuredHeight() / 2));
            this.textureOverlayView.setTranslationY((float) (getMeasuredHeight() / 2));
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.recordProgressChanged) {
            long longValue = ((Long) objArr[0]).longValue();
            this.progress = ((float) longValue) / 60000.0f;
            this.recordedTime = longValue;
            invalidate();
        } else if (i == NotificationCenter.FileDidUpload) {
            String str = (String) objArr[0];
            File file = this.cameraFile;
            if (file != null && file.getAbsolutePath().equals(str)) {
                this.file = (InputFile) objArr[1];
                this.encryptedFile = (InputEncryptedFile) objArr[2];
                this.size = ((Long) objArr[5]).longValue();
                if (this.encryptedFile != null) {
                    this.key = (byte[]) objArr[3];
                    this.f633iv = (byte[]) objArr[4];
                }
            }
        }
    }

    public void destroy(boolean z, Runnable runnable) {
        CameraSession cameraSession = this.cameraSession;
        if (cameraSession != null) {
            cameraSession.destroy();
            CameraController.getInstance().close(this.cameraSession, !z ? new CountDownLatch(1) : null, runnable);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        float x = this.cameraContainer.getX();
        float y = this.cameraContainer.getY();
        this.rect.set(x - ((float) AndroidUtilities.m26dp(8.0f)), y - ((float) AndroidUtilities.m26dp(8.0f)), (((float) this.cameraContainer.getMeasuredWidth()) + x) + ((float) AndroidUtilities.m26dp(8.0f)), (((float) this.cameraContainer.getMeasuredHeight()) + y) + ((float) AndroidUtilities.m26dp(8.0f)));
        float f = this.progress;
        if (f != 0.0f) {
            canvas.drawArc(this.rect, -90.0f, f * 360.0f, false, this.paint);
        }
        if (Theme.chat_roundVideoShadow != null) {
            int dp = ((int) x) - AndroidUtilities.m26dp(3.0f);
            int dp2 = ((int) y) - AndroidUtilities.m26dp(2.0f);
            canvas.save();
            canvas.scale(this.cameraContainer.getScaleX(), this.cameraContainer.getScaleY(), (float) (((AndroidUtilities.roundMessageSize / 2) + dp) + AndroidUtilities.m26dp(3.0f)), (float) (((AndroidUtilities.roundMessageSize / 2) + dp2) + AndroidUtilities.m26dp(3.0f)));
            Theme.chat_roundVideoShadow.setAlpha((int) (this.cameraContainer.getAlpha() * 255.0f));
            Theme.chat_roundVideoShadow.setBounds(dp, dp2, (AndroidUtilities.roundMessageSize + dp) + AndroidUtilities.m26dp(6.0f), (AndroidUtilities.roundMessageSize + dp2) + AndroidUtilities.m26dp(6.0f));
            Theme.chat_roundVideoShadow.draw(canvas);
            canvas.restore();
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        setAlpha(0.0f);
        this.switchCameraButton.setAlpha(0.0f);
        this.cameraContainer.setAlpha(0.0f);
        this.textureOverlayView.setAlpha(0.0f);
        this.muteImageView.setAlpha(0.0f);
        this.muteImageView.setScaleX(1.0f);
        this.muteImageView.setScaleY(1.0f);
        this.cameraContainer.setScaleX(0.1f);
        this.cameraContainer.setScaleY(0.1f);
        this.textureOverlayView.setScaleX(0.1f);
        this.textureOverlayView.setScaleY(0.1f);
        if (this.cameraContainer.getMeasuredWidth() != 0) {
            FrameLayout frameLayout = this.cameraContainer;
            frameLayout.setPivotX((float) (frameLayout.getMeasuredWidth() / 2));
            frameLayout = this.cameraContainer;
            frameLayout.setPivotY((float) (frameLayout.getMeasuredHeight() / 2));
            BackupImageView backupImageView = this.textureOverlayView;
            backupImageView.setPivotX((float) (backupImageView.getMeasuredWidth() / 2));
            backupImageView = this.textureOverlayView;
            backupImageView.setPivotY((float) (backupImageView.getMeasuredHeight() / 2));
        }
        if (i == 0) {
            try {
                ((Activity) getContext()).getWindow().addFlags(128);
                return;
            } catch (Exception e) {
                FileLog.m30e(e);
                return;
            }
        }
        ((Activity) getContext()).getWindow().clearFlags(128);
    }

    public void showCamera() {
        if (this.textureView == null) {
            this.switchCameraButton.setImageResource(C1067R.C1065drawable.camera_revert1);
            this.textureOverlayView.setAlpha(1.0f);
            if (this.lastBitmap == null) {
                try {
                    this.lastBitmap = BitmapFactory.decodeFile(new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg").getAbsolutePath());
                } catch (Throwable unused) {
                }
            }
            Bitmap bitmap = this.lastBitmap;
            if (bitmap != null) {
                this.textureOverlayView.setImageBitmap(bitmap);
            } else {
                this.textureOverlayView.setImageResource(C1067R.C1065drawable.icplaceholder);
            }
            this.cameraReady = false;
            this.isFrontface = true;
            this.selectedCamera = null;
            this.recordedTime = 0;
            this.progress = 0.0f;
            this.cancelled = false;
            this.file = null;
            this.encryptedFile = null;
            this.key = null;
            this.f633iv = null;
            if (initCamera()) {
                MediaController.getInstance().lambda$startAudioAgain$5$MediaController(MediaController.getInstance().getPlayingMessageObject());
                File directory = FileLoader.getDirectory(4);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(SharedConfig.getLastLocalId());
                stringBuilder.append(".mp4");
                this.cameraFile = new File(directory, stringBuilder.toString());
                SharedConfig.saveConfig();
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.m27d("show round camera");
                }
                this.textureView = new TextureView(getContext());
                this.textureView.setSurfaceTextureListener(new C28658());
                this.cameraContainer.addView(this.textureView, LayoutHelper.createFrame(-1, -1.0f));
                setVisibility(0);
                startAnimation(true);
            }
        }
    }

    public FrameLayout getCameraContainer() {
        return this.cameraContainer;
    }

    public void startAnimation(boolean z) {
        AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        PipRoundVideoView instance = PipRoundVideoView.getInstance();
        if (instance != null) {
            instance.showTemporary(z ^ 1);
        }
        this.animatorSet = new AnimatorSet();
        animatorSet = this.animatorSet;
        Animator[] animatorArr = new Animator[12];
        float[] fArr = new float[1];
        float f = 1.0f;
        float f2 = 0.0f;
        fArr[0] = z ? 1.0f : 0.0f;
        String str = "alpha";
        animatorArr[0] = ObjectAnimator.ofFloat(this, str, fArr);
        ImageView imageView = this.switchCameraButton;
        float[] fArr2 = new float[1];
        fArr2[0] = z ? 1.0f : 0.0f;
        animatorArr[1] = ObjectAnimator.ofFloat(imageView, str, fArr2);
        animatorArr[2] = ObjectAnimator.ofFloat(this.muteImageView, str, new float[]{0.0f});
        Paint paint = this.paint;
        int[] iArr = new int[1];
        iArr[0] = z ? NalUnitUtil.EXTENDED_SAR : 0;
        animatorArr[3] = ObjectAnimator.ofInt(paint, str, iArr);
        FrameLayout frameLayout = this.cameraContainer;
        float[] fArr3 = new float[1];
        fArr3[0] = z ? 1.0f : 0.0f;
        animatorArr[4] = ObjectAnimator.ofFloat(frameLayout, str, fArr3);
        frameLayout = this.cameraContainer;
        fArr3 = new float[1];
        fArr3[0] = z ? 1.0f : 0.1f;
        String str2 = "scaleX";
        animatorArr[5] = ObjectAnimator.ofFloat(frameLayout, str2, fArr3);
        frameLayout = this.cameraContainer;
        fArr3 = new float[1];
        fArr3[0] = z ? 1.0f : 0.1f;
        String str3 = "scaleY";
        animatorArr[6] = ObjectAnimator.ofFloat(frameLayout, str3, fArr3);
        frameLayout = this.cameraContainer;
        fArr3 = new float[2];
        fArr3[0] = z ? (float) (getMeasuredHeight() / 2) : 0.0f;
        fArr3[1] = z ? 0.0f : (float) (getMeasuredHeight() / 2);
        String str4 = "translationY";
        animatorArr[7] = ObjectAnimator.ofFloat(frameLayout, str4, fArr3);
        BackupImageView backupImageView = this.textureOverlayView;
        fArr3 = new float[1];
        fArr3[0] = z ? 1.0f : 0.0f;
        animatorArr[8] = ObjectAnimator.ofFloat(backupImageView, str, fArr3);
        BackupImageView backupImageView2 = this.textureOverlayView;
        float[] fArr4 = new float[1];
        fArr4[0] = z ? 1.0f : 0.1f;
        animatorArr[9] = ObjectAnimator.ofFloat(backupImageView2, str2, fArr4);
        backupImageView2 = this.textureOverlayView;
        fArr4 = new float[1];
        if (!z) {
            f = 0.1f;
        }
        fArr4[0] = f;
        animatorArr[10] = ObjectAnimator.ofFloat(backupImageView2, str3, fArr4);
        BackupImageView backupImageView3 = this.textureOverlayView;
        float[] fArr5 = new float[2];
        fArr5[0] = z ? (float) (getMeasuredHeight() / 2) : 0.0f;
        if (!z) {
            f2 = (float) (getMeasuredHeight() / 2);
        }
        fArr5[1] = f2;
        animatorArr[11] = ObjectAnimator.ofFloat(backupImageView3, str4, fArr5);
        animatorSet.playTogether(animatorArr);
        if (!z) {
            this.animatorSet.addListener(new C28669());
        }
        this.animatorSet.setDuration(180);
        this.animatorSet.setInterpolator(new DecelerateInterpolator());
        this.animatorSet.start();
    }

    public Rect getCameraRect() {
        this.cameraContainer.getLocationOnScreen(this.position);
        int[] iArr = this.position;
        return new Rect((float) iArr[0], (float) iArr[1], (float) this.cameraContainer.getWidth(), (float) this.cameraContainer.getHeight());
    }

    public void changeVideoPreviewState(int i, float f) {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            if (i == 0) {
                startProgressTimer();
                this.videoPlayer.play();
            } else if (i == 1) {
                stopProgressTimer();
                this.videoPlayer.pause();
            } else if (i == 2) {
                videoPlayer.seekTo((long) (f * ((float) videoPlayer.getDuration())));
            }
        }
    }

    public void send(int i) {
        if (this.textureView != null) {
            stopProgressTimer();
            VideoPlayer videoPlayer = this.videoPlayer;
            if (videoPlayer != null) {
                videoPlayer.releasePlayer(true);
                this.videoPlayer = null;
            }
            if (i == 4) {
                VideoEditedInfo videoEditedInfo;
                if (this.videoEditedInfo.needConvert()) {
                    this.file = null;
                    this.encryptedFile = null;
                    this.key = null;
                    this.f633iv = null;
                    videoEditedInfo = this.videoEditedInfo;
                    double d = (double) videoEditedInfo.estimatedDuration;
                    long j = videoEditedInfo.startTime;
                    if (j < 0) {
                        j = 0;
                    }
                    videoEditedInfo = this.videoEditedInfo;
                    long j2 = videoEditedInfo.endTime;
                    if (j2 < 0) {
                        j2 = videoEditedInfo.estimatedDuration;
                    }
                    videoEditedInfo = this.videoEditedInfo;
                    videoEditedInfo.estimatedDuration = j2 - j;
                    double d2 = (double) this.size;
                    double d3 = (double) videoEditedInfo.estimatedDuration;
                    Double.isNaN(d3);
                    Double.isNaN(d);
                    d3 /= d;
                    Double.isNaN(d2);
                    videoEditedInfo.estimatedSize = Math.max(1, (long) (d2 * d3));
                    videoEditedInfo = this.videoEditedInfo;
                    videoEditedInfo.bitrate = 400000;
                    long j3 = videoEditedInfo.startTime;
                    if (j3 > 0) {
                        videoEditedInfo.startTime = j3 * 1000;
                    }
                    videoEditedInfo = this.videoEditedInfo;
                    j3 = videoEditedInfo.endTime;
                    if (j3 > 0) {
                        videoEditedInfo.endTime = j3 * 1000;
                    }
                    FileLoader.getInstance(this.currentAccount).cancelUploadFile(this.cameraFile.getAbsolutePath(), false);
                } else {
                    this.videoEditedInfo.estimatedSize = Math.max(1, this.size);
                }
                videoEditedInfo = this.videoEditedInfo;
                videoEditedInfo.file = this.file;
                videoEditedInfo.encryptedFile = this.encryptedFile;
                videoEditedInfo.key = this.key;
                videoEditedInfo.f423iv = this.f633iv;
                this.baseFragment.sendMedia(new PhotoEntry(0, 0, 0, this.cameraFile.getAbsolutePath(), 0, true), this.videoEditedInfo);
            } else {
                this.cancelled = this.recordedTime < 800;
                this.recording = false;
                AndroidUtilities.cancelRunOnUIThread(this.timerRunnable);
                if (this.cameraThread != null) {
                    NotificationCenter instance = NotificationCenter.getInstance(this.currentAccount);
                    int i2 = NotificationCenter.recordStopped;
                    Object[] objArr = new Object[1];
                    int i3 = 2;
                    int i4 = (this.cancelled || i != 3) ? 0 : 2;
                    objArr[0] = Integer.valueOf(i4);
                    instance.postNotificationName(i2, objArr);
                    if (this.cancelled) {
                        i3 = 0;
                    } else if (i != 3) {
                        i3 = 1;
                    }
                    saveLastCameraBitmap();
                    this.cameraThread.shutdown(i3);
                    this.cameraThread = null;
                }
                if (this.cancelled) {
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.audioRecordTooShort, Boolean.valueOf(true));
                    startAnimation(false);
                }
            }
        }
    }

    private void saveLastCameraBitmap() {
        if (this.textureView.getBitmap() != null) {
            this.lastBitmap = Bitmap.createScaledBitmap(this.textureView.getBitmap(), 80, 80, true);
            Bitmap bitmap = this.lastBitmap;
            if (bitmap != null) {
                Utilities.blurBitmap(bitmap, 7, 1, bitmap.getWidth(), this.lastBitmap.getHeight(), this.lastBitmap.getRowBytes());
                try {
                    this.lastBitmap.compress(CompressFormat.JPEG, 87, new FileOutputStream(new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg")));
                } catch (Throwable unused) {
                }
            }
        }
    }

    public void cancel() {
        stopProgressTimer();
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        if (this.textureView != null) {
            this.cancelled = true;
            this.recording = false;
            AndroidUtilities.cancelRunOnUIThread(this.timerRunnable);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recordStopped, Integer.valueOf(0));
            if (this.cameraThread != null) {
                saveLastCameraBitmap();
                this.cameraThread.shutdown(0);
                this.cameraThread = null;
            }
            File file = this.cameraFile;
            if (file != null) {
                file.delete();
                this.cameraFile = null;
            }
            startAnimation(false);
        }
    }

    @Keep
    public void setAlpha(float f) {
        ((ColorDrawable) getBackground()).setAlpha((int) (f * 192.0f));
        invalidate();
    }

    public View getSwitchButtonView() {
        return this.switchCameraButton;
    }

    public View getMuteImageView() {
        return this.muteImageView;
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void hideCamera(boolean z) {
        destroy(z, null);
        this.cameraContainer.removeView(this.textureView);
        this.cameraContainer.setTranslationX(0.0f);
        this.cameraContainer.setTranslationY(0.0f);
        this.textureOverlayView.setTranslationX(0.0f);
        this.textureOverlayView.setTranslationY(0.0f);
        this.textureView = null;
    }

    private void switchCamera() {
        saveLastCameraBitmap();
        Bitmap bitmap = this.lastBitmap;
        if (bitmap != null) {
            this.textureOverlayView.setImageBitmap(bitmap);
            this.textureOverlayView.animate().setDuration(120).alpha(1.0f).setInterpolator(new DecelerateInterpolator()).start();
        }
        CameraSession cameraSession = this.cameraSession;
        if (cameraSession != null) {
            cameraSession.destroy();
            CameraController.getInstance().close(this.cameraSession, null, null);
            this.cameraSession = null;
        }
        this.isFrontface ^= 1;
        initCamera();
        this.cameraReady = false;
        this.cameraThread.reinitForNewCamera();
    }

    private boolean initCamera() {
        ArrayList cameras = CameraController.getInstance().getCameras();
        boolean z = false;
        if (cameras == null) {
            return false;
        }
        CameraInfo cameraInfo = null;
        int i = 0;
        while (i < cameras.size()) {
            CameraInfo cameraInfo2 = (CameraInfo) cameras.get(i);
            if (!cameraInfo2.isFrontface()) {
                cameraInfo = cameraInfo2;
            }
            if ((this.isFrontface && cameraInfo2.isFrontface()) || (!this.isFrontface && !cameraInfo2.isFrontface())) {
                this.selectedCamera = cameraInfo2;
                break;
            }
            i++;
            cameraInfo = cameraInfo2;
        }
        if (this.selectedCamera == null) {
            this.selectedCamera = cameraInfo;
        }
        CameraInfo cameraInfo3 = this.selectedCamera;
        if (cameraInfo3 == null) {
            return false;
        }
        cameras = cameraInfo3.getPreviewSizes();
        ArrayList pictureSizes = this.selectedCamera.getPictureSizes();
        this.previewSize = CameraController.chooseOptimalSize(cameras, 480, 270, this.aspectRatio);
        this.pictureSize = CameraController.chooseOptimalSize(pictureSizes, 480, 270, this.aspectRatio);
        if (this.previewSize.mWidth != this.pictureSize.mWidth) {
            int size;
            Size size2;
            int size3;
            Size size4;
            int i2;
            int i3;
            for (size = cameras.size() - 1; size >= 0; size--) {
                size2 = (Size) cameras.get(size);
                for (size3 = pictureSizes.size() - 1; size3 >= 0; size3--) {
                    size4 = (Size) pictureSizes.get(size3);
                    i2 = size2.mWidth;
                    Size size5 = this.pictureSize;
                    if (i2 >= size5.mWidth) {
                        i3 = size2.mHeight;
                        if (i3 >= size5.mHeight && i2 == size4.mWidth && i3 == size4.mHeight) {
                            this.previewSize = size2;
                            this.pictureSize = size4;
                            z = true;
                            break;
                        }
                    }
                }
                if (z) {
                    break;
                }
            }
            if (!z) {
                for (size = cameras.size() - 1; size >= 0; size--) {
                    size2 = (Size) cameras.get(size);
                    for (size3 = pictureSizes.size() - 1; size3 >= 0; size3--) {
                        size4 = (Size) pictureSizes.get(size3);
                        i2 = size2.mWidth;
                        if (i2 >= 240) {
                            i3 = size2.mHeight;
                            if (i3 >= 240 && i2 == size4.mWidth && i3 == size4.mHeight) {
                                this.previewSize = size2;
                                this.pictureSize = size4;
                                z = true;
                                break;
                            }
                        }
                    }
                    if (z) {
                        break;
                    }
                }
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("preview w = ");
            stringBuilder.append(this.previewSize.mWidth);
            stringBuilder.append(" h = ");
            stringBuilder.append(this.previewSize.mHeight);
            FileLog.m27d(stringBuilder.toString());
        }
        return true;
    }

    private void createCamera(SurfaceTexture surfaceTexture) {
        AndroidUtilities.runOnUIThread(new C2596-$$Lambda$InstantCameraView$RBsoQ3f2-7L3ZL_CYLuRpS20Mko(this, surfaceTexture));
    }

    public /* synthetic */ void lambda$createCamera$4$InstantCameraView(SurfaceTexture surfaceTexture) {
        if (this.cameraThread != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("create camera session");
            }
            surfaceTexture.setDefaultBufferSize(this.previewSize.getWidth(), this.previewSize.getHeight());
            this.cameraSession = new CameraSession(this.selectedCamera, this.previewSize, this.pictureSize, 256);
            this.cameraThread.setCurrentSession(this.cameraSession);
            CameraController.getInstance().openRound(this.cameraSession, surfaceTexture, new C2595-$$Lambda$InstantCameraView$MN-OUeYmzAWjQZgMOwz5fmkJ-G8(this), new C2603-$$Lambda$InstantCameraView$xL5rY6Uq2g-HX9Vsko6XZnx3fLI(this));
        }
    }

    public /* synthetic */ void lambda$null$2$InstantCameraView() {
        if (this.cameraSession != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("camera initied");
            }
            this.cameraSession.setInitied();
        }
    }

    public /* synthetic */ void lambda$null$3$InstantCameraView() {
        this.cameraThread.setCurrentSession(this.cameraSession);
    }

    private int loadShader(int i, String str) {
        i = GLES20.glCreateShader(i);
        GLES20.glShaderSource(i, str);
        GLES20.glCompileShader(i);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(i, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return i;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.m28e(GLES20.glGetShaderInfoLog(i));
        }
        GLES20.glDeleteShader(i);
        return 0;
    }

    private void startProgressTimer() {
        Timer timer = this.progressTimer;
        if (timer != null) {
            try {
                timer.cancel();
                this.progressTimer = null;
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
        this.progressTimer = new Timer();
        this.progressTimer.schedule(new C285710(), 0, 17);
    }

    private void stopProgressTimer() {
        Timer timer = this.progressTimer;
        if (timer != null) {
            try {
                timer.cancel();
                this.progressTimer = null;
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }
}