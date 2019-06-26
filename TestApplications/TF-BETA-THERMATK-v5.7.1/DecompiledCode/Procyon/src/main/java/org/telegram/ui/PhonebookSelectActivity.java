// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import java.io.File;
import android.net.Uri;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ContactsController;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.LetterSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.AndroidUtilities;
import android.widget.FrameLayout;
import android.text.TextUtils;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.ui.Adapters.PhonebookSearchAdapter;
import org.telegram.ui.Adapters.PhonebookAdapter;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PhonebookSelectActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int search_button = 0;
    private PhonebookSelectActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private RecyclerListView listView;
    private PhonebookAdapter listViewAdapter;
    private PhonebookSearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("SelectContact", 2131560680));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PhonebookSelectActivity.this.finishFragment();
                }
            }
        });
        super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                PhonebookSelectActivity.this.searchListViewAdapter.search(null);
                PhonebookSelectActivity.this.searching = false;
                PhonebookSelectActivity.this.searchWas = false;
                PhonebookSelectActivity.this.listView.setAdapter(PhonebookSelectActivity.this.listViewAdapter);
                PhonebookSelectActivity.this.listView.setSectionsType(1);
                ((RecyclerListView.SectionsAdapter)PhonebookSelectActivity.this.listViewAdapter).notifyDataSetChanged();
                PhonebookSelectActivity.this.listView.setFastScrollVisible(true);
                PhonebookSelectActivity.this.listView.setVerticalScrollBarEnabled(false);
                PhonebookSelectActivity.this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
            }
            
            @Override
            public void onSearchExpand() {
                PhonebookSelectActivity.this.searching = true;
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                if (PhonebookSelectActivity.this.searchListViewAdapter == null) {
                    return;
                }
                final String string = editText.getText().toString();
                if (string.length() != 0) {
                    PhonebookSelectActivity.this.searchWas = true;
                }
                PhonebookSelectActivity.this.searchListViewAdapter.search(string);
            }
        }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        this.searchListViewAdapter = new PhonebookSearchAdapter(context) {
            @Override
            protected void onUpdateSearchResults(final String s) {
                if (!TextUtils.isEmpty((CharSequence)s) && PhonebookSelectActivity.this.listView != null && PhonebookSelectActivity.this.listView.getAdapter() != PhonebookSelectActivity.this.searchListViewAdapter) {
                    PhonebookSelectActivity.this.listView.setAdapter(PhonebookSelectActivity.this.searchListViewAdapter);
                    PhonebookSelectActivity.this.listView.setSectionsType(0);
                    PhonebookSelectActivity.this.searchListViewAdapter.notifyDataSetChanged();
                    PhonebookSelectActivity.this.listView.setFastScrollVisible(false);
                    PhonebookSelectActivity.this.listView.setVerticalScrollBarEnabled(true);
                    PhonebookSelectActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                }
            }
        };
        this.listViewAdapter = new PhonebookAdapter(context) {
            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                if (PhonebookSelectActivity.this.listView.getAdapter() == this) {
                    PhonebookSelectActivity.this.listView.setFastScrollVisible(super.getItemCount() != 0);
                }
            }
        };
        super.fragmentView = (View)new FrameLayout(context) {
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if (PhonebookSelectActivity.this.listView.getAdapter() == PhonebookSelectActivity.this.listViewAdapter) {
                    if (PhonebookSelectActivity.this.emptyView.getVisibility() == 0) {
                        PhonebookSelectActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(74.0f));
                    }
                }
                else {
                    PhonebookSelectActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(0.0f));
                }
            }
        };
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.emptyView = new EmptyTextProgressView(context)).setShowAtCenter(true);
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
        this.emptyView.showTextView();
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setSectionsType(1);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setFastScrollEnabled();
        this.listView.setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setAdapter(this.listViewAdapter);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PhonebookSelectActivity$l53X2AdH9xoalnpyzoOuRLqxMRM(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1 && PhonebookSelectActivity.this.searching && PhonebookSelectActivity.this.searchWas) {
                    AndroidUtilities.hideKeyboard(PhonebookSelectActivity.this.getParentActivity().getCurrentFocus());
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                super.onScrolled(recyclerView, n, n2);
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.contactsDidLoad) {
            final PhonebookAdapter listViewAdapter = this.listViewAdapter;
            if (listViewAdapter != null) {
                ((RecyclerListView.SectionsAdapter)listViewAdapter).notifyDataSetChanged();
            }
        }
        else if (n == NotificationCenter.closeChats) {
            this.removeSelfFromStack();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc $$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc = new _$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SECTIONS, new Class[] { LetterSectionCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn_U8MjEc, "avatar_backgroundPink") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        final ActionBar actionBar = super.actionBar;
        if (actionBar != null) {
            actionBar.closeSearchField();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final PhonebookAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerListView.SectionsAdapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    public void setDelegate(final PhonebookSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public interface PhonebookSelectActivityDelegate
    {
        void didSelectContact(final TLRPC.User p0);
    }
}
