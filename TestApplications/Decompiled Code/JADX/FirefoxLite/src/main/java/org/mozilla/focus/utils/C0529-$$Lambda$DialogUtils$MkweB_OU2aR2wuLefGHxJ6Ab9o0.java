package org.mozilla.focus.utils;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnLongClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0 */
public final /* synthetic */ class C0529-$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0 implements OnLongClickListener {
    private final /* synthetic */ Dialog f$0;
    private final /* synthetic */ View f$1;

    public /* synthetic */ C0529-$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0(Dialog dialog, View view) {
        this.f$0 = dialog;
        this.f$1 = view;
    }

    public final boolean onLongClick(View view) {
        return this.f$0.dismiss();
    }
}
