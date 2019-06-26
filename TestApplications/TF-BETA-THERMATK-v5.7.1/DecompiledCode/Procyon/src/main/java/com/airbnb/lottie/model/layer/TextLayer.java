// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Arrays;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.ArrayList;
import com.airbnb.lottie.TextDelegate;
import android.graphics.Typeface;
import com.airbnb.lottie.model.Font;
import android.graphics.Path;
import com.airbnb.lottie.utils.Utils;
import android.graphics.Canvas;
import com.airbnb.lottie.model.DocumentData;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import java.util.HashMap;
import android.graphics.Paint$Style;
import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import android.graphics.RectF;
import android.graphics.Matrix;
import com.airbnb.lottie.LottieDrawable;
import android.graphics.Paint;
import com.airbnb.lottie.animation.content.ContentGroup;
import java.util.List;
import com.airbnb.lottie.model.FontCharacter;
import java.util.Map;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import androidx.collection.LongSparseArray;

public class TextLayer extends BaseLayer
{
    private final LongSparseArray<String> codePointCache;
    private BaseKeyframeAnimation<Integer, Integer> colorAnimation;
    private final LottieComposition composition;
    private final Map<FontCharacter, List<ContentGroup>> contentsForCharacter;
    private final Paint fillPaint;
    private final LottieDrawable lottieDrawable;
    private final Matrix matrix;
    private final RectF rectF;
    private final StringBuilder stringBuilder;
    private BaseKeyframeAnimation<Integer, Integer> strokeColorAnimation;
    private final Paint strokePaint;
    private BaseKeyframeAnimation<Float, Float> strokeWidthAnimation;
    private final TextKeyframeAnimation textAnimation;
    private BaseKeyframeAnimation<Float, Float> trackingAnimation;
    
    TextLayer(final LottieDrawable lottieDrawable, final Layer layer) {
        super(lottieDrawable, layer);
        this.stringBuilder = new StringBuilder(2);
        this.rectF = new RectF();
        this.matrix = new Matrix();
        this.fillPaint = new Paint(1) {
            {
                this.setStyle(Paint$Style.FILL);
            }
        };
        this.strokePaint = new Paint(1) {
            {
                this.setStyle(Paint$Style.STROKE);
            }
        };
        this.contentsForCharacter = new HashMap<FontCharacter, List<ContentGroup>>();
        this.codePointCache = new LongSparseArray<String>();
        this.lottieDrawable = lottieDrawable;
        this.composition = layer.getComposition();
        (this.textAnimation = layer.getText().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.addAnimation(this.textAnimation);
        final AnimatableTextProperties textProperties = layer.getTextProperties();
        if (textProperties != null) {
            final AnimatableColorValue color = textProperties.color;
            if (color != null) {
                (this.colorAnimation = color.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                this.addAnimation(this.colorAnimation);
            }
        }
        if (textProperties != null) {
            final AnimatableColorValue stroke = textProperties.stroke;
            if (stroke != null) {
                (this.strokeColorAnimation = stroke.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                this.addAnimation(this.strokeColorAnimation);
            }
        }
        if (textProperties != null) {
            final AnimatableFloatValue strokeWidth = textProperties.strokeWidth;
            if (strokeWidth != null) {
                (this.strokeWidthAnimation = strokeWidth.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                this.addAnimation(this.strokeWidthAnimation);
            }
        }
        if (textProperties != null) {
            final AnimatableFloatValue tracking = textProperties.tracking;
            if (tracking != null) {
                (this.trackingAnimation = tracking.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
                this.addAnimation(this.trackingAnimation);
            }
        }
    }
    
    private void applyJustification(final DocumentData.Justification justification, final Canvas canvas, final float n) {
        final int n2 = TextLayer$3.$SwitchMap$com$airbnb$lottie$model$DocumentData$Justification[justification.ordinal()];
        if (n2 != 1) {
            if (n2 != 2) {
                if (n2 == 3) {
                    canvas.translate(-n / 2.0f, 0.0f);
                }
            }
            else {
                canvas.translate(-n, 0.0f);
            }
        }
    }
    
    private String codePointToString(String string, int i) {
        int codePoint;
        int j;
        int codePoint2;
        for (codePoint = string.codePointAt(i), j = Character.charCount(codePoint) + i; j < string.length(); j += Character.charCount(codePoint2), codePoint = codePoint * 31 + codePoint2) {
            codePoint2 = string.codePointAt(j);
            if (!this.isModifier(codePoint2)) {
                break;
            }
        }
        final LongSparseArray<String> codePointCache = this.codePointCache;
        final long n = codePoint;
        if (codePointCache.containsKey(n)) {
            return this.codePointCache.get(n);
        }
        this.stringBuilder.setLength(0);
        while (i < j) {
            final int codePoint3 = string.codePointAt(i);
            this.stringBuilder.appendCodePoint(codePoint3);
            i += Character.charCount(codePoint3);
        }
        string = this.stringBuilder.toString();
        this.codePointCache.put(n, string);
        return string;
    }
    
    private void drawCharacter(final String s, final Paint paint, final Canvas canvas) {
        if (paint.getColor() == 0) {
            return;
        }
        if (paint.getStyle() == Paint$Style.STROKE && paint.getStrokeWidth() == 0.0f) {
            return;
        }
        canvas.drawText(s, 0, s.length(), 0.0f, 0.0f, paint);
    }
    
    private void drawCharacterAsGlyph(final FontCharacter fontCharacter, final Matrix matrix, final float n, final DocumentData documentData, final Canvas canvas) {
        final List<ContentGroup> contentsForCharacter = this.getContentsForCharacter(fontCharacter);
        for (int i = 0; i < contentsForCharacter.size(); ++i) {
            final Path path = contentsForCharacter.get(i).getPath();
            path.computeBounds(this.rectF, false);
            this.matrix.set(matrix);
            this.matrix.preTranslate(0.0f, (float)(-documentData.baselineShift) * Utils.dpScale());
            this.matrix.preScale(n, n);
            path.transform(this.matrix);
            if (documentData.strokeOverFill) {
                this.drawGlyph(path, this.fillPaint, canvas);
                this.drawGlyph(path, this.strokePaint, canvas);
            }
            else {
                this.drawGlyph(path, this.strokePaint, canvas);
                this.drawGlyph(path, this.fillPaint, canvas);
            }
        }
    }
    
    private void drawCharacterFromFont(final String s, final DocumentData documentData, final Canvas canvas) {
        if (documentData.strokeOverFill) {
            this.drawCharacter(s, this.fillPaint, canvas);
            this.drawCharacter(s, this.strokePaint, canvas);
        }
        else {
            this.drawCharacter(s, this.strokePaint, canvas);
            this.drawCharacter(s, this.fillPaint, canvas);
        }
    }
    
    private void drawFontTextLine(final String s, final DocumentData documentData, final Canvas canvas, final float n) {
        int i = 0;
        while (i < s.length()) {
            final String codePointToString = this.codePointToString(s, i);
            i += codePointToString.length();
            this.drawCharacterFromFont(codePointToString, documentData, canvas);
            final float measureText = this.fillPaint.measureText(codePointToString, 0, 1);
            final float n2 = documentData.tracking / 10.0f;
            final BaseKeyframeAnimation<Float, Float> trackingAnimation = this.trackingAnimation;
            float n3 = n2;
            if (trackingAnimation != null) {
                n3 = n2 + trackingAnimation.getValue();
            }
            canvas.translate(measureText + n3 * n, 0.0f);
        }
    }
    
    private void drawGlyph(final Path path, final Paint paint, final Canvas canvas) {
        if (paint.getColor() == 0) {
            return;
        }
        if (paint.getStyle() == Paint$Style.STROKE && paint.getStrokeWidth() == 0.0f) {
            return;
        }
        canvas.drawPath(path, paint);
    }
    
    private void drawGlyphTextLine(final String s, final DocumentData documentData, final Matrix matrix, final Font font, final Canvas canvas, final float n, final float n2) {
        for (int i = 0; i < s.length(); ++i) {
            final FontCharacter fontCharacter = this.composition.getCharacters().get(FontCharacter.hashFor(s.charAt(i), font.getFamily(), font.getStyle()));
            if (fontCharacter != null) {
                this.drawCharacterAsGlyph(fontCharacter, matrix, n2, documentData, canvas);
                final float n3 = (float)fontCharacter.getWidth();
                final float dpScale = Utils.dpScale();
                final float n4 = documentData.tracking / 10.0f;
                final BaseKeyframeAnimation<Float, Float> trackingAnimation = this.trackingAnimation;
                float n5 = n4;
                if (trackingAnimation != null) {
                    n5 = n4 + trackingAnimation.getValue();
                }
                canvas.translate(n3 * n2 * dpScale * n + n5 * n, 0.0f);
            }
        }
    }
    
    private void drawTextGlyphs(final DocumentData documentData, final Matrix matrix, final Font font, final Canvas canvas) {
        final float n = (float)documentData.size / 100.0f;
        final float scale = Utils.getScale(matrix);
        final String text = documentData.text;
        final float n2 = (float)documentData.lineHeight * Utils.dpScale();
        final List<String> textLines = this.getTextLines(text);
        for (int size = textLines.size(), i = 0; i < size; ++i) {
            final String s = textLines.get(i);
            final float textLineWidthForGlyphs = this.getTextLineWidthForGlyphs(s, font, n, scale);
            canvas.save();
            this.applyJustification(documentData.justification, canvas, textLineWidthForGlyphs);
            canvas.translate(0.0f, i * n2 - (size - 1) * n2 / 2.0f);
            this.drawGlyphTextLine(s, documentData, matrix, font, canvas, scale, n);
            canvas.restore();
        }
    }
    
    private void drawTextWithFont(final DocumentData documentData, final Font font, final Matrix matrix, final Canvas canvas) {
        final float scale = Utils.getScale(matrix);
        final Typeface typeface = this.lottieDrawable.getTypeface(font.getFamily(), font.getStyle());
        if (typeface == null) {
            return;
        }
        final String text = documentData.text;
        final TextDelegate textDelegate = this.lottieDrawable.getTextDelegate();
        if (textDelegate == null) {
            this.fillPaint.setTypeface(typeface);
            final Paint fillPaint = this.fillPaint;
            final double size = documentData.size;
            final double v = Utils.dpScale();
            Double.isNaN(v);
            fillPaint.setTextSize((float)(size * v));
            this.strokePaint.setTypeface(this.fillPaint.getTypeface());
            this.strokePaint.setTextSize(this.fillPaint.getTextSize());
            final float n = (float)documentData.lineHeight * Utils.dpScale();
            final List<String> textLines = this.getTextLines(text);
            for (int size2 = textLines.size(), i = 0; i < size2; ++i) {
                final String s = textLines.get(i);
                this.applyJustification(documentData.justification, canvas, this.strokePaint.measureText(s));
                canvas.translate(0.0f, i * n - (size2 - 1) * n / 2.0f);
                this.drawFontTextLine(s, documentData, canvas, scale);
                canvas.setMatrix(matrix);
            }
            return;
        }
        textDelegate.getTextInternal(text);
        throw null;
    }
    
    private List<ContentGroup> getContentsForCharacter(final FontCharacter fontCharacter) {
        if (this.contentsForCharacter.containsKey(fontCharacter)) {
            return this.contentsForCharacter.get(fontCharacter);
        }
        final List<ShapeGroup> shapes = fontCharacter.getShapes();
        final int size = shapes.size();
        final ArrayList list = new ArrayList<ContentGroup>(size);
        for (int i = 0; i < size; ++i) {
            list.add(new ContentGroup(this.lottieDrawable, this, shapes.get(i)));
        }
        this.contentsForCharacter.put(fontCharacter, (ArrayList<ContentGroup>)list);
        return (List<ContentGroup>)list;
    }
    
    private float getTextLineWidthForGlyphs(final String s, final Font font, final float n, final float n2) {
        float n3 = 0.0f;
        for (int i = 0; i < s.length(); ++i) {
            final FontCharacter fontCharacter = this.composition.getCharacters().get(FontCharacter.hashFor(s.charAt(i), font.getFamily(), font.getStyle()));
            if (fontCharacter != null) {
                final double v = n3;
                final double width = fontCharacter.getWidth();
                final double v2 = n;
                Double.isNaN(v2);
                final double v3 = Utils.dpScale();
                Double.isNaN(v3);
                final double v4 = n2;
                Double.isNaN(v4);
                Double.isNaN(v);
                n3 = (float)(v + width * v2 * v3 * v4);
            }
        }
        return n3;
    }
    
    private List<String> getTextLines(final String s) {
        return Arrays.asList(s.replaceAll("\r\n", "\r").replaceAll("\n", "\r").split("\r"));
    }
    
    private boolean isModifier(final int codePoint) {
        return Character.getType(codePoint) == 16 || Character.getType(codePoint) == 27 || Character.getType(codePoint) == 6 || Character.getType(codePoint) == 28 || Character.getType(codePoint) == 19;
    }
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.COLOR) {
            final BaseKeyframeAnimation<Integer, Integer> colorAnimation = this.colorAnimation;
            if (colorAnimation != null) {
                colorAnimation.setValueCallback((LottieValueCallback<Integer>)lottieValueCallback);
                return;
            }
        }
        if (t == LottieProperty.STROKE_COLOR) {
            final BaseKeyframeAnimation<Integer, Integer> strokeColorAnimation = this.strokeColorAnimation;
            if (strokeColorAnimation != null) {
                strokeColorAnimation.setValueCallback((LottieValueCallback<Integer>)lottieValueCallback);
                return;
            }
        }
        if (t == LottieProperty.STROKE_WIDTH) {
            final BaseKeyframeAnimation<Float, Float> strokeWidthAnimation = this.strokeWidthAnimation;
            if (strokeWidthAnimation != null) {
                strokeWidthAnimation.setValueCallback((LottieValueCallback<Float>)lottieValueCallback);
                return;
            }
        }
        if (t == LottieProperty.TEXT_TRACKING) {
            final BaseKeyframeAnimation<Float, Float> trackingAnimation = this.trackingAnimation;
            if (trackingAnimation != null) {
                trackingAnimation.setValueCallback((LottieValueCallback<Float>)lottieValueCallback);
            }
        }
    }
    
    @Override
    void drawLayer(final Canvas canvas, final Matrix matrix, int intValue) {
        canvas.save();
        if (!this.lottieDrawable.useTextGlyphs()) {
            canvas.setMatrix(matrix);
        }
        final DocumentData documentData = ((BaseKeyframeAnimation<K, DocumentData>)this.textAnimation).getValue();
        final Font font = this.composition.getFonts().get(documentData.fontName);
        if (font == null) {
            canvas.restore();
            return;
        }
        final BaseKeyframeAnimation<Integer, Integer> colorAnimation = this.colorAnimation;
        if (colorAnimation != null) {
            this.fillPaint.setColor((int)colorAnimation.getValue());
        }
        else {
            this.fillPaint.setColor(documentData.color);
        }
        final BaseKeyframeAnimation<Integer, Integer> strokeColorAnimation = this.strokeColorAnimation;
        if (strokeColorAnimation != null) {
            this.strokePaint.setColor((int)strokeColorAnimation.getValue());
        }
        else {
            this.strokePaint.setColor(documentData.strokeColor);
        }
        if (super.transform.getOpacity() == null) {
            intValue = 100;
        }
        else {
            intValue = super.transform.getOpacity().getValue();
        }
        intValue = intValue * 255 / 100;
        this.fillPaint.setAlpha(intValue);
        this.strokePaint.setAlpha(intValue);
        final BaseKeyframeAnimation<Float, Float> strokeWidthAnimation = this.strokeWidthAnimation;
        if (strokeWidthAnimation != null) {
            this.strokePaint.setStrokeWidth((float)strokeWidthAnimation.getValue());
        }
        else {
            final float scale = Utils.getScale(matrix);
            final Paint strokePaint = this.strokePaint;
            final double strokeWidth = documentData.strokeWidth;
            final double v = Utils.dpScale();
            Double.isNaN(v);
            final double v2 = scale;
            Double.isNaN(v2);
            strokePaint.setStrokeWidth((float)(strokeWidth * v * v2));
        }
        if (this.lottieDrawable.useTextGlyphs()) {
            this.drawTextGlyphs(documentData, matrix, font, canvas);
        }
        else {
            this.drawTextWithFont(documentData, font, matrix, canvas);
        }
        canvas.restore();
    }
    
    @Override
    public void getBounds(final RectF rectF, final Matrix matrix, final boolean b) {
        super.getBounds(rectF, matrix, b);
        rectF.set(0.0f, 0.0f, (float)this.composition.getBounds().width(), (float)this.composition.getBounds().height());
    }
}
