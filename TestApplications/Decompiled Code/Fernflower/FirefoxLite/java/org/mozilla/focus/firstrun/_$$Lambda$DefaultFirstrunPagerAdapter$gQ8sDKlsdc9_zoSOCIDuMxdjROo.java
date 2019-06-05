package org.mozilla.focus.firstrun;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import org.mozilla.focus.utils.Settings;

// $FF: synthetic class
public final class _$$Lambda$DefaultFirstrunPagerAdapter$gQ8sDKlsdc9_zoSOCIDuMxdjROo implements OnCheckedChangeListener {
   // $FF: synthetic field
   private final Settings f$0;

   // $FF: synthetic method
   public _$$Lambda$DefaultFirstrunPagerAdapter$gQ8sDKlsdc9_zoSOCIDuMxdjROo(Settings var1) {
      this.f$0 = var1;
   }

   public final void onCheckedChanged(CompoundButton var1, boolean var2) {
      DefaultFirstrunPagerAdapter.lambda$initForTurboModePage$0(this.f$0, var1, var2);
   }
}
