// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.views;

import android.view.MotionEvent;
import android.graphics.Paint$Style;
import android.graphics.Bitmap$Config;
import android.graphics.drawable.BitmapDrawable;
import org.osmdroid.library.R$drawable;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Paint;

public class CustomZoomButtonsDisplay
{
    private Paint mAlphaPaint;
    private int mBitmapSize;
    private boolean mHorizontalOrVertical;
    private HorizontalPosition mHorizontalPosition;
    private final MapView mMapView;
    private float mMargin;
    private float mPadding;
    private final Point mUnrotatedPoint;
    private VerticalPosition mVerticalPosition;
    private Bitmap mZoomInBitmapDisabled;
    private Bitmap mZoomInBitmapEnabled;
    private Bitmap mZoomOutBitmapDisabled;
    private Bitmap mZoomOutBitmapEnabled;
    
    public CustomZoomButtonsDisplay(final MapView mMapView) {
        this.mUnrotatedPoint = new Point();
        this.mMapView = mMapView;
        this.setPositions(true, HorizontalPosition.CENTER, VerticalPosition.BOTTOM);
        this.setMarginPadding(0.5f, 0.5f);
    }
    
    private Bitmap getBitmap(final boolean b, final boolean b2) {
        if (this.mZoomInBitmapEnabled == null) {
            this.setBitmaps(this.getZoomBitmap(true, true), this.getZoomBitmap(true, false), this.getZoomBitmap(false, true), this.getZoomBitmap(false, false));
        }
        if (b) {
            Bitmap bitmap;
            if (b2) {
                bitmap = this.mZoomInBitmapEnabled;
            }
            else {
                bitmap = this.mZoomInBitmapDisabled;
            }
            return bitmap;
        }
        Bitmap bitmap2;
        if (b2) {
            bitmap2 = this.mZoomOutBitmapEnabled;
        }
        else {
            bitmap2 = this.mZoomOutBitmapDisabled;
        }
        return bitmap2;
    }
    
    private float getFirstLeft(int n) {
        final int n2 = CustomZoomButtonsDisplay$1.$SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$HorizontalPosition[this.mHorizontalPosition.ordinal()];
        if (n2 == 1) {
            return this.mMargin * this.mBitmapSize;
        }
        if (n2 == 2) {
            final float n3 = (float)n;
            final float mMargin = this.mMargin;
            n = this.mBitmapSize;
            final float n4 = (float)n;
            final float n5 = (float)n;
            float n6;
            if (this.mHorizontalOrVertical) {
                n6 = this.mPadding * n + n;
            }
            else {
                n6 = 0.0f;
            }
            return n3 - mMargin * n4 - n5 - n6;
        }
        if (n2 == 3) {
            final float n7 = (float)(n / 2);
            float n8;
            if (this.mHorizontalOrVertical) {
                final float mPadding = this.mPadding;
                n = this.mBitmapSize;
                n8 = mPadding * n / 2.0f + n;
            }
            else {
                n8 = (float)(this.mBitmapSize / 2);
            }
            return n7 - n8;
        }
        throw new IllegalArgumentException();
    }
    
    private float getFirstTop(int n) {
        final int n2 = CustomZoomButtonsDisplay$1.$SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$VerticalPosition[this.mVerticalPosition.ordinal()];
        if (n2 == 1) {
            return this.mMargin * this.mBitmapSize;
        }
        if (n2 == 2) {
            final float n3 = (float)n;
            final float mMargin = this.mMargin;
            n = this.mBitmapSize;
            final float n4 = (float)n;
            final float n5 = (float)n;
            float n6;
            if (this.mHorizontalOrVertical) {
                n6 = 0.0f;
            }
            else {
                n6 = this.mPadding * n + n;
            }
            return n3 - mMargin * n4 - n5 - n6;
        }
        if (n2 == 3) {
            final float n7 = (float)(n / 2);
            float n8;
            if (this.mHorizontalOrVertical) {
                n8 = (float)(this.mBitmapSize / 2);
            }
            else {
                final float mPadding = this.mPadding;
                n = this.mBitmapSize;
                n8 = mPadding * n / 2.0f + n;
            }
            return n7 - n8;
        }
        throw new IllegalArgumentException();
    }
    
    private float getTopLeft(final boolean b, final boolean b2) {
        int n;
        float n2;
        float n3;
        if (b2) {
            final float firstLeft = this.getFirstLeft(this.mMapView.getWidth());
            if (!this.mHorizontalOrVertical) {
                return firstLeft;
            }
            if (!b) {
                return firstLeft;
            }
            n = this.mBitmapSize;
            n2 = firstLeft + n;
            n3 = this.mPadding;
        }
        else {
            final float firstTop = this.getFirstTop(this.mMapView.getHeight());
            if (this.mHorizontalOrVertical) {
                return firstTop;
            }
            if (b) {
                return firstTop;
            }
            n = this.mBitmapSize;
            n2 = firstTop + n;
            n3 = this.mPadding;
        }
        return n2 + n3 * n;
    }
    
    private boolean isTouched(final int n, final int n2, final boolean b) {
        final float n3 = (float)n;
        final boolean b2 = true;
        return this.isTouched(b, true, n3) && this.isTouched(b, false, (float)n2) && b2;
    }
    
    private boolean isTouched(final boolean b, final boolean b2, final float n) {
        final float topLeft = this.getTopLeft(b, b2);
        return n >= topLeft && n <= topLeft + this.mBitmapSize;
    }
    
    public void draw(final Canvas canvas, final float n, final boolean b, final boolean b2) {
        if (n == 0.0f) {
            return;
        }
        Paint mAlphaPaint;
        if (n == 1.0f) {
            mAlphaPaint = null;
        }
        else {
            if (this.mAlphaPaint == null) {
                this.mAlphaPaint = new Paint();
            }
            this.mAlphaPaint.setAlpha((int)(n * 255.0f));
            mAlphaPaint = this.mAlphaPaint;
        }
        canvas.drawBitmap(this.getBitmap(true, b), this.getTopLeft(true, true), this.getTopLeft(true, false), mAlphaPaint);
        canvas.drawBitmap(this.getBitmap(false, b2), this.getTopLeft(false, true), this.getTopLeft(false, false), mAlphaPaint);
    }
    
    protected Bitmap getIcon(final boolean b) {
        int n;
        if (b) {
            n = R$drawable.sharp_add_black_36;
        }
        else {
            n = R$drawable.sharp_remove_black_36;
        }
        return ((BitmapDrawable)this.mMapView.getResources().getDrawable(n)).getBitmap();
    }
    
    protected Bitmap getZoomBitmap(final boolean b, final boolean b2) {
        final Bitmap icon = this.getIcon(b);
        this.mBitmapSize = icon.getWidth();
        final int mBitmapSize = this.mBitmapSize;
        final Bitmap bitmap = Bitmap.createBitmap(mBitmapSize, mBitmapSize, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        int color;
        if (b2) {
            color = -1;
        }
        else {
            color = -3355444;
        }
        paint.setColor(color);
        paint.setStyle(Paint$Style.FILL);
        final int mBitmapSize2 = this.mBitmapSize;
        canvas.drawRect(0.0f, 0.0f, (float)(mBitmapSize2 - 1), (float)(mBitmapSize2 - 1), paint);
        canvas.drawBitmap(icon, 0.0f, 0.0f, (Paint)null);
        return bitmap;
    }
    
    public boolean isTouchedRotated(final MotionEvent motionEvent, final boolean b) {
        if (this.mMapView.getMapOrientation() == 0.0f) {
            this.mUnrotatedPoint.set((int)motionEvent.getX(), (int)motionEvent.getY());
        }
        else {
            this.mMapView.getProjection().rotateAndScalePoint((int)motionEvent.getX(), (int)motionEvent.getY(), this.mUnrotatedPoint);
        }
        final Point mUnrotatedPoint = this.mUnrotatedPoint;
        return this.isTouched(mUnrotatedPoint.x, mUnrotatedPoint.y, b);
    }
    
    public void setBitmaps(final Bitmap mZoomInBitmapEnabled, final Bitmap mZoomInBitmapDisabled, final Bitmap mZoomOutBitmapEnabled, final Bitmap mZoomOutBitmapDisabled) {
        this.mZoomInBitmapEnabled = mZoomInBitmapEnabled;
        this.mZoomInBitmapDisabled = mZoomInBitmapDisabled;
        this.mZoomOutBitmapEnabled = mZoomOutBitmapEnabled;
        this.mZoomOutBitmapDisabled = mZoomOutBitmapDisabled;
        this.mBitmapSize = this.mZoomInBitmapEnabled.getWidth();
    }
    
    public void setMarginPadding(final float mMargin, final float mPadding) {
        this.mMargin = mMargin;
        this.mPadding = mPadding;
    }
    
    public void setPositions(final boolean mHorizontalOrVertical, final HorizontalPosition mHorizontalPosition, final VerticalPosition mVerticalPosition) {
        this.mHorizontalOrVertical = mHorizontalOrVertical;
        this.mHorizontalPosition = mHorizontalPosition;
        this.mVerticalPosition = mVerticalPosition;
    }
    
    public enum HorizontalPosition
    {
        CENTER, 
        LEFT, 
        RIGHT;
    }
    
    public enum VerticalPosition
    {
        BOTTOM, 
        CENTER, 
        TOP;
    }
}
