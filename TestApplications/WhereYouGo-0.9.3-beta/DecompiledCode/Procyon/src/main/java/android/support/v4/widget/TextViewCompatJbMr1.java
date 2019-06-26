// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.widget;

import android.support.annotation.Nullable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(17)
@RequiresApi(17)
class TextViewCompatJbMr1
{
    public static Drawable[] getCompoundDrawablesRelative(@NonNull final TextView textView) {
        int n = 1;
        if (textView.getLayoutDirection() != 1) {
            n = 0;
        }
        final Drawable[] compoundDrawables = textView.getCompoundDrawables();
        if (n != 0) {
            final Drawable drawable = compoundDrawables[2];
            final Drawable drawable2 = compoundDrawables[0];
            compoundDrawables[0] = drawable;
            compoundDrawables[2] = drawable2;
        }
        return compoundDrawables;
    }
    
    public static void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        boolean b = true;
        if (textView.getLayoutDirection() != 1) {
            b = false;
        }
        Drawable drawable5;
        if (b) {
            drawable5 = drawable3;
        }
        else {
            drawable5 = drawable;
        }
        if (!b) {
            drawable = drawable3;
        }
        textView.setCompoundDrawables(drawable5, drawable2, drawable, drawable4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, int n, final int n2, final int n3, final int n4) {
        boolean b = true;
        if (textView.getLayoutDirection() != 1) {
            b = false;
        }
        int n5;
        if (b) {
            n5 = n3;
        }
        else {
            n5 = n;
        }
        if (!b) {
            n = n3;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        boolean b = true;
        if (textView.getLayoutDirection() != 1) {
            b = false;
        }
        Drawable drawable5;
        if (b) {
            drawable5 = drawable3;
        }
        else {
            drawable5 = drawable;
        }
        if (!b) {
            drawable = drawable3;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable5, drawable2, drawable, drawable4);
    }
}
