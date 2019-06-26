package org.telegram.ui;

class ChatLinkActivity$3 implements GroupCreateFinalActivity.GroupCreateFinalActivityDelegate {
   // $FF: synthetic field
   final ChatLinkActivity this$0;

   ChatLinkActivity$3(ChatLinkActivity var1) {
      this.this$0 = var1;
   }

   public void didFailChatCreation() {
   }

   public void didFinishChatCreation(GroupCreateFinalActivity var1, int var2) {
      ChatLinkActivity var3 = this.this$0;
      ChatLinkActivity.access$2400(var3, ChatLinkActivity.access$2300(var3).getChat(var2), var1);
   }

   public void didStartChatCreation() {
   }
}
