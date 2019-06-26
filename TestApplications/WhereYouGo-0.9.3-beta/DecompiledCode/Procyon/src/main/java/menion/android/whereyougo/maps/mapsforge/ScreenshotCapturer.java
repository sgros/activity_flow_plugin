// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.maps.mapsforge;

import java.io.IOException;
import android.os.Environment;
import java.util.Locale;
import java.io.File;
import android.graphics.Bitmap$CompressFormat;
import org.mapsforge.android.maps.PausableThread;

class ScreenshotCapturer extends PausableThread
{
    private static final String SCREENSHOT_FILE_NAME = "Map screenshot";
    private static final int SCREENSHOT_QUALITY = 90;
    private static final String THREAD_NAME = "ScreenshotCapturer";
    private final MapsforgeActivity advancedMapViewer;
    private Bitmap$CompressFormat compressFormat;
    
    ScreenshotCapturer(final MapsforgeActivity advancedMapViewer) {
        this.advancedMapViewer = advancedMapViewer;
    }
    
    private File assembleFilePath(final File parent) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Map screenshot");
        sb.append('.');
        sb.append(this.compressFormat.name().toLowerCase(Locale.ENGLISH));
        return new File(parent, sb.toString());
    }
    
    void captureScreenshot(final Bitmap$CompressFormat compressFormat) {
        synchronized (this) {
            this.compressFormat = compressFormat;
            this.notify();
        }
    }
    
    @Override
    protected void doWork() {
        // monitorenter(this)
    Label_0072:
        while (true) {
            try {
                while (true) {
                    Label_0085: {
                        try {
                            final File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            if (!externalStoragePublicDirectory.exists() && !externalStoragePublicDirectory.mkdirs()) {
                                this.advancedMapViewer.showToastOnUiThread("Could not create screenshot directory");
                            }
                            else {
                                final File assembleFilePath = this.assembleFilePath(externalStoragePublicDirectory);
                                if (!this.advancedMapViewer.mapView.takeScreenshot(this.compressFormat, 90, assembleFilePath)) {
                                    break Label_0085;
                                }
                                this.advancedMapViewer.showToastOnUiThread(assembleFilePath.getAbsolutePath());
                                this.compressFormat = null;
                            }
                            return;
                        }
                        finally {
                        }
                        // monitorexit(this)
                    }
                    this.advancedMapViewer.showToastOnUiThread("Screenshot could not be saved");
                    continue Label_0072;
                }
            }
            catch (IOException ex) {
                this.advancedMapViewer.showToastOnUiThread(ex.getMessage());
                continue Label_0072;
            }
            continue Label_0072;
        }
    }
    
    @Override
    protected String getThreadName() {
        return "ScreenshotCapturer";
    }
    
    @Override
    protected ThreadPriority getThreadPriority() {
        return ThreadPriority.BELOW_NORMAL;
    }
    
    @Override
    protected boolean hasWork() {
        synchronized (this) {
            return this.compressFormat != null;
        }
    }
}
