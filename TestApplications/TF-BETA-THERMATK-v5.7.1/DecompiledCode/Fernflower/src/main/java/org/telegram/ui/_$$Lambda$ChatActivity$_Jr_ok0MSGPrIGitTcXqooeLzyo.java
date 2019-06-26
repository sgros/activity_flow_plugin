package org.telegram.ui;

import org.telegram.messenger.MessageObject;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$_Jr_ok0MSGPrIGitTcXqooeLzyo implements Runnable {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final BaseFragment f$1;
   // $FF: synthetic field
   private final MessageObject f$2;
   // $FF: synthetic field
   private final ActionBarLayout f$3;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$_Jr_ok0MSGPrIGitTcXqooeLzyo(ChatActivity var1, BaseFragment var2, MessageObject var3, ActionBarLayout var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$migrateToNewChat$57$ChatActivity(this.f$1, this.f$2, this.f$3);
   }
}
