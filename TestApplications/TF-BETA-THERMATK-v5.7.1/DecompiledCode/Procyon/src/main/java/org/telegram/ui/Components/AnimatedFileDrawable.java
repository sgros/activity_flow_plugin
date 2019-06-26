// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Canvas;
import java.util.concurrent.TimeUnit;
import android.graphics.Shader$TileMode;
import org.telegram.messenger.FileLog;
import android.graphics.Bitmap$Config;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import android.os.Looper;
import org.telegram.messenger.AnimatedFileDrawableStream;
import android.graphics.Matrix;
import java.io.File;
import android.view.View;
import android.graphics.Rect;
import org.telegram.messenger.DispatchQueue;
import android.graphics.BitmapShader;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Handler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;

public class AnimatedFileDrawable extends BitmapDrawable implements Animatable
{
    public static final int PARAM_NUM_AUDIO_FRAME_SIZE = 5;
    public static final int PARAM_NUM_BITRATE = 3;
    public static final int PARAM_NUM_COUNT = 9;
    public static final int PARAM_NUM_DURATION = 4;
    public static final int PARAM_NUM_FRAMERATE = 7;
    public static final int PARAM_NUM_HEIGHT = 2;
    public static final int PARAM_NUM_IS_AVC = 0;
    public static final int PARAM_NUM_ROTATION = 8;
    public static final int PARAM_NUM_VIDEO_FRAME_SIZE = 6;
    public static final int PARAM_NUM_WIDTH = 1;
    private static ScheduledThreadPoolExecutor executor;
    private static final Handler uiHandler;
    private RectF actualDrawRect;
    private boolean applyTransformation;
    private Bitmap backgroundBitmap;
    private int backgroundBitmapTime;
    private BitmapShader backgroundShader;
    private int currentAccount;
    private DispatchQueue decodeQueue;
    private boolean decodeSingleFrame;
    private boolean decoderCreated;
    private boolean destroyWhenDone;
    private final Rect dstRect;
    private int invalidateAfter;
    private volatile boolean isRecycled;
    private volatile boolean isRunning;
    private long lastFrameDecodeTime;
    private long lastFrameTime;
    private int lastTimeStamp;
    private Runnable loadFrameRunnable;
    private Runnable loadFrameTask;
    protected final Runnable mInvalidateTask;
    private final Runnable mStartTask;
    private final int[] metaData;
    public volatile long nativePtr;
    private Bitmap nextRenderingBitmap;
    private int nextRenderingBitmapTime;
    private BitmapShader nextRenderingShader;
    private View parentView;
    private File path;
    private boolean pendingRemoveLoading;
    private int pendingRemoveLoadingFramesReset;
    private volatile long pendingSeekTo;
    private volatile long pendingSeekToUI;
    private boolean recycleWithSecond;
    private Bitmap renderingBitmap;
    private int renderingBitmapTime;
    private BitmapShader renderingShader;
    private int roundRadius;
    private float scaleX;
    private float scaleY;
    private View secondParentView;
    private Matrix shaderMatrix;
    private boolean singleFrameDecoded;
    private AnimatedFileDrawableStream stream;
    private long streamFileSize;
    private final Object sync;
    private Runnable uiRunnable;
    private Runnable uiRunnableNoFrame;
    private boolean useSharedQueue;
    
    static {
        uiHandler = new Handler(Looper.getMainLooper());
        AnimatedFileDrawable.executor = new ScheduledThreadPoolExecutor(2, new ThreadPoolExecutor.DiscardPolicy());
    }
    
    public AnimatedFileDrawable(final File path, final boolean b, final long streamFileSize, final TLRPC.Document document, final Object o, final int currentAccount) {
        this.invalidateAfter = 50;
        this.metaData = new int[5];
        this.pendingSeekTo = -1L;
        this.pendingSeekToUI = -1L;
        this.sync = new Object();
        this.actualDrawRect = new RectF();
        this.shaderMatrix = new Matrix();
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
        this.dstRect = new Rect();
        this.parentView = null;
        this.secondParentView = null;
        this.mInvalidateTask = new _$$Lambda$AnimatedFileDrawable$D96GYyKDLrUXCvNeQ7iluME9Yq4(this);
        this.uiRunnableNoFrame = new Runnable() {
            @Override
            public void run() {
                if (AnimatedFileDrawable.this.destroyWhenDone && AnimatedFileDrawable.this.nativePtr != 0L) {
                    destroyDecoder(AnimatedFileDrawable.this.nativePtr);
                    AnimatedFileDrawable.this.nativePtr = 0L;
                }
                if (AnimatedFileDrawable.this.nativePtr == 0L) {
                    if (AnimatedFileDrawable.this.renderingBitmap != null) {
                        AnimatedFileDrawable.this.renderingBitmap.recycle();
                        AnimatedFileDrawable.this.renderingBitmap = null;
                    }
                    if (AnimatedFileDrawable.this.backgroundBitmap != null) {
                        AnimatedFileDrawable.this.backgroundBitmap.recycle();
                        AnimatedFileDrawable.this.backgroundBitmap = null;
                    }
                    if (AnimatedFileDrawable.this.decodeQueue != null) {
                        AnimatedFileDrawable.this.decodeQueue.recycle();
                        AnimatedFileDrawable.this.decodeQueue = null;
                    }
                    return;
                }
                AnimatedFileDrawable.this.loadFrameTask = null;
                AnimatedFileDrawable.this.scheduleNextGetFrame();
            }
        };
        this.uiRunnable = new Runnable() {
            @Override
            public void run() {
                if (AnimatedFileDrawable.this.destroyWhenDone && AnimatedFileDrawable.this.nativePtr != 0L) {
                    destroyDecoder(AnimatedFileDrawable.this.nativePtr);
                    AnimatedFileDrawable.this.nativePtr = 0L;
                }
                if (AnimatedFileDrawable.this.nativePtr == 0L) {
                    if (AnimatedFileDrawable.this.renderingBitmap != null) {
                        AnimatedFileDrawable.this.renderingBitmap.recycle();
                        AnimatedFileDrawable.this.renderingBitmap = null;
                    }
                    if (AnimatedFileDrawable.this.backgroundBitmap != null) {
                        AnimatedFileDrawable.this.backgroundBitmap.recycle();
                        AnimatedFileDrawable.this.backgroundBitmap = null;
                    }
                    if (AnimatedFileDrawable.this.decodeQueue != null) {
                        AnimatedFileDrawable.this.decodeQueue.recycle();
                        AnimatedFileDrawable.this.decodeQueue = null;
                    }
                    return;
                }
                if (AnimatedFileDrawable.this.stream != null && AnimatedFileDrawable.this.pendingRemoveLoading) {
                    FileLoader.getInstance(AnimatedFileDrawable.this.currentAccount).removeLoadingVideo(AnimatedFileDrawable.this.stream.getDocument(), false, false);
                }
                if (AnimatedFileDrawable.this.pendingRemoveLoadingFramesReset <= 0) {
                    AnimatedFileDrawable.this.pendingRemoveLoading = true;
                }
                else {
                    AnimatedFileDrawable.this.pendingRemoveLoadingFramesReset--;
                }
                AnimatedFileDrawable.this.singleFrameDecoded = true;
                AnimatedFileDrawable.this.loadFrameTask = null;
                final AnimatedFileDrawable this$0 = AnimatedFileDrawable.this;
                this$0.nextRenderingBitmap = this$0.backgroundBitmap;
                final AnimatedFileDrawable this$2 = AnimatedFileDrawable.this;
                this$2.nextRenderingBitmapTime = this$2.backgroundBitmapTime;
                final AnimatedFileDrawable this$3 = AnimatedFileDrawable.this;
                this$3.nextRenderingShader = this$3.backgroundShader;
                if (AnimatedFileDrawable.this.metaData[3] < AnimatedFileDrawable.this.lastTimeStamp) {
                    AnimatedFileDrawable.this.lastTimeStamp = 0;
                }
                if (AnimatedFileDrawable.this.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp != 0) {
                    final AnimatedFileDrawable this$4 = AnimatedFileDrawable.this;
                    this$4.invalidateAfter = this$4.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp;
                }
                if (AnimatedFileDrawable.this.pendingSeekToUI >= 0L && AnimatedFileDrawable.this.pendingSeekTo == -1L) {
                    AnimatedFileDrawable.this.pendingSeekToUI = -1L;
                    AnimatedFileDrawable.this.invalidateAfter = 0;
                }
                final AnimatedFileDrawable this$5 = AnimatedFileDrawable.this;
                this$5.lastTimeStamp = this$5.metaData[3];
                if (AnimatedFileDrawable.this.secondParentView != null) {
                    AnimatedFileDrawable.this.secondParentView.invalidate();
                }
                else if (AnimatedFileDrawable.this.parentView != null) {
                    AnimatedFileDrawable.this.parentView.invalidate();
                }
                AnimatedFileDrawable.this.scheduleNextGetFrame();
            }
        };
        this.loadFrameRunnable = new Runnable() {
            @Override
            public void run() {
                if (!AnimatedFileDrawable.this.isRecycled) {
                    if (!AnimatedFileDrawable.this.decoderCreated && AnimatedFileDrawable.this.nativePtr == 0L) {
                        final AnimatedFileDrawable this$0 = AnimatedFileDrawable.this;
                        this$0.nativePtr = createDecoder(this$0.path.getAbsolutePath(), AnimatedFileDrawable.this.metaData, AnimatedFileDrawable.this.currentAccount, AnimatedFileDrawable.this.streamFileSize, AnimatedFileDrawable.this.stream);
                        AnimatedFileDrawable.this.decoderCreated = true;
                    }
                    try {
                        final long nativePtr = AnimatedFileDrawable.this.nativePtr;
                        boolean b = false;
                        if (nativePtr == 0L && AnimatedFileDrawable.this.metaData[0] != 0 && AnimatedFileDrawable.this.metaData[1] != 0) {
                            AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnableNoFrame);
                            return;
                        }
                        if (AnimatedFileDrawable.this.backgroundBitmap == null && AnimatedFileDrawable.this.metaData[0] > 0 && AnimatedFileDrawable.this.metaData[1] > 0) {
                            try {
                                AnimatedFileDrawable.this.backgroundBitmap = Bitmap.createBitmap(AnimatedFileDrawable.this.metaData[0], AnimatedFileDrawable.this.metaData[1], Bitmap$Config.ARGB_8888);
                            }
                            catch (Throwable t) {
                                FileLog.e(t);
                            }
                            if (AnimatedFileDrawable.this.backgroundShader == null && AnimatedFileDrawable.this.backgroundBitmap != null && AnimatedFileDrawable.this.roundRadius != 0) {
                                AnimatedFileDrawable.this.backgroundShader = new BitmapShader(AnimatedFileDrawable.this.backgroundBitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
                            }
                        }
                        if (AnimatedFileDrawable.this.pendingSeekTo >= 0L) {
                            AnimatedFileDrawable.this.metaData[3] = (int)AnimatedFileDrawable.this.pendingSeekTo;
                            final long access$2100 = AnimatedFileDrawable.this.pendingSeekTo;
                            synchronized (AnimatedFileDrawable.this.sync) {
                                AnimatedFileDrawable.this.pendingSeekTo = -1L;
                                // monitorexit(AnimatedFileDrawable.access$3000(this.this$0))
                                if (AnimatedFileDrawable.this.stream != null) {
                                    AnimatedFileDrawable.this.stream.reset();
                                }
                                seekToMs(AnimatedFileDrawable.this.nativePtr, access$2100);
                                b = true;
                            }
                        }
                        if (AnimatedFileDrawable.this.backgroundBitmap != null) {
                            AnimatedFileDrawable.this.lastFrameDecodeTime = System.currentTimeMillis();
                            if (getVideoFrame(AnimatedFileDrawable.this.nativePtr, AnimatedFileDrawable.this.backgroundBitmap, AnimatedFileDrawable.this.metaData, AnimatedFileDrawable.this.backgroundBitmap.getRowBytes()) == 0) {
                                AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnableNoFrame);
                                return;
                            }
                            if (b) {
                                AnimatedFileDrawable.this.lastTimeStamp = AnimatedFileDrawable.this.metaData[3];
                            }
                            AnimatedFileDrawable.this.backgroundBitmapTime = AnimatedFileDrawable.this.metaData[3];
                        }
                    }
                    catch (Throwable t2) {
                        FileLog.e(t2);
                    }
                }
                AndroidUtilities.runOnUIThread(AnimatedFileDrawable.this.uiRunnable);
            }
        };
        this.mStartTask = new _$$Lambda$AnimatedFileDrawable$AmB2znRBjaDHOIPDjH2S8BYovYQ(this);
        this.path = path;
        this.streamFileSize = streamFileSize;
        this.currentAccount = currentAccount;
        this.getPaint().setFlags(2);
        if (streamFileSize != 0L && document != null) {
            this.stream = new AnimatedFileDrawableStream(document, o, currentAccount);
        }
        if (b) {
            this.nativePtr = createDecoder(path.getAbsolutePath(), this.metaData, this.currentAccount, this.streamFileSize, this.stream);
            this.decoderCreated = true;
        }
    }
    
    private static native long createDecoder(final String p0, final int[] p1, final int p2, final long p3, final Object p4);
    
    private static native void destroyDecoder(final long p0);
    
    private static native int getVideoFrame(final long p0, final Bitmap p1, final int[] p2, final int p3);
    
    public static native void getVideoInfo(final String p0, final int[] p1);
    
    private static native void prepareToSeek(final long p0);
    
    protected static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() == AnimatedFileDrawable.uiHandler.getLooper()) {
            runnable.run();
        }
        else {
            AnimatedFileDrawable.uiHandler.post(runnable);
        }
    }
    
    private void scheduleNextGetFrame() {
        if (this.loadFrameTask == null) {
            final long nativePtr = this.nativePtr;
            long min = 0L;
            if ((nativePtr != 0L || !this.decoderCreated) && !this.destroyWhenDone) {
                if (!this.isRunning) {
                    final boolean decodeSingleFrame = this.decodeSingleFrame;
                    if (!decodeSingleFrame) {
                        return;
                    }
                    if (decodeSingleFrame && this.singleFrameDecoded) {
                        return;
                    }
                }
                if (this.lastFrameDecodeTime != 0L) {
                    final int invalidateAfter = this.invalidateAfter;
                    min = Math.min(invalidateAfter, Math.max(0L, invalidateAfter - (System.currentTimeMillis() - this.lastFrameDecodeTime)));
                }
                if (this.useSharedQueue) {
                    AnimatedFileDrawable.executor.schedule(this.loadFrameTask = this.loadFrameRunnable, min, TimeUnit.MILLISECONDS);
                }
                else {
                    if (this.decodeQueue == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("decodeQueue");
                        sb.append(this);
                        this.decodeQueue = new DispatchQueue(sb.toString());
                    }
                    this.decodeQueue.postRunnable(this.loadFrameTask = this.loadFrameRunnable, min);
                }
            }
        }
    }
    
    private static native void seekToMs(final long p0, final long p1);
    
    private static native void stopDecoder(final long p0);
    
    public void draw(final Canvas canvas) {
        if ((this.nativePtr == 0L && this.decoderCreated) || this.destroyWhenDone) {
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        if (this.isRunning) {
            if (this.renderingBitmap == null && this.nextRenderingBitmap == null) {
                this.scheduleNextGetFrame();
            }
            else if (this.nextRenderingBitmap != null && (this.renderingBitmap == null || Math.abs(currentTimeMillis - this.lastFrameTime) >= this.invalidateAfter)) {
                this.renderingBitmap = this.nextRenderingBitmap;
                this.renderingBitmapTime = this.nextRenderingBitmapTime;
                this.renderingShader = this.nextRenderingShader;
                this.nextRenderingBitmap = null;
                this.nextRenderingBitmapTime = 0;
                this.nextRenderingShader = null;
                this.lastFrameTime = currentTimeMillis;
            }
        }
        else if (!this.isRunning && this.decodeSingleFrame && Math.abs(currentTimeMillis - this.lastFrameTime) >= this.invalidateAfter) {
            final Bitmap nextRenderingBitmap = this.nextRenderingBitmap;
            if (nextRenderingBitmap != null) {
                this.renderingBitmap = nextRenderingBitmap;
                this.renderingBitmapTime = this.nextRenderingBitmapTime;
                this.renderingShader = this.nextRenderingShader;
                this.nextRenderingBitmap = null;
                this.nextRenderingBitmapTime = 0;
                this.nextRenderingShader = null;
                this.lastFrameTime = currentTimeMillis;
            }
        }
        final Bitmap renderingBitmap = this.renderingBitmap;
        if (renderingBitmap != null) {
            if (this.applyTransformation) {
                final int width = renderingBitmap.getWidth();
                final int height = this.renderingBitmap.getHeight();
                final int[] metaData = this.metaData;
                int n = 0;
                int n2 = 0;
                Label_0295: {
                    if (metaData[2] != 90) {
                        n = width;
                        n2 = height;
                        if (metaData[2] != 270) {
                            break Label_0295;
                        }
                    }
                    n2 = width;
                    n = height;
                }
                this.dstRect.set(this.getBounds());
                this.scaleX = this.dstRect.width() / (float)n;
                this.scaleY = this.dstRect.height() / (float)n2;
                this.applyTransformation = false;
            }
            if (this.roundRadius != 0) {
                Math.max(this.scaleX, this.scaleY);
                if (this.renderingShader == null) {
                    final Bitmap backgroundBitmap = this.backgroundBitmap;
                    final Shader$TileMode clamp = Shader$TileMode.CLAMP;
                    this.renderingShader = new BitmapShader(backgroundBitmap, clamp, clamp);
                }
                final Paint paint = this.getPaint();
                paint.setShader((Shader)this.renderingShader);
                this.shaderMatrix.reset();
                final Matrix shaderMatrix = this.shaderMatrix;
                final Rect dstRect = this.dstRect;
                shaderMatrix.setTranslate((float)dstRect.left, (float)dstRect.top);
                final int[] metaData2 = this.metaData;
                if (metaData2[2] == 90) {
                    this.shaderMatrix.preRotate(90.0f);
                    this.shaderMatrix.preTranslate(0.0f, (float)(-this.dstRect.width()));
                }
                else if (metaData2[2] == 180) {
                    this.shaderMatrix.preRotate(180.0f);
                    this.shaderMatrix.preTranslate((float)(-this.dstRect.width()), (float)(-this.dstRect.height()));
                }
                else if (metaData2[2] == 270) {
                    this.shaderMatrix.preRotate(270.0f);
                    this.shaderMatrix.preTranslate((float)(-this.dstRect.height()), 0.0f);
                }
                this.shaderMatrix.preScale(this.scaleX, this.scaleY);
                this.renderingShader.setLocalMatrix(this.shaderMatrix);
                final RectF actualDrawRect = this.actualDrawRect;
                final int roundRadius = this.roundRadius;
                canvas.drawRoundRect(actualDrawRect, (float)roundRadius, (float)roundRadius, paint);
            }
            else {
                final Rect dstRect2 = this.dstRect;
                canvas.translate((float)dstRect2.left, (float)dstRect2.top);
                final int[] metaData3 = this.metaData;
                if (metaData3[2] == 90) {
                    canvas.rotate(90.0f);
                    canvas.translate(0.0f, (float)(-this.dstRect.width()));
                }
                else if (metaData3[2] == 180) {
                    canvas.rotate(180.0f);
                    canvas.translate((float)(-this.dstRect.width()), (float)(-this.dstRect.height()));
                }
                else if (metaData3[2] == 270) {
                    canvas.rotate(270.0f);
                    canvas.translate((float)(-this.dstRect.height()), 0.0f);
                }
                canvas.scale(this.scaleX, this.scaleY);
                canvas.drawBitmap(this.renderingBitmap, 0.0f, 0.0f, this.getPaint());
            }
            if (this.isRunning) {
                final long max = Math.max(1L, this.invalidateAfter - (currentTimeMillis - this.lastFrameTime) - 17L);
                AnimatedFileDrawable.uiHandler.removeCallbacks(this.mInvalidateTask);
                AnimatedFileDrawable.uiHandler.postDelayed(this.mInvalidateTask, Math.min(max, this.invalidateAfter));
            }
        }
    }
    
    protected void finalize() throws Throwable {
        try {
            this.recycle();
        }
        finally {
            super.finalize();
        }
    }
    
    public Bitmap getAnimatedBitmap() {
        final Bitmap renderingBitmap = this.renderingBitmap;
        if (renderingBitmap != null) {
            return renderingBitmap;
        }
        final Bitmap nextRenderingBitmap = this.nextRenderingBitmap;
        if (nextRenderingBitmap != null) {
            return nextRenderingBitmap;
        }
        return null;
    }
    
    public Bitmap getBackgroundBitmap() {
        return this.backgroundBitmap;
    }
    
    public float getCurrentProgress() {
        if (this.metaData[4] == 0) {
            return 0.0f;
        }
        if (this.pendingSeekToUI >= 0L) {
            return this.pendingSeekToUI / (float)this.metaData[4];
        }
        final int[] metaData = this.metaData;
        return metaData[3] / (float)metaData[4];
    }
    
    public int getCurrentProgressMs() {
        if (this.pendingSeekToUI >= 0L) {
            return (int)this.pendingSeekToUI;
        }
        int n = this.nextRenderingBitmapTime;
        if (n == 0) {
            n = this.renderingBitmapTime;
        }
        return n;
    }
    
    public int getDurationMs() {
        return this.metaData[4];
    }
    
    public int getIntrinsicHeight() {
        final boolean decoderCreated = this.decoderCreated;
        int n = 0;
        if (decoderCreated) {
            final int[] metaData = this.metaData;
            if (metaData[2] != 90 && metaData[2] != 270) {
                n = metaData[1];
            }
            else {
                n = this.metaData[0];
            }
        }
        if (n == 0) {
            return AndroidUtilities.dp(100.0f);
        }
        return n;
    }
    
    public int getIntrinsicWidth() {
        final boolean decoderCreated = this.decoderCreated;
        int n = 0;
        if (decoderCreated) {
            final int[] metaData = this.metaData;
            if (metaData[2] != 90 && metaData[2] != 270) {
                n = metaData[0];
            }
            else {
                n = this.metaData[1];
            }
        }
        if (n == 0) {
            return AndroidUtilities.dp(100.0f);
        }
        return n;
    }
    
    public int getMinimumHeight() {
        final boolean decoderCreated = this.decoderCreated;
        int n = 0;
        if (decoderCreated) {
            final int[] metaData = this.metaData;
            if (metaData[2] != 90 && metaData[2] != 270) {
                n = metaData[1];
            }
            else {
                n = this.metaData[0];
            }
        }
        if (n == 0) {
            return AndroidUtilities.dp(100.0f);
        }
        return n;
    }
    
    public int getMinimumWidth() {
        final boolean decoderCreated = this.decoderCreated;
        int n = 0;
        if (decoderCreated) {
            final int[] metaData = this.metaData;
            if (metaData[2] != 90 && metaData[2] != 270) {
                n = metaData[0];
            }
            else {
                n = this.metaData[1];
            }
        }
        if (n == 0) {
            return AndroidUtilities.dp(100.0f);
        }
        return n;
    }
    
    public Bitmap getNextRenderingBitmap() {
        return this.nextRenderingBitmap;
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public int getOrientation() {
        return this.metaData[2];
    }
    
    public Bitmap getRenderingBitmap() {
        return this.renderingBitmap;
    }
    
    public boolean hasBitmap() {
        return this.nativePtr != 0L && (this.renderingBitmap != null || this.nextRenderingBitmap != null);
    }
    
    public boolean isLoadingStream() {
        final AnimatedFileDrawableStream stream = this.stream;
        return stream != null && stream.isWaitingForLoad();
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    public AnimatedFileDrawable makeCopy() {
        final AnimatedFileDrawableStream stream = this.stream;
        AnimatedFileDrawable animatedFileDrawable;
        if (stream != null) {
            animatedFileDrawable = new AnimatedFileDrawable(this.path, false, this.streamFileSize, stream.getDocument(), this.stream.getParentObject(), this.currentAccount);
        }
        else {
            animatedFileDrawable = new AnimatedFileDrawable(this.path, false, this.streamFileSize, null, null, this.currentAccount);
        }
        final int[] metaData = animatedFileDrawable.metaData;
        final int[] metaData2 = this.metaData;
        metaData[0] = metaData2[0];
        metaData[1] = metaData2[1];
        return animatedFileDrawable;
    }
    
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        this.applyTransformation = true;
    }
    
    public void recycle() {
        if (this.secondParentView != null) {
            this.recycleWithSecond = true;
            return;
        }
        this.isRunning = false;
        this.isRecycled = true;
        if (this.loadFrameTask == null) {
            if (this.nativePtr != 0L) {
                destroyDecoder(this.nativePtr);
                this.nativePtr = 0L;
            }
            final Bitmap renderingBitmap = this.renderingBitmap;
            if (renderingBitmap != null) {
                renderingBitmap.recycle();
                this.renderingBitmap = null;
            }
            final Bitmap nextRenderingBitmap = this.nextRenderingBitmap;
            if (nextRenderingBitmap != null) {
                nextRenderingBitmap.recycle();
                this.nextRenderingBitmap = null;
            }
            final DispatchQueue decodeQueue = this.decodeQueue;
            if (decodeQueue != null) {
                decodeQueue.recycle();
                this.decodeQueue = null;
            }
        }
        else {
            this.destroyWhenDone = true;
        }
        final AnimatedFileDrawableStream stream = this.stream;
        if (stream != null) {
            stream.cancel(true);
        }
    }
    
    public void seekTo(final long n, final boolean pendingRemoveLoading) {
        synchronized (this.sync) {
            this.pendingSeekTo = n;
            this.pendingSeekToUI = n;
            prepareToSeek(this.nativePtr);
            if (this.decoderCreated && this.stream != null) {
                this.stream.cancel(pendingRemoveLoading);
                this.pendingRemoveLoading = pendingRemoveLoading;
                int pendingRemoveLoadingFramesReset;
                if (this.pendingRemoveLoading) {
                    pendingRemoveLoadingFramesReset = 0;
                }
                else {
                    pendingRemoveLoadingFramesReset = 10;
                }
                this.pendingRemoveLoadingFramesReset = pendingRemoveLoadingFramesReset;
            }
        }
    }
    
    public void setActualDrawRect(final float n, final float n2, final float n3, final float n4) {
        this.actualDrawRect.set(n, n2, n3 + n, n4 + n2);
    }
    
    public void setAllowDecodeSingleFrame(final boolean decodeSingleFrame) {
        this.decodeSingleFrame = decodeSingleFrame;
        if (this.decodeSingleFrame) {
            this.scheduleNextGetFrame();
        }
    }
    
    public void setParentView(final View parentView) {
        if (this.parentView != null) {
            return;
        }
        this.parentView = parentView;
    }
    
    public void setRoundRadius(final int roundRadius) {
        this.roundRadius = roundRadius;
        this.getPaint().setFlags(3);
    }
    
    public void setSecondParentView(final View secondParentView) {
        this.secondParentView = secondParentView;
        if (secondParentView == null && this.recycleWithSecond) {
            this.recycle();
        }
    }
    
    public void setUseSharedQueue(final boolean useSharedQueue) {
        this.useSharedQueue = useSharedQueue;
    }
    
    public void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        this.scheduleNextGetFrame();
        runOnUiThread(this.mStartTask);
    }
    
    public void stop() {
        this.isRunning = false;
    }
}
