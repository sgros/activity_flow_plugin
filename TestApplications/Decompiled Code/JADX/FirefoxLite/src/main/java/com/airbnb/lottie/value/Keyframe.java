package com.airbnb.lottie.value;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieComposition;

public class Keyframe<T> {
    private final LottieComposition composition;
    public Float endFrame;
    private float endProgress;
    public final T endValue;
    public final Interpolator interpolator;
    public PointF pathCp1;
    public PointF pathCp2;
    public final float startFrame;
    private float startProgress;
    public final T startValue;

    public Keyframe(LottieComposition lottieComposition, T t, T t2, Interpolator interpolator, float f, Float f2) {
        this.startProgress = Float.MIN_VALUE;
        this.endProgress = Float.MIN_VALUE;
        this.pathCp1 = null;
        this.pathCp2 = null;
        this.composition = lottieComposition;
        this.startValue = t;
        this.endValue = t2;
        this.interpolator = interpolator;
        this.startFrame = f;
        this.endFrame = f2;
    }

    public Keyframe(T t) {
        this.startProgress = Float.MIN_VALUE;
        this.endProgress = Float.MIN_VALUE;
        this.pathCp1 = null;
        this.pathCp2 = null;
        this.composition = null;
        this.startValue = t;
        this.endValue = t;
        this.interpolator = null;
        this.startFrame = Float.MIN_VALUE;
        this.endFrame = Float.valueOf(Float.MAX_VALUE);
    }

    public float getStartProgress() {
        if (this.composition == null) {
            return 0.0f;
        }
        if (this.startProgress == Float.MIN_VALUE) {
            this.startProgress = (this.startFrame - this.composition.getStartFrame()) / this.composition.getDurationFrames();
        }
        return this.startProgress;
    }

    public float getEndProgress() {
        if (this.composition == null) {
            return 1.0f;
        }
        if (this.endProgress == Float.MIN_VALUE) {
            if (this.endFrame == null) {
                this.endProgress = 1.0f;
            } else {
                this.endProgress = getStartProgress() + ((this.endFrame.floatValue() - this.startFrame) / this.composition.getDurationFrames());
            }
        }
        return this.endProgress;
    }

    public boolean isStatic() {
        return this.interpolator == null;
    }

    public boolean containsProgress(float f) {
        return f >= getStartProgress() && f < getEndProgress();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Keyframe{startValue=");
        stringBuilder.append(this.startValue);
        stringBuilder.append(", endValue=");
        stringBuilder.append(this.endValue);
        stringBuilder.append(", startFrame=");
        stringBuilder.append(this.startFrame);
        stringBuilder.append(", endFrame=");
        stringBuilder.append(this.endFrame);
        stringBuilder.append(", interpolator=");
        stringBuilder.append(this.interpolator);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
