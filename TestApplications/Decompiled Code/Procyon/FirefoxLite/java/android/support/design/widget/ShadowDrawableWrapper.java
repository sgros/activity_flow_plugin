// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.RadialGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Path$FillType;
import android.graphics.Rect;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.graphics.drawable.DrawableWrapper;

public class ShadowDrawableWrapper extends DrawableWrapper
{
    static final double COS_45;
    private boolean addPaddingForCorners;
    final RectF contentBounds;
    float cornerRadius;
    final Paint cornerShadowPaint;
    Path cornerShadowPath;
    private boolean dirty;
    final Paint edgeShadowPaint;
    float maxShadowSize;
    private boolean printedShadowClipWarning;
    float rawMaxShadowSize;
    float rawShadowSize;
    private float rotation;
    private final int shadowEndColor;
    private final int shadowMiddleColor;
    float shadowSize;
    private final int shadowStartColor;
    
    static {
        COS_45 = Math.cos(Math.toRadians(45.0));
    }
    
    private void buildComponents(final Rect rect) {
        final float n = this.rawMaxShadowSize * 1.5f;
        this.contentBounds.set(rect.left + this.rawMaxShadowSize, rect.top + n, rect.right - this.rawMaxShadowSize, rect.bottom - n);
        this.getWrappedDrawable().setBounds((int)this.contentBounds.left, (int)this.contentBounds.top, (int)this.contentBounds.right, (int)this.contentBounds.bottom);
        this.buildShadowCorners();
    }
    
    private void buildShadowCorners() {
        final RectF rectF = new RectF(-this.cornerRadius, -this.cornerRadius, this.cornerRadius, this.cornerRadius);
        final RectF rectF2 = new RectF(rectF);
        rectF2.inset(-this.shadowSize, -this.shadowSize);
        if (this.cornerShadowPath == null) {
            this.cornerShadowPath = new Path();
        }
        else {
            this.cornerShadowPath.reset();
        }
        this.cornerShadowPath.setFillType(Path$FillType.EVEN_ODD);
        this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0f);
        this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0f);
        this.cornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.cornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.cornerShadowPath.close();
        final float n = -rectF2.top;
        if (n > 0.0f) {
            final float n2 = this.cornerRadius / n;
            this.cornerShadowPaint.setShader((Shader)new RadialGradient(0.0f, 0.0f, n, new int[] { 0, this.shadowStartColor, this.shadowMiddleColor, this.shadowEndColor }, new float[] { 0.0f, n2, (1.0f - n2) / 2.0f + n2, 1.0f }, Shader$TileMode.CLAMP));
        }
        this.edgeShadowPaint.setShader((Shader)new LinearGradient(0.0f, rectF.top, 0.0f, rectF2.top, new int[] { this.shadowStartColor, this.shadowMiddleColor, this.shadowEndColor }, new float[] { 0.0f, 0.5f, 1.0f }, Shader$TileMode.CLAMP));
        this.edgeShadowPaint.setAntiAlias(false);
    }
    
    public static float calculateHorizontalPadding(final float n, final float n2, final boolean b) {
        if (b) {
            return (float)(n + (1.0 - ShadowDrawableWrapper.COS_45) * n2);
        }
        return n;
    }
    
    public static float calculateVerticalPadding(final float n, final float n2, final boolean b) {
        if (b) {
            return (float)(n * 1.5f + (1.0 - ShadowDrawableWrapper.COS_45) * n2);
        }
        return n * 1.5f;
    }
    
    private void drawShadow(final Canvas canvas) {
        final int save = canvas.save();
        canvas.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
        final float n = -this.cornerRadius - this.shadowSize;
        final float cornerRadius = this.cornerRadius;
        final float width = this.contentBounds.width();
        final float n2 = cornerRadius * 2.0f;
        final boolean b = width - n2 > 0.0f;
        final boolean b2 = this.contentBounds.height() - n2 > 0.0f;
        final float rawShadowSize = this.rawShadowSize;
        final float rawShadowSize2 = this.rawShadowSize;
        final float rawShadowSize3 = this.rawShadowSize;
        final float rawShadowSize4 = this.rawShadowSize;
        final float rawShadowSize5 = this.rawShadowSize;
        final float rawShadowSize6 = this.rawShadowSize;
        final float n3 = cornerRadius / (rawShadowSize3 - rawShadowSize4 * 0.5f + cornerRadius);
        final float n4 = cornerRadius / (rawShadowSize - rawShadowSize2 * 0.25f + cornerRadius);
        final float n5 = cornerRadius / (rawShadowSize5 - rawShadowSize6 * 1.0f + cornerRadius);
        final int save2 = canvas.save();
        canvas.translate(this.contentBounds.left + cornerRadius, this.contentBounds.top + cornerRadius);
        canvas.scale(n3, n4);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (b) {
            canvas.scale(1.0f / n3, 1.0f);
            canvas.drawRect(0.0f, n, this.contentBounds.width() - n2, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(save2);
        final int save3 = canvas.save();
        canvas.translate(this.contentBounds.right - cornerRadius, this.contentBounds.bottom - cornerRadius);
        canvas.scale(n3, n5);
        canvas.rotate(180.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (b) {
            canvas.scale(1.0f / n3, 1.0f);
            canvas.drawRect(0.0f, n, this.contentBounds.width() - n2, -this.cornerRadius + this.shadowSize, this.edgeShadowPaint);
        }
        canvas.restoreToCount(save3);
        final int save4 = canvas.save();
        canvas.translate(this.contentBounds.left + cornerRadius, this.contentBounds.bottom - cornerRadius);
        canvas.scale(n3, n5);
        canvas.rotate(270.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (b2) {
            canvas.scale(1.0f / n5, 1.0f);
            canvas.drawRect(0.0f, n, this.contentBounds.height() - n2, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(save4);
        final int save5 = canvas.save();
        canvas.translate(this.contentBounds.right - cornerRadius, this.contentBounds.top + cornerRadius);
        canvas.scale(n3, n4);
        canvas.rotate(90.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (b2) {
            canvas.scale(1.0f / n4, 1.0f);
            canvas.drawRect(0.0f, n, this.contentBounds.height() - n2, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(save5);
        canvas.restoreToCount(save);
    }
    
    private static int toEven(final float a) {
        int round;
        final int n = round = Math.round(a);
        if (n % 2 == 1) {
            round = n - 1;
        }
        return round;
    }
    
    @Override
    public void draw(final Canvas canvas) {
        if (this.dirty) {
            this.buildComponents(this.getBounds());
            this.dirty = false;
        }
        this.drawShadow(canvas);
        super.draw(canvas);
    }
    
    @Override
    public int getOpacity() {
        return -3;
    }
    
    @Override
    public boolean getPadding(final Rect rect) {
        final int n = (int)Math.ceil(calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        final int n2 = (int)Math.ceil(calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        rect.set(n2, n, n2, n);
        return true;
    }
    
    public float getShadowSize() {
        return this.rawShadowSize;
    }
    
    @Override
    protected void onBoundsChange(final Rect rect) {
        this.dirty = true;
    }
    
    @Override
    public void setAlpha(final int alpha) {
        super.setAlpha(alpha);
        this.cornerShadowPaint.setAlpha(alpha);
        this.edgeShadowPaint.setAlpha(alpha);
    }
    
    public final void setRotation(final float rotation) {
        if (this.rotation != rotation) {
            this.rotation = rotation;
            this.invalidateSelf();
        }
    }
    
    public void setShadowSize(final float n) {
        this.setShadowSize(n, this.rawMaxShadowSize);
    }
    
    public void setShadowSize(float rawShadowSize, float n) {
        if (rawShadowSize < 0.0f || n < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        final float n2 = (float)toEven(rawShadowSize);
        n = (float)toEven(n);
        rawShadowSize = n2;
        if (n2 > n) {
            if (!this.printedShadowClipWarning) {
                this.printedShadowClipWarning = true;
            }
            rawShadowSize = n;
        }
        if (this.rawShadowSize == rawShadowSize && this.rawMaxShadowSize == n) {
            return;
        }
        this.rawShadowSize = rawShadowSize;
        this.rawMaxShadowSize = n;
        this.shadowSize = (float)Math.round(rawShadowSize * 1.5f);
        this.maxShadowSize = n;
        this.dirty = true;
        this.invalidateSelf();
    }
}
