// 
// Decompiled by Procyon v0.5.34
// 

package locus.api.android;

import android.net.Uri;
import android.content.Intent;
import locus.api.utils.Logger;
import locus.api.android.utils.LocusUtils;
import android.content.Context;
import java.io.File;

public class ActionFiles
{
    private static final String TAG;
    
    static {
        TAG = ActionFiles.class.getSimpleName();
    }
    
    private static String getMimeType(final File file) {
        final String name = file.getName();
        final int lastIndex = name.lastIndexOf(".");
        String string;
        if (lastIndex == -1) {
            string = "*/*";
        }
        else {
            string = "application/" + name.substring(lastIndex + 1);
        }
        return string;
    }
    
    public static boolean importFileLocus(final Context context, final File file) {
        return importFileLocus(context, LocusUtils.getActiveVersion(context), file, true);
    }
    
    public static boolean importFileLocus(final Context obj, final LocusUtils.LocusVersion obj2, final File obj3, final boolean b) {
        boolean b2;
        if (!isReadyForImport(obj, obj2, obj3)) {
            Logger.logE(ActionFiles.TAG, "importFileLocus(" + obj + ", " + obj2 + ", " + obj3 + ", " + b + "), " + "invalid input parameters. Import cannot be performed!");
            b2 = false;
        }
        else {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.setClassName(obj2.getPackageName(), "menion.android.locus.core.MainActivity");
            intent.setDataAndType(Uri.fromFile(obj3), getMimeType(obj3));
            intent.putExtra("INTENT_EXTRA_CALL_IMPORT", b);
            obj.startActivity(intent);
            b2 = true;
        }
        return b2;
    }
    
    public static boolean importFileSystem(final Context context, final File file) {
        boolean b;
        if (file == null || !file.exists()) {
            b = false;
        }
        else {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(file), getMimeType(file));
            context.startActivity(intent);
            b = true;
        }
        return b;
    }
    
    private static boolean isReadyForImport(final Context context, final LocusUtils.LocusVersion locusVersion, final File file) {
        return file != null && file.exists() && locusVersion != null;
    }
}
