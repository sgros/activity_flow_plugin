package org.mozilla.rocket.download;

import android.support.p001v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DownloadIndicatorIntroViewHelper.kt */
public final class DownloadIndicatorIntroViewHelper {
    public static final DownloadIndicatorIntroViewHelper INSTANCE = new DownloadIndicatorIntroViewHelper();

    /* compiled from: DownloadIndicatorIntroViewHelper.kt */
    public interface OnViewInflated {
        void onInflated(View view);
    }

    private DownloadIndicatorIntroViewHelper() {
    }

    public final void initDownloadIndicatorIntroView(Fragment fragment, View view, ViewGroup viewGroup, OnViewInflated onViewInflated) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(onViewInflated, "listener");
        if (view != null) {
            view.postDelayed(new C0595x1a8f49f3(fragment, viewGroup, view, onViewInflated), 3500);
        }
    }
}
