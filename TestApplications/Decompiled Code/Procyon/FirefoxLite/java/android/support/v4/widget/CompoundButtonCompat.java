// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.util.Log;
import android.os.Build$VERSION;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import java.lang.reflect.Field;

public final class CompoundButtonCompat
{
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;
    
    public static Drawable getButtonDrawable(final CompoundButton obj) {
        if (Build$VERSION.SDK_INT >= 23) {
            return obj.getButtonDrawable();
        }
        if (!CompoundButtonCompat.sButtonDrawableFieldFetched) {
            try {
                (CompoundButtonCompat.sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", (Throwable)ex);
            }
            CompoundButtonCompat.sButtonDrawableFieldFetched = true;
        }
        if (CompoundButtonCompat.sButtonDrawableField != null) {
            try {
                return (Drawable)CompoundButtonCompat.sButtonDrawableField.get(obj);
            }
            catch (IllegalAccessException ex2) {
                Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", (Throwable)ex2);
                CompoundButtonCompat.sButtonDrawableField = null;
            }
        }
        return null;
    }
    
    public static void setButtonTintList(final CompoundButton compoundButton, final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            compoundButton.setButtonTintList(list);
        }
        else if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintList(list);
        }
    }
    
    public static void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            compoundButton.setButtonTintMode(porterDuff$Mode);
        }
        else if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(porterDuff$Mode);
        }
    }
}
