// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.animation.content;

import android.graphics.Shader;
import android.graphics.Matrix;
import android.graphics.Canvas;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import android.graphics.Shader$TileMode;
import com.airbnb.lottie.model.content.GradientStroke;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.model.content.GradientType;
import android.graphics.RadialGradient;
import android.graphics.LinearGradient;
import androidx.collection.LongSparseArray;
import android.graphics.PointF;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import android.graphics.RectF;

public class GradientStrokeContent extends BaseStrokeContent
{
    private final RectF boundsRect;
    private final int cacheSteps;
    private final BaseKeyframeAnimation<GradientColor, GradientColor> colorAnimation;
    private ValueCallbackKeyframeAnimation colorCallbackAnimation;
    private final BaseKeyframeAnimation<PointF, PointF> endPointAnimation;
    private final boolean hidden;
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
        this.hidden = gradientStroke.isHidden();
        this.cacheSteps = (int)(lottieDrawable.getComposition().getDuration() / 32.0f);
        (this.colorAnimation = gradientStroke.getGradientColor().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        baseLayer.addAnimation(this.colorAnimation);
        (this.startPointAnimation = gradientStroke.getStartPoint().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        baseLayer.addAnimation(this.startPointAnimation);
        (this.endPointAnimation = gradientStroke.getEndPoint().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        baseLayer.addAnimation(this.endPointAnimation);
    }
    
    private int[] applyDynamicColorsIfNeeded(int[] array) {
        final ValueCallbackKeyframeAnimation colorCallbackAnimation = this.colorCallbackAnimation;
        int[] array2 = array;
        if (colorCallbackAnimation != null) {
            final Integer[] array3 = colorCallbackAnimation.getValue();
            final int length = array.length;
            final int length2 = array3.length;
            final int n = 0;
            int n2 = 0;
            if (length == length2) {
                while (true) {
                    array2 = array;
                    if (n2 >= array.length) {
                        break;
                    }
                    array[n2] = array3[n2];
                    ++n2;
                }
            }
            else {
                array = new int[array3.length];
                int n3 = n;
                while (true) {
                    array2 = array;
                    if (n3 >= array3.length) {
                        break;
                    }
                    array[n3] = array3[n3];
                    ++n3;
                }
            }
        }
        return array2;
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
        final int[] applyDynamicColorsIfNeeded = this.applyDynamicColorsIfNeeded(gradientColor.getColors());
        final float[] positions = gradientColor.getPositions();
        final RectF boundsRect = this.boundsRect;
        final int n2 = (int)(boundsRect.left + boundsRect.width() / 2.0f + pointF.x);
        final RectF boundsRect2 = this.boundsRect;
        final int n3 = (int)(boundsRect2.top + boundsRect2.height() / 2.0f + pointF.y);
        final RectF boundsRect3 = this.boundsRect;
        final int n4 = (int)(boundsRect3.left + boundsRect3.width() / 2.0f + pointF2.x);
        final RectF boundsRect4 = this.boundsRect;
        final LinearGradient linearGradient2 = new LinearGradient((float)n2, (float)n3, (float)n4, (float)(int)(boundsRect4.top + boundsRect4.height() / 2.0f + pointF2.y), applyDynamicColorsIfNeeded, positions, Shader$TileMode.CLAMP);
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
        final int[] applyDynamicColorsIfNeeded = this.applyDynamicColorsIfNeeded(gradientColor.getColors());
        final float[] positions = gradientColor.getPositions();
        final RectF boundsRect = this.boundsRect;
        final int n2 = (int)(boundsRect.left + boundsRect.width() / 2.0f + pointF.x);
        final RectF boundsRect2 = this.boundsRect;
        final int n3 = (int)(boundsRect2.top + boundsRect2.height() / 2.0f + pointF.y);
        final RectF boundsRect3 = this.boundsRect;
        final int n4 = (int)(boundsRect3.left + boundsRect3.width() / 2.0f + pointF2.x);
        final RectF boundsRect4 = this.boundsRect;
        final RadialGradient radialGradient2 = new RadialGradient((float)n2, (float)n3, (float)Math.hypot(n4 - n2, (int)(boundsRect4.top + boundsRect4.height() / 2.0f + pointF2.y) - n3), applyDynamicColorsIfNeeded, positions, Shader$TileMode.CLAMP);
        this.radialGradientCache.put(n, radialGradient2);
        return radialGradient2;
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.GRADIENT_COLOR) {
            if (lottieValueCallback == null) {
                final ValueCallbackKeyframeAnimation colorCallbackAnimation = this.colorCallbackAnimation;
                if (colorCallbackAnimation != null) {
                    super.layer.removeAnimation(colorCallbackAnimation);
                }
                this.colorCallbackAnimation = null;
            }
            else {
                (this.colorCallbackAnimation = new ValueCallbackKeyframeAnimation((LottieValueCallback<A>)lottieValueCallback)).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                super.layer.addAnimation(this.colorCallbackAnimation);
            }
        }
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, final int n) {
        if (this.hidden) {
            return;
        }
        this.getBounds(this.boundsRect, matrix, false);
        Object shader;
        if (this.type == GradientType.LINEAR) {
            shader = this.getLinearGradient();
        }
        else {
            shader = this.getRadialGradient();
        }
        super.paint.setShader((Shader)shader);
        super.draw(canvas, matrix, n);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
