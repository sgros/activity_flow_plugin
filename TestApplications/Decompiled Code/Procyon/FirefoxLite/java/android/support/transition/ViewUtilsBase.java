// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.view.ViewParent;
import android.graphics.Matrix;
import android.view.View;

class ViewUtilsBase
{
    public void clearNonTransitionAlpha(final View view) {
        if (view.getVisibility() == 0) {
            view.setTag(R.id.save_non_transition_alpha, (Object)null);
        }
    }
    
    public float getTransitionAlpha(final View view) {
        final Float n = (Float)view.getTag(R.id.save_non_transition_alpha);
        if (n != null) {
            return view.getAlpha() / n;
        }
        return view.getAlpha();
    }
    
    public void saveNonTransitionAlpha(final View view) {
        if (view.getTag(R.id.save_non_transition_alpha) == null) {
            view.setTag(R.id.save_non_transition_alpha, (Object)view.getAlpha());
        }
    }
    
    public void setLeftTopRightBottom(final View view, final int left, final int top, final int right, final int bottom) {
        view.setLeft(left);
        view.setTop(top);
        view.setRight(right);
        view.setBottom(bottom);
    }
    
    public void setTransitionAlpha(final View view, final float alpha) {
        final Float n = (Float)view.getTag(R.id.save_non_transition_alpha);
        if (n != null) {
            view.setAlpha(n * alpha);
        }
        else {
            view.setAlpha(alpha);
        }
    }
    
    public void transformMatrixToGlobal(final View view, final Matrix matrix) {
        final ViewParent parent = view.getParent();
        if (parent instanceof View) {
            final View view2 = (View)parent;
            this.transformMatrixToGlobal(view2, matrix);
            matrix.preTranslate((float)(-view2.getScrollX()), (float)(-view2.getScrollY()));
        }
        matrix.preTranslate((float)view.getLeft(), (float)view.getTop());
        final Matrix matrix2 = view.getMatrix();
        if (!matrix2.isIdentity()) {
            matrix.preConcat(matrix2);
        }
    }
    
    public void transformMatrixToLocal(final View view, final Matrix matrix) {
        final ViewParent parent = view.getParent();
        if (parent instanceof View) {
            final View view2 = (View)parent;
            this.transformMatrixToLocal(view2, matrix);
            matrix.postTranslate((float)view2.getScrollX(), (float)view2.getScrollY());
        }
        matrix.postTranslate((float)view.getLeft(), (float)view.getTop());
        final Matrix matrix2 = view.getMatrix();
        if (!matrix2.isIdentity()) {
            final Matrix matrix3 = new Matrix();
            if (matrix2.invert(matrix3)) {
                matrix.postConcat(matrix3);
            }
        }
    }
}
