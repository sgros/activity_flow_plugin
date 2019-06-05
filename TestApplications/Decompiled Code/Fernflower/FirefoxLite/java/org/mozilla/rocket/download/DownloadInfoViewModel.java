package org.mozilla.rocket.download;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.threadutils.ThreadUtils;

public final class DownloadInfoViewModel extends ViewModel {
   public static final DownloadInfoViewModel.Companion Companion = new DownloadInfoViewModel.Companion((DefaultConstructorMarker)null);
   private final SingleLiveEvent deleteSnackbarObservable;
   private final MutableLiveData downloadInfoObservable;
   private final DownloadInfoPack downloadInfoPack;
   private boolean isLastPage;
   private boolean isLoading;
   private boolean isOpening;
   private int itemCount;
   private DownloadInfoViewModel.OnProgressUpdateListener progressUpdateListener;
   private final DownloadInfoRepository repository;
   private final SingleLiveEvent toastMessageObservable;
   private DownloadInfoRepository.OnQueryItemCompleteListener updateListener;

   public DownloadInfoViewModel(DownloadInfoRepository var1) {
      Intrinsics.checkParameterIsNotNull(var1, "repository");
      super();
      this.repository = var1;
      this.downloadInfoObservable = new MutableLiveData();
      this.toastMessageObservable = new SingleLiveEvent();
      this.deleteSnackbarObservable = new SingleLiveEvent();
      this.downloadInfoPack = new DownloadInfoPack(new ArrayList(), -1, -1L);
      this.updateListener = (DownloadInfoRepository.OnQueryItemCompleteListener)(new DownloadInfoRepository.OnQueryItemCompleteListener() {
         public void onComplete(DownloadInfo var1) {
            Intrinsics.checkParameterIsNotNull(var1, "download");
            DownloadInfoViewModel.this.updateItem(var1);
         }
      });
   }

   private final long[] getRunningDownloadIds() {
      Iterable var1 = (Iterable)this.downloadInfoPack.getList();
      Collection var2 = (Collection)(new ArrayList());
      Iterator var3 = var1.iterator();

      while(true) {
         boolean var4 = var3.hasNext();
         int var5 = 0;
         boolean var6 = false;
         if (!var4) {
            var1 = (Iterable)((List)var2);
            var2 = (Collection)(new ArrayList(CollectionsKt.collectionSizeOrDefault(var1, 10)));
            Iterator var9 = var1.iterator();

            while(var9.hasNext()) {
               var2.add(((DownloadInfo)var9.next()).getDownloadId());
            }

            List var10 = (List)var2;
            long[] var14 = new long[var10.size()];

            for(int var13 = var14.length; var5 < var13; ++var5) {
               Object var11 = var10.get(var5);
               Intrinsics.checkExpressionValueIsNotNull(var11, "ids[i]");
               var14[var5] = ((Number)var11).longValue();
            }

            return var14;
         }

         Object var7;
         boolean var12;
         label35: {
            var7 = var3.next();
            DownloadInfo var8 = (DownloadInfo)var7;
            if (var8.getStatus() != 2) {
               var12 = var6;
               if (var8.getStatus() != 1) {
                  break label35;
               }
            }

            var12 = true;
         }

         if (var12) {
            var2.add(var7);
         }
      }
   }

   private final boolean isDownloading() {
      Iterable var1 = (Iterable)this.downloadInfoPack.getList();
      boolean var2 = var1 instanceof Collection;
      boolean var3 = false;
      if (var2 && ((Collection)var1).isEmpty()) {
         var2 = var3;
      } else {
         Iterator var6 = var1.iterator();

         while(true) {
            var2 = var3;
            if (!var6.hasNext()) {
               break;
            }

            DownloadInfo var4 = (DownloadInfo)var6.next();
            boolean var5;
            if (var4.getStatus() != 2 && var4.getStatus() != 1) {
               var5 = false;
            } else {
               var5 = true;
            }

            if (var5) {
               var2 = true;
               break;
            }
         }
      }

      return var2;
   }

   private final void updateItem(DownloadInfo var1) {
      int var2 = this.downloadInfoPack.getList().size();
      int var3 = 0;

      while(true) {
         if (var3 >= var2) {
            var3 = -1;
            break;
         }

         Object var4 = this.downloadInfoPack.getList().get(var3);
         Intrinsics.checkExpressionValueIsNotNull(var4, "downloadInfoPack.list[i]");
         if (Intrinsics.areEqual(((DownloadInfo)var4).getRowId(), var1.getRowId())) {
            break;
         }

         ++var3;
      }

      if (var3 == -1) {
         this.downloadInfoPack.getList().add(0, var1);
      } else {
         this.downloadInfoPack.getList().remove(var3);
         this.downloadInfoPack.getList().add(var3, var1);
      }

      this.downloadInfoPack.setNotifyType(1);
      this.downloadInfoObservable.setValue(this.downloadInfoPack);
   }

   private final void updateRunningItems() {
      int var1 = this.getRunningDownloadIds().length;
      byte var2 = 0;
      boolean var4;
      if (var1 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (!var4) {
         int var3 = this.getRunningDownloadIds().length;

         for(var1 = var2; var1 < var3; ++var1) {
            this.repository.queryByDownloadId(this.getRunningDownloadIds()[var1], (DownloadInfoRepository.OnQueryItemCompleteListener)(new DownloadInfoRepository.OnQueryItemCompleteListener() {
               public void onComplete(DownloadInfo var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "download");
                  int var2 = DownloadInfoViewModel.this.downloadInfoPack.getList().size();

                  for(int var3 = 0; var3 < var2; ++var3) {
                     Object var4 = DownloadInfoViewModel.this.downloadInfoPack.getList().get(var3);
                     Intrinsics.checkExpressionValueIsNotNull(var4, "downloadInfoPack.list[j]");
                     DownloadInfo var5 = (DownloadInfo)var4;
                     if (Intrinsics.areEqual(var1.getDownloadId(), var5.getDownloadId())) {
                        var5.setStatusInt(var1.getStatus());
                        DownloadInfoViewModel.this.downloadInfoPack.setNotifyType(4);
                        DownloadInfoViewModel.this.downloadInfoPack.setIndex((long)var3);
                        DownloadInfoViewModel.this.getDownloadInfoObservable().setValue(DownloadInfoViewModel.this.downloadInfoPack);
                     }
                  }

               }
            }));
         }
      }

   }

   public final void add(DownloadInfo var1) {
      Intrinsics.checkParameterIsNotNull(var1, "downloadInfo");
      int var2 = this.downloadInfoPack.getList().size();
      int var3 = 0;

      while(true) {
         if (var3 >= var2) {
            var3 = -1;
            break;
         }

         Object var4 = this.downloadInfoPack.getList().get(var3);
         Intrinsics.checkExpressionValueIsNotNull(var4, "downloadInfoPack.list[i]");
         long var5 = ((DownloadInfo)var4).getRowId();
         Long var7 = var1.getRowId();
         Intrinsics.checkExpressionValueIsNotNull(var7, "downloadInfo.rowId");
         if (var5 < var7) {
            break;
         }

         ++var3;
      }

      if (var3 == -1) {
         this.downloadInfoPack.getList().add(var1);
         if (this.downloadInfoPack.getList().size() > 1) {
            this.downloadInfoPack.setNotifyType(2);
            this.downloadInfoPack.setIndex((long)(this.downloadInfoPack.getList().size() - 1));
         } else {
            this.downloadInfoPack.setNotifyType(1);
         }
      } else {
         this.downloadInfoPack.getList().add(var3, var1);
         this.downloadInfoPack.setNotifyType(2);
         this.downloadInfoPack.setIndex((long)var3);
      }

      this.downloadInfoObservable.setValue(this.downloadInfoPack);
   }

   public final void cancel(final long var1) {
      this.repository.queryByRowId(var1, (DownloadInfoRepository.OnQueryItemCompleteListener)(new DownloadInfoRepository.OnQueryItemCompleteListener() {
         public void onComplete(DownloadInfo var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "download");
            if (var1x.existInDownloadManager()) {
               long var2 = var1;
               Long var4 = var1x.getRowId();
               if (var4 != null && var2 == var4 && 8 != var1x.getStatus()) {
                  DownloadInfoViewModel.this.getToastMessageObservable().setValue(2131755103);
                  DownloadInfoRepository var6 = DownloadInfoViewModel.this.repository;
                  Long var5 = var1x.getDownloadId();
                  Intrinsics.checkExpressionValueIsNotNull(var5, "download.downloadId");
                  var6.deleteFromDownloadManager(var5);
                  DownloadInfoViewModel.this.remove(var1);
               }
            }

         }
      }));
   }

   public final void confirmDelete(DownloadInfo var1) {
      Intrinsics.checkParameterIsNotNull(var1, "download");
      URI var2 = URI.create(var1.getFileUri());
      Intrinsics.checkExpressionValueIsNotNull(var2, "URI.create(download.fileUri)");
      File var7 = new File(var2.getPath());

      try {
         if (var7.delete()) {
            DownloadInfoRepository var8 = this.repository;
            Long var9 = var1.getDownloadId();
            Intrinsics.checkExpressionValueIsNotNull(var9, "download.downloadId");
            var8.deleteFromDownloadManager(var9);
            var8 = this.repository;
            Long var6 = var1.getRowId();
            Intrinsics.checkExpressionValueIsNotNull(var6, "download.rowId");
            var8.remove(var6);
         } else {
            this.toastMessageObservable.setValue(2131755081);
         }
      } catch (Exception var4) {
         String var5 = this.getClass().getSimpleName();
         StringBuilder var3 = new StringBuilder();
         var3.append("");
         var3.append(var4.getMessage());
         Log.e(var5, var3.toString());
         this.toastMessageObservable.setValue(2131755081);
      }

   }

   public final void delete(long var1) {
      this.repository.queryByRowId(var1, (DownloadInfoRepository.OnQueryItemCompleteListener)(new DownloadInfoRepository.OnQueryItemCompleteListener() {
         public void onComplete(DownloadInfo var1) {
            Intrinsics.checkParameterIsNotNull(var1, "download");
            URI var2 = URI.create(var1.getFileUri());
            Intrinsics.checkExpressionValueIsNotNull(var2, "URI.create(download.fileUri)");
            if ((new File(var2.getPath())).exists()) {
               DownloadInfoViewModel.this.getDeleteSnackbarObservable().setValue(var1);
            } else {
               DownloadInfoViewModel.this.getToastMessageObservable().setValue(2131755082);
            }

         }
      }));
   }

   public final SingleLiveEvent getDeleteSnackbarObservable() {
      return this.deleteSnackbarObservable;
   }

   public final MutableLiveData getDownloadInfoObservable() {
      return this.downloadInfoObservable;
   }

   public final SingleLiveEvent getToastMessageObservable() {
      return this.toastMessageObservable;
   }

   public final void hide(long var1) {
      int var3 = this.downloadInfoPack.getList().size();

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = this.downloadInfoPack.getList().get(var4);
         Intrinsics.checkExpressionValueIsNotNull(var5, "downloadInfoPack.list[i]");
         DownloadInfo var6 = (DownloadInfo)var5;
         Long var7 = var6.getRowId();
         if (var7 != null && var1 == var7) {
            this.downloadInfoPack.getList().remove(var6);
            this.downloadInfoPack.setNotifyType(3);
            this.downloadInfoPack.setIndex((long)var4);
            this.downloadInfoObservable.setValue(this.downloadInfoPack);
            break;
         }
      }

   }

   public final boolean isOpening() {
      return this.isOpening;
   }

   public final void loadMore(boolean var1) {
      if (!this.isLoading) {
         this.isLoading = true;
         if (var1) {
            this.isLastPage = false;
            this.isOpening = false;
            this.itemCount = 0;
            this.downloadInfoPack.getList().clear();
         }

         if (this.isLastPage) {
            this.isLoading = false;
         } else {
            this.repository.loadData(this.itemCount, 20, (DownloadInfoRepository.OnQueryListCompleteListener)(new DownloadInfoRepository.OnQueryListCompleteListener() {
               public void onComplete(List var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "list");
                  DownloadInfoViewModel.this.downloadInfoPack.getList().addAll((Collection)var1);
                  DownloadInfoViewModel.this.downloadInfoPack.setNotifyType(1);
                  DownloadInfoViewModel.this.itemCount = DownloadInfoViewModel.this.downloadInfoPack.getList().size();
                  DownloadInfoViewModel.this.getDownloadInfoObservable().setValue(DownloadInfoViewModel.this.downloadInfoPack);
                  DownloadInfoViewModel.this.setOpening(false);
                  DownloadInfoViewModel.this.isLoading = false;
                  DownloadInfoViewModel.this.isLastPage = var1.isEmpty();
                  if (DownloadInfoViewModel.this.isDownloading()) {
                     DownloadInfoViewModel.OnProgressUpdateListener var2 = DownloadInfoViewModel.this.progressUpdateListener;
                     if (var2 != null) {
                        var2.onStartUpdate();
                     }
                  }

               }
            }));
         }
      }
   }

   public final void markAllItemsAreRead() {
      this.repository.markAllItemsAreRead();
   }

   public final void notifyDownloadComplete(long var1) {
      this.repository.queryByDownloadId(var1, this.updateListener);
   }

   public final void notifyRowUpdate(long var1) {
      this.repository.queryByRowId(var1, this.updateListener);
   }

   public final void queryDownloadProgress() {
      this.repository.queryDownloadingItems(this.getRunningDownloadIds(), (DownloadInfoRepository.OnQueryListCompleteListener)(new DownloadInfoRepository.OnQueryListCompleteListener() {
         public void onComplete(final List var1) {
            Intrinsics.checkParameterIsNotNull(var1, "list");
            DownloadInfoViewModel.OnProgressUpdateListener var2;
            if (((Collection)var1).isEmpty() ^ true) {
               ThreadUtils.postToMainThread((Runnable)(new Runnable() {
                  public final void run() {
                     Iterator var1x = var1.iterator();

                     while(true) {
                        while(var1x.hasNext()) {
                           DownloadInfo var2 = (DownloadInfo)var1x.next();
                           int var3 = 0;

                           for(int var4 = DownloadInfoViewModel.this.downloadInfoPack.getList().size(); var3 < var4; ++var3) {
                              Object var5 = DownloadInfoViewModel.this.downloadInfoPack.getList().get(var3);
                              Intrinsics.checkExpressionValueIsNotNull(var5, "downloadInfoPack.list.get(i)");
                              DownloadInfo var6 = (DownloadInfo)var5;
                              if (Intrinsics.areEqual(var6.getDownloadId(), var2.getDownloadId())) {
                                 var6.setSizeTotal(var2.getSizeTotal());
                                 var6.setSizeSoFar(var2.getSizeSoFar());
                                 DownloadInfoViewModel.this.downloadInfoPack.setNotifyType(4);
                                 DownloadInfoViewModel.this.downloadInfoPack.setIndex((long)var3);
                                 DownloadInfoViewModel.this.getDownloadInfoObservable().setValue(DownloadInfoViewModel.this.downloadInfoPack);
                                 break;
                              }
                           }
                        }

                        return;
                     }
                  }
               }));
               var2 = DownloadInfoViewModel.this.progressUpdateListener;
               if (var2 != null) {
                  var2.onCompleteUpdate();
               }
            } else {
               var2 = DownloadInfoViewModel.this.progressUpdateListener;
               if (var2 != null) {
                  var2.onStopUpdate();
               }
            }

         }
      }));
   }

   public final void registerForProgressUpdate(DownloadInfoViewModel.OnProgressUpdateListener var1) {
      Intrinsics.checkParameterIsNotNull(var1, "listener");
      this.progressUpdateListener = var1;
      if (this.isDownloading()) {
         var1 = this.progressUpdateListener;
         if (var1 != null) {
            var1.onStartUpdate();
         }
      }

      this.updateRunningItems();
   }

   public final void remove(long var1) {
      this.repository.remove(var1);
      this.hide(var1);
   }

   public final void setOpening(boolean var1) {
      this.isOpening = var1;
   }

   public final void unregisterForProgressUpdate() {
      DownloadInfoViewModel.OnProgressUpdateListener var1 = this.progressUpdateListener;
      if (var1 != null) {
         var1.onStopUpdate();
      }

      this.progressUpdateListener = (DownloadInfoViewModel.OnProgressUpdateListener)null;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }

   public interface OnProgressUpdateListener {
      void onCompleteUpdate();

      void onStartUpdate();

      void onStopUpdate();
   }
}
