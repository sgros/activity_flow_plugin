package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$ChatLinkActivity$KB_csNS4ZyyV341Vg7wZcvTIREM implements MessagesStorage.IntCallback {
   // $FF: synthetic field
   private final ChatLinkActivity f$0;
   // $FF: synthetic field
   private final BaseFragment f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatLinkActivity$KB_csNS4ZyyV341Vg7wZcvTIREM(ChatLinkActivity var1, BaseFragment var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(int var1) {
      this.f$0.lambda$linkChat$9$ChatLinkActivity(this.f$1, var1);
   }
}
