package org.telegram.p004ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Property;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.ContactsController.Contact;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem;
import org.telegram.p004ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.p004ui.Adapters.ContactsAdapter;
import org.telegram.p004ui.Adapters.SearchAdapter;
import org.telegram.p004ui.Cells.GraySectionCell;
import org.telegram.p004ui.Cells.LetterSectionCell;
import org.telegram.p004ui.Cells.ProfileSearchCell;
import org.telegram.p004ui.Cells.TextCell;
import org.telegram.p004ui.Cells.UserCell;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.ContactsActivity */
public class ContactsActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int search_button = 0;
    private static final int sort_button = 1;
    private boolean allowBots = true;
    private boolean allowUsernameSearch = true;
    private boolean askAboutContacts = true;
    private int channelId;
    private int chatId;
    private boolean checkPermission = true;
    private boolean createSecretChat;
    private boolean creatingChat;
    private ContactsActivityDelegate delegate;
    private boolean destroyAfterSelect;
    private EmptyTextProgressView emptyView;
    private ImageView floatingButton;
    private FrameLayout floatingButtonContainer;
    private boolean floatingHidden;
    private AccelerateDecelerateInterpolator floatingInterpolator = new AccelerateDecelerateInterpolator();
    private SparseArray<User> ignoreUsers;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ContactsAdapter listViewAdapter;
    private boolean needFinishFragment = true;
    private boolean needForwardCount = true;
    private boolean needPhonebook;
    private boolean onlyUsers;
    private AlertDialog permissionDialog;
    private int prevPosition;
    private int prevTop;
    private boolean returnAsResult;
    private boolean scrollUpdated;
    private SearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;
    private String selectAlertString = null;
    private boolean sortByName;
    private ActionBarMenuItem sortItem;

    /* renamed from: org.telegram.ui.ContactsActivity$6 */
    class C30086 extends ViewOutlineProvider {
        C30086() {
        }

        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.m26dp(56.0f), AndroidUtilities.m26dp(56.0f));
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$8 */
    class C30108 implements OnGlobalLayoutListener {
        C30108() {
        }

        public void onGlobalLayout() {
            ContactsActivity.this.floatingButtonContainer.setTranslationY((float) (ContactsActivity.this.floatingHidden ? AndroidUtilities.m26dp(100.0f) : 0));
            ContactsActivity.this.floatingButtonContainer.setClickable(ContactsActivity.this.floatingHidden ^ 1);
            if (ContactsActivity.this.floatingButtonContainer != null) {
                ContactsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$ContactsActivityDelegate */
    public interface ContactsActivityDelegate {
        void didSelectContact(User user, String str, ContactsActivity contactsActivity);
    }

    /* renamed from: org.telegram.ui.ContactsActivity$1 */
    class C41561 extends ActionBarMenuOnItemClick {
        C41561() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                ContactsActivity.this.finishFragment();
                return;
            }
            int i2 = 1;
            if (i == 1) {
                SharedConfig.toggleSortContactsByName();
                ContactsActivity.this.sortByName = SharedConfig.sortContactsByName;
                ContactsAdapter access$100 = ContactsActivity.this.listViewAdapter;
                if (!ContactsActivity.this.sortByName) {
                    i2 = 2;
                }
                access$100.setSortType(i2);
                ContactsActivity.this.sortItem.setIcon(ContactsActivity.this.sortByName ? C1067R.C1065drawable.contacts_sort_time : C1067R.C1065drawable.contacts_sort_name);
            }
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$2 */
    class C41572 extends ActionBarMenuItemSearchListener {
        C41572() {
        }

        public void onSearchExpand() {
            ContactsActivity.this.searching = true;
            if (ContactsActivity.this.floatingButtonContainer != null) {
                ContactsActivity.this.floatingButtonContainer.setVisibility(8);
            }
            if (ContactsActivity.this.sortItem != null) {
                ContactsActivity.this.sortItem.setVisibility(8);
            }
        }

        public void onSearchCollapse() {
            ContactsActivity.this.searchListViewAdapter.searchDialogs(null);
            ContactsActivity.this.searching = false;
            ContactsActivity.this.searchWas = false;
            ContactsActivity.this.listView.setAdapter(ContactsActivity.this.listViewAdapter);
            ContactsActivity.this.listView.setSectionsType(1);
            ContactsActivity.this.listViewAdapter.notifyDataSetChanged();
            ContactsActivity.this.listView.setFastScrollVisible(true);
            ContactsActivity.this.listView.setVerticalScrollBarEnabled(false);
            ContactsActivity.this.listView.setEmptyView(null);
            ContactsActivity.this.emptyView.setText(LocaleController.getString("NoContacts", C1067R.string.NoContacts));
            if (ContactsActivity.this.floatingButtonContainer != null) {
                ContactsActivity.this.floatingButtonContainer.setVisibility(0);
                ContactsActivity.this.floatingHidden = true;
                ContactsActivity.this.floatingButtonContainer.setTranslationY((float) AndroidUtilities.m26dp(100.0f));
                ContactsActivity.this.hideFloatingButton(false);
            }
            if (ContactsActivity.this.sortItem != null) {
                ContactsActivity.this.sortItem.setVisibility(0);
            }
        }

        public void onTextChanged(EditText editText) {
            if (ContactsActivity.this.searchListViewAdapter != null) {
                String obj = editText.getText().toString();
                if (obj.length() != 0) {
                    ContactsActivity.this.searchWas = true;
                    if (ContactsActivity.this.listView != null) {
                        ContactsActivity.this.listView.setAdapter(ContactsActivity.this.searchListViewAdapter);
                        ContactsActivity.this.listView.setSectionsType(0);
                        ContactsActivity.this.searchListViewAdapter.notifyDataSetChanged();
                        ContactsActivity.this.listView.setFastScrollVisible(false);
                        ContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                    }
                    if (ContactsActivity.this.emptyView != null) {
                        ContactsActivity.this.listView.setEmptyView(ContactsActivity.this.emptyView);
                        ContactsActivity.this.emptyView.setText(LocaleController.getString("NoResult", C1067R.string.NoResult));
                    }
                }
                ContactsActivity.this.searchListViewAdapter.searchDialogs(obj);
            }
        }
    }

    /* renamed from: org.telegram.ui.ContactsActivity$5 */
    class C41585 extends OnScrollListener {
        private boolean scrollingManually;

        C41585() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                if (ContactsActivity.this.searching && ContactsActivity.this.searchWas) {
                    AndroidUtilities.hideKeyboard(ContactsActivity.this.getParentActivity().getCurrentFocus());
                }
                this.scrollingManually = true;
                return;
            }
            this.scrollingManually = false;
        }

        /* JADX WARNING: Missing block: B:15:0x004f, code skipped:
            if (java.lang.Math.abs(r0) > 1) goto L_0x005d;
     */
        public void onScrolled(androidx.recyclerview.widget.RecyclerView r4, int r5, int r6) {
            /*
            r3 = this;
            super.onScrolled(r4, r5, r6);
            r5 = org.telegram.p004ui.ContactsActivity.this;
            r5 = r5.floatingButtonContainer;
            if (r5 == 0) goto L_0x0084;
        L_0x000b:
            r5 = org.telegram.p004ui.ContactsActivity.this;
            r5 = r5.floatingButtonContainer;
            r5 = r5.getVisibility();
            r6 = 8;
            if (r5 == r6) goto L_0x0084;
        L_0x0019:
            r5 = org.telegram.p004ui.ContactsActivity.this;
            r5 = r5.layoutManager;
            r5 = r5.findFirstVisibleItemPosition();
            r6 = 0;
            r4 = r4.getChildAt(r6);
            if (r4 == 0) goto L_0x002f;
        L_0x002a:
            r4 = r4.getTop();
            goto L_0x0030;
        L_0x002f:
            r4 = 0;
        L_0x0030:
            r0 = org.telegram.p004ui.ContactsActivity.this;
            r0 = r0.prevPosition;
            r1 = 1;
            if (r0 != r5) goto L_0x0052;
        L_0x0039:
            r0 = org.telegram.p004ui.ContactsActivity.this;
            r0 = r0.prevTop;
            r0 = r0 - r4;
            r2 = org.telegram.p004ui.ContactsActivity.this;
            r2 = r2.prevTop;
            if (r4 >= r2) goto L_0x004a;
        L_0x0048:
            r2 = 1;
            goto L_0x004b;
        L_0x004a:
            r2 = 0;
        L_0x004b:
            r0 = java.lang.Math.abs(r0);
            if (r0 <= r1) goto L_0x005e;
        L_0x0051:
            goto L_0x005d;
        L_0x0052:
            r0 = org.telegram.p004ui.ContactsActivity.this;
            r0 = r0.prevPosition;
            if (r5 <= r0) goto L_0x005c;
        L_0x005a:
            r2 = 1;
            goto L_0x005d;
        L_0x005c:
            r2 = 0;
        L_0x005d:
            r6 = 1;
        L_0x005e:
            if (r6 == 0) goto L_0x0075;
        L_0x0060:
            r6 = org.telegram.p004ui.ContactsActivity.this;
            r6 = r6.scrollUpdated;
            if (r6 == 0) goto L_0x0075;
        L_0x0068:
            if (r2 != 0) goto L_0x0070;
        L_0x006a:
            if (r2 != 0) goto L_0x0075;
        L_0x006c:
            r6 = r3.scrollingManually;
            if (r6 == 0) goto L_0x0075;
        L_0x0070:
            r6 = org.telegram.p004ui.ContactsActivity.this;
            r6.hideFloatingButton(r2);
        L_0x0075:
            r6 = org.telegram.p004ui.ContactsActivity.this;
            r6.prevPosition = r5;
            r5 = org.telegram.p004ui.ContactsActivity.this;
            r5.prevTop = r4;
            r4 = org.telegram.p004ui.ContactsActivity.this;
            r4.scrollUpdated = r1;
        L_0x0084:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ContactsActivity$C41585.onScrolled(androidx.recyclerview.widget.RecyclerView, int, int):void");
        }
    }

    public ContactsActivity(Bundle bundle) {
        super(bundle);
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.encryptedChatCreated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        this.checkPermission = UserConfig.getInstance(this.currentAccount).syncContacts;
        if (this.arguments != null) {
            this.onlyUsers = getArguments().getBoolean("onlyUsers", false);
            this.destroyAfterSelect = this.arguments.getBoolean("destroyAfterSelect", false);
            this.returnAsResult = this.arguments.getBoolean("returnAsResult", false);
            this.createSecretChat = this.arguments.getBoolean("createSecretChat", false);
            this.selectAlertString = this.arguments.getString("selectAlertString");
            this.allowUsernameSearch = this.arguments.getBoolean("allowUsernameSearch", true);
            this.needForwardCount = this.arguments.getBoolean("needForwardCount", true);
            this.allowBots = this.arguments.getBoolean("allowBots", true);
            this.channelId = this.arguments.getInt("channelId", 0);
            this.needFinishFragment = this.arguments.getBoolean("needFinishFragment", true);
            this.chatId = this.arguments.getInt("chat_id", 0);
        } else {
            this.needPhonebook = true;
        }
        if (!(this.createSecretChat || this.returnAsResult)) {
            this.sortByName = SharedConfig.sortContactsByName;
        }
        ContactsController.getInstance(this.currentAccount).checkInviteText();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.encryptedChatCreated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        this.delegate = null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0130  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x01d2  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x01cf  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01dd  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x01da  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x01e7  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x01f4  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x01f1  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x01ff  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01fc  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x023e  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x029b  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0307  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0304  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0310  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x030d  */
    public android.view.View createView(android.content.Context r23) {
        /*
        r22 = this;
        r7 = r22;
        r6 = r23;
        r5 = 0;
        r7.searching = r5;
        r7.searchWas = r5;
        r0 = r7.actionBar;
        r1 = 2131165409; // 0x7f0700e1 float:1.7945034E38 double:1.052935614E-314;
        r0.setBackButtonImage(r1);
        r0 = r7.actionBar;
        r4 = 1;
        r0.setAllowOverlayTitle(r4);
        r0 = r7.destroyAfterSelect;
        if (r0 == 0) goto L_0x0050;
    L_0x001b:
        r0 = r7.returnAsResult;
        if (r0 == 0) goto L_0x002e;
    L_0x001f:
        r0 = r7.actionBar;
        r1 = 2131560680; // 0x7f0d08e8 float:1.874674E38 double:1.053130904E-314;
        r2 = "SelectContact";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setTitle(r1);
        goto L_0x005e;
    L_0x002e:
        r0 = r7.createSecretChat;
        if (r0 == 0) goto L_0x0041;
    L_0x0032:
        r0 = r7.actionBar;
        r1 = 2131559909; // 0x7f0d05e5 float:1.8745175E38 double:1.053130523E-314;
        r2 = "NewSecretChat";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setTitle(r1);
        goto L_0x005e;
    L_0x0041:
        r0 = r7.actionBar;
        r1 = 2131559901; // 0x7f0d05dd float:1.874516E38 double:1.053130519E-314;
        r2 = "NewMessageTitle";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setTitle(r1);
        goto L_0x005e;
    L_0x0050:
        r0 = r7.actionBar;
        r1 = 2131559149; // 0x7f0d02ed float:1.8743634E38 double:1.0531301476E-314;
        r2 = "Contacts";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setTitle(r1);
    L_0x005e:
        r0 = r7.actionBar;
        r1 = new org.telegram.ui.ContactsActivity$1;
        r1.<init>();
        r0.setActionBarMenuOnItemClick(r1);
        r0 = r7.actionBar;
        r0 = r0.createMenu();
        r1 = 2131165419; // 0x7f0700eb float:1.7945055E38 double:1.052935619E-314;
        r1 = r0.addItem(r5, r1);
        r1 = r1.setIsSearchField(r4);
        r2 = new org.telegram.ui.ContactsActivity$2;
        r2.<init>();
        r1 = r1.setActionBarMenuItemSearchListener(r2);
        r2 = 2131560640; // 0x7f0d08c0 float:1.8746658E38 double:1.0531308843E-314;
        r3 = "Search";
        r8 = org.telegram.messenger.LocaleController.getString(r3, r2);
        r1.setSearchFieldHint(r8);
        r2 = org.telegram.messenger.LocaleController.getString(r3, r2);
        r1.setContentDescription(r2);
        r1 = r7.createSecretChat;
        if (r1 != 0) goto L_0x00bc;
    L_0x0099:
        r1 = r7.returnAsResult;
        if (r1 != 0) goto L_0x00bc;
    L_0x009d:
        r1 = r7.sortByName;
        if (r1 == 0) goto L_0x00a5;
    L_0x00a1:
        r1 = 2131165363; // 0x7f0700b3 float:1.794494E38 double:1.0529355915E-314;
        goto L_0x00a8;
    L_0x00a5:
        r1 = 2131165362; // 0x7f0700b2 float:1.7944939E38 double:1.052935591E-314;
    L_0x00a8:
        r0 = r0.addItem(r4, r1);
        r7.sortItem = r0;
        r0 = r7.sortItem;
        r1 = 2131558429; // 0x7f0d001d float:1.8742174E38 double:1.053129792E-314;
        r2 = "AccDescrContactSorting";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setContentDescription(r1);
    L_0x00bc:
        r0 = new org.telegram.ui.Adapters.SearchAdapter;
        r10 = r7.ignoreUsers;
        r11 = r7.allowUsernameSearch;
        r12 = 0;
        r13 = 0;
        r14 = r7.allowBots;
        r15 = 0;
        r8 = r0;
        r9 = r23;
        r8.<init>(r9, r10, r11, r12, r13, r14, r15);
        r7.searchListViewAdapter = r0;
        r0 = r7.chatId;
        r8 = 3;
        r9 = 2;
        if (r0 == 0) goto L_0x00eb;
    L_0x00d5:
        r0 = r7.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r7.chatId;
        r1 = java.lang.Integer.valueOf(r1);
        r0 = r0.getChat(r1);
        r0 = org.telegram.messenger.ChatObject.canUserDoAdminAction(r0, r8);
    L_0x00e9:
        r10 = r0;
        goto L_0x0112;
    L_0x00eb:
        r0 = r7.channelId;
        if (r0 == 0) goto L_0x0111;
    L_0x00ef:
        r0 = r7.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r7.channelId;
        r1 = java.lang.Integer.valueOf(r1);
        r0 = r0.getChat(r1);
        r1 = org.telegram.messenger.ChatObject.canUserDoAdminAction(r0, r8);
        if (r1 == 0) goto L_0x010f;
    L_0x0105:
        r0 = r0.username;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x010f;
    L_0x010d:
        r0 = 2;
        goto L_0x00e9;
    L_0x010f:
        r0 = 0;
        goto L_0x00e9;
    L_0x0111:
        r10 = 0;
    L_0x0112:
        r11 = new org.telegram.ui.ContactsActivity$3;
        r3 = r7.onlyUsers;
        r12 = r7.needPhonebook;
        r13 = r7.ignoreUsers;
        r0 = r11;
        r1 = r22;
        r2 = r23;
        r14 = 1;
        r4 = r12;
        r12 = 0;
        r5 = r13;
        r13 = r6;
        r6 = r10;
        r0.<init>(r2, r3, r4, r5, r6);
        r7.listViewAdapter = r11;
        r0 = r7.listViewAdapter;
        r1 = r7.sortItem;
        if (r1 == 0) goto L_0x0138;
    L_0x0130:
        r1 = r7.sortByName;
        if (r1 == 0) goto L_0x0136;
    L_0x0134:
        r1 = 1;
        goto L_0x0139;
    L_0x0136:
        r1 = 2;
        goto L_0x0139;
    L_0x0138:
        r1 = 0;
    L_0x0139:
        r0.setSortType(r1);
        r0 = new org.telegram.ui.ContactsActivity$4;
        r0.<init>(r13);
        r7.fragmentView = r0;
        r0 = r7.fragmentView;
        r0 = (android.widget.FrameLayout) r0;
        r1 = new org.telegram.ui.Components.EmptyTextProgressView;
        r1.<init>(r13);
        r7.emptyView = r1;
        r1 = r7.emptyView;
        r1.setShowAtCenter(r14);
        r1 = r7.emptyView;
        r2 = 2131559921; // 0x7f0d05f1 float:1.87452E38 double:1.053130529E-314;
        r3 = "NoContacts";
        r2 = org.telegram.messenger.LocaleController.getString(r3, r2);
        r1.setText(r2);
        r1 = r7.emptyView;
        r1.showTextView();
        r1 = r7.emptyView;
        r2 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r3 = -1;
        r4 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r3, r2);
        r0.addView(r1, r4);
        r1 = new org.telegram.ui.Components.RecyclerListView;
        r1.<init>(r13);
        r7.listView = r1;
        r1 = r7.listView;
        r1.setSectionsType(r14);
        r1 = r7.listView;
        r1.setVerticalScrollBarEnabled(r12);
        r1 = r7.listView;
        r1.setFastScrollEnabled();
        r1 = r7.listView;
        r4 = new androidx.recyclerview.widget.LinearLayoutManager;
        r4.<init>(r13, r14, r12);
        r7.layoutManager = r4;
        r1.setLayoutManager(r4);
        r1 = r7.listView;
        r4 = r7.listViewAdapter;
        r1.setAdapter(r4);
        r1 = r7.listView;
        r2 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r3, r2);
        r0.addView(r1, r2);
        r1 = r7.listView;
        r2 = new org.telegram.ui.-$$Lambda$ContactsActivity$gnTnCjocys4rzqdEYocufNT6sRA;
        r2.<init>(r7, r10);
        r1.setOnItemClickListener(r2);
        r1 = r7.listView;
        r2 = new org.telegram.ui.ContactsActivity$5;
        r2.<init>();
        r1.setOnScrollListener(r2);
        r1 = r7.createSecretChat;
        if (r1 != 0) goto L_0x0322;
    L_0x01bc:
        r1 = r7.returnAsResult;
        if (r1 != 0) goto L_0x0322;
    L_0x01c0:
        r1 = new android.widget.FrameLayout;
        r1.<init>(r13);
        r7.floatingButtonContainer = r1;
        r1 = r7.floatingButtonContainer;
        r2 = android.os.Build.VERSION.SDK_INT;
        r5 = 21;
        if (r2 < r5) goto L_0x01d2;
    L_0x01cf:
        r2 = 56;
        goto L_0x01d4;
    L_0x01d2:
        r2 = 60;
    L_0x01d4:
        r15 = r2 + 20;
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 < r5) goto L_0x01dd;
    L_0x01da:
        r2 = 56;
        goto L_0x01df;
    L_0x01dd:
        r2 = 60;
    L_0x01df:
        r2 = r2 + 14;
        r2 = (float) r2;
        r6 = org.telegram.messenger.LocaleController.isRTL;
        if (r6 == 0) goto L_0x01e7;
    L_0x01e6:
        goto L_0x01e8;
    L_0x01e7:
        r8 = 5;
    L_0x01e8:
        r17 = r8 | 80;
        r6 = org.telegram.messenger.LocaleController.isRTL;
        r8 = 0;
        r10 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        if (r6 == 0) goto L_0x01f4;
    L_0x01f1:
        r18 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        goto L_0x01f6;
    L_0x01f4:
        r18 = 0;
    L_0x01f6:
        r19 = 0;
        r6 = org.telegram.messenger.LocaleController.isRTL;
        if (r6 == 0) goto L_0x01ff;
    L_0x01fc:
        r20 = 0;
        goto L_0x0201;
    L_0x01ff:
        r20 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
    L_0x0201:
        r21 = 0;
        r16 = r2;
        r2 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r15, r16, r17, r18, r19, r20, r21);
        r0.addView(r1, r2);
        r0 = r7.floatingButtonContainer;
        r1 = new org.telegram.ui.-$$Lambda$ContactsActivity$XQCfC9ukjnkgkb2J-v7QLg6mf6E;
        r1.<init>(r7);
        r0.setOnClickListener(r1);
        r0 = new android.widget.ImageView;
        r0.<init>(r13);
        r7.floatingButton = r0;
        r0 = r7.floatingButton;
        r1 = android.widget.ImageView.ScaleType.CENTER;
        r0.setScaleType(r1);
        r0 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r2 = "chats_actionBackground";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r6 = "chats_actionPressedBackground";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r1 = org.telegram.p004ui.ActionBar.Theme.createSimpleSelectorCircleDrawable(r1, r2, r6);
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 >= r5) goto L_0x026a;
    L_0x023e:
        r2 = r23.getResources();
        r6 = 2131165387; // 0x7f0700cb float:1.794499E38 double:1.0529356033E-314;
        r2 = r2.getDrawable(r6);
        r2 = r2.mutate();
        r6 = new android.graphics.PorterDuffColorFilter;
        r8 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r11 = android.graphics.PorterDuff.Mode.MULTIPLY;
        r6.<init>(r8, r11);
        r2.setColorFilter(r6);
        r6 = new org.telegram.ui.Components.CombinedDrawable;
        r6.<init>(r2, r1, r12, r12);
        r1 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r0 = org.telegram.messenger.AndroidUtilities.m26dp(r0);
        r6.setIconSize(r1, r0);
        r1 = r6;
    L_0x026a:
        r0 = r7.floatingButton;
        r0.setBackgroundDrawable(r1);
        r0 = r7.floatingButton;
        r1 = new android.graphics.PorterDuffColorFilter;
        r2 = "chats_actionIcon";
        r2 = org.telegram.p004ui.ActionBar.Theme.getColor(r2);
        r6 = android.graphics.PorterDuff.Mode.MULTIPLY;
        r1.<init>(r2, r6);
        r0.setColorFilter(r1);
        r0 = r7.floatingButton;
        r1 = 2131165280; // 0x7f070060 float:1.7944773E38 double:1.0529355505E-314;
        r0.setImageResource(r1);
        r0 = r7.floatingButtonContainer;
        r1 = 2131559170; // 0x7f0d0302 float:1.8743676E38 double:1.053130158E-314;
        r2 = "CreateNewContact";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setContentDescription(r1);
        r0 = android.os.Build.VERSION.SDK_INT;
        if (r0 < r5) goto L_0x02fc;
    L_0x029b:
        r0 = new android.animation.StateListAnimator;
        r0.<init>();
        r1 = new int[r14];
        r2 = 16842919; // 0x10100a7 float:2.3694026E-38 double:8.3215077E-317;
        r1[r12] = r2;
        r2 = r7.floatingButton;
        r6 = android.view.View.TRANSLATION_Z;
        r8 = new float[r9];
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r13 = (float) r13;
        r8[r12] = r13;
        r13 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r13 = (float) r13;
        r8[r14] = r13;
        r2 = android.animation.ObjectAnimator.ofFloat(r2, r6, r8);
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r2 = r2.setDuration(r3);
        r0.addState(r1, r2);
        r1 = new int[r12];
        r2 = r7.floatingButton;
        r3 = android.view.View.TRANSLATION_Z;
        r4 = new float[r9];
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r10);
        r9 = (float) r9;
        r4[r12] = r9;
        r9 = org.telegram.messenger.AndroidUtilities.m26dp(r11);
        r9 = (float) r9;
        r4[r14] = r9;
        r2 = android.animation.ObjectAnimator.ofFloat(r2, r3, r4);
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r2 = r2.setDuration(r3);
        r0.addState(r1, r2);
        r1 = r7.floatingButton;
        r1.setStateListAnimator(r0);
        r0 = r7.floatingButton;
        r1 = new org.telegram.ui.ContactsActivity$6;
        r1.<init>();
        r0.setOutlineProvider(r1);
    L_0x02fc:
        r0 = r7.floatingButtonContainer;
        r1 = r7.floatingButton;
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 < r5) goto L_0x0307;
    L_0x0304:
        r9 = 56;
        goto L_0x0309;
    L_0x0307:
        r9 = 60;
    L_0x0309:
        r2 = android.os.Build.VERSION.SDK_INT;
        if (r2 < r5) goto L_0x0310;
    L_0x030d:
        r6 = 56;
        goto L_0x0312;
    L_0x0310:
        r6 = 60;
    L_0x0312:
        r10 = (float) r6;
        r11 = 51;
        r12 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r13 = 0;
        r14 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r15 = 0;
        r2 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r9, r10, r11, r12, r13, r14, r15);
        r0.addView(r1, r2);
    L_0x0322:
        r0 = r7.fragmentView;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ContactsActivity.createView(android.content.Context):android.view.View");
    }

    public /* synthetic */ void lambda$createView$1$ContactsActivity(int i, View view, int i2) {
        String str = "user_id";
        User user;
        SparseArray sparseArray;
        Bundle bundle;
        if (this.searching && this.searchWas) {
            user = (User) this.searchListViewAdapter.getItem(i2);
            if (user != null) {
                if (this.searchListViewAdapter.isGlobalSearch(i2)) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(user);
                    MessagesController.getInstance(this.currentAccount).putUsers(arrayList, false);
                    MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(arrayList, null, false, true);
                }
                if (this.returnAsResult) {
                    sparseArray = this.ignoreUsers;
                    if (sparseArray == null || sparseArray.indexOfKey(user.f534id) < 0) {
                        didSelectResult(user, true, null);
                    } else {
                        return;
                    }
                } else if (!this.createSecretChat) {
                    bundle = new Bundle();
                    bundle.putInt(str, user.f534id);
                    if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, this)) {
                        presentFragment(new ChatActivity(bundle), true);
                    }
                } else if (user.f534id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    this.creatingChat = true;
                    SecretChatHelper.getInstance(this.currentAccount).startSecretChat(getParentActivity(), user);
                } else {
                    return;
                }
            }
            return;
        }
        int sectionForPosition = this.listViewAdapter.getSectionForPosition(i2);
        i2 = this.listViewAdapter.getPositionInSectionForPosition(i2);
        if (i2 >= 0 && sectionForPosition >= 0) {
            Bundle bundle2;
            if ((this.onlyUsers && i == 0) || sectionForPosition != 0) {
                Object item = this.listViewAdapter.getItem(sectionForPosition, i2);
                if (item instanceof User) {
                    user = (User) item;
                    if (this.returnAsResult) {
                        sparseArray = this.ignoreUsers;
                        if (sparseArray == null || sparseArray.indexOfKey(user.f534id) < 0) {
                            didSelectResult(user, true, null);
                        }
                    } else if (this.createSecretChat) {
                        this.creatingChat = true;
                        SecretChatHelper.getInstance(this.currentAccount).startSecretChat(getParentActivity(), user);
                    } else {
                        bundle = new Bundle();
                        bundle.putInt(str, user.f534id);
                        if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(bundle, this)) {
                            presentFragment(new ChatActivity(bundle), true);
                        }
                    }
                } else if (item instanceof Contact) {
                    Contact contact = (Contact) item;
                    String str2 = !contact.phones.isEmpty() ? (String) contact.phones.get(0) : null;
                    if (!(str2 == null || getParentActivity() == null)) {
                        Builder builder = new Builder(getParentActivity());
                        builder.setMessage(LocaleController.getString("InviteUser", C1067R.string.InviteUser));
                        builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                        builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1466-$$Lambda$ContactsActivity$UtCNWtIF8nw25DCjjOFgiXVoPRQ(this, str2));
                        builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                        showDialog(builder.create());
                    }
                }
            } else if (this.needPhonebook) {
                if (i2 == 0) {
                    presentFragment(new InviteContactsActivity());
                }
            } else if (i != 0) {
                if (i2 == 0) {
                    sectionForPosition = this.chatId;
                    if (sectionForPosition == 0) {
                        sectionForPosition = this.channelId;
                    }
                    presentFragment(new GroupInviteActivity(sectionForPosition));
                }
            } else if (i2 == 0) {
                presentFragment(new GroupCreateActivity(new Bundle()), false);
            } else if (i2 == 1) {
                bundle2 = new Bundle();
                bundle2.putBoolean("onlyUsers", true);
                bundle2.putBoolean("destroyAfterSelect", true);
                bundle2.putBoolean("createSecretChat", true);
                bundle2.putBoolean("allowBots", false);
                presentFragment(new ContactsActivity(bundle2), false);
            } else if (i2 == 2) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                String str3 = "channel_intro";
                if (BuildVars.DEBUG_VERSION || !globalMainSettings.getBoolean(str3, false)) {
                    presentFragment(new ChannelIntroActivity());
                    globalMainSettings.edit().putBoolean(str3, true).commit();
                } else {
                    bundle2 = new Bundle();
                    bundle2.putInt("step", 0);
                    presentFragment(new ChannelCreateActivity(bundle2));
                }
            }
        }
    }

    public /* synthetic */ void lambda$null$0$ContactsActivity(String str, DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.fromParts("sms", str, null));
            intent.putExtra("sms_body", ContactsController.getInstance(this.currentAccount).getInviteText(1));
            getParentActivity().startActivityForResult(intent, 500);
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public /* synthetic */ void lambda$createView$2$ContactsActivity(View view) {
        presentFragment(new NewContactActivity());
    }

    private void didSelectResult(User user, boolean z, String str) {
        if (!z || this.selectAlertString == null) {
            ContactsActivityDelegate contactsActivityDelegate = this.delegate;
            if (contactsActivityDelegate != null) {
                contactsActivityDelegate.didSelectContact(user, str, this);
                this.delegate = null;
            }
            if (this.needFinishFragment) {
                finishFragment();
            }
        } else if (getParentActivity() != null) {
            EditText editText;
            String str2 = "Cancel";
            String str3 = "OK";
            String str4 = "AppName";
            if (user.bot) {
                if (user.bot_nochats) {
                    try {
                        Toast.makeText(getParentActivity(), LocaleController.getString("BotCantJoinGroups", C1067R.string.BotCantJoinGroups), 0).show();
                    } catch (Exception e) {
                        FileLog.m30e(e);
                    }
                    return;
                } else if (this.channelId != 0) {
                    Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.channelId));
                    Builder builder = new Builder(getParentActivity());
                    if (ChatObject.canAddAdmins(chat)) {
                        builder.setTitle(LocaleController.getString(str4, C1067R.string.AppName));
                        builder.setMessage(LocaleController.getString("AddBotAsAdmin", C1067R.string.AddBotAsAdmin));
                        builder.setPositiveButton(LocaleController.getString("MakeAdmin", C1067R.string.MakeAdmin), new C1469-$$Lambda$ContactsActivity$mmdvMVULktgHXYl_8FoV1fI5DjI(this, user, str));
                        builder.setNegativeButton(LocaleController.getString(str2, C1067R.string.Cancel), null);
                    } else {
                        builder.setMessage(LocaleController.getString("CantAddBotAsAdmin", C1067R.string.CantAddBotAsAdmin));
                        builder.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), null);
                    }
                    showDialog(builder.create());
                    return;
                }
            }
            Builder builder2 = new Builder(getParentActivity());
            builder2.setTitle(LocaleController.getString(str4, C1067R.string.AppName));
            CharSequence formatStringSimple = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user));
            if (user.bot || !this.needForwardCount) {
                editText = null;
            } else {
                formatStringSimple = String.format("%s\n\n%s", new Object[]{formatStringSimple, LocaleController.getString("AddToTheGroupForwardCount", C1067R.string.AddToTheGroupForwardCount)});
                editText = new EditText(getParentActivity());
                editText.setTextSize(1, 18.0f);
                editText.setText("50");
                editText.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
                editText.setGravity(17);
                editText.setInputType(2);
                editText.setImeOptions(6);
                editText.setBackgroundDrawable(Theme.createEditTextDrawable(getParentActivity(), true));
                editText.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    public void afterTextChanged(Editable editable) {
                        String str = "";
                        try {
                            String obj = editable.toString();
                            if (obj.length() != 0) {
                                int intValue = Utilities.parseInt(obj).intValue();
                                if (intValue < 0) {
                                    editText.setText("0");
                                    editText.setSelection(editText.length());
                                } else if (intValue > 300) {
                                    editText.setText("300");
                                    editText.setSelection(editText.length());
                                } else {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append(str);
                                    stringBuilder.append(intValue);
                                    if (!obj.equals(stringBuilder.toString())) {
                                        EditText editText = editText;
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(str);
                                        stringBuilder.append(intValue);
                                        editText.setText(stringBuilder.toString());
                                        editText.setSelection(editText.length());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            FileLog.m30e(e);
                        }
                    }
                });
                builder2.setView(editText);
            }
            builder2.setMessage(formatStringSimple);
            builder2.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), new C1468-$$Lambda$ContactsActivity$YV_dxfbBQkZGaBy_yZbP6YhG1n0(this, user, editText));
            builder2.setNegativeButton(LocaleController.getString(str2, C1067R.string.Cancel), null);
            showDialog(builder2.create());
            if (editText != null) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) editText.getLayoutParams();
                if (marginLayoutParams != null) {
                    if (marginLayoutParams instanceof LayoutParams) {
                        ((LayoutParams) marginLayoutParams).gravity = 1;
                    }
                    int dp = AndroidUtilities.m26dp(24.0f);
                    marginLayoutParams.leftMargin = dp;
                    marginLayoutParams.rightMargin = dp;
                    marginLayoutParams.height = AndroidUtilities.m26dp(36.0f);
                    editText.setLayoutParams(marginLayoutParams);
                }
                editText.setSelection(editText.getText().length());
            }
        }
    }

    public /* synthetic */ void lambda$didSelectResult$3$ContactsActivity(User user, String str, DialogInterface dialogInterface, int i) {
        ContactsActivityDelegate contactsActivityDelegate = this.delegate;
        if (contactsActivityDelegate != null) {
            contactsActivityDelegate.didSelectContact(user, str, this);
            this.delegate = null;
        }
    }

    public /* synthetic */ void lambda$didSelectResult$4$ContactsActivity(User user, EditText editText, DialogInterface dialogInterface, int i) {
        didSelectResult(user, false, editText != null ? editText.getText().toString() : "0");
    }

    public void onResume() {
        super.onResume();
        ContactsAdapter contactsAdapter = this.listViewAdapter;
        if (contactsAdapter != null) {
            contactsAdapter.notifyDataSetChanged();
        }
        if (this.checkPermission && VERSION.SDK_INT >= 23) {
            Activity parentActivity = getParentActivity();
            if (parentActivity != null) {
                this.checkPermission = false;
                String str = "android.permission.READ_CONTACTS";
                if (parentActivity.checkSelfPermission(str) == 0) {
                    return;
                }
                if (parentActivity.shouldShowRequestPermissionRationale(str)) {
                    AlertDialog create = AlertsCreator.createContactsPermissionDialog(parentActivity, new C3675-$$Lambda$ContactsActivity$xFVgeouE3JWG-nldltsKWVnF4Hg(this)).create();
                    this.permissionDialog = create;
                    showDialog(create);
                    return;
                }
                askForPermissons(true);
            }
        }
    }

    public /* synthetic */ void lambda$onResume$5$ContactsActivity(int i) {
        this.askAboutContacts = i != 0;
        if (i != 0) {
            askForPermissons(false);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FrameLayout frameLayout = this.floatingButtonContainer;
        if (frameLayout != null) {
            frameLayout.getViewTreeObserver().addOnGlobalLayoutListener(new C30108());
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDialogDismiss(Dialog dialog) {
        super.onDialogDismiss(dialog);
        Dialog dialog2 = this.permissionDialog;
        if (dialog2 != null && dialog == dialog2 && getParentActivity() != null && this.askAboutContacts) {
            askForPermissons(false);
        }
    }

    @TargetApi(23)
    private void askForPermissons(boolean z) {
        Activity parentActivity = getParentActivity();
        if (parentActivity != null && UserConfig.getInstance(this.currentAccount).syncContacts) {
            String str = "android.permission.READ_CONTACTS";
            if (parentActivity.checkSelfPermission(str) != 0) {
                if (z && this.askAboutContacts) {
                    showDialog(AlertsCreator.createContactsPermissionDialog(parentActivity, new C3674-$$Lambda$ContactsActivity$mwsX5pHa6b8khBUhCBIBphXHFEY(this)).create());
                    return;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(str);
                arrayList.add("android.permission.WRITE_CONTACTS");
                arrayList.add("android.permission.GET_ACCOUNTS");
                parentActivity.requestPermissions((String[]) arrayList.toArray(new String[0]), 1);
            }
        }
    }

    public /* synthetic */ void lambda$askForPermissons$6$ContactsActivity(int i) {
        this.askAboutContacts = i != 0;
        if (i != 0) {
            askForPermissons(false);
        }
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (i == 1) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (iArr.length > i2) {
                    if ("android.permission.READ_CONTACTS".equals(strArr[i2])) {
                        if (iArr[i2] == 0) {
                            ContactsController.getInstance(this.currentAccount).forceImportContacts();
                        } else {
                            Editor edit = MessagesController.getGlobalNotificationsSettings().edit();
                            this.askAboutContacts = false;
                            edit.putBoolean("askAboutContacts", false).commit();
                        }
                    }
                }
            }
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
        ContactsAdapter contactsAdapter;
        if (i == NotificationCenter.contactsDidLoad) {
            contactsAdapter = this.listViewAdapter;
            if (contactsAdapter != null) {
                contactsAdapter.notifyDataSetChanged();
            }
        } else if (i == NotificationCenter.updateInterfaces) {
            i = ((Integer) objArr[0]).intValue();
            if (!((i & 2) == 0 && (i & 1) == 0 && (i & 4) == 0)) {
                updateVisibleRows(i);
            }
            if ((i & 4) != 0 && !this.sortByName) {
                contactsAdapter = this.listViewAdapter;
                if (contactsAdapter != null) {
                    contactsAdapter.sortOnlineContacts();
                }
            }
        } else if (i == NotificationCenter.encryptedChatCreated) {
            if (this.createSecretChat && this.creatingChat) {
                EncryptedChat encryptedChat = (EncryptedChat) objArr[0];
                Bundle bundle = new Bundle();
                bundle.putInt("enc_id", encryptedChat.f445id);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                presentFragment(new ChatActivity(bundle), true);
            }
        } else if (i == NotificationCenter.closeChats && !this.creatingChat) {
            removeSelfFromStack();
        }
    }

    private void updateVisibleRows(int i) {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = this.listView.getChildAt(i2);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(i);
                }
            }
        }
    }

    private void hideFloatingButton(boolean z) {
        if (this.floatingHidden != z) {
            this.floatingHidden = z;
            AnimatorSet animatorSet = new AnimatorSet();
            Animator[] animatorArr = new Animator[1];
            FrameLayout frameLayout = this.floatingButtonContainer;
            Property property = View.TRANSLATION_Y;
            float[] fArr = new float[1];
            fArr[0] = (float) (this.floatingHidden ? AndroidUtilities.m26dp(100.0f) : 0);
            animatorArr[0] = ObjectAnimator.ofFloat(frameLayout, property, fArr);
            animatorSet.playTogether(animatorArr);
            animatorSet.setDuration(300);
            animatorSet.setInterpolator(this.floatingInterpolator);
            this.floatingButtonContainer.setClickable(z ^ 1);
            animatorSet.start();
        }
    }

    public void setDelegate(ContactsActivityDelegate contactsActivityDelegate) {
        this.delegate = contactsActivityDelegate;
    }

    public void setIgnoreUsers(SparseArray<User> sparseArray) {
        this.ignoreUsers = sparseArray;
    }

    public ThemeDescription[] getThemeDescriptions() {
        C3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY = new C3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY(this);
        r11 = new ThemeDescription[40];
        r11[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r11[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch);
        r11[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder);
        r11[8] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        View view = this.listView;
        int i = ThemeDescription.FLAG_SECTIONS;
        Class[] clsArr = new Class[]{LetterSectionCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        r11[9] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r11[10] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r11[11] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        r11[12] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollActive);
        r11[13] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollInactive);
        r11[14] = new ThemeDescription(this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, Theme.key_fastScrollText);
        r11[15] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        ThemeDescriptionDelegate themeDescriptionDelegate = c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY;
        r11[16] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteGrayText);
        r11[17] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, null, null, themeDescriptionDelegate, Theme.key_windowBackgroundWhiteBlueText);
        r11[18] = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        C3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2 = c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY;
        r11[19] = new ThemeDescription(null, 0, null, null, null, c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2, Theme.key_avatar_backgroundRed);
        r11[20] = new ThemeDescription(null, 0, null, null, null, c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2, Theme.key_avatar_backgroundOrange);
        r11[21] = new ThemeDescription(null, 0, null, null, null, c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2, Theme.key_avatar_backgroundViolet);
        r11[22] = new ThemeDescription(null, 0, null, null, null, c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2, Theme.key_avatar_backgroundGreen);
        r11[23] = new ThemeDescription(null, 0, null, null, null, c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2, Theme.key_avatar_backgroundCyan);
        r11[24] = new ThemeDescription(null, 0, null, null, null, c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2, Theme.key_avatar_backgroundBlue);
        r11[25] = new ThemeDescription(null, 0, null, null, null, c3673-$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY2, Theme.key_avatar_backgroundPink);
        r11[26] = new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r11[27] = new ThemeDescription(this.listView, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r11[28] = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionIcon);
        r11[29] = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionBackground);
        r11[30] = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chats_actionPressedBackground);
        r11[31] = new ThemeDescription(this.listView, 0, new Class[]{GraySectionCell.class}, new String[]{"textView"}, null, null, null, Theme.key_graySectionText);
        r11[32] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{GraySectionCell.class}, null, null, null, Theme.key_graySection);
        r11[33] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_groupDrawable, Theme.dialogs_broadcastDrawable, Theme.dialogs_botDrawable}, null, Theme.key_chats_nameIcon);
        r11[34] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedCheckDrawable}, null, Theme.key_chats_verifiedCheck);
        r11[35] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedDrawable}, null, Theme.key_chats_verifiedBackground);
        r11[36] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_offlinePaint, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r11[37] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_onlinePaint, null, null, Theme.key_windowBackgroundWhiteBlueText3);
        r11[38] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Paint[]{Theme.dialogs_namePaint, Theme.dialogs_searchNamePaint}, null, null, Theme.key_chats_name);
        r11[39] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Paint[]{Theme.dialogs_nameEncryptedPaint, Theme.dialogs_searchNameEncryptedPaint}, null, null, Theme.key_chats_secretName);
        return r11;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$7$ContactsActivity() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof UserCell) {
                    ((UserCell) childAt).update(0);
                } else if (childAt instanceof ProfileSearchCell) {
                    ((ProfileSearchCell) childAt).update(0);
                }
            }
        }
    }
}
