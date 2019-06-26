package org.telegram.p004ui;

import android.animation.AnimatorSet;
import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.p004ui.Cells.MaxFileSizeCell;
import org.telegram.p004ui.Cells.TextCheckBoxCell;
import org.telegram.p004ui.Cells.TextCheckCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DataAutoDownloadActivity$GvlKaEmbPmrPjkZgpelfWkoLNuU */
public final /* synthetic */ class C1483-$$Lambda$DataAutoDownloadActivity$GvlKaEmbPmrPjkZgpelfWkoLNuU implements OnClickListener {
    private final /* synthetic */ DataAutoDownloadActivity f$0;
    private final /* synthetic */ TextCheckBoxCell f$1;
    private final /* synthetic */ TextCheckBoxCell[] f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ MaxFileSizeCell[] f$4;
    private final /* synthetic */ TextCheckCell[] f$5;
    private final /* synthetic */ AnimatorSet[] f$6;

    public /* synthetic */ C1483-$$Lambda$DataAutoDownloadActivity$GvlKaEmbPmrPjkZgpelfWkoLNuU(DataAutoDownloadActivity dataAutoDownloadActivity, TextCheckBoxCell textCheckBoxCell, TextCheckBoxCell[] textCheckBoxCellArr, int i, MaxFileSizeCell[] maxFileSizeCellArr, TextCheckCell[] textCheckCellArr, AnimatorSet[] animatorSetArr) {
        this.f$0 = dataAutoDownloadActivity;
        this.f$1 = textCheckBoxCell;
        this.f$2 = textCheckBoxCellArr;
        this.f$3 = i;
        this.f$4 = maxFileSizeCellArr;
        this.f$5 = textCheckCellArr;
        this.f$6 = animatorSetArr;
    }

    public final void onClick(View view) {
        this.f$0.lambda$null$0$DataAutoDownloadActivity(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, view);
    }
}
