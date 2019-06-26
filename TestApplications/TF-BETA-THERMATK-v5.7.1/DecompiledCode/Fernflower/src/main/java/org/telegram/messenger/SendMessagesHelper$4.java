package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

class SendMessagesHelper$4 implements Runnable {
   // $FF: synthetic field
   final SendMessagesHelper this$0;
   // $FF: synthetic field
   final SendMessagesHelper.DelayedMessage val$delayedMessage;
   // $FF: synthetic field
   final TLRPC.Message val$newMsgObj;
   // $FF: synthetic field
   final TLObject val$req;

   SendMessagesHelper$4(SendMessagesHelper var1, TLRPC.Message var2, TLObject var3, SendMessagesHelper.DelayedMessage var4) {
      this.this$0 = var1;
      this.val$newMsgObj = var2;
      this.val$req = var3;
      this.val$delayedMessage = var4;
   }

   public void run() {
      this.this$0.removeFromSendingMessages(this.val$newMsgObj.id);
      TLObject var1 = this.val$req;
      if (var1 instanceof TLRPC.TL_messages_sendMedia) {
         TLRPC.TL_messages_sendMedia var2 = (TLRPC.TL_messages_sendMedia)var1;
         TLRPC.InputMedia var3 = var2.media;
         if (var3 instanceof TLRPC.TL_inputMediaPhoto) {
            var2.media = this.val$delayedMessage.inputUploadMedia;
         } else if (var3 instanceof TLRPC.TL_inputMediaDocument) {
            var2.media = this.val$delayedMessage.inputUploadMedia;
         }
      } else if (var1 instanceof TLRPC.TL_messages_editMessage) {
         TLRPC.TL_messages_editMessage var4 = (TLRPC.TL_messages_editMessage)var1;
         TLRPC.InputMedia var6 = var4.media;
         if (var6 instanceof TLRPC.TL_inputMediaPhoto) {
            var4.media = this.val$delayedMessage.inputUploadMedia;
         } else if (var6 instanceof TLRPC.TL_inputMediaDocument) {
            var4.media = this.val$delayedMessage.inputUploadMedia;
         }
      }

      SendMessagesHelper.DelayedMessage var5 = this.val$delayedMessage;
      var5.performMediaUpload = true;
      SendMessagesHelper.access$1100(this.this$0, var5);
   }
}
