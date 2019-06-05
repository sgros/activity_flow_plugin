package org.mozilla.focus.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.WebView;

/* compiled from: FindInPage.kt */
public final class FindInPage$initViews$6 implements TextWatcher {
    final /* synthetic */ FindInPage$initViews$1 $obtainWebView$1;

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    FindInPage$initViews$6(FindInPage$initViews$1 findInPage$initViews$1) {
        this.$obtainWebView$1 = findInPage$initViews$1;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence != null) {
            WebView invoke = this.$obtainWebView$1.invoke();
            if (invoke != null) {
                invoke.findAllAsync(charSequence.toString());
            }
        }
    }
}
