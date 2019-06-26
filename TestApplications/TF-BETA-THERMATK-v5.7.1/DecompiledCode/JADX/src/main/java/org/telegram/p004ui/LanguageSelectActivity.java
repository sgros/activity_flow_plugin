package org.telegram.p004ui;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocaleController.LocaleInfo;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.LanguageCell;
import org.telegram.p004ui.Cells.ShadowSectionCell;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;

/* renamed from: org.telegram.ui.LanguageSelectActivity */
public class LanguageSelectActivity extends BaseFragment implements NotificationCenterDelegate {
    private EmptyTextProgressView emptyView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private ListAdapter searchListViewAdapter;
    private ArrayList<LocaleInfo> searchResult;
    private Timer searchTimer;
    private boolean searchWas;
    private boolean searching;
    private ArrayList<LocaleInfo> sortedLanguages;
    private ArrayList<LocaleInfo> unofficialLanguages;

    /* renamed from: org.telegram.ui.LanguageSelectActivity$1 */
    class C42061 extends ActionBarMenuOnItemClick {
        C42061() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                LanguageSelectActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.LanguageSelectActivity$2 */
    class C42072 extends ActionBarMenuItemSearchListener {
        C42072() {
        }

        public void onSearchExpand() {
            LanguageSelectActivity.this.searching = true;
        }

        public void onSearchCollapse() {
            LanguageSelectActivity.this.search(null);
            LanguageSelectActivity.this.searching = false;
            LanguageSelectActivity.this.searchWas = false;
            if (LanguageSelectActivity.this.listView != null) {
                LanguageSelectActivity.this.emptyView.setVisibility(8);
                LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.listAdapter);
            }
        }

        public void onTextChanged(EditText editText) {
            String obj = editText.getText().toString();
            LanguageSelectActivity.this.search(obj);
            if (obj.length() != 0) {
                LanguageSelectActivity.this.searchWas = true;
                if (LanguageSelectActivity.this.listView != null) {
                    LanguageSelectActivity.this.listView.setAdapter(LanguageSelectActivity.this.searchListViewAdapter);
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.LanguageSelectActivity$3 */
    class C42083 extends OnScrollListener {
        C42083() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1 && LanguageSelectActivity.this.searching && LanguageSelectActivity.this.searchWas) {
                AndroidUtilities.hideKeyboard(LanguageSelectActivity.this.getParentActivity().getCurrentFocus());
            }
        }
    }

    /* renamed from: org.telegram.ui.LanguageSelectActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;
        private boolean search;

        public ListAdapter(Context context, boolean z) {
            this.mContext = context;
            this.search = z;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        public int getItemCount() {
            if (!this.search) {
                int size = LanguageSelectActivity.this.sortedLanguages.size();
                if (size != 0) {
                    size++;
                }
                if (!LanguageSelectActivity.this.unofficialLanguages.isEmpty()) {
                    size += LanguageSelectActivity.this.unofficialLanguages.size() + 1;
                }
                return size;
            } else if (LanguageSelectActivity.this.searchResult == null) {
                return 0;
            } else {
                return LanguageSelectActivity.this.searchResult.size();
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View shadowSectionCell;
            if (i != 0) {
                shadowSectionCell = new ShadowSectionCell(this.mContext);
            } else {
                shadowSectionCell = new LanguageCell(this.mContext, false);
                shadowSectionCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            return new Holder(shadowSectionCell);
        }

        /* JADX WARNING: Missing block: B:12:0x0065, code skipped:
            if (r8 == (org.telegram.p004ui.LanguageSelectActivity.access$800(r6.this$0).size() - 1)) goto L_0x0067;
     */
        /* JADX WARNING: Missing block: B:14:0x0069, code skipped:
            r8 = 0;
     */
        /* JADX WARNING: Missing block: B:21:0x009c, code skipped:
            if (r8 == (org.telegram.p004ui.LanguageSelectActivity.access$1000(r6.this$0).size() - 1)) goto L_0x0067;
     */
        /* JADX WARNING: Missing block: B:26:0x00ce, code skipped:
            if (r8 == (org.telegram.p004ui.LanguageSelectActivity.access$900(r6.this$0).size() - 1)) goto L_0x0067;
     */
        public void onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder r7, int r8) {
            /*
            r6 = this;
            r0 = r7.getItemViewType();
            r1 = 1;
            if (r0 == 0) goto L_0x0045;
        L_0x0007:
            if (r0 == r1) goto L_0x000b;
        L_0x0009:
            goto L_0x0108;
        L_0x000b:
            r7 = r7.itemView;
            r7 = (org.telegram.p004ui.Cells.ShadowSectionCell) r7;
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.unofficialLanguages;
            r0 = r0.isEmpty();
            r1 = "windowBackgroundGrayShadow";
            if (r0 != 0) goto L_0x0037;
        L_0x001d:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.unofficialLanguages;
            r0 = r0.size();
            if (r8 != r0) goto L_0x0037;
        L_0x0029:
            r8 = r6.mContext;
            r0 = 2131165394; // 0x7f0700d2 float:1.7945004E38 double:1.052935607E-314;
            r8 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r8, r0, r1);
            r7.setBackgroundDrawable(r8);
            goto L_0x0108;
        L_0x0037:
            r8 = r6.mContext;
            r0 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
            r8 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r8, r0, r1);
            r7.setBackgroundDrawable(r8);
            goto L_0x0108;
        L_0x0045:
            r7 = r7.itemView;
            r7 = (org.telegram.p004ui.Cells.LanguageCell) r7;
            r0 = r6.search;
            r2 = 0;
            if (r0 == 0) goto L_0x006b;
        L_0x004e:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.searchResult;
            r0 = r0.get(r8);
            r0 = (org.telegram.messenger.LocaleController.LocaleInfo) r0;
            r3 = org.telegram.p004ui.LanguageSelectActivity.this;
            r3 = r3.searchResult;
            r3 = r3.size();
            r3 = r3 - r1;
            if (r8 != r3) goto L_0x0069;
        L_0x0067:
            r8 = 1;
            goto L_0x00d1;
        L_0x0069:
            r8 = 0;
            goto L_0x00d1;
        L_0x006b:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.unofficialLanguages;
            r0 = r0.isEmpty();
            if (r0 != 0) goto L_0x009f;
        L_0x0077:
            if (r8 < 0) goto L_0x009f;
        L_0x0079:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.unofficialLanguages;
            r0 = r0.size();
            if (r8 >= r0) goto L_0x009f;
        L_0x0085:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.unofficialLanguages;
            r0 = r0.get(r8);
            r0 = (org.telegram.messenger.LocaleController.LocaleInfo) r0;
            r3 = org.telegram.p004ui.LanguageSelectActivity.this;
            r3 = r3.unofficialLanguages;
            r3 = r3.size();
            r3 = r3 - r1;
            if (r8 != r3) goto L_0x0069;
        L_0x009e:
            goto L_0x0067;
        L_0x009f:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.unofficialLanguages;
            r0 = r0.isEmpty();
            if (r0 != 0) goto L_0x00b7;
        L_0x00ab:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.unofficialLanguages;
            r0 = r0.size();
            r0 = r0 + r1;
            r8 = r8 - r0;
        L_0x00b7:
            r0 = org.telegram.p004ui.LanguageSelectActivity.this;
            r0 = r0.sortedLanguages;
            r0 = r0.get(r8);
            r0 = (org.telegram.messenger.LocaleController.LocaleInfo) r0;
            r3 = org.telegram.p004ui.LanguageSelectActivity.this;
            r3 = r3.sortedLanguages;
            r3 = r3.size();
            r3 = r3 - r1;
            if (r8 != r3) goto L_0x0069;
        L_0x00d0:
            goto L_0x0067;
        L_0x00d1:
            r3 = r0.isLocal();
            if (r3 == 0) goto L_0x00f4;
        L_0x00d7:
            r3 = 2;
            r3 = new java.lang.Object[r3];
            r4 = r0.name;
            r3[r2] = r4;
            r4 = 2131559718; // 0x7f0d0526 float:1.8744788E38 double:1.0531304287E-314;
            r5 = "LanguageCustom";
            r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
            r3[r1] = r4;
            r4 = "%1$s (%2$s)";
            r3 = java.lang.String.format(r4, r3);
            r8 = r8 ^ r1;
            r7.setLanguage(r0, r3, r8);
            goto L_0x00f9;
        L_0x00f4:
            r3 = 0;
            r8 = r8 ^ r1;
            r7.setLanguage(r0, r3, r8);
        L_0x00f9:
            r8 = org.telegram.messenger.LocaleController.getInstance();
            r8 = r8.getCurrentLocaleInfo();
            if (r0 != r8) goto L_0x0104;
        L_0x0103:
            goto L_0x0105;
        L_0x0104:
            r1 = 0;
        L_0x0105:
            r7.setLanguageSelected(r1);
        L_0x0108:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LanguageSelectActivity$ListAdapter.onBindViewHolder(androidx.recyclerview.widget.RecyclerView$ViewHolder, int):void");
        }

        public int getItemViewType(int i) {
            return ((LanguageSelectActivity.this.unofficialLanguages.isEmpty() || !(i == LanguageSelectActivity.this.unofficialLanguages.size() || i == (LanguageSelectActivity.this.unofficialLanguages.size() + LanguageSelectActivity.this.sortedLanguages.size()) + 1)) && !(LanguageSelectActivity.this.unofficialLanguages.isEmpty() && i == LanguageSelectActivity.this.sortedLanguages.size())) ? 0 : 1;
        }
    }

    public boolean onFragmentCreate() {
        fillLanguages();
        LocaleController.getInstance().loadRemoteLanguages(this.currentAccount);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
        return super.onFragmentCreate();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
    }

    public View createView(Context context) {
        this.searching = false;
        this.searchWas = false;
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("Language", C1067R.string.Language));
        this.actionBar.setActionBarMenuOnItemClick(new C42061());
        this.actionBar.createMenu().addItem(0, (int) C1067R.C1065drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new C42072()).setSearchFieldHint(LocaleController.getString("Search", C1067R.string.Search));
        this.listAdapter = new ListAdapter(context, false);
        this.searchListViewAdapter = new ListAdapter(context, true);
        this.fragmentView = new FrameLayout(context);
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.emptyView = new EmptyTextProgressView(context);
        this.emptyView.setText(LocaleController.getString("NoResult", C1067R.string.NoResult));
        this.emptyView.showTextView();
        this.emptyView.setShowAtCenter(true);
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView = new RecyclerListView(context);
        this.listView.setEmptyView(this.emptyView);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listAdapter);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new C3704-$$Lambda$LanguageSelectActivity$_kDK86Zracai_nye8r1jdu_vwQ0(this));
        this.listView.setOnItemLongClickListener(new C3703-$$Lambda$LanguageSelectActivity$3pzz3aV3o29w5n6tkl_ts3VGtNU(this));
        this.listView.setOnScrollListener(new C42083());
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$0$LanguageSelectActivity(View view, int i) {
        if (getParentActivity() != null && this.parentLayout != null && (view instanceof LanguageCell)) {
            LocaleInfo currentLocale = ((LanguageCell) view).getCurrentLocale();
            if (currentLocale != null) {
                LocaleController.getInstance().applyLanguage(currentLocale, true, false, false, true, this.currentAccount);
                this.parentLayout.rebuildAllFragmentViews(false, false);
            }
            finishFragment();
        }
    }

    public /* synthetic */ boolean lambda$createView$2$LanguageSelectActivity(View view, int i) {
        if (!(getParentActivity() == null || this.parentLayout == null || !(view instanceof LanguageCell))) {
            LocaleInfo currentLocale = ((LanguageCell) view).getCurrentLocale();
            if (!(currentLocale == null || currentLocale.pathToFile == null || (currentLocale.isRemote() && currentLocale.serverIndex != Integer.MAX_VALUE))) {
                Builder builder = new Builder(getParentActivity());
                builder.setMessage(LocaleController.getString("DeleteLocalization", C1067R.string.DeleteLocalization));
                builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                builder.setPositiveButton(LocaleController.getString("Delete", C1067R.string.Delete), new C1562-$$Lambda$LanguageSelectActivity$oSx7KAjKIG5eHsslTckzl6eK4-U(this, currentLocale));
                builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                showDialog(builder.create());
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ void lambda$null$1$LanguageSelectActivity(LocaleInfo localeInfo, DialogInterface dialogInterface, int i) {
        if (LocaleController.getInstance().deleteLanguage(localeInfo, this.currentAccount)) {
            fillLanguages();
            ArrayList arrayList = this.searchResult;
            if (arrayList != null) {
                arrayList.remove(localeInfo);
            }
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
            listAdapter = this.searchListViewAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.suggestedLangpack && this.listAdapter != null) {
            fillLanguages();
            this.listAdapter.notifyDataSetChanged();
        }
    }

    private void fillLanguages() {
        C1560-$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU c1560-$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU = new C1560-$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU(LocaleController.getInstance().getCurrentLocaleInfo());
        this.sortedLanguages = new ArrayList();
        this.unofficialLanguages = new ArrayList(LocaleController.getInstance().unofficialLanguages);
        ArrayList arrayList = LocaleController.getInstance().languages;
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            LocaleInfo localeInfo = (LocaleInfo) arrayList.get(i);
            if (localeInfo.serverIndex != Integer.MAX_VALUE) {
                this.sortedLanguages.add(localeInfo);
            } else {
                this.unofficialLanguages.add(localeInfo);
            }
        }
        Collections.sort(this.sortedLanguages, c1560-$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU);
        Collections.sort(this.unofficialLanguages, c1560-$$Lambda$LanguageSelectActivity$bKvkTCAKOIGl0v4A4OGKsEAtQUU);
    }

    static /* synthetic */ int lambda$fillLanguages$3(LocaleInfo localeInfo, LocaleInfo localeInfo2, LocaleInfo localeInfo3) {
        if (localeInfo2 == localeInfo) {
            return -1;
        }
        if (localeInfo3 == localeInfo) {
            return 1;
        }
        int i = localeInfo2.serverIndex;
        int i2 = localeInfo3.serverIndex;
        if (i == i2) {
            return localeInfo2.name.compareTo(localeInfo3.name);
        }
        if (i > i2) {
            return 1;
        }
        if (i < i2) {
            return -1;
        }
        return 0;
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
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
                    LanguageSelectActivity.this.searchTimer.cancel();
                    LanguageSelectActivity.this.searchTimer = null;
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
                LanguageSelectActivity.this.processSearch(str);
            }
        }, 100, 300);
    }

    private void processSearch(String str) {
        Utilities.searchQueue.postRunnable(new C1561-$$Lambda$LanguageSelectActivity$eAs20MfDQaWJVrE3O9UMwR-59HM(this, str));
    }

    public /* synthetic */ void lambda$processSearch$4$LanguageSelectActivity(String str) {
        if (str.trim().toLowerCase().length() == 0) {
            updateSearchResults(new ArrayList());
            return;
        }
        System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        int size = this.unofficialLanguages.size();
        for (int i = 0; i < size; i++) {
            LocaleInfo localeInfo = (LocaleInfo) this.unofficialLanguages.get(i);
            if (localeInfo.name.toLowerCase().startsWith(str) || localeInfo.nameEnglish.toLowerCase().startsWith(str)) {
                arrayList.add(localeInfo);
            }
        }
        size = this.sortedLanguages.size();
        for (int i2 = 0; i2 < size; i2++) {
            LocaleInfo localeInfo2 = (LocaleInfo) this.sortedLanguages.get(i2);
            if (localeInfo2.name.toLowerCase().startsWith(str) || localeInfo2.nameEnglish.toLowerCase().startsWith(str)) {
                arrayList.add(localeInfo2);
            }
        }
        updateSearchResults(arrayList);
    }

    private void updateSearchResults(ArrayList<LocaleInfo> arrayList) {
        AndroidUtilities.runOnUIThread(new C1559-$$Lambda$LanguageSelectActivity$W3KO-no_vIkiCROuRNp2vphR3Bk(this, arrayList));
    }

    public /* synthetic */ void lambda$updateSearchResults$5$LanguageSelectActivity(ArrayList arrayList) {
        this.searchResult = arrayList;
        this.searchListViewAdapter.notifyDataSetChanged();
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[16];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{LanguageCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch);
        themeDescriptionArr[8] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder);
        themeDescriptionArr[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[10] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"textView2"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{LanguageCell.class}, new String[]{"checkImage"}, null, null, null, Theme.key_featuredStickers_addedIcon);
        return themeDescriptionArr;
    }
}