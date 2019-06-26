// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.os.Build$VERSION;
import org.telegram.messenger.FileLog;
import java.util.Collection;
import android.content.DialogInterface;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import android.widget.Toast;
import android.view.View$OnClickListener;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.MessagesController;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.SessionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.LayoutHelper;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.view.ViewGroup$LayoutParams;
import android.widget.AbsListView$LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Cells.CheckBoxCell;
import android.view.View;
import android.widget.TextView;
import org.telegram.tgnet.TLObject;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;
import android.widget.ImageView;
import org.telegram.ui.Components.EmptyTextProgressView;
import android.widget.LinearLayout;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class SessionsActivity extends BaseFragment implements NotificationCenterDelegate
{
    private TLRPC.TL_authorization currentSession;
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
    private ArrayList<TLObject> passwordSessions;
    private int passwordSessionsDetailRow;
    private int passwordSessionsEndRow;
    private int passwordSessionsSectionRow;
    private int passwordSessionsStartRow;
    private int rowCount;
    private ArrayList<TLObject> sessions;
    private int terminateAllSessionsDetailRow;
    private int terminateAllSessionsRow;
    private TextView textView1;
    private TextView textView2;
    
    public SessionsActivity(final int currentType) {
        this.sessions = new ArrayList<TLObject>();
        this.passwordSessions = new ArrayList<TLObject>();
        this.currentType = currentType;
    }
    
    private void loadSessions(final boolean b) {
        if (this.loading) {
            return;
        }
        if (!b) {
            this.loading = true;
        }
        if (this.currentType == 0) {
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_account_getAuthorizations(), new _$$Lambda$SessionsActivity$89ZRjxSgVCBTgysCrEBIMz4rP7Q(this)), super.classGuid);
        }
        else {
            ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(new TLRPC.TL_account_getWebAuthorizations(), new _$$Lambda$SessionsActivity$K2XooGFKxHY7V_MnCDQA6rQ3do4(this)), super.classGuid);
        }
    }
    
    private void updateRows() {
        this.rowCount = 0;
        if (this.currentSession != null) {
            this.currentSessionSectionRow = this.rowCount++;
            this.currentSessionRow = this.rowCount++;
        }
        else {
            this.currentSessionRow = -1;
            this.currentSessionSectionRow = -1;
        }
        if (this.passwordSessions.isEmpty() && this.sessions.isEmpty()) {
            this.terminateAllSessionsRow = -1;
            this.terminateAllSessionsDetailRow = -1;
            if (this.currentType != 1 && this.currentSession == null) {
                this.noOtherSessionsRow = -1;
            }
            else {
                this.noOtherSessionsRow = this.rowCount++;
            }
        }
        else {
            this.terminateAllSessionsRow = this.rowCount++;
            this.terminateAllSessionsDetailRow = this.rowCount++;
            this.noOtherSessionsRow = -1;
        }
        if (this.passwordSessions.isEmpty()) {
            this.passwordSessionsDetailRow = -1;
            this.passwordSessionsEndRow = -1;
            this.passwordSessionsStartRow = -1;
            this.passwordSessionsSectionRow = -1;
        }
        else {
            this.passwordSessionsSectionRow = this.rowCount++;
            final int rowCount = this.rowCount;
            this.passwordSessionsStartRow = rowCount;
            this.rowCount = rowCount + this.passwordSessions.size();
            final int rowCount2 = this.rowCount;
            this.passwordSessionsEndRow = rowCount2;
            this.rowCount = rowCount2 + 1;
            this.passwordSessionsDetailRow = rowCount2;
        }
        if (this.sessions.isEmpty()) {
            this.otherSessionsSectionRow = -1;
            this.otherSessionsStartRow = -1;
            this.otherSessionsEndRow = -1;
            this.otherSessionsTerminateDetail = -1;
        }
        else {
            this.otherSessionsSectionRow = this.rowCount++;
            this.otherSessionsStartRow = this.otherSessionsSectionRow + 1;
            this.otherSessionsEndRow = this.otherSessionsStartRow + this.sessions.size();
            this.rowCount += this.sessions.size();
            this.otherSessionsTerminateDetail = this.rowCount++;
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            super.actionBar.setTitle(LocaleController.getString("SessionsTitle", 2131560726));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("WebSessionsTitle", 2131561104));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    SessionsActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.emptyLayout = new LinearLayout(context)).setOrientation(1);
        this.emptyLayout.setGravity(17);
        this.emptyLayout.setBackgroundDrawable(Theme.getThemedDrawable(context, 2131165395, "windowBackgroundGrayShadow"));
        this.emptyLayout.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()));
        this.imageView = new ImageView(context);
        if (this.currentType == 0) {
            this.imageView.setImageResource(2131165373);
        }
        else {
            this.imageView.setImageResource(2131165691);
        }
        this.imageView.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("sessions_devicesImage"), PorterDuff$Mode.MULTIPLY));
        this.emptyLayout.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2));
        (this.textView1 = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.textView1.setGravity(17);
        this.textView1.setTextSize(1, 17.0f);
        this.textView1.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        if (this.currentType == 0) {
            this.textView1.setText((CharSequence)LocaleController.getString("NoOtherSessions", 2131559933));
        }
        else {
            this.textView1.setText((CharSequence)LocaleController.getString("NoOtherWebSessions", 2131559935));
        }
        this.emptyLayout.addView((View)this.textView1, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 17, 0, 16, 0, 0));
        (this.textView2 = new TextView(context)).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
        this.textView2.setGravity(17);
        this.textView2.setTextSize(1, 17.0f);
        this.textView2.setPadding(AndroidUtilities.dp(20.0f), 0, AndroidUtilities.dp(20.0f), 0);
        if (this.currentType == 0) {
            this.textView2.setText((CharSequence)LocaleController.getString("NoOtherSessionsInfo", 2131559934));
        }
        else {
            this.textView2.setText((CharSequence)LocaleController.getString("NoOtherWebSessionsInfo", 2131559936));
        }
        this.emptyLayout.addView((View)this.textView2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 17, 0, 14, 0, 0));
        (this.emptyView = new EmptyTextProgressView(context)).showProgress();
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 17));
        (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setEmptyView((View)this.emptyView);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$SessionsActivity$nGXAGNwZRPb8yKcWdHCTunuNa5s(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.newSessionReceived) {
            this.loadSessions(true);
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, HeaderCell.class, SessionCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.imageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "sessions_devicesImage"), new ThemeDescription((View)this.textView1, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.textView2, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteRedText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { SessionCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { SessionCell.class }, new String[] { "onlineTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { SessionCell.class }, new String[] { "onlineTextView" }, null, null, null, "windowBackgroundWhiteGrayText3"), new ThemeDescription((View)this.listView, 0, new Class[] { SessionCell.class }, new String[] { "detailTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { SessionCell.class }, new String[] { "detailExTextView" }, null, null, null, "windowBackgroundWhiteGrayText3") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.updateRows();
        this.loadSessions(false);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.newSessionReceived);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.newSessionReceived);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
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
            int access$600;
            if (SessionsActivity.this.loading) {
                access$600 = 0;
            }
            else {
                access$600 = SessionsActivity.this.rowCount;
            }
            return access$600;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == SessionsActivity.this.terminateAllSessionsRow) {
                return 0;
            }
            if (n == SessionsActivity.this.terminateAllSessionsDetailRow || n == SessionsActivity.this.otherSessionsTerminateDetail || n == SessionsActivity.this.passwordSessionsDetailRow) {
                return 1;
            }
            if (n == SessionsActivity.this.currentSessionSectionRow || n == SessionsActivity.this.otherSessionsSectionRow || n == SessionsActivity.this.passwordSessionsSectionRow) {
                return 2;
            }
            if (n == SessionsActivity.this.noOtherSessionsRow) {
                return 3;
            }
            if (n != SessionsActivity.this.currentSessionRow && (n < SessionsActivity.this.otherSessionsStartRow || n >= SessionsActivity.this.otherSessionsEndRow) && (n < SessionsActivity.this.passwordSessionsStartRow || n >= SessionsActivity.this.passwordSessionsEndRow)) {
                return 0;
            }
            return 4;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == SessionsActivity.this.terminateAllSessionsRow || (adapterPosition >= SessionsActivity.this.otherSessionsStartRow && adapterPosition < SessionsActivity.this.otherSessionsEndRow) || (adapterPosition >= SessionsActivity.this.passwordSessionsStartRow && adapterPosition < SessionsActivity.this.passwordSessionsEndRow);
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int statusBarHeight) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            final boolean b2 = false;
            final int n = 0;
            boolean b3 = false;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType != 3) {
                            final SessionCell sessionCell = (SessionCell)viewHolder.itemView;
                            if (statusBarHeight == SessionsActivity.this.currentSessionRow) {
                                final TLRPC.TL_authorization access$1600 = SessionsActivity.this.currentSession;
                                if (!SessionsActivity.this.sessions.isEmpty() || !SessionsActivity.this.passwordSessions.isEmpty()) {
                                    b3 = true;
                                }
                                sessionCell.setSession(access$1600, b3);
                            }
                            else if (statusBarHeight >= SessionsActivity.this.otherSessionsStartRow && statusBarHeight < SessionsActivity.this.otherSessionsEndRow) {
                                final TLObject tlObject = SessionsActivity.this.sessions.get(statusBarHeight - SessionsActivity.this.otherSessionsStartRow);
                                boolean b4 = b;
                                if (statusBarHeight != SessionsActivity.this.otherSessionsEndRow - 1) {
                                    b4 = true;
                                }
                                sessionCell.setSession(tlObject, b4);
                            }
                            else if (statusBarHeight >= SessionsActivity.this.passwordSessionsStartRow && statusBarHeight < SessionsActivity.this.passwordSessionsEndRow) {
                                final TLObject tlObject2 = SessionsActivity.this.passwordSessions.get(statusBarHeight - SessionsActivity.this.passwordSessionsStartRow);
                                boolean b5 = b2;
                                if (statusBarHeight != SessionsActivity.this.passwordSessionsEndRow - 1) {
                                    b5 = true;
                                }
                                sessionCell.setSession(tlObject2, b5);
                            }
                        }
                        else {
                            final ViewGroup$LayoutParams layoutParams = SessionsActivity.this.emptyLayout.getLayoutParams();
                            if (layoutParams != null) {
                                final int dp = AndroidUtilities.dp(220.0f);
                                final int y = AndroidUtilities.displaySize.y;
                                final int currentActionBarHeight = ActionBar.getCurrentActionBarHeight();
                                final int dp2 = AndroidUtilities.dp(128.0f);
                                statusBarHeight = n;
                                if (Build$VERSION.SDK_INT >= 21) {
                                    statusBarHeight = AndroidUtilities.statusBarHeight;
                                }
                                layoutParams.height = Math.max(dp, y - currentActionBarHeight - dp2 - statusBarHeight);
                                SessionsActivity.this.emptyLayout.setLayoutParams(layoutParams);
                            }
                        }
                    }
                    else {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (statusBarHeight == SessionsActivity.this.currentSessionSectionRow) {
                            headerCell.setText(LocaleController.getString("CurrentSession", 2131559181));
                        }
                        else if (statusBarHeight == SessionsActivity.this.otherSessionsSectionRow) {
                            if (SessionsActivity.this.currentType == 0) {
                                headerCell.setText(LocaleController.getString("OtherSessions", 2131560131));
                            }
                            else {
                                headerCell.setText(LocaleController.getString("OtherWebSessions", 2131560132));
                            }
                        }
                        else if (statusBarHeight == SessionsActivity.this.passwordSessionsSectionRow) {
                            headerCell.setText(LocaleController.getString("LoginAttempts", 2131559786));
                        }
                    }
                }
                else {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (statusBarHeight == SessionsActivity.this.terminateAllSessionsDetailRow) {
                        if (SessionsActivity.this.currentType == 0) {
                            textInfoPrivacyCell.setText(LocaleController.getString("ClearOtherSessionsHelp", 2131559111));
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("ClearOtherWebSessionsHelp", 2131559112));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                    else if (statusBarHeight == SessionsActivity.this.otherSessionsTerminateDetail) {
                        if (SessionsActivity.this.currentType == 0) {
                            textInfoPrivacyCell.setText(LocaleController.getString("TerminateSessionInfo", 2131560880));
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("TerminateWebSessionInfo", 2131560882));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                    else if (statusBarHeight == SessionsActivity.this.passwordSessionsDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("LoginAttemptsInfo", 2131559787));
                        if (SessionsActivity.this.otherSessionsTerminateDetail == -1) {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                        else {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        }
                    }
                }
            }
            else {
                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                if (statusBarHeight == SessionsActivity.this.terminateAllSessionsRow) {
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteRedText2"));
                    if (SessionsActivity.this.currentType == 0) {
                        textSettingsCell.setText(LocaleController.getString("TerminateAllSessions", 2131560878), false);
                    }
                    else {
                        textSettingsCell.setText(LocaleController.getString("TerminateAllWebSessions", 2131560879), false);
                    }
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object access$700;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            access$700 = new SessionCell(this.mContext, SessionsActivity.this.currentType);
                            ((View)access$700).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                        else {
                            access$700 = SessionsActivity.this.emptyLayout;
                        }
                    }
                    else {
                        access$700 = new HeaderCell(this.mContext);
                        ((View)access$700).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    access$700 = new TextInfoPrivacyCell(this.mContext);
                }
            }
            else {
                access$700 = new TextSettingsCell(this.mContext);
                ((View)access$700).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)access$700);
        }
    }
}
