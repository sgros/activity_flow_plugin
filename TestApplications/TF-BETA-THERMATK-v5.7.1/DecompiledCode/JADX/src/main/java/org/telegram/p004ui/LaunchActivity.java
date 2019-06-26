package org.telegram.p004ui;

import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StatFs;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.C1067R;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocaleController.LocaleInfo;
import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SendMessagesHelper.SendingMediaInfo;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.p004ui.ActionBar.ActionBarLayout;
import org.telegram.p004ui.ActionBar.ActionBarLayout.ActionBarLayoutDelegate;
import org.telegram.p004ui.ActionBar.AlertDialog;
import org.telegram.p004ui.ActionBar.AlertDialog.Builder;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.p004ui.ActionBar.DrawerLayoutContainer;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.ActionBar.Theme.ThemeInfo;
import org.telegram.p004ui.Adapters.DrawerLayoutAdapter;
import org.telegram.p004ui.Cells.DrawerAddCell;
import org.telegram.p004ui.Cells.DrawerUserCell;
import org.telegram.p004ui.Cells.LanguageCell;
import org.telegram.p004ui.Components.AlertsCreator;
import org.telegram.p004ui.Components.BlockingUpdateView;
import org.telegram.p004ui.Components.EmbedBottomSheet;
import org.telegram.p004ui.Components.LayoutHelper;
import org.telegram.p004ui.Components.PasscodeView;
import org.telegram.p004ui.Components.PipRoundVideoView;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.TermsOfServiceView;
import org.telegram.p004ui.Components.TermsOfServiceView.TermsOfServiceViewDelegate;
import org.telegram.p004ui.Components.ThemeEditorView;
import org.telegram.p004ui.Components.UpdateAppAlertDialog;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.p004ui.WallpapersListActivity.ColorWallpaper;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.InputGame;
import org.telegram.tgnet.TLRPC.LangPackString;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.TL_account_authorizationForm;
import org.telegram.tgnet.TLRPC.TL_account_getAuthorizationForm;
import org.telegram.tgnet.TLRPC.TL_account_getPassword;
import org.telegram.tgnet.TLRPC.TL_account_password;
import org.telegram.tgnet.TLRPC.TL_channels_getChannels;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_help_appUpdate;
import org.telegram.tgnet.TLRPC.TL_help_deepLinkInfo;
import org.telegram.tgnet.TLRPC.TL_help_getAppUpdate;
import org.telegram.tgnet.TLRPC.TL_help_termsOfService;
import org.telegram.tgnet.TLRPC.TL_inputChannel;
import org.telegram.tgnet.TLRPC.TL_inputGameShortName;
import org.telegram.tgnet.TLRPC.TL_inputMediaGame;
import org.telegram.tgnet.TLRPC.TL_langPackLanguage;
import org.telegram.tgnet.TLRPC.TL_langpack_getStrings;
import org.telegram.tgnet.TLRPC.TL_messages_chats;
import org.telegram.tgnet.TLRPC.TL_wallPaper;
import org.telegram.tgnet.TLRPC.TL_wallPaperSettings;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.Updates;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.Vector;

/* renamed from: org.telegram.ui.LaunchActivity */
public class LaunchActivity extends Activity implements ActionBarLayoutDelegate, NotificationCenterDelegate, DialogsActivityDelegate {
    private static ArrayList<BaseFragment> layerFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> mainFragmentsStack = new ArrayList();
    private static ArrayList<BaseFragment> rightFragmentsStack = new ArrayList();
    private ActionBarLayout actionBarLayout;
    private View backgroundTablet;
    private BlockingUpdateView blockingUpdateView;
    private ArrayList<User> contactsToSend;
    private Uri contactsToSendUri;
    private int currentAccount;
    private int currentConnectionState;
    private String documentsMimeType;
    private ArrayList<String> documentsOriginalPathsArray;
    private ArrayList<String> documentsPathsArray;
    private ArrayList<Uri> documentsUrisArray;
    private DrawerLayoutAdapter drawerLayoutAdapter;
    protected DrawerLayoutContainer drawerLayoutContainer;
    private HashMap<String, String> englishLocaleStrings;
    private boolean finished;
    private ActionBarLayout layersActionBarLayout;
    private boolean loadingLocaleDialog;
    private AlertDialog localeDialog;
    private final Pattern locationRegex = Pattern.compile("geo: ?(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)(,|\\?z=)(-?\\d+)");
    private Runnable lockRunnable;
    private OnGlobalLayoutListener onGlobalLayoutListener;
    private Intent passcodeSaveIntent;
    private boolean passcodeSaveIntentIsNew;
    private boolean passcodeSaveIntentIsRestore;
    private PasscodeView passcodeView;
    private ArrayList<SendingMediaInfo> photoPathsArray;
    private AlertDialog proxyErrorDialog;
    private ActionBarLayout rightActionBarLayout;
    private Location sendingLocation;
    private String sendingText;
    private FrameLayout shadowTablet;
    private FrameLayout shadowTabletSide;
    private RecyclerListView sideMenu;
    private HashMap<String, String> systemLocaleStrings;
    private boolean tabletFullSize;
    private TermsOfServiceView termsOfServiceView;
    private String videoPath;
    private ActionMode visibleActionMode;
    private AlertDialog visibleDialog;

    /* renamed from: org.telegram.ui.LaunchActivity$4 */
    class C30644 implements OnDismissListener {
        C30644() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            if (LaunchActivity.this.visibleDialog != null) {
                if (LaunchActivity.this.visibleDialog == LaunchActivity.this.localeDialog) {
                    try {
                        Toast.makeText(LaunchActivity.this, LaunchActivity.this.getStringForLanguageAlert(LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals("en") ? LaunchActivity.this.englishLocaleStrings : LaunchActivity.this.systemLocaleStrings, "ChangeLanguageLater", C1067R.string.ChangeLanguageLater), 1).show();
                    } catch (Exception e) {
                        FileLog.m30e(e);
                    }
                    LaunchActivity.this.localeDialog = null;
                } else if (LaunchActivity.this.visibleDialog == LaunchActivity.this.proxyErrorDialog) {
                    MessagesController.getGlobalMainSettings();
                    Editor edit = MessagesController.getGlobalMainSettings().edit();
                    edit.putBoolean("proxy_enabled", false);
                    edit.putBoolean("proxy_enabled_calls", false);
                    edit.commit();
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
                    ConnectionsManager.setProxySettings(false, "", 1080, "", "", "");
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged, new Object[0]);
                    LaunchActivity.this.proxyErrorDialog = null;
                }
            }
            LaunchActivity.this.visibleDialog = null;
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$5 */
    class C30655 implements Runnable {
        C30655() {
        }

        public void run() {
            if (LaunchActivity.this.lockRunnable == this) {
                if (AndroidUtilities.needShowPasscode(true)) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.m27d("lock app");
                    }
                    LaunchActivity.this.showPasscodeActivity();
                } else if (BuildVars.LOGS_ENABLED) {
                    FileLog.m27d("didn't pass lock check");
                }
                LaunchActivity.this.lockRunnable = null;
            }
        }
    }

    /* renamed from: org.telegram.ui.LaunchActivity$3 */
    class C42093 implements TermsOfServiceViewDelegate {
        C42093() {
        }

        public void onAcceptTerms(int i) {
            UserConfig.getInstance(i).unacceptedTermsOfService = null;
            UserConfig.getInstance(i).saveConfig(false);
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            LaunchActivity.this.termsOfServiceView.setVisibility(8);
        }

        public void onDeclineTerms(int i) {
            LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            LaunchActivity.this.termsOfServiceView.setVisibility(8);
        }
    }

    static /* synthetic */ void lambda$onCreate$1(View view) {
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009d  */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x0109 */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:40|41|42|43|44) */
    /* JADX WARNING: Missing block: B:111:0x044a, code skipped:
            r0 = -1;
     */
    /* JADX WARNING: Missing block: B:112:0x044b, code skipped:
            if (r0 == 0) goto L_0x04b4;
     */
    /* JADX WARNING: Missing block: B:113:0x044d, code skipped:
            if (r0 == 1) goto L_0x04a5;
     */
    /* JADX WARNING: Missing block: B:114:0x044f, code skipped:
            if (r0 == 2) goto L_0x0491;
     */
    /* JADX WARNING: Missing block: B:115:0x0451, code skipped:
            if (r0 == 3) goto L_0x047d;
     */
    /* JADX WARNING: Missing block: B:116:0x0453, code skipped:
            if (r0 == 4) goto L_0x0469;
     */
    /* JADX WARNING: Missing block: B:118:0x0456, code skipped:
            if (r0 == 5) goto L_0x045a;
     */
    /* JADX WARNING: Missing block: B:120:0x045a, code skipped:
            r0 = new org.telegram.p004ui.WallpapersListActivity(0);
            r11.actionBarLayout.addFragmentToStack(r0);
            r0.restoreSelfArgs(r12);
     */
    /* JADX WARNING: Missing block: B:121:0x0469, code skipped:
            if (r3 == null) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:122:0x046b, code skipped:
            r0 = new org.telegram.p004ui.ProfileActivity(r3);
     */
    /* JADX WARNING: Missing block: B:123:0x0476, code skipped:
            if (r11.actionBarLayout.addFragmentToStack(r0) == false) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:124:0x0478, code skipped:
            r0.restoreSelfArgs(r12);
     */
    /* JADX WARNING: Missing block: B:125:0x047d, code skipped:
            if (r3 == null) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:126:0x047f, code skipped:
            r0 = new org.telegram.p004ui.ChannelCreateActivity(r3);
     */
    /* JADX WARNING: Missing block: B:127:0x048a, code skipped:
            if (r11.actionBarLayout.addFragmentToStack(r0) == false) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:128:0x048c, code skipped:
            r0.restoreSelfArgs(r12);
     */
    /* JADX WARNING: Missing block: B:129:0x0491, code skipped:
            if (r3 == null) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:130:0x0493, code skipped:
            r0 = new org.telegram.p004ui.GroupCreateFinalActivity(r3);
     */
    /* JADX WARNING: Missing block: B:131:0x049e, code skipped:
            if (r11.actionBarLayout.addFragmentToStack(r0) == false) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:132:0x04a0, code skipped:
            r0.restoreSelfArgs(r12);
     */
    /* JADX WARNING: Missing block: B:133:0x04a5, code skipped:
            r0 = new org.telegram.p004ui.SettingsActivity();
            r11.actionBarLayout.addFragmentToStack(r0);
            r0.restoreSelfArgs(r12);
     */
    /* JADX WARNING: Missing block: B:134:0x04b4, code skipped:
            if (r3 == null) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:135:0x04b6, code skipped:
            r0 = new org.telegram.p004ui.ChatActivity(r3);
     */
    /* JADX WARNING: Missing block: B:136:0x04c1, code skipped:
            if (r11.actionBarLayout.addFragmentToStack(r0) == false) goto L_0x0533;
     */
    /* JADX WARNING: Missing block: B:137:0x04c3, code skipped:
            r0.restoreSelfArgs(r12);
     */
    public void onCreate(android.os.Bundle r12) {
        /*
        r11 = this;
        org.telegram.messenger.ApplicationLoader.postInitApplication();
        r0 = r11.getResources();
        r0 = r0.getConfiguration();
        org.telegram.messenger.AndroidUtilities.checkDisplaySize(r11, r0);
        r0 = org.telegram.messenger.UserConfig.selectedAccount;
        r11.currentAccount = r0;
        r0 = r11.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.isClientActivated();
        r1 = 1;
        r2 = 0;
        if (r0 != 0) goto L_0x00e8;
    L_0x0020:
        r0 = r11.getIntent();
        if (r0 == 0) goto L_0x0088;
    L_0x0026:
        r3 = r0.getAction();
        if (r3 == 0) goto L_0x0088;
    L_0x002c:
        r3 = r0.getAction();
        r4 = "android.intent.action.SEND";
        r3 = r4.equals(r3);
        if (r3 != 0) goto L_0x0081;
    L_0x0038:
        r3 = r0.getAction();
        r4 = "android.intent.action.SEND_MULTIPLE";
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x0045;
    L_0x0044:
        goto L_0x0081;
    L_0x0045:
        r3 = r0.getAction();
        r4 = "android.intent.action.VIEW";
        r3 = r4.equals(r3);
        if (r3 == 0) goto L_0x0088;
    L_0x0051:
        r3 = r0.getData();
        if (r3 == 0) goto L_0x0088;
    L_0x0057:
        r3 = r3.toString();
        r3 = r3.toLowerCase();
        r4 = "tg:proxy";
        r4 = r3.startsWith(r4);
        if (r4 != 0) goto L_0x007f;
    L_0x0067:
        r4 = "tg://proxy";
        r4 = r3.startsWith(r4);
        if (r4 != 0) goto L_0x007f;
    L_0x006f:
        r4 = "tg:socks";
        r4 = r3.startsWith(r4);
        if (r4 != 0) goto L_0x007f;
    L_0x0077:
        r4 = "tg://socks";
        r3 = r3.startsWith(r4);
        if (r3 == 0) goto L_0x0088;
    L_0x007f:
        r3 = 1;
        goto L_0x0089;
    L_0x0081:
        super.onCreate(r12);
        r11.finish();
        return;
    L_0x0088:
        r3 = 0;
    L_0x0089:
        r4 = org.telegram.messenger.MessagesController.getGlobalMainSettings();
        r5 = 0;
        r7 = "intro_crashed_time";
        r8 = r4.getLong(r7, r5);
        r10 = "fromIntro";
        r10 = r0.getBooleanExtra(r10, r2);
        if (r10 == 0) goto L_0x00a8;
    L_0x009d:
        r4 = r4.edit();
        r4 = r4.putLong(r7, r5);
        r4.commit();
    L_0x00a8:
        if (r3 != 0) goto L_0x00e8;
    L_0x00aa:
        r3 = java.lang.System.currentTimeMillis();
        r8 = r8 - r3;
        r3 = java.lang.Math.abs(r8);
        r5 = 120000; // 0x1d4c0 float:1.68156E-40 double:5.9288E-319;
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r7 < 0) goto L_0x00e8;
    L_0x00ba:
        if (r0 == 0) goto L_0x00e8;
    L_0x00bc:
        if (r10 != 0) goto L_0x00e8;
    L_0x00be:
        r3 = org.telegram.messenger.ApplicationLoader.applicationContext;
        r4 = "logininfo2";
        r3 = r3.getSharedPreferences(r4, r2);
        r3 = r3.getAll();
        r3 = r3.isEmpty();
        if (r3 == 0) goto L_0x00e8;
    L_0x00d0:
        r1 = new android.content.Intent;
        r2 = org.telegram.p004ui.IntroActivity.class;
        r1.<init>(r11, r2);
        r0 = r0.getData();
        r1.setData(r0);
        r11.startActivity(r1);
        super.onCreate(r12);
        r11.finish();
        return;
    L_0x00e8:
        r11.requestWindowFeature(r1);
        r0 = 2131624206; // 0x7f0e010e float:1.8875585E38 double:1.05316229E-314;
        r11.setTheme(r0);
        r0 = android.os.Build.VERSION.SDK_INT;
        r3 = 21;
        r4 = 0;
        if (r0 < r3) goto L_0x0112;
    L_0x00f8:
        r0 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r3 = new android.app.ActivityManager$TaskDescription;	 Catch:{ Exception -> 0x0109 }
        r5 = "actionBarDefault";
        r5 = org.telegram.p004ui.ActionBar.Theme.getColor(r5);	 Catch:{ Exception -> 0x0109 }
        r5 = r5 | r0;
        r3.<init>(r4, r4, r5);	 Catch:{ Exception -> 0x0109 }
        r11.setTaskDescription(r3);	 Catch:{ Exception -> 0x0109 }
    L_0x0109:
        r3 = r11.getWindow();	 Catch:{ Exception -> 0x0111 }
        r3.setNavigationBarColor(r0);	 Catch:{ Exception -> 0x0111 }
        goto L_0x0112;
    L_0x0112:
        r0 = r11.getWindow();
        r3 = 2131165891; // 0x7f0702c3 float:1.7946012E38 double:1.0529358523E-314;
        r0.setBackgroundDrawableResource(r3);
        r0 = org.telegram.messenger.SharedConfig.passcodeHash;
        r0 = r0.length();
        if (r0 <= 0) goto L_0x0136;
    L_0x0124:
        r0 = org.telegram.messenger.SharedConfig.allowScreenCapture;
        if (r0 != 0) goto L_0x0136;
    L_0x0128:
        r0 = r11.getWindow();	 Catch:{ Exception -> 0x0132 }
        r3 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r0.setFlags(r3, r3);	 Catch:{ Exception -> 0x0132 }
        goto L_0x0136;
    L_0x0132:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0136:
        super.onCreate(r12);
        r0 = android.os.Build.VERSION.SDK_INT;
        r3 = 24;
        if (r0 < r3) goto L_0x0145;
    L_0x013f:
        r0 = r11.isInMultiWindowMode();
        org.telegram.messenger.AndroidUtilities.isInMultiwindow = r0;
    L_0x0145:
        org.telegram.p004ui.ActionBar.Theme.createChatResources(r11, r2);
        r0 = org.telegram.messenger.SharedConfig.passcodeHash;
        r0 = r0.length();
        if (r0 == 0) goto L_0x0160;
    L_0x0150:
        r0 = org.telegram.messenger.SharedConfig.appLocked;
        if (r0 == 0) goto L_0x0160;
    L_0x0154:
        r0 = r11.currentAccount;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
        r0 = r0.getCurrentTime();
        org.telegram.messenger.SharedConfig.lastPauseTime = r0;
    L_0x0160:
        r0 = r11.getResources();
        r3 = "status_bar_height";
        r5 = "dimen";
        r6 = "android";
        r0 = r0.getIdentifier(r3, r5, r6);
        if (r0 <= 0) goto L_0x017a;
    L_0x0170:
        r3 = r11.getResources();
        r0 = r3.getDimensionPixelSize(r0);
        org.telegram.messenger.AndroidUtilities.statusBarHeight = r0;
    L_0x017a:
        r0 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r0.<init>(r11);
        r11.actionBarLayout = r0;
        r0 = new org.telegram.ui.ActionBar.DrawerLayoutContainer;
        r0.<init>(r11);
        r11.drawerLayoutContainer = r0;
        r0 = r11.drawerLayoutContainer;
        r3 = "windowBackgroundWhite";
        r3 = org.telegram.p004ui.ActionBar.Theme.getColor(r3);
        r0.setBehindKeyboardColor(r3);
        r0 = r11.drawerLayoutContainer;
        r3 = new android.view.ViewGroup$LayoutParams;
        r5 = -1;
        r3.<init>(r5, r5);
        r11.setContentView(r0, r3);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        r3 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        if (r0 == 0) goto L_0x0294;
    L_0x01a6:
        r0 = r11.getWindow();
        r6 = 16;
        r0.setSoftInputMode(r6);
        r0 = new org.telegram.ui.LaunchActivity$1;
        r0.<init>(r11);
        r6 = r11.drawerLayoutContainer;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r5, r3);
        r6.addView(r0, r7);
        r6 = new android.view.View;
        r6.<init>(r11);
        r11.backgroundTablet = r6;
        r6 = r11.getResources();
        r7 = 2131165338; // 0x7f07009a float:1.794489E38 double:1.052935579E-314;
        r6 = r6.getDrawable(r7);
        r6 = (android.graphics.drawable.BitmapDrawable) r6;
        r7 = android.graphics.Shader.TileMode.REPEAT;
        r6.setTileModeXY(r7, r7);
        r7 = r11.backgroundTablet;
        r7.setBackgroundDrawable(r6);
        r6 = r11.backgroundTablet;
        r7 = org.telegram.p004ui.Components.LayoutHelper.createRelative(r5, r5);
        r0.addView(r6, r7);
        r6 = r11.actionBarLayout;
        r0.addView(r6);
        r6 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r6.<init>(r11);
        r11.rightActionBarLayout = r6;
        r6 = r11.rightActionBarLayout;
        r7 = rightFragmentsStack;
        r6.init(r7);
        r6 = r11.rightActionBarLayout;
        r6.setDelegate(r11);
        r6 = r11.rightActionBarLayout;
        r0.addView(r6);
        r6 = new android.widget.FrameLayout;
        r6.<init>(r11);
        r11.shadowTabletSide = r6;
        r6 = r11.shadowTabletSide;
        r7 = 1076449908; // 0x40295274 float:2.6456575 double:5.31836919E-315;
        r6.setBackgroundColor(r7);
        r6 = r11.shadowTabletSide;
        r0.addView(r6);
        r6 = new android.widget.FrameLayout;
        r6.<init>(r11);
        r11.shadowTablet = r6;
        r6 = r11.shadowTablet;
        r7 = layerFragmentsStack;
        r7 = r7.isEmpty();
        r8 = 8;
        if (r7 == 0) goto L_0x022b;
    L_0x0228:
        r7 = 8;
        goto L_0x022c;
    L_0x022b:
        r7 = 0;
    L_0x022c:
        r6.setVisibility(r7);
        r6 = r11.shadowTablet;
        r7 = 2130706432; // 0x7f000000 float:1.7014118E38 double:1.0527088494E-314;
        r6.setBackgroundColor(r7);
        r6 = r11.shadowTablet;
        r0.addView(r6);
        r6 = r11.shadowTablet;
        r7 = new org.telegram.ui.-$$Lambda$LaunchActivity$KFZR9bOIUYM1vrC9qoPrRupqDO4;
        r7.<init>(r11);
        r6.setOnTouchListener(r7);
        r6 = r11.shadowTablet;
        r7 = org.telegram.p004ui.C1574-$$Lambda$LaunchActivity$OJponKw8R53ezoQT8H7udVOmkKQ.INSTANCE;
        r6.setOnClickListener(r7);
        r6 = new org.telegram.ui.ActionBar.ActionBarLayout;
        r6.<init>(r11);
        r11.layersActionBarLayout = r6;
        r6 = r11.layersActionBarLayout;
        r6.setRemoveActionBarExtraHeight(r1);
        r6 = r11.layersActionBarLayout;
        r7 = r11.shadowTablet;
        r6.setBackgroundView(r7);
        r6 = r11.layersActionBarLayout;
        r6.setUseAlphaAnimations(r1);
        r6 = r11.layersActionBarLayout;
        r7 = 2131165322; // 0x7f07008a float:1.7944858E38 double:1.052935571E-314;
        r6.setBackgroundResource(r7);
        r6 = r11.layersActionBarLayout;
        r7 = layerFragmentsStack;
        r6.init(r7);
        r6 = r11.layersActionBarLayout;
        r6.setDelegate(r11);
        r6 = r11.layersActionBarLayout;
        r7 = r11.drawerLayoutContainer;
        r6.setDrawerLayoutContainer(r7);
        r6 = r11.layersActionBarLayout;
        r7 = layerFragmentsStack;
        r7 = r7.isEmpty();
        if (r7 == 0) goto L_0x028a;
    L_0x0289:
        goto L_0x028b;
    L_0x028a:
        r8 = 0;
    L_0x028b:
        r6.setVisibility(r8);
        r6 = r11.layersActionBarLayout;
        r0.addView(r6);
        goto L_0x02a0;
    L_0x0294:
        r0 = r11.drawerLayoutContainer;
        r6 = r11.actionBarLayout;
        r7 = new android.view.ViewGroup$LayoutParams;
        r7.<init>(r5, r5);
        r0.addView(r6, r7);
    L_0x02a0:
        r0 = new org.telegram.ui.Components.RecyclerListView;
        r0.<init>(r11);
        r11.sideMenu = r0;
        r0 = r11.sideMenu;
        r0 = r0.getItemAnimator();
        r0 = (androidx.recyclerview.widget.DefaultItemAnimator) r0;
        r0.setDelayAnimations(r2);
        r0 = r11.sideMenu;
        r6 = "chats_menuBackground";
        r6 = org.telegram.p004ui.ActionBar.Theme.getColor(r6);
        r0.setBackgroundColor(r6);
        r0 = r11.sideMenu;
        r6 = new androidx.recyclerview.widget.LinearLayoutManager;
        r6.<init>(r11, r1, r2);
        r0.setLayoutManager(r6);
        r0 = r11.sideMenu;
        r6 = new org.telegram.ui.Adapters.DrawerLayoutAdapter;
        r6.<init>(r11);
        r11.drawerLayoutAdapter = r6;
        r0.setAdapter(r6);
        r0 = r11.drawerLayoutContainer;
        r6 = r11.sideMenu;
        r0.setDrawerLayout(r6);
        r0 = r11.sideMenu;
        r0 = r0.getLayoutParams();
        r0 = (android.widget.FrameLayout.LayoutParams) r0;
        r6 = org.telegram.messenger.AndroidUtilities.getRealScreenSize();
        r7 = org.telegram.messenger.AndroidUtilities.isTablet();
        r8 = 1134559232; // 0x43a00000 float:320.0 double:5.605467397E-315;
        if (r7 == 0) goto L_0x02f3;
    L_0x02ee:
        r6 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        goto L_0x030a;
    L_0x02f3:
        r7 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r8 = r6.x;
        r6 = r6.y;
        r6 = java.lang.Math.min(r8, r6);
        r8 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r8 = org.telegram.messenger.AndroidUtilities.m26dp(r8);
        r6 = r6 - r8;
        r6 = java.lang.Math.min(r7, r6);
    L_0x030a:
        r0.width = r6;
        r0.height = r5;
        r6 = r11.sideMenu;
        r6.setLayoutParams(r0);
        r0 = r11.sideMenu;
        r6 = new org.telegram.ui.-$$Lambda$LaunchActivity$dOtJNBBcNQv2FwIcA_NKr5dzUI0;
        r6.<init>(r11);
        r0.setOnItemClickListener(r6);
        r0 = r11.drawerLayoutContainer;
        r6 = r11.actionBarLayout;
        r0.setParentActionBarLayout(r6);
        r0 = r11.actionBarLayout;
        r6 = r11.drawerLayoutContainer;
        r0.setDrawerLayoutContainer(r6);
        r0 = r11.actionBarLayout;
        r6 = mainFragmentsStack;
        r0.init(r6);
        r0 = r11.actionBarLayout;
        r0.setDelegate(r11);
        org.telegram.p004ui.ActionBar.Theme.loadWallpaper();
        r0 = new org.telegram.ui.Components.PasscodeView;
        r0.<init>(r11);
        r11.passcodeView = r0;
        r0 = r11.drawerLayoutContainer;
        r6 = r11.passcodeView;
        r3 = org.telegram.p004ui.Components.LayoutHelper.createFrame(r5, r3);
        r0.addView(r6, r3);
        r11.checkCurrentAccount();
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r6 = new java.lang.Object[r1];
        r6[r2] = r11;
        r0.postNotificationName(r3, r6);
        r0 = r11.currentAccount;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
        r0 = r0.getConnectionState();
        r11.currentConnectionState = r0;
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.needShowAlert;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.reloadInterface;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.suggestedLangpack;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.didSetNewTheme;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.needSetDayNightTheme;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.closeOtherAppActivities;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.didSetPasscode;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.didSetNewWallpapper;
        r0.addObserver(r11, r3);
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r3 = org.telegram.messenger.NotificationCenter.notificationsCountUpdated;
        r0.addObserver(r11, r3);
        r0 = r11.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x04cc;
    L_0x03c3:
        r0 = r11.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.isClientActivated();
        if (r0 != 0) goto L_0x03df;
    L_0x03cf:
        r0 = r11.actionBarLayout;
        r3 = new org.telegram.ui.LoginActivity;
        r3.<init>();
        r0.addFragmentToStack(r3);
        r0 = r11.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r2, r2);
        goto L_0x03f3;
    L_0x03df:
        r0 = new org.telegram.ui.DialogsActivity;
        r0.<init>(r4);
        r3 = r11.sideMenu;
        r0.setSideMenu(r3);
        r3 = r11.actionBarLayout;
        r3.addFragmentToStack(r0);
        r0 = r11.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r1, r2);
    L_0x03f3:
        if (r12 == 0) goto L_0x0533;
    L_0x03f5:
        r0 = "fragment";
        r0 = r12.getString(r0);	 Catch:{ Exception -> 0x04c7 }
        if (r0 == 0) goto L_0x0533;
    L_0x03fd:
        r3 = "args";
        r3 = r12.getBundle(r3);	 Catch:{ Exception -> 0x04c7 }
        r4 = r0.hashCode();	 Catch:{ Exception -> 0x04c7 }
        r6 = 4;
        r7 = 3;
        r8 = 2;
        switch(r4) {
            case -1529105743: goto L_0x0440;
            case -1349522494: goto L_0x0436;
            case 3052376: goto L_0x042c;
            case 98629247: goto L_0x0422;
            case 738950403: goto L_0x0418;
            case 1434631203: goto L_0x040e;
            default: goto L_0x040d;
        };	 Catch:{ Exception -> 0x04c7 }
    L_0x040d:
        goto L_0x044a;
    L_0x040e:
        r4 = "settings";
        r0 = r0.equals(r4);	 Catch:{ Exception -> 0x04c7 }
        if (r0 == 0) goto L_0x044a;
    L_0x0416:
        r0 = 1;
        goto L_0x044b;
    L_0x0418:
        r4 = "channel";
        r0 = r0.equals(r4);	 Catch:{ Exception -> 0x04c7 }
        if (r0 == 0) goto L_0x044a;
    L_0x0420:
        r0 = 3;
        goto L_0x044b;
    L_0x0422:
        r4 = "group";
        r0 = r0.equals(r4);	 Catch:{ Exception -> 0x04c7 }
        if (r0 == 0) goto L_0x044a;
    L_0x042a:
        r0 = 2;
        goto L_0x044b;
    L_0x042c:
        r4 = "chat";
        r0 = r0.equals(r4);	 Catch:{ Exception -> 0x04c7 }
        if (r0 == 0) goto L_0x044a;
    L_0x0434:
        r0 = 0;
        goto L_0x044b;
    L_0x0436:
        r4 = "chat_profile";
        r0 = r0.equals(r4);	 Catch:{ Exception -> 0x04c7 }
        if (r0 == 0) goto L_0x044a;
    L_0x043e:
        r0 = 4;
        goto L_0x044b;
    L_0x0440:
        r4 = "wallpapers";
        r0 = r0.equals(r4);	 Catch:{ Exception -> 0x04c7 }
        if (r0 == 0) goto L_0x044a;
    L_0x0448:
        r0 = 5;
        goto L_0x044b;
    L_0x044a:
        r0 = -1;
    L_0x044b:
        if (r0 == 0) goto L_0x04b4;
    L_0x044d:
        if (r0 == r1) goto L_0x04a5;
    L_0x044f:
        if (r0 == r8) goto L_0x0491;
    L_0x0451:
        if (r0 == r7) goto L_0x047d;
    L_0x0453:
        if (r0 == r6) goto L_0x0469;
    L_0x0455:
        r3 = 5;
        if (r0 == r3) goto L_0x045a;
    L_0x0458:
        goto L_0x0533;
    L_0x045a:
        r0 = new org.telegram.ui.WallpapersListActivity;	 Catch:{ Exception -> 0x04c7 }
        r0.<init>(r2);	 Catch:{ Exception -> 0x04c7 }
        r3 = r11.actionBarLayout;	 Catch:{ Exception -> 0x04c7 }
        r3.addFragmentToStack(r0);	 Catch:{ Exception -> 0x04c7 }
        r0.restoreSelfArgs(r12);	 Catch:{ Exception -> 0x04c7 }
        goto L_0x0533;
    L_0x0469:
        if (r3 == 0) goto L_0x0533;
    L_0x046b:
        r0 = new org.telegram.ui.ProfileActivity;	 Catch:{ Exception -> 0x04c7 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x04c7 }
        r3 = r11.actionBarLayout;	 Catch:{ Exception -> 0x04c7 }
        r3 = r3.addFragmentToStack(r0);	 Catch:{ Exception -> 0x04c7 }
        if (r3 == 0) goto L_0x0533;
    L_0x0478:
        r0.restoreSelfArgs(r12);	 Catch:{ Exception -> 0x04c7 }
        goto L_0x0533;
    L_0x047d:
        if (r3 == 0) goto L_0x0533;
    L_0x047f:
        r0 = new org.telegram.ui.ChannelCreateActivity;	 Catch:{ Exception -> 0x04c7 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x04c7 }
        r3 = r11.actionBarLayout;	 Catch:{ Exception -> 0x04c7 }
        r3 = r3.addFragmentToStack(r0);	 Catch:{ Exception -> 0x04c7 }
        if (r3 == 0) goto L_0x0533;
    L_0x048c:
        r0.restoreSelfArgs(r12);	 Catch:{ Exception -> 0x04c7 }
        goto L_0x0533;
    L_0x0491:
        if (r3 == 0) goto L_0x0533;
    L_0x0493:
        r0 = new org.telegram.ui.GroupCreateFinalActivity;	 Catch:{ Exception -> 0x04c7 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x04c7 }
        r3 = r11.actionBarLayout;	 Catch:{ Exception -> 0x04c7 }
        r3 = r3.addFragmentToStack(r0);	 Catch:{ Exception -> 0x04c7 }
        if (r3 == 0) goto L_0x0533;
    L_0x04a0:
        r0.restoreSelfArgs(r12);	 Catch:{ Exception -> 0x04c7 }
        goto L_0x0533;
    L_0x04a5:
        r0 = new org.telegram.ui.SettingsActivity;	 Catch:{ Exception -> 0x04c7 }
        r0.<init>();	 Catch:{ Exception -> 0x04c7 }
        r3 = r11.actionBarLayout;	 Catch:{ Exception -> 0x04c7 }
        r3.addFragmentToStack(r0);	 Catch:{ Exception -> 0x04c7 }
        r0.restoreSelfArgs(r12);	 Catch:{ Exception -> 0x04c7 }
        goto L_0x0533;
    L_0x04b4:
        if (r3 == 0) goto L_0x0533;
    L_0x04b6:
        r0 = new org.telegram.ui.ChatActivity;	 Catch:{ Exception -> 0x04c7 }
        r0.<init>(r3);	 Catch:{ Exception -> 0x04c7 }
        r3 = r11.actionBarLayout;	 Catch:{ Exception -> 0x04c7 }
        r3 = r3.addFragmentToStack(r0);	 Catch:{ Exception -> 0x04c7 }
        if (r3 == 0) goto L_0x0533;
    L_0x04c3:
        r0.restoreSelfArgs(r12);	 Catch:{ Exception -> 0x04c7 }
        goto L_0x0533;
    L_0x04c7:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
        goto L_0x0533;
    L_0x04cc:
        r0 = r11.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.get(r2);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r3 = r0 instanceof org.telegram.p004ui.DialogsActivity;
        if (r3 == 0) goto L_0x04e1;
    L_0x04da:
        r0 = (org.telegram.p004ui.DialogsActivity) r0;
        r3 = r11.sideMenu;
        r0.setSideMenu(r3);
    L_0x04e1:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x0516;
    L_0x04e7:
        r0 = r11.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 > r1) goto L_0x04fd;
    L_0x04f1:
        r0 = r11.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x04fd;
    L_0x04fb:
        r0 = 1;
        goto L_0x04fe;
    L_0x04fd:
        r0 = 0;
    L_0x04fe:
        r3 = r11.layersActionBarLayout;
        r3 = r3.fragmentsStack;
        r3 = r3.size();
        if (r3 != r1) goto L_0x0517;
    L_0x0508:
        r3 = r11.layersActionBarLayout;
        r3 = r3.fragmentsStack;
        r3 = r3.get(r2);
        r3 = r3 instanceof org.telegram.p004ui.LoginActivity;
        if (r3 == 0) goto L_0x0517;
    L_0x0514:
        r0 = 0;
        goto L_0x0517;
    L_0x0516:
        r0 = 1;
    L_0x0517:
        r3 = r11.actionBarLayout;
        r3 = r3.fragmentsStack;
        r3 = r3.size();
        if (r3 != r1) goto L_0x052e;
    L_0x0521:
        r3 = r11.actionBarLayout;
        r3 = r3.fragmentsStack;
        r3 = r3.get(r2);
        r3 = r3 instanceof org.telegram.p004ui.LoginActivity;
        if (r3 == 0) goto L_0x052e;
    L_0x052d:
        r0 = 0;
    L_0x052e:
        r3 = r11.drawerLayoutContainer;
        r3.setAllowOpenDrawer(r0, r2);
    L_0x0533:
        r11.checkLayout();
        r0 = r11.getIntent();
        if (r12 == 0) goto L_0x053e;
    L_0x053c:
        r12 = 1;
        goto L_0x053f;
    L_0x053e:
        r12 = 0;
    L_0x053f:
        r11.handleIntent(r0, r2, r12, r2);
        r12 = android.os.Build.DISPLAY;	 Catch:{ Exception -> 0x0583 }
        r0 = android.os.Build.USER;	 Catch:{ Exception -> 0x0583 }
        r2 = "";
        if (r12 == 0) goto L_0x054f;
    L_0x054a:
        r12 = r12.toLowerCase();	 Catch:{ Exception -> 0x0583 }
        goto L_0x0550;
    L_0x054f:
        r12 = r2;
    L_0x0550:
        if (r0 == 0) goto L_0x0556;
    L_0x0552:
        r2 = r12.toLowerCase();	 Catch:{ Exception -> 0x0583 }
    L_0x0556:
        r0 = "flyme";
        r12 = r12.contains(r0);	 Catch:{ Exception -> 0x0583 }
        if (r12 != 0) goto L_0x0566;
    L_0x055e:
        r12 = "flyme";
        r12 = r2.contains(r12);	 Catch:{ Exception -> 0x0583 }
        if (r12 == 0) goto L_0x0587;
    L_0x0566:
        org.telegram.messenger.AndroidUtilities.incorrectDisplaySizeFix = r1;	 Catch:{ Exception -> 0x0583 }
        r12 = r11.getWindow();	 Catch:{ Exception -> 0x0583 }
        r12 = r12.getDecorView();	 Catch:{ Exception -> 0x0583 }
        r12 = r12.getRootView();	 Catch:{ Exception -> 0x0583 }
        r0 = r12.getViewTreeObserver();	 Catch:{ Exception -> 0x0583 }
        r2 = new org.telegram.ui.-$$Lambda$LaunchActivity$KOGX7F1UQC0GXDka6tTbIrU0Wk0;	 Catch:{ Exception -> 0x0583 }
        r2.<init>(r12);	 Catch:{ Exception -> 0x0583 }
        r11.onGlobalLayoutListener = r2;	 Catch:{ Exception -> 0x0583 }
        r0.addOnGlobalLayoutListener(r2);	 Catch:{ Exception -> 0x0583 }
        goto L_0x0587;
    L_0x0583:
        r12 = move-exception;
        org.telegram.messenger.FileLog.m30e(r12);
    L_0x0587:
        r12 = org.telegram.messenger.MediaController.getInstance();
        r12.setBaseActivity(r11, r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.onCreate(android.os.Bundle):void");
    }

    public /* synthetic */ boolean lambda$onCreate$0$LaunchActivity(View view, MotionEvent motionEvent) {
        if (!this.actionBarLayout.fragmentsStack.isEmpty() && motionEvent.getAction() == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int[] iArr = new int[2];
            this.layersActionBarLayout.getLocationOnScreen(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            if (!this.layersActionBarLayout.checkTransitionAnimation() && (x <= ((float) i) || x >= ((float) (i + this.layersActionBarLayout.getWidth())) || y <= ((float) i2) || y >= ((float) (i2 + this.layersActionBarLayout.getHeight())))) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
                        actionBarLayout.removeFragmentFromStack((BaseFragment) actionBarLayout.fragmentsStack.get(0));
                    }
                    this.layersActionBarLayout.closeLastFragment(true);
                }
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ void lambda$onCreate$2$LaunchActivity(View view, int i) {
        int i2;
        if (i == 0) {
            DrawerLayoutAdapter drawerLayoutAdapter = this.drawerLayoutAdapter;
            drawerLayoutAdapter.setAccountsShowed(drawerLayoutAdapter.isAccountsShowed() ^ 1, true);
        } else if (view instanceof DrawerUserCell) {
            switchToAccount(((DrawerUserCell) view).getAccountNumber(), true);
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (view instanceof DrawerAddCell) {
            i2 = -1;
            for (i = 0; i < 3; i++) {
                if (!UserConfig.getInstance(i).isClientActivated()) {
                    i2 = i;
                    break;
                }
            }
            if (i2 >= 0) {
                lambda$runLinkRequest$27$LaunchActivity(new LoginActivity(i2));
            }
            this.drawerLayoutContainer.closeDrawer(false);
        } else {
            i2 = this.drawerLayoutAdapter.getId(i);
            Bundle bundle;
            if (i2 == 2) {
                lambda$runLinkRequest$27$LaunchActivity(new GroupCreateActivity(new Bundle()));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 3) {
                bundle = new Bundle();
                bundle.putBoolean("onlyUsers", true);
                bundle.putBoolean("destroyAfterSelect", true);
                bundle.putBoolean("createSecretChat", true);
                bundle.putBoolean("allowBots", false);
                lambda$runLinkRequest$27$LaunchActivity(new ContactsActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 4) {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                String str = "channel_intro";
                if (BuildVars.DEBUG_VERSION || !globalMainSettings.getBoolean(str, false)) {
                    lambda$runLinkRequest$27$LaunchActivity(new ChannelIntroActivity());
                    globalMainSettings.edit().putBoolean(str, true).commit();
                } else {
                    bundle = new Bundle();
                    bundle.putInt("step", 0);
                    lambda$runLinkRequest$27$LaunchActivity(new ChannelCreateActivity(bundle));
                }
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 6) {
                lambda$runLinkRequest$27$LaunchActivity(new ContactsActivity(null));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 7) {
                lambda$runLinkRequest$27$LaunchActivity(new InviteContactsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 8) {
                lambda$runLinkRequest$27$LaunchActivity(new SettingsActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 9) {
                Browser.openUrl((Context) this, LocaleController.getString("TelegramFaqUrl", C1067R.string.TelegramFaqUrl));
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 10) {
                lambda$runLinkRequest$27$LaunchActivity(new CallLogActivity());
                this.drawerLayoutContainer.closeDrawer(false);
            } else if (i2 == 11) {
                bundle = new Bundle();
                bundle.putInt("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
                lambda$runLinkRequest$27$LaunchActivity(new ChatActivity(bundle));
                this.drawerLayoutContainer.closeDrawer(false);
            }
        }
    }

    static /* synthetic */ void lambda$onCreate$3(View view) {
        int measuredHeight = view.getMeasuredHeight();
        if (VERSION.SDK_INT >= 21) {
            measuredHeight -= AndroidUtilities.statusBarHeight;
        }
        if (measuredHeight > AndroidUtilities.m26dp(100.0f) && measuredHeight < AndroidUtilities.displaySize.y) {
            int dp = AndroidUtilities.m26dp(100.0f) + measuredHeight;
            Point point = AndroidUtilities.displaySize;
            if (dp > point.y) {
                point.y = measuredHeight;
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("fix display size y to ");
                    stringBuilder.append(AndroidUtilities.displaySize.y);
                    FileLog.m27d(stringBuilder.toString());
                }
            }
        }
    }

    public void switchToAccount(int i, boolean z) {
        if (i != UserConfig.selectedAccount) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
            UserConfig.selectedAccount = i;
            UserConfig.getInstance(0).saveConfig(false);
            checkCurrentAccount();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.removeAllFragments();
                this.rightActionBarLayout.removeAllFragments();
                if (!this.tabletFullSize) {
                    this.shadowTabletSide.setVisibility(0);
                    if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                    }
                    this.rightActionBarLayout.setVisibility(8);
                }
                this.layersActionBarLayout.setVisibility(8);
            }
            if (z) {
                this.actionBarLayout.removeAllFragments();
            } else {
                this.actionBarLayout.removeFragmentFromStack(0);
            }
            DialogsActivity dialogsActivity = new DialogsActivity(null);
            dialogsActivity.setSideMenu(this.sideMenu);
            this.actionBarLayout.addFragmentToStack(dialogsActivity, 0);
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                this.layersActionBarLayout.showLastFragment();
                this.rightActionBarLayout.showLastFragment();
            }
            if (!ApplicationLoader.mainInterfacePaused) {
                ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
            }
            if (UserConfig.getInstance(i).unacceptedTermsOfService != null) {
                showTosActivity(i, UserConfig.getInstance(i).unacceptedTermsOfService);
            }
        }
    }

    private void switchToAvailableAccountOrLogout() {
        int i = 0;
        while (i < 3) {
            if (UserConfig.getInstance(i).isClientActivated()) {
                break;
            }
            i++;
        }
        i = -1;
        TermsOfServiceView termsOfServiceView = this.termsOfServiceView;
        if (termsOfServiceView != null) {
            termsOfServiceView.setVisibility(8);
        }
        if (i != -1) {
            switchToAccount(i, true);
            return;
        }
        DrawerLayoutAdapter drawerLayoutAdapter = this.drawerLayoutAdapter;
        if (drawerLayoutAdapter != null) {
            drawerLayoutAdapter.notifyDataSetChanged();
        }
        Iterator it = this.actionBarLayout.fragmentsStack.iterator();
        while (it.hasNext()) {
            ((BaseFragment) it.next()).onFragmentDestroy();
        }
        this.actionBarLayout.fragmentsStack.clear();
        if (AndroidUtilities.isTablet()) {
            it = this.layersActionBarLayout.fragmentsStack.iterator();
            while (it.hasNext()) {
                ((BaseFragment) it.next()).onFragmentDestroy();
            }
            this.layersActionBarLayout.fragmentsStack.clear();
            it = this.rightActionBarLayout.fragmentsStack.iterator();
            while (it.hasNext()) {
                ((BaseFragment) it.next()).onFragmentDestroy();
            }
            this.rightActionBarLayout.fragmentsStack.clear();
        }
        startActivity(new Intent(this, IntroActivity.class));
        onFinish();
        finish();
    }

    public int getMainFragmentsCount() {
        return mainFragmentsStack.size();
    }

    private void checkCurrentAccount() {
        int i = this.currentAccount;
        if (i != UserConfig.selectedAccount) {
            NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
        }
        this.currentAccount = UserConfig.selectedAccount;
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mainUserInfoChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowAlert);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.openArticle);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.hasNewContactsToImport);
        updateCurrentConnectionState(this.currentAccount);
    }

    private void checkLayout() {
        if (AndroidUtilities.isTablet() && this.rightActionBarLayout != null) {
            int i = 0;
            BaseFragment baseFragment;
            if (AndroidUtilities.isInMultiwindow || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
                this.tabletFullSize = true;
                if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    while (this.rightActionBarLayout.fragmentsStack.size() > 0) {
                        baseFragment = (BaseFragment) this.rightActionBarLayout.fragmentsStack.get(0);
                        if (baseFragment instanceof ChatActivity) {
                            ((ChatActivity) baseFragment).setIgnoreAttachOnPause(true);
                        }
                        baseFragment.onPause();
                        this.rightActionBarLayout.fragmentsStack.remove(0);
                        this.actionBarLayout.fragmentsStack.add(baseFragment);
                    }
                    if (this.passcodeView.getVisibility() != 0) {
                        this.actionBarLayout.showLastFragment();
                    }
                }
                this.shadowTabletSide.setVisibility(8);
                this.rightActionBarLayout.setVisibility(8);
                View view = this.backgroundTablet;
                if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                    i = 8;
                }
                view.setVisibility(i);
                return;
            }
            this.tabletFullSize = false;
            if (this.actionBarLayout.fragmentsStack.size() >= 2) {
                while (1 < this.actionBarLayout.fragmentsStack.size()) {
                    baseFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(1);
                    if (baseFragment instanceof ChatActivity) {
                        ((ChatActivity) baseFragment).setIgnoreAttachOnPause(true);
                    }
                    baseFragment.onPause();
                    this.actionBarLayout.fragmentsStack.remove(1);
                    this.rightActionBarLayout.fragmentsStack.add(baseFragment);
                }
                if (this.passcodeView.getVisibility() != 0) {
                    this.actionBarLayout.showLastFragment();
                    this.rightActionBarLayout.showLastFragment();
                }
            }
            ActionBarLayout actionBarLayout = this.rightActionBarLayout;
            actionBarLayout.setVisibility(actionBarLayout.fragmentsStack.isEmpty() ? 8 : 0);
            this.backgroundTablet.setVisibility(this.rightActionBarLayout.fragmentsStack.isEmpty() ? 0 : 8);
            FrameLayout frameLayout = this.shadowTabletSide;
            if (this.actionBarLayout.fragmentsStack.isEmpty()) {
                i = 8;
            }
            frameLayout.setVisibility(i);
        }
    }

    private void showUpdateActivity(int i, TL_help_appUpdate tL_help_appUpdate) {
        if (this.blockingUpdateView == null) {
            this.blockingUpdateView = new BlockingUpdateView(this) {
                public void setVisibility(int i) {
                    super.setVisibility(i);
                    if (i == 8) {
                        LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                    }
                }
            };
            this.drawerLayoutContainer.addView(this.blockingUpdateView, LayoutHelper.createFrame(-1, -1.0f));
        }
        this.blockingUpdateView.show(i, tL_help_appUpdate);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }

    private void showTosActivity(int i, TL_help_termsOfService tL_help_termsOfService) {
        if (this.termsOfServiceView == null) {
            this.termsOfServiceView = new TermsOfServiceView(this);
            this.drawerLayoutContainer.addView(this.termsOfServiceView, LayoutHelper.createFrame(-1, -1.0f));
            this.termsOfServiceView.setDelegate(new C42093());
        }
        TL_help_termsOfService tL_help_termsOfService2 = UserConfig.getInstance(i).unacceptedTermsOfService;
        if (tL_help_termsOfService2 != tL_help_termsOfService && (tL_help_termsOfService2 == null || !tL_help_termsOfService2.f490id.data.equals(tL_help_termsOfService.f490id.data))) {
            UserConfig.getInstance(i).unacceptedTermsOfService = tL_help_termsOfService;
            UserConfig.getInstance(i).saveConfig(false);
        }
        this.termsOfServiceView.show(i, tL_help_termsOfService);
        this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
    }

    private void showPasscodeActivity() {
        if (this.passcodeView != null) {
            SharedConfig.appLocked = true;
            if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                SecretMediaViewer.getInstance().closePhoto(false, false);
            } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                PhotoViewer.getInstance().closePhoto(false, true);
            } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                ArticleViewer.getInstance().close(false, true);
            }
            this.passcodeView.onShow();
            SharedConfig.isWaitingForPasscodeEnter = true;
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
            this.passcodeView.setDelegate(new C3716-$$Lambda$LaunchActivity$V1zg09F7Jz3e7nYXLjhnV60E-O8(this));
            this.actionBarLayout.setVisibility(4);
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getVisibility() == 0) {
                    this.layersActionBarLayout.setVisibility(4);
                }
                this.rightActionBarLayout.setVisibility(4);
            }
        }
    }

    public /* synthetic */ void lambda$showPasscodeActivity$4$LaunchActivity() {
        SharedConfig.isWaitingForPasscodeEnter = false;
        Intent intent = this.passcodeSaveIntent;
        if (intent != null) {
            handleIntent(intent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true);
            this.passcodeSaveIntent = null;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        this.actionBarLayout.setVisibility(0);
        this.actionBarLayout.showLastFragment();
        if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
            this.rightActionBarLayout.showLastFragment();
            if (this.layersActionBarLayout.getVisibility() == 4) {
                this.layersActionBarLayout.setVisibility(0);
            }
            this.rightActionBarLayout.setVisibility(0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:126:0x0290  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0290  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0290  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x0397  */
    /* JADX WARNING: Removed duplicated region for block: B:547:0x0c8f A:{Catch:{ Exception -> 0x0c93 }} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x0bdb  */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x0bf1 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:713:0x10e4  */
    /* JADX WARNING: Removed duplicated region for block: B:712:0x10d8  */
    /* JADX WARNING: Removed duplicated region for block: B:697:0x108c  */
    /* JADX WARNING: Removed duplicated region for block: B:702:0x10a4  */
    /* JADX WARNING: Removed duplicated region for block: B:712:0x10d8  */
    /* JADX WARNING: Removed duplicated region for block: B:713:0x10e4  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:715:0x10fc  */
    /* JADX WARNING: Removed duplicated region for block: B:598:0x0e05  */
    /* JADX WARNING: Removed duplicated region for block: B:730:0x1150  */
    /* JADX WARNING: Removed duplicated region for block: B:722:0x110b  */
    /* JADX WARNING: Removed duplicated region for block: B:738:0x1195  */
    /* JADX WARNING: Removed duplicated region for block: B:538:0x0c82  */
    /* JADX WARNING: Missing block: B:12:0x0040, code skipped:
            if ("android.intent.action.MAIN".equals(r45.getAction()) == false) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:123:0x0289, code skipped:
            if (r15.sendingLocation == null) goto L_0x010f;
     */
    /* JADX WARNING: Missing block: B:455:0x0add, code skipped:
            if (r1.intValue() == 0) goto L_0x0adf;
     */
    /* JADX WARNING: Missing block: B:605:0x0e38, code skipped:
            if (r4.checkCanOpenChat(r0, (org.telegram.p004ui.ActionBar.BaseFragment) r5.get(r5.size() - 1)) != false) goto L_0x0e3c;
     */
    /* JADX WARNING: Missing block: B:608:0x0e4b, code skipped:
            if (r2.actionBarLayout.presentFragment(new org.telegram.p004ui.ChatActivity(r0), false, true, true, false) != false) goto L_0x0e4d;
     */
    /* JADX WARNING: Missing block: B:610:0x0e4f, code skipped:
            r13 = false;
     */
    /* JADX WARNING: Missing block: B:619:0x0e84, code skipped:
            if (r0.checkCanOpenChat(r5, (org.telegram.p004ui.ActionBar.BaseFragment) r4.get(r4.size() - 1)) != false) goto L_0x0e86;
     */
    /* JADX WARNING: Missing block: B:621:0x0e97, code skipped:
            if (r2.actionBarLayout.presentFragment(new org.telegram.p004ui.ChatActivity(r5), false, true, true, false) != false) goto L_0x0e4d;
     */
    /* JADX WARNING: Missing block: B:688:0x105a, code skipped:
            if ((r0.get(r0.size() - 1) instanceof org.telegram.p004ui.DialogsActivity) != false) goto L_0x1078;
     */
    /* JADX WARNING: Missing block: B:692:0x1076, code skipped:
            if ((r0.get(r0.size() - 1) instanceof org.telegram.p004ui.DialogsActivity) != false) goto L_0x1078;
     */
    private boolean handleIntent(android.content.Intent r45, boolean r46, boolean r47, boolean r48) {
        /*
        r44 = this;
        r15 = r44;
        r14 = r45;
        r0 = r47;
        r1 = org.telegram.messenger.AndroidUtilities.handleProxyIntent(r44, r45);
        r13 = 1;
        if (r1 == 0) goto L_0x0023;
    L_0x000d:
        r0 = r15.actionBarLayout;
        r0.showLastFragment();
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x0022;
    L_0x0018:
        r0 = r15.layersActionBarLayout;
        r0.showLastFragment();
        r0 = r15.rightActionBarLayout;
        r0.showLastFragment();
    L_0x0022:
        return r13;
    L_0x0023:
        r1 = org.telegram.p004ui.PhotoViewer.hasInstance();
        r12 = 0;
        if (r1 == 0) goto L_0x0049;
    L_0x002a:
        r1 = org.telegram.p004ui.PhotoViewer.getInstance();
        r1 = r1.isVisible();
        if (r1 == 0) goto L_0x0049;
    L_0x0034:
        if (r14 == 0) goto L_0x0042;
    L_0x0036:
        r1 = r45.getAction();
        r2 = "android.intent.action.MAIN";
        r1 = r2.equals(r1);
        if (r1 != 0) goto L_0x0049;
    L_0x0042:
        r1 = org.telegram.p004ui.PhotoViewer.getInstance();
        r1.closePhoto(r12, r13);
    L_0x0049:
        r1 = r45.getFlags();
        r11 = new int[r13];
        r2 = org.telegram.messenger.UserConfig.selectedAccount;
        r3 = "currentAccount";
        r2 = r14.getIntExtra(r3, r2);
        r11[r12] = r2;
        r2 = r11[r12];
        r15.switchToAccount(r2, r13);
        if (r48 != 0) goto L_0x007f;
    L_0x0060:
        r2 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r13);
        if (r2 != 0) goto L_0x006a;
    L_0x0066:
        r2 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;
        if (r2 == 0) goto L_0x007f;
    L_0x006a:
        r44.showPasscodeActivity();
        r15.passcodeSaveIntent = r14;
        r10 = r46;
        r15.passcodeSaveIntentIsNew = r10;
        r15.passcodeSaveIntentIsRestore = r0;
        r0 = r15.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0.saveConfig(r12);
        return r12;
    L_0x007f:
        r10 = r46;
        r2 = org.telegram.messenger.SharedConfig.directShare;
        r3 = "hash";
        r8 = 0;
        if (r2 == 0) goto L_0x00ad;
    L_0x0089:
        if (r14 == 0) goto L_0x00ad;
    L_0x008b:
        r2 = r45.getExtras();
        if (r2 == 0) goto L_0x00ad;
    L_0x0091:
        r2 = r45.getExtras();
        r4 = "dialogId";
        r4 = r2.getLong(r4, r8);
        r2 = r45.getExtras();
        r6 = r2.getLong(r3, r8);
        r16 = org.telegram.messenger.SharedConfig.directShareHash;
        r2 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1));
        if (r2 == 0) goto L_0x00aa;
    L_0x00a9:
        goto L_0x00ad;
    L_0x00aa:
        r19 = r4;
        goto L_0x00af;
    L_0x00ad:
        r19 = r8;
    L_0x00af:
        r7 = 0;
        r15.photoPathsArray = r7;
        r15.videoPath = r7;
        r15.sendingText = r7;
        r15.sendingLocation = r7;
        r15.documentsPathsArray = r7;
        r15.documentsOriginalPathsArray = r7;
        r15.documentsMimeType = r7;
        r15.documentsUrisArray = r7;
        r15.contactsToSend = r7;
        r15.contactsToSendUri = r7;
        r2 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        r1 = r1 & r2;
        if (r1 != 0) goto L_0x0de9;
    L_0x00c9:
        if (r14 == 0) goto L_0x0de9;
    L_0x00cb:
        r1 = r45.getAction();
        if (r1 == 0) goto L_0x0de9;
    L_0x00d1:
        if (r0 != 0) goto L_0x0de9;
    L_0x00d3:
        r0 = r45.getAction();
        r1 = "android.intent.action.SEND";
        r0 = r1.equals(r0);
        r1 = "\n";
        r2 = "";
        if (r0 == 0) goto L_0x029b;
    L_0x00e3:
        r0 = r45.getType();
        if (r0 == 0) goto L_0x0112;
    L_0x00e9:
        r3 = "text/x-vcard";
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0112;
    L_0x00f1:
        r0 = r45.getExtras();	 Catch:{ Exception -> 0x010b }
        r1 = "android.intent.extra.STREAM";
        r0 = r0.get(r1);	 Catch:{ Exception -> 0x010b }
        r0 = (android.net.Uri) r0;	 Catch:{ Exception -> 0x010b }
        if (r0 == 0) goto L_0x010f;
    L_0x00ff:
        r1 = r15.currentAccount;	 Catch:{ Exception -> 0x010b }
        r1 = org.telegram.messenger.AndroidUtilities.loadVCardFromStream(r0, r1, r12, r7, r7);	 Catch:{ Exception -> 0x010b }
        r15.contactsToSend = r1;	 Catch:{ Exception -> 0x010b }
        r15.contactsToSendUri = r0;	 Catch:{ Exception -> 0x010b }
        goto L_0x028d;
    L_0x010b:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x010f:
        r0 = 1;
        goto L_0x028e;
    L_0x0112:
        r3 = "android.intent.extra.TEXT";
        r3 = r14.getStringExtra(r3);
        if (r3 != 0) goto L_0x0126;
    L_0x011a:
        r4 = "android.intent.extra.TEXT";
        r4 = r14.getCharSequenceExtra(r4);
        if (r4 == 0) goto L_0x0126;
    L_0x0122:
        r3 = r4.toString();
    L_0x0126:
        r4 = "android.intent.extra.SUBJECT";
        r4 = r14.getStringExtra(r4);
        r5 = android.text.TextUtils.isEmpty(r3);
        if (r5 != 0) goto L_0x01ce;
    L_0x0132:
        r5 = r15.locationRegex;
        r5 = r5.matcher(r3);
        r6 = r5.find();
        if (r6 == 0) goto L_0x01a3;
    L_0x013e:
        r1 = "\\n";
        r1 = r3.split(r1);
        r4 = r1[r12];
        r6 = "My Position";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x014f;
    L_0x014e:
        goto L_0x016a;
    L_0x014f:
        r4 = r1[r12];
        r6 = "geo:";
        r4 = r4.contains(r6);
        if (r4 != 0) goto L_0x016a;
    L_0x0159:
        r4 = r1[r12];
        r6 = r1[r13];
        r8 = "geo:";
        r6 = r6.contains(r8);
        if (r6 != 0) goto L_0x0168;
    L_0x0165:
        r1 = r1[r13];
        goto L_0x016c;
    L_0x0168:
        r1 = r7;
        goto L_0x016c;
    L_0x016a:
        r1 = r7;
        r4 = r1;
    L_0x016c:
        r6 = new android.location.Location;
        r6.<init>(r2);
        r15.sendingLocation = r6;
        r6 = r15.sendingLocation;
        r8 = r5.group(r13);
        r8 = java.lang.Double.parseDouble(r8);
        r6.setLatitude(r8);
        r6 = r15.sendingLocation;
        r8 = 2;
        r5 = r5.group(r8);
        r8 = java.lang.Double.parseDouble(r5);
        r6.setLongitude(r8);
        r5 = new android.os.Bundle;
        r5.<init>();
        r6 = "venueTitle";
        r5.putCharSequence(r6, r4);
        r4 = "venueAddress";
        r5.putCharSequence(r4, r1);
        r1 = r15.sendingLocation;
        r1.setExtras(r5);
        goto L_0x01cb;
    L_0x01a3:
        r5 = "http://";
        r5 = r3.startsWith(r5);
        if (r5 != 0) goto L_0x01b3;
    L_0x01ab:
        r5 = "https://";
        r5 = r3.startsWith(r5);
        if (r5 == 0) goto L_0x01cb;
    L_0x01b3:
        r5 = android.text.TextUtils.isEmpty(r4);
        if (r5 != 0) goto L_0x01cb;
    L_0x01b9:
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r4);
        r5.append(r1);
        r5.append(r3);
        r3 = r5.toString();
    L_0x01cb:
        r15.sendingText = r3;
        goto L_0x01d6;
    L_0x01ce:
        r1 = android.text.TextUtils.isEmpty(r4);
        if (r1 != 0) goto L_0x01d6;
    L_0x01d4:
        r15.sendingText = r4;
    L_0x01d6:
        r1 = "android.intent.extra.STREAM";
        r1 = r14.getParcelableExtra(r1);
        if (r1 == 0) goto L_0x0283;
    L_0x01de:
        r3 = r1 instanceof android.net.Uri;
        if (r3 != 0) goto L_0x01ea;
    L_0x01e2:
        r1 = r1.toString();
        r1 = android.net.Uri.parse(r1);
    L_0x01ea:
        r1 = (android.net.Uri) r1;
        if (r1 == 0) goto L_0x01f6;
    L_0x01ee:
        r3 = org.telegram.messenger.AndroidUtilities.isInternalUri(r1);
        if (r3 == 0) goto L_0x01f6;
    L_0x01f4:
        r3 = 1;
        goto L_0x01f7;
    L_0x01f6:
        r3 = 0;
    L_0x01f7:
        if (r3 != 0) goto L_0x0281;
    L_0x01f9:
        if (r1 == 0) goto L_0x022d;
    L_0x01fb:
        if (r0 == 0) goto L_0x0205;
    L_0x01fd:
        r4 = "image/";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0215;
    L_0x0205:
        r4 = r1.toString();
        r4 = r4.toLowerCase();
        r5 = ".jpg";
        r4 = r4.endsWith(r5);
        if (r4 == 0) goto L_0x022d;
    L_0x0215:
        r0 = r15.photoPathsArray;
        if (r0 != 0) goto L_0x0220;
    L_0x0219:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r15.photoPathsArray = r0;
    L_0x0220:
        r0 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;
        r0.<init>();
        r0.uri = r1;
        r1 = r15.photoPathsArray;
        r1.add(r0);
        goto L_0x0281;
    L_0x022d:
        r4 = org.telegram.messenger.AndroidUtilities.getPath(r1);
        if (r4 == 0) goto L_0x026f;
    L_0x0233:
        r5 = "file:";
        r5 = r4.startsWith(r5);
        if (r5 == 0) goto L_0x0241;
    L_0x023b:
        r5 = "file://";
        r4 = r4.replace(r5, r2);
    L_0x0241:
        if (r0 == 0) goto L_0x024e;
    L_0x0243:
        r2 = "video/";
        r0 = r0.startsWith(r2);
        if (r0 == 0) goto L_0x024e;
    L_0x024b:
        r15.videoPath = r4;
        goto L_0x0281;
    L_0x024e:
        r0 = r15.documentsPathsArray;
        if (r0 != 0) goto L_0x0260;
    L_0x0252:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r15.documentsPathsArray = r0;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r15.documentsOriginalPathsArray = r0;
    L_0x0260:
        r0 = r15.documentsPathsArray;
        r0.add(r4);
        r0 = r15.documentsOriginalPathsArray;
        r1 = r1.toString();
        r0.add(r1);
        goto L_0x0281;
    L_0x026f:
        r2 = r15.documentsUrisArray;
        if (r2 != 0) goto L_0x027a;
    L_0x0273:
        r2 = new java.util.ArrayList;
        r2.<init>();
        r15.documentsUrisArray = r2;
    L_0x027a:
        r2 = r15.documentsUrisArray;
        r2.add(r1);
        r15.documentsMimeType = r0;
    L_0x0281:
        r0 = r3;
        goto L_0x028e;
    L_0x0283:
        r0 = r15.sendingText;
        if (r0 != 0) goto L_0x028d;
    L_0x0287:
        r0 = r15.sendingLocation;
        if (r0 != 0) goto L_0x028d;
    L_0x028b:
        goto L_0x010f;
    L_0x028d:
        r0 = 0;
    L_0x028e:
        if (r0 == 0) goto L_0x03a0;
    L_0x0290:
        r0 = "Unsupported content";
        r0 = android.widget.Toast.makeText(r15, r0, r12);
        r0.show();
        goto L_0x03a0;
    L_0x029b:
        r0 = r45.getAction();
        r4 = "android.intent.action.SEND_MULTIPLE";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x03a8;
    L_0x02a7:
        r0 = "android.intent.extra.STREAM";
        r0 = r14.getParcelableArrayListExtra(r0);	 Catch:{ Exception -> 0x0390 }
        r1 = r45.getType();	 Catch:{ Exception -> 0x0390 }
        if (r0 == 0) goto L_0x02e4;
    L_0x02b3:
        r3 = 0;
    L_0x02b4:
        r4 = r0.size();	 Catch:{ Exception -> 0x0390 }
        if (r3 >= r4) goto L_0x02dd;
    L_0x02ba:
        r4 = r0.get(r3);	 Catch:{ Exception -> 0x0390 }
        r4 = (android.os.Parcelable) r4;	 Catch:{ Exception -> 0x0390 }
        r5 = r4 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0390 }
        if (r5 != 0) goto L_0x02cc;
    L_0x02c4:
        r4 = r4.toString();	 Catch:{ Exception -> 0x0390 }
        r4 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0390 }
    L_0x02cc:
        r4 = (android.net.Uri) r4;	 Catch:{ Exception -> 0x0390 }
        if (r4 == 0) goto L_0x02db;
    L_0x02d0:
        r4 = org.telegram.messenger.AndroidUtilities.isInternalUri(r4);	 Catch:{ Exception -> 0x0390 }
        if (r4 == 0) goto L_0x02db;
    L_0x02d6:
        r0.remove(r3);	 Catch:{ Exception -> 0x0390 }
        r3 = r3 + -1;
    L_0x02db:
        r3 = r3 + r13;
        goto L_0x02b4;
    L_0x02dd:
        r3 = r0.isEmpty();	 Catch:{ Exception -> 0x0390 }
        if (r3 == 0) goto L_0x02e4;
    L_0x02e3:
        r0 = r7;
    L_0x02e4:
        if (r0 == 0) goto L_0x0394;
    L_0x02e6:
        if (r1 == 0) goto L_0x0325;
    L_0x02e8:
        r3 = "image/";
        r3 = r1.startsWith(r3);	 Catch:{ Exception -> 0x0390 }
        if (r3 == 0) goto L_0x0325;
    L_0x02f0:
        r1 = 0;
    L_0x02f1:
        r2 = r0.size();	 Catch:{ Exception -> 0x0390 }
        if (r1 >= r2) goto L_0x038e;
    L_0x02f7:
        r2 = r0.get(r1);	 Catch:{ Exception -> 0x0390 }
        r2 = (android.os.Parcelable) r2;	 Catch:{ Exception -> 0x0390 }
        r3 = r2 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0390 }
        if (r3 != 0) goto L_0x0309;
    L_0x0301:
        r2 = r2.toString();	 Catch:{ Exception -> 0x0390 }
        r2 = android.net.Uri.parse(r2);	 Catch:{ Exception -> 0x0390 }
    L_0x0309:
        r2 = (android.net.Uri) r2;	 Catch:{ Exception -> 0x0390 }
        r3 = r15.photoPathsArray;	 Catch:{ Exception -> 0x0390 }
        if (r3 != 0) goto L_0x0316;
    L_0x030f:
        r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0390 }
        r3.<init>();	 Catch:{ Exception -> 0x0390 }
        r15.photoPathsArray = r3;	 Catch:{ Exception -> 0x0390 }
    L_0x0316:
        r3 = new org.telegram.messenger.SendMessagesHelper$SendingMediaInfo;	 Catch:{ Exception -> 0x0390 }
        r3.<init>();	 Catch:{ Exception -> 0x0390 }
        r3.uri = r2;	 Catch:{ Exception -> 0x0390 }
        r2 = r15.photoPathsArray;	 Catch:{ Exception -> 0x0390 }
        r2.add(r3);	 Catch:{ Exception -> 0x0390 }
        r1 = r1 + 1;
        goto L_0x02f1;
    L_0x0325:
        r3 = 0;
    L_0x0326:
        r4 = r0.size();	 Catch:{ Exception -> 0x0390 }
        if (r3 >= r4) goto L_0x038e;
    L_0x032c:
        r4 = r0.get(r3);	 Catch:{ Exception -> 0x0390 }
        r4 = (android.os.Parcelable) r4;	 Catch:{ Exception -> 0x0390 }
        r5 = r4 instanceof android.net.Uri;	 Catch:{ Exception -> 0x0390 }
        if (r5 != 0) goto L_0x033e;
    L_0x0336:
        r4 = r4.toString();	 Catch:{ Exception -> 0x0390 }
        r4 = android.net.Uri.parse(r4);	 Catch:{ Exception -> 0x0390 }
    L_0x033e:
        r5 = r4;
        r5 = (android.net.Uri) r5;	 Catch:{ Exception -> 0x0390 }
        r6 = org.telegram.messenger.AndroidUtilities.getPath(r5);	 Catch:{ Exception -> 0x0390 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0390 }
        if (r4 != 0) goto L_0x034c;
    L_0x034b:
        r4 = r6;
    L_0x034c:
        if (r6 == 0) goto L_0x0379;
    L_0x034e:
        r5 = "file:";
        r5 = r6.startsWith(r5);	 Catch:{ Exception -> 0x0390 }
        if (r5 == 0) goto L_0x035c;
    L_0x0356:
        r5 = "file://";
        r6 = r6.replace(r5, r2);	 Catch:{ Exception -> 0x0390 }
    L_0x035c:
        r5 = r15.documentsPathsArray;	 Catch:{ Exception -> 0x0390 }
        if (r5 != 0) goto L_0x036e;
    L_0x0360:
        r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0390 }
        r5.<init>();	 Catch:{ Exception -> 0x0390 }
        r15.documentsPathsArray = r5;	 Catch:{ Exception -> 0x0390 }
        r5 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0390 }
        r5.<init>();	 Catch:{ Exception -> 0x0390 }
        r15.documentsOriginalPathsArray = r5;	 Catch:{ Exception -> 0x0390 }
    L_0x036e:
        r5 = r15.documentsPathsArray;	 Catch:{ Exception -> 0x0390 }
        r5.add(r6);	 Catch:{ Exception -> 0x0390 }
        r5 = r15.documentsOriginalPathsArray;	 Catch:{ Exception -> 0x0390 }
        r5.add(r4);	 Catch:{ Exception -> 0x0390 }
        goto L_0x038b;
    L_0x0379:
        r4 = r15.documentsUrisArray;	 Catch:{ Exception -> 0x0390 }
        if (r4 != 0) goto L_0x0384;
    L_0x037d:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0390 }
        r4.<init>();	 Catch:{ Exception -> 0x0390 }
        r15.documentsUrisArray = r4;	 Catch:{ Exception -> 0x0390 }
    L_0x0384:
        r4 = r15.documentsUrisArray;	 Catch:{ Exception -> 0x0390 }
        r4.add(r5);	 Catch:{ Exception -> 0x0390 }
        r15.documentsMimeType = r1;	 Catch:{ Exception -> 0x0390 }
    L_0x038b:
        r3 = r3 + 1;
        goto L_0x0326;
    L_0x038e:
        r0 = 0;
        goto L_0x0395;
    L_0x0390:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0394:
        r0 = 1;
    L_0x0395:
        if (r0 == 0) goto L_0x03a0;
    L_0x0397:
        r0 = "Unsupported content";
        r0 = android.widget.Toast.makeText(r15, r0, r12);
        r0.show();
    L_0x03a0:
        r6 = r11;
        r1 = r14;
        r2 = r15;
        r3 = 0;
        r29 = 0;
        goto L_0x0def;
    L_0x03a8:
        r0 = r45.getAction();
        r4 = "android.intent.action.VIEW";
        r0 = r4.equals(r0);
        if (r0 == 0) goto L_0x0d20;
    L_0x03b4:
        r0 = r45.getData();
        if (r0 == 0) goto L_0x0d06;
    L_0x03ba:
        r4 = r0.getScheme();
        if (r4 == 0) goto L_0x0bb9;
    L_0x03c0:
        r5 = "http";
        r5 = r4.equals(r5);
        if (r5 != 0) goto L_0x08cf;
    L_0x03c8:
        r5 = "https";
        r5 = r4.equals(r5);
        if (r5 == 0) goto L_0x03d2;
    L_0x03d0:
        goto L_0x08cf;
    L_0x03d2:
        r5 = "tg";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0bb9;
    L_0x03da:
        r0 = r0.toString();
        r4 = "tg:resolve";
        r4 = r0.startsWith(r4);
        r5 = "nonce";
        r8 = "callback_url";
        r9 = "public_key";
        r7 = "bot_id";
        r6 = "payload";
        r13 = "scope";
        r12 = "tg://telegram.org";
        if (r4 != 0) goto L_0x07f5;
    L_0x03f4:
        r4 = "tg://resolve";
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x03fe;
    L_0x03fc:
        goto L_0x07f5;
    L_0x03fe:
        r4 = "tg:privatepost";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x079f;
    L_0x0406:
        r4 = "tg://privatepost";
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0410;
    L_0x040e:
        goto L_0x079f;
    L_0x0410:
        r4 = "tg:bg";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x06d5;
    L_0x0418:
        r4 = "tg://bg";
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0422;
    L_0x0420:
        goto L_0x06d5;
    L_0x0422:
        r4 = "tg:join";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x06ba;
    L_0x042a:
        r4 = "tg://join";
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0434;
    L_0x0432:
        goto L_0x06ba;
    L_0x0434:
        r4 = "tg:addstickers";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x069e;
    L_0x043c:
        r4 = "tg://addstickers";
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0446;
    L_0x0444:
        goto L_0x069e;
    L_0x0446:
        r4 = "tg:msg";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0613;
    L_0x044e:
        r4 = "tg://msg";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0613;
    L_0x0456:
        r4 = "tg://share";
        r4 = r0.startsWith(r4);
        if (r4 != 0) goto L_0x0613;
    L_0x045e:
        r4 = "tg:share";
        r4 = r0.startsWith(r4);
        if (r4 == 0) goto L_0x0468;
    L_0x0466:
        goto L_0x0613;
    L_0x0468:
        r1 = "tg:confirmphone";
        r1 = r0.startsWith(r1);
        if (r1 != 0) goto L_0x05ed;
    L_0x0470:
        r1 = "tg://confirmphone";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x047a;
    L_0x0478:
        goto L_0x05ed;
    L_0x047a:
        r1 = "tg:login";
        r1 = r0.startsWith(r1);
        if (r1 != 0) goto L_0x05d5;
    L_0x0482:
        r1 = "tg://login";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x048c;
    L_0x048a:
        goto L_0x05d5;
    L_0x048c:
        r1 = "tg:openmessage";
        r1 = r0.startsWith(r1);
        if (r1 != 0) goto L_0x0577;
    L_0x0494:
        r1 = "tg://openmessage";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x049e;
    L_0x049c:
        goto L_0x0577;
    L_0x049e:
        r1 = "tg:passport";
        r1 = r0.startsWith(r1);
        if (r1 != 0) goto L_0x0519;
    L_0x04a6:
        r1 = "tg://passport";
        r1 = r0.startsWith(r1);
        if (r1 != 0) goto L_0x0519;
    L_0x04ae:
        r1 = "tg:secureid";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x04b7;
    L_0x04b6:
        goto L_0x0519;
    L_0x04b7:
        r1 = "tg:setlanguage";
        r1 = r0.startsWith(r1);
        if (r1 != 0) goto L_0x04f1;
    L_0x04bf:
        r1 = "tg://setlanguage";
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x04c8;
    L_0x04c7:
        goto L_0x04f1;
    L_0x04c8:
        r1 = "tg://";
        r0 = r0.replace(r1, r2);
        r1 = "tg:";
        r0 = r0.replace(r1, r2);
        r1 = 63;
        r1 = r0.indexOf(r1);
        if (r1 < 0) goto L_0x04e1;
    L_0x04dc:
        r2 = 0;
        r0 = r0.substring(r2, r1);
    L_0x04e1:
        r24 = r0;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r13 = 0;
        r23 = 0;
        goto L_0x0867;
    L_0x04f1:
        r1 = "tg:setlanguage";
        r0 = r0.replace(r1, r12);
        r1 = "tg://setlanguage";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "lang";
        r0 = r0.getQueryParameter(r1);
        r25 = r0;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r13 = 0;
        r23 = 0;
        r24 = 0;
        goto L_0x0869;
    L_0x0519:
        r1 = "tg:passport";
        r0 = r0.replace(r1, r12);
        r1 = "tg://passport";
        r0 = r0.replace(r1, r12);
        r1 = "tg:secureid";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = new java.util.HashMap;
        r1.<init>();
        r2 = r0.getQueryParameter(r13);
        r4 = android.text.TextUtils.isEmpty(r2);
        if (r4 != 0) goto L_0x0556;
    L_0x053e:
        r4 = "{";
        r4 = r2.startsWith(r4);
        if (r4 == 0) goto L_0x0556;
    L_0x0546:
        r4 = "}";
        r4 = r2.endsWith(r4);
        if (r4 == 0) goto L_0x0556;
    L_0x054e:
        r4 = r0.getQueryParameter(r5);
        r1.put(r5, r4);
        goto L_0x055d;
    L_0x0556:
        r4 = r0.getQueryParameter(r6);
        r1.put(r6, r4);
    L_0x055d:
        r4 = r0.getQueryParameter(r7);
        r1.put(r7, r4);
        r1.put(r13, r2);
        r2 = r0.getQueryParameter(r9);
        r1.put(r9, r2);
        r0 = r0.getQueryParameter(r8);
        r1.put(r8, r0);
        goto L_0x0859;
    L_0x0577:
        r1 = "tg:openmessage";
        r0 = r0.replace(r1, r12);
        r1 = "tg://openmessage";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "user_id";
        r1 = r0.getQueryParameter(r1);
        r2 = "chat_id";
        r2 = r0.getQueryParameter(r2);
        r4 = "message_id";
        r0 = r0.getQueryParameter(r4);
        if (r1 == 0) goto L_0x05a1;
    L_0x059b:
        r12 = java.lang.Integer.parseInt(r1);	 Catch:{ NumberFormatException -> 0x05a9 }
        r1 = 0;
        goto L_0x05ab;
    L_0x05a1:
        if (r2 == 0) goto L_0x05a9;
    L_0x05a3:
        r12 = java.lang.Integer.parseInt(r2);	 Catch:{ NumberFormatException -> 0x05a9 }
        r1 = r12;
        goto L_0x05aa;
    L_0x05a9:
        r1 = 0;
    L_0x05aa:
        r12 = 0;
    L_0x05ab:
        if (r0 == 0) goto L_0x05b2;
    L_0x05ad:
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x05b2 }
        goto L_0x05b3;
    L_0x05b2:
        r0 = 0;
    L_0x05b3:
        r33 = r0;
        r32 = r1;
        r31 = r12;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r13 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        r28 = 0;
        r29 = 0;
        r30 = 0;
        goto L_0x0bd9;
    L_0x05d5:
        r1 = "tg:login";
        r0 = r0.replace(r1, r12);
        r1 = "tg://login";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "code";
        r0 = r0.getQueryParameter(r1);
        goto L_0x07e9;
    L_0x05ed:
        r1 = "tg:confirmphone";
        r0 = r0.replace(r1, r12);
        r1 = "tg://confirmphone";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "phone";
        r7 = r0.getQueryParameter(r1);
        r0 = r0.getQueryParameter(r3);
        r13 = r0;
        r6 = r7;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        goto L_0x07f2;
    L_0x0613:
        r4 = "tg:msg";
        r0 = r0.replace(r4, r12);
        r4 = "tg://msg";
        r0 = r0.replace(r4, r12);
        r4 = "tg://share";
        r0 = r0.replace(r4, r12);
        r4 = "tg:share";
        r0 = r0.replace(r4, r12);
        r0 = android.net.Uri.parse(r0);
        r4 = "url";
        r4 = r0.getQueryParameter(r4);
        if (r4 != 0) goto L_0x0638;
    L_0x0637:
        goto L_0x0639;
    L_0x0638:
        r2 = r4;
    L_0x0639:
        r4 = "text";
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x066f;
    L_0x0641:
        r4 = r2.length();
        if (r4 <= 0) goto L_0x0658;
    L_0x0647:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r2);
        r4.append(r1);
        r2 = r4.toString();
        r12 = 1;
        goto L_0x0659;
    L_0x0658:
        r12 = 0;
    L_0x0659:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r2);
        r2 = "text";
        r0 = r0.getQueryParameter(r2);
        r4.append(r0);
        r2 = r4.toString();
        goto L_0x0670;
    L_0x066f:
        r12 = 0;
    L_0x0670:
        r0 = r2.length();
        r4 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r0 <= r4) goto L_0x0681;
    L_0x0678:
        r0 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r4 = 0;
        r0 = r2.substring(r4, r0);
        r7 = r0;
        goto L_0x0683;
    L_0x0681:
        r4 = 0;
        r7 = r2;
    L_0x0683:
        r0 = r7.endsWith(r1);
        if (r0 == 0) goto L_0x0694;
    L_0x0689:
        r0 = r7.length();
        r2 = 1;
        r0 = r0 - r2;
        r7 = r7.substring(r4, r0);
        goto L_0x0683;
    L_0x0694:
        r1 = r7;
        r9 = r12;
        r0 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        goto L_0x07f0;
    L_0x069e:
        r1 = "tg:addstickers";
        r0 = r0.replace(r1, r12);
        r1 = "tg://addstickers";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "set";
        r0 = r0.getQueryParameter(r1);
        r5 = r0;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        goto L_0x07ec;
    L_0x06ba:
        r1 = "tg:join";
        r0 = r0.replace(r1, r12);
        r1 = "tg://join";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "invite";
        r0 = r0.getQueryParameter(r1);
        r4 = r0;
        r0 = 0;
        r1 = 0;
        goto L_0x07eb;
    L_0x06d5:
        r1 = "tg:bg";
        r0 = r0.replace(r1, r12);
        r1 = "tg://bg";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = new org.telegram.tgnet.TLRPC$TL_wallPaper;
        r1.<init>();
        r2 = new org.telegram.tgnet.TLRPC$TL_wallPaperSettings;
        r2.<init>();
        r1.settings = r2;
        r2 = "slug";
        r2 = r0.getQueryParameter(r2);
        r1.slug = r2;
        r2 = r1.slug;
        if (r2 != 0) goto L_0x0705;
    L_0x06fd:
        r2 = "color";
        r2 = r0.getQueryParameter(r2);
        r1.slug = r2;
    L_0x0705:
        r2 = r1.slug;
        if (r2 == 0) goto L_0x0723;
    L_0x0709:
        r2 = r2.length();
        r4 = 6;
        if (r2 != r4) goto L_0x0723;
    L_0x0710:
        r0 = r1.settings;	 Catch:{ Exception -> 0x071f }
        r2 = r1.slug;	 Catch:{ Exception -> 0x071f }
        r4 = 16;
        r2 = java.lang.Integer.parseInt(r2, r4);	 Catch:{ Exception -> 0x071f }
        r4 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r2 = r2 | r4;
        r0.background_color = r2;	 Catch:{ Exception -> 0x071f }
    L_0x071f:
        r2 = 0;
        r1.slug = r2;
        goto L_0x078b;
    L_0x0723:
        r2 = "mode";
        r2 = r0.getQueryParameter(r2);
        if (r2 == 0) goto L_0x0760;
    L_0x072b:
        r2 = r2.toLowerCase();
        r4 = " ";
        r2 = r2.split(r4);
        if (r2 == 0) goto L_0x0760;
    L_0x0737:
        r4 = r2.length;
        if (r4 <= 0) goto L_0x0760;
    L_0x073a:
        r4 = 0;
    L_0x073b:
        r5 = r2.length;
        if (r4 >= r5) goto L_0x0760;
    L_0x073e:
        r5 = r2[r4];
        r6 = "blur";
        r5 = r6.equals(r5);
        if (r5 == 0) goto L_0x074e;
    L_0x0748:
        r5 = r1.settings;
        r6 = 1;
        r5.blur = r6;
        goto L_0x075d;
    L_0x074e:
        r6 = 1;
        r5 = r2[r4];
        r7 = "motion";
        r5 = r7.equals(r5);
        if (r5 == 0) goto L_0x075d;
    L_0x0759:
        r5 = r1.settings;
        r5.motion = r6;
    L_0x075d:
        r4 = r4 + 1;
        goto L_0x073b;
    L_0x0760:
        r2 = r1.settings;
        r4 = "intensity";
        r4 = r0.getQueryParameter(r4);
        r4 = org.telegram.messenger.Utilities.parseInt(r4);
        r4 = r4.intValue();
        r2.intensity = r4;
        r2 = "bg_color";
        r0 = r0.getQueryParameter(r2);	 Catch:{ Exception -> 0x078b }
        r2 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x078b }
        if (r2 != 0) goto L_0x078b;
    L_0x077e:
        r2 = r1.settings;	 Catch:{ Exception -> 0x078b }
        r4 = 16;
        r0 = java.lang.Integer.parseInt(r0, r4);	 Catch:{ Exception -> 0x078b }
        r4 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0 = r0 | r4;
        r2.background_color = r0;	 Catch:{ Exception -> 0x078b }
    L_0x078b:
        r26 = r1;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r13 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 0;
        goto L_0x086b;
    L_0x079f:
        r1 = "tg:privatepost";
        r0 = r0.replace(r1, r12);
        r1 = "tg://privatepost";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "post";
        r1 = r0.getQueryParameter(r1);
        r1 = org.telegram.messenger.Utilities.parseInt(r1);
        r2 = "channel";
        r0 = r0.getQueryParameter(r2);
        r0 = org.telegram.messenger.Utilities.parseInt(r0);
        r2 = r1.intValue();
        if (r2 == 0) goto L_0x07e8;
    L_0x07c9:
        r2 = r0.intValue();
        if (r2 != 0) goto L_0x07d0;
    L_0x07cf:
        goto L_0x07e8;
    L_0x07d0:
        r27 = r0;
        r28 = r1;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r13 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 0;
        r26 = 0;
        goto L_0x086f;
    L_0x07e8:
        r0 = 0;
    L_0x07e9:
        r1 = 0;
        r4 = 0;
    L_0x07eb:
        r5 = 0;
    L_0x07ec:
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
    L_0x07f0:
        r12 = 0;
        r13 = 0;
    L_0x07f2:
        r23 = 0;
        goto L_0x0865;
    L_0x07f5:
        r1 = "tg:resolve";
        r0 = r0.replace(r1, r12);
        r1 = "tg://resolve";
        r0 = r0.replace(r1, r12);
        r0 = android.net.Uri.parse(r0);
        r1 = "domain";
        r1 = r0.getQueryParameter(r1);
        r2 = "telegrampassport";
        r2 = r2.equals(r1);
        if (r2 == 0) goto L_0x0875;
    L_0x0813:
        r1 = new java.util.HashMap;
        r1.<init>();
        r2 = r0.getQueryParameter(r13);
        r4 = android.text.TextUtils.isEmpty(r2);
        if (r4 != 0) goto L_0x083a;
    L_0x0822:
        r4 = "{";
        r4 = r2.startsWith(r4);
        if (r4 == 0) goto L_0x083a;
    L_0x082a:
        r4 = "}";
        r4 = r2.endsWith(r4);
        if (r4 == 0) goto L_0x083a;
    L_0x0832:
        r4 = r0.getQueryParameter(r5);
        r1.put(r5, r4);
        goto L_0x0841;
    L_0x083a:
        r4 = r0.getQueryParameter(r6);
        r1.put(r6, r4);
    L_0x0841:
        r4 = r0.getQueryParameter(r7);
        r1.put(r7, r4);
        r1.put(r13, r2);
        r2 = r0.getQueryParameter(r9);
        r1.put(r9, r2);
        r0 = r0.getQueryParameter(r8);
        r1.put(r8, r0);
    L_0x0859:
        r23 = r1;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r12 = 0;
        r13 = 0;
    L_0x0865:
        r24 = 0;
    L_0x0867:
        r25 = 0;
    L_0x0869:
        r26 = 0;
    L_0x086b:
        r27 = 0;
        r28 = 0;
    L_0x086f:
        r29 = 0;
        r30 = 0;
        goto L_0x0bd3;
    L_0x0875:
        r2 = "start";
        r2 = r0.getQueryParameter(r2);
        r4 = "startgroup";
        r4 = r0.getQueryParameter(r4);
        r5 = "game";
        r5 = r0.getQueryParameter(r5);
        r6 = "post";
        r0 = r0.getQueryParameter(r6);
        r0 = org.telegram.messenger.Utilities.parseInt(r0);
        r6 = r0.intValue();
        if (r6 != 0) goto L_0x08b3;
    L_0x0897:
        r7 = r1;
        r29 = r2;
        r30 = r4;
        r12 = r5;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = 0;
        r9 = 0;
        r13 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        r28 = 0;
        goto L_0x0bd3;
    L_0x08b3:
        r28 = r0;
        r7 = r1;
        r29 = r2;
        r30 = r4;
        r12 = r5;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r8 = 0;
        r9 = 0;
        r13 = 0;
        r23 = 0;
        r24 = 0;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        goto L_0x0bd3;
    L_0x08cf:
        r4 = r0.getHost();
        r4 = r4.toLowerCase();
        r5 = "telegram.me";
        r5 = r4.equals(r5);
        if (r5 != 0) goto L_0x08ef;
    L_0x08df:
        r5 = "t.me";
        r5 = r4.equals(r5);
        if (r5 != 0) goto L_0x08ef;
    L_0x08e7:
        r5 = "telegram.dog";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0b80;
    L_0x08ef:
        r4 = r0.getPath();
        if (r4 == 0) goto L_0x0b80;
    L_0x08f5:
        r5 = r4.length();
        r6 = 1;
        if (r5 <= r6) goto L_0x0b80;
    L_0x08fc:
        r4 = r4.substring(r6);
        r5 = "bg/";
        r5 = r4.startsWith(r5);
        if (r5 == 0) goto L_0x09ca;
    L_0x0908:
        r7 = new org.telegram.tgnet.TLRPC$TL_wallPaper;
        r7.<init>();
        r1 = new org.telegram.tgnet.TLRPC$TL_wallPaperSettings;
        r1.<init>();
        r7.settings = r1;
        r1 = "bg/";
        r1 = r4.replace(r1, r2);
        r7.slug = r1;
        r1 = r7.slug;
        if (r1 == 0) goto L_0x093b;
    L_0x0920:
        r1 = r1.length();
        r2 = 6;
        if (r1 != r2) goto L_0x093b;
    L_0x0927:
        r0 = r7.settings;	 Catch:{ Exception -> 0x0936 }
        r1 = r7.slug;	 Catch:{ Exception -> 0x0936 }
        r2 = 16;
        r1 = java.lang.Integer.parseInt(r1, r2);	 Catch:{ Exception -> 0x0936 }
        r2 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r1 = r1 | r2;
        r0.background_color = r1;	 Catch:{ Exception -> 0x0936 }
    L_0x0936:
        r8 = 0;
        r7.slug = r8;
        goto L_0x09b7;
    L_0x093b:
        r8 = 0;
        r1 = "mode";
        r1 = r0.getQueryParameter(r1);
        if (r1 == 0) goto L_0x0979;
    L_0x0944:
        r1 = r1.toLowerCase();
        r2 = " ";
        r1 = r1.split(r2);
        if (r1 == 0) goto L_0x0979;
    L_0x0950:
        r2 = r1.length;
        if (r2 <= 0) goto L_0x0979;
    L_0x0953:
        r2 = 0;
    L_0x0954:
        r4 = r1.length;
        if (r2 >= r4) goto L_0x0979;
    L_0x0957:
        r4 = r1[r2];
        r5 = "blur";
        r4 = r5.equals(r4);
        if (r4 == 0) goto L_0x0967;
    L_0x0961:
        r4 = r7.settings;
        r5 = 1;
        r4.blur = r5;
        goto L_0x0976;
    L_0x0967:
        r5 = 1;
        r4 = r1[r2];
        r6 = "motion";
        r4 = r6.equals(r4);
        if (r4 == 0) goto L_0x0976;
    L_0x0972:
        r4 = r7.settings;
        r4.motion = r5;
    L_0x0976:
        r2 = r2 + 1;
        goto L_0x0954;
    L_0x0979:
        r1 = "intensity";
        r1 = r0.getQueryParameter(r1);
        r2 = android.text.TextUtils.isEmpty(r1);
        if (r2 != 0) goto L_0x0992;
    L_0x0985:
        r2 = r7.settings;
        r1 = org.telegram.messenger.Utilities.parseInt(r1);
        r1 = r1.intValue();
        r2.intensity = r1;
        goto L_0x0998;
    L_0x0992:
        r1 = r7.settings;
        r2 = 50;
        r1.intensity = r2;
    L_0x0998:
        r1 = "bg_color";
        r0 = r0.getQueryParameter(r1);	 Catch:{ Exception -> 0x09b7 }
        r1 = android.text.TextUtils.isEmpty(r0);	 Catch:{ Exception -> 0x09b7 }
        if (r1 != 0) goto L_0x09b2;
    L_0x09a4:
        r1 = r7.settings;	 Catch:{ Exception -> 0x09b7 }
        r2 = 16;
        r0 = java.lang.Integer.parseInt(r0, r2);	 Catch:{ Exception -> 0x09b7 }
        r2 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0 = r0 | r2;
        r1.background_color = r0;	 Catch:{ Exception -> 0x09b7 }
        goto L_0x09b7;
    L_0x09b2:
        r0 = r7.settings;	 Catch:{ Exception -> 0x09b7 }
        r1 = -1;
        r0.background_color = r1;	 Catch:{ Exception -> 0x09b7 }
    L_0x09b7:
        r24 = r7;
        r0 = r8;
        r1 = r0;
        r2 = r1;
        r4 = r2;
        r5 = r4;
        r6 = r5;
        r7 = r6;
        r9 = r7;
        r13 = r9;
        r18 = r13;
        r23 = r18;
        r25 = r23;
        goto L_0x0b92;
    L_0x09ca:
        r8 = 0;
        r5 = "login/";
        r5 = r4.startsWith(r5);
        if (r5 == 0) goto L_0x09ea;
    L_0x09d3:
        r0 = "login/";
        r7 = r4.replace(r0, r2);
        r23 = r7;
        r0 = r8;
        r1 = r0;
        r2 = r1;
        r4 = r2;
        r5 = r4;
        r6 = r5;
        r7 = r6;
        r9 = r7;
        r13 = r9;
        r18 = r13;
        r24 = r18;
        goto L_0x0b90;
    L_0x09ea:
        r5 = "joinchat/";
        r5 = r4.startsWith(r5);
        if (r5 == 0) goto L_0x09fc;
    L_0x09f2:
        r0 = "joinchat/";
        r7 = r4.replace(r0, r2);
        r0 = r7;
        r1 = r8;
        goto L_0x0b83;
    L_0x09fc:
        r5 = "addstickers/";
        r5 = r4.startsWith(r5);
        if (r5 == 0) goto L_0x0a0f;
    L_0x0a04:
        r0 = "addstickers/";
        r7 = r4.replace(r0, r2);
        r1 = r7;
        r0 = r8;
        r2 = r0;
        goto L_0x0b84;
    L_0x0a0f:
        r5 = "msg/";
        r5 = r4.startsWith(r5);
        if (r5 != 0) goto L_0x0b07;
    L_0x0a17:
        r5 = "share/";
        r5 = r4.startsWith(r5);
        if (r5 == 0) goto L_0x0a21;
    L_0x0a1f:
        goto L_0x0b07;
    L_0x0a21:
        r1 = "confirmphone";
        r1 = r4.startsWith(r1);
        if (r1 == 0) goto L_0x0a40;
    L_0x0a29:
        r1 = "phone";
        r7 = r0.getQueryParameter(r1);
        r0 = r0.getQueryParameter(r3);
        r13 = r0;
        r6 = r7;
        r0 = r8;
        r1 = r0;
        r2 = r1;
        r4 = r2;
        r5 = r4;
        r7 = r5;
        r9 = r7;
        r18 = r9;
        goto L_0x0b8c;
    L_0x0a40:
        r1 = "setlanguage/";
        r1 = r4.startsWith(r1);
        if (r1 == 0) goto L_0x0a5d;
    L_0x0a48:
        r0 = 12;
        r7 = r4.substring(r0);
        r18 = r7;
        r0 = r8;
        r1 = r0;
        r2 = r1;
        r4 = r2;
        r5 = r4;
        r6 = r5;
        r7 = r6;
        r9 = r7;
        r13 = r9;
        r23 = r13;
        goto L_0x0b8e;
    L_0x0a5d:
        r1 = "c/";
        r1 = r4.startsWith(r1);
        if (r1 == 0) goto L_0x0ab0;
    L_0x0a65:
        r0 = r0.getPathSegments();
        r1 = r0.size();
        r2 = 3;
        if (r1 != r2) goto L_0x0a99;
    L_0x0a70:
        r1 = 1;
        r2 = r0.get(r1);
        r2 = (java.lang.String) r2;
        r7 = org.telegram.messenger.Utilities.parseInt(r2);
        r1 = 2;
        r0 = r0.get(r1);
        r0 = (java.lang.String) r0;
        r0 = org.telegram.messenger.Utilities.parseInt(r0);
        r1 = r0.intValue();
        if (r1 == 0) goto L_0x0a99;
    L_0x0a8c:
        r1 = r7.intValue();
        if (r1 != 0) goto L_0x0a93;
    L_0x0a92:
        goto L_0x0a99;
    L_0x0a93:
        r42 = r7;
        r7 = r0;
        r0 = r42;
        goto L_0x0a9b;
    L_0x0a99:
        r0 = r8;
        r7 = r0;
    L_0x0a9b:
        r26 = r0;
        r25 = r7;
        r0 = r8;
        r1 = r0;
        r2 = r1;
        r4 = r2;
        r5 = r4;
        r6 = r5;
        r7 = r6;
        r9 = r7;
        r13 = r9;
        r18 = r13;
        r23 = r18;
        r24 = r23;
        goto L_0x0b94;
    L_0x0ab0:
        r1 = r4.length();
        r2 = 1;
        if (r1 < r2) goto L_0x0b81;
    L_0x0ab7:
        r1 = r0.getPathSegments();
        r4 = r1.size();
        if (r4 <= 0) goto L_0x0ae1;
    L_0x0ac1:
        r4 = 0;
        r5 = r1.get(r4);
        r7 = r5;
        r7 = (java.lang.String) r7;
        r4 = r1.size();
        if (r4 <= r2) goto L_0x0adf;
    L_0x0acf:
        r1 = r1.get(r2);
        r1 = (java.lang.String) r1;
        r1 = org.telegram.messenger.Utilities.parseInt(r1);
        r2 = r1.intValue();
        if (r2 != 0) goto L_0x0ae3;
    L_0x0adf:
        r1 = r8;
        goto L_0x0ae3;
    L_0x0ae1:
        r1 = r8;
        r7 = r1;
    L_0x0ae3:
        r2 = "start";
        r2 = r0.getQueryParameter(r2);
        r4 = "startgroup";
        r4 = r0.getQueryParameter(r4);
        r5 = "game";
        r0 = r0.getQueryParameter(r5);
        r9 = r0;
        r25 = r1;
        r0 = r8;
        r1 = r0;
        r5 = r1;
        r6 = r5;
        r13 = r6;
        r18 = r13;
        r23 = r18;
        r24 = r23;
        r26 = r24;
        goto L_0x0b94;
    L_0x0b07:
        r4 = "url";
        r4 = r0.getQueryParameter(r4);
        if (r4 != 0) goto L_0x0b10;
    L_0x0b0f:
        goto L_0x0b11;
    L_0x0b10:
        r2 = r4;
    L_0x0b11:
        r4 = "text";
        r4 = r0.getQueryParameter(r4);
        if (r4 == 0) goto L_0x0b47;
    L_0x0b19:
        r4 = r2.length();
        if (r4 <= 0) goto L_0x0b30;
    L_0x0b1f:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r2);
        r4.append(r1);
        r2 = r4.toString();
        r12 = 1;
        goto L_0x0b31;
    L_0x0b30:
        r12 = 0;
    L_0x0b31:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r2);
        r2 = "text";
        r0 = r0.getQueryParameter(r2);
        r4.append(r0);
        r2 = r4.toString();
        goto L_0x0b48;
    L_0x0b47:
        r12 = 0;
    L_0x0b48:
        r0 = r2.length();
        r4 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r0 <= r4) goto L_0x0b59;
    L_0x0b50:
        r0 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r4 = 0;
        r0 = r2.substring(r4, r0);
        r7 = r0;
        goto L_0x0b5b;
    L_0x0b59:
        r4 = 0;
        r7 = r2;
    L_0x0b5b:
        r0 = r7.endsWith(r1);
        if (r0 == 0) goto L_0x0b6c;
    L_0x0b61:
        r0 = r7.length();
        r2 = 1;
        r0 = r0 - r2;
        r7 = r7.substring(r4, r0);
        goto L_0x0b5b;
    L_0x0b6c:
        r5 = r7;
        r0 = r8;
        r1 = r0;
        r2 = r1;
        r4 = r2;
        r6 = r4;
        r7 = r6;
        r9 = r7;
        r13 = r9;
        r18 = r13;
        r23 = r18;
        r24 = r23;
        r25 = r24;
        r26 = r25;
        goto L_0x0b95;
    L_0x0b80:
        r8 = 0;
    L_0x0b81:
        r0 = r8;
        r1 = r0;
    L_0x0b83:
        r2 = r1;
    L_0x0b84:
        r4 = r2;
        r5 = r4;
        r6 = r5;
        r7 = r6;
        r9 = r7;
        r13 = r9;
        r18 = r13;
    L_0x0b8c:
        r23 = r18;
    L_0x0b8e:
        r24 = r23;
    L_0x0b90:
        r25 = r24;
    L_0x0b92:
        r26 = r25;
    L_0x0b94:
        r12 = 0;
    L_0x0b95:
        r29 = r2;
        r30 = r4;
        r28 = r25;
        r27 = r26;
        r31 = 0;
        r32 = 0;
        r33 = 0;
        r4 = r0;
        r25 = r18;
        r0 = r23;
        r26 = r24;
        r23 = r8;
        r24 = r23;
        r42 = r5;
        r5 = r1;
        r1 = r42;
        r43 = r12;
        r12 = r9;
        r9 = r43;
        goto L_0x0bd9;
    L_0x0bb9:
        r8 = r7;
        r0 = r8;
        r1 = r0;
        r4 = r1;
        r5 = r4;
        r6 = r5;
        r7 = r6;
        r12 = r7;
        r13 = r12;
        r23 = r13;
        r24 = r23;
        r25 = r24;
        r26 = r25;
        r27 = r26;
        r28 = r27;
        r29 = r28;
        r30 = r29;
        r9 = 0;
    L_0x0bd3:
        r31 = 0;
        r32 = 0;
        r33 = 0;
    L_0x0bd9:
        if (r0 != 0) goto L_0x0bef;
    L_0x0bdb:
        r2 = r15.currentAccount;
        r2 = org.telegram.messenger.UserConfig.getInstance(r2);
        r2 = r2.isClientActivated();
        if (r2 == 0) goto L_0x0be8;
    L_0x0be7:
        goto L_0x0bef;
    L_0x0be8:
        r40 = r11;
        r2 = r15;
        r29 = 0;
        goto L_0x0d03;
    L_0x0bef:
        if (r6 != 0) goto L_0x0ce8;
    L_0x0bf1:
        if (r13 == 0) goto L_0x0bf5;
    L_0x0bf3:
        goto L_0x0ce8;
    L_0x0bf5:
        if (r7 != 0) goto L_0x0ca3;
    L_0x0bf7:
        if (r4 != 0) goto L_0x0ca3;
    L_0x0bf9:
        if (r5 != 0) goto L_0x0ca3;
    L_0x0bfb:
        if (r1 != 0) goto L_0x0ca3;
    L_0x0bfd:
        if (r12 != 0) goto L_0x0ca3;
    L_0x0bff:
        if (r23 != 0) goto L_0x0ca3;
    L_0x0c01:
        if (r24 != 0) goto L_0x0ca3;
    L_0x0c03:
        if (r25 != 0) goto L_0x0ca3;
    L_0x0c05:
        if (r0 != 0) goto L_0x0ca3;
    L_0x0c07:
        if (r26 != 0) goto L_0x0ca3;
    L_0x0c09:
        if (r27 == 0) goto L_0x0c0d;
    L_0x0c0b:
        goto L_0x0ca3;
    L_0x0c0d:
        r34 = r44.getContentResolver();	 Catch:{ Exception -> 0x0c95 }
        r35 = r45.getData();	 Catch:{ Exception -> 0x0c95 }
        r36 = 0;
        r37 = 0;
        r38 = 0;
        r39 = 0;
        r1 = r34.query(r35, r36, r37, r38, r39);	 Catch:{ Exception -> 0x0c95 }
        if (r1 == 0) goto L_0x0c8c;
    L_0x0c23:
        r0 = r1.moveToFirst();	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        if (r0 == 0) goto L_0x0c8c;
    L_0x0c29:
        r0 = "account_name";
        r0 = r1.getColumnIndex(r0);	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        r0 = r1.getString(r0);	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        r0 = org.telegram.messenger.Utilities.parseInt(r0);	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        r0 = r0.intValue();	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        r2 = 0;
    L_0x0c3c:
        r3 = 3;
        if (r2 >= r3) goto L_0x0c57;
    L_0x0c3f:
        r3 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        r3 = r3.getClientUserId();	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        if (r3 != r0) goto L_0x0c53;
    L_0x0c49:
        r3 = 0;
        r11[r3] = r2;	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        r0 = r11[r3];	 Catch:{ Throwable -> 0x0c7b, all -> 0x0c77 }
        r13 = 1;
        r15.switchToAccount(r0, r13);	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        goto L_0x0c58;
    L_0x0c53:
        r13 = 1;
        r2 = r2 + 1;
        goto L_0x0c3c;
    L_0x0c57:
        r13 = 1;
    L_0x0c58:
        r0 = "DATA4";
        r0 = r1.getColumnIndex(r0);	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        r0 = r1.getInt(r0);	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        r2 = 0;
        r3 = r11[r2];	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        r3 = org.telegram.messenger.NotificationCenter.getInstance(r3);	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        r4 = org.telegram.messenger.NotificationCenter.closeChats;	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        r5 = new java.lang.Object[r2];	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        r3.postNotificationName(r4, r5);	 Catch:{ Throwable -> 0x0c75, all -> 0x0c73 }
        r31 = r0;
        goto L_0x0c8d;
    L_0x0c73:
        r0 = move-exception;
        goto L_0x0c79;
    L_0x0c75:
        r0 = move-exception;
        goto L_0x0c7d;
    L_0x0c77:
        r0 = move-exception;
        r13 = 1;
    L_0x0c79:
        r7 = r8;
        goto L_0x0c80;
    L_0x0c7b:
        r0 = move-exception;
        r13 = 1;
    L_0x0c7d:
        r7 = r0;
        throw r7;	 Catch:{ all -> 0x0c7f }
    L_0x0c7f:
        r0 = move-exception;
    L_0x0c80:
        if (r1 == 0) goto L_0x0c8b;
    L_0x0c82:
        if (r7 == 0) goto L_0x0c88;
    L_0x0c84:
        r1.close();	 Catch:{ Throwable -> 0x0c8b }
        goto L_0x0c8b;
    L_0x0c88:
        r1.close();	 Catch:{ Exception -> 0x0c93 }
    L_0x0c8b:
        throw r0;	 Catch:{ Exception -> 0x0c93 }
    L_0x0c8c:
        r13 = 1;
    L_0x0c8d:
        if (r1 == 0) goto L_0x0c9a;
    L_0x0c8f:
        r1.close();	 Catch:{ Exception -> 0x0c93 }
        goto L_0x0c9a;
    L_0x0c93:
        r0 = move-exception;
        goto L_0x0c97;
    L_0x0c95:
        r0 = move-exception;
        r13 = 1;
    L_0x0c97:
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0c9a:
        r40 = r11;
        r2 = r15;
        r12 = r31;
        r29 = 0;
        goto L_0x0d10;
    L_0x0ca3:
        r13 = 1;
        if (r1 == 0) goto L_0x0cbf;
    L_0x0ca6:
        r2 = "@";
        r2 = r1.startsWith(r2);
        if (r2 == 0) goto L_0x0cbf;
    L_0x0cae:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = " ";
        r2.append(r3);
        r2.append(r1);
        r1 = r2.toString();
    L_0x0cbf:
        r21 = r1;
        r22 = 0;
        r2 = r11[r22];
        r18 = 0;
        r1 = r44;
        r3 = r7;
        r6 = r29;
        r7 = r30;
        r29 = 0;
        r8 = r21;
        r10 = r28;
        r40 = r11;
        r11 = r27;
        r13 = r23;
        r14 = r25;
        r15 = r24;
        r16 = r0;
        r17 = r26;
        r1.runLinkRequest(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        r2 = r44;
        goto L_0x0d03;
    L_0x0ce8:
        r40 = r11;
        r29 = 0;
        r0 = new android.os.Bundle;
        r0.<init>();
        r1 = "phone";
        r0.putString(r1, r6);
        r0.putString(r3, r13);
        r1 = new org.telegram.ui.-$$Lambda$LaunchActivity$N1thb-LLgMOn57u-_wgkv5RqrBk;
        r2 = r44;
        r1.<init>(r2, r0);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r1);
    L_0x0d03:
        r12 = r31;
        goto L_0x0d10;
    L_0x0d06:
        r40 = r11;
        r2 = r15;
        r29 = 0;
        r12 = 0;
        r32 = 0;
        r33 = 0;
    L_0x0d10:
        r1 = r45;
        r0 = r32;
        r4 = r33;
        r6 = r40;
        r3 = 0;
        r5 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        goto L_0x0df7;
    L_0x0d20:
        r40 = r11;
        r2 = r15;
        r29 = 0;
        r0 = r45.getAction();
        r1 = "org.telegram.messenger.OPEN_ACCOUNT";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0d3e;
    L_0x0d31:
        r1 = r45;
        r6 = r40;
        r0 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 1;
        goto L_0x0df5;
    L_0x0d3e:
        r0 = r45.getAction();
        r1 = "new_dialog";
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0d58;
    L_0x0d4a:
        r1 = r45;
        r6 = r40;
        r0 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 1;
        goto L_0x0df6;
    L_0x0d58:
        r0 = r45.getAction();
        r1 = "com.tmessages.openchat";
        r0 = r0.startsWith(r1);
        if (r0 == 0) goto L_0x0dc1;
    L_0x0d64:
        r0 = "chatId";
        r1 = r45;
        r3 = 0;
        r0 = r1.getIntExtra(r0, r3);
        r4 = "userId";
        r4 = r1.getIntExtra(r4, r3);
        r5 = "encId";
        r5 = r1.getIntExtra(r5, r3);
        if (r0 == 0) goto L_0x0d8e;
    L_0x0d7b:
        r6 = r40;
        r4 = r6[r3];
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r7 = new java.lang.Object[r3];
        r4.postNotificationName(r5, r7);
        r4 = 0;
    L_0x0d8b:
        r5 = 0;
    L_0x0d8c:
        r12 = 0;
        goto L_0x0db9;
    L_0x0d8e:
        r6 = r40;
        if (r4 == 0) goto L_0x0da4;
    L_0x0d92:
        r0 = r6[r3];
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r0);
        r5 = org.telegram.messenger.NotificationCenter.closeChats;
        r7 = new java.lang.Object[r3];
        r0.postNotificationName(r5, r7);
        r12 = r4;
        r0 = 0;
        r4 = 0;
        r5 = 0;
        goto L_0x0db9;
    L_0x0da4:
        if (r5 == 0) goto L_0x0db6;
    L_0x0da6:
        r0 = r6[r3];
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r0);
        r4 = org.telegram.messenger.NotificationCenter.closeChats;
        r7 = new java.lang.Object[r3];
        r0.postNotificationName(r4, r7);
        r0 = 0;
        r4 = 0;
        goto L_0x0d8c;
    L_0x0db6:
        r0 = 0;
        r4 = 1;
        goto L_0x0d8b;
    L_0x0db9:
        r41 = r4;
        r4 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        goto L_0x0df9;
    L_0x0dc1:
        r1 = r45;
        r6 = r40;
        r3 = 0;
        r0 = r45.getAction();
        r4 = "com.tmessages.openplayer";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x0dd7;
    L_0x0dd2:
        r0 = 0;
        r4 = 0;
        r5 = 0;
        r7 = 1;
        goto L_0x0df3;
    L_0x0dd7:
        r0 = r45.getAction();
        r4 = "org.tmessages.openlocations";
        r0 = r0.equals(r4);
        if (r0 == 0) goto L_0x0def;
    L_0x0de3:
        r0 = 0;
        r4 = 0;
        r5 = 0;
        r7 = 0;
        r8 = 1;
        goto L_0x0df4;
    L_0x0de9:
        r29 = r8;
        r6 = r11;
        r1 = r14;
        r2 = r15;
        r3 = 0;
    L_0x0def:
        r0 = 0;
        r4 = 0;
        r5 = 0;
        r7 = 0;
    L_0x0df3:
        r8 = 0;
    L_0x0df4:
        r9 = 0;
    L_0x0df5:
        r10 = 0;
    L_0x0df6:
        r12 = 0;
    L_0x0df7:
        r41 = 0;
    L_0x0df9:
        r11 = r2.currentAccount;
        r11 = org.telegram.messenger.UserConfig.getInstance(r11);
        r11 = r11.isClientActivated();
        if (r11 == 0) goto L_0x10fc;
    L_0x0e05:
        if (r12 == 0) goto L_0x0e51;
    L_0x0e07:
        r0 = new android.os.Bundle;
        r0.<init>();
        r5 = "user_id";
        r0.putInt(r5, r12);
        if (r4 == 0) goto L_0x0e18;
    L_0x0e13:
        r5 = "message_id";
        r0.putInt(r5, r4);
    L_0x0e18:
        r4 = mainFragmentsStack;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0e3b;
    L_0x0e20:
        r4 = r6[r3];
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = mainFragmentsStack;
        r6 = r5.size();
        r11 = 1;
        r6 = r6 - r11;
        r5 = r5.get(r6);
        r5 = (org.telegram.p004ui.ActionBar.BaseFragment) r5;
        r4 = r4.checkCanOpenChat(r0, r5);
        if (r4 == 0) goto L_0x0e4f;
    L_0x0e3a:
        goto L_0x0e3c;
    L_0x0e3b:
        r11 = 1;
    L_0x0e3c:
        r6 = new org.telegram.ui.ChatActivity;
        r6.<init>(r0);
        r5 = r2.actionBarLayout;
        r7 = 0;
        r8 = 1;
        r9 = 1;
        r10 = 0;
        r0 = r5.presentFragment(r6, r7, r8, r9, r10);
        if (r0 == 0) goto L_0x0e4f;
    L_0x0e4d:
        r13 = 1;
        goto L_0x0eb7;
    L_0x0e4f:
        r13 = 0;
        goto L_0x0eb7;
    L_0x0e51:
        r11 = 1;
        if (r0 == 0) goto L_0x0e9a;
    L_0x0e54:
        r5 = new android.os.Bundle;
        r5.<init>();
        r7 = "chat_id";
        r5.putInt(r7, r0);
        if (r4 == 0) goto L_0x0e65;
    L_0x0e60:
        r0 = "message_id";
        r5.putInt(r0, r4);
    L_0x0e65:
        r0 = mainFragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0e86;
    L_0x0e6d:
        r0 = r6[r3];
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r4 = mainFragmentsStack;
        r6 = r4.size();
        r6 = r6 - r11;
        r4 = r4.get(r6);
        r4 = (org.telegram.p004ui.ActionBar.BaseFragment) r4;
        r0 = r0.checkCanOpenChat(r5, r4);
        if (r0 == 0) goto L_0x0e4f;
    L_0x0e86:
        r13 = new org.telegram.ui.ChatActivity;
        r13.<init>(r5);
        r12 = r2.actionBarLayout;
        r14 = 0;
        r15 = 1;
        r16 = 1;
        r17 = 0;
        r0 = r12.presentFragment(r13, r14, r15, r16, r17);
        if (r0 == 0) goto L_0x0e4f;
    L_0x0e99:
        goto L_0x0e4d;
    L_0x0e9a:
        if (r5 == 0) goto L_0x0ebc;
    L_0x0e9c:
        r0 = new android.os.Bundle;
        r0.<init>();
        r4 = "enc_id";
        r0.putInt(r4, r5);
        r13 = new org.telegram.ui.ChatActivity;
        r13.<init>(r0);
        r12 = r2.actionBarLayout;
        r14 = 0;
        r15 = 1;
        r16 = 1;
        r17 = 0;
        r13 = r12.presentFragment(r13, r14, r15, r16, r17);
    L_0x0eb7:
        r0 = r46;
        r4 = 0;
        goto L_0x1101;
    L_0x0ebc:
        if (r41 == 0) goto L_0x0ef4;
    L_0x0ebe:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 != 0) goto L_0x0eca;
    L_0x0ec4:
        r0 = r2.actionBarLayout;
        r0.removeAllFragments();
        goto L_0x0ef2;
    L_0x0eca:
        r0 = r2.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0ef2;
    L_0x0ed4:
        r0 = r2.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        r0 = r0 - r11;
        if (r0 <= 0) goto L_0x0eed;
    L_0x0edf:
        r0 = r2.layersActionBarLayout;
        r4 = r0.fragmentsStack;
        r4 = r4.get(r3);
        r4 = (org.telegram.p004ui.ActionBar.BaseFragment) r4;
        r0.removeFragmentFromStack(r4);
        goto L_0x0ed4;
    L_0x0eed:
        r0 = r2.layersActionBarLayout;
        r0.closeLastFragment(r3);
    L_0x0ef2:
        r0 = 0;
        goto L_0x0f14;
    L_0x0ef4:
        if (r7 == 0) goto L_0x0f17;
    L_0x0ef6:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0f12;
    L_0x0f00:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.get(r3);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r4 = new org.telegram.ui.Components.AudioPlayerAlert;
        r4.<init>(r2);
        r0.showDialog(r4);
    L_0x0f12:
        r0 = r46;
    L_0x0f14:
        r4 = 0;
        goto L_0x1100;
    L_0x0f17:
        if (r8 == 0) goto L_0x0f3b;
    L_0x0f19:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0f12;
    L_0x0f23:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.get(r3);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r4 = new org.telegram.ui.Components.SharingLocationsAlert;
        r5 = new org.telegram.ui.-$$Lambda$LaunchActivity$_qofCdzFwUj12rghSqYUkzh14e4;
        r5.<init>(r2, r6);
        r4.<init>(r2, r5);
        r0.showDialog(r4);
        goto L_0x0f12;
    L_0x0f3b:
        r0 = r2.videoPath;
        if (r0 != 0) goto L_0x0fc4;
    L_0x0f3f:
        r0 = r2.photoPathsArray;
        if (r0 != 0) goto L_0x0fc4;
    L_0x0f43:
        r0 = r2.sendingText;
        if (r0 != 0) goto L_0x0fc4;
    L_0x0f47:
        r0 = r2.sendingLocation;
        if (r0 != 0) goto L_0x0fc4;
    L_0x0f4b:
        r0 = r2.documentsPathsArray;
        if (r0 != 0) goto L_0x0fc4;
    L_0x0f4f:
        r0 = r2.contactsToSend;
        if (r0 != 0) goto L_0x0fc4;
    L_0x0f53:
        r0 = r2.documentsUrisArray;
        if (r0 == 0) goto L_0x0f58;
    L_0x0f57:
        goto L_0x0fc4;
    L_0x0f58:
        if (r9 == 0) goto L_0x0f8b;
    L_0x0f5a:
        r12 = r2.actionBarLayout;
        r13 = new org.telegram.ui.SettingsActivity;
        r13.<init>();
        r14 = 0;
        r15 = 1;
        r16 = 1;
        r17 = 0;
        r12.presentFragment(r13, r14, r15, r16, r17);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x0f80;
    L_0x0f70:
        r0 = r2.actionBarLayout;
        r0.showLastFragment();
        r0 = r2.rightActionBarLayout;
        r0.showLastFragment();
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r3, r3);
        goto L_0x0f85;
    L_0x0f80:
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r11, r3);
    L_0x0f85:
        r0 = r46;
        r4 = 0;
        r13 = 1;
        goto L_0x1101;
    L_0x0f8b:
        if (r10 == 0) goto L_0x0fc1;
    L_0x0f8d:
        r0 = new android.os.Bundle;
        r0.<init>();
        r4 = "destroyAfterSelect";
        r0.putBoolean(r4, r11);
        r5 = r2.actionBarLayout;
        r6 = new org.telegram.ui.ContactsActivity;
        r6.<init>(r0);
        r7 = 0;
        r8 = 1;
        r9 = 1;
        r10 = 0;
        r5.presentFragment(r6, r7, r8, r9, r10);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x0fbb;
    L_0x0fab:
        r0 = r2.actionBarLayout;
        r0.showLastFragment();
        r0 = r2.rightActionBarLayout;
        r0.showLastFragment();
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r3, r3);
        goto L_0x0f85;
    L_0x0fbb:
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r11, r3);
        goto L_0x0f85;
    L_0x0fc1:
        r4 = 0;
        goto L_0x10fe;
    L_0x0fc4:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 != 0) goto L_0x0fd7;
    L_0x0fca:
        r0 = r6[r3];
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r0);
        r4 = org.telegram.messenger.NotificationCenter.closeChats;
        r5 = new java.lang.Object[r3];
        r0.postNotificationName(r4, r5);
    L_0x0fd7:
        r0 = (r19 > r29 ? 1 : (r19 == r29 ? 0 : -1));
        if (r0 != 0) goto L_0x10eb;
    L_0x0fdb:
        r0 = new android.os.Bundle;
        r0.<init>();
        r4 = "onlySelect";
        r0.putBoolean(r4, r11);
        r4 = 3;
        r5 = "dialogsType";
        r0.putInt(r5, r4);
        r4 = "allowSwitchAccount";
        r0.putBoolean(r4, r11);
        r4 = r2.contactsToSend;
        if (r4 == 0) goto L_0x1017;
    L_0x0ff4:
        r4 = r4.size();
        if (r4 == r11) goto L_0x1033;
    L_0x0ffa:
        r4 = 2131560704; // 0x7f0d0900 float:1.8746788E38 double:1.053130916E-314;
        r5 = "SendContactTo";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        r5 = "selectAlertString";
        r0.putString(r5, r4);
        r4 = 2131560690; // 0x7f0d08f2 float:1.874676E38 double:1.053130909E-314;
        r5 = "SendContactToGroup";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        r5 = "selectAlertStringGroup";
        r0.putString(r5, r4);
        goto L_0x1033;
    L_0x1017:
        r4 = 2131560704; // 0x7f0d0900 float:1.8746788E38 double:1.053130916E-314;
        r5 = "SendMessagesTo";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        r5 = "selectAlertString";
        r0.putString(r5, r4);
        r4 = 2131560705; // 0x7f0d0901 float:1.874679E38 double:1.0531309164E-314;
        r5 = "SendMessagesToGroup";
        r4 = org.telegram.messenger.LocaleController.getString(r5, r4);
        r5 = "selectAlertStringGroup";
        r0.putString(r5, r4);
    L_0x1033:
        r13 = new org.telegram.ui.DialogsActivity;
        r13.<init>(r0);
        r13.setDelegate(r2);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x105d;
    L_0x1041:
        r0 = r2.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 <= 0) goto L_0x107a;
    L_0x104b:
        r0 = r2.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r4 = r0.size();
        r4 = r4 - r11;
        r0 = r0.get(r4);
        r0 = r0 instanceof org.telegram.p004ui.DialogsActivity;
        if (r0 == 0) goto L_0x107a;
    L_0x105c:
        goto L_0x1078;
    L_0x105d:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 <= r11) goto L_0x107a;
    L_0x1067:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r4 = r0.size();
        r4 = r4 - r11;
        r0 = r0.get(r4);
        r0 = r0 instanceof org.telegram.p004ui.DialogsActivity;
        if (r0 == 0) goto L_0x107a;
    L_0x1078:
        r0 = 1;
        goto L_0x107b;
    L_0x107a:
        r0 = 0;
    L_0x107b:
        r14 = r0;
        r12 = r2.actionBarLayout;
        r15 = 1;
        r16 = 1;
        r17 = 0;
        r12.presentFragment(r13, r14, r15, r16, r17);
        r0 = org.telegram.p004ui.SecretMediaViewer.hasInstance();
        if (r0 == 0) goto L_0x109e;
    L_0x108c:
        r0 = org.telegram.p004ui.SecretMediaViewer.getInstance();
        r0 = r0.isVisible();
        if (r0 == 0) goto L_0x109e;
    L_0x1096:
        r0 = org.telegram.p004ui.SecretMediaViewer.getInstance();
        r0.closePhoto(r3, r3);
        goto L_0x10cd;
    L_0x109e:
        r0 = org.telegram.p004ui.PhotoViewer.hasInstance();
        if (r0 == 0) goto L_0x10b6;
    L_0x10a4:
        r0 = org.telegram.p004ui.PhotoViewer.getInstance();
        r0 = r0.isVisible();
        if (r0 == 0) goto L_0x10b6;
    L_0x10ae:
        r0 = org.telegram.p004ui.PhotoViewer.getInstance();
        r0.closePhoto(r3, r11);
        goto L_0x10cd;
    L_0x10b6:
        r0 = org.telegram.p004ui.ArticleViewer.hasInstance();
        if (r0 == 0) goto L_0x10cd;
    L_0x10bc:
        r0 = org.telegram.p004ui.ArticleViewer.getInstance();
        r0 = r0.isVisible();
        if (r0 == 0) goto L_0x10cd;
    L_0x10c6:
        r0 = org.telegram.p004ui.ArticleViewer.getInstance();
        r0.close(r3, r11);
    L_0x10cd:
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r3, r3);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x10e4;
    L_0x10d8:
        r0 = r2.actionBarLayout;
        r0.showLastFragment();
        r0 = r2.rightActionBarLayout;
        r0.showLastFragment();
        goto L_0x0f85;
    L_0x10e4:
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r11, r3);
        goto L_0x0f85;
    L_0x10eb:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r4 = java.lang.Long.valueOf(r19);
        r0.add(r4);
        r4 = 0;
        r2.didSelectDialogs(r4, r0, r4, r3);
        goto L_0x10fe;
    L_0x10fc:
        r4 = 0;
        r11 = 1;
    L_0x10fe:
        r0 = r46;
    L_0x1100:
        r13 = 0;
    L_0x1101:
        if (r13 != 0) goto L_0x119f;
    L_0x1103:
        if (r0 != 0) goto L_0x119f;
    L_0x1105:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x1150;
    L_0x110b:
        r0 = r2.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.isClientActivated();
        if (r0 != 0) goto L_0x1131;
    L_0x1117:
        r0 = r2.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x118a;
    L_0x1121:
        r0 = r2.layersActionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r0.addFragmentToStack(r5);
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r3, r3);
        goto L_0x118a;
    L_0x1131:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x118a;
    L_0x113b:
        r0 = new org.telegram.ui.DialogsActivity;
        r0.<init>(r4);
        r5 = r2.sideMenu;
        r0.setSideMenu(r5);
        r5 = r2.actionBarLayout;
        r5.addFragmentToStack(r0);
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r11, r3);
        goto L_0x118a;
    L_0x1150:
        r0 = r2.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.isEmpty();
        if (r0 == 0) goto L_0x118a;
    L_0x115a:
        r0 = r2.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.isClientActivated();
        if (r0 != 0) goto L_0x1176;
    L_0x1166:
        r0 = r2.actionBarLayout;
        r5 = new org.telegram.ui.LoginActivity;
        r5.<init>();
        r0.addFragmentToStack(r5);
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r3, r3);
        goto L_0x118a;
    L_0x1176:
        r0 = new org.telegram.ui.DialogsActivity;
        r0.<init>(r4);
        r5 = r2.sideMenu;
        r0.setSideMenu(r5);
        r5 = r2.actionBarLayout;
        r5.addFragmentToStack(r0);
        r0 = r2.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r11, r3);
    L_0x118a:
        r0 = r2.actionBarLayout;
        r0.showLastFragment();
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x119f;
    L_0x1195:
        r0 = r2.layersActionBarLayout;
        r0.showLastFragment();
        r0 = r2.rightActionBarLayout;
        r0.showLastFragment();
    L_0x119f:
        r1.setAction(r4);
        return r13;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.handleIntent(android.content.Intent, boolean, boolean, boolean):boolean");
    }

    public /* synthetic */ void lambda$handleIntent$5$LaunchActivity(Bundle bundle) {
        lambda$runLinkRequest$27$LaunchActivity(new CancelAccountDeletionActivity(bundle));
    }

    public /* synthetic */ void lambda$handleIntent$7$LaunchActivity(int[] iArr, SharingLocationInfo sharingLocationInfo) {
        iArr[0] = sharingLocationInfo.messageObject.currentAccount;
        switchToAccount(iArr[0], true);
        LocationActivity locationActivity = new LocationActivity(2);
        locationActivity.setMessageObject(sharingLocationInfo.messageObject);
        locationActivity.setDelegate(new C3708-$$Lambda$LaunchActivity$4e8vXS2t3YJcvvp0fszXn1LM3wk(iArr, sharingLocationInfo.messageObject.getDialogId()));
        lambda$runLinkRequest$27$LaunchActivity(locationActivity);
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x0297  */
    private void runLinkRequest(int r25, java.lang.String r26, java.lang.String r27, java.lang.String r28, java.lang.String r29, java.lang.String r30, java.lang.String r31, boolean r32, java.lang.Integer r33, java.lang.Integer r34, java.lang.String r35, java.util.HashMap<java.lang.String, java.lang.String> r36, java.lang.String r37, java.lang.String r38, java.lang.String r39, org.telegram.tgnet.TLRPC.TL_wallPaper r40, int r41) {
        /*
        r24 = this;
        r15 = r24;
        r14 = r25;
        r0 = r26;
        r5 = r27;
        r7 = r28;
        r10 = r31;
        r13 = r36;
        r12 = r37;
        r11 = r38;
        r9 = r40;
        r1 = r41;
        r8 = 2;
        if (r1 != 0) goto L_0x0056;
    L_0x0019:
        r2 = org.telegram.messenger.UserConfig.getActivatedAccountsCount();
        if (r2 < r8) goto L_0x0056;
    L_0x001f:
        if (r13 == 0) goto L_0x0056;
    L_0x0021:
        r8 = new org.telegram.ui.-$$Lambda$LaunchActivity$oQA0vgj3X1YdD8d67UmGyFJyjHY;
        r1 = r8;
        r2 = r24;
        r3 = r25;
        r4 = r26;
        r5 = r27;
        r6 = r28;
        r7 = r29;
        r0 = r8;
        r8 = r30;
        r14 = r9;
        r9 = r31;
        r10 = r32;
        r11 = r33;
        r12 = r34;
        r15 = r13;
        r13 = r35;
        r14 = r36;
        r15 = r37;
        r16 = r38;
        r17 = r39;
        r18 = r40;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        r13 = r24;
        r0 = org.telegram.p004ui.Components.AlertsCreator.createAccountSelectDialog(r13, r0);
        r0.show();
        return;
    L_0x0056:
        r23 = r15;
        r15 = r13;
        r13 = r23;
        r2 = 0;
        r3 = 1;
        r12 = 0;
        if (r39 == 0) goto L_0x00af;
    L_0x0060:
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r1 = org.telegram.messenger.NotificationCenter.didReceiveSmsCode;
        r0 = r0.hasObservers(r1);
        if (r0 == 0) goto L_0x007a;
    L_0x006c:
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r1 = org.telegram.messenger.NotificationCenter.didReceiveSmsCode;
        r2 = new java.lang.Object[r3];
        r2[r12] = r39;
        r0.postNotificationName(r1, r2);
        goto L_0x00ae;
    L_0x007a:
        r0 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r0.<init>(r13);
        r1 = 2131558635; // 0x7f0d00eb float:1.8742591E38 double:1.0531298936E-314;
        r4 = "AppName";
        r1 = org.telegram.messenger.LocaleController.getString(r4, r1);
        r0.setTitle(r1);
        r1 = 2131560130; // 0x7f0d06c2 float:1.8745624E38 double:1.0531306323E-314;
        r3 = new java.lang.Object[r3];
        r3[r12] = r39;
        r4 = "OtherLoginCode";
        r1 = org.telegram.messenger.LocaleController.formatString(r4, r1, r3);
        r1 = org.telegram.messenger.AndroidUtilities.replaceTags(r1);
        r0.setMessage(r1);
        r1 = 2131560097; // 0x7f0d06a1 float:1.8745557E38 double:1.053130616E-314;
        r3 = "OK";
        r1 = org.telegram.messenger.LocaleController.getString(r3, r1);
        r0.setPositiveButton(r1, r2);
        r13.showAlertDialog(r0);
    L_0x00ae:
        return;
    L_0x00af:
        r11 = new org.telegram.ui.ActionBar.AlertDialog;
        r4 = 3;
        r11.<init>(r13, r4);
        r9 = new int[r3];
        r9[r12] = r12;
        if (r0 == 0) goto L_0x00e6;
    L_0x00bb:
        r10 = new org.telegram.tgnet.TLRPC$TL_contacts_resolveUsername;
        r10.<init>();
        r10.username = r0;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r25);
        r15 = new org.telegram.ui.-$$Lambda$LaunchActivity$RpMN6VrBiIorLulFIXuvXk8Fkc4;
        r1 = r15;
        r2 = r24;
        r3 = r11;
        r4 = r35;
        r5 = r25;
        r6 = r30;
        r7 = r29;
        r8 = r33;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);
        r0 = r0.sendRequest(r10, r15);
        r9[r12] = r0;
        r4 = r9;
        r9 = r11;
        r5 = r13;
        r8 = r14;
        r6 = 0;
        goto L_0x030f;
    L_0x00e6:
        if (r5 == 0) goto L_0x0166;
    L_0x00e8:
        if (r1 != 0) goto L_0x013c;
    L_0x00ea:
        r6 = new org.telegram.tgnet.TLRPC$TL_messages_checkChatInvite;
        r6.<init>();
        r6.hash = r5;
        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r25);
        r3 = new org.telegram.ui.-$$Lambda$LaunchActivity$Y1VGiKNTbvASO61ltlphoI7h8RU;
        r1 = r3;
        r2 = r24;
        r0 = r3;
        r3 = r11;
        r41 = r0;
        r0 = r4;
        r4 = r25;
        r5 = r27;
        r20 = r0;
        r0 = r6;
        r6 = r26;
        r7 = r28;
        r27 = r0;
        r0 = 2;
        r8 = r29;
        r21 = r9;
        r9 = r30;
        r10 = r31;
        r22 = r11;
        r11 = r32;
        r12 = r33;
        r13 = r34;
        r14 = r35;
        r15 = r36;
        r16 = r37;
        r17 = r38;
        r18 = r39;
        r19 = r40;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19);
        r1 = r27;
        r3 = r41;
        r2 = r20;
        r0 = r2.sendRequest(r1, r3, r0);
        r4 = r21;
        r6 = 0;
        r4[r6] = r0;
        goto L_0x015e;
    L_0x013c:
        r4 = r9;
        r22 = r11;
        r0 = 2;
        r6 = 0;
        if (r1 != r3) goto L_0x015e;
    L_0x0143:
        r1 = new org.telegram.tgnet.TLRPC$TL_messages_importChatInvite;
        r1.<init>();
        r1.hash = r5;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r25);
        r3 = new org.telegram.ui.-$$Lambda$LaunchActivity$Zfo5ch07dnPuGi7d59vdlVijeac;
        r5 = r24;
        r8 = r25;
        r9 = r22;
        r3.<init>(r5, r8, r9);
        r2.sendRequest(r1, r3, r0);
        goto L_0x030f;
    L_0x015e:
        r5 = r24;
        r8 = r25;
        r9 = r22;
        goto L_0x030f;
    L_0x0166:
        r4 = r9;
        r9 = r11;
        r5 = r13;
        r8 = r14;
        r6 = 0;
        if (r7 == 0) goto L_0x01a0;
    L_0x016d:
        r0 = mainFragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x019f;
    L_0x0175:
        r0 = new org.telegram.tgnet.TLRPC$TL_inputStickerSetShortName;
        r0.<init>();
        r0.short_name = r7;
        r1 = mainFragmentsStack;
        r2 = r1.size();
        r2 = r2 - r3;
        r1 = r1.get(r2);
        r1 = (org.telegram.p004ui.ActionBar.BaseFragment) r1;
        r2 = new org.telegram.ui.Components.StickersAlert;
        r3 = 0;
        r4 = 0;
        r25 = r2;
        r26 = r24;
        r27 = r1;
        r28 = r0;
        r29 = r3;
        r30 = r4;
        r25.<init>(r26, r27, r28, r29, r30);
        r1.showDialog(r2);
    L_0x019f:
        return;
    L_0x01a0:
        if (r10 == 0) goto L_0x01c0;
    L_0x01a2:
        r0 = new android.os.Bundle;
        r0.<init>();
        r1 = "onlySelect";
        r0.putBoolean(r1, r3);
        r1 = new org.telegram.ui.DialogsActivity;
        r1.<init>(r0);
        r0 = new org.telegram.ui.-$$Lambda$LaunchActivity$RTwp0QB0Dkt4irgon9YXmRC5sCk;
        r2 = r32;
        r0.<init>(r5, r2, r8, r10);
        r1.setDelegate(r0);
        r5.presentFragment(r1, r6, r3);
        goto L_0x030f;
    L_0x01c0:
        if (r15 == 0) goto L_0x022b;
    L_0x01c2:
        r0 = "bot_id";
        r0 = r15.get(r0);
        r0 = (java.lang.String) r0;
        r0 = org.telegram.messenger.Utilities.parseInt(r0);
        r0 = r0.intValue();
        if (r0 != 0) goto L_0x01d5;
    L_0x01d4:
        return;
    L_0x01d5:
        r1 = "payload";
        r1 = r15.get(r1);
        r1 = (java.lang.String) r1;
        r2 = "nonce";
        r2 = r15.get(r2);
        r2 = (java.lang.String) r2;
        r3 = "callback_url";
        r3 = r15.get(r3);
        r3 = (java.lang.String) r3;
        r7 = new org.telegram.tgnet.TLRPC$TL_account_getAuthorizationForm;
        r7.<init>();
        r7.bot_id = r0;
        r0 = "scope";
        r0 = r15.get(r0);
        r0 = (java.lang.String) r0;
        r7.scope = r0;
        r0 = "public_key";
        r0 = r15.get(r0);
        r0 = (java.lang.String) r0;
        r7.public_key = r0;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r25);
        r10 = new org.telegram.ui.-$$Lambda$LaunchActivity$Art3qkoRK5cBs4Y4xN6A8ukcezg;
        r26 = r10;
        r27 = r24;
        r28 = r4;
        r29 = r25;
        r30 = r9;
        r31 = r7;
        r32 = r1;
        r33 = r2;
        r34 = r3;
        r26.<init>(r27, r28, r29, r30, r31, r32, r33, r34);
        r0 = r0.sendRequest(r7, r10);
        r4[r6] = r0;
        goto L_0x030f;
    L_0x022b:
        r0 = r38;
        if (r0 == 0) goto L_0x0249;
    L_0x022f:
        r1 = new org.telegram.tgnet.TLRPC$TL_help_getDeepLinkInfo;
        r1.<init>();
        r1.path = r0;
        r0 = r5.currentAccount;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
        r2 = new org.telegram.ui.-$$Lambda$LaunchActivity$qH1w5GBYl7awDhwaFS3FGWwAWCs;
        r2.<init>(r5, r9);
        r0 = r0.sendRequest(r1, r2);
        r4[r6] = r0;
        goto L_0x030f;
    L_0x0249:
        r0 = r37;
        if (r0 == 0) goto L_0x026b;
    L_0x024d:
        r1 = new org.telegram.tgnet.TLRPC$TL_langpack_getLanguage;
        r1.<init>();
        r1.lang_code = r0;
        r0 = "android";
        r1.lang_pack = r0;
        r0 = r5.currentAccount;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
        r2 = new org.telegram.ui.-$$Lambda$LaunchActivity$BzXeKQ-qpCSdfuqJJ22744bcPM0;
        r2.<init>(r5, r9);
        r0 = r0.sendRequest(r1, r2);
        r4[r6] = r0;
        goto L_0x030f;
    L_0x026b:
        r1 = r40;
        if (r1 == 0) goto L_0x02b9;
    L_0x026f:
        r0 = r1.slug;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x0294;
    L_0x0277:
        r0 = new org.telegram.ui.WallpapersListActivity$ColorWallpaper;	 Catch:{ Exception -> 0x0290 }
        r10 = -100;
        r7 = r1.settings;	 Catch:{ Exception -> 0x0290 }
        r7 = r7.background_color;	 Catch:{ Exception -> 0x0290 }
        r0.<init>(r10, r7);	 Catch:{ Exception -> 0x0290 }
        r7 = new org.telegram.ui.WallpaperActivity;	 Catch:{ Exception -> 0x0290 }
        r7.<init>(r0, r2);	 Catch:{ Exception -> 0x0290 }
        r0 = new org.telegram.ui.-$$Lambda$LaunchActivity$zROn3pMkFlHNUe2IsaYIfrrTR3o;	 Catch:{ Exception -> 0x0290 }
        r0.<init>(r5, r7);	 Catch:{ Exception -> 0x0290 }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);	 Catch:{ Exception -> 0x0290 }
        goto L_0x0295;
    L_0x0290:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0294:
        r3 = 0;
    L_0x0295:
        if (r3 != 0) goto L_0x030f;
    L_0x0297:
        r0 = new org.telegram.tgnet.TLRPC$TL_account_getWallPaper;
        r0.<init>();
        r2 = new org.telegram.tgnet.TLRPC$TL_inputWallPaperSlug;
        r2.<init>();
        r3 = r1.slug;
        r2.slug = r3;
        r0.wallpaper = r2;
        r2 = r5.currentAccount;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);
        r3 = new org.telegram.ui.-$$Lambda$LaunchActivity$KNg5IaCFYbOpTWjqZaxL1vwTVgk;
        r3.<init>(r5, r9, r1);
        r0 = r2.sendRequest(r0, r3);
        r4[r6] = r0;
        goto L_0x030f;
    L_0x02b9:
        if (r34 == 0) goto L_0x030f;
    L_0x02bb:
        if (r33 == 0) goto L_0x030f;
    L_0x02bd:
        r0 = new android.os.Bundle;
        r0.<init>();
        r1 = r34.intValue();
        r7 = "chat_id";
        r0.putInt(r7, r1);
        r1 = r33.intValue();
        r7 = "message_id";
        r0.putInt(r7, r1);
        r1 = mainFragmentsStack;
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x02ea;
    L_0x02dc:
        r1 = mainFragmentsStack;
        r2 = r1.size();
        r2 = r2 - r3;
        r1 = r1.get(r2);
        r1 = (org.telegram.p004ui.ActionBar.BaseFragment) r1;
        goto L_0x02eb;
    L_0x02ea:
        r1 = r2;
    L_0x02eb:
        if (r1 == 0) goto L_0x02f7;
    L_0x02ed:
        r2 = org.telegram.messenger.MessagesController.getInstance(r25);
        r2 = r2.checkCanOpenChat(r0, r1);
        if (r2 == 0) goto L_0x030f;
    L_0x02f7:
        r2 = new org.telegram.ui.-$$Lambda$LaunchActivity$PRISru-yyJ8MquHeYUjmNXD8lxM;
        r26 = r2;
        r27 = r24;
        r28 = r0;
        r29 = r34;
        r30 = r4;
        r31 = r9;
        r32 = r1;
        r33 = r25;
        r26.<init>(r27, r28, r29, r30, r31, r32, r33);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
    L_0x030f:
        r0 = r4[r6];
        if (r0 == 0) goto L_0x031e;
    L_0x0313:
        r0 = new org.telegram.ui.-$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM;
        r0.<init>(r8, r4);
        r9.setOnCancelListener(r0);
        r9.show();	 Catch:{ Exception -> 0x031e }
    L_0x031e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.runLinkRequest(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.Integer, java.lang.Integer, java.lang.String, java.util.HashMap, java.lang.String, java.lang.String, java.lang.String, org.telegram.tgnet.TLRPC$TL_wallPaper, int):void");
    }

    public /* synthetic */ void lambda$runLinkRequest$8$LaunchActivity(int i, String str, String str2, String str3, String str4, String str5, String str6, boolean z, Integer num, Integer num2, String str7, HashMap hashMap, String str8, String str9, String str10, TL_wallPaper tL_wallPaper, int i2) {
        int i3 = i2;
        if (i3 != i) {
            switchToAccount(i3, true);
        }
        runLinkRequest(i2, str, str2, str3, str4, str5, str6, z, num, num2, str7, hashMap, str8, str9, str10, tL_wallPaper, 1);
    }

    public /* synthetic */ void lambda$runLinkRequest$12$LaunchActivity(AlertDialog alertDialog, String str, int i, String str2, String str3, Integer num, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1585-$$Lambda$LaunchActivity$iffzHHAMAuBNKcUpOYSEoUIrEeY(this, alertDialog, tLObject, tL_error, str, i, str2, str3, num));
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0139  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00f9  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0139  */
    /* JADX WARNING: Missing block: B:21:0x00a9, code skipped:
            if ((r0.get(r0.size() - 1) instanceof org.telegram.p004ui.DialogsActivity) != false) goto L_0x00ab;
     */
    /* JADX WARNING: Missing block: B:27:0x00c8, code skipped:
            if ((r0.get(r0.size() - 1) instanceof org.telegram.p004ui.DialogsActivity) != false) goto L_0x00ab;
     */
    public /* synthetic */ void lambda$null$11$LaunchActivity(org.telegram.p004ui.ActionBar.AlertDialog r12, org.telegram.tgnet.TLObject r13, org.telegram.tgnet.TLRPC.TL_error r14, java.lang.String r15, int r16, java.lang.String r17, java.lang.String r18, java.lang.Integer r19) {
        /*
        r11 = this;
        r1 = r11;
        r2 = r15;
        r3 = r16;
        r4 = r17;
        r5 = r18;
        r0 = r11.isFinishing();
        if (r0 != 0) goto L_0x026c;
    L_0x000e:
        r12.dismiss();	 Catch:{ Exception -> 0x0012 }
        goto L_0x0017;
    L_0x0012:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.m30e(r6);
    L_0x0017:
        r0 = r13;
        r0 = (org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer) r0;
        r6 = 0;
        if (r14 != 0) goto L_0x0257;
    L_0x001d:
        r7 = r1.actionBarLayout;
        if (r7 == 0) goto L_0x0257;
    L_0x0021:
        if (r2 == 0) goto L_0x002d;
    L_0x0023:
        if (r2 == 0) goto L_0x0257;
    L_0x0025:
        r7 = r0.users;
        r7 = r7.isEmpty();
        if (r7 != 0) goto L_0x0257;
    L_0x002d:
        r7 = org.telegram.messenger.MessagesController.getInstance(r16);
        r8 = r0.users;
        r7.putUsers(r8, r6);
        r7 = org.telegram.messenger.MessagesController.getInstance(r16);
        r8 = r0.chats;
        r7.putChats(r8, r6);
        r7 = org.telegram.messenger.MessagesStorage.getInstance(r16);
        r8 = r0.users;
        r9 = r0.chats;
        r10 = 1;
        r7.putUsersAndChats(r8, r9, r6, r10);
        r7 = "dialogsType";
        r8 = "onlySelect";
        if (r2 == 0) goto L_0x0140;
    L_0x0051:
        r4 = new android.os.Bundle;
        r4.<init>();
        r4.putBoolean(r8, r10);
        r5 = "cantSendToChannels";
        r4.putBoolean(r5, r10);
        r4.putInt(r7, r10);
        r5 = 2131560691; // 0x7f0d08f3 float:1.8746761E38 double:1.0531309094E-314;
        r7 = "SendGameTo";
        r5 = org.telegram.messenger.LocaleController.getString(r7, r5);
        r7 = "selectAlertString";
        r4.putString(r7, r5);
        r5 = 2131560692; // 0x7f0d08f4 float:1.8746763E38 double:1.05313091E-314;
        r7 = "SendGameToGroup";
        r5 = org.telegram.messenger.LocaleController.getString(r7, r5);
        r7 = "selectAlertStringGroup";
        r4.putString(r7, r5);
        r5 = new org.telegram.ui.DialogsActivity;
        r5.<init>(r4);
        r4 = new org.telegram.ui.-$$Lambda$LaunchActivity$u-E5fg1gn1kuY6fPnql7A5bYvUE;
        r4.<init>(r11, r15, r3, r0);
        r5.setDelegate(r4);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x00af;
    L_0x0090:
        r0 = r1.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 <= 0) goto L_0x00ad;
    L_0x009a:
        r0 = r1.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r2 = r0.size();
        r2 = r2 - r10;
        r0 = r0.get(r2);
        r0 = r0 instanceof org.telegram.p004ui.DialogsActivity;
        if (r0 == 0) goto L_0x00ad;
    L_0x00ab:
        r0 = 1;
        goto L_0x00cb;
    L_0x00ad:
        r0 = 0;
        goto L_0x00cb;
    L_0x00af:
        r0 = r1.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 <= r10) goto L_0x00ad;
    L_0x00b9:
        r0 = r1.actionBarLayout;
        r0 = r0.fragmentsStack;
        r2 = r0.size();
        r2 = r2 - r10;
        r0 = r0.get(r2);
        r0 = r0 instanceof org.telegram.p004ui.DialogsActivity;
        if (r0 == 0) goto L_0x00ad;
    L_0x00ca:
        goto L_0x00ab;
    L_0x00cb:
        r2 = r1.actionBarLayout;
        r3 = 1;
        r4 = 1;
        r7 = 0;
        r12 = r2;
        r13 = r5;
        r14 = r0;
        r15 = r3;
        r16 = r4;
        r17 = r7;
        r12.presentFragment(r13, r14, r15, r16, r17);
        r0 = org.telegram.p004ui.SecretMediaViewer.hasInstance();
        if (r0 == 0) goto L_0x00f3;
    L_0x00e1:
        r0 = org.telegram.p004ui.SecretMediaViewer.getInstance();
        r0 = r0.isVisible();
        if (r0 == 0) goto L_0x00f3;
    L_0x00eb:
        r0 = org.telegram.p004ui.SecretMediaViewer.getInstance();
        r0.closePhoto(r6, r6);
        goto L_0x0122;
    L_0x00f3:
        r0 = org.telegram.p004ui.PhotoViewer.hasInstance();
        if (r0 == 0) goto L_0x010b;
    L_0x00f9:
        r0 = org.telegram.p004ui.PhotoViewer.getInstance();
        r0 = r0.isVisible();
        if (r0 == 0) goto L_0x010b;
    L_0x0103:
        r0 = org.telegram.p004ui.PhotoViewer.getInstance();
        r0.closePhoto(r6, r10);
        goto L_0x0122;
    L_0x010b:
        r0 = org.telegram.p004ui.ArticleViewer.hasInstance();
        if (r0 == 0) goto L_0x0122;
    L_0x0111:
        r0 = org.telegram.p004ui.ArticleViewer.getInstance();
        r0 = r0.isVisible();
        if (r0 == 0) goto L_0x0122;
    L_0x011b:
        r0 = org.telegram.p004ui.ArticleViewer.getInstance();
        r0.close(r6, r10);
    L_0x0122:
        r0 = r1.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r6, r6);
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x0139;
    L_0x012d:
        r0 = r1.actionBarLayout;
        r0.showLastFragment();
        r0 = r1.rightActionBarLayout;
        r0.showLastFragment();
        goto L_0x026c;
    L_0x0139:
        r0 = r1.drawerLayoutContainer;
        r0.setAllowOpenDrawer(r10, r6);
        goto L_0x026c;
    L_0x0140:
        r2 = 0;
        if (r4 == 0) goto L_0x01ad;
    L_0x0143:
        r5 = r0.users;
        r5 = r5.isEmpty();
        if (r5 != 0) goto L_0x0154;
    L_0x014b:
        r0 = r0.users;
        r0 = r0.get(r6);
        r2 = r0;
        r2 = (org.telegram.tgnet.TLRPC.User) r2;
    L_0x0154:
        if (r2 == 0) goto L_0x0197;
    L_0x0156:
        r0 = r2.bot;
        if (r0 == 0) goto L_0x015f;
    L_0x015a:
        r0 = r2.bot_nochats;
        if (r0 == 0) goto L_0x015f;
    L_0x015e:
        goto L_0x0197;
    L_0x015f:
        r0 = new android.os.Bundle;
        r0.<init>();
        r0.putBoolean(r8, r10);
        r5 = 2;
        r0.putInt(r7, r5);
        r7 = 2131558596; // 0x7f0d00c4 float:1.8742512E38 double:1.0531298744E-314;
        r5 = new java.lang.Object[r5];
        r8 = org.telegram.messenger.UserObject.getUserName(r2);
        r5[r6] = r8;
        r6 = "%1$s";
        r5[r10] = r6;
        r6 = "AddToTheGroupTitle";
        r5 = org.telegram.messenger.LocaleController.formatString(r6, r7, r5);
        r6 = "addToGroupAlertString";
        r0.putString(r6, r5);
        r5 = new org.telegram.ui.DialogsActivity;
        r5.<init>(r0);
        r0 = new org.telegram.ui.-$$Lambda$LaunchActivity$4SPCx7X84qe3LzSdcC5uytKxGYQ;
        r0.<init>(r11, r3, r2, r4);
        r5.setDelegate(r0);
        r11.lambda$runLinkRequest$27$LaunchActivity(r5);
        goto L_0x026c;
    L_0x0197:
        r0 = "BotCantJoinGroups";
        r2 = 2131558849; // 0x7f0d01c1 float:1.8743025E38 double:1.0531299994E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r2);	 Catch:{ Exception -> 0x01a8 }
        r0 = android.widget.Toast.makeText(r11, r0, r6);	 Catch:{ Exception -> 0x01a8 }
        r0.show();	 Catch:{ Exception -> 0x01a8 }
        goto L_0x01ac;
    L_0x01a8:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x01ac:
        return;
    L_0x01ad:
        r4 = new android.os.Bundle;
        r4.<init>();
        r7 = r0.chats;
        r7 = r7.isEmpty();
        if (r7 != 0) goto L_0x01d5;
    L_0x01ba:
        r7 = r0.chats;
        r7 = r7.get(r6);
        r7 = (org.telegram.tgnet.TLRPC.Chat) r7;
        r7 = r7.f434id;
        r8 = "chat_id";
        r4.putInt(r8, r7);
        r7 = r0.chats;
        r7 = r7.get(r6);
        r7 = (org.telegram.tgnet.TLRPC.Chat) r7;
        r7 = r7.f434id;
        r7 = -r7;
        goto L_0x01ee;
    L_0x01d5:
        r7 = r0.users;
        r7 = r7.get(r6);
        r7 = (org.telegram.tgnet.TLRPC.User) r7;
        r7 = r7.f534id;
        r8 = "user_id";
        r4.putInt(r8, r7);
        r7 = r0.users;
        r7 = r7.get(r6);
        r7 = (org.telegram.tgnet.TLRPC.User) r7;
        r7 = r7.f534id;
    L_0x01ee:
        r7 = (long) r7;
        if (r5 == 0) goto L_0x020b;
    L_0x01f1:
        r9 = r0.users;
        r9 = r9.size();
        if (r9 <= 0) goto L_0x020b;
    L_0x01f9:
        r0 = r0.users;
        r0 = r0.get(r6);
        r0 = (org.telegram.tgnet.TLRPC.User) r0;
        r0 = r0.bot;
        if (r0 == 0) goto L_0x020b;
    L_0x0205:
        r0 = "botUser";
        r4.putString(r0, r5);
        r6 = 1;
    L_0x020b:
        if (r19 == 0) goto L_0x0216;
    L_0x020d:
        r0 = r19.intValue();
        r9 = "message_id";
        r4.putInt(r9, r0);
    L_0x0216:
        r0 = mainFragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x022c;
    L_0x021e:
        r0 = mainFragmentsStack;
        r2 = r0.size();
        r2 = r2 - r10;
        r0 = r0.get(r2);
        r2 = r0;
        r2 = (org.telegram.p004ui.ActionBar.BaseFragment) r2;
    L_0x022c:
        if (r2 == 0) goto L_0x0238;
    L_0x022e:
        r0 = org.telegram.messenger.MessagesController.getInstance(r16);
        r0 = r0.checkCanOpenChat(r4, r2);
        if (r0 == 0) goto L_0x026c;
    L_0x0238:
        if (r6 == 0) goto L_0x024c;
    L_0x023a:
        r0 = r2 instanceof org.telegram.p004ui.ChatActivity;
        if (r0 == 0) goto L_0x024c;
    L_0x023e:
        r2 = (org.telegram.p004ui.ChatActivity) r2;
        r9 = r2.getDialogId();
        r0 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r0 != 0) goto L_0x024c;
    L_0x0248:
        r2.setBotUser(r5);
        goto L_0x026c;
    L_0x024c:
        r0 = new org.telegram.ui.ChatActivity;
        r0.<init>(r4);
        r2 = r1.actionBarLayout;
        r2.presentFragment(r0);
        goto L_0x026c;
    L_0x0257:
        r0 = "NoUsernameFound";
        r2 = 2131559955; // 0x7f0d0613 float:1.8745269E38 double:1.053130546E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r2);	 Catch:{ Exception -> 0x0268 }
        r0 = android.widget.Toast.makeText(r11, r0, r6);	 Catch:{ Exception -> 0x0268 }
        r0.show();	 Catch:{ Exception -> 0x0268 }
        goto L_0x026c;
    L_0x0268:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x026c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.lambda$null$11$LaunchActivity(org.telegram.ui.ActionBar.AlertDialog, org.telegram.tgnet.TLObject, org.telegram.tgnet.TLRPC$TL_error, java.lang.String, int, java.lang.String, java.lang.String, java.lang.Integer):void");
    }

    public /* synthetic */ void lambda$null$9$LaunchActivity(String str, int i, TL_contacts_resolvedPeer tL_contacts_resolvedPeer, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        TL_inputMediaGame tL_inputMediaGame = new TL_inputMediaGame();
        tL_inputMediaGame.f546id = new TL_inputGameShortName();
        InputGame inputGame = tL_inputMediaGame.f546id;
        inputGame.short_name = str;
        inputGame.bot_id = MessagesController.getInstance(i).getInputUser((User) tL_contacts_resolvedPeer.users.get(0));
        int i2 = (int) longValue;
        SendMessagesHelper.getInstance(i).sendGame(MessagesController.getInstance(i).getInputPeer(i2), tL_inputMediaGame, 0, 0);
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        int i3 = (int) (longValue >> 32);
        if (i2 != 0) {
            String str2 = "chat_id";
            if (i3 == 1) {
                bundle.putInt(str2, i2);
            } else if (i2 > 0) {
                bundle.putInt("user_id", i2);
            } else if (i2 < 0) {
                bundle.putInt(str2, -i2);
            }
        } else {
            bundle.putInt("enc_id", i3);
        }
        DialogsActivity dialogsActivity2 = dialogsActivity;
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
            return;
        }
    }

    public /* synthetic */ void lambda$null$10$LaunchActivity(int i, User user, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        long longValue = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        int i2 = -((int) longValue);
        bundle.putInt("chat_id", i2);
        if (!mainFragmentsStack.isEmpty()) {
            MessagesController instance = MessagesController.getInstance(i);
            ArrayList arrayList2 = mainFragmentsStack;
            if (!instance.checkCanOpenChat(bundle, (BaseFragment) arrayList2.get(arrayList2.size() - 1))) {
                return;
            }
        }
        NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        MessagesController.getInstance(i).addUserToChat(i2, user, null, 0, str, null, null);
        this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
    }

    public /* synthetic */ void lambda$runLinkRequest$15$LaunchActivity(AlertDialog alertDialog, int i, String str, String str2, String str3, String str4, String str5, String str6, boolean z, Integer num, Integer num2, String str7, HashMap hashMap, String str8, String str9, String str10, TL_wallPaper tL_wallPaper, TLObject tLObject, TL_error tL_error) {
        TL_error tL_error2 = tL_error;
        AndroidUtilities.runOnUIThread(new C1580-$$Lambda$LaunchActivity$XxlJBaFgsBRiIBlnYo-htRWRAf0(this, alertDialog, tL_error2, tLObject, i, str, str2, str3, str4, str5, str6, z, num, num2, str7, hashMap, str8, str9, str10, tL_wallPaper));
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0104  */
    /* JADX WARNING: Missing block: B:17:0x0040, code skipped:
            if (android.text.TextUtils.isEmpty(r5.username) == false) goto L_0x0042;
     */
    /* JADX WARNING: Missing block: B:21:0x0087, code skipped:
            if (r1.checkCanOpenChat(r0, (org.telegram.p004ui.ActionBar.BaseFragment) r2.get(r2.size() - 1)) != false) goto L_0x0089;
     */
    /* JADX WARNING: Missing block: B:34:0x00ca, code skipped:
            if (r1.chat.megagroup != false) goto L_0x00cc;
     */
    public /* synthetic */ void lambda$null$14$LaunchActivity(org.telegram.p004ui.ActionBar.AlertDialog r20, org.telegram.tgnet.TLRPC.TL_error r21, org.telegram.tgnet.TLObject r22, int r23, java.lang.String r24, java.lang.String r25, java.lang.String r26, java.lang.String r27, java.lang.String r28, java.lang.String r29, boolean r30, java.lang.Integer r31, java.lang.Integer r32, java.lang.String r33, java.util.HashMap r34, java.lang.String r35, java.lang.String r36, java.lang.String r37, org.telegram.tgnet.TLRPC.TL_wallPaper r38) {
        /*
        r19 = this;
        r15 = r19;
        r1 = r21;
        r0 = r19.isFinishing();
        if (r0 != 0) goto L_0x019c;
    L_0x000a:
        r20.dismiss();	 Catch:{ Exception -> 0x000e }
        goto L_0x0013;
    L_0x000e:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.m30e(r2);
    L_0x0013:
        r0 = 2131560097; // 0x7f0d06a1 float:1.8745557E38 double:1.053130616E-314;
        r2 = "OK";
        r3 = 2131558635; // 0x7f0d00eb float:1.8742591E38 double:1.0531298936E-314;
        r4 = "AppName";
        r14 = 0;
        if (r1 != 0) goto L_0x0160;
    L_0x0020:
        r5 = r15.actionBarLayout;
        if (r5 == 0) goto L_0x0160;
    L_0x0024:
        r1 = r22;
        r1 = (org.telegram.tgnet.TLRPC.ChatInvite) r1;
        r5 = r1.chat;
        r6 = 1;
        r7 = 0;
        if (r5 == 0) goto L_0x00b0;
    L_0x002e:
        r5 = org.telegram.messenger.ChatObject.isLeftFromChat(r5);
        if (r5 == 0) goto L_0x0042;
    L_0x0034:
        r5 = r1.chat;
        r8 = r5.kicked;
        if (r8 != 0) goto L_0x00b0;
    L_0x003a:
        r5 = r5.username;
        r5 = android.text.TextUtils.isEmpty(r5);
        if (r5 != 0) goto L_0x00b0;
    L_0x0042:
        r0 = org.telegram.messenger.MessagesController.getInstance(r23);
        r2 = r1.chat;
        r0.putChat(r2, r7);
        r0 = new java.util.ArrayList;
        r0.<init>();
        r2 = r1.chat;
        r0.add(r2);
        r2 = org.telegram.messenger.MessagesStorage.getInstance(r23);
        r2.putUsersAndChats(r14, r0, r7, r6);
        r0 = new android.os.Bundle;
        r0.<init>();
        r1 = r1.chat;
        r1 = r1.f434id;
        r2 = "chat_id";
        r0.putInt(r2, r1);
        r1 = mainFragmentsStack;
        r1 = r1.isEmpty();
        if (r1 != 0) goto L_0x0089;
    L_0x0072:
        r1 = org.telegram.messenger.MessagesController.getInstance(r23);
        r2 = mainFragmentsStack;
        r3 = r2.size();
        r3 = r3 - r6;
        r2 = r2.get(r3);
        r2 = (org.telegram.p004ui.ActionBar.BaseFragment) r2;
        r1 = r1.checkCanOpenChat(r0, r2);
        if (r1 == 0) goto L_0x019c;
    L_0x0089:
        r1 = new org.telegram.ui.ChatActivity;
        r1.<init>(r0);
        r0 = org.telegram.messenger.NotificationCenter.getInstance(r23);
        r2 = org.telegram.messenger.NotificationCenter.closeChats;
        r3 = new java.lang.Object[r7];
        r0.postNotificationName(r2, r3);
        r0 = r15.actionBarLayout;
        r2 = 0;
        r3 = 1;
        r4 = 1;
        r5 = 0;
        r20 = r0;
        r21 = r1;
        r22 = r2;
        r23 = r3;
        r24 = r4;
        r25 = r5;
        r20.presentFragment(r21, r22, r23, r24, r25);
        goto L_0x019c;
    L_0x00b0:
        r5 = r1.chat;
        if (r5 != 0) goto L_0x00bc;
    L_0x00b4:
        r5 = r1.channel;
        if (r5 == 0) goto L_0x00cc;
    L_0x00b8:
        r5 = r1.megagroup;
        if (r5 != 0) goto L_0x00cc;
    L_0x00bc:
        r5 = r1.chat;
        if (r5 == 0) goto L_0x00ed;
    L_0x00c0:
        r5 = org.telegram.messenger.ChatObject.isChannel(r5);
        if (r5 == 0) goto L_0x00cc;
    L_0x00c6:
        r5 = r1.chat;
        r5 = r5.megagroup;
        if (r5 == 0) goto L_0x00ed;
    L_0x00cc:
        r5 = mainFragmentsStack;
        r5 = r5.isEmpty();
        if (r5 != 0) goto L_0x00ed;
    L_0x00d4:
        r0 = mainFragmentsStack;
        r2 = r0.size();
        r2 = r2 - r6;
        r0 = r0.get(r2);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r2 = new org.telegram.ui.Components.JoinGroupAlert;
        r5 = r24;
        r2.<init>(r15, r1, r5, r0);
        r0.showDialog(r2);
        goto L_0x019c;
    L_0x00ed:
        r5 = r24;
        r13 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r13.<init>(r15);
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r13.setTitle(r3);
        r3 = 2131558955; // 0x7f0d022b float:1.874324E38 double:1.0531300518E-314;
        r4 = new java.lang.Object[r6];
        r6 = r1.chat;
        if (r6 == 0) goto L_0x0107;
    L_0x0104:
        r1 = r6.title;
        goto L_0x0109;
    L_0x0107:
        r1 = r1.title;
    L_0x0109:
        r4[r7] = r1;
        r1 = "ChannelJoinTo";
        r1 = org.telegram.messenger.LocaleController.formatString(r1, r3, r4);
        r13.setMessage(r1);
        r0 = org.telegram.messenger.LocaleController.getString(r2, r0);
        r12 = new org.telegram.ui.-$$Lambda$LaunchActivity$0lbK3R9cg-xyRcV1YTV_bo7OG68;
        r1 = r12;
        r2 = r19;
        r3 = r23;
        r4 = r25;
        r5 = r24;
        r6 = r26;
        r7 = r27;
        r8 = r28;
        r9 = r29;
        r10 = r30;
        r11 = r31;
        r20 = r0;
        r0 = r12;
        r12 = r32;
        r21 = r0;
        r0 = r13;
        r13 = r33;
        r14 = r34;
        r15 = r35;
        r16 = r36;
        r17 = r37;
        r18 = r38;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18);
        r1 = r20;
        r2 = r21;
        r0.setPositiveButton(r1, r2);
        r1 = 2131558891; // 0x7f0d01eb float:1.874311E38 double:1.05313002E-314;
        r2 = "Cancel";
        r1 = org.telegram.messenger.LocaleController.getString(r2, r1);
        r5 = 0;
        r0.setNegativeButton(r1, r5);
        r6 = r19;
        r6.showAlertDialog(r0);
        goto L_0x019d;
    L_0x0160:
        r5 = r14;
        r6 = r15;
        r7 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r7.<init>(r6);
        r3 = org.telegram.messenger.LocaleController.getString(r4, r3);
        r7.setTitle(r3);
        r1 = r1.text;
        r3 = "FLOOD_WAIT";
        r1 = r1.startsWith(r3);
        if (r1 == 0) goto L_0x0185;
    L_0x0178:
        r1 = 2131559495; // 0x7f0d0447 float:1.8744336E38 double:1.0531303185E-314;
        r3 = "FloodWait";
        r1 = org.telegram.messenger.LocaleController.getString(r3, r1);
        r7.setMessage(r1);
        goto L_0x0191;
    L_0x0185:
        r1 = 2131559705; // 0x7f0d0519 float:1.8744762E38 double:1.0531304223E-314;
        r3 = "JoinToGroupErrorNotExist";
        r1 = org.telegram.messenger.LocaleController.getString(r3, r1);
        r7.setMessage(r1);
    L_0x0191:
        r0 = org.telegram.messenger.LocaleController.getString(r2, r0);
        r7.setPositiveButton(r0, r5);
        r6.showAlertDialog(r7);
        goto L_0x019d;
    L_0x019c:
        r6 = r15;
    L_0x019d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.lambda$null$14$LaunchActivity(org.telegram.ui.ActionBar.AlertDialog, org.telegram.tgnet.TLRPC$TL_error, org.telegram.tgnet.TLObject, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.Integer, java.lang.Integer, java.lang.String, java.util.HashMap, java.lang.String, java.lang.String, java.lang.String, org.telegram.tgnet.TLRPC$TL_wallPaper):void");
    }

    public /* synthetic */ void lambda$null$13$LaunchActivity(int i, String str, String str2, String str3, String str4, String str5, String str6, boolean z, Integer num, Integer num2, String str7, HashMap hashMap, String str8, String str9, String str10, TL_wallPaper tL_wallPaper, DialogInterface dialogInterface, int i2) {
        runLinkRequest(i, str, str2, str3, str4, str5, str6, z, num, num2, str7, hashMap, str8, str9, str10, tL_wallPaper, 1);
    }

    public /* synthetic */ void lambda$runLinkRequest$17$LaunchActivity(int i, AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        if (tL_error == null) {
            MessagesController.getInstance(i).processUpdates((Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new C1586-$$Lambda$LaunchActivity$k9Ujiji1KdWARvma6ge0odbCoWk(this, alertDialog, tL_error, tLObject, i));
    }

    public /* synthetic */ void lambda$null$16$LaunchActivity(AlertDialog alertDialog, TL_error tL_error, TLObject tLObject, int i) {
        if (!isFinishing()) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.m30e(e);
            }
            if (tL_error != null) {
                Builder builder = new Builder((Context) this);
                builder.setTitle(LocaleController.getString("AppName", C1067R.string.AppName));
                if (tL_error.text.startsWith("FLOOD_WAIT")) {
                    builder.setMessage(LocaleController.getString("FloodWait", C1067R.string.FloodWait));
                } else if (tL_error.text.equals("USERS_TOO_MUCH")) {
                    builder.setMessage(LocaleController.getString("JoinToGroupErrorFull", C1067R.string.JoinToGroupErrorFull));
                } else {
                    builder.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", C1067R.string.JoinToGroupErrorNotExist));
                }
                builder.setPositiveButton(LocaleController.getString("OK", C1067R.string.f61OK), null);
                showAlertDialog(builder);
            } else if (this.actionBarLayout != null) {
                Updates updates = (Updates) tLObject;
                if (!updates.chats.isEmpty()) {
                    Chat chat = (Chat) updates.chats.get(0);
                    chat.left = false;
                    chat.kicked = false;
                    MessagesController.getInstance(i).putUsers(updates.users, false);
                    MessagesController.getInstance(i).putChats(updates.chats, false);
                    Bundle bundle = new Bundle();
                    bundle.putInt("chat_id", chat.f434id);
                    if (!mainFragmentsStack.isEmpty()) {
                        MessagesController instance = MessagesController.getInstance(i);
                        ArrayList arrayList = mainFragmentsStack;
                        if (!instance.checkCanOpenChat(bundle, (BaseFragment) arrayList.get(arrayList.size() - 1))) {
                            return;
                        }
                    }
                    ChatActivity chatActivity = new ChatActivity(bundle);
                    NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
                    this.actionBarLayout.presentFragment(chatActivity, false, true, true, false);
                }
            }
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$18$LaunchActivity(boolean z, int i, String str, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z2) {
        ArrayList arrayList2 = arrayList;
        long longValue = ((Long) arrayList.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        boolean z3 = z;
        bundle.putBoolean("hasUrl", z);
        int i2 = (int) longValue;
        int i3 = (int) (longValue >> 32);
        if (i2 != 0) {
            String str2 = "chat_id";
            if (i3 == 1) {
                bundle.putInt(str2, i2);
            } else if (i2 > 0) {
                bundle.putInt("user_id", i2);
            } else if (i2 < 0) {
                bundle.putInt(str2, -i2);
            }
        } else {
            bundle.putInt("enc_id", i3);
        }
        DialogsActivity dialogsActivity2 = dialogsActivity;
        if (MessagesController.getInstance(i).checkCanOpenChat(bundle, dialogsActivity)) {
            NotificationCenter.getInstance(i).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            DataQuery.getInstance(i).saveDraft(longValue, str, null, null, false);
            this.actionBarLayout.presentFragment(new ChatActivity(bundle), true, false, true, false);
            return;
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$22$LaunchActivity(int[] iArr, int i, AlertDialog alertDialog, TL_account_getAuthorizationForm tL_account_getAuthorizationForm, String str, String str2, String str3, TLObject tLObject, TL_error tL_error) {
        TL_account_authorizationForm tL_account_authorizationForm = (TL_account_authorizationForm) tLObject;
        if (tL_account_authorizationForm != null) {
            iArr[0] = ConnectionsManager.getInstance(i).sendRequest(new TL_account_getPassword(), new C3711-$$Lambda$LaunchActivity$GofL0vsgJimoLHa3SGxufnpInEA(this, alertDialog, i, tL_account_authorizationForm, tL_account_getAuthorizationForm, str, str2, str3));
            return;
        }
        AndroidUtilities.runOnUIThread(new C1581-$$Lambda$LaunchActivity$YQzhQ7yG96OboNlBWD_9IQ2DM9I(this, alertDialog, tL_error));
    }

    public /* synthetic */ void lambda$null$20$LaunchActivity(AlertDialog alertDialog, int i, TL_account_authorizationForm tL_account_authorizationForm, TL_account_getAuthorizationForm tL_account_getAuthorizationForm, String str, String str2, String str3, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1576-$$Lambda$LaunchActivity$PDILZQFsnUj7QGqTcIzCivtaxlA(this, alertDialog, tLObject, i, tL_account_authorizationForm, tL_account_getAuthorizationForm, str, str2, str3));
    }

    public /* synthetic */ void lambda$null$19$LaunchActivity(AlertDialog alertDialog, TLObject tLObject, int i, TL_account_authorizationForm tL_account_authorizationForm, TL_account_getAuthorizationForm tL_account_getAuthorizationForm, String str, String str2, String str3) {
        TL_account_getAuthorizationForm tL_account_getAuthorizationForm2 = tL_account_getAuthorizationForm;
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tLObject != null) {
            TL_account_password tL_account_password = (TL_account_password) tLObject;
            MessagesController.getInstance(i).putUsers(tL_account_authorizationForm.users, false);
            lambda$runLinkRequest$27$LaunchActivity(new PassportActivity(5, tL_account_getAuthorizationForm2.bot_id, tL_account_getAuthorizationForm2.scope, tL_account_getAuthorizationForm2.public_key, str, str2, str3, tL_account_authorizationForm, tL_account_password));
            return;
        }
    }

    public /* synthetic */ void lambda$null$21$LaunchActivity(AlertDialog alertDialog, TL_error tL_error) {
        try {
            alertDialog.dismiss();
            if ("APP_VERSION_OUTDATED".equals(tL_error.text)) {
                AlertsCreator.showUpdateAppAlert(this, LocaleController.getString("UpdateAppAlert", C1067R.string.UpdateAppAlert), true);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LocaleController.getString("ErrorOccurred", C1067R.string.ErrorOccurred));
            stringBuilder.append("\n");
            stringBuilder.append(tL_error.text);
            showAlertDialog(AlertsCreator.createSimpleAlert(this, stringBuilder.toString()));
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$24$LaunchActivity(AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1569-$$Lambda$LaunchActivity$I0-icmFxixS_c3kxY_ZR1JLcHdI(this, alertDialog, tLObject));
    }

    public /* synthetic */ void lambda$null$23$LaunchActivity(AlertDialog alertDialog, TLObject tLObject) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tLObject instanceof TL_help_deepLinkInfo) {
            TL_help_deepLinkInfo tL_help_deepLinkInfo = (TL_help_deepLinkInfo) tLObject;
            AlertsCreator.showUpdateAppAlert(this, tL_help_deepLinkInfo.message, tL_help_deepLinkInfo.update_app);
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$26$LaunchActivity(AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1565-$$Lambda$LaunchActivity$3LcmzAiodHFy1RroGRficYduxV8(this, alertDialog, tLObject, tL_error));
    }

    public /* synthetic */ void lambda$null$25$LaunchActivity(AlertDialog alertDialog, TLObject tLObject, TL_error tL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tLObject instanceof TL_langPackLanguage) {
            showAlertDialog(AlertsCreator.createLanguageAlert(this, (TL_langPackLanguage) tLObject));
        } else if (tL_error != null) {
            if ("LANG_CODE_NOT_SUPPORTED".equals(tL_error.text)) {
                showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LanguageUnsupportedError", C1067R.string.LanguageUnsupportedError)));
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(LocaleController.getString("ErrorOccurred", C1067R.string.ErrorOccurred));
            stringBuilder.append("\n");
            stringBuilder.append(tL_error.text);
            showAlertDialog(AlertsCreator.createSimpleAlert(this, stringBuilder.toString()));
        }
    }

    public /* synthetic */ void lambda$runLinkRequest$29$LaunchActivity(AlertDialog alertDialog, TL_wallPaper tL_wallPaper, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1590-$$Lambda$LaunchActivity$nSOp0Z8yCajsdR-ViEON2LJngGI(this, alertDialog, tLObject, tL_wallPaper, tL_error));
    }

    public /* synthetic */ void lambda$null$28$LaunchActivity(AlertDialog alertDialog, TLObject tLObject, TL_wallPaper tL_wallPaper, TL_error tL_error) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (tLObject instanceof TL_wallPaper) {
            Object obj;
            TL_wallPaper tL_wallPaper2 = (TL_wallPaper) tLObject;
            if (tL_wallPaper2.pattern) {
                TL_wallPaperSettings tL_wallPaperSettings = tL_wallPaper.settings;
                ColorWallpaper colorWallpaper = new ColorWallpaper(-1, tL_wallPaperSettings.background_color, tL_wallPaper2.f569id, ((float) tL_wallPaperSettings.intensity) / 100.0f, tL_wallPaperSettings.motion, null);
                colorWallpaper.pattern = tL_wallPaper2;
            } else {
                obj = tL_wallPaper2;
            }
            WallpaperActivity wallpaperActivity = new WallpaperActivity(obj, null);
            TL_wallPaperSettings tL_wallPaperSettings2 = tL_wallPaper.settings;
            wallpaperActivity.setInitialModes(tL_wallPaperSettings2.blur, tL_wallPaperSettings2.motion);
            lambda$runLinkRequest$27$LaunchActivity(wallpaperActivity);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LocaleController.getString("ErrorOccurred", C1067R.string.ErrorOccurred));
        stringBuilder.append("\n");
        stringBuilder.append(tL_error.text);
        showAlertDialog(AlertsCreator.createSimpleAlert(this, stringBuilder.toString()));
    }

    public /* synthetic */ void lambda$runLinkRequest$32$LaunchActivity(Bundle bundle, Integer num, int[] iArr, AlertDialog alertDialog, BaseFragment baseFragment, int i) {
        if (!this.actionBarLayout.presentFragment(new ChatActivity(bundle))) {
            TL_channels_getChannels tL_channels_getChannels = new TL_channels_getChannels();
            TL_inputChannel tL_inputChannel = new TL_inputChannel();
            tL_inputChannel.channel_id = num.intValue();
            tL_channels_getChannels.f474id.add(tL_inputChannel);
            iArr[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_channels_getChannels, new C3721-$$Lambda$LaunchActivity$nGa4oq5QhPR8XqADPUUKTB3K8VU(this, alertDialog, baseFragment, i, bundle));
        }
    }

    public /* synthetic */ void lambda$null$31$LaunchActivity(AlertDialog alertDialog, BaseFragment baseFragment, int i, Bundle bundle, TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1597-$$Lambda$LaunchActivity$z_gJkNbvZ8jzLDA3H5Zgz1rUffg(this, alertDialog, tLObject, baseFragment, i, bundle));
    }

    public /* synthetic */ void lambda$null$30$LaunchActivity(AlertDialog alertDialog, TLObject tLObject, BaseFragment baseFragment, int i, Bundle bundle) {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        Object obj = 1;
        if (tLObject instanceof TL_messages_chats) {
            TL_messages_chats tL_messages_chats = (TL_messages_chats) tLObject;
            if (!tL_messages_chats.chats.isEmpty()) {
                MessagesController.getInstance(this.currentAccount).putChats(tL_messages_chats.chats, false);
                Chat chat = (Chat) tL_messages_chats.chats.get(0);
                if (baseFragment == null || MessagesController.getInstance(i).checkCanOpenChat(bundle, baseFragment)) {
                    this.actionBarLayout.presentFragment(new ChatActivity(bundle));
                }
                obj = null;
            }
        }
        if (obj != null) {
            showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", C1067R.string.LinkNotFound)));
        }
    }

    public void checkAppUpdate(boolean z) {
        if ((!z && BuildVars.DEBUG_VERSION) || (!z && !BuildVars.CHECK_UPDATES)) {
            return;
        }
        if (z || Math.abs(System.currentTimeMillis() - UserConfig.getInstance(0).lastUpdateCheckTime) >= 86400000) {
            TL_help_getAppUpdate tL_help_getAppUpdate = new TL_help_getAppUpdate();
            try {
                tL_help_getAppUpdate.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
            } catch (Exception unused) {
            }
            if (tL_help_getAppUpdate.source == null) {
                tL_help_getAppUpdate.source = "";
            }
            int i = this.currentAccount;
            ConnectionsManager.getInstance(i).sendRequest(tL_help_getAppUpdate, new C3705-$$Lambda$LaunchActivity$1W6W_t-pAiSBx5i38jWX5eUMkro(this, i));
        }
    }

    public /* synthetic */ void lambda$checkAppUpdate$35$LaunchActivity(int i, TLObject tLObject, TL_error tL_error) {
        UserConfig.getInstance(0).lastUpdateCheckTime = System.currentTimeMillis();
        UserConfig.getInstance(0).saveConfig(false);
        if (tLObject instanceof TL_help_appUpdate) {
            AndroidUtilities.runOnUIThread(new C1595-$$Lambda$LaunchActivity$zIyXV0iNbVBNj3pWgG-YQ490Bu4(this, (TL_help_appUpdate) tLObject, i));
        }
    }

    public /* synthetic */ void lambda$null$34$LaunchActivity(TL_help_appUpdate tL_help_appUpdate, int i) {
        if (BuildVars.DEBUG_PRIVATE_VERSION) {
            tL_help_appUpdate.popup = Utilities.random.nextBoolean();
        }
        if (tL_help_appUpdate.popup) {
            UserConfig.getInstance(0).pendingAppUpdate = tL_help_appUpdate;
            UserConfig.getInstance(0).pendingAppUpdateBuildVersion = BuildVars.BUILD_VERSION;
            try {
                PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                UserConfig.getInstance(0).pendingAppUpdateInstallTime = Math.max(packageInfo.lastUpdateTime, packageInfo.firstInstallTime);
            } catch (Exception e) {
                FileLog.m30e(e);
                UserConfig.getInstance(0).pendingAppUpdateInstallTime = 0;
            }
            UserConfig.getInstance(0).saveConfig(false);
            showUpdateActivity(i, tL_help_appUpdate);
            return;
        }
        new UpdateAppAlertDialog(this, tL_help_appUpdate, i).show();
    }

    public AlertDialog showAlertDialog(Builder builder) {
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        try {
            this.visibleDialog = builder.show();
            this.visibleDialog.setCanceledOnTouchOutside(true);
            this.visibleDialog.setOnDismissListener(new C30644());
            return this.visibleDialog;
        } catch (Exception e2) {
            FileLog.m30e(e2);
            return null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent, true, false, false);
    }

    public void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList<Long> arrayList, CharSequence charSequence, boolean z) {
        BaseFragment baseFragment = dialogsActivity;
        int i = 0;
        long longValue = ((Long) arrayList.get(0)).longValue();
        int i2 = (int) longValue;
        int i3 = (int) (longValue >> 32);
        Bundle bundle = new Bundle();
        int currentAccount = baseFragment != null ? dialogsActivity.getCurrentAccount() : this.currentAccount;
        bundle.putBoolean("scrollToTopOnResume", true);
        if (!AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        }
        if (i2 != 0) {
            String str = "chat_id";
            if (i3 == 1) {
                bundle.putInt(str, i2);
            } else if (i2 > 0) {
                bundle.putInt("user_id", i2);
            } else if (i2 < 0) {
                bundle.putInt(str, -i2);
            }
        } else {
            bundle.putInt("enc_id", i3);
        }
        if (MessagesController.getInstance(currentAccount).checkCanOpenChat(bundle, baseFragment)) {
            ArrayList arrayList2;
            BaseFragment chatActivity = new ChatActivity(bundle);
            ArrayList arrayList3 = this.contactsToSend;
            if (arrayList3 == null || arrayList3.size() != 1) {
                int i4;
                arrayList2 = null;
                this.actionBarLayout.presentFragment(chatActivity, baseFragment != null, baseFragment == null, true, false);
                String str2 = this.videoPath;
                if (str2 != null) {
                    chatActivity.openVideoEditor(str2, this.sendingText);
                    this.sendingText = arrayList2;
                }
                if (this.photoPathsArray != null) {
                    str2 = this.sendingText;
                    if (str2 != null && str2.length() <= 1024 && this.photoPathsArray.size() == 1) {
                        ((SendingMediaInfo) this.photoPathsArray.get(0)).caption = this.sendingText;
                        this.sendingText = arrayList2;
                    }
                    i4 = 1;
                    SendMessagesHelper.prepareSendingMedia(this.photoPathsArray, longValue, null, null, false, false, null);
                } else {
                    i4 = 1;
                }
                if (!(this.documentsPathsArray == null && this.documentsUrisArray == null)) {
                    String str3;
                    str2 = this.sendingText;
                    if (str2 != null && str2.length() <= 1024) {
                        arrayList3 = this.documentsPathsArray;
                        i2 = arrayList3 != null ? arrayList3.size() : 0;
                        ArrayList arrayList4 = this.documentsUrisArray;
                        if (i2 + (arrayList4 != null ? arrayList4.size() : 0) == i4) {
                            str2 = this.sendingText;
                            this.sendingText = arrayList2;
                            str3 = str2;
                            SendMessagesHelper.prepareSendingDocuments(this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str3, this.documentsMimeType, longValue, null, null, null);
                        }
                    }
                    str3 = arrayList2;
                    SendMessagesHelper.prepareSendingDocuments(this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, str3, this.documentsMimeType, longValue, null, null, null);
                }
                Location location = this.sendingLocation;
                if (location != null) {
                    SendMessagesHelper.prepareSendingLocation(location, longValue);
                    this.sendingText = arrayList2;
                }
                str2 = this.sendingText;
                if (str2 != null) {
                    SendMessagesHelper.prepareSendingText(str2, longValue);
                }
                arrayList3 = this.contactsToSend;
                if (!(arrayList3 == null || arrayList3.isEmpty())) {
                    while (i < this.contactsToSend.size()) {
                        SendMessagesHelper.getInstance(currentAccount).sendMessage((User) this.contactsToSend.get(i), longValue, null, null, null);
                        i++;
                    }
                }
            } else {
                if (this.contactsToSend.size() == 1) {
                    BaseFragment phonebookShareActivity = new PhonebookShareActivity(null, this.contactsToSendUri, null, null);
                    phonebookShareActivity.setDelegate(new C3715-$$Lambda$LaunchActivity$Uzy9GRHh4_9QiCM66VVA1Ftqfj0(this, chatActivity, currentAccount, longValue));
                    this.actionBarLayout.presentFragment(phonebookShareActivity, baseFragment != null, baseFragment == null, true, false);
                }
                arrayList2 = null;
            }
            this.photoPathsArray = arrayList2;
            this.videoPath = arrayList2;
            this.sendingText = arrayList2;
            this.sendingLocation = arrayList2;
            this.documentsPathsArray = arrayList2;
            this.documentsOriginalPathsArray = arrayList2;
            this.contactsToSend = arrayList2;
            this.contactsToSendUri = arrayList2;
        }
    }

    public /* synthetic */ void lambda$didSelectDialogs$36$LaunchActivity(ChatActivity chatActivity, int i, long j, User user) {
        this.actionBarLayout.presentFragment(chatActivity, true, false, true, false);
        SendMessagesHelper.getInstance(i).sendMessage(user, j, null, null, null);
    }

    private void onFinish() {
        if (!this.finished) {
            this.finished = true;
            Runnable runnable = this.lockRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.lockRunnable = null;
            }
            int i = this.currentAccount;
            if (i != -1) {
                NotificationCenter.getInstance(i).removeObserver(this, NotificationCenter.appDidLogout);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
            }
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.notificationsCountUpdated);
        }
    }

    /* renamed from: presentFragment */
    public void lambda$runLinkRequest$27$LaunchActivity(BaseFragment baseFragment) {
        this.actionBarLayout.presentFragment(baseFragment);
    }

    public boolean presentFragment(BaseFragment baseFragment, boolean z, boolean z2) {
        return this.actionBarLayout.presentFragment(baseFragment, z, z2, true, false);
    }

    public ActionBarLayout getActionBarLayout() {
        return this.actionBarLayout;
    }

    public ActionBarLayout getLayersActionBarLayout() {
        return this.layersActionBarLayout;
    }

    public ActionBarLayout getRightActionBarLayout() {
        return this.rightActionBarLayout;
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        ArrayList arrayList;
        if (!(SharedConfig.passcodeHash.length() == 0 || SharedConfig.lastPauseTime == 0)) {
            SharedConfig.lastPauseTime = 0;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        super.onActivityResult(i, i2, intent);
        ThemeEditorView instance = ThemeEditorView.getInstance();
        if (instance != null) {
            instance.onActivityResult(i, i2, intent);
        }
        if (this.actionBarLayout.fragmentsStack.size() != 0) {
            arrayList = this.actionBarLayout.fragmentsStack;
            ((BaseFragment) arrayList.get(arrayList.size() - 1)).onActivityResultFragment(i, i2, intent);
        }
        if (AndroidUtilities.isTablet()) {
            if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
                arrayList = this.rightActionBarLayout.fragmentsStack;
                ((BaseFragment) arrayList.get(arrayList.size() - 1)).onActivityResultFragment(i, i2, intent);
            }
            if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
                arrayList = this.layersActionBarLayout.fragmentsStack;
                ((BaseFragment) arrayList.get(arrayList.size() - 1)).onActivityResultFragment(i, i2, intent);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0068  */
    /* JADX WARNING: Missing block: B:31:0x0062, code skipped:
            if (r11 != 22) goto L_0x0065;
     */
    public void onRequestPermissionsResult(int r11, java.lang.String[] r12, int[] r13) {
        /*
        r10 = this;
        super.onRequestPermissionsResult(r11, r12, r13);
        r0 = 22;
        r1 = 20;
        r2 = 19;
        r3 = 5;
        r4 = 4;
        r5 = 3;
        r6 = 0;
        r7 = 1;
        if (r11 == r5) goto L_0x0032;
    L_0x0010:
        if (r11 == r4) goto L_0x0032;
    L_0x0012:
        if (r11 == r3) goto L_0x0032;
    L_0x0014:
        if (r11 == r2) goto L_0x0032;
    L_0x0016:
        if (r11 == r1) goto L_0x0032;
    L_0x0018:
        if (r11 != r0) goto L_0x001b;
    L_0x001a:
        goto L_0x0032;
    L_0x001b:
        r0 = 2;
        if (r11 != r0) goto L_0x00d9;
    L_0x001e:
        r0 = r13.length;
        if (r0 <= 0) goto L_0x00d9;
    L_0x0021:
        r0 = r13[r6];
        if (r0 != 0) goto L_0x00d9;
    L_0x0025:
        r0 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r1 = org.telegram.messenger.NotificationCenter.locationPermissionGranted;
        r2 = new java.lang.Object[r6];
        r0.postNotificationName(r1, r2);
        goto L_0x00d9;
    L_0x0032:
        r8 = r13.length;
        r9 = 0;
        if (r8 <= 0) goto L_0x0065;
    L_0x0036:
        r8 = r13[r6];
        if (r8 != 0) goto L_0x0065;
    L_0x003a:
        if (r11 != r4) goto L_0x0044;
    L_0x003c:
        r11 = org.telegram.messenger.ImageLoader.getInstance();
        r11.checkMediaPaths();
        return;
    L_0x0044:
        if (r11 != r3) goto L_0x0050;
    L_0x0046:
        r11 = r10.currentAccount;
        r11 = org.telegram.messenger.ContactsController.getInstance(r11);
        r11.forceImportContacts();
        return;
    L_0x0050:
        if (r11 != r5) goto L_0x005e;
    L_0x0052:
        r11 = org.telegram.messenger.SharedConfig.inappCamera;
        if (r11 == 0) goto L_0x005d;
    L_0x0056:
        r11 = org.telegram.messenger.camera.CameraController.getInstance();
        r11.initCamera(r9);
    L_0x005d:
        return;
    L_0x005e:
        if (r11 == r2) goto L_0x0066;
    L_0x0060:
        if (r11 == r1) goto L_0x0066;
    L_0x0062:
        if (r11 != r0) goto L_0x0065;
    L_0x0064:
        goto L_0x0066;
    L_0x0065:
        r6 = 1;
    L_0x0066:
        if (r6 == 0) goto L_0x00d9;
    L_0x0068:
        r12 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r12.<init>(r10);
        r13 = 2131558635; // 0x7f0d00eb float:1.8742591E38 double:1.0531298936E-314;
        r6 = "AppName";
        r13 = org.telegram.messenger.LocaleController.getString(r6, r13);
        r12.setTitle(r13);
        if (r11 != r5) goto L_0x0088;
    L_0x007b:
        r11 = 2131560414; // 0x7f0d07de float:1.87462E38 double:1.0531307726E-314;
        r13 = "PermissionNoAudio";
        r11 = org.telegram.messenger.LocaleController.getString(r13, r11);
        r12.setMessage(r11);
        goto L_0x00b8;
    L_0x0088:
        if (r11 != r4) goto L_0x0097;
    L_0x008a:
        r11 = 2131560420; // 0x7f0d07e4 float:1.8746212E38 double:1.0531307756E-314;
        r13 = "PermissionStorage";
        r11 = org.telegram.messenger.LocaleController.getString(r13, r11);
        r12.setMessage(r11);
        goto L_0x00b8;
    L_0x0097:
        if (r11 != r3) goto L_0x00a6;
    L_0x0099:
        r11 = 2131560412; // 0x7f0d07dc float:1.8746196E38 double:1.0531307716E-314;
        r13 = "PermissionContacts";
        r11 = org.telegram.messenger.LocaleController.getString(r13, r11);
        r12.setMessage(r11);
        goto L_0x00b8;
    L_0x00a6:
        if (r11 == r2) goto L_0x00ac;
    L_0x00a8:
        if (r11 == r1) goto L_0x00ac;
    L_0x00aa:
        if (r11 != r0) goto L_0x00b8;
    L_0x00ac:
        r11 = 2131560416; // 0x7f0d07e0 float:1.8746204E38 double:1.0531307736E-314;
        r13 = "PermissionNoCamera";
        r11 = org.telegram.messenger.LocaleController.getString(r13, r11);
        r12.setMessage(r11);
    L_0x00b8:
        r11 = 2131560419; // 0x7f0d07e3 float:1.874621E38 double:1.053130775E-314;
        r13 = "PermissionOpenSettings";
        r11 = org.telegram.messenger.LocaleController.getString(r13, r11);
        r13 = new org.telegram.ui.-$$Lambda$LaunchActivity$4W20lwtGVi8T2FP_31dzdYvI9yo;
        r13.<init>(r10);
        r12.setNegativeButton(r11, r13);
        r11 = 2131560097; // 0x7f0d06a1 float:1.8745557E38 double:1.053130616E-314;
        r13 = "OK";
        r11 = org.telegram.messenger.LocaleController.getString(r13, r11);
        r12.setPositiveButton(r11, r9);
        r12.show();
        return;
    L_0x00d9:
        r0 = r10.actionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 == 0) goto L_0x00f5;
    L_0x00e3:
        r0 = r10.actionBarLayout;
        r0 = r0.fragmentsStack;
        r1 = r0.size();
        r1 = r1 - r7;
        r0 = r0.get(r1);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r0.onRequestPermissionsResultFragment(r11, r12, r13);
    L_0x00f5:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x0133;
    L_0x00fb:
        r0 = r10.rightActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 == 0) goto L_0x0117;
    L_0x0105:
        r0 = r10.rightActionBarLayout;
        r0 = r0.fragmentsStack;
        r1 = r0.size();
        r1 = r1 - r7;
        r0 = r0.get(r1);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r0.onRequestPermissionsResultFragment(r11, r12, r13);
    L_0x0117:
        r0 = r10.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r0 = r0.size();
        if (r0 == 0) goto L_0x0133;
    L_0x0121:
        r0 = r10.layersActionBarLayout;
        r0 = r0.fragmentsStack;
        r1 = r0.size();
        r1 = r1 - r7;
        r0 = r0.get(r1);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r0.onRequestPermissionsResultFragment(r11, r12, r13);
    L_0x0133:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.onRequestPermissionsResult(int, java.lang.String[], int[]):void");
    }

    public /* synthetic */ void lambda$onRequestPermissionsResult$37$LaunchActivity(DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("package:");
            stringBuilder.append(ApplicationLoader.applicationContext.getPackageName());
            intent.setData(Uri.parse(stringBuilder.toString()));
            startActivity(intent);
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        SharedConfig.lastAppPauseTime = System.currentTimeMillis();
        ApplicationLoader.mainInterfacePaused = true;
        Utilities.stageQueue.postRunnable(C1592-$$Lambda$LaunchActivity$uQqSZiudecpXZp8TwXxpaZLYG6E.INSTANCE);
        onPasscodePause();
        this.actionBarLayout.onPause();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onPause();
            this.layersActionBarLayout.onPause();
        }
        PasscodeView passcodeView = this.passcodeView;
        if (passcodeView != null) {
            passcodeView.onPause();
        }
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        AndroidUtilities.unregisterUpdates();
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onPause();
        }
    }

    static /* synthetic */ void lambda$onPause$38() {
        ApplicationLoader.mainInterfacePausedStageQueue = true;
        ApplicationLoader.mainInterfacePausedStageQueueTime = 0;
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        super.onStart();
        Browser.bindCustomTabsService(this);
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        Browser.unbindCustomTabsService(this);
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        if (PhotoViewer.getPipInstance() != null) {
            PhotoViewer.getPipInstance().destroyPhotoViewer();
        }
        if (PhotoViewer.hasInstance()) {
            PhotoViewer.getInstance().destroyPhotoViewer();
        }
        if (SecretMediaViewer.hasInstance()) {
            SecretMediaViewer.getInstance().destroyPhotoViewer();
        }
        if (ArticleViewer.hasInstance()) {
            ArticleViewer.getInstance().destroyArticleViewer();
        }
        if (ContentPreviewViewer.hasInstance()) {
            ContentPreviewViewer.getInstance().destroy();
        }
        PipRoundVideoView instance = PipRoundVideoView.getInstance();
        MediaController.getInstance().setBaseActivity(this, false);
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, false);
        if (instance != null) {
            instance.close(false);
        }
        Theme.destroyResources();
        EmbedBottomSheet instance2 = EmbedBottomSheet.getInstance();
        if (instance2 != null) {
            instance2.destroy();
        }
        ThemeEditorView instance3 = ThemeEditorView.getInstance();
        if (instance3 != null) {
            instance3.destroy();
        }
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        try {
            if (this.onGlobalLayoutListener != null) {
                getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
            }
        } catch (Exception e2) {
            FileLog.m30e(e2);
        }
        super.onDestroy();
        onFinish();
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        MediaController.getInstance().setFeedbackView(this.actionBarLayout, true);
        ApplicationLoader.mainInterfacePaused = false;
        showLanguageAlert(false);
        Utilities.stageQueue.postRunnable(C1582-$$Lambda$LaunchActivity$ZNfXQPqW9KpGIQ6C_so6U6aBbbU.INSTANCE);
        checkFreeDiscSpace();
        MediaController.checkGallery();
        onPasscodeResume();
        if (this.passcodeView.getVisibility() != 0) {
            this.actionBarLayout.onResume();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onResume();
                this.layersActionBarLayout.onResume();
            }
        } else {
            this.actionBarLayout.dismissDialogs();
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.dismissDialogs();
                this.layersActionBarLayout.dismissDialogs();
            }
            this.passcodeView.onResume();
        }
        AndroidUtilities.checkForCrashes(this);
        AndroidUtilities.checkForUpdates(this);
        ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        updateCurrentConnectionState(this.currentAccount);
        if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().onResume();
        }
        if (PipRoundVideoView.getInstance() != null && MediaController.getInstance().isMessagePaused()) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null) {
                MediaController.getInstance().seekToProgress(playingMessageObject, playingMessageObject.audioProgress);
            }
        }
        if (UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService != null) {
            int i = UserConfig.selectedAccount;
            showTosActivity(i, UserConfig.getInstance(i).unacceptedTermsOfService);
        } else if (UserConfig.getInstance(0).pendingAppUpdate != null) {
            showUpdateActivity(UserConfig.selectedAccount, UserConfig.getInstance(0).pendingAppUpdate);
        }
        checkAppUpdate(false);
    }

    static /* synthetic */ void lambda$onResume$39() {
        ApplicationLoader.mainInterfacePausedStageQueue = false;
        ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
    }

    public void onConfigurationChanged(Configuration configuration) {
        AndroidUtilities.checkDisplaySize(this, configuration);
        super.onConfigurationChanged(configuration);
        checkLayout();
        PipRoundVideoView instance = PipRoundVideoView.getInstance();
        if (instance != null) {
            instance.onConfigurationChanged();
        }
        EmbedBottomSheet instance2 = EmbedBottomSheet.getInstance();
        if (instance2 != null) {
            instance2.onConfigurationChanged(configuration);
        }
        PhotoViewer pipInstance = PhotoViewer.getPipInstance();
        if (pipInstance != null) {
            pipInstance.onConfigurationChanged(configuration);
        }
        ThemeEditorView instance3 = ThemeEditorView.getInstance();
        if (instance3 != null) {
            instance3.onConfigurationChanged();
        }
    }

    public void onMultiWindowModeChanged(boolean z) {
        AndroidUtilities.isInMultiwindow = z;
        checkLayout();
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.appDidLogout) {
            switchToAvailableAccountOrLogout();
        } else {
            int i3 = 0;
            if (i == NotificationCenter.closeOtherAppActivities) {
                if (objArr[0] != this) {
                    onFinish();
                    finish();
                }
            } else if (i == NotificationCenter.didUpdateConnectionState) {
                i = ConnectionsManager.getInstance(i2).getConnectionState();
                if (this.currentConnectionState != i) {
                    if (BuildVars.LOGS_ENABLED) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("switch to state ");
                        stringBuilder.append(i);
                        FileLog.m27d(stringBuilder.toString());
                    }
                    this.currentConnectionState = i;
                    updateCurrentConnectionState(i2);
                }
            } else if (i == NotificationCenter.mainUserInfoChanged) {
                this.drawerLayoutAdapter.notifyDataSetChanged();
            } else {
                String str = "Cancel";
                String str2 = "AppName";
                String str3 = "OK";
                ArrayList arrayList;
                HashMap hashMap;
                RecyclerListView recyclerListView;
                if (i == NotificationCenter.needShowAlert) {
                    Integer num = (Integer) objArr[0];
                    if (num.intValue() == 3 && this.proxyErrorDialog != null) {
                        return;
                    }
                    if (num.intValue() == 4) {
                        showTosActivity(i2, (TL_help_termsOfService) objArr[1]);
                        return;
                    }
                    Builder builder = new Builder((Context) this);
                    builder.setTitle(LocaleController.getString(str2, C1067R.string.AppName));
                    if (!(num.intValue() == 2 || num.intValue() == 3)) {
                        builder.setNegativeButton(LocaleController.getString("MoreInfo", C1067R.string.MoreInfo), new C1591-$$Lambda$LaunchActivity$sNXuL3LKkNwiULIaBmkQorXYhkE(i2));
                    }
                    if (num.intValue() == 5) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam3", C1067R.string.NobodyLikesSpam3));
                        builder.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), null);
                    } else if (num.intValue() == 0) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam1", C1067R.string.NobodyLikesSpam1));
                        builder.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), null);
                    } else if (num.intValue() == 1) {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam2", C1067R.string.NobodyLikesSpam2));
                        builder.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), null);
                    } else if (num.intValue() == 2) {
                        builder.setMessage((String) objArr[1]);
                        if (((String) objArr[2]).startsWith("AUTH_KEY_DROP_")) {
                            builder.setPositiveButton(LocaleController.getString(str, C1067R.string.Cancel), null);
                            builder.setNegativeButton(LocaleController.getString("LogOut", C1067R.string.LogOut), new C1571-$$Lambda$LaunchActivity$KI45KCZymdE2FpdMIxAM9-1Ovt8(this));
                        } else {
                            builder.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), null);
                        }
                    } else if (num.intValue() == 3) {
                        builder.setMessage(LocaleController.getString("UseProxyTelegramError", C1067R.string.UseProxyTelegramError));
                        builder.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), null);
                        this.proxyErrorDialog = showAlertDialog(builder);
                        return;
                    }
                    if (!mainFragmentsStack.isEmpty()) {
                        arrayList = mainFragmentsStack;
                        ((BaseFragment) arrayList.get(arrayList.size() - 1)).showDialog(builder.create());
                    }
                } else if (i == NotificationCenter.wasUnableToFindCurrentLocation) {
                    hashMap = (HashMap) objArr[0];
                    Builder builder2 = new Builder((Context) this);
                    builder2.setTitle(LocaleController.getString(str2, C1067R.string.AppName));
                    builder2.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), null);
                    builder2.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", C1067R.string.ShareYouLocationUnableManually), new C1584-$$Lambda$LaunchActivity$g3PH5EQViR8m49F5t2uLHcHps64(this, hashMap, i2));
                    builder2.setMessage(LocaleController.getString("ShareYouLocationUnable", C1067R.string.ShareYouLocationUnable));
                    if (!mainFragmentsStack.isEmpty()) {
                        arrayList = mainFragmentsStack;
                        ((BaseFragment) arrayList.get(arrayList.size() - 1)).showDialog(builder2.create());
                    }
                } else if (i == NotificationCenter.didSetNewWallpapper) {
                    recyclerListView = this.sideMenu;
                    if (recyclerListView != null) {
                        View childAt = recyclerListView.getChildAt(0);
                        if (childAt != null) {
                            childAt.invalidate();
                        }
                    }
                } else if (i == NotificationCenter.didSetPasscode) {
                    if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
                        try {
                            getWindow().setFlags(MessagesController.UPDATE_MASK_CHAT, MessagesController.UPDATE_MASK_CHAT);
                        } catch (Exception e) {
                            FileLog.m30e(e);
                        }
                    } else if (!MediaController.getInstance().hasFlagSecureFragment()) {
                        try {
                            getWindow().clearFlags(MessagesController.UPDATE_MASK_CHAT);
                        } catch (Exception e2) {
                            FileLog.m30e(e2);
                        }
                    }
                } else if (i == NotificationCenter.reloadInterface) {
                    boolean z;
                    if (mainFragmentsStack.size() > 1) {
                        arrayList = mainFragmentsStack;
                        if (arrayList.get(arrayList.size() - 1) instanceof SettingsActivity) {
                            z = true;
                        }
                    }
                    rebuildAllFragments(z);
                } else if (i == NotificationCenter.suggestedLangpack) {
                    showLanguageAlert(false);
                } else if (i == NotificationCenter.openArticle) {
                    if (!mainFragmentsStack.isEmpty()) {
                        ArticleViewer instance = ArticleViewer.getInstance();
                        ArrayList arrayList2 = mainFragmentsStack;
                        instance.setParentActivity(this, (BaseFragment) arrayList2.get(arrayList2.size() - 1));
                        ArticleViewer.getInstance().open((TL_webPage) objArr[0], (String) objArr[1]);
                    }
                } else if (i == NotificationCenter.hasNewContactsToImport) {
                    ActionBarLayout actionBarLayout = this.actionBarLayout;
                    if (actionBarLayout != null && !actionBarLayout.fragmentsStack.isEmpty()) {
                        ((Integer) objArr[0]).intValue();
                        hashMap = (HashMap) objArr[1];
                        boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                        boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
                        ArrayList arrayList3 = this.actionBarLayout.fragmentsStack;
                        BaseFragment baseFragment = (BaseFragment) arrayList3.get(arrayList3.size() - 1);
                        Builder builder3 = new Builder((Context) this);
                        builder3.setTitle(LocaleController.getString("UpdateContactsTitle", C1067R.string.UpdateContactsTitle));
                        builder3.setMessage(LocaleController.getString("UpdateContactsMessage", C1067R.string.UpdateContactsMessage));
                        builder3.setPositiveButton(LocaleController.getString(str3, C1067R.string.f61OK), new C1578-$$Lambda$LaunchActivity$V0ixus5t-k3sjeAzTAOnXbxIAXc(i2, hashMap, booleanValue, booleanValue2));
                        builder3.setNegativeButton(LocaleController.getString(str, C1067R.string.Cancel), new C1588-$$Lambda$LaunchActivity$lR7vjLvAzG-cCRS6htHMjvKKewI(i2, hashMap, booleanValue, booleanValue2));
                        builder3.setOnBackButtonListener(new C1567-$$Lambda$LaunchActivity$6f4_bg5ZpiXCOo5ikvs5uNelDMk(i2, hashMap, booleanValue, booleanValue2));
                        AlertDialog create = builder3.create();
                        baseFragment.showDialog(create);
                        create.setCanceledOnTouchOutside(false);
                    }
                } else if (i == NotificationCenter.didSetNewTheme) {
                    if (!((Boolean) objArr[0]).booleanValue()) {
                        recyclerListView = this.sideMenu;
                        if (recyclerListView != null) {
                            String str4 = Theme.key_chats_menuBackground;
                            recyclerListView.setBackgroundColor(Theme.getColor(str4));
                            this.sideMenu.setGlowColor(Theme.getColor(str4));
                            this.sideMenu.setListSelectorColor(Theme.getColor(Theme.key_listSelector));
                            this.sideMenu.getAdapter().notifyDataSetChanged();
                        }
                        if (VERSION.SDK_INT >= 21) {
                            try {
                                setTaskDescription(new TaskDescription(null, null, Theme.getColor(Theme.key_actionBarDefault) | Theme.ACTION_BAR_VIDEO_EDIT_COLOR));
                            } catch (Exception unused) {
                            }
                        }
                    }
                    this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                } else if (i == NotificationCenter.needSetDayNightTheme) {
                    ThemeInfo themeInfo = (ThemeInfo) objArr[0];
                    boolean booleanValue3 = ((Boolean) objArr[1]).booleanValue();
                    this.actionBarLayout.animateThemedValues(themeInfo, booleanValue3);
                    if (AndroidUtilities.isTablet()) {
                        this.layersActionBarLayout.animateThemedValues(themeInfo, booleanValue3);
                        this.rightActionBarLayout.animateThemedValues(themeInfo, booleanValue3);
                    }
                } else if (i == NotificationCenter.notificationsCountUpdated) {
                    recyclerListView = this.sideMenu;
                    if (recyclerListView != null) {
                        Integer num2 = (Integer) objArr[0];
                        i = recyclerListView.getChildCount();
                        while (i3 < i) {
                            View childAt2 = this.sideMenu.getChildAt(i3);
                            if ((childAt2 instanceof DrawerUserCell) && ((DrawerUserCell) childAt2).getAccountNumber() == num2.intValue()) {
                                childAt2.invalidate();
                                break;
                            }
                            i3++;
                        }
                    }
                }
            }
        }
    }

    static /* synthetic */ void lambda$didReceivedNotification$40(int i, DialogInterface dialogInterface, int i2) {
        if (!mainFragmentsStack.isEmpty()) {
            MessagesController instance = MessagesController.getInstance(i);
            ArrayList arrayList = mainFragmentsStack;
            instance.openByUserName("spambot", (BaseFragment) arrayList.get(arrayList.size() - 1), 1);
        }
    }

    public /* synthetic */ void lambda$didReceivedNotification$41$LaunchActivity(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).performLogout(2);
    }

    public /* synthetic */ void lambda$didReceivedNotification$43$LaunchActivity(HashMap hashMap, int i, DialogInterface dialogInterface, int i2) {
        if (!mainFragmentsStack.isEmpty()) {
            ArrayList arrayList = mainFragmentsStack;
            if (AndroidUtilities.isGoogleMapsInstalled((BaseFragment) arrayList.get(arrayList.size() - 1))) {
                LocationActivity locationActivity = new LocationActivity(0);
                locationActivity.setDelegate(new C3725-$$Lambda$LaunchActivity$vKuTfZca44pasLbBQmx8DozvnHc(hashMap, i));
                lambda$runLinkRequest$27$LaunchActivity(locationActivity);
            }
        }
    }

    static /* synthetic */ void lambda$null$42(HashMap hashMap, int i, MessageMedia messageMedia, int i2) {
        for (Entry value : hashMap.entrySet()) {
            MessageObject messageObject = (MessageObject) value.getValue();
            SendMessagesHelper.getInstance(i).sendMessage(messageMedia, messageObject.getDialogId(), messageObject, null, null);
        }
    }

    private String getStringForLanguageAlert(HashMap<String, String> hashMap, String str, int i) {
        String str2 = (String) hashMap.get(str);
        return str2 == null ? LocaleController.getString(str, i) : str2;
    }

    private void checkFreeDiscSpace() {
        if (VERSION.SDK_INT < 26) {
            Utilities.globalQueue.postRunnable(new C1575-$$Lambda$LaunchActivity$OhtB5MFVTEjvucCdsB03AKPPH2Y(this), 2000);
        }
    }

    public /* synthetic */ void lambda$checkFreeDiscSpace$48$LaunchActivity() {
        String str = "last_space_check";
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            try {
                SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                if (Math.abs(globalMainSettings.getLong(str, 0) - System.currentTimeMillis()) >= 259200000) {
                    File directory = FileLoader.getDirectory(4);
                    if (directory != null) {
                        long abs;
                        StatFs statFs = new StatFs(directory.getAbsolutePath());
                        if (VERSION.SDK_INT < 18) {
                            abs = (long) Math.abs(statFs.getAvailableBlocks() * statFs.getBlockSize());
                        } else {
                            abs = statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
                        }
                        if (abs < 104857600) {
                            globalMainSettings.edit().putLong(str, System.currentTimeMillis()).commit();
                            AndroidUtilities.runOnUIThread(new C1579-$$Lambda$LaunchActivity$XotpJadOtesdoG_ogV2rSalfs0Y(this));
                        }
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    public /* synthetic */ void lambda$null$47$LaunchActivity() {
        try {
            AlertsCreator.createFreeSpaceDialog(this).show();
        } catch (Throwable unused) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0056 A:{Catch:{ Exception -> 0x0115 }} */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0054 A:{Catch:{ Exception -> 0x0115 }} */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x005f A:{Catch:{ Exception -> 0x0115 }} */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x005c A:{Catch:{ Exception -> 0x0115 }} */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0065 A:{Catch:{ Exception -> 0x0115 }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0064 A:{Catch:{ Exception -> 0x0115 }} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006d A:{Catch:{ Exception -> 0x0115 }} */
    private void showLanguageAlertInternal(org.telegram.messenger.LocaleController.LocaleInfo r17, org.telegram.messenger.LocaleController.LocaleInfo r18, java.lang.String r19) {
        /*
        r16 = this;
        r1 = r16;
        r0 = "ChooseYourLanguageOther";
        r2 = "ChooseYourLanguage";
        r3 = 0;
        r1.loadingLocaleDialog = r3;	 Catch:{ Exception -> 0x0115 }
        r4 = r17;
        r5 = r4.builtIn;	 Catch:{ Exception -> 0x0115 }
        r6 = 1;
        if (r5 != 0) goto L_0x001d;
    L_0x0010:
        r5 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x0115 }
        r5 = r5.isCurrentLocalLocale();	 Catch:{ Exception -> 0x0115 }
        if (r5 == 0) goto L_0x001b;
    L_0x001a:
        goto L_0x001d;
    L_0x001b:
        r5 = 0;
        goto L_0x001e;
    L_0x001d:
        r5 = 1;
    L_0x001e:
        r7 = new org.telegram.ui.ActionBar.AlertDialog$Builder;	 Catch:{ Exception -> 0x0115 }
        r7.<init>(r1);	 Catch:{ Exception -> 0x0115 }
        r8 = r1.systemLocaleStrings;	 Catch:{ Exception -> 0x0115 }
        r9 = 2131559098; // 0x7f0d02ba float:1.874353E38 double:1.0531301224E-314;
        r8 = r1.getStringForLanguageAlert(r8, r2, r9);	 Catch:{ Exception -> 0x0115 }
        r7.setTitle(r8);	 Catch:{ Exception -> 0x0115 }
        r8 = r1.englishLocaleStrings;	 Catch:{ Exception -> 0x0115 }
        r2 = r1.getStringForLanguageAlert(r8, r2, r9);	 Catch:{ Exception -> 0x0115 }
        r7.setSubtitle(r2);	 Catch:{ Exception -> 0x0115 }
        r2 = new android.widget.LinearLayout;	 Catch:{ Exception -> 0x0115 }
        r2.<init>(r1);	 Catch:{ Exception -> 0x0115 }
        r2.setOrientation(r6);	 Catch:{ Exception -> 0x0115 }
        r8 = 2;
        r9 = new org.telegram.p004ui.Cells.LanguageCell[r8];	 Catch:{ Exception -> 0x0115 }
        r10 = new org.telegram.messenger.LocaleController.LocaleInfo[r6];	 Catch:{ Exception -> 0x0115 }
        r11 = new org.telegram.messenger.LocaleController.LocaleInfo[r8];	 Catch:{ Exception -> 0x0115 }
        r12 = r1.systemLocaleStrings;	 Catch:{ Exception -> 0x0115 }
        r13 = "English";
        r14 = 2131559365; // 0x7f0d03c5 float:1.8744072E38 double:1.0531302543E-314;
        r12 = r1.getStringForLanguageAlert(r12, r13, r14);	 Catch:{ Exception -> 0x0115 }
        if (r5 == 0) goto L_0x0056;
    L_0x0054:
        r13 = r4;
        goto L_0x0058;
    L_0x0056:
        r13 = r18;
    L_0x0058:
        r11[r3] = r13;	 Catch:{ Exception -> 0x0115 }
        if (r5 == 0) goto L_0x005f;
    L_0x005c:
        r13 = r18;
        goto L_0x0060;
    L_0x005f:
        r13 = r4;
    L_0x0060:
        r11[r6] = r13;	 Catch:{ Exception -> 0x0115 }
        if (r5 == 0) goto L_0x0065;
    L_0x0064:
        goto L_0x0067;
    L_0x0065:
        r4 = r18;
    L_0x0067:
        r10[r3] = r4;	 Catch:{ Exception -> 0x0115 }
        r4 = 0;
    L_0x006a:
        r13 = -1;
        if (r4 >= r8) goto L_0x00bf;
    L_0x006d:
        r14 = new org.telegram.ui.Cells.LanguageCell;	 Catch:{ Exception -> 0x0115 }
        r14.<init>(r1, r6);	 Catch:{ Exception -> 0x0115 }
        r9[r4] = r14;	 Catch:{ Exception -> 0x0115 }
        r14 = r9[r4];	 Catch:{ Exception -> 0x0115 }
        r15 = r11[r4];	 Catch:{ Exception -> 0x0115 }
        r3 = r11[r4];	 Catch:{ Exception -> 0x0115 }
        r5 = r18;
        if (r3 != r5) goto L_0x0080;
    L_0x007e:
        r3 = r12;
        goto L_0x0081;
    L_0x0080:
        r3 = 0;
    L_0x0081:
        r14.setLanguage(r15, r3, r6);	 Catch:{ Exception -> 0x0115 }
        r3 = r9[r4];	 Catch:{ Exception -> 0x0115 }
        r14 = java.lang.Integer.valueOf(r4);	 Catch:{ Exception -> 0x0115 }
        r3.setTag(r14);	 Catch:{ Exception -> 0x0115 }
        r3 = r9[r4];	 Catch:{ Exception -> 0x0115 }
        r14 = "dialogButtonSelector";
        r14 = org.telegram.p004ui.ActionBar.Theme.getColor(r14);	 Catch:{ Exception -> 0x0115 }
        r14 = org.telegram.p004ui.ActionBar.Theme.createSelectorDrawable(r14, r8);	 Catch:{ Exception -> 0x0115 }
        r3.setBackgroundDrawable(r14);	 Catch:{ Exception -> 0x0115 }
        r3 = r9[r4];	 Catch:{ Exception -> 0x0115 }
        if (r4 != 0) goto L_0x00a2;
    L_0x00a0:
        r14 = 1;
        goto L_0x00a3;
    L_0x00a2:
        r14 = 0;
    L_0x00a3:
        r3.setLanguageSelected(r14);	 Catch:{ Exception -> 0x0115 }
        r3 = r9[r4];	 Catch:{ Exception -> 0x0115 }
        r14 = 50;
        r13 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r13, r14);	 Catch:{ Exception -> 0x0115 }
        r2.addView(r3, r13);	 Catch:{ Exception -> 0x0115 }
        r3 = r9[r4];	 Catch:{ Exception -> 0x0115 }
        r13 = new org.telegram.ui.-$$Lambda$LaunchActivity$xW-6R4h9aa4jorowKHJF_yJiIQM;	 Catch:{ Exception -> 0x0115 }
        r13.<init>(r10, r9);	 Catch:{ Exception -> 0x0115 }
        r3.setOnClickListener(r13);	 Catch:{ Exception -> 0x0115 }
        r4 = r4 + 1;
        r3 = 0;
        goto L_0x006a;
    L_0x00bf:
        r3 = new org.telegram.ui.Cells.LanguageCell;	 Catch:{ Exception -> 0x0115 }
        r3.<init>(r1, r6);	 Catch:{ Exception -> 0x0115 }
        r4 = r1.systemLocaleStrings;	 Catch:{ Exception -> 0x0115 }
        r5 = 2131559099; // 0x7f0d02bb float:1.8743532E38 double:1.053130123E-314;
        r4 = r1.getStringForLanguageAlert(r4, r0, r5);	 Catch:{ Exception -> 0x0115 }
        r6 = r1.englishLocaleStrings;	 Catch:{ Exception -> 0x0115 }
        r0 = r1.getStringForLanguageAlert(r6, r0, r5);	 Catch:{ Exception -> 0x0115 }
        r3.setValue(r4, r0);	 Catch:{ Exception -> 0x0115 }
        r0 = new org.telegram.ui.-$$Lambda$LaunchActivity$H9lUoWlciprQBKCCa3uBhilp70I;	 Catch:{ Exception -> 0x0115 }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0115 }
        r3.setOnClickListener(r0);	 Catch:{ Exception -> 0x0115 }
        r0 = 50;
        r0 = org.telegram.p004ui.Components.LayoutHelper.createLinear(r13, r0);	 Catch:{ Exception -> 0x0115 }
        r2.addView(r3, r0);	 Catch:{ Exception -> 0x0115 }
        r7.setView(r2);	 Catch:{ Exception -> 0x0115 }
        r0 = "OK";
        r2 = 2131560097; // 0x7f0d06a1 float:1.8745557E38 double:1.053130616E-314;
        r0 = org.telegram.messenger.LocaleController.getString(r0, r2);	 Catch:{ Exception -> 0x0115 }
        r2 = new org.telegram.ui.-$$Lambda$LaunchActivity$-_OYvDrFInFakn0WORCpq62kH08;	 Catch:{ Exception -> 0x0115 }
        r2.<init>(r1, r10);	 Catch:{ Exception -> 0x0115 }
        r7.setNegativeButton(r0, r2);	 Catch:{ Exception -> 0x0115 }
        r0 = r1.showAlertDialog(r7);	 Catch:{ Exception -> 0x0115 }
        r1.localeDialog = r0;	 Catch:{ Exception -> 0x0115 }
        r0 = org.telegram.messenger.MessagesController.getGlobalMainSettings();	 Catch:{ Exception -> 0x0115 }
        r0 = r0.edit();	 Catch:{ Exception -> 0x0115 }
        r2 = "language_showed2";
        r3 = r19;
        r0 = r0.putString(r2, r3);	 Catch:{ Exception -> 0x0115 }
        r0.commit();	 Catch:{ Exception -> 0x0115 }
        goto L_0x0119;
    L_0x0115:
        r0 = move-exception;
        org.telegram.messenger.FileLog.m30e(r0);
    L_0x0119:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.showLanguageAlertInternal(org.telegram.messenger.LocaleController$LocaleInfo, org.telegram.messenger.LocaleController$LocaleInfo, java.lang.String):void");
    }

    static /* synthetic */ void lambda$showLanguageAlertInternal$49(LocaleInfo[] localeInfoArr, LanguageCell[] languageCellArr, View view) {
        Integer num = (Integer) view.getTag();
        localeInfoArr[0] = ((LanguageCell) view).getCurrentLocale();
        int i = 0;
        while (i < languageCellArr.length) {
            languageCellArr[i].setLanguageSelected(i == num.intValue());
            i++;
        }
    }

    public /* synthetic */ void lambda$showLanguageAlertInternal$50$LaunchActivity(View view) {
        this.localeDialog = null;
        this.drawerLayoutContainer.closeDrawer(true);
        lambda$runLinkRequest$27$LaunchActivity(new LanguageSelectActivity());
        AlertDialog alertDialog = this.visibleDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.visibleDialog = null;
        }
    }

    public /* synthetic */ void lambda$showLanguageAlertInternal$51$LaunchActivity(LocaleInfo[] localeInfoArr, DialogInterface dialogInterface, int i) {
        LocaleController.getInstance().applyLanguage(localeInfoArr[0], true, false, this.currentAccount);
        rebuildAllFragments(true);
    }

    private void showLanguageAlert(boolean z) {
        String str = "ChangeLanguageLater";
        String str2 = "ChooseYourLanguageOther";
        String str3 = "ChooseYourLanguage";
        String str4 = "English";
        String str5 = "-";
        try {
            if (!this.loadingLocaleDialog) {
                if (!ApplicationLoader.mainInterfacePaused) {
                    String string = MessagesController.getGlobalMainSettings().getString("language_showed2", "");
                    String str6 = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
                    if (z || !string.equals(str6)) {
                        LocaleInfo[] localeInfoArr = new LocaleInfo[2];
                        Object obj = str6.contains(str5) ? str6.split(str5)[0] : str6;
                        Object obj2 = "in".equals(obj) ? "id" : "iw".equals(obj) ? "he" : "jw".equals(obj) ? "jv" : null;
                        for (int i = 0; i < LocaleController.getInstance().languages.size(); i++) {
                            LocaleInfo localeInfo = (LocaleInfo) LocaleController.getInstance().languages.get(i);
                            if (localeInfo.shortName.equals("en")) {
                                localeInfoArr[0] = localeInfo;
                            }
                            if (localeInfo.shortName.replace("_", str5).equals(str6) || localeInfo.shortName.equals(obj) || localeInfo.shortName.equals(obj2)) {
                                localeInfoArr[1] = localeInfo;
                            }
                            if (localeInfoArr[0] != null && localeInfoArr[1] != null) {
                                break;
                            }
                        }
                        if (!(localeInfoArr[0] == null || localeInfoArr[1] == null)) {
                            if (localeInfoArr[0] != localeInfoArr[1]) {
                                if (BuildVars.LOGS_ENABLED) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("show lang alert for ");
                                    stringBuilder.append(localeInfoArr[0].getKey());
                                    stringBuilder.append(" and ");
                                    stringBuilder.append(localeInfoArr[1].getKey());
                                    FileLog.m27d(stringBuilder.toString());
                                }
                                this.systemLocaleStrings = null;
                                this.englishLocaleStrings = null;
                                this.loadingLocaleDialog = true;
                                TL_langpack_getStrings tL_langpack_getStrings = new TL_langpack_getStrings();
                                tL_langpack_getStrings.lang_code = localeInfoArr[1].getLangCode();
                                tL_langpack_getStrings.keys.add(str4);
                                tL_langpack_getStrings.keys.add(str3);
                                tL_langpack_getStrings.keys.add(str2);
                                tL_langpack_getStrings.keys.add(str);
                                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_langpack_getStrings, new C3726-$$Lambda$LaunchActivity$zUSUlC8LSla6kKMHoMSEb1G2TkE(this, localeInfoArr, str6), 8);
                                tL_langpack_getStrings = new TL_langpack_getStrings();
                                tL_langpack_getStrings.lang_code = localeInfoArr[0].getLangCode();
                                tL_langpack_getStrings.keys.add(str4);
                                tL_langpack_getStrings.keys.add(str3);
                                tL_langpack_getStrings.keys.add(str2);
                                tL_langpack_getStrings.keys.add(str);
                                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_langpack_getStrings, new C3706-$$Lambda$LaunchActivity$3j0ynD-14Ne6162eLDw5z0libqA(this, localeInfoArr, str6), 8);
                            }
                        }
                    } else {
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("alert already showed for ");
                            stringBuilder2.append(string);
                            FileLog.m27d(stringBuilder2.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public /* synthetic */ void lambda$showLanguageAlert$53$LaunchActivity(LocaleInfo[] localeInfoArr, String str, TLObject tLObject, TL_error tL_error) {
        HashMap hashMap = new HashMap();
        if (tLObject != null) {
            Vector vector = (Vector) tLObject;
            for (int i = 0; i < vector.objects.size(); i++) {
                LangPackString langPackString = (LangPackString) vector.objects.get(i);
                hashMap.put(langPackString.key, langPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new C1583-$$Lambda$LaunchActivity$_sPBUeq_K9hirrefOHaOkEfYGN8(this, hashMap, localeInfoArr, str));
    }

    public /* synthetic */ void lambda$null$52$LaunchActivity(HashMap hashMap, LocaleInfo[] localeInfoArr, String str) {
        this.systemLocaleStrings = hashMap;
        if (this.englishLocaleStrings != null && this.systemLocaleStrings != null) {
            showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
        }
    }

    public /* synthetic */ void lambda$showLanguageAlert$55$LaunchActivity(LocaleInfo[] localeInfoArr, String str, TLObject tLObject, TL_error tL_error) {
        HashMap hashMap = new HashMap();
        if (tLObject != null) {
            Vector vector = (Vector) tLObject;
            for (int i = 0; i < vector.objects.size(); i++) {
                LangPackString langPackString = (LangPackString) vector.objects.get(i);
                hashMap.put(langPackString.key, langPackString.value);
            }
        }
        AndroidUtilities.runOnUIThread(new C1589-$$Lambda$LaunchActivity$mWoCWWLsHIOPRrn56P68AHjatJk(this, hashMap, localeInfoArr, str));
    }

    public /* synthetic */ void lambda$null$54$LaunchActivity(HashMap hashMap, LocaleInfo[] localeInfoArr, String str) {
        this.englishLocaleStrings = hashMap;
        if (this.englishLocaleStrings != null && this.systemLocaleStrings != null) {
            showLanguageAlertInternal(localeInfoArr[1], localeInfoArr[0], str);
        }
    }

    private void onPasscodePause() {
        Runnable runnable = this.lockRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.lockRunnable = null;
        }
        if (SharedConfig.passcodeHash.length() != 0) {
            SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            this.lockRunnable = new C30655();
            if (SharedConfig.appLocked) {
                AndroidUtilities.runOnUIThread(this.lockRunnable, 1000);
            } else {
                int i = SharedConfig.autoLockIn;
                if (i != 0) {
                    AndroidUtilities.runOnUIThread(this.lockRunnable, (((long) i) * 1000) + 1000);
                }
            }
        } else {
            SharedConfig.lastPauseTime = 0;
        }
        SharedConfig.saveConfig();
    }

    private void onPasscodeResume() {
        Runnable runnable = this.lockRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.lockRunnable = null;
        }
        if (AndroidUtilities.needShowPasscode(true)) {
            showPasscodeActivity();
        }
        if (SharedConfig.lastPauseTime != 0) {
            SharedConfig.lastPauseTime = 0;
            SharedConfig.saveConfig();
        }
    }

    private void updateCurrentConnectionState(int i) {
        if (this.actionBarLayout != null) {
            String str;
            i = 0;
            this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            int i2 = this.currentConnectionState;
            Runnable runnable = null;
            if (i2 == 2) {
                i = C1067R.string.WaitingForNetwork;
                str = "WaitingForNetwork";
            } else if (i2 == 5) {
                i = C1067R.string.Updating;
                str = "Updating";
            } else if (i2 == 4) {
                i = C1067R.string.ConnectingToProxy;
                str = "ConnectingToProxy";
            } else if (i2 == 1) {
                i = C1067R.string.Connecting;
                str = "Connecting";
            } else {
                str = null;
            }
            int i3 = this.currentConnectionState;
            if (i3 == 1 || i3 == 4) {
                runnable = new C1594-$$Lambda$LaunchActivity$zB6R9P65ZPbtKEyhZOKuv4q935o(this);
            }
            this.actionBarLayout.setTitleOverlayText(str, i, runnable);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:? A:{SYNTHETIC, RETURN, SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0039  */
    public /* synthetic */ void lambda$updateCurrentConnectionState$56$LaunchActivity() {
        /*
        r2 = this;
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x001d;
    L_0x0006:
        r0 = layerFragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0034;
    L_0x000e:
        r0 = layerFragmentsStack;
        r1 = r0.size();
        r1 = r1 + -1;
        r0 = r0.get(r1);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        goto L_0x0035;
    L_0x001d:
        r0 = mainFragmentsStack;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0034;
    L_0x0025:
        r0 = mainFragmentsStack;
        r1 = r0.size();
        r1 = r1 + -1;
        r0 = r0.get(r1);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        goto L_0x0035;
    L_0x0034:
        r0 = 0;
    L_0x0035:
        r1 = r0 instanceof org.telegram.p004ui.ProxyListActivity;
        if (r1 != 0) goto L_0x0046;
    L_0x0039:
        r0 = r0 instanceof org.telegram.p004ui.ProxySettingsActivity;
        if (r0 == 0) goto L_0x003e;
    L_0x003d:
        goto L_0x0046;
    L_0x003e:
        r0 = new org.telegram.ui.ProxyListActivity;
        r0.<init>();
        r2.lambda$runLinkRequest$27$LaunchActivity(r0);
    L_0x0046:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.lambda$updateCurrentConnectionState$56$LaunchActivity():void");
    }

    public void hideVisibleActionMode() {
        ActionMode actionMode = this.visibleActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle bundle) {
        try {
            super.onSaveInstanceState(bundle);
            BaseFragment baseFragment = null;
            if (AndroidUtilities.isTablet()) {
                if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    baseFragment = (BaseFragment) this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    baseFragment = (BaseFragment) this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                    baseFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                }
            } else if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                baseFragment = (BaseFragment) this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
            }
            if (baseFragment != null) {
                Bundle arguments = baseFragment.getArguments();
                String str = "args";
                String str2 = "fragment";
                if ((baseFragment instanceof ChatActivity) && arguments != null) {
                    bundle.putBundle(str, arguments);
                    bundle.putString(str2, "chat");
                } else if (baseFragment instanceof SettingsActivity) {
                    bundle.putString(str2, "settings");
                } else if ((baseFragment instanceof GroupCreateFinalActivity) && arguments != null) {
                    bundle.putBundle(str, arguments);
                    bundle.putString(str2, "group");
                } else if (baseFragment instanceof WallpapersListActivity) {
                    bundle.putString(str2, "wallpapers");
                } else if ((baseFragment instanceof ProfileActivity) && ((ProfileActivity) baseFragment).isChat() && arguments != null) {
                    bundle.putBundle(str, arguments);
                    bundle.putString(str2, "chat_profile");
                } else if ((baseFragment instanceof ChannelCreateActivity) && arguments != null && arguments.getInt("step") == 0) {
                    bundle.putBundle(str, arguments);
                    bundle.putString(str2, "channel");
                }
                baseFragment.saveSelfArgs(bundle);
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
    }

    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            finish();
            return;
        }
        boolean z = false;
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
        } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
        } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
        } else if (!AndroidUtilities.isTablet()) {
            this.actionBarLayout.onBackPressed();
        } else if (this.layersActionBarLayout.getVisibility() == 0) {
            this.layersActionBarLayout.onBackPressed();
        } else {
            if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                ArrayList arrayList = this.rightActionBarLayout.fragmentsStack;
                z = ((BaseFragment) arrayList.get(arrayList.size() - 1)).onBackPressed() ^ 1;
            }
            if (!z) {
                this.actionBarLayout.onBackPressed();
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.actionBarLayout.onLowMemory();
        if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onLowMemory();
            this.layersActionBarLayout.onLowMemory();
        }
    }

    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.visibleActionMode = actionMode;
        try {
            Menu menu = actionMode.getMenu();
            if (!(menu == null || this.actionBarLayout.extendActionMode(menu) || !AndroidUtilities.isTablet() || this.rightActionBarLayout.extendActionMode(menu))) {
                this.layersActionBarLayout.extendActionMode(menu);
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        if (VERSION.SDK_INT < 23 || actionMode.getType() != 1) {
            this.actionBarLayout.onActionModeStarted(actionMode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeStarted(actionMode);
                this.layersActionBarLayout.onActionModeStarted(actionMode);
            }
        }
    }

    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        if (this.visibleActionMode == actionMode) {
            this.visibleActionMode = null;
        }
        if (VERSION.SDK_INT < 23 || actionMode.getType() != 1) {
            this.actionBarLayout.onActionModeFinished(actionMode);
            if (AndroidUtilities.isTablet()) {
                this.rightActionBarLayout.onActionModeFinished(actionMode);
                this.layersActionBarLayout.onActionModeFinished(actionMode);
            }
        }
    }

    public boolean onPreIme() {
        if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
            return true;
        } else if (!ArticleViewer.hasInstance() || !ArticleViewer.getInstance().isVisible()) {
            return false;
        } else {
            ArticleViewer.getInstance().close(true, false);
            return true;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        keyEvent.getKeyCode();
        if (!mainFragmentsStack.isEmpty() && (!(PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) && keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0 && (keyEvent.getKeyCode() == 24 || keyEvent.getKeyCode() == 25))) {
            ArrayList arrayList = mainFragmentsStack;
            BaseFragment baseFragment = (BaseFragment) arrayList.get(arrayList.size() - 1);
            if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).maybePlayVisibleVideo()) {
                return true;
            }
            if (AndroidUtilities.isTablet() && !rightFragmentsStack.isEmpty()) {
                arrayList = rightFragmentsStack;
                baseFragment = (BaseFragment) arrayList.get(arrayList.size() - 1);
                if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).maybePlayVisibleVideo()) {
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
            if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                return super.onKeyUp(i, keyEvent);
            }
            if (AndroidUtilities.isTablet()) {
                if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                    this.layersActionBarLayout.onKeyUp(i, keyEvent);
                } else if (this.rightActionBarLayout.getVisibility() != 0 || this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                    this.actionBarLayout.onKeyUp(i, keyEvent);
                } else {
                    this.rightActionBarLayout.onKeyUp(i, keyEvent);
                }
            } else if (this.actionBarLayout.fragmentsStack.size() != 1) {
                this.actionBarLayout.onKeyUp(i, keyEvent);
            } else if (this.drawerLayoutContainer.isDrawerOpened()) {
                this.drawerLayoutContainer.closeDrawer(false);
            } else {
                if (getCurrentFocus() != null) {
                    AndroidUtilities.hideKeyboard(getCurrentFocus());
                }
                this.drawerLayoutContainer.openDrawer(false);
            }
        }
        return super.onKeyUp(i, keyEvent);
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x00b1 A:{LOOP_START, LOOP:0: B:47:0x00b1->B:49:0x00bc} */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00d3  */
    public boolean needPresentFragment(org.telegram.p004ui.ActionBar.BaseFragment r12, boolean r13, boolean r14, org.telegram.p004ui.ActionBar.ActionBarLayout r15) {
        /*
        r11 = this;
        r0 = org.telegram.p004ui.ArticleViewer.hasInstance();
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x0019;
    L_0x0008:
        r0 = org.telegram.p004ui.ArticleViewer.getInstance();
        r0 = r0.isVisible();
        if (r0 == 0) goto L_0x0019;
    L_0x0012:
        r0 = org.telegram.p004ui.ArticleViewer.getInstance();
        r0.close(r2, r1);
    L_0x0019:
        r0 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r0 == 0) goto L_0x01dd;
    L_0x001f:
        r0 = r11.drawerLayoutContainer;
        r3 = r12 instanceof org.telegram.p004ui.LoginActivity;
        if (r3 != 0) goto L_0x0033;
    L_0x0025:
        r4 = r12 instanceof org.telegram.p004ui.CountrySelectActivity;
        if (r4 != 0) goto L_0x0033;
    L_0x0029:
        r4 = r11.layersActionBarLayout;
        r4 = r4.getVisibility();
        if (r4 == 0) goto L_0x0033;
    L_0x0031:
        r4 = 1;
        goto L_0x0034;
    L_0x0033:
        r4 = 0;
    L_0x0034:
        r0.setAllowOpenDrawer(r4, r1);
        r0 = r12 instanceof org.telegram.p004ui.DialogsActivity;
        r4 = 8;
        if (r0 == 0) goto L_0x007f;
    L_0x003d:
        r0 = r12;
        r0 = (org.telegram.p004ui.DialogsActivity) r0;
        r0 = r0.isMainDialogList();
        if (r0 == 0) goto L_0x007f;
    L_0x0046:
        r0 = r11.actionBarLayout;
        if (r15 == r0) goto L_0x007f;
    L_0x004a:
        r0.removeAllFragments();
        r5 = r11.actionBarLayout;
        r9 = 0;
        r10 = 0;
        r6 = r12;
        r7 = r13;
        r8 = r14;
        r5.presentFragment(r6, r7, r8, r9, r10);
        r12 = r11.layersActionBarLayout;
        r12.removeAllFragments();
        r12 = r11.layersActionBarLayout;
        r12.setVisibility(r4);
        r12 = r11.drawerLayoutContainer;
        r12.setAllowOpenDrawer(r1, r2);
        r12 = r11.tabletFullSize;
        if (r12 != 0) goto L_0x007e;
    L_0x006a:
        r12 = r11.shadowTabletSide;
        r12.setVisibility(r2);
        r12 = r11.rightActionBarLayout;
        r12 = r12.fragmentsStack;
        r12 = r12.isEmpty();
        if (r12 == 0) goto L_0x007e;
    L_0x0079:
        r12 = r11.backgroundTablet;
        r12.setVisibility(r2);
    L_0x007e:
        return r2;
    L_0x007f:
        r0 = r12 instanceof org.telegram.p004ui.ChatActivity;
        if (r0 == 0) goto L_0x01ac;
    L_0x0083:
        r0 = r11.tabletFullSize;
        if (r0 != 0) goto L_0x008b;
    L_0x0087:
        r0 = r11.rightActionBarLayout;
        if (r15 == r0) goto L_0x0093;
    L_0x008b:
        r0 = r11.tabletFullSize;
        if (r0 == 0) goto L_0x00de;
    L_0x008f:
        r0 = r11.actionBarLayout;
        if (r15 != r0) goto L_0x00de;
    L_0x0093:
        r13 = r11.tabletFullSize;
        if (r13 == 0) goto L_0x00a6;
    L_0x0097:
        r13 = r11.actionBarLayout;
        if (r15 != r13) goto L_0x00a6;
    L_0x009b:
        r13 = r13.fragmentsStack;
        r13 = r13.size();
        if (r13 == r1) goto L_0x00a4;
    L_0x00a3:
        goto L_0x00a6;
    L_0x00a4:
        r13 = 0;
        goto L_0x00a7;
    L_0x00a6:
        r13 = 1;
    L_0x00a7:
        r15 = r11.layersActionBarLayout;
        r15 = r15.fragmentsStack;
        r15 = r15.isEmpty();
        if (r15 != 0) goto L_0x00d1;
    L_0x00b1:
        r15 = r11.layersActionBarLayout;
        r15 = r15.fragmentsStack;
        r15 = r15.size();
        r15 = r15 - r1;
        if (r15 <= 0) goto L_0x00ca;
    L_0x00bc:
        r15 = r11.layersActionBarLayout;
        r0 = r15.fragmentsStack;
        r0 = r0.get(r2);
        r0 = (org.telegram.p004ui.ActionBar.BaseFragment) r0;
        r15.removeFragmentFromStack(r0);
        goto L_0x00b1;
    L_0x00ca:
        r15 = r11.layersActionBarLayout;
        r0 = r14 ^ 1;
        r15.closeLastFragment(r0);
    L_0x00d1:
        if (r13 != 0) goto L_0x00dd;
    L_0x00d3:
        r1 = r11.actionBarLayout;
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r2 = r12;
        r4 = r14;
        r1.presentFragment(r2, r3, r4, r5, r6);
    L_0x00dd:
        return r13;
    L_0x00de:
        r0 = r11.tabletFullSize;
        if (r0 != 0) goto L_0x0128;
    L_0x00e2:
        r0 = r11.rightActionBarLayout;
        if (r15 == r0) goto L_0x0128;
    L_0x00e6:
        r0.setVisibility(r2);
        r15 = r11.backgroundTablet;
        r15.setVisibility(r4);
        r15 = r11.rightActionBarLayout;
        r15.removeAllFragments();
        r3 = r11.rightActionBarLayout;
        r6 = 1;
        r7 = 0;
        r8 = 0;
        r4 = r12;
        r5 = r13;
        r3.presentFragment(r4, r5, r6, r7, r8);
        r12 = r11.layersActionBarLayout;
        r12 = r12.fragmentsStack;
        r12 = r12.isEmpty();
        if (r12 != 0) goto L_0x0127;
    L_0x0107:
        r12 = r11.layersActionBarLayout;
        r12 = r12.fragmentsStack;
        r12 = r12.size();
        r12 = r12 - r1;
        if (r12 <= 0) goto L_0x0120;
    L_0x0112:
        r12 = r11.layersActionBarLayout;
        r13 = r12.fragmentsStack;
        r13 = r13.get(r2);
        r13 = (org.telegram.p004ui.ActionBar.BaseFragment) r13;
        r12.removeFragmentFromStack(r13);
        goto L_0x0107;
    L_0x0120:
        r12 = r11.layersActionBarLayout;
        r13 = r14 ^ 1;
        r12.closeLastFragment(r13);
    L_0x0127:
        return r2;
    L_0x0128:
        r13 = r11.tabletFullSize;
        if (r13 == 0) goto L_0x016d;
    L_0x012c:
        r3 = r11.actionBarLayout;
        if (r15 == r3) goto L_0x016d;
    L_0x0130:
        r13 = r3.fragmentsStack;
        r13 = r13.size();
        if (r13 <= r1) goto L_0x013a;
    L_0x0138:
        r5 = 1;
        goto L_0x013b;
    L_0x013a:
        r5 = 0;
    L_0x013b:
        r7 = 0;
        r8 = 0;
        r4 = r12;
        r6 = r14;
        r3.presentFragment(r4, r5, r6, r7, r8);
        r12 = r11.layersActionBarLayout;
        r12 = r12.fragmentsStack;
        r12 = r12.isEmpty();
        if (r12 != 0) goto L_0x016c;
    L_0x014c:
        r12 = r11.layersActionBarLayout;
        r12 = r12.fragmentsStack;
        r12 = r12.size();
        r12 = r12 - r1;
        if (r12 <= 0) goto L_0x0165;
    L_0x0157:
        r12 = r11.layersActionBarLayout;
        r13 = r12.fragmentsStack;
        r13 = r13.get(r2);
        r13 = (org.telegram.p004ui.ActionBar.BaseFragment) r13;
        r12.removeFragmentFromStack(r13);
        goto L_0x014c;
    L_0x0165:
        r12 = r11.layersActionBarLayout;
        r13 = r14 ^ 1;
        r12.closeLastFragment(r13);
    L_0x016c:
        return r2;
    L_0x016d:
        r13 = r11.layersActionBarLayout;
        r13 = r13.fragmentsStack;
        r13 = r13.isEmpty();
        if (r13 != 0) goto L_0x0197;
    L_0x0177:
        r13 = r11.layersActionBarLayout;
        r13 = r13.fragmentsStack;
        r13 = r13.size();
        r13 = r13 - r1;
        if (r13 <= 0) goto L_0x0190;
    L_0x0182:
        r13 = r11.layersActionBarLayout;
        r15 = r13.fragmentsStack;
        r15 = r15.get(r2);
        r15 = (org.telegram.p004ui.ActionBar.BaseFragment) r15;
        r13.removeFragmentFromStack(r15);
        goto L_0x0177;
    L_0x0190:
        r13 = r11.layersActionBarLayout;
        r15 = r14 ^ 1;
        r13.closeLastFragment(r15);
    L_0x0197:
        r3 = r11.actionBarLayout;
        r13 = r3.fragmentsStack;
        r13 = r13.size();
        if (r13 <= r1) goto L_0x01a3;
    L_0x01a1:
        r5 = 1;
        goto L_0x01a4;
    L_0x01a3:
        r5 = 0;
    L_0x01a4:
        r7 = 0;
        r8 = 0;
        r4 = r12;
        r6 = r14;
        r3.presentFragment(r4, r5, r6, r7, r8);
        return r2;
    L_0x01ac:
        r0 = r11.layersActionBarLayout;
        if (r15 == r0) goto L_0x01dc;
    L_0x01b0:
        r0.setVisibility(r2);
        r15 = r11.drawerLayoutContainer;
        r15.setAllowOpenDrawer(r2, r1);
        if (r3 == 0) goto L_0x01ca;
    L_0x01ba:
        r15 = r11.backgroundTablet;
        r15.setVisibility(r2);
        r15 = r11.shadowTabletSide;
        r15.setVisibility(r4);
        r15 = r11.shadowTablet;
        r15.setBackgroundColor(r2);
        goto L_0x01d1;
    L_0x01ca:
        r15 = r11.shadowTablet;
        r0 = 2130706432; // 0x7f000000 float:1.7014118E38 double:1.0527088494E-314;
        r15.setBackgroundColor(r0);
    L_0x01d1:
        r3 = r11.layersActionBarLayout;
        r7 = 0;
        r8 = 0;
        r4 = r12;
        r5 = r13;
        r6 = r14;
        r3.presentFragment(r4, r5, r6, r7, r8);
        return r2;
    L_0x01dc:
        return r1;
    L_0x01dd:
        r13 = r12 instanceof org.telegram.p004ui.LoginActivity;
        if (r13 == 0) goto L_0x01eb;
    L_0x01e1:
        r12 = mainFragmentsStack;
        r12 = r12.size();
        if (r12 != 0) goto L_0x01f8;
    L_0x01e9:
        r12 = 0;
        goto L_0x01f9;
    L_0x01eb:
        r12 = r12 instanceof org.telegram.p004ui.CountrySelectActivity;
        if (r12 == 0) goto L_0x01f8;
    L_0x01ef:
        r12 = mainFragmentsStack;
        r12 = r12.size();
        if (r12 != r1) goto L_0x01f8;
    L_0x01f7:
        goto L_0x01e9;
    L_0x01f8:
        r12 = 1;
    L_0x01f9:
        r13 = r11.drawerLayoutContainer;
        r13.setAllowOpenDrawer(r12, r2);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.p004ui.LaunchActivity.needPresentFragment(org.telegram.ui.ActionBar.BaseFragment, boolean, boolean, org.telegram.ui.ActionBar.ActionBarLayout):boolean");
    }

    public boolean needAddFragmentToStack(BaseFragment baseFragment, ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            DrawerLayoutContainer drawerLayoutContainer = this.drawerLayoutContainer;
            boolean z = baseFragment instanceof LoginActivity;
            boolean z2 = (z || (baseFragment instanceof CountrySelectActivity) || this.layersActionBarLayout.getVisibility() == 0) ? false : true;
            drawerLayoutContainer.setAllowOpenDrawer(z2, true);
            ActionBarLayout actionBarLayout2;
            if (baseFragment instanceof DialogsActivity) {
                if (((DialogsActivity) baseFragment).isMainDialogList()) {
                    actionBarLayout2 = this.actionBarLayout;
                    if (actionBarLayout != actionBarLayout2) {
                        actionBarLayout2.removeAllFragments();
                        this.actionBarLayout.addFragmentToStack(baseFragment);
                        this.layersActionBarLayout.removeAllFragments();
                        this.layersActionBarLayout.setVisibility(8);
                        this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                        if (!this.tabletFullSize) {
                            this.shadowTabletSide.setVisibility(0);
                            if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                                this.backgroundTablet.setVisibility(0);
                            }
                        }
                        return false;
                    }
                }
            } else if (baseFragment instanceof ChatActivity) {
                ActionBarLayout actionBarLayout3;
                if (!this.tabletFullSize) {
                    actionBarLayout2 = this.rightActionBarLayout;
                    if (actionBarLayout != actionBarLayout2) {
                        actionBarLayout2.setVisibility(0);
                        this.backgroundTablet.setVisibility(8);
                        this.rightActionBarLayout.removeAllFragments();
                        this.rightActionBarLayout.addFragmentToStack(baseFragment);
                        if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                            while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                                actionBarLayout3 = this.layersActionBarLayout;
                                actionBarLayout3.removeFragmentFromStack((BaseFragment) actionBarLayout3.fragmentsStack.get(0));
                            }
                            this.layersActionBarLayout.closeLastFragment(true);
                        }
                        return false;
                    }
                }
                if (this.tabletFullSize) {
                    actionBarLayout2 = this.actionBarLayout;
                    if (actionBarLayout != actionBarLayout2) {
                        actionBarLayout2.addFragmentToStack(baseFragment);
                        if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                            while (this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                                actionBarLayout3 = this.layersActionBarLayout;
                                actionBarLayout3.removeFragmentFromStack((BaseFragment) actionBarLayout3.fragmentsStack.get(0));
                            }
                            this.layersActionBarLayout.closeLastFragment(true);
                        }
                        return false;
                    }
                }
            } else {
                actionBarLayout2 = this.layersActionBarLayout;
                if (actionBarLayout != actionBarLayout2) {
                    actionBarLayout2.setVisibility(0);
                    this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
                    if (z) {
                        this.backgroundTablet.setVisibility(0);
                        this.shadowTabletSide.setVisibility(8);
                        this.shadowTablet.setBackgroundColor(0);
                    } else {
                        this.shadowTablet.setBackgroundColor(Theme.ACTION_BAR_PHOTO_VIEWER_COLOR);
                    }
                    this.layersActionBarLayout.addFragmentToStack(baseFragment);
                    return false;
                }
            }
            return true;
        }
        boolean z3;
        if ((baseFragment instanceof LoginActivity) ? mainFragmentsStack.size() != 0 : !((baseFragment instanceof CountrySelectActivity) && mainFragmentsStack.size() == 1)) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.drawerLayoutContainer.setAllowOpenDrawer(z3, false);
        return true;
    }

    public boolean needCloseLastFragment(ActionBarLayout actionBarLayout) {
        if (AndroidUtilities.isTablet()) {
            if (actionBarLayout == this.actionBarLayout && actionBarLayout.fragmentsStack.size() <= 1) {
                onFinish();
                finish();
                return false;
            } else if (actionBarLayout == this.rightActionBarLayout) {
                if (!this.tabletFullSize) {
                    this.backgroundTablet.setVisibility(0);
                }
            } else if (actionBarLayout == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
                onFinish();
                finish();
                return false;
            }
        } else if (actionBarLayout.fragmentsStack.size() <= 1) {
            onFinish();
            finish();
            return false;
        } else if (actionBarLayout.fragmentsStack.size() >= 2 && !(actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity)) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
        }
        return true;
    }

    public void rebuildAllFragments(boolean z) {
        ActionBarLayout actionBarLayout = this.layersActionBarLayout;
        if (actionBarLayout != null) {
            actionBarLayout.rebuildAllFragmentViews(z, z);
        } else {
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
    }

    public void onRebuildAllFragments(ActionBarLayout actionBarLayout, boolean z) {
        if (AndroidUtilities.isTablet() && actionBarLayout == this.layersActionBarLayout) {
            this.rightActionBarLayout.rebuildAllFragmentViews(z, z);
            this.actionBarLayout.rebuildAllFragmentViews(z, z);
        }
        this.drawerLayoutAdapter.notifyDataSetChanged();
    }
}
