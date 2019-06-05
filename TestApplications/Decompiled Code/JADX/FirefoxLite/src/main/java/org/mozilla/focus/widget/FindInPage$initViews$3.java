package org.mozilla.focus.widget;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: FindInPage.kt */
final class FindInPage$initViews$3 implements OnClickListener {
    final /* synthetic */ FindInPage this$0;

    FindInPage$initViews$3(FindInPage findInPage) {
        this.this$0 = findInPage;
    }

    public final void onClick(View view) {
        this.this$0.queryText.setCursorVisible(true);
    }
}
