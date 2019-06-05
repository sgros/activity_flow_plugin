// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import java.util.ArrayList;
import android.provider.DocumentsContract;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class DocumentsContractApi21
{
    private static final String TAG = "DocumentFile";
    
    private static void closeQuietly(final AutoCloseable autoCloseable) {
        if (autoCloseable == null) {
            goto Label_0016;
        }
        try {
            autoCloseable.close();
            goto Label_0016;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            goto Label_0016;
        }
    }
    
    public static Uri createDirectory(final Context context, final Uri uri, final String s) {
        return createFile(context, uri, "vnd.android.document/directory", s);
    }
    
    public static Uri createFile(final Context context, final Uri uri, final String s, final String s2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, s, s2);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Uri[] listFiles(Context ex, final Uri uri) {
        Object obj = ((Context)ex).getContentResolver();
        final Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        final ArrayList<Uri> list = new ArrayList<Uri>();
        final AutoCloseable autoCloseable = null;
        ex = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                obj = ((ContentResolver)obj).query(buildChildDocumentsUriUsingTree, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                try {
                    while (((Cursor)obj).moveToNext()) {
                        list.add(DocumentsContract.buildDocumentUriUsingTree(uri, ((Cursor)obj).getString(0)));
                    }
                    closeQuietly((AutoCloseable)obj);
                }
                catch (Exception ex) {}
                finally {
                    ex = (Exception)obj;
                }
            }
            finally {}
        }
        catch (Exception obj) {
            autoCloseable2 = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(obj);
        Log.w("DocumentFile", sb.toString());
        closeQuietly(autoCloseable2);
        return list.toArray(new Uri[list.size()]);
        closeQuietly((AutoCloseable)ex);
    }
    
    public static Uri prepareTreeUri(final Uri uri) {
        return DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
    }
    
    public static Uri renameTo(final Context context, final Uri uri, final String s) {
        try {
            return DocumentsContract.renameDocument(context.getContentResolver(), uri, s);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
