// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.download;

import org.mozilla.threadutils.ThreadUtils;
import java.util.ArrayList;
import android.content.AsyncQueryHandler;
import org.mozilla.focus.components.RelocateService;
import android.content.ContentResolver;
import org.mozilla.focus.provider.DownloadContract;
import org.mozilla.focus.utils.CursorUtils;
import java.io.File;
import android.net.Uri;
import java.util.Locale;
import android.webkit.MimeTypeMap;
import java.net.URLEncoder;
import android.app.DownloadManager;
import android.app.DownloadManager$Query;
import android.text.TextUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import android.view.View$OnClickListener;
import android.support.design.widget.Snackbar;
import org.mozilla.rocket.util.LoggerWrapper;
import java.util.List;
import org.mozilla.focus.utils.IntentUtils;
import android.view.View;
import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;

public class DownloadInfoManager
{
    private static Context mContext;
    private static DownloadInfoQueryHandler mQueryHandler;
    private static DownloadInfoManager sInstance;
    
    private static DownloadInfo createEmptyDownloadInfo(final long l, final long i, final String fileUri) {
        final DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setRowId(i);
        downloadInfo.setDownloadId(l);
        downloadInfo.setFileUri(fileUri);
        downloadInfo.setStatusInt(-1);
        return downloadInfo;
    }
    
    private static DownloadInfo cursorToDownloadInfo(final Cursor cursor) {
        final long long1 = cursor.getLong(cursor.getColumnIndex("download_id"));
        final String string = cursor.getString(cursor.getColumnIndex("file_path"));
        final Long value = cursor.getLong(cursor.getColumnIndex("_id"));
        final DownloadPojo queryDownloadManager = queryDownloadManager(DownloadInfoManager.mContext, long1);
        DownloadInfo downloadInfo;
        if (queryDownloadManager == null) {
            downloadInfo = createEmptyDownloadInfo(long1, value, string);
        }
        else {
            downloadInfo = pojoToDownloadInfo(queryDownloadManager, string, value);
        }
        return downloadInfo;
    }
    
    private static ContentValues getContentValuesFromDownloadInfo(final DownloadInfo downloadInfo) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("download_id", downloadInfo.getDownloadId());
        contentValues.put("file_path", downloadInfo.getFileUri());
        contentValues.put("status", Integer.valueOf(downloadInfo.getStatus()));
        contentValues.put("is_read", Boolean.valueOf(downloadInfo.isRead()));
        return contentValues;
    }
    
    public static DownloadInfoManager getInstance() {
        if (DownloadInfoManager.sInstance == null) {
            DownloadInfoManager.sInstance = new DownloadInfoManager();
        }
        return DownloadInfoManager.sInstance;
    }
    
    public static void init(final Context mContext) {
        DownloadInfoManager.mContext = mContext;
        DownloadInfoManager.mQueryHandler = new DownloadInfoQueryHandler(mContext);
    }
    
    public static void notifyRowUpdated(final Context context, final long n) {
        final Intent intent = new Intent("row_updated");
        intent.putExtra("row id", n);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    
    private static DownloadInfo pojoToDownloadInfo(final DownloadPojo downloadPojo, final String fileUri, final long l) {
        final DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setRowId(l);
        downloadInfo.setFileName(downloadPojo.fileName);
        downloadInfo.setDownloadId(downloadPojo.downloadId);
        downloadInfo.setSize(downloadPojo.length);
        downloadInfo.setSizeTotal(downloadPojo.length);
        downloadInfo.setSizeSoFar(downloadPojo.sizeSoFar);
        downloadInfo.setStatusInt(downloadPojo.status);
        downloadInfo.setDate(downloadPojo.timeStamp);
        downloadInfo.setMediaUri(downloadPojo.mediaUri);
        downloadInfo.setFileUri(downloadPojo.fileUri);
        downloadInfo.setMimeType(downloadPojo.mime);
        downloadInfo.setFileExtension(downloadPojo.fileExtension);
        if (TextUtils.isEmpty((CharSequence)downloadPojo.fileUri)) {
            downloadInfo.setFileUri(fileUri);
        }
        else {
            downloadInfo.setFileUri(downloadPojo.fileUri);
        }
        return downloadInfo;
    }
    
    private static DownloadPojo queryDownloadManager(Context query, final long downloadId) {
        final DownloadManager$Query downloadManager$Query = new DownloadManager$Query();
        downloadManager$Query.setFilterById(new long[] { downloadId });
        query = (Context)((DownloadManager)query.getSystemService("download")).query(downloadManager$Query);
        final DownloadPojo downloadPojo = new DownloadPojo();
        downloadPojo.downloadId = downloadId;
        if (query != null) {
            try {
                if (((Cursor)query).moveToFirst()) {
                    downloadPojo.desc = ((Cursor)query).getString(((Cursor)query).getColumnIndex("description"));
                    downloadPojo.status = ((Cursor)query).getInt(((Cursor)query).getColumnIndex("status"));
                    downloadPojo.length = ((Cursor)query).getDouble(((Cursor)query).getColumnIndex("total_size"));
                    downloadPojo.sizeSoFar = ((Cursor)query).getDouble(((Cursor)query).getColumnIndex("bytes_so_far"));
                    downloadPojo.timeStamp = ((Cursor)query).getLong(((Cursor)query).getColumnIndex("last_modified_timestamp"));
                    downloadPojo.mediaUri = ((Cursor)query).getString(((Cursor)query).getColumnIndex("mediaprovider_uri"));
                    downloadPojo.fileUri = ((Cursor)query).getString(((Cursor)query).getColumnIndex("local_uri"));
                    if (downloadPojo.fileUri != null) {
                        final String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(URLEncoder.encode(downloadPojo.fileUri, "UTF-8"));
                        downloadPojo.mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl.toLowerCase(Locale.ROOT));
                        downloadPojo.fileExtension = fileExtensionFromUrl;
                        downloadPojo.fileName = new File(Uri.parse(downloadPojo.fileUri).getPath()).getName();
                    }
                    return downloadPojo;
                }
            }
            catch (Exception ex) {
                return null;
            }
            finally {
                CursorUtils.closeCursorSafely((Cursor)query);
            }
        }
        CursorUtils.closeCursorSafely((Cursor)query);
        return null;
    }
    
    public void delete(final Long n, final AsyncDeleteListener asyncDeleteListener) {
        DownloadInfoManager.mQueryHandler.startDelete(2, (Object)new AsyncDeleteWrapper(n, asyncDeleteListener), DownloadContract.Download.CONTENT_URI, "_id = ?", new String[] { Long.toString(n) });
    }
    
    public DownloadManager getDownloadManager() {
        return (DownloadManager)DownloadInfoManager.mContext.getSystemService("download");
    }
    
    public void insert(final DownloadInfo downloadInfo, final AsyncInsertListener asyncInsertListener) {
        DownloadInfoManager.mQueryHandler.startInsert(2, (Object)asyncInsertListener, DownloadContract.Download.CONTENT_URI, getContentValuesFromDownloadInfo(downloadInfo));
    }
    
    public void markAllItemsAreRead(final AsyncUpdateListener asyncUpdateListener) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("is_read", "1");
        DownloadInfoManager.mQueryHandler.startUpdate(2, (Object)asyncUpdateListener, DownloadContract.Download.CONTENT_URI, contentValues, "status=? and is_read = ?", new String[] { String.valueOf(8), String.valueOf("0") });
    }
    
    public void query(final int i, final int j, final AsyncQueryListener asyncQueryListener) {
        final StringBuilder sb = new StringBuilder();
        sb.append(DownloadContract.Download.CONTENT_URI.toString());
        sb.append("?offset=");
        sb.append(i);
        sb.append("&limit=");
        sb.append(j);
        DownloadInfoManager.mQueryHandler.startQuery(2, (Object)asyncQueryListener, Uri.parse(sb.toString()), (String[])null, (String)null, (String[])null, "_id DESC");
    }
    
    public void queryByDownloadId(final Long n, final AsyncQueryListener asyncQueryListener) {
        DownloadInfoManager.mQueryHandler.startQuery(2, (Object)asyncQueryListener, Uri.parse(DownloadContract.Download.CONTENT_URI.toString()), (String[])null, "download_id==?", new String[] { Long.toString(n) }, (String)null);
    }
    
    public void queryByRowId(final Long n, final AsyncQueryListener asyncQueryListener) {
        DownloadInfoManager.mQueryHandler.startQuery(2, (Object)asyncQueryListener, Uri.parse(DownloadContract.Download.CONTENT_URI.toString()), (String[])null, "_id==?", new String[] { Long.toString(n) }, (String)null);
    }
    
    public void queryDownloadingAndUnreadIds(final AsyncQueryListener asyncQueryListener) {
        DownloadInfoManager.mQueryHandler.startQuery(2, (Object)asyncQueryListener, Uri.parse(DownloadContract.Download.CONTENT_URI.toString()), (String[])null, "status!=? or is_read=?", new String[] { String.valueOf(8), String.valueOf("0") }, (String)null);
    }
    
    public boolean recordExists(final long lng) {
        final ContentResolver contentResolver = DownloadInfoManager.mContext.getContentResolver();
        final Uri content_URI = DownloadContract.Download.CONTENT_URI;
        final StringBuilder sb = new StringBuilder();
        sb.append("download_id=");
        sb.append(lng);
        final Cursor query = contentResolver.query(content_URI, (String[])null, sb.toString(), (String[])null, (String)null);
        boolean b2;
        final boolean b = b2 = false;
        Label_0126: {
            if (query != null) {
                b2 = b;
                try {
                    try {
                        if (query.getCount() <= 0) {
                            break Label_0126;
                        }
                        final boolean moveToFirst = query.moveToFirst();
                        b2 = b;
                        if (moveToFirst) {
                            b2 = true;
                        }
                        break Label_0126;
                    }
                    finally {}
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    b2 = b;
                    break Label_0126;
                }
                CursorUtils.closeCursorSafely(query);
            }
        }
        CursorUtils.closeCursorSafely(query);
        return b2;
    }
    
    public void replacePath(final long l, final String pathname, String mime) {
        final File file = new File(pathname);
        final DownloadPojo queryDownloadManager = queryDownloadManager(DownloadInfoManager.mContext, l);
        if (queryDownloadManager != null) {
            final DownloadManager downloadManager = (DownloadManager)DownloadInfoManager.mContext.getSystemService("download");
            String desc;
            if (TextUtils.isEmpty((CharSequence)queryDownloadManager.desc)) {
                desc = "Downloaded from internet";
            }
            else {
                desc = queryDownloadManager.desc;
            }
            if (TextUtils.isEmpty((CharSequence)queryDownloadManager.mime)) {
                if (TextUtils.isEmpty((CharSequence)mime)) {
                    mime = "*/*";
                }
            }
            else {
                mime = queryDownloadManager.mime;
            }
            this.queryByDownloadId(l, (AsyncQueryListener)new AsyncQueryListener() {
                final /* synthetic */ long val$newId = downloadManager.addCompletedDownload(file.getName(), desc, true, mime, pathname, file.length(), true);
                
                @Override
                public void onQueryComplete(final List list) {
                    for (int i = 0; i < list.size(); ++i) {
                        final DownloadInfo downloadInfo = list.get(i);
                        if (!downloadInfo.existInDownloadManager()) {
                            throw new IllegalStateException("File entry disappeared after being moved");
                        }
                        if (l == downloadInfo.getDownloadId()) {
                            final long longValue = downloadInfo.getRowId();
                            final DownloadInfo access$100 = pojoToDownloadInfo(queryDownloadManager, pathname, longValue);
                            access$100.setDownloadId(this.val$newId);
                            DownloadInfoManager.this.updateByRowId(access$100, (AsyncUpdateListener)new AsyncUpdateListener() {
                                @Override
                                public void onUpdateComplete(final int n) {
                                    DownloadInfoManager.notifyRowUpdated(DownloadInfoManager.mContext, longValue);
                                    RelocateService.broadcastRelocateFinished(DownloadInfoManager.mContext, longValue);
                                }
                            });
                            downloadManager.remove(new long[] { l });
                            break;
                        }
                    }
                }
            });
            return;
        }
        throw new IllegalStateException("File entry disappeared after being moved");
    }
    
    public void showOpenDownloadSnackBar(final Long n, final View view, final String s) {
        this.queryByRowId(n, (AsyncQueryListener)new _$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA(s, view));
    }
    
    public void updateByRowId(final DownloadInfo downloadInfo, final AsyncUpdateListener asyncUpdateListener) {
        DownloadInfoManager.mQueryHandler.startUpdate(2, (Object)asyncUpdateListener, DownloadContract.Download.CONTENT_URI, getContentValuesFromDownloadInfo(downloadInfo), "_id = ?", new String[] { Long.toString(downloadInfo.getRowId()) });
    }
    
    public interface AsyncDeleteListener
    {
        void onDeleteComplete(final int p0, final long p1);
    }
    
    private static final class AsyncDeleteWrapper
    {
        public long id;
        public AsyncDeleteListener listener;
        
        public AsyncDeleteWrapper(final long id, final AsyncDeleteListener listener) {
            this.id = id;
            this.listener = listener;
        }
    }
    
    public interface AsyncInsertListener
    {
        void onInsertComplete(final long p0);
    }
    
    public interface AsyncQueryListener
    {
        void onQueryComplete(final List<DownloadInfo> p0);
    }
    
    public interface AsyncUpdateListener
    {
        void onUpdateComplete(final int p0);
    }
    
    private static final class DownloadInfoQueryHandler extends AsyncQueryHandler
    {
        public DownloadInfoQueryHandler(final Context context) {
            super(context.getContentResolver());
        }
        
        protected void onDeleteComplete(final int n, final Object o, final int n2) {
            if (n == 2) {
                if (o != null) {
                    final AsyncDeleteWrapper asyncDeleteWrapper = (AsyncDeleteWrapper)o;
                    if (asyncDeleteWrapper.listener != null) {
                        asyncDeleteWrapper.listener.onDeleteComplete(n2, asyncDeleteWrapper.id);
                    }
                }
            }
        }
        
        protected void onInsertComplete(final int n, final Object o, final Uri uri) {
            if (n == 2) {
                if (o != null) {
                    long long1;
                    if (uri == null) {
                        long1 = -1L;
                    }
                    else {
                        long1 = Long.parseLong(uri.getLastPathSegment());
                    }
                    ((AsyncInsertListener)o).onInsertComplete(long1);
                }
            }
        }
        
        protected void onQueryComplete(final int n, final Object o, final Cursor cursor) {
            if (n != 2) {
                CursorUtils.closeCursorSafely(cursor);
            }
            else {
                ThreadUtils.postToBackgroundThread(new Runnable() {
                    @Override
                    public void run() {
                        if (o != null) {
                            final ArrayList<DownloadInfo> list = new ArrayList<DownloadInfo>();
                            Label_0079: {
                                if (cursor != null) {
                                    try {
                                        try {
                                            while (cursor.moveToNext()) {
                                                list.add(cursorToDownloadInfo(cursor));
                                            }
                                        }
                                        finally {}
                                    }
                                    catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    CursorUtils.closeCursorSafely(cursor);
                                    break Label_0079;
                                    CursorUtils.closeCursorSafely(cursor);
                                }
                            }
                            ThreadUtils.postToMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((AsyncQueryListener)o).onQueryComplete(list);
                                }
                            });
                        }
                        else {
                            CursorUtils.closeCursorSafely(cursor);
                        }
                    }
                });
            }
        }
        
        protected void onUpdateComplete(final int n, final Object o, final int n2) {
            if (n == 2) {
                if (o != null) {
                    ((AsyncUpdateListener)o).onUpdateComplete(n2);
                }
            }
        }
    }
    
    private static class DownloadPojo
    {
        String desc;
        long downloadId;
        String fileExtension;
        String fileName;
        String fileUri;
        double length;
        String mediaUri;
        String mime;
        double sizeSoFar;
        int status;
        long timeStamp;
    }
}
