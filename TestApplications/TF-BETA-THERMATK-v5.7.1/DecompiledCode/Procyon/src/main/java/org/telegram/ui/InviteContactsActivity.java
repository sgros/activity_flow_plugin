// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Point;
import android.animation.ObjectAnimator;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Animator;
import org.telegram.messenger.Utilities;
import java.util.TimerTask;
import java.util.Timer;
import androidx.annotation.Keep;
import org.telegram.messenger.UserConfig;
import android.net.Uri;
import org.telegram.messenger.FileLog;
import android.content.Intent;
import org.telegram.ui.Cells.InviteTextCell;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.graphics.Rect;
import android.view.View$MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import android.graphics.Canvas;
import android.view.ViewGroup;
import org.telegram.ui.ActionBar.ActionBar;
import android.content.Context;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import org.telegram.messenger.LocaleController;
import android.view.View;
import org.telegram.ui.Cells.InviteUserCell;
import org.telegram.ui.ActionBar.ActionBarLayout;
import java.util.HashMap;
import android.widget.ScrollView;
import org.telegram.messenger.ContactsController;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.ui.Components.GroupCreateSpan;
import java.util.ArrayList;
import android.view.View$OnClickListener;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class InviteContactsActivity extends BaseFragment implements NotificationCenterDelegate, View$OnClickListener
{
    private InviteAdapter adapter;
    private ArrayList<GroupCreateSpan> allSpans;
    private int containerHeight;
    private TextView counterTextView;
    private FrameLayout counterView;
    private GroupCreateSpan currentDeletingSpan;
    private GroupCreateDividerItemDecoration decoration;
    private EditTextBoldCursor editText;
    private EmptyTextProgressView emptyView;
    private int fieldY;
    private boolean ignoreScrollEvent;
    private TextView infoTextView;
    private RecyclerListView listView;
    private ArrayList<ContactsController.Contact> phoneBookContacts;
    private ScrollView scrollView;
    private boolean searchWas;
    private boolean searching;
    private HashMap<String, GroupCreateSpan> selectedContacts;
    private SpansContainer spansContainer;
    private TextView textView;
    
    public InviteContactsActivity() {
        this.selectedContacts = new HashMap<String, GroupCreateSpan>();
        this.allSpans = new ArrayList<GroupCreateSpan>();
    }
    
    private void checkVisibleRows() {
        for (int childCount = this.listView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.listView.getChildAt(i);
            if (child instanceof InviteUserCell) {
                final InviteUserCell inviteUserCell = (InviteUserCell)child;
                final ContactsController.Contact contact = inviteUserCell.getContact();
                if (contact != null) {
                    inviteUserCell.setChecked(this.selectedContacts.containsKey(contact.key), true);
                }
            }
        }
    }
    
    private void closeSearch() {
        this.searching = false;
        this.searchWas = false;
        this.adapter.setSearching(false);
        this.adapter.searchDialogs(null);
        this.listView.setFastScrollVisible(true);
        this.listView.setVerticalScrollBarEnabled(false);
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
    }
    
    private void fetchContacts() {
        Collections.sort(this.phoneBookContacts = new ArrayList<ContactsController.Contact>((Collection<? extends ContactsController.Contact>)ContactsController.getInstance(super.currentAccount).phoneBookContacts), (Comparator<? super ContactsController.Contact>)_$$Lambda$InviteContactsActivity$r58ALapXATHsxuXB3Kf3_z6GjIA.INSTANCE);
        final EmptyTextProgressView emptyView = this.emptyView;
        if (emptyView != null) {
            emptyView.showTextView();
        }
        final InviteAdapter adapter = this.adapter;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    
    private void updateHint() {
        if (this.selectedContacts.isEmpty()) {
            this.infoTextView.setVisibility(0);
            this.counterView.setVisibility(4);
        }
        else {
            this.infoTextView.setVisibility(4);
            this.counterView.setVisibility(0);
            this.counterTextView.setText((CharSequence)String.format("%d", this.selectedContacts.size()));
        }
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        this.allSpans.clear();
        this.selectedContacts.clear();
        this.currentDeletingSpan = null;
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("InviteFriends", 2131559677));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    InviteContactsActivity.this.finishFragment();
                }
            }
        });
        super.fragmentView = (View)new ViewGroup(context) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view == InviteContactsActivity.this.listView || view == InviteContactsActivity.this.emptyView) {
                    InviteContactsActivity.this.parentLayout.drawHeaderShadow(canvas, InviteContactsActivity.this.scrollView.getMeasuredHeight());
                }
                return drawChild;
            }
            
            protected void onLayout(final boolean b, int n, int n2, final int n3, final int n4) {
                InviteContactsActivity.this.scrollView.layout(0, 0, InviteContactsActivity.this.scrollView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight());
                InviteContactsActivity.this.listView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight(), InviteContactsActivity.this.listView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.listView.getMeasuredHeight());
                InviteContactsActivity.this.emptyView.layout(0, InviteContactsActivity.this.scrollView.getMeasuredHeight() + AndroidUtilities.dp(72.0f), InviteContactsActivity.this.emptyView.getMeasuredWidth(), InviteContactsActivity.this.scrollView.getMeasuredHeight() + InviteContactsActivity.this.emptyView.getMeasuredHeight());
                n = n4 - n2;
                n2 = n - InviteContactsActivity.this.infoTextView.getMeasuredHeight();
                InviteContactsActivity.this.infoTextView.layout(0, n2, InviteContactsActivity.this.infoTextView.getMeasuredWidth(), InviteContactsActivity.this.infoTextView.getMeasuredHeight() + n2);
                n -= InviteContactsActivity.this.counterView.getMeasuredHeight();
                InviteContactsActivity.this.counterView.layout(0, n, InviteContactsActivity.this.counterView.getMeasuredWidth(), InviteContactsActivity.this.counterView.getMeasuredHeight() + n);
            }
            
            protected void onMeasure(int n, int n2) {
                final int size = View$MeasureSpec.getSize(n);
                final int size2 = View$MeasureSpec.getSize(n2);
                this.setMeasuredDimension(size, size2);
                if (!AndroidUtilities.isTablet() && size2 <= size) {
                    n = AndroidUtilities.dp(56.0f);
                }
                else {
                    n = AndroidUtilities.dp(144.0f);
                }
                InviteContactsActivity.this.infoTextView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE));
                InviteContactsActivity.this.counterView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
                if (InviteContactsActivity.this.infoTextView.getVisibility() == 0) {
                    n2 = InviteContactsActivity.this.infoTextView.getMeasuredHeight();
                }
                else {
                    n2 = InviteContactsActivity.this.counterView.getMeasuredHeight();
                }
                InviteContactsActivity.this.scrollView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE));
                InviteContactsActivity.this.listView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2 - InviteContactsActivity.this.scrollView.getMeasuredHeight() - n2, 1073741824));
                InviteContactsActivity.this.emptyView.measure(View$MeasureSpec.makeMeasureSpec(size, 1073741824), View$MeasureSpec.makeMeasureSpec(size2 - InviteContactsActivity.this.scrollView.getMeasuredHeight() - AndroidUtilities.dp(72.0f), 1073741824));
            }
        };
        final ViewGroup viewGroup = (ViewGroup)super.fragmentView;
        (this.scrollView = new ScrollView(context) {
            public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                if (InviteContactsActivity.this.ignoreScrollEvent) {
                    InviteContactsActivity.this.ignoreScrollEvent = false;
                    return false;
                }
                rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                rect.top += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(20.0f);
                rect.bottom += InviteContactsActivity.this.fieldY + AndroidUtilities.dp(50.0f);
                return super.requestChildRectangleOnScreen(view, rect, b);
            }
        }).setVerticalScrollBarEnabled(false);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("windowBackgroundWhite"));
        viewGroup.addView((View)this.scrollView);
        this.spansContainer = new SpansContainer(context);
        this.scrollView.addView((View)this.spansContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        (this.editText = new EditTextBoldCursor(context) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (InviteContactsActivity.this.currentDeletingSpan != null) {
                    InviteContactsActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                    InviteContactsActivity.this.currentDeletingSpan = null;
                }
                if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard((View)this)) {
                    this.clearFocus();
                    this.requestFocus();
                }
                return super.onTouchEvent(motionEvent);
            }
        }).setTextSize(1, 18.0f);
        this.editText.setHintColor(Theme.getColor("groupcreate_hintText"));
        this.editText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.editText.setCursorColor(Theme.getColor("groupcreate_cursor"));
        this.editText.setCursorWidth(1.5f);
        this.editText.setInputType(655536);
        this.editText.setSingleLine(true);
        this.editText.setBackgroundDrawable((Drawable)null);
        this.editText.setVerticalScrollBarEnabled(false);
        this.editText.setHorizontalScrollBarEnabled(false);
        this.editText.setTextIsSelectable(false);
        this.editText.setPadding(0, 0, 0, 0);
        this.editText.setImeOptions(268435462);
        final EditTextBoldCursor editText = this.editText;
        int n;
        if (LocaleController.isRTL) {
            n = 5;
        }
        else {
            n = 3;
        }
        editText.setGravity(n | 0x10);
        this.spansContainer.addView((View)this.editText);
        this.editText.setHintText(LocaleController.getString("SearchFriends", 2131560646));
        this.editText.setCustomSelectionActionModeCallback((ActionMode$Callback)new ActionMode$Callback() {
            public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
                return false;
            }
            
            public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }
            
            public void onDestroyActionMode(final ActionMode actionMode) {
            }
            
            public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
                return false;
            }
        });
        this.editText.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            private boolean wasEmpty;
            
            public boolean onKey(final View view, int action, final KeyEvent keyEvent) {
                action = keyEvent.getAction();
                boolean wasEmpty = true;
                if (action == 0) {
                    if (InviteContactsActivity.this.editText.length() != 0) {
                        wasEmpty = false;
                    }
                    this.wasEmpty = wasEmpty;
                }
                else if (keyEvent.getAction() == 1 && this.wasEmpty && !InviteContactsActivity.this.allSpans.isEmpty()) {
                    InviteContactsActivity.this.spansContainer.removeSpan(InviteContactsActivity.this.allSpans.get(InviteContactsActivity.this.allSpans.size() - 1));
                    InviteContactsActivity.this.updateHint();
                    InviteContactsActivity.this.checkVisibleRows();
                    return true;
                }
                return false;
            }
        });
        this.editText.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (InviteContactsActivity.this.editText.length() != 0) {
                    InviteContactsActivity.this.searching = true;
                    InviteContactsActivity.this.searchWas = true;
                    InviteContactsActivity.this.adapter.setSearching(true);
                    InviteContactsActivity.this.adapter.searchDialogs(InviteContactsActivity.this.editText.getText().toString());
                    InviteContactsActivity.this.listView.setFastScrollVisible(false);
                    InviteContactsActivity.this.listView.setVerticalScrollBarEnabled(true);
                    InviteContactsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                }
                else {
                    InviteContactsActivity.this.closeSearch();
                }
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
        });
        this.emptyView = new EmptyTextProgressView(context);
        if (ContactsController.getInstance(super.currentAccount).isLoadingContacts()) {
            this.emptyView.showProgress();
        }
        else {
            this.emptyView.showTextView();
        }
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
        viewGroup.addView((View)this.emptyView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, 1, false);
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setAdapter(this.adapter = new InviteAdapter(context));
        this.listView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.listView.setVerticalScrollBarEnabled(true);
        final RecyclerListView listView = this.listView;
        int verticalScrollbarPosition;
        if (LocaleController.isRTL) {
            verticalScrollbarPosition = 1;
        }
        else {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        this.listView.addItemDecoration((RecyclerView.ItemDecoration)(this.decoration = new GroupCreateDividerItemDecoration()));
        viewGroup.addView((View)this.listView);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$InviteContactsActivity$2CAh12ObsNHUdlUsHSs_VZmRL0I(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1) {
                    AndroidUtilities.hideKeyboard((View)InviteContactsActivity.this.editText);
                }
            }
        });
        (this.infoTextView = new TextView(context)).setBackgroundColor(Theme.getColor("contacts_inviteBackground"));
        this.infoTextView.setTextColor(Theme.getColor("contacts_inviteText"));
        this.infoTextView.setGravity(17);
        this.infoTextView.setText((CharSequence)LocaleController.getString("InviteFriendsHelp", 2131559678));
        this.infoTextView.setTextSize(1, 13.0f);
        this.infoTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.infoTextView.setPadding(AndroidUtilities.dp(17.0f), AndroidUtilities.dp(9.0f), AndroidUtilities.dp(17.0f), AndroidUtilities.dp(9.0f));
        viewGroup.addView((View)this.infoTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2, 83));
        (this.counterView = new FrameLayout(context)).setBackgroundColor(Theme.getColor("contacts_inviteBackground"));
        this.counterView.setVisibility(4);
        viewGroup.addView((View)this.counterView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 83));
        this.counterView.setOnClickListener((View$OnClickListener)new _$$Lambda$InviteContactsActivity$nq_MZzgy9JlkAxS_tdx9DVT5pQg(this));
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        this.counterView.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -1, 17));
        (this.counterTextView = new TextView(context)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.counterTextView.setTextSize(1, 14.0f);
        this.counterTextView.setTextColor(Theme.getColor("contacts_inviteBackground"));
        this.counterTextView.setGravity(17);
        this.counterTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(10.0f), Theme.getColor("contacts_inviteText")));
        this.counterTextView.setMinWidth(AndroidUtilities.dp(20.0f));
        this.counterTextView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(1.0f));
        linearLayout.addView((View)this.counterTextView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, 20, 16, 0, 0, 10, 0));
        (this.textView = new TextView(context)).setTextSize(1, 14.0f);
        this.textView.setTextColor(Theme.getColor("contacts_inviteText"));
        this.textView.setGravity(17);
        this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0f));
        this.textView.setText((CharSequence)LocaleController.getString("InviteToTelegram", 2131559690).toUpperCase());
        this.textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        linearLayout.addView((View)this.textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 16));
        this.updateHint();
        this.adapter.notifyDataSetChanged();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.contactsImported) {
            this.fetchContacts();
        }
    }
    
    public int getContainerHeight() {
        return this.containerHeight;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI $$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI = new _$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "groupcreate_hintText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "groupcreate_cursor"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GroupCreateSectionCell.class }, null, null, null, "graySection"), new ThemeDescription((View)this.listView, 0, new Class[] { GroupCreateSectionCell.class }, new String[] { "drawable" }, null, null, null, "groupcreate_sectionShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { GroupCreateSectionCell.class }, new String[] { "textView" }, null, null, null, "groupcreate_sectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { InviteUserCell.class }, new String[] { "textView" }, null, null, null, "groupcreate_sectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { InviteUserCell.class }, new String[] { "checkBox" }, null, null, null, "checkbox"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { InviteUserCell.class }, new String[] { "checkBox" }, null, null, null, "checkboxCheck"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { InviteUserCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { InviteUserCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { InviteUserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$InviteContactsActivity$lRM1FX_g_ooIebl8RwlJ1GDWMOI, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, 0, new Class[] { InviteTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { InviteTextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "avatar_backgroundGroupCreateSpanBlue"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "groupcreate_spanBackground"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "groupcreate_spanText"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "groupcreate_spanDelete"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "avatar_backgroundBlue"), new ThemeDescription((View)this.infoTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "contacts_inviteText"), new ThemeDescription((View)this.infoTextView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "contacts_inviteBackground"), new ThemeDescription((View)this.counterView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "contacts_inviteBackground"), new ThemeDescription((View)this.counterTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "contacts_inviteBackground"), new ThemeDescription((View)this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "contacts_inviteText"), new ThemeDescription((View)this.counterTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "contacts_inviteText") };
    }
    
    public void onClick(final View view) {
        final GroupCreateSpan currentDeletingSpan = (GroupCreateSpan)view;
        if (currentDeletingSpan.isDeleting()) {
            this.currentDeletingSpan = null;
            this.spansContainer.removeSpan(currentDeletingSpan);
            this.updateHint();
            this.checkVisibleRows();
        }
        else {
            final GroupCreateSpan currentDeletingSpan2 = this.currentDeletingSpan;
            if (currentDeletingSpan2 != null) {
                currentDeletingSpan2.cancelDeleteAnimation();
            }
            (this.currentDeletingSpan = currentDeletingSpan).startDeleteAnimation();
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsImported);
        this.fetchContacts();
        if (!UserConfig.getInstance(super.currentAccount).contactsReimported) {
            ContactsController.getInstance(super.currentAccount).forceImportContacts();
            UserConfig.getInstance(super.currentAccount).contactsReimported = true;
            UserConfig.getInstance(super.currentAccount).saveConfig(false);
        }
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsImported);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final EditTextBoldCursor editText = this.editText;
        if (editText != null) {
            editText.requestFocus();
        }
    }
    
    @Keep
    public void setContainerHeight(final int containerHeight) {
        this.containerHeight = containerHeight;
        final SpansContainer spansContainer = this.spansContainer;
        if (spansContainer != null) {
            spansContainer.requestLayout();
        }
    }
    
    public class InviteAdapter extends SelectionAdapter
    {
        private Context context;
        private ArrayList<ContactsController.Contact> searchResult;
        private ArrayList<CharSequence> searchResultNames;
        private Timer searchTimer;
        private boolean searching;
        
        public InviteAdapter(final Context context) {
            this.searchResult = new ArrayList<ContactsController.Contact>();
            this.searchResultNames = new ArrayList<CharSequence>();
            this.context = context;
        }
        
        private void updateSearchResults(final ArrayList<ContactsController.Contact> list, final ArrayList<CharSequence> list2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$InviteContactsActivity$InviteAdapter$ZxgmfjRluVB9wR7WmXECALc6XVE(this, list, list2));
        }
        
        @Override
        public int getItemCount() {
            if (this.searching) {
                return this.searchResult.size();
            }
            return InviteContactsActivity.this.phoneBookContacts.size() + 1;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (!this.searching && n == 0) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return true;
        }
        
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            final int itemCount = this.getItemCount();
            final EmptyTextProgressView access$1400 = InviteContactsActivity.this.emptyView;
            boolean single = false;
            int visibility;
            if (itemCount == 1) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            access$1400.setVisibility(visibility);
            final GroupCreateDividerItemDecoration access$1401 = InviteContactsActivity.this.decoration;
            if (itemCount == 1) {
                single = true;
            }
            access$1401.setSingle(single);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            if (viewHolder.getItemViewType() == 0) {
                final InviteUserCell inviteUserCell = (InviteUserCell)viewHolder.itemView;
                ContactsController.Contact contact;
                CharSequence charSequence;
                if (this.searching) {
                    contact = this.searchResult.get(n);
                    charSequence = this.searchResultNames.get(n);
                }
                else {
                    contact = InviteContactsActivity.this.phoneBookContacts.get(n - 1);
                    charSequence = null;
                }
                inviteUserCell.setUser(contact, charSequence);
                inviteUserCell.setChecked(InviteContactsActivity.this.selectedContacts.containsKey(contact.key), false);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 1) {
                frameLayout = new InviteUserCell(this.context, true);
            }
            else {
                frameLayout = new InviteTextCell(this.context);
                ((InviteTextCell)frameLayout).setTextAndIcon(LocaleController.getString("ShareTelegram", 2131560753), 2131165818);
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
        
        @Override
        public void onViewRecycled(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof InviteUserCell) {
                ((InviteUserCell)itemView).recycle();
            }
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
                this.notifyDataSetChanged();
            }
            else {
                (this.searchTimer = new Timer()).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            InviteAdapter.this.searchTimer.cancel();
                            InviteAdapter.this.searchTimer = null;
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        AndroidUtilities.runOnUIThread(new _$$Lambda$InviteContactsActivity$InviteAdapter$1$puDDzs3DCPG3FhnDL1PVs4vd3QI(this, s));
                    }
                }, 200L, 300L);
            }
        }
        
        public void setSearching(final boolean searching) {
            if (this.searching == searching) {
                return;
            }
            this.searching = searching;
            this.notifyDataSetChanged();
        }
    }
    
    private class SpansContainer extends ViewGroup
    {
        private View addingSpan;
        private boolean animationStarted;
        private ArrayList<Animator> animators;
        private AnimatorSet currentAnimation;
        private View removingSpan;
        
        public SpansContainer(final Context context) {
            super(context);
            this.animators = new ArrayList<Animator>();
        }
        
        public void addSpan(final GroupCreateSpan addingSpan) {
            InviteContactsActivity.this.allSpans.add(addingSpan);
            InviteContactsActivity.this.selectedContacts.put(addingSpan.getKey(), addingSpan);
            InviteContactsActivity.this.editText.setHintVisible(false);
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            (this.currentAnimation = new AnimatorSet()).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    SpansContainer.this.addingSpan = null;
                    SpansContainer.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    InviteContactsActivity.this.editText.setAllowDrawCursor(true);
                }
            });
            this.currentAnimation.setDuration(150L);
            this.addingSpan = addingSpan;
            this.animators.clear();
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.addingSpan, "scaleX", new float[] { 0.01f, 1.0f }));
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.addingSpan, "scaleY", new float[] { 0.01f, 1.0f }));
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.addingSpan, "alpha", new float[] { 0.0f, 1.0f }));
            this.addView((View)addingSpan);
        }
        
        protected void onLayout(final boolean b, int i, int childCount, final int n, final int n2) {
            View child;
            for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                child = this.getChildAt(i);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
        
        protected void onMeasure(int dp, int n) {
            final int childCount = this.getChildCount();
            final int size = View$MeasureSpec.getSize(dp);
            final int n2 = size - AndroidUtilities.dp(32.0f);
            int dp2 = AndroidUtilities.dp(12.0f);
            dp = AndroidUtilities.dp(12.0f);
            int i = 0;
            n = 0;
            int n3 = 0;
            while (i < childCount) {
                final View child = this.getChildAt(i);
                int n4;
                if (!(child instanceof GroupCreateSpan)) {
                    n4 = dp;
                }
                else {
                    child.measure(View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                    if (child != this.removingSpan && child.getMeasuredWidth() + n > n2) {
                        dp2 += child.getMeasuredHeight() + AndroidUtilities.dp(12.0f);
                        n = 0;
                    }
                    if (child.getMeasuredWidth() + n3 > n2) {
                        n4 = dp + (child.getMeasuredHeight() + AndroidUtilities.dp(12.0f));
                        n3 = 0;
                    }
                    else {
                        n4 = dp;
                    }
                    dp = AndroidUtilities.dp(16.0f) + n;
                    if (!this.animationStarted) {
                        final View removingSpan = this.removingSpan;
                        if (child == removingSpan) {
                            child.setTranslationX((float)(AndroidUtilities.dp(16.0f) + n3));
                            child.setTranslationY((float)n4);
                        }
                        else if (removingSpan != null) {
                            final float translationX = child.getTranslationX();
                            final float n5 = (float)dp;
                            if (translationX != n5) {
                                this.animators.add((Animator)ObjectAnimator.ofFloat((Object)child, "translationX", new float[] { n5 }));
                            }
                            final float translationY = child.getTranslationY();
                            final float n6 = (float)dp2;
                            if (translationY != n6) {
                                this.animators.add((Animator)ObjectAnimator.ofFloat((Object)child, "translationY", new float[] { n6 }));
                            }
                        }
                        else {
                            child.setTranslationX((float)dp);
                            child.setTranslationY((float)dp2);
                        }
                    }
                    dp = n;
                    if (child != this.removingSpan) {
                        dp = n + (child.getMeasuredWidth() + AndroidUtilities.dp(9.0f));
                    }
                    n3 += child.getMeasuredWidth() + AndroidUtilities.dp(9.0f);
                    n = dp;
                }
                ++i;
                dp = n4;
            }
            int n7;
            if (AndroidUtilities.isTablet()) {
                n7 = AndroidUtilities.dp(366.0f) / 3;
            }
            else {
                final Point displaySize = AndroidUtilities.displaySize;
                n7 = (Math.min(displaySize.x, displaySize.y) - AndroidUtilities.dp(164.0f)) / 3;
            }
            int n8 = n;
            int n9 = dp2;
            if (n2 - n < n7) {
                n9 = dp2 + AndroidUtilities.dp(44.0f);
                n8 = 0;
            }
            n = dp;
            if (n2 - n3 < n7) {
                n = dp + AndroidUtilities.dp(44.0f);
            }
            InviteContactsActivity.this.editText.measure(View$MeasureSpec.makeMeasureSpec(n2 - n8, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
            if (!this.animationStarted) {
                final int dp3 = AndroidUtilities.dp(44.0f);
                dp = n8 + AndroidUtilities.dp(16.0f);
                InviteContactsActivity.this.fieldY = n9;
                if (this.currentAnimation != null) {
                    n = n9 + AndroidUtilities.dp(44.0f);
                    if (InviteContactsActivity.this.containerHeight != n) {
                        this.animators.add((Animator)ObjectAnimator.ofInt((Object)InviteContactsActivity.this, "containerHeight", new int[] { n }));
                    }
                    final float translationX2 = InviteContactsActivity.this.editText.getTranslationX();
                    final float n10 = (float)dp;
                    if (translationX2 != n10) {
                        this.animators.add((Animator)ObjectAnimator.ofFloat((Object)InviteContactsActivity.this.editText, "translationX", new float[] { n10 }));
                    }
                    if (InviteContactsActivity.this.editText.getTranslationY() != InviteContactsActivity.this.fieldY) {
                        this.animators.add((Animator)ObjectAnimator.ofFloat((Object)InviteContactsActivity.this.editText, "translationY", new float[] { (float)InviteContactsActivity.this.fieldY }));
                    }
                    InviteContactsActivity.this.editText.setAllowDrawCursor(false);
                    this.currentAnimation.playTogether((Collection)this.animators);
                    this.currentAnimation.start();
                    this.animationStarted = true;
                }
                else {
                    InviteContactsActivity.this.containerHeight = n + dp3;
                    InviteContactsActivity.this.editText.setTranslationX((float)dp);
                    InviteContactsActivity.this.editText.setTranslationY((float)InviteContactsActivity.this.fieldY);
                }
            }
            else if (this.currentAnimation != null && !InviteContactsActivity.this.ignoreScrollEvent && this.removingSpan == null) {
                InviteContactsActivity.this.editText.bringPointIntoView(InviteContactsActivity.this.editText.getSelectionStart());
            }
            this.setMeasuredDimension(size, InviteContactsActivity.this.containerHeight);
        }
        
        public void removeSpan(final GroupCreateSpan groupCreateSpan) {
            InviteContactsActivity.this.ignoreScrollEvent = true;
            InviteContactsActivity.this.selectedContacts.remove(groupCreateSpan.getKey());
            InviteContactsActivity.this.allSpans.remove(groupCreateSpan);
            groupCreateSpan.setOnClickListener((View$OnClickListener)null);
            final AnimatorSet currentAnimation = this.currentAnimation;
            if (currentAnimation != null) {
                currentAnimation.setupEndValues();
                this.currentAnimation.cancel();
            }
            this.animationStarted = false;
            (this.currentAnimation = new AnimatorSet()).addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    SpansContainer.this.removeView((View)groupCreateSpan);
                    SpansContainer.this.removingSpan = null;
                    SpansContainer.this.currentAnimation = null;
                    SpansContainer.this.animationStarted = false;
                    InviteContactsActivity.this.editText.setAllowDrawCursor(true);
                    if (InviteContactsActivity.this.allSpans.isEmpty()) {
                        InviteContactsActivity.this.editText.setHintVisible(true);
                    }
                }
            });
            this.currentAnimation.setDuration(150L);
            this.removingSpan = groupCreateSpan;
            this.animators.clear();
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.removingSpan, "scaleX", new float[] { 1.0f, 0.01f }));
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.removingSpan, "scaleY", new float[] { 1.0f, 0.01f }));
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.removingSpan, "alpha", new float[] { 1.0f, 0.0f }));
            this.requestLayout();
        }
    }
}
