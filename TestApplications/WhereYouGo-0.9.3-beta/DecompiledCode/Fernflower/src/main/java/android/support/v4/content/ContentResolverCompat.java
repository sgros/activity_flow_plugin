package android.support.v4.content;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;

public final class ContentResolverCompat {
   private static final ContentResolverCompat.ContentResolverCompatImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 16) {
         IMPL = new ContentResolverCompat.ContentResolverCompatImplJB();
      } else {
         IMPL = new ContentResolverCompat.ContentResolverCompatImplBase();
      }

   }

   private ContentResolverCompat() {
   }

   public static Cursor query(ContentResolver var0, Uri var1, String[] var2, String var3, String[] var4, String var5, CancellationSignal var6) {
      return IMPL.query(var0, var1, var2, var3, var4, var5, var6);
   }

   interface ContentResolverCompatImpl {
      Cursor query(ContentResolver var1, Uri var2, String[] var3, String var4, String[] var5, String var6, CancellationSignal var7);
   }

   static class ContentResolverCompatImplBase implements ContentResolverCompat.ContentResolverCompatImpl {
      public Cursor query(ContentResolver var1, Uri var2, String[] var3, String var4, String[] var5, String var6, CancellationSignal var7) {
         if (var7 != null) {
            var7.throwIfCanceled();
         }

         return var1.query(var2, var3, var4, var5, var6);
      }
   }

   static class ContentResolverCompatImplJB extends ContentResolverCompat.ContentResolverCompatImplBase {
      public Cursor query(ContentResolver var1, Uri var2, String[] var3, String var4, String[] var5, String var6, CancellationSignal var7) {
         Exception var10000;
         label24: {
            Object var12;
            boolean var10001;
            if (var7 != null) {
               try {
                  var12 = var7.getCancellationSignalObject();
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label24;
               }
            } else {
               var12 = null;
            }

            try {
               Cursor var11 = ContentResolverCompatJellybean.query(var1, var2, var3, var4, var5, var6, var12);
               return var11;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         Exception var10 = var10000;
         if (ContentResolverCompatJellybean.isFrameworkOperationCanceledException(var10)) {
            throw new OperationCanceledException();
         } else {
            throw var10;
         }
      }
   }
}
