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
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;

public class PolystarContent implements PathContent, AnimationListener, KeyPathElementContent {
    private final boolean hidden;
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
    private CompoundTrimPathContent trimPaths = new CompoundTrimPathContent();
    private final Type type;

    /* renamed from: com.airbnb.lottie.animation.content.PolystarContent$1 */
    static /* synthetic */ class C01211 {
        static final /* synthetic */ int[] $SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type = new int[Type.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Missing block: B:7:?, code skipped:
            return;
     */
        static {
            /*
            r0 = com.airbnb.lottie.model.content.PolystarShape.Type.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type = r0;
            r0 = $SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = com.airbnb.lottie.model.content.PolystarShape.Type.STAR;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r0 = $SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = com.airbnb.lottie.model.content.PolystarShape.Type.POLYGON;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.animation.content.PolystarContent$C01211.<clinit>():void");
        }
    }

    public PolystarContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, PolystarShape polystarShape) {
        this.lottieDrawable = lottieDrawable;
        this.name = polystarShape.getName();
        this.type = polystarShape.getType();
        this.hidden = polystarShape.isHidden();
        this.pointsAnimation = polystarShape.getPoints().createAnimation();
        this.positionAnimation = polystarShape.getPosition().createAnimation();
        this.rotationAnimation = polystarShape.getRotation().createAnimation();
        this.outerRadiusAnimation = polystarShape.getOuterRadius().createAnimation();
        this.outerRoundednessAnimation = polystarShape.getOuterRoundedness().createAnimation();
        if (this.type == Type.STAR) {
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
        if (this.type == Type.STAR) {
            baseLayer.addAnimation(this.innerRadiusAnimation);
            baseLayer.addAnimation(this.innerRoundednessAnimation);
        }
        this.pointsAnimation.addUpdateListener(this);
        this.positionAnimation.addUpdateListener(this);
        this.rotationAnimation.addUpdateListener(this);
        this.outerRadiusAnimation.addUpdateListener(this);
        this.outerRoundednessAnimation.addUpdateListener(this);
        if (this.type == Type.STAR) {
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
                if (trimPathContent.getType() == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                    this.trimPaths.addTrimPath(trimPathContent);
                    trimPathContent.addListener(this);
                }
            }
        }
    }

    public Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        int i = C01211.$SwitchMap$com$airbnb$lottie$model$content$PolystarShape$Type[this.type.ordinal()];
        if (i == 1) {
            createStarPath();
        } else if (i == 2) {
            createPolygonPath();
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    public String getName() {
        return this.name;
    }

    private void createStarPath() {
        float f;
        double d;
        float f2;
        float f3;
        float f4;
        float f5;
        double cos;
        float floatValue = ((Float) this.pointsAnimation.getValue()).floatValue();
        BaseKeyframeAnimation baseKeyframeAnimation = this.rotationAnimation;
        double toRadians = Math.toRadians((baseKeyframeAnimation == null ? 0.0d : (double) ((Float) baseKeyframeAnimation.getValue()).floatValue()) - 90.0d);
        double d2 = (double) floatValue;
        Double.isNaN(d2);
        float f6 = (float) (6.283185307179586d / d2);
        float f7 = f6 / 2.0f;
        floatValue -= (float) ((int) floatValue);
        if (floatValue != 0.0f) {
            double d3 = (double) ((1.0f - floatValue) * f7);
            Double.isNaN(d3);
            toRadians += d3;
        }
        float floatValue2 = ((Float) this.outerRadiusAnimation.getValue()).floatValue();
        float floatValue3 = ((Float) this.innerRadiusAnimation.getValue()).floatValue();
        BaseKeyframeAnimation baseKeyframeAnimation2 = this.innerRoundednessAnimation;
        float floatValue4 = baseKeyframeAnimation2 != null ? ((Float) baseKeyframeAnimation2.getValue()).floatValue() / 100.0f : 0.0f;
        BaseKeyframeAnimation baseKeyframeAnimation3 = this.outerRoundednessAnimation;
        float floatValue5 = baseKeyframeAnimation3 != null ? ((Float) baseKeyframeAnimation3.getValue()).floatValue() / 100.0f : 0.0f;
        double d4;
        if (floatValue != 0.0f) {
            f = ((floatValue2 - floatValue3) * floatValue) + floatValue3;
            float f8 = floatValue2;
            d4 = (double) f;
            double cos2 = Math.cos(toRadians);
            Double.isNaN(d4);
            d = d2;
            f2 = (float) (d4 * cos2);
            cos2 = Math.sin(toRadians);
            Double.isNaN(d4);
            f3 = (float) (d4 * cos2);
            this.path.moveTo(f2, f3);
            d4 = (double) ((f6 * floatValue) / 2.0f);
            Double.isNaN(d4);
            toRadians += d4;
            f4 = f2;
            f5 = f;
            f2 = f8;
            float f9 = f7;
            f7 = f3;
            f3 = f9;
        } else {
            d = d2;
            f2 = floatValue2;
            d4 = (double) f2;
            cos = Math.cos(toRadians);
            Double.isNaN(d4);
            f3 = f7;
            f4 = (float) (d4 * cos);
            cos = Math.sin(toRadians);
            Double.isNaN(d4);
            f7 = (float) (d4 * cos);
            this.path.moveTo(f4, f7);
            d4 = (double) f3;
            Double.isNaN(d4);
            toRadians += d4;
            f5 = 0.0f;
        }
        cos = Math.ceil(d) * 2.0d;
        int i = 0;
        double d5 = toRadians;
        int i2 = 0;
        while (true) {
            double d6 = (double) i;
            if (d6 < cos) {
                float f10;
                float f11;
                float f12;
                float f13;
                float f14;
                float f15;
                float f16 = i2 != 0 ? f2 : floatValue3;
                if (f5 == 0.0f || d6 != cos - 2.0d) {
                    f10 = f16;
                    f16 = f3;
                } else {
                    f10 = f16;
                    f16 = (f6 * floatValue) / 2.0f;
                }
                if (f5 == 0.0f || d6 != cos - 1.0d) {
                    f11 = f6;
                    f6 = f10;
                    f10 = f2;
                } else {
                    f11 = f6;
                    f10 = f2;
                    f6 = f5;
                }
                double d7 = (double) f6;
                double cos3 = Math.cos(d5);
                Double.isNaN(d7);
                double d8 = d6;
                f = (float) (d7 * cos3);
                cos3 = Math.sin(d5);
                Double.isNaN(d7);
                f6 = (float) (d7 * cos3);
                if (floatValue4 == 0.0f && floatValue5 == 0.0f) {
                    this.path.lineTo(f, f6);
                    f12 = f6;
                    f13 = f3;
                    f14 = floatValue3;
                    f15 = floatValue4;
                    f3 = floatValue5;
                    f6 = f16;
                } else {
                    f13 = f3;
                    f14 = floatValue3;
                    f15 = floatValue4;
                    double atan2 = (double) ((float) (Math.atan2((double) f7, (double) f4) - 1.5707963267948966d));
                    floatValue3 = (float) Math.cos(atan2);
                    f2 = (float) Math.sin(atan2);
                    f3 = floatValue5;
                    float f17 = f16;
                    f12 = f6;
                    double atan22 = (double) ((float) (Math.atan2((double) f6, (double) f) - 1.5707963267948966d));
                    floatValue4 = (float) Math.cos(atan22);
                    f16 = (float) Math.sin(atan22);
                    f6 = i2 != 0 ? f15 : f3;
                    float f18 = ((i2 != 0 ? f14 : f10) * f6) * 0.47829f;
                    floatValue3 *= f18;
                    f18 *= f2;
                    float f19 = ((i2 != 0 ? f10 : f14) * (i2 != 0 ? f3 : f15)) * 0.47829f;
                    floatValue4 *= f19;
                    f19 *= f16;
                    if (floatValue != 0.0f) {
                        if (i == 0) {
                            floatValue3 *= floatValue;
                            f18 *= floatValue;
                        } else if (d8 == cos - 1.0d) {
                            floatValue4 *= floatValue;
                            f19 *= floatValue;
                        }
                    }
                    this.path.cubicTo(f4 - floatValue3, f7 - f18, f + floatValue4, f12 + f19, f, f12);
                    f6 = f17;
                }
                d7 = (double) f6;
                Double.isNaN(d7);
                d5 += d7;
                i2 ^= 1;
                i++;
                floatValue5 = f3;
                f4 = f;
                f3 = f13;
                f2 = f10;
                f6 = f11;
                floatValue3 = f14;
                floatValue4 = f15;
                f7 = f12;
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
        BaseKeyframeAnimation baseKeyframeAnimation = this.rotationAnimation;
        double toRadians = Math.toRadians((baseKeyframeAnimation == null ? 0.0d : (double) ((Float) baseKeyframeAnimation.getValue()).floatValue()) - 90.0d);
        double d = (double) floor;
        Double.isNaN(d);
        float f = (float) (6.283185307179586d / d);
        float floatValue = ((Float) this.outerRoundednessAnimation.getValue()).floatValue() / 100.0f;
        float floatValue2 = ((Float) this.outerRadiusAnimation.getValue()).floatValue();
        double d2 = (double) floatValue2;
        double cos = Math.cos(toRadians);
        Double.isNaN(d2);
        float f2 = (float) (cos * d2);
        double sin = Math.sin(toRadians);
        Double.isNaN(d2);
        float f3 = (float) (sin * d2);
        this.path.moveTo(f2, f3);
        double d3 = (double) f;
        Double.isNaN(d3);
        toRadians += d3;
        d = Math.ceil(d);
        floor = 0;
        while (((double) floor) < d) {
            double d4;
            int i;
            double d5;
            double d6;
            double cos2 = Math.cos(toRadians);
            Double.isNaN(d2);
            float f4 = (float) (cos2 * d2);
            double sin2 = Math.sin(toRadians);
            Double.isNaN(d2);
            double d7 = d;
            float f5 = (float) (d2 * sin2);
            if (floatValue != 0.0f) {
                d4 = d2;
                i = floor;
                d5 = toRadians;
                double atan2 = (double) ((float) (Math.atan2((double) f3, (double) f2) - 1.5707963267948966d));
                d6 = d3;
                double atan22 = (double) ((float) (Math.atan2((double) f5, (double) f4) - 1.5707963267948966d));
                float f6 = (floatValue2 * floatValue) * 0.25f;
                this.path.cubicTo(f2 - (((float) Math.cos(atan2)) * f6), f3 - (((float) Math.sin(atan2)) * f6), f4 + (((float) Math.cos(atan22)) * f6), f5 + (f6 * ((float) Math.sin(atan22))), f4, f5);
            } else {
                d5 = toRadians;
                d4 = d2;
                d6 = d3;
                i = floor;
                this.path.lineTo(f4, f5);
            }
            Double.isNaN(d6);
            toRadians = d5 + d6;
            floor = i + 1;
            f3 = f5;
            f2 = f4;
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
        } else {
            BaseKeyframeAnimation baseKeyframeAnimation;
            if (t == LottieProperty.POLYSTAR_INNER_RADIUS) {
                baseKeyframeAnimation = this.innerRadiusAnimation;
                if (baseKeyframeAnimation != null) {
                    baseKeyframeAnimation.setValueCallback(lottieValueCallback);
                    return;
                }
            }
            if (t == LottieProperty.POLYSTAR_OUTER_RADIUS) {
                this.outerRadiusAnimation.setValueCallback(lottieValueCallback);
                return;
            }
            if (t == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS) {
                baseKeyframeAnimation = this.innerRoundednessAnimation;
                if (baseKeyframeAnimation != null) {
                    baseKeyframeAnimation.setValueCallback(lottieValueCallback);
                    return;
                }
            }
            if (t == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
                this.outerRoundednessAnimation.setValueCallback(lottieValueCallback);
            }
        }
    }
}
