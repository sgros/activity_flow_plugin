// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import java.lang.reflect.InvocationTargetException;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(22)
class ViewUtilsApi22 extends ViewUtilsApi21
{
    private static final String TAG = "ViewUtilsApi22";
    private static Method sSetLeftTopRightBottomMethod;
    private static boolean sSetLeftTopRightBottomMethodFetched;
    
    @SuppressLint({ "PrivateApi" })
    private void fetchSetLeftTopRightBottomMethod() {
        if (!ViewUtilsApi22.sSetLeftTopRightBottomMethodFetched) {
            try {
                (ViewUtilsApi22.sSetLeftTopRightBottomMethod = View.class.getDeclaredMethod("setLeftTopRightBottom", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ViewUtilsApi22", "Failed to retrieve setLeftTopRightBottom method", (Throwable)ex);
            }
            ViewUtilsApi22.sSetLeftTopRightBottomMethodFetched = true;
        }
    }
    
    @Override
    public void setLeftTopRightBottom(final View obj, final int i, final int j, final int k, final int l) {
        this.fetchSetLeftTopRightBottomMethod();
        if (ViewUtilsApi22.sSetLeftTopRightBottomMethod == null) {
            goto Label_0068;
        }
        try {
            ViewUtilsApi22.sSetLeftTopRightBottomMethod.invoke(obj, i, j, k, l);
            goto Label_0068;
        }
        catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        }
        catch (IllegalAccessException ex2) {
            goto Label_0068;
        }
    }
}
