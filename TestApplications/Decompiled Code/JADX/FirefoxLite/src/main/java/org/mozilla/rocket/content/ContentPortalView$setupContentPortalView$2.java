package org.mozilla.rocket.content;

import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ContentPortalView.kt */
public final class ContentPortalView$setupContentPortalView$2 extends BottomSheetCallback {
    final /* synthetic */ ContentPortalView this$0;

    public void onSlide(View view, float f) {
        Intrinsics.checkParameterIsNotNull(view, "bottomSheet");
    }

    ContentPortalView$setupContentPortalView$2(ContentPortalView contentPortalView) {
        this.this$0 = contentPortalView;
    }

    public void onStateChanged(View view, int i) {
        Intrinsics.checkParameterIsNotNull(view, "bottomSheet");
        if (i == 5) {
            this.this$0.hide();
        }
    }
}
