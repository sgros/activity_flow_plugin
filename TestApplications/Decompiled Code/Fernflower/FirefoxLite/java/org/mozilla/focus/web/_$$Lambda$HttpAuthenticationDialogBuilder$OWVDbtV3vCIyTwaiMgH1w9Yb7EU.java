package org.mozilla.focus.web;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

// $FF: synthetic class
public final class _$$Lambda$HttpAuthenticationDialogBuilder$OWVDbtV3vCIyTwaiMgH1w9Yb7EU implements OnEditorActionListener {
   // $FF: synthetic field
   private final HttpAuthenticationDialogBuilder f$0;

   // $FF: synthetic method
   public _$$Lambda$HttpAuthenticationDialogBuilder$OWVDbtV3vCIyTwaiMgH1w9Yb7EU(HttpAuthenticationDialogBuilder var1) {
      this.f$0 = var1;
   }

   public final boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
      return HttpAuthenticationDialogBuilder.lambda$createDialog$0(this.f$0, var1, var2, var3);
   }
}
