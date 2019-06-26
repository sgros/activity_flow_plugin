// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;
import android.os.Build$VERSION;
import android.animation.ValueAnimator;

public class CustomZoomButtonsController
{
    private boolean detached;
    private float mAlpha01;
    private CustomZoomButtonsDisplay mDisplay;
    private final ValueAnimator mFadeOutAnimation;
    private int mFadeOutAnimationDurationInMillis;
    private boolean mJustActivated;
    private long mLatestActivation;
    private OnZoomListener mListener;
    private final MapView mMapView;
    private final Runnable mRunnable;
    private int mShowDelayInMillis;
    private Thread mThread;
    private final Object mThreadSync;
    private Visibility mVisibility;
    private boolean mZoomInEnabled;
    private boolean mZoomOutEnabled;
    
    public CustomZoomButtonsController(final MapView mMapView) {
        this.mThreadSync = new Object();
        this.mVisibility = Visibility.NEVER;
        this.mFadeOutAnimationDurationInMillis = 500;
        this.mShowDelayInMillis = 3500;
        this.mMapView = mMapView;
        this.mDisplay = new CustomZoomButtonsDisplay(this.mMapView);
        if (Build$VERSION.SDK_INT >= 11) {
            (this.mFadeOutAnimation = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f })).setInterpolator((TimeInterpolator)new LinearInterpolator());
            this.mFadeOutAnimation.setDuration((long)this.mFadeOutAnimationDurationInMillis);
            this.mFadeOutAnimation.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    if (CustomZoomButtonsController.this.detached) {
                        CustomZoomButtonsController.this.mFadeOutAnimation.cancel();
                        return;
                    }
                    CustomZoomButtonsController.this.mAlpha01 = 1.0f - (float)valueAnimator.getAnimatedValue();
                    CustomZoomButtonsController.this.invalidate();
                }
            });
        }
        else {
            this.mFadeOutAnimation = null;
        }
        this.mRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final long millis = CustomZoomButtonsController.this.mLatestActivation + CustomZoomButtonsController.this.mShowDelayInMillis - CustomZoomButtonsController.this.nowInMillis();
                    if (millis <= 0L) {
                        break;
                    }
                    try {
                        Thread.sleep(millis, 0);
                    }
                    catch (InterruptedException ex) {}
                }
                CustomZoomButtonsController.this.startFadeOut();
            }
        };
    }
    
    private boolean checkJustActivated() {
        if (this.mJustActivated) {
            this.mJustActivated = false;
            return true;
        }
        return false;
    }
    
    private void invalidate() {
        if (this.detached) {
            return;
        }
        this.mMapView.postInvalidate();
    }
    
    private boolean isTouched(final MotionEvent motionEvent) {
        if (this.mAlpha01 == 0.0f) {
            return false;
        }
        if (this.checkJustActivated()) {
            return false;
        }
        if (this.mDisplay.isTouchedRotated(motionEvent, true)) {
            if (this.mZoomInEnabled) {
                final OnZoomListener mListener = this.mListener;
                if (mListener != null) {
                    mListener.onZoom(true);
                }
            }
            return true;
        }
        if (this.mDisplay.isTouchedRotated(motionEvent, false)) {
            if (this.mZoomOutEnabled) {
                final OnZoomListener mListener2 = this.mListener;
                if (mListener2 != null) {
                    mListener2.onZoom(false);
                }
            }
            return true;
        }
        return false;
    }
    
    private long nowInMillis() {
        return System.currentTimeMillis();
    }
    
    private void startFadeOut() {
        if (this.detached) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 11) {
            this.mFadeOutAnimation.setStartDelay(0L);
            this.mMapView.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    CustomZoomButtonsController.this.mFadeOutAnimation.start();
                }
            });
        }
        else {
            this.mAlpha01 = 0.0f;
            this.invalidate();
        }
    }
    
    private void stopFadeOut() {
        if (Build$VERSION.SDK_INT >= 11) {
            this.mFadeOutAnimation.cancel();
        }
    }
    
    public void activate() {
        if (this.detached) {
            return;
        }
        if (this.mVisibility != Visibility.SHOW_AND_FADEOUT) {
            return;
        }
        final float mAlpha01 = this.mAlpha01;
        final boolean mJustActivated = this.mJustActivated;
        boolean mJustActivated2 = false;
        if (!mJustActivated) {
            if (mAlpha01 == 0.0f) {
                mJustActivated2 = true;
            }
            this.mJustActivated = mJustActivated2;
        }
        else {
            this.mJustActivated = false;
        }
        this.stopFadeOut();
        this.mAlpha01 = 1.0f;
        this.mLatestActivation = this.nowInMillis();
        this.invalidate();
        final Thread mThread = this.mThread;
        if (mThread != null && mThread.getState() != Thread.State.TERMINATED) {
            return;
        }
        synchronized (this.mThreadSync) {
            if (this.mThread == null || this.mThread.getState() == Thread.State.TERMINATED) {
                (this.mThread = new Thread(this.mRunnable)).start();
            }
        }
    }
    
    public void draw(final Canvas canvas) {
        this.mDisplay.draw(canvas, this.mAlpha01, this.mZoomInEnabled, this.mZoomOutEnabled);
    }
    
    public void onDetach() {
        this.detached = true;
        this.stopFadeOut();
    }
    
    public boolean onLongPress(final MotionEvent motionEvent) {
        return this.isTouched(motionEvent);
    }
    
    public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
        return this.isTouched(motionEvent);
    }
    
    public void setOnZoomListener(final OnZoomListener mListener) {
        this.mListener = mListener;
    }
    
    public void setVisibility(final Visibility mVisibility) {
        this.mVisibility = mVisibility;
        final int n = CustomZoomButtonsController$4.$SwitchMap$org$osmdroid$views$CustomZoomButtonsController$Visibility[this.mVisibility.ordinal()];
        if (n != 1) {
            if (n == 2 || n == 3) {
                this.mAlpha01 = 0.0f;
            }
        }
        else {
            this.mAlpha01 = 1.0f;
        }
    }
    
    public void setZoomInEnabled(final boolean mZoomInEnabled) {
        this.mZoomInEnabled = mZoomInEnabled;
    }
    
    public void setZoomOutEnabled(final boolean mZoomOutEnabled) {
        this.mZoomOutEnabled = mZoomOutEnabled;
    }
    
    public interface OnZoomListener
    {
        void onZoom(final boolean p0);
    }
    
    public enum Visibility
    {
        ALWAYS, 
        NEVER, 
        SHOW_AND_FADEOUT;
    }
}
