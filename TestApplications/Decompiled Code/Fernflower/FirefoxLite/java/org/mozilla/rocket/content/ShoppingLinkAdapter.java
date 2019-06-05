package org.mozilla.rocket.content;

import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.content.data.ShoppingLink;

public final class ShoppingLinkAdapter extends ListAdapter {
   public static final ShoppingLinkAdapter.Companion Companion = new ShoppingLinkAdapter.Companion((DefaultConstructorMarker)null);
   private final ContentPortalListener listener;

   public ShoppingLinkAdapter(ContentPortalListener var1) {
      Intrinsics.checkParameterIsNotNull(var1, "listener");
      super((DiffUtil.ItemCallback)ShoppingLinkAdapter.COMPARATOR.INSTANCE);
      this.listener = var1;
   }

   public int getItemViewType(int var1) {
      int var2 = this.getItemCount();
      byte var3 = 1;
      byte var4;
      if (var1 == var2 - 1) {
         var4 = var3;
      } else {
         var4 = 0;
      }

      return var4;
   }

   public void onAttachedToRecyclerView(RecyclerView var1) {
      Intrinsics.checkParameterIsNotNull(var1, "recyclerView");
      super.onAttachedToRecyclerView(var1);
      RecyclerView.LayoutManager var2 = var1.getLayoutManager();
      if (var2 instanceof GridLayoutManager) {
         GridLayoutManager var3 = (GridLayoutManager)var2;
         var3.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)(new GridLayoutManager.SpanSizeLookup(var3.getSpanCount()) {
            // $FF: synthetic field
            final int $cacheSpanCount;

            {
               this.$cacheSpanCount = var2;
            }

            public int getSpanSize(int var1) {
               int var2 = ShoppingLinkAdapter.this.getItemViewType(var1);
               var1 = 1;
               if (var2 == 1) {
                  var1 = this.$cacheSpanCount;
               }

               return var1;
            }
         }));
      }

   }

   public void onBindViewHolder(ShoppingLinkViewHolder var1, final int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "holder");
      final ShoppingLink var3 = (ShoppingLink)this.getItem(var2);
      if (var3 != null) {
         var1.bind(var3, (OnClickListener)(new OnClickListener() {
            public final void onClick(View var1) {
               TelemetryWrapper.INSTANCE.clickOnEcItem(String.valueOf(var2), var3.getSource(), var3.getName());
               ShoppingLinkAdapter.this.listener.onItemClicked(var3.getUrl());
            }
         }));
      }
   }

   public ShoppingLinkViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parent");
      LayoutInflater var3 = LayoutInflater.from(var1.getContext());
      View var4;
      ShoppingLinkViewHolder var5;
      if (var2 == 1) {
         var4 = var3.inflate(2131492985, var1, false);
         Intrinsics.checkExpressionValueIsNotNull(var4, "v");
         var5 = new ShoppingLinkViewHolder(var4);
      } else {
         var4 = var3.inflate(2131492984, var1, false);
         Intrinsics.checkExpressionValueIsNotNull(var4, "v");
         var5 = new ShoppingLinkViewHolder(var4);
      }

      return var5;
   }

   public static final class COMPARATOR extends DiffUtil.ItemCallback {
      public static final ShoppingLinkAdapter.COMPARATOR INSTANCE = new ShoppingLinkAdapter.COMPARATOR();

      private COMPARATOR() {
      }

      public boolean areContentsTheSame(ShoppingLink var1, ShoppingLink var2) {
         Intrinsics.checkParameterIsNotNull(var1, "oldItem");
         Intrinsics.checkParameterIsNotNull(var2, "newItem");
         return Intrinsics.areEqual(var1, var2);
      }

      public boolean areItemsTheSame(ShoppingLink var1, ShoppingLink var2) {
         Intrinsics.checkParameterIsNotNull(var1, "oldItem");
         Intrinsics.checkParameterIsNotNull(var2, "newItem");
         return Intrinsics.areEqual(var1.getUrl(), var2.getUrl());
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
