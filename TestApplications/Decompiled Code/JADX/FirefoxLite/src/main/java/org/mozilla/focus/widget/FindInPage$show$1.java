package org.mozilla.focus.widget;

import org.mozilla.focus.utils.ViewUtils;

/* compiled from: FindInPage.kt */
final class FindInPage$show$1 implements Runnable {
    final /* synthetic */ FindInPage this$0;

    FindInPage$show$1(FindInPage findInPage) {
        this.this$0 = findInPage;
    }

    public final void run() {
        this.this$0.queryText.requestFocus();
        ViewUtils.showKeyboard(this.this$0.queryText);
    }
}
