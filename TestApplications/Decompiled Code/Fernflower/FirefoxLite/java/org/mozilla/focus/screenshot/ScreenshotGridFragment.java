package org.mozilla.focus.screenshot;

import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.mozilla.focus.fragment.PanelFragment;
import org.mozilla.focus.fragment.PanelFragmentStatusListener;

public class ScreenshotGridFragment extends PanelFragment implements PanelFragmentStatusListener {
   private ScreenshotItemAdapter mAdapter;
   private ViewGroup mContainerEmptyView;
   private RecyclerView mContainerRecyclerView;

   public static ScreenshotGridFragment newInstance() {
      return new ScreenshotGridFragment();
   }

   public void notifyItemDelete(long var1) {
      if (this.mAdapter != null) {
         this.mAdapter.onItemDelete(var1);
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var8 = var1.inflate(2131492973, var2, false);
      this.mContainerRecyclerView = (RecyclerView)var8.findViewById(2131296605);
      this.mContainerEmptyView = (ViewGroup)var8.findViewById(2131296422);
      TextView var9 = (TextView)var8.findViewById(2131296603);
      Drawable var10 = this.getResources().getDrawable(2131230814, (Theme)null).mutate();
      var10.setBounds(0, 0, this.getResources().getDimensionPixelSize(2131165437), this.getResources().getDimensionPixelSize(2131165437));
      DrawableCompat.setTint(var10, ContextCompat.getColor(this.getActivity(), 2131099709));
      ImageSpan var11 = new ImageSpan(var10);
      String var4 = this.getString(2131755378);
      String var5 = this.getString(2131755377);
      StringBuilder var6 = new StringBuilder();
      var6.append(var4);
      var6.append(var5);
      SpannableString var12 = new SpannableString(var6.toString());
      int var7 = var4.length();
      var12.setSpan(var11, var7, var7 + 1, 18);
      var9.setText(var12);
      return var8;
   }

   public void onStatus(int var1) {
      if (var1 == 0) {
         this.mContainerRecyclerView.setVisibility(8);
         this.mContainerEmptyView.setVisibility(0);
      } else if (1 == var1) {
         this.mContainerRecyclerView.setVisibility(0);
         this.mContainerEmptyView.setVisibility(8);
      } else {
         this.mContainerRecyclerView.setVisibility(8);
         this.mContainerEmptyView.setVisibility(8);
      }

   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      GridLayoutManager var3 = new GridLayoutManager(this.getActivity(), 3);
      this.mAdapter = new ScreenshotItemAdapter(this.mContainerRecyclerView, this.getActivity(), this, var3);
      this.mContainerRecyclerView.setLayoutManager(var3);
      ItemOffsetDecoration var4 = new ItemOffsetDecoration(3, this.getResources().getDimensionPixelSize(2131165438));
      this.mContainerRecyclerView.addItemDecoration(var4);
      this.mContainerRecyclerView.setAdapter(this.mAdapter);
   }

   public void tryLoadMore() {
      this.mAdapter.tryLoadMore();
   }
}
