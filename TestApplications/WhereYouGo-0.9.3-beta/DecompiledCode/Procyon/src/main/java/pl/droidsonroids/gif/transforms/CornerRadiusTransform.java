// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif.transforms;

import android.graphics.Matrix;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.graphics.Shader;
import android.graphics.RectF;

public class CornerRadiusTransform implements Transform
{
    private float mCornerRadius;
    private final RectF mDstRectF;
    private Shader mShader;
    
    public CornerRadiusTransform(@FloatRange(from = 0.0) final float cornerRadius) {
        this.mDstRectF = new RectF();
        this.setCornerRadius(cornerRadius);
    }
    
    @FloatRange(from = 0.0)
    public float getCornerRadius() {
        return this.mCornerRadius;
    }
    
    @Override
    public void onBoundsChange(final Rect rect) {
        this.mDstRectF.set(rect);
        this.mShader = null;
    }
    
    @Override
    public void onDraw(final Canvas canvas, final Paint paint, final Bitmap bitmap) {
        if (this.mCornerRadius == 0.0f) {
            canvas.drawBitmap(bitmap, (Rect)null, this.mDstRectF, paint);
        }
        else {
            if (this.mShader == null) {
                this.mShader = (Shader)new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
                final Matrix localMatrix = new Matrix();
                localMatrix.setTranslate(this.mDstRectF.left, this.mDstRectF.top);
                localMatrix.preScale(this.mDstRectF.width() / bitmap.getWidth(), this.mDstRectF.height() / bitmap.getHeight());
                this.mShader.setLocalMatrix(localMatrix);
            }
            paint.setShader(this.mShader);
            canvas.drawRoundRect(this.mDstRectF, this.mCornerRadius, this.mCornerRadius, paint);
        }
    }
    
    public void setCornerRadius(@FloatRange(from = 0.0) float max) {
        max = Math.max(0.0f, max);
        if (max != this.mCornerRadius) {
            this.mCornerRadius = max;
            this.mShader = null;
        }
    }
}
