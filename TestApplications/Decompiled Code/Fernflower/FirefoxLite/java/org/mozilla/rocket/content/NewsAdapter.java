package org.mozilla.rocket.content;

import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.lite.partner.NewsItem;

public final class NewsAdapter extends ListAdapter {
   private final ContentPortalListener listener;

   public NewsAdapter(ContentPortalListener var1) {
      Intrinsics.checkParameterIsNotNull(var1, "listener");
      super((DiffUtil.ItemCallback)NewsAdapter.COMPARATOR.INSTANCE);
      this.listener = var1;
   }

   public void onBindViewHolder(NewsViewHolder var1, final int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "holder");
      final NewsItem var3 = (NewsItem)this.getItem(var2);
      if (var3 != null) {
         var1.bind(var3, (OnClickListener)(new OnClickListener() {
            public final void onClick(View var1) {
               TelemetryWrapper.INSTANCE.clickOnNewsItem(String.valueOf(var2), var3.getSource(), var3.getPartner(), var3.getCategory(), var3.getSubcategory());
               NewsAdapter.this.listener.onItemClicked(var3.getNewsUrl());
            }
         }));
      }
   }

   public NewsViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parent");
      View var3 = LayoutInflater.from(var1.getContext()).inflate(2131492979, var1, false);
      Intrinsics.checkExpressionValueIsNotNull(var3, "v");
      return new NewsViewHolder(var3);
   }

   public static final class COMPARATOR extends DiffUtil.ItemCallback {
      public static final NewsAdapter.COMPARATOR INSTANCE = new NewsAdapter.COMPARATOR();

      private COMPARATOR() {
      }

      public boolean areContentsTheSame(NewsItem var1, NewsItem var2) {
         Intrinsics.checkParameterIsNotNull(var1, "oldItem");
         Intrinsics.checkParameterIsNotNull(var2, "newItem");
         return Intrinsics.areEqual(var1, var2);
      }

      public boolean areItemsTheSame(NewsItem var1, NewsItem var2) {
         Intrinsics.checkParameterIsNotNull(var1, "oldItem");
         Intrinsics.checkParameterIsNotNull(var2, "newItem");
         return Intrinsics.areEqual(var1.getId(), var2.getId());
      }
   }
}
