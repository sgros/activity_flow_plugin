// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import java.util.TimerTask;
import org.telegram.messenger.FileLog;
import android.content.DialogInterface;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.content.Context;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Timer;
import org.telegram.messenger.LocaleController;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class LanguageSelectActivity extends BaseFragment implements NotificationCenterDelegate
{
    private EmptyTextProgressView emptyView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ListAdapter searchListViewAdapter;
    private ArrayList<LocaleController.LocaleInfo> searchResult;
    private Timer searchTimer;
    private boolean searchWas;
    private boolean searching;
    private ArrayList<LocaleController.LocaleInfo> sortedLanguages;
    private ArrayList<LocaleController.LocaleInfo> unofficialLanguages;
    
    private void fillLanguages() {
        final _$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU $$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU = new _$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU(LocaleController.getInstance().getCurrentLocaleInfo());
        this.sortedLanguages = new ArrayList<LocaleController.LocaleInfo>();
        this.unofficialLanguages = new ArrayList<LocaleController.LocaleInfo>((Collection<? extends LocaleController.LocaleInfo>)LocaleController.getInstance().unofficialLanguages);
        final ArrayList<LocaleController.LocaleInfo> languages = LocaleController.getInstance().languages;
        for (int size = languages.size(), i = 0; i < size; ++i) {
            final LocaleController.LocaleInfo localeInfo = languages.get(i);
            if (localeInfo.serverIndex != Integer.MAX_VALUE) {
                this.sortedLanguages.add(localeInfo);
            }
            else {
                this.unofficialLanguages.add(localeInfo);
            }
        }
        Collections.sort(this.sortedLanguages, $$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU);
        Collections.sort(this.unofficialLanguages, $$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU);
    }
    
    private void processSearch(final String s) {
        Utilities.searchQueue.postRunnable(new _$$Lambda$LanguageSelectActivity$eAs20MfDQaWJVrE3O9UMwR_59HM(this, s));
    }
    
    private void updateSearchResults(final ArrayList<LocaleController.LocaleInfo> list) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$LanguageSelectActivity$W3KO_no_vIkiCROuRNp2vphR3Bk(this, list));
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("Language", 2131559715));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    LanguageSelectActivity.this.finishFragment();
                }
            }
        });
        super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                LanguageSelectActivity.this.search(null);
                LanguageSelectActivity.this.searching = false;
                LanguageSelectActivity.this.searchWas = false;
                if (LanguageSelectActivity.this.listView != null) {
                    LanguageSelectActivity.this.emptyView.setVisibility(8);
                    LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.listAdapter);
                }
            }
            
            @Override
            public void onSearchExpand() {
                LanguageSelectActivity.this.searching = true;
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                final String string = editText.getText().toString();
                LanguageSelectActivity.this.search(string);
                if (string.length() != 0) {
                    LanguageSelectActivity.this.searchWas = true;
                    if (LanguageSelectActivity.this.listView != null) {
                        LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.searchListViewAdapter);
                    }
                }
            }
        }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        this.listAdapter = new ListAdapter(context, false);
        this.searchListViewAdapter = new ListAdapter(context, true);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.emptyView = new EmptyTextProgressView(context)).setText(LocaleController.getString("NoResult", 2131559943));
        this.emptyView.showTextView();
        this.emptyView.setShowAtCenter(true);
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$LanguageSelectActivity$_kDK86Zracai_nye8r1jdu_vwQ0(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$LanguageSelectActivity$3pzz3aV3o29w5n6tkl_ts3VGtNU(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1 && LanguageSelectActivity.this.searching && LanguageSelectActivity.this.searchWas) {
                    AndroidUtilities.hideKeyboard(LanguageSelectActivity.this.getParentActivity().getCurrentFocus());
                }
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.suggestedLangpack && this.listAdapter != null) {
            this.fillLanguages();
            this.listAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { LanguageCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { LanguageCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { LanguageCell.class }, new String[] { "textView2" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { LanguageCell.class }, new String[] { "checkImage" }, null, null, null, "featuredStickers_addedIcon") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.fillLanguages();
        LocaleController.getInstance().loadRemoteLanguages(super.currentAccount);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    public void search(final String s) {
        if (s == null) {
            this.searchResult = null;
        }
        else {
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            (this.searchTimer = new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        LanguageSelectActivity.this.searchTimer.cancel();
                        LanguageSelectActivity.this.searchTimer = null;
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    LanguageSelectActivity.this.processSearch(s);
                }
            }, 100L, 300L);
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        private boolean search;
        
        public ListAdapter(final Context mContext, final boolean search) {
            this.mContext = mContext;
            this.search = search;
        }
        
        @Override
        public int getItemCount() {
            if (!this.search) {
                final int size = LanguageSelectActivity.this.sortedLanguages.size();
                int n;
                if ((n = size) != 0) {
                    n = size + 1;
                }
                int n2 = n;
                if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty()) {
                    n2 = n + (LanguageSelectActivity.this.unofficialLanguages.size() + 1);
                }
                return n2;
            }
            if (LanguageSelectActivity.this.searchResult == null) {
                return 0;
            }
            return LanguageSelectActivity.this.searchResult.size();
        }
        
        @Override
        public int getItemViewType(final int n) {
            if ((!LanguageSelectActivity.this.unofficialLanguages.isEmpty() && (n == LanguageSelectActivity.this.unofficialLanguages.size() || n == LanguageSelectActivity.this.unofficialLanguages.size() + LanguageSelectActivity.this.sortedLanguages.size() + 1)) || (LanguageSelectActivity.this.unofficialLanguages.isEmpty() && n == LanguageSelectActivity.this.sortedLanguages.size())) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean languageSelected = true;
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    final ShadowSectionCell shadowSectionCell = (ShadowSectionCell)viewHolder.itemView;
                    if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty() && n == LanguageSelectActivity.this.unofficialLanguages.size()) {
                        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else {
                        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                }
            }
            else {
                final LanguageCell languageCell = (LanguageCell)viewHolder.itemView;
                LocaleController.LocaleInfo localeInfo2 = null;
                Label_0298: {
                    Label_0150: {
                        if (this.search) {
                            final LocaleController.LocaleInfo localeInfo = localeInfo2 = LanguageSelectActivity.this.searchResult.get(n);
                            if (n != LanguageSelectActivity.this.searchResult.size() - 1) {
                                break Label_0150;
                            }
                            localeInfo2 = localeInfo;
                        }
                        else if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty() && n >= 0 && n < LanguageSelectActivity.this.unofficialLanguages.size()) {
                            final LocaleController.LocaleInfo localeInfo3 = localeInfo2 = LanguageSelectActivity.this.unofficialLanguages.get(n);
                            if (n != LanguageSelectActivity.this.unofficialLanguages.size() - 1) {
                                break Label_0150;
                            }
                            localeInfo2 = localeInfo3;
                        }
                        else {
                            int index = n;
                            if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty()) {
                                index = n - (LanguageSelectActivity.this.unofficialLanguages.size() + 1);
                            }
                            final LocaleController.LocaleInfo localeInfo4 = localeInfo2 = (LocaleController.LocaleInfo)LanguageSelectActivity.this.sortedLanguages.get(index);
                            if (index != LanguageSelectActivity.this.sortedLanguages.size() - 1) {
                                break Label_0150;
                            }
                            localeInfo2 = localeInfo4;
                        }
                        n = 1;
                        break Label_0298;
                    }
                    n = 0;
                }
                if (localeInfo2.isLocal()) {
                    languageCell.setLanguage(localeInfo2, String.format("%1$s (%2$s)", localeInfo2.name, LocaleController.getString("LanguageCustom", 2131559718)), (boolean)((n ^ 0x1) != 0x0));
                }
                else {
                    languageCell.setLanguage(localeInfo2, null, (boolean)((n ^ 0x1) != 0x0));
                }
                if (localeInfo2 != LocaleController.getInstance().getCurrentLocaleInfo()) {
                    languageSelected = false;
                }
                languageCell.setLanguageSelected(languageSelected);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                o = new ShadowSectionCell(this.mContext);
            }
            else {
                o = new LanguageCell(this.mContext, false);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
}
