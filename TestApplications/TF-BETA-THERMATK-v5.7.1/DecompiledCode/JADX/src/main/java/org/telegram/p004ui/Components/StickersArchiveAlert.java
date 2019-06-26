package org.telegram.p004ui.Components;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Cells.ArchivedStickerSetCell;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.StickersActivity;
import org.telegram.tgnet.TLRPC.StickerSetCovered;

/* renamed from: org.telegram.ui.Components.StickersArchiveAlert */
public class StickersArchiveAlert extends Builder {
    private int currentType;
    private boolean ignoreLayout;
    private BaseFragment parentFragment;
    private int reqId;
    private int scrollOffsetY;
    private ArrayList<StickerSetCovered> stickerSets;

    /* renamed from: org.telegram.ui.Components.StickersArchiveAlert$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        Context context;

        public boolean isEnabled(ViewHolder viewHolder) {
            return false;
        }

        public ListAdapter(Context context) {
            this.context = context;
        }

        public int getItemCount() {
            return StickersArchiveAlert.this.stickerSets.size();
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ArchivedStickerSetCell archivedStickerSetCell = new ArchivedStickerSetCell(this.context, false);
            archivedStickerSetCell.setLayoutParams(new LayoutParams(-1, AndroidUtilities.m26dp(82.0f)));
            return new Holder(archivedStickerSetCell);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            ArchivedStickerSetCell archivedStickerSetCell = (ArchivedStickerSetCell) viewHolder.itemView;
            StickerSetCovered stickerSetCovered = (StickerSetCovered) StickersArchiveAlert.this.stickerSets.get(i);
            boolean z = true;
            if (i == StickersArchiveAlert.this.stickerSets.size() - 1) {
                z = false;
            }
            archivedStickerSetCell.setStickersSet(stickerSetCovered, z);
        }
    }

    public StickersArchiveAlert(Context context, BaseFragment baseFragment, ArrayList<StickerSetCovered> arrayList) {
        super(context);
        StickerSetCovered stickerSetCovered = (StickerSetCovered) arrayList.get(0);
        if (stickerSetCovered.set.masks) {
            this.currentType = 1;
            setTitle(LocaleController.getString("ArchivedMasksAlertTitle", C1067R.string.ArchivedMasksAlertTitle));
        } else {
            this.currentType = 0;
            setTitle(LocaleController.getString("ArchivedStickersAlertTitle", C1067R.string.ArchivedStickersAlertTitle));
        }
        this.stickerSets = new ArrayList(arrayList);
        this.parentFragment = baseFragment;
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        setView(linearLayout);
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        textView.setTextSize(1, 16.0f);
        textView.setPadding(AndroidUtilities.m26dp(23.0f), AndroidUtilities.m26dp(10.0f), AndroidUtilities.m26dp(23.0f), 0);
        if (stickerSetCovered.set.masks) {
            textView.setText(LocaleController.getString("ArchivedMasksAlertInfo", C1067R.string.ArchivedMasksAlertInfo));
        } else {
            textView.setText(LocaleController.getString("ArchivedStickersAlertInfo", C1067R.string.ArchivedStickersAlertInfo));
        }
        linearLayout.addView(textView, LayoutHelper.createLinear(-2, -2));
        RecyclerListView recyclerListView = new RecyclerListView(context);
        recyclerListView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        recyclerListView.setAdapter(new ListAdapter(context));
        recyclerListView.setVerticalScrollBarEnabled(false);
        recyclerListView.setPadding(AndroidUtilities.m26dp(10.0f), 0, AndroidUtilities.m26dp(10.0f), 0);
        recyclerListView.setGlowColor(-657673);
        linearLayout.addView(recyclerListView, LayoutHelper.createLinear(-1, -2, 0.0f, 10.0f, 0.0f, 0.0f));
        setNegativeButton(LocaleController.getString("Close", C1067R.string.Close), C2689-$$Lambda$StickersArchiveAlert$aZBoZIIFPTD8rz1ZNGesyGf1Q4c.INSTANCE);
        if (this.parentFragment != null) {
            setPositiveButton(LocaleController.getString("Settings", C1067R.string.Settings), new C2688-$$Lambda$StickersArchiveAlert$SQKaDZmcHPoQxPPXCgwZ7yu892U(this));
        }
    }

    public /* synthetic */ void lambda$new$1$StickersArchiveAlert(DialogInterface dialogInterface, int i) {
        this.parentFragment.presentFragment(new StickersActivity(this.currentType));
        dialogInterface.dismiss();
    }
}
