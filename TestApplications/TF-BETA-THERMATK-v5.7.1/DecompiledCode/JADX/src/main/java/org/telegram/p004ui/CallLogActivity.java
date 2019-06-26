package org.telegram.p004ui;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.LoadingCell;
import org.telegram.p004ui.Cells.LocationCell;
import org.telegram.p004ui.Cells.ProfileSearchCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Components.CombinedDrawable;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.p004ui.Components.voip.VoIPHelper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageAction;
import org.telegram.tgnet.TLRPC.PhoneCallDiscardReason;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputMessagesFilterPhoneCalls;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC.TL_messages_search;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.messages_Messages;

/* renamed from: org.telegram.ui.CallLogActivity */
public class CallLogActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int TYPE_IN = 1;
    private static final int TYPE_MISSED = 2;
    private static final int TYPE_OUT = 0;
    private OnClickListener callBtnClickListener = new C23181();
    private ArrayList<CallLogRow> calls = new ArrayList();
    private EmptyTextProgressView emptyView;
    private boolean endReached;
    private boolean firstLoaded;
    private ImageView floatingButton;
    private boolean floatingHidden;
    private final AccelerateDecelerateInterpolator floatingInterpolator = new AccelerateDecelerateInterpolator();
    private Drawable greenDrawable;
    private Drawable greenDrawable2;
    private ImageSpan iconIn;
    private ImageSpan iconMissed;
    private ImageSpan iconOut;
    private User lastCallUser;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loading;
    private int prevPosition;
    private int prevTop;
    private Drawable redDrawable;
    private boolean scrollUpdated;

    /* renamed from: org.telegram.ui.CallLogActivity$1 */
    class C23181 implements OnClickListener {
        C23181() {
        }

        public void onClick(View view) {
            CallLogRow callLogRow = (CallLogRow) view.getTag();
            CallLogActivity callLogActivity = CallLogActivity.this;
            User user = callLogRow.user;
            callLogActivity.lastCallUser = user;
            VoIPHelper.startCall(user, CallLogActivity.this.getParentActivity(), null);
        }
    }

    /* renamed from: org.telegram.ui.CallLogActivity$4 */
    class C23194 extends ViewOutlineProvider {
        C23194() {
        }

        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.m26dp(56.0f), AndroidUtilities.m26dp(56.0f));
        }
    }

    /* renamed from: org.telegram.ui.CallLogActivity$CallLogRow */
    private class CallLogRow {
        public List<Message> calls;
        public int type;
        public User user;

        private CallLogRow() {
        }

        /* synthetic */ CallLogRow(CallLogActivity callLogActivity, C23181 c23181) {
            this();
        }
    }

    /* renamed from: org.telegram.ui.CallLogActivity$CustomCell */
    private class CustomCell extends FrameLayout {
        public CustomCell(Context context) {
            super(context);
        }
    }

    /* renamed from: org.telegram.ui.CallLogActivity$ViewItem */
    private class ViewItem {
        public ImageView button;
        public ProfileSearchCell cell;

        public ViewItem(ImageView imageView, ProfileSearchCell profileSearchCell) {
            this.button = imageView;
            this.cell = profileSearchCell;
        }
    }

    /* renamed from: org.telegram.ui.CallLogActivity$2 */
    class C39352 extends ActionBarMenuOnItemClick {
        C39352() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                CallLogActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.CallLogActivity$3 */
    class C39363 extends OnScrollListener {
        C39363() {
        }

        /* JADX WARNING: Missing block: B:27:0x00a5, code skipped:
            if (java.lang.Math.abs(r1) > 1) goto L_0x00b3;
     */
        public void onScrolled(androidx.recyclerview.widget.RecyclerView r5, int r6, int r7) {
            /*
            r4 = this;
            r6 = org.telegram.p004ui.CallLogActivity.this;
            r6 = r6.layoutManager;
            r6 = r6.findFirstVisibleItemPosition();
            r7 = 0;
            r0 = 1;
            r1 = -1;
            if (r6 != r1) goto L_0x0011;
        L_0x000f:
            r1 = 0;
            goto L_0x0021;
        L_0x0011:
            r1 = org.telegram.p004ui.CallLogActivity.this;
            r1 = r1.layoutManager;
            r1 = r1.findLastVisibleItemPosition();
            r1 = r1 - r6;
            r1 = java.lang.Math.abs(r1);
            r1 = r1 + r0;
        L_0x0021:
            if (r1 <= 0) goto L_0x006d;
        L_0x0023:
            r2 = org.telegram.p004ui.CallLogActivity.this;
            r2 = r2.listViewAdapter;
            r2 = r2.getItemCount();
            r3 = org.telegram.p004ui.CallLogActivity.this;
            r3 = r3.endReached;
            if (r3 != 0) goto L_0x006d;
        L_0x0035:
            r3 = org.telegram.p004ui.CallLogActivity.this;
            r3 = r3.loading;
            if (r3 != 0) goto L_0x006d;
        L_0x003d:
            r3 = org.telegram.p004ui.CallLogActivity.this;
            r3 = r3.calls;
            r3 = r3.isEmpty();
            if (r3 != 0) goto L_0x006d;
        L_0x0049:
            r1 = r1 + r6;
            r2 = r2 + -5;
            if (r1 < r2) goto L_0x006d;
        L_0x004e:
            r1 = org.telegram.p004ui.CallLogActivity.this;
            r1 = r1.calls;
            r2 = org.telegram.p004ui.CallLogActivity.this;
            r2 = r2.calls;
            r2 = r2.size();
            r2 = r2 - r0;
            r1 = r1.get(r2);
            r1 = (org.telegram.p004ui.CallLogActivity.CallLogRow) r1;
            r2 = new org.telegram.ui.-$$Lambda$CallLogActivity$3$1DV60DlgjFkI_3XLU_3k-ebYIpc;
            r2.<init>(r4, r1);
            org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
        L_0x006d:
            r1 = org.telegram.p004ui.CallLogActivity.this;
            r1 = r1.floatingButton;
            r1 = r1.getVisibility();
            r2 = 8;
            if (r1 == r2) goto L_0x00d2;
        L_0x007b:
            r5 = r5.getChildAt(r7);
            if (r5 == 0) goto L_0x0086;
        L_0x0081:
            r5 = r5.getTop();
            goto L_0x0087;
        L_0x0086:
            r5 = 0;
        L_0x0087:
            r1 = org.telegram.p004ui.CallLogActivity.this;
            r1 = r1.prevPosition;
            if (r1 != r6) goto L_0x00a8;
        L_0x008f:
            r1 = org.telegram.p004ui.CallLogActivity.this;
            r1 = r1.prevTop;
            r1 = r1 - r5;
            r2 = org.telegram.p004ui.CallLogActivity.this;
            r2 = r2.prevTop;
            if (r5 >= r2) goto L_0x00a0;
        L_0x009e:
            r2 = 1;
            goto L_0x00a1;
        L_0x00a0:
            r2 = 0;
        L_0x00a1:
            r1 = java.lang.Math.abs(r1);
            if (r1 <= r0) goto L_0x00b4;
        L_0x00a7:
            goto L_0x00b3;
        L_0x00a8:
            r1 = org.telegram.p004ui.CallLogActivity.this;
            r1 = r1.prevPosition;
            if (r6 <= r1) goto L_0x00b2;
        L_0x00b0:
            r2 = 1;
            goto L_0x00b3;
        L_0x00b2:
            r2 = 0;
        L_0x00b3:
            r7 = 1;
        L_0x00b4:
            if (r7 == 0) goto L_0x00c3;
        L_0x00b6:
            r7 = org.telegram.p004ui.CallLogActivity.this;
            r7 = r7.scrollUpdated;
            if (r7 == 0) goto L_0x00c3;
        L_0x00be:
            r7 = org.telegram.p004ui.CallLogActivity.this;
            r7.hideFloatingButton(r2);
        L_0x00c3:
            r7 = org.telegram.p004ui.CallLogActivity.this;
            r7.prevPosition = r6;
            r6 = org.telegram.p004ui.CallLogActivity.this;
            r6.prevTop = r5;
            r5 = org.telegram.p004ui.CallLogActivity.this;
            r5.scrollUpdated = r0;
        L_0x00d2:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.CallLogActivity$C39363.onScrolled(androidx.recyclerview.widget.RecyclerView, int, int):void");
        }

        public /* synthetic */ void lambda$onScrolled$0$CallLogActivity$3(CallLogRow callLogRow) {
            CallLogActivity callLogActivity = CallLogActivity.this;
            List list = callLogRow.calls;
            callLogActivity.getCalls(((Message) list.get(list.size() - 1)).f461id, 100);
        }
    }

    /* renamed from: org.telegram.ui.CallLogActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() != CallLogActivity.this.calls.size();
        }

        public int getItemCount() {
            int size = CallLogActivity.this.calls.size();
            return (CallLogActivity.this.calls.isEmpty() || CallLogActivity.this.endReached) ? size : size + 1;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View customCell;
            String str = Theme.key_windowBackgroundWhite;
            if (i == 0) {
                customCell = new CustomCell(this.mContext);
                customCell.setBackgroundColor(Theme.getColor(str));
                ProfileSearchCell profileSearchCell = new ProfileSearchCell(this.mContext);
                profileSearchCell.setPadding(LocaleController.isRTL ? AndroidUtilities.m26dp(32.0f) : 0, 0, LocaleController.isRTL ? 0 : AndroidUtilities.m26dp(32.0f), 0);
                profileSearchCell.setSublabelOffset(AndroidUtilities.m26dp(LocaleController.isRTL ? 2.0f : -2.0f), -AndroidUtilities.m26dp(4.0f));
                customCell.addView(profileSearchCell);
                ImageView imageView = new ImageView(this.mContext);
                imageView.setImageResource(C1067R.C1065drawable.profile_phone);
                imageView.setAlpha(214);
                imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), Mode.MULTIPLY));
                imageView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR, 0));
                imageView.setScaleType(ScaleType.CENTER);
                imageView.setOnClickListener(CallLogActivity.this.callBtnClickListener);
                imageView.setContentDescription(LocaleController.getString("Call", C1067R.string.Call));
                customCell.addView(imageView, LayoutHelper.createFrame(48, 48.0f, (LocaleController.isRTL ? 3 : 5) | 16, 8.0f, 0.0f, 8.0f, 0.0f));
                customCell.setTag(new ViewItem(imageView, profileSearchCell));
            } else if (i != 1) {
                View textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                customCell = textInfoPrivacyCell;
            } else {
                customCell = new LoadingCell(this.mContext);
                customCell.setBackgroundColor(Theme.getColor(str));
            }
            return new Holder(customCell);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == 0) {
                SpannableString spannableString;
                ViewItem viewItem = (ViewItem) viewHolder.itemView.getTag();
                ProfileSearchCell profileSearchCell = viewItem.cell;
                CallLogRow callLogRow = (CallLogRow) CallLogActivity.this.calls.get(i);
                boolean z = false;
                Message message = (Message) callLogRow.calls.get(0);
                String str = LocaleController.isRTL ? "‫" : "";
                StringBuilder stringBuilder;
                if (callLogRow.calls.size() == 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append("  ");
                    stringBuilder.append(LocaleController.formatDateCallLog((long) message.date));
                    spannableString = new SpannableString(stringBuilder.toString());
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append("  (%d) %s");
                    spannableString = new SpannableString(String.format(stringBuilder.toString(), new Object[]{Integer.valueOf(callLogRow.calls.size()), LocaleController.formatDateCallLog((long) message.date)}));
                }
                SpannableString spannableString2 = spannableString;
                int i2 = callLogRow.type;
                if (i2 == 0) {
                    spannableString2.setSpan(CallLogActivity.this.iconOut, str.length(), str.length() + 1, 0);
                } else if (i2 == 1) {
                    spannableString2.setSpan(CallLogActivity.this.iconIn, str.length(), str.length() + 1, 0);
                } else if (i2 == 2) {
                    spannableString2.setSpan(CallLogActivity.this.iconMissed, str.length(), str.length() + 1, 0);
                }
                profileSearchCell.setData(callLogRow.user, null, null, spannableString2, false, false);
                if (!(i == CallLogActivity.this.calls.size() - 1 && CallLogActivity.this.endReached)) {
                    z = true;
                }
                profileSearchCell.useSeparator = z;
                viewItem.button.setTag(callLogRow);
            }
        }

        public int getItemViewType(int i) {
            if (i < CallLogActivity.this.calls.size()) {
                return 0;
            }
            return (CallLogActivity.this.endReached || i != CallLogActivity.this.calls.size()) ? 2 : 1;
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        int i3 = 0;
        if (i == NotificationCenter.didReceiveNewMessages && this.firstLoaded) {
            Iterator it = ((ArrayList) objArr[1]).iterator();
            while (it.hasNext()) {
                MessageObject messageObject = (MessageObject) it.next();
                Message message = messageObject.messageOwner;
                if (message.action instanceof TL_messageActionPhoneCall) {
                    CallLogRow callLogRow;
                    int i4 = message.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId() ? messageObject.messageOwner.to_id.user_id : messageObject.messageOwner.from_id;
                    int i5 = messageObject.messageOwner.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId() ? 0 : 1;
                    PhoneCallDiscardReason phoneCallDiscardReason = messageObject.messageOwner.action.reason;
                    if (i5 == 1 && ((phoneCallDiscardReason instanceof TL_phoneCallDiscardReasonMissed) || (phoneCallDiscardReason instanceof TL_phoneCallDiscardReasonBusy))) {
                        i5 = 2;
                    }
                    if (this.calls.size() > 0) {
                        callLogRow = (CallLogRow) this.calls.get(0);
                        if (callLogRow.user.f534id == i4 && callLogRow.type == i5) {
                            callLogRow.calls.add(0, messageObject.messageOwner);
                            this.listViewAdapter.notifyItemChanged(0);
                        }
                    }
                    callLogRow = new CallLogRow(this, null);
                    callLogRow.calls = new ArrayList();
                    callLogRow.calls.add(messageObject.messageOwner);
                    callLogRow.user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(i4));
                    callLogRow.type = i5;
                    this.calls.add(0, callLogRow);
                    this.listViewAdapter.notifyItemInserted(0);
                }
            }
        } else if (i == NotificationCenter.messagesDeleted && this.firstLoaded) {
            ArrayList arrayList = (ArrayList) objArr[0];
            Iterator it2 = this.calls.iterator();
            while (it2.hasNext()) {
                CallLogRow callLogRow2 = (CallLogRow) it2.next();
                Iterator it3 = callLogRow2.calls.iterator();
                while (it3.hasNext()) {
                    if (arrayList.contains(Integer.valueOf(((Message) it3.next()).f461id))) {
                        it3.remove();
                        i3 = 1;
                    }
                }
                if (callLogRow2.calls.size() == 0) {
                    it2.remove();
                }
            }
            if (i3 != 0) {
                ListAdapter listAdapter = this.listViewAdapter;
                if (listAdapter != null) {
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        getCalls(0, 50);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
    }

    public View createView(Context context) {
        this.greenDrawable = getParentActivity().getResources().getDrawable(C1067R.C1065drawable.ic_call_made_green_18dp).mutate();
        Drawable drawable = this.greenDrawable;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.greenDrawable.getIntrinsicHeight());
        drawable = this.greenDrawable;
        String str = Theme.key_calls_callReceivedGreenIcon;
        drawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str), Mode.MULTIPLY));
        this.iconOut = new ImageSpan(this.greenDrawable, 0);
        this.greenDrawable2 = getParentActivity().getResources().getDrawable(C1067R.C1065drawable.ic_call_received_green_18dp).mutate();
        drawable = this.greenDrawable2;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.greenDrawable2.getIntrinsicHeight());
        this.greenDrawable2.setColorFilter(new PorterDuffColorFilter(Theme.getColor(str), Mode.MULTIPLY));
        this.iconIn = new ImageSpan(this.greenDrawable2, 0);
        this.redDrawable = getParentActivity().getResources().getDrawable(C1067R.C1065drawable.ic_call_received_green_18dp).mutate();
        drawable = this.redDrawable;
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), this.redDrawable.getIntrinsicHeight());
        this.redDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_calls_callReceivedRedIcon), Mode.MULTIPLY));
        this.iconMissed = new ImageSpan(this.redDrawable, 0);
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setTitle(LocaleController.getString("Calls", C1067R.string.Calls));
        this.actionBar.setActionBarMenuOnItemClick(new C39352());
        this.fragmentView = new FrameLayout(context);
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        this.emptyView = new EmptyTextProgressView(context);
        this.emptyView.setText(LocaleController.getString("NoCallLog", C1067R.string.NoCallLog));
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView = new RecyclerListView(context);
        this.listView.setEmptyView(this.emptyView);
        RecyclerListView recyclerListView = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        recyclerListView = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listViewAdapter = listAdapter;
        recyclerListView.setAdapter(listAdapter);
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener(new C3587-$$Lambda$CallLogActivity$6UJZSG3aF6E0mUq9h4dUUmBm5H0(this));
        this.listView.setOnItemLongClickListener(new C3590-$$Lambda$CallLogActivity$gtRDuyh9OXWvOBCrfMrhHgkj_ds(this));
        this.listView.setOnScrollListener(new C39363());
        if (this.loading) {
            this.emptyView.showProgress();
        } else {
            this.emptyView.showTextView();
        }
        this.floatingButton = new ImageView(context);
        this.floatingButton.setVisibility(0);
        this.floatingButton.setScaleType(ScaleType.CENTER);
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.m26dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        if (VERSION.SDK_INT < 21) {
            Drawable mutate = context.getResources().getDrawable(C1067R.C1065drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, Mode.MULTIPLY));
            Drawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.m26dp(56.0f), AndroidUtilities.m26dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        this.floatingButton.setBackgroundDrawable(createSimpleSelectorCircleDrawable);
        this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chats_actionIcon), Mode.MULTIPLY));
        this.floatingButton.setImageResource(C1067R.C1065drawable.ic_call);
        this.floatingButton.setContentDescription(LocaleController.getString("Call", C1067R.string.Call));
        if (VERSION.SDK_INT >= 21) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            String str2 = "translationZ";
            stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.floatingButton, str2, new float[]{(float) AndroidUtilities.m26dp(2.0f), (float) AndroidUtilities.m26dp(4.0f)}).setDuration(200));
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButton, str2, new float[]{(float) AndroidUtilities.m26dp(4.0f), (float) AndroidUtilities.m26dp(2.0f)}).setDuration(200));
            this.floatingButton.setStateListAnimator(stateListAnimator);
            this.floatingButton.setOutlineProvider(new C23194());
        }
        frameLayout.addView(this.floatingButton, LayoutHelper.createFrame(VERSION.SDK_INT >= 21 ? 56 : 60, VERSION.SDK_INT >= 21 ? 56.0f : 60.0f, (LocaleController.isRTL ? 3 : 5) | 80, LocaleController.isRTL ? 14.0f : 0.0f, 0.0f, LocaleController.isRTL ? 0.0f : 14.0f, 14.0f));
        this.floatingButton.setOnClickListener(new C1219-$$Lambda$CallLogActivity$7Dw3WWwu34MwRrf1oRbnVFPZGBY(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$0$CallLogActivity(View view, int i) {
        if (i >= 0 && i < this.calls.size()) {
            CallLogRow callLogRow = (CallLogRow) this.calls.get(i);
            Bundle bundle = new Bundle();
            bundle.putInt("user_id", callLogRow.user.f534id);
            bundle.putInt("message_id", ((Message) callLogRow.calls.get(0)).f461id);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            presentFragment(new ChatActivity(bundle), true);
        }
    }

    public /* synthetic */ boolean lambda$createView$2$CallLogActivity(View view, int i) {
        if (i < 0 || i >= this.calls.size()) {
            return false;
        }
        CallLogRow callLogRow = (CallLogRow) this.calls.get(i);
        ArrayList arrayList = new ArrayList();
        arrayList.add(LocaleController.getString("Delete", C1067R.string.Delete));
        if (VoIPHelper.canRateCall((TL_messageActionPhoneCall) ((Message) callLogRow.calls.get(0)).action)) {
            arrayList.add(LocaleController.getString("CallMessageReportProblem", C1067R.string.CallMessageReportProblem));
        }
        new Builder(getParentActivity()).setTitle(LocaleController.getString("Calls", C1067R.string.Calls)).setItems((CharSequence[]) arrayList.toArray(new String[0]), new C1222-$$Lambda$CallLogActivity$attFsRotfjaCaQu5P_RAm6Vh5uM(this, callLogRow)).show();
        return true;
    }

    public /* synthetic */ void lambda$null$1$CallLogActivity(CallLogRow callLogRow, DialogInterface dialogInterface, int i) {
        if (i == 0) {
            confirmAndDelete(callLogRow);
        } else if (i == 1) {
            VoIPHelper.showRateAlert(getParentActivity(), (TL_messageActionPhoneCall) ((Message) callLogRow.calls.get(0)).action);
        }
    }

    public /* synthetic */ void lambda$createView$4$CallLogActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("destroyAfterSelect", true);
        bundle.putBoolean("returnAsResult", true);
        bundle.putBoolean("onlyUsers", true);
        ContactsActivity contactsActivity = new ContactsActivity(bundle);
        contactsActivity.setDelegate(new C3589-$$Lambda$CallLogActivity$glOYCOInhnTGmJL8rtqySIZYCwY(this));
        presentFragment(contactsActivity);
    }

    public /* synthetic */ void lambda$null$3$CallLogActivity(User user, String str, ContactsActivity contactsActivity) {
        VoIPHelper.startCall(user, getParentActivity(), null);
    }

    private void hideFloatingButton(boolean z) {
        if (this.floatingHidden != z) {
            this.floatingHidden = z;
            ImageView imageView = this.floatingButton;
            float[] fArr = new float[1];
            fArr[0] = this.floatingHidden ? (float) AndroidUtilities.m26dp(100.0f) : 0.0f;
            ObjectAnimator duration = ObjectAnimator.ofFloat(imageView, "translationY", fArr).setDuration(300);
            duration.setInterpolator(this.floatingInterpolator);
            this.floatingButton.setClickable(z ^ 1);
            duration.start();
        }
    }

    private void getCalls(int i, int i2) {
        if (!this.loading) {
            this.loading = true;
            EmptyTextProgressView emptyTextProgressView = this.emptyView;
            if (!(emptyTextProgressView == null || this.firstLoaded)) {
                emptyTextProgressView.showProgress();
            }
            ListAdapter listAdapter = this.listViewAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
            TL_messages_search tL_messages_search = new TL_messages_search();
            tL_messages_search.limit = i2;
            tL_messages_search.peer = new TL_inputPeerEmpty();
            tL_messages_search.filter = new TL_inputMessagesFilterPhoneCalls();
            tL_messages_search.f513q = "";
            tL_messages_search.offset_id = i;
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_search, new C3588-$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI(this), 2), this.classGuid);
        }
    }

    public /* synthetic */ void lambda$getCalls$6$CallLogActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1221-$$Lambda$CallLogActivity$UPlaIC1CV1ss5ZMXGEys0nI31eY(this, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$5$CallLogActivity(TL_error tL_error, TLObject tLObject) {
        if (tL_error == null) {
            int i;
            CallLogRow callLogRow;
            SparseArray sparseArray = new SparseArray();
            messages_Messages messages_messages = (messages_Messages) tLObject;
            this.endReached = messages_messages.messages.isEmpty();
            for (i = 0; i < messages_messages.users.size(); i++) {
                User user = (User) messages_messages.users.get(i);
                sparseArray.put(user.f534id, user);
            }
            if (this.calls.size() > 0) {
                ArrayList arrayList = this.calls;
                callLogRow = (CallLogRow) arrayList.get(arrayList.size() - 1);
            } else {
                callLogRow = null;
            }
            CallLogRow callLogRow2 = callLogRow;
            for (i = 0; i < messages_messages.messages.size(); i++) {
                Message message = (Message) messages_messages.messages.get(i);
                MessageAction messageAction = message.action;
                if (!(messageAction == null || (messageAction instanceof TL_messageActionHistoryClear))) {
                    int i2 = message.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId() ? 0 : 1;
                    PhoneCallDiscardReason phoneCallDiscardReason = message.action.reason;
                    if (i2 == 1 && ((phoneCallDiscardReason instanceof TL_phoneCallDiscardReasonMissed) || (phoneCallDiscardReason instanceof TL_phoneCallDiscardReasonBusy))) {
                        i2 = 2;
                    }
                    int i3 = message.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId() ? message.to_id.user_id : message.from_id;
                    if (!(callLogRow2 != null && callLogRow2.user.f534id == i3 && callLogRow2.type == i2)) {
                        if (!(callLogRow2 == null || this.calls.contains(callLogRow2))) {
                            this.calls.add(callLogRow2);
                        }
                        callLogRow2 = new CallLogRow(this, null);
                        callLogRow2.calls = new ArrayList();
                        callLogRow2.user = (User) sparseArray.get(i3);
                        callLogRow2.type = i2;
                    }
                    callLogRow2.calls.add(message);
                }
            }
            if (!(callLogRow2 == null || callLogRow2.calls.size() <= 0 || this.calls.contains(callLogRow2))) {
                this.calls.add(callLogRow2);
            }
        } else {
            this.endReached = true;
        }
        this.loading = false;
        this.firstLoaded = true;
        EmptyTextProgressView emptyTextProgressView = this.emptyView;
        if (emptyTextProgressView != null) {
            emptyTextProgressView.showTextView();
        }
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void confirmAndDelete(CallLogRow callLogRow) {
        if (getParentActivity() != null) {
            new Builder(getParentActivity()).setTitle(LocaleController.getString("AppName", C1067R.string.AppName)).setMessage(LocaleController.getString("ConfirmDeleteCallLog", C1067R.string.ConfirmDeleteCallLog)).setPositiveButton(LocaleController.getString("Delete", C1067R.string.Delete), new C1220-$$Lambda$CallLogActivity$H8-jHw_nBqLvayxPjMSmdGRTSFk(this, callLogRow)).setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null).show().setCanceledOnTouchOutside(true);
        }
    }

    public /* synthetic */ void lambda$confirmAndDelete$7$CallLogActivity(CallLogRow callLogRow, DialogInterface dialogInterface, int i) {
        ArrayList arrayList = new ArrayList();
        for (Message message : callLogRow.calls) {
            arrayList.add(Integer.valueOf(message.f461id));
        }
        MessagesController.getInstance(this.currentAccount).deleteMessages(arrayList, null, null, 0, false);
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listViewAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (i != 101) {
            return;
        }
        if (iArr.length <= 0 || iArr[0] != 0) {
            VoIPHelper.permissionDenied(getParentActivity(), null);
        } else {
            VoIPHelper.startCall(this.lastCallUser, getParentActivity(), null);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        C3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs = new C3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs(this);
        r10 = new ThemeDescription[33];
        r10[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{LocationCell.class, CustomCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r10[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r10[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r10[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r10[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r10[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r10[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r10[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r10[9] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_emptyListPlaceholder);
        r10[10] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle);
        r10[11] = new ThemeDescription(this.listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, null, null, null, Theme.key_progressCircle);
        View view = this.listView;
        View view2 = view;
        r10[12] = new ThemeDescription(view2, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[13] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r10[14] = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chats_actionIcon);
        r10[15] = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chats_actionBackground);
        r10[16] = new ThemeDescription(this.floatingButton, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chats_actionPressedBackground);
        r10[17] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedCheckDrawable}, null, Theme.key_chats_verifiedCheck);
        r10[18] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.dialogs_verifiedDrawable}, null, Theme.key_chats_verifiedBackground);
        r10[19] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_offlinePaint, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r10[20] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, Theme.dialogs_onlinePaint, null, null, Theme.key_windowBackgroundWhiteBlueText3);
        r10[21] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Paint[]{Theme.dialogs_namePaint, Theme.dialogs_searchNamePaint}, null, null, Theme.key_chats_name);
        r10[22] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Paint[]{Theme.dialogs_nameEncryptedPaint, Theme.dialogs_searchNameEncryptedPaint}, null, null, Theme.key_chats_secretName);
        r10[23] = new ThemeDescription(this.listView, 0, new Class[]{ProfileSearchCell.class}, null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        C3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2 = c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs;
        r10[24] = new ThemeDescription(null, 0, null, null, null, c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2, Theme.key_avatar_backgroundRed);
        r10[25] = new ThemeDescription(null, 0, null, null, null, c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2, Theme.key_avatar_backgroundOrange);
        r10[26] = new ThemeDescription(null, 0, null, null, null, c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2, Theme.key_avatar_backgroundViolet);
        r10[27] = new ThemeDescription(null, 0, null, null, null, c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2, Theme.key_avatar_backgroundGreen);
        r10[28] = new ThemeDescription(null, 0, null, null, null, c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2, Theme.key_avatar_backgroundCyan);
        r10[29] = new ThemeDescription(null, 0, null, null, null, c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2, Theme.key_avatar_backgroundBlue);
        r10[30] = new ThemeDescription(null, 0, null, null, null, c3591-$$Lambda$CallLogActivity$jzz7fdD3HMf_Ya8AHg1H2NHtPCs2, Theme.key_avatar_backgroundPink);
        r10[31] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, null, new Drawable[]{this.greenDrawable, this.greenDrawable2, Theme.calllog_msgCallUpRedDrawable, Theme.calllog_msgCallDownRedDrawable}, null, Theme.key_calls_callReceivedGreenIcon);
        r10[32] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, null, new Drawable[]{this.redDrawable, Theme.calllog_msgCallUpGreenDrawable, Theme.calllog_msgCallDownGreenDrawable}, null, Theme.key_calls_callReceivedRedIcon);
        return r10;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$8$CallLogActivity() {
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView != null) {
            int childCount = recyclerListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.listView.getChildAt(i);
                if (childAt instanceof ProfileSearchCell) {
                    ((ProfileSearchCell) childAt).update(0);
                }
            }
        }
    }
}
