// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.Utilities;
import android.graphics.Paint$Style;
import android.graphics.Paint$Cap;
import android.graphics.drawable.ColorDrawable;
import android.view.View$OnApplyWindowInsetsListener;
import android.widget.FrameLayout$LayoutParams;
import android.view.View$MeasureSpec;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.FileLoader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import android.view.WindowInsets;
import android.graphics.Bitmap;
import androidx.annotation.Keep;
import android.util.SparseArray;
import android.view.WindowManager;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build$VERSION;
import org.telegram.tgnet.TLRPC;
import java.util.Collection;
import android.net.Uri;
import android.graphics.SurfaceTexture;
import org.telegram.messenger.FileLog;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import java.util.ArrayList;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import java.io.File;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.view.WindowManager$LayoutParams;
import android.view.TextureView;
import org.telegram.ui.Components.VideoPlayer;
import android.view.VelocityTracker;
import org.telegram.ui.Components.Scroller;
import android.app.Activity;
import android.view.animation.DecelerateInterpolator;
import android.view.GestureDetector;
import org.telegram.messenger.MessageObject;
import android.animation.AnimatorSet;
import org.telegram.messenger.ImageReceiver;
import android.graphics.Paint;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.annotation.SuppressLint;
import android.view.GestureDetector$OnDoubleTapListener;
import android.view.GestureDetector$OnGestureListener;
import org.telegram.messenger.NotificationCenter;

public class SecretMediaViewer implements NotificationCenterDelegate, GestureDetector$OnGestureListener, GestureDetector$OnDoubleTapListener
{
    @SuppressLint({ "StaticFieldLeak" })
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
    private Paint blackPaint;
    private boolean canDragDown;
    private ImageReceiver centerImage;
    private float clipBottom;
    private float clipHorizontal;
    private float clipTop;
    private long closeTime;
    private boolean closeVideoAfterWatch;
    private FrameLayoutDrawer containerView;
    private int[] coords;
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
    private DecelerateInterpolator interpolator;
    private boolean invalidCoords;
    private boolean isActionBarVisible;
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
    private PhotoBackgroundDrawable photoBackgroundDrawable;
    private long photoTransitionAnimationStartTime;
    private float pinchCenterX;
    private float pinchCenterY;
    private float pinchStartDistance;
    private float pinchStartScale;
    private float pinchStartX;
    private float pinchStartY;
    private int playerRetryPlayCount;
    private float scale;
    private Scroller scroller;
    private SecretDeleteTimer secretDeleteTimer;
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
    private WindowManager$LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private boolean zoomAnimation;
    private boolean zooming;
    
    public SecretMediaViewer() {
        this.centerImage = new ImageReceiver();
        this.coords = new int[2];
        this.isActionBarVisible = true;
        this.photoBackgroundDrawable = new PhotoBackgroundDrawable(-16777216);
        this.blackPaint = new Paint();
        this.scale = 1.0f;
        this.interpolator = new DecelerateInterpolator(1.5f);
        this.pinchStartScale = 1.0f;
        this.canDragDown = true;
    }
    
    private void animateTo(final float n, final float n2, final float n3, final boolean b) {
        this.animateTo(n, n2, n3, b, 250);
    }
    
    private void animateTo(final float animateToScale, final float animateToX, final float animateToY, final boolean zoomAnimation, final int n) {
        if (this.scale == animateToScale && this.translationX == animateToX && this.translationY == animateToY) {
            return;
        }
        this.zoomAnimation = zoomAnimation;
        this.animateToScale = animateToScale;
        this.animateToX = animateToX;
        this.animateToY = animateToY;
        this.animationStartTime = System.currentTimeMillis();
        (this.imageMoveAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this, "animationValue", new float[] { 0.0f, 1.0f }) });
        this.imageMoveAnimation.setInterpolator((TimeInterpolator)this.interpolator);
        this.imageMoveAnimation.setDuration((long)n);
        this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                SecretMediaViewer.this.imageMoveAnimation = null;
                SecretMediaViewer.this.containerView.invalidate();
            }
        });
        this.imageMoveAnimation.start();
    }
    
    private void checkMinMax(final boolean b) {
        float translationX = this.translationX;
        float translationY = this.translationY;
        this.updateMinMax(this.scale);
        final float translationX2 = this.translationX;
        final float minX = this.minX;
        if (translationX2 < minX) {
            translationX = minX;
        }
        else {
            final float maxX = this.maxX;
            if (translationX2 > maxX) {
                translationX = maxX;
            }
        }
        final float translationY2 = this.translationY;
        final float minY = this.minY;
        if (translationY2 < minY) {
            translationY = minY;
        }
        else {
            final float maxY = this.maxY;
            if (translationY2 > maxY) {
                translationY = maxY;
            }
        }
        this.animateTo(this.scale, translationX, translationY, b);
    }
    
    private boolean checkPhotoAnimation() {
        final int photoAnimationInProgress = this.photoAnimationInProgress;
        boolean b = false;
        if (photoAnimationInProgress != 0 && Math.abs(this.photoTransitionAnimationStartTime - System.currentTimeMillis()) >= 500L) {
            final Runnable photoAnimationEndRunnable = this.photoAnimationEndRunnable;
            if (photoAnimationEndRunnable != null) {
                photoAnimationEndRunnable.run();
                this.photoAnimationEndRunnable = null;
            }
            this.photoAnimationInProgress = 0;
        }
        if (this.photoAnimationInProgress != 0) {
            b = true;
        }
        return b;
    }
    
    private int getContainerViewHeight() {
        return this.containerView.getHeight();
    }
    
    private int getContainerViewWidth() {
        return this.containerView.getWidth();
    }
    
    public static SecretMediaViewer getInstance() {
        final SecretMediaViewer instance;
        if ((instance = SecretMediaViewer.Instance) == null) {
            synchronized (PhotoViewer.class) {
                if (SecretMediaViewer.Instance == null) {
                    SecretMediaViewer.Instance = new SecretMediaViewer();
                }
            }
        }
        return instance;
    }
    
    public static boolean hasInstance() {
        return SecretMediaViewer.Instance != null;
    }
    
    private void onDraw(final Canvas canvas) {
        if (!this.isPhotoVisible) {
            return;
        }
        final AnimatorSet imageMoveAnimation = this.imageMoveAnimation;
        final int n = 0;
        float scale2;
        float n3;
        float clipTop2;
        float clipBottom2;
        float clipHorizontal2;
        float n4;
        float translationY3;
        if (imageMoveAnimation != null) {
            if (!this.scroller.isFinished()) {
                this.scroller.abortAnimation();
            }
            if (this.useOvershootForScale) {
                final float animationValue = this.animationValue;
                float n2;
                if (animationValue < 0.9f) {
                    n2 = animationValue / 0.9f;
                    final float scale = this.scale;
                    scale2 = scale + (this.animateToScale * 1.02f - scale) * n2;
                }
                else {
                    final float animateToScale = this.animateToScale;
                    scale2 = animateToScale + 0.01999998f * animateToScale * (1.0f - (animationValue - 0.9f) / 0.100000024f);
                    n2 = 1.0f;
                }
                final float translationY = this.translationY;
                final float animateToY = this.animateToY;
                final float translationX = this.translationX;
                n3 = translationX + (this.animateToX - translationX) * n2;
                final float clipTop = this.clipTop;
                clipTop2 = clipTop + (this.animateToClipTop - clipTop) * n2;
                final float clipBottom = this.clipBottom;
                clipBottom2 = clipBottom + (this.animateToClipBottom - clipBottom) * n2;
                final float clipHorizontal = this.clipHorizontal;
                clipHorizontal2 = clipHorizontal + (this.animateToClipHorizontal - clipHorizontal) * n2;
                n4 = translationY + (animateToY - translationY) * n2;
            }
            else {
                final float scale3 = this.scale;
                final float animateToScale2 = this.animateToScale;
                final float animationValue2 = this.animationValue;
                scale2 = (animateToScale2 - scale3) * animationValue2 + scale3;
                final float translationY2 = this.translationY;
                n4 = translationY2 + (this.animateToY - translationY2) * animationValue2;
                final float translationX2 = this.translationX;
                n3 = translationX2 + (this.animateToX - translationX2) * animationValue2;
                final float clipTop3 = this.clipTop;
                clipTop2 = clipTop3 + (this.animateToClipTop - clipTop3) * animationValue2;
                final float clipBottom3 = this.clipBottom;
                clipBottom2 = clipBottom3 + (this.animateToClipBottom - clipBottom3) * animationValue2;
                final float clipHorizontal3 = this.clipHorizontal;
                clipHorizontal2 = clipHorizontal3 + (this.animateToClipHorizontal - clipHorizontal3) * animationValue2;
            }
            if (this.animateToScale == 1.0f && this.scale == 1.0f && this.translationX == 0.0f) {
                translationY3 = n4;
            }
            else {
                translationY3 = -1.0f;
            }
            this.containerView.invalidate();
        }
        else {
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
                if (this.scroller.getStartX() < this.maxX && this.scroller.getStartX() > this.minX) {
                    this.translationX = (float)this.scroller.getCurrX();
                }
                if (this.scroller.getStartY() < this.maxY && this.scroller.getStartY() > this.minY) {
                    this.translationY = (float)this.scroller.getCurrY();
                }
                this.containerView.invalidate();
            }
            scale2 = this.scale;
            translationY3 = this.translationY;
            n3 = this.translationX;
            clipTop2 = this.clipTop;
            clipBottom2 = this.clipBottom;
            clipHorizontal2 = this.clipHorizontal;
            if (!this.moving) {
                n4 = translationY3;
            }
            else {
                n4 = translationY3;
                translationY3 = -1.0f;
            }
        }
        float n5 = 0.0f;
        float alpha = 0.0f;
        Label_0835: {
            if (this.photoAnimationInProgress != 3) {
                if (this.scale == 1.0f && translationY3 != -1.0f && !this.zoomAnimation) {
                    final float b = this.getContainerViewHeight() / 4.0f;
                    this.photoBackgroundDrawable.setAlpha((int)Math.max(127.0f, (1.0f - Math.min(Math.abs(translationY3), b) / b) * 255.0f));
                }
                else {
                    this.photoBackgroundDrawable.setAlpha(255);
                }
                if (!this.zoomAnimation) {
                    final float maxX = this.maxX;
                    if (n3 > maxX) {
                        final float min = Math.min(1.0f, (n3 - maxX) / canvas.getWidth());
                        n5 = 0.3f * min;
                        alpha = 1.0f - min;
                        n3 = this.maxX;
                        break Label_0835;
                    }
                }
            }
            alpha = 1.0f;
            n5 = 0.0f;
        }
        final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        int n6 = n;
        if (aspectRatioFrameLayout != null) {
            n6 = n;
            if (aspectRatioFrameLayout.getVisibility() == 0) {
                n6 = 1;
            }
        }
        canvas.save();
        final float n7 = scale2 - n5;
        canvas.translate(this.getContainerViewWidth() / 2 + n3, this.getContainerViewHeight() / 2 + n4);
        canvas.scale(n7, n7);
        final int bitmapWidth = this.centerImage.getBitmapWidth();
        final int bitmapHeight = this.centerImage.getBitmapHeight();
        int measuredWidth = bitmapWidth;
        int measuredHeight = bitmapHeight;
        if (n6 != 0) {
            measuredWidth = bitmapWidth;
            measuredHeight = bitmapHeight;
            if (this.textureUploaded) {
                measuredWidth = bitmapWidth;
                measuredHeight = bitmapHeight;
                if (Math.abs(bitmapWidth / (float)bitmapHeight - this.videoTextureView.getMeasuredWidth() / (float)this.videoTextureView.getMeasuredHeight()) > 0.01f) {
                    measuredWidth = this.videoTextureView.getMeasuredWidth();
                    measuredHeight = this.videoTextureView.getMeasuredHeight();
                }
            }
        }
        final float n8 = (float)this.getContainerViewHeight();
        final float n9 = (float)measuredHeight;
        final float a = n8 / n9;
        final float n10 = (float)this.getContainerViewWidth();
        final float n11 = (float)measuredWidth;
        final float min2 = Math.min(a, n10 / n11);
        final int n12 = (int)(n11 * min2);
        final int n13 = (int)(n9 * min2);
        final int n14 = -n12 / 2;
        final float n15 = (float)n14;
        final float n16 = clipHorizontal2 / n7;
        final int n17 = -n13 / 2;
        final float n18 = (float)n17;
        canvas.clipRect(n15 + n16, clipTop2 / n7 + n18, n12 / 2 - n16, n13 / 2 - clipBottom2 / n7);
        if (n6 == 0 || !this.textureUploaded || !this.videoCrossfadeStarted || this.videoCrossfadeAlpha != 1.0f) {
            this.centerImage.setAlpha(alpha);
            this.centerImage.setImageCoords(n14, n17, n12, n13);
            this.centerImage.draw(canvas);
        }
        if (n6 != 0) {
            if (!this.videoCrossfadeStarted && this.textureUploaded) {
                this.videoCrossfadeStarted = true;
                this.videoCrossfadeAlpha = 0.0f;
                this.videoCrossfadeAlphaLastTime = System.currentTimeMillis();
            }
            canvas.translate(n15, n18);
            this.videoTextureView.setAlpha(alpha * this.videoCrossfadeAlpha);
            this.aspectRatioFrameLayout.draw(canvas);
            if (this.videoCrossfadeStarted && this.videoCrossfadeAlpha < 1.0f) {
                final long currentTimeMillis = System.currentTimeMillis();
                final long videoCrossfadeAlphaLastTime = this.videoCrossfadeAlphaLastTime;
                this.videoCrossfadeAlphaLastTime = currentTimeMillis;
                this.videoCrossfadeAlpha += (currentTimeMillis - videoCrossfadeAlphaLastTime) / 200.0f;
                this.containerView.invalidate();
                if (this.videoCrossfadeAlpha > 1.0f) {
                    this.videoCrossfadeAlpha = 1.0f;
                }
            }
        }
        canvas.restore();
    }
    
    private void onPhotoClosed(final PhotoViewer.PlaceProviderObject placeProviderObject) {
        this.isVisible = false;
        this.currentProvider = null;
        this.disableShowCheck = false;
        this.releasePlayer();
        new ArrayList();
        AndroidUtilities.runOnUIThread(new _$$Lambda$SecretMediaViewer$WZXZJ3O2MqtXJfvuP6KrDf_oyHw(this), 50L);
    }
    
    private void preparePlayer(final File file) {
        if (this.parentActivity == null) {
            return;
        }
        this.releasePlayer();
        if (this.videoTextureView == null) {
            (this.aspectRatioFrameLayout = new AspectRatioFrameLayout((Context)this.parentActivity)).setVisibility(4);
            this.containerView.addView((View)this.aspectRatioFrameLayout, 0, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
            (this.videoTextureView = new TextureView((Context)this.parentActivity)).setOpaque(false);
            this.aspectRatioFrameLayout.addView((View)this.videoTextureView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        }
        this.textureUploaded = false;
        this.videoCrossfadeStarted = false;
        this.videoTextureView.setAlpha(this.videoCrossfadeAlpha = 0.0f);
        if (this.videoPlayer == null) {
            (this.videoPlayer = new VideoPlayer()).setTextureView(this.videoTextureView);
            this.videoPlayer.setDelegate((VideoPlayer.VideoPlayerDelegate)new VideoPlayer.VideoPlayerDelegate() {
                @Override
                public void onError(final Exception ex) {
                    if (SecretMediaViewer.this.playerRetryPlayCount > 0) {
                        SecretMediaViewer.this.playerRetryPlayCount--;
                        AndroidUtilities.runOnUIThread(new _$$Lambda$SecretMediaViewer$1$dToY1BP1yRZuDyMam95X6X8QLzo(this, file), 100L);
                    }
                    else {
                        FileLog.e(ex);
                    }
                }
                
                @Override
                public void onRenderedFirstFrame() {
                    if (!SecretMediaViewer.this.textureUploaded) {
                        SecretMediaViewer.this.textureUploaded = true;
                        SecretMediaViewer.this.containerView.invalidate();
                    }
                }
                
                @Override
                public void onStateChanged(final boolean b, final int n) {
                    if (SecretMediaViewer.this.videoPlayer != null) {
                        if (SecretMediaViewer.this.currentMessageObject != null) {
                            if (n != 4 && n != 1) {
                                try {
                                    SecretMediaViewer.this.parentActivity.getWindow().addFlags(128);
                                }
                                catch (Exception ex) {
                                    FileLog.e(ex);
                                }
                            }
                            else {
                                try {
                                    SecretMediaViewer.this.parentActivity.getWindow().clearFlags(128);
                                }
                                catch (Exception ex2) {
                                    FileLog.e(ex2);
                                }
                            }
                            if (n == 3 && SecretMediaViewer.this.aspectRatioFrameLayout.getVisibility() != 0) {
                                SecretMediaViewer.this.aspectRatioFrameLayout.setVisibility(0);
                            }
                            if (SecretMediaViewer.this.videoPlayer.isPlaying() && n != 4) {
                                if (!SecretMediaViewer.this.isPlaying) {
                                    SecretMediaViewer.this.isPlaying = true;
                                }
                            }
                            else if (SecretMediaViewer.this.isPlaying) {
                                SecretMediaViewer.this.isPlaying = false;
                                if (n == 4) {
                                    SecretMediaViewer.this.videoWatchedOneTime = true;
                                    if (SecretMediaViewer.this.closeVideoAfterWatch) {
                                        SecretMediaViewer.this.closePhoto(true, true);
                                    }
                                    else {
                                        SecretMediaViewer.this.videoPlayer.seekTo(0L);
                                        SecretMediaViewer.this.videoPlayer.play();
                                    }
                                }
                            }
                        }
                    }
                }
                
                @Override
                public boolean onSurfaceDestroyed(final SurfaceTexture surfaceTexture) {
                    return false;
                }
                
                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
                }
                
                @Override
                public void onVideoSizeChanged(final int n, final int n2, final int n3, float n4) {
                    if (SecretMediaViewer.this.aspectRatioFrameLayout != null) {
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
                        final AspectRatioFrameLayout access$200 = SecretMediaViewer.this.aspectRatioFrameLayout;
                        if (n5 == 0) {
                            n4 = 1.0f;
                        }
                        else {
                            n4 = n6 * n4 / n5;
                        }
                        access$200.setAspectRatio(n4, n3);
                    }
                }
            });
        }
        this.videoPlayer.preparePlayer(Uri.fromFile(file), "other");
        this.videoPlayer.setPlayWhenReady(true);
    }
    
    private boolean processTouchEvent(final MotionEvent motionEvent) {
        if (this.photoAnimationInProgress == 0) {
            if (this.animationStartTime == 0L) {
                if (motionEvent.getPointerCount() == 1 && this.gestureDetector.onTouchEvent(motionEvent) && this.doubleTap) {
                    this.doubleTap = false;
                    this.moving = false;
                    this.checkMinMax(this.zooming = false);
                    return true;
                }
                if (motionEvent.getActionMasked() != 0 && motionEvent.getActionMasked() != 5) {
                    final int actionMasked = motionEvent.getActionMasked();
                    final float n = 0.0f;
                    if (actionMasked == 2) {
                        if (motionEvent.getPointerCount() == 2 && !this.draggingDown && this.zooming) {
                            this.discardTap = true;
                            this.scale = (float)Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0)) / this.pinchStartDistance * this.pinchStartScale;
                            this.translationX = this.pinchCenterX - this.getContainerViewWidth() / 2 - (this.pinchCenterX - this.getContainerViewWidth() / 2 - this.pinchStartX) * (this.scale / this.pinchStartScale);
                            final float pinchCenterY = this.pinchCenterY;
                            final float n2 = (float)(this.getContainerViewHeight() / 2);
                            final float pinchCenterY2 = this.pinchCenterY;
                            final float n3 = (float)(this.getContainerViewHeight() / 2);
                            final float pinchStartY = this.pinchStartY;
                            final float scale = this.scale;
                            this.translationY = pinchCenterY - n2 - (pinchCenterY2 - n3 - pinchStartY) * (scale / this.pinchStartScale);
                            this.updateMinMax(scale);
                            this.containerView.invalidate();
                        }
                        else if (motionEvent.getPointerCount() == 1) {
                            final VelocityTracker velocityTracker = this.velocityTracker;
                            if (velocityTracker != null) {
                                velocityTracker.addMovement(motionEvent);
                            }
                            final float abs = Math.abs(motionEvent.getX() - this.moveStartX);
                            final float abs2 = Math.abs(motionEvent.getY() - this.dragY);
                            if (abs > AndroidUtilities.dp(3.0f) || abs2 > AndroidUtilities.dp(3.0f)) {
                                this.discardTap = true;
                            }
                            if (this.canDragDown && !this.draggingDown && this.scale == 1.0f && abs2 >= AndroidUtilities.dp(30.0f) && abs2 / 2.0f > abs) {
                                this.draggingDown = true;
                                this.moving = false;
                                this.dragY = motionEvent.getY();
                                if (this.isActionBarVisible) {
                                    this.toggleActionBar(false, true);
                                }
                                return true;
                            }
                            if (this.draggingDown) {
                                this.translationY = motionEvent.getY() - this.dragY;
                                this.containerView.invalidate();
                            }
                            else if (!this.invalidCoords && this.animationStartTime == 0L) {
                                float a = this.moveStartX - motionEvent.getX();
                                float a2 = this.moveStartY - motionEvent.getY();
                                if (this.moving || (this.scale == 1.0f && Math.abs(a2) + AndroidUtilities.dp(12.0f) < Math.abs(a)) || this.scale != 1.0f) {
                                    if (!this.moving) {
                                        this.moving = true;
                                        this.canDragDown = false;
                                        a = 0.0f;
                                        a2 = 0.0f;
                                    }
                                    this.moveStartX = motionEvent.getX();
                                    this.moveStartY = motionEvent.getY();
                                    this.updateMinMax(this.scale);
                                    final float translationX = this.translationX;
                                    float n4 = 0.0f;
                                    Label_0655: {
                                        if (translationX >= this.minX) {
                                            n4 = a;
                                            if (translationX <= this.maxX) {
                                                break Label_0655;
                                            }
                                        }
                                        n4 = a / 3.0f;
                                    }
                                    final float maxY = this.maxY;
                                    Label_0762: {
                                        if (maxY == 0.0f) {
                                            final float minY = this.minY;
                                            if (minY == 0.0f) {
                                                final float translationY = this.translationY;
                                                if (translationY - a2 < minY) {
                                                    this.translationY = minY;
                                                    a2 = n;
                                                    break Label_0762;
                                                }
                                                if (translationY - a2 > maxY) {
                                                    this.translationY = maxY;
                                                    a2 = n;
                                                }
                                                break Label_0762;
                                            }
                                        }
                                        final float translationY2 = this.translationY;
                                        if (translationY2 < this.minY || translationY2 > this.maxY) {
                                            a2 /= 3.0f;
                                        }
                                    }
                                    this.translationX -= n4;
                                    if (this.scale != 1.0f) {
                                        this.translationY -= a2;
                                    }
                                    this.containerView.invalidate();
                                }
                            }
                            else {
                                this.invalidCoords = false;
                                this.moveStartX = motionEvent.getX();
                                this.moveStartY = motionEvent.getY();
                            }
                        }
                    }
                    else if (motionEvent.getActionMasked() == 3 || motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6) {
                        if (this.zooming) {
                            this.invalidCoords = true;
                            final float scale2 = this.scale;
                            if (scale2 < 1.0f) {
                                this.updateMinMax(1.0f);
                                this.animateTo(1.0f, 0.0f, 0.0f, true);
                            }
                            else if (scale2 > 3.0f) {
                                float n5 = this.pinchCenterX - this.getContainerViewWidth() / 2 - (this.pinchCenterX - this.getContainerViewWidth() / 2 - this.pinchStartX) * (3.0f / this.pinchStartScale);
                                float n6 = this.pinchCenterY - this.getContainerViewHeight() / 2 - (this.pinchCenterY - this.getContainerViewHeight() / 2 - this.pinchStartY) * (3.0f / this.pinchStartScale);
                                this.updateMinMax(3.0f);
                                final float minX = this.minX;
                                if (n5 < minX) {
                                    n5 = minX;
                                }
                                else {
                                    final float maxX = this.maxX;
                                    if (n5 > maxX) {
                                        n5 = maxX;
                                    }
                                }
                                final float minY2 = this.minY;
                                if (n6 < minY2) {
                                    n6 = minY2;
                                }
                                else {
                                    final float maxY2 = this.maxY;
                                    if (n6 > maxY2) {
                                        n6 = maxY2;
                                    }
                                }
                                this.animateTo(3.0f, n5, n6, true);
                            }
                            else {
                                this.checkMinMax(true);
                            }
                            this.zooming = false;
                        }
                        else if (this.draggingDown) {
                            if (Math.abs(this.dragY - motionEvent.getY()) > this.getContainerViewHeight() / 6.0f) {
                                this.closePhoto(true, false);
                            }
                            else {
                                this.animateTo(1.0f, 0.0f, 0.0f, false);
                            }
                            this.draggingDown = false;
                        }
                        else if (this.moving) {
                            float translationX2 = this.translationX;
                            float translationY3 = this.translationY;
                            this.updateMinMax(this.scale);
                            this.moving = false;
                            this.canDragDown = true;
                            final VelocityTracker velocityTracker2 = this.velocityTracker;
                            if (velocityTracker2 != null && this.scale == 1.0f) {
                                velocityTracker2.computeCurrentVelocity(1000);
                            }
                            final float translationX3 = this.translationX;
                            final float minX2 = this.minX;
                            if (translationX3 < minX2) {
                                translationX2 = minX2;
                            }
                            else {
                                final float maxX2 = this.maxX;
                                if (translationX3 > maxX2) {
                                    translationX2 = maxX2;
                                }
                            }
                            final float translationY4 = this.translationY;
                            final float minY3 = this.minY;
                            if (translationY4 < minY3) {
                                translationY3 = minY3;
                            }
                            else {
                                final float maxY3 = this.maxY;
                                if (translationY4 > maxY3) {
                                    translationY3 = maxY3;
                                }
                            }
                            this.animateTo(this.scale, translationX2, translationY3, false);
                        }
                    }
                }
                else {
                    this.discardTap = false;
                    if (!this.scroller.isFinished()) {
                        this.scroller.abortAnimation();
                    }
                    if (!this.draggingDown) {
                        if (motionEvent.getPointerCount() == 2) {
                            this.pinchStartDistance = (float)Math.hypot(motionEvent.getX(1) - motionEvent.getX(0), motionEvent.getY(1) - motionEvent.getY(0));
                            this.pinchStartScale = this.scale;
                            this.pinchCenterX = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
                            this.pinchCenterY = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
                            this.pinchStartX = this.translationX;
                            this.pinchStartY = this.translationY;
                            this.zooming = true;
                            this.moving = false;
                            final VelocityTracker velocityTracker3 = this.velocityTracker;
                            if (velocityTracker3 != null) {
                                velocityTracker3.clear();
                            }
                        }
                        else if (motionEvent.getPointerCount() == 1) {
                            this.moveStartX = motionEvent.getX();
                            final float y = motionEvent.getY();
                            this.moveStartY = y;
                            this.dragY = y;
                            this.draggingDown = false;
                            this.canDragDown = true;
                            final VelocityTracker velocityTracker4 = this.velocityTracker;
                            if (velocityTracker4 != null) {
                                velocityTracker4.clear();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void releasePlayer() {
        final VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            this.playerRetryPlayCount = 0;
            videoPlayer.releasePlayer(true);
            this.videoPlayer = null;
        }
        try {
            if (this.parentActivity != null) {
                this.parentActivity.getWindow().clearFlags(128);
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
        if (aspectRatioFrameLayout != null) {
            this.containerView.removeView((View)aspectRatioFrameLayout);
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
    
    private void toggleActionBar(final boolean b, final boolean b2) {
        if (b) {
            this.actionBar.setVisibility(0);
        }
        this.actionBar.setEnabled(b);
        this.isActionBarVisible = b;
        float alpha = 1.0f;
        if (b2) {
            final ArrayList<ObjectAnimator> list = new ArrayList<ObjectAnimator>();
            final ActionBar actionBar = this.actionBar;
            if (!b) {
                alpha = 0.0f;
            }
            list.add(ObjectAnimator.ofFloat((Object)actionBar, "alpha", new float[] { alpha }));
            (this.currentActionBarAnimation = new AnimatorSet()).playTogether((Collection)list);
            if (!b) {
                this.currentActionBarAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator obj) {
                        if (SecretMediaViewer.this.currentActionBarAnimation != null && SecretMediaViewer.this.currentActionBarAnimation.equals(obj)) {
                            SecretMediaViewer.this.actionBar.setVisibility(8);
                            SecretMediaViewer.this.currentActionBarAnimation = null;
                        }
                    }
                });
            }
            this.currentActionBarAnimation.setDuration(200L);
            this.currentActionBarAnimation.start();
        }
        else {
            final ActionBar actionBar2 = this.actionBar;
            if (!b) {
                alpha = 0.0f;
            }
            actionBar2.setAlpha(alpha);
            if (!b) {
                this.actionBar.setVisibility(8);
            }
        }
    }
    
    private void updateMinMax(final float n) {
        final int n2 = (int)(this.centerImage.getImageWidth() * n - this.getContainerViewWidth()) / 2;
        final int n3 = (int)(this.centerImage.getImageHeight() * n - this.getContainerViewHeight()) / 2;
        if (n2 > 0) {
            this.minX = (float)(-n2);
            this.maxX = (float)n2;
        }
        else {
            this.maxX = 0.0f;
            this.minX = 0.0f;
        }
        if (n3 > 0) {
            this.minY = (float)(-n3);
            this.maxY = (float)n3;
        }
        else {
            this.maxY = 0.0f;
            this.minY = 0.0f;
        }
    }
    
    public void closePhoto(final boolean b, final boolean b2) {
        if (this.parentActivity != null && this.isPhotoVisible) {
            if (!this.checkPhotoAnimation()) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
                this.isActionBarVisible = false;
                final VelocityTracker velocityTracker = this.velocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                this.closeTime = System.currentTimeMillis();
                final PhotoViewer.PhotoViewerProvider currentProvider = this.currentProvider;
                PhotoViewer.PlaceProviderObject placeForPhoto = null;
                Label_0163: {
                    if (currentProvider != null) {
                        final MessageObject currentMessageObject = this.currentMessageObject;
                        final TLRPC.MessageMedia media = currentMessageObject.messageOwner.media;
                        if (!(media.photo instanceof TLRPC.TL_photoEmpty)) {
                            if (!(media.document instanceof TLRPC.TL_documentEmpty)) {
                                placeForPhoto = currentProvider.getPlaceForPhoto(currentMessageObject, null, 0, true);
                                break Label_0163;
                            }
                        }
                    }
                    placeForPhoto = null;
                }
                final VideoPlayer videoPlayer = this.videoPlayer;
                if (videoPlayer != null) {
                    videoPlayer.pause();
                }
                if (b) {
                    this.photoAnimationInProgress = 3;
                    this.containerView.invalidate();
                    this.imageMoveAnimation = new AnimatorSet();
                    if (placeForPhoto != null && placeForPhoto.imageReceiver.getThumbBitmap() != null && !b2) {
                        placeForPhoto.imageReceiver.setVisible(false, true);
                        final RectF drawRegion = placeForPhoto.imageReceiver.getDrawRegion();
                        final float n = drawRegion.right - drawRegion.left;
                        final float n2 = drawRegion.bottom - drawRegion.top;
                        final Point displaySize = AndroidUtilities.displaySize;
                        final int x = displaySize.x;
                        final int y = displaySize.y;
                        int statusBarHeight;
                        if (Build$VERSION.SDK_INT >= 21) {
                            statusBarHeight = AndroidUtilities.statusBarHeight;
                        }
                        else {
                            statusBarHeight = 0;
                        }
                        final int n3 = y + statusBarHeight;
                        this.animateToScale = Math.max(n / x, n2 / n3);
                        final float n4 = (float)placeForPhoto.viewX;
                        final float left = drawRegion.left;
                        this.animateToX = n4 + left + n / 2.0f - x / 2;
                        this.animateToY = placeForPhoto.viewY + drawRegion.top + n2 / 2.0f - n3 / 2;
                        this.animateToClipHorizontal = Math.abs(left - placeForPhoto.imageReceiver.getImageX());
                        final int n5 = (int)Math.abs(drawRegion.top - placeForPhoto.imageReceiver.getImageY());
                        final int[] array = new int[2];
                        placeForPhoto.parentView.getLocationInWindow(array);
                        final int n6 = array[1];
                        int statusBarHeight2;
                        if (Build$VERSION.SDK_INT >= 21) {
                            statusBarHeight2 = 0;
                        }
                        else {
                            statusBarHeight2 = AndroidUtilities.statusBarHeight;
                        }
                        this.animateToClipTop = n6 - statusBarHeight2 - (placeForPhoto.viewY + drawRegion.top) + placeForPhoto.clipTopAddition;
                        if (this.animateToClipTop < 0.0f) {
                            this.animateToClipTop = 0.0f;
                        }
                        final float n7 = (float)placeForPhoto.viewY;
                        final float top = drawRegion.top;
                        final float n8 = (float)(int)n2;
                        final int n9 = array[1];
                        final int height = placeForPhoto.parentView.getHeight();
                        int statusBarHeight3;
                        if (Build$VERSION.SDK_INT >= 21) {
                            statusBarHeight3 = 0;
                        }
                        else {
                            statusBarHeight3 = AndroidUtilities.statusBarHeight;
                        }
                        this.animateToClipBottom = n7 + top + n8 - (n9 + height - statusBarHeight3) + placeForPhoto.clipBottomAddition;
                        if (this.animateToClipBottom < 0.0f) {
                            this.animateToClipBottom = 0.0f;
                        }
                        this.animationStartTime = System.currentTimeMillis();
                        final float animateToClipBottom = this.animateToClipBottom;
                        final float n10 = (float)n5;
                        this.animateToClipBottom = Math.max(animateToClipBottom, n10);
                        this.animateToClipTop = Math.max(this.animateToClipTop, n10);
                        this.zoomAnimation = true;
                    }
                    else {
                        final int y2 = AndroidUtilities.displaySize.y;
                        int statusBarHeight4;
                        if (Build$VERSION.SDK_INT >= 21) {
                            statusBarHeight4 = AndroidUtilities.statusBarHeight;
                        }
                        else {
                            statusBarHeight4 = 0;
                        }
                        int n11 = y2 + statusBarHeight4;
                        if (this.translationY < 0.0f) {
                            n11 = -n11;
                        }
                        this.animateToY = (float)n11;
                    }
                    if (this.isVideo) {
                        this.videoCrossfadeStarted = false;
                        this.textureUploaded = false;
                        this.imageMoveAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofInt((Object)this.photoBackgroundDrawable, "alpha", new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)this, "animationValue", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.secretDeleteTimer, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, "videoCrossfadeAlpha", new float[] { 0.0f }) });
                    }
                    else {
                        this.centerImage.setManualAlphaAnimator(true);
                        this.imageMoveAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofInt((Object)this.photoBackgroundDrawable, "alpha", new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)this, "animationValue", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.secretDeleteTimer, "alpha", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.centerImage, "currentAlpha", new float[] { 0.0f }) });
                    }
                    this.photoAnimationEndRunnable = new _$$Lambda$SecretMediaViewer$FsmwJiUXxyTDqbCVCkGBtpTvcxw(this, placeForPhoto);
                    this.imageMoveAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                    this.imageMoveAnimation.setDuration(250L);
                    this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            final PhotoViewer.PlaceProviderObject val$object = placeForPhoto;
                            if (val$object != null) {
                                val$object.imageReceiver.setVisible(true, true);
                            }
                            SecretMediaViewer.this.isVisible = false;
                            AndroidUtilities.runOnUIThread(new _$$Lambda$SecretMediaViewer$7$T5mvUOvsURLsFjHY_qOF1JNvIrM(this));
                        }
                    });
                    this.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    if (Build$VERSION.SDK_INT >= 18) {
                        this.containerView.setLayerType(2, (Paint)null);
                    }
                    this.imageMoveAnimation.start();
                }
                else {
                    final AnimatorSet set = new AnimatorSet();
                    set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.containerView, "scaleX", new float[] { 0.9f }), (Animator)ObjectAnimator.ofFloat((Object)this.containerView, "scaleY", new float[] { 0.9f }), (Animator)ObjectAnimator.ofInt((Object)this.photoBackgroundDrawable, "alpha", new int[] { 0 }), (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, "alpha", new float[] { 0.0f }) });
                    this.photoAnimationInProgress = 2;
                    this.photoAnimationEndRunnable = new _$$Lambda$SecretMediaViewer$SXGR0dGO1jG_N03wXFFoPW86sNo(this, placeForPhoto);
                    set.setDuration(200L);
                    set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                                SecretMediaViewer.this.photoAnimationEndRunnable.run();
                                SecretMediaViewer.this.photoAnimationEndRunnable = null;
                            }
                        }
                    });
                    this.photoTransitionAnimationStartTime = System.currentTimeMillis();
                    if (Build$VERSION.SDK_INT >= 18) {
                        this.containerView.setLayerType(2, (Paint)null);
                    }
                    set.start();
                }
            }
        }
    }
    
    public void destroyPhotoViewer() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
        this.isVisible = false;
        this.currentProvider = null;
        final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
        if (currentThumb != null) {
            currentThumb.release();
            this.currentThumb = null;
        }
        this.releasePlayer();
        if (this.parentActivity != null) {
            final FrameLayout windowView = this.windowView;
            if (windowView != null) {
                try {
                    if (windowView.getParent() != null) {
                        ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate((View)this.windowView);
                    }
                    this.windowView = null;
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
        }
        SecretMediaViewer.Instance = null;
    }
    
    @Override
    public void didReceivedNotification(int i, int j, final Object... array) {
        if (i == NotificationCenter.messagesDeleted) {
            if (this.currentMessageObject == null) {
                return;
            }
            if ((int)array[1] != 0) {
                return;
            }
            if (((ArrayList)array[0]).contains(this.currentMessageObject.getId())) {
                if (this.isVideo && !this.videoWatchedOneTime) {
                    this.closeVideoAfterWatch = true;
                }
                else {
                    this.closePhoto(true, true);
                }
            }
        }
        else if (i == NotificationCenter.didCreatedNewDeleteTask) {
            if (this.currentMessageObject == null || this.secretDeleteTimer == null) {
                return;
            }
            SparseArray sparseArray;
            int key;
            ArrayList list;
            long longValue;
            int n;
            for (sparseArray = (SparseArray)array[0], i = 0; i < sparseArray.size(); ++i) {
                key = sparseArray.keyAt(i);
                for (list = (ArrayList)sparseArray.get(key), j = 0; j < list.size(); ++j) {
                    longValue = list.get(j);
                    if (j == 0) {
                        if ((n = (int)(longValue >> 32)) < 0) {
                            n = 0;
                        }
                        if (n != this.currentChannelId) {
                            return;
                        }
                    }
                    if (this.currentMessageObject.getId() == longValue) {
                        this.currentMessageObject.messageOwner.destroyTime = key;
                        this.secretDeleteTimer.invalidate();
                        return;
                    }
                }
            }
        }
        else if (i == NotificationCenter.updateMessageMedia && this.currentMessageObject.getId() == ((TLRPC.Message)array[0]).id) {
            if (this.isVideo && !this.videoWatchedOneTime) {
                this.closeVideoAfterWatch = true;
            }
            else {
                this.closePhoto(true, true);
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
    
    public boolean isShowingImage(final MessageObject messageObject) {
        if (this.isVisible && !this.disableShowCheck && messageObject != null) {
            final MessageObject currentMessageObject = this.currentMessageObject;
            if (currentMessageObject != null && currentMessageObject.getId() == messageObject.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isVisible() {
        return this.isVisible;
    }
    
    public boolean onDoubleTap(final MotionEvent motionEvent) {
        final float scale = this.scale;
        final boolean b = false;
        if (scale == 1.0f && (this.translationY != 0.0f || this.translationX != 0.0f)) {
            return false;
        }
        boolean b2 = b;
        if (this.animationStartTime == 0L) {
            if (this.photoAnimationInProgress != 0) {
                b2 = b;
            }
            else {
                final float scale2 = this.scale;
                b2 = true;
                if (scale2 == 1.0f) {
                    float n = motionEvent.getX() - this.getContainerViewWidth() / 2 - (motionEvent.getX() - this.getContainerViewWidth() / 2 - this.translationX) * (3.0f / this.scale);
                    float n2 = motionEvent.getY() - this.getContainerViewHeight() / 2 - (motionEvent.getY() - this.getContainerViewHeight() / 2 - this.translationY) * (3.0f / this.scale);
                    this.updateMinMax(3.0f);
                    final float minX = this.minX;
                    if (n < minX) {
                        n = minX;
                    }
                    else {
                        final float maxX = this.maxX;
                        if (n > maxX) {
                            n = maxX;
                        }
                    }
                    final float minY = this.minY;
                    if (n2 < minY) {
                        n2 = minY;
                    }
                    else {
                        final float maxY = this.maxY;
                        if (n2 > maxY) {
                            n2 = maxY;
                        }
                    }
                    this.animateTo(3.0f, n, n2, true);
                }
                else {
                    this.animateTo(1.0f, 0.0f, 0.0f, true);
                }
                this.doubleTap = true;
            }
        }
        return b2;
    }
    
    public boolean onDoubleTapEvent(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean onDown(final MotionEvent motionEvent) {
        return false;
    }
    
    public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float a, final float a2) {
        if (this.scale != 1.0f) {
            this.scroller.abortAnimation();
            this.scroller.fling(Math.round(this.translationX), Math.round(this.translationY), Math.round(a), Math.round(a2), (int)this.minX, (int)this.maxX, (int)this.minY, (int)this.maxY);
            this.containerView.postInvalidate();
        }
        return false;
    }
    
    public void onLongPress(final MotionEvent motionEvent) {
    }
    
    public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
        return false;
    }
    
    public void onShowPress(final MotionEvent motionEvent) {
    }
    
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
        if (this.discardTap) {
            return false;
        }
        this.toggleActionBar(this.isActionBarVisible ^ true, true);
        return true;
    }
    
    public boolean onSingleTapUp(final MotionEvent motionEvent) {
        return false;
    }
    
    public void openMedia(final MessageObject currentMessageObject, final PhotoViewer.PhotoViewerProvider currentProvider) {
        if (this.parentActivity != null && currentMessageObject != null && currentMessageObject.needDrawBluredPreview()) {
            if (currentProvider != null) {
                final PhotoViewer.PlaceProviderObject placeForPhoto = currentProvider.getPlaceForPhoto(currentMessageObject, null, 0, true);
                if (placeForPhoto == null) {
                    return;
                }
                this.currentProvider = currentProvider;
                this.openTime = System.currentTimeMillis();
                this.closeTime = 0L;
                this.isActionBarVisible = true;
                this.isPhotoVisible = true;
                this.draggingDown = false;
                final AspectRatioFrameLayout aspectRatioFrameLayout = this.aspectRatioFrameLayout;
                if (aspectRatioFrameLayout != null) {
                    aspectRatioFrameLayout.setVisibility(4);
                }
                this.releasePlayer();
                this.pinchStartDistance = 0.0f;
                this.pinchStartScale = 1.0f;
                this.pinchCenterX = 0.0f;
                this.pinchCenterY = 0.0f;
                this.pinchStartX = 0.0f;
                this.pinchStartY = 0.0f;
                this.moveStartX = 0.0f;
                this.moveStartY = 0.0f;
                this.zooming = false;
                this.moving = false;
                this.doubleTap = false;
                this.invalidCoords = false;
                this.canDragDown = true;
                this.updateMinMax(this.scale);
                this.photoBackgroundDrawable.setAlpha(0);
                this.containerView.setAlpha(1.0f);
                this.containerView.setVisibility(0);
                this.secretDeleteTimer.setAlpha(1.0f);
                this.isVideo = false;
                this.videoWatchedOneTime = false;
                this.closeVideoAfterWatch = false;
                this.disableShowCheck = true;
                this.centerImage.setManualAlphaAnimator(false);
                final RectF drawRegion = placeForPhoto.imageReceiver.getDrawRegion();
                final float width = drawRegion.width();
                final float height = drawRegion.height();
                final Point displaySize = AndroidUtilities.displaySize;
                final int x = displaySize.x;
                final int y = displaySize.y;
                int statusBarHeight;
                if (Build$VERSION.SDK_INT >= 21) {
                    statusBarHeight = AndroidUtilities.statusBarHeight;
                }
                else {
                    statusBarHeight = 0;
                }
                final int n = y + statusBarHeight;
                this.scale = Math.max(width / x, height / n);
                final float n2 = (float)placeForPhoto.viewX;
                final float left = drawRegion.left;
                this.translationX = n2 + left + width / 2.0f - x / 2;
                this.translationY = placeForPhoto.viewY + drawRegion.top + height / 2.0f - n / 2;
                this.clipHorizontal = Math.abs(left - placeForPhoto.imageReceiver.getImageX());
                final int n3 = (int)Math.abs(drawRegion.top - placeForPhoto.imageReceiver.getImageY());
                final int[] array = new int[2];
                placeForPhoto.parentView.getLocationInWindow(array);
                final int n4 = array[1];
                int statusBarHeight2;
                if (Build$VERSION.SDK_INT >= 21) {
                    statusBarHeight2 = 0;
                }
                else {
                    statusBarHeight2 = AndroidUtilities.statusBarHeight;
                }
                this.clipTop = n4 - statusBarHeight2 - (placeForPhoto.viewY + drawRegion.top) + placeForPhoto.clipTopAddition;
                if (this.clipTop < 0.0f) {
                    this.clipTop = 0.0f;
                }
                final float n5 = (float)placeForPhoto.viewY;
                final float top = drawRegion.top;
                final float n6 = (float)(int)height;
                final int n7 = array[1];
                final int height2 = placeForPhoto.parentView.getHeight();
                int statusBarHeight3;
                if (Build$VERSION.SDK_INT >= 21) {
                    statusBarHeight3 = 0;
                }
                else {
                    statusBarHeight3 = AndroidUtilities.statusBarHeight;
                }
                this.clipBottom = n5 + top + n6 - (n7 + height2 - statusBarHeight3) + placeForPhoto.clipBottomAddition;
                if (this.clipBottom < 0.0f) {
                    this.clipBottom = 0.0f;
                }
                final float clipTop = this.clipTop;
                final float n8 = (float)n3;
                this.clipTop = Math.max(clipTop, n8);
                this.clipBottom = Math.max(this.clipBottom, n8);
                this.animationStartTime = System.currentTimeMillis();
                this.animateToX = 0.0f;
                this.animateToY = 0.0f;
                this.animateToClipBottom = 0.0f;
                this.animateToClipHorizontal = 0.0f;
                this.animateToClipTop = 0.0f;
                this.animateToScale = 1.0f;
                this.zoomAnimation = true;
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateMessageMedia);
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didCreatedNewDeleteTask);
                final TLRPC.Peer to_id = currentMessageObject.messageOwner.to_id;
                int channel_id;
                if (to_id != null) {
                    channel_id = to_id.channel_id;
                }
                else {
                    channel_id = 0;
                }
                this.currentChannelId = channel_id;
                this.toggleActionBar(true, false);
                this.currentMessageObject = currentMessageObject;
                final TLRPC.Document document = currentMessageObject.getDocument();
                final ImageReceiver.BitmapHolder currentThumb = this.currentThumb;
                if (currentThumb != null) {
                    currentThumb.release();
                    this.currentThumb = null;
                }
                this.currentThumb = placeForPhoto.imageReceiver.getThumbBitmapSafe();
                if (document != null) {
                    if (MessageObject.isGifDocument(document)) {
                        this.actionBar.setTitle(LocaleController.getString("DisappearingGif", 2131559270));
                        final ImageReceiver centerImage = this.centerImage;
                        final ImageLocation forDocument = ImageLocation.getForDocument(document);
                        final ImageReceiver.BitmapHolder currentThumb2 = this.currentThumb;
                        Object o;
                        if (currentThumb2 != null) {
                            o = new BitmapDrawable(currentThumb2.bitmap);
                        }
                        else {
                            o = null;
                        }
                        centerImage.setImage(forDocument, null, (Drawable)o, -1, null, currentMessageObject, 1);
                        final SecretDeleteTimer secretDeleteTimer = this.secretDeleteTimer;
                        final TLRPC.Message messageOwner = currentMessageObject.messageOwner;
                        secretDeleteTimer.setDestroyTime(messageOwner.destroyTime * 1000L, messageOwner.ttl, false);
                    }
                    else {
                        this.playerRetryPlayCount = 1;
                        this.actionBar.setTitle(LocaleController.getString("DisappearingVideo", 2131559272));
                        final File file = new File(currentMessageObject.messageOwner.attachPath);
                        if (file.exists()) {
                            this.preparePlayer(file);
                        }
                        else {
                            File pathToMessage = FileLoader.getPathToMessage(currentMessageObject.messageOwner);
                            final StringBuilder sb = new StringBuilder();
                            sb.append(pathToMessage.getAbsolutePath());
                            sb.append(".enc");
                            final File file2 = new File(sb.toString());
                            if (file2.exists()) {
                                pathToMessage = file2;
                            }
                            this.preparePlayer(pathToMessage);
                        }
                        this.isVideo = true;
                        final ImageReceiver centerImage2 = this.centerImage;
                        final ImageReceiver.BitmapHolder currentThumb3 = this.currentThumb;
                        Object o2;
                        if (currentThumb3 != null) {
                            o2 = new BitmapDrawable(currentThumb3.bitmap);
                        }
                        else {
                            o2 = null;
                        }
                        centerImage2.setImage(null, null, (Drawable)o2, -1, null, currentMessageObject, 2);
                        if (currentMessageObject.getDuration() * 1000 > currentMessageObject.messageOwner.destroyTime * 1000L - (System.currentTimeMillis() + ConnectionsManager.getInstance(this.currentAccount).getTimeDifference() * 1000)) {
                            this.secretDeleteTimer.setDestroyTime(-1L, -1L, true);
                        }
                        else {
                            final SecretDeleteTimer secretDeleteTimer2 = this.secretDeleteTimer;
                            final TLRPC.Message messageOwner2 = currentMessageObject.messageOwner;
                            secretDeleteTimer2.setDestroyTime(messageOwner2.destroyTime * 1000L, messageOwner2.ttl, false);
                        }
                    }
                }
                else {
                    this.actionBar.setTitle(LocaleController.getString("DisappearingPhoto", 2131559271));
                    final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(currentMessageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                    final ImageReceiver centerImage3 = this.centerImage;
                    final ImageLocation forObject = ImageLocation.getForObject(closestPhotoSizeWithSize, currentMessageObject.photoThumbsObject);
                    final ImageReceiver.BitmapHolder currentThumb4 = this.currentThumb;
                    Object o3;
                    if (currentThumb4 != null) {
                        o3 = new BitmapDrawable(currentThumb4.bitmap);
                    }
                    else {
                        o3 = null;
                    }
                    centerImage3.setImage(forObject, null, (Drawable)o3, -1, null, currentMessageObject, 2);
                    final SecretDeleteTimer secretDeleteTimer3 = this.secretDeleteTimer;
                    final TLRPC.Message messageOwner3 = currentMessageObject.messageOwner;
                    secretDeleteTimer3.setDestroyTime(messageOwner3.destroyTime * 1000L, messageOwner3.ttl, false);
                }
                try {
                    if (this.windowView.getParent() != null) {
                        ((WindowManager)this.parentActivity.getSystemService("window")).removeView((View)this.windowView);
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                ((WindowManager)this.parentActivity.getSystemService("window")).addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
                this.secretDeleteTimer.invalidate();
                this.isVisible = true;
                (this.imageMoveAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.actionBar, "alpha", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.secretDeleteTimer, "alpha", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofInt((Object)this.photoBackgroundDrawable, "alpha", new int[] { 0, 255 }), (Animator)ObjectAnimator.ofFloat((Object)this.secretDeleteTimer, "alpha", new float[] { 0.0f, 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this, "animationValue", new float[] { 0.0f, 1.0f }) });
                this.photoAnimationInProgress = 3;
                this.photoAnimationEndRunnable = new _$$Lambda$SecretMediaViewer$KK7Q_lwTroM_vlzg7v5qjBIm368(this);
                this.imageMoveAnimation.setDuration(250L);
                this.imageMoveAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (SecretMediaViewer.this.photoAnimationEndRunnable != null) {
                            SecretMediaViewer.this.photoAnimationEndRunnable.run();
                            SecretMediaViewer.this.photoAnimationEndRunnable = null;
                        }
                    }
                });
                this.photoTransitionAnimationStartTime = System.currentTimeMillis();
                if (Build$VERSION.SDK_INT >= 18) {
                    this.containerView.setLayerType(2, (Paint)null);
                }
                this.imageMoveAnimation.setInterpolator((TimeInterpolator)new DecelerateInterpolator());
                this.photoBackgroundDrawable.frame = 0;
                this.photoBackgroundDrawable.drawRunnable = new _$$Lambda$SecretMediaViewer$_WXiS_iPl6V2WQvBv2qBotC4RRs(this, placeForPhoto);
                this.imageMoveAnimation.start();
            }
        }
    }
    
    @Keep
    public void setAnimationValue(final float animationValue) {
        this.animationValue = animationValue;
        this.containerView.invalidate();
    }
    
    public void setParentActivity(final Activity parentActivity) {
        this.currentAccount = UserConfig.selectedAccount;
        this.centerImage.setCurrentAccount(this.currentAccount);
        if (this.parentActivity == parentActivity) {
            return;
        }
        this.parentActivity = parentActivity;
        this.scroller = new Scroller((Context)parentActivity);
        (this.windowView = new FrameLayout(parentActivity) {
            protected void onLayout(final boolean b, int n, final int n2, final int n3, final int n4) {
                if (Build$VERSION.SDK_INT >= 21 && SecretMediaViewer.this.lastInsets != null) {
                    n = ((WindowInsets)SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft() + 0;
                }
                else {
                    n = 0;
                }
                SecretMediaViewer.this.containerView.layout(n, 0, SecretMediaViewer.this.containerView.getMeasuredWidth() + n, SecretMediaViewer.this.containerView.getMeasuredHeight());
                if (b) {
                    if (SecretMediaViewer.this.imageMoveAnimation == null) {
                        SecretMediaViewer.this.scale = 1.0f;
                        SecretMediaViewer.this.translationX = 0.0f;
                        SecretMediaViewer.this.translationY = 0.0f;
                    }
                    final SecretMediaViewer this$0 = SecretMediaViewer.this;
                    this$0.updateMinMax(this$0.scale);
                }
            }
            
            protected void onMeasure(int size, int n) {
                final int size2 = View$MeasureSpec.getSize(size);
                size = View$MeasureSpec.getSize(n);
                int n2;
                if (Build$VERSION.SDK_INT >= 21 && SecretMediaViewer.this.lastInsets != null) {
                    final WindowInsets windowInsets = (WindowInsets)SecretMediaViewer.this.lastInsets;
                    n = size;
                    if (AndroidUtilities.incorrectDisplaySizeFix) {
                        final int y = AndroidUtilities.displaySize.y;
                        if ((n = size) > y) {
                            n = y;
                        }
                        n += AndroidUtilities.statusBarHeight;
                    }
                    n -= windowInsets.getSystemWindowInsetBottom();
                    n2 = size2 - windowInsets.getSystemWindowInsetRight();
                }
                else {
                    final int y2 = AndroidUtilities.displaySize.y;
                    n2 = size2;
                    if ((n = size) > y2) {
                        n = y2;
                        n2 = size2;
                    }
                }
                this.setMeasuredDimension(n2, n);
                size = n2;
                if (Build$VERSION.SDK_INT >= 21) {
                    size = n2;
                    if (SecretMediaViewer.this.lastInsets != null) {
                        size = n2 - ((WindowInsets)SecretMediaViewer.this.lastInsets).getSystemWindowInsetLeft();
                    }
                }
                SecretMediaViewer.this.containerView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
            }
        }).setBackgroundDrawable((Drawable)this.photoBackgroundDrawable);
        this.windowView.setFocusable(true);
        this.windowView.setFocusableInTouchMode(true);
        (this.containerView = (FrameLayoutDrawer)new FrameLayoutDrawer(parentActivity) {
            protected void onLayout(final boolean b, int statusBarHeight, int n, final int n2, final int n3) {
                super.onLayout(b, statusBarHeight, n, n2, n3);
                if (SecretMediaViewer.this.secretDeleteTimer != null) {
                    n = (ActionBar.getCurrentActionBarHeight() - SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight()) / 2;
                    if (Build$VERSION.SDK_INT >= 21) {
                        statusBarHeight = AndroidUtilities.statusBarHeight;
                    }
                    else {
                        statusBarHeight = 0;
                    }
                    statusBarHeight += n;
                    SecretMediaViewer.this.secretDeleteTimer.layout(SecretMediaViewer.this.secretDeleteTimer.getLeft(), statusBarHeight, SecretMediaViewer.this.secretDeleteTimer.getRight(), SecretMediaViewer.this.secretDeleteTimer.getMeasuredHeight() + statusBarHeight);
                }
            }
        }).setFocusable(false);
        this.windowView.addView((View)this.containerView);
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.containerView.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 51;
        this.containerView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        if (Build$VERSION.SDK_INT >= 21) {
            this.containerView.setFitsSystemWindows(true);
            this.containerView.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new _$$Lambda$SecretMediaViewer$NFiT5K5Ywrc0uS0Pq28kGtaBHeE(this));
            this.containerView.setSystemUiVisibility(1280);
        }
        (this.gestureDetector = new GestureDetector(this.containerView.getContext(), (GestureDetector$OnGestureListener)this)).setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)this);
        (this.actionBar = new ActionBar((Context)parentActivity)).setTitleColor(-1);
        this.actionBar.setSubtitleColor(-1);
        this.actionBar.setBackgroundColor(2130706432);
        this.actionBar.setOccupyStatusBar(Build$VERSION.SDK_INT >= 21);
        this.actionBar.setItemsBackgroundColor(1090519039, false);
        this.actionBar.setBackButtonImage(2131165409);
        this.actionBar.setTitleRightMargin(AndroidUtilities.dp(70.0f));
        this.containerView.addView((View)this.actionBar, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        this.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    SecretMediaViewer.this.closePhoto(true, false);
                }
            }
        });
        this.secretDeleteTimer = new SecretDeleteTimer((Context)parentActivity);
        this.containerView.addView((View)this.secretDeleteTimer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(119, 48.0f, 53, 0.0f, 0.0f, 0.0f, 0.0f));
        this.windowLayoutParams = new WindowManager$LayoutParams();
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.height = -1;
        windowLayoutParams.format = -3;
        windowLayoutParams.width = -1;
        windowLayoutParams.gravity = 48;
        windowLayoutParams.type = 99;
        if (Build$VERSION.SDK_INT >= 21) {
            windowLayoutParams.flags = -2147417848;
        }
        else {
            windowLayoutParams.flags = 8;
        }
        final WindowManager$LayoutParams windowLayoutParams2 = this.windowLayoutParams;
        windowLayoutParams2.flags |= 0x2000;
        this.centerImage.setParentView((View)this.containerView);
        this.centerImage.setForceCrossfade(true);
    }
    
    @Keep
    public void setVideoCrossfadeAlpha(final float videoCrossfadeAlpha) {
        this.videoCrossfadeAlpha = videoCrossfadeAlpha;
        this.containerView.invalidate();
    }
    
    private class FrameLayoutDrawer extends FrameLayout
    {
        public FrameLayoutDrawer(final Context context) {
            super(context);
            this.setWillNotDraw(false);
        }
        
        protected boolean drawChild(final Canvas canvas, final View view, final long n) {
            return view != SecretMediaViewer.this.aspectRatioFrameLayout && super.drawChild(canvas, view, n);
        }
        
        protected void onDraw(final Canvas canvas) {
            SecretMediaViewer.this.onDraw(canvas);
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            SecretMediaViewer.this.processTouchEvent(motionEvent);
            return true;
        }
    }
    
    private class PhotoBackgroundDrawable extends ColorDrawable
    {
        private Runnable drawRunnable;
        private int frame;
        
        public PhotoBackgroundDrawable(final int n) {
            super(n);
        }
        
        public void draw(final Canvas canvas) {
            super.draw(canvas);
            if (this.getAlpha() != 0) {
                Label_0047: {
                    if (this.frame == 2) {
                        final Runnable drawRunnable = this.drawRunnable;
                        if (drawRunnable != null) {
                            drawRunnable.run();
                            this.drawRunnable = null;
                            break Label_0047;
                        }
                    }
                    this.invalidateSelf();
                }
                ++this.frame;
            }
        }
        
        @Keep
        public void setAlpha(final int alpha) {
            if (SecretMediaViewer.this.parentActivity instanceof LaunchActivity) {
                ((LaunchActivity)SecretMediaViewer.this.parentActivity).drawerLayoutContainer.setAllowDrawContent(!SecretMediaViewer.this.isPhotoVisible || alpha != 255);
            }
            super.setAlpha(alpha);
        }
    }
    
    private class SecretDeleteTimer extends FrameLayout
    {
        private Paint afterDeleteProgressPaint;
        private Paint circlePaint;
        private Paint deleteProgressPaint;
        private RectF deleteProgressRect;
        private long destroyTime;
        private long destroyTtl;
        private Drawable drawable;
        private ArrayList<Particle> freeParticles;
        private long lastAnimationTime;
        private Paint particlePaint;
        private ArrayList<Particle> particles;
        private boolean useVideoProgress;
        
        public SecretDeleteTimer(final Context context) {
            super(context);
            this.deleteProgressRect = new RectF();
            this.particles = new ArrayList<Particle>();
            this.freeParticles = new ArrayList<Particle>();
            int i = 0;
            this.setWillNotDraw(false);
            (this.particlePaint = new Paint(1)).setStrokeWidth((float)AndroidUtilities.dp(1.5f));
            this.particlePaint.setColor(-1644826);
            this.particlePaint.setStrokeCap(Paint$Cap.ROUND);
            this.particlePaint.setStyle(Paint$Style.STROKE);
            (this.deleteProgressPaint = new Paint(1)).setColor(-1644826);
            (this.afterDeleteProgressPaint = new Paint(1)).setStyle(Paint$Style.STROKE);
            this.afterDeleteProgressPaint.setStrokeCap(Paint$Cap.ROUND);
            this.afterDeleteProgressPaint.setColor(-1644826);
            this.afterDeleteProgressPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0f));
            (this.circlePaint = new Paint(1)).setColor(2130706432);
            this.drawable = context.getResources().getDrawable(2131165379);
            while (i < 40) {
                this.freeParticles.add(new Particle());
                ++i;
            }
        }
        
        private void setDestroyTime(final long destroyTime, final long destroyTtl, final boolean useVideoProgress) {
            this.destroyTime = destroyTime;
            this.destroyTtl = destroyTtl;
            this.useVideoProgress = useVideoProgress;
            this.lastAnimationTime = System.currentTimeMillis();
            this.invalidate();
        }
        
        private void updateParticles(final long n) {
            for (int size = this.particles.size(), i = 0; i < size; ++i) {
                final Particle e = this.particles.get(i);
                final float currentTime = e.currentTime;
                final float lifeTime = e.lifeTime;
                if (currentTime >= lifeTime) {
                    if (this.freeParticles.size() < 40) {
                        this.freeParticles.add(e);
                    }
                    this.particles.remove(i);
                    --i;
                    --size;
                }
                else {
                    e.alpha = 1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation(currentTime / lifeTime);
                    final float x = e.x;
                    final float vx = e.vx;
                    final float velocity = e.velocity;
                    final float n2 = (float)n;
                    e.x = x + vx * velocity * n2 / 500.0f;
                    e.y += e.vy * velocity * n2 / 500.0f;
                    e.currentTime += n2;
                }
            }
        }
        
        @SuppressLint({ "DrawAllocation" })
        protected void onDraw(final Canvas canvas) {
            if (SecretMediaViewer.this.currentMessageObject != null) {
                if (SecretMediaViewer.this.currentMessageObject.messageOwner.destroyTime != 0) {
                    canvas.drawCircle((float)(this.getMeasuredWidth() - AndroidUtilities.dp(35.0f)), (float)(this.getMeasuredHeight() / 2), (float)AndroidUtilities.dp(16.0f), this.circlePaint);
                    float n = 0.0f;
                    Label_0187: {
                        if (this.useVideoProgress) {
                            if (SecretMediaViewer.this.videoPlayer != null) {
                                final long duration = SecretMediaViewer.this.videoPlayer.getDuration();
                                final long currentPosition = SecretMediaViewer.this.videoPlayer.getCurrentPosition();
                                if (duration != -9223372036854775807L && currentPosition != -9223372036854775807L) {
                                    n = 1.0f - currentPosition / (float)duration;
                                    break Label_0187;
                                }
                            }
                            n = 1.0f;
                        }
                        else {
                            n = Math.max(0L, this.destroyTime - (System.currentTimeMillis() + ConnectionsManager.getInstance(SecretMediaViewer.this.currentAccount).getTimeDifference() * 1000)) / (this.destroyTtl * 1000.0f);
                        }
                    }
                    final int n2 = this.getMeasuredWidth() - AndroidUtilities.dp(40.0f);
                    final int n3 = (this.getMeasuredHeight() - AndroidUtilities.dp(14.0f)) / 2 - AndroidUtilities.dp(0.5f);
                    this.drawable.setBounds(n2, n3, AndroidUtilities.dp(10.0f) + n2, AndroidUtilities.dp(14.0f) + n3);
                    this.drawable.draw(canvas);
                    final float n4 = n * -360.0f;
                    canvas.drawArc(this.deleteProgressRect, -90.0f, n4, false, this.afterDeleteProgressPaint);
                    for (int size = this.particles.size(), i = 0; i < size; ++i) {
                        final Particle particle = this.particles.get(i);
                        this.particlePaint.setAlpha((int)(particle.alpha * 255.0f));
                        canvas.drawPoint(particle.x, particle.y, this.particlePaint);
                    }
                    final double v = n4 - 90.0f;
                    Double.isNaN(v);
                    final double n5 = v * 0.017453292519943295;
                    final double sin = Math.sin(n5);
                    final double n6 = -Math.cos(n5);
                    final int dp = AndroidUtilities.dp(14.0f);
                    final double n7 = -n6;
                    final double n8 = dp;
                    Double.isNaN(n8);
                    final double v2 = this.deleteProgressRect.centerX();
                    Double.isNaN(v2);
                    final float x = (float)(n7 * n8 + v2);
                    Double.isNaN(n8);
                    final double v3 = this.deleteProgressRect.centerY();
                    Double.isNaN(v3);
                    final float y = (float)(n8 * sin + v3);
                    for (int j = 0; j < 1; ++j) {
                        Particle e;
                        if (!this.freeParticles.isEmpty()) {
                            e = this.freeParticles.get(0);
                            this.freeParticles.remove(0);
                        }
                        else {
                            e = new Particle();
                        }
                        e.x = x;
                        e.y = y;
                        final double v4 = Utilities.random.nextInt(140) - 70;
                        Double.isNaN(v4);
                        double n10;
                        final double n9 = n10 = v4 * 0.017453292519943295;
                        if (n9 < 0.0) {
                            n10 = n9 + 6.283185307179586;
                        }
                        e.vx = (float)(Math.cos(n10) * sin - Math.sin(n10) * n6);
                        e.vy = (float)(Math.sin(n10) * sin + Math.cos(n10) * n6);
                        e.alpha = 1.0f;
                        e.currentTime = 0.0f;
                        e.lifeTime = (float)(Utilities.random.nextInt(100) + 400);
                        e.velocity = Utilities.random.nextFloat() * 4.0f + 20.0f;
                        this.particles.add(e);
                    }
                    final long currentTimeMillis = System.currentTimeMillis();
                    this.updateParticles(currentTimeMillis - this.lastAnimationTime);
                    this.lastAnimationTime = currentTimeMillis;
                    this.invalidate();
                }
            }
        }
        
        protected void onMeasure(int n, final int n2) {
            super.onMeasure(n, n2);
            n = this.getMeasuredHeight() / 2 - AndroidUtilities.dp(28.0f) / 2;
            this.deleteProgressRect.set((float)(this.getMeasuredWidth() - AndroidUtilities.dp(49.0f)), (float)n, (float)(this.getMeasuredWidth() - AndroidUtilities.dp(21.0f)), (float)(n + AndroidUtilities.dp(28.0f)));
        }
        
        private class Particle
        {
            float alpha;
            float currentTime;
            float lifeTime;
            float velocity;
            float vx;
            float vy;
            float x;
            float y;
        }
    }
}
