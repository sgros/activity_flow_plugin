package org.mozilla.rocket.content;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.Settings;
import org.mozilla.lite.partner.Repository;
import org.mozilla.threadutils.ThreadUtils;

public final class NewsPresenter implements ContentPortalView.NewsListListener {
   public static final NewsPresenter.Companion Companion = new NewsPresenter.Companion((DefaultConstructorMarker)null);
   private static final long LOADMORE_THRESHOLD = 3000L;
   private boolean isLoading;
   private final NewsViewContract newsViewContract;
   private NewsViewModel newsViewModel;

   public NewsPresenter(NewsViewContract var1) {
      Intrinsics.checkParameterIsNotNull(var1, "newsViewContract");
      super();
      this.newsViewContract = var1;
   }

   private final void updateSourcePriority(Context var1) {
      Settings.getInstance(var1).setPriority("pref_int_news_priority", 2);
   }

   public final void checkNewsRepositoryReset(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      if (NewsRepository.Companion.isEmpty() && this.newsViewModel != null) {
         Repository var3 = NewsRepository.Companion.getInstance(var1);
         var3.setOnDataChangedListener((Repository.OnDataChangedListener)this.newsViewModel);
         NewsViewModel var2 = this.newsViewModel;
         if (var2 != null) {
            var2.setRepository(var3);
         }

         NewsViewModel var4 = this.newsViewModel;
         if (var4 != null) {
            MutableLiveData var5 = var4.getItems();
            if (var5 != null) {
               var5.setValue((Object)null);
            }
         }

         var4 = this.newsViewModel;
         if (var4 != null) {
            var4.loadMore();
         }
      }

   }

   public void loadMore() {
      if (!this.isLoading) {
         NewsViewModel var1 = this.newsViewModel;
         if (var1 != null) {
            var1.loadMore();
         }

         this.isLoading = true;
         ThreadUtils.postToMainThreadDelayed((Runnable)(new Runnable() {
            public final void run() {
               NewsPresenter.this.isLoading = false;
            }
         }), LOADMORE_THRESHOLD);
      }

   }

   public void onShow(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      this.updateSourcePriority(var1);
      this.checkNewsRepositoryReset(var1);
   }

   public final void setupNewsViewModel(FragmentActivity var1) {
      if (var1 != null) {
         this.newsViewModel = (NewsViewModel)ViewModelProviders.of(var1).get(NewsViewModel.class);
         Repository var2 = NewsRepository.Companion.getInstance((Context)var1);
         var2.setOnDataChangedListener((Repository.OnDataChangedListener)this.newsViewModel);
         NewsViewModel var3 = this.newsViewModel;
         if (var3 != null) {
            var3.setRepository(var2);
         }

         var3 = this.newsViewModel;
         if (var3 != null) {
            MutableLiveData var4 = var3.getItems();
            if (var4 != null) {
               var4.observe(this.newsViewContract.getViewLifecycleOwner(), (Observer)(new Observer() {
                  public final void onChanged(List var1) {
                     NewsPresenter.this.newsViewContract.updateNews(var1);
                     NewsPresenter.this.isLoading = false;
                  }
               }));
            }
         }

         var3 = this.newsViewModel;
         if (var3 != null) {
            var3.loadMore();
         }

      }
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }
   }
}
