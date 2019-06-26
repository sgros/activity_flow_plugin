package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.messenger.LocaleController.LocaleInfo;
import org.telegram.p004ui.Cells.LanguageCell;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$xW-6R4h9aa4jorowKHJF_yJiIQM */
public final /* synthetic */ class C1593-$$Lambda$LaunchActivity$xW-6R4h9aa4jorowKHJF_yJiIQM implements OnClickListener {
    private final /* synthetic */ LocaleInfo[] f$0;
    private final /* synthetic */ LanguageCell[] f$1;

    public /* synthetic */ C1593-$$Lambda$LaunchActivity$xW-6R4h9aa4jorowKHJF_yJiIQM(LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr) {
        this.f$0 = localeInfoArr;
        this.f$1 = languageCellArr;
    }

    public final void onClick(View view) {
        LaunchActivity.lambda$showLanguageAlertInternal$49(this.f$0, this.f$1, view);
    }
}
