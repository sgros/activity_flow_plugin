package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$00s_Q5Ms7AV0v3XsAsD6561decA implements GroupCreateActivity.ContactsAddActivityDelegate {
   // $FF: synthetic field
   private final ProfileActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$00s_Q5Ms7AV0v3XsAsD6561decA(ProfileActivity var1) {
      this.f$0 = var1;
   }

   public final void didSelectUsers(ArrayList var1, int var2) {
      this.f$0.lambda$openAddMember$20$ProfileActivity(var1, var2);
   }

   // $FF: synthetic method
   public void needAddBot(TLRPC.User var1) {
      GroupCreateActivity$ContactsAddActivityDelegate$_CC.$default$needAddBot(this, var1);
   }
}
