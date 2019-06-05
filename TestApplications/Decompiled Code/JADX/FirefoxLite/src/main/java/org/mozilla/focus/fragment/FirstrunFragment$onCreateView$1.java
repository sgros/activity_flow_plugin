package org.mozilla.focus.fragment;

import android.support.p001v4.view.ViewPager.PageTransformer;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FirstrunFragment.kt */
final class FirstrunFragment$onCreateView$1 implements PageTransformer {
    public static final FirstrunFragment$onCreateView$1 INSTANCE = new FirstrunFragment$onCreateView$1();

    FirstrunFragment$onCreateView$1() {
    }

    public final void transformPage(View view, float f) {
        Intrinsics.checkParameterIsNotNull(view, "page");
        view.setAlpha(((float) 1) - (Math.abs(f) * 0.5f));
    }
}
