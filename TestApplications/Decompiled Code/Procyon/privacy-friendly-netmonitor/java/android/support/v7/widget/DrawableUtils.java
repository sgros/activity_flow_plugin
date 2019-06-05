// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import android.graphics.PorterDuff$Mode;
import java.lang.reflect.Field;
import android.util.Log;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.drawable.ScaleDrawable;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class DrawableUtils
{
    public static final Rect INSETS_NONE;
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;
    
    static {
        INSETS_NONE = new Rect();
        if (Build$VERSION.SDK_INT < 18) {
            return;
        }
        try {
            DrawableUtils.sInsetsClazz = Class.forName("android.graphics.Insets");
        }
        catch (ClassNotFoundException ex) {}
    }
    
    private DrawableUtils() {
    }
    
    public static boolean canSafelyMutateDrawable(@NonNull final Drawable drawable) {
        if (Build$VERSION.SDK_INT < 15 && drawable instanceof InsetDrawable) {
            return false;
        }
        if (Build$VERSION.SDK_INT < 15 && drawable instanceof GradientDrawable) {
            return false;
        }
        if (Build$VERSION.SDK_INT < 17 && drawable instanceof LayerDrawable) {
            return false;
        }
        if (drawable instanceof DrawableContainer) {
            final Drawable$ConstantState constantState = drawable.getConstantState();
            if (constantState instanceof DrawableContainer$DrawableContainerState) {
                final Drawable[] children = ((DrawableContainer$DrawableContainerState)constantState).getChildren();
                for (int length = children.length, i = 0; i < length; ++i) {
                    if (!canSafelyMutateDrawable(children[i])) {
                        return false;
                    }
                }
            }
        }
        else {
            if (drawable instanceof DrawableWrapper) {
                return canSafelyMutateDrawable(((DrawableWrapper)drawable).getWrappedDrawable());
            }
            if (drawable instanceof android.support.v7.graphics.drawable.DrawableWrapper) {
                return canSafelyMutateDrawable(((android.support.v7.graphics.drawable.DrawableWrapper)drawable).getWrappedDrawable());
            }
            if (drawable instanceof ScaleDrawable) {
                return canSafelyMutateDrawable(((ScaleDrawable)drawable).getDrawable());
            }
        }
        return true;
    }
    
    static void fixDrawable(@NonNull final Drawable drawable) {
        if (Build$VERSION.SDK_INT == 21 && "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName())) {
            fixVectorDrawableTinting(drawable);
        }
    }
    
    private static void fixVectorDrawableTinting(final Drawable drawable) {
        final int[] state = drawable.getState();
        if (state != null && state.length != 0) {
            drawable.setState(ThemeUtils.EMPTY_STATE_SET);
        }
        else {
            drawable.setState(ThemeUtils.CHECKED_STATE_SET);
        }
        drawable.setState(state);
    }
    
    public static Rect getOpticalBounds(Drawable unwrap) {
        if (DrawableUtils.sInsetsClazz != null) {
            try {
                unwrap = DrawableCompat.unwrap(unwrap);
                final Object invoke = unwrap.getClass().getMethod("getOpticalInsets", (Class<?>[])new Class[0]).invoke(unwrap, new Object[0]);
                if (invoke != null) {
                    final Rect rect = new Rect();
                    for (final Field field : DrawableUtils.sInsetsClazz.getFields()) {
                        final String name = field.getName();
                        final int hashCode = name.hashCode();
                        int n = 0;
                        Label_0182: {
                            if (hashCode != -1383228885) {
                                if (hashCode != 115029) {
                                    if (hashCode != 3317767) {
                                        if (hashCode == 108511772) {
                                            if (name.equals("right")) {
                                                n = 2;
                                                break Label_0182;
                                            }
                                        }
                                    }
                                    else if (name.equals("left")) {
                                        n = 0;
                                        break Label_0182;
                                    }
                                }
                                else if (name.equals("top")) {
                                    n = 1;
                                    break Label_0182;
                                }
                            }
                            else if (name.equals("bottom")) {
                                n = 3;
                                break Label_0182;
                            }
                            n = -1;
                        }
                        switch (n) {
                            case 3: {
                                rect.bottom = field.getInt(invoke);
                                break;
                            }
                            case 2: {
                                rect.right = field.getInt(invoke);
                                break;
                            }
                            case 1: {
                                rect.top = field.getInt(invoke);
                                break;
                            }
                            case 0: {
                                rect.left = field.getInt(invoke);
                                break;
                            }
                        }
                    }
                    return rect;
                }
            }
            catch (Exception ex) {
                Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
            }
        }
        return DrawableUtils.INSETS_NONE;
    }
    
    public static PorterDuff$Mode parseTintMode(final int n, PorterDuff$Mode value) {
        if (n == 3) {
            return PorterDuff$Mode.SRC_OVER;
        }
        if (n == 5) {
            return PorterDuff$Mode.SRC_IN;
        }
        if (n == 9) {
            return PorterDuff$Mode.SRC_ATOP;
        }
        switch (n) {
            default: {
                return value;
            }
            case 16: {
                if (Build$VERSION.SDK_INT >= 11) {
                    value = PorterDuff$Mode.valueOf("ADD");
                }
                return value;
            }
            case 15: {
                return PorterDuff$Mode.SCREEN;
            }
            case 14: {
                return PorterDuff$Mode.MULTIPLY;
            }
        }
    }
}
