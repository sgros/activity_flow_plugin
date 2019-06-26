package com.airbnb.lottie.animation.keyframe;

import android.graphics.Matrix;
import android.graphics.PointF;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.Collections;

public class TransformKeyframeAnimation {
    private BaseKeyframeAnimation<PointF, PointF> anchorPoint;
    private BaseKeyframeAnimation<?, Float> endOpacity;
    private final Matrix matrix = new Matrix();
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

    public TransformKeyframeAnimation(AnimatableTransform animatableTransform) {
        this.anchorPoint = animatableTransform.getAnchorPoint() == null ? null : animatableTransform.getAnchorPoint().createAnimation();
        this.position = animatableTransform.getPosition() == null ? null : animatableTransform.getPosition().createAnimation();
        this.scale = animatableTransform.getScale() == null ? null : animatableTransform.getScale().createAnimation();
        this.rotation = animatableTransform.getRotation() == null ? null : animatableTransform.getRotation().createAnimation();
        this.skew = animatableTransform.getSkew() == null ? null : (FloatKeyframeAnimation) animatableTransform.getSkew().createAnimation();
        if (this.skew != null) {
            this.skewMatrix1 = new Matrix();
            this.skewMatrix2 = new Matrix();
            this.skewMatrix3 = new Matrix();
            this.skewValues = new float[9];
        } else {
            this.skewMatrix1 = null;
            this.skewMatrix2 = null;
            this.skewMatrix3 = null;
            this.skewValues = null;
        }
        this.skewAngle = animatableTransform.getSkewAngle() == null ? null : (FloatKeyframeAnimation) animatableTransform.getSkewAngle().createAnimation();
        if (animatableTransform.getOpacity() != null) {
            this.opacity = animatableTransform.getOpacity().createAnimation();
        }
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

    public void addListener(AnimationListener animationListener) {
        BaseKeyframeAnimation baseKeyframeAnimation = this.opacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        baseKeyframeAnimation = this.startOpacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        baseKeyframeAnimation = this.endOpacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        baseKeyframeAnimation = this.anchorPoint;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        baseKeyframeAnimation = this.position;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        baseKeyframeAnimation = this.scale;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        baseKeyframeAnimation = this.rotation;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        FloatKeyframeAnimation floatKeyframeAnimation = this.skew;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.addUpdateListener(animationListener);
        }
        floatKeyframeAnimation = this.skewAngle;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.addUpdateListener(animationListener);
        }
    }

    public void setProgress(float f) {
        BaseKeyframeAnimation baseKeyframeAnimation = this.opacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        baseKeyframeAnimation = this.startOpacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        baseKeyframeAnimation = this.endOpacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        baseKeyframeAnimation = this.anchorPoint;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        baseKeyframeAnimation = this.position;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        baseKeyframeAnimation = this.scale;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        baseKeyframeAnimation = this.rotation;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        FloatKeyframeAnimation floatKeyframeAnimation = this.skew;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.setProgress(f);
        }
        floatKeyframeAnimation = this.skewAngle;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.setProgress(f);
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
        PointF pointF;
        float floatValue;
        this.matrix.reset();
        BaseKeyframeAnimation baseKeyframeAnimation = this.position;
        if (baseKeyframeAnimation != null) {
            pointF = (PointF) baseKeyframeAnimation.getValue();
            if (!(pointF.x == 0.0f && pointF.y == 0.0f)) {
                this.matrix.preTranslate(pointF.x, pointF.y);
            }
        }
        baseKeyframeAnimation = this.rotation;
        if (baseKeyframeAnimation != null) {
            if (baseKeyframeAnimation instanceof ValueCallbackKeyframeAnimation) {
                floatValue = ((Float) baseKeyframeAnimation.getValue()).floatValue();
            } else {
                floatValue = ((FloatKeyframeAnimation) baseKeyframeAnimation).getFloatValue();
            }
            if (floatValue != 0.0f) {
                this.matrix.preRotate(floatValue);
            }
        }
        if (this.skew != null) {
            FloatKeyframeAnimation floatKeyframeAnimation = this.skewAngle;
            floatValue = floatKeyframeAnimation == null ? 0.0f : (float) Math.cos(Math.toRadians((double) ((-floatKeyframeAnimation.getFloatValue()) + 90.0f)));
            FloatKeyframeAnimation floatKeyframeAnimation2 = this.skewAngle;
            float sin = floatKeyframeAnimation2 == null ? 1.0f : (float) Math.sin(Math.toRadians((double) ((-floatKeyframeAnimation2.getFloatValue()) + 90.0f)));
            float tan = (float) Math.tan(Math.toRadians((double) this.skew.getFloatValue()));
            clearSkewValues();
            float[] fArr = this.skewValues;
            fArr[0] = floatValue;
            fArr[1] = sin;
            float f = -sin;
            fArr[3] = f;
            fArr[4] = floatValue;
            fArr[8] = 1.0f;
            this.skewMatrix1.setValues(fArr);
            clearSkewValues();
            fArr = this.skewValues;
            fArr[0] = 1.0f;
            fArr[3] = tan;
            fArr[4] = 1.0f;
            fArr[8] = 1.0f;
            this.skewMatrix2.setValues(fArr);
            clearSkewValues();
            float[] fArr2 = this.skewValues;
            fArr2[0] = floatValue;
            fArr2[1] = f;
            fArr2[3] = sin;
            fArr2[4] = floatValue;
            fArr2[8] = 1.0f;
            this.skewMatrix3.setValues(fArr2);
            this.skewMatrix2.preConcat(this.skewMatrix1);
            this.skewMatrix3.preConcat(this.skewMatrix2);
            this.matrix.preConcat(this.skewMatrix3);
        }
        baseKeyframeAnimation = this.scale;
        if (baseKeyframeAnimation != null) {
            ScaleXY scaleXY = (ScaleXY) baseKeyframeAnimation.getValue();
            if (!(scaleXY.getScaleX() == 1.0f && scaleXY.getScaleY() == 1.0f)) {
                this.matrix.preScale(scaleXY.getScaleX(), scaleXY.getScaleY());
            }
        }
        baseKeyframeAnimation = this.anchorPoint;
        if (baseKeyframeAnimation != null) {
            pointF = (PointF) baseKeyframeAnimation.getValue();
            if (!(pointF.x == 0.0f && pointF.y == 0.0f)) {
                this.matrix.preTranslate(-pointF.x, -pointF.y);
            }
        }
        return this.matrix;
    }

    private void clearSkewValues() {
        for (int i = 0; i < 9; i++) {
            this.skewValues[i] = 0.0f;
        }
    }

    public Matrix getMatrixForRepeater(float f) {
        BaseKeyframeAnimation baseKeyframeAnimation = this.position;
        PointF pointF = null;
        PointF pointF2 = baseKeyframeAnimation == null ? null : (PointF) baseKeyframeAnimation.getValue();
        BaseKeyframeAnimation baseKeyframeAnimation2 = this.scale;
        ScaleXY scaleXY = baseKeyframeAnimation2 == null ? null : (ScaleXY) baseKeyframeAnimation2.getValue();
        this.matrix.reset();
        if (pointF2 != null) {
            this.matrix.preTranslate(pointF2.x * f, pointF2.y * f);
        }
        if (scaleXY != null) {
            double d = (double) f;
            this.matrix.preScale((float) Math.pow((double) scaleXY.getScaleX(), d), (float) Math.pow((double) scaleXY.getScaleY(), d));
        }
        baseKeyframeAnimation = this.rotation;
        if (baseKeyframeAnimation != null) {
            float floatValue = ((Float) baseKeyframeAnimation.getValue()).floatValue();
            baseKeyframeAnimation2 = this.anchorPoint;
            if (baseKeyframeAnimation2 != null) {
                pointF = (PointF) baseKeyframeAnimation2.getValue();
            }
            Matrix matrix = this.matrix;
            floatValue *= f;
            f = 0.0f;
            float f2 = pointF == null ? 0.0f : pointF.x;
            if (pointF != null) {
                f = pointF.y;
            }
            matrix.preRotate(floatValue, f2, f);
        }
        return this.matrix;
    }

    public <T> boolean applyValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        BaseKeyframeAnimation baseKeyframeAnimation;
        if (t == LottieProperty.TRANSFORM_ANCHOR_POINT) {
            baseKeyframeAnimation = this.anchorPoint;
            if (baseKeyframeAnimation == null) {
                this.anchorPoint = new ValueCallbackKeyframeAnimation(lottieValueCallback, new PointF());
            } else {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
            }
        } else if (t == LottieProperty.TRANSFORM_POSITION) {
            baseKeyframeAnimation = this.position;
            if (baseKeyframeAnimation == null) {
                this.position = new ValueCallbackKeyframeAnimation(lottieValueCallback, new PointF());
            } else {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
            }
        } else if (t == LottieProperty.TRANSFORM_SCALE) {
            baseKeyframeAnimation = this.scale;
            if (baseKeyframeAnimation == null) {
                this.scale = new ValueCallbackKeyframeAnimation(lottieValueCallback, new ScaleXY());
            } else {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
            }
        } else if (t == LottieProperty.TRANSFORM_ROTATION) {
            baseKeyframeAnimation = this.rotation;
            if (baseKeyframeAnimation == null) {
                this.rotation = new ValueCallbackKeyframeAnimation(lottieValueCallback, Float.valueOf(0.0f));
            } else {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
            }
        } else if (t == LottieProperty.TRANSFORM_OPACITY) {
            baseKeyframeAnimation = this.opacity;
            if (baseKeyframeAnimation == null) {
                this.opacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, Integer.valueOf(100));
            } else {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
            }
        } else {
            BaseKeyframeAnimation baseKeyframeAnimation2;
            if (t == LottieProperty.TRANSFORM_START_OPACITY) {
                baseKeyframeAnimation2 = this.startOpacity;
                if (baseKeyframeAnimation2 != null) {
                    if (baseKeyframeAnimation2 == null) {
                        this.startOpacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, Integer.valueOf(100));
                    } else {
                        baseKeyframeAnimation2.setValueCallback(lottieValueCallback);
                    }
                }
            }
            if (t == LottieProperty.TRANSFORM_END_OPACITY) {
                baseKeyframeAnimation2 = this.endOpacity;
                if (baseKeyframeAnimation2 != null) {
                    if (baseKeyframeAnimation2 == null) {
                        this.endOpacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, Integer.valueOf(100));
                    } else {
                        baseKeyframeAnimation2.setValueCallback(lottieValueCallback);
                    }
                }
            }
            if (t == LottieProperty.TRANSFORM_SKEW) {
                FloatKeyframeAnimation floatKeyframeAnimation = this.skew;
                if (floatKeyframeAnimation != null) {
                    if (floatKeyframeAnimation == null) {
                        this.skew = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
                    }
                    this.skew.setValueCallback(lottieValueCallback);
                }
            }
            if (t == LottieProperty.TRANSFORM_SKEW_ANGLE) {
                FloatKeyframeAnimation floatKeyframeAnimation2 = this.skewAngle;
                if (floatKeyframeAnimation2 != null) {
                    if (floatKeyframeAnimation2 == null) {
                        this.skewAngle = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
                    }
                    this.skewAngle.setValueCallback(lottieValueCallback);
                }
            }
            return false;
        }
        return true;
    }
}
