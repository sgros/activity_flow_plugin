package org.mozilla.rocket.privately.browse;

import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BrowserFragment.kt */
final class BrowserFragment$onViewCreated$7 implements OnApplyWindowInsetsListener {
    public static final BrowserFragment$onViewCreated$7 INSTANCE = new BrowserFragment$onViewCreated$7();

    BrowserFragment$onViewCreated$7() {
    }

    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        Intrinsics.checkExpressionValueIsNotNull(view, "v");
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            Intrinsics.checkExpressionValueIsNotNull(windowInsets, "insets");
            layoutParams2.topMargin = windowInsets.getSystemWindowInsetTop();
            return windowInsets;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
    }
}
