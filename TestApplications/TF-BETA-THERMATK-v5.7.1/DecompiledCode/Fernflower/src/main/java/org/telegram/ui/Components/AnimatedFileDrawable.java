package org.telegram.ui.Components;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimatedFileDrawableStream;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.tgnet.TLRPC;

public class AnimatedFileDrawable extends BitmapDrawable implements Animatable {
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
   private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2, new DiscardPolicy());
   private static final Handler uiHandler = new Handler(Looper.getMainLooper());
   private RectF actualDrawRect = new RectF();
   private boolean applyTransformation;
   private Bitmap backgroundBitmap;
   private int backgroundBitmapTime;
   private BitmapShader backgroundShader;
   private int currentAccount;
   private DispatchQueue decodeQueue;
   private boolean decodeSingleFrame;
   private boolean decoderCreated;
   private boolean destroyWhenDone;
   private final android.graphics.Rect dstRect = new android.graphics.Rect();
   private int invalidateAfter = 50;
   private volatile boolean isRecycled;
   private volatile boolean isRunning;
   private long lastFrameDecodeTime;
   private long lastFrameTime;
   private int lastTimeStamp;
   private Runnable loadFrameRunnable = new Runnable() {
      public void run() {
         // $FF: Couldn't be decompiled
      }
   };
   private Runnable loadFrameTask;
   protected final Runnable mInvalidateTask = new _$$Lambda$AnimatedFileDrawable$D96GYyKDLrUXCvNeQ7iluME9Yq4(this);
   private final Runnable mStartTask = new _$$Lambda$AnimatedFileDrawable$AmB2znRBjaDHOIPDjH2S8BYovYQ(this);
   private final int[] metaData = new int[5];
   public volatile long nativePtr;
   private Bitmap nextRenderingBitmap;
   private int nextRenderingBitmapTime;
   private BitmapShader nextRenderingShader;
   private View parentView = null;
   private File path;
   private boolean pendingRemoveLoading;
   private int pendingRemoveLoadingFramesReset;
   private volatile long pendingSeekTo = -1L;
   private volatile long pendingSeekToUI = -1L;
   private boolean recycleWithSecond;
   private Bitmap renderingBitmap;
   private int renderingBitmapTime;
   private BitmapShader renderingShader;
   private int roundRadius;
   private float scaleX = 1.0F;
   private float scaleY = 1.0F;
   private View secondParentView = null;
   private Matrix shaderMatrix = new Matrix();
   private boolean singleFrameDecoded;
   private AnimatedFileDrawableStream stream;
   private long streamFileSize;
   private final Object sync = new Object();
   private Runnable uiRunnable = new Runnable() {
      public void run() {
         if (AnimatedFileDrawable.this.destroyWhenDone && AnimatedFileDrawable.this.nativePtr != 0L) {
            AnimatedFileDrawable.destroyDecoder(AnimatedFileDrawable.this.nativePtr);
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

         } else {
            if (AnimatedFileDrawable.this.stream != null && AnimatedFileDrawable.this.pendingRemoveLoading) {
               FileLoader.getInstance(AnimatedFileDrawable.this.currentAccount).removeLoadingVideo(AnimatedFileDrawable.this.stream.getDocument(), false, false);
            }

            if (AnimatedFileDrawable.this.pendingRemoveLoadingFramesReset <= 0) {
               AnimatedFileDrawable.this.pendingRemoveLoading = true;
            } else {
               AnimatedFileDrawable.access$1010(AnimatedFileDrawable.this);
            }

            AnimatedFileDrawable.this.singleFrameDecoded = true;
            AnimatedFileDrawable.this.loadFrameTask = null;
            AnimatedFileDrawable var1 = AnimatedFileDrawable.this;
            var1.nextRenderingBitmap = var1.backgroundBitmap;
            var1 = AnimatedFileDrawable.this;
            var1.nextRenderingBitmapTime = var1.backgroundBitmapTime;
            var1 = AnimatedFileDrawable.this;
            var1.nextRenderingShader = var1.backgroundShader;
            if (AnimatedFileDrawable.this.metaData[3] < AnimatedFileDrawable.this.lastTimeStamp) {
               AnimatedFileDrawable.this.lastTimeStamp = 0;
            }

            if (AnimatedFileDrawable.this.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp != 0) {
               var1 = AnimatedFileDrawable.this;
               var1.invalidateAfter = var1.metaData[3] - AnimatedFileDrawable.this.lastTimeStamp;
            }

            if (AnimatedFileDrawable.this.pendingSeekToUI >= 0L && AnimatedFileDrawable.this.pendingSeekTo == -1L) {
               AnimatedFileDrawable.this.pendingSeekToUI = -1L;
               AnimatedFileDrawable.this.invalidateAfter = 0;
            }

            var1 = AnimatedFileDrawable.this;
            var1.lastTimeStamp = var1.metaData[3];
            if (AnimatedFileDrawable.this.secondParentView != null) {
               AnimatedFileDrawable.this.secondParentView.invalidate();
            } else if (AnimatedFileDrawable.this.parentView != null) {
               AnimatedFileDrawable.this.parentView.invalidate();
            }

            AnimatedFileDrawable.this.scheduleNextGetFrame();
         }
      }
   };
   private Runnable uiRunnableNoFrame = new Runnable() {
      public void run() {
         if (AnimatedFileDrawable.this.destroyWhenDone && AnimatedFileDrawable.this.nativePtr != 0L) {
            AnimatedFileDrawable.destroyDecoder(AnimatedFileDrawable.this.nativePtr);
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

         } else {
            AnimatedFileDrawable.this.loadFrameTask = null;
            AnimatedFileDrawable.this.scheduleNextGetFrame();
         }
      }
   };
   private boolean useSharedQueue;

   public AnimatedFileDrawable(File var1, boolean var2, long var3, TLRPC.Document var5, Object var6, int var7) {
      this.path = var1;
      this.streamFileSize = var3;
      this.currentAccount = var7;
      this.getPaint().setFlags(2);
      if (var3 != 0L && var5 != null) {
         this.stream = new AnimatedFileDrawableStream(var5, var6, var7);
      }

      if (var2) {
         this.nativePtr = createDecoder(var1.getAbsolutePath(), this.metaData, this.currentAccount, this.streamFileSize, this.stream);
         this.decoderCreated = true;
      }

   }

   // $FF: synthetic method
   static int access$1010(AnimatedFileDrawable var0) {
      int var1 = var0.pendingRemoveLoadingFramesReset--;
      return var1;
   }

   // $FF: synthetic method
   static int access$1402(AnimatedFileDrawable var0, int var1) {
      var0.backgroundBitmapTime = var1;
      return var1;
   }

   // $FF: synthetic method
   static BitmapShader access$1602(AnimatedFileDrawable var0, BitmapShader var1) {
      var0.backgroundShader = var1;
      return var1;
   }

   // $FF: synthetic method
   static long access$2102(AnimatedFileDrawable var0, long var1) {
      var0.pendingSeekTo = var1;
      return var1;
   }

   // $FF: synthetic method
   static boolean access$2400(AnimatedFileDrawable var0) {
      return var0.isRecycled;
   }

   // $FF: synthetic method
   static boolean access$2500(AnimatedFileDrawable var0) {
      return var0.decoderCreated;
   }

   // $FF: synthetic method
   static boolean access$2502(AnimatedFileDrawable var0, boolean var1) {
      var0.decoderCreated = var1;
      return var1;
   }

   // $FF: synthetic method
   static File access$2600(AnimatedFileDrawable var0) {
      return var0.path;
   }

   // $FF: synthetic method
   static long access$2700(AnimatedFileDrawable var0) {
      return var0.streamFileSize;
   }

   // $FF: synthetic method
   static long access$2800(String var0, int[] var1, int var2, long var3, Object var5) {
      return createDecoder(var0, var1, var2, var3, var5);
   }

   // $FF: synthetic method
   static int access$2900(AnimatedFileDrawable var0) {
      return var0.roundRadius;
   }

   // $FF: synthetic method
   static Object access$3000(AnimatedFileDrawable var0) {
      return var0.sync;
   }

   // $FF: synthetic method
   static void access$3100(long var0, long var2) {
      seekToMs(var0, var2);
   }

   // $FF: synthetic method
   static long access$3202(AnimatedFileDrawable var0, long var1) {
      var0.lastFrameDecodeTime = var1;
      return var1;
   }

   // $FF: synthetic method
   static int access$3300(long var0, Bitmap var2, int[] var3, int var4) {
      return getVideoFrame(var0, var2, var3, var4);
   }

   // $FF: synthetic method
   static Runnable access$3400(AnimatedFileDrawable var0) {
      return var0.uiRunnableNoFrame;
   }

   // $FF: synthetic method
   static Runnable access$3500(AnimatedFileDrawable var0) {
      return var0.uiRunnable;
   }

   private static native long createDecoder(String var0, int[] var1, int var2, long var3, Object var5);

   private static native void destroyDecoder(long var0);

   private static native int getVideoFrame(long var0, Bitmap var2, int[] var3, int var4);

   public static native void getVideoInfo(String var0, int[] var1);

   private static native void prepareToSeek(long var0);

   protected static void runOnUiThread(Runnable var0) {
      if (Looper.myLooper() == uiHandler.getLooper()) {
         var0.run();
      } else {
         uiHandler.post(var0);
      }

   }

   private void scheduleNextGetFrame() {
      if (this.loadFrameTask == null) {
         long var1 = this.nativePtr;
         long var3 = 0L;
         if ((var1 != 0L || !this.decoderCreated) && !this.destroyWhenDone) {
            if (!this.isRunning) {
               boolean var5 = this.decodeSingleFrame;
               if (!var5 || var5 && this.singleFrameDecoded) {
                  return;
               }
            }

            if (this.lastFrameDecodeTime != 0L) {
               int var6 = this.invalidateAfter;
               var3 = Math.min((long)var6, Math.max(0L, (long)var6 - (System.currentTimeMillis() - this.lastFrameDecodeTime)));
            }

            if (this.useSharedQueue) {
               ScheduledThreadPoolExecutor var7 = executor;
               Runnable var8 = this.loadFrameRunnable;
               this.loadFrameTask = var8;
               var7.schedule(var8, var3, TimeUnit.MILLISECONDS);
            } else {
               if (this.decodeQueue == null) {
                  StringBuilder var10 = new StringBuilder();
                  var10.append("decodeQueue");
                  var10.append(this);
                  this.decodeQueue = new DispatchQueue(var10.toString());
               }

               DispatchQueue var11 = this.decodeQueue;
               Runnable var9 = this.loadFrameRunnable;
               this.loadFrameTask = var9;
               var11.postRunnable(var9, var3);
            }
         }
      }

   }

   private static native void seekToMs(long var0, long var2);

   private static native void stopDecoder(long var0);

   public void draw(Canvas var1) {
      if ((this.nativePtr != 0L || !this.decoderCreated) && !this.destroyWhenDone) {
         long var2 = System.currentTimeMillis();
         Bitmap var4;
         if (this.isRunning) {
            if (this.renderingBitmap == null && this.nextRenderingBitmap == null) {
               this.scheduleNextGetFrame();
            } else if (this.nextRenderingBitmap != null && (this.renderingBitmap == null || Math.abs(var2 - this.lastFrameTime) >= (long)this.invalidateAfter)) {
               this.renderingBitmap = this.nextRenderingBitmap;
               this.renderingBitmapTime = this.nextRenderingBitmapTime;
               this.renderingShader = this.nextRenderingShader;
               this.nextRenderingBitmap = null;
               this.nextRenderingBitmapTime = 0;
               this.nextRenderingShader = null;
               this.lastFrameTime = var2;
            }
         } else if (!this.isRunning && this.decodeSingleFrame && Math.abs(var2 - this.lastFrameTime) >= (long)this.invalidateAfter) {
            var4 = this.nextRenderingBitmap;
            if (var4 != null) {
               this.renderingBitmap = var4;
               this.renderingBitmapTime = this.nextRenderingBitmapTime;
               this.renderingShader = this.nextRenderingShader;
               this.nextRenderingBitmap = null;
               this.nextRenderingBitmapTime = 0;
               this.nextRenderingShader = null;
               this.lastFrameTime = var2;
            }
         }

         var4 = this.renderingBitmap;
         if (var4 != null) {
            int var8;
            int[] var11;
            if (this.applyTransformation) {
               int var7;
               label69: {
                  int var5 = var4.getWidth();
                  int var6 = this.renderingBitmap.getHeight();
                  var11 = this.metaData;
                  if (var11[2] != 90) {
                     var7 = var5;
                     var8 = var6;
                     if (var11[2] != 270) {
                        break label69;
                     }
                  }

                  var8 = var5;
                  var7 = var6;
               }

               this.dstRect.set(this.getBounds());
               this.scaleX = (float)this.dstRect.width() / (float)var7;
               this.scaleY = (float)this.dstRect.height() / (float)var8;
               this.applyTransformation = false;
            }

            if (this.roundRadius != 0) {
               Math.max(this.scaleX, this.scaleY);
               if (this.renderingShader == null) {
                  var4 = this.backgroundBitmap;
                  TileMode var9 = TileMode.CLAMP;
                  this.renderingShader = new BitmapShader(var4, var9, var9);
               }

               Paint var12 = this.getPaint();
               var12.setShader(this.renderingShader);
               this.shaderMatrix.reset();
               Matrix var14 = this.shaderMatrix;
               android.graphics.Rect var10 = this.dstRect;
               var14.setTranslate((float)var10.left, (float)var10.top);
               int[] var15 = this.metaData;
               if (var15[2] == 90) {
                  this.shaderMatrix.preRotate(90.0F);
                  this.shaderMatrix.preTranslate(0.0F, (float)(-this.dstRect.width()));
               } else if (var15[2] == 180) {
                  this.shaderMatrix.preRotate(180.0F);
                  this.shaderMatrix.preTranslate((float)(-this.dstRect.width()), (float)(-this.dstRect.height()));
               } else if (var15[2] == 270) {
                  this.shaderMatrix.preRotate(270.0F);
                  this.shaderMatrix.preTranslate((float)(-this.dstRect.height()), 0.0F);
               }

               this.shaderMatrix.preScale(this.scaleX, this.scaleY);
               this.renderingShader.setLocalMatrix(this.shaderMatrix);
               RectF var16 = this.actualDrawRect;
               var8 = this.roundRadius;
               var1.drawRoundRect(var16, (float)var8, (float)var8, var12);
            } else {
               android.graphics.Rect var13 = this.dstRect;
               var1.translate((float)var13.left, (float)var13.top);
               var11 = this.metaData;
               if (var11[2] == 90) {
                  var1.rotate(90.0F);
                  var1.translate(0.0F, (float)(-this.dstRect.width()));
               } else if (var11[2] == 180) {
                  var1.rotate(180.0F);
                  var1.translate((float)(-this.dstRect.width()), (float)(-this.dstRect.height()));
               } else if (var11[2] == 270) {
                  var1.rotate(270.0F);
                  var1.translate((float)(-this.dstRect.height()), 0.0F);
               }

               var1.scale(this.scaleX, this.scaleY);
               var1.drawBitmap(this.renderingBitmap, 0.0F, 0.0F, this.getPaint());
            }

            if (this.isRunning) {
               var2 = Math.max(1L, (long)this.invalidateAfter - (var2 - this.lastFrameTime) - 17L);
               uiHandler.removeCallbacks(this.mInvalidateTask);
               uiHandler.postDelayed(this.mInvalidateTask, Math.min(var2, (long)this.invalidateAfter));
            }
         }

      }
   }

   protected void finalize() throws Throwable {
      try {
         this.recycle();
      } finally {
         super.finalize();
      }

   }

   public Bitmap getAnimatedBitmap() {
      Bitmap var1 = this.renderingBitmap;
      if (var1 != null) {
         return var1;
      } else {
         var1 = this.nextRenderingBitmap;
         return var1 != null ? var1 : null;
      }
   }

   public Bitmap getBackgroundBitmap() {
      return this.backgroundBitmap;
   }

   public float getCurrentProgress() {
      if (this.metaData[4] == 0) {
         return 0.0F;
      } else if (this.pendingSeekToUI >= 0L) {
         return (float)this.pendingSeekToUI / (float)this.metaData[4];
      } else {
         int[] var1 = this.metaData;
         return (float)var1[3] / (float)var1[4];
      }
   }

   public int getCurrentProgressMs() {
      if (this.pendingSeekToUI >= 0L) {
         return (int)this.pendingSeekToUI;
      } else {
         int var1 = this.nextRenderingBitmapTime;
         if (var1 == 0) {
            var1 = this.renderingBitmapTime;
         }

         return var1;
      }
   }

   public int getDurationMs() {
      return this.metaData[4];
   }

   public int getIntrinsicHeight() {
      boolean var1 = this.decoderCreated;
      int var2 = 0;
      if (var1) {
         int[] var3 = this.metaData;
         if (var3[2] != 90 && var3[2] != 270) {
            var2 = var3[1];
         } else {
            var2 = this.metaData[0];
         }
      }

      return var2 == 0 ? AndroidUtilities.dp(100.0F) : var2;
   }

   public int getIntrinsicWidth() {
      boolean var1 = this.decoderCreated;
      int var2 = 0;
      if (var1) {
         int[] var3 = this.metaData;
         if (var3[2] != 90 && var3[2] != 270) {
            var2 = var3[0];
         } else {
            var2 = this.metaData[1];
         }
      }

      return var2 == 0 ? AndroidUtilities.dp(100.0F) : var2;
   }

   public int getMinimumHeight() {
      boolean var1 = this.decoderCreated;
      int var2 = 0;
      if (var1) {
         int[] var3 = this.metaData;
         if (var3[2] != 90 && var3[2] != 270) {
            var2 = var3[1];
         } else {
            var2 = this.metaData[0];
         }
      }

      return var2 == 0 ? AndroidUtilities.dp(100.0F) : var2;
   }

   public int getMinimumWidth() {
      boolean var1 = this.decoderCreated;
      int var2 = 0;
      if (var1) {
         int[] var3 = this.metaData;
         if (var3[2] != 90 && var3[2] != 270) {
            var2 = var3[0];
         } else {
            var2 = this.metaData[1];
         }
      }

      return var2 == 0 ? AndroidUtilities.dp(100.0F) : var2;
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
      boolean var1;
      if (this.nativePtr == 0L || this.renderingBitmap == null && this.nextRenderingBitmap == null) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean isLoadingStream() {
      AnimatedFileDrawableStream var1 = this.stream;
      boolean var2;
      if (var1 != null && var1.isWaitingForLoad()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   // $FF: synthetic method
   public void lambda$new$0$AnimatedFileDrawable() {
      View var1 = this.secondParentView;
      if (var1 != null) {
         var1.invalidate();
      } else {
         var1 = this.parentView;
         if (var1 != null) {
            var1.invalidate();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$AnimatedFileDrawable() {
      View var1 = this.secondParentView;
      if (var1 != null) {
         var1.invalidate();
      } else {
         var1 = this.parentView;
         if (var1 != null) {
            var1.invalidate();
         }
      }

   }

   public AnimatedFileDrawable makeCopy() {
      AnimatedFileDrawableStream var1 = this.stream;
      AnimatedFileDrawable var4;
      if (var1 != null) {
         var4 = new AnimatedFileDrawable(this.path, false, this.streamFileSize, var1.getDocument(), this.stream.getParentObject(), this.currentAccount);
      } else {
         var4 = new AnimatedFileDrawable(this.path, false, this.streamFileSize, (TLRPC.Document)null, (Object)null, this.currentAccount);
      }

      int[] var2 = var4.metaData;
      int[] var3 = this.metaData;
      var2[0] = var3[0];
      var2[1] = var3[1];
      return var4;
   }

   protected void onBoundsChange(android.graphics.Rect var1) {
      super.onBoundsChange(var1);
      this.applyTransformation = true;
   }

   public void recycle() {
      if (this.secondParentView != null) {
         this.recycleWithSecond = true;
      } else {
         this.isRunning = false;
         this.isRecycled = true;
         if (this.loadFrameTask == null) {
            if (this.nativePtr != 0L) {
               destroyDecoder(this.nativePtr);
               this.nativePtr = 0L;
            }

            Bitmap var1 = this.renderingBitmap;
            if (var1 != null) {
               var1.recycle();
               this.renderingBitmap = null;
            }

            var1 = this.nextRenderingBitmap;
            if (var1 != null) {
               var1.recycle();
               this.nextRenderingBitmap = null;
            }

            DispatchQueue var2 = this.decodeQueue;
            if (var2 != null) {
               var2.recycle();
               this.decodeQueue = null;
            }
         } else {
            this.destroyWhenDone = true;
         }

         AnimatedFileDrawableStream var3 = this.stream;
         if (var3 != null) {
            var3.cancel(true);
         }

      }
   }

   public void seekTo(long var1, boolean var3) {
      Object var4 = this.sync;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label227: {
         label226: {
            byte var5;
            label225: {
               label224: {
                  try {
                     this.pendingSeekTo = var1;
                     this.pendingSeekToUI = var1;
                     prepareToSeek(this.nativePtr);
                     if (!this.decoderCreated || this.stream == null) {
                        break label226;
                     }

                     this.stream.cancel(var3);
                     this.pendingRemoveLoading = var3;
                     if (this.pendingRemoveLoading) {
                        break label224;
                     }
                  } catch (Throwable var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label227;
                  }

                  var5 = 10;
                  break label225;
               }

               var5 = 0;
            }

            try {
               this.pendingRemoveLoadingFramesReset = var5;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label227;
            }
         }

         label212:
         try {
            return;
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label212;
         }
      }

      while(true) {
         Throwable var6 = var10000;

         try {
            throw var6;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            continue;
         }
      }
   }

   public void setActualDrawRect(float var1, float var2, float var3, float var4) {
      this.actualDrawRect.set(var1, var2, var3 + var1, var4 + var2);
   }

   public void setAllowDecodeSingleFrame(boolean var1) {
      this.decodeSingleFrame = var1;
      if (this.decodeSingleFrame) {
         this.scheduleNextGetFrame();
      }

   }

   public void setParentView(View var1) {
      if (this.parentView == null) {
         this.parentView = var1;
      }
   }

   public void setRoundRadius(int var1) {
      this.roundRadius = var1;
      this.getPaint().setFlags(3);
   }

   public void setSecondParentView(View var1) {
      this.secondParentView = var1;
      if (var1 == null && this.recycleWithSecond) {
         this.recycle();
      }

   }

   public void setUseSharedQueue(boolean var1) {
      this.useSharedQueue = var1;
   }

   public void start() {
      if (!this.isRunning) {
         this.isRunning = true;
         this.scheduleNextGetFrame();
         runOnUiThread(this.mStartTask);
      }
   }

   public void stop() {
      this.isRunning = false;
   }
}
