// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.ViewGroup;
import org.telegram.ui.Cells.ArchivedStickerSetCell;
import org.telegram.ui.StickersActivity;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.widget.TextView;
import android.view.View;
import android.widget.LinearLayout;
import java.util.Collection;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.AlertDialog;

public class StickersArchiveAlert extends Builder
{
    private int currentType;
    private boolean ignoreLayout;
    private BaseFragment parentFragment;
    private int reqId;
    private int scrollOffsetY;
    private ArrayList<TLRPC.StickerSetCovered> stickerSets;
    
    public StickersArchiveAlert(final Context context, final BaseFragment parentFragment, final ArrayList<TLRPC.StickerSetCovered> c) {
        super(context);
        final TLRPC.StickerSetCovered stickerSetCovered = c.get(0);
        if (stickerSetCovered.set.masks) {
            this.currentType = 1;
            ((AlertDialog.Builder)this).setTitle(LocaleController.getString("ArchivedMasksAlertTitle", 2131558656));
        }
        else {
            this.currentType = 0;
            ((AlertDialog.Builder)this).setTitle(LocaleController.getString("ArchivedStickersAlertTitle", 2131558661));
        }
        this.stickerSets = new ArrayList<TLRPC.StickerSetCovered>(c);
        this.parentFragment = parentFragment;
        final LinearLayout view = new LinearLayout(context);
        view.setOrientation(1);
        ((AlertDialog.Builder)this).setView((View)view);
        final TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        textView.setTextSize(1, 16.0f);
        textView.setPadding(AndroidUtilities.dp(23.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(23.0f), 0);
        if (stickerSetCovered.set.masks) {
            textView.setText((CharSequence)LocaleController.getString("ArchivedMasksAlertInfo", 2131558655));
        }
        else {
            textView.setText((CharSequence)LocaleController.getString("ArchivedStickersAlertInfo", 2131558660));
        }
        view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
        final RecyclerListView recyclerListView = new RecyclerListView(context);
        recyclerListView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(((AlertDialog.Builder)this).getContext(), 1, false));
        recyclerListView.setAdapter(new ListAdapter(context));
        recyclerListView.setVerticalScrollBarEnabled(false);
        recyclerListView.setPadding(AndroidUtilities.dp(10.0f), 0, AndroidUtilities.dp(10.0f), 0);
        recyclerListView.setGlowColor(-657673);
        view.addView((View)recyclerListView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 0.0f, 10.0f, 0.0f, 0.0f));
        ((AlertDialog.Builder)this).setNegativeButton(LocaleController.getString("Close", 2131559117), (DialogInterface$OnClickListener)_$$Lambda$StickersArchiveAlert$aZBoZIIFPTD8rz1ZNGesyGf1Q4c.INSTANCE);
        if (this.parentFragment != null) {
            ((AlertDialog.Builder)this).setPositiveButton(LocaleController.getString("Settings", 2131560738), (DialogInterface$OnClickListener)new _$$Lambda$StickersArchiveAlert$SQKaDZmcHPoQxPPXCgwZ7yu892U(this));
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        Context context;
        
        public ListAdapter(final Context context) {
            this.context = context;
        }
        
        @Override
        public int getItemCount() {
            return StickersArchiveAlert.this.stickerSets.size();
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
            final ArchivedStickerSetCell archivedStickerSetCell = (ArchivedStickerSetCell)viewHolder.itemView;
            final TLRPC.StickerSetCovered stickerSetCovered = StickersArchiveAlert.this.stickerSets.get(index);
            final int size = StickersArchiveAlert.this.stickerSets.size();
            boolean b = true;
            if (index == size - 1) {
                b = false;
            }
            archivedStickerSetCell.setStickersSet(stickerSetCovered, b);
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            final ArchivedStickerSetCell archivedStickerSetCell = new ArchivedStickerSetCell(this.context, false);
            ((View)archivedStickerSetCell).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(82.0f)));
            return new RecyclerListView.Holder((View)archivedStickerSetCell);
        }
    }
}
