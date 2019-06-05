package org.mozilla.rocket.download;

import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.threadutils.ThreadUtils;

public final class DownloadInfoRepository {
   public static final DownloadInfoRepository.Companion Companion = new DownloadInfoRepository.Companion((DefaultConstructorMarker)null);
   private static volatile DownloadInfoRepository INSTANCE;

   public static final DownloadInfoRepository getInstance() {
      return Companion.getInstance();
   }

   public final void deleteFromDownloadManager(long var1) {
      DownloadInfoManager var3 = DownloadInfoManager.getInstance();
      Intrinsics.checkExpressionValueIsNotNull(var3, "DownloadInfoManager.getInstance()");
      var3.getDownloadManager().remove(new long[]{var1});
   }

   public final void loadData(int var1, int var2, final DownloadInfoRepository.OnQueryListCompleteListener var3) {
      Intrinsics.checkParameterIsNotNull(var3, "listenerList");
      DownloadInfoManager.getInstance().query(var1, var2, (DownloadInfoManager.AsyncQueryListener)(new DownloadInfoManager.AsyncQueryListener() {
         public final void onQueryComplete(List var1) {
            DownloadInfoRepository.OnQueryListCompleteListener var2 = var3;
            Intrinsics.checkExpressionValueIsNotNull(var1, "downloadInfoList");
            var2.onComplete(var1);
         }
      }));
   }

   public final void markAllItemsAreRead() {
      DownloadInfoManager.getInstance().markAllItemsAreRead((DownloadInfoManager.AsyncUpdateListener)null);
   }

   public final void queryByDownloadId(long var1, final DownloadInfoRepository.OnQueryItemCompleteListener var3) {
      Intrinsics.checkParameterIsNotNull(var3, "listenerItem");
      DownloadInfoManager.getInstance().queryByDownloadId(var1, (DownloadInfoManager.AsyncQueryListener)(new DownloadInfoManager.AsyncQueryListener() {
         public final void onQueryComplete(List var1) {
            if (var1.size() > 0) {
               DownloadInfo var3x = (DownloadInfo)var1.get(0);
               DownloadInfoRepository.OnQueryItemCompleteListener var2 = var3;
               Intrinsics.checkExpressionValueIsNotNull(var3x, "downloadInfo");
               var2.onComplete(var3x);
            }

         }
      }));
   }

   public final void queryByRowId(long var1, final DownloadInfoRepository.OnQueryItemCompleteListener var3) {
      Intrinsics.checkParameterIsNotNull(var3, "listenerItem");
      DownloadInfoManager.getInstance().queryByRowId(var1, (DownloadInfoManager.AsyncQueryListener)(new DownloadInfoManager.AsyncQueryListener() {
         public final void onQueryComplete(List var1) {
            if (var1.size() > 0) {
               DownloadInfo var3x = (DownloadInfo)var1.get(0);
               DownloadInfoRepository.OnQueryItemCompleteListener var2 = var3;
               Intrinsics.checkExpressionValueIsNotNull(var3x, "downloadInfo");
               var2.onComplete(var3x);
            }

         }
      }));
   }

   public final void queryDownloadingItems(final long[] var1, final DownloadInfoRepository.OnQueryListCompleteListener var2) {
      Intrinsics.checkParameterIsNotNull(var1, "runningIds");
      Intrinsics.checkParameterIsNotNull(var2, "listenerList");
      ThreadUtils.postToBackgroundThread((Runnable)(new Runnable() {
         public final void run() {
            // $FF: Couldn't be decompiled
         }
      }));
   }

   public final void queryIndicatorStatus(final DownloadInfoRepository.OnQueryListCompleteListener var1) {
      Intrinsics.checkParameterIsNotNull(var1, "listenerList");
      DownloadInfoManager.getInstance().queryDownloadingAndUnreadIds((DownloadInfoManager.AsyncQueryListener)(new DownloadInfoManager.AsyncQueryListener() {
         public final void onQueryComplete(List var1x) {
            DownloadInfoRepository.OnQueryListCompleteListener var2 = var1;
            Intrinsics.checkExpressionValueIsNotNull(var1x, "downloadInfoList");
            var2.onComplete(var1x);
         }
      }));
   }

   public final void remove(long var1) {
      DownloadInfoManager.getInstance().delete(var1, (DownloadInfoManager.AsyncDeleteListener)null);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final DownloadInfoRepository getInstance() {
         DownloadInfoRepository var1 = DownloadInfoRepository.INSTANCE;
         if (var1 == null) {
            synchronized(this){}

            Throwable var10000;
            label98: {
               boolean var10001;
               try {
                  var1 = DownloadInfoRepository.INSTANCE;
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label98;
               }

               if (var1 != null) {
                  return var1;
               }

               label87:
               try {
                  var1 = new DownloadInfoRepository();
                  DownloadInfoRepository.INSTANCE = var1;
                  return var1;
               } catch (Throwable var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label87;
               }
            }

            Throwable var8 = var10000;
            throw var8;
         } else {
            return var1;
         }
      }
   }

   public interface OnQueryItemCompleteListener {
      void onComplete(DownloadInfo var1);
   }

   public interface OnQueryListCompleteListener {
      void onComplete(List var1);
   }
}
