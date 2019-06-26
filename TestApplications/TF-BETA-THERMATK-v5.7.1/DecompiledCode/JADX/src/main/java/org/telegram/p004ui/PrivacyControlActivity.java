package org.telegram.p004ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.ChatMessageCell;
import org.telegram.p004ui.Cells.ChatMessageCell.ChatMessageCellDelegate;
import org.telegram.p004ui.Cells.ChatMessageCell.ChatMessageCellDelegate.C2348-CC;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.RadioCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.HintView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.InputUser;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.PrivacyRule;
import org.telegram.tgnet.TLRPC.TL_account_privacyRules;
import org.telegram.tgnet.TLRPC.TL_account_setPrivacy;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyChatInvite;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyForwards;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyPhoneCall;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyPhoneNumber;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyPhoneP2P;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyProfilePhoto;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyKeyStatusTimestamp;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueAllowAll;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueAllowChatParticipants;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueAllowContacts;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueAllowUsers;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueDisallowAll;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueDisallowChatParticipants;
import org.telegram.tgnet.TLRPC.TL_inputPrivacyValueDisallowUsers;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageFwdHeader;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_pollAnswer;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowChatParticipants;
import org.telegram.tgnet.TLRPC.TL_privacyValueAllowUsers;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowAll;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowChatParticipants;
import org.telegram.tgnet.TLRPC.TL_privacyValueDisallowUsers;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.PrivacyControlActivity */
public class PrivacyControlActivity extends BaseFragment implements NotificationCenterDelegate {
    public static final int PRIVACY_RULES_TYPE_CALLS = 2;
    public static final int PRIVACY_RULES_TYPE_FORWARDS = 5;
    public static final int PRIVACY_RULES_TYPE_INVITE = 1;
    public static final int PRIVACY_RULES_TYPE_LASTSEEN = 0;
    public static final int PRIVACY_RULES_TYPE_P2P = 3;
    public static final int PRIVACY_RULES_TYPE_PHONE = 6;
    public static final int PRIVACY_RULES_TYPE_PHOTO = 4;
    private static final int done_button = 1;
    private int alwaysShareRow;
    private ArrayList<Integer> currentMinus;
    private ArrayList<Integer> currentPlus;
    private int currentType;
    private int detailRow;
    private View doneButton;
    private boolean enableAnimation;
    private int everybodyRow;
    private ArrayList<Integer> initialMinus;
    private ArrayList<Integer> initialPlus;
    private int initialRulesType;
    private int lastCheckedType;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private MessageCell messageCell;
    private int messageRow;
    private int myContactsRow;
    private int neverShareRow;
    private int nobodyRow;
    private int p2pDetailRow;
    private int p2pRow;
    private int p2pSectionRow;
    private int rowCount;
    private int rulesType;
    private int sectionRow;
    private int shareDetailRow;
    private int shareSectionRow;

    /* renamed from: org.telegram.ui.PrivacyControlActivity$LinkMovementMethodMy */
    private static class LinkMovementMethodMy extends LinkMovementMethod {
        private LinkMovementMethodMy() {
        }

        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            try {
                return super.onTouchEvent(textView, spannable, motionEvent);
            } catch (Exception e) {
                FileLog.m30e(e);
                return false;
            }
        }
    }

    /* renamed from: org.telegram.ui.PrivacyControlActivity$MessageCell */
    private class MessageCell extends FrameLayout {
        private Drawable backgroundDrawable;
        private ChatMessageCell cell;
        private HintView hintView;
        private MessageObject messageObject;
        private Drawable shadowDrawable;

        /* Access modifiers changed, original: protected */
        public void dispatchSetPressed(boolean z) {
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public MessageCell(Context context) {
            super(context);
            setWillNotDraw(false);
            setClipToPadding(false);
            this.shadowDrawable = Theme.getThemedDrawable(context, (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow);
            setPadding(0, AndroidUtilities.m26dp(11.0f), 0, AndroidUtilities.m26dp(11.0f));
            int currentTimeMillis = ((int) (System.currentTimeMillis() / 1000)) - 3600;
            User user = MessagesController.getInstance(PrivacyControlActivity.this.currentAccount).getUser(Integer.valueOf(UserConfig.getInstance(PrivacyControlActivity.this.currentAccount).getClientUserId()));
            TL_message tL_message = new TL_message();
            tL_message.message = LocaleController.getString("PrivacyForwardsMessageLine", C1067R.string.PrivacyForwardsMessageLine);
            tL_message.date = currentTimeMillis + 60;
            tL_message.fwd_from = new TL_messageFwdHeader();
            tL_message.fwd_from.from_name = ContactsController.formatName(user.first_name, user.last_name);
            tL_message.media = new TL_messageMediaEmpty();
            tL_message.out = false;
            tL_message.to_id = new TL_peerUser();
            tL_message.to_id.user_id = UserConfig.getInstance(PrivacyControlActivity.this.currentAccount).getClientUserId();
            this.messageObject = new MessageObject(PrivacyControlActivity.this.currentAccount, tL_message, true);
            MessageObject messageObject = this.messageObject;
            messageObject.eventId = 1;
            messageObject.resetLayout();
            this.cell = new ChatMessageCell(context);
            this.cell.setDelegate(new ChatMessageCellDelegate(PrivacyControlActivity.this) {
                public /* synthetic */ boolean canPerformActions() {
                    return C2348-CC.$default$canPerformActions(this);
                }

                public /* synthetic */ void didLongPress(ChatMessageCell chatMessageCell, float f, float f2) {
                    C2348-CC.$default$didLongPress(this, chatMessageCell, f, f2);
                }

                public /* synthetic */ void didPressBotButton(ChatMessageCell chatMessageCell, KeyboardButton keyboardButton) {
                    C2348-CC.$default$didPressBotButton(this, chatMessageCell, keyboardButton);
                }

                public /* synthetic */ void didPressCancelSendButton(ChatMessageCell chatMessageCell) {
                    C2348-CC.$default$didPressCancelSendButton(this, chatMessageCell);
                }

                public /* synthetic */ void didPressChannelAvatar(ChatMessageCell chatMessageCell, Chat chat, int i, float f, float f2) {
                    C2348-CC.$default$didPressChannelAvatar(this, chatMessageCell, chat, i, f, f2);
                }

                public /* synthetic */ void didPressHiddenForward(ChatMessageCell chatMessageCell) {
                    C2348-CC.$default$didPressHiddenForward(this, chatMessageCell);
                }

                public /* synthetic */ void didPressImage(ChatMessageCell chatMessageCell, float f, float f2) {
                    C2348-CC.$default$didPressImage(this, chatMessageCell, f, f2);
                }

                public /* synthetic */ void didPressInstantButton(ChatMessageCell chatMessageCell, int i) {
                    C2348-CC.$default$didPressInstantButton(this, chatMessageCell, i);
                }

                public /* synthetic */ void didPressOther(ChatMessageCell chatMessageCell, float f, float f2) {
                    C2348-CC.$default$didPressOther(this, chatMessageCell, f, f2);
                }

                public /* synthetic */ void didPressReplyMessage(ChatMessageCell chatMessageCell, int i) {
                    C2348-CC.$default$didPressReplyMessage(this, chatMessageCell, i);
                }

                public /* synthetic */ void didPressShare(ChatMessageCell chatMessageCell) {
                    C2348-CC.$default$didPressShare(this, chatMessageCell);
                }

                public /* synthetic */ void didPressUrl(MessageObject messageObject, CharacterStyle characterStyle, boolean z) {
                    C2348-CC.$default$didPressUrl(this, messageObject, characterStyle, z);
                }

                public /* synthetic */ void didPressUserAvatar(ChatMessageCell chatMessageCell, User user, float f, float f2) {
                    C2348-CC.$default$didPressUserAvatar(this, chatMessageCell, user, f, f2);
                }

                public /* synthetic */ void didPressViaBot(ChatMessageCell chatMessageCell, String str) {
                    C2348-CC.$default$didPressViaBot(this, chatMessageCell, str);
                }

                public /* synthetic */ void didPressVoteButton(ChatMessageCell chatMessageCell, TL_pollAnswer tL_pollAnswer) {
                    C2348-CC.$default$didPressVoteButton(this, chatMessageCell, tL_pollAnswer);
                }

                public /* synthetic */ void didStartVideoStream(MessageObject messageObject) {
                    C2348-CC.$default$didStartVideoStream(this, messageObject);
                }

                public /* synthetic */ boolean isChatAdminCell(int i) {
                    return C2348-CC.$default$isChatAdminCell(this, i);
                }

                public /* synthetic */ void needOpenWebView(String str, String str2, String str3, String str4, int i, int i2) {
                    C2348-CC.$default$needOpenWebView(this, str, str2, str3, str4, i, i2);
                }

                public /* synthetic */ boolean needPlayMessage(MessageObject messageObject) {
                    return C2348-CC.$default$needPlayMessage(this, messageObject);
                }

                public /* synthetic */ void videoTimerReached() {
                    C2348-CC.$default$videoTimerReached(this);
                }
            });
            ChatMessageCell chatMessageCell = this.cell;
            chatMessageCell.isChat = false;
            chatMessageCell.setFullyDraw(true);
            this.cell.setMessageObject(this.messageObject, null, false, false);
            addView(this.cell, LayoutHelper.createLinear(-1, -2));
            this.hintView = new HintView(context, 1, true);
            addView(this.hintView, LayoutHelper.createFrame(-2, -2.0f, 51, 19.0f, 0.0f, 19.0f, 0.0f));
        }

        /* Access modifiers changed, original: protected */
        public void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            this.hintView.showForMessageCell(this.cell, false);
        }

        /* Access modifiers changed, original: protected */
        public void onDraw(Canvas canvas) {
            Drawable cachedWallpaperNonBlocking = Theme.getCachedWallpaperNonBlocking();
            if (cachedWallpaperNonBlocking != null) {
                this.backgroundDrawable = cachedWallpaperNonBlocking;
            }
            cachedWallpaperNonBlocking = this.backgroundDrawable;
            if (cachedWallpaperNonBlocking instanceof ColorDrawable) {
                cachedWallpaperNonBlocking.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                this.backgroundDrawable.draw(canvas);
            } else if (!(cachedWallpaperNonBlocking instanceof BitmapDrawable)) {
                super.onDraw(canvas);
            } else if (((BitmapDrawable) cachedWallpaperNonBlocking).getTileModeX() == TileMode.REPEAT) {
                canvas.save();
                float f = 2.0f / AndroidUtilities.density;
                canvas.scale(f, f);
                this.backgroundDrawable.setBounds(0, 0, (int) Math.ceil((double) (((float) getMeasuredWidth()) / f)), (int) Math.ceil((double) (((float) getMeasuredHeight()) / f)));
                this.backgroundDrawable.draw(canvas);
                canvas.restore();
            } else {
                int measuredHeight = getMeasuredHeight();
                float measuredWidth = ((float) getMeasuredWidth()) / ((float) this.backgroundDrawable.getIntrinsicWidth());
                float intrinsicHeight = ((float) measuredHeight) / ((float) this.backgroundDrawable.getIntrinsicHeight());
                if (measuredWidth < intrinsicHeight) {
                    measuredWidth = intrinsicHeight;
                }
                int ceil = (int) Math.ceil((double) (((float) this.backgroundDrawable.getIntrinsicWidth()) * measuredWidth));
                int ceil2 = (int) Math.ceil((double) (((float) this.backgroundDrawable.getIntrinsicHeight()) * measuredWidth));
                int measuredWidth2 = (getMeasuredWidth() - ceil) / 2;
                measuredHeight = (measuredHeight - ceil2) / 2;
                canvas.save();
                canvas.clipRect(0, 0, ceil, getMeasuredHeight());
                this.backgroundDrawable.setBounds(measuredWidth2, measuredHeight, ceil + measuredWidth2, ceil2 + measuredHeight);
                this.backgroundDrawable.draw(canvas);
                canvas.restore();
            }
            this.shadowDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.shadowDrawable.draw(canvas);
        }

        public void invalidate() {
            super.invalidate();
            this.cell.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.PrivacyControlActivity$1 */
    class C43031 extends ActionBarMenuOnItemClick {
        C43031() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                if (PrivacyControlActivity.this.checkDiscard()) {
                    PrivacyControlActivity.this.finishFragment();
                }
            } else if (i == 1) {
                PrivacyControlActivity.this.processDone();
            }
        }
    }

    /* renamed from: org.telegram.ui.PrivacyControlActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PrivacyControlActivity.this.nobodyRow || adapterPosition == PrivacyControlActivity.this.everybodyRow || adapterPosition == PrivacyControlActivity.this.myContactsRow || adapterPosition == PrivacyControlActivity.this.neverShareRow || adapterPosition == PrivacyControlActivity.this.alwaysShareRow || (adapterPosition == PrivacyControlActivity.this.p2pRow && !ContactsController.getInstance(PrivacyControlActivity.this.currentAccount).getLoadingPrivicyInfo(3));
        }

        public int getItemCount() {
            return PrivacyControlActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View headerCell;
            String str = Theme.key_windowBackgroundWhite;
            if (i != 0) {
                View textInfoPrivacyCell;
                if (i == 1) {
                    textInfoPrivacyCell = new TextInfoPrivacyCell(this.mContext);
                } else if (i == 2) {
                    headerCell = new HeaderCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(str));
                } else if (i != 3) {
                    textInfoPrivacyCell = PrivacyControlActivity.this.messageCell;
                } else {
                    headerCell = new RadioCell(this.mContext);
                    headerCell.setBackgroundColor(Theme.getColor(str));
                }
                headerCell = textInfoPrivacyCell;
            } else {
                headerCell = new TextSettingsCell(this.mContext);
                headerCell.setBackgroundColor(Theme.getColor(str));
            }
            return new Holder(headerCell);
        }

        private int getUsersCount(ArrayList<Integer> arrayList) {
            int i = 0;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                int intValue = ((Integer) arrayList.get(i2)).intValue();
                if (intValue > 0) {
                    i++;
                } else {
                    Chat chat = PrivacyControlActivity.this.getMessagesController().getChat(Integer.valueOf(-intValue));
                    if (chat != null) {
                        i += chat.participants_count;
                    }
                }
            }
            return i;
        }

        /* JADX WARNING: Removed duplicated region for block: B:61:0x0107  */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x00fc  */
        public void onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder r11, int r12) {
            /*
            r10 = this;
            r0 = r11.getItemViewType();
            r1 = -1;
            r2 = 3;
            r3 = 0;
            r4 = 1;
            if (r0 == 0) goto L_0x0352;
        L_0x000a:
            r5 = 4;
            r6 = 5;
            r7 = 6;
            r8 = 2;
            if (r0 == r4) goto L_0x01e4;
        L_0x0010:
            if (r0 == r8) goto L_0x011a;
        L_0x0012:
            if (r0 == r2) goto L_0x0016;
        L_0x0014:
            goto L_0x043b;
        L_0x0016:
            r11 = r11.itemView;
            r11 = (org.telegram.p004ui.Cells.RadioCell) r11;
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.everybodyRow;
            if (r12 != r0) goto L_0x005c;
        L_0x0022:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r2) goto L_0x0043;
        L_0x002a:
            r12 = 2131560139; // 0x7f0d06cb float:1.8745642E38 double:1.0531306367E-314;
            r0 = "P2PEverybody";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.lastCheckedType;
            if (r0 != 0) goto L_0x003d;
        L_0x003b:
            r0 = 1;
            goto L_0x003e;
        L_0x003d:
            r0 = 0;
        L_0x003e:
            r11.setText(r12, r0, r4);
            goto L_0x00f3;
        L_0x0043:
            r12 = 2131559736; // 0x7f0d0538 float:1.8744824E38 double:1.0531304376E-314;
            r0 = "LastSeenEverybody";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.lastCheckedType;
            if (r0 != 0) goto L_0x0056;
        L_0x0054:
            r0 = 1;
            goto L_0x0057;
        L_0x0056:
            r0 = 0;
        L_0x0057:
            r11.setText(r12, r0, r4);
            goto L_0x00f3;
        L_0x005c:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.myContactsRow;
            if (r12 != r0) goto L_0x00b2;
        L_0x0064:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r2) goto L_0x008f;
        L_0x006c:
            r12 = 2131560134; // 0x7f0d06c6 float:1.8745632E38 double:1.0531306343E-314;
            r0 = "P2PContacts";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.lastCheckedType;
            if (r0 != r8) goto L_0x007f;
        L_0x007d:
            r0 = 1;
            goto L_0x0080;
        L_0x007f:
            r0 = 0;
        L_0x0080:
            r2 = org.telegram.p004ui.PrivacyControlActivity.this;
            r2 = r2.nobodyRow;
            if (r2 == r1) goto L_0x008a;
        L_0x0088:
            r1 = 1;
            goto L_0x008b;
        L_0x008a:
            r1 = 0;
        L_0x008b:
            r11.setText(r12, r0, r1);
            goto L_0x00f4;
        L_0x008f:
            r12 = 2131559730; // 0x7f0d0532 float:1.8744812E38 double:1.0531304347E-314;
            r0 = "LastSeenContacts";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.lastCheckedType;
            if (r0 != r8) goto L_0x00a2;
        L_0x00a0:
            r0 = 1;
            goto L_0x00a3;
        L_0x00a2:
            r0 = 0;
        L_0x00a3:
            r2 = org.telegram.p004ui.PrivacyControlActivity.this;
            r2 = r2.nobodyRow;
            if (r2 == r1) goto L_0x00ad;
        L_0x00ab:
            r1 = 1;
            goto L_0x00ae;
        L_0x00ad:
            r1 = 0;
        L_0x00ae:
            r11.setText(r12, r0, r1);
            goto L_0x00f4;
        L_0x00b2:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.nobodyRow;
            if (r12 != r0) goto L_0x00f3;
        L_0x00ba:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r2) goto L_0x00da;
        L_0x00c2:
            r12 = 2131560141; // 0x7f0d06cd float:1.8745646E38 double:1.0531306377E-314;
            r0 = "P2PNobody";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.lastCheckedType;
            if (r0 != r4) goto L_0x00d5;
        L_0x00d3:
            r0 = 1;
            goto L_0x00d6;
        L_0x00d5:
            r0 = 0;
        L_0x00d6:
            r11.setText(r12, r0, r3);
            goto L_0x00f1;
        L_0x00da:
            r12 = 2131559739; // 0x7f0d053b float:1.874483E38 double:1.053130439E-314;
            r0 = "LastSeenNobody";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.lastCheckedType;
            if (r0 != r4) goto L_0x00ed;
        L_0x00eb:
            r0 = 1;
            goto L_0x00ee;
        L_0x00ed:
            r0 = 0;
        L_0x00ee:
            r11.setText(r12, r0, r3);
        L_0x00f1:
            r8 = 1;
            goto L_0x00f4;
        L_0x00f3:
            r8 = 0;
        L_0x00f4:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.lastCheckedType;
            if (r12 != r8) goto L_0x0107;
        L_0x00fc:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.enableAnimation;
            r11.setChecked(r3, r12);
            goto L_0x043b;
        L_0x0107:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.currentType;
            if (r12 != r8) goto L_0x043b;
        L_0x010f:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.enableAnimation;
            r11.setChecked(r4, r12);
            goto L_0x043b;
        L_0x011a:
            r11 = r11.itemView;
            r11 = (org.telegram.p004ui.Cells.HeaderCell) r11;
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.sectionRow;
            if (r12 != r0) goto L_0x01b8;
        L_0x0126:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r7) goto L_0x013c;
        L_0x012e:
            r12 = 2131560501; // 0x7f0d0835 float:1.8746376E38 double:1.0531308156E-314;
            r0 = "PrivacyPhoneTitle";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x013c:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r6) goto L_0x0152;
        L_0x0144:
            r12 = 2131560491; // 0x7f0d082b float:1.8746356E38 double:1.0531308106E-314;
            r0 = "PrivacyForwardsTitle";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x0152:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r5) goto L_0x0168;
        L_0x015a:
            r12 = 2131560508; // 0x7f0d083c float:1.874639E38 double:1.053130819E-314;
            r0 = "PrivacyProfilePhotoTitle";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x0168:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r2) goto L_0x017e;
        L_0x0170:
            r12 = 2131560138; // 0x7f0d06ca float:1.874564E38 double:1.053130636E-314;
            r0 = "P2PEnabledWith";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x017e:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r8) goto L_0x0194;
        L_0x0186:
            r12 = 2131561120; // 0x7f0d0aa0 float:1.8747632E38 double:1.0531311214E-314;
            r0 = "WhoCanCallMe";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x0194:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r4) goto L_0x01aa;
        L_0x019c:
            r12 = 2131561115; // 0x7f0d0a9b float:1.8747621E38 double:1.053131119E-314;
            r0 = "WhoCanAddMe";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x01aa:
            r12 = 2131559741; // 0x7f0d053d float:1.8744835E38 double:1.05313044E-314;
            r0 = "LastSeenTitle";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x01b8:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.shareSectionRow;
            if (r12 != r0) goto L_0x01ce;
        L_0x01c0:
            r12 = 2131558570; // 0x7f0d00aa float:1.874246E38 double:1.0531298615E-314;
            r0 = "AddExceptions";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x01ce:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.p2pSectionRow;
            if (r12 != r0) goto L_0x043b;
        L_0x01d6:
            r12 = 2131560495; // 0x7f0d082f float:1.8746364E38 double:1.0531308126E-314;
            r0 = "PrivacyP2PHeader";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x043b;
        L_0x01e4:
            r11 = r11.itemView;
            r11 = (org.telegram.p004ui.Cells.TextInfoPrivacyCell) r11;
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.detailRow;
            r1 = 2131165394; // 0x7f0700d2 float:1.7945004E38 double:1.052935607E-314;
            r3 = "windowBackgroundGrayShadow";
            if (r12 != r0) goto L_0x028b;
        L_0x01f5:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r7) goto L_0x020b;
        L_0x01fd:
            r12 = 2131560499; // 0x7f0d0833 float:1.8746372E38 double:1.0531308146E-314;
            r0 = "PrivacyPhoneInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0280;
        L_0x020b:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r6) goto L_0x0220;
        L_0x0213:
            r12 = 2131560487; // 0x7f0d0827 float:1.8746348E38 double:1.0531308087E-314;
            r0 = "PrivacyForwardsInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0280;
        L_0x0220:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r5) goto L_0x0235;
        L_0x0228:
            r12 = 2131560506; // 0x7f0d083a float:1.8746386E38 double:1.053130818E-314;
            r0 = "PrivacyProfilePhotoInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0280;
        L_0x0235:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r2) goto L_0x024a;
        L_0x023d:
            r12 = 2131560478; // 0x7f0d081e float:1.874633E38 double:1.053130804E-314;
            r0 = "PrivacyCallsP2PHelp";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0280;
        L_0x024a:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r8) goto L_0x025f;
        L_0x0252:
            r12 = 2131561121; // 0x7f0d0aa1 float:1.8747634E38 double:1.053131122E-314;
            r0 = "WhoCanCallMeInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0280;
        L_0x025f:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r4) goto L_0x0274;
        L_0x0267:
            r12 = 2131561116; // 0x7f0d0a9c float:1.8747623E38 double:1.0531311194E-314;
            r0 = "WhoCanAddMeInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0280;
        L_0x0274:
            r12 = 2131559187; // 0x7f0d0313 float:1.874371E38 double:1.0531301664E-314;
            r0 = "CustomHelp";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
        L_0x0280:
            r12 = r10.mContext;
            r12 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r12, r1, r3);
            r11.setBackgroundDrawable(r12);
            goto L_0x043b;
        L_0x028b:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.shareDetailRow;
            r9 = 2131165395; // 0x7f0700d3 float:1.7945006E38 double:1.0529356073E-314;
            if (r12 != r0) goto L_0x033f;
        L_0x0296:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r7) goto L_0x02ac;
        L_0x029e:
            r12 = 2131560500; // 0x7f0d0834 float:1.8746374E38 double:1.053130815E-314;
            r0 = "PrivacyPhoneInfo2";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0321;
        L_0x02ac:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r6) goto L_0x02c1;
        L_0x02b4:
            r12 = 2131560488; // 0x7f0d0828 float:1.874635E38 double:1.053130809E-314;
            r0 = "PrivacyForwardsInfo2";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0321;
        L_0x02c1:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r5) goto L_0x02d6;
        L_0x02c9:
            r12 = 2131560507; // 0x7f0d083b float:1.8746388E38 double:1.0531308185E-314;
            r0 = "PrivacyProfilePhotoInfo2";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0321;
        L_0x02d6:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r2) goto L_0x02eb;
        L_0x02de:
            r12 = 2131559189; // 0x7f0d0315 float:1.8743715E38 double:1.0531301674E-314;
            r0 = "CustomP2PInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0321;
        L_0x02eb:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r8) goto L_0x0300;
        L_0x02f3:
            r12 = 2131559186; // 0x7f0d0312 float:1.8743709E38 double:1.053130166E-314;
            r0 = "CustomCallInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0321;
        L_0x0300:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r4) goto L_0x0315;
        L_0x0308:
            r12 = 2131559190; // 0x7f0d0316 float:1.8743717E38 double:1.053130168E-314;
            r0 = "CustomShareInfo";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
            goto L_0x0321;
        L_0x0315:
            r12 = 2131559191; // 0x7f0d0317 float:1.874372E38 double:1.0531301684E-314;
            r0 = "CustomShareSettingsHelp";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            r11.setText(r12);
        L_0x0321:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.rulesType;
            if (r12 != r8) goto L_0x0334;
        L_0x0329:
            r12 = r10.mContext;
            r12 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r12, r1, r3);
            r11.setBackgroundDrawable(r12);
            goto L_0x043b;
        L_0x0334:
            r12 = r10.mContext;
            r12 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r12, r9, r3);
            r11.setBackgroundDrawable(r12);
            goto L_0x043b;
        L_0x033f:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.p2pDetailRow;
            if (r12 != r0) goto L_0x043b;
        L_0x0347:
            r12 = r10.mContext;
            r12 = org.telegram.p004ui.ActionBar.Theme.getThemedDrawable(r12, r9, r3);
            r11.setBackgroundDrawable(r12);
            goto L_0x043b;
        L_0x0352:
            r11 = r11.itemView;
            r11 = (org.telegram.p004ui.Cells.TextSettingsCell) r11;
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.alwaysShareRow;
            r5 = "Users";
            r6 = 2131559345; // 0x7f0d03b1 float:1.8744031E38 double:1.0531302444E-314;
            r7 = "EmpryUsersPlaceholder";
            if (r12 != r0) goto L_0x03ba;
        L_0x0365:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.currentPlus;
            r12 = r12.size();
            if (r12 == 0) goto L_0x0380;
        L_0x0371:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.currentPlus;
            r12 = r10.getUsersCount(r12);
            r12 = org.telegram.messenger.LocaleController.formatPluralString(r5, r12);
            goto L_0x0384;
        L_0x0380:
            r12 = org.telegram.messenger.LocaleController.getString(r7, r6);
        L_0x0384:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.rulesType;
            if (r0 == 0) goto L_0x03a3;
        L_0x038c:
            r0 = 2131558611; // 0x7f0d00d3 float:1.8742543E38 double:1.053129882E-314;
            r2 = "AlwaysAllow";
            r0 = org.telegram.messenger.LocaleController.getString(r2, r0);
            r2 = org.telegram.p004ui.PrivacyControlActivity.this;
            r2 = r2.neverShareRow;
            if (r2 == r1) goto L_0x039e;
        L_0x039d:
            r3 = 1;
        L_0x039e:
            r11.setTextAndValue(r0, r12, r3);
            goto L_0x043b;
        L_0x03a3:
            r0 = 2131558612; // 0x7f0d00d4 float:1.8742545E38 double:1.0531298823E-314;
            r2 = "AlwaysShareWith";
            r0 = org.telegram.messenger.LocaleController.getString(r2, r0);
            r2 = org.telegram.p004ui.PrivacyControlActivity.this;
            r2 = r2.neverShareRow;
            if (r2 == r1) goto L_0x03b5;
        L_0x03b4:
            r3 = 1;
        L_0x03b5:
            r11.setTextAndValue(r0, r12, r3);
            goto L_0x043b;
        L_0x03ba:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.neverShareRow;
            if (r12 != r0) goto L_0x0403;
        L_0x03c2:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.currentMinus;
            r12 = r12.size();
            if (r12 == 0) goto L_0x03dd;
        L_0x03ce:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.currentMinus;
            r12 = r10.getUsersCount(r12);
            r12 = org.telegram.messenger.LocaleController.formatPluralString(r5, r12);
            goto L_0x03e1;
        L_0x03dd:
            r12 = org.telegram.messenger.LocaleController.getString(r7, r6);
        L_0x03e1:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.rulesType;
            if (r0 == 0) goto L_0x03f6;
        L_0x03e9:
            r0 = 2131559894; // 0x7f0d05d6 float:1.8745145E38 double:1.0531305157E-314;
            r1 = "NeverAllow";
            r0 = org.telegram.messenger.LocaleController.getString(r1, r0);
            r11.setTextAndValue(r0, r12, r3);
            goto L_0x043b;
        L_0x03f6:
            r0 = 2131559895; // 0x7f0d05d7 float:1.8745147E38 double:1.053130516E-314;
            r1 = "NeverShareWith";
            r0 = org.telegram.messenger.LocaleController.getString(r1, r0);
            r11.setTextAndValue(r0, r12, r3);
            goto L_0x043b;
        L_0x0403:
            r0 = org.telegram.p004ui.PrivacyControlActivity.this;
            r0 = r0.p2pRow;
            if (r12 != r0) goto L_0x043b;
        L_0x040b:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.currentAccount;
            r12 = org.telegram.messenger.ContactsController.getInstance(r12);
            r12 = r12.getLoadingPrivicyInfo(r2);
            if (r12 == 0) goto L_0x0425;
        L_0x041b:
            r12 = 2131559768; // 0x7f0d0558 float:1.874489E38 double:1.0531304534E-314;
            r0 = "Loading";
            r12 = org.telegram.messenger.LocaleController.getString(r0, r12);
            goto L_0x042f;
        L_0x0425:
            r12 = org.telegram.p004ui.PrivacyControlActivity.this;
            r12 = r12.currentAccount;
            r12 = org.telegram.p004ui.PrivacySettingsActivity.formatRulesString(r12, r2);
        L_0x042f:
            r0 = 2131560494; // 0x7f0d082e float:1.8746362E38 double:1.053130812E-314;
            r1 = "PrivacyP2P2";
            r0 = org.telegram.messenger.LocaleController.getString(r1, r0);
            r11.setTextAndValue(r0, r12, r3);
        L_0x043b:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.PrivacyControlActivity$ListAdapter.onBindViewHolder(androidx.recyclerview.widget.RecyclerView$ViewHolder, int):void");
        }

        public int getItemViewType(int i) {
            if (i == PrivacyControlActivity.this.alwaysShareRow || i == PrivacyControlActivity.this.neverShareRow || i == PrivacyControlActivity.this.p2pRow) {
                return 0;
            }
            if (i == PrivacyControlActivity.this.shareDetailRow || i == PrivacyControlActivity.this.detailRow || i == PrivacyControlActivity.this.p2pDetailRow) {
                return 1;
            }
            if (i == PrivacyControlActivity.this.sectionRow || i == PrivacyControlActivity.this.shareSectionRow || i == PrivacyControlActivity.this.p2pSectionRow) {
                return 2;
            }
            if (i == PrivacyControlActivity.this.everybodyRow || i == PrivacyControlActivity.this.myContactsRow || i == PrivacyControlActivity.this.nobodyRow) {
                return 3;
            }
            if (i == PrivacyControlActivity.this.messageRow) {
                return 4;
            }
            return 0;
        }
    }

    public PrivacyControlActivity(int i) {
        this(i, false);
    }

    public PrivacyControlActivity(int i, boolean z) {
        this.initialPlus = new ArrayList();
        this.initialMinus = new ArrayList();
        this.lastCheckedType = -1;
        this.rulesType = i;
        if (z) {
            ContactsController.getInstance(this.currentAccount).loadPrivacySettings();
        }
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        checkPrivacy();
        updateRows();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.privacyRulesUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.privacyRulesUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
    }

    public View createView(Context context) {
        if (this.rulesType == 5) {
            this.messageCell = new MessageCell(context);
        }
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        int i = this.rulesType;
        if (i == 6) {
            this.actionBar.setTitle(LocaleController.getString("PrivacyPhone", C1067R.string.PrivacyPhone));
        } else if (i == 5) {
            this.actionBar.setTitle(LocaleController.getString("PrivacyForwards", C1067R.string.PrivacyForwards));
        } else if (i == 4) {
            this.actionBar.setTitle(LocaleController.getString("PrivacyProfilePhoto", C1067R.string.PrivacyProfilePhoto));
        } else if (i == 3) {
            this.actionBar.setTitle(LocaleController.getString("PrivacyP2P", C1067R.string.PrivacyP2P));
        } else if (i == 2) {
            this.actionBar.setTitle(LocaleController.getString("Calls", C1067R.string.Calls));
        } else if (i == 1) {
            this.actionBar.setTitle(LocaleController.getString("GroupsAndChannels", C1067R.string.GroupsAndChannels));
        } else {
            this.actionBar.setTitle(LocaleController.getString("PrivacyLastSeen", C1067R.string.PrivacyLastSeen));
        }
        this.actionBar.setActionBarMenuOnItemClick(new C43031());
        View view = this.doneButton;
        i = view != null ? view.getVisibility() : 8;
        this.doneButton = this.actionBar.createMenu().addItemWithWidth(1, C1067R.C1065drawable.ic_done, AndroidUtilities.m26dp(56.0f));
        this.doneButton.setVisibility(i);
        this.listAdapter = new ListAdapter(context);
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.listView = new RecyclerListView(context);
        this.listView.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3828-$$Lambda$PrivacyControlActivity$wxF_vl2Ux3ukEJDYev7VlVBgIRk(this));
        setMessageText();
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$2$PrivacyControlActivity(View view, int i) {
        int i2 = 0;
        if (i == this.nobodyRow || i == this.everybodyRow || i == this.myContactsRow) {
            int i3 = this.currentType;
            if (i == this.nobodyRow) {
                i3 = 1;
            } else if (i == this.everybodyRow) {
                i3 = 0;
            } else if (i == this.myContactsRow) {
                i3 = 2;
            }
            i = this.currentType;
            if (i3 != i) {
                this.enableAnimation = true;
                this.lastCheckedType = i;
                this.currentType = i3;
                view = this.doneButton;
                if (!hasChanges()) {
                    i2 = 8;
                }
                view.setVisibility(i2);
                updateRows();
            }
        } else if (i == this.neverShareRow || i == this.alwaysShareRow) {
            ArrayList arrayList;
            if (i == this.neverShareRow) {
                arrayList = this.currentMinus;
            } else {
                arrayList = this.currentPlus;
            }
            boolean z;
            if (arrayList.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(i == this.neverShareRow ? "isNeverShare" : "isAlwaysShare", true);
                if (this.rulesType != 0) {
                    z = true;
                }
                bundle.putBoolean("isGroup", z);
                GroupCreateActivity groupCreateActivity = new GroupCreateActivity(bundle);
                groupCreateActivity.setDelegate(new C3827-$$Lambda$PrivacyControlActivity$gyVCUgP7-dxVWuYDNVL7ikSQ-s4(this, i));
                presentFragment(groupCreateActivity);
            } else {
                boolean z2 = this.rulesType != 0;
                if (i == this.alwaysShareRow) {
                    z = true;
                }
                PrivacyUsersActivity privacyUsersActivity = new PrivacyUsersActivity(arrayList, z2, z);
                privacyUsersActivity.setDelegate(new C3826-$$Lambda$PrivacyControlActivity$Optb0rMT99Nhw1X8nsWqhFLsnHs(this, i));
                presentFragment(privacyUsersActivity);
            }
        } else if (i == this.p2pRow) {
            presentFragment(new PrivacyControlActivity(3));
        }
    }

    public /* synthetic */ void lambda$null$0$PrivacyControlActivity(int i, ArrayList arrayList) {
        int i2 = 0;
        if (i == this.neverShareRow) {
            this.currentMinus = arrayList;
            for (i = 0; i < this.currentMinus.size(); i++) {
                this.currentPlus.remove(this.currentMinus.get(i));
            }
        } else {
            this.currentPlus = arrayList;
            for (i = 0; i < this.currentPlus.size(); i++) {
                this.currentMinus.remove(this.currentPlus.get(i));
            }
        }
        this.lastCheckedType = -1;
        View view = this.doneButton;
        if (!hasChanges()) {
            i2 = 8;
        }
        view.setVisibility(i2);
        this.listAdapter.notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$null$1$PrivacyControlActivity(int i, ArrayList arrayList, boolean z) {
        int i2 = 0;
        if (i == this.neverShareRow) {
            this.currentMinus = arrayList;
            if (z) {
                for (i = 0; i < this.currentMinus.size(); i++) {
                    this.currentPlus.remove(this.currentMinus.get(i));
                }
            }
        } else {
            this.currentPlus = arrayList;
            if (z) {
                for (i = 0; i < this.currentPlus.size(); i++) {
                    this.currentMinus.remove(this.currentPlus.get(i));
                }
            }
        }
        View view = this.doneButton;
        if (!hasChanges()) {
            i2 = 8;
        }
        view.setVisibility(i2);
        this.listAdapter.notifyDataSetChanged();
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.privacyRulesUpdated) {
            checkPrivacy();
        } else if (i == NotificationCenter.emojiDidLoad) {
            this.listView.invalidateViews();
        }
    }

    private void applyCurrentPrivacySettings() {
        int i;
        int intValue;
        User user;
        InputUser inputUser;
        TL_account_setPrivacy tL_account_setPrivacy = new TL_account_setPrivacy();
        int i2 = this.rulesType;
        if (i2 == 6) {
            tL_account_setPrivacy.key = new TL_inputPrivacyKeyPhoneNumber();
        } else if (i2 == 5) {
            tL_account_setPrivacy.key = new TL_inputPrivacyKeyForwards();
        } else if (i2 == 4) {
            tL_account_setPrivacy.key = new TL_inputPrivacyKeyProfilePhoto();
        } else if (i2 == 3) {
            tL_account_setPrivacy.key = new TL_inputPrivacyKeyPhoneP2P();
        } else if (i2 == 2) {
            tL_account_setPrivacy.key = new TL_inputPrivacyKeyPhoneCall();
        } else if (i2 == 1) {
            tL_account_setPrivacy.key = new TL_inputPrivacyKeyChatInvite();
        } else {
            tL_account_setPrivacy.key = new TL_inputPrivacyKeyStatusTimestamp();
        }
        if (this.currentType != 0 && this.currentPlus.size() > 0) {
            TL_inputPrivacyValueAllowUsers tL_inputPrivacyValueAllowUsers = new TL_inputPrivacyValueAllowUsers();
            TL_inputPrivacyValueAllowChatParticipants tL_inputPrivacyValueAllowChatParticipants = new TL_inputPrivacyValueAllowChatParticipants();
            for (i = 0; i < this.currentPlus.size(); i++) {
                intValue = ((Integer) this.currentPlus.get(i)).intValue();
                if (intValue > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(intValue));
                    if (user != null) {
                        inputUser = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                        if (inputUser != null) {
                            tL_inputPrivacyValueAllowUsers.users.add(inputUser);
                        }
                    }
                } else {
                    tL_inputPrivacyValueAllowChatParticipants.chats.add(Integer.valueOf(-intValue));
                }
            }
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueAllowUsers);
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueAllowChatParticipants);
        }
        if (this.currentType != 1 && this.currentMinus.size() > 0) {
            TL_inputPrivacyValueDisallowUsers tL_inputPrivacyValueDisallowUsers = new TL_inputPrivacyValueDisallowUsers();
            TL_inputPrivacyValueDisallowChatParticipants tL_inputPrivacyValueDisallowChatParticipants = new TL_inputPrivacyValueDisallowChatParticipants();
            for (i = 0; i < this.currentMinus.size(); i++) {
                intValue = ((Integer) this.currentMinus.get(i)).intValue();
                if (intValue > 0) {
                    user = getMessagesController().getUser(Integer.valueOf(intValue));
                    if (user != null) {
                        inputUser = getMessagesController().getInputUser(user);
                        if (inputUser != null) {
                            tL_inputPrivacyValueDisallowUsers.users.add(inputUser);
                        }
                    }
                } else {
                    tL_inputPrivacyValueDisallowChatParticipants.chats.add(Integer.valueOf(-intValue));
                }
            }
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueDisallowUsers);
            tL_account_setPrivacy.rules.add(tL_inputPrivacyValueDisallowChatParticipants);
        }
        i2 = this.currentType;
        if (i2 == 0) {
            tL_account_setPrivacy.rules.add(new TL_inputPrivacyValueAllowAll());
        } else if (i2 == 1) {
            tL_account_setPrivacy.rules.add(new TL_inputPrivacyValueDisallowAll());
        } else if (i2 == 2) {
            tL_account_setPrivacy.rules.add(new TL_inputPrivacyValueAllowContacts());
        }
        AlertDialog alertDialog = null;
        if (getParentActivity() != null) {
            alertDialog = new AlertDialog(getParentActivity(), 3);
            alertDialog.setCanCacnel(false);
            alertDialog.show();
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_setPrivacy, new C3825-$$Lambda$PrivacyControlActivity$8u1Pr-pdaGnQbppD7jDw_8yxFb4(this, alertDialog), 2);
    }

    public /* synthetic */ void lambda$applyCurrentPrivacySettings$4$PrivacyControlActivity(AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1916-$$Lambda$PrivacyControlActivity$wCT7rGu451A16C1KVTM9KhVzZrc(this, alertDialog, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$3$PrivacyControlActivity(AlertDialog alertDialog, TL_error tL_error, TLObject tLObject) {
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
        }
        if (tL_error == null) {
            TL_account_privacyRules tL_account_privacyRules = (TL_account_privacyRules) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tL_account_privacyRules.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(tL_account_privacyRules.chats, false);
            ContactsController.getInstance(this.currentAccount).setPrivacyRules(tL_account_privacyRules.rules, this.rulesType);
            finishFragment();
            return;
        }
        showErrorAlert();
    }

    private void showErrorAlert() {
        if (getParentActivity() != null) {
            Builder builder = new Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            builder.setMessage(LocaleController.getString("PrivacyFloodControlError", C1067R.string.PrivacyFloodControlError));
            builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
            showDialog(builder.create());
        }
    }

    private void checkPrivacy() {
        this.currentPlus = new ArrayList();
        this.currentMinus = new ArrayList();
        ArrayList privacyRules = ContactsController.getInstance(this.currentAccount).getPrivacyRules(this.rulesType);
        if (privacyRules == null || privacyRules.size() == 0) {
            this.currentType = 1;
        } else {
            Object obj = -1;
            for (int i = 0; i < privacyRules.size(); i++) {
                PrivacyRule privacyRule = (PrivacyRule) privacyRules.get(i);
                int size;
                int i2;
                if (privacyRule instanceof TL_privacyValueAllowChatParticipants) {
                    TL_privacyValueAllowChatParticipants tL_privacyValueAllowChatParticipants = (TL_privacyValueAllowChatParticipants) privacyRule;
                    size = tL_privacyValueAllowChatParticipants.chats.size();
                    for (i2 = 0; i2 < size; i2++) {
                        this.currentPlus.add(Integer.valueOf(-((Integer) tL_privacyValueAllowChatParticipants.chats.get(i2)).intValue()));
                    }
                } else if (privacyRule instanceof TL_privacyValueDisallowChatParticipants) {
                    TL_privacyValueDisallowChatParticipants tL_privacyValueDisallowChatParticipants = (TL_privacyValueDisallowChatParticipants) privacyRule;
                    size = tL_privacyValueDisallowChatParticipants.chats.size();
                    for (i2 = 0; i2 < size; i2++) {
                        this.currentMinus.add(Integer.valueOf(-((Integer) tL_privacyValueDisallowChatParticipants.chats.get(i2)).intValue()));
                    }
                } else if (privacyRule instanceof TL_privacyValueAllowUsers) {
                    this.currentPlus.addAll(((TL_privacyValueAllowUsers) privacyRule).users);
                } else if (privacyRule instanceof TL_privacyValueDisallowUsers) {
                    this.currentMinus.addAll(((TL_privacyValueDisallowUsers) privacyRule).users);
                } else if (obj == -1) {
                    obj = privacyRule instanceof TL_privacyValueAllowAll ? null : privacyRule instanceof TL_privacyValueDisallowAll ? 1 : 2;
                }
            }
            if (obj == null || (obj == -1 && this.currentMinus.size() > 0)) {
                this.currentType = 0;
            } else if (obj == 2 || (obj == -1 && this.currentMinus.size() > 0 && this.currentPlus.size() > 0)) {
                this.currentType = 2;
            } else if (obj == 1 || (obj == -1 && this.currentPlus.size() > 0)) {
                this.currentType = 1;
            }
            View view = this.doneButton;
            if (view != null) {
                view.setVisibility(8);
            }
        }
        this.initialPlus.clear();
        this.initialMinus.clear();
        this.initialRulesType = this.currentType;
        this.initialPlus.addAll(this.currentPlus);
        this.initialMinus.addAll(this.currentMinus);
        updateRows();
    }

    private boolean hasChanges() {
        if (this.initialRulesType != this.currentType || this.initialMinus.size() != this.currentMinus.size() || this.initialPlus.size() != this.currentPlus.size()) {
            return true;
        }
        Collections.sort(this.initialPlus);
        Collections.sort(this.currentPlus);
        if (!this.initialPlus.equals(this.currentPlus)) {
            return true;
        }
        Collections.sort(this.initialMinus);
        Collections.sort(this.currentMinus);
        if (this.initialMinus.equals(this.currentMinus)) {
            return false;
        }
        return true;
    }

    private void updateRows() {
        int i;
        this.rowCount = 0;
        if (this.rulesType == 5) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.messageRow = i;
        } else {
            this.messageRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.sectionRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.everybodyRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.myContactsRow = i;
        i = this.rulesType;
        if (i == 0 || i == 2 || i == 3 || i == 5 || i == 6) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.nobodyRow = i;
        } else {
            this.nobodyRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.detailRow = i;
        i = this.rowCount;
        this.rowCount = i + 1;
        this.shareSectionRow = i;
        i = this.currentType;
        if (i == 1 || i == 2) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.alwaysShareRow = i;
        } else {
            this.alwaysShareRow = -1;
        }
        i = this.currentType;
        if (i == 0 || i == 2) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.neverShareRow = i;
        } else {
            this.neverShareRow = -1;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.shareDetailRow = i;
        if (this.rulesType == 2) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.p2pSectionRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.p2pRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.p2pDetailRow = i;
        } else {
            this.p2pSectionRow = -1;
            this.p2pRow = -1;
            this.p2pDetailRow = -1;
        }
        setMessageText();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void setMessageText() {
        MessageCell messageCell = this.messageCell;
        if (messageCell != null) {
            int i = this.currentType;
            if (i == 0) {
                messageCell.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsEverybody", C1067R.string.PrivacyForwardsEverybody));
                this.messageCell.messageObject.messageOwner.fwd_from.from_id = 1;
            } else if (i == 1) {
                messageCell.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsNobody", C1067R.string.PrivacyForwardsNobody));
                this.messageCell.messageObject.messageOwner.fwd_from.from_id = 0;
            } else {
                messageCell.hintView.setOverrideText(LocaleController.getString("PrivacyForwardsContacts", C1067R.string.PrivacyForwardsContacts));
                this.messageCell.messageObject.messageOwner.fwd_from.from_id = 1;
            }
            this.messageCell.cell.forceResetMessageObject();
        }
    }

    public void onResume() {
        super.onResume();
        this.lastCheckedType = -1;
        this.enableAnimation = false;
    }

    public boolean onBackPressed() {
        return checkDiscard();
    }

    private void processDone() {
        if (getParentActivity() != null) {
            if (this.currentType != 0 && this.rulesType == 0) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (!globalMainSettings.getBoolean("privacyAlertShowed", false)) {
                    Builder builder = new Builder(getParentActivity());
                    if (this.rulesType == 1) {
                        builder.setMessage(LocaleController.getString("WhoCanAddMeInfo", C1067R.string.WhoCanAddMeInfo));
                    } else {
                        builder.setMessage(LocaleController.getString("CustomHelp", C1067R.string.CustomHelp));
                    }
                    builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                    builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), new C1914-$$Lambda$PrivacyControlActivity$IKfTFdmuEI1xlx3GBeNMY8JX7Aw(this, globalMainSettings));
                    builder.setNegativeButton(LocaleController.getString("Cancel", C1067R.string.Cancel), null);
                    showDialog(builder.create());
                    return;
                }
            }
            applyCurrentPrivacySettings();
        }
    }

    public /* synthetic */ void lambda$processDone$5$PrivacyControlActivity(SharedPreferences sharedPreferences, DialogInterface dialogInterface, int i) {
        applyCurrentPrivacySettings();
        sharedPreferences.edit().putBoolean("privacyAlertShowed", true).commit();
    }

    private boolean checkDiscard() {
        if (this.doneButton.getVisibility() != 0) {
            return true;
        }
        Builder builder = new Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("UserRestrictionsApplyChanges", C1067R.string.UserRestrictionsApplyChanges));
        builder.setMessage(LocaleController.getString("PrivacySettingsChangedAlert", C1067R.string.PrivacySettingsChangedAlert));
        builder.setPositiveButton(LocaleController.getString("ApplyTheme", C1067R.string.ApplyTheme), new C1913-$$Lambda$PrivacyControlActivity$5im2Vf3cKm0s4wzjCS8m-xwDFXs(this));
        builder.setNegativeButton(LocaleController.getString("PassportDiscard", C1067R.string.PassportDiscard), new C1915-$$Lambda$PrivacyControlActivity$jMiVlh9zo_Od8OHpA_uuLslJQzk(this));
        showDialog(builder.create());
        return false;
    }

    public /* synthetic */ void lambda$checkDiscard$6$PrivacyControlActivity(DialogInterface dialogInterface, int i) {
        processDone();
    }

    public /* synthetic */ void lambda$checkDiscard$7$PrivacyControlActivity(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    public boolean canBeginSlide() {
        return checkDiscard();
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[40];
        themeDescriptionArr[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, RadioCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        themeDescriptionArr[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        themeDescriptionArr[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        themeDescriptionArr[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        themeDescriptionArr[9] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        View view = this.listView;
        Class[] clsArr = new Class[]{TextSettingsCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        themeDescriptionArr[10] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[11] = new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        themeDescriptionArr[12] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        themeDescriptionArr[13] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        themeDescriptionArr[14] = new ThemeDescription(this.listView, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        int i = ThemeDescription.FLAG_CHECKBOX;
        clsArr = new Class[]{RadioCell.class};
        strArr = new String[1];
        strArr[0] = "radioButton";
        themeDescriptionArr[15] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_radioBackground);
        view = this.listView;
        View view2 = view;
        themeDescriptionArr[16] = new ThemeDescription(view2, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, null, null, null, Theme.key_radioBackgroundChecked);
        themeDescriptionArr[17] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubble);
        themeDescriptionArr[18] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, Theme.key_chat_inBubbleSelected);
        themeDescriptionArr[19] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable}, null, Theme.key_chat_inBubbleShadow);
        themeDescriptionArr[20] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubble);
        themeDescriptionArr[21] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, Theme.key_chat_outBubbleSelected);
        themeDescriptionArr[22] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable}, null, Theme.key_chat_outBubbleShadow);
        themeDescriptionArr[23] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_messageTextIn);
        themeDescriptionArr[24] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_messageTextOut);
        themeDescriptionArr[25] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, Theme.key_chat_outSentCheck);
        themeDescriptionArr[26] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckSelected);
        themeDescriptionArr[27] = new ThemeDescription(this.listView, 0, null, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, Theme.key_chat_mediaSentCheck);
        themeDescriptionArr[28] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyLine);
        themeDescriptionArr[29] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyLine);
        themeDescriptionArr[30] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyNameText);
        themeDescriptionArr[31] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyNameText);
        themeDescriptionArr[32] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyMessageText);
        themeDescriptionArr[33] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyMessageText);
        themeDescriptionArr[34] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inReplyMediaMessageSelectedText);
        themeDescriptionArr[35] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outReplyMediaMessageSelectedText);
        themeDescriptionArr[36] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inTimeText);
        themeDescriptionArr[37] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outTimeText);
        themeDescriptionArr[38] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_inTimeSelectedText);
        themeDescriptionArr[39] = new ThemeDescription(this.listView, 0, null, null, null, null, Theme.key_chat_outTimeSelectedText);
        return themeDescriptionArr;
    }
}