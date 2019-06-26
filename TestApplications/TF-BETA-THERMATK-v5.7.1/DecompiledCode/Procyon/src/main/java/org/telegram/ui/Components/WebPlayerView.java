// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.BuildVars;
import android.webkit.ValueCallback;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.json.JSONTokener;
import java.net.URLDecoder;
import java.util.concurrent.CountDownLatch;
import java.net.URLEncoder;
import android.webkit.JavascriptInterface;
import android.text.TextUtils;
import java.util.ArrayList;
import android.util.Base64;
import android.text.Layout$Alignment;
import android.view.MotionEvent;
import android.text.TextPaint;
import org.telegram.messenger.ImageReceiver;
import android.text.StaticLayout;
import android.widget.FrameLayout;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Locale;
import android.graphics.Canvas;
import java.util.regex.Matcher;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import android.view.View$MeasureSpec;
import java.util.HashMap;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.net.Uri;
import org.telegram.messenger.ApplicationLoader;
import android.media.AudioManager;
import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Bitmaps;
import android.graphics.Bitmap$Config;
import android.graphics.drawable.Drawable;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.graphics.SurfaceTexture;
import org.telegram.messenger.AndroidUtilities;
import android.os.Build$VERSION;
import android.content.Context;
import android.webkit.WebView;
import android.view.TextureView$SurfaceTextureListener;
import android.animation.AnimatorSet;
import android.widget.ImageView;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.view.TextureView;
import android.graphics.Paint;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import java.util.regex.Pattern;
import android.media.AudioManager$OnAudioFocusChangeListener;
import android.view.ViewGroup;

public class WebPlayerView extends ViewGroup implements VideoPlayerDelegate, AudioManager$OnAudioFocusChangeListener
{
    private static final int AUDIO_FOCUSED = 2;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final Pattern aparatFileListPattern;
    private static final Pattern aparatIdRegex;
    private static final Pattern coubIdRegex;
    private static final String exprName = "[a-zA-Z_$][a-zA-Z_$0-9]*";
    private static final Pattern exprParensPattern;
    private static final Pattern jsPattern;
    private static int lastContainerId = 4001;
    private static final Pattern playerIdPattern;
    private static final Pattern sigPattern;
    private static final Pattern sigPattern2;
    private static final Pattern stmtReturnPattern;
    private static final Pattern stmtVarPattern;
    private static final Pattern stsPattern;
    private static final Pattern twitchClipFilePattern;
    private static final Pattern twitchClipIdRegex;
    private static final Pattern twitchStreamIdRegex;
    private static final Pattern vimeoIdRegex;
    private static final Pattern youtubeIdRegex;
    private boolean allowInlineAnimation;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private int audioFocus;
    private Paint backgroundPaint;
    private TextureView changedTextureView;
    private boolean changingTextureView;
    private ControlsView controlsView;
    private float currentAlpha;
    private Bitmap currentBitmap;
    private AsyncTask currentTask;
    private String currentYoutubeId;
    private WebPlayerViewDelegate delegate;
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
    private TextureView$SurfaceTextureListener surfaceTextureListener;
    private Runnable switchToInlineRunnable;
    private boolean switchingInlineMode;
    private ImageView textureImageView;
    private TextureView textureView;
    private ViewGroup textureViewContainer;
    private VideoPlayer videoPlayer;
    private int waitingForFirstTextureUpload;
    private WebView webView;
    
    static {
        youtubeIdRegex = Pattern.compile("(?:youtube(?:-nocookie)?\\.com/(?:[^/\\n\\s]+/\\S+/|(?:v|e(?:mbed)?)/|\\S*?[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})");
        vimeoIdRegex = Pattern.compile("https?://(?:(?:www|(player))\\.)?vimeo(pro)?\\.com/(?!(?:channels|album)/[^/?#]+/?(?:$|[?#])|[^/]+/review/|ondemand/)(?:.*?/)?(?:(?:play_redirect_hls|moogaloop\\.swf)\\?clip_id=)?(?:videos?/)?([0-9]+)(?:/[\\da-f]+)?/?(?:[?&].*)?(?:[#].*)?$");
        coubIdRegex = Pattern.compile("(?:coub:|https?://(?:coub\\.com/(?:view|embed|coubs)/|c-cdn\\.coub\\.com/fb-player\\.swf\\?.*\\bcoub(?:ID|id)=))([\\da-z]+)");
        aparatIdRegex = Pattern.compile("^https?://(?:www\\.)?aparat\\.com/(?:v/|video/video/embed/videohash/)([a-zA-Z0-9]+)");
        twitchClipIdRegex = Pattern.compile("https?://clips\\.twitch\\.tv/(?:[^/]+/)*([^/?#&]+)");
        twitchStreamIdRegex = Pattern.compile("https?://(?:(?:www\\.)?twitch\\.tv/|player\\.twitch\\.tv/\\?.*?\\bchannel=)([^/#?]+)");
        aparatFileListPattern = Pattern.compile("fileList\\s*=\\s*JSON\\.parse\\('([^']+)'\\)");
        twitchClipFilePattern = Pattern.compile("clipInfo\\s*=\\s*(\\{[^']+\\});");
        stsPattern = Pattern.compile("\"sts\"\\s*:\\s*(\\d+)");
        jsPattern = Pattern.compile("\"assets\":.+?\"js\":\\s*(\"[^\"]+\")");
        sigPattern = Pattern.compile("\\.sig\\|\\|([a-zA-Z0-9$]+)\\(");
        sigPattern2 = Pattern.compile("[\"']signature[\"']\\s*,\\s*([a-zA-Z0-9$]+)\\(");
        stmtVarPattern = Pattern.compile("var\\s");
        stmtReturnPattern = Pattern.compile("return(?:\\s+|$)");
        exprParensPattern = Pattern.compile("[()]");
        playerIdPattern = Pattern.compile(".*?-([a-zA-Z0-9_-]+)(?:/watch_as3|/html5player(?:-new)?|(?:/[a-z]{2}_[A-Z]{2})?/base)?\\.([a-z]+)$");
    }
    
    @SuppressLint({ "SetJavaScriptEnabled", "AddJavascriptInterface" })
    public WebPlayerView(final Context context, final boolean b, final boolean b2, final WebPlayerViewDelegate delegate) {
        super(context);
        final int lastContainerId = WebPlayerView.lastContainerId;
        WebPlayerView.lastContainerId = lastContainerId + 1;
        this.fragment_container_id = lastContainerId;
        this.allowInlineAnimation = (Build$VERSION.SDK_INT >= 21);
        this.backgroundPaint = new Paint();
        this.progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (WebPlayerView.this.videoPlayer != null) {
                    if (WebPlayerView.this.videoPlayer.isPlaying()) {
                        WebPlayerView.this.controlsView.setProgress((int)(WebPlayerView.this.videoPlayer.getCurrentPosition() / 1000L));
                        WebPlayerView.this.controlsView.setBufferedProgress((int)(WebPlayerView.this.videoPlayer.getBufferedPosition() / 1000L));
                        AndroidUtilities.runOnUIThread(WebPlayerView.this.progressRunnable, 1000L);
                    }
                }
            }
        };
        this.surfaceTextureListener = (TextureView$SurfaceTextureListener)new TextureView$SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            }
            
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                if (WebPlayerView.this.changingTextureView) {
                    if (WebPlayerView.this.switchingInlineMode) {
                        WebPlayerView.this.waitingForFirstTextureUpload = 2;
                    }
                    WebPlayerView.this.textureView.setSurfaceTexture(surfaceTexture);
                    WebPlayerView.this.textureView.setVisibility(0);
                    WebPlayerView.this.changingTextureView = false;
                    return false;
                }
                return true;
            }
            
            public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            }
            
            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
                if (WebPlayerView.this.waitingForFirstTextureUpload == 1) {
                    WebPlayerView.this.changedTextureView.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                        public boolean onPreDraw() {
                            WebPlayerView.this.changedTextureView.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver$OnPreDrawListener)this);
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
            @Override
            public void run() {
                WebPlayerView.this.switchingInlineMode = false;
                if (WebPlayerView.this.currentBitmap != null) {
                    WebPlayerView.this.currentBitmap.recycle();
                    WebPlayerView.this.currentBitmap = null;
                }
                WebPlayerView.this.changingTextureView = true;
                if (WebPlayerView.this.textureImageView != null) {
                    try {
                        WebPlayerView.this.currentBitmap = Bitmaps.createBitmap(WebPlayerView.this.textureView.getWidth(), WebPlayerView.this.textureView.getHeight(), Bitmap$Config.ARGB_8888);
                        WebPlayerView.this.textureView.getBitmap(WebPlayerView.this.currentBitmap);
                    }
                    catch (Throwable t) {
                        if (WebPlayerView.this.currentBitmap != null) {
                            WebPlayerView.this.currentBitmap.recycle();
                            WebPlayerView.this.currentBitmap = null;
                        }
                        FileLog.e(t);
                    }
                    if (WebPlayerView.this.currentBitmap != null) {
                        WebPlayerView.this.textureImageView.setVisibility(0);
                        WebPlayerView.this.textureImageView.setImageBitmap(WebPlayerView.this.currentBitmap);
                    }
                    else {
                        WebPlayerView.this.textureImageView.setImageDrawable((Drawable)null);
                    }
                }
                WebPlayerView.this.isInline = true;
                WebPlayerView.this.updatePlayButton();
                WebPlayerView.this.updateShareButton();
                WebPlayerView.this.updateFullscreenButton();
                WebPlayerView.this.updateInlineButton();
                final ViewGroup viewGroup = (ViewGroup)WebPlayerView.this.controlsView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView((View)WebPlayerView.this.controlsView);
                }
                final WebPlayerView this$0 = WebPlayerView.this;
                this$0.changedTextureView = this$0.delegate.onSwitchInlineMode((View)WebPlayerView.this.controlsView, WebPlayerView.this.isInline, WebPlayerView.this.aspectRatioFrameLayout.getAspectRatio(), WebPlayerView.this.aspectRatioFrameLayout.getVideoRotation(), WebPlayerView.this.allowInlineAnimation);
                WebPlayerView.this.changedTextureView.setVisibility(4);
                final ViewGroup viewGroup2 = (ViewGroup)WebPlayerView.this.textureView.getParent();
                if (viewGroup2 != null) {
                    viewGroup2.removeView((View)WebPlayerView.this.textureView);
                }
                WebPlayerView.this.controlsView.show(false, false);
            }
        };
        this.setWillNotDraw(false);
        this.delegate = delegate;
        this.backgroundPaint.setColor(-16777216);
        this.addView((View)(this.aspectRatioFrameLayout = new AspectRatioFrameLayout(context) {
            @Override
            protected void onMeasure(final int n, final int n2) {
                super.onMeasure(n, n2);
                if (WebPlayerView.this.textureViewContainer != null) {
                    final ViewGroup$LayoutParams layoutParams = WebPlayerView.this.textureView.getLayoutParams();
                    layoutParams.width = this.getMeasuredWidth();
                    layoutParams.height = this.getMeasuredHeight();
                    if (WebPlayerView.this.textureImageView != null) {
                        final ViewGroup$LayoutParams layoutParams2 = WebPlayerView.this.textureImageView.getLayoutParams();
                        layoutParams2.width = this.getMeasuredWidth();
                        layoutParams2.height = this.getMeasuredHeight();
                    }
                }
            }
        }), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        this.interfaceName = "JavaScriptInterface";
        (this.webView = new WebView(context)).addJavascriptInterface((Object)new JavaScriptInterface(new _$$Lambda$WebPlayerView$OTCqcKUzHnpxut9IWnkU_zTMUYs(this)), this.interfaceName);
        final WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        this.textureViewContainer = this.delegate.getTextureViewContainer();
        (this.textureView = new TextureView(context)).setPivotX(0.0f);
        this.textureView.setPivotY(0.0f);
        final ViewGroup textureViewContainer = this.textureViewContainer;
        if (textureViewContainer != null) {
            textureViewContainer.addView((View)this.textureView);
        }
        else {
            this.aspectRatioFrameLayout.addView((View)this.textureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        }
        if (this.allowInlineAnimation && this.textureViewContainer != null) {
            (this.textureImageView = new ImageView(context)).setBackgroundColor(-65536);
            this.textureImageView.setPivotX(0.0f);
            this.textureImageView.setPivotY(0.0f);
            this.textureImageView.setVisibility(4);
            this.textureViewContainer.addView((View)this.textureImageView);
        }
        (this.videoPlayer = new VideoPlayer()).setDelegate((VideoPlayer.VideoPlayerDelegate)this);
        this.videoPlayer.setTextureView(this.textureView);
        this.controlsView = new ControlsView(context);
        final ViewGroup textureViewContainer2 = this.textureViewContainer;
        if (textureViewContainer2 != null) {
            textureViewContainer2.addView((View)this.controlsView);
        }
        else {
            this.addView((View)this.controlsView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        }
        (this.progressView = new RadialProgressView(context)).setProgressColor(-1);
        this.addView((View)this.progressView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 17));
        (this.fullscreenButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.controlsView.addView((View)this.fullscreenButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 5.0f));
        this.fullscreenButton.setOnClickListener((View$OnClickListener)new _$$Lambda$WebPlayerView$W2P4sWOYF2snToxNUtlCSP61A2U(this));
        (this.playButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
        this.controlsView.addView((View)this.playButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 17));
        this.playButton.setOnClickListener((View$OnClickListener)new _$$Lambda$WebPlayerView$8RPE5WMQJ4Qql9XUXKDSHuRZ8yA(this));
        if (b) {
            (this.inlineButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.controlsView.addView((View)this.inlineButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 48, 53));
            this.inlineButton.setOnClickListener((View$OnClickListener)new _$$Lambda$WebPlayerView$FgDS8XBnRLuQdgwn4r4TJJnIjOo(this));
        }
        if (b2) {
            (this.shareButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.shareButton.setImageResource(2131165470);
            this.controlsView.addView((View)this.shareButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 48, 53));
            this.shareButton.setOnClickListener((View$OnClickListener)new _$$Lambda$WebPlayerView$tu1I4PMipEbSXLy2AXDs0pmTjdo(this));
        }
        this.updatePlayButton();
        this.updateFullscreenButton();
        this.updateInlineButton();
        this.updateShareButton();
    }
    
    private void checkAudioFocus() {
        if (!this.hasAudioFocus) {
            final AudioManager audioManager = (AudioManager)ApplicationLoader.applicationContext.getSystemService("audio");
            this.hasAudioFocus = true;
            if (audioManager.requestAudioFocus((AudioManager$OnAudioFocusChangeListener)this, 3, 1) == 1) {
                this.audioFocus = 2;
            }
        }
    }
    
    private View getControlView() {
        return (View)this.controlsView;
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
        final String playVideoUrl = this.playVideoUrl;
        if (playVideoUrl == null) {
            return;
        }
        if (playVideoUrl != null && this.playAudioUrl != null) {
            this.videoPlayer.preparePlayerLoop(Uri.parse(playVideoUrl), this.playVideoType, Uri.parse(this.playAudioUrl), this.playAudioType);
        }
        else {
            this.videoPlayer.preparePlayer(Uri.parse(this.playVideoUrl), this.playVideoType);
        }
        this.videoPlayer.setPlayWhenReady(this.isAutoplay);
        this.isLoading = false;
        if (this.videoPlayer.getDuration() != -9223372036854775807L) {
            this.controlsView.setDuration((int)(this.videoPlayer.getDuration() / 1000L));
        }
        else {
            this.controlsView.setDuration(0);
        }
        this.updateFullscreenButton();
        this.updateShareButton();
        this.updateInlineButton();
        this.controlsView.invalidate();
        final int seekToTime = this.seekToTime;
        if (seekToTime != -1) {
            this.videoPlayer.seekTo(seekToTime * 1000);
        }
    }
    
    private void showProgress(final boolean b, final boolean b2) {
        float alpha = 1.0f;
        if (b2) {
            final AnimatorSet progressAnimation = this.progressAnimation;
            if (progressAnimation != null) {
                progressAnimation.cancel();
            }
            this.progressAnimation = new AnimatorSet();
            final AnimatorSet progressAnimation2 = this.progressAnimation;
            final RadialProgressView progressView = this.progressView;
            if (!b) {
                alpha = 0.0f;
            }
            progressAnimation2.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)progressView, "alpha", new float[] { alpha }) });
            this.progressAnimation.setDuration(150L);
            this.progressAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    WebPlayerView.this.progressAnimation = null;
                }
            });
            this.progressAnimation.start();
        }
        else {
            final RadialProgressView progressView2 = this.progressView;
            if (!b) {
                alpha = 0.0f;
            }
            progressView2.setAlpha(alpha);
        }
    }
    
    private void updateFullscreenButton() {
        if (this.videoPlayer.isPlayerPrepared() && !this.isInline) {
            this.fullscreenButton.setVisibility(0);
            if (!this.inFullscreen) {
                this.fullscreenButton.setImageResource(2131165444);
                this.fullscreenButton.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 5.0f));
            }
            else {
                this.fullscreenButton.setImageResource(2131165457);
                this.fullscreenButton.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 56.0f, 85, 0.0f, 0.0f, 0.0f, 1.0f));
            }
            return;
        }
        this.fullscreenButton.setVisibility(8);
    }
    
    private void updateFullscreenState(final boolean b) {
        if (this.textureView == null) {
            return;
        }
        this.updateFullscreenButton();
        final ViewGroup textureViewContainer = this.textureViewContainer;
        if (textureViewContainer == null) {
            this.changingTextureView = true;
            if (!this.inFullscreen) {
                if (textureViewContainer != null) {
                    textureViewContainer.addView((View)this.textureView);
                }
                else {
                    this.aspectRatioFrameLayout.addView((View)this.textureView);
                }
            }
            if (this.inFullscreen) {
                final ViewGroup viewGroup = (ViewGroup)this.controlsView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView((View)this.controlsView);
                }
            }
            else {
                final ViewGroup viewGroup2 = (ViewGroup)this.controlsView.getParent();
                if (viewGroup2 != this) {
                    if (viewGroup2 != null) {
                        viewGroup2.removeView((View)this.controlsView);
                    }
                    final ViewGroup textureViewContainer2 = this.textureViewContainer;
                    if (textureViewContainer2 != null) {
                        textureViewContainer2.addView((View)this.controlsView);
                    }
                    else {
                        this.addView((View)this.controlsView, 1);
                    }
                }
            }
            (this.changedTextureView = this.delegate.onSwitchToFullscreen((View)this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), b)).setVisibility(4);
            if (this.inFullscreen && this.changedTextureView != null) {
                final ViewGroup viewGroup3 = (ViewGroup)this.textureView.getParent();
                if (viewGroup3 != null) {
                    viewGroup3.removeView((View)this.textureView);
                }
            }
            this.controlsView.checkNeedHide();
        }
        else {
            if (this.inFullscreen) {
                final ViewGroup viewGroup4 = (ViewGroup)this.aspectRatioFrameLayout.getParent();
                if (viewGroup4 != null) {
                    viewGroup4.removeView((View)this.aspectRatioFrameLayout);
                }
            }
            else {
                final ViewGroup viewGroup5 = (ViewGroup)this.aspectRatioFrameLayout.getParent();
                if (viewGroup5 != this) {
                    if (viewGroup5 != null) {
                        viewGroup5.removeView((View)this.aspectRatioFrameLayout);
                    }
                    this.addView((View)this.aspectRatioFrameLayout, 0);
                }
            }
            this.delegate.onSwitchToFullscreen((View)this.controlsView, this.inFullscreen, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), b);
        }
    }
    
    private void updateInlineButton() {
        final ImageView inlineButton = this.inlineButton;
        if (inlineButton == null) {
            return;
        }
        int imageResource;
        if (this.isInline) {
            imageResource = 2131165445;
        }
        else {
            imageResource = 2131165458;
        }
        inlineButton.setImageResource(imageResource);
        final ImageView inlineButton2 = this.inlineButton;
        int visibility;
        if (this.videoPlayer.isPlayerPrepared()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        inlineButton2.setVisibility(visibility);
        if (this.isInline) {
            this.inlineButton.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40, 53));
        }
        else {
            this.inlineButton.setLayoutParams((ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 50, 53));
        }
    }
    
    private void updatePlayButton() {
        this.controlsView.checkNeedHide();
        AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
        if (!this.videoPlayer.isPlaying()) {
            if (this.isCompleted) {
                final ImageView playButton = this.playButton;
                int imageResource;
                if (this.isInline) {
                    imageResource = 2131165426;
                }
                else {
                    imageResource = 2131165425;
                }
                playButton.setImageResource(imageResource);
            }
            else {
                final ImageView playButton2 = this.playButton;
                int imageResource2;
                if (this.isInline) {
                    imageResource2 = 2131165464;
                }
                else {
                    imageResource2 = 2131165462;
                }
                playButton2.setImageResource(imageResource2);
            }
        }
        else {
            final ImageView playButton3 = this.playButton;
            int imageResource3;
            if (this.isInline) {
                imageResource3 = 2131165460;
            }
            else {
                imageResource3 = 2131165459;
            }
            playButton3.setImageResource(imageResource3);
            AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
            this.checkAudioFocus();
        }
    }
    
    private void updateShareButton() {
        final ImageView shareButton = this.shareButton;
        if (shareButton == null) {
            return;
        }
        int visibility;
        if (!this.isInline && this.videoPlayer.isPlayerPrepared()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        shareButton.setVisibility(visibility);
    }
    
    public void destroy() {
        this.videoPlayer.releasePlayer(false);
        final AsyncTask currentTask = this.currentTask;
        if (currentTask != null) {
            currentTask.cancel(true);
            this.currentTask = null;
        }
        this.webView.stopLoading();
    }
    
    protected String downloadUrlContent(final AsyncTask asyncTask, final String s) {
        return this.downloadUrlContent(asyncTask, s, null, true);
    }
    
    protected String downloadUrlContent(final AsyncTask p0, final String p1, final HashMap<String, String> p2, final boolean p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore          5
        //     5: aload           5
        //     7: aload_2        
        //     8: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    11: aload           5
        //    13: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    16: astore_2       
        //    17: aload_2        
        //    18: ldc_w           "User-Agent"
        //    21: ldc_w           "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)"
        //    24: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    27: iload           4
        //    29: ifeq            42
        //    32: aload_2        
        //    33: ldc_w           "Accept-Encoding"
        //    36: ldc_w           "gzip, deflate"
        //    39: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    42: aload_2        
        //    43: ldc_w           "Accept-Language"
        //    46: ldc_w           "en-us,en;q=0.5"
        //    49: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    52: aload_2        
        //    53: ldc_w           "Accept"
        //    56: ldc_w           "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
        //    59: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    62: aload_2        
        //    63: ldc_w           "Accept-Charset"
        //    66: ldc_w           "ISO-8859-1,utf-8;q=0.7,*;q=0.7"
        //    69: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //    72: aload_3        
        //    73: ifnull          136
        //    76: aload_3        
        //    77: invokevirtual   java/util/HashMap.entrySet:()Ljava/util/Set;
        //    80: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    85: astore          6
        //    87: aload           6
        //    89: invokeinterface java/util/Iterator.hasNext:()Z
        //    94: ifeq            136
        //    97: aload           6
        //    99: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   104: checkcast       Ljava/util/Map$Entry;
        //   107: astore          7
        //   109: aload_2        
        //   110: aload           7
        //   112: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   117: checkcast       Ljava/lang/String;
        //   120: aload           7
        //   122: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   127: checkcast       Ljava/lang/String;
        //   130: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   133: goto            87
        //   136: aload_2        
        //   137: sipush          5000
        //   140: invokevirtual   java/net/URLConnection.setConnectTimeout:(I)V
        //   143: aload_2        
        //   144: sipush          5000
        //   147: invokevirtual   java/net/URLConnection.setReadTimeout:(I)V
        //   150: aload_2        
        //   151: instanceof      Ljava/net/HttpURLConnection;
        //   154: ifeq            385
        //   157: aload_2        
        //   158: checkcast       Ljava/net/HttpURLConnection;
        //   161: astore          6
        //   163: aload           6
        //   165: iconst_1       
        //   166: invokevirtual   java/net/HttpURLConnection.setInstanceFollowRedirects:(Z)V
        //   169: aload           6
        //   171: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   174: istore          8
        //   176: iload           8
        //   178: sipush          302
        //   181: if_icmpeq       200
        //   184: iload           8
        //   186: sipush          301
        //   189: if_icmpeq       200
        //   192: iload           8
        //   194: sipush          303
        //   197: if_icmpne       385
        //   200: aload           6
        //   202: ldc_w           "Location"
        //   205: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //   208: astore          5
        //   210: aload           6
        //   212: ldc_w           "Set-Cookie"
        //   215: invokevirtual   java/net/HttpURLConnection.getHeaderField:(Ljava/lang/String;)Ljava/lang/String;
        //   218: astore          7
        //   220: new             Ljava/net/URL;
        //   223: astore          6
        //   225: aload           6
        //   227: aload           5
        //   229: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //   232: aload           6
        //   234: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //   237: astore          5
        //   239: aload           5
        //   241: ldc_w           "Cookie"
        //   244: aload           7
        //   246: invokevirtual   java/net/URLConnection.setRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   249: aload           5
        //   251: ldc_w           "User-Agent"
        //   254: ldc_w           "Mozilla/5.0 (X11; Linux x86_64; rv:10.0) Gecko/20150101 Firefox/47.0 (Chrome)"
        //   257: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   260: iload           4
        //   262: ifeq            276
        //   265: aload           5
        //   267: ldc_w           "Accept-Encoding"
        //   270: ldc_w           "gzip, deflate"
        //   273: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   276: aload           5
        //   278: ldc_w           "Accept-Language"
        //   281: ldc_w           "en-us,en;q=0.5"
        //   284: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   287: aload           5
        //   289: ldc_w           "Accept"
        //   292: ldc_w           "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
        //   295: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   298: aload           5
        //   300: ldc_w           "Accept-Charset"
        //   303: ldc_w           "ISO-8859-1,utf-8;q=0.7,*;q=0.7"
        //   306: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   309: aload_3        
        //   310: ifnull          368
        //   313: aload_3        
        //   314: invokevirtual   java/util/HashMap.entrySet:()Ljava/util/Set;
        //   317: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   322: astore_2       
        //   323: aload_2        
        //   324: invokeinterface java/util/Iterator.hasNext:()Z
        //   329: ifeq            368
        //   332: aload_2        
        //   333: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   338: checkcast       Ljava/util/Map$Entry;
        //   341: astore_3       
        //   342: aload           5
        //   344: aload_3        
        //   345: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   350: checkcast       Ljava/lang/String;
        //   353: aload_3        
        //   354: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   359: checkcast       Ljava/lang/String;
        //   362: invokevirtual   java/net/URLConnection.addRequestProperty:(Ljava/lang/String;Ljava/lang/String;)V
        //   365: goto            323
        //   368: aload           5
        //   370: astore_2       
        //   371: aload           6
        //   373: astore          5
        //   375: goto            385
        //   378: astore_3       
        //   379: aload           5
        //   381: astore_2       
        //   382: goto            486
        //   385: aload_2        
        //   386: astore_3       
        //   387: aload_2        
        //   388: invokevirtual   java/net/URLConnection.connect:()V
        //   391: iload           4
        //   393: ifeq            449
        //   396: aload_2        
        //   397: astore_3       
        //   398: new             Ljava/util/zip/GZIPInputStream;
        //   401: astore          6
        //   403: aload_2        
        //   404: astore_3       
        //   405: aload           6
        //   407: aload_2        
        //   408: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //   411: invokespecial   java/util/zip/GZIPInputStream.<init>:(Ljava/io/InputStream;)V
        //   414: aload           6
        //   416: astore_3       
        //   417: goto            460
        //   420: astore_3       
        //   421: aload_2        
        //   422: astore_3       
        //   423: aload           5
        //   425: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //   428: astore_2       
        //   429: aload_2        
        //   430: astore_3       
        //   431: aload_2        
        //   432: invokevirtual   java/net/URLConnection.connect:()V
        //   435: aload_2        
        //   436: astore_3       
        //   437: aload_2        
        //   438: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //   441: astore          5
        //   443: aload           5
        //   445: astore_3       
        //   446: goto            460
        //   449: aload_2        
        //   450: astore_3       
        //   451: aload_2        
        //   452: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //   455: astore          5
        //   457: aload           5
        //   459: astore_3       
        //   460: iconst_1       
        //   461: istore          8
        //   463: aload_3        
        //   464: astore          7
        //   466: goto            565
        //   469: astore          5
        //   471: aload_3        
        //   472: astore_2       
        //   473: aload           5
        //   475: astore_3       
        //   476: goto            486
        //   479: astore_3       
        //   480: goto            486
        //   483: astore_3       
        //   484: aconst_null    
        //   485: astore_2       
        //   486: aload_3        
        //   487: instanceof      Ljava/net/SocketTimeoutException;
        //   490: ifeq            502
        //   493: invokestatic    org/telegram/messenger/ApplicationLoader.isNetworkOnline:()Z
        //   496: ifeq            555
        //   499: goto            509
        //   502: aload_3        
        //   503: instanceof      Ljava/net/UnknownHostException;
        //   506: ifeq            515
        //   509: iconst_0       
        //   510: istore          8
        //   512: goto            558
        //   515: aload_3        
        //   516: instanceof      Ljava/net/SocketException;
        //   519: ifeq            545
        //   522: aload_3        
        //   523: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   526: ifnull          555
        //   529: aload_3        
        //   530: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //   533: ldc_w           "ECONNRESET"
        //   536: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   539: ifeq            555
        //   542: goto            509
        //   545: aload_3        
        //   546: instanceof      Ljava/io/FileNotFoundException;
        //   549: ifeq            555
        //   552: goto            509
        //   555: iconst_1       
        //   556: istore          8
        //   558: aload_3        
        //   559: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   562: aconst_null    
        //   563: astore          7
        //   565: iload           8
        //   567: ifeq            814
        //   570: aload_2        
        //   571: instanceof      Ljava/net/HttpURLConnection;
        //   574: ifeq            602
        //   577: aload_2        
        //   578: checkcast       Ljava/net/HttpURLConnection;
        //   581: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //   584: istore          8
        //   586: iload           8
        //   588: sipush          200
        //   591: if_icmpeq       602
        //   594: goto            602
        //   597: astore_2       
        //   598: aload_2        
        //   599: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   602: aload           7
        //   604: ifnull          786
        //   607: ldc_w           32768
        //   610: newarray        B
        //   612: astore          9
        //   614: aconst_null    
        //   615: astore_3       
        //   616: aload_3        
        //   617: astore          5
        //   619: aload_1        
        //   620: invokevirtual   android/os/AsyncTask.isCancelled:()Z
        //   623: istore          4
        //   625: iload           4
        //   627: ifeq            638
        //   630: iconst_0       
        //   631: istore          8
        //   633: aload_3        
        //   634: astore_2       
        //   635: goto            760
        //   638: aload_3        
        //   639: astore          6
        //   641: aload_3        
        //   642: astore          5
        //   644: aload           7
        //   646: aload           9
        //   648: invokevirtual   java/io/InputStream.read:([B)I
        //   651: istore          10
        //   653: iload           10
        //   655: ifle            728
        //   658: aload_3        
        //   659: astore_2       
        //   660: aload_3        
        //   661: ifnonnull       684
        //   664: aload_3        
        //   665: astore          6
        //   667: aload_3        
        //   668: astore          5
        //   670: new             Ljava/lang/StringBuilder;
        //   673: astore_2       
        //   674: aload_3        
        //   675: astore          6
        //   677: aload_3        
        //   678: astore          5
        //   680: aload_2        
        //   681: invokespecial   java/lang/StringBuilder.<init>:()V
        //   684: aload_2        
        //   685: astore          6
        //   687: aload_2        
        //   688: astore          5
        //   690: new             Ljava/lang/String;
        //   693: astore          11
        //   695: aload_2        
        //   696: astore_3       
        //   697: aload           11
        //   699: aload           9
        //   701: iconst_0       
        //   702: iload           10
        //   704: ldc_w           "UTF-8"
        //   707: invokespecial   java/lang/String.<init>:([BIILjava/lang/String;)V
        //   710: aload_2        
        //   711: astore_3       
        //   712: aload_2        
        //   713: aload           11
        //   715: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   718: pop            
        //   719: aload_2        
        //   720: astore_3       
        //   721: goto            616
        //   724: astore_1       
        //   725: goto            751
        //   728: iconst_0       
        //   729: istore          8
        //   731: aload_3        
        //   732: astore_2       
        //   733: iload           10
        //   735: iconst_m1      
        //   736: if_icmpne       760
        //   739: iconst_1       
        //   740: istore          8
        //   742: aload_3        
        //   743: astore_2       
        //   744: goto            760
        //   747: astore_1       
        //   748: aload           6
        //   750: astore_2       
        //   751: iconst_0       
        //   752: istore          8
        //   754: aload_2        
        //   755: astore_3       
        //   756: aload_1        
        //   757: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   760: goto            793
        //   763: astore_2       
        //   764: aload_3        
        //   765: astore_1       
        //   766: goto            779
        //   769: astore_2       
        //   770: aload           5
        //   772: astore_1       
        //   773: goto            779
        //   776: astore_2       
        //   777: aconst_null    
        //   778: astore_1       
        //   779: aload_2        
        //   780: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   783: goto            788
        //   786: aconst_null    
        //   787: astore_1       
        //   788: iconst_0       
        //   789: istore          8
        //   791: aload_1        
        //   792: astore_2       
        //   793: aload           7
        //   795: ifnull          811
        //   798: aload           7
        //   800: invokevirtual   java/io/InputStream.close:()V
        //   803: goto            811
        //   806: astore_1       
        //   807: aload_1        
        //   808: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   811: goto            819
        //   814: iconst_0       
        //   815: istore          8
        //   817: aconst_null    
        //   818: astore_2       
        //   819: iload           8
        //   821: ifeq            832
        //   824: aload_2        
        //   825: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   828: astore_1       
        //   829: goto            834
        //   832: aconst_null    
        //   833: astore_1       
        //   834: aload_1        
        //   835: areturn        
        //    Signature:
        //  (Landroid/os/AsyncTask;Ljava/lang/String;Ljava/util/HashMap<Ljava/lang/String;Ljava/lang/String;>;Z)Ljava/lang/String;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      17     483    486    Ljava/lang/Throwable;
        //  17     27     479    483    Ljava/lang/Throwable;
        //  32     42     479    483    Ljava/lang/Throwable;
        //  42     72     479    483    Ljava/lang/Throwable;
        //  76     87     479    483    Ljava/lang/Throwable;
        //  87     133    479    483    Ljava/lang/Throwable;
        //  136    176    479    483    Ljava/lang/Throwable;
        //  200    239    479    483    Ljava/lang/Throwable;
        //  239    260    378    385    Ljava/lang/Throwable;
        //  265    276    378    385    Ljava/lang/Throwable;
        //  276    309    378    385    Ljava/lang/Throwable;
        //  313    323    378    385    Ljava/lang/Throwable;
        //  323    365    378    385    Ljava/lang/Throwable;
        //  387    391    469    479    Ljava/lang/Throwable;
        //  398    403    420    449    Ljava/lang/Exception;
        //  398    403    469    479    Ljava/lang/Throwable;
        //  405    414    420    449    Ljava/lang/Exception;
        //  405    414    469    479    Ljava/lang/Throwable;
        //  423    429    469    479    Ljava/lang/Throwable;
        //  431    435    469    479    Ljava/lang/Throwable;
        //  437    443    469    479    Ljava/lang/Throwable;
        //  451    457    469    479    Ljava/lang/Throwable;
        //  570    586    597    602    Ljava/lang/Exception;
        //  607    614    776    779    Ljava/lang/Throwable;
        //  619    625    769    776    Ljava/lang/Throwable;
        //  644    653    747    751    Ljava/lang/Exception;
        //  644    653    769    776    Ljava/lang/Throwable;
        //  670    674    747    751    Ljava/lang/Exception;
        //  670    674    769    776    Ljava/lang/Throwable;
        //  680    684    747    751    Ljava/lang/Exception;
        //  680    684    769    776    Ljava/lang/Throwable;
        //  690    695    747    751    Ljava/lang/Exception;
        //  690    695    769    776    Ljava/lang/Throwable;
        //  697    710    724    728    Ljava/lang/Exception;
        //  697    710    763    769    Ljava/lang/Throwable;
        //  712    719    724    728    Ljava/lang/Exception;
        //  712    719    763    769    Ljava/lang/Throwable;
        //  756    760    763    769    Ljava/lang/Throwable;
        //  798    803    806    811    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0728:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void enterFullscreen() {
        if (this.inFullscreen) {
            return;
        }
        this.inFullscreen = true;
        this.updateInlineButton();
        this.updateFullscreenState(false);
    }
    
    public void exitFullscreen() {
        if (!this.inFullscreen) {
            return;
        }
        this.inFullscreen = false;
        this.updateInlineButton();
        this.updateFullscreenState(false);
    }
    
    public View getAspectRatioView() {
        return (View)this.aspectRatioFrameLayout;
    }
    
    public View getControlsView() {
        return (View)this.controlsView;
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
        return this.isInline || this.switchingInlineMode;
    }
    
    public boolean loadVideo(final String s, final TLRPC.Photo photo, final Object o, String playVideoUrl, final boolean isAutoplay) {
        this.seekToTime = -1;
        String s2 = null;
        String s3 = null;
        String group = null;
        String group2 = null;
        String currentYoutubeId = null;
        String s6 = null;
        Label_0607: {
            if (s != null) {
                if (s.endsWith(".mp4")) {
                    playVideoUrl = s;
                }
                else {
                    while (true) {
                        if (playVideoUrl != null) {
                            try {
                                final Uri parse = Uri.parse(playVideoUrl);
                                if ((playVideoUrl = parse.getQueryParameter("t")) == null) {
                                    playVideoUrl = parse.getQueryParameter("time_continue");
                                }
                                if (playVideoUrl == null) {
                                    break Label_0145;
                                }
                                if (playVideoUrl.contains("m")) {
                                    final String[] split = playVideoUrl.split("m");
                                    this.seekToTime = Utilities.parseInt(split[0]) * 60 + Utilities.parseInt(split[1]);
                                    break Label_0145;
                                }
                                this.seekToTime = Utilities.parseInt(playVideoUrl);
                                break Label_0145;
                            }
                            catch (Exception ex2) {
                                final Exception ex = ex2;
                                FileLog.e(ex);
                            }
                            try {
                                final Exception ex2;
                                final Exception ex = ex2;
                                FileLog.e(ex);
                                final Matcher matcher = WebPlayerView.youtubeIdRegex.matcher(s);
                                if (matcher.find()) {
                                    playVideoUrl = matcher.group(1);
                                }
                                else {
                                    playVideoUrl = null;
                                }
                                if (playVideoUrl == null) {
                                    playVideoUrl = null;
                                }
                                s2 = playVideoUrl;
                            }
                            catch (Exception ex3) {
                                FileLog.e(ex3);
                                s2 = null;
                            }
                            Label_0268: {
                                if (s2 == null) {
                                    try {
                                        final Matcher matcher2 = WebPlayerView.vimeoIdRegex.matcher(s);
                                        if (matcher2.find()) {
                                            playVideoUrl = matcher2.group(3);
                                        }
                                        else {
                                            playVideoUrl = null;
                                        }
                                        if (playVideoUrl == null) {
                                            playVideoUrl = null;
                                        }
                                        s3 = playVideoUrl;
                                        break Label_0268;
                                    }
                                    catch (Exception ex4) {
                                        FileLog.e(ex4);
                                    }
                                }
                                s3 = null;
                            }
                            Label_0328: {
                                if (s3 == null) {
                                    try {
                                        final Matcher matcher3 = WebPlayerView.aparatIdRegex.matcher(s);
                                        if (matcher3.find()) {
                                            playVideoUrl = matcher3.group(1);
                                        }
                                        else {
                                            playVideoUrl = null;
                                        }
                                        if (playVideoUrl == null) {
                                            playVideoUrl = null;
                                        }
                                        break Label_0328;
                                    }
                                    catch (Exception ex5) {
                                        FileLog.e(ex5);
                                    }
                                }
                                playVideoUrl = null;
                            }
                            Label_0388: {
                                if (playVideoUrl == null) {
                                    try {
                                        final Matcher matcher4 = WebPlayerView.twitchClipIdRegex.matcher(s);
                                        if (matcher4.find()) {
                                            group = matcher4.group(1);
                                        }
                                        else {
                                            group = null;
                                        }
                                        if (group == null) {
                                            group = null;
                                        }
                                        break Label_0388;
                                    }
                                    catch (Exception ex6) {
                                        FileLog.e(ex6);
                                    }
                                }
                                group = null;
                            }
                            Label_0448: {
                                if (group == null) {
                                    try {
                                        final Matcher matcher5 = WebPlayerView.twitchStreamIdRegex.matcher(s);
                                        if (matcher5.find()) {
                                            group2 = matcher5.group(1);
                                        }
                                        else {
                                            group2 = null;
                                        }
                                        if (group2 == null) {
                                            group2 = null;
                                        }
                                        break Label_0448;
                                    }
                                    catch (Exception ex7) {
                                        FileLog.e(ex7);
                                    }
                                }
                                group2 = null;
                            }
                            if (group2 == null) {
                                try {
                                    final Matcher matcher6 = WebPlayerView.coubIdRegex.matcher(s);
                                    String group3;
                                    if (matcher6.find()) {
                                        group3 = matcher6.group(1);
                                    }
                                    else {
                                        group3 = null;
                                    }
                                    if (group3 == null) {
                                        group3 = null;
                                    }
                                    final String s4 = group2;
                                    final String s5 = group;
                                    group2 = playVideoUrl;
                                    group = group3;
                                    playVideoUrl = null;
                                    currentYoutubeId = s2;
                                    s6 = s3;
                                    s2 = s5;
                                    s3 = s4;
                                    break Label_0607;
                                }
                                catch (Exception ex8) {
                                    FileLog.e(ex8);
                                }
                            }
                            final String s7 = null;
                            final String s8 = group2;
                            final String s9 = group;
                            group2 = playVideoUrl;
                            group = s7;
                            playVideoUrl = s7;
                            currentYoutubeId = s2;
                            s6 = s3;
                            s2 = s9;
                            s3 = s8;
                            break Label_0607;
                        }
                        continue;
                    }
                }
            }
            else {
                playVideoUrl = null;
            }
            currentYoutubeId = null;
            s6 = null;
            group = (group2 = s6);
            s2 = (s3 = group2);
        }
        this.initied = false;
        this.isCompleted = false;
        this.isAutoplay = isAutoplay;
        this.playVideoUrl = null;
        this.playAudioUrl = null;
        this.destroy();
        this.firstFrameRendered = false;
        this.currentAlpha = 1.0f;
        final AsyncTask currentTask = this.currentTask;
        if (currentTask != null) {
            currentTask.cancel(true);
            this.currentTask = null;
        }
        this.updateFullscreenButton();
        this.updateShareButton();
        this.updateInlineButton();
        this.updatePlayButton();
        if (photo != null) {
            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, 80, true);
            if (closestPhotoSizeWithSize != null) {
                this.controlsView.imageReceiver.setImage(null, null, ImageLocation.getForPhoto(closestPhotoSizeWithSize, photo), "80_80_b", 0, null, o, 1);
                this.drawImage = true;
            }
        }
        else {
            this.drawImage = false;
        }
        final AnimatorSet progressAnimation = this.progressAnimation;
        if (progressAnimation != null) {
            progressAnimation.cancel();
            this.progressAnimation = null;
        }
        this.isLoading = true;
        this.controlsView.setProgress(0);
        String s10;
        if ((s10 = currentYoutubeId) != null) {
            this.currentYoutubeId = currentYoutubeId;
            s10 = null;
        }
        if (playVideoUrl != null) {
            this.initied = true;
            this.playVideoUrl = playVideoUrl;
            this.playVideoType = "other";
            if (this.isAutoplay) {
                this.preparePlayer();
            }
            this.showProgress(false, false);
            this.controlsView.show(true, true);
        }
        else {
            if (s10 != null) {
                final YoutubeVideoTask currentTask2 = new YoutubeVideoTask(s10);
                currentTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                this.currentTask = currentTask2;
            }
            else if (s6 != null) {
                final VimeoVideoTask currentTask3 = new VimeoVideoTask(s6);
                currentTask3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                this.currentTask = currentTask3;
            }
            else if (group != null) {
                final CoubVideoTask currentTask4 = new CoubVideoTask(group);
                currentTask4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                this.currentTask = currentTask4;
                this.isStream = true;
            }
            else if (group2 != null) {
                final AparatVideoTask currentTask5 = new AparatVideoTask(group2);
                currentTask5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                this.currentTask = currentTask5;
            }
            else if (s2 != null) {
                final TwitchClipVideoTask currentTask6 = new TwitchClipVideoTask(s, s2);
                currentTask6.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                this.currentTask = currentTask6;
            }
            else if (s3 != null) {
                final TwitchStreamVideoTask currentTask7 = new TwitchStreamVideoTask(s, s3);
                currentTask7.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])new Void[] { null, null, null });
                this.currentTask = currentTask7;
                this.isStream = true;
            }
            this.controlsView.show(false, false);
            this.showProgress(true, false);
        }
        if (s10 == null && s6 == null && group == null && group2 == null && playVideoUrl == null && s2 == null && s3 == null) {
            this.controlsView.setVisibility(8);
            return false;
        }
        this.controlsView.setVisibility(0);
        return true;
    }
    
    public void onAudioFocusChange(final int n) {
        if (n == -1) {
            if (this.videoPlayer.isPlaying()) {
                this.videoPlayer.pause();
                this.updatePlayButton();
            }
            this.hasAudioFocus = false;
            this.audioFocus = 0;
        }
        else if (n == 1) {
            this.audioFocus = 2;
            if (this.resumeAudioOnFocusGain) {
                this.resumeAudioOnFocusGain = false;
                this.videoPlayer.play();
            }
        }
        else if (n == -3) {
            this.audioFocus = 1;
        }
        else if (n == -2) {
            this.audioFocus = 0;
            if (this.videoPlayer.isPlaying()) {
                this.resumeAudioOnFocusGain = true;
                this.videoPlayer.pause();
                this.updatePlayButton();
            }
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(10.0f)), this.backgroundPaint);
    }
    
    public void onError(final Exception ex) {
        FileLog.e(ex);
        this.onInitFailed();
    }
    
    protected void onLayout(final boolean b, int n, int n2, int n3, int n4) {
        n3 -= n;
        n = (n3 - this.aspectRatioFrameLayout.getMeasuredWidth()) / 2;
        n2 = n4 - n2;
        n4 = (n2 - AndroidUtilities.dp(10.0f) - this.aspectRatioFrameLayout.getMeasuredHeight()) / 2;
        final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        aspectRatioFrameLayout.layout(n, n4, aspectRatioFrameLayout.getMeasuredWidth() + n, this.aspectRatioFrameLayout.getMeasuredHeight() + n4);
        if (this.controlsView.getParent() == this) {
            final ControlsView controlsView = this.controlsView;
            controlsView.layout(0, 0, controlsView.getMeasuredWidth(), this.controlsView.getMeasuredHeight());
        }
        n = (n3 - this.progressView.getMeasuredWidth()) / 2;
        n2 = (n2 - this.progressView.getMeasuredHeight()) / 2;
        final RadialProgressView progressView = this.progressView;
        progressView.layout(n, n2, progressView.getMeasuredWidth() + n, this.progressView.getMeasuredHeight() + n2);
        this.controlsView.imageReceiver.setImageCoords(0, 0, this.getMeasuredWidth(), this.getMeasuredHeight() - AndroidUtilities.dp(10.0f));
    }
    
    protected void onMeasure(int size, int size2) {
        size = View$MeasureSpec.getSize(size);
        size2 = View$MeasureSpec.getSize(size2);
        this.aspectRatioFrameLayout.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2 - AndroidUtilities.dp(10.0f), 1073741824));
        if (this.controlsView.getParent() == this) {
            this.controlsView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2, 1073741824));
        }
        this.progressView.measure(View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824));
        this.setMeasuredDimension(size, size2);
    }
    
    public void onRenderedFirstFrame() {
        this.firstFrameRendered = true;
        this.lastUpdateTime = System.currentTimeMillis();
        this.controlsView.invalidate();
    }
    
    public void onStateChanged(final boolean b, final int n) {
        if (n != 2) {
            if (this.videoPlayer.getDuration() != -9223372036854775807L) {
                this.controlsView.setDuration((int)(this.videoPlayer.getDuration() / 1000L));
            }
            else {
                this.controlsView.setDuration(0);
            }
        }
        if (n != 4 && n != 1 && this.videoPlayer.isPlaying()) {
            this.delegate.onPlayStateChanged(this, true);
        }
        else {
            this.delegate.onPlayStateChanged(this, false);
        }
        if (this.videoPlayer.isPlaying() && n != 4) {
            this.updatePlayButton();
        }
        else if (n == 4) {
            this.isCompleted = true;
            this.videoPlayer.pause();
            this.videoPlayer.seekTo(0L);
            this.updatePlayButton();
            this.controlsView.show(true, true);
        }
    }
    
    public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
        if (this.changingTextureView) {
            this.changingTextureView = false;
            if (this.inFullscreen || this.isInline) {
                if (this.isInline) {
                    this.waitingForFirstTextureUpload = 1;
                }
                this.changedTextureView.setSurfaceTexture(surfaceTexture);
                this.changedTextureView.setSurfaceTextureListener(this.surfaceTextureListener);
                this.changedTextureView.setVisibility(0);
                return true;
            }
        }
        return false;
    }
    
    public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
        if (this.waitingForFirstTextureUpload == 2) {
            final ImageView textureImageView = this.textureImageView;
            if (textureImageView != null) {
                textureImageView.setVisibility(4);
                this.textureImageView.setImageDrawable((Drawable)null);
                final Bitmap currentBitmap = this.currentBitmap;
                if (currentBitmap != null) {
                    currentBitmap.recycle();
                    this.currentBitmap = null;
                }
            }
            this.switchingInlineMode = false;
            this.delegate.onSwitchInlineMode((View)this.controlsView, false, this.aspectRatioFrameLayout.getAspectRatio(), this.aspectRatioFrameLayout.getVideoRotation(), this.allowInlineAnimation);
            this.waitingForFirstTextureUpload = 0;
        }
    }
    
    public void onVideoSizeChanged(final int n, final int n2, final int n3, float n4) {
        if (this.aspectRatioFrameLayout != null) {
            int n5 = n;
            int n6 = n2;
            if (n3 != 90) {
                if (n3 == 270) {
                    n5 = n;
                    n6 = n2;
                }
                else {
                    n6 = n;
                    n5 = n2;
                }
            }
            if (n5 == 0) {
                n4 = 1.0f;
            }
            else {
                n4 = n6 * n4 / n5;
            }
            this.aspectRatioFrameLayout.setAspectRatio(n4, n3);
            if (this.inFullscreen) {
                this.delegate.onVideoSizeChanged(n4, n3);
            }
        }
    }
    
    public void pause() {
        this.videoPlayer.pause();
        this.updatePlayButton();
        this.controlsView.show(true, true);
    }
    
    public void updateTextureImageView() {
        if (this.textureImageView == null) {
            return;
        }
        try {
            this.currentBitmap = Bitmaps.createBitmap(this.textureView.getWidth(), this.textureView.getHeight(), Bitmap$Config.ARGB_8888);
            this.changedTextureView.getBitmap(this.currentBitmap);
        }
        catch (Throwable t) {
            final Bitmap currentBitmap = this.currentBitmap;
            if (currentBitmap != null) {
                currentBitmap.recycle();
                this.currentBitmap = null;
            }
            FileLog.e(t);
        }
        if (this.currentBitmap != null) {
            this.textureImageView.setVisibility(0);
            this.textureImageView.setImageBitmap(this.currentBitmap);
        }
        else {
            this.textureImageView.setImageDrawable((Drawable)null);
        }
    }
    
    private class AparatVideoTask extends AsyncTask<Void, Void, String>
    {
        private boolean canRetry;
        private String[] results;
        private String videoId;
        
        public AparatVideoTask(final String videoId) {
            this.canRetry = true;
            this.results = new String[2];
            this.videoId = videoId;
        }
        
        protected String doInBackground(Void... array) {
            final String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "http://www.aparat.com/video/video/embed/vt/frame/showvideo/yes/videohash/%s", this.videoId));
            final boolean cancelled = this.isCancelled();
            array = null;
            if (cancelled) {
                return null;
            }
            try {
                final Matcher matcher = WebPlayerView.aparatFileListPattern.matcher(downloadUrlContent);
                if (matcher.find()) {
                    final JSONArray jsonArray = new JSONArray(matcher.group(1));
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        final JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                        if (jsonArray2.length() != 0) {
                            final JSONObject jsonObject = jsonArray2.getJSONObject(0);
                            if (jsonObject.has("file")) {
                                this.results[0] = jsonObject.getString("file");
                                this.results[1] = "other";
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (!this.isCancelled()) {
                array = (Void[])(Object)this.results[0];
            }
            return (String)(Object)array;
        }
        
        protected void onPostExecute(final String s) {
            if (s != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = s;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            }
            else if (!this.isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }
    
    public interface CallJavaResultInterface
    {
        void jsCallFinished(final String p0);
    }
    
    private class ControlsView extends FrameLayout
    {
        private int bufferedPosition;
        private AnimatorSet currentAnimation;
        private int currentProgressX;
        private int duration;
        private StaticLayout durationLayout;
        private int durationWidth;
        private Runnable hideRunnable;
        private ImageReceiver imageReceiver;
        private boolean isVisible;
        private int lastProgressX;
        private int progress;
        private Paint progressBufferedPaint;
        private Paint progressInnerPaint;
        private StaticLayout progressLayout;
        private Paint progressPaint;
        private boolean progressPressed;
        private TextPaint textPaint;
        
        public ControlsView(final Context context) {
            super(context);
            this.isVisible = true;
            this.hideRunnable = new _$$Lambda$WebPlayerView$ControlsView$QYTgg3cx1r3S4djGCF7dtRzr3Os(this);
            this.setWillNotDraw(false);
            (this.textPaint = new TextPaint(1)).setColor(-1);
            this.textPaint.setTextSize((float)AndroidUtilities.dp(12.0f));
            (this.progressPaint = new Paint(1)).setColor(-15095832);
            (this.progressInnerPaint = new Paint()).setColor(-6975081);
            (this.progressBufferedPaint = new Paint(1)).setColor(-1);
            this.imageReceiver = new ImageReceiver((View)this);
        }
        
        private void checkNeedHide() {
            AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
            if (this.isVisible && WebPlayerView.this.videoPlayer.isPlaying()) {
                AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
            }
        }
        
        protected void onDraw(final Canvas canvas) {
            if (WebPlayerView.this.drawImage) {
                if (WebPlayerView.this.firstFrameRendered && WebPlayerView.this.currentAlpha != 0.0f) {
                    final long currentTimeMillis = System.currentTimeMillis();
                    final long access$4600 = WebPlayerView.this.lastUpdateTime;
                    WebPlayerView.this.lastUpdateTime = currentTimeMillis;
                    final WebPlayerView this$0 = WebPlayerView.this;
                    this$0.currentAlpha -= (currentTimeMillis - access$4600) / 150.0f;
                    if (WebPlayerView.this.currentAlpha < 0.0f) {
                        WebPlayerView.this.currentAlpha = 0.0f;
                    }
                    this.invalidate();
                }
                this.imageReceiver.setAlpha(WebPlayerView.this.currentAlpha);
                this.imageReceiver.draw(canvas);
            }
            if (WebPlayerView.this.videoPlayer.isPlayerPrepared() && !WebPlayerView.this.isStream) {
                int measuredWidth = this.getMeasuredWidth();
                final int measuredHeight = this.getMeasuredHeight();
                if (!WebPlayerView.this.isInline) {
                    final StaticLayout durationLayout = this.durationLayout;
                    final int n = 6;
                    if (durationLayout != null) {
                        canvas.save();
                        final float n2 = (float)(measuredWidth - AndroidUtilities.dp(58.0f) - this.durationWidth);
                        int n3;
                        if (WebPlayerView.this.inFullscreen) {
                            n3 = 6;
                        }
                        else {
                            n3 = 10;
                        }
                        canvas.translate(n2, (float)(measuredHeight - AndroidUtilities.dp((float)(n3 + 29))));
                        this.durationLayout.draw(canvas);
                        canvas.restore();
                    }
                    if (this.progressLayout != null) {
                        canvas.save();
                        final float n4 = (float)AndroidUtilities.dp(18.0f);
                        int n5;
                        if (WebPlayerView.this.inFullscreen) {
                            n5 = n;
                        }
                        else {
                            n5 = 10;
                        }
                        canvas.translate(n4, (float)(measuredHeight - AndroidUtilities.dp((float)(n5 + 29))));
                        this.progressLayout.draw(canvas);
                        canvas.restore();
                    }
                }
                if (this.duration != 0) {
                    final boolean access$4601 = WebPlayerView.this.isInline;
                    float n6 = 7.0f;
                    int n9 = 0;
                    int n10 = 0;
                    int n11 = 0;
                    Label_0496: {
                        int n7;
                        int n8;
                        if (access$4601) {
                            n7 = measuredHeight - AndroidUtilities.dp(3.0f);
                            n8 = AndroidUtilities.dp(7.0f);
                        }
                        else {
                            if (WebPlayerView.this.inFullscreen) {
                                final int dp = AndroidUtilities.dp(29.0f);
                                final int dp2 = AndroidUtilities.dp(36.0f);
                                final int durationWidth = this.durationWidth;
                                final int dp3 = AndroidUtilities.dp(76.0f);
                                final int durationWidth2 = this.durationWidth;
                                final int dp4 = AndroidUtilities.dp(28.0f);
                                measuredWidth = measuredWidth - dp3 - durationWidth2;
                                n9 = measuredHeight - dp4;
                                n10 = measuredHeight - dp;
                                n11 = dp2 + durationWidth;
                                break Label_0496;
                            }
                            n7 = measuredHeight - AndroidUtilities.dp(13.0f);
                            n8 = AndroidUtilities.dp(12.0f);
                        }
                        n9 = measuredHeight - n8;
                        n10 = n7;
                        n11 = 0;
                    }
                    if (WebPlayerView.this.inFullscreen) {
                        canvas.drawRect((float)n11, (float)n10, (float)measuredWidth, (float)(AndroidUtilities.dp(3.0f) + n10), this.progressInnerPaint);
                    }
                    int currentProgressX;
                    if (this.progressPressed) {
                        currentProgressX = this.currentProgressX;
                    }
                    else {
                        currentProgressX = (int)((measuredWidth - n11) * (this.progress / (float)this.duration)) + n11;
                    }
                    final int bufferedPosition = this.bufferedPosition;
                    if (bufferedPosition != 0) {
                        final int duration = this.duration;
                        if (duration != 0) {
                            final float n12 = (float)n11;
                            final float n13 = (float)n10;
                            final float n14 = (float)(measuredWidth - n11);
                            final float n15 = bufferedPosition / (float)duration;
                            final float n16 = (float)(AndroidUtilities.dp(3.0f) + n10);
                            Paint paint;
                            if (WebPlayerView.this.inFullscreen) {
                                paint = this.progressBufferedPaint;
                            }
                            else {
                                paint = this.progressInnerPaint;
                            }
                            canvas.drawRect(n12, n13, n14 * n15 + n12, n16, paint);
                        }
                    }
                    final float n17 = (float)n11;
                    final float n18 = (float)n10;
                    final float n19 = (float)currentProgressX;
                    canvas.drawRect(n17, n18, n19, (float)(n10 + AndroidUtilities.dp(3.0f)), this.progressPaint);
                    if (!WebPlayerView.this.isInline) {
                        final float n20 = (float)n9;
                        if (!this.progressPressed) {
                            n6 = 5.0f;
                        }
                        canvas.drawCircle(n19, n20, (float)AndroidUtilities.dp(n6), this.progressPaint);
                    }
                }
            }
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            if (!this.isVisible) {
                this.show(true, true);
                return true;
            }
            this.onTouchEvent(motionEvent);
            return this.progressPressed;
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            int currentProgressX;
            int measuredWidth;
            int n;
            if (WebPlayerView.this.inFullscreen) {
                currentProgressX = AndroidUtilities.dp(36.0f) + this.durationWidth;
                measuredWidth = this.getMeasuredWidth() - AndroidUtilities.dp(76.0f) - this.durationWidth;
                n = this.getMeasuredHeight() - AndroidUtilities.dp(28.0f);
            }
            else {
                measuredWidth = this.getMeasuredWidth();
                n = this.getMeasuredHeight() - AndroidUtilities.dp(12.0f);
                currentProgressX = 0;
            }
            final int duration = this.duration;
            int n2;
            if (duration != 0) {
                n2 = (int)((measuredWidth - currentProgressX) * (this.progress / (float)duration));
            }
            else {
                n2 = 0;
            }
            final int currentProgressX2 = n2 + currentProgressX;
            if (motionEvent.getAction() == 0) {
                if (this.isVisible && !WebPlayerView.this.isInline && !WebPlayerView.this.isStream) {
                    if (this.duration != 0) {
                        final int lastProgressX = (int)motionEvent.getX();
                        final int n3 = (int)motionEvent.getY();
                        if (lastProgressX >= currentProgressX2 - AndroidUtilities.dp(10.0f) && lastProgressX <= AndroidUtilities.dp(10.0f) + currentProgressX2 && n3 >= n - AndroidUtilities.dp(10.0f) && n3 <= n + AndroidUtilities.dp(10.0f)) {
                            this.progressPressed = true;
                            this.lastProgressX = lastProgressX;
                            this.currentProgressX = currentProgressX2;
                            this.getParent().requestDisallowInterceptTouchEvent(true);
                            this.invalidate();
                        }
                    }
                }
                else {
                    this.show(true, true);
                }
                AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
            }
            else if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
                if (motionEvent.getAction() == 2 && this.progressPressed) {
                    final int lastProgressX2 = (int)motionEvent.getX();
                    this.currentProgressX -= this.lastProgressX - lastProgressX2;
                    this.lastProgressX = lastProgressX2;
                    final int currentProgressX3 = this.currentProgressX;
                    if (currentProgressX3 < currentProgressX) {
                        this.currentProgressX = currentProgressX;
                    }
                    else if (currentProgressX3 > measuredWidth) {
                        this.currentProgressX = measuredWidth;
                    }
                    this.setProgress((int)(this.duration * 1000 * ((this.currentProgressX - currentProgressX) / (float)(measuredWidth - currentProgressX))));
                    this.invalidate();
                }
            }
            else {
                if (WebPlayerView.this.initied && WebPlayerView.this.videoPlayer.isPlaying()) {
                    AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
                }
                if (this.progressPressed) {
                    this.progressPressed = false;
                    if (WebPlayerView.this.initied) {
                        this.progress = (int)(this.duration * ((this.currentProgressX - currentProgressX) / (float)(measuredWidth - currentProgressX)));
                        WebPlayerView.this.videoPlayer.seekTo(this.progress * 1000L);
                    }
                }
            }
            super.onTouchEvent(motionEvent);
            return true;
        }
        
        public void requestDisallowInterceptTouchEvent(final boolean b) {
            super.requestDisallowInterceptTouchEvent(b);
            this.checkNeedHide();
        }
        
        public void setBufferedProgress(final int bufferedPosition) {
            this.bufferedPosition = bufferedPosition;
            this.invalidate();
        }
        
        public void setDuration(final int duration) {
            if (this.duration != duration && duration >= 0) {
                if (!WebPlayerView.this.isStream) {
                    this.duration = duration;
                    this.durationLayout = new StaticLayout((CharSequence)String.format(Locale.US, "%d:%02d", this.duration / 60, this.duration % 60), this.textPaint, AndroidUtilities.dp(1000.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (this.durationLayout.getLineCount() > 0) {
                        this.durationWidth = (int)Math.ceil(this.durationLayout.getLineWidth(0));
                    }
                    this.invalidate();
                }
            }
        }
        
        public void setProgress(final int progress) {
            if (!this.progressPressed && progress >= 0) {
                if (!WebPlayerView.this.isStream) {
                    this.progress = progress;
                    this.progressLayout = new StaticLayout((CharSequence)String.format(Locale.US, "%d:%02d", this.progress / 60, this.progress % 60), this.textPaint, AndroidUtilities.dp(1000.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.invalidate();
                }
            }
        }
        
        public void show(final boolean isVisible, final boolean b) {
            if (this.isVisible == isVisible) {
                return;
            }
            this.isVisible = isVisible;
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.cancel();
            }
            if (this.isVisible) {
                if (b) {
                    (this.currentAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 1.0f }) });
                    this.currentAnimation.setDuration(150L);
                    this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            ControlsView.this.currentAnimation = null;
                        }
                    });
                    this.currentAnimation.start();
                }
                else {
                    this.setAlpha(1.0f);
                }
            }
            else if (b) {
                (this.currentAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "alpha", new float[] { 0.0f }) });
                this.currentAnimation.setDuration(150L);
                this.currentAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        ControlsView.this.currentAnimation = null;
                    }
                });
                this.currentAnimation.start();
            }
            else {
                this.setAlpha(0.0f);
            }
            this.checkNeedHide();
        }
    }
    
    private class CoubVideoTask extends AsyncTask<Void, Void, String>
    {
        private boolean canRetry;
        private String[] results;
        private String videoId;
        
        public CoubVideoTask(final String videoId) {
            this.canRetry = true;
            this.results = new String[4];
            this.videoId = videoId;
        }
        
        private String decodeUrl(final String str) {
            final StringBuilder sb = new StringBuilder(str);
            for (int i = 0; i < sb.length(); ++i) {
                final char char1 = sb.charAt(i);
                char c;
                if (char1 == (c = Character.toLowerCase(char1))) {
                    c = Character.toUpperCase(char1);
                }
                sb.setCharAt(i, c);
            }
            try {
                return new String(Base64.decode(sb.toString(), 0), "UTF-8");
            }
            catch (Exception ex) {
                return null;
            }
        }
        
        protected String doInBackground(Void... array) {
            final String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://coub.com/api/v2/coubs/%s.json", this.videoId));
            final boolean cancelled = this.isCancelled();
            array = null;
            if (cancelled) {
                return null;
            }
            try {
                final JSONObject jsonObject = new JSONObject(downloadUrlContent).getJSONObject("file_versions").getJSONObject("mobile");
                final String decodeUrl = this.decodeUrl(jsonObject.getString("gifv"));
                final String string = jsonObject.getJSONArray("audio").getString(0);
                if (decodeUrl != null && string != null) {
                    this.results[0] = decodeUrl;
                    this.results[1] = "other";
                    this.results[2] = string;
                    this.results[3] = "other";
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (!this.isCancelled()) {
                array = (Void[])(Object)this.results[0];
            }
            return (String)(Object)array;
        }
        
        protected void onPostExecute(final String s) {
            if (s != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = s;
                WebPlayerView.this.playVideoType = this.results[1];
                WebPlayerView.this.playAudioUrl = this.results[2];
                WebPlayerView.this.playAudioType = this.results[3];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            }
            else if (!this.isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }
    
    private class JSExtractor
    {
        private String[] assign_operators;
        ArrayList<String> codeLines;
        private String jsCode;
        private String[] operators;
        
        public JSExtractor(final String jsCode) {
            this.codeLines = new ArrayList<String>();
            this.operators = new String[] { "|", "^", "&", ">>", "<<", "-", "+", "%", "/", "*" };
            this.assign_operators = new String[] { "|=", "^=", "&=", ">>=", "<<=", "-=", "+=", "%=", "/=", "*=", "=" };
            this.jsCode = jsCode;
        }
        
        private void buildFunction(String[] split, final String s) throws Exception {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            for (int i = 0; i < split.length; ++i) {
                hashMap.put(split[i], "");
            }
            split = s.split(";");
            final boolean[] array = { false };
            for (int j = 0; j < split.length; ++j) {
                this.interpretStatement(split[j], hashMap, array, 100);
                if (array[0]) {
                    return;
                }
            }
        }
        
        private String extractFunction(String quote) {
            try {
                quote = Pattern.quote(quote);
                final Matcher matcher = Pattern.compile(String.format(Locale.US, "(?x)(?:function\\s+%s|[{;,]\\s*%s\\s*=\\s*function|var\\s+%s\\s*=\\s*function)\\s*\\(([^)]*)\\)\\s*\\{([^}]+)\\}", quote, quote, quote)).matcher(this.jsCode);
                if (matcher.find()) {
                    final String group = matcher.group();
                    if (!this.codeLines.contains(group)) {
                        final ArrayList<String> codeLines = this.codeLines;
                        final StringBuilder sb = new StringBuilder();
                        sb.append(group);
                        sb.append(";");
                        codeLines.add(sb.toString());
                    }
                    this.buildFunction(matcher.group(1).split(","), matcher.group(2));
                }
            }
            catch (Exception ex) {
                this.codeLines.clear();
                FileLog.e(ex);
            }
            return TextUtils.join((CharSequence)"", (Iterable)this.codeLines);
        }
        
        private HashMap<String, Object> extractObject(String group) throws Exception {
            final HashMap<String, Object> hashMap = new HashMap<String, Object>();
            final Matcher matcher = Pattern.compile(String.format(Locale.US, "(?:var\\s+)?%s\\s*=\\s*\\{\\s*((%s\\s*:\\s*function\\(.*?\\)\\s*\\{.*?\\}(?:,\\s*)?)*)\\}\\s*;", Pattern.quote(group), "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')")).matcher(this.jsCode);
            group = null;
            while (matcher.find()) {
                final String group2 = matcher.group();
                group = matcher.group(2);
                if (TextUtils.isEmpty((CharSequence)group)) {
                    continue;
                }
                if (!this.codeLines.contains(group2)) {
                    this.codeLines.add(matcher.group());
                    break;
                }
                break;
            }
            final Matcher matcher2 = Pattern.compile(String.format("(%s)\\s*:\\s*function\\(([a-z,]+)\\)\\{([^}]+)\\}", "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')")).matcher(group);
            while (matcher2.find()) {
                this.buildFunction(matcher2.group(2).split(","), matcher2.group(3));
            }
            return hashMap;
        }
        
        private void interpretExpression(String s, final HashMap<String, String> hashMap, final int n) throws Exception {
            final String trim = s.trim();
            if (TextUtils.isEmpty((CharSequence)trim)) {
                return;
            }
            final int n2 = 0;
            s = trim;
            if (trim.charAt(0) == '(') {
                final Matcher matcher = WebPlayerView.exprParensPattern.matcher(trim);
                int n3 = 0;
                int n4;
                while (true) {
                    n4 = n3;
                    s = trim;
                    if (!matcher.find()) {
                        break;
                    }
                    if (matcher.group(0).indexOf(48) == 40) {
                        ++n3;
                    }
                    else {
                        n4 = n3 - 1;
                        if ((n3 = n4) != 0) {
                            continue;
                        }
                        this.interpretExpression(trim.substring(1, matcher.start()), hashMap, n);
                        if (TextUtils.isEmpty((CharSequence)(s = trim.substring(matcher.end()).trim()))) {
                            return;
                        }
                        break;
                    }
                }
                if (n4 != 0) {
                    throw new Exception(String.format("Premature end of parens in %s", s));
                }
            }
            int n5 = 0;
            while (true) {
                final String[] assign_operators = this.assign_operators;
                if (n5 < assign_operators.length) {
                    final Matcher matcher2 = Pattern.compile(String.format(Locale.US, "(?x)(%s)(?:\\[([^\\]]+?)\\])?\\s*%s(.*)$", "[a-zA-Z_$][a-zA-Z_$0-9]*", Pattern.quote(assign_operators[n5]))).matcher(s);
                    if (matcher2.find()) {
                        this.interpretExpression(matcher2.group(3), hashMap, n - 1);
                        s = matcher2.group(2);
                        if (!TextUtils.isEmpty((CharSequence)s)) {
                            this.interpretExpression(s, hashMap, n);
                        }
                        else {
                            hashMap.put(matcher2.group(1), "");
                        }
                        return;
                    }
                    ++n5;
                }
                else {
                    try {
                        Integer.parseInt(s);
                    }
                    catch (Exception ex) {
                        if (Pattern.compile(String.format(Locale.US, "(?!if|return|true|false)(%s)$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(s).find()) {
                            return;
                        }
                        if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
                            return;
                        }
                        try {
                            new JSONObject(s).toString();
                        }
                        catch (Exception ex2) {
                            final Matcher matcher3 = Pattern.compile(String.format(Locale.US, "(%s)\\[(.+)\\]$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(s);
                            if (matcher3.find()) {
                                matcher3.group(1);
                                this.interpretExpression(matcher3.group(2), hashMap, n - 1);
                                return;
                            }
                            final Matcher matcher4 = Pattern.compile(String.format(Locale.US, "(%s)(?:\\.([^(]+)|\\[([^]]+)\\])\\s*(?:\\(+([^()]*)\\))?$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(s);
                            if (matcher4.find()) {
                                final String group = matcher4.group(1);
                                final String group2 = matcher4.group(2);
                                final String group3 = matcher4.group(3);
                                String s2 = group2;
                                if (TextUtils.isEmpty((CharSequence)group2)) {
                                    s2 = group3;
                                }
                                s2.replace("\"", "");
                                final String group4 = matcher4.group(4);
                                if (hashMap.get(group) == null) {
                                    this.extractObject(group);
                                }
                                if (group4 == null) {
                                    return;
                                }
                                if (s.charAt(s.length() - 1) == ')') {
                                    if (group4.length() != 0) {
                                        final String[] split = group4.split(",");
                                        for (int i = n2; i < split.length; ++i) {
                                            this.interpretExpression(split[i], hashMap, n);
                                        }
                                    }
                                    return;
                                }
                                throw new Exception("last char not ')'");
                            }
                            else {
                                final Matcher matcher5 = Pattern.compile(String.format(Locale.US, "(%s)\\[(.+)\\]$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(s);
                                if (matcher5.find()) {
                                    hashMap.get(matcher5.group(1));
                                    this.interpretExpression(matcher5.group(2), hashMap, n - 1);
                                    return;
                                }
                                int n6 = 0;
                                while (true) {
                                    final String[] operators = this.operators;
                                    if (n6 >= operators.length) {
                                        final Matcher matcher6 = Pattern.compile(String.format(Locale.US, "^(%s)\\(([a-zA-Z0-9_$,]*)\\)$", "[a-zA-Z_$][a-zA-Z_$0-9]*")).matcher(s);
                                        if (matcher6.find()) {
                                            this.extractFunction(matcher6.group(1));
                                        }
                                        throw new Exception(String.format("Unsupported JS expression %s", s));
                                    }
                                    final String s3 = operators[n6];
                                    final Matcher matcher7 = Pattern.compile(String.format(Locale.US, "(.+?)%s(.+)", Pattern.quote(s3))).matcher(s);
                                    if (matcher7.find()) {
                                        final boolean[] array = { false };
                                        final String group5 = matcher7.group(1);
                                        final int n7 = n - 1;
                                        this.interpretStatement(group5, hashMap, array, n7);
                                        if (array[0]) {
                                            throw new Exception(String.format("Premature left-side return of %s in %s", s3, s));
                                        }
                                        this.interpretStatement(matcher7.group(2), hashMap, array, n7);
                                        if (array[0]) {
                                            throw new Exception(String.format("Premature right-side return of %s in %s", s3, s));
                                        }
                                    }
                                    ++n6;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        private void interpretStatement(String s, final HashMap<String, String> hashMap, final boolean[] array, final int n) throws Exception {
            if (n >= 0) {
                array[0] = false;
                final String trim = s.trim();
                final Matcher matcher = WebPlayerView.stmtVarPattern.matcher(trim);
                if (matcher.find()) {
                    s = trim.substring(matcher.group(0).length());
                }
                else {
                    final Matcher matcher2 = WebPlayerView.stmtReturnPattern.matcher(trim);
                    s = trim;
                    if (matcher2.find()) {
                        s = trim.substring(matcher2.group(0).length());
                        array[0] = true;
                    }
                }
                this.interpretExpression(s, hashMap, n);
                return;
            }
            throw new Exception("recursion limit reached");
        }
    }
    
    public class JavaScriptInterface
    {
        private final CallJavaResultInterface callJavaResultInterface;
        
        public JavaScriptInterface(final CallJavaResultInterface callJavaResultInterface) {
            this.callJavaResultInterface = callJavaResultInterface;
        }
        
        @JavascriptInterface
        public void returnResultToJava(final String s) {
            this.callJavaResultInterface.jsCallFinished(s);
        }
    }
    
    private class TwitchClipVideoTask extends AsyncTask<Void, Void, String>
    {
        private boolean canRetry;
        private String currentUrl;
        private String[] results;
        private String videoId;
        
        public TwitchClipVideoTask(final String currentUrl, final String videoId) {
            this.canRetry = true;
            this.results = new String[2];
            this.videoId = videoId;
            this.currentUrl = currentUrl;
        }
        
        protected String doInBackground(Void... array) {
            final WebPlayerView this$0 = WebPlayerView.this;
            final String currentUrl = this.currentUrl;
            array = null;
            final String downloadUrlContent = this$0.downloadUrlContent(this, currentUrl, null, false);
            if (this.isCancelled()) {
                return null;
            }
            try {
                final Matcher matcher = WebPlayerView.twitchClipFilePattern.matcher(downloadUrlContent);
                if (matcher.find()) {
                    this.results[0] = new JSONObject(matcher.group(1)).getJSONArray("quality_options").getJSONObject(0).getString("source");
                    this.results[1] = "other";
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (!this.isCancelled()) {
                array = (Void[])(Object)this.results[0];
            }
            return (String)(Object)array;
        }
        
        protected void onPostExecute(final String s) {
            if (s != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = s;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            }
            else if (!this.isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }
    
    private class TwitchStreamVideoTask extends AsyncTask<Void, Void, String>
    {
        private boolean canRetry;
        private String currentUrl;
        private String[] results;
        private String videoId;
        
        public TwitchStreamVideoTask(final String currentUrl, final String videoId) {
            this.canRetry = true;
            this.results = new String[2];
            this.videoId = videoId;
            this.currentUrl = currentUrl;
        }
        
        protected String doInBackground(Void... array) {
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("Client-ID", "jzkbprff40iqj646a697cyrvl0zt2m6");
            final int index = this.videoId.indexOf(38);
            if (index > 0) {
                this.videoId = this.videoId.substring(0, index);
            }
            final String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://api.twitch.tv/kraken/streams/%s?stream_type=all", this.videoId), hashMap, false);
            final boolean cancelled = this.isCancelled();
            array = null;
            if (cancelled) {
                return null;
            }
            try {
                new JSONObject(downloadUrlContent).getJSONObject("stream");
                final JSONObject jsonObject = new JSONObject(WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://api.twitch.tv/api/channels/%s/access_token", this.videoId), hashMap, false));
                final String encode = URLEncoder.encode(jsonObject.getString("sig"), "UTF-8");
                final String encode2 = URLEncoder.encode(jsonObject.getString("token"), "UTF-8");
                final StringBuilder sb = new StringBuilder();
                sb.append("https://youtube.googleapis.com/v/");
                sb.append(this.videoId);
                URLEncoder.encode(sb.toString(), "UTF-8");
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("allow_source=true&allow_audio_only=true&allow_spectre=true&player=twitchweb&segment_preference=4&p=");
                sb2.append((int)(Math.random() * 1.0E7));
                sb2.append("&sig=");
                sb2.append(encode);
                sb2.append("&token=");
                sb2.append(encode2);
                this.results[0] = String.format(Locale.US, "https://usher.ttvnw.net/api/channel/hls/%s.m3u8?%s", this.videoId, sb2.toString());
                this.results[1] = "hls";
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (!this.isCancelled()) {
                array = (Void[])(Object)this.results[0];
            }
            return (String)(Object)array;
        }
        
        protected void onPostExecute(final String s) {
            if (s != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = s;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            }
            else if (!this.isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }
    
    private class VimeoVideoTask extends AsyncTask<Void, Void, String>
    {
        private boolean canRetry;
        private String[] results;
        private String videoId;
        
        public VimeoVideoTask(final String videoId) {
            this.canRetry = true;
            this.results = new String[2];
            this.videoId = videoId;
        }
        
        protected String doInBackground(Void... array) {
            final String downloadUrlContent = WebPlayerView.this.downloadUrlContent(this, String.format(Locale.US, "https://player.vimeo.com/video/%s/config", this.videoId));
            final boolean cancelled = this.isCancelled();
            array = null;
            if (cancelled) {
                return null;
            }
            try {
                final JSONObject jsonObject = new JSONObject(downloadUrlContent).getJSONObject("request").getJSONObject("files");
                if (jsonObject.has("hls")) {
                    final JSONObject jsonObject2 = jsonObject.getJSONObject("hls");
                    try {
                        this.results[0] = jsonObject2.getString("url");
                    }
                    catch (Exception ex2) {
                        this.results[0] = jsonObject2.getJSONObject("cdns").getJSONObject(jsonObject2.getString("default_cdn")).getString("url");
                    }
                    this.results[1] = "hls";
                }
                else if (jsonObject.has("progressive")) {
                    this.results[1] = "other";
                    this.results[0] = jsonObject.getJSONArray("progressive").getJSONObject(0).getString("url");
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (!this.isCancelled()) {
                array = (Void[])(Object)this.results[0];
            }
            return (String)(Object)array;
        }
        
        protected void onPostExecute(final String s) {
            if (s != null) {
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = s;
                WebPlayerView.this.playVideoType = this.results[1];
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            }
            else if (!this.isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }
    
    public interface WebPlayerViewDelegate
    {
        boolean checkInlinePermissions();
        
        ViewGroup getTextureViewContainer();
        
        void onInitFailed();
        
        void onInlineSurfaceTextureReady();
        
        void onPlayStateChanged(final WebPlayerView p0, final boolean p1);
        
        void onSharePressed();
        
        TextureView onSwitchInlineMode(final View p0, final boolean p1, final float p2, final int p3, final boolean p4);
        
        TextureView onSwitchToFullscreen(final View p0, final boolean p1, final float p2, final int p3, final boolean p4);
        
        void onVideoSizeChanged(final float p0, final int p1);
        
        void prepareToSwitchInlineMode(final boolean p0, final Runnable p1, final float p2, final boolean p3);
    }
    
    private class YoutubeVideoTask extends AsyncTask<Void, Void, String[]>
    {
        private boolean canRetry;
        private CountDownLatch countDownLatch;
        private String[] result;
        private String sig;
        private String videoId;
        
        public YoutubeVideoTask(final String videoId) {
            this.canRetry = true;
            this.countDownLatch = new CountDownLatch(1);
            this.result = new String[2];
            this.videoId = videoId;
        }
        
        private void onInterfaceResult(final String str) {
            final String[] result = this.result;
            final String s = result[0];
            final String sig = this.sig;
            final StringBuilder sb = new StringBuilder();
            sb.append("/signature/");
            sb.append(str);
            result[0] = s.replace(sig, sb.toString());
            this.countDownLatch.countDown();
        }
        
        protected String[] doInBackground(Void... str) {
            final WebPlayerView this$0 = WebPlayerView.this;
            final StringBuilder sb = new StringBuilder();
            sb.append("https://www.youtube.com/embed/");
            sb.append(this.videoId);
            final String downloadUrlContent = this$0.downloadUrlContent(this, sb.toString());
            if (this.isCancelled()) {
                return null;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("video_id=");
            sb2.append(this.videoId);
            sb2.append("&ps=default&gl=US&hl=en");
            str = (Void[])(Object)sb2.toString();
            try {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append((String)(Object)str);
                sb3.append("&eurl=");
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("https://youtube.googleapis.com/v/");
                sb4.append(this.videoId);
                sb3.append(URLEncoder.encode(sb4.toString(), "UTF-8"));
                str = (Void[])(Object)sb3.toString();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            Object str2 = str;
            if (downloadUrlContent != null) {
                final Matcher matcher = WebPlayerView.stsPattern.matcher(downloadUrlContent);
                if (matcher.find()) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append((String)(Object)str);
                    sb5.append("&sts=");
                    sb5.append(downloadUrlContent.substring(matcher.start() + 6, matcher.end()));
                    str2 = sb5.toString();
                }
                else {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append((String)(Object)str);
                    sb6.append("&sts=");
                    str2 = sb6.toString();
                }
            }
            this.result[1] = "dash";
            final String[] array = { "", "&el=leanback", "&el=embedded", "&el=detailpage", "&el=vevo" };
            String s = null;
            int n = 0;
            int n2 = 0;
            String s2;
            int n3;
            while (true) {
                s2 = s;
                n3 = n2;
                if (n >= array.length) {
                    break;
                }
                final WebPlayerView this$2 = WebPlayerView.this;
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("https://www.youtube.com/get_video_info?");
                sb7.append((String)str2);
                sb7.append(array[n]);
                final String downloadUrlContent2 = this$2.downloadUrlContent(this, sb7.toString());
                if (this.isCancelled()) {
                    return null;
                }
                String s3;
                int n4;
                int n12;
                if (downloadUrlContent2 != null) {
                    final String[] split = downloadUrlContent2.split("&");
                    s3 = null;
                    n3 = n2;
                    int i = 0;
                    n4 = 0;
                    int n5 = 0;
                    String s4 = s;
                    while (i < split.length) {
                        int n6 = 0;
                        int n7 = 0;
                        String decode = null;
                        int n8 = 0;
                        String s5 = null;
                        Label_1130: {
                            if (split[i].startsWith("dashmpd")) {
                                final String[] split2 = split[i].split("=");
                                if (split2.length == 2) {
                                    try {
                                        this.result[0] = URLDecoder.decode(split2[1], "UTF-8");
                                    }
                                    catch (Exception ex2) {
                                        FileLog.e(ex2);
                                    }
                                }
                                n6 = 1;
                                n7 = n4;
                                decode = s3;
                                n8 = n3;
                                s5 = s4;
                            }
                            else {
                                if (split[i].startsWith("url_encoded_fmt_stream_map")) {
                                    final String[] split3 = split[i].split("=");
                                    n7 = n4;
                                    decode = s3;
                                    n8 = n3;
                                    n6 = n5;
                                    s5 = s4;
                                    if (split3.length != 2) {
                                        break Label_1130;
                                    }
                                    try {
                                        final String[] split4 = URLDecoder.decode(split3[1], "UTF-8").split("[&,]");
                                        int n9 = 0;
                                        int n10 = 0;
                                        String s6 = null;
                                        String decode2;
                                        while (true) {
                                            n7 = n4;
                                            decode = s3;
                                            n8 = n3;
                                            n6 = n5;
                                            s5 = s4;
                                            if (n9 >= split4.length) {
                                                break Label_1130;
                                            }
                                            final String[] split5 = split4[n9].split("=");
                                            int n11;
                                            if (split5[0].startsWith("type")) {
                                                n11 = n10;
                                                decode2 = s6;
                                                if (URLDecoder.decode(split5[1], "UTF-8").contains("video/mp4")) {
                                                    n11 = 1;
                                                    decode2 = s6;
                                                }
                                            }
                                            else if (split5[0].startsWith("url")) {
                                                decode2 = URLDecoder.decode(split5[1], "UTF-8");
                                                n11 = n10;
                                            }
                                            else {
                                                final boolean startsWith = split5[0].startsWith("itag");
                                                n11 = n10;
                                                decode2 = s6;
                                                if (startsWith) {
                                                    n11 = 0;
                                                    decode2 = null;
                                                }
                                            }
                                            if (n11 != 0 && decode2 != null) {
                                                break;
                                            }
                                            ++n9;
                                            n10 = n11;
                                            s6 = decode2;
                                        }
                                        s5 = decode2;
                                        n7 = n4;
                                        decode = s3;
                                        n8 = n3;
                                        n6 = n5;
                                        break Label_1130;
                                    }
                                    catch (Exception ex3) {
                                        FileLog.e(ex3);
                                        n7 = n4;
                                        decode = s3;
                                        n8 = n3;
                                        n6 = n5;
                                        s5 = s4;
                                        break Label_1130;
                                    }
                                }
                                if (split[i].startsWith("use_cipher_signature")) {
                                    final String[] split6 = split[i].split("=");
                                    n7 = n4;
                                    decode = s3;
                                    n8 = n3;
                                    n6 = n5;
                                    s5 = s4;
                                    if (split6.length == 2) {
                                        n7 = n4;
                                        decode = s3;
                                        n8 = n3;
                                        n6 = n5;
                                        s5 = s4;
                                        if (split6[1].toLowerCase().equals("true")) {
                                            n8 = 1;
                                            n7 = n4;
                                            decode = s3;
                                            n6 = n5;
                                            s5 = s4;
                                        }
                                    }
                                }
                                else if (split[i].startsWith("hlsvp")) {
                                    final String[] split7 = split[i].split("=");
                                    n7 = n4;
                                    decode = s3;
                                    n8 = n3;
                                    n6 = n5;
                                    s5 = s4;
                                    if (split7.length == 2) {
                                        try {
                                            decode = URLDecoder.decode(split7[1], "UTF-8");
                                            n7 = n4;
                                            n8 = n3;
                                            n6 = n5;
                                            s5 = s4;
                                        }
                                        catch (Exception ex4) {
                                            FileLog.e(ex4);
                                            n7 = n4;
                                            decode = s3;
                                            n8 = n3;
                                            n6 = n5;
                                            s5 = s4;
                                        }
                                    }
                                }
                                else {
                                    n7 = n4;
                                    decode = s3;
                                    n8 = n3;
                                    n6 = n5;
                                    s5 = s4;
                                    if (split[i].startsWith("livestream")) {
                                        final String[] split8 = split[i].split("=");
                                        n7 = n4;
                                        decode = s3;
                                        n8 = n3;
                                        n6 = n5;
                                        s5 = s4;
                                        if (split8.length == 2) {
                                            n7 = n4;
                                            decode = s3;
                                            n8 = n3;
                                            n6 = n5;
                                            s5 = s4;
                                            if (split8[1].toLowerCase().equals("1")) {
                                                n7 = 1;
                                                s5 = s4;
                                                n6 = n5;
                                                n8 = n3;
                                                decode = s3;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        ++i;
                        n4 = n7;
                        s3 = decode;
                        n3 = n8;
                        n5 = n6;
                        s4 = s5;
                    }
                    s = s4;
                    n12 = n5;
                }
                else {
                    n4 = 0;
                    s3 = null;
                    n12 = 0;
                    n3 = n2;
                }
                if (n4 != 0) {
                    if (s3 == null || n3 != 0 || s3.contains("/s/")) {
                        return null;
                    }
                    final String[] result = this.result;
                    result[0] = s3;
                    result[1] = "hls";
                }
                if (n12 != 0) {
                    s2 = s;
                    break;
                }
                ++n;
                n2 = n3;
            }
            final String[] result2 = this.result;
            if (result2[0] == null && s2 != null) {
                result2[0] = s2;
                result2[1] = "other";
            }
            final String[] result3 = this.result;
            int n13 = 0;
            Label_2129: {
                if (result3[0] != null && (n3 != 0 || result3[0].contains("/s/")) && downloadUrlContent != null) {
                    final int index = this.result[0].indexOf("/s/");
                    final int index2 = this.result[0].indexOf(47, index + 10);
                    if (index != -1) {
                        int length;
                        if ((length = index2) == -1) {
                            length = this.result[0].length();
                        }
                        this.sig = this.result[0].substring(index, length);
                        final Matcher matcher2 = WebPlayerView.jsPattern.matcher(downloadUrlContent);
                        String str3 = null;
                        Label_1439: {
                            if (matcher2.find()) {
                                try {
                                    final Object nextValue = new JSONTokener(matcher2.group(1)).nextValue();
                                    if (nextValue instanceof String) {
                                        str3 = (String)nextValue;
                                        break Label_1439;
                                    }
                                }
                                catch (Exception ex5) {
                                    FileLog.e(ex5);
                                }
                            }
                            str3 = null;
                        }
                        if (str3 != null) {
                            final Matcher matcher3 = WebPlayerView.playerIdPattern.matcher(str3);
                            String string;
                            if (matcher3.find()) {
                                final StringBuilder sb8 = new StringBuilder();
                                sb8.append(matcher3.group(1));
                                sb8.append(matcher3.group(2));
                                string = sb8.toString();
                            }
                            else {
                                string = null;
                            }
                            final Context applicationContext = ApplicationLoader.applicationContext;
                            n13 = 0;
                            final SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("youtubecode", 0);
                            String string2;
                            String s7;
                            if (string != null) {
                                string2 = sharedPreferences.getString(string, (String)null);
                                final StringBuilder sb9 = new StringBuilder();
                                sb9.append(string);
                                sb9.append("n");
                                s7 = sharedPreferences.getString(sb9.toString(), (String)null);
                            }
                            else {
                                string2 = null;
                                s7 = null;
                            }
                            String s9 = null;
                            String s10 = null;
                            Label_1928: {
                                if (string2 == null) {
                                    String s8;
                                    if (str3.startsWith("//")) {
                                        final StringBuilder sb10 = new StringBuilder();
                                        sb10.append("https:");
                                        sb10.append(str3);
                                        s8 = sb10.toString();
                                    }
                                    else {
                                        s8 = str3;
                                        if (str3.startsWith("/")) {
                                            final StringBuilder sb11 = new StringBuilder();
                                            sb11.append("https://www.youtube.com");
                                            sb11.append(str3);
                                            s8 = sb11.toString();
                                        }
                                    }
                                    final String downloadUrlContent3 = WebPlayerView.this.downloadUrlContent(this, s8);
                                    if (this.isCancelled()) {
                                        return null;
                                    }
                                    if (downloadUrlContent3 != null) {
                                        final Matcher matcher4 = WebPlayerView.sigPattern.matcher(downloadUrlContent3);
                                        if (matcher4.find()) {
                                            s7 = matcher4.group(1);
                                        }
                                        else {
                                            final Matcher matcher5 = WebPlayerView.sigPattern2.matcher(downloadUrlContent3);
                                            if (matcher5.find()) {
                                                s7 = matcher5.group(1);
                                            }
                                        }
                                        s9 = string2;
                                        if ((s10 = s7) != null) {
                                            String access$1100 = string2;
                                            try {
                                                access$1100 = string2;
                                                final JSExtractor jsExtractor = new JSExtractor(downloadUrlContent3);
                                                access$1100 = string2;
                                                final String s11 = s9 = (access$1100 = jsExtractor.extractFunction(s7));
                                                s10 = s7;
                                                if (!TextUtils.isEmpty((CharSequence)s11)) {
                                                    s9 = s11;
                                                    s10 = s7;
                                                    if (string != null) {
                                                        access$1100 = s11;
                                                        final SharedPreferences$Editor putString = sharedPreferences.edit().putString(string, s11);
                                                        access$1100 = s11;
                                                        access$1100 = s11;
                                                        final StringBuilder sb12 = new StringBuilder();
                                                        access$1100 = s11;
                                                        sb12.append(string);
                                                        access$1100 = s11;
                                                        sb12.append("n");
                                                        access$1100 = s11;
                                                        putString.putString(sb12.toString(), s7).commit();
                                                        s9 = s11;
                                                        s10 = s7;
                                                    }
                                                }
                                            }
                                            catch (Exception ex6) {
                                                FileLog.e(ex6);
                                                s9 = access$1100;
                                                s10 = s7;
                                            }
                                        }
                                        break Label_1928;
                                    }
                                }
                                s10 = s7;
                                s9 = string2;
                            }
                            if (!TextUtils.isEmpty((CharSequence)s9)) {
                                String s12;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    final StringBuilder sb13 = new StringBuilder();
                                    sb13.append(s9);
                                    sb13.append(s10);
                                    sb13.append("('");
                                    sb13.append(this.sig.substring(3));
                                    sb13.append("');");
                                    s12 = sb13.toString();
                                }
                                else {
                                    final StringBuilder sb14 = new StringBuilder();
                                    sb14.append(s9);
                                    sb14.append("window.");
                                    sb14.append(WebPlayerView.this.interfaceName);
                                    sb14.append(".returnResultToJava(");
                                    sb14.append(s10);
                                    sb14.append("('");
                                    sb14.append(this.sig.substring(3));
                                    sb14.append("'));");
                                    s12 = sb14.toString();
                                }
                                try {
                                    AndroidUtilities.runOnUIThread(new _$$Lambda$WebPlayerView$YoutubeVideoTask$GMLQkdVjUFyM84BTj7n250BC06g(this, s12));
                                    this.countDownLatch.await();
                                    break Label_2129;
                                }
                                catch (Exception ex7) {
                                    FileLog.e(ex7);
                                }
                            }
                        }
                    }
                    n13 = 1;
                }
                else {
                    n13 = n3;
                }
            }
            String[] result4;
            if (!this.isCancelled() && n13 == 0) {
                result4 = this.result;
            }
            else {
                result4 = null;
            }
            return result4;
        }
        
        protected void onPostExecute(final String[] array) {
            if (array[0] != null) {
                if (BuildVars.LOGS_ENABLED) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("start play youtube video ");
                    sb.append(array[1]);
                    sb.append(" ");
                    sb.append(array[0]);
                    FileLog.d(sb.toString());
                }
                WebPlayerView.this.initied = true;
                WebPlayerView.this.playVideoUrl = array[0];
                WebPlayerView.this.playVideoType = array[1];
                if (WebPlayerView.this.playVideoType.equals("hls")) {
                    WebPlayerView.this.isStream = true;
                }
                if (WebPlayerView.this.isAutoplay) {
                    WebPlayerView.this.preparePlayer();
                }
                WebPlayerView.this.showProgress(false, true);
                WebPlayerView.this.controlsView.show(true, true);
            }
            else if (!this.isCancelled()) {
                WebPlayerView.this.onInitFailed();
            }
        }
    }
    
    private abstract class function
    {
        public abstract Object run(final Object[] p0);
    }
}
