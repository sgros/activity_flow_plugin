package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.Settings;
import android.text.TextUtils.TruncateAt;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.DecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BringAppForegroundService;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;

public class EmbedBottomSheet extends BottomSheet {
   @SuppressLint({"StaticFieldLeak"})
   private static EmbedBottomSheet instance;
   private boolean animationInProgress;
   private FrameLayout containerLayout;
   private TextView copyTextButton;
   private View customView;
   private CustomViewCallback customViewCallback;
   private String embedUrl;
   private FrameLayout fullscreenVideoContainer;
   private boolean fullscreenedByButton;
   private boolean hasDescription;
   private int height;
   private LinearLayout imageButtonsContainer;
   private boolean isYouTube;
   private int lastOrientation = -1;
   private OnShowListener onShowListener = new OnShowListener() {
      public void onShow(DialogInterface var1) {
         if (EmbedBottomSheet.this.pipVideoView != null && EmbedBottomSheet.this.videoView.isInline()) {
            EmbedBottomSheet.this.videoView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
               public boolean onPreDraw() {
                  EmbedBottomSheet.this.videoView.getViewTreeObserver().removeOnPreDrawListener(this);
                  return true;
               }
            });
         }

      }
   };
   private TextView openInButton;
   private String openUrl;
   private OrientationEventListener orientationEventListener;
   private Activity parentActivity;
   private ImageView pipButton;
   private PipVideoView pipVideoView;
   private int[] position = new int[2];
   private int prevOrientation = -2;
   private RadialProgressView progressBar;
   private View progressBarBlackBackground;
   private WebPlayerView videoView;
   private int waitingForDraw;
   private boolean wasInLandscape;
   private WebView webView;
   private int width;
   private final String youtubeFrame = "<!DOCTYPE html><html><head><style>body { margin: 0; width:100%%; height:100%%;  background-color:#000; }html { width:100%%; height:100%%; background-color:#000; }.embed-container iframe,.embed-container object,   .embed-container embed {       position: absolute;       top: 0;       left: 0;       width: 100%% !important;       height: 100%% !important;   }   </style></head><body>   <div class=\"embed-container\">       <div id=\"player\"></div>   </div>   <script src=\"https://www.youtube.com/iframe_api\"></script>   <script>   var player;   var observer;   var videoEl;   var playing;   var posted = false;   YT.ready(function() {       player = new YT.Player(\"player\", {                              \"width\" : \"100%%\",                              \"events\" : {                              \"onReady\" : \"onReady\",                              \"onError\" : \"onError\",                              },                              \"videoId\" : \"%1$s\",                              \"height\" : \"100%%\",                              \"playerVars\" : {                              \"start\" : %2$d,                              \"rel\" : 0,                              \"showinfo\" : 0,                              \"modestbranding\" : 1,                              \"iv_load_policy\" : 3,                              \"autohide\" : 1,                              \"autoplay\" : 1,                              \"cc_load_policy\" : 1,                              \"playsinline\" : 1,                              \"controls\" : 1                              }                            });        player.setSize(window.innerWidth, window.innerHeight);    });    function hideControls() {        playing = !videoEl.paused;       videoEl.controls = 0;       observer.observe(videoEl, {attributes: true});    }    function showControls() {        playing = !videoEl.paused;       observer.disconnect();       videoEl.controls = 1;    }    function onError(event) {       if (!posted) {            if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);             }            posted = true;       }    }    function onReady(event) {       player.playVideo();       videoEl = player.getIframe().contentDocument.getElementsByTagName('video')[0];\n       videoEl.addEventListener(\"canplay\", function() {            if (playing) {               videoEl.play();            }       }, true);       videoEl.addEventListener(\"timeupdate\", function() {            if (!posted && videoEl.currentTime > 0) {               if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);                }               posted = true;           }       }, true);       observer = new MutationObserver(function() {\n          if (videoEl.controls) {\n               videoEl.controls = 0;\n          }       });\n    }    window.onresize = function() {        player.setSize(window.innerWidth, window.innerHeight);    }    </script></body></html>";
   private ImageView youtubeLogoImage;

   @SuppressLint({"SetJavaScriptEnabled"})
   private EmbedBottomSheet(Context var1, String var2, String var3, String var4, String var5, int var6, int var7) {
      super(var1, false, 0);
      super.fullWidth = true;
      this.setApplyTopPadding(false);
      this.setApplyBottomPadding(false);
      if (var1 instanceof Activity) {
         this.parentActivity = (Activity)var1;
      }

      this.embedUrl = var5;
      boolean var8;
      if (var3 != null && var3.length() > 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      this.hasDescription = var8;
      this.openUrl = var4;
      this.width = var6;
      this.height = var7;
      if (this.width == 0 || this.height == 0) {
         android.graphics.Point var13 = AndroidUtilities.displaySize;
         this.width = var13.x;
         this.height = var13.y / 2;
      }

      this.fullscreenVideoContainer = new FrameLayout(var1);
      this.fullscreenVideoContainer.setKeepScreenOn(true);
      this.fullscreenVideoContainer.setBackgroundColor(-16777216);
      if (VERSION.SDK_INT >= 21) {
         this.fullscreenVideoContainer.setFitsSystemWindows(true);
      }

      this.fullscreenVideoContainer.setOnTouchListener(_$$Lambda$EmbedBottomSheet$awgbBJJ9KT_HOZj5dBev_ioRvPo.INSTANCE);
      super.container.addView(this.fullscreenVideoContainer, LayoutHelper.createFrame(-1, -1.0F));
      this.fullscreenVideoContainer.setVisibility(4);
      this.fullscreenVideoContainer.setOnTouchListener(_$$Lambda$EmbedBottomSheet$wxc0LCDO5uuH8qs6VYdAXQWEEZE.INSTANCE);
      this.containerLayout = new FrameLayout(var1) {
         protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();

            try {
               if ((EmbedBottomSheet.this.pipVideoView == null || EmbedBottomSheet.this.webView.getVisibility() != 0) && EmbedBottomSheet.this.webView.getParent() != null) {
                  this.removeView(EmbedBottomSheet.this.webView);
                  EmbedBottomSheet.this.webView.stopLoading();
                  EmbedBottomSheet.this.webView.loadUrl("about:blank");
                  EmbedBottomSheet.this.webView.destroy();
               }

               if (!EmbedBottomSheet.this.videoView.isInline() && EmbedBottomSheet.this.pipVideoView == null) {
                  if (EmbedBottomSheet.instance == EmbedBottomSheet.this) {
                     EmbedBottomSheet.instance = null;
                  }

                  EmbedBottomSheet.this.videoView.destroy();
               }
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
            }

         }

         protected void onMeasure(int var1, int var2) {
            var2 = MeasureSpec.getSize(var1);
            float var3 = (float)EmbedBottomSheet.this.width / (float)var2;
            int var4 = (int)Math.min((float)EmbedBottomSheet.this.height / var3, (float)(AndroidUtilities.displaySize.y / 2));
            byte var5;
            if (EmbedBottomSheet.this.hasDescription) {
               var5 = 22;
            } else {
               var5 = 0;
            }

            super.onMeasure(var1, MeasureSpec.makeMeasureSpec(var4 + AndroidUtilities.dp((float)(var5 + 84)) + 1, 1073741824));
         }
      };
      this.containerLayout.setOnTouchListener(_$$Lambda$EmbedBottomSheet$iYWCViD8wKuconyj_uGO4wKzEcQ.INSTANCE);
      this.setCustomView(this.containerLayout);
      this.webView = new WebView(var1) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (EmbedBottomSheet.this.isYouTube && var1.getAction() == 0) {
               EmbedBottomSheet.this.showOrHideYoutubeLogo(true);
            }

            return super.onTouchEvent(var1);
         }
      };
      this.webView.getSettings().setJavaScriptEnabled(true);
      this.webView.getSettings().setDomStorageEnabled(true);
      if (VERSION.SDK_INT >= 17) {
         this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
      }

      if (VERSION.SDK_INT >= 21) {
         this.webView.getSettings().setMixedContentMode(0);
         CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
      }

      this.webView.setWebChromeClient(new WebChromeClient() {
         public void onHideCustomView() {
            super.onHideCustomView();
            if (EmbedBottomSheet.this.customView != null) {
               EmbedBottomSheet.this.getSheetContainer().setVisibility(0);
               EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
               EmbedBottomSheet.this.fullscreenVideoContainer.removeView(EmbedBottomSheet.this.customView);
               if (EmbedBottomSheet.this.customViewCallback != null && !EmbedBottomSheet.this.customViewCallback.getClass().getName().contains(".chromium.")) {
                  EmbedBottomSheet.this.customViewCallback.onCustomViewHidden();
               }

               EmbedBottomSheet.this.customView = null;
            }
         }

         public void onShowCustomView(View var1, int var2, CustomViewCallback var3) {
            this.onShowCustomView(var1, var3);
         }

         public void onShowCustomView(View var1, CustomViewCallback var2) {
            if (EmbedBottomSheet.this.customView == null && EmbedBottomSheet.this.pipVideoView == null) {
               EmbedBottomSheet.this.exitFromPip();
               EmbedBottomSheet.this.customView = var1;
               EmbedBottomSheet.this.getSheetContainer().setVisibility(4);
               EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(0);
               EmbedBottomSheet.this.fullscreenVideoContainer.addView(var1, LayoutHelper.createFrame(-1, -1.0F));
               EmbedBottomSheet.this.customViewCallback = var2;
            } else {
               var2.onCustomViewHidden();
            }
         }
      });
      this.webView.setWebViewClient(new WebViewClient() {
         public void onLoadResource(WebView var1, String var2) {
            super.onLoadResource(var1, var2);
         }

         public void onPageFinished(WebView var1, String var2) {
            super.onPageFinished(var1, var2);
            if (!EmbedBottomSheet.this.isYouTube || VERSION.SDK_INT < 17) {
               EmbedBottomSheet.this.progressBar.setVisibility(4);
               EmbedBottomSheet.this.progressBarBlackBackground.setVisibility(4);
               EmbedBottomSheet.this.pipButton.setEnabled(true);
               EmbedBottomSheet.this.pipButton.setAlpha(1.0F);
            }

         }
      });
      FrameLayout var14 = this.containerLayout;
      WebView var15 = this.webView;
      var8 = this.hasDescription;
      byte var23 = 22;
      byte var20;
      if (var8) {
         var20 = 22;
      } else {
         var20 = 0;
      }

      var14.addView(var15, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, (float)(var20 + 84)));
      this.youtubeLogoImage = new ImageView(var1);
      this.youtubeLogoImage.setVisibility(8);
      this.containerLayout.addView(this.youtubeLogoImage, LayoutHelper.createFrame(66, 28.0F, 53, 0.0F, 8.0F, 8.0F, 0.0F));
      this.youtubeLogoImage.setOnClickListener(new _$$Lambda$EmbedBottomSheet$7BVC7l6jtG_KhdXyPoXIswQ3uh8(this));
      this.videoView = new WebPlayerView(var1, true, false, new WebPlayerView.WebPlayerViewDelegate() {
         public boolean checkInlinePermissions() {
            return EmbedBottomSheet.this.checkInlinePermissions();
         }

         public ViewGroup getTextureViewContainer() {
            return EmbedBottomSheet.access$4300(EmbedBottomSheet.this);
         }

         public void onInitFailed() {
            EmbedBottomSheet.this.webView.setVisibility(0);
            EmbedBottomSheet.this.imageButtonsContainer.setVisibility(0);
            EmbedBottomSheet.this.copyTextButton.setVisibility(4);
            EmbedBottomSheet.this.webView.setKeepScreenOn(true);
            EmbedBottomSheet.this.videoView.setVisibility(4);
            EmbedBottomSheet.this.videoView.getControlsView().setVisibility(4);
            EmbedBottomSheet.this.videoView.getTextureView().setVisibility(4);
            if (EmbedBottomSheet.this.videoView.getTextureImageView() != null) {
               EmbedBottomSheet.this.videoView.getTextureImageView().setVisibility(4);
            }

            EmbedBottomSheet.this.videoView.loadVideo((String)null, (TLRPC.Photo)null, (Object)null, (String)null, false);
            HashMap var1 = new HashMap();
            var1.put("Referer", "http://youtube.com");

            try {
               EmbedBottomSheet.this.webView.loadUrl(EmbedBottomSheet.this.embedUrl, var1);
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
            }

         }

         public void onInlineSurfaceTextureReady() {
            if (EmbedBottomSheet.this.videoView.isInline()) {
               EmbedBottomSheet.this.dismissInternal();
            }

         }

         public void onPlayStateChanged(WebPlayerView var1, boolean var2) {
            if (var2) {
               try {
                  EmbedBottomSheet.this.parentActivity.getWindow().addFlags(128);
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }
            } else {
               try {
                  EmbedBottomSheet.this.parentActivity.getWindow().clearFlags(128);
               } catch (Exception var3) {
                  FileLog.e((Throwable)var3);
               }
            }

         }

         public void onSharePressed() {
         }

         public TextureView onSwitchInlineMode(View var1, boolean var2, float var3, int var4, boolean var5) {
            if (var2) {
               var1.setTranslationY(0.0F);
               EmbedBottomSheet.this.pipVideoView = new PipVideoView();
               return EmbedBottomSheet.this.pipVideoView.show(EmbedBottomSheet.this.parentActivity, EmbedBottomSheet.this, var1, var3, var4, (WebView)null);
            } else {
               if (var5) {
                  EmbedBottomSheet.this.animationInProgress = true;
                  EmbedBottomSheet.this.videoView.getAspectRatioView().getLocationInWindow(EmbedBottomSheet.this.position);
                  int[] var8 = EmbedBottomSheet.this.position;
                  var8[0] -= EmbedBottomSheet.this.getLeftInset();
                  var8 = EmbedBottomSheet.this.position;
                  var8[1] = (int)((float)var8[1] - EmbedBottomSheet.access$3900(EmbedBottomSheet.this).getTranslationY());
                  TextureView var6 = EmbedBottomSheet.this.videoView.getTextureView();
                  ImageView var9 = EmbedBottomSheet.this.videoView.getTextureImageView();
                  AnimatorSet var7 = new AnimatorSet();
                  var7.playTogether(new Animator[]{ObjectAnimator.ofFloat(var9, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(var9, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(var9, "translationX", new float[]{(float)EmbedBottomSheet.this.position[0]}), ObjectAnimator.ofFloat(var9, "translationY", new float[]{(float)EmbedBottomSheet.this.position[1]}), ObjectAnimator.ofFloat(var6, "scaleX", new float[]{1.0F}), ObjectAnimator.ofFloat(var6, "scaleY", new float[]{1.0F}), ObjectAnimator.ofFloat(var6, "translationX", new float[]{(float)EmbedBottomSheet.this.position[0]}), ObjectAnimator.ofFloat(var6, "translationY", new float[]{(float)EmbedBottomSheet.this.position[1]}), ObjectAnimator.ofFloat(EmbedBottomSheet.access$4000(EmbedBottomSheet.this), "translationY", new float[]{0.0F}), ObjectAnimator.ofInt(EmbedBottomSheet.access$4100(EmbedBottomSheet.this), "alpha", new int[]{51})});
                  var7.setInterpolator(new DecelerateInterpolator());
                  var7.setDuration(250L);
                  var7.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        EmbedBottomSheet.this.animationInProgress = false;
                     }
                  });
                  var7.start();
               } else {
                  EmbedBottomSheet.access$4200(EmbedBottomSheet.this).setTranslationY(0.0F);
               }

               return null;
            }
         }

         public TextureView onSwitchToFullscreen(View var1, boolean var2, float var3, int var4, boolean var5) {
            if (var2) {
               EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(0);
               EmbedBottomSheet.this.fullscreenVideoContainer.setAlpha(1.0F);
               EmbedBottomSheet.this.fullscreenVideoContainer.addView(EmbedBottomSheet.this.videoView.getAspectRatioView());
               EmbedBottomSheet.this.wasInLandscape = false;
               EmbedBottomSheet.this.fullscreenedByButton = var5;
               if (EmbedBottomSheet.this.parentActivity != null) {
                  Exception var10000;
                  label53: {
                     boolean var10001;
                     try {
                        EmbedBottomSheet.this.prevOrientation = EmbedBottomSheet.this.parentActivity.getRequestedOrientation();
                     } catch (Exception var10) {
                        var10000 = var10;
                        var10001 = false;
                        break label53;
                     }

                     if (var5) {
                        label54: {
                           try {
                              if (((WindowManager)EmbedBottomSheet.this.parentActivity.getSystemService("window")).getDefaultDisplay().getRotation() == 3) {
                                 EmbedBottomSheet.this.parentActivity.setRequestedOrientation(8);
                                 break label54;
                              }
                           } catch (Exception var9) {
                              var10000 = var9;
                              var10001 = false;
                              break label53;
                           }

                           try {
                              EmbedBottomSheet.this.parentActivity.setRequestedOrientation(0);
                           } catch (Exception var8) {
                              var10000 = var8;
                              var10001 = false;
                              break label53;
                           }
                        }
                     }

                     try {
                        EmbedBottomSheet.access$2200(EmbedBottomSheet.this).setSystemUiVisibility(1028);
                        return null;
                     } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                     }
                  }

                  Exception var11 = var10000;
                  FileLog.e((Throwable)var11);
               }
            } else {
               EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
               EmbedBottomSheet.this.fullscreenedByButton = false;
               if (EmbedBottomSheet.this.parentActivity != null) {
                  try {
                     EmbedBottomSheet.access$2300(EmbedBottomSheet.this).setSystemUiVisibility(0);
                     EmbedBottomSheet.this.parentActivity.setRequestedOrientation(EmbedBottomSheet.this.prevOrientation);
                  } catch (Exception var6) {
                     FileLog.e((Throwable)var6);
                  }
               }
            }

            return null;
         }

         public void onVideoSizeChanged(float var1, int var2) {
         }

         public void prepareToSwitchInlineMode(boolean var1, final Runnable var2, float var3, boolean var4) {
            if (var1) {
               if (EmbedBottomSheet.this.parentActivity != null) {
                  try {
                     EmbedBottomSheet.access$2400(EmbedBottomSheet.this).setSystemUiVisibility(0);
                     if (EmbedBottomSheet.this.prevOrientation != -2) {
                        EmbedBottomSheet.this.parentActivity.setRequestedOrientation(EmbedBottomSheet.this.prevOrientation);
                     }
                  } catch (Exception var11) {
                     FileLog.e((Throwable)var11);
                  }
               }

               if (EmbedBottomSheet.this.fullscreenVideoContainer.getVisibility() == 0) {
                  EmbedBottomSheet.access$2600(EmbedBottomSheet.this).setTranslationY((float)(EmbedBottomSheet.access$2500(EmbedBottomSheet.this).getMeasuredHeight() + AndroidUtilities.dp(10.0F)));
                  EmbedBottomSheet.access$2700(EmbedBottomSheet.this).setAlpha(0);
               }

               EmbedBottomSheet.this.setOnShowListener((OnShowListener)null);
               if (var4) {
                  TextureView var6 = EmbedBottomSheet.this.videoView.getTextureView();
                  View var7 = EmbedBottomSheet.this.videoView.getControlsView();
                  ImageView var8 = EmbedBottomSheet.this.videoView.getTextureImageView();
                  Rect var9 = PipVideoView.getPipRect(var3);
                  var3 = var9.width / (float)var6.getWidth();
                  if (VERSION.SDK_INT >= 21) {
                     var9.y += (float)AndroidUtilities.statusBarHeight;
                  }

                  AnimatorSet var5 = new AnimatorSet();
                  var5.playTogether(new Animator[]{ObjectAnimator.ofFloat(var8, "scaleX", new float[]{var3}), ObjectAnimator.ofFloat(var8, "scaleY", new float[]{var3}), ObjectAnimator.ofFloat(var8, "translationX", new float[]{var9.x}), ObjectAnimator.ofFloat(var8, "translationY", new float[]{var9.y}), ObjectAnimator.ofFloat(var6, "scaleX", new float[]{var3}), ObjectAnimator.ofFloat(var6, "scaleY", new float[]{var3}), ObjectAnimator.ofFloat(var6, "translationX", new float[]{var9.x}), ObjectAnimator.ofFloat(var6, "translationY", new float[]{var9.y}), ObjectAnimator.ofFloat(EmbedBottomSheet.access$2800(EmbedBottomSheet.this), "translationY", new float[]{(float)(EmbedBottomSheet.access$2900(EmbedBottomSheet.this).getMeasuredHeight() + AndroidUtilities.dp(10.0F))}), ObjectAnimator.ofInt(EmbedBottomSheet.access$3000(EmbedBottomSheet.this), "alpha", new int[]{0}), ObjectAnimator.ofFloat(EmbedBottomSheet.this.fullscreenVideoContainer, "alpha", new float[]{0.0F}), ObjectAnimator.ofFloat(var7, "alpha", new float[]{0.0F})});
                  var5.setInterpolator(new DecelerateInterpolator());
                  var5.setDuration(250L);
                  var5.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationEnd(Animator var1) {
                        if (EmbedBottomSheet.this.fullscreenVideoContainer.getVisibility() == 0) {
                           EmbedBottomSheet.this.fullscreenVideoContainer.setAlpha(1.0F);
                           EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
                        }

                        var2.run();
                     }
                  });
                  var5.start();
               } else {
                  if (EmbedBottomSheet.this.fullscreenVideoContainer.getVisibility() == 0) {
                     EmbedBottomSheet.this.fullscreenVideoContainer.setAlpha(1.0F);
                     EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
                  }

                  var2.run();
                  EmbedBottomSheet.this.dismissInternal();
               }
            } else {
               if (ApplicationLoader.mainInterfacePaused) {
                  try {
                     Activity var12 = EmbedBottomSheet.this.parentActivity;
                     Intent var15 = new Intent(ApplicationLoader.applicationContext, BringAppForegroundService.class);
                     var12.startService(var15);
                  } catch (Throwable var10) {
                     FileLog.e(var10);
                  }
               }

               if (var4) {
                  EmbedBottomSheet var13 = EmbedBottomSheet.this;
                  var13.setOnShowListener(var13.onShowListener);
                  Rect var14 = PipVideoView.getPipRect(var3);
                  TextureView var17 = EmbedBottomSheet.this.videoView.getTextureView();
                  ImageView var16 = EmbedBottomSheet.this.videoView.getTextureImageView();
                  var3 = var14.width / (float)var17.getLayoutParams().width;
                  if (VERSION.SDK_INT >= 21) {
                     var14.y += (float)AndroidUtilities.statusBarHeight;
                  }

                  var16.setScaleX(var3);
                  var16.setScaleY(var3);
                  var16.setTranslationX(var14.x);
                  var16.setTranslationY(var14.y);
                  var17.setScaleX(var3);
                  var17.setScaleY(var3);
                  var17.setTranslationX(var14.x);
                  var17.setTranslationY(var14.y);
               } else {
                  EmbedBottomSheet.this.pipVideoView.close();
                  EmbedBottomSheet.this.pipVideoView = null;
               }

               EmbedBottomSheet.this.setShowWithoutAnimation(true);
               EmbedBottomSheet.this.show();
               if (var4) {
                  EmbedBottomSheet.this.waitingForDraw = 4;
                  EmbedBottomSheet.access$3300(EmbedBottomSheet.this).setAlpha(1);
                  EmbedBottomSheet.access$3500(EmbedBottomSheet.this).setTranslationY((float)(EmbedBottomSheet.access$3400(EmbedBottomSheet.this).getMeasuredHeight() + AndroidUtilities.dp(10.0F)));
               }
            }

         }
      });
      this.videoView.setVisibility(4);
      var14 = this.containerLayout;
      WebPlayerView var16 = this.videoView;
      if (this.hasDescription) {
         var20 = 22;
      } else {
         var20 = 0;
      }

      var14.addView(var16, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, (float)(var20 + 84 - 10)));
      this.progressBarBlackBackground = new View(var1);
      this.progressBarBlackBackground.setBackgroundColor(-16777216);
      this.progressBarBlackBackground.setVisibility(4);
      var14 = this.containerLayout;
      View var18 = this.progressBarBlackBackground;
      if (this.hasDescription) {
         var20 = 22;
      } else {
         var20 = 0;
      }

      var14.addView(var18, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, (float)(var20 + 84)));
      this.progressBar = new RadialProgressView(var1);
      this.progressBar.setVisibility(4);
      FrameLayout var21 = this.containerLayout;
      RadialProgressView var17 = this.progressBar;
      if (this.hasDescription) {
         var20 = var23;
      } else {
         var20 = 0;
      }

      var21.addView(var17, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 0.0F, 0.0F, (float)((var20 + 84) / 2)));
      if (this.hasDescription) {
         TextView var19 = new TextView(var1);
         var19.setTextSize(1, 16.0F);
         var19.setTextColor(Theme.getColor("dialogTextBlack"));
         var19.setText(var3);
         var19.setSingleLine(true);
         var19.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         var19.setEllipsize(TruncateAt.END);
         var19.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         this.containerLayout.addView(var19, LayoutHelper.createFrame(-1, -2.0F, 83, 0.0F, 0.0F, 0.0F, 77.0F));
      }

      TextView var11 = new TextView(var1);
      var11.setTextSize(1, 14.0F);
      var11.setTextColor(Theme.getColor("dialogTextGray"));
      var11.setText(var2);
      var11.setSingleLine(true);
      var11.setEllipsize(TruncateAt.END);
      var11.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      this.containerLayout.addView(var11, LayoutHelper.createFrame(-1, -2.0F, 83, 0.0F, 0.0F, 0.0F, 57.0F));
      View var9 = new View(var1);
      var9.setBackgroundColor(Theme.getColor("dialogGrayLine"));
      this.containerLayout.addView(var9, new LayoutParams(-1, 1, 83));
      ((LayoutParams)var9.getLayoutParams()).bottomMargin = AndroidUtilities.dp(48.0F);
      var14 = new FrameLayout(var1);
      var14.setBackgroundColor(Theme.getColor("dialogBackground"));
      this.containerLayout.addView(var14, LayoutHelper.createFrame(-1, 48, 83));
      LinearLayout var10 = new LinearLayout(var1);
      var10.setOrientation(0);
      var10.setWeightSum(1.0F);
      var14.addView(var10, LayoutHelper.createFrame(-2, -1, 53));
      var11 = new TextView(var1);
      var11.setTextSize(1, 14.0F);
      var11.setTextColor(Theme.getColor("dialogTextBlue4"));
      var11.setGravity(17);
      var11.setSingleLine(true);
      var11.setEllipsize(TruncateAt.END);
      var11.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
      var11.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      var11.setText(LocaleController.getString("Close", 2131559117).toUpperCase());
      var11.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var14.addView(var11, LayoutHelper.createLinear(-2, -1, 51));
      var11.setOnClickListener(new _$$Lambda$EmbedBottomSheet$y37DL_JjK8uMG3joIQ1jW7ubjuE(this));
      this.imageButtonsContainer = new LinearLayout(var1);
      this.imageButtonsContainer.setVisibility(4);
      var14.addView(this.imageButtonsContainer, LayoutHelper.createFrame(-2, -1, 17));
      this.pipButton = new ImageView(var1);
      this.pipButton.setScaleType(ScaleType.CENTER);
      this.pipButton.setImageResource(2131165908);
      this.pipButton.setEnabled(false);
      this.pipButton.setAlpha(0.5F);
      this.pipButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogTextBlue4"), Mode.MULTIPLY));
      this.pipButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
      this.imageButtonsContainer.addView(this.pipButton, LayoutHelper.createFrame(48, 48.0F, 51, 0.0F, 0.0F, 4.0F, 0.0F));
      this.pipButton.setOnClickListener(new _$$Lambda$EmbedBottomSheet$ME3BZek9Olzkn_G2jOkI4EyTOfQ(this));
      _$$Lambda$EmbedBottomSheet$xNtglytXCFyYO3cCP1Moj3HVHKg var22 = new _$$Lambda$EmbedBottomSheet$xNtglytXCFyYO3cCP1Moj3HVHKg(this);
      ImageView var12 = new ImageView(var1);
      var12.setScaleType(ScaleType.CENTER);
      var12.setImageResource(2131165901);
      var12.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogTextBlue4"), Mode.MULTIPLY));
      var12.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
      this.imageButtonsContainer.addView(var12, LayoutHelper.createFrame(48, 48, 51));
      var12.setOnClickListener(var22);
      this.copyTextButton = new TextView(var1);
      this.copyTextButton.setTextSize(1, 14.0F);
      this.copyTextButton.setTextColor(Theme.getColor("dialogTextBlue4"));
      this.copyTextButton.setGravity(17);
      this.copyTextButton.setSingleLine(true);
      this.copyTextButton.setEllipsize(TruncateAt.END);
      this.copyTextButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
      this.copyTextButton.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      this.copyTextButton.setText(LocaleController.getString("Copy", 2131559163).toUpperCase());
      this.copyTextButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var10.addView(this.copyTextButton, LayoutHelper.createFrame(-2, -1, 51));
      this.copyTextButton.setOnClickListener(var22);
      this.openInButton = new TextView(var1);
      this.openInButton.setTextSize(1, 14.0F);
      this.openInButton.setTextColor(Theme.getColor("dialogTextBlue4"));
      this.openInButton.setGravity(17);
      this.openInButton.setSingleLine(true);
      this.openInButton.setEllipsize(TruncateAt.END);
      this.openInButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
      this.openInButton.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      this.openInButton.setText(LocaleController.getString("OpenInBrowser", 2131560115).toUpperCase());
      this.openInButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var10.addView(this.openInButton, LayoutHelper.createFrame(-2, -1, 51));
      this.openInButton.setOnClickListener(new _$$Lambda$EmbedBottomSheet$TOguFKwBNAuHY9JGAFn6CeA5SYI(this));
      this.setDelegate(new BottomSheet.BottomSheetDelegate() {
         public boolean canDismiss() {
            if (EmbedBottomSheet.this.videoView.isInFullscreen()) {
               EmbedBottomSheet.this.videoView.exitFullscreen();
               return false;
            } else {
               try {
                  EmbedBottomSheet.this.parentActivity.getWindow().clearFlags(128);
               } catch (Exception var2) {
                  FileLog.e((Throwable)var2);
               }

               return true;
            }
         }

         public void onOpenAnimationEnd() {
            if (EmbedBottomSheet.this.videoView.loadVideo(EmbedBottomSheet.this.embedUrl, (TLRPC.Photo)null, (Object)null, EmbedBottomSheet.this.openUrl, true)) {
               EmbedBottomSheet.this.progressBar.setVisibility(4);
               EmbedBottomSheet.this.webView.setVisibility(4);
               EmbedBottomSheet.this.videoView.setVisibility(0);
            } else {
               EmbedBottomSheet.this.progressBar.setVisibility(0);
               EmbedBottomSheet.this.webView.setVisibility(0);
               EmbedBottomSheet.this.imageButtonsContainer.setVisibility(0);
               EmbedBottomSheet.this.copyTextButton.setVisibility(4);
               EmbedBottomSheet.this.webView.setKeepScreenOn(true);
               EmbedBottomSheet.this.videoView.setVisibility(4);
               EmbedBottomSheet.this.videoView.getControlsView().setVisibility(4);
               EmbedBottomSheet.this.videoView.getTextureView().setVisibility(4);
               if (EmbedBottomSheet.this.videoView.getTextureImageView() != null) {
                  EmbedBottomSheet.this.videoView.getTextureImageView().setVisibility(4);
               }

               EmbedBottomSheet.this.videoView.loadVideo((String)null, (TLRPC.Photo)null, (Object)null, (String)null, false);
               HashMap var1 = new HashMap();
               var1.put("Referer", "http://youtube.com");

               Exception var10000;
               Exception var18;
               label98: {
                  String var2;
                  boolean var10001;
                  try {
                     var2 = EmbedBottomSheet.this.videoView.getYoutubeId();
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label98;
                  }

                  if (var2 != null) {
                     label106: {
                        try {
                           EmbedBottomSheet.this.progressBarBlackBackground.setVisibility(0);
                           EmbedBottomSheet.this.youtubeLogoImage.setVisibility(0);
                           EmbedBottomSheet.this.youtubeLogoImage.setImageResource(2131165914);
                           EmbedBottomSheet.this.isYouTube = true;
                           if (VERSION.SDK_INT >= 17) {
                              WebView var3 = EmbedBottomSheet.this.webView;
                              EmbedBottomSheet.YoutubeProxy var16 = EmbedBottomSheet.this.new YoutubeProxy();
                              var3.addJavascriptInterface(var16, "YoutubeProxy");
                           }
                        } catch (Exception var13) {
                           var10000 = var13;
                           var10001 = false;
                           break label106;
                        }

                        String var17;
                        try {
                           var17 = EmbedBottomSheet.this.openUrl;
                        } catch (Exception var12) {
                           var10000 = var12;
                           var10001 = false;
                           break label106;
                        }

                        int var5;
                        label110: {
                           if (var17 != null) {
                              label109: {
                                 label108: {
                                    Uri var4;
                                    String var19;
                                    try {
                                       var4 = Uri.parse(EmbedBottomSheet.this.openUrl);
                                       var19 = var4.getQueryParameter("t");
                                    } catch (Exception var10) {
                                       var10000 = var10;
                                       var10001 = false;
                                       break label108;
                                    }

                                    var17 = var19;
                                    if (var19 == null) {
                                       try {
                                          var17 = var4.getQueryParameter("time_continue");
                                       } catch (Exception var9) {
                                          var10000 = var9;
                                          var10001 = false;
                                          break label108;
                                       }
                                    }

                                    if (var17 == null) {
                                       break label109;
                                    }

                                    try {
                                       if (var17.contains("m")) {
                                          String[] var20 = var17.split("m");
                                          var5 = Utilities.parseInt(var20[0]) * 60 + Utilities.parseInt(var20[1]);
                                          break label110;
                                       }
                                    } catch (Exception var11) {
                                       var10000 = var11;
                                       var10001 = false;
                                       break label108;
                                    }

                                    try {
                                       var5 = Utilities.parseInt(var17);
                                       break label110;
                                    } catch (Exception var8) {
                                       var10000 = var8;
                                       var10001 = false;
                                    }
                                 }

                                 var18 = var10000;

                                 try {
                                    FileLog.e((Throwable)var18);
                                 } catch (Exception var7) {
                                    var10000 = var7;
                                    var10001 = false;
                                    break label106;
                                 }
                              }
                           }

                           var5 = 0;
                        }

                        try {
                           EmbedBottomSheet.this.webView.loadDataWithBaseURL("https://www.youtube.com", String.format(Locale.US, "<!DOCTYPE html><html><head><style>body { margin: 0; width:100%%; height:100%%;  background-color:#000; }html { width:100%%; height:100%%; background-color:#000; }.embed-container iframe,.embed-container object,   .embed-container embed {       position: absolute;       top: 0;       left: 0;       width: 100%% !important;       height: 100%% !important;   }   </style></head><body>   <div class=\"embed-container\">       <div id=\"player\"></div>   </div>   <script src=\"https://www.youtube.com/iframe_api\"></script>   <script>   var player;   var observer;   var videoEl;   var playing;   var posted = false;   YT.ready(function() {       player = new YT.Player(\"player\", {                              \"width\" : \"100%%\",                              \"events\" : {                              \"onReady\" : \"onReady\",                              \"onError\" : \"onError\",                              },                              \"videoId\" : \"%1$s\",                              \"height\" : \"100%%\",                              \"playerVars\" : {                              \"start\" : %2$d,                              \"rel\" : 0,                              \"showinfo\" : 0,                              \"modestbranding\" : 1,                              \"iv_load_policy\" : 3,                              \"autohide\" : 1,                              \"autoplay\" : 1,                              \"cc_load_policy\" : 1,                              \"playsinline\" : 1,                              \"controls\" : 1                              }                            });        player.setSize(window.innerWidth, window.innerHeight);    });    function hideControls() {        playing = !videoEl.paused;       videoEl.controls = 0;       observer.observe(videoEl, {attributes: true});    }    function showControls() {        playing = !videoEl.paused;       observer.disconnect();       videoEl.controls = 1;    }    function onError(event) {       if (!posted) {            if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);             }            posted = true;       }    }    function onReady(event) {       player.playVideo();       videoEl = player.getIframe().contentDocument.getElementsByTagName('video')[0];\n       videoEl.addEventListener(\"canplay\", function() {            if (playing) {               videoEl.play();            }       }, true);       videoEl.addEventListener(\"timeupdate\", function() {            if (!posted && videoEl.currentTime > 0) {               if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);                }               posted = true;           }       }, true);       observer = new MutationObserver(function() {\n          if (videoEl.controls) {\n               videoEl.controls = 0;\n          }       });\n    }    window.onresize = function() {        player.setSize(window.innerWidth, window.innerHeight);    }    </script></body></html>", var2, var5), "text/html", "UTF-8", "http://youtube.com");
                           return;
                        } catch (Exception var6) {
                           var10000 = var6;
                           var10001 = false;
                        }
                     }
                  } else {
                     try {
                        EmbedBottomSheet.this.webView.loadUrl(EmbedBottomSheet.this.embedUrl, var1);
                        return;
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                     }
                  }
               }

               var18 = var10000;
               FileLog.e((Throwable)var18);
            }

         }
      });
      this.orientationEventListener = new OrientationEventListener(ApplicationLoader.applicationContext) {
         public void onOrientationChanged(int var1) {
            if (EmbedBottomSheet.this.orientationEventListener != null && EmbedBottomSheet.this.videoView.getVisibility() == 0 && EmbedBottomSheet.this.parentActivity != null && EmbedBottomSheet.this.videoView.isInFullscreen() && EmbedBottomSheet.this.fullscreenedByButton) {
               if (var1 >= 240 && var1 <= 300) {
                  EmbedBottomSheet.this.wasInLandscape = true;
               } else if (EmbedBottomSheet.this.wasInLandscape && (var1 >= 330 || var1 <= 30)) {
                  EmbedBottomSheet.this.parentActivity.setRequestedOrientation(EmbedBottomSheet.this.prevOrientation);
                  EmbedBottomSheet.this.fullscreenedByButton = false;
                  EmbedBottomSheet.this.wasInLandscape = false;
               }
            }

         }
      };
      if (this.orientationEventListener.canDetectOrientation()) {
         this.orientationEventListener.enable();
      } else {
         this.orientationEventListener.disable();
         this.orientationEventListener = null;
      }

      instance = this;
   }

   // $FF: synthetic method
   static ViewGroup access$2200(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$2300(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$2400(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$2500(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$2600(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ColorDrawable access$2700(EmbedBottomSheet var0) {
      return var0.backDrawable;
   }

   // $FF: synthetic method
   static ViewGroup access$2800(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$2900(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ColorDrawable access$3000(EmbedBottomSheet var0) {
      return var0.backDrawable;
   }

   // $FF: synthetic method
   static ColorDrawable access$3300(EmbedBottomSheet var0) {
      return var0.backDrawable;
   }

   // $FF: synthetic method
   static ViewGroup access$3400(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$3500(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$3900(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ViewGroup access$4000(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static ColorDrawable access$4100(EmbedBottomSheet var0) {
      return var0.backDrawable;
   }

   // $FF: synthetic method
   static ViewGroup access$4200(EmbedBottomSheet var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static BottomSheet.ContainerView access$4300(EmbedBottomSheet var0) {
      return var0.container;
   }

   public static EmbedBottomSheet getInstance() {
      return instance;
   }

   // $FF: synthetic method
   static boolean lambda$new$0(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static boolean lambda$new$1(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static boolean lambda$new$2(View var0, MotionEvent var1) {
      return true;
   }

   private void runJsCode(String var1) {
      if (VERSION.SDK_INT >= 21) {
         this.webView.evaluateJavascript(var1, (ValueCallback)null);
      } else {
         try {
            WebView var2 = this.webView;
            StringBuilder var3 = new StringBuilder();
            var3.append("javascript:");
            var3.append(var1);
            var2.loadUrl(var3.toString());
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }
      }

   }

   public static void show(Context var0, String var1, String var2, String var3, String var4, int var5, int var6) {
      EmbedBottomSheet var7 = instance;
      if (var7 != null) {
         var7.destroy();
      }

      (new EmbedBottomSheet(var0, var1, var2, var3, var4, var5, var6)).show();
   }

   private void showOrHideYoutubeLogo(final boolean var1) {
      ViewPropertyAnimator var2 = this.youtubeLogoImage.animate();
      float var3;
      if (var1) {
         var3 = 1.0F;
      } else {
         var3 = 0.0F;
      }

      var2 = var2.alpha(var3).setDuration(200L);
      long var4;
      if (var1) {
         var4 = 0L;
      } else {
         var4 = 2900L;
      }

      var2.setStartDelay(var4).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1x) {
            if (var1) {
               EmbedBottomSheet.this.showOrHideYoutubeLogo(false);
            }

         }
      }).start();
   }

   protected boolean canDismissWithSwipe() {
      boolean var1;
      if (this.videoView.getVisibility() == 0 && this.videoView.isInFullscreen()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   protected boolean canDismissWithTouchOutside() {
      boolean var1;
      if (this.fullscreenVideoContainer.getVisibility() != 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean checkInlinePermissions() {
      Activity var1 = this.parentActivity;
      if (var1 == null) {
         return false;
      } else if (VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(var1)) {
         (new AlertDialog.Builder(this.parentActivity)).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("PermissionDrawAboveOtherApps", 2131560413)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new OnClickListener() {
            @TargetApi(23)
            public void onClick(DialogInterface var1, int var2) {
               if (EmbedBottomSheet.this.parentActivity != null) {
                  Activity var4 = EmbedBottomSheet.this.parentActivity;
                  StringBuilder var3 = new StringBuilder();
                  var3.append("package:");
                  var3.append(EmbedBottomSheet.this.parentActivity.getPackageName());
                  var4.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(var3.toString())));
               }

            }
         }).show();
         return false;
      } else {
         return true;
      }
   }

   public void destroy() {
      WebView var1 = this.webView;
      if (var1 != null && var1.getVisibility() == 0) {
         this.containerLayout.removeView(this.webView);
         this.webView.stopLoading();
         this.webView.loadUrl("about:blank");
         this.webView.destroy();
      }

      PipVideoView var2 = this.pipVideoView;
      if (var2 != null) {
         var2.close();
         this.pipVideoView = null;
      }

      WebPlayerView var3 = this.videoView;
      if (var3 != null) {
         var3.destroy();
      }

      instance = null;
      this.dismissInternal();
   }

   public void exitFromPip() {
      if (this.webView != null && this.pipVideoView != null) {
         if (ApplicationLoader.mainInterfacePaused) {
            try {
               Activity var1 = this.parentActivity;
               Intent var2 = new Intent(ApplicationLoader.applicationContext, BringAppForegroundService.class);
               var1.startService(var2);
            } catch (Throwable var4) {
               FileLog.e(var4);
            }
         }

         if (this.isYouTube) {
            this.runJsCode("showControls();");
         }

         ViewGroup var5 = (ViewGroup)this.webView.getParent();
         if (var5 != null) {
            var5.removeView(this.webView);
         }

         FrameLayout var7 = this.containerLayout;
         WebView var6 = this.webView;
         byte var3;
         if (this.hasDescription) {
            var3 = 22;
         } else {
            var3 = 0;
         }

         var7.addView(var6, 0, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, (float)(var3 + 84)));
         this.setShowWithoutAnimation(true);
         this.show();
         this.pipVideoView.close();
         this.pipVideoView = null;
      }

   }

   // $FF: synthetic method
   public void lambda$new$3$EmbedBottomSheet(View var1) {
      if (this.youtubeLogoImage.getAlpha() != 0.0F) {
         this.openInButton.callOnClick();
      }
   }

   // $FF: synthetic method
   public void lambda$new$4$EmbedBottomSheet(View var1) {
      this.dismiss();
   }

   // $FF: synthetic method
   public void lambda$new$5$EmbedBottomSheet(View var1) {
      if (this.checkInlinePermissions()) {
         if (this.progressBar.getVisibility() != 0) {
            Activity var2;
            float var5;
            PipVideoView var6;
            label21: {
               this.pipVideoView = new PipVideoView();
               var6 = this.pipVideoView;
               var2 = this.parentActivity;
               int var3 = this.width;
               if (var3 != 0) {
                  int var4 = this.height;
                  if (var4 != 0) {
                     var5 = (float)var3 / (float)var4;
                     break label21;
                  }
               }

               var5 = 1.0F;
            }

            var6.show(var2, this, (View)null, var5, 0, this.webView);
            if (this.isYouTube) {
               this.runJsCode("hideControls();");
            }

            super.containerView.setTranslationY(0.0F);
            this.dismissInternal();
         }
      }
   }

   // $FF: synthetic method
   public void lambda$new$6$EmbedBottomSheet(View var1) {
      try {
         ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.openUrl));
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      Toast.makeText(this.getContext(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
      this.dismiss();
   }

   // $FF: synthetic method
   public void lambda$new$7$EmbedBottomSheet(View var1) {
      Browser.openUrl(this.parentActivity, (String)this.openUrl);
      this.dismiss();
   }

   public void onConfigurationChanged(Configuration var1) {
      if (this.videoView.getVisibility() == 0 && this.videoView.isInitied() && !this.videoView.isInline()) {
         if (var1.orientation == 2) {
            if (!this.videoView.isInFullscreen()) {
               this.videoView.enterFullscreen();
            }
         } else if (this.videoView.isInFullscreen()) {
            this.videoView.exitFullscreen();
         }
      }

      PipVideoView var2 = this.pipVideoView;
      if (var2 != null) {
         var2.onConfigurationChanged();
      }

   }

   public void onContainerDraw(Canvas var1) {
      int var2 = this.waitingForDraw;
      if (var2 != 0) {
         this.waitingForDraw = var2 - 1;
         if (this.waitingForDraw == 0) {
            this.videoView.updateTextureImageView();
            this.pipVideoView.close();
            this.pipVideoView = null;
         } else {
            super.container.invalidate();
         }
      }

   }

   protected void onContainerTranslationYChanged(float var1) {
      this.updateTextureViewPosition();
   }

   protected boolean onCustomLayout(View var1, int var2, int var3, int var4, int var5) {
      if (var1 == this.videoView.getControlsView()) {
         this.updateTextureViewPosition();
      }

      return false;
   }

   protected boolean onCustomMeasure(View var1, int var2, int var3) {
      if (var1 == this.videoView.getControlsView()) {
         android.view.ViewGroup.LayoutParams var4 = var1.getLayoutParams();
         var4.width = this.videoView.getMeasuredWidth();
         var3 = this.videoView.getAspectRatioView().getMeasuredHeight();
         if (this.videoView.isInFullscreen()) {
            var2 = 0;
         } else {
            var2 = AndroidUtilities.dp(10.0F);
         }

         var4.height = var3 + var2;
      }

      return false;
   }

   public void pause() {
      WebPlayerView var1 = this.videoView;
      if (var1 != null && var1.isInitied()) {
         this.videoView.pause();
      }

   }

   public void updateTextureViewPosition() {
      this.videoView.getAspectRatioView().getLocationInWindow(this.position);
      int[] var1 = this.position;
      var1[0] -= this.getLeftInset();
      if (!this.videoView.isInline() && !this.animationInProgress) {
         TextureView var2 = this.videoView.getTextureView();
         var2.setTranslationX((float)this.position[0]);
         var2.setTranslationY((float)this.position[1]);
         ImageView var3 = this.videoView.getTextureImageView();
         if (var3 != null) {
            var3.setTranslationX((float)this.position[0]);
            var3.setTranslationY((float)this.position[1]);
         }
      }

      View var4 = this.videoView.getControlsView();
      if (var4.getParent() == super.container) {
         var4.setTranslationY((float)this.position[1]);
      } else {
         var4.setTranslationY(0.0F);
      }

   }

   private class YoutubeProxy {
      private YoutubeProxy() {
      }

      // $FF: synthetic method
      YoutubeProxy(Object var2) {
         this();
      }

      // $FF: synthetic method
      public void lambda$postEvent$0$EmbedBottomSheet$YoutubeProxy(String var1) {
         byte var2;
         if (var1.hashCode() == -1097519099 && var1.equals("loaded")) {
            var2 = 0;
         } else {
            var2 = -1;
         }

         if (var2 == 0) {
            EmbedBottomSheet.this.progressBar.setVisibility(4);
            EmbedBottomSheet.this.progressBarBlackBackground.setVisibility(4);
            EmbedBottomSheet.this.pipButton.setEnabled(true);
            EmbedBottomSheet.this.pipButton.setAlpha(1.0F);
            EmbedBottomSheet.this.showOrHideYoutubeLogo(false);
         }

      }

      @JavascriptInterface
      public void postEvent(String var1, String var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$EmbedBottomSheet$YoutubeProxy$E5aY7hyeNKMq4h2nlfDXomuSVno(this, var1));
      }
   }
}
