package org.osmdroid.views;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import org.osmdroid.library.R$drawable;

public class CustomZoomButtonsDisplay {
    private Paint mAlphaPaint;
    private int mBitmapSize;
    private boolean mHorizontalOrVertical;
    private HorizontalPosition mHorizontalPosition;
    private final MapView mMapView;
    private float mMargin;
    private float mPadding;
    private final Point mUnrotatedPoint = new Point();
    private VerticalPosition mVerticalPosition;
    private Bitmap mZoomInBitmapDisabled;
    private Bitmap mZoomInBitmapEnabled;
    private Bitmap mZoomOutBitmapDisabled;
    private Bitmap mZoomOutBitmapEnabled;

    /* renamed from: org.osmdroid.views.CustomZoomButtonsDisplay$1 */
    static /* synthetic */ class C02711 {
        /* renamed from: $SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$HorizontalPosition */
        static final /* synthetic */ int[] f47xee29485 = new int[HorizontalPosition.values().length];
        /* renamed from: $SwitchMap$org$osmdroid$views$CustomZoomButtonsDisplay$VerticalPosition */
        static final /* synthetic */ int[] f48x4ecae3d7 = new int[VerticalPosition.values().length];

        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|20) */
        /* JADX WARNING: Missing block: B:21:?, code skipped:
            return;
     */
        static {
            /*
            r0 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.values();
            r0 = r0.length;
            r0 = new int[r0];
            f48x4ecae3d7 = r0;
            r0 = 1;
            r1 = f48x4ecae3d7;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.TOP;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = r2.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1[r2] = r0;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r1 = 2;
            r2 = f48x4ecae3d7;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.BOTTOM;	 Catch:{ NoSuchFieldError -> 0x001f }
            r3 = r3.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2[r3] = r1;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r2 = 3;
            r3 = f48x4ecae3d7;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = org.osmdroid.views.CustomZoomButtonsDisplay.VerticalPosition.CENTER;	 Catch:{ NoSuchFieldError -> 0x002a }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r3[r4] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r3 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.values();
            r3 = r3.length;
            r3 = new int[r3];
            f47xee29485 = r3;
            r3 = f47xee29485;	 Catch:{ NoSuchFieldError -> 0x003d }
            r4 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.LEFT;	 Catch:{ NoSuchFieldError -> 0x003d }
            r4 = r4.ordinal();	 Catch:{ NoSuchFieldError -> 0x003d }
            r3[r4] = r0;	 Catch:{ NoSuchFieldError -> 0x003d }
        L_0x003d:
            r0 = f47xee29485;	 Catch:{ NoSuchFieldError -> 0x0047 }
            r3 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.RIGHT;	 Catch:{ NoSuchFieldError -> 0x0047 }
            r3 = r3.ordinal();	 Catch:{ NoSuchFieldError -> 0x0047 }
            r0[r3] = r1;	 Catch:{ NoSuchFieldError -> 0x0047 }
        L_0x0047:
            r0 = f47xee29485;	 Catch:{ NoSuchFieldError -> 0x0051 }
            r1 = org.osmdroid.views.CustomZoomButtonsDisplay.HorizontalPosition.CENTER;	 Catch:{ NoSuchFieldError -> 0x0051 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0051 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0051 }
        L_0x0051:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.osmdroid.views.CustomZoomButtonsDisplay$C02711.<clinit>():void");
        }
    }

    public enum HorizontalPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalPosition {
        TOP,
        CENTER,
        BOTTOM
    }

    public CustomZoomButtonsDisplay(MapView mapView) {
        this.mMapView = mapView;
        setPositions(true, HorizontalPosition.CENTER, VerticalPosition.BOTTOM);
        setMarginPadding(0.5f, 0.5f);
    }

    public void setPositions(boolean z, HorizontalPosition horizontalPosition, VerticalPosition verticalPosition) {
        this.mHorizontalOrVertical = z;
        this.mHorizontalPosition = horizontalPosition;
        this.mVerticalPosition = verticalPosition;
    }

    public void setMarginPadding(float f, float f2) {
        this.mMargin = f;
        this.mPadding = f2;
    }

    public void setBitmaps(Bitmap bitmap, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4) {
        this.mZoomInBitmapEnabled = bitmap;
        this.mZoomInBitmapDisabled = bitmap2;
        this.mZoomOutBitmapEnabled = bitmap3;
        this.mZoomOutBitmapDisabled = bitmap4;
        this.mBitmapSize = this.mZoomInBitmapEnabled.getWidth();
    }

    /* Access modifiers changed, original: protected */
    public Bitmap getZoomBitmap(boolean z, boolean z2) {
        Bitmap icon = getIcon(z);
        this.mBitmapSize = icon.getWidth();
        int i = this.mBitmapSize;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColor(z2 ? -1 : -3355444);
        paint.setStyle(Style.FILL);
        int i2 = this.mBitmapSize;
        canvas.drawRect(0.0f, 0.0f, (float) (i2 - 1), (float) (i2 - 1), paint);
        canvas.drawBitmap(icon, 0.0f, 0.0f, null);
        return createBitmap;
    }

    /* Access modifiers changed, original: protected */
    public Bitmap getIcon(boolean z) {
        return ((BitmapDrawable) this.mMapView.getResources().getDrawable(z ? R$drawable.sharp_add_black_36 : R$drawable.sharp_remove_black_36)).getBitmap();
    }

    public void draw(Canvas canvas, float f, boolean z, boolean z2) {
        if (f != 0.0f) {
            Paint paint;
            if (f == 1.0f) {
                paint = null;
            } else {
                if (this.mAlphaPaint == null) {
                    this.mAlphaPaint = new Paint();
                }
                this.mAlphaPaint.setAlpha((int) (f * 255.0f));
                paint = this.mAlphaPaint;
            }
            canvas.drawBitmap(getBitmap(true, z), getTopLeft(true, true), getTopLeft(true, false), paint);
            canvas.drawBitmap(getBitmap(false, z2), getTopLeft(false, true), getTopLeft(false, false), paint);
        }
    }

    private float getTopLeft(boolean z, boolean z2) {
        float firstLeft;
        int i;
        float f;
        if (z2) {
            firstLeft = getFirstLeft(this.mMapView.getWidth());
            if (!this.mHorizontalOrVertical || !z) {
                return firstLeft;
            }
            i = this.mBitmapSize;
            firstLeft += (float) i;
            f = this.mPadding;
        } else {
            firstLeft = getFirstTop(this.mMapView.getHeight());
            if (this.mHorizontalOrVertical || z) {
                return firstLeft;
            }
            i = this.mBitmapSize;
            firstLeft += (float) i;
            f = this.mPadding;
        }
        return firstLeft + (f * ((float) i));
    }

    private float getFirstLeft(int i) {
        int i2 = C02711.f47xee29485[this.mHorizontalPosition.ordinal()];
        if (i2 == 1) {
            return this.mMargin * ((float) this.mBitmapSize);
        }
        float f;
        float f2;
        int i3;
        if (i2 == 2) {
            f = (float) i;
            f2 = this.mMargin;
            i3 = this.mBitmapSize;
            return ((f - (f2 * ((float) i3))) - ((float) i3)) - (this.mHorizontalOrVertical ? (this.mPadding * ((float) i3)) + ((float) i3) : 0.0f);
        } else if (i2 == 3) {
            f = (float) (i / 2);
            if (this.mHorizontalOrVertical) {
                f2 = this.mPadding;
                i3 = this.mBitmapSize;
                f2 = ((f2 * ((float) i3)) / 2.0f) + ((float) i3);
            } else {
                f2 = (float) (this.mBitmapSize / 2);
            }
            return f - f2;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private float getFirstTop(int i) {
        int i2 = C02711.f48x4ecae3d7[this.mVerticalPosition.ordinal()];
        if (i2 == 1) {
            return this.mMargin * ((float) this.mBitmapSize);
        }
        float f;
        float f2;
        int i3;
        if (i2 == 2) {
            f = (float) i;
            f2 = this.mMargin;
            i3 = this.mBitmapSize;
            return ((f - (f2 * ((float) i3))) - ((float) i3)) - (this.mHorizontalOrVertical ? 0.0f : (this.mPadding * ((float) i3)) + ((float) i3));
        } else if (i2 == 3) {
            f = (float) (i / 2);
            if (this.mHorizontalOrVertical) {
                f2 = (float) (this.mBitmapSize / 2);
            } else {
                f2 = this.mPadding;
                i3 = this.mBitmapSize;
                f2 = ((f2 * ((float) i3)) / 2.0f) + ((float) i3);
            }
            return f - f2;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Bitmap getBitmap(boolean z, boolean z2) {
        if (this.mZoomInBitmapEnabled == null) {
            setBitmaps(getZoomBitmap(true, true), getZoomBitmap(true, false), getZoomBitmap(false, true), getZoomBitmap(false, false));
        }
        if (z) {
            return z2 ? this.mZoomInBitmapEnabled : this.mZoomInBitmapDisabled;
        }
        return z2 ? this.mZoomOutBitmapEnabled : this.mZoomOutBitmapDisabled;
    }

    public boolean isTouchedRotated(MotionEvent motionEvent, boolean z) {
        if (this.mMapView.getMapOrientation() == 0.0f) {
            this.mUnrotatedPoint.set((int) motionEvent.getX(), (int) motionEvent.getY());
        } else {
            this.mMapView.getProjection().rotateAndScalePoint((int) motionEvent.getX(), (int) motionEvent.getY(), this.mUnrotatedPoint);
        }
        Point point = this.mUnrotatedPoint;
        return isTouched(point.x, point.y, z);
    }

    private boolean isTouched(int i, int i2, boolean z) {
        if (isTouched(z, true, (float) i) && isTouched(z, false, (float) i2)) {
            return true;
        }
        return false;
    }

    private boolean isTouched(boolean z, boolean z2, float f) {
        float topLeft = getTopLeft(z, z2);
        return f >= topLeft && f <= topLeft + ((float) this.mBitmapSize);
    }
}
