package org.mozilla.focus.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.rocket.C0769R;

public class StorageUtils {
    public static File getAppMediaDirOnRemovableStorage(Context context) throws NoRemovableStorageException {
        File firstRemovableMedia = getFirstRemovableMedia(context);
        if (firstRemovableMedia == null) {
            throw new NoRemovableStorageException("No removable media to use");
        } else if ("mounted".equals(Environment.getExternalStorageState(firstRemovableMedia))) {
            return firstRemovableMedia;
        } else {
            throw new NoRemovableStorageException("No mounted-removable media to use");
        }
    }

    public static File getTargetDirOnRemovableStorageForDownloads(Context context, String str) throws NoRemovableStorageException {
        if (!Settings.getInstance(context).shouldSaveToRemovableStorage()) {
            return null;
        }
        File file = new File(getAppMediaDirOnRemovableStorage(context), "downloads");
        if (MimeUtils.isImage(str)) {
            return new File(file, "pictures");
        }
        return new File(file, "others");
    }

    public static File getTargetDirForSaveScreenshot(Context context) {
        String replaceAll = context.getString(C0769R.string.app_name).replaceAll(" ", "");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), replaceAll);
        FileUtils.ensureDir(file);
        if (!Settings.getInstance(context).shouldSaveToRemovableStorage()) {
            return file;
        }
        try {
            return new File(getAppMediaDirOnRemovableStorage(context), replaceAll);
        } catch (NoRemovableStorageException unused) {
            return file;
        }
    }

    static File getFirstRemovableMedia(Context context) {
        for (File file : context.getExternalMediaDirs()) {
            if (file != null) {
                try {
                    if (Environment.isExternalStorageRemovable(file)) {
                        return file;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
