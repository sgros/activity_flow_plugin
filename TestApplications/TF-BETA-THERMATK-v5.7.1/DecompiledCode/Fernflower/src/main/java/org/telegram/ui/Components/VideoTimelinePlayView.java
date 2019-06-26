package org.telegram.ui.Components;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;

@TargetApi(10)
public class VideoTimelinePlayView extends View {
   private static final Object sync = new Object();
   private float bufferedProgress = 0.5F;
   private AsyncTask currentTask;
   private VideoTimelinePlayView.VideoTimelineViewDelegate delegate;
   private Drawable drawableLeft;
   private Drawable drawableRight;
   private int frameHeight;
   private long frameTimeOffset;
   private int frameWidth;
   private ArrayList frames = new ArrayList();
   private int framesToLoad;
   private boolean isRoundFrames;
   private int lastWidth;
   private float maxProgressDiff = 1.0F;
   private MediaMetadataRetriever mediaMetadataRetriever;
   private float minProgressDiff = 0.0F;
   private Paint paint = new Paint(1);
   private Paint paint2;
   private float playProgress = 0.5F;
   private float pressDx;
   private boolean pressedLeft;
   private boolean pressedPlay;
   private boolean pressedRight;
   private float progressLeft;
   private float progressRight = 1.0F;
   private android.graphics.Rect rect1;
   private android.graphics.Rect rect2;
   private RectF rect3 = new RectF();
   private long videoLength;

   public VideoTimelinePlayView(Context var1) {
      super(var1);
      this.paint.setColor(-1);
      this.paint2 = new Paint();
      this.paint2.setColor(2130706432);
      this.drawableLeft = var1.getResources().getDrawable(2131165902);
      this.drawableLeft.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
      this.drawableRight = var1.getResources().getDrawable(2131165903);
      this.drawableRight.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
   }

   private void reloadFrames(int var1) {
      if (this.mediaMetadataRetriever != null) {
         if (var1 == 0) {
            if (this.isRoundFrames) {
               int var2 = AndroidUtilities.dp(56.0F);
               this.frameWidth = var2;
               this.frameHeight = var2;
               this.framesToLoad = (int)Math.ceil((double)((float)(this.getMeasuredWidth() - AndroidUtilities.dp(16.0F)) / ((float)this.frameHeight / 2.0F)));
            } else {
               this.frameHeight = AndroidUtilities.dp(40.0F);
               this.framesToLoad = (this.getMeasuredWidth() - AndroidUtilities.dp(16.0F)) / this.frameHeight;
               this.frameWidth = (int)Math.ceil((double)((float)(this.getMeasuredWidth() - AndroidUtilities.dp(16.0F)) / (float)this.framesToLoad));
            }

            this.frameTimeOffset = this.videoLength / (long)this.framesToLoad;
         }

         this.currentTask = new AsyncTask() {
            private int frameNum = 0;

            protected Bitmap doInBackground(Integer... var1) {
               this.frameNum = var1[0];
               if (this.isCancelled()) {
                  return null;
               } else {
                  Exception var8;
                  Bitmap var14;
                  label52: {
                     Bitmap var2;
                     try {
                        var2 = VideoTimelinePlayView.this.mediaMetadataRetriever.getFrameAtTime(VideoTimelinePlayView.this.frameTimeOffset * (long)this.frameNum * 1000L, 2);
                     } catch (Exception var13) {
                        var8 = var13;
                        var14 = null;
                        break label52;
                     }

                     Exception var10000;
                     label55: {
                        boolean var10001;
                        try {
                           if (this.isCancelled()) {
                              return null;
                           }
                        } catch (Exception var12) {
                           var10000 = var12;
                           var10001 = false;
                           break label55;
                        }

                        var14 = var2;
                        if (var2 == null) {
                           return var14;
                        }

                        Canvas var3;
                        float var4;
                        float var5;
                        try {
                           var14 = Bitmap.createBitmap(VideoTimelinePlayView.this.frameWidth, VideoTimelinePlayView.this.frameHeight, var2.getConfig());
                           var3 = new Canvas(var14);
                           var4 = (float)VideoTimelinePlayView.this.frameWidth / (float)var2.getWidth();
                           var5 = (float)VideoTimelinePlayView.this.frameHeight / (float)var2.getHeight();
                        } catch (Exception var11) {
                           var10000 = var11;
                           var10001 = false;
                           break label55;
                        }

                        if (var4 <= var5) {
                           var4 = var5;
                        }

                        try {
                           int var6 = (int)((float)var2.getWidth() * var4);
                           int var7 = (int)((float)var2.getHeight() * var4);
                           android.graphics.Rect var15 = new android.graphics.Rect(0, 0, var2.getWidth(), var2.getHeight());
                           android.graphics.Rect var9 = new android.graphics.Rect((VideoTimelinePlayView.this.frameWidth - var6) / 2, (VideoTimelinePlayView.this.frameHeight - var7) / 2, var6, var7);
                           var3.drawBitmap(var2, var15, var9, (Paint)null);
                           var2.recycle();
                           return var14;
                        } catch (Exception var10) {
                           var10000 = var10;
                           var10001 = false;
                        }
                     }

                     var8 = var10000;
                     var14 = var2;
                  }

                  FileLog.e((Throwable)var8);
                  return var14;
               }
            }

            protected void onPostExecute(Bitmap var1) {
               if (!this.isCancelled()) {
                  VideoTimelinePlayView.this.frames.add(var1);
                  VideoTimelinePlayView.this.invalidate();
                  if (this.frameNum < VideoTimelinePlayView.this.framesToLoad) {
                     VideoTimelinePlayView.this.reloadFrames(this.frameNum + 1);
                  }
               }

            }
         };
         this.currentTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[]{var1, null, null});
      }
   }

   public void clearFrames() {
      for(int var1 = 0; var1 < this.frames.size(); ++var1) {
         Bitmap var2 = (Bitmap)this.frames.get(var1);
         if (var2 != null) {
            var2.recycle();
         }
      }

      this.frames.clear();
      AsyncTask var3 = this.currentTask;
      if (var3 != null) {
         var3.cancel(true);
         this.currentTask = null;
      }

      this.invalidate();
   }

   public void destroy() {
      Object var1 = sync;
      synchronized(var1){}

      label300: {
         Throwable var10000;
         boolean var10001;
         label302: {
            label293: {
               Exception var2;
               try {
                  try {
                     if (this.mediaMetadataRetriever != null) {
                        this.mediaMetadataRetriever.release();
                        this.mediaMetadataRetriever = null;
                     }
                     break label293;
                  } catch (Exception var27) {
                     var2 = var27;
                  }
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label302;
               }

               try {
                  FileLog.e((Throwable)var2);
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label302;
               }
            }

            label283:
            try {
               break label300;
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break label283;
            }
         }

         while(true) {
            Throwable var31 = var10000;

            try {
               throw var31;
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               continue;
            }
         }
      }

      for(int var3 = 0; var3 < this.frames.size(); ++var3) {
         Bitmap var29 = (Bitmap)this.frames.get(var3);
         if (var29 != null) {
            var29.recycle();
         }
      }

      this.frames.clear();
      AsyncTask var30 = this.currentTask;
      if (var30 != null) {
         var30.cancel(true);
         this.currentTask = null;
      }

   }

   public float getLeftProgress() {
      return this.progressLeft;
   }

   public float getProgress() {
      return this.playProgress;
   }

   public float getRightProgress() {
      return this.progressRight;
   }

   public boolean isDragging() {
      return this.pressedPlay;
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.getMeasuredWidth() - AndroidUtilities.dp(36.0F);
      float var3 = (float)var2;
      int var4 = (int)(this.progressLeft * var3) + AndroidUtilities.dp(16.0F);
      int var5 = (int)(this.progressRight * var3) + AndroidUtilities.dp(16.0F);
      var1.save();
      var1.clipRect(AndroidUtilities.dp(16.0F), AndroidUtilities.dp(4.0F), AndroidUtilities.dp(20.0F) + var2, AndroidUtilities.dp(48.0F));
      boolean var6 = this.frames.isEmpty();
      int var7 = 0;
      int var8;
      if (var6 && this.currentTask == null) {
         this.reloadFrames(0);
      } else {
         for(var8 = 0; var7 < this.frames.size(); ++var7) {
            Bitmap var9 = (Bitmap)this.frames.get(var7);
            if (var9 != null) {
               int var10 = AndroidUtilities.dp(16.0F);
               int var11;
               if (this.isRoundFrames) {
                  var11 = this.frameWidth / 2;
               } else {
                  var11 = this.frameWidth;
               }

               var10 += var11 * var8;
               var11 = AndroidUtilities.dp(6.0F);
               if (this.isRoundFrames) {
                  this.rect2.set(var10, var11, var10 + AndroidUtilities.dp(28.0F), var11 + AndroidUtilities.dp(28.0F));
                  var1.drawBitmap(var9, this.rect1, this.rect2, (Paint)null);
               } else {
                  var1.drawBitmap(var9, (float)var10, (float)var11, (Paint)null);
               }
            }

            ++var8;
         }
      }

      var8 = AndroidUtilities.dp(6.0F);
      var7 = AndroidUtilities.dp(48.0F);
      float var12 = (float)AndroidUtilities.dp(16.0F);
      float var13 = (float)var8;
      float var14 = (float)var4;
      var1.drawRect(var12, var13, var14, (float)AndroidUtilities.dp(46.0F), this.paint2);
      var1.drawRect((float)(AndroidUtilities.dp(4.0F) + var5), var13, (float)(AndroidUtilities.dp(16.0F) + var2 + AndroidUtilities.dp(4.0F)), (float)AndroidUtilities.dp(46.0F), this.paint2);
      float var15 = (float)AndroidUtilities.dp(4.0F);
      var12 = (float)(AndroidUtilities.dp(2.0F) + var4);
      float var16 = (float)var7;
      var1.drawRect(var14, var15, var12, var16, this.paint);
      var1.drawRect((float)(AndroidUtilities.dp(2.0F) + var5), (float)AndroidUtilities.dp(4.0F), (float)(AndroidUtilities.dp(4.0F) + var5), var16, this.paint);
      var1.drawRect((float)(AndroidUtilities.dp(2.0F) + var4), (float)AndroidUtilities.dp(4.0F), (float)(AndroidUtilities.dp(4.0F) + var5), var13, this.paint);
      var1.drawRect((float)(AndroidUtilities.dp(2.0F) + var4), (float)(var7 - AndroidUtilities.dp(2.0F)), (float)(AndroidUtilities.dp(4.0F) + var5), var16, this.paint);
      var1.restore();
      this.rect3.set((float)(var4 - AndroidUtilities.dp(8.0F)), (float)AndroidUtilities.dp(4.0F), (float)(AndroidUtilities.dp(2.0F) + var4), var16);
      var1.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), this.paint);
      this.drawableLeft.setBounds(var4 - AndroidUtilities.dp(8.0F), AndroidUtilities.dp(4.0F) + (AndroidUtilities.dp(44.0F) - AndroidUtilities.dp(18.0F)) / 2, var4 + AndroidUtilities.dp(2.0F), (AndroidUtilities.dp(44.0F) - AndroidUtilities.dp(18.0F)) / 2 + AndroidUtilities.dp(22.0F));
      this.drawableLeft.draw(var1);
      this.rect3.set((float)(AndroidUtilities.dp(2.0F) + var5), (float)AndroidUtilities.dp(4.0F), (float)(AndroidUtilities.dp(12.0F) + var5), var16);
      var1.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), this.paint);
      this.drawableRight.setBounds(AndroidUtilities.dp(2.0F) + var5, AndroidUtilities.dp(4.0F) + (AndroidUtilities.dp(44.0F) - AndroidUtilities.dp(18.0F)) / 2, var5 + AndroidUtilities.dp(12.0F), (AndroidUtilities.dp(44.0F) - AndroidUtilities.dp(18.0F)) / 2 + AndroidUtilities.dp(22.0F));
      this.drawableRight.draw(var1);
      var14 = (float)AndroidUtilities.dp(18.0F);
      var13 = this.progressLeft;
      var3 = var14 + var3 * (var13 + (this.progressRight - var13) * this.playProgress);
      this.rect3.set(var3 - (float)AndroidUtilities.dp(1.5F), (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(1.5F) + var3, (float)AndroidUtilities.dp(50.0F));
      var1.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), this.paint2);
      var1.drawCircle(var3, (float)AndroidUtilities.dp(52.0F), (float)AndroidUtilities.dp(3.5F), this.paint2);
      this.rect3.set(var3 - (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(1.0F) + var3, (float)AndroidUtilities.dp(50.0F));
      var1.drawRoundRect(this.rect3, (float)AndroidUtilities.dp(1.0F), (float)AndroidUtilities.dp(1.0F), this.paint);
      var1.drawCircle(var3, (float)AndroidUtilities.dp(52.0F), (float)AndroidUtilities.dp(3.0F), this.paint);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      var1 = MeasureSpec.getSize(var1);
      if (this.lastWidth != var1) {
         this.clearFrames();
         this.lastWidth = var1;
      }

   }

   public boolean onTouchEvent(MotionEvent var1) {
      if (var1 == null) {
         return false;
      } else {
         float var2 = var1.getX();
         float var3 = var1.getY();
         int var4 = this.getMeasuredWidth() - AndroidUtilities.dp(32.0F);
         float var5 = (float)var4;
         int var6 = (int)(this.progressLeft * var5) + AndroidUtilities.dp(16.0F);
         float var7 = this.progressLeft;
         int var8 = (int)((var7 + (this.progressRight - var7) * this.playProgress) * var5) + AndroidUtilities.dp(16.0F);
         int var9 = (int)(this.progressRight * var5) + AndroidUtilities.dp(16.0F);
         VideoTimelinePlayView.VideoTimelineViewDelegate var11;
         if (var1.getAction() == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            if (this.mediaMetadataRetriever == null) {
               return false;
            }

            int var10 = AndroidUtilities.dp(12.0F);
            var4 = AndroidUtilities.dp(8.0F);
            if ((float)(var8 - var4) <= var2 && var2 <= (float)(var4 + var8) && var3 >= 0.0F && var3 <= (float)this.getMeasuredHeight()) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.didStartDragging();
               }

               this.pressedPlay = true;
               this.pressDx = (float)((int)(var2 - (float)var8));
               this.invalidate();
               return true;
            }

            if ((float)(var6 - var10) <= var2 && var2 <= (float)(var6 + var10) && var3 >= 0.0F && var3 <= (float)this.getMeasuredHeight()) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.didStartDragging();
               }

               this.pressedLeft = true;
               this.pressDx = (float)((int)(var2 - (float)var6));
               this.invalidate();
               return true;
            }

            if ((float)(var9 - var10) <= var2 && var2 <= (float)(var10 + var9) && var3 >= 0.0F && var3 <= (float)this.getMeasuredHeight()) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.didStartDragging();
               }

               this.pressedRight = true;
               this.pressDx = (float)((int)(var2 - (float)var9));
               this.invalidate();
               return true;
            }
         } else if (var1.getAction() != 1 && var1.getAction() != 3) {
            if (var1.getAction() == 2) {
               if (this.pressedPlay) {
                  this.playProgress = (float)((int)(var2 - this.pressDx) - AndroidUtilities.dp(16.0F)) / var5;
                  var3 = this.playProgress;
                  var7 = this.progressLeft;
                  if (var3 < var7) {
                     this.playProgress = var7;
                  } else {
                     var7 = this.progressRight;
                     if (var3 > var7) {
                        this.playProgress = var7;
                     }
                  }

                  var7 = this.playProgress;
                  var3 = this.progressLeft;
                  var5 = this.progressRight;
                  this.playProgress = (var7 - var3) / (var5 - var3);
                  var11 = this.delegate;
                  if (var11 != null) {
                     var11.onPlayProgressChanged(var3 + (var5 - var3) * this.playProgress);
                  }

                  this.invalidate();
                  return true;
               }

               if (this.pressedLeft) {
                  var6 = (int)(var2 - this.pressDx);
                  if (var6 < AndroidUtilities.dp(16.0F)) {
                     var6 = AndroidUtilities.dp(16.0F);
                  } else if (var6 > var9) {
                     var6 = var9;
                  }

                  this.progressLeft = (float)(var6 - AndroidUtilities.dp(16.0F)) / var5;
                  var3 = this.progressRight;
                  var7 = this.progressLeft;
                  var5 = this.maxProgressDiff;
                  if (var3 - var7 > var5) {
                     this.progressRight = var7 + var5;
                  } else {
                     var5 = this.minProgressDiff;
                     if (var5 != 0.0F && var3 - var7 < var5) {
                        this.progressLeft = var3 - var5;
                        if (this.progressLeft < 0.0F) {
                           this.progressLeft = 0.0F;
                        }
                     }
                  }

                  var11 = this.delegate;
                  if (var11 != null) {
                     var11.onLeftProgressChanged(this.progressLeft);
                  }

                  this.invalidate();
                  return true;
               }

               if (this.pressedRight) {
                  var9 = (int)(var2 - this.pressDx);
                  if (var9 >= var6) {
                     if (var9 > AndroidUtilities.dp(16.0F) + var4) {
                        var6 = var4 + AndroidUtilities.dp(16.0F);
                     } else {
                        var6 = var9;
                     }
                  }

                  this.progressRight = (float)(var6 - AndroidUtilities.dp(16.0F)) / var5;
                  var7 = this.progressRight;
                  var3 = this.progressLeft;
                  var5 = this.maxProgressDiff;
                  if (var7 - var3 > var5) {
                     this.progressLeft = var7 - var5;
                  } else {
                     var5 = this.minProgressDiff;
                     if (var5 != 0.0F && var7 - var3 < var5) {
                        this.progressRight = var3 + var5;
                        if (this.progressRight > 1.0F) {
                           this.progressRight = 1.0F;
                        }
                     }
                  }

                  var11 = this.delegate;
                  if (var11 != null) {
                     var11.onRightProgressChanged(this.progressRight);
                  }

                  this.invalidate();
                  return true;
               }
            }
         } else {
            if (this.pressedLeft) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.didStopDragging();
               }

               this.pressedLeft = false;
               return true;
            }

            if (this.pressedRight) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.didStopDragging();
               }

               this.pressedRight = false;
               return true;
            }

            if (this.pressedPlay) {
               var11 = this.delegate;
               if (var11 != null) {
                  var11.didStopDragging();
               }

               this.pressedPlay = false;
               return true;
            }
         }

         return false;
      }
   }

   public void setColor(int var1) {
      this.paint.setColor(var1);
   }

   public void setDelegate(VideoTimelinePlayView.VideoTimelineViewDelegate var1) {
      this.delegate = var1;
   }

   public void setMaxProgressDiff(float var1) {
      this.maxProgressDiff = var1;
      float var2 = this.progressRight;
      var1 = this.progressLeft;
      float var3 = this.maxProgressDiff;
      if (var2 - var1 > var3) {
         this.progressRight = var1 + var3;
         this.invalidate();
      }

   }

   public void setMinProgressDiff(float var1) {
      this.minProgressDiff = var1;
   }

   public void setProgress(float var1) {
      this.playProgress = var1;
      this.invalidate();
   }

   public void setRoundFrames(boolean var1) {
      this.isRoundFrames = var1;
      if (this.isRoundFrames) {
         this.rect1 = new android.graphics.Rect(AndroidUtilities.dp(14.0F), AndroidUtilities.dp(14.0F), AndroidUtilities.dp(42.0F), AndroidUtilities.dp(42.0F));
         this.rect2 = new android.graphics.Rect();
      }

   }

   public void setVideoPath(String var1) {
      this.destroy();
      this.mediaMetadataRetriever = new MediaMetadataRetriever();
      this.progressLeft = 0.0F;
      this.progressRight = 1.0F;

      try {
         this.mediaMetadataRetriever.setDataSource(var1);
         this.videoLength = Long.parseLong(this.mediaMetadataRetriever.extractMetadata(9));
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      this.invalidate();
   }

   public interface VideoTimelineViewDelegate {
      void didStartDragging();

      void didStopDragging();

      void onLeftProgressChanged(float var1);

      void onPlayProgressChanged(float var1);

      void onRightProgressChanged(float var1);
   }
}
