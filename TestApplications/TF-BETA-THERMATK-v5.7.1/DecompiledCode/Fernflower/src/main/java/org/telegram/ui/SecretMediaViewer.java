package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.MeasureSpec;
import android.view.WindowManager.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.annotation.Keep;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Scroller;
import org.telegram.ui.Components.VideoPlayer;

public class SecretMediaViewer implements NotificationCenter.NotificationCenterDelegate, OnGestureListener, OnDoubleTapListener {
   @SuppressLint({"StaticFieldLeak"})
   private static volatile SecretMediaViewer Instance;
   private ActionBar actionBar;
   private float animateToClipBottom;
   private float animateToClipHorizontal;
   private float animateToClipTop;
   private float animateToScale;
   private float animateToX;
   private float animateToY;
   private long animationStartTime;
   private float animationValue;
   private AspectRatioFrameLayout aspectRatioFrameLayout;
   private Paint blackPaint = new Paint();
   private boolean canDragDown = true;
   private ImageReceiver centerImage = new ImageReceiver();
   private float clipBottom;
   private float clipHorizontal;
   private float clipTop;
   private long closeTime;
   private boolean closeVideoAfterWatch;
   private SecretMediaViewer.FrameLayoutDrawer containerView;
   private int[] coords = new int[2];
   private int currentAccount;
   private AnimatorSet currentActionBarAnimation;
   private int currentChannelId;
   private MessageObject currentMessageObject;
   private PhotoViewer.PhotoViewerProvider currentProvider;
   private int currentRotation;
   private ImageReceiver.BitmapHolder currentThumb;
   private boolean disableShowCheck;
   private boolean discardTap;
   private boolean doubleTap;
   private float dragY;
   private boolean draggingDown;
   private GestureDetector gestureDetector;
   private AnimatorSet imageMoveAnimation;
   private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.5F);
   private boolean invalidCoords;
   private boolean isActionBarVisible = true;
   private boolean isPhotoVisible;
   private boolean isPlaying;
   private boolean isVideo;
   private boolean isVisible;
   private Object lastInsets;
   private float maxX;
   private float maxY;
   private float minX;
   private float minY;
   private float moveStartX;
   private float moveStartY;
   private boolean moving;
   private long openTime;
   private Activity parentActivity;
   private Runnable photoAnimationEndRunnable;
   private int photoAnimationInProgress;
   private SecretMediaViewer.PhotoBackgroundDrawable photoBackgroundDrawable = new SecretMediaViewer.PhotoBackgroundDrawable(-16777216);
   private long photoTransitionAnimationStartTime;
   private float pinchCenterX;
   private float pinchCenterY;
   private float pinchStartDistance;
   private float pinchStartScale = 1.0F;
   private float pinchStartX;
   private float pinchStartY;
   private int playerRetryPlayCount;
   private float scale = 1.0F;
   private Scroller scroller;
   private SecretMediaViewer.SecretDeleteTimer secretDeleteTimer;
   private boolean textureUploaded;
   private float translationX;
   private float translationY;
   private boolean useOvershootForScale;
   private VelocityTracker velocityTracker;
   private float videoCrossfadeAlpha;
   private long videoCrossfadeAlphaLastTime;
   private boolean videoCrossfadeStarted;
   private VideoPlayer videoPlayer;
   private TextureView videoTextureView;
   private boolean videoWatchedOneTime;
   private LayoutParams windowLayoutParams;
   private FrameLayout windowView;
   private boolean zoomAnimation;
   private boolean zooming;

   // $FF: synthetic method
   static int access$1210(SecretMediaViewer var0) {
      int var1 = var0.playerRetryPlayCount--;
      return var1;
   }

   private void animateTo(float var1, float var2, float var3, boolean var4) {
      this.animateTo(var1, var2, var3, var4, 250);
   }

   private void animateTo(float var1, float var2, float var3, boolean var4, int var5) {
      if (this.scale != var1 || this.translationX != var2 || this.translationY != var3) {
         this.zoomAnimation = var4;
         this.animateToScale = var1;
         this.animateToX = var2;
         this.animateToY = var3;
         this.animationStartTime = System.currentTimeMillis();
         this.imageMoveAnimation = new AnimatorSet();
         this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0F, 1.0F})});
         this.imageMoveAnimation.setInterpolator(this.interpolator);
         this.imageMoveAnimation.setDuration((long)var5);
         this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               SecretMediaViewer.this.imageMoveAnimation = null;
               SecretMediaViewer.this.containerView.invalidate();
            }
         });
         this.imageMoveAnimation.start();
      }
   }

   private void checkMinMax(boolean var1) {
      float var2 = this.translationX;
      float var3 = this.translationY;
      this.updateMinMax(this.scale);
      float var4 = this.translationX;
      float var5 = this.minX;
      if (var4 < var5) {
         var2 = var5;
      } else {
         var5 = this.maxX;
         if (var4 > var5) {
            var2 = var5;
         }
      }

      var4 = this.translationY;
      var5 = this.minY;
      if (var4 < var5) {
         var3 = var5;
      } else {
         var5 = this.maxY;
         if (var4 > var5) {
            var3 = var5;
         }
      }

      this.animateTo(this.scale, var2, var3, var1);
   }

   private boolean checkPhotoAnimation() {
      int var1 = this.photoAnimationInProgress;
      boolean var2 = false;
      if (var1 != 0 && Math.abs(this.photoTransitionAnimationStartTime - System.currentTimeMillis()) >= 500L) {
         Runnable var3 = this.photoAnimationEndRunnable;
         if (var3 != null) {
            var3.run();
            this.photoAnimationEndRunnable = null;
         }

         this.photoAnimationInProgress = 0;
      }

      if (this.photoAnimationInProgress != 0) {
         var2 = true;
      }

      return var2;
   }

   private int getContainerViewHeight() {
      return this.containerView.getHeight();
   }

   private int getContainerViewWidth() {
      return this.containerView.getWidth();
   }

   public static SecretMediaViewer getInstance() {
      SecretMediaViewer var0 = Instance;
      SecretMediaViewer var1 = var0;
      if (var0 == null) {
         synchronized(PhotoViewer.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new SecretMediaViewer();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public static boolean hasInstance() {
      boolean var0;
      if (Instance != null) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   private void onDraw(Canvas var1) {
      if (this.isPhotoVisible) {
         AnimatorSet var2 = this.imageMoveAnimation;
         boolean var3 = false;
         float var4;
         float var5;
         float var6;
         float var7;
         float var8;
         float var9;
         float var10;
         float var11;
         if (var2 != null) {
            if (!this.scroller.isFinished()) {
               this.scroller.abortAnimation();
            }

            if (this.useOvershootForScale) {
               var4 = this.animationValue;
               if (var4 < 0.9F) {
                  var5 = var4 / 0.9F;
                  var4 = this.scale;
                  var4 += (this.animateToScale * 1.02F - var4) * var5;
               } else {
                  var5 = this.animateToScale;
                  var4 = var5 + 0.01999998F * var5 * (1.0F - (var4 - 0.9F) / 0.100000024F);
                  var5 = 1.0F;
               }

               var6 = this.translationY;
               var7 = this.animateToY;
               var8 = this.translationX;
               var9 = var8 + (this.animateToX - var8) * var5;
               var8 = this.clipTop;
               var10 = var8 + (this.animateToClipTop - var8) * var5;
               var8 = this.clipBottom;
               var11 = var8 + (this.animateToClipBottom - var8) * var5;
               var8 = this.clipHorizontal;
               var8 += (this.animateToClipHorizontal - var8) * var5;
               var5 = var6 + (var7 - var6) * var5;
            } else {
               var5 = this.scale;
               var4 = this.animateToScale;
               var8 = this.animationValue;
               var4 = (var4 - var5) * var8 + var5;
               var5 = this.translationY;
               var5 += (this.animateToY - var5) * var8;
               var11 = this.translationX;
               var9 = var11 + (this.animateToX - var11) * var8;
               var11 = this.clipTop;
               var10 = var11 + (this.animateToClipTop - var11) * var8;
               var11 = this.clipBottom;
               var11 += (this.animateToClipBottom - var11) * var8;
               var7 = this.clipHorizontal;
               var8 = var7 + (this.animateToClipHorizontal - var7) * var8;
            }

            if (this.animateToScale == 1.0F && this.scale == 1.0F && this.translationX == 0.0F) {
               var7 = var5;
            } else {
               var7 = -1.0F;
            }

            this.containerView.invalidate();
         } else {
            if (this.animationStartTime != 0L) {
               this.translationX = this.animateToX;
               this.translationY = this.animateToY;
               this.clipBottom = this.animateToClipBottom;
               this.clipTop = this.animateToClipTop;
               this.clipHorizontal = this.animateToClipHorizontal;
               this.scale = this.animateToScale;
               this.animationStartTime = 0L;
               this.updateMinMax(this.scale);
               this.zoomAnimation = false;
               this.useOvershootForScale = false;
            }

            if (!this.scroller.isFinished() && this.scroller.computeScrollOffset()) {
               if ((float)this.scroller.getStartX() < this.maxX && (float)this.scroller.getStartX() > this.minX) {
                  this.translationX = (float)this.scroller.getCurrX();
               }

               if ((float)this.scroller.getStartY() < this.maxY && (float)this.scroller.getStartY() > this.minY) {
                  this.translationY = (float)this.scroller.getCurrY();
               }

               this.containerView.invalidate();
            }

            var4 = this.scale;
            var7 = this.translationY;
            var9 = this.translationX;
            var10 = this.clipTop;
            var11 = this.clipBottom;
            var8 = this.clipHorizontal;
            if (!this.moving) {
               var5 = var7;
            } else {
               var5 = var7;
               var7 = -1.0F;
            }
         }

         label114: {
            if (this.photoAnimationInProgress != 3) {
               if (this.scale == 1.0F && var7 != -1.0F && !this.zoomAnimation) {
                  var6 = (float)this.getContainerViewHeight() / 4.0F;
                  this.photoBackgroundDrawable.setAlpha((int)Math.max(127.0F, (1.0F - Math.min(Math.abs(var7), var6) / var6) * 255.0F));
               } else {
                  this.photoBackgroundDrawable.setAlpha(255);
               }

               if (!this.zoomAnimation) {
                  var7 = this.maxX;
                  if (var9 > var7) {
                     var7 = Math.min(1.0F, (var9 - var7) / (float)var1.getWidth());
                     var6 = 0.3F * var7;
                     var7 = 1.0F - var7;
                     var9 = this.maxX;
                     break label114;
                  }
               }
            }

            var7 = 1.0F;
            var6 = 0.0F;
         }

         AspectRatioFrameLayout var21 = this.aspectRatioFrameLayout;
         boolean var12 = var3;
         if (var21 != null) {
            var12 = var3;
            if (var21.getVisibility() == 0) {
               var12 = true;
            }
         }

         var1.save();
         var4 -= var6;
         var1.translate((float)(this.getContainerViewWidth() / 2) + var9, (float)(this.getContainerViewHeight() / 2) + var5);
         var1.scale(var4, var4);
         int var13 = this.centerImage.getBitmapWidth();
         int var14 = this.centerImage.getBitmapHeight();
         int var15 = var13;
         int var22 = var14;
         if (var12) {
            var15 = var13;
            var22 = var14;
            if (this.textureUploaded) {
               var15 = var13;
               var22 = var14;
               if (Math.abs((float)var13 / (float)var14 - (float)this.videoTextureView.getMeasuredWidth() / (float)this.videoTextureView.getMeasuredHeight()) > 0.01F) {
                  var15 = this.videoTextureView.getMeasuredWidth();
                  var22 = this.videoTextureView.getMeasuredHeight();
               }
            }
         }

         var9 = (float)this.getContainerViewHeight();
         var5 = (float)var22;
         float var16 = var9 / var5;
         var6 = (float)this.getContainerViewWidth();
         var9 = (float)var15;
         var6 = Math.min(var16, var6 / var9);
         var13 = (int)(var9 * var6);
         var14 = (int)(var5 * var6);
         var22 = -var13 / 2;
         var5 = (float)var22;
         var9 = var8 / var4;
         var15 = -var14 / 2;
         var8 = (float)var15;
         var1.clipRect(var5 + var9, var10 / var4 + var8, (float)(var13 / 2) - var9, (float)(var14 / 2) - var11 / var4);
         if (!var12 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0F) {
            this.centerImage.setAlpha(var7);
            this.centerImage.setImageCoords(var22, var15, var13, var14);
            this.centerImage.draw(var1);
         }

         if (var12) {
            if (!this.videoCrossfadeStarted && this.textureUploaded) {
               this.videoCrossfadeStarted = true;
               this.videoCrossfadeAlpha = 0.0F;
               this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
            }

            var1.translate(var5, var8);
            this.videoTextureView.setAlpha(var7 * this.videoCrossfadeAlpha);
            this.aspectRatioFrameLayout.draw(var1);
            if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0F) {
               long var17 = System.currentTimeMillis();
               long var19 = this.videoCrossfadeAlphaLastTime;
               this.videoCrossfadeAlphaLastTime = var17;
               this.videoCrossfadeAlpha += (float)(var17 - var19) / 200.0F;
               this.containerView.invalidate();
               if (this.videoCrossfadeAlpha > 1.0F) {
                  this.videoCrossfadeAlpha = 1.0F;
               }
            }
         }

         var1.restore();
      }
   }

   private void onPhotoClosed(PhotoViewer.PlaceProviderObject var1) {
      this.isVisible = false;
      this.currentProvider = null;
      this.disableShowCheck = false;
      this.releasePlayer();
      new ArrayList();
      AndroidUtilities.runOnUIThread(new _$$Lambda$SecretMediaViewer$WZXZJ3O2MqtXJfvuP6KrDf_oyHw(this), 50L);
   }

   private void preparePlayer(final File var1) {
      if (this.parentActivity != null) {
         this.releasePlayer();
         if (this.videoTextureView == null) {
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(this.parentActivity);
            this.aspectRatioFrameLayout.setVisibility(4);
            this.containerView.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
            this.videoTextureView = new TextureView(this.parentActivity);
            this.videoTextureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1, 17));
         }

         this.textureUploaded = false;
         this.videoCrossfadeStarted = false;
         TextureView var2 = this.videoTextureView;
         this.videoCrossfadeAlpha = 0.0F;
         var2.setAlpha(0.0F);
         if (this.videoPlayer == null) {
            this.videoPlayer = new VideoPlayer();
            this.videoPlayer.setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate(new VideoPlayer.VideoPlayerDelegate() {
               // $FF: synthetic method
               public void lambda$onError$0$SecretMediaViewer$1(File var1x) {
                  SecretMediaViewer.this.preparePlayer(var1x);
               }

               public void onError(Exception var1x) {
                  if (SecretMediaViewer.this.playerRetryPlayCount > 0) {
                     SecretMediaViewer.access$1210(SecretMediaViewer.this);
                     AndroidUtilities.runOnUIThread(new _$$Lambda$SecretMediaViewer$1$dToY1BP1yRZuDyMam95X6X8QLzo(this, var1), 100L);
                  } else {
                     FileLog.e((Throwable)var1x);
                  }

               }

               public void onRenderedFirstFrame() {
                  if (!SecretMediaViewer.this.textureUploaded) {
                     SecretMediaViewer.this.textureUploaded = true;
                     SecretMediaViewer.this.containerView.invalidate();
                  }

               }

               public void onStateChanged(boolean var1x, int var2) {
                  if (SecretMediaViewer.this.videoPlayer != null && SecretMediaViewer.this.currentMessageObject != null) {
                     if (var2 != 4 && var2 != 1) {
                        try {
                           SecretMediaViewer.this.parentActivity.getWindow().addFlags(128);
                        } catch (Exception var5) {
                           FileLog.e((Throwable)var5);
                        }
                     } else {
                        try {
                           SecretMediaViewer.this.parentActivity.getWindow().clearFlags(128);
                        } catch (Exception var4) {
                           FileLog.e((Throwable)var4);
                        }
                     }

                     if (var2 == 3 && SecretMediaViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                        SecretMediaViewer.this.aspectRatioFrameLayout.setVisibility(0);
                     }

                     if (SecretMediaViewer.this.videoPlayer.isPlaying() && var2 != 4) {
                        if (!SecretMediaViewer.this.isPlaying) {
                           SecretMediaViewer.this.isPlaying = true;
                        }
                     } else if (SecretMediaViewer.this.isPlaying) {
                        SecretMediaViewer.this.isPlaying = false;
                        if (var2 == 4) {
                           SecretMediaViewer.this.videoWatchedOneTime = true;
                           if (SecretMediaViewer.this.closeVideoAfterWatch) {
                              SecretMediaViewer.this.closePhoto(true, true);
                           } else {
                              SecretMediaViewer.this.videoPlayer.seekTo(0L);
                              SecretMediaViewer.this.videoPlayer.play();
                           }
                        }
                     }
                  }

               }

               public boolean onSurfaceDestroyed(SurfaceTexture var1x) {
                  return false;
               }

               public void onSurfaceTextureUpdated(SurfaceTexture var1x) {
               }

               public void onVideoSizeChanged(int var1x, int var2, int var3, float var4) {
                  if (SecretMediaViewer.this.aspectRatioFrameLayout != null) {
                     int var5 = var1x;
                     int var6 = var2;
                     if (var3 != 90) {
                        if (var3 == 270) {
                           var5 = var1x;
                           var6 = var2;
                        } else {
                           var6 = var1x;
                           var5 = var2;
                        }
                     }

                     AspectRatioFrameLayout var7 = SecretMediaViewer.this.aspectRatioFrameLayout;
                     if (var5 == 0) {
                        var4 = 1.0F;
                     } else {
                        var4 = (float)var6 * var4 / (float)var5;
                     }

                     var7.setAspectRatio(var4, var3);
                  }

               }
            });
         }

         this.videoPlayer.preparePlayer(Uri.fromFile(var1), "other");
         this.videoPlayer.setPlayWhenReady(true);
      }
   }

   private boolean processTouchEvent(MotionEvent var1) {
      if (this.photoAnimationInProgress == 0 && this.animationStartTime == 0L) {
         if (var1.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(var1) && this.doubleTap) {
            this.doubleTap = false;
            this.moving = false;
            this.zooming = false;
            this.checkMinMax(false);
            return true;
         }

         float var5;
         VelocityTracker var10;
         if (var1.getActionMasked() != 0 && var1.getActionMasked() != 5) {
            int var2 = var1.getActionMasked();
            float var3 = 0.0F;
            float var4;
            float var6;
            if (var2 == 2) {
               float var7;
               float var8;
               if (var1.getPointerCount() == 2 && !this.draggingDown && this.zooming) {
                  this.discardTap = true;
                  this.scale = (float)Math.hypot((double)(var1.getX(1) - var1.getX(0)), (double)(var1.getY(1) - var1.getY(0))) / this.pinchStartDistance * this.pinchStartScale;
                  this.translationX = this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - (this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - this.pinchStartX) * (this.scale / this.pinchStartScale);
                  var3 = this.pinchCenterY;
                  var4 = (float)(this.getContainerViewHeight() / 2);
                  var5 = this.pinchCenterY;
                  var6 = (float)(this.getContainerViewHeight() / 2);
                  var7 = this.pinchStartY;
                  var8 = this.scale;
                  this.translationY = var3 - var4 - (var5 - var6 - var7) * (var8 / this.pinchStartScale);
                  this.updateMinMax(var8);
                  this.containerView.invalidate();
               } else if (var1.getPointerCount() == 1) {
                  VelocityTracker var9 = this.velocityTracker;
                  if (var9 != null) {
                     var9.addMovement(var1);
                  }

                  var5 = Math.abs(var1.getX() - this.moveStartX);
                  var4 = Math.abs(var1.getY() - this.dragY);
                  if (var5 > (float)AndroidUtilities.dp(3.0F) || var4 > (float)AndroidUtilities.dp(3.0F)) {
                     this.discardTap = true;
                  }

                  if (this.canDragDown && !this.draggingDown && this.scale == 1.0F && var4 >= (float)AndroidUtilities.dp(30.0F) && var4 / 2.0F > var5) {
                     this.draggingDown = true;
                     this.moving = false;
                     this.dragY = var1.getY();
                     if (this.isActionBarVisible) {
                        this.toggleActionBar(false, true);
                     }

                     return true;
                  }

                  if (this.draggingDown) {
                     this.translationY = var1.getY() - this.dragY;
                     this.containerView.invalidate();
                  } else if (!this.invalidCoords && this.animationStartTime == 0L) {
                     var4 = this.moveStartX - var1.getX();
                     var5 = this.moveStartY - var1.getY();
                     if (this.moving || this.scale == 1.0F && Math.abs(var5) + (float)AndroidUtilities.dp(12.0F) < Math.abs(var4) || this.scale != 1.0F) {
                        if (!this.moving) {
                           this.moving = true;
                           this.canDragDown = false;
                           var4 = 0.0F;
                           var5 = 0.0F;
                        }

                        label164: {
                           this.moveStartX = var1.getX();
                           this.moveStartY = var1.getY();
                           this.updateMinMax(this.scale);
                           var8 = this.translationX;
                           if (var8 >= this.minX) {
                              var6 = var4;
                              if (var8 <= this.maxX) {
                                 break label164;
                              }
                           }

                           var6 = var4 / 3.0F;
                        }

                        label210: {
                           var7 = this.maxY;
                           if (var7 == 0.0F) {
                              var8 = this.minY;
                              if (var8 == 0.0F) {
                                 var4 = this.translationY;
                                 if (var4 - var5 < var8) {
                                    this.translationY = var8;
                                    var5 = var3;
                                 } else if (var4 - var5 > var7) {
                                    this.translationY = var7;
                                    var5 = var3;
                                 }
                                 break label210;
                              }
                           }

                           var4 = this.translationY;
                           if (var4 < this.minY || var4 > this.maxY) {
                              var5 /= 3.0F;
                           }
                        }

                        this.translationX -= var6;
                        if (this.scale != 1.0F) {
                           this.translationY -= var5;
                        }

                        this.containerView.invalidate();
                     }
                  } else {
                     this.invalidCoords = false;
                     this.moveStartX = var1.getX();
                     this.moveStartY = var1.getY();
                  }
               }
            } else if (var1.getActionMasked() == 3 || var1.getActionMasked() == 1 || var1.getActionMasked() == 6) {
               if (this.zooming) {
                  this.invalidCoords = true;
                  var5 = this.scale;
                  if (var5 < 1.0F) {
                     this.updateMinMax(1.0F);
                     this.animateTo(1.0F, 0.0F, 0.0F, true);
                  } else if (var5 > 3.0F) {
                     var5 = this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - (this.pinchCenterX - (float)(this.getContainerViewWidth() / 2) - this.pinchStartX) * (3.0F / this.pinchStartScale);
                     var4 = this.pinchCenterY - (float)(this.getContainerViewHeight() / 2) - (this.pinchCenterY - (float)(this.getContainerViewHeight() / 2) - this.pinchStartY) * (3.0F / this.pinchStartScale);
                     this.updateMinMax(3.0F);
                     var6 = this.minX;
                     if (var5 < var6) {
                        var5 = var6;
                     } else {
                        var6 = this.maxX;
                        if (var5 > var6) {
                           var5 = var6;
                        }
                     }

                     var6 = this.minY;
                     if (var4 < var6) {
                        var4 = var6;
                     } else {
                        var6 = this.maxY;
                        if (var4 > var6) {
                           var4 = var6;
                        }
                     }

                     this.animateTo(3.0F, var5, var4, true);
                  } else {
                     this.checkMinMax(true);
                  }

                  this.zooming = false;
               } else if (this.draggingDown) {
                  if (Math.abs(this.dragY - var1.getY()) > (float)this.getContainerViewHeight() / 6.0F) {
                     this.closePhoto(true, false);
                  } else {
                     this.animateTo(1.0F, 0.0F, 0.0F, false);
                  }

                  this.draggingDown = false;
               } else if (this.moving) {
                  var5 = this.translationX;
                  var4 = this.translationY;
                  this.updateMinMax(this.scale);
                  this.moving = false;
                  this.canDragDown = true;
                  var10 = this.velocityTracker;
                  if (var10 != null && this.scale == 1.0F) {
                     var10.computeCurrentVelocity(1000);
                  }

                  var3 = this.translationX;
                  var6 = this.minX;
                  if (var3 < var6) {
                     var5 = var6;
                  } else {
                     var6 = this.maxX;
                     if (var3 > var6) {
                        var5 = var6;
                     }
                  }

                  var3 = this.translationY;
                  var6 = this.minY;
                  if (var3 < var6) {
                     var4 = var6;
                  } else {
                     var6 = this.maxY;
                     if (var3 > var6) {
                        var4 = var6;
                     }
                  }

                  this.animateTo(this.scale, var5, var4, false);
               }
            }
         } else {
            this.discardTap = false;
            if (!this.scroller.isFinished()) {
               this.scroller.abortAnimation();
            }

            if (!this.draggingDown) {
               if (var1.getPointerCount() == 2) {
                  this.pinchStartDistance = (float)Math.hypot((double)(var1.getX(1) - var1.getX(0)), (double)(var1.getY(1) - var1.getY(0)));
                  this.pinchStartScale = this.scale;
                  this.pinchCenterX = (var1.getX(0) + var1.getX(1)) / 2.0F;
                  this.pinchCenterY = (var1.getY(0) + var1.getY(1)) / 2.0F;
                  this.pinchStartX = this.translationX;
                  this.pinchStartY = this.translationY;
                  this.zooming = true;
                  this.moving = false;
                  var10 = this.velocityTracker;
                  if (var10 != null) {
                     var10.clear();
                  }
               } else if (var1.getPointerCount() == 1) {
                  this.moveStartX = var1.getX();
                  var5 = var1.getY();
                  this.moveStartY = var5;
                  this.dragY = var5;
                  this.draggingDown = false;
                  this.canDragDown = true;
                  var10 = this.velocityTracker;
                  if (var10 != null) {
                     var10.clear();
                  }
               }
            }
         }
      }

      return false;
   }

   private void releasePlayer() {
      VideoPlayer var1 = this.videoPlayer;
      if (var1 != null) {
         this.playerRetryPlayCount = 0;
         var1.releasePlayer(true);
         this.videoPlayer = null;
      }

      try {
         if (this.parentActivity != null) {
            this.parentActivity.getWindow().clearFlags(128);
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      AspectRatioFrameLayout var3 = this.aspectRatioFrameLayout;
      if (var3 != null) {
         this.containerView.removeView(var3);
         this.aspectRatioFrameLayout = null;
      }

      if (this.videoTextureView != null) {
         this.videoTextureView = null;
      }

      this.isPlaying = false;
   }

   private boolean scaleToFill() {
      return false;
   }

   private void toggleActionBar(boolean var1, boolean var2) {
      if (var1) {
         this.actionBar.setVisibility(0);
      }

      this.actionBar.setEnabled(var1);
      this.isActionBarVisible = var1;
      float var3 = 1.0F;
      ActionBar var5;
      if (var2) {
         ArrayList var4 = new ArrayList();
         var5 = this.actionBar;
         if (!var1) {
            var3 = 0.0F;
         }

         var4.add(ObjectAnimator.ofFloat(var5, "alpha", new float[]{var3}));
         this.currentActionBarAnimation = new AnimatorSet();
         this.currentActionBarAnimation.playTogether(var4);
         if (!var1) {
            this.currentActionBarAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (SecretMediaViewer.this.currentActionBarAnimation != null && SecretMediaViewer.this.currentActionBarAnimation.equals(var1)) {
                     SecretMediaViewer.this.actionBar.setVisibility(8);
                     SecretMediaViewer.this.currentActionBarAnimation = null;
                  }

               }
            });
         }

         this.currentActionBarAnimation.setDuration(200L);
         this.currentActionBarAnimation.start();
      } else {
         var5 = this.actionBar;
         if (!var1) {
            var3 = 0.0F;
         }

         var5.setAlpha(var3);
         if (!var1) {
            this.actionBar.setVisibility(8);
         }
      }

   }

   private void updateMinMax(float var1) {
      int var2 = (int)((float)this.centerImage.getImageWidth() * var1 - (float)this.getContainerViewWidth()) / 2;
      int var3 = (int)((float)this.centerImage.getImageHeight() * var1 - (float)this.getContainerViewHeight()) / 2;
      if (var2 > 0) {
         this.minX = (float)(-var2);
         this.maxX = (float)var2;
      } else {
         this.maxX = 0.0F;
         this.minX = 0.0F;
      }

      if (var3 > 0) {
         this.minY = (float)(-var3);
         this.maxY = (float)var3;
      } else {
         this.maxY = 0.0F;
         this.minY = 0.0F;
      }

   }

   public void closePhoto(boolean var1, boolean var2) {
      if (this.parentActivity != null && this.isPhotoVisible && !this.checkPhotoAnimation()) {
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
         this.isActionBarVisible = false;
         VelocityTracker var3 = this.velocityTracker;
         if (var3 != null) {
            var3.recycle();
            this.velocityTracker = null;
         }

         final PhotoViewer.PlaceProviderObject var15;
         label87: {
            this.closeTime = System.currentTimeMillis();
            PhotoViewer.PhotoViewerProvider var4 = this.currentProvider;
            if (var4 != null) {
               MessageObject var5 = this.currentMessageObject;
               TLRPC.MessageMedia var14 = var5.messageOwner.media;
               if (!(var14.photo instanceof TLRPC.TL_photoEmpty) && !(var14.document instanceof TLRPC.TL_documentEmpty)) {
                  var15 = var4.getPlaceForPhoto(var5, (TLRPC.FileLocation)null, 0, true);
                  break label87;
               }
            }

            var15 = null;
         }

         VideoPlayer var16 = this.videoPlayer;
         if (var16 != null) {
            var16.pause();
         }

         if (var1) {
            this.photoAnimationInProgress = 3;
            this.containerView.invalidate();
            this.imageMoveAnimation = new AnimatorSet();
            int var8;
            int var10;
            if (var15 != null && var15.imageReceiver.getThumbBitmap() != null && !var2) {
               var15.imageReceiver.setVisible(false, true);
               RectF var17 = var15.imageReceiver.getDrawRegion();
               float var6 = var17.right - var17.left;
               float var7 = var17.bottom - var17.top;
               Point var19 = AndroidUtilities.displaySize;
               var8 = var19.x;
               int var9 = var19.y;
               if (VERSION.SDK_INT >= 21) {
                  var10 = AndroidUtilities.statusBarHeight;
               } else {
                  var10 = 0;
               }

               var10 += var9;
               this.animateToScale = Math.max(var6 / (float)var8, var7 / (float)var10);
               float var11 = (float)var15.viewX;
               float var12 = var17.left;
               this.animateToX = var11 + var12 + var6 / 2.0F - (float)(var8 / 2);
               this.animateToY = (float)var15.viewY + var17.top + var7 / 2.0F - (float)(var10 / 2);
               this.animateToClipHorizontal = Math.abs(var12 - (float)var15.imageReceiver.getImageX());
               var8 = (int)Math.abs(var17.top - (float)var15.imageReceiver.getImageY());
               int[] var20 = new int[2];
               var15.parentView.getLocationInWindow(var20);
               var9 = var20[1];
               if (VERSION.SDK_INT >= 21) {
                  var10 = 0;
               } else {
                  var10 = AndroidUtilities.statusBarHeight;
               }

               this.animateToClipTop = (float)(var9 - var10) - ((float)var15.viewY + var17.top) + (float)var15.clipTopAddition;
               if (this.animateToClipTop < 0.0F) {
                  this.animateToClipTop = 0.0F;
               }

               var11 = (float)var15.viewY;
               var12 = var17.top;
               var7 = (float)((int)var7);
               var9 = var20[1];
               int var13 = var15.parentView.getHeight();
               if (VERSION.SDK_INT >= 21) {
                  var10 = 0;
               } else {
                  var10 = AndroidUtilities.statusBarHeight;
               }

               this.animateToClipBottom = var11 + var12 + var7 - (float)(var9 + var13 - var10) + (float)var15.clipBottomAddition;
               if (this.animateToClipBottom < 0.0F) {
                  this.animateToClipBottom = 0.0F;
               }

               this.animationStartTime = System.currentTimeMillis();
               var11 = this.animateToClipBottom;
               var7 = (float)var8;
               this.animateToClipBottom = Math.max(var11, var7);
               this.animateToClipTop = Math.max(this.animateToClipTop, var7);
               this.zoomAnimation = true;
            } else {
               var8 = AndroidUtilities.displaySize.y;
               if (VERSION.SDK_INT >= 21) {
                  var10 = AndroidUtilities.statusBarHeight;
               } else {
                  var10 = 0;
               }

               var10 += var8;
               if (this.translationY < 0.0F) {
                  var10 = -var10;
               }

               this.animateToY = (float)var10;
            }

            if (this.isVideo) {
               this.videoCrossfadeStarted = false;
               this.textureUploaded = false;
               this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofInt(this.photoBackgroundDrawable, "alpha", new int[]{0}), ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.actionBar, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.secretDeleteTimer, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this, "videoCrossfadeAlpha", new float[]{0.0F})});
            } else {
               this.centerImage.setManualAlphaAnimator(true);
               this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofInt(this.photoBackgroundDrawable, "alpha", new int[]{0}), ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.actionBar, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.secretDeleteTimer, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(this.centerImage, "currentAlpha", new float[]{0.0F})});
            }

            this.photoAnimationEndRunnable = new _$$Lambda$SecretMediaViewer$FsmwJiUXxyTDqbCVCkGBtpTvcxw(this, var15);
            this.imageMoveAnimation.setInterpolator(new DecelerateInterpolator());
            this.imageMoveAnimation.setDuration(250L);
            this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
               // $FF: synthetic method
               public void lambda$onAnimationEnd$0$SecretMediaViewer$7() {
                  if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                     SecretMediaViewer.this.photoAnimationEndRunnable.run();
                     SecretMediaViewer.this.photoAnimationEndRunnable = null;
                  }

               }

               public void onAnimationEnd(Animator var1) {
                  PhotoViewer.PlaceProviderObject var2 = var15;
                  if (var2 != null) {
                     var2.imageReceiver.setVisible(true, true);
                  }

                  SecretMediaViewer.this.isVisible = false;
                  AndroidUtilities.runOnUIThread(new _$$Lambda$SecretMediaViewer$7$T5mvUOvsURLsFjHY_qOF1JNvIrM(this));
               }
            });
            this.photoTransitionAnimationStartTime = System.currentTimeMillis();
            if (VERSION.SDK_INT >= 18) {
               this.containerView.setLayerType(2, (Paint)null);
            }

            this.imageMoveAnimation.start();
         } else {
            AnimatorSet var18 = new AnimatorSet();
            var18.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.containerView, "scaleX", new float[]{0.9F}), ObjectAnimator.ofFloat(this.containerView, "scaleY", new float[]{0.9F}), ObjectAnimator.ofInt(this.photoBackgroundDrawable, "alpha", new int[]{0}), ObjectAnimator.ofFloat(this.actionBar, "alpha", new float[]{0.0F})});
            this.photoAnimationInProgress = 2;
            this.photoAnimationEndRunnable = new _$$Lambda$SecretMediaViewer$SXGR0dGO1jG_N03wXFFoPW86sNo(this, var15);
            var18.setDuration(200L);
            var18.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                     SecretMediaViewer.this.photoAnimationEndRunnable.run();
                     SecretMediaViewer.this.photoAnimationEndRunnable = null;
                  }

               }
            });
            this.photoTransitionAnimationStartTime = System.currentTimeMillis();
            if (VERSION.SDK_INT >= 18) {
               this.containerView.setLayerType(2, (Paint)null);
            }

            var18.start();
         }
      }

   }

   public void destroyPhotoViewer() {
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
      this.isVisible = false;
      this.currentProvider = null;
      ImageReceiver.BitmapHolder var1 = this.currentThumb;
      if (var1 != null) {
         var1.release();
         this.currentThumb = null;
      }

      this.releasePlayer();
      if (this.parentActivity != null) {
         FrameLayout var3 = this.windowView;
         if (var3 != null) {
            try {
               if (var3.getParent() != null) {
                  ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
               }

               this.windowView = null;
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
            }
         }
      }

      Instance = null;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.messagesDeleted) {
         if (this.currentMessageObject == null) {
            return;
         }

         if ((Integer)var3[1] != 0) {
            return;
         }

         if (((ArrayList)var3[0]).contains(this.currentMessageObject.getId())) {
            if (this.isVideo && !this.videoWatchedOneTime) {
               this.closeVideoAfterWatch = true;
            } else {
               this.closePhoto(true, true);
            }
         }
      } else if (var1 == NotificationCenter.didCreatedNewDeleteTask) {
         if (this.currentMessageObject == null || this.secretDeleteTimer == null) {
            return;
         }

         SparseArray var11 = (SparseArray)var3[0];

         for(var1 = 0; var1 < var11.size(); ++var1) {
            int var4 = var11.keyAt(var1);
            ArrayList var5 = (ArrayList)var11.get(var4);

            for(var2 = 0; var2 < var5.size(); ++var2) {
               long var6 = (Long)var5.get(var2);
               if (var2 == 0) {
                  int var8 = (int)(var6 >> 32);
                  int var9 = var8;
                  if (var8 < 0) {
                     var9 = 0;
                  }

                  if (var9 != this.currentChannelId) {
                     return;
                  }
               }

               if ((long)this.currentMessageObject.getId() == var6) {
                  this.currentMessageObject.messageOwner.destroyTime = var4;
                  this.secretDeleteTimer.invalidate();
                  return;
               }
            }
         }
      } else if (var1 == NotificationCenter.updateMessageMedia) {
         TLRPC.Message var10 = (TLRPC.Message)var3[0];
         if (this.currentMessageObject.getId() == var10.id) {
            if (this.isVideo && !this.videoWatchedOneTime) {
               this.closeVideoAfterWatch = true;
            } else {
               this.closePhoto(true, true);
            }
         }
      }

   }

   @Keep
   public float getAnimationValue() {
      return this.animationValue;
   }

   public long getCloseTime() {
      return this.closeTime;
   }

   public MessageObject getCurrentMessageObject() {
      return this.currentMessageObject;
   }

   public long getOpenTime() {
      return this.openTime;
   }

   @Keep
   public float getVideoCrossfadeAlpha() {
      return this.videoCrossfadeAlpha;
   }

   public boolean isShowingImage(MessageObject var1) {
      boolean var3;
      if (this.isVisible && !this.disableShowCheck && var1 != null) {
         MessageObject var2 = this.currentMessageObject;
         if (var2 != null && var2.getId() == var1.getId()) {
            var3 = true;
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   public boolean isVisible() {
      return this.isVisible;
   }

   // $FF: synthetic method
   public void lambda$closePhoto$3$SecretMediaViewer(PhotoViewer.PlaceProviderObject var1) {
      this.imageMoveAnimation = null;
      this.photoAnimationInProgress = 0;
      if (VERSION.SDK_INT >= 18) {
         this.containerView.setLayerType(0, (Paint)null);
      }

      this.containerView.setVisibility(4);
      this.onPhotoClosed(var1);
   }

   // $FF: synthetic method
   public void lambda$closePhoto$4$SecretMediaViewer(PhotoViewer.PlaceProviderObject var1) {
      SecretMediaViewer.FrameLayoutDrawer var2 = this.containerView;
      if (var2 != null) {
         if (VERSION.SDK_INT >= 18) {
            var2.setLayerType(0, (Paint)null);
         }

         this.containerView.setVisibility(4);
         this.photoAnimationInProgress = 0;
         this.onPhotoClosed(var1);
         this.containerView.setScaleX(1.0F);
         this.containerView.setScaleY(1.0F);
      }
   }

   // $FF: synthetic method
   public void lambda$onPhotoClosed$5$SecretMediaViewer() {
      ImageReceiver.BitmapHolder var1 = this.currentThumb;
      if (var1 != null) {
         var1.release();
         this.currentThumb = null;
      }

      this.centerImage.setImageBitmap((Bitmap)null);

      try {
         if (this.windowView.getParent() != null) {
            ((WindowManager)this.parentActivity.getSystemService("window")).removeView(this.windowView);
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      this.isPhotoVisible = false;
   }

   // $FF: synthetic method
   public void lambda$openMedia$1$SecretMediaViewer() {
      this.photoAnimationInProgress = 0;
      this.imageMoveAnimation = null;
      SecretMediaViewer.FrameLayoutDrawer var1 = this.containerView;
      if (var1 != null) {
         if (VERSION.SDK_INT >= 18) {
            var1.setLayerType(0, (Paint)null);
         }

         this.containerView.invalidate();
      }
   }

   // $FF: synthetic method
   public void lambda$openMedia$2$SecretMediaViewer(PhotoViewer.PlaceProviderObject var1) {
      this.disableShowCheck = false;
      var1.imageReceiver.setVisible(false, true);
   }

   // $FF: synthetic method
   public WindowInsets lambda$setParentActivity$0$SecretMediaViewer(View var1, WindowInsets var2) {
      WindowInsets var3 = (WindowInsets)this.lastInsets;
      this.lastInsets = var2;
      if (var3 == null || !var3.toString().equals(var2.toString())) {
         this.windowView.requestLayout();
      }

      return var2.consumeSystemWindowInsets();
   }

   public boolean onDoubleTap(MotionEvent var1) {
      float var2 = this.scale;
      boolean var3 = false;
      if (var2 == 1.0F && (this.translationY != 0.0F || this.translationX != 0.0F)) {
         return false;
      } else {
         boolean var4 = var3;
         if (this.animationStartTime == 0L) {
            if (this.photoAnimationInProgress != 0) {
               var4 = var3;
            } else {
               var2 = this.scale;
               var4 = true;
               if (var2 == 1.0F) {
                  var2 = var1.getX() - (float)(this.getContainerViewWidth() / 2) - (var1.getX() - (float)(this.getContainerViewWidth() / 2) - this.translationX) * (3.0F / this.scale);
                  float var5 = var1.getY() - (float)(this.getContainerViewHeight() / 2) - (var1.getY() - (float)(this.getContainerViewHeight() / 2) - this.translationY) * (3.0F / this.scale);
                  this.updateMinMax(3.0F);
                  float var6 = this.minX;
                  if (var2 < var6) {
                     var2 = var6;
                  } else {
                     var6 = this.maxX;
                     if (var2 > var6) {
                        var2 = var6;
                     }
                  }

                  var6 = this.minY;
                  if (var5 < var6) {
                     var5 = var6;
                  } else {
                     var6 = this.maxY;
                     if (var5 > var6) {
                        var5 = var6;
                     }
                  }

                  this.animateTo(3.0F, var2, var5, true);
               } else {
                  this.animateTo(1.0F, 0.0F, 0.0F, true);
               }

               this.doubleTap = true;
            }
         }

         return var4;
      }
   }

   public boolean onDoubleTapEvent(MotionEvent var1) {
      return false;
   }

   public boolean onDown(MotionEvent var1) {
      return false;
   }

   public boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      if (this.scale != 1.0F) {
         this.scroller.abortAnimation();
         this.scroller.fling(Math.round(this.translationX), Math.round(this.translationY), Math.round(var3), Math.round(var4), (int)this.minX, (int)this.maxX, (int)this.minY, (int)this.maxY);
         this.containerView.postInvalidate();
      }

      return false;
   }

   public void onLongPress(MotionEvent var1) {
   }

   public boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4) {
      return false;
   }

   public void onShowPress(MotionEvent var1) {
   }

   public boolean onSingleTapConfirmed(MotionEvent var1) {
      if (this.discardTap) {
         return false;
      } else {
         this.toggleActionBar(this.isActionBarVisible ^ true, true);
         return true;
      }
   }

   public boolean onSingleTapUp(MotionEvent var1) {
      return false;
   }

   public void openMedia(MessageObject var1, PhotoViewer.PhotoViewerProvider var2) {
      if (this.parentActivity != null && var1 != null && var1.needDrawBluredPreview() && var2 != null) {
         PhotoViewer.PlaceProviderObject var3 = var2.getPlaceForPhoto(var1, (TLRPC.FileLocation)null, 0, true);
         if (var3 == null) {
            return;
         }

         this.currentProvider = var2;
         this.openTime = System.currentTimeMillis();
         this.closeTime = 0L;
         this.isActionBarVisible = true;
         this.isPhotoVisible = true;
         this.draggingDown = false;
         AspectRatioFrameLayout var22 = this.aspectRatioFrameLayout;
         if (var22 != null) {
            var22.setVisibility(4);
         }

         this.releasePlayer();
         this.pinchStartDistance = 0.0F;
         this.pinchStartScale = 1.0F;
         this.pinchCenterX = 0.0F;
         this.pinchCenterY = 0.0F;
         this.pinchStartX = 0.0F;
         this.pinchStartY = 0.0F;
         this.moveStartX = 0.0F;
         this.moveStartY = 0.0F;
         this.zooming = false;
         this.moving = false;
         this.doubleTap = false;
         this.invalidCoords = false;
         this.canDragDown = true;
         this.updateMinMax(this.scale);
         this.photoBackgroundDrawable.setAlpha(0);
         this.containerView.setAlpha(1.0F);
         this.containerView.setVisibility(0);
         this.secretDeleteTimer.setAlpha(1.0F);
         this.isVideo = false;
         this.videoWatchedOneTime = false;
         this.closeVideoAfterWatch = false;
         this.disableShowCheck = true;
         this.centerImage.setManualAlphaAnimator(false);
         RectF var23 = var3.imageReceiver.getDrawRegion();
         float var4 = var23.width();
         float var5 = var23.height();
         Point var6 = AndroidUtilities.displaySize;
         int var7 = var6.x;
         int var8 = var6.y;
         int var9;
         if (VERSION.SDK_INT >= 21) {
            var9 = AndroidUtilities.statusBarHeight;
         } else {
            var9 = 0;
         }

         var9 += var8;
         this.scale = Math.max(var4 / (float)var7, var5 / (float)var9);
         float var10 = (float)var3.viewX;
         float var11 = var23.left;
         this.translationX = var10 + var11 + var4 / 2.0F - (float)(var7 / 2);
         this.translationY = (float)var3.viewY + var23.top + var5 / 2.0F - (float)(var9 / 2);
         this.clipHorizontal = Math.abs(var11 - (float)var3.imageReceiver.getImageX());
         var7 = (int)Math.abs(var23.top - (float)var3.imageReceiver.getImageY());
         int[] var30 = new int[2];
         var3.parentView.getLocationInWindow(var30);
         var8 = var30[1];
         if (VERSION.SDK_INT >= 21) {
            var9 = 0;
         } else {
            var9 = AndroidUtilities.statusBarHeight;
         }

         this.clipTop = (float)(var8 - var9) - ((float)var3.viewY + var23.top) + (float)var3.clipTopAddition;
         if (this.clipTop < 0.0F) {
            this.clipTop = 0.0F;
         }

         var4 = (float)var3.viewY;
         var10 = var23.top;
         var5 = (float)((int)var5);
         int var12 = var30[1];
         var8 = var3.parentView.getHeight();
         if (VERSION.SDK_INT >= 21) {
            var9 = 0;
         } else {
            var9 = AndroidUtilities.statusBarHeight;
         }

         this.clipBottom = var4 + var10 + var5 - (float)(var12 + var8 - var9) + (float)var3.clipBottomAddition;
         if (this.clipBottom < 0.0F) {
            this.clipBottom = 0.0F;
         }

         var5 = this.clipTop;
         var4 = (float)var7;
         this.clipTop = Math.max(var5, var4);
         this.clipBottom = Math.max(this.clipBottom, var4);
         this.animationStartTime = System.currentTimeMillis();
         this.animateToX = 0.0F;
         this.animateToY = 0.0F;
         this.animateToClipBottom = 0.0F;
         this.animateToClipHorizontal = 0.0F;
         this.animateToClipTop = 0.0F;
         this.animateToScale = 1.0F;
         this.zoomAnimation = true;
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateMessageMedia);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didCreatedNewDeleteTask);
         TLRPC.Peer var24 = var1.messageOwner.to_id;
         if (var24 != null) {
            var9 = var24.channel_id;
         } else {
            var9 = 0;
         }

         this.currentChannelId = var9;
         this.toggleActionBar(true, false);
         this.currentMessageObject = var1;
         TLRPC.Document var25 = var1.getDocument();
         ImageReceiver.BitmapHolder var31 = this.currentThumb;
         if (var31 != null) {
            var31.release();
            this.currentThumb = null;
         }

         this.currentThumb = var3.imageReceiver.getThumbBitmapSafe();
         ImageLocation var13;
         TLRPC.Message var21;
         ImageReceiver.BitmapHolder var26;
         BitmapDrawable var27;
         SecretMediaViewer.SecretDeleteTimer var28;
         ImageReceiver var32;
         if (var25 != null) {
            if (MessageObject.isGifDocument(var25)) {
               this.actionBar.setTitle(LocaleController.getString("DisappearingGif", 2131559270));
               var32 = this.centerImage;
               var13 = ImageLocation.getForDocument(var25);
               var26 = this.currentThumb;
               if (var26 != null) {
                  var27 = new BitmapDrawable(var26.bitmap);
               } else {
                  var27 = null;
               }

               var32.setImage(var13, (String)null, var27, -1, (String)null, var1, 1);
               var28 = this.secretDeleteTimer;
               var21 = var1.messageOwner;
               var28.setDestroyTime((long)var21.destroyTime * 1000L, (long)var21.ttl, false);
            } else {
               this.playerRetryPlayCount = 1;
               this.actionBar.setTitle(LocaleController.getString("DisappearingVideo", 2131559272));
               File var29 = new File(var1.messageOwner.attachPath);
               if (var29.exists()) {
                  this.preparePlayer(var29);
               } else {
                  var29 = FileLoader.getPathToMessage(var1.messageOwner);
                  StringBuilder var33 = new StringBuilder();
                  var33.append(var29.getAbsolutePath());
                  var33.append(".enc");
                  File var34 = new File(var33.toString());
                  if (var34.exists()) {
                     var29 = var34;
                  }

                  this.preparePlayer(var29);
               }

               this.isVideo = true;
               var32 = this.centerImage;
               var26 = this.currentThumb;
               if (var26 != null) {
                  var27 = new BitmapDrawable(var26.bitmap);
               } else {
                  var27 = null;
               }

               var32.setImage((ImageLocation)null, (String)null, var27, -1, (String)null, var1, 2);
               long var14 = (long)var1.messageOwner.destroyTime;
               long var16 = System.currentTimeMillis();
               long var18 = (long)(ConnectionsManager.getInstance(this.currentAccount).getTimeDifference() * 1000);
               if ((long)(var1.getDuration() * 1000) > var14 * 1000L - (var16 + var18)) {
                  this.secretDeleteTimer.setDestroyTime(-1L, -1L, true);
               } else {
                  var28 = this.secretDeleteTimer;
                  var21 = var1.messageOwner;
                  var28.setDestroyTime((long)var21.destroyTime * 1000L, (long)var21.ttl, false);
               }
            }
         } else {
            this.actionBar.setTitle(LocaleController.getString("DisappearingPhoto", 2131559271));
            TLRPC.PhotoSize var35 = FileLoader.getClosestPhotoSizeWithSize(var1.photoThumbs, AndroidUtilities.getPhotoSize());
            var32 = this.centerImage;
            var13 = ImageLocation.getForObject(var35, var1.photoThumbsObject);
            var26 = this.currentThumb;
            if (var26 != null) {
               var27 = new BitmapDrawable(var26.bitmap);
            } else {
               var27 = null;
            }

            var32.setImage(var13, (String)null, var27, -1, (String)null, var1, 2);
            var28 = this.secretDeleteTimer;
            var21 = var1.messageOwner;
            var28.setDestroyTime((long)var21.destroyTime * 1000L, (long)var21.ttl, false);
         }

         try {
            if (this.windowView.getParent() != null) {
               ((WindowManager)this.parentActivity.getSystemService("window")).removeView(this.windowView);
            }
         } catch (Exception var20) {
            FileLog.e((Throwable)var20);
         }

         ((WindowManager)this.parentActivity.getSystemService("window")).addView(this.windowView, this.windowLayoutParams);
         this.secretDeleteTimer.invalidate();
         this.isVisible = true;
         this.imageMoveAnimation = new AnimatorSet();
         this.imageMoveAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.actionBar, "alpha", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this.secretDeleteTimer, "alpha", new float[]{0.0F, 1.0F}), ObjectAnimator.ofInt(this.photoBackgroundDrawable, "alpha", new int[]{0, 255}), ObjectAnimator.ofFloat(this.secretDeleteTimer, "alpha", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(this, "animationValue", new float[]{0.0F, 1.0F})});
         this.photoAnimationInProgress = 3;
         this.photoAnimationEndRunnable = new _$$Lambda$SecretMediaViewer$KK7Q_lwTroM_vlzg7v5qjBIm368(this);
         this.imageMoveAnimation.setDuration(250L);
         this.imageMoveAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                  SecretMediaViewer.this.photoAnimationEndRunnable.run();
                  SecretMediaViewer.this.photoAnimationEndRunnable = null;
               }

            }
         });
         this.photoTransitionAnimationStartTime = System.currentTimeMillis();
         if (VERSION.SDK_INT >= 18) {
            this.containerView.setLayerType(2, (Paint)null);
         }

         this.imageMoveAnimation.setInterpolator(new DecelerateInterpolator());
         this.photoBackgroundDrawable.frame = 0;
         this.photoBackgroundDrawable.drawRunnable = new _$$Lambda$SecretMediaViewer$_WXiS_iPl6V2WQvBv2qBotC4RRs(this, var3);
         this.imageMoveAnimation.start();
      }

   }

   @Keep
   public void setAnimationValue(float var1) {
      this.animationValue = var1;
      this.containerView.invalidate();
   }

   public void setParentActivity(Activity var1) {
      this.currentAccount = UserConfig.selectedAccount;
      this.centerImage.setCurrentAccount(this.currentAccount);
      if (this.parentActivity != var1) {
         this.parentActivity = var1;
         this.scroller = new Scroller(var1);
         this.windowView = new FrameLayout(var1) {
            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               if (VERSION.SDK_INT >= 21 && SecretMediaViewer.this.lastInsets != null) {
                  var2 = ((WindowInsets)SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft() + 0;
               } else {
                  var2 = 0;
               }

               SecretMediaViewer.this.containerView.layout(var2, 0, SecretMediaViewer.this.containerView.getMeasuredWidth() + var2, SecretMediaViewer.this.containerView.getMeasuredHeight());
               if (var1) {
                  if (SecretMediaViewer.this.imageMoveAnimation == null) {
                     SecretMediaViewer.this.scale = 1.0F;
                     SecretMediaViewer.this.translationX = 0.0F;
                     SecretMediaViewer.this.translationY = 0.0F;
                  }

                  SecretMediaViewer var6 = SecretMediaViewer.this;
                  var6.updateMinMax(var6.scale);
               }

            }

            protected void onMeasure(int var1, int var2) {
               int var3 = MeasureSpec.getSize(var1);
               var1 = MeasureSpec.getSize(var2);
               int var5;
               if (VERSION.SDK_INT >= 21 && SecretMediaViewer.this.lastInsets != null) {
                  WindowInsets var4 = (WindowInsets)SecretMediaViewer.this.lastInsets;
                  var2 = var1;
                  if (AndroidUtilities.incorrectDisplaySizeFix) {
                     var5 = AndroidUtilities.displaySize.y;
                     var2 = var1;
                     if (var1 > var5) {
                        var2 = var5;
                     }

                     var2 += AndroidUtilities.statusBarHeight;
                  }

                  var2 -= var4.getSystemWindowInsetBottom();
                  var5 = var3 - var4.getSystemWindowInsetRight();
               } else {
                  int var6 = AndroidUtilities.displaySize.y;
                  var5 = var3;
                  var2 = var1;
                  if (var1 > var6) {
                     var2 = var6;
                     var5 = var3;
                  }
               }

               this.setMeasuredDimension(var5, var2);
               var1 = var5;
               if (VERSION.SDK_INT >= 21) {
                  var1 = var5;
                  if (SecretMediaViewer.this.lastInsets != null) {
                     var1 = var5 - ((WindowInsets)SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft();
                  }
               }

               SecretMediaViewer.this.containerView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
            }
         };
         this.windowView.setBackgroundDrawable(this.photoBackgroundDrawable);
         this.windowView.setFocusable(true);
         this.windowView.setFocusableInTouchMode(true);
         this.containerView = new SecretMediaViewer.FrameLayoutDrawer(var1) {
            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               super.onLayout(var1, var2, var3, var4, var5);
               if (SecretMediaViewer.this.secretDeleteTimer != null) {
                  var3 = (ActionBar.getCurrentActionBarHeight() - SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight()) / 2;
                  if (VERSION.SDK_INT >= 21) {
                     var2 = AndroidUtilities.statusBarHeight;
                  } else {
                     var2 = 0;
                  }

                  var2 += var3;
                  SecretMediaViewer.this.secretDeleteTimer.layout(SecretMediaViewer.this.secretDeleteTimer.getLeft(), var2, SecretMediaViewer.this.secretDeleteTimer.getRight(), SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight() + var2);
               }

            }
         };
         this.containerView.setFocusable(false);
         this.windowView.addView(this.containerView);
         android.widget.FrameLayout.LayoutParams var2 = (android.widget.FrameLayout.LayoutParams)this.containerView.getLayoutParams();
         var2.width = -1;
         var2.height = -1;
         var2.gravity = 51;
         this.containerView.setLayoutParams(var2);
         if (VERSION.SDK_INT >= 21) {
            this.containerView.setFitsSystemWindows(true);
            this.containerView.setOnApplyWindowInsetsListener(new _$$Lambda$SecretMediaViewer$NFiT5K5Ywrc0uS0Pq28kGtaBHeE(this));
            this.containerView.setSystemUiVisibility(1280);
         }

         this.gestureDetector = new GestureDetector(this.containerView.getContext(), this);
         this.gestureDetector.setOnDoubleTapListener(this);
         this.actionBar = new ActionBar(var1);
         this.actionBar.setTitleColor(-1);
         this.actionBar.setSubtitleColor(-1);
         this.actionBar.setBackgroundColor(2130706432);
         ActionBar var5 = this.actionBar;
         boolean var3;
         if (VERSION.SDK_INT >= 21) {
            var3 = true;
         } else {
            var3 = false;
         }

         var5.setOccupyStatusBar(var3);
         this.actionBar.setItemsBackgroundColor(1090519039, false);
         this.actionBar.setBackButtonImage(2131165409);
         this.actionBar.setTitleRightMargin(AndroidUtilities.dp(70.0F));
         this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0F));
         this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            public void onItemClick(int var1) {
               if (var1 == -1) {
                  SecretMediaViewer.this.closePhoto(true, false);
               }

            }
         });
         this.secretDeleteTimer = new SecretMediaViewer.SecretDeleteTimer(var1);
         this.containerView.addView(this.secretDeleteTimer, LayoutHelper.createFrame(119, 48.0F, 53, 0.0F, 0.0F, 0.0F, 0.0F));
         this.windowLayoutParams = new LayoutParams();
         LayoutParams var4 = this.windowLayoutParams;
         var4.height = -1;
         var4.format = -3;
         var4.width = -1;
         var4.gravity = 48;
         var4.type = 99;
         if (VERSION.SDK_INT >= 21) {
            var4.flags = -2147417848;
         } else {
            var4.flags = 8;
         }

         var4 = this.windowLayoutParams;
         var4.flags |= 8192;
         this.centerImage.setParentView(this.containerView);
         this.centerImage.setForceCrossfade(true);
      }
   }

   @Keep
   public void setVideoCrossfadeAlpha(float var1) {
      this.videoCrossfadeAlpha = var1;
      this.containerView.invalidate();
   }

   private class FrameLayoutDrawer extends FrameLayout {
      public FrameLayoutDrawer(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
      }

      protected boolean drawChild(Canvas var1, View var2, long var3) {
         boolean var5;
         if (var2 != SecretMediaViewer.this.aspectRatioFrameLayout && super.drawChild(var1, var2, var3)) {
            var5 = true;
         } else {
            var5 = false;
         }

         return var5;
      }

      protected void onDraw(Canvas var1) {
         SecretMediaViewer.this.onDraw(var1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         SecretMediaViewer.this.processTouchEvent(var1);
         return true;
      }
   }

   private class PhotoBackgroundDrawable extends ColorDrawable {
      private Runnable drawRunnable;
      private int frame;

      public PhotoBackgroundDrawable(int var2) {
         super(var2);
      }

      public void draw(Canvas var1) {
         super.draw(var1);
         if (this.getAlpha() != 0) {
            label14: {
               if (this.frame == 2) {
                  Runnable var2 = this.drawRunnable;
                  if (var2 != null) {
                     var2.run();
                     this.drawRunnable = null;
                     break label14;
                  }
               }

               this.invalidateSelf();
            }

            ++this.frame;
         }

      }

      @Keep
      public void setAlpha(int var1) {
         if (SecretMediaViewer.this.parentActivity instanceof LaunchActivity) {
            DrawerLayoutContainer var2 = ((LaunchActivity)SecretMediaViewer.this.parentActivity).drawerLayoutContainer;
            boolean var3;
            if (SecretMediaViewer.this.isPhotoVisible && var1 == 255) {
               var3 = false;
            } else {
               var3 = true;
            }

            var2.setAllowDrawContent(var3);
         }

         super.setAlpha(var1);
      }
   }

   private class SecretDeleteTimer extends FrameLayout {
      private Paint afterDeleteProgressPaint;
      private Paint circlePaint;
      private Paint deleteProgressPaint;
      private RectF deleteProgressRect = new RectF();
      private long destroyTime;
      private long destroyTtl;
      private Drawable drawable;
      private ArrayList freeParticles = new ArrayList();
      private long lastAnimationTime;
      private Paint particlePaint;
      private ArrayList particles = new ArrayList();
      private boolean useVideoProgress;

      public SecretDeleteTimer(Context var2) {
         super(var2);
         int var3 = 0;
         this.setWillNotDraw(false);
         this.particlePaint = new Paint(1);
         this.particlePaint.setStrokeWidth((float)AndroidUtilities.dp(1.5F));
         this.particlePaint.setColor(-1644826);
         this.particlePaint.setStrokeCap(Cap.ROUND);
         this.particlePaint.setStyle(Style.STROKE);
         this.deleteProgressPaint = new Paint(1);
         this.deleteProgressPaint.setColor(-1644826);
         this.afterDeleteProgressPaint = new Paint(1);
         this.afterDeleteProgressPaint.setStyle(Style.STROKE);
         this.afterDeleteProgressPaint.setStrokeCap(Cap.ROUND);
         this.afterDeleteProgressPaint.setColor(-1644826);
         this.afterDeleteProgressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
         this.circlePaint = new Paint(1);
         this.circlePaint.setColor(2130706432);

         for(this.drawable = var2.getResources().getDrawable(2131165379); var3 < 40; ++var3) {
            this.freeParticles.add(new SecretMediaViewer.SecretDeleteTimer.Particle());
         }

      }

      private void setDestroyTime(long var1, long var3, boolean var5) {
         this.destroyTime = var1;
         this.destroyTtl = var3;
         this.useVideoProgress = var5;
         this.lastAnimationTime = System.currentTimeMillis();
         this.invalidate();
      }

      private void updateParticles(long var1) {
         int var3 = this.particles.size();

         for(int var4 = 0; var4 < var3; ++var4) {
            SecretMediaViewer.SecretDeleteTimer.Particle var5 = (SecretMediaViewer.SecretDeleteTimer.Particle)this.particles.get(var4);
            float var6 = var5.currentTime;
            float var7 = var5.lifeTime;
            if (var6 >= var7) {
               if (this.freeParticles.size() < 40) {
                  this.freeParticles.add(var5);
               }

               this.particles.remove(var4);
               --var4;
               --var3;
            } else {
               var5.alpha = 1.0F - AndroidUtilities.decelerateInterpolator.getInterpolation(var6 / var7);
               float var8 = var5.x;
               var6 = var5.vx;
               var7 = var5.velocity;
               float var9 = (float)var1;
               var5.x = var8 + var6 * var7 * var9 / 500.0F;
               var5.y += var5.vy * var7 * var9 / 500.0F;
               var5.currentTime += var9;
            }
         }

      }

      @SuppressLint({"DrawAllocation"})
      protected void onDraw(Canvas var1) {
         if (SecretMediaViewer.this.currentMessageObject != null && SecretMediaViewer.this.currentMessageObject.messageOwner.destroyTime != 0) {
            var1.drawCircle((float)(this.getMeasuredWidth() - AndroidUtilities.dp(35.0F)), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(16.0F), this.circlePaint);
            long var2;
            long var4;
            float var6;
            if (this.useVideoProgress) {
               label47: {
                  if (SecretMediaViewer.this.videoPlayer != null) {
                     var2 = SecretMediaViewer.this.videoPlayer.getDuration();
                     var4 = SecretMediaViewer.this.videoPlayer.getCurrentPosition();
                     if (var2 != -9223372036854775807L && var4 != -9223372036854775807L) {
                        var6 = 1.0F - (float)var4 / (float)var2;
                        break label47;
                     }
                  }

                  var6 = 1.0F;
               }
            } else {
               var2 = System.currentTimeMillis();
               var4 = (long)(ConnectionsManager.getInstance(SecretMediaViewer.this.currentAccount).getTimeDifference() * 1000);
               var6 = (float)Math.max(0L, this.destroyTime - (var2 + var4)) / ((float)this.destroyTtl * 1000.0F);
            }

            int var7 = this.getMeasuredWidth() - AndroidUtilities.dp(40.0F);
            int var8 = (this.getMeasuredHeight() - AndroidUtilities.dp(14.0F)) / 2 - AndroidUtilities.dp(0.5F);
            this.drawable.setBounds(var7, var8, AndroidUtilities.dp(10.0F) + var7, AndroidUtilities.dp(14.0F) + var8);
            this.drawable.draw(var1);
            var6 *= -360.0F;
            var1.drawArc(this.deleteProgressRect, -90.0F, var6, false, this.afterDeleteProgressPaint);
            var7 = this.particles.size();

            for(var8 = 0; var8 < var7; ++var8) {
               SecretMediaViewer.SecretDeleteTimer.Particle var9 = (SecretMediaViewer.SecretDeleteTimer.Particle)this.particles.get(var8);
               this.particlePaint.setAlpha((int)(var9.alpha * 255.0F));
               var1.drawPoint(var9.x, var9.y, this.particlePaint);
            }

            double var10 = (double)(var6 - 90.0F);
            Double.isNaN(var10);
            var10 *= 0.017453292519943295D;
            double var12 = Math.sin(var10);
            double var14 = -Math.cos(var10);
            var8 = AndroidUtilities.dp(14.0F);
            double var16 = -var14;
            var10 = (double)var8;
            Double.isNaN(var10);
            double var18 = (double)this.deleteProgressRect.centerX();
            Double.isNaN(var18);
            var6 = (float)(var16 * var10 + var18);
            Double.isNaN(var10);
            var16 = (double)this.deleteProgressRect.centerY();
            Double.isNaN(var16);
            float var20 = (float)(var10 * var12 + var16);

            for(var8 = 0; var8 < 1; ++var8) {
               SecretMediaViewer.SecretDeleteTimer.Particle var21;
               if (!this.freeParticles.isEmpty()) {
                  var21 = (SecretMediaViewer.SecretDeleteTimer.Particle)this.freeParticles.get(0);
                  this.freeParticles.remove(0);
               } else {
                  var21 = new SecretMediaViewer.SecretDeleteTimer.Particle();
               }

               var21.x = var6;
               var21.y = var20;
               var10 = (double)(Utilities.random.nextInt(140) - 70);
               Double.isNaN(var10);
               var16 = var10 * 0.017453292519943295D;
               var10 = var16;
               if (var16 < 0.0D) {
                  var10 = var16 + 6.283185307179586D;
               }

               var21.vx = (float)(Math.cos(var10) * var12 - Math.sin(var10) * var14);
               var21.vy = (float)(Math.sin(var10) * var12 + Math.cos(var10) * var14);
               var21.alpha = 1.0F;
               var21.currentTime = 0.0F;
               var21.lifeTime = (float)(Utilities.random.nextInt(100) + 400);
               var21.velocity = Utilities.random.nextFloat() * 4.0F + 20.0F;
               this.particles.add(var21);
            }

            var4 = System.currentTimeMillis();
            this.updateParticles(var4 - this.lastAnimationTime);
            this.lastAnimationTime = var4;
            this.invalidate();
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, var2);
         var1 = this.getMeasuredHeight() / 2 - AndroidUtilities.dp(28.0F) / 2;
         this.deleteProgressRect.set((float)(this.getMeasuredWidth() - AndroidUtilities.dp(49.0F)), (float)var1, (float)(this.getMeasuredWidth() - AndroidUtilities.dp(21.0F)), (float)(var1 + AndroidUtilities.dp(28.0F)));
      }

      private class Particle {
         float alpha;
         float currentTime;
         float lifeTime;
         float velocity;
         float vx;
         float vy;
         float x;
         float y;

         private Particle() {
         }

         // $FF: synthetic method
         Particle(Object var2) {
            this();
         }
      }
   }
}
