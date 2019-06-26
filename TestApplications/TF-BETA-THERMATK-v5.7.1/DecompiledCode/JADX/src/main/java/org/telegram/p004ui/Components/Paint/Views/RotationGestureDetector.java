package org.telegram.p004ui.Components.Paint.Views;

import android.view.MotionEvent;

/* renamed from: org.telegram.ui.Components.Paint.Views.RotationGestureDetector */
public class RotationGestureDetector {
    private float angle;
    /* renamed from: fX */
    private float f594fX;
    /* renamed from: fY */
    private float f595fY;
    private OnRotationGestureListener mListener;
    /* renamed from: sX */
    private float f596sX;
    /* renamed from: sY */
    private float f597sY;
    private float startAngle;

    /* renamed from: org.telegram.ui.Components.Paint.Views.RotationGestureDetector$OnRotationGestureListener */
    public interface OnRotationGestureListener {
        void onRotation(RotationGestureDetector rotationGestureDetector);

        void onRotationBegin(RotationGestureDetector rotationGestureDetector);

        void onRotationEnd(RotationGestureDetector rotationGestureDetector);
    }

    public float getAngle() {
        return this.angle;
    }

    public float getStartAngle() {
        return this.startAngle;
    }

    public RotationGestureDetector(OnRotationGestureListener onRotationGestureListener) {
        this.mListener = onRotationGestureListener;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    float x = motionEvent.getX(0);
                    float y = motionEvent.getY(0);
                    this.angle = angleBetweenLines(this.f594fX, this.f595fY, this.f596sX, this.f597sY, motionEvent.getX(1), motionEvent.getY(1), x, y);
                    if (this.mListener != null) {
                        if (Float.isNaN(this.startAngle)) {
                            this.startAngle = this.angle;
                            this.mListener.onRotationBegin(this);
                        } else {
                            this.mListener.onRotation(this);
                        }
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked != 5) {
                        if (actionMasked == 6) {
                            this.startAngle = Float.NaN;
                            OnRotationGestureListener onRotationGestureListener = this.mListener;
                            if (onRotationGestureListener != null) {
                                onRotationGestureListener.onRotationEnd(this);
                            }
                        }
                    }
                }
                return true;
            }
            this.startAngle = Float.NaN;
            return true;
        }
        this.f596sX = motionEvent.getX(0);
        this.f597sY = motionEvent.getY(0);
        this.f594fX = motionEvent.getX(1);
        this.f595fY = motionEvent.getY(1);
        return true;
    }

    private float angleBetweenLines(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        f = ((float) Math.toDegrees((double) (((float) Math.atan2((double) (f2 - f4), (double) (f - f3))) - ((float) Math.atan2((double) (f6 - f8), (double) (f5 - f7)))))) % 360.0f;
        if (f < -180.0f) {
            f += 360.0f;
        }
        return f > 180.0f ? f - 360.0f : f;
    }
}
