package org.mozilla.focus.activity;

import android.arch.lifecycle.Observer;
import java.util.List;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.activity.-$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o */
public final /* synthetic */ class C0681-$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o implements Observer {
    private final /* synthetic */ MainActivity f$0;

    public /* synthetic */ C0681-$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o(MainActivity mainActivity) {
        this.f$0 = mainActivity;
    }

    public final void onChanged(Object obj) {
        MainActivity.lambda$updateMenu$5(this.f$0, (List) obj);
    }
}
