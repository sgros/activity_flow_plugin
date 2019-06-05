package org.mozilla.focus.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.activity.-$$Lambda$MainActivity$8YTaJwSpEv5fKJyTfPnaPvg8EA8 */
public final /* synthetic */ class C0429-$$Lambda$MainActivity$8YTaJwSpEv5fKJyTfPnaPvg8EA8 implements OnClickListener {
    private final /* synthetic */ MainActivity f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C0429-$$Lambda$MainActivity$8YTaJwSpEv5fKJyTfPnaPvg8EA8(MainActivity mainActivity, String str) {
        this.f$0 = mainActivity;
        this.f$1 = str;
    }

    public final void onClick(View view) {
        this.f$0.startActivity(new Intent(this.f$0, EditBookmarkActivity.class).putExtra("ITEM_UUID_KEY", this.f$1));
    }
}
