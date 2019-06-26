// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;

class SendMessagesHelper$3 implements Runnable
{
    final /* synthetic */ SendMessagesHelper this$0;
    final /* synthetic */ SendMessagesHelper.DelayedMessage val$delayedMessage;
    final /* synthetic */ ArrayList val$msgObjs;
    final /* synthetic */ TLRPC.TL_messages_sendMultiMedia val$req;
    
    SendMessagesHelper$3(final SendMessagesHelper this$0, final TLRPC.TL_messages_sendMultiMedia val$req, final SendMessagesHelper.DelayedMessage val$delayedMessage, final ArrayList val$msgObjs) {
        this.this$0 = this$0;
        this.val$req = val$req;
        this.val$delayedMessage = val$delayedMessage;
        this.val$msgObjs = val$msgObjs;
    }
    
    @Override
    public void run() {
        for (int size = this.val$req.multi_media.size(), i = 0; i < size; ++i) {
            if (this.val$delayedMessage.parentObjects.get(i) != null) {
                this.this$0.removeFromSendingMessages(((MessageObject)this.val$msgObjs.get(i)).getId());
                final TLRPC.TL_inputSingleMedia tl_inputSingleMedia = this.val$req.multi_media.get(i);
                final TLRPC.InputMedia media = tl_inputSingleMedia.media;
                if (media instanceof TLRPC.TL_inputMediaPhoto) {
                    tl_inputSingleMedia.media = this.val$delayedMessage.inputMedias.get(i);
                }
                else if (media instanceof TLRPC.TL_inputMediaDocument) {
                    tl_inputSingleMedia.media = this.val$delayedMessage.inputMedias.get(i);
                }
                final SendMessagesHelper.DelayedMessage val$delayedMessage = this.val$delayedMessage;
                val$delayedMessage.videoEditedInfo = val$delayedMessage.videoEditedInfos.get(i);
                final SendMessagesHelper.DelayedMessage val$delayedMessage2 = this.val$delayedMessage;
                val$delayedMessage2.httpLocation = val$delayedMessage2.httpLocations.get(i);
                final SendMessagesHelper.DelayedMessage val$delayedMessage3 = this.val$delayedMessage;
                val$delayedMessage3.photoSize = val$delayedMessage3.locations.get(i);
                final SendMessagesHelper.DelayedMessage val$delayedMessage4 = this.val$delayedMessage;
                val$delayedMessage4.performMediaUpload = true;
                this.this$0.performSendDelayedMessage(val$delayedMessage4, i);
            }
        }
    }
}
