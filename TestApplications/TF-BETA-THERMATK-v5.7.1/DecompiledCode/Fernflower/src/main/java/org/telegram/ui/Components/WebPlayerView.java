package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Bitmaps;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;

public class WebPlayerView extends ViewGroup implements VideoPlayer.VideoPlayerDelegate, OnAudioFocusChangeListener {
   private static final int AUDIO_FOCUSED = 2;
   private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
   private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
   private static final Pattern aparatFileListPattern = Pattern.compile("fileList\\s*=\\s*JSON\\.parse\\('([^']+)'\\)");
   private static final Pattern aparatIdRegex = Pattern.compile("^https?://(?:www\\.)?aparat\\.com/(?:v/|video/video/embed/videohash/)([a-zA-Z0-9]+)");
   private static final Pattern coubIdRegex = Pattern.compile("(?:coub:|https?://(?:coub\\.com/(?:view|embed|coubs)/|c-cdn\\.coub\\.com/fb-player\\.swf\\?.*\\bcoub(?:ID|id)=))([\\da-z]+)");
   private static final String exprName = "[a-zA-Z_$][a-zA-Z_$0-9]*";
   private static final Pattern exprParensPattern = Pattern.compile("[()]");
   private static final Pattern jsPattern = Pattern.compile("\"assets\":.+?\"js\":\\s*(\"[^\"]+\")");
   private static int lastContainerId;
   private static final Pattern playerIdPattern = Pattern.compile(".*?-([a-zA-Z0-9_-]+)(?:/watch_as3|/html5player(?:-new)?|(?:/[a-z]{2}_[A-Z]{2})?/base)?\\.([a-z]+)$");
   private static final Pattern sigPattern = Pattern.compile("\\.sig\\|\\|([a-zA-Z0-9$]+)\\(");
   private static final Pattern sigPattern2 = Pattern.compile("[\"']signature[\"']\\s*,\\s*([a-zA-Z0-9$]+)\\(");
   private static final Pattern stmtReturnPattern = Pattern.compile("return(?:\\s+|$)");
   private static final Pattern stmtVarPattern = Pattern.compile("var\\s");
   private static final Pattern stsPattern = Pattern.compile("\"sts\"\\s*:\\s*(\\d+)");
   private static final Pattern twitchClipFilePattern = Pattern.compile("clipInfo\\s*=\\s*(\\{[^']+\\});");
   private static final Pattern twitchClipIdRegex = Pattern.compile("https?://clips\\.twitch\\.tv/(?:[^/]+/)*([^/?#&]+)");
   private static final Pattern twitchStreamIdRegex = Pattern.compile("https?://(?:(?:www\\.)?twitch\\.tv/|player\\.twitch\\.tv/\\?.*?\\bchannel=)([^/#?]+)");
   private static final Pattern vimeoIdRegex = Pattern.compile("https?://(?:(?:www|(player))\\.)?vimeo(pro)?\\.com/(?!(?:channels|album)/[^/?#]+/?(?:$|[?#])|[^/]+/review/|ondemand/)(?:.*?/)?(?:(?:play_redirect_hls|moogaloop\\.swf)\\?clip_id=)?(?:videos?/)?([0-9]+)(?:/[\\da-f]+)?/?(?:[?&].*)?(?:[#].*)?$");
   private static final Pattern youtubeIdRegex = Pattern.compile("(?:youtube(?:-nocookie)?\\.com/(?:[^/\\n\\s]+/\\S+/|(?:v|e(?:mbed)?)/|\\S*?[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
   private boolean allowInlineAnimation;
   private AspectRatioFrameLayout aspectRatioFrameLayout;
   private int audioFocus;
   private Paint backgroundPaint;
   private TextureView changedTextureView;
   private boolean changingTextureView;
   private WebPlayerView.ControlsView controlsView;
   private float currentAlpha;
   private Bitmap currentBitmap;
   private AsyncTask currentTask;
   private String currentYoutubeId;
   private WebPlayerView.WebPlayerViewDelegate delegate;
   private boolean drawImage;
   private boolean firstFrameRendered;
   private int fragment_container_id;
   private ImageView fullscreenButton;
   private boolean hasAudioFocus;
   private boolean inFullscreen;
   private boolean initFailed;
   private boolean initied;
   private ImageView inlineButton;
   private String interfaceName;
   private boolean isAutoplay;
   private boolean isCompleted;
   private boolean isInline;
   private boolean isLoading;
   private boolean isStream;
   private long lastUpdateTime;
   private String playAudioType;
   private String playAudioUrl;
   private ImageView playButton;
   private String playVideoType;
   private String playVideoUrl;
   private AnimatorSet progressAnimation;
   private Runnable progressRunnable;
   private RadialProgressView progressView;
   private boolean resumeAudioOnFocusGain;
   private int seekToTime;
   private ImageView shareButton;
   private SurfaceTextureListener surfaceTextureListener;
   private Runnable switchToInlineRunnable;
   private boolean switchingInlineMode;
   private ImageView textureImageView;
   private TextureView textureView;
   private ViewGroup textureViewContainer;
   private VideoPlayer videoPlayer;
   private int waitingForFirstTextureUpload;
   private WebView webView;

   @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
   public WebPlayerView(Context var1, boolean var2, boolean var3, WebPlayerView.WebPlayerViewDelegate var4) {
      super(var1);
      int var5 = lastContainerId++;
      this.fragment_container_id = var5;
      boolean var6;
      if (VERSION.SDK_INT >= 21) {
         var6 = true;
      } else {
         var6 = false;
      }

      this.allowInlineAnimation = var6;
      this.backgroundPaint = new Paint();
      this.progressRunnable = new Runnable() {
         public void run() {
            if (WebPlayerView.this.videoPlayer != null && WebPlayerView.this.videoPlayer.isPlaying()) {
               WebPlayerView.this.controlsView.setProgress((int)(WebPlayerView.this.videoPlayer.getCurrentPosition() / 1000L));
               WebPlayerView.this.controlsView.setBufferedProgress((int)(WebPlayerView.this.videoPlayer.getBufferedPosition() / 1000L));
               AndroidUtilities.runOnUIThread(WebPlayerView.this.progressRunnable, 1000L);
            }

         }
      };
      this.surfaceTextureListener = new SurfaceTextureListener() {
         public void onSurfaceTextureAvailable(SurfaceTexture var1, int var2, int var3) {
         }

         public boolean onSurfaceTextureDestroyed(SurfaceTexture var1) {
            if (WebPlayerView.this.changingTextureView) {
               if (WebPlayerView.this.switchingInlineMode) {
                  WebPlayerView.this.waitingForFirstTextureUpload = 2;
               }

               WebPlayerView.this.textureView.setSurfaceTexture(var1);
               WebPlayerView.this.textureView.setVisibility(0);
               WebPlayerView.this.changingTextureView = false;
               return false;
            } else {
               return true;
            }
         }

         public void onSurfaceTextureSizeChanged(SurfaceTexture var1, int var2, int var3) {
         }

         public void onSurfaceTextureUpdated(SurfaceTexture var1) {
            if (WebPlayerView.this.waitingForFirstTextureUpload == 1) {
               WebPlayerView.this.changedTextureView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                  // $FF: synthetic method
                  public void lambda$onPreDraw$0$WebPlayerView$2$1() {
                     WebPlayerView.this.delegate.onInlineSurfaceTextureReady();
                  }

                  public boolean onPreDraw() {
                     WebPlayerView.this.changedTextureView.getViewTreeObserver().removeOnPreDrawListener(this);
                     if (WebPlayerView.this.textureImageView != null) {
                        WebPlayerView.this.textureImageView.setVisibility(4);
                        WebPlayerView.this.textureImageView.setImageDrawable((Drawable)null);
                        if (WebPlayerView.this.currentBitmap != null) {
                           WebPlayerView.this.currentBitmap.recycle();
                           WebPlayerView.this.currentBitmap = null;
                        }
                     }

                     AndroidUtilities.runOnUIThread(new _$$Lambda$WebPlayerView$2$1$XktWEKlh2pqd2EX_7__wMiImvaQ(this));
                     WebPlayerView.this.waitingForFirstTextureUpload = 0;
                     return true;
                  }
               });
               WebPlayerView.this.changedTextureView.invalidate();
            }

         }
      };
      this.switchToInlineRunnable = new Runnable() {
         public void run() {
            WebPlayerView.this.switchingInlineMode = false;
            if (WebPlayerView.this.currentBitmap != null) {
               WebPlayerView.this.currentBitmap.recycle();
               WebPlayerView.this.currentBitmap = null;
            }

            WebPlayerView.this.changingTextureView = true;
            if (WebPlayerView.this.textureImageView != null) {
               try {
                  WebPlayerView.this.currentBitmap = Bitmaps.createBitmap(WebPlayerView.this.textureView.getWidth(), WebPlayerView.this.textureView.getHeight(), Config.ARGB_8888);
                  WebPlayerView.this.textureView.getBitmap(WebPlayerView.this.currentBitmap);
               } catch (Throwable var2) {
                  if (WebPlayerView.this.currentBitmap != null) {
                     WebPlayerView.this.currentBitmap.recycle();
                     WebPlayerView.this.currentBitmap = null;
                  }

                  FileLog.e(var2);
               }

               if (WebPlayerView.this.currentBitmap != null) {
                  WebPlayerView.this.textureImageView.setVisibility(0);
                  WebPlayerView.this.textureImageView.setImageBitmap(WebPlayerView.this.currentBitmap);
               } else {
                  WebPlayerView.this.textureImageView.setImageDrawable((Drawable)null);
               }
            }

            WebPlayerView.this.isInline = true;
            WebPlayerView.this.updatePlayButton();
            WebPlayerView.this.updateShareButton();
            WebPlayerView.this.updateFullscreenButton();
            WebPlayerView.this.updateInlineButton();
            ViewGroup var1 = (ViewGroup)WebPlayerView.this.controlsView.getParent();
            if (var1 != null) {
               var1.removeView(WebPlayerView.this.controlsView);
            }

            WebPlayerView var3 = WebPlayerView.this;
            var3.changedTextureView = var3.delegate.onSwitchInlineMode(WebPlayerView.this.controlsView, WebPlayerView.this.isInline, WebPlayerView.this.aspectRatioFrameLayout.getAspectRatio(), WebPlayerView.this.aspectRatioFrameLayout.getVideoRotation(), WebPlayerView.this.allowInlineAnimation);
            WebPlayerView.this.changedTextureView.setVisibility(4);
            var1 = (ViewGroup)WebPlayerView.this.textureView.getParent();
            if (var1 != null) {
               var1.removeView(WebPlayerView.this.textureView);
            }

            WebPlayerView.this.controlsView.show(false, false);
         }
      };
      this.setWillNotDraw(false);
      this.delegate = var4;
      this.backgroundPaint.setColor(-16777216);
      this.aspectRatioFrameLayout = new AspectRatioFrameLayout(var1) {
         protected void onMeasure(int var1, int var2) {
            super.onMeasure(var1, var2);
            if (WebPlayerView.this.textureViewContainer != null) {
               LayoutParams var3 = WebPlayerView.this.textureView.getLayoutParams();
               var3.width = this.getMeasuredWidth();
               var3.height = this.getMeasuredHeight();
               if (WebPlayerView.this.textureImageView != null) {
                  var3 = WebPlayerView.this.textureImageView.getLayoutParams();
                  var3.width = this.getMeasuredWidth();
                  var3.height = this.getMeasuredHeight();
               }
            }

         }
      };
      this.addView(this.aspectRatioFrameLayout, LayoutHelper.createFrame(-1, -1, 17));
      this.interfaceName = "JavaScriptInterface";
      this.webView = new WebView(var1);
      this.webView.addJavascriptInterface(new WebPlayerView.JavaScriptInterface(new _$$Lambda$WebPlayerView$OTCqcKUzHnpxut9IWnkU_zTMUYs(this)), this.interfaceName);
      WebSettings var7 = this.webView.getSettings();
      var7.setJavaScriptEnabled(true);
      var7.setDefaultTextEncodingName("utf-8");
      this.textureViewContainer = this.delegate.getTextureViewContainer();
      this.textureView = new TextureView(var1);
      this.textureView.setPivotX(0.0F);
      this.textureView.setPivotY(0.0F);
      ViewGroup var8 = this.textureViewContainer;
      if (var8 != null) {
         var8.addView(this.textureView);
      } else {
         this.aspectRatioFrameLayout.addView(this.textureView, LayoutHelper.createFrame(-1, -1, 17));
      }

      if (this.allowInlineAnimation && this.textureViewContainer != null) {
         this.textureImageView = new ImageView(var1);
         this.textureImageView.setBackgroundColor(-65536);
         this.textureImageView.setPivotX(0.0F);
         this.textureImageView.setPivotY(0.0F);
         this.textureImageView.setVisibility(4);
         this.textureViewContainer.addView(this.textureImageView);
      }

      this.videoPlayer = new VideoPlayer();
      this.videoPlayer.setDelegate(this);
      this.videoPlayer.setTextureView(this.textureView);
      this.controlsView = new WebPlayerView.ControlsView(var1);
      var8 = this.textureViewContainer;
      if (var8 != null) {
         var8.addView(this.controlsView);
      } else {
         this.addView(this.controlsView, LayoutHelper.createFrame(-1, -1.0F));
      }

      this.progressView = new RadialProgressView(var1);
      this.progressView.setProgressColor(-1);
      this.addView(this.progressView, LayoutHelper.createFrame(48, 48, 17));
      this.fullscreenButton = new ImageView(var1);
      this.fullscreenButton.setScaleType(ScaleType.CENTER);
      this.controlsView.addView(this.fullscreenButton, LayoutHelper.createFrame(56, 56.0F, 85, 0.0F, 0.0F, 0.0F, 5.0F));
      this.fullscreenButton.setOnClickListener(new _$$Lambda$WebPlayerView$W2P4sWOYF2snToxNUtlCSP61A2U(this));
      this.playButton = new ImageView(var1);
      this.playButton.setScaleType(ScaleType.CENTER);
      this.controlsView.addView(this.playButton, LayoutHelper.createFrame(48, 48, 17));
      this.playButton.setOnClickListener(new _$$Lambda$WebPlayerView$8RPE5WMQJ4Qql9XUXKDSHuRZ8yA(this));
      if (var2) {
         this.inlineButton = new ImageView(var1);
         this.inlineButton.setScaleType(ScaleType.CENTER);
         this.controlsView.addView(this.inlineButton, LayoutHelper.createFrame(56, 48, 53));
         this.inlineButton.setOnClickListener(new _$$Lambda$WebPlayerView$FgDS8XBnRLuQdgwn4r4TJJnIjOo(this));
      }

      if (var3) {
         this.shareButton = new ImageView(var1);
         this.shareButton.setScaleType(ScaleType.CENTER);
         this.shareButton.setImageResource(2131165470);
         this.controlsView.addView(this.shareButton, LayoutHelper.createFrame(56, 48, 53));
         this.shareButton.setOnClickListener(new _$$Lambda$WebPlayerView$tu1I4PMipEbSXLy2AXDs0pmTjdo(this));
      }

      this.updatePlayButton();
      this.updateFullscreenButton();
      this.updateInlineButton();
      this.updateShareButton();
   }

   private void checkAudioFocus() {
      if (!this.hasAudioFocus) {
         AudioManager var1 = (AudioManager)ApplicationLoader.applicationContext.getSystemService("audio");
         this.hasAudioFocus = true;
         if (var1.requestAudioFocus(this, 3, 1) == 1) {
            this.audioFocus = 2;
         }
      }

   }

   private View getControlView() {
      return this.controlsView;
   }

   private View getProgressView() {
      return this.progressView;
   }

   private void onInitFailed() {
      if (this.controlsView.getParent() != this) {
         this.controlsView.setVisibility(8);
      }

      this.delegate.onInitFailed();
   }

   private void preparePlayer() {
      String var1 = this.playVideoUrl;
      if (var1 != null) {
         if (var1 != null && this.playAudioUrl != null) {
            this.videoPlayer.preparePlayerLoop(Uri.parse(var1), this.playVideoType, Uri.parse(this.playAudioUrl), this.playAudioType);
         } else {
            this.videoPlayer.preparePlayer(Uri.parse(this.playVideoUrl), this.playVideoType);
         }

         this.videoPlayer.setPlayWhenReady(this.isAutoplay);
         this.isLoading = false;
         if (this.videoPlayer.getDuration() != -9223372036854775807L) {
            this.controlsView.setDuration((int)(this.videoPlayer.getDuration() / 1000L));
         } else {
            this.controlsView.setDuration(0);
         }

         this.updateFullscreenButton();
         this.updateShareButton();
         this.updateInlineButton();
         this.controlsView.invalidate();
         int var2 = this.seekToTime;
         if (var2 != -1) {
            this.videoPlayer.seekTo((long)(var2 * 1000));
         }

      }
   }

   private void showProgress(boolean var1, boolean var2) {
      float var3 = 1.0F;
      if (var2) {
         AnimatorSet var4 = this.progressAnimation;
         if (var4 != null) {
            var4.cancel();
         }

         this.progressAnimation = new AnimatorSet();
         var4 = this.progressAnimation;
         RadialProgressView var5 = this.progressView;
         if (!var1) {
            var3 = 0.0F;
         }

         var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(var5, "alpha", new float[]{var3})});
         this.progressAnimation.setDuration(150L);
         this.progressAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1) {
               WebPlayerView.this.progressAnimation = null;
            }
         });
         this.progressAnimation.start();
      } else {
         RadialProgressView var6 = this.progressView;
         if (!var1) {
            var3 = 0.0F;
         }

         var6.setAlpha(var3);
      }

   }

   private void updateFullscreenButton() {
      if (this.videoPlayer.isPlayerPrepared() && !this.isInline) {
         this.fullscreenButton.setVisibility(0);
         if (!this.inFullscreen) {
            this.fullscreenButton.setImageResource(2131165444);
            this.fullscreenButton.setLayoutParams(LayoutHelper.createFrame(56, 56.0F, 85, 0.0F, 0.0F, 0.0F, 5.0F));
         } else {
            this.fullscreenButton.setImageResource(2131165457);
            this.fullscreenButton.setLayoutParams(LayoutHelper.createFrame(56, 56.0F, 85, 0.0F, 0.0F, 0.0F, 1.0F));
         }

      } else {
         this.fullscreenButton.setVisibility(8);
      }
   }

   private void updateFullscreenState(boolean var1) {
      if (this.textureView != null) {
         this.updateFullscreenButton();
         ViewGroup var2 = this.textureViewContainer;
         if (var2 == null) {
            this.changingTextureView = true;
            if (!this.inFullscreen) {
               if (var2 != null) {
                  var2.addView(this.textureView);
               } else {
                  this.aspectRatioFrameLayout.addView(this.textureView);
               }
            }

            if (this.inFullscreen) {
               var2 = (ViewGroup)this.controlsView.getParent();
               if (var2 != null) {
                  var2.removeView(this.controlsView);
               }
            } else {
               var2 = (ViewGroup)this.controlsView.getParent();
               if (var2 != this) {
                  if (var2 != null) {
                     var2.removeView(this.controlsView);
                  }

                  var2 = this.textureViewContainer;
                  if (var2 != null) {
                     var2.addView(this.controlsView);
                  } else {
                     this.addView(this.controlsView, 1);
                  }
               }
            }

            this.changedTextureView = this.delegate.onSwitchToFullscreen(this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), var1);
            this.changedTextureView.setVisibility(4);
            if (this.inFullscreen && this.changedTextureView != null) {
               var2 = (ViewGroup)this.textureView.getParent();
               if (var2 != null) {
                  var2.removeView(this.textureView);
               }
            }

            this.controlsView.checkNeedHide();
         } else {
            if (this.inFullscreen) {
               var2 = (ViewGroup)this.aspectRatioFrameLayout.getParent();
               if (var2 != null) {
                  var2.removeView(this.aspectRatioFrameLayout);
               }
            } else {
               var2 = (ViewGroup)this.aspectRatioFrameLayout.getParent();
               if (var2 != this) {
                  if (var2 != null) {
                     var2.removeView(this.aspectRatioFrameLayout);
                  }

                  this.addView(this.aspectRatioFrameLayout, 0);
               }
            }

            this.delegate.onSwitchToFullscreen(this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), var1);
         }

      }
   }

   private void updateInlineButton() {
      ImageView var1 = this.inlineButton;
      if (var1 != null) {
         int var2;
         if (this.isInline) {
            var2 = 2131165445;
         } else {
            var2 = 2131165458;
         }

         var1.setImageResource(var2);
         var1 = this.inlineButton;
         byte var3;
         if (this.videoPlayer.isPlayerPrepared()) {
            var3 = 0;
         } else {
            var3 = 8;
         }

         var1.setVisibility(var3);
         if (this.isInline) {
            this.inlineButton.setLayoutParams(LayoutHelper.createFrame(40, 40, 53));
         } else {
            this.inlineButton.setLayoutParams(LayoutHelper.createFrame(56, 50, 53));
         }

      }
   }

   private void updatePlayButton() {
      this.controlsView.checkNeedHide();
      AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
      ImageView var1;
      int var2;
      if (!this.videoPlayer.isPlaying()) {
         if (this.isCompleted) {
            var1 = this.playButton;
            if (this.isInline) {
               var2 = 2131165426;
            } else {
               var2 = 2131165425;
            }

            var1.setImageResource(var2);
         } else {
            var1 = this.playButton;
            if (this.isInline) {
               var2 = 2131165464;
            } else {
               var2 = 2131165462;
            }

            var1.setImageResource(var2);
         }
      } else {
         var1 = this.playButton;
         if (this.isInline) {
            var2 = 2131165460;
         } else {
            var2 = 2131165459;
         }

         var1.setImageResource(var2);
         AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
         this.checkAudioFocus();
      }

   }

   private void updateShareButton() {
      ImageView var1 = this.shareButton;
      if (var1 != null) {
         byte var2;
         if (!this.isInline && this.videoPlayer.isPlayerPrepared()) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         var1.setVisibility(var2);
      }
   }

   public void destroy() {
      this.videoPlayer.releasePlayer(false);
      AsyncTask var1 = this.currentTask;
      if (var1 != null) {
         var1.cancel(true);
         this.currentTask = null;
      }

      this.webView.stopLoading();
   }

   protected String downloadUrlContent(AsyncTask var1, String var2) {
      return this.downloadUrlContent(var1, var2, (HashMap)null, true);
   }

   protected String downloadUrlContent(AsyncTask param1, String param2, HashMap param3, boolean param4) {
      // $FF: Couldn't be decompiled
   }

   public void enterFullscreen() {
      if (!this.inFullscreen) {
         this.inFullscreen = true;
         this.updateInlineButton();
         this.updateFullscreenState(false);
      }
   }

   public void exitFullscreen() {
      if (this.inFullscreen) {
         this.inFullscreen = false;
         this.updateInlineButton();
         this.updateFullscreenState(false);
      }
   }

   public View getAspectRatioView() {
      return this.aspectRatioFrameLayout;
   }

   public View getControlsView() {
      return this.controlsView;
   }

   public ImageView getTextureImageView() {
      return this.textureImageView;
   }

   public TextureView getTextureView() {
      return this.textureView;
   }

   public String getYoutubeId() {
      return this.currentYoutubeId;
   }

   public boolean isInFullscreen() {
      return this.inFullscreen;
   }

   public boolean isInitied() {
      return this.initied;
   }

   public boolean isInline() {
      boolean var1;
      if (!this.isInline && !this.switchingInlineMode) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   // $FF: synthetic method
   public void lambda$new$0$WebPlayerView(String var1) {
      AsyncTask var2 = this.currentTask;
      if (var2 != null && !var2.isCancelled()) {
         var2 = this.currentTask;
         if (var2 instanceof WebPlayerView.YoutubeVideoTask) {
            ((WebPlayerView.YoutubeVideoTask)var2).onInterfaceResult(var1);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$WebPlayerView(View var1) {
      if (this.initied && !this.changingTextureView && !this.switchingInlineMode && this.firstFrameRendered) {
         this.inFullscreen ^= true;
         this.updateFullscreenState(true);
      }

   }

   // $FF: synthetic method
   public void lambda$new$2$WebPlayerView(View var1) {
      if (this.initied && this.playVideoUrl != null) {
         if (!this.videoPlayer.isPlayerPrepared()) {
            this.preparePlayer();
         }

         if (this.videoPlayer.isPlaying()) {
            this.videoPlayer.pause();
         } else {
            this.isCompleted = false;
            this.videoPlayer.play();
         }

         this.updatePlayButton();
      }

   }

   // $FF: synthetic method
   public void lambda$new$3$WebPlayerView(View var1) {
      if (this.textureView != null && this.delegate.checkInlinePermissions() && !this.changingTextureView && !this.switchingInlineMode && this.firstFrameRendered) {
         this.switchingInlineMode = true;
         if (!this.isInline) {
            this.inFullscreen = false;
            this.delegate.prepareToSwitchInlineMode(true, this.switchToInlineRunnable, this.aspectRatioFrameLayout.getAspectRatio(), this.allowInlineAnimation);
         } else {
            ViewGroup var2 = (ViewGroup)this.aspectRatioFrameLayout.getParent();
            if (var2 != this) {
               if (var2 != null) {
                  var2.removeView(this.aspectRatioFrameLayout);
               }

               this.addView(this.aspectRatioFrameLayout, 0, LayoutHelper.createFrame(-1, -1, 17));
               this.aspectRatioFrameLayout.measure(MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight() - AndroidUtilities.dp(10.0F), 1073741824));
            }

            Bitmap var3 = this.currentBitmap;
            if (var3 != null) {
               var3.recycle();
               this.currentBitmap = null;
            }

            this.changingTextureView = true;
            this.isInline = false;
            this.updatePlayButton();
            this.updateShareButton();
            this.updateFullscreenButton();
            this.updateInlineButton();
            this.textureView.setVisibility(4);
            var2 = this.textureViewContainer;
            if (var2 != null) {
               var2.addView(this.textureView);
            } else {
               this.aspectRatioFrameLayout.addView(this.textureView);
            }

            var2 = (ViewGroup)this.controlsView.getParent();
            if (var2 != this) {
               if (var2 != null) {
                  var2.removeView(this.controlsView);
               }

               var2 = this.textureViewContainer;
               if (var2 != null) {
                  var2.addView(this.controlsView);
               } else {
                  this.addView(this.controlsView, 1);
               }
            }

            this.controlsView.show(false, false);
            this.delegate.prepareToSwitchInlineMode(false, (Runnable)null, this.aspectRatioFrameLayout.getAspectRatio(), this.allowInlineAnimation);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$new$4$WebPlayerView(View var1) {
      WebPlayerView.WebPlayerViewDelegate var2 = this.delegate;
      if (var2 != null) {
         var2.onSharePressed();
      }

   }

   public boolean loadVideo(String var1, TLRPC.Photo var2, Object var3, String var4, boolean var5) {
      String var7;
      String var8;
      String var9;
      String var10;
      String var13;
      String var35;
      label276: {
         this.seekToTime = -1;
         if (var1 != null) {
            if (!var1.endsWith(".mp4")) {
               Matcher var36;
               label280: {
                  label269: {
                     Exception var10000;
                     Exception var33;
                     label287: {
                        boolean var10001;
                        if (var4 != null) {
                           label286: {
                              label283: {
                                 Uri var6;
                                 try {
                                    var6 = Uri.parse(var4);
                                    var7 = var6.getQueryParameter("t");
                                 } catch (Exception var23) {
                                    var10000 = var23;
                                    var10001 = false;
                                    break label283;
                                 }

                                 var4 = var7;
                                 if (var7 == null) {
                                    try {
                                       var4 = var6.getQueryParameter("time_continue");
                                    } catch (Exception var22) {
                                       var10000 = var22;
                                       var10001 = false;
                                       break label283;
                                    }
                                 }

                                 if (var4 == null) {
                                    break label286;
                                 }

                                 try {
                                    if (var4.contains("m")) {
                                       String[] var34 = var4.split("m");
                                       this.seekToTime = Utilities.parseInt(var34[0]) * 60 + Utilities.parseInt(var34[1]);
                                       break label286;
                                    }
                                 } catch (Exception var24) {
                                    var10000 = var24;
                                    var10001 = false;
                                    break label283;
                                 }

                                 try {
                                    this.seekToTime = Utilities.parseInt(var4);
                                    break label286;
                                 } catch (Exception var21) {
                                    var10000 = var21;
                                    var10001 = false;
                                 }
                              }

                              var33 = var10000;

                              try {
                                 FileLog.e((Throwable)var33);
                              } catch (Exception var20) {
                                 var10000 = var20;
                                 var10001 = false;
                                 break label287;
                              }
                           }
                        }

                        try {
                           var36 = youtubeIdRegex.matcher(var1);
                           if (var36.find()) {
                              var4 = var36.group(1);
                              break label269;
                           }
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label287;
                        }

                        var4 = null;
                        break label269;
                     }

                     var33 = var10000;
                     FileLog.e((Throwable)var33);
                     var8 = null;
                     break label280;
                  }

                  if (var4 == null) {
                     var4 = null;
                  }

                  var8 = var4;
               }

               label234: {
                  if (var8 == null) {
                     label284: {
                        label231: {
                           try {
                              var36 = vimeoIdRegex.matcher(var1);
                              if (var36.find()) {
                                 var4 = var36.group(3);
                                 break label231;
                              }
                           } catch (Exception var18) {
                              FileLog.e((Throwable)var18);
                              break label284;
                           }

                           var4 = null;
                        }

                        if (var4 == null) {
                           var4 = null;
                        }

                        var9 = var4;
                        break label234;
                     }
                  }

                  var9 = null;
               }

               label222: {
                  if (var9 == null) {
                     label220: {
                        label219: {
                           try {
                              var36 = aparatIdRegex.matcher(var1);
                              if (var36.find()) {
                                 var4 = var36.group(1);
                                 break label219;
                              }
                           } catch (Exception var17) {
                              FileLog.e((Throwable)var17);
                              break label220;
                           }

                           var4 = null;
                        }

                        if (var4 == null) {
                           var4 = null;
                        }
                        break label222;
                     }
                  }

                  var4 = null;
               }

               label210: {
                  if (var4 == null) {
                     label208: {
                        label207: {
                           try {
                              Matcher var38 = twitchClipIdRegex.matcher(var1);
                              if (var38.find()) {
                                 var7 = var38.group(1);
                                 break label207;
                              }
                           } catch (Exception var16) {
                              FileLog.e((Throwable)var16);
                              break label208;
                           }

                           var7 = null;
                        }

                        if (var7 == null) {
                           var7 = null;
                        }
                        break label210;
                     }
                  }

                  var7 = null;
               }

               label198: {
                  if (var7 == null) {
                     label196: {
                        label195: {
                           try {
                              Matcher var37 = twitchStreamIdRegex.matcher(var1);
                              if (var37.find()) {
                                 var35 = var37.group(1);
                                 break label195;
                              }
                           } catch (Exception var15) {
                              FileLog.e((Throwable)var15);
                              break label196;
                           }

                           var35 = null;
                        }

                        if (var35 == null) {
                           var35 = null;
                        }
                        break label198;
                     }
                  }

                  var35 = null;
               }

               String var11;
               String var12;
               if (var35 == null) {
                  label285: {
                     label184: {
                        try {
                           Matcher var39 = coubIdRegex.matcher(var1);
                           if (var39.find()) {
                              var10 = var39.group(1);
                              break label184;
                           }
                        } catch (Exception var14) {
                           FileLog.e((Throwable)var14);
                           break label285;
                        }

                        var10 = null;
                     }

                     if (var10 == null) {
                        var10 = null;
                     }

                     var11 = var35;
                     var12 = var7;
                     var35 = var4;
                     var7 = var10;
                     var4 = null;
                     var10 = var8;
                     var13 = var9;
                     var8 = var12;
                     var9 = var11;
                     break label276;
                  }
               }

               var10 = null;
               var11 = var35;
               var12 = var7;
               var35 = var4;
               var7 = var10;
               var4 = var10;
               var10 = var8;
               var13 = var9;
               var8 = var12;
               var9 = var11;
               break label276;
            }

            var4 = var1;
         } else {
            var4 = null;
         }

         var10 = null;
         var13 = null;
         var7 = var13;
         var35 = var13;
         var8 = var13;
         var9 = var13;
      }

      this.initied = false;
      this.isCompleted = false;
      this.isAutoplay = var5;
      this.playVideoUrl = null;
      this.playAudioUrl = null;
      this.destroy();
      this.firstFrameRendered = false;
      this.currentAlpha = 1.0F;
      AsyncTask var40 = this.currentTask;
      if (var40 != null) {
         var40.cancel(true);
         this.currentTask = null;
      }

      this.updateFullscreenButton();
      this.updateShareButton();
      this.updateInlineButton();
      this.updatePlayButton();
      if (var2 != null) {
         TLRPC.PhotoSize var41 = FileLoader.getClosestPhotoSizeWithSize(var2.sizes, 80, true);
         if (var41 != null) {
            this.controlsView.imageReceiver.setImage((ImageLocation)null, (String)null, ImageLocation.getForPhoto(var41, var2), "80_80_b", 0, (String)null, var3, 1);
            this.drawImage = true;
         }
      } else {
         this.drawImage = false;
      }

      AnimatorSet var28 = this.progressAnimation;
      if (var28 != null) {
         var28.cancel();
         this.progressAnimation = null;
      }

      this.isLoading = true;
      this.controlsView.setProgress(0);
      String var30 = var10;
      if (var10 != null) {
         this.currentYoutubeId = var10;
         var30 = null;
      }

      if (var4 != null) {
         this.initied = true;
         this.playVideoUrl = var4;
         this.playVideoType = "other";
         if (this.isAutoplay) {
            this.preparePlayer();
         }

         this.showProgress(false, false);
         this.controlsView.show(true, true);
      } else {
         if (var30 != null) {
            WebPlayerView.YoutubeVideoTask var25 = new WebPlayerView.YoutubeVideoTask(var30);
            var25.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentTask = var25;
         } else if (var13 != null) {
            WebPlayerView.VimeoVideoTask var26 = new WebPlayerView.VimeoVideoTask(var13);
            var26.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentTask = var26;
         } else if (var7 != null) {
            WebPlayerView.CoubVideoTask var27 = new WebPlayerView.CoubVideoTask(var7);
            var27.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentTask = var27;
            this.isStream = true;
         } else if (var35 != null) {
            WebPlayerView.AparatVideoTask var29 = new WebPlayerView.AparatVideoTask(var35);
            var29.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentTask = var29;
         } else if (var8 != null) {
            WebPlayerView.TwitchClipVideoTask var31 = new WebPlayerView.TwitchClipVideoTask(var1, var8);
            var31.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentTask = var31;
         } else if (var9 != null) {
            WebPlayerView.TwitchStreamVideoTask var32 = new WebPlayerView.TwitchStreamVideoTask(var1, var9);
            var32.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[]{null, null, null});
            this.currentTask = var32;
            this.isStream = true;
         }

         this.controlsView.show(false, false);
         this.showProgress(true, false);
      }

      if (var30 == null && var13 == null && var7 == null && var35 == null && var4 == null && var8 == null && var9 == null) {
         this.controlsView.setVisibility(8);
         return false;
      } else {
         this.controlsView.setVisibility(0);
         return true;
      }
   }

   public void onAudioFocusChange(int var1) {
      if (var1 == -1) {
         if (this.videoPlayer.isPlaying()) {
            this.videoPlayer.pause();
            this.updatePlayButton();
         }

         this.hasAudioFocus = false;
         this.audioFocus = 0;
      } else if (var1 == 1) {
         this.audioFocus = 2;
         if (this.resumeAudioOnFocusGain) {
            this.resumeAudioOnFocusGain = false;
            this.videoPlayer.play();
         }
      } else if (var1 == -3) {
         this.audioFocus = 1;
      } else if (var1 == -2) {
         this.audioFocus = 0;
         if (this.videoPlayer.isPlaying()) {
            this.resumeAudioOnFocusGain = true;
            this.videoPlayer.pause();
            this.updatePlayButton();
         }
      }

   }

   protected void onDraw(Canvas var1) {
      var1.drawRect(0.0F, 0.0F, (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(10.0F)), this.backgroundPaint);
   }

   public void onError(Exception var1) {
      FileLog.e((Throwable)var1);
      this.onInitFailed();
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      var4 -= var2;
      var2 = (var4 - this.aspectRatioFrameLayout.getMeasuredWidth()) / 2;
      var3 = var5 - var3;
      var5 = (var3 - AndroidUtilities.dp(10.0F) - this.aspectRatioFrameLayout.getMeasuredHeight()) / 2;
      AspectRatioFrameLayout var6 = this.aspectRatioFrameLayout;
      var6.layout(var2, var5, var6.getMeasuredWidth() + var2, this.aspectRatioFrameLayout.getMeasuredHeight() + var5);
      if (this.controlsView.getParent() == this) {
         WebPlayerView.ControlsView var7 = this.controlsView;
         var7.layout(0, 0, var7.getMeasuredWidth(), this.controlsView.getMeasuredHeight());
      }

      var2 = (var4 - this.progressView.getMeasuredWidth()) / 2;
      var3 = (var3 - this.progressView.getMeasuredHeight()) / 2;
      RadialProgressView var8 = this.progressView;
      var8.layout(var2, var3, var8.getMeasuredWidth() + var2, this.progressView.getMeasuredHeight() + var3);
      this.controlsView.imageReceiver.setImageCoords(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight() - AndroidUtilities.dp(10.0F));
   }

   protected void onMeasure(int var1, int var2) {
      var1 = MeasureSpec.getSize(var1);
      var2 = MeasureSpec.getSize(var2);
      this.aspectRatioFrameLayout.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2 - AndroidUtilities.dp(10.0F), 1073741824));
      if (this.controlsView.getParent() == this) {
         this.controlsView.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
      }

      this.progressView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0F), 1073741824));
      this.setMeasuredDimension(var1, var2);
   }

   public void onRenderedFirstFrame() {
      this.firstFrameRendered = true;
      this.lastUpdateTime = System.currentTimeMillis();
      this.controlsView.invalidate();
   }

   public void onStateChanged(boolean var1, int var2) {
      if (var2 != 2) {
         if (this.videoPlayer.getDuration() != -9223372036854775807L) {
            this.controlsView.setDuration((int)(this.videoPlayer.getDuration() / 1000L));
         } else {
            this.controlsView.setDuration(0);
         }
      }

      if (var2 != 4 && var2 != 1 && this.videoPlayer.isPlaying()) {
         this.delegate.onPlayStateChanged(this, true);
      } else {
         this.delegate.onPlayStateChanged(this, false);
      }

      if (this.videoPlayer.isPlaying() && var2 != 4) {
         this.updatePlayButton();
      } else if (var2 == 4) {
         this.isCompleted = true;
         this.videoPlayer.pause();
         this.videoPlayer.seekTo(0L);
         this.updatePlayButton();
         this.controlsView.show(true, true);
      }

   }

   public boolean onSurfaceDestroyed(SurfaceTexture var1) {
      if (this.changingTextureView) {
         this.changingTextureView = false;
         if (this.inFullscreen || this.isInline) {
            if (this.isInline) {
               this.waitingForFirstTextureUpload = 1;
            }

            this.changedTextureView.setSurfaceTexture(var1);
            this.changedTextureView.setSurfaceTextureListener(this.surfaceTextureListener);
            this.changedTextureView.setVisibility(0);
            return true;
         }
      }

      return false;
   }

   public void onSurfaceTextureUpdated(SurfaceTexture var1) {
      if (this.waitingForFirstTextureUpload == 2) {
         ImageView var2 = this.textureImageView;
         if (var2 != null) {
            var2.setVisibility(4);
            this.textureImageView.setImageDrawable((Drawable)null);
            Bitmap var3 = this.currentBitmap;
            if (var3 != null) {
               var3.recycle();
               this.currentBitmap = null;
            }
         }

         this.switchingInlineMode = false;
         this.delegate.onSwitchInlineMode(this.controlsView, false, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), this.allowInlineAnimation);
         this.waitingForFirstTextureUpload = 0;
      }

   }

   public void onVideoSizeChanged(int var1, int var2, int var3, float var4) {
      if (this.aspectRatioFrameLayout != null) {
         int var5 = var1;
         int var6 = var2;
         if (var3 != 90) {
            if (var3 == 270) {
               var5 = var1;
               var6 = var2;
            } else {
               var6 = var1;
               var5 = var2;
            }
         }

         if (var5 == 0) {
            var4 = 1.0F;
         } else {
            var4 = (float)var6 * var4 / (float)var5;
         }

         this.aspectRatioFrameLayout.setAspectRatio(var4, var3);
         if (this.inFullscreen) {
            this.delegate.onVideoSizeChanged(var4, var3);
         }
      }

   }

   public void pause() {
      this.videoPlayer.pause();
      this.updatePlayButton();
      this.controlsView.show(true, true);
   }

   public void updateTextureImageView() {
      if (this.textureImageView != null) {
         try {
            this.currentBitmap = Bitmaps.createBitmap(this.textureView.getWidth(), this.textureView.getHeight(), Config.ARGB_8888);
            this.changedTextureView.getBitmap(this.currentBitmap);
         } catch (Throwable var3) {
            Bitmap var2 = this.currentBitmap;
            if (var2 != null) {
               var2.recycle();
               this.currentBitmap = null;
            }

            FileLog.e(var3);
         }

         if (this.currentBitmap != null) {
            this.textureImageView.setVisibility(0);
            this.textureImageView.setImageBitmap(this.currentBitmap);
         } else {
            this.textureImageView.setImageDrawable((Drawable)null);
         }

      }
   }

   private class AparatVideoTask extends AsyncTask {
      private boolean canRetry = true;
      private String[] results = new String[2];
      private String videoId;

      public AparatVideoTask(String var2) {
         this.videoId = var2;
      }

      protected String doInBackground(Void... var1) {
         String var2 = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "http://www.aparat.com/video/video/embed/vt/frame/showvideo/yes/videohash/%s", this.videoId));
         boolean var3 = this.isCancelled();
         String var10 = null;
         if (var3) {
            return null;
         } else {
            label54: {
               Exception var10000;
               label58: {
                  JSONArray var12;
                  boolean var10001;
                  try {
                     Matcher var11 = WebPlayerView.aparatFileListPattern.matcher(var2);
                     if (!var11.find()) {
                        break label54;
                     }

                     String var4 = var11.group(1);
                     var12 = new JSONArray(var4);
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label58;
                  }

                  int var5 = 0;

                  while(true) {
                     label59: {
                        JSONArray var14;
                        try {
                           if (var5 >= var12.length()) {
                              break label54;
                           }

                           var14 = var12.getJSONArray(var5);
                           if (var14.length() == 0) {
                              break label59;
                           }
                        } catch (Exception var8) {
                           var10000 = var8;
                           var10001 = false;
                           break;
                        }

                        JSONObject var15;
                        try {
                           var15 = var14.getJSONObject(0);
                           if (!var15.has("file")) {
                              break label59;
                           }
                        } catch (Exception var7) {
                           var10000 = var7;
                           var10001 = false;
                           break;
                        }

                        try {
                           this.results[0] = var15.getString("file");
                           this.results[1] = "other";
                        } catch (Exception var6) {
                           var10000 = var6;
                           var10001 = false;
                           break;
                        }
                     }

                     ++var5;
                  }
               }

               Exception var13 = var10000;
               FileLog.e((Throwable)var13);
            }

            if (!this.isCancelled()) {
               var10 = this.results[0];
            }

            return var10;
         }
      }

      protected void onPostExecute(String var1) {
         if (var1 != null) {
            WebPlayerView.this.initied = true;
            WebPlayerView.this.playVideoUrl = var1;
            WebPlayerView.this.playVideoType = this.results[1];
            if (WebPlayerView.this.isAutoplay) {
               WebPlayerView.this.preparePlayer();
            }

            WebPlayerView.this.showProgress(false, true);
            WebPlayerView.this.controlsView.show(true, true);
         } else if (!this.isCancelled()) {
            WebPlayerView.this.onInitFailed();
         }

      }
   }

   public interface CallJavaResultInterface {
      void jsCallFinished(String var1);
   }

   private class ControlsView extends FrameLayout {
      private int bufferedPosition;
      private AnimatorSet currentAnimation;
      private int currentProgressX;
      private int duration;
      private StaticLayout durationLayout;
      private int durationWidth;
      private Runnable hideRunnable = new _$$Lambda$WebPlayerView$ControlsView$QYTgg3cx1r3S4djGCF7dtRzr3Os(this);
      private ImageReceiver imageReceiver;
      private boolean isVisible = true;
      private int lastProgressX;
      private int progress;
      private Paint progressBufferedPaint;
      private Paint progressInnerPaint;
      private StaticLayout progressLayout;
      private Paint progressPaint;
      private boolean progressPressed;
      private TextPaint textPaint;

      public ControlsView(Context var2) {
         super(var2);
         this.setWillNotDraw(false);
         this.textPaint = new TextPaint(1);
         this.textPaint.setColor(-1);
         this.textPaint.setTextSize((float)AndroidUtilities.dp(12.0F));
         this.progressPaint = new Paint(1);
         this.progressPaint.setColor(-15095832);
         this.progressInnerPaint = new Paint();
         this.progressInnerPaint.setColor(-6975081);
         this.progressBufferedPaint = new Paint(1);
         this.progressBufferedPaint.setColor(-1);
         this.imageReceiver = new ImageReceiver(this);
      }

      private void checkNeedHide() {
         AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
         if (this.isVisible && WebPlayerView.this.videoPlayer.isPlaying()) {
            AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
         }

      }

      // $FF: synthetic method
      public void lambda$new$0$WebPlayerView$ControlsView() {
         this.show(false, true);
      }

      protected void onDraw(Canvas var1) {
         if (WebPlayerView.this.drawImage) {
            if (WebPlayerView.this.firstFrameRendered && WebPlayerView.this.currentAlpha != 0.0F) {
               long var2 = System.currentTimeMillis();
               long var4 = WebPlayerView.this.lastUpdateTime;
               WebPlayerView.this.lastUpdateTime = var2;
               WebPlayerView var6 = WebPlayerView.this;
               var6.currentAlpha = var6.currentAlpha - (float)(var2 - var4) / 150.0F;
               if (WebPlayerView.this.currentAlpha < 0.0F) {
                  WebPlayerView.this.currentAlpha = 0.0F;
               }

               this.invalidate();
            }

            this.imageReceiver.setAlpha(WebPlayerView.this.currentAlpha);
            this.imageReceiver.draw(var1);
         }

         if (WebPlayerView.this.videoPlayer.isPlayerPrepared() && !WebPlayerView.this.isStream) {
            int var7 = this.getMeasuredWidth();
            int var8 = this.getMeasuredHeight();
            float var10;
            if (!WebPlayerView.this.isInline) {
               StaticLayout var22 = this.durationLayout;
               byte var9 = 6;
               byte var11;
               if (var22 != null) {
                  var1.save();
                  var10 = (float)(var7 - AndroidUtilities.dp(58.0F) - this.durationWidth);
                  if (WebPlayerView.this.inFullscreen) {
                     var11 = 6;
                  } else {
                     var11 = 10;
                  }

                  var1.translate(var10, (float)(var8 - AndroidUtilities.dp((float)(var11 + 29))));
                  this.durationLayout.draw(var1);
                  var1.restore();
               }

               if (this.progressLayout != null) {
                  var1.save();
                  var10 = (float)AndroidUtilities.dp(18.0F);
                  if (WebPlayerView.this.inFullscreen) {
                     var11 = var9;
                  } else {
                     var11 = 10;
                  }

                  var1.translate(var10, (float)(var8 - AndroidUtilities.dp((float)(var11 + 29))));
                  this.progressLayout.draw(var1);
                  var1.restore();
               }
            }

            if (this.duration != 0) {
               int var13;
               int var14;
               int var15;
               int var24;
               int var25;
               label78: {
                  boolean var12 = WebPlayerView.this.isInline;
                  var10 = 7.0F;
                  if (var12) {
                     var25 = var8 - AndroidUtilities.dp(3.0F);
                     var24 = AndroidUtilities.dp(7.0F);
                  } else {
                     if (WebPlayerView.this.inFullscreen) {
                        var24 = AndroidUtilities.dp(29.0F);
                        var25 = AndroidUtilities.dp(36.0F);
                        var14 = this.durationWidth;
                        var15 = AndroidUtilities.dp(76.0F);
                        int var16 = this.durationWidth;
                        var13 = AndroidUtilities.dp(28.0F);
                        var7 = var7 - var15 - var16;
                        var13 = var8 - var13;
                        var24 = var8 - var24;
                        var25 += var14;
                        break label78;
                     }

                     var25 = var8 - AndroidUtilities.dp(13.0F);
                     var24 = AndroidUtilities.dp(12.0F);
                  }

                  var13 = var8 - var24;
                  var24 = var25;
                  var25 = 0;
               }

               if (WebPlayerView.this.inFullscreen) {
                  var1.drawRect((float)var25, (float)var24, (float)var7, (float)(AndroidUtilities.dp(3.0F) + var24), this.progressInnerPaint);
               }

               if (this.progressPressed) {
                  var8 = this.currentProgressX;
               } else {
                  var8 = (int)((float)(var7 - var25) * ((float)this.progress / (float)this.duration)) + var25;
               }

               var14 = this.bufferedPosition;
               float var18;
               float var20;
               float var21;
               if (var14 != 0) {
                  var15 = this.duration;
                  if (var15 != 0) {
                     float var17 = (float)var25;
                     var18 = (float)var24;
                     float var19 = (float)(var7 - var25);
                     var20 = (float)var14 / (float)var15;
                     var21 = (float)(AndroidUtilities.dp(3.0F) + var24);
                     Paint var23;
                     if (WebPlayerView.this.inFullscreen) {
                        var23 = this.progressBufferedPaint;
                     } else {
                        var23 = this.progressInnerPaint;
                     }

                     var1.drawRect(var17, var18, var19 * var20 + var17, var21, var23);
                  }
               }

               var18 = (float)var25;
               var20 = (float)var24;
               var21 = (float)var8;
               var1.drawRect(var18, var20, var21, (float)(var24 + AndroidUtilities.dp(3.0F)), this.progressPaint);
               if (!WebPlayerView.this.isInline) {
                  var18 = (float)var13;
                  if (!this.progressPressed) {
                     var10 = 5.0F;
                  }

                  var1.drawCircle(var21, var18, (float)AndroidUtilities.dp(var10), this.progressPaint);
               }
            }
         }

      }

      public boolean onInterceptTouchEvent(MotionEvent var1) {
         if (var1.getAction() == 0) {
            if (!this.isVisible) {
               this.show(true, true);
               return true;
            } else {
               this.onTouchEvent(var1);
               return this.progressPressed;
            }
         } else {
            return super.onInterceptTouchEvent(var1);
         }
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var2;
         int var3;
         int var4;
         if (WebPlayerView.this.inFullscreen) {
            var2 = AndroidUtilities.dp(36.0F) + this.durationWidth;
            var3 = this.getMeasuredWidth() - AndroidUtilities.dp(76.0F) - this.durationWidth;
            var4 = this.getMeasuredHeight() - AndroidUtilities.dp(28.0F);
         } else {
            var3 = this.getMeasuredWidth();
            var4 = this.getMeasuredHeight() - AndroidUtilities.dp(12.0F);
            var2 = 0;
         }

         int var5 = this.duration;
         if (var5 != 0) {
            var5 = (int)((float)(var3 - var2) * ((float)this.progress / (float)var5));
         } else {
            var5 = 0;
         }

         var5 += var2;
         if (var1.getAction() == 0) {
            if (this.isVisible && !WebPlayerView.this.isInline && !WebPlayerView.this.isStream) {
               if (this.duration != 0) {
                  var2 = (int)var1.getX();
                  var3 = (int)var1.getY();
                  if (var2 >= var5 - AndroidUtilities.dp(10.0F) && var2 <= AndroidUtilities.dp(10.0F) + var5 && var3 >= var4 - AndroidUtilities.dp(10.0F) && var3 <= var4 + AndroidUtilities.dp(10.0F)) {
                     this.progressPressed = true;
                     this.lastProgressX = var2;
                     this.currentProgressX = var5;
                     this.getParent().requestDisallowInterceptTouchEvent(true);
                     this.invalidate();
                  }
               }
            } else {
               this.show(true, true);
            }

            AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
         } else if (var1.getAction() != 1 && var1.getAction() != 3) {
            if (var1.getAction() == 2 && this.progressPressed) {
               var4 = (int)var1.getX();
               this.currentProgressX -= this.lastProgressX - var4;
               this.lastProgressX = var4;
               var4 = this.currentProgressX;
               if (var4 < var2) {
                  this.currentProgressX = var2;
               } else if (var4 > var3) {
                  this.currentProgressX = var3;
               }

               this.setProgress((int)((float)(this.duration * 1000) * ((float)(this.currentProgressX - var2) / (float)(var3 - var2))));
               this.invalidate();
            }
         } else {
            if (WebPlayerView.this.initied && WebPlayerView.this.videoPlayer.isPlaying()) {
               AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
            }

            if (this.progressPressed) {
               this.progressPressed = false;
               if (WebPlayerView.this.initied) {
                  this.progress = (int)((float)this.duration * ((float)(this.currentProgressX - var2) / (float)(var3 - var2)));
                  WebPlayerView.this.videoPlayer.seekTo((long)this.progress * 1000L);
               }
            }
         }

         super.onTouchEvent(var1);
         return true;
      }

      public void requestDisallowInterceptTouchEvent(boolean var1) {
         super.requestDisallowInterceptTouchEvent(var1);
         this.checkNeedHide();
      }

      public void setBufferedProgress(int var1) {
         this.bufferedPosition = var1;
         this.invalidate();
      }

      public void setDuration(int var1) {
         if (this.duration != var1 && var1 >= 0 && !WebPlayerView.this.isStream) {
            this.duration = var1;
            this.durationLayout = new StaticLayout(String.format(Locale.US, "%d:%02d", this.duration / 60, this.duration % 60), this.textPaint, AndroidUtilities.dp(1000.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            if (this.durationLayout.getLineCount() > 0) {
               this.durationWidth = (int)Math.ceil((double)this.durationLayout.getLineWidth(0));
            }

            this.invalidate();
         }

      }

      public void setProgress(int var1) {
         if (!this.progressPressed && var1 >= 0 && !WebPlayerView.this.isStream) {
            this.progress = var1;
            this.progressLayout = new StaticLayout(String.format(Locale.US, "%d:%02d", this.progress / 60, this.progress % 60), this.textPaint, AndroidUtilities.dp(1000.0F), Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            this.invalidate();
         }

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
                        ControlsView.this.currentAnimation = null;
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
                     ControlsView.this.currentAnimation = null;
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

   private class CoubVideoTask extends AsyncTask {
      private boolean canRetry = true;
      private String[] results = new String[4];
      private String videoId;

      public CoubVideoTask(String var2) {
         this.videoId = var2;
      }

      private String decodeUrl(String var1) {
         StringBuilder var7 = new StringBuilder(var1);

         for(int var2 = 0; var2 < var7.length(); ++var2) {
            char var3 = var7.charAt(var2);
            char var4 = Character.toLowerCase(var3);
            char var5 = var4;
            if (var3 == var4) {
               var4 = Character.toUpperCase(var3);
               var5 = var4;
            }

            var7.setCharAt(var2, var5);
         }

         try {
            var1 = new String(Base64.decode(var7.toString(), 0), "UTF-8");
            return var1;
         } catch (Exception var6) {
            return null;
         }
      }

      protected String doInBackground(Void... var1) {
         String var2 = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://coub.com/api/v2/coubs/%s.json", this.videoId));
         boolean var3 = this.isCancelled();
         String var7 = null;
         if (var3) {
            return null;
         } else {
            label33: {
               Exception var10000;
               label38: {
                  boolean var10001;
                  String var9;
                  try {
                     JSONObject var4 = new JSONObject(var2);
                     var4 = var4.getJSONObject("file_versions").getJSONObject("mobile");
                     var2 = this.decodeUrl(var4.getString("gifv"));
                     var9 = var4.getJSONArray("audio").getString(0);
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label38;
                  }

                  if (var2 == null || var9 == null) {
                     break label33;
                  }

                  try {
                     this.results[0] = var2;
                     this.results[1] = "other";
                     this.results[2] = var9;
                     this.results[3] = "other";
                     break label33;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }

               Exception var8 = var10000;
               FileLog.e((Throwable)var8);
            }

            if (!this.isCancelled()) {
               var7 = this.results[0];
            }

            return var7;
         }
      }

      protected void onPostExecute(String var1) {
         if (var1 != null) {
            WebPlayerView.this.initied = true;
            WebPlayerView.this.playVideoUrl = var1;
            WebPlayerView.this.playVideoType = this.results[1];
            WebPlayerView.this.playAudioUrl = this.results[2];
            WebPlayerView.this.playAudioType = this.results[3];
            if (WebPlayerView.this.isAutoplay) {
               WebPlayerView.this.preparePlayer();
            }

            WebPlayerView.this.showProgress(false, true);
            WebPlayerView.this.controlsView.show(true, true);
         } else if (!this.isCancelled()) {
            WebPlayerView.this.onInitFailed();
         }

      }
   }

   private class JSExtractor {
      private String[] assign_operators = new String[]{"|=", "^=", "&=", ">>=", "<<=", "-=", "+=", "%=", "/=", "*=", "="};
      ArrayList codeLines = new ArrayList();
      private String jsCode;
      private String[] operators = new String[]{"|", "^", "&", ">>", "<<", "-", "+", "%", "/", "*"};

      public JSExtractor(String var2) {
         this.jsCode = var2;
      }

      private void buildFunction(String[] var1, String var2) throws Exception {
         HashMap var3 = new HashMap();

         int var4;
         for(var4 = 0; var4 < var1.length; ++var4) {
            var3.put(var1[var4], "");
         }

         var1 = var2.split(";");
         boolean[] var5 = new boolean[1];

         for(var4 = 0; var4 < var1.length; ++var4) {
            this.interpretStatement(var1[var4], var3, var5, 100);
            if (var5[0]) {
               return;
            }
         }

      }

      private String extractFunction(String var1) {
         try {
            var1 = Pattern.quote(var1);
            Matcher var6 = Pattern.compile(String.format(Locale.US, "(?x)(?:function\\s+%s|[{;,]\\s*%s\\s*=\\s*function|var\\s+%s\\s*=\\s*function)\\s*\\(([^)]*)\\)\\s*\\{([^}]+)\\}", var1, var1, var1)).matcher(this.jsCode);
            if (var6.find()) {
               String var2 = var6.group();
               if (!this.codeLines.contains(var2)) {
                  ArrayList var3 = this.codeLines;
                  StringBuilder var4 = new StringBuilder();
                  var4.append(var2);
                  var4.append(";");
                  var3.add(var4.toString());
               }

               this.buildFunction(var6.group(1).split(","), var6.group(2));
            }
         } catch (Exception var5) {
            this.codeLines.clear();
            FileLog.e((Throwable)var5);
         }

         return TextUtils.join("", this.codeLines);
      }

      private HashMap extractObject(String var1) throws Exception {
         HashMap var2 = new HashMap();
         Matcher var3 = Pattern.compile(String.format(Locale.US, "(?:var\\s+)?%s\\s*=\\s*\\{\\s*((%s\\s*:\\s*function\\(.*?\\)\\s*\\{.*?\\}(?:,\\s*)?)*)\\}\\s*;", Pattern.quote(var1), "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')")).matcher(this.jsCode);
         var1 = null;

         while(var3.find()) {
            String var4 = var3.group();
            var1 = var3.group(2);
            if (!TextUtils.isEmpty(var1)) {
               if (!this.codeLines.contains(var4)) {
                  this.codeLines.add(var3.group());
               }
               break;
            }
         }

         Matcher var5 = Pattern.compile(String.format("(%s)\\s*:\\s*function\\(([a-z,]+)\\)\\{([^}]+)\\}", "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')")).matcher(var1);

         while(var5.find()) {
            this.buildFunction(var5.group(2).split(","), var5.group(3));
         }

         return var2;
      }

      private void interpretExpression(String var1, HashMap var2, int var3) throws Exception {
         String var4 = var1.trim();
         if (!TextUtils.isEmpty(var4)) {
            byte var5 = 0;
            var1 = var4;
            int var7;
            int var8;
            if (var4.charAt(0) == '(') {
               Matcher var6 = WebPlayerView.exprParensPattern.matcher(var4);
               var7 = 0;

               while(true) {
                  var8 = var7;
                  var1 = var4;
                  if (!var6.find()) {
                     break;
                  }

                  if (var6.group(0).indexOf(48) == 40) {
                     ++var7;
                  } else {
                     var8 = var7 - 1;
                     var7 = var8;
                     if (var8 == 0) {
                        this.interpretExpression(var4.substring(1, var6.start()), var2, var3);
                        var4 = var4.substring(var6.end()).trim();
                        var1 = var4;
                        if (TextUtils.isEmpty(var4)) {
                           return;
                        }
                        break;
                     }
                  }
               }

               if (var8 != 0) {
                  throw new Exception(String.format("Premature end of parens in %s", var1));
               }
            }

            var7 = 0;

            while(true) {
               String[] var16 = this.assign_operators;
               Matcher var17;
               if (var7 >= var16.length) {
                  try {
                     Integer.parseInt(var1);
                     return;
                  } catch (Exception var13) {
                     if (Pattern.compile(String.format(Locale.US, "(?!if|return|true|false)(%s)$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(var1).find()) {
                        return;
                     } else if (var1.charAt(0) == '"' && var1.charAt(var1.length() - 1) == '"') {
                        return;
                     } else {
                        try {
                           JSONObject var19 = new JSONObject(var1);
                           var19.toString();
                           return;
                        } catch (Exception var12) {
                           var17 = Pattern.compile(String.format(Locale.US, "(%s)\\[(.+)\\]$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(var1);
                           if (var17.find()) {
                              var17.group(1);
                              this.interpretExpression(var17.group(2), var2, var3 - 1);
                              return;
                           } else {
                              Matcher var9 = Pattern.compile(String.format(Locale.US, "(%s)(?:\\.([^(]+)|\\[([^]]+)\\])\\s*(?:\\(+([^()]*)\\))?$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(var1);
                              String var18;
                              if (!var9.find()) {
                                 var17 = Pattern.compile(String.format(Locale.US, "(%s)\\[(.+)\\]$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(var1);
                                 if (var17.find()) {
                                    var2.get(var17.group(1));
                                    this.interpretExpression(var17.group(2), var2, var3 - 1);
                                    return;
                                 } else {
                                    var7 = 0;

                                    while(true) {
                                       var16 = this.operators;
                                       if (var7 >= var16.length) {
                                          Matcher var14 = Pattern.compile(String.format(Locale.US, "^(%s)\\(([a-zA-Z0-9_$,]*)\\)$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(var1);
                                          if (var14.find()) {
                                             this.extractFunction(var14.group(1));
                                          }

                                          throw new Exception(String.format("Unsupported JS expression %s", var1));
                                       }

                                       var18 = var16[var7];
                                       Matcher var20 = Pattern.compile(String.format(Locale.US, "(.+?)%s(.+)", Pattern.quote(var18))).matcher(var1);
                                       if (var20.find()) {
                                          boolean[] var21 = new boolean[1];
                                          var4 = var20.group(1);
                                          var8 = var3 - 1;
                                          this.interpretStatement(var4, var2, var21, var8);
                                          if (var21[0]) {
                                             throw new Exception(String.format("Premature left-side return of %s in %s", var18, var1));
                                          }

                                          this.interpretStatement(var20.group(2), var2, var21, var8);
                                          if (var21[0]) {
                                             throw new Exception(String.format("Premature right-side return of %s in %s", var18, var1));
                                          }
                                       }

                                       ++var7;
                                    }
                                 }
                              } else {
                                 String var10 = var9.group(1);
                                 var18 = var9.group(2);
                                 String var11 = var9.group(3);
                                 var4 = var18;
                                 if (TextUtils.isEmpty(var18)) {
                                    var4 = var11;
                                 }

                                 var4.replace("\"", "");
                                 var4 = var9.group(4);
                                 if (var2.get(var10) == null) {
                                    this.extractObject(var10);
                                 }

                                 if (var4 == null) {
                                    return;
                                 } else if (var1.charAt(var1.length() - 1) != ')') {
                                    throw new Exception("last char not ')'");
                                 } else {
                                    if (var4.length() != 0) {
                                       String[] var15 = var4.split(",");

                                       for(var7 = var5; var7 < var15.length; ++var7) {
                                          this.interpretExpression(var15[var7], var2, var3);
                                       }
                                    }

                                    return;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               var4 = var16[var7];
               var17 = Pattern.compile(String.format(Locale.US, "(?x)(%s)(?:\\[([^\\]]+?)\\])?\\s*%s(.*)$", "[a-zA-Z_$][a-zA-Z_$0-9]*", Pattern.quote(var4))).matcher(var1);
               if (var17.find()) {
                  this.interpretExpression(var17.group(3), var2, var3 - 1);
                  var1 = var17.group(2);
                  if (!TextUtils.isEmpty(var1)) {
                     this.interpretExpression(var1, var2, var3);
                  } else {
                     var2.put(var17.group(1), "");
                  }

                  return;
               }

               ++var7;
            }
         }
      }

      private void interpretStatement(String var1, HashMap var2, boolean[] var3, int var4) throws Exception {
         if (var4 >= 0) {
            var3[0] = false;
            String var5 = var1.trim();
            Matcher var7 = WebPlayerView.stmtVarPattern.matcher(var5);
            if (var7.find()) {
               var1 = var5.substring(var7.group(0).length());
            } else {
               Matcher var6 = WebPlayerView.stmtReturnPattern.matcher(var5);
               var1 = var5;
               if (var6.find()) {
                  var1 = var5.substring(var6.group(0).length());
                  var3[0] = true;
               }
            }

            this.interpretExpression(var1, var2, var4);
         } else {
            throw new Exception("recursion limit reached");
         }
      }
   }

   public class JavaScriptInterface {
      private final WebPlayerView.CallJavaResultInterface callJavaResultInterface;

      public JavaScriptInterface(WebPlayerView.CallJavaResultInterface var2) {
         this.callJavaResultInterface = var2;
      }

      @JavascriptInterface
      public void returnResultToJava(String var1) {
         this.callJavaResultInterface.jsCallFinished(var1);
      }
   }

   private class TwitchClipVideoTask extends AsyncTask {
      private boolean canRetry = true;
      private String currentUrl;
      private String[] results = new String[2];
      private String videoId;

      public TwitchClipVideoTask(String var2, String var3) {
         this.videoId = var3;
         this.currentUrl = var2;
      }

      protected String doInBackground(Void... var1) {
         WebPlayerView var2 = WebPlayerView.this;
         String var3 = this.currentUrl;
         String var5 = null;
         String var6 = var2.downloadUrlContent(this, var3, (HashMap)null, false);
         if (this.isCancelled()) {
            return null;
         } else {
            try {
               Matcher var7 = WebPlayerView.twitchClipFilePattern.matcher(var6);
               if (var7.find()) {
                  var6 = var7.group(1);
                  JSONObject var8 = new JSONObject(var6);
                  JSONObject var9 = var8.getJSONArray("quality_options").getJSONObject(0);
                  this.results[0] = var9.getString("source");
                  this.results[1] = "other";
               }
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }

            if (!this.isCancelled()) {
               var5 = this.results[0];
            }

            return var5;
         }
      }

      protected void onPostExecute(String var1) {
         if (var1 != null) {
            WebPlayerView.this.initied = true;
            WebPlayerView.this.playVideoUrl = var1;
            WebPlayerView.this.playVideoType = this.results[1];
            if (WebPlayerView.this.isAutoplay) {
               WebPlayerView.this.preparePlayer();
            }

            WebPlayerView.this.showProgress(false, true);
            WebPlayerView.this.controlsView.show(true, true);
         } else if (!this.isCancelled()) {
            WebPlayerView.this.onInitFailed();
         }

      }
   }

   private class TwitchStreamVideoTask extends AsyncTask {
      private boolean canRetry = true;
      private String currentUrl;
      private String[] results = new String[2];
      private String videoId;

      public TwitchStreamVideoTask(String var2, String var3) {
         this.videoId = var3;
         this.currentUrl = var2;
      }

      protected String doInBackground(Void... var1) {
         HashMap var2 = new HashMap();
         var2.put("Client-ID", "jzkbprff40iqj646a697cyrvl0zt2m6");
         int var3 = this.videoId.indexOf(38);
         if (var3 > 0) {
            this.videoId = this.videoId.substring(0, var3);
         }

         String var4 = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://api.twitch.tv/kraken/streams/%s?stream_type=all", this.videoId), var2, false);
         boolean var5 = this.isCancelled();
         String var8 = null;
         if (var5) {
            return null;
         } else {
            try {
               JSONObject var6 = new JSONObject(var4);
               var6.getJSONObject("stream");
               String var12 = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://api.twitch.tv/api/channels/%s/access_token", this.videoId), var2, false);
               JSONObject var9 = new JSONObject(var12);
               var12 = URLEncoder.encode(var9.getString("sig"), "UTF-8");
               String var10 = URLEncoder.encode(var9.getString("token"), "UTF-8");
               StringBuilder var11 = new StringBuilder();
               var11.append("https://youtube.googleapis.com/v/");
               var11.append(this.videoId);
               URLEncoder.encode(var11.toString(), "UTF-8");
               var11 = new StringBuilder();
               var11.append("allow_source=true&allow_audio_only=true&allow_spectre=true&player=twitchweb&segment_preference=4&p=");
               var11.append((int)(Math.random() * 1.0E7D));
               var11.append("&sig=");
               var11.append(var12);
               var11.append("&token=");
               var11.append(var10);
               var12 = var11.toString();
               var12 = String.format(Locale.US, "https://usher.ttvnw.net/api/channel/hls/%s.m3u8?%s", this.videoId, var12);
               this.results[0] = var12;
               this.results[1] = "hls";
            } catch (Exception var7) {
               FileLog.e((Throwable)var7);
            }

            if (!this.isCancelled()) {
               var8 = this.results[0];
            }

            return var8;
         }
      }

      protected void onPostExecute(String var1) {
         if (var1 != null) {
            WebPlayerView.this.initied = true;
            WebPlayerView.this.playVideoUrl = var1;
            WebPlayerView.this.playVideoType = this.results[1];
            if (WebPlayerView.this.isAutoplay) {
               WebPlayerView.this.preparePlayer();
            }

            WebPlayerView.this.showProgress(false, true);
            WebPlayerView.this.controlsView.show(true, true);
         } else if (!this.isCancelled()) {
            WebPlayerView.this.onInitFailed();
         }

      }
   }

   private class VimeoVideoTask extends AsyncTask {
      private boolean canRetry = true;
      private String[] results = new String[2];
      private String videoId;

      public VimeoVideoTask(String var2) {
         this.videoId = var2;
      }

      protected String doInBackground(Void... var1) {
         String var2 = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://player.vimeo.com/video/%s/config", this.videoId));
         boolean var3 = this.isCancelled();
         String var11 = null;
         if (var3) {
            return null;
         } else {
            label55: {
               Exception var10000;
               label54: {
                  JSONObject var12;
                  boolean var10001;
                  try {
                     JSONObject var4 = new JSONObject(var2);
                     var12 = var4.getJSONObject("request").getJSONObject("files");
                     var3 = var12.has("hls");
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label54;
                  }

                  if (var3) {
                     label60: {
                        try {
                           var12 = var12.getJSONObject("hls");
                        } catch (Exception var8) {
                           var10000 = var8;
                           var10001 = false;
                           break label60;
                        }

                        try {
                           this.results[0] = var12.getString("url");
                        } catch (Exception var7) {
                           try {
                              String var14 = var12.getString("default_cdn");
                              var12 = var12.getJSONObject("cdns").getJSONObject(var14);
                              this.results[0] = var12.getString("url");
                           } catch (Exception var6) {
                              var10000 = var6;
                              var10001 = false;
                              break label60;
                           }
                        }

                        try {
                           this.results[1] = "hls";
                           break label55;
                        } catch (Exception var5) {
                           var10000 = var5;
                           var10001 = false;
                        }
                     }
                  } else {
                     try {
                        if (var12.has("progressive")) {
                           this.results[1] = "other";
                           var12 = var12.getJSONArray("progressive").getJSONObject(0);
                           this.results[0] = var12.getString("url");
                        }
                        break label55;
                     } catch (Exception var9) {
                        var10000 = var9;
                        var10001 = false;
                     }
                  }
               }

               Exception var13 = var10000;
               FileLog.e((Throwable)var13);
            }

            if (!this.isCancelled()) {
               var11 = this.results[0];
            }

            return var11;
         }
      }

      protected void onPostExecute(String var1) {
         if (var1 != null) {
            WebPlayerView.this.initied = true;
            WebPlayerView.this.playVideoUrl = var1;
            WebPlayerView.this.playVideoType = this.results[1];
            if (WebPlayerView.this.isAutoplay) {
               WebPlayerView.this.preparePlayer();
            }

            WebPlayerView.this.showProgress(false, true);
            WebPlayerView.this.controlsView.show(true, true);
         } else if (!this.isCancelled()) {
            WebPlayerView.this.onInitFailed();
         }

      }
   }

   public interface WebPlayerViewDelegate {
      boolean checkInlinePermissions();

      ViewGroup getTextureViewContainer();

      void onInitFailed();

      void onInlineSurfaceTextureReady();

      void onPlayStateChanged(WebPlayerView var1, boolean var2);

      void onSharePressed();

      TextureView onSwitchInlineMode(View var1, boolean var2, float var3, int var4, boolean var5);

      TextureView onSwitchToFullscreen(View var1, boolean var2, float var3, int var4, boolean var5);

      void onVideoSizeChanged(float var1, int var2);

      void prepareToSwitchInlineMode(boolean var1, Runnable var2, float var3, boolean var4);
   }

   private class YoutubeVideoTask extends AsyncTask {
      private boolean canRetry = true;
      private CountDownLatch countDownLatch = new CountDownLatch(1);
      private String[] result = new String[2];
      private String sig;
      private String videoId;

      public YoutubeVideoTask(String var2) {
         this.videoId = var2;
      }

      private void onInterfaceResult(String var1) {
         String[] var2 = this.result;
         String var3 = var2[0];
         String var4 = this.sig;
         StringBuilder var5 = new StringBuilder();
         var5.append("/signature/");
         var5.append(var1);
         var2[0] = var3.replace(var4, var5.toString());
         this.countDownLatch.countDown();
      }

      protected String[] doInBackground(Void... var1) {
         WebPlayerView var2 = WebPlayerView.this;
         StringBuilder var42 = new StringBuilder();
         var42.append("https://www.youtube.com/embed/");
         var42.append(this.videoId);
         String var3 = var2.downloadUrlContent(this, var42.toString());
         if (this.isCancelled()) {
            return null;
         } else {
            var42 = new StringBuilder();
            var42.append("video_id=");
            var42.append(this.videoId);
            var42.append("&ps=default&gl=US&hl=en");
            String var43 = var42.toString();

            StringBuilder var4;
            StringBuilder var44;
            String var45;
            label370: {
               try {
                  var44 = new StringBuilder();
                  var44.append(var43);
                  var44.append("&eurl=");
                  var4 = new StringBuilder();
                  var4.append("https://youtube.googleapis.com/v/");
                  var4.append(this.videoId);
                  var44.append(URLEncoder.encode(var4.toString(), "UTF-8"));
                  var45 = var44.toString();
               } catch (Exception var41) {
                  FileLog.e((Throwable)var41);
                  break label370;
               }

               var43 = var45;
            }

            var45 = var43;
            Matcher var46;
            if (var3 != null) {
               var46 = WebPlayerView.stsPattern.matcher(var3);
               if (var46.find()) {
                  var4 = new StringBuilder();
                  var4.append(var43);
                  var4.append("&sts=");
                  var4.append(var3.substring(var46.start() + 6, var46.end()));
                  var45 = var4.toString();
               } else {
                  var44 = new StringBuilder();
                  var44.append(var43);
                  var44.append("&sts=");
                  var45 = var44.toString();
               }
            }

            this.result[1] = "dash";
            String[] var5 = new String[]{"", "&el=leanback", "&el=embedded", "&el=detailpage", "&el=vevo"};
            var43 = null;
            int var6 = 0;
            boolean var7 = false;

            while(true) {
               String var48 = var43;
               boolean var8 = var7;
               String var16;
               Exception var10000;
               boolean var10001;
               String[] var47;
               Exception var49;
               String var53;
               if (var6 < var5.length) {
                  WebPlayerView var9 = WebPlayerView.this;
                  var4 = new StringBuilder();
                  var4.append("https://www.youtube.com/get_video_info?");
                  var4.append(var45);
                  var4.append(var5[var6]);
                  var48 = var9.downloadUrlContent(this, var4.toString());
                  if (this.isCancelled()) {
                     return null;
                  }

                  boolean var12;
                  boolean var14;
                  if (var48 != null) {
                     String[] var10 = var48.split("&");
                     var48 = null;
                     var8 = var7;
                     int var11 = 0;
                     var12 = false;
                     var7 = false;
                     var53 = var43;

                     while(true) {
                        if (var11 >= var10.length) {
                           var43 = var53;
                           var14 = var7;
                           break;
                        }

                        boolean var13;
                        boolean var15;
                        if (var10[var11].startsWith("dashmpd")) {
                           var47 = var10[var11].split("=");
                           if (var47.length == 2) {
                              try {
                                 this.result[0] = URLDecoder.decode(var47[1], "UTF-8");
                              } catch (Exception var22) {
                                 FileLog.e((Throwable)var22);
                              }
                           }

                           var13 = true;
                           var14 = var12;
                           var43 = var48;
                           var15 = var8;
                           var16 = var53;
                        } else {
                           String[] var17;
                           if (var10[var11].startsWith("url_encoded_fmt_stream_map")) {
                              var17 = var10[var11].split("=");
                              var14 = var12;
                              var43 = var48;
                              var15 = var8;
                              var13 = var7;
                              var16 = var53;
                              if (var17.length == 2) {
                                 label352: {
                                    label379: {
                                       try {
                                          var17 = URLDecoder.decode(var17[1], "UTF-8").split("[&,]");
                                       } catch (Exception var40) {
                                          var10000 = var40;
                                          var10001 = false;
                                          break label379;
                                       }

                                       int var18 = 0;
                                       boolean var19 = false;
                                       String var20 = null;

                                       while(true) {
                                          var14 = var12;
                                          var43 = var48;
                                          var15 = var8;
                                          var13 = var7;
                                          var16 = var53;

                                          label381: {
                                             String[] var64;
                                             label382: {
                                                try {
                                                   if (var18 >= var17.length) {
                                                      break label352;
                                                   }

                                                   var64 = var17[var18].split("=");
                                                   if (var64[0].startsWith("type")) {
                                                      break label382;
                                                   }
                                                } catch (Exception var39) {
                                                   var10000 = var39;
                                                   var10001 = false;
                                                   break;
                                                }

                                                label383: {
                                                   try {
                                                      if (var64[0].startsWith("url")) {
                                                         var43 = URLDecoder.decode(var64[1], "UTF-8");
                                                         break label383;
                                                      }
                                                   } catch (Exception var37) {
                                                      var10000 = var37;
                                                      var10001 = false;
                                                      break;
                                                   }

                                                   boolean var21;
                                                   try {
                                                      var21 = var64[0].startsWith("itag");
                                                   } catch (Exception var36) {
                                                      var10000 = var36;
                                                      var10001 = false;
                                                      break;
                                                   }

                                                   var14 = var19;
                                                   var43 = var20;
                                                   if (var21) {
                                                      var14 = false;
                                                      var43 = null;
                                                   }
                                                   break label381;
                                                }

                                                var14 = var19;
                                                break label381;
                                             }

                                             var14 = var19;
                                             var43 = var20;

                                             try {
                                                if (!URLDecoder.decode(var64[1], "UTF-8").contains("video/mp4")) {
                                                   break label381;
                                                }
                                             } catch (Exception var38) {
                                                var10000 = var38;
                                                var10001 = false;
                                                break;
                                             }

                                             var14 = true;
                                             var43 = var20;
                                          }

                                          if (var14 && var43 != null) {
                                             var16 = var43;
                                             var14 = var12;
                                             var43 = var48;
                                             var15 = var8;
                                             var13 = var7;
                                             break label352;
                                          }

                                          ++var18;
                                          var19 = var14;
                                          var20 = var43;
                                       }
                                    }

                                    var49 = var10000;
                                    FileLog.e((Throwable)var49);
                                    var14 = var12;
                                    var43 = var48;
                                    var15 = var8;
                                    var13 = var7;
                                    var16 = var53;
                                 }
                              }
                           } else if (var10[var11].startsWith("use_cipher_signature")) {
                              var17 = var10[var11].split("=");
                              var14 = var12;
                              var43 = var48;
                              var15 = var8;
                              var13 = var7;
                              var16 = var53;
                              if (var17.length == 2) {
                                 var14 = var12;
                                 var43 = var48;
                                 var15 = var8;
                                 var13 = var7;
                                 var16 = var53;
                                 if (var17[1].toLowerCase().equals("true")) {
                                    var15 = true;
                                    var14 = var12;
                                    var43 = var48;
                                    var13 = var7;
                                    var16 = var53;
                                 }
                              }
                           } else if (var10[var11].startsWith("hlsvp")) {
                              var17 = var10[var11].split("=");
                              var14 = var12;
                              var43 = var48;
                              var15 = var8;
                              var13 = var7;
                              var16 = var53;
                              if (var17.length == 2) {
                                 label313: {
                                    try {
                                       var43 = URLDecoder.decode(var17[1], "UTF-8");
                                    } catch (Exception var35) {
                                       FileLog.e((Throwable)var35);
                                       var14 = var12;
                                       var43 = var48;
                                       var15 = var8;
                                       var13 = var7;
                                       var16 = var53;
                                       break label313;
                                    }

                                    var14 = var12;
                                    var15 = var8;
                                    var13 = var7;
                                    var16 = var53;
                                 }
                              }
                           } else {
                              var14 = var12;
                              var43 = var48;
                              var15 = var8;
                              var13 = var7;
                              var16 = var53;
                              if (var10[var11].startsWith("livestream")) {
                                 var17 = var10[var11].split("=");
                                 var14 = var12;
                                 var43 = var48;
                                 var15 = var8;
                                 var13 = var7;
                                 var16 = var53;
                                 if (var17.length == 2) {
                                    var14 = var12;
                                    var43 = var48;
                                    var15 = var8;
                                    var13 = var7;
                                    var16 = var53;
                                    if (var17[1].toLowerCase().equals("1")) {
                                       var14 = true;
                                       var16 = var53;
                                       var13 = var7;
                                       var15 = var8;
                                       var43 = var48;
                                    }
                                 }
                              }
                           }
                        }

                        ++var11;
                        var12 = var14;
                        var48 = var43;
                        var8 = var15;
                        var7 = var13;
                        var53 = var16;
                     }
                  } else {
                     var12 = false;
                     var48 = null;
                     var14 = false;
                     var8 = var7;
                  }

                  if (var12) {
                     if (var48 == null || var8 || var48.contains("/s/")) {
                        return null;
                     }

                     String[] var54 = this.result;
                     var54[0] = var48;
                     var54[1] = "hls";
                  }

                  if (!var14) {
                     ++var6;
                     var7 = var8;
                     continue;
                  }

                  var48 = var43;
               }

               var47 = this.result;
               if (var47[0] == null && var48 != null) {
                  var47[0] = var48;
                  var47[1] = "other";
               }

               var47 = this.result;
               if (var47[0] != null && (var8 || var47[0].contains("/s/")) && var3 != null) {
                  label286: {
                     int var55 = this.result[0].indexOf("/s/");
                     int var59 = this.result[0].indexOf(47, var55 + 10);
                     if (var55 != -1) {
                        int var51 = var59;
                        if (var59 == -1) {
                           var51 = this.result[0].length();
                        }

                        label282: {
                           this.sig = this.result[0].substring(var55, var51);
                           var46 = WebPlayerView.jsPattern.matcher(var3);
                           if (var46.find()) {
                              try {
                                 JSONTokener var57 = new JSONTokener(var46.group(1));
                                 Object var60 = var57.nextValue();
                                 if (var60 instanceof String) {
                                    var48 = (String)var60;
                                    break label282;
                                 }
                              } catch (Exception var34) {
                                 FileLog.e((Throwable)var34);
                              }
                           }

                           var48 = null;
                        }

                        if (var48 != null) {
                           Matcher var61 = WebPlayerView.playerIdPattern.matcher(var48);
                           if (var61.find()) {
                              var44 = new StringBuilder();
                              var44.append(var61.group(1));
                              var44.append(var61.group(2));
                              var16 = var44.toString();
                           } else {
                              var16 = null;
                           }

                           Context var62 = ApplicationLoader.applicationContext;
                           var7 = false;
                           SharedPreferences var67 = var62.getSharedPreferences("youtubecode", 0);
                           if (var16 != null) {
                              var43 = var67.getString(var16, (String)null);
                              var44 = new StringBuilder();
                              var44.append(var16);
                              var44.append("n");
                              var45 = var67.getString(var44.toString(), (String)null);
                           } else {
                              var43 = null;
                              var45 = null;
                           }

                           String var65;
                           label274: {
                              if (var43 == null) {
                                 StringBuilder var56;
                                 if (var48.startsWith("//")) {
                                    var56 = new StringBuilder();
                                    var56.append("https:");
                                    var56.append(var48);
                                    var53 = var56.toString();
                                 } else {
                                    var53 = var48;
                                    if (var48.startsWith("/")) {
                                       var56 = new StringBuilder();
                                       var56.append("https://www.youtube.com");
                                       var56.append(var48);
                                       var53 = var56.toString();
                                    }
                                 }

                                 var3 = WebPlayerView.this.downloadUrlContent(this, var53);
                                 if (this.isCancelled()) {
                                    return null;
                                 }

                                 if (var3 != null) {
                                    Matcher var52 = WebPlayerView.sigPattern.matcher(var3);
                                    if (var52.find()) {
                                       var45 = var52.group(1);
                                    } else {
                                       var52 = WebPlayerView.sigPattern2.matcher(var3);
                                       if (var52.find()) {
                                          var45 = var52.group(1);
                                       }
                                    }

                                    var53 = var43;
                                    var65 = var45;
                                    if (var45 != null) {
                                       var48 = var43;

                                       label265: {
                                          label389: {
                                             WebPlayerView.JSExtractor var58;
                                             try {
                                                var58 = new WebPlayerView.JSExtractor;
                                             } catch (Exception var33) {
                                                var10000 = var33;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;

                                             try {
                                                var58.<init>(var3);
                                             } catch (Exception var32) {
                                                var10000 = var32;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;

                                             try {
                                                var43 = var58.extractFunction(var45);
                                             } catch (Exception var31) {
                                                var10000 = var31;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;
                                             var53 = var43;
                                             var65 = var45;

                                             try {
                                                if (TextUtils.isEmpty(var43)) {
                                                   break label274;
                                                }
                                             } catch (Exception var30) {
                                                var10000 = var30;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var53 = var43;
                                             var65 = var45;
                                             if (var16 == null) {
                                                break label274;
                                             }

                                             var48 = var43;

                                             Editor var63;
                                             try {
                                                var63 = var67.edit().putString(var16, var43);
                                             } catch (Exception var29) {
                                                var10000 = var29;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;

                                             StringBuilder var66;
                                             try {
                                                var66 = new StringBuilder;
                                             } catch (Exception var28) {
                                                var10000 = var28;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;

                                             try {
                                                var66.<init>();
                                             } catch (Exception var27) {
                                                var10000 = var27;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;

                                             try {
                                                var66.append(var16);
                                             } catch (Exception var26) {
                                                var10000 = var26;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;

                                             try {
                                                var66.append("n");
                                             } catch (Exception var25) {
                                                var10000 = var25;
                                                var10001 = false;
                                                break label389;
                                             }

                                             var48 = var43;

                                             try {
                                                var63.putString(var66.toString(), var45).commit();
                                                break label265;
                                             } catch (Exception var24) {
                                                var10000 = var24;
                                                var10001 = false;
                                             }
                                          }

                                          var49 = var10000;
                                          FileLog.e((Throwable)var49);
                                          var53 = var48;
                                          var65 = var45;
                                          break label274;
                                       }

                                       var53 = var43;
                                       var65 = var45;
                                    }
                                    break label274;
                                 }
                              }

                              var65 = var45;
                              var53 = var43;
                           }

                           if (!TextUtils.isEmpty(var53)) {
                              if (VERSION.SDK_INT >= 21) {
                                 var42 = new StringBuilder();
                                 var42.append(var53);
                                 var42.append(var65);
                                 var42.append("('");
                                 var42.append(this.sig.substring(3));
                                 var42.append("');");
                                 var43 = var42.toString();
                              } else {
                                 var42 = new StringBuilder();
                                 var42.append(var53);
                                 var42.append("window.");
                                 var42.append(WebPlayerView.this.interfaceName);
                                 var42.append(".returnResultToJava(");
                                 var42.append(var65);
                                 var42.append("('");
                                 var42.append(this.sig.substring(3));
                                 var42.append("'));");
                                 var43 = var42.toString();
                              }

                              try {
                                 _$$Lambda$WebPlayerView$YoutubeVideoTask$GMLQkdVjUFyM84BTj7n250BC06g var50 = new _$$Lambda$WebPlayerView$YoutubeVideoTask$GMLQkdVjUFyM84BTj7n250BC06g(this, var43);
                                 AndroidUtilities.runOnUIThread(var50);
                                 this.countDownLatch.await();
                                 break label286;
                              } catch (Exception var23) {
                                 FileLog.e((Throwable)var23);
                              }
                           }
                        }
                     }

                     var7 = true;
                  }
               } else {
                  var7 = var8;
               }

               if (!this.isCancelled() && !var7) {
                  var47 = this.result;
               } else {
                  var47 = null;
               }

               return var47;
            }
         }
      }

      // $FF: synthetic method
      public void lambda$doInBackground$1$WebPlayerView$YoutubeVideoTask(String var1) {
         if (VERSION.SDK_INT >= 21) {
            WebPlayerView.this.webView.evaluateJavascript(var1, new _$$Lambda$WebPlayerView$YoutubeVideoTask$frhxjuVE3CuEISsmdJnF0IVDS2M(this));
         } else {
            try {
               StringBuilder var2 = new StringBuilder();
               var2.append("<script>");
               var2.append(var1);
               var2.append("</script>");
               String var6 = Base64.encodeToString(var2.toString().getBytes("UTF-8"), 0);
               WebView var5 = WebPlayerView.this.webView;
               StringBuilder var3 = new StringBuilder();
               var3.append("data:text/html;charset=utf-8;base64,");
               var3.append(var6);
               var5.loadUrl(var3.toString());
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }

      }

      // $FF: synthetic method
      public void lambda$null$0$WebPlayerView$YoutubeVideoTask(String var1) {
         String[] var2 = this.result;
         String var3 = var2[0];
         String var4 = this.sig;
         StringBuilder var5 = new StringBuilder();
         var5.append("/signature/");
         var5.append(var1.substring(1, var1.length() - 1));
         var2[0] = var3.replace(var4, var5.toString());
         this.countDownLatch.countDown();
      }

      protected void onPostExecute(String[] var1) {
         if (var1[0] != null) {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var2 = new StringBuilder();
               var2.append("start play youtube video ");
               var2.append(var1[1]);
               var2.append(" ");
               var2.append(var1[0]);
               FileLog.d(var2.toString());
            }

            WebPlayerView.this.initied = true;
            WebPlayerView.this.playVideoUrl = var1[0];
            WebPlayerView.this.playVideoType = var1[1];
            if (WebPlayerView.this.playVideoType.equals("hls")) {
               WebPlayerView.this.isStream = true;
            }

            if (WebPlayerView.this.isAutoplay) {
               WebPlayerView.this.preparePlayer();
            }

            WebPlayerView.this.showProgress(false, true);
            WebPlayerView.this.controlsView.show(true, true);
         } else if (!this.isCancelled()) {
            WebPlayerView.this.onInitFailed();
         }

      }
   }

   private abstract class function {
      public abstract Object run(Object[] var1);
   }
}
