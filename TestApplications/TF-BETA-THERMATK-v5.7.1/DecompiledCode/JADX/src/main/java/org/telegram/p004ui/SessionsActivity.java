package org.telegram.p004ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.C2190ActionBar;
import org.telegram.p004ui.ActionBar.C2190ActionBar.ActionBarMenuOnItemClick;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.ThemeDescription;
import org.telegram.p004ui.Cells.CheckBoxCell;
import org.telegram.p004ui.Cells.HeaderCell;
import org.telegram.p004ui.Cells.SessionCell;
import org.telegram.p004ui.Cells.TextInfoPrivacyCell;
import org.telegram.p004ui.Cells.TextSettingsCell;
import org.telegram.p004ui.Components.EmptyTextProgressView;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.Holder;
import org.telegram.p004ui.Components.RecyclerListView.SelectionAdapter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_account_authorizations;
import org.telegram.tgnet.TLRPC.TL_account_getAuthorizations;
import org.telegram.tgnet.TLRPC.TL_account_getWebAuthorizations;
import org.telegram.tgnet.TLRPC.TL_account_resetAuthorization;
import org.telegram.tgnet.TLRPC.TL_account_resetWebAuthorization;
import org.telegram.tgnet.TLRPC.TL_account_resetWebAuthorizations;
import org.telegram.tgnet.TLRPC.TL_account_webAuthorizations;
import org.telegram.tgnet.TLRPC.TL_auth_resetAuthorizations;
import org.telegram.tgnet.TLRPC.TL_authorization;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_webAuthorization;
import org.telegram.tgnet.TLRPC.User;

/* renamed from: org.telegram.ui.SessionsActivity */
public class SessionsActivity extends BaseFragment implements NotificationCenterDelegate {
    private TL_authorization currentSession;
    private int currentSessionRow;
    private int currentSessionSectionRow;
    private int currentType;
    private LinearLayout emptyLayout;
    private EmptyTextProgressView emptyView;
    private ImageView imageView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loading;
    private int noOtherSessionsRow;
    private int otherSessionsEndRow;
    private int otherSessionsSectionRow;
    private int otherSessionsStartRow;
    private int otherSessionsTerminateDetail;
    private ArrayList<TLObject> passwordSessions = new ArrayList();
    private int passwordSessionsDetailRow;
    private int passwordSessionsEndRow;
    private int passwordSessionsSectionRow;
    private int passwordSessionsStartRow;
    private int rowCount;
    private ArrayList<TLObject> sessions = new ArrayList();
    private int terminateAllSessionsDetailRow;
    private int terminateAllSessionsRow;
    private TextView textView1;
    private TextView textView2;

    /* renamed from: org.telegram.ui.SessionsActivity$1 */
    class C43251 extends ActionBarMenuOnItemClick {
        C43251() {
        }

        public void onItemClick(int i) {
            if (i == -1) {
                SessionsActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.SessionsActivity$ListAdapter */
    private class ListAdapter extends SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isEnabled(ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == SessionsActivity.this.terminateAllSessionsRow || ((adapterPosition >= SessionsActivity.this.otherSessionsStartRow && adapterPosition < SessionsActivity.this.otherSessionsEndRow) || (adapterPosition >= SessionsActivity.this.passwordSessionsStartRow && adapterPosition < SessionsActivity.this.passwordSessionsEndRow));
        }

        public int getItemCount() {
            return SessionsActivity.this.loading ? 0 : SessionsActivity.this.rowCount;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textSettingsCell;
            String str = Theme.key_windowBackgroundWhite;
            if (i == 0) {
                textSettingsCell = new TextSettingsCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor(str));
            } else if (i == 1) {
                textSettingsCell = new TextInfoPrivacyCell(this.mContext);
            } else if (i == 2) {
                textSettingsCell = new HeaderCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor(str));
            } else if (i != 3) {
                textSettingsCell = new SessionCell(this.mContext, SessionsActivity.this.currentType);
                textSettingsCell.setBackgroundColor(Theme.getColor(str));
            } else {
                textSettingsCell = SessionsActivity.this.emptyLayout;
            }
            return new Holder(textSettingsCell);
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            int i2 = 0;
            if (itemViewType == 0) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i == SessionsActivity.this.terminateAllSessionsRow) {
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteRedText2));
                    if (SessionsActivity.this.currentType == 0) {
                        textSettingsCell.setText(LocaleController.getString("TerminateAllSessions", C1067R.string.TerminateAllSessions), false);
                    } else {
                        textSettingsCell.setText(LocaleController.getString("TerminateAllWebSessions", C1067R.string.TerminateAllWebSessions), false);
                    }
                }
            } else if (itemViewType == 1) {
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                itemViewType = SessionsActivity.this.terminateAllSessionsDetailRow;
                String str = Theme.key_windowBackgroundGrayShadow;
                if (i == itemViewType) {
                    if (SessionsActivity.this.currentType == 0) {
                        textInfoPrivacyCell.setText(LocaleController.getString("ClearOtherSessionsHelp", C1067R.string.ClearOtherSessionsHelp));
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.getString("ClearOtherWebSessionsHelp", C1067R.string.ClearOtherWebSessionsHelp));
                    }
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                } else if (i == SessionsActivity.this.otherSessionsTerminateDetail) {
                    if (SessionsActivity.this.currentType == 0) {
                        textInfoPrivacyCell.setText(LocaleController.getString("TerminateSessionInfo", C1067R.string.TerminateSessionInfo));
                    } else {
                        textInfoPrivacyCell.setText(LocaleController.getString("TerminateWebSessionInfo", C1067R.string.TerminateWebSessionInfo));
                    }
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, str));
                } else if (i == SessionsActivity.this.passwordSessionsDetailRow) {
                    textInfoPrivacyCell.setText(LocaleController.getString("LoginAttemptsInfo", C1067R.string.LoginAttemptsInfo));
                    if (SessionsActivity.this.otherSessionsTerminateDetail == -1) {
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider_bottom, str));
                    } else {
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, (int) C1067R.C1065drawable.greydivider, str));
                    }
                }
            } else if (itemViewType == 2) {
                HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                if (i == SessionsActivity.this.currentSessionSectionRow) {
                    headerCell.setText(LocaleController.getString("CurrentSession", C1067R.string.CurrentSession));
                } else if (i == SessionsActivity.this.otherSessionsSectionRow) {
                    if (SessionsActivity.this.currentType == 0) {
                        headerCell.setText(LocaleController.getString("OtherSessions", C1067R.string.OtherSessions));
                    } else {
                        headerCell.setText(LocaleController.getString("OtherWebSessions", C1067R.string.OtherWebSessions));
                    }
                } else if (i == SessionsActivity.this.passwordSessionsSectionRow) {
                    headerCell.setText(LocaleController.getString("LoginAttempts", C1067R.string.LoginAttempts));
                }
            } else if (itemViewType != 3) {
                SessionCell sessionCell = (SessionCell) viewHolder.itemView;
                boolean z;
                TLObject tLObject;
                if (i == SessionsActivity.this.currentSessionRow) {
                    TL_authorization access$1600 = SessionsActivity.this.currentSession;
                    if (!(SessionsActivity.this.sessions.isEmpty() && SessionsActivity.this.passwordSessions.isEmpty())) {
                        z = true;
                    }
                    sessionCell.setSession(access$1600, z);
                } else if (i >= SessionsActivity.this.otherSessionsStartRow && i < SessionsActivity.this.otherSessionsEndRow) {
                    tLObject = (TLObject) SessionsActivity.this.sessions.get(i - SessionsActivity.this.otherSessionsStartRow);
                    if (i != SessionsActivity.this.otherSessionsEndRow - 1) {
                        z = true;
                    }
                    sessionCell.setSession(tLObject, z);
                } else if (i >= SessionsActivity.this.passwordSessionsStartRow && i < SessionsActivity.this.passwordSessionsEndRow) {
                    tLObject = (TLObject) SessionsActivity.this.passwordSessions.get(i - SessionsActivity.this.passwordSessionsStartRow);
                    if (i != SessionsActivity.this.passwordSessionsEndRow - 1) {
                        z = true;
                    }
                    sessionCell.setSession(tLObject, z);
                }
            } else {
                LayoutParams layoutParams = SessionsActivity.this.emptyLayout.getLayoutParams();
                if (layoutParams != null) {
                    i = AndroidUtilities.m26dp(220.0f);
                    itemViewType = (AndroidUtilities.displaySize.y - C2190ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.m26dp(128.0f);
                    if (VERSION.SDK_INT >= 21) {
                        i2 = AndroidUtilities.statusBarHeight;
                    }
                    layoutParams.height = Math.max(i, itemViewType - i2);
                    SessionsActivity.this.emptyLayout.setLayoutParams(layoutParams);
                }
            }
        }

        public int getItemViewType(int i) {
            if (i == SessionsActivity.this.terminateAllSessionsRow) {
                return 0;
            }
            if (i == SessionsActivity.this.terminateAllSessionsDetailRow || i == SessionsActivity.this.otherSessionsTerminateDetail || i == SessionsActivity.this.passwordSessionsDetailRow) {
                return 1;
            }
            if (i == SessionsActivity.this.currentSessionSectionRow || i == SessionsActivity.this.otherSessionsSectionRow || i == SessionsActivity.this.passwordSessionsSectionRow) {
                return 2;
            }
            if (i == SessionsActivity.this.noOtherSessionsRow) {
                return 3;
            }
            if (i == SessionsActivity.this.currentSessionRow || ((i >= SessionsActivity.this.otherSessionsStartRow && i < SessionsActivity.this.otherSessionsEndRow) || (i >= SessionsActivity.this.passwordSessionsStartRow && i < SessionsActivity.this.passwordSessionsEndRow))) {
                return 4;
            }
            return 0;
        }
    }

    public SessionsActivity(int i) {
        this.currentType = i;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        loadSessions(false);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newSessionReceived);
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newSessionReceived);
    }

    public View createView(Context context) {
        Context context2 = context;
        this.actionBar.setBackButtonImage(C1067R.C1065drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            this.actionBar.setTitle(LocaleController.getString("SessionsTitle", C1067R.string.SessionsTitle));
        } else {
            this.actionBar.setTitle(LocaleController.getString("WebSessionsTitle", C1067R.string.WebSessionsTitle));
        }
        this.actionBar.setActionBarMenuOnItemClick(new C43251());
        this.listAdapter = new ListAdapter(context2);
        this.fragmentView = new FrameLayout(context2);
        FrameLayout frameLayout = (FrameLayout) this.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        this.emptyLayout = new LinearLayout(context2);
        this.emptyLayout.setOrientation(1);
        this.emptyLayout.setGravity(17);
        this.emptyLayout.setBackgroundDrawable(Theme.getThemedDrawable(context2, (int) C1067R.C1065drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
        this.emptyLayout.setLayoutParams(new AbsListView.LayoutParams(-1, AndroidUtilities.displaySize.y - C2190ActionBar.getCurrentActionBarHeight()));
        this.imageView = new ImageView(context2);
        if (this.currentType == 0) {
            this.imageView.setImageResource(C1067R.C1065drawable.devices);
        } else {
            this.imageView.setImageResource(C1067R.C1065drawable.no_apps);
        }
        this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_sessions_devicesImage), Mode.MULTIPLY));
        this.emptyLayout.addView(this.imageView, LayoutHelper.createLinear(-2, -2));
        this.textView1 = new TextView(context2);
        TextView textView = this.textView1;
        String str = Theme.key_windowBackgroundWhiteGrayText2;
        textView.setTextColor(Theme.getColor(str));
        this.textView1.setGravity(17);
        this.textView1.setTextSize(1, 17.0f);
        this.textView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        if (this.currentType == 0) {
            this.textView1.setText(LocaleController.getString("NoOtherSessions", C1067R.string.NoOtherSessions));
        } else {
            this.textView1.setText(LocaleController.getString("NoOtherWebSessions", C1067R.string.NoOtherWebSessions));
        }
        this.emptyLayout.addView(this.textView1, LayoutHelper.createLinear(-2, -2, 17, 0, 16, 0, 0));
        this.textView2 = new TextView(context2);
        this.textView2.setTextColor(Theme.getColor(str));
        this.textView2.setGravity(17);
        this.textView2.setTextSize(1, 17.0f);
        this.textView2.setPadding(AndroidUtilities.m26dp(20.0f), 0, AndroidUtilities.m26dp(20.0f), 0);
        if (this.currentType == 0) {
            this.textView2.setText(LocaleController.getString("NoOtherSessionsInfo", C1067R.string.NoOtherSessionsInfo));
        } else {
            this.textView2.setText(LocaleController.getString("NoOtherWebSessionsInfo", C1067R.string.NoOtherWebSessionsInfo));
        }
        this.emptyLayout.addView(this.textView2, LayoutHelper.createLinear(-2, -2, 17, 0, 14, 0, 0));
        this.emptyView = new EmptyTextProgressView(context2);
        this.emptyView.showProgress();
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, -1, 17));
        this.listView = new RecyclerListView(context2);
        this.listView.setLayoutManager(new LinearLayoutManager(context2, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setEmptyView(this.emptyView);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener(new C3864-$$Lambda$SessionsActivity$nGXAGNwZRPb8yKcWdHCTunuNa5s(this));
        return this.fragmentView;
    }

    public /* synthetic */ void lambda$createView$11$SessionsActivity(View view, int i) {
        int i2 = i;
        String str = "Cancel";
        String str2 = "OK";
        String str3 = "AppName";
        if (i2 == this.terminateAllSessionsRow) {
            if (getParentActivity() != null) {
                Builder builder = new Builder(getParentActivity());
                if (this.currentType == 0) {
                    builder.setMessage(LocaleController.getString("AreYouSureSessions", C1067R.string.AreYouSureSessions));
                } else {
                    builder.setMessage(LocaleController.getString("AreYouSureWebSessions", C1067R.string.AreYouSureWebSessions));
                }
                builder.setTitle(LocaleController.getString(str3, C1067R.string.AppName));
                builder.setPositiveButton(LocaleController.getString(str2, C1067R.string.f61OK), new C1978-$$Lambda$SessionsActivity$oaaWYlgt897yPsbKGRVtpZZB0Zw(this));
                builder.setNegativeButton(LocaleController.getString(str, C1067R.string.Cancel), null);
                showDialog(builder.create());
            }
        } else if (((i2 >= this.otherSessionsStartRow && i2 < this.otherSessionsEndRow) || (i2 >= this.passwordSessionsStartRow && i2 < this.passwordSessionsEndRow)) && getParentActivity() != null) {
            Builder builder2 = new Builder(getParentActivity());
            builder2.setTitle(LocaleController.getString(str3, C1067R.string.AppName));
            boolean[] zArr = new boolean[1];
            if (this.currentType == 0) {
                builder2.setMessage(LocaleController.getString("TerminateSessionQuestion", C1067R.string.TerminateSessionQuestion));
            } else {
                builder2.setMessage(LocaleController.formatString("TerminateWebSessionQuestion", C1067R.string.TerminateWebSessionQuestion, ((TL_webAuthorization) this.sessions.get(i2 - this.otherSessionsStartRow)).domain));
                FrameLayout frameLayout = new FrameLayout(getParentActivity());
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(r10.bot_id));
                String str4 = "";
                String firstName = user != null ? UserObject.getFirstName(user) : str4;
                CheckBoxCell checkBoxCell = new CheckBoxCell(getParentActivity(), 1);
                checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                checkBoxCell.setText(LocaleController.formatString("TerminateWebSessionStop", C1067R.string.TerminateWebSessionStop, firstName), str4, false, false);
                checkBoxCell.setPadding(LocaleController.isRTL ? AndroidUtilities.m26dp(16.0f) : AndroidUtilities.m26dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.m26dp(8.0f) : AndroidUtilities.m26dp(16.0f), 0);
                frameLayout.addView(checkBoxCell, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                checkBoxCell.setOnClickListener(new C1977-$$Lambda$SessionsActivity$fTHr4kOeCS_36_P5G_qrJ-UGaNE(zArr));
                builder2.setCustomViewOffset(16);
                builder2.setView(frameLayout);
            }
            builder2.setPositiveButton(LocaleController.getString(str2, C1067R.string.f61OK), new C1973-$$Lambda$SessionsActivity$Rp5LHvaMpc39OI5FKrDiSKjtcrQ(this, i2, zArr));
            builder2.setNegativeButton(LocaleController.getString(str, C1067R.string.Cancel), null);
            showDialog(builder2.create());
        }
    }

    public /* synthetic */ void lambda$null$4$SessionsActivity(DialogInterface dialogInterface, int i) {
        if (this.currentType == 0) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_auth_resetAuthorizations(), new C3862-$$Lambda$SessionsActivity$dxKKwoZ08ejJ_JgokkvdiYJTtHg(this));
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_resetWebAuthorizations(), new C3863-$$Lambda$SessionsActivity$msYb4DTCxuG4mkTvoMcr_IVJivE(this));
    }

    public /* synthetic */ void lambda$null$1$SessionsActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1972-$$Lambda$SessionsActivity$DgWzs1LwXUgkxX8mgzP9nfgTbVA(this, tL_error, tLObject));
        for (int i = 0; i < 3; i++) {
            UserConfig instance = UserConfig.getInstance(i);
            if (instance.isClientActivated()) {
                instance.registeredForPush = false;
                instance.saveConfig(false);
                MessagesController.getInstance(i).registerForPush(SharedConfig.pushString);
                ConnectionsManager.getInstance(i).setUserId(instance.getClientUserId());
            }
        }
    }

    public /* synthetic */ void lambda$null$0$SessionsActivity(TL_error tL_error, TLObject tLObject) {
        if (getParentActivity() != null && tL_error == null && (tLObject instanceof TL_boolTrue)) {
            Toast.makeText(getParentActivity(), LocaleController.getString("TerminateAllSessions", C1067R.string.TerminateAllSessions), 0).show();
            finishFragment();
        }
    }

    public /* synthetic */ void lambda$null$3$SessionsActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1971-$$Lambda$SessionsActivity$Az0uYHbPXgbOODawLpn12NZhF5M(this, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$2$SessionsActivity(TL_error tL_error, TLObject tLObject) {
        if (getParentActivity() != null) {
            if (tL_error == null && (tLObject instanceof TL_boolTrue)) {
                Toast.makeText(getParentActivity(), LocaleController.getString("TerminateAllWebSessions", C1067R.string.TerminateAllWebSessions), 0).show();
            } else {
                Toast.makeText(getParentActivity(), LocaleController.getString("UnknownError", C1067R.string.UnknownError), 0).show();
            }
            finishFragment();
        }
    }

    static /* synthetic */ void lambda$null$5(boolean[] zArr, View view) {
        if (view.isEnabled()) {
            CheckBoxCell checkBoxCell = (CheckBoxCell) view;
            zArr[0] = zArr[0] ^ 1;
            checkBoxCell.setChecked(zArr[0], true);
        }
    }

    public /* synthetic */ void lambda$null$10$SessionsActivity(int i, boolean[] zArr, DialogInterface dialogInterface, int i2) {
        if (getParentActivity() != null) {
            AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
            alertDialog.setCanCacnel(false);
            alertDialog.show();
            if (this.currentType == 0) {
                TL_authorization tL_authorization;
                int i3 = this.otherSessionsStartRow;
                if (i < i3 || i >= this.otherSessionsEndRow) {
                    tL_authorization = (TL_authorization) this.passwordSessions.get(i - this.passwordSessionsStartRow);
                } else {
                    tL_authorization = (TL_authorization) this.sessions.get(i - i3);
                }
                TL_account_resetAuthorization tL_account_resetAuthorization = new TL_account_resetAuthorization();
                tL_account_resetAuthorization.hash = tL_authorization.hash;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_resetAuthorization, new C3865-$$Lambda$SessionsActivity$uCzzVW_FF8MV8J-oXGgpzlkkOI4(this, alertDialog, tL_authorization));
            } else {
                TL_webAuthorization tL_webAuthorization = (TL_webAuthorization) this.sessions.get(i - this.otherSessionsStartRow);
                TL_account_resetWebAuthorization tL_account_resetWebAuthorization = new TL_account_resetWebAuthorization();
                tL_account_resetWebAuthorization.hash = tL_webAuthorization.hash;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_account_resetWebAuthorization, new C3860-$$Lambda$SessionsActivity$F3jY3_Lk4fRyFUz5zR6es4a8E6M(this, alertDialog, tL_webAuthorization));
                if (zArr[0]) {
                    MessagesController.getInstance(this.currentAccount).blockUser(tL_webAuthorization.bot_id);
                }
            }
        }
    }

    public /* synthetic */ void lambda$null$7$SessionsActivity(AlertDialog alertDialog, TL_authorization tL_authorization, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1975-$$Lambda$SessionsActivity$We5_mg_HTuMQbN9C9uG4Vipc1GQ(this, alertDialog, tL_error, tL_authorization));
    }

    public /* synthetic */ void lambda$null$6$SessionsActivity(AlertDialog alertDialog, TL_error tL_error, TL_authorization tL_authorization) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tL_error == null) {
            this.sessions.remove(tL_authorization);
            this.passwordSessions.remove(tL_authorization);
            updateRows();
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    public /* synthetic */ void lambda$null$9$SessionsActivity(AlertDialog alertDialog, TL_webAuthorization tL_webAuthorization, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1979-$$Lambda$SessionsActivity$sjBDbKRuMyWfdQovBWSND8F1Gd4(this, alertDialog, tL_error, tL_webAuthorization));
    }

    public /* synthetic */ void lambda$null$8$SessionsActivity(AlertDialog alertDialog, TL_error tL_error, TL_webAuthorization tL_webAuthorization) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tL_error == null) {
            this.sessions.remove(tL_webAuthorization);
            updateRows();
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.newSessionReceived) {
            loadSessions(true);
        }
    }

    private void loadSessions(boolean z) {
        if (!this.loading) {
            if (!z) {
                this.loading = true;
            }
            if (this.currentType == 0) {
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getAuthorizations(), new C3859-$$Lambda$SessionsActivity$89ZRjxSgVCBTgysCrEBIMz4rP7Q(this)), this.classGuid);
            } else {
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_account_getWebAuthorizations(), new C3861-$$Lambda$SessionsActivity$K2XooGFKxHY7V_MnCDQA6rQ3do4(this)), this.classGuid);
            }
        }
    }

    public /* synthetic */ void lambda$loadSessions$13$SessionsActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1976-$$Lambda$SessionsActivity$Zt74L2MQ-s9swKF0srb1gpKxots(this, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$12$SessionsActivity(TL_error tL_error, TLObject tLObject) {
        int i = 0;
        this.loading = false;
        if (tL_error == null) {
            this.sessions.clear();
            this.passwordSessions.clear();
            TL_account_authorizations tL_account_authorizations = (TL_account_authorizations) tLObject;
            int size = tL_account_authorizations.authorizations.size();
            while (i < size) {
                TL_authorization tL_authorization = (TL_authorization) tL_account_authorizations.authorizations.get(i);
                if ((tL_authorization.flags & 1) != 0) {
                    this.currentSession = tL_authorization;
                } else if (tL_authorization.password_pending) {
                    this.passwordSessions.add(tL_authorization);
                } else {
                    this.sessions.add(tL_authorization);
                }
                i++;
            }
            updateRows();
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public /* synthetic */ void lambda$loadSessions$15$SessionsActivity(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1974-$$Lambda$SessionsActivity$VzziGibbmz1VChBvlPo1LDLCXbs(this, tL_error, tLObject));
    }

    public /* synthetic */ void lambda$null$14$SessionsActivity(TL_error tL_error, TLObject tLObject) {
        this.loading = false;
        if (tL_error == null) {
            this.sessions.clear();
            TL_account_webAuthorizations tL_account_webAuthorizations = (TL_account_webAuthorizations) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tL_account_webAuthorizations.users, false);
            this.sessions.addAll(tL_account_webAuthorizations.authorizations);
            updateRows();
        }
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void updateRows() {
        int i;
        this.rowCount = 0;
        if (this.currentSession != null) {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.currentSessionSectionRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.currentSessionRow = i;
        } else {
            this.currentSessionRow = -1;
            this.currentSessionSectionRow = -1;
        }
        if (this.passwordSessions.isEmpty() && this.sessions.isEmpty()) {
            this.terminateAllSessionsRow = -1;
            this.terminateAllSessionsDetailRow = -1;
            if (this.currentType == 1 || this.currentSession != null) {
                i = this.rowCount;
                this.rowCount = i + 1;
                this.noOtherSessionsRow = i;
            } else {
                this.noOtherSessionsRow = -1;
            }
        } else {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.terminateAllSessionsRow = i;
            i = this.rowCount;
            this.rowCount = i + 1;
            this.terminateAllSessionsDetailRow = i;
            this.noOtherSessionsRow = -1;
        }
        if (this.passwordSessions.isEmpty()) {
            this.passwordSessionsDetailRow = -1;
            this.passwordSessionsEndRow = -1;
            this.passwordSessionsStartRow = -1;
            this.passwordSessionsSectionRow = -1;
        } else {
            i = this.rowCount;
            this.rowCount = i + 1;
            this.passwordSessionsSectionRow = i;
            i = this.rowCount;
            this.passwordSessionsStartRow = i;
            this.rowCount = i + this.passwordSessions.size();
            i = this.rowCount;
            this.passwordSessionsEndRow = i;
            this.rowCount = i + 1;
            this.passwordSessionsDetailRow = i;
        }
        if (this.sessions.isEmpty()) {
            this.otherSessionsSectionRow = -1;
            this.otherSessionsStartRow = -1;
            this.otherSessionsEndRow = -1;
            this.otherSessionsTerminateDetail = -1;
            return;
        }
        i = this.rowCount;
        this.rowCount = i + 1;
        this.otherSessionsSectionRow = i;
        this.otherSessionsStartRow = this.otherSessionsSectionRow + 1;
        this.otherSessionsEndRow = this.otherSessionsStartRow + this.sessions.size();
        this.rowCount += this.sessions.size();
        i = this.rowCount;
        this.rowCount = i + 1;
        this.otherSessionsTerminateDetail = i;
    }

    public ThemeDescription[] getThemeDescriptions() {
        r1 = new ThemeDescription[22];
        r1[0] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, SessionCell.class}, null, null, null, Theme.key_windowBackgroundWhite);
        r1[1] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray);
        r1[2] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        r1[3] = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        r1[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        r1[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        r1[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        r1[7] = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector);
        r1[8] = new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider);
        r1[9] = new ThemeDescription(this.imageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_sessions_devicesImage);
        r1[10] = new ThemeDescription(this.textView1, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        r1[11] = new ThemeDescription(this.textView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        r1[12] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle);
        View view = this.listView;
        Class[] clsArr = new Class[]{TextSettingsCell.class};
        String[] strArr = new String[1];
        strArr[0] = "textView";
        r1[13] = new ThemeDescription(view, 0, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteRedText2);
        r1[14] = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow);
        r1[15] = new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4);
        r1[16] = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader);
        r1[17] = new ThemeDescription(this.listView, 0, new Class[]{SessionCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        view = this.listView;
        int i = ThemeDescription.FLAG_CHECKTAG;
        clsArr = new Class[]{SessionCell.class};
        strArr = new String[1];
        strArr[0] = "onlineTextView";
        r1[18] = new ThemeDescription(view, i, clsArr, strArr, null, null, null, Theme.key_windowBackgroundWhiteValueText);
        r1[19] = new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{SessionCell.class}, new String[]{"onlineTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        r1[20] = new ThemeDescription(this.listView, 0, new Class[]{SessionCell.class}, new String[]{"detailTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        r1[21] = new ThemeDescription(this.listView, 0, new Class[]{SessionCell.class}, new String[]{"detailExTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        return r1;
    }
}
