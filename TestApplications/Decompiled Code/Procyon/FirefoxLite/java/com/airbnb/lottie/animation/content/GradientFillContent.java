// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.utils.MiscUtils;
import android.graphics.Shader;
import com.airbnb.lottie.L;
import android.graphics.Canvas;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import android.graphics.Shader$TileMode;
import java.util.ArrayList;
import com.airbnb.lottie.model.content.GradientFill;
import com.airbnb.lottie.model.content.GradientType;
import android.graphics.Matrix;
import android.graphics.RadialGradient;
import java.util.List;
import android.graphics.Path;
import android.graphics.Paint;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.LinearGradient;
import android.support.v4.util.LongSparseArray;
import com.airbnb.lottie.model.layer.BaseLayer;
import android.graphics.PointF;
import android.graphics.ColorFilter;
import com.airbnb.lottie.model.content.GradientColor;
import android.graphics.RectF;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;

public class GradientFillContent implements DrawingContent, KeyPathElementContent, AnimationListener
{
    private final RectF boundsRect;
    private final int cacheSteps;
    private final BaseKeyframeAnimation<GradientColor, GradientColor> colorAnimation;
    private BaseKeyframeAnimation<ColorFilter, ColorFilter> colorFilterAnimation;
    private final BaseKeyframeAnimation<PointF, PointF> endPointAnimation;
    private final BaseLayer layer;
    private final LongSparseArray<LinearGradient> linearGradientCache;
    private final LottieDrawable lottieDrawable;
    private final String name;
    private final BaseKeyframeAnimation<Integer, Integer> opacityAnimation;
    private final Paint paint;
    private final Path path;
    private final List<PathContent> paths;
    private final LongSparseArray<RadialGradient> radialGradientCache;
    private final Matrix shaderMatrix;
    private final BaseKeyframeAnimation<PointF, PointF> startPointAnimation;
    private final GradientType type;
    
    public GradientFillContent(final LottieDrawable lottieDrawable, final BaseLayer layer, final GradientFill gradientFill) {
        this.linearGradientCache = new LongSparseArray<LinearGradient>();
        this.radialGradientCache = new LongSparseArray<RadialGradient>();
        this.shaderMatrix = new Matrix();
        this.path = new Path();
        this.paint = new Paint(1);
        this.boundsRect = new RectF();
        this.paths = new ArrayList<PathContent>();
        this.layer = layer;
        this.name = gradientFill.getName();
        this.lottieDrawable = lottieDrawable;
        this.type = gradientFill.getGradientType();
        this.path.setFillType(gradientFill.getFillType());
        this.cacheSteps = (int)(lottieDrawable.getComposition().getDuration() / 32.0f);
        (this.colorAnimation = gradientFill.getGradientColor().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        layer.addAnimation(this.colorAnimation);
        (this.opacityAnimation = gradientFill.getOpacity().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        layer.addAnimation(this.opacityAnimation);
        (this.startPointAnimation = gradientFill.getStartPoint().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        layer.addAnimation(this.startPointAnimation);
        (this.endPointAnimation = gradientFill.getEndPoint().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        layer.addAnimation(this.endPointAnimation);
    }
    
    private int getGradientHash() {
        final int round = Math.round(this.startPointAnimation.getProgress() * this.cacheSteps);
        final int round2 = Math.round(this.endPointAnimation.getProgress() * this.cacheSteps);
        final int round3 = Math.round(this.colorAnimation.getProgress() * this.cacheSteps);
        int n;
        if (round != 0) {
            n = 527 * round;
        }
        else {
            n = 17;
        }
        int n2 = n;
        if (round2 != 0) {
            n2 = n * 31 * round2;
        }
        int n3 = n2;
        if (round3 != 0) {
            n3 = n2 * 31 * round3;
        }
        return n3;
    }
    
    private LinearGradient getLinearGradient() {
        final int gradientHash = this.getGradientHash();
        final LongSparseArray<LinearGradient> linearGradientCache = this.linearGradientCache;
        final long n = gradientHash;
        final LinearGradient linearGradient = linearGradientCache.get(n);
        if (linearGradient != null) {
            return linearGradient;
        }
        final PointF pointF = this.startPointAnimation.getValue();
        final PointF pointF2 = this.endPointAnimation.getValue();
        final GradientColor gradientColor = this.colorAnimation.getValue();
        final LinearGradient linearGradient2 = new LinearGradient(pointF.x, pointF.y, pointF2.x, pointF2.y, gradientColor.getColors(), gradientColor.getPositions(), Shader$TileMode.CLAMP);
        this.linearGradientCache.put(n, linearGradient2);
        return linearGradient2;
    }
    
    private RadialGradient getRadialGradient() {
        final int gradientHash = this.getGradientHash();
        final LongSparseArray<RadialGradient> radialGradientCache = this.radialGradientCache;
        final long n = gradientHash;
        final RadialGradient radialGradient = radialGradientCache.get(n);
        if (radialGradient != null) {
            return radialGradient;
        }
        final PointF pointF = this.startPointAnimation.getValue();
        final PointF pointF2 = this.endPointAnimation.getValue();
        final GradientColor gradientColor = this.colorAnimation.getValue();
        final int[] colors = gradientColor.getColors();
        final float[] positions = gradientColor.getPositions();
        final float x = pointF.x;
        final float y = pointF.y;
        final RadialGradient radialGradient2 = new RadialGradient(x, y, (float)Math.hypot(pointF2.x - x, pointF2.y - y), colors, positions, Shader$TileMode.CLAMP);
        this.radialGradientCache.put(n, radialGradient2);
        return radialGradient2;
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.COLOR_FILTER) {
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
    public void draw(final Canvas canvas, final Matrix matrix, int n) {
        L.beginSection("GradientFillContent#draw");
        this.path.reset();
        for (int i = 0; i < this.paths.size(); ++i) {
            this.path.addPath(this.paths.get(i).getPath(), matrix);
        }
        this.path.computeBounds(this.boundsRect, false);
        Object shader;
        if (this.type == GradientType.Linear) {
            shader = this.getLinearGradient();
        }
        else {
            shader = this.getRadialGradient();
        }
        this.shaderMatrix.set(matrix);
        ((Shader)shader).setLocalMatrix(this.shaderMatrix);
        this.paint.setShader((Shader)shader);
        if (this.colorFilterAnimation != null) {
            this.paint.setColorFilter((ColorFilter)this.colorFilterAnimation.getValue());
        }
        n = (int)(n / 255.0f * this.opacityAnimation.getValue() / 100.0f * 255.0f);
        this.paint.setAlpha(MiscUtils.clamp(n, 0, 255));
        canvas.drawPath(this.path, this.paint);
        L.endSection("GradientFillContent#draw");
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix) {
        this.path.reset();
        for (int i = 0; i < this.paths.size(); ++i) {
            this.path.addPath(this.paths.get(i).getPath(), matrix);
        }
        this.path.computeBounds(rectF, false);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
    }
    
    @Override
    public String getName() {
        return this.name;
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
        for (int i = 0; i < list2.size(); ++i) {
            final Content content = list2.get(i);
            if (content instanceof PathContent) {
                this.paths.add((PathContent)content);
            }
        }
    }
}
