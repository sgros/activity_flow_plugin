package com.airbnb.lottie.animation.keyframe;

import android.graphics.Matrix;
import android.graphics.PointF;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;

public class TransformKeyframeAnimation {
    private final BaseKeyframeAnimation<PointF, PointF> anchorPoint;
    private final BaseKeyframeAnimation<?, Float> endOpacity;
    private final Matrix matrix = new Matrix();
    private final BaseKeyframeAnimation<Integer, Integer> opacity;
    private final BaseKeyframeAnimation<?, PointF> position;
    private final BaseKeyframeAnimation<Float, Float> rotation;
    private final BaseKeyframeAnimation<ScaleXY, ScaleXY> scale;
    private final BaseKeyframeAnimation<?, Float> startOpacity;

    public TransformKeyframeAnimation(AnimatableTransform animatableTransform) {
        this.anchorPoint = animatableTransform.getAnchorPoint().createAnimation();
        this.position = animatableTransform.getPosition().createAnimation();
        this.scale = animatableTransform.getScale().createAnimation();
        this.rotation = animatableTransform.getRotation().createAnimation();
        this.opacity = animatableTransform.getOpacity().createAnimation();
        if (animatableTransform.getStartOpacity() != null) {
            this.startOpacity = animatableTransform.getStartOpacity().createAnimation();
        } else {
            this.startOpacity = null;
        }
        if (animatableTransform.getEndOpacity() != null) {
            this.endOpacity = animatableTransform.getEndOpacity().createAnimation();
        } else {
            this.endOpacity = null;
        }
    }

    public void addAnimationsToLayer(BaseLayer baseLayer) {
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

    public void addListener(AnimationListener animationListener) {
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

    public void setProgress(float f) {
        this.anchorPoint.setProgress(f);
        this.position.setProgress(f);
        this.scale.setProgress(f);
        this.rotation.setProgress(f);
        this.opacity.setProgress(f);
        if (this.startOpacity != null) {
            this.startOpacity.setProgress(f);
        }
        if (this.endOpacity != null) {
            this.endOpacity.setProgress(f);
        }
    }

    public BaseKeyframeAnimation<?, Integer> getOpacity() {
        return this.opacity;
    }

    public BaseKeyframeAnimation<?, Float> getStartOpacity() {
        return this.startOpacity;
    }

    public BaseKeyframeAnimation<?, Float> getEndOpacity() {
        return this.endOpacity;
    }

    public Matrix getMatrix() {
        this.matrix.reset();
        PointF pointF = (PointF) this.position.getValue();
        if (!(pointF.x == 0.0f && pointF.y == 0.0f)) {
            this.matrix.preTranslate(pointF.x, pointF.y);
        }
        float floatValue = ((Float) this.rotation.getValue()).floatValue();
        if (floatValue != 0.0f) {
            this.matrix.preRotate(floatValue);
        }
        ScaleXY scaleXY = (ScaleXY) this.scale.getValue();
        if (!(scaleXY.getScaleX() == 1.0f && scaleXY.getScaleY() == 1.0f)) {
            this.matrix.preScale(scaleXY.getScaleX(), scaleXY.getScaleY());
        }
        pointF = (PointF) this.anchorPoint.getValue();
        if (!(pointF.x == 0.0f && pointF.y == 0.0f)) {
            this.matrix.preTranslate(-pointF.x, -pointF.y);
        }
        return this.matrix;
    }

    public Matrix getMatrixForRepeater(float f) {
        PointF pointF = (PointF) this.position.getValue();
        PointF pointF2 = (PointF) this.anchorPoint.getValue();
        ScaleXY scaleXY = (ScaleXY) this.scale.getValue();
        float floatValue = ((Float) this.rotation.getValue()).floatValue();
        this.matrix.reset();
        this.matrix.preTranslate(pointF.x * f, pointF.y * f);
        double d = (double) f;
        this.matrix.preScale((float) Math.pow((double) scaleXY.getScaleX(), d), (float) Math.pow((double) scaleXY.getScaleY(), d));
        this.matrix.preRotate(floatValue * f, pointF2.x, pointF2.y);
        return this.matrix;
    }

    public <T> boolean applyValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.TRANSFORM_ANCHOR_POINT) {
            this.anchorPoint.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.TRANSFORM_POSITION) {
            this.position.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.TRANSFORM_SCALE) {
            this.scale.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.TRANSFORM_ROTATION) {
            this.rotation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.TRANSFORM_OPACITY) {
            this.opacity.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.TRANSFORM_START_OPACITY && this.startOpacity != null) {
            this.startOpacity.setValueCallback(lottieValueCallback);
        } else if (t != LottieProperty.TRANSFORM_END_OPACITY || this.endOpacity == null) {
            return false;
        } else {
            this.endOpacity.setValueCallback(lottieValueCallback);
        }
        return true;
    }
}
