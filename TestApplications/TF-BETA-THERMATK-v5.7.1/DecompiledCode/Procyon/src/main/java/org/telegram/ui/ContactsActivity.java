// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.content.SharedPreferences$Editor;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.content.res.Configuration;
import android.content.Intent;
import android.net.Uri;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.LetterSectionCell;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ActionBarMenu;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.StateListAnimator;
import android.graphics.drawable.Drawable;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import android.view.View$OnClickListener;
import android.os.Build$VERSION;
import org.telegram.ui.Components.LayoutHelper;
import android.text.TextUtils;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.Cells.UserCell;
import android.util.Property;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import android.widget.FrameLayout$LayoutParams;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.View;
import org.telegram.messenger.Utilities;
import android.text.Editable;
import android.text.TextWatcher;
import org.telegram.ui.ActionBar.Theme;
import android.widget.EditText;
import org.telegram.messenger.UserObject;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.FileLog;
import android.content.Context;
import android.widget.Toast;
import org.telegram.messenger.LocaleController;
import android.annotation.TargetApi;
import android.app.Activity;
import java.util.ArrayList;
import android.app.Dialog;
import org.telegram.messenger.MessagesStorage;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.UserConfig;
import android.os.Bundle;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Adapters.SearchAdapter;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Adapters.ContactsAdapter;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.tgnet.TLRPC;
import android.util.SparseArray;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ContactsActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int search_button = 0;
    private static final int sort_button = 1;
    private boolean allowBots;
    private boolean allowUsernameSearch;
    private boolean askAboutContacts;
    private int channelId;
    private int chatId;
    private boolean checkPermission;
    private boolean createSecretChat;
    private boolean creatingChat;
    private ContactsActivityDelegate delegate;
    private boolean destroyAfterSelect;
    private EmptyTextProgressView emptyView;
    private ImageView floatingButton;
    private FrameLayout floatingButtonContainer;
    private boolean floatingHidden;
    private AccelerateDecelerateInterpolator floatingInterpolator;
    private SparseArray<TLRPC.User> ignoreUsers;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ContactsAdapter listViewAdapter;
    private boolean needFinishFragment;
    private boolean needForwardCount;
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
    private String selectAlertString;
    private boolean sortByName;
    private ActionBarMenuItem sortItem;
    
    public ContactsActivity(final Bundle bundle) {
        super(bundle);
        this.floatingInterpolator = new AccelerateDecelerateInterpolator();
        this.allowBots = true;
        this.needForwardCount = true;
        this.needFinishFragment = true;
        this.selectAlertString = null;
        this.allowUsernameSearch = true;
        this.askAboutContacts = true;
        this.checkPermission = true;
    }
    
    @TargetApi(23)
    private void askForPermissons(final boolean b) {
        final Activity parentActivity = this.getParentActivity();
        if (parentActivity != null && UserConfig.getInstance(super.currentAccount).syncContacts) {
            if (parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
                if (b && this.askAboutContacts) {
                    this.showDialog(AlertsCreator.createContactsPermissionDialog(parentActivity, new _$$Lambda$ContactsActivity$mwsX5pHa6b8khBUhCBIBphXHFEY(this)).create());
                    return;
                }
                final ArrayList<String> list = new ArrayList<String>();
                list.add("android.permission.READ_CONTACTS");
                list.add("android.permission.WRITE_CONTACTS");
                list.add("android.permission.GET_ACCOUNTS");
                parentActivity.requestPermissions((String[])list.toArray(new String[0]), 1);
            }
        }
    }
    
    private void didSelectResult(final TLRPC.User user, final boolean b, final String s) {
        if (b && this.selectAlertString != null) {
            if (this.getParentActivity() == null) {
                return;
            }
            if (user.bot) {
                if (user.bot_nochats) {
                    try {
                        Toast.makeText((Context)this.getParentActivity(), (CharSequence)LocaleController.getString("BotCantJoinGroups", 2131558849), 0).show();
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    return;
                }
                if (this.channelId != 0) {
                    final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.channelId);
                    final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
                    if (ChatObject.canAddAdmins(chat)) {
                        builder.setTitle(LocaleController.getString("AppName", 2131558635));
                        builder.setMessage(LocaleController.getString("AddBotAsAdmin", 2131558565));
                        builder.setPositiveButton(LocaleController.getString("MakeAdmin", 2131559795), (DialogInterface$OnClickListener)new _$$Lambda$ContactsActivity$mmdvMVULktgHXYl_8FoV1fI5DjI(this, user, s));
                        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                    }
                    else {
                        builder.setMessage(LocaleController.getString("CantAddBotAsAdmin", 2131558902));
                        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                    }
                    this.showDialog(builder.create());
                    return;
                }
            }
            final AlertDialog.Builder builder2 = new AlertDialog.Builder((Context)this.getParentActivity());
            builder2.setTitle(LocaleController.getString("AppName", 2131558635));
            String message = LocaleController.formatStringSimple(this.selectAlertString, UserObject.getUserName(user));
            Object view;
            if (!user.bot && this.needForwardCount) {
                message = String.format("%s\n\n%s", message, LocaleController.getString("AddToTheGroupForwardCount", 2131558595));
                view = new EditText((Context)this.getParentActivity());
                ((EditText)view).setTextSize(1, 18.0f);
                ((EditText)view).setText((CharSequence)"50");
                ((EditText)view).setTextColor(Theme.getColor("dialogTextBlack"));
                ((EditText)view).setGravity(17);
                ((EditText)view).setInputType(2);
                ((EditText)view).setImeOptions(6);
                ((EditText)view).setBackgroundDrawable(Theme.createEditTextDrawable((Context)this.getParentActivity(), true));
                ((EditText)view).addTextChangedListener((TextWatcher)new TextWatcher() {
                    public void afterTextChanged(final Editable editable) {
                        try {
                            final String string = editable.toString();
                            if (string.length() != 0) {
                                final int intValue = Utilities.parseInt(string);
                                if (intValue < 0) {
                                    ((EditText)view).setText((CharSequence)"0");
                                    ((EditText)view).setSelection(((EditText)view).length());
                                }
                                else if (intValue > 300) {
                                    ((EditText)view).setText((CharSequence)"300");
                                    ((EditText)view).setSelection(((EditText)view).length());
                                }
                                else {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("");
                                    sb.append(intValue);
                                    if (!string.equals(sb.toString())) {
                                        final EditText val$editTextFinal = (EditText)view;
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("");
                                        sb2.append(intValue);
                                        val$editTextFinal.setText((CharSequence)sb2.toString());
                                        ((EditText)view).setSelection(((EditText)view).length());
                                    }
                                }
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    
                    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                    
                    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                    }
                });
                builder2.setView((View)view);
            }
            else {
                view = null;
            }
            builder2.setMessage(message);
            builder2.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$ContactsActivity$YV_dxfbBQkZGaBy_yZbP6YhG1n0(this, user, (EditText)view));
            builder2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            this.showDialog(builder2.create());
            if (view != null) {
                final ViewGroup$MarginLayoutParams layoutParams = (ViewGroup$MarginLayoutParams)((EditText)view).getLayoutParams();
                if (layoutParams != null) {
                    if (layoutParams instanceof FrameLayout$LayoutParams) {
                        ((FrameLayout$LayoutParams)layoutParams).gravity = 1;
                    }
                    final int dp = AndroidUtilities.dp(24.0f);
                    layoutParams.leftMargin = dp;
                    layoutParams.rightMargin = dp;
                    layoutParams.height = AndroidUtilities.dp(36.0f);
                    ((EditText)view).setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                }
                ((EditText)view).setSelection(((EditText)view).getText().length());
            }
        }
        else {
            final ContactsActivityDelegate delegate = this.delegate;
            if (delegate != null) {
                delegate.didSelectContact(user, s, this);
                this.delegate = null;
            }
            if (this.needFinishFragment) {
                this.finishFragment();
            }
        }
    }
    
    private void hideFloatingButton(final boolean floatingHidden) {
        if (this.floatingHidden == floatingHidden) {
            return;
        }
        this.floatingHidden = floatingHidden;
        final AnimatorSet set = new AnimatorSet();
        final FrameLayout floatingButtonContainer = this.floatingButtonContainer;
        final Property translation_Y = View.TRANSLATION_Y;
        int dp;
        if (this.floatingHidden) {
            dp = AndroidUtilities.dp(100.0f);
        }
        else {
            dp = 0;
        }
        set.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)floatingButtonContainer, translation_Y, new float[] { (float)dp }) });
        set.setDuration(300L);
        set.setInterpolator((TimeInterpolator)this.floatingInterpolator);
        this.floatingButtonContainer.setClickable(floatingHidden ^ true);
        set.start();
    }
    
    private void updateVisibleRows(final int n) {
        final RecyclerListView listView = this.listView;
        if (listView != null) {
            for (int childCount = listView.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.listView.getChildAt(i);
                if (child instanceof UserCell) {
                    ((UserCell)child).update(n);
                }
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        if (this.destroyAfterSelect) {
            if (this.returnAsResult) {
                super.actionBar.setTitle(LocaleController.getString("SelectContact", 2131560680));
            }
            else if (this.createSecretChat) {
                super.actionBar.setTitle(LocaleController.getString("NewSecretChat", 2131559909));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("NewMessageTitle", 2131559901));
            }
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("Contacts", 2131559149));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int n) {
                if (n == -1) {
                    ContactsActivity.this.finishFragment();
                }
                else {
                    final int n2 = 1;
                    if (n == 1) {
                        SharedConfig.toggleSortContactsByName();
                        ContactsActivity.this.sortByName = SharedConfig.sortContactsByName;
                        final ContactsAdapter access$100 = ContactsActivity.this.listViewAdapter;
                        if (ContactsActivity.this.sortByName) {
                            n = n2;
                        }
                        else {
                            n = 2;
                        }
                        access$100.setSortType(n);
                        final ActionBarMenuItem access$101 = ContactsActivity.this.sortItem;
                        if (ContactsActivity.this.sortByName) {
                            n = 2131165363;
                        }
                        else {
                            n = 2131165362;
                        }
                        access$101.setIcon(n);
                    }
                }
            }
        });
        final ActionBarMenu menu = super.actionBar.createMenu();
        final ActionBarMenuItem setActionBarMenuItemSearchListener = menu.addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                ContactsActivity.this.searchListViewAdapter.searchDialogs(null);
                ContactsActivity.this.searching = false;
                ContactsActivity.this.searchWas = false;
                ContactsActivity.this.listView.setAdapter(ContactsActivity.this.listViewAdapter);
                ContactsActivity.this.listView.setSectionsType(1);
                ((RecyclerListView.SectionsAdapter)ContactsActivity.this.listViewAdapter).notifyDataSetChanged();
                ContactsActivity.this.listView.setFastScrollVisible(true);
                ContactsActivity.this.listView.setVerticalScrollBarEnabled(false);
                ContactsActivity.this.listView.setEmptyView(null);
                ContactsActivity.this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
                if (ContactsActivity.this.floatingButtonContainer != null) {
                    ContactsActivity.this.floatingButtonContainer.setVisibility(0);
                    ContactsActivity.this.floatingHidden = true;
                    ContactsActivity.this.floatingButtonContainer.setTranslationY((float)AndroidUtilities.dp(100.0f));
                    ContactsActivity.this.hideFloatingButton(false);
                }
                if (ContactsActivity.this.sortItem != null) {
                    ContactsActivity.this.sortItem.setVisibility(0);
                }
            }
            
            @Override
            public void onSearchExpand() {
                ContactsActivity.this.searching = true;
                if (ContactsActivity.this.floatingButtonContainer != null) {
                    ContactsActivity.this.floatingButtonContainer.setVisibility(8);
                }
                if (ContactsActivity.this.sortItem != null) {
                    ContactsActivity.this.sortItem.setVisibility(8);
                }
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                if (ContactsActivity.this.searchListViewAdapter == null) {
                    return;
                }
                final String string = editText.getText().toString();
                if (string.length() != 0) {
                    ContactsActivity.this.searchWas = true;
                    if (ContactsActivity.this.listView != null) {
                        ContactsActivity.this.listView.setAdapter(ContactsActivity.this.searchListViewAdapter);
                        ContactsActivity.this.listView.setSectionsType(0);
                        ContactsActivity.this.searchListViewAdapter.notifyDataSetChanged();
                        ContactsActivity.this.listView.setFastScrollVisible(false);
                        ContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                    }
                    if (ContactsActivity.this.emptyView != null) {
                        ContactsActivity.this.listView.setEmptyView((View)ContactsActivity.this.emptyView);
                        ContactsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                    }
                }
                ContactsActivity.this.searchListViewAdapter.searchDialogs(string);
            }
        });
        setActionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        setActionBarMenuItemSearchListener.setContentDescription((CharSequence)LocaleController.getString("Search", 2131560640));
        if (!this.createSecretChat && !this.returnAsResult) {
            int n;
            if (this.sortByName) {
                n = 2131165363;
            }
            else {
                n = 2131165362;
            }
            (this.sortItem = menu.addItem(1, n)).setContentDescription((CharSequence)LocaleController.getString("AccDescrContactSorting", 2131558429));
        }
        this.searchListViewAdapter = new SearchAdapter(context, this.ignoreUsers, this.allowUsernameSearch, false, false, this.allowBots, 0);
        final int chatId = this.chatId;
        final int n2 = 3;
        int n3 = 0;
        Label_0385: {
            int canUserDoAdminAction;
            if (chatId != 0) {
                canUserDoAdminAction = (ChatObject.canUserDoAdminAction(MessagesController.getInstance(super.currentAccount).getChat(this.chatId), 3) ? 1 : 0);
            }
            else {
                if (this.channelId == 0) {
                    n3 = 0;
                    break Label_0385;
                }
                final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(this.channelId);
                if (ChatObject.canUserDoAdminAction(chat, 3) && TextUtils.isEmpty((CharSequence)chat.username)) {
                    canUserDoAdminAction = 2;
                }
                else {
                    canUserDoAdminAction = 0;
                }
            }
            n3 = canUserDoAdminAction;
        }
        this.listViewAdapter = new ContactsAdapter(context, this.onlyUsers, this.needPhonebook, this.ignoreUsers, n3) {
            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                if (ContactsActivity.this.listView != null && ContactsActivity.this.listView.getAdapter() == this) {
                    final int itemCount = super.getItemCount();
                    final boolean access$1100 = ContactsActivity.this.needPhonebook;
                    final boolean b = true;
                    boolean fastScrollVisible = true;
                    int n = 8;
                    if (access$1100) {
                        final EmptyTextProgressView access$1101 = ContactsActivity.this.emptyView;
                        if (itemCount == 2) {
                            n = 0;
                        }
                        access$1101.setVisibility(n);
                        final RecyclerListView access$1102 = ContactsActivity.this.listView;
                        if (itemCount == 2) {
                            fastScrollVisible = false;
                        }
                        access$1102.setFastScrollVisible(fastScrollVisible);
                    }
                    else {
                        final EmptyTextProgressView access$1103 = ContactsActivity.this.emptyView;
                        if (itemCount == 0) {
                            n = 0;
                        }
                        access$1103.setVisibility(n);
                        ContactsActivity.this.listView.setFastScrollVisible(itemCount != 0 && b);
                    }
                }
            }
        };
        final ContactsAdapter listViewAdapter = this.listViewAdapter;
        int sortType;
        if (this.sortItem != null) {
            if (this.sortByName) {
                sortType = 1;
            }
            else {
                sortType = 2;
            }
        }
        else {
            sortType = 0;
        }
        listViewAdapter.setSortType(sortType);
        super.fragmentView = (View)new FrameLayout(context) {
            protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if (ContactsActivity.this.listView.getAdapter() == ContactsActivity.this.listViewAdapter) {
                    if (ContactsActivity.this.emptyView.getVisibility() == 0) {
                        ContactsActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(74.0f));
                    }
                }
                else {
                    ContactsActivity.this.emptyView.setTranslationY((float)AndroidUtilities.dp(0.0f));
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
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        this.listView.setAdapter(this.listViewAdapter);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ContactsActivity$gnTnCjocys4rzqdEYocufNT6sRA(this, n3));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean scrollingManually;
            
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1) {
                    if (ContactsActivity.this.searching && ContactsActivity.this.searchWas) {
                        AndroidUtilities.hideKeyboard(ContactsActivity.this.getParentActivity().getCurrentFocus());
                    }
                    this.scrollingManually = true;
                }
                else {
                    this.scrollingManually = false;
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, int top, int n) {
                super.onScrolled(recyclerView, top, n);
                if (ContactsActivity.this.floatingButtonContainer != null && ContactsActivity.this.floatingButtonContainer.getVisibility() != 8) {
                    final int firstVisibleItemPosition = ContactsActivity.this.layoutManager.findFirstVisibleItemPosition();
                    n = 0;
                    final View child = recyclerView.getChildAt(0);
                    if (child != null) {
                        top = child.getTop();
                    }
                    else {
                        top = 0;
                    }
                    boolean b2 = false;
                    Label_0152: {
                        boolean b;
                        if (ContactsActivity.this.prevPosition == firstVisibleItemPosition) {
                            final int access$1400 = ContactsActivity.this.prevTop;
                            b = (b2 = (top < ContactsActivity.this.prevTop));
                            if (Math.abs(access$1400 - top) <= 1) {
                                break Label_0152;
                            }
                        }
                        else {
                            b = (firstVisibleItemPosition > ContactsActivity.this.prevPosition);
                        }
                        n = 1;
                        b2 = b;
                    }
                    if (n != 0 && ContactsActivity.this.scrollUpdated && (b2 || (!b2 && this.scrollingManually))) {
                        ContactsActivity.this.hideFloatingButton(b2);
                    }
                    ContactsActivity.this.prevPosition = firstVisibleItemPosition;
                    ContactsActivity.this.prevTop = top;
                    ContactsActivity.this.scrollUpdated = true;
                }
            }
        });
        if (!this.createSecretChat && !this.returnAsResult) {
            this.floatingButtonContainer = new FrameLayout(context);
            final FrameLayout floatingButtonContainer = this.floatingButtonContainer;
            int n4;
            if (Build$VERSION.SDK_INT >= 21) {
                n4 = 56;
            }
            else {
                n4 = 60;
            }
            int n5;
            if (Build$VERSION.SDK_INT >= 21) {
                n5 = 56;
            }
            else {
                n5 = 60;
            }
            final float n6 = (float)(n5 + 14);
            int n7;
            if (LocaleController.isRTL) {
                n7 = n2;
            }
            else {
                n7 = 5;
            }
            float n8;
            if (LocaleController.isRTL) {
                n8 = 4.0f;
            }
            else {
                n8 = 0.0f;
            }
            float n9;
            if (LocaleController.isRTL) {
                n9 = 0.0f;
            }
            else {
                n9 = 4.0f;
            }
            frameLayout.addView((View)floatingButtonContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n4 + 20, n6, n7 | 0x50, n8, 0.0f, n9, 0.0f));
            this.floatingButtonContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$ContactsActivity$XQCfC9ukjnkgkb2J_v7QLg6mf6E(this));
            (this.floatingButton = new ImageView(context)).setScaleType(ImageView$ScaleType.CENTER);
            CombinedDrawable simpleSelectorCircleDrawable;
            final Drawable drawable = simpleSelectorCircleDrawable = (CombinedDrawable)Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
            if (Build$VERSION.SDK_INT < 21) {
                final Drawable mutate = context.getResources().getDrawable(2131165387).mutate();
                mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-16777216, PorterDuff$Mode.MULTIPLY));
                simpleSelectorCircleDrawable = new CombinedDrawable(mutate, drawable, 0, 0);
                simpleSelectorCircleDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            }
            this.floatingButton.setBackgroundDrawable((Drawable)simpleSelectorCircleDrawable);
            this.floatingButton.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), PorterDuff$Mode.MULTIPLY));
            this.floatingButton.setImageResource(2131165280);
            this.floatingButtonContainer.setContentDescription((CharSequence)LocaleController.getString("CreateNewContact", 2131559170));
            if (Build$VERSION.SDK_INT >= 21) {
                final StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
                stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, View.TRANSLATION_Z, new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
                this.floatingButton.setStateListAnimator(stateListAnimator);
                this.floatingButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                    @SuppressLint({ "NewApi" })
                    public void getOutline(final View view, final Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    }
                });
            }
            final FrameLayout floatingButtonContainer2 = this.floatingButtonContainer;
            final ImageView floatingButton = this.floatingButton;
            int n10;
            if (Build$VERSION.SDK_INT >= 21) {
                n10 = 56;
            }
            else {
                n10 = 60;
            }
            int n11;
            if (Build$VERSION.SDK_INT >= 21) {
                n11 = 56;
            }
            else {
                n11 = 60;
            }
            floatingButtonContainer2.addView((View)floatingButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n10, (float)n11, 51, 10.0f, 0.0f, 10.0f, 0.0f));
        }
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int intValue, final int n, final Object... array) {
        if (intValue == NotificationCenter.contactsDidLoad) {
            final ContactsAdapter listViewAdapter = this.listViewAdapter;
            if (listViewAdapter != null) {
                ((RecyclerListView.SectionsAdapter)listViewAdapter).notifyDataSetChanged();
            }
        }
        else if (intValue == NotificationCenter.updateInterfaces) {
            intValue = (int)array[0];
            if ((intValue & 0x2) != 0x0 || (intValue & 0x1) != 0x0 || (intValue & 0x4) != 0x0) {
                this.updateVisibleRows(intValue);
            }
            if ((intValue & 0x4) != 0x0 && !this.sortByName) {
                final ContactsAdapter listViewAdapter2 = this.listViewAdapter;
                if (listViewAdapter2 != null) {
                    listViewAdapter2.sortOnlineContacts();
                }
            }
        }
        else if (intValue == NotificationCenter.encryptedChatCreated) {
            if (this.createSecretChat && this.creatingChat) {
                final TLRPC.EncryptedChat encryptedChat = (TLRPC.EncryptedChat)array[0];
                final Bundle bundle = new Bundle();
                bundle.putInt("enc_id", encryptedChat.id);
                NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                this.presentFragment(new ChatActivity(bundle), true);
            }
        }
        else if (intValue == NotificationCenter.closeChats && !this.creatingChat) {
            this.removeSelfFromStack();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY $$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY = new _$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "actionBarDefaultSearch"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "actionBarDefaultSearchPlaceholder"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SECTIONS, new Class[] { LetterSectionCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ContactsActivity$mX2AyzEZkfKNXN64OUntWgWNyBY, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionIcon"), new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionBackground"), new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "chats_actionPressedBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { GraySectionCell.class }, new String[] { "textView" }, null, null, null, "key_graySectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GraySectionCell.class }, null, null, null, "graySection"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_groupDrawable, Theme.dialogs_broadcastDrawable, Theme.dialogs_botDrawable }, null, "chats_nameIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_verifiedCheckDrawable }, null, "chats_verifiedCheck"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_verifiedDrawable }, null, "chats_verifiedBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, (Paint)Theme.dialogs_offlinePaint, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, (Paint)Theme.dialogs_onlinePaint, null, null, "windowBackgroundWhiteBlueText3"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_namePaint, (Paint)Theme.dialogs_searchNamePaint }, null, null, "chats_name"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_nameEncryptedPaint, (Paint)Theme.dialogs_searchNameEncryptedPaint }, null, null, "chats_secretName") };
    }
    
    @Override
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final FrameLayout floatingButtonContainer = this.floatingButtonContainer;
        if (floatingButtonContainer != null) {
            floatingButtonContainer.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    final FrameLayout access$400 = ContactsActivity.this.floatingButtonContainer;
                    int dp;
                    if (ContactsActivity.this.floatingHidden) {
                        dp = AndroidUtilities.dp(100.0f);
                    }
                    else {
                        dp = 0;
                    }
                    access$400.setTranslationY((float)dp);
                    ContactsActivity.this.floatingButtonContainer.setClickable(ContactsActivity.this.floatingHidden ^ true);
                    if (ContactsActivity.this.floatingButtonContainer != null) {
                        ContactsActivity.this.floatingButtonContainer.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)this);
                    }
                }
            });
        }
    }
    
    @Override
    protected void onDialogDismiss(final Dialog dialog) {
        super.onDialogDismiss(dialog);
        final AlertDialog permissionDialog = this.permissionDialog;
        if (permissionDialog != null && dialog == permissionDialog && this.getParentActivity() != null && this.askAboutContacts) {
            this.askForPermissons(false);
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.encryptedChatCreated);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.closeChats);
        this.checkPermission = UserConfig.getInstance(super.currentAccount).syncContacts;
        if (super.arguments != null) {
            this.onlyUsers = this.getArguments().getBoolean("onlyUsers", false);
            this.destroyAfterSelect = super.arguments.getBoolean("destroyAfterSelect", false);
            this.returnAsResult = super.arguments.getBoolean("returnAsResult", false);
            this.createSecretChat = super.arguments.getBoolean("createSecretChat", false);
            this.selectAlertString = super.arguments.getString("selectAlertString");
            this.allowUsernameSearch = super.arguments.getBoolean("allowUsernameSearch", true);
            this.needForwardCount = super.arguments.getBoolean("needForwardCount", true);
            this.allowBots = super.arguments.getBoolean("allowBots", true);
            this.channelId = super.arguments.getInt("channelId", 0);
            this.needFinishFragment = super.arguments.getBoolean("needFinishFragment", true);
            this.chatId = super.arguments.getInt("chat_id", 0);
        }
        else {
            this.needPhonebook = true;
        }
        if (!this.createSecretChat && !this.returnAsResult) {
            this.sortByName = SharedConfig.sortContactsByName;
        }
        ContactsController.getInstance(super.currentAccount).checkInviteText();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.encryptedChatCreated);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        this.delegate = null;
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
    public void onRequestPermissionsResultFragment(int i, final String[] array, final int[] array2) {
        if (i == 1) {
            SharedPreferences$Editor edit;
            for (i = 0; i < array.length; ++i) {
                if (array2.length > i) {
                    if ("android.permission.READ_CONTACTS".equals(array[i])) {
                        if (array2[i] == 0) {
                            ContactsController.getInstance(super.currentAccount).forceImportContacts();
                        }
                        else {
                            edit = MessagesController.getGlobalNotificationsSettings().edit();
                            this.askAboutContacts = false;
                            edit.putBoolean("askAboutContacts", false).commit();
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ContactsAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerListView.SectionsAdapter)listViewAdapter).notifyDataSetChanged();
        }
        if (this.checkPermission && Build$VERSION.SDK_INT >= 23) {
            final Activity parentActivity = this.getParentActivity();
            if (parentActivity != null) {
                this.checkPermission = false;
                if (parentActivity.checkSelfPermission("android.permission.READ_CONTACTS") != 0) {
                    if (parentActivity.shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
                        this.showDialog(this.permissionDialog = AlertsCreator.createContactsPermissionDialog(parentActivity, new _$$Lambda$ContactsActivity$xFVgeouE3JWG_nldltsKWVnF4Hg(this)).create());
                    }
                    else {
                        this.askForPermissons(true);
                    }
                }
            }
        }
    }
    
    public void setDelegate(final ContactsActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setIgnoreUsers(final SparseArray<TLRPC.User> ignoreUsers) {
        this.ignoreUsers = ignoreUsers;
    }
    
    public interface ContactsActivityDelegate
    {
        void didSelectContact(final TLRPC.User p0, final String p1, final ContactsActivity p2);
    }
}
