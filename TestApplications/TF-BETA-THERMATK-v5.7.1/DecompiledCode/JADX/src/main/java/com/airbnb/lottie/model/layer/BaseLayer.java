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
import com.airbnb.lottie.C0093L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.LPaint;
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
import com.airbnb.lottie.model.content.ShapeData;
import com.airbnb.lottie.model.layer.Layer.LayerType;
import com.airbnb.lottie.model.layer.Layer.MatteType;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.p004ui.ActionBar.Theme;

public abstract class BaseLayer implements DrawingContent, AnimationListener, KeyPathElement {
    private final List<BaseKeyframeAnimation<?, ?>> animations = new ArrayList();
    final Matrix boundsMatrix = new Matrix();
    private final Paint clearPaint = new LPaint(Mode.CLEAR);
    private final Paint contentPaint = new LPaint(1);
    private final String drawTraceName;
    private final Paint dstInPaint = new LPaint(1, Mode.DST_IN);
    private final Paint dstOutPaint = new LPaint(1, Mode.DST_OUT);
    final Layer layerModel;
    final LottieDrawable lottieDrawable;
    private MaskKeyframeAnimation mask;
    private final RectF maskBoundsRect = new RectF();
    private final Matrix matrix = new Matrix();
    private final RectF matteBoundsRect = new RectF();
    private BaseLayer matteLayer;
    private final Paint mattePaint = new LPaint(1);
    private BaseLayer parentLayer;
    private List<BaseLayer> parentLayers;
    private final Path path = new Path();
    private final RectF rect = new RectF();
    private final RectF tempMaskBoundsRect = new RectF();
    final TransformKeyframeAnimation transform;
    private boolean visible = true;

    /* renamed from: com.airbnb.lottie.model.layer.BaseLayer$2 */
    static /* synthetic */ class C01232 {
        static final /* synthetic */ int[] $SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode = new int[MaskMode.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType = new int[LayerType.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0051 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0067 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0072 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|28) */
        /* JADX WARNING: Can't wrap try/catch for region: R(24:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|28) */
        /* JADX WARNING: Can't wrap try/catch for region: R(23:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|28) */
        /* JADX WARNING: Can't wrap try/catch for region: R(23:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|28) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|28) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|23|24|25|26|28) */
        /* JADX WARNING: Missing block: B:29:?, code skipped:
            return;
     */
        static {
            /*
            r0 = com.airbnb.lottie.model.content.Mask.MaskMode.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode = r0;
            r0 = 1;
            r1 = $SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_SUBTRACT;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = r2.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1[r2] = r0;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r1 = 2;
            r2 = $SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_INTERSECT;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = r3.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2[r3] = r1;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r2 = 3;
            r3 = $SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_ADD;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r3[r4] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r3 = com.airbnb.lottie.model.layer.Layer.LayerType.values();
            r3 = r3.length;
            r3 = new int[r3];
            $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType = r3;
            r3 = $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType;	 Catch:{ NoSuchFieldError -> 0x003d }
            r4 = com.airbnb.lottie.model.layer.Layer.LayerType.SHAPE;	 Catch:{ NoSuchFieldError -> 0x003d }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x003d }
            r3[r4] = r0;	 Catch:{ NoSuchFieldError -> 0x003d }
        L_0x003d:
            r0 = $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType;	 Catch:{ NoSuchFieldError -> 0x0047 }
            r3 = com.airbnb.lottie.model.layer.Layer.LayerType.PRE_COMP;	 Catch:{ NoSuchFieldError -> 0x0047 }
            r3 = r3.ordinal();	 Catch:{ NoSuchFieldError -> 0x0047 }
            r0[r3] = r1;	 Catch:{ NoSuchFieldError -> 0x0047 }
        L_0x0047:
            r0 = $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType;	 Catch:{ NoSuchFieldError -> 0x0051 }
            r1 = com.airbnb.lottie.model.layer.Layer.LayerType.SOLID;	 Catch:{ NoSuchFieldError -> 0x0051 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0051 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0051 }
        L_0x0051:
            r0 = $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType;	 Catch:{ NoSuchFieldError -> 0x005c }
            r1 = com.airbnb.lottie.model.layer.Layer.LayerType.IMAGE;	 Catch:{ NoSuchFieldError -> 0x005c }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x005c }
            r2 = 4;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x005c }
        L_0x005c:
            r0 = $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType;	 Catch:{ NoSuchFieldError -> 0x0067 }
            r1 = com.airbnb.lottie.model.layer.Layer.LayerType.NULL;	 Catch:{ NoSuchFieldError -> 0x0067 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0067 }
            r2 = 5;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0067 }
        L_0x0067:
            r0 = $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType;	 Catch:{ NoSuchFieldError -> 0x0072 }
            r1 = com.airbnb.lottie.model.layer.Layer.LayerType.TEXT;	 Catch:{ NoSuchFieldError -> 0x0072 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0072 }
            r2 = 6;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0072 }
        L_0x0072:
            r0 = $SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType;	 Catch:{ NoSuchFieldError -> 0x007d }
            r1 = com.airbnb.lottie.model.layer.Layer.LayerType.UNKNOWN;	 Catch:{ NoSuchFieldError -> 0x007d }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x007d }
            r2 = 7;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x007d }
        L_0x007d:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.model.layer.BaseLayer$C01232.<clinit>():void");
        }
    }

    public abstract void drawLayer(Canvas canvas, Matrix matrix, int i);

    /* Access modifiers changed, original: 0000 */
    public void resolveChildKeyPath(KeyPath keyPath, int i, List<KeyPath> list, KeyPath keyPath2) {
    }

    public void setContents(List<Content> list, List<Content> list2) {
    }

    static BaseLayer forModel(Layer layer, LottieDrawable lottieDrawable, LottieComposition lottieComposition) {
        switch (C01232.$SwitchMap$com$airbnb$lottie$model$layer$Layer$LayerType[layer.getLayerType().ordinal()]) {
            case 1:
                return new ShapeLayer(lottieDrawable, layer);
            case 2:
                return new CompositionLayer(lottieDrawable, layer, lottieComposition.getPrecomps(layer.getRefId()), lottieComposition);
            case 3:
                return new SolidLayer(lottieDrawable, layer);
            case 4:
                return new ImageLayer(lottieDrawable, layer);
            case 5:
                return new NullLayer(lottieDrawable, layer);
            case 6:
                return new TextLayer(lottieDrawable, layer);
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown layer type ");
                stringBuilder.append(layer.getLayerType());
                Logger.warning(stringBuilder.toString());
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
        if (layer.getMatteType() == MatteType.INVERT) {
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
                BaseLayer.this.setVisible(floatKeyframeAnimation.getFloatValue() == 1.0f);
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
        if (baseKeyframeAnimation != null) {
            this.animations.add(baseKeyframeAnimation);
        }
    }

    public void removeAnimation(BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        this.animations.remove(baseKeyframeAnimation);
    }

    public void getBounds(RectF rectF, Matrix matrix, boolean z) {
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        buildParentLayerListIfNeeded();
        this.boundsMatrix.set(matrix);
        if (z) {
            List list = this.parentLayers;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.boundsMatrix.preConcat(((BaseLayer) this.parentLayers.get(size)).transform.getMatrix());
                }
            } else {
                BaseLayer baseLayer = this.parentLayer;
                if (baseLayer != null) {
                    this.boundsMatrix.preConcat(baseLayer.transform.getMatrix());
                }
            }
        }
        this.boundsMatrix.preConcat(this.transform.getMatrix());
    }

    public void draw(Canvas canvas, Matrix matrix, int i) {
        C0093L.beginSection(this.drawTraceName);
        if (!this.visible || this.layerModel.isHidden()) {
            C0093L.endSection(this.drawTraceName);
            return;
        }
        buildParentLayerListIfNeeded();
        String str = "Layer#parentMatrix";
        C0093L.beginSection(str);
        this.matrix.reset();
        this.matrix.set(matrix);
        for (int size = this.parentLayers.size() - 1; size >= 0; size--) {
            this.matrix.preConcat(((BaseLayer) this.parentLayers.get(size)).transform.getMatrix());
        }
        C0093L.endSection(str);
        i = (int) ((((((float) i) / 255.0f) * ((float) (this.transform.getOpacity() == null ? 100 : ((Integer) this.transform.getOpacity().getValue()).intValue()))) / 100.0f) * 255.0f);
        String str2 = "Layer#drawLayer";
        if (hasMatteOnThisLayer() || hasMasksOnThisLayer()) {
            str = "Layer#computeBounds";
            C0093L.beginSection(str);
            getBounds(this.rect, this.matrix, false);
            intersectBoundsWithMatte(this.rect, matrix);
            this.matrix.preConcat(this.transform.getMatrix());
            intersectBoundsWithMask(this.rect, this.matrix);
            C0093L.endSection(str);
            if (!this.rect.isEmpty()) {
                str = "Layer#saveLayer";
                C0093L.beginSection(str);
                saveLayerCompat(canvas, this.rect, this.contentPaint, true);
                C0093L.endSection(str);
                clearCanvas(canvas);
                C0093L.beginSection(str2);
                drawLayer(canvas, this.matrix, i);
                C0093L.endSection(str2);
                if (hasMasksOnThisLayer()) {
                    applyMasks(canvas, this.matrix);
                }
                String str3 = "Layer#restoreLayer";
                if (hasMatteOnThisLayer()) {
                    str2 = "Layer#drawMatte";
                    C0093L.beginSection(str2);
                    C0093L.beginSection(str);
                    saveLayerCompat(canvas, this.rect, this.mattePaint, false);
                    C0093L.endSection(str);
                    clearCanvas(canvas);
                    this.matteLayer.draw(canvas, matrix, i);
                    C0093L.beginSection(str3);
                    canvas.restore();
                    C0093L.endSection(str3);
                    C0093L.endSection(str2);
                }
                C0093L.beginSection(str3);
                canvas.restore();
                C0093L.endSection(str3);
            }
            recordRenderTime(C0093L.endSection(this.drawTraceName));
            return;
        }
        this.matrix.preConcat(this.transform.getMatrix());
        C0093L.beginSection(str2);
        drawLayer(canvas, this.matrix, i);
        C0093L.endSection(str2);
        recordRenderTime(C0093L.endSection(this.drawTraceName));
    }

    private void recordRenderTime(float f) {
        this.lottieDrawable.getComposition().getPerformanceTracker().recordRenderTime(this.layerModel.getName(), f);
    }

    private void clearCanvas(Canvas canvas) {
        String str = "Layer#clearLayer";
        C0093L.beginSection(str);
        RectF rectF = this.rect;
        canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f, this.clearPaint);
        C0093L.endSection(str);
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
                int i2 = C01232.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[mask.getMaskMode().ordinal()];
                if (i2 == 1) {
                    return;
                }
                if ((i2 != 2 && i2 != 3) || !mask.isInverted()) {
                    this.path.computeBounds(this.tempMaskBoundsRect, false);
                    if (i == 0) {
                        this.maskBoundsRect.set(this.tempMaskBoundsRect);
                    } else {
                        RectF rectF2 = this.maskBoundsRect;
                        rectF2.set(Math.min(rectF2.left, this.tempMaskBoundsRect.left), Math.min(this.maskBoundsRect.top, this.tempMaskBoundsRect.top), Math.max(this.maskBoundsRect.right, this.tempMaskBoundsRect.right), Math.max(this.maskBoundsRect.bottom, this.tempMaskBoundsRect.bottom));
                    }
                    i++;
                } else {
                    return;
                }
            }
            if (!rectF.intersect(this.maskBoundsRect)) {
                rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    private void intersectBoundsWithMatte(RectF rectF, Matrix matrix) {
        if (hasMatteOnThisLayer() && this.layerModel.getMatteType() != MatteType.INVERT) {
            this.matteBoundsRect.set(0.0f, 0.0f, 0.0f, 0.0f);
            this.matteLayer.getBounds(this.matteBoundsRect, matrix, true);
            if (!rectF.intersect(this.matteBoundsRect)) {
                rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    private void applyMasks(Canvas canvas, Matrix matrix) {
        String str = "Layer#saveLayer";
        C0093L.beginSection(str);
        int i = 0;
        saveLayerCompat(canvas, this.rect, this.dstInPaint, false);
        C0093L.endSection(str);
        while (i < this.mask.getMasks().size()) {
            Mask mask = (Mask) this.mask.getMasks().get(i);
            BaseKeyframeAnimation baseKeyframeAnimation = (BaseKeyframeAnimation) this.mask.getMaskAnimations().get(i);
            BaseKeyframeAnimation baseKeyframeAnimation2 = (BaseKeyframeAnimation) this.mask.getOpacityAnimations().get(i);
            int i2 = C01232.$SwitchMap$com$airbnb$lottie$model$content$Mask$MaskMode[mask.getMaskMode().ordinal()];
            if (i2 == 1) {
                if (i == 0) {
                    Paint paint = new Paint();
                    paint.setColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
                    canvas.drawRect(this.rect, paint);
                }
                if (mask.isInverted()) {
                    applyInvertedSubtractMask(canvas, matrix, mask, baseKeyframeAnimation, baseKeyframeAnimation2);
                } else {
                    applySubtractMask(canvas, matrix, mask, baseKeyframeAnimation, baseKeyframeAnimation2);
                }
            } else if (i2 != 2) {
                if (i2 == 3) {
                    if (mask.isInverted()) {
                        applyInvertedAddMask(canvas, matrix, mask, baseKeyframeAnimation, baseKeyframeAnimation2);
                    } else {
                        applyAddMask(canvas, matrix, mask, baseKeyframeAnimation, baseKeyframeAnimation2);
                    }
                }
            } else if (mask.isInverted()) {
                applyInvertedIntersectMask(canvas, matrix, mask, baseKeyframeAnimation, baseKeyframeAnimation2);
            } else {
                applyIntersectMask(canvas, matrix, mask, baseKeyframeAnimation, baseKeyframeAnimation2);
            }
            i++;
        }
        String str2 = "Layer#restoreLayer";
        C0093L.beginSection(str2);
        canvas.restore();
        C0093L.endSection(str2);
    }

    private void applyAddMask(Canvas canvas, Matrix matrix, Mask mask, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        this.path.set((Path) baseKeyframeAnimation.getValue());
        this.path.transform(matrix);
        this.contentPaint.setAlpha((int) (((float) ((Integer) baseKeyframeAnimation2.getValue()).intValue()) * 2.55f));
        canvas.drawPath(this.path, this.contentPaint);
    }

    private void applyInvertedAddMask(Canvas canvas, Matrix matrix, Mask mask, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        saveLayerCompat(canvas, this.rect, this.contentPaint, true);
        canvas.drawRect(this.rect, this.contentPaint);
        this.path.set((Path) baseKeyframeAnimation.getValue());
        this.path.transform(matrix);
        this.contentPaint.setAlpha((int) (((float) ((Integer) baseKeyframeAnimation2.getValue()).intValue()) * 2.55f));
        canvas.drawPath(this.path, this.dstOutPaint);
        canvas.restore();
    }

    private void applySubtractMask(Canvas canvas, Matrix matrix, Mask mask, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        this.path.set((Path) baseKeyframeAnimation.getValue());
        this.path.transform(matrix);
        canvas.drawPath(this.path, this.dstOutPaint);
    }

    private void applyInvertedSubtractMask(Canvas canvas, Matrix matrix, Mask mask, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        saveLayerCompat(canvas, this.rect, this.dstOutPaint, true);
        canvas.drawRect(this.rect, this.contentPaint);
        this.dstOutPaint.setAlpha((int) (((float) ((Integer) baseKeyframeAnimation2.getValue()).intValue()) * 2.55f));
        this.path.set((Path) baseKeyframeAnimation.getValue());
        this.path.transform(matrix);
        canvas.drawPath(this.path, this.dstOutPaint);
        canvas.restore();
    }

    private void applyIntersectMask(Canvas canvas, Matrix matrix, Mask mask, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        saveLayerCompat(canvas, this.rect, this.dstInPaint, true);
        this.path.set((Path) baseKeyframeAnimation.getValue());
        this.path.transform(matrix);
        this.contentPaint.setAlpha((int) (((float) ((Integer) baseKeyframeAnimation2.getValue()).intValue()) * 2.55f));
        canvas.drawPath(this.path, this.contentPaint);
        canvas.restore();
    }

    private void applyInvertedIntersectMask(Canvas canvas, Matrix matrix, Mask mask, BaseKeyframeAnimation<ShapeData, Path> baseKeyframeAnimation, BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2) {
        saveLayerCompat(canvas, this.rect, this.dstInPaint, true);
        canvas.drawRect(this.rect, this.contentPaint);
        this.dstOutPaint.setAlpha((int) (((float) ((Integer) baseKeyframeAnimation2.getValue()).intValue()) * 2.55f));
        this.path.set((Path) baseKeyframeAnimation.getValue());
        this.path.transform(matrix);
        canvas.drawPath(this.path, this.dstOutPaint);
        canvas.restore();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasMasksOnThisLayer() {
        MaskKeyframeAnimation maskKeyframeAnimation = this.mask;
        return (maskKeyframeAnimation == null || maskKeyframeAnimation.getMaskAnimations().isEmpty()) ? false : true;
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
        BaseLayer baseLayer = this.matteLayer;
        if (baseLayer != null) {
            this.matteLayer.setProgress(baseLayer.layerModel.getTimeStretch() * f);
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
