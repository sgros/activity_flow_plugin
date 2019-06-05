package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.model.content.PolystarShape.Type;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PolystarContent implements KeyPathElementContent, PathContent, AnimationListener {
    private final BaseKeyframeAnimation<?, Float> innerRadiusAnimation;
    private final BaseKeyframeAnimation<?, Float> innerRoundednessAnimation;
    private boolean isPathValid;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final BaseKeyframeAnimation<?, Float> outerRadiusAnimation;
    private final BaseKeyframeAnimation<?, Float> outerRoundednessAnimation;
    private final Path path = new Path();
    private final BaseKeyframeAnimation<?, Float> pointsAnimation;
    private final BaseKeyframeAnimation<?, PointF> positionAnimation;
    private final BaseKeyframeAnimation<?, Float> rotationAnimation;
    private TrimPathContent trimPath;
    private final Type type;

    public PolystarContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, PolystarShape polystarShape) {
        this.lottieDrawable = lottieDrawable;
        this.name = polystarShape.getName();
        this.type = polystarShape.getType();
        this.pointsAnimation = polystarShape.getPoints().createAnimation();
        this.positionAnimation = polystarShape.getPosition().createAnimation();
        this.rotationAnimation = polystarShape.getRotation().createAnimation();
        this.outerRadiusAnimation = polystarShape.getOuterRadius().createAnimation();
        this.outerRoundednessAnimation = polystarShape.getOuterRoundedness().createAnimation();
        if (this.type == Type.Star) {
            this.innerRadiusAnimation = polystarShape.getInnerRadius().createAnimation();
            this.innerRoundednessAnimation = polystarShape.getInnerRoundedness().createAnimation();
        } else {
            this.innerRadiusAnimation = null;
            this.innerRoundednessAnimation = null;
        }
        baseLayer.addAnimation(this.pointsAnimation);
        baseLayer.addAnimation(this.positionAnimation);
        baseLayer.addAnimation(this.rotationAnimation);
        baseLayer.addAnimation(this.outerRadiusAnimation);
        baseLayer.addAnimation(this.outerRoundednessAnimation);
        if (this.type == Type.Star) {
            baseLayer.addAnimation(this.innerRadiusAnimation);
            baseLayer.addAnimation(this.innerRoundednessAnimation);
        }
        this.pointsAnimation.addUpdateListener(this);
        this.positionAnimation.addUpdateListener(this);
        this.rotationAnimation.addUpdateListener(this);
        this.outerRadiusAnimation.addUpdateListener(this);
        this.outerRoundednessAnimation.addUpdateListener(this);
        if (this.type == Type.Star) {
            this.innerRadiusAnimation.addUpdateListener(this);
            this.innerRoundednessAnimation.addUpdateListener(this);
        }
    }

    public void onValueChanged() {
        invalidate();
    }

    private void invalidate() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }

    public void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < list.size(); i++) {
            Content content = (Content) list.get(i);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent = (TrimPathContent) content;
                if (trimPathContent.getType() == ShapeTrimPath.Type.Simultaneously) {
                    this.trimPath = trimPathContent;
                    this.trimPath.addListener(this);
                }
            }
        }
    }

    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        switch (this.type) {
            case Star:
                createStarPath();
                break;
            case Polygon:
                createPolygonPath();
                break;
        }
        this.path.close();
        Utils.applyTrimPathIfNeeded(this.path, this.trimPath);
        this.isPathValid = true;
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    private void createStarPath() {
        float f;
        int i;
        double d;
        double d2;
        float cos;
        float sin;
        float floatValue = ((Float) this.pointsAnimation.getValue()).floatValue();
        double toRadians = Math.toRadians((this.rotationAnimation == null ? 0.0d : (double) ((Float) this.rotationAnimation.getValue()).floatValue()) - 90.0d);
        double d3 = (double) floatValue;
        float f2 = (float) (6.283185307179586d / d3);
        float f3 = f2 / 2.0f;
        floatValue -= (float) ((int) floatValue);
        int i2 = (floatValue > 0.0f ? 1 : (floatValue == 0.0f ? 0 : -1));
        if (i2 != 0) {
            toRadians += (double) ((1.0f - floatValue) * f3);
        }
        float floatValue2 = ((Float) this.outerRadiusAnimation.getValue()).floatValue();
        float floatValue3 = ((Float) this.innerRadiusAnimation.getValue()).floatValue();
        float floatValue4 = this.innerRoundednessAnimation != null ? ((Float) this.innerRoundednessAnimation.getValue()).floatValue() / 100.0f : 0.0f;
        float floatValue5 = this.outerRoundednessAnimation != null ? ((Float) this.outerRoundednessAnimation.getValue()).floatValue() / 100.0f : 0.0f;
        if (i2 != 0) {
            f = ((floatValue2 - floatValue3) * floatValue) + floatValue3;
            i = i2;
            d = (double) f;
            d2 = d3;
            cos = (float) (d * Math.cos(toRadians));
            sin = (float) (d * Math.sin(toRadians));
            this.path.moveTo(cos, sin);
            toRadians += (double) ((f2 * floatValue) / 2.0f);
        } else {
            d2 = d3;
            i = i2;
            double d4 = (double) floatValue2;
            float cos2 = (float) (Math.cos(toRadians) * d4);
            sin = (float) (d4 * Math.sin(toRadians));
            this.path.moveTo(cos2, sin);
            toRadians += (double) f3;
            cos = cos2;
            f = 0.0f;
        }
        d = Math.ceil(d2) * 2.0d;
        int i3 = 0;
        d2 = toRadians;
        float f4 = floatValue2;
        float f5 = floatValue3;
        int i4 = 0;
        while (true) {
            double d5 = (double) i3;
            if (d5 < d) {
                float f6;
                float f7;
                float f8;
                float f9;
                double d6;
                float f10;
                float f11;
                float f12;
                float f13 = i4 != 0 ? f4 : f5;
                int i5 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i5 == 0 || d5 != d - 2.0d) {
                    f6 = f13;
                    f13 = f3;
                } else {
                    f6 = f13;
                    f13 = (f2 * floatValue) / 2.0f;
                }
                if (i5 == 0 || d5 != d - 1.0d) {
                    f7 = f13;
                    f8 = f2;
                    f2 = f6;
                } else {
                    f7 = f13;
                    f8 = f2;
                    f2 = f;
                }
                double d7 = (double) f2;
                double d8 = d5;
                floatValue2 = (float) (d7 * Math.cos(d2));
                f13 = (float) (d7 * Math.sin(d2));
                if (floatValue4 == 0.0f && floatValue5 == 0.0f) {
                    this.path.lineTo(floatValue2, f13);
                    f9 = f3;
                    d6 = d;
                    f10 = floatValue4;
                    f11 = floatValue5;
                    f12 = f;
                } else {
                    f10 = floatValue4;
                    f11 = floatValue5;
                    f12 = f;
                    double atan2 = (double) ((float) (Math.atan2((double) sin, (double) cos) - 1.5707963267948966d));
                    float f14 = cos;
                    f2 = (float) Math.cos(atan2);
                    cos = (float) Math.sin(atan2);
                    f9 = f3;
                    d6 = d;
                    double atan22 = (double) ((float) (Math.atan2((double) f13, (double) floatValue2) - 1.5707963267948966d));
                    float cos3 = (float) Math.cos(atan22);
                    f3 = (float) Math.sin(atan22);
                    float f15 = i4 != 0 ? f10 : f11;
                    floatValue4 = ((i4 != 0 ? f5 : f4) * f15) * 0.47829f;
                    f2 *= floatValue4;
                    floatValue4 *= cos;
                    floatValue5 = ((i4 != 0 ? f4 : f5) * (i4 != 0 ? f11 : f10)) * 0.47829f;
                    cos3 *= floatValue5;
                    floatValue5 *= f3;
                    if (i != 0) {
                        if (i3 == 0) {
                            f2 *= floatValue;
                            floatValue4 *= floatValue;
                        } else if (d8 == d6 - 1.0d) {
                            cos3 *= floatValue;
                            floatValue5 *= floatValue;
                        }
                    }
                    this.path.cubicTo(f14 - f2, sin - floatValue4, floatValue2 + cos3, f13 + floatValue5, floatValue2, f13);
                }
                d2 += (double) f7;
                i4 ^= 1;
                i3++;
                sin = f13;
                cos = floatValue2;
                f2 = f8;
                floatValue4 = f10;
                floatValue5 = f11;
                f = f12;
                f3 = f9;
                d = d6;
            } else {
                PointF pointF = (PointF) this.positionAnimation.getValue();
                this.path.offset(pointF.x, pointF.y);
                this.path.close();
                return;
            }
        }
    }

    private void createPolygonPath() {
        int floor = (int) Math.floor((double) ((Float) this.pointsAnimation.getValue()).floatValue());
        double toRadians = Math.toRadians((this.rotationAnimation == null ? 0.0d : (double) ((Float) this.rotationAnimation.getValue()).floatValue()) - 90.0d);
        double d = (double) floor;
        float f = (float) (6.283185307179586d / d);
        float floatValue = ((Float) this.outerRoundednessAnimation.getValue()).floatValue() / 100.0f;
        float floatValue2 = ((Float) this.outerRadiusAnimation.getValue()).floatValue();
        double d2 = (double) floatValue2;
        float cos = (float) (Math.cos(toRadians) * d2);
        float sin = (float) (Math.sin(toRadians) * d2);
        this.path.moveTo(cos, sin);
        double d3 = (double) f;
        toRadians += d3;
        d = Math.ceil(d);
        floor = 0;
        while (((double) floor) < d) {
            double d4;
            int i;
            double d5;
            double d6;
            float cos2 = (float) (Math.cos(toRadians) * d2);
            double d7 = d;
            float sin2 = (float) (d2 * Math.sin(toRadians));
            if (floatValue != 0.0f) {
                d4 = d2;
                i = floor;
                d5 = toRadians;
                double atan2 = (double) ((float) (Math.atan2((double) sin, (double) cos) - 1.5707963267948966d));
                d6 = d3;
                double atan22 = (double) ((float) (Math.atan2((double) sin2, (double) cos2) - 1.5707963267948966d));
                float f2 = (floatValue2 * floatValue) * 0.25f;
                this.path.cubicTo(cos - (((float) Math.cos(atan2)) * f2), sin - (((float) Math.sin(atan2)) * f2), cos2 + (((float) Math.cos(atan22)) * f2), sin2 + (f2 * ((float) Math.sin(atan22))), cos2, sin2);
            } else {
                i = floor;
                d5 = toRadians;
                d4 = d2;
                d6 = d3;
                this.path.lineTo(cos2, sin2);
            }
            toRadians = d5 + d6;
            floor = i + 1;
            sin = sin2;
            cos = cos2;
            d = d7;
            d2 = d4;
            d3 = d6;
        }
        PointF pointF = (PointF) this.positionAnimation.getValue();
        this.path.offset(pointF.x, pointF.y);
        this.path.close();
    }

    public void resolveKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, i, list, keyPath2, this);
    }

    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.POLYSTAR_POINTS) {
            this.pointsAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_ROTATION) {
            this.rotationAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_INNER_RADIUS && this.innerRadiusAnimation != null) {
            this.innerRadiusAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_OUTER_RADIUS) {
            this.outerRadiusAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS && this.innerRoundednessAnimation != null) {
            this.innerRoundednessAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
            this.outerRoundednessAnimation.setValueCallback(lottieValueCallback);
        }
    }
}
