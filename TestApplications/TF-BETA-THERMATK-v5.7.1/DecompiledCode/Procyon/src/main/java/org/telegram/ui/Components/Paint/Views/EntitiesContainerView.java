// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint.Views;

import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector$OnScaleGestureListener;
import android.widget.FrameLayout;

public class EntitiesContainerView extends FrameLayout implements ScaleGestureDetector$OnScaleGestureListener, OnRotationGestureListener
{
    private EntitiesContainerViewDelegate delegate;
    private ScaleGestureDetector gestureDetector;
    private boolean hasTransformed;
    private float previousAngle;
    private float previousScale;
    private RotationGestureDetector rotationGestureDetector;
    
    public EntitiesContainerView(final Context context, final EntitiesContainerViewDelegate delegate) {
        super(context);
        this.previousScale = 1.0f;
        this.gestureDetector = new ScaleGestureDetector(context, (ScaleGestureDetector$OnScaleGestureListener)this);
        this.rotationGestureDetector = new RotationGestureDetector((RotationGestureDetector.OnRotationGestureListener)this);
        this.delegate = delegate;
    }
    
    public void bringViewToFront(final EntityView entityView) {
        if (this.indexOfChild((View)entityView) != this.getChildCount() - 1) {
            this.removeView((View)entityView);
            this.addView((View)entityView, this.getChildCount());
        }
    }
    
    public int entitiesCount() {
        int i = 0;
        int n = 0;
        while (i < this.getChildCount()) {
            if (this.getChildAt(i) instanceof EntityView) {
                ++n;
            }
            ++i;
        }
        return n;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return motionEvent.getPointerCount() == 2 && this.delegate.shouldReceiveTouches();
    }
    
    public void onRotation(final RotationGestureDetector rotationGestureDetector) {
        final EntityView onSelectedEntityRequest = this.delegate.onSelectedEntityRequest();
        final float angle = rotationGestureDetector.getAngle();
        onSelectedEntityRequest.rotate(onSelectedEntityRequest.getRotation() + (this.previousAngle - angle));
        this.previousAngle = angle;
    }
    
    public void onRotationBegin(final RotationGestureDetector rotationGestureDetector) {
        this.previousAngle = rotationGestureDetector.getStartAngle();
        this.hasTransformed = true;
    }
    
    public void onRotationEnd(final RotationGestureDetector rotationGestureDetector) {
    }
    
    public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
        final float scaleFactor = scaleGestureDetector.getScaleFactor();
        this.delegate.onSelectedEntityRequest().scale(scaleFactor / this.previousScale);
        this.previousScale = scaleFactor;
        return false;
    }
    
    public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
        this.previousScale = 1.0f;
        return this.hasTransformed = true;
    }
    
    public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.delegate.onSelectedEntityRequest() == null) {
            return false;
        }
        if (motionEvent.getPointerCount() == 1) {
            final int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.hasTransformed = false;
            }
            else if (actionMasked == 1 || actionMasked == 2) {
                if (!this.hasTransformed) {
                    final EntitiesContainerViewDelegate delegate = this.delegate;
                    if (delegate != null) {
                        delegate.onEntityDeselect();
                    }
                }
                return false;
            }
        }
        this.gestureDetector.onTouchEvent(motionEvent);
        this.rotationGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    
    public interface EntitiesContainerViewDelegate
    {
        void onEntityDeselect();
        
        EntityView onSelectedEntityRequest();
        
        boolean shouldReceiveTouches();
    }
}
