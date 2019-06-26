package org.telegram.messenger;

import android.view.View;
import android.view.View.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$AndroidUtilities$N3FZjh44dp7TBj2YD5HgSuaQoW4 */
public final /* synthetic */ class C0281-$$Lambda$AndroidUtilities$N3FZjh44dp7TBj2YD5HgSuaQoW4 implements OnClickListener {
    private final /* synthetic */ String f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ String f$3;
    private final /* synthetic */ String f$4;
    private final /* synthetic */ Runnable f$5;

    public /* synthetic */ C0281-$$Lambda$AndroidUtilities$N3FZjh44dp7TBj2YD5HgSuaQoW4(String str, String str2, String str3, String str4, String str5, Runnable runnable) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = str3;
        this.f$3 = str4;
        this.f$4 = str5;
        this.f$5 = runnable;
    }

    public final void onClick(View view) {
        AndroidUtilities.lambda$showProxyAlert$2(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, view);
    }
}
