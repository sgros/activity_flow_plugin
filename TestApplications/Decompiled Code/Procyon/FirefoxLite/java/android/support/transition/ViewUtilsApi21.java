// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.graphics.Matrix;
import android.view.View;
import java.lang.reflect.Method;

class ViewUtilsApi21 extends ViewUtilsApi19
{
    private static Method sTransformMatrixToGlobalMethod;
    private static boolean sTransformMatrixToGlobalMethodFetched;
    private static Method sTransformMatrixToLocalMethod;
    private static boolean sTransformMatrixToLocalMethodFetched;
    
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
    public void transformMatrixToGlobal(final View obj, final Matrix matrix) {
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
    public void transformMatrixToLocal(final View obj, final Matrix matrix) {
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
