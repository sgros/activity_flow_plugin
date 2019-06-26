package org.telegram.p004ui.Components;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.messenger.MediaController;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AudioPlayerAlert$4X94PKs6A-0UhkXhWsjRM6iEc3I */
public final /* synthetic */ class C2503-$$Lambda$AudioPlayerAlert$4X94PKs6A-0UhkXhWsjRM6iEc3I implements OnClickListener {
    public static final /* synthetic */ C2503-$$Lambda$AudioPlayerAlert$4X94PKs6A-0UhkXhWsjRM6iEc3I INSTANCE = new C2503-$$Lambda$AudioPlayerAlert$4X94PKs6A-0UhkXhWsjRM6iEc3I();

    private /* synthetic */ C2503-$$Lambda$AudioPlayerAlert$4X94PKs6A-0UhkXhWsjRM6iEc3I() {
    }

    public final void onClick(View view) {
        MediaController.getInstance().playNextMessage();
    }
}
