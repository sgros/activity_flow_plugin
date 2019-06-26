// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.graphics.Point;
import java.util.Collection;
import org.telegram.messenger.Utilities;
import java.util.TimerTask;
import android.widget.FrameLayout;
import android.text.style.ForegroundColorSpan;
import org.telegram.ui.Cells.TextCell;
import java.util.HashMap;
import org.telegram.ui.Adapters.SearchAdapterHelper$SearchAdapterHelperDelegate$_CC;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import org.telegram.ui.Adapters.SearchAdapterHelper;
import androidx.annotation.Keep;
import android.content.DialogInterface;
import org.telegram.messenger.FileLog;
import android.widget.Toast;
import android.widget.TextView;
import org.telegram.ui.Cells.GroupCreateSectionCell;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.StateListAnimator;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.widget.ImageView$ScaleType;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import android.widget.TextView$OnEditorActionListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.graphics.Rect;
import android.os.Build$VERSION;
import android.view.View$MeasureSpec;
import android.graphics.Canvas;
import android.view.ViewGroup;
import org.telegram.ui.ActionBar.ActionBar;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.messenger.UserObject;
import org.telegram.ui.ActionBar.Theme;
import android.widget.LinearLayout;
import org.telegram.messenger.ChatObject;
import org.telegram.ui.Components.TypefaceSpan;
import android.text.TextUtils;
import android.text.SpannableStringBuilder;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.messenger.LocaleController;
import android.view.View;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Cells.GroupCreateUserCell;
import org.telegram.ui.ActionBar.ActionBarLayout;
import android.os.Bundle;
import org.telegram.messenger.MessagesController;
import android.widget.ScrollView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.GroupCreateDividerItemDecoration;
import org.telegram.tgnet.TLObject;
import android.util.SparseArray;
import android.widget.ImageView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import android.animation.AnimatorSet;
import org.telegram.ui.Components.GroupCreateSpan;
import java.util.ArrayList;
import android.view.View$OnClickListener;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class GroupCreateActivity extends BaseFragment implements NotificationCenterDelegate, View$OnClickListener
{
    private static final int done_button = 1;
    private GroupCreateAdapter adapter;
    private boolean addToGroup;
    private ArrayList<GroupCreateSpan> allSpans;
    private int channelId;
    private int chatId;
    private int chatType;
    private int containerHeight;
    private GroupCreateSpan currentDeletingSpan;
    private AnimatorSet currentDoneButtonAnimation;
    private GroupCreateActivityDelegate delegate;
    private ContactsAddActivityDelegate delegate2;
    private boolean doneButtonVisible;
    private EditTextBoldCursor editText;
    private EmptyTextProgressView emptyView;
    private int fieldY;
    private ImageView floatingButton;
    private boolean ignoreScrollEvent;
    private SparseArray<TLObject> ignoreUsers;
    private boolean isAlwaysShare;
    private boolean isGroup;
    private boolean isNeverShare;
    private GroupCreateDividerItemDecoration itemDecoration;
    private RecyclerListView listView;
    private int maxCount;
    private ScrollView scrollView;
    private boolean searchWas;
    private boolean searching;
    private SparseArray<GroupCreateSpan> selectedContacts;
    private SpansContainer spansContainer;
    
    public GroupCreateActivity() {
        this.maxCount = MessagesController.getInstance(super.currentAccount).maxMegagroupCount;
        this.chatType = 0;
        this.selectedContacts = (SparseArray<GroupCreateSpan>)new SparseArray();
        this.allSpans = new ArrayList<GroupCreateSpan>();
    }
    
    public GroupCreateActivity(final Bundle bundle) {
        super(bundle);
        this.maxCount = MessagesController.getInstance(super.currentAccount).maxMegagroupCount;
        this.chatType = 0;
        this.selectedContacts = (SparseArray<GroupCreateSpan>)new SparseArray();
        this.allSpans = new ArrayList<GroupCreateSpan>();
        this.chatType = bundle.getInt("chatType", 0);
        this.isAlwaysShare = bundle.getBoolean("isAlwaysShare", false);
        this.isNeverShare = bundle.getBoolean("isNeverShare", false);
        this.addToGroup = bundle.getBoolean("addToGroup", false);
        this.isGroup = bundle.getBoolean("isGroup", false);
        this.chatId = bundle.getInt("chatId");
        this.channelId = bundle.getInt("channelId");
        if (!this.isAlwaysShare && !this.isNeverShare && !this.addToGroup) {
            int maxCount;
            if (this.chatType == 0) {
                maxCount = MessagesController.getInstance(super.currentAccount).maxMegagroupCount;
            }
            else {
                maxCount = MessagesController.getInstance(super.currentAccount).maxBroadcastCount;
            }
            this.maxCount = maxCount;
        }
        else {
            this.maxCount = 0;
        }
    }
    
    private void checkVisibleRows() {
        for (int childCount = this.listView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.listView.getChildAt(i);
            if (child instanceof GroupCreateUserCell) {
                final GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell)child;
                final TLObject object = groupCreateUserCell.getObject();
                int id;
                if (object instanceof TLRPC.User) {
                    id = ((TLRPC.User)object).id;
                }
                else if (object instanceof TLRPC.Chat) {
                    id = -((TLRPC.Chat)object).id;
                }
                else {
                    id = 0;
                }
                if (id != 0) {
                    final SparseArray<TLObject> ignoreUsers = this.ignoreUsers;
                    if (ignoreUsers != null && ignoreUsers.indexOfKey(id) >= 0) {
                        groupCreateUserCell.setChecked(true, false);
                        groupCreateUserCell.setCheckBoxEnabled(false);
                    }
                    else {
                        groupCreateUserCell.setChecked(this.selectedContacts.indexOfKey(id) >= 0, true);
                        groupCreateUserCell.setCheckBoxEnabled(true);
                    }
                }
            }
        }
    }
    
    private void closeSearch() {
        this.searching = false;
        this.searchWas = false;
        this.itemDecoration.setSearching(false);
        this.adapter.setSearching(false);
        this.adapter.searchDialogs(null);
        this.listView.setFastScrollVisible(true);
        this.listView.setVerticalScrollBarEnabled(false);
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
    }
    
    private void onAddToGroupDone(final int n) {
        final ArrayList<TLRPC.User> list = new ArrayList<TLRPC.User>();
        for (int i = 0; i < this.selectedContacts.size(); ++i) {
            list.add(this.getMessagesController().getUser(this.selectedContacts.keyAt(i)));
        }
        final ContactsAddActivityDelegate delegate2 = this.delegate2;
        if (delegate2 != null) {
            delegate2.didSelectUsers(list, n);
        }
        this.finishFragment();
    }
    
    private boolean onDonePressed(final boolean b) {
        final int size = this.selectedContacts.size();
        int i = 0;
        if (size == 0) {
            return false;
        }
        if (b && this.addToGroup) {
            if (this.getParentActivity() == null) {
                return false;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
            if (this.selectedContacts.size() == 1) {
                builder.setTitle(LocaleController.getString("AddOneMemberAlertTitle", 2131558579));
            }
            else {
                builder.setTitle(LocaleController.formatString("AddMembersAlertTitle", 2131558576, LocaleController.formatPluralString("Members", this.selectedContacts.size())));
            }
            final StringBuilder sb = new StringBuilder();
            for (int j = 0; j < this.selectedContacts.size(); ++j) {
                final TLRPC.User user = this.getMessagesController().getUser(this.selectedContacts.keyAt(j));
                if (user != null) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append("**");
                    sb.append(ContactsController.formatName(user.first_name, user.last_name));
                    sb.append("**");
                }
            }
            final MessagesController messagesController = this.getMessagesController();
            int k = this.chatId;
            if (k == 0) {
                k = this.channelId;
            }
            final TLRPC.Chat chat = messagesController.getChat(k);
            if (this.selectedContacts.size() > 5) {
                final SpannableStringBuilder message = new SpannableStringBuilder((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", 2131558575, LocaleController.formatPluralString("Members", this.selectedContacts.size()), chat.title)));
                final String format = String.format("%d", this.selectedContacts.size());
                final int index = TextUtils.indexOf((CharSequence)message, (CharSequence)format);
                if (index >= 0) {
                    message.setSpan((Object)new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), index, format.length() + index, 33);
                }
                builder.setMessage((CharSequence)message);
            }
            else {
                builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AddMembersAlertNamesText", 2131558575, sb, chat.title)));
            }
            final CheckBoxCell[] array = { null };
            if (!ChatObject.isChannel(chat)) {
                final LinearLayout view = new LinearLayout((Context)this.getParentActivity());
                view.setOrientation(1);
                (array[0] = new CheckBoxCell((Context)this.getParentActivity(), 1)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
                array[0].setMultiline(true);
                if (this.selectedContacts.size() == 1) {
                    array[0].setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AddOneMemberForwardMessages", 2131558580, UserObject.getFirstName(this.getMessagesController().getUser(this.selectedContacts.keyAt(0))))), "", true, false);
                }
                else {
                    array[0].setText(LocaleController.getString("AddMembersForwardMessages", 2131558577), "", true, false);
                }
                final CheckBoxCell checkBoxCell = array[0];
                int n;
                if (LocaleController.isRTL) {
                    n = AndroidUtilities.dp(16.0f);
                }
                else {
                    n = AndroidUtilities.dp(8.0f);
                }
                int n2;
                if (LocaleController.isRTL) {
                    n2 = AndroidUtilities.dp(8.0f);
                }
                else {
                    n2 = AndroidUtilities.dp(16.0f);
                }
                checkBoxCell.setPadding(n, 0, n2, 0);
                view.addView((View)array[0], (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2));
                array[0].setOnClickListener((View$OnClickListener)new _$$Lambda$GroupCreateActivity$3XN987OICBmldOkyTJTx1cRwAQA(array));
                builder.setCustomViewOffset(12);
                builder.setView((View)view);
            }
            builder.setPositiveButton(LocaleController.getString("Add", 2131558555), (DialogInterface$OnClickListener)new _$$Lambda$GroupCreateActivity$R31kPKuAiH7RQVcTaHcTSTgxjkI(this, array));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            this.showDialog(builder.create());
        }
        else if (this.chatType == 2) {
            final ArrayList<TLRPC.InputUser> list = new ArrayList<TLRPC.InputUser>();
            for (int l = 0; l < this.selectedContacts.size(); ++l) {
                final TLRPC.InputUser inputUser = MessagesController.getInstance(super.currentAccount).getInputUser(MessagesController.getInstance(super.currentAccount).getUser(this.selectedContacts.keyAt(l)));
                if (inputUser != null) {
                    list.add(inputUser);
                }
            }
            MessagesController.getInstance(super.currentAccount).addUsersToChannel(this.chatId, list, null);
            NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            final Bundle bundle = new Bundle();
            bundle.putInt("chat_id", this.chatId);
            this.presentFragment(new ChatActivity(bundle), true);
        }
        else {
            if (!this.doneButtonVisible || this.selectedContacts.size() == 0) {
                return false;
            }
            if (this.addToGroup) {
                this.onAddToGroupDone(0);
            }
            else {
                final ArrayList<Integer> list2 = new ArrayList<Integer>();
                while (i < this.selectedContacts.size()) {
                    list2.add(this.selectedContacts.keyAt(i));
                    ++i;
                }
                if (!this.isAlwaysShare && !this.isNeverShare) {
                    final Bundle bundle2 = new Bundle();
                    bundle2.putIntegerArrayList("result", (ArrayList)list2);
                    bundle2.putInt("chatType", this.chatType);
                    this.presentFragment(new GroupCreateFinalActivity(bundle2));
                }
                else {
                    final GroupCreateActivityDelegate delegate = this.delegate;
                    if (delegate != null) {
                        delegate.didSelectUsers(list2);
                    }
                    this.finishFragment();
                }
            }
        }
        return true;
    }
    
    private void updateHint() {
        if (!this.isAlwaysShare && !this.isNeverShare && !this.addToGroup) {
            if (this.chatType == 2) {
                super.actionBar.setSubtitle(LocaleController.formatPluralString("Members", this.selectedContacts.size()));
            }
            else if (this.selectedContacts.size() == 0) {
                super.actionBar.setSubtitle(LocaleController.formatString("MembersCountZero", 2131559838, LocaleController.formatPluralString("Members", this.maxCount)));
            }
            else {
                super.actionBar.setSubtitle(LocaleController.formatString("MembersCount", 2131559837, this.selectedContacts.size(), this.maxCount));
            }
        }
        if (this.chatType != 2) {
            if (this.doneButtonVisible && this.allSpans.isEmpty()) {
                final AnimatorSet currentDoneButtonAnimation = this.currentDoneButtonAnimation;
                if (currentDoneButtonAnimation != null) {
                    currentDoneButtonAnimation.cancel();
                }
                (this.currentDoneButtonAnimation = new AnimatorSet()).playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "scaleX", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "scaleY", new float[] { 0.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "alpha", new float[] { 0.0f }) });
                this.currentDoneButtonAnimation.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        GroupCreateActivity.this.floatingButton.setVisibility(4);
                    }
                });
                this.currentDoneButtonAnimation.setDuration(180L);
                this.currentDoneButtonAnimation.start();
                this.doneButtonVisible = false;
            }
            else if (!this.doneButtonVisible && !this.allSpans.isEmpty()) {
                final AnimatorSet currentDoneButtonAnimation2 = this.currentDoneButtonAnimation;
                if (currentDoneButtonAnimation2 != null) {
                    currentDoneButtonAnimation2.cancel();
                }
                this.currentDoneButtonAnimation = new AnimatorSet();
                this.floatingButton.setVisibility(0);
                this.currentDoneButtonAnimation.playTogether(new Animator[] { (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "scaleX", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "scaleY", new float[] { 1.0f }), (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "alpha", new float[] { 1.0f }) });
                this.currentDoneButtonAnimation.setDuration(180L);
                this.currentDoneButtonAnimation.start();
                this.doneButtonVisible = true;
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        this.allSpans.clear();
        this.selectedContacts.clear();
        this.currentDeletingSpan = null;
        this.doneButtonVisible = (this.chatType == 2);
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        final int chatType = this.chatType;
        if (chatType == 2) {
            super.actionBar.setTitle(LocaleController.getString("ChannelAddMembers", 2131558920));
        }
        else if (this.addToGroup) {
            super.actionBar.setTitle(LocaleController.getString("SelectContacts", 2131560681));
        }
        else if (this.isAlwaysShare) {
            if (this.isGroup) {
                super.actionBar.setTitle(LocaleController.getString("AlwaysAllow", 2131558611));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("AlwaysShareWithTitle", 2131558613));
            }
        }
        else if (this.isNeverShare) {
            if (this.isGroup) {
                super.actionBar.setTitle(LocaleController.getString("NeverAllow", 2131559894));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("NeverShareWithTitle", 2131559896));
            }
        }
        else {
            final ActionBar actionBar = super.actionBar;
            int n;
            String s;
            if (chatType == 0) {
                n = 2131559900;
                s = "NewGroup";
            }
            else {
                n = 2131559897;
                s = "NewBroadcastList";
            }
            actionBar.setTitle(LocaleController.getString(s, n));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    GroupCreateActivity.this.finishFragment();
                }
                else if (n == 1) {
                    GroupCreateActivity.this.onDonePressed(true);
                }
            }
        });
        super.fragmentView = (View)new ViewGroup(context) {
            protected boolean drawChild(final Canvas canvas, final View view, final long n) {
                final boolean drawChild = super.drawChild(canvas, view, n);
                if (view == GroupCreateActivity.this.listView || view == GroupCreateActivity.this.emptyView) {
                    GroupCreateActivity.this.parentLayout.drawHeaderShadow(canvas, GroupCreateActivity.this.scrollView.getMeasuredHeight());
                }
                return drawChild;
            }
            
            protected void onLayout(final boolean b, int dp, int n, final int n2, final int n3) {
                GroupCreateActivity.this.scrollView.layout(0, 0, GroupCreateActivity.this.scrollView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight());
                GroupCreateActivity.this.listView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.listView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.listView.getMeasuredHeight());
                GroupCreateActivity.this.emptyView.layout(0, GroupCreateActivity.this.scrollView.getMeasuredHeight(), GroupCreateActivity.this.emptyView.getMeasuredWidth(), GroupCreateActivity.this.scrollView.getMeasuredHeight() + GroupCreateActivity.this.emptyView.getMeasuredHeight());
                if (GroupCreateActivity.this.floatingButton != null) {
                    if (LocaleController.isRTL) {
                        dp = AndroidUtilities.dp(14.0f);
                    }
                    else {
                        dp = n2 - dp - AndroidUtilities.dp(14.0f) - GroupCreateActivity.this.floatingButton.getMeasuredWidth();
                    }
                    n = n3 - n - AndroidUtilities.dp(14.0f) - GroupCreateActivity.this.floatingButton.getMeasuredHeight();
                    GroupCreateActivity.this.floatingButton.layout(dp, n, GroupCreateActivity.this.floatingButton.getMeasuredWidth() + dp, GroupCreateActivity.this.floatingButton.getMeasuredHeight() + n);
                }
            }
            
            protected void onMeasure(int n, int size) {
                final int size2 = View$MeasureSpec.getSize(n);
                size = View$MeasureSpec.getSize(size);
                this.setMeasuredDimension(size2, size);
                final boolean tablet = AndroidUtilities.isTablet();
                float n2 = 56.0f;
                if (!tablet && size <= size2) {
                    n = AndroidUtilities.dp(56.0f);
                }
                else {
                    n = AndroidUtilities.dp(144.0f);
                }
                GroupCreateActivity.this.scrollView.measure(View$MeasureSpec.makeMeasureSpec(size2, 1073741824), View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE));
                GroupCreateActivity.this.listView.measure(View$MeasureSpec.makeMeasureSpec(size2, 1073741824), View$MeasureSpec.makeMeasureSpec(size - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                GroupCreateActivity.this.emptyView.measure(View$MeasureSpec.makeMeasureSpec(size2, 1073741824), View$MeasureSpec.makeMeasureSpec(size - GroupCreateActivity.this.scrollView.getMeasuredHeight(), 1073741824));
                if (GroupCreateActivity.this.floatingButton != null) {
                    if (Build$VERSION.SDK_INT < 21) {
                        n2 = 60.0f;
                    }
                    n = AndroidUtilities.dp(n2);
                    GroupCreateActivity.this.floatingButton.measure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(n, 1073741824));
                }
            }
        };
        final ViewGroup viewGroup = (ViewGroup)super.fragmentView;
        (this.scrollView = new ScrollView(context) {
            public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
                if (GroupCreateActivity.this.ignoreScrollEvent) {
                    GroupCreateActivity.this.ignoreScrollEvent = false;
                    return false;
                }
                rect.offset(view.getLeft() - view.getScrollX(), view.getTop() - view.getScrollY());
                rect.top += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(20.0f);
                rect.bottom += GroupCreateActivity.this.fieldY + AndroidUtilities.dp(50.0f);
                return super.requestChildRectangleOnScreen(view, rect, b);
            }
        }).setVerticalScrollBarEnabled(false);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, Theme.getColor("windowBackgroundWhite"));
        viewGroup.addView((View)this.scrollView);
        this.spansContainer = new SpansContainer(context);
        this.scrollView.addView((View)this.spansContainer, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f));
        this.spansContainer.setOnClickListener((View$OnClickListener)new _$$Lambda$GroupCreateActivity$eUWXrWcPgL8v_iB3AHPMNXDrIzs(this));
        (this.editText = new EditTextBoldCursor(context) {
            public boolean onTouchEvent(final MotionEvent motionEvent) {
                if (GroupCreateActivity.this.currentDeletingSpan != null) {
                    GroupCreateActivity.this.currentDeletingSpan.cancelDeleteAnimation();
                    GroupCreateActivity.this.currentDeletingSpan = null;
                }
                if (motionEvent.getAction() == 0 && !AndroidUtilities.showKeyboard((View)this)) {
                    this.clearFocus();
                    this.requestFocus();
                }
                return super.onTouchEvent(motionEvent);
            }
        }).setTextSize(1, 16.0f);
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
        int n2;
        if (LocaleController.isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        editText.setGravity(n2 | 0x10);
        this.spansContainer.addView((View)this.editText);
        if (this.chatType == 2) {
            this.editText.setHintText(LocaleController.getString("AddMutual", 2131558578));
        }
        else if (this.addToGroup) {
            this.editText.setHintText(LocaleController.getString("SearchForPeople", 2131560644));
        }
        else if (!this.isAlwaysShare && !this.isNeverShare) {
            this.editText.setHintText(LocaleController.getString("SendMessageTo", 2131560703));
        }
        else {
            this.editText.setHintText(LocaleController.getString("SearchForPeopleAndGroups", 2131560645));
        }
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
        this.editText.setOnEditorActionListener((TextView$OnEditorActionListener)new _$$Lambda$GroupCreateActivity$gD5dQDpB0G04wuqUX611txNyFto(this));
        this.editText.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            private boolean wasEmpty;
            
            public boolean onKey(final View view, int action, final KeyEvent keyEvent) {
                if (action == 67) {
                    action = keyEvent.getAction();
                    boolean wasEmpty = true;
                    if (action == 0) {
                        if (GroupCreateActivity.this.editText.length() != 0) {
                            wasEmpty = false;
                        }
                        this.wasEmpty = wasEmpty;
                    }
                    else if (keyEvent.getAction() == 1 && this.wasEmpty && !GroupCreateActivity.this.allSpans.isEmpty()) {
                        GroupCreateActivity.this.spansContainer.removeSpan(GroupCreateActivity.this.allSpans.get(GroupCreateActivity.this.allSpans.size() - 1));
                        GroupCreateActivity.this.updateHint();
                        GroupCreateActivity.this.checkVisibleRows();
                        return true;
                    }
                }
                return false;
            }
        });
        this.editText.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
                if (GroupCreateActivity.this.editText.length() != 0) {
                    if (!GroupCreateActivity.this.adapter.searching) {
                        GroupCreateActivity.this.searching = true;
                        GroupCreateActivity.this.searchWas = true;
                        GroupCreateActivity.this.adapter.setSearching(true);
                        GroupCreateActivity.this.itemDecoration.setSearching(true);
                        GroupCreateActivity.this.listView.setFastScrollVisible(false);
                        GroupCreateActivity.this.listView.setVerticalScrollBarEnabled(true);
                        GroupCreateActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                        GroupCreateActivity.this.emptyView.showProgress();
                    }
                    GroupCreateActivity.this.adapter.searchDialogs(GroupCreateActivity.this.editText.getText().toString());
                }
                else {
                    GroupCreateActivity.this.closeSearch();
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
        this.emptyView.setShowAtCenter(true);
        this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
        viewGroup.addView((View)this.emptyView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, 1, false);
        (this.listView = new RecyclerListView(context)).setFastScrollEnabled();
        this.listView.setEmptyView((View)this.emptyView);
        this.listView.setAdapter(this.adapter = new GroupCreateAdapter(context));
        this.listView.setLayoutManager((RecyclerView.LayoutManager)layoutManager);
        this.listView.setVerticalScrollBarEnabled(false);
        final RecyclerListView listView = this.listView;
        int verticalScrollbarPosition;
        if (LocaleController.isRTL) {
            verticalScrollbarPosition = 1;
        }
        else {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        this.listView.addItemDecoration((RecyclerView.ItemDecoration)(this.itemDecoration = new GroupCreateDividerItemDecoration()));
        viewGroup.addView((View)this.listView);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$GroupCreateActivity$6yZ3Pg9mYqhNWQKcnf5So14xft8(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1) {
                    AndroidUtilities.hideKeyboard((View)GroupCreateActivity.this.editText);
                }
            }
        });
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
        if (!this.isNeverShare && !this.isAlwaysShare && !this.addToGroup) {
            final BackDrawable imageDrawable = new BackDrawable(false);
            imageDrawable.setArrowRotation(180);
            this.floatingButton.setImageDrawable((Drawable)imageDrawable);
        }
        else {
            this.floatingButton.setImageResource(2131165384);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            final StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[] { 16842919 }, (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "translationZ", new float[] { (float)AndroidUtilities.dp(2.0f), (float)AndroidUtilities.dp(4.0f) }).setDuration(200L));
            stateListAnimator.addState(new int[0], (Animator)ObjectAnimator.ofFloat((Object)this.floatingButton, "translationZ", new float[] { (float)AndroidUtilities.dp(4.0f), (float)AndroidUtilities.dp(2.0f) }).setDuration(200L));
            this.floatingButton.setStateListAnimator(stateListAnimator);
            this.floatingButton.setOutlineProvider((ViewOutlineProvider)new ViewOutlineProvider() {
                @SuppressLint({ "NewApi" })
                public void getOutline(final View view, final Outline outline) {
                    outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                }
            });
        }
        viewGroup.addView((View)this.floatingButton);
        this.floatingButton.setOnClickListener((View$OnClickListener)new _$$Lambda$GroupCreateActivity$Lid5KkPKTwmHxkGJugR_qNCJxvA(this));
        if (this.chatType != 2) {
            this.floatingButton.setVisibility(4);
            this.floatingButton.setScaleX(0.0f);
            this.floatingButton.setScaleY(0.0f);
            this.floatingButton.setAlpha(0.0f);
        }
        this.floatingButton.setContentDescription((CharSequence)LocaleController.getString("Next", 2131559911));
        this.updateHint();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int i, int n, final Object... array) {
        if (i == NotificationCenter.contactsDidLoad) {
            final EmptyTextProgressView emptyView = this.emptyView;
            if (emptyView != null) {
                emptyView.showTextView();
            }
            final GroupCreateAdapter adapter = this.adapter;
            if (adapter != null) {
                ((RecyclerView.Adapter)adapter).notifyDataSetChanged();
            }
        }
        else if (i == NotificationCenter.updateInterfaces) {
            if (this.listView != null) {
                n = 0;
                final int intValue = (int)array[0];
                final int childCount = this.listView.getChildCount();
                i = n;
                if ((intValue & 0x2) == 0x0) {
                    i = n;
                    if ((intValue & 0x1) == 0x0) {
                        if ((intValue & 0x4) == 0x0) {
                            return;
                        }
                        i = n;
                    }
                }
                while (i < childCount) {
                    final View child = this.listView.getChildAt(i);
                    if (child instanceof GroupCreateUserCell) {
                        ((GroupCreateUserCell)child).update(intValue);
                    }
                    ++i;
                }
            }
        }
        else if (i == NotificationCenter.chatDidCreated) {
            this.removeSelfFromStack();
        }
    }
    
    public int getContainerHeight() {
        return this.containerHeight;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE $$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE = new _$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollActive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollInactive"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_FASTSCROLL, null, null, null, null, "fastScrollText"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "groupcreate_hintText"), new ThemeDescription((View)this.editText, ThemeDescription.FLAG_CURSORCOLOR, null, null, null, null, "groupcreate_cursor"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { GroupCreateSectionCell.class }, null, null, null, "graySection"), new ThemeDescription((View)this.listView, 0, new Class[] { GroupCreateSectionCell.class }, new String[] { "drawable" }, null, null, null, "groupcreate_sectionShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { GroupCreateSectionCell.class }, new String[] { "textView" }, null, null, null, "groupcreate_sectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { GroupCreateUserCell.class }, new String[] { "textView" }, null, null, null, "groupcreate_sectionText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { GroupCreateUserCell.class }, new String[] { "checkBox" }, null, null, null, "checkbox"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { GroupCreateUserCell.class }, new String[] { "checkBox" }, null, null, null, "checkboxDisabled"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[] { GroupCreateUserCell.class }, new String[] { "checkBox" }, null, null, null, "checkboxCheck"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { GroupCreateUserCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[] { GroupCreateUserCell.class }, new String[] { "statusTextView" }, null, null, null, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { GroupCreateUserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$GroupCreateActivity$hlvy0IowbbkFD_DuH8EGWj1jbRE, "avatar_backgroundPink"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "avatar_backgroundGroupCreateSpanBlue"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "groupcreate_spanBackground"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "groupcreate_spanText"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "groupcreate_spanDelete"), new ThemeDescription((View)this.spansContainer, 0, new Class[] { GroupCreateSpan.class }, null, null, null, "avatar_backgroundBlue") };
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
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatDidCreated);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatDidCreated);
        AndroidUtilities.removeAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final EditTextBoldCursor editText = this.editText;
        if (editText != null) {
            editText.requestFocus();
        }
        AndroidUtilities.requestAdjustResize(this.getParentActivity(), super.classGuid);
    }
    
    @Keep
    public void setContainerHeight(final int containerHeight) {
        this.containerHeight = containerHeight;
        final SpansContainer spansContainer = this.spansContainer;
        if (spansContainer != null) {
            spansContainer.requestLayout();
        }
    }
    
    public void setDelegate(final ContactsAddActivityDelegate delegate2) {
        this.delegate2 = delegate2;
    }
    
    public void setDelegate(final GroupCreateActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setIgnoreUsers(final SparseArray<TLObject> ignoreUsers) {
        this.ignoreUsers = ignoreUsers;
    }
    
    public interface ContactsAddActivityDelegate
    {
        void didSelectUsers(final ArrayList<TLRPC.User> p0, final int p1);
        
        void needAddBot(final TLRPC.User p0);
    }
    
    public interface GroupCreateActivityDelegate
    {
        void didSelectUsers(final ArrayList<Integer> p0);
    }
    
    public class GroupCreateAdapter extends FastScrollAdapter
    {
        private ArrayList<TLObject> contacts;
        private Context context;
        private int inviteViaLink;
        private SearchAdapterHelper searchAdapterHelper;
        private ArrayList<TLObject> searchResult;
        private ArrayList<CharSequence> searchResultNames;
        private Timer searchTimer;
        private boolean searching;
        private int usersStartRow;
        
        public GroupCreateAdapter(final Context context) {
            this.searchResult = new ArrayList<TLObject>();
            this.searchResultNames = new ArrayList<CharSequence>();
            this.contacts = new ArrayList<TLObject>();
            this.context = context;
            final ArrayList<TLRPC.TL_contact> contacts = ContactsController.getInstance(GroupCreateActivity.this.currentAccount).contacts;
            for (int i = 0; i < contacts.size(); ++i) {
                final TLRPC.User user = MessagesController.getInstance(GroupCreateActivity.this.currentAccount).getUser(contacts.get(i).user_id);
                if (user != null && !user.self) {
                    if (!user.deleted) {
                        this.contacts.add(user);
                    }
                }
            }
            if (GroupCreateActivity.this.isNeverShare || GroupCreateActivity.this.isAlwaysShare) {
                final ArrayList<TLRPC.Dialog> allDialogs = GroupCreateActivity.this.getMessagesController().getAllDialogs();
                for (int size = allDialogs.size(), j = 0; j < size; ++j) {
                    final int n = (int)allDialogs.get(j).id;
                    if (n < 0) {
                        final TLRPC.Chat chat = GroupCreateActivity.this.getMessagesController().getChat(-n);
                        if (chat != null && chat.migrated_to == null) {
                            if (!ChatObject.isChannel(chat) || chat.megagroup) {
                                this.contacts.add(chat);
                            }
                        }
                    }
                }
                Collections.sort(this.contacts, new Comparator<TLObject>() {
                    private String getName(final TLObject tlObject) {
                        if (tlObject instanceof TLRPC.User) {
                            final TLRPC.User user = (TLRPC.User)tlObject;
                            return ContactsController.formatName(user.first_name, user.last_name);
                        }
                        return ((TLRPC.Chat)tlObject).title;
                    }
                    
                    @Override
                    public int compare(final TLObject tlObject, final TLObject tlObject2) {
                        return this.getName(tlObject).compareTo(this.getName(tlObject2));
                    }
                });
            }
            (this.searchAdapterHelper = new SearchAdapterHelper(false)).setDelegate((SearchAdapterHelper.SearchAdapterHelperDelegate)new SearchAdapterHelper.SearchAdapterHelperDelegate() {
                @Override
                public void onDataSetChanged() {
                    GroupCreateAdapter.this.notifyDataSetChanged();
                }
                
                @Override
                public void onSetHashtags(final ArrayList<HashtagObject> list, final HashMap<String, HashtagObject> hashMap) {
                }
            });
        }
        
        private void updateSearchResults(final ArrayList<TLObject> list, final ArrayList<CharSequence> list2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$GroupCreateActivity$GroupCreateAdapter$C0i_zDLg66DiKGKME02xepSowZQ(this, list, list2));
        }
        
        @Override
        public int getItemCount() {
            if (this.searching) {
                final int size = this.searchResult.size();
                final int size2 = this.searchAdapterHelper.getLocalServerSearch().size();
                final int size3 = this.searchAdapterHelper.getGlobalSearch().size();
                int n = size + size2;
                if (size3 != 0) {
                    n += size3 + 1;
                }
                return n;
            }
            int size4;
            final int n2 = size4 = this.contacts.size();
            if (GroupCreateActivity.this.addToGroup) {
                if (GroupCreateActivity.this.chatId != 0) {
                    this.inviteViaLink = (ChatObject.canUserDoAdminAction(MessagesController.getInstance(GroupCreateActivity.this.currentAccount).getChat(GroupCreateActivity.this.chatId), 3) ? 1 : 0);
                }
                else {
                    final int access$3500 = GroupCreateActivity.this.channelId;
                    final boolean b = false;
                    if (access$3500 != 0) {
                        final TLRPC.Chat chat = MessagesController.getInstance(GroupCreateActivity.this.currentAccount).getChat(GroupCreateActivity.this.channelId);
                        int inviteViaLink = b ? 1 : 0;
                        if (ChatObject.canUserDoAdminAction(chat, 3)) {
                            inviteViaLink = (b ? 1 : 0);
                            if (TextUtils.isEmpty((CharSequence)chat.username)) {
                                inviteViaLink = 2;
                            }
                        }
                        this.inviteViaLink = inviteViaLink;
                    }
                    else {
                        this.inviteViaLink = 0;
                    }
                }
                size4 = n2;
                if (this.inviteViaLink != 0) {
                    this.usersStartRow = 1;
                    size4 = n2 + 1;
                }
            }
            return size4;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (this.searching) {
                if (n == this.searchResult.size() + this.searchAdapterHelper.getLocalServerSearch().size()) {
                    return 0;
                }
                return 1;
            }
            else {
                if (this.inviteViaLink != 0 && n == 0) {
                    return 2;
                }
                return 1;
            }
        }
        
        @Override
        public String getLetter(final int n) {
            if (!this.searching && n >= this.usersStartRow) {
                final int size = this.contacts.size();
                final int usersStartRow = this.usersStartRow;
                if (n < size + usersStartRow) {
                    final TLObject tlObject = this.contacts.get(n - usersStartRow);
                    String s;
                    String last_name;
                    if (tlObject instanceof TLRPC.User) {
                        final TLRPC.User user = (TLRPC.User)tlObject;
                        s = user.first_name;
                        last_name = user.last_name;
                    }
                    else {
                        s = ((TLRPC.Chat)tlObject).title;
                        last_name = "";
                    }
                    if (LocaleController.nameDisplayOrder == 1) {
                        if (!TextUtils.isEmpty((CharSequence)s)) {
                            return s.substring(0, 1).toUpperCase();
                        }
                        if (!TextUtils.isEmpty((CharSequence)last_name)) {
                            return last_name.substring(0, 1).toUpperCase();
                        }
                    }
                    else {
                        if (!TextUtils.isEmpty((CharSequence)last_name)) {
                            return last_name.substring(0, 1).toUpperCase();
                        }
                        if (!TextUtils.isEmpty((CharSequence)s)) {
                            return s.substring(0, 1).toUpperCase();
                        }
                    }
                    return "";
                }
            }
            return null;
        }
        
        @Override
        public int getPositionForScrollProgress(final float n) {
            return (int)(this.getItemCount() * n);
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final SparseArray access$3700 = GroupCreateActivity.this.ignoreUsers;
            boolean b2;
            final boolean b = b2 = true;
            if (access$3700 != null) {
                final View itemView = viewHolder.itemView;
                b2 = b;
                if (itemView instanceof GroupCreateUserCell) {
                    final TLObject object = ((GroupCreateUserCell)itemView).getObject();
                    b2 = b;
                    if (object instanceof TLRPC.User) {
                        b2 = (GroupCreateActivity.this.ignoreUsers.indexOfKey(((TLRPC.User)object).id) < 0 && b);
                    }
                }
            }
            return b2;
        }
        
        @Override
        public void onBindViewHolder(ViewHolder o, int n) {
            final int itemViewType = ((RecyclerView.ViewHolder)o).getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType == 2) {
                        final TextCell textCell = (TextCell)((ViewHolder)o).itemView;
                        if (this.inviteViaLink == 2) {
                            textCell.setTextAndIcon(LocaleController.getString("ChannelInviteViaLink", 2131558953), 2131165787, false);
                        }
                        else {
                            textCell.setTextAndIcon(LocaleController.getString("InviteToGroupByLink", 2131559688), 2131165787, false);
                        }
                    }
                }
                else {
                    final GroupCreateUserCell groupCreateUserCell = (GroupCreateUserCell)((ViewHolder)o).itemView;
                    final boolean searching = this.searching;
                    final CharSequence charSequence = null;
                    Object o3 = null;
                    Object o4 = null;
                    CharSequence charSequence3 = null;
                    Label_0591: {
                        Object o2;
                        if (searching) {
                            final int size = this.searchResult.size();
                            final int size2 = this.searchAdapterHelper.getGlobalSearch().size();
                            final int size3 = this.searchAdapterHelper.getLocalServerSearch().size();
                            if (n >= 0 && n < size) {
                                o = this.searchResult.get(n);
                            }
                            else if (n >= size && n < size3 + size) {
                                o = this.searchAdapterHelper.getLocalServerSearch().get(n - size);
                            }
                            else if (n > size + size3 && n <= size2 + size + size3) {
                                o = this.searchAdapterHelper.getGlobalSearch().get(n - size - size3 - 1);
                            }
                            else {
                                o = null;
                            }
                            o2 = o;
                            if (o != null) {
                                String str;
                                if (o instanceof TLRPC.User) {
                                    str = ((TLRPC.User)o).username;
                                }
                                else {
                                    str = ((TLRPC.Chat)o).username;
                                }
                                if (n < size) {
                                    final CharSequence charSequence2 = this.searchResultNames.get(n);
                                    o3 = charSequence;
                                    o4 = o;
                                    if ((charSequence3 = charSequence2) == null) {
                                        break Label_0591;
                                    }
                                    o3 = charSequence;
                                    o4 = o;
                                    charSequence3 = charSequence2;
                                    if (TextUtils.isEmpty((CharSequence)str)) {
                                        break Label_0591;
                                    }
                                    final String string = charSequence2.toString();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("@");
                                    sb.append(str);
                                    o3 = charSequence;
                                    o4 = o;
                                    charSequence3 = charSequence2;
                                    if (string.startsWith(sb.toString())) {
                                        o3 = charSequence2;
                                        charSequence3 = null;
                                        o4 = o;
                                    }
                                    break Label_0591;
                                }
                                else {
                                    o2 = o;
                                    if (n > size) {
                                        o2 = o;
                                        if (!TextUtils.isEmpty((CharSequence)str)) {
                                            String str2;
                                            final String s = str2 = this.searchAdapterHelper.getLastFoundUsername();
                                            if (s.startsWith("@")) {
                                                str2 = s.substring(1);
                                            }
                                            try {
                                                o3 = new SpannableStringBuilder();
                                                ((SpannableStringBuilder)o3).append((CharSequence)"@");
                                                ((SpannableStringBuilder)o3).append((CharSequence)str);
                                                int index = str.toLowerCase().indexOf(str2);
                                                if (index != -1) {
                                                    n = str2.length();
                                                    if (index == 0) {
                                                        ++n;
                                                    }
                                                    else {
                                                        ++index;
                                                    }
                                                    ((SpannableStringBuilder)o3).setSpan((Object)new ForegroundColorSpan(Theme.getColor("windowBackgroundWhiteBlueText4")), index, n + index, 33);
                                                }
                                                charSequence3 = null;
                                                o4 = o;
                                            }
                                            catch (Exception ex) {
                                                charSequence3 = null;
                                                o3 = str;
                                                o4 = o;
                                            }
                                            break Label_0591;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            o2 = this.contacts.get(n - this.usersStartRow);
                        }
                        charSequence3 = null;
                        o4 = o2;
                        o3 = charSequence;
                    }
                    groupCreateUserCell.setObject((TLObject)o4, charSequence3, (CharSequence)o3);
                    if (o4 instanceof TLRPC.User) {
                        n = ((TLRPC.User)o4).id;
                    }
                    else if (o4 instanceof TLRPC.Chat) {
                        n = -((TLRPC.Chat)o4).id;
                    }
                    else {
                        n = 0;
                    }
                    if (n != 0) {
                        if (GroupCreateActivity.this.ignoreUsers != null && GroupCreateActivity.this.ignoreUsers.indexOfKey(n) >= 0) {
                            groupCreateUserCell.setChecked(true, false);
                            groupCreateUserCell.setCheckBoxEnabled(false);
                        }
                        else {
                            groupCreateUserCell.setChecked(GroupCreateActivity.this.selectedContacts.indexOfKey(n) >= 0, false);
                            groupCreateUserCell.setCheckBoxEnabled(true);
                        }
                    }
                }
            }
            else {
                final GroupCreateSectionCell groupCreateSectionCell = (GroupCreateSectionCell)((ViewHolder)o).itemView;
                if (this.searching) {
                    groupCreateSectionCell.setText(LocaleController.getString("GlobalSearch", 2131559594));
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    frameLayout = new TextCell(this.context);
                }
                else {
                    frameLayout = new GroupCreateUserCell(this.context, true, 0);
                }
            }
            else {
                frameLayout = new GroupCreateSectionCell(this.context);
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
        
        @Override
        public void onViewRecycled(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof GroupCreateUserCell) {
                ((GroupCreateUserCell)itemView).recycle();
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
                this.searchAdapterHelper.mergeResults(null);
                this.searchAdapterHelper.queryServerSearch(null, true, GroupCreateActivity.this.isAlwaysShare || GroupCreateActivity.this.isNeverShare, false, false, 0, 0);
                this.notifyDataSetChanged();
            }
            else {
                (this.searchTimer = new Timer()).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            GroupCreateAdapter.this.searchTimer.cancel();
                            GroupCreateAdapter.this.searchTimer = null;
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        AndroidUtilities.runOnUIThread(new _$$Lambda$GroupCreateActivity$GroupCreateAdapter$3$wWcLpY_du4_rpNgvonXl87vzFTU(this, s));
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
        
        public void addSpan(final GroupCreateSpan groupCreateSpan) {
            GroupCreateActivity.this.allSpans.add(groupCreateSpan);
            GroupCreateActivity.this.selectedContacts.put(groupCreateSpan.getUid(), (Object)groupCreateSpan);
            GroupCreateActivity.this.editText.setHintVisible(false);
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
                    GroupCreateActivity.this.editText.setAllowDrawCursor(true);
                }
            });
            this.currentAnimation.setDuration(150L);
            this.addingSpan = groupCreateSpan;
            this.animators.clear();
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.addingSpan, "scaleX", new float[] { 0.01f, 1.0f }));
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.addingSpan, "scaleY", new float[] { 0.01f, 1.0f }));
            this.animators.add((Animator)ObjectAnimator.ofFloat((Object)this.addingSpan, "alpha", new float[] { 0.0f, 1.0f }));
            this.addView((View)groupCreateSpan);
        }
        
        protected void onLayout(final boolean b, int i, int childCount, final int n, final int n2) {
            View child;
            for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                child = this.getChildAt(i);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
        
        protected void onMeasure(int n, int dp) {
            final int childCount = this.getChildCount();
            final int size = View$MeasureSpec.getSize(n);
            final int n2 = size - AndroidUtilities.dp(26.0f);
            dp = AndroidUtilities.dp(10.0f);
            n = AndroidUtilities.dp(10.0f);
            int i = 0;
            int n3 = 0;
            int n4 = 0;
            while (i < childCount) {
                final View child = this.getChildAt(i);
                int n5;
                if (!(child instanceof GroupCreateSpan)) {
                    n5 = n;
                }
                else {
                    child.measure(View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
                    int n6 = n3;
                    int n7 = dp;
                    if (child != this.removingSpan) {
                        n6 = n3;
                        n7 = dp;
                        if (child.getMeasuredWidth() + n3 > n2) {
                            n7 = dp + (child.getMeasuredHeight() + AndroidUtilities.dp(8.0f));
                            n6 = 0;
                        }
                    }
                    int n8 = n4;
                    dp = n;
                    if (child.getMeasuredWidth() + n4 > n2) {
                        dp = n + (child.getMeasuredHeight() + AndroidUtilities.dp(8.0f));
                        n8 = 0;
                    }
                    n = AndroidUtilities.dp(13.0f) + n6;
                    if (!this.animationStarted) {
                        final View removingSpan = this.removingSpan;
                        if (child == removingSpan) {
                            child.setTranslationX((float)(AndroidUtilities.dp(13.0f) + n8));
                            child.setTranslationY((float)dp);
                        }
                        else if (removingSpan != null) {
                            final float translationX = child.getTranslationX();
                            final float n9 = (float)n;
                            if (translationX != n9) {
                                this.animators.add((Animator)ObjectAnimator.ofFloat((Object)child, "translationX", new float[] { n9 }));
                            }
                            final float translationY = child.getTranslationY();
                            final float n10 = (float)n7;
                            if (translationY != n10) {
                                this.animators.add((Animator)ObjectAnimator.ofFloat((Object)child, "translationY", new float[] { n10 }));
                            }
                        }
                        else {
                            child.setTranslationX((float)n);
                            child.setTranslationY((float)n7);
                        }
                    }
                    n = n6;
                    if (child != this.removingSpan) {
                        n = n6 + (child.getMeasuredWidth() + AndroidUtilities.dp(9.0f));
                    }
                    n4 = n8 + (child.getMeasuredWidth() + AndroidUtilities.dp(9.0f));
                    n5 = dp;
                    dp = n7;
                    n3 = n;
                }
                ++i;
                n = n5;
            }
            int n11;
            if (AndroidUtilities.isTablet()) {
                n11 = AndroidUtilities.dp(372.0f) / 3;
            }
            else {
                final Point displaySize = AndroidUtilities.displaySize;
                n11 = (Math.min(displaySize.x, displaySize.y) - AndroidUtilities.dp(158.0f)) / 3;
            }
            int n12 = n3;
            int n13 = dp;
            if (n2 - n3 < n11) {
                n13 = dp + AndroidUtilities.dp(40.0f);
                n12 = 0;
            }
            dp = n;
            if (n2 - n4 < n11) {
                dp = n + AndroidUtilities.dp(40.0f);
            }
            GroupCreateActivity.this.editText.measure(View$MeasureSpec.makeMeasureSpec(n2 - n12, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(32.0f), 1073741824));
            if (!this.animationStarted) {
                n = AndroidUtilities.dp(42.0f);
                final int n14 = n12 + AndroidUtilities.dp(16.0f);
                GroupCreateActivity.this.fieldY = n13;
                if (this.currentAnimation != null) {
                    n = n13 + AndroidUtilities.dp(42.0f);
                    if (GroupCreateActivity.this.containerHeight != n) {
                        this.animators.add((Animator)ObjectAnimator.ofInt((Object)GroupCreateActivity.this, "containerHeight", new int[] { n }));
                    }
                    final float translationX2 = GroupCreateActivity.this.editText.getTranslationX();
                    final float n15 = (float)n14;
                    if (translationX2 != n15) {
                        this.animators.add((Animator)ObjectAnimator.ofFloat((Object)GroupCreateActivity.this.editText, "translationX", new float[] { n15 }));
                    }
                    if (GroupCreateActivity.this.editText.getTranslationY() != GroupCreateActivity.this.fieldY) {
                        this.animators.add((Animator)ObjectAnimator.ofFloat((Object)GroupCreateActivity.this.editText, "translationY", new float[] { (float)GroupCreateActivity.this.fieldY }));
                    }
                    GroupCreateActivity.this.editText.setAllowDrawCursor(false);
                    this.currentAnimation.playTogether((Collection)this.animators);
                    this.currentAnimation.start();
                    this.animationStarted = true;
                }
                else {
                    GroupCreateActivity.this.containerHeight = dp + n;
                    GroupCreateActivity.this.editText.setTranslationX((float)n14);
                    GroupCreateActivity.this.editText.setTranslationY((float)GroupCreateActivity.this.fieldY);
                }
            }
            else if (this.currentAnimation != null && !GroupCreateActivity.this.ignoreScrollEvent && this.removingSpan == null) {
                GroupCreateActivity.this.editText.bringPointIntoView(GroupCreateActivity.this.editText.getSelectionStart());
            }
            this.setMeasuredDimension(size, GroupCreateActivity.this.containerHeight);
        }
        
        public void removeSpan(final GroupCreateSpan groupCreateSpan) {
            GroupCreateActivity.this.ignoreScrollEvent = true;
            GroupCreateActivity.this.selectedContacts.remove(groupCreateSpan.getUid());
            GroupCreateActivity.this.allSpans.remove(groupCreateSpan);
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
                    GroupCreateActivity.this.editText.setAllowDrawCursor(true);
                    if (GroupCreateActivity.this.allSpans.isEmpty()) {
                        GroupCreateActivity.this.editText.setHintVisible(true);
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
