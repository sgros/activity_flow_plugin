// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.value;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieComposition;

public class Keyframe<T>
{
    private final LottieComposition composition;
    public Float endFrame;
    private float endProgress;
    public T endValue;
    private float endValueFloat;
    private int endValueInt;
    public final Interpolator interpolator;
    public PointF pathCp1;
    public PointF pathCp2;
    public final float startFrame;
    private float startProgress;
    public final T startValue;
    private float startValueFloat;
    private int startValueInt;
    
    public Keyframe(final LottieComposition composition, final T startValue, final T endValue, final Interpolator interpolator, final float startFrame, final Float endFrame) {
        this.startValueFloat = -3987645.8f;
        this.endValueFloat = -3987645.8f;
        this.startValueInt = 784923401;
        this.endValueInt = 784923401;
        this.startProgress = Float.MIN_VALUE;
        this.endProgress = Float.MIN_VALUE;
        this.pathCp1 = null;
        this.pathCp2 = null;
        this.composition = composition;
        this.startValue = startValue;
        this.endValue = endValue;
        this.interpolator = interpolator;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
    }
    
    public Keyframe(final T t) {
        this.startValueFloat = -3987645.8f;
        this.endValueFloat = -3987645.8f;
        this.startValueInt = 784923401;
        this.endValueInt = 784923401;
        this.startProgress = Float.MIN_VALUE;
        this.endProgress = Float.MIN_VALUE;
        this.pathCp1 = null;
        this.pathCp2 = null;
        this.composition = null;
        this.startValue = t;
        this.endValue = t;
        this.interpolator = null;
        this.startFrame = Float.MIN_VALUE;
        this.endFrame = Float.MAX_VALUE;
    }
    
    public boolean containsProgress(final float n) {
        return n >= this.getStartProgress() && n < this.getEndProgress();
    }
    
    public float getEndProgress() {
        if (this.composition == null) {
            return 1.0f;
        }
        if (this.endProgress == Float.MIN_VALUE) {
            if (this.endFrame == null) {
                this.endProgress = 1.0f;
            }
            else {
                this.endProgress = this.getStartProgress() + (this.endFrame - this.startFrame) / this.composition.getDurationFrames();
            }
        }
        return this.endProgress;
    }
    
    public float getEndValueFloat() {
        if (this.endValueFloat == -3987645.8f) {
            this.endValueFloat = (float)this.endValue;
        }
        return this.endValueFloat;
    }
    
    public int getEndValueInt() {
        if (this.endValueInt == 784923401) {
            this.endValueInt = (int)this.endValue;
        }
        return this.endValueInt;
    }
    
    public float getStartProgress() {
        final LottieComposition composition = this.composition;
        if (composition == null) {
            return 0.0f;
        }
        if (this.startProgress == Float.MIN_VALUE) {
            this.startProgress = (this.startFrame - composition.getStartFrame()) / this.composition.getDurationFrames();
        }
        return this.startProgress;
    }
    
    public float getStartValueFloat() {
        if (this.startValueFloat == -3987645.8f) {
            this.startValueFloat = (float)this.startValue;
        }
        return this.startValueFloat;
    }
    
    public int getStartValueInt() {
        if (this.startValueInt == 784923401) {
            this.startValueInt = (int)this.startValue;
        }
        return this.startValueInt;
    }
    
    public boolean isStatic() {
        return this.interpolator == null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Keyframe{startValue=");
        sb.append(this.startValue);
        sb.append(", endValue=");
        sb.append(this.endValue);
        sb.append(", startFrame=");
        sb.append(this.startFrame);
        sb.append(", endFrame=");
        sb.append(this.endFrame);
        sb.append(", interpolator=");
        sb.append(this.interpolator);
        sb.append('}');
        return sb.toString();
    }
}
