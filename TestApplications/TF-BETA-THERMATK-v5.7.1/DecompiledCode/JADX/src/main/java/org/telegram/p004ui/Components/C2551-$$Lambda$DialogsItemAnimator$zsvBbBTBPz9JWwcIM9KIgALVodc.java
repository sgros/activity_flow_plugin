package org.telegram.p004ui.Components;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$DialogsItemAnimator$zsvBbBTBPz9JWwcIM9KIgALVodc */
public final /* synthetic */ class C2551-$$Lambda$DialogsItemAnimator$zsvBbBTBPz9JWwcIM9KIgALVodc implements Runnable {
    private final /* synthetic */ DialogsItemAnimator f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C2551-$$Lambda$DialogsItemAnimator$zsvBbBTBPz9JWwcIM9KIgALVodc(DialogsItemAnimator dialogsItemAnimator, ArrayList arrayList) {
        this.f$0 = dialogsItemAnimator;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$runPendingAnimations$2$DialogsItemAnimator(this.f$1);
    }
}
