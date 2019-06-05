package org.mozilla.focus.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.telemetry.TelemetryWrapper;

public class ScreenshotCaptureTask extends AsyncTask<Object, Void, String> {
    private final Context context;

    public ScreenshotCaptureTask(Context context) {
        this.context = context.getApplicationContext();
    }

    /* Access modifiers changed, original: protected|varargs */
    public String doInBackground(Object... objArr) {
        String str = (String) objArr[0];
        String str2 = (String) objArr[1];
        Bitmap bitmap = (Bitmap) objArr[2];
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        try {
            Context context = this.context;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Screenshot_");
            stringBuilder.append(simpleDateFormat.format(new Date(currentTimeMillis)));
            String saveBitmapToStorage = saveBitmapToStorage(context, stringBuilder.toString(), bitmap);
            if (!TextUtils.isEmpty(saveBitmapToStorage)) {
                FileUtils.notifyMediaScanner(this.context, saveBitmapToStorage);
                ScreenshotManager.getInstance().insert(new Screenshot(str, str2, currentTimeMillis, saveBitmapToStorage), null);
                TelemetryWrapper.clickToolbarCapture(ScreenshotManager.getInstance().getCategory(this.context, str2), ScreenshotManager.getInstance().getCategoryVersion());
            }
            return saveBitmapToStorage;
        } catch (IOException unused) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x003b A:{SYNTHETIC, Splitter:B:18:0x003b} */
    private static java.lang.String saveBitmapToStorage(android.content.Context r4, java.lang.String r5, android.graphics.Bitmap r6) throws java.io.IOException {
        /*
        r4 = org.mozilla.focus.utils.StorageUtils.getTargetDirForSaveScreenshot(r4);
        r0 = org.mozilla.fileutils.FileUtils.ensureDir(r4);
        if (r0 == 0) goto L_0x003f;
    L_0x000a:
        r0 = ".png";
        r5 = r5.concat(r0);
        r0 = new java.io.File;
        r0.<init>(r4, r5);
        r4 = 0;
        r5 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0035 }
        r5.<init>(r0);	 Catch:{ all -> 0x0035 }
        r1 = android.graphics.Bitmap.CompressFormat.PNG;	 Catch:{ all -> 0x0033 }
        r2 = 0;
        r6 = r6.compress(r1, r2, r5);	 Catch:{ all -> 0x0033 }
        if (r6 == 0) goto L_0x002c;
    L_0x0024:
        r5.flush();	 Catch:{ all -> 0x0033 }
        r4 = r0.getPath();	 Catch:{ all -> 0x0033 }
        goto L_0x002f;
    L_0x002c:
        r0.delete();	 Catch:{ all -> 0x0033 }
    L_0x002f:
        r5.close();	 Catch:{ IOException -> 0x0032 }
    L_0x0032:
        return r4;
    L_0x0033:
        r4 = move-exception;
        goto L_0x0039;
    L_0x0035:
        r5 = move-exception;
        r3 = r5;
        r5 = r4;
        r4 = r3;
    L_0x0039:
        if (r5 == 0) goto L_0x003e;
    L_0x003b:
        r5.close();	 Catch:{ IOException -> 0x003e }
    L_0x003e:
        throw r4;
    L_0x003f:
        r4 = new java.io.IOException;
        r5 = "Can't create folder";
        r4.<init>(r5);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.screenshot.ScreenshotCaptureTask.saveBitmapToStorage(android.content.Context, java.lang.String, android.graphics.Bitmap):java.lang.String");
    }
}
