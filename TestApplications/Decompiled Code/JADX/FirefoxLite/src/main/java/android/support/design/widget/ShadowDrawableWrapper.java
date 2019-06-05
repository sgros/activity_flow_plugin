package android.support.design.widget;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.p004v7.graphics.drawable.DrawableWrapper;

public class ShadowDrawableWrapper extends DrawableWrapper {
    static final double COS_45 = Math.cos(Math.toRadians(45.0d));
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

    public int getOpacity() {
        return -3;
    }

    private static int toEven(float f) {
        int round = Math.round(f);
        return round % 2 == 1 ? round - 1 : round;
    }

    public void setAlpha(int i) {
        super.setAlpha(i);
        this.cornerShadowPaint.setAlpha(i);
        this.edgeShadowPaint.setAlpha(i);
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect rect) {
        this.dirty = true;
    }

    public void setShadowSize(float f, float f2) {
        if (f < 0.0f || f2 < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        f = (float) toEven(f);
        f2 = (float) toEven(f2);
        if (f > f2) {
            if (!this.printedShadowClipWarning) {
                this.printedShadowClipWarning = true;
            }
            f = f2;
        }
        if (this.rawShadowSize != f || this.rawMaxShadowSize != f2) {
            this.rawShadowSize = f;
            this.rawMaxShadowSize = f2;
            this.shadowSize = (float) Math.round(f * 1.5f);
            this.maxShadowSize = f2;
            this.dirty = true;
            invalidateSelf();
        }
    }

    public void setShadowSize(float f) {
        setShadowSize(f, this.rawMaxShadowSize);
    }

    public float getShadowSize() {
        return this.rawShadowSize;
    }

    public boolean getPadding(Rect rect) {
        int ceil = (int) Math.ceil((double) calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        int ceil2 = (int) Math.ceil((double) calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        rect.set(ceil2, ceil, ceil2, ceil);
        return true;
    }

    public static float calculateVerticalPadding(float f, float f2, boolean z) {
        return z ? (float) (((double) (f * 1.5f)) + ((1.0d - COS_45) * ((double) f2))) : f * 1.5f;
    }

    public static float calculateHorizontalPadding(float f, float f2, boolean z) {
        return z ? (float) (((double) f) + ((1.0d - COS_45) * ((double) f2))) : f;
    }

    public void draw(Canvas canvas) {
        if (this.dirty) {
            buildComponents(getBounds());
            this.dirty = false;
        }
        drawShadow(canvas);
        super.draw(canvas);
    }

    public final void setRotation(float f) {
        if (this.rotation != f) {
            this.rotation = f;
            invalidateSelf();
        }
    }

    private void drawShadow(Canvas canvas) {
        int i;
        float f;
        int i2;
        float f2;
        float f3;
        Canvas canvas2 = canvas;
        int save = canvas.save();
        canvas2.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
        float f4 = (-this.cornerRadius) - this.shadowSize;
        float f5 = this.cornerRadius;
        float f6 = f5 * 2.0f;
        Object obj = this.contentBounds.width() - f6 > 0.0f ? 1 : null;
        Object obj2 = this.contentBounds.height() - f6 > 0.0f ? 1 : null;
        float f7 = f5 / ((this.rawShadowSize - (this.rawShadowSize * 0.5f)) + f5);
        float f8 = f5 / ((this.rawShadowSize - (this.rawShadowSize * 0.25f)) + f5);
        float f9 = f5 / ((this.rawShadowSize - (this.rawShadowSize * 1.0f)) + f5);
        int save2 = canvas.save();
        canvas2.translate(this.contentBounds.left + f5, this.contentBounds.top + f5);
        canvas2.scale(f7, f8);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (obj != null) {
            canvas2.scale(1.0f / f7, 1.0f);
            i = save2;
            f = f9;
            i2 = save;
            f2 = f8;
            canvas.drawRect(0.0f, f4, this.contentBounds.width() - f6, -this.cornerRadius, this.edgeShadowPaint);
        } else {
            i = save2;
            f = f9;
            i2 = save;
            f2 = f8;
        }
        canvas2.restoreToCount(i);
        i = canvas.save();
        canvas2.translate(this.contentBounds.right - f5, this.contentBounds.bottom - f5);
        f8 = f;
        canvas2.scale(f7, f8);
        canvas2.rotate(180.0f);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (obj != null) {
            canvas2.scale(1.0f / f7, 1.0f);
            f3 = f2;
            f2 = f8;
            canvas.drawRect(0.0f, f4, this.contentBounds.width() - f6, (-this.cornerRadius) + this.shadowSize, this.edgeShadowPaint);
        } else {
            f3 = f2;
            f2 = f8;
        }
        canvas2.restoreToCount(i);
        int save3 = canvas.save();
        canvas2.translate(this.contentBounds.left + f5, this.contentBounds.bottom - f5);
        canvas2.scale(f7, f2);
        canvas2.rotate(270.0f);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (obj2 != null) {
            canvas2.scale(1.0f / f2, 1.0f);
            canvas.drawRect(0.0f, f4, this.contentBounds.height() - f6, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas2.restoreToCount(save3);
        save = canvas.save();
        canvas2.translate(this.contentBounds.right - f5, this.contentBounds.top + f5);
        f5 = f3;
        canvas2.scale(f7, f5);
        canvas2.rotate(90.0f);
        canvas2.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (obj2 != null) {
            canvas2.scale(1.0f / f5, 1.0f);
            canvas.drawRect(0.0f, f4, this.contentBounds.height() - f6, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas2.restoreToCount(save);
        canvas2.restoreToCount(i2);
    }

    private void buildShadowCorners() {
        RectF rectF = new RectF(-this.cornerRadius, -this.cornerRadius, this.cornerRadius, this.cornerRadius);
        RectF rectF2 = new RectF(rectF);
        rectF2.inset(-this.shadowSize, -this.shadowSize);
        if (this.cornerShadowPath == null) {
            this.cornerShadowPath = new Path();
        } else {
            this.cornerShadowPath.reset();
        }
        this.cornerShadowPath.setFillType(FillType.EVEN_ODD);
        this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0f);
        this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0f);
        this.cornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.cornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.cornerShadowPath.close();
        float f = -rectF2.top;
        if (f > 0.0f) {
            float f2 = this.cornerRadius / f;
            float f3 = ((1.0f - f2) / 2.0f) + f2;
            Paint paint = this.cornerShadowPaint;
            Shader shader = r8;
            Shader radialGradient = new RadialGradient(0.0f, 0.0f, f, new int[]{0, this.shadowStartColor, this.shadowMiddleColor, this.shadowEndColor}, new float[]{0.0f, f2, f3, 1.0f}, TileMode.CLAMP);
            paint.setShader(shader);
        }
        this.edgeShadowPaint.setShader(new LinearGradient(0.0f, rectF.top, 0.0f, rectF2.top, new int[]{this.shadowStartColor, this.shadowMiddleColor, this.shadowEndColor}, new float[]{0.0f, 0.5f, 1.0f}, TileMode.CLAMP));
        this.edgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(Rect rect) {
        float f = this.rawMaxShadowSize * 1.5f;
        this.contentBounds.set(((float) rect.left) + this.rawMaxShadowSize, ((float) rect.top) + f, ((float) rect.right) - this.rawMaxShadowSize, ((float) rect.bottom) - f);
        getWrappedDrawable().setBounds((int) this.contentBounds.left, (int) this.contentBounds.top, (int) this.contentBounds.right, (int) this.contentBounds.bottom);
        buildShadowCorners();
    }
}
