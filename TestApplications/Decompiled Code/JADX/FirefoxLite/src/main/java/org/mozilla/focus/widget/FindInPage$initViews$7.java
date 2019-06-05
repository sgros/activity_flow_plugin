package org.mozilla.focus.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.mozilla.focus.utils.ViewUtils;

/* compiled from: FindInPage.kt */
final class FindInPage$initViews$7 implements OnEditorActionListener {
    final /* synthetic */ FindInPage this$0;

    FindInPage$initViews$7(FindInPage findInPage) {
        this.this$0 = findInPage;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            ViewUtils.hideKeyboard(this.this$0.queryText);
            this.this$0.queryText.setCursorVisible(false);
        }
        return false;
    }
}
