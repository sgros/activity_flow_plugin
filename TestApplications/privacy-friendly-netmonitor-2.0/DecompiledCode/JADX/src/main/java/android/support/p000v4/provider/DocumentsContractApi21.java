package android.support.p000v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.ArrayList;

@RequiresApi(21)
/* renamed from: android.support.v4.provider.DocumentsContractApi21 */
class DocumentsContractApi21 {
    private static final String TAG = "DocumentFile";

    DocumentsContractApi21() {
    }

    public static Uri createFile(Context context, Uri uri, String str, String str2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, str, str2);
        } catch (Exception unused) {
            return null;
        }
    }

    public static Uri createDirectory(Context context, Uri uri, String str) {
        return DocumentsContractApi21.createFile(context, uri, "vnd.android.document/directory", str);
    }

    public static Uri prepareTreeUri(Uri uri) {
        return DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
    }

    public static Uri[] listFiles(Context context, Uri uri) {
        Object e;
        String str;
        StringBuilder stringBuilder;
        Throwable th;
        ContentResolver contentResolver = context.getContentResolver();
        Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        ArrayList arrayList = new ArrayList();
        AutoCloseable autoCloseable = null;
        try {
            Cursor query = contentResolver.query(buildChildDocumentsUriUsingTree, new String[]{"document_id"}, null, null, null);
            while (query.moveToNext()) {
                try {
                    arrayList.add(DocumentsContract.buildDocumentUriUsingTree(uri, query.getString(0)));
                } catch (Exception e2) {
                    e = e2;
                    autoCloseable = query;
                    try {
                        str = TAG;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed query: ");
                        stringBuilder.append(e);
                        Log.w(str, stringBuilder.toString());
                        DocumentsContractApi21.closeQuietly(autoCloseable);
                        return (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
                    } catch (Throwable th2) {
                        th = th2;
                        DocumentsContractApi21.closeQuietly(autoCloseable);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    autoCloseable = query;
                    DocumentsContractApi21.closeQuietly(autoCloseable);
                    throw th;
                }
            }
            DocumentsContractApi21.closeQuietly(query);
        } catch (Exception e3) {
            e = e3;
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed query: ");
            stringBuilder.append(e);
            Log.w(str, stringBuilder.toString());
            DocumentsContractApi21.closeQuietly(autoCloseable);
            return (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
        }
        return (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
    }

    public static Uri renameTo(Context context, Uri uri, String str) {
        try {
            return DocumentsContract.renameDocument(context.getContentResolver(), uri, str);
        } catch (Exception unused) {
            return null;
        }
    }

    private static void closeQuietly(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }
}
