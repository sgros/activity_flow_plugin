package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.Cells.TextCheckCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DataAutoDownloadActivity$m0gzCqbXGWHGQEKzinvKQh1h1B0 */
public final /* synthetic */ class C1485-$$Lambda$DataAutoDownloadActivity$m0gzCqbXGWHGQEKzinvKQh1h1B0 implements OnClickListener {
    private final /* synthetic */ TextCheckCell[] f$0;

    public /* synthetic */ C1485-$$Lambda$DataAutoDownloadActivity$m0gzCqbXGWHGQEKzinvKQh1h1B0(TextCheckCell[] textCheckCellArr) {
        this.f$0 = textCheckCellArr;
    }

    public final void onClick(View view) {
        this.f$0[0].setChecked(this.f$0[0].isChecked() ^ 1);
    }
}
