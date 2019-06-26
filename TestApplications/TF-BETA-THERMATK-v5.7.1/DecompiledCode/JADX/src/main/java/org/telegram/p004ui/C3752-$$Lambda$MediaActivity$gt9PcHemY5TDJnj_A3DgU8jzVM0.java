package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.p004ui.MediaActivity.MediaPage;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$MediaActivity$gt9PcHemY5TDJnj_A3DgU8jzVM0 */
public final /* synthetic */ class C3752-$$Lambda$MediaActivity$gt9PcHemY5TDJnj_A3DgU8jzVM0 implements OnItemClickListener {
    private final /* synthetic */ MediaActivity f$0;
    private final /* synthetic */ MediaPage f$1;

    public /* synthetic */ C3752-$$Lambda$MediaActivity$gt9PcHemY5TDJnj_A3DgU8jzVM0(MediaActivity mediaActivity, MediaPage mediaPage) {
        this.f$0 = mediaActivity;
        this.f$1 = mediaPage;
    }

    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$2$MediaActivity(this.f$1, view, i);
    }
}
