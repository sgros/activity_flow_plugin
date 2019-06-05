package org.mozilla.rocket.download;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.Inject;

public final class DownloadViewModelFactory extends ViewModelProvider.NewInstanceFactory {
   public static final DownloadViewModelFactory.Companion Companion = new DownloadViewModelFactory.Companion((DefaultConstructorMarker)null);
   private static volatile DownloadViewModelFactory INSTANCE;
   private final DownloadInfoRepository repository;

   private DownloadViewModelFactory(DownloadInfoRepository var1) {
      this.repository = var1;
   }

   // $FF: synthetic method
   public DownloadViewModelFactory(DownloadInfoRepository var1, DefaultConstructorMarker var2) {
      this(var1);
   }

   public static final DownloadViewModelFactory getInstance() {
      return Companion.getInstance();
   }

   public ViewModel create(Class var1) {
      Intrinsics.checkParameterIsNotNull(var1, "modelClass");
      if (var1.isAssignableFrom(DownloadIndicatorViewModel.class)) {
         return (ViewModel)(new DownloadIndicatorViewModel(this.repository));
      } else if (var1.isAssignableFrom(DownloadInfoViewModel.class)) {
         return (ViewModel)(new DownloadInfoViewModel(this.repository));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unknown ViewModel class: ");
         var2.append(var1.getName());
         throw (Throwable)(new IllegalArgumentException(var2.toString()));
      }
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final DownloadViewModelFactory getInstance() {
         DownloadViewModelFactory var1 = DownloadViewModelFactory.INSTANCE;
         if (var1 == null) {
            synchronized(this){}

            Throwable var10000;
            label98: {
               boolean var10001;
               try {
                  var1 = DownloadViewModelFactory.INSTANCE;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label98;
               }

               if (var1 != null) {
                  return var1;
               }

               label87:
               try {
                  DownloadInfoRepository var2 = Inject.provideDownloadInfoRepository();
                  Intrinsics.checkExpressionValueIsNotNull(var2, "Inject.provideDownloadInfoRepository()");
                  var1 = new DownloadViewModelFactory(var2, (DefaultConstructorMarker)null);
                  DownloadViewModelFactory.INSTANCE = var1;
                  return var1;
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label87;
               }
            }

            Throwable var9 = var10000;
            throw var9;
         } else {
            return var1;
         }
      }
   }
}
