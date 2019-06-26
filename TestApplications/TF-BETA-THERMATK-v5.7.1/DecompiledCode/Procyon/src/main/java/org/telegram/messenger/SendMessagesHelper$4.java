// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

class SendMessagesHelper$4 implements Runnable
{
    final /* synthetic */ SendMessagesHelper this$0;
    final /* synthetic */ SendMessagesHelper.DelayedMessage val$delayedMessage;
    final /* synthetic */ TLRPC.Message val$newMsgObj;
    final /* synthetic */ TLObject val$req;
    
    SendMessagesHelper$4(final SendMessagesHelper this$0, final TLRPC.Message val$newMsgObj, final TLObject val$req, final SendMessagesHelper.DelayedMessage val$delayedMessage) {
        this.this$0 = this$0;
        this.val$newMsgObj = val$newMsgObj;
        this.val$req = val$req;
        this.val$delayedMessage = val$delayedMessage;
    }
    
    @Override
    public void run() {
        this.this$0.removeFromSendingMessages(this.val$newMsgObj.id);
        final TLObject val$req = this.val$req;
        if (val$req instanceof TLRPC.TL_messages_sendMedia) {
            final TLRPC.TL_messages_sendMedia tl_messages_sendMedia = (TLRPC.TL_messages_sendMedia)val$req;
            final TLRPC.InputMedia media = tl_messages_sendMedia.media;
            if (media instanceof TLRPC.TL_inputMediaPhoto) {
                tl_messages_sendMedia.media = this.val$delayedMessage.inputUploadMedia;
            }
            else if (media instanceof TLRPC.TL_inputMediaDocument) {
                tl_messages_sendMedia.media = this.val$delayedMessage.inputUploadMedia;
            }
        }
        else if (val$req instanceof TLRPC.TL_messages_editMessage) {
            final TLRPC.TL_messages_editMessage tl_messages_editMessage = (TLRPC.TL_messages_editMessage)val$req;
            final TLRPC.InputMedia media2 = tl_messages_editMessage.media;
            if (media2 instanceof TLRPC.TL_inputMediaPhoto) {
                tl_messages_editMessage.media = this.val$delayedMessage.inputUploadMedia;
            }
            else if (media2 instanceof TLRPC.TL_inputMediaDocument) {
                tl_messages_editMessage.media = this.val$delayedMessage.inputUploadMedia;
            }
        }
        final SendMessagesHelper.DelayedMessage val$delayedMessage = this.val$delayedMessage;
        val$delayedMessage.performMediaUpload = true;
        this.this$0.performSendDelayedMessage(val$delayedMessage);
    }
}
