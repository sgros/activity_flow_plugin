// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import org.telegram.messenger.FileLog;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.tgnet.ConnectionsManager;
import java.util.Collection;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Cells.DividerCell;
import android.view.ViewGroup;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Cells.GraySectionCell;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.Cells.LetterSectionCell;
import android.view.View;
import java.util.HashMap;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import java.util.ArrayList;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import org.telegram.ui.Components.RecyclerListView;

public class ContactsAdapter extends SectionsAdapter
{
    private SparseArray<?> checkedMap;
    private int currentAccount;
    private SparseArray<TLRPC.User> ignoreUsers;
    private boolean isAdmin;
    private boolean isChannel;
    private Context mContext;
    private boolean needPhonebook;
    private ArrayList<TLRPC.TL_contact> onlineContacts;
    private int onlyUsers;
    private boolean scrolling;
    private int sortType;
    
    public ContactsAdapter(final Context mContext, final int onlyUsers, final boolean needPhonebook, final SparseArray<TLRPC.User> ignoreUsers, final int n) {
        this.currentAccount = UserConfig.selectedAccount;
        this.mContext = mContext;
        this.onlyUsers = onlyUsers;
        this.needPhonebook = needPhonebook;
        this.ignoreUsers = ignoreUsers;
        final boolean b = true;
        this.isAdmin = (n != 0);
        this.isChannel = (n == 2 && b);
    }
    
    @Override
    public int getCountForSection(int n) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> hashMap;
        if (this.onlyUsers == 2) {
            hashMap = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
        }
        else {
            hashMap = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        }
        ArrayList<String> list;
        if (this.onlyUsers == 2) {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
        }
        else {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        }
        final int onlyUsers = this.onlyUsers;
        final int n2 = 0;
        if (onlyUsers != 0 && !this.isAdmin) {
            if (n < list.size()) {
                final int size = hashMap.get(list.get(n)).size();
                if (n == list.size() - 1) {
                    n = size;
                    if (!this.needPhonebook) {
                        return n;
                    }
                }
                n = size + 1;
                return n;
            }
        }
        else if (n == 0) {
            if (!this.needPhonebook && !this.isAdmin) {
                return 4;
            }
            return 2;
        }
        else if (this.sortType == 2) {
            if (n == 1) {
                if (this.onlineContacts.isEmpty()) {
                    n = n2;
                }
                else {
                    n = this.onlineContacts.size() + 1;
                }
                return n;
            }
        }
        else if (--n < list.size()) {
            final int size2 = hashMap.get(list.get(n)).size();
            if (n == list.size() - 1) {
                n = size2;
                if (!this.needPhonebook) {
                    return n;
                }
            }
            n = size2 + 1;
            return n;
        }
        if (this.needPhonebook) {
            return ContactsController.getInstance(this.currentAccount).phoneBookContacts.size();
        }
        return 0;
    }
    
    @Override
    public Object getItem(int n, final int n2) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> hashMap;
        if (this.onlyUsers == 2) {
            hashMap = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
        }
        else {
            hashMap = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        }
        ArrayList<String> list;
        if (this.onlyUsers == 2) {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
        }
        else {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        }
        if (this.onlyUsers != 0 && !this.isAdmin) {
            if (n < list.size()) {
                final ArrayList<TLRPC.TL_contact> list2 = hashMap.get(list.get(n));
                if (n2 < list2.size()) {
                    return MessagesController.getInstance(this.currentAccount).getUser(list2.get(n2).user_id);
                }
            }
            return null;
        }
        if (n == 0) {
            return null;
        }
        if (this.sortType == 2) {
            if (n == 1) {
                if (n2 < this.onlineContacts.size()) {
                    return MessagesController.getInstance(this.currentAccount).getUser(this.onlineContacts.get(n2).user_id);
                }
                return null;
            }
        }
        else if (--n < list.size()) {
            final ArrayList<TLRPC.TL_contact> list3 = hashMap.get(list.get(n));
            if (n2 < list3.size()) {
                return MessagesController.getInstance(this.currentAccount).getUser(list3.get(n2).user_id);
            }
            return null;
        }
        if (this.needPhonebook) {
            return ContactsController.getInstance(this.currentAccount).phoneBookContacts.get(n2);
        }
        return null;
    }
    
    @Override
    public int getItemViewType(int n, final int n2) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> hashMap;
        if (this.onlyUsers == 2) {
            hashMap = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
        }
        else {
            hashMap = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        }
        ArrayList<String> list;
        if (this.onlyUsers == 2) {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
        }
        else {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        }
        final int onlyUsers = this.onlyUsers;
        final int n3 = 0;
        final int n4 = 0;
        final int n5 = 0;
        if (onlyUsers != 0 && !this.isAdmin) {
            if (n2 < hashMap.get(list.get(n)).size()) {
                n = n5;
            }
            else {
                n = 3;
            }
            return n;
        }
        if (n == 0) {
            if (((this.needPhonebook || this.isAdmin) && n2 == 1) || n2 == 3) {
                return 2;
            }
        }
        else if (this.sortType == 2) {
            if (n == 1) {
                if (n2 < this.onlineContacts.size()) {
                    n = n3;
                }
                else {
                    n = 3;
                }
                return n;
            }
        }
        else if (--n < list.size()) {
            if (n2 < hashMap.get(list.get(n)).size()) {
                n = n4;
            }
            else {
                n = 3;
            }
            return n;
        }
        return 1;
    }
    
    @Override
    public String getLetter(int sectionForPosition) {
        if (this.sortType == 2) {
            return null;
        }
        ArrayList<String> list;
        if (this.onlyUsers == 2) {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
        }
        else {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        }
        if ((sectionForPosition = ((RecyclerListView.SectionsAdapter)this).getSectionForPosition(sectionForPosition)) == -1) {
            sectionForPosition = list.size() - 1;
        }
        if (sectionForPosition > 0 && sectionForPosition <= list.size()) {
            return list.get(sectionForPosition - 1);
        }
        return null;
    }
    
    @Override
    public int getPositionForScrollProgress(final float n) {
        return (int)(((RecyclerListView.SectionsAdapter)this).getItemCount() * n);
    }
    
    @Override
    public int getSectionCount() {
        int size;
        if (this.sortType == 2) {
            size = 1;
        }
        else {
            ArrayList<String> list;
            if (this.onlyUsers == 2) {
                list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
            }
            else {
                list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
            }
            size = list.size();
        }
        int n = size;
        if (this.onlyUsers == 0) {
            n = size + 1;
        }
        int n2 = n;
        if (this.isAdmin) {
            n2 = n + 1;
        }
        final boolean needPhonebook = this.needPhonebook;
        return n2;
    }
    
    @Override
    public View getSectionHeaderView(int n, final View view) {
        if (this.onlyUsers == 2) {
            final HashMap<String, ArrayList<TLRPC.TL_contact>> usersMutualSectionsDict = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
        }
        else {
            final HashMap<String, ArrayList<TLRPC.TL_contact>> usersSectionsDict = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        }
        ArrayList<String> list;
        if (this.onlyUsers == 2) {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
        }
        else {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        }
        Object o = view;
        if (view == null) {
            o = new LetterSectionCell(this.mContext);
        }
        final LetterSectionCell letterSectionCell = (LetterSectionCell)o;
        if (this.sortType == 2) {
            letterSectionCell.setLetter("");
        }
        else if (this.onlyUsers != 0 && !this.isAdmin) {
            if (n < list.size()) {
                letterSectionCell.setLetter(list.get(n));
            }
            else {
                letterSectionCell.setLetter("");
            }
        }
        else if (n == 0) {
            letterSectionCell.setLetter("");
        }
        else if (--n < list.size()) {
            letterSectionCell.setLetter(list.get(n));
        }
        else {
            letterSectionCell.setLetter("");
        }
        return (View)o;
    }
    
    @Override
    public boolean isEnabled(int n, final int n2) {
        HashMap<String, ArrayList<TLRPC.TL_contact>> hashMap;
        if (this.onlyUsers == 2) {
            hashMap = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
        }
        else {
            hashMap = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        }
        ArrayList<String> list;
        if (this.onlyUsers == 2) {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
        }
        else {
            list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        }
        final int onlyUsers = this.onlyUsers;
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = false;
        final boolean b4 = false;
        boolean b5 = false;
        if (onlyUsers != 0 && !this.isAdmin) {
            if (n2 < hashMap.get(list.get(n)).size()) {
                b5 = true;
            }
            return b5;
        }
        if (n != 0) {
            if (this.sortType == 2) {
                if (n == 1) {
                    boolean b6 = b3;
                    if (n2 < this.onlineContacts.size()) {
                        b6 = true;
                    }
                    return b6;
                }
            }
            else if (--n < list.size()) {
                boolean b7 = b4;
                if (n2 < hashMap.get(list.get(n)).size()) {
                    b7 = true;
                }
                return b7;
            }
            return true;
        }
        if (!this.needPhonebook && !this.isAdmin) {
            boolean b8 = b;
            if (n2 != 3) {
                b8 = true;
            }
            return b8;
        }
        boolean b9 = b2;
        if (n2 != 1) {
            b9 = true;
        }
        return b9;
    }
    
    @Override
    public void onBindViewHolder(int sortType, final int n, final ViewHolder viewHolder) {
        final int itemViewType = viewHolder.getItemViewType();
        boolean b = false;
        if (itemViewType != 0) {
            if (itemViewType != 1) {
                if (itemViewType == 2) {
                    final GraySectionCell graySectionCell = (GraySectionCell)viewHolder.itemView;
                    sortType = this.sortType;
                    if (sortType == 0) {
                        graySectionCell.setText(LocaleController.getString("Contacts", 2131559149));
                    }
                    else if (sortType == 1) {
                        graySectionCell.setText(LocaleController.getString("SortedByName", 2131560799));
                    }
                    else {
                        graySectionCell.setText(LocaleController.getString("SortedByLastSeen", 2131560798));
                    }
                }
            }
            else {
                final TextCell textCell = (TextCell)viewHolder.itemView;
                if (sortType == 0) {
                    if (this.needPhonebook) {
                        textCell.setTextAndIcon(LocaleController.getString("InviteFriends", 2131559677), 2131165584, false);
                    }
                    else if (this.isAdmin) {
                        if (this.isChannel) {
                            textCell.setTextAndIcon(LocaleController.getString("ChannelInviteViaLink", 2131558953), 2131165787, false);
                        }
                        else {
                            textCell.setTextAndIcon(LocaleController.getString("InviteToGroupByLink", 2131559688), 2131165787, false);
                        }
                    }
                    else if (n == 0) {
                        textCell.setTextAndIcon(LocaleController.getString("NewGroup", 2131559900), 2131165580, false);
                    }
                    else if (n == 1) {
                        textCell.setTextAndIcon(LocaleController.getString("NewSecretChat", 2131559909), 2131165594, false);
                    }
                    else if (n == 2) {
                        textCell.setTextAndIcon(LocaleController.getString("NewChannel", 2131559898), 2131165568, false);
                    }
                }
                else {
                    final ContactsController.Contact contact = ContactsController.getInstance(this.currentAccount).phoneBookContacts.get(n);
                    if (contact.first_name != null && contact.last_name != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(contact.first_name);
                        sb.append(" ");
                        sb.append(contact.last_name);
                        textCell.setText(sb.toString(), false);
                    }
                    else {
                        final String first_name = contact.first_name;
                        if (first_name != null && contact.last_name == null) {
                            textCell.setText(first_name, false);
                        }
                        else {
                            textCell.setText(contact.last_name, false);
                        }
                    }
                }
            }
        }
        else {
            final UserCell userCell = (UserCell)viewHolder.itemView;
            int avatarPadding;
            if (this.sortType == 2) {
                avatarPadding = 6;
            }
            else {
                avatarPadding = 58;
            }
            userCell.setAvatarPadding(avatarPadding);
            ArrayList<TLRPC.TL_contact> onlineContacts;
            if (this.sortType == 2) {
                onlineContacts = this.onlineContacts;
            }
            else {
                HashMap<String, ArrayList<TLRPC.TL_contact>> hashMap;
                if (this.onlyUsers == 2) {
                    hashMap = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
                }
                else {
                    hashMap = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
                }
                ArrayList<String> list;
                if (this.onlyUsers == 2) {
                    list = ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray;
                }
                else {
                    list = ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
                }
                int n2;
                if (this.onlyUsers != 0 && !this.isAdmin) {
                    n2 = 0;
                }
                else {
                    n2 = 1;
                }
                onlineContacts = hashMap.get(list.get(sortType - n2));
            }
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(onlineContacts.get(n).user_id);
            userCell.setData(user, null, null, 0);
            final SparseArray<?> checkedMap = this.checkedMap;
            if (checkedMap != null) {
                if (checkedMap.indexOfKey(user.id) >= 0) {
                    b = true;
                }
                userCell.setChecked(b, this.scrolling ^ true);
            }
            final SparseArray<TLRPC.User> ignoreUsers = this.ignoreUsers;
            if (ignoreUsers != null) {
                if (ignoreUsers.indexOfKey(user.id) >= 0) {
                    userCell.setAlpha(0.5f);
                }
                else {
                    userCell.setAlpha(1.0f);
                }
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int dp) {
        Object o;
        if (dp != 0) {
            if (dp != 1) {
                if (dp != 2) {
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
                    o = new GraySectionCell(this.mContext);
                }
            }
            else {
                o = new TextCell(this.mContext);
            }
        }
        else {
            o = new UserCell(this.mContext, 58, 1, false);
        }
        return new RecyclerListView.Holder((View)o);
    }
    
    public void setCheckedMap(final SparseArray<?> checkedMap) {
        this.checkedMap = checkedMap;
    }
    
    public void setIsScrolling(final boolean scrolling) {
        this.scrolling = scrolling;
    }
    
    public void setSortType(int i) {
        this.sortType = i;
        if (this.sortType == 2) {
            if (this.onlineContacts == null) {
                this.onlineContacts = new ArrayList<TLRPC.TL_contact>();
                final int clientUserId = UserConfig.getInstance(this.currentAccount).clientUserId;
                this.onlineContacts.addAll(ContactsController.getInstance(this.currentAccount).contacts);
                for (i = 0; i < this.onlineContacts.size(); ++i) {
                    if (this.onlineContacts.get(i).user_id == clientUserId) {
                        this.onlineContacts.remove(i);
                        break;
                    }
                }
            }
            this.sortOnlineContacts();
        }
        else {
            ((RecyclerListView.SectionsAdapter)this).notifyDataSetChanged();
        }
    }
    
    public void sortOnlineContacts() {
        if (this.onlineContacts == null) {
            return;
        }
        try {
            Collections.sort(this.onlineContacts, new _$$Lambda$ContactsAdapter$AjIuF4bNE_A90essgyL0wfJ8HaU(MessagesController.getInstance(this.currentAccount), ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()));
            ((RecyclerListView.SectionsAdapter)this).notifyDataSetChanged();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
}
