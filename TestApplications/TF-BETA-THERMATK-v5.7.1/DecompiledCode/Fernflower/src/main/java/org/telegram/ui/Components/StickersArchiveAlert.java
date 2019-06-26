package org.telegram.ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.StickersActivity;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ArchivedStickerSetCell;

public class StickersArchiveAlert extends AlertDialog.Builder {
   private int currentType;
   private boolean ignoreLayout;
   private BaseFragment parentFragment;
   private int reqId;
   private int scrollOffsetY;
   private ArrayList stickerSets;

   public StickersArchiveAlert(Context var1, BaseFragment var2, ArrayList var3) {
      super(var1);
      TLRPC.StickerSetCovered var4 = (TLRPC.StickerSetCovered)var3.get(0);
      if (var4.set.masks) {
         this.currentType = 1;
         this.setTitle(LocaleController.getString("ArchivedMasksAlertTitle", 2131558656));
      } else {
         this.currentType = 0;
         this.setTitle(LocaleController.getString("ArchivedStickersAlertTitle", 2131558661));
      }

      this.stickerSets = new ArrayList(var3);
      this.parentFragment = var2;
      LinearLayout var5 = new LinearLayout(var1);
      var5.setOrientation(1);
      this.setView(var5);
      TextView var6 = new TextView(var1);
      var6.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      var6.setTextSize(1, 16.0F);
      var6.setPadding(AndroidUtilities.dp(23.0F), AndroidUtilities.dp(10.0F), AndroidUtilities.dp(23.0F), 0);
      if (var4.set.masks) {
         var6.setText(LocaleController.getString("ArchivedMasksAlertInfo", 2131558655));
      } else {
         var6.setText(LocaleController.getString("ArchivedStickersAlertInfo", 2131558660));
      }

      var5.addView(var6, LayoutHelper.createLinear(-2, -2));
      RecyclerListView var7 = new RecyclerListView(var1);
      var7.setLayoutManager(new LinearLayoutManager(this.getContext(), 1, false));
      var7.setAdapter(new StickersArchiveAlert.ListAdapter(var1));
      var7.setVerticalScrollBarEnabled(false);
      var7.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
      var7.setGlowColor(-657673);
      var5.addView(var7, LayoutHelper.createLinear(-1, -2, 0.0F, 10.0F, 0.0F, 0.0F));
      this.setNegativeButton(LocaleController.getString("Close", 2131559117), _$$Lambda$StickersArchiveAlert$aZBoZIIFPTD8rz1ZNGesyGf1Q4c.INSTANCE);
      if (this.parentFragment != null) {
         this.setPositiveButton(LocaleController.getString("Settings", 2131560738), new _$$Lambda$StickersArchiveAlert$SQKaDZmcHPoQxPPXCgwZ7yu892U(this));
      }

   }

   // $FF: synthetic method
   static void lambda$new$0(DialogInterface var0, int var1) {
      var0.dismiss();
   }

   // $FF: synthetic method
   public void lambda$new$1$StickersArchiveAlert(DialogInterface var1, int var2) {
      this.parentFragment.presentFragment(new StickersActivity(this.currentType));
      var1.dismiss();
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      Context context;

      public ListAdapter(Context var2) {
         this.context = var2;
      }

      public int getItemCount() {
         return StickersArchiveAlert.this.stickerSets.size();
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         ArchivedStickerSetCell var3 = (ArchivedStickerSetCell)var1.itemView;
         TLRPC.StickerSetCovered var6 = (TLRPC.StickerSetCovered)StickersArchiveAlert.this.stickerSets.get(var2);
         int var4 = StickersArchiveAlert.this.stickerSets.size();
         boolean var5 = true;
         if (var2 == var4 - 1) {
            var5 = false;
         }

         var3.setStickersSet(var6, var5);
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         ArchivedStickerSetCell var3 = new ArchivedStickerSetCell(this.context, false);
         var3.setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(82.0F)));
         return new RecyclerListView.Holder(var3);
      }
   }
}
