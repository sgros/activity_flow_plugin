package org.telegram.p004ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.messenger.MediaController;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AudioPlayerAlert$_25s4XwmMXC5vKIS0zNvR6jdxfg */
public final /* synthetic */ class C2509-$$Lambda$AudioPlayerAlert$_25s4XwmMXC5vKIS0zNvR6jdxfg implements OnClickListener {
    public static final /* synthetic */ C2509-$$Lambda$AudioPlayerAlert$_25s4XwmMXC5vKIS0zNvR6jdxfg INSTANCE = new C2509-$$Lambda$AudioPlayerAlert$_25s4XwmMXC5vKIS0zNvR6jdxfg();

    private /* synthetic */ C2509-$$Lambda$AudioPlayerAlert$_25s4XwmMXC5vKIS0zNvR6jdxfg() {
    }

    public final void onClick(View view) {
        MediaController.getInstance().playPreviousMessage();
    }
}
