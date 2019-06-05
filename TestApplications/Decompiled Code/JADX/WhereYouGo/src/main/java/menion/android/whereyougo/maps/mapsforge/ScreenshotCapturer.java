package menion.android.whereyougo.maps.mapsforge;

import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.mapsforge.android.maps.PausableThread;

class ScreenshotCapturer extends PausableThread {
    private static final String SCREENSHOT_FILE_NAME = "Map screenshot";
    private static final int SCREENSHOT_QUALITY = 90;
    private static final String THREAD_NAME = "ScreenshotCapturer";
    private final MapsforgeActivity advancedMapViewer;
    private CompressFormat compressFormat;

    ScreenshotCapturer(MapsforgeActivity advancedMapViewer) {
        this.advancedMapViewer = advancedMapViewer;
    }

    private File assembleFilePath(File directory) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SCREENSHOT_FILE_NAME);
        stringBuilder.append('.');
        stringBuilder.append(this.compressFormat.name().toLowerCase(Locale.ENGLISH));
        return new File(directory, stringBuilder.toString());
    }

    /* Access modifiers changed, original: declared_synchronized */
    public synchronized void captureScreenshot(CompressFormat screenhotFormat) {
        this.compressFormat = screenhotFormat;
        notify();
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void doWork() {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (directory.exists() || directory.mkdirs()) {
                File outputFile = assembleFilePath(directory);
                if (this.advancedMapViewer.mapView.takeScreenshot(this.compressFormat, 90, outputFile)) {
                    this.advancedMapViewer.showToastOnUiThread(outputFile.getAbsolutePath());
                } else {
                    this.advancedMapViewer.showToastOnUiThread("Screenshot could not be saved");
                }
                this.compressFormat = null;
            } else {
                this.advancedMapViewer.showToastOnUiThread("Could not create screenshot directory");
            }
        } catch (IOException e) {
            this.advancedMapViewer.showToastOnUiThread(e.getMessage());
        }
        return;
    }

    /* Access modifiers changed, original: protected */
    public String getThreadName() {
        return THREAD_NAME;
    }

    /* Access modifiers changed, original: protected */
    public ThreadPriority getThreadPriority() {
        return ThreadPriority.BELOW_NORMAL;
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized boolean hasWork() {
        return this.compressFormat != null;
    }
}
