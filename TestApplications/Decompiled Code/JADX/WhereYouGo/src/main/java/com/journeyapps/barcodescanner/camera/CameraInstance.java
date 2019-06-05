package com.journeyapps.barcodescanner.camera;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import com.google.zxing.client.android.C0186R;
import com.journeyapps.barcodescanner.Size;
import com.journeyapps.barcodescanner.Util;

public class CameraInstance {
    private static final String TAG = CameraInstance.class.getSimpleName();
    private boolean cameraClosed = true;
    private CameraManager cameraManager;
    private CameraSettings cameraSettings = new CameraSettings();
    private CameraThread cameraThread;
    private Runnable closer = new C02296();
    private Runnable configure = new C02274();
    private DisplayConfiguration displayConfiguration;
    private boolean open = false;
    private Runnable opener = new C02263();
    private Runnable previewStarter = new C02285();
    private Handler readyHandler;
    private CameraSurface surface;

    /* renamed from: com.journeyapps.barcodescanner.camera.CameraInstance$3 */
    class C02263 implements Runnable {
        C02263() {
        }

        public void run() {
            try {
                Log.d(CameraInstance.TAG, "Opening camera");
                CameraInstance.this.cameraManager.open();
            } catch (Exception e) {
                CameraInstance.this.notifyError(e);
                Log.e(CameraInstance.TAG, "Failed to open camera", e);
            }
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.camera.CameraInstance$4 */
    class C02274 implements Runnable {
        C02274() {
        }

        public void run() {
            try {
                Log.d(CameraInstance.TAG, "Configuring camera");
                CameraInstance.this.cameraManager.configure();
                if (CameraInstance.this.readyHandler != null) {
                    CameraInstance.this.readyHandler.obtainMessage(C0186R.C0185id.zxing_prewiew_size_ready, CameraInstance.this.getPreviewSize()).sendToTarget();
                }
            } catch (Exception e) {
                CameraInstance.this.notifyError(e);
                Log.e(CameraInstance.TAG, "Failed to configure camera", e);
            }
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.camera.CameraInstance$5 */
    class C02285 implements Runnable {
        C02285() {
        }

        public void run() {
            try {
                Log.d(CameraInstance.TAG, "Starting preview");
                CameraInstance.this.cameraManager.setPreviewDisplay(CameraInstance.this.surface);
                CameraInstance.this.cameraManager.startPreview();
            } catch (Exception e) {
                CameraInstance.this.notifyError(e);
                Log.e(CameraInstance.TAG, "Failed to start preview", e);
            }
        }
    }

    /* renamed from: com.journeyapps.barcodescanner.camera.CameraInstance$6 */
    class C02296 implements Runnable {
        C02296() {
        }

        public void run() {
            try {
                Log.d(CameraInstance.TAG, "Closing camera");
                CameraInstance.this.cameraManager.stopPreview();
                CameraInstance.this.cameraManager.close();
            } catch (Exception e) {
                Log.e(CameraInstance.TAG, "Failed to close camera", e);
            }
            CameraInstance.this.cameraClosed = true;
            CameraInstance.this.readyHandler.sendEmptyMessage(C0186R.C0185id.zxing_camera_closed);
            CameraInstance.this.cameraThread.decrementInstances();
        }
    }

    public CameraInstance(Context context) {
        Util.validateMainThread();
        this.cameraThread = CameraThread.getInstance();
        this.cameraManager = new CameraManager(context);
        this.cameraManager.setCameraSettings(this.cameraSettings);
    }

    public CameraInstance(CameraManager cameraManager) {
        Util.validateMainThread();
        this.cameraManager = cameraManager;
    }

    public void setDisplayConfiguration(DisplayConfiguration configuration) {
        this.displayConfiguration = configuration;
        this.cameraManager.setDisplayConfiguration(configuration);
    }

    public DisplayConfiguration getDisplayConfiguration() {
        return this.displayConfiguration;
    }

    public void setReadyHandler(Handler readyHandler) {
        this.readyHandler = readyHandler;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        setSurface(new CameraSurface(surfaceHolder));
    }

    public void setSurface(CameraSurface surface) {
        this.surface = surface;
    }

    public CameraSettings getCameraSettings() {
        return this.cameraSettings;
    }

    public void setCameraSettings(CameraSettings cameraSettings) {
        if (!this.open) {
            this.cameraSettings = cameraSettings;
            this.cameraManager.setCameraSettings(cameraSettings);
        }
    }

    private Size getPreviewSize() {
        return this.cameraManager.getPreviewSize();
    }

    public int getCameraRotation() {
        return this.cameraManager.getCameraRotation();
    }

    public void open() {
        Util.validateMainThread();
        this.open = true;
        this.cameraClosed = false;
        this.cameraThread.incrementAndEnqueue(this.opener);
    }

    public void configureCamera() {
        Util.validateMainThread();
        validateOpen();
        this.cameraThread.enqueue(this.configure);
    }

    public void startPreview() {
        Util.validateMainThread();
        validateOpen();
        this.cameraThread.enqueue(this.previewStarter);
    }

    public void setTorch(final boolean on) {
        Util.validateMainThread();
        if (this.open) {
            this.cameraThread.enqueue(new Runnable() {
                public void run() {
                    CameraInstance.this.cameraManager.setTorch(on);
                }
            });
        }
    }

    public void close() {
        Util.validateMainThread();
        if (this.open) {
            this.cameraThread.enqueue(this.closer);
        } else {
            this.cameraClosed = true;
        }
        this.open = false;
    }

    public boolean isOpen() {
        return this.open;
    }

    public boolean isCameraClosed() {
        return this.cameraClosed;
    }

    public void requestPreview(final PreviewCallback callback) {
        validateOpen();
        this.cameraThread.enqueue(new Runnable() {
            public void run() {
                CameraInstance.this.cameraManager.requestPreviewFrame(callback);
            }
        });
    }

    private void validateOpen() {
        if (!this.open) {
            throw new IllegalStateException("CameraInstance is not open");
        }
    }

    private void notifyError(Exception error) {
        if (this.readyHandler != null) {
            this.readyHandler.obtainMessage(C0186R.C0185id.zxing_camera_error, error).sendToTarget();
        }
    }

    /* Access modifiers changed, original: protected */
    public CameraManager getCameraManager() {
        return this.cameraManager;
    }

    /* Access modifiers changed, original: protected */
    public CameraThread getCameraThread() {
        return this.cameraThread;
    }

    /* Access modifiers changed, original: protected */
    public CameraSurface getSurface() {
        return this.surface;
    }
}
