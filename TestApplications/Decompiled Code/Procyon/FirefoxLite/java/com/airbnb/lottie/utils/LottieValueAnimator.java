// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.utils;

import android.view.Choreographer;
import com.airbnb.lottie.LottieComposition;
import android.view.Choreographer$FrameCallback;

public class LottieValueAnimator extends BaseLottieAnimator implements Choreographer$FrameCallback
{
    private LottieComposition composition;
    private float frame;
    private long lastFrameTimeNs;
    private float maxFrame;
    private float minFrame;
    private int repeatCount;
    protected boolean running;
    private float speed;
    private boolean speedReversedForRepeatMode;
    
    public LottieValueAnimator() {
        this.speed = 1.0f;
        this.speedReversedForRepeatMode = false;
        this.lastFrameTimeNs = 0L;
        this.frame = 0.0f;
        this.repeatCount = 0;
        this.minFrame = -2.14748365E9f;
        this.maxFrame = 2.14748365E9f;
        this.running = false;
    }
    
    private float getFrameDurationNs() {
        if (this.composition == null) {
            return Float.MAX_VALUE;
        }
        return 1.0E9f / this.composition.getFrameRate() / Math.abs(this.speed);
    }
    
    private boolean isReversed() {
        return this.getSpeed() < 0.0f;
    }
    
    private void verifyFrame() {
        if (this.composition == null) {
            return;
        }
        if (this.frame >= this.minFrame && this.frame <= this.maxFrame) {
            return;
        }
        throw new IllegalStateException(String.format("Frame must be [%f,%f]. It is %f", this.minFrame, this.maxFrame, this.frame));
    }
    
    public void cancel() {
        this.notifyCancel();
        this.removeFrameCallback();
    }
    
    public void clearComposition() {
        this.composition = null;
        this.minFrame = -2.14748365E9f;
        this.maxFrame = 2.14748365E9f;
    }
    
    public void doFrame(long nanoTime) {
        this.postFrameCallback();
        if (this.composition != null && this.isRunning()) {
            nanoTime = System.nanoTime();
            final float n = (nanoTime - this.lastFrameTimeNs) / this.getFrameDurationNs();
            final float frame = this.frame;
            float n2 = n;
            if (this.isReversed()) {
                n2 = -n;
            }
            this.frame = frame + n2;
            final boolean contains = MiscUtils.contains(this.frame, this.getMinFrame(), this.getMaxFrame());
            this.frame = MiscUtils.clamp(this.frame, this.getMinFrame(), this.getMaxFrame());
            this.lastFrameTimeNs = nanoTime;
            this.notifyUpdate();
            if (contains ^ true) {
                if (this.getRepeatCount() != -1 && this.repeatCount >= this.getRepeatCount()) {
                    this.frame = this.getMaxFrame();
                    this.removeFrameCallback();
                    this.notifyEnd(this.isReversed());
                }
                else {
                    this.notifyRepeat();
                    ++this.repeatCount;
                    if (this.getRepeatMode() == 2) {
                        this.speedReversedForRepeatMode ^= true;
                        this.reverseAnimationSpeed();
                    }
                    else {
                        float frame2;
                        if (this.isReversed()) {
                            frame2 = this.getMaxFrame();
                        }
                        else {
                            frame2 = this.getMinFrame();
                        }
                        this.frame = frame2;
                    }
                    this.lastFrameTimeNs = nanoTime;
                }
            }
            this.verifyFrame();
        }
    }
    
    public void endAnimation() {
        this.removeFrameCallback();
        this.notifyEnd(this.isReversed());
    }
    
    public float getAnimatedFraction() {
        if (this.composition == null) {
            return 0.0f;
        }
        if (this.isReversed()) {
            return (this.getMaxFrame() - this.frame) / (this.getMaxFrame() - this.getMinFrame());
        }
        return (this.frame - this.getMinFrame()) / (this.getMaxFrame() - this.getMinFrame());
    }
    
    public Object getAnimatedValue() {
        return this.getAnimatedValueAbsolute();
    }
    
    public float getAnimatedValueAbsolute() {
        if (this.composition == null) {
            return 0.0f;
        }
        return (this.frame - this.composition.getStartFrame()) / (this.composition.getEndFrame() - this.composition.getStartFrame());
    }
    
    public long getDuration() {
        long n;
        if (this.composition == null) {
            n = 0L;
        }
        else {
            n = (long)this.composition.getDuration();
        }
        return n;
    }
    
    public float getFrame() {
        return this.frame;
    }
    
    public float getMaxFrame() {
        if (this.composition == null) {
            return 0.0f;
        }
        float n;
        if (this.maxFrame == 2.14748365E9f) {
            n = this.composition.getEndFrame();
        }
        else {
            n = this.maxFrame;
        }
        return n;
    }
    
    public float getMinFrame() {
        if (this.composition == null) {
            return 0.0f;
        }
        float n;
        if (this.minFrame == -2.14748365E9f) {
            n = this.composition.getStartFrame();
        }
        else {
            n = this.minFrame;
        }
        return n;
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void playAnimation() {
        this.running = true;
        this.notifyStart(this.isReversed());
        float n;
        if (this.isReversed()) {
            n = this.getMaxFrame();
        }
        else {
            n = this.getMinFrame();
        }
        this.setFrame((int)n);
        this.lastFrameTimeNs = System.nanoTime();
        this.repeatCount = 0;
        this.postFrameCallback();
    }
    
    protected void postFrameCallback() {
        if (this.isRunning()) {
            this.removeFrameCallback(false);
            Choreographer.getInstance().postFrameCallback((Choreographer$FrameCallback)this);
        }
    }
    
    protected void removeFrameCallback() {
        this.removeFrameCallback(true);
    }
    
    protected void removeFrameCallback(final boolean b) {
        Choreographer.getInstance().removeFrameCallback((Choreographer$FrameCallback)this);
        if (b) {
            this.running = false;
        }
    }
    
    public void reverseAnimationSpeed() {
        this.setSpeed(-this.getSpeed());
    }
    
    public void setComposition(final LottieComposition composition) {
        final boolean b = this.composition == null;
        this.composition = composition;
        if (b) {
            this.setMinAndMaxFrames((int)Math.max(this.minFrame, composition.getStartFrame()), (int)Math.min(this.maxFrame, composition.getEndFrame()));
        }
        else {
            this.setMinAndMaxFrames((int)composition.getStartFrame(), (int)composition.getEndFrame());
        }
        this.setFrame((int)this.frame);
        this.lastFrameTimeNs = System.nanoTime();
    }
    
    public void setFrame(final int n) {
        final float frame = this.frame;
        final float n2 = (float)n;
        if (frame == n2) {
            return;
        }
        this.frame = MiscUtils.clamp(n2, this.getMinFrame(), this.getMaxFrame());
        this.lastFrameTimeNs = System.nanoTime();
        this.notifyUpdate();
    }
    
    public void setMaxFrame(final int n) {
        this.setMinAndMaxFrames((int)this.minFrame, n);
    }
    
    public void setMinAndMaxFrames(final int n, final int n2) {
        float startFrame;
        if (this.composition == null) {
            startFrame = -3.4028235E38f;
        }
        else {
            startFrame = this.composition.getStartFrame();
        }
        float endFrame;
        if (this.composition == null) {
            endFrame = Float.MAX_VALUE;
        }
        else {
            endFrame = this.composition.getEndFrame();
        }
        final float n3 = (float)n;
        this.minFrame = MiscUtils.clamp(n3, startFrame, endFrame);
        final float n4 = (float)n2;
        this.maxFrame = MiscUtils.clamp(n4, startFrame, endFrame);
        this.setFrame((int)MiscUtils.clamp(this.frame, n3, n4));
    }
    
    public void setMinFrame(final int n) {
        this.setMinAndMaxFrames(n, (int)this.maxFrame);
    }
    
    public void setRepeatMode(final int repeatMode) {
        super.setRepeatMode(repeatMode);
        if (repeatMode != 2 && this.speedReversedForRepeatMode) {
            this.speedReversedForRepeatMode = false;
            this.reverseAnimationSpeed();
        }
    }
    
    public void setSpeed(final float speed) {
        this.speed = speed;
    }
}
