package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.EGLExt;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.TextureView.SurfaceTextureListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.annotation.Keep;
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
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.camera.CameraController;
import org.telegram.messenger.camera.CameraInfo;
import org.telegram.messenger.camera.CameraSession;
import org.telegram.messenger.video.MP4Builder;
import org.telegram.messenger.video.Mp4Movie;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.ActionBar.Theme;

@TargetApi(18)
public class InstantCameraView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
   private static final String FRAGMENT_SCREEN_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision lowp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
   private static final String FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float scaleX;\nuniform float scaleY;\nuniform float alpha;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec2 coord = vec2((vTextureCoord.x - 0.5) * scaleX, (vTextureCoord.y - 0.5) * scaleY);\n   float coef = ceil(clamp(0.2601 - dot(coord, coord), 0.0, 1.0));\n   vec3 color = texture2D(sTexture, vTextureCoord).rgb * coef + (1.0 - step(0.001, coef));\n   gl_FragColor = vec4(color * alpha, alpha);\n}\n";
   private static final int MSG_AUDIOFRAME_AVAILABLE = 3;
   private static final int MSG_START_RECORDING = 0;
   private static final int MSG_STOP_RECORDING = 1;
   private static final int MSG_VIDEOFRAME_AVAILABLE = 2;
   private static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
   private AnimatorSet animatorSet;
   private org.telegram.messenger.camera.Size aspectRatio;
   private ChatActivity baseFragment;
   private FrameLayout cameraContainer;
   private File cameraFile;
   private volatile boolean cameraReady;
   private CameraSession cameraSession;
   private int[] cameraTexture;
   private float cameraTextureAlpha;
   private InstantCameraView.CameraGLThread cameraThread;
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
   private org.telegram.messenger.camera.Size pictureSize;
   private int[] position;
   private org.telegram.messenger.camera.Size previewSize;
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

   public InstantCameraView(Context var1, ChatActivity var2) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.isFrontface = true;
      this.position = new int[2];
      this.cameraTexture = new int[1];
      this.oldCameraTexture = new int[1];
      this.cameraTextureAlpha = 1.0F;
      this.timerRunnable = new Runnable() {
         public void run() {
            if (InstantCameraView.this.recording) {
               NotificationCenter var1 = NotificationCenter.getInstance(InstantCameraView.this.currentAccount);
               int var2 = NotificationCenter.recordProgressChanged;
               InstantCameraView var3 = InstantCameraView.this;
               long var4 = System.currentTimeMillis() - InstantCameraView.this.recordStartTime;
               var3.duration = var4;
               var1.postNotificationName(var2, var4, 0.0D);
               AndroidUtilities.runOnUIThread(InstantCameraView.this.timerRunnable, 50L);
            }
         }
      };
      org.telegram.messenger.camera.Size var3;
      if (SharedConfig.roundCamera16to9) {
         var3 = new org.telegram.messenger.camera.Size(16, 9);
      } else {
         var3 = new org.telegram.messenger.camera.Size(4, 3);
      }

      this.aspectRatio = var3;
      this.mMVPMatrix = new float[16];
      this.mSTMatrix = new float[16];
      this.moldSTMatrix = new float[16];
      this.setOnTouchListener(new _$$Lambda$InstantCameraView$qN9GPrIe8LvogWTbvas_MPqE9F8(this));
      this.setWillNotDraw(false);
      this.setBackgroundColor(-1073741824);
      this.baseFragment = var2;
      boolean var4;
      if (this.baseFragment.getCurrentEncryptedChat() != null) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.isSecretChat = var4;
      this.paint = new Paint(1) {
         public void setAlpha(int var1) {
            super.setAlpha(var1);
            InstantCameraView.this.invalidate();
         }
      };
      this.paint.setStyle(Style.STROKE);
      this.paint.setStrokeCap(Cap.ROUND);
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(3.0F));
      this.paint.setColor(-1);
      this.rect = new RectF();
      if (VERSION.SDK_INT >= 21) {
         this.cameraContainer = new FrameLayout(var1) {
            public void setAlpha(float var1) {
               super.setAlpha(var1);
               InstantCameraView.this.invalidate();
            }

            public void setScaleX(float var1) {
               super.setScaleX(var1);
               InstantCameraView.this.invalidate();
            }
         };
         this.cameraContainer.setOutlineProvider(new ViewOutlineProvider() {
            @TargetApi(21)
            public void getOutline(View var1, Outline var2) {
               int var3 = AndroidUtilities.roundMessageSize;
               var2.setOval(0, 0, var3, var3);
            }
         });
         this.cameraContainer.setClipToOutline(true);
         this.cameraContainer.setWillNotDraw(false);
      } else {
         final Path var7 = new Path();
         final Paint var9 = new Paint(1);
         var9.setColor(-16777216);
         var9.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
         this.cameraContainer = new FrameLayout(var1) {
            protected void dispatchDraw(Canvas var1) {
               try {
                  super.dispatchDraw(var1);
                  var1.drawPath(var7, var9);
               } catch (Exception var2) {
               }

            }

            protected void onSizeChanged(int var1, int var2, int var3, int var4) {
               super.onSizeChanged(var1, var2, var3, var4);
               var7.reset();
               Path var5 = var7;
               float var6 = (float)(var1 / 2);
               var5.addCircle(var6, (float)(var2 / 2), var6, Direction.CW);
               var7.toggleInverseFillType();
            }

            public void setScaleX(float var1) {
               super.setScaleX(var1);
               InstantCameraView.this.invalidate();
            }
         };
         this.cameraContainer.setWillNotDraw(false);
         this.cameraContainer.setLayerType(2, (Paint)null);
      }

      FrameLayout var8 = this.cameraContainer;
      int var5 = AndroidUtilities.roundMessageSize;
      this.addView(var8, new LayoutParams(var5, var5, 17));
      this.switchCameraButton = new ImageView(var1);
      this.switchCameraButton.setScaleType(ScaleType.CENTER);
      this.switchCameraButton.setContentDescription(LocaleController.getString("AccDescrSwitchCamera", 2131558478));
      this.addView(this.switchCameraButton, LayoutHelper.createFrame(48, 48.0F, 83, 20.0F, 0.0F, 0.0F, 14.0F));
      this.switchCameraButton.setOnClickListener(new _$$Lambda$InstantCameraView$S2zQmSsGveVOM2kGAgGGE27kbGY(this));
      this.muteImageView = new ImageView(var1);
      this.muteImageView.setScaleType(ScaleType.CENTER);
      this.muteImageView.setImageResource(2131165906);
      this.muteImageView.setAlpha(0.0F);
      this.addView(this.muteImageView, LayoutHelper.createFrame(48, 48, 17));
      ((LayoutParams)this.muteImageView.getLayoutParams()).topMargin = AndroidUtilities.roundMessageSize / 2 - AndroidUtilities.dp(24.0F);
      this.textureOverlayView = new BackupImageView(this.getContext());
      this.textureOverlayView.setRoundRadius(AndroidUtilities.roundMessageSize / 2);
      BackupImageView var6 = this.textureOverlayView;
      var5 = AndroidUtilities.roundMessageSize;
      this.addView(var6, new LayoutParams(var5, var5, 17));
      this.setVisibility(4);
   }

   // $FF: synthetic method
   static boolean access$5000(InstantCameraView var0) {
      return var0.isFrontface;
   }

   // $FF: synthetic method
   static AnimatorSet access$5100(InstantCameraView var0) {
      return var0.muteAnimation;
   }

   // $FF: synthetic method
   static AnimatorSet access$5102(InstantCameraView var0, AnimatorSet var1) {
      var0.muteAnimation = var1;
      return var1;
   }

   private void createCamera(SurfaceTexture var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$RBsoQ3f2_7L3ZL_CYLuRpS20Mko(this, var1));
   }

   private boolean initCamera() {
      ArrayList var1 = CameraController.getInstance().getCameras();
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         CameraInfo var3 = null;
         int var4 = 0;

         CameraInfo var5;
         while(true) {
            var5 = var3;
            if (var4 >= var1.size()) {
               break;
            }

            var5 = (CameraInfo)var1.get(var4);
            if (!var5.isFrontface()) {
               var3 = var5;
            }

            if (this.isFrontface && var5.isFrontface() || !this.isFrontface && !var5.isFrontface()) {
               this.selectedCamera = var5;
               var5 = var3;
               break;
            }

            ++var4;
            var3 = var5;
         }

         if (this.selectedCamera == null) {
            this.selectedCamera = var5;
         }

         var3 = this.selectedCamera;
         if (var3 == null) {
            return false;
         } else {
            ArrayList var13 = var3.getPreviewSizes();
            ArrayList var16 = this.selectedCamera.getPictureSizes();
            this.previewSize = CameraController.chooseOptimalSize(var13, 480, 270, this.aspectRatio);
            this.pictureSize = CameraController.chooseOptimalSize(var16, 480, 270, this.aspectRatio);
            if (this.previewSize.mWidth != this.pictureSize.mWidth) {
               int var6 = var13.size() - 1;
               boolean var14 = var2;

               org.telegram.messenger.camera.Size var7;
               int var8;
               int var10;
               org.telegram.messenger.camera.Size var11;
               while(true) {
                  var2 = var14;
                  if (var6 < 0) {
                     break;
                  }

                  var7 = (org.telegram.messenger.camera.Size)var13.get(var6);
                  var8 = var16.size() - 1;

                  while(true) {
                     var2 = var14;
                     if (var8 < 0) {
                        break;
                     }

                     org.telegram.messenger.camera.Size var9 = (org.telegram.messenger.camera.Size)var16.get(var8);
                     int var12 = var7.mWidth;
                     var11 = this.pictureSize;
                     if (var12 >= var11.mWidth) {
                        var10 = var7.mHeight;
                        if (var10 >= var11.mHeight && var12 == var9.mWidth && var10 == var9.mHeight) {
                           this.previewSize = var7;
                           this.pictureSize = var9;
                           var2 = true;
                           break;
                        }
                     }

                     --var8;
                  }

                  if (var2) {
                     break;
                  }

                  --var6;
                  var14 = var2;
               }

               boolean var17;
               if (!var2) {
                  for(var4 = var13.size() - 1; var4 >= 0; var2 = var17) {
                     var7 = (org.telegram.messenger.camera.Size)var13.get(var4);
                     var6 = var16.size() - 1;

                     while(true) {
                        var17 = var2;
                        if (var6 < 0) {
                           break;
                        }

                        var11 = (org.telegram.messenger.camera.Size)var16.get(var6);
                        var8 = var7.mWidth;
                        if (var8 >= 240) {
                           var10 = var7.mHeight;
                           if (var10 >= 240 && var8 == var11.mWidth && var10 == var11.mHeight) {
                              this.previewSize = var7;
                              this.pictureSize = var11;
                              var17 = true;
                              break;
                           }
                        }

                        --var6;
                     }

                     if (var17) {
                        break;
                     }

                     --var4;
                  }
               }
            }

            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var15 = new StringBuilder();
               var15.append("preview w = ");
               var15.append(this.previewSize.mWidth);
               var15.append(" h = ");
               var15.append(this.previewSize.mHeight);
               FileLog.d(var15.toString());
            }

            return true;
         }
      }
   }

   private int loadShader(int var1, String var2) {
      int var3 = GLES20.glCreateShader(var1);
      GLES20.glShaderSource(var3, var2);
      GLES20.glCompileShader(var3);
      int[] var4 = new int[1];
      GLES20.glGetShaderiv(var3, 35713, var4, 0);
      var1 = var3;
      if (var4[0] == 0) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.e(GLES20.glGetShaderInfoLog(var3));
         }

         GLES20.glDeleteShader(var3);
         var1 = 0;
      }

      return var1;
   }

   private void saveLastCameraBitmap() {
      if (this.textureView.getBitmap() != null) {
         this.lastBitmap = Bitmap.createScaledBitmap(this.textureView.getBitmap(), 80, 80, true);
         Bitmap var1 = this.lastBitmap;
         if (var1 != null) {
            Utilities.blurBitmap(var1, 7, 1, var1.getWidth(), this.lastBitmap.getHeight(), this.lastBitmap.getRowBytes());

            try {
               File var4 = new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg");
               FileOutputStream var2 = new FileOutputStream(var4);
               this.lastBitmap.compress(CompressFormat.JPEG, 87, var2);
            } catch (Throwable var3) {
            }
         }
      }

   }

   private void startProgressTimer() {
      Timer var1 = this.progressTimer;
      if (var1 != null) {
         try {
            var1.cancel();
            this.progressTimer = null;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }
      }

      this.progressTimer = new Timer();
      this.progressTimer.schedule(new TimerTask() {
         // $FF: synthetic method
         public void lambda$run$0$InstantCameraView$10() {
            // $FF: Couldn't be decompiled
         }

         public void run() {
            AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$10$Q79AnmgYDpgQ0WR_lA8rbDjA_6E(this));
         }
      }, 0L, 17L);
   }

   private void stopProgressTimer() {
      Timer var1 = this.progressTimer;
      if (var1 != null) {
         try {
            var1.cancel();
            this.progressTimer = null;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }
      }

   }

   private void switchCamera() {
      this.saveLastCameraBitmap();
      Bitmap var1 = this.lastBitmap;
      if (var1 != null) {
         this.textureOverlayView.setImageBitmap(var1);
         this.textureOverlayView.animate().setDuration(120L).alpha(1.0F).setInterpolator(new DecelerateInterpolator()).start();
      }

      CameraSession var2 = this.cameraSession;
      if (var2 != null) {
         var2.destroy();
         CameraController.getInstance().close(this.cameraSession, (CountDownLatch)null, (Runnable)null);
         this.cameraSession = null;
      }

      this.isFrontface ^= true;
      this.initCamera();
      this.cameraReady = false;
      this.cameraThread.reinitForNewCamera();
   }

   public void cancel() {
      this.stopProgressTimer();
      VideoPlayer var1 = this.videoPlayer;
      if (var1 != null) {
         var1.releasePlayer(true);
         this.videoPlayer = null;
      }

      if (this.textureView != null) {
         this.cancelled = true;
         this.recording = false;
         AndroidUtilities.cancelRunOnUIThread(this.timerRunnable);
         NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.recordStopped, 0);
         if (this.cameraThread != null) {
            this.saveLastCameraBitmap();
            this.cameraThread.shutdown(0);
            this.cameraThread = null;
         }

         File var2 = this.cameraFile;
         if (var2 != null) {
            var2.delete();
            this.cameraFile = null;
         }

         this.startAnimation(false);
      }
   }

   public void changeVideoPreviewState(int var1, float var2) {
      VideoPlayer var3 = this.videoPlayer;
      if (var3 != null) {
         if (var1 == 0) {
            this.startProgressTimer();
            this.videoPlayer.play();
         } else if (var1 == 1) {
            this.stopProgressTimer();
            this.videoPlayer.pause();
         } else if (var1 == 2) {
            var3.seekTo((long)(var2 * (float)var3.getDuration()));
         }

      }
   }

   public void destroy(boolean var1, Runnable var2) {
      CameraSession var3 = this.cameraSession;
      if (var3 != null) {
         var3.destroy();
         CameraController var4 = CameraController.getInstance();
         CameraSession var5 = this.cameraSession;
         CountDownLatch var6;
         if (!var1) {
            var6 = new CountDownLatch(1);
         } else {
            var6 = null;
         }

         var4.close(var5, var6, var2);
      }

   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.recordProgressChanged) {
         long var4 = (Long)var3[0];
         this.progress = (float)var4 / 60000.0F;
         this.recordedTime = var4;
         this.invalidate();
      } else if (var1 == NotificationCenter.FileDidUpload) {
         String var6 = (String)var3[0];
         File var7 = this.cameraFile;
         if (var7 != null && var7.getAbsolutePath().equals(var6)) {
            this.file = (TLRPC.InputFile)var3[1];
            this.encryptedFile = (TLRPC.InputEncryptedFile)var3[2];
            this.size = (Long)var3[5];
            if (this.encryptedFile != null) {
               this.key = (byte[])var3[3];
               this.iv = (byte[])var3[4];
            }
         }
      }

   }

   public FrameLayout getCameraContainer() {
      return this.cameraContainer;
   }

   public Rect getCameraRect() {
      this.cameraContainer.getLocationOnScreen(this.position);
      int[] var1 = this.position;
      return new Rect((float)var1[0], (float)var1[1], (float)this.cameraContainer.getWidth(), (float)this.cameraContainer.getHeight());
   }

   public View getMuteImageView() {
      return this.muteImageView;
   }

   public Paint getPaint() {
      return this.paint;
   }

   public View getSwitchButtonView() {
      return this.switchCameraButton;
   }

   public void hideCamera(boolean var1) {
      this.destroy(var1, (Runnable)null);
      this.cameraContainer.removeView(this.textureView);
      this.cameraContainer.setTranslationX(0.0F);
      this.cameraContainer.setTranslationY(0.0F);
      this.textureOverlayView.setTranslationX(0.0F);
      this.textureOverlayView.setTranslationY(0.0F);
      this.textureView = null;
   }

   // $FF: synthetic method
   public void lambda$createCamera$4$InstantCameraView(SurfaceTexture var1) {
      if (this.cameraThread != null) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("create camera session");
         }

         var1.setDefaultBufferSize(this.previewSize.getWidth(), this.previewSize.getHeight());
         this.cameraSession = new CameraSession(this.selectedCamera, this.previewSize, this.pictureSize, 256);
         this.cameraThread.setCurrentSession(this.cameraSession);
         CameraController.getInstance().openRound(this.cameraSession, var1, new _$$Lambda$InstantCameraView$MN_OUeYmzAWjQZgMOwz5fmkJ_G8(this), new _$$Lambda$InstantCameraView$xL5rY6Uq2g_HX9Vsko6XZnx3fLI(this));
      }
   }

   // $FF: synthetic method
   public boolean lambda$new$0$InstantCameraView(View var1, MotionEvent var2) {
      if (var2.getAction() == 0) {
         ChatActivity var8 = this.baseFragment;
         if (var8 != null) {
            VideoPlayer var10 = this.videoPlayer;
            if (var10 != null) {
               boolean var3 = var10.isMuted() ^ true;
               this.videoPlayer.setMute(var3);
               AnimatorSet var9 = this.muteAnimation;
               if (var9 != null) {
                  var9.cancel();
               }

               this.muteAnimation = new AnimatorSet();
               var9 = this.muteAnimation;
               ImageView var11 = this.muteImageView;
               float var4 = 1.0F;
               float var5;
               if (var3) {
                  var5 = 1.0F;
               } else {
                  var5 = 0.0F;
               }

               ObjectAnimator var12 = ObjectAnimator.ofFloat(var11, "alpha", new float[]{var5});
               ImageView var6 = this.muteImageView;
               if (var3) {
                  var5 = 1.0F;
               } else {
                  var5 = 0.5F;
               }

               ObjectAnimator var13 = ObjectAnimator.ofFloat(var6, "scaleX", new float[]{var5});
               ImageView var7 = this.muteImageView;
               if (var3) {
                  var5 = var4;
               } else {
                  var5 = 0.5F;
               }

               var9.playTogether(new Animator[]{var12, var13, ObjectAnimator.ofFloat(var7, "scaleY", new float[]{var5})});
               this.muteAnimation.addListener(new InstantCameraView$2(this));
               this.muteAnimation.setDuration(180L);
               this.muteAnimation.setInterpolator(new DecelerateInterpolator());
               this.muteAnimation.start();
            } else {
               var8.checkRecordLocked();
            }
         }
      }

      return true;
   }

   // $FF: synthetic method
   public void lambda$new$1$InstantCameraView(View var1) {
      if (this.cameraReady) {
         CameraSession var2 = this.cameraSession;
         if (var2 != null && var2.isInitied() && this.cameraThread != null) {
            this.switchCamera();
            ObjectAnimator var3 = ObjectAnimator.ofFloat(this.switchCameraButton, "scaleX", new float[]{0.0F}).setDuration(100L);
            var3.addListener(new InstantCameraView$7(this));
            var3.start();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$InstantCameraView() {
      if (this.cameraSession != null) {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("camera initied");
         }

         this.cameraSession.setInitied();
      }

   }

   // $FF: synthetic method
   public void lambda$null$3$InstantCameraView() {
      this.cameraThread.setCurrentSession(this.cameraSession);
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

   protected void onDraw(Canvas var1) {
      float var2 = this.cameraContainer.getX();
      float var3 = this.cameraContainer.getY();
      this.rect.set(var2 - (float)AndroidUtilities.dp(8.0F), var3 - (float)AndroidUtilities.dp(8.0F), (float)this.cameraContainer.getMeasuredWidth() + var2 + (float)AndroidUtilities.dp(8.0F), (float)this.cameraContainer.getMeasuredHeight() + var3 + (float)AndroidUtilities.dp(8.0F));
      float var4 = this.progress;
      if (var4 != 0.0F) {
         var1.drawArc(this.rect, -90.0F, var4 * 360.0F, false, this.paint);
      }

      if (Theme.chat_roundVideoShadow != null) {
         int var5 = (int)var2 - AndroidUtilities.dp(3.0F);
         int var6 = (int)var3 - AndroidUtilities.dp(2.0F);
         var1.save();
         var1.scale(this.cameraContainer.getScaleX(), this.cameraContainer.getScaleY(), (float)(AndroidUtilities.roundMessageSize / 2 + var5 + AndroidUtilities.dp(3.0F)), (float)(AndroidUtilities.roundMessageSize / 2 + var6 + AndroidUtilities.dp(3.0F)));
         Theme.chat_roundVideoShadow.setAlpha((int)(this.cameraContainer.getAlpha() * 255.0F));
         Theme.chat_roundVideoShadow.setBounds(var5, var6, AndroidUtilities.roundMessageSize + var5 + AndroidUtilities.dp(6.0F), AndroidUtilities.roundMessageSize + var6 + AndroidUtilities.dp(6.0F));
         Theme.chat_roundVideoShadow.draw(var1);
         var1.restore();
      }

   }

   public boolean onInterceptTouchEvent(MotionEvent var1) {
      this.getParent().requestDisallowInterceptTouchEvent(true);
      return super.onInterceptTouchEvent(var1);
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (this.getVisibility() != 0) {
         this.cameraContainer.setTranslationY((float)(this.getMeasuredHeight() / 2));
         this.textureOverlayView.setTranslationY((float)(this.getMeasuredHeight() / 2));
      }

   }

   public void send(int var1) {
      if (this.textureView != null) {
         this.stopProgressTimer();
         VideoPlayer var2 = this.videoPlayer;
         if (var2 != null) {
            var2.releasePlayer(true);
            this.videoPlayer = null;
         }

         if (var1 == 4) {
            VideoEditedInfo var18;
            if (this.videoEditedInfo.needConvert()) {
               this.file = null;
               this.encryptedFile = null;
               this.key = null;
               this.iv = null;
               var18 = this.videoEditedInfo;
               double var3 = (double)var18.estimatedDuration;
               long var5 = var18.startTime;
               if (var5 < 0L) {
                  var5 = 0L;
               }

               var18 = this.videoEditedInfo;
               long var7 = var18.endTime;
               if (var7 < 0L) {
                  var7 = var18.estimatedDuration;
               }

               var18 = this.videoEditedInfo;
               var18.estimatedDuration = var7 - var5;
               double var9 = (double)this.size;
               double var11 = (double)var18.estimatedDuration;
               Double.isNaN(var11);
               Double.isNaN(var3);
               var11 /= var3;
               Double.isNaN(var9);
               var18.estimatedSize = Math.max(1L, (long)(var9 * var11));
               var18 = this.videoEditedInfo;
               var18.bitrate = 400000;
               var5 = var18.startTime;
               if (var5 > 0L) {
                  var18.startTime = var5 * 1000L;
               }

               var18 = this.videoEditedInfo;
               var5 = var18.endTime;
               if (var5 > 0L) {
                  var18.endTime = var5 * 1000L;
               }

               FileLoader.getInstance(this.currentAccount).cancelUploadFile(this.cameraFile.getAbsolutePath(), false);
            } else {
               this.videoEditedInfo.estimatedSize = Math.max(1L, this.size);
            }

            var18 = this.videoEditedInfo;
            var18.file = this.file;
            var18.encryptedFile = this.encryptedFile;
            var18.key = this.key;
            var18.iv = this.iv;
            this.baseFragment.sendMedia(new MediaController.PhotoEntry(0, 0, 0L, this.cameraFile.getAbsolutePath(), 0, true), this.videoEditedInfo);
         } else {
            boolean var13;
            if (this.recordedTime < 800L) {
               var13 = true;
            } else {
               var13 = false;
            }

            this.cancelled = var13;
            this.recording = false;
            AndroidUtilities.cancelRunOnUIThread(this.timerRunnable);
            if (this.cameraThread != null) {
               NotificationCenter var19 = NotificationCenter.getInstance(this.currentAccount);
               int var14 = NotificationCenter.recordStopped;
               var13 = this.cancelled;
               byte var15 = 2;
               byte var16;
               if (!var13 && var1 == 3) {
                  var16 = 2;
               } else {
                  var16 = 0;
               }

               var19.postNotificationName(var14, Integer.valueOf(var16));
               byte var17;
               if (this.cancelled) {
                  var17 = 0;
               } else if (var1 == 3) {
                  var17 = var15;
               } else {
                  var17 = 1;
               }

               this.saveLastCameraBitmap();
               this.cameraThread.shutdown(var17);
               this.cameraThread = null;
            }

            if (this.cancelled) {
               NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.audioRecordTooShort, true);
               this.startAnimation(false);
            }
         }

      }
   }

   @Keep
   public void setAlpha(float var1) {
      ((ColorDrawable)this.getBackground()).setAlpha((int)(var1 * 192.0F));
      this.invalidate();
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      this.setAlpha(0.0F);
      this.switchCameraButton.setAlpha(0.0F);
      this.cameraContainer.setAlpha(0.0F);
      this.textureOverlayView.setAlpha(0.0F);
      this.muteImageView.setAlpha(0.0F);
      this.muteImageView.setScaleX(1.0F);
      this.muteImageView.setScaleY(1.0F);
      this.cameraContainer.setScaleX(0.1F);
      this.cameraContainer.setScaleY(0.1F);
      this.textureOverlayView.setScaleX(0.1F);
      this.textureOverlayView.setScaleY(0.1F);
      if (this.cameraContainer.getMeasuredWidth() != 0) {
         FrameLayout var2 = this.cameraContainer;
         var2.setPivotX((float)(var2.getMeasuredWidth() / 2));
         var2 = this.cameraContainer;
         var2.setPivotY((float)(var2.getMeasuredHeight() / 2));
         BackupImageView var5 = this.textureOverlayView;
         var5.setPivotX((float)(var5.getMeasuredWidth() / 2));
         var5 = this.textureOverlayView;
         var5.setPivotY((float)(var5.getMeasuredHeight() / 2));
      }

      Exception var10000;
      boolean var10001;
      if (var1 == 0) {
         try {
            ((Activity)this.getContext()).getWindow().addFlags(128);
            return;
         } catch (Exception var3) {
            var10000 = var3;
            var10001 = false;
         }
      } else {
         try {
            ((Activity)this.getContext()).getWindow().clearFlags(128);
            return;
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
         }
      }

      Exception var6 = var10000;
      FileLog.e((Throwable)var6);
   }

   public void showCamera() {
      if (this.textureView == null) {
         this.switchCameraButton.setImageResource(2131165335);
         this.textureOverlayView.setAlpha(1.0F);
         if (this.lastBitmap == null) {
            try {
               File var1 = new File(ApplicationLoader.getFilesDirFixed(), "icthumb.jpg");
               this.lastBitmap = BitmapFactory.decodeFile(var1.getAbsolutePath());
            } catch (Throwable var3) {
            }
         }

         Bitmap var4 = this.lastBitmap;
         if (var4 != null) {
            this.textureOverlayView.setImageBitmap(var4);
         } else {
            this.textureOverlayView.setImageResource(2131165477);
         }

         this.cameraReady = false;
         this.isFrontface = true;
         this.selectedCamera = null;
         this.recordedTime = 0L;
         this.progress = 0.0F;
         this.cancelled = false;
         this.file = null;
         this.encryptedFile = null;
         this.key = null;
         this.iv = null;
         if (this.initCamera()) {
            MediaController.getInstance().pauseMessage(MediaController.getInstance().getPlayingMessageObject());
            File var2 = FileLoader.getDirectory(4);
            StringBuilder var5 = new StringBuilder();
            var5.append(SharedConfig.getLastLocalId());
            var5.append(".mp4");
            this.cameraFile = new File(var2, var5.toString());
            SharedConfig.saveConfig();
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d("show round camera");
            }

            this.textureView = new TextureView(this.getContext());
            this.textureView.setSurfaceTextureListener(new SurfaceTextureListener() {
               public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("camera surface available");
                  }

                  if (InstantCameraView.this.cameraThread == null && var1 != null) {
                     if (InstantCameraView.this.cancelled) {
                        return;
                     }

                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("start create thread");
                     }

                     InstantCameraView var4 = InstantCameraView.this;
                     var4.cameraThread = var4.new CameraGLThread(var1, var2, var3);
                  }

               }

               public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
                  if (InstantCameraView.this.cameraThread != null) {
                     InstantCameraView.this.cameraThread.shutdown(0);
                     InstantCameraView.this.cameraThread = null;
                  }

                  if (InstantCameraView.this.cameraSession != null) {
                     CameraController.getInstance().close(InstantCameraView.this.cameraSession, (CountDownLatch)null, (Runnable)null);
                  }

                  return true;
               }

               public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
               }

               public void onSurfaceTextureUpdated(SurfaceTexture var1) {
               }
            });
            this.cameraContainer.addView(this.textureView, LayoutHelper.createFrame(-1, -1.0F));
            this.setVisibility(0);
            this.startAnimation(true);
         }
      }
   }

   public void startAnimation(boolean var1) {
      AnimatorSet var2 = this.animatorSet;
      if (var2 != null) {
         var2.cancel();
      }

      PipRoundVideoView var20 = PipRoundVideoView.getInstance();
      if (var20 != null) {
         var20.showTemporary(var1 ^ true);
      }

      this.animatorSet = new AnimatorSet();
      AnimatorSet var3 = this.animatorSet;
      float var4 = 1.0F;
      float var5 = 0.0F;
      float var6;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.0F;
      }

      ObjectAnimator var21 = ObjectAnimator.ofFloat(this, "alpha", new float[]{var6});
      ImageView var7 = this.switchCameraButton;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.0F;
      }

      ObjectAnimator var22 = ObjectAnimator.ofFloat(var7, "alpha", new float[]{var6});
      ObjectAnimator var8 = ObjectAnimator.ofFloat(this.muteImageView, "alpha", new float[]{0.0F});
      Paint var9 = this.paint;
      short var10;
      if (var1) {
         var10 = 255;
      } else {
         var10 = 0;
      }

      ObjectAnimator var23 = ObjectAnimator.ofInt(var9, "alpha", new int[]{var10});
      FrameLayout var11 = this.cameraContainer;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.0F;
      }

      ObjectAnimator var24 = ObjectAnimator.ofFloat(var11, "alpha", new float[]{var6});
      FrameLayout var12 = this.cameraContainer;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.1F;
      }

      ObjectAnimator var25 = ObjectAnimator.ofFloat(var12, "scaleX", new float[]{var6});
      FrameLayout var13 = this.cameraContainer;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.1F;
      }

      ObjectAnimator var26 = ObjectAnimator.ofFloat(var13, "scaleY", new float[]{var6});
      FrameLayout var14 = this.cameraContainer;
      if (var1) {
         var6 = (float)(this.getMeasuredHeight() / 2);
      } else {
         var6 = 0.0F;
      }

      float var15;
      if (var1) {
         var15 = 0.0F;
      } else {
         var15 = (float)(this.getMeasuredHeight() / 2);
      }

      ObjectAnimator var27 = ObjectAnimator.ofFloat(var14, "translationY", new float[]{var6, var15});
      BackupImageView var16 = this.textureOverlayView;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.0F;
      }

      ObjectAnimator var28 = ObjectAnimator.ofFloat(var16, "alpha", new float[]{var6});
      BackupImageView var17 = this.textureOverlayView;
      if (var1) {
         var6 = 1.0F;
      } else {
         var6 = 0.1F;
      }

      ObjectAnimator var29 = ObjectAnimator.ofFloat(var17, "scaleX", new float[]{var6});
      BackupImageView var18 = this.textureOverlayView;
      if (var1) {
         var6 = var4;
      } else {
         var6 = 0.1F;
      }

      ObjectAnimator var30 = ObjectAnimator.ofFloat(var18, "scaleY", new float[]{var6});
      BackupImageView var19 = this.textureOverlayView;
      if (var1) {
         var6 = (float)(this.getMeasuredHeight() / 2);
      } else {
         var6 = 0.0F;
      }

      if (var1) {
         var15 = var5;
      } else {
         var15 = (float)(this.getMeasuredHeight() / 2);
      }

      var3.playTogether(new Animator[]{var21, var22, var8, var23, var24, var25, var26, var27, var28, var29, var30, ObjectAnimator.ofFloat(var19, "translationY", new float[]{var6, var15})});
      if (!var1) {
         this.animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               if (var1.equals(InstantCameraView.this.animatorSet)) {
                  InstantCameraView.this.hideCamera(true);
                  InstantCameraView.this.setVisibility(4);
               }

            }
         });
      }

      this.animatorSet.setDuration(180L);
      this.animatorSet.setInterpolator(new DecelerateInterpolator());
      this.animatorSet.start();
   }

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

      // $FF: synthetic method
      AudioBufferInfo(Object var2) {
         this();
      }
   }

   public class CameraGLThread extends DispatchQueue {
      private final int DO_REINIT_MESSAGE = 2;
      private final int DO_RENDER_MESSAGE = 0;
      private final int DO_SETSESSION_MESSAGE = 3;
      private final int DO_SHUTDOWN_MESSAGE = 1;
      private final int EGL_CONTEXT_CLIENT_VERSION = 12440;
      private final int EGL_OPENGL_ES2_BIT = 4;
      private Integer cameraId = 0;
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
      private InstantCameraView.VideoRecorder videoEncoder;

      public CameraGLThread(SurfaceTexture var2, int var3, int var4) {
         super("CameraGLThread");
         this.surfaceTexture = var2;
         int var5 = InstantCameraView.this.previewSize.getWidth();
         int var6 = InstantCameraView.this.previewSize.getHeight();
         float var7 = (float)var3;
         float var8 = var7 / (float)Math.min(var5, var6);
         var3 = (int)((float)var5 * var8);
         var6 = (int)((float)var6 * var8);
         if (var3 > var6) {
            InstantCameraView.this.scaleX = 1.0F;
            InstantCameraView.this.scaleY = (float)var3 / (float)var4;
         } else {
            InstantCameraView.this.scaleX = (float)var6 / var7;
            InstantCameraView.this.scaleY = 1.0F;
         }

      }

      private boolean initGL() {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start init gl");
         }

         this.egl10 = (EGL10)EGLContext.getEGL();
         this.eglDisplay = this.egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
         EGLDisplay var1 = this.eglDisplay;
         StringBuilder var14;
         if (var1 == EGL10.EGL_NO_DISPLAY) {
            if (BuildVars.LOGS_ENABLED) {
               var14 = new StringBuilder();
               var14.append("eglGetDisplay failed ");
               var14.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
               FileLog.e(var14.toString());
            }

            this.finish();
            return false;
         } else {
            int[] var2 = new int[2];
            if (!this.egl10.eglInitialize(var1, var2)) {
               if (BuildVars.LOGS_ENABLED) {
                  var14 = new StringBuilder();
                  var14.append("eglInitialize failed ");
                  var14.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                  FileLog.e(var14.toString());
               }

               this.finish();
               return false;
            } else {
               int[] var9 = new int[1];
               EGLConfig[] var11 = new EGLConfig[1];
               if (!this.egl10.eglChooseConfig(this.eglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 0, 12325, 0, 12326, 0, 12344}, var11, 1, var9)) {
                  if (BuildVars.LOGS_ENABLED) {
                     var14 = new StringBuilder();
                     var14.append("eglChooseConfig failed ");
                     var14.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                     FileLog.e(var14.toString());
                  }

                  this.finish();
                  return false;
               } else if (var9[0] > 0) {
                  this.eglConfig = var11[0];
                  this.eglContext = this.egl10.eglCreateContext(this.eglDisplay, this.eglConfig, EGL10.EGL_NO_CONTEXT, new int[]{12440, 2, 12344});
                  if (this.eglContext == null) {
                     if (BuildVars.LOGS_ENABLED) {
                        var14 = new StringBuilder();
                        var14.append("eglCreateContext failed ");
                        var14.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.e(var14.toString());
                     }

                     this.finish();
                     return false;
                  } else {
                     SurfaceTexture var12 = this.surfaceTexture;
                     if (var12 instanceof SurfaceTexture) {
                        this.eglSurface = this.egl10.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, var12, (int[])null);
                        EGLSurface var13 = this.eglSurface;
                        if (var13 != null && var13 != EGL10.EGL_NO_SURFACE) {
                           if (!this.egl10.eglMakeCurrent(this.eglDisplay, var13, var13, this.eglContext)) {
                              if (BuildVars.LOGS_ENABLED) {
                                 var14 = new StringBuilder();
                                 var14.append("eglMakeCurrent failed ");
                                 var14.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                                 FileLog.e(var14.toString());
                              }

                              this.finish();
                              return false;
                           } else {
                              this.gl = this.eglContext.getGL();
                              float var3 = 1.0F / InstantCameraView.this.scaleX / 2.0F;
                              float var4 = 1.0F / InstantCameraView.this.scaleY / 2.0F;
                              float[] var15 = new float[]{-1.0F, -1.0F, 0.0F, 1.0F, -1.0F, 0.0F, -1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F};
                              float[] var10 = new float[8];
                              float var5 = 0.5F - var3;
                              var10[0] = var5;
                              float var6 = 0.5F - var4;
                              var10[1] = var6;
                              var3 += 0.5F;
                              var10[2] = var3;
                              var10[3] = var6;
                              var10[4] = var5;
                              var4 += 0.5F;
                              var10[5] = var4;
                              var10[6] = var3;
                              var10[7] = var4;
                              this.videoEncoder = InstantCameraView.this.new VideoRecorder();
                              InstantCameraView.this.vertexBuffer = ByteBuffer.allocateDirect(var15.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                              InstantCameraView.this.vertexBuffer.put(var15).position(0);
                              InstantCameraView.this.textureBuffer = ByteBuffer.allocateDirect(var10.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                              InstantCameraView.this.textureBuffer.put(var10).position(0);
                              Matrix.setIdentityM(InstantCameraView.this.mSTMatrix, 0);
                              int var7 = InstantCameraView.this.loadShader(35633, "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n");
                              int var8 = InstantCameraView.this.loadShader(35632, "#extension GL_OES_EGL_image_external : require\nprecision lowp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n");
                              if (var7 != 0 && var8 != 0) {
                                 this.drawProgram = GLES20.glCreateProgram();
                                 GLES20.glAttachShader(this.drawProgram, var7);
                                 GLES20.glAttachShader(this.drawProgram, var8);
                                 GLES20.glLinkProgram(this.drawProgram);
                                 var2 = new int[1];
                                 GLES20.glGetProgramiv(this.drawProgram, 35714, var2, 0);
                                 if (var2[0] == 0) {
                                    if (BuildVars.LOGS_ENABLED) {
                                       FileLog.e("failed link shader");
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
                                 this.cameraSurface.setOnFrameAvailableListener(new _$$Lambda$InstantCameraView$CameraGLThread$owshQSrJ0yE90p_o4TfGsfbd_z0(this));
                                 InstantCameraView.this.createCamera(this.cameraSurface);
                                 if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("gl initied");
                                 }

                                 return true;
                              } else {
                                 if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("failed creating shader");
                                 }

                                 this.finish();
                                 return false;
                              }
                           }
                        } else {
                           if (BuildVars.LOGS_ENABLED) {
                              var14 = new StringBuilder();
                              var14.append("createWindowSurface failed ");
                              var14.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                              FileLog.e(var14.toString());
                           }

                           this.finish();
                           return false;
                        }
                     } else {
                        this.finish();
                        return false;
                     }
                  }
               } else {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e("eglConfig not initialized");
                  }

                  this.finish();
                  return false;
               }
            }
         }
      }

      private void onDraw(Integer var1) {
         if (this.initied) {
            if (!this.eglContext.equals(this.egl10.eglGetCurrentContext()) || !this.eglSurface.equals(this.egl10.eglGetCurrentSurface(12377))) {
               EGL10 var2 = this.egl10;
               EGLDisplay var3 = this.eglDisplay;
               EGLSurface var4 = this.eglSurface;
               if (!var2.eglMakeCurrent(var3, var4, var4, this.eglContext)) {
                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var7 = new StringBuilder();
                     var7.append("eglMakeCurrent failed ");
                     var7.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                     FileLog.e(var7.toString());
                  }

                  return;
               }
            }

            this.cameraSurface.updateTexImage();
            if (!this.recording) {
               this.videoEncoder.startRecording(InstantCameraView.this.cameraFile, EGL14.eglGetCurrentContext());
               this.recording = true;
               int var5 = this.currentSession.getCurrentOrientation();
               if (var5 == 90 || var5 == 270) {
                  float var6 = InstantCameraView.this.scaleX;
                  InstantCameraView var8 = InstantCameraView.this;
                  var8.scaleX = var8.scaleY;
                  InstantCameraView.this.scaleY = var6;
               }
            }

            this.videoEncoder.frameAvailable(this.cameraSurface, var1, System.nanoTime());
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

      public void finish() {
         if (this.eglSurface != null) {
            EGL10 var1 = this.egl10;
            EGLDisplay var2 = this.eglDisplay;
            EGLSurface var3 = EGL10.EGL_NO_SURFACE;
            var1.eglMakeCurrent(var2, var3, var3, EGL10.EGL_NO_CONTEXT);
            this.egl10.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = null;
         }

         EGLContext var4 = this.eglContext;
         if (var4 != null) {
            this.egl10.eglDestroyContext(this.eglDisplay, var4);
            this.eglContext = null;
         }

         EGLDisplay var5 = this.eglDisplay;
         if (var5 != null) {
            this.egl10.eglTerminate(var5);
            this.eglDisplay = null;
         }

      }

      public void handleMessage(Message var1) {
         int var2 = var1.what;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 == 3) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("set gl rednderer session");
                     }

                     CameraSession var3 = (CameraSession)var1.obj;
                     CameraSession var5 = this.currentSession;
                     if (var5 == var3) {
                        this.rotationAngle = var5.getWorldAngle();
                        Matrix.setIdentityM(InstantCameraView.this.mMVPMatrix, 0);
                        if (this.rotationAngle != 0) {
                           Matrix.rotateM(InstantCameraView.this.mMVPMatrix, 0, (float)this.rotationAngle, 0.0F, 0.0F, 1.0F);
                        }
                     } else {
                        this.currentSession = var3;
                     }
                  }
               } else {
                  EGL10 var6 = this.egl10;
                  EGLDisplay var10 = this.eglDisplay;
                  EGLSurface var4 = this.eglSurface;
                  if (!var6.eglMakeCurrent(var10, var4, var4, this.eglContext)) {
                     if (BuildVars.LOGS_ENABLED) {
                        StringBuilder var8 = new StringBuilder();
                        var8.append("eglMakeCurrent failed ");
                        var8.append(GLUtils.getEGLErrorString(this.egl10.eglGetError()));
                        FileLog.d(var8.toString());
                     }

                     return;
                  }

                  SurfaceTexture var7 = this.cameraSurface;
                  if (var7 != null) {
                     var7.getTransformMatrix(InstantCameraView.this.moldSTMatrix);
                     this.cameraSurface.setOnFrameAvailableListener((OnFrameAvailableListener)null);
                     this.cameraSurface.release();
                     InstantCameraView.this.oldCameraTexture[0] = InstantCameraView.this.cameraTexture[0];
                     InstantCameraView.this.cameraTextureAlpha = 0.0F;
                     InstantCameraView.this.cameraTexture[0] = 0;
                  }

                  this.cameraId = this.cameraId + 1;
                  InstantCameraView.this.cameraReady = false;
                  GLES20.glGenTextures(1, InstantCameraView.this.cameraTexture, 0);
                  GLES20.glBindTexture(36197, InstantCameraView.this.cameraTexture[0]);
                  GLES20.glTexParameteri(36197, 10241, 9729);
                  GLES20.glTexParameteri(36197, 10240, 9729);
                  GLES20.glTexParameteri(36197, 10242, 33071);
                  GLES20.glTexParameteri(36197, 10243, 33071);
                  this.cameraSurface = new SurfaceTexture(InstantCameraView.this.cameraTexture[0]);
                  this.cameraSurface.setOnFrameAvailableListener(new _$$Lambda$InstantCameraView$CameraGLThread$8CYV7PTMH7zxhUymqIagAcuf5w8(this));
                  InstantCameraView.this.createCamera(this.cameraSurface);
               }
            } else {
               this.finish();
               if (this.recording) {
                  this.videoEncoder.stopRecording(var1.arg1);
               }

               Looper var9 = Looper.myLooper();
               if (var9 != null) {
                  var9.quit();
               }
            }
         } else {
            this.onDraw((Integer)var1.obj);
         }

      }

      // $FF: synthetic method
      public void lambda$handleMessage$1$InstantCameraView$CameraGLThread(SurfaceTexture var1) {
         this.requestRender();
      }

      // $FF: synthetic method
      public void lambda$initGL$0$InstantCameraView$CameraGLThread(SurfaceTexture var1) {
         this.requestRender();
      }

      public void reinitForNewCamera() {
         Handler var1 = InstantCameraView.this.getHandler();
         if (var1 != null) {
            this.sendMessage(var1.obtainMessage(2), 0);
         }

      }

      public void requestRender() {
         Handler var1 = InstantCameraView.this.getHandler();
         if (var1 != null) {
            this.sendMessage(var1.obtainMessage(0, this.cameraId), 0);
         }

      }

      public void run() {
         this.initied = this.initGL();
         super.run();
      }

      public void setCurrentSession(CameraSession var1) {
         Handler var2 = InstantCameraView.this.getHandler();
         if (var2 != null) {
            this.sendMessage(var2.obtainMessage(3, var1), 0);
         }

      }

      public void shutdown(int var1) {
         Handler var2 = InstantCameraView.this.getHandler();
         if (var2 != null) {
            this.sendMessage(var2.obtainMessage(1, var1, 0), 0);
         }

      }
   }

   private static class EncoderHandler extends Handler {
      private WeakReference mWeakEncoder;

      public EncoderHandler(InstantCameraView.VideoRecorder var1) {
         this.mWeakEncoder = new WeakReference(var1);
      }

      public void exit() {
         Looper.myLooper().quit();
      }

      public void handleMessage(Message var1) {
         int var2 = var1.what;
         Object var3 = var1.obj;
         InstantCameraView.VideoRecorder var5 = (InstantCameraView.VideoRecorder)this.mWeakEncoder.get();
         if (var5 != null) {
            if (var2 != 0) {
               if (var2 != 1) {
                  if (var2 != 2) {
                     if (var2 == 3) {
                        var5.handleAudioFrameAvailable((InstantCameraView.AudioBufferInfo)var1.obj);
                     }
                  } else {
                     var5.handleVideoFrameAvailable((long)var1.arg1 << 32 | (long)var1.arg2 & 4294967295L, (Integer)var1.obj);
                  }
               } else {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e("stop encoder");
                  }

                  var5.handleStopRecording(var1.arg1);
               }
            } else {
               try {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.e("start encoder");
                  }

                  var5.prepareEncoder();
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
                  var5.handleStopRecording(0);
                  Looper.myLooper().quit();
               }
            }

         }
      }
   }

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
      private ArrayBlockingQueue buffers;
      private ArrayList buffersToWrite;
      private long currentTimestamp;
      private long desyncTime;
      private int drawProgram;
      private android.opengl.EGLConfig eglConfig;
      private android.opengl.EGLContext eglContext;
      private android.opengl.EGLDisplay eglDisplay;
      private android.opengl.EGLSurface eglSurface;
      private volatile InstantCameraView.EncoderHandler handler;
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

      private VideoRecorder() {
         this.videoConvertFirstWrite = true;
         this.eglDisplay = EGL14.EGL_NO_DISPLAY;
         this.eglContext = EGL14.EGL_NO_CONTEXT;
         this.eglSurface = EGL14.EGL_NO_SURFACE;
         this.buffersToWrite = new ArrayList();
         this.videoTrackIndex = -5;
         this.audioTrackIndex = -5;
         this.audioStartTime = -1L;
         this.currentTimestamp = 0L;
         this.lastTimestamp = -1L;
         this.sync = new Object();
         this.videoFirst = -1L;
         this.audioFirst = -1L;
         this.lastCameraId = 0;
         this.buffers = new ArrayBlockingQueue(10);
         this.recorderRunnable = new Runnable() {
            public void run() {
               long var1 = -1L;
               boolean var3 = false;

               while(!var3) {
                  boolean var4 = var3;
                  if (!VideoRecorder.this.running) {
                     var4 = var3;
                     if (VideoRecorder.this.audioRecorder.getRecordingState() != 1) {
                        try {
                           VideoRecorder.this.audioRecorder.stop();
                        } catch (Exception var12) {
                           var3 = true;
                        }

                        var4 = var3;
                        if (VideoRecorder.this.sendWhenDone == 0) {
                           break;
                        }
                     }
                  }

                  InstantCameraView.AudioBufferInfo var5;
                  if (VideoRecorder.this.buffers.isEmpty()) {
                     var5 = InstantCameraView.this.new AudioBufferInfo();
                  } else {
                     var5 = (InstantCameraView.AudioBufferInfo)VideoRecorder.this.buffers.poll();
                  }

                  var5.lastWroteBuffer = 0;
                  var5.results = 10;
                  int var14 = 0;

                  long var6;
                  while(true) {
                     var6 = var1;
                     if (var14 >= 10) {
                        break;
                     }

                     long var8 = var1;
                     if (var1 == -1L) {
                        var8 = System.nanoTime() / 1000L;
                     }

                     int var10 = VideoRecorder.this.audioRecorder.read(var5.buffer, var14 * 2048, 2048);
                     if (var10 <= 0) {
                        var5.results = var14;
                        var6 = var8;
                        if (!VideoRecorder.this.running) {
                           var5.last = true;
                           var6 = var8;
                        }
                        break;
                     }

                     var5.offset[var14] = var8;
                     var5.read[var14] = var10;
                     var1 = var8 + (long)(var10 * 1000000 / '걄' / 2);
                     ++var14;
                  }

                  var1 = var6;
                  if (var5.results < 0 && !var5.last) {
                     if (!VideoRecorder.this.running) {
                        var3 = true;
                     } else {
                        try {
                           VideoRecorder.this.buffers.put(var5);
                        } catch (Exception var13) {
                           var3 = var4;
                           continue;
                        }

                        var3 = var4;
                     }
                  } else {
                     var3 = var4;
                     if (!VideoRecorder.this.running) {
                        var3 = var4;
                        if (var5.results < 10) {
                           var3 = true;
                        }
                     }

                     VideoRecorder.this.handler.sendMessage(VideoRecorder.this.handler.obtainMessage(3, var5));
                  }
               }

               try {
                  VideoRecorder.this.audioRecorder.release();
               } catch (Exception var11) {
                  FileLog.e((Throwable)var11);
               }

               VideoRecorder.this.handler.sendMessage(VideoRecorder.this.handler.obtainMessage(1, VideoRecorder.this.sendWhenDone, 0));
            }
         };
      }

      // $FF: synthetic method
      VideoRecorder(Object var2) {
         this();
      }

      private void didWriteData(File var1, long var2, boolean var4) {
         boolean var5 = this.videoConvertFirstWrite;
         long var6 = 0L;
         if (var5) {
            FileLoader.getInstance(InstantCameraView.this.currentAccount).uploadFile(var1.toString(), InstantCameraView.this.isSecretChat, false, 1, 33554432);
            this.videoConvertFirstWrite = false;
            if (var4) {
               FileLoader var8 = FileLoader.getInstance(InstantCameraView.this.currentAccount);
               String var9 = var1.toString();
               var5 = InstantCameraView.this.isSecretChat;
               if (var4) {
                  var6 = var1.length();
               }

               var8.checkUploadNewDataAvailable(var9, var5, var2, var6);
            }
         } else {
            FileLoader var11 = FileLoader.getInstance(InstantCameraView.this.currentAccount);
            String var10 = var1.toString();
            var5 = InstantCameraView.this.isSecretChat;
            if (var4) {
               var6 = var1.length();
            }

            var11.checkUploadNewDataAvailable(var10, var5, var2, var6);
         }

      }

      private void handleAudioFrameAvailable(InstantCameraView.AudioBufferInfo var1) {
         if (!this.audioStopedByTime) {
            ArrayList var2 = this.buffersToWrite;
            var2.add(var1);
            InstantCameraView.AudioBufferInfo var28 = var1;
            int var3;
            long var4;
            if (this.audioFirst == -1L) {
               if (this.videoFirst == -1L) {
                  if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("video record not yet started");
                  }

                  return;
               }

               while(true) {
                  var3 = 0;

                  StringBuilder var30;
                  boolean var32;
                  label235: {
                     while(true) {
                        if (var3 >= var1.results) {
                           var32 = false;
                           break label235;
                        }

                        long[] var29;
                        if (var3 == 0 && Math.abs(this.videoFirst - var1.offset[var3]) > 10000000L) {
                           var4 = this.videoFirst;
                           var29 = var1.offset;
                           this.desyncTime = var4 - var29[var3];
                           this.audioFirst = var29[var3];
                           if (BuildVars.LOGS_ENABLED) {
                              var30 = new StringBuilder();
                              var30.append("detected desync between audio and video ");
                              var30.append(this.desyncTime);
                              FileLog.d(var30.toString());
                           }
                           break;
                        }

                        var29 = var1.offset;
                        if (var29[var3] >= this.videoFirst) {
                           var1.lastWroteBuffer = var3;
                           this.audioFirst = var29[var3];
                           if (BuildVars.LOGS_ENABLED) {
                              var30 = new StringBuilder();
                              var30.append("found first audio frame at ");
                              var30.append(var3);
                              var30.append(" timestamp = ");
                              var30.append(var1.offset[var3]);
                              FileLog.d(var30.toString());
                           }
                           break;
                        }

                        if (BuildVars.LOGS_ENABLED) {
                           var30 = new StringBuilder();
                           var30.append("ignore first audio frame at ");
                           var30.append(var3);
                           var30.append(" timestamp = ");
                           var30.append(var1.offset[var3]);
                           FileLog.d(var30.toString());
                        }

                        ++var3;
                     }

                     var32 = true;
                  }

                  var28 = var1;
                  if (var32) {
                     break;
                  }

                  if (BuildVars.LOGS_ENABLED) {
                     var30 = new StringBuilder();
                     var30.append("first audio frame not found, removing buffers ");
                     var30.append(var1.results);
                     FileLog.d(var30.toString());
                  }

                  this.buffersToWrite.remove(var1);
                  if (this.buffersToWrite.isEmpty()) {
                     return;
                  }

                  var1 = (InstantCameraView.AudioBufferInfo)this.buffersToWrite.get(0);
               }
            }

            if (this.audioStartTime == -1L) {
               this.audioStartTime = var28.offset[var28.lastWroteBuffer];
            }

            var1 = var28;
            if (this.buffersToWrite.size() > 1) {
               var1 = (InstantCameraView.AudioBufferInfo)this.buffersToWrite.get(0);
            }

            try {
               this.drainEncoder(false);
            } catch (Exception var13) {
               FileLog.e((Throwable)var13);
            }

            boolean var11;
            for(boolean var6 = false; var1 != null; var6 = var11) {
               Throwable var10000;
               label254: {
                  int var7;
                  boolean var10001;
                  try {
                     var7 = this.audioEncoder.dequeueInputBuffer(0L);
                  } catch (Throwable var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label254;
                  }

                  if (var7 < 0) {
                     var11 = var6;
                     continue;
                  }

                  ByteBuffer var8;
                  label209: {
                     try {
                        if (VERSION.SDK_INT >= 21) {
                           var8 = this.audioEncoder.getInputBuffer(var7);
                           break label209;
                        }
                     } catch (Throwable var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label254;
                     }

                     try {
                        var8 = this.audioEncoder.getInputBuffers()[var7];
                        var8.clear();
                     } catch (Throwable var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label254;
                     }
                  }

                  long var9;
                  try {
                     var9 = var1.offset[var1.lastWroteBuffer];
                     var3 = var1.lastWroteBuffer;
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label254;
                  }

                  var28 = var1;

                  while(true) {
                     var11 = var6;
                     var1 = var28;

                     label249: {
                        label250: {
                           label251: {
                              try {
                                 if (var3 > var28.results) {
                                    break;
                                 }

                                 if (var3 >= var28.results) {
                                    break label250;
                                 }

                                 if (this.running || var28.offset[var3] < this.videoLast - this.desyncTime) {
                                    break label251;
                                 }

                                 if (BuildVars.LOGS_ENABLED) {
                                    StringBuilder var31 = new StringBuilder();
                                    var31.append("stop audio encoding because of stoped video recording at ");
                                    var31.append(var28.offset[var3]);
                                    var31.append(" last video ");
                                    var31.append(this.videoLast);
                                    FileLog.d(var31.toString());
                                 }
                              } catch (Throwable var23) {
                                 var10000 = var23;
                                 var10001 = false;
                                 break label254;
                              }

                              try {
                                 this.audioStopedByTime = true;
                                 this.buffersToWrite.clear();
                              } catch (Throwable var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label254;
                              }

                              var1 = null;
                              var11 = true;
                              break;
                           }

                           try {
                              if (var8.remaining() < var28.read[var3]) {
                                 var28.lastWroteBuffer = var3;
                                 break label249;
                              }
                           } catch (Throwable var22) {
                              var10000 = var22;
                              var10001 = false;
                              break label254;
                           }

                           try {
                              var8.put(var28.buffer, var3 * 2048, var28.read[var3]);
                           } catch (Throwable var19) {
                              var10000 = var19;
                              var10001 = false;
                              break label254;
                           }
                        }

                        label253: {
                           try {
                              if (var3 < var28.results - 1) {
                                 break label253;
                              }

                              this.buffersToWrite.remove(var28);
                              if (this.running) {
                                 this.buffers.put(var28);
                              }
                           } catch (Throwable var20) {
                              var10000 = var20;
                              var10001 = false;
                              break label254;
                           }

                           try {
                              if (!this.buffersToWrite.isEmpty()) {
                                 var28 = (InstantCameraView.AudioBufferInfo)this.buffersToWrite.get(0);
                                 break label253;
                              }
                           } catch (Throwable var21) {
                              var10000 = var21;
                              var10001 = false;
                              break label254;
                           }

                           try {
                              var11 = var28.last;
                           } catch (Throwable var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label254;
                           }

                           var1 = null;
                           break;
                        }

                        ++var3;
                        continue;
                     }

                     var1 = null;
                     var11 = var6;
                     break;
                  }

                  int var12;
                  MediaCodec var35;
                  try {
                     var35 = this.audioEncoder;
                     var12 = var8.position();
                  } catch (Throwable var16) {
                     var10000 = var16;
                     var10001 = false;
                     break label254;
                  }

                  var4 = 0L;
                  if (var9 != 0L) {
                     try {
                        var4 = var9 - this.audioStartTime;
                     } catch (Throwable var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label254;
                     }
                  }

                  byte var34;
                  if (var11) {
                     var34 = 4;
                  } else {
                     var34 = 0;
                  }

                  try {
                     var35.queueInputBuffer(var7, 0, var12, var4, var34);
                     continue;
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                  }
               }

               Throwable var33 = var10000;
               FileLog.e(var33);
               break;
            }

         }
      }

      private void handleStopRecording(int var1) {
         if (this.running) {
            this.sendWhenDone = var1;
            this.running = false;
         } else {
            try {
               this.drainEncoder(true);
            } catch (Exception var7) {
               FileLog.e((Throwable)var7);
            }

            MediaCodec var2 = this.videoEncoder;
            if (var2 != null) {
               try {
                  var2.stop();
                  this.videoEncoder.release();
                  this.videoEncoder = null;
               } catch (Exception var6) {
                  FileLog.e((Throwable)var6);
               }
            }

            var2 = this.audioEncoder;
            if (var2 != null) {
               try {
                  var2.stop();
                  this.audioEncoder.release();
                  this.audioEncoder = null;
               } catch (Exception var5) {
                  FileLog.e((Throwable)var5);
               }
            }

            MP4Builder var8 = this.mediaMuxer;
            if (var8 != null) {
               try {
                  var8.finishMovie();
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }
            }

            if (var1 != 0) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$VideoRecorder$8yHVUV5ygBxWBv3JaxOCwtBcKyQ(this, var1));
            } else {
               FileLoader.getInstance(InstantCameraView.this.currentAccount).cancelUploadFile(this.videoFile.getAbsolutePath(), false);
               this.videoFile.delete();
            }

            EGL14.eglDestroySurface(this.eglDisplay, this.eglSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
            Surface var9 = this.surface;
            if (var9 != null) {
               var9.release();
               this.surface = null;
            }

            android.opengl.EGLDisplay var10 = this.eglDisplay;
            if (var10 != EGL14.EGL_NO_DISPLAY) {
               android.opengl.EGLSurface var3 = EGL14.EGL_NO_SURFACE;
               EGL14.eglMakeCurrent(var10, var3, var3, EGL14.EGL_NO_CONTEXT);
               EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
               EGL14.eglReleaseThread();
               EGL14.eglTerminate(this.eglDisplay);
            }

            this.eglDisplay = EGL14.EGL_NO_DISPLAY;
            this.eglContext = EGL14.EGL_NO_CONTEXT;
            this.eglConfig = null;
            this.handler.exit();
         }
      }

      private void handleVideoFrameAvailable(long var1, Integer var3) {
         try {
            this.drainEncoder(false);
         } catch (Exception var11) {
            FileLog.e((Throwable)var11);
         }

         if (!this.lastCameraId.equals(var3)) {
            this.lastTimestamp = -1L;
            this.lastCameraId = var3;
         }

         long var5;
         long var7;
         label60: {
            var5 = this.lastTimestamp;
            var7 = 0L;
            if (var5 == -1L) {
               this.lastTimestamp = var1;
               if (this.currentTimestamp != 0L) {
                  var7 = System.currentTimeMillis();
                  long var9 = this.lastCommitedFrameTime;
                  var5 = 0L;
                  var7 = (var7 - var9) * 1000000L;
                  break label60;
               }
            } else {
               var7 = var1 - var5;
               this.lastTimestamp = var1;
            }

            var5 = var7;
         }

         this.lastCommitedFrameTime = System.currentTimeMillis();
         if (!this.skippedFirst) {
            this.skippedTime += var7;
            if (this.skippedTime < 200000000L) {
               return;
            }

            this.skippedFirst = true;
         }

         this.currentTimestamp += var7;
         if (this.videoFirst == -1L) {
            this.videoFirst = var1 / 1000L;
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var12 = new StringBuilder();
               var12.append("first video frame was at ");
               var12.append(this.videoFirst);
               FileLog.d(var12.toString());
            }
         }

         this.videoLast = var1;
         GLES20.glUseProgram(this.drawProgram);
         GLES20.glVertexAttribPointer(this.positionHandle, 3, 5126, false, 12, InstantCameraView.this.vertexBuffer);
         GLES20.glEnableVertexAttribArray(this.positionHandle);
         GLES20.glVertexAttribPointer(this.textureHandle, 2, 5126, false, 8, InstantCameraView.this.textureBuffer);
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
            GLES20.glUniform1f(this.alphaHandle, 1.0F);
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
         if (InstantCameraView.this.oldCameraTexture[0] != 0 && InstantCameraView.this.cameraTextureAlpha < 1.0F) {
            InstantCameraView var13 = InstantCameraView.this;
            var13.cameraTextureAlpha = var13.cameraTextureAlpha + (float)var5 / 2.0E8F;
            if (InstantCameraView.this.cameraTextureAlpha > 1.0F) {
               GLES20.glDisable(3042);
               this.blendEnabled = false;
               InstantCameraView.this.cameraTextureAlpha = 1.0F;
               GLES20.glDeleteTextures(1, InstantCameraView.this.oldCameraTexture, 0);
               InstantCameraView.this.oldCameraTexture[0] = 0;
               if (!InstantCameraView.this.cameraReady) {
                  InstantCameraView.this.cameraReady = true;
                  AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$VideoRecorder$ONc2_IZzZjKNnbhigUCIh3I7vt4(this));
               }
            }
         } else if (!InstantCameraView.this.cameraReady) {
            InstantCameraView.this.cameraReady = true;
            AndroidUtilities.runOnUIThread(new _$$Lambda$InstantCameraView$VideoRecorder$cj_4IZt_HllVK7x5iDyMvDcfDnc(this));
         }

      }

      private void prepareEncoder() {
         int var1;
         int var2;
         StringBuilder var14;
         label101: {
            Exception var10000;
            label104: {
               boolean var10001;
               try {
                  var1 = AudioRecord.getMinBufferSize(44100, 16, 2);
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label104;
               }

               var2 = var1;
               if (var1 <= 0) {
                  var2 = 3584;
               }

               var1 = 49152;
               if (49152 < var2) {
                  try {
                     var1 = (var2 / 2048 + 1) * 2048 * 2;
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label104;
                  }
               }

               for(var2 = 0; var2 < 3; ++var2) {
                  try {
                     ArrayBlockingQueue var3 = this.buffers;
                     InstantCameraView.AudioBufferInfo var4 = InstantCameraView.this.new AudioBufferInfo();
                     var3.add(var4);
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label104;
                  }
               }

               try {
                  AudioRecord var13 = new AudioRecord(0, 44100, 16, 2, var1);
                  this.audioRecorder = var13;
                  this.audioRecorder.startRecording();
                  if (BuildVars.LOGS_ENABLED) {
                     var14 = new StringBuilder();
                     var14.append("initied audio record with channels ");
                     var14.append(this.audioRecorder.getChannelCount());
                     var14.append(" sample rate = ");
                     var14.append(this.audioRecorder.getSampleRate());
                     var14.append(" bufferSize = ");
                     var14.append(var1);
                     FileLog.d(var14.toString());
                  }
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label104;
               }

               try {
                  Thread var16 = new Thread(this.recorderRunnable);
                  var16.setPriority(10);
                  var16.start();
                  BufferInfo var17 = new BufferInfo();
                  this.audioBufferInfo = var17;
                  var17 = new BufferInfo();
                  this.videoBufferInfo = var17;
                  MediaFormat var18 = new MediaFormat();
                  var18.setString("mime", "audio/mp4a-latm");
                  var18.setInteger("aac-profile", 2);
                  var18.setInteger("sample-rate", 44100);
                  var18.setInteger("channel-count", 1);
                  var18.setInteger("bitrate", 32000);
                  var18.setInteger("max-input-size", 20480);
                  this.audioEncoder = MediaCodec.createEncoderByType("audio/mp4a-latm");
                  this.audioEncoder.configure(var18, (Surface)null, (MediaCrypto)null, 1);
                  this.audioEncoder.start();
                  this.videoEncoder = MediaCodec.createEncoderByType("video/avc");
                  var18 = MediaFormat.createVideoFormat("video/avc", this.videoWidth, this.videoHeight);
                  var18.setInteger("color-format", 2130708361);
                  var18.setInteger("bitrate", this.videoBitrate);
                  var18.setInteger("frame-rate", 30);
                  var18.setInteger("i-frame-interval", 1);
                  this.videoEncoder.configure(var18, (Surface)null, (MediaCrypto)null, 1);
                  this.surface = this.videoEncoder.createInputSurface();
                  this.videoEncoder.start();
                  Mp4Movie var11 = new Mp4Movie();
                  var11.setCacheFile(this.videoFile);
                  var11.setRotation(0);
                  var11.setSize(this.videoWidth, this.videoHeight);
                  MP4Builder var19 = new MP4Builder();
                  this.mediaMuxer = var19.createMovie(var11, InstantCameraView.this.isSecretChat);
                  _$$Lambda$InstantCameraView$VideoRecorder$8ZN8xq5EwIyFVJLsetA_qjZZbSQ var20 = new _$$Lambda$InstantCameraView$VideoRecorder$8ZN8xq5EwIyFVJLsetA_qjZZbSQ(this);
                  AndroidUtilities.runOnUIThread(var20);
                  break label101;
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            Exception var15 = var10000;
            throw new RuntimeException(var15);
         }

         if (this.eglDisplay == EGL14.EGL_NO_DISPLAY) {
            this.eglDisplay = EGL14.eglGetDisplay(0);
            android.opengl.EGLDisplay var12 = this.eglDisplay;
            if (var12 != EGL14.EGL_NO_DISPLAY) {
               int[] var21 = new int[2];
               if (EGL14.eglInitialize(var12, var21, 0, var21, 1)) {
                  if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
                     android.opengl.EGLConfig[] var5 = new android.opengl.EGLConfig[1];
                     var21 = new int[1];
                     var12 = this.eglDisplay;
                     var1 = var5.length;
                     if (!EGL14.eglChooseConfig(var12, new int[]{12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12610, 1, 12344}, 0, var5, 0, var1, var21, 0)) {
                        throw new RuntimeException("Unable to find a suitable EGLConfig");
                     }

                     this.eglContext = EGL14.eglCreateContext(this.eglDisplay, var5[0], this.sharedEglContext, new int[]{12440, 2, 12344}, 0);
                     this.eglConfig = var5[0];
                  }

                  var21 = new int[1];
                  EGL14.eglQueryContext(this.eglDisplay, this.eglContext, 12440, var21, 0);
                  if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
                     this.eglSurface = EGL14.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, this.surface, new int[]{12344}, 0);
                     android.opengl.EGLSurface var22 = this.eglSurface;
                     if (var22 != null) {
                        if (!EGL14.eglMakeCurrent(this.eglDisplay, var22, var22, this.eglContext)) {
                           if (BuildVars.LOGS_ENABLED) {
                              var14 = new StringBuilder();
                              var14.append("eglMakeCurrent failed ");
                              var14.append(GLUtils.getEGLErrorString(EGL14.eglGetError()));
                              FileLog.e(var14.toString());
                           }

                           throw new RuntimeException("eglMakeCurrent failed");
                        } else {
                           GLES20.glBlendFunc(770, 771);
                           var2 = InstantCameraView.this.loadShader(35633, "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n   gl_Position = uMVPMatrix * aPosition;\n   vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n");
                           var1 = InstantCameraView.this.loadShader(35632, "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform float scaleX;\nuniform float scaleY;\nuniform float alpha;\nuniform samplerExternalOES sTexture;\nvoid main() {\n   vec2 coord = vec2((vTextureCoord.x - 0.5) * scaleX, (vTextureCoord.y - 0.5) * scaleY);\n   float coef = ceil(clamp(0.2601 - dot(coord, coord), 0.0, 1.0));\n   vec3 color = texture2D(sTexture, vTextureCoord).rgb * coef + (1.0 - step(0.001, coef));\n   gl_FragColor = vec4(color * alpha, alpha);\n}\n");
                           if (var2 != 0 && var1 != 0) {
                              this.drawProgram = GLES20.glCreateProgram();
                              GLES20.glAttachShader(this.drawProgram, var2);
                              GLES20.glAttachShader(this.drawProgram, var1);
                              GLES20.glLinkProgram(this.drawProgram);
                              var21 = new int[1];
                              GLES20.glGetProgramiv(this.drawProgram, 35714, var21, 0);
                              if (var21[0] == 0) {
                                 GLES20.glDeleteProgram(this.drawProgram);
                                 this.drawProgram = 0;
                              } else {
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
                     } else {
                        throw new RuntimeException("surface was null");
                     }
                  } else {
                     throw new IllegalStateException("surface already created");
                  }
               } else {
                  this.eglDisplay = null;
                  throw new RuntimeException("unable to initialize EGL14");
               }
            } else {
               throw new RuntimeException("unable to get EGL14 display");
            }
         } else {
            throw new RuntimeException("EGL already set up");
         }
      }

      public void drainEncoder(boolean var1) throws Exception {
         if (var1) {
            this.videoEncoder.signalEndOfInputStream();
         }

         ByteBuffer[] var2;
         if (VERSION.SDK_INT < 21) {
            var2 = this.videoEncoder.getOutputBuffers();
         } else {
            var2 = null;
         }

         ByteBuffer[] var4;
         MediaFormat var5;
         int var6;
         long var7;
         BufferInfo var11;
         ByteBuffer var13;
         StringBuilder var14;
         while(true) {
            int var3 = this.videoEncoder.dequeueOutputBuffer(this.videoBufferInfo, 10000L);
            if (var3 == -1) {
               var4 = var2;
               if (!var1) {
                  break;
               }
            } else if (var3 == -3) {
               var4 = var2;
               if (VERSION.SDK_INT < 21) {
                  var4 = this.videoEncoder.getOutputBuffers();
               }
            } else if (var3 == -2) {
               var5 = this.videoEncoder.getOutputFormat();
               var4 = var2;
               if (this.videoTrackIndex == -5) {
                  this.videoTrackIndex = this.mediaMuxer.addTrack(var5, false);
                  var4 = var2;
               }
            } else {
               var4 = var2;
               if (var3 >= 0) {
                  if (VERSION.SDK_INT < 21) {
                     var13 = var2[var3];
                  } else {
                     var13 = this.videoEncoder.getOutputBuffer(var3);
                  }

                  if (var13 == null) {
                     var14 = new StringBuilder();
                     var14.append("encoderOutputBuffer ");
                     var14.append(var3);
                     var14.append(" was null");
                     throw new RuntimeException(var14.toString());
                  }

                  var11 = this.videoBufferInfo;
                  var6 = var11.size;
                  if (var6 > 1) {
                     if ((var11.flags & 2) == 0) {
                        var7 = this.mediaMuxer.writeSampleData(this.videoTrackIndex, var13, var11, true);
                        if (var7 != 0L) {
                           this.didWriteData(this.videoFile, var7, false);
                        }
                     } else if (this.videoTrackIndex == -5) {
                        byte[] var9 = new byte[var6];
                        var13.limit(var11.offset + var6);
                        var13.position(this.videoBufferInfo.offset);
                        var13.get(var9);
                        var6 = this.videoBufferInfo.size - 1;

                        ByteBuffer var12;
                        while(true) {
                           if (var6 < 0 || var6 <= 3) {
                              var13 = null;
                              var12 = null;
                              break;
                           }

                           if (var9[var6] == 1 && var9[var6 - 1] == 0 && var9[var6 - 2] == 0) {
                              int var10 = var6 - 3;
                              if (var9[var10] == 0) {
                                 var13 = ByteBuffer.allocate(var10);
                                 var12 = ByteBuffer.allocate(this.videoBufferInfo.size - var10);
                                 var13.put(var9, 0, var10).position(0);
                                 var12.put(var9, var10, this.videoBufferInfo.size - var10).position(0);
                                 break;
                              }
                           }

                           --var6;
                        }

                        MediaFormat var15 = MediaFormat.createVideoFormat("video/avc", this.videoWidth, this.videoHeight);
                        if (var13 != null && var12 != null) {
                           var15.setByteBuffer("csd-0", var13);
                           var15.setByteBuffer("csd-1", var12);
                        }

                        this.videoTrackIndex = this.mediaMuxer.addTrack(var15, false);
                     }
                  }

                  this.videoEncoder.releaseOutputBuffer(var3, false);
                  var4 = var2;
                  if ((this.videoBufferInfo.flags & 4) != 0) {
                     break;
                  }
               }
            }

            var2 = var4;
         }

         if (VERSION.SDK_INT < 21) {
            var2 = this.audioEncoder.getOutputBuffers();
         }

         while(true) {
            var6 = this.audioEncoder.dequeueOutputBuffer(this.audioBufferInfo, 0L);
            if (var6 == -1) {
               if (!var1) {
                  break;
               }

               var4 = var2;
               if (!this.running) {
                  var4 = var2;
                  if (this.sendWhenDone == 0) {
                     break;
                  }
               }
            } else if (var6 == -3) {
               var4 = var2;
               if (VERSION.SDK_INT < 21) {
                  var4 = this.audioEncoder.getOutputBuffers();
               }
            } else if (var6 == -2) {
               var5 = this.audioEncoder.getOutputFormat();
               var4 = var2;
               if (this.audioTrackIndex == -5) {
                  this.audioTrackIndex = this.mediaMuxer.addTrack(var5, true);
                  var4 = var2;
               }
            } else {
               var4 = var2;
               if (var6 >= 0) {
                  if (VERSION.SDK_INT < 21) {
                     var13 = var2[var6];
                  } else {
                     var13 = this.audioEncoder.getOutputBuffer(var6);
                  }

                  if (var13 == null) {
                     var14 = new StringBuilder();
                     var14.append("encoderOutputBuffer ");
                     var14.append(var6);
                     var14.append(" was null");
                     throw new RuntimeException(var14.toString());
                  }

                  var11 = this.audioBufferInfo;
                  if ((var11.flags & 2) != 0) {
                     var11.size = 0;
                  }

                  var11 = this.audioBufferInfo;
                  if (var11.size != 0) {
                     var7 = this.mediaMuxer.writeSampleData(this.audioTrackIndex, var13, var11, false);
                     if (var7 != 0L) {
                        this.didWriteData(this.videoFile, var7, false);
                     }
                  }

                  this.audioEncoder.releaseOutputBuffer(var6, false);
                  var4 = var2;
                  if ((this.audioBufferInfo.flags & 4) != 0) {
                     break;
                  }
               }
            }

            var2 = var4;
         }

      }

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
         } finally {
            super.finalize();
         }

      }

      public void frameAvailable(SurfaceTexture var1, Integer var2, long var3) {
         Object var5 = this.sync;
         synchronized(var5){}

         Throwable var10000;
         boolean var10001;
         label181: {
            try {
               if (!this.ready) {
                  return;
               }
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label181;
            }

            try {
               ;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label181;
            }

            long var6 = var1.getTimestamp();
            if (var6 == 0L) {
               ++this.zeroTimeStamps;
               if (this.zeroTimeStamps <= 1) {
                  return;
               }

               var6 = var3;
               if (BuildVars.LOGS_ENABLED) {
                  FileLog.d("fix timestamp enabled");
                  var6 = var3;
               }
            } else {
               this.zeroTimeStamps = 0;
            }

            this.handler.sendMessage(this.handler.obtainMessage(2, (int)(var6 >> 32), (int)var6, var2));
            return;
         }

         while(true) {
            Throwable var20 = var10000;

            try {
               throw var20;
            } catch (Throwable var17) {
               var10000 = var17;
               var10001 = false;
               continue;
            }
         }
      }

      public Surface getInputSurface() {
         return this.surface;
      }

      // $FF: synthetic method
      public void lambda$handleStopRecording$2$InstantCameraView$VideoRecorder(int var1) {
         InstantCameraView.this.videoEditedInfo = new VideoEditedInfo();
         InstantCameraView.this.videoEditedInfo.roundVideo = true;
         InstantCameraView.this.videoEditedInfo.startTime = -1L;
         InstantCameraView.this.videoEditedInfo.endTime = -1L;
         InstantCameraView.this.videoEditedInfo.file = InstantCameraView.this.file;
         InstantCameraView.this.videoEditedInfo.encryptedFile = InstantCameraView.this.encryptedFile;
         InstantCameraView.this.videoEditedInfo.key = InstantCameraView.this.key;
         InstantCameraView.this.videoEditedInfo.iv = InstantCameraView.this.iv;
         InstantCameraView.this.videoEditedInfo.estimatedSize = Math.max(1L, InstantCameraView.this.size);
         InstantCameraView.this.videoEditedInfo.framerate = 25;
         VideoEditedInfo var2 = InstantCameraView.this.videoEditedInfo;
         InstantCameraView.this.videoEditedInfo.originalWidth = 240;
         var2.resultWidth = 240;
         var2 = InstantCameraView.this.videoEditedInfo;
         InstantCameraView.this.videoEditedInfo.originalHeight = 240;
         var2.resultHeight = 240;
         InstantCameraView.this.videoEditedInfo.originalPath = this.videoFile.getAbsolutePath();
         if (var1 == 1) {
            InstantCameraView.this.baseFragment.sendMedia(new MediaController.PhotoEntry(0, 0, 0L, this.videoFile.getAbsolutePath(), 0, true), InstantCameraView.this.videoEditedInfo);
         } else {
            InstantCameraView.this.videoPlayer = new VideoPlayer();
            InstantCameraView.this.videoPlayer.setDelegate(new InstantCameraView$VideoRecorder$2(this));
            InstantCameraView.this.videoPlayer.setTextureView(InstantCameraView.this.textureView);
            InstantCameraView.this.videoPlayer.preparePlayer(Uri.fromFile(this.videoFile), "other");
            InstantCameraView.this.videoPlayer.play();
            InstantCameraView.this.videoPlayer.setMute(true);
            InstantCameraView.this.startProgressTimer();
            AnimatorSet var3 = new AnimatorSet();
            var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(InstantCameraView.this.switchCameraButton, "alpha", new float[]{0.0F}), ObjectAnimator.ofInt(InstantCameraView.this.paint, "alpha", new int[]{0}), ObjectAnimator.ofFloat(InstantCameraView.this.muteImageView, "alpha", new float[]{1.0F})});
            var3.setDuration(180L);
            var3.setInterpolator(new DecelerateInterpolator());
            var3.start();
            InstantCameraView.this.videoEditedInfo.estimatedDuration = InstantCameraView.this.duration;
            NotificationCenter.getInstance(InstantCameraView.this.currentAccount).postNotificationName(NotificationCenter.audioDidSent, InstantCameraView.this.videoEditedInfo, this.videoFile.getAbsolutePath());
         }

         this.didWriteData(this.videoFile, 0L, true);
      }

      // $FF: synthetic method
      public void lambda$handleVideoFrameAvailable$0$InstantCameraView$VideoRecorder() {
         InstantCameraView.this.textureOverlayView.animate().setDuration(120L).alpha(0.0F).setInterpolator(new DecelerateInterpolator()).start();
      }

      // $FF: synthetic method
      public void lambda$handleVideoFrameAvailable$1$InstantCameraView$VideoRecorder() {
         InstantCameraView.this.textureOverlayView.animate().setDuration(120L).alpha(0.0F).setInterpolator(new DecelerateInterpolator()).start();
      }

      // $FF: synthetic method
      public void lambda$prepareEncoder$3$InstantCameraView$VideoRecorder() {
         if (!InstantCameraView.this.cancelled) {
            try {
               InstantCameraView.this.performHapticFeedback(3, 2);
            } catch (Exception var2) {
            }

            AndroidUtilities.lockOrientation(InstantCameraView.this.baseFragment.getParentActivity());
            InstantCameraView.this.recording = true;
            InstantCameraView.this.recordStartTime = System.currentTimeMillis();
            AndroidUtilities.runOnUIThread(InstantCameraView.this.timerRunnable);
            NotificationCenter.getInstance(InstantCameraView.this.currentAccount).postNotificationName(NotificationCenter.recordStarted);
         }
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }

      public void startRecording(File param1, android.opengl.EGLContext param2) {
         // $FF: Couldn't be decompiled
      }

      public void stopRecording(int var1) {
         this.handler.sendMessage(this.handler.obtainMessage(1, var1, 0));
      }
   }
}
