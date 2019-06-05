package org.mozilla.focus.download;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.p001v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import com.adjust.sdk.Constants;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.mozilla.focus.components.RelocateService;
import org.mozilla.focus.provider.DownloadContract.Download;
import org.mozilla.focus.utils.CursorUtils;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.util.LoggerWrapper;
import org.mozilla.threadutils.ThreadUtils;

public class DownloadInfoManager {
    private static Context mContext;
    private static DownloadInfoQueryHandler mQueryHandler;
    private static DownloadInfoManager sInstance;

    public interface AsyncDeleteListener {
        void onDeleteComplete(int i, long j);
    }

    private static final class AsyncDeleteWrapper {
        /* renamed from: id */
        public long f41id;
        public AsyncDeleteListener listener;

        public AsyncDeleteWrapper(long j, AsyncDeleteListener asyncDeleteListener) {
            this.f41id = j;
            this.listener = asyncDeleteListener;
        }
    }

    public interface AsyncInsertListener {
        void onInsertComplete(long j);
    }

    public interface AsyncQueryListener {
        void onQueryComplete(List<DownloadInfo> list);
    }

    public interface AsyncUpdateListener {
        void onUpdateComplete(int i);
    }

    private static final class DownloadInfoQueryHandler extends AsyncQueryHandler {
        public DownloadInfoQueryHandler(Context context) {
            super(context.getContentResolver());
        }

        /* Access modifiers changed, original: protected */
        public void onInsertComplete(int i, Object obj, Uri uri) {
            if (i == 2 && obj != null) {
                ((AsyncInsertListener) obj).onInsertComplete(uri == null ? -1 : Long.parseLong(uri.getLastPathSegment()));
            }
        }

        /* Access modifiers changed, original: protected */
        public void onDeleteComplete(int i, Object obj, int i2) {
            if (i == 2 && obj != null) {
                AsyncDeleteWrapper asyncDeleteWrapper = (AsyncDeleteWrapper) obj;
                if (asyncDeleteWrapper.listener != null) {
                    asyncDeleteWrapper.listener.onDeleteComplete(i2, asyncDeleteWrapper.f41id);
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onUpdateComplete(int i, Object obj, int i2) {
            if (i == 2 && obj != null) {
                ((AsyncUpdateListener) obj).onUpdateComplete(i2);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onQueryComplete(int i, final Object obj, final Cursor cursor) {
            if (i != 2) {
                CursorUtils.closeCursorSafely(cursor);
            } else {
                ThreadUtils.postToBackgroundThread(new Runnable() {
                    public void run() {
                        if (obj != null) {
                            final ArrayList arrayList = new ArrayList();
                            if (cursor != null) {
                                while (cursor.moveToNext()) {
                                    try {
                                        arrayList.add(DownloadInfoManager.cursorToDownloadInfo(cursor));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } catch (Throwable th) {
                                        CursorUtils.closeCursorSafely(cursor);
                                    }
                                }
                                CursorUtils.closeCursorSafely(cursor);
                            }
                            ThreadUtils.postToMainThread(new Runnable() {
                                public void run() {
                                    ((AsyncQueryListener) obj).onQueryComplete(arrayList);
                                }
                            });
                            return;
                        }
                        CursorUtils.closeCursorSafely(cursor);
                    }
                });
            }
        }
    }

    private static class DownloadPojo {
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

        private DownloadPojo() {
        }

        /* synthetic */ DownloadPojo(C04501 c04501) {
            this();
        }
    }

    public static DownloadInfoManager getInstance() {
        if (sInstance == null) {
            sInstance = new DownloadInfoManager();
        }
        return sInstance;
    }

    public static void init(Context context) {
        mContext = context;
        mQueryHandler = new DownloadInfoQueryHandler(context);
    }

    public void insert(DownloadInfo downloadInfo, AsyncInsertListener asyncInsertListener) {
        mQueryHandler.startInsert(2, asyncInsertListener, Download.CONTENT_URI, getContentValuesFromDownloadInfo(downloadInfo));
    }

    public void delete(Long l, AsyncDeleteListener asyncDeleteListener) {
        mQueryHandler.startDelete(2, new AsyncDeleteWrapper(l.longValue(), asyncDeleteListener), Download.CONTENT_URI, "_id = ?", new String[]{Long.toString(l.longValue())});
    }

    public void updateByRowId(DownloadInfo downloadInfo, AsyncUpdateListener asyncUpdateListener) {
        AsyncUpdateListener asyncUpdateListener2 = asyncUpdateListener;
        mQueryHandler.startUpdate(2, asyncUpdateListener2, Download.CONTENT_URI, getContentValuesFromDownloadInfo(downloadInfo), "_id = ?", new String[]{Long.toString(downloadInfo.getRowId().longValue())});
    }

    public void query(int i, int i2, AsyncQueryListener asyncQueryListener) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Download.CONTENT_URI.toString());
        stringBuilder.append("?offset=");
        stringBuilder.append(i);
        stringBuilder.append("&limit=");
        stringBuilder.append(i2);
        AsyncQueryListener asyncQueryListener2 = asyncQueryListener;
        mQueryHandler.startQuery(2, asyncQueryListener2, Uri.parse(stringBuilder.toString()), null, null, null, "_id DESC");
    }

    public void queryByDownloadId(Long l, AsyncQueryListener asyncQueryListener) {
        String[] strArr = new String[]{Long.toString(l.longValue())};
        AsyncQueryListener asyncQueryListener2 = asyncQueryListener;
        mQueryHandler.startQuery(2, asyncQueryListener2, Uri.parse(Download.CONTENT_URI.toString()), null, "download_id==?", strArr, null);
    }

    public void queryByRowId(Long l, AsyncQueryListener asyncQueryListener) {
        String[] strArr = new String[]{Long.toString(l.longValue())};
        AsyncQueryListener asyncQueryListener2 = asyncQueryListener;
        mQueryHandler.startQuery(2, asyncQueryListener2, Uri.parse(Download.CONTENT_URI.toString()), null, "_id==?", strArr, null);
    }

    public void queryDownloadingAndUnreadIds(AsyncQueryListener asyncQueryListener) {
        String[] strArr = new String[]{String.valueOf(8), String.valueOf("0")};
        AsyncQueryListener asyncQueryListener2 = asyncQueryListener;
        mQueryHandler.startQuery(2, asyncQueryListener2, Uri.parse(Download.CONTENT_URI.toString()), null, "status!=? or is_read=?", strArr, null);
    }

    public void markAllItemsAreRead(AsyncUpdateListener asyncUpdateListener) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_read", "1");
        AsyncUpdateListener asyncUpdateListener2 = asyncUpdateListener;
        mQueryHandler.startUpdate(2, asyncUpdateListener2, Download.CONTENT_URI, contentValues, "status=? and is_read = ?", new String[]{String.valueOf(8), String.valueOf("0")});
    }

    public boolean recordExists(long j) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = Download.CONTENT_URI;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("download_id=");
        stringBuilder.append(j);
        Cursor query = contentResolver.query(uri, null, stringBuilder.toString(), null, null);
        boolean z = false;
        if (query != null) {
            try {
                if (query.getCount() > 0 && query.moveToFirst()) {
                    z = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                CursorUtils.closeCursorSafely(query);
            }
        }
        CursorUtils.closeCursorSafely(query);
        return z;
    }

    public void replacePath(long j, String str, String str2) {
        File file = new File(str);
        DownloadPojo queryDownloadManager = queryDownloadManager(mContext, j);
        if (queryDownloadManager != null) {
            String str3;
            String str4;
            final String str5;
            final long addCompletedDownload;
            final long j2;
            final DownloadPojo downloadPojo;
            final DownloadManager downloadManager;
            DownloadManager downloadManager2 = (DownloadManager) mContext.getSystemService("download");
            String str6 = TextUtils.isEmpty(queryDownloadManager.desc) ? "Downloaded from internet" : queryDownloadManager.desc;
            if (!TextUtils.isEmpty(queryDownloadManager.mime)) {
                str3 = queryDownloadManager.mime;
            } else if (TextUtils.isEmpty(str2)) {
                str3 = "*/*";
            } else {
                str4 = str2;
                str5 = str;
                addCompletedDownload = downloadManager2.addCompletedDownload(file.getName(), str6, true, str4, str5, file.length(), true);
                j2 = j;
                downloadPojo = queryDownloadManager;
                downloadManager = downloadManager2;
                queryByDownloadId(Long.valueOf(j), new AsyncQueryListener() {
                    public void onQueryComplete(List list) {
                        int i = 0;
                        while (i < list.size()) {
                            DownloadInfo downloadInfo = (DownloadInfo) list.get(i);
                            if (!downloadInfo.existInDownloadManager()) {
                                throw new IllegalStateException("File entry disappeared after being moved");
                            } else if (j2 == downloadInfo.getDownloadId().longValue()) {
                                final long longValue = downloadInfo.getRowId().longValue();
                                DownloadInfo access$100 = DownloadInfoManager.pojoToDownloadInfo(downloadPojo, str5, longValue);
                                access$100.setDownloadId(Long.valueOf(addCompletedDownload));
                                DownloadInfoManager.this.updateByRowId(access$100, new AsyncUpdateListener() {
                                    public void onUpdateComplete(int i) {
                                        DownloadInfoManager.notifyRowUpdated(DownloadInfoManager.mContext, longValue);
                                        RelocateService.broadcastRelocateFinished(DownloadInfoManager.mContext, longValue);
                                    }
                                });
                                downloadManager.remove(new long[]{j2});
                                return;
                            } else {
                                i++;
                            }
                        }
                    }
                });
                return;
            }
            str4 = str3;
            str5 = str;
            addCompletedDownload = downloadManager2.addCompletedDownload(file.getName(), str6, true, str4, str5, file.length(), true);
            j2 = j;
            downloadPojo = queryDownloadManager;
            downloadManager = downloadManager2;
            queryByDownloadId(Long.valueOf(j), /* anonymous class already generated */);
            return;
        }
        throw new IllegalStateException("File entry disappeared after being moved");
    }

    public static void notifyRowUpdated(Context context, long j) {
        Intent intent = new Intent("row_updated");
        intent.putExtra("row id", j);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public DownloadManager getDownloadManager() {
        return (DownloadManager) mContext.getSystemService("download");
    }

    public void showOpenDownloadSnackBar(Long l, View view, String str) {
        queryByRowId(l, new C0683-$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA(str, view));
    }

    static /* synthetic */ void lambda$showOpenDownloadSnackBar$1(String str, View view, List list) {
        if ((list.size() > 0 ? 1 : null) == null) {
            LoggerWrapper.throwOrWarn(str, "Download Completed with unknown local row id");
            return;
        }
        DownloadInfo downloadInfo = (DownloadInfo) list.get(0);
        boolean existInDownloadManager = downloadInfo.existInDownloadManager();
        if (!existInDownloadManager) {
            LoggerWrapper.throwOrWarn(str, "Download Completed with unknown DownloadManager id");
        }
        Snackbar make = Snackbar.make(view, view.getContext().getString(C0769R.string.download_completed, new Object[]{downloadInfo.getFileName()}), 0);
        if (existInDownloadManager) {
            make.setAction((int) C0769R.string.open, new C0447-$$Lambda$DownloadInfoManager$PzoHQaka_eea0cAHebO641vAq-U(view, downloadInfo));
        }
        make.show();
    }

    private static ContentValues getContentValuesFromDownloadInfo(DownloadInfo downloadInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("download_id", downloadInfo.getDownloadId());
        contentValues.put("file_path", downloadInfo.getFileUri());
        contentValues.put("status", Integer.valueOf(downloadInfo.getStatus()));
        contentValues.put("is_read", Boolean.valueOf(downloadInfo.isRead()));
        return contentValues;
    }

    private static DownloadInfo cursorToDownloadInfo(Cursor cursor) {
        long j = cursor.getLong(cursor.getColumnIndex("download_id"));
        String string = cursor.getString(cursor.getColumnIndex("file_path"));
        Long valueOf = Long.valueOf(cursor.getLong(cursor.getColumnIndex("_id")));
        DownloadPojo queryDownloadManager = queryDownloadManager(mContext, j);
        return queryDownloadManager == null ? createEmptyDownloadInfo(j, valueOf.longValue(), string) : pojoToDownloadInfo(queryDownloadManager, string, valueOf.longValue());
    }

    private static DownloadInfo pojoToDownloadInfo(DownloadPojo downloadPojo, String str, long j) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setRowId(Long.valueOf(j));
        downloadInfo.setFileName(downloadPojo.fileName);
        downloadInfo.setDownloadId(Long.valueOf(downloadPojo.downloadId));
        downloadInfo.setSize(downloadPojo.length);
        downloadInfo.setSizeTotal(downloadPojo.length);
        downloadInfo.setSizeSoFar(downloadPojo.sizeSoFar);
        downloadInfo.setStatusInt(downloadPojo.status);
        downloadInfo.setDate(downloadPojo.timeStamp);
        downloadInfo.setMediaUri(downloadPojo.mediaUri);
        downloadInfo.setFileUri(downloadPojo.fileUri);
        downloadInfo.setMimeType(downloadPojo.mime);
        downloadInfo.setFileExtension(downloadPojo.fileExtension);
        if (TextUtils.isEmpty(downloadPojo.fileUri)) {
            downloadInfo.setFileUri(str);
        } else {
            downloadInfo.setFileUri(downloadPojo.fileUri);
        }
        return downloadInfo;
    }

    private static DownloadInfo createEmptyDownloadInfo(long j, long j2, String str) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setRowId(Long.valueOf(j2));
        downloadInfo.setDownloadId(Long.valueOf(j));
        downloadInfo.setFileUri(str);
        downloadInfo.setStatusInt(-1);
        return downloadInfo;
    }

    private static DownloadPojo queryDownloadManager(Context context, long j) {
        Query query = new Query();
        query.setFilterById(new long[]{j});
        Cursor query2 = ((DownloadManager) context.getSystemService("download")).query(query);
        DownloadPojo downloadPojo = new DownloadPojo();
        downloadPojo.downloadId = j;
        if (query2 != null) {
            try {
                if (query2.moveToFirst()) {
                    downloadPojo.desc = query2.getString(query2.getColumnIndex("description"));
                    downloadPojo.status = query2.getInt(query2.getColumnIndex("status"));
                    downloadPojo.length = query2.getDouble(query2.getColumnIndex("total_size"));
                    downloadPojo.sizeSoFar = query2.getDouble(query2.getColumnIndex("bytes_so_far"));
                    downloadPojo.timeStamp = query2.getLong(query2.getColumnIndex("last_modified_timestamp"));
                    downloadPojo.mediaUri = query2.getString(query2.getColumnIndex("mediaprovider_uri"));
                    downloadPojo.fileUri = query2.getString(query2.getColumnIndex("local_uri"));
                    if (downloadPojo.fileUri != null) {
                        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(URLEncoder.encode(downloadPojo.fileUri, Constants.ENCODING));
                        downloadPojo.mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl.toLowerCase(Locale.ROOT));
                        downloadPojo.fileExtension = fileExtensionFromUrl;
                        downloadPojo.fileName = new File(Uri.parse(downloadPojo.fileUri).getPath()).getName();
                    }
                    CursorUtils.closeCursorSafely(query2);
                    return downloadPojo;
                }
            } catch (Exception unused) {
                CursorUtils.closeCursorSafely(query2);
                return null;
            } catch (Throwable th) {
                CursorUtils.closeCursorSafely(query2);
                throw th;
            }
        }
        CursorUtils.closeCursorSafely(query2);
        return null;
    }
}
