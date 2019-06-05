package com.airbnb.lottie.model.layer;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build.VERSION;
import com.airbnb.lottie.C0352L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.DrawingContent;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.MaskKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.model.content.Mask.MaskMode;
import com.airbnb.lottie.model.layer.Layer.MatteType;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseLayer implements DrawingContent, AnimationListener, KeyPathElement {
    private final Paint addMaskPaint = new Paint(1);
    private final List<BaseKeyframeAnimation<?, ?>> animations = new ArrayList();
    final Matrix boundsMatrix = new Matrix();
    private final Paint clearPaint = new Paint();
    private final Paint contentPaint = new Paint(1);
    private final String drawTraceName;
    final Layer layerModel;
    final LottieDrawable lottieDrawable;
    private MaskKeyframeAnimation mask;
    private final RectF maskBoundsRect = new RectF();
    private final Matrix matrix = new Matrix();
    private final RectF matteBoundsRect = new RectF();
    private BaseLayer matteLayer;
    private final Paint mattePaint = new Paint(1);
    private BaseLayer parentLayer;
    private List<BaseLayer> parentLayers;
    private final Path path = new Path();
    private final RectF rect = new RectF();
    private final Paint subtractMaskPaint = new Paint(1);
    private final RectF tempMaskBoundsRect = new RectF();
    final TransformKeyframeAnimation transform;
    private boolean visible = true;

    public abstract void drawLayer(Canvas canvas, Matrix matrix, int i);

    /* Access modifiers changed, original: 0000 */
    public void resolveChildKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
    }

    public void setContents(List<Content> list, List<Content> list2) {
    }

    static BaseLayer forModel(Layer layer, LottieDrawable lottieDrawable, LottieComposition lottieComposition) {
        switch (layer.getLayerType()) {
            case Shape:
                return new ShapeLayer(lottieDrawable, layer);
            case PreComp:
                return new CompositionLayer(lottieDrawable, layer, lottieComposition.getPrecomps(layer.getRefId()), lottieComposition);
            case Solid:
                return new SolidLayer(lottieDrawable, layer);
            case Image:
                return new ImageLayer(lottieDrawable, layer);
            case Null:
                return new NullLayer(lottieDrawable, layer);
            case Text:
                return new TextLayer(lottieDrawable, layer);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown layer type ");
                stringBuilder.append(layer.getLayerType());
                C0352L.warn(stringBuilder.toString());
                return null;
        }
    }

    BaseLayer(LottieDrawable lottieDrawable, Layer layer) {
        this.lottieDrawable = lottieDrawable;
        this.layerModel = layer;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(layer.getName());
        stringBuilder.append("#draw");
        this.drawTraceName = stringBuilder.toString();
        this.clearPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.addMaskPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        this.subtractMaskPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        if (layer.getMatteType() == MatteType.Invert) {
            this.mattePaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        } else {
            this.mattePaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        }
        this.transform = layer.getTransform().createAnimation();
        this.transform.addListener(this);
        if (!(layer.getMasks() == null || layer.getMasks().isEmpty())) {
            this.mask = new MaskKeyframeAnimation(layer.getMasks());
            for (BaseKeyframeAnimation addUpdateListener : this.mask.getMaskAnimations()) {
                addUpdateListener.addUpdateListener(this);
            }
            for (BaseKeyframeAnimation addUpdateListener2 : this.mask.getOpacityAnimations()) {
                addAnimation(addUpdateListener2);
                addUpdateListener2.addUpdateListener(this);
            }
        }
        setupInOutAnimations();
    }

    public void onValueChanged() {
        invalidateSelf();
    }

    /* Access modifiers changed, original: 0000 */
    public Layer getLayerModel() {
        return this.layerModel;
    }

    /* Access modifiers changed, original: 0000 */
    public void setMatteLayer(BaseLayer baseLayer) {
        this.matteLayer = baseLayer;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasMatteOnThisLayer() {
        return this.matteLayer != null;
    }

    /* Access modifiers changed, original: 0000 */
    public void setParentLayer(BaseLayer baseLayer) {
        this.parentLayer = baseLayer;
    }

    private void setupInOutAnimations() {
        boolean z = true;
        if (this.layerModel.getInOutKeyframes().isEmpty()) {
            setVisible(true);
            return;
        }
        final FloatKeyframeAnimation floatKeyframeAnimation = new FloatKeyframeAnimation(this.layerModel.getInOutKeyframes());
        floatKeyframeAnimation.setIsDiscrete();
        floatKeyframeAnimation.addUpdateListener(new AnimationListener() {
            public void onValueChanged() {
                BaseLayer.this.setVisible(((Float) floatKeyframeAnimation.getValue()).floatValue() == 1.0f);
            }
        });
        if (((Float) floatKeyframeAnimation.getValue()).floatValue() != 1.0f) {
            z = false;
        }
        setVisible(z);
        addAnimation(floatKeyframeAnimation);
    }

    private void invalidateSelf() {
        this.lottieDrawable.invalidateSelf();
    }

    @SuppressLint({"WrongConstant"})
    private void saveLayerCompat(Canvas canvas, RectF rectF, Paint paint, boolean z) {
        if (VERSION.SDK_INT < 23) {
            canvas.saveLayer(rectF, paint, z ? 31 : 19);
        } else {
            canvas.saveLayer(rectF, paint);
        }
    }

    public void addAnimation(BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        this.animations.add(baseKeyframeAnimation);
    }

    public void getBounds(RectF rectF, Matrix matrix) {
        this.boundsMatrix.set(matrix);
        this.boundsMatrix.preConcat(this.transform.getMatrix());
    }

    public void draw(Canvas canvas, Matrix matrix, int i) {
        C0352L.beginSection(this.drawTraceName);
        if (this.visible) {
            buildParentLayerListIfNeeded();
            C0352L.beginSection("Layer#parentMatrix");
            this.matrix.reset();
            this.matrix.set(matrix);
            for (int size = this.parentLayers.size() - 1; size >= 0; size--) {
                this.matrix.preConcat(((BaseLayer) this.parentLayers.get(size)).transform.getMatrix());
            }
            C0352L.endSection("Layer#parentMatrix");
            i = (int) ((((((float) i) / 255.0f) * ((float) ((Integer) this.transform.getOpacity().getValue()).intValue())) / 100.0f) * 255.0f);
            if (hasMatteOnThisLayer() || hasMasksOnThisLayer()) {
                C0352L.beginSection("Layer#computeBounds");
                this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
                getBounds(this.rect, this.matrix);
                intersectBoundsWithMatte(this.rect, this.matrix);
                this.matrix.preConcat(this.transform.getMatrix());
                intersectBoundsWithMask(this.rect, this.matrix);
                this.rect.set(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight());
                C0352L.endSection("Layer#computeBounds");
                C0352L.beginSection("Layer#saveLayer");
                saveLayerCompat(canvas, this.rect, this.contentPaint, true);
                C0352L.endSection("Layer#saveLayer");
                clearCanvas(canvas);
                C0352L.beginSection("Layer#drawLayer");
                drawLayer(canvas, this.matrix, i);
                C0352L.endSection("Layer#drawLayer");
                if (hasMasksOnThisLayer()) {
                    applyMasks(canvas, this.matrix);
                }
                if (hasMatteOnThisLayer()) {
                    C0352L.beginSection("Layer#drawMatte");
                    C0352L.beginSection("Layer#saveLayer");
                    saveLayerCompat(canvas, this.rect, this.mattePaint, false);
                    C0352L.endSection("Layer#saveLayer");
                    clearCanvas(canvas);
                    this.matteLayer.draw(canvas, matrix, i);
                    C0352L.beginSection("Layer#restoreLayer");
                    canvas.restore();
                    C0352L.endSection("Layer#restoreLayer");
                    C0352L.endSection("Layer#drawMatte");
                }
                C0352L.beginSection("Layer#restoreLayer");
                canvas.restore();
                C0352L.endSection("Layer#restoreLayer");
                recordRenderTime(C0352L.endSection(this.drawTraceName));
                return;
            }
            this.matrix.preConcat(this.transform.getMatrix());
            C0352L.beginSection("Layer#drawLayer");
            drawLayer(canvas, this.matrix, i);
            C0352L.endSection("Layer#drawLayer");
            recordRenderTime(C0352L.endSection(this.drawTraceName));
            return;
        }
        C0352L.endSection(this.drawTraceName);
    }

    private void recordRenderTime(float f) {
        this.lottieDrawable.getComposition().getPerformanceTracker().recordRenderTime(this.layerModel.getName(), f);
    }

    private void clearCanvas(Canvas canvas) {
        C0352L.beginSection("Layer#clearLayer");
        canvas.drawRect(this.rect.left - 1.0f, this.rect.top - 1.0f, this.rect.right + 1.0f, this.rect.bottom + 1.0f, this.clearPaint);
        C0352L.endSection("Layer#clearLayer");
    }

    private void intersectBoundsWithMask(RectF rectF, Matrix matrix) {
        this.maskBoundsRect.set(0.0f, 0.0f, 0.0f, 0.0f);
        if (hasMasksOnThisLayer()) {
            int size = this.mask.getMasks().size();
            int i = 0;
            while (i < size) {
                Mask mask = (Mask) this.mask.getMasks().get(i);
                this.path.set((Path) ((BaseKeyframeAnimation) this.mask.getMaskAnimations().get(i)).getValue());
                this.path.transform(matrix);
                switch (mask.getMaskMode()) {
                    case MaskModeSubtract:
                        return;
                    case MaskModeIntersect:
                        return;
                    default:
                        this.path.computeBounds(this.tempMaskBoundsRect, false);
                        if (i == 0) {
                            this.maskBoundsRect.set(this.tempMaskBoundsRect);
                        } else {
                            this.maskBoundsRect.set(Math.min(this.maskBoundsRect.left, this.tempMaskBoundsRect.left), Math.min(this.maskBoundsRect.top, this.tempMaskBoundsRect.top), Math.max(this.maskBoundsRect.right, this.tempMaskBoundsRect.right), Math.max(this.maskBoundsRect.bottom, this.tempMaskBoundsRect.bottom));
                        }
                        i++;
                }
            }
            rectF.set(Math.max(rectF.left, this.maskBoundsRect.left), Math.max(rectF.top, this.maskBoundsRect.top), Math.min(rectF.right, this.maskBoundsRect.right), Math.min(rectF.bottom, this.maskBoundsRect.bottom));
        }
    }

    private void intersectBoundsWithMatte(RectF rectF, Matrix matrix) {
        if (hasMatteOnThisLayer() && this.layerModel.getMatteType() != MatteType.Invert) {
            this.matteLayer.getBounds(this.matteBoundsRect, matrix);
            rectF.set(Math.max(rectF.left, this.matteBoundsRect.left), Math.max(rectF.top, this.matteBoundsRect.top), Math.min(rectF.right, this.matteBoundsRect.right), Math.min(rectF.bottom, this.matteBoundsRect.bottom));
        }
    }

    private void applyMasks(Canvas canvas, Matrix matrix) {
        applyMasks(canvas, matrix, MaskMode.MaskModeAdd);
        applyMasks(canvas, matrix, MaskMode.MaskModeIntersect);
        applyMasks(canvas, matrix, MaskMode.MaskModeSubtract);
    }

    private void applyMasks(Canvas canvas, Matrix matrix, MaskMode maskMode) {
        Paint paint;
        Object obj = 1;
        if (C03782.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[maskMode.ordinal()] != 1) {
            paint = this.addMaskPaint;
        } else {
            paint = this.subtractMaskPaint;
        }
        int size = this.mask.getMasks().size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            if (((Mask) this.mask.getMasks().get(i2)).getMaskMode() == maskMode) {
                break;
            }
        }
        obj = null;
        if (obj != null) {
            C0352L.beginSection("Layer#drawMask");
            C0352L.beginSection("Layer#saveLayer");
            saveLayerCompat(canvas, this.rect, paint, false);
            C0352L.endSection("Layer#saveLayer");
            clearCanvas(canvas);
            while (i < size) {
                if (((Mask) this.mask.getMasks().get(i)).getMaskMode() == maskMode) {
                    this.path.set((Path) ((BaseKeyframeAnimation) this.mask.getMaskAnimations().get(i)).getValue());
                    this.path.transform(matrix);
                    BaseKeyframeAnimation baseKeyframeAnimation = (BaseKeyframeAnimation) this.mask.getOpacityAnimations().get(i);
                    int alpha = this.contentPaint.getAlpha();
                    this.contentPaint.setAlpha((int) (((float) ((Integer) baseKeyframeAnimation.getValue()).intValue()) * 2.55f));
                    canvas.drawPath(this.path, this.contentPaint);
                    this.contentPaint.setAlpha(alpha);
                }
                i++;
            }
            C0352L.beginSection("Layer#restoreLayer");
            canvas.restore();
            C0352L.endSection("Layer#restoreLayer");
            C0352L.endSection("Layer#drawMask");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasMasksOnThisLayer() {
        return (this.mask == null || this.mask.getMaskAnimations().isEmpty()) ? false : true;
    }

    private void setVisible(boolean z) {
        if (z != this.visible) {
            this.visible = z;
            invalidateSelf();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setProgress(float f) {
        this.transform.setProgress(f);
        int i = 0;
        if (this.mask != null) {
            for (int i2 = 0; i2 < this.mask.getMaskAnimations().size(); i2++) {
                ((BaseKeyframeAnimation) this.mask.getMaskAnimations().get(i2)).setProgress(f);
            }
        }
        if (this.layerModel.getTimeStretch() != 0.0f) {
            f /= this.layerModel.getTimeStretch();
        }
        if (this.matteLayer != null) {
            this.matteLayer.setProgress(this.matteLayer.layerModel.getTimeStretch() * f);
        }
        while (i < this.animations.size()) {
            ((BaseKeyframeAnimation) this.animations.get(i)).setProgress(f);
            i++;
        }
    }

    private void buildParentLayerListIfNeeded() {
        if (this.parentLayers == null) {
            if (this.parentLayer == null) {
                this.parentLayers = Collections.emptyList();
                return;
            }
            this.parentLayers = new ArrayList();
            for (Object obj = this.parentLayer; obj != null; obj = obj.parentLayer) {
                this.parentLayers.add(obj);
            }
        }
    }

    public String getName() {
        return this.layerModel.getName();
    }

    public void resolveKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
        if (keyPath.matches(getName(), i)) {
            if (!"__container".equals(getName())) {
                keyPath2 = keyPath2.addKey(getName());
                if (keyPath.fullyResolvesTo(getName(), i)) {
                    list.add(keyPath2.resolve(this));
                }
            }
            if (keyPath.propagateToChildren(getName(), i)) {
                resolveChildKeyPath(keyPath, i + keyPath.incrementDepthBy(getName(), i), list, keyPath2);
            }
        }
    }

    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        this.transform.applyValueCallback(t, lottieValueCallback);
    }
}
