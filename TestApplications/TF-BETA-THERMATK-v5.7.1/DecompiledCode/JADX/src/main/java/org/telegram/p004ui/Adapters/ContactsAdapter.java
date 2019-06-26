package org.telegram.p004ui.Adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.Cells.DividerCell;
import org.telegram.p004ui.Cells.GraySectionCell;
import org.telegram.p004ui.Cells.LetterSectionCell;
import org.telegram.p004ui.Cells.TextCell;
import org.telegram.p004ui.Cells.UserCell;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SectionsAdapter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.TL_contact;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.Adapters.ContactsAdapter */
public class ContactsAdapter extends SectionsAdapter {
    private SparseArray<?> checkedMap;
    private int currentAccount = UserConfig.selectedAccount;
    private SparseArray<User> ignoreUsers;
    private boolean isAdmin;
    private boolean isChannel;
    private Context mContext;
    private boolean needPhonebook;
    private ArrayList<TL_contact> onlineContacts;
    private int onlyUsers;
    private boolean scrolling;
    private int sortType;

    public ContactsAdapter(Context context, int i, boolean z, SparseArray<User> sparseArray, int i2) {
        this.mContext = context;
        this.onlyUsers = i;
        this.needPhonebook = z;
        this.ignoreUsers = sparseArray;
        boolean z2 = true;
        this.isAdmin = i2 != 0;
        if (i2 != 2) {
            z2 = false;
        }
        this.isChannel = z2;
    }

    public void setSortType(int i) {
        this.sortType = i;
        if (this.sortType == 2) {
            if (this.onlineContacts == null) {
                this.onlineContacts = new ArrayList();
                i = UserConfig.getInstance(this.currentAccount).clientUserId;
                this.onlineContacts.addAll(ContactsController.getInstance(this.currentAccount).contacts);
                int size = this.onlineContacts.size();
                for (int i2 = 0; i2 < size; i2++) {
                    if (((TL_contact) this.onlineContacts.get(i2)).user_id == i) {
                        this.onlineContacts.remove(i2);
                        break;
                    }
                }
            }
            sortOnlineContacts();
            return;
        }
        notifyDataSetChanged();
    }

    public void sortOnlineContacts() {
        if (this.onlineContacts != null) {
            try {
                int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                Collections.sort(this.onlineContacts, new C2233-$$Lambda$ContactsAdapter$AjIuF4bNE-A90essgyL0wfJ8HaU(MessagesController.getInstance(this.currentAccount), currentTime));
                notifyDataSetChanged();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0049 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0054 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x005d A:{SKIP} */
    static /* synthetic */ int lambda$sortOnlineContacts$0(org.telegram.messenger.MessagesController r2, int r3, org.telegram.tgnet.TLRPC.TL_contact r4, org.telegram.tgnet.TLRPC.TL_contact r5) {
        /*
        r5 = r5.user_id;
        r5 = java.lang.Integer.valueOf(r5);
        r5 = r2.getUser(r5);
        r4 = r4.user_id;
        r4 = java.lang.Integer.valueOf(r4);
        r2 = r2.getUser(r4);
        r4 = 50000; // 0xc350 float:7.0065E-41 double:2.47033E-319;
        r0 = 0;
        if (r5 == 0) goto L_0x0028;
    L_0x001a:
        r1 = r5.self;
        if (r1 == 0) goto L_0x0021;
    L_0x001e:
        r5 = r3 + r4;
        goto L_0x0029;
    L_0x0021:
        r5 = r5.status;
        if (r5 == 0) goto L_0x0028;
    L_0x0025:
        r5 = r5.expires;
        goto L_0x0029;
    L_0x0028:
        r5 = 0;
    L_0x0029:
        if (r2 == 0) goto L_0x0039;
    L_0x002b:
        r1 = r2.self;
        if (r1 == 0) goto L_0x0032;
    L_0x002f:
        r2 = r3 + r4;
        goto L_0x003a;
    L_0x0032:
        r2 = r2.status;
        if (r2 == 0) goto L_0x0039;
    L_0x0036:
        r2 = r2.expires;
        goto L_0x003a;
    L_0x0039:
        r2 = 0;
    L_0x003a:
        r3 = -1;
        r4 = 1;
        if (r5 <= 0) goto L_0x0047;
    L_0x003e:
        if (r2 <= 0) goto L_0x0047;
    L_0x0040:
        if (r5 <= r2) goto L_0x0043;
    L_0x0042:
        return r4;
    L_0x0043:
        if (r5 >= r2) goto L_0x0046;
    L_0x0045:
        return r3;
    L_0x0046:
        return r0;
    L_0x0047:
        if (r5 >= 0) goto L_0x0052;
    L_0x0049:
        if (r2 >= 0) goto L_0x0052;
    L_0x004b:
        if (r5 <= r2) goto L_0x004e;
    L_0x004d:
        return r4;
    L_0x004e:
        if (r5 >= r2) goto L_0x0051;
    L_0x0050:
        return r3;
    L_0x0051:
        return r0;
    L_0x0052:
        if (r5 >= 0) goto L_0x0056;
    L_0x0054:
        if (r2 > 0) goto L_0x005a;
    L_0x0056:
        if (r5 != 0) goto L_0x005b;
    L_0x0058:
        if (r2 == 0) goto L_0x005b;
    L_0x005a:
        return r3;
    L_0x005b:
        if (r2 >= 0) goto L_0x005f;
    L_0x005d:
        if (r5 > 0) goto L_0x0063;
    L_0x005f:
        if (r2 != 0) goto L_0x0064;
    L_0x0061:
        if (r5 == 0) goto L_0x0064;
    L_0x0063:
        return r4;
    L_0x0064:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.Adapters.ContactsAdapter.lambda$sortOnlineContacts$0(org.telegram.messenger.MessagesController, int, org.telegram.tgnet.TLRPC$TL_contact, org.telegram.tgnet.TLRPC$TL_contact):int");
    }

    public void setCheckedMap(SparseArray<?> sparseArray) {
        this.checkedMap = sparseArray;
    }

    public void setIsScrolling(boolean z) {
        this.scrolling = z;
    }

    public Object getItem(int i, int i2) {
        HashMap hashMap = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict : ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        ArrayList arrayList = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        ArrayList arrayList2;
        if (this.onlyUsers != 0 && !this.isAdmin) {
            if (i < arrayList.size()) {
                arrayList2 = (ArrayList) hashMap.get(arrayList.get(i));
                if (i2 < arrayList2.size()) {
                    return MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((TL_contact) arrayList2.get(i2)).user_id));
                }
            }
            return null;
        } else if (i == 0) {
            return null;
        } else {
            if (this.sortType != 2) {
                i--;
                if (i < arrayList.size()) {
                    arrayList2 = (ArrayList) hashMap.get(arrayList.get(i));
                    if (i2 < arrayList2.size()) {
                        return MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((TL_contact) arrayList2.get(i2)).user_id));
                    }
                    return null;
                }
            } else if (i == 1) {
                if (i2 < this.onlineContacts.size()) {
                    return MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((TL_contact) this.onlineContacts.get(i2)).user_id));
                }
                return null;
            }
            if (this.needPhonebook) {
                return ContactsController.getInstance(this.currentAccount).phoneBookContacts.get(i2);
            }
            return null;
        }
    }

    public boolean isEnabled(int i, int i2) {
        HashMap hashMap = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict : ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        ArrayList arrayList = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        boolean z = false;
        if (this.onlyUsers != 0 && !this.isAdmin) {
            if (i2 < ((ArrayList) hashMap.get(arrayList.get(i))).size()) {
                z = true;
            }
            return z;
        } else if (i != 0) {
            if (this.sortType != 2) {
                i--;
                if (i < arrayList.size()) {
                    if (i2 < ((ArrayList) hashMap.get(arrayList.get(i))).size()) {
                        z = true;
                    }
                    return z;
                }
            } else if (i == 1) {
                if (i2 < this.onlineContacts.size()) {
                    z = true;
                }
                return z;
            }
            return true;
        } else if (this.needPhonebook || this.isAdmin) {
            if (i2 != 1) {
                z = true;
            }
            return z;
        } else {
            if (i2 != 3) {
                z = true;
            }
            return z;
        }
    }

    public int getSectionCount() {
        int i;
        if (this.sortType == 2) {
            i = 1;
        } else {
            i = (this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray).size();
        }
        if (this.onlyUsers == 0) {
            i++;
        }
        if (this.isAdmin) {
            i++;
        }
        boolean z = this.needPhonebook;
        return i;
    }

    public int getCountForSection(int i) {
        HashMap hashMap = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict : ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        ArrayList arrayList = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        int i2 = 0;
        int size;
        if (this.onlyUsers == 0 || this.isAdmin) {
            if (i == 0) {
                if (this.needPhonebook || this.isAdmin) {
                    return 2;
                }
                return 4;
            } else if (this.sortType != 2) {
                i--;
                if (i < arrayList.size()) {
                    size = ((ArrayList) hashMap.get(arrayList.get(i))).size();
                    if (i != arrayList.size() - 1 || this.needPhonebook) {
                        size++;
                    }
                    return size;
                }
            } else if (i == 1) {
                if (!this.onlineContacts.isEmpty()) {
                    i2 = this.onlineContacts.size() + 1;
                }
                return i2;
            }
        } else if (i < arrayList.size()) {
            size = ((ArrayList) hashMap.get(arrayList.get(i))).size();
            if (i != arrayList.size() - 1 || this.needPhonebook) {
                size++;
            }
            return size;
        }
        if (this.needPhonebook) {
            return ContactsController.getInstance(this.currentAccount).phoneBookContacts.size();
        }
        return 0;
    }

    public View getSectionHeaderView(int i, View view) {
        HashMap hashMap;
        if (this.onlyUsers == 2) {
            hashMap = ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict;
        } else {
            hashMap = ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        }
        ArrayList arrayList = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        if (view == null) {
            view = new LetterSectionCell(this.mContext);
        }
        LetterSectionCell letterSectionCell = (LetterSectionCell) view;
        String str = "";
        if (this.sortType == 2) {
            letterSectionCell.setLetter(str);
        } else if (this.onlyUsers == 0 || this.isAdmin) {
            if (i == 0) {
                letterSectionCell.setLetter(str);
            } else {
                i--;
                if (i < arrayList.size()) {
                    letterSectionCell.setLetter((String) arrayList.get(i));
                } else {
                    letterSectionCell.setLetter(str);
                }
            }
        } else if (i < arrayList.size()) {
            letterSectionCell.setLetter((String) arrayList.get(i));
        } else {
            letterSectionCell.setLetter(str);
        }
        return view;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View userCell;
        if (i == 0) {
            userCell = new UserCell(this.mContext, 58, 1, false);
        } else if (i == 1) {
            userCell = new TextCell(this.mContext);
        } else if (i != 2) {
            userCell = new DividerCell(this.mContext);
            float f = 28.0f;
            i = AndroidUtilities.m26dp(LocaleController.isRTL ? 28.0f : 72.0f);
            int dp = AndroidUtilities.m26dp(8.0f);
            if (LocaleController.isRTL) {
                f = 72.0f;
            }
            userCell.setPadding(i, dp, AndroidUtilities.m26dp(f), AndroidUtilities.m26dp(8.0f));
        } else {
            userCell = new GraySectionCell(this.mContext);
        }
        return new Holder(userCell);
    }

    public void onBindViewHolder(int i, int i2, ViewHolder viewHolder) {
        int itemViewType = viewHolder.getItemViewType();
        boolean z = false;
        if (itemViewType == 0) {
            ArrayList arrayList;
            UserCell userCell = (UserCell) viewHolder.itemView;
            userCell.setAvatarPadding(this.sortType == 2 ? 6 : 58);
            if (this.sortType == 2) {
                arrayList = this.onlineContacts;
            } else {
                HashMap hashMap = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict : ContactsController.getInstance(this.currentAccount).usersSectionsDict;
                ArrayList arrayList2 = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
                int i3 = (this.onlyUsers == 0 || this.isAdmin) ? 1 : 0;
                arrayList = (ArrayList) hashMap.get(arrayList2.get(i - i3));
            }
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((TL_contact) arrayList.get(i2)).user_id));
            userCell.setData(user, null, null, 0);
            SparseArray sparseArray = this.checkedMap;
            if (sparseArray != null) {
                if (sparseArray.indexOfKey(user.f534id) >= 0) {
                    z = true;
                }
                userCell.setChecked(z, this.scrolling ^ 1);
            }
            sparseArray = this.ignoreUsers;
            if (sparseArray == null) {
                return;
            }
            if (sparseArray.indexOfKey(user.f534id) >= 0) {
                userCell.setAlpha(0.5f);
            } else {
                userCell.setAlpha(1.0f);
            }
        } else if (itemViewType == 1) {
            TextCell textCell = (TextCell) viewHolder.itemView;
            if (i != 0) {
                Contact contact = (Contact) ContactsController.getInstance(this.currentAccount).phoneBookContacts.get(i2);
                if (contact.first_name == null || contact.last_name == null) {
                    String str = contact.first_name;
                    if (str == null || contact.last_name != null) {
                        textCell.setText(contact.last_name, false);
                        return;
                    } else {
                        textCell.setText(str, false);
                        return;
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(contact.first_name);
                stringBuilder.append(" ");
                stringBuilder.append(contact.last_name);
                textCell.setText(stringBuilder.toString(), false);
            } else if (this.needPhonebook) {
                textCell.setTextAndIcon(LocaleController.getString("InviteFriends", C1067R.string.InviteFriends), C1067R.C1065drawable.menu_invite, false);
            } else if (this.isAdmin) {
                if (this.isChannel) {
                    textCell.setTextAndIcon(LocaleController.getString("ChannelInviteViaLink", C1067R.string.ChannelInviteViaLink), C1067R.C1065drawable.profile_link, false);
                } else {
                    textCell.setTextAndIcon(LocaleController.getString("InviteToGroupByLink", C1067R.string.InviteToGroupByLink), C1067R.C1065drawable.profile_link, false);
                }
            } else if (i2 == 0) {
                textCell.setTextAndIcon(LocaleController.getString("NewGroup", C1067R.string.NewGroup), C1067R.C1065drawable.menu_groups, false);
            } else if (i2 == 1) {
                textCell.setTextAndIcon(LocaleController.getString("NewSecretChat", C1067R.string.NewSecretChat), C1067R.C1065drawable.menu_secret, false);
            } else if (i2 == 2) {
                textCell.setTextAndIcon(LocaleController.getString("NewChannel", C1067R.string.NewChannel), C1067R.C1065drawable.menu_broadcast, false);
            }
        } else if (itemViewType == 2) {
            GraySectionCell graySectionCell = (GraySectionCell) viewHolder.itemView;
            i2 = this.sortType;
            if (i2 == 0) {
                graySectionCell.setText(LocaleController.getString("Contacts", C1067R.string.Contacts));
            } else if (i2 == 1) {
                graySectionCell.setText(LocaleController.getString("SortedByName", C1067R.string.SortedByName));
            } else {
                graySectionCell.setText(LocaleController.getString("SortedByLastSeen", C1067R.string.SortedByLastSeen));
            }
        }
    }

    public int getItemViewType(int i, int i2) {
        HashMap hashMap = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).usersMutualSectionsDict : ContactsController.getInstance(this.currentAccount).usersSectionsDict;
        ArrayList arrayList = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        int i3 = 0;
        if (this.onlyUsers == 0 || this.isAdmin) {
            if (i == 0) {
                if (((this.needPhonebook || this.isAdmin) && i2 == 1) || i2 == 3) {
                    return 2;
                }
            } else if (this.sortType != 2) {
                i--;
                if (i < arrayList.size()) {
                    if (i2 >= ((ArrayList) hashMap.get(arrayList.get(i))).size()) {
                        i3 = 3;
                    }
                    return i3;
                }
            } else if (i == 1) {
                if (i2 >= this.onlineContacts.size()) {
                    i3 = 3;
                }
                return i3;
            }
            return 1;
        }
        if (i2 >= ((ArrayList) hashMap.get(arrayList.get(i))).size()) {
            i3 = 3;
        }
        return i3;
    }

    public String getLetter(int i) {
        if (this.sortType == 2) {
            return null;
        }
        ArrayList arrayList = this.onlyUsers == 2 ? ContactsController.getInstance(this.currentAccount).sortedUsersMutualSectionsArray : ContactsController.getInstance(this.currentAccount).sortedUsersSectionsArray;
        i = getSectionForPosition(i);
        if (i == -1) {
            i = arrayList.size() - 1;
        }
        if (i <= 0 || i > arrayList.size()) {
            return null;
        }
        return (String) arrayList.get(i - 1);
    }

    public int getPositionForScrollProgress(float f) {
        return (int) (((float) getItemCount()) * f);
    }
}