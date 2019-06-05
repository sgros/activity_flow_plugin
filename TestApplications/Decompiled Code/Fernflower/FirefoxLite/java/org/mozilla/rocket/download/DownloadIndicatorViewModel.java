package org.mozilla.rocket.download;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;

public final class DownloadIndicatorViewModel extends ViewModel {
   private final MutableLiveData downloadIndicatorObservable;
   private final DownloadInfoRepository repository;

   public DownloadIndicatorViewModel(DownloadInfoRepository var1) {
      Intrinsics.checkParameterIsNotNull(var1, "repository");
      super();
      this.repository = var1;
      this.downloadIndicatorObservable = new MutableLiveData();
   }

   public final MutableLiveData getDownloadIndicatorObservable() {
      return this.downloadIndicatorObservable;
   }

   public final void updateIndicator() {
      this.repository.queryIndicatorStatus((DownloadInfoRepository.OnQueryListCompleteListener)(new DownloadInfoRepository.OnQueryListCompleteListener() {
         public void onComplete(List var1) {
            Intrinsics.checkParameterIsNotNull(var1, "list");
            Iterator var8 = var1.iterator();
            boolean var2 = false;
            boolean var3 = false;
            boolean var4 = false;

            while(true) {
               DownloadInfo var5;
               boolean var6;
               boolean var7;
               do {
                  do {
                     if (!var8.hasNext()) {
                        MutableLiveData var10 = DownloadIndicatorViewModel.this.getDownloadIndicatorObservable();
                        DownloadIndicatorViewModel.Status var9;
                        if (var2) {
                           var9 = DownloadIndicatorViewModel.Status.DOWNLOADING;
                        } else if (var3) {
                           var9 = DownloadIndicatorViewModel.Status.UNREAD;
                        } else if (var4) {
                           var9 = DownloadIndicatorViewModel.Status.WARNING;
                        } else {
                           var9 = DownloadIndicatorViewModel.Status.DEFAULT;
                        }

                        var10.setValue(var9);
                        return;
                     }

                     var5 = (DownloadInfo)var8.next();
                     var6 = var2;
                     if (!var2) {
                        label58: {
                           if (var5.getStatus() != 2) {
                              var6 = var2;
                              if (var5.getStatus() != 1) {
                                 break label58;
                              }
                           }

                           var6 = true;
                        }
                     }

                     var7 = var3;
                     if (!var3) {
                        var7 = var3;
                        if (var5.getStatus() == 8) {
                           var7 = var3;
                           if (!var5.isRead()) {
                              var7 = true;
                           }
                        }
                     }

                     var2 = var6;
                     var3 = var7;
                  } while(var4);

                  if (var5.getStatus() == 4) {
                     break;
                  }

                  var2 = var6;
                  var3 = var7;
               } while(var5.getStatus() != 16);

               var4 = true;
               var2 = var6;
               var3 = var7;
            }
         }
      }));
   }

   public static enum Status {
      DEFAULT,
      DOWNLOADING,
      UNREAD,
      WARNING;

      static {
         DownloadIndicatorViewModel.Status var0 = new DownloadIndicatorViewModel.Status("DEFAULT", 0);
         DEFAULT = var0;
         DownloadIndicatorViewModel.Status var1 = new DownloadIndicatorViewModel.Status("DOWNLOADING", 1);
         DOWNLOADING = var1;
         DownloadIndicatorViewModel.Status var2 = new DownloadIndicatorViewModel.Status("UNREAD", 2);
         UNREAD = var2;
         DownloadIndicatorViewModel.Status var3 = new DownloadIndicatorViewModel.Status("WARNING", 3);
         WARNING = var3;
      }
   }
}
