// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Adapters;

import java.util.TimerTask;
import android.view.View;
import org.telegram.ui.Cells.GraySectionCell;
import android.view.ViewGroup;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.messenger.FileLog;
import android.text.style.ForegroundColorSpan;
import org.telegram.ui.ActionBar.Theme;
import android.text.SpannableStringBuilder;
import java.util.Collection;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.LocaleController;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import java.util.HashMap;
import java.util.Timer;
import org.telegram.tgnet.TLObject;
import java.util.ArrayList;
import android.content.Context;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import org.telegram.ui.Components.RecyclerListView;

public class SearchAdapter extends SelectionAdapter
{
    private boolean allowBots;
    private boolean allowChats;
    private boolean allowUsernameSearch;
    private int channelId;
    private SparseArray<?> checkedMap;
    private SparseArray<TLRPC.User> ignoreUsers;
    private Context mContext;
    private boolean onlyMutual;
    private SearchAdapterHelper searchAdapterHelper;
    private ArrayList<TLObject> searchResult;
    private ArrayList<CharSequence> searchResultNames;
    private Timer searchTimer;
    private boolean useUserCell;
    
    public SearchAdapter(final Context mContext, final SparseArray<TLRPC.User> ignoreUsers, final boolean allowUsernameSearch, final boolean onlyMutual, final boolean allowChats, final boolean allowBots, final int channelId) {
        this.searchResult = new ArrayList<TLObject>();
        this.searchResultNames = new ArrayList<CharSequence>();
        this.mContext = mContext;
        this.ignoreUsers = ignoreUsers;
        this.onlyMutual = onlyMutual;
        this.allowUsernameSearch = allowUsernameSearch;
        this.allowChats = allowChats;
        this.allowBots = allowBots;
        this.channelId = channelId;
        (this.searchAdapterHelper = new SearchAdapterHelper(true)).setDelegate((SearchAdapterHelper.SearchAdapterHelperDelegate)new SearchAdapterHelper.SearchAdapterHelperDelegate() {
            @Override
            public SparseArray<TLRPC.User> getExcludeUsers() {
                return SearchAdapter.this.ignoreUsers;
            }
            
            @Override
            public void onDataSetChanged() {
                SearchAdapter.this.notifyDataSetChanged();
            }
            
            @Override
            public void onSetHashtags(final ArrayList<HashtagObject> list, final HashMap<String, HashtagObject> hashMap) {
            }
        });
    }
    
    private void processSearch(final String s) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$SearchAdapter$orWB0TdKMjyMiCeh3w5NvcjUVZU(this, s));
    }
    
    private void updateSearchResults(final ArrayList<TLObject> list, final ArrayList<CharSequence> list2) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$SearchAdapter$6uwOh4k7ujlooXYRN4wF38fqlek(this, list, list2));
    }
    
    public TLObject getItem(final int index) {
        final int size = this.searchResult.size();
        final int size2 = this.searchAdapterHelper.getGlobalSearch().size();
        if (index >= 0 && index < size) {
            return this.searchResult.get(index);
        }
        if (index > size && index <= size2 + size) {
            return this.searchAdapterHelper.getGlobalSearch().get(index - size - 1);
        }
        return null;
    }
    
    @Override
    public int getItemCount() {
        final int size = this.searchResult.size();
        final int size2 = this.searchAdapterHelper.getGlobalSearch().size();
        int n = size;
        if (size2 != 0) {
            n = size + (size2 + 1);
        }
        return n;
    }
    
    @Override
    public int getItemViewType(final int n) {
        if (n == this.searchResult.size()) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean isEnabled(final ViewHolder viewHolder) {
        return viewHolder.getItemViewType() == 0;
    }
    
    public boolean isGlobalSearch(final int n) {
        final int size = this.searchResult.size();
        final int size2 = this.searchAdapterHelper.getGlobalSearch().size();
        return (n < 0 || n >= size) && (n > size && n <= size2 + size);
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
        if (viewHolder.getItemViewType() == 0) {
            final TLObject item = this.getItem(index);
            if (item != null) {
                final boolean b = item instanceof TLRPC.User;
                final boolean b2 = false;
                CharSequence str;
                int n;
                if (b) {
                    final TLRPC.User user = (TLRPC.User)item;
                    str = user.username;
                    n = user.id;
                }
                else if (item instanceof TLRPC.Chat) {
                    final TLRPC.Chat chat = (TLRPC.Chat)item;
                    str = chat.username;
                    n = chat.id;
                }
                else {
                    str = null;
                    n = 0;
                }
                final int size = this.searchResult.size();
                boolean b3 = true;
                Object o = null;
                Label_0388: {
                    if (index < size) {
                        o = this.searchResultNames.get(index);
                        if (o != null && str != null && ((String)str).length() > 0) {
                            final String string = ((CharSequence)o).toString();
                            final StringBuilder sb = new StringBuilder();
                            sb.append("@");
                            sb.append((String)str);
                            if (string.startsWith(sb.toString())) {
                                str = null;
                                break Label_0388;
                            }
                        }
                        final String s = null;
                        str = (CharSequence)o;
                        o = s;
                    }
                    else if (index > this.searchResult.size() && str != null) {
                        String str2;
                        final String s2 = str2 = this.searchAdapterHelper.getLastFoundUsername();
                        if (s2.startsWith("@")) {
                            str2 = s2.substring(1);
                        }
                        try {
                            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                            spannableStringBuilder.append((CharSequence)"@");
                            spannableStringBuilder.append(str);
                            int index2 = ((String)str).toLowerCase().indexOf(str2);
                            if (index2 != -1) {
                                int length = str2.length();
                                if (index2 == 0) {
                                    ++length;
                                }
                                else {
                                    ++index2;
                                }
                                spannableStringBuilder.setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), index2, length + index2, 33);
                            }
                            str = null;
                            o = spannableStringBuilder;
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                            final String s3 = null;
                            o = str;
                            str = s3;
                        }
                    }
                    else {
                        str = (CharSequence)(o = null);
                    }
                }
                if (this.useUserCell) {
                    final UserCell userCell = (UserCell)viewHolder.itemView;
                    userCell.setData(item, str, (CharSequence)o, 0);
                    final SparseArray<?> checkedMap = this.checkedMap;
                    if (checkedMap != null) {
                        if (checkedMap.indexOfKey(n) < 0) {
                            b3 = false;
                        }
                        userCell.setChecked(b3, false);
                    }
                }
                else {
                    final ProfileSearchCell profileSearchCell = (ProfileSearchCell)viewHolder.itemView;
                    profileSearchCell.setData(item, null, str, (CharSequence)o, false, false);
                    boolean useSeparator = b2;
                    if (index != this.getItemCount() - 1) {
                        useSeparator = b2;
                        if (index != this.searchResult.size() - 1) {
                            useSeparator = true;
                        }
                    }
                    profileSearchCell.useSeparator = useSeparator;
                }
            }
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        Object o;
        if (n != 0) {
            o = new GraySectionCell(this.mContext);
            ((GraySectionCell)o).setText(LocaleController.getString("GlobalSearch", 2131559594));
        }
        else if (this.useUserCell) {
            final UserCell userCell = (UserCell)(o = new UserCell(this.mContext, 1, 1, (boolean)(0 != 0)));
            if (this.checkedMap != null) {
                userCell.setChecked(false, false);
                o = userCell;
            }
        }
        else {
            o = new ProfileSearchCell(this.mContext);
        }
        return new RecyclerListView.Holder((View)o);
    }
    
    public void searchDialogs(final String s) {
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
            if (this.allowUsernameSearch) {
                this.searchAdapterHelper.queryServerSearch(null, true, this.allowChats, this.allowBots, true, this.channelId, 0);
            }
            this.notifyDataSetChanged();
        }
        else {
            (this.searchTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        SearchAdapter.this.searchTimer.cancel();
                        SearchAdapter.this.searchTimer = null;
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    SearchAdapter.this.processSearch(s);
                }
            }, 200L, 300L);
        }
    }
    
    public void setCheckedMap(final SparseArray<?> checkedMap) {
        this.checkedMap = checkedMap;
    }
    
    public void setUseUserCell(final boolean useUserCell) {
        this.useUserCell = useUserCell;
    }
}
