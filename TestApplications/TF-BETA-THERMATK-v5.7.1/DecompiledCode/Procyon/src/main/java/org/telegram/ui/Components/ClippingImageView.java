// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.Shader;
import android.graphics.Shader$TileMode;
import org.telegram.messenger.FileLog;
import android.graphics.Matrix$ScaleToFit;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import androidx.annotation.Keep;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Matrix;
import org.telegram.messenger.ImageReceiver;
import android.graphics.BitmapShader;
import android.graphics.RectF;
import android.view.View;

public class ClippingImageView extends View
{
    private float animationProgress;
    private float[][] animationValues;
    private RectF bitmapRect;
    private BitmapShader bitmapShader;
    private ImageReceiver.BitmapHolder bmp;
    private int clipBottom;
    private int clipLeft;
    private int clipRight;
    private int clipTop;
    private RectF drawRect;
    private int imageX;
    private int imageY;
    private Matrix matrix;
    private boolean needRadius;
    private int orientation;
    private Paint paint;
    private int radius;
    private Paint roundPaint;
    private RectF roundRect;
    private Matrix shaderMatrix;
    
    public ClippingImageView(final Context context) {
        super(context);
        (this.paint = new Paint(2)).setFilterBitmap(true);
        this.matrix = new Matrix();
        this.drawRect = new RectF();
        this.bitmapRect = new RectF();
        this.roundPaint = new Paint(3);
        this.roundRect = new RectF();
        this.shaderMatrix = new Matrix();
    }
    
    @Keep
    public float getAnimationProgress() {
        return this.animationProgress;
    }
    
    public Bitmap getBitmap() {
        final ImageReceiver.BitmapHolder bmp = this.bmp;
        Bitmap bitmap;
        if (bmp != null) {
            bitmap = bmp.bitmap;
        }
        else {
            bitmap = null;
        }
        return bitmap;
    }
    
    public int getClipBottom() {
        return this.clipBottom;
    }
    
    public int getClipHorizontal() {
        return this.clipRight;
    }
    
    public int getClipLeft() {
        return this.clipLeft;
    }
    
    public int getClipRight() {
        return this.clipRight;
    }
    
    public int getClipTop() {
        return this.clipTop;
    }
    
    public int getOrientation() {
        return this.orientation;
    }
    
    public int getRadius() {
        return this.radius;
    }
    
    public void onDraw(final Canvas canvas) {
        if (this.getVisibility() != 0) {
            return;
        }
        final ImageReceiver.BitmapHolder bmp = this.bmp;
        if (bmp != null && !bmp.isRecycled()) {
            final float scaleY = this.getScaleY();
            canvas.save();
            if (this.needRadius) {
                this.shaderMatrix.reset();
                this.roundRect.set(this.imageX / scaleY, this.imageY / scaleY, this.getWidth() - this.imageX / scaleY, this.getHeight() - this.imageY / scaleY);
                this.bitmapRect.set(0.0f, 0.0f, (float)this.bmp.getWidth(), (float)this.bmp.getHeight());
                AndroidUtilities.setRectToRect(this.shaderMatrix, this.bitmapRect, this.roundRect, this.orientation, false);
                this.bitmapShader.setLocalMatrix(this.shaderMatrix);
                canvas.clipRect(this.clipLeft / scaleY, this.clipTop / scaleY, this.getWidth() - this.clipRight / scaleY, this.getHeight() - this.clipBottom / scaleY);
                final RectF roundRect = this.roundRect;
                final int radius = this.radius;
                canvas.drawRoundRect(roundRect, (float)radius, (float)radius, this.roundPaint);
            }
            else {
                final int orientation = this.orientation;
                if (orientation != 90 && orientation != 270) {
                    if (orientation == 180) {
                        this.drawRect.set((float)(-this.getWidth() / 2), (float)(-this.getHeight() / 2), (float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
                        this.matrix.setRectToRect(this.bitmapRect, this.drawRect, Matrix$ScaleToFit.FILL);
                        this.matrix.postRotate((float)this.orientation, 0.0f, 0.0f);
                        this.matrix.postTranslate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
                    }
                    else {
                        this.drawRect.set(0.0f, 0.0f, (float)this.getWidth(), (float)this.getHeight());
                        this.matrix.setRectToRect(this.bitmapRect, this.drawRect, Matrix$ScaleToFit.FILL);
                    }
                }
                else {
                    this.drawRect.set((float)(-this.getHeight() / 2), (float)(-this.getWidth() / 2), (float)(this.getHeight() / 2), (float)(this.getWidth() / 2));
                    this.matrix.setRectToRect(this.bitmapRect, this.drawRect, Matrix$ScaleToFit.FILL);
                    this.matrix.postRotate((float)this.orientation, 0.0f, 0.0f);
                    this.matrix.postTranslate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
                }
                canvas.clipRect(this.clipLeft / scaleY, this.clipTop / scaleY, this.getWidth() - this.clipRight / scaleY, this.getHeight() - this.clipBottom / scaleY);
                try {
                    canvas.drawBitmap(this.bmp.bitmap, this.matrix, this.paint);
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
            }
            canvas.restore();
        }
    }
    
    @Keep
    public void setAnimationProgress(final float animationProgress) {
        this.animationProgress = animationProgress;
        final float[][] animationValues = this.animationValues;
        this.setScaleX(animationValues[0][0] + (animationValues[1][0] - animationValues[0][0]) * this.animationProgress);
        final float[][] animationValues2 = this.animationValues;
        this.setScaleY(animationValues2[0][1] + (animationValues2[1][1] - animationValues2[0][1]) * this.animationProgress);
        final float[][] animationValues3 = this.animationValues;
        this.setTranslationX(animationValues3[0][2] + (animationValues3[1][2] - animationValues3[0][2]) * this.animationProgress);
        final float[][] animationValues4 = this.animationValues;
        this.setTranslationY(animationValues4[0][3] + (animationValues4[1][3] - animationValues4[0][3]) * this.animationProgress);
        final float[][] animationValues5 = this.animationValues;
        this.setClipHorizontal((int)(animationValues5[0][4] + (animationValues5[1][4] - animationValues5[0][4]) * this.animationProgress));
        final float[][] animationValues6 = this.animationValues;
        this.setClipTop((int)(animationValues6[0][5] + (animationValues6[1][5] - animationValues6[0][5]) * this.animationProgress));
        final float[][] animationValues7 = this.animationValues;
        this.setClipBottom((int)(animationValues7[0][6] + (animationValues7[1][6] - animationValues7[0][6]) * this.animationProgress));
        final float[][] animationValues8 = this.animationValues;
        this.setRadius((int)(animationValues8[0][7] + (animationValues8[1][7] - animationValues8[0][7]) * this.animationProgress));
        final float[][] animationValues9 = this.animationValues;
        if (animationValues9[0].length > 8) {
            this.setImageY((int)(animationValues9[0][8] + (animationValues9[1][8] - animationValues9[0][8]) * this.animationProgress));
            final float[][] animationValues10 = this.animationValues;
            this.setImageX((int)(animationValues10[0][9] + (animationValues10[1][9] - animationValues10[0][9]) * this.animationProgress));
        }
        this.invalidate();
    }
    
    public void setAnimationValues(final float[][] animationValues) {
        this.animationValues = animationValues;
    }
    
    public void setClipBottom(final int clipBottom) {
        this.clipBottom = clipBottom;
        this.invalidate();
    }
    
    public void setClipHorizontal(final int n) {
        this.clipRight = n;
        this.clipLeft = n;
        this.invalidate();
    }
    
    public void setClipLeft(final int clipLeft) {
        this.clipLeft = clipLeft;
        this.invalidate();
    }
    
    public void setClipRight(final int clipRight) {
        this.clipRight = clipRight;
        this.invalidate();
    }
    
    public void setClipTop(final int clipTop) {
        this.clipTop = clipTop;
        this.invalidate();
    }
    
    public void setClipVertical(final int n) {
        this.clipBottom = n;
        this.clipTop = n;
        this.invalidate();
    }
    
    public void setImageBitmap(final ImageReceiver.BitmapHolder bmp) {
        final ImageReceiver.BitmapHolder bmp2 = this.bmp;
        if (bmp2 != null) {
            bmp2.release();
            this.bitmapShader = null;
        }
        this.bmp = bmp;
        if (bmp != null && bmp.bitmap != null) {
            this.bitmapRect.set(0.0f, 0.0f, (float)bmp.getWidth(), (float)bmp.getHeight());
            if (this.needRadius) {
                final Bitmap bitmap = this.bmp.bitmap;
                final Shader$TileMode clamp = Shader$TileMode.CLAMP;
                this.bitmapShader = new BitmapShader(bitmap, clamp, clamp);
                this.roundPaint.setShader((Shader)this.bitmapShader);
            }
        }
        this.invalidate();
    }
    
    public void setImageX(final int imageX) {
        this.imageX = imageX;
    }
    
    public void setImageY(final int imageY) {
        this.imageY = imageY;
    }
    
    public void setNeedRadius(final boolean needRadius) {
        this.needRadius = needRadius;
    }
    
    public void setOrientation(final int orientation) {
        this.orientation = orientation;
    }
    
    public void setRadius(final int radius) {
        this.radius = radius;
    }
}
