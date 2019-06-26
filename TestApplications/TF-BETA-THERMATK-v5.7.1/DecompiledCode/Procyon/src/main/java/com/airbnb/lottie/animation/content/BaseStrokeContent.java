// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.animation.keyframe.IntegerKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import android.graphics.Canvas;
import android.graphics.PathEffect;
import android.graphics.DashPathEffect;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.L;
import android.graphics.Matrix;
import android.graphics.Paint$Style;
import com.airbnb.lottie.animation.LPaint;
import java.util.ArrayList;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import android.graphics.Paint$Join;
import android.graphics.Paint$Cap;
import android.graphics.RectF;
import android.graphics.PathMeasure;
import android.graphics.Path;
import android.graphics.Paint;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.List;
import android.graphics.ColorFilter;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public abstract class BaseStrokeContent implements AnimationListener, KeyPathElementContent, DrawingContent
{
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
    private final List<BaseKeyframeAnimation<?, Float>> dashPatternAnimations;
    private final BaseKeyframeAnimation<?, Float> dashPatternOffsetAnimation;
    private final float[] dashPatternValues;
    protected final BaseLayer layer;
    private final LottieDrawable lottieDrawable;
    private final BaseKeyframeAnimation<?, Integer> opacityAnimation;
    final Paint paint;
    private final Path path;
    private final List<PathGroup> pathGroups;
    private final PathMeasure pm;
    private final RectF rect;
    private final Path trimPathPath;
    private final BaseKeyframeAnimation<?, Float> widthAnimation;
    
    BaseStrokeContent(final LottieDrawable lottieDrawable, final BaseLayer layer, final Paint$Cap strokeCap, final Paint$Join strokeJoin, final float strokeMiter, final AnimatableIntegerValue animatableIntegerValue, final AnimatableFloatValue animatableFloatValue, final List<AnimatableFloatValue> list, final AnimatableFloatValue animatableFloatValue2) {
        this.pm = new PathMeasure();
        this.path = new Path();
        this.trimPathPath = new Path();
        this.rect = new RectF();
        this.pathGroups = new ArrayList<PathGroup>();
        this.paint = new LPaint(1);
        this.lottieDrawable = lottieDrawable;
        this.layer = layer;
        this.paint.setStyle(Paint$Style.STROKE);
        this.paint.setStrokeCap(strokeCap);
        this.paint.setStrokeJoin(strokeJoin);
        this.paint.setStrokeMiter(strokeMiter);
        this.opacityAnimation = animatableIntegerValue.createAnimation();
        this.widthAnimation = animatableFloatValue.createAnimation();
        if (animatableFloatValue2 == null) {
            this.dashPatternOffsetAnimation = null;
        }
        else {
            this.dashPatternOffsetAnimation = animatableFloatValue2.createAnimation();
        }
        this.dashPatternAnimations = new ArrayList<BaseKeyframeAnimation<?, Float>>(list.size());
        this.dashPatternValues = new float[list.size()];
        final int n = 0;
        for (int i = 0; i < list.size(); ++i) {
            this.dashPatternAnimations.add(list.get(i).createAnimation());
        }
        layer.addAnimation(this.opacityAnimation);
        layer.addAnimation(this.widthAnimation);
        for (int j = 0; j < this.dashPatternAnimations.size(); ++j) {
            layer.addAnimation(this.dashPatternAnimations.get(j));
        }
        final BaseKeyframeAnimation<?, Float> dashPatternOffsetAnimation = this.dashPatternOffsetAnimation;
        if (dashPatternOffsetAnimation != null) {
            layer.addAnimation(dashPatternOffsetAnimation);
        }
        this.opacityAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.widthAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        for (int k = n; k < list.size(); ++k) {
            this.dashPatternAnimations.get(k).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        }
        final BaseKeyframeAnimation<?, Float> dashPatternOffsetAnimation2 = this.dashPatternOffsetAnimation;
        if (dashPatternOffsetAnimation2 != null) {
            dashPatternOffsetAnimation2.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        }
    }
    
    private void applyDashPatternIfNeeded(final Matrix matrix) {
        L.beginSection("StrokeContent#applyDashPattern");
        if (this.dashPatternAnimations.isEmpty()) {
            L.endSection("StrokeContent#applyDashPattern");
            return;
        }
        final float scale = Utils.getScale(matrix);
        for (int i = 0; i < this.dashPatternAnimations.size(); ++i) {
            this.dashPatternValues[i] = this.dashPatternAnimations.get(i).getValue();
            if (i % 2 == 0) {
                final float[] dashPatternValues = this.dashPatternValues;
                if (dashPatternValues[i] < 1.0f) {
                    dashPatternValues[i] = 1.0f;
                }
            }
            else {
                final float[] dashPatternValues2 = this.dashPatternValues;
                if (dashPatternValues2[i] < 0.1f) {
                    dashPatternValues2[i] = 0.1f;
                }
            }
            final float[] dashPatternValues3 = this.dashPatternValues;
            dashPatternValues3[i] *= scale;
        }
        final BaseKeyframeAnimation<?, Float> dashPatternOffsetAnimation = this.dashPatternOffsetAnimation;
        float floatValue;
        if (dashPatternOffsetAnimation == null) {
            floatValue = 0.0f;
        }
        else {
            floatValue = dashPatternOffsetAnimation.getValue();
        }
        this.paint.setPathEffect((PathEffect)new DashPathEffect(this.dashPatternValues, floatValue));
        L.endSection("StrokeContent#applyDashPattern");
    }
    
    private void applyTrimPath(final Canvas canvas, final PathGroup pathGroup, final Matrix matrix) {
        L.beginSection("StrokeContent#applyTrimPath");
        if (pathGroup.trimPath == null) {
            L.endSection("StrokeContent#applyTrimPath");
            return;
        }
        this.path.reset();
        for (int i = pathGroup.paths.size() - 1; i >= 0; --i) {
            this.path.addPath(((PathContent)pathGroup.paths.get(i)).getPath(), matrix);
        }
        this.pm.setPath(this.path, false);
        float length = this.pm.getLength();
        while (this.pm.nextContour()) {
            length += this.pm.getLength();
        }
        final float n = pathGroup.trimPath.getOffset().getValue() * length / 360.0f;
        final float n2 = pathGroup.trimPath.getStart().getValue() * length / 100.0f + n;
        final float n3 = pathGroup.trimPath.getEnd().getValue() * length / 100.0f + n;
        int j = pathGroup.paths.size() - 1;
        float n4 = 0.0f;
        while (j >= 0) {
            this.trimPathPath.set(((PathContent)pathGroup.paths.get(j)).getPath());
            this.trimPathPath.transform(matrix);
            this.pm.setPath(this.trimPathPath, false);
            final float length2 = this.pm.getLength();
            float n5 = 1.0f;
            Label_0502: {
                if (n3 > length) {
                    final float n6 = n3 - length;
                    if (n6 < n4 + length2 && n4 < n6) {
                        float n7;
                        if (n2 > length) {
                            n7 = (n2 - length) / length2;
                        }
                        else {
                            n7 = 0.0f;
                        }
                        Utils.applyTrimPathIfNeeded(this.trimPathPath, n7, Math.min(n6 / length2, 1.0f), 0.0f);
                        canvas.drawPath(this.trimPathPath, this.paint);
                        break Label_0502;
                    }
                }
                final float n8 = n4 + length2;
                if (n8 >= n2) {
                    if (n4 <= n3) {
                        if (n8 <= n3 && n2 < n4) {
                            canvas.drawPath(this.trimPathPath, this.paint);
                        }
                        else {
                            float n9;
                            if (n2 < n4) {
                                n9 = 0.0f;
                            }
                            else {
                                n9 = (n2 - n4) / length2;
                            }
                            if (n3 <= n8) {
                                n5 = (n3 - n4) / length2;
                            }
                            Utils.applyTrimPathIfNeeded(this.trimPathPath, n9, n5, 0.0f);
                            canvas.drawPath(this.trimPathPath, this.paint);
                        }
                    }
                }
            }
            n4 += length2;
            --j;
        }
        L.endSection("StrokeContent#applyTrimPath");
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.OPACITY) {
            this.opacityAnimation.setValueCallback((LottieValueCallback<Integer>)lottieValueCallback);
        }
        else if (t == LottieProperty.STROKE_WIDTH) {
            this.widthAnimation.setValueCallback((LottieValueCallback<Float>)lottieValueCallback);
        }
        else if (t == LottieProperty.COLOR_FILTER) {
            if (lottieValueCallback == null) {
                this.colorFilterAnimation = null;
            }
            else {
                (this.colorFilterAnimation = new ValueCallbackKeyframeAnimation<ColorFilter, ColorFilter>((LottieValueCallback<ColorFilter>)lottieValueCallback)).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                this.layer.addAnimation(this.colorFilterAnimation);
            }
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, int i) {
        L.beginSection("StrokeContent#draw");
        if (Utils.hasZeroScaleAxis(matrix)) {
            L.endSection("StrokeContent#draw");
            return;
        }
        i = (int)(i / 255.0f * ((IntegerKeyframeAnimation)this.opacityAnimation).getIntValue() / 100.0f * 255.0f);
        final Paint paint = this.paint;
        final int n = 0;
        paint.setAlpha(MiscUtils.clamp(i, 0, 255));
        this.paint.setStrokeWidth(((FloatKeyframeAnimation)this.widthAnimation).getFloatValue() * Utils.getScale(matrix));
        if (this.paint.getStrokeWidth() <= 0.0f) {
            L.endSection("StrokeContent#draw");
            return;
        }
        this.applyDashPatternIfNeeded(matrix);
        final BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation = this.colorFilterAnimation;
        i = n;
        if (colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter)colorFilterAnimation.getValue());
            i = n;
        }
        while (i < this.pathGroups.size()) {
            final PathGroup pathGroup = this.pathGroups.get(i);
            if (pathGroup.trimPath != null) {
                this.applyTrimPath(canvas, pathGroup, matrix);
            }
            else {
                L.beginSection("StrokeContent#buildPath");
                this.path.reset();
                for (int j = pathGroup.paths.size() - 1; j >= 0; --j) {
                    this.path.addPath(((PathContent)pathGroup.paths.get(j)).getPath(), matrix);
                }
                L.endSection("StrokeContent#buildPath");
                L.beginSection("StrokeContent#drawPath");
                canvas.drawPath(this.path, this.paint);
                L.endSection("StrokeContent#drawPath");
            }
            ++i;
        }
        L.endSection("StrokeContent#draw");
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        L.beginSection("StrokeContent#getBounds");
        this.path.reset();
        for (int i = 0; i < this.pathGroups.size(); ++i) {
            final PathGroup pathGroup = this.pathGroups.get(i);
            for (int j = 0; j < pathGroup.paths.size(); ++j) {
                this.path.addPath(((PathContent)pathGroup.paths.get(j)).getPath(), matrix);
            }
        }
        this.path.computeBounds(this.rect, false);
        final float floatValue = ((FloatKeyframeAnimation)this.widthAnimation).getFloatValue();
        final RectF rect = this.rect;
        final float left = rect.left;
        final float n = floatValue / 2.0f;
        rect.set(left - n, rect.top - n, rect.right + n, rect.bottom + n);
        rectF.set(this.rect);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
        L.endSection("StrokeContent#getBounds");
    }
    
    @Override
    public void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }
    
    @Override
    public void resolveKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, n, list, keyPath2, this);
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
        int i = list.size() - 1;
        TrimPathContent trimPathContent = null;
        while (i >= 0) {
            final Content content = list.get(i);
            TrimPathContent trimPathContent2 = trimPathContent;
            if (content instanceof TrimPathContent) {
                final TrimPathContent trimPathContent3 = (TrimPathContent)content;
                trimPathContent2 = trimPathContent;
                if (trimPathContent3.getType() == ShapeTrimPath.Type.INDIVIDUALLY) {
                    trimPathContent2 = trimPathContent3;
                }
            }
            --i;
            trimPathContent = trimPathContent2;
        }
        if (trimPathContent != null) {
            trimPathContent.addListener(this);
        }
        int j = list2.size() - 1;
        PathGroup pathGroup = null;
        while (j >= 0) {
            final Content content2 = list2.get(j);
            PathGroup pathGroup2 = null;
            Label_0222: {
                if (content2 instanceof TrimPathContent) {
                    final TrimPathContent trimPathContent4 = (TrimPathContent)content2;
                    if (trimPathContent4.getType() == ShapeTrimPath.Type.INDIVIDUALLY) {
                        if (pathGroup != null) {
                            this.pathGroups.add(pathGroup);
                        }
                        pathGroup2 = new PathGroup(trimPathContent4);
                        trimPathContent4.addListener(this);
                        break Label_0222;
                    }
                }
                pathGroup2 = pathGroup;
                if (content2 instanceof PathContent) {
                    if ((pathGroup2 = pathGroup) == null) {
                        pathGroup2 = new PathGroup(trimPathContent);
                    }
                    pathGroup2.paths.add(content2);
                }
            }
            --j;
            pathGroup = pathGroup2;
        }
        if (pathGroup != null) {
            this.pathGroups.add(pathGroup);
        }
    }
    
    private static final class PathGroup
    {
        private final List<PathContent> paths;
        private final TrimPathContent trimPath;
        
        private PathGroup(final TrimPathContent trimPath) {
            this.paths = new ArrayList<PathContent>();
            this.trimPath = trimPath;
        }
    }
}
