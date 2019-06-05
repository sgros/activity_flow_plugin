package org.mozilla.focus.widget;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.telemetry.TelemetryWrapper.FIND_IN_PAGE;

/* compiled from: FindInPage.kt */
final class FindInPage$initViews$4 implements OnClickListener {
    final /* synthetic */ FindInPage$initViews$1 $obtainWebView$1;

    FindInPage$initViews$4(FindInPage$initViews$1 findInPage$initViews$1) {
        this.$obtainWebView$1 = findInPage$initViews$1;
    }

    public final void onClick(View view) {
        WebView invoke = this.$obtainWebView$1.invoke();
        if (invoke != null) {
            invoke.findNext(false);
        }
        TelemetryWrapper.findInPage(FIND_IN_PAGE.CLICK_PREVIOUS);
    }
}
