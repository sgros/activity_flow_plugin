package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

class ChatUsersActivity$3 implements GroupCreateActivity.ContactsAddActivityDelegate {
   // $FF: synthetic field
   final ChatUsersActivity this$0;

   ChatUsersActivity$3(ChatUsersActivity var1) {
      this.this$0 = var1;
   }

   public void didSelectUsers(ArrayList var1, int var2) {
      int var3 = var1.size();

      for(int var4 = 0; var4 < var3; ++var4) {
         TLRPC.User var5 = (TLRPC.User)var1.get(var4);
         MessagesController.getInstance(ChatUsersActivity.access$6200(this.this$0)).addUserToChat(ChatUsersActivity.access$1100(this.this$0), var5, (TLRPC.ChatFull)null, var2, (String)null, this.this$0, (Runnable)null);
      }

   }

   public void needAddBot(TLRPC.User var1) {
      ChatUsersActivity.access$6300(this.this$0, var1.id, (TLObject)null, (TLRPC.TL_chatAdminRights)null, (TLRPC.TL_chatBannedRights)null, true, 0, false);
   }
}
