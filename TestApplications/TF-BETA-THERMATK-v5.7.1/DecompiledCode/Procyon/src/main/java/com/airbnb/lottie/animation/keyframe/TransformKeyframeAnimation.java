// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.keyframe;

import java.util.Collections;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.value.ScaleXY;
import android.graphics.Matrix;
import android.graphics.PointF;

public class TransformKeyframeAnimation
{
    private BaseKeyframeAnimation<PointF, PointF> anchorPoint;
    private BaseKeyframeAnimation<?, Float> endOpacity;
    private final Matrix matrix;
    private BaseKeyframeAnimation<Integer, Integer> opacity;
    private BaseKeyframeAnimation<?, PointF> position;
    private BaseKeyframeAnimation<Float, Float> rotation;
    private BaseKeyframeAnimation<ScaleXY, ScaleXY> scale;
    private FloatKeyframeAnimation skew;
    private FloatKeyframeAnimation skewAngle;
    private final Matrix skewMatrix1;
    private final Matrix skewMatrix2;
    private final Matrix skewMatrix3;
    private final float[] skewValues;
    private BaseKeyframeAnimation<?, Float> startOpacity;
    
    public TransformKeyframeAnimation(final AnimatableTransform animatableTransform) {
        this.matrix = new Matrix();
        BaseKeyframeAnimation<PointF, PointF> animation;
        if (animatableTransform.getAnchorPoint() == null) {
            animation = null;
        }
        else {
            animation = animatableTransform.getAnchorPoint().createAnimation();
        }
        this.anchorPoint = animation;
        BaseKeyframeAnimation<?, PointF> animation2;
        if (animatableTransform.getPosition() == null) {
            animation2 = null;
        }
        else {
            animation2 = animatableTransform.getPosition().createAnimation();
        }
        this.position = animation2;
        BaseKeyframeAnimation<ScaleXY, ScaleXY> animation3;
        if (animatableTransform.getScale() == null) {
            animation3 = null;
        }
        else {
            animation3 = animatableTransform.getScale().createAnimation();
        }
        this.scale = animation3;
        BaseKeyframeAnimation<Float, Float> animation4;
        if (animatableTransform.getRotation() == null) {
            animation4 = null;
        }
        else {
            animation4 = animatableTransform.getRotation().createAnimation();
        }
        this.rotation = animation4;
        FloatKeyframeAnimation skew;
        if (animatableTransform.getSkew() == null) {
            skew = null;
        }
        else {
            skew = (FloatKeyframeAnimation)animatableTransform.getSkew().createAnimation();
        }
        this.skew = skew;
        if (this.skew != null) {
            this.skewMatrix1 = new Matrix();
            this.skewMatrix2 = new Matrix();
            this.skewMatrix3 = new Matrix();
            this.skewValues = new float[9];
        }
        else {
            this.skewMatrix1 = null;
            this.skewMatrix2 = null;
            this.skewMatrix3 = null;
            this.skewValues = null;
        }
        FloatKeyframeAnimation skewAngle;
        if (animatableTransform.getSkewAngle() == null) {
            skewAngle = null;
        }
        else {
            skewAngle = (FloatKeyframeAnimation)animatableTransform.getSkewAngle().createAnimation();
        }
        this.skewAngle = skewAngle;
        if (animatableTransform.getOpacity() != null) {
            this.opacity = animatableTransform.getOpacity().createAnimation();
        }
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
    
    private void clearSkewValues() {
        for (int i = 0; i < 9; ++i) {
            this.skewValues[i] = 0.0f;
        }
    }
    
    public void addAnimationsToLayer(final BaseLayer baseLayer) {
        baseLayer.addAnimation(this.opacity);
        baseLayer.addAnimation(this.startOpacity);
        baseLayer.addAnimation(this.endOpacity);
        baseLayer.addAnimation(this.anchorPoint);
        baseLayer.addAnimation(this.position);
        baseLayer.addAnimation(this.scale);
        baseLayer.addAnimation(this.rotation);
        baseLayer.addAnimation(this.skew);
        baseLayer.addAnimation(this.skewAngle);
    }
    
    public void addListener(final BaseKeyframeAnimation.AnimationListener animationListener) {
        final BaseKeyframeAnimation<Integer, Integer> opacity = this.opacity;
        if (opacity != null) {
            opacity.addUpdateListener(animationListener);
        }
        final BaseKeyframeAnimation<?, Float> startOpacity = this.startOpacity;
        if (startOpacity != null) {
            startOpacity.addUpdateListener(animationListener);
        }
        final BaseKeyframeAnimation<?, Float> endOpacity = this.endOpacity;
        if (endOpacity != null) {
            endOpacity.addUpdateListener(animationListener);
        }
        final BaseKeyframeAnimation<PointF, PointF> anchorPoint = this.anchorPoint;
        if (anchorPoint != null) {
            anchorPoint.addUpdateListener(animationListener);
        }
        final BaseKeyframeAnimation<?, PointF> position = this.position;
        if (position != null) {
            position.addUpdateListener(animationListener);
        }
        final BaseKeyframeAnimation<ScaleXY, ScaleXY> scale = this.scale;
        if (scale != null) {
            scale.addUpdateListener(animationListener);
        }
        final BaseKeyframeAnimation<Float, Float> rotation = this.rotation;
        if (rotation != null) {
            rotation.addUpdateListener(animationListener);
        }
        final FloatKeyframeAnimation skew = this.skew;
        if (skew != null) {
            skew.addUpdateListener(animationListener);
        }
        final FloatKeyframeAnimation skewAngle = this.skewAngle;
        if (skewAngle != null) {
            skewAngle.addUpdateListener(animationListener);
        }
    }
    
    public <T> boolean applyValueCallback(final T t, final LottieValueCallback<T> valueCallback) {
        if (t == LottieProperty.TRANSFORM_ANCHOR_POINT) {
            final BaseKeyframeAnimation<PointF, PointF> anchorPoint = this.anchorPoint;
            if (anchorPoint == null) {
                this.anchorPoint = new ValueCallbackKeyframeAnimation<PointF, PointF>((LottieValueCallback<PointF>)valueCallback, new PointF());
            }
            else {
                anchorPoint.setValueCallback((LottieValueCallback<PointF>)valueCallback);
            }
        }
        else if (t == LottieProperty.TRANSFORM_POSITION) {
            final BaseKeyframeAnimation<?, PointF> position = this.position;
            if (position == null) {
                this.position = new ValueCallbackKeyframeAnimation<Object, PointF>((LottieValueCallback<Object>)valueCallback, new PointF());
            }
            else {
                position.setValueCallback((LottieValueCallback<PointF>)valueCallback);
            }
        }
        else if (t == LottieProperty.TRANSFORM_SCALE) {
            final BaseKeyframeAnimation<ScaleXY, ScaleXY> scale = this.scale;
            if (scale == null) {
                this.scale = new ValueCallbackKeyframeAnimation<ScaleXY, ScaleXY>((LottieValueCallback<ScaleXY>)valueCallback, new ScaleXY());
            }
            else {
                scale.setValueCallback((LottieValueCallback<ScaleXY>)valueCallback);
            }
        }
        else if (t == LottieProperty.TRANSFORM_ROTATION) {
            final BaseKeyframeAnimation<Float, Float> rotation = this.rotation;
            if (rotation == null) {
                this.rotation = new ValueCallbackKeyframeAnimation<Float, Float>((LottieValueCallback<Float>)valueCallback, 0.0f);
            }
            else {
                rotation.setValueCallback((LottieValueCallback<Float>)valueCallback);
            }
        }
        else {
            if (t != LottieProperty.TRANSFORM_OPACITY) {
                if (t == LottieProperty.TRANSFORM_START_OPACITY) {
                    final BaseKeyframeAnimation<?, Float> startOpacity = this.startOpacity;
                    if (startOpacity != null) {
                        if (startOpacity == null) {
                            this.startOpacity = new ValueCallbackKeyframeAnimation<Object, Float>((LottieValueCallback<Float>)valueCallback, Float.valueOf(100));
                            return true;
                        }
                        startOpacity.setValueCallback((LottieValueCallback<Float>)valueCallback);
                        return true;
                    }
                }
                if (t == LottieProperty.TRANSFORM_END_OPACITY) {
                    final BaseKeyframeAnimation<?, Float> endOpacity = this.endOpacity;
                    if (endOpacity != null) {
                        if (endOpacity == null) {
                            this.endOpacity = new ValueCallbackKeyframeAnimation<Object, Float>((LottieValueCallback<Float>)valueCallback, Float.valueOf(100));
                            return true;
                        }
                        endOpacity.setValueCallback((LottieValueCallback<Float>)valueCallback);
                        return true;
                    }
                }
                if (t == LottieProperty.TRANSFORM_SKEW) {
                    final FloatKeyframeAnimation skew = this.skew;
                    if (skew != null) {
                        if (skew == null) {
                            this.skew = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe<Float>(0.0f)));
                        }
                        ((BaseKeyframeAnimation<K, T>)this.skew).setValueCallback((LottieValueCallback<T>)valueCallback);
                        return true;
                    }
                }
                if (t == LottieProperty.TRANSFORM_SKEW_ANGLE) {
                    final FloatKeyframeAnimation skewAngle = this.skewAngle;
                    if (skewAngle != null) {
                        if (skewAngle == null) {
                            this.skewAngle = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe<Float>(0.0f)));
                        }
                        ((BaseKeyframeAnimation<K, T>)this.skewAngle).setValueCallback((LottieValueCallback<T>)valueCallback);
                        return true;
                    }
                }
                return false;
            }
            final BaseKeyframeAnimation<Integer, Integer> opacity = this.opacity;
            if (opacity == null) {
                this.opacity = new ValueCallbackKeyframeAnimation<Integer, Integer>((LottieValueCallback<Integer>)valueCallback, 100);
            }
            else {
                opacity.setValueCallback((LottieValueCallback<Integer>)valueCallback);
            }
        }
        return true;
    }
    
    public BaseKeyframeAnimation<?, Float> getEndOpacity() {
        return this.endOpacity;
    }
    
    public Matrix getMatrix() {
        this.matrix.reset();
        final BaseKeyframeAnimation<?, PointF> position = this.position;
        if (position != null) {
            final PointF pointF = position.getValue();
            if (pointF.x != 0.0f || pointF.y != 0.0f) {
                this.matrix.preTranslate(pointF.x, pointF.y);
            }
        }
        final BaseKeyframeAnimation<Float, Float> rotation = this.rotation;
        if (rotation != null) {
            float n;
            if (rotation instanceof ValueCallbackKeyframeAnimation) {
                n = rotation.getValue();
            }
            else {
                n = ((FloatKeyframeAnimation)rotation).getFloatValue();
            }
            if (n != 0.0f) {
                this.matrix.preRotate(n);
            }
        }
        if (this.skew != null) {
            final FloatKeyframeAnimation skewAngle = this.skewAngle;
            float n2;
            if (skewAngle == null) {
                n2 = 0.0f;
            }
            else {
                n2 = (float)Math.cos(Math.toRadians(-skewAngle.getFloatValue() + 90.0f));
            }
            final FloatKeyframeAnimation skewAngle2 = this.skewAngle;
            float n3;
            if (skewAngle2 == null) {
                n3 = 1.0f;
            }
            else {
                n3 = (float)Math.sin(Math.toRadians(-skewAngle2.getFloatValue() + 90.0f));
            }
            final float n4 = (float)Math.tan(Math.toRadians(this.skew.getFloatValue()));
            this.clearSkewValues();
            final float[] skewValues = this.skewValues;
            skewValues[0] = n2;
            skewValues[1] = n3;
            final float n5 = -n3;
            skewValues[3] = n5;
            skewValues[4] = n2;
            skewValues[8] = 1.0f;
            this.skewMatrix1.setValues(skewValues);
            this.clearSkewValues();
            final float[] skewValues2 = this.skewValues;
            skewValues2[0] = 1.0f;
            skewValues2[3] = n4;
            skewValues2[8] = (skewValues2[4] = 1.0f);
            this.skewMatrix2.setValues(skewValues2);
            this.clearSkewValues();
            final float[] skewValues3 = this.skewValues;
            skewValues3[0] = n2;
            skewValues3[1] = n5;
            skewValues3[3] = n3;
            skewValues3[4] = n2;
            skewValues3[8] = 1.0f;
            this.skewMatrix3.setValues(skewValues3);
            this.skewMatrix2.preConcat(this.skewMatrix1);
            this.skewMatrix3.preConcat(this.skewMatrix2);
            this.matrix.preConcat(this.skewMatrix3);
        }
        final BaseKeyframeAnimation<ScaleXY, ScaleXY> scale = this.scale;
        if (scale != null) {
            final ScaleXY scaleXY = scale.getValue();
            if (scaleXY.getScaleX() != 1.0f || scaleXY.getScaleY() != 1.0f) {
                this.matrix.preScale(scaleXY.getScaleX(), scaleXY.getScaleY());
            }
        }
        final BaseKeyframeAnimation<PointF, PointF> anchorPoint = this.anchorPoint;
        if (anchorPoint != null) {
            final PointF pointF2 = anchorPoint.getValue();
            if (pointF2.x != 0.0f || pointF2.y != 0.0f) {
                this.matrix.preTranslate(-pointF2.x, -pointF2.y);
            }
        }
        return this.matrix;
    }
    
    public Matrix getMatrixForRepeater(final float n) {
        final BaseKeyframeAnimation<?, PointF> position = this.position;
        final PointF pointF = null;
        PointF pointF2;
        if (position == null) {
            pointF2 = null;
        }
        else {
            pointF2 = position.getValue();
        }
        final BaseKeyframeAnimation<ScaleXY, ScaleXY> scale = this.scale;
        ScaleXY scaleXY;
        if (scale == null) {
            scaleXY = null;
        }
        else {
            scaleXY = scale.getValue();
        }
        this.matrix.reset();
        if (pointF2 != null) {
            this.matrix.preTranslate(pointF2.x * n, pointF2.y * n);
        }
        if (scaleXY != null) {
            final Matrix matrix = this.matrix;
            final double a = scaleXY.getScaleX();
            final double n2 = n;
            matrix.preScale((float)Math.pow(a, n2), (float)Math.pow(scaleXY.getScaleY(), n2));
        }
        final BaseKeyframeAnimation<Float, Float> rotation = this.rotation;
        if (rotation != null) {
            final float floatValue = rotation.getValue();
            final BaseKeyframeAnimation<PointF, PointF> anchorPoint = this.anchorPoint;
            PointF pointF3;
            if (anchorPoint == null) {
                pointF3 = pointF;
            }
            else {
                pointF3 = anchorPoint.getValue();
            }
            final Matrix matrix2 = this.matrix;
            float y = 0.0f;
            float x;
            if (pointF3 == null) {
                x = 0.0f;
            }
            else {
                x = pointF3.x;
            }
            if (pointF3 != null) {
                y = pointF3.y;
            }
            matrix2.preRotate(floatValue * n, x, y);
        }
        return this.matrix;
    }
    
    public BaseKeyframeAnimation<?, Integer> getOpacity() {
        return this.opacity;
    }
    
    public BaseKeyframeAnimation<?, Float> getStartOpacity() {
        return this.startOpacity;
    }
    
    public void setProgress(final float progress) {
        final BaseKeyframeAnimation<Integer, Integer> opacity = this.opacity;
        if (opacity != null) {
            opacity.setProgress(progress);
        }
        final BaseKeyframeAnimation<?, Float> startOpacity = this.startOpacity;
        if (startOpacity != null) {
            startOpacity.setProgress(progress);
        }
        final BaseKeyframeAnimation<?, Float> endOpacity = this.endOpacity;
        if (endOpacity != null) {
            endOpacity.setProgress(progress);
        }
        final BaseKeyframeAnimation<PointF, PointF> anchorPoint = this.anchorPoint;
        if (anchorPoint != null) {
            anchorPoint.setProgress(progress);
        }
        final BaseKeyframeAnimation<?, PointF> position = this.position;
        if (position != null) {
            position.setProgress(progress);
        }
        final BaseKeyframeAnimation<ScaleXY, ScaleXY> scale = this.scale;
        if (scale != null) {
            scale.setProgress(progress);
        }
        final BaseKeyframeAnimation<Float, Float> rotation = this.rotation;
        if (rotation != null) {
            rotation.setProgress(progress);
        }
        final FloatKeyframeAnimation skew = this.skew;
        if (skew != null) {
            skew.setProgress(progress);
        }
        final FloatKeyframeAnimation skewAngle = this.skewAngle;
        if (skewAngle != null) {
            skewAngle.setProgress(progress);
        }
    }
}
