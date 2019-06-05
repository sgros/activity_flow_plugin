// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie.model.layer;

import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.model.content.ShapeGroup;
import java.util.ArrayList;
import com.airbnb.lottie.TextDelegate;
import android.graphics.Typeface;
import com.airbnb.lottie.model.Font;
import android.graphics.Path;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.model.DocumentData;
import android.graphics.Canvas;
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

public class TextLayer extends BaseLayer
{
    private BaseKeyframeAnimation<Integer, Integer> colorAnimation;
    private final LottieComposition composition;
    private final Map<FontCharacter, List<ContentGroup>> contentsForCharacter;
    private final Paint fillPaint;
    private final LottieDrawable lottieDrawable;
    private final Matrix matrix;
    private final RectF rectF;
    private BaseKeyframeAnimation<Integer, Integer> strokeColorAnimation;
    private final Paint strokePaint;
    private BaseKeyframeAnimation<Float, Float> strokeWidthAnimation;
    private final char[] tempCharArray;
    private final TextKeyframeAnimation textAnimation;
    private BaseKeyframeAnimation<Float, Float> trackingAnimation;
    
    TextLayer(final LottieDrawable lottieDrawable, final Layer layer) {
        super(lottieDrawable, layer);
        this.tempCharArray = new char[1];
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
        this.lottieDrawable = lottieDrawable;
        this.composition = layer.getComposition();
        (this.textAnimation = layer.getText().createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
        this.addAnimation(this.textAnimation);
        final AnimatableTextProperties textProperties = layer.getTextProperties();
        if (textProperties != null && textProperties.color != null) {
            (this.colorAnimation = textProperties.color.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            this.addAnimation(this.colorAnimation);
        }
        if (textProperties != null && textProperties.stroke != null) {
            (this.strokeColorAnimation = textProperties.stroke.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            this.addAnimation(this.strokeColorAnimation);
        }
        if (textProperties != null && textProperties.strokeWidth != null) {
            (this.strokeWidthAnimation = textProperties.strokeWidth.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            this.addAnimation(this.strokeWidthAnimation);
        }
        if (textProperties != null && textProperties.tracking != null) {
            (this.trackingAnimation = textProperties.tracking.createAnimation()).addUpdateListener((BaseKeyframeAnimation.AnimationListener)this);
            this.addAnimation(this.trackingAnimation);
        }
    }
    
    private void drawCharacter(final char[] array, final Paint paint, final Canvas canvas) {
        if (paint.getColor() == 0) {
            return;
        }
        if (paint.getStyle() == Paint$Style.STROKE && paint.getStrokeWidth() == 0.0f) {
            return;
        }
        canvas.drawText(array, 0, 1, 0.0f, 0.0f, paint);
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
    
    private void drawCharacterFromFont(final char c, final DocumentData documentData, final Canvas canvas) {
        this.tempCharArray[0] = c;
        if (documentData.strokeOverFill) {
            this.drawCharacter(this.tempCharArray, this.fillPaint, canvas);
            this.drawCharacter(this.tempCharArray, this.strokePaint, canvas);
        }
        else {
            this.drawCharacter(this.tempCharArray, this.strokePaint, canvas);
            this.drawCharacter(this.tempCharArray, this.fillPaint, canvas);
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
    
    private void drawTextGlyphs(final DocumentData documentData, final Matrix matrix, final Font font, final Canvas canvas) {
        final float n = (float)documentData.size / 100.0f;
        final float scale = Utils.getScale(matrix);
        final String text = documentData.text;
        for (int i = 0; i < text.length(); ++i) {
            final FontCharacter fontCharacter = this.composition.getCharacters().get(FontCharacter.hashFor(text.charAt(i), font.getFamily(), font.getStyle()));
            if (fontCharacter != null) {
                this.drawCharacterAsGlyph(fontCharacter, matrix, n, documentData, canvas);
                final float n2 = (float)fontCharacter.getWidth();
                final float dpScale = Utils.dpScale();
                float n3 = documentData.tracking / 10.0f;
                if (this.trackingAnimation != null) {
                    n3 += this.trackingAnimation.getValue();
                }
                canvas.translate(n2 * n * dpScale * scale + n3 * scale, 0.0f);
            }
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
        String textInternal = text;
        if (textDelegate != null) {
            textInternal = textDelegate.getTextInternal(text);
        }
        this.fillPaint.setTypeface(typeface);
        this.fillPaint.setTextSize((float)(documentData.size * Utils.dpScale()));
        this.strokePaint.setTypeface(this.fillPaint.getTypeface());
        this.strokePaint.setTextSize(this.fillPaint.getTextSize());
        for (int i = 0; i < textInternal.length(); ++i) {
            final char char1 = textInternal.charAt(i);
            this.drawCharacterFromFont(char1, documentData, canvas);
            this.tempCharArray[0] = char1;
            final float measureText = this.fillPaint.measureText(this.tempCharArray, 0, 1);
            float n = documentData.tracking / 10.0f;
            if (this.trackingAnimation != null) {
                n += this.trackingAnimation.getValue();
            }
            canvas.translate(measureText + n * scale, 0.0f);
        }
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
    
    @Override
    public <T> void addValueCallback(final T t, final LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.COLOR && this.colorAnimation != null) {
            this.colorAnimation.setValueCallback((LottieValueCallback<Integer>)lottieValueCallback);
        }
        else if (t == LottieProperty.STROKE_COLOR && this.strokeColorAnimation != null) {
            this.strokeColorAnimation.setValueCallback((LottieValueCallback<Integer>)lottieValueCallback);
        }
        else if (t == LottieProperty.STROKE_WIDTH && this.strokeWidthAnimation != null) {
            this.strokeWidthAnimation.setValueCallback((LottieValueCallback<Float>)lottieValueCallback);
        }
        else if (t == LottieProperty.TEXT_TRACKING && this.trackingAnimation != null) {
            this.trackingAnimation.setValueCallback((LottieValueCallback<Float>)lottieValueCallback);
        }
    }
    
    @Override
    void drawLayer(final Canvas canvas, final Matrix matrix, int n) {
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
        if (this.colorAnimation != null) {
            this.fillPaint.setColor((int)this.colorAnimation.getValue());
        }
        else {
            this.fillPaint.setColor(documentData.color);
        }
        if (this.strokeColorAnimation != null) {
            this.strokePaint.setColor((int)this.strokeColorAnimation.getValue());
        }
        else {
            this.strokePaint.setColor(documentData.strokeColor);
        }
        n = this.transform.getOpacity().getValue() * 255 / 100;
        this.fillPaint.setAlpha(n);
        this.strokePaint.setAlpha(n);
        if (this.strokeWidthAnimation != null) {
            this.strokePaint.setStrokeWidth((float)this.strokeWidthAnimation.getValue());
        }
        else {
            this.strokePaint.setStrokeWidth((float)(documentData.strokeWidth * Utils.dpScale() * Utils.getScale(matrix)));
        }
        if (this.lottieDrawable.useTextGlyphs()) {
            this.drawTextGlyphs(documentData, matrix, font, canvas);
        }
        else {
            this.drawTextWithFont(documentData, font, matrix, canvas);
        }
        canvas.restore();
    }
}
