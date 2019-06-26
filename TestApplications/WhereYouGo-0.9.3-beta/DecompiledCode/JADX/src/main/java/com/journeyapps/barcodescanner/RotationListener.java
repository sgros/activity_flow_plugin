package com.journeyapps.barcodescanner;

import android.content.Context;
import android.view.OrientationEventListener;
import android.view.WindowManager;

public class RotationListener {
    private RotationCallback callback;
    private int lastRotation;
    private OrientationEventListener orientationEventListener;
    private WindowManager windowManager;

    public void listen(Context context, RotationCallback callback) {
        stop();
        context = context.getApplicationContext();
        this.callback = callback;
        this.windowManager = (WindowManager) context.getSystemService("window");
        this.orientationEventListener = new OrientationEventListener(context, 3) {
            public void onOrientationChanged(int orientation) {
                WindowManager localWindowManager = RotationListener.this.windowManager;
                RotationCallback localCallback = RotationListener.this.callback;
                if (RotationListener.this.windowManager != null && localCallback != null) {
                    int newRotation = localWindowManager.getDefaultDisplay().getRotation();
                    if (newRotation != RotationListener.this.lastRotation) {
                        RotationListener.this.lastRotation = newRotation;
                        localCallback.onRotationChanged(newRotation);
                    }
                }
            }
        };
        this.orientationEventListener.enable();
        this.lastRotation = this.windowManager.getDefaultDisplay().getRotation();
    }

    public void stop() {
        if (this.orientationEventListener != null) {
            this.orientationEventListener.disable();
        }
        this.orientationEventListener = null;
        this.windowManager = null;
        this.callback = null;
    }
}
