package org.mozilla.rocket.home.pinsite;

import android.graphics.drawable.Drawable;
import android.support.p001v4.graphics.ColorUtils;
import android.support.p001v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;

/* compiled from: PinViewWrapper.kt */
public final class PinViewWrapper {
    public static final Companion Companion = new Companion();
    private final ViewGroup view;

    /* compiled from: PinViewWrapper.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public PinViewWrapper(ViewGroup viewGroup) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "view");
        this.view = viewGroup;
    }

    public final void setVisibility(int i) {
        this.view.setVisibility(i);
    }

    public final void setPinColor(int i) {
        ViewGroup viewGroup = this.view;
        Drawable background = this.view.getBackground();
        Drawable drawable = null;
        viewGroup.setBackground(background != null ? tint(background, i) : null);
        int i2 = -1;
        if (ColorUtils.calculateContrast(i, -1) < 1.5d) {
            i2 = -16777216;
        }
        View findViewById = this.view.findViewById(C0427R.C0426id.pin_icon);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "iconView");
        background = findViewById.getBackground();
        if (background != null) {
            drawable = tint(background, i2);
        }
        findViewById.setBackground(drawable);
    }

    public final Drawable tint(Drawable drawable, int i) {
        Intrinsics.checkParameterIsNotNull(drawable, "receiver$0");
        drawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(drawable, i);
        Intrinsics.checkExpressionValueIsNotNull(drawable, "DrawableCompat.wrap(this…nt(this, color)\n        }");
        return drawable;
    }
}
