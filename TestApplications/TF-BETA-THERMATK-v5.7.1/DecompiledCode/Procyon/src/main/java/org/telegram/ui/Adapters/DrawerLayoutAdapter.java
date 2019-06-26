// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import android.view.ViewGroup$LayoutParams;
import android.view.View$OnClickListener;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewGroup;
import org.telegram.ui.Cells.DrawerActionCell;
import org.telegram.ui.Cells.DrawerUserCell;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.LocaleController;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Cells.DrawerProfileCell;
import android.content.Context;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;

public class DrawerLayoutAdapter extends SelectionAdapter
{
    private ArrayList<Integer> accountNumbers;
    private boolean accountsShowed;
    private ArrayList<Item> items;
    private Context mContext;
    private DrawerProfileCell profileCell;
    
    public DrawerLayoutAdapter(final Context mContext) {
        this.items = new ArrayList<Item>(11);
        this.accountNumbers = new ArrayList<Integer>();
        this.mContext = mContext;
        final int activatedAccountsCount = UserConfig.getActivatedAccountsCount();
        boolean accountsShowed = true;
        if (activatedAccountsCount <= 1 || !MessagesController.getGlobalMainSettings().getBoolean("accountsShowed", true)) {
            accountsShowed = false;
        }
        this.accountsShowed = accountsShowed;
        Theme.createDialogsResources(mContext);
        this.resetItems();
    }
    
    private int getAccountRowsCount() {
        int n = this.accountNumbers.size() + 1;
        if (this.accountNumbers.size() < 3) {
            ++n;
        }
        return n;
    }
    
    private void resetItems() {
        this.accountNumbers.clear();
        for (int i = 0; i < 3; ++i) {
            if (UserConfig.getInstance(i).isClientActivated()) {
                this.accountNumbers.add(i);
            }
        }
        Collections.sort(this.accountNumbers, (Comparator<? super Integer>)_$$Lambda$DrawerLayoutAdapter$4y_nKr_8KM_K0ZE7aMWcSgkl64k.INSTANCE);
        this.items.clear();
        if (!UserConfig.getInstance(UserConfig.selectedAccount).isClientActivated()) {
            return;
        }
        if (Theme.getEventType() == 0) {
            this.items.add(new Item(2, LocaleController.getString("NewGroup", 2131559900), 2131165581));
            this.items.add(new Item(3, LocaleController.getString("NewSecretChat", 2131559909), 2131165595));
            this.items.add(new Item(4, LocaleController.getString("NewChannel", 2131559898), 2131165573));
            this.items.add(null);
            this.items.add(new Item(6, LocaleController.getString("Contacts", 2131559149), 2131165577));
            this.items.add(new Item(11, LocaleController.getString("SavedMessages", 2131560633), 2131165567));
            this.items.add(new Item(10, LocaleController.getString("Calls", 2131558888), 2131165570));
            this.items.add(new Item(7, LocaleController.getString("InviteFriends", 2131559677), 2131165585));
            this.items.add(new Item(8, LocaleController.getString("Settings", 2131560738), 2131165598));
            this.items.add(new Item(9, LocaleController.getString("TelegramFAQ", 2131560869), 2131165583));
        }
        else {
            this.items.add(new Item(2, LocaleController.getString("NewGroup", 2131559900), 2131165580));
            this.items.add(new Item(3, LocaleController.getString("NewSecretChat", 2131559909), 2131165594));
            this.items.add(new Item(4, LocaleController.getString("NewChannel", 2131559898), 2131165568));
            this.items.add(null);
            this.items.add(new Item(6, LocaleController.getString("Contacts", 2131559149), 2131165576));
            this.items.add(new Item(11, LocaleController.getString("SavedMessages", 2131560633), 2131165592));
            this.items.add(new Item(10, LocaleController.getString("Calls", 2131558888), 2131165569));
            this.items.add(new Item(7, LocaleController.getString("InviteFriends", 2131559677), 2131165584));
            this.items.add(new Item(8, LocaleController.getString("Settings", 2131560738), 2131165596));
            this.items.add(new Item(9, LocaleController.getString("TelegramFAQ", 2131560869), 2131165582));
        }
    }
    
    public int getId(int index) {
        final int n = index -= 2;
        if (this.accountsShowed) {
            index = n - this.getAccountRowsCount();
        }
        int id;
        final int n2 = id = -1;
        if (index >= 0) {
            if (index >= this.items.size()) {
                id = n2;
            }
            else {
                final Item item = this.items.get(index);
                id = n2;
                if (item != null) {
                    id = item.id;
                }
            }
        }
        return id;
    }
    
    @Override
    public int getItemCount() {
        int n = this.items.size() + 2;
        if (this.accountsShowed) {
            n += this.getAccountRowsCount();
        }
        return n;
    }
    
    @Override
    public int getItemViewType(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        final int n2 = n -= 2;
        if (this.accountsShowed) {
            if (n2 < this.accountNumbers.size()) {
                return 4;
            }
            if (this.accountNumbers.size() < 3) {
                if (n2 == this.accountNumbers.size()) {
                    return 5;
                }
                if (n2 == this.accountNumbers.size() + 1) {
                    return 2;
                }
            }
            else if (n2 == this.accountNumbers.size()) {
                return 2;
            }
            n = n2 - this.getAccountRowsCount();
        }
        if (n == 3) {
            return 2;
        }
        return 3;
    }
    
    public boolean isAccountsShowed() {
        return this.accountsShowed;
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        final int itemViewType = viewHolder.getItemViewType();
        return itemViewType == 3 || itemViewType == 4 || itemViewType == 5;
    }
    
    @Override
    public void notifyDataSetChanged() {
        this.resetItems();
        super.notifyDataSetChanged();
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int index) {
        final int itemViewType = viewHolder.getItemViewType();
        if (itemViewType != 0) {
            if (itemViewType != 3) {
                if (itemViewType == 4) {
                    ((DrawerUserCell)viewHolder.itemView).setAccount(this.accountNumbers.get(index - 2));
                }
            }
            else {
                final int n = index -= 2;
                if (this.accountsShowed) {
                    index = n - this.getAccountRowsCount();
                }
                final DrawerActionCell drawerActionCell = (DrawerActionCell)viewHolder.itemView;
                this.items.get(index).bind(drawerActionCell);
                drawerActionCell.setPadding(0, 0, 0, 0);
            }
        }
        else {
            ((DrawerProfileCell)viewHolder.itemView).setUser(MessagesController.getInstance(UserConfig.selectedAccount).getUser(UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()), this.accountsShowed);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Object profileCell;
        if (n != 0) {
            if (n != 2) {
                if (n != 3) {
                    if (n != 4) {
                        if (n != 5) {
                            profileCell = new EmptyCell(this.mContext, AndroidUtilities.dp(8.0f));
                        }
                        else {
                            profileCell = new DrawerAddCell(this.mContext);
                        }
                    }
                    else {
                        profileCell = new DrawerUserCell(this.mContext);
                    }
                }
                else {
                    profileCell = new DrawerActionCell(this.mContext);
                }
            }
            else {
                profileCell = new DividerCell(this.mContext);
            }
        }
        else {
            (this.profileCell = new DrawerProfileCell(this.mContext)).setOnArrowClickListener((View$OnClickListener)new _$$Lambda$DrawerLayoutAdapter$u_CA_vrvN_8Y4__5_7WI_JZfY_U(this));
            profileCell = this.profileCell;
        }
        ((View)profileCell).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
        return new RecyclerListView.Holder((View)profileCell);
    }
    
    public void setAccountsShowed(final boolean accountsShowed, final boolean b) {
        if (this.accountsShowed == accountsShowed) {
            return;
        }
        this.accountsShowed = accountsShowed;
        final DrawerProfileCell profileCell = this.profileCell;
        if (profileCell != null) {
            profileCell.setAccountsShowed(this.accountsShowed);
        }
        MessagesController.getGlobalMainSettings().edit().putBoolean("accountsShowed", this.accountsShowed).commit();
        if (b) {
            if (this.accountsShowed) {
                this.notifyItemRangeInserted(2, this.getAccountRowsCount());
            }
            else {
                this.notifyItemRangeRemoved(2, this.getAccountRowsCount());
            }
        }
        else {
            this.notifyDataSetChanged();
        }
    }
    
    private class Item
    {
        public int icon;
        public int id;
        public String text;
        
        public Item(final int id, final String text, final int icon) {
            this.icon = icon;
            this.id = id;
            this.text = text;
        }
        
        public void bind(final DrawerActionCell drawerActionCell) {
            drawerActionCell.setTextAndIcon(this.text, this.icon);
        }
    }
}
