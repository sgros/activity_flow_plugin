package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.content.ContentGroup;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import com.airbnb.lottie.model.DocumentData;
import com.airbnb.lottie.model.DocumentData.Justification;
import com.airbnb.lottie.model.Font;
import com.airbnb.lottie.model.FontCharacter;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.model.content.ShapeGroup;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextLayer extends BaseLayer {
    private final LongSparseArray<String> codePointCache = new LongSparseArray();
    private BaseKeyframeAnimation<Integer, Integer> colorAnimation;
    private final LottieComposition composition;
    private final Map<FontCharacter, List<ContentGroup>> contentsForCharacter = new HashMap();
    private final Paint fillPaint = new Paint(1) {
    };
    private final LottieDrawable lottieDrawable;
    private final Matrix matrix = new Matrix();
    private final RectF rectF = new RectF();
    private final StringBuilder stringBuilder = new StringBuilder(2);
    private BaseKeyframeAnimation<Integer, Integer> strokeColorAnimation;
    private final Paint strokePaint = new Paint(1) {
    };
    private BaseKeyframeAnimation<Float, Float> strokeWidthAnimation;
    private final TextKeyframeAnimation textAnimation;
    private BaseKeyframeAnimation<Float, Float> trackingAnimation;

    /* renamed from: com.airbnb.lottie.model.layer.TextLayer$3 */
    static /* synthetic */ class C01273 {
        static final /* synthetic */ int[] $SwitchMap$com$airbnb$lottie$model$DocumentData$Justification = new int[Justification.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        static {
            /*
            r0 = com.airbnb.lottie.model.DocumentData.Justification.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$com$airbnb$lottie$model$DocumentData$Justification = r0;
            r0 = $SwitchMap$com$airbnb$lottie$model$DocumentData$Justification;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = com.airbnb.lottie.model.DocumentData.Justification.LEFT_ALIGN;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r0 = $SwitchMap$com$airbnb$lottie$model$DocumentData$Justification;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = com.airbnb.lottie.model.DocumentData.Justification.RIGHT_ALIGN;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r0 = $SwitchMap$com$airbnb$lottie$model$DocumentData$Justification;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = com.airbnb.lottie.model.DocumentData.Justification.CENTER;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r2 = 3;
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.model.layer.TextLayer$C01273.<clinit>():void");
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:11:0x0090 in {2, 7, 8, 10} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private void drawTextWithFont(com.airbnb.lottie.model.DocumentData r8, com.airbnb.lottie.model.Font r9, android.graphics.Matrix r10, android.graphics.Canvas r11) {
        /*
        r7 = this;
        r0 = com.airbnb.lottie.utils.Utils.getScale(r10);
        r1 = r7.lottieDrawable;
        r2 = r9.getFamily();
        r9 = r9.getStyle();
        r9 = r1.getTypeface(r2, r9);
        if (r9 != 0) goto L_0x0015;
        return;
        r1 = r8.text;
        r2 = r7.lottieDrawable;
        r2 = r2.getTextDelegate();
        if (r2 != 0) goto L_0x008b;
        r2 = r7.fillPaint;
        r2.setTypeface(r9);
        r9 = r7.fillPaint;
        r2 = r8.size;
        r4 = com.airbnb.lottie.utils.Utils.dpScale();
        r4 = (double) r4;
        java.lang.Double.isNaN(r4);
        r2 = r2 * r4;
        r2 = (float) r2;
        r9.setTextSize(r2);
        r9 = r7.strokePaint;
        r2 = r7.fillPaint;
        r2 = r2.getTypeface();
        r9.setTypeface(r2);
        r9 = r7.strokePaint;
        r2 = r7.fillPaint;
        r2 = r2.getTextSize();
        r9.setTextSize(r2);
        r2 = r8.lineHeight;
        r9 = (float) r2;
        r2 = com.airbnb.lottie.utils.Utils.dpScale();
        r9 = r9 * r2;
        r1 = r7.getTextLines(r1);
        r2 = r1.size();
        r3 = 0;
        if (r3 >= r2) goto L_0x008a;
        r4 = r1.get(r3);
        r4 = (java.lang.String) r4;
        r5 = r7.strokePaint;
        r5 = r5.measureText(r4);
        r6 = r8.justification;
        r7.applyJustification(r6, r11, r5);
        r5 = r2 + -1;
        r5 = (float) r5;
        r5 = r5 * r9;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = r5 / r6;
        r6 = (float) r3;
        r6 = r6 * r9;
        r6 = r6 - r5;
        r5 = 0;
        r11.translate(r5, r6);
        r7.drawFontTextLine(r4, r8, r11, r0);
        r11.setMatrix(r10);
        r3 = r3 + 1;
        goto L_0x005e;
        return;
        r2.getTextInternal(r1);
        r8 = 0;
        throw r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.model.layer.TextLayer.drawTextWithFont(com.airbnb.lottie.model.DocumentData, com.airbnb.lottie.model.Font, android.graphics.Matrix, android.graphics.Canvas):void");
    }

    TextLayer(LottieDrawable lottieDrawable, Layer layer) {
        AnimatableColorValue animatableColorValue;
        super(lottieDrawable, layer);
        this.lottieDrawable = lottieDrawable;
        this.composition = layer.getComposition();
        this.textAnimation = layer.getText().createAnimation();
        this.textAnimation.addUpdateListener(this);
        addAnimation(this.textAnimation);
        AnimatableTextProperties textProperties = layer.getTextProperties();
        if (textProperties != null) {
            animatableColorValue = textProperties.color;
            if (animatableColorValue != null) {
                this.colorAnimation = animatableColorValue.createAnimation();
                this.colorAnimation.addUpdateListener(this);
                addAnimation(this.colorAnimation);
            }
        }
        if (textProperties != null) {
            animatableColorValue = textProperties.stroke;
            if (animatableColorValue != null) {
                this.strokeColorAnimation = animatableColorValue.createAnimation();
                this.strokeColorAnimation.addUpdateListener(this);
                addAnimation(this.strokeColorAnimation);
            }
        }
        if (textProperties != null) {
            AnimatableFloatValue animatableFloatValue = textProperties.strokeWidth;
            if (animatableFloatValue != null) {
                this.strokeWidthAnimation = animatableFloatValue.createAnimation();
                this.strokeWidthAnimation.addUpdateListener(this);
                addAnimation(this.strokeWidthAnimation);
            }
        }
        if (textProperties != null) {
            AnimatableFloatValue animatableFloatValue2 = textProperties.tracking;
            if (animatableFloatValue2 != null) {
                this.trackingAnimation = animatableFloatValue2.createAnimation();
                this.trackingAnimation.addUpdateListener(this);
                addAnimation(this.trackingAnimation);
            }
        }
    }

    public void getBounds(RectF rectF, Matrix matrix, boolean z) {
        super.getBounds(rectF, matrix, z);
        rectF.set(0.0f, 0.0f, (float) this.composition.getBounds().width(), (float) this.composition.getBounds().height());
    }

    /* Access modifiers changed, original: 0000 */
    public void drawLayer(Canvas canvas, Matrix matrix, int i) {
        canvas.save();
        if (!this.lottieDrawable.useTextGlyphs()) {
            canvas.setMatrix(matrix);
        }
        DocumentData documentData = (DocumentData) this.textAnimation.getValue();
        Font font = (Font) this.composition.getFonts().get(documentData.fontName);
        if (font == null) {
            canvas.restore();
            return;
        }
        BaseKeyframeAnimation baseKeyframeAnimation = this.colorAnimation;
        if (baseKeyframeAnimation != null) {
            this.fillPaint.setColor(((Integer) baseKeyframeAnimation.getValue()).intValue());
        } else {
            this.fillPaint.setColor(documentData.color);
        }
        baseKeyframeAnimation = this.strokeColorAnimation;
        if (baseKeyframeAnimation != null) {
            this.strokePaint.setColor(((Integer) baseKeyframeAnimation.getValue()).intValue());
        } else {
            this.strokePaint.setColor(documentData.strokeColor);
        }
        int intValue = ((this.transform.getOpacity() == null ? 100 : ((Integer) this.transform.getOpacity().getValue()).intValue()) * NalUnitUtil.EXTENDED_SAR) / 100;
        this.fillPaint.setAlpha(intValue);
        this.strokePaint.setAlpha(intValue);
        baseKeyframeAnimation = this.strokeWidthAnimation;
        if (baseKeyframeAnimation != null) {
            this.strokePaint.setStrokeWidth(((Float) baseKeyframeAnimation.getValue()).floatValue());
        } else {
            float scale = Utils.getScale(matrix);
            Paint paint = this.strokePaint;
            double d = documentData.strokeWidth;
            double dpScale = (double) Utils.dpScale();
            Double.isNaN(dpScale);
            d *= dpScale;
            dpScale = (double) scale;
            Double.isNaN(dpScale);
            paint.setStrokeWidth((float) (d * dpScale));
        }
        if (this.lottieDrawable.useTextGlyphs()) {
            drawTextGlyphs(documentData, matrix, font, canvas);
        } else {
            drawTextWithFont(documentData, font, matrix, canvas);
        }
        canvas.restore();
    }

    private void drawTextGlyphs(DocumentData documentData, Matrix matrix, Font font, Canvas canvas) {
        DocumentData documentData2 = documentData;
        Canvas canvas2 = canvas;
        float f = ((float) documentData2.size) / 100.0f;
        float scale = Utils.getScale(matrix);
        float dpScale = ((float) documentData2.lineHeight) * Utils.dpScale();
        List textLines = getTextLines(documentData2.text);
        int size = textLines.size();
        int i = 0;
        while (i < size) {
            String str = (String) textLines.get(i);
            float textLineWidthForGlyphs = getTextLineWidthForGlyphs(str, font, f, scale);
            canvas.save();
            applyJustification(documentData2.justification, canvas2, textLineWidthForGlyphs);
            canvas2.translate(0.0f, (((float) i) * dpScale) - ((((float) (size - 1)) * dpScale) / 2.0f));
            int i2 = i;
            drawGlyphTextLine(str, documentData, matrix, font, canvas, scale, f);
            canvas.restore();
            i = i2 + 1;
        }
    }

    private void drawGlyphTextLine(String str, DocumentData documentData, Matrix matrix, Font font, Canvas canvas, float f, float f2) {
        for (int i = 0; i < str.length(); i++) {
            FontCharacter fontCharacter = (FontCharacter) this.composition.getCharacters().get(FontCharacter.hashFor(str.charAt(i), font.getFamily(), font.getStyle()));
            if (fontCharacter != null) {
                drawCharacterAsGlyph(fontCharacter, matrix, f2, documentData, canvas);
                float width = ((((float) fontCharacter.getWidth()) * f2) * Utils.dpScale()) * f;
                float f3 = ((float) documentData.tracking) / 10.0f;
                BaseKeyframeAnimation baseKeyframeAnimation = this.trackingAnimation;
                if (baseKeyframeAnimation != null) {
                    f3 += ((Float) baseKeyframeAnimation.getValue()).floatValue();
                }
                canvas.translate(width + (f3 * f), 0.0f);
            }
        }
    }

    private List<String> getTextLines(String str) {
        String str2 = "\r";
        return Arrays.asList(str.replaceAll("\r\n", str2).replaceAll("\n", str2).split(str2));
    }

    private void drawFontTextLine(String str, DocumentData documentData, Canvas canvas, float f) {
        int i = 0;
        while (i < str.length()) {
            String codePointToString = codePointToString(str, i);
            i += codePointToString.length();
            drawCharacterFromFont(codePointToString, documentData, canvas);
            float measureText = this.fillPaint.measureText(codePointToString, 0, 1);
            float f2 = ((float) documentData.tracking) / 10.0f;
            BaseKeyframeAnimation baseKeyframeAnimation = this.trackingAnimation;
            if (baseKeyframeAnimation != null) {
                f2 += ((Float) baseKeyframeAnimation.getValue()).floatValue();
            }
            canvas.translate(measureText + (f2 * f), 0.0f);
        }
    }

    private float getTextLineWidthForGlyphs(String str, Font font, float f, float f2) {
        float f3 = 0.0f;
        for (int i = 0; i < str.length(); i++) {
            FontCharacter fontCharacter = (FontCharacter) this.composition.getCharacters().get(FontCharacter.hashFor(str.charAt(i), font.getFamily(), font.getStyle()));
            if (fontCharacter != null) {
                double d = (double) f3;
                double width = fontCharacter.getWidth();
                double d2 = (double) f;
                Double.isNaN(d2);
                width *= d2;
                d2 = (double) Utils.dpScale();
                Double.isNaN(d2);
                width *= d2;
                d2 = (double) f2;
                Double.isNaN(d2);
                width *= d2;
                Double.isNaN(d);
                f3 = (float) (d + width);
            }
        }
        return f3;
    }

    private void applyJustification(Justification justification, Canvas canvas, float f) {
        int i = C01273.$SwitchMap$com$airbnb$lottie$model$DocumentData$Justification[justification.ordinal()];
        if (i == 1) {
            return;
        }
        if (i == 2) {
            canvas.translate(-f, 0.0f);
        } else if (i == 3) {
            canvas.translate((-f) / 2.0f, 0.0f);
        }
    }

    private void drawCharacterAsGlyph(FontCharacter fontCharacter, Matrix matrix, float f, DocumentData documentData, Canvas canvas) {
        List contentsForCharacter = getContentsForCharacter(fontCharacter);
        for (int i = 0; i < contentsForCharacter.size(); i++) {
            Path path = ((ContentGroup) contentsForCharacter.get(i)).getPath();
            path.computeBounds(this.rectF, false);
            this.matrix.set(matrix);
            this.matrix.preTranslate(0.0f, ((float) (-documentData.baselineShift)) * Utils.dpScale());
            this.matrix.preScale(f, f);
            path.transform(this.matrix);
            if (documentData.strokeOverFill) {
                drawGlyph(path, this.fillPaint, canvas);
                drawGlyph(path, this.strokePaint, canvas);
            } else {
                drawGlyph(path, this.strokePaint, canvas);
                drawGlyph(path, this.fillPaint, canvas);
            }
        }
    }

    private void drawGlyph(Path path, Paint paint, Canvas canvas) {
        if (paint.getColor() != 0) {
            if (paint.getStyle() != Style.STROKE || paint.getStrokeWidth() != 0.0f) {
                canvas.drawPath(path, paint);
            }
        }
    }

    private void drawCharacterFromFont(String str, DocumentData documentData, Canvas canvas) {
        if (documentData.strokeOverFill) {
            drawCharacter(str, this.fillPaint, canvas);
            drawCharacter(str, this.strokePaint, canvas);
            return;
        }
        drawCharacter(str, this.strokePaint, canvas);
        drawCharacter(str, this.fillPaint, canvas);
    }

    private void drawCharacter(String str, Paint paint, Canvas canvas) {
        if (paint.getColor() != 0) {
            if (paint.getStyle() != Style.STROKE || paint.getStrokeWidth() != 0.0f) {
                canvas.drawText(str, 0, str.length(), 0.0f, 0.0f, paint);
            }
        }
    }

    private List<ContentGroup> getContentsForCharacter(FontCharacter fontCharacter) {
        if (this.contentsForCharacter.containsKey(fontCharacter)) {
            return (List) this.contentsForCharacter.get(fontCharacter);
        }
        List shapes = fontCharacter.getShapes();
        int size = shapes.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new ContentGroup(this.lottieDrawable, this, (ShapeGroup) shapes.get(i)));
        }
        this.contentsForCharacter.put(fontCharacter, arrayList);
        return arrayList;
    }

    private String codePointToString(String str, int i) {
        int codePointAt = str.codePointAt(i);
        int charCount = Character.charCount(codePointAt) + i;
        while (charCount < str.length()) {
            int codePointAt2 = str.codePointAt(charCount);
            if (!isModifier(codePointAt2)) {
                break;
            }
            charCount += Character.charCount(codePointAt2);
            codePointAt = (codePointAt * 31) + codePointAt2;
        }
        long j = (long) codePointAt;
        if (this.codePointCache.containsKey(j)) {
            return (String) this.codePointCache.get(j);
        }
        this.stringBuilder.setLength(0);
        while (i < charCount) {
            codePointAt = str.codePointAt(i);
            this.stringBuilder.appendCodePoint(codePointAt);
            i += Character.charCount(codePointAt);
        }
        str = this.stringBuilder.toString();
        this.codePointCache.put(j, str);
        return str;
    }

    private boolean isModifier(int i) {
        return Character.getType(i) == 16 || Character.getType(i) == 27 || Character.getType(i) == 6 || Character.getType(i) == 28 || Character.getType(i) == 19;
    }

    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        BaseKeyframeAnimation baseKeyframeAnimation;
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.COLOR) {
            baseKeyframeAnimation = this.colorAnimation;
            if (baseKeyframeAnimation != null) {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
                return;
            }
        }
        if (t == LottieProperty.STROKE_COLOR) {
            baseKeyframeAnimation = this.strokeColorAnimation;
            if (baseKeyframeAnimation != null) {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
                return;
            }
        }
        if (t == LottieProperty.STROKE_WIDTH) {
            baseKeyframeAnimation = this.strokeWidthAnimation;
            if (baseKeyframeAnimation != null) {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
                return;
            }
        }
        if (t == LottieProperty.TEXT_TRACKING) {
            BaseKeyframeAnimation baseKeyframeAnimation2 = this.trackingAnimation;
            if (baseKeyframeAnimation2 != null) {
                baseKeyframeAnimation2.setValueCallback(lottieValueCallback);
            }
        }
    }
}
