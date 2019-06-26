// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Cells.DividerCell;
import android.view.ViewGroup;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.ui.Cells.UserCell;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.Cells.LetterSectionCell;
import android.view.View;
import java.util.HashMap;
import java.util.ArrayList;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import org.telegram.ui.Components.RecyclerListView;

public class PhonebookAdapter extends SectionsAdapter
{
    private int currentAccount;
    private Context mContext;
    
    public PhonebookAdapter(final Context mContext) {
        this.currentAccount = UserConfig.selectedAccount;
        this.mContext = mContext;
    }
    
    @Override
    public int getCountForSection(final int index) {
        final HashMap<String, ArrayList<Object>> phoneBookSectionsDict = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
        final ArrayList<String> phoneBookSectionsArray = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
        if (index < phoneBookSectionsArray.size()) {
            int size = phoneBookSectionsDict.get(phoneBookSectionsArray.get(index)).size();
            if (index != phoneBookSectionsArray.size() - 1) {
                ++size;
            }
            return size;
        }
        return 0;
    }
    
    @Override
    public Object getItem(final int index, final int index2) {
        final HashMap<String, ArrayList<Object>> phoneBookSectionsDict = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
        final ArrayList<String> phoneBookSectionsArray = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
        if (index < phoneBookSectionsArray.size()) {
            final ArrayList<Object> list = phoneBookSectionsDict.get(phoneBookSectionsArray.get(index));
            if (index2 < list.size()) {
                return list.get(index2);
            }
        }
        return null;
    }
    
    @Override
    public int getItemViewType(int index, final int n) {
        if (n < ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict.get(ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.get(index)).size()) {
            index = 0;
        }
        else {
            index = 1;
        }
        return index;
    }
    
    @Override
    public String getLetter(int sectionForPosition) {
        final ArrayList<String> phoneBookSectionsArray = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
        if ((sectionForPosition = ((RecyclerListView.SectionsAdapter)this).getSectionForPosition(sectionForPosition)) == -1) {
            sectionForPosition = phoneBookSectionsArray.size() - 1;
        }
        if (sectionForPosition >= 0 && sectionForPosition < phoneBookSectionsArray.size()) {
            return phoneBookSectionsArray.get(sectionForPosition);
        }
        return null;
    }
    
    @Override
    public int getPositionForScrollProgress(final float n) {
        return (int)(((RecyclerListView.SectionsAdapter)this).getItemCount() * n);
    }
    
    @Override
    public int getSectionCount() {
        return ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.size();
    }
    
    @Override
    public View getSectionHeaderView(final int index, final View view) {
        final HashMap<String, ArrayList<Object>> phoneBookSectionsDict = ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict;
        final ArrayList<String> phoneBookSectionsArray = ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray;
        Object o = view;
        if (view == null) {
            o = new LetterSectionCell(this.mContext);
        }
        final LetterSectionCell letterSectionCell = (LetterSectionCell)o;
        if (index < phoneBookSectionsArray.size()) {
            letterSectionCell.setLetter(phoneBookSectionsArray.get(index));
        }
        else {
            letterSectionCell.setLetter("");
        }
        return (View)o;
    }
    
    @Override
    public boolean isEnabled(final int index, final int n) {
        return n < ContactsController.getInstance(this.currentAccount).phoneBookSectionsDict.get(ContactsController.getInstance(this.currentAccount).phoneBookSectionsArray.get(index)).size();
    }
    
    @Override
    public void onBindViewHolder(final int n, final int n2, final ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == 0) {
            final UserCell userCell = (UserCell)viewHolder.itemView;
            final Object item = this.getItem(n, n2);
            TLRPC.User user;
            if (item instanceof ContactsController.Contact) {
                final ContactsController.Contact contact = (ContactsController.Contact)item;
                user = contact.user;
                if (user == null) {
                    userCell.setCurrentId(contact.contact_id);
                    final String formatName = ContactsController.formatName(contact.first_name, contact.last_name);
                    String format;
                    if (contact.phones.isEmpty()) {
                        format = "";
                    }
                    else {
                        format = PhoneFormat.getInstance().format(contact.phones.get(0));
                    }
                    userCell.setData(null, formatName, format, 0);
                    user = null;
                }
            }
            else {
                user = (TLRPC.User)item;
            }
            if (user != null) {
                final PhoneFormat instance = PhoneFormat.getInstance();
                final StringBuilder sb = new StringBuilder();
                sb.append("+");
                sb.append(user.phone);
                userCell.setData(user, null, instance.format(sb.toString()), 0);
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int dp) {
        Object o;
        if (dp != 0) {
            o = new DividerCell(this.mContext);
            final boolean isRTL = LocaleController.isRTL;
            final float n = 28.0f;
            float n2;
            if (isRTL) {
                n2 = 28.0f;
            }
            else {
                n2 = 72.0f;
            }
            final int dp2 = AndroidUtilities.dp(n2);
            dp = AndroidUtilities.dp(8.0f);
            float n3 = n;
            if (LocaleController.isRTL) {
                n3 = 72.0f;
            }
            ((View)o).setPadding(dp2, dp, AndroidUtilities.dp(n3), AndroidUtilities.dp(8.0f));
        }
        else {
            o = new UserCell(this.mContext, 58, 1, false);
            ((UserCell)o).setNameTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        }
        return new RecyclerListView.Holder((View)o);
    }
}
