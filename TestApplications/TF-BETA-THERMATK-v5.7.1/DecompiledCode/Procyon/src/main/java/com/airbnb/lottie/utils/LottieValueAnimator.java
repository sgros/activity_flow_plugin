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
        final LottieComposition composition = this.composition;
        if (composition == null) {
            return Float.MAX_VALUE;
        }
        return 1.0E9f / composition.getFrameRate() / Math.abs(this.speed);
    }
    
    private boolean isReversed() {
        return this.getSpeed() < 0.0f;
    }
    
    private void verifyFrame() {
        if (this.composition == null) {
            return;
        }
        final float frame = this.frame;
        if (frame >= this.minFrame && frame <= this.maxFrame) {
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
    
    public void doFrame(final long n) {
        this.postFrameCallback();
        if (this.composition != null) {
            if (this.isRunning()) {
                final long lastFrameTimeNs = this.lastFrameTimeNs;
                long n2 = 0L;
                if (lastFrameTimeNs != 0L) {
                    n2 = n - lastFrameTimeNs;
                }
                final float n3 = n2 / this.getFrameDurationNs();
                final float frame = this.frame;
                float n4 = n3;
                if (this.isReversed()) {
                    n4 = -n3;
                }
                this.frame = frame + n4;
                final boolean contains = MiscUtils.contains(this.frame, this.getMinFrame(), this.getMaxFrame());
                this.frame = MiscUtils.clamp(this.frame, this.getMinFrame(), this.getMaxFrame());
                this.lastFrameTimeNs = n;
                this.notifyUpdate();
                if (contains ^ true) {
                    if (this.getRepeatCount() != -1 && this.repeatCount >= this.getRepeatCount()) {
                        float frame2;
                        if (this.speed < 0.0f) {
                            frame2 = this.getMinFrame();
                        }
                        else {
                            frame2 = this.getMaxFrame();
                        }
                        this.frame = frame2;
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
                            float frame3;
                            if (this.isReversed()) {
                                frame3 = this.getMaxFrame();
                            }
                            else {
                                frame3 = this.getMinFrame();
                            }
                            this.frame = frame3;
                        }
                        this.lastFrameTimeNs = n;
                    }
                }
                this.verifyFrame();
            }
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
        float n;
        float n2;
        float n3;
        if (this.isReversed()) {
            n = this.getMaxFrame() - this.frame;
            n2 = this.getMaxFrame();
            n3 = this.getMinFrame();
        }
        else {
            n = this.frame - this.getMinFrame();
            n2 = this.getMaxFrame();
            n3 = this.getMinFrame();
        }
        return n / (n2 - n3);
    }
    
    public Object getAnimatedValue() {
        return this.getAnimatedValueAbsolute();
    }
    
    public float getAnimatedValueAbsolute() {
        final LottieComposition composition = this.composition;
        if (composition == null) {
            return 0.0f;
        }
        return (this.frame - composition.getStartFrame()) / (this.composition.getEndFrame() - this.composition.getStartFrame());
    }
    
    public long getDuration() {
        final LottieComposition composition = this.composition;
        long n;
        if (composition == null) {
            n = 0L;
        }
        else {
            n = (long)composition.getDuration();
        }
        return n;
    }
    
    public float getFrame() {
        return this.frame;
    }
    
    public float getMaxFrame() {
        final LottieComposition composition = this.composition;
        if (composition == null) {
            return 0.0f;
        }
        float n;
        if ((n = this.maxFrame) == 2.14748365E9f) {
            n = composition.getEndFrame();
        }
        return n;
    }
    
    public float getMinFrame() {
        final LottieComposition composition = this.composition;
        if (composition == null) {
            return 0.0f;
        }
        float n;
        if ((n = this.minFrame) == -2.14748365E9f) {
            n = composition.getStartFrame();
        }
        return n;
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    public void pauseAnimation() {
        this.removeFrameCallback();
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
        this.lastFrameTimeNs = 0L;
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
    
    public void resumeAnimation() {
        this.running = true;
        this.postFrameCallback();
        this.lastFrameTimeNs = 0L;
        if (this.isReversed() && this.getFrame() == this.getMinFrame()) {
            this.frame = this.getMaxFrame();
        }
        else if (!this.isReversed() && this.getFrame() == this.getMaxFrame()) {
            this.frame = this.getMinFrame();
        }
    }
    
    public void reverseAnimationSpeed() {
        this.setSpeed(-this.getSpeed());
    }
    
    public void setComposition(final LottieComposition composition) {
        final boolean b = this.composition == null;
        this.composition = composition;
        if (b) {
            this.setMinAndMaxFrames((float)(int)Math.max(this.minFrame, composition.getStartFrame()), (float)(int)Math.min(this.maxFrame, composition.getEndFrame()));
        }
        else {
            this.setMinAndMaxFrames((float)(int)composition.getStartFrame(), (float)(int)composition.getEndFrame());
        }
        final float frame = this.frame;
        this.frame = 0.0f;
        this.setFrame((int)frame);
    }
    
    public void setFrame(final int n) {
        final float frame = this.frame;
        final float n2 = (float)n;
        if (frame == n2) {
            return;
        }
        this.frame = MiscUtils.clamp(n2, this.getMinFrame(), this.getMaxFrame());
        this.lastFrameTimeNs = 0L;
        this.notifyUpdate();
    }
    
    public void setMaxFrame(final float n) {
        this.setMinAndMaxFrames(this.minFrame, n);
    }
    
    public void setMinAndMaxFrames(final float f, final float f2) {
        if (f <= f2) {
            final LottieComposition composition = this.composition;
            float startFrame;
            if (composition == null) {
                startFrame = -3.4028235E38f;
            }
            else {
                startFrame = composition.getStartFrame();
            }
            final LottieComposition composition2 = this.composition;
            float endFrame;
            if (composition2 == null) {
                endFrame = Float.MAX_VALUE;
            }
            else {
                endFrame = composition2.getEndFrame();
            }
            this.minFrame = MiscUtils.clamp(f, startFrame, endFrame);
            this.maxFrame = MiscUtils.clamp(f2, startFrame, endFrame);
            this.setFrame((int)MiscUtils.clamp(this.frame, f, f2));
            return;
        }
        throw new IllegalArgumentException(String.format("minFrame (%s) must be <= maxFrame (%s)", f, f2));
    }
    
    public void setMinFrame(final int n) {
        this.setMinAndMaxFrames((float)n, (float)(int)this.maxFrame);
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
