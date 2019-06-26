// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import org.telegram.ui.Cells.LoadingCell;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Cells.HeaderCell;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.drawable.ColorDrawable;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.messenger.SharedConfig;
import android.os.Build$VERSION;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View$MeasureSpec;
import org.telegram.messenger.DialogObject;
import android.view.ViewGroup;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Cells.DialogMeUrlCell;
import org.telegram.ui.Cells.DialogsEmptyCell;
import org.telegram.ui.Cells.UserCell;
import java.util.List;
import java.util.Collections;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.DialogsActivity;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ContactsController;
import org.telegram.tgnet.TLObject;
import androidx.viewpager.widget.ViewPager;
import org.telegram.ui.Cells.DialogCell;
import android.view.View;
import android.content.SharedPreferences;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import java.util.ArrayList;
import android.content.Context;
import org.telegram.ui.Cells.ArchiveHintCell;
import org.telegram.ui.Components.RecyclerListView;

public class DialogsAdapter extends SelectionAdapter
{
    private ArchiveHintCell archiveHintCell;
    private int currentAccount;
    private int currentCount;
    private boolean dialogsListFrozen;
    private int dialogsType;
    private int folderId;
    private boolean hasHints;
    private boolean isOnlySelect;
    private boolean isReordering;
    private Context mContext;
    private long openedDialogId;
    private ArrayList<Long> selectedDialogs;
    private boolean showArchiveHint;
    private boolean showContacts;
    
    public DialogsAdapter(final Context mContext, final int dialogsType, final int folderId, final boolean isOnlySelect) {
        this.currentAccount = UserConfig.selectedAccount;
        this.mContext = mContext;
        this.dialogsType = dialogsType;
        this.folderId = folderId;
        this.isOnlySelect = isOnlySelect;
        this.hasHints = (folderId == 0 && dialogsType == 0 && !isOnlySelect);
        this.selectedDialogs = new ArrayList<Long>();
        if (this.folderId == 1) {
            final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            this.showArchiveHint = globalMainSettings.getBoolean("archivehint", true);
            globalMainSettings.edit().putBoolean("archivehint", false).commit();
            if (this.showArchiveHint) {
                this.archiveHintCell = new ArchiveHintCell(mContext);
            }
        }
    }
    
    public boolean addOrRemoveSelectedDialog(final long l, final View view) {
        if (this.selectedDialogs.contains(l)) {
            this.selectedDialogs.remove(l);
            if (view instanceof DialogCell) {
                ((DialogCell)view).setChecked(false, true);
            }
            return false;
        }
        this.selectedDialogs.add(l);
        if (view instanceof DialogCell) {
            ((DialogCell)view).setChecked(true, true);
        }
        return true;
    }
    
    public int fixPosition(int n) {
        int n2 = n;
        if (this.hasHints) {
            n2 = n - (MessagesController.getInstance(this.currentAccount).hintDialogs.size() + 2);
        }
        n = n2;
        if (this.showArchiveHint) {
            n = n2 - 2;
        }
        return n;
    }
    
    public ViewPager getArchiveHintCellPager() {
        final ArchiveHintCell archiveHintCell = this.archiveHintCell;
        ViewPager viewPager;
        if (archiveHintCell != null) {
            viewPager = archiveHintCell.getViewPager();
        }
        else {
            viewPager = null;
        }
        return viewPager;
    }
    
    public TLObject getItem(int n) {
        if (this.showContacts) {
            n -= 3;
            if (n >= 0 && n < ContactsController.getInstance(this.currentAccount).contacts.size()) {
                return MessagesController.getInstance(this.currentAccount).getUser(ContactsController.getInstance(this.currentAccount).contacts.get(n).user_id);
            }
            return null;
        }
        else {
            int n2 = n;
            if (this.showArchiveHint) {
                n2 = n - 2;
            }
            final ArrayList<TLRPC.Dialog> dialogsArray = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, this.dialogsListFrozen);
            n = n2;
            if (this.hasHints) {
                n = MessagesController.getInstance(this.currentAccount).hintDialogs.size() + 2;
                if (n2 < n) {
                    return MessagesController.getInstance(this.currentAccount).hintDialogs.get(n2 - 1);
                }
                n = n2 - n;
            }
            if (n >= 0 && n < dialogsArray.size()) {
                return dialogsArray.get(n);
            }
            return null;
        }
    }
    
    @Override
    public int getItemCount() {
        this.showContacts = false;
        final int size = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, this.dialogsListFrozen).size();
        if (size != 0 || (this.folderId == 0 && !MessagesController.getInstance(this.currentAccount).isLoadingDialogs(this.folderId))) {
            int n;
            if (MessagesController.getInstance(this.currentAccount).isDialogsEndReached(this.folderId) && size != 0) {
                n = size;
            }
            else {
                n = size + 1;
            }
            int n2;
            if (this.hasHints) {
                n2 = n + (MessagesController.getInstance(this.currentAccount).hintDialogs.size() + 2);
            }
            else {
                n2 = n;
                if (this.dialogsType == 0) {
                    n2 = n;
                    if (size == 0) {
                        n2 = n;
                        if (this.folderId == 0) {
                            if (ContactsController.getInstance(this.currentAccount).contacts.isEmpty() && ContactsController.getInstance(this.currentAccount).isLoadingContacts()) {
                                return this.currentCount = 0;
                            }
                            n2 = n;
                            if (!ContactsController.getInstance(this.currentAccount).contacts.isEmpty()) {
                                n2 = n + (ContactsController.getInstance(this.currentAccount).contacts.size() + 2);
                                this.showContacts = true;
                            }
                        }
                    }
                }
            }
            int n3 = n2;
            if (this.folderId == 1) {
                n3 = n2;
                if (this.showArchiveHint) {
                    n3 = n2 + 2;
                }
            }
            int currentCount = n3;
            if (this.folderId == 0) {
                currentCount = n3;
                if (size != 0) {
                    currentCount = n3 + 1;
                }
            }
            return this.currentCount = currentCount;
        }
        if (this.folderId == 1 && this.showArchiveHint) {
            return this.currentCount = 2;
        }
        return this.currentCount = 0;
    }
    
    @Override
    public int getItemViewType(int size) {
        if (this.showContacts) {
            if (size == 0) {
                return 5;
            }
            if (size == 1) {
                return 8;
            }
            if (size == 2) {
                return 7;
            }
            return 6;
        }
        else {
            int n2;
            if (this.hasHints) {
                final int size2 = MessagesController.getInstance(this.currentAccount).hintDialogs.size();
                final int n = size2 + 2;
                if (size < n) {
                    if (size == 0) {
                        return 2;
                    }
                    if (size == size2 + 1) {
                        return 3;
                    }
                    return 4;
                }
                else {
                    n2 = size - n;
                }
            }
            else {
                n2 = size;
                if (this.showArchiveHint) {
                    if (size == 0) {
                        return 9;
                    }
                    if (size == 1) {
                        return 8;
                    }
                    n2 = size - 2;
                }
            }
            size = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, this.dialogsListFrozen).size();
            if (n2 == size) {
                if (!MessagesController.getInstance(this.currentAccount).isDialogsEndReached(this.folderId)) {
                    return 1;
                }
                if (size == 0) {
                    return 5;
                }
                return 10;
            }
            else {
                if (n2 > size) {
                    return 10;
                }
                return 0;
            }
        }
    }
    
    public ArrayList<Long> getSelectedDialogs() {
        return this.selectedDialogs;
    }
    
    public boolean hasSelectedDialogs() {
        final ArrayList<Long> selectedDialogs = this.selectedDialogs;
        return selectedDialogs != null && !selectedDialogs.isEmpty();
    }
    
    public boolean isDataSetChanged() {
        final int currentCount = this.currentCount;
        final int itemCount = this.getItemCount();
        boolean b = true;
        if (currentCount == itemCount) {
            b = (currentCount == 1 && b);
        }
        return b;
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        final int itemViewType = viewHolder.getItemViewType();
        boolean b = true;
        if (itemViewType == 1 || itemViewType == 5 || itemViewType == 3 || itemViewType == 8 || itemViewType == 7 || itemViewType == 9 || itemViewType == 10) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void notifyDataSetChanged() {
        this.hasHints = (this.folderId == 0 && this.dialogsType == 0 && !this.isOnlySelect && !MessagesController.getInstance(this.currentAccount).hintDialogs.isEmpty());
        super.notifyDataSetChanged();
    }
    
    @Override
    public void notifyItemMoved(final int n, final int n2) {
        final ArrayList<TLRPC.Dialog> dialogsArray = DialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.folderId, false);
        final int fixPosition = this.fixPosition(n);
        final int fixPosition2 = this.fixPosition(n2);
        final TLRPC.Dialog dialog = dialogsArray.get(fixPosition);
        final TLRPC.Dialog dialog2 = dialogsArray.get(fixPosition2);
        final int pinnedNum = dialog.pinnedNum;
        dialog.pinnedNum = dialog2.pinnedNum;
        dialog2.pinnedNum = pinnedNum;
        Collections.swap(dialogsArray, fixPosition, fixPosition2);
        super.notifyItemMoved(n, n2);
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        final int itemViewType = viewHolder.getItemViewType();
        if (itemViewType != 0) {
            if (itemViewType != 4) {
                if (itemViewType != 5) {
                    if (itemViewType == 6) {
                        ((UserCell)viewHolder.itemView).setData(MessagesController.getInstance(this.currentAccount).getUser(ContactsController.getInstance(this.currentAccount).contacts.get(n - 3).user_id), null, null, 0);
                    }
                }
                else {
                    ((DialogsEmptyCell)viewHolder.itemView).setType(this.showContacts ? 1 : 0);
                }
            }
            else {
                ((DialogMeUrlCell)viewHolder.itemView).setRecentMeUrl((TLRPC.RecentMeUrl)this.getItem(n));
            }
        }
        else {
            final DialogCell dialogCell = (DialogCell)viewHolder.itemView;
            final TLRPC.Dialog dialog = (TLRPC.Dialog)this.getItem(n);
            final TLRPC.Dialog dialog2 = (TLRPC.Dialog)this.getItem(n + 1);
            final int folderId = this.folderId;
            final boolean b = true;
            if (folderId == 0) {
                dialogCell.useSeparator = (n != this.getItemCount() - 2);
            }
            else {
                dialogCell.useSeparator = (n != this.getItemCount() - 1);
            }
            dialogCell.fullSeparator = (dialog.pinned && dialog2 != null && !dialog2.pinned);
            if (this.dialogsType == 0 && AndroidUtilities.isTablet()) {
                dialogCell.setDialogSelected(dialog.id == this.openedDialogId && b);
            }
            dialogCell.setChecked(this.selectedDialogs.contains(dialog.id), false);
            dialogCell.setDialog(dialog, this.dialogsType, this.folderId);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int n) {
        Object archiveHintCell = null;
        switch (n) {
            default: {
                archiveHintCell = new View(this.mContext) {
                    protected void onMeasure(final int n, int n2) {
                        final int size = DialogsActivity.getDialogsArray(DialogsAdapter.this.currentAccount, DialogsAdapter.this.dialogsType, DialogsAdapter.this.folderId, DialogsAdapter.this.dialogsListFrozen).size();
                        final Object value = MessagesController.getInstance(DialogsAdapter.this.currentAccount).dialogs_dict.get(DialogObject.makeFolderDialogId(1));
                        final int n3 = 0;
                        final boolean b = value != null;
                        int n4 = n3;
                        if (size != 0) {
                            if (!b) {
                                n4 = n3;
                            }
                            else {
                                final int size2 = View$MeasureSpec.getSize(n2);
                                if ((n2 = size2) == 0) {
                                    final View view = (View)this.getParent();
                                    n2 = size2;
                                    if (view != null) {
                                        n2 = view.getMeasuredHeight();
                                    }
                                }
                                int n5;
                                if ((n5 = n2) == 0) {
                                    final int y = AndroidUtilities.displaySize.y;
                                    final int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                                    if (Build$VERSION.SDK_INT >= 21) {
                                        n2 = AndroidUtilities.statusBarHeight;
                                    }
                                    else {
                                        n2 = 0;
                                    }
                                    n5 = y - currentActionBarHeight - n2;
                                }
                                float n6;
                                if (SharedConfig.useThreeLinesLayout) {
                                    n6 = 78.0f;
                                }
                                else {
                                    n6 = 72.0f;
                                }
                                n2 = AndroidUtilities.dp(n6);
                                final int n7 = size * n2 + (size - 1);
                                if (n7 < n5) {
                                    n4 = n5 - n7 + n2 + 1;
                                }
                                else {
                                    final int n8 = n7 - n5;
                                    ++n2;
                                    n4 = n3;
                                    if (n8 < n2) {
                                        n4 = n2 - n8;
                                    }
                                }
                            }
                        }
                        this.setMeasuredDimension(View$MeasureSpec.getSize(n), n4);
                    }
                };
                break;
            }
            case 9: {
                final ArchiveHintCell archiveHintCell2 = (ArchiveHintCell)(archiveHintCell = this.archiveHintCell);
                if (archiveHintCell2.getParent() != null) {
                    ((ViewGroup)this.archiveHintCell.getParent()).removeView((View)this.archiveHintCell);
                    archiveHintCell = archiveHintCell2;
                    break;
                }
                break;
            }
            case 8: {
                archiveHintCell = new ShadowSectionCell(this.mContext);
                final CombinedDrawable backgroundDrawable = new CombinedDrawable((Drawable)new ColorDrawable(Theme.getColor("windowBackgroundGray")), Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                backgroundDrawable.setFullsize(true);
                ((View)archiveHintCell).setBackgroundDrawable((Drawable)backgroundDrawable);
                break;
            }
            case 7: {
                archiveHintCell = new HeaderCell(this.mContext);
                ((HeaderCell)archiveHintCell).setText(LocaleController.getString("YourContacts", 2131561143));
                break;
            }
            case 6: {
                archiveHintCell = new UserCell(this.mContext, 8, 0, false);
                break;
            }
            case 5: {
                archiveHintCell = new DialogsEmptyCell(this.mContext);
                break;
            }
            case 4: {
                archiveHintCell = new DialogMeUrlCell(this.mContext);
                break;
            }
            case 3: {
                archiveHintCell = new FrameLayout(this.mContext) {
                    protected void onMeasure(final int n, final int n2) {
                        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(12.0f), 1073741824));
                    }
                };
                ((FrameLayout)archiveHintCell).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
                final View view = new View(this.mContext);
                view.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                ((FrameLayout)archiveHintCell).addView(view, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
                break;
            }
            case 2: {
                archiveHintCell = new HeaderCell(this.mContext);
                ((HeaderCell)archiveHintCell).setText(LocaleController.getString("RecentlyViewed", 2131560539));
                final TextView textView = new TextView(this.mContext);
                textView.setTextSize(1, 15.0f);
                textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
                textView.setText((CharSequence)LocaleController.getString("RecentlyViewedHide", 2131560540));
                final boolean isRTL = LocaleController.isRTL;
                final int n2 = 3;
                int n3;
                if (isRTL) {
                    n3 = 3;
                }
                else {
                    n3 = 5;
                }
                textView.setGravity(n3 | 0x10);
                int n4;
                if (LocaleController.isRTL) {
                    n4 = n2;
                }
                else {
                    n4 = 5;
                }
                ((FrameLayout)archiveHintCell).addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, n4 | 0x30, 17.0f, 15.0f, 17.0f, 0.0f));
                textView.setOnClickListener((View$OnClickListener)new _$$Lambda$DialogsAdapter$9rZHEUZrGiCwZ807CAFufwsMqa0(this));
                break;
            }
            case 1: {
                archiveHintCell = new LoadingCell(this.mContext);
                break;
            }
            case 0: {
                archiveHintCell = new DialogCell(this.mContext, true, false);
                break;
            }
        }
        if (n == 5) {
            n = -1;
        }
        else {
            n = -2;
        }
        ((View)archiveHintCell).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, n));
        return new RecyclerListView.Holder((View)archiveHintCell);
    }
    
    public void onReorderStateChanged(final boolean isReordering) {
        this.isReordering = isReordering;
    }
    
    @Override
    public void onViewAttachedToWindow(final ViewHolder viewHolder) {
        final View itemView = viewHolder.itemView;
        if (itemView instanceof DialogCell) {
            final DialogCell dialogCell = (DialogCell)itemView;
            dialogCell.onReorderStateChanged(this.isReordering, false);
            dialogCell.setDialogIndex(this.fixPosition(viewHolder.getAdapterPosition()));
            dialogCell.checkCurrentDialogIndex(this.dialogsListFrozen);
            dialogCell.setChecked(this.selectedDialogs.contains(dialogCell.getDialogId()), false);
        }
    }
    
    public void setDialogsListFrozen(final boolean dialogsListFrozen) {
        this.dialogsListFrozen = dialogsListFrozen;
    }
    
    public void setOpenedDialogId(final long openedDialogId) {
        this.openedDialogId = openedDialogId;
    }
}
