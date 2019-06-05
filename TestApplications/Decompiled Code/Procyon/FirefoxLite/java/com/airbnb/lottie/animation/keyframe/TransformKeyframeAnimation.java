// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.value.ScaleXY;
import android.graphics.Matrix;
import android.graphics.PointF;

public class TransformKeyframeAnimation
{
    private final BaseKeyframeAnimation<PointF, PointF> anchorPoint;
    private final BaseKeyframeAnimation<?, Float> endOpacity;
    private final Matrix matrix;
    private final BaseKeyframeAnimation<Integer, Integer> opacity;
    private final BaseKeyframeAnimation<?, PointF> position;
    private final BaseKeyframeAnimation<Float, Float> rotation;
    private final BaseKeyframeAnimation<ScaleXY, ScaleXY> scale;
    private final BaseKeyframeAnimation<?, Float> startOpacity;
    
    public TransformKeyframeAnimation(final AnimatableTransform animatableTransform) {
        this.matrix = new Matrix();
        this.anchorPoint = animatableTransform.getAnchorPoint().createAnimation();
        this.position = animatableTransform.getPosition().createAnimation();
        this.scale = animatableTransform.getScale().createAnimation();
        this.rotation = animatableTransform.getRotation().createAnimation();
        this.opacity = animatableTransform.getOpacity().createAnimation();
        if (animatableTransform.getStartOpacity() != null) {
            this.startOpacity = animatableTransform.getStartOpacity().createAnimation();
        }
        else {
            this.startOpacity = null;
        }
        if (animatableTransform.getEndOpacity() != null) {
            this.endOpacity = animatableTransform.getEndOpacity().createAnimation();
        }
        else {
            this.endOpacity = null;
        }
    }
    
    public void addAnimationsToLayer(final BaseLayer baseLayer) {
        baseLayer.addAnimation(this.anchorPoint);
        baseLayer.addAnimation(this.position);
        baseLayer.addAnimation(this.scale);
        baseLayer.addAnimation(this.rotation);
        baseLayer.addAnimation(this.opacity);
        if (this.startOpacity != null) {
            baseLayer.addAnimation(this.startOpacity);
        }
        if (this.endOpacity != null) {
            baseLayer.addAnimation(this.endOpacity);
        }
    }
    
    public void addListener(final BaseKeyframeAnimation.AnimationListener animationListener) {
        this.anchorPoint.addUpdateListener(animationListener);
        this.position.addUpdateListener(animationListener);
        this.scale.addUpdateListener(animationListener);
        this.rotation.addUpdateListener(animationListener);
        this.opacity.addUpdateListener(animationListener);
        if (this.startOpacity != null) {
            this.startOpacity.addUpdateListener(animationListener);
        }
        if (this.endOpacity != null) {
            this.endOpacity.addUpdateListener(animationListener);
        }
    }
    
    public <T> boolean applyValueCallback(final T t, final LottieValueCallback<T> valueCallback) {
        if (t == LottieProperty.TRANSFORM_ANCHOR_POINT) {
            this.anchorPoint.setValueCallback((LottieValueCallback<PointF>)valueCallback);
        }
        else if (t == LottieProperty.TRANSFORM_POSITION) {
            this.position.setValueCallback((LottieValueCallback<PointF>)valueCallback);
        }
        else if (t == LottieProperty.TRANSFORM_SCALE) {
            this.scale.setValueCallback((LottieValueCallback<ScaleXY>)valueCallback);
        }
        else if (t == LottieProperty.TRANSFORM_ROTATION) {
            this.rotation.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        else if (t == LottieProperty.TRANSFORM_OPACITY) {
            this.opacity.setValueCallback((LottieValueCallback<Integer>)valueCallback);
        }
        else if (t == LottieProperty.TRANSFORM_START_OPACITY && this.startOpacity != null) {
            this.startOpacity.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        else {
            if (t != LottieProperty.TRANSFORM_END_OPACITY || this.endOpacity == null) {
                return false;
            }
            this.endOpacity.setValueCallback((LottieValueCallback<Float>)valueCallback);
        }
        return true;
    }
    
    public BaseKeyframeAnimation<?, Float> getEndOpacity() {
        return this.endOpacity;
    }
    
    public Matrix getMatrix() {
        this.matrix.reset();
        final PointF pointF = this.position.getValue();
        if (pointF.x != 0.0f || pointF.y != 0.0f) {
            this.matrix.preTranslate(pointF.x, pointF.y);
        }
        final float floatValue = this.rotation.getValue();
        if (floatValue != 0.0f) {
            this.matrix.preRotate(floatValue);
        }
        final ScaleXY scaleXY = this.scale.getValue();
        if (scaleXY.getScaleX() != 1.0f || scaleXY.getScaleY() != 1.0f) {
            this.matrix.preScale(scaleXY.getScaleX(), scaleXY.getScaleY());
        }
        final PointF pointF2 = this.anchorPoint.getValue();
        if (pointF2.x != 0.0f || pointF2.y != 0.0f) {
            this.matrix.preTranslate(-pointF2.x, -pointF2.y);
        }
        return this.matrix;
    }
    
    public Matrix getMatrixForRepeater(final float n) {
        final PointF pointF = this.position.getValue();
        final PointF pointF2 = this.anchorPoint.getValue();
        final ScaleXY scaleXY = this.scale.getValue();
        final float floatValue = this.rotation.getValue();
        this.matrix.reset();
        this.matrix.preTranslate(pointF.x * n, pointF.y * n);
        final Matrix matrix = this.matrix;
        final double a = scaleXY.getScaleX();
        final double n2 = n;
        matrix.preScale((float)Math.pow(a, n2), (float)Math.pow(scaleXY.getScaleY(), n2));
        this.matrix.preRotate(floatValue * n, pointF2.x, pointF2.y);
        return this.matrix;
    }
    
    public BaseKeyframeAnimation<?, Integer> getOpacity() {
        return this.opacity;
    }
    
    public BaseKeyframeAnimation<?, Float> getStartOpacity() {
        return this.startOpacity;
    }
    
    public void setProgress(final float progress) {
        this.anchorPoint.setProgress(progress);
        this.position.setProgress(progress);
        this.scale.setProgress(progress);
        this.rotation.setProgress(progress);
        this.opacity.setProgress(progress);
        if (this.startOpacity != null) {
            this.startOpacity.setProgress(progress);
        }
        if (this.endOpacity != null) {
            this.endOpacity.setProgress(progress);
        }
    }
}
