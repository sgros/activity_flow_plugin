package org.telegram.p004ui.Adapters;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$DialogsSearchAdapter$jlz_Jg93hVH6t3KHP9A-P2d1MzU */
public final /* synthetic */ class C2246-$$Lambda$DialogsSearchAdapter$jlz_Jg93hVH6t3KHP9A-P2d1MzU implements Runnable {
    private final /* synthetic */ DialogsSearchAdapter f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C2246-$$Lambda$DialogsSearchAdapter$jlz_Jg93hVH6t3KHP9A-P2d1MzU(DialogsSearchAdapter dialogsSearchAdapter, String str, int i) {
        this.f$0 = dialogsSearchAdapter;
        this.f$1 = str;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$searchDialogsInternal$8$DialogsSearchAdapter(this.f$1, this.f$2);
    }
}
