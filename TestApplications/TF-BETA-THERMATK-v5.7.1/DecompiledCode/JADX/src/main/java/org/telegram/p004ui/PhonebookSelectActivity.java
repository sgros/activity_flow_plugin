package org.telegram.p004ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.p004ui.Adapters.PhonebookAdapter;
import org.telegram.p004ui.Adapters.PhonebookSearchAdapter;
import org.telegram.p004ui.Cells.LetterSectionCell;
import org.telegram.p004ui.Cells.UserCell;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.PhonebookSelectActivity */
public class PhonebookSelectActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int search_button = 0;
    private PhonebookSelectActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private RecyclerListView listView;
    private PhonebookAdapter listViewAdapter;
    private PhonebookSearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;

    /* renamed from: org.telegram.ui.PhonebookSelectActivity$PhonebookSelectActivityDelegate */
    public interface PhonebookSelectActivityDelegate {
        void didSelectContact(User user);
    }

    /* renamed from: org.telegram.ui.PhonebookSelectActivity$1 */
    class C42631 extends ActionBarMenuOnItemClick {
        C42631() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                PhonebookSelectActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.PhonebookSelectActivity$2 */
    class C42642 extends ActionBarMenuItemSearchListener {
        C42642() {
        }

        public void onSearchExpand() {
            PhonebookSelectActivity.this.searching = true;
        }

        public void onSearchCollapse() {
            PhonebookSelectActivity.this.searchListViewAdapter.search(null);
            PhonebookSelectActivity.this.searching = false;
            PhonebookSelectActivity.this.searchWas = false;
            PhonebookSelectActivity.this.listView.setAdapter(PhonebookSelectActivity.this.listViewAdapter);
            PhonebookSelectActivity.this.listView.setSectionsType(1);
            PhonebookSelectActivity.this.listViewAdapter.notifyDataSetChanged();
            PhonebookSelectActivity.this.listView.setFastScrollVisible(true);
            PhonebookSelectActivity.this.listView.setVerticalScrollBarEnabled(false);
            PhonebookSelectActivity.this.emptyView.setText(LocaleController.getString("NoContacts", C1067R.string.NoContacts));
        }

        public void onTextChanged(EditText editText) {
            if (PhonebookSelectActivity.this.searchListViewAdapter != null) {
                String obj = editText.getText().toString();
                if (obj.length() != 0) {
                    PhonebookSelectActivity.this.searchWas = true;
                }
                PhonebookSelectActivity.this.searchListViewAdapter.search(obj);
            }
        }
    }

    /* renamed from: org.telegram.ui.PhonebookSelectActivity$6 */
    class C42656 extends OnScrollListener {
        C42656() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1 && PhonebookSelectActivity.this.searching && PhonebookSelectActivity.this.searchWas) {
                AndroidUtilities.hideKeyboard(PhonebookSelectActivity.this.getParentActivity().getCurrentFocus());
            }
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
    }

    public View createView(Context context) {
        this.searching = false;
        this.searchWas = false;
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("SelectContact", C1067R.string.SelectContact));
        this.actionBar.setActionBarMenuOnItemClick(new C42631());
        this.actionBar.createMenu().addItem(0, (int) C1067R.C1065drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new C42642()).setSearchFieldHint(LocaleController.getString("Search", C1067R.string.Search));
        this.searchListViewAdapter = new PhonebookSearchAdapter(context) {
            /* Access modifiers changed, original: protected */
            public void onUpdateSearchResults(String str) {
                if (!TextUtils.isEmpty(str) && PhonebookSelectActivity.this.listView != null && PhonebookSelectActivity.this.listView.getAdapter() != PhonebookSelectActivity.this.searchListViewAdapter) {
                    PhonebookSelectActivity.this.listView.setAdapter(PhonebookSelectActivity.this.searchListViewAdapter);
                    PhonebookSelectActivity.this.listView.setSectionsType(0);
                    PhonebookSelectActivity.this.searchListViewAdapter.notifyDataSetChanged();
                    PhonebookSelectActivity.this.listView.setFastScrollVisible(false);
                    PhonebookSelectActivity.this.listView.setVerticalScrollBarEnabled(true);
                    PhonebookSelectActivity.this.emptyView.setText(LocaleController.getString("NoResult", C1067R.string.NoResult));
                }
            }
        };
        this.listViewAdapter = new PhonebookAdapter(context) {
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                if (PhonebookSelectActivity.this.listView.getAdapter() == this) {
                    PhonebookSelectActivity.this.listView.setFastScrollVisible(super.getItemCount() != 0);
                }
            }
        };
        this.fragmentView = new FrameLayout(context) {
            /* Access modifiers changed, original: protected */
            public void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                if (PhonebookSelectActivity.this.listView.getAdapter() != PhonebookSelectActivity.this.listViewAdapter) {
                    PhonebookSelectActivity.this.emptyView.setTranslationY((float) AndroidUtilities.m26dp(0.0f));
                } else if (PhonebookSelectActivity.this.emptyView.getVisibility() == 0) {
                    PhonebookSelectActivity.this.emptyView.setTranslationY((float) AndroidUtilities.m26dp(74.0f));
                }
            }
        };
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.emptyView = new EmptyTextProgressView(context);
        this.emptyView.setShowAtCenter(true);
        this.emptyView.setText(LocaleController.getString("NoContacts", C1067R.string.NoContacts));
        this.emptyView.showTextView();
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView = new RecyclerListView(context);
        this.listView.setSectionsType(1);
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setFastScrollEnabled();
        this.listView.setEmptyView(this.emptyView);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setAdapter(this.listViewAdapter);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new C3807-$$Lambda$PhonebookSelectActivity$l53X2AdH9xoalnpyzoOuRLqxMRM(this));
        this.listView.setOnScrollListener(new C42656());
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$1$PhonebookSelectActivity(View view, int i) {
        Object item;
        if (this.searching && this.searchWas) {
            item = this.searchListViewAdapter.getItem(i);
        } else {
            int sectionForPosition = this.listViewAdapter.getSectionForPosition(i);
            i = this.listViewAdapter.getPositionInSectionForPosition(i);
            if (i >= 0 && sectionForPosition >= 0) {
                item = this.listViewAdapter.getItem(sectionForPosition, i);
            } else {
                return;
            }
        }
        if (item != null) {
            Contact contact;
            String formatName;
            if (item instanceof Contact) {
                contact = (Contact) item;
                User user = contact.user;
                formatName = user != null ? ContactsController.formatName(user.first_name, user.last_name) : "";
            } else {
                User user2 = (User) item;
                Contact contact2 = new Contact();
                contact2.first_name = user2.first_name;
                contact2.last_name = user2.last_name;
                contact2.phones.add(user2.phone);
                contact2.user = user2;
                Contact contact3 = contact2;
                formatName = ContactsController.formatName(contact2.first_name, contact2.last_name);
                contact = contact3;
            }
            PhonebookShareActivity phonebookShareActivity = new PhonebookShareActivity(contact, null, null, formatName);
            phonebookShareActivity.setDelegate(new C3805-$$Lambda$PhonebookSelectActivity$4oi9dmdaHeu97U36787Z9FtySPo(this));
            presentFragment(phonebookShareActivity);
        }
    }

    public /* synthetic */ void lambda$null$0$PhonebookSelectActivity(User user) {
        removeSelfFromStack();
        this.delegate.didSelectContact(user);
    }

    public void onResume() {
        super.onResume();
        PhonebookAdapter phonebookAdapter = this.listViewAdapter;
        if (phonebookAdapter != null) {
            phonebookAdapter.notifyDataSetChanged();
        }
    }

    public void onPause() {
        super.onPause();
        C2190ActionBar c2190ActionBar = this.actionBar;
        if (c2190ActionBar != null) {
            c2190ActionBar.closeSearchField();
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.contactsDidLoad) {
            PhonebookAdapter phonebookAdapter = this.listViewAdapter;
            if (phonebookAdapter != null) {
                phonebookAdapter.notifyDataSetChanged();
            }
        } else if (i == NotificationCenter.closeChats) {
            removeSelfFromStack();
        }
    }

    public void setDelegate(PhonebookSelectActivityDelegate phonebookSelectActivityDelegate) {
        this.delegate = phonebookSelectActivityDelegate;
    }

    public ThemeDescription[] getThemeDescriptions() {
        C3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc = new C3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc(this);
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[26];
        themeDescriptionArr[0] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[2] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch);
        themeDescriptionArr[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        View view = this.listView;
        View view2 = view;
        themeDescriptionArr[9] = new ThemeDescription(view2, ThemeDescription.FLAG_SECTIONS, new Class[]{LetterSectionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[10] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[11] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollActive);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollInactive);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollText);
        themeDescriptionArr[15] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        ThemeDescriptionDelegate themeDescriptionDelegate = c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc;
        themeDescriptionArr[16] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText);
        themeDescriptionArr[17] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        C3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2 = c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc;
        themeDescriptionArr[19] = new ThemeDescription(null, 0, null, null, null, c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2, Theme.key_avatar_backgroundRed);
        themeDescriptionArr[20] = new ThemeDescription(null, 0, null, null, null, c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2, Theme.key_avatar_backgroundOrange);
        themeDescriptionArr[21] = new ThemeDescription(null, 0, null, null, null, c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2, Theme.key_avatar_backgroundViolet);
        themeDescriptionArr[22] = new ThemeDescription(null, 0, null, null, null, c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2, Theme.key_avatar_backgroundGreen);
        themeDescriptionArr[23] = new ThemeDescription(null, 0, null, null, null, c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2, Theme.key_avatar_backgroundCyan);
        themeDescriptionArr[24] = new ThemeDescription(null, 0, null, null, null, c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2, Theme.key_avatar_backgroundBlue);
        themeDescriptionArr[25] = new ThemeDescription(null, 0, null, null, null, c3806-$$Lambda$PhonebookSelectActivity$6vwEKMfHE2uXMGZcFqjn-U8MjEc2, Theme.key_avatar_backgroundPink);
        return themeDescriptionArr;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$2$PhonebookSelectActivity() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                }
            }
        }
    }
}
