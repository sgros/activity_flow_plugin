// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.animation.Animator;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import android.graphics.Matrix;
import android.widget.ImageView;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class ImageViewUtilsApi21 implements ImageViewUtilsImpl
{
    private static final String TAG = "ImageViewUtilsApi21";
    private static Method sAnimateTransformMethod;
    private static boolean sAnimateTransformMethodFetched;
    
    private void fetchAnimateTransformMethod() {
        if (!ImageViewUtilsApi21.sAnimateTransformMethodFetched) {
            try {
                (ImageViewUtilsApi21.sAnimateTransformMethod = ImageView.class.getDeclaredMethod("animateTransform", Matrix.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.i("ImageViewUtilsApi21", "Failed to retrieve animateTransform method", (Throwable)ex);
            }
            ImageViewUtilsApi21.sAnimateTransformMethodFetched = true;
        }
    }
    
    @Override
    public void animateTransform(final ImageView obj, final Matrix matrix) {
        this.fetchAnimateTransformMethod();
        if (ImageViewUtilsApi21.sAnimateTransformMethod == null) {
            goto Label_0042;
        }
        try {
            ImageViewUtilsApi21.sAnimateTransformMethod.invoke(obj, matrix);
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
    public void reserveEndAnimateTransform(final ImageView imageView, final Animator animator) {
    }
    
    @Override
    public void startAnimateTransform(final ImageView imageView) {
    }
}
