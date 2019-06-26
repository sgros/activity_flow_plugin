package org.telegram.p004ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import com.google.android.exoplayer2.util.NalUnitUtil;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.p004ui.ActionBar.ActionBarMenu;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.BottomSheet.Builder;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.RadioButtonCell;
import org.telegram.p004ui.Cells.ShadowSectionCell;
import org.telegram.p004ui.Cells.TextCell;
import org.telegram.p004ui.Cells.TextCheckCell;
import org.telegram.p004ui.Cells.TextDetailCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.AvatarDrawable;
import org.telegram.p004ui.Components.BackupImageView;
import org.telegram.p004ui.Components.EditTextBoldCursor;
import org.telegram.p004ui.Components.EditTextEmoji;
import org.telegram.p004ui.Components.ImageUpdater;
import org.telegram.p004ui.Components.ImageUpdater.ImageUpdaterDelegate;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RadialProgressView;
import org.telegram.p004ui.Components.SizeNotifierFrameLayout;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.ChatPhoto;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_chatBannedRights;
import org.telegram.tgnet.TLRPC.TL_chatParticipantAdmin;
import org.telegram.tgnet.TLRPC.TL_chatParticipantCreator;

/* renamed from: org.telegram.ui.ChatEditActivity */
public class ChatEditActivity extends BaseFragment implements ImageUpdaterDelegate, NotificationCenterDelegate {
    private static final int done_button = 1;
    private TextCell adminCell;
    private FileLocation avatar;
    private AnimatorSet avatarAnimation;
    private FileLocation avatarBig;
    private LinearLayout avatarContainer;
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private ImageView avatarEditor;
    private BackupImageView avatarImage;
    private View avatarOverlay;
    private RadialProgressView avatarProgressView;
    private TextCell blockCell;
    private int chatId;
    private boolean createAfterUpload;
    private Chat currentChat;
    private TextSettingsCell deleteCell;
    private FrameLayout deleteContainer;
    private ShadowSectionCell deleteInfoCell;
    private EditTextBoldCursor descriptionTextView;
    private View doneButton;
    private boolean donePressed;
    private TextDetailCell historyCell;
    private boolean historyHidden;
    private ImageUpdater imageUpdater = new ImageUpdater();
    private ChatFull info;
    private LinearLayout infoContainer;
    private ShadowSectionCell infoSectionCell;
    private boolean isChannel;
    private TextDetailCell linkedCell;
    private TextCell logCell;
    private TextCell membersCell;
    private EditTextEmoji nameTextView;
    private AlertDialog progressDialog;
    private LinearLayout settingsContainer;
    private ShadowSectionCell settingsSectionCell;
    private ShadowSectionCell settingsTopSectionCell;
    private TextCheckCell signCell;
    private boolean signMessages;
    private TextSettingsCell stickersCell;
    private FrameLayout stickersContainer;
    private TextInfoPrivacyCell stickersInfoCell3;
    private TextDetailCell typeCell;
    private LinearLayout typeEditContainer;
    private InputFile uploadedAvatar;

    /* renamed from: org.telegram.ui.ChatEditActivity$6 */
    class C24566 implements TextWatcher {
        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        C24566() {
        }
    }

    /* renamed from: org.telegram.ui.ChatEditActivity$1 */
    class C39931 extends ActionBarMenuOnItemClick {
        C39931() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                if (ChatEditActivity.this.checkDiscard()) {
                    ChatEditActivity.this.finishFragment();
                }
            } else if (i == 1) {
                ChatEditActivity.this.processDone();
            }
        }
    }

    public ChatEditActivity(Bundle bundle) {
        super(bundle);
        this.chatId = bundle.getInt("chat_id", 0);
    }

    /* JADX WARNING: Missing block: B:17:0x005f, code skipped:
            if (r5.info == null) goto L_0x0061;
     */
    public boolean onFragmentCreate() {
        /*
        r5 = this;
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r5.chatId;
        r1 = java.lang.Integer.valueOf(r1);
        r0 = r0.getChat(r1);
        r5.currentChat = r0;
        r0 = r5.currentChat;
        r1 = 1;
        r2 = 0;
        if (r0 != 0) goto L_0x0062;
    L_0x0018:
        r0 = new java.util.concurrent.CountDownLatch;
        r0.<init>(r1);
        r3 = r5.currentAccount;
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
        r3 = r3.getStorageQueue();
        r4 = new org.telegram.ui.-$$Lambda$ChatEditActivity$j-VWblaHSOc0ptEwu8DVX6LNsH0;
        r4.<init>(r5, r0);
        r3.postRunnable(r4);
        r0.await();	 Catch:{ Exception -> 0x0033 }
        goto L_0x0037;
    L_0x0033:
        r3 = move-exception;
        org.telegram.messenger.FileLog.m30e(r3);
    L_0x0037:
        r3 = r5.currentChat;
        if (r3 == 0) goto L_0x0061;
    L_0x003b:
        r3 = r5.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r4 = r5.currentChat;
        r3.putChat(r4, r1);
        r3 = r5.info;
        if (r3 != 0) goto L_0x0062;
    L_0x004a:
        r3 = r5.currentAccount;
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
        r4 = r5.chatId;
        r3.loadChatInfo(r4, r0, r2, r2);
        r0.await();	 Catch:{ Exception -> 0x0059 }
        goto L_0x005d;
    L_0x0059:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x005d:
        r0 = r5.info;
        if (r0 != 0) goto L_0x0062;
    L_0x0061:
        return r2;
    L_0x0062:
        r0 = r5.currentChat;
        r0 = org.telegram.messenger.ChatObject.isChannel(r0);
        if (r0 == 0) goto L_0x0071;
    L_0x006a:
        r0 = r5.currentChat;
        r0 = r0.megagroup;
        if (r0 != 0) goto L_0x0071;
    L_0x0070:
        goto L_0x0072;
    L_0x0071:
        r1 = 0;
    L_0x0072:
        r5.isChannel = r1;
        r0 = r5.imageUpdater;
        r0.parentFragment = r5;
        r0.delegate = r5;
        r0 = r5.currentChat;
        r0 = r0.signatures;
        r5.signMessages = r0;
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r0);
        r1 = org.telegram.messenger.NotificationCenter.chatInfoDidLoad;
        r0.addObserver(r5, r1);
        r0 = super.onFragmentCreate();
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ChatEditActivity.onFragmentCreate():boolean");
    }

    public /* synthetic */ void lambda$onFragmentCreate$0$ChatEditActivity(CountDownLatch countDownLatch) {
        this.currentChat = MessagesStorage.getInstance(this.currentAccount).getChat(this.chatId);
        countDownLatch.countDown();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.clear();
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
    }

    public void onResume() {
        super.onResume();
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onResume();
            this.nameTextView.getEditText().requestFocus();
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        updateFields(true);
    }

    public void onPause() {
        super.onPause();
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onPause();
        }
    }

    public boolean onBackPressed() {
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji == null || !editTextEmoji.isPopupShowing()) {
            return checkDiscard();
        }
        this.nameTextView.hidePopup(true);
        return false;
    }

    public View createView(Context context) {
        ChatFull chatFull;
        int i;
        Context context2 = context;
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new C39931());
        C39942 c39942 = new SizeNotifierFrameLayout(context2) {
            private boolean ignoreLayout;

            /* Access modifiers changed, original: protected */
            public void onMeasure(int i, int i2) {
                int size = MeasureSpec.getSize(i);
                int size2 = MeasureSpec.getSize(i2);
                setMeasuredDimension(size, size2);
                size2 -= getPaddingTop();
                measureChildWithMargins(ChatEditActivity.this.actionBar, i, 0, i2, 0);
                int i3 = 0;
                if (getKeyboardHeight() > AndroidUtilities.m26dp(20.0f)) {
                    this.ignoreLayout = true;
                    ChatEditActivity.this.nameTextView.hideEmojiView();
                    this.ignoreLayout = false;
                }
                int childCount = getChildCount();
                while (i3 < childCount) {
                    View childAt = getChildAt(i3);
                    if (!(childAt == null || childAt.getVisibility() == 8 || childAt == ChatEditActivity.this.actionBar)) {
                        if (ChatEditActivity.this.nameTextView == null || !ChatEditActivity.this.nameTextView.isPopupView(childAt)) {
                            measureChildWithMargins(childAt, i, 0, i2, 0);
                        } else if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                            childAt.measure(MeasureSpec.makeMeasureSpec(size, 1073741824), MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
                        } else if (AndroidUtilities.isTablet()) {
                            childAt.measure(MeasureSpec.makeMeasureSpec(size, 1073741824), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.m26dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (size2 - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                        } else {
                            childAt.measure(MeasureSpec.makeMeasureSpec(size, 1073741824), MeasureSpec.makeMeasureSpec((size2 - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                        }
                    }
                    i3++;
                }
            }

            /* Access modifiers changed, original: protected */
            /* JADX WARNING: Removed duplicated region for block: B:41:0x00bc  */
            /* JADX WARNING: Removed duplicated region for block: B:40:0x00b3  */
            /* JADX WARNING: Removed duplicated region for block: B:32:0x008c  */
            /* JADX WARNING: Removed duplicated region for block: B:25:0x0072  */
            /* JADX WARNING: Removed duplicated region for block: B:40:0x00b3  */
            /* JADX WARNING: Removed duplicated region for block: B:41:0x00bc  */
            public void onLayout(boolean r10, int r11, int r12, int r13, int r14) {
                /*
                r9 = this;
                r10 = r9.getChildCount();
                r0 = r9.getKeyboardHeight();
                r1 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
                r1 = org.telegram.messenger.AndroidUtilities.m26dp(r1);
                r2 = 0;
                if (r0 > r1) goto L_0x0026;
            L_0x0011:
                r0 = org.telegram.messenger.AndroidUtilities.isInMultiwindow;
                if (r0 != 0) goto L_0x0026;
            L_0x0015:
                r0 = org.telegram.messenger.AndroidUtilities.isTablet();
                if (r0 != 0) goto L_0x0026;
            L_0x001b:
                r0 = org.telegram.p004ui.ChatEditActivity.this;
                r0 = r0.nameTextView;
                r0 = r0.getEmojiPadding();
                goto L_0x0027;
            L_0x0026:
                r0 = 0;
            L_0x0027:
                r9.setBottomClip(r0);
            L_0x002a:
                if (r2 >= r10) goto L_0x00d3;
            L_0x002c:
                r1 = r9.getChildAt(r2);
                r3 = r1.getVisibility();
                r4 = 8;
                if (r3 != r4) goto L_0x003a;
            L_0x0038:
                goto L_0x00cf;
            L_0x003a:
                r3 = r1.getLayoutParams();
                r3 = (android.widget.FrameLayout.LayoutParams) r3;
                r4 = r1.getMeasuredWidth();
                r5 = r1.getMeasuredHeight();
                r6 = r3.gravity;
                r7 = -1;
                if (r6 != r7) goto L_0x004f;
            L_0x004d:
                r6 = 51;
            L_0x004f:
                r7 = r6 & 7;
                r6 = r6 & 112;
                r7 = r7 & 7;
                r8 = 1;
                if (r7 == r8) goto L_0x0063;
            L_0x0058:
                r8 = 5;
                if (r7 == r8) goto L_0x005e;
            L_0x005b:
                r7 = r3.leftMargin;
                goto L_0x006e;
            L_0x005e:
                r7 = r13 - r4;
                r8 = r3.rightMargin;
                goto L_0x006d;
            L_0x0063:
                r7 = r13 - r11;
                r7 = r7 - r4;
                r7 = r7 / 2;
                r8 = r3.leftMargin;
                r7 = r7 + r8;
                r8 = r3.rightMargin;
            L_0x006d:
                r7 = r7 - r8;
            L_0x006e:
                r8 = 16;
                if (r6 == r8) goto L_0x008c;
            L_0x0072:
                r8 = 48;
                if (r6 == r8) goto L_0x0084;
            L_0x0076:
                r8 = 80;
                if (r6 == r8) goto L_0x007d;
            L_0x007a:
                r3 = r3.topMargin;
                goto L_0x0099;
            L_0x007d:
                r6 = r14 - r0;
                r6 = r6 - r12;
                r6 = r6 - r5;
                r3 = r3.bottomMargin;
                goto L_0x0097;
            L_0x0084:
                r3 = r3.topMargin;
                r6 = r9.getPaddingTop();
                r3 = r3 + r6;
                goto L_0x0099;
            L_0x008c:
                r6 = r14 - r0;
                r6 = r6 - r12;
                r6 = r6 - r5;
                r6 = r6 / 2;
                r8 = r3.topMargin;
                r6 = r6 + r8;
                r3 = r3.bottomMargin;
            L_0x0097:
                r3 = r6 - r3;
            L_0x0099:
                r6 = org.telegram.p004ui.ChatEditActivity.this;
                r6 = r6.nameTextView;
                if (r6 == 0) goto L_0x00ca;
            L_0x00a1:
                r6 = org.telegram.p004ui.ChatEditActivity.this;
                r6 = r6.nameTextView;
                r6 = r6.isPopupView(r1);
                if (r6 == 0) goto L_0x00ca;
            L_0x00ad:
                r3 = org.telegram.messenger.AndroidUtilities.isTablet();
                if (r3 == 0) goto L_0x00bc;
            L_0x00b3:
                r3 = r9.getMeasuredHeight();
                r6 = r1.getMeasuredHeight();
                goto L_0x00c9;
            L_0x00bc:
                r3 = r9.getMeasuredHeight();
                r6 = r9.getKeyboardHeight();
                r3 = r3 + r6;
                r6 = r1.getMeasuredHeight();
            L_0x00c9:
                r3 = r3 - r6;
            L_0x00ca:
                r4 = r4 + r7;
                r5 = r5 + r3;
                r1.layout(r7, r3, r4, r5);
            L_0x00cf:
                r2 = r2 + 1;
                goto L_0x002a;
            L_0x00d3:
                r9.notifyHeightChanged();
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ChatEditActivity$C39942.onLayout(boolean, int, int, int, int):void");
            }

            public void requestLayout() {
                if (!this.ignoreLayout) {
                    super.requestLayout();
                }
            }
        };
        c39942.setOnTouchListener(C1397-$$Lambda$ChatEditActivity$VwiI9D4ZnAE2nkj3zFy5AkednDE.INSTANCE);
        this.fragmentView = c39942;
        this.fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        ScrollView scrollView = new ScrollView(context2);
        scrollView.setFillViewport(true);
        c39942.addView(scrollView, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout = new LinearLayout(context2);
        scrollView.addView(linearLayout, new LayoutParams(-1, -2));
        linearLayout.setOrientation(1);
        this.actionBar.setTitle(LocaleController.getString("ChannelEdit", C1067R.string.ChannelEdit));
        this.avatarContainer = new LinearLayout(context2);
        this.avatarContainer.setOrientation(1);
        LinearLayout linearLayout2 = this.avatarContainer;
        String str = Theme.key_windowBackgroundWhite;
        linearLayout2.setBackgroundColor(Theme.getColor(str));
        linearLayout.addView(this.avatarContainer, LayoutHelper.createLinear(-1, -2));
        FrameLayout frameLayout = new FrameLayout(context2);
        this.avatarContainer.addView(frameLayout, LayoutHelper.createLinear(-1, -2));
        this.avatarImage = new BackupImageView(context2) {
            public void invalidate() {
                if (ChatEditActivity.this.avatarOverlay != null) {
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate();
            }

            public void invalidate(int i, int i2, int i3, int i4) {
                if (ChatEditActivity.this.avatarOverlay != null) {
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
                super.invalidate(i, i2, i3, i4);
            }
        };
        this.avatarImage.setRoundRadius(AndroidUtilities.m26dp(32.0f));
        int i2 = 5;
        frameLayout.addView(this.avatarImage, LayoutHelper.createFrame(64, 64.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 16.0f, 12.0f, LocaleController.isRTL ? 16.0f : 0.0f, 12.0f));
        if (ChatObject.canChangeChatInfo(this.currentChat)) {
            this.avatarDrawable.setInfo(5, null, null, false);
            final Paint paint = new Paint(1);
            paint.setColor(1426063360);
            this.avatarOverlay = new View(context2) {
                /* Access modifiers changed, original: protected */
                public void onDraw(Canvas canvas) {
                    if (ChatEditActivity.this.avatarImage != null && ChatEditActivity.this.avatarImage.getImageReceiver().hasNotThumb()) {
                        paint.setAlpha((int) (ChatEditActivity.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f));
                        canvas.drawCircle((float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2), (float) AndroidUtilities.m26dp(32.0f), paint);
                    }
                }
            };
            frameLayout.addView(this.avatarOverlay, LayoutHelper.createFrame(64, 64.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 16.0f, 12.0f, LocaleController.isRTL ? 16.0f : 0.0f, 12.0f));
            this.avatarOverlay.setOnClickListener(new C1392-$$Lambda$ChatEditActivity$Hxmf_lPSvYp0l6WoXkJtGNwopNs(this));
            this.avatarOverlay.setContentDescription(LocaleController.getString("ChoosePhoto", C1067R.string.ChoosePhoto));
            this.avatarEditor = new ImageView(context2) {
                public void invalidate(int i, int i2, int i3, int i4) {
                    super.invalidate(i, i2, i3, i4);
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }

                public void invalidate() {
                    super.invalidate();
                    ChatEditActivity.this.avatarOverlay.invalidate();
                }
            };
            this.avatarEditor.setScaleType(ScaleType.CENTER);
            this.avatarEditor.setImageResource(C1067R.C1065drawable.menu_camera_av);
            this.avatarEditor.setEnabled(false);
            this.avatarEditor.setClickable(false);
            frameLayout.addView(this.avatarEditor, LayoutHelper.createFrame(64, 64.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 16.0f, 12.0f, LocaleController.isRTL ? 16.0f : 0.0f, 12.0f));
            this.avatarProgressView = new RadialProgressView(context2);
            this.avatarProgressView.setSize(AndroidUtilities.m26dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            frameLayout.addView(this.avatarProgressView, LayoutHelper.createFrame(64, 64.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 16.0f, 12.0f, LocaleController.isRTL ? 16.0f : 0.0f, 12.0f));
            showAvatarProgress(false, false);
        } else {
            this.avatarDrawable.setInfo(5, this.currentChat.title, null, false);
        }
        this.nameTextView = new EditTextEmoji(context2, c39942, this, 0);
        if (this.isChannel) {
            this.nameTextView.setHint(LocaleController.getString("EnterChannelName", C1067R.string.EnterChannelName));
        } else {
            this.nameTextView.setHint(LocaleController.getString("GroupName", C1067R.string.GroupName));
        }
        this.nameTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
        editTextEmoji = this.nameTextView;
        editTextEmoji.setFocusable(editTextEmoji.isEnabled());
        this.nameTextView.setFilters(new InputFilter[]{new LengthFilter(100)});
        frameLayout.addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0f, 16, LocaleController.isRTL ? 5.0f : 96.0f, 0.0f, LocaleController.isRTL ? 96.0f : 5.0f, 0.0f));
        this.settingsContainer = new LinearLayout(context2);
        this.settingsContainer.setOrientation(1);
        this.settingsContainer.setBackgroundColor(Theme.getColor(str));
        linearLayout.addView(this.settingsContainer, LayoutHelper.createLinear(-1, -2));
        this.descriptionTextView = new EditTextBoldCursor(context2);
        this.descriptionTextView.setTextSize(1, 16.0f);
        this.descriptionTextView.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        EditTextBoldCursor editTextBoldCursor = this.descriptionTextView;
        String str2 = Theme.key_windowBackgroundWhiteBlackText;
        editTextBoldCursor.setTextColor(Theme.getColor(str2));
        this.descriptionTextView.setPadding(0, 0, 0, AndroidUtilities.m26dp(6.0f));
        this.descriptionTextView.setBackgroundDrawable(null);
        editTextBoldCursor = this.descriptionTextView;
        if (!LocaleController.isRTL) {
            i2 = 3;
        }
        editTextBoldCursor.setGravity(i2);
        this.descriptionTextView.setInputType(180225);
        this.descriptionTextView.setImeOptions(6);
        this.descriptionTextView.setEnabled(ChatObject.canChangeChatInfo(this.currentChat));
        editTextBoldCursor = this.descriptionTextView;
        editTextBoldCursor.setFocusable(editTextBoldCursor.isEnabled());
        this.descriptionTextView.setFilters(new InputFilter[]{new LengthFilter(NalUnitUtil.EXTENDED_SAR)});
        this.descriptionTextView.setHint(LocaleController.getString("DescriptionOptionalPlaceholder", C1067R.string.DescriptionOptionalPlaceholder));
        this.descriptionTextView.setCursorColor(Theme.getColor(str2));
        this.descriptionTextView.setCursorSize(AndroidUtilities.m26dp(20.0f));
        this.descriptionTextView.setCursorWidth(1.5f);
        this.settingsContainer.addView(this.descriptionTextView, LayoutHelper.createLinear(-1, -2, 23.0f, 12.0f, 23.0f, 6.0f));
        this.descriptionTextView.setOnEditorActionListener(new C1403-$$Lambda$ChatEditActivity$p1fZRHy8NDyNuO213khwXU229Jc(this));
        this.descriptionTextView.addTextChangedListener(new C24566());
        this.settingsTopSectionCell = new ShadowSectionCell(context2);
        linearLayout.addView(this.settingsTopSectionCell, LayoutHelper.createLinear(-1, -2));
        this.typeEditContainer = new LinearLayout(context2);
        this.typeEditContainer.setOrientation(1);
        this.typeEditContainer.setBackgroundColor(Theme.getColor(str));
        linearLayout.addView(this.typeEditContainer, LayoutHelper.createLinear(-1, -2));
        if (this.currentChat.creator) {
            chatFull = this.info;
            if (chatFull == null || chatFull.can_set_username) {
                this.typeCell = new TextDetailCell(context2);
                this.typeCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                this.typeEditContainer.addView(this.typeCell, LayoutHelper.createLinear(-1, -2));
                this.typeCell.setOnClickListener(new C1405-$$Lambda$ChatEditActivity$takgJ7d_dj5vza0E_4qO74BhrTA(this));
            }
        }
        if (ChatObject.isChannel(this.currentChat) && ((this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 1)) || (!this.isChannel && ChatObject.canUserDoAdminAction(this.currentChat, 0)))) {
            this.linkedCell = new TextDetailCell(context2);
            this.linkedCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView(this.linkedCell, LayoutHelper.createLinear(-1, -2));
            this.linkedCell.setOnClickListener(new C1406-$$Lambda$ChatEditActivity$vs7xjVOaqM3gt8vxvzKAx_LFF8w(this));
        }
        if (!this.isChannel && ChatObject.canBlockUsers(this.currentChat) && (ChatObject.isChannel(this.currentChat) || this.currentChat.creator)) {
            this.historyCell = new TextDetailCell(context2);
            this.historyCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.typeEditContainer.addView(this.historyCell, LayoutHelper.createLinear(-1, -2));
            this.historyCell.setOnClickListener(new C1399-$$Lambda$ChatEditActivity$ZaLTd9UrDPsZkM9f0GkspKG3v50(this, context2));
        }
        if (this.isChannel) {
            this.signCell = new TextCheckCell(context2);
            this.signCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.signCell.setTextAndValueAndCheck(LocaleController.getString("ChannelSignMessages", C1067R.string.ChannelSignMessages), LocaleController.getString("ChannelSignMessagesInfo", C1067R.string.ChannelSignMessagesInfo), this.signMessages, true, false);
            this.typeEditContainer.addView(this.signCell, LayoutHelper.createFrame(-1, -2.0f));
            this.signCell.setOnClickListener(new C1402-$$Lambda$ChatEditActivity$m-aFUQRAeXShPmT_g_6jI0sXdes(this));
        }
        ActionBarMenu createMenu = this.actionBar.createMenu();
        if (!(!ChatObject.canChangeChatInfo(this.currentChat) && this.signCell == null && this.historyCell == null)) {
            this.doneButton = createMenu.addItemWithWidth(1, C1067R.C1065drawable.ic_done, AndroidUtilities.m26dp(56.0f));
            this.doneButton.setContentDescription(LocaleController.getString("Done", C1067R.string.Done));
        }
        if (!(this.signCell == null && this.historyCell == null && this.typeCell == null && this.linkedCell == null)) {
            this.settingsSectionCell = new ShadowSectionCell(context2);
            linearLayout.addView(this.settingsSectionCell, LayoutHelper.createLinear(-1, -2));
        }
        this.infoContainer = new LinearLayout(context2);
        this.infoContainer.setOrientation(1);
        this.infoContainer.setBackgroundColor(Theme.getColor(str));
        linearLayout.addView(this.infoContainer, LayoutHelper.createLinear(-1, -2));
        this.blockCell = new TextCell(context2);
        this.blockCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        TextCell textCell = this.blockCell;
        int i3 = (ChatObject.isChannel(this.currentChat) || this.currentChat.creator) ? 0 : 8;
        textCell.setVisibility(i3);
        this.blockCell.setOnClickListener(new C1398-$$Lambda$ChatEditActivity$Z_VSyPell-FXQ74xw_1QaAWQHLA(this));
        this.adminCell = new TextCell(context2);
        this.adminCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.adminCell.setOnClickListener(new C1390-$$Lambda$ChatEditActivity$BW8nfxB2gbGLRBoiaMPR3BQCdjM(this));
        this.membersCell = new TextCell(context2);
        this.membersCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
        this.membersCell.setOnClickListener(new C1396-$$Lambda$ChatEditActivity$R8nmnQgVpQwtOgAwCTLlWo2fY0k(this));
        if (ChatObject.isChannel(this.currentChat)) {
            this.logCell = new TextCell(context2);
            this.logCell.setTextAndIcon(LocaleController.getString("EventLog", C1067R.string.EventLog), C1067R.C1065drawable.group_log, false);
            this.logCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            this.logCell.setOnClickListener(new C1387-$$Lambda$ChatEditActivity$-_qhLtKqz7XJ52ia47Bo4pX1C7s(this));
        }
        if (this.isChannel) {
            i = -2;
        } else {
            i = -2;
            this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, -2));
        }
        this.infoContainer.addView(this.adminCell, LayoutHelper.createLinear(-1, i));
        this.infoContainer.addView(this.membersCell, LayoutHelper.createLinear(-1, i));
        if (this.isChannel) {
            this.infoContainer.addView(this.blockCell, LayoutHelper.createLinear(-1, i));
        }
        textCell = this.logCell;
        if (textCell != null) {
            this.infoContainer.addView(textCell, LayoutHelper.createLinear(-1, i));
        }
        this.infoSectionCell = new ShadowSectionCell(context2);
        linearLayout.addView(this.infoSectionCell, LayoutHelper.createLinear(-1, i));
        if (!ChatObject.hasAdminRights(this.currentChat)) {
            this.infoContainer.setVisibility(8);
            this.settingsTopSectionCell.setVisibility(8);
        }
        if (!this.isChannel) {
            chatFull = this.info;
            if (chatFull != null && chatFull.can_set_stickers) {
                this.stickersContainer = new FrameLayout(context2);
                this.stickersContainer.setBackgroundColor(Theme.getColor(str));
                linearLayout.addView(this.stickersContainer, LayoutHelper.createLinear(-1, -2));
                this.stickersCell = new TextSettingsCell(context2);
                this.stickersCell.setTextColor(Theme.getColor(str2));
                this.stickersCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                this.stickersContainer.addView(this.stickersCell, LayoutHelper.createFrame(-1, -2.0f));
                this.stickersCell.setOnClickListener(new C1389-$$Lambda$ChatEditActivity$51Cw78hSbx5h61mWEeKV8Wy14wg(this));
                this.stickersInfoCell3 = new TextInfoPrivacyCell(context2);
                this.stickersInfoCell3.setText(LocaleController.getString("GroupStickersInfo", C1067R.string.GroupStickersInfo));
                linearLayout.addView(this.stickersInfoCell3, LayoutHelper.createLinear(-1, -2));
            }
        }
        boolean z = this.currentChat.creator;
        str2 = Theme.key_windowBackgroundGrayShadow;
        if (z) {
            this.deleteContainer = new FrameLayout(context2);
            this.deleteContainer.setBackgroundColor(Theme.getColor(str));
            linearLayout.addView(this.deleteContainer, LayoutHelper.createLinear(-1, -2));
            this.deleteCell = new TextSettingsCell(context2);
            this.deleteCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText5));
            this.deleteCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (this.isChannel) {
                this.deleteCell.setText(LocaleController.getString("ChannelDelete", C1067R.string.ChannelDelete), false);
            } else if (this.currentChat.megagroup) {
                this.deleteCell.setText(LocaleController.getString("DeleteMega", C1067R.string.DeleteMega), false);
            } else {
                this.deleteCell.setText(LocaleController.getString("DeleteAndExitButton", C1067R.string.DeleteAndExitButton), false);
            }
            this.deleteContainer.addView(this.deleteCell, LayoutHelper.createFrame(-1, -2.0f));
            this.deleteCell.setOnClickListener(new C1404-$$Lambda$ChatEditActivity$qBqi8ghp1hDeyx8-eISTzlbn7qQ(this));
            this.deleteInfoCell = new ShadowSectionCell(context2);
            this.deleteInfoCell.setBackgroundDrawable(Theme.getThemedDrawable(context2, (int) C1067R.C1065drawable.greydivider_bottom, str2));
            linearLayout.addView(this.deleteInfoCell, LayoutHelper.createLinear(-1, -2));
        } else if (!this.isChannel && this.stickersInfoCell3 == null) {
            this.infoSectionCell.setBackgroundDrawable(Theme.getThemedDrawable(context2, (int) C1067R.C1065drawable.greydivider_bottom, str2));
        }
        TextInfoPrivacyCell textInfoPrivacyCell = this.stickersInfoCell3;
        if (textInfoPrivacyCell != null) {
            if (this.deleteInfoCell == null) {
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context2, (int) C1067R.C1065drawable.greydivider_bottom, str2));
            } else {
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(context2, (int) C1067R.C1065drawable.greydivider, str2));
            }
        }
        this.nameTextView.setText(this.currentChat.title);
        EditTextEmoji editTextEmoji2 = this.nameTextView;
        editTextEmoji2.setSelection(editTextEmoji2.length());
        ChatFull chatFull2 = this.info;
        if (chatFull2 != null) {
            this.descriptionTextView.setText(chatFull2.about);
        }
        Chat chat = this.currentChat;
        ChatPhoto chatPhoto = chat.photo;
        if (chatPhoto != null) {
            this.avatar = chatPhoto.photo_small;
            this.avatarBig = chatPhoto.photo_big;
            this.avatarImage.setImage(ImageLocation.getForChat(chat, false), "50_50", this.avatarDrawable, this.currentChat);
        } else {
            this.avatarImage.setImageDrawable(this.avatarDrawable);
        }
        updateFields(true);
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$3$ChatEditActivity(View view) {
        this.imageUpdater.openMenu(this.avatar != null, new C1395-$$Lambda$ChatEditActivity$PhelyuCPHfVAtJ2gMmY7rzmsEtA(this));
    }

    public /* synthetic */ void lambda$null$2$ChatEditActivity() {
        this.avatar = null;
        this.avatarBig = null;
        this.uploadedAvatar = null;
        showAvatarProgress(false, true);
        this.avatarImage.setImage(null, null, this.avatarDrawable, this.currentChat);
    }

    public /* synthetic */ boolean lambda$createView$4$ChatEditActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            View view = this.doneButton;
            if (view != null) {
                view.performClick();
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ void lambda$createView$5$ChatEditActivity(View view) {
        ChatEditTypeActivity chatEditTypeActivity = new ChatEditTypeActivity(this.chatId);
        chatEditTypeActivity.setInfo(this.info);
        presentFragment(chatEditTypeActivity);
    }

    public /* synthetic */ void lambda$createView$6$ChatEditActivity(View view) {
        ChatLinkActivity chatLinkActivity = new ChatLinkActivity(this.chatId);
        chatLinkActivity.setInfo(this.info);
        presentFragment(chatLinkActivity);
    }

    public /* synthetic */ void lambda$createView$8$ChatEditActivity(Context context, View view) {
        Builder builder = new Builder(context);
        builder.setApplyTopPadding(false);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        HeaderCell headerCell = new HeaderCell(context, true, 23, 15, false);
        headerCell.setHeight(47);
        headerCell.setText(LocaleController.getString("ChatHistory", C1067R.string.ChatHistory));
        linearLayout.addView(headerCell);
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2));
        RadioButtonCell[] radioButtonCellArr = new RadioButtonCell[2];
        for (int i = 0; i < 2; i++) {
            radioButtonCellArr[i] = new RadioButtonCell(context, true);
            radioButtonCellArr[i].setTag(Integer.valueOf(i));
            radioButtonCellArr[i].setBackgroundDrawable(Theme.getSelectorDrawable(false));
            if (i == 0) {
                radioButtonCellArr[i].setTextAndValue(LocaleController.getString("ChatHistoryVisible", C1067R.string.ChatHistoryVisible), LocaleController.getString("ChatHistoryVisibleInfo", C1067R.string.ChatHistoryVisibleInfo), true, this.historyHidden ^ 1);
            } else {
                String str = "ChatHistoryHidden";
                if (ChatObject.isChannel(this.currentChat)) {
                    radioButtonCellArr[i].setTextAndValue(LocaleController.getString(str, C1067R.string.ChatHistoryHidden), LocaleController.getString("ChatHistoryHiddenInfo", C1067R.string.ChatHistoryHiddenInfo), false, this.historyHidden);
                } else {
                    radioButtonCellArr[i].setTextAndValue(LocaleController.getString(str, C1067R.string.ChatHistoryHidden), LocaleController.getString("ChatHistoryHiddenInfo2", C1067R.string.ChatHistoryHiddenInfo2), false, this.historyHidden);
                }
            }
            linearLayout2.addView(radioButtonCellArr[i], LayoutHelper.createLinear(-1, -2));
            radioButtonCellArr[i].setOnClickListener(new C1393-$$Lambda$ChatEditActivity$Jz5JNMPNPO8bP_0CHPma3fBKfwA(this, radioButtonCellArr, builder));
        }
        builder.setCustomView(linearLayout);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$null$7$ChatEditActivity(RadioButtonCell[] radioButtonCellArr, Builder builder, View view) {
        Integer num = (Integer) view.getTag();
        boolean z = false;
        radioButtonCellArr[0].setChecked(num.intValue() == 0, true);
        radioButtonCellArr[1].setChecked(num.intValue() == 1, true);
        if (num.intValue() == 1) {
            z = true;
        }
        this.historyHidden = z;
        builder.getDismissRunnable().run();
        updateFields(true);
    }

    public /* synthetic */ void lambda$createView$9$ChatEditActivity(View view) {
        this.signMessages ^= 1;
        ((TextCheckCell) view).setChecked(this.signMessages);
    }

    public /* synthetic */ void lambda$createView$10$ChatEditActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("chat_id", this.chatId);
        bundle.putInt("type", !this.isChannel ? 3 : 0);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    public /* synthetic */ void lambda$createView$11$ChatEditActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("chat_id", this.chatId);
        bundle.putInt("type", 1);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    public /* synthetic */ void lambda$createView$12$ChatEditActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("chat_id", this.chatId);
        bundle.putInt("type", 2);
        ChatUsersActivity chatUsersActivity = new ChatUsersActivity(bundle);
        chatUsersActivity.setInfo(this.info);
        presentFragment(chatUsersActivity);
    }

    public /* synthetic */ void lambda$createView$13$ChatEditActivity(View view) {
        presentFragment(new ChannelAdminLogActivity(this.currentChat));
    }

    public /* synthetic */ void lambda$createView$14$ChatEditActivity(View view) {
        GroupStickersActivity groupStickersActivity = new GroupStickersActivity(this.currentChat.f434id);
        groupStickersActivity.setInfo(this.info);
        presentFragment(groupStickersActivity);
    }

    public /* synthetic */ void lambda$createView$16$ChatEditActivity(View view) {
        AlertsCreator.createClearOrDeleteDialogAlert(this, false, true, false, this.currentChat, null, false, new C3638-$$Lambda$ChatEditActivity$R2aORr4g6yH_oKe4WQtg_qOwYik(this));
    }

    public /* synthetic */ void lambda$null$15$ChatEditActivity(boolean z) {
        if (AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, Long.valueOf(-((long) this.chatId)));
        } else {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
        MessagesController.getInstance(this.currentAccount).deleteUserFromChat(this.chatId, MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId())), this.info, true, false);
        finishFragment();
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.chatInfoDidLoad) {
            ChatFull chatFull = (ChatFull) objArr[0];
            if (chatFull.f435id == this.chatId) {
                if (this.info == null) {
                    EditTextBoldCursor editTextBoldCursor = this.descriptionTextView;
                    if (editTextBoldCursor != null) {
                        editTextBoldCursor.setText(chatFull.about);
                    }
                }
                this.info = chatFull;
                boolean z = !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory;
                this.historyHidden = z;
                updateFields(false);
            }
        }
    }

    public void didUploadPhoto(InputFile inputFile, PhotoSize photoSize, PhotoSize photoSize2) {
        AndroidUtilities.runOnUIThread(new C1400-$$Lambda$ChatEditActivity$_yvKcw5WJa5Pk6zwjtXTtTJaTb0(this, inputFile, photoSize2, photoSize));
    }

    public /* synthetic */ void lambda$didUploadPhoto$17$ChatEditActivity(InputFile inputFile, PhotoSize photoSize, PhotoSize photoSize2) {
        if (inputFile != null) {
            this.uploadedAvatar = inputFile;
            if (this.createAfterUpload) {
                try {
                    if (this.progressDialog != null && this.progressDialog.isShowing()) {
                        this.progressDialog.dismiss();
                        this.progressDialog = null;
                    }
                } catch (Exception e) {
                    FileLog.m30e(e);
                }
                this.donePressed = false;
                this.doneButton.performClick();
            }
            showAvatarProgress(false, true);
            return;
        }
        this.avatar = photoSize.location;
        this.avatarBig = photoSize2.location;
        this.avatarImage.setImage(ImageLocation.getForLocal(this.avatar), "50_50", this.avatarDrawable, this.currentChat);
        showAvatarProgress(true, false);
    }

    public String getInitialSearchString() {
        return this.nameTextView.getText().toString();
    }

    /* JADX WARNING: Missing block: B:3:0x0006, code skipped:
            if (r0 != null) goto L_0x000b;
     */
    private boolean checkDiscard() {
        /*
        r3 = this;
        r0 = r3.info;
        if (r0 == 0) goto L_0x0009;
    L_0x0004:
        r0 = r0.about;
        if (r0 == 0) goto L_0x0009;
    L_0x0008:
        goto L_0x000b;
    L_0x0009:
        r0 = "";
    L_0x000b:
        r1 = r3.info;
        if (r1 == 0) goto L_0x001f;
    L_0x000f:
        r1 = r3.currentChat;
        r1 = org.telegram.messenger.ChatObject.isChannel(r1);
        if (r1 == 0) goto L_0x001f;
    L_0x0017:
        r1 = r3.info;
        r1 = r1.hidden_prehistory;
        r2 = r3.historyHidden;
        if (r1 != r2) goto L_0x0066;
    L_0x001f:
        r1 = r3.imageUpdater;
        r1 = r1.uploadingImage;
        if (r1 != 0) goto L_0x0066;
    L_0x0025:
        r1 = r3.nameTextView;
        if (r1 == 0) goto L_0x003b;
    L_0x0029:
        r2 = r3.currentChat;
        r2 = r2.title;
        r1 = r1.getText();
        r1 = r1.toString();
        r1 = r2.equals(r1);
        if (r1 == 0) goto L_0x0066;
    L_0x003b:
        r1 = r3.descriptionTextView;
        if (r1 == 0) goto L_0x004d;
    L_0x003f:
        r1 = r1.getText();
        r1 = r1.toString();
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0066;
    L_0x004d:
        r0 = r3.signMessages;
        r1 = r3.currentChat;
        r2 = r1.signatures;
        if (r0 != r2) goto L_0x0066;
    L_0x0055:
        r0 = r3.uploadedAvatar;
        if (r0 != 0) goto L_0x0066;
    L_0x0059:
        r0 = r3.avatar;
        if (r0 != 0) goto L_0x0064;
    L_0x005d:
        r0 = r1.photo;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_chatPhoto;
        if (r0 == 0) goto L_0x0064;
    L_0x0063:
        goto L_0x0066;
    L_0x0064:
        r0 = 1;
        return r0;
    L_0x0066:
        r0 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r1 = r3.getParentActivity();
        r0.<init>(r1);
        r1 = 2131560995; // 0x7f0d0a23 float:1.8747378E38 double:1.0531310596E-314;
        r2 = "UserRestrictionsApplyChanges";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setTitle(r1);
        r1 = r3.isChannel;
        if (r1 == 0) goto L_0x008c;
    L_0x007f:
        r1 = 2131558999; // 0x7f0d0257 float:1.874333E38 double:1.0531300735E-314;
        r2 = "ChannelSettingsChangedAlert";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setMessage(r1);
        goto L_0x0098;
    L_0x008c:
        r1 = 2131559613; // 0x7f0d04bd float:1.8744575E38 double:1.053130377E-314;
        r2 = "GroupSettingsChangedAlert";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r0.setMessage(r1);
    L_0x0098:
        r1 = 2131558639; // 0x7f0d00ef float:1.87426E38 double:1.0531298956E-314;
        r2 = "ApplyTheme";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r2 = new org.telegram.ui.-$$Lambda$ChatEditActivity$42WGB1bZqU27h5UDp3vuD-usGEg;
        r2.<init>(r3);
        r0.setPositiveButton(r1, r2);
        r1 = 2131560212; // 0x7f0d0714 float:1.874579E38 double:1.053130673E-314;
        r2 = "PassportDiscard";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r2 = new org.telegram.ui.-$$Lambda$ChatEditActivity$NBEr6CX4NZ1r3XbdnOXbearPc6k;
        r2.<init>(r3);
        r0.setNegativeButton(r1, r2);
        r0 = r0.create();
        r3.showDialog(r0);
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ChatEditActivity.checkDiscard():boolean");
    }

    public /* synthetic */ void lambda$checkDiscard$18$ChatEditActivity(DialogInterface dialogInterface, int i) {
        processDone();
    }

    public /* synthetic */ void lambda$checkDiscard$19$ChatEditActivity(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    private int getAdminCount() {
        ChatFull chatFull = this.info;
        if (chatFull == null) {
            return 1;
        }
        int size = chatFull.participants.participants.size();
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            ChatParticipant chatParticipant = (ChatParticipant) this.info.participants.participants.get(i2);
            if ((chatParticipant instanceof TL_chatParticipantAdmin) || (chatParticipant instanceof TL_chatParticipantCreator)) {
                i++;
            }
        }
        return i;
    }

    /* JADX WARNING: Missing block: B:34:0x00c6, code skipped:
            if (r1 != null) goto L_0x00cb;
     */
    private void processDone() {
        /*
        r5 = this;
        r0 = r5.donePressed;
        if (r0 != 0) goto L_0x013c;
    L_0x0004:
        r0 = r5.nameTextView;
        if (r0 != 0) goto L_0x000a;
    L_0x0008:
        goto L_0x013c;
    L_0x000a:
        r0 = r0.length();
        if (r0 != 0) goto L_0x002c;
    L_0x0010:
        r0 = r5.getParentActivity();
        r1 = "vibrator";
        r0 = r0.getSystemService(r1);
        r0 = (android.os.Vibrator) r0;
        if (r0 == 0) goto L_0x0023;
    L_0x001e:
        r1 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0.vibrate(r1);
    L_0x0023:
        r0 = r5.nameTextView;
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r2 = 0;
        org.telegram.messenger.AndroidUtilities.shakeView(r0, r1, r2);
        return;
    L_0x002c:
        r0 = 1;
        r5.donePressed = r0;
        r1 = r5.currentChat;
        r1 = org.telegram.messenger.ChatObject.isChannel(r1);
        if (r1 != 0) goto L_0x0050;
    L_0x0037:
        r1 = r5.historyHidden;
        if (r1 != 0) goto L_0x0050;
    L_0x003b:
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r5.getParentActivity();
        r2 = r5.chatId;
        r3 = new org.telegram.ui.-$$Lambda$ChatEditActivity$FM5TsTTFL8A_rr_SPaVMYlNXC2Q;
        r3.<init>(r5);
        r0.convertToMegaGroup(r1, r2, r3);
        return;
    L_0x0050:
        r1 = r5.info;
        if (r1 == 0) goto L_0x0073;
    L_0x0054:
        r1 = r5.currentChat;
        r1 = org.telegram.messenger.ChatObject.isChannel(r1);
        if (r1 == 0) goto L_0x0073;
    L_0x005c:
        r1 = r5.info;
        r2 = r1.hidden_prehistory;
        r3 = r5.historyHidden;
        if (r2 == r3) goto L_0x0073;
    L_0x0064:
        r1.hidden_prehistory = r3;
        r1 = r5.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = r5.chatId;
        r3 = r5.historyHidden;
        r1.toogleChannelInvitesHistory(r2, r3);
    L_0x0073:
        r1 = r5.imageUpdater;
        r1 = r1.uploadingImage;
        if (r1 == 0) goto L_0x0097;
    L_0x0079:
        r5.createAfterUpload = r0;
        r0 = new org.telegram.ui.ActionBar.AlertDialog;
        r1 = r5.getParentActivity();
        r2 = 3;
        r0.<init>(r1, r2);
        r5.progressDialog = r0;
        r0 = r5.progressDialog;
        r1 = new org.telegram.ui.-$$Lambda$ChatEditActivity$G_WbIT-ViCCFZMn6b9uYoBS_uJ0;
        r1.<init>(r5);
        r0.setOnCancelListener(r1);
        r0 = r5.progressDialog;
        r0.show();
        return;
    L_0x0097:
        r1 = r5.currentChat;
        r1 = r1.title;
        r2 = r5.nameTextView;
        r2 = r2.getText();
        r2 = r2.toString();
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x00c0;
    L_0x00ab:
        r1 = r5.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = r5.chatId;
        r3 = r5.nameTextView;
        r3 = r3.getText();
        r3 = r3.toString();
        r1.changeChatTitle(r2, r3);
    L_0x00c0:
        r1 = r5.info;
        if (r1 == 0) goto L_0x00c9;
    L_0x00c4:
        r1 = r1.about;
        if (r1 == 0) goto L_0x00c9;
    L_0x00c8:
        goto L_0x00cb;
    L_0x00c9:
        r1 = "";
    L_0x00cb:
        r2 = r5.descriptionTextView;
        if (r2 == 0) goto L_0x00f4;
    L_0x00cf:
        r2 = r2.getText();
        r2 = r2.toString();
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x00f4;
    L_0x00dd:
        r1 = r5.currentAccount;
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);
        r2 = r5.chatId;
        r3 = r5.descriptionTextView;
        r3 = r3.getText();
        r3 = r3.toString();
        r4 = r5.info;
        r1.updateChatAbout(r2, r3, r4);
    L_0x00f4:
        r1 = r5.signMessages;
        r2 = r5.currentChat;
        r3 = r2.signatures;
        if (r1 == r3) goto L_0x010b;
    L_0x00fc:
        r2.signatures = r0;
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r5.chatId;
        r2 = r5.signMessages;
        r0.toogleChannelSignatures(r1, r2);
    L_0x010b:
        r0 = r5.uploadedAvatar;
        if (r0 == 0) goto L_0x0121;
    L_0x010f:
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r5.chatId;
        r2 = r5.uploadedAvatar;
        r3 = r5.avatar;
        r4 = r5.avatarBig;
        r0.changeChatAvatar(r1, r2, r3, r4);
        goto L_0x0139;
    L_0x0121:
        r0 = r5.avatar;
        if (r0 != 0) goto L_0x0139;
    L_0x0125:
        r0 = r5.currentChat;
        r0 = r0.photo;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_chatPhoto;
        if (r0 == 0) goto L_0x0139;
    L_0x012d:
        r0 = r5.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r1 = r5.chatId;
        r2 = 0;
        r0.changeChatAvatar(r1, r2, r2, r2);
    L_0x0139:
        r5.finishFragment();
    L_0x013c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.ChatEditActivity.processDone():void");
    }

    public /* synthetic */ void lambda$processDone$20$ChatEditActivity(int i) {
        this.chatId = i;
        this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(i));
        this.donePressed = false;
        ChatFull chatFull = this.info;
        if (chatFull != null) {
            chatFull.hidden_prehistory = true;
        }
        processDone();
    }

    public /* synthetic */ void lambda$processDone$21$ChatEditActivity(DialogInterface dialogInterface) {
        this.createAfterUpload = false;
        this.progressDialog = null;
        this.donePressed = false;
    }

    private void showAvatarProgress(final boolean z, boolean z2) {
        if (this.avatarEditor != null) {
            AnimatorSet animatorSet = this.avatarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.avatarAnimation = null;
            }
            if (z2) {
                this.avatarAnimation = new AnimatorSet();
                AnimatorSet animatorSet2;
                Animator[] animatorArr;
                if (z) {
                    this.avatarProgressView.setVisibility(0);
                    animatorSet2 = this.avatarAnimation;
                    animatorArr = new Animator[2];
                    animatorArr[0] = ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, new float[]{0.0f});
                    animatorArr[1] = ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{1.0f});
                    animatorSet2.playTogether(animatorArr);
                } else {
                    this.avatarEditor.setVisibility(0);
                    animatorSet2 = this.avatarAnimation;
                    animatorArr = new Animator[2];
                    animatorArr[0] = ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, new float[]{1.0f});
                    animatorArr[1] = ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, new float[]{0.0f});
                    animatorSet2.playTogether(animatorArr);
                }
                this.avatarAnimation.setDuration(180);
                this.avatarAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        if (ChatEditActivity.this.avatarAnimation != null && ChatEditActivity.this.avatarEditor != null) {
                            if (z) {
                                ChatEditActivity.this.avatarEditor.setVisibility(4);
                            } else {
                                ChatEditActivity.this.avatarProgressView.setVisibility(4);
                            }
                            ChatEditActivity.this.avatarAnimation = null;
                        }
                    }

                    public void onAnimationCancel(Animator animator) {
                        ChatEditActivity.this.avatarAnimation = null;
                    }
                });
                this.avatarAnimation.start();
            } else if (z) {
                this.avatarEditor.setAlpha(1.0f);
                this.avatarEditor.setVisibility(4);
                this.avatarProgressView.setAlpha(1.0f);
                this.avatarProgressView.setVisibility(0);
            } else {
                this.avatarEditor.setAlpha(1.0f);
                this.avatarEditor.setVisibility(0);
                this.avatarProgressView.setAlpha(0.0f);
                this.avatarProgressView.setVisibility(4);
            }
        }
    }

    public void onActivityResultFragment(int i, int i2, Intent intent) {
        this.imageUpdater.onActivityResult(i, i2, intent);
    }

    public void saveSelfArgs(Bundle bundle) {
        String str;
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            str = imageUpdater.currentPicturePath;
            if (str != null) {
                bundle.putString("path", str);
            }
        }
        EditTextEmoji editTextEmoji = this.nameTextView;
        if (editTextEmoji != null) {
            str = editTextEmoji.getText().toString();
            if (str != null && str.length() != 0) {
                bundle.putString("nameTextView", str);
            }
        }
    }

    public void restoreSelfArgs(Bundle bundle) {
        ImageUpdater imageUpdater = this.imageUpdater;
        if (imageUpdater != null) {
            imageUpdater.currentPicturePath = bundle.getString("path");
        }
    }

    public void setInfo(ChatFull chatFull) {
        this.info = chatFull;
        if (chatFull != null) {
            if (this.currentChat == null) {
                this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chatId));
            }
            boolean z = !ChatObject.isChannel(this.currentChat) || this.info.hidden_prehistory;
            this.historyHidden = z;
        }
    }

    private void updateFields(boolean z) {
        Chat chat;
        ChatFull chatFull;
        int i;
        int i2;
        String str;
        String str2;
        if (z) {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chatId));
            if (chat != null) {
                this.currentChat = chat;
            }
        }
        z = TextUtils.isEmpty(this.currentChat.username);
        TextDetailCell textDetailCell = this.historyCell;
        if (textDetailCell != null) {
            if (z) {
                chatFull = this.info;
                if (chatFull == null || chatFull.linked_chat_id == 0) {
                    i = 0;
                    textDetailCell.setVisibility(i);
                }
            }
            i = 8;
            textDetailCell.setVisibility(i);
        }
        ShadowSectionCell shadowSectionCell = this.settingsSectionCell;
        if (shadowSectionCell != null) {
            if (this.signCell == null && this.typeCell == null && this.linkedCell == null) {
                TextDetailCell textDetailCell2 = this.historyCell;
                if (textDetailCell2 == null || textDetailCell2.getVisibility() != 0) {
                    i = 8;
                    shadowSectionCell.setVisibility(i);
                }
            }
            i = 0;
            shadowSectionCell.setVisibility(i);
        }
        TextCell textCell = this.logCell;
        if (textCell != null) {
            if (this.currentChat.megagroup) {
                chatFull = this.info;
                if (chatFull == null || chatFull.participants_count <= Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                    i = 8;
                    textCell.setVisibility(i);
                }
            }
            i = 0;
            textCell.setVisibility(i);
        }
        if (this.typeCell != null) {
            String string;
            if (this.isChannel) {
                if (z) {
                    i2 = C1067R.string.TypePrivate;
                    str = "TypePrivate";
                } else {
                    i2 = C1067R.string.TypePublic;
                    str = "TypePublic";
                }
                string = LocaleController.getString(str, i2);
            } else {
                if (z) {
                    i2 = C1067R.string.TypePrivateGroup;
                    str = "TypePrivateGroup";
                } else {
                    i2 = C1067R.string.TypePublicGroup;
                    str = "TypePublicGroup";
                }
                string = LocaleController.getString(str, i2);
            }
            if (this.isChannel) {
                this.typeCell.setTextAndValue(LocaleController.getString("ChannelType", C1067R.string.ChannelType), string, true);
            } else {
                this.typeCell.setTextAndValue(LocaleController.getString("GroupType", C1067R.string.GroupType), string, true);
            }
        }
        if (this.linkedCell != null) {
            ChatFull chatFull2 = this.info;
            if (chatFull2 == null || (!this.isChannel && chatFull2.linked_chat_id == 0)) {
                this.linkedCell.setVisibility(8);
            } else {
                this.linkedCell.setVisibility(0);
                str2 = "Discussion";
                if (this.info.linked_chat_id == 0) {
                    this.linkedCell.setTextAndValue(LocaleController.getString(str2, C1067R.string.Discussion), LocaleController.getString("DiscussionInfo", C1067R.string.DiscussionInfo), true);
                } else {
                    chat = getMessagesController().getChat(Integer.valueOf(this.info.linked_chat_id));
                    if (chat == null) {
                        this.linkedCell.setVisibility(8);
                    } else if (this.isChannel) {
                        if (TextUtils.isEmpty(chat.username)) {
                            this.linkedCell.setTextAndValue(LocaleController.getString(str2, C1067R.string.Discussion), chat.title, true);
                        } else {
                            TextDetailCell textDetailCell3 = this.linkedCell;
                            str = LocaleController.getString(str2, C1067R.string.Discussion);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("@");
                            stringBuilder.append(chat.username);
                            textDetailCell3.setTextAndValue(str, stringBuilder.toString(), true);
                        }
                    } else if (TextUtils.isEmpty(chat.username)) {
                        this.linkedCell.setTextAndValue(LocaleController.getString("LinkedChannel", C1067R.string.LinkedChannel), chat.title, false);
                    } else {
                        textDetailCell = this.linkedCell;
                        str2 = LocaleController.getString("LinkedChannel", C1067R.string.LinkedChannel);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("@");
                        stringBuilder2.append(chat.username);
                        textDetailCell.setTextAndValue(str2, stringBuilder2.toString(), false);
                    }
                }
            }
        }
        if (!(this.info == null || this.historyCell == null)) {
            if (this.historyHidden) {
                i2 = C1067R.string.ChatHistoryHidden;
                str = "ChatHistoryHidden";
            } else {
                i2 = C1067R.string.ChatHistoryVisible;
                str = "ChatHistoryVisible";
            }
            this.historyCell.setTextAndValue(LocaleController.getString("ChatHistory", C1067R.string.ChatHistory), LocaleController.getString(str, i2), false);
        }
        TextSettingsCell textSettingsCell = this.stickersCell;
        str2 = "GroupStickers";
        if (textSettingsCell != null) {
            if (this.info.stickerset != null) {
                textSettingsCell.setTextAndValue(LocaleController.getString(str2, C1067R.string.GroupStickers), this.info.stickerset.title, false);
            } else {
                textSettingsCell.setText(LocaleController.getString(str2, C1067R.string.GroupStickers), false);
            }
        }
        TextCell textCell2 = this.membersCell;
        if (textCell2 != null) {
            String str3 = "ChannelBlacklist";
            String str4 = "ChannelSubscribers";
            String str5 = "ChannelMembers";
            String string2;
            if (this.info != null) {
                Object[] objArr;
                String str6 = "%d";
                String format;
                TextCell textCell3;
                boolean z2;
                if (this.isChannel) {
                    textCell2.setTextAndValueAndIcon(LocaleController.getString(str4, C1067R.string.ChannelSubscribers), String.format(str6, new Object[]{Integer.valueOf(this.info.participants_count)}), C1067R.C1065drawable.actions_viewmembers, true);
                    textCell2 = this.blockCell;
                    string2 = LocaleController.getString(str3, C1067R.string.ChannelBlacklist);
                    objArr = new Object[1];
                    ChatFull chatFull3 = this.info;
                    objArr[0] = Integer.valueOf(Math.max(chatFull3.banned_count, chatFull3.kicked_count));
                    format = String.format(str6, objArr);
                    textCell3 = this.logCell;
                    z2 = textCell3 != null && textCell3.getVisibility() == 0;
                    textCell2.setTextAndValueAndIcon(string2, format, C1067R.C1065drawable.actions_removed, z2);
                } else {
                    String format2;
                    if (ChatObject.isChannel(this.currentChat)) {
                        textCell2 = this.membersCell;
                        format = LocaleController.getString(str5, C1067R.string.ChannelMembers);
                        format2 = String.format(str6, new Object[]{Integer.valueOf(this.info.participants_count)});
                        textCell3 = this.logCell;
                        z2 = textCell3 != null && textCell3.getVisibility() == 0;
                        textCell2.setTextAndValueAndIcon(format, format2, C1067R.C1065drawable.actions_viewmembers, z2);
                    } else {
                        textCell2 = this.membersCell;
                        format = LocaleController.getString(str5, C1067R.string.ChannelMembers);
                        format2 = String.format(str6, new Object[]{Integer.valueOf(this.info.participants.participants.size())});
                        textCell3 = this.logCell;
                        z2 = textCell3 != null && textCell3.getVisibility() == 0;
                        textCell2.setTextAndValueAndIcon(format, format2, C1067R.C1065drawable.actions_viewmembers, z2);
                    }
                    TL_chatBannedRights tL_chatBannedRights = this.currentChat.default_banned_rights;
                    if (tL_chatBannedRights != null) {
                        i2 = !tL_chatBannedRights.send_stickers ? 1 : 0;
                        if (!this.currentChat.default_banned_rights.send_media) {
                            i2++;
                        }
                        if (!this.currentChat.default_banned_rights.embed_links) {
                            i2++;
                        }
                        if (!this.currentChat.default_banned_rights.send_messages) {
                            i2++;
                        }
                        if (!this.currentChat.default_banned_rights.pin_messages) {
                            i2++;
                        }
                        if (!this.currentChat.default_banned_rights.send_polls) {
                            i2++;
                        }
                        if (!this.currentChat.default_banned_rights.invite_users) {
                            i2++;
                        }
                        if (!this.currentChat.default_banned_rights.change_info) {
                            i2++;
                        }
                    } else {
                        i2 = 8;
                    }
                    this.blockCell.setTextAndValueAndIcon(LocaleController.getString("ChannelPermissions", C1067R.string.ChannelPermissions), String.format("%d/%d", new Object[]{Integer.valueOf(i2), Integer.valueOf(8)}), C1067R.C1065drawable.actions_permissions, true);
                }
                textCell2 = this.adminCell;
                string2 = LocaleController.getString("ChannelAdministrators", C1067R.string.ChannelAdministrators);
                objArr = new Object[1];
                objArr[0] = Integer.valueOf(ChatObject.isChannel(this.currentChat) ? this.info.admins_count : getAdminCount());
                textCell2.setTextAndValueAndIcon(string2, String.format(str6, objArr), C1067R.C1065drawable.actions_addadmin, true);
            } else {
                if (this.isChannel) {
                    textCell2.setTextAndIcon(LocaleController.getString(str4, C1067R.string.ChannelSubscribers), C1067R.C1065drawable.actions_viewmembers, true);
                    textCell2 = this.blockCell;
                    string2 = LocaleController.getString(str3, C1067R.string.ChannelBlacklist);
                    TextCell textCell4 = this.logCell;
                    boolean z3 = textCell4 != null && textCell4.getVisibility() == 0;
                    textCell2.setTextAndIcon(string2, C1067R.C1065drawable.actions_removed, z3);
                } else {
                    string2 = LocaleController.getString(str5, C1067R.string.ChannelMembers);
                    TextCell textCell5 = this.logCell;
                    boolean z4 = textCell5 != null && textCell5.getVisibility() == 0;
                    textCell2.setTextAndIcon(string2, C1067R.C1065drawable.actions_viewmembers, z4);
                    this.blockCell.setTextAndIcon(LocaleController.getString("ChannelPermissions", C1067R.string.ChannelPermissions), C1067R.C1065drawable.actions_permissions, true);
                }
                this.adminCell.setTextAndIcon(LocaleController.getString("ChannelAdministrators", C1067R.string.ChannelAdministrators), C1067R.C1065drawable.actions_addadmin, true);
            }
        }
        textSettingsCell = this.stickersCell;
        if (textSettingsCell != null) {
            ChatFull chatFull4 = this.info;
            if (chatFull4 == null) {
                return;
            }
            if (chatFull4.stickerset != null) {
                textSettingsCell.setTextAndValue(LocaleController.getString(str2, C1067R.string.GroupStickers), this.info.stickerset.title, false);
            } else {
                textSettingsCell.setText(LocaleController.getString(str2, C1067R.string.GroupStickers), false);
            }
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        C3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs = new C3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs(this);
        r10 = new ThemeDescription[57];
        View view = this.membersCell;
        int i = ThemeDescription.FLAG_TEXTCOLOR;
        Class[] clsArr = new Class[]{TextCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        r10[6] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.membersCell;
        clsArr = new Class[]{TextCell.class};
        strArr = new String[1];
        strArr[0] = "imageView";
        r10[7] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r10[8] = new ThemeDescription(this.adminCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[9] = new ThemeDescription(this.adminCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[10] = new ThemeDescription(this.adminCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r10[11] = new ThemeDescription(this.blockCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[12] = new ThemeDescription(this.blockCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[13] = new ThemeDescription(this.blockCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r10[14] = new ThemeDescription(this.logCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[15] = new ThemeDescription(this.logCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[16] = new ThemeDescription(this.logCell, 0, new Class[]{TextCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayIcon);
        r10[17] = new ThemeDescription(this.typeCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[18] = new ThemeDescription(this.typeCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.typeCell;
        clsArr = new Class[]{TextDetailCell.class};
        strArr = new String[1];
        strArr[0] = "valueTextView";
        r10[19] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        r10[20] = new ThemeDescription(this.historyCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[21] = new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[22] = new ThemeDescription(this.historyCell, 0, new Class[]{TextDetailCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        r10[23] = new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[24] = new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        r10[25] = new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField);
        r10[26] = new ThemeDescription(this.nameTextView, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated);
        r10[27] = new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[28] = new ThemeDescription(this.descriptionTextView, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText);
        r10[29] = new ThemeDescription(this.avatarContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r10[30] = new ThemeDescription(this.settingsContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r10[31] = new ThemeDescription(this.typeEditContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r10[32] = new ThemeDescription(this.deleteContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r10[33] = new ThemeDescription(this.stickersContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r10[34] = new ThemeDescription(this.infoContainer, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite);
        r10[35] = new ThemeDescription(this.settingsTopSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[36] = new ThemeDescription(this.settingsSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[37] = new ThemeDescription(this.deleteInfoCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[38] = new ThemeDescription(this.infoSectionCell, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[39] = new ThemeDescription(this.signCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[40] = new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.signCell;
        clsArr = new Class[]{TextCheckCell.class};
        strArr = new String[1];
        strArr[0] = "checkBox";
        r10[41] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_switchTrack);
        r10[42] = new ThemeDescription(this.signCell, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked);
        r10[43] = new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[44] = new ThemeDescription(this.deleteCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteRedText5);
        r10[45] = new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r10[46] = new ThemeDescription(this.stickersCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r10[47] = new ThemeDescription(this.stickersInfoCell3, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r10[48] = new ThemeDescription(this.stickersInfoCell3, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        C3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2 = c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs;
        r10[49] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_text);
        r10[50] = new ThemeDescription(null, 0, null, null, null, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_backgroundRed);
        r10[51] = new ThemeDescription(null, 0, null, null, null, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_backgroundOrange);
        r10[52] = new ThemeDescription(null, 0, null, null, null, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_backgroundViolet);
        r10[53] = new ThemeDescription(null, 0, null, null, null, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_backgroundGreen);
        r10[54] = new ThemeDescription(null, 0, null, null, null, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_backgroundCyan);
        r10[55] = new ThemeDescription(null, 0, null, null, null, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_backgroundBlue);
        r10[56] = new ThemeDescription(null, 0, null, null, null, c3636-$$Lambda$ChatEditActivity$7jDsoknIpJ3fyHQHkRcRc05xZJs2, Theme.key_avatar_backgroundPink);
        return r10;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$22$ChatEditActivity() {
        if (this.avatarImage != null) {
            this.avatarDrawable.setInfo(5, null, null, false);
            this.avatarImage.invalidate();
        }
    }
}