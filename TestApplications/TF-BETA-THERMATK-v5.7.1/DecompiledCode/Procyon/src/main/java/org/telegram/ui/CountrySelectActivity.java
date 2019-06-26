// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import java.util.TimerTask;
import org.telegram.messenger.Utilities;
import java.util.Timer;
import org.telegram.ui.Cells.DividerCell;
import android.view.ViewGroup;
import java.util.Iterator;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.telegram.messenger.FileLog;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.telegram.messenger.ApplicationLoader;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.ui.Cells.LetterSectionCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.messenger.AndroidUtilities;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.ActionBar.BaseFragment;

public class CountrySelectActivity extends BaseFragment
{
    private CountrySelectActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private RecyclerListView listView;
    private CountryAdapter listViewAdapter;
    private boolean needPhoneCode;
    private CountrySearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;
    
    public CountrySelectActivity(final boolean needPhoneCode) {
        this.needPhoneCode = needPhoneCode;
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        final ActionBar actionBar = super.actionBar;
        int verticalScrollbarPosition = 1;
        actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("ChooseCountry", 2131559086));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    CountrySelectActivity.this.finishFragment();
                }
            }
        });
        super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                CountrySelectActivity.this.searchListViewAdapter.search(null);
                CountrySelectActivity.this.searching = false;
                CountrySelectActivity.this.searchWas = false;
                CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.listViewAdapter);
                CountrySelectActivity.this.listView.setFastScrollVisible(true);
                CountrySelectActivity.this.emptyView.setText(LocaleController.getString("ChooseCountry", 2131559086));
            }
            
            @Override
            public void onSearchExpand() {
                CountrySelectActivity.this.searching = true;
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                final String string = editText.getText().toString();
                CountrySelectActivity.this.searchListViewAdapter.search(string);
                if (string.length() != 0) {
                    CountrySelectActivity.this.searchWas = true;
                    if (CountrySelectActivity.this.listView != null) {
                        CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.searchListViewAdapter);
                        CountrySelectActivity.this.listView.setFastScrollVisible(false);
                    }
                    CountrySelectActivity.this.emptyView;
                }
            }
        }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        this.searching = false;
        this.searchWas = false;
        this.listViewAdapter = new CountryAdapter(context);
        this.searchListViewAdapter = new CountrySearchAdapter(context, this.listViewAdapter.getCountries());
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.emptyView = new EmptyTextProgressView(context)).showTextView();
        this.emptyView.setShowAtCenter(true);
        this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setSectionsType(1);
        this.listView.setEmptyView((View)this.emptyView);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setFastScrollEnabled();
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setAdapter(this.listViewAdapter);
        final RecyclerListView listView = this.listView;
        if (!LocaleController.isRTL) {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$CountrySelectActivity$JqHWqr_68DyDf_WzOb4K8Bisl88(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1 && CountrySelectActivity.this.searching && CountrySelectActivity.this.searchWas) {
                    AndroidUtilities.hideKeyboard(CountrySelectActivity.this.getParentActivity().getCurrentFocus());
                }
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SECTIONS, new Class[] { LetterSectionCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final CountryAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerListView.SectionsAdapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    public void setCountrySelectActivityDelegate(final CountrySelectActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public static class Country
    {
        public String code;
        public String name;
        public String shortname;
    }
    
    public class CountryAdapter extends SectionsAdapter
    {
        private HashMap<String, ArrayList<Country>> countries;
        private Context mContext;
        private ArrayList<String> sortedCountries;
        
        public CountryAdapter(final Context mContext) {
            this.countries = new HashMap<String, ArrayList<Country>>();
            this.sortedCountries = new ArrayList<String>();
            this.mContext = mContext;
            try {
                final InputStream open = ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt");
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open));
                while (true) {
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    final String[] split = line.split(";");
                    final Country e = new Country();
                    e.name = split[2];
                    e.code = split[0];
                    e.shortname = split[1];
                    final String upperCase = e.name.substring(0, 1).toUpperCase();
                    ArrayList<Country> value;
                    if ((value = this.countries.get(upperCase)) == null) {
                        value = new ArrayList<Country>();
                        this.countries.put(upperCase, value);
                        this.sortedCountries.add(upperCase);
                    }
                    value.add(e);
                }
                bufferedReader.close();
                open.close();
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            Collections.sort(this.sortedCountries, (Comparator<? super String>)_$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
            final Iterator<ArrayList<Country>> iterator = this.countries.values().iterator();
            while (iterator.hasNext()) {
                Collections.sort(iterator.next(), (Comparator<? super Country>)_$$Lambda$CountrySelectActivity$CountryAdapter$GbbT4_eUqPD5K07oYJV3z7sG8q4.INSTANCE);
            }
        }
        
        @Override
        public int getCountForSection(final int index) {
            int size = this.countries.get(this.sortedCountries.get(index)).size();
            if (index != this.sortedCountries.size() - 1) {
                ++size;
            }
            return size;
        }
        
        public HashMap<String, ArrayList<Country>> getCountries() {
            return this.countries;
        }
        
        public Country getItem(final int index, final int index2) {
            if (index >= 0) {
                if (index < this.sortedCountries.size()) {
                    final ArrayList<Country> list = this.countries.get(this.sortedCountries.get(index));
                    if (index2 >= 0) {
                        if (index2 < list.size()) {
                            return list.get(index2);
                        }
                    }
                }
            }
            return null;
        }
        
        @Override
        public int getItemViewType(int index, final int n) {
            if (n < this.countries.get(this.sortedCountries.get(index)).size()) {
                index = 0;
            }
            else {
                index = 1;
            }
            return index;
        }
        
        @Override
        public String getLetter(int sectionForPosition) {
            if ((sectionForPosition = ((RecyclerListView.SectionsAdapter)this).getSectionForPosition(sectionForPosition)) == -1) {
                sectionForPosition = this.sortedCountries.size() - 1;
            }
            return this.sortedCountries.get(sectionForPosition);
        }
        
        @Override
        public int getPositionForScrollProgress(final float n) {
            return (int)(((RecyclerListView.SectionsAdapter)this).getItemCount() * n);
        }
        
        @Override
        public int getSectionCount() {
            return this.sortedCountries.size();
        }
        
        @Override
        public View getSectionHeaderView(final int index, final View view) {
            Object o = view;
            if (view == null) {
                o = new LetterSectionCell(this.mContext);
                ((LetterSectionCell)o).setCellHeight(AndroidUtilities.dp(48.0f));
            }
            ((LetterSectionCell)o).setLetter(this.sortedCountries.get(index).toUpperCase());
            return (View)o;
        }
        
        @Override
        public boolean isEnabled(final int index, final int n) {
            return n < this.countries.get(this.sortedCountries.get(index)).size();
        }
        
        @Override
        public void onBindViewHolder(final int index, final int index2, final ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 0) {
                final Country country = this.countries.get(this.sortedCountries.get(index)).get(index2);
                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                final String name = country.name;
                String string;
                if (CountrySelectActivity.this.needPhoneCode) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(country.code);
                    string = sb.toString();
                }
                else {
                    string = null;
                }
                textSettingsCell.setTextAndValue(name, string, false);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int n) {
            Object o;
            if (n != 0) {
                o = new DividerCell(this.mContext);
                final boolean isRTL = LocaleController.isRTL;
                final float n2 = 24.0f;
                float n3;
                if (isRTL) {
                    n3 = 24.0f;
                }
                else {
                    n3 = 72.0f;
                }
                n = AndroidUtilities.dp(n3);
                final int dp = AndroidUtilities.dp(8.0f);
                float n4 = n2;
                if (LocaleController.isRTL) {
                    n4 = 72.0f;
                }
                ((View)o).setPadding(n, dp, AndroidUtilities.dp(n4), AndroidUtilities.dp(8.0f));
            }
            else {
                o = new TextSettingsCell(this.mContext);
                final boolean isRTL2 = LocaleController.isRTL;
                final float n5 = 16.0f;
                float n6;
                if (isRTL2) {
                    n6 = 16.0f;
                }
                else {
                    n6 = 54.0f;
                }
                n = AndroidUtilities.dp(n6);
                float n7 = n5;
                if (LocaleController.isRTL) {
                    n7 = 54.0f;
                }
                ((View)o).setPadding(n, 0, AndroidUtilities.dp(n7), 0);
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    public class CountrySearchAdapter extends SelectionAdapter
    {
        private HashMap<String, ArrayList<Country>> countries;
        private Context mContext;
        private ArrayList<Country> searchResult;
        private Timer searchTimer;
        
        public CountrySearchAdapter(final Context mContext, final HashMap<String, ArrayList<Country>> countries) {
            this.mContext = mContext;
            this.countries = countries;
        }
        
        private void processSearch(final String s) {
            Utilities.searchQueue.postRunnable(new _$$Lambda$CountrySelectActivity$CountrySearchAdapter$udtIr0WBUnvIEbTuJKkynwxgLek(this, s));
        }
        
        private void updateSearchResults(final ArrayList<Country> list) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$CountrySelectActivity$CountrySearchAdapter$XZsv25DJkZo_nbKtZwVZiPFyURs(this, list));
        }
        
        public Country getItem(final int index) {
            if (index >= 0 && index < this.searchResult.size()) {
                return this.searchResult.get(index);
            }
            return null;
        }
        
        @Override
        public int getItemCount() {
            final ArrayList<Country> searchResult = this.searchResult;
            if (searchResult == null) {
                return 0;
            }
            return searchResult.size();
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
        public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
            final Country country = this.searchResult.get(index);
            final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
            final String name = country.name;
            String string;
            if (CountrySelectActivity.this.needPhoneCode) {
                final StringBuilder sb = new StringBuilder();
                sb.append("+");
                sb.append(country.code);
                string = sb.toString();
            }
            else {
                string = null;
            }
            final int size = this.searchResult.size();
            boolean b = true;
            if (index == size - 1) {
                b = false;
            }
            textSettingsCell.setTextAndValue(name, string, b);
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            return new RecyclerListView.Holder((View)new TextSettingsCell(this.mContext));
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
                            CountrySearchAdapter.this.searchTimer.cancel();
                            CountrySearchAdapter.this.searchTimer = null;
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        CountrySearchAdapter.this.processSearch(s);
                    }
                }, 100L, 300L);
            }
        }
    }
    
    public interface CountrySelectActivityDelegate
    {
        void didSelectCountry(final String p0, final String p1);
    }
}
