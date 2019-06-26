// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint.Views;

import android.view.MotionEvent;

public class RotationGestureDetector
{
    private float angle;
    private float fX;
    private float fY;
    private OnRotationGestureListener mListener;
    private float sX;
    private float sY;
    private float startAngle;
    
    public RotationGestureDetector(final OnRotationGestureListener mListener) {
        this.mListener = mListener;
    }
    
    private float angleBetweenLines(float n, float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8) {
        n2 = (n = (float)Math.toDegrees((float)Math.atan2(n2 - n4, n - n3) - (float)Math.atan2(n6 - n8, n5 - n7)) % 360.0f);
        if (n2 < -180.0f) {
            n = n2 + 360.0f;
        }
        n2 = n;
        if (n > 180.0f) {
            n2 = n - 360.0f;
        }
        return n2;
    }
    
    public float getAngle() {
        return this.angle;
    }
    
    public float getStartAngle() {
        return this.startAngle;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        Label_0191: {
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            if (actionMasked == 5) {
                                break Label_0191;
                            }
                            if (actionMasked != 6) {
                                return true;
                            }
                            this.startAngle = Float.NaN;
                            final OnRotationGestureListener mListener = this.mListener;
                            if (mListener != null) {
                                mListener.onRotationEnd(this);
                                return true;
                            }
                            return true;
                        }
                    }
                    else {
                        this.angle = this.angleBetweenLines(this.fX, this.fY, this.sX, this.sY, motionEvent.getX(1), motionEvent.getY(1), motionEvent.getX(0), motionEvent.getY(0));
                        if (this.mListener == null) {
                            return true;
                        }
                        if (Float.isNaN(this.startAngle)) {
                            this.startAngle = this.angle;
                            this.mListener.onRotationBegin(this);
                            return true;
                        }
                        this.mListener.onRotation(this);
                        return true;
                    }
                }
                this.startAngle = Float.NaN;
                return true;
            }
        }
        this.sX = motionEvent.getX(0);
        this.sY = motionEvent.getY(0);
        this.fX = motionEvent.getX(1);
        this.fY = motionEvent.getY(1);
        return true;
    }
    
    public interface OnRotationGestureListener
    {
        void onRotation(final RotationGestureDetector p0);
        
        void onRotationBegin(final RotationGestureDetector p0);
        
        void onRotationEnd(final RotationGestureDetector p0);
    }
}
