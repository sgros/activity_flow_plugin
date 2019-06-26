package org.telegram.p004ui.Components;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.p004ui.Components.ShareAlert.SearchField;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$ShareAlert$W5k8hWsyZg9DfQTgiUPSh2_mvaQ */
public final /* synthetic */ class C4055-$$Lambda$ShareAlert$W5k8hWsyZg9DfQTgiUPSh2_mvaQ implements OnItemClickListener {
    private final /* synthetic */ ShareAlert f$0;
    private final /* synthetic */ SearchField f$1;

    public /* synthetic */ C4055-$$Lambda$ShareAlert$W5k8hWsyZg9DfQTgiUPSh2_mvaQ(ShareAlert shareAlert, SearchField searchField) {
        this.f$0 = shareAlert;
        this.f$1 = searchField;
    }

    public final void onItemClick(View view, int i) {
        this.f$0.lambda$new$2$ShareAlert(this.f$1, view, i);
    }
}
