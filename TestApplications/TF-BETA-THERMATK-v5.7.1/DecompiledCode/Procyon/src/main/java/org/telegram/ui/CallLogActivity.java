// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.text.SpannableString;
import android.util.SparseArray;
import android.os.Bundle;
import android.content.DialogInterface;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.LoadingCell;
import android.graphics.Paint;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import java.util.Iterator;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.MessageObject;
import android.annotation.SuppressLint;
import android.graphics.Outline;
import android.view.ViewOutlineProvider;
import android.animation.Animator;
import android.animation.StateListAnimator;
import org.telegram.ui.Components.CombinedDrawable;
import android.os.Build$VERSION;
import android.widget.ImageView$ScaleType;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Components.voip.VoIPHelper;
import android.view.View;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.tgnet.TLRPC;
import android.text.style.ImageSpan;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import org.telegram.ui.Components.EmptyTextProgressView;
import java.util.ArrayList;
import android.view.View$OnClickListener;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class CallLogActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int TYPE_IN = 1;
    private static final int TYPE_MISSED = 2;
    private static final int TYPE_OUT = 0;
    private View$OnClickListener callBtnClickListener;
    private ArrayList<CallLogRow> calls;
    private EmptyTextProgressView emptyView;
    private boolean endReached;
    private boolean firstLoaded;
    private ImageView floatingButton;
    private boolean floatingHidden;
    private final AccelerateDecelerateInterpolator floatingInterpolator;
    private Drawable greenDrawable;
    private Drawable greenDrawable2;
    private ImageSpan iconIn;
    private ImageSpan iconMissed;
    private ImageSpan iconOut;
    private TLRPC.User lastCallUser;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loading;
    private int prevPosition;
    private int prevTop;
    private Drawable redDrawable;
    private boolean scrollUpdated;
    
    public CallLogActivity() {
        this.calls = new ArrayList<CallLogRow>();
        this.floatingInterpolator = new AccelerateDecelerateInterpolator();
        this.callBtnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final CallLogRow callLogRow = (CallLogRow)view.getTag();
                final CallLogActivity this$0 = CallLogActivity.this;
                final TLRPC.User user = callLogRow.user;
                this$0.lastCallUser = user;
                VoIPHelper.startCall(user, CallLogActivity.this.getParentActivity(), null);
            }
        };
    }
    
    private void confirmAndDelete(final CallLogRow callLogRow) {
        if (this.getParentActivity() == null) {
            return;
        }
        new AlertDialog.Builder((Context)this.getParentActivity()).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("ConfirmDeleteCallLog", 2131559135)).setPositiveButton(LocaleController.getString("Delete", 2131559227), (DialogInterface$OnClickListener)new _$$Lambda$CallLogActivity$H8_jHw_nBqLvayxPjMSmdGRTSFk(this, callLogRow)).setNegativeButton(LocaleController.getString("Cancel", 2131558891), null).show().setCanceledOnTouchOutside(true);
    }
    
    private void getCalls(int sendRequest, final int limit) {
        if (this.loading) {
            return;
        }
        this.loading = true;
        final EmptyTextProgressView emptyView = this.emptyView;
        if (emptyView != null && !this.firstLoaded) {
            emptyView.showProgress();
        }
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
        final TLRPC.TL_messages_search tl_messages_search = new TLRPC.TL_messages_search();
        tl_messages_search.limit = limit;
        tl_messages_search.peer = new TLRPC.TL_inputPeerEmpty();
        tl_messages_search.filter = new TLRPC.TL_inputMessagesFilterPhoneCalls();
        tl_messages_search.q = "";
        tl_messages_search.offset_id = sendRequest;
        sendRequest = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_search, new _$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI(this), 2);
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(sendRequest, super.classGuid);
    }
    
    private void hideFloatingButton(final boolean floatingHidden) {
        if (this.floatingHidden == floatingHidden) {
            return;
        }
        this.floatingHidden = floatingHidden;
        final ImageView floatingButton = this.floatingButton;
        float n;
        if (this.floatingHidden) {
            n = (float)AndroidUtilities.dp(100.0f);
        }
        else {
            n = 0.0f;
        }
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)floatingButton, "translationY", new float[] { n }).setDuration(300L);
        setDuration.setInterpolator((TimeInterpolator)this.floatingInterpolator);
        this.floatingButton.setClickable(floatingHidden ^ true);
        setDuration.start();
    }
    
    @Override
    public View createView(final Context context) {
        this.greenDrawable = this.getParentActivity().getResources().getDrawable(2131165432).mutate();
        final Drawable greenDrawable = this.greenDrawable;
        greenDrawable.setBounds(0, 0, greenDrawable.getIntrinsicWidth(), this.greenDrawable.getIntrinsicHeight());
        this.greenDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("calls_callReceivedGreenIcon"), PorterDuff$Mode.MULTIPLY));
        this.iconOut = new ImageSpan(this.greenDrawable, 0);
        this.greenDrawable2 = this.getParentActivity().getResources().getDrawable(2131165435).mutate();
        final Drawable greenDrawable2 = this.greenDrawable2;
        greenDrawable2.setBounds(0, 0, greenDrawable2.getIntrinsicWidth(), this.greenDrawable2.getIntrinsicHeight());
        this.greenDrawable2.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("calls_callReceivedGreenIcon"), PorterDuff$Mode.MULTIPLY));
        this.iconIn = new ImageSpan(this.greenDrawable2, 0);
        this.redDrawable = this.getParentActivity().getResources().getDrawable(2131165435).mutate();
        final Drawable redDrawable = this.redDrawable;
        redDrawable.setBounds(0, 0, redDrawable.getIntrinsicWidth(), this.redDrawable.getIntrinsicHeight());
        this.redDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("calls_callReceivedRedIcon"), PorterDuff$Mode.MULTIPLY));
        this.iconMissed = new ImageSpan(this.redDrawable, 0);
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("Calls", 2131558888));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    CallLogActivity.this.finishFragment();
                }
            }
        });
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.emptyView = new EmptyTextProgressView(context)).setText(LocaleController.getString("NoCallLog", 2131559917));
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        this.listView.setAdapter(this.listViewAdapter = new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        int verticalScrollbarPosition;
        if (LocaleController.isRTL) {
            verticalScrollbarPosition = 1;
        }
        else {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$CallLogActivity$6UJZSG3aF6E0mUq9h4dUUmBm5H0(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$CallLogActivity$gtRDuyh9OXWvOBCrfMrhHgkj_ds(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int top, int n) {
                final int firstVisibleItemPosition = CallLogActivity.this.layoutManager.findFirstVisibleItemPosition();
                n = 0;
                if (firstVisibleItemPosition == -1) {
                    top = 0;
                }
                else {
                    top = Math.abs(CallLogActivity.this.layoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1;
                }
                if (top > 0) {
                    final int itemCount = CallLogActivity.this.listViewAdapter.getItemCount();
                    if (!CallLogActivity.this.endReached && !CallLogActivity.this.loading && !CallLogActivity.this.calls.isEmpty() && top + firstVisibleItemPosition >= itemCount - 5) {
                        AndroidUtilities.runOnUIThread(new _$$Lambda$CallLogActivity$3$1DV60DlgjFkI_3XLU_3k_ebYIpc(this, CallLogActivity.this.calls.get(CallLogActivity.this.calls.size() - 1)));
                    }
                }
                if (CallLogActivity.this.floatingButton.getVisibility() != 8) {
                    final View child = recyclerView.getChildAt(0);
                    if (child != null) {
                        top = child.getTop();
                    }
                    else {
                        top = 0;
                    }
                    boolean b2 = false;
                    Label_0261: {
                        boolean b;
                        if (CallLogActivity.this.prevPosition == firstVisibleItemPosition) {
                            final int access$900 = CallLogActivity.this.prevTop;
                            b = (b2 = (top < CallLogActivity.this.prevTop));
                            if (Math.abs(access$900 - top) <= 1) {
                                break Label_0261;
                            }
                        }
                        else {
                            b = (firstVisibleItemPosition > CallLogActivity.this.prevPosition);
                        }
                        n = 1;
                        b2 = b;
                    }
                    if (n != 0 && CallLogActivity.this.scrollUpdated) {
                        CallLogActivity.this.hideFloatingButton(b2);
                    }
                    CallLogActivity.this.prevPosition = firstVisibleItemPosition;
                    CallLogActivity.this.prevTop = top;
                    CallLogActivity.this.scrollUpdated = true;
                }
            }
        });
        if (this.loading) {
            this.emptyView.showProgress();
        }
        else {
            this.emptyView.showTextView();
        }
        (this.floatingButton = new ImageView(context)).setVisibility(0);
        this.floatingButton.setScaleType(ImageView$ScaleType.CENTER);
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
        this.floatingButton.setImageResource(2131165429);
        this.floatingButton.setContentDescription((CharSequence)LocaleController.getString("Call", 2131558869));
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
        final ImageView floatingButton = this.floatingButton;
        int n;
        if (Build$VERSION.SDK_INT >= 21) {
            n = 56;
        }
        else {
            n = 60;
        }
        float n2;
        if (Build$VERSION.SDK_INT >= 21) {
            n2 = 56.0f;
        }
        else {
            n2 = 60.0f;
        }
        int n3;
        if (LocaleController.isRTL) {
            n3 = 3;
        }
        else {
            n3 = 5;
        }
        float n4;
        if (LocaleController.isRTL) {
            n4 = 14.0f;
        }
        else {
            n4 = 0.0f;
        }
        float n5;
        if (LocaleController.isRTL) {
            n5 = 0.0f;
        }
        else {
            n5 = 14.0f;
        }
        frameLayout.addView((View)floatingButton, (ViewGroup$LayoutParams)LayoutHelper.createFrame(n, n2, n3 | 0x50, n4, 0.0f, n5, 14.0f));
        this.floatingButton.setOnClickListener((View$OnClickListener)new _$$Lambda$CallLogActivity$7Dw3WWwu34MwRrf1oRbnVFPZGBY(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int n, int i, final Object... array) {
        final int didReceiveNewMessages = NotificationCenter.didReceiveNewMessages;
        i = 0;
        if (n == didReceiveNewMessages && this.firstLoaded) {
            for (final MessageObject messageObject : (ArrayList)array[1]) {
                final TLRPC.Message messageOwner = messageObject.messageOwner;
                if (messageOwner.action instanceof TLRPC.TL_messageActionPhoneCall) {
                    if (messageOwner.from_id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                        i = messageObject.messageOwner.to_id.user_id;
                    }
                    else {
                        i = messageObject.messageOwner.from_id;
                    }
                    if (messageObject.messageOwner.from_id == UserConfig.getInstance(super.currentAccount).getClientUserId()) {
                        n = 0;
                    }
                    else {
                        n = 1;
                    }
                    final TLRPC.PhoneCallDiscardReason reason = messageObject.messageOwner.action.reason;
                    int type = n;
                    Label_0178: {
                        if (n == 1) {
                            if (!(reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed)) {
                                type = n;
                                if (!(reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy)) {
                                    break Label_0178;
                                }
                            }
                            type = 2;
                        }
                    }
                    if (this.calls.size() > 0) {
                        final CallLogRow callLogRow = this.calls.get(0);
                        if (callLogRow.user.id == i && callLogRow.type == type) {
                            callLogRow.calls.add(0, messageObject.messageOwner);
                            this.listViewAdapter.notifyItemChanged(0);
                            continue;
                        }
                    }
                    final CallLogRow element = new CallLogRow();
                    (element.calls = new ArrayList<TLRPC.Message>()).add(messageObject.messageOwner);
                    element.user = MessagesController.getInstance(super.currentAccount).getUser(i);
                    element.type = type;
                    this.calls.add(0, element);
                    this.listViewAdapter.notifyItemInserted(0);
                }
            }
        }
        else if (n == NotificationCenter.messagesDeleted && this.firstLoaded) {
            final ArrayList list = (ArrayList)array[0];
            final Iterator<CallLogRow> iterator2 = this.calls.iterator();
            while (iterator2.hasNext()) {
                final CallLogRow callLogRow2 = iterator2.next();
                final Iterator<TLRPC.Message> iterator3 = callLogRow2.calls.iterator();
                n = i;
                while (iterator3.hasNext()) {
                    if (list.contains(iterator3.next().id)) {
                        iterator3.remove();
                        n = 1;
                    }
                }
                i = n;
                if (callLogRow2.calls.size() == 0) {
                    iterator2.remove();
                    i = n;
                }
            }
            if (i != 0) {
                final ListAdapter listViewAdapter = this.listViewAdapter;
                if (listViewAdapter != null) {
                    ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
                }
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs $$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs = new _$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { LocationCell.class, CustomCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { LoadingCell.class }, new String[] { "progressBar" }, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "chats_actionIcon"), new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "chats_actionBackground"), new ThemeDescription((View)this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, "chats_actionPressedBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_verifiedCheckDrawable }, null, "chats_verifiedCheck"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Drawable[] { Theme.dialogs_verifiedDrawable }, null, "chats_verifiedBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, (Paint)Theme.dialogs_offlinePaint, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, (Paint)Theme.dialogs_onlinePaint, null, null, "windowBackgroundWhiteBlueText3"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_namePaint, (Paint)Theme.dialogs_searchNamePaint }, null, null, "chats_name"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_nameEncryptedPaint, (Paint)Theme.dialogs_searchNameEncryptedPaint }, null, null, "chats_secretName"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, null, new Drawable[] { this.greenDrawable, this.greenDrawable2, Theme.calllog_msgCallUpRedDrawable, Theme.calllog_msgCallDownRedDrawable }, null, "calls_callReceivedGreenIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, null, new Drawable[] { this.redDrawable, Theme.calllog_msgCallUpGreenDrawable, Theme.calllog_msgCallDownGreenDrawable }, null, "calls_callReceivedRedIcon") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.getCalls(0, 50);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
    }
    
    @Override
    public void onRequestPermissionsResultFragment(final int n, final String[] array, final int[] array2) {
        if (n == 101) {
            if (array2.length > 0 && array2[0] == 0) {
                VoIPHelper.startCall(this.lastCallUser, this.getParentActivity(), null);
            }
            else {
                VoIPHelper.permissionDenied(this.getParentActivity(), null);
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    private class CallLogRow
    {
        public List<TLRPC.Message> calls;
        public int type;
        public TLRPC.User user;
    }
    
    private class CustomCell extends FrameLayout
    {
        public CustomCell(final Context context) {
            super(context);
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            int size;
            final int n = size = CallLogActivity.this.calls.size();
            if (!CallLogActivity.this.calls.isEmpty()) {
                size = n;
                if (!CallLogActivity.this.endReached) {
                    size = n + 1;
                }
            }
            return size;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n < CallLogActivity.this.calls.size()) {
                return 0;
            }
            if (!CallLogActivity.this.endReached && n == CallLogActivity.this.calls.size()) {
                return 1;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() != CallLogActivity.this.calls.size();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
            if (viewHolder.getItemViewType() == 0) {
                final ViewItem viewItem = (ViewItem)viewHolder.itemView.getTag();
                final ProfileSearchCell cell = viewItem.cell;
                final CallLogRow tag = CallLogActivity.this.calls.get(index);
                final List<TLRPC.Message> calls = tag.calls;
                boolean useSeparator = false;
                final TLRPC.Message message = calls.get(0);
                String s;
                if (LocaleController.isRTL) {
                    s = "\u202b";
                }
                else {
                    s = "";
                }
                SpannableString spannableString;
                if (tag.calls.size() == 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append("  ");
                    sb.append(LocaleController.formatDateCallLog(message.date));
                    spannableString = new SpannableString((CharSequence)sb.toString());
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s);
                    sb2.append("  (%d) %s");
                    spannableString = new SpannableString((CharSequence)String.format(sb2.toString(), tag.calls.size(), LocaleController.formatDateCallLog(message.date)));
                }
                final int type = tag.type;
                if (type != 0) {
                    if (type != 1) {
                        if (type == 2) {
                            spannableString.setSpan((Object)CallLogActivity.this.iconMissed, s.length(), s.length() + 1, 0);
                        }
                    }
                    else {
                        spannableString.setSpan((Object)CallLogActivity.this.iconIn, s.length(), s.length() + 1, 0);
                    }
                }
                else {
                    spannableString.setSpan((Object)CallLogActivity.this.iconOut, s.length(), s.length() + 1, 0);
                }
                cell.setData(tag.user, null, null, (CharSequence)spannableString, false, false);
                if (index != CallLogActivity.this.calls.size() - 1 || !CallLogActivity.this.endReached) {
                    useSeparator = true;
                }
                cell.useSeparator = useSeparator;
                viewItem.button.setTag((Object)tag);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int dp) {
            FrameLayout frameLayout;
            if (dp != 0) {
                if (dp != 1) {
                    frameLayout = new TextInfoPrivacyCell(this.mContext);
                    ((View)frameLayout).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                }
                else {
                    frameLayout = new LoadingCell(this.mContext);
                    ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                frameLayout = new CustomCell(this.mContext);
                frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                final ProfileSearchCell profileSearchCell = new ProfileSearchCell(this.mContext);
                if (LocaleController.isRTL) {
                    dp = AndroidUtilities.dp(32.0f);
                }
                else {
                    dp = 0;
                }
                int dp2;
                if (LocaleController.isRTL) {
                    dp2 = 0;
                }
                else {
                    dp2 = AndroidUtilities.dp(32.0f);
                }
                profileSearchCell.setPadding(dp, 0, dp2, 0);
                float n;
                if (LocaleController.isRTL) {
                    n = 2.0f;
                }
                else {
                    n = -2.0f;
                }
                profileSearchCell.setSublabelOffset(AndroidUtilities.dp(n), -AndroidUtilities.dp(4.0f));
                frameLayout.addView((View)profileSearchCell);
                final ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(2131165791);
                imageView.setAlpha(214);
                imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), PorterDuff$Mode.MULTIPLY));
                imageView.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
                imageView.setScaleType(ImageView$ScaleType.CENTER);
                imageView.setOnClickListener(CallLogActivity.this.callBtnClickListener);
                imageView.setContentDescription((CharSequence)LocaleController.getString("Call", 2131558869));
                if (LocaleController.isRTL) {
                    dp = 3;
                }
                else {
                    dp = 5;
                }
                frameLayout.addView((View)imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(48, 48.0f, dp | 0x10, 8.0f, 0.0f, 8.0f, 0.0f));
                ((View)frameLayout).setTag((Object)new ViewItem(imageView, profileSearchCell));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
    
    private class ViewItem
    {
        public ImageView button;
        public ProfileSearchCell cell;
        
        public ViewItem(final ImageView button, final ProfileSearchCell cell) {
            this.button = button;
            this.cell = cell;
        }
    }
}
