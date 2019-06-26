package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.ArrayList;

@RequiresApi(21)
class DocumentsContractApi21 {
   private static final String TAG = "DocumentFile";

   private static void closeQuietly(AutoCloseable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }

   }

   public static Uri createDirectory(Context var0, Uri var1, String var2) {
      return createFile(var0, var1, "vnd.android.document/directory", var2);
   }

   public static Uri createFile(Context var0, Uri var1, String var2, String var3) {
      try {
         Uri var5 = DocumentsContract.createDocument(var0.getContentResolver(), var1, var2, var3);
         return var5;
      } catch (Exception var4) {
         return null;
      }
   }

   public static Uri[] listFiles(Context var0, Uri var1) {
      ContentResolver var2 = var0.getContentResolver();
      Uri var3 = DocumentsContract.buildChildDocumentsUriUsingTree(var1, DocumentsContract.getDocumentId(var1));
      ArrayList var4 = new ArrayList();
      StringBuilder var5 = null;
      Cursor var78 = null;

      Cursor var83;
      label667: {
         Throwable var10000;
         label673: {
            Cursor var80;
            Exception var81;
            boolean var10001;
            label660: {
               label659: {
                  try {
                     try {
                        var83 = var2.query(var3, new String[]{"document_id"}, (String)null, (String[])null, (String)null);
                        break label659;
                     } catch (Exception var76) {
                        var81 = var76;
                     }
                  } catch (Throwable var77) {
                     var10000 = var77;
                     var10001 = false;
                     break label673;
                  }

                  var80 = var5;
                  break label660;
               }

               while(true) {
                  Exception var79;
                  try {
                     if (!var83.moveToNext()) {
                        break label667;
                     }

                     var4.add(DocumentsContract.buildDocumentUriUsingTree(var1, var83.getString(0)));
                     continue;
                  } catch (Exception var74) {
                     var79 = var74;
                  } finally {
                     ;
                  }

                  var80 = var83;
                  var81 = var79;
                  break;
               }
            }

            var78 = var80;

            try {
               var5 = new StringBuilder;
            } catch (Throwable var73) {
               var10000 = var73;
               var10001 = false;
               break label673;
            }

            var78 = var80;

            try {
               var5.<init>();
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label673;
            }

            var78 = var80;

            try {
               var5.append("Failed query: ");
            } catch (Throwable var71) {
               var10000 = var71;
               var10001 = false;
               break label673;
            }

            var78 = var80;

            try {
               var5.append(var81);
            } catch (Throwable var70) {
               var10000 = var70;
               var10001 = false;
               break label673;
            }

            var78 = var80;

            try {
               Log.w("DocumentFile", var5.toString());
            } catch (Throwable var69) {
               var10000 = var69;
               var10001 = false;
               break label673;
            }

            closeQuietly(var80);
            return (Uri[])var4.toArray(new Uri[var4.size()]);
         }

         Throwable var82 = var10000;
         closeQuietly(var78);
         throw var82;
      }

      closeQuietly(var83);
      return (Uri[])var4.toArray(new Uri[var4.size()]);
   }

   public static Uri prepareTreeUri(Uri var0) {
      return DocumentsContract.buildDocumentUriUsingTree(var0, DocumentsContract.getTreeDocumentId(var0));
   }

   public static Uri renameTo(Context var0, Uri var1, String var2) {
      try {
         Uri var4 = DocumentsContract.renameDocument(var0.getContentResolver(), var1, var2);
         return var4;
      } catch (Exception var3) {
         return null;
      }
   }
}
