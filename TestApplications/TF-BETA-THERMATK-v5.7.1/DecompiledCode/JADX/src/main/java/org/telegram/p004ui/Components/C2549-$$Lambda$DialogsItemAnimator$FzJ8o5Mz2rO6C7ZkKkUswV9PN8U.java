package org.telegram.p004ui.Components;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$DialogsItemAnimator$FzJ8o5Mz2rO6C7ZkKkUswV9PN8U */
public final /* synthetic */ class C2549-$$Lambda$DialogsItemAnimator$FzJ8o5Mz2rO6C7ZkKkUswV9PN8U implements Runnable {
    private final /* synthetic */ DialogsItemAnimator f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C2549-$$Lambda$DialogsItemAnimator$FzJ8o5Mz2rO6C7ZkKkUswV9PN8U(DialogsItemAnimator dialogsItemAnimator, ArrayList arrayList) {
        this.f$0 = dialogsItemAnimator;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$runPendingAnimations$0$DialogsItemAnimator(this.f$1);
    }
}
