// 
// Decompiled by Procyon v0.5.34
// 

package com.google.zxing.client.android.camera.open;

import android.hardware.Camera$CameraInfo;
import android.util.Log;
import android.hardware.Camera;

public final class OpenCameraInterface
{
    public static final int NO_REQUESTED_CAMERA = -1;
    private static final String TAG;
    
    static {
        TAG = OpenCameraInterface.class.getName();
    }
    
    private OpenCameraInterface() {
    }
    
    public static int getCameraId(int i) {
        final int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 0) {
            Log.w(OpenCameraInterface.TAG, "No cameras!");
            i = -1;
        }
        else {
            boolean b;
            if (i >= 0) {
                b = true;
            }
            else {
                b = false;
            }
            int n = i;
            if (!b) {
                Camera$CameraInfo camera$CameraInfo;
                for (i = 0; i < numberOfCameras; ++i) {
                    camera$CameraInfo = new Camera$CameraInfo();
                    Camera.getCameraInfo(i, camera$CameraInfo);
                    if (camera$CameraInfo.facing == 0) {
                        break;
                    }
                }
                n = i;
            }
            if ((i = n) >= numberOfCameras) {
                if (b) {
                    i = -1;
                }
                else {
                    i = 0;
                }
            }
        }
        return i;
    }
    
    public static Camera open(int cameraId) {
        cameraId = getCameraId(cameraId);
        Camera open;
        if (cameraId == -1) {
            open = null;
        }
        else {
            open = Camera.open(cameraId);
        }
        return open;
    }
}
