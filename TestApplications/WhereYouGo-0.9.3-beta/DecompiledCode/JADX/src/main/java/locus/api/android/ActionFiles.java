package locus.api.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import locus.api.android.utils.LocusConst;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.LocusUtils.LocusVersion;
import locus.api.utils.Logger;

public class ActionFiles {
    private static final String TAG = ActionFiles.class.getSimpleName();

    public static boolean importFileSystem(Context context, File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        Intent sendIntent = new Intent("android.intent.action.VIEW");
        sendIntent.setDataAndType(Uri.fromFile(file), getMimeType(file));
        context.startActivity(sendIntent);
        return true;
    }

    public static boolean importFileLocus(Context ctx, File file) {
        return importFileLocus(ctx, LocusUtils.getActiveVersion(ctx), file, true);
    }

    public static boolean importFileLocus(Context ctx, LocusVersion lv, File file, boolean callImport) {
        if (isReadyForImport(ctx, lv, file)) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName(lv.getPackageName(), "menion.android.locus.core.MainActivity");
            intent.setDataAndType(Uri.fromFile(file), getMimeType(file));
            intent.putExtra(LocusConst.INTENT_EXTRA_CALL_IMPORT, callImport);
            ctx.startActivity(intent);
            return true;
        }
        Logger.logE(TAG, "importFileLocus(" + ctx + ", " + lv + ", " + file + ", " + callImport + "), " + "invalid input parameters. Import cannot be performed!");
        return false;
    }

    private static boolean isReadyForImport(Context context, LocusVersion lv, File file) {
        if (file == null || !file.exists() || lv == null) {
            return false;
        }
        return true;
    }

    private static String getMimeType(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return "*/*";
        }
        return "application/" + name.substring(index + 1);
    }
}
