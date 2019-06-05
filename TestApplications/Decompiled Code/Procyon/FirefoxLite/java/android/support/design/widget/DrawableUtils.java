// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.util.Log;
import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.drawable.DrawableContainer;
import java.lang.reflect.Method;

public class DrawableUtils
{
    private static Method setConstantStateMethod;
    private static boolean setConstantStateMethodFetched;
    
    public static boolean setContainerConstantState(final DrawableContainer drawableContainer, final Drawable$ConstantState drawable$ConstantState) {
        return setContainerConstantStateV9(drawableContainer, drawable$ConstantState);
    }
    
    private static boolean setContainerConstantStateV9(final DrawableContainer obj, final Drawable$ConstantState drawable$ConstantState) {
        if (!DrawableUtils.setConstantStateMethodFetched) {
            try {
                (DrawableUtils.setConstantStateMethod = DrawableContainer.class.getDeclaredMethod("setConstantState", DrawableContainer$DrawableContainerState.class)).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {
                Log.e("DrawableUtils", "Could not fetch setConstantState(). Oh well.");
            }
            DrawableUtils.setConstantStateMethodFetched = true;
        }
        if (DrawableUtils.setConstantStateMethod != null) {
            try {
                DrawableUtils.setConstantStateMethod.invoke(obj, drawable$ConstantState);
                return true;
            }
            catch (Exception ex2) {
                Log.e("DrawableUtils", "Could not invoke setConstantState(). Oh well.");
            }
        }
        return false;
    }
}
