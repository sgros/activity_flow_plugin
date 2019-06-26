package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.ActionBar.BottomSheet;
import org.telegram.p004ui.Components.NumberPicker;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$26GEXmzp746KJTav-ZQPy3lxX3s */
public final /* synthetic */ class C1844-$$Lambda$PhotoViewer$26GEXmzp746KJTav-ZQPy3lxX3s implements OnClickListener {
    private final /* synthetic */ PhotoViewer f$0;
    private final /* synthetic */ NumberPicker f$1;
    private final /* synthetic */ BottomSheet f$2;

    public /* synthetic */ C1844-$$Lambda$PhotoViewer$26GEXmzp746KJTav-ZQPy3lxX3s(PhotoViewer photoViewer, NumberPicker numberPicker, BottomSheet bottomSheet) {
        this.f$0 = photoViewer;
        this.f$1 = numberPicker;
        this.f$2 = bottomSheet;
    }

    public final void onClick(View view) {
        this.f$0.lambda$null$16$PhotoViewer(this.f$1, this.f$2, view);
    }
}
