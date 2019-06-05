// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import android.graphics.Shader;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.Shader$TileMode;
import com.airbnb.lottie.model.content.GradientStroke;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.content.GradientType;
import android.graphics.RadialGradient;
import android.graphics.LinearGradient;
import android.support.v4.util.LongSparseArray;
import android.graphics.PointF;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import android.graphics.RectF;

public class GradientStrokeContent extends BaseStrokeContent
{
    private final RectF boundsRect;
    private final int cacheSteps;
    private final BaseKeyframeAnimation<GradientColor, GradientColor> colorAnimation;
    private final BaseKeyframeAnimation<PointF, PointF> endPointAnimation;
    private final LongSparseArray<LinearGradient> linearGradientCache;
    private final String name;
    private final LongSparseArray<RadialGradient> radialGradientCache;
    private final BaseKeyframeAnimation<PointF, PointF> startPointAnimation;
    private final GradientType type;
    
    public GradientStrokeContent(final LottieDrawable lottieDrawable, final BaseLayer baseLayer, final GradientStroke gradientStroke) {
        super(lottieDrawable, baseLayer, gradientStroke.getCapType().toPaintCap(), gradientStroke.getJoinType().toPaintJoin(), gradientStroke.getMiterLimit(), gradientStroke.getOpacity(), gradientStroke.getWidth(), gradientStroke.getLineDashPattern(), gradientStroke.getDashOffset());
        this.linearGradientCache = new LongSparseArray<LinearGradient>();
        this.radialGradientCache = new LongSparseArray<RadialGradient>();
        this.boundsRect = new RectF();
        this.name = gradientStroke.getName();
        this.type = gradientStroke.getGradientType();
        this.cacheSteps = (int)(lottieDrawable.getComposition().getDuration() / 32.0f);
        (this.colorAnimation = gradientStroke.getGradientColor().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        baseLayer.addAnimation(this.colorAnimation);
        (this.startPointAnimation = gradientStroke.getStartPoint().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        baseLayer.addAnimation(this.startPointAnimation);
        (this.endPointAnimation = gradientStroke.getEndPoint().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        baseLayer.addAnimation(this.endPointAnimation);
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
        final LinearGradient linearGradient2 = new LinearGradient((float)(int)(this.boundsRect.left + this.boundsRect.width() / 2.0f + pointF.x), (float)(int)(this.boundsRect.top + this.boundsRect.height() / 2.0f + pointF.y), (float)(int)(this.boundsRect.left + this.boundsRect.width() / 2.0f + pointF2.x), (float)(int)(this.boundsRect.top + this.boundsRect.height() / 2.0f + pointF2.y), gradientColor.getColors(), gradientColor.getPositions(), Shader$TileMode.CLAMP);
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
        final int n2 = (int)(this.boundsRect.left + this.boundsRect.width() / 2.0f + pointF.x);
        final int n3 = (int)(this.boundsRect.top + this.boundsRect.height() / 2.0f + pointF.y);
        final RadialGradient radialGradient2 = new RadialGradient((float)n2, (float)n3, (float)Math.hypot((int)(this.boundsRect.left + this.boundsRect.width() / 2.0f + pointF2.x) - n2, (int)(this.boundsRect.top + this.boundsRect.height() / 2.0f + pointF2.y) - n3), colors, positions, Shader$TileMode.CLAMP);
        this.radialGradientCache.put(n, radialGradient2);
        return radialGradient2;
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, final int n) {
        this.getBounds(this.boundsRect, matrix);
        if (this.type == GradientType.Linear) {
            this.paint.setShader((Shader)this.getLinearGradient());
        }
        else {
            this.paint.setShader((Shader)this.getRadialGradient());
        }
        super.draw(canvas, matrix, n);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
