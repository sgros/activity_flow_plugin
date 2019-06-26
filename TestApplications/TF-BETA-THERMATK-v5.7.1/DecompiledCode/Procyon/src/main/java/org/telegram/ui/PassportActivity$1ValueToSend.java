// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

class PassportActivity$1ValueToSend
{
    boolean selfie_required;
    final /* synthetic */ PassportActivity this$0;
    boolean translation_required;
    TLRPC.TL_secureValue value;
    
    public PassportActivity$1ValueToSend(final PassportActivity this$0, final TLRPC.TL_secureValue value, final boolean selfie_required, final boolean translation_required) {
        this.this$0 = this$0;
        this.value = value;
        this.selfie_required = selfie_required;
        this.translation_required = translation_required;
    }
}
