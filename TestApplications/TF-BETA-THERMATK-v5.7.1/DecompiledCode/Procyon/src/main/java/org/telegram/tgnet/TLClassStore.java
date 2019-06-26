// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.tgnet;

import org.telegram.messenger.FileLog;
import android.util.SparseArray;

public class TLClassStore
{
    static TLClassStore store;
    private SparseArray<Class> classStore;
    
    public TLClassStore() {
        (this.classStore = (SparseArray<Class>)new SparseArray()).put(TLRPC.TL_error.constructor, (Object)TLRPC.TL_error.class);
        this.classStore.put(TLRPC.TL_decryptedMessageService.constructor, (Object)TLRPC.TL_decryptedMessageService.class);
        this.classStore.put(TLRPC.TL_decryptedMessage.constructor, (Object)TLRPC.TL_decryptedMessage.class);
        this.classStore.put(TLRPC.TL_config.constructor, (Object)TLRPC.TL_config.class);
        this.classStore.put(TLRPC.TL_decryptedMessageLayer.constructor, (Object)TLRPC.TL_decryptedMessageLayer.class);
        this.classStore.put(TLRPC.TL_decryptedMessage_layer17.constructor, (Object)TLRPC.TL_decryptedMessage.class);
        this.classStore.put(TLRPC.TL_decryptedMessage_layer45.constructor, (Object)TLRPC.TL_decryptedMessage_layer45.class);
        this.classStore.put(TLRPC.TL_decryptedMessageService_layer8.constructor, (Object)TLRPC.TL_decryptedMessageService_layer8.class);
        this.classStore.put(TLRPC.TL_decryptedMessage_layer8.constructor, (Object)TLRPC.TL_decryptedMessage_layer8.class);
        this.classStore.put(TLRPC.TL_message_secret.constructor, (Object)TLRPC.TL_message_secret.class);
        this.classStore.put(TLRPC.TL_message_secret_layer72.constructor, (Object)TLRPC.TL_message_secret_layer72.class);
        this.classStore.put(TLRPC.TL_message_secret_old.constructor, (Object)TLRPC.TL_message_secret_old.class);
        this.classStore.put(TLRPC.TL_messageEncryptedAction.constructor, (Object)TLRPC.TL_messageEncryptedAction.class);
        this.classStore.put(TLRPC.TL_null.constructor, (Object)TLRPC.TL_null.class);
        this.classStore.put(TLRPC.TL_updateShortChatMessage.constructor, (Object)TLRPC.TL_updateShortChatMessage.class);
        this.classStore.put(TLRPC.TL_updates.constructor, (Object)TLRPC.TL_updates.class);
        this.classStore.put(TLRPC.TL_updateShortMessage.constructor, (Object)TLRPC.TL_updateShortMessage.class);
        this.classStore.put(TLRPC.TL_updateShort.constructor, (Object)TLRPC.TL_updateShort.class);
        this.classStore.put(TLRPC.TL_updatesCombined.constructor, (Object)TLRPC.TL_updatesCombined.class);
        this.classStore.put(TLRPC.TL_updateShortSentMessage.constructor, (Object)TLRPC.TL_updateShortSentMessage.class);
        this.classStore.put(TLRPC.TL_updatesTooLong.constructor, (Object)TLRPC.TL_updatesTooLong.class);
    }
    
    public static TLClassStore Instance() {
        if (TLClassStore.store == null) {
            TLClassStore.store = new TLClassStore();
        }
        return TLClassStore.store;
    }
    
    public TLObject TLdeserialize(final NativeByteBuffer nativeByteBuffer, final int n, final boolean b) {
        final Class clazz = (Class)this.classStore.get(n);
        if (clazz != null) {
            try {
                final TLObject tlObject = clazz.newInstance();
                tlObject.readParams(nativeByteBuffer, b);
                return tlObject;
            }
            catch (Throwable t) {
                FileLog.e(t);
            }
        }
        return null;
    }
}
