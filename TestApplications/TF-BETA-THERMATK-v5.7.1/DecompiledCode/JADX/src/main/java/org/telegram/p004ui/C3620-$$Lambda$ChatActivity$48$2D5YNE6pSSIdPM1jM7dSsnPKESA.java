package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.AudioSelectActivity.AudioSelectActivityDelegate;
import org.telegram.p004ui.ChatActivity.C397348;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$48$2D5YNE6pSSIdPM1jM7dSsnPKESA */
public final /* synthetic */ class C3620-$$Lambda$ChatActivity$48$2D5YNE6pSSIdPM1jM7dSsnPKESA implements AudioSelectActivityDelegate {
    private final /* synthetic */ C397348 f$0;
    private final /* synthetic */ BaseFragment f$1;

    public /* synthetic */ C3620-$$Lambda$ChatActivity$48$2D5YNE6pSSIdPM1jM7dSsnPKESA(C397348 c397348, BaseFragment baseFragment) {
        this.f$0 = c397348;
        this.f$1 = baseFragment;
    }

    public final void didSelectAudio(ArrayList arrayList) {
        this.f$0.lambda$startMusicSelectActivity$0$ChatActivity$48(this.f$1, arrayList);
    }
}
