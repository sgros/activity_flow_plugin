package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$u_E5fg1gn1kuY6fPnql7A5bYvUE implements DialogsActivity.DialogsActivityDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_contacts_resolvedPeer f$3;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$u_E5fg1gn1kuY6fPnql7A5bYvUE(LaunchActivity var1, String var2, int var3, TLRPC.TL_contacts_resolvedPeer var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      this.f$0.lambda$null$9$LaunchActivity(this.f$1, this.f$2, this.f$3, var1, var2, var3, var4);
   }
}
