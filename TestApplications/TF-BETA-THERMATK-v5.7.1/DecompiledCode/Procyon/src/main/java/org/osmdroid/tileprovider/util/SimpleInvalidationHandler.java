// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.util;

import android.os.Message;
import android.view.View;
import android.os.Handler;

public class SimpleInvalidationHandler extends Handler
{
    private View mView;
    
    public SimpleInvalidationHandler(final View mView) {
        this.mView = mView;
    }
    
    public void destroy() {
        this.mView = null;
    }
    
    public void handleMessage(final Message message) {
        if (message.what == 0) {
            final View mView = this.mView;
            if (mView != null) {
                mView.invalidate();
            }
        }
    }
}
