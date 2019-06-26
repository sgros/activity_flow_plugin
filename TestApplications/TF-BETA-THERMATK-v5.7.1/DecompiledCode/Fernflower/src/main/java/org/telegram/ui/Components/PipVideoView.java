package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.ActionBar.ActionBar;

public class PipVideoView {
   private View controlsView;
   private DecelerateInterpolator decelerateInterpolator;
   private Activity parentActivity;
   private EmbedBottomSheet parentSheet;
   private PhotoViewer photoViewer;
   private SharedPreferences preferences;
   private int videoHeight;
   private int videoWidth;
   private LayoutParams windowLayoutParams;
   private WindowManager windowManager;
   private FrameLayout windowView;

   private void animateToBoundsMaybe() {
      int var3;
      int var4;
      Editor var5;
      int var6;
      ArrayList var10;
      boolean var12;
      label99: {
         label92: {
            int var1 = getSideCoord(true, 0, 0.0F, this.videoWidth);
            int var2 = getSideCoord(true, 1, 0.0F, this.videoWidth);
            var3 = getSideCoord(false, 0, 0.0F, this.videoHeight);
            var4 = getSideCoord(false, 1, 0.0F, this.videoHeight);
            var5 = this.preferences.edit();
            var6 = AndroidUtilities.dp(20.0F);
            if (Math.abs(var1 - this.windowLayoutParams.x) > var6) {
               int var7 = this.windowLayoutParams.x;
               if (var7 >= 0 || var7 <= -this.videoWidth / 4) {
                  if (Math.abs(var2 - this.windowLayoutParams.x) > var6) {
                     var7 = this.windowLayoutParams.x;
                     int var8 = AndroidUtilities.displaySize.x;
                     int var9 = this.videoWidth;
                     if (var7 <= var8 - var9 || var7 >= var8 - var9 / 4 * 3) {
                        if (this.windowView.getAlpha() != 1.0F) {
                           var10 = new ArrayList();
                           if (this.windowLayoutParams.x < 0) {
                              var10.add(ObjectAnimator.ofInt(this, "x", new int[]{-this.videoWidth}));
                           } else {
                              var10.add(ObjectAnimator.ofInt(this, "x", new int[]{AndroidUtilities.displaySize.x}));
                           }

                           var12 = true;
                           break label99;
                        }

                        var5.putFloat("px", (float)(this.windowLayoutParams.x - var1) / (float)(var2 - var1));
                        var5.putInt("sidex", 2);
                        var10 = null;
                        break label92;
                     }
                  }

                  var10 = new ArrayList();
                  var5.putInt("sidex", 1);
                  if (this.windowView.getAlpha() != 1.0F) {
                     var10.add(ObjectAnimator.ofFloat(this.windowView, "alpha", new float[]{1.0F}));
                  }

                  var12 = false;
                  var10.add(ObjectAnimator.ofInt(this, "x", new int[]{var2}));
                  break label99;
               }
            }

            var10 = new ArrayList();
            var5.putInt("sidex", 0);
            if (this.windowView.getAlpha() != 1.0F) {
               var10.add(ObjectAnimator.ofFloat(this.windowView, "alpha", new float[]{1.0F}));
            }

            var10.add(ObjectAnimator.ofInt(this, "x", new int[]{var1}));
         }

         var12 = false;
      }

      ArrayList var11 = var10;
      if (!var12) {
         if (Math.abs(var3 - this.windowLayoutParams.y) > var6 && this.windowLayoutParams.y > ActionBar.getCurrentActionBarHeight()) {
            if (Math.abs(var4 - this.windowLayoutParams.y) <= var6) {
               var11 = var10;
               if (var10 == null) {
                  var11 = new ArrayList();
               }

               var5.putInt("sidey", 1);
               var11.add(ObjectAnimator.ofInt(this, "y", new int[]{var4}));
               var10 = var11;
            } else {
               var5.putFloat("py", (float)(this.windowLayoutParams.y - var3) / (float)(var4 - var3));
               var5.putInt("sidey", 2);
            }
         } else {
            var11 = var10;
            if (var10 == null) {
               var11 = new ArrayList();
            }

            var5.putInt("sidey", 0);
            var11.add(ObjectAnimator.ofInt(this, "y", new int[]{var3}));
            var10 = var11;
         }

         var5.commit();
         var11 = var10;
      }

      if (var11 != null) {
         if (this.decelerateInterpolator == null) {
            this.decelerateInterpolator = new DecelerateInterpolator();
         }

         AnimatorSet var13 = new AnimatorSet();
         var13.setInterpolator(this.decelerateInterpolator);
         var13.setDuration(150L);
         if (var12) {
            var11.add(ObjectAnimator.ofFloat(this.windowView, "alpha", new float[]{0.0F}));
            var13.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (PipVideoView.this.parentSheet != null) {
                     PipVideoView.this.parentSheet.destroy();
                  } else if (PipVideoView.this.photoViewer != null) {
                     PipVideoView.this.photoViewer.destroyPhotoViewer();
                  }

               }
            });
         }

         var13.playTogether(var11);
         var13.start();
      }

   }

   public static Rect getPipRect(float var0) {
      SharedPreferences var1 = ApplicationLoader.applicationContext.getSharedPreferences("pipconfig", 0);
      int var2 = var1.getInt("sidex", 1);
      int var3 = var1.getInt("sidey", 0);
      float var4 = var1.getFloat("px", 0.0F);
      float var5 = var1.getFloat("py", 0.0F);
      int var6;
      int var7;
      if (var0 > 1.0F) {
         var6 = AndroidUtilities.dp(192.0F);
         var7 = (int)((float)var6 / var0);
      } else {
         var7 = AndroidUtilities.dp(192.0F);
         var6 = (int)((float)var7 * var0);
      }

      return new Rect((float)getSideCoord(true, var2, var4, var6), (float)getSideCoord(false, var3, var5, var7), (float)var6, (float)var7);
   }

   private static int getSideCoord(boolean var0, int var1, float var2, int var3) {
      int var4;
      if (var0) {
         var4 = AndroidUtilities.displaySize.x;
      } else {
         var4 = AndroidUtilities.displaySize.y - var3;
         var3 = ActionBar.getCurrentActionBarHeight();
      }

      var3 = var4 - var3;
      if (var1 == 0) {
         var1 = AndroidUtilities.dp(10.0F);
      } else if (var1 == 1) {
         var1 = var3 - AndroidUtilities.dp(10.0F);
      } else {
         var1 = Math.round((float)(var3 - AndroidUtilities.dp(20.0F)) * var2) + AndroidUtilities.dp(10.0F);
      }

      var3 = var1;
      if (!var0) {
         var3 = var1 + ActionBar.getCurrentActionBarHeight();
      }

      return var3;
   }

   public void close() {
      try {
         this.windowManager.removeView(this.windowView);
      } catch (Exception var2) {
      }

      this.parentSheet = null;
      this.photoViewer = null;
      this.parentActivity = null;
   }

   @Keep
   public int getX() {
      return this.windowLayoutParams.x;
   }

   @Keep
   public int getY() {
      return this.windowLayoutParams.y;
   }

   public void onConfigurationChanged() {
      int var1 = this.preferences.getInt("sidex", 1);
      int var2 = this.preferences.getInt("sidey", 0);
      float var3 = this.preferences.getFloat("px", 0.0F);
      float var4 = this.preferences.getFloat("py", 0.0F);
      this.windowLayoutParams.x = getSideCoord(true, var1, var3, this.videoWidth);
      this.windowLayoutParams.y = getSideCoord(false, var2, var4, this.videoHeight);
      this.windowManager.updateViewLayout(this.windowView, this.windowLayoutParams);
   }

   public void onVideoCompleted() {
      View var1 = this.controlsView;
      if (var1 instanceof PipVideoView.MiniControlsView) {
         PipVideoView.MiniControlsView var2 = (PipVideoView.MiniControlsView)var1;
         var2.isCompleted = true;
         var2.progress = 0.0F;
         var2.bufferedPosition = 0.0F;
         var2.updatePlayButton();
         var2.invalidate();
         var2.show(true, true);
      }

   }

   public void setBufferedProgress(float var1) {
      View var2 = this.controlsView;
      if (var2 instanceof PipVideoView.MiniControlsView) {
         ((PipVideoView.MiniControlsView)var2).setBufferedProgress(var1);
      }

   }

   @Keep
   public void setX(int var1) {
      LayoutParams var2 = this.windowLayoutParams;
      var2.x = var1;
      this.windowManager.updateViewLayout(this.windowView, var2);
   }

   @Keep
   public void setY(int var1) {
      LayoutParams var2 = this.windowLayoutParams;
      var2.y = var1;
      this.windowManager.updateViewLayout(this.windowView, var2);
   }

   public TextureView show(Activity var1, EmbedBottomSheet var2, View var3, float var4, int var5, WebView var6) {
      return this.show(var1, (PhotoViewer)null, var2, var3, var4, var5, var6);
   }

   public TextureView show(Activity var1, PhotoViewer var2, float var3, int var4) {
      return this.show(var1, var2, (EmbedBottomSheet)null, (View)null, var3, var4, (WebView)null);
   }

   public TextureView show(Activity var1, PhotoViewer var2, EmbedBottomSheet var3, View var4, float var5, int var6, WebView var7) {
      this.parentSheet = var3;
      this.parentActivity = var1;
      this.photoViewer = var2;
      this.windowView = new FrameLayout(var1) {
         private boolean dragging;
         private float startX;
         private float startY;

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            float var2 = var1.getRawX();
            float var3 = var1.getRawY();
            if (var1.getAction() == 0) {
               this.startX = var2;
               this.startY = var3;
            } else if (var1.getAction() == 2 && !this.dragging && (Math.abs(this.startX - var2) >= AndroidUtilities.getPixelsInCM(0.3F, true) || Math.abs(this.startY - var3) >= AndroidUtilities.getPixelsInCM(0.3F, false))) {
               this.dragging = true;
               this.startX = var2;
               this.startY = var3;
               if (PipVideoView.this.controlsView != null) {
                  ((ViewParent)PipVideoView.this.controlsView).requestDisallowInterceptTouchEvent(true);
               }

               return true;
            }

            return super.onInterceptTouchEvent(var1);
         }

         public boolean onTouchEvent(MotionEvent var1) {
            if (!this.dragging) {
               return false;
            } else {
               float var2 = var1.getRawX();
               float var3 = var1.getRawY();
               if (var1.getAction() == 2) {
                  float var4 = this.startX;
                  float var5 = this.startY;
                  LayoutParams var9 = PipVideoView.this.windowLayoutParams;
                  var9.x = (int)((float)var9.x + (var2 - var4));
                  var9 = PipVideoView.this.windowLayoutParams;
                  var9.y = (int)((float)var9.y + (var3 - var5));
                  int var6 = PipVideoView.this.videoWidth / 2;
                  int var7 = PipVideoView.this.windowLayoutParams.x;
                  int var8 = -var6;
                  if (var7 < var8) {
                     PipVideoView.this.windowLayoutParams.x = var8;
                  } else if (PipVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipVideoView.this.windowLayoutParams.width + var6) {
                     PipVideoView.this.windowLayoutParams.x = AndroidUtilities.displaySize.x - PipVideoView.this.windowLayoutParams.width + var6;
                  }

                  var7 = PipVideoView.this.windowLayoutParams.x;
                  var4 = 1.0F;
                  if (var7 < 0) {
                     var4 = 1.0F + (float)PipVideoView.this.windowLayoutParams.x / (float)var6 * 0.5F;
                  } else if (PipVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipVideoView.this.windowLayoutParams.width) {
                     var4 = 1.0F - (float)(PipVideoView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x + PipVideoView.this.windowLayoutParams.width) / (float)var6 * 0.5F;
                  }

                  if (PipVideoView.this.windowView.getAlpha() != var4) {
                     PipVideoView.this.windowView.setAlpha(var4);
                  }

                  if (PipVideoView.this.windowLayoutParams.y < 0) {
                     PipVideoView.this.windowLayoutParams.y = 0;
                  } else if (PipVideoView.this.windowLayoutParams.y > AndroidUtilities.displaySize.y - PipVideoView.this.windowLayoutParams.height + 0) {
                     PipVideoView.this.windowLayoutParams.y = AndroidUtilities.displaySize.y - PipVideoView.this.windowLayoutParams.height + 0;
                  }

                  PipVideoView.this.windowManager.updateViewLayout(PipVideoView.this.windowView, PipVideoView.this.windowLayoutParams);
                  this.startX = var2;
                  this.startY = var3;
               } else if (var1.getAction() == 1) {
                  this.dragging = false;
                  PipVideoView.this.animateToBoundsMaybe();
               }

               return true;
            }
         }

         public void requestDisallowInterceptTouchEvent(boolean var1) {
            super.requestDisallowInterceptTouchEvent(var1);
         }
      };
      if (var5 > 1.0F) {
         this.videoWidth = AndroidUtilities.dp(192.0F);
         this.videoHeight = (int)((float)this.videoWidth / var5);
      } else {
         this.videoHeight = AndroidUtilities.dp(192.0F);
         this.videoWidth = (int)((float)this.videoHeight * var5);
      }

      AspectRatioFrameLayout var8 = new AspectRatioFrameLayout(var1);
      var8.setAspectRatio(var5, var6);
      this.windowView.addView(var8, LayoutHelper.createFrame(-1, -1, 17));
      TextureView var18;
      if (var7 != null) {
         ViewGroup var17 = (ViewGroup)var7.getParent();
         if (var17 != null) {
            var17.removeView(var7);
         }

         var8.addView(var7, LayoutHelper.createFrame(-1, -1.0F));
         var18 = null;
      } else {
         var18 = new TextureView(var1);
         var8.addView(var18, LayoutHelper.createFrame(-1, -1.0F));
      }

      if (var4 == null) {
         boolean var9;
         if (var2 != null) {
            var9 = true;
         } else {
            var9 = false;
         }

         this.controlsView = new PipVideoView.MiniControlsView(var1, var9);
      } else {
         this.controlsView = var4;
      }

      this.windowView.addView(this.controlsView, LayoutHelper.createFrame(-1, -1.0F));
      this.windowManager = (WindowManager)ApplicationLoader.applicationContext.getSystemService("window");
      this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("pipconfig", 0);
      int var10 = this.preferences.getInt("sidex", 1);
      var6 = this.preferences.getInt("sidey", 0);
      float var11 = this.preferences.getFloat("px", 0.0F);
      var5 = this.preferences.getFloat("py", 0.0F);

      Exception var10000;
      label60: {
         boolean var10001;
         label54: {
            try {
               LayoutParams var15 = new LayoutParams();
               this.windowLayoutParams = var15;
               this.windowLayoutParams.width = this.videoWidth;
               this.windowLayoutParams.height = this.videoHeight;
               this.windowLayoutParams.x = getSideCoord(true, var10, var11, this.videoWidth);
               this.windowLayoutParams.y = getSideCoord(false, var6, var5, this.videoHeight);
               this.windowLayoutParams.format = -3;
               this.windowLayoutParams.gravity = 51;
               if (VERSION.SDK_INT >= 26) {
                  this.windowLayoutParams.type = 2038;
                  break label54;
               }
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
               break label60;
            }

            try {
               this.windowLayoutParams.type = 2003;
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label60;
            }
         }

         try {
            this.windowLayoutParams.flags = 16777736;
            this.windowManager.addView(this.windowView, this.windowLayoutParams);
            return var18;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
         }
      }

      Exception var16 = var10000;
      FileLog.e((Throwable)var16);
      return null;
   }

   public void updatePlayButton() {
      View var1 = this.controlsView;
      if (var1 instanceof PipVideoView.MiniControlsView) {
         PipVideoView.MiniControlsView var2 = (PipVideoView.MiniControlsView)var1;
         var2.updatePlayButton();
         var2.invalidate();
      }

   }

   private class MiniControlsView extends FrameLayout {
      private float bufferedPosition;
      private AnimatorSet currentAnimation;
      private Runnable hideRunnable = new _$$Lambda$PipVideoView$MiniControlsView$1lMZ0uhQOCqoH6v0Akw4gfbgK2g(this);
      private ImageView inlineButton;
      private boolean isCompleted;
      private boolean isVisible = true;
      private ImageView playButton;
      private float progress;
      private Paint progressInnerPaint;
      private Paint progressPaint;
      private Runnable progressRunnable = new Runnable() {
         public void run() {
            if (PipVideoView.this.photoViewer != null) {
               VideoPlayer var1 = PipVideoView.this.photoViewer.getVideoPlayer();
               if (var1 != null) {
                  MiniControlsView.this.setProgress((float)var1.getCurrentPosition() / (float)var1.getDuration());
                  if (PipVideoView.this.photoViewer == null) {
                     MiniControlsView.this.setBufferedProgress((float)var1.getBufferedPosition() / (float)var1.getDuration());
                  }

                  AndroidUtilities.runOnUIThread(MiniControlsView.this.progressRunnable, 1000L);
               }
            }
         }
      };

      public MiniControlsView(Context var2, boolean var3) {
         super(var2);
         this.inlineButton = new ImageView(var2);
         this.inlineButton.setScaleType(ScaleType.CENTER);
         this.inlineButton.setImageResource(2131165458);
         this.addView(this.inlineButton, LayoutHelper.createFrame(56, 48, 53));
         this.inlineButton.setOnClickListener(new _$$Lambda$PipVideoView$MiniControlsView$wbm8DJnUhe315ylEORpdCN9TCPU(this));
         if (var3) {
            this.progressPaint = new Paint();
            this.progressPaint.setColor(-15095832);
            this.progressInnerPaint = new Paint();
            this.progressInnerPaint.setColor(-6975081);
            this.setWillNotDraw(false);
            this.playButton = new ImageView(var2);
            this.playButton.setScaleType(ScaleType.CENTER);
            this.addView(this.playButton, LayoutHelper.createFrame(48, 48, 17));
            this.playButton.setOnClickListener(new _$$Lambda$PipVideoView$MiniControlsView$1JTbmkeJbI8PQhj0gt64f8TrJNY(this));
         }

         this.setOnTouchListener(_$$Lambda$PipVideoView$MiniControlsView$jKPELGvyg4VHgdM0XCb_ZaGLO4U.INSTANCE);
         this.updatePlayButton();
         this.show(false, false);
      }

      private void checkNeedHide() {
         AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
         if (this.isVisible) {
            AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
         }

      }

      // $FF: synthetic method
      static boolean lambda$new$3(View var0, MotionEvent var1) {
         return true;
      }

      private void updatePlayButton() {
         if (PipVideoView.this.photoViewer != null) {
            VideoPlayer var1 = PipVideoView.this.photoViewer.getVideoPlayer();
            if (var1 != null) {
               AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
               if (!var1.isPlaying()) {
                  if (this.isCompleted) {
                     this.playButton.setImageResource(2131165426);
                  } else {
                     this.playButton.setImageResource(2131165464);
                  }
               } else {
                  this.playButton.setImageResource(2131165460);
                  AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
               }

            }
         }
      }

      // $FF: synthetic method
      public void lambda$new$0$PipVideoView$MiniControlsView() {
         this.show(false, true);
      }

      // $FF: synthetic method
      public void lambda$new$1$PipVideoView$MiniControlsView(View var1) {
         if (PipVideoView.this.parentSheet != null) {
            PipVideoView.this.parentSheet.exitFromPip();
         } else if (PipVideoView.this.photoViewer != null) {
            PipVideoView.this.photoViewer.exitFromPip();
         }

      }

      // $FF: synthetic method
      public void lambda$new$2$PipVideoView$MiniControlsView(View var1) {
         if (PipVideoView.this.photoViewer != null) {
            VideoPlayer var2 = PipVideoView.this.photoViewer.getVideoPlayer();
            if (var2 != null) {
               if (var2.isPlaying()) {
                  var2.pause();
               } else {
                  var2.play();
               }

               this.updatePlayButton();
            }
         }
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         this.checkNeedHide();
      }

      protected void onDraw(Canvas var1) {
         int var2 = this.getMeasuredWidth();
         int var3 = this.getMeasuredHeight() - AndroidUtilities.dp(3.0F);
         AndroidUtilities.dp(7.0F);
         float var4 = (float)(var2 - 0);
         var2 = (int)(this.progress * var4);
         float var5 = this.bufferedPosition;
         if (var5 != 0.0F) {
            float var6 = (float)0;
            var1.drawRect(var6, (float)var3, var6 + var4 * var5, (float)(AndroidUtilities.dp(3.0F) + var3), this.progressInnerPaint);
         }

         var1.drawRect((float)0, (float)var3, (float)(var2 + 0), (float)(var3 + AndroidUtilities.dp(3.0F)), this.progressPaint);
      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         if (var1.getAction() == 0) {
            if (!this.isVisible) {
               this.show(true, true);
               return true;
            }

            this.checkNeedHide();
         }

         return super.onInterceptTouchEvent(var1);
      }

      public void requestDisallowInterceptTouchEvent(boolean var1) {
         super.requestDisallowInterceptTouchEvent(var1);
         this.checkNeedHide();
      }

      public void setBufferedProgress(float var1) {
         this.bufferedPosition = var1;
         this.invalidate();
      }

      public void setProgress(float var1) {
         this.progress = var1;
         this.invalidate();
      }

      public void show(boolean var1, boolean var2) {
         if (this.isVisible != var1) {
            this.isVisible = var1;
            AnimatorSet var3 = this.currentAnimation;
            if (var3 != null) {
               var3.cancel();
            }

            if (this.isVisible) {
               if (var2) {
                  this.currentAnimation = new AnimatorSet();
                  this.currentAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "alpha", new float[]{1.0F})});
                  this.currentAnimation.setDuration(150L);
                  this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        MiniControlsView.this.currentAnimation = null;
                     }
                  });
                  this.currentAnimation.start();
               } else {
                  this.setAlpha(1.0F);
               }
            } else if (var2) {
               this.currentAnimation = new AnimatorSet();
               this.currentAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0F})});
               this.currentAnimation.setDuration(150L);
               this.currentAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     MiniControlsView.this.currentAnimation = null;
                  }
               });
               this.currentAnimation.start();
            } else {
               this.setAlpha(0.0F);
            }

            this.checkNeedHide();
         }
      }
   }
}
