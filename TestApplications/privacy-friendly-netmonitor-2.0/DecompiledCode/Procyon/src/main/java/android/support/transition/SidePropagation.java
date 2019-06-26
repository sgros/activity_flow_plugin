// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.graphics.Rect;
import android.view.ViewGroup;
import android.support.v4.view.ViewCompat;
import android.view.View;

public class SidePropagation extends VisibilityPropagation
{
    private float mPropagationSpeed;
    private int mSide;
    
    public SidePropagation() {
        this.mPropagationSpeed = 3.0f;
        this.mSide = 80;
    }
    
    private int distance(final View view, int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        final int mSide = this.mSide;
        final int n9 = 0;
        final int n10 = 1;
        boolean b = true;
        int mSide2 = 0;
        Label_0094: {
            Label_0047: {
                if (mSide == 8388611) {
                    if (ViewCompat.getLayoutDirection(view) != 1) {
                        b = false;
                    }
                    if (!b) {
                        break Label_0047;
                    }
                }
                else {
                    if (this.mSide != 8388613) {
                        mSide2 = this.mSide;
                        break Label_0094;
                    }
                    int n11;
                    if (ViewCompat.getLayoutDirection(view) == 1) {
                        n11 = n10;
                    }
                    else {
                        n11 = 0;
                    }
                    if (n11 != 0) {
                        break Label_0047;
                    }
                }
                mSide2 = 5;
                break Label_0094;
            }
            mSide2 = 3;
        }
        if (mSide2 != 3) {
            if (mSide2 != 5) {
                if (mSide2 != 48) {
                    if (mSide2 != 80) {
                        n = n9;
                    }
                    else {
                        n = n2 - n6 + Math.abs(n3 - n);
                    }
                }
                else {
                    n = n8 - n2 + Math.abs(n3 - n);
                }
            }
            else {
                n = n - n5 + Math.abs(n4 - n2);
            }
        }
        else {
            n = n7 - n + Math.abs(n4 - n2);
        }
        return n;
    }
    
    private int getMaxDistance(final ViewGroup viewGroup) {
        final int mSide = this.mSide;
        if (mSide != 3 && mSide != 5 && mSide != 8388611 && mSide != 8388613) {
            return viewGroup.getHeight();
        }
        return viewGroup.getWidth();
    }
    
    @Override
    public long getStartDelay(final ViewGroup viewGroup, final Transition transition, TransitionValues transitionValues, final TransitionValues transitionValues2) {
        if (transitionValues == null && transitionValues2 == null) {
            return 0L;
        }
        final Rect epicenter = transition.getEpicenter();
        int n;
        if (transitionValues2 != null && this.getViewVisibility(transitionValues) != 0) {
            transitionValues = transitionValues2;
            n = 1;
        }
        else {
            n = -1;
        }
        final int viewX = this.getViewX(transitionValues);
        final int viewY = this.getViewY(transitionValues);
        final int[] array = new int[2];
        viewGroup.getLocationOnScreen(array);
        final int n2 = array[0] + Math.round(viewGroup.getTranslationX());
        final int n3 = array[1] + Math.round(viewGroup.getTranslationY());
        final int n4 = n2 + viewGroup.getWidth();
        final int n5 = n3 + viewGroup.getHeight();
        int centerX;
        int centerY;
        if (epicenter != null) {
            centerX = epicenter.centerX();
            centerY = epicenter.centerY();
        }
        else {
            final int n6 = (n2 + n4) / 2;
            centerY = (n3 + n5) / 2;
            centerX = n6;
        }
        final float n7 = this.distance((View)viewGroup, viewX, viewY, centerX, centerY, n2, n3, n4, n5) / (float)this.getMaxDistance(viewGroup);
        long duration;
        if ((duration = transition.getDuration()) < 0L) {
            duration = 300L;
        }
        return Math.round(duration * n / this.mPropagationSpeed * n7);
    }
    
    public void setPropagationSpeed(final float mPropagationSpeed) {
        if (mPropagationSpeed == 0.0f) {
            throw new IllegalArgumentException("propagationSpeed may not be 0");
        }
        this.mPropagationSpeed = mPropagationSpeed;
    }
    
    public void setSide(final int mSide) {
        this.mSide = mSide;
    }
}
