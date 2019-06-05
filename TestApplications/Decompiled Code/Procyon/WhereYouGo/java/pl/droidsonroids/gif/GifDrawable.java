// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import java.util.Locale;
import android.support.annotation.IntRange;
import android.support.annotation.FloatRange;
import pl.droidsonroids.gif.transforms.CornerRadiusTransform;
import java.util.concurrent.TimeUnit;
import android.os.SystemClock;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.os.Build$VERSION;
import android.graphics.Bitmap$Config;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.io.FileDescriptor;
import java.io.File;
import android.content.res.Resources$NotFoundException;
import android.support.annotation.RawRes;
import android.support.annotation.DrawableRes;
import android.content.res.Resources;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;
import java.io.IOException;
import android.support.annotation.NonNull;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.content.ContentResolver;
import pl.droidsonroids.gif.transforms.Transform;
import android.graphics.PorterDuff$Mode;
import android.graphics.PorterDuffColorFilter;
import android.content.res.ColorStateList;
import java.util.concurrent.ScheduledFuture;
import android.graphics.Paint;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.widget.MediaController$MediaPlayerControl;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public class GifDrawable extends Drawable implements Animatable, MediaController$MediaPlayerControl
{
    final Bitmap mBuffer;
    private final Rect mDstRect;
    final ScheduledThreadPoolExecutor mExecutor;
    final InvalidationHandler mInvalidationHandler;
    final boolean mIsRenderingTriggeredOnDraw;
    volatile boolean mIsRunning;
    final ConcurrentLinkedQueue<AnimationListener> mListeners;
    final GifInfoHandle mNativeInfoHandle;
    long mNextFrameRenderTime;
    protected final Paint mPaint;
    private final RenderTask mRenderTask;
    ScheduledFuture<?> mRenderTaskSchedule;
    private int mScaledHeight;
    private int mScaledWidth;
    private final Rect mSrcRect;
    private ColorStateList mTint;
    private PorterDuffColorFilter mTintFilter;
    private PorterDuff$Mode mTintMode;
    private Transform mTransform;
    
    public GifDrawable(@Nullable final ContentResolver contentResolver, @NonNull final Uri uri) throws IOException {
        this(GifInfoHandle.openUri(contentResolver, uri), null, null, true);
    }
    
    public GifDrawable(@NonNull final AssetFileDescriptor assetFileDescriptor) throws IOException {
        this(new GifInfoHandle(assetFileDescriptor), null, null, true);
    }
    
    public GifDrawable(@NonNull final AssetManager assetManager, @NonNull final String s) throws IOException {
        this(assetManager.openFd(s));
    }
    
    public GifDrawable(@NonNull final Resources resources, @DrawableRes @RawRes final int n) throws Resources$NotFoundException, IOException {
        this(resources.openRawResourceFd(n));
        final float densityScale = GifViewUtils.getDensityScale(resources, n);
        this.mScaledHeight = (int)(this.mNativeInfoHandle.getHeight() * densityScale);
        this.mScaledWidth = (int)(this.mNativeInfoHandle.getWidth() * densityScale);
    }
    
    public GifDrawable(@NonNull final File file) throws IOException {
        this(file.getPath());
    }
    
    public GifDrawable(@NonNull final FileDescriptor fileDescriptor) throws IOException {
        this(new GifInfoHandle(fileDescriptor), null, null, true);
    }
    
    public GifDrawable(@NonNull final InputStream inputStream) throws IOException {
        this(new GifInfoHandle(inputStream), null, null, true);
    }
    
    public GifDrawable(@NonNull final String s) throws IOException {
        this(new GifInfoHandle(s), null, null, true);
    }
    
    public GifDrawable(@NonNull final ByteBuffer byteBuffer) throws IOException {
        this(new GifInfoHandle(byteBuffer), null, null, true);
    }
    
    GifDrawable(final GifInfoHandle mNativeInfoHandle, GifDrawable mBuffer, ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, final boolean mIsRenderingTriggeredOnDraw) {
        final boolean b = true;
        this.mIsRunning = true;
        this.mNextFrameRenderTime = Long.MIN_VALUE;
        this.mDstRect = new Rect();
        this.mPaint = new Paint(6);
        this.mListeners = new ConcurrentLinkedQueue<AnimationListener>();
        this.mRenderTask = new RenderTask(this);
        this.mIsRenderingTriggeredOnDraw = mIsRenderingTriggeredOnDraw;
        while (true) {
            while (true) {
                Label_0076: {
                    if (scheduledThreadPoolExecutor != null) {
                        break Label_0076;
                    }
                    Label_0301: {
                        break Label_0301;
                    Label_0226_Outer:
                        while (true) {
                            final GifInfoHandle mNativeInfoHandle2 = mBuffer.mNativeInfoHandle;
                            // monitorenter(mNativeInfoHandle2)
                            final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor2;
                            scheduledThreadPoolExecutor = scheduledThreadPoolExecutor2;
                            while (true) {
                            Label_0322:
                                while (true) {
                                    Label_0314: {
                                        try {
                                            if (!mBuffer.mNativeInfoHandle.isRecycled()) {
                                                scheduledThreadPoolExecutor = scheduledThreadPoolExecutor2;
                                                if (mBuffer.mNativeInfoHandle.getHeight() >= this.mNativeInfoHandle.getHeight()) {
                                                    scheduledThreadPoolExecutor = scheduledThreadPoolExecutor2;
                                                    if (mBuffer.mNativeInfoHandle.getWidth() >= this.mNativeInfoHandle.getWidth()) {
                                                        mBuffer.shutdown();
                                                        scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor)mBuffer.mBuffer;
                                                        ((Bitmap)scheduledThreadPoolExecutor).eraseColor(0);
                                                    }
                                                }
                                            }
                                            // monitorexit(mNativeInfoHandle2)
                                            if (scheduledThreadPoolExecutor == null) {
                                                this.mBuffer = Bitmap.createBitmap(this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight(), Bitmap$Config.ARGB_8888);
                                                if (Build$VERSION.SDK_INT >= 12) {
                                                    mBuffer = (GifDrawable)this.mBuffer;
                                                    if (mNativeInfoHandle.isOpaque()) {
                                                        break Label_0322;
                                                    }
                                                    final boolean hasAlpha = b;
                                                    ((Bitmap)mBuffer).setHasAlpha(hasAlpha);
                                                }
                                                this.mSrcRect = new Rect(0, 0, this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight());
                                                this.mInvalidationHandler = new InvalidationHandler(this);
                                                this.mRenderTask.doWork();
                                                this.mScaledWidth = this.mNativeInfoHandle.getWidth();
                                                this.mScaledHeight = this.mNativeInfoHandle.getHeight();
                                                return;
                                            }
                                            break Label_0314;
                                            scheduledThreadPoolExecutor = GifRenderingExecutor.getInstance();
                                            break;
                                        }
                                        finally {
                                        }
                                        // monitorexit(mNativeInfoHandle2)
                                    }
                                    this.mBuffer = (Bitmap)scheduledThreadPoolExecutor;
                                    continue Label_0226_Outer;
                                }
                                final boolean hasAlpha = false;
                                continue;
                            }
                        }
                    }
                }
                this.mExecutor = scheduledThreadPoolExecutor;
                this.mNativeInfoHandle = mNativeInfoHandle;
                scheduledThreadPoolExecutor = null;
                final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor2 = null;
                if (mBuffer != null) {
                    continue;
                }
                break;
            }
            continue;
        }
    }
    
    public GifDrawable(@NonNull final byte[] array) throws IOException {
        this(new GifInfoHandle(array), null, null, true);
    }
    
    private void cancelPendingRenderTask() {
        if (this.mRenderTaskSchedule != null) {
            this.mRenderTaskSchedule.cancel(false);
        }
        this.mInvalidationHandler.removeMessages(-1);
    }
    
    @Nullable
    public static GifDrawable createFromResource(@NonNull final Resources resources, @DrawableRes @RawRes final int n) {
        try {
            return new GifDrawable(resources, n);
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private void shutdown() {
        this.mIsRunning = false;
        this.mInvalidationHandler.removeMessages(-1);
        this.mNativeInfoHandle.recycle();
    }
    
    private PorterDuffColorFilter updateTintFilter(final ColorStateList list, final PorterDuff$Mode porterDuff$Mode) {
        PorterDuffColorFilter porterDuffColorFilter;
        if (list == null || porterDuff$Mode == null) {
            porterDuffColorFilter = null;
        }
        else {
            porterDuffColorFilter = new PorterDuffColorFilter(list.getColorForState(this.getState(), 0), porterDuff$Mode);
        }
        return porterDuffColorFilter;
    }
    
    public void addAnimationListener(@NonNull final AnimationListener e) {
        this.mListeners.add(e);
    }
    
    public boolean canPause() {
        return true;
    }
    
    public boolean canSeekBackward() {
        boolean b = true;
        if (this.getNumberOfFrames() <= 1) {
            b = false;
        }
        return b;
    }
    
    public boolean canSeekForward() {
        boolean b = true;
        if (this.getNumberOfFrames() <= 1) {
            b = false;
        }
        return b;
    }
    
    public void draw(@NonNull final Canvas canvas) {
        int n;
        if (this.mTintFilter != null && this.mPaint.getColorFilter() == null) {
            this.mPaint.setColorFilter((ColorFilter)this.mTintFilter);
            n = 1;
        }
        else {
            n = 0;
        }
        if (this.mTransform == null) {
            canvas.drawBitmap(this.mBuffer, this.mSrcRect, this.mDstRect, this.mPaint);
        }
        else {
            this.mTransform.onDraw(canvas, this.mPaint, this.mBuffer);
        }
        if (n != 0) {
            this.mPaint.setColorFilter((ColorFilter)null);
        }
        if (this.mIsRenderingTriggeredOnDraw && this.mIsRunning && this.mNextFrameRenderTime != Long.MIN_VALUE) {
            final long max = Math.max(0L, this.mNextFrameRenderTime - SystemClock.uptimeMillis());
            this.mNextFrameRenderTime = Long.MIN_VALUE;
            this.mExecutor.remove(this.mRenderTask);
            this.mRenderTaskSchedule = this.mExecutor.schedule(this.mRenderTask, max, TimeUnit.MILLISECONDS);
        }
    }
    
    public long getAllocationByteCount() {
        final long allocationByteCount = this.mNativeInfoHandle.getAllocationByteCount();
        long n;
        if (Build$VERSION.SDK_INT >= 19) {
            n = allocationByteCount + this.mBuffer.getAllocationByteCount();
        }
        else {
            n = allocationByteCount + this.getFrameByteCount();
        }
        return n;
    }
    
    public int getAlpha() {
        return this.mPaint.getAlpha();
    }
    
    public int getAudioSessionId() {
        return 0;
    }
    
    public int getBufferPercentage() {
        return 100;
    }
    
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }
    
    @Nullable
    public String getComment() {
        return this.mNativeInfoHandle.getComment();
    }
    
    @FloatRange(from = 0.0)
    public float getCornerRadius() {
        float cornerRadius;
        if (this.mTransform instanceof CornerRadiusTransform) {
            cornerRadius = ((CornerRadiusTransform)this.mTransform).getCornerRadius();
        }
        else {
            cornerRadius = 0.0f;
        }
        return cornerRadius;
    }
    
    public Bitmap getCurrentFrame() {
        final Bitmap copy = this.mBuffer.copy(this.mBuffer.getConfig(), this.mBuffer.isMutable());
        if (Build$VERSION.SDK_INT >= 12) {
            copy.setHasAlpha(this.mBuffer.hasAlpha());
        }
        return copy;
    }
    
    public int getCurrentFrameIndex() {
        return this.mNativeInfoHandle.getCurrentFrameIndex();
    }
    
    public int getCurrentLoop() {
        int currentLoop;
        final int n = currentLoop = this.mNativeInfoHandle.getCurrentLoop();
        if (n != 0) {
            if (n < this.mNativeInfoHandle.getLoopCount()) {
                currentLoop = n;
            }
            else {
                currentLoop = n - 1;
            }
        }
        return currentLoop;
    }
    
    public int getCurrentPosition() {
        return this.mNativeInfoHandle.getCurrentPosition();
    }
    
    public int getDuration() {
        return this.mNativeInfoHandle.getDuration();
    }
    
    @NonNull
    public GifError getError() {
        return GifError.fromCode(this.mNativeInfoHandle.getNativeErrorCode());
    }
    
    public int getFrameByteCount() {
        return this.mBuffer.getRowBytes() * this.mBuffer.getHeight();
    }
    
    public int getFrameDuration(@IntRange(from = 0L) final int n) {
        return this.mNativeInfoHandle.getFrameDuration(n);
    }
    
    public long getInputSourceByteCount() {
        return this.mNativeInfoHandle.getSourceLength();
    }
    
    public int getIntrinsicHeight() {
        return this.mScaledHeight;
    }
    
    public int getIntrinsicWidth() {
        return this.mScaledWidth;
    }
    
    public int getLoopCount() {
        return this.mNativeInfoHandle.getLoopCount();
    }
    
    public long getMetadataAllocationByteCount() {
        return this.mNativeInfoHandle.getMetadataByteCount();
    }
    
    public int getNumberOfFrames() {
        return this.mNativeInfoHandle.getNumberOfFrames();
    }
    
    public int getOpacity() {
        int n;
        if (!this.mNativeInfoHandle.isOpaque() || this.mPaint.getAlpha() < 255) {
            n = -2;
        }
        else {
            n = -1;
        }
        return n;
    }
    
    @NonNull
    public final Paint getPaint() {
        return this.mPaint;
    }
    
    public int getPixel(final int n, final int n2) {
        if (n >= this.mNativeInfoHandle.getWidth()) {
            throw new IllegalArgumentException("x must be < width");
        }
        if (n2 >= this.mNativeInfoHandle.getHeight()) {
            throw new IllegalArgumentException("y must be < height");
        }
        return this.mBuffer.getPixel(n, n2);
    }
    
    public void getPixels(@NonNull final int[] array) {
        this.mBuffer.getPixels(array, 0, this.mNativeInfoHandle.getWidth(), 0, 0, this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight());
    }
    
    @Nullable
    public Transform getTransform() {
        return this.mTransform;
    }
    
    public boolean isAnimationCompleted() {
        return this.mNativeInfoHandle.isAnimationCompleted();
    }
    
    public boolean isPlaying() {
        return this.mIsRunning;
    }
    
    public boolean isRecycled() {
        return this.mNativeInfoHandle.isRecycled();
    }
    
    public boolean isRunning() {
        return this.mIsRunning;
    }
    
    public boolean isStateful() {
        return super.isStateful() || (this.mTint != null && this.mTint.isStateful());
    }
    
    protected void onBoundsChange(final Rect rect) {
        this.mDstRect.set(rect);
        if (this.mTransform != null) {
            this.mTransform.onBoundsChange(rect);
        }
    }
    
    protected boolean onStateChange(final int[] array) {
        boolean b;
        if (this.mTint != null && this.mTintMode != null) {
            this.mTintFilter = this.updateTintFilter(this.mTint, this.mTintMode);
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public void pause() {
        this.stop();
    }
    
    public void recycle() {
        this.shutdown();
        this.mBuffer.recycle();
    }
    
    public boolean removeAnimationListener(final AnimationListener o) {
        return this.mListeners.remove(o);
    }
    
    public void reset() {
        this.mExecutor.execute(new SafeRunnable(this) {
            public void doWork() {
                if (GifDrawable.this.mNativeInfoHandle.reset()) {
                    GifDrawable.this.start();
                }
            }
        });
    }
    
    public void seekTo(@IntRange(from = 0L, to = 2147483647L) final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Position is not positive");
        }
        this.mExecutor.execute(new SafeRunnable(this) {
            public void doWork() {
                GifDrawable.this.mNativeInfoHandle.seekToTime(n, GifDrawable.this.mBuffer);
                this.mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
            }
        });
    }
    
    public void seekToFrame(@IntRange(from = 0L, to = 2147483647L) final int n) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("Frame index is not positive");
        }
        this.mExecutor.execute(new SafeRunnable(this) {
            public void doWork() {
                GifDrawable.this.mNativeInfoHandle.seekToFrame(n, GifDrawable.this.mBuffer);
                GifDrawable.this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
            }
        });
    }
    
    public Bitmap seekToFrameAndGet(@IntRange(from = 0L, to = 2147483647L) final int n) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("Frame index is not positive");
        }
        synchronized (this.mNativeInfoHandle) {
            this.mNativeInfoHandle.seekToFrame(n, this.mBuffer);
            final Bitmap currentFrame = this.getCurrentFrame();
            // monitorexit(this.mNativeInfoHandle)
            this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
            return currentFrame;
        }
    }
    
    public Bitmap seekToPositionAndGet(@IntRange(from = 0L, to = 2147483647L) final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Position is not positive");
        }
        synchronized (this.mNativeInfoHandle) {
            this.mNativeInfoHandle.seekToTime(n, this.mBuffer);
            final Bitmap currentFrame = this.getCurrentFrame();
            // monitorexit(this.mNativeInfoHandle)
            this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
            return currentFrame;
        }
    }
    
    public void setAlpha(@IntRange(from = 0L, to = 255L) final int alpha) {
        this.mPaint.setAlpha(alpha);
    }
    
    public void setColorFilter(@Nullable final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
    
    public void setCornerRadius(@FloatRange(from = 0.0) final float n) {
        this.mTransform = new CornerRadiusTransform(n);
    }
    
    @Deprecated
    public void setDither(final boolean dither) {
        this.mPaint.setDither(dither);
        this.invalidateSelf();
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.mPaint.setFilterBitmap(filterBitmap);
        this.invalidateSelf();
    }
    
    public void setLoopCount(@IntRange(from = 0L, to = 65535L) final int loopCount) {
        this.mNativeInfoHandle.setLoopCount(loopCount);
    }
    
    public void setSpeed(@FloatRange(from = 0.0, fromInclusive = false) final float speedFactor) {
        this.mNativeInfoHandle.setSpeedFactor(speedFactor);
    }
    
    public void setTintList(final ColorStateList mTint) {
        this.mTint = mTint;
        this.mTintFilter = this.updateTintFilter(mTint, this.mTintMode);
        this.invalidateSelf();
    }
    
    public void setTintMode(@NonNull final PorterDuff$Mode mTintMode) {
        this.mTintMode = mTintMode;
        this.mTintFilter = this.updateTintFilter(this.mTint, mTintMode);
        this.invalidateSelf();
    }
    
    public void setTransform(@Nullable final Transform mTransform) {
        this.mTransform = mTransform;
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        final boolean setVisible = super.setVisible(b, b2);
        if (!this.mIsRenderingTriggeredOnDraw) {
            if (b) {
                if (b2) {
                    this.reset();
                }
                if (setVisible) {
                    this.start();
                }
            }
            else if (setVisible) {
                this.stop();
            }
        }
        return setVisible;
    }
    
    public void start() {
        synchronized (this) {
            if (!this.mIsRunning) {
                this.mIsRunning = true;
                // monitorexit(this)
                this.startAnimation(this.mNativeInfoHandle.restoreRemainder());
            }
        }
    }
    
    void startAnimation(final long a) {
        if (this.mIsRenderingTriggeredOnDraw) {
            this.mNextFrameRenderTime = 0L;
            this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
        }
        else {
            this.cancelPendingRenderTask();
            this.mRenderTaskSchedule = this.mExecutor.schedule(this.mRenderTask, Math.max(a, 0L), TimeUnit.MILLISECONDS);
        }
    }
    
    public void stop() {
        synchronized (this) {
            if (this.mIsRunning) {
                this.mIsRunning = false;
                // monitorexit(this)
                this.cancelPendingRenderTask();
                this.mNativeInfoHandle.saveRemainder();
            }
        }
    }
    
    public String toString() {
        return String.format(Locale.ENGLISH, "GIF: size: %dx%d, frames: %d, error: %d", this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight(), this.mNativeInfoHandle.getNumberOfFrames(), this.mNativeInfoHandle.getNativeErrorCode());
    }
}
