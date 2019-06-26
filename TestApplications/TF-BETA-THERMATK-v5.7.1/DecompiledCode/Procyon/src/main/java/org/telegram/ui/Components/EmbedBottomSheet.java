// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.webkit.JavascriptInterface;
import android.graphics.Canvas;
import android.content.res.Configuration;
import org.telegram.messenger.browser.Browser;
import android.widget.Toast;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.annotation.TargetApi;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.provider.Settings;
import android.view.ViewPropertyAnimator;
import android.webkit.ValueCallback;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Point;
import java.util.Locale;
import org.telegram.messenger.Utilities;
import android.net.Uri;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import org.telegram.messenger.LocaleController;
import android.widget.FrameLayout$LayoutParams;
import android.text.TextUtils$TruncateAt;
import org.telegram.ui.ActionBar.Theme;
import android.content.Intent;
import org.telegram.messenger.BringAppForegroundService;
import org.telegram.messenger.ApplicationLoader;
import android.view.WindowManager;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.TextureView;
import java.util.Map;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;
import android.view.ViewGroup;
import android.view.View$OnClickListener;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.CookieManager;
import android.view.MotionEvent;
import android.view.View$MeasureSpec;
import org.telegram.messenger.FileLog;
import android.view.ViewGroup$LayoutParams;
import android.view.View$OnTouchListener;
import android.os.Build$VERSION;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.content.DialogInterface;
import android.content.Context;
import android.webkit.WebView;
import android.widget.ImageView;
import android.app.Activity;
import android.view.OrientationEventListener;
import android.content.DialogInterface$OnShowListener;
import android.widget.LinearLayout;
import android.webkit.WebChromeClient$CustomViewCallback;
import android.view.View;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.annotation.SuppressLint;
import org.telegram.ui.ActionBar.BottomSheet;

public class EmbedBottomSheet extends BottomSheet
{
    @SuppressLint({ "StaticFieldLeak" })
    private static EmbedBottomSheet instance;
    private boolean animationInProgress;
    private FrameLayout containerLayout;
    private TextView copyTextButton;
    private View customView;
    private WebChromeClient$CustomViewCallback customViewCallback;
    private String embedUrl;
    private FrameLayout fullscreenVideoContainer;
    private boolean fullscreenedByButton;
    private boolean hasDescription;
    private int height;
    private LinearLayout imageButtonsContainer;
    private boolean isYouTube;
    private int lastOrientation;
    private DialogInterface$OnShowListener onShowListener;
    private TextView openInButton;
    private String openUrl;
    private OrientationEventListener orientationEventListener;
    private Activity parentActivity;
    private ImageView pipButton;
    private PipVideoView pipVideoView;
    private int[] position;
    private int prevOrientation;
    private RadialProgressView progressBar;
    private View progressBarBlackBackground;
    private WebPlayerView videoView;
    private int waitingForDraw;
    private boolean wasInLandscape;
    private WebView webView;
    private int width;
    private final String youtubeFrame;
    private ImageView youtubeLogoImage;
    
    @SuppressLint({ "SetJavaScriptEnabled" })
    private EmbedBottomSheet(final Context context, final String text, final String text2, final String openUrl, final String embedUrl, int width, int height) {
        super(context, false, 0);
        this.position = new int[2];
        this.lastOrientation = -1;
        this.prevOrientation = -2;
        this.youtubeFrame = "<!DOCTYPE html><html><head><style>body { margin: 0; width:100%%; height:100%%;  background-color:#000; }html { width:100%%; height:100%%; background-color:#000; }.embed-container iframe,.embed-container object,   .embed-container embed {       position: absolute;       top: 0;       left: 0;       width: 100%% !important;       height: 100%% !important;   }   </style></head><body>   <div class=\"embed-container\">       <div id=\"player\"></div>   </div>   <script src=\"https://www.youtube.com/iframe_api\"></script>   <script>   var player;   var observer;   var videoEl;   var playing;   var posted = false;   YT.ready(function() {       player = new YT.Player(\"player\", {                              \"width\" : \"100%%\",                              \"events\" : {                              \"onReady\" : \"onReady\",                              \"onError\" : \"onError\",                              },                              \"videoId\" : \"%1$s\",                              \"height\" : \"100%%\",                              \"playerVars\" : {                              \"start\" : %2$d,                              \"rel\" : 0,                              \"showinfo\" : 0,                              \"modestbranding\" : 1,                              \"iv_load_policy\" : 3,                              \"autohide\" : 1,                              \"autoplay\" : 1,                              \"cc_load_policy\" : 1,                              \"playsinline\" : 1,                              \"controls\" : 1                              }                            });        player.setSize(window.innerWidth, window.innerHeight);    });    function hideControls() {        playing = !videoEl.paused;       videoEl.controls = 0;       observer.observe(videoEl, {attributes: true});    }    function showControls() {        playing = !videoEl.paused;       observer.disconnect();       videoEl.controls = 1;    }    function onError(event) {       if (!posted) {            if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);             }            posted = true;       }    }    function onReady(event) {       player.playVideo();       videoEl = player.getIframe().contentDocument.getElementsByTagName('video')[0];\n       videoEl.addEventListener(\"canplay\", function() {            if (playing) {               videoEl.play();            }       }, true);       videoEl.addEventListener(\"timeupdate\", function() {            if (!posted && videoEl.currentTime > 0) {               if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);                }               posted = true;           }       }, true);       observer = new MutationObserver(function() {\n          if (videoEl.controls) {\n               videoEl.controls = 0;\n          }       });\n    }    window.onresize = function() {        player.setSize(window.innerWidth, window.innerHeight);    }    </script></body></html>";
        this.onShowListener = (DialogInterface$OnShowListener)new DialogInterface$OnShowListener() {
            public void onShow(final DialogInterface dialogInterface) {
                if (EmbedBottomSheet.this.pipVideoView != null && EmbedBottomSheet.this.videoView.isInline()) {
                    EmbedBottomSheet.this.videoView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                        public boolean onPreDraw() {
                            EmbedBottomSheet.this.videoView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
                            return true;
                        }
                    });
                }
            }
        };
        super.fullWidth = true;
        this.setApplyTopPadding(false);
        this.setApplyBottomPadding(false);
        if (context instanceof Activity) {
            this.parentActivity = (Activity)context;
        }
        this.embedUrl = embedUrl;
        this.hasDescription = (text2 != null && text2.length() > 0);
        this.openUrl = openUrl;
        this.width = width;
        this.height = height;
        if (this.width == 0 || this.height == 0) {
            final Point displaySize = AndroidUtilities.displaySize;
            this.width = displaySize.x;
            this.height = displaySize.y / 2;
        }
        (this.fullscreenVideoContainer = new FrameLayout(context)).setKeepScreenOn(true);
        this.fullscreenVideoContainer.setBackgroundColor(-16777216);
        if (Build$VERSION.SDK_INT >= 21) {
            this.fullscreenVideoContainer.setFitsSystemWindows(true);
        }
        this.fullscreenVideoContainer.setOnTouchListener((View$OnTouchListener)_$$Lambda$EmbedBottomSheet$awgbBJJ9KT_HOZj5dBev_ioRvPo.INSTANCE);
        super.container.addView((View)this.fullscreenVideoContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.fullscreenVideoContainer.setVisibility(4);
        this.fullscreenVideoContainer.setOnTouchListener((View$OnTouchListener)_$$Lambda$EmbedBottomSheet$wxc0LCDO5uuH8qs6VYdAXQWEEZE.INSTANCE);
        (this.containerLayout = new FrameLayout(context) {
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                try {
                    if ((EmbedBottomSheet.this.pipVideoView == null || EmbedBottomSheet.this.webView.getVisibility() != 0) && EmbedBottomSheet.this.webView.getParent() != null) {
                        this.removeView((View)EmbedBottomSheet.this.webView);
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
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            
            protected void onMeasure(final int n, int size) {
                size = View$MeasureSpec.getSize(n);
                final int n2 = (int)Math.min(EmbedBottomSheet.this.height / (EmbedBottomSheet.this.width / (float)size), (float)(AndroidUtilities.displaySize.y / 2));
                if (EmbedBottomSheet.this.hasDescription) {
                    size = 22;
                }
                else {
                    size = 0;
                }
                super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(n2 + AndroidUtilities.dp((float)(size + 84)) + 1, 1073741824));
            }
        }).setOnTouchListener((View$OnTouchListener)_$$Lambda$EmbedBottomSheet$iYWCViD8wKuconyj_uGO4wKzEcQ.INSTANCE);
        this.setCustomView((View)this.containerLayout);
        this.webView = new WebView(context) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (EmbedBottomSheet.this.isYouTube && motionEvent.getAction() == 0) {
                    EmbedBottomSheet.this.showOrHideYoutubeLogo(true);
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        if (Build$VERSION.SDK_INT >= 17) {
            this.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.webView.getSettings().setMixedContentMode(0);
            CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
        }
        this.webView.setWebChromeClient((WebChromeClient)new WebChromeClient() {
            public void onHideCustomView() {
                super.onHideCustomView();
                if (EmbedBottomSheet.this.customView == null) {
                    return;
                }
                EmbedBottomSheet.this.getSheetContainer().setVisibility(0);
                EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
                EmbedBottomSheet.this.fullscreenVideoContainer.removeView(EmbedBottomSheet.this.customView);
                if (EmbedBottomSheet.this.customViewCallback != null && !EmbedBottomSheet.this.customViewCallback.getClass().getName().contains(".chromium.")) {
                    EmbedBottomSheet.this.customViewCallback.onCustomViewHidden();
                }
                EmbedBottomSheet.this.customView = null;
            }
            
            public void onShowCustomView(final View view, final int n, final WebChromeClient$CustomViewCallback webChromeClient$CustomViewCallback) {
                this.onShowCustomView(view, webChromeClient$CustomViewCallback);
            }
            
            public void onShowCustomView(final View view, final WebChromeClient$CustomViewCallback webChromeClient$CustomViewCallback) {
                if (EmbedBottomSheet.this.customView == null && EmbedBottomSheet.this.pipVideoView == null) {
                    EmbedBottomSheet.this.exitFromPip();
                    EmbedBottomSheet.this.customView = view;
                    EmbedBottomSheet.this.getSheetContainer().setVisibility(4);
                    EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(0);
                    EmbedBottomSheet.this.fullscreenVideoContainer.addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                    EmbedBottomSheet.this.customViewCallback = webChromeClient$CustomViewCallback;
                    return;
                }
                webChromeClient$CustomViewCallback.onCustomViewHidden();
            }
        });
        this.webView.setWebViewClient((WebViewClient)new WebViewClient() {
            public void onLoadResource(final WebView webView, final String s) {
                super.onLoadResource(webView, s);
            }
            
            public void onPageFinished(final WebView webView, final String s) {
                super.onPageFinished(webView, s);
                if (!EmbedBottomSheet.this.isYouTube || Build$VERSION.SDK_INT < 17) {
                    EmbedBottomSheet.this.progressBar.setVisibility(4);
                    EmbedBottomSheet.this.progressBarBlackBackground.setVisibility(4);
                    EmbedBottomSheet.this.pipButton.setEnabled(true);
                    EmbedBottomSheet.this.pipButton.setAlpha(1.0f);
                }
            }
        });
        final FrameLayout containerLayout = this.containerLayout;
        final WebView webView = this.webView;
        final boolean hasDescription = this.hasDescription;
        height = 22;
        if (hasDescription) {
            width = 22;
        }
        else {
            width = 0;
        }
        containerLayout.addView((View)webView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, (float)(width + 84)));
        (this.youtubeLogoImage = new ImageView(context)).setVisibility(8);
        this.containerLayout.addView((View)this.youtubeLogoImage, (ViewGroup$LayoutParams)LayoutHelper.createFrame(66, 28.0f, 53, 0.0f, 8.0f, 8.0f, 0.0f));
        this.youtubeLogoImage.setOnClickListener((View$OnClickListener)new _$$Lambda$EmbedBottomSheet$7BVC7l6jtG_KhdXyPoXIswQ3uh8(this));
        (this.videoView = new WebPlayerView(context, true, false, (WebPlayerView.WebPlayerViewDelegate)new WebPlayerView.WebPlayerViewDelegate() {
            @Override
            public boolean checkInlinePermissions() {
                return EmbedBottomSheet.this.checkInlinePermissions();
            }
            
            @Override
            public ViewGroup getTextureViewContainer() {
                return (ViewGroup)EmbedBottomSheet.this.container;
            }
            
            @Override
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
                EmbedBottomSheet.this.videoView.loadVideo(null, null, null, null, false);
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("Referer", "http://youtube.com");
                try {
                    EmbedBottomSheet.this.webView.loadUrl(EmbedBottomSheet.this.embedUrl, (Map)hashMap);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            
            @Override
            public void onInlineSurfaceTextureReady() {
                if (EmbedBottomSheet.this.videoView.isInline()) {
                    EmbedBottomSheet.this.dismissInternal();
                }
            }
            
            @Override
            public void onPlayStateChanged(final WebPlayerView webPlayerView, final boolean b) {
                if (b) {
                    try {
                        EmbedBottomSheet.this.parentActivity.getWindow().addFlags(128);
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                }
                else {
                    try {
                        EmbedBottomSheet.this.parentActivity.getWindow().clearFlags(128);
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
            }
            
            @Override
            public void onSharePressed() {
            }
            
            @Override
            public TextureView onSwitchInlineMode(final View view, final boolean b, final float n, final int n2, final boolean b2) {
                if (b) {
                    view.setTranslationY(0.0f);
                    EmbedBottomSheet.this.pipVideoView = new PipVideoView();
                    return EmbedBottomSheet.this.pipVideoView.show(EmbedBottomSheet.this.parentActivity, EmbedBottomSheet.this, view, n, n2, null);
                }
                if (b2) {
                    EmbedBottomSheet.this.animationInProgress = true;
                    EmbedBottomSheet.this.videoView.getAspectRatioView().getLocationInWindow(EmbedBottomSheet.this.position);
                    final int[] access$3700 = EmbedBottomSheet.this.position;
                    access$3700[0] -= BottomSheet.this.getLeftInset();
                    final int[] access$3701 = EmbedBottomSheet.this.position;
                    access$3701[1] -= (int)EmbedBottomSheet.this.containerView.getTranslationY();
                    final TextureView textureView = EmbedBottomSheet.this.videoView.getTextureView();
                    final ImageView textureImageView = EmbedBottomSheet.this.videoView.getTextureImageView();
                    final AnimatorSet set = new AnimatorSet();
                    set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "translationX", new float[] { (float)EmbedBottomSheet.this.position[0] }), (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "translationY", new float[] { (float)EmbedBottomSheet.this.position[1] }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "translationX", new float[] { (float)EmbedBottomSheet.this.position[0] }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "translationY", new float[] { (float)EmbedBottomSheet.this.position[1] }), (Animator)ObjectAnimator.ofFloat((Object)EmbedBottomSheet.this.containerView, "translationY", new float[] { 0.0f }), (Animator)ObjectAnimator.ofInt((Object)EmbedBottomSheet.this.backDrawable, "alpha", new int[] { 51 }) });
                    set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                    set.setDuration(250L);
                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            EmbedBottomSheet.this.animationInProgress = false;
                        }
                    });
                    set.start();
                }
                else {
                    EmbedBottomSheet.this.containerView.setTranslationY(0.0f);
                }
                return null;
            }
            
            @Override
            public TextureView onSwitchToFullscreen(final View view, final boolean b, final float n, final int n2, final boolean b2) {
                if (b) {
                    EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(0);
                    EmbedBottomSheet.this.fullscreenVideoContainer.setAlpha(1.0f);
                    EmbedBottomSheet.this.fullscreenVideoContainer.addView(EmbedBottomSheet.this.videoView.getAspectRatioView());
                    EmbedBottomSheet.this.wasInLandscape = false;
                    EmbedBottomSheet.this.fullscreenedByButton = b2;
                    if (EmbedBottomSheet.this.parentActivity != null) {
                        try {
                            EmbedBottomSheet.this.prevOrientation = EmbedBottomSheet.this.parentActivity.getRequestedOrientation();
                            if (b2) {
                                if (((WindowManager)EmbedBottomSheet.this.parentActivity.getSystemService("window")).getDefaultDisplay().getRotation() == 3) {
                                    EmbedBottomSheet.this.parentActivity.setRequestedOrientation(8);
                                }
                                else {
                                    EmbedBottomSheet.this.parentActivity.setRequestedOrientation(0);
                                }
                            }
                            EmbedBottomSheet.this.containerView.setSystemUiVisibility(1028);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                }
                else {
                    EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
                    EmbedBottomSheet.this.fullscreenedByButton = false;
                    if (EmbedBottomSheet.this.parentActivity != null) {
                        try {
                            EmbedBottomSheet.this.containerView.setSystemUiVisibility(0);
                            EmbedBottomSheet.this.parentActivity.setRequestedOrientation(EmbedBottomSheet.this.prevOrientation);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                    }
                }
                return null;
            }
            
            @Override
            public void onVideoSizeChanged(final float n, final int n2) {
            }
            
            @Override
            public void prepareToSwitchInlineMode(final boolean b, final Runnable runnable, float n, final boolean b2) {
                if (b) {
                    if (EmbedBottomSheet.this.parentActivity != null) {
                        try {
                            EmbedBottomSheet.this.containerView.setSystemUiVisibility(0);
                            if (EmbedBottomSheet.this.prevOrientation != -2) {
                                EmbedBottomSheet.this.parentActivity.setRequestedOrientation(EmbedBottomSheet.this.prevOrientation);
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    if (EmbedBottomSheet.this.fullscreenVideoContainer.getVisibility() == 0) {
                        EmbedBottomSheet.this.containerView.setTranslationY((float)(EmbedBottomSheet.this.containerView.getMeasuredHeight() + AndroidUtilities.dp(10.0f)));
                        EmbedBottomSheet.this.backDrawable.setAlpha(0);
                    }
                    EmbedBottomSheet.this.setOnShowListener((DialogInterface$OnShowListener)null);
                    if (b2) {
                        final TextureView textureView = EmbedBottomSheet.this.videoView.getTextureView();
                        final View controlsView = EmbedBottomSheet.this.videoView.getControlsView();
                        final ImageView textureImageView = EmbedBottomSheet.this.videoView.getTextureImageView();
                        final Rect pipRect = PipVideoView.getPipRect(n);
                        n = pipRect.width / textureView.getWidth();
                        if (Build$VERSION.SDK_INT >= 21) {
                            pipRect.y += AndroidUtilities.statusBarHeight;
                        }
                        final AnimatorSet set = new AnimatorSet();
                        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "scaleX", new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "scaleY", new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "translationX", new float[] { pipRect.x }), (Animator)ObjectAnimator.ofFloat((Object)textureImageView, "translationY", new float[] { pipRect.y }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "scaleX", new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "scaleY", new float[] { n }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "translationX", new float[] { pipRect.x }), (Animator)ObjectAnimator.ofFloat((Object)textureView, "translationY", new float[] { pipRect.y }), (Animator)ObjectAnimator.ofFloat((Object)EmbedBottomSheet.this.containerView, "translationY", new float[] { (float)(EmbedBottomSheet.this.containerView.getMeasuredHeight() + AndroidUtilities.dp(10.0f)) }), (Animator)ObjectAnimator.ofInt((Object)EmbedBottomSheet.this.backDrawable, "alpha", new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)EmbedBottomSheet.this.fullscreenVideoContainer, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)controlsView, "alpha", new float[] { 0.0f }) });
                        set.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                        set.setDuration(250L);
                        set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                if (EmbedBottomSheet.this.fullscreenVideoContainer.getVisibility() == 0) {
                                    EmbedBottomSheet.this.fullscreenVideoContainer.setAlpha(1.0f);
                                    EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
                                }
                                runnable.run();
                            }
                        });
                        set.start();
                    }
                    else {
                        if (EmbedBottomSheet.this.fullscreenVideoContainer.getVisibility() == 0) {
                            EmbedBottomSheet.this.fullscreenVideoContainer.setAlpha(1.0f);
                            EmbedBottomSheet.this.fullscreenVideoContainer.setVisibility(4);
                        }
                        runnable.run();
                        EmbedBottomSheet.this.dismissInternal();
                    }
                }
                else {
                    if (ApplicationLoader.mainInterfacePaused) {
                        try {
                            EmbedBottomSheet.this.parentActivity.startService(new Intent(ApplicationLoader.applicationContext, (Class)BringAppForegroundService.class));
                        }
                        catch (Throwable t) {
                            FileLog.e(t);
                        }
                    }
                    if (b2) {
                        final EmbedBottomSheet this$0 = EmbedBottomSheet.this;
                        this$0.setOnShowListener(this$0.onShowListener);
                        final Rect pipRect2 = PipVideoView.getPipRect(n);
                        final TextureView textureView2 = EmbedBottomSheet.this.videoView.getTextureView();
                        final ImageView textureImageView2 = EmbedBottomSheet.this.videoView.getTextureImageView();
                        n = pipRect2.width / textureView2.getLayoutParams().width;
                        if (Build$VERSION.SDK_INT >= 21) {
                            pipRect2.y += AndroidUtilities.statusBarHeight;
                        }
                        textureImageView2.setScaleX(n);
                        textureImageView2.setScaleY(n);
                        textureImageView2.setTranslationX(pipRect2.x);
                        textureImageView2.setTranslationY(pipRect2.y);
                        textureView2.setScaleX(n);
                        textureView2.setScaleY(n);
                        textureView2.setTranslationX(pipRect2.x);
                        textureView2.setTranslationY(pipRect2.y);
                    }
                    else {
                        EmbedBottomSheet.this.pipVideoView.close();
                        EmbedBottomSheet.this.pipVideoView = null;
                    }
                    EmbedBottomSheet.this.setShowWithoutAnimation(true);
                    EmbedBottomSheet.this.show();
                    if (b2) {
                        EmbedBottomSheet.this.waitingForDraw = 4;
                        EmbedBottomSheet.this.backDrawable.setAlpha(1);
                        EmbedBottomSheet.this.containerView.setTranslationY((float)(EmbedBottomSheet.this.containerView.getMeasuredHeight() + AndroidUtilities.dp(10.0f)));
                    }
                }
            }
        })).setVisibility(4);
        final FrameLayout containerLayout2 = this.containerLayout;
        final WebPlayerView videoView = this.videoView;
        if (this.hasDescription) {
            width = 22;
        }
        else {
            width = 0;
        }
        containerLayout2.addView((View)videoView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, (float)(width + 84 - 10)));
        (this.progressBarBlackBackground = new View(context)).setBackgroundColor(-16777216);
        this.progressBarBlackBackground.setVisibility(4);
        final FrameLayout containerLayout3 = this.containerLayout;
        final View progressBarBlackBackground = this.progressBarBlackBackground;
        if (this.hasDescription) {
            width = 22;
        }
        else {
            width = 0;
        }
        containerLayout3.addView(progressBarBlackBackground, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, (float)(width + 84)));
        (this.progressBar = new RadialProgressView(context)).setVisibility(4);
        final FrameLayout containerLayout4 = this.containerLayout;
        final RadialProgressView progressBar = this.progressBar;
        if (this.hasDescription) {
            width = height;
        }
        else {
            width = 0;
        }
        containerLayout4.addView((View)progressBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 0.0f, 0.0f, (float)((width + 84) / 2)));
        if (this.hasDescription) {
            final TextView textView = new TextView(context);
            textView.setTextSize(1, 16.0f);
            textView.setTextColor(Theme.getColor("dialogTextBlack"));
            textView.setText((CharSequence)text2);
            textView.setSingleLine(true);
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView.setEllipsize(TextUtils$TruncateAt.END);
            textView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
            this.containerLayout.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 77.0f));
        }
        final TextView textView2 = new TextView(context);
        textView2.setTextSize(1, 14.0f);
        textView2.setTextColor(Theme.getColor("dialogTextGray"));
        textView2.setText((CharSequence)text);
        textView2.setSingleLine(true);
        textView2.setEllipsize(TextUtils$TruncateAt.END);
        textView2.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        this.containerLayout.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 0.0f, 57.0f));
        final View view = new View(context);
        view.setBackgroundColor(Theme.getColor("dialogGrayLine"));
        this.containerLayout.addView(view, (ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-1, 1, 83));
        ((FrameLayout$LayoutParams)view.getLayoutParams()).bottomMargin = AndroidUtilities.dp(48.0f);
        final FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor("dialogBackground"));
        this.containerLayout.addView((View)frameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        linearLayout.setWeightSum(1.0f);
        frameLayout.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 53));
        final TextView textView3 = new TextView(context);
        textView3.setTextSize(1, 14.0f);
        textView3.setTextColor(Theme.getColor("dialogTextBlue4"));
        textView3.setGravity(17);
        textView3.setSingleLine(true);
        textView3.setEllipsize(TextUtils$TruncateAt.END);
        textView3.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
        textView3.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        textView3.setText((CharSequence)LocaleController.getString("Close", 2131559117).toUpperCase());
        textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        frameLayout.addView((View)textView3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -1, 51));
        textView3.setOnClickListener((View$OnClickListener)new _$$Lambda$EmbedBottomSheet$y37DL_JjK8uMG3joIQ1jW7ubjuE(this));
        (this.imageButtonsContainer = new LinearLayout(context)).setVisibility(4);
        frameLayout.addView((View)this.imageButtonsContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 17));
        (this.pipButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.pipButton.setImageResource(2131165908);
        this.pipButton.setEnabled(false);
        this.pipButton.setAlpha(0.5f);
        this.pipButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogTextBlue4"), PorterDuff$Mode.MULTIPLY));
        this.pipButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
        this.imageButtonsContainer.addView((View)this.pipButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, 51, 0.0f, 0.0f, 4.0f, 0.0f));
        this.pipButton.setOnClickListener((View$OnClickListener)new _$$Lambda$EmbedBottomSheet$ME3BZek9Olzkn_G2jOkI4EyTOfQ(this));
        final _$$Lambda$EmbedBottomSheet$xNtglytXCFyYO3cCP1Moj3HVHKg $$Lambda$EmbedBottomSheet$xNtglytXCFyYO3cCP1Moj3HVHKg = new _$$Lambda$EmbedBottomSheet$xNtglytXCFyYO3cCP1Moj3HVHKg(this);
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setImageResource(2131165901);
        imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogTextBlue4"), PorterDuff$Mode.MULTIPLY));
        imageView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
        this.imageButtonsContainer.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 51));
        imageView.setOnClickListener((View$OnClickListener)$$Lambda$EmbedBottomSheet$xNtglytXCFyYO3cCP1Moj3HVHKg);
        (this.copyTextButton = new TextView(context)).setTextSize(1, 14.0f);
        this.copyTextButton.setTextColor(Theme.getColor("dialogTextBlue4"));
        this.copyTextButton.setGravity(17);
        this.copyTextButton.setSingleLine(true);
        this.copyTextButton.setEllipsize(TextUtils$TruncateAt.END);
        this.copyTextButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
        this.copyTextButton.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        this.copyTextButton.setText((CharSequence)LocaleController.getString("Copy", 2131559163).toUpperCase());
        this.copyTextButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        linearLayout.addView((View)this.copyTextButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
        this.copyTextButton.setOnClickListener((View$OnClickListener)$$Lambda$EmbedBottomSheet$xNtglytXCFyYO3cCP1Moj3HVHKg);
        (this.openInButton = new TextView(context)).setTextSize(1, 14.0f);
        this.openInButton.setTextColor(Theme.getColor("dialogTextBlue4"));
        this.openInButton.setGravity(17);
        this.openInButton.setSingleLine(true);
        this.openInButton.setEllipsize(TextUtils$TruncateAt.END);
        this.openInButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 0));
        this.openInButton.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
        this.openInButton.setText((CharSequence)LocaleController.getString("OpenInBrowser", 2131560115).toUpperCase());
        this.openInButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        linearLayout.addView((View)this.openInButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 51));
        this.openInButton.setOnClickListener((View$OnClickListener)new _$$Lambda$EmbedBottomSheet$TOguFKwBNAuHY9JGAFn6CeA5SYI(this));
        this.setDelegate((BottomSheetDelegateInterface)new BottomSheetDelegate() {
            @Override
            public boolean canDismiss() {
                if (EmbedBottomSheet.this.videoView.isInFullscreen()) {
                    EmbedBottomSheet.this.videoView.exitFullscreen();
                    return false;
                }
                try {
                    EmbedBottomSheet.this.parentActivity.getWindow().clearFlags(128);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                return true;
            }
            
            @Override
            public void onOpenAnimationEnd() {
                if (EmbedBottomSheet.this.videoView.loadVideo(EmbedBottomSheet.this.embedUrl, null, null, EmbedBottomSheet.this.openUrl, true)) {
                    EmbedBottomSheet.this.progressBar.setVisibility(4);
                    EmbedBottomSheet.this.webView.setVisibility(4);
                    EmbedBottomSheet.this.videoView.setVisibility(0);
                }
                else {
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
                    EmbedBottomSheet.this.videoView.loadVideo(null, null, null, null, false);
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("Referer", "http://youtube.com");
                    try {
                        final String youtubeId = EmbedBottomSheet.this.videoView.getYoutubeId();
                        if (youtubeId != null) {
                            EmbedBottomSheet.this.progressBarBlackBackground.setVisibility(0);
                            EmbedBottomSheet.this.youtubeLogoImage.setVisibility(0);
                            EmbedBottomSheet.this.youtubeLogoImage.setImageResource(2131165914);
                            EmbedBottomSheet.this.isYouTube = true;
                            if (Build$VERSION.SDK_INT >= 17) {
                                EmbedBottomSheet.this.webView.addJavascriptInterface((Object)new YoutubeProxy(), "YoutubeProxy");
                            }
                            int intValue = 0;
                            Label_0427: {
                                if (EmbedBottomSheet.this.openUrl != null) {
                                    try {
                                        final Uri parse = Uri.parse(EmbedBottomSheet.this.openUrl);
                                        String s;
                                        if ((s = parse.getQueryParameter("t")) == null) {
                                            s = parse.getQueryParameter("time_continue");
                                        }
                                        if (s != null) {
                                            if (s.contains("m")) {
                                                final String[] split = s.split("m");
                                                intValue = Utilities.parseInt(split[0]) * 60 + Utilities.parseInt(split[1]);
                                                break Label_0427;
                                            }
                                            intValue = Utilities.parseInt(s);
                                            break Label_0427;
                                        }
                                    }
                                    catch (Exception ex) {
                                        FileLog.e(ex);
                                    }
                                }
                                intValue = 0;
                            }
                            EmbedBottomSheet.this.webView.loadDataWithBaseURL("https://www.youtube.com", String.format(Locale.US, "<!DOCTYPE html><html><head><style>body { margin: 0; width:100%%; height:100%%;  background-color:#000; }html { width:100%%; height:100%%; background-color:#000; }.embed-container iframe,.embed-container object,   .embed-container embed {       position: absolute;       top: 0;       left: 0;       width: 100%% !important;       height: 100%% !important;   }   </style></head><body>   <div class=\"embed-container\">       <div id=\"player\"></div>   </div>   <script src=\"https://www.youtube.com/iframe_api\"></script>   <script>   var player;   var observer;   var videoEl;   var playing;   var posted = false;   YT.ready(function() {       player = new YT.Player(\"player\", {                              \"width\" : \"100%%\",                              \"events\" : {                              \"onReady\" : \"onReady\",                              \"onError\" : \"onError\",                              },                              \"videoId\" : \"%1$s\",                              \"height\" : \"100%%\",                              \"playerVars\" : {                              \"start\" : %2$d,                              \"rel\" : 0,                              \"showinfo\" : 0,                              \"modestbranding\" : 1,                              \"iv_load_policy\" : 3,                              \"autohide\" : 1,                              \"autoplay\" : 1,                              \"cc_load_policy\" : 1,                              \"playsinline\" : 1,                              \"controls\" : 1                              }                            });        player.setSize(window.innerWidth, window.innerHeight);    });    function hideControls() {        playing = !videoEl.paused;       videoEl.controls = 0;       observer.observe(videoEl, {attributes: true});    }    function showControls() {        playing = !videoEl.paused;       observer.disconnect();       videoEl.controls = 1;    }    function onError(event) {       if (!posted) {            if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);             }            posted = true;       }    }    function onReady(event) {       player.playVideo();       videoEl = player.getIframe().contentDocument.getElementsByTagName('video')[0];\n       videoEl.addEventListener(\"canplay\", function() {            if (playing) {               videoEl.play();            }       }, true);       videoEl.addEventListener(\"timeupdate\", function() {            if (!posted && videoEl.currentTime > 0) {               if (window.YoutubeProxy !== undefined) {                   YoutubeProxy.postEvent(\"loaded\", null);                }               posted = true;           }       }, true);       observer = new MutationObserver(function() {\n          if (videoEl.controls) {\n               videoEl.controls = 0;\n          }       });\n    }    window.onresize = function() {        player.setSize(window.innerWidth, window.innerHeight);    }    </script></body></html>", youtubeId, intValue), "text/html", "UTF-8", "http://youtube.com");
                        }
                        else {
                            EmbedBottomSheet.this.webView.loadUrl(EmbedBottomSheet.this.embedUrl, (Map)hashMap);
                        }
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                }
            }
        });
        this.orientationEventListener = new OrientationEventListener(ApplicationLoader.applicationContext) {
            public void onOrientationChanged(final int n) {
                if (EmbedBottomSheet.this.orientationEventListener != null) {
                    if (EmbedBottomSheet.this.videoView.getVisibility() == 0) {
                        if (EmbedBottomSheet.this.parentActivity != null && EmbedBottomSheet.this.videoView.isInFullscreen() && EmbedBottomSheet.this.fullscreenedByButton) {
                            if (n >= 240 && n <= 300) {
                                EmbedBottomSheet.this.wasInLandscape = true;
                            }
                            else if (EmbedBottomSheet.this.wasInLandscape && (n >= 330 || n <= 30)) {
                                EmbedBottomSheet.this.parentActivity.setRequestedOrientation(EmbedBottomSheet.this.prevOrientation);
                                EmbedBottomSheet.this.fullscreenedByButton = false;
                                EmbedBottomSheet.this.wasInLandscape = false;
                            }
                        }
                    }
                }
            }
        };
        if (this.orientationEventListener.canDetectOrientation()) {
            this.orientationEventListener.enable();
        }
        else {
            this.orientationEventListener.disable();
            this.orientationEventListener = null;
        }
        EmbedBottomSheet.instance = this;
    }
    
    public static EmbedBottomSheet getInstance() {
        return EmbedBottomSheet.instance;
    }
    
    private void runJsCode(final String str) {
        if (Build$VERSION.SDK_INT >= 21) {
            this.webView.evaluateJavascript(str, (ValueCallback)null);
        }
        else {
            try {
                final WebView webView = this.webView;
                final StringBuilder sb = new StringBuilder();
                sb.append("javascript:");
                sb.append(str);
                webView.loadUrl(sb.toString());
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
    }
    
    public static void show(final Context context, final String s, final String s2, final String s3, final String s4, final int n, final int n2) {
        final EmbedBottomSheet instance = EmbedBottomSheet.instance;
        if (instance != null) {
            instance.destroy();
        }
        new EmbedBottomSheet(context, s, s2, s3, s4, n, n2).show();
    }
    
    private void showOrHideYoutubeLogo(final boolean b) {
        final ViewPropertyAnimator animate = this.youtubeLogoImage.animate();
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        final ViewPropertyAnimator setDuration = animate.alpha(n).setDuration(200L);
        long startDelay;
        if (b) {
            startDelay = 0L;
        }
        else {
            startDelay = 2900L;
        }
        setDuration.setStartDelay(startDelay).setInterpolator((TimeInterpolator)new DecelerateInterpolator()).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (b) {
                    EmbedBottomSheet.this.showOrHideYoutubeLogo(false);
                }
            }
        }).start();
    }
    
    @Override
    protected boolean canDismissWithSwipe() {
        return this.videoView.getVisibility() != 0 || !this.videoView.isInFullscreen();
    }
    
    @Override
    protected boolean canDismissWithTouchOutside() {
        return this.fullscreenVideoContainer.getVisibility() != 0;
    }
    
    public boolean checkInlinePermissions() {
        final Activity parentActivity = this.parentActivity;
        if (parentActivity == null) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays((Context)parentActivity)) {
            new AlertDialog.Builder((Context)this.parentActivity).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("PermissionDrawAboveOtherApps", 2131560413)).setPositiveButton(LocaleController.getString("PermissionOpenSettings", 2131560419), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                @TargetApi(23)
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    if (EmbedBottomSheet.this.parentActivity != null) {
                        final Activity access$2000 = EmbedBottomSheet.this.parentActivity;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("package:");
                        sb.append(EmbedBottomSheet.this.parentActivity.getPackageName());
                        access$2000.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString())));
                    }
                }
            }).show();
            return false;
        }
        return true;
    }
    
    public void destroy() {
        final WebView webView = this.webView;
        if (webView != null && webView.getVisibility() == 0) {
            this.containerLayout.removeView((View)this.webView);
            this.webView.stopLoading();
            this.webView.loadUrl("about:blank");
            this.webView.destroy();
        }
        final PipVideoView pipVideoView = this.pipVideoView;
        if (pipVideoView != null) {
            pipVideoView.close();
            this.pipVideoView = null;
        }
        final WebPlayerView videoView = this.videoView;
        if (videoView != null) {
            videoView.destroy();
        }
        EmbedBottomSheet.instance = null;
        this.dismissInternal();
    }
    
    public void exitFromPip() {
        if (this.webView != null) {
            if (this.pipVideoView != null) {
                if (ApplicationLoader.mainInterfacePaused) {
                    try {
                        this.parentActivity.startService(new Intent(ApplicationLoader.applicationContext, (Class)BringAppForegroundService.class));
                    }
                    catch (Throwable t) {
                        FileLog.e(t);
                    }
                }
                if (this.isYouTube) {
                    this.runJsCode("showControls();");
                }
                final ViewGroup viewGroup = (ViewGroup)this.webView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView((View)this.webView);
                }
                final FrameLayout containerLayout = this.containerLayout;
                final WebView webView = this.webView;
                int n;
                if (this.hasDescription) {
                    n = 22;
                }
                else {
                    n = 0;
                }
                containerLayout.addView((View)webView, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, (float)(n + 84)));
                this.setShowWithoutAnimation(true);
                this.show();
                this.pipVideoView.close();
                this.pipVideoView = null;
            }
        }
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        if (this.videoView.getVisibility() == 0 && this.videoView.isInitied() && !this.videoView.isInline()) {
            if (configuration.orientation == 2) {
                if (!this.videoView.isInFullscreen()) {
                    this.videoView.enterFullscreen();
                }
            }
            else if (this.videoView.isInFullscreen()) {
                this.videoView.exitFullscreen();
            }
        }
        final PipVideoView pipVideoView = this.pipVideoView;
        if (pipVideoView != null) {
            pipVideoView.onConfigurationChanged();
        }
    }
    
    @Override
    public void onContainerDraw(final Canvas canvas) {
        final int waitingForDraw = this.waitingForDraw;
        if (waitingForDraw != 0) {
            this.waitingForDraw = waitingForDraw - 1;
            if (this.waitingForDraw == 0) {
                this.videoView.updateTextureImageView();
                this.pipVideoView.close();
                this.pipVideoView = null;
            }
            else {
                super.container.invalidate();
            }
        }
    }
    
    @Override
    protected void onContainerTranslationYChanged(final float n) {
        this.updateTextureViewPosition();
    }
    
    @Override
    protected boolean onCustomLayout(final View view, final int n, final int n2, final int n3, final int n4) {
        if (view == this.videoView.getControlsView()) {
            this.updateTextureViewPosition();
        }
        return false;
    }
    
    @Override
    protected boolean onCustomMeasure(final View view, int dp, int measuredHeight) {
        if (view == this.videoView.getControlsView()) {
            final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = this.videoView.getMeasuredWidth();
            measuredHeight = this.videoView.getAspectRatioView().getMeasuredHeight();
            if (this.videoView.isInFullscreen()) {
                dp = 0;
            }
            else {
                dp = AndroidUtilities.dp(10.0f);
            }
            layoutParams.height = measuredHeight + dp;
        }
        return false;
    }
    
    public void pause() {
        final WebPlayerView videoView = this.videoView;
        if (videoView != null && videoView.isInitied()) {
            this.videoView.pause();
        }
    }
    
    public void updateTextureViewPosition() {
        this.videoView.getAspectRatioView().getLocationInWindow(this.position);
        final int[] position = this.position;
        position[0] -= this.getLeftInset();
        if (!this.videoView.isInline() && !this.animationInProgress) {
            final TextureView textureView = this.videoView.getTextureView();
            textureView.setTranslationX((float)this.position[0]);
            textureView.setTranslationY((float)this.position[1]);
            final ImageView textureImageView = this.videoView.getTextureImageView();
            if (textureImageView != null) {
                ((View)textureImageView).setTranslationX((float)this.position[0]);
                ((View)textureImageView).setTranslationY((float)this.position[1]);
            }
        }
        final View controlsView = this.videoView.getControlsView();
        if (controlsView.getParent() == super.container) {
            controlsView.setTranslationY((float)this.position[1]);
        }
        else {
            controlsView.setTranslationY(0.0f);
        }
    }
    
    private class YoutubeProxy
    {
        @JavascriptInterface
        public void postEvent(final String s, final String s2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$EmbedBottomSheet$YoutubeProxy$E5aY7hyeNKMq4h2nlfDXomuSVno(this, s));
        }
    }
}
