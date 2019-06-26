package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.ActionBar.Theme.ThemeInfo;
import org.telegram.p004ui.ThemeActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU */
public final /* synthetic */ class C2087-$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU implements OnClickListener {
    private final /* synthetic */ ListAdapter f$0;
    private final /* synthetic */ ThemeInfo f$1;

    public /* synthetic */ C2087-$$Lambda$ThemeActivity$ListAdapter$mOT1foTAY8nRoymoRurolXzJymU(ListAdapter listAdapter, ThemeInfo themeInfo) {
        this.f$0 = listAdapter;
        this.f$1 = themeInfo;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showOptionsForTheme$1$ThemeActivity$ListAdapter(this.f$1, dialogInterface, i);
    }
}
