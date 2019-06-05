package org.mozilla.focus.fragment;

import android.arch.lifecycle.Observer;
import android.widget.Toast;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.fragment.-$$Lambda$DownloadsFragment$wa1yltsD9O1p0ux6NYh0WV_9aAU */
public final /* synthetic */ class C0692-$$Lambda$DownloadsFragment$wa1yltsD9O1p0ux6NYh0WV_9aAU implements Observer {
    private final /* synthetic */ DownloadsFragment f$0;

    public /* synthetic */ C0692-$$Lambda$DownloadsFragment$wa1yltsD9O1p0ux6NYh0WV_9aAU(DownloadsFragment downloadsFragment) {
        this.f$0 = downloadsFragment;
    }

    public final void onChanged(Object obj) {
        Toast.makeText(this.f$0.getActivity(), this.f$0.getString(((Integer) obj).intValue()), 0).show();
    }
}
