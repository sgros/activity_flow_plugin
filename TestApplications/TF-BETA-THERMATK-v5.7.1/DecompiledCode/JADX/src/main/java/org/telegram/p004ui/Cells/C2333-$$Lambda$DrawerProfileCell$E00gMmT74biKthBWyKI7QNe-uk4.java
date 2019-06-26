package org.telegram.p004ui.Cells;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Cells.-$$Lambda$DrawerProfileCell$E00gMmT74biKthBWyKI7QNe-uk4 */
public final /* synthetic */ class C2333-$$Lambda$DrawerProfileCell$E00gMmT74biKthBWyKI7QNe-uk4 implements OnClickListener {
    private final /* synthetic */ DrawerProfileCell f$0;
    private final /* synthetic */ OnClickListener f$1;

    public /* synthetic */ C2333-$$Lambda$DrawerProfileCell$E00gMmT74biKthBWyKI7QNe-uk4(DrawerProfileCell drawerProfileCell, OnClickListener onClickListener) {
        this.f$0 = drawerProfileCell;
        this.f$1 = onClickListener;
    }

    public final void onClick(View view) {
        this.f$0.lambda$setOnArrowClickListener$0$DrawerProfileCell(this.f$1, view);
    }
}
