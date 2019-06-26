// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.net.Uri;
import org.telegram.messenger.video.Mp4Movie;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.opengl.EGLExt;
import android.view.Surface;
import org.telegram.messenger.video.MP4Builder;
import java.util.concurrent.ArrayBlockingQueue;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodec$BufferInfo;
import java.lang.ref.WeakReference;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.nio.Buffer;
import android.opengl.EGL14;
import android.graphics.SurfaceTexture$OnFrameAvailableListener;
import android.opengl.Matrix;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.opengl.GLUtils;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGL10;
import org.telegram.messenger.DispatchQueue;
import android.animation.AnimatorListenerAdapter;
import android.view.TextureView$SurfaceTextureListener;
import android.graphics.BitmapFactory;
import android.app.Activity;
import androidx.annotation.Keep;
import android.graphics.drawable.ColorDrawable;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.FileLoader;
import org.telegram.ui.ActionBar.Theme;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import java.util.concurrent.CountDownLatch;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.TimerTask;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.Bitmap$CompressFormat;
import org.telegram.messenger.Utilities;
import android.opengl.GLES20;
import java.util.ArrayList;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.BuildVars;
import java.util.List;
import org.telegram.messenger.camera.CameraController;
import android.graphics.SurfaceTexture;
import android.view.View$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.Path$Direction;
import android.graphics.Canvas;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Path;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.os.Build$VERSION;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Style;
import android.view.View$OnTouchListener;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.messenger.VideoEditedInfo;
import android.view.TextureView;
import java.nio.FloatBuffer;
import org.telegram.messenger.camera.CameraInfo;
import android.graphics.RectF;
import java.util.Timer;
import android.graphics.Paint;
import android.widget.ImageView;
import android.graphics.Bitmap;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.camera.CameraSession;
import java.io.File;
import org.telegram.ui.ChatActivity;
import org.telegram.messenger.camera.Size;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

@TargetApi(18)
public class InstantCameraView extends FrameLayout implements NotificationCenterDelegate
{
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
    private int[] cameraTexture;
    private float cameraTextureAlpha;
    private CameraGLThread cameraThread;
    private boolean cancelled;
    private int currentAccount;
    private boolean deviceHasGoodCamera;
    private long duration;
    private TLRPC.InputEncryptedFile encryptedFile;
    private TLRPC.InputFile file;
    private boolean isFrontface;
    private boolean isSecretChat;
    private byte[] iv;
    private byte[] key;
    private Bitmap lastBitmap;
    private float[] mMVPMatrix;
    private float[] mSTMatrix;
    private float[] moldSTMatrix;
    private AnimatorSet muteAnimation;
    private ImageView muteImageView;
    private int[] oldCameraTexture;
    private Paint paint;
    private Size pictureSize;
    private int[] position;
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
    private Runnable timerRunnable;
    private FloatBuffer vertexBuffer;
    private VideoEditedInfo videoEditedInfo;
    private VideoPlayer videoPlayer;
    
    public InstantCameraView(final Context context, final ChatActivity baseFragment) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.isFrontface = true;
        this.position = new int[2];
        this.cameraTexture = new int[1];
        this.oldCameraTexture = new int[1];
        this.cameraTextureAlpha = 1.0f;
        this.timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!InstantCameraView.this.recording) {
                    return;
                }
                final NotificationCenter instance = NotificationCenter.getInstance(InstantCameraView.this.currentAccount);
                final int recordProgressChanged = NotificationCenter.recordProgressChanged;
                final InstantCameraView this$0 = InstantCameraView.this;
                final long l = System.currentTimeMillis() - InstantCameraView.this.recordStartTime;
                this$0.duration = l;
                instance.postNotificationName(recordProgressChanged, l, 0.0);
                AndroidUtilities.runOnUIThread(InstantCameraView.this.timerRunnable, 50L);
            }
        };
        Size aspectRatio;
        if (SharedConfig.roundCamera16to9) {
            aspectRatio = new Size(16, 9);
        }
        else {
            aspectRatio = new Size(4, 3);
        }
        this.aspectRatio = aspectRatio;
        this.mMVPMatrix = new float[16];
        this.mSTMatrix = new float[16];
        this.moldSTMatrix = new float[16];
        this.setOnTouchListener((View$OnTouchListener)new _$$Lambda$InstantCameraView$qN9GPrIe8LvogWTbvas_MPqE9F8(this));
        this.setWillNotDraw(false);
        this.setBackgroundColor(-1073741824);
        this.baseFragment = baseFragment;
        this.isSecretChat = (this.baseFragment.getCurrentEncryptedChat() != null);
        (this.paint = new Paint(1) {
            public void setAlpha(final int alpha) {
                super.setAlpha(alpha);
                InstantCameraView.this.invalidate();
            }
        }).setStyle(Paint$Style.STROKE);
        this.paint.setStrokeCap(Paint$Cap.ROUND);
        this.paint.setStrokeWidth((float)AndroidUtilities.dp(3.0f));
        this.paint.setColor(-1);
        this.rect = new RectF();
        if (Build$VERSION.SDK_INT >= 21) {
            (this.cameraContainer = new FrameLayout(context) {
                public void setAlpha(final float alpha) {
                    super.setAlpha(alpha);
                    InstantCameraView.this.invalidate();
                }
                
                public void setScaleX(final float scaleX) {
                    super.setScaleX(scaleX);
                    InstantCameraView.this.invalidate();
                }
            }).setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @TargetApi(21)
                public void getOutline(final View view, final Outline outline) {
                    final int roundMessageSize = AndroidUtilities.roundMessageSize;
                    outline.setOval(0, 0, roundMessageSize, roundMessageSize);
                }
            });
            this.cameraContainer.setClipToOutline(true);
            this.cameraContainer.setWillNotDraw(false);
        }
        else {
            final Path path = new Path();
            final Paint paint = new Paint(1);
            paint.setColor(-16777216);
            paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
            (this.cameraContainer = new FrameLayout(context) {
                protected void dispatchDraw(final Canvas canvas) {
                    try {
                        super.dispatchDraw(canvas);
                        canvas.drawPath(path, paint);
                    }
                    catch (Exception ex) {}
                }
                
                protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
                    super.onSizeChanged(n, n2, n3, n4);
                    path.reset();
                    final Path val$path = path;
                    final float n5 = (float)(n / 2);
                    val$path.addCircle(n5, (float)(n2 / 2), n5, Path$Direction.CW);
                    path.toggleInverseFillType();
                }
                
                public void setScaleX(final float scaleX) {
                    super.setScaleX(scaleX);
                    InstantCameraView.this.invalidate();
                }
            }).setWillNotDraw(false);
            this.cameraContainer.setLayerType(2, (Paint)null);
        }
        final FrameLayout cameraContainer = this.cameraContainer;
        final int roundMessageSize = AndroidUtilities.roundMessageSize;
        this.addView((View)cameraContainer, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(roundMessageSize, roundMessageSize, 17));
        (this.switchCameraButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.switchCameraButton.setContentDescription((CharSequence)LocaleController.getString("AccDescrSwitchCamera", 2131558478));
        this.addView((View)this.switchCameraButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 83, 20.0f, 0.0f, 0.0f, 14.0f));
        this.switchCameraButton.setOnClickListener((View$OnClickListener)new _$$Lambda$InstantCameraView$S2zQmSsGveVOM2kGAgGGE27kbGY(this));
        (this.muteImageView = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.muteImageView.setImageResource(2131165906);
        this.muteImageView.setAlpha(0.0f);
        this.addView((View)this.muteImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 17));
        ((FrameLayout$LayoutParams)this.muteImageView.getLayoutParams()).topMargin = AndroidUtilities.roundMessageSize / 2 - AndroidUtilities.dp(24.0f);
        (this.textureOverlayView = new BackupImageView(this.getContext())).setRoundRadius(AndroidUtilities.roundMessageSize / 2);
        final BackupImageView textureOverlayView = this.textureOverlayView;
        final int roundMessageSize2 = AndroidUtilities.roundMessageSize;
        this.addView((View)textureOverlayView, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(roundMessageSize2, roundMessageSize2, 17));
        this.setVisibility(4);
    }
    
    private void createCamera(final SurfaceTexture surfaceTexture) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$RBsoQ3f2_7L3ZL_CYLuRpS20Mko(this, surfaceTexture));
    }
    
    private boolean initCamera() {
        final ArrayList<CameraInfo> cameras = CameraController.getInstance().getCameras();
        final int n = 0;
        if (cameras == null) {
            return false;
        }
        CameraInfo cameraInfo = null;
        int index = 0;
        CameraInfo selectedCamera;
        while (true) {
            selectedCamera = cameraInfo;
            if (index >= cameras.size()) {
                break;
            }
            final CameraInfo selectedCamera2 = cameras.get(index);
            if (!selectedCamera2.isFrontface()) {
                cameraInfo = selectedCamera2;
            }
            if ((this.isFrontface && selectedCamera2.isFrontface()) || (!this.isFrontface && !selectedCamera2.isFrontface())) {
                this.selectedCamera = selectedCamera2;
                selectedCamera = cameraInfo;
                break;
            }
            ++index;
            cameraInfo = selectedCamera2;
        }
        if (this.selectedCamera == null) {
            this.selectedCamera = selectedCamera;
        }
        final CameraInfo selectedCamera3 = this.selectedCamera;
        if (selectedCamera3 == null) {
            return false;
        }
        final ArrayList<Size> previewSizes = selectedCamera3.getPreviewSizes();
        final ArrayList<Size> pictureSizes = this.selectedCamera.getPictureSizes();
        this.previewSize = CameraController.chooseOptimalSize(previewSizes, 480, 270, this.aspectRatio);
        this.pictureSize = CameraController.chooseOptimalSize(pictureSizes, 480, 270, this.aspectRatio);
        if (this.previewSize.mWidth != this.pictureSize.mWidth) {
            int index2 = previewSizes.size() - 1;
            int n2 = n;
            int n3;
            while (true) {
                n3 = n2;
                if (index2 < 0) {
                    break;
                }
                final Size previewSize = previewSizes.get(index2);
                int index3 = pictureSizes.size() - 1;
                while (true) {
                    n3 = n2;
                    if (index3 < 0) {
                        break;
                    }
                    final Size pictureSize = pictureSizes.get(index3);
                    final int mWidth = previewSize.mWidth;
                    final Size pictureSize2 = this.pictureSize;
                    if (mWidth >= pictureSize2.mWidth) {
                        final int mHeight = previewSize.mHeight;
                        if (mHeight >= pictureSize2.mHeight && mWidth == pictureSize.mWidth && mHeight == pictureSize.mHeight) {
                            this.previewSize = previewSize;
                            this.pictureSize = pictureSize;
                            n3 = 1;
                            break;
                        }
                    }
                    --index3;
                }
                if (n3 != 0) {
                    break;
                }
                --index2;
                n2 = n3;
            }
            if (n3 == 0) {
                int n4;
                for (int i = previewSizes.size() - 1; i >= 0; --i, n3 = n4) {
                    final Size previewSize2 = previewSizes.get(i);
                    int index4 = pictureSizes.size() - 1;
                    while (true) {
                        n4 = n3;
                        if (index4 < 0) {
                            break;
                        }
                        final Size pictureSize3 = pictureSizes.get(index4);
                        final int mWidth2 = previewSize2.mWidth;
                        if (mWidth2 >= 240) {
                            final int mHeight2 = previewSize2.mHeight;
                            if (mHeight2 >= 240 && mWidth2 == pictureSize3.mWidth && mHeight2 == pictureSize3.mHeight) {
                                this.previewSize = previewSize2;
                                this.pictureSize = pictureSize3;
                                n4 = 1;
                                break;
                            }
                        }
                        --index4;
                    }
                    if (n4 != 0) {
                        break;
                    }
                }
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("preview w = ");
            sb.append(this.previewSize.mWidth);
            sb.append(" h = ");
            sb.append(this.previewSize.mHeight);
            FileLog.d(sb.toString());
        }
        return true;
    }
    
    private int loadShader(int n, final String s) {
        final int glCreateShader = GLES20.glCreateShader(n);
        GLES20.glShaderSource(glCreateShader, s);
        GLES20.glCompileShader(glCreateShader);
        final int[] array = { 0 };
        GLES20.glGetShaderiv(glCreateShader, 35713, array, 0);
        n = glCreateShader;
        if (array[0] == 0) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e(GLES20.glGetShaderInfoLog(glCreateShader));
            }
            GLES20.glDeleteShader(glCreateShader);
            n = 0;
        }
        return n;
    }
    
    private void saveLastCameraBitmap() {
        if (this.textureView.getBitmap() == null) {
            return;
        }
        this.lastBitmap = Bitmap.createScaledBitmap(this.textureView.getBitmap(), 80, 80, true);
        final Bitmap lastBitmap = this.lastBitmap;
        if (lastBitmap == null) {
            return;
        }
        Utilities.blurBitmap(lastBitmap, 7, 1, lastBitmap.getWidth(), this.lastBitmap.getHeight(), this.lastBitmap.getRowBytes());
        try {
            this.lastBitmap.compress(Bitmap$CompressFormat.JPEG, 87, (OutputStream)new FileOutputStream(new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg")));
        }
        catch (Throwable t) {}
    }
    
    private void startProgressTimer() {
        final Timer progressTimer = this.progressTimer;
        if (progressTimer != null) {
            try {
                progressTimer.cancel();
                this.progressTimer = null;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        (this.progressTimer = new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$10$Q79AnmgYDpgQ0WR_lA8rbDjA_6E(this));
            }
        }, 0L, 17L);
    }
    
    private void stopProgressTimer() {
        final Timer progressTimer = this.progressTimer;
        if (progressTimer != null) {
            try {
                progressTimer.cancel();
                this.progressTimer = null;
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    private void switchCamera() {
        this.saveLastCameraBitmap();
        final Bitmap lastBitmap = this.lastBitmap;
        if (lastBitmap != null) {
            this.textureOverlayView.setImageBitmap(lastBitmap);
            this.textureOverlayView.animate().setDuration(120L).alpha(1.0f).setInterpolator((TimeInterpolator)new DecelerateInterpolator()).start();
        }
        final CameraSession cameraSession = this.cameraSession;
        if (cameraSession != null) {
            cameraSession.destroy();
            CameraController.getInstance().close(this.cameraSession, null, null);
            this.cameraSession = null;
        }
        this.isFrontface ^= true;
        this.initCamera();
        this.cameraReady = false;
        this.cameraThread.reinitForNewCamera();
    }
    
    public void cancel() {
        this.stopProgressTimer();
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        if (this.textureView == null) {
            return;
        }
        this.cancelled = true;
        this.recording = false;
        AndroidUtilities.cancelRunOnUIThread(this.timerRunnable);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recordStopped, 0);
        if (this.cameraThread != null) {
            this.saveLastCameraBitmap();
            this.cameraThread.shutdown(0);
            this.cameraThread = null;
        }
        final File cameraFile = this.cameraFile;
        if (cameraFile != null) {
            cameraFile.delete();
            this.cameraFile = null;
        }
        this.startAnimation(false);
    }
    
    public void changeVideoPreviewState(final int n, final float n2) {
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer == null) {
            return;
        }
        if (n == 0) {
            this.startProgressTimer();
            this.videoPlayer.play();
        }
        else if (n == 1) {
            this.stopProgressTimer();
            this.videoPlayer.pause();
        }
        else if (n == 2) {
            videoPlayer.seekTo((long)(n2 * videoPlayer.getDuration()));
        }
    }
    
    public void destroy(final boolean b, final Runnable runnable) {
        final CameraSession cameraSession = this.cameraSession;
        if (cameraSession != null) {
            cameraSession.destroy();
            final CameraController instance = CameraController.getInstance();
            final CameraSession cameraSession2 = this.cameraSession;
            CountDownLatch countDownLatch;
            if (!b) {
                countDownLatch = new CountDownLatch(1);
            }
            else {
                countDownLatch = null;
            }
            instance.close(cameraSession2, countDownLatch, runnable);
        }
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.recordProgressChanged) {
            final long longValue = (long)array[0];
            this.progress = longValue / 60000.0f;
            this.recordedTime = longValue;
            this.invalidate();
        }
        else if (n == NotificationCenter.FileDidUpload) {
            final String anObject = (String)array[0];
            final File cameraFile = this.cameraFile;
            if (cameraFile != null && cameraFile.getAbsolutePath().equals(anObject)) {
                this.file = (TLRPC.InputFile)array[1];
                this.encryptedFile = (TLRPC.InputEncryptedFile)array[2];
                this.size = (long)array[5];
                if (this.encryptedFile != null) {
                    this.key = (byte[])array[3];
                    this.iv = (byte[])array[4];
                }
            }
        }
    }
    
    public FrameLayout getCameraContainer() {
        return this.cameraContainer;
    }
    
    public Rect getCameraRect() {
        this.cameraContainer.getLocationOnScreen(this.position);
        final int[] position = this.position;
        return new Rect((float)position[0], (float)position[1], (float)this.cameraContainer.getWidth(), (float)this.cameraContainer.getHeight());
    }
    
    public View getMuteImageView() {
        return (View)this.muteImageView;
    }
    
    public Paint getPaint() {
        return this.paint;
    }
    
    public View getSwitchButtonView() {
        return (View)this.switchCameraButton;
    }
    
    public void hideCamera(final boolean b) {
        this.destroy(b, null);
        this.cameraContainer.removeView((View)this.textureView);
        this.cameraContainer.setTranslationX(0.0f);
        this.cameraContainer.setTranslationY(0.0f);
        this.textureOverlayView.setTranslationX(0.0f);
        this.textureOverlayView.setTranslationY(0.0f);
        this.textureView = null;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileDidUpload);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileDidUpload);
    }
    
    protected void onDraw(final Canvas canvas) {
        final float x = this.cameraContainer.getX();
        final float y = this.cameraContainer.getY();
        this.rect.set(x - AndroidUtilities.dp(8.0f), y - AndroidUtilities.dp(8.0f), this.cameraContainer.getMeasuredWidth() + x + AndroidUtilities.dp(8.0f), this.cameraContainer.getMeasuredHeight() + y + AndroidUtilities.dp(8.0f));
        final float progress = this.progress;
        if (progress != 0.0f) {
            canvas.drawArc(this.rect, -90.0f, progress * 360.0f, false, this.paint);
        }
        if (Theme.chat_roundVideoShadow != null) {
            final int n = (int)x - AndroidUtilities.dp(3.0f);
            final int n2 = (int)y - AndroidUtilities.dp(2.0f);
            canvas.save();
            canvas.scale(this.cameraContainer.getScaleX(), this.cameraContainer.getScaleY(), (float)(AndroidUtilities.roundMessageSize / 2 + n + AndroidUtilities.dp(3.0f)), (float)(AndroidUtilities.roundMessageSize / 2 + n2 + AndroidUtilities.dp(3.0f)));
            Theme.chat_roundVideoShadow.setAlpha((int)(this.cameraContainer.getAlpha() * 255.0f));
            Theme.chat_roundVideoShadow.setBounds(n, n2, AndroidUtilities.roundMessageSize + n + AndroidUtilities.dp(6.0f), AndroidUtilities.roundMessageSize + n2 + AndroidUtilities.dp(6.0f));
            Theme.chat_roundVideoShadow.draw(canvas);
            canvas.restore();
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(motionEvent);
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (this.getVisibility() != 0) {
            this.cameraContainer.setTranslationY((float)(this.getMeasuredHeight() / 2));
            this.textureOverlayView.setTranslationY((float)(this.getMeasuredHeight() / 2));
        }
    }
    
    public void send(int n) {
        if (this.textureView == null) {
            return;
        }
        this.stopProgressTimer();
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        if (n == 4) {
            if (this.videoEditedInfo.needConvert()) {
                this.file = null;
                this.encryptedFile = null;
                this.key = null;
                this.iv = null;
                final VideoEditedInfo videoEditedInfo = this.videoEditedInfo;
                final double v = (double)videoEditedInfo.estimatedDuration;
                long startTime = videoEditedInfo.startTime;
                if (startTime < 0L) {
                    startTime = 0L;
                }
                final VideoEditedInfo videoEditedInfo2 = this.videoEditedInfo;
                long n2 = videoEditedInfo2.endTime;
                if (n2 < 0L) {
                    n2 = videoEditedInfo2.estimatedDuration;
                }
                final VideoEditedInfo videoEditedInfo3 = this.videoEditedInfo;
                videoEditedInfo3.estimatedDuration = n2 - startTime;
                final double v2 = (double)this.size;
                final double v3 = (double)videoEditedInfo3.estimatedDuration;
                Double.isNaN(v3);
                Double.isNaN(v);
                final double n3 = v3 / v;
                Double.isNaN(v2);
                videoEditedInfo3.estimatedSize = Math.max(1L, (long)(v2 * n3));
                final VideoEditedInfo videoEditedInfo4 = this.videoEditedInfo;
                videoEditedInfo4.bitrate = 400000;
                final long startTime2 = videoEditedInfo4.startTime;
                if (startTime2 > 0L) {
                    videoEditedInfo4.startTime = startTime2 * 1000L;
                }
                final VideoEditedInfo videoEditedInfo5 = this.videoEditedInfo;
                final long endTime = videoEditedInfo5.endTime;
                if (endTime > 0L) {
                    videoEditedInfo5.endTime = endTime * 1000L;
                }
                FileLoader.getInstance(this.currentAccount).cancelUploadFile(this.cameraFile.getAbsolutePath(), false);
            }
            else {
                this.videoEditedInfo.estimatedSize = Math.max(1L, this.size);
            }
            final VideoEditedInfo videoEditedInfo6 = this.videoEditedInfo;
            videoEditedInfo6.file = this.file;
            videoEditedInfo6.encryptedFile = this.encryptedFile;
            videoEditedInfo6.key = this.key;
            videoEditedInfo6.iv = this.iv;
            this.baseFragment.sendMedia(new MediaController.PhotoEntry(0, 0, 0L, this.cameraFile.getAbsolutePath(), 0, true), this.videoEditedInfo);
        }
        else {
            this.cancelled = (this.recordedTime < 800L);
            this.recording = false;
            AndroidUtilities.cancelRunOnUIThread(this.timerRunnable);
            if (this.cameraThread != null) {
                final NotificationCenter instance = NotificationCenter.getInstance(this.currentAccount);
                final int recordStopped = NotificationCenter.recordStopped;
                final boolean cancelled = this.cancelled;
                final int n4 = 2;
                int i;
                if (!cancelled && n == 3) {
                    i = 2;
                }
                else {
                    i = 0;
                }
                instance.postNotificationName(recordStopped, i);
                if (this.cancelled) {
                    n = 0;
                }
                else if (n == 3) {
                    n = n4;
                }
                else {
                    n = 1;
                }
                this.saveLastCameraBitmap();
                this.cameraThread.shutdown(n);
                this.cameraThread = null;
            }
            if (this.cancelled) {
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.audioRecordTooShort, true);
                this.startAnimation(false);
            }
        }
    }
    
    @Keep
    public void setAlpha(final float n) {
        ((ColorDrawable)this.getBackground()).setAlpha((int)(n * 192.0f));
        this.invalidate();
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        this.setAlpha(0.0f);
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
            final FrameLayout cameraContainer = this.cameraContainer;
            cameraContainer.setPivotX((float)(cameraContainer.getMeasuredWidth() / 2));
            final FrameLayout cameraContainer2 = this.cameraContainer;
            cameraContainer2.setPivotY((float)(cameraContainer2.getMeasuredHeight() / 2));
            final BackupImageView textureOverlayView = this.textureOverlayView;
            textureOverlayView.setPivotX((float)(textureOverlayView.getMeasuredWidth() / 2));
            final BackupImageView textureOverlayView2 = this.textureOverlayView;
            textureOverlayView2.setPivotY((float)(textureOverlayView2.getMeasuredHeight() / 2));
        }
        while (true) {
            if (visibility == 0) {
                try {
                    ((Activity)this.getContext()).getWindow().addFlags(128);
                    return;
                    ((Activity)this.getContext()).getWindow().clearFlags(128);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                return;
            }
            continue;
        }
    }
    
    public void showCamera() {
        if (this.textureView != null) {
            return;
        }
        this.switchCameraButton.setImageResource(2131165335);
        this.textureOverlayView.setAlpha(1.0f);
        if (this.lastBitmap == null) {
            try {
                this.lastBitmap = BitmapFactory.decodeFile(new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg").getAbsolutePath());
            }
            catch (Throwable t) {}
        }
        final Bitmap lastBitmap = this.lastBitmap;
        if (lastBitmap != null) {
            this.textureOverlayView.setImageBitmap(lastBitmap);
        }
        else {
            this.textureOverlayView.setImageResource(2131165477);
        }
        this.cameraReady = false;
        this.isFrontface = true;
        this.selectedCamera = null;
        this.recordedTime = 0L;
        this.progress = 0.0f;
        this.cancelled = false;
        this.file = null;
        this.encryptedFile = null;
        this.key = null;
        this.iv = null;
        if (!this.initCamera()) {
            return;
        }
        MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
        final File directory = FileLoader.getDirectory(4);
        final StringBuilder sb = new StringBuilder();
        sb.append(SharedConfig.getLastLocalId());
        sb.append(".mp4");
        this.cameraFile = new File(directory, sb.toString());
        SharedConfig.saveConfig();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("show round camera");
        }
        (this.textureView = new TextureView(this.getContext())).setSurfaceTextureListener((TextureView$SurfaceTextureListener)new TextureView$SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("camera surface available");
                }
                if (InstantCameraView.this.cameraThread == null && surfaceTexture != null) {
                    if (InstantCameraView.this.cancelled) {
                        return;
                    }
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("start create thread");
                    }
                    final InstantCameraView this$0 = InstantCameraView.this;
                    this$0.cameraThread = this$0.new CameraGLThread(surfaceTexture, n, n2);
                }
            }
            
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                if (InstantCameraView.this.cameraThread != null) {
                    InstantCameraView.this.cameraThread.shutdown(0);
                    InstantCameraView.this.cameraThread = null;
                }
                if (InstantCameraView.this.cameraSession != null) {
                    CameraController.getInstance().close(InstantCameraView.this.cameraSession, null, null);
                }
                return true;
            }
            
            public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            }
            
            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
            }
        });
        this.cameraContainer.addView((View)this.textureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.setVisibility(0);
        this.startAnimation(true);
    }
    
    public void startAnimation(final boolean b) {
        final AnimatorSet animatorSet = this.animatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        final PipRoundVideoView instance = PipRoundVideoView.getInstance();
        if (instance != null) {
            instance.showTemporary(b ^ true);
        }
        this.animatorSet = new AnimatorSet();
        final AnimatorSet animatorSet2 = this.animatorSet;
        final float n = 1.0f;
        final float n2 = 0.0f;
        float n3;
        if (b) {
            n3 = 1.0f;
        }
        else {
            n3 = 0.0f;
        }
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { n3 });
        final ImageView switchCameraButton = this.switchCameraButton;
        float n4;
        if (b) {
            n4 = 1.0f;
        }
        else {
            n4 = 0.0f;
        }
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat((Object)switchCameraButton, "alpha", new float[] { n4 });
        final ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat((Object)this.muteImageView, "alpha", new float[] { 0.0f });
        final Paint paint = this.paint;
        int n5;
        if (b) {
            n5 = 255;
        }
        else {
            n5 = 0;
        }
        final ObjectAnimator ofInt = ObjectAnimator.ofInt((Object)paint, "alpha", new int[] { n5 });
        final FrameLayout cameraContainer = this.cameraContainer;
        float n6;
        if (b) {
            n6 = 1.0f;
        }
        else {
            n6 = 0.0f;
        }
        final ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat((Object)cameraContainer, "alpha", new float[] { n6 });
        final FrameLayout cameraContainer2 = this.cameraContainer;
        float n7;
        if (b) {
            n7 = 1.0f;
        }
        else {
            n7 = 0.1f;
        }
        final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat((Object)cameraContainer2, "scaleX", new float[] { n7 });
        final FrameLayout cameraContainer3 = this.cameraContainer;
        float n8;
        if (b) {
            n8 = 1.0f;
        }
        else {
            n8 = 0.1f;
        }
        final ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat((Object)cameraContainer3, "scaleY", new float[] { n8 });
        final FrameLayout cameraContainer4 = this.cameraContainer;
        float n9;
        if (b) {
            n9 = (float)(this.getMeasuredHeight() / 2);
        }
        else {
            n9 = 0.0f;
        }
        float n10;
        if (b) {
            n10 = 0.0f;
        }
        else {
            n10 = (float)(this.getMeasuredHeight() / 2);
        }
        final ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat((Object)cameraContainer4, "translationY", new float[] { n9, n10 });
        final BackupImageView textureOverlayView = this.textureOverlayView;
        float n11;
        if (b) {
            n11 = 1.0f;
        }
        else {
            n11 = 0.0f;
        }
        final ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat((Object)textureOverlayView, "alpha", new float[] { n11 });
        final BackupImageView textureOverlayView2 = this.textureOverlayView;
        float n12;
        if (b) {
            n12 = 1.0f;
        }
        else {
            n12 = 0.1f;
        }
        final ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat((Object)textureOverlayView2, "scaleX", new float[] { n12 });
        final BackupImageView textureOverlayView3 = this.textureOverlayView;
        float n13;
        if (b) {
            n13 = n;
        }
        else {
            n13 = 0.1f;
        }
        final ObjectAnimator ofFloat10 = ObjectAnimator.ofFloat((Object)textureOverlayView3, "scaleY", new float[] { n13 });
        final BackupImageView textureOverlayView4 = this.textureOverlayView;
        float n14;
        if (b) {
            n14 = (float)(this.getMeasuredHeight() / 2);
        }
        else {
            n14 = 0.0f;
        }
        float n15;
        if (b) {
            n15 = n2;
        }
        else {
            n15 = (float)(this.getMeasuredHeight() / 2);
        }
        animatorSet2.playTogether(new Animator[] { (Animator)ofFloat, (Animator)ofFloat2, (Animator)ofFloat3, (Animator)ofInt, (Animator)ofFloat4, (Animator)ofFloat5, (Animator)ofFloat6, (Animator)ofFloat7, (Animator)ofFloat8, (Animator)ofFloat9, (Animator)ofFloat10, (Animator)ObjectAnimator.ofFloat((Object)textureOverlayView4, "translationY", new float[] { n14, n15 }) });
        if (!b) {
            this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (animator.equals(InstantCameraView.this.animatorSet)) {
                        InstantCameraView.this.hideCamera(true);
                        InstantCameraView.this.setVisibility(4);
                    }
                }
            });
        }
        this.animatorSet.setDuration(180L);
        this.animatorSet.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
        this.animatorSet.start();
    }
    
    private class AudioBufferInfo
    {
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
    }
    
    public class CameraGLThread extends DispatchQueue
    {
        private final int DO_REINIT_MESSAGE;
        private final int DO_RENDER_MESSAGE;
        private final int DO_SETSESSION_MESSAGE;
        private final int DO_SHUTDOWN_MESSAGE;
        private final int EGL_CONTEXT_CLIENT_VERSION;
        private final int EGL_OPENGL_ES2_BIT;
        private Integer cameraId;
        private SurfaceTexture cameraSurface;
        private CameraSession currentSession;
        private int drawProgram;
        private EGL10 egl10;
        private EGLConfig eglConfig;
        private EGLContext eglContext;
        private EGLDisplay eglDisplay;
        private EGLSurface eglSurface;
        private GL gl;
        private boolean initied;
        private int positionHandle;
        private boolean recording;
        private int rotationAngle;
        private SurfaceTexture surfaceTexture;
        private int textureHandle;
        private int textureMatrixHandle;
        private int vertexMatrixHandle;
        private VideoRecorder videoEncoder;
        
        public CameraGLThread(final SurfaceTexture surfaceTexture, int n, final int n2) {
            super("CameraGLThread");
            this.EGL_CONTEXT_CLIENT_VERSION = 12440;
            this.EGL_OPENGL_ES2_BIT = 4;
            this.DO_RENDER_MESSAGE = 0;
            this.DO_SHUTDOWN_MESSAGE = 1;
            this.DO_REINIT_MESSAGE = 2;
            this.DO_SETSESSION_MESSAGE = 3;
            this.cameraId = 0;
            this.surfaceTexture = surfaceTexture;
            final int width = InstantCameraView.this.previewSize.getWidth();
            final int height = InstantCameraView.this.previewSize.getHeight();
            final float n3 = (float)n;
            final float n4 = n3 / Math.min(width, height);
            n = (int)(width * n4);
            final int n5 = (int)(height * n4);
            if (n > n5) {
                InstantCameraView.this.scaleX = 1.0f;
                InstantCameraView.this.scaleY = n / (float)n2;
            }
            else {
                InstantCameraView.this.scaleX = n5 / n3;
                InstantCameraView.this.scaleY = 1.0f;
            }
        }
        
        private boolean initGL() {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("start init gl");
            }
            this.egl10 = (EGL10)EGLContext.getEGL();
            this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            final EGLDisplay eglDisplay = this.eglDisplay;
            if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("eglGetDisplay failed ");
                    sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb.toString());
                }
                this.finish();
                return false;
            }
            if (!this.egl10.eglInitialize(eglDisplay, new int[2])) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("eglInitialize failed ");
                    sb2.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb2.toString());
                }
                this.finish();
                return false;
            }
            final int[] array = { 0 };
            final EGLConfig[] array2 = { null };
            if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[] { 12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12325, 0, 12326, 0, 12344 }, array2, 1, array)) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("eglChooseConfig failed ");
                    sb3.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb3.toString());
                }
                this.finish();
                return false;
            }
            if (array[0] <= 0) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("eglConfig not initialized");
                }
                this.finish();
                return false;
            }
            this.eglConfig = array2[0];
            this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[] { 12440, 2, 12344 });
            if (this.eglContext == null) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("eglCreateContext failed ");
                    sb4.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb4.toString());
                }
                this.finish();
                return false;
            }
            final SurfaceTexture surfaceTexture = this.surfaceTexture;
            if (!(surfaceTexture instanceof SurfaceTexture)) {
                this.finish();
                return false;
            }
            this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, (Object)surfaceTexture, (int[])null);
            final EGLSurface eglSurface = this.eglSurface;
            if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("createWindowSurface failed ");
                    sb5.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb5.toString());
                }
                this.finish();
                return false;
            }
            if (!this.egl10.eglMakeCurrent(this.eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("eglMakeCurrent failed ");
                    sb6.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                    FileLog.e(sb6.toString());
                }
                this.finish();
                return false;
            }
            this.gl = this.eglContext.getGL();
            final float n = 1.0f / InstantCameraView.this.scaleX / 2.0f;
            final float n2 = 1.0f / InstantCameraView.this.scaleY / 2.0f;
            final float[] array3;
            final float[] src = array3 = new float[12];
            array3[1] = (array3[0] = -1.0f);
            array3[2] = 0.0f;
            array3[3] = 1.0f;
            array3[4] = -1.0f;
            array3[5] = 0.0f;
            array3[6] = -1.0f;
            array3[7] = 1.0f;
            array3[8] = 0.0f;
            array3[10] = (array3[9] = 1.0f);
            array3[11] = 0.0f;
            final float[] src2 = new float[8];
            final float n3 = 0.5f - n;
            src2[0] = n3;
            final float n4 = 0.5f - n2;
            src2[1] = n4;
            final float n5 = n + 0.5f;
            src2[2] = n5;
            src2[3] = n4;
            src2[4] = n3;
            final float n6 = n2 + 0.5f;
            src2[5] = n6;
            src2[6] = n5;
            src2[7] = n6;
            this.videoEncoder = new VideoRecorder();
            InstantCameraView.this.vertexBuffer = ByteBuffer.allocateDirect(src.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            InstantCameraView.this.vertexBuffer.put(src).position(0);
            InstantCameraView.this.textureBuffer = ByteBuffer.allocateDirect(src2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            InstantCameraView.this.textureBuffer.put(src2).position(0);
            Matrix.setIdentityM(InstantCameraView.this.mSTMatrix, 0);
            final int access$1800 = InstantCameraView.this.loadShader(35633, "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n");
            final int access$1801 = InstantCameraView.this.loadShader(35632, "#extension GL_OES_EGL_image_external : require\nprecision lowp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
            if (access$1800 != 0 && access$1801 != 0) {
                GLES20.glAttachShader(this.drawProgram = GLES20.glCreateProgram(), access$1800);
                GLES20.glAttachShader(this.drawProgram, access$1801);
                GLES20.glLinkProgram(this.drawProgram);
                final int[] array4 = { 0 };
                GLES20.glGetProgramiv(this.drawProgram, 35714, array4, 0);
                if (array4[0] == 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("failed link shader");
                    }
                    GLES20.glDeleteProgram(this.drawProgram);
                    this.drawProgram = 0;
                }
                else {
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
                (this.cameraSurface = new SurfaceTexture(InstantCameraView.this.cameraTexture[0])).setOnFrameAvailableListener((SurfaceTexture$OnFrameAvailableListener)new _$$Lambda$InstantCameraView$CameraGLThread$owshQSrJ0yE90p_o4TfGsfbd_z0(this));
                InstantCameraView.this.createCamera(this.cameraSurface);
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("gl initied");
                }
                return true;
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("failed creating shader");
            }
            this.finish();
            return false;
        }
        
        private void onDraw(final Integer n) {
            if (!this.initied) {
                return;
            }
            if (!this.eglContext.equals(this.egl10.eglGetCurrentContext()) || !this.eglSurface.equals(this.egl10.eglGetCurrentSurface(12377))) {
                final EGL10 egl10 = this.egl10;
                final EGLDisplay eglDisplay = this.eglDisplay;
                final EGLSurface eglSurface = this.eglSurface;
                if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("eglMakeCurrent failed ");
                        sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.e(sb.toString());
                    }
                    return;
                }
            }
            this.cameraSurface.updateTexImage();
            if (!this.recording) {
                this.videoEncoder.startRecording(InstantCameraView.this.cameraFile, EGL14.eglGetCurrentContext());
                this.recording = true;
                final int currentOrientation = this.currentSession.getCurrentOrientation();
                if (currentOrientation == 90 || currentOrientation == 270) {
                    final float access$1200 = InstantCameraView.this.scaleX;
                    final InstantCameraView this$0 = InstantCameraView.this;
                    this$0.scaleX = this$0.scaleY;
                    InstantCameraView.this.scaleY = access$1200;
                }
            }
            this.videoEncoder.frameAvailable(this.cameraSurface, n, System.nanoTime());
            this.cameraSurface.getTransformMatrix(InstantCameraView.this.mSTMatrix);
            GLES20.glUseProgram(this.drawProgram);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
            GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 12, (Buffer)InstantCameraView.this.vertexBuffer);
            GLES20.glEnableVertexAttribArray(this.positionHandle);
            GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 8, (Buffer)InstantCameraView.this.textureBuffer);
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
        
        public void finish() {
            if (this.eglSurface != null) {
                final EGL10 egl10 = this.egl10;
                final EGLDisplay eglDisplay = this.eglDisplay;
                final EGLSurface egl_NO_SURFACE = EGL10.EGL_NO_SURFACE;
                egl10.eglMakeCurrent(eglDisplay, egl_NO_SURFACE, egl_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
                this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
                this.eglSurface = null;
            }
            final EGLContext eglContext = this.eglContext;
            if (eglContext != null) {
                this.egl10.eglDestroyContext(this.eglDisplay, eglContext);
                this.eglContext = null;
            }
            final EGLDisplay eglDisplay2 = this.eglDisplay;
            if (eglDisplay2 != null) {
                this.egl10.eglTerminate(eglDisplay2);
                this.eglDisplay = null;
            }
        }
        
        @Override
        public void handleMessage(final Message message) {
            final int what = message.what;
            if (what != 0) {
                if (what != 1) {
                    if (what != 2) {
                        if (what == 3) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("set gl rednderer session");
                            }
                            final CameraSession currentSession = (CameraSession)message.obj;
                            final CameraSession currentSession2 = this.currentSession;
                            if (currentSession2 == currentSession) {
                                this.rotationAngle = currentSession2.getWorldAngle();
                                Matrix.setIdentityM(InstantCameraView.this.mMVPMatrix, 0);
                                if (this.rotationAngle != 0) {
                                    Matrix.rotateM(InstantCameraView.this.mMVPMatrix, 0, (float)this.rotationAngle, 0.0f, 0.0f, 1.0f);
                                }
                            }
                            else {
                                this.currentSession = currentSession;
                            }
                        }
                    }
                    else {
                        final EGL10 egl10 = this.egl10;
                        final EGLDisplay eglDisplay = this.eglDisplay;
                        final EGLSurface eglSurface = this.eglSurface;
                        if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                            if (BuildVars.LOGS_ENABLED) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("eglMakeCurrent failed ");
                                sb.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                FileLog.d(sb.toString());
                            }
                            return;
                        }
                        final SurfaceTexture cameraSurface = this.cameraSurface;
                        if (cameraSurface != null) {
                            cameraSurface.getTransformMatrix(InstantCameraView.this.moldSTMatrix);
                            this.cameraSurface.setOnFrameAvailableListener((SurfaceTexture$OnFrameAvailableListener)null);
                            this.cameraSurface.release();
                            InstantCameraView.this.oldCameraTexture[0] = InstantCameraView.this.cameraTexture[0];
                            InstantCameraView.this.cameraTextureAlpha = 0.0f;
                            InstantCameraView.this.cameraTexture[0] = 0;
                        }
                        ++this.cameraId;
                        InstantCameraView.this.cameraReady = false;
                        GLES20.glGenTextures(1, InstantCameraView.this.cameraTexture, 0);
                        GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
                        GLES20.glTexParameteri(36197, 10241, 9729);
                        GLES20.glTexParameteri(36197, 10240, 9729);
                        GLES20.glTexParameteri(36197, 10242, 33071);
                        GLES20.glTexParameteri(36197, 10243, 33071);
                        (this.cameraSurface = new SurfaceTexture(InstantCameraView.this.cameraTexture[0])).setOnFrameAvailableListener((SurfaceTexture$OnFrameAvailableListener)new _$$Lambda$InstantCameraView$CameraGLThread$8CYV7PTMH7zxhUymqIagAcuf5w8(this));
                        InstantCameraView.this.createCamera(this.cameraSurface);
                    }
                }
                else {
                    this.finish();
                    if (this.recording) {
                        this.videoEncoder.stopRecording(message.arg1);
                    }
                    final Looper myLooper = Looper.myLooper();
                    if (myLooper != null) {
                        myLooper.quit();
                    }
                }
            }
            else {
                this.onDraw((Integer)message.obj);
            }
        }
        
        public void reinitForNewCamera() {
            final Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                this.sendMessage(handler.obtainMessage(2), 0);
            }
        }
        
        public void requestRender() {
            final Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                this.sendMessage(handler.obtainMessage(0, (Object)this.cameraId), 0);
            }
        }
        
        @Override
        public void run() {
            this.initied = this.initGL();
            super.run();
        }
        
        public void setCurrentSession(final CameraSession cameraSession) {
            final Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                this.sendMessage(handler.obtainMessage(3, (Object)cameraSession), 0);
            }
        }
        
        public void shutdown(final int n) {
            final Handler handler = InstantCameraView.this.getHandler();
            if (handler != null) {
                this.sendMessage(handler.obtainMessage(1, n, 0), 0);
            }
        }
    }
    
    private static class EncoderHandler extends Handler
    {
        private WeakReference<VideoRecorder> mWeakEncoder;
        
        public EncoderHandler(final VideoRecorder referent) {
            this.mWeakEncoder = new WeakReference<VideoRecorder>(referent);
        }
        
        public void exit() {
            Looper.myLooper().quit();
        }
        
        public void handleMessage(final Message message) {
            final int what = message.what;
            final Object obj = message.obj;
            final VideoRecorder videoRecorder = this.mWeakEncoder.get();
            if (videoRecorder == null) {
                return;
            }
            if (what != 0) {
                if (what != 1) {
                    if (what != 2) {
                        if (what == 3) {
                            videoRecorder.handleAudioFrameAvailable((AudioBufferInfo)message.obj);
                        }
                    }
                    else {
                        videoRecorder.handleVideoFrameAvailable((long)message.arg1 << 32 | ((long)message.arg2 & 0xFFFFFFFFL), (Integer)message.obj);
                    }
                }
                else {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("stop encoder");
                    }
                    videoRecorder.handleStopRecording(message.arg1);
                }
            }
            else {
                try {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.e("start encoder");
                    }
                    videoRecorder.prepareEncoder();
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                    videoRecorder.handleStopRecording(0);
                    Looper.myLooper().quit();
                }
            }
        }
    }
    
    private class VideoRecorder implements Runnable
    {
        private static final String AUDIO_MIME_TYPE = "audio/mp4a-latm";
        private static final int FRAME_RATE = 30;
        private static final int IFRAME_INTERVAL = 1;
        private static final String VIDEO_MIME_TYPE = "video/avc";
        private int alphaHandle;
        private MediaCodec$BufferInfo audioBufferInfo;
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
        private android.opengl.EGLConfig eglConfig;
        private android.opengl.EGLContext eglContext;
        private android.opengl.EGLDisplay eglDisplay;
        private android.opengl.EGLSurface eglSurface;
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
        private android.opengl.EGLContext sharedEglContext;
        private boolean skippedFirst;
        private long skippedTime;
        private Surface surface;
        private final Object sync;
        private int textureHandle;
        private int textureMatrixHandle;
        private int vertexMatrixHandle;
        private int videoBitrate;
        private MediaCodec$BufferInfo videoBufferInfo;
        private boolean videoConvertFirstWrite;
        private MediaCodec videoEncoder;
        private File videoFile;
        private long videoFirst;
        private int videoHeight;
        private long videoLast;
        private int videoTrackIndex;
        private int videoWidth;
        private int zeroTimeStamps;
        
        private VideoRecorder() {
            this.videoConvertFirstWrite = true;
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            this.buffersToWrite = new ArrayList<AudioBufferInfo>();
            this.videoTrackIndex = -5;
            this.audioTrackIndex = -5;
            this.audioStartTime = -1L;
            this.currentTimestamp = 0L;
            this.lastTimestamp = -1L;
            this.sync = new Object();
            this.videoFirst = -1L;
            this.audioFirst = -1L;
            this.lastCameraId = 0;
            this.buffers = new ArrayBlockingQueue<AudioBufferInfo>(10);
            this.recorderRunnable = new Runnable() {
                @Override
                public void run() {
                    long n = -1L;
                    int n2 = 0;
                    int n3;
                    AudioBufferInfo e;
                    int results;
                    long n4;
                    long n5;
                    int read;
                    Label_0397_Outer:Block_17_Outer:
                    while (true) {
                        Label_0377: {
                            if (n2 != 0) {
                                break Label_0377;
                            }
                            n3 = n2;
                            if (!VideoRecorder.this.running) {
                                n3 = n2;
                                if (VideoRecorder.this.audioRecorder.getRecordingState() != 1) {
                                    try {
                                        VideoRecorder.this.audioRecorder.stop();
                                    }
                                    catch (Exception ex2) {
                                        n2 = 1;
                                    }
                                    n3 = n2;
                                    if (VideoRecorder.this.sendWhenDone == 0) {
                                        break Label_0377;
                                    }
                                }
                            }
                            if (VideoRecorder.this.buffers.isEmpty()) {
                                e = new AudioBufferInfo();
                            }
                            else {
                                e = VideoRecorder.this.buffers.poll();
                            }
                            e.lastWroteBuffer = 0;
                            e.results = 10;
                            results = 0;
                            while (true) {
                                n4 = n;
                                if (results >= 10) {
                                    break;
                                }
                                n5 = n;
                                if (n == -1L) {
                                    n5 = System.nanoTime() / 1000L;
                                }
                                read = VideoRecorder.this.audioRecorder.read(e.buffer, results * 2048, 2048);
                                if (read <= 0) {
                                    e.results = results;
                                    n4 = n5;
                                    if (!VideoRecorder.this.running) {
                                        e.last = true;
                                        n4 = n5;
                                        break;
                                    }
                                    break;
                                }
                                else {
                                    e.offset[results] = n5;
                                    e.read[results] = read;
                                    n = n5 + read * 1000000 / 44100 / 2;
                                    ++results;
                                }
                            }
                            n = n4;
                            Label_0322: {
                                if (e.results >= 0 || e.last) {
                                    break Label_0322;
                                }
                                if (!VideoRecorder.this.running) {
                                    n2 = 1;
                                    continue;
                                }
                                try {
                                    VideoRecorder.this.buffers.put(e);
                                    n2 = n3;
                                    continue Label_0397_Outer;
                                    // iftrue(Label_0350:, VideoRecorder.access$3100(this.this$1))
                                    while (true) {
                                        Block_16: {
                                        Label_0350:
                                            while (true) {
                                                VideoRecorder.this.handler.sendMessage(VideoRecorder.this.handler.obtainMessage(1, VideoRecorder.this.sendWhenDone, 0));
                                                return;
                                                n2 = n3;
                                                break Block_16;
                                                n2 = 1;
                                                break Label_0350;
                                                try {
                                                    VideoRecorder.this.audioRecorder.release();
                                                }
                                                catch (Exception ex) {
                                                    FileLog.e(ex);
                                                }
                                                continue Block_17_Outer;
                                            }
                                            VideoRecorder.this.handler.sendMessage(VideoRecorder.this.handler.obtainMessage(3, (Object)e));
                                            continue Label_0397_Outer;
                                        }
                                        n2 = n3;
                                        continue;
                                    }
                                }
                                // iftrue(Label_0350:, e.results >= 10)
                                catch (Exception ex3) {
                                    n2 = n3;
                                }
                            }
                        }
                    }
                }
            };
        }
        
        private void didWriteData(final File file, final long n, final boolean b) {
            final boolean videoConvertFirstWrite = this.videoConvertFirstWrite;
            long n2 = 0L;
            if (videoConvertFirstWrite) {
                FileLoader.getInstance(InstantCameraView.this.currentAccount).uploadFile(file.toString(), InstantCameraView.this.isSecretChat, false, 1, 33554432);
                this.videoConvertFirstWrite = false;
                if (b) {
                    final FileLoader instance = FileLoader.getInstance(InstantCameraView.this.currentAccount);
                    final String string = file.toString();
                    final boolean access$3700 = InstantCameraView.this.isSecretChat;
                    if (b) {
                        n2 = file.length();
                    }
                    instance.checkUploadNewDataAvailable(string, access$3700, n, n2);
                }
            }
            else {
                final FileLoader instance2 = FileLoader.getInstance(InstantCameraView.this.currentAccount);
                final String string2 = file.toString();
                final boolean access$3701 = InstantCameraView.this.isSecretChat;
                if (b) {
                    n2 = file.length();
                }
                instance2.checkUploadNewDataAvailable(string2, access$3701, n, n2);
            }
        }
        
        private void handleAudioFrameAvailable(AudioBufferInfo audioBufferInfo) {
            if (this.audioStopedByTime) {
                return;
            }
            this.buffersToWrite.add(audioBufferInfo);
            AudioBufferInfo audioBufferInfo2 = audioBufferInfo;
            Label_0388: {
                if (this.audioFirst == -1L) {
                    if (this.videoFirst == -1L) {
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("video record not yet started");
                        }
                        return;
                    }
                Label_0309_Outer:
                    while (true) {
                        int i = 0;
                        while (true) {
                            while (i < audioBufferInfo.results) {
                                if (i == 0 && Math.abs(this.videoFirst - audioBufferInfo.offset[i]) > 10000000L) {
                                    final long videoFirst = this.videoFirst;
                                    final long[] offset = audioBufferInfo.offset;
                                    this.desyncTime = videoFirst - offset[i];
                                    this.audioFirst = offset[i];
                                    if (BuildVars.LOGS_ENABLED) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("detected desync between audio and video ");
                                        sb.append(this.desyncTime);
                                        FileLog.d(sb.toString());
                                    }
                                }
                                else {
                                    final long[] offset2 = audioBufferInfo.offset;
                                    if (offset2[i] < this.videoFirst) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append("ignore first audio frame at ");
                                            sb2.append(i);
                                            sb2.append(" timestamp = ");
                                            sb2.append(audioBufferInfo.offset[i]);
                                            FileLog.d(sb2.toString());
                                        }
                                        ++i;
                                        continue Label_0309_Outer;
                                    }
                                    audioBufferInfo.lastWroteBuffer = i;
                                    this.audioFirst = offset2[i];
                                    if (BuildVars.LOGS_ENABLED) {
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append("found first audio frame at ");
                                        sb3.append(i);
                                        sb3.append(" timestamp = ");
                                        sb3.append(audioBufferInfo.offset[i]);
                                        FileLog.d(sb3.toString());
                                    }
                                }
                                final boolean b = true;
                                audioBufferInfo2 = audioBufferInfo;
                                if (b) {
                                    break Label_0388;
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("first audio frame not found, removing buffers ");
                                    sb4.append(audioBufferInfo.results);
                                    FileLog.d(sb4.toString());
                                }
                                this.buffersToWrite.remove(audioBufferInfo);
                                if (!this.buffersToWrite.isEmpty()) {
                                    audioBufferInfo = this.buffersToWrite.get(0);
                                    continue Label_0309_Outer;
                                }
                                return;
                            }
                            final boolean b = false;
                            continue;
                        }
                    }
                }
            }
            if (this.audioStartTime == -1L) {
                this.audioStartTime = audioBufferInfo2.offset[audioBufferInfo2.lastWroteBuffer];
            }
            audioBufferInfo = audioBufferInfo2;
            if (this.buffersToWrite.size() > 1) {
                audioBufferInfo = this.buffersToWrite.get(0);
            }
            try {
                this.drainEncoder(false);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            int n = 0;
            while (audioBufferInfo != null) {
                try {
                    final int dequeueInputBuffer = this.audioEncoder.dequeueInputBuffer(0L);
                    int last;
                    if (dequeueInputBuffer >= 0) {
                        ByteBuffer inputBuffer;
                        if (Build$VERSION.SDK_INT >= 21) {
                            inputBuffer = this.audioEncoder.getInputBuffer(dequeueInputBuffer);
                        }
                        else {
                            inputBuffer = this.audioEncoder.getInputBuffers()[dequeueInputBuffer];
                            inputBuffer.clear();
                        }
                        final long n2 = audioBufferInfo.offset[audioBufferInfo.lastWroteBuffer];
                        int lastWroteBuffer = audioBufferInfo.lastWroteBuffer;
                        AudioBufferInfo audioBufferInfo3 = audioBufferInfo;
                        while (true) {
                            last = n;
                            audioBufferInfo = audioBufferInfo3;
                            if (lastWroteBuffer > audioBufferInfo3.results) {
                                break;
                            }
                            if (lastWroteBuffer < audioBufferInfo3.results) {
                                if (!this.running && audioBufferInfo3.offset[lastWroteBuffer] >= this.videoLast - this.desyncTime) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        final StringBuilder sb5 = new StringBuilder();
                                        sb5.append("stop audio encoding because of stoped video recording at ");
                                        sb5.append(audioBufferInfo3.offset[lastWroteBuffer]);
                                        sb5.append(" last video ");
                                        sb5.append(this.videoLast);
                                        FileLog.d(sb5.toString());
                                    }
                                    this.audioStopedByTime = true;
                                    this.buffersToWrite.clear();
                                    audioBufferInfo = null;
                                    last = 1;
                                    break;
                                }
                                if (inputBuffer.remaining() < audioBufferInfo3.read[lastWroteBuffer]) {
                                    audioBufferInfo3.lastWroteBuffer = lastWroteBuffer;
                                    audioBufferInfo = null;
                                    last = n;
                                    break;
                                }
                                inputBuffer.put(audioBufferInfo3.buffer, lastWroteBuffer * 2048, audioBufferInfo3.read[lastWroteBuffer]);
                            }
                            if (lastWroteBuffer >= audioBufferInfo3.results - 1) {
                                this.buffersToWrite.remove(audioBufferInfo3);
                                if (this.running) {
                                    this.buffers.put(audioBufferInfo3);
                                }
                                if (this.buffersToWrite.isEmpty()) {
                                    last = (audioBufferInfo3.last ? 1 : 0);
                                    audioBufferInfo = null;
                                    break;
                                }
                                audioBufferInfo3 = this.buffersToWrite.get(0);
                            }
                            ++lastWroteBuffer;
                        }
                        final MediaCodec audioEncoder = this.audioEncoder;
                        final int position = inputBuffer.position();
                        long n3 = 0L;
                        if (n2 != 0L) {
                            n3 = n2 - this.audioStartTime;
                        }
                        int n4;
                        if (last != 0) {
                            n4 = 4;
                        }
                        else {
                            n4 = 0;
                        }
                        audioEncoder.queueInputBuffer(dequeueInputBuffer, 0, position, n3, n4);
                    }
                    else {
                        last = n;
                    }
                    n = last;
                    continue;
                }
                catch (Throwable t) {
                    FileLog.e(t);
                }
                break;
            }
        }
        
        private void handleStopRecording(final int sendWhenDone) {
            if (this.running) {
                this.sendWhenDone = sendWhenDone;
                this.running = false;
                return;
            }
            try {
                this.drainEncoder(true);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            final MediaCodec videoEncoder = this.videoEncoder;
            if (videoEncoder != null) {
                try {
                    videoEncoder.stop();
                    this.videoEncoder.release();
                    this.videoEncoder = null;
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
            }
            final MediaCodec audioEncoder = this.audioEncoder;
            if (audioEncoder != null) {
                try {
                    audioEncoder.stop();
                    this.audioEncoder.release();
                    this.audioEncoder = null;
                }
                catch (Exception ex3) {
                    FileLog.e(ex3);
                }
            }
            final MP4Builder mediaMuxer = this.mediaMuxer;
            if (mediaMuxer != null) {
                try {
                    mediaMuxer.finishMovie();
                }
                catch (Exception ex4) {
                    FileLog.e(ex4);
                }
            }
            if (sendWhenDone != 0) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$VideoRecorder$8yHVUV5ygBxWBv3JaxOCwtBcKyQ(this, sendWhenDone));
            }
            else {
                FileLoader.getInstance(InstantCameraView.this.currentAccount).cancelUploadFile(this.videoFile.getAbsolutePath(), false);
                this.videoFile.delete();
            }
            EGL14.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            final Surface surface = this.surface;
            if (surface != null) {
                surface.release();
                this.surface = null;
            }
            final android.opengl.EGLDisplay eglDisplay = this.eglDisplay;
            if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
                final android.opengl.EGLSurface egl_NO_SURFACE = EGL14.EGL_NO_SURFACE;
                EGL14.eglMakeCurrent(eglDisplay, egl_NO_SURFACE, egl_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
                EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
                EGL14.eglReleaseThread();
                EGL14.eglTerminate(this.eglDisplay);
            }
            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglConfig = null;
            this.handler.exit();
        }
        
        private void handleVideoFrameAvailable(final long videoLast, final Integer n) {
            try {
                this.drainEncoder(false);
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (!this.lastCameraId.equals(n)) {
                this.lastTimestamp = -1L;
                this.lastCameraId = n;
            }
            final long lastTimestamp = this.lastTimestamp;
            long n2 = 0L;
            long n3 = 0L;
            Label_0113: {
                if (lastTimestamp == -1L) {
                    this.lastTimestamp = videoLast;
                    if (this.currentTimestamp != 0L) {
                        final long currentTimeMillis = System.currentTimeMillis();
                        final long lastCommitedFrameTime = this.lastCommitedFrameTime;
                        n3 = 0L;
                        n2 = (currentTimeMillis - lastCommitedFrameTime) * 1000000L;
                        break Label_0113;
                    }
                }
                else {
                    n2 = videoLast - lastTimestamp;
                    this.lastTimestamp = videoLast;
                }
                n3 = n2;
            }
            this.lastCommitedFrameTime = System.currentTimeMillis();
            if (!this.skippedFirst) {
                this.skippedTime += n2;
                if (this.skippedTime < 200000000L) {
                    return;
                }
                this.skippedFirst = true;
            }
            this.currentTimestamp += n2;
            if (this.videoFirst == -1L) {
                this.videoFirst = videoLast / 1000L;
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("first video frame was at ");
                    sb.append(this.videoFirst);
                    FileLog.d(sb.toString());
                }
            }
            this.videoLast = videoLast;
            GLES20.glUseProgram(this.drawProgram);
            GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 12, (Buffer)InstantCameraView.this.vertexBuffer);
            GLES20.glEnableVertexAttribArray(this.positionHandle);
            GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 8, (Buffer)InstantCameraView.this.textureBuffer);
            GLES20.glEnableVertexAttribArray(this.textureHandle);
            GLES20.glUniform1f(this.scaleXHandle, InstantCameraView.this.scaleX);
            GLES20.glUniform1f(this.scaleYHandle, InstantCameraView.this.scaleY);
            GLES20.glUniformMatrix4fv(this.vertexMatrixHandle, 1, false, InstantCameraView.this.mMVPMatrix, 0);
            GLES20.glActiveTexture(33984);
            if (InstantCameraView.this.oldCameraTexture[0] != 0) {
                if (!this.blendEnabled) {
                    GLES20.glEnable(3042);
                    this.blendEnabled = true;
                }
                GLES20.glUniformMatrix4fv(this.textureMatrixHandle, 1, false, InstantCameraView.this.moldSTMatrix, 0);
                GLES20.glUniform1f(this.alphaHandle, 1.0f);
                GLES20.glBindTexture(36197, InstantCameraView.this.oldCameraTexture[0]);
                GLES20.glDrawArrays(5, 0, 4);
            }
            GLES20.glUniformMatrix4fv(this.textureMatrixHandle, 1, false, InstantCameraView.this.mSTMatrix, 0);
            GLES20.glUniform1f(this.alphaHandle, InstantCameraView.this.cameraTextureAlpha);
            GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glDisableVertexAttribArray(this.positionHandle);
            GLES20.glDisableVertexAttribArray(this.textureHandle);
            GLES20.glBindTexture(36197, 0);
            GLES20.glUseProgram(0);
            EGLExt.eglPresentationTimeANDROID(this.eglDisplay, this.eglSurface, this.currentTimestamp);
            EGL14.eglSwapBuffers(this.eglDisplay, this.eglSurface);
            if (InstantCameraView.this.oldCameraTexture[0] != 0 && InstantCameraView.this.cameraTextureAlpha < 1.0f) {
                final InstantCameraView this$0 = InstantCameraView.this;
                this$0.cameraTextureAlpha += n3 / 2.0E8f;
                if (InstantCameraView.this.cameraTextureAlpha > 1.0f) {
                    GLES20.glDisable(3042);
                    this.blendEnabled = false;
                    InstantCameraView.this.cameraTextureAlpha = 1.0f;
                    GLES20.glDeleteTextures(1, InstantCameraView.this.oldCameraTexture, 0);
                    InstantCameraView.this.oldCameraTexture[0] = 0;
                    if (!InstantCameraView.this.cameraReady) {
                        InstantCameraView.this.cameraReady = true;
                        AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$VideoRecorder$ONc2_IZzZjKNnbhigUCIh3I7vt4(this));
                    }
                }
            }
            else if (!InstantCameraView.this.cameraReady) {
                InstantCameraView.this.cameraReady = true;
                AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$VideoRecorder$cj_4IZt_HllVK7x5iDyMvDcfDnc(this));
            }
        }
        
        private void prepareEncoder() {
            try {
                int minBufferSize;
                if ((minBufferSize = AudioRecord.getMinBufferSize(44100, 16, 2)) <= 0) {
                    minBufferSize = 3584;
                }
                int i = 49152;
                if (49152 < minBufferSize) {
                    i = (minBufferSize / 2048 + 1) * 2048 * 2;
                }
                for (int j = 0; j < 3; ++j) {
                    this.buffers.add(new AudioBufferInfo());
                }
                (this.audioRecorder = new AudioRecord(0, 44100, 16, 2, i)).startRecording();
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("initied audio record with channels ");
                    sb.append(this.audioRecorder.getChannelCount());
                    sb.append(" sample rate = ");
                    sb.append(this.audioRecorder.getSampleRate());
                    sb.append(" bufferSize = ");
                    sb.append(i);
                    FileLog.d(sb.toString());
                }
                final Thread thread = new Thread(this.recorderRunnable);
                thread.setPriority(10);
                thread.start();
                this.audioBufferInfo = new MediaCodec$BufferInfo();
                this.videoBufferInfo = new MediaCodec$BufferInfo();
                final MediaFormat mediaFormat = new MediaFormat();
                mediaFormat.setString("mime", "audio/mp4a-latm");
                mediaFormat.setInteger("aac-profile", 2);
                mediaFormat.setInteger("sample-rate", 44100);
                mediaFormat.setInteger("channel-count", 1);
                mediaFormat.setInteger("bitrate", 32000);
                mediaFormat.setInteger("max-input-size", 20480);
                (this.audioEncoder = MediaCodec.createEncoderByType("audio/mp4a-latm")).configure(mediaFormat, (Surface)null, (MediaCrypto)null, 1);
                this.audioEncoder.start();
                this.videoEncoder = MediaCodec.createEncoderByType("video/avc");
                final MediaFormat videoFormat = MediaFormat.createVideoFormat("video/avc", this.videoWidth, this.videoHeight);
                videoFormat.setInteger("color-format", 2130708361);
                videoFormat.setInteger("bitrate", this.videoBitrate);
                videoFormat.setInteger("frame-rate", 30);
                videoFormat.setInteger("i-frame-interval", 1);
                this.videoEncoder.configure(videoFormat, (Surface)null, (MediaCrypto)null, 1);
                this.surface = this.videoEncoder.createInputSurface();
                this.videoEncoder.start();
                final Mp4Movie mp4Movie = new Mp4Movie();
                mp4Movie.setCacheFile(this.videoFile);
                mp4Movie.setRotation(0);
                mp4Movie.setSize(this.videoWidth, this.videoHeight);
                this.mediaMuxer = new MP4Builder().createMovie(mp4Movie, InstantCameraView.this.isSecretChat);
                AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$VideoRecorder$8ZN8xq5EwIyFVJLsetA_qjZZbSQ(this));
                if (this.eglDisplay != EGL14.EGL_NO_DISPLAY) {
                    throw new RuntimeException("EGL already set up");
                }
                this.eglDisplay = EGL14.eglGetDisplay(0);
                final android.opengl.EGLDisplay eglDisplay = this.eglDisplay;
                if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
                    throw new RuntimeException("unable to get EGL14 display");
                }
                final int[] array = new int[2];
                if (!EGL14.eglInitialize(eglDisplay, array, 0, array, 1)) {
                    this.eglDisplay = null;
                    throw new RuntimeException("unable to initialize EGL14");
                }
                if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
                    final android.opengl.EGLConfig[] array2 = { null };
                    if (!EGL14.eglChooseConfig(this.eglDisplay, new int[] { 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12610, 1, 12344 }, 0, array2, 0, array2.length, new int[] { 0 }, 0)) {
                        throw new RuntimeException("Unable to find a suitable EGLConfig");
                    }
                    this.eglContext = EGL14.eglCreateContext(this.eglDisplay, array2[0], this.sharedEglContext, new int[] { 12440, 2, 12344 }, 0);
                    this.eglConfig = array2[0];
                }
                EGL14.eglQueryContext(this.eglDisplay, this.eglContext, 12440, new int[1], 0);
                if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
                    throw new IllegalStateException("surface already created");
                }
                this.eglSurface = EGL14.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, (Object)this.surface, new int[] { 12344 }, 0);
                final android.opengl.EGLSurface eglSurface = this.eglSurface;
                if (eglSurface == null) {
                    throw new RuntimeException("surface was null");
                }
                if (!EGL14.eglMakeCurrent(this.eglDisplay, eglSurface, eglSurface, this.eglContext)) {
                    if (BuildVars.LOGS_ENABLED) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("eglMakeCurrent failed ");
                        sb2.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
                        FileLog.e(sb2.toString());
                    }
                    throw new RuntimeException("eglMakeCurrent failed");
                }
                GLES20.glBlendFunc(770, 771);
                final int access$1800 = InstantCameraView.this.loadShader(35633, "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n");
                final int access$1801 = InstantCameraView.this.loadShader(35632, "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float scaleX;\nuniform float scaleY;\nuniform float alpha;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec2 coord = vec2((vTextureCoord.x - 0.5) * scaleX, (vTextureCoord.y - 0.5) * scaleY);\n   float coef = ceil(clamp(0.2601 - dot(coord, coord), 0.0, 1.0));\n   vec3 color = texture2D(sTexture, vTextureCoord).rgb * coef + (1.0 - step(0.001, coef));\n   gl_FragColor = vec4(color * alpha, alpha);\n}\n");
                if (access$1800 != 0 && access$1801 != 0) {
                    GLES20.glAttachShader(this.drawProgram = GLES20.glCreateProgram(), access$1800);
                    GLES20.glAttachShader(this.drawProgram, access$1801);
                    GLES20.glLinkProgram(this.drawProgram);
                    final int[] array3 = { 0 };
                    GLES20.glGetProgramiv(this.drawProgram, 35714, array3, 0);
                    if (array3[0] == 0) {
                        GLES20.glDeleteProgram(this.drawProgram);
                        this.drawProgram = 0;
                    }
                    else {
                        this.positionHandle = GLES20.glGetAttribLocation(this.drawProgram, "aPosition");
                        this.textureHandle = GLES20.glGetAttribLocation(this.drawProgram, "aTextureCoord");
                        this.scaleXHandle = GLES20.glGetUniformLocation(this.drawProgram, "scaleX");
                        this.scaleYHandle = GLES20.glGetUniformLocation(this.drawProgram, "scaleY");
                        this.alphaHandle = GLES20.glGetUniformLocation(this.drawProgram, "alpha");
                        this.vertexMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uMVPMatrix");
                        this.textureMatrixHandle = GLES20.glGetUniformLocation(this.drawProgram, "uSTMatrix");
                    }
                }
            }
            catch (Exception cause) {
                throw new RuntimeException(cause);
            }
        }
        
        public void drainEncoder(final boolean b) throws Exception {
            if (b) {
                this.videoEncoder.signalEndOfInputStream();
            }
            ByteBuffer[] array;
            if (Build$VERSION.SDK_INT < 21) {
                array = this.videoEncoder.getOutputBuffers();
            }
            else {
                array = null;
            }
            int dequeueOutputBuffer = 0;
            Label_0847: {
                while (true) {
                    dequeueOutputBuffer = this.videoEncoder.dequeueOutputBuffer(this.videoBufferInfo, 10000L);
                    ByteBuffer[] outputBuffers;
                    if (dequeueOutputBuffer == -1) {
                        outputBuffers = array;
                        if (!b) {
                            break;
                        }
                    }
                    else if (dequeueOutputBuffer == -3) {
                        outputBuffers = array;
                        if (Build$VERSION.SDK_INT < 21) {
                            outputBuffers = this.videoEncoder.getOutputBuffers();
                        }
                    }
                    else if (dequeueOutputBuffer == -2) {
                        final MediaFormat outputFormat = this.videoEncoder.getOutputFormat();
                        outputBuffers = array;
                        if (this.videoTrackIndex == -5) {
                            this.videoTrackIndex = this.mediaMuxer.addTrack(outputFormat, false);
                            outputBuffers = array;
                        }
                    }
                    else {
                        outputBuffers = array;
                        if (dequeueOutputBuffer >= 0) {
                            ByteBuffer outputBuffer;
                            if (Build$VERSION.SDK_INT < 21) {
                                outputBuffer = array[dequeueOutputBuffer];
                            }
                            else {
                                outputBuffer = this.videoEncoder.getOutputBuffer(dequeueOutputBuffer);
                            }
                            if (outputBuffer == null) {
                                break Label_0847;
                            }
                            final MediaCodec$BufferInfo videoBufferInfo = this.videoBufferInfo;
                            final int size = videoBufferInfo.size;
                            Label_0494: {
                                if (size > 1) {
                                    if ((videoBufferInfo.flags & 0x2) == 0x0) {
                                        final long writeSampleData = this.mediaMuxer.writeSampleData(this.videoTrackIndex, outputBuffer, videoBufferInfo, true);
                                        if (writeSampleData != 0L) {
                                            this.didWriteData(this.videoFile, writeSampleData, false);
                                        }
                                    }
                                    else if (this.videoTrackIndex == -5) {
                                        final byte[] src = new byte[size];
                                        outputBuffer.limit(videoBufferInfo.offset + size);
                                        outputBuffer.position(this.videoBufferInfo.offset);
                                        outputBuffer.get(src);
                                        while (true) {
                                            for (int n = this.videoBufferInfo.size - 1; n >= 0 && n > 3; --n) {
                                                if (src[n] == 1 && src[n - 1] == 0 && src[n - 2] == 0) {
                                                    final int offset = n - 3;
                                                    if (src[offset] == 0) {
                                                        final ByteBuffer allocate = ByteBuffer.allocate(offset);
                                                        final ByteBuffer allocate2 = ByteBuffer.allocate(this.videoBufferInfo.size - offset);
                                                        allocate.put(src, 0, offset).position(0);
                                                        allocate2.put(src, offset, this.videoBufferInfo.size - offset).position(0);
                                                        final MediaFormat videoFormat = MediaFormat.createVideoFormat("video/avc", this.videoWidth, this.videoHeight);
                                                        if (allocate != null && allocate2 != null) {
                                                            videoFormat.setByteBuffer("csd-0", allocate);
                                                            videoFormat.setByteBuffer("csd-1", allocate2);
                                                        }
                                                        this.videoTrackIndex = this.mediaMuxer.addTrack(videoFormat, false);
                                                        break Label_0494;
                                                    }
                                                }
                                            }
                                            final ByteBuffer allocate = null;
                                            final ByteBuffer allocate2 = null;
                                            continue;
                                        }
                                    }
                                }
                            }
                            this.videoEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                            outputBuffers = array;
                            if ((this.videoBufferInfo.flags & 0x4) != 0x0) {
                                break;
                            }
                        }
                    }
                    array = outputBuffers;
                }
                if (Build$VERSION.SDK_INT < 21) {
                    array = this.audioEncoder.getOutputBuffers();
                }
                int dequeueOutputBuffer2 = 0;
                Label_0798: {
                    while (true) {
                        dequeueOutputBuffer2 = this.audioEncoder.dequeueOutputBuffer(this.audioBufferInfo, 0L);
                        ByteBuffer[] outputBuffers2;
                        if (dequeueOutputBuffer2 == -1) {
                            if (!b) {
                                break;
                            }
                            outputBuffers2 = array;
                            if (!this.running) {
                                outputBuffers2 = array;
                                if (this.sendWhenDone == 0) {
                                    break;
                                }
                            }
                        }
                        else if (dequeueOutputBuffer2 == -3) {
                            outputBuffers2 = array;
                            if (Build$VERSION.SDK_INT < 21) {
                                outputBuffers2 = this.audioEncoder.getOutputBuffers();
                            }
                        }
                        else if (dequeueOutputBuffer2 == -2) {
                            final MediaFormat outputFormat2 = this.audioEncoder.getOutputFormat();
                            outputBuffers2 = array;
                            if (this.audioTrackIndex == -5) {
                                this.audioTrackIndex = this.mediaMuxer.addTrack(outputFormat2, true);
                                outputBuffers2 = array;
                            }
                        }
                        else {
                            outputBuffers2 = array;
                            if (dequeueOutputBuffer2 >= 0) {
                                ByteBuffer outputBuffer2;
                                if (Build$VERSION.SDK_INT < 21) {
                                    outputBuffer2 = array[dequeueOutputBuffer2];
                                }
                                else {
                                    outputBuffer2 = this.audioEncoder.getOutputBuffer(dequeueOutputBuffer2);
                                }
                                if (outputBuffer2 == null) {
                                    break Label_0798;
                                }
                                final MediaCodec$BufferInfo audioBufferInfo = this.audioBufferInfo;
                                if ((audioBufferInfo.flags & 0x2) != 0x0) {
                                    audioBufferInfo.size = 0;
                                }
                                final MediaCodec$BufferInfo audioBufferInfo2 = this.audioBufferInfo;
                                if (audioBufferInfo2.size != 0) {
                                    final long writeSampleData2 = this.mediaMuxer.writeSampleData(this.audioTrackIndex, outputBuffer2, audioBufferInfo2, false);
                                    if (writeSampleData2 != 0L) {
                                        this.didWriteData(this.videoFile, writeSampleData2, false);
                                    }
                                }
                                this.audioEncoder.releaseOutputBuffer(dequeueOutputBuffer2, false);
                                outputBuffers2 = array;
                                if ((this.audioBufferInfo.flags & 0x4) != 0x0) {
                                    break;
                                }
                            }
                        }
                        array = outputBuffers2;
                    }
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("encoderOutputBuffer ");
                sb.append(dequeueOutputBuffer2);
                sb.append(" was null");
                throw new RuntimeException(sb.toString());
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("encoderOutputBuffer ");
            sb2.append(dequeueOutputBuffer);
            sb2.append(" was null");
            throw new RuntimeException(sb2.toString());
        }
        
        @Override
        protected void finalize() throws Throwable {
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
            }
            finally {
                super.finalize();
            }
        }
        
        public void frameAvailable(final SurfaceTexture surfaceTexture, final Integer n, final long n2) {
            synchronized (this.sync) {
                if (!this.ready) {
                    return;
                }
                // monitorexit(this.sync)
                long timestamp = surfaceTexture.getTimestamp();
                if (timestamp == 0L) {
                    ++this.zeroTimeStamps;
                    if (this.zeroTimeStamps <= 1) {
                        return;
                    }
                    timestamp = n2;
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("fix timestamp enabled");
                        timestamp = n2;
                    }
                }
                else {
                    this.zeroTimeStamps = 0;
                }
                this.handler.sendMessage(this.handler.obtainMessage(2, (int)(timestamp >> 32), (int)timestamp, (Object)n));
            }
        }
        
        public Surface getInputSurface() {
            return this.surface;
        }
        
        @Override
        public void run() {
            Looper.prepare();
            synchronized (this.sync) {
                this.handler = new EncoderHandler(this);
                this.ready = true;
                this.sync.notify();
                // monitorexit(this.sync)
                Looper.loop();
                synchronized (this.sync) {
                    this.ready = false;
                }
            }
        }
        
        public void startRecording(final File p0, final android.opengl.EGLContext p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: astore_3       
            //     4: aload_3        
            //     5: astore          4
            //     7: aload_3        
            //     8: ifnonnull       16
            //    11: ldc_w           ""
            //    14: astore          4
            //    16: aload           4
            //    18: ldc_w           "zeroflte"
            //    21: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
            //    24: ifne            54
            //    27: aload           4
            //    29: ldc_w           "zenlte"
            //    32: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
            //    35: ifeq            41
            //    38: goto            54
            //    41: sipush          240
            //    44: istore          5
            //    46: ldc_w           400000
            //    49: istore          6
            //    51: goto            64
            //    54: sipush          320
            //    57: istore          5
            //    59: ldc_w           600000
            //    62: istore          6
            //    64: aload_0        
            //    65: aload_1        
            //    66: putfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.videoFile:Ljava/io/File;
            //    69: aload_0        
            //    70: iload           5
            //    72: putfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.videoWidth:I
            //    75: aload_0        
            //    76: iload           5
            //    78: putfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.videoHeight:I
            //    81: aload_0        
            //    82: iload           6
            //    84: putfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.videoBitrate:I
            //    87: aload_0        
            //    88: aload_2        
            //    89: putfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.sharedEglContext:Landroid/opengl/EGLContext;
            //    92: aload_0        
            //    93: getfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.sync:Ljava/lang/Object;
            //    96: astore_1       
            //    97: aload_1        
            //    98: monitorenter   
            //    99: aload_0        
            //   100: getfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.running:Z
            //   103: ifeq            109
            //   106: aload_1        
            //   107: monitorexit    
            //   108: return         
            //   109: aload_0        
            //   110: iconst_1       
            //   111: putfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.running:Z
            //   114: new             Ljava/lang/Thread;
            //   117: astore_2       
            //   118: aload_2        
            //   119: aload_0        
            //   120: ldc_w           "TextureMovieEncoder"
            //   123: invokespecial   java/lang/Thread.<init>:(Ljava/lang/Runnable;Ljava/lang/String;)V
            //   126: aload_2        
            //   127: bipush          10
            //   129: invokevirtual   java/lang/Thread.setPriority:(I)V
            //   132: aload_2        
            //   133: invokevirtual   java/lang/Thread.start:()V
            //   136: aload_0        
            //   137: getfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.ready:Z
            //   140: istore          7
            //   142: iload           7
            //   144: ifne            157
            //   147: aload_0        
            //   148: getfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.sync:Ljava/lang/Object;
            //   151: invokevirtual   java/lang/Object.wait:()V
            //   154: goto            136
            //   157: aload_1        
            //   158: monitorexit    
            //   159: aload_0        
            //   160: getfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.handler:Lorg/telegram/ui/Components/InstantCameraView$EncoderHandler;
            //   163: aload_0        
            //   164: getfield        org/telegram/ui/Components/InstantCameraView$VideoRecorder.handler:Lorg/telegram/ui/Components/InstantCameraView$EncoderHandler;
            //   167: iconst_0       
            //   168: invokevirtual   android/os/Handler.obtainMessage:(I)Landroid/os/Message;
            //   171: invokevirtual   android/os/Handler.sendMessage:(Landroid/os/Message;)Z
            //   174: pop            
            //   175: return         
            //   176: astore_2       
            //   177: aload_1        
            //   178: monitorexit    
            //   179: aload_2        
            //   180: athrow         
            //   181: astore_2       
            //   182: goto            136
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                            
            //  -----  -----  -----  -----  --------------------------------
            //  99     108    176    181    Any
            //  109    136    176    181    Any
            //  136    142    176    181    Any
            //  147    154    181    185    Ljava/lang/InterruptedException;
            //  147    154    176    181    Any
            //  157    159    176    181    Any
            //  177    179    176    181    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0157:
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public void stopRecording(final int n) {
            this.handler.sendMessage(this.handler.obtainMessage(1, n, 0));
        }
    }
}
