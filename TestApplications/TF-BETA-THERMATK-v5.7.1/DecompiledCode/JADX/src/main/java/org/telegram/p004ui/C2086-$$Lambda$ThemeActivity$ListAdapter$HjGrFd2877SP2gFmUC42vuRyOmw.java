package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.ActionBar.Theme.ThemeInfo;
import org.telegram.p004ui.ThemeActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ThemeActivity$ListAdapter$HjGrFd2877SP2gFmUC42vuRyOmw */
public final /* synthetic */ class C2086-$$Lambda$ThemeActivity$ListAdapter$HjGrFd2877SP2gFmUC42vuRyOmw implements OnClickListener {
    private final /* synthetic */ ListAdapter f$0;
    private final /* synthetic */ ThemeInfo f$1;

    public /* synthetic */ C2086-$$Lambda$ThemeActivity$ListAdapter$HjGrFd2877SP2gFmUC42vuRyOmw(ListAdapter listAdapter, ThemeInfo themeInfo) {
        this.f$0 = listAdapter;
        this.f$1 = themeInfo;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$0$ThemeActivity$ListAdapter(this.f$1, dialogInterface, i);
    }
}
