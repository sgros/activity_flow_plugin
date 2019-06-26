package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.widget.MediaController.MediaPlayerControl;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import pl.droidsonroids.gif.transforms.CornerRadiusTransform;
import pl.droidsonroids.gif.transforms.Transform;

public class GifDrawable extends Drawable implements Animatable, MediaPlayerControl {
   final Bitmap mBuffer;
   private final Rect mDstRect;
   final ScheduledThreadPoolExecutor mExecutor;
   final InvalidationHandler mInvalidationHandler;
   final boolean mIsRenderingTriggeredOnDraw;
   volatile boolean mIsRunning;
   final ConcurrentLinkedQueue mListeners;
   final GifInfoHandle mNativeInfoHandle;
   long mNextFrameRenderTime;
   protected final Paint mPaint;
   private final RenderTask mRenderTask;
   ScheduledFuture mRenderTaskSchedule;
   private int mScaledHeight;
   private int mScaledWidth;
   private final Rect mSrcRect;
   private ColorStateList mTint;
   private PorterDuffColorFilter mTintFilter;
   private Mode mTintMode;
   private Transform mTransform;

   public GifDrawable(@Nullable ContentResolver var1, @NonNull Uri var2) throws IOException {
      this(GifInfoHandle.openUri(var1, var2), (GifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
   }

   public GifDrawable(@NonNull AssetFileDescriptor var1) throws IOException {
      this(new GifInfoHandle(var1), (GifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
   }

   public GifDrawable(@NonNull AssetManager var1, @NonNull String var2) throws IOException {
      this(var1.openFd(var2));
   }

   public GifDrawable(@NonNull Resources var1, @DrawableRes @RawRes int var2) throws NotFoundException, IOException {
      this(var1.openRawResourceFd(var2));
      float var3 = GifViewUtils.getDensityScale(var1, var2);
      this.mScaledHeight = (int)((float)this.mNativeInfoHandle.getHeight() * var3);
      this.mScaledWidth = (int)((float)this.mNativeInfoHandle.getWidth() * var3);
   }

   public GifDrawable(@NonNull File var1) throws IOException {
      this(var1.getPath());
   }

   public GifDrawable(@NonNull FileDescriptor var1) throws IOException {
      this(new GifInfoHandle(var1), (GifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
   }

   public GifDrawable(@NonNull InputStream var1) throws IOException {
      this(new GifInfoHandle(var1), (GifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
   }

   public GifDrawable(@NonNull String var1) throws IOException {
      this(new GifInfoHandle(var1), (GifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
   }

   public GifDrawable(@NonNull ByteBuffer var1) throws IOException {
      this(new GifInfoHandle(var1), (GifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
   }

   GifDrawable(GifInfoHandle var1, GifDrawable var2, ScheduledThreadPoolExecutor var3, boolean var4) {
      boolean var5 = true;
      super();
      this.mIsRunning = true;
      this.mNextFrameRenderTime = Long.MIN_VALUE;
      this.mDstRect = new Rect();
      this.mPaint = new Paint(6);
      this.mListeners = new ConcurrentLinkedQueue();
      this.mRenderTask = new RenderTask(this);
      this.mIsRenderingTriggeredOnDraw = var4;
      if (var3 == null) {
         var3 = GifRenderingExecutor.getInstance();
      }

      this.mExecutor = (ScheduledThreadPoolExecutor)var3;
      this.mNativeInfoHandle = var1;
      Bitmap var40 = null;
      Object var6 = null;
      if (var2 != null) {
         label421: {
            GifInfoHandle var7 = var2.mNativeInfoHandle;
            synchronized(var7){}
            var40 = (Bitmap)var6;

            Throwable var10000;
            boolean var10001;
            label413: {
               label420: {
                  try {
                     if (var2.mNativeInfoHandle.isRecycled()) {
                        break label420;
                     }
                  } catch (Throwable var37) {
                     var10000 = var37;
                     var10001 = false;
                     break label413;
                  }

                  var40 = (Bitmap)var6;

                  try {
                     if (var2.mNativeInfoHandle.getHeight() < this.mNativeInfoHandle.getHeight()) {
                        break label420;
                     }
                  } catch (Throwable var36) {
                     var10000 = var36;
                     var10001 = false;
                     break label413;
                  }

                  var40 = (Bitmap)var6;

                  try {
                     if (var2.mNativeInfoHandle.getWidth() >= this.mNativeInfoHandle.getWidth()) {
                        var2.shutdown();
                        var40 = var2.mBuffer;
                        var40.eraseColor(0);
                     }
                  } catch (Throwable var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label413;
                  }
               }

               label401:
               try {
                  break label421;
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label401;
               }
            }

            while(true) {
               Throwable var38 = var10000;

               try {
                  throw var38;
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  continue;
               }
            }
         }
      }

      if (var40 == null) {
         this.mBuffer = Bitmap.createBitmap(this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight(), Config.ARGB_8888);
      } else {
         this.mBuffer = var40;
      }

      if (VERSION.SDK_INT >= 12) {
         Bitmap var39 = this.mBuffer;
         if (!var1.isOpaque()) {
            var4 = var5;
         } else {
            var4 = false;
         }

         var39.setHasAlpha(var4);
      }

      this.mSrcRect = new Rect(0, 0, this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight());
      this.mInvalidationHandler = new InvalidationHandler(this);
      this.mRenderTask.doWork();
      this.mScaledWidth = this.mNativeInfoHandle.getWidth();
      this.mScaledHeight = this.mNativeInfoHandle.getHeight();
   }

   public GifDrawable(@NonNull byte[] var1) throws IOException {
      this(new GifInfoHandle(var1), (GifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
   }

   private void cancelPendingRenderTask() {
      if (this.mRenderTaskSchedule != null) {
         this.mRenderTaskSchedule.cancel(false);
      }

      this.mInvalidationHandler.removeMessages(-1);
   }

   @Nullable
   public static GifDrawable createFromResource(@NonNull Resources var0, @DrawableRes @RawRes int var1) {
      GifDrawable var2;
      GifDrawable var4;
      try {
         var2 = new GifDrawable(var0, var1);
      } catch (IOException var3) {
         var4 = null;
         return var4;
      }

      var4 = var2;
      return var4;
   }

   private void shutdown() {
      this.mIsRunning = false;
      this.mInvalidationHandler.removeMessages(-1);
      this.mNativeInfoHandle.recycle();
   }

   private PorterDuffColorFilter updateTintFilter(ColorStateList var1, Mode var2) {
      PorterDuffColorFilter var3;
      if (var1 != null && var2 != null) {
         var3 = new PorterDuffColorFilter(var1.getColorForState(this.getState(), 0), var2);
      } else {
         var3 = null;
      }

      return var3;
   }

   public void addAnimationListener(@NonNull AnimationListener var1) {
      this.mListeners.add(var1);
   }

   public boolean canPause() {
      return true;
   }

   public boolean canSeekBackward() {
      boolean var1 = true;
      if (this.getNumberOfFrames() <= 1) {
         var1 = false;
      }

      return var1;
   }

   public boolean canSeekForward() {
      boolean var1 = true;
      if (this.getNumberOfFrames() <= 1) {
         var1 = false;
      }

      return var1;
   }

   public void draw(@NonNull Canvas var1) {
      boolean var2;
      if (this.mTintFilter != null && this.mPaint.getColorFilter() == null) {
         this.mPaint.setColorFilter(this.mTintFilter);
         var2 = true;
      } else {
         var2 = false;
      }

      if (this.mTransform == null) {
         var1.drawBitmap(this.mBuffer, this.mSrcRect, this.mDstRect, this.mPaint);
      } else {
         this.mTransform.onDraw(var1, this.mPaint, this.mBuffer);
      }

      if (var2) {
         this.mPaint.setColorFilter((ColorFilter)null);
      }

      if (this.mIsRenderingTriggeredOnDraw && this.mIsRunning && this.mNextFrameRenderTime != Long.MIN_VALUE) {
         long var3 = Math.max(0L, this.mNextFrameRenderTime - SystemClock.uptimeMillis());
         this.mNextFrameRenderTime = Long.MIN_VALUE;
         this.mExecutor.remove(this.mRenderTask);
         this.mRenderTaskSchedule = this.mExecutor.schedule(this.mRenderTask, var3, TimeUnit.MILLISECONDS);
      }

   }

   public long getAllocationByteCount() {
      long var1 = this.mNativeInfoHandle.getAllocationByteCount();
      if (VERSION.SDK_INT >= 19) {
         var1 += (long)this.mBuffer.getAllocationByteCount();
      } else {
         var1 += (long)this.getFrameByteCount();
      }

      return var1;
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

   @FloatRange(
      from = 0.0D
   )
   public float getCornerRadius() {
      float var1;
      if (this.mTransform instanceof CornerRadiusTransform) {
         var1 = ((CornerRadiusTransform)this.mTransform).getCornerRadius();
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public Bitmap getCurrentFrame() {
      Bitmap var1 = this.mBuffer.copy(this.mBuffer.getConfig(), this.mBuffer.isMutable());
      if (VERSION.SDK_INT >= 12) {
         var1.setHasAlpha(this.mBuffer.hasAlpha());
      }

      return var1;
   }

   public int getCurrentFrameIndex() {
      return this.mNativeInfoHandle.getCurrentFrameIndex();
   }

   public int getCurrentLoop() {
      int var1 = this.mNativeInfoHandle.getCurrentLoop();
      int var2 = var1;
      if (var1 != 0) {
         if (var1 < this.mNativeInfoHandle.getLoopCount()) {
            var2 = var1;
         } else {
            var2 = var1 - 1;
         }
      }

      return var2;
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

   public int getFrameDuration(@IntRange(from = 0L) int var1) {
      return this.mNativeInfoHandle.getFrameDuration(var1);
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
      byte var1;
      if (this.mNativeInfoHandle.isOpaque() && this.mPaint.getAlpha() >= 255) {
         var1 = -1;
      } else {
         var1 = -2;
      }

      return var1;
   }

   @NonNull
   public final Paint getPaint() {
      return this.mPaint;
   }

   public int getPixel(int var1, int var2) {
      if (var1 >= this.mNativeInfoHandle.getWidth()) {
         throw new IllegalArgumentException("x must be < width");
      } else if (var2 >= this.mNativeInfoHandle.getHeight()) {
         throw new IllegalArgumentException("y must be < height");
      } else {
         return this.mBuffer.getPixel(var1, var2);
      }
   }

   public void getPixels(@NonNull int[] var1) {
      this.mBuffer.getPixels(var1, 0, this.mNativeInfoHandle.getWidth(), 0, 0, this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight());
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
      boolean var1;
      if (!super.isStateful() && (this.mTint == null || !this.mTint.isStateful())) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected void onBoundsChange(Rect var1) {
      this.mDstRect.set(var1);
      if (this.mTransform != null) {
         this.mTransform.onBoundsChange(var1);
      }

   }

   protected boolean onStateChange(int[] var1) {
      boolean var2;
      if (this.mTint != null && this.mTintMode != null) {
         this.mTintFilter = this.updateTintFilter(this.mTint, this.mTintMode);
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void pause() {
      this.stop();
   }

   public void recycle() {
      this.shutdown();
      this.mBuffer.recycle();
   }

   public boolean removeAnimationListener(AnimationListener var1) {
      return this.mListeners.remove(var1);
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

   public void seekTo(@IntRange(from = 0L,to = 2147483647L) final int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Position is not positive");
      } else {
         this.mExecutor.execute(new SafeRunnable(this) {
            public void doWork() {
               GifDrawable.this.mNativeInfoHandle.seekToTime(var1, GifDrawable.this.mBuffer);
               this.mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
            }
         });
      }
   }

   public void seekToFrame(@IntRange(from = 0L,to = 2147483647L) final int var1) {
      if (var1 < 0) {
         throw new IndexOutOfBoundsException("Frame index is not positive");
      } else {
         this.mExecutor.execute(new SafeRunnable(this) {
            public void doWork() {
               GifDrawable.this.mNativeInfoHandle.seekToFrame(var1, GifDrawable.this.mBuffer);
               GifDrawable.this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
            }
         });
      }
   }

   public Bitmap seekToFrameAndGet(@IntRange(from = 0L,to = 2147483647L) int param1) {
      // $FF: Couldn't be decompiled
   }

   public Bitmap seekToPositionAndGet(@IntRange(from = 0L,to = 2147483647L) int param1) {
      // $FF: Couldn't be decompiled
   }

   public void setAlpha(@IntRange(from = 0L,to = 255L) int var1) {
      this.mPaint.setAlpha(var1);
   }

   public void setColorFilter(@Nullable ColorFilter var1) {
      this.mPaint.setColorFilter(var1);
   }

   public void setCornerRadius(@FloatRange(from = 0.0D) float var1) {
      this.mTransform = new CornerRadiusTransform(var1);
   }

   @Deprecated
   public void setDither(boolean var1) {
      this.mPaint.setDither(var1);
      this.invalidateSelf();
   }

   public void setFilterBitmap(boolean var1) {
      this.mPaint.setFilterBitmap(var1);
      this.invalidateSelf();
   }

   public void setLoopCount(@IntRange(from = 0L,to = 65535L) int var1) {
      this.mNativeInfoHandle.setLoopCount(var1);
   }

   public void setSpeed(@FloatRange(from = 0.0D,fromInclusive = false) float var1) {
      this.mNativeInfoHandle.setSpeedFactor(var1);
   }

   public void setTintList(ColorStateList var1) {
      this.mTint = var1;
      this.mTintFilter = this.updateTintFilter(var1, this.mTintMode);
      this.invalidateSelf();
   }

   public void setTintMode(@NonNull Mode var1) {
      this.mTintMode = var1;
      this.mTintFilter = this.updateTintFilter(this.mTint, var1);
      this.invalidateSelf();
   }

   public void setTransform(@Nullable Transform var1) {
      this.mTransform = var1;
   }

   public boolean setVisible(boolean var1, boolean var2) {
      boolean var3 = super.setVisible(var1, var2);
      if (!this.mIsRenderingTriggeredOnDraw) {
         if (var1) {
            if (var2) {
               this.reset();
            }

            if (var3) {
               this.start();
            }
         } else if (var3) {
            this.stop();
         }
      }

      return var3;
   }

   public void start() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label158: {
         try {
            if (this.mIsRunning) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label158;
         }

         try {
            this.mIsRunning = true;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label158;
         }

         this.startAnimation(this.mNativeInfoHandle.restoreRemainder());
         return;
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   void startAnimation(long var1) {
      if (this.mIsRenderingTriggeredOnDraw) {
         this.mNextFrameRenderTime = 0L;
         this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
      } else {
         this.cancelPendingRenderTask();
         this.mRenderTaskSchedule = this.mExecutor.schedule(this.mRenderTask, Math.max(var1, 0L), TimeUnit.MILLISECONDS);
      }

   }

   public void stop() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label158: {
         try {
            if (!this.mIsRunning) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label158;
         }

         try {
            this.mIsRunning = false;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label158;
         }

         this.cancelPendingRenderTask();
         this.mNativeInfoHandle.saveRemainder();
         return;
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public String toString() {
      return String.format(Locale.ENGLISH, "GIF: size: %dx%d, frames: %d, error: %d", this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight(), this.mNativeInfoHandle.getNumberOfFrames(), this.mNativeInfoHandle.getNativeErrorCode());
   }
}
