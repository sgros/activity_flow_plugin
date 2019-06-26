// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.value;

public class LottieFrameInfo<T>
{
    private float endFrame;
    private T endValue;
    private float interpolatedKeyframeProgress;
    private float linearKeyframeProgress;
    private float overallProgress;
    private float startFrame;
    private T startValue;
    
    public LottieFrameInfo<T> set(final float startFrame, final float endFrame, final T startValue, final T endValue, final float linearKeyframeProgress, final float interpolatedKeyframeProgress, final float overallProgress) {
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.startValue = startValue;
        this.endValue = endValue;
        this.linearKeyframeProgress = linearKeyframeProgress;
        this.interpolatedKeyframeProgress = interpolatedKeyframeProgress;
        this.overallProgress = overallProgress;
        return this;
    }
}
