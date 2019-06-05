package android.support.transition;

import android.graphics.Matrix;
import android.view.View;
import android.view.ViewParent;

class ViewUtilsBase {
    ViewUtilsBase() {
    }

    public void setTransitionAlpha(View view, float f) {
        Float f2 = (Float) view.getTag(C0096R.C0095id.save_non_transition_alpha);
        if (f2 != null) {
            view.setAlpha(f2.floatValue() * f);
        } else {
            view.setAlpha(f);
        }
    }

    public float getTransitionAlpha(View view) {
        Float f = (Float) view.getTag(C0096R.C0095id.save_non_transition_alpha);
        if (f != null) {
            return view.getAlpha() / f.floatValue();
        }
        return view.getAlpha();
    }

    public void saveNonTransitionAlpha(View view) {
        if (view.getTag(C0096R.C0095id.save_non_transition_alpha) == null) {
            view.setTag(C0096R.C0095id.save_non_transition_alpha, Float.valueOf(view.getAlpha()));
        }
    }

    public void clearNonTransitionAlpha(View view) {
        if (view.getVisibility() == 0) {
            view.setTag(C0096R.C0095id.save_non_transition_alpha, null);
        }
    }

    public void transformMatrixToGlobal(View view, Matrix matrix) {
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            View view2 = (View) parent;
            transformMatrixToGlobal(view2, matrix);
            matrix.preTranslate((float) (-view2.getScrollX()), (float) (-view2.getScrollY()));
        }
        matrix.preTranslate((float) view.getLeft(), (float) view.getTop());
        Matrix matrix2 = view.getMatrix();
        if (!matrix2.isIdentity()) {
            matrix.preConcat(matrix2);
        }
    }

    public void transformMatrixToLocal(View view, Matrix matrix) {
        ViewParent parent = view.getParent();
        if (parent instanceof View) {
            View view2 = (View) parent;
            transformMatrixToLocal(view2, matrix);
            matrix.postTranslate((float) view2.getScrollX(), (float) view2.getScrollY());
        }
        matrix.postTranslate((float) view.getLeft(), (float) view.getTop());
        Matrix matrix2 = view.getMatrix();
        if (!matrix2.isIdentity()) {
            Matrix matrix3 = new Matrix();
            if (matrix2.invert(matrix3)) {
                matrix.postConcat(matrix3);
            }
        }
    }

    public void setLeftTopRightBottom(View view, int i, int i2, int i3, int i4) {
        view.setLeft(i);
        view.setTop(i2);
        view.setRight(i3);
        view.setBottom(i4);
    }
}
