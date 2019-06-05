package android.support.v7.recyclerview.extensions;

import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import java.util.List;

public abstract class ListAdapter extends RecyclerView.Adapter {
   private final AsyncListDiffer mHelper;

   protected ListAdapter(DiffUtil.ItemCallback var1) {
      this.mHelper = new AsyncListDiffer(new AdapterListUpdateCallback(this), (new AsyncDifferConfig.Builder(var1)).build());
   }

   protected Object getItem(int var1) {
      return this.mHelper.getCurrentList().get(var1);
   }

   public int getItemCount() {
      return this.mHelper.getCurrentList().size();
   }

   public void submitList(List var1) {
      this.mHelper.submitList(var1);
   }
}
