// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.home.pinsite;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.ColorUtils;
import kotlin.jvm.internal.Intrinsics;
import android.view.ViewGroup;

public final class PinViewWrapper
{
    public static final Companion Companion;
    private final ViewGroup view;
    
    static {
        Companion = new Companion(null);
    }
    
    public PinViewWrapper(final ViewGroup view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        this.view = view;
    }
    
    public final void setPinColor(final int n) {
        final ViewGroup view = this.view;
        final Drawable background = this.view.getBackground();
        final Drawable drawable = null;
        Drawable tint;
        if (background != null) {
            tint = this.tint(background, n);
        }
        else {
            tint = null;
        }
        view.setBackground(tint);
        int n2 = -1;
        if (ColorUtils.calculateContrast(n, -1) < 1.5) {
            n2 = -16777216;
        }
        final View viewById = this.view.findViewById(2131296566);
        Intrinsics.checkExpressionValueIsNotNull(viewById, "iconView");
        final Drawable background2 = viewById.getBackground();
        Drawable tint2 = drawable;
        if (background2 != null) {
            tint2 = this.tint(background2, n2);
        }
        viewById.setBackground(tint2);
    }
    
    public final void setVisibility(final int visibility) {
        this.view.setVisibility(visibility);
    }
    
    public final Drawable tint(Drawable mutate, final int n) {
        Intrinsics.checkParameterIsNotNull(mutate, "receiver$0");
        mutate = DrawableCompat.wrap(mutate).mutate();
        DrawableCompat.setTint(mutate, n);
        Intrinsics.checkExpressionValueIsNotNull(mutate, "DrawableCompat.wrap(this\u2026nt(this, color)\n        }");
        return mutate;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
    }
}
