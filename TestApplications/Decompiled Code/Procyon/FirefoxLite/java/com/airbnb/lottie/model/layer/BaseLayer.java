// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import android.annotation.SuppressLint;
import android.os.Build$VERSION;
import com.airbnb.lottie.LottieComposition;
import java.util.Collections;
import com.airbnb.lottie.L;
import com.airbnb.lottie.model.content.Mask;
import android.graphics.Canvas;
import java.util.Iterator;
import com.airbnb.lottie.model.content.ShapeData;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import java.util.ArrayList;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import android.graphics.Path;
import android.graphics.RectF;
import com.airbnb.lottie.animation.keyframe.MaskKeyframeAnimation;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.Matrix;
import java.util.List;
import android.graphics.Paint;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.content.DrawingContent;

public abstract class BaseLayer implements DrawingContent, AnimationListener, KeyPathElement
{
    private final Paint addMaskPaint;
    private final List<BaseKeyframeAnimation<?, ?>> animations;
    final Matrix boundsMatrix;
    private final Paint clearPaint;
    private final Paint contentPaint;
    private final String drawTraceName;
    final Layer layerModel;
    final LottieDrawable lottieDrawable;
    private MaskKeyframeAnimation mask;
    private final RectF maskBoundsRect;
    private final Matrix matrix;
    private final RectF matteBoundsRect;
    private BaseLayer matteLayer;
    private final Paint mattePaint;
    private BaseLayer parentLayer;
    private List<BaseLayer> parentLayers;
    private final Path path;
    private final RectF rect;
    private final Paint subtractMaskPaint;
    private final RectF tempMaskBoundsRect;
    final TransformKeyframeAnimation transform;
    private boolean visible;
    
    BaseLayer(final LottieDrawable lottieDrawable, final Layer layerModel) {
        this.path = new Path();
        this.matrix = new Matrix();
        this.contentPaint = new Paint(1);
        this.addMaskPaint = new Paint(1);
        this.subtractMaskPaint = new Paint(1);
        this.mattePaint = new Paint(1);
        this.clearPaint = new Paint();
        this.rect = new RectF();
        this.maskBoundsRect = new RectF();
        this.matteBoundsRect = new RectF();
        this.tempMaskBoundsRect = new RectF();
        this.boundsMatrix = new Matrix();
        this.animations = new ArrayList<BaseKeyframeAnimation<?, ?>>();
        this.visible = true;
        this.lottieDrawable = lottieDrawable;
        this.layerModel = layerModel;
        final StringBuilder sb = new StringBuilder();
        sb.append(layerModel.getName());
        sb.append("#draw");
        this.drawTraceName = sb.toString();
        this.clearPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.CLEAR));
        this.addMaskPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_IN));
        this.subtractMaskPaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_OUT));
        if (layerModel.getMatteType() == Layer.MatteType.Invert) {
            this.mattePaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_OUT));
        }
        else {
            this.mattePaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_IN));
        }
        (this.transform = layerModel.getTransform().createAnimation()).addListener(this);
        if (layerModel.getMasks() != null && !layerModel.getMasks().isEmpty()) {
            this.mask = new MaskKeyframeAnimation(layerModel.getMasks());
            final Iterator<BaseKeyframeAnimation<ShapeData, Path>> iterator = this.mask.getMaskAnimations().iterator();
            while (iterator.hasNext()) {
                iterator.next().addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            }
            for (final BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation : this.mask.getOpacityAnimations()) {
                this.addAnimation(baseKeyframeAnimation);
                baseKeyframeAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            }
        }
        this.setupInOutAnimations();
    }
    
    private void applyMasks(final Canvas canvas, final Matrix matrix) {
        this.applyMasks(canvas, matrix, Mask.MaskMode.MaskModeAdd);
        this.applyMasks(canvas, matrix, Mask.MaskMode.MaskModeIntersect);
        this.applyMasks(canvas, matrix, Mask.MaskMode.MaskModeSubtract);
    }
    
    private void applyMasks(final Canvas canvas, final Matrix matrix, final Mask.MaskMode maskMode) {
        final int n = BaseLayer$2.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[maskMode.ordinal()];
        final int n2 = 1;
        Paint paint;
        if (n != 1) {
            paint = this.addMaskPaint;
        }
        else {
            paint = this.subtractMaskPaint;
        }
        final int size = this.mask.getMasks().size();
        final int n3 = 0;
        int i = 0;
        while (true) {
            while (i < size) {
                if (this.mask.getMasks().get(i).getMaskMode() == maskMode) {
                    final int n4 = n2;
                    if (n4 == 0) {
                        return;
                    }
                    L.beginSection("Layer#drawMask");
                    L.beginSection("Layer#saveLayer");
                    this.saveLayerCompat(canvas, this.rect, paint, false);
                    L.endSection("Layer#saveLayer");
                    this.clearCanvas(canvas);
                    for (int j = n3; j < size; ++j) {
                        if (this.mask.getMasks().get(j).getMaskMode() == maskMode) {
                            this.path.set((Path)this.mask.getMaskAnimations().get(j).getValue());
                            this.path.transform(matrix);
                            final BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = this.mask.getOpacityAnimations().get(j);
                            final int alpha = this.contentPaint.getAlpha();
                            this.contentPaint.setAlpha((int)(baseKeyframeAnimation.getValue() * 2.55f));
                            canvas.drawPath(this.path, this.contentPaint);
                            this.contentPaint.setAlpha(alpha);
                        }
                    }
                    L.beginSection("Layer#restoreLayer");
                    canvas.restore();
                    L.endSection("Layer#restoreLayer");
                    L.endSection("Layer#drawMask");
                    return;
                }
                else {
                    ++i;
                }
            }
            final int n4 = 0;
            continue;
        }
    }
    
    private void buildParentLayerListIfNeeded() {
        if (this.parentLayers != null) {
            return;
        }
        if (this.parentLayer == null) {
            this.parentLayers = Collections.emptyList();
            return;
        }
        this.parentLayers = new ArrayList<BaseLayer>();
        for (BaseLayer baseLayer = this.parentLayer; baseLayer != null; baseLayer = baseLayer.parentLayer) {
            this.parentLayers.add(baseLayer);
        }
    }
    
    private void clearCanvas(final Canvas canvas) {
        L.beginSection("Layer#clearLayer");
        canvas.drawRect(this.rect.left - 1.0f, this.rect.top - 1.0f, this.rect.right + 1.0f, this.rect.bottom + 1.0f, this.clearPaint);
        L.endSection("Layer#clearLayer");
    }
    
    static BaseLayer forModel(final Layer layer, final LottieDrawable lottieDrawable, final LottieComposition lottieComposition) {
        switch (BaseLayer$2.$SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType[layer.getLayerType().ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown layer type ");
                sb.append(layer.getLayerType());
                L.warn(sb.toString());
                return null;
            }
            case 6: {
                return new TextLayer(lottieDrawable, layer);
            }
            case 5: {
                return new NullLayer(lottieDrawable, layer);
            }
            case 4: {
                return new ImageLayer(lottieDrawable, layer);
            }
            case 3: {
                return new SolidLayer(lottieDrawable, layer);
            }
            case 2: {
                return new CompositionLayer(lottieDrawable, layer, lottieComposition.getPrecomps(layer.getRefId()), lottieComposition);
            }
            case 1: {
                return new ShapeLayer(lottieDrawable, layer);
            }
        }
    }
    
    private void intersectBoundsWithMask(final RectF rectF, final Matrix matrix) {
        this.maskBoundsRect.set(0.0f, 0.0f, 0.0f, 0.0f);
        if (!this.hasMasksOnThisLayer()) {
            return;
        }
        final int size = this.mask.getMasks().size();
        int i = 0;
        while (i < size) {
            final Mask mask = this.mask.getMasks().get(i);
            this.path.set((Path)this.mask.getMaskAnimations().get(i).getValue());
            this.path.transform(matrix);
            switch (BaseLayer$2.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[mask.getMaskMode().ordinal()]) {
                default: {
                    this.path.computeBounds(this.tempMaskBoundsRect, false);
                    if (i == 0) {
                        this.maskBoundsRect.set(this.tempMaskBoundsRect);
                    }
                    else {
                        this.maskBoundsRect.set(Math.min(this.maskBoundsRect.left, this.tempMaskBoundsRect.left), Math.min(this.maskBoundsRect.top, this.tempMaskBoundsRect.top), Math.max(this.maskBoundsRect.right, this.tempMaskBoundsRect.right), Math.max(this.maskBoundsRect.bottom, this.tempMaskBoundsRect.bottom));
                    }
                    ++i;
                    continue;
                }
                case 2: {
                    return;
                }
                case 1: {
                    return;
                }
            }
        }
        rectF.set(Math.max(rectF.left, this.maskBoundsRect.left), Math.max(rectF.top, this.maskBoundsRect.top), Math.min(rectF.right, this.maskBoundsRect.right), Math.min(rectF.bottom, this.maskBoundsRect.bottom));
    }
    
    private void intersectBoundsWithMatte(final RectF rectF, final Matrix matrix) {
        if (!this.hasMatteOnThisLayer()) {
            return;
        }
        if (this.layerModel.getMatteType() == Layer.MatteType.Invert) {
            return;
        }
        this.matteLayer.getBounds(this.matteBoundsRect, matrix);
        rectF.set(Math.max(rectF.left, this.matteBoundsRect.left), Math.max(rectF.top, this.matteBoundsRect.top), Math.min(rectF.right, this.matteBoundsRect.right), Math.min(rectF.bottom, this.matteBoundsRect.bottom));
    }
    
    private void invalidateSelf() {
        this.lottieDrawable.invalidateSelf();
    }
    
    private void recordRenderTime(final float n) {
        this.lottieDrawable.getComposition().getPerformanceTracker().recordRenderTime(this.layerModel.getName(), n);
    }
    
    @SuppressLint({ "WrongConstant" })
    private void saveLayerCompat(final Canvas canvas, final RectF rectF, final Paint paint, final boolean b) {
        if (Build$VERSION.SDK_INT < 23) {
            int n;
            if (b) {
                n = 31;
            }
            else {
                n = 19;
            }
            canvas.saveLayer(rectF, paint, n);
        }
        else {
            canvas.saveLayer(rectF, paint);
        }
    }
    
    private void setVisible(final boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            this.invalidateSelf();
        }
    }
    
    private void setupInOutAnimations() {
        final boolean empty = this.layerModel.getInOutKeyframes().isEmpty();
        boolean visible = true;
        if (!empty) {
            final FloatKeyframeAnimation floatKeyframeAnimation = new FloatKeyframeAnimation(this.layerModel.getInOutKeyframes());
            floatKeyframeAnimation.setIsDiscrete();
            floatKeyframeAnimation.addUpdateListener((BaseKeyframeAnimation.AnimationListener)new AnimationListener() {
                @Override
                public void onValueChanged() {
                    BaseLayer.this.setVisible(((BaseKeyframeAnimation<K, Float>)floatKeyframeAnimation).getValue() == 1.0f);
                }
            });
            if (((BaseKeyframeAnimation<K, Float>)floatKeyframeAnimation).getValue() != 1.0f) {
                visible = false;
            }
            this.setVisible(visible);
            this.addAnimation(floatKeyframeAnimation);
        }
        else {
            this.setVisible(true);
        }
    }
    
    public void addAnimation(final BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        this.animations.add(baseKeyframeAnimation);
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        this.transform.applyValueCallback(t, lottieValueCallback);
    }
    
    @Override
    public void draw(final Canvas canvas, final Matrix matrix, int n) {
        L.beginSection(this.drawTraceName);
        if (!this.visible) {
            L.endSection(this.drawTraceName);
            return;
        }
        this.buildParentLayerListIfNeeded();
        L.beginSection("Layer#parentMatrix");
        this.matrix.reset();
        this.matrix.set(matrix);
        for (int i = this.parentLayers.size() - 1; i >= 0; --i) {
            this.matrix.preConcat(this.parentLayers.get(i).transform.getMatrix());
        }
        L.endSection("Layer#parentMatrix");
        n = (int)(n / 255.0f * this.transform.getOpacity().getValue() / 100.0f * 255.0f);
        if (!this.hasMatteOnThisLayer() && !this.hasMasksOnThisLayer()) {
            this.matrix.preConcat(this.transform.getMatrix());
            L.beginSection("Layer#drawLayer");
            this.drawLayer(canvas, this.matrix, n);
            L.endSection("Layer#drawLayer");
            this.recordRenderTime(L.endSection(this.drawTraceName));
            return;
        }
        L.beginSection("Layer#computeBounds");
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        this.getBounds(this.rect, this.matrix);
        this.intersectBoundsWithMatte(this.rect, this.matrix);
        this.matrix.preConcat(this.transform.getMatrix());
        this.intersectBoundsWithMask(this.rect, this.matrix);
        this.rect.set(0.0f, 0.0f, (float)canvas.getWidth(), (float)canvas.getHeight());
        L.endSection("Layer#computeBounds");
        L.beginSection("Layer#saveLayer");
        this.saveLayerCompat(canvas, this.rect, this.contentPaint, true);
        L.endSection("Layer#saveLayer");
        this.clearCanvas(canvas);
        L.beginSection("Layer#drawLayer");
        this.drawLayer(canvas, this.matrix, n);
        L.endSection("Layer#drawLayer");
        if (this.hasMasksOnThisLayer()) {
            this.applyMasks(canvas, this.matrix);
        }
        if (this.hasMatteOnThisLayer()) {
            L.beginSection("Layer#drawMatte");
            L.beginSection("Layer#saveLayer");
            this.saveLayerCompat(canvas, this.rect, this.mattePaint, false);
            L.endSection("Layer#saveLayer");
            this.clearCanvas(canvas);
            this.matteLayer.draw(canvas, matrix, n);
            L.beginSection("Layer#restoreLayer");
            canvas.restore();
            L.endSection("Layer#restoreLayer");
            L.endSection("Layer#drawMatte");
        }
        L.beginSection("Layer#restoreLayer");
        canvas.restore();
        L.endSection("Layer#restoreLayer");
        this.recordRenderTime(L.endSection(this.drawTraceName));
    }
    
    abstract void drawLayer(final Canvas p0, final Matrix p1, final int p2);
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix) {
        this.boundsMatrix.set(matrix);
        this.boundsMatrix.preConcat(this.transform.getMatrix());
    }
    
    Layer getLayerModel() {
        return this.layerModel;
    }
    
    @Override
    public String getName() {
        return this.layerModel.getName();
    }
    
    boolean hasMasksOnThisLayer() {
        return this.mask != null && !this.mask.getMaskAnimations().isEmpty();
    }
    
    boolean hasMatteOnThisLayer() {
        return this.matteLayer != null;
    }
    
    @Override
    public void onValueChanged() {
        this.invalidateSelf();
    }
    
    void resolveChildKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, final KeyPath keyPath2) {
    }
    
    @Override
    public void resolveKeyPath(final KeyPath keyPath, final int n, final List<KeyPath> list, KeyPath keyPath2) {
        if (!keyPath.matches(this.getName(), n)) {
            return;
        }
        KeyPath addKey = keyPath2;
        if (!"__container".equals(this.getName())) {
            keyPath2 = (addKey = keyPath2.addKey(this.getName()));
            if (keyPath.fullyResolvesTo(this.getName(), n)) {
                list.add(keyPath2.resolve(this));
                addKey = keyPath2;
            }
        }
        if (keyPath.propagateToChildren(this.getName(), n)) {
            this.resolveChildKeyPath(keyPath, n + keyPath.incrementDepthBy(this.getName(), n), list, addKey);
        }
    }
    
    @Override
    public void setContents(final List<Content> list, final List<Content> list2) {
    }
    
    void setMatteLayer(final BaseLayer matteLayer) {
        this.matteLayer = matteLayer;
    }
    
    void setParentLayer(final BaseLayer parentLayer) {
        this.parentLayer = parentLayer;
    }
    
    void setProgress(float timeStretch) {
        this.transform.setProgress(timeStretch);
        final MaskKeyframeAnimation mask = this.mask;
        final int n = 0;
        if (mask != null) {
            for (int i = 0; i < this.mask.getMaskAnimations().size(); ++i) {
                this.mask.getMaskAnimations().get(i).setProgress(timeStretch);
            }
        }
        float progress = timeStretch;
        if (this.layerModel.getTimeStretch() != 0.0f) {
            progress = timeStretch / this.layerModel.getTimeStretch();
        }
        int j = n;
        if (this.matteLayer != null) {
            timeStretch = this.matteLayer.layerModel.getTimeStretch();
            this.matteLayer.setProgress(timeStretch * progress);
            j = n;
        }
        while (j < this.animations.size()) {
            this.animations.get(j).setProgress(progress);
            ++j;
        }
    }
}
