package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

class SendMessagesHelper$3 implements Runnable {
   // $FF: synthetic field
   final SendMessagesHelper this$0;
   // $FF: synthetic field
   final SendMessagesHelper.DelayedMessage val$delayedMessage;
   // $FF: synthetic field
   final ArrayList val$msgObjs;
   // $FF: synthetic field
   final TLRPC.TL_messages_sendMultiMedia val$req;

   SendMessagesHelper$3(SendMessagesHelper var1, TLRPC.TL_messages_sendMultiMedia var2, SendMessagesHelper.DelayedMessage var3, ArrayList var4) {
      this.this$0 = var1;
      this.val$req = var2;
      this.val$delayedMessage = var3;
      this.val$msgObjs = var4;
   }

   public void run() {
      int var1 = this.val$req.multi_media.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         if (this.val$delayedMessage.parentObjects.get(var2) != null) {
            this.this$0.removeFromSendingMessages(((MessageObject)this.val$msgObjs.get(var2)).getId());
            TLRPC.TL_inputSingleMedia var3 = (TLRPC.TL_inputSingleMedia)this.val$req.multi_media.get(var2);
            TLRPC.InputMedia var4 = var3.media;
            if (var4 instanceof TLRPC.TL_inputMediaPhoto) {
               var3.media = (TLRPC.InputMedia)this.val$delayedMessage.inputMedias.get(var2);
            } else if (var4 instanceof TLRPC.TL_inputMediaDocument) {
               var3.media = (TLRPC.InputMedia)this.val$delayedMessage.inputMedias.get(var2);
            }

            SendMessagesHelper.DelayedMessage var5 = this.val$delayedMessage;
            var5.videoEditedInfo = (VideoEditedInfo)var5.videoEditedInfos.get(var2);
            var5 = this.val$delayedMessage;
            var5.httpLocation = (String)var5.httpLocations.get(var2);
            var5 = this.val$delayedMessage;
            var5.photoSize = (TLRPC.PhotoSize)var5.locations.get(var2);
            var5 = this.val$delayedMessage;
            var5.performMediaUpload = true;
            SendMessagesHelper.access$1200(this.this$0, var5, var2);
         }
      }

   }
}
