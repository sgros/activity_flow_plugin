// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.view.View$OnTouchListener;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.graphics.Paint;
import android.widget.ImageView;
import org.telegram.messenger.FileLog;
import android.os.Build$VERSION;
import android.view.ViewGroup;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import android.view.ViewParent;
import android.view.MotionEvent;
import android.content.Context;
import android.view.TextureView;
import android.webkit.WebView;
import android.view.ViewGroup$LayoutParams;
import androidx.annotation.Keep;
import org.telegram.messenger.ApplicationLoader;
import android.content.SharedPreferences$Editor;
import java.util.Collection;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.ActionBar;
import android.animation.ObjectAnimator;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import android.widget.FrameLayout;
import android.view.WindowManager;
import android.view.WindowManager$LayoutParams;
import android.content.SharedPreferences;
import org.telegram.ui.PhotoViewer;
import android.app.Activity;
import android.view.animation.DecelerateInterpolator;
import android.view.View;

public class PipVideoView
{
    private View controlsView;
    private DecelerateInterpolator decelerateInterpolator;
    private Activity parentActivity;
    private EmbedBottomSheet parentSheet;
    private PhotoViewer photoViewer;
    private SharedPreferences preferences;
    private int videoHeight;
    private int videoWidth;
    private WindowManager$LayoutParams windowLayoutParams;
    private WindowManager windowManager;
    private FrameLayout windowView;
    
    private void animateToBoundsMaybe() {
        final int sideCoord = getSideCoord(true, 0, 0.0f, this.videoWidth);
        final int sideCoord2 = getSideCoord(true, 1, 0.0f, this.videoWidth);
        final int sideCoord3 = getSideCoord(false, 0, 0.0f, this.videoHeight);
        final int sideCoord4 = getSideCoord(false, 1, 0.0f, this.videoHeight);
        final SharedPreferences$Editor edit = this.preferences.edit();
        final int dp = AndroidUtilities.dp(20.0f);
        ArrayList<ObjectAnimator> list = null;
        boolean b = false;
        Label_0460: {
            Label_0458: {
                if (Math.abs(sideCoord - this.windowLayoutParams.x) > dp) {
                    final int x = this.windowLayoutParams.x;
                    if (x >= 0 || x <= -this.videoWidth / 4) {
                        if (Math.abs(sideCoord2 - this.windowLayoutParams.x) > dp) {
                            final int x2 = this.windowLayoutParams.x;
                            final int x3 = AndroidUtilities.displaySize.x;
                            final int videoWidth = this.videoWidth;
                            if (x2 <= x3 - videoWidth || x2 >= x3 - videoWidth / 4 * 3) {
                                if (this.windowView.getAlpha() != 1.0f) {
                                    list = new ArrayList<ObjectAnimator>();
                                    if (this.windowLayoutParams.x < 0) {
                                        list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { -this.videoWidth }));
                                    }
                                    else {
                                        list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { AndroidUtilities.displaySize.x }));
                                    }
                                    b = true;
                                    break Label_0460;
                                }
                                edit.putFloat("px", (this.windowLayoutParams.x - sideCoord) / (float)(sideCoord2 - sideCoord));
                                edit.putInt("sidex", 2);
                                list = null;
                                break Label_0458;
                            }
                        }
                        list = new ArrayList<ObjectAnimator>();
                        edit.putInt("sidex", 1);
                        if (this.windowView.getAlpha() != 1.0f) {
                            list.add(ObjectAnimator.ofFloat((Object)this.windowView, "alpha", new float[] { 1.0f }));
                        }
                        b = false;
                        list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { sideCoord2 }));
                        break Label_0460;
                    }
                }
                list = new ArrayList<ObjectAnimator>();
                edit.putInt("sidex", 0);
                if (this.windowView.getAlpha() != 1.0f) {
                    list.add(ObjectAnimator.ofFloat((Object)this.windowView, "alpha", new float[] { 1.0f }));
                }
                list.add(ObjectAnimator.ofInt((Object)this, "x", new int[] { sideCoord }));
            }
            b = false;
        }
        ArrayList<ObjectAnimator> list2 = list;
        if (!b) {
            if (Math.abs(sideCoord3 - this.windowLayoutParams.y) > dp && this.windowLayoutParams.y > ActionBar.getCurrentActionBarHeight()) {
                if (Math.abs(sideCoord4 - this.windowLayoutParams.y) <= dp) {
                    ArrayList<ObjectAnimator> list3;
                    if ((list3 = list) == null) {
                        list3 = new ArrayList<ObjectAnimator>();
                    }
                    edit.putInt("sidey", 1);
                    list3.add(ObjectAnimator.ofInt((Object)this, "y", new int[] { sideCoord4 }));
                    list = list3;
                }
                else {
                    edit.putFloat("py", (this.windowLayoutParams.y - sideCoord3) / (float)(sideCoord4 - sideCoord3));
                    edit.putInt("sidey", 2);
                }
            }
            else {
                ArrayList<ObjectAnimator> list4;
                if ((list4 = list) == null) {
                    list4 = new ArrayList<ObjectAnimator>();
                }
                edit.putInt("sidey", 0);
                list4.add(ObjectAnimator.ofInt((Object)this, "y", new int[] { sideCoord3 }));
                list = list4;
            }
            edit.commit();
            list2 = list;
        }
        if (list2 != null) {
            if (this.decelerateInterpolator == null) {
                this.decelerateInterpolator = new DecelerateInterpolator();
            }
            final AnimatorSet set = new AnimatorSet();
            set.setInterpolator((TimeInterpolator)this.decelerateInterpolator);
            set.setDuration(150L);
            if (b) {
                list2.add(ObjectAnimator.ofFloat((Object)this.windowView, "alpha", new float[] { 0.0f }));
                set.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        if (PipVideoView.this.parentSheet != null) {
                            PipVideoView.this.parentSheet.destroy();
                        }
                        else if (PipVideoView.this.photoViewer != null) {
                            PipVideoView.this.photoViewer.destroyPhotoViewer();
                        }
                    }
                });
            }
            set.playTogether((Collection)list2);
            set.start();
        }
    }
    
    public static Rect getPipRect(final float n) {
        final SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("pipconfig", 0);
        final int int1 = sharedPreferences.getInt("sidex", 1);
        final int int2 = sharedPreferences.getInt("sidey", 0);
        final float float1 = sharedPreferences.getFloat("px", 0.0f);
        final float float2 = sharedPreferences.getFloat("py", 0.0f);
        int dp;
        int dp2;
        if (n > 1.0f) {
            dp = AndroidUtilities.dp(192.0f);
            dp2 = (int)(dp / n);
        }
        else {
            dp2 = AndroidUtilities.dp(192.0f);
            dp = (int)(dp2 * n);
        }
        return new Rect((float)getSideCoord(true, int1, float1, dp), (float)getSideCoord(false, int2, float2, dp2), (float)dp, (float)dp2);
    }
    
    private static int getSideCoord(final boolean b, int dp, final float n, int currentActionBarHeight) {
        int x;
        if (b) {
            x = AndroidUtilities.displaySize.x;
        }
        else {
            x = AndroidUtilities.displaySize.y - currentActionBarHeight;
            currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
        }
        currentActionBarHeight = x - currentActionBarHeight;
        if (dp == 0) {
            dp = AndroidUtilities.dp(10.0f);
        }
        else if (dp == 1) {
            dp = currentActionBarHeight - AndroidUtilities.dp(10.0f);
        }
        else {
            dp = Math.round((currentActionBarHeight - AndroidUtilities.dp(20.0f)) * n) + AndroidUtilities.dp(10.0f);
        }
        currentActionBarHeight = dp;
        if (!b) {
            currentActionBarHeight = dp + ActionBar.getCurrentActionBarHeight();
        }
        return currentActionBarHeight;
    }
    
    public void close() {
        while (true) {
            try {
                this.windowManager.removeView((View)this.windowView);
                this.parentSheet = null;
                this.photoViewer = null;
                this.parentActivity = null;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
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
        final int int1 = this.preferences.getInt("sidex", 1);
        final int int2 = this.preferences.getInt("sidey", 0);
        final float float1 = this.preferences.getFloat("px", 0.0f);
        final float float2 = this.preferences.getFloat("py", 0.0f);
        this.windowLayoutParams.x = getSideCoord(true, int1, float1, this.videoWidth);
        this.windowLayoutParams.y = getSideCoord(false, int2, float2, this.videoHeight);
        this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
    }
    
    public void onVideoCompleted() {
        final View controlsView = this.controlsView;
        if (controlsView instanceof MiniControlsView) {
            final MiniControlsView miniControlsView = (MiniControlsView)controlsView;
            miniControlsView.isCompleted = true;
            miniControlsView.progress = 0.0f;
            miniControlsView.bufferedPosition = 0.0f;
            miniControlsView.updatePlayButton();
            miniControlsView.invalidate();
            miniControlsView.show(true, true);
        }
    }
    
    public void setBufferedProgress(final float bufferedProgress) {
        final View controlsView = this.controlsView;
        if (controlsView instanceof MiniControlsView) {
            ((MiniControlsView)controlsView).setBufferedProgress(bufferedProgress);
        }
    }
    
    @Keep
    public void setX(final int x) {
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.x = x;
        this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)windowLayoutParams);
    }
    
    @Keep
    public void setY(final int y) {
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.y = y;
        this.windowManager.updateViewLayout((View)this.windowView, (ViewGroup$LayoutParams)windowLayoutParams);
    }
    
    public TextureView show(final Activity activity, final EmbedBottomSheet embedBottomSheet, final View view, final float n, final int n2, final WebView webView) {
        return this.show(activity, null, embedBottomSheet, view, n, n2, webView);
    }
    
    public TextureView show(final Activity activity, final PhotoViewer photoViewer, final float n, final int n2) {
        return this.show(activity, photoViewer, null, null, n, n2, null);
    }
    
    public TextureView show(final Activity parentActivity, final PhotoViewer photoViewer, final EmbedBottomSheet parentSheet, final View controlsView, float float1, int int1, final WebView webView) {
        this.parentSheet = parentSheet;
        this.parentActivity = parentActivity;
        this.photoViewer = photoViewer;
        this.windowView = new FrameLayout(parentActivity) {
            private boolean dragging;
            private float startX;
            private float startY;
            
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                final float rawX = motionEvent.getRawX();
                final float rawY = motionEvent.getRawY();
                if (motionEvent.getAction() == 0) {
                    this.startX = rawX;
                    this.startY = rawY;
                }
                else if (motionEvent.getAction() == 2 && !this.dragging && (Math.abs(this.startX - rawX) >= AndroidUtilities.getPixelsInCM(0.3f, true) || Math.abs(this.startY - rawY) >= AndroidUtilities.getPixelsInCM(0.3f, false))) {
                    this.dragging = true;
                    this.startX = rawX;
                    this.startY = rawY;
                    if (PipVideoView.this.controlsView != null) {
                        ((ViewParent)PipVideoView.this.controlsView).requestDisallowInterceptTouchEvent(true);
                    }
                    return true;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
            
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (!this.dragging) {
                    return false;
                }
                final float rawX = motionEvent.getRawX();
                final float rawY = motionEvent.getRawY();
                if (motionEvent.getAction() == 2) {
                    final float startX = this.startX;
                    final float startY = this.startY;
                    final WindowManager$LayoutParams access$500 = PipVideoView.this.windowLayoutParams;
                    access$500.x += (int)(rawX - startX);
                    final WindowManager$LayoutParams access$501 = PipVideoView.this.windowLayoutParams;
                    access$501.y += (int)(rawY - startY);
                    final int n = PipVideoView.this.videoWidth / 2;
                    final int x = PipVideoView.this.windowLayoutParams.x;
                    final int x2 = -n;
                    if (x < x2) {
                        PipVideoView.this.windowLayoutParams.x = x2;
                    }
                    else if (PipVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipVideoView.this.windowLayoutParams.width + n) {
                        PipVideoView.this.windowLayoutParams.x = AndroidUtilities.displaySize.x - PipVideoView.this.windowLayoutParams.width + n;
                    }
                    final int x3 = PipVideoView.this.windowLayoutParams.x;
                    float alpha = 1.0f;
                    if (x3 < 0) {
                        alpha = 1.0f + PipVideoView.this.windowLayoutParams.x / (float)n * 0.5f;
                    }
                    else if (PipVideoView.this.windowLayoutParams.x > AndroidUtilities.displaySize.x - PipVideoView.this.windowLayoutParams.width) {
                        alpha = 1.0f - (PipVideoView.this.windowLayoutParams.x - AndroidUtilities.displaySize.x + PipVideoView.this.windowLayoutParams.width) / (float)n * 0.5f;
                    }
                    if (PipVideoView.this.windowView.getAlpha() != alpha) {
                        PipVideoView.this.windowView.setAlpha(alpha);
                    }
                    if (PipVideoView.this.windowLayoutParams.y < 0) {
                        PipVideoView.this.windowLayoutParams.y = 0;
                    }
                    else if (PipVideoView.this.windowLayoutParams.y > AndroidUtilities.displaySize.y - PipVideoView.this.windowLayoutParams.height + 0) {
                        PipVideoView.this.windowLayoutParams.y = AndroidUtilities.displaySize.y - PipVideoView.this.windowLayoutParams.height + 0;
                    }
                    PipVideoView.this.windowManager.updateViewLayout((View)PipVideoView.this.windowView, (ViewGroup$LayoutParams)PipVideoView.this.windowLayoutParams);
                    this.startX = rawX;
                    this.startY = rawY;
                }
                else if (motionEvent.getAction() == 1) {
                    this.dragging = false;
                    PipVideoView.this.animateToBoundsMaybe();
                }
                return true;
            }
            
            public void requestDisallowInterceptTouchEvent(final boolean b) {
                super.requestDisallowInterceptTouchEvent(b);
            }
        };
        if (float1 > 1.0f) {
            this.videoWidth = AndroidUtilities.dp(192.0f);
            this.videoHeight = (int)(this.videoWidth / float1);
        }
        else {
            this.videoHeight = AndroidUtilities.dp(192.0f);
            this.videoWidth = (int)(this.videoHeight * float1);
        }
        final AspectRatioFrameLayout aspectRatioFrameLayout = new AspectRatioFrameLayout((Context)parentActivity);
        aspectRatioFrameLayout.setAspectRatio(float1, int1);
        this.windowView.addView((View)aspectRatioFrameLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        Object o;
        if (webView != null) {
            final ViewGroup viewGroup = (ViewGroup)webView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView((View)webView);
            }
            aspectRatioFrameLayout.addView((View)webView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
            o = null;
        }
        else {
            o = new TextureView((Context)parentActivity);
            aspectRatioFrameLayout.addView((View)o, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        }
        if (controlsView == null) {
            this.controlsView = (View)new MiniControlsView((Context)parentActivity, photoViewer != null);
        }
        else {
            this.controlsView = controlsView;
        }
        this.windowView.addView(this.controlsView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.windowManager = (WindowManager)ApplicationLoader.applicationContext.getSystemService("window");
        this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("pipconfig", 0);
        final int int2 = this.preferences.getInt("sidex", 1);
        int1 = this.preferences.getInt("sidey", 0);
        final float float2 = this.preferences.getFloat("px", 0.0f);
        float1 = this.preferences.getFloat("py", 0.0f);
        try {
            this.windowLayoutParams = new WindowManager$LayoutParams();
            this.windowLayoutParams.width = this.videoWidth;
            this.windowLayoutParams.height = this.videoHeight;
            this.windowLayoutParams.x = getSideCoord(true, int2, float2, this.videoWidth);
            this.windowLayoutParams.y = getSideCoord(false, int1, float1, this.videoHeight);
            this.windowLayoutParams.format = -3;
            this.windowLayoutParams.gravity = 51;
            if (Build$VERSION.SDK_INT >= 26) {
                this.windowLayoutParams.type = 2038;
            }
            else {
                this.windowLayoutParams.type = 2003;
            }
            this.windowLayoutParams.flags = 16777736;
            this.windowManager.addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
            return (TextureView)o;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return null;
        }
    }
    
    public void updatePlayButton() {
        final View controlsView = this.controlsView;
        if (controlsView instanceof MiniControlsView) {
            final MiniControlsView miniControlsView = (MiniControlsView)controlsView;
            miniControlsView.updatePlayButton();
            miniControlsView.invalidate();
        }
    }
    
    private class MiniControlsView extends FrameLayout
    {
        private float bufferedPosition;
        private AnimatorSet currentAnimation;
        private Runnable hideRunnable;
        private ImageView inlineButton;
        private boolean isCompleted;
        private boolean isVisible;
        private ImageView playButton;
        private float progress;
        private Paint progressInnerPaint;
        private Paint progressPaint;
        private Runnable progressRunnable;
        
        public MiniControlsView(final Context context, final boolean b) {
            super(context);
            this.isVisible = true;
            this.hideRunnable = new _$$Lambda$PipVideoView$MiniControlsView$1lMZ0uhQOCqoH6v0Akw4gfbgK2g(this);
            this.progressRunnable = new Runnable() {
                @Override
                public void run() {
                    if (PipVideoView.this.photoViewer == null) {
                        return;
                    }
                    final VideoPlayer videoPlayer = PipVideoView.this.photoViewer.getVideoPlayer();
                    if (videoPlayer == null) {
                        return;
                    }
                    MiniControlsView.this.setProgress(videoPlayer.getCurrentPosition() / (float)videoPlayer.getDuration());
                    if (PipVideoView.this.photoViewer == null) {
                        MiniControlsView.this.setBufferedProgress(videoPlayer.getBufferedPosition() / (float)videoPlayer.getDuration());
                    }
                    AndroidUtilities.runOnUIThread(MiniControlsView.this.progressRunnable, 1000L);
                }
            };
            (this.inlineButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            this.inlineButton.setImageResource(2131165458);
            this.addView((View)this.inlineButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(56, 48, 53));
            this.inlineButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PipVideoView$MiniControlsView$wbm8DJnUhe315ylEORpdCN9TCPU(this));
            if (b) {
                (this.progressPaint = new Paint()).setColor(-15095832);
                (this.progressInnerPaint = new Paint()).setColor(-6975081);
                this.setWillNotDraw(false);
                (this.playButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
                this.addView((View)this.playButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48, 17));
                this.playButton.setOnClickListener((View$OnClickListener)new _$$Lambda$PipVideoView$MiniControlsView$1JTbmkeJbI8PQhj0gt64f8TrJNY(this));
            }
            this.setOnTouchListener((View$OnTouchListener)_$$Lambda$PipVideoView$MiniControlsView$jKPELGvyg4VHgdM0XCb_ZaGLO4U.INSTANCE);
            this.updatePlayButton();
            this.show(false, false);
        }
        
        private void checkNeedHide() {
            AndroidUtilities.cancelRunOnUIThread(this.hideRunnable);
            if (this.isVisible) {
                AndroidUtilities.runOnUIThread(this.hideRunnable, 3000L);
            }
        }
        
        private void updatePlayButton() {
            if (PipVideoView.this.photoViewer == null) {
                return;
            }
            final VideoPlayer videoPlayer = PipVideoView.this.photoViewer.getVideoPlayer();
            if (videoPlayer == null) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.progressRunnable);
            if (!videoPlayer.isPlaying()) {
                if (this.isCompleted) {
                    this.playButton.setImageResource(2131165426);
                }
                else {
                    this.playButton.setImageResource(2131165464);
                }
            }
            else {
                this.playButton.setImageResource(2131165460);
                AndroidUtilities.runOnUIThread(this.progressRunnable, 500L);
            }
        }
        
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.checkNeedHide();
        }
        
        protected void onDraw(final Canvas canvas) {
            final int measuredWidth = this.getMeasuredWidth();
            final int n = this.getMeasuredHeight() - AndroidUtilities.dp(3.0f);
            AndroidUtilities.dp(7.0f);
            final float n2 = (float)(measuredWidth - 0);
            final int n3 = (int)(this.progress * n2);
            final float bufferedPosition = this.bufferedPosition;
            if (bufferedPosition != 0.0f) {
                final float n4 = 0;
                canvas.drawRect(n4, (float)n, n4 + n2 * bufferedPosition, (float)(AndroidUtilities.dp(3.0f) + n), this.progressInnerPaint);
            }
            canvas.drawRect((float)0, (float)n, (float)(n3 + 0), (float)(n + AndroidUtilities.dp(3.0f)), this.progressPaint);
        }
        
        public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                if (!this.isVisible) {
                    this.show(true, true);
                    return true;
                }
                this.checkNeedHide();
            }
            return super.onInterceptTouchEvent(motionEvent);
        }
        
        public void requestDisallowInterceptTouchEvent(final boolean b) {
            super.requestDisallowInterceptTouchEvent(b);
            this.checkNeedHide();
        }
        
        public void setBufferedProgress(final float bufferedPosition) {
            this.bufferedPosition = bufferedPosition;
            this.invalidate();
        }
        
        public void setProgress(final float progress) {
            this.progress = progress;
            this.invalidate();
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
                            MiniControlsView.this.currentAnimation = null;
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
                        MiniControlsView.this.currentAnimation = null;
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
}
