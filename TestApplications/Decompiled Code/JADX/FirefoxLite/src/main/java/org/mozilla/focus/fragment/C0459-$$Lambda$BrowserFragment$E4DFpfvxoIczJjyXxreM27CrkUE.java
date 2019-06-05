package org.mozilla.focus.fragment;

import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.WindowInsets;
import android.widget.RelativeLayout.LayoutParams;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE */
public final /* synthetic */ class C0459-$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE implements OnApplyWindowInsetsListener {
    public static final /* synthetic */ C0459-$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE INSTANCE = new C0459-$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE();

    private /* synthetic */ C0459-$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE() {
    }

    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        return ((LayoutParams) view.getLayoutParams()).topMargin = windowInsets.getSystemWindowInsetTop();
    }
}
