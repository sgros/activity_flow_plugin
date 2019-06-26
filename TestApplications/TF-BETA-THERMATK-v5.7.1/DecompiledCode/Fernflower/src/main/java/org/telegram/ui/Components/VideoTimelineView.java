package org.telegram.ui.Components;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;

@TargetApi(10)
public class VideoTimelineView extends View {
   private static final Object sync = new Object();
   private AsyncTask currentTask;
   private VideoTimelineView.VideoTimelineViewDelegate delegate;
   private int frameHeight;
   private long frameTimeOffset;
   private int frameWidth;
   private ArrayList frames = new ArrayList();
   private int framesToLoad;
   private boolean isRoundFrames;
   private float maxProgressDiff = 1.0F;
   private MediaMetadataRetriever mediaMetadataRetriever;
   private float minProgressDiff = 0.0F;
   private Paint paint = new Paint(1);
   private Paint paint2;
   private float pressDx;
   private boolean pressedLeft;
   private boolean pressedRight;
   private float progressLeft;
   private float progressRight = 1.0F;
   private android.graphics.Rect rect1;
   private android.graphics.Rect rect2;
   private long videoLength;

   public VideoTimelineView(Context var1) {
      super(var1);
      this.paint.setColor(-1);
      this.paint2 = new Paint();
      this.paint2.setColor(2130706432);
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
                  Exception var9;
                  Bitmap var14;
                  label52: {
                     Bitmap var2;
                     try {
                        var2 = VideoTimelineView.this.mediaMetadataRetriever.getFrameAtTime(VideoTimelineView.this.frameTimeOffset * (long)this.frameNum * 1000L, 2);
                     } catch (Exception var13) {
                        var14 = null;
                        var9 = var13;
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
                           var14 = Bitmap.createBitmap(VideoTimelineView.this.frameWidth, VideoTimelineView.this.frameHeight, var2.getConfig());
                           var3 = new Canvas(var14);
                           var4 = (float)VideoTimelineView.this.frameWidth / (float)var2.getWidth();
                           var5 = (float)VideoTimelineView.this.frameHeight / (float)var2.getHeight();
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
                           android.graphics.Rect var8 = new android.graphics.Rect(0, 0, var2.getWidth(), var2.getHeight());
                           android.graphics.Rect var15 = new android.graphics.Rect((VideoTimelineView.this.frameWidth - var6) / 2, (VideoTimelineView.this.frameHeight - var7) / 2, var6, var7);
                           var3.drawBitmap(var2, var8, var15, (Paint)null);
                           var2.recycle();
                           return var14;
                        } catch (Exception var10) {
                           var10000 = var10;
                           var10001 = false;
                        }
                     }

                     var9 = var10000;
                     var14 = var2;
                  }

                  FileLog.e((Throwable)var9);
                  return var14;
               }
            }

            protected void onPostExecute(Bitmap var1) {
               if (!this.isCancelled()) {
                  VideoTimelineView.this.frames.add(var1);
                  VideoTimelineView.this.invalidate();
                  if (this.frameNum < VideoTimelineView.this.framesToLoad) {
                     VideoTimelineView.this.reloadFrames(this.frameNum + 1);
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

   public float getRightProgress() {
      return this.progressRight;
   }

   protected void onDraw(Canvas var1) {
      int var2 = this.getMeasuredWidth() - AndroidUtilities.dp(36.0F);
      float var3 = (float)var2;
      int var4 = (int)(this.progressLeft * var3) + AndroidUtilities.dp(16.0F);
      int var5 = (int)(var3 * this.progressRight) + AndroidUtilities.dp(16.0F);
      var1.save();
      int var6 = AndroidUtilities.dp(16.0F);
      int var7 = AndroidUtilities.dp(20.0F);
      int var8 = this.getMeasuredHeight();
      int var9 = 0;
      var1.clipRect(var6, 0, var7 + var2, var8);
      if (this.frames.isEmpty() && this.currentTask == null) {
         this.reloadFrames(0);
      } else {
         for(var6 = 0; var9 < this.frames.size(); ++var9) {
            Bitmap var10 = (Bitmap)this.frames.get(var9);
            if (var10 != null) {
               var7 = AndroidUtilities.dp(16.0F);
               if (this.isRoundFrames) {
                  var8 = this.frameWidth / 2;
               } else {
                  var8 = this.frameWidth;
               }

               var7 += var8 * var6;
               var8 = AndroidUtilities.dp(2.0F);
               if (this.isRoundFrames) {
                  this.rect2.set(var7, var8, AndroidUtilities.dp(28.0F) + var7, AndroidUtilities.dp(28.0F) + var8);
                  var1.drawBitmap(var10, this.rect1, this.rect2, (Paint)null);
               } else {
                  var1.drawBitmap(var10, (float)var7, (float)var8, (Paint)null);
               }
            }

            ++var6;
         }
      }

      var9 = AndroidUtilities.dp(2.0F);
      float var11 = (float)AndroidUtilities.dp(16.0F);
      float var12 = (float)var9;
      var3 = (float)var4;
      var1.drawRect(var11, var12, var3, (float)(this.getMeasuredHeight() - var9), this.paint2);
      var1.drawRect((float)(AndroidUtilities.dp(4.0F) + var5), var12, (float)(AndroidUtilities.dp(16.0F) + var2 + AndroidUtilities.dp(4.0F)), (float)(this.getMeasuredHeight() - var9), this.paint2);
      var1.drawRect(var3, 0.0F, (float)(AndroidUtilities.dp(2.0F) + var4), (float)this.getMeasuredHeight(), this.paint);
      var1.drawRect((float)(AndroidUtilities.dp(2.0F) + var5), 0.0F, (float)(AndroidUtilities.dp(4.0F) + var5), (float)this.getMeasuredHeight(), this.paint);
      var1.drawRect((float)(AndroidUtilities.dp(2.0F) + var4), 0.0F, (float)(AndroidUtilities.dp(4.0F) + var5), var12, this.paint);
      var1.drawRect((float)(var4 + AndroidUtilities.dp(2.0F)), (float)(this.getMeasuredHeight() - var9), (float)(AndroidUtilities.dp(4.0F) + var5), (float)this.getMeasuredHeight(), this.paint);
      var1.restore();
      var1.drawCircle(var3, (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(7.0F), this.paint);
      var1.drawCircle((float)(var5 + AndroidUtilities.dp(4.0F)), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(7.0F), this.paint);
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
         int var7 = (int)(this.progressRight * var5) + AndroidUtilities.dp(16.0F);
         VideoTimelineView.VideoTimelineViewDelegate var8;
         if (var1.getAction() == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            if (this.mediaMetadataRetriever == null) {
               return false;
            }

            var4 = AndroidUtilities.dp(12.0F);
            if ((float)(var6 - var4) <= var2 && var2 <= (float)(var6 + var4) && var3 >= 0.0F && var3 <= (float)this.getMeasuredHeight()) {
               var8 = this.delegate;
               if (var8 != null) {
                  var8.didStartDragging();
               }

               this.pressedLeft = true;
               this.pressDx = (float)((int)(var2 - (float)var6));
               this.invalidate();
               return true;
            }

            if ((float)(var7 - var4) <= var2 && var2 <= (float)(var4 + var7) && var3 >= 0.0F && var3 <= (float)this.getMeasuredHeight()) {
               var8 = this.delegate;
               if (var8 != null) {
                  var8.didStartDragging();
               }

               this.pressedRight = true;
               this.pressDx = (float)((int)(var2 - (float)var7));
               this.invalidate();
               return true;
            }
         } else if (var1.getAction() != 1 && var1.getAction() != 3) {
            if (var1.getAction() == 2) {
               if (this.pressedLeft) {
                  var6 = (int)(var2 - this.pressDx);
                  if (var6 < AndroidUtilities.dp(16.0F)) {
                     var7 = AndroidUtilities.dp(16.0F);
                  } else if (var6 <= var7) {
                     var7 = var6;
                  }

                  this.progressLeft = (float)(var7 - AndroidUtilities.dp(16.0F)) / var5;
                  var2 = this.progressRight;
                  var3 = this.progressLeft;
                  var5 = this.maxProgressDiff;
                  if (var2 - var3 > var5) {
                     this.progressRight = var3 + var5;
                  } else {
                     var5 = this.minProgressDiff;
                     if (var5 != 0.0F && var2 - var3 < var5) {
                        this.progressLeft = var2 - var5;
                        if (this.progressLeft < 0.0F) {
                           this.progressLeft = 0.0F;
                        }
                     }
                  }

                  var8 = this.delegate;
                  if (var8 != null) {
                     var8.onLeftProgressChanged(this.progressLeft);
                  }

                  this.invalidate();
                  return true;
               }

               if (this.pressedRight) {
                  var7 = (int)(var2 - this.pressDx);
                  if (var7 < var6) {
                     var7 = var6;
                  } else if (var7 > AndroidUtilities.dp(16.0F) + var4) {
                     var7 = var4 + AndroidUtilities.dp(16.0F);
                  }

                  this.progressRight = (float)(var7 - AndroidUtilities.dp(16.0F)) / var5;
                  var2 = this.progressRight;
                  var3 = this.progressLeft;
                  var5 = this.maxProgressDiff;
                  if (var2 - var3 > var5) {
                     this.progressLeft = var2 - var5;
                  } else {
                     var5 = this.minProgressDiff;
                     if (var5 != 0.0F && var2 - var3 < var5) {
                        this.progressRight = var3 + var5;
                        if (this.progressRight > 1.0F) {
                           this.progressRight = 1.0F;
                        }
                     }
                  }

                  var8 = this.delegate;
                  if (var8 != null) {
                     var8.onRightProgressChanged(this.progressRight);
                  }

                  this.invalidate();
                  return true;
               }
            }
         } else {
            if (this.pressedLeft) {
               var8 = this.delegate;
               if (var8 != null) {
                  var8.didStopDragging();
               }

               this.pressedLeft = false;
               return true;
            }

            if (this.pressedRight) {
               var8 = this.delegate;
               if (var8 != null) {
                  var8.didStopDragging();
               }

               this.pressedRight = false;
               return true;
            }
         }

         return false;
      }
   }

   public void setColor(int var1) {
      this.paint.setColor(var1);
   }

   public void setDelegate(VideoTimelineView.VideoTimelineViewDelegate var1) {
      this.delegate = var1;
   }

   public void setMaxProgressDiff(float var1) {
      this.maxProgressDiff = var1;
      var1 = this.progressRight;
      float var2 = this.progressLeft;
      float var3 = this.maxProgressDiff;
      if (var1 - var2 > var3) {
         this.progressRight = var2 + var3;
         this.invalidate();
      }

   }

   public void setMinProgressDiff(float var1) {
      this.minProgressDiff = var1;
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

      void onRightProgressChanged(float var1);
   }
}
