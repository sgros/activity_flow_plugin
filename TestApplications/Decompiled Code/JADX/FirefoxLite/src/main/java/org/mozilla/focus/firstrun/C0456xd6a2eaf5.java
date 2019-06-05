package org.mozilla.focus.firstrun;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.mozilla.focus.utils.Settings;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.firstrun.-$$Lambda$DefaultFirstrunPagerAdapter$gQ8sDKlsdc9_zoSOCIDuMxdjROo */
public final /* synthetic */ class C0456xd6a2eaf5 implements OnCheckedChangeListener {
    private final /* synthetic */ Settings f$0;

    public /* synthetic */ C0456xd6a2eaf5(Settings settings) {
        this.f$0 = settings;
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        DefaultFirstrunPagerAdapter.lambda$initForTurboModePage$0(this.f$0, compoundButton, z);
    }
}
