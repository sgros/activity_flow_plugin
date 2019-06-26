// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.camera;

import java.util.ArrayList;
import android.hardware.Camera;

public class CameraInfo
{
    protected Camera camera;
    protected int cameraId;
    protected final int frontCamera;
    protected ArrayList<Size> pictureSizes;
    protected ArrayList<Size> previewSizes;
    
    public CameraInfo(final int cameraId, final int frontCamera) {
        this.pictureSizes = new ArrayList<Size>();
        this.previewSizes = new ArrayList<Size>();
        this.cameraId = cameraId;
        this.frontCamera = frontCamera;
    }
    
    private Camera getCamera() {
        return this.camera;
    }
    
    public int getCameraId() {
        return this.cameraId;
    }
    
    public ArrayList<Size> getPictureSizes() {
        return this.pictureSizes;
    }
    
    public ArrayList<Size> getPreviewSizes() {
        return this.previewSizes;
    }
    
    public boolean isFrontface() {
        return this.frontCamera != 0;
    }
}
