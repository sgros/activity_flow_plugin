package org.telegram.p004ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.DividerCell;
import org.telegram.p004ui.Cells.LetterSectionCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SectionsAdapter;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;

/* renamed from: org.telegram.ui.CountrySelectActivity */
public class CountrySelectActivity extends BaseFragment {
    private CountrySelectActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private RecyclerListView listView;
    private CountryAdapter listViewAdapter;
    private boolean needPhoneCode;
    private CountrySearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;

    /* renamed from: org.telegram.ui.CountrySelectActivity$Country */
    public static class Country {
        public String code;
        public String name;
        public String shortname;
    }

    /* renamed from: org.telegram.ui.CountrySelectActivity$CountrySelectActivityDelegate */
    public interface CountrySelectActivityDelegate {
        void didSelectCountry(String str, String str2);
    }

    /* renamed from: org.telegram.ui.CountrySelectActivity$1 */
    class C41601 extends ActionBarMenuOnItemClick {
        C41601() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                CountrySelectActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.CountrySelectActivity$2 */
    class C41612 extends ActionBarMenuItemSearchListener {
        C41612() {
        }

        public void onSearchExpand() {
            CountrySelectActivity.this.searching = true;
        }

        public void onSearchCollapse() {
            CountrySelectActivity.this.searchListViewAdapter.search(null);
            CountrySelectActivity.this.searching = false;
            CountrySelectActivity.this.searchWas = false;
            CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.listViewAdapter);
            CountrySelectActivity.this.listView.setFastScrollVisible(true);
            CountrySelectActivity.this.emptyView.setText(LocaleController.getString("ChooseCountry", C1067R.string.ChooseCountry));
        }

        public void onTextChanged(EditText editText) {
            String obj = editText.getText().toString();
            CountrySelectActivity.this.searchListViewAdapter.search(obj);
            if (obj.length() != 0) {
                CountrySelectActivity.this.searchWas = true;
                if (CountrySelectActivity.this.listView != null) {
                    CountrySelectActivity.this.listView.setAdapter(CountrySelectActivity.this.searchListViewAdapter);
                    CountrySelectActivity.this.listView.setFastScrollVisible(false);
                }
                CountrySelectActivity.this.emptyView;
            }
        }
    }

    /* renamed from: org.telegram.ui.CountrySelectActivity$3 */
    class C41623 extends OnScrollListener {
        C41623() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1 && CountrySelectActivity.this.searching && CountrySelectActivity.this.searchWas) {
                AndroidUtilities.hideKeyboard(CountrySelectActivity.this.getParentActivity().getCurrentFocus());
            }
        }
    }

    /* renamed from: org.telegram.ui.CountrySelectActivity$CountrySearchAdapter */
    public class CountrySearchAdapter extends SelectionAdapter {
        private HashMap<String, ArrayList<Country>> countries;
        private Context mContext;
        private ArrayList<Country> searchResult;
        private Timer searchTimer;

        public int getItemViewType(int i) {
            return 0;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return true;
        }

        public CountrySearchAdapter(Context context, HashMap<String, ArrayList<Country>> hashMap) {
            this.mContext = context;
            this.countries = hashMap;
        }

        public void search(final String str) {
            if (str == null) {
                this.searchResult = null;
                return;
            }
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                }
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
                public void run() {
                    try {
                        CountrySearchAdapter.this.searchTimer.cancel();
                        CountrySearchAdapter.this.searchTimer = null;
                    } catch (Exception e) {
                        FileLog.m30e(e);
                    }
                    CountrySearchAdapter.this.processSearch(str);
                }
            }, 100, 300);
        }

        private void processSearch(String str) {
            Utilities.searchQueue.postRunnable(new C1481x7426f4d4(this, str));
        }

        /* renamed from: lambda$processSearch$0$CountrySelectActivity$CountrySearchAdapter */
        public /* synthetic */ void mo16598xa1825eb2(String str) {
            if (str.trim().toLowerCase().length() == 0) {
                updateSearchResults(new ArrayList());
                return;
            }
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = (ArrayList) this.countries.get(str.substring(0, 1).toUpperCase());
            if (arrayList2 != null) {
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    Country country = (Country) it.next();
                    if (country.name.toLowerCase().startsWith(str)) {
                        arrayList.add(country);
                    }
                }
            }
            updateSearchResults(arrayList);
        }

        private void updateSearchResults(ArrayList<Country> arrayList) {
            AndroidUtilities.runOnUIThread(new C1480xb73c9b43(this, arrayList));
        }

        /* renamed from: lambda$updateSearchResults$1$CountrySelectActivity$CountrySearchAdapter */
        public /* synthetic */ void mo16599xa883bb23(ArrayList arrayList) {
            this.searchResult = arrayList;
            notifyDataSetChanged();
        }

        public int getItemCount() {
            ArrayList arrayList = this.searchResult;
            if (arrayList == null) {
                return 0;
            }
            return arrayList.size();
        }

        public Country getItem(int i) {
            return (i < 0 || i >= this.searchResult.size()) ? null : (Country) this.searchResult.get(i);
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Holder(new TextSettingsCell(this.mContext));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            String stringBuilder;
            Country country = (Country) this.searchResult.get(i);
            TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            String str = country.name;
            if (CountrySelectActivity.this.needPhoneCode) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("+");
                stringBuilder2.append(country.code);
                stringBuilder = stringBuilder2.toString();
            } else {
                stringBuilder = null;
            }
            boolean z = true;
            if (i == this.searchResult.size() - 1) {
                z = false;
            }
            textSettingsCell.setTextAndValue(str, stringBuilder, z);
        }
    }

    /* renamed from: org.telegram.ui.CountrySelectActivity$CountryAdapter */
    public class CountryAdapter extends SectionsAdapter {
        private HashMap<String, ArrayList<Country>> countries = new HashMap();
        private Context mContext;
        private ArrayList<String> sortedCountries = new ArrayList();

        public CountryAdapter(Context context) {
            this.mContext = context;
            try {
                InputStream open = ApplicationLoader.applicationContext.getResources().getAssets().open("countries.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    String[] split = readLine.split(";");
                    Country country = new Country();
                    country.name = split[2];
                    country.code = split[0];
                    country.shortname = split[1];
                    readLine = country.name.substring(0, 1).toUpperCase();
                    ArrayList arrayList = (ArrayList) this.countries.get(readLine);
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        this.countries.put(readLine, arrayList);
                        this.sortedCountries.add(readLine);
                    }
                    arrayList.add(country);
                }
                bufferedReader.close();
                open.close();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            Collections.sort(this.sortedCountries, C2079-$$Lambda$TEfSBt3hRUlBSSARfPEHsJesTtE.INSTANCE);
            for (ArrayList sort : this.countries.values()) {
                Collections.sort(sort, C1479x7538ed2f.INSTANCE);
            }
        }

        public HashMap<String, ArrayList<Country>> getCountries() {
            return this.countries;
        }

        public Country getItem(int i, int i2) {
            if (i >= 0 && i < this.sortedCountries.size()) {
                ArrayList arrayList = (ArrayList) this.countries.get(this.sortedCountries.get(i));
                if (i2 >= 0 && i2 < arrayList.size()) {
                    return (Country) arrayList.get(i2);
                }
            }
            return null;
        }

        public boolean isEnabled(int i, int i2) {
            return i2 < ((ArrayList) this.countries.get(this.sortedCountries.get(i))).size();
        }

        public int getSectionCount() {
            return this.sortedCountries.size();
        }

        public int getCountForSection(int i) {
            int size = ((ArrayList) this.countries.get(this.sortedCountries.get(i))).size();
            return i != this.sortedCountries.size() + -1 ? size + 1 : size;
        }

        public View getSectionHeaderView(int i, View view) {
            if (view == null) {
                view = new LetterSectionCell(this.mContext);
                view.setCellHeight(AndroidUtilities.m26dp(48.0f));
            }
            ((LetterSectionCell) view).setLetter(((String) this.sortedCountries.get(i)).toUpperCase());
            return view;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View dividerCell;
            float f;
            if (i != 0) {
                dividerCell = new DividerCell(this.mContext);
                f = 24.0f;
                i = AndroidUtilities.m26dp(LocaleController.isRTL ? 24.0f : 72.0f);
                int dp = AndroidUtilities.m26dp(8.0f);
                if (LocaleController.isRTL) {
                    f = 72.0f;
                }
                dividerCell.setPadding(i, dp, AndroidUtilities.m26dp(f), AndroidUtilities.m26dp(8.0f));
            } else {
                dividerCell = new TextSettingsCell(this.mContext);
                f = 16.0f;
                i = AndroidUtilities.m26dp(LocaleController.isRTL ? 16.0f : 54.0f);
                if (LocaleController.isRTL) {
                    f = 54.0f;
                }
                dividerCell.setPadding(i, 0, AndroidUtilities.m26dp(f), 0);
            }
            return new Holder(dividerCell);
        }

        public void onBindViewHolder(int i, int i2, ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 0) {
                String stringBuilder;
                Country country = (Country) ((ArrayList) this.countries.get(this.sortedCountries.get(i))).get(i2);
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                String str = country.name;
                if (CountrySelectActivity.this.needPhoneCode) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("+");
                    stringBuilder2.append(country.code);
                    stringBuilder = stringBuilder2.toString();
                } else {
                    stringBuilder = null;
                }
                textSettingsCell.setTextAndValue(str, stringBuilder, false);
            }
        }

        public int getItemViewType(int i, int i2) {
            return i2 < ((ArrayList) this.countries.get(this.sortedCountries.get(i))).size() ? 0 : 1;
        }

        public String getLetter(int i) {
            i = getSectionForPosition(i);
            if (i == -1) {
                i = this.sortedCountries.size() - 1;
            }
            return (String) this.sortedCountries.get(i);
        }

        public int getPositionForScrollProgress(float f) {
            return (int) (((float) getItemCount()) * f);
        }
    }

    public CountrySelectActivity(boolean z) {
        this.needPhoneCode = z;
    }

    public boolean onFragmentCreate() {
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    public View createView(Context context) {
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        int i = 1;
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("ChooseCountry", C1067R.string.ChooseCountry));
        this.actionBar.setActionBarMenuOnItemClick(new C41601());
        this.actionBar.createMenu().addItem(0, (int) C1067R.C1065drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new C41612()).setSearchFieldHint(LocaleController.getString("Search", C1067R.string.Search));
        this.searching = false;
        this.searchWas = false;
        this.listViewAdapter = new CountryAdapter(context);
        this.searchListViewAdapter = new CountrySearchAdapter(context, this.listViewAdapter.getCountries());
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.emptyView = new EmptyTextProgressView(context);
        this.emptyView.showTextView();
        this.emptyView.setShowAtCenter(true);
        this.emptyView.setText(LocaleController.getString("NoResult", C1067R.string.NoResult));
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView = new RecyclerListView(context);
        this.listView.setSectionsType(1);
        this.listView.setEmptyView(this.emptyView);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setFastScrollEnabled();
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setAdapter(this.listViewAdapter);
        RecyclerListView recyclerListView = this.listView;
        if (!LocaleController.isRTL) {
            i = 2;
        }
        recyclerListView.setVerticalScrollbarPosition(i);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new C3676-$$Lambda$CountrySelectActivity$JqHWqr-68DyDf_WzOb4K8Bisl88(this));
        this.listView.setOnScrollListener(new C41623());
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$0$CountrySelectActivity(View view, int i) {
        Country item;
        if (this.searching && this.searchWas) {
            item = this.searchListViewAdapter.getItem(i);
        } else {
            int sectionForPosition = this.listViewAdapter.getSectionForPosition(i);
            int positionInSectionForPosition = this.listViewAdapter.getPositionInSectionForPosition(i);
            if (positionInSectionForPosition >= 0 && sectionForPosition >= 0) {
                item = this.listViewAdapter.getItem(sectionForPosition, positionInSectionForPosition);
            }
        }
        if (i >= 0) {
            finishFragment();
            if (item != null) {
                CountrySelectActivityDelegate countrySelectActivityDelegate = this.delegate;
                if (countrySelectActivityDelegate != null) {
                    countrySelectActivityDelegate.didSelectCountry(item.name, item.shortname);
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        CountryAdapter countryAdapter = this.listViewAdapter;
        if (countryAdapter != null) {
            countryAdapter.notifyDataSetChanged();
        }
    }

    public void setCountrySelectActivityDelegate(CountrySelectActivityDelegate countrySelectActivityDelegate) {
        this.delegate = countrySelectActivityDelegate;
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[17];
        r1[9] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[10] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollActive);
        r1[11] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollInactive);
        r1[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollText);
        r1[13] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        View view = this.listView;
        Class[] clsArr = new Class[]{TextSettingsCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        r1[14] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[15] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        r1[16] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SECTIONS, new Class[]{LetterSectionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        return r1;
    }
}
