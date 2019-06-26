// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class DocumentsContractApi19
{
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";
    
    public static boolean canRead(final Context context, final Uri uri) {
        return context.checkCallingOrSelfUriPermission(uri, 1) == 0 && !TextUtils.isEmpty((CharSequence)getRawType(context, uri));
    }
    
    public static boolean canWrite(final Context context, final Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        final String rawType = getRawType(context, uri);
        final int queryForInt = queryForInt(context, uri, "flags", 0);
        return !TextUtils.isEmpty((CharSequence)rawType) && ((queryForInt & 0x4) != 0x0 || ("vnd.android.document/directory".equals(rawType) && (queryForInt & 0x8) != 0x0) || (!TextUtils.isEmpty((CharSequence)rawType) && (queryForInt & 0x2) != 0x0));
    }
    
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
    
    public static boolean delete(final Context context, final Uri uri) {
        try {
            return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean exists(Context context, final Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        boolean b = true;
        final AutoCloseable autoCloseable = null;
        context = null;
        Object o;
        final Exception obj;
        try {
            try {
                final Cursor query = contentResolver.query(uri, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                try {
                    if (query.getCount() <= 0) {
                        b = false;
                    }
                    closeQuietly((AutoCloseable)query);
                    return b;
                }
                catch (Exception ex2) {}
            }
            finally {
                o = context;
                final Exception ex = obj;
            }
        }
        catch (Exception obj) {
            o = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(obj);
        Log.w("DocumentFile", sb.toString());
        closeQuietly((AutoCloseable)o);
        return false;
        closeQuietly((AutoCloseable)o);
        throw null;
    }
    
    public static long getFlags(final Context context, final Uri uri) {
        return queryForLong(context, uri, "flags", 0L);
    }
    
    public static String getName(final Context context, final Uri uri) {
        return queryForString(context, uri, "_display_name", null);
    }
    
    private static String getRawType(final Context context, final Uri uri) {
        return queryForString(context, uri, "mime_type", null);
    }
    
    public static String getType(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        if ("vnd.android.document/directory".equals(rawType)) {
            return null;
        }
        return rawType;
    }
    
    public static boolean isDirectory(final Context context, final Uri uri) {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }
    
    public static boolean isDocumentUri(final Context context, final Uri uri) {
        return DocumentsContract.isDocumentUri(context, uri);
    }
    
    public static boolean isFile(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        return !"vnd.android.document/directory".equals(rawType) && !TextUtils.isEmpty((CharSequence)rawType);
    }
    
    public static boolean isVirtual(final Context context, final Uri uri) {
        final boolean documentUri = isDocumentUri(context, uri);
        boolean b = false;
        if (!documentUri) {
            return false;
        }
        if ((getFlags(context, uri) & 0x200L) != 0x0L) {
            b = true;
        }
        return b;
    }
    
    public static long lastModified(final Context context, final Uri uri) {
        return queryForLong(context, uri, "last_modified", 0L);
    }
    
    public static long length(final Context context, final Uri uri) {
        return queryForLong(context, uri, "_size", 0L);
    }
    
    private static int queryForInt(final Context context, final Uri uri, final String s, final int n) {
        return (int)queryForLong(context, uri, s, n);
    }
    
    private static long queryForLong(Context context, final Uri uri, final String s, final long n) {
        final ContentResolver contentResolver = context.getContentResolver();
        final AutoCloseable autoCloseable = null;
        context = null;
        Object o;
        final Exception obj;
        try {
            try {
                final Cursor query = contentResolver.query(uri, new String[] { s }, (String)null, (String[])null, (String)null);
                try {
                    if (query.moveToFirst() && !query.isNull(0)) {
                        final long long1 = query.getLong(0);
                        closeQuietly((AutoCloseable)query);
                        return long1;
                    }
                    closeQuietly((AutoCloseable)query);
                    return n;
                }
                catch (Exception ex2) {}
            }
            finally {
                o = context;
                final Exception ex = obj;
            }
        }
        catch (Exception obj) {
            o = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(obj);
        Log.w("DocumentFile", sb.toString());
        closeQuietly((AutoCloseable)o);
        return n;
        closeQuietly((AutoCloseable)o);
        throw null;
    }
    
    private static String queryForString(Context string, Uri query, final String obj, final String s) {
        final ContentResolver contentResolver = string.getContentResolver();
        final AutoCloseable autoCloseable = null;
        string = null;
        AutoCloseable autoCloseable2;
        try {
            try {
                query = (Uri)contentResolver.query(query, new String[] { (String)obj }, (String)null, (String[])null, (String)null);
                try {
                    if (((Cursor)query).moveToFirst() && !((Cursor)query).isNull(0)) {
                        string = (Context)((Cursor)query).getString(0);
                        closeQuietly((AutoCloseable)query);
                        return (String)string;
                    }
                    closeQuietly((AutoCloseable)query);
                    return s;
                }
                catch (Exception obj) {}
                finally {
                    string = (Context)query;
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
        return s;
        closeQuietly((AutoCloseable)string);
    }
}
