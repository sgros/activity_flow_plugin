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
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.mozilla.focus.components.RelocateService;
import org.mozilla.focus.provider.DownloadContract;
import org.mozilla.focus.utils.CursorUtils;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.rocket.util.LoggerWrapper;
import org.mozilla.threadutils.ThreadUtils;

public class DownloadInfoManager {
   private static Context mContext;
   private static DownloadInfoManager.DownloadInfoQueryHandler mQueryHandler;
   private static DownloadInfoManager sInstance;

   private static DownloadInfo createEmptyDownloadInfo(long var0, long var2, String var4) {
      DownloadInfo var5 = new DownloadInfo();
      var5.setRowId(var2);
      var5.setDownloadId(var0);
      var5.setFileUri(var4);
      var5.setStatusInt(-1);
      return var5;
   }

   private static DownloadInfo cursorToDownloadInfo(Cursor var0) {
      long var1 = var0.getLong(var0.getColumnIndex("download_id"));
      String var3 = var0.getString(var0.getColumnIndex("file_path"));
      Long var5 = var0.getLong(var0.getColumnIndex("_id"));
      DownloadInfoManager.DownloadPojo var4 = queryDownloadManager(mContext, var1);
      DownloadInfo var6;
      if (var4 == null) {
         var6 = createEmptyDownloadInfo(var1, var5, var3);
      } else {
         var6 = pojoToDownloadInfo(var4, var3, var5);
      }

      return var6;
   }

   private static ContentValues getContentValuesFromDownloadInfo(DownloadInfo var0) {
      ContentValues var1 = new ContentValues();
      var1.put("download_id", var0.getDownloadId());
      var1.put("file_path", var0.getFileUri());
      var1.put("status", var0.getStatus());
      var1.put("is_read", var0.isRead());
      return var1;
   }

   public static DownloadInfoManager getInstance() {
      if (sInstance == null) {
         sInstance = new DownloadInfoManager();
      }

      return sInstance;
   }

   public static void init(Context var0) {
      mContext = var0;
      mQueryHandler = new DownloadInfoManager.DownloadInfoQueryHandler(var0);
   }

   // $FF: synthetic method
   static void lambda$null$0(View var0, DownloadInfo var1, View var2) {
      IntentUtils.intentOpenFile(var0.getContext(), var1.getFileUri(), var1.getMimeType());
   }

   // $FF: synthetic method
   static void lambda$showOpenDownloadSnackBar$1(String var0, View var1, List var2) {
      boolean var3;
      if (var2.size() > 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (!var3) {
         LoggerWrapper.throwOrWarn(var0, "Download Completed with unknown local row id");
      } else {
         DownloadInfo var6 = (DownloadInfo)var2.get(0);
         boolean var4 = var6.existInDownloadManager();
         if (!var4) {
            LoggerWrapper.throwOrWarn(var0, "Download Completed with unknown DownloadManager id");
         }

         Snackbar var5 = Snackbar.make(var1, var1.getContext().getString(2131755104, new Object[]{var6.getFileName()}), 0);
         if (var4) {
            var5.setAction(2131755279, new _$$Lambda$DownloadInfoManager$PzoHQaka_eea0cAHebO641vAq_U(var1, var6));
         }

         var5.show();
      }
   }

   public static void notifyRowUpdated(Context var0, long var1) {
      Intent var3 = new Intent("row_updated");
      var3.putExtra("row id", var1);
      LocalBroadcastManager.getInstance(var0).sendBroadcast(var3);
   }

   private static DownloadInfo pojoToDownloadInfo(DownloadInfoManager.DownloadPojo var0, String var1, long var2) {
      DownloadInfo var4 = new DownloadInfo();
      var4.setRowId(var2);
      var4.setFileName(var0.fileName);
      var4.setDownloadId(var0.downloadId);
      var4.setSize(var0.length);
      var4.setSizeTotal(var0.length);
      var4.setSizeSoFar(var0.sizeSoFar);
      var4.setStatusInt(var0.status);
      var4.setDate(var0.timeStamp);
      var4.setMediaUri(var0.mediaUri);
      var4.setFileUri(var0.fileUri);
      var4.setMimeType(var0.mime);
      var4.setFileExtension(var0.fileExtension);
      if (TextUtils.isEmpty(var0.fileUri)) {
         var4.setFileUri(var1);
      } else {
         var4.setFileUri(var0.fileUri);
      }

      return var4;
   }

   private static DownloadInfoManager.DownloadPojo queryDownloadManager(Context var0, long var1) {
      Query var3 = new Query();
      var3.setFilterById(new long[]{var1});
      Cursor var9 = ((DownloadManager)var0.getSystemService("download")).query(var3);
      DownloadInfoManager.DownloadPojo var10 = new DownloadInfoManager.DownloadPojo();
      var10.downloadId = var1;
      if (var9 != null) {
         try {
            if (!var9.moveToFirst()) {
               return null;
            }

            var10.desc = var9.getString(var9.getColumnIndex("description"));
            var10.status = var9.getInt(var9.getColumnIndex("status"));
            var10.length = var9.getDouble(var9.getColumnIndex("total_size"));
            var10.sizeSoFar = var9.getDouble(var9.getColumnIndex("bytes_so_far"));
            var10.timeStamp = var9.getLong(var9.getColumnIndex("last_modified_timestamp"));
            var10.mediaUri = var9.getString(var9.getColumnIndex("mediaprovider_uri"));
            var10.fileUri = var9.getString(var9.getColumnIndex("local_uri"));
            if (var10.fileUri != null) {
               String var4 = MimeTypeMap.getFileExtensionFromUrl(URLEncoder.encode(var10.fileUri, "UTF-8"));
               var10.mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var4.toLowerCase(Locale.ROOT));
               var10.fileExtension = var4;
               File var11 = new File(Uri.parse(var10.fileUri).getPath());
               var10.fileName = var11.getName();
            }
         } catch (Exception var7) {
            return null;
         } finally {
            CursorUtils.closeCursorSafely(var9);
         }

         return var10;
      } else {
         return null;
      }
   }

   public void delete(Long var1, DownloadInfoManager.AsyncDeleteListener var2) {
      mQueryHandler.startDelete(2, new DownloadInfoManager.AsyncDeleteWrapper(var1, var2), DownloadContract.Download.CONTENT_URI, "_id = ?", new String[]{Long.toString(var1)});
   }

   public DownloadManager getDownloadManager() {
      return (DownloadManager)mContext.getSystemService("download");
   }

   public void insert(DownloadInfo var1, DownloadInfoManager.AsyncInsertListener var2) {
      mQueryHandler.startInsert(2, var2, DownloadContract.Download.CONTENT_URI, getContentValuesFromDownloadInfo(var1));
   }

   public void markAllItemsAreRead(DownloadInfoManager.AsyncUpdateListener var1) {
      ContentValues var2 = new ContentValues();
      var2.put("is_read", "1");
      mQueryHandler.startUpdate(2, var1, DownloadContract.Download.CONTENT_URI, var2, "status=? and is_read = ?", new String[]{String.valueOf(8), String.valueOf("0")});
   }

   public void query(int var1, int var2, DownloadInfoManager.AsyncQueryListener var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(DownloadContract.Download.CONTENT_URI.toString());
      var4.append("?offset=");
      var4.append(var1);
      var4.append("&limit=");
      var4.append(var2);
      String var5 = var4.toString();
      mQueryHandler.startQuery(2, var3, Uri.parse(var5), (String[])null, (String)null, (String[])null, "_id DESC");
   }

   public void queryByDownloadId(Long var1, DownloadInfoManager.AsyncQueryListener var2) {
      String var3 = DownloadContract.Download.CONTENT_URI.toString();
      mQueryHandler.startQuery(2, var2, Uri.parse(var3), (String[])null, "download_id==?", new String[]{Long.toString(var1)}, (String)null);
   }

   public void queryByRowId(Long var1, DownloadInfoManager.AsyncQueryListener var2) {
      String var3 = DownloadContract.Download.CONTENT_URI.toString();
      mQueryHandler.startQuery(2, var2, Uri.parse(var3), (String[])null, "_id==?", new String[]{Long.toString(var1)}, (String)null);
   }

   public void queryDownloadingAndUnreadIds(DownloadInfoManager.AsyncQueryListener var1) {
      String var2 = DownloadContract.Download.CONTENT_URI.toString();
      mQueryHandler.startQuery(2, var1, Uri.parse(var2), (String[])null, "status!=? or is_read=?", new String[]{String.valueOf(8), String.valueOf("0")}, (String)null);
   }

   public boolean recordExists(long var1) {
      ContentResolver var3 = mContext.getContentResolver();
      Uri var4 = DownloadContract.Download.CONTENT_URI;
      StringBuilder var5 = new StringBuilder();
      var5.append("download_id=");
      var5.append(var1);
      Cursor var14 = var3.query(var4, (String[])null, var5.toString(), (String[])null, (String)null);
      boolean var6 = false;
      boolean var7 = var6;
      if (var14 != null) {
         label65: {
            var7 = var6;
            boolean var11 = false;

            boolean var8;
            label60: {
               try {
                  var11 = true;
                  if (var14.getCount() <= 0) {
                     var11 = false;
                     break label65;
                  }

                  var8 = var14.moveToFirst();
                  var11 = false;
                  break label60;
               } catch (Exception var12) {
                  var12.printStackTrace();
                  var11 = false;
               } finally {
                  if (var11) {
                     CursorUtils.closeCursorSafely(var14);
                  }
               }

               var7 = var6;
               break label65;
            }

            var7 = var6;
            if (var8) {
               var7 = true;
            }
         }
      }

      CursorUtils.closeCursorSafely(var14);
      return var7;
   }

   public void replacePath(final long var1, final String var3, String var4) {
      File var5 = new File(var3);
      final DownloadInfoManager.DownloadPojo var6 = queryDownloadManager(mContext, var1);
      if (var6 != null) {
         final DownloadManager var7 = (DownloadManager)mContext.getSystemService("download");
         String var8;
         if (TextUtils.isEmpty(var6.desc)) {
            var8 = "Downloaded from internet";
         } else {
            var8 = var6.desc;
         }

         if (TextUtils.isEmpty(var6.mime)) {
            if (TextUtils.isEmpty(var4)) {
               var4 = "*/*";
            }
         } else {
            var4 = var6.mime;
         }

         this.queryByDownloadId(var1, new DownloadInfoManager.AsyncQueryListener(var7.addCompletedDownload(var5.getName(), var8, true, var4, var3, var5.length(), true)) {
            // $FF: synthetic field
            final long val$newId;

            {
               this.val$newId = var6x;
            }

            public void onQueryComplete(List var1x) {
               for(int var2 = 0; var2 < var1x.size(); ++var2) {
                  DownloadInfo var3x = (DownloadInfo)var1x.get(var2);
                  if (!var3x.existInDownloadManager()) {
                     throw new IllegalStateException("File entry disappeared after being moved");
                  }

                  if (var1 == var3x.getDownloadId()) {
                     final long var4 = var3x.getRowId();
                     DownloadInfo var6x = DownloadInfoManager.pojoToDownloadInfo(var6, var3, var4);
                     var6x.setDownloadId(this.val$newId);
                     DownloadInfoManager.this.updateByRowId(var6x, new DownloadInfoManager.AsyncUpdateListener() {
                        public void onUpdateComplete(int var1x) {
                           DownloadInfoManager.notifyRowUpdated(DownloadInfoManager.mContext, var4);
                           RelocateService.broadcastRelocateFinished(DownloadInfoManager.mContext, var4);
                        }
                     });
                     var7.remove(new long[]{var1});
                     break;
                  }
               }

            }
         });
      } else {
         throw new IllegalStateException("File entry disappeared after being moved");
      }
   }

   public void showOpenDownloadSnackBar(Long var1, View var2, String var3) {
      this.queryByRowId(var1, new _$$Lambda$DownloadInfoManager$kRTO87mBRttGY83Sgc9tpXiKrPA(var3, var2));
   }

   public void updateByRowId(DownloadInfo var1, DownloadInfoManager.AsyncUpdateListener var2) {
      mQueryHandler.startUpdate(2, var2, DownloadContract.Download.CONTENT_URI, getContentValuesFromDownloadInfo(var1), "_id = ?", new String[]{Long.toString(var1.getRowId())});
   }

   public interface AsyncDeleteListener {
      void onDeleteComplete(int var1, long var2);
   }

   private static final class AsyncDeleteWrapper {
      public long id;
      public DownloadInfoManager.AsyncDeleteListener listener;

      public AsyncDeleteWrapper(long var1, DownloadInfoManager.AsyncDeleteListener var3) {
         this.id = var1;
         this.listener = var3;
      }
   }

   public interface AsyncInsertListener {
      void onInsertComplete(long var1);
   }

   public interface AsyncQueryListener {
      void onQueryComplete(List var1);
   }

   public interface AsyncUpdateListener {
      void onUpdateComplete(int var1);
   }

   private static final class DownloadInfoQueryHandler extends AsyncQueryHandler {
      public DownloadInfoQueryHandler(Context var1) {
         super(var1.getContentResolver());
      }

      protected void onDeleteComplete(int var1, Object var2, int var3) {
         if (var1 == 2 && var2 != null) {
            DownloadInfoManager.AsyncDeleteWrapper var4 = (DownloadInfoManager.AsyncDeleteWrapper)var2;
            if (var4.listener != null) {
               var4.listener.onDeleteComplete(var3, var4.id);
            }
         }

      }

      protected void onInsertComplete(int var1, Object var2, Uri var3) {
         if (var1 == 2 && var2 != null) {
            long var4;
            if (var3 == null) {
               var4 = -1L;
            } else {
               var4 = Long.parseLong(var3.getLastPathSegment());
            }

            ((DownloadInfoManager.AsyncInsertListener)var2).onInsertComplete(var4);
         }

      }

      protected void onQueryComplete(int var1, final Object var2, final Cursor var3) {
         if (var1 != 2) {
            CursorUtils.closeCursorSafely(var3);
         } else {
            ThreadUtils.postToBackgroundThread(new Runnable() {
               public void run() {
                  if (var2 != null) {
                     final ArrayList var1 = new ArrayList();
                     if (var3 != null) {
                        while(true) {
                           boolean var5 = false;

                           try {
                              var5 = true;
                              if (!var3.moveToNext()) {
                                 var5 = false;
                                 break;
                              }

                              var1.add(DownloadInfoManager.cursorToDownloadInfo(var3));
                              var5 = false;
                           } catch (Exception var6) {
                              var6.printStackTrace();
                              var5 = false;
                              break;
                           } finally {
                              if (var5) {
                                 CursorUtils.closeCursorSafely(var3);
                              }
                           }
                        }

                        CursorUtils.closeCursorSafely(var3);
                     }

                     ThreadUtils.postToMainThread(new Runnable() {
                        public void run() {
                           ((DownloadInfoManager.AsyncQueryListener)var2).onQueryComplete(var1);
                        }
                     });
                  } else {
                     CursorUtils.closeCursorSafely(var3);
                  }

               }
            });
         }

      }

      protected void onUpdateComplete(int var1, Object var2, int var3) {
         if (var1 == 2 && var2 != null) {
            ((DownloadInfoManager.AsyncUpdateListener)var2).onUpdateComplete(var3);
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

      // $FF: synthetic method
      DownloadPojo(Object var1) {
         this();
      }
   }
}
