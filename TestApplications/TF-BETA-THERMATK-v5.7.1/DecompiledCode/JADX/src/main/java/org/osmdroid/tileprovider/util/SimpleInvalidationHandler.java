package org.osmdroid.tileprovider.util;

import android.os.Handler;
import android.os.Message;
import android.view.View;

public class SimpleInvalidationHandler extends Handler {
    private View mView;

    public SimpleInvalidationHandler(View view) {
        this.mView = view;
    }

    public void handleMessage(Message message) {
        if (message.what == 0) {
            View view = this.mView;
            if (view != null) {
                view.invalidate();
            }
        }
    }

    public void destroy() {
        this.mView = null;
    }
}
