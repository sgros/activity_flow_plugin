// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.ui.Components.EmbedBottomSheet;
import android.telephony.PhoneStateListener;

class MediaController$3 extends PhoneStateListener
{
    final /* synthetic */ MediaController this$0;
    
    MediaController$3(final MediaController this$0) {
        this.this$0 = this$0;
    }
    
    public void onCallStateChanged(final int n, final String s) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$MediaController$3$kfHEHMBmovTxgGbvrlQqhhaeP5A(this, n));
    }
}
