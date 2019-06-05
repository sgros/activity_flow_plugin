// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.download;

import kotlin.jvm.internal.Intrinsics;
import android.view.ViewGroup;
import android.view.View;
import android.support.v4.app.Fragment;

public final class DownloadIndicatorIntroViewHelper
{
    public static final DownloadIndicatorIntroViewHelper INSTANCE;
    
    static {
        INSTANCE = new DownloadIndicatorIntroViewHelper();
    }
    
    private DownloadIndicatorIntroViewHelper() {
    }
    
    public final void initDownloadIndicatorIntroView(final Fragment fragment, final View view, final ViewGroup viewGroup, final OnViewInflated onViewInflated) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(onViewInflated, "listener");
        if (view != null) {
            view.postDelayed((Runnable)new DownloadIndicatorIntroViewHelper$initDownloadIndicatorIntroView.DownloadIndicatorIntroViewHelper$initDownloadIndicatorIntroView$1(fragment, viewGroup, view, onViewInflated), 3500L);
        }
    }
    
    public interface OnViewInflated
    {
        void onInflated(final View p0);
    }
}
