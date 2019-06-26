// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.support.annotation.NonNull;
import android.util.Log;
import android.graphics.Matrix;
import android.view.View;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class ViewUtilsApi21 extends ViewUtilsApi19
{
    private static final String TAG = "ViewUtilsApi21";
    private static Method sSetAnimationMatrixMethod;
    private static boolean sSetAnimationMatrixMethodFetched;
    private static Method sTransformMatrixToGlobalMethod;
    private static boolean sTransformMatrixToGlobalMethodFetched;
    private static Method sTransformMatrixToLocalMethod;
    private static boolean sTransformMatrixToLocalMethodFetched;
    
    private void fetchSetAnimationMatrix() {
        if (!ViewUtilsApi21.sSetAnimationMatrixMethodFetched) {
            try {
                (ViewUtilsApi21.sSetAnimationMatrixMethod = View.class.getDeclaredMethod("setAnimationMatrix", Matrix.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi21", "Failed to retrieve setAnimationMatrix method", (Throwable)ex);
            }
            ViewUtilsApi21.sSetAnimationMatrixMethodFetched = true;
        }
    }
    
    private void fetchTransformMatrixToGlobalMethod() {
        if (!ViewUtilsApi21.sTransformMatrixToGlobalMethodFetched) {
            try {
                (ViewUtilsApi21.sTransformMatrixToGlobalMethod = View.class.getDeclaredMethod("transformMatrixToGlobal", Matrix.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToGlobal method", (Throwable)ex);
            }
            ViewUtilsApi21.sTransformMatrixToGlobalMethodFetched = true;
        }
    }
    
    private void fetchTransformMatrixToLocalMethod() {
        if (!ViewUtilsApi21.sTransformMatrixToLocalMethodFetched) {
            try {
                (ViewUtilsApi21.sTransformMatrixToLocalMethod = View.class.getDeclaredMethod("transformMatrixToLocal", Matrix.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToLocal method", (Throwable)ex);
            }
            ViewUtilsApi21.sTransformMatrixToLocalMethodFetched = true;
        }
    }
    
    @Override
    public void setAnimationMatrix(@NonNull final View obj, final Matrix matrix) {
        this.fetchSetAnimationMatrix();
        if (ViewUtilsApi21.sSetAnimationMatrixMethod == null) {
            goto Label_0042;
        }
        try {
            ViewUtilsApi21.sSetAnimationMatrixMethod.invoke(obj, matrix);
            goto Label_0042;
        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException(ex.getCause());
        }
        catch (InvocationTargetException ex2) {
            goto Label_0042;
        }
    }
    
    @Override
    public void transformMatrixToGlobal(@NonNull final View obj, @NonNull final Matrix matrix) {
        this.fetchTransformMatrixToGlobalMethod();
        if (ViewUtilsApi21.sTransformMatrixToGlobalMethod == null) {
            goto Label_0042;
        }
        try {
            ViewUtilsApi21.sTransformMatrixToGlobalMethod.invoke(obj, matrix);
            goto Label_0042;
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        }
        catch (IllegalAccessException ex2) {
            goto Label_0042;
        }
    }
    
    @Override
    public void transformMatrixToLocal(@NonNull final View obj, @NonNull final Matrix matrix) {
        this.fetchTransformMatrixToLocalMethod();
        if (ViewUtilsApi21.sTransformMatrixToLocalMethod == null) {
            goto Label_0042;
        }
        try {
            ViewUtilsApi21.sTransformMatrixToLocalMethod.invoke(obj, matrix);
            goto Label_0042;
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        }
        catch (IllegalAccessException ex2) {
            goto Label_0042;
        }
    }
}
