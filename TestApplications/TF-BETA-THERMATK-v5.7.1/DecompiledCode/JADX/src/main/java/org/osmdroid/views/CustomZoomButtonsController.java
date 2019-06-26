package org.osmdroid.views;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import java.lang.Thread.State;

public class CustomZoomButtonsController {
    private boolean detached;
    private float mAlpha01;
    private CustomZoomButtonsDisplay mDisplay;
    private final ValueAnimator mFadeOutAnimation;
    private int mFadeOutAnimationDurationInMillis = 500;
    private boolean mJustActivated;
    private long mLatestActivation;
    private OnZoomListener mListener;
    private final MapView mMapView;
    private final Runnable mRunnable;
    private int mShowDelayInMillis = 3500;
    private Thread mThread;
    private final Object mThreadSync = new Object();
    private Visibility mVisibility = Visibility.NEVER;
    private boolean mZoomInEnabled;
    private boolean mZoomOutEnabled;

    /* renamed from: org.osmdroid.views.CustomZoomButtonsController$1 */
    class C02671 implements AnimatorUpdateListener {
        C02671() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (CustomZoomButtonsController.this.detached) {
                CustomZoomButtonsController.this.mFadeOutAnimation.cancel();
                return;
            }
            CustomZoomButtonsController.this.mAlpha01 = 1.0f - ((Float) valueAnimator.getAnimatedValue()).floatValue();
            CustomZoomButtonsController.this.invalidate();
        }
    }

    /* renamed from: org.osmdroid.views.CustomZoomButtonsController$2 */
    class C02682 implements Runnable {
        C02682() {
        }

        public void run() {
            while (true) {
                long access$400 = (CustomZoomButtonsController.this.mLatestActivation + ((long) CustomZoomButtonsController.this.mShowDelayInMillis)) - CustomZoomButtonsController.this.nowInMillis();
                if (access$400 <= 0) {
                    CustomZoomButtonsController.this.startFadeOut();
                    return;
                }
                try {
                    Thread.sleep(access$400, 0);
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* renamed from: org.osmdroid.views.CustomZoomButtonsController$3 */
    class C02693 implements Runnable {
        C02693() {
        }

        public void run() {
            CustomZoomButtonsController.this.mFadeOutAnimation.start();
        }
    }

    /* renamed from: org.osmdroid.views.CustomZoomButtonsController$4 */
    static /* synthetic */ class C02704 {
        /* renamed from: $SwitchMap$org$osmdroid$views$CustomZoomButtonsController$Visibility */
        static final /* synthetic */ int[] f46xd7d73fa4 = new int[Visibility.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        static {
            /*
            r0 = org.osmdroid.views.CustomZoomButtonsController.Visibility.values();
            r0 = r0.length;
            r0 = new int[r0];
            f46xd7d73fa4 = r0;
            r0 = f46xd7d73fa4;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = org.osmdroid.views.CustomZoomButtonsController.Visibility.ALWAYS;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r0 = f46xd7d73fa4;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r0 = f46xd7d73fa4;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = org.osmdroid.views.CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r2 = 3;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.CustomZoomButtonsController$C02704.<clinit>():void");
        }
    }

    public interface OnZoomListener {
        void onZoom(boolean z);
    }

    public enum Visibility {
        ALWAYS,
        NEVER,
        SHOW_AND_FADEOUT
    }

    public CustomZoomButtonsController(MapView mapView) {
        this.mMapView = mapView;
        this.mDisplay = new CustomZoomButtonsDisplay(this.mMapView);
        if (VERSION.SDK_INT >= 11) {
            this.mFadeOutAnimation = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mFadeOutAnimation.setInterpolator(new LinearInterpolator());
            this.mFadeOutAnimation.setDuration((long) this.mFadeOutAnimationDurationInMillis);
            this.mFadeOutAnimation.addUpdateListener(new C02671());
        } else {
            this.mFadeOutAnimation = null;
        }
        this.mRunnable = new C02682();
    }

    public void setZoomInEnabled(boolean z) {
        this.mZoomInEnabled = z;
    }

    public void setZoomOutEnabled(boolean z) {
        this.mZoomOutEnabled = z;
    }

    public void setOnZoomListener(OnZoomListener onZoomListener) {
        this.mListener = onZoomListener;
    }

    public void setVisibility(Visibility visibility) {
        this.mVisibility = visibility;
        int i = C02704.f46xd7d73fa4[this.mVisibility.ordinal()];
        if (i == 1) {
            this.mAlpha01 = 1.0f;
        } else if (i == 2 || i == 3) {
            this.mAlpha01 = 0.0f;
        }
    }

    public void onDetach() {
        this.detached = true;
        stopFadeOut();
    }

    private long nowInMillis() {
        return System.currentTimeMillis();
    }

    private void startFadeOut() {
        if (!this.detached) {
            if (VERSION.SDK_INT >= 11) {
                this.mFadeOutAnimation.setStartDelay(0);
                this.mMapView.post(new C02693());
            } else {
                this.mAlpha01 = 0.0f;
                invalidate();
            }
        }
    }

    private void stopFadeOut() {
        if (VERSION.SDK_INT >= 11) {
            this.mFadeOutAnimation.cancel();
        }
    }

    private void invalidate() {
        if (!this.detached) {
            this.mMapView.postInvalidate();
        }
    }

    public void activate() {
        if (!this.detached && this.mVisibility == Visibility.SHOW_AND_FADEOUT) {
            float f = this.mAlpha01;
            boolean z = false;
            if (this.mJustActivated) {
                this.mJustActivated = false;
            } else {
                if (f == 0.0f) {
                    z = true;
                }
                this.mJustActivated = z;
            }
            stopFadeOut();
            this.mAlpha01 = 1.0f;
            this.mLatestActivation = nowInMillis();
            invalidate();
            Thread thread = this.mThread;
            if (thread == null || thread.getState() == State.TERMINATED) {
                synchronized (this.mThreadSync) {
                    if (this.mThread == null || this.mThread.getState() == State.TERMINATED) {
                        this.mThread = new Thread(this.mRunnable);
                        this.mThread.start();
                    }
                }
            }
        }
    }

    private boolean checkJustActivated() {
        if (!this.mJustActivated) {
            return false;
        }
        this.mJustActivated = false;
        return true;
    }

    private boolean isTouched(MotionEvent motionEvent) {
        if (this.mAlpha01 == 0.0f || checkJustActivated()) {
            return false;
        }
        OnZoomListener onZoomListener;
        if (this.mDisplay.isTouchedRotated(motionEvent, true)) {
            if (this.mZoomInEnabled) {
                onZoomListener = this.mListener;
                if (onZoomListener != null) {
                    onZoomListener.onZoom(true);
                }
            }
            return true;
        } else if (!this.mDisplay.isTouchedRotated(motionEvent, false)) {
            return false;
        } else {
            if (this.mZoomOutEnabled) {
                onZoomListener = this.mListener;
                if (onZoomListener != null) {
                    onZoomListener.onZoom(false);
                }
            }
            return true;
        }
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return isTouched(motionEvent);
    }

    public boolean onLongPress(MotionEvent motionEvent) {
        return isTouched(motionEvent);
    }

    public void draw(Canvas canvas) {
        this.mDisplay.draw(canvas, this.mAlpha01, this.mZoomInEnabled, this.mZoomOutEnabled);
    }
}
