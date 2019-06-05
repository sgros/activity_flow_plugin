package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import com.airbnb.lottie.C0352L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeTrimPath.Type;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseStrokeContent implements DrawingContent, KeyPathElementContent, AnimationListener {
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
    private final List<BaseKeyframeAnimation<?, Float>> dashPatternAnimations;
    private final BaseKeyframeAnimation<?, Float> dashPatternOffsetAnimation;
    private final float[] dashPatternValues;
    private final BaseLayer layer;
    private final LottieDrawable lottieDrawable;
    private final BaseKeyframeAnimation<?, Integer> opacityAnimation;
    final Paint paint = new Paint(1);
    private final Path path = new Path();
    private final List<PathGroup> pathGroups = new ArrayList();
    /* renamed from: pm */
    private final PathMeasure f75pm = new PathMeasure();
    private final RectF rect = new RectF();
    private final Path trimPathPath = new Path();
    private final BaseKeyframeAnimation<?, Float> widthAnimation;

    private static final class PathGroup {
        private final List<PathContent> paths;
        private final TrimPathContent trimPath;

        private PathGroup(TrimPathContent trimPathContent) {
            this.paths = new ArrayList();
            this.trimPath = trimPathContent;
        }
    }

    BaseStrokeContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, Cap cap, Join join, float f, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue, List<AnimatableFloatValue> list, AnimatableFloatValue animatableFloatValue2) {
        int i;
        this.lottieDrawable = lottieDrawable;
        this.layer = baseLayer;
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeCap(cap);
        this.paint.setStrokeJoin(join);
        this.paint.setStrokeMiter(f);
        this.opacityAnimation = animatableIntegerValue.createAnimation();
        this.widthAnimation = animatableFloatValue.createAnimation();
        if (animatableFloatValue2 == null) {
            this.dashPatternOffsetAnimation = null;
        } else {
            this.dashPatternOffsetAnimation = animatableFloatValue2.createAnimation();
        }
        this.dashPatternAnimations = new ArrayList(list.size());
        this.dashPatternValues = new float[list.size()];
        for (i = 0; i < list.size(); i++) {
            this.dashPatternAnimations.add(((AnimatableFloatValue) list.get(i)).createAnimation());
        }
        baseLayer.addAnimation(this.opacityAnimation);
        baseLayer.addAnimation(this.widthAnimation);
        for (i = 0; i < this.dashPatternAnimations.size(); i++) {
            baseLayer.addAnimation((BaseKeyframeAnimation) this.dashPatternAnimations.get(i));
        }
        if (this.dashPatternOffsetAnimation != null) {
            baseLayer.addAnimation(this.dashPatternOffsetAnimation);
        }
        this.opacityAnimation.addUpdateListener(this);
        this.widthAnimation.addUpdateListener(this);
        for (int i2 = 0; i2 < list.size(); i2++) {
            ((BaseKeyframeAnimation) this.dashPatternAnimations.get(i2)).addUpdateListener(this);
        }
        if (this.dashPatternOffsetAnimation != null) {
            this.dashPatternOffsetAnimation.addUpdateListener(this);
        }
    }

    public void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }

    public void setContents(List<Content> list, List<Content> list2) {
        Content content;
        TrimPathContent trimPathContent = null;
        for (int size = list.size() - 1; size >= 0; size--) {
            content = (Content) list.get(size);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent2 = (TrimPathContent) content;
                if (trimPathContent2.getType() == Type.Individually) {
                    trimPathContent = trimPathContent2;
                }
            }
        }
        if (trimPathContent != null) {
            trimPathContent.addListener(this);
        }
        Object obj = null;
        for (int size2 = list2.size() - 1; size2 >= 0; size2--) {
            content = (Content) list2.get(size2);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent3 = (TrimPathContent) content;
                if (trimPathContent3.getType() == Type.Individually) {
                    if (obj != null) {
                        this.pathGroups.add(obj);
                    }
                    obj = new PathGroup(trimPathContent3);
                    trimPathContent3.addListener(this);
                }
            }
            if (content instanceof PathContent) {
                if (obj == null) {
                    obj = new PathGroup(trimPathContent);
                }
                obj.paths.add((PathContent) content);
            }
        }
        if (obj != null) {
            this.pathGroups.add(obj);
        }
    }

    public void draw(Canvas canvas, Matrix matrix, int i) {
        C0352L.beginSection("StrokeContent#draw");
        int i2 = 0;
        this.paint.setAlpha(MiscUtils.clamp((int) ((((((float) i) / 255.0f) * ((float) ((Integer) this.opacityAnimation.getValue()).intValue())) / 100.0f) * 255.0f), 0, 255));
        this.paint.setStrokeWidth(((Float) this.widthAnimation.getValue()).floatValue() * Utils.getScale(matrix));
        if (this.paint.getStrokeWidth() <= 0.0f) {
            C0352L.endSection("StrokeContent#draw");
            return;
        }
        applyDashPatternIfNeeded(matrix);
        if (this.colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter) this.colorFilterAnimation.getValue());
        }
        while (i2 < this.pathGroups.size()) {
            PathGroup pathGroup = (PathGroup) this.pathGroups.get(i2);
            if (pathGroup.trimPath != null) {
                applyTrimPath(canvas, pathGroup, matrix);
            } else {
                C0352L.beginSection("StrokeContent#buildPath");
                this.path.reset();
                for (int size = pathGroup.paths.size() - 1; size >= 0; size--) {
                    this.path.addPath(((PathContent) pathGroup.paths.get(size)).getPath(), matrix);
                }
                C0352L.endSection("StrokeContent#buildPath");
                C0352L.beginSection("StrokeContent#drawPath");
                canvas.drawPath(this.path, this.paint);
                C0352L.endSection("StrokeContent#drawPath");
            }
            i2++;
        }
        C0352L.endSection("StrokeContent#draw");
    }

    private void applyTrimPath(Canvas canvas, PathGroup pathGroup, Matrix matrix) {
        C0352L.beginSection("StrokeContent#applyTrimPath");
        if (pathGroup.trimPath == null) {
            C0352L.endSection("StrokeContent#applyTrimPath");
            return;
        }
        this.path.reset();
        for (int size = pathGroup.paths.size() - 1; size >= 0; size--) {
            this.path.addPath(((PathContent) pathGroup.paths.get(size)).getPath(), matrix);
        }
        this.f75pm.setPath(this.path, false);
        float length = this.f75pm.getLength();
        while (this.f75pm.nextContour()) {
            length += this.f75pm.getLength();
        }
        float floatValue = (((Float) pathGroup.trimPath.getOffset().getValue()).floatValue() * length) / 360.0f;
        float floatValue2 = ((((Float) pathGroup.trimPath.getStart().getValue()).floatValue() * length) / 100.0f) + floatValue;
        float floatValue3 = ((((Float) pathGroup.trimPath.getEnd().getValue()).floatValue() * length) / 100.0f) + floatValue;
        float f = 0.0f;
        for (int size2 = pathGroup.paths.size() - 1; size2 >= 0; size2--) {
            float f2;
            this.trimPathPath.set(((PathContent) pathGroup.paths.get(size2)).getPath());
            this.trimPathPath.transform(matrix);
            this.f75pm.setPath(this.trimPathPath, false);
            float length2 = this.f75pm.getLength();
            float f3 = 1.0f;
            if (floatValue3 > length) {
                f2 = floatValue3 - length;
                if (f2 < f + length2 && f < f2) {
                    Utils.applyTrimPathIfNeeded(this.trimPathPath, floatValue2 > length ? (floatValue2 - length) / length2 : 0.0f, Math.min(f2 / length2, 1.0f), 0.0f);
                    canvas.drawPath(this.trimPathPath, this.paint);
                    f += length2;
                }
            }
            f2 = f + length2;
            if (f2 >= floatValue2 && f <= floatValue3) {
                if (f2 > floatValue3 || floatValue2 >= f) {
                    float f4 = floatValue2 < f ? 0.0f : (floatValue2 - f) / length2;
                    if (floatValue3 <= f2) {
                        f3 = (floatValue3 - f) / length2;
                    }
                    Utils.applyTrimPathIfNeeded(this.trimPathPath, f4, f3, 0.0f);
                    canvas.drawPath(this.trimPathPath, this.paint);
                } else {
                    canvas.drawPath(this.trimPathPath, this.paint);
                }
            }
            f += length2;
        }
        C0352L.endSection("StrokeContent#applyTrimPath");
    }

    public void getBounds(RectF rectF, Matrix matrix) {
        C0352L.beginSection("StrokeContent#getBounds");
        this.path.reset();
        for (int i = 0; i < this.pathGroups.size(); i++) {
            PathGroup pathGroup = (PathGroup) this.pathGroups.get(i);
            for (int i2 = 0; i2 < pathGroup.paths.size(); i2++) {
                this.path.addPath(((PathContent) pathGroup.paths.get(i2)).getPath(), matrix);
            }
        }
        this.path.computeBounds(this.rect, false);
        float floatValue = ((Float) this.widthAnimation.getValue()).floatValue() / 2.0f;
        this.rect.set(this.rect.left - floatValue, this.rect.top - floatValue, this.rect.right + floatValue, this.rect.bottom + floatValue);
        rectF.set(this.rect);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
        C0352L.endSection("StrokeContent#getBounds");
    }

    private void applyDashPatternIfNeeded(Matrix matrix) {
        C0352L.beginSection("StrokeContent#applyDashPattern");
        if (this.dashPatternAnimations.isEmpty()) {
            C0352L.endSection("StrokeContent#applyDashPattern");
            return;
        }
        float scale = Utils.getScale(matrix);
        for (int i = 0; i < this.dashPatternAnimations.size(); i++) {
            this.dashPatternValues[i] = ((Float) ((BaseKeyframeAnimation) this.dashPatternAnimations.get(i)).getValue()).floatValue();
            if (i % 2 == 0) {
                if (this.dashPatternValues[i] < 1.0f) {
                    this.dashPatternValues[i] = 1.0f;
                }
            } else if (this.dashPatternValues[i] < 0.1f) {
                this.dashPatternValues[i] = 0.1f;
            }
            float[] fArr = this.dashPatternValues;
            fArr[i] = fArr[i] * scale;
        }
        this.paint.setPathEffect(new DashPathEffect(this.dashPatternValues, this.dashPatternOffsetAnimation == null ? 0.0f : ((Float) this.dashPatternOffsetAnimation.getValue()).floatValue()));
        C0352L.endSection("StrokeContent#applyDashPattern");
    }

    public void resolveKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, i, list, keyPath2, this);
    }

    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.OPACITY) {
            this.opacityAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.STROKE_WIDTH) {
            this.widthAnimation.setValueCallback(lottieValueCallback);
        } else if (t != LottieProperty.COLOR_FILTER) {
        } else {
            if (lottieValueCallback == null) {
                this.colorFilterAnimation = null;
                return;
            }
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback);
            this.colorFilterAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorFilterAnimation);
        }
    }
}
