package org.mozilla.rocket.urlinput;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.R;
import org.mozilla.icon.FavIconUtils;

public final class QuickSearchAdapter extends ListAdapter {
   private final Function1 clickListener;

   public QuickSearchAdapter(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "clickListener");
      super((DiffUtil.ItemCallback)(new QuickSearchDiffCallback()));
      this.clickListener = var1;
   }

   public void onBindViewHolder(QuickSearchAdapter.EngineViewHolder var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "viewHolder");
      Object var3 = this.getItem(var2);
      Intrinsics.checkExpressionValueIsNotNull(var3, "getItem(i)");
      var1.bind((QuickSearch)var3, this.clickListener);
   }

   public QuickSearchAdapter.EngineViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Intrinsics.checkParameterIsNotNull(var1, "parent");
      View var3 = LayoutInflater.from(var1.getContext()).inflate(2131493014, var1, false);
      Intrinsics.checkExpressionValueIsNotNull(var3, "itemView");
      return new QuickSearchAdapter.EngineViewHolder(var3);
   }

   public static final class EngineViewHolder extends RecyclerView.ViewHolder {
      private ImageView icon;

      public EngineViewHolder(View var1) {
         Intrinsics.checkParameterIsNotNull(var1, "itemView");
         super(var1);
         ImageView var2 = (ImageView)var1.findViewById(R.id.quick_search_img);
         Intrinsics.checkExpressionValueIsNotNull(var2, "itemView.quick_search_img");
         this.icon = var2;
      }

      public final void bind(final QuickSearch var1, final Function1 var2) {
         Intrinsics.checkParameterIsNotNull(var1, "item");
         Intrinsics.checkParameterIsNotNull(var2, "clickListener");
         ThreadPolicy var3 = StrictMode.allowThreadDiskWrites();
         View var4 = this.itemView;
         Intrinsics.checkExpressionValueIsNotNull(var4, "itemView");
         Bitmap var5 = FavIconUtils.getBitmapFromUri(var4.getContext(), var1.getIcon());
         this.icon.setImageBitmap(var5);
         StrictMode.setThreadPolicy(var3);
         this.itemView.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View var1x) {
               var2.invoke(var1);
            }
         }));
      }
   }
}
