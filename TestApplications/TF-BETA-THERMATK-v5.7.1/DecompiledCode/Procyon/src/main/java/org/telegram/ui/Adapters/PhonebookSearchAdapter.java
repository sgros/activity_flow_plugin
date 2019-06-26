// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import java.util.TimerTask;
import org.telegram.messenger.FileLog;
import android.view.View;
import android.view.ViewGroup;
import org.telegram.tgnet.TLObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.ui.Cells.UserCell;
import java.util.Collection;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import android.util.SparseBooleanArray;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import java.util.Timer;
import java.util.ArrayList;
import android.content.Context;
import org.telegram.ui.Components.RecyclerListView;

public class PhonebookSearchAdapter extends SelectionAdapter
{
    private Context mContext;
    private ArrayList<Object> searchResult;
    private ArrayList<CharSequence> searchResultNames;
    private Timer searchTimer;
    
    public PhonebookSearchAdapter(final Context mContext) {
        this.searchResult = new ArrayList<Object>();
        this.searchResultNames = new ArrayList<CharSequence>();
        this.mContext = mContext;
    }
    
    private void processSearch(final String s) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$PhonebookSearchAdapter$KOELxq6F75LZaR2Hw7LDqS_qxfw(this, s));
    }
    
    private void updateSearchResults(final String s, final ArrayList<Object> list, final ArrayList<CharSequence> list2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$PhonebookSearchAdapter$5E7mSLYtYVP_MgRNf26lnHW5RXo(this, s, list, list2));
    }
    
    public Object getItem(final int index) {
        return this.searchResult.get(index);
    }
    
    @Override
    public int getItemCount() {
        return this.searchResult.size();
    }
    
    @Override
    public int getItemViewType(final int n) {
        return 0;
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        return true;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
        if (viewHolder.getItemViewType() == 0) {
            final UserCell userCell = (UserCell)viewHolder.itemView;
            final Object item = this.getItem(n);
            TLRPC.User user;
            if (item instanceof ContactsController.Contact) {
                final ContactsController.Contact contact = (ContactsController.Contact)item;
                user = contact.user;
                if (user == null) {
                    userCell.setCurrentId(contact.contact_id);
                    final CharSequence charSequence = this.searchResultNames.get(n);
                    String format;
                    if (contact.phones.isEmpty()) {
                        format = "";
                    }
                    else {
                        format = PhoneFormat.getInstance().format(contact.phones.get(0));
                    }
                    userCell.setData(null, charSequence, format, 0);
                    user = null;
                }
            }
            else {
                user = (TLRPC.User)item;
            }
            if (user != null) {
                final CharSequence charSequence2 = this.searchResultNames.get(n);
                final PhoneFormat instance = PhoneFormat.getInstance();
                final StringBuilder sb = new StringBuilder();
                sb.append("+");
                sb.append(user.phone);
                userCell.setData(user, charSequence2, instance.format(sb.toString()), 0);
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        final UserCell userCell = new UserCell(this.mContext, 8, 0, false);
        userCell.setNameTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        return new RecyclerListView.Holder((View)userCell);
    }
    
    protected void onUpdateSearchResults(final String s) {
    }
    
    public void search(final String s) {
        try {
            if (this.searchTimer != null) {
                this.searchTimer.cancel();
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        if (s == null) {
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.notifyDataSetChanged();
        }
        else {
            (this.searchTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        PhonebookSearchAdapter.this.searchTimer.cancel();
                        PhonebookSearchAdapter.this.searchTimer = null;
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    PhonebookSearchAdapter.this.processSearch(s);
                }
            }, 200L, 300L);
        }
    }
}
